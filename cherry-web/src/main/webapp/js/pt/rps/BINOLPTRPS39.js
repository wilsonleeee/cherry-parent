var BINOLPTRPS39 = function() {};

BINOLPTRPS39.prototype = {
		
	/**
	 * 清理执行结果信息
	 */
	"clearActionHtml" : function() {
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
	},
		
	"changeRemOrDel":function(object, url) {
		BINOLPTRPS39.clearActionHtml();
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		$(window).unbind('resize');
	/*	if($(object).attr('class').indexOf('display-tree') != -1) {
			if($(object).attr('class').indexOf('display-tree-on') != -1) {
				return false;
			} else {
				$(object).siblings().removeClass('display-table-on');
				$(object).addClass('display-tree-on');
			}
		} else {
			if($(object).attr('class').indexOf('display-table-on') != -1) {
				return false;
			} else {
				$(object).siblings().removeClass('display-tree-on');
				$(object).addClass('display-table-on');
			}
		}*/
		$("#slideButton").find('a').find('.button-text').removeClass('reminder_selected');
		$(object).find(".button-text").addClass('reminder_selected');
		var callback = function(msg) {
			$("#reminderInitId").html(msg);
		};
		var params = "cargoType=" + $("#cargoType").val();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: callback
		});
	},
	"search":function(flag) {
		BINOLPTRPS39.clearActionHtml();
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		var reminderType = flag;
		var url = $("#searchUrl").attr("href");
		var params = $("#mainForm").serialize();
		params += "&csrftoken=" + $("#csrftoken").val();
		params += "&" + getRangeParams();
		url += "?" + params; 
		$("#section").show();
		//反向催单
		if("1" == reminderType) {
			var tableSetting00 = {
					index : 1,
					tableId:'#dataTable01',
					url:url,
					aaSorting:[[5, "desc"]],
					aoColumns:[
					           {"sName":"BIN_ReminderNo","sWidth":"16%"},
					           {"sName":"DeliverNo","bVisible":false,"sWidth":"16%"},
					           {"sName":"CounterCode","sWidth":"20%"},
					           {"sName":"BIN_CargoType","bVisible":false,"sWidth":"4%","sClass":"center"},
					           {"sName":"Receive_Date","sWidth":"8%","sClass":"alignRight"},
					           {"sName":"delayDate","bVisible":false,"sWidth":"4%","sClass":"alignRight"},
					           {"sName":"Receive_Quantity","sWidth":"6%","sClass":"alignRight"},
					           {"sName":"EmployeeCode","sWidth":"8%","sClass":"center"},
					           {"sName":"Email","bVisible":false,"sWidth":"12%","bSortable":false},
					           {"sName":"MobilePhone","bVisible":false,"sWidth":"10%","bSortable":false},
					           {"sName":"ExpressBillCode","bVisible":false,"sWidth":"10%"},//货运单号
					           {"sName":"Fst_ReminderTime","bVisible":false,"sWidth":"8%","bSortable":false},//第一次催单时间
					           {"sName":"Snd_ReminderTime","bVisible":false,"sWidth":"8%","bSortable":false},//第二次催单时间
					           {"sName":"Trade_Date","bVisible":false,"sWidth":"8%","sClass":"alignRight"},//制单日期
					           {"sName":"Comment","bVisible":false,"sWidth":"20%","bSortable":false},
					           {"sName":"Reminder_Count","sWidth":"8%","sClass":"center"},//催单次数
					           {"sName":"Status","sWidth":"8%","sClass":"center"}
					           ],
					           //横向滚动条出现的临界宽度
					           sScrollX : "100%",
					           fnDrawCallback:function() {}
			}
			getTable(tableSetting00);
		}
		//收货延迟
		else if("0" == reminderType) {
			var tableSetting01 = {
					 // datatable 对象索引
					 index : 2,
					 // 表格ID
					 tableId : '#dataTable00',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 7, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [	
									{ "sName": "DeliverNo","sWidth":"20%"},
									{ "sName": "DeliverNoIF","bVisible":false,"sWidth":"20%"},
									{ "sName": "RelevanceNo","bVisible":false,"sWidth":"20%"},
									{ "sName": "BIN_OrganizationID","sWidth":"10%","bSortable":false},
									{ "sName": "BIN_OrganizationIDReceive","sWidth":"15%"},
									{ "sName": "TotalQuantity","sWidth":"8%","sClass":"alignRight"},
									{ "sName": "TotalAmount","sWidth":"8%","sClass":"alignRight"},
									{ "sName": "DeliverDate","sWidth":"8%","sClass":"alignRight"},
									{ "sName": "DelayDate","sWidth":"8%","sClass":"center"},
									{ "sName": "VerifiedFlag","sWidth":"10%","sClass":"center"},
									{ "sName": "TradeStatus","sWidth":"10%","sClass":"center"},
									{ "sName": "BIN_EmployeeID","bVisible":false,"sWidth":"8%"},
									{ "sName": "BIN_EmployeeIDAudit","bVisible":false,"sWidth":"8%"}
									],			
									
					//横向滚动条出现的临界宽度
					sScrollX : "100%",
			 		fnDrawCallback:function() {}
			 };
			getTable(tableSetting01);
		}
	},
	
	//Excel导出
	"exportExcel":function(url) {
		BINOLPTRPS39.clearActionHtml();
		//无数据不导出
		if($(".dataTable_empty:visible").length == 0) {
			if(!$('#mainForm').valid()) {
				return false;
			};
			var params = $("#mainForm").serialize();
			params += "&csrftoken=" + $("#csrftoken").val();
			params += "&" + getRangeParams();
			url += "?" + params;
			window.open(url, "_self");
		}
	},
	
	//催单
	"remind":function(count, reminderId, url, cargoType) {
		BINOLPTRPS39.clearActionHtml();
		url += "?" + "reminderId=" + reminderId + "&reminderCount=" + count + "&cargoType=" + cargoType; 
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text:  $("#reminderText").html(),
			width: 	280,
			height: 180,
			title: $("#reminderTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){
				cherryAjaxRequest({
					url:url,
					callback:function(){
						if(oTableArr[1]){
							oTableArr[1].fnDraw();
						}
						removeDialog("#dialogInit");
					}
				});
				
			},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	//消单
	"handleInit":function(url, reminderId, cargoType, remInOrganizationId) {
		var params = "reminderId=" + reminderId + "&cargoType=" + cargoType + "&remInOrganizationId=" + remInOrganizationId; 
		var dialogSetting = {
				dialogInit: "#dialogInit",
				text:$("#deliverTips").html(),
				width: 	280,
				height: 180,
				title: $("#pushTitle").text(),
				confirm: $("#existsTitle").text(),
				cancel: $("#notExistsTitle").text(),
				confirmEvent: function(){
					removeDialog("#dialogInit");
					BINOLPTRPS39.deliverExists(url, params);
				},
				cancelEvent: function(){
					removeDialog("#dialogInit");
					openWin(url);
				}
			};
		openDialog(dialogSetting);
	},
	"deliverExists":function(url, thisObj) {
		var that = this;
		that.clearActionHtml();
		var dialogSetting = {
				dialogInit: "#dialogInit",
				width: 	400,
				height: 'auto',
				title: 	$("#pushTitle").text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					that.handle(thisObj);
					if(oTableArr[1] != null){
						oTableArr[1].fnDraw();
					}
	    		},
				cancelEvent: function(){removeDialog("#dialogInit");}
			};
		openDialog(dialogSetting);
		var addDeliverInitUrl = $("#addDeliverInitUrl").attr("href");
		var callback = function(msg) {
			$("#dialogInit").html(msg);
		};
		cherryAjaxRequest({
			url:addDeliverInitUrl,
			param:null,
			callback:callback,
			formId:'#mainForm'
		});
	},
	"handle":function(thisObj) {
		var params = thisObj;
		if(!$('#createForm').valid()) {
			return false;
		}
		$('#createForm :input').each(function() {
			$(this).val($.trim(this.value));
			
		})
		params += "&" + $('#createForm').serialize();
		var callback = function(msg) {
			if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
				resultHandle(data);
			} else {
				$("#dialogInit").html(msg);
				// 保存成功的场合
				if($("#dialogInit").find("#successDiv").length != 0) {
					$("#dialogInit").dialog("destroy");
	                var dialogSetting = {
						dialogInit: "#dialogInit",
						text: msg,
						width: 280,
						height: 180,
						title: $("#operateResultTitle").text(),
						confirm: $("#dialogClose").text(),
						confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();},
                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
					};
					openDialog(dialogSetting);
				} else if($("#dialogInit").find("#errorMessageDiv").length != 0) {
					 $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
		                $("#dialogInit").dialog( "option", {
		                    buttons: [{
		                        text: $("#dialogClose").text(),
		                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
		                    }],
							closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
		                });
				}
			}
		};
		var url = $("#handleDeliverUrl").attr("href");
		cherryAjaxRequest({
			url: url,
			param: params,
			formId: '#createForm',
			callback: callback
		});	
	}
};

var BINOLPTRPS39 = new BINOLPTRPS39();

$(function(){
	BINOLPTRPS39.clearActionHtml();
	$('.tabs').tabs();
});