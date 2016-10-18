var CTCOM_dialogBody ="";
var BINOLCTCOM08_GLOBAL = function () {
};

BINOLCTCOM08_GLOBAL.prototype = {
	
	"search" : function() {
		var $form = $('#getTemplateForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "templateCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "templateName", "sWidth" : "10%"},
		                    {"sName" : "templateUse", "sWidth" : "5%"},
		                    {"sName" : "contents", "bSortable" : false},
		                    {"sName" : "customerType", "sWidth" : "5%"},
		                    {"sName" : "status", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false} 
		                    ];
		var url = $("#searchTplUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#templatedetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#templateDataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 2 , 4, 7 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			// 默认显示行数
			iDisplayLength : 10,
			fnDrawCallback:function() {
				$('#templateDataTable').find("a.description").cluetip({
					splitTitle: '|',
				    width: 500,
				    cluezIndex: 20000,
				    height: 'auto',
				    cluetipClass: 'default', 
				    cursor: 'pointer',
				    showTitle: false
				});
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},

	"chooseMsgTemplate" : function(obj){
		var contentsValue = $(obj).find("#contents").val();
		if(contentsValue != undefined && null != contentsValue){
			$("#cluetip-waitimage").remove();
			$("#cluetip").remove();
			$("#msgContents").val(contentsValue);
			$("#contentsSpan").removeClass('error');
			$("#msgContents").removeClass('error');
			$("#messageContents").find("#errorText").remove();
			BINOLCTCOM07.changeText(contentsValue);
		}
		// 关闭弹出窗口
		closeCherryDialog('templateDialogInit',CTCOM_dialogBody);
	}
	
}

var BINOLCTCOM08 = new BINOLCTCOM08_GLOBAL();

function search() {
	BINOLCTCOM08.search();
}

$(document).ready(function() {
	// 清空弹出框内的表对象
	oTableArr[0]=null;
	
	// 加载页面时查询模板列表
	BINOLCTCOM08.search();
	
	// 给弹出框窗体全局变量赋值
	CTCOM_dialogBody= $('#templateDialogInit').html();
});