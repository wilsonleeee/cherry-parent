var BINOLMOMAN01 = function () {
    
};

BINOLMOMAN01.prototype = {
	/*
	 * 勾选或取消勾选 ，全选是否勾选
	 */
	"checkSelect": function(obj){
//		if($(obj).prop("id") == 'validFlag1') {
//			// 始终处于不勾选状态
//			$(obj).prop("checked",false);
//		}
		if($(obj).prop("checked")) {
	        if($("#dataTable_Cloned input@[id=validFlag]").length == $("#dataTable_Cloned input@[id=validFlag][checked]").length) {
	            $("input@[id=allSelect]").prop("checked",true);
	        }
            this.validCheckBox();
        } else {
            $("input@[id=allSelect]").prop("checked",false);
			var errorText = $.trim($("#errorSpan2").text());
			if(errorText != ""){
	            if(errorText == $.trim($("#errmsg4").val())){//启用
	                this.validEnable("enable");
	            }else if(errorText == $.trim($("#errmsg2").val())){//停用
	                this.validEnable("disable");
	            }else if(errorText == $.trim($("#errmsg3").val())){//解除绑定
	                this.validEnable("unbind");
	            }
			}
        }
		//this.setDom();
	},

	/*
	 * 全选
	 */
    "checkSelectAll": function(obj){
		$('#dataTable_wrapper #dataTable_Cloned').find("#validFlag:checkbox").prop("checked", $(obj).prop("checked"));
		if ($(obj).prop("checked") && $("input@[id=validFlag][checked]").length>0){
			this.validCheckBox();
		}else{
			$('#errorMessage').empty();
		}
		//this.setDom();
	},
	/*
	 * 用户查询
	 */
	"search": function(){
		if (!$('#mainForm').valid()) {
			return false;
		};
		$("#mainForm").find(":input").each(function() {
			$(this).val($.trim(this.value));
		});
		 var url = $("#searchUrl").attr("href");
		 // 查询参数序列化
		 var params= $("#mainForm").serialize();
		 url = url + "?" + params;
		 // 显示结果一览
		 $("#section").show();
		 // 表格设置
		 var tableSetting = {
				 // datatable 对象索引
				 index : 1,
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 2, "asc" ]],
				 // 表格列属性设置
				 aoColumns : [	{ "sName": "checkbox", "sWidth": "1%", "bSortable": false},
				              	{ "sName": "no","sWidth": "1%","bSortable": false},
								{ "sName": "machineCode","sWidth": "20%"},
								{ "sName": "machineCodeOld","bVisible": false,"sWidth": "15%"},
								{ "sName": "machineType","sWidth": "15%"},
								{ "sName": "counterNameIF","sWidth": "10%"},
								{ "sName": "softWareVersion","sWidth": "10%"},
								{ "sName": "mobileMacAddress","bVisible": false,"sWidth": "10%"},
								{ "sName": "phoneCode","bVisible": false,"sWidth": "25%","sClass":"alignRight"},	
								{ "sName": "employeeName","bVisible": false,"sWidth": "10%"},
								{ "sName": "machineStatus","sWidth": "8%","sClass": "center"},
								{ "sName": "startTime","bVisible": false,"sWidth": "10%"},
								{ "sName": "createTime","sWidth": "10%"},
								{ "sName": "bindStatus","sWidth": "10%" }
								//{ "sName": "operate","sWidth": "10%","bSortable": false,"sClass": "center"}// 11
							],			
								
				// 不可设置显示或隐藏的列	
				aiExclude :[0, 1, 2],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 滚动体的宽度
				sScrollXInner:"",
				// 固定列数
				fixedColumns : 3,
				callbackFun : function (){
			        $('#allSelect').prop("checked", false);
		        }
		 };
		 // 调用获取表格函数
		 getTable(tableSetting);
		 //this.setDom();
	},
	
	/**
	 * 确认画面
	 */
	"confirmInit": function(object,confirmname){
		if(!this.validCheckBox()){
	        return false;
	    };
	    if(!this.validEnable(confirmname)){
	    	return false;
	    }
		var param = "csrftoken=" + getTokenVal() + $(object).parent().find(':input').serialize();
		param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").serialize();
		param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").nextAll("#machineCodeArr").serialize();
		param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").nextAll("#machineCodeOldArr").serialize();
		param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").nextAll("#machineTypeArr").serialize();
		param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").nextAll("#phoneCodeArr").serialize();
		param += "&"+$("#mainForm").find("#brandInfoId").serialize();
		var strText = '';
		var strTitle = '';
		if (confirmname == 'disable') {
			strText="#disableText";
			strTitle="#disableTitle";
		}else if(confirmname == 'enable'){
			strText="#enableText";
			strTitle="#enableTitle";
		}else if (confirmname == 'unbind'){
			strText="#unbindText";
			strTitle="#unbindTitle";
		}else if(confirmname == 'delete'){
			strText="#deleteMachineText";
			strTitle="#deleteMachineTitle";
		}else if(confirmname == 'issue'){
			strText="#issueText";
			strTitle="#issueTitle";
		}else if(confirmname == 'scrap'){
			strText="#scrapText";
			strTitle="#scrapTitle";
		}
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text:  $(strText).html(),
			width: 	500,
			height: 300,
			title: 	$(strTitle).text(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLMOMAN01.confirmProcess($(object).attr("href"), param);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},

	"confirmProcess":function (url,param) {
		var callback = function(msg) {
//            if (msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
//            }else {
//                $("#dialogInit").html(msg);
//	            // 保存成功的场合
//	            if($("#dialogInit").find("#successDiv").length != 0) {
//					$("#dialogInit").dialog("destroy");
//	                var dialogSetting = {
//						dialogInit: "#dialogInit",
//						text: msg,
//						width: 500,
//						height: 300,
//						title: $("#save_success_message").text(),
//						confirm: $("#dialogClose").text(),
//						confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();},
//                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
//					}
//					openDialog(dialogSetting);
//	            } 
//	            // 保存失败的场合
//	            else if($("#dialogInit").find("#errorMessageDiv").length != 0){
//	                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
//	                $("#dialogInit").dialog( "option", {
//	                    buttons: [{
//	                        text: $("#dialogClose").text(),
//	                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
//	                    }],
//						closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
//	                });
//	            } 
//            }
            
            removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback,
			formId: '#mainForm'
		});
	},
	
	/*
	 * 验证勾选，显示隐藏错误提示
	 */
	"validCheckBox": function(){
		var flag = true;
		var checksize = $("input@[id=validFlag][checked]").length;
		if (checksize == 0){
			$('#actionResultDisplay').empty();
		    //没有勾选
	        $('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
	        $('#errorMessage').html($("#errorDiv2").html());
	        flag = false;
		}else{
			$('#actionResultDisplay').empty();
			$('#errorMessage').empty();
		}
		return flag;
	},
	
	"validEnable": function(btnname){
		$('#actionResultDisplay').empty();
		$('#errorMessage').empty();
		var checkedArray = [];
		var msArray = [];
		var bsArray = [];
		var flag = true;
		var flag_Global = true;
		var flag1 = true;
		var flag2 = true;
		$("#dataTable_Cloned tbody tr").each(function(i){
			var dom1 =$("#dataTable_Cloned tbody tr:eq("+i+")").find(":checkbox[checked]");
			if(dom1.length>0){
				checkedArray.push(i);
				var objRow = $("#dataTable_Cloned tbody tr:eq("+i+")");
				// 机器状态
				var ms_value = objRow.find(":hidden[name='machineinfo_ms']").val();
				// 机器绑定状态
				var bs_value = objRow.find(":hidden[name='machineinfo_bs']").val();
				msArray.push(ms_value);
				bsArray.push(bs_value);
			}
		});
		/** -----------已报废的机器只允许查询操作START----------*/
		for(i=0;i<msArray.length;i++){
			// 查找已报废的机器
			if(msArray[i] == 3) {
				flag_Global = false;
				break;
			}
		}
		if(!flag_Global) {
			flag = false;
			$('#errorDiv2 #errorSpan2').html($('#errmsg7').val());
            $('#errorMessage').html($("#errorDiv2").html());
            return flag;
		}
		/** -----------已报废的机器只允许查询操作END----------*/
		if(btnname=="disable"){
			for(i=0;i<msArray.length;i++){
				//只有已启用的机器才能被停用
				if(msArray[i]!=1){
					flag1 = false;
					break;
				}
			}
			if (!flag1){
				flag = false;
				// 您选择的机器中含有已报废的机器，不能执行该操作，请重新选择！
				$('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
                $('#errorMessage').html($("#errorDiv2").html());
			}
		}else if(btnname=="enable"){
			for(i=0;i<msArray.length;i++){
				//只有已停用的机器才能被启用
				if(msArray[i]!=0){
					flag1 = false;
					break;
				}
			}
			if (!flag1){
				flag = false;
				$('#errorDiv2 #errorSpan2').html($('#errmsg4').val());
                $('#errorMessage').html($("#errorDiv2").html());
			}
		}else if(btnname=="unbind"){
			for(i=0;i<bsArray.length;i++){
				// 只有有绑定柜台的机器才可解除绑定
				if(bsArray[i]==0){
					flag2 = false;
					break;
				}
			}
			if (!flag2){
				flag = false;
				$('#errorDiv2 #errorSpan2').html($('#errmsg3').val());
                $('#errorMessage').html($("#errorDiv2").html());
			}
		} else {
			for(i = 0 ; i < msArray.length ; i++){
				// MachineStatus状态值 1：已启用； 0：已停用 ；2：未下发 
				if(msArray[i] != 2){
					flag2 = false;
					break;
				}
			}
			if(!flag2){
				// flag = false;
				// 只有未下发的机器才可执行下发
				if(btnname == "issue"){
					flag = false;
					$('#errorDiv2 #errorSpan2').html($('#errmsg6').val());
					$('#errorMessage').html($("#errorDiv2").html());
				}
				//$('#errorDiv2 #errorSpan2').html(btnname == "delete" ? $('#errmsg5').val() : $('#errmsg6').val());
                //$('#errorMessage').html($("#errorDiv2").html());
			}
		}
		return flag;
	},
	
	/*
	 * 关闭Dialog
	 */
	"dialogClose": function(){
		this.doClose('#dialogInit');
		if (binOLMOMAN01_global.refresh) {
			this.search();
			binOLMOMAN01_global.refresh = false;
		}
	},

	/*
	 * 关闭画面
	 */
	"doClose": function(id) {
		$(id).dialog("destroy"); 
		$(id).remove();
	},
	
	// 删除机器确认画面【已废除不用】
	"deleteMachineInit":function(object) {
		var machineCodeOld = $(object).parent().parent().find('#machineCodeOldArr').val();
		
		var param = "&"+$(object).parent().parent().find('#machineCodeArr').serialize()+"&"+$(object).parent().parent().find('#machineCodeOldArr').serialize();
		
		var dialogSetting = {
			dialogInit: "#dialogInit",
			text: $("#deleteMachineText").html(),
			width: 	500,
			height: 300,
			title: 	$("#deleteMachineTitle").text()+':'+machineCodeOld,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){BINOLMOMAN01.deleteMachine($(object).attr("href"), param);},
			cancelEvent: function(){removeDialog("#dialogInit");}
		};
		openDialog(dialogSetting);
	},
	
	// 删除机器处理
	"deleteMachine":function(url,param) {
		var callback = function(msg) {
			$("#dialogInit").html(msg);
			if($("#errorMessageDiv").length > 0) {
				$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
				$("#dialogInit").dialog( "option", {
					buttons: [{
						text: $("#dialogClose").text(),
					    click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
					}]
				});
			} else {
				removeDialog("#dialogInit");
				if(oTableArr[1] != null)oTableArr[1].fnDraw();
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
	
//	// 判断DOM控件的是否可用，可用返回true，反之false
//	"isEnable": function(dom){
//		var className = $(dom).attr("class");
//		var index = className.indexOf("ui-state-disabled");
//		if(index != -1){
//			return false;
//		}else{
//			return true;
//		}
//	}
	
//	//启用、禁用按钮 可用性设置
//	"setDom": function(){
//		// 按钮不可以样式名
//		var disClass = "ui-state-disabled";
//		var checkedArray = [];
//		var msArray = [];
//		var bsArray = [];
//		var flag1 = true;
//		var flag2 = true;
//		$("#dataTable_Cloned tbody tr").each(function(i){
//			var dom1 =$("#dataTable_Cloned tbody tr:eq("+i+")").find(":checkbox[checked]");
//			if(dom1.length>0){
//				checkedArray.push(i);
//				var objRow = $("#dataTable_Cloned tbody tr:eq("+i+")");
//				var ms_value = objRow.find(":hidden[name='machineinfo_ms']").val();
//				var bs_value = objRow.find(":hidden[name='machineinfo_bs']").val();
//				msArray.push(ms_value);
//				bsArray.push(bs_value);
//			}
//		});
//		for(i=0;i<msArray.length;i++){
//			if(msArray[i]==2){
//				$("#disableBtn").addClass(disClass);
//				flag1 = false;
//				break;
//			}
//		}
//		for(i=0;i<bsArray.length;i++){
//			if(bsArray[i]==0){
//				$("#unbindBtn").addClass(disClass);
//				flag2 = false;
//				break;
//			}
//		}
//		if (flag1){
//			$("#disableBtn").removeClass(disClass);
//		}
//      if (flag2){
//        	$("#unbindBtn").removeClass(disClass);
//		}
//        if (checkedArray.length == 0) {
//        	$("#disableBtn").addClass(disClass);
//        	$("#unbindBtn").addClass(disClass);
//        }
//	}
};

var BINOLMOMAN01 = new BINOLMOMAN01();

/*
 * 全局变量定义
 */
var binOLMOMAN01_global = {};

// 刷新区分
binOLMOMAN01_global.refresh = false;

$(function(){
//	// 表格列选中
//    $('thead th').live('click',function(){
//        $("th.sorting").removeClass('sorting');
//        $(this).addClass('sorting');
//    });
	
	counterBinding({elementId:"counterCodeName",showNum:20,selected:"name"});
	
	BINOLMOMAN01.search();
});