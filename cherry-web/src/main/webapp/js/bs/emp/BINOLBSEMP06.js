var BINOLBSEMP06 = function() {
	
};

BINOLBSEMP06.prototype = {
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
	    	// 验证是否选择了要导入的文件
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
		        	$("#errorBA").html("");
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
		        	}else if(data.indexOf("button-text") > -1 ){
		        		$("#errorBAShow").show();
		        		$("#hiddenTable").html(data);
		        		$("#errorMessage").html($("#hiddenTable #errorDiv").html());
		        		$("#errorBA").html($("#hiddenTable tbody").html());
		        		$("#hiddenTable").html("");
		        	} else {
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
		        	}
		        }
	        });
	    },
	    "delErrorBA":function(trId){
	    	$("#errorBA #"+trId).remove();
	    	$("#errorMessage").html("");
	    }
};

var BINOLBSEMP06 = new BINOLBSEMP06();
