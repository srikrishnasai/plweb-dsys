(function (channel) {
    "use strict";
    var _applyOverlayStyling = function(editable) {
        if (editable && editable.overlay) {
    
            var $overlayDom = $(editable.overlay.dom);
            $overlayDom.toggleClass("targeted", true);
    
            // build a visual "tab" to show Targeted content
            var icon = new Coral.Icon().set({
                icon: 'userLock'
            });
            var $tabLabel = $("<div class='targeting-icon-overlay targeting-icon-overlay--top'></div>");
            $tabLabel.append(icon);
    
            $overlayDom.append($tabLabel);
        }
    };
    
    var _prepareAuthControlledComponentsList = function() {
        var pagePath = Granite.author.page.path + "/jcr:content";
        var query = '/bin/querybuilder.json?path=' + pagePath + '&property=compauth&property.value=true';
        var data = Granite.HTTP.eval(query);
        setTimeout(function() {
            var authComponents = [];
            $.each(data.hits, function(idx, element){
                authComponents.push(element.path);
            });
        
            var editables = Granite.author.editables;
            $.each(editables, function(idx, editable){
                if (authComponents.indexOf(editable.path) > -1) {
                    _applyOverlayStyling(editable);
                }
            });
        });
    }
    // setTimeout(_prepareAuthControlledComponentsList, 500);
    channel.on("cq-editor-loaded", function (event) {
        // Hook into the cq-editor-loaded event to detect when the editor was open in Edit mode...
        if (Granite.author.layerManager.getCurrentLayerName() === "Edit") {
            setTimeout(_prepareAuthControlledComponentsList, 500);
        }
    }).on("cq-layer-activated", function (event) {
        // ...and hook into the layer activation to re-apply the styling if the layer changed
        var currentLayer = event["layer"],
            prevLayer = event["prevLayer"];
    
        if (currentLayer == "Edit" && prevLayer !== "Edit") {
            setTimeout(_prepareAuthControlledComponentsList, 500);
        }
    });

    if (Granite && Granite.author && Granite.author.inner) {
        window.Granite.author.inner.EditorFrame.subscribeRequestMessage('cqauthor-cmd', function(message){
            console.log('component refreshed');
            if (message && message.data && message.data.path) {
                setTimeout(_prepareAuthControlledComponentsList, 500);
            }
        });
    }
})(jQuery(document));