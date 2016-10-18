var BINOLMBMBM19 = function () {
	this.needUnlock = true;
};
BINOLMBMBM19.prototype = {
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
		var $selectMode_1 = $("#importType_1");
		var $selectMode_2 = $("#importType_2");
		var $importTypeVal="";
		if($selectMode_1.is(":checked")){// Excel导入
			$importTypeVal="1"
		}else if($selectMode_2.is(":checked")){
			$importTypeVal="2"
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
    	$.ajaxFileUpload({
			url : url,
			secureuri : false,
			data : {
				'csrftoken':parentTokenVal(),
				'brandInfoId' : $('#brandInfoId').val(),
				'importType' : $importTypeVal,
				'reason' : $('#reason').val(),
				'importName' : $('#importName').val()
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
						var urlDetail = '/Cherry/mb/BINOLMBMBM18_detailInit.action';
						    urlDetail = urlDetail + '?' + "profileImportId="+msgJson.memImportId + '&profileBillNo='+msgJson.billNo + '&importType='+msgJson.importType;
						var html='';
							html+=$('#exportTip1').val()+'<a  id="htmlShow" onclick="BINOLMBMBM19.openDetailPage(this);return false;">'+"【"+msgJson.billNo+"】"+'</a>'+$('#exportTip2').val();
							$("#successSpan").html(html);
							$("#htmlShow").attr("href",urlDetail); 
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
	},
	//切换导入方式
	"changeMode":function(){
		var $selectMode_1 = $("#importType_1");
		var $selectMode_2 = $("#importType_2");
		var $downLoad_A =  $("#downLoad_A");
		var $downLoad_B =  $("#downLoad_B");
		if($selectMode_2.is(":checked")){// Excel导入
			$downLoad_B.show();
			$downLoad_A.hide();
			$("#bg_yew_2").attr("class","bg_yew"); 
			$("#highlight_2").attr("class","highlight"); 
			$("#bg_yew_1").removeClass();
			$("#highlight_1").removeClass();
		}else{
			$("#bg_yew_1").attr("class","bg_yew"); 
			$("#highlight_1").attr("class","highlight"); 
			$("#bg_yew_2").removeClass();
			$("#highlight_2").removeClass();
			$downLoad_B.hide();
			$downLoad_A.show();
		}
		
	}
};
var BINOLMBMBM19 = new BINOLMBMBM19();
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
	//初始化选择更新模式
	BINOLMBMBM19.changeMode();
	cherryValidate({//form表单验证
		formId: "importForm",		
		rules: {
			reason: {required: true},
			importName:{required: true,maxlength: 30},
			importType:{required: true}
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLMBMBM19.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};