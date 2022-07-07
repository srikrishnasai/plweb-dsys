(function () {
    "use strict";

    /**Intialize Swiper for carousel view */
    function initializeSwiper(totalItems, swiper, prevButton, nextButton, pagination) {
        return new Swiper(swiper, {
            direction: "horizontal",
            loop: false,
            pagination: {
                el: pagination,
                type: "bullets",
                clickable: true,
            },
            spaceBetween: 10,
            speed: 1000,
            navigation: {
                nextEl: nextButton,
                prevEl: prevButton,
            }
        });
    }

    function loadComponent(elem) {

        var videoContainer = $(elem);
        var swiper;

        var totalSlides = videoContainer.find("#asset-carousel").find("li").not(".swiper-slide-duplicate").length;
        var swiperContainer = videoContainer.find(".assetSwiper");
        var prevButton = videoContainer.find(".swiper-button-prev")[0];
        var nextButton = videoContainer.find(".swiper-button-next")[0];
        var pagination = videoContainer.find(".swiper-pagination")[0];

        swiper = initializeSwiper(totalSlides, swiperContainer[0], prevButton, nextButton, pagination);

        swiper.on('slideChange', function () {
            videoContainer.find("video").each(function () {
                $(this)[0].pause();
            });
        });
    }



    $(window).on("load", function () {
        $(".asset-container").each(function () {
            loadComponent(this);
        });
    });

}());