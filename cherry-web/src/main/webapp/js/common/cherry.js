var childWin;
var popupSwitch = true;
var popupWin;
// HTML转义处理
String.prototype.escapeHTML = function () {
	return this.replace(/&/g, '&amp;')
	.replace(/</g, '&lt;')
	.replace(/>/g, '&gt;')
	.replace(/"/g, '&quot;')
	.replace(/'/g, '&apos;');
};
// HTML反转义处理
String.prototype.unEscapeHTML = function () {
	return this.replace(/&amp;/g,'&')
	.replace(/&lt;/g,'<')
	.replace(/&gt;/g,'>')
	.replace(/&quot;/g,'"')
	.replace(/&apos;/g,"'");
};
//屏蔽右键
document.oncontextmenu=function(){return false};

//禁止F5 以及 Ctrl+R 刷新
document.onkeydown = function(e){
    if ($.browser.msie) {
        if( event.keyCode==116 || ((event.ctrlKey)&&(event.keyCode==82))){
            event.keyCode = 0;
            //阻止IE事件冒泡
            event.cancelBubble=true;
            event.returnvalue=false;
            return false;
         }
     }else{
         if( e.which==116 || ((e.ctrlKey)&&(e.which==82))){
            return false;
         }
     }
}

//form表单转json函数
$.fn.serializeForm2Json = function(submitBlank) {
    var o = {};
    var a = this.serializeArray();
    if(isEmpty(submitBlank)){
    	submitBlank = true;
    }
    $.each(a, function() {
        if (o[this.name]) {
            if (!o[this.name].push) {
                o[this.name] = [ o[this.name] ];
            }
            if(submitBlank || !isEmpty(this.value)){
            	o[this.name].push(this.value || '');
            }
        } else {
        	if(submitBlank || !isEmpty(this.value)){
        		o[this.name] = this.value || '';
        	}
        }
    });
    return JSON.stringify(o);
}

/* 
 * 输入框和下拉框组合控件
 * 
 * Inputs:  Object:options 			组合控件参数	
 * 			options.delay			延时
 * 			options.inputId			输入框ID
 * 			options.btnId			下拉按钮ID
 * 
 */
if (typeof $.widget === 'function') {
	(function($) {
		$.widget("ui.combobox", {
			options: {
				delay: 60,		 // 延时
				inputId: '',     // 输入框ID
				btnId: '',		 // 下拉按钮ID
				range: 'body'	 // 范围
			},
			_create: function() {
				var self = this,
					select = self.element.hide(),
					delay = self.options.delay,
					inputId = self.options.inputId,
					range = self.options.range,
					btnId = self.options.btnId;
				// 输入框设置
				var input = $(inputId,$(range))
					.autocomplete({
						source: function(request, response) {
							// 匹配选项,匹配的字段标记为红色在此段处理
							response(select.children("option").not(':disabled').map(function() {
								var text = $(this).text().replace(/(^\s*)|(\s*$)/g, "");
								// 当输入字符长度大于2开始匹配
								if (!request.term || request.term.length > 0 && text.indexOf(request.term.toUpperCase()) >= 0) {
									return {
										label: escapeHTMLHandle(text),
										value: text,
										option: this
									};
								}
							}));
						},
						// 通过选中下拉选项联动输入框
						select: function(event, ui) {
							select.val( ui.item.option.value );
							ui.item.option.selected = true;
							self._trigger("selected", event, {
								item: ui.item.option
							});
							select.trigger("onchange");
						},
						// 匹配输入字段，如果没有匹配则不显示下拉选项
						change: function(event, ui) {
							var input = $(this),
								inputVal = input.val(),
								valid = false;
							select.children( "option" ).each(function() {
								if ( this.text === inputVal ) {
									this.selected = valid = true;
									return false;
								}
							});
							if (!valid) {
								input.val('');
								select.attr('selectedIndex', -1).val('');
								return false;
							}
						},
						delay: delay,
						minLength: 0
					})
					.dblclick(function() {
						$(this).select();
					})
					.addClass("ui-widget ui-widget-content");
				if (jQuery.browser.mozilla) {
					input[0].addEventListener(
							'input', 
							function() {
								var val = this.value;
							if (val) {
									$(this).autocomplete("search", val);
								}
							}, false
					);
				}
				
				input.data("autocomplete")._renderItem = function( ul, item ) {					
					return $( "<li></li>" )
						.data( "item.autocomplete", item )
						.append( "<a>" + item.label + "</a>" )
						.appendTo( ul );
				};
				// 下拉按钮设置点击事件
				$(btnId,$(range))
				.click(function() {
					if (input.autocomplete("widget").is(":visible")) {
						input.autocomplete("close");
						return;
					}
					input.autocomplete("search", "").focus();
				});
			}
		});
	})(jQuery);
}

/**
 * 刷新盘点的session
 * @param  url:  请求url
 * 
 */
function refreshCherrySession(url){
	
	url += "?csrftoken="+getTokenVal();
//	alert("开始刷新，refreshSessionTimerID=【"+refreshSessionTimerID+"】,url="+url);
	//只请求即可实现session刷新，固没有回调函数
	ajaxRequest(url,null,null);	
}
/* 
 * 设置输入框的值
 * 
 * Inputs:  string:inputId 			输入框ID
 * 			string:defVal			输入框默认显示的内容
 * 
 */
function setInputVal(inputId, defVal, styleVal) {
	if (styleVal) {
		$(inputId).attr("style", styleVal);
	}
	$(inputId).focus(function(){
		if (styleVal) {
			$(this).removeAttr("style");
		}
		if ($(this).val() == defVal) {
			$(this).val('');
		}
	}).focusout(function() {
		if ($(this).val() == '') {
			if (styleVal) {
				$(this).attr("style", styleVal);
			}
			$(this).val(defVal);
		}
	});
}

/* 
 * 初期化输入框的值
 * 
 * Inputs:  string:inputId 			输入框ID
 * 			string:defVal			输入框默认显示的内容
 * 
 */
function initInputVal(inputId, defVal) {
	$(inputId).val(defVal);
}

/* 
 * 查询库存部门
 * 
 * Inputs:  string:obj 			部门下拉框ID
 * 			string:url			Ajax请求地址
 * 			string:selId		仓库下拉框ID
 * 			string:busiType		业务类型
 * 			string:value		下拉选项的值
 * 			string:label		下拉选项的内容
 * 
 */
function getStockDepart(obj, url, selId, busiType, value, label, operType) {
	var token = $('#csrftoken').val();
	// 操作类型
	var operTypeVal = operType || "1";
	var params = $(obj).serialize() + "&busiType=" + busiType + 
				"&csrftoken=" + token + "&operationType=" + operTypeVal;
	$(selId).find("option:not(:first)").remove();
	// 发送Ajax请求
	doAjax(url, value, label, selId, params);
}

/* 
 * 下拉框Ajax请求
 * 
 * Inputs:  string:url			Ajax请求地址
 * 			string:params		Ajax请求的参数
 * 			string:value		下拉选项的值
 * 			string:label		下拉选项的内容
 * 			string:selId		仓库下拉框ID
 * 
 */
function doAjax(url, value, label, selId, params) {
	doAjax2(url, value, label, $(selId),params);
}

/* 
 * 下拉框Ajax请求
 * 
 * Inputs:  string:url			Ajax请求地址
 * 			string:params		Ajax请求的参数
 * 			string:value		下拉选项的值
 * 			string:label		下拉选项的内容
 * 			string:obj			select对象
 * 			string:checkedVal	选中的值
 * 
 */
function doAjax2(url, value, label, obj, params, checkedVal, callback) {	
	$.ajax({
        url: url, 
        type: 'post',
        data: params,
        success: function(msg){
					if(msg.indexOf('pageid=login') > -1){
						if (0 == $("#timeoutform").length) {
							window.close();
							if (window.opener) {
								$("#timeoutform", window.opener.document).submit();
							}
						} else {
							$("#timeoutform").submit();
						}
						return;
					}
					if(window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
				    	for (var one in msgJson){
						    var selVal = msgJson[one][value];
						    var selLab = msgJson[one][label];
					        var str = '<option value="'+ selVal + '">' + escapeHTMLHandle(selLab) + '</option>';
					        if(checkedVal == selVal){
					        	str = '<option selected="selected" value="'+ selVal + '">' + escapeHTMLHandle(selLab) + '</option>';
					        }
					        obj.append(str); 
					    }
					}
					if(typeof(callback) == "function") {
						callback(msg);
					}
				}
    });
}


/* 
 * Ajax返回多个下拉框
 * 
 * Inputs:  Object: object 				参数对象
 * 				object.url				Ajax请求地址
 * 				object.params			参数
 * 				object.selects			下拉框属性
 * 					selects.selId		下拉框ID
 * 					selects.selName		Ajax返回的集合名称
 * 					selects.optVal		下拉框选项值的key
 * 					selects.optLab		下拉框选项内容的key
 * 					selects.firstOpt	是否保留下拉框的第一个选项			
 * 
 */
function doAjaxMulSel(object) {
	$.ajax({
        url: object.url, 
        type: 'post',
        data: object.params,
        success: function(msg){
					if(msg.indexOf('pageid=login') > -1){
						if (0 == $("#timeoutform").length) {
							window.close();
							if (window.opener) {
								$("#timeoutform", window.opener.document).submit();
							}
						} else {
							$("#timeoutform").submit();
						}
						return;
					}
					if(window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
						var selects = object.selects;
						for (var i in selects) {
				    		var options = msgJson[selects[i].selName];
				    		if (options) {
				    			var $selId = $(selects[i].selId);
				    			// 是否保留第一个选项
				    			var firstOpt = selects[i].firstOpt;
				    			if (firstOpt == false) {
				    				// 不保留下拉框的第一个选项
				    				$selId.find("option").remove();
				    			} else {
				    				// 保留下拉框的第一个选项
				    				$selId.find("option:not(:first)").remove();
				    			}
				    			for (var j in options) {
				    				// 选项值
								    var selVal = options[j][selects[i].optVal];
								    // 选项内容
								    var selLab = options[j][selects[i].optLab];
							        var str = '<option value="'+ selVal + '">' + selLab + '</option>';
							        $selId.append(str);
				    			}
				    		}
						}
					}
		    	}
    });
}

/* 
 * 增加了Object的属性值复制的方法
 * 
 * Inputs:  Object:copyObj		复制后的对象
 * 			Object:obj			原始的对象
 * 
 */
Object.copyVal = function(copyObj, obj) {
	for (var property in obj) {
		copyObj[property] = obj[property];
	}
	return copyObj;
};

/* 
 * 弹出新的页面
 * 
 * Inputs:  string:url		新页面的地址
 * 			Object:opt		新页面的属性参数
 * 
 */
function popup(url, opt) {
	if (!popupSwitch) {
		return false;
	}
	var sessionUrl = $('#DUMMYURL').attr('href');
	cherryAjaxRequest({
		url: sessionUrl,
		callback: function(msg) {
			if (!popupSwitch) {
				return false;
			}
			popupSwitch = false;
			var options = {
				name : "",							// 弹出窗口的文件名
				height: 680,						// 窗口高度
				width: 1150,						// 窗口宽度
				top: 0,								// 窗口距离屏幕上方的象素值
				left: 0,							// 窗口距离屏幕左侧的象素值
				toolbar: "no",						// 是否显示工具栏，yes为显示
				menubar: "no",						// 是否显示菜单栏，yes为显示
				scrollbars: "yes",					// 是否显示滚动栏，yes为显示
				resizable: "yes",					// 是否允许改变窗口大小，yes为允许
				location: "no",						// 是否显示地址栏，yes为允许
				status: "no",						// 是否显示状态栏内的信息（通常是文件已经打开），yes为允许
				center: "yes",						// 页面是否居中显示
				childModel: 1						// 弹出子页面的模式(1:只弹出一个子页面，2:可以弹出多个子页面，3:在一个窗口里切换页面)
			};
			Object.copyVal(options, opt || {});
			if (options["center"] == "yes") {
				options.top = (screen.height - 30 - options["height"])/2;
				options.left = (screen.width - 10 - options["width"])/2;
			}
			var params = "height=" + options["height"] + 
						", width=" + options["width"] + 
						", top=" + options["top"] + 
						", left=" + options["left"] + 
						", toolbar=" + options["toolbar"] + 
						", menubar=" + options["menubar"] + 
						", scrollbars=" + options["scrollbars"] + 
						", resizable=" + options["resizable"] + 
						", location=" + options["location"] + 
						", status=" + options["status"];
			// popup新窗口
			if (1 != options.childModel || !childWin || !isChildWinOpen()) {
				if (!url) {
					return false;
				}
				if (3 == options.childModel && null != childWin) {
					childWin.close();
				}
				// 新建form对象
				var tempForm = document.createElement("form");
				var urlIndex = url.indexOf("?");
				if (-1 == urlIndex) {
					// 直接弹出新页面
					childWin = window.open(url, options["name"], params);
				} else {
					// 不带参数的地址
					var tempUrl = url.substring(0, url.indexOf("?"));
					// 参数
					var tempParam = url.substring(url.indexOf("?") + 1);
					var parr = tempParam.split("&");
					if (parr) {
						for (var i in parr) {
							var tempInputArr = parr[i].split("=");
							if (2 == tempInputArr.length) {
								var name = tempInputArr[0];
								var val = tempInputArr[1];
								// 新建input元素用于提交参数
								var tempInput = document.createElement("input");
								tempInput.type = "hidden";
								tempInput.name = name;
								tempInput.value = val;
								tempForm.appendChild(tempInput);
							}
						}
					}
					var pageName = "ChildPage" + randomNum(10);
					tempForm.method = "post";
					tempForm.action = tempUrl;
					tempForm.target = pageName;
					document.body.appendChild(tempForm);
					// post方式弹出新页面
					childWin = window.open("", pageName, params);
					tempForm.submit();
					document.body.removeChild(tempForm);
				}
			}
			popupSwitch = true;
		}
	});
}

function isChildWinOpen() {
	try {
		if(childWin.closed || childWin.document == null) {
			return false;
		}
		return true;
	} catch(ex) {}
	return false;
} 

/* 
 * 生成指定长度的随机数
 * 
 * Inputs:  int:length		随机数的长度
 * 			
 * 
 */
function randomNum(length) {
	var arr = new Array("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z","A","B","C","D","E","F","G","H","I","Z","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z");
	var num = "";
	for(i = 1; i <= length; i++){
		num = num + arr[Math.floor(Math.random()*62)];
	} 
	return num;
}

/* 
 * 禁用父页面
 * 
 * 
 */
function lockParentWindow(){
	popupWin = childWin;
	if ($('#coverAllBody').length > 0){
		return false;
	}
	// 遮盖层
	var html = '<div id="coverAllBody" style="position:absolute;z-index: 30;opacity: 0.5;filter: alpha(opacity=50);background-color: #808080;display: block;" onclick="removeCoverDiv();"></div>';
	if($("div.ui-dialog:visible").length>0){
		html = '<div id="coverAllBody" style="position:absolute;z-index: 30000;opacity: 0.5;filter: alpha(opacity=50);background-color: #808080;display: block;" onclick="removeCoverDiv();"></div>';
	}
	$('body').append(html); 
	var offset = $('body').offset();
	var iWinHeight = $(window).height();
	var iDocHeight = $(document).height();
	$('#coverAllBody').css({
		top: offset.top + 'px',
		left: offset.left + 'px',
		"min-width" : '992px',
		"width": "100%",
		height: ((iWinHeight>iDocHeight)? iWinHeight : iDocHeight) +"px"
	});
}

/* 
 * 恢复使用父页面
 * 
 * 
 */
function unlockParentWindow(){
	if ($('#coverAllBody').length > 0){
		$('#coverAllBody').remove();
	}
}

/* 
 * 详细画面的datatable设置
 * 
 * 
 */
function initDetailTable(opt, id) {
	if (!+"\v1") {
		var $divId;
		if (id) {
			$divId = $(id);
		} else {
			$divId = $("#table_scroll");
		}
		if ($divId.length > 0) {
			$divId.append('<div style="height:20px;"></div>');
		}
	}
}

/* 
 * 弹出新的页面
 * 
 * Inputs:  Object:obj		打开新画面的链接
 * 
 */
function openWin(obj,opt) {
	// 新页面地址
	var url = $(obj).attr("href");
	var token = $('#csrftoken').val();
	if (url) {
		var joinChar = "";
		if (url.lastIndexOf("?") == -1) {
			joinChar = "?";
		} else if (url.lastIndexOf("?") < url.length - 1) {
			joinChar = "&";
		}
		url += joinChar + "csrftoken=" + token;
		popup(url,opt);
	}
}

/* 
 * 弹出新的页面
 * 
 * Inputs:  String:url		打开新画面的链接
 * 			Object:opt		新画面的设置参数
 * 
 */
function openWinByUrl(url,opt) {
	if (url) {
		var token = $('#csrftoken').val();
		var joinChar = "";
		if (url.lastIndexOf("?") == -1) {
			joinChar = "?";
		} else if (url.lastIndexOf("?") < url.length - 1) {
			joinChar = "&";
		}
		url += joinChar + "csrftoken=" + token;
		popup(url,opt);
	}
}

/* 
 * 关闭dialog
 * 
 * Inputs:  String:dialogID		dialog的ID
 * 
 */
function closeCherryDialog (dialogID,dialogBody){
	var tmpHtml = $('#'+dialogID).html();
	removeDialog('#'+dialogID);
	if (dialogBody!=null){
		$('#'+dialogID).html(dialogBody);	
	}else{
		$('#'+dialogID).html(tmpHtml);	
	}
	
	$("body").unbind('click');

}

/* 
 * 打开dialog
 * 
 * Inputs:  Object:dialogSetting		生成dialog的参数
 * 
 */
function openDialog(dialogSetting) {
	var buttons = [];
	if(dialogSetting.confirm != null) {
		buttons.push({
			text: dialogSetting.confirm,
			click: function() {dialogSetting.confirmEvent();}
		});
	}
	if(dialogSetting.confirm2 != null) {
		buttons.push({
			text: dialogSetting.confirm2,
			click: function() {dialogSetting.confirm2Event();}
		});
	}
	if(dialogSetting.cancel != null) {
		buttons.push({
			text: dialogSetting.cancel,
			click: function() {dialogSetting.cancelEvent();}
		});
	}
	var _dialogSetting = {
		bgiframe: true,
		width: dialogSetting.width,
		height: dialogSetting.height,
		position: dialogSetting.position,
		title: dialogSetting.title,
		zIndex: 30,  
		modal: true, 
		resizable: false,
		close: function() {removeDialog(dialogSetting.dialogInit);},
		open: dialogSetting.open
	};
	
	if(dialogSetting.resizable){
		_dialogSetting.resizable = dialogSetting.resizable;
	}
	
	if(buttons.length > 0) {
		_dialogSetting.buttons = buttons;
	}
	if(dialogSetting.minHeight){
		_dialogSetting.minHeight = dialogSetting.minHeight;
	}
	if(dialogSetting.maxHeight){
		_dialogSetting.maxHeight = dialogSetting.maxHeight;
	}
	if(dialogSetting.maxWidth){
		_dialogSetting.maxWidth = dialogSetting.maxWidth;
	}
	if(dialogSetting.minWidth){
		_dialogSetting.minWidth = dialogSetting.minWidth;
	}
	if(dialogSetting.zIndex){
		_dialogSetting.zIndex = dialogSetting.zIndex;
	}
	if(typeof dialogSetting.closeEvent == "function") {
		_dialogSetting.close = function(){dialogSetting.closeEvent();};
	}
	var $dialogInit = $(dialogSetting.dialogInit);
	if(dialogSetting.text != null) {
		$dialogInit.html(dialogSetting.text);
	} else {
		$dialogInit.html($("#dialogInitMessage").html());
	}
	$dialogInit.dialog(_dialogSetting);
}

/* 
 * 关闭dialog
 * 
 * Inputs:  String:dialogDiv		'#'+dialog的ID
 * 
 */
function removeDialog(dialogDiv) {
	var dialogDivId = $(dialogDiv).attr("id");
	$(dialogDiv).dialog('destroy');
	$(dialogDiv).remove();
	$('#div_main').append('<div style="display:none" id="'+dialogDivId+'"></div>');
}

/* 
 * 取得父页面的Token
 * 
 * 
 * 
 */
function getParentToken() {
	if (window.opener) {
		return $('#csrftoken', window.opener.document).serialize();
	}
}

/* 
 * 序列化Token值
 * 	当本页面没Token时，取得父页面Token值
 * 
 * 
 */
function getSerializeToken() {
	if($("#csrftokenCode").length > 0) {
		var $csrftoken = $('#csrftokenCode');
		if($csrftoken.length == 0){
			if (window.opener) {
				return $('#csrftokenCode', window.opener.document).serialize();
			}
		}else{
			return $csrftoken.serialize();
		}
	} else {
		var $csrftoken = $('#csrftoken');
		if($csrftoken.length == 0){
			return getParentToken();
		}else{
			return $csrftoken.serialize();
		}
	}
}

/* 
 * 序列化Token值
 * 	当本页面没Token时，取得父页面Token值
 * 
 * 
 */
function getTokenVal() {
	var $csrftoken = $('#csrftoken');
	if($csrftoken.length == 0){
		return parentTokenVal();
	}else{
		return $csrftoken.val();
	}
}
/* 
 * 取得父页面的Token的值
 * 
 * 
 * 
 */
function parentTokenVal() {
	if (window.opener) {
		return $('#csrftoken', window.opener.document).val();
	}
}

/* 
 * 画面表单js验证共通
 * 
 * Inputs:  Object:options      			validate验证的参数设置
 *          		String:formId			需要验证的form的id
 *          		Object:rules			各表单的验证规则对象
 * 
 */
function cherryValidate(options) {
	if(options && options.formId && options.rules) {
		$.validator.classRules = function(element){return {};};
		var setting = {
			submitHandler: function(form) { form.submit(); },
			rules: {},
			errorPlacement: function(error,element){
				var $parent = element.parent();
				$parent.removeClass('error');
				$parent.find('#errorText').remove();
				if($("#cluetip").is(":visible")) {
					$("#cluetip").remove();
					$("#cluetip-waitimage").remove();
				}
				if(error.text()) {
					$parent.addClass("error");
					$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
					$parent.find('#errorText').attr("title",'error|'+error.text());
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
				}
			},
			success: function(){return false;},
			focusInvalid: false,   
			onkeyup: false,
			onclick: false
		};
		$.extend(setting,options || {});
		$('#'+options.formId).validate(setting);
	}
}

/* 
 * 全选
 * 
 * Inputs:  Object:obj		全选对象
 * 			String:tableId 	表格ID
 * 			String:childId  子节点ID
 * 
 */
function doSelectAll(obj, tableId, childId){
	var $selTableId = (childId)? $(tableId).children(childId) : $(tableId);
	if ($(obj).is(":checked")) {
		// 全部选中
		$selTableId.find(":checkbox:enabled").attr("checked", true);
	} else {
		// 全部不选
		$selTableId.find(":checkbox:enabled").attr("checked", false);
	}
}

/* 
 * 设置全选按钮状态
 * 
 * Inputs:  Object:obj		选中的对象
 * 			String:tableId 	表格ID
 * 			String:btnId   	按钮ID
 * 			String:childId  子节点ID
 * 
 */
function changeAllSelect(obj, tableId, btnId, childId){
	var $selTableId = (childId)? $(tableId).children(childId) : $(tableId);
	var selId = btnId || "#allSelect";
	if ($(obj).is(":checked")) {
		if ($selTableId.find(":checkbox:not(:checked)").length == 0) {
			// 选中
			$(selId).attr("checked", true);
		}
	} else {
		// 不选
		$(selId).attr("checked", false);
	}
}

/* 
 * 设置行样式
 * 
 * Inputs:  String:tableId 	需要设置行号的表格ID
 * 
 */
function setRowClass(tableId) {
	$(tableId).find("tr:visible:odd").attr("class", "even");
	$(tableId).find("tr:visible:even").attr("class", "odd");
}

/*
 * 给textarea框加入长度限制
 * @param o
 * @return
 */
function isMaxLen(o){  
	 var nMaxLen=o.getAttribute? parseInt(o.getAttribute("maxlength")):"";  
	 if(o.getAttribute && o.value.length>nMaxLen){  
	 o.value=o.value.substring(0,nMaxLen);
	 }  
} 
/*
 * 给文本框加入字节长度限制(一个中文占两个字节)
 * 
 * @param o:调用此方法的文本框对象
 * @return 
 *
 */
function isMaxByteLen(o){
	var nMaxLen=o.getAttribute? parseInt(o.getAttribute("maxlength")):"";
	var bytesCount=0;
	var str=o.value;
	for (var i = 0; i < str.length; i++){
      var c = str.charAt(i);
      if (/^[\u0000-\u00ff]$/.test(c))
      {
        bytesCount += 1;
      }
      else
      {
        bytesCount += 2;
      }
	}
	if(o.getAttribute && bytesCount>nMaxLen){  
		 o.value=o.value.substring(0,str.length-1);
		 isMaxByteLen(o);//循环调用，直到字节数不再超过限制数
	}
}
/* 
 * 表格排序
 * 
 * Inputs:  String:tableId 	表格ID
 * 			int:rowNumKbn	是否需要重新编号
 */
function tableSort(tableId, rowNumKbn) {
	$(tableId).each(function(){
		var $table = $(this);
		$("th",$table).each(function(column){				
			var findSortKey;
			// 排序图标
			var span = "<span class='sort-icon right ui-icon ui-icon-carat-2-n-s'></span>";
			// 文字排序
			if ($(this).is(".th-sort-alpha")){	
				$(this).append(span);
				findSortKey = function($cell){						
					return $cell.text();	
				};
			}
			// 数字排序
			else if($(this).is(".th-sort-numeric")){
				$(this).append(span);
				findSortKey = function($cell){					
					var key = parseFloat($cell.text().replace(/[^\d.-]*/g,""));
					return isNaN(key) ? 0 : key;
				};
			}
			// 日期排序
			else if($(this).is(".th-sort-date")){
				$(this).append(span);
				findSortKey = function($cell){
					var date = $cell.text().replace(/(^\s*)|(\s*$)/g, "");
					if (/^(\d{4})\/|\-(\d{1,2})\/|\-(\d{1,2})$/.test(date)) {
						var splitChar = date.charAt(4);
						var dateArray = date.split(splitChar);
						var yearKey = dateArray[0];
						var monthKey = dateArray[1].length == 1? "0" + dateArray[1] : dateArray[1];
						var dayKey = dateArray[2].length == 1? "0" + dateArray[2] : dateArray[2];
						date = yearKey + monthKey + dayKey;
					} else {
						date = "";
					}
					return date;
				};
			}
			
			if(findSortKey){				
				$(this).click(function(){
					var newDirection = 1;
					if($(this).is(".sorted-asc")){
						newDirection = -1;
					}
					$th = $table.find("th");
					$th.removeClass("sorted-asc").removeClass("sorted-desc");
					$th.find("span.sort-icon").attr("class", "sort-icon right ui-icon ui-icon-carat-2-n-s");
					var $sortHead = $table.find('th').filter(":nth-child(" + (column+1) + ")");
					if(newDirection == 1){
						$sortHead.addClass("sorted-asc");
						// 升序图标
						$sortHead.find("span.sort-icon").attr("class", "sort-icon right ui-icon ui-icon-triangle-1-n");
					}
					else{
						$sortHead.addClass("sorted-desc");
						// 降序图标
						$sortHead.find("span.sort-icon").attr("class", "sort-icon right ui-icon ui-icon-triangle-1-s");
					}
					var rows = $table.find("tbody > tr").get();
					$.each(rows,function(index,row){
						row.sortKey = findSortKey($(row).children("td").eq(column));
					});
					rows.sort(function(a,b){
						if(a.sortKey < b.sortKey) return -newDirection;
						if(a.sortKey > b.sortKey) return newDirection;
						return 0;
					});
					$.each(rows,function(index,row){
						$table.children("tbody").append(row);
						row.sortKey = null;
					});
					$table.find('td').removeClass("sorting_1").filter(":nth-child(" + (column+1) + ")").addClass("sorting_1");
					// 设置行样式
					setRowClass(tableId + " > tbody");
					if (1 == rowNumKbn) {
						return false;
					}
					// 设置行号
					tableRowNum(tableId + " > tbody");
				});
			};
		});
	});
}

/* 
 * 设置行号
 * 
 * Inputs:  String:tableId 	需要设置行号的表格ID
 * 
 */
function tableRowNum(tableId){
	var $trs = $(tableId).find("tr:visible");
	for (var i=0 ; i<$trs.length; i++) {
		var tr = $trs[i];
		// 行号
		$(tr).find("td.rowNum").text(i+1);
	}
}

/* 
 * 深层克隆对象
 * 
 * Inputs:  jsonObj:需要克隆的对象
 * 
 */
function  clone(jsonObj) {   
    var  buf;   
    if  (jsonObj  instanceof  Array) {   
        buf = [];   
        var  i = jsonObj.length;   
        while  (i--) {   
            buf[i] = clone(jsonObj[i]);   
        }   
        return  buf;   
    }else   if  (jsonObj  instanceof  Object){   
        buf = {};   
        for  ( var  k  in  jsonObj) {   
            buf[k] = clone(jsonObj[k]);   
        }   
        return  buf;   
    }else {   
        return  jsonObj;   
    }   
} 

function changeDateToString(DateIn)
{
    var Year=0;
    var Month=0;
    var Day=0;
    var CurrentDate="";
    //初始化时间
    Year      = DateIn.getFullYear();
    Month     = DateIn.getMonth()+1;
    Day       = DateIn.getDate();
    CurrentDate = Year + "-";
    if (Month >= 10 )
    {
        CurrentDate = CurrentDate + Month + "-";
    }
    else
    {
        CurrentDate = CurrentDate + "0" + Month + "-";
    }
    if (Day >= 10 )
    {
        CurrentDate = CurrentDate + Day ;
    }
    else
    {
        CurrentDate = CurrentDate + "0" + Day ;
    }
    return CurrentDate;
}

/* 
 * 去除遮盖层
 * 
 * 
 */
function removeCoverDiv() {   
	if (null != popupWin && popupWin.closed) {
		$("#coverAllBody").remove();
	}
}

// 数据权限刷新确认
function privilegeRefreshConfirm(refreshPrivilegeUrl) {
	var title = $('#privilegeTitle').text();
	var text = $('#privileMessage').html();
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){privilegeRefreshHandle(refreshPrivilegeUrl);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

// 数据权限刷新处理
function privilegeRefreshHandle(refreshPrivilegeUrl) {
	
	if(refreshPrivilegeUrl) {
		refreshPrivilegeUrl += '?refreshFlag=1';
		cherryAjaxRequest({
			url: refreshPrivilegeUrl,
			param:null,
			reloadFlg : true,
			callback: function(msg) {
			}
		});
	}
	removeDialog("#dialogInit");
}

function escapeHTMLHandle(s) {
	if(typeof s=="string") {
		return s.escapeHTML();
	} else {
		return s;
	}
}

/**
 * 在柜台的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中柜台名称；"code":表示要选中的是柜台CODE；"codeName":表示的是选中的是(code)name。默认是"name"
 * 
 * */
function counterBinding(options){
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
	var url = postPath+"/common/BINOLCM21_getCounterInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			counterInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name",
			brandInfoId:function() { return $('#brandInfoId').val();},
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else if(options.selected=="codeName"){
				return escapeHTMLHandle(row[2])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	});
	
}

/**
 * 在产品的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为prtVendorId
 *              String:options.targetShow 参数可选，选中下拉列表中的某个值时，显示在text框上的值，默认为nameTotal
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到单据明细，用于发货添加新行等，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择产品后需要执行的方法（如判断重复、查询库存），targetDetail=true时调用。
 *              String:option.proType 参数可选，默认为产品，值为prm时表示促销品
 * 
 * */
function productBinding(options){
	var recordPrtName = null;
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getProductInfo.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "prtVendorId";
	var targetShow = options.targetShow ? options.targetShow : "nameTotal";
	if(options.proType == "prm"){
		url = postPath+"/common/BINOLCM21_getPrmProductInfo.action"+"?"+csrftoken;
	}	
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			productInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			validFlag : function() { 
					if(options.validFlag){
						 var $validFlag = $('input[name='+options.validFlag+']:checked').val();
						 if (!$validFlag){//如果是下拉框，则取下拉框中的值
							 $validFlag=  $('#'+options.validFlag).val();
						 }
						 return $validFlag;
					} else {
						return 1;
					}
				}
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[1])+"-"+escapeHTMLHandle(row[2])+" ["+escapeHTMLHandle(row[0])+"]";
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			if(targetId == 'prtVendorId'){
				$("#"+targetId).val(data[3]);
			}else if(targetId == 'productId'){
				$("#"+targetId).val(data[4]);
			}else if(targetId == 'unitCode'){
				$("#"+targetId).val(data[1]);
			}else{
				$("#"+targetId).val(data[3]);
			}
			if(targetShow == 'unitCode'){
				$('#'+options.elementId).val(data[1]);
			} else if(targetShow == 'barCode') {
				$('#'+options.elementId).val(data[2]);
			} else {
				$('#'+options.elementId).val(data[0]);
			}
			$('#'+options.elementId).data("prtName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.productName = data[0];
			obj.unitCode = data[1];
			obj.barCode = data[2];
			obj.prtVendorId = data[3];
			obj.prtId = data[4];
			obj.price = data[5];
			obj.memPrice = data[6];
			obj.standardCost = data[7];
			obj.orderPrice = data[8];
			obj.IsExchanged = data[9];
			//选择产品后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName"))){
//				alert($('#'+options.elementId).val()+","+$('#'+options.elementId).data("prtName"));
				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}


/**
 * 在产品的text框上绑定下拉框选项（浓妆淡抹订货商城）
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为prtVendorId
 *              String:options.targetShow 参数可选，选中下拉列表中的某个值时，显示在text框上的值，默认为nameTotal
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到单据明细，用于发货添加新行等，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择产品后需要执行的方法（如判断重复、查询库存），targetDetail=true时调用。
 *              String:option.proType 参数可选，默认为产品，值为prm时表示促销品
 * 
 * */
function nzdmProductBinding(options){
	var recordPrtName = null;
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getProductInfoNZDM.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "prtVendorId";
	var targetShow = options.targetShow ? options.targetShow : "nameTotal";
	if(options.proType == "prm"){
		url = postPath+"/common/BINOLCM21_getPrmProductInfo.action"+"?"+csrftoken;
	}	
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			productInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			validFlag : function() { 
					if(options.validFlag){
						 var $validFlag = $('input[name='+options.validFlag+']:checked').val();
						 return $validFlag;
					} else {
						return 1;
					}
				}
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[1])+"-"+escapeHTMLHandle(row[2])+" ["+escapeHTMLHandle(row[0])+"]";
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			if(targetId == 'prtVendorId'){
				$("#"+targetId).val(data[3]);
			}else if(targetId == 'productId'){
				$("#"+targetId).val(data[4]);
			}else if(targetId == 'unitCode'){
				$("#"+targetId).val(data[1]);
			}else{
				$("#"+targetId).val(data[3]);
			}
			if(targetShow == 'unitCode'){
				$('#'+options.elementId).val(data[1]);
			} else if(targetShow == 'barCode') {
				$('#'+options.elementId).val(data[2]);
			} else {
				$('#'+options.elementId).val(data[0]);
			}
			$('#'+options.elementId).data("prtName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.productName = data[0];
			obj.unitCode = data[1];
			obj.barCode = data[2];
			obj.prtVendorId = data[3];
			obj.prtId = data[4];
			obj.price = data[5];
			obj.memPrice = data[6];
			obj.standardCost = data[7];
			obj.orderPrice = data[8];
			obj.IsExchanged = data[9];
			obj.distributionPrice = data[10];
			obj.stockAmount = data[11];
			//选择产品后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName"))){
//				alert($('#'+options.elementId).val()+","+$('#'+options.elementId).data("prtName"));
//				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
//			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}


/**
 * 在柜台产品的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.counterCode 柜台Code,当前使用的柜台Code
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为prtVendorId
 *              String:options.targetShow 参数可选，选中下拉列表中的某个值时，显示在text框上的值，默认为nameTotal
 *              options.afterSelectFun 参数可选，选择产品后需要执行的方法（如判断重复、查询库存），targetDetail=true时调用。
 *              String:option.proType 参数可选，默认为产品，值为prm时表示促销品
 *              String:option.originalBrandStr 参数可选，输入子品牌查询字符串
 * 
 * */
function cntProductBinding(options){
	var recordPrtName = null;
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var eventFlag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getCntProductInfo.action"+"?"+csrftoken;
	var originalBrandStr = options.originalBrandStr;
	if(originalBrandStr!=undefined && originalBrandStr!=null &&　originalBrandStr!=""){
		url+="&originalBrandStr="+options.originalBrandStr;
	}
	var targetId = options.targetId ? options.targetId : "prtVendorId";
	var targetShow = options.targetShow ? options.targetShow : "nameTotal";
	if(options.proType == "prm"){
		url = postPath+"/common/BINOLCM21_getPrmProductInfo.action"+"?"+csrftoken;
	}	
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			productInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			validFlag : function() { 
				if(options.validFlag){
					var $validFlag = $('input[name='+options.validFlag+']:checked').val();
					return $validFlag;
				} else {
					return 1;
				}
			},
			counterCode : options.counterCode
//			counterCode : "NR1SS908"
		},
		loadingClass: "ac_loading",
		minChars:1,
		matchContains:1,
		matchContains:true,
		scroll:false,
		delay:200,
		cacheLength:0,
		width:300,
		max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			if(max == 1){
				// 为适用扫描枪扫描输入方式，在匹配结果为1的情况下自动执行afterSelectFun方法
				if(!eventFlag){
					var productObj = {};
					productObj.elementId = options.elementId;
					productObj.productName = row[0];
					productObj.unitCode = row[1];
					productObj.barCode = row[2];
					productObj.prtVendorId = row[3];
					productObj.prtId = row[4];
					productObj.price = row[5];
					productObj.memPrice = row[6];
					productObj.standardCost = row[7];
					productObj.IsExchanged = row[8];
					productObj.stockQuantity = row[10];
					productObj.isSocket = row[11];
					productObj.platinumPrice = row[12];
					productObj.tagPrice = row[13];
					// 选择产品后执行方法
					if($.isFunction(options.afterSelectFun)){
						options.afterSelectFun.call(this,productObj);
					}
					// 更改事件状态防止重复输入
					eventFlag = true;
					// 延时0.5秒后解除状态(自动完成插件击键后激活autoComplete的延迟时间默认远程为400毫秒 本地为10毫秒，解除状态需要在激活autoComplete之后，若设置了delay参数，setTimeout时间需要做相应的调整 )
					var refresh = function() {
						eventFlag = false;
					}
					setTimeout(refresh,500);
					return null;
//					return row[9] == 0 ? 
//							escapeHTMLHandle(row[1])+"-"+escapeHTMLHandle(row[2])+" ["+escapeHTMLHandle(row[0])+"]"
//							: "[" + escapeHTMLHandle(row[0])+"] " + escapeHTMLHandle(row[2]); // 小店云场景模式，显示的不同。
				}
			}else{
//				productObj.stockQuantity = row[9];
				return row[9] == 0 ? 
						escapeHTMLHandle(row[1])+"-"+escapeHTMLHandle(row[2])+" ["+escapeHTMLHandle(row[0])+"]"
						: "[" + escapeHTMLHandle(row[0])+"] " + escapeHTMLHandle(row[2]);
			}
		}
	}).result(function(event, data, formatted){
		if(!eventFlag){
			if(options.targetDetail == null || options.targetDetail == undefined){
				if(targetId == 'prtVendorId'){
					$("#"+targetId).val(data[3]);
				}else if(targetId == 'productId'){
					$("#"+targetId).val(data[4]);
				}else if(targetId == 'unitCode'){
					$("#"+targetId).val(data[1]);
				}else{
					$("#"+targetId).val(data[3]);
				}
				if(targetShow == 'unitCode'){
					$('#'+options.elementId).val(data[1]);
				} else if(targetShow == 'barCode') {
					$('#'+options.elementId).val(data[2]);
				} else {
					$('#'+options.elementId).val(data[0]);
				}
				$('#'+options.elementId).data("prtName",$("#"+targetId).val());
				$('#'+options.elementId).data("change",true);
			}else if(options.targetDetail == true){
				var obj = {};
				obj.elementId = options.elementId;
				obj.productName = data[0];
				obj.unitCode = data[1];
				obj.barCode = data[2];
				obj.prtVendorId = data[3];
				obj.prtId = data[4];
				obj.price = data[5];
				obj.memPrice = data[6];
				obj.standardCost = data[7];
				obj.IsExchanged = data[8];
				obj.stockQuantity = data[10];
				obj.isSocket = data[11];
				obj.platinumPrice = data[12];
				obj.tagPrice = data[13];
				//选择产品后执行方法
				if($.isFunction(options.afterSelectFun)){
					options.afterSelectFun.call(this,obj);
				}
			}
		}else{
			$('#'+options.elementId).val("");
		}
	});
}

