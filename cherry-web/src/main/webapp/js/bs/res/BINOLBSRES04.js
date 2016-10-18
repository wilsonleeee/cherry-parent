window.onbeforeunload = function() {
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function() {
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({
		formId : "add",
		rules : {
			brandInfoId : {
				required : true
			},
			levelCode: {
				required : true
			},
			resellerCode : {
				required : true,
				maxlength : 20
			}, // 渠道名称
			resellerName : {
				required : true,
				maxlength : 30
			}
		}
	});
});

function changeLevel(){
	var levelCode = $("#levelCode").val();
	$("#parentResellerCode").val("");
	$("#parentResellerName").html("");
	if(levelCode == '2'){
		$("#parentResellerCodeBtn").show();
	}else{
		$("#parentResellerCodeBtn").hide();
	}
}

function selectParentReseller(){
	var url = $("#initResellerDialog").attr("href");
	var levelCode = $.trim($("#levelCode").val());
	if(levelCode == null || levelCode == ''){
		levelCode = 0;
	}else{
		levelCode = parseInt(levelCode) - 1;
	}
	var param = "param=" + levelCode;
	var value = $("#parentResellerCode").val();;
	var callback = function(objId){
		var checkedRadio = $("#" + objId).find("input[name='resellerCode']:checked");
		var resellerCode = "";
		var resellerName = "";
		if(checkedRadio){
			resellerCode = checkedRadio.val();
			resellerName = checkedRadio.parent().parent().parent().children("td").eq(2).find("span").text().escapeHTML();
		}
		$("#parentResellerCode").val(resellerCode);
		$("#parentResellerName").html( "（" + resellerCode + "）" + resellerName.unEscapeHTML());
	}
	popDataTableOfResellerList(url, param, value, callback);
}

function save() {
	if (!$('#add').valid()) {
		return false;
	}
	var param = $('#add').find(':input').serialize();
	var callback = function(msg) {
		if (window.opener.oTableArr[0] != null)
			window.opener.oTableArr[0].fnDraw();
	};
	cherryAjaxRequest({
		url : $('#RES04_save').attr('href'),
		param : param,
		callback : callback
	});
}