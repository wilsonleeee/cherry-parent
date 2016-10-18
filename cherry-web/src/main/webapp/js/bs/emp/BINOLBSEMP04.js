/*
 * 全局变量定义
 */
var BINOLBSEMP04_global = {};
//是否刷新父页面
BINOLBSEMP04_global.doRefresh = false;
//是否需要解锁
BINOLBSEMP04_global.needUnlock = true;

window.onbeforeunload = function(){
	if (BINOLBSEMP04_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (BINOLBSEMP04_global.doRefresh) {
				var url = $('#search_url', window.opener.document).val();
				// 刷新父页面
				window.opener.search(url);
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
	$('.tabs').tabs();
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
		//employeeCode: {required: true, employCodeValid:true},// 员工编号
		employeeName: {required: true},// 姓名
		 brandInfoId: {required: true},// 品牌
		  //longinName: {required: true},// 登录帐号
		    //password: {required: true},// 登录密码
  positionCategoryId: {required: true},// 岗位
			   phone: {telValid: true},// 电话
	     mobilePhone: {phoneValid: $("#mobileRule").val()},// 手机
	     	   email: {emailValid: true},// 电子邮箱
	    identityCard: {iCardValid: true},// 身份证
	     	 zipCode: {zipValid: true},// 邮编
		 	birthDay: {dateValid: true},// 生日
		   commtDate: {dateValid: true}// 入职日期
		}
	});
} );

// 删除DIV块
function delDiv(_this) {
	$(_this).parent().parent().remove();
}
// 添加DIV块
function addDiv(target,source) {
	$(target).append($(source).html());
}
// 参数序列化
function getParams(obj){
	return $(obj).find(":input").serialize();
}
// 对象JSON化
function toJSON(obj){
	var JSON = [];
	$(obj).find(':input').each(function(){
		if(this.value != '') {
			JSON.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
		}
	});
	return "{"+JSON.toString()+"}";
}
// 对象JSON数组化
function toJSONArr($obj){
	var JSONArr = [];
	$obj.each(function(){
		JSONArr.push(toJSON(this));
	});
	return "["+JSONArr.toString()+"]";
}
// 取得带token的URL
function getTokenUrl(url){
	// token
	var parentToken = getParentToken();
	return url + "?" + parentToken;
}
// 保存员工
function BSEMP04_saveEmp(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 基本信息
	var baseInfo = getParams("#b_info");
	
	// 登录帐号信息
	var loginInfo = "";
	var longinName = $("#longinName").val();
	if(longinName != "") {
		var currentUserPsd = $("#currentUserPsd").val();
	    var password = $("#passwordShow").val();
	    $("#password").val("");
	    if(password != "") {
	    	var p = {adata:'CHERRYAUTH',
			        iter:1000,
			        mode:'ccm',
			        ts:64,
			        ks:128};
		    var rp = {};
			var ct = sjcl.encrypt(currentUserPsd, password, p, rp).replace(/,/g,",\n");
			$("#password").val(ct);
	    }
		$("#b_longin").find(":input").each(function(){
			if($(this).attr("name") == "passwordShow" || $(this).attr("name") == "currentUserPsd") {
				return true;
			} else {
				loginInfo = loginInfo == "" ? $(this).serialize() : loginInfo + "&" + $(this).serialize();
			}
		});
	}
	
	// 上司
	var higher = getParams("#b_higher");
	// 关注用户
	var likeEmployee = getParams("#b_likeEmployee");
	// 管辖部门
	var followDepart = "followDepart=" + toJSONArr($('#b_followDepart').find('tbody').find('tr'));
	// 关注部门
	var likeDepart = "likeDepart=" + toJSONArr($('#b_likeDepart').find('tbody').find('tr'));
	// 其他信息
	var moreInfo = getParams("#b_moreInfo");
	// 地址信息
	var addressInfo = "addressInfo=" + toJSONArr($('#b_address').find('table'));
	// 入离职信息
	var quitInfo = getParams("#b_quit");
	var param = baseInfo + "&" + moreInfo + "&" + addressInfo + "&" + quitInfo;
	if(loginInfo != "") {
		param += "&" + loginInfo;
	}
	if(higher != "") {
		param += "&" + higher;
	}
	if(likeEmployee != "") {
		param += "&" + likeEmployee;
	}
	if(followDepart != "") {
		param += "&" + followDepart;
	}
	if(likeDepart != "") {
		param += "&" + likeDepart;
	}
//	var higherEmp = $('#b_higher :input[name=higher]').val();
//	var oldHigherEmp = $('#b_info :input[name=oldHigher]').val();
	var employeeId = $('#b_info :input[name=employeeId]').val();
	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: function(msg) {
			if($('#actionResultBody').length > 0) {
				if(refreshPrivilegeUrl) {
					window.opener.cherryAjaxRequest({
						url: refreshPrivilegeUrl,
						param: null,
						reloadFlg : true,
						callback: function(msg) {
						}
					});
				}
				if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
				if(window.opener.document.getElementById('employeeTree')) {
					window.opener.bsemp01_refreshNode();
					window.opener.bsemp01_employeeDetail(employeeId);
//					if(employeeId) {
//						window.opener.bsemp01_employeeDetail(employeeId);
//						window.opener.bsemp01_refreshNode(higherEmp);
//						if(oldHigherEmp != higherEmp) {
//							window.opener.bsemp01_refreshNode(oldHigherEmp);
//						}
//					} else {
//						window.opener.bsemp01_refreshNode(higherEmp);
//					}
				}
			}
		},
		coverId: "#saveButton"
	});
}

