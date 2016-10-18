/**
 * @author zgl
 *
 */
function popDepartTableByBusinessType(thisObj, param, callback){
	oTableArr[30] = null;
	var thisHtml = $("#_departDialog_main").html();
	var url = $('#_popDepartUrl').text();
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken + (param?('&'+param):'');
	var tableSetting = {
			 // 一页显示行数
			 iDisplayLength:10,
			 // 表格ID
			 tableId : '#_departDataTable',
			 // 数据URL
			 url : url,
			 // 表格列属性设置,
			 aaSorting : [[ 0, "asc" ]],
			 
			 aoColumns : [  { "sName": "organizationId","sWidth": "10%","bSortable": false}, 	// 0
							{ "sName": "departCode","sWidth": "35%"},                     	// 1
							{ "sName": "departName","sWidth": "35%"},
							{ "sName": "type","sWidth": "20%"}],    // 2
			index : 30			
	 };
	
	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
		bgiframe: true,
		width:600, 
		height:"auto",
		minWidth:600,
		minHeight:320,
		zIndex: 9999,
		modal: true,
		resizable: false,
		title:$("#PopdepartTitle").text(),
		close: function(event, ui) {
			$('#_departDialog_errorDisplay').empty(); 
//			removeDialog("#_departDialog");
			$("#_departDialog").dialog("destroy");
			$("#_departDialog").remove();
			$("#_departDialog_main").html(thisHtml);
		}
	};
//	if(thisObj) {
//		var opos = $(thisObj).offset();
//		var oleft = parseInt(opos.left,10) ;
//		var otop = parseInt(opos.top, 10) + 25 - $(document).scrollTop();
//		dialogSetting.position = [ oleft , otop ];
//	}
	$('#_departDialog').dialog(dialogSetting);
	
	$('#_departConfirm').unbind('click');
	$('#_departConfirm').click(function(){
		var $select = $('#_departDataTable').find(':input[checked]');
		if($select.length == 0) {
			$('#_departDialog_errorDisplay').html($('#_departDialog_error').html());
			return;
		} else {
			if(typeof(callback) == "function") {
				callback();
			}
			$('#_departDialog_errorDisplay').empty();
//			removeDialog("#_departDialog");
			$("#_departDialog").dialog("destroy");
			$("#_departDialog").remove();
			$("#_departDialog_main").html(thisHtml);
		}
	});
}