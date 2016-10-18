var BINOLMBMBM18_1 = function () {
	this.needUnlock = true;
};
BINOLMBMBM18_1.prototype = {
		"search" : function() {
			var $form =$("#detailMainForm");
			$form.find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			var $impType = $("#impType").val();
			var url = $("#detailSearch_Url").attr("href");
			url += "?" + getSerializeToken();;
			url += "&" + $form.serialize();
			// 显示结果一览
			$("#detailInfo").removeClass("hide");
			var aoColumn = null;
			var aaSorting = null;
			var index = 0;
			if($impType=='1'){
				index= 1;
				aaSorting = [[1, "desc"]];
				aoColumn= [  { "sName": "number", "sWidth": "3%", "bSortable": false },
				                { "sName": "MemberCode"},
								{ "sName": "MemName"},
								{ "sName": "MemPhone","bVisible": false},
								{ "sName": "MobilePhone"},
								{ "sName": "MemSex"},
								{ "sName": "MemProvince"},
								{ "sName": "MemCity"},
								{ "sName": "MemAddress","bVisible": false},
								{ "sName": "MemPostcode","bVisible": false},
								{ "sName": "MemBirthday"},
								{ "sName": "MemAgeGetMethod","bVisible": false},
								{ "sName": "MemMail","bVisible": false},
								{ "sName": "MemGranddate"},
								{ "sName": "Bacode"},
								{ "sName": "CardCounter"},
								{ "sName": "MemLevel","sClass":"center","bVisible": false},
								{ "sName": "InitTotalAmount"},
								{ "sName": "Referrer","bVisible": false},
								{ "sName": "IsReceiveMsg","sClass":"center","bVisible": false},
								{ "sName": "TestMemFlag","sClass":"center"},
								{ "sName": "Memo1","sClass":"center"},
								{ "sName": "ChannelCode","sClass":"center"},
								{ "sName": "ResultFlag","sClass":"center"},
								{ "sName": "ImportResults","bSortable": false}];
			}else if($impType=='2'){
				index= 2;
				aaSorting = [[1, "desc"]];
				aoColumn= [  { "sName": "number", "sWidth": "3%", "bSortable": false },
				                { "sName": "MemberCode"},
								{ "sName": "MemName"},
								{ "sName": "MemPhone","bVisible": false},
								{ "sName": "MobilePhone"},
								{ "sName": "MemSex"},
								{ "sName": "MemProvince"},
								{ "sName": "MemCity"},
								{ "sName": "MemAddress","bVisible": false},
								{ "sName": "MemPostcode","bVisible": false},
								{ "sName": "MemBirthday"},
								{ "sName": "MemMail","bVisible": false},
								{ "sName": "Bacode"},
								{ "sName": "CardCounter"},
								{ "sName": "InitTotalAmount"},
								{ "sName": "Referrer","bVisible": false},
								{ "sName": "IsReceiveMsg","sClass":"center","bVisible": false},
								{ "sName": "TestMemFlag","sClass":"center"},
								{ "sName": "Memo1","sClass":"center"},
								{ "sName": "ChannelCode","sClass":"center"},
								{ "sName": "ResultFlag","sClass":"center"},
								{ "sName": "ImportResults","bSortable": false}];
			}
			// 表格设置
			var tableSetting = {
					 index:index,
					 // 表格ID
					 tableId : '#detailTable_'+index,
					 // 数据URL
					 url : url,
					 // 表格默认排序
					 aaSorting : aaSorting,
					 // 表格列属性设置
					 aoColumns : aoColumn,
					// 不可设置显示或隐藏的列	
					aiExclude :[0, 1, 2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					fnDrawCallback:function() {
						$('#detailTable_'+index).find("a.description").cluetip({
							splitTitle: '|',
						    width: '300',
						    height: 'auto',
						    cluetipClass: 'default', 
						    cursor: 'pointer',
						    showTitle: false
						});
						$('#detailTable_'+index).find("a.Memo").cluetip({
							splitTitle: '|',
						    width: '300',
						    height: 'auto',
						    cluetipClass: 'default',
						    cursor:'pointer',
						    arrows: false
						});
						$('#detailTable_'+index).find("a.MemAddress").cluetip({
							splitTitle: '|',
						    width: '300',
						    height: 'auto',
						    cluetipClass: 'default',
						    cursor:'pointer',
						    arrows: false
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
var BINOLMBMBM18_1 = new BINOLMBMBM18_1();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLMBMBM18_1.search();
});
window.onbeforeunload = function() {
	if (BINOLMBMBM18_1.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};
