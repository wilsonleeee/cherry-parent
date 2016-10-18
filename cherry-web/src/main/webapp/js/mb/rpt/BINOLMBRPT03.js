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
	
	cherryValidate({
		formId: 'queryForm',
		rules: {
			saleDateStart: {dateValid: true},
			saleDateEnd: {dateValid: true}
		}
	});
	$('#searchButton').click(function(){
		if(!$('#queryForm').valid()) {
			return false;
		}
		var url = $("#searchUrl").attr("href");
		var param = $("#queryForm").serialize();
		var callback = function(msg) {
			$("#memLevelRptDiv").html(msg);
		};
		$("#memLevelRptDiv").html($("#table_sProcessing").html());
		cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback,
    		isResultHandle: false
    	});
		return false;
	});
	$("#exportExcel").click(function(){
		if(!$('#queryForm').valid()) {
			return false;
		}
		var url = $("#exportExcelUrl").attr("href");
        var param = $("#queryForm").serialize();
        url = url + "?" + param;
        document.location.href = url;
		return false;
	});
});