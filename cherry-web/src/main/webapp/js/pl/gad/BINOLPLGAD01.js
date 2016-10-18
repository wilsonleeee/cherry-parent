var BINOLPLGAD01 = function() {
};
BINOLPLGAD01.prototype = {
		
	"search":function(){
		oTableArr[0] == null;
		var $form = $('#mainForm');
		//if (!$form.valid()) {
			//return false;
		//};
		var url = $("#search_Url").text();
		 // 查询参数序列化
		var param= $form.serialize();
		 url = url + "?" + param;
		 // 显示结果一览
		 $("#section").show();
		// 表格设置
		 var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 排序列
				 aaSorting : [[3, "desc"]],
				 // 表格列属性设置
				 aoColumns : [	
		                    	{ "sName": "no", "sWidth": "5%","bSortable": false,"sClass":"center"},			// 0
		                    	{ "sName": "gadgetName", "sWidth": "30%"},// 1
		                    	{ "sName": "gadgetCode", "sWidth": "30%"},// 2
		                    	{ "sName": "pageId", "sWidth": "25%"}, //3
		                    	{ "sName": "gadgetInfoId", "sWidth": "10%","bSortable": false,"sClass":"center"}
	                    	 ],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index : 0
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
	},	
		
	/*
	 * 弹出新画面
	 */
	"popupWin" : function(obj, optKbn, suffix) {
		$("#actionResultDisplay").empty();
		
		var url = $(obj).attr("href");
		var gadgetInfoId = $(obj).parent().find("#gadgetInfoId").val();
		var param = "?gadgetInfoId=" + gadgetInfoId;
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		url = url + param;
		openWinByUrl(url);
	}
	
};

var binOLPLGAD01 = new BINOLPLGAD01();

//画面初始状态加载
$(document).ready(function() {
	binOLPLGAD01.search();
	$("#searchBut").bind("click",function(){
		binOLPLGAD01.search();
		$("#actionResultDisplay").empty();
	});

	// 数据过滤:画面所属
	$('#pageId').change(function(){	
		binOLPLGAD01.search();
	});	
	// 数据过滤:小工具名称
	$('#gadgetName').keyup(function(){	
		binOLPLGAD01.search();
	});	
	
	
});