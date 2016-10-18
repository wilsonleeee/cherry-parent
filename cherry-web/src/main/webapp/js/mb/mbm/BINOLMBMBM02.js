function BINOLMBMBM02() {
	this.needUnlock = true;
};

BINOLMBMBM02.prototype = {
		
	"searchSaleRecord" : function() {
		var url = $("#searchSaleRecordUrl").attr("href");
		var parentToken = parentTokenVal();
		$("#saleCherryForm").find("#parentCsrftoken").val(parentToken);
		var params= $("#saleCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#memSaleDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 6, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "billCode", "sWidth": "25%" },
				                { "sName": "departCode", "sWidth": "20%" },
				                { "sName": "memberCode", "sWidth": "10%" },
								{ "sName": "saleType", "sWidth": "10%" },
								{ "sName": "quantity", "sWidth": "10%" },
								{ "sName": "amount", "sWidth": "10%" },
								{ "sName": "saleTime", "sWidth": "15%" }],
				// 横向滚动条出现的临界宽度
				//sScrollX : "100%",
				//bAutoWidth : true,
				index: 0,
				fnDrawCallback : function() {
					$("#memSaleDataTable").find('tr').click(function() {
						binolmbmbm02.searchDetail(this);
					});
				},
				callbackFun: function(msg) {
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#saleCountInfo",$msg);
			 		if($headInfo.length > 0) {
			 			$("#saleCountInfo").html($headInfo.html());
			 		} else {
			 			$("#saleCountInfo").empty();
			 		}
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
//		$(window).bind('resize', function () {
//			if(oTableArr[0]) {
//				oTableArr[0].fnAdjustColumnSizing();
//			}
//		});
    },
    "searchDetail" : function(object) {
    	$(object).parent().find("tr[class=detail]").hide();
		if($(object).attr("class").indexOf("red") == -1) {
			$(object).addClass("red");
			$(object).siblings().removeClass("red");
			if($(object).next() && $(object).next().attr("class") == "detail") {
	    		$(object).next().show();
	    	} else {
	    		var url = $(object).find("input").val();
	    		if(url) {
	    			$(object).after('<tr class="detail"><td colspan="'+$(object).find('td').length+'" class="detail box2-active"></td></tr>');
	        		var $td = $(object).next().find("td");
	            	var callback = function(msg) {
	            		$td.html(msg);
	            	};
	            	cherryAjaxRequest({
	            		url: url,
	            		callback: callback
	            	});
	    		}
	    	}
		} else {
			$(object).removeClass("red");
		}
    },
    "changeClub" : function(obj) {
    	var url = $("#changeClubUrl").attr("href");
    	var param = $("#memberClubId").serialize() + "&" + $("#memberInfoId").serialize();
    	cherryAjaxRequest({
			url: url,
			param: param,
			callback:  function(msg){
				$("#clubTable").html(msg);
			}
		});
    }
};

var binolmbmbm02 =  new BINOLMBMBM02();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	$('.tabs').tabs();
	$("a@[href='#tabs-2']").click(function() {
		binolmbmbm02.searchSaleRecord();
		productBinding({elementId:"nameTotal",showNum:20});
    });
	$("a@[href='#tabs-3']").click(function() {
		if(oTableArr[1]) {
			oTableArr[1] = null;
		}
		var url = $("#PointDetailInitUrl").attr("href");
		var parentToken = parentTokenVal();
		$("#PointDetailInitForm").find("#parentCsrftoken").val(parentToken);
		var params= $("#PointDetailInitForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		var callback = function(msg){
			$("#memPointInfoDiv").html(msg);
		}
		cherryAjaxRequest({
			url: url,
			param: null,
			callback: callback
		});
    });
} );

window.onbeforeunload = function(){
	if (binolmbmbm02.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};