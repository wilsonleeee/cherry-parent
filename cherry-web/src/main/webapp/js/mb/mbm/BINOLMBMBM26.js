function BINOLMBMBM26() {};

BINOLMBMBM26.prototype = {
	// 查询会员问题一览
    "searchIssueList": function() {
    	var $issueCherryForm = $("#issueCherryForm");
		var url = $("#searchIssueUrl").attr("href");
		url += "?" + getSerializeToken();
		url += "&" + $issueCherryForm.serialize();
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#issueDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 8, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  { "sName": "issueNo", "sWidth": "10%" },
				                { "sName": "issueSummary", "sWidth": "12%" },
				                { "sName": "issueType", "sWidth": "8%" },
				                { "sName": "assigneeName", "sWidth": "8%" },
				                { "sName": "speakerName", "sWidth": "8%" },
				                { "sName": "priority", "sWidth": "8%" },
				                { "sName": "issueStatus", "sWidth": "8%" },
				                { "sName": "resolution", "sWidth": "8%" },
				                { "sName": "createTime", "sWidth": "10%" },
				                { "sName": "updateTime", "sWidth": "10%" }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				//bAutoWidth : true,
				fnDrawCallback : function() {
					$('#issueDataTable').find("a.description").cluetip({
						splitTitle: '|',
					    width: 300,
					    height: 'auto',
					    cluetipClass: 'default', 
					    cursor: 'pointer',
					    showTitle: false
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    // 查询会员问题明细
	"searchIssueDetail": function(url) {
//    	if($("#issueDetailDialog").length == 0) {
//    		$("body").append('<div style="display:none" id="issueDetailDialog"></div>');
//    	} else {
//    		$("#issueDetailDialog").empty();
//    	}
//    	var dialogSetting = {
//			dialogInit: "#issueDetailDialog",
//			text: $("#dialogLoading").text(),
//			width: 700,
//			position: [(document.body.offsetWidth-700)/2,document.body.offsetHeight*0.05],
//			zIndex: 10000,
//			title: $("#issueDetailTitle").text()
//		};
//    	openDialog(dialogSetting);
    	var issueDetailCallback = function(msg) {
    		if($("#issueDetailDialog").length > 0) {
    			$("#issueDetailDialog").html(msg);
    		} else {
    			$("#addIssueDiv").html(msg);
    			$('#expandIssueButton').children('#expandDirection').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
    			$('#addIssueDiv').show();
    			$('#expandIssueButton').children('#expandCharacter').text($("#hiddenButtonDiv").html());
    		}
    	};
    	cherryAjaxRequest({
    		url: url,
    		param: null,
    		callback: issueDetailCallback
    	});
	},
	"selectIssueType": function(val) {
		var $first = $("#issueSubTypeQ").find('option:first');
		var option = '<option value="'+$first.val()+'">'+$first.text()+'</option>';
		var showFlag = false;
		if(val != "") {
			$("#issueSubTypeTempQ").find('option').each(function(){
				var _val = $(this).val();
				var text = $(this).text();
				if(_val.split('_')[0] == val) {
					option += '<option value="'+_val+'">'+text+'</option>';
					showFlag = true;
				}
			});
		}
		$("#issueSubTypeQ").html(option);
		if(showFlag) {
			$("#issueSubTypeQ").show();
		} else {
			$("#issueSubTypeQ").hide();
		}
	}
};

var binolmbmbm26 =  new BINOLMBMBM26();

$(function() {
	binolmbmbm26.searchIssueList();
	if($("#issueSubTypeTempQ").find('option').length > 0) {
		$("#issueTypeQ").change(function(){
			var val = $(this).val();
			binolmbmbm26.selectIssueType(val);
		});
		$("#issueTypeQ").change();
	}
});

