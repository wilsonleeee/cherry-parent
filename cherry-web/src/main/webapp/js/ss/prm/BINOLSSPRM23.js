
/**
 * 输入了盘点数量
 * @param thisObj
 */
function SSPRM23_changeCount(thisObj,index){
	var count = parseInt($(thisObj).val(),10);
	var allowNegativeFlag = $("#allowNegativeFlag").val();
	if(allowNegativeFlag != "1"){
		count = Math.abs(count);
	}
	if(isNaN(count)){		
		$(thisObj).val("");
		//清空盘差和金额差额
		$('#count'+index ).html("");
		$('#gainCount'+index ).val("");
		$('#money'+index ).html("");
		$('#gainMoney'+index ).val("");
		return;
	}	
	$(thisObj).val(count);
	var tempCount = count-$('#bookCount'+index).val();
	var money = tempCount*$('#priceUnit'+index).val();
	
	if(tempCount<0){
		$('#count'+index ).addClass("errTRbgColor");
		
	}else{
		$('#count'+index ).removeClass("errTRbgColor");		
	}
	if(money<0){		
		$('#money'+index ).addClass("errTRbgColor");
	}else{		
		$('#money'+index ).removeClass("errTRbgColor");
	}
	$('#count'+index ).html(tempCount);
	$('#gainCount'+index ).val(tempCount);
	$('#money'+index ).html(money.toFixed(2));
	$('#gainMoney'+index ).val(money.toFixed(2));
}

/**
 * 更改了部门
 * @param thisObj
 */
function SSPRM23_chooseDepart(thisObj){
	SSPRM23_deleteActionMsg();
	var organizationid = $('#organizationId').val();
	var param ="organizationid="+organizationid;
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,param,SSPRM23_changeDepotDropDownList);	
	
}

/**
 * 修改仓库下拉框
 * @param data
 * @return
 */
function SSPRM23_changeDepotDropDownList(data){	
	//data为json格式的文本字符串	
	var member = eval("("+data+")");    //包数据解析为json 格式  
	$("#depot ").empty();
	$.each(member, function(i){
		$("#depot ").append("<option value='"+ member[i].BIN_InventoryInfoID+"'>"+escapeHTMLHandle(member[i].InventoryName)+"</option>"); 
		});
	
	//修改逻辑仓库
	var organizationid = $('#organizationId').val();
	var param ="organizationid="+organizationid;
	var url = $('#s_getLogicDepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,param,SSPRM23_changeLogicDepotDropDownList);	
}

/**
 * 修改逻辑仓库下拉框
 * @param data
 * @return
 */
function SSPRM23_changeLogicDepotDropDownList(data){
	//data为json格式的文本字符串	
	var member = eval("("+data+")");    //包数据解析为json 格式  
	$("#logicDepot ").empty();
	if (null == member){
		$("#pLogicDepot").hide();
	}else{
		$.each(member, function(i){
			$("#logicDepot ").append("<option value='"+ member[i].BIN_LogicInventoryInfoID+"'>"+escapeHTMLHandle(member[i].LogicInventoryCodeName)+"</option>"); 
		});
		//没有逻辑仓库不显示
		if(member.length>0){
			$("#pLogicDepot").show();
		}else{
			$("#pLogicDepot").hide();
		}
	}
}

/**
 * 修改大分类下拉框
 * @param data
 * @return
 */
function SSPRM23_choosePrimaryCategory(){
	SSPRM23_deleteActionMsg();
	var organizationid = $('#organizationId').val();
	var primaryCategory =$('#largeCategory').val();;
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getMCategoryAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,{'organizationid':organizationid,'largeCategory':primaryCategory},SSPRM23_changeMAndSCategory);	
}

/**
 * 修改大分类下拉框后，中，小下拉框联动
 * @param data
 * @return
 */
function SSPRM23_changeMAndSCategory(data){	
	var member = eval("("+data+")");    //包数据解析为json 格式  
	$("#middleCategory option:not(:first)").remove();
	$.each(member, function(i){
		$("#middleCategory").append("<option value='"+ member[i].SecondryCategoryCode+"'>"+escapeHTMLHandle(member[i].SecondCategoryName)+"</option>"); 
	});
	
	$("#smallCategory option:not(:first)").remove();
}

/**
 * 修改中分类下拉框
 * @param data
 * @return
 */
function SSPRM23_chooseSecondCategory(){
	SSPRM23_deleteActionMsg();
	var organizationid = $('#organizationId').val();
	var primary =$('#largeCategory').val();
	var second =$('#middleCategory').val();
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getSCategoryAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,{'organizationid':organizationid,'largeCategory':primary,'middleCategory':second},SSPRM23_changeSCategory);	
}
/**
 * 修改中分类下拉框后，小下拉框联动
 * @param data
 * @return
 */
