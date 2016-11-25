function BINOLBSCNT10() {};

BINOLBSCNT10.prototype = {
	"searchPointDetail" : function() {

	},
	"changeTab" : function(thisObj) {

		$(thisObj).siblings().removeClass('ui-tabs-selected');
		$(thisObj).addClass('ui-tabs-selected');

	},
	"exportExcel" : function() {
		if($(".dataTables_empty:visible").length==0) {
			//alert(oTableArr[0].fnSettings().sAjaxSource)
			var url = $("#exportUrl").attr("href");
			url += "?" + getSerializeToken();
			url += "&" + $("#pointDetailForm").serialize();
			document.location.href = url;
		}
	},
	"changeClub" : function() {
		binolbscnt10.searchMemPoint();
		binolbscnt10.searchPointDetail();
	},
	"searchMemPoint" : function() {

	}
};

var binolbscnt10 =  new BINOLBSCNT10();

$(function() {


});

