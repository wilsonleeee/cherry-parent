var BINOLPTJCS25 = function () {
};
BINOLPTJCS25.prototype = {
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
var BINOLPTJCS25 = new BINOLPTJCS25();

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