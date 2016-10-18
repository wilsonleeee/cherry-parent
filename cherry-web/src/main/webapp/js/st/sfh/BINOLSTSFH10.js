function BINOLSTSFH10(){};

BINOLSTSFH10.prototype={
		
		"dialogHtml":"",
		
		"productTree" : null,
		
		"counterTree" : null,

		/**
		 * 全局参数设定
		 */
		"globalSearch" : function(flag) {
			if (flag == undefined) {
				$("#actionResultDisplay").html("");
	
			}
			var url = $("#globalSearch").html();
			var $form = $("#mainForm");
			// 表单验证
			if (!$form.valid()) {
				return;
			}
			var params = "brandInfoId=" + $("#brandInfoId4 option:selected").val()
					+ "&csrftoken=" + $("#csrftoken").val();
			url = url + "?" + params;
			// 表格设置
			var tableSetting4 = {
				// 表格ID
				tableId : '#dataTable4',
				// 数据URL
				url : url,
				// 表格列属性设置
				// 表格默认排序
				aaSorting : [ [ 1, "asc" ] ],
				aoColumns : [ {"sName" : "no.","bSortable" : false}, // 0
				              {"sName" : "brandName"}, // 1
				              {"sName" : "saleDaysOfMonth"}, // 2
				              {"sName" : "daysOfProduct"}, // 3
				              {"sName" : "globalParameterId","bSortable" : false} ],// 4
				
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index : 4
			};
			// 调用获取表格函数
			getTable(tableSetting4);
		},
		
		"proSearch":function(flag){
			if(flag == undefined){
				$("#actionResultDisplay").html("");
				
			}
			var url = $("#proSearch").html();
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}	 
			var params= "brandInfoId="+$("#brandInfoId1 option:selected").val()+"&"+$("#productName1").serialize()+"&date="+$("#date1").val()+"&csrftoken="+$("#csrftoken").val();
//			var params =  $form.serialize()+"&csrftoken="+$("#csrftoken").val();
			 url = url + "?" + params;
			 // 表格设置
			 var tableSetting1 = {
					 // 表格ID
					 tableId : '#dataTable1',
					 // 数据URL
					 url : url,
					 // 表格列属性设置
					// 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 aoColumns : [
					              	{ "sName": "no.","sWidth": "5%","bSortable":false}, 			 // 0
									{ "sName": "productName","sWidth": "25%"},					     // 1
									{ "sName": "year","sWidth": "15%","bSortable":false},	                     // 2
									{ "sName": "adtCoefficient","sWidth": "15%"},                       // 3
									{ "sName": "productOrderParameterId","sWidth": "5%","bSortable":false}],					     // 4
			
					// 不可设置显示或隐藏的列	
					//aiExclude :[0,1,2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					index:1
			 };
			 // 调用获取表格函数
			 getTable(tableSetting1);
	},
		"couSearch":function(flag){
			if(flag == undefined){
				$("#actionResultDisplay").html("");
				
			}
			var url = $("#couSearch").html();
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}	 
			 var params= "brandInfoId="+$("#brandInfoId2 option:selected").val()+"&departCode="+$("#departCode1").val()+"&csrftoken="+$("#csrftoken").val();
			 url = url + "?" + params;
			 // 表格设置
			 var tableSetting2 = {
					 // 表格ID
					 tableId : '#dataTable2',
					 // 数据URL
					 url : url,
					 // 表格列属性设置
					// 表格默认排序
					 aaSorting : [[ 1, "asc" ]],
					 aoColumns : [
					              	{ "sName": "no.","sWidth": "5%","bSortable":false}, 			 // 0
									{ "sName": "departCode","sWidth": "25%"},					     // 1
									{ "sName": "orderDays","sWidth": "15%"},	                     // 2
									{ "sName": "intransitDays","sWidth": "15%"},                       // 3
									{ "sName": "counterOrderParameterId","sWidth": "5%","bSortable":false}],					     // 4
			
					// 不可设置显示或隐藏的列	
					//aiExclude :[0,1,2],
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					index:2
			 };
			 // 调用获取表格函数
			 getTable(tableSetting2);
		},
			"couPrtSearch":function(flag){
				if(flag == undefined){
					$("#actionResultDisplay").html("");
					
				}
				var url = $("#couPrtSearch").html();
				var $form = $("#mainForm");
				// 表单验证
				if(!$form.valid()){
					return;
				}	 
				 var params= "brandInfoId="+$("#brandInfoId3 option:selected").val()+"&departCode="+$("#departCode2").val()+"&"+$("#productName2").serialize()+"&csrftoken="+$("#csrftoken").val();
				 url = url + "?" + params;
				 // 表格设置
				 var tableSetting3 = {
						 // 表格ID
						 tableId : '#dataTable3',
						 // 数据URL
						 url : url,
						 // 表格列属性设置
						// 表格默认排序
						 aaSorting : [[ 1, "asc" ]],
						 aoColumns : [
						              	{ "sName": "no.","bSortable":false}, 			 		// 0
										{ "sName": "departCode"},					     		// 1
										{ "sName": "productName"},	                     		// 2
										{ "sName": "lowestStockDays"},                       	// 3
										{ "sName": "growthFactor"},                       	// 4
										{ "sName": "regulateFactor"},                       	// 5
										{ "sName": "exhibitQuantity"},                       	// 6
										{ "sName": "counterPrtOrParameterId","bSortable":false}],// 7
				
						// 不可设置显示或隐藏的列	
						//aiExclude :[0,1,2],
						// 横向滚动条出现的临界宽度
						sScrollX : "100%",
						index:3
				 };
				 // 调用获取表格函数
				 getTable(tableSetting3);
			},
			
			//弹出编辑弹出框
			"popEditDialog":function(flag,obj){
				var that = this;
				var dialogSetting = {
						dialogInit: "#editDialog",
						bgiframe: true,
						width: 550, 
						height: 250,
						zIndex: 9999,
						modal: true, 
						resizable: false,
						confirm: $("#yes").html(),
						cancel: $("#cancel").html(),
						cancelEvent:function(){removeDialog("#editDialog");}
				};
				//编辑产品订货参数
				if(flag == "prt"){
					dialogSetting.title = $("#mulProduct").text();
					//确认事件
					dialogSetting.confirmEvent = function(){that.editPrtParam(obj);};
					openDialog(dialogSetting);
					//取得html
					var html = that.getEditPrtHtml();
					$(dialogSetting.dialogInit).html(html);
					var objId = obj.id;
					var product = $(obj).parent().parent().parent().children("td").eq(1).find("span").html().escapeHTML();
					$("#editTable #date").html(objId.split("_")[1]);
					$("#editTable #adtCoefficient").val(objId.split("_")[2]);
					$("#editTable #product").html(product);
					$("#editTable #adtCoefficient").focus();
					html = null;
				}//编辑柜台订货参数
				else if(flag == "cut"){
					dialogSetting.title = $("#mulCounter").text();
					//确认事件
					dialogSetting.confirmEvent = function(){that.editCouParam(obj);};
					openDialog(dialogSetting);
					//取得html
					var html = that.getEditCutHtml();
					$(dialogSetting.dialogInit).html(html);
					var idArr = $(obj).attr("id").split("_");
					var counter = $(obj).parent().parent().parent().children("td").eq(1).find("span").html().escapeHTML();
					$("#editTable #counter").html(counter);
					$("#editTable #orderDays").val(idArr[1]);
					$("#editTable #intransitDays").val(idArr[2]);
					html = null;
				}//编辑柜台产品订货参数
				else if(flag == "cutPrt"){
					dialogSetting.title = $("#mulCouPrt").text();
					//确认事件
					dialogSetting.confirmEvent = function(){that.editCouPrtParam(obj);};
					openDialog(dialogSetting);
					var html = that.getEditCutPrtHtml();
					$(dialogSetting.dialogInit).html(html);
					var counter = $(obj).parent().parent().parent().children("td").eq(1).find("span").html().escapeHTML();
					var product = $(obj).parent().parent().parent().children("td").eq(2).find("span").html().escapeHTML();
					$("#editTable #product").html(product);
					$("#editTable #counter").html(counter);
					$("#editTable #lowestStockDays").val($(obj).attr("id").split("_")[1]);
					$("#editTable #editGrowthFactor_pop").val($(obj).attr("id").split("_")[2]);
					$("#editTable #editRegulateFactor_pop").val($(obj).attr("id").split("_")[3]);
					$("#editTable #lowestStockDays").focus();
					html = null;
				}// 编辑全局订货参数
				else if(flag == 'global') {
					dialogSetting.title = $("#mulGlobal").text();
					//确认事件
					dialogSetting.confirmEvent = function(){that.editGlobalParam(obj);};
					openDialog(dialogSetting);
					var html = that.getEditGlobalHtml();
					$(dialogSetting.dialogInit).html(html);
					$("#editTable #saleDaysOfMonth").val($(obj).attr("id").split("_")[1]);
					$("#editTable #daysOfProduct").val($(obj).attr("id").split("_")[2]);
					$("#editTable #saleDaysOfMonth").focus();
					html = null;
				}
			},
			
			//取得编辑产品参数HTML
			"getEditPrtHtml":function(){
				var html = [];
				html.push('<div id="errorDiv" class="actionError" style="display:none">');
				html.push('<ul><li><span id="errorSpan"></span></li></ul>');
				html.push('</div>');
				html.push('<table id="editTable" width="100%" class="detail" border="1" cellspacing="0" cellpadding="0">');
				html.push('<tbody>');
				//产品
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_product").html().escapeHTML()+'</span></th>');
				html.push('<td><span id="product"></span></td>');
				html.push('</tr>');
				//年月
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_date").html().escapeHTML()+'</span></th>');
				html.push('<td><span id="date"></span></td>');
				html.push('</tr>');
				//调整系数
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_adtCoefficient").html().escapeHTML()+'<span class="highlight">*</span></span></th>');
				html.push('<td><span><input name="adtCoefficient" maxlength="16" class="text" id="adtCoefficient" title="'+$("#SFH10_adtCoefficientTip").html()+'"></input></span></td>');
				html.push('</tr>');
				html.push('</tbody>');
				html.push('</table>');
				return html.join("");
			},
			
			//取得编辑柜台参数HTML
			"getEditCutHtml":function(){
				var html = [];
				html.push('<div id="errorDiv" class="actionError" style="display:none">');
				html.push('<ul><li><span id="errorSpan"></span></li></ul>');
				html.push('</div>');
				html.push('<table id="editTable" width="100%" class="detail" border="1" cellspacing="0" cellpadding="0">');
				html.push('<tbody>');
				//柜台
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_counter").html().escapeHTML()+'</span></th>');
				html.push('<td><span id="counter"></span></td>');
				html.push('</tr>');
				//订货间隔
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_orderDays").html().escapeHTML()+'<span class="highlight">*</span></span></th>');
				html.push('<td><span><input name="orderDays" maxlength="4" class="text" id="orderDays"></input>'+$("#SFH10_day").html()+'</span></td>');
				html.push('</tr>');
				//在途天数
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_intransitDays").html().escapeHTML()+'<span class="highlight">*</span></span></th>');
				html.push('<td><span><input name="intransitDays" maxlength="4" class="text" id="intransitDays"></input>'+$("#SFH10_day").html()+'</span></td>');
				html.push('</tr>');
				html.push('</tbody>');
				html.push('</table>');
				return html.join("");
			},
			
			//取得最低库存天数HTML
			"getEditCutPrtHtml":function(){
				var html = [];
				html.push('<div id="errorDiv" class="actionError" style="display:none">');
				html.push('<ul><li><span id="errorSpan"></span></li></ul>');
				html.push('</div>');
				html.push('<table id="editTable" width="100%" class="detail" border="1" cellspacing="0" cellpadding="0">');
				html.push('<tbody>');
				//柜台
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_counter").html().escapeHTML()+'</span></th>');
				html.push('<td><span id="counter"></span></td>');
				html.push('</tr>');
				//产品
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_product").html().escapeHTML()+'</span></th>');
				html.push('<td><span id="product"></span></td>');
				html.push('</tr>');
				//最低库存天数
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_lowestStockDays").html().escapeHTML()+'<span class="highlight">*</span></span></th>');
				html.push('<td><span><input name="lowestStockDays" maxlength="4" class="text" id="lowestStockDays"></input>'+$("#SFH10_day").html()+'</span></td>');
				html.push('</tr>');
				// 旬环比增长系数
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_growthFactor").html().escapeHTML()+'</span></th>');
				html.push('<td><span><input name="growthFactor" maxlength="10" class="text" id="editGrowthFactor_pop"></input>'+$("#SFH10_percent").html().escapeHTML()+'</span></td>');
				html.push('</tr>');
				// 主推计划(调整系数)
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_regulateFactor").html().escapeHTML()+'</span></th>');
				html.push('<td><span><input name="regulateFactor" maxlength="10" class="text" id="editRegulateFactor_pop"></input>'+$("#SFH10_percent").html().escapeHTML()+'</span></td>');
				html.push('</tr>');
				html.push('</tbody>');
				html.push('</table>');
				return html.join("");
			},
			
			/**
			 * 取得全局订货参数HTML
			 */
			"getEditGlobalHtml" : function(){
				var html = [];
				html.push('<div id="errorDiv" class="actionError" style="display:none">');
				html.push('<ul><li><span id="errorSpan"></span></li></ul>');
				html.push('</div>');
				html.push('<table id="editTable" width="100%" class="detail" border="1" cellspacing="0" cellpadding="0">');
				html.push('<tbody>');
				// 月销售天数
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_saleDaysOfMonth").html().escapeHTML()+'<span class="highlight">*</span></span></th>');
				html.push('<td><span><input name="saleDaysOfMonth" maxlength="7" class="text" id="saleDaysOfMonth"></input>'+$("#SFH10_day").html()+'</span></td>');
				html.push('</tr>');
				// 对X天内有销售的产品生成建议发货数量
				html.push('<tr>');
				html.push('<th style="width:20%"><span>'+$("#SFH10_daysOfProduct").html().escapeHTML()+'</span></th>');
				html.push('<td><span><input name="daysOfProduct" maxlength="4" class="text" id="daysOfProduct"></input>'+$("#SFH10_day").html()+'</span></td>');
				html.push('</tr>');
				html.push('</tbody>');
				html.push('</table>');
				return html.join("");
			},
			
			"popSetProtParamDialog":function(comfirm,obj){
				var that = this;
				var comfirmTitle = $(comfirm).html();
				var dialogSetting = {
						bgiframe: true,
						width:450, 
						minHeight:590,
						zIndex: 9999,
						modal: true, 
						height: "auto",
						resizable: false,
						title:$("#mulProduct").text(),
						buttons : [ {
							text : comfirmTitle,
							click : function() {
								if(obj == undefined){
									that.setProductParameter();
								}else{
									that.editPrtParam(obj);
								}
							}
						} ,
						{
							text:$("#cancel").html(),
							click:function(){
								that.closeDialog("pop_product_main", "pop_product_body");
							}
						}],
						close: function(event, ui) { that.closeDialog("pop_product_main", "pop_product_body"); }
					};
				this.dialogHtml="";
				this.dialogHtml = $("#pop_product_main").html();
				$('#pop_product_body').dialog(dialogSetting);
				if(obj == undefined){
					that.getTreeNodes("product", $("#pop_product_body").find("#treeDemo"));
					productBinding({
						elementId:"position_left_0",
						showNum:20,
						selected:"name"
						});
					$("#position_left_0").bind("keydown",function(event){
						if(event.keyCode==13){
							that.locationPrtPosition(this,"keydown");
				        }
					});
				}else{
					$("#pop_product_body").parent().css("height",220);
					$("#pop_product_body").parent().css("top","30%");
					$("#pop_product_body").css("height",142);
					$("#pop_product_body #tree").remove();
					var objId = obj.id;
					var product = $(obj).parent().parent().parent().children("td").eq(1).find("span").html();
					$("#date_pop").val(objId.split("_")[1]);
					$("#date_pop").attr("disabled",true);
					$("#adtCoefficient_pop").val(objId.split("_")[2]);
					$("#product_pop").show().find("span").html(product);
					$("#adtCoefficient_pop").focus();
				}
			},
			"popSetCoutParamDialog":function(comfirm,obj){
				var that = this;
				var comfirmTitle = $(comfirm).html();
				var dialogSetting = {
						bgiframe: true,
						width:450, 
						minHeight:590,
						zIndex: 9999,
						modal: true, 
						resizable: false,
						height: "auto",
						title:$("#mulCounter").text(),
						buttons : [ {
							text : comfirmTitle,
							click : function() {
								if(obj == undefined){
									that.setCounterParameter();
								}else{
									that.editCouParam(obj);
								}
							}
						} ,
						{
							text:$("#cancel").html(),
							click:function(){
								that.closeDialog("pop_counter_main", "pop_counter_body");
							}
						} ],
						close: function(event, ui) { that.closeDialog("pop_counter_main", "pop_counter_body"); }
					};
				this.dialogHtml="";
				this.dialogHtml = $("#pop_counter_main").html();
				$('#pop_counter_body').dialog(dialogSetting);
				if(obj == undefined){
					counterBinding({
						elementId:"position_left_1",
						showNum:20,
						selected:"name"
						});
					that.getTreeNodes("counter", $("#pop_counter_body").find("#treeDemo"));
					$("#position_left_1").bind("keydown",function(event){
						if(event.keyCode==13){
							that.locationCutPosition(this,"keydown");
				        }
					});
				}else{
					$("#pop_counter_body").parent().css("height",220);
					$("#pop_counter_body").parent().css("top","30%");
					$("#pop_counter_body").css("height",142);
					$("#pop_counter_body #tree").remove();
					var idArr = $(obj).attr("id").split("_");
					$("#orderDays_pop").val(idArr[1]);
					$("#intransitDays_pop").val(idArr[2]);
				}
			},
			
			/**
			 * 弹出柜台产品订货参数对话框，新增了旬环比增长系数、主推计划(调整系数)两个字段
			 */
			"popSetCouPrtParamDialog":function(){
				var that = this;
				var dialogSetting = {
						bgiframe: true,
						width:450, 
						minHeight:560,
						zIndex: 9999,
						modal: true, 
						resizable: false,
						height: "auto",
						title:$("#mulCouPrt").text(),
						buttons : [ {
							text : $("#yes").html(),
							click : function() {
								that.setCouPrtParam();
							}
						},
						{
							text:$("#cancel").html(),
							click:function(){
								that.closeDialog("pop_setCounter_main", "pop_setCounter_body");
							}
						}  ],
						close: function(event, ui) { that.closeDialog("pop_setCounter_main", "pop_setCounter_body"); }
					};
				this.dialogHtml="";
				this.dialogHtml = $("#pop_setCounter_main").html();
				$('#pop_setCounter_body').dialog(dialogSetting);
				this.getTreeNodes("all");
				productBinding({
					elementId:"position_left_2",
					showNum:20,
					selected:"name"
					});
				counterBinding({
					elementId:"position_left_3",
					showNum:20,
					selected:"name"
					});
				$("#position_left_2").bind("keydown",function(event){
					if(event.keyCode==13){
						that.locationPrtPosition(this,"keydown");
			        }
				});
				$("#position_left_3").bind("keydown",function(event){
					if(event.keyCode==13){
						that.locationCutPosition(this,"keydown");
			        }
				});
				$("#setLowestStockDays_pop").focus();
			},
			
			"popSetGlobalParamDialog" : function() {
				var that = this;
				var dialogSetting = {
						bgiframe: true,
						width:500, 
						minHeight:200,
						zIndex: 9999,
						modal: true, 
						resizable: false,
						height: "auto",
						title:$("#mulGlobal").text(),
						buttons : [ {
							text : $("#yes").html(),
							click : function() {
								that.setGlobalParam();
							}
						},
						{
							text:$("#cancel").html(),
							click:function(){
								that.closeDialog("pop_setGlobal_main", "pop_setGlobal_body");
							}
						}  ],
						close: function(event, ui) { that.closeDialog("pop_setGlobal_main", "pop_setGlobal_body"); }
					};
				this.dialogHtml="";
				this.dialogHtml = $("#pop_setGlobal_main").html();
				$('#pop_setGlobal_body').dialog(dialogSetting);
				
				$("#setSaleDaysOfMonth_pop").focus();
			},
			
			"popEditCouPrtParamDialog":function(obj){
				var that = this;
				var dialogSetting = {
						bgiframe: true,
						width:360, 
						height:200,
						zIndex: 9999,
						modal: true, 
						resizable: false,
						title:'设定最低库存天数',
						buttons : [ {
							text : $("#yes").html(),
							click : function() {
								that.editCouPrtParam(obj);
							}
						},
						{
							text:$("#cancel").html(),
							click:function(){
								that.closeDialog("pop_editCounter_main", "pop_editCounter_body");
							}
						} ],
						close: function(event, ui) { that.closeDialog("pop_editCounter_main", "pop_editCounter_body"); }
					};
				this.dialogHtml="";
				this.dialogHtml = $("#pop_editCounter_main").html();
				$('#pop_editCounter_body').dialog(dialogSetting);
				$("#editLowestStockDays_pop").val($(obj).attr("id").split("_")[1]);
				$("#editLowestStockDays_pop").focus();
			},
			
			"getTreeNodes":function(treeNodesFlag,location){
				var that = this;
				var url = $("#getTreeNodes").html();
				var param = "brandInfoId="+$("#brandInfoId1 option:selected").val()+"&treeNodesFlag="+treeNodesFlag;
				var callback = function(msg){
					that.loadTree(msg,treeNodesFlag,location);
				};
				cherryAjaxRequest({
					url:url,
					param:param,
					callback:callback
				});
			},
			
			"loadTree":function(nodes,flag,location){
				var $treeDemo = location;
				var treeNodes = eval("(" + nodes + ")");
				var treeSetting = {
					checkable : true,
					showLine : true
				};
				if(flag=="product"){
					this.productTree = null;
					this.productTree = $treeDemo.zTree(treeSetting, treeNodes);
				}else if(flag=="counter"){
					this.counterTree = null;
					this.counterTree = $treeDemo.zTree(treeSetting, treeNodes);
				}else{
					var productNodes = treeNodes.productNodes;
					var counterNodes = treeNodes.counterNodes;
					this.counterTree = null;
					this.productTree = null;
					this.productTree = $("#treeDemo_product").zTree(treeSetting, productNodes);
					this.counterTree = $("#treeDemo_counter").zTree(treeSetting, counterNodes);
				}
				
			},
			
			"closeDialog" : function(dialogParent, dialogDiv) {
				$("#" + dialogDiv).dialog("destroy");
				$("#" + dialogDiv).remove();
				$("#" + dialogParent).html(this.dialogHtml);
			},
			"setProductParameter":function(){
				if($.trim($("#date_pop").val())==""){
					$("#errorSpan_product").html($("#noDate").html());
					$("#errorDiv_product").show();
					return false;
				}
				if(!this.isDate($.trim($("#date_pop").val()))){
					$("#errorSpan_product").html($("#dateWarning").html());
					$("#errorDiv_product").show();
					return false;
				}
				if($.trim($("#adtCoefficient_pop").val()) == ""){
					$("#errorSpan_product").html($("#noAdtCoefficient").html());
					$("#errorDiv_product").show();
					return false;
				}
				if(!this.isPositiveFloat($.trim($("#adtCoefficient_pop").val()))){
					$("#errorSpan_product").html($("#adtCoefficientWarning").html());
					$("#errorDiv_product").show();
					return false;
				}
				var checkedPrt = [];
				var checkedNodes = this.productTree.getCheckedNodes();
				if(checkedNodes.length==0){
					$("#errorSpan_product").html($("#noChioceProduct").html());
					$("#errorDiv_product").show();
					return false;
				}
				for ( var key in checkedNodes) {
					if(checkedNodes[key].nodes == undefined && checkedNodes[key].id != 'all'){
						var o={
								productVendorId:checkedNodes[key].id
						};
						checkedPrt.push(o);
					}
				}
				var that = this;
				var url = $("#setProductParameter").html();
				var param = "date="+$("#date_pop").val()+"&adtCoefficient="+$("#adtCoefficient_pop").val()+"&brandInfoId="+$("#brandInfoId1 option:selected").val()+"&checkedPrt="+JSON2.stringify(checkedPrt);
				var callback = function(){
					that.proSearch("flag");
					that.closeDialog("pop_product_main", "pop_product_body");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					callback:callback,
					coverId:coverId
				});
