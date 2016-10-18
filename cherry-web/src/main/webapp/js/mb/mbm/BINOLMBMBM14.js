function BINOLMBMBM14() {};

BINOLMBMBM14.prototype = {
	"searchMemInfoRecord" : function() {
		var $memInfoRecordForm = $('#memInfoRecordForm');
		if(!$memInfoRecordForm.valid()) {
			return false;
		}
		var url = $("#memInfoRecordUrl").attr("href");
		url+="?" + getSerializeToken();
		url+="&" + $memInfoRecordForm.serialize();
		$("#memInfoRecordDataTableDiv").show();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#memInfoRecordDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 3, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "memCode", "sWidth": "15%" },
				                { "sName": "modifyType", "sWidth": "10%" },
				                { "sName": "batchNo", "sWidth": "15%" },
				                { "sName": "modifyTime", "sWidth": "15%" },
				                { "sName": "modifyCounter", "sWidth": "15%" },
								{ "sName": "modifyEmployee", "sWidth": "15%" },
								{ "sName": "remark", "sWidth": "10%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				bAutoWidth : true,
				fnDrawCallback : function() {
					$("#memInfoRecordDataTable").find('tr').click(function() {
						binolmbmbm10.searchDetail(this);
					});
					$('#memInfoRecordDataTable').find("a.description").cluetip({
						splitTitle: '|',
					    width: 300,
					    height: 'auto',
					    cluetipClass: 'default', 
					    cursor: 'pointer',
					    showTitle: false
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    // 导出会员修改履历信息
	"exportExcel" : function() {
        if($(".dataTables_empty:visible").length==0) {
        	//alert(oTableArr[0].fnSettings().sAjaxSource)
    		var url = $("#exportUrl").attr("href");
    		url+= "?" + getSerializeToken();
    		url+= "&" + $("#memInfoRecordForm").serialize();
            document.location.href = url;
        }
    }
};

var binolmbmbm14 =  new BINOLMBMBM14();

$(function() {
	$('#modifyTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#modifyTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#modifyTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#modifyTimeStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'memInfoRecordForm',
		rules: {
			modifyTimeStart: {dateValid: true},
			modifyTimeEnd: {dateValid: true}
		}
	});
	counterBinding({elementId:"modifyCounter",showNum:20,selected:"code"});
	employeeBinding({elementId:"modifyEmployee",showNum:20,selected:"code"});
	binolmbmbm14.searchMemInfoRecord();
});