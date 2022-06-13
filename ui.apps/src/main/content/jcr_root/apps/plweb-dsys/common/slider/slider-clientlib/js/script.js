(function ($, $document) {
    "use strict";
    $document.on("dialog-ready", function () {
        $('.custom-dialog-slider__elem').each(function () {
            var slider,
                $sliderContainer,
                $hiddenInput;
            slider = new Coral.Slider();
            $sliderContainer = $(this).closest('.custom-dialog-slider__wrapper');
            $hiddenInput = $sliderContainer.find('.custom-dialog-slider__val');
            slider.set({
                min: 0,
                max: 100,
                value: $hiddenInput.val(),
                step: 2,
                tooltips: true,
                filled: true
            });
            $(this).append(slider); 
            $(slider).on('change', function () { 
                $hiddenInput.val($(this).val());
            });
        })
    });
})($, $(document));