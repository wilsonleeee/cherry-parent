var BINOLMOMAN08 = function () {
    
};
$(document).ready(function(){
	$("#addPaper").click(function(){
		var url = $("#addUrl").attr("href");
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
					confirmEvent: function(){BINOLMOMAN08.savaEditDialog();},
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
BINOLMOMAN08.prototype = {
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
					 aaSorting : [[ 2, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [	     
					              	{ "sName": "no","sWidth": "1%","bSortable": false}, 
									{ "sName": "ConfigCode","sWidth": "20%"},			            // 2
									{ "sName": "ConfigNote", "sWidth": "20%","bSortable": false},
									{ "sName": "ConfigType","sWidth": "20%"},		// 3
									{ "sName": "ConfigValue","sWidth": "20%","bSortable": false},	
									{ "sName": "operate", "sWidth": "15%","bSortable": false}	
									 
								],			
									
					// 不可设置显示或隐藏的列	
					aiExclude :[1, 2,3,4],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 滚动体的宽度
					sScrollXInner:"",

			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
			 //this.setDom();
		},
		 //弹出编辑按钮对话框			
		"MAN08EditPaper" : function(obj) {
			var url = $("#editPaper").attr("href");
			var _this=this;
			 url = url + '?posConfigID=' + obj + "&csrftoken=" + getTokenVal();
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
		
		"savaEditDialog":function()
		{ 
			var _this =this;
			  if(!$('#main_form').valid()) {
					return false;
				}
//			  	var configValue =$("#configValue").val().trim();
//			  	alert(configValue);
//			  	if(configValue==undefined||configValue==" "){
//			  		return false;
//			  	}
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

var BINOLMOMAN08 = new BINOLMOMAN08();

/*
 * 全局变量定义
 */
var BINOLMOMAN08_global = {};

// 刷新区分
BINOLMOMAN08_global.refresh = false;

$(function(){
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });
	
	counterBinding({elementId:"counterCodeName",showNum:20,selected:"name"});
	
	BINOLMOMAN08.search();
});