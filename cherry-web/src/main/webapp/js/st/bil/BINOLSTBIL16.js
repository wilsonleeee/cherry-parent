var binOLSTBIL16_global = {};
//是否需要解锁
binOLSTBIL16_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLSTBIL16_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};

$(document).ready(function() {
	$('.tabs').tabs();
	initDetailTable();
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	$("#exportButton").prop("disabled",false);
} );

var BINOLSTBIL16 = function () {};
BINOLSTBIL16.prototype = {
	//产品弹出框 盘点申请修改不能添加删除
	"openProPopup":function(_this){
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
				
				//账面数量
				var bookQuantity = 0;
				
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTBIL16.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				html += '<td id="dataTd5" class="alignRight">'+BINOLSTBIL16.formateMoney(info.standardCost,2)+'</td>';
				html += '<td id="dataTd6" class="alignRight">'+bookQuantity+'</td>';
				html += '<td id="nowCount" class="alignRight"><input name="checkQuantityArr" id="checkQuantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTBIL16.changeCount(this);"/></td>';
				html += '<td id="dataTd7" class="alignRight">'+0+'</td>';
				html += '<td id="money" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd8">'+$("#defaultHandleType").text()+'</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width:98%"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.standardCost+'"/>';
				html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
				html += '<input type="hidden" id="productVendorPackageIDArr'+index+'" name="productVendorPackageIDArr" value="0"/>';
				html += '<input type="hidden" id="inventoryInfoIDArr'+index+'" name="inventoryInfoIDArr" value="'+$("#BIN_InventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="logicInventoryInfoIDArr'+index+'" name="logicInventoryInfoIDArr" value="'+$("#BIN_LogicInventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="storageLocationInfoIDArr'+index+'" name="storageLocationInfoIDArr" value="0"/>';
				html += '<input type="hidden" id="bookQuantityArr'+index+'" name="bookQuantityArr" value="'+bookQuantity+'"/>';
				html += '<input type="hidden" id="gainQuantityArr'+index+'" name="gainQuantityArr" value="0"/>';
				html += '<input type="hidden" id="handleTypeArr'+index+'" name="handleTypeArr" value="2"/>';
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
				BINOLSTBIL16.changeOddEvenColor();
				BINOLSTBIL16.calcTotal();
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
			if($(this).prop("checked")!=true){
				flag=false;
			}
		});
		if(flag){
			$('#allSelect').prop("checked",true);
		}
	},
	
	/**
	 * 删除选中行
	 * @return
	 */
	"deleteRow":function(){
		this.clearActionMsg();
		$("#databody :checkbox").each(function(){
			if($(this).prop("checked")){
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
		BINOLSTBIL16.calcTotal();
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
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
			if($(this).find("#quantityArr").val()==""){
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
	 * 输入了盘点实盘数量
	 * @param thisObj
	 */
	"changeCount":function(thisObj){
		var $tr_obj = $(thisObj).parent().parent();
		var trID = $tr_obj.attr('id');
		var index = trID.substr(7);
		var checkQuantity = parseInt($(thisObj).val(),10);
		var allowNegativeFlag = $("#allowNegativeFlag").val();
		if(allowNegativeFlag != "1"){
			checkQuantity = Math.abs(checkQuantity);
		}
		var price = $("#"+trID).find("[name='priceUnitArr']").val();
		if(isNaN(checkQuantity)){
			$(thisObj).val("");
			$("#"+trID + " > #dataTd7").html(0);
			//$("#"+trID + " > #dataTd10 > [name='gainQuantityArr']").val(0);
			$("#"+trID + " > #money").html("0.00");
			BINOLSTBIL16.calcTotal();
			return;
		}	
		$(thisObj).val(checkQuantity);
		
		//重新计算盘差
		var gainQuantity = checkQuantity  -$("#"+trID + " > #dataTd6").text();
		$("#"+trID + " > #dataTd7").html(gainQuantity);
		//$("#"+trID + " > #dataTd10 > [name='gainQuantityArr']").val(gainQuantity);
		
		//重新计算盘差金额
		$("#"+trID + " > #money").html(this.formateMoney(gainQuantity*price,2));
		
		BINOLSTBIL16.calcTotal();
	},
	
	/*
	 * 保存退库申请单
	 */
	"saveForm":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#saveUrl").attr("href");
		var callback = function(msg){
			if(oTableArr[1] != null)oTableArr[1].fnDraw();
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
	"submitForm":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return false;
		}
		var params=$('#mainForm').formSerialize();
		var url = $("#submitUrl").attr("href");
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
	 * 开放修改单据
	 * @return
	 */
	"modifyForm":function(){
		$("#showBtnopenDepartBox").removeClass("hide");
		$("#showAcceptDepotCodeName").removeClass("hide");
		$("#showAcceptLogicInventoryName").removeClass("hide");
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$("#showOrderTypeSelect").removeClass("hide");
		
		$("#hideAcceptDepotCodeName").addClass("hide");
		$("#hideAcceptLogicInventoryName").addClass("hide");
		$("#hideOrderType").addClass("hide");
		
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("#dataTd6").removeClass("hide");
			$(this).find("[name='checkQuantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
			
			$(this).find("#hideCheckQuantiyArr").addClass("hide");
			$(this).find("#hideReason").addClass("hide");
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		//$("#btnSave").css("display","");
		return "";
	},
	
	"back":function(){
		binOLSTBIL16_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#proStocktakeRequestIDDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#proStocktakeRequestIDDetailUrl').submit();
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
		var sumQuantity = 0;
		var sumRealQuantity = 0;
		var overQuantity = 0;
		var overAmount = 0.00;
		var shortQuantity = 0;
		var shortAmount = 0.00;
		if(rows.length > 0){
			rows.each(function(i){
				var bookQuantity = Number($(this).find("[name='bookQuantityArr']").val());
				var checkQuantity = Number($(this).find("[name='checkQuantityArr']").val());
				var gainQuantity = checkQuantity-bookQuantity;
				var amount = Number($(this).find("[name='priceUnitArr']").val());
				
				sumQuantity += bookQuantity;
				sumRealQuantity += checkQuantity;
				
				if(gainQuantity>0){
					overQuantity += gainQuantity;
					overAmount += gainQuantity * amount;
				}else{
					shortQuantity += gainQuantity;
					shortAmount += gainQuantity * amount
				}
			});
		}
		$("#sumQuantity").html(this.formateMoney(sumQuantity,0));
		$("#sumRealQuantity").html(this.formateMoney(sumRealQuantity,0));
		$("#overQuantity").html(this.formateMoney(overQuantity,0));
		$("#overAmount").html(this.formateMoney(overAmount,2));
		$("#shortQuantity").html(this.formateMoney(shortQuantity,0));
		$("#shortAmount").html(this.formateMoney(shortAmount,2));
	}
};
var BINOLSTBIL16 = new BINOLSTBIL16();