/**
 * 在部门的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中部门名称；"code":表示要选中的是部门CODE，默认是"name"
 * 				String:options.includeCounter 参数可选，查询结果中是否包含柜台，只要提供该参数就表示包含柜台信息，默认是不包含
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 * 
 * */
function departBinding(options){
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
	var url = postPath+"/common/BINOLCM21_getDepartInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			departInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name",
			//是否包含柜台，默认是不包含
			flag:function(){
					if(options.includeCounter){
						return options.includeCounter;
					}else{
						return "";
					}
				},
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	});
}

/**
 * 产品分类的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中员工名称；"code":表示要选中的是员工CODE，默认是"name"
 * 
 * 
 * */
function productCategoryBinding(options){
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
	var url = postPath + "/common/BINOLCM21_getProductCategory.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			prtCatInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name"
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[0])+" "+ (row[1] ? "["+escapeHTMLHandle(row[1])+"]" : "");
			}else{
				return escapeHTMLHandle(row[1])+" "+ (row[0] ? "["+escapeHTMLHandle(row[0])+"]" : "");
			}
		}
	});
}

/**
 * 在仓库的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中仓库名称；"code":表示要选中的是仓库CODE，默认是"name"
 * 				String:options.onlyNoneCounterDeport 参数可选，只查询出非柜台仓库，默认是查询所有仓库信息
 * 				String:options.onlyCounterDeport 参数可选，只查询出柜台仓库，默认是查询所有仓库信息
 * 				String:options.includeRegion 包含区域信息，默认是不包含
 * 				String:options.includeDepart 包含部门信息，默认是不包含(用于实体仓库业务配置)
 * 				String:options.onlyDepart 只查部门信息，默认否(用于实体仓库业务配置)
 * 
 * */
