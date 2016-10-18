function BINOLWSMNG01(){};

BINOLWSMNG01.prototype = {
	
	// 添加新行
//	"addNewLine":function(){
//		
//		BINOLSTSFH01.clearActionMsg();
//		
//		// 设置全选状态
//		$('#allSelect').prop("checked",false);
//		
//		//已有空行不新增一行
//		var addNewLineFlag = true;
//		$.each($('#databody >tr'), function(i){
//			if($(this).find("[name='unitCodeBinding']").is(":visible")){
//				addNewLineFlag = false;
//				$(this).find("[name='unitCodeBinding']").focus();
//				return;
//			}
//		});
//		
//		if(addNewLineFlag){
//			var operateType = $("#operateType").val();
//			var nextIndex = parseInt($("#rowNumber").val())+1;
//			$("#rowNumber").val(nextIndex);
//			var html = '<tr id="dataRow'+nextIndex+'">';
//			html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH01.changechkbox(this);"/></td>';
//			html += '<td><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/><input type="hidden" id="unitCodeArr"></td>';
//			html += '<td><span id="spanBarCode"></span><input type="hidden" id="barCodeArr"></td>';
//			html += '<td><span id="spanProductName"></span></td>';
//			html += '<td style="text-align:right;"><span id="spanPrice"/></span></td>';
//			html += '<td id="nowCount" style="text-align:right;"></td>';
//			html += '<td class="center"><input  value="" name="quantityArr" id="quantityuArr" cssClass="text-number" style="width:120px;text-align:right;" size="10" maxlength="4" onchange="BINOLWSMNG01.changeCount(this)" oninput="BINOLWSMNG01.changeCount(this)" cssStyle="width:98%"/></td>';
//			html += '<td id="money" class="center" style="text-align:right;">0.00</td>';
//			if(operateType == '2') {
//				html += '<td class="center"><input value="" name="commentsArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
//			} else {
//				html += '<td class="center"><input value="" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
//			}
//			html +='<td style="display:none">';
//			if(operateType == '2') {
//				html += $("#firstDetailData").html()+'</td></tr>';	
//			} else {
//				html += '<input type="hidden" name="prtVendorId" value=""/>'
//		        +'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value=""/>'
//		        +'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value=""/></td></tr>';
//			}
//			
//			
//			$("#mainTable").append(html);
//			
//			var unitCode = "unitCodeBinding_"+nextIndex;
//			this.setProductBinding(unitCode);
//	
//			$("#unitCodeBinding_"+nextIndex).focus();
//			
//			this.bindInput();
//		}
//	},
	
	"setProductBinding":function(id){
		cntProductBinding({counterCode:$("#counterCode").val(),elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLWSMNG01.pbAfterSelectFun});
	},
	
	"bindInput":function(){
		var tableName = "mainTable";
		if($("#spanBtnadd.ui-state-disabled").length > 0){
			//空白单
			tableName = "blankTable";
		}
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLWSMNG01.bindInputTR(trParam);
		})
	},
	
	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，备注按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			
				var key = e.which;
				if (key == 13) {
					//回车键
					e.preventDefault();
		            
					//当前文本框为下拉框可见且输入有问题如重复，停止跳到下一个
					var nextFlag = true;
					if($(this).attr("name") == "unitCodeBinding" && $(this).is(":visible")){
						nextFlag = false;
					}
					if(nextFlag){
						var nxtIdx = $inp.index(this) + 1;
						var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
						if($nextInputText.length>0){
							//跳到下一个文本框
							$nextInputText.focus();
						}else{
							//跳到下一行第一个文本框，如果没有下一行，新增一行
							if($(trParam).next().length==0){
								//代码暂时未使用
								//BINOLWSMNG01.addNewLine();
							}else{
								$(trParam).next().find("input:text:visible:eq(0)").focus();
							}
						}
					}
					
				}
			
		});
	},
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		$('#'+info.elementId).val(info.unitCode);
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("[name='productVendorIDArr']").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan2").html($("#notOnlyOneWarning").val());
				$("#errorDiv2").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv2").hide();
			
			var price = info.price;//销售价格
			// 目前未使用此配置项，统一使用销售价格
			var sysConfigUsePrice = $("#sysConfigUsePrice").val();
			if(sysConfigUsePrice == "MemPrice"){
				price = info.memPrice;//会员价格
			} else if(sysConfigUsePrice == "StandardCost") {
				price = info.standardCost;// 结算价
			}
			
			//设置隐藏值
			$('#'+info.elementId).parent().parent().find("[name='prtVendorId']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='productVendorIDArr']").val(info.prtVendorId);
			$('#'+info.elementId).parent().parent().find("[name='priceUnitArr']").val(price);
			$('#'+info.elementId).parent().parent().find("[name='referencePriceArr']").val(price);
			$('#'+info.elementId).parent().parent().find("#unitCodeArr").val(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#barCodeArr").val(info.barCode);
			
			//设置显示值
			$('#'+info.elementId).parent().parent().find("#spanUnitCode").html(info.unitCode);
			$('#'+info.elementId).parent().parent().find("#spanBarCode").html(info.barCode);
			$('#'+info.elementId).parent().parent().find("#spanProductName").html(info.productName);
			$('#'+info.elementId).parent().parent().find("#spanPrice").html(BINOLWSMNG01.formateMoney(price,2));
			$('#'+info.elementId).parent().parent().find("#priceUnitArr").val(price);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
//			//跳到下一个文本框
//			var nxtIdx = $('#'+info.elementId).parent().parent().find("input:text").index($('#'+info.elementId)) + 1;
//			var $nextInputText =  $('#'+info.elementId).parent().parent().find("input:text:eq("+nxtIdx+")");
			
			//跳到数量文本框
			var $quantityInputText =  $('#'+info.elementId).parent().parent().find("#quantityuArr");

			//选中一个产品后后默认发货数量1
			var quantity = $('#'+info.elementId).parent().parent().find("#quantityuArr").val();
			if(quantity == ""){
				$('#'+info.elementId).parent().parent().find("#quantityuArr").val("1");
			}
			//计算金额（防止先输入数量后输入产品）
			BINOLWSMNG01.changeCount($quantityInputText);
			
			$quantityInputText.select();
			
			//查询库存
			BINOLSTSFH01.getPrtStock(info.prtVendorId);
			
		}
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
	 * 删除选中行
	 */
	"deleterowWithOne":function(){
		BINOLSTSFH01.clearActionMsg();
		$("#databody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){
				$this.parent().parent().remove();
			}
		});
		$('#allSelect').prop("checked",false);
		BINOLSTSFH01.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
		
		$("input[type=checkbox]").prop("checked",false);
		BINOLWSMNG01.calcTotal();
		//以下代码暂未使用
