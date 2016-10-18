
function BINOLPTRPS36_global() {
	
};

BINOLPTRPS36_global.prototype = {
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
	                    	{ "sName": "no","bSortable": false},			// 0
	                    	{ "sName": "counterCode"},						// 1
	                    	{ "sName": "counterName"},						// 2
	                    	{ "sName": "monthSaleAmount"},					// 3
	                    	{ "sName": "employeeNum"},						// 4
	                    	{ "sName": "numberOfDay"},						// 5
	                    	{ "sName": "monthPingEffect"}					// 6
						],
			 sScrollX : "100%",
			// 不可设置显示或隐藏的列	
			aiExclude :[0,1,2,3]
		};
		$("#section").show();
		// 调用获取表格函数
		getTable(tableSetting);
    },
	"exportExcel" : function(url){
		$("#actionResultDisplay").empty();
		if(!$("#mainForm").valid()){
			return;
		}
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
		    var param = $("#mainForm").serialize();
		    url = url + "?" + param;
		    document.location.href = url;
		}
	}
    
};

var BINOLPTRPS36 =  new BINOLPTRPS36_global();

$(document).ready(function(){
	
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			month:{required:true,dateYYYYmm:true}
		}		
	});
});
