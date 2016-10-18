var BINOLPTJCS23 = function () {
};
BINOLPTJCS23.prototype = {
	"toSoluDetail" : function(){
		var tokenVal = parentTokenVal();
		$("#parentCsrftoken").val(tokenVal);
		$("#soluDetailForm").submit();
	},
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
	},
	// 自动生成unitcode
	"getNewUnitCode":function(){
		var url = $("#getUuitCodeUrlId").val();
		
		var $barCode = $("#barCode").find(":input[name='barCode']");
		
		if($barCode.val().trim()==''){
			return false;
		}
		
		var params = 'barCode='+$barCode.val();
		var parentToken = getParentToken();
		params += "&" + parentToken;
//		alert(params);
		cherryAjaxRequest({
			url: url,
			param: params,
			reloadFlg : true,
			callback: function(data){
//				alert(data);
				$("#unitCode").val(data);
				$("#unitCodeSpanId").text(data);
//				alert("getCntCode: " + data);
			}
		});
	},
	// 检查是否存在相同的条码信息
	"chkExistBarCode":function(){
		var that = this;
		
		var url = $("#chkExistBarCodeUrlId").val();
		
		// 参数序列化(基本属性)
		var param = $("#baseInfo").find(":input").not($(":input", "#barCode")).serialize();
		
		// 产品条码
		param += "&barCode=" + this.toJSONArr($("#barCode").find(".vendorSpan"));
		
		var callback = function(msg) {
			// 启用时，若存在有效产品编码条码时，则openDialog,否则直接调用_this.editValidFlag（）方法
			if(msg.indexOf("existProductListIsEmpty1") != -1){
				var dialogSetting = {
						dialogInit: "#dialogChkExistBarCodeDIV",
						bgiframe: true,
						width:  700,
						height: 260,
						resizable : true,
						text: msg,
						title: 	$("#tipTitle").text(),
						confirm: $("#dialogConfirmgoOn").text(),
						confirmEvent: function(){
							removeDialog("#dialogChkExistBarCodeDIV");
						},
						cancel: $("#dialogClose").text(),
						cancelEvent: function(){
							var productPriceSolutionIDVal = $("#productPriceSolutionID").val();
							removeDialog("#dialogChkExistBarCodeDIV");
							if(productPriceSolutionIDVal){
								that.toSoluDetail(); // 跳转到方案明细画面
							}else{
								// 关闭新增画面
								window.close();
							}
						}
				};
				openDialog(dialogSetting);
			}else if(msg.indexOf("existProductListIsEmpty0") != -1){
//				alert("产品中不存在当前的barcode 调试 ");
			}else{
//				alert("else");
			}
			
		};
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			coverId:"#div_main",	
			loadFlg:true
		});
		
	},	// 对象JSON化
	"toJSON" : function(obj) {
		var JSON = [];
		$(obj).find(':input').each(
				function() {
					$this = $(this);
					if (($this.attr("type") == "radio" && this.checked)
							|| $this.attr("type") != "radio") {
						if ($.trim($this.val()) != '') {
							var name = $this.attr("name");
							if (name.indexOf("_") != -1) {
								name = name.split("_")[0];
							}
							JSON.push('"'
									+ encodeURIComponent(name)
									+ '":"'
									+ encodeURIComponent($.trim($this.val())
											.replace(/\\/g, '\\\\').replace(
													/"/g, '\\"')) + '"');
						}
					}
				});
		return "{" + JSON.toString() + "}";
	},
	// 对象JSON数组化
	"toJSONArr" : function($obj) {
		var that = this;
		var JSONArr = [];
		$obj.each(function() {
			JSONArr.push(that.toJSON(this));
		});
		return "[" + JSONArr.toString() + "]";
	}

};
var BINOLPTJCS23 = new BINOLPTJCS23();

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
		}).keydown(function(event){
			// 当前元素，键盘按下时，字体显示黑色
			this.style.color='#333'
//	    	var key = event.which;
//	        if(key == "13"){
//	            alert('你输入的内容为：' + txt;
//	        }
		});
	});
	
	$('#barCodeValId').bind('keydown',function(event){
        if(event.keyCode == "13") {
        	  $("input[name='nameTotal']").focus().select(); 
//            alert('你输入的内容为：' + $('#barCodeValId').val());
        }
    });
	
	
	
});