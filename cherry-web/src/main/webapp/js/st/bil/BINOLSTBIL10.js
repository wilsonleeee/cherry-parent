var binOLSTBIL10_global = {};
//是否需要解锁
binOLSTBIL10_global.needUnlock = true;

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
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	initDetailTable();
	//tableSort("#sort_table");
	$("#exportButton").prop("disabled",false);
	
	$('.tabs').tabs();
} );


var BINOLSTBIL10 = function () {};
BINOLSTBIL10.prototype = {
		
		//是否盲盘
		"blindFlag":false,
		
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
		"openProPopup":function(_this){
			if($('#depotInfoID').val()==null || $('#depotInfoID').val()==""){
				$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00006').val());
				$('#errorDiv2').show();
				return false;
			}
			var that = this;
			that.clearActionMsg();
			that.changeOddEvenColor();
			var option = {
	         	   targetId: "databody",
	 	           checkType : "checkbox",
	 	           mode : 2,
	 	           brandInfoId : $("#brandInfoId").val(),
	 		       getHtmlFun:that.getHtmlFun,// 选择产品后填充明细数据
	 		       click : function(){
						that.changeOddEvenColor();
						that.getStocks();
						that.calcTotal();
						var $checkboxs = $('#databody').find(':checkbox');
		        	    var $unChecked = $('#databody').find(':checkbox:not(":checked")');
		        	    if($unChecked.length == 0 && $checkboxs.length > 0){
		        		    $('#allSelect').prop("checked",true);
		        	    }else{
		        		    $('#allSelect').prop("checked",false);
		        	    }
//		        	    BINOLSTIOS05.bindInput();		         	    
//		    			//重新绑定下拉框
//		    			$("#databody tr [name='unitCodeBinding']").each(function(){
//		    				var unitCode = $(this).attr("id");
//		    				BINOLSTIOS05.setProductBinding(unitCode);
//		    			});
					}
	 	        };
	 		popAjaxPrtDialog(option);
		},
		
		//批量取得库存
		"getStocks":function(){
			//当非批次盘点时
				var count = 0;
				//ajax取得该产品的库存数量
				var param = "depotInfoId="+$("#depotInfoID").val();
				if ($("#logicInventoryInfoID").length>0 && $("#logicInventoryInfoID").val()!=""){
					param += "&logicDepotsInfoId="+$("#logicInventoryInfoID").val();
				}
				$("#databody").find("tr").each(function(i){
						param += "&productVendorIDArr="+$(this).find("[name=productVendorIDArr]").val();
						count ++;
				});
				if(count == 0){
					return false;
				}
				
				var callback = function(msg){
					var stocks = eval('('+msg+')');
					$("#databody").find("tr").each(function(i){
						if($(this).find("#hasproductflagArr").val()=="0"){
							var index = $(this).find("#productVendorIDArr").val();
							$(this).find("#hasproductflagArr").val("1");
							$(this).find("#dataTd5").html(stocks[index]);
							$(this).find("#bookQuantityArr").val(stocks[index]);
						}
					});
				};
				
				cherryAjaxRequest({
					url:$("#s_getStockCount").html(),
					reloadFlg:true,
					param:param,
					callback:callback
				});	
		},
		
		"getHtmlFun":function(info){
			if(!info){
				return ;
			}
			var rowNumber = Math.abs($('#rowNumber').val())+1;
			$('#rowNumber').val(rowNumber);
			var nextIndex = $('#rowNumber').val();
			
			var html = [];
			html.push('<tr id="tr_'+nextIndex+'">');
			//chcekbox
			html.push('<td class="center"><input type="hidden" name="prtVendorId" value="'+info.proId+'"></input><input id="chkbox" type="checkbox" onclick="BINOLSTBIL10.changechkbox(this);"/></td>');
			//UnitCode
			html.push('<td id="dataTd1" style="white-space:nowrap">'+info.unitCode.unEscapeHTML()+'</td>');
			//BarCode
			html.push('<td id="dataTd2">'+info.barCode.unEscapeHTML()+'</td>');
			//名称
			html.push('<td id="dataTd3">'+info.nameTotal.unEscapeHTML()+'</td>');
			//批次号
			if($("#BatchStockTakingTrue").prop("checked") == true){
				html.push('<td id="dataTd4"><span><input name="batchNoArr" id="batchNoArr" style="width:95%;" class="text-number" maxlength="20" onchange="BINOLSTBIL10.checkBatchNo(this);"></input></span></td>');
			}
			//当前库存
			if(!BINOLSTBIL10.blindFlag){
				html.push('<td id="dataTd5" style="text-align:right;"></td>');
			}
			//数量
			html.push('<td id="dataTd6"><span><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTBIL10.changeCount(this);"></input></span></td>');
			//盘差
			if(!BINOLSTBIL10.blindFlag){
				html.push('<td id="gainCount" style="text-align:right;"></td>');
			}
			//备注
			html.push('<td id="dataTd8"><span><input name="commentsArr" style="width:95%;" id="commentsArr" size="25" maxlength="200"></input></span></td>');
			//隐藏
			html.push('<td id="dataTd11" style="display:none">');
			html.push('<input type="hidden" name="bookQuantityArr" id="bookQuantityArr" value=""></input>');
			html.push('<input type="hidden" name="gainQuantityArr" id="gainQuantityArr" value=""></input>');
			html.push('<input type="hidden" name="productVendorIDArr" id="productVendorIDArr" value="'+info.proId+'"></input>');
			html.push('<input type="hidden" name="hasproductflagArr" id="hasproductflagArr" value="0"></input>');
			html.push('<input type="hidden" name="priceUnitArr" id="priceUnitArr" value="'+info.salePrice+'"></input>');
			html.push('</td>');
			html.push('</tr>');
			return html.join("");
		},
		/**
		 * 开放修改单据
		 * param 定义
		 * @return
		 */
		"modifyOrder":function(param){

			$("#showToolbar").removeClass("hide");//显示工具栏			
			$("#sort_table").hide();//隐藏显示数据
			$("#sort_table1").show();//展示编辑数据
			
			// 暂存按钮
			$("#btnReturn").css("display","");
			$("#btn-icon-edit-big").css("display","none");
			$("#btnSave").css("display","");
			$("#btn-icon-discard").css("display","none");


		},
		"back":function(){
			binOLSTBIL10_global.needUnlock=false;
			var tokenVal = $('#csrftoken',window.opener.document).val();
			$('#productOrderDetailUrl').find("input[name='csrftoken']").val(tokenVal);
			$('#productOrderDetailUrl').submit();
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
		 * 全选，全不选
		 */
		"selectAll":function(){
			if($('#allSelect').prop("checked")){
				$("#databody").find("input[type='checkbox']").each(function(){
						$(this).prop("checked",true);						
					});
				}
			else{
				$("#databody").find("input[type='checkbox']").each(function(){
					$(this).prop("checked",false);});
			}
		},
		/**
		 * 输入了盘点数量
		 * @param thisObj
		 */
		"changeCount":function(thisObj){
			var $this = $(thisObj);
			if($this.val()=='') {
				return false;
			}
			var $tr_obj = $this.parent().parent().parent();
			var quantity = parseInt($(thisObj).val(),10);
			var allowNegativeFlag = $("#allowNegativeFlag").val();
			if(allowNegativeFlag != "0"){
				quantity = Math.abs(quantity);
			}
			if(isNaN(quantity)){		
				$(thisObj).val("");
				//清空盘差
				$tr_obj.find('#gainCount').html("");
				$tr_obj.find('#gainQuantityArr').val("");
			}else{
				$(thisObj).val(quantity);
				if($tr_obj.find('#bookQuantityArr').val() != "" && $tr_obj.find('#bookQuantityArr').val() != undefined){
					var tempCount = Number(quantity)-Number($tr_obj.find('#bookQuantityArr').val());
					$tr_obj.find('#gainCount').html(tempCount);
					$tr_obj.find('#gainQuantityArr').val(tempCount);
				}
			}	
			BINOLSTBIL10.calcTotal();//计算总金额，总数量
		},
		
		/**
		 * 删除选中行
		 * @return
		 */
		"deleteRow":function(){
			this.clearActionMsg();
			$("#databody").find("input[type='checkbox']").each(function(i){
				if($(this).prop("checked"))
					{					
						$('#tr_' +(i+1)).remove();	
					}					
				});
			
			//重置序号
			$.each($('#databody >tr'), function(i){	
				$(this).attr("id","tr_"+(i+1));
			});	
			
			this.changeOddEvenColor();
			$('#allSelect').prop("checked",false);
			BINOLSTBIL10.calcTotal();
			$('#rowNumber').val($("#databody").children().length);
		},
		
		//隔行变色
		"changeOddEvenColor":function(){
			$("#databody").find("tr").each( function(i){
				$("#databody tr").removeClass("even");
				$("#databody tr").removeClass("odd");
				$("#databody tr:odd").addClass("even");
				$("#databody tr:even").addClass("odd");
			});	
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
			var totalStackQuantity = 0;//总库存数量
			var totalSuggestedQuantity = 0;//总实盘数量
			var y_totalQuantity = 0;//总盘盈数量
			var y_totalAmount = 0.00;//总盘盈金额
			var k_totalQuantity = 0;//总盘亏数量
			var k_totalAmount = 0.00;//总盘亏金额
			
			
			if(rows.length > 0){
				rows.each(function(i){
					var quantity = Number($(this).find("#quantityArr").val().replace(",",""));
					var bookQuantityArr =  Number($(this).find("#bookQuantityArr").val().replace(",",""));
					var gainQuantityArr =  Number($(this).find("#gainQuantityArr").val().replace(",",""));
					var amount = Number($(this).find("[name='priceUnitArr']").val().replace(",",""));
					if(gainQuantityArr>0){//盘盈数量
						y_totalQuantity += gainQuantityArr;
						y_totalAmount += gainQuantityArr * amount;
					}
					if(gainQuantityArr<0){//盘亏数量						
						 k_totalQuantity += gainQuantityArr;
						 k_totalAmount += gainQuantityArr * amount;
					}					
					
					totalStackQuantity += bookQuantityArr;
					totalSuggestedQuantity += quantity;
				});
			}		
			
		
			$("#totalStackQuantity").html(this.formateMoney(totalStackQuantity,0));//总库存数量
			$("#totalSuggestedQuantity").html(this.formateMoney(totalSuggestedQuantity,0));//总实盘数量					
			$("#y_totalQuantity").html(this.formateMoney(y_totalQuantity,0));//总盘盈数量
			$("#y_totalAmount").html(this.formateMoney(y_totalAmount,2));//总盘盈金额
			$("#k_totalQuantity").html(this.formateMoney(k_totalQuantity,0));//总盘亏数量
			$("#k_totalAmount").html(this.formateMoney(k_totalAmount,2));//总盘亏金额
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
		/*
		 * 保存订单
		 */
		"saveOrder":function(){
			this.clearActionMsg();
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
		}
}

var BINOLSTBIL10 = new BINOLSTBIL10();