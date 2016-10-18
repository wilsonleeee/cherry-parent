
////查询
//function search(){
//	var url = $("#search_url").html();
//	
//	var nameTotal = $("#nameTotal").val();
//	 // 查询参数序列化
//	 var params= $("#mainForm").serialize();
//	 url = url + "?" + params;
//	 window.location.href=url;
//}
/**
 * 空验证
 * @param str
 * @return
 */
function isEmpty(obj){
	if(obj == undefined || obj == null){
		return true;
	}else if(typeof(obj) == "string"){
		if($.trim(obj) == ""){
			return true;
		}
	}
	return false;
}

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }	
	
	var queStr = sessionStorage.getItem("queStr");//如果sessionStorage有值以sessionStorage的值为准
 	if(isEmpty(queStr)){
		queStr = $("#queStr").val();
		sessionStorage.setItem('queStr', queStr);//将数据写入到sessionStorage中
	}else{
		$("#queStr").val(queStr);
	} 
	queStr = $("#queStr").val();
	var listJSONObj = eval('('+queStr+')');
	if(listJSONObj.length>0){	
		$("#dataTable").find("td").each(function(){//循环每一个产品
			var _this=this;
			var prtVendorId = $(_this).find("#prtVendorId").val();//当前单元格对应的产品厂商ID
			for(var i=0;i<listJSONObj.length;i++){
				if(listJSONObj[i].prtVendorId==prtVendorId){
					var quantityArr = 1;//当前单元格对应的订货数量
					if(!isEmpty(listJSONObj[i].quantityArr)){
						quantityArr = listJSONObj[i].quantityArr;
					}
					$(_this).find("#quantityArr").val(quantityArr);
					$(_this).find("#reasonArr").val(listJSONObj[i].reasonArr);
					$(_this).find("#productImageUrl").addClass("select");
					$(_this).find("#productImageSelect").addClass("select_icon");
				}
			}
			
		});
		
	}
	
	
	
	var   isNewProductFlag = $("#isNewProductFlag").val();//是否新品标识
	if(!isEmpty(isNewProductFlag)){
		$("#isNewProductCheckbox").attr("checked",true);
	}else{
		$("#isNewProductCheckbox").attr("checked",false);
	}
});


window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};


/**
 * 验证数量
 * 
 * */
function validAmount(obj){
	var $thisVal= $(obj).val();
	var thisTD = $(obj).parent().parent().parent();//表示当前单元格
	var productImageUrl = $(thisTD).find("#productImageUrl");//产品图片	
	
    var re = new RegExp("^[0-9]*[1-9][0-9]*$");//正整数校验
    if (isEmpty($thisVal)) {
        $(obj).val("1");
    }else{
    	if(!re.test($thisVal)){
    		$(obj).val("1");
    	}
    }
    
    if($(productImageUrl).attr("class").indexOf("select")>=0){//表示该数据已经选中，需要重新将数据放入到sessionStorage中
    		
    	var prtVendorId = $(thisTD).find("#prtVendorId").val();//当前单元格对应的产品厂商ID
    	var unitCodeArr = $(thisTD).find("#unitCodeArr").val();//当前单元格对应的产品厂商编码
    	var barCodeArr = $(thisTD).find("#barCodeArr").val();//当前单元格对应的产品条码
    	var productName = $(thisTD).find("#productName").val();//当前单元格对应的产品名称
    	var amount = $(thisTD).find("#amount").val();//当前单元格对应的库存数量
    	var distributionPrice = $(thisTD).find("#distributionPrice").val();//当前单元格对应的订货价格
    	var quantityArr = $(thisTD).find("#quantityArr").val();//当前单元格对应的订货数量
    	var reasonArr = $(thisTD).find("#reasonArr").val();//当前单元格对应的备注
    	
    	var queStr = sessionStorage.getItem("queStr");//如果sessionStorage有值以sessionStorage的值为准
    	if(isEmpty(queStr)){
    		queStr = $("#queStr").val();//原来选中的数据
    	}

    	var newArray = new Array();//数据重组以后的新数组
    	var listJSONObj = eval('('+queStr+')');	
    		if(listJSONObj.length>0){
    			for(var i=0;i<listJSONObj.length;i++){				
    				if(listJSONObj[i].prtVendorId!=prtVendorId){//厂商ID不一致直接添加
    					newArray.push(listJSONObj[i]);
    				}else{////厂商ID一致重新组装数据，此时订货数量发生改变
    					var newProduct = {
    							"prtVendorId":prtVendorId,"unitCodeArr":unitCodeArr,
    							"barCodeArr":barCodeArr,"productName":productName,
    							"amount":amount,"distributionPrice":distributionPrice,
    							"quantityArr":quantityArr,"reasonArr":reasonArr
    						};//拼接新数据
    					newArray.push(newProduct);
    				}
    			}				
    		}
    		
    		queStr = JSON.stringify(newArray);
    		$("#queStr").val(queStr);	
    		sessionStorage.setItem('queStr', queStr);//将数据写入到sessionStorage
    }
}

