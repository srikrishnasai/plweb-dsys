/** The page is carousel page  */
var CAROUSEL_PAGE = "carousel";

/** Page is sidebar page */
var SIDEBAR_PAGE = "sidebar";

var MIN_LAPTOP_WIDTH = 1023;

var MAX_MOBILE_DEVICE_WIDTH = 550;

var SLIDES_BELOW_450 = 2;

var SLIDES_BELOW_600 = 2.2;

var SLIDES_BELOW_768 = 2.7;

var SLIDES_BELOW_1251 = 3;

var SLIDES_ABOVE_1251 = 3.5;

var CAROUSEL_BREAKPOINT_450 = 450;

var CAROUSEL_BREAKPOINT_600 = 600;

var CAROUSEL_BREAKPOINT_768 = 768;

var CAROUSEL_BREAKPOINT_1251 = 1251;

var REMOVE_SHIMMER_TIMER_FAST = 10000;

var REMOVE_SHIMMER_TIMER_SLOW = 45000;

var REMOVE_SHIMMER_TIMER = function () {
    /**Slow 3G on chrome is at 0.35 */
    if (navigator.connection) {
        if (navigator.connection.downlink > 0.4) return REMOVE_SHIMMER_TIMER_FAST;
        else return REMOVE_SHIMMER_TIMER_SLOW;
    } else return REMOVE_SHIMMER_TIMER_FAST;
};

/**
 * @description - It calculates if the swiper at a particular breakpoint needs loop or not
 * @param {number} length - The number of slides that are there
 * @returns {object} - Returns a object with different keys for different breakpoints and with value that is loop needed or not
 */
function calculateCarouselLoop(length) {
    var loop = {};
    if (length > SLIDES_BELOW_450) loop.initial = true;
    else loop.initial = false;
    if (length > SLIDES_BELOW_600) loop[CAROUSEL_BREAKPOINT_450] = true;
    else loop[CAROUSEL_BREAKPOINT_450] = false;
    if (length > SLIDES_BELOW_768) loop[CAROUSEL_BREAKPOINT_600] = true;
    else loop[CAROUSEL_BREAKPOINT_600] = false;
    if (length > SLIDES_BELOW_1251) loop[CAROUSEL_BREAKPOINT_768] = true;
    else loop[CAROUSEL_BREAKPOINT_768] = false;
    if (length > SLIDES_ABOVE_1251) loop[CAROUSEL_BREAKPOINT_1251] = true;
    else loop[CAROUSEL_BREAKPOINT_1251] = false;
    return loop;
}

/**
 * @description Whenever user click on any thumbnail from the sidebar take the src title and poster of that video and add it to the player and change the PLAYING overlay to the newly selected video
 * @param {Object} elem - The element which is clicked.
 */
function playVideoFromSidebar(elem, container) {
    if (container.find(".video-list__shimmer").length === 0) {
        var video = $(elem).find("video");
        var player = container.find("#videoPlayer");
        var path = video.attr("data-video-path");
        var title = video.attr("data-title");
        var desc = video.attr("data-desc");
        var poster = video.attr("poster");
        // var videoElement = document.getElementById("videoPlayer");
        container.find(".o-1").each(function (el) {
            $(this).removeClass("o-1");
        });
        $(elem).find("#overlay").addClass("o-1");
        $(elem).find("#overlay").find("span").addClass("o-1");
        player.parent().parent().find("#videoTitle").html(title);
        player
            .parent()
            .parent()
            .find("#videoDescription")
            .html(desc ? desc : "");
        player.attr("src", path);
        player.attr("poster", poster ? poster : "");
        player[0].play();
    }
}
/**
 * @description Whenever user click on any thumbnail from the carousel take the src title and poster of that video and add it to the player and change the PLAYING overlay to the newly selected video
 * @param {Object} elem - The element which is clicked.
 */
