function PopMsgList2() {};

PopMsgList2.prototype = {
	"search":function(){

		$('#mainForm :input').each(function(){
			$(this).val($.trim(this.value));
		});
		var url = $("#TopAction_getMsgListSearch").val();

		// 查询参数序列化
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#section").show();

		var tableSetting = {
				// 表格ID
				tableId : '#msgList_dataTable2',
				// 数据URL
				url : url,

				// 表格列属性设置
				aoColumns : [  { "sName": "No","sWidth": "10%","bSortable": false},
				               { "sName": "PublishDate","sWidth": "10%"},
				               { "sName": "MessageTitle","sWidth": "10%"},
				               { "sName": "MessageBody","sWidth": "30%","bSortable": false},
				               { "sName": "MessageType","sWidth": "20%","bSortable": false}],
				// 一页显示页数
				iDisplayLength:10,
				// 排序
				//aaSorting:[[1, "desc"]],
				aiExclude :[0, 1],
				//index : index,
				//colVisFlag: false,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 2/*,
				callbackFun:function(msg){
					//重新设置画面上的未读数
		 			var $msg = $("<div></div>").html(msg);
		 			var unReadCount = parseInt($msg.find("#sessionUnReadCount").text(),10);
		 			$("#unReadCount").html(unReadCount);
		 			toggleNewMsgIcon(unReadCount);
				}*/
		};
		// 调用获取表格函数
		getTable(tableSetting);
	
		/*var dialogSetting = {
				bgiframe: true,
		        width:860, 
		        height:"auto",
		        minWidth:860,
		        zIndex: 9999,
		        modal: true,
		        resizable: false,
		        title:$("#MsgList2Title").text(),
		        buttons: [{
		        	text: $("#header_global_page_close").val(),
		        	click: function() {
		        		popMsgList2.closePopMsgList2(MsgList2DialogHtml);
		        	}
		        }],
		        close: function(event, ui) {
		        	popMsgList2.closePopMsgList2(MsgList2DialogHtml);
		        }
		};
		$('#MsgList2Dialog').dialog(dialogSetting);*/
	},
	/**
	 * 弹出消息详细
	 *
	 * */
	"showDetail":function(obj){
		var $this = $(obj);
		var that = this;
		var dialogSetting = {
			dialogInit: "#messageDatail",
			width: 	430,
			height: 300,
			title: 	$("#detailTitle").text(),
			close: "关闭",
			closeEvent: function(){

				var callback = function(){
					removeDialog("#messageDetail");
					popMsgList2.search();
				}

				var param = "counterMessageId="+$this.attr("counter_message_id");
				cherryAjaxRequest({
					url:$("#TopAction_setMsgRead").val(),
					reloadFlg:true,
					param:param,
					callback:callback
				});

			}
		};
		that.messageDatailHtml = $("#detail").html();
		openDialog(dialogSetting);
		$("#messageDatail").html(that.messageDatailHtml);
		var messageDatail = $this.attr("message_body");
		var messageTitle = $this.attr("message_title");
		$("#messageDatail").find("#messageTitle").html(messageTitle);
		$("#messageDatail").find("#messageBody").html(messageDatail);
	}
};

var popMsgList2 = new PopMsgList2();

$(document).ready(function() {
	popMsgList2.search();
});