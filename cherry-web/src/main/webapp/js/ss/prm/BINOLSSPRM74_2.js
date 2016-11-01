function BINOLSSPRM74_2() {};
var submitable = true;
BINOLSSPRM74_2.prototype = {
		"showhidediv":function(id){
			var display =$('#'+id).css('display');
			if(display == 'none'){
				$('#'+id).show();
			}else{
				$('#'+id).hide();
			}
		},
		"showResultMessage":function(){
			var resultCode=$("#resultCode").val();
			var resultMessage=$("#resultMessage").val();
			if(resultCode != 0){
				$("#showResultMessage").html(resultMessage);
				$("#err").show();
			}
		},
		"showErrorMessage":function(message){
			$("#showResultMessage").html(message);
			$("#err").show();
			$("#showResultMessage").parent().find(".btn_qr").focus();
		},
		"confirm":function(){
			var memberCode=$("#memberCode").html();
			var mobileNo;
			if(memberCode == "VBC000000001"){
				mobileNo=$("#mobileNo input").val();
			}else{
				mobileNo=$("#mobileNo").html();
			}
			var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
			 if (!reg.test(mobileNo)) {
			      BINOLSSPRM74_2.showErrorMessage("您输入的手机号码有误，请确认后再提交");
			      return false;
			 }
			if(submitable==false){
				return;
			}else{
				submitable=false;
			}
			var competedRule=new Array();
			$("#rule_table tr:gt(0)").each(function(){
				var $this=$(this);
				var radioFlag=$this.find("input[type='radio']").is(":checked");
				var checkboxFlag=$this.find("input[type='checkbox']").is(":checked");
				if(radioFlag || checkboxFlag){
					var maincode=$this.find("input[name='maincode']").val();
					var ruleType=$this.find("input[name='ruleType']").val();
					var map={
						"maincode":maincode,
						"ruleType":ruleType
					};
					competedRule.push(map);
				}
			});
			//已经计算完毕活动
			var competedRule_json=JSON2.stringify(competedRule);
			//主单信息
			var main_map_json=$("#main_map_json").val();
			//购物车信息
			var cart_list_json=$("#cart_list_json").val();
			//已经参加的活动
			var rule_list_json=$("#rule_list_json").val();
			var promotionSendUrl=$("#promotionSendUrl").attr("href");
			var datasourceName=$("#datasourceName").val();
			var params="memberPhone="+mobileNo+"&main_json="+main_map_json+"&rule_json="+rule_list_json+"&competedRule_json="+competedRule_json+"&shoppingcart_json="+cart_list_json+"&datasourceName="+datasourceName;
			setTimeout("submitable=true",5000)
			$.ajax({
				url: promotionSendUrl,
				data: params,
//				timeout: 3000,
				type :'post',
				success: function(data) {
					if(data == null || data == "" || data == undefined || data == "ERROR"){
						BINOLSSPRM74_2.showErrorMessage($("confirmErr").val());
						return;
					}
					var result_map = eval("("+data+")");
					var resultCode =result_map.ResultCode;
					var resultMsg =result_map.ResultMsg;
					if(resultCode == "0"){
						var resultMsg_arr=resultMsg.split(",");
						$("#dzq .error").html("您选择的优惠券已经发送到手机号"+resultMsg_arr[0]+"，共"+resultMsg_arr[1]+"张，请注意查收。");
						$("#dzq").show();
					}else{
						if(resultMsg == null || resultMsg == "" || resultMsg == undefined){
							BINOLSSPRM74_2.showErrorMessage("发券操作失败，请联系系统管理员");
						}else{
							BINOLSSPRM74_2.showErrorMessage(resultMsg);
						}
					}
				}
//				,
//				complete : function(XMLHttpRequest,status){
//					var connectTimes=Number($("#connectTimes").val());
//					if(status=='timeout' ){//超时,status还有success,error等值的情况
//			　　　　　  	if(connectTimes <=0){
//			　　　　　  		$("#connectTimes").val("1");
//			　　　　　  		BINOLSSPRM74_2.showErrorMessage($("#connectErr").val());
//			　　　　　  	}else{
//			　　　　　  		for(connectTimes;connectTimes>0;connectTimes--){
//			　　　　　  			$("#connectTimes").val(0);
//			　　　　　  			BINOLSSPRM74_2.confirm();
//			　　　　　  		}
//			　　　　　  	}
//			　　　　}
//				}
			});
		},
		"cancel":function(){
			window.close();
		},
		"checkGroup":function(obj){
			var $this=$(obj);
			//根据配置项来确定可以选择几个发券活动
			var sendChooseFlag=$("#sendChooseFlag").val();
			if(sendChooseFlag == "N"){
				var sendChooseCount=$("#rule_table input:checked").length;
				if(sendChooseCount > 1){
					BINOLSSPRM74_2.showErrorMessage("您至多选择一个发券活动");
					$this.attr("checked",false);
					return false;
				}
			}
			var flag=0;
			var groupId=$this.parents("tr").find("input[name='groupId']").val();
			$("#rule_table tbody tr").each(function(){
				var checkedFlag=$(this).find("input[type='checkbox']").is(":checked");
				if(checkedFlag){
					var $groupId=$(this).find("input[name='groupId']").val();
					if(groupId == $groupId){
						flag += 1;
					}
				}
			});
			if(flag > 1){
				$this.attr("checked",false);
			}
		},
		"checkMobile":function(obj){
			var mobile=$(obj).val();
			var reg = /^0?1[3|4|5|7|8][0-9]\d{8}$/;
			 if (!reg.test(mobile)) {
			      $("#mobileError").show();
			 }else{
				  $("#mobileError").hide();
			 }
		}
};

var BINOLSSPRM74_2 =  new BINOLSSPRM74_2();

$(document).ready(function() {
	BINOLSSPRM74_2.showResultMessage();
//	var height=document.body.clientHeight;
//	var width=document.body.clientWidth;
//	$(".tablebox").css("height",height*0.55);
//	$(".tablebox.gift").css("height",height*0.3);
//	$(".leftbox").css("height",height*0.74);
//	$(".rightbox").css("height",height*0.74);
//	$(".bottom_item.promotion").css("font-size",width*0.015);
});
