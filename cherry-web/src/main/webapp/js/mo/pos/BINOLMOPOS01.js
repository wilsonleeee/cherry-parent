var BINOLMOPOS01_GLOBAL = function() {

};

BINOLMOPOS01_GLOBAL.prototype = {
	"showMessageDialog":function (dialogSetting){
		if(dialogSetting.type == "MESSAGE"){
			$("#messageContent").show();
			$("#successContent").hide();
			$("#messageContentSpan").text(dialogSetting.message);
		}else{
			$("#messageContent").hide();
			$("#successContent").show();
			$("#successContentSpan").text(dialogSetting.message);
		}
		var $dialog = $('#messageDialogDiv');
		$dialog.dialog({ 
			//默认不打开弹出框
			autoOpen: false,  
			//弹出框宽度
			width: 400, 
			//弹出框高度
			height: 200, 
			//弹出框标题
			title:$("#messageDialogTitle").text(), 
			//弹出框索引
			zIndex: 99,  
			modal: true, 
			resizable:false,
			//关闭按钮
			close: function() {
				closeCherryDialog("messageDialogDiv");
				if(typeof dialogSetting.focusEvent == "function") {
					dialogSetting.focusEvent();
				}
			}
		});
		$dialog.dialog("open");
		// 给确认按钮绑定事件
		$("#btnMessageConfirm").bind("click", function(){
			BINOLMOPOS01.messageConfirm(dialogSetting.focusEvent);
		});
	},
	
	"messageConfirm":function (focusEvent){
		closeCherryDialog("messageDialogDiv");
		if(typeof focusEvent == "function") {
			focusEvent();
		}
	},
	// 关闭弹出窗口
	"cancel":function(){
		closeCherryDialog('dialogInit');
		$('#dialogInit').html("");
	},
	// 确定
	"confirm":function(){
		if($("#storePayConfigBody").find(':checkbox:[checked]').length == 0) {
			BINOLMOPOS01.showMessageDialog({
				message:"请选择至少一种支付方式！",
				type:"MESSAGE"
			});
		}else {
			var codeKeyList = new Array();
			$("#storePayConfigBody").find(':checkbox:[checked]').each(function(){
				var codeKey = {"codeKey":$(this).val()};
				codeKeyList.push(codeKey);
			});
			var addUrl = $("#addUrl").attr("href");
			codeKeyList = JSON.stringify(codeKeyList);
			var params = "codeKeyList="+codeKeyList;
			cherryAjaxRequest({
				url:addUrl,
				param:params,
				callback:function(data){
					if(data=="SUCCESS"){
						BINOLMOPOS01.cancel();
						BINOLMOPOS01.showMessageDialog({
							"message":"添加成功！",
							"type":"SUCCESS"
						});
						BINOLMOPOS01.search();
					}else {
						BINOLMOPOS01.showMessageDialog({
							"message":"添加失败！",
							"type":"MESSAGE"
						})
					}
				}
			});
		}
	},
	// 用户查询
	"search" : function() {
		var $form = $('#mainForm');
		$form.find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		if(!$form.valid()){
			return false;
		}
		//表格列属性
		var aoColumnsArr = [
		                    {"sName":"No", "sWidth":"7%","bSortable":false},
		                    {"sName":"storePayCode", "sWidth":"15%"},
		                    {"sName":"storePayValue", "sWidth":"15%"},
		                    {"sName":"payType", "sWidth":"15%","bVisible":false},
		                    {"sName":"isEnable", "sWidth":"15%"},
		                    {"sName":"defaultPay", "sWidth":"15%"}
		                    ];
		
		var url = $("#searchUrl").attr("href");
		var params= $("#mainForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		
		$("#section").show();
		// 表格设置
		var tableSetting = {
			// datatable 对象索引
			index : 1,
			// 表格ID
			tableId : "#storePayConfigTable",
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 4, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 1 , 2 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
//			fixedColumns : 1,
			fnDrawCallback : function() {
				
			}
		};
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"addInit":function(){
		$('#checkAllPay').prop("checked",false);
		var openCardInitUrl=$('#addInitUrl').attr('href')+"?"+getSerializeToken();
		var dialogSetting = {
				dialogInit: "#dialogInit",
				width: 800,
				height: 600,
				title: "支付方式",
				closeEvent:function(){
					removeDialog("#dialogInit");
				}
			};
			cherryAjaxRequest({
				url: openCardInitUrl,
				callback: function(data) {
					if(data=="NC"){
						BINOLMOPOS01.showMessageDialog({
							"message":"无可选支付方式！",
							"type":"MESSAGE"
						})
					}else {
						openDialog(dialogSetting);
						$("#dialogInit").html(data);
					}
				}
			});
	},
	"delStorePayConfig":function(storePayCode){
		var url = $("#deleteUrl").attr("href");
		var params = "storePayCode="+storePayCode;
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:function(data){
				if(data=="SUCCESS"){
					BINOLMOPOS01.showMessageDialog({
						"message":"操作成功！",
						"type":"SUCCESS"
					});
					BINOLMOPOS01.search();
				}else {
					BINOLMOPOS01.showMessageDialog({
						"message":"操作失败！",
						"type":"MESSAGE"
					});
				}
			}
		});
	},
	"editConfirm":function(t,flag){
		var object = $("#storePayConfigTableTbode").find(':checkbox');
		var count = 0;
		if(object.length>0){
			$.each(object,function(i){
				if(object[i].checked){
					count+=1;
				}
			});
		}
		if(count<1){
			BINOLMOPOS01.showMessageDialog({
				"message":"请至少选择一种支付方式！",
				"type":"MESSAGE"
			});
			return false;
		}else if("default"==flag && count>1){
			BINOLMOPOS01.showMessageDialog({
				"message":"只能选择一种支付方式设为默认！",
				"type":"MESSAGE"
			});
			return false;
		}
		var url = $(t).attr("href");
		var editArray = new Array();
		if(object.length>0){
			$.each(object,function(i){
				if(object[i].checked){
					var storePayCode = $(object[i]).val();
					var o = {"storePayCode":storePayCode,"flag":flag};
					editArray.push(o);
				}
			});
		}
		editArray = JSON.stringify(editArray);
		var params = "editJson="+editArray;
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:function(data){
				if(data=="SUCCESS"){
					BINOLMOPOS01.showMessageDialog({
						"message":"操作成功！",
						"type":"SUCCESS"
					});
					$('#checkAllPay').prop("checked",false);
					BINOLMOPOS01.search();
				}else {
					BINOLMOPOS01.showMessageDialog({
						"message":"操作失败！",
						"type":"MESSAGE"
					});
				}
			}
		});
	},
	// 选择记录
	"checkRecord":function (object, id) {
		var $id = $(id);
		if($(object).attr('id') == "checkAllPay") {
			if(object.checked) {
				$id.find(':checkbox').prop("checked",true);
			} else {
				$id.find(':checkbox').prop("checked",false);
			}
		} else {
			if($id.find(':checkbox:not([checked])').length == 0) {
				$('#checkAllPay').prop("checked",true);
			} else {
				$('#checkAllPay').prop("checked",false);
			}
		}
	}
};

var BINOLMOPOS01 = new BINOLMOPOS01_GLOBAL();

$(document).ready(function(){
	BINOLMOPOS01.search();
});
