
$(function(){

	bsdep06_searchOrg();
});


// 查询组织一览
function bsdep06_searchOrg() {
	var url = $("#orgListUrl").attr("href");
	$('#searchOrgForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var params= $("#searchOrgForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#OrgSection").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dataTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[ 1, "asc" ]],
			 // 表格列属性设置
			 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false },
			                { "sName": "OrgCode", "sWidth": "10%" },
			                { "sName": "OrgNameChinese", "sWidth": "20%" },
							{ "sName": "OrgNameForeign", "sWidth": "20%" },
							{ "sName": "OrgNameShort", "sWidth": "15%", "bVisible": false },
							{ "sName": "OrgNameForeignShort", "sWidth": "15%", "bVisible": false },
							{ "sName": "ValidFlag", "sWidth": "5%", "sClass":"center" },
							{ "sName": "Operate", "sWidth": "10%", "bSortable": false,"sClass": "center" }],
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 添加组织画面初期表示
function bsdep06_addOrgInit(url) {
	var param = $('#csrftoken').serialize();
	url = url + '?' + param;
	popup(url);
}

//// 删除组织确认画面
//function bsdep06_deleteOrgInit(object) {
//	var orgName = $(object).parent().parent().find('td:first-child').next().text();
//	var param = $(object).parent().find(':input').serialize();
//	var dialogSetting = {
//		dialogInit: "#dialogInit",
//		text: $("#deleteOrgText").html(),
//		width: 	500,
//		height: 300,
//		title: 	$("#deleteOrgTitle").text()+':'+orgName,
//		confirm: $("#dialogConfirm").text(),
//		cancel: $("#dialogCancel").text(),
//		confirmEvent: function(){bsdep06_deleteOrg($(object).attr("href"), param);},
//		cancelEvent: function(){removeDialog("#dialogInit");}
//	};
//	openDialog(dialogSetting);
//	
//}
//
//// 删除组织处理
//function bsdep06_deleteOrg(url,param) {
//	var callback = function(msg) {
//		$("#dialogInit").html(msg);
//		if($("#errorMessageDiv").length > 0) {
//			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
//			$("#dialogInit").dialog( "option", {
//				buttons: [{
//					text: $("#dialogClose").text(),
//				    click: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
//				}]
//			});
//		} else {
//			removeDialog("#dialogInit");
//			if(oTableArr[0] != null)oTableArr[0].fnDraw();
//		}
//	};
//	cherryAjaxRequest({
//		url: url,
//		param: param,
//		callback: callback
//	});
//}

//组织启用停用处理
function bsdep06_editValidFlag(flag, url) {
	
	var param = "";
	if($('#dataTable_Cloned :input[checked]').length == 0) {
		$("#errorMessage").html($("#errorMessageTemp").html());
		return false;
	}
	param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
	var callback = function() {
		if(oTableArr[0] != null)oTableArr[0].fnDraw();
	};
	bscom03_deleteConfirm(flag, url, param, callback);
}