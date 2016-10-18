var CTCOM_dialogBody ="";
var BINOLCTCOM07_GLOBAL = function () {
};

BINOLCTCOM07_GLOBAL.prototype = {
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
        var msgLength = msg.value.length;
        msg.focus();
        if(typeof document.selection != "undefined"){
            document.selection.createRange().text = str;  
        }else{
            msg.value = msg.value.substr(0,msg.selectionStart) + str + msg.value.substring(msg.selectionStart,msgLength);
        }
        // 调用信息内容更改函数，改变相关的显示内容
        this.changeText(msg.value);
	},
	
	// 编辑短信页面短信内容编辑框更改内容时相关显示内容的改变
	"changeText" : function(text){
		if(text != ''){
			var signature = $('#signature').val();
			if(signature != ''){
				text = text + "【" + signature + "】";
			}
			$('#msgContentsTemp').val(text);
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
	
	"getTemplateInit" :function(obj){
		var dialogSetting = {
			dialogInit: "#templateDialogInit",
			width: 800,
			height: 500,
			title: $("#chooseTemplateTitle").text()
		};
		openDialog(dialogSetting);
		
		var setMessageUrl = $(obj).attr("href");
		var param = "templateUse=SDXX&messageType=1";
		var callback = function(msg) {
			$("#templateDialogInit").html(msg);
		};
		cherryAjaxRequest({
			url: setMessageUrl,
			param: param,
			callback: callback
		});
	},
	
	"sendMsg" :function(url){
		if(!$('#sendMsgForm').valid()) {
			return false;
		}
		if(!this.validIllegalChar()){
			return false;
		}
		// 参数序列化
		var param = $("#sendMsgForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				if(msg.indexOf('id="fieldErrorDiv"') == -1) {
					$("#div_sendview").html($("#sendResultPage").html());
				}
			},
			coverId: "#pageButton"
		});
	}
	
}

var BINOLCTCOM07 = new BINOLCTCOM07_GLOBAL();

$(document).ready(function() {
	cherryValidate({
		formId: "sendMsgForm",
		rules: {
			msgContents: {required: true,maxlength: 500}
	   }
	});
	
	// 给弹出框窗体全局变量赋值
	CTCOM_dialogBody= $('#sendDialogInit').html();
});


