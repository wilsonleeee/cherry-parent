var BINOLCTTPL04_GLOBAL = function() {

};

BINOLCTTPL04_GLOBAL.prototype = {
	"delError" : function(){
		$("#actionResultDisplay").empty();
	},
	// 非法字符查询
	"search" : function() {
		var $form = $('#mainForm');
		$form.find(":input").each(function(){
			$(this).val($.trim(this.value));
		});
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "charValue", "sWidth" : "15%"},
		                    {"sName" : "commType", "sWidth" : "15%"},
		                    {"sName" : "remark", "sWidth" : "20%"},
		                    {"sName" : "validFlag", "sWidth" : "5%","sClass":"center" },
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false, "sClass":"center"} 
		                    ];
		var url = $("#searchUrl").attr("href");
		var params = $form.serialize();
		url = url + "?" + params;
		$("#illegalCharDetail").show();
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
			aiExclude : [ 0 , 1 , 5 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			//fixedColumns : 3,
			fnDrawCallback:function() {
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
	//非法字符添加画面初始化
	"initAddDialog" : function(url){
		$that = this;
		$that.delError();
		var callback = function(msg) {
			if ($("#add_dialog").length == 0) {
				$("body").append(
						'<div style="display:none" id="add_dialog"></div>');
			} else {
				$("#add_dialog").empty();
			}
			var dialogId = "add_dialog";
			var $dialog = $('#' + dialogId);
			var dialogTitle = $('#addDialogTitle').text();
			var dialogContent = msg;
			$dialog.html(dialogContent);
			$dialog.dialog({
				// 默认不打开弹出框
				autoOpen : false,
				// 弹出框宽度
				width : 550,
				// 弹出框高度
				height : 300,
				// 弹出框标题
				title : dialogTitle,
				// 弹出框索引
				zIndex : 1,
				modal : true,
				resizable : false,
				// 弹出框按钮
				buttons : [
						{
							text : $("#dialogConfirm").text(),// 确认按钮
							click : function() {
								$that.add();
							}
						}, {
							text : $("#dialogCancel").text(),// 取消按钮
							click : function() {
								removeDialog('#' + dialogId);
							}
						} ],
				// 关闭按钮
				close : function() {
					removeDialog('#' + dialogId);
				}
			});
			$dialog.dialog("open");
		}
		cherryAjaxRequest({
			url : url,
			param : null,
			callback : callback
		});
	},
	//添加非法字符
	"add" : function(){
		var $that = this;
		cherryValidate({//form表单验证
			formId: "addForm",		
			rules: {
				charValue:{required: true,maxlength:50},
				remark:{required: true,maxlength:200}
			}		
		});
		var $form = $('#addForm');
		$form.find(":input").each(function(){
			$(this).val($.trim(this.value));
		});
		if(!$("#addForm").valid()){
			return false;
		}
		var addUrl = $("#addUrl").attr("href");
		var addParams = $("#addForm").serialize();
		cherryAjaxRequest({
			url : addUrl,
			param : addParams,
			callback : function(msg){
				var $msg = $("<div>"+msg+"</div>");
				if(msg.indexOf("fieldErrorDiv") == -1){
					removeDialog("#add_dialog");
					$that.search();
				}
			}
		});
	},
	//停用启用画面
	"initDisable" : function(url,validFlag){
		var $that = this;
		$that.delError();
		if ($("#disable_dialog").length == 0) {
			$("body").append(
					'<div style="display:none" id="disable_dialog"></div>');
		} else {
			$("#disable_dialog").empty();
		}
		var dialogId = "disable_dialog";
		var $dialog = $('#' + dialogId);
		var dialogTitle = "";
		var dialogContent = "";
		if (validFlag == '0') {
			dialogTitle = $("#disableDialogTitle").text();
			dialogContent = $('#disableDialogContent').html();
		} else {
			dialogTitle = $("#enableDialogTitle").text();
			dialogContent = $('#enableDialogContent').html();
		}
		$dialog.html(dialogContent);
		$dialog.dialog({
			// 默认不打开弹出框
			autoOpen : false,
			// 弹出框宽度
			width : 400,
			// 弹出框高度
			height : 250,
			// 弹出框标题
			title : dialogTitle,
			// 弹出框索引
			zIndex : 1,
			modal : true,
			resizable : false,
			// 弹出框按钮
			buttons : [
					{
						text : $("#dialogConfirm").text(),// 确认按钮
						click : function() {
							removeDialog("#disable_dialog");
							cherryAjaxRequest({
								url : url,
								param : null,
								callback : function(msg){
									removeDialog('#' + dialogId);
									$that.search();
								}
							});
						}
					}, {
						text : $("#dialogCancel").text(),// 取消按钮
						click : function() {
							removeDialog("#disable_dialog");
						}
					} ],
			// 关闭按钮
			close : function() {
				removeDialog('#' + dialogId);
			}
		});
		$dialog.dialog("open");
	},
	//非法字符添加画面初始化
	"initEditDialog" : function(url){
		$that = this;
		$that.delError();
		var callback = function(msg) {
			if ($("#edit_dialog").length == 0) {
				$("body").append(
						'<div style="display:none" id="edit_dialog"></div>');
			} else {
				$("#edit_dialog").empty();
			}
			var dialogId = "edit_dialog";
			var $dialog = $('#' + dialogId);
			var dialogTitle = $('#editDialogTitle').text();
			var dialogContent = msg;
			$dialog.html(dialogContent);
			$dialog.dialog({
				// 默认不打开弹出框
				autoOpen : false,
				// 弹出框宽度
				width : 550,
				// 弹出框高度
				height : 300,
				// 弹出框标题
				title : dialogTitle,
				// 弹出框索引
				zIndex : 1,
				modal : true,
				resizable : false,
				// 弹出框按钮
				buttons : [
						{
							text : $("#dialogConfirm").text(),// 确认按钮
							click : function() {
								$that.edit();
							}
						}, {
							text : $("#dialogCancel").text(),// 取消按钮
							click : function() {
								removeDialog('#' + dialogId);
							}
						} ],
				// 关闭按钮
				close : function() {
					removeDialog('#' + dialogId);
				}
			});
			$dialog.dialog("open");
		}
		cherryAjaxRequest({
			url : url,
			param : null,
			callback : callback
		});
	},
	"edit" : function(){
		var $that = this;
		cherryValidate({//form表单验证
			formId: "editForm",		
			rules: {
				charValue:{required: true,maxlength:50},
				remark:{required: true,maxlength:200}
			}		
		});
		var $form = $('#editForm');
		$form.find(":input").each(function(){
			$(this).val($.trim(this.value));
		});
		if(!$("#editForm").valid()){
			return false;
		}
		var updateUrl = $("#updateUrl").attr("href");
		var updateParams = $("#editForm").serialize();
		cherryAjaxRequest({
			url : updateUrl,
			param : updateParams,
			callback : function(msg){
				var $msg = $("<div>"+msg+"</div>");
				if(msg.indexOf("fieldErrorDiv") == -1){
					removeDialog("#edit_dialog");
					$that.search();
				}
			}
		});
	}
};

var BINOLCTTPL04 = new BINOLCTTPL04_GLOBAL();

$(document).ready(
	function() {
		BINOLCTTPL04.search();
	}
);