
// 向服务端发送rest请求
function sendRequestToServer(url, method, opt_postParams, opt_callback, opt_excludeSecurityToken) {
    
	opt_postParams = opt_postParams || {};
	
	var makeRequestParams = {
  		'CONTENT_TYPE' : 'JSON',
  		'METHOD' : method,
  		'POST_DATA' : gadgets.json.stringify(opt_postParams)};

	if (!opt_excludeSecurityToken) {
  		url = url + '?st=' + generateSecureToken();
	}

	gadgets.io.makeNonProxiedRequest(url,
  		function(data) {
    		data = data.data;
    		if (opt_callback) {
        		opt_callback(data);
    		}
  		},
  		makeRequestParams,
  		{'Content-Type':'application/javascript'}
	);
}

//生成访问小工具的SecureToken
function generateSecureToken() {
    
    var fields = ['Cherry', 'Cherry', 'Gadget', 'Shindig', 'url', '0', 'default'];
    for (var i = 0; i < fields.length; i++) {
      // escape each field individually, for metachars in URL
      fields[i] = escape(fields[i]);
    }
    return fields.join(':');
}

var callbackAdded = false;
function cherrySubscribeRequest(url, opt_callback) {
    // jquery.atmosphere.response
    function callback(response) {
        // Websocket events.
        //$.atmosphere.log('info', ["response.state: " + response.state]);
        //$.atmosphere.log('info', ["response.transport: " + response.transport]);

        detectedTransport = response.transport;
        if (response.transport != 'polling' && response.state != 'connected' && response.state != 'closed') {
            //$.atmosphere.log('info', ["response.responseBody: " + response.responseBody]);
            if (response.status == 200) {
                var data = response.responseBody;
                if (data.length > 0) {
                	if (opt_callback) {
                		opt_callback(data);
            		}
                }
            }
        }
    }

    
    $.atmosphere.subscribe(url, !callbackAdded ? callback : null,
            $.atmosphere.request = { transport: 'websocket',timeout: '1200000',fallbackTransport: 'long-polling',maxRequest: 10000000});
    callbackAdded = true;
}

function parsingTemplate(temp, params) {
	if(temp) {
		if(params && params.length > 0) {
			for(var i = 0; i < params.length; i++) {
				temp = temp.replace('{'+i+'}', params[i]);
			}
		}
	}
	return temp;
}


function getBaseUrl(prePath) {
	var strFullPath = window.document.location.href;
	var strPath = window.document.location.pathname;
	if(!prePath) {
		prePath = strFullPath.substring(0, strFullPath.indexOf(strPath));
	} else {
		var port = window.document.location.port;
		prePath = prePath + ":" + port;
	}
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	return prePath + postPath;
}

/**
 * 关闭強制退出系统提示框，返回登录画面
 * @param logoutUrl
 */
function closeDivKickUser(logoutUrl){
	if (0 == $("#timeoutform").length && $("#currentTopMenu").val() != "RP") {
		window.close();
		if (window.opener) {
			window.opener.location.href = logoutUrl;
		}
	} else {
		window.location.href = logoutUrl;
	}
}

/**
 * 把收到的推送信息在右下角弹出
 * @param html
 */
function addToTalkBoxMenu(html){
	//右下角提示框
	$("#talkboxmenu_window").stop();
	$("#talkboxmenu_window").show();
//	if($("#talkboxmenu_window:visible").length == 0){
//		$("#talkboxmenu_window").slideDown();
//	}
	
	if($("#openboxlist > ul").length>=5){
		$("#openboxlist > ul:first").remove();
		$("#openboxlist > div.line:first").remove();
	}
	
	var index = 1;
	var ulID = $("#openboxlist ul:last-child").attr("id");
	if(ulID != undefined){
		index = parseInt(ulID.substr(7))+1;
	}
	
	var html = "<ul id='ul_msg_"+index+"'><li>"+html+"</li></ul><div class='line'></div>";
	$("#openboxlist").append(html);
	
	//8秒后自动隐藏
	//$("#talkboxmenu_window").delay(8000).slideUp();
	$("#talkboxmenu_window").delay(8000).hide(0);
}

function toggleNewMsgIcon(unReadCount){
	if(unReadCount>0){
		$("#newMessageIcon").show();
		 //$("#unReadCount").show();
		 //$("#unReadCount").corner("6px");
	}else{
		$("#newMessageIcon").hide();
		//$("#unReadCount").hide();
	}
}

