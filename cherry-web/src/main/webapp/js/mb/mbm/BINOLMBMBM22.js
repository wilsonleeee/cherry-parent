function BINOLMBMBM22() {};

BINOLMBMBM22.prototype = {
    "searchAnswerList" : function() {
    	var $answerCherryForm = $("#answerCherryForm");
		var url = $("#searchAnswerListUrl").attr("href");
		url+="?"+ getSerializeToken();
		url+="&"+ $answerCherryForm.serialize();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#answerListDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 3, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "paperName", "sWidth": "25%" },
				                { "sName": "counterName", "sWidth": "25%" },
				                { "sName": "employeeName", "sWidth": "25%" },
				                { "sName": "checkDate", "sWidth": "25%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				//bAutoWidth : true,
				fnDrawCallback : function() {
					$("#answerListDataTable").find('tr').click(function() {
						binolmbmbm10.searchDetail(this);
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    }
};

var binolmbmbm22 =  new BINOLMBMBM22();

$(function() {
	
	$('#checkDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#checkDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#checkDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#checkDateStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'answerCherryForm',
		rules: {
			checkDateStart: {dateValid: true},
			checkDateEnd: {dateValid: true}
		}
	});
	binolmbmbm22.searchAnswerList();
});

