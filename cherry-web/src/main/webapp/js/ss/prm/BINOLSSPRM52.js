
/**
 * 全局变量定义
 */
var binOLSSPRM52_global = {};
//当前点击对象
binOLSSPRM52_global.thisClickObj = {};
binOLSSPRM52_global.thisButton="";

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;
///////////////////////////添加明细////////////////////////////////////
/**
* 打开促销产品查询box
* @param thisObj
* @return
*/
function SSPRM52_openPromotionSearchBox (thisObj){
	SSPRM52_clearActionMsg();
	changeOddEvenColor();
	
//	var prmPopParam = {};
//	prmPopParam.thisObj= thisObj;
//	prmPopParam.organizationID=$('#outOrganizationID').val();
//	prmPopParam.priceType='SalePrice';
//	prmPopParam.prmExpiredFlag='1';
//	
//	popDataTableOfPrmInfo(prmPopParam);	
//	binOLSSPRM52_global.thisClickObj = $(thisObj);
	
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
			var html = '<tr id="dataRow'+index+'"><td class="center" id="dataTd0"><input id="chkbox" type="checkbox" value="'+index+'" onclick="SSPRM52_changechkbox(this)"/></td>';
			html += '<td id="dataTd1" style="white-space:nowrap">'+info.unitCode+'</td>';
			html += '<td id="dataTd2">'+info.barCode+'</td>';
			html += '<td id="dataTd3">'+info.nameTotal+'</td>';
			html += '<td id="dataTd4" style="text-align:right;">'+info.standardCost+'</td>';
			html += '<td id="nowCount'+index+'" style="text-align:right;"></td>';
			html += '<td id="dataTd7" style="text-align: right;"><input id="quantityuArr" class="text-number" type="text" value="" onchange="SSPRM52_changeCount(this)" maxlength="9" size="10" name="quantityuArr"></td>';
			html += '<td id="money'+index+'" style="text-align:right;">0.00</td>';
			html += '<td id="dataTd10"><input type="text" value="" maxlength="200" size="25" name="reasonArr"></td>';
			html += '<td style="display:none" id="dataTd11">';
			html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.standardCost+'"/>';
			html += '<input type="hidden" id="priceTotalArr'+index+'" name="priceTotalArr" value="0.00"/>';
			html += '<input type="hidden" id="nowCountArr" name="nowCountArr"/>';
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
			SSPRM52_calcTotal();
			//ajax取得选中促销品的库存数量
			var param ='depotId='+$('#outDepotId').val()+'&loginDepotId='+$("#outLogicInventoryInfoID").val();
			param += "&lockSection="+$("#lockSection").val();
			var len = $("#databody tr").length;
			if(len>0){
				for(var i=0;i<len;i++){
					var promotionProductVendorID = $($("#databody tr [name=prmVendorId]")[i]).val();
					var currentIndex = $($("#databody tr :checkbox")[i]).val();
					param += "&promotionProductVendorID="+promotionProductVendorID;
					param += "&currentIndex="+currentIndex;
				}
				cherryAjaxRequest({
					url:$("#s_getStockCount").html(),
					reloadFlg:true,
					param:param,
					callback:SSPRM52_getStockSuccess
				});
			}
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
//	$('#promotionDialog').dialog('destroy');	
//	var length = selectedPrmArr.length;
//	
//	//循环遍历
//	for(var i=0;i<length;i++){
//		//添加一个新行
//		SSPRM52_addrow();
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
//		//ajax取得该促销品的库存数量
//		var param ='promotionProductVendorID='+promotionProductVendorID+'&depotId='+$('#outDepotId').val()+'&loginDepotId='+$("#outLogicInventoryInfoID").val()+'&currentIndex='+index;
//		cherryAjaxRequest({
//			url:$("#s_getStockCount").html(),
//			reloadFlg:true,
//			param:param,
//			callback:SSPRM52_getStockSuccess
//		});
//	}
//	//改变奇偶行的颜色
//	this.changeOddEvenColor1();
//	//添加了新行，去除全选框的选中状态
//	$('#allSelect').prop("checked",false);
//}
function SSPRM52_getStockSuccess(msg){	
	var member = eval("("+msg+")"); //数据解析为json 格式  	
	$.each(member, function(i){
		$('#nowCount'+member[i].currentIndex).html( member[i].Quantity);
		$('#nowCount'+member[i].currentIndex).parent().find("#nowCountArr").val( member[i].Quantity);
	});
}
///**
// * 添加新行
// */
//function SSPRM52_addrow(){
////	SSPRM52_clearActionMsg();
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
//	$('#'+newid).find('#nowCount').attr('id','nowCount'+rowNumber);
//	$('#rowNumber').val(rowNumber);
//}

///////////////////////////画面控制//////////////////////////////////////////
/**
 * 删除选中行
 */
function SSPRM52_deleterow(){
	SSPRM52_clearActionMsg();
	$("#databody :checkbox").each(function(){
		if($(this).prop("checked"))
			{			
			 	$('#dataRow' + $(this).val()).remove();	
			}					
		});
	
	changeOddEvenColor();
	$('#allSelect').prop("checked",false);
	
	SSPRM52_calcTotal();
}

/**
 * 全选，全不选
 */
function SSPRM52_selectAll(){
	if($('#allSelect').prop("checked")){
		$("#databody :checkbox").each(function(){
			$(this).prop("checked",true);	
		});
	}
	else{
		$("#databody :checkbox").each(function(){
			$(this).prop("checked",false);
		});
	}
}

/**
 * 点击了选择框后，来控制全选框
 * @return
 */
function SSPRM52_changechkbox(obj){
	
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
 * 输入了发货数量
 * @param thisObj
 */
function SSPRM52_changeCount(thisObj){
	var $tr_obj = $(thisObj).parent().parent();
	var trID = $tr_obj.attr('id');
	var index = trID.substr(7);
	
	if($('#promotionProductVendorIDArr'+index ).val()==""){
		$(thisObj).val("");	
		return;
	}
	//var count = Math.abs(parseInt($(thisObj).val(),10));
	var count = parseInt($(thisObj).val(),10);

	if(isNaN(count)){		
		$(thisObj).val("");
		//清空金额
		$('#money'+index).html("0.00");
		SSPRM52_calcTotal();
		return;
	}
	$(thisObj).val(count);	
	var money = count*$('#priceUnitArr'+index).val();
	$('#money'+index).html(money.toFixed(2));
	$('#priceTotalArr'+index ).val(money.toFixed(2));
	
	SSPRM52_calcTotal();
}

function SSPRM52_calcTotal(){
	//计算总金额、总数量
	var rows = $("#databody").children();
	var totalQuantity = 0;
	var totalAmount = 0.00;
	if(rows.length > 0){
		rows.each(function(i){
			var quantity = Number($(this).find("#quantityuArr").val());
			var amount = Number($(this).find("[name='priceUnitArr']").val());
			totalQuantity += quantity;
			totalAmount += quantity * amount;
		});
	}
	$("#totalQuantity").html(totalQuantity);
	$("#totalAmount").html(totalAmount.toFixed(2));
}

/////////////////////////////////按钮/////////////////////////////////////////
/**
 * 点击保存按钮
 */
function SSPRM52_btnSaveClick(){
	binOLSSPRM52_global.thisButton="btnSave";
	SSPRM52_clearActionMsg();
	if(!submitflag){
		return false;
	}	
	if(!SSPRM52_hasErrData()){
		return false;
	};

	SSPRM52_submitForm();
	
}

/**
 * 点击发货按钮
 */
function SSPRM52_btnSendClick(){
	binOLSSPRM52_global.thisButton="btnSend";
	SSPRM52_clearActionMsg();
	if(!submitflag){
		return false;
	}	
	if(!SSPRM52_hasErrData()){
		return false;
	};
	if(SSPRM52_hasWarningData()){
		SSPRM52_submitConfirm();
	}else{
		SSPRM52_submitForm();
	}
}

/**
 * 点击删除按钮
 */
function SSPRM52_btnDeleteClick(){	
	binOLSSPRM52_global.thisButton="btnDelete";
	SSPRM52_clearActionMsg();
	SSPRM52_submitForm();	
}
/**
 * 点击审核退回按钮
 */
function SSPRM52_btnBackClick(){	
	binOLSSPRM52_global.thisButton="btnBack";
	SSPRM52_clearActionMsg();
	SSPRM52_submitForm();	
}
/**
 * 点击审核按钮
 */
function SSPRM52_btnAgreeClick(){	
	binOLSSPRM52_global.thisButton="btnAgree";
	SSPRM52_clearActionMsg();
	if(!submitflag){
		return false;
	}	
	if(!SSPRM52_hasErrData()){
		return false;
	};
	if(SSPRM52_hasWarningData()){
		SSPRM52_submitConfirm();
	}else{
		SSPRM52_submitForm();
	}
}
/**
 * 开始保存/发货/审核/删除
 */
function SSPRM52_submitForm(){
	var params=$('#mainForm').formSerialize();
	var curl="";
	if(binOLSSPRM52_global.thisButton=="btnSend"){
		//发货
		curl = $("#s_sendURL").html();
	}else if(binOLSSPRM52_global.thisButton=="btnSave"){
		//保存
		curl = $("#s_saveURL").html();
	}else if(binOLSSPRM52_global.thisButton=="btnDelete"){
		//删除
		curl = $("#s_deleteURL").html();
	}else if(binOLSSPRM52_global.thisButton=="btnAgree"){
		//审核通过
		curl = $("#s_auditAgreeURL").html();
	}else if(binOLSSPRM52_global.thisButton=="btnBack"){
		//审核退回
		curl = $("#s_auditDisagreeURL").html();
	}
	cherryAjaxRequest({
		url:curl,
		param:params,
		callback:SSPRM52_submitSuccess
	});
}


/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM52_submitSuccess(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$('#btnSave').hide();
		$('#btnDelete').hide();
		$('#btnSend').hide();
		$('#btnBack').hide();
		$('#btnAgree').hide();
	}
	if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
}

//////////////////////////数据校验///////////////////////////////////
/**
 * 提交前对数据进行检查
 * 检查有无明细数据，是否输入了数量
 * @returns {Boolean}
 */
function SSPRM52_hasErrData(){
	var flag = true;
	var count= 0;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		count +=1;
		if($(this).find("#quantityuArr").val()=="" || $(this).find("#quantityuArr").val()=="0"){
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
 * 提交前对数据进行检查
 * 检查发货数量是否大于库存
 * @returns {Boolean}
 */
function SSPRM52_hasWarningData(){
	var flag = false;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		if(Number($(this).find("#quantityuArr").val())>Number($(this).find("#nowCountArr").val())){
			flag = true;
			$(this).attr("class","errTRbgColor");
		}else{
			$(this).removeClass('errTRbgColor');
		}
	});	
	changeOddEvenColor1();	
	return flag;
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

/**
 * 删除掉画面头部的提示信息框
 * @return
 */
function SSPRM52_clearActionMsg(){
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
}

function SSPRM52_submitConfirm() {
	var checkStockFlag = $("#checkStockFlag").val();
	//为1库存允许为负，为0库存不允许为负
	if(checkStockFlag == "0"){
		$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00034').val());
		$('#errorDiv2').show();
		return;
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",	
		width: 	350,
		height: 200,
		//title: 	$("#updateRoleTitle").text()+':'+roleName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){SSPRM52_submitForm();$("#dialogInit").dialog('destroy');changeOddEvenColor();},
		cancelEvent: function(){$("#dialogInit").dialog('destroy');}
	};
	openDialog(dialogSetting);
	$("#dialogInit").html($("#errmsg4").val());
}

$(document).ready(function() {
	$('.tabs').tabs();
    $('#planArriveDate').cherryDate({
        beforeShow: function(input){
            var value = $('#dateToday').val();
            return [value,'minDate'];
        }
    });
});