(function(document, $) {
    var $document = $(document);
    function selectShowHideHandler(element) {
        if ($(element).is('coral-select')) {
            // eslint-disable-next-line no-undef
            Coral.commons.ready(element, function(component) {
                showHide(component, element);
                component.on('change', function() {
                    showHide(component, element);
                });
            });
        } else {
            var component = $(element).data('select');
            if (component) {
                showHide(component, element);
            }
        }
    }

    // if the target is a tab, hides/shows the tab trigger too
    function checkAndHideTabTrigger($target, type = 'hide') {
        if ($target && $target.length) {
            if ($target.attr('role') === 'tabpanel') {
                var tabId = $target.attr('id');
                var $dialogContent = $target.closest('.cq-dialog-content');
                if ($dialogContent && $dialogContent.length && (tabId || tabId === 0)) {
                    var $tabTrigger = $dialogContent.find('.coral-TabPanel-tab[aria-controls="' + tabId + '"]');
                    if ($tabTrigger && $tabTrigger.length) {
                        if (type === 'hide') {
                            $tabTrigger.addClass('hide');
                        } else {
                            $tabTrigger.removeClass('hide');
                        }
                    }
                }
            }
        }
    }

    // returns the values and targets as an array of objects which can be looped to show and hide
    function getValuesAndTargets(element) {
        var targetData = [];
        if (element) {
            var targetDataSet = $(element).data();
            if (targetDataSet && Object.keys(targetDataSet)?.length) {
                Object.keys(targetDataSet).forEach((dataKey) => {
                    if (dataKey.indexOf('cqDialogSelectShowhideTarget') >= 0) {
                        targetData.push({
                            target: targetDataSet[dataKey],
                            falsy: targetDataSet['cqDialogSelectShowhideFalsy' + dataKey.replace('cqDialogSelectShowhideTarget', '')]
                        });
                    }
                });
            }
        }
        return targetData;
    }

    function showHide(component, element) {
        var targetDatum = getValuesAndTargets(element);

        if (targetDatum && targetDatum.length) {
            targetDatum.forEach((targetData) => {
                var target = targetData.target;
                var falsyValuesStr = targetData.falsy;
                if (target && falsyValuesStr) {
                    var $target = $(target);
                    var falsy = falsyValuesStr.split(',');
                    $target.addClass('hide');
                    checkAndHideTabTrigger($target);

                    var $formWrapper = $target.closest('.coral-Form-fieldwrapper');
                    if ($formWrapper && $formWrapper.length) {
                        $formWrapper.addClass('hide');
                        checkAndHideTabTrigger($formWrapper);
                    }

                    if (falsy.indexOf(component.value) === -1) {
                        $target.removeClass('hide');
                        checkAndHideTabTrigger($target, 'show');
                        if ($formWrapper && $formWrapper.length) {
                            $formWrapper.removeClass('hide');
                            checkAndHideTabTrigger($formWrapper, 'show');
                        }
                    }
                }
            });
        }
    }

    $document.on('foundation-contentloaded', function(e) {
        $('.cq-dialog-select-showhide').each(function() {
            selectShowHideHandler(this);
        });
    });

    $document.on('dialog-ready', function(e) {
        $('.cq-dialog-select-showhide').each(function() {
            selectShowHideHandler(this);
        });
    });

    $document.on('change', '.cq-dialog-select-showhide', function(e) {
        selectShowHideHandler(this);
    });
// eslint-disable-next-line no-undef
})(document, Granite.$);
