
(function (document, $) {
    "use strict";
    $(document).on("foundation-contentloaded", function (e) {
        checkboxShowHideHandler($(".cq-dialog-checkbox-showhide", e.target));
    });

    $(document).on("change", ".cq-dialog-checkbox-showhide", function (e) {
        checkboxShowHideHandler($(this));
    });
    function checkboxShowHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-checkbox")) {
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                var component = $(element).data("checkbox");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }
    function showHide(component, element) {
        var target = $(element).data("cqDialogCheckboxShowhideTarget");
        var $target = $(target);
        if (target) {
            $target.show();
            if (component.checked) {
                $target.hide();
            }
        }
    }
})(document, Granite.$);