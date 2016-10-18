var BINOLMBPTM04_1 = function () {
	this.needUnlock = true;
};
BINOLMBPTM04_1.prototype = {
		"search" : function() {
			var $form = $("#detailMainForm");
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var url = $("#detailSearch_Url").attr("href");
			url += "?" + getSerializeToken();;
			url += "&" + $form.serialize();
			// 显示结果一览
			$("#pointDetailInfo").removeClass("hide");
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId : '#pointDetailTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "3%", "bSortable": false },
					                { "sName": "memCode", "sWidth": "20%" },
									{ "sName": "memName", "sWidth": "10%" },
									{ "sName": "mobilePhone", "sWidth": "10%" },
									{ "sName": "point", "sWidth": "10%" },
									{ "sName": "pointTime", "sWidth": "10%" },
									{ "sName": "resultFlag","sClass":"center"},
									{ "sName": "importResults", "sWidth": "27%" ,"bSortable": false}],
					// 不可设置显示或隐藏的列	
					aiExclude :[0, 1,2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback:function() {
						$('#pointDetailTable').find("a.description").cluetip({
							splitTitle: '|',
						    width: 350,
						    height: 'auto',
						    cluetipClass: 'default', 
						    cursor: 'pointer',
						    showTitle: false
						});
					},
					callbackFun : function (msg){
				 		var $msg = $("<div></div>").html(msg);
				 		var $headInfo = $("#headInfo",$msg);
				 		if($headInfo.length > 0){
				 			$("#headInfo").html($headInfo.html());
				 		}else{
				 			$("#headInfo").empty();
				 		}
				 	}
			};
			// 调用获取表格函数
			getTable(tableSetting);
	    },
	    /* 
	     * 导出Excel
	     */
	    "exportExcel":function (url){
	    	var that = this;
	    	//无数据不导出
	        if($(".dataTables_empty:visible").length==0){
	    	    if (!$('#detailMainForm').valid()) {
	                return false;
	            }
	    	    url += "?" + getSerializeToken();
	    		url += "&" + $("#detailMainForm").serialize();
	            //锁住父页面
	            that.needUnlock=false;
	            document.location.href = url;
	            //开启父页面
	            that.needUnlock=true;
	        }
	    }
};
var BINOLMBPTM04_1 = new BINOLMBPTM04_1();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLMBPTM04_1.search();
});
window.onbeforeunload = function() {
	if (BINOLMBPTM04_1.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};