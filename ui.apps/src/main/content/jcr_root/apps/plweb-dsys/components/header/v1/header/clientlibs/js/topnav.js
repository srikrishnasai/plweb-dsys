(function () {
    var ANIMATION_TIME = 600;

    var CLASS_LOADING_TIME = 10;

    var SHOW_LIST_CLASS = "show-list";

    var ACTIVATE_LIST_CLASS = "activate-list";

    var LIST_ID = "#list"

    var TOPNAV_CLASS_NAME = ".topnav__all-sites"

    function hideList(list) {
        list.removeClass(SHOW_LIST_CLASS);
        setTimeout(function () {
            list.removeClass(ACTIVATE_LIST_CLASS);
        }, ANIMATION_TIME + CLASS_LOADING_TIME);
    }

    $(window).on("load", function () {
        $(TOPNAV_CLASS_NAME).each(function () {
            $(this).on("click", function (e) {
                e.stopPropagation();
                var list = $(this).find(LIST_ID);
                if (list.hasClass(SHOW_LIST_CLASS)) {
                    hideList(list);
                } else {
                    list.toggleClass(ACTIVATE_LIST_CLASS);
                    setTimeout(function () {
                        list.toggleClass(SHOW_LIST_CLASS);
                    }, CLASS_LOADING_TIME);
                }
            });
        });
    });
    $(window).on("click", function () {
        var list = $(LIST_ID);
        hideList(list);
    });
})();
