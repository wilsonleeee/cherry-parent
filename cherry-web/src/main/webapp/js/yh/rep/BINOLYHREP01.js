function BINOLYHREP01_global() {	
};

BINOLYHREP01_global.prototype = {
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
		                    	{ "sName": "billCode"},
		                    	{ "sName": "couponCode"},
		                    	{ "sName": "memberName"},
		                    	{ "sName": "productName"},
		                    	{ "sName": "detailAmount","sClass":"alignRight"},
		                    	{ "sName": "resellerName1L","bSortable": false},
		                    	{ "sName": "resellerName2L","bSortable": false}
							],
				 sScrollX : "100%",
				// 不可设置显示或隐藏的列	
				aiExclude :[0,1,2],
				// 滚动体的宽度
				sScrollXInner:"",
				// 固定列数
				fixedColumns : 3,
				callbackFun : function (msg){
					var $msg = $("<div></div>").html(msg);
					var $headInfo = $("#headInfo",$msg);
					$("#headInfo").html($headInfo.html());
		 		}
			};
			
			// 调用获取表格函数
			getTable(tableSetting);
		},
		
		/*
		 * 代理商类型变化时清空一、二级代理商值
		 */
		"clearResellerVal" : function() {
			$("#parentResellerCode").val("");
			$("#parentResellerName").val("");
			$("#resellerCode").val("");
			$("#resellerName").val("");
			
		},
		/**
	     * 推荐会员购买信息导出
	     */
		"exportMemBuyInfoExcel" : function(url,exportFormat){
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
	            		url: $("#memberBuyExporCheckUrl").attr("href"),
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
		},
		/**
	     * 推荐会员购买信息导出
	     */
		"exportBaSaleExcel" : function(url,exportFormat){
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
	            		url: $("#baSaleExporCheckUrl").attr("href"),
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
var BINOLYHREP01 =  new BINOLYHREP01_global();

/**
 * 查询参数序列化
 * @returns
 */
function getSearchParams() {
	var $form = $("#mainForm");
	var params= $form.serialize();
	params = params + "&" +getRangeParams();
	return params;
}

$(document).ready(function(){
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	
	//一级代理商下拉框绑定
	var optionParent = {
			elementId:"parentResellerName",
			selectedCode:"parentResellerCode",
			selected:"name",
			showNum:20,
			flag:"1",
			provinceKey:"provinceId",
			cityKey:"cityId",
			resellerTypeKey:"resellerType"
			
	};
	resellerInfoBinding(optionParent);
	
	// 二级代理商下拉框绑定
	var option = {
			elementId:"resellerName",
			selectedCode:"resellerCode",
			selected:"name",
			showNum:20,
			flag:"2",
			parentKey:"parentResellerCode",
			provinceKey:"provinceId",
			cityKey:"cityId",
			resellerTypeKey:"resellerType"
	};
	resellerInfoBinding(option);
	
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate:{required:true,dateValid:true},
			endDate:{required:true,dateValid:true}
		}		
	});
});