/**
 * 选中不选中(点图片选择)
 * 
 * */
function changeBox(obj){
	var productImageUrl =obj;//当前产品图片
	var thisTD = $(obj).parent().parent();//表示当前单元格
	var productImageSelect = $(thisTD).find("#productImageSelect");//图片选中的样式div	
	var prtVendorId = $(thisTD).find("#prtVendorId").val();//当前单元格对应的产品厂商ID
	var unitCodeArr = $(thisTD).find("#unitCodeArr").val();//当前单元格对应的产品厂商编码
	var barCodeArr = $(thisTD).find("#barCodeArr").val();//当前单元格对应的产品条码
	var productName = $(thisTD).find("#productName").val();//当前单元格对应的产品名称
	var amount = $(thisTD).find("#amount").val();//当前单元格对应的库存数量
	var distributionPrice = $(thisTD).find("#distributionPrice").val();//当前单元格对应的订货价格
	var quantityArr = $(thisTD).find("#quantityArr").val();//当前单元格对应的订货数量
	var reasonArr = $(thisTD).find("#reasonArr").val();//当前单元格对应的备注
	
	var queStr = sessionStorage.getItem("queStr");//如果sessionStorage有值以sessionStorage的值为准
	if(isEmpty(queStr)){
		queStr = $("#queStr").val();//原来选中的数据
	}

	var newArray = new Array();//去掉已取消的数据以后的新数组
	var listJSONObj = eval('('+queStr+')');
	if($(productImageUrl).attr("class").indexOf("select")>=0){//选中改为不选中（删除选中的数据）
		$(productImageUrl).removeClass("select");
		$(productImageSelect).removeClass("select_icon");
		if(listJSONObj.length>0){
			for(var i=0;i<listJSONObj.length;i++){				
				if(listJSONObj[i].prtVendorId!=prtVendorId){//厂商ID一样移除当前产品
					newArray.push(listJSONObj[i]);
				}
			}				
		}
	}else{//不选中改为选中（新增选中的数据）
		$(productImageUrl).addClass("select");
		$(productImageSelect).addClass("select_icon");
		if(listJSONObj.length>0){
			for(var i=0;i<listJSONObj.length;i++){
				newArray.push(listJSONObj[i]);
			}				
		}		
		var newProduct = {
				"prtVendorId":prtVendorId,"unitCodeArr":unitCodeArr,
				"barCodeArr":barCodeArr,"productName":productName,
				"amount":amount,"distributionPrice":distributionPrice,
				"quantityArr":quantityArr,"reasonArr":reasonArr
			};//拼接新数据
		newArray.push(newProduct);
		
	}
	
	queStr = JSON.stringify(newArray);
	//alert(queStr);
	$("#queStr").val(queStr);	
	sessionStorage.setItem('queStr', queStr);//将数据写入到sessionStorage
	
}




/***
 * 保存产品到订单
 * */
function  productSave(){
	var queStr = sessionStorage.getItem("queStr");//如果sessionStorage有值以sessionStorage的值为准
	if(isEmpty(queStr)){
		queStr = $("#queStr").val();//原来选中的数据
	}

	var databody= window.opener.document.getElementById("databody");//父页面的数据主体
	//先删除原来的数据
	$(databody).find("tr").each(function(){
		$(this).remove();
	});
	
	var listJSONObj = eval('('+queStr+')');
	if(listJSONObj.length>0){		
		//重新添加新数据
		for(var i=0;i<listJSONObj.length;i++){		
			$(databody).append(getHtmlFun(listJSONObj[i]));
		}			
	}
	
	/***
	 * 点击确定按钮之后的处理
	 */
	// 改变奇偶行的样式
	window.opener.BINOLSTSFH22.changeOddEvenColor();
	//计算总金额总数量
	window.opener.BINOLSTSFH22.calcuTatol();
	// 设置全选状态
    var $checkboxs = $(window.opener.document.getElementById("databody")).find(':checkbox');
    var $unChecked = $(window.opener.document.getElementById("databody")).find(':checkbox:not(":checked")');
    if($unChecked.length == 0 && $checkboxs.length > 0){
	   $(window.opener.document).find('#allSelect').prop("checked",true);
    }else{
    	$(window.opener.document).find('#allSelect').prop("checked",false);
    }
	    
    window.opener.BINOLSTSFH22.bindInput();
	    
	window.close();		
}



