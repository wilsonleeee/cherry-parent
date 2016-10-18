var BINOLMOWAT05_1 = function () {};

BINOLMOWAT05_1.prototype = {
	/*
	 * 用户查询
	 */
	"search": function(){
		 var url = $("#detailListUrl").attr("href");
         // 查询参数序列化
         var params= $("#detailForm").serialize();
         params = params + "&" +getRangeParams() + "&csrftoken="+parentTokenVal();
         url = url + "?" + params;
		 // 显示结果一览
		 $("#detailListSection").show();
		 // 表格设置
		 var tableSetting = {
				 index : 2,
				 // 表格ID
				 tableId : '#detailDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 4, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","bSortable": false},
								{ "sName": "region"},
								{ "sName": "province"},
								{ "sName": "city"},
								{ "sName": "departName"},
								{ "sName": "arriveTime"},
								{ "sName": "leaveTime"},
								{ "sName": "stayMinutes"}
							],
				// 不可设置显示或隐藏的列	
				aiExclude :[0],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
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
        	
            var url = $("#downUrl").attr("href");
            var params = $("#detailForm").serialize();
            params += "&csrftoken=" + parentTokenVal() + "&" +getRangeParams();
            url = url + "?" + params;
            window.open(url,"_self");
        }
    }
};

var BINOLMOWAT05_1 = new BINOLMOWAT05_1();

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
	if (window.opener) {
        window.opener.lockParentWindow();
    }
	
	BINOLMOWAT05_1.search();

	$("#export").live('click',function(){
		BINOLMOWAT05_1.exportExcel();
	});
});