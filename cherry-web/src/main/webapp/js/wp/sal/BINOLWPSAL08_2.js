var WPCOM_dialogBody ="";
var BINOLWPSAL08_2_GLOBAL = function() {

};


BINOLWPSAL08_2_GLOBAL.prototype = {
		"cancel":function(){
			// 最后一行第一个可见的文本框获得焦点
			BINOLWPSAL02.firstInputSelect();
			var activityCode = $("#maincode_LYHD_0").val();
			// 移除对应选择框选中状态
			var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ activityCode +"']");
			if(!!$thisCheckBox.attr("checked")){
				$thisCheckBox.removeAttr("checked");
			}
			closeCherryDialog('dialogInit',WPCOM_dialogBody);
		},
		"confirm":function(){
			var maincode=$('#maincode_LYHD_0').val();
			var memberInfoId = $("#memberInfoId").val();
			var spanMemberCode=$('#memberCode_LYHD_0').val();
			var subjectCode=$('#subjectCode_LYHD_0').val();
			var getLYHDActivityInfoUrl=$('#getLYHDActivityInfoUrl').attr("href");
			var params="memberCode="+spanMemberCode+"&maincode="+maincode+"&activityType=1"+"&subjectCode="+subjectCode+"&memberInfoId="+memberInfoId;
			cherryAjaxRequest({
				url: getLYHDActivityInfoUrl,
				param: params,
				callback: function(data) {
//					// 最后一行第一个可见的文本框获得焦点
//					BINOLWPSAL02.firstInputSelect();
					closeCherryDialog('dialogInit',WPCOM_dialogBody);
					if(data != undefined && data != null && data != ""){
						if(data == "ERROR"){
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"没有查询到礼品信息", 
								type:"MESSAGE", 
								focusEvent:function(){
									// 移除对应选择框选中状态
									var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ maincode +"']");
									if(!!$thisCheckBox.attr("checked")){
										$thisCheckBox.removeAttr("checked");
									}
									// 输入框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
						}else if(data == "NOTMEMBER"){
							// 显示提示信息
							BINOLWPSAL02.showMessageDialog({
								message:"活动需要会员才能参与", 
								type:"MESSAGE", 
								focusEvent:function(){
									// 移除对应选择框选中状态
									var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ maincode +"']");
									if(!!$thisCheckBox.attr("checked")){
										$thisCheckBox.removeAttr("checked");
									}
									// 输入框获得焦点
									BINOLWPSAL02.firstInputSelect();
								}
							});
						}else{
							var param_map = eval("("+data+")");
							var promotionInfo=param_map.promotionInfo;
							var promotionDetail=param_map.promotionDetail;
							BINOLWPSAL02.deleteEmptyRow();
							BINOLWPSAL02.appendPromotionInfo(promotionInfo);
		//					BINOLWPSAL02.appendPromotionProduct(promotionDetail);
							// 增加明细记录行
							$.each(promotionDetail, function(i){
								// 添加促销活动记录行
								BINOLWPSAL02.appendPromotionProduct(promotionDetail[i]);
							});
							BINOLWPSAL02.addNewLine();
							BINOLWPSAL02.calcuTatol();
						}
					}else{
						// 查询结果为空的情况下显示错误信息
						BINOLWPSAL02.showMessageDialog({
							message:"没有查询到礼品信息", 
							type:"MESSAGE", 
							focusEvent:function(){
								// 移除对应选择框选中状态
								var $thisCheckBox = $("#promotionData").find("#ckPromotion:[value='"+ maincode +"']");
								if(!!$thisCheckBox.attr("checked")){
									$thisCheckBox.removeAttr("checked");
								}
								// 最后一行第一个可见的文本框获得焦点
								BINOLWPSAL02.firstInputSelect();
							}
						});
					}
					// 还原按钮样式
					$("#btnCollect").attr("class","btn_top").removeAttr("disabled");
					$("#btnDiscount").attr("class","btn_top").removeAttr("disabled");
				}
			});
		}
};



var BINOLWPSAL08_2 = new BINOLWPSAL08_2_GLOBAL();


$(document).ready(function(){
	WPCOM_dialogBody = $('#dialogInit').html();
	$("#customerText").bind("keydown", function (e) {
		var key = e.which;
		if (key == 13) {
			e.preventDefault();
			BINOLWPSAL08_2.confirm();
		}
	});
});