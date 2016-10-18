function BINOLWPMBM01_03(){};

BINOLWPMBM01_03.prototype = {
	"mobileCheck":function(){
		$("#CheckMessageDiv").addClass("hide");
		$("#CheckMessageDiv2").addClass("hide");
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
		var mobilePhoneOld = $("#mobilePhoneOld").val();
		var mobilePhone = $("#mobilePhone").val();
		var url = $("#mobileCheckUpdateInit").attr("href");
		var param = $("#saveForm").serialize();
		if(param) {
			url = url + "?" + param;
		}
		cherryAjaxRequest({
			url : url,
			param : null,
			callback : function(msg){
				//如果返回的不是错误信息验证页面则跳转到发送验证码页面
				if(msg.indexOf("fieldErrorDiv")<0){
					$dialog.html(msg);
					// 弹出验证框
					var dialogSetting = {
						dialogInit: "#" + dialogId,
						text: msg,
						width: 	400,
						height: 310,
						
						title: 	'短信验证',
						confirm: '验证',
						cancel : '返回',
						confirmEvent: function(){
							//获取手机号码以及输入的校验码
							$("#errorMessageDiv2").addClass("hide");
							$("#errorMessageDiv").addClass("hide");
							var mobileNumber = $("#mobilePhoneC").val();
							var couponCode = $("#mobileCheckDialog  #couponCode").val();
							var mobileNumberOld = $("#mobilePhoneOldC").val();
							var couponCodeOld = $("#mobileCheckDialog #couponCodeOld").val();
							if(mobileNumberOld == undefined || couponCodeOld == undefined){
								var par = "mobilePhone="+mobileNumber+"&couponCode="+couponCode;
							}else {
								var par = "mobilePhone="+mobileNumber+"&couponCode="+couponCode+"&mobilePhoneOld="+mobileNumberOld+"&couponCodeOld="+couponCodeOld;
							}
							cherryAjaxRequest({
								url:$("#couponCheck").attr("href"),
								param:par,
								callback:function(flag){
									if(flag==0){
										//移除验证窗口
										removeDialog("#" + dialogId);
										//并修改资料
										binolwpmbm01_03.save();
									}else if(flag==1){
										$("#errorMessageDiv").removeClass("hide");
									}else if(flag==2){
										$("#errorMessageDiv2").removeClass("hide");
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
	
	"save":function(){
//		$("#CheckMessageDiv2").addClass("hide");
//		$("#CheckMessageDiv").addClass("hide");
//		//判断是否需要短信验证以及是否验证通过
//		var isNeedCheck = $("#isNeedCheck").val();
//		if(isNeedCheck == "Y"){
//			var checkFlag = $("#checkFlag").val();
//			if(checkFlag){
//				if(checkFlag!='0'){
//					$("#CheckMessageDiv2").removeClass("hide");
//					return false;
//				}
//			}else{
//				$("#CheckMessageDiv2").removeClass("hide");
//				return false;
//			}
//		}
		if(!$('#saveForm').valid()) {
			return false;
		}
		var url = $("#updateUrl").attr("href");
		var param = $("#saveForm").serialize();
		if(param) {
			url = url + "?" + param;
		}
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: function(data) {
				if(!data) {
					$("#memOtherDiv").hide();
					$("#memInitDiv").show();
					$("#memOtherDiv").empty();
					if(oTableArr[100] != null) {
						oTableArr[100].fnDraw();
					}
				}
			},
			formId: "saveForm"
		});
	}
};

var binolwpmbm01_03 = new BINOLWPMBM01_03();