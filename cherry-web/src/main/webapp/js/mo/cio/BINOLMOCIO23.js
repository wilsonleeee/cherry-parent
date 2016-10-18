var BINOLMOCIO23 = function () {};

BINOLMOCIO23.prototype = {
	"BINOLMOCIO23_Tree":null,
	"messageDatailHtml":null,
	
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
				 // datatable 对象索引
				 index : 1,
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 5, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "no","bSortable": false},
								{ "sName": "messageTitle"},
								{ "sName": "messageType"},
								{ "sName": "startValidDate","sClass": "center"},
								{ "sName": "endValidDate","sClass": "center"},
								{ "sName": "publishDate","sClass": "center"}
//								{ "sName": "status","sClass": "center"}

							],			
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1, 2],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 滚动体的宽度
				sScrollXInner:"",
				// 固定列数
				fixedColumns : 3,
		        fnDrawCallback:function(){
			 		$("#allSelect").prop("checked",false);
//		        	$("#deleteBtn").addClass("ui-state-disabled");
		        }
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},
	/**
	 * 柜台消息数据过滤
	 */
	"cntMsgDateFilter" : function(filterType,thisObj){
		
		$('#ui-tabs').find('li').removeClass('ui-tabs-selected');
		$(thisObj).addClass('ui-tabs-selected');
		// 数据过滤
		oTableArr[1].fnFilter(filterType);

		$('#th_operator').show();
	}
	
	
};


var BINOLMOCIO23 = new BINOLMOCIO23();

$(function(){
	// 表单验证配置
    cherryValidate({
        formId: 'mainForm',
        rules: {
        	startDate: {dateValid:true},    // 开始日期
        	endDate: {dateValid:true}   // 结束日期
    	}
    });
        
	BINOLMOCIO23.search();
	
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });
	
//	$("#deleteBtn").live('click',function(){
////		if(BINOLMOCIO21.isEnable("#deleteBtn")){
//			BINOLMOCIO21.delMsgInit();
////		}
//	});
});