//$(document).ready(function() {
	cherrySubscribeRequest(getBaseUrl() + '/pubsub/pushMsg', function(msg) {
		if (msg.indexOf('<!-- EOD -->') > -1) {
			return false;
		}
		var json = eval('(' + msg + ')');
		if(json.TradeType == "kickUser"){
			//取消订阅消息
			$.atmosphere.unsubscribe();
			var logoutUrl = $("#logoutURL").attr("href");
			if (logoutUrl == undefined) {
				logoutUrl = $('#logoutURL', window.opener.document).attr("href");
			}
			var message = $("#kickUserText").val();
			message = message.replace("{0}",json.UserName).replace("{1}",json.RemoteIP);
			var dialogSetting = {
				dialogInit : "#pushMsgDialog",
				zIndex : 9999,
				text : message,
				width : 350,
				height : 130,
				title : $("#pushTitleText").val(),
				confirm : $("#confirmText").val(),
				confirmEvent : function() {
					closeDivKickUser(logoutUrl);
				},
				closeEvent : function() {
					closeDivKickUser(logoutUrl);
				}
			};
			//当有弹出窗口时，只在弹出窗口弹出，父页面不弹出。
			if(!isChildWinOpen() || $("#currentTopMenu").val() == "RP"){
				openDialog(dialogSetting);
			}
		}else if(json.TradeType == "overdueTactic"){
			if(json.OverdueTactic == "1"){
				//右下角提示框
				var message = $("#"+json.Message).val().replace("{0}",json.ExpireDate);
				var html = "<a href='#' class='left'><span>"+$("#header_systemMsg").val()+"</span>"+message+"</a>";
				addToTalkBoxMenu(html);
			}else if(json.OverdueTactic == "2"){
				var popDivUrl = $("#usernameId").attr("href");
				var param = "&popType=dialog";
				var callback = function(msg) {
					//强制修改密码弹出框
					var dialogSetting = {
							dialogInit : "#dialogUpdatePwdInit",
							text:msg,
							zIndex : 9990,
							width : 500,
							height : 300,
							title : $("#header_updatePW").val(),
							confirm : $("#header_global_page_ok").val(),
							confirmEvent : function() {
								popUpdatePwd_doSave($("#BINOLLGTOP03_updatePwd_url").attr("href"));
							}
					};
					openDialog(dialogSetting);
					//隐藏右上角关闭按钮
					$(".ui-dialog-titlebar-close").hide();
				};
				cherryAjaxRequest({
					url: popDivUrl,
					param: param,
					callback: callback,
					formId: '#updatePwdForm'
				});
			}
		}else if(json.TradeType == "loginInfo"){
			//右下角提示框			
			var html = "<a href='#' class='left'><span>"+$("#header_lastLogin").val()+"</span>"+json.LastLogin+"<span><br/>";
			html += $("#header_lastLoginIP").val()+"</span>"+json.LoginIP+"</a>";
			addToTalkBoxMenu(html);
		}else if(json.TradeType == "exportMsg") {
			if(json.pageName) {
				var pageName = $("#pageName").val();
				if(!pageName || pageName != json.pageName) {
					return;
				}
			}
			if(json.exportStatus == "1") {
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
						confirm: $("#pushDownloadButton").val(),
						cancel: $("#pushCancelButton").val(),
						confirmEvent: function(){
							if(typeof(popMsgList) != "undefined") {
								//点击下载后标记本信息已读。
								popMsgList.setMsgRead(json.MessageID);
							}
							if($("#exportIframe").length > 0) {
				    	    	$("#exportIframe").remove();
				    	    }
				    	    var iframe = document.createElement("iframe");
				    	    $(iframe).attr("id","exportIframe");
				    	    $(iframe).hide();
				    	    iframe.src =  getBaseUrl()+'/common/BINOLCM37_download' + "?" + getSerializeToken() + "&tempFilePath=" + json.tempFilePath;
				    	    document.body.appendChild(iframe);
							removeDialog("#exportDialog");
						},
						cancelEvent: function(){removeDialog("#exportDialog");}
				};
			    openDialog(dialogSetting);
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
		}
		
		if(json.TradeType == "exportMsg" || json.TradeType == "osworkflow" || json.TradeType == "PRT" || json.TradeType == "PRM" || json.TradeType == "DPRT" || json.TradeType == "ACT"){
		    //未读数
		    var unReadCount = 0;
		    if(null != json.UnReadCount){
		    	unReadCount =  parseInt(json.UnReadCount);
		    	$("#unReadCount").html(unReadCount);
		    	toggleNewMsgIcon(unReadCount);
		    }
		}
	});
//});