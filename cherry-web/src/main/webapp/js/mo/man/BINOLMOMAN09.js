var BINOLMOMAN09 = function () {
    
};
$(document).ready(function(){
	$("#addPaper").click(function(){
		var url = $("#addUrl").attr("href") + "?csrftoken=" + getTokenVal();
		var _this=this;
		var callback = function(msg) {
			var dialogSetting = {
					dialogInit: "#dialogInit",
					bgiframe: true,
					width: 	500,
					height: 300,
					text: msg,
					title: 	$("#addTitle").text(),
					confirm: $("#confirm").text(),
					cancel: $("#cancel").text(),
					confirmEvent: function(){BINOLMOMAN09.savaEditDialog();},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				
				//弹出框体
				openDialog(dialogSetting);
				
//				_this.getLogiDepotByAjax("main_form");
		};
		cherryAjaxRequest({
			url: url,
			callback: callback
		});
	});

});
BINOLMOMAN09.prototype = {
		/*
		 * 查询
		 */
		"search": function(){
			if (!$('#mainForm').valid()) {
				return false;
			};
			$("#checkAll").prop("checked",false);
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
					 aaSorting : [[ 2, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [	{ "sName": "checkbox", "sWidth": "1%", "bSortable": false},  
					              	{ "sName": "no","sWidth": "5%","bSortable": false}, 
									{ "sName": "MenuCode","sWidth": "15%"},			            // 2
									{ "sName": "MenuType", "sWidth": "15%","bSortable": false},
									{ "sName": "MenuLink","sWidth": "15%"},		// 3
									{ "sName": "Comment","sWidth": "19%","bSortable": false},
									{ "sName": "IsLeaf","sWidth": "15%","bSortable": false},
									{ "sName": "operate", "sWidth": "15%","bSortable": false}	
									 
								],			
									
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1, 2,3,4],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 滚动体的宽度
					sScrollXInner:"",

			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
			 //this.setDom();
		},
		"showDetail":function(obj){
			var $this = $(obj);
			var dialogSetting = {
				dialogInit: "#dialogInit",
				width: 	430,
				height: 300,
				title: 	$("#showDetailTitle").text(),
				confirm: $("#dialogClose").text(),
				confirmEvent: function(){
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
			$("#dialogContent").html($this.attr("rel"));
			$("#dialogInit").html($("#dialogDetail").html());
		},
		 //弹出编辑按钮对话框			
		"MAN09EditPaper" : function(obj) {
			var url = $("#editPaper").attr("href");
			var _this=this;
			 url = url + '?posMenuID=' + obj + "&csrftoken=" + getTokenVal();
			var callback = function(msg) {
				var dialogSetting = {
						dialogInit: "#dialogInit",
						bgiframe: true,
						width: 	500,
						height: 300,
						text: msg,
						title: 	$("#updateTitle").text(),
						confirm: $("#confirm").text(),
						cancel: $("#cancel").text(),
						confirmEvent: function(){_this.savaEditDialog();},
						cancelEvent: function(){removeDialog("#dialogInit");}
					};
					
					//弹出框体
					openDialog(dialogSetting);
					
//					_this.getLogiDepotByAjax("main_form");
			};
			cherryAjaxRequest({
				url: url,
				callback: callback
			});
		},
		// 选择记录
		"checkRecord":function(object, id) {
			$("#errorMessage").empty();
			var $id = $(id);
			if($(object).attr('id') == "checkAll") {
				if(object.checked) {
					$id.find(':checkbox').prop("checked",true);
				} else {
					$id.find(':checkbox').prop("checked",false);
				}
			} else {
				if($id.find(':checkbox:not([checked])').length == 0) {
					$id.parent().prev().find('#checkAll').prop("checked",true);
				} else {
					$id.parent().prev().find('#checkAll').prop("checked",false);
				}
			}
		},
		// 删除
		"delete" : function(url) {
			if($('#dataTable :input[checked]').length == 0) {
				$("#errorMessage").html($("#errorMessageTemp").html());
				return false;
			}
			var param = $('#dataTable :input[checked]').serialize() + "&csrftoken=" + getTokenVal();
			var callback = function() {
				if(oTableArr[0] != null)oTableArr[0].fnDraw();
			};
			deleteConfirm(url, param, callback);
		},
		//保存
		"savaEditDialog":function()
		{ 
			var _this =this;
			  if(!$('#main_form').valid()) {
					return false;
				}
				var url = $("#saveUrl").attr("href");
				var param = $("#main_form").find(":input").serialize() + "&csrftoken=" + getTokenVal();
				var callback = function(msg) {
					var dialogSetting = {
							dialogInit: "#dialogInit",
							bgiframe: true,
							width: 	450,
							height: 300,
							text: msg,
							title: 	$("#updateTitle").text(),
							confirm: $("#confirm").text(),
							confirmEvent: function(){removeDialog("#dialogInit");_this.search()},
	                        closeEvent: function(){removeDialog("#dialogInit");_this.search()}
						};
						
						//弹出框体
						openDialog(dialogSetting);
						
				};
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: callback,
					formId: '#mainForm'
				});
			 
		}
};

var BINOLMOMAN09 = new BINOLMOMAN09();

//删除确认
function deleteConfirm(url, param, callback) {
	
	var title = $('#deleteTitle').text();
	var text = $('#deleteMessage').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){deleteHandle(url, param, callback);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};

	openDialog(dialogSetting);
}


//删除處理
function deleteHandle(url, param, callback) {
	
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogConfirm").text(),
				    click: function(){removeDialog("#dialogInit");}
				}]
			});
		} else {
			removeDialog("#dialogInit");
			BINOLMOMAN09.search();
			if(typeof(delCallback) == "function") {
				delCallback();
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#mainForm'
	});

}

/*
 * 全局变量定义
 */
var BINOLMOMAN09_global = {};

// 刷新区分
BINOLMOMAN09_global.refresh = false;

$(function(){
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });
	
	counterBinding({elementId:"counterCodeName",showNum:20,selected:"name"});
	
	BINOLMOMAN09.search();
});