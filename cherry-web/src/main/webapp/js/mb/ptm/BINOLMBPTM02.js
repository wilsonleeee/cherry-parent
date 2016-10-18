
function BINOLMBPTM02() {
	
};

BINOLMBPTM02.prototype = {
		
	"searchPointInfo" : function() {
		if(!$('#pointInfoCherryForm').valid()) {
			return false;
		}
		var url = $("#pointInfoListUrl").attr("href");
		var params= $("#pointInfoCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#pointInfo").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#pointInfoDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 4, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "number", "sWidth": "3%", "bSortable": false },
				                { "sName": "memCode", "sWidth": "10%" },
								{ "sName": "departCode", "sWidth": "10%" },
								{ "sName": "billCode", "sWidth": "30%" },
								{ "sName": "changeDate", "sWidth": "15%" },
								{ "sName": "amount", "sWidth": "10%" },
								{ "sName": "quantity", "sWidth": "5%" },
								{ "sName": "point", "sWidth": "10%" },
								{ "sName": "operate", "sWidth": "7%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    
    /**
    * 一览excel导出
    */
    "exportExcel" : function() {
    	if($(".dataTables_empty:visible").length==0) {
    		if(!$('#pointInfoCherryForm').valid()) {
    			return false;
    		}
    		var params = $("#pointInfoCherryForm").serialize();
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
    },
    
    /**
     * 一览Csv导出
     */
    "exportCsv" : function() {
    	if($(".dataTables_empty:visible").length==0) {
    		if (!$('#pointInfoCherryForm').valid()) {
    	        return false;
    	    };
    	    var params = $("#pointInfoCherryForm").serialize();
    		exportReport({
    			exportUrl:$("#exportCsvUrl").attr("href"),
    			exportParam:params
    		});
    	}
    }
};

var binolmbptm02 =  new BINOLMBPTM02();

$(function(){
	$('#changeDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#changeDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#changeDateStart').val();
			return [value,'minDate'];
		}
	});
	productBinding({elementId:"nameTotal",showNum:20});
	counterBinding({elementId:"departCode",showNum:20,selected:"code"});
	
	// 表单验证初期化
	cherryValidate({
		formId: 'pointInfoCherryForm',
		rules: {
			changeDateStart: {dateValid: true},
			changeDateEnd: {dateValid: true},
			memPointStart: {numberValid: true},
			memPointEnd: {numberValid: true}
		}
	});
});