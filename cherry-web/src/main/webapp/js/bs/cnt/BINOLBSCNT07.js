
function BINOLBSCNT07() {};

BINOLBSCNT07.prototype = {
	// 柜台查询
	"search" : function(){
		var maintainCoutSynergy=$("#maintainCoutSynergy").val();
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
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [

				],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
		};

		// 调用获取表格函数
		getTable(tableSetting);
	}

};

var binolbscnt07 =  new BINOLBSCNT07();



$(document).ready(function() {

	binolbscnt07.search();

});

