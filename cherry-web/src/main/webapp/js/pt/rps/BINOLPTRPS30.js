
function BINOLPTRPS30_global() {
	
};

BINOLPTRPS30_global.prototype = {
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
			 aaSorting : [[5, "desc"]],
			 // 表格列属性设置
			 aoColumns : [	
	                    	{ "sName": "no", "sWidth": "5%","bSortable": false},			// 1
	                    	{ "sName": "regionName", "sWidth": "10%","bVisible": false},// 2
	                    	{ "sName": "resellerName", "sWidth": "10%","bVisible": false},// 3
	                    	{ "sName": "basName", "sWidth": "10%","bVisible": false},// 4
	                    	{ "sName": "counterName", "sWidth": "15%"},// 5
	                    	{ "sName": "saleAmount", "sWidth": "15%"},// 6
	                    	{ "sName": "saleCount", "sWidth": "10%"},// 7
	                    	{ "sName": "saleQuantity", "sWidth": "15%","sClass":"center"},//8
	                    	{ "sName": "saleQuantity1", "sWidth": "5%","sClass":"center"}, // 9
	                    	{ "sName": "memSaleAmount", "sWidth": "5%","sClass":"center"},// 10
	                    	{ "sName": "notMemSaleAmout", "sWidth": "5%","sClass":"center"},// 11
	                    	{ "sName": "promSaleAmount", "sWidth": "5%","sClass":"center"},// 12
	                    	{ "sName": "inventoryAmount", "sWidth": "5%","sClass":"center"}//13
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
                var checkUrl = getBaseUrl() + "/pt/BINOLPTRPS30_checkExport";
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

var BINOLPTRPS30 =  new BINOLPTRPS30_global();

$(document).ready(function(){
	var counterOption = {
			elementId: "counterName",
			showNum: "20",
			selected: "name"
	}
	counterBinding(counterOption);
    $('#startDate').cherryDate({
        beforeShow: function(input){
            var value = $('#endDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endDate').cherryDate({
        beforeShow: function(input){
            var value = $('#startDate').val();
            return [value,'minDate'];
        }
    });
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate:{required:true,dateValid:true},
			endDate:{required:true,dateValid:true}
		}		
	});
});
