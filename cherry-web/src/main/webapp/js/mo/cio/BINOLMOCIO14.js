var BINOLMOCIO14 = function () {};

BINOLMOCIO14.prototype = {

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
		BINOLMOCIO14_tree = null;
		BINOLMOCIO14_tree = $("#treeDemo").zTree(treeSetting, treeNodes);
		$("#dataTable_processing").addClass("hide");
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
				var channelUrl = $("#getChannelCntTree_url").attr("href");
				var channelParam = "brandInfoId="+$("#brandInfoId").val()+"&counterMessageId="+$("#counterMessageId").val();
				channelParam += "&privilegeFlag=1&"+ $("#channelRegionId").serialize() + "&csrftoken=" + getTokenVal();
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
		var url = $("#getTree_url").attr("href");
		var param="brandInfoId="+$("#brandInfoId").val()+"&counterMessageId="+$("#counterMessageId").val();
		param +="&privilegeFlag=1&"+ $("#selMode").serialize() + "&csrftoken=" + getTokenVal();
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
	},
	
	/**
	 * 发布
	 * @return
	 */
	"publish":function(){
		//清空消息
		$('#actionResultDisplay').html('');
		var allowNodesArray = new Array();
		var forbiddenNodesArray = new Array();
		var flagArrow = $("input@[id=radioAllow]").prop("checked");
		var flagForbidden = $("input@[id=radioForbidden]").prop("checked");
		var allowNodes = BINOLMOCIO14_tree.getCheckedNodes(flagArrow?true:false);
		var forbiddenNodes = BINOLMOCIO14_tree.getCheckedNodes(flagForbidden?true:false);
		for(var i=0;i<allowNodes.length;i++){
			//只添加柜台节点
			if(allowNodes[i].nodes==undefined){
				var nodeObj ={
						id : allowNodes[i].id
					};
					allowNodesArray.push(nodeObj);
			}
		}
		for(var i=0;i<forbiddenNodes.length;i++){
			//只添加柜台节点
			if(forbiddenNodes[i].nodes==undefined){
				var nodeObj ={
						id : forbiddenNodes[i].id
				};
				forbiddenNodesArray.push(nodeObj);
			}
		}

		var url = $("#publish_url").attr("href");
		var param = "brandInfoId=" + $("#brandInfoId").val()+"&counterMessageId="+$("#counterMessageId").val();
		param += "&radioControlFlag="+$("input@[name=radioControlFlag]:checked").val()+"&allowNodesArray="+JSON2.stringify(allowNodesArray)+"&forbiddenNodesArray="+JSON2.stringify(forbiddenNodesArray);
		param += "&csrftoken=" + getTokenVal();
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
	
	"locationCutPosition" : function (obj,flag){
		if(typeof flag == "undefined"){
			var $input = $(obj).prev();
		}else{
			var $input = $(obj);
		}
		var inputNode = BINOLMOCIO14_tree.getNodeByParam("name",$input.val());
		BINOLMOCIO14_tree.expandNode(inputNode,true,false);
		BINOLMOCIO14_tree.selectNode(inputNode);
	},
	
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
				'brandInfoId' : $('#brandInfoId').val(),
				'privilegeFlag' : "1",
				'counterMessageId' : $("#counterMessageId").val(),
				'radioControlFlag' : $("input@[name=radioControlFlag]:checked")
						.val()
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

var BINOLMOCIO14 = new BINOLMOCIO14();

var BINOLMOCIO14_tree;

var treeSetting = {
	checkable : true,
	showLine : true,
	expandSpeed : ""
};

window.onbeforeunload = function(){
    if (window.opener) {
        window.opener.unlockParentWindow();
    }
};

$(function(){
    if (window.opener) {
        window.opener.lockParentWindow();
    }
    BINOLMOCIO14.ajaxGetNodes();
    
    counterBinding({
		elementId:"position_left_0",
		showNum:20,
		selected:"name"
	});
	
	$("#position_left_0").bind("keydown",function(event){
		if(event.keyCode==13){
			BINOLMOCIO14.locationCutPosition(this,"keydown");
        }
	});
});