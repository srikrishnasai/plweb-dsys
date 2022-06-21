
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
        observer: true,
        observeParents: true,
        centeredSlides: true,
        centerInsufficientSlides: true,
        slideToClickedSlide: true,
        speed: 1000,
        navigation: {
            nextEl: nextButton,
            prevEl: prevButton,
        }
    });
}

function playVideoForCarousel(elem) {
    var videoElement = $('#videoPlayer');
    videoElement[0].play();
}

function loadComponent(elem) {

    var videoContainer = $(elem);
    var swiper;

    var totalSlides = videoContainer.find("#asset-carousel").find("li").not(".swiper-slide-duplicate").length;

        var swiperContainer = videoContainer.find(".swiper");
        var prevButton = videoContainer.find(".swiper-button-prev")[0];
        var nextButton = videoContainer.find(".swiper-button-next")[0];
        var pagination = videoContainer.find(".swiper-pagination")[0];

    swiper = initializeSwiper(totalSlides, swiperContainer[0], prevButton, nextButton, pagination);
}



$(window).on("load", function () {
    $(".asset-container").each(function () {
        loadComponent(this);
    });
});