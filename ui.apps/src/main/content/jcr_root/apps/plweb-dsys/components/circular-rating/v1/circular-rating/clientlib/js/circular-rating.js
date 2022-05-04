$(document).ready(function () {
    var checkIfElementIsVisibleForCircularRating = function (baseElement, elem) {
        var startAnimation = baseElement.hasClass("go");
        if (startAnimation) {
            elem.addClass("animate-rating-circle");
        } else {
            setTimeout(function () {
                checkIfElementIsVisibleForCircularRating(baseElement, elem);
            },100);
        }
    };
    $(".circular-rating__circle-inner-wrapper").each(function () {
        checkIfElementIsVisibleForCircularRating($(this), $(this).find(".rating-circle"));
    });
});
