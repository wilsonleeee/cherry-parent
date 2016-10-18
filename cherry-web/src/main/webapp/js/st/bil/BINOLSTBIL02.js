/**
 * @author zgl
 *
 */

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

var binOLSTBIL02_global = {};
//是否需要解锁
binOLSTBIL02_global.needUnlock = true;

function BINOLSTBIL02(){}

BINOLSTBIL02.prototype={
	/**
	 * 产品弹出框
	 * @return
	 */
	"openProPopup":function(_this){
		var that = this;
		this.clearActionMsg();
		this.changeOddEvenColor();
		
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
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" name="chkbox" type="checkbox" value="'+index+'" onclick="binOLSTBIL02.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				html += '<td id="dataTd5"><input type="text" name="batchNoArr" id="batchNoArr" class="text-number" maxlength="20" style=""/></td>';
				var price = info.salePrice;
				if(isEmpty(price)){
					price = parseFloat("0.00");
				}else{
					price = parseFloat(price);
				}
				price = price.toFixed(2);
				html += '<td id="tdReferencePrice" class="alignRight">'+binOLSTBIL02.formateMoney(price,2)+'</td>';
				var discountPrice = price*$("#batchPriceRate").val()/100;
				discountPrice = discountPrice.toFixed(2);
				html += '<td id="dataTd6" class="alignRight"><input type="text" class="price" id="priceUnitArr" name="priceUnitArr" maxlength="17" value="'+ discountPrice + '" onchange="binOLSTBIL02.setPrice(this)"/></td>';
				var operateType = $("#operateType").val();
				if(operateType == "11" || operateType == "12" || operateType == "16"){
					html += '<td id="dataTd7" class="alignRight"></td>';
				}
				html += '<td id="newCount" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="binOLSTBIL02.changeCount(this);"/></td>';
				html += '<td id="preMoney" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style=""/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="referencePriceArr'+index+'" name="referencePriceArr" value="'+price+'"/>';
				html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
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
				binOLSTBIL02.setOddEvenColor();
				//ajax取得选中产品的库存数量
				var param ='depotInfoId='+$('#inventoryInfoID').val()+'&logicDepotsInfoId='+$("#logicInventoryInfoID").val();
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
						callback:binOLSTBIL02.getStockSuccess
					});
				}
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
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
	 * 解析库存JSON
	 * @param msg
	 */
	"getStockSuccess":function(msg){
		var member = eval("("+msg+")"); //数据解析为json 格式  	
		$.each(member, function(i){
			$('#hasproductflagArr'+member[i].currentIndex).val(member[i].hasproductflag);
			$('#dataRow'+member[i].currentIndex +' > #dataTd7').html(member[i].Quantity);
		});
	},
	
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
     */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
		$("#databody tr:odd").attr("class","odd");
		$("#databody tr:even").attr("class","even");
	},
	
	"selectAll":function(obj){
		var _this = obj;
		var subCheckedBox = document.getElementsByName("chkbox");
		for(var i = 0 ; i < subCheckedBox.length ; i++){
			subCheckedBox[i].checked = _this.checked;
		}
	},
	
	"changechkbox":function(obj){
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
		binOLSTBIL02.calcTotal();
	},
	
	"saveIndepot":function(){
		if(!this.checkDate()){
			return false;
		}
		var url = document.getElementById("saveIndepot").href;
		var param = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback:function(msg){
				if(window.opener.oTableArr[1] != null){
					window.opener.oTableArr[1].fnDraw();
				}
			}
		});
	},
	
	"submitIndepot":function(){
		if(!this.checkDate()){
			return false;
		}
		var url = document.getElementById("submitIndepot").href;
		var param = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback:function(msg){
				if(window.opener.oTableArr[1] != null){
					window.opener.oTableArr[1].fnDraw();
				}
			}
		});
	},
	
	"setOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").each(function(){
			if($(this).attr("class").indexOf("errTRbgColor") == -1){
				$(this).attr("class","even");
			}
		});
		$("#databody tr:even").each(function(i){
			if(i != 0){
				if($(this).attr("class").indexOf("errTRbgColor") == -1){
					$(this).attr("class","odd");
				}
			}
		});
	},
	
	"unchecke":function(){
		$("input[type=checkbox]").prop("checked",false);
	},
	
	"changeCount":function(obj){
		var $this = $(obj);
		if(isNaN($this.val().toString())){
			$this.val("");
			$this.parent().next().html("0.00");
		}else{
			var price = Number($this.parent().parent().find("[name='priceUnitArr']").val());
			$this.val(parseInt(Number($this.val())));
			var amount = price*Number($this.val());
			$this.parent().next().html(binOLSTBIL02.formateMoney(amount,2));
		}
		binOLSTBIL02.calcTotal();
	},
	
	/**
	 * 计算申请总数量，总金额
	 * */
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
		$("#preTotalQuantity").html(binOLSTBIL02.formateMoney(totalQuantity,0));
		$("#preTotalAmount").html(binOLSTBIL02.formateMoney(totalAmount,2));
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
	
	"modifyForm":function(){
		var operateType = $("#operateType").val();
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$("#showAddBtn").removeClass("hide");
		$("#showDelBtn").removeClass("hide");
		$("#spanCalPrice").removeClass("hide");
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("[name='batchNoArr']").removeClass("hide");
			$(this).find("[name='priceUnitArr']").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");

			$(this).find("#hideBatchNoArr").addClass("hide");
			$(this).find("#hidePriceArr").addClass("hide");
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideComments").addClass("hide");
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		$("#btnSave").css("display","");
		$("#btn-icon-discard").css("display","none");
		return "";
	},
	
	"back":function(){
		binOLSTBIL02_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#productInDepotDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#productInDepotDetailUrl').submit();
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
		//数量
		var count = $tr_obj.find("#quantityArr").val();
		//画面显示的金额
		$tr_obj.find("#preMoney").html(binOLSTBIL02.formateMoney(count*discountPrice,2));
		binOLSTBIL02.calcTotal();
	},
	
	// 根据折扣率计算折扣价格（批量/单个）
	"setDiscountPrice":function(obj){
		var curRateIndex = $("#curRateIndex").val();
		var findTRParam = "tr:eq("+curRateIndex+")";
		var $priceRate = $("#priceRate");
		var priceRate = $priceRate.val();
		if(isNaN(priceRate) || priceRate == ""){
			priceRate = 100;
		}
		
		if(curRateIndex == ""){
			//批量计算折扣价格
			$("#batchPriceRate").val(parseFloat(priceRate).toFixed("2"));
			findTRParam = "tr";
		}
		
		$.each($('#databody').find(findTRParam), function(n){
			var $tr_obj = $(this);
			
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

			$tr_obj.find("#preMoney").html(binOLSTBIL02.formateMoney(count*discountPrice,2));				
		});
		
		binOLSTBIL02.calcTotal();
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
			var curRateIndex = $("#databody tr").index($tr_obj);
			$("#curRateIndex").val(curRateIndex);
			
			// 原价
			var $costPrice = $tr_obj.find("[name='referencePriceArr']");
			// 折扣价格
			var $discountPrice = $tr_obj.find("[name='priceArr']");
			var costPrice = parseFloat($costPrice.val()).toFixed("2");
			var discountPrice = parseFloat($discountPrice.val()).toFixed("2");
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
	}
};

var binOLSTBIL02 = new BINOLSTBIL02();

$(document).ready(function(){
	 $('.tabs').tabs();
//	// 产品popup初始化
//	binOLSTBIL02.STBIL02_dialogBody = $('#productDialog').html();
	if (window.opener) {
	       window.opener.lockParentWindow();
	}
	window.onbeforeunload = function(){
		if(OS_BILL_Jump_needUnlock){
			if(window.opener){
				window.opener.unlockParentWindow();
			}
		}
	};
	$("#exportButton").prop("disabled",false);
});