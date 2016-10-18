var BINOLPLSCF17_GLOBAL = function () {
    
};

BINOLPLSCF17_GLOBAL.prototype = {
		//用户查询
		"search" : function (){
			var $form = $('#mainForm');
			// 查询参数序列化
			var params= $form.serialize();
			var url = $("#searchUrl").attr("href");
			url = url + "?" + params;
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 排序列
					 aaSorting : [[3, "desc"]],
					 // 表格列属性设置			 
					 aoColumns : [	{ "sName": "no","sWidth": "5%","bSortable": false}, 				// 1
			                    	{ "sName": "handlerName","sWidth": "35%","bSortable": true},		// 2
			                    	{ "sName": "descriptionDtl","sWidth": "45%","bSortable": true},		// 3	
									{ "sName": "validFlag","sWidth": "15%","bSortable": true}			// 4
								],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					index : 1
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		//全选
		"checkHandler" : function(obj, id) {
			var $id = $(id);
			var $obj = $(obj);
			if($obj.attr('id') == "checkAll") {
				if($obj.is(':checked')) {
					$id.find(':checkbox').prop("checked",true);
				} else {
					$id.find(':checkbox').prop("checked",false);
				}
			} else {
				if ($id.find(':checkbox:not([checked])').length == 0) {
					$('#checkAll').prop("checked",true);
				} else {
					$('#checkAll').prop("checked",false);
				}
			}
		},
		
		//启用/停用配置
		"handlerValid" : function(kbn) {
			var $ck = $("input[name='validFlag']:checked");
			if ($ck.length == 0) {
				return false;
			}
			var url = $("#handValidUrl").attr("href");
			var param = $("#brandSel").serialize();
			var option = {
					dialogId : "#dialogHandler",
					cancelTxt : $("#cancelTxt").text(),
					url : url
			};
			// 启用
			if ('1' == kbn) {
				option.title= $("#titleOnTxt").text();
				if ($ck.filter("[value='0']").length == 0) {
					option.cancelTxt = $("#closeTxt").text();
					option.text = "<p class='message'><span>" + $("#warnOnTxt").text() + "</span></p>";
				} else {
					option.confirmTxt = $("#sureTxt").text();
					option.text = "<p class='message'><span>" + $("#configOnTxt").text() + "</span></p>";
					option.param = param + "&validFlag=1";
				}
			} else {
				option.title= $("#titleOffTxt").text();
				if ($ck.filter("[value='1']").length == 0) {
					option.cancelTxt = $("#closeTxt").text();
					option.text = "<p class='message'><span>" + $("#warnOffTxt").text() + "</span></p>";
				} else {
					option.confirmTxt = $("#sureTxt").text();
					option.text = "<p class='message'><span>" + $("#configOffTxt").text() + "</span></p>";
					option.param = param + "&validFlag=0";
				}
			}
			var m = [];
			$ck.each(function() {
				var n = [];
				$(this).parent().find(":input").each(function(){
					n.push('"'+this.name+'":"'+
							$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				});
				m.push("{" + n.toString() + "}");
			});
			option.param += "&upInfo=[" + m.toString() + "]";
			BINOLPLSCF17.dialogPanel(option);
		},
		//执行停用/启用
		"execValid" : function(url, param) {
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : function(msg){
					removeDialog("#dialogHandler");
					BINOLPLSCF17.search();
					$('#checkAll').prop("checked",false);
				}
			});
		},
		
		//弹出确认框
		"dialogPanel" : function(option) {
			var dialogSetting = {
					dialogInit: option.dialogId,
					text: option.text,
					width: 	500,
					height: 300,
					title: 	option.title,
					cancel: option.cancelTxt,
					cancelEvent: function(){
						removeDialog($(option.dialogId));
					}
				};
			if (option.confirmTxt) {
				dialogSetting.confirm = option.confirmTxt;
				dialogSetting.confirmEvent =  function(){
					BINOLPLSCF17.execValid(option.url, option.param);
					removeDialog($(option.dialogId));
				};
				
			}
			openDialog(dialogSetting);
		},
		
		//刷新处理器
		"refreshHandler" : function(obj) {
			var url = $(obj).attr("href");
			var param = $("#brandSel").serialize();
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: function(msg) {
					if(window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var option = {
								dialogId : "#dialogRefresh",
								title : $("#refreshTxt").text(), 
								cancelTxt : $("#closeTxt").text()
						};
						var ret = msgJson["RESULT"];
						if ("NG" == ret) {
							option.text = "<p class='message'><span>" + $("#refreshNGTxt").text() + "</span></p>";
						} else {
							option.text = "<p class='message'><span>" + $("#refreshOKTxt").text() + "</span></p>";
						}
						BINOLPLSCF17.dialogPanel(option);
					}
				}
			});
		}
}
var BINOLPLSCF17 = new BINOLPLSCF17_GLOBAL();

$(document).ready(function() {
	BINOLPLSCF17.search();
});