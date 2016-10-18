/**
 * @author zgl
 *
 */
function popDataTableOfCounter1(option){
	oTableArr[111] = null;
	var url = $('#popLowerCounterUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	if(typeof option.departType != "undefined"){
		var param = "departType="+option.departType[0];
		for(var i=1 ; i<option.departType.length; i++){
			param = param+"&departType="+option.departType[i];
		}
	}
	if(param != undefined){
		url += "?" + csrftoken + "&"+param +(option.param?('&'+option.param):'');
	}else{
		url += "?" + csrftoken + (option.param?('&'+option.param):'');
	}
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
			index : 111,
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
		title:$("#PopdepartTitle").text(),
		close: function(event, ui) { 
			$(this).dialog( "destroy" ); 
			$(option.thisId).remove();
			$(option.parentId).html(option.thisHtml);
		}
	};
	$('#lowerCounterDialog').dialog(dialogSetting);
}