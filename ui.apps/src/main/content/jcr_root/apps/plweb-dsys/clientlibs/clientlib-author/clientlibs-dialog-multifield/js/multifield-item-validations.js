console.log('test');
(function() {
    "use strict";
    var registry = $(window).adaptTo("foundation-registry");
    registry.register("foundation.validation.validator", {
     selector: "[data-foundation-validation^='multifield-max']",
     validate: function(el) {
    // parse the max number from the attribute value, the value maybe something like "multifield-max-8"
    var validationName = el.getAttribute("data-validation")
    var max = validationName.replace("multifield-max-", "");
    max = parseInt(max);
    // el here is a coral-multifield element
    // see: https://helpx.adobe.com/experience-manager/6-4/sites/developing/using/reference-materials/coral-ui/coralui3/Coral.Multifield.html
    if (el.items.length > max){
        // items added are more than allowed, return error
        return "Max allowed numbers of list item is: "+ max
    }
  }
    });


    })(jQuery);