/**
 * @author zgl
 * 
 */
function BINOLSTJCS04() {
};

BINOLSTJCS04.prototype = {

	"tree" : null,

	"dialogHtml" : "",

	"search" : function() {
		if(arguments[0] == undefined){
			$("#actionResultDisplay").html("");
			$("#errorDiv #errorSpan").html("");
			$("#errorDiv").hide();
		}
		var url = $("#search").html();
		var $form = $("#mainForm");
		// 表单验证
		if (!$form.valid()) {
			return;
		}
		var params = $form.serialize();
		url = url + "?" + params;
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#dataTable',
			// 数据URL
			url : url,
			// 表格列属性设置
			// 表格默认排序
			aaSorting : [ [ 2, "asc" ] ],
			aoColumns : [ {
				"sName" : "checkbox",
				"sWidth" : "1%",
				"bSortable" : false
			}, {
				"sName" : "no.",
				"sWidth" : "5%",
				"bSortable" : false
			}, // 0
					{
						"sName" : "inventoryCode",
						"sWidth" : "25%"
					}, // 1
					{
						"sName" : "departCode",
						"sWidth" : "25%"
					}, // 2
					{
						"sName" : "brandName",
						"sWidth" : "10%",
						"bVisible" : false
					}, // 3
					{
						"sName" : "defaultFlag",
						"sWidth" : "5%",
						"sClass":"center"
					}, // 4
					{
						"sName" : "comments",
						"sWidth" : "30%"
					},{
						"sName" : "comments",
						"sWidth" : "10%",
						"bSortable":false,
						"sClass":"center"
					} ], // 5

			// 不可设置显示或隐藏的列
			aiExclude : [ 0, 1,2 ],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			// 固定列数
			fixedColumns : 3
		};
		// 调用获取表格函数
		getTable(tableSetting);
		$("#deportOption a").unbind("click");
		$("input[name=allSelect]").prop("checked", false);
	},

	"loadTree" : function(nodes) {
		var treeNodes = eval("(" + nodes + ")");
		var treeSetting = {
			checkable : true,
			showLine : true,
			checkType : {
				"Y" : "",
				"N" : ""
			}
		};
		this.tree = null;
		this.tree = $("#treeDemo").zTree(treeSetting, treeNodes);
	},

	"popAddDialog" : function() {
		$("#actionResultDisplay").html("");
		$("#errorDiv #errorSpan").html("");
		$("#errorDiv").hide();
		var that = this;
		var comfirmTitle = $("#yes").val();
		var dialogTitle = $("#dialogTitle").val();
		var dialogSetting = {
			bgiframe : true,
			width : 450,
			height: "auto",
			minHeight:630,
			zIndex : 9999,
			modal : true,
			resizable : false,
			title : dialogTitle,
			buttons : [ {
				text : comfirmTitle,
				click : function() {
					that.setInvDepRelation();
				}
			},
			{
				text : $("#cancel").val(),
				click : function() {
					that.closeDialog("reaInvenDepartBind_main","reaInvenDepartBind_body");
				}
			}],
			close : function(event, ui) {
				that.closeDialog("reaInvenDepartBind_main",
						"reaInvenDepartBind_body");
			}
		};
		this.dialogHtml = $("#reaInvenDepartBind_main").html();
		$('#reaInvenDepartBind_body').dialog(dialogSetting);
		
		departBinding({
			elementId:"position_left_0",
			showNum:20,
			selected:"name",
			includeCounter:false
		});
		
		$("#position_left_0").bind("keydown",function(event){
			if(event.keyCode==13){
				that.locationDepPosition(this,"keydown");
	        }
		});
		
		this.getAddTree();
	},

	"popEditDialog" : function(obj) {
		var that = this;
		var comfirmTitle = $("#yes").val();
		var dialogTitle = $("#dialogTitle").val();
		var dialogSetting = {
			bgiframe : true,
			width : 470,
			height : 280,
			zIndex : 9999,
			modal : true,
			resizable : false,
			title : dialogTitle,
			buttons : [ {
				text : comfirmTitle,
				click : function() {
					that.saveEditCom();
				}
			},
			{
				text : $("#cancel").val(),
				click : function() {
				that.closeDialog("reaInvenDepartEdit_main","reaInvenDepartEdit_body");
				}
			} ],
			close : function(event, ui) {
				that.closeDialog("reaInvenDepartEdit_main",
						"reaInvenDepartEdit_body");
			}
		};
		this.dialogHtml = $("#reaInvenDepartEdit_main").html();
		$('#reaInvenDepartEdit_body').dialog(dialogSetting);
		this.setEditCom(obj);
	},

	"closeDialog" : function(dialogParent, dialogDiv) {
		$("#" + dialogDiv).dialog("destroy");
		$("#" + dialogDiv).remove();
		$("#" + dialogParent).html(this.dialogHtml);
	},

	"getAddTree" : function() {
		var that = this;
		var url = $("#getAddTreeNodes").html();
		var param = "brandInfoId="
				+ $('#reaInvenDepartBind_body').find(
						"#brandInfoId option:selected").val()
				+ "&inventoryInfoId="
				+ $('#reaInvenDepartBind_body').find(
						"#inventoryInfoId option:selected").val();
		var callback = function(msg) {
			that.loadTree(msg);
		};
		cherryAjaxRequest( {
			url : url,
			param : param,
			callback : callback
		});
	},

	"setInvDepRelation" : function() {
		var that = this;
		var _thisDepot = $('#reaInvenDepartBind_body').find("#inventoryInfoId option:selected").val();
		var _thisDepotId = _thisDepot.split("*")[0];
		var _thisTestType = _thisDepot.split("*")[1];
		var url = $("#setInvDepRelation").html();
		var checkedNodes = this.tree.getCheckedNodes();
		var departInfo = [];
		for ( var key in checkedNodes) {
			if(checkedNodes[key].testType != _thisTestType){
				this.showWarm(_thisTestType, "reaInvenDepartBind_body_add");
				return false;
			}
			var o = {
				organizationId : checkedNodes[key].id
			};
			departInfo.push(o);
		}
		if(departInfo.length == 0){
			$("#errorDiv_dialog").show();
			return false;
		}
		var param = "brandInfoId="
				+ $('#reaInvenDepartBind_body').find(
						"#brandInfoId option:selected").val()
				+ "&inventoryInfoId="
				+ _thisDepotId
				+ "&departInfo=" + JSON2.stringify(departInfo)
				+ "&defaultFlag=" + $("input[name=defaultFlag]:checked").val()
				+ "&comments="
				+ $('#reaInvenDepartBind_body').find("#comments").val();
		var callback = function(msg) {
			that.closeDialog("reaInvenDepartBind_main",
					"reaInvenDepartBind_body");
			that.search("flag");
		};
		cherryAjaxRequest( {
			url : url,
			param : param,
			callback : callback
		});
	},

	"showWarm":function(testType,id){
		if(testType == "1"){
			$("#"+id+" #errorSpan1").html($("#testWarming").val());
			$("#"+id+" #errorDiv1").show();
		}else{
			$("#"+id+" #errorSpan1").html($("#officialWarming").val());
			$("#"+id+" #errorDiv1").show();
		}
	},
	
	"deleteInvDepRelation" : function() {
		var that = this;
		var url = $("#deleteInvDepRelation").html();
		var identityIdArr = [];
		$("#dataTable_Cloned").find("input[name=validFlag]:checked").each(
				function() {
					var id = this.id;
					var idArr = id.split("*");
					var identityId = idArr[0];
					var o = {
						identityId : identityId
					};
					identityIdArr.push(o);
				});
		var param = "identityIdArr=" + JSON2.stringify(identityIdArr);
		var callback = function() {
			removeDialog("#dialogInit");
			that.search();
		};
		cherryAjaxRequest( {
			url : url,
			param : param,
			callback : callback
		});
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
				that.deleteInvDepRelation();
			},
			cancelEvent : function() {
				removeDialog("#dialogInit");
			}
		};
		openDialog(dialogSetting);
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
		var num = $("#dataTable_Cloned").find("input[name=validFlag]:checked").length;
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

	"setEditCom" : function(obj) {
		var _this = obj;
		var id = _this.id;
		var value = $(_this).attr("value");
		var idArr = id.split("*");
		var identityId = idArr[0];
		var inventoryInfoId = idArr[1];
		var organizationId = idArr[2];
		var defaultFlag = idArr[3];
		var depotTestType = idArr[4];
		var departTestType = idArr[5];
		$("#inventoryInfoId_edit").attr("value", inventoryInfoId+"*"+depotTestType);
		$("#organizationId_edit").attr("value", organizationId+"*"+departTestType);
		$("input[name=defaultFlag_edit]").each(function() {
			if ($(this).val() == defaultFlag) {
				$(this).prop("checked", true);
			}
		});
		$("#comments_edit").val(value);
		$("#identityId_edit").val(identityId);
	},

	"saveEditCom" : function() {
		var that = this;
		var _thisDepotArr = $("#inventoryInfoId_edit option:selected").val().split("*");
		var _thisdepartArr = $("#organizationId_edit option:selected").val().split("*");
		var _thisDepotId = _thisDepotArr[0];
		var _thisDepotTestType = _thisDepotArr[1];
		var _thisDepartId = _thisdepartArr[0];
		var _thisDepartTestType = _thisdepartArr[1];
		if(_thisDepotTestType != _thisDepartTestType){
			this.showWarm(_thisDepotTestType, "reaInvenDepartEdit_body");
			return false;
		}
		var url = $("#saveEditCom").html();
		var param = "brandInfoId="
				+ $("#mainForm").find("#brandInfoId option:selected").val()
				+ "&identityId=" + $("#identityId_edit").val()
				+ "&inventoryInfoId="
				+ _thisDepotId
				+ "&organizationId="
				+ _thisDepartId
				+ "&defaultFlag="
				+ $("input[name=defaultFlag_edit]:checked").val()
				+ "&comments=" + $("#comments_edit").val();
		var callback = function() {
			that.closeDialog("reaInvenDepartEdit_main",
					"reaInvenDepartEdit_body");
			that.search("flag");
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
		var inputNodes = this.tree.getNodesByParamFuzzy("name",")"+$input.val());
		this.tree.expandNode(inputNodes[0],true,false);
		this.tree.selectNode(inputNodes[0]);
	}

};

var binOLSTJCS04 = new BINOLSTJCS04();

$(document).ready(function(){
	binOLSTJCS04.search();
	departBinding({
		elementId:"departName",
		showNum:20,
		selected:"name",
		includeCounter:false
	});
	deportBinding({
		elementId:"inventoryName",
		showNum:20,
		selected:"name",
		onlyNoneCounterDeport:true
	});
});