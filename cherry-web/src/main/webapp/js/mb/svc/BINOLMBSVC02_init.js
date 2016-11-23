
function BINOLMBSVC02_init() {
	
};

BINOLMBSVC02_init.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
			
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		"searchList":function(){
			if (!$('#saleForm').valid()) {
				return false;
			};
			
			//清除提示信息
			$('#actionResultDisplay').empty();
			var url = $("#saleSearchUrl").attr("href");
			var params = $("#saleForm").serialize()+ "&" + getSerializeToken();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
				// 表格ID
				tableId : '#resultSaleDataTable',
				// 数据URL
				url : url,
				// 表格默认排序
				aaSorting : [ [ 1, "asc" ] ],
				// 表格列属性设置
				aoColumns : [ {
					"sName" : "number",
					"sWidth" : "5%",
					"bSortable" : false
				}, {
					"sName" : "cardCode",
					"sWidth" : "10%",
					"bSortable" : true
				}, {
					"sName" : "cardType",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "departName",
					"sWidth" : "15%",
					"bSortable" : true
				}, {
					"sName" : "transactionTime",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "billCode",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "relevantCode",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "rechargeSaleBillCode",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "transactionType",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "amount",
					"sWidth" : "15%",
					"bSortable" : true
				} ,{
					"sName" : "giftAmount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "totalAmount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				},{
					"sName" : "discount",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "validFlag",
					"sWidth" : "15%",
					"bSortable" : true,
					"bVisible" : false
				} ,{
					"sName" : "operator",
					"sWidth" : "15%"
				}],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		},
		"initSaleDetail":function(billCode,cardCode){
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 1000,
					height: 500,
					title: "服务明细",
					closeEvent:function(){
					}
			};
			openDialog(dialogSetting);
			var viewSaleDetailInitUrl=$('#viewSaleDetailInitUrl').attr("href");
			viewSaleDetailInitUrl += "?billCode="+billCode+"&cardCode="+ cardCode + "&" +getSerializeToken();
			cherryAjaxRequest({
				url: viewSaleDetailInitUrl,
				callback: function(data) {
					$("#dialogInit").html(data);
				}
			});
		}
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLMBSVC02_init.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLMBSVC02_init.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};



var BINOLMBSVC02_init =  new BINOLMBSVC02_init();

$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	
	cherryValidate({			
		formId: "saleForm",		
		rules: {
			fromDate: {dateValid:true},	// 开始日期
			toDate: {dateValid:true}	// 结束日期
		}		
	});
	
	// 柜台下拉框绑定
	var option = {
			elementId:"counterName",
			showNum:20,
			targetId:"counterCode",
			targetDetail:true
		};
	counterSelectBinding(option);
	
	BINOLMBSVC02_init.searchList();

	$("#saleExportExcel").click(function(){
		var param=$("#saleForm").serialize();
			var callback = function(msg) {
        		var url = $("#saleExportExcelUrl").attr("href");
        		var params=$("#saleForm").serialize()+"&" + getSerializeToken();
        		var that = this;
    			that.needUnlock=false;
	            url = url + "?" +params;
	            document.location.href = url;
	            that.needUnlock=true;
        	};
        	exportExcelReport({
        		url: $("#saleExportCheckUrl").attr("href"),
        		param: $("#saleForm").serialize()+"&" + getSerializeToken(),
        		callback: callback
        	});
		return false;
	});
	
	$("#saleExportCsv").click(function(){
			exportReport({
				exportUrl:$("#saleExportCsvUrl").attr("href"),
				exportParam:$("#saleForm").serialize()+"&" + getSerializeToken()
			});
		return false;
	});
	BINOLMBSVC02_init.needUnlock = true;
});
