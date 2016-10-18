function search() {
	$("#actionResultDisplay").empty();
	if(!$('#queryForm').valid()) {
		return false;
	}
	var url = $("#searchUrl").attr("href");
	var param = $("#queryForm").serialize();
	url = url + "?" + param;
	$("#section").show();
	// 表格设置
	tableSetting = {
		 // 表格ID
		 tableId : '#dataTable',
		 // 数据URL
		 url : url,
		 // 排序列
		 aaSorting : [[4, "desc"]],
		 // 表格列属性设置
		 aoColumns : [	
                    	{ "sName": "no", "sWidth": "1%","bSortable": false},
                    	{ "sName": "counter"},
                    	{ "sName": "busniessPrincipal"},
                    	{ "sName": "totalMemberNum"},
                    	{ "sName": "totalMemberSaleAmount"},
                    	{ "sName": "newMemberNum","sClass":"alignRight"},
                    	{ "sName": "newMemberSaleAmount","sClass":"alignRight"},
                    	{ "sName": "buyBackMemberNum","sClass":"alignRight"},
                    	{ "sName": "buyBackMemSaleAmount","sClass":"alignRight"},
                    	{ "sName": "newMemberProportion","sClass":"alignRight"},
                    	{ "sName": "newMemConsumeAverage","sClass":"alignRight"}
					],
		 sScrollX : "100%",
		// 不可设置显示或隐藏的列	
		aiExclude :[0,1],
		callbackFun : function (msg){
			var $msg = $("<div></div>").html(msg);
			var $headInfo = $("#headInfo",$msg);
			$("#headInfo").html($headInfo.html());
 		}
	};
	// 调用获取表格函数
	getTable(tableSetting);
}

function exportExcel(){
	$("#actionResultDisplay").empty();
	//无数据不导出
	if($(".dataTables_empty:visible").length==0){
		if(!$('#queryForm').valid()) {
			return false;
		}
		var url = $("#exportExcelUrl").attr("href");
	    var param = $("#queryForm").serialize();
	    url = url + "?" + param;
	    document.location.href = url;
	}
}

$(function(){
	$('#startDate').cherryDate({
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		beforeShow: function(input){
			var value = $('#startDate').val();
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
			startDate: {required:true,dateValid: true},
			endDate: {required:true,dateValid: true}
		}
	});
});