function popOnlineUser(){
	var onlineUserDialogHtml = "<div id=\"onlineUserDialog\" class=\"hide\">"+$("#onlineUserDialog").html()+"</div>";
	
	var index = 219;
	if(oTableArr[index]){
		oTableArr[index] = null;
	}
	var url = $("#TopAction_getOnlineUserUrl").attr("href");
	var csrftoken = $('#csrftoken').serialize();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	url += "?" + csrftoken;
	var tableSetting = {
			// 一页显示页数
			iDisplayLength:10,
			// 表格ID
			tableId : '#onlineUser_dataTable',
			// 数据URL
			url : url,
			// 排序
			//aaSorting:[[1, "asc"]],
			// 表格列属性设置
			aoColumns : [  { "sName": "No","sWidth": "1%","bSortable": false},
			               { "sName": "LoginName","sWidth": "25%","bSortable": false},
			               { "sName": "LoginIP","sWidth": "25%","bSortable": false},
			               { "sName": "LoginTime","sWidth": "25%","bSortable": false},
			               { "sName": "UserAgent","sWidth": "25%","bSortable": false}],
			index : index,
			colVisFlag: false,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%"
	};

	// 调用获取表格函数
	getTable(tableSetting);

	var dialogSetting = {
			bgiframe: true,
	        width:600, 
	        height:"auto",
	        minWidth:600,
	        zIndex: 9999,
	        modal: true,
	        resizable: false,
	        title:$("#onlineUserTitle").text(),
	        buttons: [{
	        	text: $("#global_page_close").text(),
	        	click: function() {
	        		closePopOnlineUser(onlineUserDialogHtml);
	        	}
	        }],
	        close: function(event, ui) {
	        	closePopOnlineUser(onlineUserDialogHtml);
	        }
	};
	$('#onlineUserDialog').dialog(dialogSetting);
}
	
function closePopOnlineUser(onlineUserDialogHtml){
    //关闭是dataTable的弹出窗在header上的特别处理，如果使用closeCherryDialog关闭则会出现种种问题。
    var dialogID = "onlineUserDialog";
    $("#"+dialogID).dialog('destroy');
    $("#"+dialogID).remove();
    $("#onlineUserDialogInit").html(onlineUserDialogHtml);
}
	
/* 
 * 导出Excel
 */
function exportOnlineUserExcel(){
	//无数据不导出
    if($(".dataTables_empty").length==0){
    	var url = $("#TopAction_downOnlineUserUrl").attr("href");
    	var params= "csrftoken=" +$("#csrftoken").val();
    	url = url + "?" + params;
    	window.open(url,"_self");
	}
}