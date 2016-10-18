var BINOLMBCLB02_GLOBAL = function () {
    
};

BINOLMBCLB02_GLOBAL.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		
		// 选择子品牌
		"selOriginalBrand" : function (thisObj, param) {

			var url = $('#origBrandSearchUrl').text();
			var csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
			url += "?" + csrftoken + (param?('&'+param):'');
			var tableSetting = {
					 // 一页显示页数
					 iDisplayLength:10,
					 // 表格ID
					 tableId : '#origBrandDataTable',
					 // 数据URL
					 url : url,
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no","sWidth": "10%","bSortable": false}, 	  
									{ "sName": "originalBrand","sWidth": "45%"},   
									{ "sName": "origBrandName","sWidth": "45%","bSortable": false}],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback : function() {
						$('#origBrandDataTable').find(':input[name="origBrandCode"]').each(function(){
							var $thisCheckbox = $(this);
							var value = $thisCheckbox.val();
							$('#origBrand_tbody').find('tr').each(function(){
								if(value == $(this).find(':input[name=origBrandCode]').val()) {
									$thisCheckbox.prop("checked",true);
									return false;
								}
							});
						});
						$('#origBrandDataTable').find(':input[name="origBrandCode"]').click(function(){
							// 如果全选状态时，删除全选记录
							$('#origBrand_tbody').find('tr').each(function(){
								if("ALL" == $(this).find(':input[name=origBrandCode]').val()) {
									$('#origBrand_tbody').empty();
									return false;
								}
							});
							var $thisCheckbox = $(this);
							if(this.checked) {
								var text = '<tr>';
								$thisCheckbox.parent('td').siblings().each(function(i){
									text += '<td>' + $(this).find('span').html() + '</td>';
								});
								text = text + '<td class="center">' + '<input type="hidden" name="origBrandCode" value="' + $(this).val() +'" />' + 
								'<a class="delete" onclick="BINOLMBCLB02.removeBrand(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
								$('#origBrand_tbody').append(text);
							} else {
								var value = $thisCheckbox.val();
								$('#origBrand_tbody').find('tr').each(function(){
									if(value == $(this).find(':input[name=origBrandCode]').val()) {
										$(this).remove();
										return false;
									}
								});
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
				height:"auto",
				resizable: false,
				title:$("#PopOrigBrandTitle").text(),
				close: function(event, ui) { $(this).dialog( "destroy" ); }
			};
//			if(thisObj) {
//				var opos = $(thisObj).offset();
//				var oleft = parseInt(opos.left,10) ;
//				var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//				dialogSetting.position = [ oleft , otop ];
//			}
			$('#origBrandDialog').dialog(dialogSetting);
			
			
			// 选择全部
			$('#origBrandConfirm').unbind('click');
			$('#origBrandConfirm').click(function(){
				$('#origBrand_tbody').empty();
				var text = '<tr><td>ALL</td><td>全部子品牌</td><td class="center"><input type="hidden" name="origBrandCode" value="ALL" />\
					<a class="delete" onclick="BINOLMBCLB02.removeBrand(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>\
					</td></tr>';
				$('#origBrand_tbody').append(text);
				$('#origBrandDialog').dialog( "destroy" );
			});
			// 关闭
			$('#origBrandClose').unbind('click');
			$('#origBrandClose').click(function(){
				$('#origBrandDialog').dialog( "destroy" );
			});
		},
		// 删除选中
		"removeBrand" : function (object) {
			$(object).parents('tr').remove();
		},
		/*
		 *保存俱乐部
		 */
		"saveClub" : function (){
			if (!$('#mainForm').valid()) {
				return false;
			};
			if ($("#clubMod").val() == "1") {
				var tree = CHERRYTREE.trees[0];
				if(!isEmpty(tree)){
					// 取得选中节点
					var nodes = CHERRYTREE.getTreeNodes(tree,true);
					// 取得选中节点【排除全选节点的子节点】
					var checkedNodes = CHERRYTREE.getCheckedNodes(nodes);
					// 取得叶子节点
					var leafNodes = CHERRYTREE.getLeafNodes(nodes);
					$("#placeJson_0").val(JSON.stringify(checkedNodes));
					$("#saveJson_0").val(JSON.stringify(leafNodes));
				}
			}
			// 参数序列化
			var param = null;
			// 基本信息
			$("#mainForm").find(":input").each(function() {
				if (!$(this).is(":disabled")) {
					$(this).val($.trim(this.value));
					if (null == param) {
						param = $(this).serialize();
					} else {
						param += "&" + $(this).serialize();
					}
				}
			});
			if ($("#clubMod").val() == "2") {
				// 子品牌
				var origBrands = [];
				$('#origBrand_tbody').find('tr').each(function(){
					$(this).find(':input[name=origBrandCode]').each(function(){
						var code = '"'+this.name+'":"'+
						$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"';
						origBrands.push("{" + code + "}");
					});
				});
				param += "&origBrands=[" + origBrands.toString() + "]";
			} else {
				param += "&" + $("#locationType_0").serialize() + 
				"&" + $("#placeJson_0").serialize() + 
				"&" + $("#saveJson_0").serialize();
			}
			cherryAjaxRequest({
				url: $("#saveUrl").attr("href"),
				param: param,
				coverId: "#pageBrandButton",
				callback: function(msg) {
					BINOLMBCLB02.doRefresh = true;
				}
			});
		}
		
}

var BINOLMBCLB02 = new BINOLMBCLB02_GLOBAL();

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLMBCLB02.doRefresh) {
			// 刷新父页面
			window.opener.search();
		}
		if(BINOLMBCLB02.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};

$(document).ready(function() {
	cherryValidate({			
		formId: "mainForm",		
		rules: {		
			clubName: {required: true, maxlength: 50},	// 俱乐部名称
			clubCode: {required: true, maxlength: 50}	// 俱乐部代码
		}		
	});
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});