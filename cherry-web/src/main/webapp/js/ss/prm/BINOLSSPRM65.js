var binOLSSPRM65_global = {};
//是否需要解锁
binOLSSPRM65_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

function BINOLSSPRM65(){}

BINOLSSPRM65.prototype={
	/**
	 * 产品弹出框
	 * @return
	 */
	"openPrmPopup":function(_this){
		var that = this;
		this.clearActionMsg();
		this.changeOddEvenColor();
		
		// 产品弹出框属性设置
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
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" name="chkbox" type="checkbox" value="'+index+'" onclick="binOLSTBIL02.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				//html += '<td id="dataTd5"><input type="text" name="batchNoArr" id="batchNoArr" class="text-number" maxlength="20" /></td>';
				html += '<td id="dataTd6" class="alignRight">'+binOLSSPRM65.formateMoney(info.standardCost,2)+'</td>';
				var operateType = $("#operateType").val();
				if(operateType == "11" || operateType == "12"){
					html += '<td id="dataTd7" class="alignRight"></td>';
				}
				html += '<td id="newCount" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="binOLSSPRM65.changeCount(this);"/></td>';
				html += '<td id="preMoney" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width:98%"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.standardCost+'"/>';
				html += '<input type="hidden" id="prmVendorIDArr'+index+'" name="prmVendorIDArr" value="'+info.proId+'"/>';
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
				//重置序号
				$.each($('#databody >tr'), function(i){	
					var index = $(this).attr("id").substr(7);
					$("#dataRow"+index+" td:eq(1)").html(i+1);
				});
				binOLSSPRM65.setOddEvenColor();
				//ajax取得选中产品的库存数量
				var param ='depotId='+$('#inventoryInfoID').val()+'&loginDepotId='+$("#logicInventoryInfoID").val();
				var len = $("#databody tr").length;
				if(len>0){
					for(var i=0;i<len;i++){
						var prmVendorID = $($("#databody tr [name=prmVendorId]")[i]).val();
						var currentIndex = $($("#databody tr :checkbox")[i]).val();
						param += "&promotionProductVendorID="+prmVendorID;
						param += "&currentIndex="+currentIndex;
					}
					cherryAjaxRequest({
						url:$("#s_getStockCount").html(),
						reloadFlg:true,
						param:param,
						callback:binOLSSPRM65.getStockSuccess
					});
				}
			}
		};
		// 调用产品弹出框共通
		popAjaxPrmDialog(option);
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
			$('#hasprmflagArr'+member[i].currentIndex).val(member[i].hasprmflag);
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
	},
	
	"saveIndepot":function(){
		if(!this.checkDate()){
			return false;
		}
		var url = document.getElementById("saveIndepot").href;
		var param = $("#mainForm").serialize();
		cherryAjaxRequest({
			url: url,
			param: param
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
			$this.parent().next().html(binOLSSPRM65.formateMoney(amount,2));
		}
		binOLSSPRM65.calcTotal();
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
		$("#preTotalQuantity").html(binOLSSPRM65.formateMoney(totalQuantity,0));
		$("#preTotalAmount").html(binOLSSPRM65.formateMoney(totalAmount,2));
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
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
			
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideComments").addClass("hide");
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		//$("#btnSave").css("display","");
		$("#btnEdit").css("display","none");
		$("#btn-icon-delete-big").css("display","none");
		//$("#btn-icon-inhibit").css("display","none");
	},
	
	"back":function(){
		OS_BILL_Jump_needUnlock = false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#prm64DetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#prm64DetailUrl').submit();
	}
};



var binOLSSPRM65 = new BINOLSSPRM65();

$(document).ready(function(){
	 $('.tabs').tabs();
//	// 产品popup初始化
//	binOLSTBIL02.STBIL02_dialogBody = $('#productDialog').html();
	initDetailTable();
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