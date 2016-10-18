function BINOLSSPRM68(){};
BINOLSSPRM68.prototype={
		// 页面跳转
		"toNext":function(pageNo,step){
			var $form = $('#mainForm');
			var $step = $('#step');
			$step.val(step);
			this.toNextBefore(pageNo,step);
			$form.submit();
		},
		// 页面跳转下一步前处理
		"toNextBefore":function(pageNo,step){
			if(step == 1){
				if(pageNo == 1){
					PRM68_1.nextBefore();
				}else if(pageNo == 2){
					PRM68_2.nextBefore();
				}else if(pageNo == 3){
					PRM68_3.nextBefore();
				}else if(pageNo == 4){
					PRM68_4.nextBefore();
				}else if(pageNo == 4){
					PRM68_5.nextBefore();
				}
			}
		},
		// 切换逻辑关系
		"changeLogicOpt": function(_this){
			var $this = $(_this);
			var $target = $this.parent().parent();
			var $logicOpt = $target.prev('div').find(':input[name="logicOpt"]');
			var $buttons = $target.children('.LOGICBTN').find('button');
			if($this.prop('class') == 'button_OR'){
				$buttons.prop('class','button_AND');
				$buttons.find('.text').text($('#logicOpt_text_AND').text());
				$logicOpt.val('AND');
			}else{
				$buttons.prop('class','button_OR');
				$buttons.find('.text').text($('#logicOpt_text_OR').text());
				$logicOpt.val('OR');
			}
		},
		"getActGrpInfo": function(_this){
//			var value = $(_this).val();
//			if(!isEmpty(value)){
//				var url = '/Cherry/ss/BINOLSSPRM13_getGroupInfo';
//				param = "prmActGrp=" + value;
//				cherryAjaxRequest({
//					url:url,
//					param:param,
//					callback:function(msg){
//						$('#groupInfo').html(msg);
//						$('#groupInfo').find(':input').each(function(){
//							var $input = $(this);
//							$input.prop('name','pageA.' + $input.prop('name'));
//						});
//						$('#groupInfo').show();
//					}
//				});	
//			}
		},
		"setTemplateFlag":function(val){
			$('#templateFlag').val(val);
		}
}
var PRM68 = new BINOLSSPRM68();
$(document).ready(function() {
	var $actionResultDisplay = $('#actionResultDisplay');
	if($actionResultDisplay.find('li').length > 0){
		$actionResultDisplay.prop('class','actionError');
	}
});