function BINOLMBRPT01() {};

BINOLMBRPT01.prototype = {
		// 导出会员销售报表
		"exportExcel" : function(flag) {
			if($("#dateMode").val() == '1') {
				if(!$('#memSaleCherryForm').valid()) {
					return false;
				}
			}
    		var url = $("#exportUrl").attr("href");
            var param = $("#memSaleCherryForm").serialize();
            url = url + "?" + param;
            document.location.href = url;
	    }
}

var binolmbrpt01 =  new BINOLMBRPT01();

$(function(){
	$('#saleDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#saleDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleDateStart').val();
			return [value,'minDate'];
		}
	});
	$("#dateMode").change(function(){
		if($(this).val() == '0') {
			$(this).parent().next().show();
			$(this).parent().next().next().hide();
		} else {
			$(this).parent().next().hide();
			$(this).parent().next().next().show();
		}
	});
	var channelCounterJson = $("#channelCounterJson").val();
	if(channelCounterJson) {
		var channelCounterList = eval('('+channelCounterJson+')');
		if(channelCounterList.length > 0) {
			for(var i = 0; i < channelCounterList.length; i++) {
				$("#channelId").append('<option value="'+channelCounterList[i].channelId+'">'+channelCounterList[i].channelName+'</option>');
			}
		}
		$("#channelId").change(function(){
			var $counterId = $("#organizationId");
			var channelId = $(this).val();
			var options = '<option value="">'+$counterId.find('option').first().html()+'</option>';
			if(channelId != "") {
				for(var i = 0; i < channelCounterList.length; i++) {
					if(channelCounterList[i].channelId == channelId) {
						var counterList = channelCounterList[i].list;
						if(counterList && counterList.length > 0) {
							for(var j = 0; j < counterList.length; j++) {
								options += '<option value="'+counterList[j].organizationId+'">'+counterList[j].counterName+'</option>';
							}
						}
						break;
					}
				}
			}
			$counterId.html(options);
		});
	}
	
	$("#channelId").change(function(){
		if($(this).val() != '') {
			$("#channelName").val($(this).find("option:selected").text());
		} else {
			$("#channelName").val('');
		}
		$("#organizationName").val('');
	});
	
	$("#organizationId").change(function(){
		if($(this).val() != '') {
			$("#organizationName").val($(this).find("option:selected").text());
		} else {
			$("#organizationName").val('');
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'memSaleCherryForm',
		rules: {
			saleDateStart: {required: true, dateValid: true},
			saleDateEnd: {required: true, dateValid: true}
		}
	});
});