/**
 * 向父页面追加的页面
 * */
function getHtmlFun(info){	
	var prtVendorId = info.prtVendorId;//产品厂商Id
	var unitCodeArr = info.unitCodeArr;//厂商编码
	var barCodeArr = info.barCodeArr;//产品条码
	var productName = info.productName;//产品名称
	var distributionPrice = info.distributionPrice;//订货价格
	var quantityArr=info.quantityArr;//订货数量
	var amount=info.amount;//库存数量
	var reasonArr=info.reasonArr;//备注
	
	
	if(isEmpty(distributionPrice)){
		distributionPrice = parseFloat("0.0");
	}else{
		distributionPrice = parseFloat(distributionPrice);
	}
	distributionPrice = distributionPrice.toFixed(2);
	
	if(isEmpty(quantityArr)){
		quantityArr = "1";
	}
	
	
	var totalPrice = distributionPrice * Number(quantityArr);//每个产品的总金额
	totalPrice = totalPrice.toFixed(2);
	if(isEmpty(reasonArr)){reasonArr = "";}
	
	var nextIndex = parseInt(window.opener.document.getElementById('rowNumber').value)+1;
	window.opener.document.getElementById('rowNumber').value=nextIndex;
	var html = '<tr id="dataRow'+nextIndex+'">';
	html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH22.changechkbox(this);"/></td>';
	html += '<td>' + unitCodeArr +'<input type="hidden" id="spanUnitCode"  value="'+ unitCodeArr + '"/></td>';
	html += '<td>' + barCodeArr + '<input type="hidden" id="spanBarCode" value="'+  barCodeArr + '"/></td>';
	html += '<td>' + productName + '</td>';
	html += '<td style="text-align:right;" >'+ distributionPrice + '</td>';
	html += '<td style="text-align:right;">'+amount+'</td>';
	html += '<td class="center"><input value="' + quantityArr +'" name="quantityArr" id="quantityArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTSFH22.calcuAmount(this)" oninput="BINOLSTSFH22.calcuAmount(this)"/></td>';
	html += '<td class="center" style="text-align:right;">' + totalPrice + '</td>';
	html += '<td class="center"><input value="' + reasonArr +'" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
	html +='<td style="display:none">'
		+'<input type="hidden" id="prtVendorId" name="prtVendorId" value="'+ prtVendorId + '"/>'
		+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+prtVendorId+'"/>'
		+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ distributionPrice +'"/>'
	    +'<input type="hidden" id="stockAmount" name="stockAmount" value="'+amount+'"/>'
	    +'<input type="hidden" id="unitCode" name="unitCode" value="'+unitCodeArr+'"/>'
	    +'<input type="hidden" id="referencePriceArr" name="referencePriceArr" value=""/></td></tr>';
	return html;
}


/**
 * 订货数量增减
 * */
