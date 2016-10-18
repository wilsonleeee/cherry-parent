
/**
 * 全局变量定义
 */
var binOLSSPRM17_global = {};
//当前点击对象
binOLSSPRM17_global.thisClickObj = {};
//促销品查询弹出框用
binOLSSPRM17_global.dialogBody ="";
//点击保存，发货按钮用
binOLSSPRM17_global.thisButton="";
/**
* 页面初期处理
*/

$(document).ready(function() {
	if($("#actionResultDiv.actionError").is(":visible")){
		return;
	}
	binOLSSPRM17_global.dialogBody = $('#promotionDialog').html();
	chooseDepart();
});

/**
* 打开促销产品查询box
* @param thisObj
* @return
*/
function SSPRM17_openPromotionSearchBox (thisObj){
	var that= this;
	SSPRM17_clearActionMsg();
	if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()=="" || $('#outOrganizationId').val()=="0"){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if($('#outDepot').val()==null || $('#outDepot').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsg4').val());
		$('#errorDiv2').show();
		return false;
	}

	// 产品弹出框属性设置
	 var option = {
       targetId: "databody",// 目标区ID
       checkType : "checkbox",// 选择框类型
       prmCate :'CXLP',//促销礼品
       mode : 2,//模式
       brandInfoId : $("#brandInfoId").val(),//品牌
       isStock : "1",//显示管理库存的促销品
       getHtmlFun:BINOLSSPRM17.getHtmlFun,
       click:function(){//点击确定按钮之后的处理
			//改变奇偶行的样式
			changeOddEvenColor();
			// 设置全选状态
    	   var $checkboxs = $('#databody').find(':checkbox');
    	   var $unChecked = $('#databody').find(':checkbox:not(":checked")');
    	   if($unChecked.length == 0 && $checkboxs.length > 0){
    		   $('#allSelect').prop("checked",true);
    	   }else{
    		   $('#allSelect').prop("checked",false);
    	   }
//			// AJAX取得产品当前库存量
    	    BINOLSSPRM17.getPrmStock();
//			//计算总金额总数量
			BINOLSSPRM17.calcTotal();
		}
    };
     // 调用产品弹出框共通
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
//	closeCherryDialog('promotionDialog',binOLSSPRM17_global.dialogBody);
//	oTableArr[0]= null;
//	
//	var length = selectedPrmArr.length;
//	var selIndex = SSPRM17_getIndex();
//	//循环遍历
//	for(var i=0;i<length;i++){
//		//添加一个新行
//		SSPRM17_addrow(selIndex);
//		var selectedValue = $(selectedPrmArr[i]).val();
//		var selectedObj = window.JSON2.parse(selectedValue);
//		var index = $('#rowNumber').val();
//		// 促销品产品厂商ID
//		var promotionProductVendorID = selectedObj.promotionProductVendorId;	
//		var trID = 'dataRow'+index;	
//		//检查重复行
//		var sameFlag =false;
//		$.each($('#databody').find('tr'), function(n){
//			if($(this).find("input@[name='promotionProductVendorIDArr']").val() == promotionProductVendorID){
//				$(this).attr("class","errTRbgColor");
//				$('#errorDiv2 #errorSpan2').html($('#errmsgESS00025').val());
//				$('#errorDiv2').show();
//				sameFlag =true;
//				//删除新增行
//				$('#databody').find('tr:last').remove();
//				return false;
//			}
//		});
//		
//		if(sameFlag){
//			continue;
//		}
//		$('#promotionProductVendorIDArr' + index).val(promotionProductVendorID);
//		// 促销品厂商编码
//		var selectedPrmUnitCode = selectedObj.unitCode;
//		$('#'+trID).find('#dataTd1').html(selectedPrmUnitCode);		
//		// 促销品条码
//		var selectedPrmBarCode = selectedObj.barCode;
//		$('#'+trID).find('#dataTd2').html(selectedPrmBarCode);		
//		// 促销品名称
//		var selectedPrmName = selectedObj.nameTotal;
//		$('#'+trID).find('#dataTd3').html(selectedPrmName);	
//		// 促销品价格
//		var selectedPrice = selectedObj.standardCost;
//		if(selectedPrice==""){
//			$('#'+trID).find('#dataTd4').html("0.00");
//			$('#'+trID).find('#priceUnitArr').val(0.00);
//		}else{
//			$('#'+trID).find('#dataTd4').html(selectedPrice);
//			$('#'+trID).find('#priceUnitArr').val(selectedPrice);
//		}
//		//ajax取得该促销品的库存数量
//		var outLoginDepotId = $('#outLoginDepotId option:selected').val();
//		if(typeof outLoginDepotId == "undefined"){
//			outLoginDepotId = 0;
//		}
//		var param ='promotionProductVendorID='+promotionProductVendorID+'&depotId='+$('#outDepot').val()+'&currentIndex='+index+'&loginDepotId='+outLoginDepotId;
//		cherryAjaxRequest({
//			url:$("#s_getStockCount").html(),
//			reloadFlg:true,
//			param:param,
//			callback:SSPRM17_getStockSuccess
//		});
//		
//		$("#sortImage").attr("class","css_right ui-icon ui-icon-carat-2-n-s");
//	}
//	//改变奇偶行的颜色
//	this.changeOddEvenColor1();
//	//添加了新行，去除全选框的选中状态
//	$('#allSelect').prop("checked",false);
//}

//function SSPRM17_getStockSuccess(msg){	
//	var member = eval("("+msg+")"); //数据解析为json 格式  	
//	$.each(member, function(i){
//		$('#nowCount'+member[i].currentIndex).html( member[i].Quantity);
//		$('#dataRow'+member[i].currentIndex).find('#nowCountArr').val( member[i].Quantity);
//	});
//}
///**
// * 添加新行
// */
//function SSPRM17_addrow(selIndex){
////	SSPRM17_clearActionMsg();
//	var rowNumber = Math.abs($('#rowNumber').val())+1;
//	var newid = 'dataRow'+rowNumber;
//	$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");	
//	$('#'+newid + ' :checkbox').val(rowNumber);
////	$('#'+newid).find('#money').attr('id','money'+rowNumber);
////	$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//	$('#'+newid).find('#promotionProductVendorIDArr').attr('id','promotionProductVendorIDArr'+rowNumber);
//	$('#'+newid).find('#nowCount').attr('id','nowCount'+rowNumber);
//	//$('#'+newid).find('#nowCountArr').attr('id','nowCountArr'+rowNumber);	
//	//$('#'+newid).find('#inOrganizationIDArr').get(0).selectedIndex=selIndex;
//	$('#rowNumber').val(rowNumber);
//}
//
//function SSPRM17_getIndex(){
//	//var index = $('#databody').find('#inOrganizationIDArr').last().get(0).selectedIndex;
//	return 0;
//}

/**
 * 删除选中行
 */
function deleterow(){
	SSPRM17_clearActionMsg();
	var $checkboxs = $("#databody").find(":checkbox:checked");
	$checkboxs.parent().parent().remove();
	changeOddEvenColor();
	$('#allSelect').prop("checked",false);	
	//计算总金额总数量
	BINOLSSPRM17.calcTotal();
}

/**
 * 全选，全不选
 */
function selectAll(){
	if($('#allSelect').prop("checked")){
		$("#databody :checkbox").each(function(){
			if($(this).val()!=0){
				$(this).prop("checked",true);	
				}
			});}
	else{$("#databody :checkbox").each(function(){
			$(this).prop("checked",false);});
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
		if(i>=0){
				if($(this).prop("checked")!=true){
					flag=false;
				}	
			}		
		});
	if(flag){
		$('#allSelect').prop("checked",true);
	}
}

