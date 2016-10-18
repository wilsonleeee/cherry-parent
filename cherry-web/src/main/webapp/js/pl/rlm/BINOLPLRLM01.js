
$(function(){

	searchRole();
});

//角色查询
function searchRole(){
	var url = $("#roleListUrl").attr("href");
	$('#searchRoleForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var params= $("#searchRoleForm").serialize();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	 
	// 显示结果一览
	$("#roleSection").removeClass("hide");
	// 表格设置
	var tableSetting = {
			 // 表格ID
			 tableId : '#dateTable',
			 // 数据URL
			 url : url,
			 // 表格默认排序
			 aaSorting : [],
			 // 表格列属性设置
			 aoColumns : [{ "sName": "no", "sWidth": "1%", "bSortable": false },
			                { "sName": "RoleName", "sWidth": "15%", "bSortable": false },
							{ "sName": "Decription", "sWidth": "29%", "bSortable": false },
							{ "sName": "RoleKind", "sWidth": "15%", "bSortable": false },
							{ "sName": "BrandNameChinese", "sWidth": "10%", "bSortable": false },
							{ "sName": "Operate", "sWidth": "25%", "bSortable": false,"sClass": "center" }],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

// 根据角色分类检索角色一览
function searchRoleByKind(object,roleKind) {
	$("#searchRoleForm :input[name=roleKind]").val(roleKind);
	$(object).siblings("a").removeClass("on");
	$(object).addClass("on");
	searchRole();
}

// 保存角色处理
function saveRole(url) {
	if($("#roleForm").length == 0)
		return;
	if(!$("#roleForm").valid())
		return;
	$('#roleForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var param = $("#roleForm").serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
		} else {
			$("#dialogInit").html(msg);
			// 保存成功的场合
			if($("#dialogInit").find("#successDiv").length != 0) {
				$("#dialogInit").dialog("destroy");
				var dialogSetting = {
					dialogInit: "#dialogInit",	
					text: msg,
					width: 	500,
					height: 300,
					title: 	$("#saveSuccessTitle").text(),
					confirm: $("#dialogClose").text(),
					cancel: $("#roleAuthorizeTitle").text(),
					confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();},
					cancelEvent: function(){
						var roleName = $("#successDiv").find(':input[name=roleName]').val();
						var roleAuthorityUrl = $("#roleAuthorityInitUrl").attr("href");
						var param = $("#successDiv").find(':input[name=roleId]').serialize();
						param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
						$("#dialogInit").dialog( "destroy" );
						var dialogSetting = {
							dialogInit: "#dialogInit",		
							width: 	800,
							height: 600,
							title: 	$("#roleAuthorizeTitle").text()+':'+roleName,
							confirm: $("#dialogConfirm").text(),
							cancel: $("#dialogCancel").text(),
							confirmEvent: function(){saveRoleAuthority($("#roleAuthorizeUrl").attr("href"));},
							cancelEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();},
							closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
						};
						openDialog(dialogSetting);
						var callback = function(msg) {
							$("#dialogInit").html(msg);
						};
						cherryAjaxRequest({
							url: roleAuthorityUrl,
							param: param,
							callback: callback,
							formId: '#searchRoleForm'
						});
					},
					closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
				};
				openDialog(dialogSetting);
			} 
			// 保存失败的场合
			else if($("#dialogInit").find("#errorMessageDiv").length != 0){
				$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
				$("#dialogInit").dialog( "option", {
					buttons: [{
						text: $("#dialogClose").text(),
					    click: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
					}],
					closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
				});
			} 
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#searchRoleForm'
	});
}