function deportBinding(options){
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
	var url = postPath+"/common/BINOLCM21_getDeportInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			deportInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name",
			//是否包含柜区域信息，默认是不包含
			flag:function(){
					if(options.includeRegion){
						return options.includeRegion;
					}else{
						return "";
					}
				},
				//是否包含柜区域信息，默认是不包含
				includeDepart:function(){
					if(options.includeDepart){
						return options.includeDepart;
					}else{
						return "";
					}
				},
				onlyDepart:function(){
					if(options.onlyDepart){
						return options.onlyDepart;
					}else{
						return "";
					}
				},
			//只查询出柜台仓库
			onlyCounterDeport:function(){
					if(options.onlyCounterDeport){
						return options.onlyCounterDeport;
					}else{
						return "";
					}
				},
			//只查询出非柜台仓库
			onlyNoneCounterDeport:function(){
					if(options.onlyNoneCounterDeport){
						return options.onlyNoneCounterDeport;
					}else{
						return "";
					}
				}
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	});
}

/**
 * 在员工的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中员工名称；"code":表示要选中的是员工CODE，默认是"name"
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 * 
 * 
 * */
function employeeBinding(options){
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
	var url = postPath + "/common/BINOLCM21_getEmployeeInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			employeeInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name",
			// 是否带权限查询		
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0		
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	});
}