function playVideoFromCarousel(e) {
    if (e.preventDefault) e.preventDefault();
    var videoContainer = $(e.target).closest("#videoContainer");
    if (videoContainer.find(".video-list__shimmer").length === 0) {
        var elem = $(e.target).closest("#card");
        var videoElem = $(elem).find("video");
        var path = videoElem.attr("data-video-path");
        var title = videoElem.attr("data-title");
        var desc = videoElem.attr("data-desc");
        var poster = videoElem.attr("poster");
        videoContainer.find(".o-1").each(function (el) {
            $(this).removeClass("o-1");
        });
        /** In carousel as there are duplicate slides add overlay to all the slides */
        videoContainer.find(".video-list__carousel-thumbnail").each(function () {
            var carouselVideoPath = $(this).attr("data-video-path");
            if (carouselVideoPath === path) $(this).siblings("#overlay").addClass("o-1");
            if (carouselVideoPath === path) $(this).siblings("#overlay").find("span").addClass("o-1");
        });
        var videoElement = videoContainer.find("#carouselPlayer");
        videoElement.parent().parent().find("#videoTitle").html(title);
        setTimeout(function () {
            $("html, body").animate(
                {
                    scrollTop: videoContainer.find("#playerContainer").offset().top - 100,
                },
                1000
            );
        }, 0);
        videoElement
            .parent()
            .siblings("#videoDesc")
            .html(desc ? desc : "");
        videoElement.attr("src", path);
        videoElement.attr("poster", poster ? poster : "");
        videoElement[0].play();
    }
}

/**
 * @description When play button is clicked on the player in carousel view play the video and hide the button
 */
function playVideoForCarousel(elem) {
    var videoElement = elem.find("#carouselPlayer");
    var playButton = elem.find("#carouselPlay");
    playButton.css("opacity", "0");
    playButton.css("z-index", "0");
    videoElement[0].play();
}
/**
 * @description When play button is clicked on the player in sidebar view play the video and hide the button
 */
function playVideoForSidebar(elem) {
    var videoElement = elem.find("#videoPlayer");
    var playButton = elem.find("#sideBarPlay");
    videoElement[0].play();
    playButton.css("opacity", "0");
    playButton.css("z-index", "0");
}

/**
 * @description - When in default mode the video component is loaded and video is played hiding the play button
 * @param {DOMNode} elem - The video node that has been played
 */
function onDefaultVideoPlay(elem) {
    $(elem).siblings("button").css({ opacity: "0", "z-index": "0" });
}

/**
 * @description - When in default mode the video component is loaded and video is paused showing the play button
 * @param {DOMNode} elem - The video node that has been played
 */
function onDefaultVideoPause(elem) {
    $(elem).siblings("button").css({ opacity: "1", "z-index": "100000" });
}

/**
 * @description - When in default mode the video component is loaded and on clicking the play button playing the video
 * @param {DOMNode} elem - The video node that has been played
 */
function defaultVideoPlay(elem) {
    $(elem).get(0).play();
}

// function updateNumericPagination(index, length) {
//     $("#pagination").html("<span>" + index + "</span>/<span>" + length + "</span>");
// }

