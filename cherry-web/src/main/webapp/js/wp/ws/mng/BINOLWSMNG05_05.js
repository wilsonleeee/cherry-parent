var BINOLWSMNG05_05_global = {};
BINOLWSMNG05_05_global.allocationDialogHtml="";

var BINOLWSMNG05_05 = function () {
    
};

BINOLWSMNG05_05.prototype = {
	/**
	 * 产品调入申请单框（可调出）
	 * @param thisObj
	 * @param param
	 * @return
	 */
	"popDataTableOfProBG":function(param){
		if ("" == BINOLWSMNG05_05_global.allocationDialogHtml) {
			// 临时存放部门信息页面内容
			BINOLWSMNG05_05_global.allocationDialogHtml = $("#allocationDialog").html();
		} else {
			$("#allocationDialog").html(BINOLWSMNG05_05_global.allocationDialogHtml);
		}
		oTableArr[31] = null;
		var url = $('#popProductAllocationUrl').text();
		var csrftoken = $('#csrftoken').serialize();
		if(!csrftoken) {
			csrftoken = $('#csrftoken',window.opener.document).serialize();
		}
		url += "?" + csrftoken + (param?('&'+param):'');
		var tableSetting = {
				 // 一页显示页数
				 iDisplayLength:10,
				 // 表格ID
				 tableId : '#allocationDataTable',
				 // 数据URL
				 url : url,
				 // 表格列属性设置
				 aoColumns : [  
				              	{ "sName": "radio","sWidth": "1%","bSortable": false},
				                { "sName": "allocationNoIF","sWidth": "20%","bSortable": true},
								{ "sName": "departNameIn","sWidth": "10%"},
								{ "sName": "totalQuantity","sWidth": "10%"},
								{ "sName": "totalAmount","sWidth": "10%"}
							 ],
				index : 31,
				colVisFlag: false,
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
			title:$("#MNG05_allocationOut").text(),
			close: function(event, ui) {removeDialog("#allocationDialog");}
		};
		var height = 420;
		$('#allocationDialog').css("max-height",height+"px");
		dialogSetting.buttons = [{
			text: $("#global_page_close").text(),
			click: function() { 
				if(typeof(callback) == "function") {
					callback();
				}
				$(this).dialog("close");
			}
		}];
		dialogSetting.maxHeight = height + 80;
		$('#allocationDialog').dialog(dialogSetting);
	}
}

var binOLWSMNG05_05 = new BINOLWSMNG05_05();