//				$("#pop_product_body").html('<p class="message" style="margin:10% auto 0;"><span>'+$("#doing").html());
//				$("#pop_product_body").parent().css("height",165);
//				$("#pop_product_body").css("height",150);
//				$("#pop_product_body").next().remove();
//				$("#pop_product_body").prev().remove();
			},
			"editPrtParam":function(obj){
				if($.trim($("#editTable #adtCoefficient").val()) == ""){
					$("#errorSpan").html($("#noAdtCoefficient").html());
					$("#errorDiv").show();
					return false;
				}
				if(!this.isPositiveFloat($.trim($("#editTable #adtCoefficient").val()))){
					$("#errorSpan").html($("#adtCoefficientWarning").html());
					$("#errorDiv").show();
					return false;
				}
				var that = this;
				var productOrderParameterId = $(obj).attr("id").split("_")[0];
				var url = $("#editPrtParam").html();
				var param = "productOrderParameterId="+productOrderParameterId+"&adtCoefficient="+$("#editTable #adtCoefficient").val();
				var callback = function(){
					that.proSearch("flag");
					removeDialog("#editDialog");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					callback:callback,
					coverId:coverId
				});
			},
			
			"setCounterParameter":function(){
				if($.trim($("#orderDays_pop").val())=="" || $.trim($("#intransitDays_pop").val()) == ""){
					$("#errorSpan_counter").html($("#noDays").html());
					$("#errorDiv_counter").show();
					return false;
				}
				if(!this.isPositiveInteger($.trim($("#orderDays_pop").val())) || !this.isPositiveInteger($.trim($("#intransitDays_pop").val()))){
					$("#errorSpan_counter").html($("#daysWarning").html());
					$("#errorDiv_counter").show();
					return false;
				}
				var checkedCut = [];
				var checkedNodes = this.counterTree.getCheckedNodes();
				if(checkedNodes.length==0){
					$("#errorSpan_counter").html($("#noChioceCounter").html());
					$("#errorDiv_counter").show();
					return false;
				}
				for ( var key in checkedNodes) {
					if(checkedNodes[key].nodes == undefined && checkedNodes[key].id != "all"){
						var o={
								organizationId:checkedNodes[key].id
						};
						checkedCut.push(o);
					}
				}
				var that = this;
				var url = $("#setCounterParameter").html();
				var param = "orderDays="+$("#orderDays_pop").val()+"&intransitDays="+$("#intransitDays_pop").val()+"&brandInfoId="+$("#brandInfoId2 option:selected").val()+"&checkedCut="+JSON2.stringify(checkedCut);
				var callback = function(){
					that.couSearch("flag");
					that.closeDialog("pop_counter_main", "pop_counter_body");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					callback:callback,
					coverId:coverId
				});
