var BINOLMOWAT09 = function () {};

BINOLMOWAT09.prototype = {
	/*
	 * 用户查询
	 */
	"search": function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
         // 查询参数序列化
         var params= $("#mainForm").find("div.column").find(":input").serialize();
         params = params + "&csrftoken=" +$("#csrftoken").val();
         params = params + "&" +getRangeParams();
         url = url + "?" + params;
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
				 aoColumns : [	{ "sName": "no","bSortable": false},
				              	{ "sName": "counterCode"},
				              	{ "sName": "counterName"},
								{ "sName": "baCode"},
								{ "sName": "baName"},
								{ "sName": "attendanceDate"},
								{ "sName": "arriveTime"},
								{ "sName": "leaveTime"}
							],
								
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
            params = params + "&" +getRangeParams();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    }
};

var BINOLMOWAT09 = new BINOLMOWAT09();

$(function(){
    // 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
            startAttendanceDate: {dateValid:true},    // 开始日期
            endAttendanceDate: {dateValid:true}   // 结束日期
        }
    });

	$("#export").live('click',function(){
		BINOLMOWAT09.exportExcel();
	});
});