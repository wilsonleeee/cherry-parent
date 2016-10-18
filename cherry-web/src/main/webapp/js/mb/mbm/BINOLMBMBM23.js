function BINOLMBMBM23() {};

BINOLMBMBM23.prototype = {
    "searchSmsSendDetailList" : function() {
    	var $form = $('#smsSendDetailCherryForm');
    	$("#actionResultDisplay").empty();
    	if(!$form.valid()) {
			return false;
		}
		var url = $("#searchSmsSendDetailUrl").attr("href");
		url += "?" + getSerializeToken();
		url += "&" + $form.serialize();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#smsSendDetailDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 4, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "message", "sWidth": "25%", "bSortable": false },
				                { "sName": "mobilephone", "sWidth": "15%", "bSortable": false },
				                { "sName": "planCode", "sWidth": "15%", "bSortable": false },
				                { "sName": "couponCode", "sWidth": "15%", "bSortable": false },
				                { "sName": "sendTime", "sWidth": "20%" },
				                { "sName": "operate", "sWidth": "10%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index:1,
				//bAutoWidth : true,
				fnDrawCallback : function() {
					$('#smsSendDetailDataTable').find("a.description").cluetip({
						splitTitle: '|',
					    width: 300,
					    height: 'auto',
					    cluetipClass: 'default', 
					    cursor: 'pointer',
					    showTitle: false
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    "showDetail":function(obj){
		var $this = $(obj);
		var dialogSetting = {
			dialogInit: "#dialogInit",
			width: 	430,
			height: 300,
			title: 	$("#showDetailTitle").text(),
			confirm: $("#dialogClose").text(),
			confirmEvent: function(){
				removeDialog("#dialogInit");
			}
		};
		openDialog(dialogSetting);
		$("#dialogContent").html($this.attr("rel"));
		$("#dialogInit").html($("#dialogDetail").html());
	},
	"sendmsgtest":function(obj) {
		var dialogSetting = {
			dialogInit: "#sendDialogInit",
			width: 1000,
			height: 650,
			zIndex: 10000,
			title: $("#sendDialogTitle").text()
		};
		openDialog(dialogSetting);
		
		var setMessageUrl = $(obj).attr("href");
		var callback = function(msg) {
			$("#sendDialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: setMessageUrl,
			param: "",
			callback: callback
		});
	},
	"sendMsgInit": function(url) {
		if($("#sendMsgInitDialog").length == 0) {
    		$("body").append('<div style="display:none" id="sendMsgInitDialog"></div>');
    	} else {
    		$("#sendMsgInitDialog").empty();
    	}
    	var dialogSetting = {
    		dialogInit: "#sendMsgInitDialog",
    		text: '<p class="message" style="margin: 45px 0">'+$("#sendMsgInitDialogText").text()+'</p>',
    		width: 	400,
    		height: 200,
    		title: 	$("#sendMsgInitDialogTitle").text(),
    		confirm: $("#dialogConfirm").text(),
    		cancel: $("#dialogCancel").text(),
    		confirmEvent: function(){
    			cherryAjaxRequest({
    				url: url,
    				param: null,
    				callback: function(msg) {
    					if(oTableArr[1] != null)oTableArr[1].fnDraw();
    				}
    			});
    			removeDialog("#sendMsgInitDialog");
    		},
    		cancelEvent: function(){removeDialog("#sendMsgInitDialog");}
    	};
    	openDialog(dialogSetting);
	}
};

var binolmbmbm23 =  new BINOLMBMBM23();

$(function() {
	
	$('#sendTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#sendTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#sendTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#sendTimeStart').val();
			return [value,'minDate'];
		}
	});
	// 表单验证初期化
	cherryValidate({
		formId: 'smsSendDetailCherryForm',
		rules: {
			sendTimeStart: {dateValid: true},
			sendTimeEnd: {dateValid: true}
		}
	});
	binolmbmbm23.searchSmsSendDetailList();
});

