var STCM12_global = {};
STCM12_global.deliverDialogHtml="";

/**
 * 产品复制发货单框
 * @param thisObj
 * @param param
 * @return
 */
function popDataTableOfProDeliver(thisObj, param,callback){
	if ("" == STCM12_global.deliverDialogHtml) {
		// 临时存放部门信息页面内容
		STCM12_global.deliverDialogHtml = $("#deliverDialog").html();
	} else {
		$("#deliverDialog").html(STCM12_global.deliverDialogHtml);
	}
	oTableArr[31] = null;
	var url = $('#popDeliverUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#deliverDataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  
			              	{ "sName": "radio","sWidth": "1%","bSortable": false},
			                { "sName": "deliverRecNo","sWidth": "20%","bSortable": true},
							{ "sName": "departName","sWidth": "10%"},
							{ "sName": "departNameReceive","sWidth": "10%"},
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
		title:$("#SendBillTitle").text(),
		close: function(event, ui) {removeDialog("#deliverDialog");}
	};
	var height = 420;
	$('#deliverDialog').css("max-height",height+"px");
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
	$('#deliverDialog').dialog(dialogSetting);
}