
function BINOLCPACT11() {
	
};

BINOLCPACT11.prototype = {
		"search" : function(url) {
			var $form = $('#mainForm');
			// 表单验证
			if(!$form.valid()){
				return;
			}
			var $text = $form.find(':text');
			$text.each(function(){
				$(this).val($.trim($(this).val()));
			});
			url += "?" + getSerializeToken();
			url += "&" + $form.serialize();
			// 显示结果一览
			$("#section").show();
			 // 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no.","bSortable":false},//1
									{ "sName": "tradeNoIF"},//2
									{ "sName": "batchNo"},//3
									{ "sName": "subCampaignName","bVisible": false},//4
									{ "sName": "memName"},//5
									{ "sName": "mobilePhone"},//6
//									{ "sName": "departName"},//7
									{ "sName": "optTime"},//8
									{ "sName": "state","sClass":"center"}//9
								],       
									
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		}
};

var BINOLCPACT11 =  new BINOLCPACT11();
//初始化
$(document).ready(function() {
	counterBinding({
		elementId:"departName",
		showNum:20,
		selected :"code"
	});
	// 日期验证
	cherryValidate({
		formId : 'mainForm',
		rules : {
			startDate : {
				dateValid : true
			},
			endDate : {
				dateValid : true
			}
		}
	});
});
