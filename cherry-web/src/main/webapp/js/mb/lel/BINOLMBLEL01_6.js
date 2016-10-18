function BINOLMBLEL01_6() {
}
BINOLMBLEL01_6.prototype = {
	"activeboxHtml":"",	
	"getHtmlFun":function(info){
			var html = '<li class="left" style="width:20%;"><span class="list_normal" style="background-color: #FFFFFF;margin:5px 0px;width:170px;">'; 	
			html += '<span class="text" style="line-height:19px;">'+'('+info.memCode+')'+info.memName+'</span>';
			html += '<input type="hidden" name="memberIdArr" id="memberIdArr" value="'+ info.memberInfoId + '"/>';
			html += '<input type="hidden" name="memCodeArr" id="memCodeArr" value="'+ info.memCode + '"/>';
			html += '<span class="close right" style="margin: 3px 0 0 0;" onclick="BINOLMBLEL01_6.delPrmLabel(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
			html += '</span></li>';
			return html;
	},
	/**
	 * 会员信息弹出框
	 * 
	 * */
	"openMemPopup":function(){
		//清空Error信息
		$("#reCalcActionResult").empty();
		var that = this;
        // 产品弹出框属性设置
		var option = {
		   targetId: "databody",//目标区ID
           checkType : "checkbox",// 选择框类型
           mode : 2, // 模式
           brandInfoId : $("#brandInfoId").val(),// 品牌ID
           getHtmlFun:that.getHtmlFun// 目标区追加数据行function
        };
         // 调用产品弹出框共通
	popAjaxMemDialog(option);
	
},
"delPrmLabel":function(obj){
	var targetId = $(obj).parents("ul").first().prop("id");
	$(obj).parents("li").first().remove();
},
"checkedSelect":function(){
	//清空Error信息
	$("#reCalcActionResult").empty();
	var _this=$("#selectMode").val();
	if(_this==1){
		$('#addopenMem').hide();
		$('#activebox').hide();
		$('#activebox').empty();
		
	}
	if(_this==2){
		$('#addopenMem').show();
		$('#activebox').show();
		$("#activebox").html(BINOLMBLEL01_6.activeboxHtml);
	}
	
},
"doSave":function (){
	if(!$('#reCalcDialogForm').valid()) {
		return false;
	}
	//确定要进行会员重算
	var isconf = confirm($("#reCalcReconf").html());
	if (!isconf) {
		return false;
	}
	var url = $("#execReCalcUrl").attr("href");
	// 参数(品牌信息ID)
	var param = $("#reCalcDialogForm").serialize();
	// 禁用确定按钮
	$("#savebutton").attr("disabled", "disabled");
	cherryAjaxRequest({
		url: url,
		param: param,
		isDialog: true,
		resultId: "#reCalcActionResult",
		bodyId: "#reCalcDialog",
		callback: function(msg) {
			// 激活确定按钮
			$("#savebutton").removeAttr("disabled");
			$("#activebox").html(BINOLMBLEL01_6.activeboxHtml);
		}
	});
	
}
};
var BINOLMBLEL01_6 = new BINOLMBLEL01_6();
/**
* 页面初期处理
*/
$(function(){
	BINOLMBLEL01_6.activeboxHtml = $("#activebox").html();
	$('#activebox').hide();
	$('#addopenMem').hide();
	cherryValidate({						
		formId: 'reCalcDialogForm',
		rules: {
			reCalcDate: {required: true,dateValid:true}//重算开始日
		}
	});
});