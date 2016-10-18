

function bscom05_popDataTableOfLowerCounter(thisObj, param){
	var url = $('#popLowerCounterUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#lowerCounter_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "BIN_OrganizationID","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "DepartCode","sWidth": "30%"},   
							{ "sName": "DepartName","sWidth": "30%"},
							{ "sName": "Type","sWidth": "30%"}],
			index : 1,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				$('#lowerCounter_dataTable').find(':input[name="path"]').each(function(){
					var $thisCheckbox = $(this);
					var value = $thisCheckbox.val();
					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
						if(value == $(this).find(':input[name=counterPath]').val()) {
							$thisCheckbox.prop("checked",true);
							return false;
						}
					});
				});
				$('#lowerCounter_dataTable').find(':input[name="path"]').click(function(){
					var $thisCheckbox = $(this);
					if(this.checked) {
						var text = '<tr>';
						$thisCheckbox.parent('td').siblings().each(function(){
							text = text + '<td>' + $(this).find('span').html() + '</td>';
						});
						text = text + '<td class="center">' + '<input type="hidden" name="counterPath" value="' + $(this).val() +'" />' + 
						'<a class="delete" onclick="bscom05_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
						$(thisObj).parent().next().find('tbody').append(text);
					} else {
						var value = $thisCheckbox.val();
						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
							if(value == $(this).find(':input[name=counterPath]').val()) {
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
		title:$("#PopdepartTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#lowerCounterDialog').dialog(dialogSetting);
	
	$('#lowerCounterConfirm').unbind('click');
	$('#lowerCounterConfirm').click(function(){
		$('#lowerCounterDialog').dialog( "destroy" );
	});
}

function bscom05_popDataTableOfDepartEmp(thisObj, param){
	var url = $('#popDepartEmpUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#departEmp_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "employeeName","sWidth": "30%"},
							{ "sName": "phone","sWidth": "20%","bSortable": false},
							{ "sName": "mobilePhone","sWidth": "20%","bSortable": false},
							{ "sName": "email","sWidth": "20%","bSortable": false}],
			index : 2,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
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
		title:$("#EmployeeTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#departEmpDialog').dialog(dialogSetting);
	
	$('#departEmpConfirm').unbind('click');
	$('#departEmpConfirm').click(function(){
		var $selected = $('#departEmp_dataTable').find(':input[checked]');
		if($selected.length > 0) {
			var $table = $(thisObj).parents('table');
			$table.find(':input[name=employeeId]').val($selected.val());
			$selected.parent('td').siblings().each(function(){
				var id = $(this).find('span').attr("id");
				if(id) {
					$table.find('#'+id).text($(this).find('span').text());
				}
			});
		}
		$('#departEmpDialog').dialog( "destroy" );
	});
}

function bscom05_popDataTableOfHigherDepart(thisObj, param){
	var url = $('#popHigherDepartUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#higherDepart_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "BIN_OrganizationID","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "DepartCode","sWidth": "30%"},   
							{ "sName": "DepartName","sWidth": "30%"},
							{ "sName": "Type","sWidth": "30%"}],
			index : 3,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				var value = $(thisObj).parent().next().find(':input[name=path]').val();
				$('#higherDepart_dataTable').find(':input').each(function(){
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
		title:$("#PopdepartTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#higherDepartDialog').dialog(dialogSetting);
	
	
	
	$('#higherDepartConfirm').unbind('click');
	$('#higherDepartConfirm').click(function(){
		var $thisCheckbox = $('#higherDepart_dataTable').find(':input[checked]');
		if($thisCheckbox.length > 0) {
			var text = '<tr>';
			$thisCheckbox.parent('td').siblings().each(function(){
				text = text + '<td>' + $(this).find('span').html() + '</td>';
			});
			text = text + '<td class="center">' + '<input type="hidden" name="path" value="' + $thisCheckbox.val() +'" />' + 
			'<a class="delete" onclick="bscom05_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
			$(thisObj).parent().next().find('tbody').html(text);
		}
		$('#higherDepartDialog').dialog( "destroy" );
	});
}



function bscom05_removeDepart(object) {
	$(object).parents('tr').remove();
}

