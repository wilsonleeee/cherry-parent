$(function(){
	$('#wechatBindTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#wechatBindTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#wechatBindTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#wechatBindTimeStart').val();
			return [value,'minDate'];
		}
	});
	cherryValidate({
		formId: 'bindCountRptForm',
		rules: {
			wechatBindTimeStart: {dateValid: true},
			wechatBindTimeEnd: {dateValid: true}
		}
	});
	$('#refreshBindCountRpt').click(function(){
		if(!$('#bindCountRptForm').valid()) {
			return false;
		}
		var url = $("#searchTotalGetCountUrl").attr("href");
		var param = $("#bindCountRptForm").serialize();
		var callback = function(msg) {
			$("#bindCountRptDiv").html(msg);
		};
		$("#bindCountRptDiv").html($("#table_sProcessing").html());
		cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback,
    		isResultHandle: false
    	});
		return false;
	});
	$("#exportExcel").click(function(){
		if(!$('#bindCountRptForm').valid()) {
			return false;
		}
		var url = $("#eptCampRptUrl").attr("href");
        var param = $("#bindCountRptForm").serialize();
        url = url + "?" + param;
        document.location.href = url;
		return false;
	});
});