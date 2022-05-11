(function(document, $) {
    var $document = $(document);

    function updateHref($link, $input) {
        if ($link && $input) {
            var inputVal = $input.val();
            if (inputVal) {
                $link.show();
                $link.attr('href', '/editor.html' + inputVal);
            } else {
                $link.hide();
            }
        }
    }

    function addLinkToCF(elem) {
        if (elem) {
            var $el = $(elem);
            var $elWrapper = $el.closest('.coral-PathBrowser.add-path-editor-link');
            if ($elWrapper && $elWrapper.length) {
                var $link = $elWrapper.closest('.coral-Form-fieldwrapper').find('.content-fragment-path-link');
                if ($link && $link.length) {
                    updateHref($link, $el);
                } else {
                    $link = $('<a href="#" style="display: inline-block; margin-top: 5px; font-size: 0.9rem; font-weight: 700; line-height:1.6rem;" class="content-fragment-path-link" target="blank">Edit Content Fragment</a>');
                    updateHref($link, $el);
                    $link.insertAfter($elWrapper);
                }
            }
        }
    }

    $document.on('foundation-contentloaded', function(e) {
        $('.add-path-editor-link .js-coral-pathbrowser-input').each(function() {
            addLinkToCF(this);
        });
    });

    $document.on('dialog-ready', function(e) {
        $('.add-path-editor-link .js-coral-pathbrowser-input').each(function() {
            addLinkToCF(this);
        });
    });

    $document.on('change keydown keyup', '.add-path-editor-link .js-coral-pathbrowser-input', function(e) {
        addLinkToCF(this);
    });
})(document, Granite.$);