function SSPRM23_changeSCategory(data){
	var member = eval("("+data+")");    //包数据解析为json 格式  
	$("#smallCategory option:not(:first)").remove();
	$.each(member, function(i){
		$("#smallCategory").append("<option value='"+ member[i].SmallCategoryCode+"'>"+escapeHTMLHandle(member[i].SmallCategoryName)+"</option>"); 
	});	
}

/**
 * 开始盘点
 * @return
 */
function SSPRM23_startStocktaking(){
	SSPRM23_deleteActionMsg();
	
	if($('#organizationId').val()==null){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if($('#depot').val()==null){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00042').val());
		$('#errorDiv2').show();
		return false;
	}
	//盘点理由字符长度后端验证
	var reasonAllLength=$('#reasonAll').val().length;
	if(reasonAllLength > 100){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00061').val());
		$('#errorDiv2').show();
		return false;
	}
	$('#hidDepot').val($('#depot').val());
	$('#hidLogicDepot').val($('#logicDepot').val());
	var params =$('#mainForm').formSerialize();
	$('#organizationId').attr('disabled',true);
	$('#depot').attr('disabled',true);
	$('#logicDepot').attr('disabled',true);
	$('#validFlag').attr('disabled',true);
	$('#largeCategory').attr('disabled',true);
	$('#middleCategory').attr('disabled',true);
	$('#smallCategory').attr('disabled',true);
	$('#spanBtnStart').hide();
	$('#spanBtnCancel').show();	
	
	if(submitflag){
	submitflag=false;
	var argUrl = $('#s_startStocktaking').html();	
	ajaxRequest(argUrl,params,SSPRM23_startStocktakingResult);		
	}
}

function SSPRM23_startStocktakingResult(msg){	
	//$('#mydetail').empty();
	submitflag =true;
	if(msg.indexOf("hidden1Result1Div1")!=-1){
		//正常执行的话返回的就是详细的jsp页面
		$('#mydetail').html(msg);
	}else{		
		$('#actionResultDisplay').html(msg);
		SSPRM23_cancelStocktaking();
		//$("#genDiv").before(msg);
	}
}
/**
 * 取消盘点
 */
function SSPRM23_cancelStocktaking(){
	//退出盘点时不再维持session
	window.clearInterval(refreshSessionTimerID);
	refreshSessionTimerID = null;
	
	$('#organizationId').attr('disabled',false);
	$('#depot').attr('disabled',false);
	$('#logicDepot').attr('disabled',false);
	$('#validFlag').attr('disabled',false);
	$('#largeCategory').attr('disabled',false);
	$('#middleCategory').attr('disabled',false);
	$('#smallCategory').attr('disabled',false);
	$('#spanBtnStart').show();
	$('#spanBtnCancel').hide();
	$('#mydetail').empty();
}
/**
 * 验证是否有未输入的数量
 * @return
 */
function SSPRM23_checkNull(){
	var flag = true;
	var length = $('#databody').find("[name='quantityuArr']").length;
	for(var i=0;i<length;i++){
		var curValue = $($('#databody').find("[name='quantityuArr']")[i]).val();
		if(curValue == ""){
			flag = false;
			break;
		}
	}
	return flag;
}
/**
 * 反映到库存
 * @return
 */
function SSPRM23_saveToDB(){
	cherryAjaxRequest({
		url:$("#s_saveURL").html(),
		param:$('#mainForm').formSerialize(),
		callback:SSPRM23_submitSuccess
	});
}
/**
 * 点击确定按钮
 */
function SSPRM23_submitFormSave(){
	//如果画面上有未输入数量的明细行,弹出提示框提示
	if(!SSPRM23_checkNull()){
		var dialogSetting = {
				dialogInit: "#dialogInit",	
				width: 	300,
				height: 180,
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){SSPRM23_saveToDB();removeDialog("#dialogInit");},
				cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
		$("#dialogInit").html($("#errmsgESS00049").val());
	}else{
		SSPRM23_saveToDB();
	}
}

/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM23_submitSuccess(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#mydetail").html("");
		//取消盘点即不再维持session
		SSPRM23_cancelStocktaking();
	}
}

/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function SSPRM23_deleteActionMsg(){
	$('#actionResultDisplay').html("");
	$("#errorDiv2 #errorSpan2").html("");
	$("#errorDiv2").hide();
	$("#errorMessage").empty();
}