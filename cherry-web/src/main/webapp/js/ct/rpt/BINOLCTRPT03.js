var BINOLCTRPT03_GLOBAL = function () {

};

BINOLCTRPT03_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		//清除提示信息
		$('#actionResultDisplay').empty();
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "planCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "planName", "sWidth" : "5%"},
		                    {"sName" : "runType", "bVisible" : false, "bSortable": false, "sWidth" : "5%"},
		                    {"sName" : "memCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "memName", "sWidth" : "5%"},
		                    {"sName" : "joinDate", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "birthDay", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "counterCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "counterName", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "rsm", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "amm", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "mobilePhone", "sWidth" : "5%"},
		                    {"sName" : "message", "sWidth" : "20%", "bSortable" : false},
		                    {"sName" : "sendTime", "sWidth" : "5%"},
		                    {"sName" : "couponCode", "sWidth" : "5%"},
		                    {"sName" : "act", "sWidth" : "5%", "bSortable" : false}
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
			aaSorting : [ [ 14, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 5 , 12 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			fnDrawCallback : function() {
				$('#dataTable').find("a.description").cluetip({
					splitTitle: '|',
				    width: 500,
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
		var params= getSerializeToken();
		cherryAjaxRequest({
			url:url,
			param:params
		});
	},
	"getSendDialog" : function(url){
		//清除提示信息
		$('#actionResultDisplay').empty();
		if($("#sendDialog").length == 0) {
    		$("body").append('<div style="display:none" id="sendDialog"></div>');
    	} 
		$('#sendDialog').html($('#sendDialogContent').html());
		var dialogId = "sendDialog";
		var $dialog = $('#' + dialogId);
		$dialog.dialog({ 
			//默认不打开弹出框
			autoOpen: false,  
			//弹出框宽度
			width: 350, 
			//弹出框高度
			height: 250, 
			//弹出框标题
			title:$("#dialogTitle").text(), 
			//弹出框索引
			zIndex: 1,  
			modal: true, 
			resizable:false,
			//弹出框按钮
			buttons: [{
				text:$("#dialogConfirm").text(),//确认按钮
				click: function() {
					BINOLCTRPT03.sendMsg(url);
					$dialog.dialog("close");
				}
			},
			{	
				text:$("#dialogCancel").text(),//取消按钮
				click: function() {
					$dialog.dialog("close");
				}
			}],
			//关闭按钮
			close: function() {
				closeCherryDialog(dialogId);
			}
		});
		$dialog.dialog("open");
	},
	"changeRunType" : function(thisObj){
		var index = $(thisObj).val();
		$('#runType_all').find('p').hide();
		$('#runType_all').find(':input').val("");
		$('#runType_all').find('#runType_'+index).show();
		$('#runType_all').show();
	}
}

var BINOLCTRPT03 = new BINOLCTRPT03_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if(BINOLCTRPT03.needUnlock){
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
		BINOLCTRPT03.search();
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
		BINOLCTRPT03.needUnlock = true;
	}
});