//				$("#pop_counter_body").html('<p class="message" style="margin:10% auto 0;"><span>'+$("#doing").html());
//				$("#pop_counter_body").parent().css("height",165);
//				$("#pop_counter_body").css("height",150);
//				$("#pop_counter_body").next().remove();
//				$("#pop_counter_body").prev().remove();
			},
			"editCouParam":function(obj){
				if($.trim($("#editTable #orderDays").val())=="" || $.trim($("#editTable #intransitDays").val()) == ""){
					$("#errorSpan").html($("#noDays").html());
					$("#errorDiv").show();
					return false;
				}
				if(!this.isPositiveInteger($.trim($("#editTable #orderDays").val())) || !this.isPositiveInteger($.trim($("#editTable #intransitDays").val()))){
					$("#errorSpan").html($("#daysWarning").html());
					$("#errorDiv").show();
					return false;
				}
				var that = this;
				var counterOrderParameterId = $(obj).attr("id").split("_")[0];
				var url = $("#editCouParam").html();
				var param = "counterOrderParameterId="+counterOrderParameterId+"&orderDays="+$("#editTable #orderDays").val()+"&intransitDays="+$("#editTable #intransitDays").val();
				var callback = function(){
					that.couSearch("flag");
					removeDialog("#editDialog");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					callback:callback,
					coverId:coverId
				});
			},
			
			"setCouPrtParam":function(flag){
				if($.trim($("#setLowestStockDays_pop").val()) == ""){
					$("#errorSpan_couPrt").html($("#noInputWarn").html()+'【'+$("#SFH10_lowestStockDays").html()+'】');
					$("#errorDiv_couPrt").show();
					return false;
				}
//				if($.trim($("#setGrowthFactor_pop").val()) == ""){
//					$("#errorSpan_couPrt").html($("#noInputWarn").html()+'【'+$("#SFH10_growthFactor").html()+'】');
//					$("#errorDiv_couPrt").show();
//					return false;
//				}
//				if($.trim($("#setRegulateFactor_pop").val()) == ""){
//					$("#errorSpan_couPrt").html($("#noInputWarn").html()+'【'+$("#SFH10_regulateFactor").html()+'】');
//					$("#errorDiv_couPrt").show();
//					return false;
//				}
				if(!this.isPositiveInteger($.trim($("#setLowestStockDays_pop").val()))){
					$("#errorSpan_couPrt").html($("#daysWarning").html());
					$("#errorDiv_couPrt").show();
					return false;
				}
				if($.trim($("#setGrowthFactor_pop").val()) != "" && !this.isPositiveDecimal($.trim($("#setGrowthFactor_pop").val()),'2')){
					$("#errorSpan_couPrt").html($("#growthFactorWarning").html());
					$("#errorDiv_couPrt").show();
					return false;
				}
				if($.trim($("#setRegulateFactor_pop").val()) != "" && !this.isPositiveDecimal($.trim($("#setRegulateFactor_pop").val()),'2')){
					$("#errorSpan_couPrt").html($("#regulateFactorWarning").html());
					$("#errorDiv_couPrt").show();
					return false;
				}
				var that = this;
				var url = $("#setCouPrtParam").html();
				var checkedPrt = [];
				var checkedCut = [];
				var checkedPrtNodes = this.productTree.getCheckedNodes();
				if(checkedPrtNodes.length==0){
					$("#errorSpan_couPrt").html($("#noChioceProduct").html());
					$("#errorDiv_couPrt").show();
					return false;
				}
				var checkedCutNodes = this.counterTree.getCheckedNodes();
				if(checkedCutNodes.length==0){
					$("#errorSpan_couPrt").html($("#noChioceCounter").html());
					$("#errorDiv_couPrt").show();
					return false;
				}
				for(var key1 in checkedPrtNodes){
					if(checkedPrtNodes[key1].nodes == undefined && checkedPrtNodes[key1].id !='all' ){
						var o = {
							productVendorId:checkedPrtNodes[key1].id
						};
						checkedPrt.push(o);
					}
				}
				
				for(var key2 in checkedCutNodes){
					if(checkedCutNodes[key2].nodes == undefined && checkedCutNodes[key2].id != 'all'){
						var o = {
							organizationId:checkedCutNodes[key2].id
						};
						checkedCut.push(o);
					}
				}
				var param = "departCode=" + $("#counterCode_pop").val()
				+ "&lowestStockDays=" + $("#setLowestStockDays_pop").val()
				+ "&growthFactor=" + $("#setGrowthFactor_pop").val()
				+ "&regulateFactor=" + $("#setRegulateFactor_pop").val()
				+ "&brandInfoId=" + $("#brandInfoId3 option:selected").val()
				+ "&checkedCut=" + JSON2.stringify(checkedCut) + "&checkedPrt="
				+ JSON2.stringify(checkedPrt);
				var callback = function(){
					that.couPrtSearch("flag");
					that.closeDialog("pop_setCounter_main", "pop_setCounter_body");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					coverId:coverId,
					callback:callback
				});
