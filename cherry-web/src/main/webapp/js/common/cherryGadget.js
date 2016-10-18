
// 用户信息
var gadgetUserInfo;
var gadgetParam;
var gadgetAllRefresh = false;
var gadgetSizeMax = false;
// 小工具初始化
function initGadget(containerId,gadgetInfo) {
	if(containerId == null || containerId == undefined || "" == containerId) {
		return;
	}
	if(gadgetInfo == null || gadgetInfo == undefined || "" == gadgetInfo) {
		return;
	}
	var gadgetObject = eval('('+gadgetInfo+')');
	
	var gadgetWidth = gadgetObject.gadgetWidth;
	var gadgetContext = gadgetObject.gadgetContext;
	gadgetUserInfo = gadgetObject.userInfo;
	gadgetParam = gadgetObject.gadgetParam;
	var gadgetLanguage = gadgetObject.language;
	
	shindig.IfrGadget.finishRender = function(chrome) {
		document.getElementById(this.getIframeId()).src = this.getIframeUrl();
	}
	
	shindig.BaseIfrGadget.prototype.getTitleBarContent = function(continuation) {
	  var settingsButton = this.hasViewablePrefs_() ?
	      '<a href="#" onclick="shindig.container.getGadget(' + this.id +
	          ').handleOpenUserPrefsDialog();return false;" class="' + this.cssClassTitleButton +
	          '">settings</a> '
	      : '';
	  var closeSpan = '<span class="gadget-icon icon-ibox-close right" onclick="closeOrExpandGadget(this,'+this.id+');return false;"></span>';
	  var maximumSpan = '<span id="maximumSpan" class="gadget-icon icon-ibox-maximum right" onclick="changeGadgetSize(this,'+this.id+','+gadgetWidth+','+gadgetContext+');return false;"></span>';
	  var refreshSpan = '<span class="gadget-icon icon-ibox-refresh right" onclick="shindig.container.getGadget('+this.id+').refresh();return false;"></span>';
	  continuation('<div id="' + this.cssClassTitleBar + '-' + this.id +
	      '" class="' + this.cssClassTitleBar + '"><span id="' +
	      this.getIframeId() + '_title" class="' +
	      this.cssClassTitle + '">' + (this.title ? this.title : 'Title') + '</span>'+closeSpan+maximumSpan+refreshSpan+'</div>');
	};
	
	var gadgetPort = gadgetObject.gadgetPort;
	var baseUrl = getBaseUrl('http://127.0.0.1', gadgetPort);
	$('#'+containerId).html(gadgetObject.gadgetLayout);
	$('#'+containerId).append('<div class="clear"></div>');
	var gadgetArr = gadgetObject.gadgetInfoList;
	var chromeIds = [];
	for (var i in gadgetArr) {
	  	var chromeId = "gadget-chrome"+i;
	  	chromeIds.push(chromeId);
	  	var $gadgetColumn = $('#gadget_column'+gadgetArr[i].columnNumber);
	  	if($gadgetColumn.find("div").length == 0) {
	  		$gadgetColumn.empty();
	  	}
	  	$gadgetColumn.append('<div id="'+ chromeId + '" class="gadget-chrome '+ (gadgetArr[i].gadgetClass?gadgetArr[i].gadgetClass:'') +'"></div>');
	  	var gadgetUrl = baseUrl + gadgetArr[i].gadgetPath;
	  	var title = gadgetArr[i].gadgetName;
		var cherryGadget = shindig.container.createGadget({'specUrl': gadgetUrl,'title': title});
		shindig.container.addGadget(cherryGadget);
  	}
	shindig.container.layoutManager.setGadgetChromeIds(chromeIds);
	if(gadgetLanguage != null && gadgetLanguage != "") {
		shindig.container.setLanguage(gadgetLanguage);
	}
  	shindig.container.renderGadgets();
}

// 左边菜单初始化
function gadgetLeftMenuInit(containerId,gadgetInfo) {
	if(containerId == null || containerId == undefined || "" == containerId) {
		return;
	}
	if(gadgetInfo == null || gadgetInfo == undefined || "" == gadgetInfo) {
		return;
	}
	var gadgetObject = eval('('+gadgetInfo+')');
	var gadgetWidth = gadgetObject.gadgetWidth;
	var gadgetContext = gadgetObject.gadgetContext;
	var gadgetArr = gadgetObject.gadgetInfoList;
	for (var i in gadgetArr) {
		$('#'+containerId).append('<li><a id="leftLink_'+i+'" href="#" onclick="gadgetClickLeftMenu(this,'+i+','+gadgetWidth+','+gadgetContext+');return false;">'+gadgetArr[i].gadgetName+'</a></li>');
	}
}

