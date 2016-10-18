function BINOLCPACT04() {
	this.needUnlock = true;
};

BINOLCPACT04.prototype = {
		// 查询
		"memSearch" : function(index) {
			var url = '/Cherry/cp/BINOLCPCOM03_memInfosearch';
			var searchCode= $("#searchCode").val();
			var campMebJson = $('#campMebJson').val();
			if(!isEmpty(searchCode) || !isEmpty(campMebJson)){
				var params= getSerializeToken();
//				var brandInfoId=$("#brandInfoId").val();
//				params +="&brandInfoId="+brandInfoId;
				if(!isEmpty(searchCode)){
					params +="&searchCode="+searchCode;
				}
				if(!isEmpty(campMebJson)){
					params +="&campMebInfo="+campMebJson;
				}
				url = url + "?" + params;
				// 显示结果一览
				$("#mebResult_div").show();
				$("#memberInfo").show();
				// 表格设置
				var tableSetting = {
						 index: 100000,
						 // 表格ID
						 tableId : '#memberDataTable',
						 // 数据URL
						 url : url,
						 iDisplayLength:5,
						 // 表格默认排序
						 aaSorting : [[ 1, "asc" ]],
						 // 表格列属性设置
						 aoColumns : [
						                { "sName": "customerType","bSortable": false},
						                { "sName": "memCode"},
						                { "sName": "memName"},
						                { "sName": "mobilePhone"},
						                { "sName": "birthDay","bSortable": false},
										{ "sName": "joinDate","bSortable": false},
						                { "sName": "changablePoint","sClass":"alignRight","bSortable": false},
						                { "sName": "receiveMsgFlg","sClass":"center","bSortable": false}]
				};
				// 调用获取表格函数
				getTable(tableSetting);
			}
	    },
	    "searchConDialog" :function(conInfo){
	    	var	dialogId = 'memSearchDialog';
	    	var $dialog = $('#' + dialogId);
			var url = '/Cherry/common/BINOLCM33_init';
			var params = "reqContent=" + conInfo;
			if($dialog.length == 0){
				$('#div_main').append('<div id="'+dialogId+'"></div>');
			}
			var dialogSetting = {
				dialogInit: '#' + dialogId,
				width: 900,
				height: 580,
				title: $("#objDialogTitle").text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				//确认按钮
				confirmEvent: function(){removeDialog('#' + dialogId);},
				//关闭按钮
				cancelEvent: function(){removeDialog('#' + dialogId);}
			};
			openDialog(dialogSetting);
			cherryAjaxRequest({
				url: url,
				param : params,
				callback: function(msg) {
					$('#' + dialogId).html(msg);
				}
			});
		},
	"searchDetail" : function(object) {
		$(object).parent().find("tr[class=detail]").remove();
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
	"showMore": function(_this,moreId){
		var $moreId = $(moreId);
		if($moreId.is(':visible')){
			$moreId.hide('slow');
		}else{
			$moreId.show('slow');
		}
	}
};

var BINOLCPACT04 =  new BINOLCPACT04();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	$('#actMenuDiv').find("li.TIP").cluetip({width: '380',splitTitle:'|'});
	$("#actMenuDiv").find("a").click(function() {
		var url = $(this).attr("dir");
		$("#actMenuDiv").find('a').removeClass("subon").removeClass("on");
		$(this).parents('li').children('a').addClass("on");
		if(!$(this).hasClass("menuOne")) {
			$(this).addClass("subon");
		} else {
			var $first = $(this).parent().find('ul a').first();
			if($first.length > 0) {
				$first.addClass("subon");
				url = $first.attr("dir");
			}
		}
		
		if(url) {
			oTableArr = new Array(null,null);
			fixedColArr = new Array(null,null);
			var parentToken = parentTokenVal();
			$("#actDetailForm").find("#parentCsrftoken").val(parentToken);
			var params= $("#actDetailForm").serialize();
			if(params != null && params != "") {
				if (url.indexOf("?") > 0) {
					url += "&" + params;
				} else {
					url = url + "?" + params;
				}
			}
			var callback = function(msg){
				$("#actDetailDiv").html(msg);
			}
			cherryAjaxRequest({
				url: url,
				param: null,
				callback: callback
			});
		}
		return false;
	});
	
	$("#crmArrowDiv").find("a").click(function() {
		$(this).toggleClass("crm_l_arrow");
		$(this).toggleClass("crm_l_arrow_f");
		$("#actMenuDiv").toggleClass("crm_left");
		$("#actMenuDiv").toggleClass("crm_left_f");
		$("#crmArrowDiv").toggleClass("crm_middle");
		$("#crmArrowDiv").toggleClass("crm_middle_f");
		$("#crmContentDiv").toggleClass("crm_right");
		$("#crmContentDiv").toggleClass("crm_right_f");
		$("#actDetailDiv").toggleClass("crm_content");
		$("#actDetailDiv").toggleClass("crm_content_f");
	});
	
	$("#topMenu").click();
});

window.onbeforeunload = function() {
	if (BINOLCPACT04.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};