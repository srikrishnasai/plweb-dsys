$(document).ready(function () {
    
    // Add Dynamic row for div
    $('.bulleted-list').find('.bulleted-list-wrapper').each(function () {
        var length = $(this).attr("data-length");
        var columnCount = $(this).attr('data-columnCount');
        var rawCount;
        if(columnCount == 'two'){
            rawCount=length/2;
            rawCount=Math.ceil(rawCount);
            $(this).find('.bullet-list').css('grid-template-rows', 'repeat('+rawCount+',auto)');
        }
        else if(columnCount == 'three'){
            rawCount=length/3;
            rawCount=Math.ceil(rawCount);
            if(length !== "4"){
                $(this).find('.bullet-list').css('grid-template-rows', 'repeat('+rawCount+',auto)');
            }
            else{
                $(this).find('.bullet-list').css('grid-auto-flow', 'row');
            }
        }
    });

    //remove default style
    $('.bulleted-list').each(function() {
       var hasCheckmarClass = $(this).hasClass('cmp-list--checkmarks');
       var hasNumberClass = $(this).hasClass('cmp-list--numbers');

       if(hasCheckmarClass || hasNumberClass){
            $(this).find('.bullet-list').removeClass('bullet-list-bullet');
       }
    });
});