/**
 * 输入了数量
 * @param thisObj
 */
function SSPRM17_changeCount(thisObj){
	var $this = $(thisObj);
	var $thisTd = $this.parent();
	var $thisTr = $thisTd.parent();
	if(isNaN($this.val().toString())){
		$this.val("0");
		$thisTd.next().text("0.00");
	}else{
		var price = Number($thisTr.find(":input[name='priceUnitArr']").val());
		$this.val(parseInt(Number($this.val())));
		var amount = price * Number($this.val());
		$thisTd.next().text(amount.toFixed(2));
	}
	//计算总金额总数量
	BINOLSSPRM17.calcTotal();
}

/**
 * 更改了部门
 * @param thisObj
 */
function chooseDepart(thisObj){
	SSPRM17_clearActionMsg();
	var organizationid = $('#outOrganizationId').val();
	var param ="organizationid="+organizationid;
	
	//更改了部门后，取得该部门所拥有的仓库
	var url = $('#urlgetdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
	ajaxRequest(url,param,changeDepotDropDownList);

	//清空收货部门
	$("#inOrgName").text("");
	$("#inOrganizationID").val("");
	
//  接收部门改成弹出画面后不需要了
//	//取得该部门所对应的收货部门（即该部门下辖的所有部门）
//	url = $('#urlgetdepartAjax').html()+"?csrftoken="+$('#csrftoken').val();	
//	ajaxRequest(url,param,changeConDepartDropDownList);
	
	SSPRM17_clearDetailData();
}
/**
 * 清除明细行数据
 * @return
 */
function SSPRM17_clearDetailData(flag){
	$("#databody > tr").remove();
	if(!flag){
		$("#inOrganizationID").val("");
		$("#inOrgName").html("&nbsp");
	}
	$("#totalQuantity").html("0");
	$("#totalAmount").html("0.00");
}

/**
 * 更改了仓库
 * @param thisObj
 */
function SSPRM17_chooseDepot(thisObj){
	SSPRM17_clearDetailData();
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
 * 修改接收部门下拉框
 * @param data
 * @return
 */
function changeConDepartDropDownList(data){	
	//data为json格式的文本字符串	
	var member = eval("("+data+")");    //包数据解析为json 格式  
	var tempstr = "";
	$.each(member, function(i){
		tempstr += "<option value='"+ member[i].BIN_OrganizationID+"'>"+escapeHTMLHandle(member[i].DepartName)+"</option>";		
	});
//	//改变所有接收部门下拉框
//	$('#dataRow0').find("#inOrganizationIDArr").each(function(){	
//			 $(this).html(tempstr);							
//		});
}
////////////////////////////按钮///////////////////////////////
/**
 * 点击发货按钮
 */
function SSPRM17_btnSendClick(){
	binOLSSPRM17_global.thisButton="btnSend";
	SSPRM17_clearActionMsg();
	if(!submitflag){
		return false;
	}	
	if(!SSPRM17_hasErrData()){
		//如果有错误数据行，则报错
		return false;
	};
	if(SSPRM17_hasWarningData()){
		SSPRM17_submitConfirm();
	}else{
		SSPRM17_submitSaveOrSend();
	}
}
/**
 * 点击保存按钮
 */
function SSPRM17_btnSaveClick(){
	binOLSSPRM17_global.thisButton="btnSave";
	SSPRM17_clearActionMsg();
	if(!submitflag){
		return false;
	}	
	if(!SSPRM17_hasErrData()){
		return false;
	};

	SSPRM17_submitSaveOrSend();
}
/**
 * 开始保存或发货
 */
function SSPRM17_submitSaveOrSend(){
	var params=$('#mainForm').formSerialize();
	var curl="";
	if(binOLSSPRM17_global.thisButton=="btnSend"){
		curl = $("#submitURL").html();
	}else{
		curl = $("#saveURL").html();
	}
	cherryAjaxRequest({
		url:curl,
		param:params,
		callback:SSPRM17_submitSuccess
	});
}

/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM17_submitSuccess(msg){
	if(window.opener && window.opener.oTableArr[1] != null) {
		window.opener.oTableArr[1].fnDraw();
	}
	submitflag=true;	
	if(msg.indexOf("actionMessage") > -1){
		$("#databody > tr").remove();
		SSPRM17_clearDetailData();
	}
}

/**
 * 提交前对数据进行检查
 * 检查有无明细数据，是否输入了数量
 * @returns {Boolean}
 */
function SSPRM17_hasErrData(){
	var flag = true;
	var count= 0;
	if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()=="" || $('#outOrganizationId').val()=="0"){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	var inOrganizationID = $("#inOrganizationID").val();
	//没有选择收货部门
	if(inOrganizationID==""){
		flag = false;
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00048').val());
		$('#errorDiv2').show();
		return flag;
	}
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
			count +=1;
			var $this = $(this);
			var quantityuArr = $this.find(":input[name='quantityuArr']").val();
			if(quantityuArr==""||quantityuArr=="0"){
				//没输入数量的数据行	输入0
				flag = false;
				$(this).attr("class","errTRbgColor");				
			}else{
				$(this).removeClass('errTRbgColor');				
			}
			//设置每行数据的接受部门
			$(this).find("[name='inOrganizationIDArr']").val(inOrganizationID);
		}
	);	
	changeOddEvenColor1();
	if(!flag){
		//存在没输入数量的数据行		
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
function SSPRM17_hasWarningData(){
	var flag = false;
	//检查有无空行
	$.each($('#databody >tr'), function(i){	
		if(i>=0){
			if(Number($(this).find("#quantityuArr").val())>Number($(this).find("td").eq(5).text())){
				flag = true;
				$(this).attr("class","errTRbgColor");				
			}else{
				$(this).removeClass('errTRbgColor');				
			}		
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
function SSPRM17_clearActionMsg(){
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style",'display:none');
}

function SSPRM17_submitConfirm() {
	var checkStockFlag = $("#checkStockFlag").val();
	//为1库存允许为负，为0库存不允许为负
	if(checkStockFlag == "0"){
		$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00034').val());
		$('#errorDiv2').show();
		return;
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",	
		width: 	300,
		height: 180,
		//title: 	$("#updateRoleTitle").text()+':'+roleName,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){SSPRM17_submitSaveOrSend();removeDialog("#dialogInit");},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
	$("#dialogInit").html($("#errmsg3").val());
}

/**
 * 发货部门弹出框
 * @return
 */
function SSPRM17_openSendDepartBox(thisObj){
 	SSPRM17_clearActionMsg();
 	changeOddEvenColor();
 	
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
 	param += "&valid=1";//不允许查询停用部门
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


/**
 * 接收部门弹出框
 * @return
 */
function SSPRM17_openReceiveDepartBox(thisObj){
	if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()=="" || $('#outOrganizationId').val()=="0"){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if($('#outDepot').val()==null || $('#outDepot').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsg4').val());
		$('#errorDiv2').show();
		return false;
	}
	SSPRM17_clearActionMsg();
	changeOddEvenColor();
//	var param = "checkType=checkBox&privilegeFlg=1&businessType=1&levelFlg=1&organizationId="+$("#outOrganizationId").val();
	var param = "checkType=radio&businessType=40&depotId="+$("#outDepot option:selected").val()+"&inOutFlag=OUT&departId="+$("#outOrganizationId").val();
	var callback = function() {
		var $selected = $('#_departDataTable').find(':input[checked]').parents("tr");
		if($selected.length == 1) {
			var departId = $selected.find("input@[name='organizationId']").val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			//$(thisObj).parents("span").find("#inOrgNameArr").val("("+departCode+")"+departName);
			//$(thisObj).parents("span").find("#inOrganizationIDArr").val(departId);
			$("#inOrgName").html("("+departCode+")"+departName);
			$("#inOrganizationID").val(departId);
		}
//		}else if($selected.length > 1){
//			for(var i=0;i<$selected.length;i++){
//				var selIndex = SSPRM17_getIndex();
//				var departId = $($selected[i]).find("input@[name='organizationId']").val();
//				var departCode = $($selected[i]).find("td:eq(1)").text();
//				var departName = $($selected[i]).find("td:eq(2)").text();
//				SSPRM17_clearActionMsg();
//				var rowNumber = Math.abs($('#rowNumber').val())+1;
//				var newid = 'dataRow'+rowNumber;
//				$(thisObj).parents("tr").clone().attr('id',newid).removeAttr("style").appendTo("#databody");
//				$(thisObj).parents("tr").remove();
//				$('#'+newid + ' :checkbox').val(rowNumber);
////				$('#'+newid).find('#money').attr('id','money'+rowNumber);
////				$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//				$('#'+newid).find('[name=promotionProductVendorIDArr]').attr('id','promotionProductVendorIDArr'+rowNumber);
//				$('#'+newid).find('#nowCount').attr('id','nowCount'+rowNumber);	
////				$('#'+newid + ' #inOrganizationIDArr').get(0).selectedIndex=selIndex;
//				$('#rowNumber').val(rowNumber);
//				$('#'+newid).find('#inOrgNameArr').val("("+departCode+")"+departName);
//				$('#'+newid).find('#inOrganizationIDArr').val(departId);
//			}
//		}
		changeOddEvenColor();
		$("#sortImage").attr("class","css_right ui-icon ui-icon-carat-2-n-s");
	};
	popDepartTableByBusinessType(thisObj,param,callback);
}

function getTableHtmlObj(){
	var children = $("#databody").children();
	if(children.length == 0){
		return false;
	}
	var firstChild = {
			_id : children[0].id,
			_class : $(children[0]).attr("class"),
			_html : children[0].innerHTML,
			_style : children[0].style
	};
	var htmlObjArr = [];
	var tableObj = {};
	for(var i = 1 ; i < children.length ; i++){
		var child = children[i];
		var o = {
			_id : child.id,
			_class : $(child).attr("class"),
			_html : child.innerHTML.escapeHTML(),
			_style : child.style,
			_inOrgNameArr : $(child).find("#inOrgNameArr").val(),
			_quantityuArr : $(child).find("#quantityuArr").val(),
			_reasonArr : $(child).find("#reasonArr").val()
		};
		htmlObjArr.push(o);
	}
	tableObj.first = firstChild;
	tableObj.others = htmlObjArr;
	return tableObj;
}

function sortTable(){
	var tableObj = getTableHtmlObj();
	var firstChild = tableObj.first;
	var htmlObjArr = tableObj.others;
	htmlObjArr.sort(sortObject);
	if($("#sortImage").attr("class").indexOf("ui-icon-triangle-1-n") > -1){
		htmlObjArr.reverse();
		$("#sortImage").attr("class","css_right ui-icon ui-icon-triangle-1-s");
	}else{
		$("#sortImage").attr("class","css_right ui-icon ui-icon-triangle-1-n");
	}
	var tr0Html = "<tr id='"+firstChild._id+"' class='"+firstChild._class+"' style='display:none'></tr>";
	$("#databody").html("");
	$("#databody").append(tr0Html);
	$("#databody #"+firstChild._id).append(firstChild._html);
	for(var i = 0 ; i < htmlObjArr.length ; i++){
		var trHtml = "<tr id='"+htmlObjArr[i]._id+"' class='"+htmlObjArr[i]._class+"'></tr>";
		$("#databody").append(trHtml);
		$("#databody #"+htmlObjArr[i]._id).append(htmlObjArr[i]._html.unEscapeHTML());
		$("#"+htmlObjArr[i]._id+" #inOrgNameArr").val(htmlObjArr[i]._inOrgNameArr);
		$("#"+htmlObjArr[i]._id+" #quantityuArr").val(htmlObjArr[i]._quantityuArr);
		$("#"+htmlObjArr[i]._id+" #reasonArr").val(htmlObjArr[i]._reasonArr);
	}
	changeOddEvenColor();
}

function sortObject(obj1,obj2){
	if(obj1._inOrgNameArr < obj2._inOrgNameArr){
		return -1;
	}else if(obj1._inOrgNameArr > obj2._inOrgNameArr){
		return 1;
	}else{
		return 0;
	}
}



function BINOLSSPRM17(){};

BINOLSSPRM17.prototype={
	/**
	 * 取得促销品发货单详单
	 * @return
	 */
	"getPrmDeliverDetail":function(negativeFlag,deliverId,receiveDepId,departNameReceive){
		var that = this;
		var callback = function(msg) {
			var jsons = eval(msg);
			for(var i=0;i<jsons.length;i++){
				var html = BINOLSSPRM17.getHtmlFun(jsons[i],negativeFlag);
				$("#databody").append(html);
			}
			//改变奇偶行的颜色
			changeOddEvenColor();
			//添加了新行，去除全选框的选中状态
			$('#allSelect').prop("checked",false);
			//计算总金额总数量
			BINOLSSPRM17.calcTotal();
		};
		//ajax取得选中的发货单详细
		var param ="deliverId="+deliverId+"&organizationId="+$("#outOrganizationId").val()+"&depotInfoId="+$("#outDepot").val()+"&logicDepotInfoId="+$("#outLoginDepotId").val();
		cherryAjaxRequest({
			url:$("#s_getDeliverDetail").html(),
			reloadFlg:true,
			param:param,
			callback:callback
		});
	},
	
	/**
	 * 复制发货单
	 * @return
	 */
	"copyDeliver":function(){
		if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()=="" || $('#outOrganizationId').val()=="0"){
			$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
			$('#errorDiv2').show();
			return false;
		}
		if($('#outDepot').val()==null || $('#outDepot').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg4').val());
			$('#errorDiv2').show();
			return false;
		}
		SSPRM17_clearActionMsg();
		changeOddEvenColor();
		var callback = function() {
			var $selected = $('#deliverDataTable').find(':input[checked]').parents("tr");
			//原单数量取反
			var negativeFlag= $('#checkboxType').is(":checked");
			if($selected.length>0){
				var deliverId = $selected.find("[name=deliverId]").val();
				var receiveDepId = $selected.find("[name=receiveDepId]").val();
				var departNameReceive = $selected.find("[name=departNameReceive]").val();
				//清空原先发货单
				SSPRM17_clearDetailData();
				BINOLSSPRM17.getPrmDeliverDetail(negativeFlag,deliverId,receiveDepId,departNameReceive);
			}
		};
		var param ="organizationId="+$('#outOrganizationId').val()+"&depotInfoId="+$('#outDepot').val();
		popDataTableOfPrmDeliver(null,param,callback);
	},
	
	"calcTotal":function(){
		//计算总金额、总数量
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
					var $tds = $(this).find("td");
					totalQuantity += Number($tds.eq(6).find(":input").val());
					totalAmount +=Number($tds.eq(7).text());
			});
		}
		$("#totalQuantity").html(totalQuantity);
		$("#totalAmount").html(totalAmount.toFixed(2));
	},
	"getHtmlFun":function(info,negativeFlag){
		var proId = info.proId;
		var unitCode = info.unitCode;
		var barCode = info.barCode;
		var nameTotal = info.nameTotal;
		var price = info.standardCost;
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var curStock = info.stockQuantity;//当前库存
		if(isEmpty(curStock)){
			curStock = 0;
		}
		var amount = price * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = escapeHTMLHandle(info.reason);//备注
		if(quantity == 0){quantity = "";}
		if(isEmpty(reason)){reason = "";}
			var html = '<tr>';
   			html += '<td><input type="checkbox" onclick="changechkbox(this);" value="'+ proId +'"></td>';
   			html += '<td >'+unitCode+'</td>';
   			html += '<td>'+barCode+'</td>';
   			html += '<td>'+nameTotal+'</td>';
   			html += '<td style="text-align: right;">'+price+'</td>';
   			html += '<td style="text-align: right;">'+curStock+'</td>';
   			html += '<td class="center"><input  value="' + quantity +'" onchange="SSPRM17_changeCount(this);" class="text-number" style="width:95%;" size="10" maxlength="9" name="quantityuArr" id="quantityuArr" /></td>';
   			html += '<td style="text-align: right;">'+amount+'</td>';
   			html += '<td class="center"><input value="' + reason +'" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
   			html += '<td style="display: none;">';
   			html += '<input type="hidden" name="priceUnitArr" value="'+price+'"/>';
   			html += '<input type="hidden" name="prmVendorId" value="'+proId+'"/>';
   			html += '<input type="hidden" name="promotionProductVendorIDArr" id="promotionProductVendorIDArr" value="'+proId+'"/>';
   			html += '<input type="hidden" name="inOrganizationIDArr"/>';
   			html += '<input type="hidden" name="nowCountArr" id="nowCountArr" value="'+curStock+'/>';
   			html += '</td></tr>';
	return html;
	},
	/**
	 * AJAX取得产品当前库存量
	 * 
	 * */
	"getPrmStock": function(){
		var url = $("#s_getStockCount").html();
		var lenTR = $("#databody").find("tr");
		var outLoginDepotId = $("#outLoginDepotId").serialize();
		if(typeof outLoginDepotId == "undefined"){
			outLoginDepotId = 0;
		}
		var params = getSerializeToken();
			params += "&" + $("#outDepot").serialize();
			params += "&" + outLoginDepotId;
			params += "&" + $("#databody").find("#promotionProductVendorIDArr").serialize();
		if(lenTR.length>0){	
			ajaxRequest(url,params,function(json){
				for (var one in json){
				    var prmVendorId = json[one].prmVendorId;
				    var stock = json[one].stock;
				    $("#databody").find("tr").each(function(){
				    	var $tr = $(this);
				    	if($tr.find("@[name='prmVendorId']").val()==prmVendorId){
				    		$tr.find("td:eq(5)").text(stock);
				    	}
				    });
				}
			},"json");
		}
	}
};

var BINOLSSPRM17 = new BINOLSSPRM17();

$(function(){
    $('#planArriveDate').cherryDate({
        beforeShow: function(input){
            var value = $('#dateToday').val();
            return [value,'minDate'];
        }
    });
});