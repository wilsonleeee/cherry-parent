function BINOLMBRPT06_global() {	
};

BINOLMBRPT06_global.prototype = {
		"search" : function() {
			$("#actionResultDisplay").empty();
			if(!$("#mainForm").valid()){
				return;
			}
			var url = $("#searchUrl").attr("href");
			var params= $("#mainForm").serialize();
			url = url + "?" + params;
			
			$("#section").show();
			// 表格设置
			tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 排序列
				 aaSorting : [[1, "desc"]],
				 // 表格列属性设置
				 aoColumns : [	
		                    	{ "sName": "no", "sWidth": "1%","bSortable": false},
		                    	{ "sName": "member"},
		                    	{ "sName": "recommendedMember"},
		                    	{ "sName": "mobilePhone"},
		                    	{ "sName": "orderCount","sClass":"alignRight"},
		                    	{ "sName": "saleQuantity","sClass":"alignRight"},
		                    	{ "sName": "saleAmount","sClass":"alignRight"}
							],
				 sScrollX : "100%",
				// 不可设置显示或隐藏的列	
				aiExclude :[0,1],
				callbackFun : function (msg){
					var $msg = $("<div></div>").html(msg);
					var $headInfo = $("#headInfo",$msg);
					$("#headInfo").html($headInfo.html());
		 		}
			};
			
			// 调用获取表格函数
			getTable(tableSetting);
		},
		
		/**
	     * 推荐会员购买信息导出
	     */
		"exportExcel" : function(exportFormat){
			var url = $("#exportExcelUrl").attr("href");
			$("#actionResultDisplay").empty();
			//无数据不导出
			if($(".dataTables_empty:visible").length==0){
				if (!$('#mainForm').valid()) {
			        return false;
			    };
				var params= $("#mainForm").serialize();
				params = params + "&exportFormat=" + exportFormat;
				
	            if(exportFormat == "0"){
	            	var callback = function(msg) {
	        		    url = url + "?" + params;
	        		    window.open(url,"_self");
	        	    }
	            	
	        	    exportExcelReport({
	            		url: $("#exporCheckUrl").attr("href"),
	            		param: params,
	            		callback: callback
	            	});
	            }else {
					exportReport({
						exportUrl : url,
						exportParam : params
					});
	            }
			}
		}
		
};
var BINOLMBRPT06 =  new BINOLMBRPT06_global();

$(document).ready(function(){
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate:{required:true,dateValid:true},
			endDate:{required:true,dateValid:true}
		}		
	});
});
