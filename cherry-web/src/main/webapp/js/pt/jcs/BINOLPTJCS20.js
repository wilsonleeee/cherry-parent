var binolptjcs20_global = {};
binolptjcs20_global.needUnlock = true;
var binolptjcs20_doRefresh = false;

window.onbeforeunload = function() {
	if (window.opener) {
		window.opener.unlockParentWindow();
		if(binolptjcs20_doRefresh){
			var url = $('#search_url', window.opener.document).val();
			window.opener.search(url);
			}
		}
};

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	$('#detailId .endTime').cherryDate({
		// 结束时间大于起始时间
		beforeShow: function(input){										
			var value = $(input).parents("table").find(".startTime").val();	
			return [value,'minDate'];									
		}
	});
	
	$('#detailId .startTime').cherryDate({
		// 开始时间小于起始时间
		beforeShow: function(input){										
			var value = $(input).parents("table").find(".endTime").val();	
			return [value,'maxDate'];									
		}
	});
	cherryValidate({			
		formId: "update",		
		rules: {
			brandInfoId: {required: true},
			solutionName: {required: true,maxlength: 20}, 
			solutionCode: {required: true,maxlength: 10},
		    startDate: {dateValid: true},
		    endDate: {dateValid: true}
	   }		
	});
});

function doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolptjcs20_global.needUnlock = false;
	$("#toDetailForm").submit();
}
	
function save() {
	if(!$('#update').valid()) {
		return false;
	}
	var param = $('#update').find(':input').serialize();
	var callback = function(msg) {
		if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
	};
	cherryAjaxRequest({
		url: $('#JCS20_save').attr('href'),
		param: param,
		callback:  function(msg) {binolptjcs20_doRefresh = true;}
	});
}