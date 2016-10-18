function BINOLSTCM15(){};

var STCM15_global = {};
STCM15_global.prtStockDialogHtml="";
STCM15_global.tableIndex = 31;

BINOLSTCM15.prototype={
	"changeHideID":function(){
		if($("#STCM15_depotId").val() != undefined && $("#STCM15_depotId").val() != ""){
			$("#hideODInventoryInfoID").val($("#STCM15_depotId").val());
		}
		if($("#STCM15_logicInventoryId").val() != undefined && $("#STCM15_logicInventoryId").val() != ""){
			$("#hideODLogicInventoryInfoID").val($("#STCM15_logicInventoryId").val());
		}
		var oSettings = oTableArr[STCM15_global.tableIndex].fnSettings();
		var url = oSettings.sAjaxSource;
		url = replaceParamVal(url, "inventoryInfoID", $("#hideODInventoryInfoID").val(), true);
		url = replaceParamVal(url, "logicInventoryInfoID", $("#hideODLogicInventoryInfoID").val(), true);
		if($("#searchStockForm").valid()){
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			url = replaceParamVal(url, "startDate", startDate, true);
			url = replaceParamVal(url, "endDate", endDate, true);
			oSettings.sAjaxSource = url;
			// 刷新表格数据
			oTableArr[STCM15_global.tableIndex].fnDraw();
		}
	},
	"searchStock":function(){
		if($("#searchStockForm").valid()){
			var startDate = $("#startDate").val();
			var endDate = $("#endDate").val();
			var oSettings = oTableArr[STCM15_global.tableIndex].fnSettings();
			var url = oSettings.sAjaxSource;
			url = replaceParamVal(url, "startDate", startDate, true);
			url = replaceParamVal(url, "endDate", endDate, true);
			oSettings.sAjaxSource = url;
			// 刷新表格数据
			oTableArr[STCM15_global.tableIndex].fnDraw();
		}
	}
};

var BINOLSTCM15 = new BINOLSTCM15();

function popAjaxProStockDialog (option){
	var $dialog = $('#prtStockDialog');
	if($dialog.length == 0){
		var url = "/Cherry/st/common/BINOLSTCM15_init";
		if(option.initType != null && option.initType == "select"){
			url += "?" + getSerializeToken()+"&initType=select&brandInfoID="+option.brandInfoID+"&departID="+option.departID;
		}else if(option.initType != null && option.initType == "showCodeNameByID"){
			url += "?" + getSerializeToken()+"&initType=showCodeNameByID&brandInfoID="+option.brandInfoID+"&departID="+option.departID+"&inventoryInfoID="+option.inventoryInfoID+"&logicInventoryInfoID="+option.logicInventoryInfoID;
		}else{
			url += "?" + getSerializeToken()+"&entryID="+$('#entryID').val()+"&productOrderID="+$("#productOrderID").val();
		}
		$.ajax({
	        url: url,
	        type: 'post',
	        success: function(msg){
	        	$("body").append(msg);
				popDataTableOfProStock(option);
			}
		});
	}else{
		//重新查询仓库信息
		if(option.initType != null && option.initType == "select"){
			var params = getSerializeToken()+"&initType=select&brandInfoID="+option.brandInfoID+"&departID="+option.departID;
	        cherryAjaxRequest({
	            url:"/Cherry/st/common/BINOLSTCM15_getDepotLogicInfo",
	            param:params,
	            callback:function(data){
	        		var json = eval("("+data+")");    //包数据解析为json 格式  
	        		var depotList = json.depotList;
	        		var logicDepotList = json.logicDepotList;
	        		$("#STCM15_depotId option").remove();
	        		$.each(depotList, function(i){
	        			$("#STCM15_depotId ").append("<option value='"+ depotList[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(depotList[i].DepotCodeName)+"</option>"); 
	        			if(i == 0){
	        				$("#hideODInventoryInfoID").val(depotList[i].BIN_DepotInfoID);
	        			}
	        		});
	            	
	        		$("#STCM15_logicInventoryId option").remove();
	        		$.each(logicDepotList, function(i){
	        			$("#STCM15_logicInventoryId ").append("<option value='"+ logicDepotList[i].BIN_LogicInventoryInfoID+"'>"+escapeHTMLHandle(logicDepotList[i].LogicInventoryCodeName)+"</option>"); 
	        			if(i == 0){
	        				$("#hideODLogicInventoryInfoID").val(logicDepotList[i].BIN_LogicInventoryInfoID);
	        			}
	        		});
	            	popDataTableOfProStock(option);
	            }
	        });
		}else{
			popDataTableOfProStock(option);
		}
	}
}

/**
 * 产品发货接收方库存弹出框
 * @param thisObj
 * @param param
 * @return
 */
function popDataTableOfProStock(option){
	var url = $('#popPrtStockUrl').text();
	url += "?" + getSerializeToken();
	// startDate
	url += "&" + $('#startDate').serialize();
	// endDate
	url += "&" + $('#endDate').serialize();
	url += "&" + option.param;
	url += "&inventoryInfoID=" + $("#hideODInventoryInfoID").val();
	url += "&logicInventoryInfoID=" + $("#hideODLogicInventoryInfoID").val();
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:50,
			 // 表格ID
			 tableId : '#stockDataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  
			              	{ "sName": "no","sWidth": "10%","bSortable": false},
			                { "sName": "UnitCode","sWidth": "10%","bSortable": false},
							{ "sName": "BarCode","sWidth": "10%","bSortable": false},
							{ "sName": "ProductName","sWidth": "20%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false},
							{ "sName": "Quantity","sWidth": "10%","bSortable": false}
						 ],
			index : STCM15_global.tableIndex,
			aaSorting : [[1, "asc"]],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fixedColumns : 6
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var departName = $("#orderDepartCodeName").html();
	if(departName == null || departName == ""){
		departName = option.DepartName;
	}
	var dialogSetting = {
		bgiframe: true,
		width:1000, 
		zIndex: 9999,
		minHeight:320,
		modal: true,
		resizable: true,
		//title:$("#OrderDepartTitle").text()+" "+departName+" "+$("#CounterStockTitle").text(),
		title:departName+" "+$("#CounterStockTitle").text(),
		close: function(event, ui) {
			//还原到默认
			$("#stockKw").val("");
			if (oTableArr[STCM15_global.tableIndex]!=null){
				var oSettings = oTableArr[STCM15_global.tableIndex].fnSettings();
				oSettings.oPreviousSearch.sSearch = "";
			}
			$("#startDate").val($("#hideStartDate").val());
			$("#endDate").val($("#hideEndDate").val());
			$("#searchStockForm").valid();
			$("#stockDataTable_Cloned tr").remove();
			$("#stockDataTable tr").remove();
			$("#stockDataTable_info").html("");
			$(this).dialog( "destroy" );
		}
	};
	var height = 1600;
	$('#prtStockDialog').css("max-height",height+"px");
	dialogSetting.buttons = [{
		text: $("#STCM15_global_page_ok").text(),
		click: function() { 
			$(this).dialog("close");
		}
	}];
	dialogSetting.maxHeight = height + 80;
	$('#prtStockDialog').dialog(dialogSetting);
}