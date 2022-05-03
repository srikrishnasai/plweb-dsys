(function () {
    "use strict";

    var UP_ARROW_CLASS = "secondary-nav--up-arrow";

    var CLOSE_ARROW_CLASS = "secondary-nav--close";

    var LIST_HEIGHT = 40;

    var MAX_TABLET_WIDTH = 991;

    var TIME_TO_ADD_DISPLAY_CLASS = 10;

    var ANIMATION_DURATION = 600;

    function showMegaMenu(dropdownElem, button) {
        var maxHeight = $(dropdownElem).find("a").length * LIST_HEIGHT;
        $(dropdownElem).css("max-height", maxHeight + "px");
        $(button).toggleClass(UP_ARROW_CLASS);
    }

    function hideMegaMenu(dropdownElem, button) {
        $(dropdownElem).removeAttr("style");
        $(button).toggleClass(UP_ARROW_CLASS);
    }
    function hideNav(button, nav) {
        $(button).toggleClass(CLOSE_ARROW_CLASS);
        nav.removeClass("secondary-nav--dropdown-show");
        setTimeout(function () {
            nav.removeClass("secondary-nav--dropdown-active");
        }, ANIMATION_DURATION + TIME_TO_ADD_DISPLAY_CLASS);
    }

    function showNav(button, nav) {
        $(button).toggleClass(CLOSE_ARROW_CLASS);
        nav.addClass("secondary-nav--dropdown-active");
        setTimeout(function () {
            nav.addClass("secondary-nav--dropdown-show");
        }, TIME_TO_ADD_DISPLAY_CLASS);
    }

    $(window).on("load", function () {
        $(".header").each(function () {
            var hamburger = $("#hamburger");
            var nav = $(this).find(".secondary-nav__navigation").first();
            hamburger.on("click", function () {
                if (hamburger.hasClass(CLOSE_ARROW_CLASS)) hideNav(this, nav);
                else showNav(this, nav);
            });
        });
        $(".secondary-nav__navigation").each(function () {
            var nav = this;
            $(nav)
                .find("li")
                .each(function () {
                    if ($(document).width() > MAX_TABLET_WIDTH) {
                        var dropdown = $(this).find("#dropdown");
                        $(this).hover(
                            function () {
                                if (!dropdown.hasClass("secondary-nav--dropdown-active")) {
                                    dropdown.addClass("secondary-nav--dropdown-active");
                                    setTimeout(function () {
                                        dropdown.addClass("secondary-nav--dropdown-show");
                                    }, TIME_TO_ADD_DISPLAY_CLASS);
                                }
                            },
                            function () {
                                dropdown.removeClass("secondary-nav--dropdown-show");
                                setTimeout(function () {
                                    dropdown.removeClass("secondary-nav--dropdown-active");
                                }, ANIMATION_DURATION + TIME_TO_ADD_DISPLAY_CLASS);
                            }
                        );
                    } else {
                        var dropdownButton = $(this).find("#dropdownButton");
                        if (dropdownButton) {
                            $(dropdownButton).on("click", function () {
                                var dropdownElem = $(this).parent().parent().find("#dropdown");
                                if (!$(this).hasClass(UP_ARROW_CLASS)) {
                                    showMegaMenu(dropdownElem, this);
                                } else hideMegaMenu(dropdownElem, this);
                            });
                        }
                    }
                });
        });
    });
})();
