
function BINOLMBSVC01() {
	
};

BINOLMBSVC01.prototype = {
		
		/**
		 * 清理提示信息
		 */
		"clearMessage" : function() {
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
		},
		
		/**
		 * 停用规则确认框
		 */
		"stopRuleDialog":function (discountId,subDiscountId){
			this.clearMessage();
			var dialogId = "stop_rule_dialog";
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
						BINOLMBSVC01.stopRulePlan(discountId,subDiscountId);
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
		
		/**
		 * 重启规则确认框
		 */
		"restartRuleDialog":function (discountId,subDiscountId){
			this.clearMessage();
			var dialogId = "restart_rule_dialog";
			var $dialog = $('#' + dialogId);
			$dialog.dialog({ 
				//默认不打开弹出框
				autoOpen: false,  
				//弹出框宽度
				width: 350, 
				//弹出框高度
				height: 250, 
				//弹出框标题
				title:$("#restartDialogTitle").text(), 
				//弹出框索引
				zIndex: 2,  
				modal: true, 
				resizable:false,
				//弹出框按钮
				buttons: [{
					text:$("#dialogConfirm").text(),//确认按钮
					click: function() {
						//点击确认后执行活动停用
						BINOLMBSVC01.restartRulePlan(discountId,subDiscountId);
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
		
		/**
		 * 停用规则
		 */
		"stopRulePlan":function (discountId,subDiscountId){
			var url = $('#stopRuleUrl').attr("href");
			var param = "discountId=" + discountId + "&subDiscountId="
						+ subDiscountId + "&validFlag=0";
			cherryAjaxRequest({
				url:url,
				param:param,
				callback: function(data) {
					$("#stop_rule_dialog").dialog("close");
					//刷新表格数据
					if(oTableArr[0] != null)
						oTableArr[0].fnDraw();
				}
			});
		},
		
		/**
		 * 重启规则
		 */
		"restartRulePlan":function (discountId,subDiscountId){
			var url = $('#stopRuleUrl').attr("href");
			var param = "discountId=" + discountId + "&subDiscountId="
						+ subDiscountId + "&validFlag=1";
			cherryAjaxRequest({
				url:url,
				param:param,
				callback: function(data) {
					$("#restart_rule_dialog").dialog("close");
					//刷新表格数据
					if(oTableArr[0] != null)
						oTableArr[0].fnDraw();
				}
			});
		},
		
		/**
		 * 查询
		 */
		"searchList":function(){
			//清除提示信息
			this.clearMessage();
			var url = $("#searchUrl").attr("href");
			var params = $("#ruleForm").serialize();
			if (params != null && params != "") {
				url = url + "?" + params;
			}
			$("#resultList").show();
			// 表格设置
			var tableSetting = {
				// 表格ID
				tableId : '#resultRuleDataTable',
				// 数据URL
				url : url,
				// 表格默认排序
				aaSorting : [ [ 8, "asc" ] ],
				// 表格列属性设置
				aoColumns : [ {
					"sName" : "number",
					"bSortable" : false
				}, {
					"sName" : "subDiscountName",
					"bSortable" : true
				}, {
					"sName" : "discountBeginDate",
					"bSortable" : true
				}, {
					"sName" : "discountEndDate",
					"bSortable" : true
				}, {
					"sName" : "rechargeType",
					"bSortable" : true
				}, {
					"sName" : "applyCntCount",
					"bSortable" : false
				}, {
					"sName" : "usedCntCount",
					"bSortable" : false
				}, {
					"sName" : "involveNumber",
					"bSortable" : false
				}, {
					"sName" : "rechargeValueActual",
					"bSortable" : true
				}, {
					"sName" : "giftAmount",
					"bSortable" : true
				}, {
					"sName" : "operator",
					"bSortable" : false
				} ],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				fnDrawCallback : function() {

				},
				callbackFun : function(msg) {
					var $msg = $("<div></div>").html(msg);
					var $headInfo = $("#headInfo", $msg);
					if ($headInfo.length > 0) {
						$("#headInfo").html($headInfo.html());
					} else {
						$("#headInfo").empty();
					}
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		}
};

var BINOLMBSVC01 =  new BINOLMBSVC01();
//初始化
$(document).ready(function() {
	// 表单验证配置
	cherryValidate({
		formId : 'ruleForm',
		rules: {
			startDate: {dateValid: true},
			endDate: {dateValid: true}
		}
	});

	$("#searchButton").click(function() {
		// 表单验证
		if (!$("#ruleForm").valid()) {
			return false;
		}
		BINOLMBSVC01.searchList();
		return false;
	});
	
	// 初始化时即获取查询结果 
	BINOLMBSVC01.searchList();
});
