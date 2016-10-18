
function BINOLPLSCF16() {};

BINOLPLSCF16.prototype = {
	"updateIndex" : function(updateIndexUrl) {
		var confirmMes = $("#confirmMes").text();
		var param = $("#updateIndexForm").serialize();
		if(confirm(confirmMes)) {
			cherryAjaxRequest({
				url: updateIndexUrl,
				param: param,
				reloadFlg : true,
				callback: function(msg) {
				}
			});
		}
	},
	"searchMemInit": function(url) {
		var param = $("#updateIndexForm").serialize();
		popDataTableOfMemList(url, param);
	},
	"searchMemSaleInit": function(url) {
		var param = $("#updateIndexForm").serialize();
		popDataTableOfMemSaleList(url, param);
	}
};

var binolplscf16 =  new BINOLPLSCF16();