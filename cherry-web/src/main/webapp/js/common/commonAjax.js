var urlS;
var submitflag = true;
//用于延长盘点session时效
var refreshSessionTimerID = null;

//左菜单点击专用
function ajaxSubmitLeftMenu(argUrl){
	//切换左菜单则停止盘点时用于延长session时效的刷新
	if(refreshSessionTimerID != null){
		window.clearInterval(refreshSessionTimerID);
		refreshSessionTimerID = null;
	}
	//if(submitflag){
		//submitflag=false;
		// 变量初始化
		oTableArr = new Array(null,null);
		fixedColArr = new Array(null,null);
		// 防止ie下使用layout插件后点左边菜单报js错问题
		$(window).unbind('resize');
		$.ajax({
               url :argUrl,  //后台处理程序
               type:'post',    //数据发送方式
               dataType:'html',   //接受数据格式
               data:{'csrftoken':$('#csrftoken').val()}, //要传递的数据,左菜单提交时只需提交验证token
               success:function(temp){
					//submitflag=true;
					//当发生超时错误时，返回的是登录画面，会刷新在<div id="div_main">区域，所以再次提交，整个浏览器跳转到登录画面
					if(temp.indexOf('pageid=login') > -1){
						$("#timeoutform").submit();
					}else{	
						$('#div_main').html(temp);
					} 
               },  
               error:function(){
            	   //submitflag=true;
               }
		 });
	//}
}

/* 
 * 共通AJAX调用函数①
 * 
 * Inputs:  string:url ajax请求url
 * 			string:param ajax请求参数，可以是插叙字符串的形式，也可以是json格式；
 *          function:callback ajax回调函数
 *          string:argDataType 请求返回类型，默认是html；
 * 
 */
function ajaxRequest(url,param,callback,argDataType) {
	$.ajax({
		type: "POST",
		url: url,
		data: param,
		dataType:argDataType?argDataType:'html',
		success: function(msg) {
			if(typeof(callback) == "function") {
				requestEnd();
				if(typeof msg=="string"){
					if(msg.indexOf('pageid=login') > -1){
						if (0 == $("#timeoutform").length) {
							window.close();
							if (window.opener) {
								$("#timeoutform", window.opener.document).submit();
							}
						} else {
							$("#timeoutform").submit();
						}
						
						//if (window.opener) {
						//	windowclose();
						//	$("#timeoutform", window.opener.document).submit();
						//}else{
						//	$("#timeoutform").submit();
						//}
						return;
					}
				}
				callback(msg);
			}
	    },
	    error: function() {
	    	requestEnd();
	    }
	});
}

/* 
 * AJAX调用时防止重复提交
 * 通过在需要防止重复提交的内容块覆盖一块DIV来实现
 * 
 * Inputs:  string:id 需要覆盖的内容块的ID
 * 
 */
function requestStart(id) {
	var $id;
	if(typeof id == 'undefined'){
		$id = $('body');
	} else {
		$id = $(id);
	}
	var html = "<div id=\"COVERDIV_AJAX\" style=\"position:absolute;z-index: 30;opacity: 0;filter: alpha(opacity=0);background-color: #F5F5F5;display: block;\"></div>";
	$id.append(html); 
	var offset = $id.offset();
	$('#COVERDIV_AJAX').css({
		top: offset.top + 'px',
		left: offset.left + 'px',
		width: $id.width(),
		height: $id.height()
	}); 	
}

/* 
 * AJAX调用时防止重复提交,并显示等待画面
 * 通过在需要防止重复提交的内容块覆盖一块DIV来实现
 * 
 * Inputs:  string:id 需要覆盖的内容块的ID
 * 
 */
function loadImage(id) {
	requestStart(id);
	var $id;
	if(typeof id == 'undefined'){
		$id = $('body');
	} else {
		$id = $(id);
	}
	var html = "<div id=\"COVERDIV_LOADAJAX\" style=\"position:absolute;z-index: 31;filter: alpha(opacity=0);background-color: #F5F5F5;display: block;\">处理中...</div>";
	$id.append(html); 
	var offset = $id.offset();
	$('#COVERDIV_LOADAJAX').css({
		top: offset.top+$id.height()/2 + 'px',
		left: offset.left+$id.width()/2 + 'px'
	}); 
}

