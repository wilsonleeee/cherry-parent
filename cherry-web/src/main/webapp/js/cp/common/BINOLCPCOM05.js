var BINOLCPCOM05_GLOBAL = function () {

};
BINOLCPCOM05_GLOBAL.prototype = {
		
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		"doNext" : function (actionId){
			if (!$('#toNextForm').valid()) {
				return false;
			};
			var tokenVal = parentTokenVal();
			$("#parentCsrftoken").val(tokenVal);
			if (actionId) {
				$("#actionId").val(actionId);
			}
			this.needUnlock = false;
			$("#toNextForm").submit();
		},
		/*
		 * 根据主题活动类型改变子活动类型列表
		 */
		"changeSubType" : function(obj) {
			// 主题活动类型
			var val = $(obj).val();
			var $optDiv = $("#subCampType_" + val);
			$("#subCampType").html($optDiv.html());
			var $subDiv = $("#subCampaignValid_" + val);
			$("#subCampaignValid").html($subDiv.html());
			if(val == 'DHHD'){
				$('#exPointInfo').html($('#exPointInfo_DHHD').html());
				// 活动开始日期
				CAMPAIGN_TEMPLATE.initDate("#exPointDeadDate");
				if ($("#memberClubId").length > 0) {
					$("#clubSpan").show();
				}
			}else{
				$('#exPointInfo').empty();
				if ($("#memberClubId").length > 0) {
					$("#clubSpan").hide();
				}
			}
			this.setSendFlag();
		},
		// 改变参考类型
		"changeReferType":function(_this,key){
			var $this = $(_this);
			var thisVal = $(_this).val();
			var $siblings = $this.siblings('div');
			$siblings.hide();
			// 删除验证错误提示信息
			var $error = $siblings.find('.error');
			$error.removeClass('error');
			$error.find('#errorText').remove();
			$siblings.find(':input').val('');
			var idPex = '#referType_' + key + '_';
			if(thisVal == '0'){
				$(idPex + '0').show();
			}else{
				$(idPex + '1').show();
			}
		},
		/*
		 * 活动验证选择
		 */
		"changeSubValid" : function(_this) {
			var $this = $(_this);
			var $lv = $("#localValidRule");
			if ($lv.hasClass("error")) {
				$lv.find("#errorText").remove();
				$lv.removeClass("error");
			}
			// 本地验证
			if ("1" == $this.val()) {
				$lv.show();
			} else {
				$lv.hide();
				$lv.find(':input').val("");
			}
		},
		/*
		 * 活动阶段选择pop窗口
		 */
		"openCampTimeDialog":function(thisObj){
			var that = this;
			// 当前操作的活动内容
			var $ul = $("ul.sortable:visible");
			// 当前可视的活动阶段
			var $allLis = $ul.children();
			// 当前操作的活动阶段数组
			var $lis = $ul.children(":visible");
			var $inputs = $('#PopTime_dataTable').find(':input');
			// 初始化 PopTimeDialog
			$inputs.prop("checked",false);
			$inputs.each(function(){
				var $input = $(this);
				$lis.each(function(){
					var $li = $(this);
					if($input.val() == $li.prop("id")) {
						$input.prop("checked",true);
						if( $li.prop("id") == '3'){
							$input.prop("disabled",true);
						}
						return;
					}
				});
			});
			var dialogSetting = {
				bgiframe: true,
				width:300, 
				height:200,
				zIndex: 90,
				modal: true, 
				title: $("#PopTimeTitle").text(),
				close: function(event, ui) {
					$('#PopTimeDialog').dialog("destroy");
				},
				buttons: [{
					text: $("#global_page_ok").text(),
					click: function() {
						$inputs.each(function(){
							var $input = $(this);
							var $li= $allLis.filter("#"+ $input.val());
							if($input.is(":checked")){
								$li.show();
							}else{
								$li.hide();
								$li.find('.date').val("");
							}
						});
						$(this).dialog("close");
						// 联动-积分扣减时间
						that.changeTime();
					}
				}]
			};
			$('#PopTimeDialog').dialog(dialogSetting);
		},
		//移除活动阶段
		"removeCampTime":function (_this){
			var $thisItem = $(_this).parents("li");
			var $items = $thisItem.parent().find("li");
			$thisItem.hide();
			$thisItem.find('.date').val("");
			// 联动-积分扣减时间
			this.changeTime();
		},
		// 积分扣减时间change
		"changeDeduct":function(_this){
			var $this = $(_this);
			var $ul = $('ul.sortable');
			var $li_1 = $ul.find('#1');
			if($this.val() == "1"){
				$li_1.show();
			}else{
				$li_1.hide();
				$li_1.find('.date').val("");
			}
			this.initReferType($ul);
		},
		// 活动阶段-联动-积分扣减时间
		"changeTime":function(){
			var $ul = $('ul.sortable');
			var $li_1 = $ul.find('#1');
			var $exPointDeductFlag = $('#exPointDeductFlag');
			if($li_1.is(':hidden')){	
				$exPointDeductFlag.val("");
			}else{
				$exPointDeductFlag.val("1");
			}
			this.initReferType($ul);
		},
		// 初始化参考预约日期
		"initReferType":function(ul){
			var $ul = $(ul);
			var $li_1 = $ul.find('#1');
			var $li_3 = $ul.find('#3');
			var $referType = $li_3.find(':input[name="campInfo.referType"]');
			if($li_1.is(':hidden')){
				$referType.find('option[value="5"]').hide();
				if($referType.val() == '5'){
					$referType.val('6');
				}
			}else{
				$referType.find('option[value="5"]').show();
			}
		},
		"setSendFlag":function(){
//			var $campaignType = $('#campaignType');
//			var $sendFlag = $(':input[name="campInfo.sendFlag"]');
//			var $sendFlag1 = $('#campInfo_sendFlag1');
//			var $sendFlag0 = $('#campInfo_sendFlag0');
//			// 活动可修改
//			if(!$('#campInfo_needBuyFlag0').prop('disabled')){
//				$sendFlag.prop('disabled',false);
//				var $td = $sendFlag.parent();
//				$td.find('input:hidden').remove();
//				// 礼品领用类型 && 需要购买
//				if($campaignType.val() == 'LYHD'){
//					var $needBuyFlag = $(':input[name="campInfo.needBuyFlag"]').filter(':checked');
//					if($needBuyFlag.val() == '1'){
//						$sendFlag1.prop('checked',true);
//					}else{
//						$sendFlag0.prop('checked',true);
//					}
//					var val = $td.find(':input[name="campInfo.sendFlag"]').filter(':checked').val();
//					$td.append('<input type="hidden" name="campInfo.sendFlag" value="'+val+'"/>');
//					$sendFlag1.prop('disabled',true);
//					$sendFlag0.prop('disabled',true);
//				}else{
//					$sendFlag1.prop('checked',true);
//				}
//			}
			
		}
};

