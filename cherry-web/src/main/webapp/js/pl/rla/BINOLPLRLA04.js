$(document).ready(function() {
	$('#orgId').combobox({inputId: '#depart_text', btnId: '#depart_btn'});
	$('#positionId').combobox({inputId: '#position_text', btnId: '#position_btn'});
	setInputVal('#depart_text', $("#select_default").text());
	setInputVal('#position_text', $("#select_default").text());
	searchUser();
});


//用户查询
function searchUser(){
	var url = $("#userListUrl").attr("href");
	var params= $("#searchUserForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	// 显示结果一览
	$("#userSection").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dateTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [[1, "asc"]],
			 // 表格列属性设置
			 aoColumns : [{ "sName": "no", "sWidth": "1%", "bSortable": false },
			                { "sName": "LonginName", "sWidth": "18%" },
			                { "sName": "EmployeeName", "sWidth": "18%"},
			                { "sName": "BrandNameChinese", "sWidth": "18%", "bSortable": false },
							{ "sName": "DepartName", "sWidth": "18%", "bSortable": false },
							{ "sName": "PositionName", "sWidth": "18%", "bSortable": false },
							{ "sName": "Operate", "sWidth": "9%", "bSortable": false }],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 用户角色分配初期表示处理
function userRoleAssignInit(object) {
	var roleName = $(object).parent().parent().find('td:first-child').next().next().text();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		width: 	800,
		height: 600,
		title: 	$("#userRoleAssignTitle").text()+":"+roleName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){saveRoleAssign(true);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	
	var url = $(object).attr("href");
	var param = "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		if(msg.indexOf("<table") > -1){
			openDialog(dialogSetting);
			$("#dialogInit").html(msg);
		}else{
			alert($("#noUserRole").html());
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#searchUserForm'
	});
}

// 根据品牌取得部门
function getOrgByBrandInfo(obj,url,i18nText) {
	url = url+'?'+$("#csrftoken").serialize() + '&' + $(obj).serialize();
	// 部门、岗位初始化
	$('#depart_text').val(i18nText);
	$('#position_text').val(i18nText);
	$("#orgId option").remove();
    $("#orgId").append("<option  value=''>"+i18nText+"</option>");
	$("#positionId option").remove();
    $("#positionId").append("<option  value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "departId", "departName", $('#orgId'));
}

// 根据部门取得岗位 
function getPosition(obj,url,i18nText){
	url = url+'?'+$("#csrftoken").serialize() + '&' + $(obj).serialize();
	// 岗位初始化
	$('#position_text').val(i18nText);
	$("#positionId option").remove();
    $("#positionId").append("<option  value=''>"+i18nText+"</option>");
    // AJAX请求
    doAjax(url, "positionId", "positionName", "#positionId");
}