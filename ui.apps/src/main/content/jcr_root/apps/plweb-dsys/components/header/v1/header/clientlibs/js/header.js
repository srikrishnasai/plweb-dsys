(function () {
    "use strict";

    var UP_ARROW_CLASS = "secondary-nav--up-arrow";

    var CLOSE_ARROW_CLASS = "secondary-nav--close";

    var LIST_HEIGHT = 40;

    var MAX_TABLET_WIDTH = 991;

    var TIME_TO_ADD_DISPLAY_CLASS = 10;

    var ANIMATION_DURATION = 600;

    var LIST_ID = "#list";

    var ACTIVATE_LIST_CLASS = "activate-list";


    function showMegaMenu(dropdownElem, button) {
        var hasUpArrow = $('.secondary-nav__navigation').find(".secondary-nav--up-arrow");
        if(hasUpArrow.length > 0){
            $('.secondary-nav--up-arrow').parent().parent().find("#dropdown").removeAttr("style");
            $('.secondary-nav__navigation').find(".secondary-nav--up-arrow").removeClass(UP_ARROW_CLASS);
        }
        var maxHeight = $(dropdownElem).find("a").length * LIST_HEIGHT;
        $(dropdownElem).css("max-height", maxHeight + "px");
        $(button).addClass(UP_ARROW_CLASS);
    }

    function hideMegaMenu(dropdownElem, button) {
        $(dropdownElem).removeAttr("style");
        $(button).removeClass(UP_ARROW_CLASS);
    }
    function hideNav(button, nav) {
        
        $(button).toggleClass(CLOSE_ARROW_CLASS);
        nav.removeClass("secondary-nav--dropdown-show");
        setTimeout(function () {
            nav.removeClass("secondary-nav--dropdown-active");
            $(nav).find("li").each(function () {
                var dropdownButton = $(this).find("#dropdownButton");
                var dropdownElem = $(dropdownButton).parent().parent().find("#dropdown");
                hideMegaMenu(dropdownElem, dropdownButton);
            })
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
                .find(".secondary-nav__link-wrapper")
                .each(function () {
                    if ($(document).width() > MAX_TABLET_WIDTH) {
                        var dropdown = $(this).find("#dropdown");
                        $(this).hover(function () {
                                $(dropdown).fadeIn();
                            }, function () {
                                $(dropdown).fadeOut();
                            }
                        );
                    }
                    var dropdownButton = $(this).find("#dropdownButton");
                    if (dropdownButton) {
                        $(dropdownButton).on("click", function () {
                            var dropdownElem = $(this).parent().parent().find("#dropdown");
                            if (!$(this).hasClass(UP_ARROW_CLASS)) {
                                showMegaMenu(dropdownElem, this);
                            } else hideMegaMenu(dropdownElem, this);
                        });
                    }
                });
        });
    });
})();
