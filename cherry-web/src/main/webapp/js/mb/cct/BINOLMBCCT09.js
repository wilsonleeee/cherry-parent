var BINOLMBCCT09_GLOBAL = function () {
};

BINOLMBCCT09_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#getCallLogForm');
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "callId", "bVisible" : false, "sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "cNo", "sWidth" : "5%"},
		                    {"sName" : "customerNumber", "sWidth" : "10%"},
		                    {"sName" : "customerCode", "sWidth" : "7%"},
		                    {"sName" : "customerName", "sWidth" : "8%"},
		                    {"sName" : "isMember", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "callType", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "callTime", "sWidth" : "10%"},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		
		var url = $("#callLogSearchUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#callLogDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#callLogDataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 7, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 1, 2 , 3 , 7 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 设置页面索引
			index : 152,
			// 固定列数
			fixedColumns : 0,
			callbackFun : function(msg) {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	// 查看问题记录
	"showIssue" :function(issueId){
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
	},
	
	"exportExcel" : function(url,exportFormat){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#getCallLogForm").serialize();
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
	}
	
}

var BINOLMBCCT09 = new BINOLMBCCT09_GLOBAL();

$(document).ready(function() {
	$("#callTimeStart").cherryDate({
		beforeShow: function(input){
			var value = $("#callTimeEnd").val();
			return [value, "maxDate"];
		}
	});
	$("#callTimeEnd").cherryDate({
		beforeShow: function(input){
			var value = $("#callTimeStart").val();
			return [value, "minDate"];
		}
	});
	cherryValidate({
		//form表单验证
		formId: "getCallLogForm",		
		rules: {
			callTimeStart:{dateValid: true},
			callTimeEnd:{dateValid: true}
		}		
	});
});