function upOrdown(obj,flag){
	var thisTD = $(obj).parent().parent().parent();//表示当前单元格
	var productImageUrl = $(thisTD).find("#productImageUrl");//产品图片
	var number = $(thisTD).find("#quantityArr").val();//当前单元格对应的订货数量
	
	if(flag == "down"){//减数量
		if(!isEmpty(number)){			
			var intNumber = parseInt(number);
			if(intNumber>0){
				intNumber=intNumber-1;
				$(thisTD).find("#quantityArr").val(intNumber)
				
				if($(productImageUrl).attr("class").indexOf("select")>=0){//表示该数据已经选中，需要重新将数据放入到sessionStorage中
					
					var prtVendorId = $(thisTD).find("#prtVendorId").val();//当前单元格对应的产品厂商ID
					var unitCodeArr = $(thisTD).find("#unitCodeArr").val();//当前单元格对应的产品厂商编码
					var barCodeArr = $(thisTD).find("#barCodeArr").val();//当前单元格对应的产品条码
					var productName = $(thisTD).find("#productName").val();//当前单元格对应的产品名称
					var amount = $(thisTD).find("#amount").val();//当前单元格对应的库存数量
					var distributionPrice = $(thisTD).find("#distributionPrice").val();//当前单元格对应的订货价格
					var quantityArr = $(thisTD).find("#quantityArr").val();//当前单元格对应的订货数量
					var reasonArr = $(thisTD).find("#reasonArr").val();//当前单元格对应的备注
					
					var queStr = sessionStorage.getItem("queStr");//如果sessionStorage有值以sessionStorage的值为准
					if(isEmpty(queStr)){
						queStr = $("#queStr").val();//原来选中的数据
					}
					
					var newArray = new Array();//数据重组以后的新数组
					var listJSONObj = eval('('+queStr+')');	
					if(listJSONObj.length>0){
						for(var i=0;i<listJSONObj.length;i++){				
							if(listJSONObj[i].prtVendorId!=prtVendorId){//厂商ID不一致直接添加
								newArray.push(listJSONObj[i]);
							}else{////厂商ID一致重新组装数据，此时订货数量发生改变
								var newProduct = {
										"prtVendorId":prtVendorId,"unitCodeArr":unitCodeArr,
										"barCodeArr":barCodeArr,"productName":productName,
										"amount":amount,"distributionPrice":distributionPrice,
										"quantityArr":quantityArr,"reasonArr":reasonArr
								};//拼接新数据
								newArray.push(newProduct);
							}
						}				
					}
					
					queStr = JSON.stringify(newArray);
					$("#queStr").val(queStr);	
					sessionStorage.setItem('queStr', queStr);//将数据写入到sessionStorage
				}
			}
		}
	}else if(flag == "up"){//增库存
		if(isEmpty(number)){
			$(thisTD).find("#quantityArr").val("1");
		}else{
			var intNumber = parseInt(number);
			intNumber=intNumber+1;
			$(thisTD).find("#quantityArr").val(intNumber)			
		}
		
		if($(productImageUrl).attr("class").indexOf("select")>=0){//表示该数据已经选中，需要重新将数据放入到sessionStorage中
				
			var prtVendorId = $(thisTD).find("#prtVendorId").val();//当前单元格对应的产品厂商ID
			var unitCodeArr = $(thisTD).find("#unitCodeArr").val();//当前单元格对应的产品厂商编码
			var barCodeArr = $(thisTD).find("#barCodeArr").val();//当前单元格对应的产品条码
			var productName = $(thisTD).find("#productName").val();//当前单元格对应的产品名称
			var amount = $(thisTD).find("#amount").val();//当前单元格对应的库存数量
			var distributionPrice = $(thisTD).find("#distributionPrice").val();//当前单元格对应的订货价格
			var quantityArr = $(thisTD).find("#quantityArr").val();//当前单元格对应的订货数量
			var reasonArr = $(thisTD).find("#reasonArr").val();//当前单元格对应的备注
			
			var queStr = sessionStorage.getItem("queStr");//如果sessionStorage有值以sessionStorage的值为准
			if(isEmpty(queStr)){
				queStr = $("#queStr").val();//原来选中的数据
			}
			
			var newArray = new Array();//数据重组以后的新数组
			var listJSONObj = eval('('+queStr+')');	
			if(listJSONObj.length>0){
				for(var i=0;i<listJSONObj.length;i++){				
					if(listJSONObj[i].prtVendorId!=prtVendorId){//厂商ID不一致直接添加
						newArray.push(listJSONObj[i]);
					}else{////厂商ID一致重新组装数据，此时订货数量发生改变
						var newProduct = {
								"prtVendorId":prtVendorId,"unitCodeArr":unitCodeArr,
								"barCodeArr":barCodeArr,"productName":productName,
								"amount":amount,"distributionPrice":distributionPrice,
								"quantityArr":quantityArr,"reasonArr":reasonArr
						};//拼接新数据
						newArray.push(newProduct);
					}
				}				
			}
			
			queStr = JSON.stringify(newArray);
			$("#queStr").val(queStr);	
			sessionStorage.setItem('queStr', queStr);//将数据写入到sessionStorage
		}		
	}
	
    
}


//新品check
function  newProductCheck(obj){
	if($(obj).attr("checked")){//选中
		$("#isNewProductFlag").val("1");
	}else{//不选中
		$("#isNewProductFlag").val("");
	}
}



