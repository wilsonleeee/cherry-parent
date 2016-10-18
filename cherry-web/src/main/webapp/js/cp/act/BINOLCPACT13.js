var BINOLCPACT13_global = function () {
	this.needUnlock = true;
};
BINOLCPACT13_global.prototype = {
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
    	$('#errorDiv #errorSpan').html("");
    	$('#successDiv #successSpan').html("");
		$('#errorDiv').hide();
		$('#successDiv').hide();
    },
    // AJAX文件上传
    "ajaxFileUpload" : function (url){
    	$('#errorMessage').empty();
    	$('#errorDiv #errorSpan').html("");
    	$('#successDiv #successSpan').html("");
		$('#errorDiv').hide();
		$('#successDiv').hide();
    	var $form = $("#importForm");
		// 表单验证
		if(!$form.valid()){
			return;
		}
		//上传文件不能为空
		if($('#upExcel').val()==""){
			$('#errorDiv #errorSpan').html($('#errmsg1').val());
			$('#errorDiv').show();
			$("#pathExcel").val("");
			return false;
		}
		var importType = $("input@[name=importType]:checked").val();
    	// AJAX登陆图片
    	$ajaxLoading = $("#loading");
    	// 错误信息提示区
    	$errorMessage = $('#errorMessage');
    	// 清空错误信息
    	$ajaxLoading.ajaxStart(function(){$(this).show();});
    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
    	// 导入按钮
    	var $excelBtn = $("#upload");
    	// 禁用导入按钮
    	$excelBtn.prop("disabled",true);
    	$excelBtn.addClass("ui-state-disabled");
    	$.ajaxFileUpload({
			url : url,
			secureuri : false,
			data : {
				'csrftoken':parentTokenVal(),
				'privilegeFlag' : "1",
				'brandInfoId' : $('#brandInfoId').val(),
				'importType' : importType
			},
			fileElementId : 'upExcel',
			dataType : 'html',
			success: function (msg){
				//释放按钮
				$excelBtn.removeAttr("disabled",false);
				$excelBtn.removeClass("ui-state-disabled");
				if(msg.indexOf('id="actionResultDiv"') > -1){
					$errorMessage.html(msg);
				}else{
					var msgJson = window.JSON.parse(msg);
					if(msgJson.errorMsg!=undefined){
						var  $errorDiv =$('#errorDiv #errorSpan');
						$errorDiv.html(msgJson.errorMsg)
						$('#errorDiv').show();
					}else{
						var  $successDiv =$('#successDiv #successSpan');
						$('#successDiv').show();
						var html='';
						html+=$('#exportTip1').val()+msgJson.totalCount;
						$("#successSpan").html(html);
						//导入成功后刷新父页面
						if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
					}
				}
	        }
		});
    },
	//关闭按钮
    "close":function(){
    	window.close();
	}

};
var BINOLCPACT13 = new BINOLCPACT13_global();

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
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	
	cherryValidate({//form表单验证
		formId: "importForm",		
		rules: {
			importType:{required: true}
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLCPACT13.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};