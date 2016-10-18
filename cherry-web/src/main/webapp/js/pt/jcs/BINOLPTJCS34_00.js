//是否刷新父页面
var BINOLPTJCS34_00_doRefresh = false;
var PTJCS00_dialogBody = "";
//产品对话框 确定事件
function selectProduct() {
	closeCherryDialog('productDialog', PTJCS00_dialogBody);
}
// 页面关闭
window.onbeforeunload = function() {
	if (window.opener) {
		window.opener.unlockParentWindow();
		if (BINOLPTJCS34_00_doRefresh) {
			if (window.opener.BINOLPTJCS34.modeFlg == 'listMode') {
				// 刷新父页面
				window.opener.BINOLPTJCS34.search($("#search_url",
						window.opener.document).val());
			} else {
				// 初始化树
				window.opener.BINOLPTJCS34.initTree();
			}
		}
	}
};
var BINOLPTJCS34_00 = function() {
};
BINOLPTJCS34_00.prototype = {
	// 对象JSON化
	"toJSON" : function(obj) {
		var JSON = [];
		$(obj).find(':input').each(
				function() {
					$this = $(this);
					if (($this.attr("type") == "radio" && this.checked)
							|| $this.attr("type") != "radio") {
						if ($.trim($this.val()) != '') {
							var name = $this.attr("name");
							if (name.indexOf("_") != -1) {
								name = name.split("_")[0];
							}
							JSON.push('"'
									+ encodeURIComponent(name)
									+ '":"'
									+ encodeURIComponent($.trim($this.val())
											.replace(/\\/g, '\\\\').replace(
													/"/g, '\\"')) + '"');
						}
					}
				});
		return "{" + JSON.toString() + "}";
	},
	// 对象JSON数组化
	"toJSONArr" : function($obj) {
		var that = this;
		var JSONArr = [];
		$obj.each(function() {
			JSONArr.push(that.toJSON(this));
		});
		return "[" + JSONArr.toString() + "]";
	},
	// 取得参数
	"getParams" : function() {
		// 参数(品牌信息ID)
		var params = $("#brandSel").serialize();
		var parentToken = getParentToken();
		params += "&" + parentToken;
		return params;
	},
	// 添加DIV
	"addDivRow" : function(id, rowId) {
		$(id).append($(rowId).html());
	},

	// 显示/隐藏扩展属性DIV
	"showExtInfo" : function(_this, text1, text2) {
		var $extInfo = $("#extInfo");
		var $this = $(_this);
		var $button = $this.find("span.button-text");
		if ($extInfo.find(":visible").length > 0) {
			$extInfo.hide();
			$button.text(text1);
			$("#prtExtLab").hide();
		} else {
			$extInfo.show();
			$button.text(text2);
			$("#prtExtLab").show();
		}
	},
	// 标准销售价格初期化
	"initSalePrice" : function() {
		$("#priceInfo")
				.find("table")
				.each(
						function() {
							$(this).find(".date").unbind("cherryDate");
							$(this)
									.find("input[name='priceStartDate'][class='date']")
									.cherryDate(
											{
												holidayObj : $("#dateHolidays")
														.val(),
												beforeShow : function(input) {
													var value = $(input)
															.parents("table")
															.find(
																	"input[name='priceEndDate']")
															.val();
													return [ value, "maxDate" ];
												}
											});
							$(this)
									.find("input[name='priceEndDate'][class='date']")
									.cherryDate(
											{
												holidayObj : $("#dateHolidays")
														.val(),
												beforeShow : function(input) {
													var value = $(input)
															.parents("table")
															.find(
																	"input[name='priceStartDate']")
															.val();
													return [ value, "minDate" ];
												}
											});
						});
	},
	// 添加价格DIV
	"addPriceDivRow" : function() {
		// 添加价格DIV
		this.addDivRow('#priceInfo', '#priceRow');
//		var $salePrices = $("#priceInfo").find("input[name='salePrice']");
//		var size = $salePrices.length;
//		if(size >= 2){
//			var $last = $salePrices.eq(size-1);
//			var $perLast = $salePrices.eq(size-2);
//			$last.val($perLast.val());
//		}
		// 标准销售价格初期化
		this.initSalePrice();
	},
	// 初期化日期
	"initDate" : function(date1, date2, type) {
		$(date1).cherryDate( {
			holidayObj : $("#dateHolidays").val(),
			beforeShow : function(input) {
				var value = $(date2).val();
				return [ value, type ];
			}
		});
	},
	// 大小分类绑定处理
	"bind" : function(_this, url, teminalFlag) {
		// 要替换的分类行
		var $line = null;
		var propValId = $(_this).val();
		var param = getParentToken() + "&propValId=" + propValId;
		if (teminalFlag == 1) {
			$line = $("#cate_2");
			param += "&teminalFlag=2";
		} else {
			$line = $("#cate_1");
			param += "&teminalFlag=1";
		}
		if ($line.length == 0) {
			return;
		}
		// 要替换的分类ID
		var propId = $line.find("input:first").attr("name").split("_")[1];
		param += "&propId=" + propId;
		// 取得分类list
		ajaxRequest(url, param, function(msg) {
			if (teminalFlag == 2) {
				var str = $.param($(":radio:checked", msg));
				var val = str.substring(str.lastIndexOf("=") + 1);
				if (val == "") {
					return;
				}
			}
			$line.html(msg);
		});
	},
	/**
	 * 校验产品分类是否为空
	 * 此校验只针对需要下发的分类	
	 * 
	 */
	"validCateInfo" : function(){
		var result = true;
		// 定义分类为空的名称集合变更
		var propNameArr = '';
		// 终端大分类
		var $teminalTrans1 = $("#cateInfo .detail .teminalTrans1");
		if($teminalTrans1.length > 0){
			// 判断产品的值是否为空
			var $tt1 = $teminalTrans1.parent().parent().parent().find(".all_clean").find("tbody").html();
			if($.trim($tt1).length == 0){
				var propName1 = $teminalTrans1.children("span").text();
				propNameArr += propName1 + '、';
			}
		}
		// 终端中分类
		var $teminalTrans3 = $("#cateInfo .detail .teminalTrans3");
		if($teminalTrans3.length > 0){
			// 判断产品的值是否为空
			var $tt3 = $teminalTrans3.parent().parent().parent().find(".all_clean").find("tbody").html();
			if($.trim($tt3).length == 0){
				var propName3 = $teminalTrans3.children("span").text();
				propNameArr += propName3 + '、';
			}
		}
		// 终端小分类
		var $teminalTrans2 = $("#cateInfo .detail .teminalTrans2");
		if($teminalTrans2.length > 0){
			// 判断产品的值是否为空
			var $tt2 = $teminalTrans2.parent().parent().parent().find(".all_clean").find("tbody").html();
			if($.trim($tt2).length == 0){
				var propName2 = $teminalTrans2.children("span").text();
				propNameArr += propName2 + '、';
			}
		}
		if(propNameArr.length != 0){
			$("#cateInfoMessDiv > #errorMessage").empty();
			$("#cateInfoMessDiv > #errorMessage").html($("#errorMessageTemp1").html());
			var $actionErrorSpan = $("#cateInfoMessDiv > #errorMessage > .actionError span");
			$actionErrorSpan.prepend(propNameArr.substring(0, propNameArr.length-1));
			result = false;
		}
		return result;
	},
	// 产品保存
	"doSave" : function(url) {
		
		if($("#optionFlag").val() == 1){
			
		}
		
		// 方案ID，在方案中创建产品时，有值
		var productPriceSolutionIDVal = $("#productPriceSolutionID").val();
//		alert(productPriceSolutionIDVal);

		// 表单验证
		if (!$('#mainForm').valid()) {
//			this.validCateInfo();
			return false;
		} else {
//			if(!this.validCateInfo()){
//				return false;
//			}
		}
		
		// 参数序列化(基本属性)
		var param = $("#baseInfo").find(":input").not($(":input", "#barCode"))
				.serialize();
		// 一品多码的unitCode
		param += "&" + $("#ubBody").find("td:first").find(":input").serialize();
		// 扩展属性
		param += "&" + $("#extInfo").find(":input").serialize();
		// 产品条码
		param += "&barCode="
				+ this.toJSONArr($("#barCode").find(".vendorSpan"));
//		param += "&unitCode=" + $("#unitCode").val();
		param += "&productId=" + $("#productId").val();
		var mode = $("#mode").val();
		// BOM，套装
		if(mode.indexOf('BOM') != -1){
			// BOM信息
			param += "&bomInfo=" + this.toJSONArr($("#bomBody").find("tr"));
		}
		
		// 产品分类
		param += "&cateInfo=" + this.toJSONArr($("#cateInfo").find(".detail").children().children());
		// 标准价格信息
		param += "&priceInfo=" + this.toJSONArr($("#priceInfo").find("table"));
		// 销售价格范围
		param += "&" + $("#minMaxPriceInfo").find(":input").serialize();
		if ($("#barCodeInfo").length > 0) {
			// 产品条码信息
			param += "&barCodeInfo="
					+ this.toJSONArr($("#barCodeInfo").find("span"));
		}
		
		param += "&productPriceSolutionID="+ productPriceSolutionIDVal;
//		alert(param);
		cherryAjaxRequest( {
			url : url,
			param : param,
			callback : function(msg) {
//				if(productPriceSolutionIDVal){
////					alert("跳转到方案明细");
//					// 判断msg中是否含有错误，没有则隔2秒后返回方案明细画面
//					if(msg.indexOf("actionMessage") > -1){
//						window.setTimeout(
//								function(){
//									// 保存成功后，返回方案明细画面
//									var tokenVal = parentTokenVal();
//									$("#parentCsrftoken").val(tokenVal);
//									$("#soluDetailForm").submit();
//								},1000);
//					}
//					
//					// 保存成功后，返回方案明细画面
////					var tokenVal = parentTokenVal();
////					$("#parentCsrftoken").val(tokenVal);
////					$("#soluDetailForm").submit();
//				}else{
					BINOLPTJCS34_00_doRefresh = true;
//				}
			},
			coverId : "#pageButton"//,
			//isResultHandle: (productPriceSolutionIDVal ? false : true) // 方案中创建产品时，不跳转到指定页面。
		});
	},
	// 添加条码
	"addBarCode" : function(id, rowId) {
		var $id = $(id);
		var $spans = $id.find("span.vendorSpan:visible");
		if ($spans.length == 1) {
			$spans.find("span.close").show();
		}
		this.addDivRow(id, rowId);
	},
	// 添加条码(U2M一品多码)
	"addUBLines" : function(id, rowId) {
		var $id = $(id);
		// 在barCode元素下面tr的最后一行后面添加一行barcode
		$(id).parent().find("tr:last").after($(rowId).html());
		
		// 合并产品编码行
		var $barCode = $('#barCode').find("tr");
		if ($barCode.length > 1){
			$id.find("td:first").attr("rowspan",$barCode.length);
		}
		// 添加输入框默认值相关属性
		$(id).parent().find("tr:last input").each(function(){
			var txt = $(this).val();
			$(this).focus(function(){
				// 当前元素获得焦点时，如果当前值等于默认值，则清空文本框
				if(txt === $(this).val()) {
					$(this).val("");
				}
			}).blur(function(){
				// 当前元素失去焦点时，如果文本框为空，则显示默认值，字体显示灰色
				if($(this).val() == ""){
					$(this).val(txt);
					this.style.color='#D5D5D5'
				} 
			}).keydown(function(){
				// 当前元素，键盘按下时，字体显示黑色
				this.style.color='#333'
			});
		})
	},
	// 删除条码
	"delBarCode" : function(_this) {
		var title = $('#disableTitle').text();
		var text = $('#disableMessage').html();
		var dialogSetting = {
				dialogInit: "#dialogInit",
				text: text,
				width: 	500,
				height: 300,
				title: 	title,
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){BINOLPTJCS34_00.delBarCodeHandle(_this);},
				cancelEvent: function(){removeDialog("#dialogInit");}
			};
			openDialog(dialogSetting);
	},
	"delBarCodeHandle" : function(_this){
		var $td = $("#barCode");
		var $span = $(_this).parent().parent();
		var $option = $(":input[name='option']", $span);
		if ($option.length == 0) {
			$span.remove();
		} else {
			var $barCode = $span.find(":input[name='barCode']");
			var $oBarCode = $span.find(":input[name='oldBarCode']");
			$option.val(2);
			// 防止条码被篡改
			$barCode.val($oBarCode.val());
			$span.hide();
		}
		var $spans = $td.find("span.vendorSpan:visible");
		if ($spans.length == 0) {
			this.addDivRow('#barCode', '#barCodeHide');
			$spans = $td.find("span.vendorSpan:visible");
			$spans.find("span.close").hide();
		}
		removeDialog("#dialogInit");
	},
	// 产品类型改变
	"changeMode" : function(_this) {

		// bom列表初始化
		$("#bomBody").empty();
		var value = $(_this).val();
			
		// 一码多品时，产品类型不能切换为bom,促销品套餐
		var barCodeSize = $("#barCode").find(".vendorSpan").length;
		if(barCodeSize >1){
			if ((value.indexOf('BOM') != -1) || (value.indexOf('PBOM') != -1)){
				$("#mode").val($("#modeBef").val());
				return false;
			}
		}
		
		// BOM信息DIV
		var $bomDiv = $("#bomDiv");
		var $isBOM = $("#isBOM");
		// BOM,套装
		if (value.indexOf('BOM') != -1) {
			$bomDiv.show();
			$isBOM.find("option:last").prop("selected", true);
			$isBOM.prop("disabled", true);
			$isBOM.after("<input type='hidden' id='isBOMHidden' name='isBOM' value='"+$isBOM.val()+"'/>");
			// 隐藏"添加编码条码"按钮
			$("#addBarCode").hide();
			// 保留首条BarCode
			//var $barCodes = $("#barCode").find("span.vendorSpan:visible");
			//$barCodes.not(":first").remove();
			var $barCode = $('#barCode').find("tr")
			$barCode.not(":first").remove();
			$("#ubBody").find("td:first").removeAttr("rowspan"); 
//			$barCodes.find("span.close").hide();
			// this.addBOMs();
		} else {
			$isBOM.prop("disabled", false);
			$("#isBOMHidden").remove();
			$isBOM.find("option:first").prop("selected", true);
			$bomDiv.hide();
			// 显示添加条码按钮
			$("#addBarCode").removeAttr("style");
			$("#addBarCode").show();
		}
	},
	// 产品销售方式：计重类的兑换关系
	"showExchangeRel" : function(_this) {
		var value = $(_this).val();
		
		if(value == '2'){
			// 当产品销售方式为计重类时，显示兑换关系文本框
			$("#exchangeRelationID").show();	
			$("#exRelLab").show();
		}else {
			$("#exchangeRelationID").hide();
			$("#exRelLab").hide();
		}
		
	},
	// 显示更多产品分类
	"cateVisible": function(text1, text2){
		var cateVisibleVal = $("#cateVisible").val();
		if(cateVisibleVal == 0){
			// 显示更多产品分类
			$("#cateVisible").val(1);
			$("#cateInfo .detail .teminalTransOth").show();
			
			$("#cateVisibleBtn").text(text2);
		} else {
			// 隐藏更多产品分类
			$("#cateVisible").val(0);
			$("#cateInfo .detail .teminalTransOth").hide();
			
			$("#cateVisibleBtn").text(text1);
		}
	},
	// 删除BOM产品组件
	"delBOMRow" : function(_this) {
		$(_this).parents('tr').remove();
		// BOM价格计算
//		this.getBOMPrice();
	},
	// 添加BOM产品组件
	"addBOMs" : function() {
		var proPopParam = {
			checkType : "checkbox",
			modal : false,
			autoClose : [],
			brandInfoId: $("#baseInfo").find(":input[name='brandInfoId']").val(),
			csrftoken : getParentToken(),
			isBOM : '1',
			dialogBody : PTJCS00_dialogBody,
			callback:function(){
				// 选中已选BOM组件
				setCheckbox();
				// 产品pop框checkbox事件绑定
				checkboxBind();}
		};
		popDataTableOfPrtInfo(proPopParam);
	},
	// BOM产品价格计算
	"getBOMPrice" : function(){
		var sumPrice = 0;
		// BOM组件数量
		var $trs = $("#bomBody").find("tr");
		var $salePrices = $("#priceInfo").find("input[name='salePrice']");
		$trs.each(function(){
			var $this = $(this);
			var bomPrice = $this.find("input[name='bomPrice']").val();
			var bomQuantity = $this.find("input[name='bomQuantity']").val();
			bomPrice = parseFloat(bomPrice);
			bomQuantity = parseInt(bomQuantity);
			if(isNaN(bomPrice)){
				bomPrice = 0;
			}
			if(isNaN(bomQuantity)){
				bomQuantity = 0;
			}
			sumPrice += bomPrice * bomQuantity;
		});
		$salePrices.val(sumPrice.toFixed("2"));
	},
	// 设置会员价格
	"setMemPrice":function(_this){
		// 当前激活table
		var $table = $("#priceInfo").find(".activating");
		if(_this != null && _this != undefined){
			$table = $(_this).parents("table");
		}
		// 销售价格
		var $salePrice = $table.find("input[name='salePrice']");
		// 会员价格
		var $memPrice = $table.find("input[name='memPrice']");
		var salePrice = parseFloat($salePrice.val());
		// 会员折扣
		var $memRate = $("#memRate");
		var memRate = parseInt($memRate.val());
		if(isNaN(salePrice)){
			salePrice = 0;
		}
		if(isNaN(memRate)){
			memRate = 0;
		}
		var memPrice = salePrice*memRate/100;
		$memPrice.val(memPrice.toFixed("2"));
	},
	// 初始化会员折扣弹出框 
	"initRateDiv":function(_this){
		var $this = $(_this);
		// 会员折扣
		var $memRate = $("#memRate");
		var $rateDiv = $memRate.parent();
		// ============激活当前编辑table开始=================== //
		var $table =  $this.parents("table");
		var $priceInfoDiv = $("#priceInfo");
		$priceInfoDiv.find("table").removeClass("activating");
		$table.addClass("activating");
		// ============激活当前编辑table结束=================== //
		// 销售价格
		var $salePrice = $table.find("input[name='salePrice']");
		// 会员价格
		var $memPrice = $table.find("input[name='memPrice']");
		var salePrice = parseFloat($salePrice.val());
		var memPrice = parseFloat($memPrice.val());
		if(isNaN(salePrice)){salePrice = 0;}
		if(isNaN(memPrice)){memPrice = 0;}
		if(salePrice!=0){
			var memRate = parseInt(memPrice*100/salePrice);
			$memRate.val(memRate);
		}
		// 弹出会员折扣框
		$rateDiv.show();
		$memRate.focus();
		$memRate.trigger("select");
		// 设置弹出框位置
		var offset = $this.offset();
		//var ra = $rateDiv.offset();
		$rateDiv.offset({"top":offset.top-$rateDiv.height()-2,"left":offset.left});
	},
	// 关闭弹出框
	"closeDialog":function(_this){
		var $rateDiv = $(_this).parent();
		$rateDiv.hide();
		
	},
	// 删除分类
	"removeCate":function(_this){
		// 分类块
		var $obj = $(_this).parent().parent().parent();
		$obj.remove();
	},
	"openCateDialog" : function(propId,_this){
		var $this = $(_this);
		var cateInfo = "[{}]";
		var $trs = $this.parent().parent().parent().siblings();
		cateInfo = Obj2JSONArr($trs);
		var option = {
         	   targetId: "prtCate_" + propId,
         	   propId : propId,
 	           checkType : "radio",
 	           cateInfo :cateInfo,
 	           mode : 2,
 	           click:function(){},
 		       getHtmlFun:function(info){
		       			var html = '<tr><td><span class="list_normal">';
		       			html += '<span class="text" style="line-height:19px;">' + info.cateValName + '</span>';
		       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="BINOLPTJCS34_00.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
  		       			html += '<input type="hidden" name="cateValId" value="' + info.cateValId + '"/>';
  		       			html += '<input type="hidden" name="propValId" value="' + info.cateValId + '"/>';
  		       			html += '</span></td></tr>';
 				return html;
 	           }
 	        };
 		popAjaxPrtCateDialog(option);
	}
};
// 上传产品图片
function doAjaxFileUpload() {
	var options = {
		success : function(data, status) {
			uploadSuccess(data.result, data.msg);
			if ("OK" == data.result) {
				// 保存后的图片路径
				var imagePath = "<input type='hidden' name='imagePath' value='"
						+ data.imagePath + "'/>";
				$("#baseInfo").append(imagePath);
			}
		}
	};
	uploadFile(options);
}
//选中已选产品
function setCheckbox(){
	var $bomBody = $("#bomBody");
	var $prtVendorId = $("#barCode").find("input[name='prtVendorId']");
	var prtVendorId = null;
	if($prtVendorId.length == 1){
		prtVendorId = $prtVendorId.val();
	}
	var $prtSelected = $bomBody.find(':input[name=subPrtVendorId]');
	$("#dataTableBody").find(":input").each(function(){
		var $checkbox = $(this);
		var selectedObj = window.JSON2.parse($checkbox.val());
		var selectedPrtVendorId = selectedObj.productVendorId;
		// 不能把自己作为自己的BOM组件
		if(prtVendorId == selectedPrtVendorId){
			$checkbox.prop("disabled",true);
		}
		$prtSelected.each(function(){
			var $this = $(this);
			if($this.val() == selectedPrtVendorId){
				$checkbox.prop("checked",true);
				return false;
			}
		});
	});
}
// 产品pop框checkbox事件绑定
function checkboxBind() {
	$("#dataTableBody").find(":input").click(function() {
		var $thisCheckbox = $(this);
		var $bomBody = $("#bomBody");
		var $delButton = $("#delButton");
		var selectedObj = window.JSON2.parse($thisCheckbox.val());
		if (this.checked) {
			var html = '<tr><td>'
					+ selectedObj.unitCode
					+ '<input type="hidden" name="subPrtVendorId" value="'
					+ selectedObj.productVendorId
					+ '"/>'
					+ '</td><td>'
					+ selectedObj.barCode
					+ '</td><td>'
					+ selectedObj.nameTotal
					+ '</td><td><span><input name="bomPrice" class="price" value="'
					+ parseFloat(selectedObj.salePrice).toFixed("2")
					+ '"/></span></td><td><span><input name="bomQuantity" class="number" value="1"/></span></td><td class="center">'
					+ $delButton.html() + '</td></tr>';
			$bomBody.append(html);
		} else {
			$bomBody.find('tr').each(
					function() {
						if (selectedObj.productVendorId == $(this).find(
								':input[name=subPrtVendorId]').val()) {
							$(this).remove();
							return false;
						}
					});
		}
		// BOM产品价格计算
//		BINOLPTJCS34_00.getBOMPrice();
	});
}

var BINOLPTJCS34_00 = new BINOLPTJCS34_00();
// 页面初期化方法
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	// 事件委托
//	$("#bomBody").find(":input").live("change",function(){
//		// BOM产品价格计算
//		BINOLPTJCS34_00.getBOMPrice();
//		return false;
//	});
	// 产品对话框初始化
		PTJCS00_dialogBody = $('#productDialog').html();
		// 标准销售价格初期化
		BINOLPTJCS34_00.initSalePrice();
		// price
//		$("#mainForm").find(".price").live("focusout",function() {
//			$this = $(this);
//			var value = $this.val();
//			value = parseFloat(value);
//			if(isNaN(value)){
//				value = 0;
//			}
//			$this.val(value.toFixed("2"));
//		});
		cherryValidate( {
			formId : "mainForm",
			rules : {
				unitCode : {
					required : true,
					//unitCodeValid : true,
					maxlength : 20
				}, // 厂商编码
				barCode : {
					required : true,
					//unitCodeValid : true
				}, // 产品条码
				nameTotal : {
					required : true//,
					//byteLengthValid: [40]//字节数最大为40（一个中文两个字节）
				}, // 中文名
				originalBrand : {
					required : true//,
					//byteLengthValid: [40]//字节数最大为40（一个中文两个字节）
				}, // 子品牌
				nameAlias : {
					maxlength : 50
				}, // 别名
				nameShort : {
					maxlength : 20
				}, // 中文简称
				saleUnit : {
					maxlength : 3
				}, // 销售单位
//				nameForeign : {
//					byteLengthValid: [100]//字节数最大为40（一个中文两个字节）
//				}, // 英文名
				standardCost : {
					floatValid : [ 12, 2 ]
				}, // 标准成本
				exchangeRelation : {
					floatValid : [ 6, 2 ]
				}, // 兑换关系（销售方式为计重类）
				nameShortForeign : {
					maxlength : 20
				}, // 英文简称
				sellStartDate : {
					dateValid : true
				}, // 开始销售日期
				sellEndDate : {
					dateValid : true
				}, // 停止销售日期
				shelfLife : {
					number : true,
					maxlength : 9
				}, // 保质期
				recNumDay : {
					number : true,
					maxlength : 9
				}, // 建议使用天数
				//salePrice : {
				//	floatValid : [ 14, 2 ]
				//}, // 销售价格
				//memPrice : {
				//	floatValid : [ 14, 2 ]
				//}, // 会员价格
				priceStartDate : {
					required : true,
					dateValid : true
				}, // 价格生效日
				priceEndDate : {
					dateValid : true
				},
				spec : {
					maxlength : 15
				},
				minSalePrice :{
					number : true,
					min: 0,
					floatValid : [ 14, 2 ]
				},
				maxSalePrice :{
					number : true,
					min: 0,
					floatValid : [ 14, 2 ]
				}
			}
		});
	});
