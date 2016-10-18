var BINOLSTJCS11_03=function(){};

BINOLSTJCS11_03.prototype = {
	"doSave": function(url){
		$("#actionResultDisplay").empty();
		if(!$("#mainForm").valid()){
			return;
		}
		url += "?" + parentTokenVal();
		var params = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: params,
			callback: function(msg){
				if(msg.indexOf("actionResultDiv") == -1){
					if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
				}
			}
		});
	},
	//柜台弹出框
    "popCounterDialog": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#organizationId").val($checkedRadio.val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="BINOLSTJCS11_03.delOrgHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#counterSpan").html('<span>' + html + '</span>');
			}
		}
		var value = $("#organizationId").val();
		var param = "validFlag=1&privilegeFlg=1";
		popDataTableOfCounterList(url, param, value, callback);
	},
	"delOrgHtml": function(_this){
		$(_this).parent().remove();
	}
	
};
var BINOLSTJCS11_03 = new BINOLSTJCS11_03();

//画面初始状态加载
$(document).ready(function() {
    $('#startDate').cherryDate({
        beforeShow: function(input){
            var value = $('#endDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endDate').cherryDate({
        beforeShow: function(input){
            var value = $('#startDate').val();
            return [value,'minDate'];
        }
    });
    
    $("#startTime").timepicker({
		timeFormat: 'HH:mm:ss',
		showSecond: true,
		timeOnlyTitle: $('#timeOnlyTitle').text(),
		currentText: $('#currentText').text(),
		closeText: $('#closeText').text(),
		hourMax: 23
	});
    $("#endTime").timepicker({
		timeFormat: 'HH:mm:ss',
		showSecond: true,
		timeOnlyTitle: $('#timeOnlyTitle').text(),
		currentText: $('#currentText').text(),
		closeText: $('#closeText').text(),
		hourMax: 23
	});
    
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			organizationId:{required:true},
			batchCode:{required:true,maxlength:25,alphanumeric:true},
			comments:{maxlength:200},
			startDate: {required:true, dateValid:true},	// 开始日期
			endDate: {required:true, dateValid:true},	// 结束日期
			startTime: {required:true, timeHHmmssValid:true},	// 开始时间
			endTime: {required:true, timeHHmmssValid:true}	// 结束时间
		}		
	});
});
