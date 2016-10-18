
function BINOLMBSVC01_init() {
	
};

BINOLMBSVC01_init.prototype = {
		/* 是否刷新一览画面 */
		"doRefresh" : false,
		/* 是否打开父页面锁定*/
		"needUnlock" : true,
		"updateRule":function(){
			if (!$('#ruleForm').valid()) {
				return false;
			};
			// 参数序列化
			var param = null;
			// 基本信息
			$("#baseInfo").find(":input").each(function() {
				if ($(this).attr("name") && !$(this).is(":disabled")) {
					$(this).val($.trim(this.value));
					if (null == param) {
						param = $(this).serialize();
					} else {
						param += "&" + $(this).serialize();
					}
				}
			});
			
			// 规则内容
			$("#detailInfo").find(":input").each(function() {
				if ($(this).attr("name") && !$(this).is(":disabled")) {
					$(this).val($.trim(this.value));
					if (null == param) {
						param = $(this).serialize();
					} else {
						param += "&" + $(this).serialize();
					}
				}
			});
			
			// 使用范围
			var allowNodesArray = new Array();
			var allowNodes = BINOLMBSVC01_1_tree.getCheckedNodes(true);
			for(var i=0; i<allowNodes.length; i++){
				//只添加柜台节点
				if(allowNodes[i].nodes==undefined){
					var nodeObj = {
							id : allowNodes[i].id
						};
						allowNodesArray.push(nodeObj);
				}
			}
			if (null == param) {
				param = "allowNodesArray="+JSON2.stringify(allowNodesArray);
			} else {
				param += "&" + "allowNodesArray="+JSON2.stringify(allowNodesArray);
			}
			
			var subDiscountId=$('#subDiscountId').val();
			var discountId = $('#discountId').val();
			var updateRuleUrl=$('#updateRuleUrl').attr("href");
			param += "&discountId=" + discountId + "&subDiscountId=" + subDiscountId;
			cherryAjaxRequest({
				url: updateRuleUrl,
				param: param,
				callback: function(data) {
					BINOLMBSVC01_init.doRefresh=true;
				}
			});
		},
		
		/**
		 * 初始化加载树
		 * @return
		 */
		"loadTree":function(nodes){
			$("#position_left_0").val("");
			var treeNodes = eval("(" + nodes + ")");
			var treeSetting = {
				checkable : true,
				showLine : true
			};
			BINOLMBSVC01_1_tree = null;
			BINOLMBSVC01_1_tree = $("#treeDemo").zTree(treeSetting, treeNodes);
			$("#dataTable_processing").addClass("hide");
		},

		/**
		 * 定位
		 */
		"locationCutPosition" : function(obj, flag) {
			if (typeof flag == "undefined") {
				var $input = $(obj).prev();
			} else {
				var $input = $(obj);
			}
			var inputNode = BINOLMBSVC01_1_tree.getNodeByParam("name", $input.val());
			BINOLMBSVC01_1_tree.expandNode(inputNode, true, false);
			BINOLMBSVC01_1_tree.selectNode(inputNode);
		},
		
		/**
		 * 加载渠道柜台树
		 */
		"loadChannelRegion" : function(msg) {
			var that = this;
			var channelRegionId = $("#channelRegionId").val();
			// 取得大区信息List
			var regionIdInfo = eval('('+msg+')');
			if(regionIdInfo && regionIdInfo.length > 0) {
				var regionSelect = '<select name="channelRegionId" id="channelRegionId"><option value="">'+$("#select_default").html()+'</option>';
				for(var i = 0; i < regionIdInfo.length; i++) {
					regionSelect += '<option value="'+regionIdInfo[i].regionId+'">'+regionIdInfo[i].regionName+'</option>';
				}
				regionSelect += '</select>';
				$('#channelRegionDiv').find("span").html(regionSelect);
				// 显示大区选择框
				$("#channelRegionDiv").removeClass("hide");
				if(channelRegionId != undefined && channelRegionId != ''){
					$('#channelRegionDiv').find(":input[name=channelRegionId]").val(channelRegionId);
				}
				// 选择框的change事件（加载指定大区的渠道柜台树）
				$('#channelRegionDiv').find("#channelRegionId").change(function() {
					var channelUrl = $("#getChannelCntTreeUrl").attr("href");
					
					var discountId = $("#discountId").val();
					var channelParam = "privilegeFlag=1&"+ $("#channelRegionId").serialize();
					if(undefined != discountId && '' != discountId) {
						channelParam += "&discountId="+discountId;
					}
					$("#dataTable_processing").removeClass("hide");
					var channelCallback = function (msg) {
						// 加载指定大区的渠道柜台树
						that.loadTree(msg);
					};
					cherryAjaxRequest({
						url: channelUrl,
						param: channelParam,
						callback: channelCallback
					});
				});
				//初始执行一次change事件
				$('#channelRegionDiv').find("#channelRegionId").trigger("change");
			}
		},

		
		/**
		 * 取得树的数据，加到树上
		 * @return
		 */
		"ajaxGetNodes" : function() {
			var selMode = $("#selMode").val();
			var that = this;
			var url = $("#getTreeUrl").attr("href");
			var discountId = $("#discountId").val();
			var param = "privilegeFlag=1&"+ $("#selMode").serialize();
			if(undefined != discountId && '' != discountId) {
				param += "&discountId="+discountId;
			}
			$("#dataTable_processing").removeClass("hide");
			var callback = function(msg){
					if(selMode == "1" || selMode == "3"){
						//正常加载树
						$("#channelRegionDiv").find("span").html("");
						$("#channelRegionDiv").addClass("hide");
						that.loadTree(msg);
					}else {
						// 加载渠道柜台树 
						that.loadChannelRegion(msg);
					}
					$("#dataTable_processing").addClass("hide");
				};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback,
				coverId: "#div_main"
			});
		}
};

window.onbeforeunload = function(){
	if (window.opener) {
		if (BINOLMBSVC01_init.doRefresh) {
			// 刷新父页面
			window.opener.BINOLMBSVC01.searchList();
		}
		if(BINOLMBSVC01_init.needUnlock){
			// 解除父页面锁定
			window.opener.unlockParentWindow();
		}
	}   
};



var BINOLMBSVC01_init =  new BINOLMBSVC01_init();

var BINOLMBSVC01_init_tree;

var treeSetting = {
	checkable : true,
	showLine : true,
	expandSpeed : ""
};

//初始化
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	
	// 初始化树
	BINOLMBSVC01_init.ajaxGetNodes();

	counterBinding({
		elementId : "position_left_0",
		showNum : 20,
		selected : "name"
	});

	$("#position_left_0").bind("keydown", function(event) {
		if (event.keyCode == 13) {
			BINOLMBSVC01_init.locationCutPosition(this, "keydown");
		}
	});
	
	$('#discountBeginDate').cherryDate({
		beforeShow : function(input) {
			var value = $('#discountEndDate').val();
			return [ value, 'maxDate' ];
		}
	});
	$('#discountEndDate').cherryDate({
		beforeShow : function(input) {
			var value = $('#discountBeginDate').val();
			return [ value, 'minDate' ];
		}
	});
	
	cherryValidate({
		formId: "ruleForm",		
		rules: {
			"rechargeRule.ruleName" : {required: true, maxlength: 20},
			"rechargeRule.discountBeginDate": {required: true},
			"rechargeRule.discountEndDate": {required: true},
			"rechargeRule.giftAmount": {pointValid : [10,2],required: true}// 可为0
	   }		
	});
	
	BINOLMBSVC01_init.needUnlock = true;
});
