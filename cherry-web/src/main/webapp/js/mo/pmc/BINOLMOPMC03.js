var BINOLMOPMC03 = function(){
	
};

BINOLMOPMC03.prototype = {
		
		/**
		 * 取得树的数据，加到树上
		 */
		"ajaxGetNodes" : function(){
			var selMode = $("#selMode").val();
			var that = this;
			var url = $("#getCounterTreeUrl").attr("href");
			var param = $("#treeForm").serialize();
			param += (param ? "&" : "") + "privilegeFlag=1&"
				+ $("#selMode").serialize() + "&csrftoken=" + getTokenVal();
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
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback,
				coverId: "#div_main"
			});
		},
		
		/**
		 * 按渠道模式显示柜台时加载指定大区的柜台树
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
					var channelParam = $("#treeForm").serialize();
						channelParam += (channelParam ? "&" : "") + "privilegeFlag=1&"
								+ $("#channelRegionId").serialize()
								+ "&csrftoken=" + getTokenVal();
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
		 * 加载树
		 */
		"loadTree" : function(msg) {
			$("#position_left_0").val("");
			var nodesObj = eval("(" + msg + ")");
			var treeSetting = {
					checkable : true,
					showLine : true,
					open : true
			};
			BINOLMOPMC03_tree = null;
			BINOLMOPMC03_tree = $("#treeDemo").zTree(treeSetting, nodesObj);
			$("#dataTable_processing").addClass("hide");
		},
		
		/**
		 * 定位树节点
		 */
		"locationPosition" : function(obj,flag) {
			if(typeof flag == "undefined"){
				// 点击定位按钮
				var $input = $(obj).prev();
			}else{
				// 直接ENTER键
				var $input = $(obj);
			}
			var inputNode = BINOLMOPMC03_tree.getNodeByParam("name",$input.val());
			BINOLMOPMC03_tree.expandNode(inputNode,true,false);
			BINOLMOPMC03_tree.selectNode(inputNode);
		},
		
		"save" : function() {
			// 清空消息
			$('#actionResultDisplay').html('');
			// 选择的柜台节点
			var checkNodesArray = new Array();
			// 查看所有节点的改变状态(此方法只有根节点)
			var counterNodes = BINOLMOPMC03_tree.getCheckedNodes();
			for(var i=0; i<counterNodes.length; i++){
				//只添加柜台节点(取得其柜台ID)
				if(counterNodes[i].nodes==undefined && counterNodes[i].counterInfoId != '0'){
					var nodeObj ={
							counterInfoID : counterNodes[i].counterInfoId
						};
					checkNodesArray.push(nodeObj);
				}
			}
			var url = $("#saveCounterTreeUrl").attr("href");
			var param = $("#treeForm").serialize() + 
				"&checkNodesArray="+JSON2.stringify(checkNodesArray) + "&csrftoken=" + getTokenVal();
			// 显示进度条
			$("#dataTable_processing").removeClass("hide");
			var callback = function(msg) {
				$("#dataTable_processing").addClass("hide");
				if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
			};
			cherryAjaxRequest( {
				url : url,
				param : param,
				callback : callback,
				coverId: "#div_main"
			});
		},
		
		/**
		 * 柜台信息EXCEL文件上传
		 */
		"ajaxFileUpload" : function(url) {
			// AJAX登陆图片
	    	$ajaxLoading = $("#loading");
	    	// 错误信息提示区
	    	$errorMessage = $('#errorMessage');
	    	// 清空错误信息
	    	$errorMessage.empty();
	    	if($('#upExcel').val()==''){
	    		var errHtml = this.getErrHtml($('#errmsg1').val());
	    		$errorMessage.html(errHtml);
	    		$("#pathExcel").val("");
	    		return false;
	    	}
	    	$ajaxLoading.ajaxStart(function(){$(this).show();});
	    	$ajaxLoading.ajaxComplete(function(){$(this).hide();});
			$.ajaxFileUpload({
				url : url,
				secureuri : false,
				data : {
					'csrftoken' : getTokenVal(),
					'privilegeFlag' : "1",
					'menuGrpID' : $("#menuGrpID").val(),
					'startDate' : $("#startDate").val(),
					'endDate' : $("#endDate").val(),
					'machineType' : $("#machineType").val()
				},
				fileElementId : 'upExcel',
				dataType : 'html',
				success : function(data) {
					if(data.indexOf("success")>-1){
						// 导入成功替换整个页面
						$("#div_main").html(data);
						if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
					}else {
						$errorMessage.html(data);
					}
					
				}
			});
		},
		
		// 取得错误信息HTML
		"getErrHtml" : function (text){
			var errHtml = '<div class="actionError"><ul><li><span>';
			errHtml += text;
			errHtml += '</span></li></ul></div>';
			return errHtml;
		},
		
		/**
	     * 删除掉画面头部的提示信息框
	     * @return
	     */
		// 清空错误信息
	    "deleteActionMsg" : function (){
	    	$('#errorMessage').empty();
	    }
			
};

var BINOLMOPMC03 = new BINOLMOPMC03();

var BINOLMOPMC03_tree;

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
	if (window.opener) {
        window.opener.lockParentWindow();
    }
	// 画面布局初期化处理
	BINOLMOPMC03.ajaxGetNodes();
	
	counterBinding({
		elementId:"position_left_0",
		showNum:20,
		selected:"codeName"
	});
	
	$("#position_left_0").bind("keydown",function(event){
		if(event.keyCode==13){
			BINOLMOPMC03.locationPosition(this,"keydown");
        }
	});
});