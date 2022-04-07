(function () {
    "use strict";
    var AdaptiveImage = function () {
        var _self = this;
        this.resolution = null;
        this.$imageElements = [];
        this._deviceResolution = null;
        this._windowWidth = window.innerWidth;
        this._offsetBottomBuffer = 200;
        
        this.getDeviceResolution = function () {
            this._windowWidth = window.innerWidth;
            if (this._windowWidth <= 767) {
                return 'sm';
            } else if (this._windowWidth <= 1199) {
                return 'md'
            } else  if (this._windowWidth > 1199) {
                return 'lg'
            } 
        }
        
        /**
         * The Image onload callback function, 
         *  here we are showing the image pre-loader (spinner) until image completely loaded on the dom
         */
        this.imageOnload = function (imageEle) {
            // console.log("Image on load ----------------");
            if (imageEle.complete && imageEle.naturalHeight !== 0 && imageEle.src !== 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEAAAAALAAAAAABAAEAAAI=') {
                $(imageEle).closest('.adaptive-image').addClass('adaptive-image--img-loaded');
                setTimeout(function(){
                    $(imageEle).closest('.adaptive-image').find('.adaptive-image__loader').hide();    
                }, 450);
            } else {
                requestAnimationFrame(function () {
                    _self.imageOnload(imageEle);
                });
            }
        }
        
        /**
         * The Image onerror callback function, 
         * here we are checking image src is not equal to default placeholder image path removing the image pre-loader (spinner)
         */
        this.imageOnError = function (imageEle) {
            if (imageEle.src !== 'data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEAAAAALAAAAAABAAEAAAI=') {
                $(imageEle).closest('.adaptive-image').addClass('adaptive-image--img-loaded');
                setTimeout(function(){
                    $(imageEle).closest('.adaptive-image').find('.adaptive-image__loader').hide();    
                }, 450);
            }
        }

        /**
         * Function for add/update image src when image data attribute (data-src) value is not same as current image src 
         * While the image is downloading and rendering on the DOM CSS loader will be displayed
         */
        this.addSrc = function(imgElm) {
            // console.log("Inside addsrc", imgElm);
            if (!imgElm) {
                return;
            }
            
            var dataSrc = imgElm.dataset['src'];

            if(!dataSrc) {
                $(imgElm).closest('.adaptive-image').addClass('adaptive-image--img-loaded adaptive-image--img-no-src');
                setTimeout(function(){
                    $(imgElm).closest('.adaptive-image').find('.adaptive-image__loader').hide();    
                }, 450);
            } else if($(imgElm).closest('.adaptive-image').hasClass('adaptive-image--img-no-src')) {
                $(imgElm).closest('.adaptive-image').removeClass('adaptive-image--img-no-src');
            }

            if (_self.isAuth && (imgElm.src || '').indexOf(dataSrc) === -1) {
                $(imgElm).attr('src', imgElm.dataset['src']);
            } else {
                if((imgElm.src || '').indexOf(dataSrc) === -1) {
                    imgElm.onload = function(){
                        _self.imageOnload(imgElm);
                    }
                    imgElm.onerror = function(){
                        _self.imageOnError(imgElm);
                    }
                    
                    $(imgElm).attr('src', imgElm.dataset['src']);
                }
            }
        }

        /**
         * Function for :
         * @param: $component. When passed loads only the images present inside that specific element
         */
        this.getMainImg = function ($component) {
            _self._deviceResolution = _self.getDeviceResolution();
            // console.log(_self._deviceResolution);
            var $imgElems = [];
            if ($component && $component.length) {
                $imgElems = $component.find('img.adaptive-image__elm');
            } else {
                $imgElems = $("img.adaptive-image__elm");
            }

            if ($imgElems.length) {
                $imgElems.each(function () {
                    var imageSrc = this.dataset[_self._deviceResolution + 'Src'],
                        $this = $(this);
                        $this.attr('data-src', imageSrc);
                        _self.addSrc(this);
                });
            }
        }

        /**
         * Window Resize event listener callback
         */
        this.windowResize = function() {
            _self.getMainImg();
        }

        /**
         * Adding required events for the window object
         */
        this.bindEvents = function () {
            window.addEventListener('DOMContentLoaded', function() {               
                _self.getMainImg();
            });

            // resize events
            window.addEventListener('resize', _self.windowResize);
        };

        this.init = function () {
            this.bindEvents();
        };

        this.init();
    }
    window.PLWebDsys = window.PLWebDsys || {};
    PLWebDsys.adaptiveImage = new AdaptiveImage();
})();
