/*******************************************************************************
 * Copyright 2018 Adobe
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/* global
    CQ
 */
(function () {
    "use strict";

    var containerUtils = window.CQ && window.CQ.CoreComponents && window.CQ.CoreComponents.container && window.CQ.CoreComponents.container.utils ? window.CQ.CoreComponents.container.utils : undefined;
    if (!containerUtils) {
        // eslint-disable-next-line no-console
        console.warn("Tabs: container utilities at window.CQ.CoreComponents.container.utils are not available. This can lead to missing features. Ensure the core.wcm.components.commons.site.container client library is included on the page.");
    }
    var dataLayerEnabled;
    var dataLayer;

    var NS = "cmp";
    var IS = "tabs";

    var keyCodes = {
        END: 35,
        HOME: 36,
        ARROW_LEFT: 37,
        ARROW_UP: 38,
        ARROW_RIGHT: 39,
        ARROW_DOWN: 40
    };

    var selectors = {
        self: "[data-" + NS + '-is="' + IS + '"]',
        active: {
            tab: "cmp-tabs__tab--active",
            tabpanel: "cmp-tabs__tabpanel--active"
        }
    };

    /**
     * Tabs Configuration
     *
     * @typedef {Object} TabsConfig Represents a Tabs configuration
     * @property {HTMLElement} element The HTMLElement representing the Tabs
     * @property {Object} options The Tabs options
     */

    /**
     * Tabs
     *
     * @class Tabs
     * @classdesc An interactive Tabs component for navigating a list of tabs
     * @param {TabsConfig} config The Tabs configuration
     */
    function Tabs(config) {
        var that = this;

        if (config && config.element) {
            init(config);
        }

        /**
         * Initializes the Tabs
         *
         * @private
         * @param {TabsConfig} config The Tabs configuration
         */
        function init(config) {
            that._config = config;

            // prevents multiple initialization
            config.element.removeAttribute("data-" + NS + "-is");

            cacheElements(config.element);
            initTabScrollWithEvents();
            that._active = getActiveIndex(that._elements["tab"]);

            if (that._elements.tabpanel) {
                refreshActive();
                bindEvents();
            }

            // Show the tab based on deep-link-id if it matches with any existing tab item id
            if (containerUtils) {
                var deepLinkItemIdx = containerUtils.getDeepLinkItemIdx(that, "tab");
                if (deepLinkItemIdx && deepLinkItemIdx !== -1) {
                    var deepLinkItem = that._elements["tab"][deepLinkItemIdx];
                    if (deepLinkItem && that._elements["tab"][that._active].id !== deepLinkItem.id) {
                        navigateAndFocusTab(deepLinkItemIdx);
                    }
                }
            }

            if (window.Granite && window.Granite.author && window.Granite.author.MessageChannel) {
                /*
                 * Editor message handling:
                 * - subscribe to "cmp.panelcontainer" message requests sent by the editor frame
                 * - check that the message data panel container type is correct and that the id (path) matches this specific Tabs component
                 * - if so, route the "navigate" operation to enact a navigation of the Tabs based on index data
                 */
                CQ.CoreComponents.MESSAGE_CHANNEL = CQ.CoreComponents.MESSAGE_CHANNEL || new window.Granite.author.MessageChannel("cqauthor", window);
                CQ.CoreComponents.MESSAGE_CHANNEL.subscribeRequestMessage("cmp.panelcontainer", function (message) {
                    if (message.data && message.data.type === "cmp-tabs" && message.data.id === that._elements.self.dataset["cmpPanelcontainerId"]) {
                        if (message.data.operation === "navigate") {
                            navigate(message.data.index);
                        }
                    }
                });
            }
        }

        /**
         * Returns the index of the active tab, if no tab is active returns 0
         *
         * @param {Array} tabs Tab elements
         * @returns {Number} Index of the active tab, 0 if none is active
         */
        function getActiveIndex(tabs) {
            if (tabs) {
                for (var i = 0; i < tabs.length; i++) {
                    if (tabs[i].classList.contains(selectors.active.tab)) {
                        return i;
                    }
                }
            }
            return 0;
        }

        /**
         * Caches the Tabs elements as defined via the {@code data-tabs-hook="ELEMENT_NAME"} markup API
         *
         * @private
         * @param {HTMLElement} wrapper The Tabs wrapper element
         */
        function cacheElements(wrapper) {
            that._elements = {};
            that._elements.self = wrapper;
            var hooks = that._elements.self.querySelectorAll("[data-" + NS + "-hook-" + IS + "]");

            for (var i = 0; i < hooks.length; i++) {
                var hook = hooks[i];
                if (hook.closest("." + NS + "-" + IS) === that._elements.self) { // only process own tab elements
                    var capitalized = IS;
                    capitalized = capitalized.charAt(0).toUpperCase() + capitalized.slice(1);
                    var key = hook.dataset[NS + "Hook" + capitalized];
                    if (that._elements[key]) {
                        if (!Array.isArray(that._elements[key])) {
                            var tmp = that._elements[key];
                            that._elements[key] = [tmp];
                        }
                        that._elements[key].push(hook);
                    } else {
                        that._elements[key] = hook;
                    }
                }
            }
        }

        /**
        * Checking element is scrollable or not
        *
        * @private
        */
        function isScrollable(ele) {
            // Compare the width to see if the element has scrollable content
            const hasScrollableContent = ele.scrollWidth > ele.clientWidth;

            // It's not enough because the element's `overflow-x` style can be set as
            // * `hidden`
            // * `hidden !important`
            // In those cases, the scrollbar isn't shown
            const overflowXStyle = window.getComputedStyle(ele).overflowX;
            const isOverflowHidden = overflowXStyle.indexOf('hidden') !== -1;

            return hasScrollableContent && !isOverflowHidden;
        };

        /**
         * Init Tabs scroll
         *
         * @private
         */
        function initTabScrollWithEvents() {
            // duration of scroll animation
            var scrollDuration = 300;
            // paddles
            var leftPaddle = document.getElementsByClassName('cmp-tabs__paddle--left');
            var rightPaddle = document.getElementsByClassName('cmp-tabs__paddle--right');

            // get some relevant size for the paddle triggering point
            var paddleMargin = 20;
            // get items dimensions
            var itemsLength = $('.cmp-tabs__tab').length;
            var itemSize = $('.cmp-tabs__tab').outerWidth(true);

            var getMenuWrapperSize = function () {
                return $('.cmp-tabs__tablist').outerWidth();
            }
            var menuWrapperSize = getMenuWrapperSize();

            // get total width of all menu items
            var getMenuSize = function () {
                return itemsLength * itemSize;
            };
            var menuSize = getMenuSize();
            // get how much of menu is invisible
            var menuInvisibleSize = menuSize - menuWrapperSize;

            var getMenuSize = function () {
                return itemsLength * itemSize;
            };

            // get how much have we scrolled to the left
            var getMenuPosition = function () {
                return $('#cmp-tabs').scrollLeft();
            };

            var menuSize = getMenuSize();

            if (!isScrollable($('.cmp-tabs__tablist')[0])) {
                $(leftPaddle).addClass('cmp-tabs__paddle--hidden');
                $(rightPaddle).addClass('cmp-tabs__paddle--hidden');
            }

            $('#cmp-tabs').on('scroll', function () {
                // get how much of menu is invisible
                menuInvisibleSize = menuSize - menuWrapperSize;
                // get how much have we scrolled so far
                var menuPosition = getMenuPosition();

                var menuEndOffset = menuInvisibleSize - paddleMargin;

                // show & hide the paddles 
                // depending on scroll position
                if (menuPosition <= paddleMargin) {
                    $(leftPaddle).addClass('cmp-tabs__paddle--hidden');
                    $(rightPaddle).removeClass('cmp-tabs__paddle--hidden');
                } else if (menuPosition < menuEndOffset) {
                    // show both paddles in the middle
                    $(leftPaddle).removeClass('cmp-tabs__paddle--hidden');
                    $(rightPaddle).removeClass('cmp-tabs__paddle--hidden');
                } else if (menuPosition >= menuEndOffset) {
                    $(leftPaddle).removeClass('cmp-tabs__paddle--hidden');
                    $(rightPaddle).addClass('cmp-tabs__paddle--hidden');
                }
            });


            // scroll to left
            $(".cmp-tabs__paddle--right").on('click', function () {
                var leftPos = $('.cmp-tabs__tablist').scrollLeft();
                $('#cmp-tabs').animate({ scrollLeft: leftPos + 175 }, scrollDuration);
            });

            // scroll to right
            $(".cmp-tabs__paddle--left").on('click', function () {
                var leftPos = $('.cmp-tabs__tablist').scrollLeft();
                $('#cmp-tabs').animate({ scrollLeft: leftPos - 175 }, scrollDuration);
            });
        }

        /**
         * Binds Tabs event handling
         *
         * @private
         */
        function bindEvents() {
            var tabs = that._elements["tab"];
            if (tabs) {
                for (var i = 0; i < tabs.length; i++) {
                    (function (index) {
                        tabs[i].addEventListener("click", function (event) {
                            navigateAndFocusTab(index);
                        });
                        tabs[i].addEventListener("keydown", function (event) {
                            onKeyDown(event);
                        });
                    })(i);
                }
            }

        }

        /**
         * Handles tab keydown events
         *
         * @private
         * @param {Object} event The keydown event
         */
        function onKeyDown(event) {
            var index = that._active;
            var lastIndex = that._elements["tab"].length - 1;

            switch (event.keyCode) {
                case keyCodes.ARROW_LEFT:
                case keyCodes.ARROW_UP:
                    event.preventDefault();
                    if (index > 0) {
                        navigateAndFocusTab(index - 1);
                    }
                    break;
                case keyCodes.ARROW_RIGHT:
                case keyCodes.ARROW_DOWN:
                    event.preventDefault();
                    if (index < lastIndex) {
                        navigateAndFocusTab(index + 1);
                    }
                    break;
                case keyCodes.HOME:
                    event.preventDefault();
                    navigateAndFocusTab(0);
                    break;
                case keyCodes.END:
                    event.preventDefault();
                    navigateAndFocusTab(lastIndex);
                    break;
                default:
                    return;
            }
        }

        /**
         * Refreshes the tab markup based on the current {@code Tabs#_active} index
         *
         * @private
         */
        function refreshActive() {
            var tabpanels = that._elements["tabpanel"];
            var tabs = that._elements["tab"];

            if (tabpanels) {
                if (Array.isArray(tabpanels)) {
                    for (var i = 0; i < tabpanels.length; i++) {
                        if (i === parseInt(that._active)) {
                            tabpanels[i].classList.add(selectors.active.tabpanel);
                            tabpanels[i].removeAttribute("aria-hidden");
                            tabs[i].classList.add(selectors.active.tab);
                            tabs[i].setAttribute("aria-selected", true);
                            tabs[i].setAttribute("tabindex", "0");
                        } else {
                            tabpanels[i].classList.remove(selectors.active.tabpanel);
                            tabpanels[i].setAttribute("aria-hidden", true);
                            tabs[i].classList.remove(selectors.active.tab);
                            tabs[i].setAttribute("aria-selected", false);
                            tabs[i].setAttribute("tabindex", "-1");
                        }
                    }
                } else {
                    // only one tab
                    tabpanels.classList.add(selectors.active.tabpanel);
                    tabs.classList.add(selectors.active.tab);
                }
            }
        }

        /**
         * Focuses the element and prevents scrolling the element into view
         *
         * @param {HTMLElement} element Element to focus
         */
        function focusWithoutScroll(element) {
            var x = window.scrollX || window.pageXOffset;
            var y = window.scrollY || window.pageYOffset;
            element.focus();
            window.scrollTo(x, y);
        }

        /**
         * Navigates to the tab at the provided index
         *
         * @private
         * @param {Number} index The index of the tab to navigate to
         */
        function navigate(index) {
            that._active = index;
            refreshActive();
        }

        /**
         * Navigates to the item at the provided index and ensures the active tab gains focus
         *
         * @private
         * @param {Number} index The index of the item to navigate to
         */
        function navigateAndFocusTab(index) {
            var exActive = that._active;
            navigate(index);
            focusWithoutScroll(that._elements["tab"][index]);

            if (dataLayerEnabled) {

                var activeItem = getDataLayerId(that._elements.tabpanel[index]);
                var exActiveItem = getDataLayerId(that._elements.tabpanel[exActive]);

                dataLayer.push({
                    event: "cmp:show",
                    eventInfo: {
                        path: "component." + activeItem
                    }
                });

                dataLayer.push({
                    event: "cmp:hide",
                    eventInfo: {
                        path: "component." + exActiveItem
                    }
                });

                var tabsId = that._elements.self.id;
                var uploadPayload = { component: {} };
                uploadPayload.component[tabsId] = { shownItems: [activeItem] };

                var removePayload = { component: {} };
                removePayload.component[tabsId] = { shownItems: undefined };

                dataLayer.push(removePayload);
                dataLayer.push(uploadPayload);
            }
        }
    }

    /**
     * Scrolls the browser when the URI fragment is changed to the item of the container Tab component that corresponds to the deep link in the URI fragment,
       and displays its content.
     */
    function onHashChange() {
        if (location.hash && location.hash !== "#") {
            var anchorLocation = decodeURIComponent(location.hash);
            var anchorElement = document.querySelector(anchorLocation);
            if (anchorElement && anchorElement.classList.contains("cmp-tabs__tab") && !anchorElement.classList.contains("cmp-tabs__tab--active")) {
                anchorElement.click();
            }
        }
    }

    /**
     * Reads options data from the Tabs wrapper element, defined via {@code data-cmp-*} data attributes
     *
     * @private
     * @param {HTMLElement} element The Tabs element to read options data from
     * @returns {Object} The options read from the component data attributes
     */
    function readData(element) {
        var data = element.dataset;
        var options = [];
        var capitalized = IS;
        capitalized = capitalized.charAt(0).toUpperCase() + capitalized.slice(1);
        var reserved = ["is", "hook" + capitalized];

        for (var key in data) {
            if (Object.prototype.hasOwnProperty.call(data, key)) {
                var value = data[key];

                if (key.indexOf(NS) === 0) {
                    key = key.slice(NS.length);
                    key = key.charAt(0).toLowerCase() + key.substring(1);

                    if (reserved.indexOf(key) === -1) {
                        options[key] = value;
                    }
                }
            }
        }

        return options;
    }

    /**
     * Parses the dataLayer string and returns the ID
     *
     * @private
     * @param {HTMLElement} item the accordion item
     * @returns {String} dataLayerId or undefined
     */
    function getDataLayerId(item) {
        if (item) {
            if (item.dataset.cmpDataLayer) {
                return Object.keys(JSON.parse(item.dataset.cmpDataLayer))[0];
            } else {
                return item.id;
            }
        }
        return null;
    }

    /**
     * Document ready handler and DOM mutation observers. Initializes Tabs components as necessary.
     *
     * @private
     */
    function onDocumentReady() {
        dataLayerEnabled = document.body.hasAttribute("data-cmp-data-layer-enabled");
        dataLayer = (dataLayerEnabled) ? window.adobeDataLayer = window.adobeDataLayer || [] : undefined;

        var elements = document.querySelectorAll(selectors.self);
        for (var i = 0; i < elements.length; i++) {
            new Tabs({ element: elements[i], options: readData(elements[i]) });
        }

        var MutationObserver = window.MutationObserver || window.WebKitMutationObserver || window.MozMutationObserver;
        var body = document.querySelector("body");
        var observer = new MutationObserver(function (mutations) {
            mutations.forEach(function (mutation) {
                // needed for IE
                var nodesArray = [].slice.call(mutation.addedNodes);
                if (nodesArray.length > 0) {
                    nodesArray.forEach(function (addedNode) {
                        if (addedNode.querySelectorAll) {
                            var elementsArray = [].slice.call(addedNode.querySelectorAll(selectors.self));
                            elementsArray.forEach(function (element) {
                                new Tabs({ element: element, options: readData(element) });
                            });
                        }
                    });
                }
            });
        });

        observer.observe(body, {
            subtree: true,
            childList: true,
            characterData: true
        });
    }

    if (document.readyState !== "loading") {
        onDocumentReady();
    } else {
        document.addEventListener("DOMContentLoaded", onDocumentReady);
    }

    if (containerUtils) {
        window.addEventListener("load", containerUtils.scrollToAnchor, false);
    }
    window.addEventListener("hashchange", onHashChange, false);

}());