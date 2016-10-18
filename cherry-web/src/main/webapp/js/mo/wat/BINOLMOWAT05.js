var BINOLMOWAT05 = function () {};

BINOLMOWAT05.prototype = {
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
         var params= $("#mainForm").serialize();
         url = url + "?" + params;
		 // 显示结果一览
		 $("#section").show();
		 // 表格设置
		 var tableSetting = {
				 index : 1,
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 1, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","bSortable": false},
								{ "sName": "employeeName"},
								{ "sName": "region"},
								{ "sName": "province"},
								{ "sName": "city"},
								{ "sName": "categoryName"},
								{ "sName": "udiskSN"},
								{ "sName": "arrCntCount"},
								{ "sName": "arrCntSum"},
								{ "sName": "stayMinutesSum"}
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
	 * 岗位类别
	 * 
	 * Inputs:  Object:obj      品牌
	 *          String:url      AJAX请求地址
	 *          
	 * 
	 */
	"doPositionCategory":function(obj, url) {
	    $("#positionCategoryId").find("option:not(:first)").remove();
	    if ("" == $(obj).val()) {
	        return false;
	    }
		// 参数(品牌信息ID)
        var params = $("#mainForm").serialize();
	    doAjax(url, "positionCategoryId", "categoryName", "#positionCategoryId", params);
	},
	
	/* 
     * 导出Excel
     */
	"exportExcel" : function(flag){
		
		//无数据不导出
        if($(".dataTables_empty:visible").length==0){
		    if (!$('#mainForm').valid()) {
                return false;
            };
            var url = $("#downUrl").attr("href");
            var params= $("#mainForm").serialize();
            // 带人员权限的导出数据
            params += "&flag="+flag;
            url = url + "?" + params;
            window.open(url,"_self");
        }
    }
	
};

var BINOLMOWAT05 = new BINOLMOWAT05();

$(function(){
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });

    // 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
            startAttendanceDate: {dateValid:true},    // 开始日期
            endAttendanceDate: {dateValid:true}   // 结束日期
        }
    });

	//BINOLMOWAT05.search();

    //明细数据导出
    $("#exportDetail").live('click',function(){
		BINOLMOWAT05.exportExcel('1');
	});
    // 统计信息导出
    $("#exportCount").live('click',function(){
		BINOLMOWAT05.exportExcel('2');
	});
});