var BINOLCPCOM05 = new BINOLCPCOM05_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if(BINOLCPCOM05.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(document).ready(function() {
	BINOLCPCOM05.changeSubType('#campaignType');
	BINOLCPCOM05.initReferType('ul.sortable');
	// 活动开始日期
	CAMPAIGN_TEMPLATE.initDate("#campFromDate", "#campToDate", "maxDate");
	// 活动结束日期
	CAMPAIGN_TEMPLATE.initDate("#campToDate", "#campFromDate", "minDate");
	// 预约开始日期
	CAMPAIGN_TEMPLATE.initDate("#campaignOrderFromDate", "#campaignOrderToDate", "maxDate");
	// 预约结束日期
	CAMPAIGN_TEMPLATE.initDate("#campaignOrderToDate", "#campaignOrderFromDate", "minDate");
	// 备货开始日期
	CAMPAIGN_TEMPLATE.initDate("#campaignStockFromDate", "#campaignStockToDate", "maxDate");
	// 备货结束日期
	CAMPAIGN_TEMPLATE.initDate("#campaignStockToDate", "#campaignStockFromDate", "minDate");
	// 领用开始日期
	CAMPAIGN_TEMPLATE.initDate("#obtainFromDate", "#obtainToDate", "maxDate");
	// 领用结束日期
	CAMPAIGN_TEMPLATE.initDate("#obtainToDate", "#obtainFromDate", "minDate");
	cherryValidate({			
		formId: "toNextForm",		
		rules: {		
			"campInfo.campaignName": {required: true, maxlength: 20},	// 活动名称
			"campInfo.descriptionDtl": {maxlength: 300},	// 活动描述
			"campInfo.campaignFromDate":{required: true,dateValid: true},
			"campInfo.campaignToDate":{required: true,dateValid: true},
			"campInfo.campaignOrderFromDate":{dateValid: true},
			"campInfo.campaignOrderToDate":{dateValid: true},
			"campInfo.campaignStockFromDate":{dateValid: true},
			"campInfo.campaignStockToDate":{dateValid: true},
			"campInfo.obtainFromDate":{dateValid: true},
			"campInfo.obtainToDate":{dateValid: true}
		}		
	});
	CAMPAIGN_TEMPLATE.msgHandle($("#ERROR_MSG").html());
	
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLCPCOM05.needUnlock = true;
});