/**Intialize Swiper for carousel view */
function initializeSwiper(isLoopNeeded, totalItems, swiper, prevButton, nextButton, pagination) {
    return new Swiper(swiper, {
        direction: "horizontal",
        loop: isLoopNeeded.initial,
        pagination: {
            el: pagination,
            type: "bullets",
            clickable: true,
        },
        slidesPerView: SLIDES_BELOW_450,
        spaceBetween: 10,
        observer: true,
        observeParents: true,
        centeredSlides: isLoopNeeded.initial ? true : false,
        centerInsufficientSlides: true,
        slideToClickedSlide: true,
        speed: 1000,

        navigation: {
            nextEl: nextButton,
            prevEl: prevButton,
        },
        breakpoints: {
            450: {
                slidesPerView: SLIDES_BELOW_600,
                spaceBetween: 10,
                centeredSlides: isLoopNeeded[CAROUSEL_BREAKPOINT_450] ? true : false,
                loop: isLoopNeeded[CAROUSEL_BREAKPOINT_450],
            },
            600: {
                slidesPerView: SLIDES_BELOW_768,
                spaceBetween: 20,
                centeredSlides: isLoopNeeded[CAROUSEL_BREAKPOINT_600] ? true : false,
                loop: isLoopNeeded[CAROUSEL_BREAKPOINT_600],
            },
            768: {
                slidesPerView: SLIDES_BELOW_1251,
                spaceBetween: 30,
                centeredSlides: isLoopNeeded[CAROUSEL_BREAKPOINT_768] ? true : false,
                loop: isLoopNeeded[CAROUSEL_BREAKPOINT_768],
                slideToClickedSlide: false,
            },
            1251: {
                slidesPerView: SLIDES_ABOVE_1251,
                spaceBetween: 30,
                centeredSlides: isLoopNeeded[CAROUSEL_BREAKPOINT_1251] ? true : false,
                loop: isLoopNeeded[CAROUSEL_BREAKPOINT_1251],
                slideToClickedSlide: false,
            },
        },
        /*  on: { 
            init:       function(swiper){
                updateNumericPagination(swiper.realIndex + 1,totalItems)
            },
            slideChange:   function(swiper){
                updateNumericPagination(swiper.realIndex + 1,totalItems)
            },
                breakpoint: function(swiper){
                    $(this.$el[0]).find("li").each(function(){
                        $(this).find("#card").on("click", function(){
                                    playVideoFromCarousel(this)
                            })
                    })
                }
        }*/
    });
}

/**
 * @description - Adds the play and pause listeners to show the play button and hide the button
 * @param {DOMNode} elem - DOM element on which event listeners need to be registered
 * @param {DOMNode} button - Play button to show when video pause event is fired by DOM
 * @param {function} callback - Callback function when video's play event is fired by DOM
 */
function addListeners(elem, button, callback, videoContainer) {
    function showPlayButton() {
        $(button).css("opacity", "1");
        $(button).css("z-index", "10000");
    }

    elem.on("play", function () {
        callback(videoContainer);
    });
    elem.on("pause", showPlayButton);

    elem.on("playing", function () {
        $(button).css("opacity", "0");
        $(button).css("z-index", "1");
    });
    elem.on("ended", showPlayButton);
}