// 保存角色授权处理
function saveRoleAuthority(url) {
	if($("#resourceForm").length == 0)
		return;
	var param = "csrftoken=" + getTokenVal() + "&" + $("#resourceForm").serialize();
//	// 把被选中的checkbox拼成参数处理
//	$("#dialogInit :button[name=subSysId]").each(function(){
//		if($(this).attr("class").indexOf("checkbox_true_full") != -1) {
//			param += this.name + "=" + this.value + "&";
//		} else {
//			$(this).parent().next(".group-content").find(":button[name=moduleId]").each(function(){
//				if($(this).attr("class").indexOf("checkbox_true_full") != -1) {
//					param += this.name + "=" + this.value + "&";
//				} else {
//					$(this).parent().next(".group-content").find(":button[name=functionId]").each(function(){
//						if($(this).attr("class").indexOf("checkbox_true_full") != -1) {
//							param += this.name + "=" + this.value + "&";
//						} else {
//							$(this).parent().parent().nextAll().find(":button[name=pageId]").each(function(){
//								if($(this).attr("class").indexOf("checkbox_true_full") != -1) {
//									param += this.name + "=" + this.value + "&";
//								} else {
//									$(this).parent().parent().next(".after").find(":button[name=controlId]").each(function(){
//										if($(this).attr("class").indexOf("checkbox_true_full") != -1) {
//											param += this.name + "=" + this.value + "&";
//										}
//									});
//								}
//							});
//						}
//					});
//				}
//			});
//		}
//	});
//	if(param != "") {
//		param = param.substring(0, param.length-1);
//	}
//	var hideParam = $("#resourceForm").serialize();
//	if(hideParam != "") {
//		param += '&' + hideParam;
//	}
	
	var callback = function(msg) {
		$("#dialogInit").dialog( "destroy" );
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text: msg,
			width: 	500,
			height: 300,
			title: 	$("#saveSuccessTitle").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();},
			closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
		};
		openDialog(dialogSetting);
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#searchRoleForm'
	});
}

// 添加角色画面初期化
function addRoleInit(object) {
	var dialogSetting = {
		dialogInit: "#dialogInit",
		width: 	500,
		height: 330,
		title: 	$("#addRoleTitle").text(),
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){saveRole($("#addRoleUrl").attr("href"));},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	var addUrl = $(object).attr("href") + "?csrftoken=" + getTokenVal();
	var callback = function(msg) {
		$("#dialogInit").html(msg);
	};
	cherryAjaxRequest({
		url: addUrl,
		param: null,
		callback: callback,
		formId: '#searchRoleForm'
	});
}

// 更新角色画面初期化
function updateRoleInit(object) {
	var roleName = $(object).parent().parent().find('td:first-child').next().text();
	var dialogSetting = {
		dialogInit: "#dialogInit",	
		width: 	500,
		height: 300,
		title: 	$("#updateRoleTitle").text()+':'+roleName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){saveRole($("#updateRoleUrl").attr("href"));},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	var updateUrl = $(object).attr("href");
	var param = $(object).parent().find(':input[name=roleId]').serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		$("#dialogInit").html(msg);
	};
	cherryAjaxRequest({
		url: updateUrl,
		param: param,
		callback: callback,
		formId: '#searchRoleForm'
	});
}

// 角色授权画面初期化
function roleAuthorityInit(object) {
	var roleName = $(object).parent().parent().find('td:first-child').next().text();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		width: 	800,
		height: 600,
		title: 	$("#roleAuthorizeTitle").text()+':'+roleName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){saveRoleAuthority($("#roleAuthorizeUrl").attr("href"));},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	var roleAuthorityUrl = $(object).attr("href");
	var param = $(object).parent().find(':input[name=roleId]').serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		$("#dialogInit").html(msg);
	};
	cherryAjaxRequest({
		url: roleAuthorityUrl,
		param: param,
		callback: callback,
		formId: '#searchRoleForm'
	});
}

// 删除角色处理
function deleteRole(url, param) {
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
		callback: callback,
		formId: '#searchRoleForm'
	});
}

// 删除角色确认画面
function deleteRoleInit(object) {
	var roleName = $(object).parent().parent().find('td:first-child').next().text();
	var param = $(object).parent().find(':input').serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: $("#deleteRoleText").html(),
		width: 	500,
		height: 300,
		title: 	$("#deleteRoleTitle").text()+':'+roleName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){deleteRole($(object).attr("href"), param);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}
