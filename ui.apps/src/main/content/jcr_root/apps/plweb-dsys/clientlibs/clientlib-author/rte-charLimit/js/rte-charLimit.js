(function (document, $) {
    "use strict";
    $(document).on("foundation-contentloaded", function (e) {
        var $rteContainer = $('.custom-rte').closest('.richtext-container');
        var $rteBox = $rteContainer.find('.coral-RichText.coral-DecoratedTextfield-input');
        var maxCharLimit = $('.custom-rte').attr('data-max-char-limit');
        var ctrlDown = false,
            ctrlKey = 17,
            cmdKey = 91,
            vKey = 86,
            maxCharCount = maxCharLimit || 250;

        var escapeCodes = [8, 46, 37, 38, 39, 40, 35, 36, 45, 27]; // backspace, delete, leftarrow, uparrow, downarrow, rightarrow, home, end, insert, escape

        function truncateAndUpdate(el, text) {
            var truncatedText = text.substring(0, maxCharCount) || ''
            $(el).text(truncatedText);
            $(el).focusEnd();
            $rteContainer.find('.custom-rte__char-count').html(maxCharCount - truncatedText.length);
        }

        function secondCheckAndChangeText(el) {
            var text = removeMarkup($(el).text());
            if (maxCharCount - text.length < 0) {
                truncateAndUpdate(el, text);
            }
        }

        function removeMarkup(val) {
            var divElem = document.createElement('div');
            divElem.innerHTML = val;
            return divElem.textContent;
        }

        function updateCount(el, text) {
            secondCheckAndChangeText(el);
            var truncatedText = removeMarkup($(el).text());
            $rteContainer.find('.custom-rte__char-count').html(maxCharCount - truncatedText.length);
        }

        function addCharCount() {
            var curCharCount = maxCharCount;
            if ($rteBox.text()) {
                curCharCount = $rteBox.text().length;
            } else {
                curCharCount = 0;
            }
            var $rteCountTextEle = $rteContainer && $rteContainer.find('.custom-rte__char-count');
            if ($rteCountTextEle && $rteCountTextEle.length) {
                $rteCountTextEle.innerHTML = (maxCharCount - curCharCount);
            } else {
                //Charcters counter 
                $rteContainer.append("<h3><span class='custom-rte__char-count'>" + (maxCharCount - curCharCount) + "</span> chars remaining</h3>");
            }
        }

        // ON TEXT INPUT 
        function onTextInput(e) {
            var text = removeMarkup($(this).text());

            // on pasting code, check if no of characters is greater than max and truncate if needed
            if (ctrlDown && e.keyCode == vKey) {
                truncateAndUpdate(this, $(this).text());
            }

            // update remaining character letter count
            updateCount(this, text);

            // prevent default when pressed key is not one of the escape codes, and when the charCount exceeds maxCharcount
            if (text.length >= maxCharCount && escapeCodes.indexOf(e.keyCode) < 0) {
                e.preventDefault();
                return;
            }
        }

        addCharCount();
        $rteBox.on('keydown keyup', onTextInput);

        // focus end jQuery
        $.fn.focusEnd = function () {
            $(this).focus();
            var tmp = $('<span />').appendTo($(this)),
                node = tmp.get(0),
                range = null,
                sel = null;

            if (document.selection) {
                range = document.body.createTextRange();
                range.moveToElementText(node);
                range.select();
            } else if (window.getSelection) {
                range = document.createRange();
                range.selectNode(node);
                sel = window.getSelection();
                sel.removeAllRanges();
                sel.addRange(range);
            }
            tmp.remove();
            return this;
        }

        // CHECK ctrl key press
        $(document).keydown(function (e) {
            if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = true;
        }).keyup(function (e) {
            if (e.keyCode == ctrlKey || e.keyCode == cmdKey) ctrlDown = false;
        });
    });

})(document, Granite.$);

