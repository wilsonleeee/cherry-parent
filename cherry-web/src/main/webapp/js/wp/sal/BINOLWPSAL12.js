var WPCOM_dialogBody ="";
var BINOLWPSAL12_GLOBAL = function() {

};

BINOLWPSAL12_GLOBAL.prototype = {
	// 查询挂单记录
	"getPayResult" : function() {
		var $form = $('#payResultForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "billCode", "sWidth" : "25%"},
		                    {"sName" : "businessDate", "sWidth" : "12%"},
		                    {"sName" : "memberCode", "sWidth" : "10%"},
		                    {"sName" : "memberName", "sWidth" : "8%"},
		                    {"sName" : "payAmount", "sWidth" : "8%"},
		                    {"sName" : "payState", "sWidth" : "8%"},
		                    {"sName" : "payMessage"}
		                    ];
		var url = $("#getPayResultUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 5,
			// 表格ID
			tableId : "#getPayResultTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			// 默认显示行数
			iDisplayLength : 5,
			fnDrawCallback : function() {
				var payState = $("#payStateDiv").text().trim();
				if(payState=="ERROR"){
					$("#btnPaySuccess").hide();
					$("#btnPayFail").attr("class","close");
					$("#btnPayFail").show();
				}else if(payState=="SUCCESS"){
					$("#btnPayFail").hide();
					$("#btnPaySuccess").attr("class","close");
					$("#btnPaySuccess").show();
				}else if(payState=="WAIT"){
					$("#btnPayFail").hide();
					$("#btnPaySuccess").hide();
					$("#btnRefresh").attr("class","close");
				}else{
					$("#btnPaySuccess").hide();
					$("#btnPayFail").attr("class","close");
					$("#btnPayFail").show();
				}
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"paySuccess" : function() {
		BINOLWPSAL03.billSubmit();
		// 关闭弹出窗口
		closeCherryDialog('payResultDialogInit',WPCOM_dialogBody);
		closeCherryDialog('dialogInit', $('#returnBill_dialog').html());
	},
	
	"payFail" : function(){
		// 关闭弹出窗口
		closeCherryDialog('payResultDialogInit',WPCOM_dialogBody);
		// 退回收款页面
		BINOLWPSAL03.webPaymentCancel();
	}
};

var BINOLWPSAL12 = new BINOLWPSAL12_GLOBAL();

$(document).ready(function(){
	// 清空弹出框内的表对象
	oTableArr[5]=null;
	// 调用挂单记录查询
	BINOLWPSAL12.getPayResult();
	// 刷新按钮获得焦点
	$("#btnRefresh").focus();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#payResultDialogInit').html();
});