/**
 * 在营业员的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.selectedId 选中项的营业员ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中营业员名称；"code":表示要选中的是营业员CODE，默认是"name"
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 * 				String:options.filterBaCode 参数可选，需要过滤的BaCode值，为空的场合默认不过滤
 * 
 * */
function baInfoBinding(options){
	var $selectedId = $("#baInfoId");
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath + "/common/BINOLCM21_getBaInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			employeeInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中BA名称
			selected:options.selected ? options.selected : "name",
			// 是否带权限查询
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0,
			// 需要过滤的BaCode值
			filterBaCode : options.filterBaCode ? options.filterBaCode : ""
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	}).result(function(event, data, formatted){
		if(options.selectedId ? true : false) {
			$selectedId = $('#'+options.selectedId);
		}
		$selectedId.val(data[2]);
		$('#'+options.elementId).data("BaInfoName",$selectedId.val());
		$('#'+options.elementId).data("change",true);
		$('#'+options.elementId).parent().removeClass('error');
		$('#'+options.elementId).parent().find('#errorText').remove();
		$selectedId.parent().removeClass('error');
		$selectedId.parent().find('#errorText').remove();
	}).bind("keydown",function(event){
		var flag = false;
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("BaInfoName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("BaInfoName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("BaInfoName"))){
				$selectedId.val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("BaInfoName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("BaInfoName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$selectedId.val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}

/**
 * 在代理商的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.selectedCode 选中项的代理商CODE
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中代理商名称；"code":表示要选中的是代理商CODE，默认是"name"
 * 				String:options.flag 参数必须，标记查询的是ALL:不区分一级/二级代理商； 1：一级代理商；2：二级代理商,默认二级代理商
 * 				String:options.parentKey 参数可选，当flag为2时查找的上级代理商KEY
 * 				String:options.resellerTypeKey 参数可选，用于筛选所属代理商类型
 * 				String:options.provinceKey 参数可选，用于筛选所属省份
 * 				String:options.cityKey 参数可选，用于筛选所属城市
 * */
function resellerInfoBinding(options){
	var flag = options.flag;
	var $selectedCode = $("#resellerCode");
	if('1'== flag) {
		$selectedCode = $("#parentResellerCode");
	} 
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath + "/common/BINOLCM21_getResellerInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			textInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中BA名称
			selected:options.selected ? options.selected : "name",
			// 默认查询的是二级代理商
			flag : options.flag ? options.flag : '2',
			// 上级代理商
			parentValue:function() {return options.flag == '2' ? $('#'+options.parentKey).val() : '';},
			// 代理商类型
			resellerType : function() {return $('#'+options.resellerTypeKey).val();},
			// 所属省份
			provinceId : function() {return $('#'+options.provinceKey).val();},
			// 所属城市
			cityId : function() {return $('#'+options.cityKey).val();}
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	}).result(function(event, data, formatted){
		if(options.selectedId ? true : false) {
			$selectedCode = $('#'+options.selectedCode);
		}
		$selectedCode.val(data[2]);
		$('#'+options.elementId).data("ResellerName",$selectedCode.val());
		$('#'+options.elementId).data("change",true);
		$('#'+options.elementId).parent().removeClass('error');
		$('#'+options.elementId).parent().find('#errorText').remove();
		$selectedCode.parent().removeClass('error');
		$selectedCode.parent().find('#errorText').remove();
	}).bind("keydown",function(event){
		var flag = false;
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("ResellerName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("ResellerName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("ResellerName"))){
				$selectedCode.val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("BaInfoName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("BaInfoName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$selectedCode.val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}


/**
 * 在销售人员的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中销售人员的员工名称；"code":表示要选中的是销售人员的员工CODE，默认是"name"
 * 				String:options.categoryCode 参数可选，岗位类别，默认是BA，"ALL"表示全部
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 * 
 * */
function salesStaffBinding(options){
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath + "/common/BINOLCM21_getSalesStaffInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			employeeInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中BA名称
			selected:options.selected ? options.selected : "name",
			//默认为BA
			categoryCode:options.categoryCode ? options.categoryCode : "01",
			// 是否带权限查询
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0	
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	}).result(function(event, data, formatted){
		$("#salesStaffId").val(data[2]);
		$('#'+options.elementId).data("EmployeeName",$("#salesStaffId").val());
		$('#'+options.elementId).data("change",true);
		$('#'+options.elementId).parent().removeClass('error');
		$('#'+options.elementId).parent().find('#errorText').remove();
		$('#salesStaffId').parent().removeClass('error');
		$('#salesStaffId').parent().find('#errorText').remove();
	}).bind("keydown",function(event){
		var flag = false;
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("EmployeeName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("EmployeeName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("EmployeeName"))){
				$("#salesStaffId").val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("EmployeeName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("EmployeeName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#salesStaffId").val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}

/**
 * 在往来单位(合作伙伴)的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中往来单位名称；"code":表示要选中的是往来单位CODE，默认是"name"
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为bussinessPartnerId
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到单据明细，用于发货添加新行等，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择后需要执行的方法，targetDetail=true时调用。
 * 
 * 
 * */
function bussinessPartnerBinding(options){
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getBussinessPartnerInfo.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "bussinessPartnerId";
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			partnerInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//设定被选中的是名称还是code,默认是名称
			selected:options.selected ? options.selected : "name"
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[1])+" "+"["+escapeHTMLHandle(row[0])+"]";
			}else{
				return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
			}
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			$("#"+targetId).val(data[2]);
			$('#'+options.elementId).data("PartnerName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.partnerId = data[2];
			//选择后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		var flag = false;
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("PartnerName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("PartnerName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("PartnerName"))){
				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("PartnerName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("PartnerName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}

/**
 * 在区域的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中员工名称；"code":表示要选中的是员工CODE，默认是"name"
 * 
 * 
 * */
function regionBinding(options){
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
	var url = postPath + "/common/BINOLCM21_getRegionInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			regionInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name"
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[0])+" "+ (row[1] ? "["+escapeHTMLHandle(row[1])+"]" : "");
			}else{
				return escapeHTMLHandle(row[1])+" "+ (row[0] ? "["+escapeHTMLHandle(row[0])+"]" : "");
			}
		}
	});
}

