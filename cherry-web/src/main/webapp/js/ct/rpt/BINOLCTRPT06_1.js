var BINOLCTRPT06_1_GLOBAL = function() {

};

BINOLCTRPT06_1_GLOBAL.prototype = {
		
		"detailSearch" : function() {
			var $form = $('#detailForm');
			if(!$form.valid()){
				return false;
			}
			var url = $("#detailSearchUrl").attr("href");
			var params = $form.serialize();
			url = url + "?" + params + "&" + getSerializeToken();
			$("#memberDetailSection").show();
			// 表格设置
			var tableSetting = {
				index : 2,
				// 表格ID
				tableId : "#dataTableDetail",
				// 数据URL
				url : url,
				// 排序列
				aaSorting : [ [ 3, "desc" ] ],
				// 表格列属性设置
				aoColumns : [{"sName" : "RowNumber", "bSortable": false},
		                    {"sName" : "memberCode"},
		                    {"sName" : "memberName"},
		                    {"sName" : "orderQuantity"},
		                    {"sName" : "orderAmount"},
		                    {"sName" : "dealQuantity"},
		                    {"sName" : "dealAmount"}],
                // 不可设置显示或隐藏的列	
    			aiExclude :[0, 1, 2],
    			// 固定列数
    			fixedColumns : 3,
				callbackFun : function(msg) {
					var $msg = $("<div></div>").html(msg);
			 		var $memCommunResultInfo = $("#memCommunResultInfo",$msg);
			 		if($memCommunResultInfo.length > 0){
			 			$("#memCommunResultInfo").html($memCommunResultInfo.html());
			 		}else{
			 			$("#memCommunResultInfo").empty();
				 	}
				}
			};
			// 调用获取表格函数
			getTable(tableSetting);
		}
	
};

var BINOLCTRPT06_1 = new BINOLCTRPT06_1_GLOBAL();

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
	if (window.opener) {
        window.opener.lockParentWindow();
    }
});
