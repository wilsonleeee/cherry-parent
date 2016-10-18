var BINOLMBCCT10_GLOBAL = function () {
};

BINOLMBCCT10_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#getIssueForm');
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "callId", "bVisible" : false, "sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "customerCode", "sWidth" : "7%"},
		                    {"sName" : "customerName", "sWidth" : "8%"},
		                    {"sName" : "customerNumber", "sWidth" : "10%"},
		                    {"sName" : "issueSummary", "sWidth" : "15%", "bSortable" : false},
		                    {"sName" : "issueType", "sWidth" : "5%"},
		                    {"sName" : "issueSubType", "sWidth" : "5%"},
		                    {"sName" : "resolution", "sWidth" : "5%"},
		                    {"sName" : "callTime", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "cNo", "sWidth" : "5%"},
		                    {"sName" : "createTime", "sWidth" : "5%"},
		                    {"sName" : "resolutionDate", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		
		var url = $("#issueSearchUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#issueDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#issueInfoTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 9, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 2 , 3 , 4 , 9 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 设置页面索引
			index : 153,
			// 固定列数
			fixedColumns : 0,
			fnDrawCallback : function() {
				$('#issueInfoTable').find("a.description").cluetip({
					splitTitle: '|',
				    width: 300,
				    height: 'auto',
				    cluetipClass: 'default', 
				    cursor: 'pointer',
				    showTitle: false
				});
			},
			callbackFun : function(msg) {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"exportExcel" : function(url,exportFormat){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#getIssueForm").serialize();
            params = params + "&" + getSerializeToken() + "&exportFormat=" + exportFormat;
            if(exportFormat == "0"){
	            url = url + "?" + params;
	            document.location.href = url;
            }else{
	            exportReport({
					exportUrl: url,
					exportParam: params
				});
            }
		}
	},
	
	// 查看问题记录
	"showDetail" :function(issueId){
		var dialogSetting = {
			dialogInit: "#issueDetailDialog",
			width: 1000,
			height: 650,
			title: $("#issueDetailDialogTitle").text()
		};
		openDialog(dialogSetting);
		
		var setUrl = $("#addIssueUrl").attr("href");
		var param = "issueId=" + issueId;
		var callback = function(msg) {
			$("#issueDetailDialog").html(msg);
		};
		cherryAjaxRequest({
			url: setUrl,
			param: param,
			callback: callback
		});
	}
	
}

var BINOLMBCCT10 = new BINOLMBCCT10_GLOBAL();

$(document).ready(function() {
	$("#createTimeStart").cherryDate({
		beforeShow: function(input){
			var value = $("#createTimeEnd").val();
			return [value, "maxDate"];
		}
	});
	$("#createTimeEnd").cherryDate({
		beforeShow: function(input){
			var value = $("#createTimeStart").val();
			return [value, "minDate"];
		}
	});
	cherryValidate({
		//form表单验证
		formId: "getIssueForm",
		rules: {
			createTimeStart:{dateValid: true},
			createTimeEnd:{dateValid: true}
		}		
	});
	// 问题来源默认为呼叫中心
	$("#issueSource").val("1");	
});
