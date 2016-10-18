var BINOLMBPTM04 = function () {
};
BINOLMBPTM04.prototype = {
		"search" : function() {
			var url = $("#search_Url").attr("href");
			var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			// 显示结果一览
			$("#pointInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#pointDataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "3%", "bSortable": false},
					                { "sName": "pointImportCode", "sWidth": "15%" },
					                { "sName": "pointBillName", "sWidth": "15%" },
									{ "sName": "pointType", "sWidth": "5%" },
									{ "sName": "importType", "sWidth": "10%" },
									{ "sName": "businessTime", "sWidth": "15%" },
									{ "sName": "employeeName", "sWidth": "15%" },
									{ "sName": "reason", "sWidth": "30%", "bSortable": false}],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback: function() {
						$("a.description").cluetip({
							splitTitle: '|',
						    width: '300',
						    height: 'auto',
						    cluetipClass: 'default',
						    cursor:'pointer',
						    arrows: false, 
						    dropShadow: false
						});
					}
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    }
};
var BINOLMBPTM04 = new BINOLMBPTM04();
$(document).ready(function() {
	BINOLMBPTM04.search();
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startTime: {dateValid:true},	// 开始日期
			endTime: {dateValid:true}	// 结束日期
		}		
	});
});
//（父页面刷新）
function search () {
	BINOLMBPTM04.search();
}