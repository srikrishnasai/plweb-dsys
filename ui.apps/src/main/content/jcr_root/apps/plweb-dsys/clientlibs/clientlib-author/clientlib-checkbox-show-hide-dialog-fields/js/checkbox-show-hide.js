//This Js hides the target if check box is checked..

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
    function showHide(component, element, isShowOnChecked = false) {
        var target = $(element).data("cqDialogCheckboxShowhideTarget");
        var isShowOnChecked = $(element).data("isshowoncheckedtarget");
		var $target = $(target);
        if (target) {
          if (component.checked) {
              isShowOnChecked ? $target.show() : $target.hide();
          } else {
              isShowOnChecked ? $target.hide() : $target.show();
          }
        }
    }
})(document, Granite.$);