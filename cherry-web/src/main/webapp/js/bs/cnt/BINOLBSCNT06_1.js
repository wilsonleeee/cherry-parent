
var BINOLBSCNT06_1 = function(){};

BINOLBSCNT06_1.prototype = {
	"needUnlock":true,
	
	"clearError":function(obj){
		var _this = obj;
		_this.style.color = "#000";
	},
	
	"close":function(){
		window.close();
	},
	
	"validData":function(){
		
		return $("#highlight").length > 0||$("#showBasError").length > 0? false:true;
		
	},
	
	"saveEdit":function(url){
		
		//验证数据
		if(!$('#mainForm').valid()) {
			return false;
		}
		if(!this.validData()){
			return false;
		}
		var counterCode = $("#editCounterCode").val();
		var url = url;
		var callback = function(msg){
			if(msg.indexOf("actionResultBody") > -1){
				window.opener.BINOLBSCNT06.delErrorCounter(counterCode);
			}
			$("div").removeClass("container");
		};
		cherryAjaxRequest({
			url: url,
			param: $('#mainForm').serialize(),
			callback: callback
		});
		
		
	}
};

var binOLBSCNT06_1 = new BINOLBSCNT06_1();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
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
	
	
	
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			brandInfoId: {required: true},
			counterCode: {required: true,alphanumeric:true,maxlength: 15},
			counterName: {required: true,maxlength: 50},
			counterKind: {required: true},
			counterAddress: {maxlength: 50},
			counterSpace: {floatValid: [4,2]},
			employeeNum:{validIntNum:true},
			foreignName: {maxlength: 50},
			counterCategory: {maxlength: 100},
			resellerCode: {maxlength: 5},
			resellerName: {maxlength: 30},
			mallName: {maxlength: 30},
			address: {maxlength: 50},
			counterTelephone : {maxlength: 20,numberValid:true},
			longitude : {maxlength: 32},
			latitude : {maxlength: 32},
			equipmentCode : {maxlength: 50}
		}
	});
	
} );
window.onbeforeunload = function(){
	if (binOLBSCNT06_1.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};