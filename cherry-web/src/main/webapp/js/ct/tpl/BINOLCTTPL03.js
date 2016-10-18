var BINOLCTTPL03_GLOBAL = function() {

};

BINOLCTTPL03_GLOBAL.prototype = {
	"queryTypeChange" : function(){
		BINOLCTTPL03.delActionResult();
		var queryType = $("#queryType").val();
		if(queryType == 2){
			$("#templateUseDiv").show();
		}else{
			$("#templateUseDiv").hide();
		}
		BINOLCTTPL03.search();
	},
	"delActionResult" : function(){
		$("#actionResultDisplay").empty();
	},
	// 参数查询
	"search" : function() {
		var $form = $('#mainForm');
		var url = $("#searchUrl").attr("href");
		var params = $form.serialize();
		$("#variableDetail").show();
		cherryAjaxRequest({
			url : url,
			param : params,
			callback : function(msg) {
				$("#variableDetailTable").html(msg);
				$('#dataTable').find("a.description").cluetip({
					splitTitle : '|',
					width : 300,
					height : 'auto',
					cluetipClass : 'default',
					cursor : 'pointer',
					showTitle : false
				});
			}
		});
	},

	/**
	 * 停用或启用沟通模板参数弹出框
	 * 
	 * @return
	 */
	"disableDialog" : function(associateId, validFlag) {
		BINOLCTTPL03.delActionResult();
		if ($("#disable_dialog").length == 0) {
			$("body").append(
					'<div style="display:none" id="disable_dialog"></div>');
		} else {
			$("#disable_dialog").empty();
		}
		var dialogId = "disable_dialog";
		var $dialog = $('#' + dialogId);
		var dialogTitle = "";
		var dialogContent = "";
		if (validFlag == '0') {
			dialogTitle = $("#disableDialogTitle").text();
			dialogContent = $('#disableDialogContent').html();
		} else {
			dialogTitle = $("#enableDialogTitle").text();
			dialogContent = $('#enableDialogContent').html();
		}
		$dialog.html(dialogContent);
		$dialog.dialog({
			// 默认不打开弹出框
			autoOpen : false,
			// 弹出框宽度
			width : 350,
			// 弹出框高度
			height : 250,
			// 弹出框标题
			title : dialogTitle,
			// 弹出框索引
			zIndex : 1,
			modal : true,
			resizable : false,
			// 弹出框按钮
			buttons : [
					{
						text : $("#dialogConfirm").text(),// 确认按钮
						click : function() {
							var param = "associateId=" + associateId
									+ "&validFlag=" + validFlag;
							// 点击确认后执行停用
							var url = $('#disOrEnableUrl').attr("href");
							cherryAjaxRequest({
								url : url,
								param : param,
								callback : function(data) {
									BINOLCTTPL03.search();
								}
							});
							removeDialog("#disable_dialog");
						}
					}, {
						text : $("#dialogCancel").text(),// 取消按钮
						click : function() {
							removeDialog("#disable_dialog");
						}
					} ],
			// 关闭按钮
			close : function() {
				closeCherryDialog(dialogId);
			}
		});
		$dialog.dialog("open");
	},

	/**
	 * 更新沟通模板参数
	 */
	"update" : function(associateId) {
		$('#editForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var operatorChar = $("#operatorChar").val();
		var computedValue = $("#computedValue").val();
		if(computedValue != undefined && isNaN(computedValue)){
			BINOLCTTPL03.getError("#computedValue",$("#errorMsg1").text());
			return false;
		}else{
			if(operatorChar == '1' || operatorChar == '2'){
				if(computedValue < 0 || computedValue > 100000){
					BINOLCTTPL03.getError("#computedValue",$("#errorMsg2").text());
					return false;
				}
			}else if(operatorChar == '3' || operatorChar == '4'){
				if(computedValue < 0.01 || computedValue > 100){
					BINOLCTTPL03.getError("#computedValue",$("#errorMsg3").text());
					return false;
				}
			}
		}
		var param = $("#editForm").serialize();
		var url = $('#updateUrl').attr("href");
		cherryAjaxRequest({
			url : url,
			param : param,
			callback : function(data) {
				BINOLCTTPL03.search();
				$("#edit_dialog").dialog("close");
			}
		});
	},
	"getError" : function(obj,errorMsg){
		var $parent = $(obj).parent();
		$parent.removeClass('error');
		$parent.find("#errorText").remove();
		$parent.addClass("error");
		$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
		$parent.find('#errorText').attr("title",'error|'+errorMsg);
		$parent.find('#errorText').cluetip({
	    	splitTitle: '|',
		    width: 150,
		    cluezIndex: 20000,
		    tracking: true,
		    cluetipClass: 'error', 
		    arrows: true, 
		    dropShadow: false,
		    hoverIntent: false,
		    sticky: false,
		    mouseOutClose: true,
		    closePosition: 'title',
		    closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	},
	/**
	 * 编辑沟通模板参数弹出框
	 * 
	 * @return
	 */
	"editDialog" : function(variableCode,type) {
		BINOLCTTPL03.delActionResult();
		var callback = function(msg) {
			if ($("#edit_dialog").length == 0) {
				$("body").append(
						'<div style="display:none" id="edit_dialog"></div>');
			} else {
				$("#edit_dialog").empty();
			}
			var dialogId = "edit_dialog";
			var $dialog = $('#' + dialogId);
			var dialogTitle = $('#editDialogTitle').text();
			var dialogContent = msg;
			$dialog.html(dialogContent);
			$dialog.dialog({
				// 默认不打开弹出框
				autoOpen : false,
				// 弹出框宽度
				width : 600,
				// 弹出框高度
				height : 300,
				// 弹出框标题
				title : dialogTitle,
				// 弹出框索引
				zIndex : 1,
				modal : true,
				resizable : false,
				// 弹出框按钮
				buttons : [
						{
							text : $("#dialogConfirm").text(),// 确认按钮
							click : function() {
								// 点击确认后执行更新
								BINOLCTTPL03.update(variableCode);
							}
						}, {
							text : $("#dialogCancel").text(),// 取消按钮
							click : function() {
								$dialog.dialog("close");
							}
						} ],
				// 关闭按钮
				close : function() {
					closeCherryDialog(dialogId);
				}
			});
			$dialog.dialog("open");
			$('#basicVariable').val($('#basicVariableTemp').val());
		}
		var url = $("#editUrl").attr("href");
		var param = "variableCode=" + variableCode + "&type=" + type + "&templateUse=" + $("#templateUse").val();
		cherryAjaxRequest({
			url : url,
			param : param,
			callback : callback
		});
	}
};

var BINOLCTTPL03 = new BINOLCTTPL03_GLOBAL();

$(document).ready(function() {
	BINOLCTTPL03.search();
});