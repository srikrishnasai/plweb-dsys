(function ($, $document) {
    "use strict";
    $document.on("dialog-ready", function () {
        var $coralTabs = $('.ups-tabs');
        
        var showOrHideTabs = function (val) {
            
            if (val === '1') {
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(2)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(3)').hide();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(4)').hide();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(5)').hide();
            } else if (val === '2') {
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(2)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(3)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(4)').hide();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(5)').hide();
            } else if (val === '3') {
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(2)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(3)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(4)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(5)').hide();
            } else if (val == '4') {
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(2)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(3)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(4)').show();
                $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(5)').show();
            }
        };

        var hideAllChartTabs = function () {
            $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(2)').hide();
            $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(3)').hide();
            $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(4)').hide();
            $coralTabs.find('.coral3-TabList .coral3-Tab:nth-child(5)').hide();
        }
        var showHideTabsOnLoad = function () {
            var $bgType = $('.ups-tabs .ups-show-hide-tabs coral-select-item:selected');

            if ($bgType) {
                var bgTypeVal = $bgType.val();
                if (bgTypeVal) {
                    showOrHideTabs(bgTypeVal);
                } else {
                    hideAllChartTabs();
                }
            }
        };

        var initComponent = function () {
            showHideTabsOnLoad();
        };

        $document.on("change", ".ups-show-hide-tabs", function (e) {
            if (e.target.value) {
                showOrHideTabs(e.target.value);
            } else {
                hideAllChartTabs();
            }
        });

        initComponent();
    });
})($, $(document));