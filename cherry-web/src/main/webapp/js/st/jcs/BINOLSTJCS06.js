var BINOLSTJCS06 = function(){
};
BINOLSTJCS06.prototype = {

	/** 查询数据 */
	"search" : function (flag){
		var that = this;
		if(!flag)that.empty();
		var url=url?url:$("#search").html();
		var param = $("#brandInfoId").serialize()+ "&csrftoken=" +$('#csrftoken').val();
		var divId = "#dataTable";
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
			    $(divId).html(msg);
			    
			    //更新编辑区分 --完成编辑
				$("#flag").val("0");
			}
		});
		$("#checkAll").prop("checked",false);
	},
	/** 弹出新增按钮对话框 */
	"popAddDialog" : function() {
		$("#actionResultDisplay").empty();
		
		$("#errorMessage").html("");
		$("#errorMessageTemp1 #errorSpan").html("");
		$("#errorMessageTemp1").hide();
		
		var _this = this;
		var url = $("#add_depot").attr("href");
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width:  450,
					height: 360,
					text: msg,
					title: 	$("#addMsgTitle").text(),
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){_this.addMsgDialog();},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				openDialog(dialogSetting);
		};
		cherryAjaxRequest({
			url: url,
			callback: callback
		});
		
		
	},
	/** 添加保存 */
	"addMsgDialog":function() {  
		var that = this;
		if(!$('#addForm').valid()) {
			return false;
		}
		var url = $("#addSave_depot").attr("href");
		var param = $('#addForm').find(":input").serialize()+"&brandInfoId="+$("#brandInfoId").val();
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
						width: 450,
						height: 350,
						title: $("#saveSuccessTitle").text(),
						confirm: $("#dialogClose").text(),
						confirmEvent: function(){removeDialog("#dialogInit");that.search(true);},
                        closeEvent: function(){removeDialog("#dialogInit");that.search(true);}
					};
					openDialog(dialogSetting);
				}// 保存失败的场合
				else if($("#dialogInit").find("#errorMessageDiv").length != 0){
	                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
	                $("#dialogInit").dialog( "option", {
	                    buttons: [{
	                        text: $("#dialogClose").text(),
	                        click: function(){removeDialog("#dialogInit");that.search(true);}
	                    }],
						closeEvent: function(){removeDialog("#dialogInit");that.search(true);}
	                });
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			isDialog: true,
			resultId: "#actionResultDisplay2",
			formId: '#addForm'
		});
		
		
	},
	/** 弹出编辑按钮对话框 */
	"popEditDialog" : function(logInvId) {
		$("#actionResultDisplay").empty();
		var _this = this;
		var url = $("#edit_depot").attr("href");
        param="logInvId=" + logInvId;
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width: 	450,
					height: 360,
					text: msg,
					title: 	$("#updateMsgTitle").text(),
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){_this.savaEditDialog();},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				
				//弹出框体
				openDialog(dialogSetting);
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	/**  编辑保存 */
	"savaEditDialog":function() { 
		var that = this;
		if(!$('#main_form').valid()) {
			return false;
		}
		var url = $("#editSave_depot").attr("href");
		var param = $("#main_form").find(":input").serialize()+"&brandInfoId="+$("#brandInfoId").val();
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
						width: 450,
						height: 330,
						title: $("#saveSuccessTitle").text(),
						confirm: $("#dialogClose").text(),
						confirmEvent: function(){removeDialog("#dialogInit");that.search(true);},
                        closeEvent: function(){removeDialog("#dialogInit");that.search(true);}
					};
					openDialog(dialogSetting);
				}// 保存失败的场合
				else if($("#dialogInit").find("#errorMessageDiv").length != 0){
	                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
	                $("#dialogInit").dialog( "option", {
	                    buttons: [{
	                        text: $("#dialogClose").text(),
	                        click: function(){removeDialog("#dialogInit");that.search(true);}
	                    }],
						closeEvent: function(){removeDialog("#dialogInit");that.search(true);}
	                });
				}
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			isDialog: true,
			resultId: "#actionResultDisplay2",
			formId: '#mainForm'
		});
		
		
	},
	/** 停用或启用 --停用data为0，启用data为1 
	 * 已废除 
	 * */
	"disOrEnable":function(target,url,data){
		var that = this;
		that.empty();
		if(that.disabOptBtn()==false)return;
		$target = $(target);
		var param="";
		$checked=$target.find("tr td input:checked");
		if($checked.length == 0){
			that.selectEmply();
			return;
		}
		$checked.each(function(){
			var type = $(this).parent().parent().find("[name='type']").val();
			param+=$(this).nextAll("[name='logInvId']")[0].value+"join"+data+"join"+type+",";
		});
		param="requestData="+param;
		param += "&brandInfoId=" + $("#brandInfoId").val(); 
		var divId = "#dataTable";
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') > -1){
					that.search(true);
				}
			}
		});
	},
	"checkSelectAll":function(checkbox){
		var that = this;
		that.empty();
		if($(checkbox).prop("checked"))
		{
			$("#dataTable").find(":checkbox").prop("checked",true);
		}else
		{
			$("#dataTable").find(":checkbox").prop("checked",false);
		}
	},
	"empty":function(){
		$("#actionResultDisplay").empty();
		$("#errorMessage").empty();
	},
	/**
	 * 弹出停用
	 * 已废除
	 * 
	 * */
	"confirmInit":function(data)
	{
		var that = this;
		that.empty();
		if(that.disabOptBtn()==false)return;
		var $checked = $("#dataTable").find("tr td input:checked");
		if($checked.length == 0){
			that.selectEmply();
			return;
		}
		var url=$("#disOrEnable").html();
		var text = "";
		var title = "";
		if(data=="0"){
		   text = '<p class="message"><span>'+$('#confirIsDisable').text();
		   title = $('#disableTitle').text();
		}else{
		   text = '<p class="message"><span>'+$('#confirIsEnable').text();
		   title = $('#enableTitle').text();
		}
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text: text,
			width: 	300,
			height: 200,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){that.disOrEnable('#dataTable',url,data);removeDialog("#dialogInit");},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	/**
	 * 选择为空
	 * 
	 * 
	 */
	"selectEmply":function (){
		$("#errorMessage").empty();
		$("#actionResultDisplay").empty();
		$("#errorMessage").html($("#errorMessageTemp1").html());
	},
	/**
	 * 设置按钮有效与无效
	 * 已废除
	 * 
	 */
	"disabOptBtn":function (){
		var flag = $("#flag").val();
		if(flag == "1"){
			return false;
		}
		return true;
	},
	"checkBoxClick":function(){
		var that = this;
		that.empty();
		
		var $eachCheck = $("#dataTable").find(":checkbox");
		var flag =true;
		$eachCheck.each(function(){
			if($(this).prop("checked")==false){
				flag = false;
			};
		});
		if(flag){
			$("#checkAll").prop("checked",true);
		}else{
			$("#checkAll").prop("checked",false);
		}
		return;
	},
	/** 下发--已废除 */
	"issued":function(_this,url){
		var that = this;
		that.empty();
		if(that.disabOptBtn()==false)return;
		var param = $("#brandInfoId").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') > -1){
					that.search(true);
				}
			}
		});
	},
	"isInteger":function(obj){
		var $this = $(obj);
		var patrn=/^[1-9]{1}[0-9]{0,7}$/; 
		if (!patrn.exec($this.val())){
			$this.val("");
		}
	}
};
var BINOLSTJCS06 = new BINOLSTJCS06();
