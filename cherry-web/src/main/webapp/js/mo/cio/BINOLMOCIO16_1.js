function BINOLMOCIO16_1(){
	this.needUnlock = true;
};

BINOLMOCIO16_1.prototype={
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
	    	// 批次号验证【英字母与数字组成】
	    	if($('#isChecked').attr("checked") == undefined ){
	    		var importBatchCode = $('#importBatchCode').val();
	    		var reg = /^[A-Za-z0-9]+$/i;
	    		var errorMsg = '';
	    		if(importBatchCode == undefined || importBatchCode == ''){
	    			errorMsg = $('#errmsg2').val();
	    		}else if(importBatchCode.length > 25){
	    			errorMsg = $('#errmsg3').val();
	    		}else if(!reg.test(importBatchCode)){
	    			errorMsg = $('#errmsg4').val();
	    		}
	    		if(errorMsg != ''){
	    			var $obj = $("#importBatchCodeSpan");
	    			$obj.removeClass('error');
	    			$obj.find("#errorText").remove();
	    			$obj.addClass("error");
	    			$obj.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
	    			$obj.find('#errorText').attr("title",'error|'+errorMsg);
	    			$obj.find('#errorText').cluetip({
	    		    	splitTitle: '|',
	    			    width: 150,
	    			    cluezIndex: 20000,
	    			    tracking: true,
	    			    cluetipClass: 'error', 
	    			    arrows: true, 
	    			    dropShadow: false,
	    			    hoverIntent: false,
	    			    sticky: false,
	    			    mouseOutClose: true,
	    			    closePosition: 'title',
	    			    closeText: '<span class="ui-icon ui-icon-close"></span>'
	    			});
	    			flag = false;
	    		}
	    	}
			return flag;
	    },
	    
	    /**
		 * ajax文件导入
		 * @param url:导入URL
		 * @param falg: 0: 只导入柜台消息不进行下发
		 * 		  		1：导入柜台消息并进行下发
		 */
	    "ajaxFileUpload" : function (url,flag){
	    	var $that=this;
	    	$that.deleteActionMsg();
	    	// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#errorMessage');
	    	// 清空错误信息
	    	$errorMessage.empty();
	    	var $form = $("#mainForm");
	    	$('#importBatchCode').val($.trim($('#importBatchCode').val()));
	    	$('#comments').val($.trim($('#comments').val()));
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
	    	// 仅导入消息按钮
	    	var $excelBtn_0 = $("#upload_0");
	    	// 导入消息并下发按钮
	    	var $excelBtn_1 = $("#upload_1");
	    	// 禁用导入按钮
	    	$excelBtn_1.prop("disabled",true);
	    	$excelBtn_0.addClass("ui-state-disabled");
	    	$excelBtn_1.prop("disabled",true);
	    	$excelBtn_1.addClass("ui-state-disabled");
	    	$.ajaxFileUpload({
		        url: url,
		        secureuri:false,
		        data:{
		        		'csrftoken':parentTokenVal(),
		        		'brandInfoId':$('#brandInfoId').val(),
		        		'importBatchCode' : $('#importBatchCode').val(),
		        		'comments' : $('#comments').val(),
		        		'privilegeFlag' : '1',
		        		'isPublish' : flag,
		        		'isChecked' : $('#isChecked').attr("checked")
		        	},
		        fileElementId:'upExcel',
		        dataType: 'html',
		        success: function (msg){
		        	//释放按钮
		        	$excelBtn_0.removeAttr("disabled",false);
		        	$excelBtn_0.removeClass("ui-state-disabled");
		        	$excelBtn_1.removeAttr("disabled",false);
		        	$excelBtn_1.removeClass("ui-state-disabled");
		        	$('#errorMessage').html(msg);
		        	var errorCount = $('#fFailed').html();
		        	if( errorCount != null && errorCount !="" && errorCount != 0){
		        		$('#errorSpan').html($('#failedResult').html());
		        		$('#errorDiv').show();
		        	}if(errorCount == 0){
		        		$('#successSpan').html($('#successResult').html());
		        		$('#successDiv').show();
		        	}
		        	if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		        }
	        });
	    },
	    
	    "close":function(){
	    	window.close();
		},
		
		"isChecked":function(thisObj){
			if(thisObj.checked){
				$('#importBatchCodeSpan').hide();
			}else{
				$('#importBatchCodeSpan').show();
			}
		}
};

var BINOLMOCIO16_1 = new BINOLMOCIO16_1();

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
//	cherryValidate({//form表单验证
//		formId: "mainForm",		
//		rules: {
//			comments:{required: true,maxlength: 200}
//		}		
//	});
});
window.onbeforeunload = function(){
	if (BINOLMOCIO16_1.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};