var binOLPLCOM02_global = {};
//是否需要解锁
binOLPLCOM02_global.needUnlock = true;

var BINOLPLCOM02 = function() {
};
BINOLPLCOM02.prototype = {
	/*
	 * 显示导航条
	 */
	"showSteps" : function() {
		$li = $("#ol_steps").find("li");
		var onStep = $("#onStep").val() || "0";
		var steps = $li.length;
		if (steps > 0) {
			$li.each(function(index) {
				if (index +1 == parseInt(onStep) - 1) {
					// 前一步骤
					$(this).attr("class", "prev");
				} else if (index +1 == parseInt(onStep)) {
					// 当前步骤
					$(this).attr("class", "on");
				} else {
					$(this).attr("class", "");
				}
					
				if (index == steps - 1) {
					$(this).addClass("last");
				}
			});
		}
	},
	
	/*
	 * 把参数拼成json对象形式的字符串
	 */ 
	"getJsonParam":function(elementid) {
		var ruleType = $("#ruleType").val();
		var ruleArr = new Array();
		if(ruleType == 2 || ruleType ==4){
			$(elementid).each(function(i){
				var obj={
					SortNo : i+1,
					ActorType : $(this).find("[name=roleTypeActor]").val(),
					ActorValue : parseInt($(this).find("[name=roleValueActor]").val())
				};
				ruleArr.push(obj);
			});
		}else if(ruleType == 3){
			// 复杂模式
			$(elementid).each(function(i){
				var roleTypeAuditor =$(this).find("[name=roleTypeAuditor]").val();
				var obj={
						SortNo : i+1,
						RoleTypeCreater : $(this).find("[name=roleTypeCreater]").val(),
						RoleValueCreater : $(this).find("[name=roleValueCreater]").val(),
						RoleTypeAuditor : roleTypeAuditor,
						RoleValueAuditor : $(this).find("[name=roleValueAuditor]").val()
				};
				if(roleTypeAuditor == "P"){
					obj.RolePrivilegeFlag = $(this).find("[name=rolePrivilegeFlag]").val();
				}
				ruleArr.push(obj);
			});
		}else if(ruleType == 5){
			// 代收模式
			$(elementid).each(function(i){
				var roleTypeConfirmation =$(this).find("[name=roleTypeConfirmation]").val();
				var obj={
						SortNo : i+1,
						RoleTypeReceiver : $(this).find("[name=roleTypeReceiver]").val(),
						RoleValueReceiver : $(this).find("[name=roleValueReceiver]").val(),
						RoleTypeConfirmation : roleTypeConfirmation,
						RoleValueConfirmation : $(this).find("[name=roleValueConfirmation]").val()
				};
				if(roleTypeConfirmation == "P"){
					obj.RolePrivilegeRecFlag = $(this).find("[name=rolePrivilegeRecFlag]").val();
				}
				ruleArr.push(obj);
			});
		}
		var strJson = JSON2.stringify(ruleArr);
		return strJson;
	},
	
	/*
	 * 下一步动作
	 */
	"doNext" : function (actionId, actionKbn){
		binOLPLCOM02_global.needUnlock = false;
		var tokenVal = parentTokenVal();
			
		$("#parentCsrftoken").val(tokenVal);
		if (actionId) {
			$("#actionId").val(actionId);
			//$("#actionKbn").val(actionKbn);
		}
		var thirdPartyFlag = $("[name=thirdParty]:checked").val();
		$("#hideThirdParty").val(thirdPartyFlag);

		if($("#canEdit").length>0){
			if($("#canEdit").prop("checked")){
				$("#hideCanEditFlag").val("true");
			}else{
				$("#hideCanEditFlag").val("false");
			}
		}
		if($("#preferred").length>0){
			if($("#preferred").prop("checked")){
				$("#hidePreferredFlag").val("true");
			}else{
				$("#hidePreferredFlag").val("false");
			}
		}
		if($("#autoAudit").length>0){
			if($("#autoAudit").prop("checked")){
				$("#hideAutoAuditFlag").val("true");
			}else{
				$("#hideAutoAuditFlag").val("false");
			}
		}
		//生成json格式的规则
		var ruleParams = this.getJsonParam("#tableAudit [id='hiddentd']");

		$("#ruleParams").val(ruleParams);
		$("#toNextForm").submit();
	},
		
	/*
	 * 保存规则
	 */
	"doSave" : function (){
		binOLPLCOM02.hideError();
		//无规则不保存
		if($("#ruleType").val() == "" || $("#ruleType").val() == "1"){
			return;
		}
		var thirdPartyFlag = $("[name=thirdParty]:checked").val();
		var canEditFlag = "";
		if($("#canEdit").length>0){
			if($("#canEdit").prop("checked")){
				canEditFlag = "true";
			}else{
				canEditFlag = "false";
			}
		}
		var preferredFlag = "";
		if($("#preferred").length>0){
			if($("#preferred").prop("checked")){
				preferredFlag = "true";
			}else{
				preferredFlag = "false";
			}
		}
		var autoAuditFlag = "";
		if($("#autoAudit").length>0){
			if($("#autoAudit").prop("checked")){
				autoAuditFlag = "true";
			}else{
				autoAuditFlag = "false";
			}
		}
		var url = $("#saveRuleUrl").attr("href");
		var param = $('#mainForm').formSerialize();

		var ruleOnAction = "";
		for(var i=0;i<$("[name=ruleOnAction]").length;i++){
			ruleOnAction += "&ruleOnAction=" +$($("[name=ruleOnAction]")[i]).val();
		};
		var ruleOnStep = "";
		for(var i=0;i<$("[name=ruleOnStep]").length;i++){
			ruleOnStep += "&ruleOnStep=" +$($("[name=ruleOnStep]")[i]).val();
		};
		param += "&brandInfoId="+$("#brandInfoId").val()+"&onStep="+$("#onStep").val()
				+"&flowType="+$("#flowType").val()+"&workFlowId="+$("#workFlowId").val()
				+"&ruleType="+$("#ruleType").val()+"&thirdPartyFlag="+thirdPartyFlag
				+"&canEditFlag="+canEditFlag
				+"&preferredFlag="+preferredFlag
				+"&autoAuditFlag="+autoAuditFlag
				+"&ruleOnFlowCode="+$("#ruleOnFlowCode").val()
				+"&stepId="+$("#stepId").val()
				+ruleOnAction+ruleOnStep;
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg) {
			
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	
	/*
	 * 保存所有规则
	 */
	"doSaveAll":function(){
		var thirdPartyFlag = $("[name=thirdParty]:checked").val();
		var canEditFlag = "";
		if($("#canEdit").length>0){
			if($("#canEdit").prop("checked")){
				canEditFlag = "true";
			}else{
				canEditFlag = "false";
			}
		}
		var preferredFlag = "";
		if($("#preferred").length>0){
			if($("#preferred").prop("checked")){
				preferredFlag = "true";
			}else{
				preferredFlag = "false";
			}
		}
		var autoAuditFlag = "";
		if($("#autoAudit").length>0){
			if($("#autoAudit").prop("checked")){
				autoAuditFlag = "true";
			}else{
				autoAuditFlag = "false";
			}
		}
		var url = $("#saveAllRuleUrl").attr("href");
		var ruleParams = this.getJsonParam("#tableAudit [id='hiddentd']");
		var param = "&brandInfoId="+$("#brandInfoId").val()
				+"&flowType="+$("#flowType").val()+"&workFlowId="+$("#workFlowId").val()
				+"&stepId="+$("#stepId").val()+"&hideThirdParty="+thirdPartyFlag
				+"&hideCanEditFlag="+canEditFlag+"&hidePreferredFlag="+preferredFlag
				+"&hideAutoAuditFlag="+autoAuditFlag
				+"&ruleParams="+ruleParams
				+"&onStep="+$("#onStep").val();
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg) {
			
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	
	"validSelect":function(obj){
		binOLPLCOM02.hideError();
		$(obj).valid();
	},
	
	/*
	 * 根据身份类型查询身份信息List
	 */
	"searchCodeByType":function(obj) {
		binOLPLCOM02.hideError();
		$(obj).valid();

		var relevanceIDName = "";
		if($(obj).parent().find("#initiatorType").length==1){
			relevanceIDName = "#initiatorID";
		}else if($(obj).parent().find("#auditorType").length==1){
			relevanceIDName = "#auditorID";
			$("#privilegeRelationship").hide();
			$("#privilegeFlag").hide();
		}else if($(obj).parent().find("#actorType").length==1){
			relevanceIDName = "#actorID";
		}else if($(obj).parent().find("#receiveatorType").length==1){
			relevanceIDName = "#receiveatorID";
		}else if($(obj).parent().find("#confirmationType").length==1){
			relevanceIDName = "#confirmationID";
			$("#privilegeRecRelationship").hide();
			$("#privilegeRecFlag").hide();
		}
		$(relevanceIDName).find("option:not(:first)").remove();

		var type = $(obj).val();
		if(type == ""){
			return;
		}else if(type == "2" && relevanceIDName == "#auditorID"){
			//审核者身份类型，选择岗位显示权限控制下拉框
			$("#privilegeRelationship").show();
			$("#privilegeFlag").show();
		} else if(type == "2" && relevanceIDName == "#confirmationID"){
			// 确认者身份类型，选择岗位显示权限控制下拉框
			$("#privilegeRecRelationship").show();
			$("#privilegeRecFlag").show();
		}
		
		var url = $("#searchCodeByTypeUrl").attr("href");
		var param = "auditorType="+type+"&brandInfoId="+$("#brandInfoId").val();
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg) {
			if(msg) {
				var json = eval('('+msg+')');
				for(var i in json) {
					$(relevanceIDName).append('<option value="'+json[i].code+'">'+escapeHTMLHandle(json[i].name)+'</option>');
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	
	/*
	 * 重置序号
	 */
	"resetNum":function(){
    	$("#tableAudit tr").each(function(i){
    		$(this).find("td:eq(0)").text(i);
    	});
	},
	
	/*
	 * 重置表格的优先级图标
	 */
	"resetIcon":function(){
		var lastTR = $("#tableAudit tbody tr:last-child");
		var blankIcon = "<span class='left' style='height:16px; width:16px; display:block;'></span>";
		
		$("#tableAudit tbody tr").each(function(i){
			$(this).find("#viewSeq").html("");
			if(i == 0){
				$(this).find("#viewSeq").append(blankIcon);
				$(this).find("#viewSeq").append(blankIcon);
				$(this).find("#viewSeq").append($("#arrowDownDiv").html());
				$(this).find("#viewSeq").append($("#arrowLastDiv").html());
			}else if(i == lastTR.index()){
				$(this).find("#viewSeq").append($("#arrowFirstDiv").html());
				$(this).find("#viewSeq").append($("#arrowUpDiv").html());
				$(this).find("#viewSeq").append(blankIcon);
				$(this).find("#viewSeq").append(blankIcon);
			}else{
				$(this).find("#viewSeq").append($("#arrowFirstDiv").html());
				$(this).find("#viewSeq").append($("#arrowUpDiv").html());
				$(this).find("#viewSeq").append($("#arrowDownDiv").html());
				$(this).find("#viewSeq").append($("#arrowLastDiv").html());
			}
    	});
		
		if($("#tableAudit tbody tr").length == 1){
			$("#tableAudit tbody tr:eq(0)").find("#viewSeq").html("");
		}
	},
	
	/*
	 * 移至最上
	 */
	"moveFirst":function(obj){
		var current=$(obj).parent().parent();
		var first=$("#tableAudit tbody tr")[0];
		if(current.index()>0){
			current.insertBefore(first);
		}
		this.resetNum();
		this.resetIcon();
	},
	
	/*
	 * 上移
	 */
	"moveUp":function(obj){
		var current=$(obj).parent().parent();
		var prev=current.prev();
		if(current.index()>0){
			current.insertBefore(prev);
		}
		this.resetNum();
		this.resetIcon();
	},
	
	/*
	 * 下移
	 */
	"moveDown":function(obj){
		var current=$(obj).parent().parent();
		var next=current.next();
		if(next){
			current.insertAfter(next);
		}
		this.resetNum();
		this.resetIcon();
	},
	
	/*
	 * 移至最下
	 */
	"moveLast":function(obj){
		var current=$(obj).parent().parent();
		var last = $("#tableAudit tbody tr:last-child");
		if(current.index() != last.index()){
			current.insertAfter(last);
		}
		this.resetNum();
		this.resetIcon();
	},
	
	/*
	 * 根据select的index值取的真正type
	 */
	"getSelectType":function(value){
		var type="";
		if(value == 1){
			type = "U";
		}else if(value == 2){
			type = "P";
		}else if(value == 3){
			type = "D";
		}else if(value == 4){
			type = "DT";
		}
		return type;
	},
	
	/*
	 * 在画面上增加一行规则
	 */
	"addRule":function(obj){
		$("#actionResultDisplay #actionResultDiv").attr("style","display:none;");
		//验证数据
		if (!$('#mainForm').valid()) {
			return false;
		};
		var ruleType = $("#ruleType").val();
		//类型为用户时，发起者与审核者不允许相同
		if(ruleType == 3){
			if($("#initiatorType").val() == "1" && $("#initiatorID").val() == $("#auditorID").val()){
				$("#errorSpan").html($("#errMsg1").val());
				$("#actionDisplay #errorDiv").attr("style","");
				return false;
			}
		}

		//取序号
		var index = $("#tableAudit").find("tr").length;
		
		var newRow = "";
		
		if(ruleType == 2 || ruleType == 4){
			// 简单模式、CherryShow模式
			newRow = "<tr><td>"+index+"</td><td id='ActorTypeText'>"
			+$("#actorType option:selected").text()+"</td><td id='ActorValueText'>"
			+$("#actorID option:selected").text()
			//+"</td><td id='viewSeq'>"
			//+$("#arrowFirstDiv").html()
			//+$("#arrowUpDiv").html()
			//+$("#arrowDownDiv").html()
			//+$("#arrowLastDiv").html()
			+"</td><td>"
			+$("#deleteRuleDiv").html()
			+"</td><td id='hiddentd' class='hide'>"
			+"<input type='hidden' name='roleTypeActor' value='"+this.getSelectType($("#actorType").val())+"'>"
			+"<input type='hidden' name='roleValueActor' value='"+$("#actorID").val()+"'>"
			+"</td></tr>";
		}else if(ruleType == 3){
			// 复杂模式
			var privilegeFlagText = "";
			var privilegeFlag = "";
			if($("#auditorType").val() == 2){
				privilegeFlagText = $("#privilegeFlag option:selected").text();
				privilegeFlag = $("#privilegeFlag").val();
			}
			var preferredFlagClass = "";
			if($("#preferred").prop("checked") == false){
				preferredFlagClass = "hide";
			}
			newRow = "<tr><td>"+index+"</td><td id='RoleTypeCreaterText'>"
			+$("#initiatorType option:selected").text()+"</td><td id='RoleValueCreaterText'>"
			+$("#initiatorID option:selected").text()+"</td><td id='RoleTypeAuditorText'>"
			+$("#auditorType option:selected").text()+"</td><td id='RoleValueAuditorText'>"
			+$("#auditorID option:selected").text()+"</td><td id='RolePrivilegeFlagText'>"
			+privilegeFlagText+"</td><td id='viewSeq' class='"+preferredFlagClass+"'>"
			+$("#arrowFirstDiv").html()
			+$("#arrowUpDiv").html()
			+$("#arrowDownDiv").html()
			+$("#arrowLastDiv").html()
			+"</td><td>"
			+$("#deleteRuleDiv").html()
			+"</td><td id='hiddentd' class='hide'>"
			+"<input type='hidden' name='roleTypeCreater' value='"+this.getSelectType($("#initiatorType").val())+"'>"
			+"<input type='hidden' name='roleValueCreater' value='"+$("#initiatorID").val()+"'>"
			+"<input type='hidden' name='roleTypeAuditor' value='"+this.getSelectType($("#auditorType").val())+"'>"
			+"<input type='hidden' name='roleValueAuditor' value='"+$("#auditorID").val()+"'>"
			+"<input type='hidden' name='rolePrivilegeFlag' value='"+privilegeFlag+"'>";
			+"</td></tr>";
		} else if(ruleType == 5) {
			// 代收模式
			var privilegeRecFlagText = "";
			var privilegeRecFlag = "";
			if($("#confirmationType").val() == 2){
				privilegeRecFlagText = $("#privilegeRecFlag option:selected").text();
				privilegeRecFlag = $("#privilegeRecFlag").val();
				
			}
			var preferredFlagClass = "";
			if($("#preferred").prop("checked") == false){
				preferredFlagClass = "hide";
			}
			newRow = "<tr><td>"+index+"</td><td id='RoleTypeReceiverText'>"
			+$("#receiveatorType option:selected").text()+"</td><td id='RoleValueReceiverText'>"
			+$("#receiveatorID option:selected").text()+"</td><td id='RoleTypeConfirmationText'>"
			+$("#confirmationType option:selected").text()+"</td><td id='RoleValueConfirmationText'>"
			+$("#confirmationID option:selected").text()+"</td><td id='RolePrivilegeRecFlagText'>"
			+privilegeRecFlagText+"</td><td id='viewSeq' class='"+preferredFlagClass+"'>"
			+$("#arrowFirstDiv").html()
			+$("#arrowUpDiv").html()
			+$("#arrowDownDiv").html()
			+$("#arrowLastDiv").html()
			+"</td><td>"
			+$("#deleteRuleDiv").html()
			+"</td><td id='hiddentd' class='hide'>"
			+"<input type='hidden' name='roleTypeReceiver' value='"+this.getSelectType($("#receiveatorType").val())+"'>"
			+"<input type='hidden' name='roleValueReceiver' value='"+$("#receiveatorID").val()+"'>"
			+"<input type='hidden' name='roleTypeConfirmation' value='"+this.getSelectType($("#confirmationType").val())+"'>"
			+"<input type='hidden' name='roleValueConfirmation' value='"+$("#confirmationID").val()+"'>"
			+"<input type='hidden' name='rolePrivilegeRecFlag' value='"+privilegeRecFlag+"'>";
			+"</td></tr>";
		}

		$("#tableAudit").append(newRow);
		this.resetIcon();
	},
	
	/*
	 * 删除规则
	 */
	"deleteRule":function(obj){
		$(obj).parent().parent().remove();
		this.resetNum();
		this.resetIcon();
	},
	
	/*
	 * 隐藏/显示规则设置区
	 */
	"changeRadio":function(obj){
		if($("#thirdParty_1").prop("checked")){
			$("#ruleSet").addClass("hide");
			$("#tableAudit").addClass("hide");
			$("#otherRule").addClass("hide");
			$("#divBtnAdd").addClass("hide");
		}else if($("#thirdParty_2").prop("checked")){
			$("#ruleSet").removeClass("hide");
			$("#tableAudit").removeClass("hide");
			$("#otherRule").removeClass("hide");
			$("#divBtnAdd").removeClass("hide");
		}
	},
	
	/*
	 * 隐藏/显示优先级
	 */
	"changePreferredFlag":function(obj){
		if($("#preferred").prop("checked")){
			$("#thViewSeq").removeClass("hide");
			$("[id='viewSeq']").removeClass("hide");
		}else{
			$("#thViewSeq").addClass("hide");
			$("[id='viewSeq']").addClass("hide");
		}
	},
	
	/**
	 * 隐藏错误提示
	 */
	"hideError":function(){
		$("#actionDisplay #errorDiv").attr("style","display:none;");
	}
};
var binOLPLCOM02 = new BINOLPLCOM02();

$(document).ready(function() {
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	// 显示导航条
	binOLPLCOM02.showSteps();
	
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			initiatorType:{required: true},
			initiatorID:{required: true},
			auditorType:{required: true},
			auditorID:{required: true},
			actorType:{required: true},
			actorID:{required: true},
			receiveatorType:{required: true},
			receiveatorID:{required: true},
			confirmationType:{required: true},
			confirmationID:{required: true}
		}
	});
	
});