/**
 * 按渠道的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中渠道名称，默认是"name"
 * 
 * 
 * */
function channelBinding(options){
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
	var url = postPath + "/common/BINOLCM21_getChannelInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			channelInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中渠道名称
			selected:options.selected ? options.selected : "name"
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[0]);
			}else{
				return escapeHTMLHandle(row[1]);
			}
		}
	});
}

/**
 * 在逻辑仓库的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中员工名称；"code":表示要选中的是员工CODE，默认是"name"
 * 
 * 
 * */
function logicInventoryBinding(options){
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
	var url = postPath + "/common/BINOLCM21_getLogicInventoryInfo.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			logicInventoryInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name"
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[0])+" "+ (row[1] ? "["+escapeHTMLHandle(row[1])+"]" : "");
			}else{
				return escapeHTMLHandle(row[1])+" "+ (row[0] ? "["+escapeHTMLHandle(row[0])+"]" : "");
			}
		}
	});
}

/**
 * 在会员问题的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中员工名称；"code":表示要选中的是员工CODE，默认是"name"
 * 
 * 
 * */
function issueBinding(options){
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
	var url = postPath + "/common/BINOLCM21_getIssueInfo.action"+"?"+csrftoken;
	var param = options.param;
	if(param) {
		url = url + "&" + param;
	}
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			issueNoKw: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台名称
			selected:options.selected ? options.selected : "name"
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
			if(typeof options.selected == "undefined" || options.selected=="name"){
				return escapeHTMLHandle(row[0])+" "+ (row[1] ? "["+escapeHTMLHandle(row[1])+"]" : "");
			}else{
				return escapeHTMLHandle(row[1])+" "+ (row[0] ? "["+escapeHTMLHandle(row[0])+"]" : "");
			}
		}
	});
}



/**
 * 内部销售客户绑定
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为organizationId
 *              String:options.includeCounter 参数可选，查询结果中是否包含柜台，只要提供该参数就表示包含柜台信息，默认是不包含
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到单据明细，用于发货添加新行等，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择后需要执行的方法，targetDetail=true时调用。
 * 
 * */
function organizationBinding(options){
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getOrganizationDetail.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "organizationId";
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			departInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//是否包含柜台，默认是不包含
			flag:function(){
					if(options.includeCounter){
						return options.includeCounter;
					}else{
						return "";
					}
				},
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			$("#"+targetId).val(data[2]);
			$('#'+options.elementId).val(data[1]);
			$('#'+options.elementId).data("orgName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.orgId = data[2];
			obj.departCode = data[0];
			obj.name = data[1];
			obj.contactPerson = data[3];
			obj.address = data[4];
			//选择后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("orgName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("orgName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("orgName"))){
				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("orgName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("orgName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}


/**
 * 柜台绑定
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为counterCode
 *              String:options.paramType 参数可选，柜台查询参数类型（0.不带参数，1.渠道ID，2.区域ID，默认为不带参数）
 * 				String:options.paramValue 参数可选，柜台查询参数值，与参数类型配合使用
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到明细，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择后需要执行的方法，targetDetail=true时调用。
 * 
 * */
function counterSelectBinding(options){
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getCounterDetail.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "counterCode";
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			counterInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//查询条件类型
			conditionType:options.paramType ? options.paramType : 0,
			//查询条件值
			conditionValue:options.paramValue ? options.paramValue : '',
			//是否带权限查询
			privilegeFlag:options.privilegeFlag ? options.privilegeFlag : 0
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			$("#"+targetId).val(data[2]);
			$('#'+options.elementId).val(data[1]);
			$('#'+options.elementId).data("counterName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.counterId = data[2];
			obj.counterCode = data[0];
			obj.counterName = data[1];
			//选择后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("counterName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("counterName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("counterName"))){
				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("counterName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("counterName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}


/**
 * 在销售人员的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为baCode
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中员工名称；"code":表示要选中的是员工CODE，默认是"name"
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到明细，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择后需要执行的方法，targetDetail=true时调用。
 * */
function baBinding(options){
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getBaByCounter.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "baCode";
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			baInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[0])+" "+"["+escapeHTMLHandle(row[1])+"]";
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			$("#"+targetId).val(data[1]);
			$('#'+options.elementId).val(data[0]);
			$('#'+options.elementId).data("baName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.baCode = data[0];
			obj.baName = data[1];
			//选择后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("baName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("baName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("baName"))){
				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("baName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("baName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}


function clickTab_2WorkFlow(){
		var entryID = $("#entryID").val();
		var params= "WorkFlowID="+entryID;
		var url = "/Cherry/common/BINOLCM25_detailWork";
		
		var callback = function(msg){
			 $("#tabs-2").html(msg);
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
}

/**
 * js进行中英文字符长度控制,如果超出长度就将后面的截断。
 * @param str
 * @param maxLength
 * 
 * */
function subString(str, maxLength){
	var strLength = str.length;
	var count = 0;
	for(var i = 0 ; i < strLength ; i++){
		if(str.substr(i,1).charCodeAt(0)>255){
			count += 2;
		}else{
			count ++;
		}
		if(count > maxLength){
			str = str.substring(0,i);
			break;
		}
	}
	return str;
}
/**
 * 空验证
 * @param str
 * @return
 */
function isEmpty(obj){
	if(obj == undefined || obj == null){
		return true;
	}else if(typeof(obj) == "string"){
		if($.trim(obj) == ""){
			return true;
		}
	}
	return false;
}

/**
 * AJAX产品分类弹出框
 * 
 * @param option
 * @return
 */
function popAjaxPrtCateDialog (option){
	var $dialog = $('#prtCategoryDialog');
//	if(isEmpty(option.propId)){
//		console.error("propId is missing!");
//		return ;
//	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrtCateDialog";
		url += "?" + getSerializeToken();
		if(!isEmpty(option.isBind)){
			url += "&param=" + option.isBind;
		}
		if(!isEmpty(option.propId)){
			url += "&param1=" + option.propId;
		}
		if(!isEmpty(option.cateInfo)){
			url += "&param2=" + option.cateInfo;
		}
		if(!isEmpty(option.checkType)){
			url += "&checkType=" + option.checkType;
		}
		if(!isEmpty(option.teminalFlag)){
			url += "&teminalFlag=" + option.teminalFlag;
		}
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popPrtCateDialog (option);
			}			
		});
	}else{
		popPrtCateDialog (option);
	}
}
/**
 * AJAX促销品分类弹出框
 * 
 * @param option
 * @return
 */
function popAjaxPrmCateDialog (option){
	var $dialog = $('#prmCategoryDialog');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrmCateDialog";
		url += "?" + getSerializeToken();
		url += "&brandInfoId=" + option.brandInfoId;
		url += "&checkType=" + option.checkType;
		if(!isEmpty(option.param)){
			url += "&param=" + option.param;
		}
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popPrmCateDialog (option);
			}			
		});
	}else{
		popPrmCateDialog (option);
	}
}
/**
 * AJAX产品弹出框
 * 
 * @param option
 * 				String:options.optionalValidFlag  自选有效区分,参数可选(1:弹出框显示自选有效区分，无值：不显示自选有效区分)
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1。初始化弹窗有效区分，没有optionalValidFlag时，以该值为准
 * 				Array:options.ignorePrtPrmVendorID 剔除产品，参数可选，默认为空
 * 				String options.ignoreSoluId 产品方案ID，剔除产品方案中的产品，参数可选，默认为空
 * 				String options.ignorePrtFunId 产品功能开启时间主表ID，剔除产品功能开启时间明细表中的产品，参数可选，默认为空。
 * 				String options.isPosCloud 是否小店云模式（小店云显示barcode），参数可选(0[否]、1[是])，默认为否
 * 				String options.showOrderPrice 是否显示采购价（目前思乐得的大仓入库有用），参数可选（0：否；1：是），默认为否
* 注意：ignoreSoluId与ignorePrtFunId只能二选一，不能同时使用
 * @return
 */
function popAjaxPrtDialog (option){
	var $dialog = $('#productDialog');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrtDialog";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		if(option.originalBrand!=null && option.originalBrand!=undefined && option.originalBrand!=""){
			// 产品品牌ID
			url += "&param=" + option.originalBrand;
		}
		
		if(!isEmpty(option.optionalValidFlag)){
			// 自选有效区分
			url += "&param2=" + option.optionalValidFlag;
		}
		//增加自由盘点的标志
		if(!isEmpty(option.freeCount)){
			// 自选有效区分
			url += "&freeCount=" + option.freeCount;
		}
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				// 自选有效区分
				if(!isEmpty(option.optionalValidFlag)) {
					$("input[name='radio22'][value="+option.popValidFlag+"]").attr("checked",true); 
				}
				popProductDialog(option);
			}			
		});
	}else{
		popProductDialog(option);
	}
}
/**
 * AJAX产品弹出框(薇诺娜，无厂商编码，同一产品条码以最新的记录为准)
 * 
 * @param option
 * 				String:options.optionalValidFlag  自选有效区分,参数可选(1:弹出框显示自选有效区分，无值：不显示自选有效区分)
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1。初始化弹窗有效区分，没有optionalValidFlag时，以该值为准
 * 				Array:options.ignorePrtPrmVendorID 剔除产品，参数可选，默认为空
 * 				String options.ignoreSoluId 产品方案ID，剔除产品方案中的产品，参数可选，默认为空
 * 				String options.ignorePrtFunId 产品功能开启时间主表ID，剔除产品功能开启时间明细表中的产品，参数可选，默认为空。
 * 				String options.isPosCloud 是否小店云模式（小店云显示barcode），参数可选(0[否]、1[是])，默认为否
 * 				String options.showOrderPrice 是否显示采购价（目前思乐得的大仓入库有用），参数可选（0：否；1：是），默认为否
 * 注意：ignoreSoluId与ignorePrtFunId只能二选一，不能同时使用
 * @return
 */
