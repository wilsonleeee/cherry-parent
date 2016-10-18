function BINOLWPMC01(){};
BINOLWPMC01.prototype = {
	//会员短信验证
	"mobilePhoneCheck":function(){
		var mobilePhone = $("#mobilePhoneC").val();
		var url = $("#sendMessage").val();
		var param = "mobilePhone="+mobilePhone;
		$("#errorMessageDiv").attr("hide","true");
		param += "&checkType=0";
		var $Obj = $("#CheckButton");
		var countdown = 60;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:function(){
				binolwpmc01.time($Obj,countdown);
			}
		});

	},
	//换卡旧号码短信验证
	"mobilePhoneOldCheck":function(){
		var mobilePhoneOld = $("#mobilePhoneOldC").val();
		var url = $("#sendMessage").val();
		var param = "mobilePhone="+mobilePhoneOld;
		param += "&checkType=0";
		var $Obj = $("#CheckOldButton");
		$("#errorMessageDiv").attr("hide","true");
		$("#errorMessageDiv2").attr("hide","true");
		var countdown = 60;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:function(){
				binolwpmc01.time($Obj,countdown);
			}
		});
	},
	//手机号码验证
	"regMobile":function(phoneNumber){
		var reg = /^1[0-9]{10}$/;
		if(phoneNumber.length==0){
			alert('请输入手机号码！');
			return 0;
		}
		if(phoneNumber.length!=11){
			alert('请输入11位的手机号码！');
			return 0;
		}
		if(!reg.test(phoneNumber)){
			alert('请输入有效的手机号码！');
			return 0;
		}
		return 1;
	},
	//短信发送倒计时
	"time":function(Obj,countdown){
		var $Obj = $(Obj);
		if (countdown == 0) {
			$Obj.find('span').text("发送短信");
			$Obj.removeAttr("disabled");
		} else { 
			$Obj.find('span').text("重新发送(" + countdown + ")");
			$Obj.attr('disabled',"true");
			countdown--; 
			setTimeout(function(){
				binolwpmc01.time($Obj,countdown);
			},1000);
		} 
	}
};

var binolwpmc01 = new BINOLWPMC01();