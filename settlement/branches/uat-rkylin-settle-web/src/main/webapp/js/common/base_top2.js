jQuery(document).ready(function(){
	var titleId = $("#titleID").html();
	var topId = $("#topID").html();
	if(topId==null||topId==""||topId=="undefined"){
		topId = "other";
	}
	if(titleId==null||titleId==""||titleId=="undefined"){
		titleId = "";
	}
	if(titleId=="about"){
		$("#indexLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#servicesLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#safeLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#productLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#solutionLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}else if(titleId=="services"){
		$("#indexLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#aboutLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#safeLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#productLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#solutionLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}else if(titleId=="safe"){
		$("#indexLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#aboutLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#servicesLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#productLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#solutionLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}else if(titleId=="product"){
		$("#indexLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#aboutLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#servicesLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#safeLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#solutionLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}else if(titleId=="solution"){
		$("#indexLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#aboutLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#servicesLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#safeLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#productLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}else if(titleId=="index"){
		$("#aboutLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#servicesLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#safeLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#productLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#solutionLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}else{
		$("#indexLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#aboutLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#servicesLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#safeLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#productLI").removeClass("cir_cornerl02 nfirst hnav_check");
		$("#solutionLI").removeClass("cir_cornerl02 nfirst hnav_check");
	}
	if(titleId!=null&&titleId!=""){
		$("#"+titleId+"LI").addClass("cir_cornerl02 nfirst hnav_check");	
	}
	
	if(topId=="index"){
		$("#topRegA").removeClass("active");
		$("#topLoginA").removeClass("active");
		$("#topLoginA").addClass("active");
	}else if(topId=="register"){
		$("#topLoginA").removeClass("active");
		$("#topRegA").removeClass("active");
		$("#topRegA").addClass("active");
	}else if(topId=="login"){
		$("#topRegA").removeClass("active");
		$("#topLoginA").removeClass("active");
		$("#topLoginA").addClass("active");
	}else if(topId=="help"){
		$("#topRegA").removeClass("active");
		$("#topLoginA").removeClass("active");
		$("#topLoginA").addClass("active");
	}else{
		$("#topRegA").removeClass("active");
		$("#topLoginA").removeClass("active");
	}
	
});
