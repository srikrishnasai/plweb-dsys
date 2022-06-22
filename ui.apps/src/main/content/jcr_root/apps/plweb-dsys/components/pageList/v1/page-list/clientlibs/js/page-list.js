$(document).ready(function () {
    $('.page-list').find('.pages-container').each(function () {
        var length = $(this).attr("data-length");
        var columnCount = $(this).attr('data-columnCount');
        var rawCount;
        if(columnCount == 'two'){
            rawCount=length/2;
            rawCount=Math.ceil(rawCount);
            console.log("rawCount",rawCount);
            $(this).find('.ul-pages-list').css('grid-template-rows', 'repeat('+rawCount+',auto)');
        } else {
            $(this).find('.ul-pages-list').css('grid-template-rows', 'repeat('+length+',auto)');
        }
    });
});