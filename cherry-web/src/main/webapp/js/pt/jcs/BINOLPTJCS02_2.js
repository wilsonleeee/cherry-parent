/*
 * 全局变量定义
 */
var JCS02_2_global = {};
//是否需要解锁
JCS02_2_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
});

window.onbeforeunload = function(){
	if (JCS02_2_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
	//window.opener.plscf06_searchCode();
	window.opener.BINOLPTJCS02.search(false);
}

// 编辑按钮
function jcs02_2Edit(){
	document.getElementById("shwoMainInfo").style.display = "none";
	document.getElementById("shwoListInfo").style.display = "none";
	document.getElementById("editOption").style.display = "none";
	document.getElementById("saveOption").style.display = "block";
	document.getElementById("editMainInfo").style.display = "block";
	document.getElementById("editListInfo").style.display = "block";
	document.getElementById("actionResultDisplay").style.display = "none";
}

//返回
function jcs02_2DoBack(){
	var parentToken = parentTokenVal();
	$("#parentCsrftoken1").val(parentToken);
	var url = document.getElementById("doBack").innerHTML;
	var param = $("#extDefValForm").serialize()+"&csrftoken="+$("#parentCsrftoken1").val();
	var callback = function(msg){
		$("body").html(msg);
	};
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callback
	});
}

function jcs02_2SaveEdit(){
	var vailRet = vailtData();
	if(vailRet == "error1"){
		$("#errorSpan").html($("#error1").html());
		$("#errorDiv").show();
		return false;
	}else if(vailRet == "error2"){
		$("#errorSpan").html($("#error2").html());
		$("#errorDiv").show();
		return false;
	}else if(vailRet == "error3"){
		$("#errorSpan").html($("#error3").html());
		$("#errorDiv").show();
		return false;
	}
	
	var parentToken = parentTokenVal();
	$("#parentCsrftoken1").val(parentToken);
	var url = document.getElementById("saveEdit").innerHTML;
	var param = $("#extDefValForm").serialize()+"&"+$("#editListInfo").serialize()+"&"+$("#editMainInfo").serialize();
	var callback = function(msg){
		if(msg.indexOf('class="errorMessage"')==-1){
			$("body").html(msg);
			$("#actionResultDisplay").show();
		}
	};
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callback
	});
}

function vailtData(){
	var tempData = {};
	var flag = "";
	var tr = $("#editListTable").find("tr");
	tr.each(function(i){
		if( i > 0 ){
			var value1 = $(this).find("#value1Arr").val();
			if($.trim(value1) == ""){
				$(this).prop("class","errTRbgColor");
				flag = "error1";
			}else if(tempData[$.trim(value1)]){
				$(this).prop("class","errTRbgColor");
				$(tr[tempData[$.trim(value1)]]).prop("class","errTRbgColor");
				flag = "error2";
			}else{
				tempData[$.trim(value1)] = i;
				$(this).removeProp("class");
			}
		}
	});
	var viewType = $("#viewType").val();
	if(tr.length < 3 && viewType == "checkbox"){
		flag = "error3";
	}
	return flag;
}

function jcs02_2Delete(obj){
	var _this = obj;
	var trNode = _this.parentNode.parentNode;
	$(trNode).remove();
}

function addNewCode(){
	var tableNode = document.getElementById("editListTable");
	var _parentNode = tableNode.childNodes;
	var editTbody,editTr;
	for(var i in _parentNode){
		if(_parentNode[i].nodeType==1){
			editTbody = _parentNode[i];
			break;
		}
	}
	$(editTbody).append($("#addNewCode tbody").html());
}

function jcs02_2IsInteger(obj){
	var $this = $(obj);
	var patrn=/^[1-9]{1}[0-9]{0,7}$/; 
	if (!patrn.exec($this.val())){
		$this.val("");
	}
}