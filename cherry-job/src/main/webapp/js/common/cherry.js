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
         if(e.which==116 || ((e.ctrlKey)&&(e.which==82))){
            return false;
         }
     }
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
				btnId: ''		 // 下拉按钮ID
			},
			_create: function() {
				var self = this,
					select = self.element.hide(),
					delay = self.options.delay,
					inputId = self.options.inputId,
					btnId = self.options.btnId;
				// 输入框设置
				var input = $(inputId)
					.autocomplete({
						source: function(request, response) {
							// 匹配选项,匹配的字段标记为红色在此段处理
							response(select.children("option").not(':disabled').map(function() {
								var text = $(this).text().replace(/(^\s*)|(\s*$)/g, "");
								// 当输入字符长度大于2开始匹配
								if (!request.term || request.term.length > 2 && text.indexOf(request.term.toUpperCase()) >= 0) {
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
				$(btnId)
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
function doAjax2(url, value, label, obj, params, checkedVal) {	
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
				options.top = (screen.height - options["height"])/2;
				options.left = (screen.width - options["width"])/2;
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
			if (1 != options.childModel || !childWin || childWin.closed) {
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
		title: dialogSetting.title,
		zIndex: 30,  
		modal: true, 
		resizable: false,
		close: function() {removeDialog(dialogSetting.dialogInit);}		
	};
	if(buttons.length > 0) {
		_dialogSetting.buttons = buttons;
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
		$selTableId.find(":checkbox").attr("checked", true);
	} else {
		// 全部不选
		$selTableId.find(":checkbox").attr("checked", false);
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
function privilegeRefreshConfirm(orgPrivilegeUrl, empPrivilegeUrl) {
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
		confirmEvent: function(){privilegeRefreshHandle(orgPrivilegeUrl, empPrivilegeUrl);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

// 数据权限刷新处理
function privilegeRefreshHandle(orgPrivilegeUrl, empPrivilegeUrl) {
	
	if(orgPrivilegeUrl) {
		cherryAjaxRequest({
			url: orgPrivilegeUrl,
			param: null,
			reloadFlg : true,
			callback: function(msg) {
			}
		});
	}
	if(empPrivilegeUrl) {
		cherryAjaxRequest({
			url: empPrivilegeUrl,
			param: null,
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
