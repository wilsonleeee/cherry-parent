
// 取得不同品牌的基本配置信息
function plscf_changeBrand(url,object) {
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	var param = $(object).serialize();
	param += (param ? "&":"") + "csrftoken=" + getTokenVal();
	var callback = function(msg){
		$('#actionResultDisplay').empty();
		$('#baseConfInfo').html(msg);
	};
	
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// 保存基本配置信息
function plscf_saveBsCf(url,obj) {
	var $this = $(obj);
	var $tdObj = $this.parent().parent().parent();
	var param = $("#mainForm").find('#brandInfoId').serialize() + "&configCode="
			+ $tdObj.find("#configCode").val() + "&"
			+ $tdObj.find("#configValue").serialize() + "&type="
			+ $tdObj.find("#type").val();
	var callback = function(msg){
		$tdObj.find("input").attr("disabled",true);
		$tdObj.find("select").attr("disabled",true);
		$tdObj.find("#save").hide();
		$tdObj.find("#edit").show();
	};
	
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
	
	$("#csrftoken").attr("disabled",false);
}

function plscf_edit(obj){
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	var $this = $(obj);
	var $tdObj = $this.parent().parent().parent();
	$tdObj.find("input").attr("disabled",false);
	$tdObj.find("select").attr("disabled",false);
	$this.parent().hide();
	$tdObj.find("#save").show();
}

function plscf_back(obj){
	$("#errorMessage").empty();
	$("#actionResultDisplay").empty();
	var $this = $(obj);
	var $tdObj = $this.parent().parent().parent();
	$this.parent().hide();
	$tdObj.find("#edit").show();
	$tdObj.find("input").attr("disabled",true).css("color","black");
	$tdObj.find("select").each(function(i){
		if(i >= 0){
			$(this).attr("disabled",true).css("color","black");
		}
	});
	
	$("#csrftoken").attr("disabled",false);
}

function getSystemConfigHelp(obj) {
	var $this = $(obj);
	var configCode = $this.parent().find("#configCode").val();
	var title = $this.parent().prev().first().text();
	//系统配置项code是从1000开始的
	var index = parseInt(configCode) - 1000;
	var text = "";
	if(BINOLPLSCF01_js_i18n_configHelp[index] != undefined){
		text = configHelpHead + BINOLPLSCF01_js_i18n_configHelp[index] + configHelpFoot;
	}else{
		//若未为配置项设置描述则以标题代替
		text = configHelpHead + title + configHelpFoot;
	}
	var dialogSetting = {
		dialogInit : "#configHelpDialogInit",
		bgiframe : true,
		width : 400,
		height : 250,
		text : text,
		title : title,
		resizable : true
	};
	openDialog(dialogSetting);
}

$(document).ready(function(){
	$("#mainForm input").attr("disabled",true).css("color","black");
	$("#mainForm select").each(function(i){
		if(i >= 0){
			$(this).attr("disabled",true).css("color","black");
		}
	});
	// 品牌始终处于可编辑状态【主要用于查看不同品牌的配置信息】
	$("#mainForm").find("#brandInfoId").attr("disabled",false);
	$("#csrftoken").attr("disabled",false);
});
