var BINOLCTTPL01_GLOBAL = function() {

};

BINOLCTTPL01_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "templateCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "templateName", "sWidth" : "10%"},
		                    {"sName" : "templateUse", "sWidth" : "5%"},
		                    {"sName" : "contents", "bSortable" : false},
		                    {"sName" : "customerType", "sWidth" : "5%"},
		                    {"sName" : "status", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false} 
		                    ];
		var url = $("#searchTplUrl").attr("href");
		var params = $form.serialize();
		url = url + "?" + params;
		
		$("#templatedetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 2 , 4 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			fnDrawCallback:function() {
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
	
	/**
	 * 停用沟通计划弹出框
	 * @return
	 */
	"disableTemplateDialog":function (templateCode){
		var dialogId = "disable_template_dialog";
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
					//点击确认后执行活动停用
					BINOLCTTPL01.disableTemplate(templateCode);
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
	
	/**
	 * 停用沟通计划
	 */
	"disableTemplate":function (templateCode){
		var url = $('#disableTemplateUrl').html();
		var param = "templateCode="+templateCode;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback: function(data) {
				$("#disable_template_dialog").dialog("close");
				//刷新表格数据
				if(oTableArr[0] != null)
					oTableArr[0].fnDraw();
			}
		});
	}
};

var BINOLCTTPL01 = new BINOLCTTPL01_GLOBAL();

function search() {
	BINOLCTTPL01.search();
}

$(document).ready(
	function() {
		BINOLCTTPL01.search();
	}
);