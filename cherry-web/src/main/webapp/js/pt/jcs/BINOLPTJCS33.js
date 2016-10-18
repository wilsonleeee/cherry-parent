var BINOLPTJCS33 = function () {
};
BINOLPTJCS33.prototype = {
	"toSoluDetail" : function(){
		var tokenVal = parentTokenVal();
		$("#parentCsrftoken").val(tokenVal);
		$("#soluDetailForm").submit();
	},
	"toAddPrt" : function(productId){
		var tokenVal = parentTokenVal();
		$("#parentCsrftoken").val(tokenVal);
		$("#toAddForm").find(":input[name='productId']").val(productId);
		$("#toAddForm").submit();
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
		
		var productPriceSolutionIDVal = $("#productPriceSolutionID").val();
		
		param += "&productPriceSolutionID="+ productPriceSolutionIDVal;
		
		var callback = function(msg) {
			// 启用时，若存在有效产品编码条码时，则openDialog,否则直接调用_this.editValidFlag（）方法
			if(msg.indexOf("existProductListIsEmpty1") != -1){
				var dialogSetting = {
						dialogInit: "#dialogChkExistBarCodeDIV",
						bgiframe: true,
						width:  750,
						height: 260,
						resizable : true,
						text: msg,
						title: 	$("#tipTitle").text(),
						confirm: $("#dialogConfirmgoOn").text(),
						confirmEvent: function(){
							removeDialog("#dialogChkExistBarCodeDIV");
						}
				};
				dialogSetting.cancel = $("#dialogClose").text();
				dialogSetting.cancelEvent = function(){removeDialog("#dialogChkExistBarCodeDIV");}
				
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
	},
	/**
	 * 在柜台的text框上绑定下拉框选项
	 * @param Object:options
	 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
	 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
	 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中柜台名称；"code":表示要选中的是柜台CODE；"codeName":表示的是选中的是(code)name。默认是"name"
	 * 
	 * */
	"originalBrandBinding" : function(options){
		var csrftoken = '';
		if($("#csrftokenCode").length > 0) {
			csrftoken = $("#csrftokenCode").serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftokenCode',window.opener.document).serialize();
			}
		} else {
			csrftoken = $('#csrftoken').serialize();
			if(!csrftoken) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
		}
		var strPath = window.document.location.pathname;
		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
		var url = postPath+"/common/BINOLCM21_getCodes.action"+"?"+csrftoken;
		
		$('#'+options.elementId).autocompleteCherry(url,{
			extraParams:{
				originalBrandStr: function() { return $('#'+options.elementId).val();},
				codeType: options.codeType,
				//默认是最多显示50条
				number:options.showNum? options.showNum : 50,
				//默认是选中柜台名称
				selected:options.selected ? options.selected : "value1",
				brandInfoId:function() { return $('#brandInfoId').val();}
			},
		    search: function( event, ui ) {
		        // event 是当前事件对象
		    	alert("search");
		    	$(":input[name=originalBrand]").val('');
		        // ui对象是空的，只是为了和其他事件的参数签名保持一致
		    },
			loadingClass: "ac_loading",
			minChars:1,
		    matchContains:1,
		    matchContains: true,
		    scroll: false,
		    cacheLength: 0,
		    width:230,
		    max:options.showNum ? options.showNum : 50,
			formatItem: function(row, i, max) {
				return escapeHTMLHandle(row[1]);//+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}).result(function(event, data, formatted){
			$('#originalBrandStr').val(data[1]);
			$('#originalBrand').val(data[0]);
			$('#originalBrandStrOld').val(data[1]);
		});
		
	},
	"clearVal" : function(){
		if($(":input[name=originalBrandStr]").val().trim() == ''){
			$(":input[name=originalBrand]").val('');
		}
		if($(":input[name=originalBrand]").val() == ''){
			$(":input[name=originalBrandStr]").val('');
		}
	},
	"clear" : function(){
		if($(":input[name=originalBrandStr]").val().trim() != $(":input[name=originalBrandStrOld]").val()){
			$(":input[name=originalBrand]").val('');
		}
		
	}

};
var BINOLPTJCS33 = new BINOLPTJCS33();

$(function() { 
	// 控制编码条码输入框
//	$("#barCode input").each(function(){
//		var txt = $(this).val();
//		$(this).focus(function(){
//			// 当前元素获得焦点时，如果当前值等于默认值，则清空文本框
//			if(txt === $(this).val()) {
//				$(this).val("");
//			}
//		}).blur(function(){
//			// 当前元素失去焦点时，如果文本框为空，则显示默认值，字体显示灰色
//			if($(this).val() == ""){
//				$(this).val(txt);
//				this.style.color='#D5D5D5'
//			} 
//		}).keydown(function(event){
//			// 当前元素，键盘按下时，字体显示黑色
//			this.style.color='#333'
////	    	var key = event.which;
////	        if(key == "13"){
////	            alert('你输入的内容为：' + txt;
////	        }
//		});
//	});
	if($("input[name='nameTotal']").val() == 0){
		$("#barCodeValId").focus().select(); 
	}
	$('#barCodeValId').bind('keydown',function(event){
        if(event.keyCode == "13") {
        	  $("input[name='nameTotal']").focus().select(); 
//            alert('你输入的内容为：' + $('#barCodeValId').val());
        }
    });
	
	
	
});