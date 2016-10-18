function BINOLBSWEM04() {};

BINOLBSWEM04.prototype = {	
	/**
	 * 清理执行结果信息
	 */
	"clearActionHtml" : function() {
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
	},
		
	"search" : function(){
		BINOLBSWEM04.clearActionHtml();
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
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
			aaSorting : [[ 2, "asc" ]],
			// 表格列属性设置
			aoColumns : [
		             	{ "sName": "no","bSortable": false,"sClass":"center","sWidth": "4%"},
			            { "sName": "agentMobile"},
			            { "sName": "agentName"},
			            { "sName": "departName"},
			            { "sName": "agentLevel","sWidth": "10%"},
			            { "sName": "superMobile"},
			            { "sName": "provinceId","sWidth": "10%"},
			            { "sName": "cityId","sWidth": "10%"},
						{ "sName": "validFlag","sClass":"center","sWidth": "3%"},
					],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
	}
};

var BINOLBSWEM04 =  new BINOLBSWEM04();

$(document).ready(function() {
	BINOLBSWEM04.search();
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	//柜台主管选择
	binolbscnt01.counterBASBinding({
		elementId:"counterBAS",
		testUrl:"/basis/BINOLBSCNT01_getCounterBAS.action"+"?",
		showNum:20
	});
});