/* 
 * 清除在AJAX提交时加的DIV块
 * 
 */
function requestEnd() {
	submitflag = true;
	$("#COVERDIV_AJAX").remove(); 
	$("#COVERDIV_LOADAJAX").remove();
}

/* 
 * Action执行后有错误或成功消息要返回的场合，把错误信息打在画面上的共通处理
 * 
 */
function resultHandle(msg, formId) {
	//var divObject = document.createElement("div");
	//$(divObject).html(msg);
	// Action执行后有错误或成功消息要返回的场合
	if(msg.indexOf('id="actionResultDiv"') > -1 || msg.indexOf('id="fieldErrorDiv"') > -1) {
		// 把Action错误或成功信息打在画面上
		$('#actionResultDisplay').html(msg);
		// 清空原有的错误信息
		$(':input').parent().removeClass('error');
		$(':input').parent().find('#errorText').remove();
		// ActionFieldError存在的场合
		if($('#fieldErrorDiv').length > 0) {
			// 取得新的错误信息
			var fieldErroVal = $('#fieldErrorDiv :input').val();
			fieldErroVal = fieldErroVal.replace(new RegExp("({|})","gm"),"");
			var fieldErros = fieldErroVal.split(',');
			// 根据新的错误信息逐个循环，把错误打在画面输入框对应的位置
			for(var i = 0; i < fieldErros.length; i++) {
				var field = fieldErros[i].split('=');
				if(field.length == 2) {
					var fieldKeys = field[0].split('_');
					var fieldVal = field[1].replace(new RegExp("([\[]{1})|(]{1})","gm"),"");
					var m = 0;
					if(fieldKeys.length == 2) {
						m = fieldKeys[1];
					}
					var $parent = $($(':input[name='+fieldKeys[0]+']')[m]).parent();
					if(formId) {
						// 支持formId参数的开头可带可不带字符‘#’
						formId = formId.indexOf('#')==0 ? formId:'#'+formId;
						$parent = $($(formId+' :input[name='+fieldKeys[0]+']')[m]).parent();
					}
					$parent.addClass("error");
					$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
					$parent.find('#errorText').attr("title",'error|'+fieldVal);
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
			}
		}
	} else if(msg.indexOf('id="actionResultBody"') > -1) {
		$('body').html(msg);
	} else if(msg.indexOf('id="AJAXSYSERROR"') > -1) {
		$('body').html(msg);
	}
}

/* 
 * Action执行后有错误或成功消息要返回的场合，把错误信息打在画面上的共通处理
 * 
 */
function resultHandleDialog(msg, resultId, bodyId) {
	//var divObject = document.createElement("div");
	//$(divObject).html(msg);
	// Action执行后有错误或成功消息要返回的场合
	if(msg.indexOf('id="actionResultDiv"') > -1 || msg.indexOf('id="fieldErrorDiv"') > -1) {
		// 把Action错误或成功信息打在画面上
		$(resultId).html(msg);
		// 清空原有的错误信息
		$(':input').parent().removeClass('error');
		$(':input').parent().find('#errorText').remove();
		// ActionFieldError存在的场合
		if($('#fieldErrorDiv').length > 0) {
			// 取得新的错误信息
			var fieldErroVal = $('#fieldErrorDiv :input').val();
			fieldErroVal = fieldErroVal.replace(new RegExp("({|})","gm"),"");
			var fieldErros = fieldErroVal.split(',');
			// 根据新的错误信息逐个循环，把错误打在画面输入框对应的位置
			for(var i = 0; i < fieldErros.length; i++) {
				var field = fieldErros[i].split('=');
				if(field.length == 2) {
					var fieldKeys = field[0].split('_');
					var fieldVal = field[1].replace(new RegExp("([\[]{1})|(]{1})","gm"),"");
					var m = 0;
					if(fieldKeys.length == 2) {
						m = fieldKeys[1];
					}
					var $parent = $($(':input[name='+fieldKeys[0]+']')[m]).parent();
					$parent.addClass("error");
					$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
					$parent.find('#errorText').attr("title",'error|'+fieldVal);
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
			}
		}
	} else if(msg.indexOf('id="actionResultBody"') > -1) {
		$(bodyId).html(msg);
	} else if(msg.indexOf('id="AJAXSYSERROR"') > -1) {
		$(bodyId).html(msg);
	}
}

/* 
 * 共通AJAX调用函数(带csrftoken处理、重复提交处理、load画面处理)
 * 
 * Inputs:  Object:options 					ajax调用参数
 * 				string:options.url     		ajax请求url
 * 				string:options.param 		ajax请求参数,查询字符串格式
 *         	 	function:options.callback 	ajax回调函数
 *          	string:options.formId 		自定义form的ID
 *          	string:options.coverId 		需要覆盖的内容块的ID
 *          	boolean:options.loadFlg 	是否需要load画面
 *          	boolean:options.reloadFlg 	是否允许重复提交
 *            	options.isDialog
 *              options.isResultHandle      是否对返回结果进行错误提示处理
 * 
 */
function cherryAjaxRequest(options) {
	// 参数为空或者ajax请求url为空时不做任何处理
	if(options == null || !options.url) {
		return false;
	}
	// 判断是否允许重复提交
	if(!options.reloadFlg) {
		// 判断是否重复提交
		if(!submitflag) return false;
		submitflag = false;
	}
	
	// csrftoken处理
	var csrftoken = '';
	if($("#csrftokenCode").length > 0) {
		csrftoken = $("#csrftokenCode").serialize();
	} else {
		csrftoken = $('#csrftoken').serialize();
		if(!csrftoken) {
			if (window.opener) {
				csrftoken = $('#csrftoken',window.opener.document).serialize();
			}
		}
	}
	// 带上csrftoken参数的处理
	var param = null;
	if (options.notdeftoken) {
		param = options.param;
	} else {
		// 带上csrftoken参数的处理
		param = options.param ? options.param + (csrftoken ? '&' + csrftoken : '') : (csrftoken ? csrftoken : '');
	}
	// 需要load画面时
	if(options.loadFlg) {
		// 生成load画面
		loadImage(options.coverId);
	} else {
		// 需要防止重复提交时
		if(options.coverId) {
			// 生成覆盖的内容块
			requestStart(options.coverId);
		}
	}
	var _callback = options.callback;
	var callback = function(msg) {
		// 登录画面的场合
		if(msg.indexOf('pageid=login') > -1){
			$("#timeoutform").submit();
			return;
		}
		if(options.isResultHandle == undefined || options.isResultHandle) {
			if (options.isDialog) {
				resultHandleDialog(msg, options.resultId, options.bodyId);
			} else {
				resultHandle(msg, options.formId);
			}
		} else {
			if(msg.indexOf('id="AJAXSYSERROR"') > -1) {
				$(bodyId).html(msg);
			}
		}
		if(typeof(_callback) == "function") {
			_callback(msg);
		}
	};
	// ajax请求处理
	ajaxRequest(options.url, param, callback);
}

/* 
 * Action执行后有Field错误共通处理
 * 
 */
function ajaxFieldHandle(msg,input) {
	// 把Action错误或成功信息打在画面上
	$('#actionResultDisplay').html(msg);
	// 清空原有的错误信息
	$(input).parent().removeClass('error');
	$(input).parent().find('#errorText').remove();
	// ActionFieldError存在的场合
	if($('#fieldErrorDiv').length > 0) {
		// 取得新的错误信息
		var fieldErroVal = $('#fieldErrorDiv :input').val();
		fieldErroVal = fieldErroVal.replace(new RegExp("({|})","gm"),"");
		var fieldErros = fieldErroVal.split(',');
		// 根据新的错误信息逐个循环，把错误打在画面输入框对应的位置
		for(var i = 0; i < fieldErros.length; i++) {
			var field = fieldErros[i].split('=');
			if(field.length == 2) {
				var fieldKeys = field[0].split('_');
				var fieldVal = field[1].replace(new RegExp("([\[]{1})|(]{1})","gm"),"");
				var m = 0;
				if(fieldKeys.length == 2) {
					m = fieldKeys[1];
				}
				var $parent = $($(':input[name='+fieldKeys[0]+']')[m]).parent();
				$parent.addClass("error");
				$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
				$parent.find('#errorText').attr("title",'error|'+fieldVal);
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
		}
	}
}