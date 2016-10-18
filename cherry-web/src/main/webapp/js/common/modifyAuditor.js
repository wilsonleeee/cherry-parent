var ModifyAuditor = function () {};

ModifyAuditor.prototype = {
		
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
				close: function() {modifyAuditor.removeShowDialog(dialogSetting.dialogInit);}		
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
		$('#hideDivModifyActor').append('<div style="display:none" id="'+dialogDivId+'"></div>');
	},
		
	"showDialog":function(){	
		var dialogSetting = {
			dialogInit: "#dialogModifyAuditorInit",
			width: 	700,
			height: 400,
			title: $("#editTitile").text(),
			confirm: $("#ok").text(),
			cancel: $("#cancle").text(),
			confirmEvent: function(){modifyAuditor.save();},
			cancelEvent: function(){modifyAuditor.removeShowDialog("#dialogModifyAuditorInit");}
		};
		modifyAuditor.openDialog(dialogSetting);
		var showUrl = $("#showDialogUrl").attr("href");
		var param = "workFlowId="+$("#entryID").val();
		var callback = function(msg) {
			$("#dialogModifyAuditorInit").html(msg);
		};
		cherryAjaxRequest({
			url: showUrl,
			param: param,
			callback: callback,
			formId: '#mainForm',
			reloadFlg:true
		});
	},
		
	//根据身份类型查询身份信息List
	"searchCodeByType":function() {
		$("#auditorID").find("option:not(:first)").remove();
		var auditorType = $("#auditorType").val();
		var auditorIdDefaultHtml = '<select id="auditorID" name="auditorID" onchange=""><option value="">'+popModifyAuditor_js_i18n.selectPlease+'</option></select>';
		var ruleType = $("#ruleType").val();
		if(auditorType == ""){
			$("#auditorIdSpan").html(auditorIdDefaultHtml);
			return;
		}else if(auditorType == "2" && ruleType == "3"){
			$("#trPrivilegeRelationship").removeClass("hide");
		}else{
			$("#trPrivilegeRelationship").addClass("hide");
		}
		var url = $("#searchCodeByTypeUrl").attr("href");
		var param = "auditorType="+$("#auditorType").val();
		var callback = function(msg) {
			if(msg && msg != 'null') {
				var json = eval('('+msg+')');
				// 参与 者身份户类型为岗位是才以下拉框的形式显示参与者
				if(auditorType && auditorType == '2') {
					$("#auditorIdSpan").html(auditorIdDefaultHtml);
					for(var i in json) {
						$("#auditorID").append('<option value="'+json[i].code+'">'+escapeHTMLHandle(json[i].name)+'</option>');
					}
				} else if(auditorType == '1' || auditorType == '3') {
					// 清空下拉框改用按钮，点击后弹出选择对话框
					$("#auditorIdSpan").html("");
					$("#auditorIdSpan").append('<s:hidden id="auditorID"></s:hidden><span id="auditorName"></span><a id="selectDepartButton" class="add right" style="margin-left:50px" onclick="modifyAuditor.popEmpOrDepart('+auditorType+');return false;"><span class="ui-icon icon-search"></span><span class="button-text">'+popModifyAuditor_js_i18n.selectBtn+'</span></a>');
				}
			} 
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	
	/**
	 * 根据参与者身份户类型弹出选择框
	 */
	"popEmpOrDepart" : function(auditorType) {
		if(auditorType == '1') {
			var url = $("#searchUserInitUrl").attr("href");
			var callback = function(tableId) {
				// 弹出框选中的项
				var $checkedRadio = $("#"+tableId).find(":input[checked]");
				if($checkedRadio.length > 0) {
					$("#auditorID").val($checkedRadio.val());
					var code = $checkedRadio.parent().next().text();
					var name = $checkedRadio.parent().next().next().text();
					var html = name != '' ? '(' + code + ')' + name : code;
					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="modifyAuditor.delPopSelectHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$("#auditorName").html(html);
				}
			}
			// 当前选中的人员
			var value = $("#auditorID").val();
			popDataTableOfUserList(url, null, value, callback);
		} else if(auditorType == '3'){
			var url = $("#searchDepartInitUrl").attr("href");
			var callback = function(tableId) {
				// 弹出框选中的项
				var $checkedRadio = $("#"+tableId).find(":input[checked]");
				if($checkedRadio.length > 0) {
					$("#auditorID").val($checkedRadio.val());
					var code = $checkedRadio.parent().next().text();
					var name = $checkedRadio.parent().next().next().text();
					var html = name != '' ? '(' + code + ')' + name : code;
					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="modifyAuditor.delPopSelectHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$("#auditorName").html(html);
				}
			}
			// 当前选中的人员
			var value = $("#auditorID").val();
			popDataTableOfAllDepartList(url, null, value, callback);
			
		}
	},
	
	/**
	 * 用于清除
	 */
	"delPopSelectHtml": function(object) {
		$("#auditorID").val("");
		$("#auditorName").empty();
	},
	
	/**
	 * 保存
	 * @return
	 */
	"save":function(){
		if(!$("#modifyAudiorForm").valid())
			return;
		if($("#auditorID").val()==""){
			return;
		}
		var url =  $("#saveAuditorUrl").attr("href");
		var refreshUrl =  $("#refreshAuditorUrl").attr("href");
		var param = "workFlowId="+$("#entryID").val()+"&auditorType="+$("#auditorType").val()+"&auditorID="+$("#auditorID").val()+"&bussinessType="+$("#cmBusinessType").val();
		param += "&ruleType="+$("#ruleType").val()+"&privilegeFlag="+$("#privilegeFlag").val();
		var callback = function(msg) {
			if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
			} else {
				$("#dialogModifyAuditorInit").html(msg);
				// 保存成功的场合
				if($("#dialogModifyAuditorInit").find("#successDiv").length != 0) {
					$("#dialogModifyAuditorInit").dialog("destroy");
	                var dialogSetting = {
						dialogInit: "#dialogModifyAuditorInit",
						text: msg,
						width: 500,
						height: 300,
						title: $("#successTitile").text(),
						confirm: $("#close").text(),
						confirmEvent: function(){
	                		modifyAuditor.removeShowDialog("#dialogModifyAuditorInit");
	                		cherryAjaxRequest({
	                			url: refreshUrl,
	                			param : "WorkFlowID="+$("#entryID").val(),
	                			callback: function (msg){
	                				if($("#workFlowDetail").find("#audtiorTR").length > 0){
	                					$("#audtiorTR").remove();
	                				}
	                				$("#workFlowDetail").append(msg);
	                				detailWork_showEmployee("|"+$("#spanEmpList").text());
	                				//刷新tabs-1的内容
	                				var refreshOSBillUrl = $("#refreshOSBillUrl").attr("href");
	                				if(refreshOSBillUrl != ""){
	                					refreshOSBillUrl = "/Cherry/"+refreshOSBillUrl;
		    	                		cherryAjaxRequest({
		    	                			url:refreshOSBillUrl,
		    	                			param:"",
		    	                			callback:function (msg){
		    	                				var htmlBreadcrumb = $(msg).find("span.breadcrumb").html();
		    	                				if(htmlBreadcrumb != null && htmlBreadcrumb != ""){
		    	                					$("span.breadcrumb").html(htmlBreadcrumb);
		    	                				}
		    	                				var htmlTabs1 = $(msg).find("#tabs-1").html();
		    	                				if(htmlTabs1 != null && htmlTabs1 != ""){
		    	                					$("#tabs-1").html(htmlTabs1);
		    	                				}
		    	                				$("#csrftoken").val(window.opener.document.getElementById("csrftoken").value);
		    	                			}
		    	                		});	
	                				}
	                				refreshGadgetTask();
	                			}
	                		});
	                	},
                        closeEvent: function(){
	                		modifyAuditor.removeShowDialog("#dialogModifyAuditorInit");
	                		cherryAjaxRequest({
	                			url: refreshUrl,
	                			param : "WorkFlowID="+$("#entryID").val(),
	                			callback: function (msg){
	                				if($("#workFlowDetail").find("#audtiorTR").length > 0){
	                					$("#audtiorTR").remove();
	                				}
	                				$("#workFlowDetail").append(msg);
                					detailWork_showEmployee("|"+$("#spanEmpList").text());
	                			}
	                		});
	                	}
					};
	                modifyAuditor.openDialog(dialogSetting);
				}// 保存失败的场合
				else if($("#dialogModifyAuditorInit").find("#errorMessageDiv").length != 0){
	                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
	                $("#dialogModifyAuditorInit").dialog( "option", {
	                    buttons: [{
	                        text: $("#close").text(),
	                        click: function(){modifyAuditor.removeShowDialog("#dialogModifyAuditorInit");}
	                    }],
						closeEvent: function(){modifyAuditor.removeShowDialog("#dialogModifyAuditorInit");}
	                });
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
};

var modifyAuditor = new ModifyAuditor();

$(document).ready(function(){

});