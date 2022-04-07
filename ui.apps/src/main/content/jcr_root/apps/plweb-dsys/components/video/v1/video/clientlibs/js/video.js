function addListeners(elem, showPlayButton, hidePlayButton) {
    elem.on("play", hidePlayButton);
    elem.on("pause", showPlayButton);

    elem.on("playing", hidePlayButton);
    elem.on("ended", showPlayButton);
}

$(window).on("load", function () {
    $(".video__main").each(function () {
        var videoElem = $(this).find("video");
        var button = $(this).find("button");

        function showPlayButton() {
            $(button).css("opacity", "1");
            $(button).css("z-index", "2");
        }

        function hidePlayButton() {
            $(button).css("opacity", "0");
            $(button).css("z-index", "-1");
        }
        button.on("click", function () {
            hidePlayButton();
            videoElem[0].play();
        });
        addListeners(videoElem, showPlayButton, hidePlayButton);
        videoElem.one("canplay", function () {
            button.css("opacity", "1");
        });
    });
});
