
// For carousel same height for different image sizes and screen dimensions
function setHeights() {
	var heights = [];
	setTimeout(function () {
		$('.cmp-carousel').each(function () {
			var $this = $(this);
			heights = [];
			$this.find('.cmp-carousel__item').each(function () {
				heights.push($(this).height());
			});
			$(this).find('.cmp-carousel__item').height(Math.max(...heights));
		});
	}, 1500);
}

$(window).on("load", function () {
	setHeights();
	$(window).resize(function () {
		$('.cmp-carousel__item').height('auto');
		setHeights();
	});
});