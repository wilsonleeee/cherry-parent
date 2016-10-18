

function bscom02_popDataTableOfEmployee(thisObj, param){
	var url = $('#popEmployeeUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#employee_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "employeeName","sWidth": "30%"},
							{ "sName": "departName","sWidth": "30%"},
							{ "sName": "categoryName","sWidth": "30%"}],
			index : 4,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				var value = $(thisObj).parent().next().find('#higherResult').find(':input[name=higher]').val();
				$('#employee_dataTable').find(':input').each(function(){
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
		height:"auto",
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
		var $selected = $('#employee_dataTable').find(':input[checked]');
		var $higher = $(thisObj).parent().next().find('#higherResult');
		if($selected.length > 0) {
			var text = '<input type="hidden" name="higher" value="'+ $selected.val() +'"/>';
			// 上司的员工ID
			text += '<input type="hidden" name="higherEmployeeId" value="' + $selected.parents('tr').find('#employeeId').val() + '"/>';
			text = text + '<span class="left" style="word-wrap:break-word;overflow:hidden; margin-right:10px;">'+$selected.parents('tr').find('#employeeName').text()+'</span>'
			+'<span class="close left" onclick="bscom02_closeHigher(this);"><span class="ui-icon ui-icon-close"></span></span>';
			$higher.html(text);
		}
		$('#employeeDialog').dialog( "destroy" );
	});
}

function bscom02_popDataTableOfLikeEmployee(thisObj, param){
	var url = $('#popEmployeeUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + '&fromPage=1' + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#likeEmployee_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "employeeId","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "employeeName","sWidth": "30%"},
							{ "sName": "departName","sWidth": "30%"},
							{ "sName": "categoryName","sWidth": "30%"}],
			index : 3,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				$('#likeEmployee_dataTable').find(':input[name="employeeId"]').each(function(){
					var $thisCheckbox = $(this);
					var value = $thisCheckbox.val();
					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
						if(value == $(this).find(':input[name=likeEmployeeId]').val()) {
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
						text = text + '<td class="center">' + '<input type="hidden" name="likeEmployeeId" value="' + $(this).val() +'" />' + 
						'<a class="delete" onclick="bscom02_removeEmployee(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
						$(thisObj).parent().next().find('tbody').append(text);
					} else {
						var value = $thisCheckbox.val();
						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
							if(value == $(this).find(':input[name=likeEmployeeId]').val()) {
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
		height:"auto",
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



function bscom02_popDataTableOfFollowDepart(thisObj, param){
	var url = $('#popDepartUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#followDepart_dataTable',
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
				$('#followDepart_dataTable').find(':input[name="organizationId"]').each(function(){
					var $thisCheckbox = $(this);
//					var $select = $(this).parents('tr').find(':input[name="manageType"]');
					var value = $thisCheckbox.val();
					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
						if(value == $(this).find(':input[name=organizationId]').val()) {
							$thisCheckbox.prop("checked",true);
//							$select.val($(this).find(':input[name="manageType"]').val());
							return false;
						}
					});
				});
				$('#followDepart_dataTable').find(':input[name="organizationId"]').click(function(){
					var $thisCheckbox = $(this);
					if(this.checked) {
						var text = '<tr>';
						$thisCheckbox.parent('td').siblings().each(function(){
							text = text + '<td>' + $(this).find('span').html() + '</td>';
						});
						text = text + '<td class="center">' + '<input type="hidden" name="organizationId" value="' + $(this).val() +'" />' + 
						'<a class="delete" onclick="bscom02_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
						$(thisObj).parent().next().find('tbody').append(text);
					} else {
						var value = $thisCheckbox.val();
						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
							if(value == $(this).find(':input[name=organizationId]').val()) {
								$(this).remove();
								return false;
							}
						});
					}
				});
//				$('#depart_dataTable').find(':input[name="manageType"]').change(function(){
//					var $thisSelect = $(this);
//					var $thisCheckbox = $thisSelect.parents('tr').find(':input[name="organizationId"]');
//					if($thisCheckbox.attr('checked') == true) {
//						var value = $thisCheckbox.val();
//						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
//							if(value == $(this).find(':input[name=organizationId]').val()) {
//								$(this).find(':input[name="manageType"]').val($thisSelect.val());
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
		height:"auto",
		title:$("#PopdepartTitle").text(),
		close: function(event, ui) { $(this).dialog( "destroy" ); }
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#followDepartDialog').dialog(dialogSetting);
	
	
	
	$('#followDepartConfirm').unbind('click');
	$('#followDepartConfirm').click(function(){
		$('#followDepartDialog').dialog( "destroy" );
	});
}

function bscom02_popDataTableOfLikeDepart(thisObj, param,flag){
	var url = $('#popDepartUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	if($("#emp_flag").val()=="1"){
		url += "&type=4&gradeFlag=1";
	}
	var tableSetting = {
			 // 一页显示页数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#likeDepart_dataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置
			 aoColumns : [  { "sName": "BIN_OrganizationID","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "DepartCode","sWidth": "30%"},   
							{ "sName": "DepartName","sWidth": "30%"},
							{ "sName": "Type","sWidth": "30%"}],
			index : 2,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			fnDrawCallback : function() {
				$('#likeDepart_dataTable').find(':input[name="organizationId"]').each(function(){
					var $thisCheckbox = $(this);
//					var $select = $(this).parents('tr').find(':input[name="manageType"]');
					var value = $thisCheckbox.val();
					$(thisObj).parent().next().find('tbody').find('tr').each(function(){
						if(value == $(this).find(':input[name=organizationId]').val()) {
							$thisCheckbox.prop("checked",true);
//							$select.val($(this).find(':input[name="manageType"]').val());
							return false;
						}
					});
				});
				$('#likeDepart_dataTable').find(':input[name="organizationId"]').click(function(){
					var $thisCheckbox = $(this);
					if(this.checked) {
						var text = '<tr>';
						$thisCheckbox.parent('td').siblings().each(function(){
							text = text + '<td>' + $(this).find('span').html() + '</td>';
						});
						text = text + '<td class="center">' + '<input type="hidden" name="organizationId" value="' + $(this).val() +'" />' + 
						'<a class="delete" onclick="bscom02_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a>' + '</td>' + '</tr>';
						$(thisObj).parent().next().find('tbody').append(text);
					} else {
						var value = $thisCheckbox.val();
						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
							if(value == $(this).find(':input[name=organizationId]').val()) {
								$(this).remove();
								return false;
							}
						});
					}
				});
//				$('#depart_dataTable').find(':input[name="manageType"]').change(function(){
//					var $thisSelect = $(this);
//					var $thisCheckbox = $thisSelect.parents('tr').find(':input[name="organizationId"]');
//					if($thisCheckbox.attr('checked') == true) {
//						var value = $thisCheckbox.val();
//						$(thisObj).parent().next().find('tbody').find('tr').each(function(){
//							if(value == $(this).find(':input[name=organizationId]').val()) {
//								$(this).find(':input[name="manageType"]').val($thisSelect.val());
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
		height:"auto",
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
	$('#likeDepartDialog').dialog(dialogSetting);
	
	
	
	$('#likeDepartConfirm').unbind('click');
	$('#likeDepartConfirm').click(function(){
		$('#likeDepartDialog').dialog( "destroy" );
	});
}

function bscom02_removeDepart(object) {
	$(object).parents('tr').remove();
}

function bscom02_closeHigher(object) {
	$(object).parent().empty();
}

function bscom02_removeEmployee(object) {
	$(object).parents('tr').remove();
}