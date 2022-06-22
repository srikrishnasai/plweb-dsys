
(function (document, $) {
    "use strict";

    // when dialog gets injected
    $(document).on("foundation-contentloaded", function (e) {
        // if there is already an inital value make sure the according target element becomes visible
        showHideHandler($(".featured-video-hide-show", e.target));
    });

    
    $(document).on("selected", ".featured-video-hide-show", function (e) {
        showHideHandler($(this));
   	});

    function showHideHandler(el) {
        el.each(function (i, element) {
            if($(element).is("coral-select")) {
                // handle Coral3 base drop-down
                Coral.commons.ready(element, function (component) {
                    showHide(component, element);
                    component.on("change", function () {
                        showHide(component, element);
                    });
                });
            } else {
                // handle Coral2 based drop-down
                var component = $(element).data("select");
                if (component) {
                    showHide(component, element);
                }
            }
        })
    }

    function showHide(component, element) {
        // get the selector to find the target elements. its stored as data-.. attribute
        var target = $(element).data("featuredVideoHideShowTarget");
        var tabId = $('.featured-video-tab').closest('.coral3-Panel').attr('aria-labelledby');
        var $target = $(target);

        console.log($target);
        if (target) {
            var value;
            if (typeof component.value !== "undefined") {
                value = component.value;
            } else if (typeof component.getValue === "function") {
                value = component.getValue();
            }

           // console.log($target.attr('data-hideshowtargetvalue'));
            console.log(value);
            if(value === '' || value === $target.attr('data-hideshowtargetvalue')) {
				$target.hide();
                $('#' + tabId).hide();
            } else {
				$target.show();
                $('#' + tabId).show();
            }
        }
    }

})(document, Granite.$);