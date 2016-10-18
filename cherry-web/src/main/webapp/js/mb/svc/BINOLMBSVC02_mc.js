function BINOLMBSVC02_mc() {
	
};
BINOLMBSVC02_mc.prototype = {
	"sendMessage":function(_this){
		$("#errorMessageDiv span").empty();
		$("#errorMessageDiv").addClass("hide");
		var $CheckDialog = $(_this).parent().parent().parent();
		var url = $CheckDialog.find("#sendMessage").val();
		var counterCode = $CheckDialog.find("#counterCode").val();
		var cardCode = $CheckDialog.find("#cardCode").val();
		var $Obj = $("#CheckButton");
		var countdown = 60;
		if(!counterCode || !cardCode){
			return false;
		}
		var param = "cardCode="+cardCode+"&counterCode="+counterCode;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:function(msg){
				//发送成功
				if(msg==0){
					//倒计时
					BINOLMBSVC02_mc.time($Obj,countdown);
				}else{
					$("#errorMessageDiv span").text(msg);
					$("#errorMessageDiv").removeClass("hide");
				}
			}
		});
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
				BINOLMBSVC02_mc.time($Obj,countdown);
			},1000);
		} 
	}
};

var BINOLMBSVC02_mc =  new BINOLMBSVC02_mc();

