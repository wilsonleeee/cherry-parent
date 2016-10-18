var binOLSTSFH05_global = {};
//是否需要解锁
binOLSTSFH05_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	var search_url = $('#search_url',window.opener.document).val();
	if(search_url.indexOf("BINOLPTRPS25_search")>-1){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

var BINOLSTSFH05 = function () {};
BINOLSTSFH05.prototype = {

//		"STSFH05_dialogBody":"",
		
//		"number":0,
		
//		"addNewLine":function(){
//			var rowNumber = Math.abs($('#rowNumber').val())+1;
//			var newid = 'dataRow'+rowNumber;
//			$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");	
//			$('#'+newid).find(':checkbox').val(rowNumber);
//			$('#'+newid).find('#unitCodeArr').attr('id','unitCodeArr'+rowNumber);
//			$('#'+newid).find('#barCodeArr').attr('id','barCodeArr'+rowNumber);
//			$('#'+newid).find('#productVendorIDArr').attr('id','productVendorIDArr'+rowNumber);
//			$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//			$('#rowNumber').val(rowNumber);
//			this.unchecke();
//		},
		
//		"selectProduct":function(selectedArr){
//			var that = this;
//			var i = that.number;
//			var length = selectedArr.length;
//			var $selectedProduct = $(selectedArr[i]);
//			var selectedObj = window.JSON2.parse($selectedProduct.val()); 
//			var unitCode = selectedObj.unitCode;
//			// 条码
//			var barCode = selectedObj.barCode;
//			// 厂商ID
//			var productVendorID = selectedObj.productVendorId;
//			//价格
//			var price;
//			if(!selectedObj.salePrice){
//				price = "0.00";
//			}else{
//				price = selectedObj.salePrice;
//			}
//			var url = $("#getProductStock").attr("href");
//			var param = "productVendorId="+productVendorID+"&depotInfoId="+$("#outDepotInfoID").val()+"&logicDepotsInfoId="+$("#outLogicInventoryInfoID").val();
//			var callback = function(msg){
//				that.addNewLine();
//				var index = $('#rowNumber').val();
//				// 产品厂商编号
//				var trID = 'dataRow'+index;
//				//检查重复行
//				var flag = true;
//				$.each($('#databody').find('tr'), function(n){
//					if($(this).find("input@[name='productVendorIDArr']").val() == productVendorID){
//						$(this).attr("class","errTRbgColor");
//						$('#errorSpan2').html($('#errmsg_EST00007').val());
//						$('#errorDiv2').show();
//						//删除新增行
//						$('#databody').find('tr:last').remove();
//						flag = false;
//						return false;
//					}
//				});
//				if(flag){
//					$('#productVendorIDArr' + index).val(productVendorID);
//					$('#priceUnitArr' + index).val(price);
//					//编号
//					var no = $('#databody').find('tr').length-1;
//					$('#'+trID).find('#dataTd1').html(no);
//					// 厂商编码
//					$('#'+trID).find('#dataTd2').html(unitCode);
//					// 产品名称
//					$('#'+trID).find('#dataTd3').html(selectedObj.nameTotal);
//					// 条码
//					$('#'+trID).find('#dataTd4').html(barCode);
//					//产品价格
//					$('#'+trID).find('#dataTd5').html(BINOLSTSFH05.formateMoney(price,2));
//					//库存
//					if(isNaN(msg)){
//						msg='0';
//					}
//					$('#'+trID).find('#nowCount').html(msg);
//					//that.changeOddEvenColor();
//					that.changeOddEvenColor1();
//				}
//				if(that.number < length-1){
//					that.number++;
//					that.selectProduct(selectedArr);
//				}
//			};
//			cherryAjaxRequest({
//				url: url,
//				param: param,
//				callback:callback
//			});
//				
//		},
		
		/**
		 * 产品弹出框
		 * @return
		 */
		"openProPopup":function(_this){
			var that = this;
			this.clearActionMsg();
//			var callback = function(){
//				$("#selectProducts").attr("onclick","");
//				$("#selectProducts").bind("click",function(){
//					var selectedArr = $("#dataTableBody").find("input:checked");
//					if(selectedArr.length>0){
//						that.number = 0;
//						that.selectProduct(selectedArr);
//					}
//					closeCherryDialog('productDialog',that.STSFH05_dialogBody);
//				});
//			};
//			var proPopParam = {
//					thisObj : _this,
//					  index : 1,
//				  checkType : "checkbox",
//				      modal : true,
//				  autoClose : [],
//				 dialogBody : that.STSFH05_dialogBody,
//				 callback : callback
//			};
//			popDataTableOfPrtInfo(proPopParam);
			
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
					var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" name="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTSFH05.changechkbox(this)"/></td>';
					html += '<td id="dataTd1" style="white-space:nowrap"></td>';
					html += '<td id="dataTd2">'+info.unitCode+'</td>';
					html += '<td id="dataTd4">'+info.barCode+'</td>';
					html += '<td id="dataTd3">'+info.nameTotal+'</td>';
					var price = info.salePrice;//销售价格
					var costPrice = info.costPrice;//成本价
					var totalCostPrice = info.totalCostPrice;//总成本价
					
					var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
					
					var sysConfigUsePrice = $("#sysConfigUsePrice").val();
					if(sysConfigUsePrice == "MemPrice"){
						price = info.memPrice;// 会员价格
					} else if(sysConfigUsePrice == "StandardCost") {
						price = info.standardCost;// 结算价
					}
					if(isEmpty(price)){
						price = parseFloat("0.0");
					}else{
						price = parseFloat(price);
					}
					
					if(costPrice!= undefined){
						if(!isEmpty(costPrice)){
							costPrice = parseFloat(costPrice).toFixed(2);
						}
					}else{
						costPrice ="";
					}
					
					if(totalCostPrice!= undefined){
						if(!isEmpty(totalCostPrice)){
							totalCostPrice = parseFloat(totalCostPrice).toFixed(2);
						}
					}else{
						totalCostPrice ="";
					}
					
					html += '<td id="dataTdReferencePrice" class="alignRight">'+BINOLSTSFH05.formateMoney(price,2)+'</td>';
					html += '<td style="text-align:right;" ><div id="costPrice">'+costPrice+'</div></td>';
					html += '<td style="text-align:right;" ><div id="totalCostPrice">'+totalCostPrice+'</div></td>';
					//var operateType = $("#operateType").val();
					html += '<td id="dataTd5" class="alignRight">';
					if(useCostPrice== "1"){	
						if(!isEmpty(costPrice)){
							html += '<input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="'+ costPrice + '" onchange="BINOLSTSFH05.setPrice(this);"/>';
						}else{
							html += '<input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="0.00" onchange="BINOLSTSFH05.setPrice(this);"/>';
						}
					}
					else{
						html += '<input type="text" id="priceUnitArr" class="price" name= "priceUnitArr" value="'+ price + '" onchange="BINOLSTSFH05.setPrice(this);"/>';
					}
					//html += '<input id="priceUnitArr" class="price" type="text" onchange="BINOLSTSFH05.setPrice(this);return false;" value="'+price.toFixed("2")+'" maxlength="9" size="10" name="priceUnitArr">';
 					html += ' '+'<span id="spanCalPrice" class=""><span class="calculator" title="'+$("#spanCalTitle").text()+'" onclick="BINOLSTSFH05.initRateDiv(this);"></span></span>';
					html += '</td>';
					if($("#th_ProductQuantity").is(":visible")){
						html += '<td id="dataTd6" class="alignRight"></td>';
					}
					if($("#th_OrderQuantity").is(":visible")){
						html += '<td id="orderQuantity" class="alignRight"></td>';
					}
					html += '<td id="nowCount" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9"  onkeyup="BINOLSTSFH05.changeCount(this);" onchange="BINOLSTSFH05.changeCount(this);"/></td>';
					html += '<td id="money" style="text-align:right;">0.00</td>';
					html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width: 98%;"/></td>';
					html += '<td style="display:none" id="dataTd10">';
					html += '<input type="hidden" id="referencePriceArr'+index+'" name="referencePriceArr" value="'+price+'"/>';
					html += '<input type="hidden" id="suggestedQuantityArr'+index+'" name="suggestedQuantityArr" value="0"/>';
					html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
					html += '<input type="hidden" id="costPriceArr" name="costPriceArr" value="'+costPrice+'"/>';
					html += '<input type="hidden" name="prtVendorId" value="'+info.proId+'"/>';
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
					BINOLSTSFH05.changeOddEvenColor();
					BINOLSTSFH05.calcTotal();
					//ajax取得选中产品的库存数量
					var param ='depotInfoId='+$('#outDepotInfoID').val()+'&logicDepotsInfoId='+$("#outLogicInventoryInfoID").val();
					param += '&operateType='+$("#operateType").val();
					param += '&lockSection='+$("#lockSection").val();
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
							callback:BINOLSTSFH05.getStockSuccess
						});
					}
				}
			};
			// 调用产品弹出框共通
			popAjaxPrtDialog(option);
		},
		
		/**
		 * 解析库存JSON
		 * @param msg
		 */
		"getStockSuccess":function(msg){
			var member = eval("("+msg+")"); //数据解析为json 格式  	
			$.each(member, function(i){
				$('#hasproductflagArr'+member[i].currentIndex).val(member[i].hasproductflag);
				$('#dataRow'+member[i].currentIndex +' > #dataTd6').html(member[i].Quantity);
			});
		},
		
		/**
		  * 删除掉画面头部的提示信息框
		  * @return
	     */
		"clearActionMsg":function(){
			$('#actionResultDisplay').html("");
			$('#errorDiv2').attr("style",'display:none');
			$("#databody tr:odd").attr("class","even");
			$("#databody tr:even").attr("class","odd");
		},
		
		"selectAll":function(obj){
			this.clearActionMsg();
			var _this = obj;
			var subCheckedBox = document.getElementsByName("chkbox");
			for(var i = 0 ; i < subCheckedBox.length ; i++){
				subCheckedBox[i].checked = _this.checked;
			}
		},
		
		"changechkbox":function(obj){
			this.clearActionMsg();
			var _this = obj;
			if(_this.checked){
				var subCheckedBox = document.getElementsByName("chkbox");
				var checkedNum = 0;
				for(var i = 0 ; i < subCheckedBox.length ; i++){
					if(subCheckedBox[i].checked){
						checkedNum++;
					}
				}
				if(checkedNum == subCheckedBox.length){
					document.getElementById("allSelect").checked = _this.checked;
				}
			}else{
				document.getElementById("allSelect").checked = _this.checked;
			}
		},
		
		"deleteLine":function(){
			var checked = $("input[name=chkbox]:checked");
			if(checked.length == 0){
				return false;
			}
			checked.each(function(){
				if(!$(this).is(":hidden")){
					$(this).parent().parent().remove();
				}
			});
			var rowObj = $("#databody").children();
			for(var i = 0 ; i < rowObj.length ; i++){
				$(rowObj[i]).find("#dataTd1").html(i+1);
			}
			this.changeOddEvenColor();
			this.unchecke();
			this.calcTotal();
		},
		
		"save":function(){
			if(!this.checkDepot()){
				return false;
			}
			if(!this.checkDate()){
				return false;
			}
			
			var errorFlag = false;			
			$.each($('#databody >tr'), function(i){
				if($(this).find("#quantityArr").val() != ""){

					if(isNaN($(this).find("#quantityArr").val())){
						$(this).attr("class","errTRbgColor");
						errorFlag = true;
					}else{
						$(this).removeClass('errTRbgColor');
					}
				}
			});
			
			
			if(errorFlag){
				$("#errorSpan2").html($("#errmsg1").val());
				$("#errorDiv2").show();
				return false;
			}
			
			var checkStockFlag = $("#checkStockFlag").val();
			//为1库存允许为负，为0库存不允许为负
			if(checkStockFlag == "0"){
				if(this.hasWarningData()){
					$("#errorSpan2").html($("#errmsg_EST00034").val());
					$("#errorDiv2").show();
					return false;
				}
			}
			
									
			var errorQuantityFlag= false;
			$.each($('#databody >tr'), function(i){
				if($(this).find("#quantityArr").val() != ""){
					if(Number($(this).find("#quantityArr").val())>Number($(this).find("#dataTd6").text().replace(/,/g,""))){
						errorQuantityFlag = true;
						$(this).attr("class","errTRbgColor");
					}else{
						$(this).removeClass('errTRbgColor');
					}
				}
			});
			
			if(errorQuantityFlag){
				$("#errorSpan2").html($("#errmsg_EST00034").val());
				$("#errorDiv2").show();
				return false;
			}
			else{
				var url = document.getElementById("saveUrl").href;
				var param = $("#mainForm").serialize();
				cherryAjaxRequest({
					url: url,
					param: param,
					callback:function(msg){
						if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
					}
				});
			}
		},
		
		"submit":function(){
			if(!this.checkDepot()){
				return false;
			}
			if(!this.checkDate()){
				return false;
			}
			
			var errorFlag = false;			
			$.each($('#databody >tr'), function(i){
				if($(this).find("#quantityArr").val() != ""){

					if(isNaN($(this).find("#quantityArr").val())){
						$(this).attr("class","errTRbgColor");
						errorFlag = true;
					}else{
						$(this).removeClass('errTRbgColor');
					}
				}
			});
			
			
			if(errorFlag){
				$("#errorSpan2").html($("#errmsg1").val());
				$("#errorDiv2").show();
				return false;
			}
			
			var checkStockFlag = $("#checkStockFlag").val();
			//为1库存允许为负，为0库存不允许为负
			if(checkStockFlag == "0"){
				if(this.hasWarningData()){
					$("#errorSpan2").html($("#errmsg_EST00034").val());
					$("#errorDiv2").show();
					return false;
				}
			}
			
			var errorQuantityFlag= false;
			$.each($('#databody >tr'), function(i){
				if($(this).find("#quantityArr").val() != ""){
					if(Number($(this).find("#quantityArr").val())>Number($(this).find("#dataTd6").text().replace(/,/g,""))){
						errorQuantityFlag = true;
						$(this).attr("class","errTRbgColor");
					}else{
						$(this).removeClass('errTRbgColor');
					}
				}
			});
			if(errorQuantityFlag){
				BINOLSTSFH05.submitConfirm();
			}else{
				BINOLSTSFH05.submitSend();
			}
		},
		
		/**
		 * 提交前对数据进行检查
		 * 检查发货数量是否大于库存
		 * @returns {Boolean}
		 */
		"hasWarningData":function(){
			var flag = false;
			//检查有无空行
			$.each($('#databody >tr'), function(i){	
				if(i>=0){
					if(Number($(this).find("#quantityArr").val())>Number($(this).find("#dataTd6").text())){
						flag = true;
						$(this).attr("class","errTRbgColor");				
					}else{
						$(this).removeClass('errTRbgColor');				
					}
				}
			});	
			return flag;
		},
		
		"changeOddEvenColor":function(){
			$("#databody tr:odd").prop("class","even");
			$("#databody tr:even").prop("class","odd");
		},
		
		"changeOddEvenColor1":function(){
			$("#databody tr:odd").each(function(){
				if($(this).prop("class").indexOf("errTRbgColor") == -1){
					$(this).prop("class","odd");
				}
			});
			$("#databody tr:even").each(function(i){
				if(i != 0){
					if($(this).prop("class").indexOf("errTRbgColor") == -1){
						$(this).prop("class","even");
					}
				}
			});
		},
		
		"changeCount":function(obj){
//			var $this = $(obj);
//			if(isNaN($this.val().toString())){
//				$this.val("");
//				$this.parent().next().html("0.00");
//			}else{
//				var price = Number($this.parent().parent().find("[name='priceUnitArr']").val());
//				$this.val(parseInt(Number($this.val())));
//				var amount = price*Number($this.val());
//				$this.parent().next().html(this.formateMoney(amount,2));
//			}
			
			var $this = $(obj);
			var $thisTd = $this.parent();
			var TrID = $thisTd.parent().attr("id");
			var $thisVal =$this.val().toString();
			
			var useCostPrice = $("#useCostPrice").val();//实际执行价是否按成本价计算
				
			if($thisVal == ""){
				$("#"+TrID).find(":input[name='costPriceArr']").val("");
				$("#"+TrID).find("div[id=costPrice]").html("");
				$("#"+TrID).find("#totalCostPrice").html("");
				$("#"+TrID).find("#money").html("0.00");
				if(useCostPrice == "1"){			
					$("#"+TrID).find("#priceUnitArr").val("0.00");
				}
				
			}else if(isNaN($thisVal)){		
					if($thisVal!="-"){
						$this.val("0");
						$("#"+TrID).find(":input[name='costPriceArr']").val("");
						$("#"+TrID).find("div[id=costPrice]").html("");
						$("#"+TrID).find("#totalCostPrice").html("");
						$("#"+TrID).find("#money").html("0.00");
						if(useCostPrice == "1"){			
							$("#"+TrID).find("#priceUnitArr").val("0.00");
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
				$thisTd.next().text(this.formateMoney(amount,2));
			}
			
			this.calcTotal();
			
			BINOLSTSFH05.isCorrectQuantity(obj);
			
			/**以下是 根据发货数量动态取对应的成本价**/
			
			
			var getCostPriceURL = $("#getCostPriceURL").html();//得到产品对应的平均成本价
			var productVendorId = $("#"+TrID).find(":input[name='productVendorIDArr']").val();
			var priceUnitArr = $("#"+TrID).find(":input[name='priceUnitArr']").val();//实际执行价
			var quantityuArr = $("#"+TrID).find(":input[name='quantityArr']").val();//发货数量
			
			var param = "productVendorId="+productVendorId;
				param += "&outDepotId="+$("#outDepotInfoID").val();
				param += "&outLoginDepotId="+$("#outLogicInventoryInfoID").val();
				param += "&prtCount="+quantityuArr;
				
				//alert(param);
				
			if(quantityuArr !="" && quantityuArr !=null && quantityuArr != undefined && !isNaN(quantityuArr) && productVendorId!=""){//表示有发货数量，此时需要计算出对应的成本价
				cherryAjaxRequest({
					url: getCostPriceURL,
					param:param,
					callback: function(msg) {
							var data = eval("("+msg+")");    //包数据解析为json 格式  
							
							
							if(data.costPrice!=null && data.costPrice!="" && !isNaN(data.costPrice)){
								$("#"+TrID).find(":input[name='costPriceArr']").val(parseFloat(data.costPrice).toFixed(2));
								$("#"+TrID).find("div[id=costPrice]").html(parseFloat(data.costPrice).toFixed(2));
								if(useCostPrice == "1"){
									$("#"+TrID).find(":input[name='priceUnitArr']").val(parseFloat(data.costPrice).toFixed(2));
								}
							}else{
								$("#"+TrID).find(":input[name='costPriceArr']").val("");
								$("#"+TrID).find("div[id=costPrice]").html("");
								if(useCostPrice == "1"){
									$("#"+TrID).find(":input[name='priceUnitArr']").val(parseFloat("0.0").toFixed(2));
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
						$thisTd.next().text(BINOLSTSFH05.formateMoney(amount,2));
						
						
						BINOLSTSFH05.calcTotal();
						
						BINOLSTSFH05.isCorrectQuantity(obj);
					}
				});
			}
		},
		
		"unchecke":function(){
			$("input[type=checkbox]").prop("checked",false);
		},
		
		"checkDate":function(){
			this.clearActionMsg();
			var flag = true;
			var count = 0;
			//检查有无空行
			$.each($('#databody >tr'), function(i){
				count ++;
				var quantity = Number($(this).find("#quantityArr").val());
				if(quantity == 0){
					flag = false;
					$(this).attr("class","errTRbgColor");
					$("#errorSpan2").html($("#errmsg1").val());
					$("#errorDiv2").show();
				}else{
					$(this).removeClass('errTRbgColor');
				}
			});	
			if(count == 0){
				flag = false;
				$('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
				$('#errorDiv2').show();
			}
			return flag;
		},
		/**
		 * 计算总数量，总金额
		 * */
		"calcTotal":function(){
			var rows = $("#databody").children();
			var totalQuantity = 0;
			var totalAmount = 0.00;
			if(rows.length > 0){
				rows.each(function(i){
					var $tds = $(this).find("td");
					var $inputVal=$tds.find("#quantityArr").val();
					if(isNaN($inputVal)){
						$inputVal=0;
					}
					totalQuantity += Number($inputVal);
//					var price = $tds.find("[name='priceUnitArr']").val();
//					if(price == undefined || price == ""){
//						price = "0.00";
//					}

					if($(this).find("#money").length>0){
						totalAmount += parseFloat($(this).find("#money").html().replace(/,/g,""));
					}
				});
			}
			$("#totalQuantity").html(BINOLSTSFH05.formateMoney(totalQuantity,0));
			$("#totalAmount").html(BINOLSTSFH05.formateMoney(totalAmount,2));
			$("#totalAmountCheck").val(totalAmount);
		},

		
		"refreshDeliverType":function(obj){
			var $this = $(obj);
			$("#deliverType").val($this.val());
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
		$("#saveBut").show();
		$("#showBtnopenDepartBox").removeClass("hide");
		$("#showAcceptDepotCodeName").removeClass("hide");
		$("#showAcceptLogicInventoryName").removeClass("hide");
		$("#showToolbar").removeClass("hide");
		$("#showAddBtn").removeClass("hide");
		$("#showDelBtn").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$("#showDeliverTypeSelect").removeClass("hide");
		$("#showEditPlanArriveDate").removeClass("hide");
		
		$("#hideAcceptDepotCodeName").addClass("hide");
		$("#hideAcceptLogicInventoryName").addClass("hide");
		$("#hideDeliverType").addClass("hide");
		$("#hideEditPlanArriveDate").addClass("hide");
		
//		//如果有品牌设定不可设置折扣价格，需要在工作流修改按钮OS_ButtonScript传入参数HIDEDISCOUNTPRICE。
//		if(param.indexOf("HIDEDISCOUNTPRICE") == -1){
//			$("#thDiscountPrice").removeClass("hide");
//		}
		$("#spanBatchCalc").show();
		
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("#dataTd6").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
				
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideComments").addClass("hide");
			
//			if(param.indexOf("HIDEDISCOUNTPRICE") == -1){
//				$(this).find("#tdDiscountPrice").removeClass("hide");
			if($("#operateType").val() != "50"){
				$(this).find("#spanCalPrice").removeClass("hide");
				$(this).find("[name='priceUnitArr']").removeClass("hide");
				$(this).find("#hidePriceArr").addClass("hide");
			}
				
//			}
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
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
		binOLSTSFH05_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#productDeliverDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#productDeliverDetailUrl').submit();
	},
	
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
	 	
	 	var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text().escapeHTML();
				var departName = $selected.find("td:eq(2)").text().escapeHTML();
				$("#outOrganizationID").val(departId);
				$("#outOrgName").text("("+departCode.unEscapeHTML()+")"+departName.unEscapeHTML());
				BINOLSTSFH05.chooseDepart();
			}
			
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){	
		BINOLSTSFH05.clearActionMsg();
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
		if($("#outDepotInfoId option").length>1){
			$("#outDepotInfoId").get(0).selectedIndex=0;
		}
		$("#outDepotInfoID").val($("#outDepotInfoId").val());
		$("#depotInfoIdAccept").val("");
		
		BINOLSTSFH05.refreshStockCount();
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
		$("#outDepotInfoID").val($("#outDepotInfoId").val());
		$("#outLogicInventoryInfoID").val($("#outLogicDepotsInfoId").val());
		
		var stockUrl = $('#s_getStockCount').html();
		var params = "&depotInfoId="+$("#outDepotInfoId").val()+"&logicDepotsInfoId="+$("#outLogicDepotsInfoId").val();
		params += "&operateType="+$("#operateType").val();	
		params += "&lockSection="+$("#lockSection").val();
		$.each($('#databody >tr'), function(i){	
			if(i>=0){
				var index = $(this).attr("id").substr(7);
				var productVendorId = $(this).find("[name=productVendorIDArr]").val();
				params += "&currentIndex="+index+"&productVendorId="+productVendorId;
			}
		});
		var callback = function(msg){
			var member = eval("("+msg+")");
			$.each(member, function(i){
				//发货方库存
				$('#dataRow'+member[i].currentIndex +' > #dataTd6').html(BINOLSTSFH05.formateMoney(member[i].Quantity,0));
			});
		};
		
		cherryAjaxRequest({
			url:stockUrl,
			reloadFlg:true,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 打开编辑功能
	 */
	"openEdit":function(Id){
		$("#"+Id).prop("disabled",false);
	},
	
	/**
	 * 赋值
	 */
	"setVal":function(fromId,toId){
		$("#"+toId).val($("#"+fromId).val());
	},
	
	/**
	 * 验证部门、仓库、逻辑仓库
	 */
	"checkDepot":function(){
		var operateType = $("#operateType").val();
		if(operateType == "34" || operateType == "32" || operateType == "31"){
			if($("#outOrganizationID").val() == null || $("#outOrganizationID").val() == ""){
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
				$('#errorDiv2').show();
				return false;
			}else if($("#outDepotInfoId").val() == null || $("#outDepotInfoId").val() == ""){
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
				$('#errorDiv2').show();
				return false;
			}else if($("#outLogicDepotsInfoId").val() == null || $("#outLogicDepotsInfoId").val() == ""){
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00025').val());
				$('#errorDiv2').show();
				return false;
			}else{
				$('#errorDiv2').hide();
				return true;
			}
		}else{
			return true;
		}
	},
	
	"openProStockPopup":function(obj){
		var option = {};
		var operateType = $("#operateType").val();
		if(operateType == "2"){
			option.initType = "select";
			option.brandInfoID = $("#BIN_BrandInfoID").val();
			option.departID = $("#inOrganizationID").val();
			option.param = "&"+$('[name=productVendorIDArr]').serialize();
			option.DepartName=$("#orderDepartCodeName").text();
		}else{
			option.param = "entryID="+$('#entryID').val()+"&"+$('[name=productVendorIDArr]').serialize();
		}
		popAjaxProStockDialog(option);
	},
	
	 // 格式化折扣价格及重新计算金额
	"setPrice":function(obj){
		var $this = $(obj);
		var $tr_obj = $this.parent().parent();

		// 折扣价
		var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
		var discountPrice = parseFloat($this.val());

		if(isNaN(discountPrice)){
			discountPrice = 0;
		}
		$discountPrice.val(discountPrice.toFixed("2"));
//		//隐藏最终上传的价格
//		$tr_obj.find("[name=priceUnitArr]").val($discountPrice.val());
		//数量
		var count = $tr_obj.find("#quantityArr").val();
		//画面显示的金额
		$tr_obj.find("#money").html(BINOLSTSFH05.formateMoney(count*discountPrice,2));
		BINOLSTSFH05.calcTotal();
	},
	
	// 根据折扣率计算折扣价格（批量/单个）
	"setDiscountPrice":function(obj){
		var curRateIndex = $("#curRateIndex").val();
		var findTRParam = "#dataRow"+curRateIndex;
		var $priceRate = $("#priceRate");
		var useCostPrice =$("#useCostPrice").val();
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
			
			if(useCostPrice == "0"){//折扣率按参考价计算
				//原价
				var $costPrice = $tr_obj.find("input[name='referencePriceArr']");
				var costPrice = parseFloat($costPrice.val());
				if(isNaN(costPrice)){
					costPrice = 0;
				}
	
				// 折扣价
				var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
				var discountPrice = parseFloat($discountPrice.val());
				if(isNaN(discountPrice)){
					discountPrice = 0;
				}
				discountPrice = costPrice*priceRate/100;
				$discountPrice.val(discountPrice.toFixed("2"));
				//数量
				var count = $tr_obj.find("#quantityArr").val();

				$tr_obj.find("#money").html(BINOLSTSFH05.formateMoney(count*discountPrice,2));
			}else{//折扣率按成本价计算
				
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
					discountPrice = costPrice*priceRate/100;
					$discountPrice.val(discountPrice.toFixed("2"));
					//数量
					var count = $tr_obj.find("#quantityArr").val();
	
					$tr_obj.find("#money").html(BINOLSTSFH05.formateMoney(count*discountPrice,2));
				}else{
					$tr_obj.find("input[name='priceUnitArr']").val("0.00");
					$tr_obj.find("#money").html("0.00");
				}
			}
			
			
		});
		
		BINOLSTSFH05.calcTotal();
	},
	
	// 初始化折扣率弹出框 
	"initRateDiv":function(_this,type){
		var $this = $(_this);
		// 折扣率
		var $priceRate = $("#priceRate");
		var $rateDiv = $priceRate.parent();
		
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
	
	/*
	 * 删除单据
	 */
	"deleteBill":function(){
		this.clearActionMsg();
		var params=$('#mainForm').formSerialize();
		var url = $("#deleteUrl").attr("href");
		var callback = function(msg){
			if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url:url,
			param:params,
			callback:callback
		});
	},
	
	/**
	 * 判断数量是否已经更正，如果是明细行移除红色背景
	 */
	"isCorrectQuantity":function(obj){
		var $tr_obj = $(obj).parent().parent();
		if($tr_obj.find("#quantityArr").val() != ""){
			if(Number($tr_obj.find("#dataTd6").text().replace(/,/g,"")) - Number($tr_obj.find("#quantityArr").val()) >= 0){
				$tr_obj.removeClass('errTRbgColor');
				//恢复原先背景色
				if($("#databody tr:odd").index($tr_obj) == -1){
					$tr_obj.attr("class","odd");
				}else{
					$tr_obj.attr("class","even");
				}
			}
		}
	},
	
	/**
	 * 执行发货
	 */
	"submitSend":function(){
		var url = document.getElementById("submitUrl").href;
		var param = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback:function(msg){
				if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
			}
		});
	},
	
	/**
	 * 发货数量大于库存弹出框
	 */
	"submitConfirm":function() {
		var dialogSetting = {
			dialogInit: "#dialogInit",	
			width: 	300,
			height: 180,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLSTSFH05.submitSend();removeDialog("#dialogInit");},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
		$("#dialogInit").html($("#errmsg3").val());
	}
};

var BINOLSTSFH05 = new BINOLSTSFH05();


$(document).ready(function() {
//	// 产品popup初始化
//	BINOLSTSFH05.STSFH05_dialogBody = $('#productDialog').html();
	initDetailTable();
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	$("#exportButton").prop("disabled",false);
	
    $('#planArriveDate').cherryDate({
        beforeShow: function(input){
            var value = $('#dateToday').val();
            return [value,'minDate'];
        }
    });
	
	$('.tabs').tabs();
} );