
function BINOLBSSAM03() {
	
};

BINOLBSSAM03.prototype = {
		"searchList":function(){
			//清除提示信息
			$('#actionResultDisplay').empty();
			var url = $("#searchUrl").attr("href");
			var params = $("#payrollForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
					// 表格ID
					tableId : '#resultPayrollDataTable',
					// 数据URL
					url : url,
					// 表格默认排序
					aaSorting : [ [ 1, "asc" ] ],
					// 表格列属性设置
					aoColumns : [ {
						"sName" : "number",
						"sWidth" : "2%",
						"bSortable" : false
					}, {
						"sName" : "departName",
						"sWidth" : "10%",
						"bSortable" : true
					}, {
						"sName" : "employeeName",
						"sWidth" : "10%",
						"bSortable" : true
					}, {
						"sName" : "workingHours",
						"sWidth" : "15%",
						"bSortable" : true
					}, {
						"sName" : "workingDays",
						"sWidth" : "15%",
						"bSortable" : true
					}, {
						"sName" : "usualOvertime",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "specialOvertime",
						"sWidth" : "15%",
						"bSortable" : true
					}, {
						"sName" : "saleTarget",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "saleAmount",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "bonusRate",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "completionRate",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "score",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "wagesAmount",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "memo",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
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

var BINOLBSSAM03 =  new BINOLBSSAM03();
//初始化
$(document).ready(function() {
	$("#exportExcel").click(function(){
		if($("#resultPayrollDataTable").find(".dataTables_empty:visible").length==0) {
			var callback = function(msg) {
        		var url = $("#exportExcelUrl").attr("href");
        		$("#payrollForm").attr("action",url);
        		$("#payrollForm").submit();
        	};
        	exportExcelReport({
        		url: $("#exportCheckUrl").attr("href"),
        		param: $("#payrollForm").serialize(),
        		callback: callback
        	});
        }
		return false;
	});
	
	$("#exportCsv").click(function(){
		if($("#resultPayrollDataTable").find(".dataTables_empty:visible").length==0) {
			exportReport({
				exportUrl:$("#exportCsvUrl").attr("href"),
				exportParam:$("#payrollForm").serialize()
			});
		}
		return false;
	});
	BINOLBSSAM03.searchList();
});
