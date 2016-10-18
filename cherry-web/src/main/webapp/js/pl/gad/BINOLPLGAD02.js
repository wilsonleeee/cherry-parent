var BINOLPLGAD02 = function() {};

BINOLPLGAD02.prototype = {
		
	"searchGadgetList": function() {
		var url = $("#searchGadgetListUrl").attr("href");
		var param = $("#mainForm").serialize();
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg){
			$('#actionResultDisplay').empty();
			$('#gadgetInfoListDiv').html(msg);
		};
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	},
	"saveGadgetInfo": function(url) {
		var param = $("#gadgetInfoForm").serialize();
		var callback = function(msg){
			$('#actionResultDisplay').html(msg);
		};
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
};

var binolplgad02 = new BINOLPLGAD02();


$(function(){
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	binolplgad02.searchGadgetList();
});

window.onbeforeunload = function() {
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};