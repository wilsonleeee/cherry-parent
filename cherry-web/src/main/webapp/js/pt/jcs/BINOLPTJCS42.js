var BINOLPTJCS42 = function () {};

BINOLPTJCS42.prototype = {
		
	"clearMessage":function(){
		$('#actionResultDisplay').empty();
		$('#errorMessage').empty();
	},
		
	/*
	 * 用户查询
	 */
	"search": function(){
		BINOLPTJCS42.clearMessage();
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
         // 查询参数序列化
         var params= $("#mainForm").find("div.column").find(":input").serialize();
         params = params + "&"+$("#mainForm").find("#validFlag").serialize();
         params = params + "&csrftoken=" +$("#csrftoken").val();
         params = params + "&" +getRangeParams();
         url = url + "?" + params;
		 // 显示结果一览
		 $("#section").show();
		 // 表格设置
		 var tableSetting = {
				 // datatable 对象索引
				 index : 1,
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","bSortable": false},
				              	{ "sName": "productName"},
				              	{ "sName": "unitCode"},
								{ "sName": "barCode"},
								{ "sName": "resellerName"},
								{ "sName": "qrCodeCiphertext","bVisible": false},
								{ "sName": "wholeURL"},
								{ "sName": "validFlag","sClass":"center","bVisible": false},
							],
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 2
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		BINOLPTJCS42.clearMessage();
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").find("div.column").find(":input").serialize();
            params = params + "&"+$("#mainForm").find("#validFlag").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            params = params + "&" +getRangeParams();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    },
    
	/* 
     * 全部生成
     */
	"reGenerate" : function(){
		BINOLPTJCS42.clearMessage();
		BINOLPTJCS42.showDialog();
    },
    
	/* 
	 * 打开dialog
	 * 
	 * Inputs:  Object:dialogSetting		生成dialog的参数
	 * 
	 */
	"openDialog":function(dialogSetting) {
		var buttons = [];
		if(dialogSetting.confirm != null) {
			buttons.push({
				id: "btnPopGenQRConfirm",
				text: dialogSetting.confirm,
				click: function() {dialogSetting.confirmEvent();}
			});
		}
		if(dialogSetting.cancel != null) {
			buttons.push({
				text: dialogSetting.cancel,
				click: function() {dialogSetting.cancelEvent();}
			});
		}
		var _dialogSetting = {
			bgiframe: true,
			width: dialogSetting.width,
			height: dialogSetting.height,
			title: dialogSetting.title,
			zIndex: 30,  
			modal: true, 
			resizable: false,
			close: function() {BINOLPTJCS42.removeShowDialog(dialogSetting.dialogInit);}		
		};
		if(buttons.length > 0) {
			_dialogSetting.buttons = buttons;
		}
		if(typeof dialogSetting.closeEvent == "function") {
			_dialogSetting.close = function(){dialogSetting.closeEvent();};
		}
		var $dialogInit = $(dialogSetting.dialogInit);
		if(dialogSetting.text != null) {
			$dialogInit.html(dialogSetting.text);
		} else {
			$dialogInit.html($("#dialogInitMessage").html());
		}
		$dialogInit.dialog(_dialogSetting);
	},

	"removeShowDialog":function(dialogDiv){
		var dialogDivId = $(dialogDiv).attr("id");
		$(dialogDiv).dialog('destroy');
		$(dialogDiv).remove();
		$('#hideDivGenerateQRCode').append('<div style="display:none" id="'+dialogDivId+'"></div>');
	},
	
	"showDialog":function(){
		var text = $("#generateQRCodeDialog").html();
		text = text.replace('formGenerateQRCodeTemplet','formGenerateQRCode');
		var dialogSetting = {
			dialogInit: "#dialogGenerateQRCodeInit",
			text: text,
			width: 	470,
			height: 280,
			title: 	$("#JCS42_popTitle").val(),
			confirm: $("#JCS42_resetQRCode").val(),
			cancel: $("#JCS42_cancel").val(),
			confirmEvent: function(){BINOLPTJCS42.confirm();},
			cancelEvent: function(){BINOLPTJCS42.removeShowDialog("#dialogGenerateQRCodeInit");}
		};
		BINOLPTJCS42.openDialog(dialogSetting);
	},

	"confirm":function(){
		var prefixURL = $("#formGenerateQRCode #prefixURL").val().trim();
		if(prefixURL == ""){
			$("#formGenerateQRCode #errorDivGenQR").show();
			return;
		}
		var brandInfoID = $("#formGenerateQRCode #brandInfoIdGenQR").val();
		var resellerId = $("#formGenerateQRCode #resellerIdGenQR").val();
	    
	    var url = $("#reGenerateUrl").attr("href");
	    var param = "prefixURL="+prefixURL+"&brandInfoId="+brandInfoID+"&resellerId="+resellerId;
	    $("#btnPopGenQRConfirm span").text($("#JCS42_genWaiting").val());
	    cherryAjaxRequest({
	        url:url,
	        param:param,
	        callback:function(msg){
	        	BINOLPTJCS42.removeShowDialog("#dialogGenerateQRCodeInit");
	        	if(oTableArr[1] != null)oTableArr[1].fnDraw();
	        },
	        coverId:"#btnPopGenQRConfirm"
	    });
	},
	
	"popBrandChange":function(){
		var url = $('#getResellerListUrl').attr("href");
		var param = "brandInfoId="+$("#formGenerateQRCode #brandInfoIdGenQR").val();
		var callback = function(msg){
			$("#formGenerateQRCode #resellerIdGenQR").find("option:not(:first)").remove();
			var json = eval("("+msg+")"); 
			$.each(json, function(i){
				$("#formGenerateQRCode #resellerIdGenQR").append("<option value='"+ json[i].BIN_ResellerInfoID+"'>"+escapeHTMLHandle(json[i].ResellerCodeName)+"</option>"); 
			});
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	}
};

var BINOLPTJCS42 = new BINOLPTJCS42();

$(function(){
	productBinding({elementId:"productName",showNum:20});
	
	var option = {
			elementId:"resellerName",
			selectedCode:"resellerCode",
			selected:"name",
			showNum:20,
			flag:"ALL"
	};
	resellerInfoBinding(option);
	
	BINOLPTJCS42.search();
	$("#export").live('click',function(){
		BINOLPTJCS42.exportExcel();
	});
});