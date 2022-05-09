(function () {

    function checkContainerClass(){
        $(".section-container").each(function () {
            var isContainerGrid = $(this).hasClass("section-container__layout--grid");
            if (isContainerGrid) {
                $(this).find(".aem-Grid").first().css("display","flex");
            }
        });
    }

    $(window).on("load", checkContainerClass);
    // TODO : Run this code block only in edit mode though in disable mode this event once page loaded will not be fired again
    $(window).on("DOMNodeInserted", checkContainerClass)
})();