$(
    (function () {
        // video component
        var mediaName = '${list.mediaName  @ context="text"}';
        var mediaFile = '${list.mediaName  @ context="text"}';
        var mediaPath = '${list.videoPath  @ context="text"}';
        var video = document.getElementById('${list.id  @ context="scriptString"}');
        var type = '${list.resourceType  @ context="text"}';
        var videoOpen = false;
        // delay (in ms) due to buggy player implementation
        // when seeking, video.currentTime is not updated correctly so we need to delay
        // retreiving currentTime by an offset
        var delay = 250;
        //mouse up flag
        var isMouseUp = true;
        //store currentTime for 1 second
        var pauseTime = 0;
        // clickstream cloud data to be send based on context mapping
        var CQ_data = new Object();

        if (video && video.addEventListener) {
            video.addEventListener("playing", play, false);
        }

        function open() {
            video.addEventListener("pause", pause, false);
            video.addEventListener("ended", ended, false);
            video.addEventListener("seeking", pause, false);
            video.addEventListener("seeked", play, false);

            //store flag for mouse events in order to play only if the mouse is up
            video.addEventListener("mousedown", mouseDown, false);
            video.addEventListener("mouseup", mouseUp, false);
            function mouseDown() {
                isMouseUp = false;
            }
            function mouseUp() {
                isMouseUp = true;
            }

            CQ_data = new Object();
            CQ_data["length"] = Math.floor(video.duration);
            CQ_data["playerType"] = "HTML5 video";
            CQ_data["source"] = mediaName;
            CQ_data["playhead"] = Math.floor(video.currentTime);

            CQ_data["videoName"] = mediaName;
            CQ_data["videoFileName"] = mediaFile;
            CQ_data["videoFilePath"] = mediaPath;

            if (window.CQ_Analytics) {
                CQ_Analytics.record({
                    event: "videoinitialize",
                    values: CQ_data,
                    componentPath: mediaPath,
                });
            }

            storeVideoCurrentTime();
        }

        function play() {
            if (window.CQ_Analytics && CQ_Analytics.Sitecatalyst) {
                // open video call
                if (!videoOpen) {
                    open();
                    videoOpen = true;
                } else {
                    //send pause event before play for scrub events
                    pause();
                    // register play
                    setTimeout(playDelayed, delay);
                }
            }
        }

        function playDelayed() {
            if (isMouseUp) {
                CQ_data = new Object();
                CQ_data["playhead"] = Math.floor(video.currentTime - delay / 1000);
                CQ_data["source"] = mediaName;
                if (window.CQ_Analytics) {
                    CQ_Analytics.record({
                        event: "videoplay",
                        values: CQ_data,
                        componentPath: mediaPath,
                    });
                }
            }
        }

        function pause() {
            CQ_data = new Object();
            CQ_data["playhead"] = pauseTime;
            CQ_data["source"] = mediaName;
            if (window.CQ_Analytics) {
                CQ_Analytics.record({
                    event: "videopause",
                    values: CQ_data,
                    componentPath: mediaPath,
                });
            }
        }

        function ended() {
            CQ_data = new Object();
            CQ_data["playhead"] = Math.floor(video.currentTime);
            CQ_data["source"] = mediaName;
            if (window.CQ_Analytics) {
                CQ_Analytics.record({ event: "videoend", values: CQ_data, componentPath: mediaPath });
            }
            //reset temp variables
            videoOpen = false;
            pauseTime = 0;
        }

        //store current time for one second that will be use for pause
        function storeVideoCurrentTime() {
            timer = window.setInterval(function () {
                if (video.ended != true) {
                    pauseTime = Math.floor(video.currentTime);
                } else {
                    window.clearInterval(timer);
                }
            }, 1000);
        }
    })()
);

/**
 * @description - Load the view by fetching the view from the properties
 * @param {HTMLElement} elem - Pass the video container that needs to be initialized
 */
