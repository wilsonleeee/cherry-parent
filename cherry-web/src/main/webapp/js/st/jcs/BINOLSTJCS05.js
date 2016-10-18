/**
 * @author zgl
 *
 */
function BINOLSTJCS05(){};

BINOLSTJCS05.prototype={
		
		"regionTree":null,
		
		"deportTree":null,
		
		"dialogHtml":null,
		
		"search":function(){
			var that = this;
			if(arguments[0] == undefined){
				$("#actionResultDisplay").empty();
				$("#errorSpan").html("");
				$("#errorDiv").hide();
			}
			var url= $("#search").html();
			var $form = $("#mainForm");
			// 表单验证
			if(!$form.valid()){
				return;
			}	
			var configModel = $("#configModel").val();
			var columns = [];
			if(configModel == "1"){
				columns.push({ "sName": "no.","sWidth": "5%","bSortable":false});
				columns.push({ "sName": "outCode","sWidth": "25%"});
				columns.push({ "sName": "inCode","sWidth": "25%"});
				columns.push({ "sName": "businessType","sWidth": "15%","sClass":"center"});
				columns.push({ "sName": "brandName","sWidth": "15%","sClass":"center"});
			}else{
				columns.push({ "sName": "checkbox", "sWidth": "1%", "bSortable": false});
				columns.push({ "sName": "no.","sWidth": "5%","bSortable":false});
				columns.push({ "sName": "outCode","sWidth": "25%"});
				columns.push({ "sName": "inCode","sWidth": "25%"});
				columns.push({ "sName": "businessType","sWidth": "15%","sClass":"center"});
				columns.push({ "sName": "brandName","sWidth": "15%","sClass":"center"});
				columns.push({ "sName": "brandName","sWidth": "10%","bSortable": false,"sClass":"center"});
			}
			 var params= $form.serialize();
			 url = url + "?" + params;
			 // 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 表格列属性设置
					// 表格默认排序
					 aaSorting : [[ function(){return configModel == "1" ? 3 : 4;}(), "asc" ]],
					 aoColumns :columns,
					// 不可设置显示或隐藏的列	
					aiExclude :function(){return configModel == "1" ? [0, 1] : [0, 1,2];}(),
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					index:3,
					// 固定列数
					fixedColumns : function(){
				 		return configModel == "1" ? 2 : 3;
			 		}(),
					fnDrawCallback : function() {
						$("#deportOption a").unbind("click");
					}
			 
			 };
			 $("input[name=allSelect]").prop("checked", false);
			 // 调用获取表格函数
			 getTable(tableSetting);
		},
		
		/**
		 * 递归子节点设置手动选中false
		 */
		"cancelManualFlag":function(node){
			for(var i in node.nodes) {
				node.nodes[i].manualFlag = "false";
				if(node.nodes[i].nodes != null){
					binOLSTJCS05.cancelManualFlag(node.nodes[i].nodes);
				}
			}
		},
		
		/**
		 * 节点勾选状态改变执行方法
		 */
		"zTreeOnChange":function(event, treeId, treeNode){
			var tnId = treeNode.id;
			//手工勾选节点
			if(treeNode.checked){
				treeNode.manualFlag = "true";
			}else{
				treeNode.manualFlag = "false";
				
				//取消勾选，上级节点取消手工勾选
				var parentNode = treeNode.parentNode;
				parentNode.manualFlag = "false";
				//取消勾选，当前节点的同级节点需要设置手工勾选节点
				for(var i in parentNode.nodes) {
					if(tnId != parentNode.nodes[i].id){
						if(parentNode.nodes[i].checked){
							parentNode.nodes[i].manualFlag = "true";
						}else{
							parentNode.nodes[i].manualFlag = "false";
						}
					}
				}
				//取消勾选，当前节点的子节点需要设置取消手工勾选节点
				binOLSTJCS05.cancelManualFlag(treeNode);
			}

			//勾选Root节点也会手工勾选/取消下一级节点
			if(tnId == -1 || tnId == -2){
				var rootNode = binOLSTJCS05.deportTree.getNodeByTId(treeNode.tId);
				if(treeNode.checked){
					for(var i in rootNode.nodes) {
						rootNode.nodes[i].manualFlag = "true";
					}
				}else{
					for(var i in rootNode.nodes) {
						rootNode.nodes[i].manualFlag = "false";
					}
				}
			}
		},
		
		"loadTree":function(nodesObj){
			var treeSetting = {
					checkable : true,
					showLine : true,
					//"p" 表示操作会影响父级节点； 
				    //"s" 表明操作会影响子级节点。
					checkType : {
						"Y" : "s",
						"N" : "s"
					},
					callback: {
						change: binOLSTJCS05.zTreeOnChange
					}
			};
			this.deportTree = $("#treeDemo1").zTree(treeSetting, nodesObj);
		},
		
		"changeTree":function(obj){
			var $this = $(obj);
			if($this.val() == 0){
				$("#regionTree").hide();
				$("#deportTree").show();
			}else{
				$("#deportTree").hide();
				$("#regionTree").show();
			}
		},
		
		"popAddDialog":function(){
			$("#actionResultDisplay").empty();
			$("#errorSpan").html("");
			$("#errorDiv").hide();
			var that = this;
			var comfirmTitle = $("#yes").val();
			var dialogTitle = $("#dialogTitle").val();
			var dialogSetting = {
				bgiframe : true,
				width : 500,
				height: "auto",
				minHeight:570,
				zIndex : 9999,
				modal : true,
				resizable : false,
				title : dialogTitle,
				buttons : [ {
					text : comfirmTitle,
					click : function() {
						that.saveAdd();
					}
				},{
					text:$("#cancel").val(),
					click : function() {
						that.closeDialog("add_main","add_body");
					}
				} ],
				close : function(event, ui) {
					that.closeDialog("add_main",
							"add_body");
				}
			};
			this.dialogHtml = $("#add_main").html();
			$('#add_body').dialog(dialogSetting);
			this.showAndhideReverseBusiness("#add_body #businessType");
			deportBinding({
				elementId:"position_left_0",
				showNum:20,
				includeRegion:true
			});
			
			$("#position_left_0").bind("keydown",function(event){
				if(event.keyCode==13){
					that.locationDepPosition(this,"keydown");
		        }
			});
			
			var url = $("#getAddTreeNodes").html();
			var param = $("#mainForm").serialize();
			var callback = function(msg){
				var nodesObj = eval("(" + msg + ")");
				that.regionTree = null;
				that.loadTree(nodesObj);
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
			
		},
		
		"popEditdialog":function(obj){
			$("#actionResultDisplay").empty();
			$("#errorSpan").html("");
			$("#errorDiv").hide();
			var that = this;
			var comfirmTitle = $("#yes").val();
			var dialogTitle = $("#dialogTitle").val();
			var dialogSetting = {
				bgiframe : true,
				width : 500,
				height: "auto",
				minHeight:570,
				zIndex : 9999,
				modal : true,
				resizable : false,
				title : dialogTitle,
				buttons : [ {
					text : comfirmTitle,
					click : function() {
						that.saveEdit();
					}
				},{
					text:$("#cancel").val(),
					click : function() {
						that.closeDialog("add_main","add_body");
					}
				}  ],
				close : function(event, ui) {
					that.closeDialog("add_main",
							"add_body");
				}
			};
			this.dialogHtml = $("#add_main").html();
			$('#add_body').dialog(dialogSetting);
			deportBinding({
				elementId:"position_left_0",
				showNum:20,
				includeRegion:true
			});
			
			$("#position_left_0").bind("keydown",function(event){
				if(event.keyCode==13){
					that.locationDepPosition(this,"keydown");
		        }
			});
			$("#businessType_edit").show();
			$("#businessType_add").hide();
			var url = $("#getEditInfo").html();
			var checked = obj;
			$("#businessType_edit #businessType option").each(function(){
				if($(this).val() == checked.name){
					$(this).prop("selected",true);
				}
			});
			var businessType_edit = checked.name;
			//是否显示同步更新逆向业务
			if(businessType_edit!='40'){
				$("#synchronousReverseBusiness").prop("checked",false);
				$("#synchronousReverseBusiness").hide().next().hide();
			}else{
				$("#synchronousReverseBusiness").prop("checked",true);
				$("#synchronousReverseBusiness").show().next().show();
			}
			//入出库方标志显示
			if(businessType_edit=='60'){
				$("#edit_inDeport").show();
				$("#edit_outDeport").hide();
				$("#left_tree_out").show();
				$("#left_tree_in").hide();
			}else{
				$("#edit_inDeport").hide();
				$("#edit_outDeport").show();
				$("#left_tree_out").hide();
				$("#left_tree_in").show();
			}
			
			$("#businessType_edit #businessType").attr("disabled","disabled");
			var param = "depotBusinessId="+checked.id+"&businessType="+checked.name;
			$("#editDepotBusinessId").val(checked.id);
			var callback = function(msg){
				var obj = eval("("+msg+")");
				var nodes = obj.inInfo;
				var editInfo = obj.outInfo;
				if(businessType_edit=='60'){
					$("#inventoryInfoId_edit option").each(function(){
						if($(this).val().split("*")[0] == editInfo.InID){
							$(this).prop("selected",true);
						}
					});
				}else{
					$("#inventoryInfoId_edit option").each(function(){
						if($(this).val().split("*")[0] == editInfo.OutID){
							$(this).prop("selected",true);
						}
					});
				}
				$("#inventoryInfoId_edit").attr("disabled","disabled");
				that.regionTree = null;
				that.loadTree(nodes);
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		},
		
		"closeDialog" : function(dialogParent, dialogDiv) {
			$("#" + dialogDiv).dialog("destroy");
			$("#" + dialogDiv).remove();
			$("#" + dialogParent).html(this.dialogHtml);
		},
		
		"saveAdd":function(){
			var that = this;
			that.clearErrorMessage();
			var thisDepot = $("#inventoryInfoId_add option:selected").val();
			var thisDepotId = thisDepot.split("*")[0];
			var thisDepotTestType = thisDepot.split("*")[1];
			var url = $("#saveAdd").html();
			var checkedNodes = that.deportTree.getCheckedNodes();
			var arr = [];
			for(var key in checkedNodes){
				if(checkedNodes[key].manualFlag != "true"){
					//只加入手动勾选的节点
					continue;
				}
				if(checkedNodes[key].id != "-1" && checkedNodes[key].id != "-2"){
					if(typeof checkedNodes[key].nodes == "undefined"){
						if( checkedNodes[key].testType != thisDepotTestType){
							this.showWarm(thisDepotTestType);
							return false;
						}
					}
					var o = {
						InID:checkedNodes[key].id	
					};
					if(typeof checkedNodes[key].deportFlag == "undefined"){
						o.InIdFlag = "1";
					}else{
						o.InIdFlag = "0";
					}
					//按部门组织架构配置
					if($("#configByDepOrg").is(":checked")){
						o.InIdFlag = "2";
					}
					arr.push(o);
				}
			}
			if(arr.length == 0){
				$("#errorDiv_dialog").show();
				return false;
			}
			if($("#reverseBusiness").is(":checked")){
				var flag = "1";
			}else{
				var flag = "0";
			}
			var param = "brandInfoId="+$("#brandInfoId option:selected").val()+"&businessType="+$("#businessType_add #businessType option:selected").val()+"&flag="+flag+"&deportId="+thisDepotId+"&deportOrRegionStr="+JSON2.stringify(arr);
			var callback = function(){
				that.closeDialog("add_main",
				"add_body");
				that.search("flag");
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		},

		"saveEdit":function(){
			var that = this;
			that.clearErrorMessage();
			var url = $("#saveEdit").html();
			var thisDepot = $("#inventoryInfoId_edit option:selected").val();
			var thisDepotId = thisDepot.split("*")[0];
			var thisDepotTestType = thisDepot.split("*")[1];
			var checkedNodes = that.deportTree.getCheckedNodes();
			var businessType = $("#businessType_edit #businessType option:selected").val();
			var arr = [];
			if(businessType == "60"){
				for(var key in checkedNodes){
					if(checkedNodes[key].manualFlag != "true"){
						//只加入手动勾选的节点
						continue;
					}
					if(checkedNodes[key].id != "-1" && checkedNodes[key].id != "-2"){
						if(typeof checkedNodes[key].nodes == "undefined"){
							if( checkedNodes[key].testType != thisDepotTestType){
								this.showWarm(thisDepotTestType);
								return false;
							}
						}
						var o = {
							OutID:checkedNodes[key].id	
						};
						if(typeof checkedNodes[key].deportFlag == "undefined"){
							o.OutIdFlag = "1";
						}else{
							o.OutIdFlag = "0";
						}
						//按部门组织架构配置
						if($("#configByDepOrg").is(":checked")){
							o.OutIdFlag = "2";
						}
						arr.push(o);
					}
				}
			}else{
				for(var key in checkedNodes){
					if(checkedNodes[key].manualFlag != "true"){
						//只加入手动勾选的节点
						continue;
					}
					if(checkedNodes[key].id != "-1" && checkedNodes[key].id != "-2"){
						if(typeof checkedNodes[key].nodes == "undefined"){
							if( checkedNodes[key].testType != thisDepotTestType){
								this.showWarm(thisDepotTestType);
								return false;
							}
						}
						var o = {
							InID:checkedNodes[key].id	
						};
						if(typeof checkedNodes[key].deportFlag == "undefined"){
							o.InIdFlag = "1";
						}else{
							o.InIdFlag = "0";
						}
						//按部门组织架构配置
						if($("#configByDepOrg").is(":checked")){
							o.InIdFlag = "2";
						}
						arr.push(o);
					}
				}
			}
			if(arr.length == 0){
				$("#errorDiv_dialog").show();
				return false;
			}
			if($("#synchronousReverseBusiness").is(":checked")){
				var flag = "1";
			}else{
				var flag = "0";
			}
			var param = "brandInfoId="+$("#brandInfoId option:selected").val()+"&businessType="+$("#businessType_edit #businessType option:selected").val()+"&flag="+flag+"&deportId="+thisDepotId+"&deportOrRegionStr="+JSON2.stringify(arr);
			if($("#configByDepOrg").is(":checked")){
				param += "&configByDepOrg="+$("#configByDepOrg").val();
			}
			var callback = function(){
				that.closeDialog("add_main",
				"add_body");
				that.search("flag");
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		},
		
		"showWarm":function(testType){
			if(testType == "1"){
				$("#add_body #errorSpan2").html($("#testWarming").val());
				$("#add_body #errorDiv2").show();
			}else{
				$("#add_body #errorSpan2").html($("#officialWarming").val());
				$("#add_body #errorDiv2").show();
			}
		},
		
		"showAndhideReverseBusiness":function(obj){
			var $this = $(obj);
			if($this.find("option:selected").val()=='30'){
				$("#reverseBusiness").prop("checked",false);
				$("#reverseBusiness").hide().next().hide();
			}else{
				$("#reverseBusiness").show().next().show();
			}
		},
		
		"checkSelectAll" : function(obj) {
			$("#actionResultDisplay").html("");
			$("#errorDiv #errorSpan").html("");
			$("#errorDiv").hide();
			var $this = $(obj);
			$("#dataTable_Cloned").find("input[name=validFlag]").prop("checked",
					$this.prop("checked"));
			this.optButtCol();
		},

		"checkSelect" : function(obj) {
			$("#actionResultDisplay").html("");
			$("#errorDiv #errorSpan").html("");
			$("#errorDiv").hide();
			var $this = $(obj);
			if ($this.prop("checked")) {
				var num = $("#dataTable_Cloned").find("input[name=validFlag]").length;
				if ($("#dataTable_Cloned").find("input[name=validFlag]:checked").length == num) {
					$("input[name=allSelect]").prop("checked", true);
				}
			} else {
				$("input[name=allSelect]").prop("checked", false);
			}
			this.optButtCol();
		},

		"optButtCol" : function() {
			var that = this;
			$("#actionResultDisplay").html("");
			$("#errorDiv #errorSpan").html("");
			$("#errorDiv").hide();
			var checked = $("#dataTable_Cloned").find("input[name=validFlag]:checked");
			var num = checked.length;
			if (num <= 0) {
				$("#deleteBtn").unbind("click");
			} else {
				$("#deleteBtn").unbind("click");
				$("#deleteBtn").bind("click", function() {
					that.confirmInit();
					return false;
				});
			}
		},

		"confirmInit" : function() {
			var that = this;
			$("#actionResultDisplay").html("");
			$("#errorDiv #errorSpan").html("");
			$("#errorDiv").hide();
			var text = '<p class="message"><span>' + $("#deleteWarming").val();
			var title = $("#deleteTitle").val();
			var dialogSetting = {
				dialogInit : "#dialogInit",
				text : text,
				width : 500,
				height : 300,
				title : title,
				confirm : $("#yes").val(),
				cancel : $("#cancel").val(),
				confirmEvent : function() {
					that.deleteDepotBusiness();	
				},
				cancelEvent : function() {
					removeDialog("#dialogInit");
				}
			};
			openDialog(dialogSetting);
		},
		
		"deleteDepotBusiness":function(){
			var that = this;
			var url = $("#deleteDepotBusiness").html();
			var idArr = [];
			$("#dataTable_Cloned").find("input[name=validFlag]:checked").each(function(){
				var o = {
					BIN_DepotBusinessID:this.id
				};
				idArr.push(o);
			});
			var param = "deportBusinessStr="+JSON2.stringify(idArr);
			var callback = function(){
				removeDialog("#dialogInit");
				that.search();
				$("input[name=allSelect]").prop("checked", false);
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		},
		
		"locationDepPosition":function(obj,flag){
			if(typeof flag == "undefined"){
				var $input = $(obj).prev();
			}else{
				var $input = $(obj);
			}
			var inputNodes = this.deportTree.getNodesByParamFuzzy("name",")"+$input.val());
			this.deportTree.expandNode(inputNodes[0],true,false);
			this.deportTree.selectNode(inputNodes[0]);
		},
		
		"clearErrorMessage":function(){
			$("#add_body #errorDiv_dialog").hide();
			$("#add_body #errorDiv2").hide();
		},
		
		"relodeTree":function(){
			var url = $("#getAddTreeNodes").html();
			var param = $("#mainForm").serialize();
			if($("#businessType_edit").is(":visible")){
				url = $("#getEditInfo").html();
				param = "depotBusinessId="+$("#editDepotBusinessId").val()+"&businessType="+$("#businessType_edit #businessType").val();
			}
			if($("#configByDepOrg").is(":checked")){
				param += "&configByDepOrg="+$("#configByDepOrg").val();
				$("#spanTip").hide();
				$("#spanTipDepart").show();
				if($("#left_tree_in").is(":visible")){
					$("#spanchoiceInRegion").hide();
					$("#spanchoiceInDepart").show();
				}else{
					$("#spanchoiceOutDepor").hide();
					$("#spanchoiceOutDepart").show();
				}
				
				deportBinding({
					elementId:"position_left_0",
					showNum:20,
					includeDepart:true,
					onlyDepart:true
				});
			}else{
				$("#spanTip").show();
				$("#spanTipDepart").hide();
				if($("#left_tree_out").is(":visible")){
					$("#spanchoiceOutDepor").show();
					$("#spanchoiceOutDepart").hide();
				}else{
					$("#spanchoiceInRegion").show();
					$("#spanchoiceInDepart").hide();
				}
				
				deportBinding({
					elementId:"position_left_0",
					showNum:20,
					includeRegion:true
				});
			}
			var callback = function(msg){
				if($("#businessType_edit").is(":visible")){
					var obj = eval("("+msg+")");
					var nodes = obj.inInfo;
					binOLSTJCS05.regionTree = null;
					binOLSTJCS05.loadTree(nodes);
				}else{
					var nodesObj = eval("(" + msg + ")");
					binOLSTJCS05.regionTree = null;
					binOLSTJCS05.loadTree(nodesObj);
				}
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback
			});
		}
};

var binOLSTJCS05 = new BINOLSTJCS05();

$(document).ready(function(){
	binOLSTJCS05.search();
	deportBinding({
		elementId:"name",
		showNum:20,
		includeRegion:true,
		includeDepart:true
	});
	/*
	deportBinding({
		elementId:"code",
		showNum:20,
		selected:"code",
		includeRegion:true
	});
	*/
});