/*
 * 全局变量定义
 */
var SCF08_global = {};
//是否需要解锁
SCF08_global.needUnlock = true;

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
});

window.onbeforeunload = function(){
	if (SCF08_global.needUnlock) {
		if(window.opener){
			window.opener.unlockParentWindow();
		}
	}
};

function doClose(){
	window.close();
	window.opener.plscf06_searchCode();
}

// 编辑按钮
function scf08Edit(){
	document.getElementById("shwoMainInfo").style.display = "none";
	document.getElementById("shwoListInfo").style.display = "none";
	document.getElementById("editOption").style.display = "none";
	document.getElementById("saveOption").style.display = "block";
	document.getElementById("editMainInfo").style.display = "block";
	document.getElementById("editListInfo").style.display = "block";
	document.getElementById("actionResultDisplay").style.display = "none";
	
}

//返回
function scf08DoBack(){
	var parentToken = parentTokenVal();
	$("#parentCsrftoken1").val(parentToken);
	var url = document.getElementById("doBack").innerHTML;
	var param = $("#coderForm").serialize()+"&csrftoken="+$("#parentCsrftoken1").val();
	var callback = function(msg){
		$("body").html(msg);
	};
	cherryAjaxRequest({
		url:url,
		param:param,
		callback:callback
	});
}

function scf08AllSelected(obj){
	var _this = obj;
	var flag = _this.checked;
	var subCheckbox = document.getElementsByName("checkbox");
	for(var key in subCheckbox){
		subCheckbox[key].checked = flag;
	}
}

function scf08SingleSelected(obj){
	var parentToken = parentTokenVal();
	$("#parentCsrftoken1").val(parentToken);
	var _this = obj;
	var flag = _this.checked;
	var checkedLength = 0;
	if(!flag){
		document.getElementById("allSelect").checked=flag;
	}else{
		checkedLength = scf08GetCheckedNum();
		var subCheckbox = document.getElementsByName("checkbox"); 
		if(checkedLength == subCheckbox.length){
			document.getElementById("allSelect").checked=flag;
		}
	}
}

function scf08GetCheckedNum(){
	var subCheckbox = document.getElementsByName("checkbox"); 
	var checkedLength = 0;
	for(var i=0 ; i<subCheckbox.length; i++){
		if(subCheckbox[i].checked == true){
			checkedLength++;
		}
	}
	return checkedLength;
}

function scf08DeleteCode(){
	var checkedLength = 0;
	checkedLength = scf08GetCheckedNum();
	if(checkedLength > 0){
		var subCheckbox = document.getElementsByName("checkbox");
		var codeId = [];
		for(var i=0 ; i<subCheckbox.length; i++){
			if(subCheckbox[i].checked == true){
				var idArr = subCheckbox[i].id.split("*");
				var o = {
						codeType : idArr[0],
						codeKey : idArr[1]
				};
				codeId.push(o);
			}
		}
		var url = document.getElementById("deleteCode").innerHTML;
		var param = $("#coderForm").serialize()+"&deleteCodeArr="+JSON2.stringify(codeId);
		param += (param ? "&" : "") + "csrftoken=" + getTokenVal();
		var callback = function(msg){
			$("body").html(msg);
		};
		cherryAjaxRequest({
			url:url,
			param:param,
			callback:callback
		});
	}else{
		return false;
	}
}

function scf8SaveEdit(){
	
	var vailRet = vailtData();
	if(vailRet == "error1"){
		$("#errorSpan").html($("#error1").html());
		$("#errorDiv").show();
		return false;
	}else if(vailRet == "error2"){
		$("#errorSpan").html($("#error2").html());
		$("#errorDiv").show();
		return false;
	}
	
	var parentToken = parentTokenVal();
	$("#parentCsrftoken1").val(parentToken);
	var url = document.getElementById("saveEdit").innerHTML;
	var param = $("#coderForm").serialize()+"&"+$("#editListInfo").serialize()+"&"+$("#editMainInfo").serialize();
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
			var codeKey =$(this).find("#codeKeyArr").val();
			if($.trim(codeKey) == ""){
				$(this).prop("class","errTRbgColor");
				flag = "error1";
			}else if(tempData[$.trim(codeKey)]){
				$(this).prop("class","errTRbgColor");
				$(tr[tempData[$.trim(codeKey)]]).prop("class","errTRbgColor");
				flag = "error2";
			}else{
				tempData[$.trim(codeKey)] = i;
				$(this).removeProp("class");
			}
		}
	});
	return flag;
}

function scf08Delete(obj){
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

function scf08IsInteger(obj){
	var $this = $(obj);
	var patrn=/^[1-9]{1}[0-9]{0,7}$/; 
	if (!patrn.exec($this.val())){
		$this.val("");
	}
}