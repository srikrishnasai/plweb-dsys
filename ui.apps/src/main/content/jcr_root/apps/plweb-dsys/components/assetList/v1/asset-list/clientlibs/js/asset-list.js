(function () {
    'use strict';
    var AssetList = function ($assetlist) {

        // stops carousel autoplay
        this.carouselMouseoverCallback = function () {
            if (this.assetCarousel && this.assetCarousel.autoplay) {
                this.assetCarousel.autoplay.stop();
            }
        };

        // Resumes carousel autoplay
        this.carouselMouseoutCallback = function () {
            if (this.assetCarousel && this.assetCarousel.autoplay) {
                this.assetCarousel.autoplay.start();
            }
        };

        // appends events to pause autoplay carousel
        this.pauseOnHover = function () {
            this.$elem
                .off('mouseover.carouselMouseover')
                .on('mouseover.carouselMouseover', this.carouselMouseoverCallback.bind(this));
            this.$elem
                .off('mouseout.carouselMouseover')
                .on('mouseout.carouselMouseout', this.carouselMouseoutCallback.bind(this));
        };

        // initializes the carousels
        this.initializeCarousels = function () {
            if (this.$assetCarouselWrapper && this.$assetCarouselWrapper.length) {
                var $assetCarousel = this.$assetCarouselWrapper.find('.swiper').get(0);
                // eslint-disable-next-line no-undef
                this.assetCarousel = new Swiper($assetCarousel, {
                    // Disable preloading of all images
                    preloadImages: false,
                    // Enable lazy loading
                    lazy: true,
                    slidesPerView: 1,
                    centeredSlides: true,
                    effect: this.swiperAnimation,
                    grabCursor: true,
                    pagination: {
                        el: this.$assetCarouselWrapper.find('.swiper-pagination').get(0),
                        clickable: true
                    },
                    navigation: {
                        nextEl: this.$nextBtn.get(0),
                        prevEl: this.$prevBtn.get(0)
                    },
                    autoHeight: true,
                    autoplay: this.autoPlayOptions,
                    loop: this.hasLoop,
                    breakpoints: {
                        768: {
                            spaceBetween: 10,
                            centeredSlides: true,
                            slidesPerView: 'auto'
                        }
                    },
                    on: {
                        init: function () {
                            this.$elem.addClass(this._visibilityClass);
                        }.bind(this)
                    }
                });
            }
        };

        this.bindEvents = function () {
            // update count of the pagination
            // slide the text carousel to the corresponding slide in image carousel
            if (this.assetCarousel) {
                if (this.autoPlayOptions && this.autoPlayOptions.disableOnInteraction === false) {
                    this.pauseOnHover();
                }
                var self = this;
                this.assetCarousel.on('slideChange', function () {
                    self.$assetCarouselWrapper.find("video").each(function () {
                        $(this)[0].pause();
                    });
                });
            }
        };

        this.initVariables = function () {
            this.$elem = $assetlist;
            this.hasLoop = window.PLDSYS?.utilities?.getSwiperLoop(this.$elem) || false;
            this.autoPlayOptions = window.PLDSYS?.utilities?.getSwiperAutoplayOpts(this.$elem) || false;
            this.swiperAnimation = window.PLDSYS?.utilities?.getSwiperAnimation(this.$elem) || '';
            this.assetCarousel = null;
            this.$assetCarouselWrapper = this.$elem;
            this.$nextBtn = this.$elem.find('.asset-container__swiper-control--next');
            this.$prevBtn = this.$elem.find('.asset-container__swiper-control--prev');
            this.$counter = this.$elem.find('.asset-container__counter-upper');
            this._visibilityClass = 'asset-container__main--visible';
        };

        this.init = function () {
            this.initVariables();
            this.initializeCarousels();
            this.bindEvents();
        };

        this.init();
    };

    $(document).ready(function () {
        var $assetlist = $('.asset-container__main');
        if ($assetlist && $assetlist.length) {
            $assetlist.each(function () {
                // eslint-disable-next-line no-invalid-this
                if (!$(this).hasClass('asset-container--mounted')) {
                    // eslint-disable-next-line no-invalid-this
                    $(this).addClass('asset-container--mounted');
                    // eslint-disable-next-line no-invalid-this
                    new AssetList($(this));
                }
            });
        }
    });
})();