// 由品牌取得部门联动
function BSEMP04_getDepart(obj,url,select_default){
    
	
	var $depart = $("#b_info").find(":input[name='departId']");
	$depart.empty();
	$depart.append('<option value="">'+select_default+'</option>');
	var $positionCategory = $("#b_info").find(":input[name='positionCategoryId']");
	$positionCategory.empty();
	$positionCategory.append('<option value="">'+select_default+'</option>');
	// 清空上司
	$('#b_higher').find('#higherResult').empty();
	// 清空关注用户
	$('#b_likeEmployee').find('tbody').empty();
	// 清空管辖部门
	$('#b_followDepart').find('tbody').empty();
	// 清空关注部门
	$('#b_likeDepart').find('tbody').empty();
	// 清空用户代号
	$('#employeeCode').val('');
    var callback = function(msg){
		var jsons = eval('('+msg+')');
		
		if(jsons.orgList.length > 0) {
			for(var i in jsons.orgList) {
				$depart.append('<option value="'+jsons.orgList[i].departId+'">'+jsons.orgList[i].departName.escapeHTML()+'</option>');
			}
		}
		
		if(jsons.positionCategoryList.length > 0) {
			for(var i in jsons.positionCategoryList) {
				$positionCategory.append('<option value="'+jsons.positionCategoryList[i].positionCategoryId+'">'+jsons.positionCategoryList[i].categoryName.escapeHTML()+'</option>');
			}
		}
		
		if(jsons.employeeId) {
			$('#employeeCode').val(jsons.employeeId);
		}
		
	};
	cherryAjaxRequest({
		url: url,
		param: $(obj).serialize(),
		callback: callback
	});
}

