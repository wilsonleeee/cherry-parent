/*
 * 全局变量定义
 */
var BINOLBSEMP03_global = {};
//是否刷新父页面
BINOLBSEMP03_global.doRefresh = false;
//是否需要解锁
BINOLBSEMP03_global.needUnlock = true;

window.onbeforeunload = function(){
	if (BINOLBSEMP03_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
			if (BINOLBSEMP03_global.doRefresh) {
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
// 入退职日期控件绑定
function bindQuitDate($object,holidays){
	$object.find(':input[name="commtDate"]').cherryDate({
		holidayObj: holidays, 
		yearRange:'c-30:c',
		beforeShow: function(input){
			var value = $object.prev().find(':input[name="depDate"]').val();
			return [value,'minDate'];
		}
	});
	
	$object.find(':input[name="depDate"]').cherryDate({
		holidayObj: holidays, 
		yearRange:'c-1:c',
		beforeShow: function(input){
			var value = $object.find(':input[name="commtDate"]').val();
			return [value,'minDate'];
		}
	});
}
//生日日期控件绑定
function bindBirthDay(holidays){
	$('#birthDay').cherryDate({holidayObj: holidays, yearRange:'c-60:c'});
}

//返回
function BSEMP03_doBack(){
	var tokenVal = parentTokenVal();
	$("#parentCsrftoken").val(tokenVal);
	BINOLBSEMP03_global.needUnlock = false;
	$("#toDetailForm").submit();
}
// 初始化
$(document).ready(function() {
	if (window.opener) {
	  window.opener.lockParentWindow();
	}
	// 省份对象
	var $province = $("#b_address table.detail").find(":input[name='provinceId']");
	// 城市取得URL
	var getCityUrl = $("#getCity_url").val();
	var i18nText_selt = $("#global_select").val();
	$province.each(function(){
		// AJAX取得城市List
		BSEMP03_getCity(this,getCityUrl,i18nText_selt);
	});
	// 部门对象
	var $depart = $("#dpt_post").find(":input[name='departId']");
	var getPostUrl = $("#getPost_url").val();
	var i18nText_post = $("#employee_post").val();
	$depart.each(function(){
		// AJAX取得城市List
		BSEMP03_getPost(this,getPostUrl,i18nText_post);
	});
	// 日期控件绑定
	var holidays = $("#holidays").val();
	//生日日期控件绑定
	bindBirthDay(holidays);
	$("#quitInfo").find("table").each(function(){
		// 入退职日期控件绑定
		bindQuitDate($(this),holidays);
	});
	
	// 表单验证配置
	cherryValidate({						
		formId: 'mainForm',
		rules: {
		employeeCode: {required: true, employCodeValid:true},// 员工编号
		employeeName: {required: true},// 姓名
		  //longinName: {required: true},// 登录帐号
  positionCategoryId: {required: true},// 岗位
			   phone: {telValid: true},// 电话
	     mobilePhone: {phoneValid: $("#mobileRule").val()},// 手机
	    identityCard: {iCardValid: true},// 身份证
	     	   email: {emailValid: true},// 电子邮箱
	     	 zipCode: {zipValid: true},// 邮编
		 	birthDay: {dateValid: true},// 生日
		   commtDate: {dateValid: true},// 入职日期
		     depDate: {dateValid: true}// 离职日期
		}
	});
	
	bsemp03_selectPos($("#positionCategoryId")[0]);
} );
//删除DIV块
function BSEMP03_delDiv(_this, inputId) {
	var $obj = $(_this).parent().parent().parent();
	var $obj0=$obj.parent();
	var radio = $obj.find(":radio[checked]");
	var $inputId = $obj.find("#"+inputId);
	if($inputId.length == 0){
		$obj.remove();
	}else{
		$obj.empty();
		$obj.addClass("hide");
		var hidden = '<input type="hidden" name="' + inputId + '" value="' + $inputId.val() + '"/>'
					+'<input type="hidden" name="option" value="0"/>';
		$obj.html(hidden);
	}
	if(radio.length == 1){
		$obj0.find(":radio").first().prop("checked",true);
	}
	// 查找target下隐藏的删除按钮
	var delButton = $obj0.find("span.button-del");
	if(delButton.length == 1){
		delButton.addClass("hide");
	}
}
// 添加DIV块
function BSEMP03_addDiv(target,source,bindFlag) {
	var $target= $(target);
	// 查找target下隐藏的删除按钮
	var delButton = $target.find("span.hide");
	if(delButton.length != 0){
		delButton.removeClass("hide");
	}
	$target.append($(source).html());
	if(bindFlag){
		// 入退职日期控件绑定
		bindQuitDate($target.find("table:last-child"),$("#holidays").val());
	}
}

// 参数序列化
function BSEMP03_getParams(obj){
	return $(obj).find(":input").serialize();
}
// 对象JSON化
function BSEMP03_toJSON(obj){
	var JSON = [];
	$(obj).find(':input').each(function(){
		if($(this).is(":radio")){
			if($(this).prop("checked")){
				this.value = '1';
			}else{
				this.value = '0';
			}		
		}
		if(this.value != '') {
			JSON.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
		}
	});
	return "{"+JSON.toString()+"}";
}
// 对象JSON数组化
function BSEMP03_toJSONArr(obj){
	var JSONArr = [];
	$(obj).each(function(){
		JSONArr.push(BSEMP03_toJSON(this));
	});
	return "["+JSONArr.toString()+"]";
}

//取得带token的URL
function BSEMP03_getTokenUrl(url){
	// token
	var parentToken = getParentToken();
	return url + "?" + parentToken;
}
//由品牌取得部门联动
function BSEMP03_getDepart(obj,url,i18nText1,i18nText2){
	$obj = $(obj);
	$depart = $("#dpt_post,#h_post").find(":input[name='departId']");
	$post = $("#dpt_post,#h_post").find(":input[name='positionId']");
	var param = $obj.serialize();
	url = BSEMP03_getTokenUrl(url) + "&" + param;
	// 部门、岗位初始化
	$depart.empty();
	$post.empty();
	$depart.append("<option  value=''>"+i18nText1+"</option>");
	$post.append("<option  value=''>"+i18nText2+"</option>");
	// AJAX请求
    doAjax2(url, "departId", "departName", $depart);
}
//由部门取得岗位联动
function BSEMP03_getPost(obj,url,i18nText){
	$obj = $(obj);
	$post = $obj.parent().find(":input[name='positionId']");
	var positionId = $post.val();
	url = BSEMP03_getTokenUrl(url) + "&organizationId=" + $obj.val();
	// 岗位初始化
	$post.empty();
	$post.append("<option  value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "positionId", "positionName", $post, '', positionId);
}
//由省份取得城市联动
function BSEMP03_getCity(obj,url,i18nText){
	var $obj = $(obj);
	var $city = $(obj).parent().parent().find(":input[name='cityId']");
	var cityId = $city.val();
	var url = BSEMP03_getTokenUrl(url) + "&regionId=" + $obj.val();
	// 城市初始化
	$city.empty();
	$city.append("<option  value=''>"+i18nText+"</option>");
	// AJAX请求
    doAjax2(url, "regionId", "regionName", $city, '' ,cityId);
}
//保存员工
function BSEMP03_saveEmp(url){
	var $form = $("#mainForm");
	// 表单验证
	if(!$form.valid()){
		return;
	}
	// 基本信息
	var baseInfo = BSEMP03_getParams("#b_info");
	
	
	// 登录帐号信息
	var loginInfo = "";
	var userId = $("#userId").val();
	if(userId != "") {
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
	} else {
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
	}
	
	// 上司
	var higher = BSEMP03_getParams("#b_higher");
	// 关注用户
	var likeEmployee = BSEMP03_getParams("#b_likeEmployee");
	// 管辖部门
	var followDepart = "followDepart=" + BSEMP03_toJSONArr($('#b_followDepart').find('tbody').find('tr'));
	// 关注部门
	var likeDepart = "likeDepart=" + BSEMP03_toJSONArr($('#b_likeDepart').find('tbody').find('tr'));
	// 其他信息
	var moreInfo = BSEMP03_getParams("#b_moreInfo");
	// 地址信息
	var addressInfo = "addressInfo=" + BSEMP03_toJSONArr($('#b_address').find('table'));
	// 入离职信息
	var quitInfo = "quitInfo=" + BSEMP03_toJSONArr("#quitInfo>table");
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

//显示或隐藏更多信息
function bsemp03_more(object) {
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
//登录帐号为空时，默认设置姓名为登录帐号
function bsemp03_setLonginName(object) {
	if($(object).val() != "") {
		if($("#longinName").val() == "") {
			$("#longinName").val($(object).val());
		}
	}
}
//添加营业员时某些模块的隐藏
function bsemp03_showOrHide(obj){
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

function bsemp03_popDepart(obj,param){
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
 	param = param + "&privilegeFlg=1&businessType=1" + departType;
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
			brandInfoId:$("#brandInfoId").val(),
			param:param,
			click:callback
	};
	popAjaxDepDialog(option);
}

function bsemp03_selectPos(obj){
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
		//编辑初始化时画面会清掉部门信息，若不重新选择部门，部门会被清空
//		$("#departId").val("");
//		$("#showRelDepartName").html("");
	} else {
		$("#emp_flag").val("0");
		$("#highlight").show();
	}
}

function bsemp03_selectCreatOrgFlag(obj){
	
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