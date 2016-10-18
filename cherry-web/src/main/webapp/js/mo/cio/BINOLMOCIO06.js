/**
 * @author user3
 */
var BINOLMOCIO06 = function(){};

BINOLMOCIO06.prototype = {
		
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
			var issuedType = $('input:radio[name="issued"]:checked').val();
			$ajaxLoading.ajaxStart(function(){$(this).show();});
			$ajaxLoading.ajaxComplete(function(){$(this).hide();});
			$.ajaxFileUpload({
				url : url,
				secureuri : false,
				data : {
					'csrftoken' : getTokenVal(),
					'brandInfoId' : $("#form_brandInfoId").val(),
					'organizationInfoId' : $("#form_organizationInfoId").val(),
					'paperId' : $("#form_paperId").val(),
					'isIssued' : $("#form_isIssued").val(),
					'paperType' : $("#form_paperType").val(),
					'paperName' : $("#form_paperName").val(),
					'maxPoint' : $("#form_maxPoint").val(),
					'issuedType' : issuedType
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
					$("div").removeClass("container");
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

var BINOLMOCIO06 = new BINOLMOCIO06();

var BINOLMOCIO06_tree;
var treeSetting = {
		checkable : true,
		showLine : true
};
window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};
/**
 * 加载树
 * 
 * 
 * */
function loadTree(str){
	var treeNodes = eval("(" + str + ")");
//	var startTime=(new Date()).getTime();
	BINOLMOCIO06_tree=$("#treeDemo").zTree(treeSetting,treeNodes);
//	var endTime=(new Date()).getTime();
//	var time = endTime-startTime;
//	alert("用时："+time+"毫秒");
}

function getTreeNodes(){
	var url = $("#getTreeNodes").html();
	var param = "paperId="+$("#form_paperId").val()+"&organizationInfoId="+$("#form_organizationInfoId").val()+"&brandInfoId="+$("#form_brandInfoId").val();
	var callback=function(msg){
		loadTree(msg);
	};
	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : callback
	});
}

function changeRadio(val){
	var url = $("#changeRadio").html();
	var param = "issuedType="+val+"&paperId="+$("#paperId").html()+"&organizationInfoId="+$("#organizationInfoId").html()+"&brandInfoId="+$("#brandInfoId").html();
	var callback=function(msg){
		loadTree(msg);
	};
	cherryAjaxRequest( {
		url : url,
		param : param,
		callback : callback
	});
}

/**
 * 问卷下发
 * 
 * */
function issuedPaper(){
	var checkedCounter = [];
	var unCheckedCounter = [];
	var url = $("#issuedPaper").html();
	var nodes = BINOLMOCIO06_tree.getCheckedNodes();
	for(var i in nodes){
		if(nodes[i].nodes == undefined){
			var o={
					organizationId:nodes[i].id
			};
			checkedCounter.push(o);
		}
	}
	var nodes2 = BINOLMOCIO06_tree.getCheckedNodes(false);
	for(var i in nodes2){
		if(nodes2[i].nodes == undefined){
			unCheckedCounter.push(nodes2[i].id);
		}
	}
	var param = $("#form").serialize() + "&issuedType="
			+ $("input[name='issued']:checked").val() + "&checkedCounter="
			+ JSON2.stringify(checkedCounter) + "&unCheckedCounter="
			+ JSON2.stringify(unCheckedCounter) + "&csrftoken=" + getTokenVal();
	cherryAjaxRequest( {
		url : url,
		param : param,
		isResultHandle:true,
		callback:function(msg){
		if($('#actionResultBody').length > 0) {
			if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		}
		$("div").removeClass("container");
	}
	});
}

function locationCutPosition(obj,flag){
	if(typeof flag == "undefined"){
		var $input = $(obj).prev();
	}else{
		var $input = $(obj);
	}
	var inputNode = BINOLMOCIO06_tree.getNodeByParam("name",$input.val());
	BINOLMOCIO06_tree.expandNode(inputNode,true,false);
	BINOLMOCIO06_tree.selectNode(inputNode);
}

$(document).ready(function(){
	getTreeNodes();
	counterBinding({
		elementId:"position_left_0",
		showNum:20,
		selected:"codeName"
	});
	
	$("#position_left_0").bind("keydown",function(event){
		if(event.keyCode==13){
			locationCutPosition(this,"keydown");
        }
	});
});
