var CTCOM_dialogBody ="";
var BINOLCTCOM04_GLOBAL = function () {
};

BINOLCTCOM04_GLOBAL.prototype = {
	//验证失败提示信息
	"getError" : function(obj,errorMsg){
		var $parent = $(obj).parent();
		$parent.removeClass('error');
		$parent.find("#errorText").remove();
		$parent.addClass("error");
		$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
		$parent.find('#errorText').attr("title",'error|'+errorMsg);
		$parent.find('#errorText').cluetip({
	    	splitTitle: '|',
		    width: 150,
		    cluezIndex: 20000,
		    tracking: true,
		    cluetipClass: 'error', 
		    arrows: true, 
		    dropShadow: false,
		    hoverIntent: false,
		    sticky: false,
		    mouseOutClose: true,
		    closePosition: 'title',
		    closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	},
	//查询一个字符串在另一个字符串中出现的次数
	"countInstances" :	function (mainStr,subStr,isIgnoreCase){
		if(isIgnoreCase){
			mainStr = mainStr.toLowerCase();
			subStr = subStr.toLowerCase();
		}
        var count = 0;
        var offset = 0;
        do{
            offset = mainStr.indexOf(subStr, offset);
            if(offset != -1){
                count++;
                offset += subStr.length;
            }
        }while(offset != -1)
	    return count;
	},
	//验证信息内容是否存在非法字符	
	"validIllegalChar" : function(){
		$("#msgContents").parent().removeClass('error');
		$("#msgContents").parent().find("#errorText").remove();
		var $that = this;
		var flag = true;
		var msgContents = $("#msgContentsTemp").val();
		var $param = $("#templateParam");
		//排除模版变量 ,将模版变量替换为五个空格，防止除去后组合成新的非法字符
		$param.find("li").each(
			function(){
				var paramValue = $(this).find("#paramValue").val();
				if(msgContents.search(paramValue)>=0){
					msgContents = msgContents.replace(new RegExp(paramValue,"g"), "     ");
				}
			}
		);
		//从非法字符列表中读取非法字符
		$("#illegalCharDiv").find("li").each(function(){
			$(this).removeClass("red");
			$(this).removeAttr("title");
			var illegalChar = $.trim($(this).text());
			//获取该非法字符在信息内容中存在的个数
			var count = $that.countInstances(msgContents,illegalChar,true);
			if( count != 0){
				//存在该字符将字符标记为红色
				$(this).addClass("red");
				var msg = $("#illegalCharCount").text();
				$(this).attr("title",msg.replace("{0}",count));
				flag = false;
			}
		});
		//存在非法字符
		if(flag == false){
			//错误提示
			$that.getError("#msgContents",$("#illegalCharError").text());
			//展开非法字符列表
			if($("#expandIllegalChar").children('.ui-icon').is('.ui-icon-triangle-1-n')){
				$that.expandIllegalChar("#expandIllegalChar");
			}
		}
		return flag;
	},
	//展开/隐藏非法字符列表
	"expandIllegalChar" : function(thisObj){
		if($(thisObj).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(thisObj).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
		} else {
			$(thisObj).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
		}
		$('#illegalCharDiv').toggle();
	},
	// tab页切换
	"setDisplay" :function(id,type){
		var $obj = $(id);
		if(type == 'show'){
			$obj.show();
		}else{
			$obj.hide();
		}
			
	},
	// 用户查询
	"search" : function() {
		var $form = $('#getMessageForm');
		//表格列属性
		var aoColumnsArr = [
		                    {"sName" : "RowNumber", "sWidth": "3%", "bSortable": false},
		                    {"sName" : "templateCode", "bVisible" : false, "sWidth" : "5%"},
		                    {"sName" : "templateName", "sWidth" : "10%"},
		                    {"sName" : "templateUse", "sWidth" : "5%"},
		                    {"sName" : "contents", "bSortable" : false},
		                    {"sName" : "customerType", "sWidth" : "5%"},
		                    {"sName" : "status", "sWidth" : "5%", "bSortable" : false},
		                    {"sName" : "act", "sWidth" : "10%", "bSortable" : false} 
		                    ];
		var url = $("#searchTplUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken();
		url = url + "?" + params;
		$("#templatedetail").show();
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 排序列
			aaSorting : [ [ 2, "desc" ] ],
			// 表格列属性设置
			aoColumns : aoColumnsArr,
			// 不可设置显示或隐藏的列
			aiExclude : [ 0 , 2 , 4, 7 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3,
			// 默认显示行数
			iDisplayLength : 10,
			fnDrawCallback:function() {
				$('#dataTable').find("a.description").cluetip({
					splitTitle: '|',
				    width: 500,
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
	
	// 模板选择页面选择事件，将选择的模板信息赋给沟通信息设置对应的显示栏
	"chooseMsgTemplate" : function(obj){
		var templateCodeValue = $(obj).find("#templateCode").val();
		var contentsValue = $(obj).find("#contents").val();
		if(templateCodeValue != undefined && null != templateCodeValue){
			$(".MSGINFOON").find("#isTemplate").val("1");
			$(".MSGINFOON").find("#templateCode").val(templateCodeValue);
		}
		if(contentsValue != undefined && null != contentsValue){
			$(".MSGINFOON").find("#cluetip-waitimage").remove();
			$(".MSGINFOON").find("#cluetip").remove();
			$(".MSGINFOON").find("#contents").val(contentsValue);
			if(contentsValue.length > 30)
			{
				$(".MSGINFOON").find("#linkContents").html(contentsValue.substring(0,30)+" ...");
				$(".MSGINFOON").find("#lblContents").html(contentsValue.substring(0,30)+" ...");
			}else{
				$(".MSGINFOON").find("#linkContents").html(contentsValue);
				$(".MSGINFOON").find("#lblContents").html(contentsValue);
			}
			$(".MSGINFOON").find("#linkContents").attr("title",'沟通内容|'+contentsValue);
			$(".MSGINFOON").find("#linkContents").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
			$(".MSGINFOON").find("#errorText").remove();
		}
		// 关闭弹出窗口
		closeCherryDialog('dialogInit',CTCOM_dialogBody);
		// 移除临时样式，将弹出框内的表对象清空
		$(".MSGINFOON").removeClass("MSGINFOON");
		oTableArr[0]=null;
	},
	
	// 信息编辑页面确定按钮点击事件，将信息编辑框的内容赋给沟通信息设置对应的显示栏
	"editMsgTemplate" : function(){
		if(!this.validIllegalChar()){
			return false;
		}
		var contentsValue = $.trim($("#msgContents").val());
		if(contentsValue != undefined && null != contentsValue && contentsValue != ""){
			contentsValue = $("#msgContentsTemp").val();
			$(".MSGINFOON").find("#contents").val(contentsValue);
			if(contentsValue.length > 30)
			{
				$(".MSGINFOON").find("#linkContents").html(contentsValue.substring(0,30)+" ...");
				$(".MSGINFOON").find("#lblContents").html(contentsValue.substring(0,30)+" ...");
			}else{
				$(".MSGINFOON").find("#linkContents").html(contentsValue);
				$(".MSGINFOON").find("#lblContents").html(contentsValue);
			}
			$(".MSGINFOON").find("#isTemplate").val("2");
			
			$(".MSGINFOON").find("#linkContents").attr("title",'沟通内容|'+contentsValue);
			$(".MSGINFOON").find("#linkContents").cluetip({width: '500',splitTitle:'|',height: 'auto',cluetipClass: 'default',cursor: 'pointer',showTitle: false});
			$(".MSGINFOON").find("#errorText").remove();
		}
		// 关闭弹出窗口
		closeCherryDialog('dialogInit',CTCOM_dialogBody);
		// 移除临时样式，将弹出框内的表对象清空
		$(".MSGINFOON").removeClass("MSGINFOON");
		oTableArr[0]=null;
	},
	
	// 信息编辑框按键响应事件，触发显示内容的改变
	"changeViewValue" : function(obj){
		var text = $.trim($(obj).val());
		// 调用信息内容更改函数，改变相关的显示内容
		this.changeText(text);
		this.validIllegalChar();
	},
	
	// 模板变量选择事件，选择模板变量时将变量值加入到信息编辑框
	"selectParam" : function(obj){
		var msg = document.getElementById("msgContents");
		var str = $(obj).find("#paramValue").val();
		var type = $(obj).find("#paramType").val();
        var msgLength = msg.value.length;
        msg.focus();
        if(typeof document.selection != "undefined"){
            document.selection.createRange().text = str;  
        }else{
            msg.value = msg.value.substr(0,msg.selectionStart) + str + msg.value.substring(msg.selectionStart,msgLength);
        }
        // 若选择的参数为参与了计算的参数时给出提示
        if(type != "2"){
        	if ($("#paramCheckRemind_dialog").length == 0) {
    			$("body").append('<div style="display:none" id="paramCheckRemind_dialog"></div>');
    		} else {
    			$("#paramCheckRemind_dialog").empty();
    		}
    		var dialogId = "paramCheckRemind_dialog";
    		var $dialog = $('#' + dialogId);
    		var dialogTitle = $("#paramRemindDialogTitle").text();
    		var dialogContent = $('#paramCheckRemindPage').html();
    		$dialog.html(dialogContent);
    		$dialog.dialog({ 
    			//默认不打开弹出框
    			autoOpen: false,  
    			//弹出框宽度
    			width: 500, 
    			//弹出框高度
    			height: 200, 
    			//弹出框标题
    			title:dialogTitle,
    			//弹出框索引
    			zIndex: 20000,
    			modal: true, 
    			resizable:false,
    			//弹出框按钮
    			buttons: [{	
    				text:$("#paramRemindDialogClose").text(),//关闭按钮
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
        }
        // 调用信息内容更改函数，改变相关的显示内容
        this.changeText(msg.value);
	},
	
	// 编辑短信页面短信内容编辑框更改内容时相关显示内容的改变
	"changeText" : function(text){
		//添加信息签名
		if(text != ''){
			var signature = $('#signature').val();
			if(signature != ''){
				text = text + "【"+signature+"】";
			}
			$("#msgContentsTemp").val(text);
		}else{
			$('#msgContentsTemp').val("");
		}
		var $view = $("#contentsView");
		var $param = $("#templateParam");
		var $TextNum = $("#countTextNum");
		var $MsgNum = $("#countMsgNum");
		// 根据模板变量值的内容替换信息内容
		$param.find("li").each(
			function(){
				var paramValue = $(this).find("#paramValue").val();
				var comments = $(this).find("#comments").val();
				if(text.search(paramValue)>=0){
					text = text.replace(new RegExp(paramValue,"g"), comments);
				}
			}
		);
		// 显示经过替换后的信息内容
		$view.val(text);
		// 显示信息长度统计值
		$TextNum.html(text.length);
		// 当信息长度超出70个字符的倍数时更改信息条数提示信息
		if((text.length)/70>1){
			$MsgNum.html(parseInt((text.length)/70)+1);
		}else{
			$MsgNum.html("1");
		}
	},
	
    "getDialog" : function(){
    	$('#actionResultDiv1').attr("class","hide");
    	if($("#templateSaveDialog").length == 0) {
    		$("body").append('<div style="display:none" id="templateSaveDialog"></div>');
    	} else {
    		$("#templateSaveDialog").empty();
    	}
    	$("#templateSaveDialog").html('<div id="dialogActionResult"></div><form id="saveTemplateForm" method="post" csrftoken="false"></form>');
    	$("#saveTemplateForm").html($("#templateSavePage").html());
    	cherryValidate({
    		formId: 'saveTemplateForm',
    		rules: {
    			templateName: {required: true,maxlength: 50}
    		}
    	});
    	var dialogSetting = {
    			bgiframe: true,
    			width:'350', 
    			height:'auto',
    			zIndex: 1,
    			modal: true, 
    			resizable: false,
    			title:$('#templateSaveDialogTitle').html(),
    			close: function() { removeDialog("#templateSaveDialog"); }
		};
		$('#templateSaveDialog').dialog(dialogSetting);
    },
    
    "closeDialog" : function(){
    	removeDialog("#templateSaveDialog");
    },
    
    "confirmDialog" : function(){
    	if($('#msgContents').val() == null || $('#msgContents').val() == '' ){
    		$('#actionResultDiv1').attr("class","actionError");
			$('#ActionResultSpan1').html($('#msgContentsNull').html());
			removeDialog("#templateSaveDialog");
			return false;
    	}
    	var $form = $('#saveTemplateForm');
    	if(!$form.valid()) {
			return false;
		}
    	var url = $("#saveTemplateUrl").attr("href");
		var params = $form.serialize() + "&" + getSerializeToken() + "&msgContents=" + encodeURI($("#msgContentsTemp").val());
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg) {
				var msgJson = window.JSON.parse(msg);
				if(msgJson.errorMsg!=null){
					$('#actionResultDiv1').attr("class","actionError");
					$('#ActionResultSpan1').html(msgJson.errorMsg);
				}else{
					$('#actionResultDiv1').attr("class","actionSuccess");
					$('#ActionResultSpan1').html(msgJson.successMsg);
				}
				$('#actionResultDiv1').show();
				removeDialog("#templateSaveDialog");
			}
		});
    }
	
}

var BINOLCTCOM04 = new BINOLCTCOM04_GLOBAL();

function search() {
	BINOLCTCOM04.search();
}

$(document).ready(function() {
	var isNeedInit = $("#isNeedInit").val();
	if(isNeedInit != undefined && isNeedInit == "false"){
		//共通js，有些画面引用时不需要初始化方法
		return;
	}
	// 清空弹出框内的表对象
	oTableArr[0]=null;
	$('.ui-tabs').tabs();
	// 加载页面时查询模板列表
	BINOLCTCOM04.search();
	
	var text = $("#msgContents").val();
	//判断信息内容是否有签名，有则拆分签名
	if(text != ""){
		$('#signature').val("");
		var length = text.length;
		var end = text.lastIndexOf("】");
		var start = text.lastIndexOf("【");
		if(end !=-1 && start != -1 && end == (length-1)){
			$('#signature').val(text.substring(start+1,end));
			$("#msgContents").val(text.substring(0,start));
			$("#msgContentsTemp").val(text);
			BINOLCTCOM04.changeText(text.substring(0,start));
		}else{
			$("#msgContentsTemp").val(text);
			BINOLCTCOM04.changeText(text);
		}
		
	}
	
	// 给弹出框窗体全局变量赋值
	CTCOM_dialogBody= $('#dialogInit').html();
});