//				$("#pop_setCounter_body").html('<p class="message" style="margin:10% auto 0;"><span>'+$("#doing").html());
//				$("#pop_setCounter_body").parent().css("height",165);
//				$("#pop_setCounter_body").css("height",150);
//				$("#pop_setCounter_body").next().remove();
//				$("#pop_setCounter_body").prev().remove();
			},
			
			"editCouPrtParam":function(obj){
				var that = this;
				if($.trim($("#lowestStockDays").val()) == ""){
					$("#errorSpan").html($("#noInputWarn").html()+'【'+$("#SFH10_lowestStockDays").html()+'】');
					$("#errorDiv").show();
					return false;
				}
//				if($.trim($("#editGrowthFactor_pop").val()) == ""){
//					$("#errorSpan").html($("#noInputWarn").html()+'【'+$("#SFH10_growthFactor").html()+'】');
//					$("#errorDiv").show();
//					return false;
//				}
//				if($.trim($("#editRegulateFactor_pop").val()) == ""){
//					$("#errorSpan").html($("#noInputWarn").html()+'【'+$("#SFH10_regulateFactor").html()+'】');
//					$("#errorDiv").show();
//					return false;
//				}
				if(!this.isPositiveInteger($.trim($("#lowestStockDays").val()))){
					$("#errorSpan").html($("#daysWarning").html());
					$("#errorDiv").show();
					return false;
				}
				if($.trim($("#editGrowthFactor_pop").val()) != "" && !this.isPositiveDecimal($.trim($("#editGrowthFactor_pop").val()),'2')){
					$("#errorSpan").html($("#growthFactorWarning").html());
					$("#errorDiv").show();
					return false;
				}
				if($.trim($("#editRegulateFactor_pop").val()) != "" && !this.isPositiveDecimal($.trim($("#editRegulateFactor_pop").val()),'2')){
					$("#errorSpan").html($("#regulateFactorWarning").html());
					$("#errorDiv").show();
					return false;
				}
				var url = $("#editCouPtrParam").html();
				var counterPrtOrParameterId = $(obj).attr("id").split("_")[0];
				var param = "lowestStockDays=" + $("#lowestStockDays").val()
				+ "&growthFactor=" + $("#editGrowthFactor_pop").val()
				+ "&regulateFactor=" + $("#editRegulateFactor_pop").val()
				+ "&counterPrtOrParameterId=" + counterPrtOrParameterId;
				var callback = function(){
					that.couPrtSearch("flag");
					removeDialog("#editDialog");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					coverId:coverId,
					callback:callback
				});
			},
			

			"setGlobalParam" : function(flag) {
				if ($.trim($("#setSaleDaysOfMonth_pop").val()) == "") {
					// 请输入月销售天数
					$("#errorSpan_global").html($("#noInputWarn").html()+'【'+$("#SFH10_saleDaysOfMonth").html()+'】');
					$("#errorDiv_global").show();
					return false;
				}
//				if($.trim($("#setDaysOfProduct_pop").val()) == "") {
//					// 请输入对X天内有销售的产品生成建议发货数量
//					$("#errorSpan_global").html($("#noInputWarn").html()+'【'+$("#SFH10_daysOfProduct").html()+'】');
//					$("#errorDiv_global").show();
//					return false;
//				}
				if (!this.isPositiveDecimal($.trim($("#setSaleDaysOfMonth_pop").val()))) {
					$("#errorSpan_global").html($("#daysDecimalWarning").html());
					$("#errorDiv_global").show();
					return false;
				}
				if($.trim($("#setDaysOfProduct_pop").val()) != "" && !this.isPositiveInteger($.trim($("#setDaysOfProduct_pop").val()))) {
					// 输入的【对X天内有销售的产品生成建议发货数量】的天数只能是0-9999之间的整数
					$("#errorSpan_global").html($("#daysOfProductWarning").html());
					$("#errorDiv_global").show();
					return false;
				}
				var that = this;
				var url = $("#setGlobalParam").html();
				var param = "saleDaysOfMonth=" + $("#setSaleDaysOfMonth_pop").val()
						+ "&daysOfProduct=" + $("#setDaysOfProduct_pop").val()
						+ "&brandInfoId=" + $("#brandInfoId_Global option:selected").val();
				var callback = function() {
					that.globalSearch("flag");
					that.closeDialog("pop_setGlobal_main", "pop_setGlobal_body");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url : url,
					param : param,
					coverId : coverId,
					callback : callback
				});
			},
			
			"editGlobalParam":function(obj){
				var that = this;
				if($.trim($("#saleDaysOfMonth").val()) == ""){
					$("#errorSpan").html($("#noInputWarn").html()+'【'+$("#SFH10_saleDaysOfMonth").html()+'】');
					$("#errorDiv").show();
					return false;
				}
//				if($.trim($("#daysOfProduct").val()) == "") {
//					// 请输入对X天内有销售的产品生成建议发货数量
//					$("#errorSpan").html($("#noInputWarn").html()+'【'+$("#SFH10_daysOfProduct").html()+'】');
//					$("#errorDiv").show();
//					return false;
//				}
				if(!this.isPositiveDecimal($.trim($("#saleDaysOfMonth").val()))){
					$("#errorSpan").html($("#daysDecimalWarning").html());
					$("#errorDiv").show();
					return false;
				}
				if($.trim($("#daysOfProduct").val()) != "" && !this.isPositiveInteger($.trim($("#daysOfProduct").val()))) {
					// 输入的【对X天内有销售的产品生成建议发货数量】的天数只能是0-9999之间的整数
					$("#errorSpan_global").html($("#daysOfProductWarning").html());
					$("#errorDiv_global").show();
					return false;
				}
				var url = $("#editGlobalParam").html();
				var counterPrtOrParameterId = $(obj).attr("id").split("_")[0];
				var param = "saleDaysOfMonth=" + $("#saleDaysOfMonth").val()
				+ "&daysOfProduct=" + $("#daysOfProduct").val()
				+ "&globalParameterId=" + counterPrtOrParameterId;
				var callback = function(){
					that.globalSearch("flag");
					removeDialog("#editDialog");
				};
				var coverId = 'body';
				cherryAjaxRequest({
					url:url,
					param:param,
					coverId:coverId,
					callback:callback
				});
			},
			
			"issOrderParam":function(){
				var url = $("#issOrderParam").html();
				var param = "brandInfoId="+$("#brandInfoId1 option:selected").val();
				var coverId = 'body';
				var callback = function(){
					removeDialog("#dialogInit");
				};
				cherryAjaxRequest({
					url:url,
					param:param,
					coverId:coverId,
					callback:callback
				});
				$("#dialogInit").html('<p class="message"><span>'+$("#downing").html());
//				$("#dialogInit").next().remove();
//				$("#dialogInit").prev().remove();
			},
			
			"confirmInit":function(){
				var that = this;
//				var text = '<p class="message"><span>'+$("#downWarning").html();
//				var title = $("#downTitle").html();
				var dialogSetting = {
						dialogInit: "#dialogInit",
						text: '<p class="message"><span>'+$("#downWarning").html(),
						width: 	500,
						height: 300,
						title: 	$("#downTitle").html(),
						confirm: $("#yes").html(),
						confirmEvent: function(){that.issOrderParam();}
					};
				openDialog(dialogSetting);
			},
			
			"isPositiveFloat":function(s){
				var patrn=/^((([1-9][0-9]{0,5}|0.[0-9]{1,4})|[1-9][0-9]{0,11}\.[0-9]{1,4})|0)$/; 
				if (!patrn.exec(s)) return false;
				return true;
			},
			
			/**
			 * 数据是否符合Decimal格式
			 * flag : 	无此参数：Decimal(6,2)格式
			 * 			'2':Decimal(10,2)格式
			 */
			"isPositiveDecimal":function(s,flag){
				// 数据是否符合Decimal（6，2）
				var patrn=/^\d{1,4}(?:\.\d{1,2}|\.?)$/; 
				if(flag != undefined && flag == '2') {
					// 数据是否符合Decimal（10，2）
					patrn=/^\d{1,8}(?:\.\d{1,2}|\.?)$/; 
				} 
				if (!patrn.exec(s)) return false;
				return true;
			},
			
			"isPositiveInteger":function(s){
				var patrn=/^(0|[1-9][0-9]{0,3})$/; 
				if (!patrn.exec(s)) return false;
				return true;
			},
			"isDate":function(s){
				var patrn=/^[0-9]{4}(0[1-9]|1[0,1,2])$/; 
				if (!patrn.exec(s)) return false;
				return true;
			},
			
			"locationPrtPosition":function(obj,flag){
				if(typeof flag == "undefined"){
					var $input = $(obj).prev();
				}else{
					var $input = $(obj);
				}
				var inputNode = this.productTree.getNodesByParamFuzzy("name",$input.val());
				this.productTree.expandNode(inputNode[0],true,false);
				this.productTree.selectNode(inputNode[0]);
			},
			
			"locationCutPosition":function(obj,flag){
				if(typeof flag == "undefined"){
					var $input = $(obj).prev();
				}else{
					var $input = $(obj);
				}
				var inputNodes = this.counterTree.getNodesByParamFuzzy("name",")"+$input.val());
				this.counterTree.expandNode(inputNodes[0],true,false);
				this.counterTree.selectNode(inputNodes[0]);
			},
			
			"dateTip":function(flag){
				if(flag == 1){
					$('#dateTip').show();
					return false;
				}else if(flag == 0){
					$('#dateTip').hide();
					return false;
				}else{
					;
				}
				
			}
};

var binOLSTSFH10 = new BINOLSTSFH10();

var PTODR01_dialogBody = "";