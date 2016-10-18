
function BINOLMOWAT10() {};

BINOLMOWAT10.prototype = {
	// Job运行履历查询
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
			// 数据URL
			url : url,
			// 表格默认排序
			aaSorting : [[ 4, "desc" ]],
			// 表格列属性设置
			aoColumns : [{ "sName": "no","sWidth": "3%","bSortable": false},
			             /*{ "sName": "brandCode","sWidth": "10%"},*/
			             { "sName": "jobCode","sWidth": "10%"},
			             { "sName": "result","sWidth": "5%"},
			             { "sName": "comments","sWidth": "30%","bSortable": false},
			             { "sName": "runStartTime","sWidth": "20%"},
			             { "sName": "runEndTime","sWidth": "20%"}],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"showDetail":function(obj){
		var $this = $(obj);
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 	430,
			height: 230,
			title: 	$("#showDetailTitle").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){
				removeDialog("#dialogInit");
			}
		};
		openDialog(dialogSetting);
		$("#dialogContent").html($this.attr("rel"));
		$("#dialogInit").html($("#dialogDetail").html());
	}
	
};

var binolmowat10 =  new BINOLMOWAT10();

$(document).ready(function() {
	// Job运行履历查询
	binolmowat10.search();
});

