var binOLSSPRM56_global = {};
//当前点击对象
binOLSSPRM56_global.thisClickObj = {};

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

$(document).ready(function() {
	window.onbeforeunload = function(){
		if(OS_BILL_Jump_needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	};		
	if (window.opener) {	
	  window.opener.lockParentWindow();	
	};
	$('.tabs').tabs();
	initDetailTable();
	//binOLSSPRM56_global.dialogBody = $('#promotionDialog').html();
	
} );


/**
* 打开促销产品查询box
* @param thisObj
* @return
*/
function SSPRM56_openPromotionSearchBox (thisObj){
	
	SSPRM56_clearActionMsg();
	changeOddEvenColor();
	
//	var prmPopParam = {};
//	prmPopParam.thisObj= thisObj;
//	prmPopParam.priceType='AllocationPrice';
//	prmPopParam.prmExpiredFlag='1';
//	prmPopParam.dialogBody=binOLSSPRM56_global.dialogBody;
//	
//	popDataTableOfPrmInfo(prmPopParam);	
//	binOLSSPRM56_global.thisClickObj = $(thisObj);
	
	//SSPRM56_clearActionMsg();
	//var url = $('#prmSearchUrl').html()+"?csrftoken="+$('#csrftoken').val()
	//+"&organizationID="+$('#outOrganizationId').val();
	//popDataTableOfPrmInfo(thisObj,$('#outOrganizationId').val());	
	//binOLSSPRM56_global.thisClickObj = $(thisObj);
	
	// 促销品弹出框属性设置
	var option = {
		targetId: "databody",//目标区ID
		checkType : "checkbox",// 选择框类型
		prmCate :'CXLP', // 促销品类别
		mode : 2, // 模式
		brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
		getHtmlFun:function(info){// 目标区追加数据行function
			var rowNumber = Math.abs($('#rowNumber').val())+1;
			$('#rowNumber').val(rowNumber);
			var index = $('#rowNumber').val();
			var html = '<tr id="dataRow'+index+'"><td class="center" id="dataTd0"><input id="chkbox" type="checkbox" value="'+index+'" onclick="SSPRM56_changechkbox(this)"/></td>';
			html += '<td id="dataTd1" style="white-space:nowrap">'+info.unitCode+'</td>';
			html += '<td id="dataTd2">'+info.barCode+'</td>';
			html += '<td id="dataTd3">'+info.nameTotal+'</td>';
			html += '<td id="dataTd4" style="text-align:right;">'+info.standardCost+'</td>';
			html += '<td id="dataTd7"><input id="quantityuArr" class="text-number" type="text" value="" onchange="SSPRM56_changeCount(this)" maxlength="9" size="10" name="quantityuArr"></td>';
			html += '<td id="money'+index+'" style="text-align:right;">0.00</td>';
			html += '<td id="dataTd10"><input type="text" value="" maxlength="200" size="25" name="reasonArr"></td>';
			html += '<td style="display:none" id="dataTd11">';
			html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.standardCost+'"/>';
			html += '<input type="hidden" id="priceTotalArr'+index+'" name="priceTotalArr" value="0.00"/>';
			html += '<input type="hidden" id="promotionProductVendorIDArr'+index+'" name="promotionProductVendorIDArr" value="'+info.proId+'"/>';
			html += '<input type="hidden" name="prmVendorId" value="'+info.proId+'"/>';
			html += '</td></tr>';
			return html;
		},
		click:function(){
			// 设置全选状态
			var $checkboxs = $('#databody').find(':checkbox');
			var $unChecked = $('#databody').find(':checkbox:not(":checked")');
			if($unChecked.length == 0 && $checkboxs.length > 0){
				$('#allSelect').prop("checked",true);
			}else{
				$('#allSelect').prop("checked",false);
			}
			changeOddEvenColor();
		}
	};
	// 调用促销品弹出框共通
	popAjaxPrmDialog(option);
}

///**
//* 选择促销品
//* @return
//*/
//function selectPromotion () {	
//	var that = this;
//	//取得选中值
//	var selectedPrmArr = $('#prm_dataTableBody input:checked');
//	//关闭清除弹出框
//	//closeCherryDialog('promotionDialog',binOLSSPRM56_global.dialogBody);
//	//oTableArr[0]= null;
//	$('#promotionDialog').dialog('destroy');
//	
//	var length = selectedPrmArr.length;
//	
//	//循环遍历
//	for(var i=0;i<length;i++){
//		//添加一个新行
//		SSPRM56_addrow();
//		var selectedValue = $(selectedPrmArr[i]).val();
//		var selectedObj = window.JSON2.parse(selectedValue);
//		var index = $('#rowNumber').val();
//		// 促销品产品厂商ID
//		var promotionProductVendorID = selectedObj.promotionProductVendorId;	
//
//		var trID = 'dataRow'+index;	
//		//检查重复
//		var sameFlag = false;
//		$.each($('#databody >tr'), function(n){					
//			if($(this).find("input@[name='promotionProductVendorIDArr']").val() == promotionProductVendorID){
//				$(this).attr("class","errTRbgColor");
//				$('#errorDiv2 #errorSpan2').html($('#errmsg3').val());
//				$('#errorDiv2').show();
//				sameFlag =true;
//				//删除新增行
//				$('#databody').find('tr:last').remove();
//				return false;
//				}	
//		});
//		if(sameFlag){
//			continue;
//		}
//		$('#promotionProductVendorIDArr' + index).val(promotionProductVendorID);
//		// 促销品厂商编码
//		var selectedPrmUnitCode = selectedObj.unitCode;
//		$('#'+trID).find('#dataTd1').html(selectedPrmUnitCode);
//		
//		// 促销品条码
//		var selectedPrmBarCode = selectedObj.barCode;
//		$('#'+trID).find('#dataTd2').html(selectedPrmBarCode);
//		
//		// 促销品名称
//		var selectedPrmName = selectedObj.nameTotal;
//		$('#'+trID).find('#dataTd3').html(selectedPrmName);	
//
//
//		// 促销品价格
//		var selectedPrice = selectedObj.standardCost;		
//		if(selectedPrice==""){
//			$('#'+trID).find('#dataTd4').html("0.00");
//			$('#priceUnitArr'+ index).val(0.00);
//		}else{
//			$('#'+trID).find('#dataTd4').html(selectedPrice);
//			$('#priceUnitArr'+ index).val(selectedPrice);
//		}		
//
//		$('#money'+ index).html("0.00");
//	}
//	//改变奇偶行的颜色
//	this.changeOddEvenColor1();
//	//添加了新行，去除全选框的选中状态
//	$('#allSelect').prop("checked",false);
//	
//	//ajax取得该促销品的库存数量
//	//var param ='promotionProductVendorID='+promotionProductVendorID+'&depotId='+$('#depot').val();
//	//cherryAjaxRequest({
//	//	url:$("#s_getStockCount").html(),
//	//	param:param,
//	//	callback:SSPRM56_getStockSuccess
//	//});
//
//	//closeCherryDialog('promotionDialog',binOLSSPRM56_global.dialogBody);
//	//oTableArr[0]= null;
//	
//}

///**
// * 添加新行
// */
//function SSPRM56_addrow(){
//	
////	SSPRM56_clearActionMsg();
//	var rowNumber = Math.abs($('#rowNumber').val())+1;
//
//	var newid = 'dataRow'+rowNumber;
//	$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");
//	
//	$('#'+newid + ' :checkbox').val(rowNumber);
//	$('#'+newid).find('#money').attr('id','money'+rowNumber);
//	$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//	$('#'+newid).find('#priceTotalArr').attr('id','priceTotalArr'+rowNumber);
//	$('#'+newid).find('#promotionProductVendorIDArr').attr('id','promotionProductVendorIDArr'+rowNumber);
//	
//	$('#rowNumber').val(rowNumber);
//}
/**
 * 删除选中行
 */
function SSPRM56_deleterow(){
	SSPRM56_clearActionMsg();
	$("#databody :checkbox").each(function(){
		if($(this).prop("checked"))
			{			
			 	$('#dataRow' + $(this).val()).remove();	
			}					
		});
	
	changeOddEvenColor();
	$('#allSelect').prop("checked",false);
}

/**
 * 全选，全不选
 */
function SSPRM56_selectAll(){
	if($('#allSelect').prop("checked")){
		$("#databody :checkbox").each(function(){
			$(this).prop("checked",true);
		});
	}
	else{
		$("#databody :checkbox").each(function(){
			$(this).removeAttr("checked");
		});
	}
}

/**
 * 点击了选择框后，来控制全选框
 * @return
 */
function SSPRM56_changechkbox(obj){
	
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
 * 输入了调拨数量
 * @param thisObj
 */
function SSPRM56_changeCount(thisObj){
	
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
		//清空金额
		$('#money'+index).html("0.00");
		return;
	}	
	$(thisObj).val(count);	
	var money = count*$('#priceUnitArr'+index).val();
	$('#money'+index).html(money.toFixed(2));
	$('#priceTotalArr'+index ).val(money.toFixed(2));
}

/**
 * 提交前对数据进行检查
 * @returns {Boolean}
 */
function SSPRM56_checkData(){	
	changeOddEvenColor();
	var flag = true;
	var count= 0;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		count +=1;
		if($(this).find("[name=quantityuArr]").val()=="" || $(this).find("[name=quantityuArr]").val()=="0"){
			flag = false;
			$(this).attr("class","errTRbgColor");
		}else{
			$(this).removeClass('errTRbgColor');
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
function SSPRM56_clearActionMsg(){
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
	$.each($('#databody >tr'), function(i){	
		if(i>0){			
			$(this).removeClass('errTRbgColor');
		}
	});	
}
function changeOddEvenColor(){
	$("#databody tr:odd").attr("class","even");
	$("#databody tr:even").attr("class","odd");
}
//function SSPRM56_clearDetailData(){
//	$("#databody > tr[id!='dataRow0']").remove();
//}
