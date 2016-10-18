
function BINOLMBSVC02_03() {
	
};

BINOLMBSVC02_03.prototype = {
		"addLines":function(){
			var trCount=$("#cardTbody tr").length;
			if(trCount > 10){
				return false;
			}
			var html="<tr>";
			html +=$('#cardModel tr').html();
			html += "</tr>";
			$('#cardTbody').append(html);
		},
		
		/**
		 * 校验柜台是否已经输入
		 */
		"checkCounter" : function(obj) {
			var counterCode = $('#counterName').val();
			if(counterCode == undefined || counterCode == null || counterCode == 'null' || counterCode =='') {
				BINOLMBSVC02_03.addErrorMessage($('#counterName').parent(), "请先选择柜台号");
				return false;
			} else {
				$('#counterName').parent().find('span').remove();
				$('#counterName').parent().attr("class","");
			}
		},
		
		"checkCard":function(obj){
			$(obj).parent().find('span').remove();
			$(obj).parent().attr("class","");
			$(obj).parent().parent().parent().find('td:eq(4) span').text('');
			var cardCode=$(obj).val();
			if(cardCode == null || cardCode == 'null' || cardCode == undefined || cardCode ==''){
				return false;
			}
			var checkCardUrl=$('#checkCardUrl').attr("href");
			var params="cardCode="+cardCode+"&counterCode="+$('#counterName').val();
			cherryAjaxRequest({
				url: checkCardUrl,
				param:params,
				callback: function(data) {
					if(0 == data){
						BINOLMBSVC02_03.addErrorMessage($(obj).parent(), '储值卡卡号存在');
						return false;
					}else{
						$(obj).parent().find('span').remove();
						$(obj).parent().attr("class","");
					}
				}
			});
		},
		"confirm":function(){
			var counterCode=$('#counterName').val();
			if(counterCode == undefined || counterCode == null || counterCode == 'null' || counterCode ==''){
				BINOLMBSVC02_03.addErrorMessage($('#counterName').parent(), "请输入柜台号");
				return false;
			} else {
				var $counter_parent = $('#counterName').parent();
				$counter_parent.find('span').remove();
				$counter_parent.attr("class","");
			}
			var all=new Array();
			$("#cardTbody tr").each(function(){
				var $cardCode=$(this).find("input[name='cardCode']");
				var $rechargeValue=$(this).find("input[name='rechargeValue']");
				var $cardState=$(this).find("#cardState");
				var $cardPassword=$(this).find("input[name='cardPassword']");
				var cardCode=$(this).find("input[name='cardCode']").val();
				var rechargeValue=$(this).find("input[name='rechargeValue']").val();
				var cardState=$(this).find("#cardState").val();
				var cardPassword=$(this).find("input[name='cardPassword']").val();
				if(cardCode == null || cardCode == 'null' || cardCode == undefined || cardCode ==''){
					BINOLMBSVC02_03.addErrorMessage($cardCode.parent(), "请输入卡号");
					return false;
				}
				if(isNaN(cardCode)){
					BINOLMBSVC02_03.addErrorMessage($cardCode.parent(), "请输入数字");
					return false;
				}
				if(rechargeValue == null || rechargeValue == 'null' || rechargeValue == undefined || rechargeValue ==''){
					BINOLMBSVC02_03.addErrorMessage($rechargeValue.parent(), "请输入充值金额");
					return false;
				}
				if(isNaN(rechargeValue)){
					BINOLMBSVC02_03.addErrorMessage($rechargeValue.parent(), "请输入数字");
					return false;
				}
				if(cardState == null || cardState == 'null' || cardState == undefined || cardState ==''){
					BINOLMBSVC02_03.addErrorMessage($cardState.parent(), "请选择储值卡状态");
					return false;
				}
				if(cardPassword == null || cardPassword == 'null' || cardPassword == undefined || cardPassword ==''){
					BINOLMBSVC02_03.addErrorMessage($cardPassword.parent(), "请输入储值卡密码");
					return false;
				}
				var map={
						"CounterCode":counterCode,
						"CardCode":cardCode,
						"RechargeValue":rechargeValue,
						"State":cardState,
						"Password":cardPassword
					};
				all.push(map);
			});
			var cardArr=JSON.stringify(all);
			var params ="cardArr="+cardArr;
			var length=$(".error").length;
			if(length == 0){
				var openCardUrl=$("#openCardUrl").attr("href");
				cherryAjaxRequest({
					url: openCardUrl,
					param:params,
					callback: function(data) {
						var rusult_map = eval("("+data+")");
						var errorCode=rusult_map.ERRORCODE;
						var errorMsg=rusult_map.ERRORMSG;
						var resultContent_list=rusult_map.resultContent_list;
						var result_dialog_success=$('#result_dialog_success').text();
						/*if(resultContent_list == null || resultContent_list == 'null' || resultContent_list == undefined || resultContent_list ==''){*/
						if("0"==errorCode){
							var dialogId = "result_success";
							var $dialog = $('#' + dialogId);
							$dialog.find('span').text(result_dialog_success);
							$dialog.dialog({ 
								//默认不打开弹出框
								autoOpen: false,  
								//弹出框宽度
								width: 350, 
								//弹出框高度
								height: 250, 
								//弹出框标题
								title:$("#result_dialog_success").text(), 
								//弹出框索引
								zIndex: 1,  
								modal: true, 
								resizable:false,
								//弹出框按钮
								buttons: [{
									text:$("#dialogConfirm").text(),//确认按钮
									click: function() {
										closeCherryDialog('dialogInit');
										// 清空弹出框内容
										$('#dialogInit').html("");
										$dialog.dialog("close");
										if(oTableArr[1] != null)oTableArr[1].fnDraw();
									}
								}],
								//关闭按钮
								close: function() {
									closeCherryDialog(dialogId);
									if(oTableArr[1] != null)oTableArr[1].fnDraw();
								}
							});
							$dialog.dialog("open");
						}else{
							var result_dialog_error=$('#result_dialog_error').text();
							var dialogId = "result_error";
							var $dialog = $('#' + dialogId);
							$dialog.find('span').text(result_dialog_error);
							$dialog.dialog({ 
								//默认不打开弹出框
								autoOpen: false,  
								//弹出框宽度
								width: 350, 
								//弹出框高度
								height: 250, 
								//弹出框标题
								title:$("#result_dialog_error").text(), 
								//弹出框索引
								zIndex: 1,  
								modal: true, 
								resizable:false,
								//弹出框按钮
								buttons: [{
									text:$("#dialogConfirm").text(),//确认按钮
									click: function() {
										$dialog.dialog("close");
										if(oTableArr[1] != null)oTableArr[1].fnDraw();
									}
								}],
								//关闭按钮
								close: function() {
									BINOLMBSVC02_03.addErrorCardList(resultContent_list);
									closeCherryDialog(dialogId);
									if(oTableArr[1] != null)oTableArr[1].fnDraw();
								}
							});
							$dialog.dialog("open");
						}
					}
				});
			}
		},
		"cancel":function(){
			closeCherryDialog('dialogInit');
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
		},
		"delLine":function(obj){
			var length=$('#cardTbody tr').length;
			if(length == 1){
				return false;
			}
			$(obj).parent().parent().parent().remove();
		},
		"mouseleaveFc":function(obj){
//			$('#countCode').attr("class","");
			$(obj).parent().find('span').remove();
			$(obj).parent().attr("class","");
		},
		"addErrorCardList":function(obj){
			$('#cardTbody tr').remove();
			$.each(obj,function(i){
				BINOLMBSVC02_03.addLines();
				var $lastTr=$('#cardTbody tr:last');
				$lastTr.find('input[name="cardCode"]').val(obj[i].CardCode);
				$lastTr.find('input[name="rechargeValue"]').val(obj[i].RechargeValue);
				$lastTr.find('slelect[name="cardState"]').val(obj[i].State);
				$lastTr.find('slelect[name="cardPassword"]').val(obj[i].Password);
				$lastTr.find('td:eq(4) span').attr("class","text").text(obj[i].ErrorMessage);
//				var $parent=$lastTr.find('input[name="cardCode"]').parent();
//				BINOLMBSVC02_03.addErrorMessage($parent,obj[i].ErrorMessage);
			});
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

var BINOLMBSVC02_03 =  new BINOLMBSVC02_03();

$(document).ready(function() {
	BINOLMBSVC02_03.addLines();
	$('#cardState option').each(function(){
		var obj=$(this).val();
		if("ST" == obj || "FZ" == obj || '' == obj){
			$(this).remove();
		}
	});
	// 柜台下拉框绑定
	var option = {
			elementId:"counterName",
			showNum:20,
			targetId:"counterCode",
			targetDetail:true,
			privilegeFlag:1
	};
	counterSelectBinding(option);
});
