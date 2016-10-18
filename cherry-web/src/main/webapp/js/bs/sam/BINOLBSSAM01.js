
function BINOLBSSAM01() {
	
};

BINOLBSSAM01.prototype = {
		"searchList":function(){
			//清除提示信息
			$('#actionResultDisplay').empty();
			var url = $("#searchUrl").attr("href");
			var params = $("#scheduleForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
					// 表格ID
					tableId : '#resultScheduleDataTable',
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
						"sName" : "workDate",
						"sWidth" : "10%",
						"bSortable" : true
					}, {
						"sName" : "type",
						"sWidth" : "10%",
						"bSortable" : true
					}, {
						"sName" : "workBeginTime",
						"sWidth" : "15%",
						"bSortable" : true
					}, {
						"sName" : "workEndTime",
						"sWidth" : "15%",
						"bSortable" : true
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

var BINOLBSSAM01 =  new BINOLBSSAM01();
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
		formId: "scheduleForm",		
		rules: {
			fromDate: {dateValid: true},
			toDate: {dateValid: true}
	   }		
	});
	
	$("#searchButton").click(function() {
		// 表单验证
		if (!$("#scheduleForm").valid()) {
			return false;
		}
		BINOLBSSAM01.searchList();
		return false;
	});
	BINOLBSSAM01.searchList();
});
