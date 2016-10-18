function BINOLSTJCS12() {};

BINOLSTJCS12.prototype = {
	// 电商产品对应关系查询
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
			aoColumns : [{ "sName": "no","sWidth": "2%","bSortable": false},
			             { "sName": "esProductTitleName","sWidth": "10%"},
			             { "sName": "skuCode","sWidth": "5%"},
			             { "sName": "outCode","sWidth": "5%"},
			             { "sName": "unitCode","sWidth": "5%"},
			             { "sName": "barCode","sWidth": "5%"},
			             { "sName": "nameTotal","sWidth": "10%"},
			             { "sName": "getDate","sWidth": "10%"},
			             { "sName": "isRelationChange","sWidth": "5%"}],
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
			width: 	550,
			height: 360,
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

var binolstjcs12 =  new BINOLSTJCS12();

$(document).ready(function() {
	// 电商产品对应关系查询
	binolstjcs12.search();
});

