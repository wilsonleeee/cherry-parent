var BINOLCTCOM06_GLOBAL = function () {
};

BINOLCTCOM06_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#customerForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "memCode", "sWidth": "10%"},
		                    {"sName" : "memName", "sWidth": "15%"},
		                    {"sName" : "gender", "bSortable" : false, "bVisible" : false, "sWidth" : "3%"},
		                    {"sName" : "mobilePhone", "sWidth" : "10%"},
		                    {"sName" : "email", "sWidth" : "15%"},
		                    {"sName" : "telephone", "sWidth" : "10%", "bVisible" : false},
		                    {"sName" : "totalPoint", "sWidth" : "5%"},
		                    {"sName" : "changablePoint", "sWidth" : "5%", "bVisible" : false},
		                    {"sName" : "counterCode", "sWidth" : "10%", "bVisible" : false},
		                    {"sName" : "counterName", "sWidth" : "10%"},
		                    {"sName" : "receiveMsgFlg", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		var url = $("#showCustomerUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#customerDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#customerDataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 1, "asc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 , 3 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			// 默认显示行数
			iDisplayLength : 10,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
}

var BINOLCTCOM06 = new BINOLCTCOM06_GLOBAL();

$(document).ready(function() {
	// 清空弹出框内的表对象
	oTableArr[0]=null;
	// 加载页面时
	BINOLCTCOM06.search();
});
