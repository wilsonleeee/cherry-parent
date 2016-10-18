function BINOLWPMBM01_02(){};

BINOLWPMBM01_02.prototype = {
	//手机验证
	"mobileCheck":function(){
		$("#CheckMessageDiv").addClass("hide");
		if(!$('#saveForm').valid()) {
			return false;
		}
		var dialogId = 'mobileCheckDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		$("#mobileCheckDiv").empty();
		var url = $("#mobileCheckInit").attr("href");
		var param = $("#saveForm").serialize();
		if(param) {
			url = url + "?" + param;
		}
		cherryAjaxRequest({
			url: url,
			param:null,
			callback: function(msg){
				//如果返回的不是错误信息验证页面则跳转到发送验证码页面
				if(msg.indexOf("fieldErrorDiv")<0){
					$dialog.html(msg);
					// 弹出验证框
					var dialogSetting = {
						dialogInit: "#" + dialogId, 
						text: msg,
						width: 	400,
						height: 200,
						
						title: 	'短信验证',
						confirm: '保存',
						cancel:'返回',
						confirmEvent: function(){
							$("#errorMessageDiv").addClass("hide");
							//获取手机号码以及输入的校验码
							var mobileNumber = $("#mobilePhoneC").val();
							var couponCode = $("#mobileCheckDialog #couponCode").val();
							var par = "mobilePhone="+mobileNumber+"&couponCode="+couponCode;
							cherryAjaxRequest({
								url:$("#couponCheck").attr("href"),
								param:par,
								callback:function(flag){
									if(flag==0){
										//移除验证窗口
										removeDialog("#" + dialogId);
										//并出现会员添加成功
										binolwpmbm01_02.save();
									}else if(flag==1){
										$("#errorMessageDiv").removeClass("hide");
									}
								}
							});
						},
						cancelEvent: function(){
							removeDialog("#" + dialogId);
						}
					};
					openDialog(dialogSetting);
				}
			},
			formId: "saveForm"
		});
	},
	//保存会员资料
	"save":function(){
		var url = $("#addUrl").attr("href");
		var param = $("#saveForm").serialize();
		if(param) {
			url = url + "?" + param;
		}
		var memCode = $("#memCode").val();
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: function(data) {
				if(!data) {
					$("#memOtherDiv").hide();
					$("#memInitDiv").show();
					if(memCode != '') {
						$("#memCodeQ").val(memCode);
						$("#search").click();
					}
					$("#memOtherDiv").empty();
				}
			},
			formId: "saveForm"
		});
	}
};

var binolwpmbm01_02 = new BINOLWPMBM01_02();