function popAjaxPrtDialogOne (option){
	var $dialog = $('#productDialogOne');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrtDialogOne";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		if(option.originalBrand!=null && option.originalBrand!=undefined && option.originalBrand!=""){
			// 产品品牌ID
			url += "&param=" + option.originalBrand;
		}
		
		/*if(!isEmpty(option.optionalValidFlag)){
			// 自选有效区分
			url += "&param2=" + option.optionalValidFlag;
		}*/
		$.ajax({
			url: url, 
			type: 'post',
			success: function(msg){
				$("body").append(msg);
				// 自选有效区分
				/*if(!isEmpty(option.optionalValidFlag)) {
					$("input[name='radio22'][value="+option.popValidFlag+"]").attr("checked",true); 
				}*/
				popProductDialogOne(option);
			}			
		});
	}else{
		popProductDialogOne(option);
	}
}
/**
 * AJAX产品弹出框(浓妆淡抹商城产品添加)
 * 
 * @param option
 * 				String:options.optionalValidFlag  自选有效区分,参数可选(1:弹出框显示自选有效区分，无值：不显示自选有效区分)
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1。初始化弹窗有效区分，没有optionalValidFlag时，以该值为准
 * 				Array:options.ignorePrtPrmVendorID 剔除产品，参数可选，默认为空
 * 				String options.ignoreSoluId 产品方案ID，剔除产品方案中的产品，参数可选，默认为空
 * 				String options.ignorePrtFunId 产品功能开启时间主表ID，剔除产品功能开启时间明细表中的产品，参数可选，默认为空。
 * 				String options.isPosCloud 是否小店云模式（小店云显示barcode），参数可选(0[否]、1[是])，默认为否
 * 				String options.showOrderPrice 是否显示采购价（目前思乐得的大仓入库有用），参数可选（0：否；1：是），默认为否
* 注意：ignoreSoluId与ignorePrtFunId只能二选一，不能同时使用
 * @return
 */
function popAjaxPrtDialogTwo (option){
	var $dialog = $('#productDialog');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrtDialogTwo";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		if(option.originalBrand!=null && option.originalBrand!=undefined && option.originalBrand!=""){
			// 产品品牌ID
			url += "&param=" + option.originalBrand;
		}
		
		if(!isEmpty(option.optionalValidFlag)){
			// 自选有效区分
			url += "&param2=" + option.optionalValidFlag;
		}
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				// 自选有效区分
				if(!isEmpty(option.optionalValidFlag)) {
					$("input[name='radio22'][value="+option.popValidFlag+"]").attr("checked",true); 
				}
				popProductDialogTwo(option);
			}			
		});
	}else{
		popProductDialogTwo(option);
	}
}
/**
 * AJAX促销品弹出框
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除促销品，参数可选，默认为空
 * @return
 */
function popAjaxPrmDialog (option){
	var $dialog = $('#promotionDialog');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrmDialog";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		url += "&brandInfoId=" + option.brandInfoId;
		if(!isEmpty(option.isStock)){
			url += "&isStock=" + option.isStock;
		}
		if(!isEmpty(option.prmCate)){
			url += "&param=" + option.prmCate;
		}
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popPromotionDialog(option);
			}			
		});
	}else{
		popPromotionDialog(option);
	}
}
/**
 * AJAX促销品弹出框(薇诺娜，合并)
 * 
 * @param option
 * 				String:options.popValidFlag 有效区分,参数可选(0[无效]、1[有效]、2[全部])，默认为1
 * 				Array:options.ignorePrtPrmVendorID 剔除促销品，参数可选，默认为空
 * @return
 */
function popAjaxPrmDialogOne (option){
	var $dialog = $('#promotionDialogOne');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initPrmDialogOne";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		url += "&brandInfoId=" + option.brandInfoId;
		if(!isEmpty(option.isStock)){
			url += "&isStock=" + option.isStock;
		}
		if(!isEmpty(option.prmCate)){
			url += "&param=" + option.prmCate;
		}
		$.ajax({
			url: url, 
			type: 'post',
			success: function(msg){
				$("body").append(msg);
				popPromotionDialogOne(option);
			}			
		});
	}else{
		popPromotionDialogOne(option);
	}
}
/**
 * AJAX会员弹出框
 * 
 * @param option
 * @return
 */
function popAjaxMemDialog (option){
	var $dialog = $('#memberDialog');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initMemberDialog";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		url += "&brandInfoId=" + option.brandInfoId;
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popMemberDialog(option);
			}			
		});
	}else{
		popMemberDialog(option);
	}
}
/**
* AJAX对象批次弹出框
* 
* @param option
* @return
*/
function popAjaxObjBatchDialog (option){
	var $dialog = $('#objBatchDialog');
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM02_initObjBatchDialog";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		if(!isEmpty(option.brandInfoId)){
			url += "&brandInfoId=" + option.brandInfoId;
		}
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popObjBatchDialog(option);
			}			
		});
	}else{
		popObjBatchDialog(option);
	}
}
/**
 * AJAX部门弹出框
 * 
 * @param option
 * 			brandInfoId 必填（String/Number）	  品牌ID
 * 			checkType   必填	（String）  			  两种值："checkbox"或者"radio"
 * 			param		可选（String）	          部门查询的一些额外的参数，比如权限类型、部门类型等等
 * 			click		可选（Function）           点击确定或者关闭按钮后回调函数
 * 			modal       可选（Boolean）			  弹出部门后是否锁定父页面，默认为锁定
 * 			resizable	可选（Boolean）			  弹出框大小是否可变，默认为不可变
 * 			
 * 			
 * @return
 */
function popAjaxDepDialog (option){
	var $dialog = $('#departDialogInit');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.checkType)){
		console.error("checkType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var strPath = window.document.location.pathname;
		var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
		var url = postPath + "/common/BINOLCM02_initDepDialog";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		url += "&brandInfoId=" + option.brandInfoId;
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popDataTableOfDepartInfo(option);
			}			
		});
	}else{
		popDataTableOfDepartInfo(option);
	}
}
/*
 * 对象JSON化
 * obj：目标对象
 * encodeFlag：是否执行URL转码
 */
function Obj2JSON(obj,encodeFlag,submitBlank) {
	if(isEmpty(encodeFlag)){
		encodeFlag = false;
	}
	if(isEmpty(submitBlank)){
		submitBlank = false;
	}
	var JSON = [];
	$(obj).find(':input:not(:disabled)').each(
	function() {
		$this = $(this);
		var flag = true;
		// 过滤未选中的radio，checkbox对象
		if($this.is(":radio") || $this.is(":checkbox")){
			if(!$this.is(":checked")){
				flag = false;
			}
		}
		if(flag){
			var name = $this.attr("name");
			var value = $this.val();
			if(!isEmpty(name)){
				if(!isEmpty(value) || submitBlank){
					value = $.trim(value);
					if(encodeFlag){
						name = encodeURIComponent(name);
						value = encodeURIComponent(value);
					}
					value = value.replace(/\\/g, '\\\\').replace(/"/g, '\\"');
					JSON.push('"'+ name+ '":"'+ value + '"');
				}
			}
		}
	});
	return JSON;
}
/*
 * 对象JSON数组化对象JSON数组化
 * obj：目标对象
 * encodeFlag：是否执行URL转码
 */
function Obj2JSONArr(obj,encodeFlag,submitBlank) {
	if(isEmpty(encodeFlag)){
		encodeFlag = false;
	}
	if(isEmpty(submitBlank)){
		submitBlank = false;
	}
	var JSONArr = [];
	$(obj).each(function() {
		JSONArr.push("{"+Obj2JSON(this,encodeFlag,submitBlank).toString() + "}");
	});
	return "[" + JSONArr.toString() + "]";
}
function Obj2JSON2(obj) {
	var JSON = [];
	$(obj).find(':input').each(function() {
		$this = $(this);
		var name = $this.attr("name");
		if(!isEmpty(name)){
			var value = $this.val();
			JSON.push('"'+ name+ '":"'+ value + '"');
		}
	});
	return JSON;
}
function Obj2JSONArr2(obj) {
	var JSONArr = [];
	$(obj).each(function() {
		JSONArr.push("{"+Obj2JSON2(this).toString() + "}");
	});
	return "[" + JSONArr.toString() + "]";
}
/*
 * 使用sjcl加密
 * password：密码
 * plaintext：加密内容
 */
function sjclEncrypt(password, plaintext){
	var p = {adata:'CHERRYAUTH',
	        iter:1000,
	        mode:'ccm',
	        ts:64,
	        ks:128};
    var rp = {};
	var ct = sjcl.encrypt(password, plaintext, p, rp).replace(/,/g,",\n");
	return ct;
}
/**
 * 字符串转数字
 * @param obj
 */
function getNumber(obj){
	var num = 0;
	if(!isEmpty(obj)){
		if(typeof(obj) == 'number'){
			num = obj;
		}else{
			num = Number(obj);
			if(isNaN(num)){
				num = 0;
			}
		}
	}
	return num;
}

/**
 * 字符串转价格
 * @param obj
 */
function getPrice(obj){
	var num = getNumber(obj);
	return num.toFixed(2);
}

/**
 * AJAX主活动弹出框
 * 
 * @param option
 * @return
 */
function actGrpAjaxPrtDialog(option){
		var $dialog = $('#actGrpDialog');
		var url = "/Cherry/ss/BINOLSSPRM13_initactGrpDialog";
		url += "?" + getSerializeToken();
		url += "&checkType=" + option.checkType;
		url += "&brandInfoId=" + option.brandInfoId;
		if($dialog.length == 0){
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				actGrpDialog(option);
			}			
		});
	  }else{
			actGrpDialog(option);
		}
	
}

/**
 * AJAX沟通模板弹出框
 * 
 * @param option
 * @return
 */
function popAjaxTemplateDialog (option){
	var $dialog = $('#msgTemplateDialog');
	if(isEmpty(option.brandInfoId)){
		console.error("brandInfoId is missing!");
		return ;
	}
	if(isEmpty(option.messageType)){
		console.error("MessageType is missing!");
		return ;
	}
	if($dialog.length == 0){
		var url = "/Cherry/common/BINOLCM32_initMsgTemplateDialog";
		url += "?" + getSerializeToken();
		url += "&messageType=" + option.messageType;
		$.ajax({
	        url: url, 
	        type: 'post',
	        success: function(msg){
				$("body").append(msg);
				popMsgTemplateDialog(option);
			}
		});
	}else{
		popMsgTemplateDialog(option);
	}
}

/**
 * 刷新首页任务栏
 */
function refreshGadgetTask(){
    //刷新首页工作流任务
    var gadgetInfoId = $('#gadgetInfoId', window.opener.document).val();
    if(gadgetInfoId != null){
        var json = eval('('+gadgetInfoId+')');
        var gadgetIndex = 0;
        for(var i in json.gadgetInfoList) {
            if(json.gadgetInfoList[i].gadgetCode == "BINOLLGTOP03"){
                gadgetIndex = i;
                break;
            }
        }
        window.opener.shindig.container.getGadget(gadgetIndex).refresh();
    }
}

/**
 * 执行工作流action
 * @param entryid
 * @param actionid
 * @param fun
 * @param action
 */
function doaction(entryid,actionid,fun,action){
	//没有配置在工作流里需要在执行doacion前执行的方法
	if($("#beforeDoActionFun_"+actionid).length>0){
		var backValue = eval($("#beforeDoActionFun_"+actionid).val());
        if (backValue=="doaction"){

        }else{
            return;
        }
	}
    if(fun != ""){
        var backValue= eval(fun);
        if (backValue=="doaction"){

        }else{
            return;
        }
    }
    
    var curl = $("#osdoactionUrl").attr("href");
    curl=curl +"?entryid="+entryid+"&actionid="+actionid;
    var params=$('#mainForm').formSerialize();

    //只有配置了OS_NextStrutsAction才有action的name
    if(action!=""){
    	OS_BILL_Jump_needUnlock = false;
        $("#entryid").val(entryid);
        $("#actionid").val(actionid);
        var tokenVal = $('#csrftoken',window.opener.document).val();
        //在submit时把csrftokenForm改成csrftoken，以防ajax请求时csrftoken报错。
        $("#csrftokenForm").attr("name","csrftoken");
        $('#mainForm').find("input[name='csrftoken']").val(tokenVal);
        $('#mainForm').submit();
    }else{
        cherryAjaxRequest({
            url:curl,
            param:params,
            coverId:"body",
            callback:function(msg){     
            	if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
            	if($(window.opener.document).find("#msgListDialog").is(":visible")){
            		if(window.opener.oTableArr[229] != null)window.opener.oTableArr[229].fnDraw();
            	}
            	if($(window.opener.document).find("#deliverDialog").is(":visible") || $(window.opener.document).find("#allocationDialog").is(":visible")){
            		if(window.opener.oTableArr[31] != null)window.opener.oTableArr[31].fnDraw();
            	}
                if($("#errorDiv2")) $("#errorDiv2").hide();
                refreshGadgetTask();
            }
        });
    }
}

