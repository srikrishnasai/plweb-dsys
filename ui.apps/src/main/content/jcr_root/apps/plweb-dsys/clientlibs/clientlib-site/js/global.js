
// For carousel same height for different image sizes and screen dimensions
function setHeights() {
	var heights = [];
	$('.cmp-carousel__item').each(function () {
		heights.push($(this).height());
	});

	$('.cmp-carousel__item').height(Math.max(...heights));
}

$(document).ready(function () {
	console.log($);
	setHeights();

	$(window).resize(function () {
		$('.cmp-carousel__item').height('auto');
		setHeights();
	});

});