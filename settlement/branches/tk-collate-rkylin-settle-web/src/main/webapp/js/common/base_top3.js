jQuery(document).ready(function(){
	var bannerId = $("#pageHi").html();
	if(bannerId==null||bannerId==""||bannerId=="undefined"){
		bannerId = "myBanner";
	}
	$("#myBanner").removeClass("hnav_check");
	$("#queryBanner").removeClass("hnav_check");
	$("#enterpriseBanner").removeClass("hnav_check");
	$("#otherBanner").removeClass("hnav_check");
	if(bannerId=="myBanner"){
		$("#myBanner").removeClass("hnav_check");
		$("#myBanner").removeClass("hnav_check");
		$("#myBanner").addClass("hnav_check");
	}else if(bannerId=="queryBanner"){
		$("#queryBanner").removeClass("hnav_check");
		$("#queryBanner").removeClass("hnav_check");
		$("#queryBanner").addClass("hnav_check");
	}else if(bannerId=="enterpriseBanner"){
		$("#enterpriseBanner").removeClass("hnav_check");
		$("#enterpriseBanner").removeClass("hnav_check");
		$("#enterpriseBanner").addClass("hnav_check");
	}else if(bannerId=="safeBanner"){
		$("#safeBanner").removeClass("hnav_check");
		$("#safeBanner").removeClass("hnav_check");
		$("#safeBanner").addClass("hnav_check");
	}else if(bannerId=="otherBanner"){
		$("#otherBanner").removeClass("hnav_check");
		$("#otherBanner").removeClass("hnav_check");
		$("#otherBanner").addClass("hnav_check");
	}else{
		$("#myBanner").removeClass("hnav_check");
		$("#myBanner").removeClass("hnav_check");
		$("#myBanner").addClass("hnav_check");
	}
});
