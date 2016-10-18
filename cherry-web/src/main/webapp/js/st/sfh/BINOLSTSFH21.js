
function BINOLSTSFH21_global() {	
};

BINOLSTSFH21_global.prototype = {
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
			 aaSorting : [[7, "desc"]],
			 // 表格列属性设置
			 aoColumns : [	
	                    	{ "sName": "no", "bSortable": false},			// 1
	                    	{ "sName": "departName"},// 部门名称
	                    	{ "sName": "startDate"},// 销售数据汇总开始日期
	                    	{ "sName": "endDate"},// 销售数据汇总结束日期
	                    	{ "sName": "FPC", "bVisible": false},// 空列
	                    	{ "sName": "barCode"},// 产品条码
	                    	{ "sName": "prtName"},// 产品英文名
	                    	{ "sName": "saleQuantity", "sClass":"alignRight"},// 统计周期中的销售数量
	                    	{ "sName": "saleAmount ", "sClass":"alignRight"}, // 统计周期中的销售金额
	                    	{ "sName": "stockQuantity ", "sClass":"alignRight"},// 期末库存数量
	                    	{ "sName": "UnitsInTransit", "sClass":"center", "bVisible": false},// 空列
	                    	{ "sName": "stockAmount", "sClass":"alignRight"},//  期末库存数量
	                    	{ "sName": "propNameMid", "sClass":"center"},//产品系列-POS 中分类
	                    	{ "sName": "propNameBig", "sClass":"center"},// POS大分类
	                    	{ "sName": "Brand", "sClass":"center"},// 固定填写MaxFactor
	                    	{ "sName": "salePrice", "sClass":"alignRight"}// 标准零售价
						],
			 sScrollX : "100%",
			// 不可设置显示或隐藏的列	
			aiExclude :[0,1,2,3,4,5],
			 // 固定列数
			fixedColumns : 6
		};
		$("#section").show();
		// 调用获取表格函数
		getTable(tableSetting);
    },
    
    /**
     * 导出数据
     */
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

var BINOLSTSFH21 =  new BINOLSTSFH21_global();

$(document).ready(function(){
	// 产品选择绑定
	productBinding({elementId:"nameTotal",showNum:20});
    
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate:{required:true,dateValid:true},
			endDate:{required:true,dateValid:true}
		}		
	});
});
