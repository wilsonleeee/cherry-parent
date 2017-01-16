var BINOLSTIOS05 = function () {};

BINOLSTIOS05.prototype = {
		
	//是否盲盘
	"blindFlag":false,
		
	"STIOS05_dialogBody":"",
		
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
		if($('#depotInfoId').val()==null || $('#depotInfoId').val()==""){
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
			   freeCount:1,//自由盘点的标志
 		       click : function(){
					that.changeOddEvenColor();
					that.getStocks();
					var $checkboxs = $('#databody').find(':checkbox');
	        	    var $unChecked = $('#databody').find(':checkbox:not(":checked")');
	        	    if($unChecked.length == 0 && $checkboxs.length > 0){
	        		    $('#allSelect').prop("checked",true);
	        	    }else{
	        		    $('#allSelect').prop("checked",false);
	        	    }
	        	    BINOLSTIOS05.bindInput();
	         	    
	    			//重新绑定下拉框
	    			$("#databody tr [name='unitCodeBinding']").each(function(){
	    				var unitCode = $(this).attr("id");
	    				BINOLSTIOS05.setProductBinding(unitCode);
	    			});
				}
 	        };
 		popAjaxPrtDialog(option);
	},
	
	"getHtmlFun":function(info){
		if(!info){
			return ;
		}
		var nextIndex = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(nextIndex);
		var html = [];
		html.push('<tr id="tr_'+nextIndex+'">');
		//chcekbox
		html.push('<td class="center"><input type="hidden" name="prtVendorId" value="'+info.proId+'"></input><input id="chkbox" type="checkbox" onclick="BINOLSTIOS05.changechkbox(this);"/></td>');
		//UnitCode
		html.push('<td id="dataTd1" style="white-space:nowrap">'+info.unitCode.unEscapeHTML()+'</td>');
		//BarCode
		html.push('<td id="dataTd2">'+info.barCode.unEscapeHTML()+'</td>');
		//名称
		html.push('<td id="dataTd3">'+info.nameTotal.unEscapeHTML()+'</td>');
		//批次号
		if($("#BatchStockTakingTrue").prop("checked") == true){
			html.push('<td id="dataTd4"><span><input name="batchNoArr" id="batchNoArr" style="width:95%;" class="text-number" maxlength="20" onchange="BINOLSTIOS05.checkBatchNo(this);"></input></span></td>');
		}
		//当前库存
		if(!BINOLSTIOS05.blindFlag){
			html.push('<td id="dataTd5" style="text-align:right;"></td>');
		}
		//数量
		html.push('<td id="dataTd6"><span><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTIOS05.changeCount(this);"></input></span></td>');
		//盘差
		if(!BINOLSTIOS05.blindFlag){
			html.push('<td id="gainCount" style="text-align:right;"></td>');
		}
		//备注
		html.push('<td id="dataTd8"><span><input name="reasonArr" style="width:95%;" id="reasonArr" size="25" maxlength="200"></input></span></td>');
		//隐藏
		html.push('<td id="dataTd11" style="display:none">');
		html.push('<input type="hidden" name="bookCountArr" id="bookCountArr" value=""></input>');
		html.push('<input type="hidden" name="gainCountArr" id="gainCountArr" value=""></input>');
		html.push('<input type="hidden" name="productVendorIDArr" id="productVendorIDArr" value="'+info.proId+'"></input>');
		html.push('<input type="hidden" name="hasproductflagArr" id="hasproductflagArr" value="0"></input>');
		html.push('<input type="hidden" name="priceArr" id="priceArr" value="'+info.salePrice+'"></input>');
		html.push('</td>');
		html.push('</tr>');
		return html.join("");
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
	 * AJAX取得产品当前库存量
	 * 
	 * */
	"getPrtStock": function(obj,productVendorID){
		var $detail = $(obj);
		//ajax取得该产品的库存数量
		var param = "depotInfoId="+$("#depotInfoId").val();
		if ($("#logicDepotsInfoId").length>0 && $("#logicDepotsInfoId").val()!=""){
			param += "&logicDepotsInfoId="+$("#logicDepotsInfoId").val();
		}
		param += "&ProductVendorIDArr="+productVendorID;
		var callback = function(msg){
			var stocks = eval('('+msg+')');
			if($detail.find("#hasproductflagArr").val()=="0"){
				$detail.find("#hasproductflagArr").val("1");
				$detail.find("#dataTd5").html(stocks[productVendorID]);
				$detail.find("#bookCountArr").val(stocks[productVendorID]);
				//added by
				if($("#BatchStockTakingTrue").prop("checked") == true){
					// 跳到批次文本框
					var $batchNoArrInputText = $detail.find("#batchNoArr");
					// 获取焦点
					$batchNoArrInputText.select();
				} else {
					//跳到数量文本框
					var $quantityInputText =  $detail.find("#quantityArr");
					//选中一个产品后后默认数量1
					var quantity = $quantityInputText.val();
					if(quantity == ""){
						$quantityInputText.val("1");
					}
					//计算盘差（防止先输入数量后输入产品）
					BINOLSTIOS05.changeCount($quantityInputText);
					// 获取焦点
					$quantityInputText.select();
				}
			}
		};
		
		cherryAjaxRequest({
			url:$("#s_getStockCount").html(),
			reloadFlg:true,
			param:param,
			callback:callback
		});
	},
	
	//批量取得库存
	"getStocks":function(){
		//当非批次盘点时
		if($("#BatchStockTakingFalse").prop("checked") == true){
			var count = 0;
			//ajax取得该产品的库存数量
			var param = "depotInfoId="+$("#depotInfoId").val();
			if ($("#logicDepotsInfoId").length>0 && $("#logicDepotsInfoId").val()!=""){
				param += "&logicDepotsInfoId="+$("#logicDepotsInfoId").val();
			}
			$("#databody").find("tr").each(function(i){
				if($(this).find("#hasproductflagArr").val()=="0"){
					param += "&ProductVendorIDArr="+$(this).find("#productVendorIDArr").val();
					count ++;
				}
			});
			if(count == 0){
				return false;
			}
			var callback = function(msg){
				var stocks = eval('('+msg+')');
				$("#databody").find("tr").each(function(i){
					if($(this).find("#hasproductflagArr").val()=="0"){
						var index = $(this).find("#productVendorIDArr").val();//$(this).attr("id").split("_")[1];
						$(this).find("#hasproductflagArr").val("1");
						$(this).find("#dataTd5").html(stocks[index]);
						$(this).find("#bookCountArr").val(stocks[index]);
					}
				});
			};
			
			cherryAjaxRequest({
				url:$("#s_getStockCount").html(),
				reloadFlg:true,
				param:param,
				callback:callback
			});
		}
		
	},
	
	//记录用户输入
	"recordInput":function(){
		var inputArr = [];
		$("#databody").find("tr").each(function(){
			var obj = {
					trId:$(this).attr("id"),
					batchNoArr:$(this).find("#batchNoArr").val(),
					quantityArr:$(this).find("#quantityArr").val(),
					reasonArr:$(this).find("#reasonArr").val()
			};
			inputArr.push(obj);
		});
		return inputArr;
	},
	
	//设定用户输入
	"setInput":function(inputArr){
		$.each(inputArr,function(i){
			var tempObj = inputArr[i];
			var trId = inputArr[i].trId;
			var $tr = $("#databody").find("#"+trId);
			$tr.find("#batchNoArr").val(inputArr[i].batchNoArr);
			$tr.find("#quantityArr").val(inputArr[i].quantityArr);
			$tr.find("#reasonArr").val(inputArr[i].reasonArr);
		});
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
	 * 新增一行
	 * @return
	 */
	"addRow":function(){
		this.clearActionMsg();
		var rowNumber = Math.abs($('#rowNumber').val())+1;
		var newid = 'dataRow'+rowNumber;
		$('#dataRow0').clone().attr('id',newid).removeAttr("style").appendTo("#databody");	
		$('#'+newid).find(':checkbox').val(rowNumber);
		$('#'+newid).find('#gainCount').attr('id','gainCount'+rowNumber);
		$('#'+newid).find('#bookCountArr').attr('id','bookCountArr'+rowNumber);
		$('#'+newid).find('#gainCountArr').attr('id','gainCountArr'+rowNumber);
		$('#'+newid).find('#unitCodeArr').attr('id','unitCodeArr'+rowNumber);
		$('#'+newid).find('#barCodeArr').attr('id','barCodeArr'+rowNumber);
		$('#'+newid).find('#productVendorIDArr').attr('id','productVendorIDArr'+rowNumber);
		$('#'+newid).find('#priceArr').attr('id','priceArr'+rowNumber);
		$('#rowNumber').val(rowNumber);
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
					$(this).parent().parent().remove();
				}					
			});
		
		this.changeOddEvenColor();
		$('#allSelect').prop("checked",false);
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
			$tr_obj.find('#gainCountArr').val("");
			return;
		}	
		$(thisObj).val(quantity);
		if($tr_obj.find('#bookCountArr').val() != "" && $tr_obj.find('#bookCountArr').val() != undefined){
			var tempCount = Number(quantity)-Number($tr_obj.find('#bookCountArr').val());
			$tr_obj.find('#gainCount').html(tempCount);
			$tr_obj.find('#gainCountArr').val(tempCount);
		}
	},
	
	"clearDetailData":function(){
		$("#databody > tr").remove();
	},
	
	"chooseDepot":function(){
		this.clearDetailData();
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
			if($("#BatchStockTakingTrue").prop("checked") == true && $(this).find("td:eq(4) #batchNoArr").val()==""){
				flag = false;
				$(this).attr("class","errTRbgColor");
			}else if($("#BatchStockTakingTrue").prop("checked") == true && $(this).find("td:eq(4) #errorText").length>0){
				flag = false;
				$(this).attr("class","errTRbgColor");	
			}else if($(this).find("#quantityArr").val()==""){
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
	 * 盘点确认
	 * @returns {Boolean}
	 */
	"confirm":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return;
		}
		var param=$("#mainForm").formSerialize();
		var url=$("#urlSubmit").text();
		var callback=function(msg){
			if((msg.indexOf('errorMessage') == -1) && (msg.indexOf('hidFieldErrorMsg') == -1)){
				BINOLSTIOS05.clearDetailData();
				//在这里将checkbox去除选中
				$("#allSelect").attr("checked",false);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	/**
	 * 盘点暂存
	 * @returns {Boolean}
	 */
	"save":function(){
		this.clearActionMsg();
		if(!this.checkData()){
			return;
		}
		var param=$("#mainForm").formSerialize();
		var url=$("#urlSave").text();
		var callback=function(msg){
			if((msg.indexOf('errorMessage') == -1) && (msg.indexOf('hidFieldErrorMsg') == -1)){
				BINOLSTIOS05.clearDetailData();
				//在这里将checkbox去除选中状态
				$("#allSelect").attr("checked",false);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});

	},
	
	/**
	 * 判断批次号是否存在
	 * @param msg
	 * @return
	 */
	"checkBatchNo":function(thisObj){
		var $tr = $(thisObj).parent().parent().parent();
		var productVendorID= $tr.find("#dataTd11 [name='productVendorIDArr']").val();
		var url = $("#s_checkBatchNo").text();
		var index = $(thisObj).parent().parent().parent().attr("id");
		var param = "batchNo="+$(thisObj).val()+"&depotInfoId="+$("#depotInfoId").val()+"&productVendorId="+productVendorID;
		if ($("#logicDepotsInfoId").length>0 && $("#logicDepotsInfoId").val()!=""){
			param += "&logicDepotsInfoId="+$("#logicDepotsInfoId").val();
		}
		var callback=function(msg){
			var stock = eval('('+msg+')');
			var quantity = stock[productVendorID];
			if(quantity === ""){
				$tr.find('#dataTd5').html("");
				//清空盘差
				$tr.find('#gainCount').html("");
				$tr.find('#gainCountArr').val("");
				$tr.find("#bookCountArr").val("");
				$tr.find('#dataTd4 span input').parent().append(errSpan($('#errmsg_EST00017').val()));
				$tr.find('#dataTd4 span input').parent().addClass("error");
				
			}else{
				$tr.find("#bookCountArr").val(quantity);
				$tr.find('#dataTd5').html(quantity);
				$tr.find('#dataTd4 span').removeClass("error");
				$tr.find('#dataTd4 span').find("#errorText").remove();
				$("#cluetip").hide();
				var curQuantity = $tr.find("#quantityArr").val();
				if(curQuantity != "" && curQuantity != undefined){
					var gc = Number(quantity) - Number($tr.find("#quantityArr").val());
					$tr.find('#gainCount').html(gc);
					$tr.find('#gainCountArr').val(gc);
				}
			}
			/*
			var member = eval("("+msg+")");
			$.each(member, function(i){
				if(member[i].productBatchId != null && member[i].productBatchId != ""){
					$("#bookCountArr"+curIndex).val(member[i].quantity);
					$('#'+member[i].currentIndex +' > #dataTd5').html(member[i].quantity);
					$('#'+member[i].currentIndex +' > #dataTd4 span').removeClass("error");
					$('#'+member[i].currentIndex +' > #dataTd4 span').find("#errorText").remove();
					$("#cluetip").hide();
					var curQuantity = $('#'+member[i].currentIndex+ " #dataTd6 #quantityArr").val();
					if(curQuantity != "" && curQuantity != undefined){
						var gc = member[i].quantity -$('#'+member[i].currentIndex+ " #dataTd6 #quantityArr").val();
						$('#gainCount'+curIndex).html(gc);
						$('#gainCountArr'+curIndex ).val(gc);
					}
				}else{
					$('#'+member[i].currentIndex +' > #dataTd5').html("");
					//清空盘差
					$('#gainCount'+curIndex).html("");
					$('#gainCountArr'+curIndex ).val("");
					$("#bookCountArr"+curIndex).val("");
					$('#'+member[i].currentIndex +' > #dataTd4 span input').parent().append(errSpan($('#errmsg_EST00017').val()));
					$('#'+member[i].currentIndex +' > #dataTd4 span input').parent().addClass("error");
				}
			});
			*/
		};
		cherryAjaxRequest({
			url:url,
			param:param,
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
			$('#dataRow'+member[i].currentIndex +' > #dataTd5').html(member[i].Quantity);
			$('#bookCountArr'+member[i].currentIndex).val(member[i].Quantity);
		});
	},
	
	/**
	 * 提交成功的回调函数
	 * @param msg
	 */
	"submitSuccess":function(){
		submitflag=true;
		if($('#actionResultDiv').attr('class')=='actionSuccess'){
			$("#databody > tr").each(function(i){
				$(this).remove();			
			});
		}
	},
	
	/**
	 * 部门弹出框
	 * @return
	 */
	"openDepartBox":function(thisObj){
	 	//取得所有部门类型
	 	var departType = "";
	 	var witposStacking = $("#witposStaking").val();
	 	if(witposStacking != "1"){
	 		for(var i=0;i<$("#departTypePop option").length;i++){
		 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
		 		//排除4柜台
		 		if(departTypeValue != "4"){
		 			departType += "&departType="+departTypeValue;
		 		}
		 	}
	 	}else{
	 		for(var i=0;i<$("#departTypePop option").length;i++){
		 		departType += "&departType="+$("#departTypePop option:eq("+i+")").val();
		 	}
	 	}
	 	var param = "checkType=radio&privilegeFlg=1&businessType=1"+departType;
		var callback = function() {
			var $selected = $('#departDataTable').find(':input[checked]').parents("tr");
			if($selected.length > 0) {
				var departId = $selected.find("input@[name='organizationId']").val();
				var departCode = $selected.find("td:eq(1)").text();
				var departName = $selected.find("td:eq(2)").text();
				$("#organizationId").val(departId);
				$("#orgName").text("("+departCode+")"+departName);
				BINOLSTIOS05.chooseDepart();
				if(witposStacking == "1"){
					BINOLSTIOS05.getLogDepotByAjax(departId);
				}
			}
			
		};
		popDataTableOfDepart(thisObj,param,callback);
	},
	
	/**
	 * 根据判断部门ID取得对应的逻辑仓库
	 * 
	 * */
	"getLogDepotByAjax":function(departId){
		var url = $("#url_getLogDepotByAjax").html();
		var param = "departId="+departId;
		var callback = function(msg){
			var logDepotList = window.JSON2.parse(msg); 
			var optionHtml = "";
			for(var i=0 ; i < logDepotList.length; i++){
				optionHtml = optionHtml + "<option value="+logDepotList[i].BIN_LogicInventoryInfoID+">"+logDepotList[i].LogicInventoryCodeName+"</option>";
			}
			$("#logicDepotsInfoId").html(optionHtml);
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	/**
	 * 更改了部门
	 * @param thisObj
	 */
	"chooseDepart":function(thisObj){	
		this.clearActionMsg();
		var organizationid = $('#organizationId').val();
		var param ="organizationid="+organizationid;
		
		//更改了部门后，取得该部门所拥有的仓库
		var url = $('#urlgetdepotAjax').html()+"?csrftoken="+$('#csrftoken').val();	
		ajaxRequest(url,param,this.changeDepotDropDownList);
		//清空明细
		this.clearDetailData();
	},
	
	/**
	 * 修改仓库下拉框
	 * @param data
	 * @return
	 */
	"changeDepotDropDownList":function(data){
		//data为json格式的文本字符串	
		var member = eval("("+data+")");    //包数据解析为json 格式  
		$("#depotInfoId").find("option:not(:first)").remove();
		$.each(member, function(i){
			$("#depotInfoId ").append("<option value='"+ member[i].BIN_DepotInfoID+"'>"+escapeHTMLHandle(member[i].DepotCodeName)+"</option>"); 
		});
		if($("#depotInfoId option").length>1){
			$("#depotInfoId").get(0).selectedIndex=1;
			if($("#organizationId").val() != undefined && $("#organizationId").val() !=""){
				$("#depotInfoId").attr("disabled",false);
				$("#logicDepotsInfoId").attr("disabled",false);
				BINOLSTIOS05.clearActionMsg();
			}
		}else{
			$("#depotInfoId").attr("disabled",true);
			$("#logicDepotsInfoId").attr("disabled",true);
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
		}
	},

	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		BINOLSTIOS05.clearActionMsg();
		if($('#depotInfoId').val()==null || $('#depotInfoId').val()==""){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00006').val());
			$('#errorDiv2').show();
			return false;
		}
		// 设置全选状态
		$('#allSelect').prop("checked",false);
		var nowRows = $('#databody >tr').length;
		var maxCount = $('#maxCount').val();
		var flag = 0;
		if(maxCount == null || maxCount == "")
			flag = 0;
		else if(nowRows >= maxCount){
			flag = 1;
		}
		//已有空行不新增一行
		var addNewLineFlag = true;
		$.each($('#databody >tr'), function(i){
			var nextIndex = parseInt($("#rowNumber").val())+1;
			if($(this).find("[name='unitCodeBinding']").is(":visible")){
				addNewLineFlag = false;
				$(this).find("[name='unitCodeBinding']").focus();
				return;
			}
			if(flag) {
				addNewLineFlag = false;
				var $p = $('#send_selectinfo_1_dialog').find('p.message');
				var $message = $p.find('span');
				var $loading = $p.find('img');
				$loading.hide();
				var option = {
					autoOpen: false,
					width: 350,
					height: 250,
					title: "提示",
					zIndex: 1,
					modal: true,
					resizable: false,
					buttons: [
						{
							text: "确定",
							click: function () {
								closeCherryDialog("send_selectinfo_1_dialog");
							}
						}],
					close: function () {
						closeCherryDialog("send_selectinfo_1_dialog");
					}
				};
				$message.text("单次盘点产品数不能超过"+maxCount+"行");
				$("#send_selectinfo_1_dialog").dialog(option);
				$("#send_selectinfo_1_dialog").dialog("open");
				return;
			}
		});
		
		if(addNewLineFlag){
			var nextIndex = parseInt($("#rowNumber").val())+1;

			$("#rowNumber").val(nextIndex);
			var html = [];
			html.push('<tr id="tr_'+nextIndex+'">');
			//chcekbox
			html.push('<td class="center"><input type="hidden" name="prtVendorId" value=""></input><input id="chkbox" type="checkbox" onclick="BINOLSTIOS05.changechkbox(this);"/></td>');
			//UnitCode
			html.push('<td id="dataTd1"><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/></td>');
			//BarCode
			html.push('<td id="dataTd2"><span id="spanBarCode"></span></td>');
			//名称
			html.push('<td id="dataTd3"><span id="spanProductName"></span></td>');
			//批次号
			if($("#BatchStockTakingTrue").prop("checked") == true){
				html.push('<td id="dataTd4"><span><input name="batchNoArr" id="batchNoArr" style="width:95%;" class="text-number" maxlength="20" onchange="BINOLSTIOS05.checkBatchNo(this);"></input></span></td>');
			}
			//当前库存
			if(!BINOLSTIOS05.blindFlag){
				html.push('<td id="dataTd5" style="text-align:right;"></td>');
			}
			//数量
			html.push('<td id="dataTd6"><span><input name="quantityArr" id="quantityArr" class="text-number" size="10" maxlength="9" onchange="BINOLSTIOS05.changeCount(this);"></input></span></td>');
			//盘差
			if(!BINOLSTIOS05.blindFlag){
				html.push('<td id="gainCount" style="text-align:right;"></td>');
			}
			//备注
			html.push('<td id="dataTd8"><span><input name="reasonArr" style="width:95%;" id="reasonArr" size="25" maxlength="200"></input></span></td>');
			//隐藏
			html.push('<td id="dataTd11" style="display:none">');
			html.push('<input type="hidden" name="bookCountArr" id="bookCountArr" value=""></input>');
			html.push('<input type="hidden" name="gainCountArr" id="gainCountArr" value=""></input>');
			html.push('<input type="hidden" name="productVendorIDArr" id="productVendorIDArr" value=""></input>');
			html.push('<input type="hidden" name="hasproductflagArr" id="hasproductflagArr" value="0"></input>');
			html.push('<input type="hidden" name="priceArr" id="priceArr" value=""></input>');
			html.push('</td>');
			html.push('</tr>');
			$("#databody").append(html.join(""));
			
			var unitCode = "unitCodeBinding_"+nextIndex;
			BINOLSTIOS05.setProductBinding(unitCode);
			/*var barCode = "barCodeBinding_"+nextIndex;
			BINOLSTIOS05.setProductBinding(barCode);*/
			$("#unitCodeBinding_"+nextIndex).focus();
			BINOLSTIOS05.bindInput();
		}
	},
	
	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:BINOLSTIOS05.pbAfterSelectFun});
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		BINOLSTIOS05.clearActionMsg();
		$('#'+info.elementId).val(info.unitCode);
		
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#databody >tr'), function(i){
			if($(this).find("#productVendorIDArr").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan2").html($("#errmsg_EST00035").val());
				$("#errorDiv2").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv2").hide();
			// 下拉框共通中的销售价格为price,而弹出框中的销售价格为salePrice
			var price = info.price;//销售价格
//			var sysConfigUsePrice = $("#sysConfigUsePrice").val();
//			if(sysConfigUsePrice == "MemPrice"){
//				price = info.memPrice;//会员价格
//			} else if("StandardCost" == sysConfigUsePrice) {
//				price = info.standardCost;//结算价格
//			}
			var $dataTd = $('#'+info.elementId).parent().parent();
			//设置隐藏值
//			$dataTd.find("[name='prtVendorId']").val(info.prtVendorId);
			$dataTd.find("[name='productVendorIDArr']").val(info.prtVendorId);
			$dataTd.find("#priceArr").val(price);
			
			//设置显示值
			$dataTd.find("#spanUnitCode").html(info.unitCode);
			$dataTd.find("#spanBarCode").html(info.barCode);
			$dataTd.find("#spanProductName").html(info.productName);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//查询库存
//			if(!BINOLSTIOS05.blindFlag){
			BINOLSTIOS05.getPrtStock($dataTd,info.prtVendorId);
//			}
		}
	},
	
	
	/**
	 * 绑定输入框
	 */
	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLSTIOS05.bindInputTR(trParam);
		});
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTIOS05"){
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
						if($(this).attr("name") == "unitCodeBinding"){
							//手动输入产品关键词，按Enter键选择产品后，跳到批次或者数量文本框
							if($("#BatchStockTakingTrue").prop("checked") == true){
								// 跳到批次文本框
								var $batchNoArrInputText =  $dataTd.find("#batchNoArr");
								$batchNoArrInputText.select();
							} else {
								var $quantityInputText =  $('#'+trParam).find("#quantityArr");
								$quantityInputText.select();
							}
						}else{
							var nxtIdx = $inp.index(this) + 1;
							var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
							if($nextInputText.length>0){
								//跳到下一个文本框
								$nextInputText.focus();
							}else{
								//跳到下一行第一个文本框，如果没有下一行，新增一行
								if($(trParam).next().length==0){
									BINOLSTIOS05.addNewLine();
								}else{
									$(trParam).next().find("input:text:visible:eq(0)").focus();
								}
							}
						}
					}
				}
			}
		});
	}
};

