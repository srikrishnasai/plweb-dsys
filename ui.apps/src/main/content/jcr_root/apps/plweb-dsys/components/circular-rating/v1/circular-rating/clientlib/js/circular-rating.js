(function () {
    $(document).ready(function()  {

        var OBSERVER_CLASS_NAME = ".circular-rating__circle-inner-wrapper"

        var ANIMATION_CLASS_NAME = "animate-rating-circle"

        var CLASS_TO_ADD_ANIMATION = ".rating-circle"

        

        /** Check if browser is IE or not */
        if (!window.document.documentMode) {
            var circularRating = document.querySelectorAll(OBSERVER_CLASS_NAME);
            circularRating.forEach(function (el) {
                var observer = new IntersectionObserver(observerCallback, { threshold: [0] });
                
                function observerCallback(entries) {
                    if (entries[0].isIntersecting === true) {
                        var elem = entries[0].target;
                        var ratingCircle = elem.querySelector(CLASS_TO_ADD_ANIMATION);
                        if (!ratingCircle.classList.contains(ANIMATION_CLASS_NAME)) {
                            ratingCircle.classList.add(ANIMATION_CLASS_NAME);
                        }
                        observer.unobserve(entries[0].target);
                    }
                }
                observer.observe(el);
            });
        } else {
            var PREREQUISITE_CLASS_NAME = "go"

            var ELEM_VISIBLE_TIMER = 100

            var checkIfElementIsVisibleForCircularRating = function (baseElement, elem) {
                var startAnimation = baseElement.hasClass(PREREQUISITE_CLASS_NAME);
                if (startAnimation) {
                    elem.addClass(ANIMATION_CLASS_NAME);
                } else {
                    setTimeout(function () {
                        checkIfElementIsVisibleForCircularRating(baseElement, elem);
                    }, ELEM_VISIBLE_TIMER);
                }
            };
            $(OBSERVER_CLASS_NAME).each(function () {
                checkIfElementIsVisibleForCircularRating($(this), $(this).find(CLASS_TO_ADD_ANIMATION));
            });
        }
    });
})();
