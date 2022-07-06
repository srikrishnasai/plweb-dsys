
(function () {

    var UP_ARROW_CLASS = "footer--up-arrow"

    var LIST_HEIGHT = 40

    var FOOTER_HEIGHT = 0;

    function showList(ulElem, button) {
        var maxHeight = $(ulElem).children().length * LIST_HEIGHT;
        var hasFooterUpClass = $('.footer__list').find('.footer--up-arrow');

        if (hasFooterUpClass.length) {
            $('.footer--up-arrow').parent().parent().find("ul").css("max-height", "0px");
            $('.footer__list').find('.footer--up-arrow').toggleClass(UP_ARROW_CLASS);
        }

        calculateHeight(FOOTER_HEIGHT + (maxHeight - 20) + 'px');
        $(ulElem).css("max-height", maxHeight + "px", false)
        $(button).toggleClass(UP_ARROW_CLASS);
    }

    function hideList(ulElem, button) {
        calculateHeight(FOOTER_HEIGHT + 'px', false);
        $(ulElem).css("max-height", "0px")
        $(button).toggleClass(UP_ARROW_CLASS);
    }

    function calculateHeight(height, initial = true) {
        $('.footer').parent().css({
            "transition": `padding-bottom ${initial ? '0s' : '0.5s'} linear`,
            "padding-bottom": height,
        });
    }

    $(window).on("load", function () {
        FOOTER_HEIGHT = $('footer').height();
        calculateHeight(FOOTER_HEIGHT + 'px');
        $(".footer__nav-bar").each(function () {
            $(this).find(".footer__drop-down").each(function () {
                $(this).on("click", function () {
                    var ulElem = $(this).parent().parent().find("ul")
                    if (!$(this).hasClass(UP_ARROW_CLASS)) {
                        showList(ulElem, this)
                    }
                    else hideList(ulElem, this)
                })
            })
        })

        $(window).resize(function () {
            FOOTER_HEIGHT = $('footer').height();
            calculateHeight(FOOTER_HEIGHT + 'px');
        });
    })

    $(document).ready(function () {
        calculateHeight($('footer').height() + 'px');
        document.addEventListener('readystatechange', (event) => {
            calculateHeight($('footer').height() + 'px');
        });
    });
})()