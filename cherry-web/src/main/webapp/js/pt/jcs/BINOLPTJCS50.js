
function BINOLPTJCS50(){
	this.needUnlock = true;
};

BINOLPTJCS50.prototype={
	// 对象JSON化
	"toJSON" : function(obj) {
		var JSON = [];
		var propValArr = [];
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
							
							if(name == 'propValId'){
								propValArr.push('"'+encodeURIComponent($.trim($this.val())
										.replace(/\\/g, '\\\\').replace(
												/"/g, '\\"')) + '"');
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
		JSON.push('"propValArr":[' + propValArr.toString()+']');
		return "{" + JSON.toString() + "}";
	},
	"doClose" : function(){
		window.close();
	},
	"edit" : function(url){
		BINOLPTJCS50.cleanMsgDIV();
		var tokenVal = parentTokenVal();
		$("#parentCsrftoken").val(tokenVal);
		this.needUnlock = false;
		$("#toEditForm").submit();
	},
	"search" : function() {
//			if(!$("#mainForm").valid()){
//				return;
//			}
//			$("#mainForm").find(":input").each(function(){
//				$(this).val($.trim(this.value));
//			});
		var url = $("#search_Url").attr("href");
		var params= "productPriceSolutionID="+ $("#productPriceSolutionID").val();
		params += "&"+$("#searchForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		url = url + "&csrftoken="+parentTokenVal();
		// 显示结果一览
		$("#productInDepotExcelInfo").removeClass("hide");
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 2, "desc" ]],
				 // 表格列属性设置
				 aoColumns : [  
				                { "sName": "checkbox", "sWidth": "2%","bSortable": false}, 			// 1
				                { "sName": "number", "sWidth": "5%", "bSortable": false},
				                { "sName": "unitCode", "sWidth": "20%" },
				                { "sName": "nameTotal", "sWidth": "20%" },
				                { "sName": "salePrice", "sWidth": "5%","sClass":"alignRight" , "bSortable": false},
				                { "sName": "memPrice", "sWidth": "5%","sClass":"alignRight" , "bSortable": false}
				                //,{ "sName": "操作", "sWidth": "5%","sClass":"center", "bSortable": false }
				                ],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				aiExclude :[0,1],
				fixedColumns : 2,
				fnDrawCallback : function() {
					
						/*
					  $("#productInDepotExcelInfoDataTable tbody tr td .editSaleClass").click(function () {
						  var $this = $(this);
						  alert($(this).html());
						  var dd = $(this).text();
						  alert(dd);
						  $(this).parent().append($("#salePriceEditForm").html());
						  $("input[name='salePrice']").focus();
						  
						  $("input[name='salePrice']").keyup(function(event){

							  switch(event.keyCode) {
							  case 27:
							  alert("ESC");
							  $(this).unbind();
							  alert($(this).parent().html());
							  $("#salePrice").remove();
							  break;
							  case 13:
							  alert("Enter");
							  $(this).unbind();
							  alert($(this).parent().html());
							  alert($(this).parent().prev().html());
							  $(this).parent().prev().text($(this).val());
							  $("#salePrice").remove();
							  break;
							  }
						  })
						  
					 });
					  $("#productInDepotExcelInfoDataTable tbody tr td .editMemClass").click(function () {
						  alert($(this).html());
						  var dd = $(this).text();
						  alert(dd);
						  $(this).parent().append($("#saleMemEditForm").html());
						  
						  $("input[name='memPrice']").keyup(function(event){

							  switch(event.keyCode) {
							  case 27:
							  alert("ESC");
							  break;
							  case 13:
							  alert("Enter");
							  break;
							  }
						  })
					  });
					  */
					
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    /** ------------以下为添加产品明细维护------------------------------------------           */
	"openChkPrtDialogInit" : function(){
		BINOLPTJCS50.cleanMsgDIV();
		if(BINOLPTJCS50.disabOptBtn()==false)return;
		var _this = this;
		var $addPrtTable = $("#addPrtTable").clone();
		$addPrtTable.find("#databody").attr('id','newDatabody');
//		$addPrtTable.find("#allSelect").attr('id','newAllSelect');
		
		var text = '<div id="prtSaveDiv">'  + $addPrtTable.html() + '</div>';
		var title = "";
//				text = '<p class="message"><span>'+$('#confirIsEnable').text();
//				text = '<p class="message"><span>'+'请确认现在下发柜台产品数据吗？';
//				title = $('#enableValTitle').text();
		title = '添加方案产品明细';
		var dialogSetting = {
				dialogInit: "#dialogChkPrtInitDIV",
				text: text,
				width: 	1000,
				height: 500,
				title: 	title,
//					confirm: $("#dialogConfirmIss").text(),
				confirm: '添加',
//					cancel: $("#dialogCancelIss").text(),
				cancel: '取消',
				confirmEvent: function(){
					
					BINOLPTJCS50.addRow();
					//_this.issuedPrt();
					removeDialog("#dialogChkPrtInitDIV");
				},
				cancelEvent: function(){removeDialog("#dialogChkPrtInitDIV");}
		};
		openDialog(dialogSetting);
	},
	/**
	 * 产品弹出框
	 * 
	 * */
	"openProPopup":function(){
		var that = this;
		// 删除掉画面头部的提示信息框 
//		BINOLSTSFH01.clearActionMsg();
		// 产品弹出框属性设置
		var option = {
			targetId: "newDatabody",//目标区ID
			checkType : "checkbox",// 选择框类型
			mode : 2, // 模式
			ignoreSoluId : $("#productPriceSolutionID").val(),  // 剔除方案中的产品
			brandInfoId : $("#brandInfoId").val(),// 品牌ID
			getHtmlFun:that.getHtmlFun,// 目标区追加数据行function
			click:function(){//点击确定按钮之后的处理
				var checkbox= $('#dataTableBody').find(':input').is(":checked");
				//改变奇偶行的样式
				BINOLPTJCS50.changeOddEvenColor();
				// 拖动排序
				$("#prtSaveDiv #mainTable #databody").sortable({
					update: function(event,ui){BINOLPTJCS50.changeOddEvenColor();}
				});
				// 设置全选状态
				var $checkboxs = $('#prtSaveDiv #databody').find(':checkbox');
				var $unChecked = $('#prtSaveDiv #databody').find(':checkbox:not(":checked")');
				if($unChecked.length == 0 && $checkboxs.length > 0){
					$('#prtSaveDiv #allSelect').prop("checked",true);
				}else{
					$('#prtSaveDiv #allSelect').prop("checked",false);
				}
				// AJAX取得产品当前库存量
//				that.getPrtStock();
				//计算总金额总数量
//				BINOLSTSFH01.calcTotal();
			}
		};
		// 调用产品弹出框共通
		popAjaxPrtDialog(option);
	},
	"changeOddEvenColor":function(){
		$("#newDatabody tr:odd").attr("class","even");
		$("#newDatabody tr:even").attr("class","odd");
	},
	"changechkbox":function(obj){
		var chked = $(obj).prop("checked");
		if(!chked){
			$('#prtSaveDiv #allSelect').prop("checked",false);
			return false;
		}
		var flag = true;
		$("#newDatabody :checkbox").each(function(i){
			if(i>=0){
				if($(this).prop("checked")!=true){
					flag=false;
				}
			}
		});
		if(flag){
			$('#prtSaveDiv #allSelect').prop("checked",true);
		}
	},
	"getHtmlFun":function(info,negativeFlag){
//		alert(JSON.stringify(info));
		var prtId = info.prtId;//产品Id
		var productVendorId = info.productVendorId;//产品厂商Id
		var unitCode = info.unitCode;//厂商编码
		var barCode = info.barCode;//产品条码
		var nameTotal = info.nameTotal;//产品名称
		var price = info.salePrice;//销售价格
		var memPrice = info.memPrice;//会员价格
		// 销售价格处理
		if(isEmpty(price)){
			price = parseFloat("0.0");
		}else{
			price = parseFloat(price);
		}
		price = price.toFixed(2);
		
		// 会员价格处理
		if(isEmpty(memPrice)){
			memPrice = parseFloat("0.0");
		}else{
			memPrice = parseFloat(memPrice);
		}
		memPrice = memPrice.toFixed(2);
		
		
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
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLPTJCS50.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		//html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td>' + nameTotal + '</td>';
		html += '<td>' + price + '</td>';
		html += '<td>' + memPrice + '</td>';
		/*
		html += '<td class="center"><input value="' + price +'" name="salePriceArr"  type="text" id="salePriceArr" class="price" maxlength="17" cssStyle="width:98%" onchange="BINOLPTJCS50.setPrice(this);" /></td>';
		html += '<td class="center"><input value="' + memPrice +'" name="memPriceArr"  type="text" id="memPriceArr" maxlength="17"  cssStyle="width:98%" onchange="BINOLPTJCS50.setPrice(this)" /></td>';
		*/
		html +='<td style="display:none">'
			+'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" name="salePriceArr" value="'+ price + '"/>'
			+'<input type="hidden" name="memPriceArr" value="'+ memPrice + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="prtIdArr" name="prtIdArr" value="'+ prtId +'"/></td></tr>';
		return html;
	},
	/**
	 * 删除选中行
	 */
	"deleterow":function(){
		BINOLPTJCS50.cleanMsgDIV();
//		BINOLSTSFH01.clearActionMsg();
		$("#newDatabody :checkbox").each(function(){
			var $this = $(this);
			if($this.prop("checked")){
				$this.parent().parent().remove();
			}
		});
		$('#prtSaveDiv #allSelect').prop("checked",false);
		BINOLSTSFH01.changeOddEvenColor();
		$('#prtSaveDiv #allSelect').prop("checked",false);
		
		$("#prtSaveDiv input[type=checkbox]").prop("checked",false);
//		BINOLSTSFH01.calcTotal();
	},
	"chkPrice":function(obj){
		var value = $(obj).val();
		var param = [14,2];
		
		var checkFloat = false;
		checkFloat = /^([0-9]\d*)(\.\d+)?$/.test(value);
		if(checkFloat) {
			var ar = value.split('.');
			if(ar.length == 2) {
				if(ar[0].length > param[0] || ar[1].length > param[1]) {
					checkFloat = false;
				}
			} else if(ar.length == 1) {
				if(ar[0].length > param[0]) {
					checkFloat = false;
				}
			} else {
				checkFloat = false;
			}
		}
//		alert(checkFloat);
		if(!checkFloat){
			$(obj).val(0);
		}
	},
	 // 格式化价格及重新计算金额
	"setPrice":function(obj){
		var $this = $(obj);
//		var $tr_obj = $this.parent().parent();

		// 折扣价
//		var $discountPrice = $tr_obj.find("input[name='priceUnitArr']");
		var discountPrice = parseFloat($this.val());

		if(isNaN(discountPrice)){
			discountPrice = 0;
		}
		$this.val(discountPrice.toFixed("2"));
	},
	"addRow":function(){
//		if(!BINOLSTSFH01.checkData()){
//			return false;
//		}
		
		var url = $("#addRow_Url").attr("href");
		var param = $("#prtSaveDiv #mainForm").serialize();
//		alert(param);
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
		param += "&csrftoken="+parentTokenVal();
		var callback = function(msg){
			BINOLPTJCS50.search();
//			if(msg.indexOf("actionMessage") > -1){
//				alert('添加成功');
////				BINOLSTSFH01.clearPage(true);
//			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	// 编辑
	"editRowInit" : function (obj){
		BINOLPTJCS50.cleanMsgDIV();
//		var that = this;
//		that.empty();
		var $this = $(obj);
		
		if(BINOLPTJCS50.disabOptBtn()==false)return;
//		alert($this.parent().parent().parent().html());
		$this.parent().parent().parent().find("[name='salePrice']").parent().prev().hide();
		$this.parent().parent().parent().find("[name='salePrice']").parent().show();
		
		$this.parent().parent().parent().find("[name='memPrice']").parent().prev().hide();
		$this.parent().parent().parent().find("[name='memPrice']").parent().show();
		
		$this.parent().parent().parent().find(".btnEdit").hide();
		$this.parent().parent().parent().find(".btnSave").show();
		//更新编辑区分 --编辑中
		$("#flag").val("1");
	},
	// 更新
	"editRow" : function (obj){
		var $this = $(obj);
		
		var url = $("#editRow_Url").attr("href");
		var param = $this.parent().parent().parent().find(":input").serialize();
		
		param += "&" + $("#brandInfoId").serialize();
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
		param += "&csrftoken="+parentTokenVal();
		
//		alert(param);
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				var map = eval("(" + msg + ")");
				var $curRow = $this.parent().parent().parent();
				$curRow.find("[name='salePrice']").parent().prev().show();
				$curRow.find("[name='salePrice']").parent().hide();
				
				$curRow.find("[name='memPrice']").parent().prev().show();
				$curRow.find("[name='memPrice']").parent().hide();
				
				$curRow.find(".btnEdit").show();
				$curRow.find(".btnSave").hide();
				if(map.result == 0){
					$curRow.find("[name='salePrice']").parent().prev().text($curRow.find("[name='salePrice']").val());
					$curRow.find("[name='memPrice']").parent().prev().text($curRow.find("[name='memPrice']").val());
				}else{
					//修改异常
				}
				
				$("#flag").val('');
				// 返回无错误
//				if(msg.indexOf('id="actionResultDiv"') > -1
//						&& msg.indexOf('class="errorMessage"') == -1){
//					that.search(true);
//				}
				
			}
		});
		
	},
	/**
	 * 设置按钮有效与无效
	 * 
	 */
	"disabOptBtn":function (){
		var flag = $("#flag").val();
		if(flag == "1"){
			return false;
		}
		return true;
	},
	/**
	 * 批量删除
	 */
	"editBatchValidFlag" : function(flag, url) {
		var param = "";
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
		
		param += "&" + $("#brandInfoId").serialize();
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
		param += "&csrftoken="+parentTokenVal();
//		alert(param);
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		bscom03_deleteConfirm(flag, url, param, callback);
	},
	/**
	 * 清空提示消息
	 */
	"cleanMsgDIV" : function (){
		// 清空消息
		$("#actionResultDisplay").empty();
		$("#errorMessage").empty();
	},
	// 删除分类
	"removeCate":function(_this){
		// 分类块
		var $obj = $(_this).parent().parent().parent();
		var dialogSetting = {
				dialogInit: "#dialogInit",
				text: $('#disableMessageCate').html(),
				width: 	500,
				height: 300,
				title: 	$('#disableTitleCate').html(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){
					$obj.remove();
					JCS50_cate.addCate();
					removeDialog("#dialogInit");
				},
				cancelEvent: function(){
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
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
	"openCateDialog" : function(propId,_this){
		var $this = $(_this);
		var cateInfo = "[{}]";
		var $trs = $this.parent().parent().parent().siblings();
		cateInfo = Obj2JSONArr($trs);
		var option = {
         	   targetId: "prtCate_" + propId,
         	   propId : propId,
 	           checkType : "checkbox",
 	           cateInfo :cateInfo,
 	           mode : 2,
 	           click:function(){
 	      		JCS50_cate.addCate();
 	           },
 		       getHtmlFun:function(info){
		       			var html = '<tr><td><span class="list_normal">';
		       			html += '<span class="text" style="line-height:19px;">' + info.cateValName + '</span>';
		       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="BINOLPTJCS50.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
  		       			html += '<input type="hidden" name="cateValId" value="' + info.cateValId + '"/>';
  		       			html += '<input type="hidden" name="propValId" value="' + info.cateValId + '"/>';
  		       			html += '</span></td></tr>';
 				return html;
 	           }
 	        };
 		popAjaxPrtCateDialog(option);
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
	}
		
};

/**
 * 产品方案明细添加分类相关
 * @returns
 */
function BINOLPTJCS50_cate(){
};
BINOLPTJCS50_cate.prototype={
	/**
	 * 方案明细添加产品分类
	 */
	"addCate" : function(){
   		// 产品分类
   		var param  = "cateInfo=" + BINOLPTJCS50.toJSONArr($("#cateInfo").find(".detail").children().children());
//   		alert("分类值: "+param);
   		
		var url = $("#addCate_Url").attr("href");
		param += "&productPriceSolutionID="+ $("#productPriceSolutionID").val();
		param += "&csrftoken="+parentTokenVal();
		var callback = function(msg){
			BINOLPTJCS50.search();
//			if(msg.indexOf("actionMessage") > -1){
//				alert('添加成功');
////				BINOLSTSFH01.clearPage(true);
//			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback,
			coverId : "#div_main"
		});
	}	
}


var BINOLPTJCS50 = new BINOLPTJCS50();
var JCS50_cate = new BINOLPTJCS50_cate();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLPTJCS50.search();
	
});
window.onbeforeunload = function(){
	if (BINOLPTJCS50.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

