function BINOLPTRPS37_global() {
	
};

BINOLPTRPS37_global.prototype = {
		// 查询
		"search" : function(url){
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}
			var aoColumns = null;
			aoColumns = [{ "sName": "no","bSortable":false},
			             { "sName": "departCode"},	
			             { "sName": "depotCode"},	
			             { "sName": "logicDepotCode"},
			             { "sName": "productName"},
			             { "sName": "unitCode"},
			             { "sName": "barCode"},
			             { "sName": "moduleCode","sClass":"alignRight"},
			             { "sName": "stockQuantity","sClass":"center"}
			             ];
			// 显示查询结果
			$("#section").show();
			// 查询参数序列化
			 var params= getSearchParams();
			 url = url + "?" + params;
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					// 排序列
					 aaSorting : [[1, "desc"]],
					 // 表格列属性设置
					 aoColumns : aoColumns,	
					// 不可设置显示或隐藏的列	
					aiExclude :[0, 1, 2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 固定列数
					fixedColumns : 2,
					callbackFun : function (msg){
			 		}
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		//xls导出
		"exportExcel" : function(url){
			if($(".dataTables_empty:visible").length==0){
				if (!$('#mainForm').valid()) {
			        return false;
			    };
				var callback = function(msg) {
					var params = getSearchParams();
					url = url + "?" + params;
					window.open(url,"_self");
		    	}
		    	exportExcelReport({
		    		url: $("#exporChecktUrl").attr("href"),
		    		param: getSearchParams(),
		    		callback: callback
		    	});
				
		    }
		},

		//导出CSV
		"exportCsv" : function() {
			if($(".dataTables_empty:visible").length==0) {
				if (!$('#mainForm').valid()) {
			        return false;
			    };
			    var params = getSearchParams();
				exportReport({
					exportUrl:$("#exportCsvUrl").attr("href"),
					exportParam:params
				});
			}
		}
}

var BINOLPTRPS37 =  new BINOLPTRPS37_global();

/**
 * 
 * 查询参数序列化
 */
function getSearchParams(){
	var $form = $("#mainForm");
	var params= $form.find("div.column").find(":input").serialize(); 
	params = params + "&csrftoken=" +$("#csrftoken").val();
	params = params + "&" +getRangeParams();
	return params;
}

$(document).ready(function(){
	
	productBinding({elementId:"nameTotal",showNum:20});
	
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			minLimit: {required : true,numberValid: true}
		}
	});
});