//点击左边菜单链接
function gadgetClickLeftMenu(object, id, gadgetWidth, gadgetContext) {
	var $object = $(object);
	if($object.hasClass('ui-corner-all on')) {
		return false;
	} else {
		gadgetAllRefresh = true;
		var $oldSelected = $object.parents('ul').find('.ui-corner-all');
		if($oldSelected.length > 0) {
			$oldSelected.removeClass("ui-corner-all on");
			var selectedId = $oldSelected.attr("id").split("_")[1];
			var $maxGadget = $('#gadget-chrome'+selectedId).find("#maximumSpan");
//			changeGadgetSize($maxGadget, selectedId, gadgetWidth, gadgetContext);
			$maxGadget.removeClass('icon-ibox-reduction').addClass('icon-ibox-maximum');
			$maxGadget.parents("div[id^=gadget_column]").css("width",gadgetWidth+"%");
//			shindig.container.getGadget(selectedId).refresh();
		}
		$object.addClass('ui-corner-all on');
		changeGadgetSize($('#gadget-chrome'+id).find("#maximumSpan"), id, gadgetWidth, gadgetContext);
	}
}

function getUserInfo() {
	return gadgetUserInfo;
}

function getGadgetParam() {
	return gadgetParam;
}

//取得BaseUrl（带有项目名的URL）
function getBaseUrl(prePath, gadgetPort) {
	var strFullPath = window.document.location.href;
	var strPath = window.document.location.pathname;
	if(!prePath) {
		prePath = strFullPath.substring(0, strFullPath.indexOf(strPath));
	} else {
		var port = window.document.location.port;
		if(gadgetPort) {
			port = gadgetPort;
		}
		prePath = prePath + ":" + port;
	}
	var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
	return prePath + postPath;
}

function getGadgetSizeMax() {
	return gadgetSizeMax;
}

//小工具最大化和还原
function changeGadgetSize(object, id, gadgetWidth, gadgetContext) {
	var $object = $(object);
	if($object.hasClass('icon-ibox-maximum')) {
		gadgetSizeMax = true;
		$object.removeClass('icon-ibox-maximum').addClass('icon-ibox-reduction');
		$object.parents("div[id^=gadget-chrome]").siblings().hide();
		$object.parents("div[id^=gadget_column]").siblings().hide();
		$object.parents("div[id^=gadget-chrome]").show();
		$object.parents("div[id^=gadget_column]").show();
		$object.parents("div[id^=gadget_column]").css("width",gadgetContext+"%");
		$('#leftLink_'+id).addClass("ui-corner-all on");
		shindig.container.getGadget(id).refresh();
	} else {
		gadgetSizeMax = false;
		$object.removeClass('icon-ibox-reduction').addClass('icon-ibox-maximum');
		$object.parents("div[id^=gadget-chrome]").siblings().show();
		$object.parents("div[id^=gadget_column]").siblings().show();
		$object.parents("div[id^=gadget_column]").css("width",gadgetWidth+"%");
		$('#leftLink_'+id).removeClass("ui-corner-all on");
		if(gadgetAllRefresh) {
			shindig.container.refreshGadgets();
			gadgetAllRefresh = false;
		} else {
			shindig.container.getGadget(id).refresh();
		}
	}
}

// 双击时小工具隐藏和展开
function closeOrExpandGadgetByDbclick(object, id) {
	var $object = $(object).find('span.icon-ibox-expand');
	if($object.length == 0) {
		$object = $(object).find('span.icon-ibox-close');
	}
	if($object.length > 0) {
		closeOrExpandGadget($object[0], id);
	}
}

// 小工具隐藏和展开
function closeOrExpandGadget(object, id) {
	if($(object).hasClass('icon-ibox-expand')) {
		$(object).removeClass('icon-ibox-expand').addClass('icon-ibox-close');
	} else {
		$(object).removeClass('icon-ibox-close').addClass('icon-ibox-expand');
	}
	shindig.container.getGadget(id).handleToggle();
}

////点击左边菜单链接
//function gadgetClickLeftMenu(object, id) {
//	var $object = $(object);
//	if($object.hasClass('ui-corner-all on')) {
//		return false;
//	} else {
//		var $oldSelected = $object.parents('ul').find('.ui-corner-all');
//		if($oldSelected.length > 0) {
//			$oldSelected.removeClass("ui-corner-all on");
//		}
//		$object.addClass('ui-corner-all on');
//		var gadgetObject = eval('('+$("#gadgetInfoId").val()+')');
//		var gadgetArr = gadgetObject.gadgetInfoList;
//		gadgetMaxInit(gadgetArr[id],gadgetArr.length);
//	}
//}
//
//// 最大化小工具
//function gadgetMaxInit(gadgetInfo,length) {
//	$("#gadgetContainer").hide();
//	$("#gadgetMaxContainer").show();
//	var baseUrl = getBaseUrl();
//	var gadgetUrl = baseUrl + gadgetInfo.gadgetPath;
//  	var title = gadgetInfo.gadgetName;
//	if($("#gadget-chrome-max").length > 0) {
//		shindig.container.getGadget(length).title = title;
//		shindig.container.getGadget(length).specUrl = gadgetUrl;
//		shindig.container.getGadget(length).refresh();
//	} else {
//	  	$("#gadgetMaxContainer").append('<div id="gadget-chrome-max" class="gadget-chrome '+ (gadgetInfo.gadgetClass?gadgetInfo.gadgetClass:'') +'"></div>');
//		var cherryGadget = shindig.container.createGadget({'specUrl': gadgetUrl,'title': title});
//		shindig.container.addGadget(cherryGadget);
//		shindig.container.getGadget(length).render($("#gadget-chrome-max")[0]);
//	}
//}

