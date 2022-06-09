(function($) {
    "use strict";
    var registry = $(window).adaptTo("foundation-registry");
    registry.register("foundation.validation.validator", {
        selector: "[data-validation=phone-number-validation]",
        validate: function(element) {
            let el = $(element);
            let pattern = /^(\+\d{1,2}\s)?\(?\d{3}\)?[\s.-]\d{3}[\s.-]\d{4}$/;
            let value = el.val();
            if (value && !pattern.test(value)) {
                return "Enter valid phone number as per given format";
            }
        }
    });

})(jQuery);

(function($) {
    "use strict";
    var registry = $(window).adaptTo("foundation-registry");
    registry.register("foundation.validation.validator", {
        selector: "[data-validation=email-validation]",
        validate: function(element) {
            let el = $(element);
            let pattern = /^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
            let value = el.val();
            if (value && !pattern.test(value)) {
                return "Enter valid email address";
            }
        }
    });

})(jQuery);