var WPCOM_dialogBody ="";
var BINOLWPSAL05_GLOBAL = function() {

};

BINOLWPSAL05_GLOBAL.prototype = {
	
	"confirm":function(){
		var dgHangBillUrl = $("#dgHangBillUrl").attr("href");
		var params = $("#mainForm").serialize();
		var counterCode = $("#dgCounterCode").val();
		var dgSaleUrl = $("#dgSaleUrl").attr("href");
		cherryAjaxRequest({
			url: dgHangBillUrl,
			param: params,
			callback: function(data) {
				// 还原按钮样式
				$("#btnHangBills").attr("class","btn_top");
				if(data == "SUCCESS"){
					var param = "counterCode=" + counterCode;
					cherryAjaxRequest({
						url: dgSaleUrl,
						param: param,
						callback: function(data) {
							$("#webpos_main").html(data);
						}
					});
				}else{
					// 显示提示信息
					BINOLWPSAL02.showMessageDialog({
						message:"挂单失败", 
						type:"MESSAGE", 
						focusEvent:function(){
							// 最后一行第一个可见的文本框获得焦点
							BINOLWPSAL02.firstInputSelect();
						}
					});
				}
			}
		});
		// 关闭弹出窗口
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
		// 清空弹出框内容
		$('#dialogInit').html("");
		// 解除退货和补登按钮禁用
		$("#btnReturnsGoods").removeAttr("disabled");
		$("#btnReturnsGoods").attr("class","btn_top");
		$("#btnAddHistoryBill").removeAttr("disabled");
		$("#btnAddHistoryBill").attr("class","btn_top");
		// 解除清空购物车禁用
		$("#btnEmptyShoppingCart").removeAttr("disabled");
		$("#btnEmptyShoppingCart").attr("class","btn_top");
	},
	
	"cancel":function(){
		// 还原按钮样式
		$("#btnHangBills").attr("class","btn_top");
		
		closeCherryDialog('dialogInit',WPCOM_dialogBody);
//		// 获取最后一行
//		var $lastTr = $('#databody').find("tr:last");
//		// 判断最后一行数据是否为折扣数据或空行
//		if(($lastTr.find("#productVendorIDArr").val() != undefined && $lastTr.find("#productVendorIDArr").val() != "") 
//				&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
//			var showSaleRows = $("#showSaleRows").val();
//			// 判断行数是否大于允许的行数
//			if($('#databody >tr').length < Number(showSaleRows)){
//				// 添加一个空行
//				BINOLWPSAL02.addNewLine();
//			}else{
//				// 最后一个可见的文本框获得焦点
//				$('#databody >tr').find("input:text:visible:last").select();
//			}
//		}else{
//			// 最后一个可见的文本框获得焦点
//			$('#databody >tr').find("input:text:visible:last").select();
//		}
		
		// 添加一个空行
		BINOLWPSAL02.addNewLine();
		// 清空弹出框内容
		$('#dialogInit').html("");
		// 解除退货和补登按钮禁用
		$("#btnReturnsGoods").removeAttr("disabled");
		$("#btnReturnsGoods").attr("class","btn_top");
		$("#btnAddHistoryBill").removeAttr("disabled");
		$("#btnAddHistoryBill").attr("class","btn_top");
		// 解除清空购物车禁用
		$("#btnEmptyShoppingCart").removeAttr("disabled");
		$("#btnEmptyShoppingCart").attr("class","btn_top");
	}
};

var BINOLWPSAL05 = new BINOLWPSAL05_GLOBAL();

$(document).ready(function(){
	// 挂单页面确定按钮获得焦点
	$("#btnConfirm").focus();
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();
});