//由部门取得岗位联动
function BSEMP04_getPost(obj,url,i18nText){
	$obj = $(obj);
	$post = $obj.parent().find(":input[name='positionId']");
	url = getTokenUrl(url) + "&organizationId=" + $obj.val();
	// 岗位初始化
	$post.empty();
	$post.append("<option  value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "positionId", "positionName", $post);
}
//由省份取得城市联动
function BSEMP04_getCity(obj,url,i18nText){
	$obj = $(obj);
	$city = $(obj).parent().parent().find(":input[name='cityId']");
	url = getTokenUrl(url) + "&regionId=" + $obj.val();
	// 城市初始化
	$city.empty();
	$city.append("<option  value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "regionId", "regionName", $city);
}
// 显示或隐藏更多信息
function bsemp04_more(object) {
	if($('#empMore').is(':hidden')) {
		$('#empMore').show();
		$(object).find(".button-text").html($("#hideMoreInfo").html());
		$(object).find(".ui-icon").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-n");
	} else {
		$('#empMore').hide();
		$(object).find(".button-text").html($("#showMoreInfo").html());
		$(object).find(".ui-icon").removeClass("ui-icon-triangle-1-n").addClass("ui-icon-triangle-1-s");
	}
}
// 登录帐号为空时，默认设置姓名为登录帐号
function bsemp04_setLonginName(object) {
	if($(object).val() != "") {
		if($("#longinName").val() == "") {
			$("#longinName").val($(object).val());
		}
	}
}
// 添加营业员时某些模块的隐藏
function bsemp04_showOrHide(obj){
	$this = $(obj);
	var positionCategoryId = $this.find("option:selected").val();
	var url = $("#getPositionCategoryInfoUrl").val();
	var param = "positionCategoryId="+positionCategoryId;
	var callback = function(msg){
		var categoryInfo = eval('('+msg+')');
		if(categoryInfo.categoryCode=='01'){
			$("#emp_flag").val("1");
			$("#highlight").hide();
		}else{
			$("#emp_flag").val("0");
			$("#highlight").show();
		}
	};
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callback
	});
	$("#departId").val("");
	$("#showRelDepartName").html("");
}

function bsemp04_popDepart(obj,param){
	var $this = $(obj);
	//取得所有部门类型
 	var departType = "";
// 	if($("#emp_flag").val()=="1"){
// 		departType = "&departType=4&gradeFlag=1";
//	}else{
//		for(var i=0;i<$("#departTypePop option").length;i++){
//	 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
//	 		departType += "&departType="+departTypeValue;
//	 	}
//	}
 	for(var i=0;i<$("#departTypePop option").length;i++){
 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
 		departType += "&departType="+departTypeValue;
 	}
 	param = param + "&privilegeFlg=1&businessType=1&brandInfoId=" + $("#brandInfoId").val()+ departType;
	var that = this;
	var callback = function(){
		var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
		var departId = "";
		var departName = "";
		if(checkedRadio){
			departId = checkedRadio.val();
    		departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
		}
		$("#departId").val(departId);
		$("#showRelDepartName").html(departName.unEscapeHTML());
	};
	var option={
			checkType:"radio",
			brandInfoId:$("#brandInfoId option:selected").val(),
			param:param,
			click:callback
	};
	popAjaxDepDialog(option);
}


function bsemp04_selectPos(obj){
	$this = $(obj);
	var positionCategoryId = $this.val();
	var categoryCode = "";
	$("#positionCategoryTemp").find("option").each(function(){
		if(positionCategoryId == $(this).val()) {
			categoryCode = $(this).text();
			return false;
		}
	});
	if(categoryCode == "02") {
		$("#creatOrgFlagSpan").show();
	} else {
		$("#creatOrgFlagSpan").hide();
		$("#creatOrgFlag0").attr("checked",false);
		$("#creatOrgFlag1").attr("checked",false);
		$("#selectDepartButton").show();
	}
	if(categoryCode == "01") {
		$("#emp_flag").val("1");
		$("#highlight").hide();
		$("#departId").val("");
		$("#showRelDepartName").html("");
	} else {
		$("#emp_flag").val("0");
		$("#highlight").show();
	}
}

function bsemp04_selectCreatOrgFlag(obj){
	if(obj.value == "0"){
		$("#creatOrgFlag1").removeAttr("checked");
	}else if(obj.value == "1"){
		$("#creatOrgFlag0").removeAttr("checked");
	}
	
	if(obj.checked) {
		$("#selectDepartButton").hide();
		$("#departId").val("");
		$("#showRelDepartName").html("");
	} else {
		$("#selectDepartButton").show();
	}
}