/**
 * 指定时间（时分秒处理）
 * 
 * @param option
 * @return
 */
function timeHHSSMM(timeTemplate){
	//指定时间（时分秒处理）
	var $that = timeTemplate;
	$that.bind({
	"focus" : function(){ 
		var name = $(this).prop("name");
		var val = $(this).val();
		$(this).val("");
		if (val != "") {
			timeTempVal[name] = val;
		}
	},
	"focusout" : function(){ 
		var name = $(this).prop("name");
		var val = $(this).val();
		if (val == "") {
			$(this).val(timeTempVal[name]);
		} else {
			val = $.trim(val);
			if (val.length == 1) {
				var re = /^[0-9]$/; 
				if (re.test(val)) {
					val = "0" + val;
					timeTempVal[name] = val;
					$(this).val(val);
				}
			}
		}
	}
});
}

/* 
 * 行单元格合并处理
 * 
 * $table:需要处理的table的jquery对象
 * cols:指定哪几列需要行合并处理，是一个数组，存放具体的列号
 * 
 */
function tableRowspanRef($table,cols, ref) {
	
	// 保存需要合并的行数
    var table_SpanNum = 0;
	// 保存需要合并的第一个td对象
    var table_firsttd = "";
	// 保存当前的td对象
    var table_currenttd = "";
    // 保存需要合并的第一个td的索引
    var firstIndex = 0;
    // 保存合并的行数(下标为需要合并的第一个td的索引)
    var rows = new Array();
    $table.find('tr td:nth-child('+ref+')').each(function(i) {
		table_currenttd = $(this);
		if(table_SpanNum == 0) {
			table_firsttd = $(this);
			table_SpanNum = 1;
			firstIndex = i;
		} else {
			if(table_currenttd.text() == table_firsttd.text()) {
				table_SpanNum++;  
			} else {
				table_firsttd = $(this);  
				table_SpanNum = 1;
				firstIndex = i;
			}
		}
		rows[firstIndex] = table_SpanNum;
	});
    
    for(var j=0; j<cols.length; j++) {
		$table.find('tr td:nth-child(' + cols[j] + ')').each(function(i) {
			if(rows[i]) {
				if(rows[i] > 1) {
					$(this).attr("rowSpan",rows[i]);
				}
			} else {
				$(this).hide();
			}		 
		});
	}
}

function tableRowspan($table, cols) {
	
	// 保存需要合并的行数
    var table_SpanNum = 0;
	// 保存需要合并的第一个td对象
    var table_firsttd = "";
	// 保存当前的td对象
    var table_currenttd = "";
    for(var j=0; j<cols.length; j++) {
		$table.find('tr td:nth-child(' + cols[j] + ')').each(function(i) {
			table_currenttd = $(this);
			if(table_SpanNum == 0) {
				table_firsttd = $(this);
				table_SpanNum = 1;
			} else {
				if(table_currenttd.text() == table_firsttd.text()) {
					table_SpanNum++;
					table_currenttd.hide();
				} else {
					if(table_SpanNum > 1) {
						table_firsttd.attr("rowSpan",table_SpanNum);
					}
					table_firsttd = $(this);  
					table_SpanNum = 1;
				}
			}
		});
		if(table_SpanNum > 1) {
			table_firsttd.attr("rowSpan",table_SpanNum);
		}
	}
}

/**
 * 取得虚拟促销品条码
 * @return
 */
function getBarCode(type,index){
	var targetId = "#barCode_" + type;
	if(!isEmpty(index)){
		targetId += "_" + index;
	}
	var $target = $(targetId);
	if($target.length > 0){
		var $barCode = $target.find(":input[name='barCode']");
		var $unitCode = $target.find(":input[name='unitCode']");
		if(isEmpty($barCode.val())){
			if(type == 'TZZK'){
				type = '4';
			}else if(type == 'DHCP'){
				type = '5';
			}else if(type == 'DHMY'){
				type = 'A';
			}
			var url = "/Cherry/common/BINOLCM15_getSeqCode";
			var params = $("#brandInfoId").serialize();
			params += "&" + getSerializeToken();
			params += "&type=" + type;
			ajaxRequest(url,params,function(code){
				$barCode.val(code);
				if($unitCode.length > 0){
					$unitCode.val(code);
				}
			});
		}
	}
}

/**
 * show action msg/error
 * @param msg
 * @param level
 * @returns {String}
 */
function showMsg(msg,level){
	var msgClass = "actionSuccess";
	if(level > 0){
		msgClass = "actionError";
	}
	var html = '<div class="'+msgClass+'"><ul>';
	html += '<li><span>'+ msg+'</span></li>';
	html += '</ul></div>';
	return html;
}

/**
 * show action msg/error
 * @param msg
 * @param level
 * @returns {String}
 */
function showMsgList(target,msgInfo){
	var $target = $(target);
	var msgClass = "actionSuccess";
	if(msgInfo.level > 0){
		msgClass = "actionError";
	}
	var html = '<div class="'+msgClass+'"><ul>';
	for(var i=0; i< msgInfo.msgList.length; i++){
		html += '<li><span>' + msgInfo.msgList[i] + '</span></li>';
	}
	html += '</ul></div>';
	$target.html(html);
}

// 在领用text框上绑定下拉框选项
function counterCodeBinding(options){
	var csrftoken = getSerializeToken();
	if(!csrftoken) {
		csrftoken = $('#csrftoken',window.opener.document).serialize();
	}
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath + "/common/BINOLCM02_getCounterCode.action"+"?"+csrftoken;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			counterStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			//默认是选中柜台Code
			selected:options.selected ? options.selected : "code",
			brandInfoId:function() { return $('#brandInfoId').val();}
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			if(typeof options.selected == "undefined" || options.selected=="code"){
				return escapeHTMLHandle(row[0])+" "+ (row[1] ? "【"+escapeHTMLHandle(row[1])+"】" : "");
			}else{
				return escapeHTMLHandle(row[0])+" "+ (row[1] ? "【"+escapeHTMLHandle(row[1])+"】" : "");
			}
		}
	});
}

function exportExcelReport(setting) {
	var callback = function(msg) {
		var json = eval('(' + msg + ')'); 
    	if(json.exportStatus == "1") {
    		var _callback = setting.callback;
    		if(typeof(_callback) == "function") {
    			_callback(msg);
    		}
    	} else {
    		if($("#exportDialog").length == 0) {
    			$("body").append('<div style="display:none" id="exportDialog"></div>');
    		} else {
    			$("#exportDialog").empty();
    		}
    	    var dialogSetting = {
    				dialogInit: "#exportDialog",
    				text: '<p class="message" style="margin: 10px 0;text-align: left;">'+json.message+'</p>',
    				width: 	400,
					height: 200,
    				title: 	$("#pushTitleText").val(),
    				cancel: $("#pushCloseButton").val(),
					cancelEvent: function(){removeDialog("#exportDialog");}
    		};
    	    openDialog(dialogSetting);
    	}
	};
	cherryAjaxRequest({
		url: setting.url,
		param: setting.param,
		callback: callback
	});
}

function exportReport(setting) {
	if($("#exportDialog").length == 0) {
		$("body").append('<div style="display:none" id="exportDialog"></div>');
	} else {
		$("#exportDialog").empty();
	}
	var options = {
			exportUrl: null,
			exportParam: null,
			title: $("#pushTitleText").val(),
			text: $("#exportWarnText").html(),
			confirm: $("#confirmText").val(),
			close: $("#pushCancelButton").val()
	}
	$.extend(options, setting || {});
    var dialogSetting = {
			dialogInit: "#exportDialog",
			text: options.text,
			width: 	430,
			height: 280,
			title: 	options.title,
			confirm: options.confirm,
			cancel: options.close,
			confirmEvent: function(){
//				var exportCallback = function(msg) {
//			    	if(msg.indexOf('id="messageDiv"') > -1) {
//			    		$("#exportDialog").html(msg);
//			    		$("#messageDiv").find("span").html($("#messageDiv").next().text());
//			    	} else {
//			    		if($("#exportIframe").length > 0) {
//			    	    	$("#exportIframe").remove();
//			    	    }
//			    	    var iframe = document.createElement("iframe");
//			    	    $(iframe).attr("id","exportIframe");
//			    	    $(iframe).hide();
//			    	    iframe.src =  getBaseUrl()+'/common/BINOLCM37_download' + "?" + getSerializeToken() + "&tempFilePath=" + msg;
//			    	    document.body.appendChild(iframe);
//			    	    removeDialog("#exportDialog");
//			    	}
//			    }
				var param = options.exportParam;
				if(param) {
					param = param + "&" + $("#exportDialog").find("#charset").serialize();
				} else {
					param = $("#exportDialog").find("#charset").serialize();
				}
			    cherryAjaxRequest({
					url: options.exportUrl,
					param: param,
					callback: null,
					isResultHandle: false
				});
				removeDialog("#exportDialog");
			},
			cancelEvent: function(){removeDialog("#exportDialog");}
	};
    openDialog(dialogSetting);
}

/**
 * AJAX活动会员导入共通弹出框
 * 
 * @param option
 * @return
 */
function popAjaxMemImportDialog(option){
	option.dialogId = 'memImportDialog';
	var url = "/Cherry/common/BINOLCM02_initMemImportDialog";
	url += "?" + getSerializeToken();
	if(!isEmpty(option.searchCode)){
		url += "&searchCode=" + option.searchCode;
	}	
	$.ajax({
        url: url, 
        type: 'post',
        success: function(msg){
			$("body").append(msg);
			popMemImportDialog(option);
		}			
	});
}


/**
 * AJAX活动Coupon导入共通弹出框
 * 
 * @param option
 * @return
 */
function popAjaxCouponDialog(option){
	option.dialogId = 'couponDialog';
	var url = "/Cherry/common/BINOLCM02_initCouponDialog";
	url += "?" + getSerializeToken();
	if(!isEmpty(option.batchCode)){
		url += "&batchCode=" + option.batchCode;
	}	
	$.ajax({
        url: url, 
        type: 'post',
        success: function(msg){
			$("body").append(msg);
			popCouponDialog(option);
		}			
	});
}

/**
 * AJAX文件上传-活动对象
 * 
 * @param option
 * @return
 */