var BINOLSTIOS05 = new BINOLSTIOS05();

$(function(){
	// 产品popup初始化
	BINOLSTIOS05.STIOS05_dialogBody = $('#productDialog').html();
	BINOLSTIOS05.blindFlag = $("#blindFlag").val()=="0"? false:true;
	BINOLSTIOS05.chooseDepart();
	$("#mainTable th:eq(4)").hide();
	$("#dataRow0 td:eq(4)").hide();
	$('[name="isBatchStockTaking"]').live('click',function(){
		if($("#BatchStockTakingTrue").prop("checked") == true){
			$("#mainTable th:eq(4)").show();
			$("#dataRow0 td:eq(4)").show();
			BINOLSTIOS05.clearDetailData();
		}else if($("#BatchStockTakingFalse").prop("checked") == true){
			$("#mainTable th:eq(4)").hide();
			$("#dataRow0 td:eq(4)").hide();
			BINOLSTIOS05.clearDetailData();
		}
	});
});

function errSpan(text) {
	// 新建显示错误信息的span
	var errSpan = document.createElement("span");
	$(errSpan).attr("class", "ui-icon icon-error tooltip-trigger-error");
	$(errSpan).attr("id", "errorText");
	$(errSpan).attr("title",'error|'+ text);
	$(errSpan).cluetip({
    	splitTitle: '|',
	    width: 150,
	    tracking: true,
	    cluetipClass: 'error', 
	    arrows: true, 
	    dropShadow: false,
	    hoverIntent: false,
	    sticky: false,
	    mouseOutClose: true,
	    closePosition: 'title',
	    closeText: '<span class="ui-icon ui-icon-close"></span>'
	});
	return errSpan;
}