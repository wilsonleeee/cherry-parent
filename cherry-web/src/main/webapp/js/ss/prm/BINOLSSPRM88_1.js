function BINOLSSPRM88_1(){};
BINOLSSPRM88_1.prototype={
		// 添加主活动
		"addPrmActGrop":function(){
			var that = this;
			var $dialog = $('#AddprmActDialog');
			var setting = {
					 width: 350,
					 height: 250, 
					 modal: true,
					 title:'添加主活动',
					 resizable:false,
					 buttons: [{text:'保存',click: function(){that.addActiveGroup();}},
							   {text:'取消',click: function(){$(this).dialog("close");}}]
			};
			$dialog.dialog(setting);
			$('#startDate').cherryDate({
				holidayObj: '',
				beforeShow: function(input){
					return [$('#endDate').val(),'maxDate'];
				}
			});
			$('#endDate').cherryDate({
				holidayObj: '',
				beforeShow: function(input){
					return [$('#startDate').val(),'minDate'];
				}
			});
			$dialog.dialog("open");
			
		},
		"addActiveGroup": function(_this){
			var url = '/Cherry/ss/BINOLSSPRM13_addPrmActiveGrp';
			var $dialog = $('#AddprmActDialog');
			var $prmActGrp = $('#prmActGrp');
			var groupName = $dialog.find(':input[name="groupName"]').val();
			var param = $dialog.find(':input').serialize();
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:function(msg){
					if (msg.indexOf('fieldErrorDiv')<0){
						var htmlTmp = '<option value="'+msg +'">'+groupName+'</option>';
						$('#prmActGrp').append(htmlTmp);
						$prmActGrp.val(msg);
						PRM88.getActGrpInfo($prmActGrp);
						$dialog.dialog("close");
					}
				}
			});
		},
		"nextBefore":function(){
			
		},
		"changeBrand":function(_this){
			var url = $('#BINOLSSPRM13Url').html()+"?pageA.brandInfoId="+$(_this).val()+"&"+getParentToken();
			window.location.href = url;
		}
}
var PRM88_1 = new BINOLSSPRM88_1();
$(document).ready(function() {
	PRM88.getActGrpInfo('#prmActGrp');
	cherryValidate({
		formId : "mainForm",
		rules : {
			'pageA.prmActiveName' : {required: true,maxlength: 20},
			'pageA.prmActGrp':{required: true},
			'pageA.maxExecCount' : {positiveIntegerValid : true,  required: true}
		}
	});
});