function customerUpload(){
	var that = this;
	var $dialog = $('#memImportDialog');
	var $actionMessage = $('#actionMessage_import');
	var $excelUpload = $dialog.find(':input[name="upExcel"]').val();
	var $recordNameVal =  $dialog.find(':input[name="recordName"]').val();
	//隐藏已经导入的会员数量
	$("#showMessageInfo").hide();
	if($recordNameVal==""){
		showMsgList($actionMessage,{level:1,msgList:[$("#nameIsNull").text()]});
    	return false;
	}else if($recordNameVal.length>30){
		showMsgList($actionMessage,{level:1,msgList:[$("#nameLength").text()]});
    	return false;
	}
	if($excelUpload==''){
		$dialog.find(':input[name="pathExcel"]').val("");
		showMsgList($actionMessage,{level:1,msgList:[$("#select_file").text()]});
		return false;
	}
	// 导入柜台URL
	var url = '/Cherry/cp/BINOLCPPOI01_importMember';
	var params = {};
	params.csrftoken = getTokenVal();
	params.recordName = $dialog.find(':input[name="recordName"]').val();
	params.searchCode = $dialog.find(':input[name="searchCode"]').val();
	params.customerType = $dialog.find(':input[name="customerType"]').val();
	params.comments = $dialog.find(':input[name="comments"]').val();
	if(!isEmpty(params.searchCode)){
		params.importType = $dialog.find(':input[name="importType"]').val();
	}
	var $loadingImg = $("#loadingImg");
	$loadingImg.ajaxStart(function(){$(this).show();});
	$loadingImg.ajaxComplete(function(){$(this).hide();});
	// 导入按钮
	var $importBtn = $("#upload");
	// 禁用导入按钮
	$importBtn.prop("disabled",true);
	$importBtn.addClass("ui-state-disabled");
	$.ajaxFileUpload({
        url: url,
        secureuri:false,
        data:params,
        fileElementId:'upExcel_import',
        dataType: 'html',
        success: function (info){
        	//释放导入按钮
        	$importBtn.removeAttr("disabled",false);
        	$importBtn.removeClass("ui-state-disabled");
        	if($("<div>"+info+"<div>").find("#actionResultDiv").length > 0){
        		$actionMessage.html(info);
        	}else if (window.JSON && window.JSON.parse) {
        			var customerCount = "";
					var json = window.JSON.parse(info);
					//父页面searchCode赋值
					$('.SEARCHCODE').filter(':visible').find(':input[name="searchCode"]').val(json['searchCode']);
					if(json['importType']=="1"){
						customerCount = json['totalSize'];
		        	}else{
		        		customerCount = json['newSize'];
		        	}
					//弹出框searchCode赋值
		        	$dialog.find(':input[name="searchCode"]').val(json['searchCode']);
		        	$dialog.find(':input[name="customerType"]').val(json['customerType']);
		        	$dialog.find(':input[name="customerCount"]').val(customerCount);
		        	$dialog.find(':input[id="importType_init"]').hide();
		        	$dialog.find(':input[id="importType_two"]').show();
		        	var msgInfo = json['msgInfo'];
		        	//显示提示信息
		        	showMsgList($actionMessage,msgInfo); 	        	
		        	var $showContext=$('.FORM_CONTEXT').filter(':visible');
		        	var $memCount = $showContext.find("strong.green");
		        	if($memCount.length>0){//仅仅在活动导入时用于显示
		        		$showContext.find(".ShowCount").show();
		        		if(json['importType']=="1"){
			        		$memCount.text(json['totalSize']);
			        	}else{
			        		$memCount.text(json['newSize']);
			        	}			        	
		        		$showContext.find("span.right").find("a.search").click();
		        	}
        	}	        	
        }
    });
}


/**
 * AJAX文件上传-活动Coupon
 * 
 * @param option
 * @return
 */
function couponUpload(){
	var that = this;
	var $dialog = $('#couponDialog');
	var $actionMessage = $('#actionMessage_import');
	var $excelUpload = $dialog.find(':input[name="upExcel"]').val();
	//隐藏已经导入的会员数量
	$("#showMessageInfo").hide();
	if($excelUpload==''){
		$dialog.find(':input[name="pathExcel"]').val("");
		showMsgList($actionMessage,{level:1,msgList:[$("#select_file").text()]});
		return false;
	}
	// 导入Coupon
	var url = '/Cherry/cp/BINOLCPPOI01_importCouponCode';
	var params = {};
	params.csrftoken = parentTokenVal();
	params.batchCode = $dialog.find(':input[name="batchCode"]').val();
	params.campaignCode = $dialog.find(':input[name="campaignCode"]').val();
	params.obtainToDate = $dialog.find(':input[name="obtainToDate"]').val();
	if(!isEmpty(params.batchCode)){
		params.importType = $dialog.find(':input[name="importType"]').val();
	}
	var $loadingImg = $("#loadingImg");
	$loadingImg.ajaxStart(function(){$(this).show();});
	$loadingImg.ajaxComplete(function(){$(this).hide();});
	// 导入按钮
	var $importBtn = $("#upload");
	// 禁用导入按钮
	$importBtn.prop("disabled",true);
	$importBtn.addClass("ui-state-disabled");
	$.ajaxFileUpload({
        url: url,
        secureuri:false,
        data:params,
        fileElementId:'upExcel_import',
        dataType: 'html',
        success: function (info){
        	//释放导入按钮
        	$importBtn.removeAttr("disabled",false);
        	$importBtn.removeClass("ui-state-disabled");
        	if($("<div>"+info+"<div>").find("#actionResultDiv").length > 0){
        		$actionMessage.html(info);
        	}else if (window.JSON && window.JSON.parse) {
					var json = window.JSON.parse(info);
					//父页面searchCode赋值
					$('.BATCHCODE').filter(':visible').find(':input[name="batchCode"]').val(json['batchCode']);
					//弹出框searchCode赋值
		        	$dialog.find(':input[name="batchCode"]').val(json['batchCode']);
		        	$dialog.find(':input[id="importType_init"]').hide();
		        	$dialog.find(':input[id="importType_two"]').show();
		        	var msgInfo = json['msgInfo'];
		        	//显示提示信息
		        	showMsgList($actionMessage,msgInfo); 	
		        	var $showContext=$('.FORM_CONTEXT').filter(':visible');	        	
		        	$showContext.find("span.right").find("a.search").click();
        	}	        	
        }
    });
}

// 区域联动初始化
function regionLinkageInit(regionJson, option) {
	if(!regionJson) {
		return;
	}
	var optionDefault = '<option value="">'+option.optionDefault+'</option>';
	if(option.cityId && $("#"+option.cityId).length > 0) {
		$("#"+option.provinceId).change(function(){
			cityInit();
			countyInit();
		});
	}
	if(option.countyId && $("#"+option.countyId).length > 0) {
		$("#"+option.cityId).change(function(){
			countyInit();
		});
	}
	
	if(option.provinceVal) {
		provinceInit(option.provinceVal);
		if(option.cityVal) {
			cityInit(option.cityVal);
			if(option.countyVal) {
				countyInit(option.countyVal);
			} else {
				countyInit();
			}
		} else {
			cityInit();
			countyInit();
		}
	} else {
		provinceInit();
		cityInit();
		countyInit();
	}
	
	function provinceInit(val) {
		if(!option.provinceId || $("#"+option.provinceId).length == 0) {
			return;
		}
		var optionProvince = optionDefault;
		for(var i = 0; i < regionJson.length; i++) {
			optionProvince += '<option value="'+regionJson[i].id+'">'+regionJson[i].name+'</option>';
		}
		$("#"+option.provinceId).html(optionProvince);
		if(val) {
			$("#"+option.provinceId).val(val);
		}
	}
	
	function cityInit(val) {
		if(!option.cityId || $("#"+option.cityId).length == 0) {
			return;
		}
		var optionCity = optionDefault;
		var provinceVal = $("#"+option.provinceId).val();
		if(provinceVal != "") {
			for(var i = 0; i < regionJson.length; i++) {
				if(regionJson[i].id == provinceVal) {
					var cityJson = regionJson[i].child;
					if(cityJson) {
						for(var j = 0; j < cityJson.length; j++) {
							optionCity += '<option value="'+cityJson[j].id+'">'+cityJson[j].name+'</option>';
						}
					}
					break;
				}
			}
		}
		$("#"+option.cityId).html(optionCity);
		if(val){
			$("#"+option.cityId).val(val);
		}
	}
	
	function countyInit(val) {
		if(!option.countyId || $("#"+option.countyId).length == 0) {
			return;
		}
		var optionCounty = optionDefault;
		var provinceVal = $("#"+option.provinceId).val();
		var cityVal = $("#"+option.cityId).val();
		if(provinceVal != "") {
			for(var i = 0; i < regionJson.length; i++) {
				if(regionJson[i].id == provinceVal) {
					var cityJson = regionJson[i].child;
					if(cityJson) {
						for(var j = 0; j < cityJson.length; j++) {
							if(cityJson[j].id == cityVal) {
								var countyJson = cityJson[j].child;
								if(countyJson) {
									for(var x = 0; x < countyJson.length; x++) {
										optionCounty += '<option value="'+countyJson[x].id+'">'+countyJson[x].name+'</option>';
									}
								}
								break;
							}
						}
					}
					break;
				}
			}
		}
		$("#"+option.countyId).html(optionCounty);
		if(val){
			$("#"+option.countyId).val(val);
		}
	}
}


/**
 * 在产品的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 *              String:options.targetId 参数可选，选中下拉列表中的某个值时，需要保存的目标值ID，默认为prtVendorId
 *              String:options.targetShow 参数可选，选中下拉列表中的某个值时，显示在text框上的值，默认为nameTotal
 *              String:options.targetDetail 参数可选，选中下拉列表中的某个值时，需要显示到单据明细，用于发货添加新行等，仅当值为true时有效。
 *              options.afterSelectFun 参数可选，选择产品后需要执行的方法（如判断重复、查询库存），targetDetail=true时调用。
 *              String:option.proType 参数可选，默认为产品，值为prm时表示促销品
 *
 * */
function productBindingNew(options){
	var recordPrtName = null;
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
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var strPath = window.document.location.pathname;
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	var url = postPath+"/common/BINOLCM21_getProductInfo.action"+"?"+csrftoken;
	var targetId = options.targetId ? options.targetId : "prtVendorId";
	var targetShow = options.targetShow ? options.targetShow : "nameTotal";
	if(options.proType == "prm"){
		url = postPath+"/common/BINOLCM21_getPrmProductInfo.action"+"?"+csrftoken;
	}
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			productInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			validFlag : function() {
				/*if(options.validFlag){
				 var $validFlag = $('input[name='+options.validFlag+']:checked').val();
				 if (!$validFlag){//如果是下拉框，则取下拉框中的值
				 $validFlag=  $('#'+options.validFlag).val();
				 }
				 return $validFlag;
				 } else {
				 return 1;
				 }*/
				return options.validFlag;
			}
		},
		loadingClass: "ac_loading",
		minChars:1,
		matchContains:1,
		matchContains: true,
		scroll: false,
		cacheLength: 0,
		width:300,
		max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[1])+"-"+escapeHTMLHandle(row[2])+" ["+escapeHTMLHandle(row[0])+"]";
		}
	}).result(function(event, data, formatted){
		if(options.targetDetail == null || options.targetDetail == undefined){
			if(targetId == 'prtVendorId'){
				$("#"+targetId).val(data[3]);
			}else if(targetId == 'productId'){
				$("#"+targetId).val(data[4]);
			}else if(targetId == 'unitCode'){
				$("#"+targetId).val(data[1]);
			}else{
				$("#"+targetId).val(data[3]);
			}
			if(targetShow == 'unitCode'){
				$('#'+options.elementId).val(data[1]);
			} else if(targetShow == 'barCode') {
				$('#'+options.elementId).val(data[2]);
			} else {
				$('#'+options.elementId).val(data[0]);
			}
			$('#'+options.elementId).data("prtName",$("#"+targetId).val());
			$('#'+options.elementId).data("change",true);
		}else if(options.targetDetail == true){
			var obj = {};
			obj.elementId = options.elementId;
			obj.productName = data[0];
			obj.unitCode = data[1];
			obj.barCode = data[2];
			obj.prtVendorId = data[3];
			obj.prtId = data[4];
			obj.price = data[5];
			obj.memPrice = data[6];
			obj.standardCost = data[7];
			obj.orderPrice = data[8];
			obj.IsExchanged = data[9];
			//选择产品后执行方法
			if($.isFunction(options.afterSelectFun)){
				options.afterSelectFun.call(this,obj);
			}
		}
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",false);

			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",false);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag"))&&(!$('#'+options.elementId).data("change"))&&($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName"))){
//				alert($('#'+options.elementId).val()+","+$('#'+options.elementId).data("prtName"));
				$("#"+targetId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",true);
				$('#'+options.elementId).data("flag",true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}else{
				$('#'+options.elementId).data("change",false);
				$('#'+options.elementId).data("flag",true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change")&&($('#'+options.elementId).data("flag"))){
			$("#"+targetId).val("");
		}else{
			$('#'+options.elementId).data("change",false);
		}
	}).data("flag",true);
}