//		if($("#databody tr").length == 0) {
//			this.addNewLine();
//		}
	},
	
	/*
	 * 保存订单
	 */
	"saveExt":function(){
		if(!BINOLWSMNG01.checkData()){
			return false;
		}
		var url = document.getElementById("saveURL").innerHTML;
		var operateType = $("#operateType").val();
		if(operateType == '2') {
			url = $("#save_url").attr("href");
		}
		//删除没有选择产品的行
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val()==""){
				$(this).remove();
			}
		});
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback,
			coverId:"body"
		});
	},
	
	/**
	 * 提交前对数据进行检查
	 * @returns {Boolean}
	 */
	"checkData":function(){
		BINOLSTSFH01.clearActionMsg();
		var flag = true;
		var operateType = $("#operateType").val();
		if(operateType == "newBill"){
			var tradeEmployeeID = $("#tradeEmployeeID").val();
			if(tradeEmployeeID == ""){
				$("#errorSpan2").html($("#errmsg_EST00039").val());
				$("#errorDiv2").show();
				return false;
			}
		}
		if(Number($("#inDepotId option:selected").val()) == 0){
			flag = false;
			$("#errorSpan2").html($("#errmsg4").val());
			$("#errorDiv2").show();
			return;
		}
		if($("#outOrganizationId").val() == ""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00040').val());
			$('#errorDiv2').show();
			return false;
		}
		
		//检查有无空行
		var count = 0;
		$.each($('#databody >tr'), function(i){
			if(i>=0){
				if($(this).find("#productVendorIDArr").val()!=""){
					//跳过没有选择产品的行
					count +=1;
					if($(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0"){
						flag = false;
						$(this).attr("class","errTRbgColor");				
					}else{
						$(this).removeClass('errTRbgColor');				
					}
				}

			}
		});
		BINOLSTSFH01.changeOddEvenColor1();
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
	
	/*
	 * 提交订单
	 */
	"submitExt":function(){
		if(!BINOLWSMNG01.checkData()){
			return false;
		}
		var url = document.getElementById("submitURL").innerHTML;
		var operateType = $("#operateType").val();
		if(operateType == '2') {
			url = $("#submit_url").attr("href");
		}
		//删除没有选择产品的行
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val()==""){
				$(this).remove();
			}
		});
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback,
			coverId:"body"
		});
	},
	
	/*
	 * 计算总金额，总数量
	 */
	"calcTotal":function(){
		//计算总金额、总数量
		var rows = $("#databody").children();
		var totalQuantity = 0;
		var totalAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var quantity = Number($(this).find("#quantityArr").val());
				var amount = Number($(this).find("[name='priceUnitArr']").val());
				totalQuantity += quantity;
				totalAmount += quantity * amount;
			});
		}
		$("#totalQuantity").html(BINOLWSMNG01.formateMoney(totalQuantity,0));
		$("#totalAmount").html(BINOLWSMNG01.formateMoney(totalAmount,2));
	},
	
	/**
	 * 输入了订货数量
	 * @param thisObj
	 */
	"changeCount":function(obj){
		var $this = $(obj);
		var $thisTd = $this.parent();
		if(isNaN($this.val().toString())){
			$this.val("0");
			$thisTd.next().text("0.00");
			$this.parent().parent().find(":input[name=applyQuantityArr]").val("0");
		}else{
			var price = $thisTd.parent().find("#priceUnitArr").val();
			var quantity = Math.abs(parseInt(Number($this.val())));
			$this.val(quantity);
			var amount = price * Number(quantity);
			$thisTd.next().text(BINOLWSMNG01.formateMoney(amount,2));
			$this.parent().parent().find(":input[name=applyQuantityArr]").val(quantity);
		}
		BINOLWSMNG01.calcTotal();
	},
	"getHtmlFun":function(info,negativeFlag){
		var productVendorId = info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//产品名称
		var price = info.salePrice;//销售价格
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);
//		var curStock = info.curStock;//当前库存
//		if(isEmpty(curStock)){
//			curStock = 0;
//		}
		var quantity=info.quantity;//数量
		if(isEmpty(quantity)){
			quantity = "";
		}
		if(!isEmpty(quantity) && negativeFlag){
			quantity = Number(quantity)*(-1);
		}
		var amount = price * Number(quantity);//金额
		amount = amount.toFixed(2);
		var reason = info.reason;//备注
		if(quantity == 0){quantity = "";}
		if(isEmpty(reason)){reason = "";}
		var html = '<tr>';
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH01.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr" value="'+ unitCode + '" name="unitCodeBinding"/></td>';
		html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td>' + nameTotal + '</td>';
		html += '<td style="text-align:right;" >'+ price + '</td>';
		html += '<td style="text-align:right;"></td>';
		html += '<td class="center"><input value="' + quantity +'" name="quantityArr" id="quantityArr" cssClass="text-number" onchange="BINOLWSMNG01.changeCount(this)" oninput="BINOLWSMNG01.changeCount(this)" style="width:120px;text-align:right;" size="10" maxlength="9" onchange="BINOLSTSFH01.calcAmount(this)" oninput="BINOLSTSFH01.calcAmount(this)" cssStyle="width:98%"/></td>';
		html += '<td class="center" style="text-align:right;">' + amount + '</td>';
		//html += '<td class="center"><input value="' + reason +'" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200"  cssStyle="width:98%"/></td>';
		
		var operateType = $("#operateType").val();
		if(operateType == '2') {
			html += '<td class="center"><input value="" name="commentsArr"  type="text" id="reasonArr" size="25" maxlength="200" /></td>';
		} else {
			html += '<td class="center"><input value="" name="reasonArr"  type="text" id="reasonArr" size="25" maxlength="200" /></td>';
		}
		
		html +='<td style="display:none">';
		// 插入firstDetailData2
		if(operateType == '2') {
			html += $("#firstDetailData2").html();	
			
			html += '<input type="hidden" name="referencePriceArr" value="'+ price + '"/>'
		} 
		html += '<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="priceUnitArr" name="priceUnitArr" value="'+ price +'"/>';
		html += '</td>';
		html += '</tr>';
		return html;
	},
	/**
	 * 产品弹出框
	 * 
	 * */
	"openProPopup":function(){
		var that = this;
		BINOLWSMNG01.clearActionMsg();
		if($('#inOrganizationId').val()==null || $('#inOrganizationId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepart').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#inDepotId option:selected").val()==""){
			$('#errorDiv2 #errorSpan2').html($('#noOutDepot').val());
			$('#errorDiv2').show();
			return false;
		}
		// 产品弹出框属性设置
		var option = {
			targetId: "databody",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			brandInfoId : $("#brandInfoId").val(),// 品牌ID
			getHtmlFun:that.getHtmlFun,// 目标区追加数据行function
			click:function(){//点击确定按钮之后的处理
				var checkbox= $('#dataTableBody').find(':input').is(":checked");
				//改变奇偶行的样式
				BINOLWSMNG01.changeOddEvenColor();
				// 拖动排序
				$("#mainTable #databody").sortable({
					update: function(event,ui){BINOLWSMNG01.changeOddEvenColor();}
				});
				// 设置全选状态
				var $checkboxs = $('#databody').find(':checkbox');
				var $unChecked = $('#databody').find(':checkbox:not(":checked")');
				if($unChecked.length == 0 && $checkboxs.length > 0){
					$('#allSelect').prop("checked",true);
				}else{
					$('#allSelect').prop("checked",false);
				}
				// AJAX取得产品当前库存量
				BINOLSTSFH01.getPrtStock();
				//计算总金额总数量
				BINOLWSMNG01.calcTotal();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
	},
	/**
	 * 删除掉画面头部的提示信息框
	 * @return
	 */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	}
		
}

var BINOLWSMNG01 = new BINOLWSMNG01();