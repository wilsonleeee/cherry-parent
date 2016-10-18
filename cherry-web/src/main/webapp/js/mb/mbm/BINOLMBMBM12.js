function BINOLMBMBM12(){};

BINOLMBMBM12.prototype = {
		
	"searchMemInfoRecord" : function() {
		if(!$('#memInfoRecordForm').valid()) {
			return false;
		}
		var url = $("#memInfoRecordUrl").attr("href");
		var params= $("#memInfoRecordForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#memInfoRecord").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#memInfoRecordDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 4, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false },
				                { "sName": "memCode", "sWidth": "10%" },
				                { "sName": "modifyType", "sWidth": "5%" },
				                { "sName": "batchNo", "sWidth": "15%" },
				                { "sName": "modifyTime", "sWidth": "15%" },
				                { "sName": "modifyCounter", "sWidth": "15%" },
								{ "sName": "modifyEmployee", "sWidth": "15%" },
								{ "sName": "remark", "sWidth": "10%" },
								{ "sName": "showDetail", "sWidth": "10%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {
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
            var param = $("#memInfoRecordForm").serialize();
            url = url + "?" + param;
            document.location.href = url;
        }
    }
};

var binolmbmbm12 =  new BINOLMBMBM12();

$(function(){
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
	counterBinding({elementId:"modifyCounter",showNum:20,selected:"code"});
	employeeBinding({elementId:"modifyEmployee",showNum:20,selected:"code"});
	
	// 表单验证初期化
	cherryValidate({
		formId: 'memInfoRecordForm',
		rules: {
			modifyTimeStart: {dateValid: true},
			modifyTimeEnd: {dateValid: true}
		}
	});
});