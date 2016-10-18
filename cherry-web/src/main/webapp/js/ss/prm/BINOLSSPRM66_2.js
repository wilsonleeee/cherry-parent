var BINOLSSPRM66_2 = function () {
	this.needUnlock = true;
};
BINOLSSPRM66_2.prototype = {
	// 取得错误信息HTML
	"getErrHtml" : function (text){
		var errHtml = '<div class="actionError"><ul><li><span>';
		errHtml += text;
		errHtml += '</span></li></ul></div>';
		return errHtml;
	},
    // 清空错误信息
    "deleteActionMsg" : function (){
    	$('#actionResultDisplay').empty();
    	$('#errorMessage').empty();	
    },
    "formValid" : function(){
    	var flag = true;
    	var $form = $("#importForm");
    	// 表单验证
    	flag = $form.valid();
    	//批次号验证
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
    // AJAX文件上传
    "ajaxFileUpload" : function (url){
    	var $that=this;
    	//清除错误信息
    	$that.deleteActionMsg();
		// AJAX登陆图片
    	$ajaxLoading = $("#loading");
    	$errorMessage = $('#errorMessage');
    	var $form = $("#importForm");
    	$('#importBatchCode').val($.trim($('#importBatchCode').val()));
    	$('#comments').val($.trim($('#comments').val()));
		// 表单验证
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
				'brandInfoId' : $('#brandInfoId').val(),
				'importBatchCode' : $('#importBatchCode').val(),
				'comments' : $('#comments').val(),
				'importRepeat' : $('#importRepeat').val(),
		        'isChecked' : $('#isChecked').attr("checked")
			},
			fileElementId : 'upExcel',
			dataType : 'html',
			success: function (msg){
				//释放按钮
				$excelBtn.removeAttr("disabled",false);
				$excelBtn.removeClass("ui-state-disabled");
				//导入成功后刷新父页面
				if(window.opener.oTableArr[0] != null){
					window.opener.oTableArr[0].fnDraw();
				}
				$("#actionResultDisplay").html(msg);
	        }
		});
    },
	//关闭按钮
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
var BINOLSSPRM66_2 = new BINOLSSPRM66_2();
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
			importRepeat:{required: true},
			comments:{required: true}
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLSSPRM66_2.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};