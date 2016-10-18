
function BINOLMBSVC05() {
	
};

BINOLMBSVC05.prototype = {
		"searchList":function(){
			//清除提示信息
			$('#actionResultDisplay').empty();
			var url = $("#searchTradeUrl").attr("href");
			var params = $("#cardTradeForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
				// datatable 对象索引
				index : 1,
				// 表格ID
				tableId : '#resultCardDataTable',
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
					"sName" : "billCode",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "transactionTime",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "transactionType",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "totalAmount",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "cardCode",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "mobilePhone",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "memo",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {

				},
				callbackFun : function(msg) {
					var $msg = $("<div></div>").html(msg);
					var $headInfo = $("#headInfo", $msg);
					if ($headInfo.length > 0) {
						$("#headInfo").html($headInfo.html());
					} else {
						$("#headInfo").empty();
					}
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		}
};

var BINOLMBSVC05 =  new BINOLMBSVC05();

$(document).ready(function() {
	$("#searchButton").click(function() {
		BINOLMBSVC05.searchList();
		return false;
	});
});
