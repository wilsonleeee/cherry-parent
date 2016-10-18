
function BINOLBSSAM02() {
	
};

BINOLBSSAM02.prototype = {
		"searchList":function(){
			//清除提示信息
			$('#actionResultDisplay').empty();
			var url = $("#searchUrl").attr("href");
			var params = $("#overTimeForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
					// 表格ID
					tableId : '#resultOverTimeDataTable',
					// 数据URL
					url : url,
					// 表格默认排序
					aaSorting : [ [ 2, "asc" ] ],
					// 表格列属性设置
					aoColumns : [ {
						"sName" : "number",
						"sWidth" : "2%",
						"bSortable" : false
					}, {
						"sName" : "applyDepart",
						"sWidth" : "10%",
						"bSortable" : false
					}, {
						"sName" : "workDepart",
						"sWidth" : "10%",
						"bSortable" : false
					}, {
						"sName" : "applyEmployee",
						"sWidth" : "15%",
						"bSortable" : true
					}, {
						"sName" : "auditedState",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "overtimeDate",
						"sWidth" : "15%",
						"bSortable" : true
					}, {
						"sName" : "overtimeBegin",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "overtimeEnd",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "overtimeHours",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "overtimeMemo",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "applyTime",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : true
					}, {
						"sName" : "auditedEmployee",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : false
					}, {
						"sName" : "auditedTime",
						"sWidth" : "15%",
						"bSortable" : true,
						"bVisible" : false
					}, {
						"sName" : "auditedMemo",
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

var BINOLBSSAM02 =  new BINOLBSSAM02();
//初始化
$(document).ready(function() {
	$("#fromDate").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("p").find("input[name='toDate']").val();
			return [value, "maxDate"];
		}
	});
	$("#toDate").cherryDate({
		beforeShow: function(input){
			var value = $(input).parents("p").find("input[name='fromDate']").val();
			return [value, "minDate"];
		}
	});
	cherryValidate({
		formId: "overTimeForm",		
		rules: {
			fromDate: {dateValid: true},
			toDate: {dateValid: true}
	   }		
	});
	
	$("#searchButton").click(function() {
		// 表单验证
		if (!$("#overTimeForm").valid()) {
			return false;
		}
		BINOLBSSAM02.searchList();
		return false;
	});
	
	BINOLBSSAM02.searchList();
});
