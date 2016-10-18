

function bspos99_popDataTableOfEmployee(thisObj, param){
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
			 aoColumns : [  { "sName": "BIN_EmployeeID","sWidth": "10%","bSortable": false}, 	  
							{ "sName": "EmployeeName","sWidth": "15%"},   
							{ "sName": "DepartName","sWidth": "15%","bSortable": false},
							{ "sName": "PositionName","sWidth": "15%","bSortable": false}],
			index : 1
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
		title:'员工信息',
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
		if($selected.length > 0) {
			var $table = $(thisObj).parents('tr');
			$table.find(':input[name=employeeId]').val($selected.val());
			$selected.parent('td').siblings().each(function(){
				var id = $(this).find('span').attr("id");
				if(id) {
					$table.find('#'+id).text($(this).find('span').text());
				}
			});
		}
		$('#employeeDialog').dialog( "destroy" );
	});
}