
function BINOLBSCNT07() {};

BINOLBSCNT07.prototype = {
	// 积分计划柜台查询
	"search" : function(){
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 排序列
			aaSorting : [[2, "desc"]],
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [

				{ "sName": "checkbox","bSortable": false,"sClass":"center"},
				{ "sName": "no","sWidth": "1%","bSortable": false,"sClass":"center"},
				{ "sName": "CounterCode","sClass":"center"},
				{ "sName": "counterName","sClass":"center"},
				{ "sName": "pointPlan","bSortable": false,"sClass":"center"},
				{ "sName": "explain","bSortable": false},
				{ "sName": "StartDate","sClass":"center"},
				{ "sName": "EndDate","sClass":"center"},
				{ "sName": "CurrentPointLimit","sClass":"center"},
				{ "sName": "employeeName","sClass":"center"},
				{ "sName": "Comment"}
			],
			// 不可设置显示或隐藏的列
			aiExcludeArr :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3
		};

		// 调用获取表格函数
		getTable(tableSetting);
	},

	/*
	 * 导出Excel
	 */
	"exportExcel" : function(){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
			if (!$('#mainForm').valid()) {
				return false;
			};
			var url = $("#downUrl").attr("href");
			var params= $("#mainForm").serialize();
			params = params + "&csrftoken=" +$("#csrftoken").val();
			url = url + "?" + params;
			window.open(url,"_self");
		}
	}

};

var binolbscnt07 =  new BINOLBSCNT07();



$(document).ready(function() {

	binolbscnt07.search();

});

