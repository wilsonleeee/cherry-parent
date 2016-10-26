var BINOLSTIOS04 = function () {};

BINOLSTIOS04.prototype = {
	
	"submitflag":true,
		
	/**
	  * 删除掉画面头部的提示信息框
	  * @return
      */
	"clearActionMsg":function(){
		$('#actionResultDisplay').html("");
		$('#errorDiv2').attr("style",'display:none');
	},
	
	/**
	 * 部门弹出框
	 * @return
	 */
	"openDepartBox":function(thisObj){
		if($("#spanBtnCancel").is(":visible")){
			return;
		}
	 	//取得所有部门类型
	 	var departType = "";
	 	var witposStacking = $("#witposStaking").val();
	 	if(witposStacking != "1"){
		 	for(var i=0;i<$("#departTypePop option").length;i++){
		 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
		 		////排除4柜台
		 		if(departTypeValue != "4"){
		 			departType += "&departType="+departTypeValue;
		 		}
		 	}
	 	}else{
	 		for(var i=0;i<$("#departTypePop option").length;i++){
		 		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
		 		departType += "&departType="+departTypeValue;
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
				BINOLSTIOS04.chooseDepart();
				if(witposStacking == "1"){
					BINOLSTIOS04.getLogDepotByAjax(departId);
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
				BINOLSTIOS04.clearActionMsg();
			}
		}else{
			$("#depotInfoId").attr("disabled",true);
			$("#logicDepotsInfoId").attr("disabled",true);
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00015').val());
			$('#errorDiv2').show();
		}
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
			if(i>0){
					if($(this).prop("checked")!=true){
						flag=false;
					}	
				}
			
			});
		if(flag){
			$('#allSelect').prop("checked",true);
		}
	},
	
	"changeOddEvenColor":function(){
		$("#databody tr:odd").attr("class","odd");
		$("#databody tr:even").attr("class","even");
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
		var $tr_obj = $(thisObj).parent().parent();
		var trID = $tr_obj.attr('id');
		var index = trID.substr(7);
		
		var count = parseInt($(thisObj).val(),10);
		var allowNegativeFlag = $("#allowNegativeFlag").val();
		if(allowNegativeFlag != "0"){
			count = Math.abs(count);
		}
		if(isNaN(count)){		
			$(thisObj).val("");
			//清空盘差
			$('#gainCount'+index).html("");
			$('#gainCountArr'+index ).val("");
			//清空盘差金额
			$tr_obj.find('#dataTd9').html("");
			return;
		}	
		$(thisObj).val(count);
		if($('#bookCountArr'+index).val() != "" && $('#bookCountArr'+index).val() != undefined){
			var tempCount = count-$('#bookCountArr'+index).val();
			$('#gainCount'+index ).html(tempCount);
			$('#gainCountArr'+index ).val(tempCount);
			//盘差金额
			var price = Number($('#priceArr'+index).val());
			var gainMoney = price*tempCount;
			$tr_obj.find('#dataTd9').html(gainMoney.toFixed(2));
		}
	},
	
	"clearDetailData":function(){
		$("#databody > tr[id!='dataRow0']").remove();
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
				if($("#BatchStockTakingTrue").prop("checked") == true && ($(this).find("td:eq(5) [name=batchNoArr]").val() == undefined || $(this).find("td:eq(5) [name=batchNoArr]").val()=="")){
					flag = false;
					$(this).attr("class","errTRbgColor");
				}else if($("#BatchStockTakingTrue").prop("checked") == true && $(this).find("td:eq(5) #errorText").length>0){
					flag = false;
					$(this).attr("class","errTRbgColor");	
				}else if($(this).find("[name=quantityArr]").val()== undefined || $(this).find("[name=quantityArr]").val()==""){
					flag = false;
					$(this).attr("class","errTRbgColor");				
				}else if(isNaN($(this).find("[name=quantityArr]").val())){
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
	        //设置滚动条回到顶端
	        $('html,body').animate({scrollTop: '0px'}, 0);
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
	 * 提交前对数据进行检查(盲盘)
	 * @returns {Boolean}
	 */
	"checkDataBlind":function(){
		var flag = true;
		var count= 0;
		//检查有无空行
		$.each($('#databody >tr'), function(i){	
				count +=1;
				if($("#BatchStockTakingTrue").prop("checked") == true && ($(this).find("td:eq(5) [name=batchNoArr]").val() == undefined || $(this).find("td:eq(5) [name=batchNoArr]").val()=="")){
					flag = false;
					$(this).attr("class","errTRbgColor");
				}else if($("#BatchStockTakingTrue").prop("checked") == true && $(this).find("td:eq(5) #errorText").length>0){
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
	        //设置滚动条回到顶端
	        $('html,body').animate({scrollTop: '0px'}, 0);
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
	 * 检查盲盘时是否输入数量
	 * @return
	 */
	"checkNullBlind":function(){
		var flag = true;
		var length = $('#databody').find("[name='quantityArr']").length;
		for(var i=0;i<length;i++){
			var curValue = $($('#databody').find("[name='quantityArr']")[i]).val();
			if(curValue == ""){
				flag = false;
				break;
			}
		}
		return flag;
	},
	
	/**
	 * 反映到库存
	 * @return
	 */
	"saveToDB":function(){
		var param=$("#mainForm").formSerialize();
		param += "&depotInfoId="+$("#depotInfoId").val();
		if($("#logicDepotsInfoId").val() != undefined && $("#logicDepotsInfoId").val() != ""){
			param += "&logicDepotsInfoId="+$("#logicDepotsInfoId").val();
		}
		if($("#BatchStockTakingTrue").prop("checked") == true){
			param += "&isBatchStockTaking=true";
		}else{
			param += "&isBatchStockTaking=false";
		}
		var url=$("#urlSave").text();
		var callback=function(msg){
			if((msg.indexOf('errorMessage') == -1) && (msg.indexOf('hidFieldErrorMsg') == -1)){
				$("#mydetail").empty();
				BINOLSTIOS04.cancelStocktaking();
			}
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	},
	
	/**
	 * 盘点确认
	 * @returns {Boolean}
	 */
	"confirm":function(){
		this.clearActionMsg();
		if($("#blindFlag").val()=="0"){
			if(!this.checkData()){
				return;
			}else{
				this.saveToDB();
			}
		}else{
			if(!this.checkDataBlind()){
				return;
			}else{
				//盲盘不输入数量，出现确认自动清零提示框
				if(!this.checkNullBlind()){
					var dialogSetting = {
							dialogInit: "#dialogInit",	
							width: 	300,
							height: 180,
							confirm: $("#dialogConfirm").text(),
							cancel: $("#dialogCancel").text(),
							confirmEvent: function(){BINOLSTIOS04.saveToDB();removeDialog("#dialogInit");},
							cancelEvent: function(){removeDialog("#dialogInit");}
					};
					openDialog(dialogSetting);
					$("#dialogInit").html($("#errmsg_EST00024").val());
				}else{
					this.saveToDB();
				}
			}
		}
	},
	
	/**
	 * 暂存
	 * @return
	 */
	"saveTemp":function(){
		var param=$("#mainForm").formSerialize();
		param += "&depotInfoId="+$("#depotInfoId").val();
		if($("#logicDepotsInfoId").val() != undefined && $("#logicDepotsInfoId").val() != ""){
			param += "&logicDepotsInfoId="+$("#logicDepotsInfoId").val();
		}
		if($("#BatchStockTakingTrue").prop("checked") == true){
			param += "&isBatchStockTaking=true";
		}else{
			param += "&isBatchStockTaking=false";
		}
		var url=$("#urlSaveTemp").text();
		var callback=function(msg){
			if((msg.indexOf('errorMessage') == -1) && (msg.indexOf('hidFieldErrorMsg') == -1)){
				$("#mydetail").empty();
				BINOLSTIOS04.cancelStocktaking();
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
		if($("#blindFlag").val()=="0"){
			if(!this.checkData()){
				return;
			}else{
				this.saveTemp();
			}
		}else{
			if(!this.checkDataBlind()){
				return;
			}else{
				//盲盘不输入数量，出现确认自动清零提示框
				if(!this.checkNullBlind()){
					var dialogSetting = {
							dialogInit: "#dialogInit",	
							width: 	300,
							height: 180,
							confirm: $("#dialogConfirm").text(),
							cancel: $("#dialogCancel").text(),
							confirmEvent: function(){BINOLSTIOS04.saveTemp();removeDialog("#dialogInit");},
							cancelEvent: function(){removeDialog("#dialogInit");}
					};
					openDialog(dialogSetting);
					$("#dialogInit").html($("#errmsg_EST00024").val());
				}else{
					this.saveTemp();
				}
			}
		}
	},
	
	/**
	 * 判断批次号是否存在
	 * @param msg
	 * @return
	 */
	"checkBatchNo":function(thisObj){
		var productVendorID= $(thisObj).parent().parent().parent().find("#dataTd11 [name='productVendorIDArr']").val();
		var url = $("#s_checkBatchNo").text();
		var index = $(thisObj).parent().parent().parent().attr("id");
		var param = "currentIndex="+index+"&batchNo="+$(thisObj).val()+"&depotInfoId="+$("#depotInfoId").val()+"&productVendorId="+productVendorID;
		if ($("#logicDepotsInfoId").length>0 && $("#logicDepotsInfoId").val()!=""){
			param += "&logicDepotsInfoId="+$("#logicDepotsInfoId").val();
		}
		var callback=function(msg){
			var member = eval("("+msg+")");
			$.each(member, function(i){
				var curIndex = member[i].currentIndex.substr(7);
				if(member[i].productBatchId != null && member[i].productBatchId != ""){
					$("#bookCountArr"+curIndex).val(member[i].quantity);
					$('#'+member[i].currentIndex +' > #dataTd6').html(member[i].quantity);
					$('#'+member[i].currentIndex +' > #dataTd5 span').removeClass("error");
					$('#'+member[i].currentIndex +' > #dataTd5 span').find("#errorText").remove();
					$("#cluetip").hide();
					var curQuantity = $('#'+member[i].currentIndex+ " #dataTd7 [name=quantityArr]").val();
					if(curQuantity != "" && curQuantity != undefined){
						var gc = member[i].quantity - curQuantity;
						$('#gainCount'+curIndex).html(gc);
						//盘差金额
						var price = $('#'+member[i].currentIndex +' > #dataTd9').html();
						var gainMoney = gc*price;
						$('#'+member[i].currentIndex +' > #dataTd9').html(gainMoney.toFixed(2));
						$('#gainCountArr'+curIndex).val(gc);
					}
				}else{
					$('#'+member[i].currentIndex +' > #dataTd6').html("");
					//清空盘差
					$('#gainCount'+curIndex).html("");
					$('#gainCountArr'+curIndex ).val("");
					$("#bookCountArr"+curIndex).val("");
					$('#'+member[i].currentIndex +' > #dataTd5 span input').parent().append(errSpan($('#errmsg_EST00017').val()));
					$('#'+member[i].currentIndex +' > #dataTd5 span input').parent().addClass("error");
				}
			});
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
		BINOLSTIOS04.submitflag=true;
		if($('#actionResultDiv').attr('class')=='actionSuccess'){
			$("#databody > tr").each(function(i){
				if(i>0){
					$(this).remove();
				}			
			});
		}
	},
	
	/**
	 * 开始盘点
	 * @return
	 */
	"startStocktaking":function(){
		
		this.clearActionMsg();
		if($("#organizationId").val() == undefined || $("#organizationId").val() == "" ){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00013').val());
			$('#errorDiv2').show();
			return false;
		}
		if($("#depotInfoId").val() == undefined || $("#depotInfoId").val() == "" ){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00006').val());
			$('#errorDiv2').show();
			return false;
		}
		//盘点理由字符长度后端验证
		var commentLength =$("#comments").val().length;
		if(commentLength > 100){
			$('#errorDiv2 #errorSpan2').html($('#errmsg_EST00030').val());
			$('#errorDiv2').show();
			return false;
		}
		var params =$('#mainForm').formSerialize();
		$("#openDepartBox").addClass("ui-state-disabled");
		$("#depotInfoId").attr('disabled',true);
		$("#logicDepotsInfoId").attr('disabled',true);
		$("[name=isBatchStockTaking]").attr('disabled',true);
		$("[name=categoryArr]").attr('disabled',true);
		
		$('#spanBtnStart').hide();
		$('#spanBtnCancel').show();
		
		if(BINOLSTIOS04.submitflag){
			BINOLSTIOS04.submitflag=false;
			var argUrl = $('#s_startStocktaking').html();	
			ajaxRequest(argUrl,params,BINOLSTIOS04.startStocktakingResult);		
		}
		
	},
	
	"startStocktakingResult":function(msg){
		BINOLSTIOS04.clearActionMsg();
		$('#ajaxerrdiv').empty();
		$('#mydetail').empty();
		submitflag =true;
		if(msg.indexOf("hidden1Result1Div1")!=-1){
			//正常执行的话返回的就是详细的jsp页面
			$('#mydetail').html(msg);
			BINOLSTIOS04.bindInput();
		}else{
			$('#actionResultDisplay').html(msg);
			BINOLSTIOS04.cancelStocktaking();
		}
	},
	
	/**
	 * 取消盘点
	 * @return
	 */
	"cancelStocktaking":function(){
		//退出盘点时不再维持session
		window.clearInterval(refreshSessionTimerID);
		refreshSessionTimerID = null;
		
		$("#openDepartBox").removeClass("ui-state-disabled");
		$("#depotInfoId").attr('disabled',false);
		$("#logicDepotsInfoId").attr('disabled',false);
		$("[name=isBatchStockTaking]").attr('disabled',false);
		$("[name=categoryArr]").attr('disabled',false);
		
		$('#spanBtnCancel').hide();
		$('#spanBtnStart').show();
		$('#mydetail').empty();
		BINOLSTIOS04.submitflag=true;
	},
	
	/**
	 * 绑定输入框
	 */
	"bindInput":function(){
		var tableName = "mainTable";
		$("#"+tableName+" #databody tr").each(function(i) {
			var trID = $(this).attr("id");
			var trParam = "#"+tableName +" #"+trID;
			BINOLSTIOS04.bindInputTR(trParam);
		});
	},

	"bindInputTR":function(trParam){
		//按Enter键到下一个文本框，当前行最后一个文本框按Enter键到下一行或新增行。
		var $inp = $(trParam+" input:text");
		$inp.bind('keydown', function (e) {
			if($("#currentMenuID").val() == "BINOLSTIOS04"){
				var key = e.which;
				if (key == 13) {
					//回车键
					e.preventDefault();
					var nxtIdx = $inp.index(this) + 1;
					var $nextInputText = $(trParam+" input:text:eq("+nxtIdx+")");
					if($nextInputText.length>0){
						//跳到下一个文本框
						$nextInputText.focus();
					}else{
						//跳到下一行第一个文本框
						$(trParam).next().find("input:text:visible:eq(0)").focus();
					}
				}
			}
		});
	}
	
};

var BINOLSTIOS04 = new BINOLSTIOS04();

$(function(){
	//在右边column高于左边column的情况下，设置左边column高度为右边column高度。
	var leftHeight = $("#divColumnLeft").height();
	var rightHeight = $("#divColumnRight").height();
	if(rightHeight>leftHeight){
		$("#divColumnLeft").height(rightHeight);
	}
	
	BINOLSTIOS04.chooseDepart();
//	$('[name="isBatchStockTaking"]').live('click',function(){
//		if($("#BatchStockTakingTrue").prop("checked") == true){
//			$("#mainTable th:eq(4)").show();
//			$("#dataRow0 td:eq(4)").show();
//			BINOLSTIOS04.clearDetailData();
//		}else if($("#BatchStockTakingFalse").prop("checked") == true){
//			$("#mainTable th:eq(4)").hide();
//			$("#dataRow0 td:eq(4)").hide();
//			BINOLSTIOS04.clearDetailData();
//		}
//	});
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
