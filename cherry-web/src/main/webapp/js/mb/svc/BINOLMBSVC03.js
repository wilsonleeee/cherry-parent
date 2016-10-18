
function BINOLMBSVC03() {
	
};

BINOLMBSVC03.prototype = {
		"confirm":function(){
			var $cardCode = $('#dgCardCode');
			var $counterCode = $('#counterName');
			var dgCardCode = $cardCode.val();
			var counterCode = $counterCode.val();
			if(!$('#cardDetailRechargeForm').valid()) {
				return false;
			}
			if(isNaN(dgCardCode)){
				BINOLMBSVC03.addErrorMessage($cardCode.parent(), '请输入数字');
				return false;
			}
			// 校验储值卡号在指定柜台是否存在
			var checkCardUrl=$('#checkCardUrl').attr('href');
			var params="cardCode="+dgCardCode +"&counterCode=" + counterCode + "&" + getSerializeToken();
			cherryAjaxRequest({
				url : checkCardUrl,
				param :params,
				callback: function(data) {
					if(data == 0){
						// 存在的场合
						var cardDetailRechargeInitUrl = $("#cardDetailRechargeInitUrl").attr("href");
						// 关闭弹出窗口
						closeCherryDialog('dialogInit',WPCOM_dialogBody);
						$('#dialogInit').html("");
						BINOLMBSVC03.getVerification(dgCardCode,counterCode,cardDetailRechargeInitUrl)
					}else if (data == 1){
						// 不存在的场合
						BINOLMBSVC03.addErrorMessage($cardCode.parent(), '请填写正确的卡号');
						return false;
					}
				}
			});
			
		},
		
		"getVerification":function(dgCardCode,counterCode,cardDetailRechargeInitUrl){
			var dialogSetting = {
					dialogInit: "#dialogInit",
					width: 800,
					height: 500,
					title: "储值卡充值",
					closeEvent:function(){
						removeDialog("#dialogInit");
					}
				};
				var url = cardDetailRechargeInitUrl+"?cardCode="+dgCardCode +"&counterCode=" + counterCode +  "&" + getSerializeToken();
				openDialog(dialogSetting);
				cherryAjaxRequest({
					url: url,
					callback: function(data) {
						$("#dialogInit").html(data);
					}
				});
			},
			"commit":function(){
				var cardType = $("#cardType").text();
				if(cardType!="" && cardType!=null){
					var params = $("#rechargeForm").serialize()+ "&" + getSerializeToken();
				}
			},
			"cancel":function(){
				closeCherryDialog('dialogInit',WPCOM_dialogBody);
				// 清空弹出框内容
				$('#dialogInit').html("");
			},
			"addErrorMessage":function($parent,errorMessage){
				$parent.addClass("error");
				$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
				$parent.find('#errorText').attr("title",'error|'+errorMessage);
				$parent.find('#errorText').cluetip({
			    	splitTitle: '|',
				    width: 150,
				    cluezIndex: 20000,
				    tracking: true,
				    cluetipClass: 'error', 
				    arrows: true, 
				    dropShadow: false,
				    hoverIntent: false,
				    sticky: false,
				    mouseOutClose: true,
				    closePosition: 'title',
				    closeText: '<span class="ui-icon ui-icon-close"></span>'
				});
			}
};

var BINOLMBSVC03 =  new BINOLMBSVC03();

$(document).ready(function() {
	cherryValidate({
		formId: "cardDetailRechargeForm",		
		rules: {
			cardCode : {required : true},
			counterName : {required : true}
	   }		
	});
	// 给弹出框窗体全局变量赋值
	WPCOM_dialogBody = $('#dialogInit').html();	

	
	$("#dgCardCode").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			var cardCode = $("#dgCardCode").val();
			var counterCode = $("#counterCode").val();
			if(cardCode != "" && counterCode != ""){
				BINOLMBSVC03.confirm();
			}
		}
	});
	// 柜台下拉框绑定
	var option = {
			elementId:"counterName",
			showNum:20,
			targetId:"counterCode",
			targetDetail:true
	};
	counterSelectBinding(option);
	
});
