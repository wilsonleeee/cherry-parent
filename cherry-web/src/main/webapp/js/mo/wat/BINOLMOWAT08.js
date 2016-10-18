
function BINOLMOWAT08_global() {};

BINOLMOWAT08_global.prototype = {
	// MQ消息错误日志查询
	"search" : function(subType){
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize() + "&csrftoken=" + getTokenVal();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		var aoColumns = [{ "sName": "id","sWidth": "3%","bSortable": false},
			             { "sName": "subType","sWidth": "5%"},
			             { "sName": "errorCode","sWidth": "5%"},
			             { "sName": "time","sWidth": "10%"},
			             { "sName": "content","sWidth": "50%","bSortable": false},
			             { "sName": "messageBody","sWidth": "15%","bSortable": false}
			             ];
		if(!isEmpty(subType)){
			aoColumns = [{ "sName": "id","sWidth": "3%","bSortable": false},
			             { "sName": "subType","sWidth": "5%"},
			             { "sName": "errorCode","sWidth": "5%"},
			             { "sName": "time","sWidth": "10%"},
			             { "sName": "content","sWidth": "50%","bSortable": false},
			             { "sName": "messageBody","sWidth": "15%","bSortable": false,"bVisible": false}
			             ];
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
			aaSorting : [[ 3, "desc" ]],
			// 表格列属性设置
			aoColumns : aoColumns,
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
			height: 300,
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
var BINOLMOWAT08 =  new BINOLMOWAT08_global();


