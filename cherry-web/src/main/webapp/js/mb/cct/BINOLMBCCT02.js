var BINOLMBCCT02_GLOBAL = function () {
};

BINOLMBCCT02_GLOBAL.prototype = {
	"search" : function() {
		var $form = $('#getIssueForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "rowNumber", "bVisible" : false, "sWidth": "3%", "bSortable": false},
		                    {"sName" : "issueNo", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "callTime", "sWidth" : "8%"},
		                    {"sName" : "phoneNum", "sWidth" : "7%"},
		                    {"sName" : "issueSummary", "sWidth" : "15%"},
		                    {"sName" : "resolution", "sWidth" : "5%"},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		var url = $("#searchIssueListUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		var indexNum = $('#pageIndex').val();
		indexNum ++;
		$('#pageIndex').val(indexNum);
		$("#issueDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#issueListTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 设置页面索引
			index : indexNum,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 默认显示行数
			iDisplayLength : 10,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"editCustomer" : function() {
		var $form = $('#getIssueForm');
		var url = $('#addCustomerUrl').attr("href");
		var params = $form.serialize();
		cherryAjaxRequest({
			url:url,
			param:params,
			callback: function(data) {
				$("#getIssueDiv").remove();
				$("#rightPageDiv").html($("#addCustomerInfoBox").html());
				$("#addCustomerDiv").html(data);
			}
		});
	},
	
	"showDetail" : function(issueId) {
		var url = $('#getIssueInfoUrl').attr("href");
		var params = "issueId="+issueId;
		cherryAjaxRequest({
			url:url,
			param:params,
			callback: function(data) {
				$("#addIssueDiv").html(data);
			}
		});
	}
}

var BINOLMBCCT02 = new BINOLMBCCT02_GLOBAL();

$(document).ready(function() {
	
});
