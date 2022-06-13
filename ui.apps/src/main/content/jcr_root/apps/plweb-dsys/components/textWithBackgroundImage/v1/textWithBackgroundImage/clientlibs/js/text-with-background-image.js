(function () {
    var TextWithBackgroundImage = function ($el) {
        /**
         * Sets the position values of the text container
         */
        this.setTextPos = function () {
            if (this.$txtContainer && this.$txtContainer.length) {
                this.$txtContainer.css({
                    top: this.topVal,
                    left: this.leftVal
                });
            }
        };

        // as per window width load image respectively
        this.loadImage = function () {
            $('.text-with-bg-img__container').each(function (index, element) {
                var largeImg = $(this).find('.bg-lg-img').val();
                var smallImg = $(this).find('.bg-sm-img').val();

                if ($(window).width() < 576) {
                    $(this).find('.text-with-bg-img__img-container').css("background-image", "url(" + smallImg + ")");
                }
                else {
                    $(this).find('.text-with-bg-img__img-container').css("background-image", "url(" + largeImg + ")");
                }

            });
        };

        // initializes variables being used in this file
        this.initVariables = function () {
            this.$el = $el;
            this.$txtContainer = this.$el.find('.text-with-bg-img__txt-container');
            this.topVal = (this.$el.attr('data-vertical-pos') || 0) + '%';
            this.leftVal = (this.$el.attr('data-horizontal-pos') || 0) + '%';
        };

        this.bindEvents = function () {
            var _self = this;
            $(window).resize(function () {
                _self.loadImage();
            });
        };

        // init component
        this.init = function () {
            this.initVariables();
            this.setTextPos();
            this.bindEvents();
            this.loadImage();
        };

        if ($el) {
            this.init();
        }
    };

    $(document).ready(function () {
        $('.text-with-bg-img').each(function (i) {
            if (!$(this).hasClass('text-with-bg-img--mounted')) {
                $(this).addClass('text-with-bg-img--mounted');
                new TextWithBackgroundImage($(this));
            }
        });
    });
})();
