
/**
* 打开促销产品查询box
* @param thisObj
* @return
*/
function SSPRM24_openPromotionSearchBox (thisObj){
	SSPRM24_clearActionMsg();
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
	changeOddEvenColor();
	
	var option = {
	     	   targetId: "databody",
	           checkType : "checkbox",
	           prmCate :'CXLP',
	           mode : 2,
	           brandInfoId : $("#brandInfoId").val(),
	           isStock : "1",//显示管理库存的促销品
		       getHtmlFun:SSPRM24_getHtmlFun,
			   click : function(){
					//改变奇偶行的颜色
					changeOddEvenColor();
					//ajax取得促销品库存
					SSPRM24_getStocks();
					//去除全选框的选中状态
					var $checkboxs = $('#databody').find(':checkbox');
	        	    var $unChecked = $('#databody').find(':checkbox:not(":checked")');
	        	    if($unChecked.length == 0 && $checkboxs.length > 0){
	        		    $('#allSelect').prop("checked",true);
	        	    }else{
	        		    $('#allSelect').prop("checked",false);
	        	    }
			   }
	        };
		popAjaxPrmDialog(option);
}

/**
 * 
 * 
 * */
function SSPRM24_getHtmlFun(info){
	if(!info){
		return ;
	}
	var html = [];
	html.push('<tr id="tr_'+info.proId+'">');
	//chcekbox
	html.push('<td class="center"><input type="hidden" name="prmVendorId" value="'+info.proId+'"></input><input id="chkbox" type="checkbox" onclick="changechkbox(this);"/></td>');
	//UnitCode
	html.push('<td id="dataTd1" style="white-space:nowrap">'+info.unitCode.unEscapeHTML()+'</td>');
	//BarCode
	html.push('<td id="dataTd2">'+info.barCode.unEscapeHTML()+'</td>');
	//名称
	html.push('<td id="dataTd3">'+info.nameTotal.unEscapeHTML()+'</td>');
	//价格
	html.push('<td id="dataTd4">'+info.standardCost+'</td>');
	//数量
	html.push('<td id="dataTd6"><span><input name="quantityuArr" id="quantityuArr" class="text-number" size="10" maxlength="9" onchange="SSPRM24_changeCount(this);" ondragstart="return false;" ondragover="return false;" ondragend="return false;" ondrop="return false;"></input></span></td>');
	//备注
	html.push('<td id="dataTd8"><span><input name="reasonArr" style="width:95%;" id="reasonArr" size="25" maxlength="200"></input></span></td>');
	//隐藏
	html.push('<td id="dataTd11" style="display:none">');
	html.push('<input type="hidden" id="hasprmflagArr" name="hasprmflagArr" value="0"/>');
	html.push('<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+info.standardCost+'"/>');
	html.push('<input type="hidden" id="promotionProductVendorIDArr" name="promotionProductVendorIDArr" value="'+info.proId+'"/>');
	html.push('<input type="hidden" id="bookCountArr" name="bookCountArr" />');
	html.push('<input type="hidden" id="gainCountArr" name="gainCountArr" />');
	html.push('<input type="hidden" id="gainMoneyArr" name="gainMoneyArr" />');
	html.push('<input type="hidden" id="isStockArr" name="isStockArr"/>');
	html.push('</td>');
	html.push('</tr>');
	return html.join("");
}

/**
 * 批量取得库存
 * 
 * */
/**
 * 批量取得库存
 * 
 * */
function SSPRM24_getStocks(){
	var url = $("#s_getStockCount").html();
	var lenTR = $("#databody").find("tr");
	var params = getSerializeToken();
		params += "&" + $("#depot").serialize();
		params += "&" + $("#logicDepot").serialize();
		params += "&" + $("#databody").find("#promotionProductVendorIDArr").serialize();
	if(lenTR.length>0){
		ajaxRequest(url,params,function(json){
			for (var one in json){
			    var prmVendorId = json[one].prmVendorId;
			    var stock = json[one].stock;
			    var isStock=json[one].IsStock;
			    $("#databody").find("tr").each(function(){
			    	var $tr = $(this);
			    	if($tr.find("@[name='prmVendorId']").val()==prmVendorId){
			    		//库存数量
			    		$(this).find("#dataTd5").html(stock);
			    		$(this).find("#bookCountArr").val(stock);
			    		//是否管理库存
			    		$(this).find("#isStockArr").val(isStock);
			    	}
			    });
			}
		},"json");
	}
}

/**
 * 删除选中行
 */
function SSPRM24_deleterow(){
	SSPRM24_clearActionMsg();
	$("#databody :checkbox").each(function(){
		if($(this).prop("checked"))
			{			
			 	$(this).parent().parent().remove();	
			}					
		});
	changeOddEvenColor();
	$('#allSelect').prop("checked",false);
}

/**
 * 全选，全不选
 */
function SSPRM24_selectAll(){
	if($('#allSelect').prop("checked")){
		$("#databody :checkbox").each(function(){
			if($(this).val()!=0){
				$(this).prop("checked",true);	
			}
		});
	}else{
		$("#databody :checkbox").each(function(){
			$(this).prop("checked",false);
		});
	}
}

/**
 * 点击了选择框后，来控制全选框
 * @return
 */
function changechkbox(obj){
	
	var chked = $(obj).prop("checked");
	if(!chked){
		$('#allSelect').prop("checked",false);
		return false;
	}
	var flag = true;
	$("#databody :checkbox").each(function(i){
		if($(this).prop("checked")!=true){
			flag=false;
		}
		});
	if(flag){
		$('#allSelect').prop("checked",true);
	}
}
/**
 * 输入了盘点数量
 * @param thisObj
 */
function SSPRM24_changeCount(thisObj){
	var $tr_obj = $(thisObj).parent().parent().parent();
	var count = parseInt($(thisObj).val(),10);
	var allowNegativeFlag = $("#allowNegativeFlag").val();
	if(allowNegativeFlag != "1"){
		count = Math.abs(count);
	}
	if($tr_obj.find('#promotionProductVendorIDArr').val()=="" || isNaN(count)){
		$tr_obj.find(thisObj).val("");
		//清空盘差和金额差额
		$tr_obj.find('#gainCountArr').val("");
		$tr_obj.find('#gainMoneyArr').val("");
		return;
	}
	
	$(thisObj).val(count);
	var tempCount = count-$tr_obj.find('#bookCountArr').val();
	var money = tempCount*$tr_obj.find('#priceUnitArr').val();	
	$tr_obj.find('#gainCountArr').val(tempCount);
	$tr_obj.find('#gainMoneyArr').val(money.toFixed(2));
}

/**
 * 更改了部门
 * @param thisObj
 */
function SSPRM24_chooseDepart(thisObj){
	SSPRM24_clearActionMsg();
	//清除现有明细
	$("#databody > tr").remove();
	$('#allSelect').prop("checked",false);
	
	var organizationid = $('#organizationId').val();
	var param ="organizationid="+organizationid;
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#s_getdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,param,SSPRM24_changeDepotDropDownList);	
	
}

/**
 * 修改仓库下拉框
 * @param data
 * @return
 */
function SSPRM24_changeDepotDropDownList(data){	
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
	ajaxRequest(url,param,SSPRM24_changeLogicDepotDropDownList);
}

/**
 * 修改逻辑仓库下拉框
 * @param data
 * @return
 */
function SSPRM24_changeLogicDepotDropDownList(data){
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
 * 点击确定按钮
 */
function SSPRM24_submitFormSave(){	
	SSPRM24_clearActionMsg();
	if(!SSPRM24_checkIsStock()){
		return;
	}
	if(!SSPRM24_checkData()){
		return;
	}
	cherryAjaxRequest({
		url:$("#s_saveURL").html(),
		param:$('#mainForm').formSerialize(),
		callback:SSPRM24_submitSuccess
	});	
}

/**
 * 提交前对数据进行检查
 * @returns {Boolean}
 */
function SSPRM24_checkData(){	
	var flag = true;
	var count= 0;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		count +=1;
		if($(this).find("#quantityuArr").val()==""){
			flag = false;
			$(this).attr("class","errTRbgColor");				
		}else{
			$(this).removeClass('errTRbgColor');				
		}
	});	
	changeOddEvenColor1();
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
 * 提交前对检查是否有不需要管理库存的促销品
 * @returns {Boolean}
 */

function SSPRM24_checkIsStock(){	
	var flag = true;
	//检查是否有不需要管理库存的促销品
	$.each($('#databody >tr'), function(i){	
		if($(this).find("#isStockArr").val()=="0"){
			flag = false;
			$(this).attr("class","errTRbgColor");				
		}else{
			$(this).removeClass('errTRbgColor');				
		}	
	});	
	changeOddEvenColor1();
	if(!flag){
		//有不需要管理库存的促销品		
		$('#errorDiv2 #errorSpan2').html($('#errmsg4').val());
		$('#errorDiv2').show();
		return flag;
	}
	return flag;
}
/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM24_submitSuccess(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#databody > tr").remove();
	}
}

/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function SSPRM24_clearActionMsg(){
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
	$('#databody >tr').removeClass('errTRbgColor');
}
function changeOddEvenColor(){
	$("#databody tr:odd").attr("class","even");
	$("#databody tr:even").attr("class","odd");
}
function changeOddEvenColor1(){
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

function SSPRM24_clearDetail(){
	//清除现有明细
	$("#databody > tr").remove();
	$('#allSelect').prop("checked",false);
}