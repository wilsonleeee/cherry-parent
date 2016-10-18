function BINOLSSPRM75_2() {
	
};

BINOLSSPRM75_2.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		
		/*
		 *保存优惠券
		 */
		"saveCoupon" : function (){
			// 参数序列化
			var param = "";
			// 获取优惠券号码以及时间
			param+="couponNo="+$("#couponNo").val();
			$("#baseInfo").find(":input").each(function() {
				if ($(this).attr("name") && !$(this).is(":disabled")) {
					$(this).val($.trim(this.value));
					if (null == param) {
						param = $(this).serialize();
					} else {
						param += "&" + $(this).serialize();
					}
				}
			});
			cherryAjaxRequest({
				url: $("#saveUrl").attr("href"),
				param: param,
				coverId: "#pageBrandButton",
				callback: function(msg) {
					binolssprm75_2.doRefresh = true;
				}
			});
		}
};


window.onbeforeunload = function(){
	if (window.opener) {
		if (binolssprm75_2.doRefresh) {
			// 刷新父页面
			window.opener.reSearch();
		}
	}   
};

var binolssprm75_2 =  new BINOLSSPRM75_2();
$(function(){
	$('#startTime').cherryDate({
		beforeShow: function(input){
			var value = $('#endTime').val();
			return [value,'maxDate'];
		}
	});
	$('#endTime').cherryDate({
		beforeShow: function(input){
			var value = $('#startTime').val();
			return [value,'minDate'];
		}
	});
});