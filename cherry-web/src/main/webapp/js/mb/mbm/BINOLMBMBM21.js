function BINOLMBMBM21() {};

BINOLMBMBM21.prototype = {
    "searchReferList" : function() {
    	var $referCherryForm = $("#referCherryForm");
		var url = $("#searchReferListUrl").attr("href");
		url += "?" + getSerializeToken();
		url += "&" + $referCherryForm.serialize();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#referListDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 7, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "name", "sWidth": "13%", "bSortable": false },
				                { "sName": "memCode", "sWidth": "13%" },
				                { "sName": "mobilePhone", "sWidth": "13%", "bSortable": false },
				                { "sName": "birthDay", "sWidth": "13%", "bSortable": false },
				                { "sName": "standardProvince", "sWidth": "10%", "bSortable": false },
								{ "sName": "standardCity", "sWidth": "10%", "bSortable": false },
								{ "sName": "counterName", "sWidth": "15%" },
								{ "sName": "joinDate", "sWidth": "13%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				//bAutoWidth : true,
				fnDrawCallback : function() {

				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    }
};

var binolmbmbm21 =  new BINOLMBMBM21();

$(function() {
	$('#expandConditionRefer').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#referConditionDiv').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#referConditionDiv').hide();
		}
	});
	binolmbmbm21.searchReferList();
});

