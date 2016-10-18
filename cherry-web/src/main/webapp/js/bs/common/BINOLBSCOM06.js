

function bscom06_popDataTableOfRegion(thisObj, param){
	var url = $('#popRegionUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#region_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "regionId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "regionCode","sWidth": "30%"},   
							{ "sName": "regionName","sWidth": "30%"},
							{ "sName": "regionType","sWidth": "30%"}],
			index : 1,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				var value = $(thisObj).parent().next().find(':input[name=higherPath]').val();
				$('#region_dataTable').find(':input').each(function(){
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
		title:$("#RegionTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#regionDialog').dialog(dialogSetting);
	
	$('#regionConfirm').unbind('click');
	$('#regionConfirm').click(function(){
		var $thisCheckbox = $('#region_dataTable').find(':input[checked]');
		if($thisCheckbox.length > 0) {
			var text = '<tr>';
			$thisCheckbox.parent('td').siblings().each(function(){
				text = text + '<td>' + $(this).find('span').html() + '</td>';
			});
			text = text + '<td class="center">' + '<input type="hidden" name="higherPath" value="' + $thisCheckbox.val() +'" />' + 
			'<a class="delete" onclick="bscom06_removeRegion(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">移除</span></a>' + '</td>' + '</tr>';
			$(thisObj).parent().next().find('tbody').html(text);
		}
		$('#regionDialog').dialog( "destroy" );
	});
}




function bscom06_popProvince(thisObj, param){
	var url = $('#popProvinceUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#region_dataTable1',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "regionId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "regionCode","sWidth": "30%"},   
							{ "sName": "regionName","sWidth": "30%"},
							{ "sName": "regionType","sWidth": "30%"}],
			index : 2,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				$('#region_dataTable1').find(':input[name="path"]').each(function(){
					var $thisCheckbox = $(this);
					var value = $thisCheckbox.val();
					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
						if(value == $(this).find(':input[name=provincePath]').val()) {
							$thisCheckbox.prop("checked",true);
							return false;
						}
					});
				});
				$('#region_dataTable1').find(':input[name="path"]').click(function(){
					var $thisCheckbox = $(this);
					if(this.checked) {
						var text = '<tr>';
						$thisCheckbox.parent('td').siblings().each(function(){
							text = text + '<td>' + $(this).find('span').html() + '</td>';
						});
						text = text + '<td class="center">' + '<input type="hidden" name="provincePath" value="' + $(this).val() +'" />' + 
						'<a class="delete" onclick="bscom06_removeRegion(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">移除</span></a>' + '</td>' + '</tr>';
						$(thisObj).parent().next().find('tbody').append(text);
					} else {
						var value = $thisCheckbox.val();
						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
							if(value == $(this).find(':input[name=provincePath]').val()) {
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
		resizable: false,
		title:$("#ProvinceTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
	$('#regionDialog1').dialog(dialogSetting);
	$('#regionConfirm1').unbind('click');
	$('#regionConfirm1').click(function(){
		$('#regionDialog1').dialog( "destroy" );
	});
}

function bscom06_removeRegion(object) {
	$(object).parents('tr').remove();
}
