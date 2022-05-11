(function(document, $) {
    "use strict";
    // when dialog gets injected
    $(document).on("foundation-contentloaded", function(e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideHandler($(".cq-dialog-showhide-checkbox-multifield", e.target));
    });

    $(document).on("change", ".cq-dialog-showhide-checkbox-multifield", function(e) {
        showHideHandler($(this));
    });

    function showHideHandler(el) {
        el.each(function(i, element) {
            if ($(element).is("coral-checkbox")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function(component) {
                    showHide(component, element);
                    component.on("change", function() {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("checkbox");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("cq-dialog-showhide-checkbox-multifield-target");
        var $target = $(target);
        var elementIndex = $(element).closest('coral-multifield-item').index();
        if (target) {
            var value = component.checked;
            $target.each(function(index) {
                var tarIndex = $(this).closest('coral-multifield-item').index();
                if (elementIndex == tarIndex) {
                    if(value) {
                        $(this).not(".hide").parent().addClass("hide");
                    } else {
                        $(this).parent().removeClass("hide");
                    }
                }
            });
        }
    }

})(document, Granite.$);