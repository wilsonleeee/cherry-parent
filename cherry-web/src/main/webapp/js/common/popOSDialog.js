var popOSDialog = function () {};

popOSDialog.prototype = {
		
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
					id: "btnPopOSConfirm",
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
				close: function() {popOSDialog.removeShowDialog(dialogSetting.dialogInit);}		
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
		$('#hideDivOS').append('<div style="display:none" id="'+dialogDivId+'"></div>');
	},
		
	"showDialog":function(actionid){
		if($("#errorDiv2 #errorSpan2").is(":visible")){
			return;
		}
		
		var text = "";
		text += $("#osDialog").html();
		text = text.replace('osDialogFormTemplet','osDialogForm');
		var dialogSetting = {
			dialogInit: "#dialogOSInit",
			text: text,
			width: 	387,
			height: 310,
			title: 	$("#os_confirm").val(),
			confirm: $("#os_global_page_ok").val(),
			cancel: $("#os_global_page_cancel").val(),
			confirmEvent: function(){popOSDialog.confirm(actionid);},
			cancelEvent: function(){popOSDialog.removeShowDialog("#dialogOSInit");}
		};
		popOSDialog.openDialog(dialogSetting);
	},
	
	"showSDDialog":function(actionid){
		var text = "";
		text += $("#osSDNoIFDialog").html();
		text = text.replace('osDialogFormSDNoIF','osDialogForm');
		text = text.replace(/deliverNoIFTemplet/g,'deliverNoIF');
		var dialogSetting = {
			dialogInit: "#dialogOSInit",
			text: text,
			width: 	350,
			height: 160,
			title: 	$("#os_pd_pleaseInputSDNoIF").val(),
			confirm: $("#os_global_page_ok").val(),
			cancel: $("#os_global_page_cancel").val(),
			confirmEvent: function(){popOSDialog.confirmSDNoIF(actionid);},
			cancelEvent: function(){popOSDialog.removeShowDialog("#dialogOSInit");}
		};
		popOSDialog.openDialog(dialogSetting);
		
		cherryValidate({			
			formId: "osDialogForm",		
			rules: {
				deliverNoIF: {alphanumeric:true,maxlength: 40}
			}		
		});
	},
	
	"confirm":function(actionid){
		var opComments = $("#osDialogForm #opComments").val();
		if(opComments == ""){
			$("#osDialogForm #errorDivOS").show();
			return;
		}
	    
		var entryid = $("#entryID").val();
	    var curl = $("#osdoactionUrl").attr("href");
	    curl=curl +"?entryid="+entryid+"&actionid="+actionid;
	    var params=$('#mainForm').formSerialize();
	    params += "&opComments="+$("#osDialogForm #opComments").val();
	    cherryAjaxRequest({
	        url:curl,
	        param:params,
	        callback:function(msg){
	          if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
	          if($(window.opener.document).find("#msgListDialog").is(":visible")){
	        	  if(window.opener.oTableArr[229] != null)window.opener.oTableArr[229].fnDraw();
	          }
	          if($(window.opener.document).find("#deliverDialog").is(":visible") || $(window.opener.document).find("#allocationDialog").is(":visible")){
	        	  if(window.opener.oTableArr[31] != null)window.opener.oTableArr[31].fnDraw();
	          }
	          if($("#errorDiv2")) $("#errorDiv2").hide();
	          refreshGadgetTask();
	        },
	        coverId:"#btnPopOSConfirm"
	    });
	},
	
	"confirmSDNoIF":function(actionid){
		if(!$("#osDialogForm").valid()){
			return;
		}
		var deliverNoIF = $("#osDialogForm #deliverNoIF").val();
		var entryid = $("#entryID").val();
	    var curl = $("#osdoactionUrl").attr("href");
	    curl=curl +"?entryid="+entryid+"&actionid="+actionid;
	    var params=$('#mainForm').formSerialize();
	    params += "&deliverNoIF="+deliverNoIF;
	    cherryAjaxRequest({
	        url:curl,
	        param:params,
	        callback:function(msg){
	        	if(msg.indexOf("fieldErrorDiv")>-1){
	        		return;
	        	}
	        	if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
	        	if($(window.opener.document).find("#msgListDialog").is(":visible")){
	        		if(window.opener.oTableArr[229] != null)window.opener.oTableArr[229].fnDraw();
	        	}
	        	if($("#errorDiv2")) $("#errorDiv2").hide();
	        	refreshGadgetTask();
	        },
	        coverId:"#btnPopOSConfirm"
	    });
	}
};

var popOSDialog = new popOSDialog();