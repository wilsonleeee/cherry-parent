/**
 * @author LuoHong
 */
/**以页面为单位的js函数采用原型的方法声明，调用的时候采用"画面名.方法名"*/
var BINOLSTJCS09=function(){};

BINOLSTJCS09.prototype = {
		"search":function(){
			oTableArr[0] == null;
			var $form = $('#mainForm');
			if (!$form.valid()) {
				return false;
			};
			var url = $("#searchUrl").attr("href");
			 // 查询参数序列化
			var param= $form.serialize();
			 url = url + "?" + param;

			 // 显示结果一览
			 $("#section").show();
			// 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 排序列
					 aaSorting : [[2, "desc"]],
					 // 表格列属性设置
					 aoColumns : [	
					              	//{ "sName": "checkbox", "sWidth": "3%","bSortable": false}, 		// 0
			                    	{ "sName": "no", "sWidth": "3%","bSortable": false,"sClass":"center"},			// 1
			                    	{ "sName": "logicType", "sWidth": "10%"},// 2
			                    	{ "sName": "productType", "sWidth": "5%","sClass":"center"},// 3
			                    	{ "sName": "businessType", "sWidth": "8%","sClass":"center"},	// 4
			                    	{ "sName": "subType", "sWidth": "5%","sClass":"center"},	// 5
			                    	{ "sName": "logicInventoryCode", "sWidth": "20%"},// 6
			                    	//{ "sName": "type", "sWidth": "5%","sClass":"center"},	// 7
									{ "sName": "configOrder", "sWidth": "5%","sClass": "center","bSortable": false,"sClass":"center"}, //8
									{ "sName": "comments", "sWidth": "20%","bSortable": false,"bVisible": true},//9
									//{ "sName": "validFlag", "sWidth": "3%","bSortable": false,"sClass":"center"},//10
									{ "sName": "logicDepotId", "sWidth": "3%","bSortable": false,"sClass":"center"}],// 11
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 不可设置显示或隐藏的列	
					aiExclude :[0,1,2],
					// 固定列数
					fixedColumns : 3,
					index : 0
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
			},
			
			
           //弹出新增按钮对话框			
			"popAddDialog" : function() {
				$("#actionResultDisplay").empty();
				var $form = $('#mainForm');
				if (!$form.valid()) {
					return false;
				};
				$("#errorMessage").html("");
				$("#errorDiv2 #errorSpan2").html("");
				$("#errorDiv2").hide();
				var param= $form.serialize();
				var _this = this;
				var url = $("#add_depot").attr("href");
				var callback = function(msg) {
					var dialogSetting = {
							dialogInit: "#dialogInit",
							bgiframe: true,
							width:  500,
							height: 330,
							text: msg,
							title: 	$("#addMsgTitle").text(),
							confirm: $("#dialogConfirm").text(),
							cancel: $("#dialogCancel").text(),
							confirmEvent: function(){_this.addMsgDialog();},
							cancelEvent: function(){removeDialog("#dialogInit");}
						};
						openDialog(dialogSetting);
						_this.getLogiDepotByAjax("addForm");
				};
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: callback
				});
				
				
			},
			"addMsgDialog":function()
			{  
				if(!$('#addForm').valid()) {
				return false;
					}
				var url = $("#add1_depot").attr("href");
				var param = $('#addForm').find(":input").serialize()+"&brandInfoId="+$("#mainForm #brandInfoId").val();
				var callback = function(msg) {
					if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
					} else {
						$("#dialogInit").html(msg);
						// 保存成功的场合
						if($("#dialogInit").find("#successDiv").length != 0) {
							$("#dialogInit").dialog("destroy");
			                var dialogSetting = {
								dialogInit: "#dialogInit",
								text: msg,
								width: 450,
								height: 330,
								title: $("#saveSuccessTitle").text(),
								confirm: $("#dialogClose").text(),
								confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();},
		                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
							};
							openDialog(dialogSetting);
						}// 保存失败的场合
						else if($("#dialogInit").find("#errorMessageDiv").length != 0){
			                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			                $("#dialogInit").dialog( "option", {
			                    buttons: [{
			                        text: $("#dialogClose").text(),
			                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
			                    }],
								closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();}
			                });
						}
					}
				};
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: callback,
					isDialog: true,
					resultId: "#actionResultDisplay2",
					formId: '#mainForm'
				});
				
				
			},
			 //弹出编辑按钮对话框			
			"popEditDialog" : function(obj) {
				$("#actionResultDisplay").empty();
				var $editObj = $(obj);
				var _this = this;
				var $form = $('#mainForm');
				var url = $("#edit_depot").attr("href");
				var logicDepotId = $editObj.prop("id");
		        url = url + '?logicDepotId=' + logicDepotId + '&' + $form.serialize();
				var callback = function(msg) {
					var dialogSetting = {
							dialogInit: "#dialogInit",
							bgiframe: true,
							width: 	500,
							height: 340,
							text: msg,
							title: 	$("#updateMsgTitle").text(),
							confirm: $("#dialogConfirm").text(),
							cancel: $("#dialogCancel").text(),
							confirmEvent: function(){_this.savaEditDialog();},
							cancelEvent: function(){removeDialog("#dialogInit");}
						};
						
						//弹出框体
						openDialog(dialogSetting);
						
						//_this.getLogiDepotByAjax("main_form");
				};
				cherryAjaxRequest({
					url: url,
					callback: callback
				});
			},
			"savaEditDialog":function()
			{ 
				  if(!$('#main_form').valid()) {
						return false;
					}
				var url = $("#edit1_depot").attr("href");
				var param = $("#main_form").find(":input").serialize()+"&brandInfoId="+$("#mainForm #brandInfoId").val();
				var callback = function(msg) {
					if(msg.indexOf('id="actionResultDiv"') != -1 || msg.indexOf('id="fieldErrorDiv"') != -1) {
					} else {
						$("#dialogInit").html(msg);
						// 保存成功的场合
						if($("#dialogInit").find("#successDiv").length != 0) {
							$("#dialogInit").dialog("destroy");
			                var dialogSetting = {
								dialogInit: "#dialogInit",
								text: msg,
								width: 450,
								height: 330,
								title: $("#saveSuccessTitle").text(),
								confirm: $("#dialogClose").text(),
								confirmEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();},
		                        closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
							};
							openDialog(dialogSetting);
						}// 保存失败的场合
						else if($("#dialogInit").find("#errorMessageDiv").length != 0){
			                $("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			                $("#dialogInit").dialog( "option", {
			                    buttons: [{
			                        text: $("#dialogClose").text(),
			                        click: function(){removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw(); }
			                    }],
								closeEvent: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
			                });
						}
					}
				};
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: callback,
					isDialog: true,
					resultId: "#actionResultDisplay2",
					formId: '#mainForm'
				});
				
				
			},
         //关闭弹出框			
			"closeDialog" : function(dialogParent, dialogDiv) {
				$("#" + dialogDiv).dialog("destroy");
				$("#" + dialogDiv).remove();
				$("#" + dialogParent).html(this.dialogHtml);
			},
		
			/**
			 * 逻辑仓库业务关系删除弹出初始化pop窗口
			 */
			"delMsgInit":function(logicDepotId){
				var _this = this;
				var param = "logicDepotId=" + logicDepotId + "&brandInfoId="+$("#mainForm #brandInfoId").val();
				var dialogSetting = {
					dialogInit: "#dialogInit",
					text:  $("#deleteMsgText").html(),
					width: 	450,
					height: 300,
					title: 	$("#deleteMsgTitle").text(),
					confirm: $("#dialogConfirm").text(),
					cancel: $("#dialogCancel").text(),
					confirmEvent: function(){_this.delMsg(param)},
					cancelEvent: function(){removeDialog("#dialogInit");}
				};
				openDialog(dialogSetting);
			},
			/*
			 * 删除
			 */
			"delMsg":function(param){
				var that = this;
				var url = $("#delete_depot").attr("href");
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: function(msg) {
						// 返回无错误
						if(msg.indexOf('id="actionResultDiv"') > -1){
							that.search();
						}
					}
				});
				that.closeEvent();
			},
			"closeEvent": function(){
				removeDialog("#dialogInit");if(oTableArr[1] != null)oTableArr[1].fnDraw();
			},
			
			// 选择勾选框
			"checkSelect": function(){
				$("#errorMessage").html("");
				$("#errorDiv2 #errorSpan2").html("");
				$("#errorDiv2").hide();
				
				var checkBoxNum = $("#dataTable_Cloned").find("input[type='checkbox']").length;
				var checkedNum = $("#dataTable_Cloned").find("input:checked").length;
				if(checkedNum == checkBoxNum){
					$("#dataTable_Cloned").prev().find("#checkAll").prop("checked","checked");
				}else{
					$("#dataTable_Cloned").prev().find("#checkAll").removeProp("checked");
				}
			},
			
			"CheckAllSelect":function(obj){
				var $this = $(obj);
				$("#errorMessage").html("");
				$("#errorDiv2 #errorSpan2").html("");
				$("#errorDiv2").hide();
				
				$("#dataTable_Cloned").find("input[type='checkbox']").prop("checked",$this.prop("checked"));
			},
			
			//验证勾选框			
			"validCheckbox": function(){
				var flag = true;
				var checksize = $("input@[name='logicDepotId'][checked]").length;
				if (checksize == 0){
				    //没有勾选
			        $('#errorDiv2 #errorSpan2').html($('#errmsg_EMO00036').val());
			        $('#errorMessage').html($("#errorDiv2").html());
			        flag = false;
				}else{
					$('#errorMessage').empty();
				}
				return flag;
			},
			
			//ajax取得逻辑仓库并显示到页面上
			"getLogiDepotByAjax":function(formId,flag){
				var $form = $("#"+formId);
				//alert($form.find("#logicType").val());
				var _this = this;
				//如果是收货业务则显示子类型和终端+后台逻辑仓库
				if(flag){
					var selectedLogicType = $form.find("#logicType").val();
					var selectedBusinessType = $form.find("#businessType").val();
				}else{
					var selectedLogicType = $form.find("#logicType").find(":selected").val();
					var selectedBusinessType = $form.find("#businessType").find(":selected").val();
				}
				
				// 根据业务所属显示相应业务类型（后台、终端）
				if(selectedLogicType == "0") {
					// 后台业务类型
					
					if($form.find("#businessType_tr_1").html() != null){
						$form.find("#businessType_tr_1").remove();
					}
					if($form.find("#businessType_tr_0").html() == null){
						var businessTypeHtml = $("#hide_table").find("#businessType_tr_0").html();
						$form.find("#productType_tr").after("<tr id='businessType_tr_0'>"+businessTypeHtml+"</tr>");
					}
					
					// 不是终端业务且不是终端入库业务业务，则不显示
					if($form.find("#subType_tr").html()!=null){
						$form.find("#subType_tr").remove();
					}
					
				} else {
					// 终端业务类型
					
					if($form.find("#businessType_tr_0").html() != null){
						$form.find("#businessType_tr_0").remove();
					}
					if($form.find("#businessType_tr_1").html() == null){
						var businessTypeHtml = $("#hide_table").find("#businessType_tr_1").html();
						$form.find("#productType_tr").after("<tr id='businessType_tr_1'>"+businessTypeHtml+"</tr>");
					}
				}
				
				var url = $("#getLogiDepotByAjax").prop("href");
				var param = $form.serialize()+"&brandInfoId="+$("#mainForm #brandInfoId").val();
				//alert(param);
				var callback = function(msg){
					var logicDepots = window.JSON2.parse(msg);
					//alert(logicDepots);
					$form.find("#logicInvId").html("");
					var optionHtml = "";
					if(logicDepots.length > 0){
						for(var i = 0 ; i < logicDepots.length ; i++){
							optionHtml = optionHtml + "<option value='" + logicDepots[i].BIN_LogicInventoryInfoID + "'>" + logicDepots[i].InventoryCodeName.toString().escapeHTML() + "</option>";
						}
						$form.find("#logicInvId").html(optionHtml);
					}
					optionHtml = null;
				};
				
				cherryAjaxRequest({
					url: url,
					param:param,
					callback: callback
				});
			},
			// 查询条件--根据业务所属业务类型显示
			"getBusinessType":function(formId){
				// 查询条件 --当前Form
				var $form = $("#"+formId);
				// 查询条件 --当前业务所属
				var selLogicType = $form.find("#logicType").find(":selected").val();
				if(selLogicType == "0") {
					if($form.find("#businessType_1").html() != null){
						$form.find("#businessType_1").remove();
					}
					if($form.find("#businessType_0").html() == null){
						var businessTypeHtml = $("#hide_div_sel").find("#businessType_0").html();
						$form.find("#logicTypeId").after("<div id='businessType_0'>"+businessTypeHtml+"</div>");
					}
					
					// 加布局占位符
					if($form.find("#layout_empty").html() == null){
						var layout_emptyHtml = $("#hide_div_sel").find("#layout_empty").html();
						$form.find("#inventoryNameId").after("<div id='layout_empty'>"+layout_emptyHtml+"</div>");
					}
				} 
				else if(selLogicType == "1"){
					if($form.find("#businessType_0").html() != null){
						$form.find("#businessType_0").remove();
					}
					if($form.find("#businessType_1").html() == null){
						var businessTypeHtml = $("#hide_div_sel").find("#businessType_1").html();
						$form.find("#logicTypeId").after("<div id='businessType_1'>"+businessTypeHtml+"</div>");
					}
					
					// 加布局占位符
					if($form.find("#layout_empty").html() == null){
						var layout_emptyHtml = $("#hide_div_sel").find("#layout_empty").html();
						$form.find("#inventoryNameId").after("<div id='layout_empty'>"+layout_emptyHtml+"</div>");
					}
				}
				else {
					if($form.find("#businessType_0").html() != null){
						$form.find("#businessType_0").remove();
					}
					if($form.find("#businessType_1").html() != null){
						$form.find("#businessType_1").remove();
					}
					// 去除布局占位符
					if($form.find("#layout_empty").html() != null){
						$form.find("#layout_empty").remove();
					}
				}
			},
			// 取得业务类型子类型--目前只有终端入库具备子类型2012-10-09
			"getSubType":function(formId){
				// 当前Form
				var $form = $("#"+formId);
				// 当前业务所属
				var selLogicType = $form.find("#logicType").find(":selected").val();
				if(selLogicType == "1"){ // 目前只终端的入库具备子业务
					var selBusinessType = $form.find("#businessType").find(":selected").val();
					// 终端订货（OD）类型有子类型（subType）
					if(selBusinessType == "OD") {
						if($form.find("#subType_tr").html()==null){
							var subTypeHtml = $("#hide_table").find("#subType_tr").html();
							$form.find("#businessType_tr_1").after("<tr id='subType_tr'>"+subTypeHtml+"</tr>");
							subTypeHtml = null;
						}
					} else {
						// 不是终端业务且不是终端入库业务业务，则不显示
						if($form.find("#subType_tr").html()!=null){
							$form.find("#subType_tr").remove();
						}
					}
				}
			},
			// 下发
			"issued":function(url){
				var that = this;
				//if(that.disabOptBtn()==false)return;
				var param = $("#brandInfoId").serialize();
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: function(msg) {
						// 返回无错误
						if(msg.indexOf('id="actionResultDiv"') > -1){
							that.search();
						}
					}
				});
			}
};
var BINOLSTJCS09 = new BINOLSTJCS09();

//画面初始状态加载
$(document).ready(function() {
	BINOLSTJCS09.search();
	logicInventoryBinding({
		elementId:"inventoryName",
		showNum:20,
		selected:"name"
	});
	$("#searchBut").bind("click",function(){
		$("#actionResultDisplay").empty();
	});
});
