
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	// 表单验证初期化
	cherryValidate({
		formId: 'update',
		rules: {
		    code:  {required: true,alphanumeric: true,maxlength: 15},
		    nameEn: {maxlength: 50},
		    nameCn: {required: true,maxlength: 50},
		    phoneNumber: {telValid:true,maxlength: 20},
		    postalCode:{zipValid:true,maxlength: 10},
		    provinceId:{required: true},
		    cityId : {required: true},
		    contactPerson:{maxlength: 200},
		    contactAddress:{maxlength: 200},
		    deliverAddress:{maxlength: 200}
		}
	});
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	// 县级市选择
	$("#county").click(function(){
		// 显示县级市列表DIV
		bscom03_showRegin(this,"countyTemp");
	});
	
});	


function savepartner() {
	
	if(!$('#update').valid()) {
		return false;
	}
	var param = $('#update').find(':input').serialize();
	var callback = function(msg) {
		    // 刷新父页面
			window.opener.BINOLBSPAT01.search();
	
	};
	cherryAjaxRequest({
		url: $('#editUrl').attr('href'),
		param: param,
		callback: callback
	});
	
}
