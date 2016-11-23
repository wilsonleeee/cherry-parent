
function BINOLMBSVC02_04() {
	
};

BINOLMBSVC02_04.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
			
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		"searchList":function(){
			var url = $("#serviceSearchUrl").attr("href");
			var params = $("#serviceForm").serialize()+ "&" + getSerializeToken();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#serviceList").show();
			// 表格设置
			var tableSetting = {
				// 表格ID
				tableId : '#resultServiceDataTable',
				// 数据URL
				url : url,
				// 表格默认排序
				aaSorting : [ [ 1, "asc" ] ],
				// 表格列属性设置
				aoColumns : [ {
					"sName" : "number",
					"sWidth" : "5%",
					"bSortable" : false
				}, {
					"sName" : "cardCode",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "cardType",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "departName",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "transactionTime",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "billCode",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "relevantCode",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "transactionType",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "amount",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "giftAmount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "totalAmount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "serviceType",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : true
				} ,{
					"sName" : "discount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "serviceQuantity",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : true
				} ,{
					"sName" : "quantity",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : true
				} ,{
					"sName" : "totalQuantity",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : true
				}],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {
				}
			};
//			 调用获取表格函数
			getTable(tableSetting);
		}
		
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLMBSVC02_04.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLMBSVC02_04.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};



var BINOLMBSVC02_04 =  new BINOLMBSVC02_04();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	
	BINOLMBSVC02_04.searchList();
	BINOLMBSVC02_04.needUnlock = true;
});
