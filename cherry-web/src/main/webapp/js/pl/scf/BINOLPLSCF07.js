/*
 * 全局变量定义
 */
var plscf07_global = {};
//是否刷新父页面
plscf07_global.doRefresh = false;
//是否需要解锁
plscf07_global.needUnlock = true;

window.onbeforeunload = function(){
	if (plscf07_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (plscf07_global.doRefresh) {
				// 刷新父页面
				window.opener.plscf06_searchCode();
			}
		}
	}
};
function doClose(){
	window.close();
}
$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
			codeType: {required: true, alphanumeric:true}// code类别
		}
	});
} );

// 参数序列化
function getParams(obj){
	return $(obj).find(":input").serialize();
}
//保存code管理类别
function plscf07_saveCodeM(url){
	$("#errorSpan").html("");
	$("#errorDiv").hide();
	
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return false;
	}
	
	var vailRet = vailtData();
	if(vailRet == "error1"){
		$("#errorSpan").html($("#error1").html());
		$("#errorDiv").show();
		return false;
	}else if(vailRet == "error2"){
		$("#errorSpan").html($("#error2").html());
		$("#errorDiv").show();
		return false;
	}
	// 基本信息
	var param = getParams("#base_info")+"&"+getParams("#addListInfo");
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			//是否刷新父页面
			plscf07_global.doRefresh = true;
		},
		coverId: "#saveButton"
	});
}

function vailtData(){
	var tempData = {};
	var flag = "";
	var tr = $("#editListTable").find("tr")
	tr.each(function(i){
		if( i > 0 ){
			var codeKey = $(this).find("#codeKeyArr").val();
			if($.trim(codeKey) == ""){
				$(this).prop("class","errTRbgColor");
				flag = "error1";
			}else if(tempData[$.trim(codeKey)]){
				$(this).prop("class","errTRbgColor");
				$(tr[tempData[$.trim(codeKey)]]).prop("class","errTRbgColor");
				flag = "error2";
			}else{
				tempData[$.trim(codeKey)] = i;
				$(this).removeProp("class");
			}
		}
	});
	return flag;
}

//根据组织查询品牌List
function plscf07_searchBrand(url,object,defaultSelect) {
	
	var $select = $('#brandInfoId');
	$select.empty();
	//$select.append('<option value="">'+defaultSelect+'</option>');
	var param = $(object).serialize();
	param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
	var callback = function(msg) {
		if(msg) {
			var json = eval('('+msg+')');
			for(var i in json) {
				$select.append('<option value="'+json[i].brandCode+'">'+json[i].brandName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

function scf07AddNewCode(){
	var tableNode = document.getElementById("editListTable");
	var _parentNode = tableNode.childNodes;
	var editTbody,editTr;
	for(var i in _parentNode){
		if(_parentNode[i].nodeType==1){
			editTbody = _parentNode[i];
			break;
		}
	}
	$(editTbody).append($("#addNewCode tbody").html());
	$("span[name=codyType_span]").html($("#codeType").val());
}

function changeCodeType(){
	$("span[name=codyType_span]").html($("#codeType").val());
}

function scf07Delete(obj){
	var _this = obj;
	var trNode = _this.parentNode.parentNode;
	$(trNode).remove();
}

function scf07IsInteger(obj){
	var $this = $(obj);
	var patrn=/^[1-9]{1}[0-9]{0,7}$/; 
	if (!patrn.exec($this.val())){
		$this.val("");
	}
}