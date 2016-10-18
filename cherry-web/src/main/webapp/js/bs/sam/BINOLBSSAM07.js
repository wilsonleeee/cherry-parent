var BINOLBSSAM07_GLOBAL = function() {

};

BINOLBSSAM07_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		$form.find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		/*if(!$form.valid()){
			return false;
		}*/
		//表格列属性
		var aoColumnsArr = [
		                    {"sName":"No", "sWidth":"5%","bSortable":false},
		                    {"sName":"departName", "sWidth":"20%"},
		                    {"sName":"employeeName", "sWidth":"20%"},
		                    {"sName":"workDate", "sWidth":"20%"},
		                    {"sName":"attendanceType", "sWidth":"15%"},
		                    {"sName":"attendanceDateTime", "sWidth":"20%"}
		                    ];
		var url = $("#searchUrl").attr("href");
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		$("#bAAttendanceSection").show();
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 1,
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 3, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 0,
			fnDrawCallback : function() {
				
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	"exportExcel" : function(url){
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
			if (!$('#mainForm').valid()) {
		        return false;
		    };
		    var params= $("#mainForm").serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
            window.open(url,"_self");
		}
	}
	
};

var BINOLBSSAM07 = new BINOLBSSAM07_GLOBAL();
/**
* 页面初期处理
*/

$.fn.selectRange = function(start, end) {
	return this.each(function() {
		if (this.setSelectionRange) {
			this.focus();
			this.setSelectionRange(start, end);
		} else if (this.createTextRange) {
			var range = this.createTextRange();
			range.collapse(true);
			range.moveEnd('character', end);
			range.moveStart('character', start);
			range.select();
		}
	});
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLBSSAM07.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLBSSAM07.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};
$(document).ready(function(){
	$('#startDateTime').cherryDate({
	    beforeShow: function(input){
	        var value = $('#endDateTime').val();
	        return [value,'maxDate'];
	    }
	});
	$('#endDateTime').cherryDate({
	    beforeShow: function(input){
	        var value = $('#startDateTime').val();
	        return [value,'minDate'];
	    }
	});
	$('.ui-tabs').tabs();
	BINOLBSSAM07.needUnlock = true;
	BINOLBSSAM07.search();
});

