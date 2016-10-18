var BINOLCTPLN01_GLOBAL = function() {

};

BINOLCTPLN01_GLOBAL.prototype = {
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "planCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "planName", "sWidth" : "10%"},
		                    {"sName" : "campaignCode", "bVisible" : false, "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "campaignName", "sWidth" : "10%"},
		                    {"sName" : "channelName", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "counterCode", "bVisible" : false, "sWidth" : "7%"},
		                    {"sName" : "counterName", "bVisible" : false, "sWidth" : "10%"},
		                    {"sName" : "status", "sWidth" : "5%"},
		                    {"sName" : "lastRunTime", "sWidth" : "5%"},
		                    {"sName" : "createTime", "sWidth" : "5%"},
		                    {"sName" : "memo1", "bVisible" : false, "sWidth" : "10%", "bSortable" : false},
		                    {"sName" : "validFlag", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false} 
		                    ];
		var url = $("#searchPlnUrl").attr("href");
		var params = $form.serialize();
		url = url + "?" + params;
		
		$("#plandetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 10, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 2 , 4 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			fnDrawCallback : function() {
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	
	/**
	 * 停用沟通计划弹出框
	 * @return
	 */
	"stopCommPlanDialog":function (planCode){
		var dialogId = "stop_plan_dialog";
		var $dialog = $('#' + dialogId);
		$dialog.dialog({ 
			//默认不打开弹出框
			autoOpen: false,  
			//弹出框宽度
			width: 350, 
			//弹出框高度
			height: 250, 
			//弹出框标题
			title:$("#dialogTitle").text(), 
			//弹出框索引
			zIndex: 1,  
			modal: true, 
			resizable:false,
			//弹出框按钮
			buttons: [{
				text:$("#dialogConfirm").text(),//确认按钮
				click: function() {
					//点击确认后执行活动停用
					BINOLCTPLN01.stopCommPlan(planCode);
					$dialog.dialog("close");
				}
			},
			{	
				text:$("#dialogCancel").text(),//取消按钮
				click: function() {
					$dialog.dialog("close");
				}
			}],
			//关闭按钮
			close: function() {
				closeCherryDialog(dialogId);
			}
		});
		$dialog.dialog("open");
	},
	
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
	},
	
	/**
	 * 停用沟通计划
	 */
	"stopCommPlan":function (planCode){
		var url = $('#stopCommPlanUrl').html();
		var param = "planCode="+planCode;
		cherryAjaxRequest({
			url:url,
			param:param,
			callback: function(data) {
				$("#stop_plan_dialog").dialog("close");
				//刷新表格数据
				if(oTableArr[0] != null)
					oTableArr[0].fnDraw();
			}
		});
	}
};

var BINOLCTPLN01 = new BINOLCTPLN01_GLOBAL();

$(document).ready(
	function(){
		$("#fromDate").cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("p").find("input[name='toDate']").val();
				return [value, "maxDate"];
			}
		});
		$("#toDate").cherryDate({
			beforeShow: function(input){
				var value = $(input).parents("p").find("input[name='fromDate']").val();
				return [value, "minDate"];
			}
		});
		cherryValidate({
			formId: "mainForm",		
			rules: {
				fromDate: {dateValid: true},
				toDate: {dateValid: true}
		   }		
		});
		BINOLCTPLN01.search();
	}
);
