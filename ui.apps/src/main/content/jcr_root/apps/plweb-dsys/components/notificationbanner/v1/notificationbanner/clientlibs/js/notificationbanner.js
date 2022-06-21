$(document).ready(function() {
    $(".notification_bar").each(function(){
		var bannerPosition = $(this).attr("data-banner-position");
  		var sticky = $(this).attr("data-is-sticky");
        if(bannerPosition != 'none'){ 
    		setCSSProperties(bannerPosition , this);
    	}
    })


    var readCookie = localStorage.getItem("notificationBanner");
    if($(".cookienotification").attr("data-banner-type") === 'cookienotification'){
		// To set cookie in local storage if the exising cookie is expired.
        if (typeof(readCookie) != 'undefined' && readCookie != null) {  
            var cookieValue = JSON.parse(readCookie);
            if(new Date().getTime() > cookieValue.expires){
                localStorage.removeItem("notificationBanner");               
                }
                else{
					// To hide the banner if the exising cookie is not expired.
                    $(".cookienotification").css("display","none");
                    var bannerPosition = $(".cookienotification").attr("data-banner-position");
    				if(bannerPosition === "bottom"){
                        $(".footer").css("margin-bottom","0");
                        $('.footer').parent().css("margin-bottom","0");
                    }
                        else{
                            document.body.style.marginTop = "0px";
                    }
                }
        }
        else{
            localStorage.removeItem("notificationBanner");
        }
    }
});

// To set new cookie in local storage.
function setCookieBanner(curElem) {
    localStorage.removeItem("notificationBanner");
    var cookieExpiry = $(curElem).parent().attr("data-cookie-expiry");
    var object = {expires: new Date().getTime() + cookieExpiry * 24 * 60 * 60 * 1000};
	localStorage.setItem("notificationBanner", JSON.stringify(object));
	$(curElem).parent().css("display","none");
    var bannerPosition = $(curElem).parent().attr("data-banner-position");
    if(bannerPosition === "bottom"){
    	//document.body.style.marginBottom = "0px";
        $('.footer').parent().css("margin-bottom","0");
        $(".footer").css("margin-bottom","0");
    }
    else{
		document.body.style.marginTop = "0px";
    }

}

// To set the type of the banner: either Cookie notification or Normal notification banner
function setBannerType(curElem){
	var bannerType = $(curElem).parent().attr("data-banner-type");
    if(bannerType === 'cookienotification'){
		setCookieBanner(curElem);
    }
    if(bannerType === 'normalnotification'){
		var targetPath = $(curElem).attr("data-path");
        var targetTab = $(curElem).attr("data-path-tab");
        var buttonType = $(curElem).attr("data-button-type");
        var bannerPosition = $(curElem).parent().attr("data-banner-position");

        if(buttonType === "linkButton"){
        	if(targetTab === "true"){
				window.open(targetPath + ".html",'_blank');
            }else{
				window.location.href = targetPath + ".html";
            }
        }
        else{
            $(curElem).parent().css("display","none");
            // To remove the margin that given for the notificationbar.
            if(bannerPosition === "bottom"){
                document.body.style.marginBottom = "0px";
                }
                else{
                    document.body.style.marginTop = "0px";
                }
            }

    }
}

// To set the css property dynamically when banner position is either top or bottom.
// param1: postion of the banner -> Top or Bottom
// param2: Current element
function setCSSProperties(bannerPosition,elem){
	var notificationBar = $(elem);
    notificationBar.css("left" ,"0");
    if(bannerPosition === 'bottom'){
        if($("#wcmmmode").attr("data-wcm-mode") !== 'EDIT' && $("#wcmmmode").attr("data-wcm-mode") !== 'edit' ){
	      	//document.body.style.marginBottom = notificationBar.innerHeight() + "px";
            $('.footer').parent().css("margin-bottom", notificationBar.innerHeight() + 'px')
            $(".footer").css("margin-bottom", notificationBar.innerHeight() + 'px');
            notificationBar.addClass('show-banner');
            notificationBar.css("position" ,"fixed");
        }
        notificationBar.css("bottom", "0")
    } 
    if(bannerPosition === 'top'){
        if($("#wcmmmode").attr("data-wcm-mode") !== 'EDIT' && $("#wcmmmode").attr("data-wcm-mode") !== 'edit' ){
       		document.body.style.marginTop = notificationBar.innerHeight() + "px";
            notificationBar.addClass('show-banner');
            notificationBar.css("position" ,"fixed");
        }
       notificationBar.css("top", "0")
	} 
} 