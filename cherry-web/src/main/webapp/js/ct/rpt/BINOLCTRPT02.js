var BINOLCTRPT02_GLOBAL = function() {

};

BINOLCTRPT02_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "batchId", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "eventType", "sWidth" : "10%"},
		                    {"sName" : "commType", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runType", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runBeginTime", "sWidth" : "5%"},
		                    {"sName" : "sendMsgNum", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "sendErrorNum", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runStatus", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "runError", "bVisible" : false,"sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false} 
		                    ];
		
		var url = $("#evnetRunSearchUrl").attr("href");
		var params = $form.serialize();
		url = url + "?" + params;
		$("#runDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 5, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 2 , 5 , 6 , 7 , 8],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2,
			callbackFun : function(msg) {
				var $msg = $("<div></div>").html(msg);
		 		var $totalInfo = $("#totalInfo",$msg);
		 		if($totalInfo.length > 0){
		 			$("#totalInfo").html($totalInfo.html());
		 		}else{
		 			$("#totalInfo").empty();
			 	}
			},
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},	
	"exportExcel" : function(url,exportFormat){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#mainForm").serialize();
            params = params + "&" +getSerializeToken() + "&exportFormat=" +exportFormat;
            if(exportFormat == "0"){
	            url = url + "?" +params;
	            document.location.href = url;
            }else{
	            exportReport({
					exportUrl: url,
					exportParam: params
				});
            }
		}
	}
	
};

var BINOLCTRPT02 = new BINOLCTRPT02_GLOBAL();

$(document).ready(
	function(){
		$("#startTime").cherryDate({
			beforeShow: function(input){
				var value = $("#endTime").val();
				return [value, "maxDate"];
			}
		});
		$("#endTime").cherryDate({
			beforeShow: function(input){
				var value = $("#startTime").val();
				return [value, "minDate"];
			}
		});
		cherryValidate({//form表单验证
			formId: "mainForm",		
			rules: {
				startTime:{dateValid: true},
				endTime:{dateValid: true}
			}		
		});
	}
);
