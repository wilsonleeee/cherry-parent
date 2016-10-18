var BINOLBSEMP06_1 = function(){
	
};

BINOLBSEMP06_1.prototype = {
		
		"clearError":function(obj){
			var _this = obj;
			_this.style.color = "#000";
		},
		
		/**
		 * 保存编辑
		 */
		"saveEdit":function(url){
			//验证数据
			if(!$('#editForm').valid()) {
				return false;
			}
			// 用于编辑保存成功后删除编辑的BA错误信息
			var baCode = $("#editBaCode").val();
			var callback = function(msg) {
				if(msg.indexOf("actionResultBody") > -1) {
					window.opener.BINOLBSEMP06.delErrorBA(baCode);
				}
				$("div").removeClass("container");
			};
			cherryAjaxRequest({
				url: url,
				param: $('#editForm').serialize(),
				callback: callback
			});
		},
		
		"popCounter" : function(obj,param){
			var $this = $(obj);
			//取得所有部门类型为柜台的部门
		 	var departType = "&departType=4";
		 	param = param + "&privilegeFlg=1&businessType=1&brandInfoId=" + $("#brandInfoId").val()+ departType;
			var that = this;
			var callback = function(){
				var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
				var departId = "";
				var departName = "";
				var departCode = "";
				var showDepart = "";
				if(checkedRadio){
					departId = checkedRadio.val();
		    		departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
		    		departCode = checkedRadio.parent().parent().children("td").eq(1).find("span").text().escapeHTML();
		    		if(departName != '' && departCode != ''){
		    			showDepart = "("+departCode+")"+departName;
		    		}
				}
				$("#organizationID").val(departId);
				$("#counterCode").val(departCode);
				$("#counterName").val(departName);
				$("#showCounterName").html(showDepart.unEscapeHTML());
			};
			var option={
					checkType:"radio",
					brandInfoId:$("#brandInfoId option:selected").val(),
					param:param,
					click:callback
			};
			popAjaxDepDialog(option);
		}
		
};

var BINOLBSEMP06_1 = new BINOLBSEMP06_1();

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};

$(document).ready(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	// 表单验证初期化
	cherryValidate({
		formId : 'editForm',
		rules : {
			brandInfoId: {required: true},
			baCode: {required: true, employCodeValid:true,maxlength: 15},
			baName: {required: true,maxlength: 30},
			commtDate: {dateValid: true},
			depDate: {dateValid: true}
		}
	});
});