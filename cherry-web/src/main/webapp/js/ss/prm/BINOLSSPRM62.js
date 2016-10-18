var binOLSSPRM62_global = {};
//是否需要解锁
binOLSSPRM62_global.needUnlock = true;

//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if(binOLSSPRM62_global.needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	}
};

$(document).ready(function() {
	initDetailTable();
	if (window.opener) {
       window.opener.lockParentWindow();
     }
	 $("#exportButton").prop("disabled",false);

} );

var BINOLSSPRM62 = function () {};
BINOLSSPRM62.prototype = {
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
			if($(this).find("#quantityArr").val()==""||$(this).find("#quantityArr").val()=="0"){
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
	 * 删除掉画面头部的提示信息框
	 * @return
	 */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},
		
	/**
	 * 促销品弹出框
	 * @return
	 */
	"openPrmPopup" : function(_this){
		this.clearActionMsg();
		this.changeOddEvenColor();
		
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
				var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" name="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSSPRM62.changechkbox(this)"/></td>';
				html += '<td id="dataTd1" style="white-space:nowrap"></td>';
				html += '<td id="dataTd2">'+info.unitCode+'</td>';
				html += '<td id="dataTd3">'+info.barCode+'</td>';
				html += '<td id="dataTd4">'+info.nameTotal+'</td>';
				html += '<td id="dataTd5" class="alignRight">'+BINOLSSPRM62.formateMoney(info.standardCost,2)+'</td>';
				html += '<td id="dataTd7" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSSPRM62.changeCount(this);"/></td>';
				html += '<td id="money" style="text-align:right;">0.00</td>';
				html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width:99%"/></td>';
				html += '<td style="display:none" id="dataTd10">';
				html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.standardCost+'"/>';
				html += '<input type="hidden" id="productVendorPackageIDArr'+index+'" name="productVendorPackageIDArr" value="0"/>';
				html += '<input type="hidden" id="fromDepotInfoIDArr'+index+'" name="fromDepotInfoIDArr" value="'+$("#BIN_DepotInfoID").val()+'"/>';
				html += '<input type="hidden" id="fromLogicInventoryInfoIDArr'+index+'" name="fromLogicInventoryInfoIDArr" value="'+$("#FromLogicInventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="fromStorageLocationInfoIDArr'+index+'" name="fromStorageLocationInfoIDArr" value="0"/>';
				html += '<input type="hidden" id="toDepotInfoIDArr'+index+'" name="toDepotInfoIDArr" value="'+$("#BIN_DepotInfoID").val()+'"/>';
				html += '<input type="hidden" id="toLogicInventoryInfoIDArr'+index+'" name="toLogicInventoryInfoIDArr" value="'+$("#ToLogicInventoryInfoID").val()+'"/>';
				html += '<input type="hidden" id="toStorageLocationInfoIDArr'+index+'" name="toStorageLocationInfoIDArr" value="0"/>';
				html += '<input type="hidden" id="prmVendorIDArr'+index+'" name="promotionProductVendorIDArr" value="'+info.proId+'"/>';
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
				BINOLSSPRM62.changeOddEvenColor();
				BINOLSSPRM62.calcTotal();
			}
		};
		// 调用促销品弹出框共通
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
	 * 输入了数量
	 * @param thisObj
	 */
	"changeCount":function(thisObj){
		var $tr_obj = $(thisObj).parent().parent();
		var trID = $tr_obj.attr('id');
		var index = trID.substr(7);
		var count = Math.abs(parseInt($(thisObj).val(),10));
		var price = $("#"+trID + " > #dataTd5").html();
		if(isNaN(count)){
			$(thisObj).val("");
			$("#"+trID + " > #money").html("");
			return;
		}	
		$(thisObj).val(count);
		$("#"+trID + " > #money").html((count*price).toFixed(2));
		BINOLSSPRM62.calcTotal();
	},
	
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
		$("#totalQuantity").html(totalQuantity);
		$("#totalAmount").html(totalAmount.toFixed(2));
	},
	
	"modifyForm":function(){
		$("#showToolbar").removeClass("hide");
		$("#showCheckbox").removeClass("hide");
		$.each($('#databody').find('tr'), function(n){
			$(this).find("#dataTd0").removeClass("hide");
			$(this).find("#dataTd6").removeClass("hide");
			$(this).find("[name='quantityArr']").removeClass("hide");
			$(this).find("[name='commentsArr']").removeClass("hide");
				
			$(this).find("#hideQuantiyArr").addClass("hide");
			$(this).find("#hideComments").addClass("hide");
		});
		$("#btnReturn").css("display","");
		$("#btn-icon-edit-big").css("display","none");
		$("#btn-icon-delete-big").css("display","none");
	},
	
	"back":function(){
		binOLSSPRM62_global.needUnlock=false;
		var tokenVal = $('#csrftoken',window.opener.document).val();
		$('#prmShiftDetailUrl').find("input[name='csrftoken']").val(tokenVal);
		$('#prmShiftDetailUrl').submit();
	},
	
	/*
	 * 保存订单
	 */
	"saveShift":function(){
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
	"submitShift":function(){
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
	"changeOddEvenColor1":function(){
		$("#databody tr:odd").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("even");
			}
			
		});
		$("#databody tr:even").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("odd");
			}
			
		});
	},
	
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
	},
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","even");
		$("#databody tr:even").attr("class","odd");
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
	}
};
var BINOLSSPRM62 = new BINOLSSPRM62();
$(function(){
	$('.tabs').tabs();
});