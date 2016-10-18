var popMsgList = function () {};

popMsgList.prototype = {
	"popMsgList":function(){
		var msgListDialogHtml = "<div id=\"msgListDialog\" class=\"hide\">"+$("#msgListDialog").html()+"</div>";

		var index = 229;
		if(oTableArr[index]){
			oTableArr[index] = null;
		}
		var url = $("#TopAction_getMsgListUrl").attr("href");
		var csrftoken = $('#csrftoken').serialize();
		if(!csrftoken) {
			csrftoken = $('#csrftoken',window.opener.document).serialize();
		}
		url += "?" + csrftoken;
		var tableSetting = {
				// 一页显示页数
				iDisplayLength:10,
				// 表格ID
				tableId : '#msgList_dataTable',
				// 数据URL
				url : url,
				// 排序
				aaSorting:[[6, "asc"]],
				// 表格列属性设置
				aoColumns : [  { "sName": "No","sWidth": "1%","bSortable": false},
				               { "sName": "time","sWidth": "10%","bSortable": false},
				               { "sName": "messageType","sWidth": "5%","bSortable": false},
				               { "sName": "content","sWidth": "99%","bSortable": false},
				               { "sName": "readType","sWidth": "99%","bSortable": false},
				               { "sName": "control","sWidth": "99%","bSortable": false},
				               { "sName": "hideCol","sWidth": "1%","bVisible": false}],
				index : index,
				colVisFlag: false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				callbackFun:function(msg){
					//重新设置画面上的未读数
		 			var $msg = $("<div></div>").html(msg);
		 			var unReadCount = parseInt($msg.find("#sessionUnReadCount").text(),10);
		 			$("#unReadCount").html(unReadCount);
		 			toggleNewMsgIcon(unReadCount);
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	
		var dialogSetting = {
				bgiframe: true,
		        width:860, 
		        height:"auto",
		        minWidth:860,
		        zIndex: 9999,
		        modal: true,
		        resizable: false,
		        title:$("#msgListTitle").text(),
		        buttons: [{
		        	text: $("#header_global_page_close").val(),
		        	click: function() {
		        		popMsgList.closePopMsgList(msgListDialogHtml);
		        	}
		        }],
		        close: function(event, ui) {
		        	popMsgList.closePopMsgList(msgListDialogHtml);
		        }
		};
		$('#msgListDialog').dialog(dialogSetting);
	},
	
	"closePopMsgList":function(msgListDialogHtml){
	    //关闭是dataTable的弹出窗在header上的特别处理，如果使用closeCherryDialog关闭则会出现种种问题。
	    var dialogID = "msgListDialog";
	    $("#"+dialogID).dialog('destroy');
	    $("#"+dialogID).remove();
	    $("#msgListDialogInit").html(msgListDialogHtml);
	},

	"downloadFile":function(no){
		if($("#"+$("#msgListNo_"+no).html()+":visible").length>0){
			popMsgList.setMsgRead($("#msgListNo_"+no).html());
		}
		if($("#exportIframe").length > 0) {
	    	$("#exportIframe").remove();
	    }
	    var iframe = document.createElement("iframe");
	    $(iframe).attr("id","exportIframe");
	    $(iframe).hide();
	    iframe.src =  getBaseUrl()+'/common/BINOLCM37_download' + "?" + getSerializeToken() + "&tempFilePath=" + $("#msgDownloadFileURL_"+no).html();
	    document.body.appendChild(iframe);
	},
	
	"setAllMsgRead":function(){
		if($("#msgList_dataTable .dataTables_empty:visible").length == 1){
			return;
		}
		var param ="messageID=ALL";
		var callback = function(msg) {
			var unReadCount = parseInt(msg);
			$("#unReadCount").html(unReadCount);
			toggleNewMsgIcon(unReadCount);
		};
		cherryAjaxRequest({
			url:$("#TopAction_setMsgReadUrl").html(),
			reloadFlg:true,
			param:param,
			callback:callback
		});
		$.each($('#msgList_dataTable > tbody > tr'), function(i){
			$(this).find("td:eq(4)").html($("#header_messageStatus_read").html());
			$(this).find("td:eq(5) a").hide();
		});
	},
	
	"setMsgRead":function(msgid){
		if($("#"+msgid).is(":visible") == false){
			return;
		}
		var param ="messageID="+msgid;
		var callback = function(msg) {
			var unReadCount = parseInt(msg);
			$("#unReadCount").html(unReadCount);
			toggleNewMsgIcon(unReadCount);
		};
		cherryAjaxRequest({
			url:$("#TopAction_setMsgReadUrl").html(),
			reloadFlg:true,
			param:param,
			callback:callback
		});
		$("#"+msgid).parent().parent().find("td:eq(4)").html($("#header_messageStatus_read").html());
		$("#"+msgid).hide();
	},
	
	"beforeLogout":function(){
		var logoutUrl = $("#logoutURL").attr("href");
		var r = false;
		if(parseInt($("#unReadCount").text())>0){
			  r = confirm($("#header_beforeLogoutUnRead").val());
		}else{
			r = true;
		}
		if(r){
			window.location.href = logoutUrl;
		}
	}
};

var popMsgList = new popMsgList();