
function BINOLMBPTM06() {
	
};

BINOLMBPTM06.prototype = {	
	"searchReportSummyInfo" : function() {
		if(!$('#reportInfoCherryForm').valid()) {
			return false;
		}
		var url = $("#reportSummyInfoUrl").attr("href");
		var params= $("#reportInfoCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		/*// 显示结果一览
		$("#reportSummyInfo").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#reportSummyDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  
				                { "sName": "memberTotal", "sWidth": "15%" },
								{ "sName": "newMemberTotal", "sWidth": "15%" },
								{ "sName": "newMemberQRPointsTotal", "sWidth": "15%" },
								{ "sName": "oldMemberTotal", "sWidth": "15%" },
								{ "sName": "oldMemberQRPointsTotal", "sWidth": "15%" },
								{ "sName": "entrySkinTestMemberTotal", "sWidth": "15%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "60%",
				index : 1
		};
		// 调用获取表格函数
		getTable(tableSetting);
		*/
		function callback(html){
			$("#reportSummyInfo").html(html);
		};
		cherryAjaxRequest({
			url: url,
			callback: callback 
		});
    },
    	
	"searchReportDetailInfo" : function() {
		
		var url = $("#reportDetailInfoListUrl").attr("href");
		var params= $("#reportInfoCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#reportDetailInfo").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#reportDetailDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 7, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "number", "bSortable": false },
				                { "sName": "memberCode" },
								{ "sName": "memberName" },
								{ "sName": "mobile" },
								{ "sName": "grantCardTime"},
								{ "sName": "grantPoints" },
								{ "sName": "grantPointTime" },
								{ "sName": "newOldMemberFlag" },
								{ "sName": "region", "bVisible": false},
								{ "sName": "province", "bVisible": false },
								{ "sName": "city", "bVisible": false },
								{ "sName": "counterCode", "bVisible": false },
								{ "sName": "counterName", "bVisible": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index : 2
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    
    /**
    * 一览excel导出
    */
    "exportExcel" : function() {
    	if($(".dataTables_empty:visible").length==0) {
    		if(!$('#reportInfoCherryForm').valid()) {
    			return false;
    		}
    		var params = $("#reportInfoCherryForm").serialize();
    		var callback = function(msg) {
    			var url = $("#exportUrl").attr("href");
    			url = url + "?" + params;
    		    window.open(url,"_self");
    		}
    	    exportExcelReport({
        		url: $("#exporChecktUrl").attr("href"),
        		param: params,
        		callback: callback
        	});
    	}
    }
};

var binolmbptm06 =  new BINOLMBPTM06();

$(function(){
	// 报表信息查询
	binolmbptm06.searchReportSummyInfo();
	binolmbptm06.searchReportDetailInfo();
	
	$('#startDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'minDate'];
		}
	});
	
	// 表单验证初期化
	cherryValidate({
		formId: 'reportInfoCherryForm',
		rules: {
			startDate: {required: true,dateValid: true},
			endDate: {dateValid: true}
		}
	});
});