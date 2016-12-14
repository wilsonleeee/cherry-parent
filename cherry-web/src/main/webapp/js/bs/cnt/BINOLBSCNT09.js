 var BINOLBSCNT09 = function () {
	this.needUnlock = true;
};
BINOLBSCNT09.prototype = {
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
    	var dataArr = {
    			'csrftoken':parentTokenVal(),
				'brandInfoId' : $('#brandInfoId').val(),
				'pointType' : $('#pointType').val(),
				'importType' : $('#importType').val(),
				'comment' : $('#comment').val(),
				'importName' : $('#importName').val()
			}
    	if ($('#memberClubId').length > 0) {
    		dataArr = $.extend(dataArr, {'memberClubId' : $('#memberClubId').val()});
    	}	
    	$.ajaxFileUpload({
			url : url,
			secureuri : false,
			data : dataArr,
			fileElementId : 'upExcel',
			dataType : 'html',
			success: function (data){
				//释放按钮
				$excelBtn.removeAttr("disabled",false);
				$excelBtn.removeClass("ui-state-disabled");
				$("#errorCounters").html("");
				if(data.indexOf("actionMessage")>-1){
					$errorMessage.html(data);
				} else {
					$("#errorCountersShow").show();
					$("#hiddenTable").html(data);
					$("#errorMessage").html($("#hiddenTable #errorDiv").html());
					$("#errorCounters").html($("#hiddenTable tbody").html());
					$("#hiddenTable").html("");
				}
	        }
		});
    },
    //积分类型切换
    "changePointType":function(){
    	var that = this;
		var $this = $("#pointType");
		var $importType =$("#importType");
		$importType.val("");
		if($this.val()=='0'){//积分类型为0时，启用导入方式
			$importType.prop("disabled",false);
		}else{
			$importType.prop("disabled",true);
		}
		//清除错误信息
		that.deleteActionMsg();
	},
	//关闭按钮
    "close":function(){
    	window.close();
	},
	"openDetailPage":function(object){
		var url = $(object).attr("href");
		if (url) {
			var joinChar = "";
			if (url.lastIndexOf("?") == -1) {
				joinChar = "?";
			} else if (url.lastIndexOf("?") < url.length - 1) {
				joinChar = "&";
			}
			url += joinChar + getSerializeToken();
			document.location.href = url;
		}
	}
};
var BINOLBSCNT09 = new BINOLBSCNT09();
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
	var $pointType=$("#pointType");
	$pointType.val(0);
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({//form表单验证
		formId: "importForm",		
		rules: {
			comment: {required: true}
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLBSCNT09.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};