(function () {

    function checkContainerClass(){
        $(".section-container__layout").each(function () {
            var isContainerGrid = $(this).hasClass("section-container__layout--grid");
            if (isContainerGrid) {
                // adding a class section-container-grid to the first AEM Grid element
                $(this).find(".aem-Grid").first().addClass("section-container-grid");
            }
        });
    }
    $(document).ready(function() {
        checkContainerClass();
    });
    
})();
