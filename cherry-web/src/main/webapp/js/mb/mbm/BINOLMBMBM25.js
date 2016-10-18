var BINOLMBMBM25 = function () {
	this.needUnlock = true;
};
BINOLMBMBM25.prototype = {
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
				'reason' : $('#reason').val(),
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
						var urlDetail = '/Cherry/mb/BINOLMBMBM24_detailInit.action';
					    urlDetail = urlDetail + '?' + "keyAttrImportId="+msgJson.keyAttrImportId + '&serialNo='+msgJson.billNo;
					    var html='';
						html+=$('#exportTip1').val()+'<a  id="htmlShow" onclick="BINOLMBMBM25.openDetailPage(this);return false;">'+"【"+msgJson.billNo+"】"+'</a>'+$('#exportTip2').val();
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
	}
};
var BINOLMBMBM25 = new BINOLMBMBM25();
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
			pointType: {required: true},
			reason: {required: true},
			importType:{required: true},
			importName:{required: true,maxlength: 30}
		}		
	});
});
window.onbeforeunload = function(){
	if (BINOLMBMBM25.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};