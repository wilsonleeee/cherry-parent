
$(function(){

	plscf02_searchAudit();
});

// 查询审核审批配置一览
function plscf02_searchAudit() {
	var url = $("#auditListUrl").attr("href");
	var params= $("#searchAuditForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#auditSection").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dateTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 0, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [//{ "sName": "checkbox", "sWidth": "1%", "bSortable": false },
			                { "sName": "BussinessTypeCode", "sWidth": "14%" },
			                { "sName": "InitiatorType", "sWidth": "20%", "bSortable": false },
							{ "sName": "InitiatorID", "sWidth": "15%", "bSortable": false },
							{ "sName": "AuditorType", "sWidth": "20%", "bSortable": false },
							{ "sName": "AuditorID", "sWidth": "15%", "bSortable": false },
							{ "sName": "Operate", "sWidth": "16%", "bSortable": false,"sClass": "center" }],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 根据品牌查询业务类型List
function plscf02_searchBuType(url,object,defaultSelect) {
	
	var $select = $('#bussinessTypeCode');
	$select.empty();
	$select.append('<option value="">'+defaultSelect+'</option>');
	var param = $(object).serialize();
	var callback = function(msg) {
		if(msg) {
			var json = eval('('+msg+')');
			for(var i in json) {
				$select.append('<option value="'+json[i].configValue+'">'+escapeHTMLHandle(json[i].commentsChinese)+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// 添加审核审批配置画面初期表示
function plscf02_addAuditInit(url) {
	var param = $('#csrftoken').serialize();
	url = url + '?' + param;
	popup(url);
}

// 删除审核审批配置确认画面
function plscf02_deleteAuditInit(object) {

	var param = $(object).parent().find(':input').serialize();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: $("#deleteAuditText").html(),
		width: 	500,
		height: 300,
		title: 	$("#deleteAuditTitle").text(),
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){plscf02_deleteAudit($(object).attr("href"), param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	
}

// 删除审核审批配置处理
function plscf02_deleteAudit(url,param) {
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogClose").text(),
				    click: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
				}]
			});
		} else {
			removeDialog("#dialogInit");
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}