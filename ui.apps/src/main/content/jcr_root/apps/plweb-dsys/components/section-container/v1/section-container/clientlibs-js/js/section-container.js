(function () {

    function checkContainerClass(){
        $(".section-container").each(function () {
            var isContainerGrid = $(this).hasClass("section-container__layout--block");
            if (isContainerGrid) {
                $(this).find(".aem-Grid").first().css("display","block");
                $(this).find(".aem-Grid").first().css("overflow","hidden");
            }
        });
    }
    $(document).ready(function() {
        checkContainerClass();
    });
    
})();
