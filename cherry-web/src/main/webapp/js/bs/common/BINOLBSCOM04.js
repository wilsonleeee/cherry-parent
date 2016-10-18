

function bscom04_popDataTableOfEmployee(thisObj, param){
	var url = $('#popEmployeeUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#employee_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "employeeName","sWidth": "30%"},
							{ "sName": "departName","sWidth": "30%"},
							{ "sName": "categoryName","sWidth": "30%"}],
			index : 1,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				var value = $(thisObj).parent().next().find(':input[name=counterHead]').val();
				$('#employee_dataTable').find(':input').each(function(){
					if(value == $(this).val()) {
						$(this).prop('checked', true);
						return false;
					}
				});
//				$('#employee_dataTable').find(':input[name="employeeId"]').each(function(){
//					var $thisCheckbox = $(this);
//					var value = $thisCheckbox.val();
//					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
//						if(value == $(this).find(':input[name=counterHead]').val()) {
//							$thisCheckbox.prop("checked",true);
//							return false;
//						}
//					});
//				});
//				$('#employee_dataTable').find(':input[name="employeeId"]').click(function(){
//					var $thisCheckbox = $(this);
//					if(this.checked) {
//						var text = '<tr>';
//						$thisCheckbox.parent('td').siblings().each(function(){
//							text = text + '<td>' + $(this).find('span').html() + '</td>';
//						});
//						text = text + '<td class="center">' + '<input type="hidden" name="counterHead" value="' + $(this).val() +'" />' + 
//						'<a class="delete" onclick="bscom04_removeEmployee(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
//						$(thisObj).parent().next().find('tbody').append(text);
//					} else {
//						var value = $thisCheckbox.val();
//						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
//							if(value == $(this).find(':input[name=counterHead]').val()) {
//								$(this).remove();
//								return false;
//							}
//						});
//					}
//				});
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
		title:$("#EmployeeTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#employeeDialog').dialog(dialogSetting);
	
	$('#employeeConfirm').unbind('click');
	$('#employeeConfirm').click(function(){
//		$('#employeeDialog').dialog( "destroy" );
		var $thisCheckbox = $('#employee_dataTable').find(':input[checked]');
		if($thisCheckbox.length > 0) {
			var text = '<tr>';
			$thisCheckbox.parent('td').siblings().each(function(){
				text = text + '<td>' + $(this).find('span').html() + '</td>';
			});
			text = text + '<td class="center">' + '<input type="hidden" name="counterHead" value="' + $thisCheckbox.val() +'" />' + 
			'<a class="delete" onclick="bscom04_removeEmployee(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
			$(thisObj).parent().next().find('tbody').html(text);
		}
		$("#showBasError").remove();
		$('#employeeDialog').dialog( "destroy" );
	});
}

function bscom04_popDataTableOfLikeEmployee(thisObj, param){
	var url = $('#popEmployeeUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + '&fromPage=1' + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#likeEmployee_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "employeeName","sWidth": "30%"},
							{ "sName": "departName","sWidth": "30%"},
							{ "sName": "categoryName","sWidth": "30%"}],
			index : 2,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				$('#likeEmployee_dataTable').find(':input[name="employeeId"]').each(function(){
					var $thisCheckbox = $(this);
					var value = $thisCheckbox.val();
					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
						if(value == $(this).find(':input[name=likeCounterEmp]').val()) {
							$thisCheckbox.prop("checked",true);
							return false;
						}
					});
				});
				$('#likeEmployee_dataTable').find(':input[name="employeeId"]').click(function(){
					var $thisCheckbox = $(this);
					if(this.checked) {
						var text = '<tr>';
						$thisCheckbox.parent('td').siblings().each(function(){
							text = text + '<td>' + $(this).find('span').html() + '</td>';
						});
						text = text + '<td class="center">' + '<input type="hidden" name="likeCounterEmp" value="' + $(this).val() +'" />' + 
						'<a class="delete" onclick="bscom04_removeEmployee(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
						$(thisObj).parent().next().find('tbody').append(text);
					} else {
						var value = $thisCheckbox.val();
						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
							if(value == $(this).find(':input[name=likeCounterEmp]').val()) {
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
		title:$("#EmployeeTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#likeEmployeeDialog').dialog(dialogSetting);
	
	$('#likeEmployeeConfirm').unbind('click');
	$('#likeEmployeeConfirm').click(function(){
		$('#likeEmployeeDialog').dialog( "destroy" );
	});
}

function bscom04_popDataTableOfHigheOrg(thisObj, param){
	var url = $('#popHigherOrgUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:5,
			 // 表格ID
			 tableId : '#higherOrg_dataTable',
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
				$('#higherOrg_dataTable').find(':input').each(function(){
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
	$('#higherOrgDialog').dialog(dialogSetting);
	
	
	
	$('#higherOrgConfirm').unbind('click');
	$('#higherOrgConfirm').click(function(){
		var $thisCheckbox = $('#higherOrg_dataTable').find(':input[checked]');
		if($thisCheckbox.length > 0) {
			var text = '<tr>';
			$thisCheckbox.parent('td').siblings().each(function(){
				text = text + '<td>' + $(this).find('span').html() + '</td>';
			});
			text = text + '<td class="center">' + '<input type="hidden" name="path" value="' + $thisCheckbox.val() +'" />' + 
			'<a class="delete" onclick="bscom04_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
			$(thisObj).parent().next().find('tbody').html(text);
		}
		$('#higherOrgDialog').dialog( "destroy" );
	});
}

function bscom04_removeDepart(object) {
	$(object).parents('tr').remove();
}

function bscom04_removeEmployee(object) {
	$(object).parents('tr').remove();
	$("#showBasError").remove();
}