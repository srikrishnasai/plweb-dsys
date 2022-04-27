(function () {
    "use strict";
    $(window).on("load", function () {
        $(".secondary-nav__navigation").each(function () {
            var nav = this;
            $(nav)
                .find("li")
                .each(function () {
                    var dropdown = $(this).find("#dropdown");
                    var link = $(this).find("#navLink");
                    link.hover(
                        function () {
                            link.addClass("secondary-nav--link-active");
                        },
                        function () {}
                    );
                    $(this).hover(
                        function () {
                            if (!dropdown.hasClass("secondary-nav--dropdown-active")) {
                                dropdown.addClass("secondary-nav--dropdown-active");
                                setTimeout(function () {
                                    dropdown.addClass("secondary-nav--dropdown-show");
                                }, 300);
                            }
                        },
                        function () {
                            dropdown.removeClass("secondary-nav--dropdown-show");
                            link.removeClass("secondary-nav--link-active");
                            setTimeout(function () {
                                dropdown.removeClass("secondary-nav--dropdown-active");
                            }, 350);
                        }
                    );
                });
        });
    });
})();
