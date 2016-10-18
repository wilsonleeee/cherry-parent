function BINOLMBMBM07() {};

BINOLMBMBM07.prototype = {
	"searchCurrentCampaign" : function() {
		var url = $("#searchCampaignInfoUrl").attr("href");
		var parentToken = getTokenVal();
		$("#currentCampaignForm").find("#parentCsrftoken").val(parentToken);
		var params= $("#currentCampaignForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		$("#campaignDataTableDiv").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#campaignDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 3, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "actGroupName", "sWidth": "20%" },
				                { "sName": "actName", "sWidth": "20%" },
				                { "sName": "departName", "sWidth": "20%" },
				                { "sName": "participateTime", "sWidth": "20%" },
								{ "sName": "state", "sWidth": "20%" }],
				// 横向滚动条出现的临界宽度
				//sScrollX : "100%",
				//bAutoWidth : true,
				fnDrawCallback : function() {
					$("#campaignDataTable").find('tr').click(function() {
						//binolmbmbm10.searchDetail(this);
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    "searchCampaignHistory" : function() {
    	var $campaignHistoryForm = $('#campaignHistoryForm');
    	if(!$campaignHistoryForm.valid()) {
			return false;
		}
		var url = $("#searchCampaignHistoryUrl").attr("href");
		url += "?" + getSerializeToken();
		url += "&" + $campaignHistoryForm.serialize();
		$("#campaignDataTableDiv").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#campaignDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 7, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "actName", "sWidth": "10%", "bSortable": false },
				                { "sName": "mainCode", "sWidth": "10%" },
				                { "sName": "actGroupName", "sWidth": "10%", "bSortable": false },
				                { "sName": "actType", "sWidth": "10%" },
				                { "sName": "tradeNoIF", "sWidth": "10%" },
				                { "sName": "batchNo", "sWidth": "10%" },
								{ "sName": "departName", "sWidth": "10%", "bSortable": false },
								{ "sName": "participateTime", "sWidth": "10%" },
								{ "sName": "informType", "sWidth": "10%", "bSortable": false },
								{ "sName": "state", "sWidth": "10%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				bAutoWidth : true,
				fnDrawCallback : function() {
					$("#campaignDataTable").find('tr').click(function() {
						//binolmbmbm10.searchDetail(this);
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
	"orderCampaignInit": function(url) {
		if($("#orderCampaignInitDialog").length == 0) {
    		$("body").append('<div style="display:none" id="orderCampaignInitDialog"></div>');
    	} else {
    		$("#orderCampaignInitDialog").empty();
    	}
    	var dialogSetting = {
    		dialogInit: "#orderCampaignInitDialog",
    		text: this.orderCampaignDiv,
    		width: 	400,
    		height: 250,
    		title: 	$("#orderCampaignDialogTitle").text(),
    		confirm: $("#dialogConfirm").text(),
    		cancel: $("#dialogCancel").text(),
    		confirmEvent: function(){binolmbmbm07.orderCampaign(url);},
    		cancelEvent: function(){removeDialog("#orderCampaignInitDialog");}
    	};
    	openDialog(dialogSetting);
	},
	"orderCampaign": function(url) {
		var params = $("#orderCampaignInitDialog").find("#orderCampaignForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg) {
				if(msg.indexOf('class="actionSuccess"') > -1) {
					var refresh = function() {
						cherryAjaxRequest({
							url: $("#currentCampaignInitUrl").attr("href"),
							param: $("#memDetailForm").serialize(),
							callback: function(msg) {
								$("#memDetailDiv").html(msg);
							}
						});
					};
					window.setTimeout(refresh,1000);
				}
			}
		});
		removeDialog("#orderCampaignInitDialog");
	},
    "popOrderCntDialog": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#orderCampaignInitDialog").find("#organizationId").val($checkedRadio.val());
				$("#orderCampaignInitDialog").find("#orderCntCode").val($checkedRadio.next().val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm07.delOrgHtml();"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#orderCampaignInitDialog").find("#orderCntDiv").html(html);
			}
		}
		var value = $("#orderCampaignInitDialog").find("#organizationId").val();
		var param = "validFlag=1&privilegeFlg=1";
		popDataTableOfCounterList(url, param, value, callback);
	},
	"delOrgHtml": function() {
		$("#orderCampaignInitDialog").find("#organizationId").val("");
		$("#orderCampaignInitDialog").find("#orderCntCode").val("");
		$("#orderCampaignInitDialog").find("#orderCntDiv").empty();
	},
	"cancelCampaignInit": function(url) {
		if($("#cancelCampaignInitDialog").length == 0) {
    		$("body").append('<div style="display:none" id="cancelCampaignInitDialog"></div>');
    	} else {
    		$("#cancelCampaignInitDialog").empty();
    	}
    	var dialogSetting = {
    		dialogInit: "#cancelCampaignInitDialog",
    		text: '<p class="message" style="margin: 45px 0">'+$("#cancelCampaignDialogText").text()+'</p>',
    		width: 	400,
    		height: 200,
    		title: 	$("#cancelCampaignDialogTitle").text(),
    		confirm: $("#dialogConfirm").text(),
    		cancel: $("#dialogCancel").text(),
    		confirmEvent: function(){
    			cherryAjaxRequest({
    				url: url,
    				param: null,
    				callback: function(msg) {
    					if(msg.indexOf('class="actionSuccess"') > -1) {
    						var refresh = function() {
    							cherryAjaxRequest({
            						url: $("#searchCampaignOrderUrl").attr("href"),
            						param: $("#memDetailForm").serialize(),
            						callback: function(msg) {
            							$("#memDetailDiv").html(msg);
            						}
            					});
    						};
    						window.setTimeout(refresh,1000);
    					}
    				}
    			});
    			removeDialog("#cancelCampaignInitDialog");
    		},
    		cancelEvent: function(){removeDialog("#cancelCampaignInitDialog");}
    	};
    	openDialog(dialogSetting);
	}
};

var binolmbmbm07 =  new BINOLMBMBM07();

$(function(){
	binolmbmbm07.orderCampaignDiv = $("#orderCampaignDiv").html();
	$("#orderCampaignDiv").remove();
});