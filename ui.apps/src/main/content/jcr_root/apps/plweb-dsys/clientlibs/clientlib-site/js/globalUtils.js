// Setup global localStorage variables here.
// You can use knockout ie. 'ICO.packageIt.suites([{},{}])'
//                     - OR -
// localStorage.setItem(), localStorage.removeItem()

window.PLDSYS = window.PLDSYS || {};
(function () {
    var Utilities = function () {
        var self = this;

        // returns swiper carousel authored loop flag
        self.getSwiperLoop = function ($swiperWrapper) {
            if (this.isAuth) {
                return false;
            }
            var swiperDataset = $swiperWrapper && $swiperWrapper[0] ? $swiperWrapper[0].dataset : {};
            var swiperLoop = false;
            if ($swiperWrapper.attr('data-cmp-loop') && swiperDataset) {
                swiperLoop = (swiperDataset.cmpLoop === 'true' || swiperDataset.cmpLoop === true) ? true : false;
            }
            return swiperLoop;
        };

        // returns carousel options authored for a carousel
        self.getSwiperAutoplayOpts = function ($swiperWrapper) {
            var swiperDataset = $swiperWrapper[0] ? $swiperWrapper[0].dataset : {};
            var autoPlayFeatures = false;
            if ($swiperWrapper.attr('data-cmp-autoplay') && swiperDataset) {
                autoPlayFeatures = {
                    delay: swiperDataset.cmpDelay ? (parseInt(swiperDataset.cmpDelay, 10) * 1000) : 3000, // default
                    disableOnInteraction: swiperDataset.cmpAutopauseDisabled === 'true' ? true : false // default
                };
            }
            return autoPlayFeatures;
        };

        // returns carousel animation authored for a carousel
        self.getSwiperAnimation = function ($swiperWrapper) {
            var swiperDataset = $swiperWrapper[0] ? $swiperWrapper[0].dataset : {};
            var animation = false;
            if ($swiperWrapper.attr('data-cmp-animation') && swiperDataset) {
                animation = swiperDataset.cmpAnimation ? swiperDataset.cmpAnimation : ''; // default
            }
            return animation;
        };
    };

    window.PLDSYS.utilities = new Utilities();
}());
