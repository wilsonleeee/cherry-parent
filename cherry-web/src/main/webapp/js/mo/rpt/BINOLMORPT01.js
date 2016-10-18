
function BINOLMORPT01() {};

BINOLMORPT01.prototype = {	
	// 考核报表查询
	"search" : function(){
		var $form = $("#mainForm");
		// 表单验证
		if(!$form.valid()){
			return;
		}	 
		var url = $("#search_url").val();
		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [{ "sName": "number","sWidth": "5%","bSortable": false},
					{ "sName": "paperName","sWidth": "20%"},
					{ "sName": "departName","sWidth": "20%"},
					{ "sName": "employeeName","sWidth": "15%"},
					{ "sName": "ownerName","sWidth": "10%"},
					{ "sName": "checkDate","sWidth": "10%"},
					{ "sName": "totalPoint","sWidth": "10%"},
					{ "sName": "button","sWidth": "10%","bSortable": false,"sClass":"center"}],	
			// 不可设置显示或隐藏的列
			aiExclude :[0, 1],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 2
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"popCheckPaper" : function(object){
		var confirmClick = function(){
			var $checked = $('#checkPaper_dataTable').find(":input[checked]");
			if($checked.length > 0) {
				$("#checkPaperId").val($checked.val());
				$("#paperName").val($checked.parent().next().text());
			} else {
				$("#checkPaperId").val("");
				$("#paperName").val("");
			}
		};
		popDataTableOfCheckPaper(object,null,confirmClick);
	},
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(){
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").find("div.column").find(":input").serialize();
            params = params + "&csrftoken=" +$("#csrftoken").val();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    }
};

var binolmorpt01 =  new BINOLMORPT01();



$(document).ready(function() {
	//日期验证
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			checkDateStart: {dateValid: true},
			checkDateEnd: {dateValid: true}
		}
	});
	// 考核报表查询
	binolmorpt01.search();
	
	$("#export").live('click',function(){
		binolmorpt01.exportExcel();
	});
});

