var BINOLBSEMP08_global = function(){
	this.needUnlock = true;
};

BINOLBSEMP08_global.prototype = {
		"search" : function() {
			var $form = $('#mainForm');
			
			if (!$form.valid()) {
				return false;
			};
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var url = $("#searchUrl").attr("href");
			var params = $form.serialize();
			if(params != null && params != "") {
				url = url + "?" + params;
			}
			url = url + "&csrftoken="+parentTokenVal();
			// 显示结果一览
			$("#section").show();
			// 表格设置
			var tableSetting = {
					 // datatable 对象索引
					 index : 3,
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "no.","bSortable":false},
					                { "sName": "parentResellerNameShow"},
									{ "sName": "resellerNameShow"},
									{ "sName": "couponCode"},
									{ "sName": "useFlag", "sClass":"center"},
									{ "sName": "synchFlag", "sClass":"center"},
									{ "sName": "operation", "sClass":"center","bSortable":false}
									],
					// 不可设置显示或隐藏的列	
//					aiExclude :[0,1,2,3,4,5],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%"
					// 固定列数
//					fixedColumns : 6
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/**
		 * 弹出删除确认框
		 */
		"popDeleteDialog" : function(obj) {
			var that = this;
			that.clearActionHtml();
			var param = "csrftoken=" + getTokenVal();
			var dialogSetting = {
				dialogInit: "#dialogInit",
				text: $("#dialogContent").html(),
				width: 	500,
				height: 300,
				title: 	$('#deleteBaCouponTitle').text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					that.deleteBaCoupon($(obj).attr("href"),param);
					removeDialog("#dialogInit");
				},
				cancelEvent: function(){
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
		},
		
		/**
		 * 删除未同步的代理商优惠券
		 */
		"deleteBaCoupon" : function(url,param) {
			var that = this;
			var callback = function(msg){
				if(msg.indexOf('id="actionResultDiv"') > -1){
						if(oTableArr[3] != null)oTableArr[3].fnDraw();
						if(oTableArr[2] != null)oTableArr[2].fnDraw();
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback
			});
		},
		
		/**
		 * 清理执行结果信息
		 */
		"clearActionHtml" : function() {
			$("#errorDiv2 #errorSpan2").html("");
			$("#errorDiv2").hide();
			$("#errorMessage").empty();
			$("#actionResultDisplay").empty();
		}
};

var BINOLBSEMP08 = new BINOLBSEMP08_global();

$(document).ready(function(){
	if (window.opener) {
		window.opener.lockParentWindow();
	}
});
window.onbeforeunload = function(){
	if (BINOLBSEMP08.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};