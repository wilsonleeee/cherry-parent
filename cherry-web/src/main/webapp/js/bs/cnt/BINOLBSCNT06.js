var BINOLBSCNT06 = function () {
};
BINOLBSCNT06.prototype = {
	// 取得错误信息HTML
	"getErrHtml" : function (text){
		var errHtml = '<div class="actionError"><ul><li><span>';
		errHtml += text;
		errHtml += '</span></li></ul></div>';
		return errHtml;
	},
    // 清空错误信息
    "deleteActionMsg" : function (){
    	$('#errorMessage').empty();
    },
    // AJAX文件上传
    "ajaxFileUpload" : function (url){
    	// AJAX登陆图片
    	$ajaxLoading = $("#loading");
    	// 错误信息提示区
    	$errorMessage = $('#errorMessage');
    	// 清空错误信息
    	$errorMessage.empty();
    	if($('#upExcel').val()==''){
    		var errHtml = this.getErrHtml($('#errmsg1').val());
    		$errorMessage.html(errHtml);
    		$("#pathExcel").val("");
    		return false;
    	}
    	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
    	$ajaxLoading.ajaxStart(function(){$(this).show();});
    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
    	$.ajaxFileUpload({
	        url: url,
	        secureuri:false,
	        data:{'csrftoken':$('#csrftoken').val(),'brandInfoId':$('#brandInfoId').val()},
	        fileElementId:'upExcel',
	        dataType: 'html',
	        success: function (data){
	        	$("#errorCounters").html("");
	        	if(data.indexOf("actionMessage")>-1){
	        		$errorMessage.html(data);
	        		if(refreshPrivilegeUrl) {
	 					cherryAjaxRequest({
	 						url: refreshPrivilegeUrl,
	 						param: null,
	 						reloadFlg : true,
	 						callback: function(msg) {
	 						}
	 					});
	 				}
	        	}else if($errorMessage.find(".actionSuccess").length > 0) {
	        		$errorMessage.html(data);
	        		if(refreshPrivilegeUrl) {
	 					cherryAjaxRequest({
	 						url: refreshPrivilegeUrl,
	 						param: null,
	 						reloadFlg : true,
	 						callback: function(msg) {
	 						}
	 					});
	 				}
	        	}else{
	        		$("#errorCountersShow").show();
	        		$("#hiddenTable").html(data);
	        		$("#errorMessage").html($("#hiddenTable #errorDiv").html());
	        		$("#errorCounters").html($("#hiddenTable tbody").html());
	        		$("#hiddenTable").html("");
	        	}
	        }
        });
    },
    
    /* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    },
   
    "delErrorCounter":function(trId){
    	$("#errorCounters #"+trId).remove();
    	$("#errorMessage").html("");
    }
};
var BINOLBSCNT06 = new BINOLBSCNT06();