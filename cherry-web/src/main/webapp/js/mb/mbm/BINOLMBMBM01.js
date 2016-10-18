
function BINOLMBMBM01() {
	
};

BINOLMBMBM01.prototype = {
		
	"searchMember" : function() {
		var url = $("#memberListUrl").attr("href");
		var params= $("#memberCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#memberInfo").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#dateTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 5, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false },
				                { "sName": "name", "sWidth": "20%" },
				                { "sName": "memCode", "sWidth": "20%" },
				                { "sName": "mobilePhone", "sWidth": "20%" },
				                { "sName": "levelName", "sWidth": "15%" },
								{ "sName": "joinDate", "sWidth": "20%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%"
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    "changeLevel" : function (obj) {
    	var clubId = $(obj).val();
    	if ("" != clubId) {
    		var url = $("#searchLevelUrl").attr("href");
    		var param = $("#memberClubId").serialize() + "&" + $("#memberInfoId").serialize() + "&" + "csrftoken=" + getTokenVal();
    		$.ajax({
    	        url: url, 
    	        type: 'post',
    	        data: param,
    	        success: function(msg){
    						if(window.JSON && window.JSON.parse) {
    							var msgJson = window.JSON.parse(msg);
    							if (msgJson) {
    								var htm = "";
	    					    	for (var one in msgJson){
	    					    		htm += '<input type="checkbox" id="memLevel-"' + one + 'value="' + msgJson[one]["memberLevelId"] +  '" name="memLevel">' +
	    					    		'<label class="checkboxLabel" for="memLevel-' + one + '">' + msgJson[one]["levelName"] + '</label>'
	    							    
	    						    }
	    					    	$("#levelSpan").html(htm);
    							}
    						}
    					}
    	    });
    	} else {
    		$("#levelSpan").empty();
    	}
    }
};


var binolmbmbm01 =  new BINOLMBMBM01();

$(function(){
	$('#expandCondition').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#moreCondition').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#moreCondition').hide();
		}
	});
	counterBinding({elementId:"counterCode",showNum:"20",selected:"code"});
	counterBinding({elementId:"saleCounterCode",showNum:"20",selected:"code"});
	$("#birthDayMonth").change(function(){
		var month = $(this).val();
		var i = 1;
		var max = 0;
		var options = '<option value="">'+$('#birthDayDate').find('option').first().html()+'</option>';
		if(month == "") {
			$('#birthDayDate').html(options);
			return;
		}
		if(month == '2') {
			max = 29;
		} else if(month == '4' || month == '6' || month == '9' || month == '11') {
			max = 30;
		} else {
			max = 31;
		}
		for(i = 1; i <= max; i++) {
			options += '<option value="'+i+'">'+i+'</option>';
		}
		$('#birthDayDate').html(options);
	});
});