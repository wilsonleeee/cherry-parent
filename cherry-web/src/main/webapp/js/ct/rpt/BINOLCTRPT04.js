var BINOLCTRPT04_GLOBAL = function () {

};

BINOLCTRPT04_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		//清除提示信息
		$('#actionResultDisplay').empty();
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "memCode",  "sWidth" : "5%"},
		                    {"sName" : "memName", "sWidth" : "5%"},
		                    {"sName" : "mobilePhone", "sWidth" : "5%"},
		                    {"sName" : "commType", "sWidth" : "5%"},
		                    {"sName" : "message","bVisible" : false, "sWidth" : "20%", "bSortable" : false},
		                    {"sName" : "sendTime", "sWidth" : "5%"},
		                    {"sName" : "errorType", "sWidth" : "5%"},
		                    {"sName" : "errorText", "sWidth" : "20%","bSortable" : false}
		                    ];
		
		var url = $("#searchUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#sendMsgDetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 6, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [0,2,8 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			fnDrawCallback : function() {
				$('#dataTable').find("a.description").cluetip({
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
	"exportExcel" : function(url,exportFormat){
		//清除提示信息
		$('#actionResultDisplay').empty();
		//无数据不导出
		if($(".dataTables_empty:visible").length==0){
            var params= $("#mainForm").serialize();
            params = params + "&" + getSerializeToken() + "&exportFormat=" +exportFormat;
            if(exportFormat == "0"){
            	var that = this;
    			that.needUnlock=false;
	            url = url + "?" +params;
	            document.location.href = url;
	            that.needUnlock=true;
            }else{
	            exportReport({
					exportUrl: url,
					exportParam: params
				});
            }
		}
	},
	"sendMsg" : function(url){
		//清除提示信息
		$('#actionResultDisplay').empty();
		var params= getSerializeToken();
		cherryAjaxRequest({
			url:url,
			param:params
		});
	}
}

var BINOLCTRPT04 = new BINOLCTRPT04_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if(BINOLCTRPT04.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	var showViewType = $("#showViewType").val();
	if(showViewType == "detail"){
		if (window.opener) {
			window.opener.lockParentWindow();
		}
		BINOLCTRPT04.search();
	}else{
		$("#sendBeginDate").cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("p").find("input[name='sendEndDate']").val();
				return [value, "maxDate"];
			}
		});
		$("#sendEndDate").cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("p").find("input[name='sendBeginDate']").val();
				return [value, "minDate"];
			}
		});
	}
	// 初始化时移除从上一页面带入的错误信息提示DIV
	$("#cluetip-waitimage").remove();
	$("#cluetip").remove();
	
	if(showViewType == "detail"){
		BINOLCTRPT04.needUnlock = true;
	}
});
