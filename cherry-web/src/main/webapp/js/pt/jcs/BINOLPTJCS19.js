
function BINOLPTJCS19(){
	this.needUnlock = true;
};

BINOLPTJCS19.prototype={
	"doClose" : function(){
		window.close();
	},
	"edit" : function(url){
		BINOLPTJCS19.cleanMsgDIV();
		var tokenVal = parentTokenVal();
		$("#parentCsrftoken").val(tokenVal);
		this.needUnlock = false;
		$("#toEditForm").submit();
	},
	"addPrt" : function (url){
		var tokenVal = parentTokenVal();
		$("#parentCsrftokenToAddForm").val(tokenVal);
		$("#toAddForm").submit();
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
				                { "sName": "checkbox", "sWidth": "2%","bSortable": false}, 			// 0
				                { "sName": "number", "sWidth": "5%", "bSortable": false},// 1
				                { "sName": "unitCode", "sWidth": "17%" },// 2
				                { "sName": "barCode", "sWidth": "17%" },// 3
				                { "sName": "nameTotal", "sWidth": "20%" },// 4
				                { "sName": "salePrice", "sWidth": "5%","sClass":"alignRight" , "bSortable": false},// 5
				                { "sName": "memPrice", "sWidth": "5%","sClass":"alignRight" , "bSortable": false},// 6
				                { "sName": "操作", "sWidth": "5%","sClass":"center", "bSortable": false }// 7
				                ],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				aiExclude :[0,1],
				fixedColumns : 2,
				iDisplayLength : 10,
				fnDrawCallback : function() {
				}
		};
		
		// 非小店云模式oSettings.aoColumns[i] is undefined
		if($("#isPosCloud").val()==0){
			// 去除条码显示
			tableSetting.aoColumns.splice(3,1,{ "sName": "barCode","bVisible" : false});
			tableSetting.aoColumns.splice(4,0,{ "sName": "originalBrand", "sWidth": "15%","bVisible" : false}); // 子品牌
			tableSetting.aiExclude = [0,1,3];
		} 
		// 小店云模式
		else if ($("#isPosCloud").val()==1){
			// 显示方案产品名称(同时去除标准产品名称)
			tableSetting.aoColumns.splice(4,1,{ "sName": "soluProductName", "sWidth": "20%"});
			tableSetting.aoColumns.splice(4,0,{ "sName": "originalBrand", "sWidth": "15%"}); // 子品牌
		}
		
		// 调用获取表格函数
		getTable(tableSetting);
    },
    /** ------------以下为添加产品明细维护------------------------------------------           */
	"openChkPrtDialogInit" : function(){
		BINOLPTJCS19.cleanMsgDIV();
		if(BINOLPTJCS19.disabOptBtn()==false)return;
		var _this = this;
		var $addPrtTable = $("#addPrtTable").clone();
		// 非小店云模式，去除条码显示
		if($("#isPosCloud").val()==0){
			$addPrtTable.find("#thBarCodeId").remove();
		}
		$addPrtTable.find("#databody").attr('id','newDatabody');
//		$addPrtTable.find("#allSelect").attr('id','newAllSelect');
		
		var text = '<div id="prtSaveDiv">'  + $addPrtTable.html() + '</div>';
		var title = "";
//				text = '<p class="message"><span>'+$('#confirIsEnable').text();
//				text = '<p class="message"><span>'+'请确认现在下发柜台产品数据吗？';
//				title = $('#enableValTitle').text();
		title = '添加方案产品明细';
		if($("#isPosCloud").val()==0){
			title = '添加方案产品明细';
		} else if($("#isPosCloud").val()==1){
			title = '添加产品列表明细';
		}
		var dialogSetting = {
				dialogInit: "#dialogChkPrtInitDIV",
				text: text,
				width: 	1050,
				height: 500,
				title: 	title,
//					confirm: $("#dialogConfirmIss").text(),
				confirm: '添加',
//					cancel: $("#dialogCancelIss").text(),
				cancel: '取消',
				confirmEvent: function(){
					
					BINOLPTJCS19.addRow();
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
			isPosCloud : $("#isPosCloud").val(),  // 是否小店云模式（小店云显示barcode）
			brandInfoId : $("#brandInfoId").val(),// 品牌ID
			getHtmlFun:that.getHtmlFun,// 目标区追加数据行function
			click:function(){//点击确定按钮之后的处理
				var checkbox= $('#dataTableBody').find(':input').is(":checked");
				//改变奇偶行的样式
				BINOLPTJCS19.changeOddEvenColor();
				// 拖动排序
				$("#prtSaveDiv #mainTable #databody").sortable({
					update: function(event,ui){BINOLPTJCS19.changeOddEvenColor();}
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
		var originalBrand = info.originalBrand;// 子品牌
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
		html += '<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLPTJCS19.changechkbox(this);"/></td>';
		html += '<td>' + unitCode +'<input type="hidden" id="unitCodeArr"  value="'+ unitCode + '"/></td>';
		// 小店云模式，显示条码及可修改方案名称
		if($("#isPosCloud").val()==1){
			html += '<td>' + barCode +'<input type="hidden" id="barCodeArr"  value="'+ barCode + '"/></td>';
			html += '<td>' + originalBrand + '</td>';
			html += '<td class="center"><input value="' + nameTotal +'" name="soluProductNameArr"  type="text" id="soluProductNameArr"  maxlength="80" cssStyle="width:98%" onchange="" /></td>';
		}else if($("#isPosCloud").val()==0){
			html += '<td>' + originalBrand + '</td>';
			html += '<td>' + nameTotal + '</td>';
		}
		//html += '<td>' + barCode + '<input type="hidden" id="barCodeArr" value="'+  barCode + '"/></td>';
		html += '<td class="center"><input value="' + price +'" name="salePriceArr"  type="text" id="salePriceArr" class="price" maxlength="17" cssStyle="width:98%" onchange="BINOLPTJCS19.setPrice(this);" /></td>';
		html += '<td class="center"><input value="' + memPrice +'" name="memPriceArr"  type="text" id="memPriceArr" maxlength="17"  cssStyle="width:98%" onchange="BINOLPTJCS19.setPrice(this)" /></td>';
		html +='<td style="display:none">'
			+'<input type="hidden" name="prtVendorId" value="'+ productVendorId + '"/>'
			+'<input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value="'+productVendorId+'"/>'
			+'<input type="hidden" id="prtIdArr" name="prtIdArr" value="'+ prtId +'"/></td></tr>';
		return html;
	},
	/**
	 * 删除选中行
	 */
	"deleterow":function(){
		BINOLPTJCS19.cleanMsgDIV();
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
			BINOLPTJCS19.search();
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
		BINOLPTJCS19.cleanMsgDIV();
//		var that = this;
//		that.empty();
		var $this = $(obj);
		
		if(BINOLPTJCS19.disabOptBtn()==false)return;
//		alert($this.parent().parent().parent().html());
		if($("#isPosCloud").val()==1){
			$this.parent().parent().parent().find("[name='soluProductName']").parent().prev().hide();
			$this.parent().parent().parent().find("[name='soluProductName']").parent().show();
		}
		
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
				
				$curRow.find("[name='soluProductName']").parent().prev().show();
				$curRow.find("[name='soluProductName']").parent().hide();
				
				$curRow.find(".btnEdit").show();
				$curRow.find(".btnSave").hide();
				if(map.result == 0){
					$curRow.find("[name='salePrice']").parent().prev().text($curRow.find("[name='salePrice']").val());
					$curRow.find("[name='memPrice']").parent().prev().text($curRow.find("[name='memPrice']").val());
					$curRow.find("[name='soluProductName']").parent().prev().text($curRow.find("[name='soluProductName']").val());
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
	}
		
};


var BINOLPTJCS19 = new BINOLPTJCS19();
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	BINOLPTJCS19.search();
	
	//88888888888888888888888888888888888888
	/*
	
	alert(oTableArr[0]);
	
	var oTable = oTableArr[0];
	
	var url = $("#editRow_Url").attr("href");
	var params= "productPriceSolutionID="+ $("#productPriceSolutionID").val();
	if(params != null && params != "") {
		url = url + "?" + params;
	}
	url = url + "&csrftoken="+parentTokenVal()+"&test=1";
	*/
	/* Apply the jEditable handlers to the table */
	/*
	$('td', oTable.fnGetNodes()).editable( url, {
		indicator : 'Saving...',
		tooltip : 'Click to edit...',
		"callback": function( sValue, y ) {
			alert('callback');
			var aPos = oTable.fnGetPosition( this );
			alert(aPos);
			var map = eval("(" + sValue + ")");
			oTable.fnUpdate( map.value, aPos[0], aPos[1] );
		},
		"submitdata": function ( value, settings ) {
			return {
				"row_id": this.parentNode.getAttribute('id'),
				"column": oTable.fnGetPosition( this )[2]
			};
		},
		"height": "14px"
	} );
	*/
	//88888888888888888888888888888888888888888888
	//双击
	/*
	  $("#productInDepotExcelInfoDataTable tbody tr").dblclick(function () {
		  var dd = $(this).val();
		  alert(dd);
//	              if ($(this).hasClass('row_selected')) {
//	   
//	              }
//	              else {
//	                  editor.$('tr.row_selected').removeClass('row_selected');
//	                  $(this).addClass('row_selected');
//	              }
//	 
//	             var aData = editor.fnGetData(this);
//	             ......//对得到的数据可以操作
//	              $("#e_Attributes").dialog("open");//打开dialog
	 
	 });
	 */
});
window.onbeforeunload = function(){
	if (BINOLPTJCS19.needUnlock ) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

