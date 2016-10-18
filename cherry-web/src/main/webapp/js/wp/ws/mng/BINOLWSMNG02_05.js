var BINOLWSMNG02_05_global = {};
BINOLWSMNG02_05_global.deliverDialogHtml="";

var BINOLWSMNG02_05 = function () {
    
};

BINOLWSMNG02_05.prototype = {
	/**
	 * 产品发货单框（可收货）
	 * @param thisObj
	 * @param param
	 * @return
	 */
	"popDataTableOfProDeliver":function(param){
		if ("" == BINOLWSMNG02_05_global.deliverDialogHtml) {
			// 临时存放部门信息页面内容
			BINOLWSMNG02_05_global.deliverDialogHtml = $("#deliverDialog").html();
		} else {
			$("#deliverDialog").html(BINOLWSMNG02_05_global.deliverDialogHtml);
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
			title:$("#MNG02_receive").text(),
			close: function(event, ui) {removeDialog("#deliverDialog");}
		};
		var height = 420;
		$('#deliverDialog').css("max-height",height+"px");
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
		$('#deliverDialog').dialog(dialogSetting);
	},
	
	"checkRecData":function(){
		var tradeEmployeeID = $("#tradeEmployeeID").val();
		if(tradeEmployeeID == ""){
			$("#errorSpan2").html($("#errmsg_EST00039").val());
			$("#errorDiv2").show();
			return false;
		}else{
			return true;
		}
	},
	
	"beforeDoActionFun":function(){
		if(binOLWSMNG02_05.checkRecData()){
			return "doaction";
		}else{
			return "";
		}
	}
}

var binOLWSMNG02_05 = new BINOLWSMNG02_05();