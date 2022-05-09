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
    /** Checking if app is running in author mode or disabled mode. The code block should run in author mode only */
    if(window.Granite){
        $(window).on("DOMNodeInserted", checkContainerClass)
    }
})();
