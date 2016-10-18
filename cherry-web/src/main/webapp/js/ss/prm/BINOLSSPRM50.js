
/**
 * 输入了发货数量
 * @param thisObj
 */
function changeCount(thisObj){	
	var count = Math.abs(parseInt($(thisObj).val(),10));
	$tr_obj = $(thisObj).parent().parent();	
	var trID = $tr_obj.attr('id');
	var money = count*$('#'+trID +' #priceUnitArr').val();
	if(isNaN(count)){
		$(thisObj).val("");
		$('#'+trID +' > #dataTd8').html("0.00");
		return;
	}
	if(isNaN(money)){		
		//$('#'+trID +' > #dataTd8').html("0.00");
		return;
	}
	$(thisObj).val(count);
	$('#'+trID +' > #dataTd8').html(money.toFixed(2));
}

/**
 * 更改了部门
 * @param thisObj
 */
function chooseDepart(thisObj){	
	SSPRM50_deleteActionMsg();
	var organizationid = $('#outOrganizationId').val();
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
	$("#outDepot ").empty();
	$.each(member, function(i){
		$("#outDepot ").append("<option value='"+ member[i].BIN_InventoryInfoID+"'>"+escapeHTMLHandle(member[i].InventoryName)+"</option>"); 
		});
}

/**
 * 点击确定按钮
 */
function submitFormSend(){
	if(!submitflag){
		return false;
	}	
	if(!checkData()){
		return false;
	};
	
	var params=$('#mainForm').formSerialize();
	cherryAjaxRequest({
		url:$("#submitURL").html(),
		param:params,
		callback:submitSuccess
	});
}
/**
 * 点击保存按钮
 */
function submitFormSave(){
	
	if(!submitflag){
		return false;
	}	
	if(!checkData()){
		return false;
	};

	var params=$('#mainForm').formSerialize();
	cherryAjaxRequest({
		url:$("#saveURL").html(),
		param:params,
		callback:submitSuccess
	});
}
/**
 * 提交成功的回调函数
 * @param msg
 */
function submitSuccess(msg){
	submitflag=true;	
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#mydetail").html("");
		$("#upExcel").val("");
	}
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
			count +=1;
			if($.trim($(this).find("#dataTd1").html())=="" ||$.trim($(this).find("#dataTd2").html())==""||$.trim($(this).find("#dataTd5").html())==""){
				flag = false;				
				$(this).attr('class','errTRbgColor');				
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
function SSPRM50_deleteActionMsg(){
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
}

/**
 * 发货部门弹出框
 * @return
 */
function SSPRM50_openSendDepartBox(thisObj){
	SSPRM50_deleteActionMsg();
 	
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
			$("#outOrganizationId").val(departId);
			$("#outOrgName").text("("+departCode+")"+departName);
			chooseDepart();
		}
		
	};
	popDataTableOfDepart(thisObj,param,callback);
}