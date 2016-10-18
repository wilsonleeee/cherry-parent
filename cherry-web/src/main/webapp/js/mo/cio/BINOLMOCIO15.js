/*
 * 全局变量定义
 */
var binOLMOCIO15_global = {};
function BINOLMOCIO15(){};

BINOLMOCIO15.prototype = {
		/**
		 * 查询
		 */
		"search" : function(msg) {
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			if(typeof msg != "undefined"){
				$("#actionResultDisplay").html(msg);
				$("#actionResultDisplay").show();
			}else{
				$("#actionResultDisplay").empty();
			}
			var $form = $('#mainForm');
			if (!$form.valid()) {
				return false;
			};

			var url = $("#searchUrl").attr("href");
			 // 查询参数序列化【包含了csrftoken值】
			var params= $form.serialize();
			 url = url + "?" + params;
			 // 显示结果一览
			 $("#section").show();
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 数据URL
					 url : url,
					 // 表格列属性设置			 
					 aoColumns :    [{ "sName": "no","bSortable": false},			// 0
					                 { "sName": "rivalNameCN"},						// 1
									 { "sName": "rivalNameEN"},						// 2
					                 { "sName": "operate", "bSortable": false, "sClass": "center"}							// 3
					                 ],					
					                     
					 // 不可设置显示或隐藏的列	
				   	aiExclude :[0, 1, 2, 3],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					callbackFun : function (){
			        $('#allSelect').prop("checked", false);
			 		}
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/**
		 * 弹出添加dialog
		 * 
		 * 
		 */
		"showChoiceDialog" : function(object) {
			var that = this;
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
			
			var param = $(object).parent().find(':input').serialize();
			param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
			$("#addRival").dialog( {
				resizable : false,
				modal : true,
				title: 	$("#addRivalTitle").text(),
				height : 180,
				width : 340,
				zIndex: 30,  
				buttons: [{
						text: $("#dialogConfirm").text(),
					    click: function(){
					    		that.mocioconfirm($("#addRivalUrl").attr("href"),param);
					    	}
					},
				    {
						text: $("#dialogCancel").text(),
						click: function(){
							closeCherryDialog('addRival',dialogBody);
							}
					}],
				    close: function(){
				    	closeCherryDialog('addRival',dialogBody);
				    }
			});
			$("#addRivalNameCN").val("");
			$("#addRivalNameEN").val("");
			$("#addRival").dialog("open");
			cherryValidate({
				formId: 'cherryvalidate',	
				rules: {
					addbrandInfoId:	{required: true},
					addRivalNameCN: {required: true,maxlength: 20},
					addRivalNameEN: {maxlength: 20}
				}	
			});
		},
		
		/**
		 * 确认添加竞争对手
		 */
		"mocioconfirm" : function(url,param) {
			if(!$('#cherryvalidate').valid()) {
				return false;
			}
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
						closeCherryDialog('addRival',dialogBody);
						BINOLMOCIO15.search(msg);
					}
			};
			param += "&"+$("#addRivalNameCN").serialize();
			param += "&"+$("#addRivalNameEN").serialize();
			param += "&"+$("#addbrandInfoId").serialize();
			cherryAjaxRequest({
				url: url,
				param: param,
				formId: '#cherryvalidate',
				callback: callback
			});	
		},
		
		/**
		 * 弹出编辑竞争对手对话框
		 */
		"popEditDialog" : function(obj){
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
		    var _$this = $(obj);
		    var that = this;
		    
		    var rivalNameCN = _$this.parent().find("#rivalNameCN").val();
		    var rivalNameEN = _$this.parent().find("#rivalNameEN").val();
		    var editBrandInfoId =  _$this.parent().find("#editBrandInfoId").val();
		    // 编辑对象的ID
		    var param = 'rivalId=' + _$this.parent().find("#rivalId").val();
		    param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		    
		    $("#editRival").dialog( {
				resizable : false,
				modal : true,
				title: 	$("#editRivalTitle").text(),
				height : 180,
				width : 340,
				zIndex: 30,  
				buttons: [{
					text: $("#dialogConfirm").text(),
				    click: function(){
				    		that.editConfirm($("#editRivalUrl").attr("href"),param);
				    	}
				},
				{
					text: $("#dialogCancel").text(),
				    click: function(){
				    		closeCherryDialog('editRival',editDialogBody);
				    	}
				}],
				close: function(){
					closeCherryDialog('editRival',editDialogBody);
				}
			});
		    $("#editRival #editRivalNameCN").val(rivalNameCN);
			$("#editRival #editRivalNameEN").val(rivalNameEN);
			$("#editRival #editBrandInfoId").val(editBrandInfoId);
			$("#editRival").dialog("open");
			cherryValidate({
				formId: 'editRivalForm',	
				rules: {
					editBrandInfoId: {required: true},
					editRivalNameCN: {required: true,maxlength: 20},
					editRivalNameEN: {maxlength: 20}
				}	
			});
		},
		
		/**
		 * 确认更新竞争对手
		 */
		"editConfirm" : function(url,param) {
			if(!$('#editRivalForm').valid()) {
				return false;
			}
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
						closeCherryDialog('editRival',editDialogBody);
						BINOLMOCIO15.search(msg);
					}
			};
			param += "&"+$("#editRivalNameCN").serialize();
			param += "&"+$("#editRivalNameEN").serialize();
			param += "&editBrandInfoId="+$("#editRival #editBrandInfoId option:selected").val();
			cherryAjaxRequest({
				url: url,
				param: param,
				formId: '#editRivalForm',
				callback: callback
			});	
		},
		
		/**
		 * 删除指定竞争对手确认框
		 */
		"popDeleteDialog" : function(obj) {
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
			var _$this = $(obj);
			var that = this;
			    
		    // 删除对象的ID
		    var param = 'rivalId=' + _$this.parent().find("#rivalId").val();
		    param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
			
			var dialogSetting = {
				dialogInit: "#dialogInit",
				text: $("#delConfirmMessage").html(),
				width: 	500,
				height: 300,
				title: 	$("#deleteRivalTitle").text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
						BINOLMOCIO15.deleteConfirm($("#deleteRivalUrl").attr("href"),param);
						removeDialog("#dialogInit");
					},
				cancelEvent: function(){
						removeDialog("#dialogInit");
					}
			};
			openDialog(dialogSetting);
		},
		
		/**
		 * 删除指定竞争对手
		 */
		"deleteConfirm" : function(url,param) {
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
						closeCherryDialog('editRival',editDialogBody);
						BINOLMOCIO15.search(msg);
					}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});	
		}
};

var BINOLMOCIO15 = new BINOLMOCIO15();

// 消息区分(true:显示消息，false:删除消息)
binOLMOCIO15_global.msgKbn = false;
$(document).ready(function() {
	BINOLMOCIO15.search();
	$("#addRivalButton").click(function() {
		BINOLMOCIO15.showChoiceDialog();
	});
	// 新增竞争对手弹出框HTML
	dialogBody = $('#addRival').html();
	// 编辑竞争对手弹出框HTML
	editDialogBody = $('#editRival').html();
});
