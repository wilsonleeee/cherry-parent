var BINOLPTJCS05 = function () {
};
BINOLPTJCS05.prototype = {
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
    	$ajaxLoading.ajaxStart(function(){$(this).show();});
    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
    	$.ajaxFileUpload({
	         url: url,
	         secureuri:false,
	         data:{'csrftoken':$('#csrftoken').val(),'brandInfoId':$('#brandInfoId').val()},
	         fileElementId:'upExcel',
	         dataType: 'html',
	         success: function (data){
	        	 $errorMessage.html(data);
	         }
        });
    }
};
var BINOLPTJCS05 = new BINOLPTJCS05();