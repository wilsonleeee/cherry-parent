
$(function(){

	plplt01_searchPlt();
});

// 查询权限类型一览
function plplt01_searchPlt() {
	var url = $("#pltListUrl").attr("href");
	var params= $("#searchPltForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#pltSection").removeClass("hide");
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
			                { "sName": "Category", "sWidth": "14%" },
			                { "sName": "DepartType", "sWidth": "14%" },
							{ "sName": "BIN_PositionCategoryID", "sWidth": "14%" },
							{ "sName": "BusinessType", "sWidth": "14%" },
							{ "sName": "OperationType", "sWidth": "8%", "bSortable": false },
							{ "sName": "PrivilegeType", "sWidth": "14%", "bSortable": false },
							{ "sName": "Exclusive", "sWidth": "7%", "bSortable": false },
							{ "sName": "Operate", "sWidth": "15%", "bSortable": false,"sClass": "center" }],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 根据数据过滤类型过滤权限类型一览
function plplt01_pltFilter(filterType,thisObj) {
	
	$(thisObj).siblings().removeClass('ui-tabs-selected');
	$(thisObj).addClass('ui-tabs-selected');

	// 数据过滤
	oTableArr[0].fnFilter(filterType);
}

// 添加权限类型画面初期表示
function plplt01_addPltInit(url) {
	var param = $('#searchPltForm').find(':input[name=csrftoken]').serialize();
	url = url + '?' + param;
	popup(url);
}

// 删除权限类型确认画面
function plplt01_deletePltInit(object) {

	var param = $(object).parent().find(':input').serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: $("#deletePltText").html(),
		width: 	500,
		height: 300,
		title: 	$("#deletePltTitle").text(),
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){plplt01_deletePtl($(object).attr("href"), param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	
}

// 删除权限类型处理
function plplt01_deletePtl(url,param) {
	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
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
			if(refreshPrivilegeUrl) {
				cherryAjaxRequest({
					url: refreshPrivilegeUrl,
					param: null,
					reloadFlg : true,
					callback: function(msg) {
					}
				});
			}
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