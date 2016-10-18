var BINOLPLSCF15 = function() {
};
BINOLPLSCF15.prototype = {
	/*
	 * 弹出新画面
	 */
	"popupWin" : function(obj, optKbn, suffix) {
		$("#actionResultDisplay").empty();
		
		var url = $(obj).attr("href");
		var flowType = $(obj).parent().find("#flowType").val();
		var param = "?&flowType="+flowType+"&brandInfoId="+$("#brandInfoId").val();
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		url = url+param;
		openWinByUrl(url);
	},
	
	/*
	 * 更改品牌
	 */
	"changeBrand":function(){
		var url = $("#changeBrand_url").attr("href");
		var param = "brandInfoId="+$("#brandInfoId").val();
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg){
			var jsons = eval('('+msg+')');
			if(jsons.length > 0) {
				for(var i in jsons) {
					if(jsons[i].configStatus == 1){
						$("#dataTable tr:eq("+i+") td:eq(2)").html($("#status_custom").val());
					}else{
						$("#dataTable tr:eq("+i+") td:eq(2)").html($("#status_default").val());
					}
				}
			}
		};
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
};

var binOLPLSCF15 = new BINOLPLSCF15();