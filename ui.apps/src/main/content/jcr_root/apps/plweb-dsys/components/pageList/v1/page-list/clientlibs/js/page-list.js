$(document).ready(function () {
    $('.bulleted-list').find('.bulleted-list-wrapper').each(function () {
        var length = $(this).attr("data-length");
        var columnCount = $(this).attr('data-columnCount');
        var rawCount;
        if(columnCount == 'two'){
            rawCount=length/2;
            rawCount=Math.ceil(rawCount);
            console.log("rawCount",rawCount);
            $(this).find('.bullet-list').css('grid-template-rows', 'repeat('+rawCount+',auto)');
        }
        else if(columnCount == 'three'){
            rawCount=length/3;
            rawCount=Math.ceil(rawCount);
            console.log("rawCount",rawCount);
            if(length !== "4"){
                $(this).find('.bullet-list').css('grid-template-rows', 'repeat('+rawCount+',auto)');
            }
            else{
                $(this).find('.bullet-list').css('grid-auto-flow', 'row');
            }
        }
    });
});