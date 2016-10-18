
// 取得不同品牌的基本配置信息
function plscf_changeBrand(url,object) {
	
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
function plscf_saveBsCf(url) {
	
	var param = $('#mainForm').serialize();
	var callback = function(msg){
		$("#mainForm input").attr("disabled",true);
		$("#mainForm select").attr("disabled",true);
		$("#save").hide();
		$("#edit").show();
	};
	
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
	
	$("#csrftoken").attr("disabled",false);
}

function plscf_edit(obj){
	var $this = $(obj);
	$("#mainForm input").attr("disabled",false);
	$("#mainForm select").attr("disabled",false);
	$this.parent().hide();
	$("#save").show();
}

function plscf_back(obj){
	var $this = $(obj);
	$this.parent().hide();
	$("#edit").show();
	$("#mainForm input").attr("disabled",true).css("color","black");
	$("#mainForm select").each(function(i){
		if(i >= 0){
			$(this).attr("disabled",true).css("color","black");
		}
	});
	
	$("#csrftoken").attr("disabled",false);
}

$(document).ready(function(){
	$("#mainForm input").attr("disabled",true).css("color","black");
	$("#mainForm select").each(function(i){
		if(i >= 0){
			$(this).attr("disabled",true).css("color","black");
		}
	});
	$("#csrftoken").attr("disabled",false);
});
