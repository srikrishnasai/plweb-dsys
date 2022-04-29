(function () {
    var ANIMATION_TIME = 600;
    var CLASS_LOADING_TIME = 10;

    function hideList(list) {
        list.removeClass("show-list");
        setTimeout(function () {
            list.removeClass("activate-list");
        }, ANIMATION_TIME + CLASS_LOADING_TIME);
    }

    $(window).on("load", function () {
        $(".topnav__all-sites").each(function () {
            $(this).on("click", function (e) {
                e.stopPropagation();
                var list = $(this).find("#list");
                if (list.hasClass("show-list")) {
                    hideList(list);
                } else {
                    list.toggleClass("activate-list");
                    setTimeout(function () {
                        list.toggleClass("show-list");
                    }, CLASS_LOADING_TIME);
                }
            });
        });
    });
    $(window).on("click", function () {
        var list = $("#list");
        hideList(list);
    });
})();
