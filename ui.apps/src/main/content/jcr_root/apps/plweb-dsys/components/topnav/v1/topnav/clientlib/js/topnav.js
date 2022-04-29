(function(){
    $(window).on("load",function(){
        $(".topnav__all-sites").each(function(){
                $(this).on("click", function(e){
                    e.stopPropagation()
                   $(this).find("#list").toggleClass("show-list")
                })
        })
    })
    $(window).on("click",function(e){
        $("#list").removeClass("show-list")
    })
})()