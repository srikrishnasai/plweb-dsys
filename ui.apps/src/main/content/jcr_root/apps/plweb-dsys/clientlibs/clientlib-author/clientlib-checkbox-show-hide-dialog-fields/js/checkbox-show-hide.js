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
      var isShowOnChecked = $(element).data("isShowOnCheckedTarget");
            if($(element).is("coral-checkbox")) {
                Coral.commons.ready(element, function (component) {
                    showHide(component, element, true);
                    component.on("change", function () {
                        showHide(component, element, isShowOnChecked);
                    });
                });
            } else {
                var component = $(element).data("checkbox");
                if (component) {
                    showHide(component, element, isShowOnChecked);
                }
            }
        })
    }
    function showHide(component, element, isShowOnChecked = false) {
        var target = $(element).data("cqDialogCheckboxShowhideTarget");
		var $target = $(target);
        if (target) {
          isShowOnChecked ? $target.hide() :  $target.show();
          if (component.checked) {
                isShowOnChecked ? $target.show() :  $target.hide();

            }
        }
    }
})(document, Granite.$);