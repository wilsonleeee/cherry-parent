var BINOLJNCOM02_GLOBAL = function () {

};
BINOLJNCOM02_GLOBAL.prototype = {
		
		/*
		 * 查询
		 */
		"search" : function (){
			var url = $("#searchUrl").attr("href");
			 // 查询参数序列化
			 var params= $("#mainForm").serialize();
			 url = url + "?" + params;
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
					              	{ "sName": "no","bSortable": false}, 								// 1
					              	{ "sName": "groupName"},											// 2
									{ "sName": "campaignType"}, 			            				// 3
									{ "sName": "validFlag"},                          					// 4
									{ "sName": "operator","sClass":"center","bSortable": false}],		// 5
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 2
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},	

		/*
		 * 查询
		 */
		"openPage" : function (obj, suffix){
			var url = $(obj).attr("href");
			var params = "";
			$("#urlParams_" + suffix).find(":input").each(function() {
				if ("" != params) {
					params += "&" + $(this).serialize();
				} else {
					params = $(this).serialize();
				}
			});
			url += "?" + params;
			openWinByUrl(url);
		}
};

function JNCOM02_search() {
	BINOLJNCOM02.search();
}
var BINOLJNCOM02 = new BINOLJNCOM02_GLOBAL();
$(document).ready(function() {
	BINOLJNCOM02.search();
});