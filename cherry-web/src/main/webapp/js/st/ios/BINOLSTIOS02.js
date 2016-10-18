/**
 * @author zgl
 *
 */
function BINOLSTIOS02(){};

BINOLSTIOS02.prototype={
		
		"readyPopDepart":function(obj){
		var that = this;
//		var departType = [1,2,3,5,0,6,7];
		//取得所有部门类型
	 	var departType = "";
	 	var CounterShiftFlag = $("#CounterShiftFlag").val();
	 	for(var i=0;i<$("#departTypePop option").length;i++){
	 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
			if(CounterShiftFlag == "false" && departTypeValue == "4"){
				//不支持终端移库时排除4柜台
				continue;
			}else{
				departType += "&departType="+departTypeValue;
			}
	 	}
	 	
	 	var param = "privilegeFlg=1&businessType=1"+departType;
		var callback = function() {
			var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
    		var departId = "";
    		var departName = "";
    		var departCode = "";
    		if(checkedRadio){
    			departId = checkedRadio.val();
	    		departCode = checkedRadio.parent().parent().children("td").eq(1).find("span").text().escapeHTML();
	    		departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
    		}
			if(departId != undefined && null != departId){
				$("#departInfoId").val(departId);
				$("#departName").html("("+departCode+")"+departName);
				that.getDepotInfo(departCode,obj);
				
				$("#actionResultDisplay").empty();
				$("#errorSpan").html("");
				$("#errorDiv").hide();
			}else{
				$("#errorSpan_pop").html($("#noDepartWarnig").html());
				$("#errorDiv_pop").show();
			}
		};
		var option={
    			checkType:"radio",
    			brandInfoId:$("#brandInfoId").val(),
    			param:param,
    			click:callback
    	};
    	popAjaxDepDialog(option);
	},
	
	//取得对应的仓库
	"getDepotInfo":function(val,obj){
		var url = $("#getDapotInfo").html();
		$("#getDepotInfo_param").val(val);
		var param = "departCode="+$("#getDepotInfo_param").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#errorSpan").html($("#noDepotWarning").html());
				$("#errorDiv").show();
				$("#depotInfoId").html("");
				$("#shfitDate").html("");
				$("#fromLogicInventoryInfoId").attr("disabled",true);
				$("#toLogicInventoryInfoId").attr("disabled",true);
				$("#depotInfoId").attr("disabled",true);
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var depotInfoId = obj[index].BIN_DepotInfoID;
					var depotNameCn = obj[index].DepotCodeName;
					html=html+'<option value="'+depotInfoId+'">'+depotNameCn+'</option>';
				}
				$("#depotInfoId").html(html);
				$("#fromLogicInventoryInfoId").attr("disabled",false);
				$("#toLogicInventoryInfoId").attr("disabled",false);
				$("#depotInfoId").attr("disabled",false);
				$("#shfitDate").html("");
			}
			binOLSTIOS02.getLogicDepotInfo();
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
			});
	},
	
	//取得对应的逻辑仓库
	"getLogicDepotInfo":function(){
		var url = $("#getLogicInfo").html();
		var param = "departId="+$("#departInfoId").val();
		var callback = function(msg){
			if(msg == '[]'){
				$("#fromLogicInventoryInfoId").html("");
				$("#toLogicInventoryInfoId").html("");
			}else{
				var obj = eval('(' + msg + ')');
				var html = "";
				for(var index in obj){
					var logicInventoryInfoID = obj[index].BIN_LogicInventoryInfoID;
					var logicInventoryCodeName = obj[index].LogicInventoryCodeName;
					html=html+'<option value="'+logicInventoryInfoID+'">'+logicInventoryCodeName+'</option>';
				}
				$("#fromLogicInventoryInfoId").html(html);
				$("#toLogicInventoryInfoId").html(html);
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	"openProPopup":function(_this){
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		}else if($("#depotInfoId").val() == null || $("#depotInfoId").val() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}else if(!$("#fromLogicInventoryInfoId").val()){
			$("#errorSpan").html($("#noLogiDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}
		var that = this;
		var option = {
         	   	targetId: "shfitDate",
 	           	checkType : "checkbox",
 	           	mode : 2,
 	         //  bindFlag : true,
 	           	brandInfoId : $("#brandInfoId").val(),
 		       	getHtmlFun:function(info){
					return that.getHtmlFun(info);
			   	},
			   	click:function (){
			   			// 设置全选状态
		        	   var $checkboxs = $('#shfitDate').find(':checkbox');
		        	   var $unChecked = $('#shfitDate').find(':checkbox:not(":checked")');
		        	   if($unChecked.length == 0 && $checkboxs.length > 0){
		        		   $('#checkAll').prop("checked",true);
		        	   }else{
		        		   $('#checkAll').prop("checked",false);
		        	   }
		        	that.changeOddEvenColor();
			   		that.getStock();
			   		binOLSTIOS02.bindInput();
					//重新绑定下拉框
	    			$("#shfitDate tr [name='unitCodeBinding']").each(function(){
	    				var unitCode = $(this).attr("id");
	    				binOLSTIOS02.setProductBinding(unitCode);
	    			});
			   	}
 	        };
 		popAjaxPrtDialog(option);
	},
	
	"getHtmlFun" : function (info){
		var num = parseInt($("#rowNumber").val())+1;
		$("#rowNumber").val(num);
		
		var html = '<tr id="dataRow_'+num+'">';
		html += '<td style="text-align:center" id="dataTd0">';
		html += '<input type="checkbox"  class="checkbox" id="checkbox_'+num+'" onclick="binOLSTIOS02.checkSelect(this)" name="validFlag"/>';
		html += '<input id="priceArr"  name="priceArr" value="'+info.salePrice+'" type="hidden"/>';
		html += '<input id="prtVendorId"  name="prtVendorId" value="'+info.productVendorId+'" type="hidden"/>';
		html += '<input id="productVendorIdArr"  name="productVendorIdArr" value="'+info.productVendorId+'" type="hidden"/></td>';
		html += '<td id="dataTd2">'+info.unitCode+'</td>';
		html += '<td id="dataTd3">'+info.barCode+'</td>';
		html += '<td id="dataTd4">'+info.nameTotal+'</td>';
		html += '<td style="text-align:right" id="dataTd5">'+0+'</td>';
		html += '<td  id="dataTd6" style="width:100px">';
		html += '<input type="text" value="" maxlength="8" style="text-align:right;width:95%" class="text1" name="quantityArr" id="quantityArr" onchange="binOLSTIOS02.isInteger(this);"/></td>';
		html += '<td style="text-align:left" id="dataTd7"><input type="text" value="" style="width:95%" class="text1" name="commentsArr" id="commentsArr"/></td></tr>';
    	return html;
	},
	
	/**
	 * 添加新行
	 */
	"addNewLine":function(){
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		}else if($("#depotInfoId").val() == null || $("#depotInfoId").val() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}else if(!$("#fromLogicInventoryInfoId").val()){
			$("#errorSpan").html($("#noLogiDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}
		// 设置全选状态
		$('#allSelect').prop("checked",false);
				
		//已有空行不新增一行
		var addNewLineFlag = true;
		$.each($('#shfitDate >tr'), function(i){
			if($(this).find("[name='unitCodeBinding']").is(":visible")){
				addNewLineFlag = false;
				$(this).find("[name='unitCodeBinding']").focus();
				return;
			}
		});
		
		if(addNewLineFlag){
			var nextIndex = parseInt($("#rowNumber").val())+1;
			$("#rowNumber").val(nextIndex);
			
			var html = '<tr id="dataRow_'+nextIndex+'">';
			html += '<td style="text-align:center" id="dataTd0">';
			html += '<input type="checkbox"  class="checkbox" id="checkbox_'+nextIndex+'" onclick="binOLSTIOS02.checkSelect(this)" name="validFlag"/>';
			html += '<input id="priceArr" name="priceArr" value="" type="hidden"/>';
			html += '<input id="prtVendorId" name="prtVendorId" value="" type="hidden"/>';
			html += '<input id="productVendorIdArr" name="productVendorIdArr" value="" type="hidden"/></td>';
			//UnitCode
			html += '<td id="dataTd2"><span id="spanUnitCode"></span><input type="text" id="unitCodeBinding_'+nextIndex+'" name="unitCodeBinding"  value=""/></td>';
			//BarCode
			html += '<td id="dataTd3"><span id="spanBarCode"></span></td>';
			//名称
			html += ('<td id="dataTd4"><span id="spanProductName"></span></td>');
			
			html += '<td style="text-align:right" id="dataTd5">'+0+'</td>';
			html += '<td  id="dataTd6" style="width:100px">';
			html += '<input type="text" value="" maxlength="8" style="text-align:right;width:95%" class="text1" name="quantityArr" id="quantityArr" onchange="binOLSTIOS02.isInteger(this);"/></td>';
			html += '<td style="text-align:left" id="dataTd7"><input type="text" value="" style="width:95%" class="text1" name="commentsArr" id="commentsArr"/></td></tr>';
			
			$("#shfitDate").append(html);
			
			var unitCode = "unitCodeBinding_"+nextIndex;
			binOLSTIOS02.setProductBinding(unitCode);
			
			$("#unitCodeBinding_"+nextIndex).focus();
			
			binOLSTIOS02.bindInput();
		}
	},
	
	"setProductBinding":function(id){
		productBinding({elementId:id,showNum:20,targetDetail:true,afterSelectFun:binOLSTIOS02.pbAfterSelectFun});
	},
	
	/**
	 * 产品下拉输入框选中后执行方法
	 */
	"pbAfterSelectFun":function(info){
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		$('#'+info.elementId).val(info.unitCode);
		//验证产品厂商ID是否重复
		var flag = true;
		$.each($('#shfitDate >tr'), function(i){
			if($(this).find("#productVendorIdArr").val() == info.prtVendorId){
				flag = false;
				$("#errorSpan").html($("#errmsg_EST00035").val());
				$("#errorDiv").show();
				return;
			}
		});
		if(flag){
			$("#errorDiv").hide();
			// 下拉框共通中的销售价格为price,而弹出框中的销售价格为salePrice
			var price = info.price;//销售价格
			if(isEmpty(price)){
				price = parseFloat("0.0");
			}else{
				price = parseFloat(price);
			}
			price = price.toFixed(2);
			var $dataTd = $('#'+info.elementId).parent().parent();
			//设置隐藏值
			$dataTd.find("[name='prtVendorId']").val(info.prtVendorId);
			$dataTd.find("[name='productVendorIdArr']").val(info.prtVendorId);
			$dataTd.find("#priceArr").val(price);
			
			//设置显示值
			$dataTd.find("#spanUnitCode").html(info.unitCode);
			$dataTd.find("#spanBarCode").html(info.barCode);
			$dataTd.find("#spanProductName").html(info.productName);
			
			//取消该文本框的自动完成并隐藏。
			$('#'+info.elementId).unautocomplete();
			$('#'+info.elementId).hide();
			
			//查询库存
			binOLSTIOS02.getStock($dataTd);
			
			//跳到数量文本框
			var $quantityInputText =  $dataTd.find("#quantityArr");
			//选中一个产品后后默认数量1
			var quantity = $quantityInputText.val();
			if(quantity == ""){
				$quantityInputText.val("1");
			}
			//计算盘差（防止先输入数量后输入产品）
//			binOLSTIOS02.changeCount($quantityInputText);
			// 获取焦点
			$quantityInputText.select();
		}
	},
	
	/**
	 * 绑定输入框
	 */
	"bindInput":function(){
		var tableName = "dataTable";
		$("#"+tableName+" #shfitDate tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			binOLSTIOS02.bindInputTR(trParam);
		});
	},
	
	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTIOS02"){
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
							//手动输入产品关键词，按Enter键选择产品后，跳到数量文本框
							var $quantityInputText =  $('#'+trParam).find("#quantityArr");
							$quantityInputText.select();
						}else{
							var nxtIdx = $inp.index(this) + 1;
							var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
							if($nextInputText.length>0){
								//跳到下一个文本框
								$nextInputText.focus();
							}else{
								//跳到下一行第一个文本框，如果没有下一行，新增一行
								if($(trParam).next().length==0){
									binOLSTIOS02.addNewLine();
								}else{
									$(trParam).next().find("input:text:visible:eq(0)").focus();
								}
							}
						}
					}
				}
			}
		});
	},
	
	/**
	 * AJAX取得产品当前库存量
	 * @param obj:当前产品对象，无此参数则查询页面上的所有产品的库存
	 * 
	 * */
	"getStock":function(obj){
		var lenTR = $("#shfitDate").find("tr");
		var url = $("#getPrtVenIdAndStock").html();
		var param = $("#mainForm").serialize();
		if(obj) {
			param += "&" + $(obj).find("#productVendorIdArr").serialize();
		} else {
			param += "&" + $("#shfitDate").find("#productVendorIdArr").serialize();
		}
		if(lenTR.length>0){
			var callback = function(msg){
				var json = eval('('+msg+')');
				for (var one in json){
					var prtVendorId = json[one].prtVendorId;
					var stock = json[one].stock;
					$("#shfitDate").find("tr").each(function(){
						var $tr = $(this);
						if($tr.find("@[name='prtVendorId']").val()==prtVendorId){
							$tr.find("td:eq(4)").text(stock);
						}
					});
				}
			};
			cherryAjaxRequest({
				url:url,
				param:param,
				callback:callback,
				reloadFlg:true
			});
		}
		
		
	},
	/**
	 * 保存
	 */
	"btnSaveClick":function(){
		var that = this;
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		}else  if($("#depotInfoId").val() == null || $("#depotInfoId").val() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}else if($("#shfitDate").html().length == 0){
			$("#errorSpan").html($("#noProductWarning").html());
			$("#errorDiv").show();
			return false;
		}else if(!$("#fromLogicInventoryInfoId option:selected").val() || !$("#toLogicInventoryInfoId option:selected").val()){
			$("#errorSpan").html($("#noLogiDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}
		if(!this.checkData()){
			$("#errorSpan").html($("#errmsg_EST00008").html());
			$("#errorDiv").show();
			return false;
		}
		var url = $("#save").html();
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				that.clearPage();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
			});
	},
	/**
	 * 暂存
	 */
	"saveTemp":function(){
		var that = this;
		$("#actionResultDisplay").empty();
		$("#errorSpan").html("");
		$("#errorDiv").hide();
		if($("#departName").html() == ""){
			$("#errorSpan").html($("#noDepartWarnig").html());
			$("#errorDiv").show();
			return false;
		}else  if($("#depotInfoId").val() == null || $("#depotInfoId").val() == ""){
			$("#errorSpan").html($("#noDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}else if($("#shfitDate").html().length == 0){
			$("#errorSpan").html($("#noProductWarning").html());
			$("#errorDiv").show();
			return false;
		}else if(!$("#fromLogicInventoryInfoId option:selected").val() || !$("#toLogicInventoryInfoId option:selected").val()){
			$("#errorSpan").html($("#noLogiDepotWarning").html());
			$("#errorDiv").show();
			return false;
		}
		if(!this.checkData()){
			$("#errorSpan").html($("#errmsg_EST00008").html());
			$("#errorDiv").show();
			return false;
		}
		
		var url = $("#saveTemp").html();
		var param = $("#mainForm").serialize();
		var callback = function(msg){
			if(msg.indexOf("actionMessage") > -1){
				that.clearPage();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
			});
	},
	
	"clearPage":function(){
		$("#shfitDate").html("");
		$("#comments").val("");
	},
	
	"checkSelectAll" : function(obj) {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		$("#shfitDate").find("input[name=validFlag]").prop("checked",
				$this.prop("checked"));
	},

	"checkSelect" : function(obj) {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var $this = $(obj);
		if ($this.prop("checked")) {
			var num = $("#shfitDate").find("input[name=validFlag]").length;
			if ($("#shfitDate").find("input[name=validFlag]:checked").length == num) {
				$("input[name=allSelect]").prop("checked", true);
			}
		} else {
			$("input[name=allSelect]").prop("checked", false);
		}
	},
	
	"deleteLine":function(){
		var checked = $("#shfitDate").find("input[name=validFlag]:checked");
		if(checked.length == 0){
			return false;
		}else{
			checked.each(function(){
				var id = $(this).attr("id");
				var idArr = id.split("_");
				var index = idArr[1];
				var trId = "dataRow_"+index;
				$("#"+trId).remove();
			});
			$("input[name=allSelect]").prop("checked", false);
			$("#shfitDate tr:odd").attr("class","even");
			$("#shfitDate tr:even").attr("class","odd");
		}
	},
	
	"isInteger":function(obj){
		var $this = $(obj);
		var patrn=/^0$|^[1-9]{1}[0-9]{0,7}$/; 
		if (!patrn.exec($this.val())){
			$this.val("");
		}
	},
	
	"changeOddEvenColor":function(){
		$("#shfitDate tr:odd").attr("class","even");
		$("#shfitDate tr:even").attr("class","odd");
	},
	
	"changeOddEvenColor1":function(){
		$("#shfitDate tr:odd").attr("class",function(){
			if($(this).attr("class")=="errTRbgColor"){
				
			}else{
				$(this).addClass("odd");
			}
			
		});
		$("#shfitDate tr:even").attr("class",function(){
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
		
		//检查有无空行
		$.each($('#shfitDate >tr'), function(){
				if($(this).find("#productVendorIdArr").val()=="" || $(this).find("#quantityArr").val()=="" || $(this).find("#quantityArr").val()=="0"){
					flag = false;
					$(this).attr("class","errTRbgColor");	
				}else{
					$(this).removeClass('errTRbgColor');				
				}	
		});	
		return flag;
	}
	
};
var binOLSTIOS02 = new BINOLSTIOS02();