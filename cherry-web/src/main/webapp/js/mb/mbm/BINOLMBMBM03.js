
var binolmbMBM03_global = {};
var timeTempVal= {};
binolmbMBM03_global.needUnlock = true;

$(document).ready(function() {
	
	if ($("#box2content").length > 0) {
		searchMB03MemInfo();
	}
	if ($("#levelBody").length > 0) {
		searchMB03Level();
	}
} );
function editMem(){
	var tokenVal = getTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	binolmbMBM03_global.needUnlock = false;
	$("#toEditForm").submit();
}
//会员属性修改历史记录备注提示框
function getPrintTip() {
	var csrftoken = getSerializeToken();
	$('#dataTable').find("a.reason").each(function(){
		var url = $(this).attr("href") + '&' + csrftoken;
		$(this).attr("rel",url);
	});
	$('#dataTable').find("a.reason").cluetip({
	    width: '300',
	    height: 'auto',
	    cluetipClass: 'default',
	    cursor:'pointer',
	    arrows: true, 
	    dropShadow: false});
}
//会员积分修改历史记录备注提示框
function getPointTip() {
	var csrftoken = getSerializeToken();
	$('#dataTable1').find("a.reason1").each(function(){
		var url = $(this).attr("href") + '&' + csrftoken;
		$(this).attr("rel",url);
	});
	$('#dataTable1').find("a.reason1").cluetip({
	    width: '300',
	    height: 'auto',
	    cluetipClass: 'default',
	    cursor:'pointer',
	    arrows: true, 
	    dropShadow: false});
}
function checkedRadio(_this){
	//清除错误消息
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
	var $saveBtn = $("#save");
	//释放按钮
	$saveBtn.removeAttr("disabled",false);
	$saveBtn.removeClass("ui-state-disabled");
	//积分模式
	var value = $(_this).val();
	// 总积分值
	if(value == '1'){
		$('#pointTypeB').hide('slow');
		$('#pointTypeA').show('slow');
		$("#dateTime").prop("disabled",false);
		$("#startHH").prop("disabled",false);
		$("#startMM").prop("disabled",false);
		$("#startSS").prop("disabled",false);
		$("#totalPoint").prop("disabled",false);
		$("#difdateTime").val("");
		$("#difPoint").val("");
		$("#startHour").val("00");
		$("#startMinute").val("00");
		$("#startSecond").val("00");
	}else{
		$("#dateTime").prop("disabled",true);
		$("#startHH").prop("disabled",true);
		$("#startMM").prop("disabled",true);
		$("#startSS").prop("disabled",true);
		$("#totalPoint").prop("disabled",true);
	}
	//积分差值
	if(value == '2'){
		$('#pointTypeA').hide('slow');
		$('#pointTypeB').show('slow');
		$("#difdateTime").prop("disabled",false);
		$("#difPoint").prop("disabled",false);
		$("#startHour").prop("disabled",false);
		$("#startMinute").prop("disabled",false);
		$("#startSecond").prop("disabled",false);
		$("#dateTime").val("");
		$("#startHH").val("00");
		$("#startMM").val("00");
		$("#startSS").val("00");
		$("#totalPoint").val(binolmbMBM03_global.totalPoint);
	}else{
		$("#difdateTime").prop("disabled",true);
		$("#startHour").prop("disabled",true);
		$("#startMinute").prop("disabled",true);
		$("#startSecond").prop("disabled",true);
		$("#difPoint").prop("disabled",true);
	}
}
//积分更新
function updateCurPoint(url){
	if(!$('#mainForm').valid()) {
		return false;
	}
	var value = $('#pointType').val();
	if(value==1){
		//总积分值没有改变是否继续更新
		var totalPoint = $("#totalPoint").val();
		var oldtotalPoint = $("#oldtotalPoint").val();
		if(oldtotalPoint==totalPoint){
			var isconf = confirm($("#pointUnChanged").html());
			if (!isconf) {
				return false;
			}
		}
	}
	var $saveBtn = $("#save");
	// 禁用按钮
	$saveBtn.prop("disabled",true);
	$saveBtn.addClass("ui-state-disabled");
	var param=$('#mainForm').serialize();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
		}
	});
}

function searchMB03MemInfo() {
	var url = $("#searchMemInfoUrl").attr("href");
	var param = $("#memberInfoId").serialize();
	if ($("#memberClubId").length > 0) {
		param +=  '&' + $("#memberClubId").serialize();
	}
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$("#box2content").html(msg);
			$('.tabs').tabs();
			$('.tabs').show();
			$('#pointTypeA').hide();
			$('#pointType').val(2);
			
			//更新前的总积分值
			var totalPoint=$('#totalPoint').val();
			binolmbMBM03_global.totalPoint=$('#totalPoint').val();
			$('#oldtotalPoint').val(totalPoint);
			$("#dateTime").prop("disabled",true);
			$("#totalPoint").prop("disabled",true);
			// 表单验证配置
			cherryValidate({						
				formId: 'mainForm',
				rules: {
				dateTime: {required: true,dateValid:true},//总积分值指定时间
				startHH: {required: true,number: true,maxlength: 2},//指定时间（时）
				startMM: {required: true,number: true,maxlength: 2},//指定时间（分）
				startSS: {required: true,number: true,maxlength: 2},//指定时间（秒）
				startHour: {required: true,number: true,maxlength: 2},//指定时间（时）
				startMinute: {required: true,number: true,maxlength: 2},//指定时间（分）
				startSecond: {required: true,number: true,maxlength: 2},//指定时间（秒）
				difdateTime:{required: true,dateValid:true},//差值积分指定时间
				totalPoint: {required: true,pointValid: [10,2]},//总积分验证
				difPoint: {required: true,validIntNum: true,maxlength: 9},//积分验证
				comments:{required: true,maxlength: 100}//备注
				}
			});
			var $timeTemplate1 = $("#timeTemplate1").find(":input['.number']");
			var $timeTemplate2 = $("#timeTemplate2").find(":input['.number']");	
			timeHHSSMM($timeTemplate1);
			timeHHSSMM($timeTemplate2);
		}
	});
}

function searchMB03Level() {
	var url = $("#searchLevelUrl").attr("href");
	var param = $("#memberInfoId").serialize();
	if ($("#memberClubId").length > 0) {
		param +=  '&' + $("#memberClubId").serialize();
	}
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			$('#levelBody').html(msg);
			//会员属性修改的备注提示框
			getPrintTip();
			//会员积分修改的备注提示框
			getPointTip();
		}
	});
} 