var binOLSSPRM20_global = {};
//当前点击对象
binOLSSPRM20_global.thisClickObj = {};
binOLSSPRM20_global.dialogBody ="";

/**
 * 选择了岗位下拉框
 * @param thisObj
 * @return
 */
function SSPRM20_choosePosition(thisObj){
	SSPRM20_clearActionMsg();
	var param ='positionId='+$('#positionId').val();
	cherryAjaxRequest({
		url:$("#s_getOutDepart").html(),
		param:param,
		callback:SSPRM20_getOutDepartSuccess
	});
	
}

/**
 * 选择了岗位下拉框后改变调出部门下拉框
 * @param msg
 * @return
 */
function SSPRM20_getOutDepartSuccess(msg){
	//data为json格式的文本字符串	
	var member = eval("("+msg+")");    //包数据解析为json 格式  
	$("#outOrganizationId").empty();
	$.each(member, function(i){
		$("#outOrganizationId").append("<option value='"+ member[i].BIN_OrganizationID+"'>"+escapeHTMLHandle(member[i].DepartName)+"</option>"); 
	});	
	SSPRM20_chooseOutDepart();
}

/**
 * 更改了调出部门
 * @param thisObj
 */
function SSPRM20_chooseOutDepart(){
	SSPRM20_clearActionMsg();
	$("#mydetail").html("");
	var param ='positionId='+$('#positionId').val()+'&outOrganizationId='+$('#outOrganizationId').val();	
	//取得仓库
	cherryAjaxRequest({
		url:$("#s_getDepot").html(),
		param:param,
		callback:SSPRM20_getDepotSuccess
	});	
}

/**
 * 修改仓库下拉框
 * @param data
 * @return
 */
function SSPRM20_getDepotSuccess(data){	
	
	//data为json格式的文本字符串	
	var member = eval("("+data+")");    //包数据解析为json 格式  
	$("#outDepotId").empty();
	$.each(member, function(i){
		$("#outDepotId").append("<option value='"+ member[i].BIN_InventoryInfoID+"'>"+escapeHTMLHandle(member[i].InventoryName)+"</option>"); 
		});
}


function SSPRM20_openPopup(){
	SSPRM20_clearActionMsg();
	if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if($('#outDepotId').val()==null || $('#outDepotId').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00042').val());
		$('#errorDiv2').show();
		return false;
	}
	// 新页面地址
	var url = $('#s_popup').html();
	var token = $('#csrftoken').val();
	url += "?&csrftoken=" + token;
	url+= "&outOrganizationId=" +$('#outOrganizationId').val();
	popup(url,{height:500, width:650});	
}

/**
 * 弹出画面正常关闭后,主画面取得发货单详细信息
 */
function getAllocationDetail(argUrl,params){
	
	cherryAjaxRequest({
		url:argUrl,
		param:params,
		callback:SSPRM20_getDetailResult
	});	
	
}

/**
 * 取详细信息成功后就显示详细的表格，出错了就报错
 * @return
 */
function SSPRM20_getDetailResult(msg){	
	$('#ajaxerrdiv').empty();
	submitflag =true;
	if(msg.indexOf("hidden1Result1Div1")!=-1){
		//正常执行的话返回的就是详细的jsp页面
		$('#mydetail').html(msg);
	}else{
		$('#ajaxerrdiv').html(msg);
		//$("#genDiv").before(msg);
	}
}

/**
 * 输入了调拨数量
 * @param thisObj
 */
function SSPRM20_changeCount(thisObj){
	var $tr_obj = $(thisObj).parent().parent();
	var trID = $tr_obj.attr('id');
	var index = trID.substr(7);
	
	if($('#promotionProductVendorIDArr'+index ).val()==""){
		$(thisObj).val("");	
		return;
	}
	var count = Math.abs(parseInt($(thisObj).val(),10));

	if(isNaN(count)){		
		$(thisObj).val("");
		//清空盘差和金额差额
		$('#money'+index).html("0.00");
		return;
	}	
	$(thisObj).val(count);	
	var money = count*$('#priceUnitArr'+index).val();
	$('#money'+index).html(money.toFixed(2));
	$('#priceTotalArr'+index ).val(money.toFixed(2));
}



/**
 * 点击确定按钮
 */
function SSPRM20_submitFormSave(){	
	SSPRM20_clearActionMsg();
	cherryAjaxRequest({
		url:$("#s_saveURL").html(),
		param:$('#mainForm').formSerialize(),
		callback:SSPRM20_submitSuccess
	});	
}

/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM20_submitSuccess(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#mydetail").html("");
	}
}

/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function SSPRM20_clearActionMsg(){
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
	$.each($('#databody >tr'), function(i){	
		if(i>0){			
			$(this).removeClass('errTRbgColor');
		}
	});	
}
function changeOddEvenColor(){
	$("#databody tr:odd").attr("class","odd");
	$("#databody tr:even").attr("class","even");
}
function SSPRM20_clearDetailData(){
	$("#databody > tr[id!='dataRow0']").remove();
}

/**
 * 调出部门弹出框
 * @return
 */
function SSPRM20_openOutDepartBox(thisObj){
	SSPRM20_clearActionMsg();
 	changeOddEvenColor();
 	
 	var param = "checkType=radio&privilegeFlg=1&businessType=1";
	var callback = function() {
		var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
		if($selected.length > 0) {
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			$("#outOrganizationId").val(departId);
			$("#outOrgName").text("("+departCode+")"+departName);
			SSPRM20_chooseOutDepart();
		}
	};
	popDataTableOfDepart(thisObj,param,callback);
}