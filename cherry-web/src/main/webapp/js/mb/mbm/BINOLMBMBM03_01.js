var binolmbMBM03_1_global = {};
// 是否刷新父页面
binolmbMBM03_1_global.doRefresh = false;
binolmbMBM03_1_global.needUnlock = true;
$(document).ready(function() {
	var memberLevelId = $('#memberLevelId').val();
	var totalAmount = $('#totalAmount').val();
	var btimes = $('#btimes').val();
	var joinDate = $('#joinDate').val();
	// 取更新前的等级
	$('#oldmemberLevelId').val(memberLevelId);
	// 取更新前的金额
	$('#oldtotalAmount').val(totalAmount);
	// 取更新前的化妆次数
	$('#oldbtimes').val(btimes);
	// //取更新前的入会时间
	$('#oldjoinDate').val(joinDate);
	// 表单验证配置
	cherryValidate({
		formId : 'mainForm',
		rules : {
			memberLevelId : {
				required : true
			},// 等级
			joinDate : {
				required : true,
				dateValid : true
			},// 入会时间
			totalAmount : {
				pointValid : [ 10, 2 ]
			},// 累计金额
			btimes : {
				digits : true,
				maxlength : 9
			},// 化妆次数
			comments : {
				required : true
			}
		// 备注
		}
	});

});
// 保存提交
function doSave(url) {
	if (!$('#mainForm').valid()) {
		return false;
	}
	var param = $('#mainForm').serialize();
	cherryAjaxRequest({
		url : url,
		param : param,
		callback : function(msg) {
		}
	});
}
/**
 * 验证数字
 * 
 */
function MBM03_Number(obj) {
	var $this = $(obj);
	if (isNaN($this.val().toString())) {
		$this.val("");
		$this.parent().next().html("0.00");
	} else {
		var price = Number($this.parent().parent().find("#dataTd4").html());
		$this.val(parseInt(Number($this.val())));
		var amount = price * Number($this.val());
		$this.parent().next().html(amount.toFixed(2));
	}
}

function changeMB03Level() {
	var url = $("#changeLevelUrl").attr("href");
	var param = $("#memberClubId").serialize() + "&" + $("#memberInfoId").serialize() + "&" + "csrftoken=" + getTokenVal();
	var value = 'memberLevelId';
	var label = 'levelName';
	$.ajax({
        url: url, 
        type: 'post',
        data: param,
        success: function(msg){
					if(window.JSON && window.JSON.parse) {
						var mjson = window.JSON.parse(msg);
						var checkedVal = mjson['oldLevelId'];
						$('#oldmemberLevelId').val(checkedVal);
						$('#memberLevelId').find("option").not(":eq(0)").remove();
						var msgJson = mjson['MSGJSON'];
				    	for (var one in msgJson){
						    var selVal = msgJson[one][value];
						    var selLab = msgJson[one][label];
					        var str = '<option value="'+ selVal + '">' + escapeHTMLHandle(selLab) + '</option>';
					        if(checkedVal == selVal){
					        	str = '<option selected="selected" value="'+ selVal + '">' + escapeHTMLHandle(selLab) + '</option>';
					        }
					        $('#memberLevelId').append(str); 
					    }
					}
					
				}
    });
}