function loadComponent(elem) {
    var videoContainer = $(elem);

    /**Check the property to know which page it is */
    var property = videoContainer.find("#properties").attr("data-attribute");
    var swiper;
    var isSidebarPageInitialized = false;

    var shimmerTimer = setTimeout(function () {
        videoContainer.find(".video-list__shimmer").each(function () {
            $(this).removeClass("video-list__shimmer");
        });
    }, REMOVE_SHIMMER_TIMER());

    function initializeCarouselPage() {
        /** If it's mobile device and the padding of the page is more give width to the container and negative margin so gutter is 20px on both side */
        if (window.innerWidth < MAX_MOBILE_DEVICE_WIDTH) {
            if (window.innerWidth - videoContainer.width() > 40) {
                var width = "calc(100vw - 40px)";
                videoContainer.css("width", width);
                var containerWidth = videoContainer.width();
                var left = videoContainer.offset().left;
                var maxRightSide = containerWidth + left;
                /** Due to margin or something the spacing on right side is not equal so calculate it. eg padding would be 36px but the max element can go on right side is 32px so there would be 4px difference */
                var difference = window.innerWidth - maxRightSide;
                var margin = (window.innerWidth - containerWidth) / 2 - difference;
                videoContainer.css("margin-left", "-" + margin + "px");
            }
        }
        /**Initializing swiper process */
        var totalSlides = videoContainer.find("#carousel").find("li").not(".swiper-slide-duplicate").length;
        var isLoopNeeded = calculateCarouselLoop(totalSlides);
        var swiperContainer = videoContainer.find(".videoListSwiper");

        var prevButton = videoContainer.find(".swiper-button-prev")[0];
        var nextButton = videoContainer.find(".swiper-button-next")[0];
        var pagination = videoContainer.find(".swiper-pagination")[0];
        swiper = initializeSwiper(isLoopNeeded, totalSlides, swiperContainer[0], prevButton, nextButton, pagination);

        /**Find player and play button and add click listener to play button */
        var player = videoContainer.find("#carouselPlayer");
        var button = videoContainer.find("#carouselPlay");
        button.on("click", function () {
            playVideoForCarousel(videoContainer);
        });

        /**Add play , pause and playing listener to the player */
        addListeners(player, button, playVideoForCarousel, videoContainer);
        var videoElementFromCarousel = swiperContainer.find(".swiper-slide-active").find("video").first();

        /**Get the path, title, poster , desc from the first elem of carousel */
        var path = $(videoElementFromCarousel).attr("data-video-path");
        var title = $(videoElementFromCarousel).attr("data-title");
        var desc = $(videoElementFromCarousel).attr("data-desc");
        var poster = $(videoElementFromCarousel).attr("poster");
        videoContainer.find(".o-1").each(function (el) {
            $(this).removeClass("o-1");
        });
        $(videoElementFromCarousel).siblings("#overlay").addClass("o-1");
        $(videoElementFromCarousel).siblings("#overlay").find("span").addClass("o-1");
        player.parent().parent().find("#videoTitle").html(title);
        player.parent().siblings("#videoDesc").html(desc);
        player.attr("src", path);
        player.attr("poster", poster);
        $(player).attr("autoplay") && player[0].play();
        videoContainer.find(".video-list__carousel-thumbnail").each(function () {
            var carouselVideoPath = $(this).attr("data-video-path");
            if (carouselVideoPath === path) {
                $(this).siblings("#overlay").addClass("o-1");
                $(this).siblings("#overlay").find("span").addClass("o-1");
            }
        });

        /** Add listener so that play button is showed only when video can be played on initial load  */
        player.one("canplay", function () {
            videoContainer.find(".video-list__shimmer").each(function () {
                $(this).removeClass("video-list__shimmer");
            });
            clearTimeout(shimmerTimer);
            button.css("opacity", "1");
        });
    }

    /**
     * @description Intializing the sidebar page by picking the first video from the list and adding it to the player.
     */
    function initializeSidebarPage() {
        isSidebarPageInitialized = true;
        var player = videoContainer.find("#videoPlayer");
        var button = videoContainer.find("#sideBarPlay");
        button.on("click", function () {
            playVideoForSidebar(videoContainer);
        });
        addListeners(player, button, playVideoForSidebar, videoContainer);
        var videoElementFromSidebar = videoContainer.find(".video-list__right-pane").find("video").first();
        videoContainer
            .find("#sidebar")
            .find(".video-list__preview")
            .each(function () {
                $(this).on("click", function () {
                    playVideoFromSidebar(this, videoContainer);
                });
            });
        var path = $(videoElementFromSidebar).attr("data-video-path");
        var title = $(videoElementFromSidebar).attr("data-title");
        var desc = $(videoElementFromSidebar).attr("data-desc");
        var poster = $(videoElementFromSidebar).attr("poster");
        $(videoElementFromSidebar).siblings("#overlay").addClass("o-1");
        $(videoElementFromSidebar).siblings("#overlay").find("span").addClass("o-1");
        $(player).parent().parent().find("#videoTitle").html(title);
        $(player).parent().parent().find("#videoDescription").html(desc);

        player.attr("src", path);
        player.attr("poster", poster);
        $(player).attr("autoplay") && player[0].play();

        var rightRail = videoContainer.find("#rightBar").height();
        var scrollHeight = videoContainer.find(".video-list__right-pane-content")[0].scrollHeight;
        var downArrow = videoContainer.find(".video-list__down-arrow");
        var topArrow = videoContainer.find(".video-list__top-arrow");
        var sidebarContent = videoContainer.find(".video-list__right-pane-content");

        scrollHeight > rightRail && downArrow.css({ "z-index": "1000000", opacity: "1" });
        downArrow.on("click", function () {
            sidebarContent.animate({ scrollTop: "+=360px" }, 800);
        });
        topArrow.on("click", function () {
            sidebarContent.animate({ scrollTop: "-=360px" }, 800);
        });
        sidebarContent.on("scroll", function () {
            var scrollPosition = sidebarContent.scrollTop();
            if (scrollPosition > 100) topArrow.css({ "z-index": "1000000", opacity: "1" });
            if (scrollPosition < 100) topArrow.css({ "z-index": "-1", opacity: "0" });
            var scrollHeight = sidebarContent[0].scrollHeight;
            if (scrollPosition + rightRail >= scrollHeight - 20) {
                downArrow.css({ "z-index": "-1", opacity: "0" });
            } else {
                downArrow.css({ "z-index": "1000000", opacity: "1" });
            }
        });

        player.one("canplay", function () {
            videoContainer.find(".video-list__shimmer").each(function () {
                $(this).removeClass("video-list__shimmer");
            });
            clearTimeout(shimmerTimer);
            button.css("opacity", "1");
        });
    }

    /**
     * @description Initializing the default page and adding listener to show/hide play button
     */
    function initializeDefaultPage() {
        videoContainer.find("video").each(function () {
            $(this).siblings("#playButton").css("opacity", "1");
            $(this).on("play", function () {
                onDefaultVideoPlay(this);
            });
            $(this).on("pause", function () {
                onDefaultVideoPause(this);
            });

            $(this).on("playing", function () {
                onDefaultVideoPlay(this);
            });
            $(this).on("ended", function () {
                onDefaultVideoPause(this);
            });
            $(this)
                .siblings("button")
                .on("click", function () {
                    defaultVideoPlay($(this).siblings("video"));
                });
        });
    }
    if (property === CAROUSEL_PAGE || (property === SIDEBAR_PAGE && window.innerWidth <= MIN_LAPTOP_WIDTH)) {
        initializeCarouselPage();
    } else if (property === SIDEBAR_PAGE) {
        initializeSidebarPage();
    } else {
        initializeDefaultPage();
    }

    /** Listener so that if user resizes in sidebar mode than to change to carousel mode if size goes down */
    $(window).resize(function () {
        if (window.innerWidth <= MIN_LAPTOP_WIDTH && property === SIDEBAR_PAGE) {
            initializeCarouselPage();
        }
        if (window.innerWidth > MIN_LAPTOP_WIDTH && property === SIDEBAR_PAGE) {
            if (!isSidebarPageInitialized) {
                initializeSidebarPage();
                isSidebarPageInitialized = true;
            }
        }
        if (window.innerWidth > MAX_MOBILE_DEVICE_WIDTH) {
            videoContainer.css("width", "");
            videoContainer.css("margin-left", "");
        }
    });
}

$(window).on("load", function () {
    var isAndroid = /(android)/i.test(navigator.userAgent);
    var isMobile = /(mobile)/i.test(navigator.userAgent);
    var isIos = /(iPhone)/i.test(navigator.userAgent);
    if (isAndroid && isMobile) {
        $(".playButton").each(function () {
            $(this).hide();
        });
    }
    $(".video-list").each(function () {
        loadComponent(this);
        if (isIos) {
            $(this)
                .find("li")
                .each(function () {
                    if (!$(this).find("video").attr("poster")) {
                        var source = $(this).find("video").attr("data-video-path") + "#t=2";
                        $(this).find("video").find("source").first().attr("src", source);
                        $(this).find("video").attr("src", source);
                    }
                });
        }
    });
});
