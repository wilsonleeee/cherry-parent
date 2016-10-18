
function BINOLMOPMC04_3() {};

BINOLMOPMC04_3.prototype = {
		
		/**
		 * 弹出上级菜单选择框
		 */
		"popParentMenuDialog" : function(thisObj) {
			var url = $(thisObj).attr("href");
			var param = "machineType="+$("#machineType").val()+"&csrftoken=" + getTokenVal();
			url += "?" + param;
			var tableSetting = {
					 // 一页显示页数
					 iDisplayLength:5,
					 // 表格ID
					 tableId : '#parentMenu_dataTable',
					 // 数据URL
					 url : url,
					 // 表格列属性设置
					 aoColumns : [  { "sName": "posMenuID", "sWidth": "10%","bSortable": false}, 	  
									{ "sName": "menuCode", "sWidth": "30%"},   
									{ "sName": "menuName", "sWidth": "30%"},
									{ "sName": "menuType", "sWidth": "30%"}],
					index : 1,
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback : function() {
						var value = $(thisObj).parent().next().find(':input[name=parentMenuID]').val();
						$('#parentMenu_dataTable').find(':input').each(function(){
							if(value == $(this).val()) {
								$(this).prop('checked', true);
								return false;
							}
						});
					}
			 };
			
			// 调用获取表格函数
			getTable(tableSetting);

			var dialogSetting = {
				bgiframe: true,
				width:650, 
				height:360,
				minWidth:650,
				minHeight:360,
				zIndex: 9999,
				modal: true, 
				resizable: false,
				title:$("#PopParentMenuTitle").text(),
				close: function(event, ui) { $(this).dialog( "destroy" ); }
			};
			
			$('#parentMenuDialog').dialog(dialogSetting);
			
			$('#parentMenuConfirm').unbind('click');
			$('#parentMenuConfirm').click(function(){
				var $thisCheckbox = $('#parentMenu_dataTable').find(':input[checked]');
				if($thisCheckbox.length > 0) {
					var text = '<tr>';
					$thisCheckbox.parent('td').siblings().each(function(){
						text = text + '<td>' + $(this).find('span').html() + '</td>';
					});
					text = text + '<td class="center">' + '<input type="hidden" name="parentMenuID" value="' + $thisCheckbox.val() +'" />' + 
					'<a class="delete" onclick="BINOLMOPMC04_3.removeMenu(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
					$(thisObj).parent().next().find('tbody').html(text);
				}
				$('#parentMenuDialog').dialog( "destroy" );
			});
		},
		
		/**
		 * 删除已选中的上级菜单
		 */
		"removeMenu" : function(object) {
			$(object).parents('tr').remove();
		},
		
		/**
		 * 机器类型发生改变时清除已选的上级菜单
		 */
		"cleanParentMenu" : function() {
			$("#parentMenuDiv").find('tbody').html("");
		},
		
		/**
		 * 保存新增的菜单信息
		 */
		"saveAddMenu" : function() {
			if($('#addMenuForm').valid()) {
				var url = $("#saveAddMenuUrl").attr("href");
				var param = $("#addMenuForm").serialize();
				param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
				
				var callback = function(msg) {
					if($('#actionResultBody').length > 0) {
						if(window.opener.document.getElementById('menuBrandTree')) {
							window.opener.freshTree();
						}
					}
				};
				
				cherryAjaxRequest( {
					url : url,
					param : param,
					callback : callback,
					formId : '#addMenuForm'
				});
			}
		}
};

var BINOLMOPMC04_3 = new BINOLMOPMC04_3();

$(document).ready(function() {
	
	if (window.opener) {
       window.opener.lockParentWindow();
    }
});

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};