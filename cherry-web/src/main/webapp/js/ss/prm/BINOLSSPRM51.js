
/**
 * 输入了发货数量
 * @param thisObj
 */
function PRM51_changeCount(thisObj){	
	var count = Math.abs(parseInt($(thisObj).val(),10));
	$tr_obj = $(thisObj).parent().parent();	
	var trID = $tr_obj.attr('id');
	//var money = count*$('#'+trID +' #priceUnitArr').val();
	if(isNaN(count)){
		$(thisObj).val("0");
		//$('#'+trID +' > #dataTd8').html("0.00");
		return;
	}
	//if(isNaN(money)){		
		//$('#'+trID +' > #dataTd8').html("0.00");
	//	return;
	//}
	$(thisObj).val(count);
	//$('#'+trID +' > #dataTd8').html(money.toFixed(2));
}

/**
 * 更改了部门
 * @param thisObj
 */
function chooseDepart(thisObj){	
	var organizationid = $('#inOrganizationId').val();
	var param ="organizationid="+organizationid;
	
	//更改了部门后，取得该部门所拥有的仓库
	
	var url = $('#urlgetdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
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
	$("#inDepot ").empty();
	$.each(member, function(i){
		$("#inDepot ").append("<option value='"+ member[i].BIN_InventoryInfoID+"'>"+escapeHTMLHandle(member[i].InventoryName)+"</option>"); 
		});
}

/**
 * 打开弹出画面
 */
function openPopup1(){
	SSPRM51_deleteActionMsg();
	if($('#inOrganizationId').val()==null || $('#inOrganizationId').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if($('#inDepot').val()==null || $('#inDepot').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00042').val());
		$('#errorDiv2').show();
		return false;
	}
	
	// 新页面地址
	var url = $('#s_popup').html();
	var token = $('#csrftoken').val();
	url += "?&csrftoken=" + token;
	url+= "&inOrganizationId=" +$('#inOrganizationId').val();
	popup(url,{height:500, width:650});		
}

/**
 * 弹出画面正常关闭后,主画面取得发货单详细信息
 */
function getDeliverDetail(argUrl,params){
	if(submitflag){
	  submitflag=false;	
	  ajaxRequest(argUrl,params,getDeliverDetailResult);
	  //$('#mydetail').load(argUrl,params,function(){submitflag=true;});		
	}
}

/**
 * 取详细信息成功后就显示详细的表格，出错了就报错
 * @return
 */
function getDeliverDetailResult(msg){	
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
 * 点击确定按钮
 */
function submitForm51(){
	if(!submitflag){
		return false;
	}	
	//if(!checkData()){
	//	return false;
	//};
	var url = $("#submitURL").html();
	var params=$('#mainForm').formSerialize();
	submitflag=false;
	ajaxRequest(url,params,submitSuccess);
}

/**
 * 提交成功的回调函数
 * @param msg
 */
function submitSuccess(msg){
	$('#ajaxerrdiv').empty();	
	if(msg.indexOf("hidden1Result1Div1")!=-1){
		//正常执行的话返回的就是详细的jsp页面
		$('#div_main').html(msg);
	}else{
		$('#ajaxerrdiv').html(msg);
		//$("#genDiv").before(msg);
	}	
	submitflag =true;
}

/**
 * 提交前对数据进行检查
 * @returns {Boolean}
 */
function checkData(){
	
	var flag = true;
	var count= 0;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		if(i>0){
			count +=1;
			if($(this).find("#unicodeSpan").html()=="" ||$(this).find("#dataTd2").html()==""){
				flag = false;				
				$(this).attr('class','errTRbgColor');				
			}		
		}
	});
	
	if(!flag){
		//有空行存在		
		$('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
		$('#errorDiv2').show();
		return flag;
	}
	
	if(count==0){
		flag = false;
		$('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
		$('#errorDiv2').show();
		return flag;
	}	
	
	return flag;
}
/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function SSPRM51_deleteActionMsg(){
	$('#errorMsgDivTOP').html("");
	$('#actionMsgDivTOP').html("");
	$('#errorDiv2').attr("style",'display:none');
}

/**
 * 接收部门弹出框
 * @return
 */
function SSPRM51_openReceiveDepartBox(thisObj){
	SSPRM51_deleteActionMsg();
 	//取得所有部门类型
 	var departType = "";
 	for(var i=0;i<$("#departTypePop option").length;i++){
 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
 		//发货排除4柜台
 		if(departTypeValue != "4"){
 			departType += "&departType="+departTypeValue;
 		}
 	}
	var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType;
	var callback = function() {
		var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
		if($selected.length > 0) {
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			//$(thisObj).parents("span").find("#inOrgNameArr").val("("+departCode+")"+departName);
			//$(thisObj).parents("span").find("#inOrganizationIDArr").val(departId);
			$("#inOrganizationId").val(departId);
			$("#inOrgName").text("("+departCode+")"+departName);
			chooseDepart();
		}
	};
	popDataTableOfDepart(thisObj,param,callback);
}