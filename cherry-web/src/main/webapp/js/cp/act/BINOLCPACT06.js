
function BINOLCPACT06() {
	
};

BINOLCPACT06.prototype = {
	//活动结果查询
	"search" : function (flag){
		var $form = $('#mainForm');
		// 表单验证
		if(!$form.valid()){
			return;
		}
		var $text = $form.find(':text');
		$text.each(function(){
			$(this).val($.trim($(this).val()));
		});
		var url = $('#campOrderUrl').val();
		url += "?" + getSerializeToken();
		url += "&" + $form.serialize();
		var tableSetting = {
				// datatable 对象索引
				index : 1,
				// 表格ID
				tableId : '#dataTable',
				// 数据URL
				url : url,
				// 排序列
				aaSorting : [[1, "DESC"]],
                // 不可设置显示或隐藏的列	
      			aiExclude :[0,1],
      			// 横向滚动条出现的临界宽度
    			sScrollX : "100%",
    			// 固定列数
				fixedColumns : 3
		 };
		var aoColumns = [{ "sName": "no","bSortable": false}, 				// 0
				           	{ "sName": "billNo"},							// 1
				           	{ "sName": "memNameCode"},  					// 2
							{ "sName": "mobile"},							// 3
							{ "sName": "messageId","bVisible": false},		// 4
							{ "sName": "counterOrder","bVisible": false},	// 5
							{ "sName": "counterGot"},						// 6
							{ "sName": "couponCode"},						// 7
							{ "sName": "getFromTime"},						// 8
							{ "sName": "getToTime"},						// 9
							{ "sName": "campOrderTime","bVisible": false},	    		// 10
							{ "sName": "bookDate","bVisible": false},	    			// 11
							{ "sName": "bookTimeRange","bVisible": false},	    		// 12
							{ "sName": "cancelTime","bVisible": false},	    			// 13
							{ "sName": "finishTime","bVisible": false},	    			// 14
							{ "sName": "quantity","bVisible": false},  		// 15
							{ "sName": "amout","bVisible": false}, 			// 16
							{ "sName": "pointRequired","bVisible": false}, 	// 17
					    	{ "sName": "state","sClass":"center"}, 				    		// 18
					    	{ "sName": "testType","sClass":"center","bVisible": false},		// 19
					    	{ "sName": "dataChannel","sClass":"center","bVisible": false},	// 20
					    	{ "sName": "sendFlag","sClass":"center","bVisible": false}];	// 21
		if(!isEmpty(flag)){
			aoColumns = [{ "sName": "no","bSortable": false}, 					// 0
					           	{ "sName": "billNo"},							// 1
					           	{ "sName": "memNameCode"},  						// 2
								{ "sName": "mobile"},							// 3
								{ "sName": "messageId","bVisible": false},      //4
								{ "sName": "testType","sClass":"center","bVisible": false},//5
								{ "sName": "counterOrder","bVisible": false},						// 6
								{ "sName": "counterGot"},						// 7
								{ "sName": "couponCode"},						// 8
								{ "sName": "campOrderTime"},	    			// 9
								{ "sName": "bookDate","bVisible": false},	    			// 10
								{ "sName": "bookTimeRange","bVisible": false},	    			// 11
								{ "sName": "cancelTime","bVisible": false},	    			// 12
								{ "sName": "finishTime","bVisible": false},	    			// 13
								{ "sName": "quantity"},  						// 14
								{ "sName": "amout"}, 							// 15
								{ "sName": "dataChannel","sClass":"center","bVisible": false},	// 16
						    	{ "sName": "option","sClass":"center","bSortable": false}];//17
		}
		tableSetting.aoColumns = aoColumns;
		//DataTable结果显示
		$("#section").show();
		// 调用获取表格函数
		getTable(tableSetting);
	},
	/**
	 * 根据ID查询礼品信息
	 */
	"getPrtInfo" : function(obj,temp) {
		var $this = $(obj);
		var that = this;
		var url = $this.attr("href");
		var dialogSetting = {
			dialogInit : "#popPrtTable",
			width : 800,
			height : 560,
			zIndex: 9999,  
			resizable : true,
			title : $("#dialogTitle").text(),
			confirm :$("#dialogOk").text(),
			cancel: $("#dialogClose").text(),
			confirmEvent : function() {
				that.deleteMsg();
				that.updDetailInfo();
			},
			cancelEvent: function(){
				removeDialog("#popPrtTable");
			}
		};
		openDialog(dialogSetting);
		that.setSaveBtn();
		var callback = function(msg) {
			var $temp = temp; 
			//取得活动明细信息
			$("#popPrtTable").html(msg);
			//OK已领用状态
			var campState = $("#campState").val();
			//隐藏领用结束日期按钮
			$("#fromTime").next().hide();
			$("#endTime").next().hide();
			if($temp==0 || campState=="OK" || campState=="CA"){
				$("#editCountId").hide();
				$("#editTimeId").hide();
				$("#editFromTimeId").hide();
			}
			//绑定领用柜台
			counterBinding({
				elementId:"counterCode",
				showNum:10,
				selected :"code"
			});
			//验证表单数据
			cherryValidate({			
				formId: "detailForm",		
				rules: {
					counterCode:{required:true},
					fromTime: {required: true,dateValid: true},
					endTime: {required: true,dateValid: true}
				}		
			});
		};
		var $url= url+'&csrftoken='+parentTokenVal();
		cherryAjaxRequest( {
			url : $url,
			callback : callback
		});
	},
	"option" :function(url,billNo){
		var that = this;
		if(isEmpty(billNo)){
			var $form = $('#mainForm');
			var $text = $form.find(':text');
			$text.each(function(){
				$(this).val($.trim($(this).val()));
			});
			url += "?" + getSerializeToken();
			url += "&" + $form.serialize();
		}
		var $msgDiv = $('#actionMsgDiv');
		$msgDiv.empty();
		cherryAjaxRequest( {
			url : url,
			callback : function(msg){
				$msgDiv.html(msg);
				that.search('1');
			}
		});
	},
	"confirmInit":function(url,conUrl,billNo){
		var _this = this;
		var dialogSetting = {
			dialogInit: "#confirmInitDIV",
			width: 	500,
			height: 380,
			title: 	$("#confirmTitle").text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){
				var $count=$("#countId").val();
				if($count>0){
					_this.option(url,billNo);
				}
				removeDialog("#confirmInitDIV");
			},
			cancelEvent: function(){removeDialog("#confirmInitDIV");}
		};
		openDialog(dialogSetting);
		var $conditDiv=$("#conditDiv");
		var $mainForm = $("#mainForm");
		var $confirmInitDIV = $('#confirmInitDIV');
		var $confirmInitBody = $('#confirmInitBody');
		var $messageCondition =$('#messageCondition');
		var html = '';
		var callback = function(msg) {
			$mainForm.find(".box-content").find("p.CONDISOIN").each(function(){
				var $this = $(this);
				var $inputs = $this.find(":input").not('button');
				var $testType = $("#testType option:selected").text();
				var showConFlag = false;
				$inputs.each(function(){
					var $input = $(this);
					if(!$input.is('select')){
						if(!isEmpty($input.val())){
							showConFlag = true;
							return;
						}
					}else{
						showConFlag = true;
					}
				});
				if(showConFlag){
					//活动搜索条件
					$messageCondition.text($('#conditionTip').text());
					var keyName = $.trim($this.find('label').text());
					html+='<div class="clearfix">';
					html+='<div class="left" style="width:25%;"><span class="ui-icon icon-arrow-crm"></span>';
					html+=keyName;
					html+='</div>';
					html+='<p style="margin:0; width:75%;" class="left"><span>';
					$inputs.each(function(i){
						var $input = $(this);
						if($input.is('select')){
							if($testType==""){//全部会员
								$testType=$("#selectAll").text();
							}
							html += $testType;
						}else{
							if(i != 0){html += '&nbsp;~&nbsp;'}
							html += $.trim($input.val());
						}
					});
					html += '</span></p></div>';
				}
			});
			$conditDiv.html(html);
			$('#count').text(msg);
			$('#countId').val(msg);
			$confirmInitDIV.html($confirmInitBody.html());
		};
		if(isEmpty(billNo)){
			//条件url
			conUrl += "&" + $("#mainForm").serialize();
			cherryAjaxRequest( {
				url:conUrl,
				callback : callback
			});
		}else{
			//您所选择的单据号
			$messageCondition.text($('#optionBillNo').text());
			html+='<div class="clearfix">';
			html+='<div class="left" style="width:25%;"><span class="ui-icon icon-arrow-crm"></span>';
			html+=$("#campBillNo").text();
			html+='</div>';
			html+='<p style="margin:0; width:75%;" class="left"><span>';
			html += billNo +'</span></p></div>';
			$conditDiv.html(html);
			$('#count').text("1");
			$confirmInitDIV.html($confirmInitBody.html());
		}
	},
	/**
	 * 活动明细Excel导出
	 */
       "exportExcel":function (url){
    	//无数据不导出
        if($(".dataTables_empty:visible").length==0){
    	    if (!$('#mainForm').valid()) {
                return false;
            }
    	    url += "?" + getSerializeToken();
    		url += "&" + $("#mainForm").serialize();
    		document.location.href = url;
        }
    },
    "editCounter": function(object) {//编辑领用柜台
    	var that = this;
    	if($("#counterCode").is(":hidden")) {
    		$("#counterCode").show();
    		$("#counterCode").prev().hide();
    		$("#flagCountId").val("1");
    		$(object).attr("style","margin-top: -3px;");
    		$("#editCounterGotButton").prev().removeClass("icon-edit").addClass("icon-delete");
    		$("#editCounterGotButton").html($("#cancleButtonText").html());
    	} else {
    		$("#counterCode").hide();
    		$("#counterCode").prev().show();
    		$("#flagCountId").val("0");
    		$("#counterCode").val($("#counterGotOld").val());
    		$(object).attr("style","");
    		$("#editCounterGotButton").prev().removeClass("icon-delete").addClass("icon-edit");
    		$("#editCounterGotButton").html($("#editButtonText").html());
    		that.deleteMsg();
    	}
    	that.setSaveBtn();
    },
    "editFromDate":function(object) {//编辑领用开始日期
    	var that = this;
    	if($("#fromTime").is(":hidden")) {
    		$("#fromTime").show();
    		$("#fromTime").prev().hide();
    		$("#fromTime").next().show();
    		$("#fromTime").val($("#getFromTimeOld").val());
    		$("#flagFromTimeId").val("1");
    		$(object).attr("style","margin-top: -3px;");
    		$("#editFromTimeButton").prev().removeClass("icon-edit").addClass("icon-delete");
    		$("#editFromTimeButton").html($("#cancleButtonText").html());
    	} else {
    		$("#fromTime").hide();
    		$("#fromTime").prev().show();
    		$("#fromTime").next().hide();
    		$("#flagFromTimeId").val("0");
    		$("#fromTime").val($("#getFromTimeOld").val());
    		$(object).attr("style","");
    		$("#editFromTimeButton").prev().removeClass("icon-delete").addClass("icon-edit");
    		$("#editFromTimeButton").html($("#editButtonText").html());
    		that.deleteMsg();
    	}
    	that.setSaveBtn();
    },
    "editEndDate":function(object) {//编辑领用结束日期
    	var that = this;
    	if($("#endTime").is(":hidden")) {
    		$("#endTime").show();
    		$("#endTime").prev().hide();
    		$("#endTime").next().show();
    		$("#endTime").val($("#getToTimeOld").val());
    		$("#flagTimeId").val("1");
    		$(object).attr("style","margin-top: -3px;");
    		$("#editEndTimeButton").prev().removeClass("icon-edit").addClass("icon-delete");
    		$("#editEndTimeButton").html($("#cancleButtonText").html());
    	} else {
    		$("#endTime").hide();
    		$("#endTime").prev().show();
    		$("#endTime").next().hide();
    		$("#flagTimeId").val("0");
    		$("#endTime").val($("#getToTimeOld").val());
    		$(object).attr("style","");
    		$("#editEndTimeButton").prev().removeClass("icon-delete").addClass("icon-edit");
    		$("#editEndTimeButton").html($("#editButtonText").html());
    		that.deleteMsg();
    	}
    	that.setSaveBtn();
    }
    ,
    "updDetailInfo": function() {//更新处理
    	var url = $('#updDetailUrl').val();
		if(!$('#detailForm').valid()) {
			return false;
		}
		var callback = function(msg) {
			var msgJson = window.JSON.parse(msg);
			var  $errorDiv =$('#errorDiv #errorSpan');
			var  $successDiv =$('#successDiv #successSpan');
			if(msgJson.error==0){
				$("#errorSpan").html($("#message_Error1").text());
				$('#errorDiv').show();
			}else if(msgJson.error==1){
				$('#successDiv').show();
				$("#successSpan").html($("#message_Error2").text());
				//领用开始日期编辑
				$("#fromTime").prev().text(msgJson.getFromTime);
				$("#getFromTimeOld").val(msgJson.getFromTime);
				$("#fromTime").prev().show();
				$("#fromTime").next().hide();
				$("#fromTime").hide();
				//领用结束日期编辑
				$("#endTime").prev().text(msgJson.getToTime);
				$("#getToTimeOld").val(msgJson.getToTime);
				$("#endTime").prev().show();
				$("#endTime").next().hide();
				$("#endTime").hide();
				//柜台编辑
				$("#counterCode").prev().text(msgJson.counterGot);
				$("#counterGotOld").val(msgJson.counterGot);
				$("#counterCode").prev().show();
				$("#counterCode").next().hide();
				$("#counterCode").hide();
				//标记
				$("#flagCountId").val("0");
				$("#flagfromTimeId").val("0");
				$("#flagTimeId").val("0");
				$("#editCountId").hide();
				$("#editTimeId").hide();
				$("#editFromTimeId").hide();
				if(msgJson.sendFlag==0){
					$("#sendFlagId").text($("#message_Error6").text());
				}else if(msgJson.sendFlag==1) {
					$("#sendFlagId").text($("#message_Error7").text());
				}else if(msgJson.sendFlag==2){
					$("#sendFlagId").text($("#message_Error8").text());
				}
				if(msgJson.state=='AR'){
					$("#campStateId").text($("#message_Error11").text());
				}
				//成功后隐藏保存按钮
				$(".ui-dialog-buttonset").find('button').eq(0).hide();
				//成功后刷新父页面
				BINOLCPACT06.search();
			}else if(msgJson.error==2){
				$errorDiv.html(msgJson.errorMsg)
				$("#errorSpan").html($("#message_Error3").text());
				$('#errorDiv').show();
			}else if(msgJson.error==3){
				$errorDiv.html(msgJson.errorMsg)
				$("#errorSpan").html($("#message_Error4").text());
				$('#errorDiv').show();
			}else if(msgJson.error==4){
				$("#errorSpan").html($("#message_Error9").text());
				$('#errorDiv').show();
			}else if(msgJson.error==5){
				$("#errorSpan").html($("#message_Error5").text());
				$('#errorDiv').show();
			}else if(msgJson.error==6){
				$("#errorSpan").html($("#message_Error10").text());
				$('#errorDiv').show();
			}else if(msgJson.error==7){
				$("#errorSpan").html($("#message_Error12").text());
				$('#errorDiv').show();
			}else if(msgJson.error==8){
				$("#errorSpan").html($("#message_Error13").text());
				$('#errorDiv').show();
			}else if(msgJson.error==9){
				$("#errorSpan").html($("#message_Error14").text());
				$('#errorDiv').show();
			}
		};
		var $param= $('#detailForm').serialize()+'&csrftoken='+parentTokenVal();
		cherryAjaxRequest({
			url: url,
			param: $param ,
			callback: callback
		});
    },
    "deleteMsg":function(){
    	$('#errorMessage').empty();
    	$('#errorDiv #errorSpan').html("");
    	$('#successDiv #successSpan').html("");
		$('#errorDiv').hide();
		$('#successDiv').hide();
    },
    "setSaveBtn":function(){//设置保存按钮
    	var flagCountId = $('#flagCountId').val();
    	var flagTimeId = $('#flagTimeId').val();
    	var flagFromTimeId = $('#flagFromTimeId').val();
    	var $btns = $(".ui-dialog-buttonset").find('button');
    	if(flagTimeId == '1' || flagCountId == '1' ||  flagFromTimeId == '1'){
    		$btns.eq(0).show();
    	}else{
    		$btns.eq(0).hide();
    	}
    },
    //批量修改领用柜台，领用时间弹出框
    "editInit":function(url,conUrl){
    	$("#successDiv").hide();
		var _this = this;
		var dialogSetting = {
			dialogInit: "#confirmInitDIV",
			width: 	660,
			height: 480,
			zIndex: 9999,  
			resizable : false,
			title: 	$("#editConfirmTitle").text(),
			confirm: $("#dialogOk").text(),
			cancel: $("#dialogClose").text(),
			confirmEvent: function(){
				if(!$('#editForm').valid()) {
					return false;
				}
		    	var $ul = $("ul.sortable:visible");
				// 当前操作的活动数组
				var $lis = $ul.children(":visible");
		    	if($lis.length== 0){//单据内容不能为空
		    		//隐藏提示信息
					$("#errorEditDiv").hide();
		    		$("#errorSpan").html($("#message_Error16").text());
					$('#errorDiv').show();
					return;
		    	}
		    	//单据数量
				var $billNum=$("#billNum").val();
				if($billNum>0){
					$("#dataTable_processing_2").show();
					//批量执行单据修改
					BINOLCPACT06.batchUpdate(url);
				}else{
					//隐藏提示信息
					$("#errorEditDiv").hide();
					$("#errorSpan").html($("#message_Error15").text());
					$('#errorDiv').show();
				}
			},
			cancelEvent: function(){removeDialog("#confirmInitDIV");}
		};
		openDialog(dialogSetting);
		var $mainForm = $("#mainForm");
		var html = '';
		var callback = function(msg) {
			//取得活动明细信息
			$("#confirmInitDIV").html(msg);
			//绑定领用柜台
			counterBinding({
				elementId:"batchCounter",
				showNum:10,
				selected :"code"
			});
			//验证表单数据
			cherryValidate({			
				formId: "editForm",		
				rules: {
					batchFromTime: {dateValid: true},
					referFromValue:{digits: true},
					referToValue:{digits: true},
					batchToTime: {dateValid: true}
				}		
			});
			$mainForm.find(".box-content").find("p.CONDISOIN").each(function(){
				var $this = $(this);
				var $inputs = $this.find(":input").not('button');
				var showConFlag = false;
				$inputs.each(function(){
					var $input = $(this);
					if(!$input.is('select')){
						if(!isEmpty($input.val())){
							showConFlag = true;
							return;
						}
					}else{
						showConFlag = true;
					}
				});
				if(showConFlag){
					var keyName = $.trim($this.find('label').text());
					html+='<div class="clearfix">';
					html+='<div class="left" style="width:25%;"><span class="ui-icon icon-arrow-crm"></span>';
					html+=keyName;
					html+='</div>';
					html+='<p style="margin:0; width:75%;" class="left"><span>';
					$inputs.each(function(i){
						var $input = $(this);
						if($input.is('select')){
							var $selectedVal= $input.find("option:selected").text();
							html += $selectedVal;
						}else{
							if(i != 0){html += '&nbsp;~&nbsp;'}
							html += $.trim($input.val());
						}
					});
					html += '</span></p></div>';
				}
			});
			$("#batchConditDiv").html(html);
			$("#dataTable_processing_2").addClass("hide");
		};
		//条件url
		conUrl += "?" + $("#mainForm").serialize();
		cherryAjaxRequest( {
			url:conUrl,
			callback : callback
		});
	},
	//批量更新单据处理
    "batchUpdate": function(url) {
		var callback = function(msg) {
			$("#dataTable_processing_2").hide();
			var msgJson = window.JSON.parse(msg);
			var  $errorDiv =$('#errorDiv #errorSpan');
			var  $successDiv =$('#successDiv #successSpan');
			//隐藏提示信息
			$("#errorEditDiv").hide();
			if(msgJson.error==1){
				//成功显示操作成功
				$("#successDiv").show();
				$('#errorDiv').hide();
				$("#successSpan").html($("#message_Error2").text());
				//成功后隐藏保存按钮
				$(".ui-dialog-buttonset").find('button').eq(0).hide();
				//成功后刷新父页面
				BINOLCPACT06.search();
			}else if(msgJson.error==2){
				$("#errorSpan").html($("#message_Error3").text());
				$('#errorDiv').show();
			}else if(msgJson.error==3){
				$("#errorSpan").html($("#message_Error13").text());
				$('#errorDiv').show();
			}else if(msgJson.error==4){
				$("#errorSpan").html($("#message_Error4").text());
				$('#errorDiv').show();
			}else if(msgJson.error==5){
				$("#errorSpan").html($("#message_Error5").text());
				$('#errorDiv').show();
			}else if(msgJson.error==6){
				$("#errorSpan").html($("#message_Error10").text());
				$('#errorDiv').show();
			}else if(msgJson.error==7){
				$("#errorSpan").html($("#message_Error14").text());
				$('#errorDiv').show();
			}
		};
		var $ul = $("ul.sortable:visible");
		var $inputs= $ul.children(":visible").find(':input').serialize();
		var $param= $inputs +'&'+$('#mainForm').serialize()+'&csrftoken='+parentTokenVal();
		cherryAjaxRequest({
			url: url,
			param: $param ,
			callback: callback
		});
    },
    //添加修改活动内容
	"openCampDialog":function(thisObj){
		// 当前操作的活动内容
		var $ul = $("ul.sortable:visible");
		// 当前可视的活动
		var $allLis = $ul.children();
		// 当前操作的活动数组
		var $lis = $ul.children(":visible");
		var $inputs = $('#PopTime_dataTable').find(':input');
		// 初始化 PopTimeDialog
		$inputs.prop("checked",false);
		$inputs.each(function(){
			var $input = $(this);
			$lis.each(function(){
				var $li = $(this);
				if($input.val() == $li.prop("id")) {
					$input.prop("checked",true);
					return;
				}
			});
		});
		var dialogSetting = {
			bgiframe: true,
			width:300, 
			height:200,
			zIndex: 90,
			modal: true, 
			title: $("#PopTimeTitle").text(),
			close: function(event, ui) {
				$('#PopTimeDialog').dialog("destroy");
			},
			buttons: [{
				text: $("#global_page_ok").text(),
				click: function() {
					$inputs.each(function(){
						var $input = $(this);
						var $li= $allLis.filter("#"+ $input.val());
						if($input.is(":checked")){
							$li.show();
						}else{
							var $siblings = $lis.find('div');
							// 删除验证错误提示信息
							var $error = $siblings.find('.error');
							$error.removeClass('error');
							$error.find('#errorText').remove();
							$li.hide();
							$li.find('.date').val("");
						}
					});
					$(this).dialog("close");
				}
			}]
		};
		$('#PopTimeDialog').dialog(dialogSetting);
	},
	//移除活动内容
	"removeButton":function (_this){
		var $this = $(_this);
		var $thisItem = $(_this).parents("li");
		var $items = $thisItem.parent().find("li");
		$thisItem.hide();
		$thisItem.find('.date').val("");
		var $siblings = $this.siblings('span').find('div');
		// 删除验证错误提示信息
		var $error = $siblings.find('.error');
		$error.removeClass('error');
		$error.find('#errorText').remove();
		$siblings.find(':input').val('');
	},
	// 改变参考类型
	"changeReferType":function(_this,key){
		var $this = $(_this);
		var thisVal = $(_this).val();
		var $siblings = $this.siblings('div');
		$siblings.hide();
		// 删除验证错误提示信息
		var $error = $siblings.find('.error');
		$error.removeClass('error');
		$error.find('#errorText').remove();
		$siblings.find(':input').val('');
		var idPex = '#referType_' + key + '_';
		if(thisVal == '0'){
			$(idPex + '0').show();
		}else{
			$(idPex + '1').show();
		}
	}
};
var BINOLCPACT06 =  new BINOLCPACT06();
$(document).ready(function() {
	counterBinding({
		elementId:"counterGot",
		showNum:20,
		selected :"code"
	});
	counterBinding({
		elementId:"counterOrder",
		showNum:20,
		selected :"code"
	});
	counterBinding({
		elementId:"counterBelong",
		showNum:20,
		selected :"code"
	});
	cherryValidate({			
		formId: "mainForm",		
		rules: {
			startDate: {dateValid:true},	// 开始日期
			endDate: {dateValid:true}	// 结束日期
		}		
	});
});
