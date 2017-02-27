var BINOLPTJCS03 = function () {
};
BINOLPTJCS03.prototype = {
	// 删除DIV
	"delDivRow" : function(obj) {
		$(obj).parents('table').remove();
	},
	// 品牌联动
	"getSelOpts" : function(url1,url2) {
		// 参数
		var params = BINOLPTJCS00.getParams();
		ajaxRequest(url1,params,function(msg){$("#cateInfo").html(msg);});
		ajaxRequest(url2,params,function(msg){$("#extInfo").html(msg);});
	}
};
var BINOLPTJCS03 = new BINOLPTJCS03();

$(function() { 
	// 控制编码条码输入框
	$("#barCode input").each(function(){
		var txt = $(this).val();
		$(this).focus(function(){
			// 当前元素获得焦点时，如果当前值等于默认值，则清空文本框
			if(txt === $(this).val()) {
				$(this).val("");
			}
		}).blur(function(){
			// 当前元素失去焦点时，如果文本框为空，则显示默认值，字体显示灰色
			if($(this).val() == ""){
				$(this).val(txt);
				this.style.color='#D5D5D5'
			} 
		}).keydown(function(){
			// 当前元素，键盘按下时，字体显示黑色
			this.style.color='#333'
		});
	});

});

$(function() {
	//产品名称(中文名)前台正则校验
	$("#nameTotal").bind("keyup blur",function() {
		//清空span标签错误信息
		$("#nameTotalTip").html("");
		var nameTotal = $(this).val();
		if($.trim(nameTotal).length != 0) {
			//不能含有半角：分号;    逗号,    单双引号'"      斜杠/\      问号?
			var re = /^[^;,'"/?\\]+$/;
			if (!re.test(nameTotal)) {
				//span标签显示错误信息
				$("#nameTotalTip").html("含有非法字符/\\',;\"?");
			}
		}
	});
});
