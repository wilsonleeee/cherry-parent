var binOLSTSFH03_global = {};
//是否需要解锁
binOLSTSFH03_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

$(document).ready(function() {
	initDetailTable();
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	$("#exportButton").prop("disabled",false);
	$('.tabs').tabs();
} );

var BINOLSTSFH03 = function () {};
BINOLSTSFH03.prototype = {
//	"STSFH03_dialogBody":"",

	/**
	 * 发货部门弹出框
	 * @return
	 */
	"openDepartBox":function(thisObj){
	 	//取得所有部门类型
	 	var departType = "";
	 	for(var i=0;i<$("#departTypePop option").length;i++){
	 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
	 		//排除4柜台
	 		if(departTypeValue != "4"){
	 			departType += "&departType="+departTypeValue;
	 		}
	 	}
		
	 	var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType+"&testType="+$("#inTestType").val();
//	 	var param = "checkType=radio&businessType=30&depotId="+$("#indepotID").val()+"&inOutFlag=IN&departId="+$("#inOrganizationId").val();
	 	
	 	var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text().escapeHTML();
				var departName = $selected.find("td:eq(2)").text().escapeHTML();
				$("#outOrganizationID").val(departId);
				$("#outOrgName").text("("+departCode.unEscapeHTML()+")"+departName.unEscapeHTML());
				BINOLSTSFH03.chooseDepart();
			}
			
		};
		popDataTableOfDepart(thisObj,param,callback);
