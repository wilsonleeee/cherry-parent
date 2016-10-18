
function BINOLPTUNQ03() {};

BINOLPTUNQ03.prototype = {	
		"close" : function() {
			window.close();
		},
		// 取得错误信息HTML
		"getErrHtml" : function (text){
			var errHtml = '<div class="actionError"><ul><li><span>';
			errHtml += text;
			errHtml += '</span></li></ul></div>';
			return errHtml;
		},
	    // 清空错误信息
	    "deleteActionMsg" : function (){
//	    	$('#errorMessage').empty();
	    	$("#errorMessageTemp").hide();
	    },

	    // AJAX文件上传
	    "ajaxFileUpload" : function (url){
	    	
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	// 清空错误信息
//	    	$('#errorMessage').empty();
	    	$("#errorMessageTemp").hide();
	    	$("#successMessageTemp").hide();
	    	if($('#upExcel').val().length == 0){
	    		var errHtml = this.getErrHtml($('#errmsg1').val());
//	    		$('#errorMessage').html(errHtml);
	    		$('#errorMessage').show();
	    		$("#pathExcel").val("");
	    		return false;
	    	}
	    	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
	    	// 导入维护的唯一码不过校验的时候，八秒后自动隐藏加载旋转图片
	    	setTimeout(function(){ $("#loading").hide(); },8000);
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{'csrftoken':$('#csrftoken').val(),'brandInfoId':$('#brandInfoId').val()},
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (data){
		        	var msgJson = eval("("+data+")");
		        	if(msgJson.errorCode == "1"){
                    // 提示输入的Excel为空
	        		$("#errorMessageTemp").show();
	        		}else{
	                 // 维护成功提示
		        	 $("#successMessageTemp").show();
	        		}
		        }
	        });
	      
	    }
}
var BINOLPTUNQ03 =  new BINOLPTUNQ03();

$(document).ready(function() {
	$('#expandCondition').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#moreCondition').show('slow');
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#moreCondition').hide('slow');
		}
	});
	
});