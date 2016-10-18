//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if (window.opener) {
			window.opener.unlockParentWindow();
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

var BINOLSTBIL08 = function () {};
BINOLSTBIL08.prototype = {
//		"STBIL08_dialogBody":"",
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
//		/**
//		 * 新增一行
//		 * @return
//		 */
//		"addRow":function(){
//			this.clearActionMsg();
//			var rowNumber = Math.abs($('#rowNumber').val())+1;
//			var newid = 'dataRow'+rowNumber;
//			$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");	
//			$('#'+newid).find(':checkbox').val(rowNumber);
//			$('#'+newid).find('#productVendorIDArr').attr('id','productVendorIDArr'+rowNumber);
//			$('#'+newid).find('#priceUnitArr').attr('id','priceUnitArr'+rowNumber);
//			
//			$('#'+newid).find('#productVendorPackageIDArr').attr('id','productVendorPackageIDArr'+rowNumber);
//			$('#'+newid).find('#fromDepotInfoIDArr').attr('id','fromDepotInfoIDArr'+rowNumber);
//			$('#'+newid).find('#fromLogicInventoryInfoIDArr').attr('id','fromLogicInventoryInfoIDArr'+rowNumber);
//			$('#'+newid).find('#fromStorageLocationInfoIDArr').attr('id','fromStorageLocationInfoIDArr'+rowNumber);
//			$('#'+newid).find('#toDepotInfoIDArr').attr('id','toDepotInfoIDArr'+rowNumber);
//			$('#'+newid).find('#toLogicInventoryInfoIDArr').attr('id','toLogicInventoryInfoIDArr'+rowNumber);
//			$('#'+newid).find('#toStorageLocationInfoIDArr').attr('id','toStorageLocationInfoIDArr'+rowNumber);
//			
//			$('#rowNumber').val(rowNumber);
//			
//			$('#'+newid).find('#productVendorPackageIDArr'+rowNumber).val($("#BIN_ProductVendorPackageID").val());
//			$('#'+newid).find('#fromDepotInfoIDArr'+rowNumber).val($("#BIN_DepotInfoID").val());
//			$('#'+newid).find('#fromLogicInventoryInfoIDArr'+rowNumber).val($("#FromLogicInventoryInfoID").val());
//			$('#'+newid).find('#fromStorageLocationInfoIDArr'+rowNumber).val($("#FromStorageLocationInfoID").val());
//			$('#'+newid).find('#toDepotInfoIDArr'+rowNumber).val($("#BIN_DepotInfoID").val());
//			$('#'+newid).find('#toLogicInventoryInfoIDArr'+rowNumber).val($("#ToLogicInventoryInfoID").val());
//			$('#'+newid).find('#toStorageLocationInfoIDArr'+rowNumber).val($("#ToStorageLocationInfoID").val());
//		},
		/**
		 * 删除掉画面头部的提示信息框
		 * @return
		 */
		"clearActionMsg":function(){
			$('#actionResultDisplay').html("");
			$('#errorDiv2').attr("style",'display:none');
		},
		/**
		 * 产品弹出框
		 * @return
		 */
		"openProPopup" : function(_this){
			this.clearActionMsg();
			this.changeOddEvenColor();
//			var proPopParam = {
//					thisObj : _this,
//					  index : 0,
//				  checkType : "checkbox",
//				      modal : true,
//				  autoClose : [],
//				 dialogBody : BINOLSTBIL08.STBIL08_dialogBody,
//				 csrftoken  : "csrftoken="+window.opener.document.getElementById("csrftoken").value
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
					var html = '<tr id="dataRow'+index+'"><td id="dataTd0"><input id="chkbox" name="chkbox" type="checkbox" value="'+index+'" onclick="BINOLSTBIL08.changechkbox(this)"/></td>';
					html += '<td id="dataTd1" style="white-space:nowrap"></td>';
					html += '<td id="dataTd2">'+info.unitCode+'</td>';
					html += '<td id="dataTd3">'+info.barCode+'</td>';
					html += '<td id="dataTd4">'+info.nameTotal+'</td>';
					html += '<td id="dataTd5" class="alignRight">'+BINOLSTBIL08.formateMoney(info.salePrice,2)+'</td>';
					html += '<td id="dataTd7" class="alignRight"><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTBIL08.changeCount(this);"/></td>';
					html += '<td id="money" style="text-align:right;">0.00</td>';
					html += '<td id="dataTd9"><input type="text" name="commentsArr" size="25" maxlength="200" style="width:99%"/></td>';
					html += '<td style="display:none" id="dataTd10">';
					html += '<input type="hidden" id="priceUnitArr'+index+'" name="priceUnitArr" value="'+info.salePrice+'"/>';
					html += '<input type="hidden" id="productVendorIDArr'+index+'" name="productVendorIDArr" value="'+info.proId+'"/>';
					html += '<input type="hidden" id="productVendorPackageIDArr'+index+'" name="productVendorPackageIDArr" value="0"/>';
					html += '<input type="hidden" id="fromDepotInfoIDArr'+index+'" name="fromDepotInfoIDArr" value="'+$("#BIN_DepotInfoID").val()+'"/>';
					html += '<input type="hidden" id="fromLogicInventoryInfoIDArr'+index+'" name="fromLogicInventoryInfoIDArr" value="'+$("#FromLogicInventoryInfoID").val()+'"/>';
					html += '<input type="hidden" id="fromStorageLocationInfoIDArr'+index+'" name="fromStorageLocationInfoIDArr" value="0"/>';
					html += '<input type="hidden" id="toDepotInfoIDArr'+index+'" name="toDepotInfoIDArr" value="'+$("#BIN_DepotInfoID").val()+'"/>';
					html += '<input type="hidden" id="toLogicInventoryInfoIDArr'+index+'" name="toLogicInventoryInfoIDArr" value="'+$("#ToLogicInventoryInfoID").val()+'"/>';
					html += '<input type="hidden" id="toStorageLocationInfoIDArr'+index+'" name="toStorageLocationInfoIDArr" value="0"/>';
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
					$.each($('#databody >tr'), function(i){	
						var index = $(this).attr("id").substr(7);
						$("#dataRow"+index).attr("id","dataRow"+(i+1));
					});
					BINOLSTBIL08.changeOddEvenColor();
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
	"submitShift":function(){
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
	"deleteShift":function(){
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
		$("#databody :checkbox").each(function(i){
			if($(this).prop("checked"))
				{
					$('#dataRow' + (i+1)).remove();	
				}					
			});
		//重置序号
		$.each($('#databody >tr'), function(i){	
			var index = $(this).attr("id").substr(7);
			$("#dataRow"+index+" td:eq(1)").html(i+1);
		});	
		$.each($('#databody >tr'), function(i){	
			var index = $(this).attr("id").substr(7);
			$("#dataRow"+index).attr("id","dataRow"+(i+1));
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
var BINOLSTBIL08 = new BINOLSTBIL08();
$(function(){
	$('.tabs').tabs();
//	// 产品popup初始化
//	BINOLSTBIL08.STBIL08_dialogBody = $('#productDialog').html();
});

//function selectProduct () {
//	var selectedArr = $("#dataTableBody").find(":input[checked]");
//	
//	var length = selectedArr.length;
//	for(var i=0;i<length;i++){
//		BINOLSTBIL08.addRow();
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
//		$('#productVendorIDArr' + index).val(productVendorID);
//		
//		//序号
//		var curNo = $("#databody").find("tr").length-1;
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
//		if(!selectedObj.salePrice){
//			price = "0.00";
//		}else{
//			price = selectedObj.salePrice;
//		}
//		$('#'+trID).find('#dataTd5').html(price);
//		$('#'+trID).find('#dataTd10 #priceUnitArr'+index).val(price);
//		BINOLSTBIL08.changeOddEvenColor1();
//	}
//	closeCherryDialog('productDialog',BINOLSTBIL08.STBIL08_dialogBody);	
//	oTableArr[0]= null;	
//}
