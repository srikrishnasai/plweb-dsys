(function () {

    function checkContainerClass(){
        $(".section-container").each(function () {
            var isContainerGrid = $(this).hasClass("section-container__layout--grid");
            if (isContainerGrid) {
                $(this).find(".aem-Grid").first().css("display","flex");
            }
        });
    }
    $(document).ready(function() {
        checkContainerClass();
    });
    
})();
