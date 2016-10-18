var SSCM10_global = {};
SSCM10_global.inDepotDialogHtml="";

/**
 * 促销品复制入库单框
 * @param thisObj
 * @param param
 * @return
 */
function popDataTableOfPrmInDepot(thisObj, param,callback){
	if ("" == SSCM10_global.inDepotDialogHtml) {
		// 临时存放部门信息页面内容
		SSCM10_global.inDepotDialogHtml = $("#inDepotDialog").html();
	} else {
		$("#inDepotDialog").html(SSCM10_global.inDepotDialogHtml);
	}
	oTableArr[31] = null;
	var url = $('#popInDepotUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#inDepotDataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  
			              	{ "sName": "radio","sWidth": "1%","bSortable": false},
			                { "sName": "billNoIF","sWidth": "20%","bSortable": true},
							{ "sName": "departName","sWidth": "10%"},
							{ "sName": "totalQuantity","sWidth": "10%"},
							{ "sName": "totalAmount","sWidth": "10%"}
						 ],
			index : 31,
			aaSorting : [[1, "desc"]]
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:800, 
		zIndex: 9999,
		minHeight:320,
		modal: true,
		resizable: true,
		title:$("#InDepotBillTitle").text(),
		close: function(event, ui) {removeDialog("#inDepotDialog");}
	};
	var height = 420;
	$('#inDepotDialog').css("max-height",height+"px");
	dialogSetting.buttons = [{
		text: $("#global_page_ok").text(),
		click: function() { 
			if(typeof(callback) == "function") {
				callback();
			}
			$(this).dialog("close");
		}
	}];
	dialogSetting.maxHeight = height + 80;
	$('#inDepotDialog').dialog(dialogSetting);
}