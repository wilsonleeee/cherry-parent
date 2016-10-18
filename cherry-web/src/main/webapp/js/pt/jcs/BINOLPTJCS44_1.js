function BINOLPTJCS44_1() {
	
};

BINOLPTJCS44_1.prototype = {
	"pbAfterSelectFun":function(info){
		if($('#searchPrintbody >tr').length > 20){
			// 显示提示信息
			BINOLPTJCS44_1.showMessageDialog({
				message:"每页最多打印21条记录", 
				type:"MESSAGE", 
				focusEvent:function(){
					// 清空扫描输入框
					$("#productSearchStr").val("");
					$("#productSearchStr").focus();
				}
			});
		}else{
			var nextIndex = parseInt($("#rowNumber").val())+1;
			$("#rowNumber").val(nextIndex);
			var rowIndex = parseInt($("#rowCode").val())+1;
			$("#rowCode").val(rowIndex);
			var html = '<tr id="dataRow'+nextIndex+'">';
			html += '<td id="indexNo">'+rowIndex+'</td>';
			html += '<td><span>'+info.unitCode+'</span><input id="billId" name="billId" type="hidden" value="'+ info.prtId +'"/></td>';
			html += '<td><span>'+info.barCode+'</span></td>';
			html += '<td><span>'+info.productName+'</span></td>';
			html += '<td><span>'+info.memPrice+'</span></td>';
			html += '<td><span>'+info.tagPrice+'</span></td>';
			html += '<td><span><button class="wp_del" onclick="BINOLPTJCS44_1.deleteRow(this);return false;">删除</button></td></tr>';
			$("#searchPrintbody").append(html);
			// 清空扫描输入框
			$("#productSearchStr").val("");
		}
	},
	
	"deleteRow":function(obj){
		var $this = $(obj);
		var $thisTr = $this.parent().parent().parent();
		// 移除选中行
		$thisTr.remove();
		
		// 更新序号
		var indexNo = 1;
		var rows = $("#searchPrintbody").children();
		if(rows.length > 0){
			rows.each(function(i){
				if($(this).find("#indexNo").length > 0){
					$(this).find("#indexNo").text(indexNo);
					$("#rowCode").val(indexNo);
					indexNo++;
				}
			});
		}
		$("#productSearchStr").focus();
	},
	
	"printRecord":function(){
		printSearchProductWithoutParam();
	},
	
	"close":function(){
		// 关闭弹出窗口
		removeDialog("#printDialogInit");
	},
	
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
			BINOLPTJCS44_1.messageConfirm(dialogSetting.focusEvent);
		});
	},
	
	"messageConfirm":function (focusEvent){
		closeCherryDialog("messageDialogDiv");
		if(typeof focusEvent == "function") {
			focusEvent();
		}
	}
};

var BINOLPTJCS44_1 =  new BINOLPTJCS44_1();
//初始化
$(document).ready(function() {
	var counterCode = $("#counterCode").val();
	var option = {
		elementId:"productSearchStr",
		counterCode:counterCode,
		showNum:20,
		targetDetail:true,
		afterSelectFun:BINOLPTJCS44_1.pbAfterSelectFun
	};
	cntProductBinding(option);
});

