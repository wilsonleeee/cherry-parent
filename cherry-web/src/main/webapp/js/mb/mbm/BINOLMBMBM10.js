function BINOLMBMBM10() {
	this.needUnlock = true;
};

BINOLMBMBM10.prototype = {
	"searchDetail" : function(object) {
		$(object).parent().find("tr[class=detail]").remove();
		if($(object).attr("class").indexOf("selectedColor") == -1) {
			$(object).addClass("selectedColor");
			$(object).siblings().removeClass("selectedColor");
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
			$(object).removeClass("selectedColor");
		}
	},
	"addIssue": function(url) {
		var addIssueCallback = function(msg) {
			$("#addIssueDiv").html(msg);
			$('#expandIssueButton').children('#expandDirection').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#addIssueDiv').show();
			$('#expandIssueButton').children('#expandCharacter').text($("#hiddenButtonDiv").html());
    	};
    	cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: addIssueCallback
    	});
	}
};

var binolmbmbm10 =  new BINOLMBMBM10();

$(document).ready(function() {
	if($("#csrftoken").length == 0) {
		if (window.opener) {
	       window.opener.lockParentWindow();
	    }
	}
	$("#crmMenuDiv").find("a").click(function() {

		var url = $(this).attr("dir");
		$("#crmMenuDiv").find('a').removeClass("subon").removeClass("on");
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
			var params = $("#memDetailForm").serialize();
			var callback = function(msg){
				$("#memDetailDiv").html(msg);
			}
			cherryAjaxRequest({
				url: url,
				param: params,
				callback: callback
			});
		}
		return false;
	});
	
	$("#crmArrowDiv").find("a").click(function() {
		$(this).toggleClass("crm_l_arrow");
		$(this).toggleClass("crm_l_arrow_f");
		$("#crmMenuDiv").toggleClass("crm_left");
		$("#crmMenuDiv").toggleClass("crm_left_f");
		$("#crmArrowDiv").toggleClass("crm_middle");
		$("#crmArrowDiv").toggleClass("crm_middle_f");
		$("#crmContentDiv").toggleClass("crm_right");
		$("#crmContentDiv").toggleClass("crm_right_f");
		$("#memDetailDiv").toggleClass("crm_content");
		$("#memDetailDiv").toggleClass("crm_content_f");
		$("#addIssueDiv").toggleClass("crm_issue");
		$("#addIssueDiv").toggleClass("crm_issue_f");
	});
	
	// 打开关闭新建问题窗口
	$('#expandIssueButton').click(function(){
		if($(this).children('#expandDirection').is('.ui-icon-triangle-1-n')) {
			$(this).children('#expandDirection').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#addIssueDiv').show();
			$(this).children('#expandCharacter').text($("#hiddenButtonDiv").html());
		} else {
			$(this).children('#expandDirection').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#addIssueDiv').hide();
			$(this).children('#expandCharacter').text($("#showButtonDiv").html());
		}
	});
	
	$("#topMenu").click();
});

if($("#csrftoken").length == 0) {
	window.onbeforeunload = function() {
		if (binolmbmbm10.needUnlock) {
			if(window.opener){
				window.opener.unlockParentWindow();
			}
		}
	};
}
