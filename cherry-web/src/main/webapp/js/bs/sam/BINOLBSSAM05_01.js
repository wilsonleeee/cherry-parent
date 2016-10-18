function BINOLBSSAM05_01(){
	this.needUnlock = true;
};

BINOLBSSAM05_01.prototype={
		"close": function(){
			window.close();
			// 解除父页面锁定
			window.opener.unlockParentWindow();
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
	    	$('#errorMessage').empty();
	    	$('#errorDiv #errorSpan').html("");
	    	$('#successDiv #successSpan').html("");
			$('#errorDiv').hide();
			$('#successDiv').hide();
	    },
	    "formValid" : function(){
	    	var flag = true;
	    	var $form = $("#mainForm");
	    	// 表单验证
	    	flag = $form.valid();
	    	//批次号验证
			return flag;
	    },
	    // AJAX文件上传
	    "ajaxFileUpload" : function (url){
	    	var $that=this;
	    	$that.deleteActionMsg();
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#errorMessage');
	    	// 清空错误信息
	    	$errorMessage.empty();
	    	var $form = $("#mainForm");
	    	//表单验证
	    	if(!$that.formValid()){
	    		return false;
	    	}
	    	if($('#upExcel').val()==''){
	    		var errHtml = $that.getErrHtml($('#errmsg1').val());
	    		$errorMessage.html(errHtml);
	    		$("#pathExcel").val("");
	    		return false;
	    	}
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});;
	    	// 导入按钮
	    	var $excelBtn = $("#upload");
	    	// 禁用导入按钮
	    	$excelBtn.prop("disabled",true);
	    	$excelBtn.addClass("ui-state-disabled");
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{
		        		'csrftoken':parentTokenVal(),
		        		'brandInfoId':$('#brandInfoId').val(),
		        		'importRepeat' : $('#importRepeat').val()
		        	},
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (msg){
		        	//释放按钮
		        	$excelBtn.removeAttr("disabled",false);
					$excelBtn.removeClass("ui-state-disabled");
		        	$('#errorMessage').html(msg);
		        	var errorCount = $('#fFailed').html();
		        	if( errorCount != null && errorCount !="" && errorCount != 0){
		        		$('#errorSpan').html($('#failedResult').html());
		        		$('#errorDiv').show();
		        	}if(errorCount == 0){
		        		$('#successSpan').html($('#successResult').html());
		        		$('#successDiv').show();
		        	}
		        	if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		        }
	        });
	    },
};
var BINOLBSSAM05_01 = new BINOLBSSAM05_01();

/**
* 页面初期处理
*/

$.fn.selectRange = function(start, end) {
	return this.each(function() {
		if (this.setSelectionRange) {
			this.focus();
			this.setSelectionRange(start, end);
		} else if (this.createTextRange) {
			var range = this.createTextRange();
			range.collapse(true);
			range.moveEnd('character', end);
			range.moveStart('character', start);
			range.select();
		}
	});
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLBSSAM05_01.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLBSSAM05_01.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};
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
		formId: "mainForm",		
		rules: {
			importRepeat:{required: true},
			brandInfoId:{required: true}
		}		
	});
});
