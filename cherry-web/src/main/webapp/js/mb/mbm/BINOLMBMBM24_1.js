var BINOLMBMBM24_1 = function () {
	this.needUnlock = true;
};
BINOLMBMBM24_1.prototype = {
		"search" : function() {
			var $form = $("#detailMainForm");
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var url = $("#detailSearch_Url").attr("href");
			url += "?" + getSerializeToken();;
			url += "&" + $form.serialize();
			// 显示结果一览
			$("#detailInfo").removeClass("hide");
			var tableSetting = {
					 // 表格ID
					 tableId : '#detailTable',
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : [[ 1, "desc" ]],
					 // 表格列属性设置
					 aoColumns : [  { "sName": "number", "sWidth": "3%", "bSortable": false },
					                { "sName": "MemberCode", "sWidth": "20%" },
									{ "sName": "MemberName", "sWidth": "15%" },
									{ "sName": "MemberLevel", "sWidth": "15%" },
									{ "sName": "JoinDate", "sWidth": "10%"},
									{ "sName": "CurBtimes", "sWidth": "10%","sClass":"center","bVisible": false},
									{ "sName": "ResultFlag","sClass":"center"},
									{ "sName": "ImportResults", "sWidth": "27%" ,"bSortable": false}],
					// 不可设置显示或隐藏的列	
					aiExclude :[0, 1, 2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback:function() {
						$('#detailTable').find("a.description").cluetip({
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
var BINOLMBMBM24_1 = new BINOLMBMBM24_1();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLMBMBM24_1.search();
});
window.onbeforeunload = function() {
	if (BINOLMBMBM24_1.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};