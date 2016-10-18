
function BINOLPTRPS29_global() {
	
};

BINOLPTRPS29_global.prototype = {
	"search" : function() {
		$("#actionResultDisplay").empty();
		if(!$("#mainForm").valid()){
			return;
		}
		var url = $("#searchUrl").attr("href");
		var params= $("#mainForm").serialize();
		url = url + "?" + params;
		// 表格设置
		tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 排序列
			 aaSorting : [[3, "desc"]],
			 // 表格列属性设置
			 aoColumns : [	
	                    	{ "sName": "no", "sWidth": "5%","bSortable": false},			// 1
	                    	{ "sName": "batchCode", "sWidth": "10%","bVisible": false},// 2
	                    	{ "sName": "resellerCode", "sWidth": "10%","bVisible": false},// 3
	                    	{ "sName": "resellerName", "sWidth": "10%","bVisible": false},// 4
	                    	{ "sName": "counterCode", "sWidth": "15%"},// 5
	                    	{ "sName": "counterName", "sWidth": "15%"},// 6
	                    	{ "sName": "startTime", "sWidth": "15%"},// 7
	                    	{ "sName": "endTime", "sWidth": "10%"},// 8
	                    	{ "sName": "totalQuantity", "sWidth": "15%","sClass":"center"},//9
	                    	{ "sName": "totalAmount", "sWidth": "5%","sClass":"center"}, // 10
	                    	{ "sName": "totalBillCount", "sWidth": "5%","sClass":"center"}// 11
						],
			 sScrollX : "100%",
			// 不可设置显示或隐藏的列	
			aiExclude :[0,4,5]
		};
		$("#section").show();
		// 调用获取表格函数
		getTable(tableSetting);
    },
	"exportExcel" : function(url,exportFormat){
		$("#actionResultDisplay").empty();
		if(!$("#mainForm").valid()){
			return;
		}
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#mainForm").serialize();
            params = params + "&exportFormat=" +exportFormat;
            if(exportFormat == "0" || exportFormat == "2"){
                var checkUrl = getBaseUrl() + "/pt/BINOLPTRPS29_checkExport";
                var callback = function(msg){
                	if(msg.indexOf("actionError") == -1){
			            url = url + "?" +params;
			            document.location.href = url;
                	}
                };
                cherryAjaxRequest({
                	url: checkUrl,
                	param: params,
                	callback: callback
                });
            }else {
            	exportReport({
					exportUrl: url,
					exportParam: params
				});
            }
		}
	}
    
};

var BINOLPTRPS29 =  new BINOLPTRPS29_global();

$(document).ready(function(){
	var counterOption = {
		elementId: "counterName",
		showNum: "20",
		selected: "name"
	}
	counterBinding(counterOption);
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			batchCode:{required:true,maxlength:25,alphanumeric:true},
		}		
	});
});
