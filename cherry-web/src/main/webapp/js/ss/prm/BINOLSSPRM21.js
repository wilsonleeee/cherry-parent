
/**
 * 输入了盘点数量
 * @param thisObj
 */
function changeCount(thisObj,index){
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
function chooseDepart(thisObj){
	SSPRM21_deleteActionMsg();
	var organizationid = $('#organizationId').val();
	var param ="organizationid="+organizationid;
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,param,changeDepotDropDownList);	
	
}

/**
 * 修改仓库下拉框
 * @param data
 * @return
 */
function changeDepotDropDownList(data){	
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
	ajaxRequest(url,param,changeLogicDepotDropDownList);
}

/**
 * 修改逻辑仓库下拉框
 * @param data
 * @return
 */
function changeLogicDepotDropDownList(data){
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
function choosePrimaryCategory(){
	SSPRM21_deleteActionMsg();
	var organizationid = $('#organizationId').val();
	var primaryCategory =$('#largeCategory').val();;
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getMCategoryAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,{'organizationid':organizationid,'largeCategory':primaryCategory},changeMAndSCategory);	
}

/**
 * 修改大分类下拉框后，中，小下拉框联动
 * @param data
 * @return
 */
function changeMAndSCategory(data){	
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
function chooseSecondCategory(){
	SSPRM21_deleteActionMsg();
	var organizationid = $('#organizationId').val();
	var primary =$('#largeCategory').val();
	var second =$('#middleCategory').val();
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getSCategoryAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,{'organizationid':organizationid,'largeCategory':primary,'middleCategory':second},changeSCategory);	
}
/**
 * 修改中分类下拉框后，小下拉框联动
 * @param data
 * @return
 */
function changeSCategory(data){
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
function startStocktaking(){
	SSPRM21_deleteActionMsg();
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
	ajaxRequest(argUrl,params,startStocktakingResult);		
	}
}

function startStocktakingResult(msg){
	SSPRM21_deleteActionMsg();
	$('#ajaxerrdiv').empty();
	$('#mydetail').empty();
	submitflag =true;
	if(msg.indexOf("hidden1Result1Div1")!=-1){
		//正常执行的话返回的就是详细的jsp页面
		$('#mydetail').html(msg);
	}else{
		$('#actionResultDisplay').html(msg);
		cancelStocktaking();
	}
}

/**
 * 取消盘点
 */
function cancelStocktaking(){
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
 * 点击确定按钮
 */
function submitFormSave(){
	if(SSPRM21_checkData()){
		cherryAjaxRequest({
			url:$("#s_saveURL").html(),
			param:$('#mainForm').formSerialize(),
			callback:submitSuccess
		});
	}
}

/**
 * 提交成功的回调函数
 * @param msg
 */
function submitSuccess(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#mydetail").html("");
		 cancelStocktaking();
	}
}

/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function SSPRM21_deleteActionMsg(){
	$('#actionResultDisplay').html("");
	$("#errorDiv2 #errorSpan2").html("");
	$("#errorDiv2").hide();
	$("#errorMessage").empty();
}

function SSPRM21_changeOddEvenColor1(){
	$("#databody tr:odd").attr("class",function(){
		if($(this).attr("class")=="errTRbgColor"){
			
		}else{
			$(this).addClass("odd");
		}
		
	});
	$("#databody tr:even").attr("class",function(){
		if($(this).attr("class")=="errTRbgColor"){
			
		}else{
			$(this).addClass("even");
		}
	});
}

function SSPRM21_checkData(){
	var flag = true;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		if($(this).find("[name='quantityuArr']").val()==""){
			flag = false;
			$(this).attr("class","errTRbgColor");				
		}else{
			$(this).removeClass('errTRbgColor');				
		}
	});
	SSPRM21_changeOddEvenColor1();
	if(!flag){
		//有空行存在		
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00007').val());
		$('#errorDiv2').show();
		return flag;
	}else{
		$('#errorDiv2').hide();
	}
	return flag;
}