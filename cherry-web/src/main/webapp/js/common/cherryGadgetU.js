// 用户信息
var gadgetUserInfo;
// 小工具共通参数
var gadgetParam;
// 小工具信息数组
var gadgetArr;
// 是否刷新所有小工具内容
var gadgetAllRefresh = false;
// 小工具是否最大化
var gadgetSizeMax = false;
// 语言环境
var gadgetLanguage;

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
	gadgetLanguage = gadgetObject.language;
	var gadgetPort = gadgetObject.gadgetPort;
	$('#'+containerId).html(gadgetObject.gadgetLayout);
	$('#'+containerId).append('<div class="clear"></div>');
	gadgetArr = gadgetObject.gadgetInfoList;
	for (var i in gadgetArr) {
	  	var chromeId = "gadget-chrome"+i;
	  	var $gadgetColumn = $('#gadget_column'+gadgetArr[i].columnNumber);
	  	var title = gadgetArr[i].gadgetName;
	  	var chromeDiv = '';
	  	chromeDiv += '<div id="'+ chromeId + '" class="gadget-chrome '+ (gadgetArr[i].gadgetClass?gadgetArr[i].gadgetClass:'') +'">';
	  	chromeDiv += '<div id="gadgets-gadget-title-bar-1" class="gadgets-gadget-title-bar">';
	  	chromeDiv += '<span id="remote_iframe_1_title" class="gadgets-gadget-title">'+title+'</span>';
	  	chromeDiv += '<span class="gadget-icon right icon-ibox-close" onclick="closeOrExpandGadget(this,'+i+');return false;"></span>';
	  	chromeDiv += '<span id="maximumSpan" class="gadget-icon icon-ibox-maximum right" onclick="changeGadgetSize(this,'+i+','+gadgetWidth+','+gadgetContext+');return false;"></span>';
	  	chromeDiv += '<span class="gadget-icon icon-ibox-refresh right" onclick="refreshSingleGadget('+i+');return false;"></span>';
	  	chromeDiv += '</div>';
	  	chromeDiv += '<div id="remote_iframe_1_userPrefsDialog" class="gadgets-gadget-user-prefs-dialog"></div>';
	  	chromeDiv += '<div class="gadgets-gadget-content"></div>';
	  	chromeDiv += '</div>';
	  	$gadgetColumn.append(chromeDiv);
  	}
	refreshAllGadget();
}

//左边菜单初始化
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
			$maxGadget.removeClass('icon-ibox-reduction').addClass('icon-ibox-maximum');
			$maxGadget.parents("div[id^=gadget_column]").css("width",gadgetWidth+"%");
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

function getGadgetSizeMax() {
	return gadgetSizeMax;
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
		refreshSingleGadget(id);
	} else {
		gadgetSizeMax = false;
		$object.removeClass('icon-ibox-reduction').addClass('icon-ibox-maximum');
		$object.parents("div[id^=gadget-chrome]").siblings().show();
		$object.parents("div[id^=gadget_column]").siblings().show();
		$object.parents("div[id^=gadget_column]").css("width",gadgetWidth+"%");
		$('#leftLink_'+id).removeClass("ui-corner-all on");
		if(gadgetAllRefresh) {
			refreshAllGadget();
			gadgetAllRefresh = false;
		} else {
			refreshSingleGadget(id);
		}
	}
}

// 小工具隐藏和展开
function closeOrExpandGadget(object, id) {
	if($(object).hasClass('icon-ibox-expand')) {
		$(object).removeClass('icon-ibox-expand').addClass('icon-ibox-close');
		$('#gadget-chrome'+id).find(".gadgets-gadget-content").show();
	} else {
		$(object).removeClass('icon-ibox-close').addClass('icon-ibox-expand');
		$('#gadget-chrome'+id).find(".gadgets-gadget-content").hide();
	}
}

// 刷新单个小工具
function refreshSingleGadget(id) {
	for (var i in gadgetArr) {
	  	if(id == i) {
	  		var baseUrl = getBaseUrl();
	  		var gadgetUrl = baseUrl + gadgetArr[i].gadgetPath;
	  		refreshGadget(i, gadgetUrl);
	  		break;
	  	}
  	}
}

// 刷新所有小工具
function refreshAllGadget() {
	var baseUrl = getBaseUrl();
	for (var i in gadgetArr) {
	  	var gadgetUrl = baseUrl + gadgetArr[i].gadgetPath;
	  	refreshGadget(i, gadgetUrl);
  	}
}

// 小工具内容初始化
function refreshGadget(id,url) {
	if(endWith(url, ".html")) {
		var iframe = '<iframe class="gadgets-gadget" src="'+url+'" frameborder="no" scrolling="no"></iframe>';
		$('#gadget-chrome'+id).find(".gadgets-gadget-content").html(iframe);
	}
}

// 判断字符串是否以指定字符结尾
function endWith(url, endStr) {
	var d = url.length-endStr.length;
	return (d>=0 && url.lastIndexOf(endStr)==d)
}

// iframe自适应
function adjustHeight(id) {
	var ifm = document.getElementById(id);
    var subWeb = document.frames ? document.frames[id].document : ifm.contentDocument;
    if(ifm != null && subWeb != null) {
    	ifm.height = subWeb.body.scrollHeight;
    }
}



// 小工具内容国际化
function GadgetPrefs() {};
GadgetPrefs.prototype = {
	"getMsg" : function(key) {
		if(gadgetLanguage == 'zh_TW') {
			return Language_zh_TW[key];
		} else {
			return Language_zh_CN[key];
		}
    }
};
var Language_zh_CN = {
		"moreButton":"显示更多",
		"memSale":"会员销售",
		"unMemSale":"非会员销售",
		"totalAmount":"今日总金额",
		"totalQuantity":"今日总数量",
		"amountProportion":"金额占比",
		"quantityProportion":"数量占比",
		"amount":"总金额",
		"quantity":"总金额",
		"yuan":"元",
		"salePart":"件",
		"dateTime":"时间",
		"member":"会员",
		"ns":"销售",
		"sr":"退货",
		"px":"积分兑换",
		"SALE_NS":"{0}在{1}完成了一笔{2}{3}，金额{4}元，数量{5}件",
		"SALE_SR":"{0}在{1}完成了一笔{2}{3}，金额{4}元，数量{5}件",
		"SALE_PX":"{0}在{1}给{2}进行了{3}，金额{4}元，数量{5}件"
};
var Language_zh_TW = {
		"moreButton":"显示更多",
		"memSale":"会员销售",
		"unMemSale":"非会员销售",
		"totalAmount":"今日总金额",
		"totalQuantity":"今日总数量",
		"amountProportion":"金额占比",
		"quantityProportion":"数量占比",
		"amount":"总金额",
		"quantity":"总金额",
		"yuan":"元",
		"salePart":"件",
		"dateTime":"时间",
		"member":"会员",
		"ns":"销售",
		"sr":"退货",
		"px":"积分兑换",
		"SALE_NS":"{0}在{1}完成了一笔{2}{3}，金额{4}元，数量{5}件",
		"SALE_SR":"{0}在{1}完成了一笔{2}{3}，金额{4}元，数量{5}件",
		"SALE_PX":"{0}在{1}给{2}进行了{3}，金额{4}元，数量{5}件"
};
var gadgetPrefs = new GadgetPrefs();
function getGadgetPrefs() {
	return gadgetPrefs;
}
