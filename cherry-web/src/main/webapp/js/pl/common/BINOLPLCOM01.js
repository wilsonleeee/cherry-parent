
// 保存分配的角色处理
function saveRoleAssign(flag) {
	var url = $("#saveRoleAssignUrl").attr("href");
	var stack = [];
	$("#roleInfoList :input[id=roleId][checked]").each(function(){
		var $object = $(this).parent().parent();
		var query = '{"startDate":"' + $object.find(":input[name=startDate]").val() + 
					'","expireDate":"' + $object.find(":input[name=expireDate]").val() + 
					'","roleId":"' + $object.find(":input[name=roleId]").val();
		if(flag) {
			query += '","privilegeFlag":"' + $object.find(":input[name=privilegeFlag]").val() + '"}';
		} else {
			query += '"}';
		}
		stack.push(query);
	});
	var param = $("#hiddenParam :input").serialize();
	if(stack.toString() != "") {
		param += "&" + "roleAssign=[" + stack.toString() + "]";
	}
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var callback = function(msg){
		if(flag) {
			if(msg.indexOf('id="saveSuccessId"') > -1) {
				$("#dialogInit").dialog( "destroy" );
				var dialogSetting = {
					dialogInit: "#dialogInit",
					text: $("#saveSuccessText").html(),
					width: 	500,
					height: 300,
					title: 	$("#saveSuccessTitle").text(),
					confirm: $("#dialogClose").text(),
					confirmEvent: function(){removeDialog("#dialogInit");}
				};
				openDialog(dialogSetting);
			} else {
				$("#dialogInit").html(msg);
			}
		} else {
			$("#roleAssignInit").html(msg);
			if($("#saveSuccessId").length != 0) {
				$("#saveSuccessId").show();
			}
		}
	};
	if(flag) {
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			formId: '#searchUserForm'
		});
	} else {
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			formId: '#privilegeCherryForm'
		});
	}
	
}