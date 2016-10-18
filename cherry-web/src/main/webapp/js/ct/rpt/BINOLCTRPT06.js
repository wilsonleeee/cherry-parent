var BINOLCTRPT06_GLOBAL = function() {

};

BINOLCTRPT06_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "bSortable": false},
		                    {"sName" : "channelName", "bVisible" : false},
		                    {"sName" : "counterCode", "bVisible" : false},
		                    {"sName" : "counterName", "bVisible" : false},
		                    {"sName" : "communicationName"},
		                    {"sName" : "sendMemCount"},
		                    {"sName" : "sendTime"}
		                    ];
		
		var url = $("#searchUrl").attr("href");
		var params = $form.serialize();
		url = url + "?" + params;
		$("#section").show();
		// 表格设置
		var tableSetting = {
			index : 1,
			// 表格ID
			tableId : "#dataTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 5, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			callbackFun : function(msg) {
			},
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	/**
	 * 更改渠道事件
	 */
	"changeChannel" : function(){
		$('#counterCode').val("");
		$('#counterName').val("");
		var channelId = $("#channelId option:selected").val();
		if(channelId != null && channelId != "" && channelId != undefined){
			$('#counterName').removeAttr("disabled");
			$('#counterName').prop("class","text");
			// 柜台下拉框绑定
			var option = {
				elementId:"counterName",
				showNum:20,
				targetId:"counterCode",
				paramType:"1",
				paramValue:channelId,
				targetDetail:true,
				afterSelectFun:BINOLCTPLN01.ctAfterSelectFun
			};
			counterSelectBinding(option);
		}else{
			$('#counterName').attr("disabled","true");
			$('#counterName').prop("class","text disabled");
		}
	},
	
	"ctAfterSelectFun":function(info){
		$('#counterCode').val(info.counterCode);
		$('#counterName').val(info.counterName);
	}

	/**
	 * 弹出明细查询对话框
	 */
//	"popDetailDialog" : function(obj) {
//		var url = $(obj).attr("href");
//    	var title = "（"+$(obj).parent().find("#communicationNameShow").val()+"）";
//    	var dialogSetting = {
//    			dialogInit: "#dialogInit",
//    			width: 	900,
//    			height: 625,
//    			title: 	title + $("#detailDialogTitle").text(),
//    			closeEvent: function(){
//    				removeDialog("#dialogInit");
//    			}
//    	};
//    	openDialog(dialogSetting);
//	    	
//    	cherryAjaxRequest({
//    		url: url,
//    		param: null,
//    		callback: function(data) {
//				$("#dialogInit").html(data);
//			}
//    	});
//	}
	
};

var BINOLCTRPT06 = new BINOLCTRPT06_GLOBAL();

$(document).ready(
	function(){
		$("#startDate").cherryDate({
			beforeShow: function(input){
				var value = $("#endDate").val();
				return [value, "maxDate"];
			}
		});
		$("#endDate").cherryDate({
			beforeShow: function(input){
				var value = $("#startDate").val();
				return [value, "minDate"];
			}
		});
		cherryValidate({//form表单验证
			formId: "mainForm",		
			rules: {
				startDate:{dateValid: true},
				endDate:{dateValid: true}
			}		
		});
	}
);
