
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
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
		formId: "add",		
		rules: {
			brandInfoId: {required: true},
			prtFunDateName: {required: true},
			prtFunType: {required: true},
		    startDate: {dateValid: true},
		    endDate: {dateValid: true}
	   }		
	});
});

function save() {
	if(!$('#add').valid()) {
		return false;
	}
	var param = $('#add').find(':input').serialize();
	var callback = function(msg) {
		if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
	};
	cherryAjaxRequest({
		url: $('#JCS41_save').attr('href'),
		param: param,
		callback: callback	
	});
}