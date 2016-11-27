function BINOLBSCNT10() {};

BINOLBSCNT10.prototype = {
	"searchCounterDetail" : function() {
		var url = $("#searchPointCounterDetail").attr('href');
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable_pointPlanCounterDetail',
			// 排序列
			aaSorting : [[0, "desc"]],
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [
				{ "sName": "UpdateTime","sClass":"center"},
				{ "sName": "ModifyType","bSortable": false,"sClass":"center"},
				{ "sName": "UpdatedBy","sClass":"center"},
				{ "sName": "StartDate","sClass":"center"},
				{ "sName": "EndDate","sClass":"center"},
				{ "sName": "Comment"}
			],
			// 不可设置显示或隐藏的列
			aiExcludeArr :[0, 1, 2, 3, 4, 5],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 6
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"changeTab" : function(thisObj) {

		$(thisObj).siblings().removeClass('ui-tabs-selected');
		$(thisObj).addClass('ui-tabs-selected');

	},
	"exportExcel" : function() {
		if($(".dataTables_empty:visible").length==0) {
			//alert(oTableArr[0].fnSettings().sAjaxSource)
			var url = $("#exportUrl").attr("href");
			url += "?" + getSerializeToken();
			url += "&" + $("#pointDetailForm").serialize();
			document.location.href = url;
		}
	},
	"changeClub" : function() {
		binolbscnt10.searchMemPoint();
		binolbscnt10.searchPointDetail();
	},
	"searchMemPoint" : function() {

	}
};

var binolbscnt10 =  new BINOLBSCNT10();

$(function() {

	binolbscnt10.searchCounterDetail();
});

