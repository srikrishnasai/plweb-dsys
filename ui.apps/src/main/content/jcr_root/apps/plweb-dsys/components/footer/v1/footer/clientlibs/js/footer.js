
(function () {

    var UP_ARROW_CLASS = "footer--up-arrow"

    var LIST_HEIGHT = 40

    function showList(ulElem, button) {
        var maxHeight = $(ulElem).children().length * LIST_HEIGHT;
        var hasFooterUpClass = $('.footer__list').find('.footer--up-arrow');

        if (hasFooterUpClass.length) {
            $('.footer--up-arrow').parent().parent().find("ul").css("max-height", "0px");
            $('.footer__list').find('.footer--up-arrow').toggleClass(UP_ARROW_CLASS);
        }
        $(ulElem).css("max-height", maxHeight + "px")
        $(button).toggleClass(UP_ARROW_CLASS);
    }

    function hideList(ulElem, button) {
        $(ulElem).css("max-height", "0px")
        $(button).toggleClass(UP_ARROW_CLASS);
    }

    function calculateHeight() {
        $('.footer').parent().css("padding-bottom", $('footer').height() + 'px');
    }

    $(window).on("load", function () {
        calculateHeight();
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
    })

    $(document).ready(function () {
        calculateHeight();
        document.addEventListener('readystatechange', (event) => {
            calculateHeight();
        });
    });
})()