//		popDepartTableByBusinessType(thisObj,param,callback);
	},

	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){	
		BINOLSTSFH03.clearActionMsg();
		var organizationid = $('#outOrganizationID').val();
		var param ="organizationid="+organizationid;
		
		//更改了部门后，取得该部门所拥有的仓库
		var csrftoken = $('#csrftoken').val();
		if(csrftoken == undefined){
			csrftoken = parentTokenVal();
		}
		var url = $('#urlgetdepotAjax').html()+"?csrftoken="+csrftoken;
		ajaxRequest(url,param,this.changeDepotDropDownList);
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){	
		//data为json格式的文本字符串	
		var member = eval("("+data+")");    //包数据解析为json 格式  
		//$("#outDepotInfoId").find("option:not(:first)").remove();
		$("#outDepotInfoId").find("option").remove();
		$.each(member, function(i){
			$("#outDepotInfoId ").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});
		if($("#outDepotInfoId option").length>0){
			$("#outDepotInfoId").get(0).selectedIndex=0;
		}
		BINOLSTSFH03.refreshStockCount();
	},
	
	//刷新显示库存
	"refreshStockCount":function(){
		if($("#outDepotInfoId").val() == null || $("#outDepotInfoId").val() == undefined){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
			return false;
		}else{
			$('#errorDiv2').hide();
		}
		
		//修改发货仓库、逻辑仓库隐藏项
		$("#depotInfoIdAccept").val($("#outDepotInfoId").val());
		$("#logicDepotsInfoIdAccept").val($("#outLogicDepotsInfoId").val());
		
		var stockUrl = $('#s_getStockCount').html();
		var params = "&depotInfoId="+$("#outDepotInfoId").val()+"&logicDepotsInfoId="+$("#outLogicDepotsInfoId").val();
		params += "&operateType="+$("#operateType").val();
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				var index = $(this).attr("id").substr(7);
				var productVendorId = $(this).find("[name=productVendorIDArr]").val();
				params += "&currentIndex="+index+"&productVendorId="+productVendorId;
				
				//重置详细的发货仓库ID，发货逻辑仓库ID
				$(this).find("[name=inventoryInfoIDAcceptArr]").val($("#outDepotInfoId").val());
				$(this).find("[name=logicInventoryInfoIDAcceptArr]").val($("#outLogicDepotsInfoId").val());
				
				//成本价相关
				var quantityuArr = $(this).find(":input[name='quantityArr']").val();//发货数量
				if(quantityuArr !="" && quantityuArr !=null && quantityuArr != undefined && !isNaN(quantityuArr)){//表示有发货数量，此时需要计算出对应的成本价
					params += "&quantityArr="+quantityuArr;
				}else{
					params += "&quantityArr=0";
				}
			}
		});
		var operateType = $("#operateType").val();
		var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
				
		var callback = function(msg){
			var member = eval("("+msg+")");
			$.each(member, function(i){
				//发货方库存
				$('#dataRow'+member[i].currentIndex +' > #dataTd6').html(BINOLSTSFH03.formateMoney(member[i].Quantity,0));
				//成本价相关
				if(useCostPrice == "1" && operateType == "40"){//可以发货的编辑页面
					if(member[i].costPrice!=null && member[i].costPrice!="" && !isNaN(member[i].costPrice)){
						$('#dataRow'+member[i].currentIndex).find(":input[name='costPriceArr']").val(parseFloat(member[i].costPrice).toFixed(2));
						$('#dataRow'+member[i].currentIndex).find("div[id=costPrice]").html(parseFloat(member[i].costPrice).toFixed(2));
						if(useCostPrice == "1"){//执行价需要按成本价计算
							$('#dataRow'+member[i].currentIndex).find(":input[name='priceUnitArr']").val(parseFloat(member[i].costPrice).toFixed(2));
							$('#dataRow'+member[i].currentIndex).find("#priceArr").val(parseFloat(member[i].costPrice).toFixed(2));
							
							var price = $('#dataRow'+member[i].currentIndex).find(":input[name='priceUnitArr']").val();
							if(price == undefined || price == ""){
								price = "0.00";
							}
							
							var quantityuArr =  $('#dataRow'+member[i].currentIndex).find(":input[name='quantityArr']").val();
							
							var amount = price * Number(quantityuArr);
							$('#dataRow'+member[i].currentIndex).find("#money").text(BINOLSTSFH03.formateMoney(amount,2));
						}
					}else{
						$('#dataRow'+member[i].currentIndex).find(":input[name='costPriceArr']").val("");
						$('#dataRow'+member[i].currentIndex).find("div[id=costPrice]").html("");
						if(useCostPrice == "1"){//执行价需要按成本价计算
							$('#dataRow'+member[i].currentIndex).find(":input[name='priceUnitArr']").val(parseFloat("0.0").toFixed(2));
							$('#dataRow'+member[i].currentIndex).find("#priceArr").val(parseFloat("0.0").toFixed(2));
							$('#dataRow'+member[i].currentIndex).find("#money").text("0.00");
						}
					}
					
					if(member[i].totalCostPrice!=null && member[i].totalCostPrice!="" && !isNaN(member[i].totalCostPrice)){
						$('#dataRow'+member[i].currentIndex).find("div[id=totalCostPrice]").html(parseFloat(member[i].totalCostPrice).toFixed(2));					
					}else{
						$('#dataRow'+member[i].currentIndex).find("div[id=totalCostPrice]").html("");
					}
					
				}
			});
			//成本价相关
			if(useCostPrice == "1" && operateType == "40"){//可以发货的编辑页面
				BINOLSTSFH03.calcTotal();
			}

		};
		
		cherryAjaxRequest({
			url:stockUrl,
			reloadFlg:true,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 产品弹出框
	 * @return
	 */
	"openProPopup":function(_this){
		this.clearActionMsg();
		this.changeOddEvenColor();
//		var proPopParam = {
//				thisObj : _this,
//				  index : 0,
//			  checkType : "checkbox",
//			      modal : true,
//			  autoClose : [],
//			 dialogBody : BINOLSTSFH03.STSFH03_dialogBody,
//			 csrftoken  : "csrftoken="+window.opener.document.getElementById("csrftoken").value
//		};
//		popDataTableOfPrtInfo(proPopParam);
		
		var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
		var operateType = $("#operateType").val();
		// 产品弹出框属性设置
		var option = {
			targetId: "databody",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			brandInfoId : $("#BIN_BrandInfoID").val(),// 品牌ID
			getHtmlFun:function(info){// 目标区追加数据行function
				var rowNumber = Math.abs($('#rowNumber').val())+1;
				$('#rowNumber').val(rowNumber);
				var index = $('#rowNumber').val();
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTSFH03.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd4">'+info.barCode+'</td>';
				html += '<td id="dataTd3">'+info.nameTotal+'</td>';
				html += '<td id="dataTdPCB">'+info.primaryCategoryBig+'</td>';
				//html += '<td id="dataTdPCM">'+""+'</td>';
				html += '<td id="dataTdPCS">'+info.primaryCategorySmall+'</td>';
				html += '<td id="dataTd5" class="alignRight">'+BINOLSTSFH03.formateMoney(info.salePrice,2)+'</td>';
				if(useCostPrice == "1"){//需要显示成本价，总成本字段
					html += '<td id="tdCostPrice"><div id="costPrice"></div></td>';
					html += '<td id="tdTotalCostPrice"><div id="totalCostPrice"></div></td>';
				}
				else {//不需要显示成本价，总成本字段
					html += '<td id="tdCostPrice" style="display: none;"><div id="costPrice"></div></td>';
					html += '<td id="tdTotalCostPrice" style="display: none;"><div id="totalCostPrice"></div></td>';
				}
				if($("#thDiscountPrice").is(":visible")){
					html += '<td id="tdDiscountPrice" class="alignRight" style="width:10%;">';
					if(useCostPrice == "1"){//执行价按成本价计算
						html += '<input id="priceArr" class="price" type="text" onchange="BINOLSTSFH03.setPrice(this);return false;" value="0.00" maxlength="9" size="10">';
					}
					else {//执行价按单价计算
						html += '<input id="priceArr" class="price" type="text" onchange="BINOLSTSFH03.setPrice(this);return false;" value="'+info.salePrice.toFixed("2")+'" maxlength="9" size="10">';
					}				
					html += ' '+'<span id="spanCalPrice" class=""><span class="calculator" title="'+$("#spanCalTitle").text()+'" onclick="BINOLSTSFH03.initRateDiv(this);"></span></span>';
					html += '<div id="hidePriceArr" class="hide">'+info.salePrice+'</div>';
					html += '</td>';
				}
				if($("#th_OrderStock").is(":visible")){
					html += '<td id="dataTdOrderStock" class="alignRight"></td>';
				}
				if($("#th_ProductQuantity").is(":visible")){
					html += '<td id="dataTd6" class="alignRight"></td>';
				}
				if($("#th_SaleQuantity").is(":visible")){
					html += '<td id="dataTdSaleQuantity" class="alignRight"></td>';
				}
				html += '<td id="dataTd7" class="alignRight"></td>';
				html += '<td id="dataTdApplyQuantity"  class="alignRight">0</td>';
				html += '<td id="nowCount" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTSFH03.changeCount(this);"  onkeyup="BINOLSTSFH03.changeCount(this);"/></td>';
				html += '<td id="money" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" value="'+info.salePrice+'" name="referencePriceArr">';
				if(useCostPrice == "1"){//执行价按成本价计算
					html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="0.00"/>';
				}
				else {//执行价按单价计算
					html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.salePrice+'"/>';
				}				
//				html += '<input type="hidden" id="priceTotalArr'+index+'" name="priceTotalArr" value="0.00"/>';
				html += '<input type="hidden" id="suggestedQuantityArr'+index+'" name="suggestedQuantityArr" value="0"/>';
				html += '<input type="hidden" id="applyQuantityArr'+index+'" name="applyQuantityArr" value="0"/>';
//				html += '<input type="hidden" id="unitCodeArr'+index+'" name="unitCodeArr" value="'+info.unitCode+'"/>';
//				html += '<input type="hidden" id="barCodeArr'+index+'" name="barCodeArr" value="'+info.barCode+'"/>';
				html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
				html += '<input type="hidden" id="productVendorPackageIDArr'+index+'" name="productVendorPackageIDArr" value="0"/>';
				html += '<input type="hidden" id="inventoryInfoIDArr'+index+'" name="inventoryInfoIDArr" value="'+$("#inventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="logicInventoryInfoIDArr'+index+'" name="logicInventoryInfoIDArr" value="'+$("#logicInventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="storageLocationInfoIDArr'+index+'" name="storageLocationInfoIDArr" value="0"/>';
				html += '<input type="hidden" id="inventoryInfoIDAcceptArr'+index+'" name="inventoryInfoIDAcceptArr" value="'+$("#outDepotInfoId").val()+'"/>';
				html += '<input type="hidden" id="logicInventoryInfoIDAcceptArr'+index+'" name="logicInventoryInfoIDAcceptArr" value="'+$("#outLogicDepotsInfoId").val()+'"/>';
				html += '<input type="hidden" name="prtVendorId" value="'+info.proId+'"/>';
				html += '<input type="hidden" id="costPriceArr" name="costPriceArr" value=""/>';
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
				//重置序号
				$.each($('#databody >tr'), function(i){	
					var index = $(this).attr("id").substr(7);
					$("#dataRow"+index+" td:eq(1)").html(i+1);
				});
				BINOLSTSFH03.changeOddEvenColor();
				BINOLSTSFH03.calcTotal();
				//ajax取得选中产品的库存数量
				var param ='depotInfoId='+$('#outDepotInfoId').val()+'&logicDepotsInfoId='+$("#outLogicDepotsInfoId").val();
				param += '&depotInfoIDIn='+$('#inventoryInfoID').val()+'&logicDepotsInfoIDIn='+$("#logicInventoryInfoID").val();
				param += '&searchSaleQuantity=true&searchSuggestedQuantity=true&organizationId='+$("#inOrganizationID").val()+'&brandInfoId='+$("#BIN_BrandInfoID").val()+'&orderTime='+$("#orderTime").val();
				param += '&operateType='+$("#operateType").val();
				var len = $("#databody tr").length;
				if(len>0){
					for(var i=0;i<len;i++){
						var productVendorID = $($("#databody tr [name=prtVendorId]")[i]).val();
						var currentIndex = $($("#databody tr :checkbox")[i]).val();
						param += "&productVendorId="+productVendorID;
						param += "&currentIndex="+currentIndex;
					}
					cherryAjaxRequest({
						url:$("#s_getStockCount").html(),
						reloadFlg:true,
						param:param,
						callback:BINOLSTSFH03.getStockSuccess
					});
				}
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},

	/**
	 * 删除掉画面头部的提示信息框
	 * @return
	 */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},
	
	/**
	 * 全选，全不选
	 */
	"selectAll":function(){
		if($('#allSelect').prop("checked")){
			$("#databody :checkbox").each(function(){
				if($(this).val()!=0){
					$(this).prop("checked",true);	
					}
				
				});
			}
		else{
			$("#databody :checkbox").each(function(){
				$(this).prop("checked",false);});
		}
	},

	/**
	 * 点击了选择框后，来控制全选框
	 * @return
	 */
	"changechkbox":function(obj){
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
	},
	
//	/**
//	 * 新增一行
//	 * @return
//	 */
//	"addRow":function(){
//		//this.clearActionMsg();
//		var rowNumber = Math.abs($('#rowNumber').val())+1;
//		var newid = 'dataRow'+rowNumber;
//		$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");	
//		$('#'+newid).find(':checkbox').val(rowNumber);
//		$('#'+newid).find('#unitCodeArr').attr('id','unitCodeArr'+rowNumber);
//		$('#'+newid).find('#barCodeArr').attr('id','barCodeArr'+rowNumber);
//		$('#'+newid).find('#productVendorIDArr').attr('id','productVendorIDArr'+rowNumber);
//		$('#'+newid).find('#suggestedQuantityArr').attr('id','suggestedQuantityArr'+rowNumber);
//		$('#'+newid).find('#inventoryInfoIDArr').attr('id','inventoryInfoIDArr'+rowNumber);
//		$('#'+newid).find('#logicInventoryInfoIDArr').attr('id','logicInventoryInfoIDArr'+rowNumber);
//		$('#'+newid).find('#storageLocationInfoIDArr').attr('id','storageLocationInfoIDArr'+rowNumber);
//		$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//		$('#'+newid).find('#inventoryInfoIDAcceptArr').attr('id','inventoryInfoIDAcceptArr'+rowNumber);
//		$('#'+newid).find('#logicInventoryInfoIDAcceptArr').attr('id','logicInventoryInfoIDAcceptArr'+rowNumber);
//		$('#rowNumber').val(rowNumber);
//		
//		$('#'+newid).find('#inventoryInfoIDArr'+rowNumber).val($("#inventoryInfoID").val());
//		$('#'+newid).find('#logicInventoryInfoIDArr'+rowNumber).val($("#logicInventoryInfoID").val());
//		$('#'+newid).find('#inventoryInfoIDAcceptArr'+rowNumber).val($("#outDepotInfoId").val());
//		$('#'+newid).find('#logicInventoryInfoIDAcceptArr'+rowNumber).val($("#outLogicDepotsInfoId").val());
//
//	},
	
	/**
	 * 删除选中行
	 * @return
	 */
	"deleteRow":function(){
		this.clearActionMsg();
		$("#databody :checkbox").each(function(){
			if($(this).prop("checked"))
				{
					$('#dataRow' + $(this).val()).remove();	
				}					
			});
		//重置序号
		$.each($('#databody >tr'), function(i){	
			var index = $(this).attr("id").substr(7);
			$("#dataRow"+index+" td:eq(1)").html(i+1);
		});	
		this.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		BINOLSTSFH03.calcTotal();
	},
	
	"changeOddEvenColor":function(){
		//$("#databody tr:odd").attr("class","even");
		//$("#databody tr:even").attr("class","odd");
		$("#databody tr").removeClass("even");
		$("#databody tr").removeClass("odd");
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
	},
	
	"changeOddEvenColor1":function(){
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
	},
	
	/**
	 * 提交前对数据进行检查
	 * @returns {Boolean}
	 */
	"checkData":function(){
		var flag = true;
		var count= 0;
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
			count +=1;
			if($(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0"){
				flag = false;
				$(this).attr("class","errTRbgColor");
			}else{
				$(this).removeClass('errTRbgColor');
			}		
		});	
		this.changeOddEvenColor1();
		if(!flag){
			//有空行存在		
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00008').val());
			$('#errorDiv2').show();
			return flag;
		}
		if(count==0){
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00009').val());
			$('#errorDiv2').show();
			return flag;
		}
		return flag;
	},
	
	/**
	 * 保存前对数据进行检查（允许不输入数量）
	 * @returns {Boolean}
	 */
	"checkSaveData":function(){
		var flag = true;
		this.changeOddEvenColor1();
		if($('#databody >tr').length == 0){
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00009').val());
			$('#errorDiv2').show();
			return flag;
		}
		return flag;
	},
	
	/**
	 * 输入了订货数量
	 * @param thisObj
	 */
	"changeCount1":function(thisObj){
		var $tr_obj = $(thisObj).parent().parent();
		var trID = $tr_obj.attr('id');
		var index = trID.substr(7);
		var count = Math.abs(parseInt($(thisObj).val(),10));
		var price = $("#"+trID).find("[name='priceUnitArr']").val();
		if(isNaN(count)){
			if($(thisObj).val()!="-"){
				$(thisObj).val("0");
				$("#"+trID + " > #money").html("0.00");
				BINOLSTSFH03.calcTotal();
				BINOLSTSFH03.checkAbnormalQuantity($tr_obj,$(thisObj).val());
				return;
			}
		}	
		$(thisObj).val(count);
		$("#"+trID + " > #money").html(this.formateMoney(count*price,2));
		
		BINOLSTSFH03.calcTotal();
		
		BINOLSTSFH03.checkAbnormalQuantity($tr_obj,count);
	},
	
	/**
	 * 计算金额
	 * 
	 * */
	"changeCount":function (obj){
		var $tr_obj = $(obj).parent().parent();
		var $this = $(obj);
		var $thisTd = $this.parent();
		var TrID = $thisTd.parent().attr("id");
		var $thisVal =$this.val().toString();
		
		var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
		var operateType = $("#operateType").val();
	
		if($thisVal == ""){
			$("#"+TrID).find(":input[name='costPriceArr']").val("");
			$("#"+TrID).find("div[id=costPrice]").html("");
			$("#"+TrID).find("#totalCostPrice").html("");
			$("#"+TrID).find("#money").html("0.00");
			
			if(useCostPrice == "1"){				
				$("#"+TrID).find("#priceUnitArr").val("0.00");
				$("#"+TrID).find("#priceArr").val("0.00");
			}
			
		}else if(isNaN($thisVal)){		
				if($thisVal!="-"){
					$this.val("0");
					$("#"+TrID).find(":input[name='costPriceArr']").val("");
					$("#"+TrID).find("div[id=costPrice]").html("");
					$("#"+TrID).find("#totalCostPrice").html("");
					$("#"+TrID).find("#money").html("0.00");
					if(operateType == "40" && useCostPrice == "1"){			
						$("#"+TrID).find("#priceUnitArr").val("0.00");
						$("#"+TrID).find("#priceArr").val("0.00");
					}
				}
		}else{
			var price = $this.parent().parent().find(":input[name='priceUnitArr']").val();
			if(price == undefined || price == ""){
				price = "0.00";
			}
			//IE、Chrome下，由onkeyup事件触发给文本框设置值会影响切换到下一文本框后无法全选，所以作如下处理。
			var quantity = parseInt(Number($this.val()));
			if($thisVal.toString() != quantity.toString()){
				$this.val(parseInt(Number($this.val())));
			}
			var amount = price * Number($this.val());
			$thisTd.next().text(BINOLSTSFH03.formateMoney(amount,2));
		}
		BINOLSTSFH03.calcTotal();		
		BINOLSTSFH03.checkAbnormalQuantity($tr_obj,$thisVal);
		
		/**以下是 根据发货数量动态取对应的成本价**/
		if(operateType != "2"){
			var getCostPriceURL = $("#getCostPriceURL").html();//得到产品对应的平均成本价
			var productVendorId = $("#"+TrID).find(":input[name='prtVendorId']").val();
			var priceUnitArr = $("#"+TrID).find(":input[name='priceUnitArr']").val();//实际执行价
			var quantityuArr = $("#"+TrID).find(":input[name='quantityArr']").val();//发货数量
			
			var param = "productVendorId="+productVendorId;
				param += "&outDepotId="+$("#outDepotInfoId option:selected").val();
				param += "&outLoginDepotId="+$("#outLogicDepotsInfoId option:selected").val();
				param += "&prtCount="+quantityuArr;
				
				//alert(param);
			
			if(quantityuArr !="" && quantityuArr !=null 
					&& quantityuArr != undefined 
					&& !isNaN(quantityuArr) && productVendorId!=""){//表示有发货数量，此时需要计算出对应的成本价
				cherryAjaxRequest({
					url: getCostPriceURL,
					param:param,
					callback: function(msg) {
							var data = eval("("+msg+")");    //包数据解析为json 格式  
							if(data.costPrice!=null && data.costPrice!="" && !isNaN(data.costPrice)){
								$("#"+TrID).find(":input[name='costPriceArr']").val(parseFloat(data.costPrice).toFixed(2));
								$("#"+TrID).find("div[id=costPrice]").html(parseFloat(data.costPrice).toFixed(2));
								if(operateType == "40" && useCostPrice == "1"){	
									$("#"+TrID).find(":input[name='priceUnitArr']").val(parseFloat(data.costPrice).toFixed(2));
									$("#"+TrID).find("#priceArr").val(parseFloat(data.costPrice).toFixed(2));
								}
							}else{
								$("#"+TrID).find(":input[name='costPriceArr']").val("");
								$("#"+TrID).find("div[id=costPrice]").html("");
								if(operateType == "40" && useCostPrice == "1"){	
									$("#"+TrID).find(":input[name='priceUnitArr']").val(parseFloat("0.0").toFixed(2));
									$("#"+TrID).find("#priceArr").val(parseFloat("0.0").toFixed(2));
								}
							}
							
							if(data.totalCostPrice!=null && data.totalCostPrice!="" && !isNaN(data.totalCostPrice)){
								$("#"+TrID).find("div[id=totalCostPrice]").html(parseFloat(data.totalCostPrice).toFixed(2));					
							}else{
								$("#"+TrID).find("div[id=totalCostPrice]").html("");
							}
						var price = $this.parent().parent().find(":input[name='priceUnitArr']").val();
						if(price == undefined || price == ""){
							price = "0.00";
						}
						//IE、Chrome下，由onkeyup事件触发给文本框设置值会影响切换到下一文本框后无法全选，所以作如下处理。
						var quantity = parseInt(Number($this.val()));
						if($thisVal.toString() != quantity.toString()){
							$this.val(parseInt(Number($this.val())));
						}
						var amount = price * Number($this.val());
						$thisTd.next().text(BINOLSTSFH03.formateMoney(amount,2));
						
						
						BINOLSTSFH03.calcTotal();		
						BINOLSTSFH03.checkAbnormalQuantity($tr_obj,$thisVal);
					}
				});
			}
		}
	},
	/**
	 * 异常数量与建议数量检查，异常背景色显示红色
	 */
	"checkAbnormalQuantity":function(obj,count){
		//计划订量是否在建议数量范围内
		var maxPercent = $("#maxPercent").val();
		var minPercent = $("#minPercent").val();
		var suggestedQuantity = obj.find("[name='suggestedQuantityArr']").val();
		var maxQuantity = suggestedQuantity;
		var minQuantity = suggestedQuantity;
		if(maxPercent != ""){
			maxQuantity = suggestedQuantity*maxPercent/100.0;
		}
		if(minPercent != ""){
			minQuantity = suggestedQuantity*minPercent/100.0;
		}
		if(count.toString() == "" || (count >= minQuantity && count <= maxQuantity)){
			obj.removeClass("abnormal");
		}else{
			obj.addClass("abnormal");
		}
	},
	
	/**
	 * 暂存
	 */
	"saveTemp" : function() {
		var that = this;
		that.clearActionMsg();
		if (!this.checkSaveData()) {
			return false;
		}
		var url = $("#saveTempUrl").attr("href");
		var params = $('#mainForm').formSerialize();

		var callback = function(msg) {
			that.back();
		};
		cherryAjaxRequest({
			url : url,
			param : params,
			callback : callback
		});
	},
	
	/*
	 * 保存订单
	 */
	"saveOrder":function(){
		this.clearActionMsg();
		if(!this.checkSaveData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#saveUrl").attr("href");
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/*
	 * 提交订单
	 */
	"submitOrder":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#submitUrl").attr("href");
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/*
	 * 删除订单
	 */
	"deleteOrder":function(){
		this.clearActionMsg();
		var params=$('#mainForm').formSerialize();
		var url = $("#deleteUrl").attr("href");
		var callback = function(msg){
			if(oTableArr[1] != null)oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 解析库存JSON
	 * @param msg
	 */
	"getStockSuccess":function(msg){
		var member = eval("("+msg+")"); //数据解析为json 格式  	
		$.each(member, function(i){
			$('#hasproductflagArr'+member[i].currentIndex).val(member[i].hasproductflag);
			$('#dataRow'+member[i].currentIndex +' > #dataTd6').html(BINOLSTSFH03.formateMoney(member[i].Quantity,0));
			$('#dataRow'+member[i].currentIndex +' > #dataTdOrderStock').html(BINOLSTSFH03.formateMoney(member[i].OrderStock,0));
			if(member[i].SaleQuantity != undefined){
				$('#dataRow'+member[i].currentIndex +' > #dataTdSaleQuantity').html(BINOLSTSFH03.formateMoney(member[i].SaleQuantity,0));
			}
			if(member[i].SuggestedQuantity != undefined){
				$('#dataRow'+member[i].currentIndex +' > #dataTd7').html(BINOLSTSFH03.formateMoney(member[i].SuggestedQuantity,0));
				$('#dataRow'+member[i].currentIndex).find("[name='suggestedQuantityArr']").val(member[i].SuggestedQuantity);
				var count = $('#dataRow'+member[i].currentIndex +' #quantityArr').val();
				BINOLSTSFH03.checkAbnormalQuantity($('#dataRow'+member[i].currentIndex),count);
			}
		});
	},
	
	/**
	 * 开放修改单据
	 * param 定义 以逗号分隔 HIDEDISCOUNTPRICE 隐藏折扣价格 HIDESD 隐藏发货、发货拒绝按钮显示提交按钮
	 * @return
	 */
	"modifyOrder":function(param){
		if(param == undefined){
			param = "";
		}
		var operateType = $("#operateType").val();
		var useCostPrice = $("#useCostPrice").val();
		if(operateType != "40"){
			$("#showOrderTypeSelect").removeClass("hide");
			$("#hideOrderType").addClass("hide");			
		}
		
		if(operateType == "40"){
			$("#showMainComments").removeClass("hide");
		}
		// 标记当前为编辑状态
		$("#modifiedFlag").val("1");
		
		if(useCostPrice == "1"){
				//显示平均成本和总成本字段
				$("#table_scroll").find("table").find("tr").each(function(){
					$(this).find("#tdCostPrice").css("display","");
					$(this).find("#tdTotalCostPrice").css("display","");
				});
				
				//计算每一行的金额，成本价，折扣金额
				$("#databody").find("tr").each(function(){
					var referencePriceArr = $(this).find("[name='referencePriceArr']").val();//单价
					var costPriceArr = $(this).find("[name='costPriceArr']").val();//成本价
					var quantityArr = $(this).find("[name='quantityArr']").val();//发货数量
					var editPriceArr = $(this).find("[name='editPriceArr']").val();//上次的编辑价格
					
					if(!isEmpty(costPriceArr)){//成本价不为空的情况
						$(this).find("#priceArr").val(costPriceArr);
						$(this).find("[name='priceUnitArr']").val(costPriceArr);
						
						if(!isEmpty(quantityArr)){
							if(parseFloat(costPriceArr) != 0 && parseInt(quantityArr) != 0){
								$(this).find("#money").text(((parseFloat(costPriceArr))*(parseFloat(quantityArr))).toFixed(2));//每一明细行的金额
							}
							else{
								$(this).find("#money").text("0.00");//每一明细行的金额
							}
						}
					}else{//成本价为空的情况
						$(this).find("#priceArr").val("0.00");
						$(this).find("[name='priceUnitArr']").val("0.00");
						// 成本价置为0
//						$(this).find("[name='costPriceArr']").val("0.00");
						if(!isEmpty(quantityArr)){
							$(this).find("#money").text("0.00");//每一明细行的金额
						}
					}	
					
					if(!isEmpty(editPriceArr)) {// 上次的编辑价格
						$(this).find("#priceArr").val(editPriceArr);
						$(this).find("[name='priceUnitArr']").val(editPriceArr);
						
						if(!isEmpty(quantityArr)){
							if(parseFloat(editPriceArr) != 0 && parseInt(quantityArr) != 0){
								$(this).find("#money").text(((parseFloat(editPriceArr))*(parseFloat(quantityArr))).toFixed(2));//每一明细行的金额
							} else {
								$(this).find("#money").text("0.00");//每一明细行的金额
							}
						}
					} 
				});
				
				
				var totalAmount = 0.00;				
				//统计每一行的金额
				$("#databody").find("tr").each(function(){
					if($(this).find("#money").length>0){
						totalAmount += parseFloat($(this).find("#money").html().replace(/,/g,""));
					}
				});
				$("#totalAmount").text(totalAmount.toFixed(2));//设置总金额
				$("#totalAmountCheck").val(totalAmount.toFixed(2));//设置总金额
			
		}
		
		$("#showBtnopenDepartBox").removeClass("hide");
		$("#showAcceptDepotCodeName").removeClass("hide");
		$("#showAcceptLogicInventoryName").removeClass("hide");
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$("#showAddBtn").removeClass("hide");
		$("#showDelBtn").removeClass("hide");
		$("#showEditExpectDeliverDate").removeClass("hide");

		
		$("#hideAcceptDepotCodeName").addClass("hide");
		$("#hideAcceptLogicInventoryName").addClass("hide");
		$("#hideEditExpectDeliverDate").addClass("hide");
		
		if(param.indexOf("HIDEDISCOUNTPRICE") == -1){
			$("#thDiscountPrice").removeClass("hide");
		}
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("#dataTd6").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
			
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideComments").addClass("hide");
			
			if(param.indexOf("HIDEDISCOUNTPRICE") == -1){
				// 50：收货操作
				if($("#operateType").val() != "50"){
					$(this).find("#tdDiscountPrice").removeClass("hide");
					$(this).find("#spanCalPrice").removeClass("hide");
					$(this).find("#priceArr").removeClass("hide");
					$(this).find("#hidePriceArr").addClass("hide");
				}
			}
		});
		// 暂存按钮
		$("#btnSaveTemp").css("display","");
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		$("#btnSave").css("display","");
		$("#btn-icon-discard").css("display","none");
		
		//如果修改发货数量后需要重新提交审核，需要在工作流修改按钮OS_ButtonScript传入参数HIDESD。 
		if(param.indexOf("HIDESD") > -1){
			$("#btn-icon-confirm").remove();
			$("#btn-icon-inhibit").remove();
			$("#btnSDSubmit").css("display","");
		}
		return "";
	},
	
	"back":function(){
		var operateType = $("#operateType").val();
		$("#modifiedFlag").val("0");//表示数据没有被修改过
		
		binOLSTSFH03_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#productOrderDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#productOrderDetailUrl').submit();
	},
	
	/*
	 * 用逗号分割金额、数量
	 */
	"formateMoney":function(money,num){
		money = parseFloat(money);
		money = String(money.toFixed(num));
		var re = /(-?\d+)(\d{3})/;
		while(re.test(money))
			money = money.replace(re,"$1,$2");
		return money;
	},
	
	/*
	 * 计算总金额，总数量
	 */
	"calcTotal":function(){
		//计算总金额、总数量
		var rows = $("#databody").children();
		var totalSuggestedQuantity = 0;
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var quantity = Number($(this).find("#quantityArr").val());
				var amount = Number($(this).find("[name='priceUnitArr']").val());
				var suggestedQuantity = 0;
				if($(this).find("#dataTd7").html() != ""){
					suggestedQuantity = Number($(this).find("#dataTd7").html().replace(",",""));
				}
				if(isNaN(quantity)){
					quantity=0;
				}
				totalQuantity += Number(quantity);
				if($(this).find("#money").length>0){
					totalAmount += parseFloat($(this).find("#money").html().replace(/,/g,""));
				}
//				totalQuantity += quantity;
//				totalAmount += quantity * amount;
				totalSuggestedQuantity += suggestedQuantity;
			});
		}
		$("#totalSuggestedQuantity").html(this.formateMoney(totalSuggestedQuantity,0));
		$("#totalQuantity").html(this.formateMoney(totalQuantity,0));
		$("#totalAmount").html(this.formateMoney(totalAmount,2));
		$("#totalAmountCheck").val(totalAmount);
	},
	
	"refreshOrderType":function(obj){
		var $this = $(obj);
		$("#orderType").val($this.val());
	},
	
	"openProStockPopup":function(obj){
		var option = {};
		option.param = "entryID="+$('#entryID').val()+"&"+$('[name=productVendorIDArr]').serialize()+"&productOrderID="+$("#productOrderID").val();
		popAjaxProStockDialog(option);
	},

	 // 格式化折扣价格及重新计算金额
	"setPrice":function(obj){
		var $this = $(obj);
		var $tr_obj = $this.parent().parent();

		// 折扣价
		var $discountPrice = $tr_obj.find("#priceArr");
		var discountPrice = parseFloat($this.val());

		if(isNaN(discountPrice)){
			discountPrice = 0;
		}
		$discountPrice.val(discountPrice.toFixed("2"));
		//隐藏最终上传的价格
		$tr_obj.find("[name=priceUnitArr]").val($discountPrice.val());
		//数量
		var count = $tr_obj.find("#quantityArr").val();
		//画面显示的金额
		$tr_obj.find("#money").html(this.formateMoney(count*discountPrice,2));
		BINOLSTSFH03.calcTotal();
	},
	
	// 根据折扣率计算折扣价格（批量/单个）
	"setDiscountPrice":function(obj){
		var curRateIndex = $("#curRateIndex").val();
		var findTRParam = "#dataRow"+curRateIndex;
		var $priceRate = $("#priceRate");
		var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
		var operateType = $("#operateType").val();
		
		//var priceRate = parseInt($priceRate.val());
		var priceRate = $priceRate.val();
		if(isNaN(priceRate) || priceRate == ""){
			priceRate = 100;
		}
		
		if(curRateIndex == ""){
			//批量计算折扣价格
			$("#batchPriceRate").val(priceRate);
			findTRParam = "tr";
		}
		
		$.each($('#databody').find(findTRParam), function(n){
			var $tr_obj = $(this);
			if(useCostPrice == "1"){//折扣率按成本价计算
				
				//原价
				var costPrice = $tr_obj.find("input[name='costPriceArr']").val();
				if(costPrice!=""){
					if(isNaN(costPrice)){
						costPrice = "";
					}
				}
	
				// 折扣价
				var discountPrice = $tr_obj.find("input[name='priceUnitArr']").val();
				if(discountPrice!=""){
					if(isNaN(discountPrice)){
						discountPrice = "";
					}
				}
				
				
				if(costPrice!=""){
					// 折扣价
					var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
					var $priceArr = $tr_obj.find("#priceArr");
					discountPrice = costPrice*priceRate/100;
					$discountPrice.val(discountPrice.toFixed("2"));
					$priceArr.val(discountPrice.toFixed("2"));
					//数量
					var count = $tr_obj.find("#quantityArr").val();
	
					$tr_obj.find("#money").html(BINOLSTSFH03.formateMoney(count*discountPrice,2));
				}else{
					$tr_obj.find("input[name='priceUnitArr']").val("0.00");
					$tr_obj.find("#priceArr").val("0.00");
					$tr_obj.find("#money").html("0.00");
				}
			}			
			else{//折扣率按参考价计算
				//原价
				var $costPrice = $tr_obj.find("input[name='referencePriceArr']");
				var costPrice = parseFloat($costPrice.val());
				if(isNaN(costPrice)){
					costPrice = 0;
				}
	
				// 折扣价
				var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
				var $priceArr = $tr_obj.find("#priceArr");
				var discountPrice = parseFloat($discountPrice.val());
				if(isNaN(discountPrice)){
					discountPrice = 0;
				}
				discountPrice = costPrice*priceRate/100;
				$discountPrice.val(discountPrice.toFixed("2"));
				$priceArr.val(discountPrice.toFixed("2"));
				//数量
				var count = $tr_obj.find("#quantityArr").val();

				$tr_obj.find("#money").html(BINOLSTSFH03.formateMoney(count*discountPrice,2));
			}

			
			
		});
		
		BINOLSTSFH03.calcTotal();
	},
	
	// 初始化折扣率弹出框 
	"initRateDiv":function(_this,type){
		var $this = $(_this);
		// 折扣率
		var $priceRate = $("#priceRate");
		var $rateDiv = $priceRate.parent();
		var operateType = $("#operateType").val();
		
		if(type == "batch"){
			//批量处理折扣价格
			$("#curRateIndex").val("");
			var batchPriceRate = $("#batchPriceRate").val();
			if(batchPriceRate == ""){
				batchPriceRate = "100";
			}
			$priceRate.val(parseFloat(batchPriceRate).toFixed("2"));
		}else{
			//折扣率弹出框的行数
			var $tr_obj = $this.parent().parent().parent();
			var trID = $tr_obj.attr('id');
			var curRateIndex = trID.substr(7);
			$("#curRateIndex").val(curRateIndex);
			
			var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
			// 原价
			var $costPrice = parseFloat("0.0");
			if(useCostPrice == "1"){
				if(!isEmpty($costPrice)){
					$costPrice = $tr_obj.find("[name='costPriceArr']").val();					
				}
			}else{				
				$costPrice = $tr_obj.find("[name='referencePriceArr']").val();
			}
			// 折扣价格
			var $discountPrice = $tr_obj.find("[name='priceUnitArr']").val();
			var costPrice = parseFloat($costPrice).toFixed("2");
			var discountPrice = parseFloat($discountPrice).toFixed("2");
			if(isNaN(costPrice)){costPrice = 0;}
			if(isNaN(discountPrice)){discountPrice = 0;}
			if(costPrice!=0){
				var priceRate = discountPrice*100/costPrice;
				$priceRate.val(priceRate.toFixed("2"));
			}
		}
		
		// 弹出折扣率设置框
		$rateDiv.show();
		//$priceRate.focus();
		$priceRate.trigger("select");
		// 设置弹出框位置
		var offset = $this.offset();
		$rateDiv.offset({"top":offset.top-$rateDiv.height()-2,"left":offset.left});
	},
	
	// 关闭折扣率弹出框
	"closeDialog":function(_this){
		$("#curRateIndex").val("");
		var $rateDiv = $(_this).parent();
		$rateDiv.hide();
	},
	
	"expectDeliverDateChange":function(){
		$("#expectDeliverDate").val($("#selExpectDeliverDate").val());
	},
	
	"expectDeliverDateSelect":function(dateText, inst){
		BINOLSTSFH03.expectDeliverDateChange();
	},
	
	"transforValueComment":function(){
		$("#comments").val($("#deliveryComments").val());
	}
};
var BINOLSTSFH03 = new BINOLSTSFH03();

$(function(){
//	// 产品popup初始化
//	BINOLSTSFH03.STSFH03_dialogBody = $('#productDialog').html();
	
	//期望发货日期
	$('#selExpectDeliverDate').cherryDate({
		onSelectEvent:BINOLSTSFH03.expectDeliverDateSelect
	});
});

//function selectProduct () {
//	var selectedArr = $("#dataTableBody").find(":input[checked]");
//	
//	var length = selectedArr.length;
//	for(var i=0;i<length;i++){
//		BINOLSTSFH03.addRow();
//		var $selectedProduct = $(selectedArr[i]);
//		var selectedObj = window.JSON2.parse($selectedProduct.val()); 
//		var index = $('#rowNumber').val();
//		// 产品厂商编号
//		var unitCode = selectedObj.unitCode;
//		// 条码
//		var barCode = selectedObj.barCode;
//		// 厂商ID
//		var productVendorID = selectedObj.productVendorId;
//		
//		var trID = 'dataRow'+index;
//		//检查重复行
//		var sameFlag =false;
//		$.each($('#databody').find('tr'), function(n){
//			if($(this).find("input@[name='productVendorIDArr']").val() == productVendorID){
//				$(this).attr("class","errTRbgColor");
//				$('#errorDiv2').find('#errorSpan2').html($('#errmsg_EST00007').val());
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
//		$('#unitCodeArr' + index).val(unitCode);
//		$('#barCodeArr' + index).val(barCode);
//		$('#productVendorIDArr' + index).val(productVendorID);
//		
//		//序号
//		var curNo = $("#databody tr").length-1;
//		$('#'+trID).find('#dataTd1').html(curNo);
//
//		// 厂商编码
//		$('#'+trID).find('#dataTd2').html(unitCode);
//		
//		// 条码
//		$('#'+trID).find('#dataTd3').html(barCode);
//		
//		// 产品名称
//		$('#'+trID).find('#dataTd4').html(selectedObj.nameTotal);
//		
//		//价格
//		var price;
//		if(!selectedObj.standardCost){
//			price = "0.00";
//		}else{
//			price = selectedObj.standardCost;
//		}
//		$('#'+trID).find('#dataTd5').html(BINOLSTSFH03.formateMoney(price,2));
//		$('#'+trID).find('#dataTd10').find('#priceUnitArr'+index).val(price);
//		
//		
//		//ajax取得该产品的库存数量
//		var param = "currentIndex="+index+"&depotInfoId="+$("#outDepotInfoId").val()+"&productVendorId="+productVendorID;
//		if ($("#outLogicDepotsInfoId").length>0 && $("#outLogicDepotsInfoId").val()!=""){
//			param += "&logicDepotsInfoId="+$("#outLogicDepotsInfoId").val();
//		}
//		cherryAjaxRequest({
//			url:$("#s_getStockCount").html(),
//			reloadFlg:true,
//			param:param,
//			callback:BINOLSTSFH03.getStockSuccess
//		});
//	}
//	BINOLSTSFH03.changeOddEvenColor1();
//	closeCherryDialog('productDialog',BINOLSTSFH03.STSFH03_dialogBody);	
//	oTableArr[0]= null;	
//}
