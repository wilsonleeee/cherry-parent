var BINOLSTJCS02 = function(){};

BINOLSTJCS02.prototype={
		
		"dialogBody":"",
		
		// 显示省、市或县级市信息
	    "showRegin" : function(object, reginDiv) {
	    	var $reginDiv = $('#'+reginDiv);
	    	if($reginDiv.is(':hidden') && $reginDiv.find('li').length > 0) {
	    		var opos = $(object).offset();
	    		var oleft = parseInt(opos.left, 10);
	    		var otop = parseInt(opos.top + $(object).outerHeight(), 10);
	    		$reginDiv.css({'left': oleft + "px", 'top': otop });
	    		$reginDiv.show();
	    		
	    		if(reginDiv != 'provinceTemp') {
	    			$('#provinceTemp').hide();
	    		}
	    		if(reginDiv != 'cityTemp') {
	    			$('#cityTemp').hide();
	    		}
	    		if(reginDiv != 'countyTemp') {
	    			$('#countyTemp').hide();
	    		}
	    		var firstFlag = true;
	    		$("body").unbind('click');
	    		// 隐藏弹出的DIV
	    		$("body").bind('click',function(event){
	    			if(!firstFlag) {
	    				$reginDiv.hide();
	    				$("body").unbind('click');
	    			}
	    			firstFlag = false;
	    		});
	    	}
	    },
	    
	    // 省、市、县级市的联动查询
	    "getNextRegin":function(obj, text, grade) {
	    	var $obj = $(obj);
	    	// 区域ID
	    	var id =  $obj.attr("id");
	    	// 区域名称
	    	var name =  $obj.text();
	    	// 下一级标志
	    	var nextGrade = 1;
	    	$("#cityText").parent().parent().removeClass('error');
	    	$("#cityText").parent().parent().find('#errorText').remove();
	    	// 选择省的场合
	    	if(grade == 1) {
	    		// 设置省名称
	    		$("#provinceText").text(name);
	    		// 省hidden值修改
	    		if(id && id.indexOf("_") > 0) {
	    			var arrayId = id.split("_");
	    			$("#regionId").val(arrayId[0]);
	    			$("#provinceId").val(arrayId[1]);
	    			id = arrayId[1];
	    		} else {
	    			$("#provinceId").val(id);
	    		}
	    		// 城市不存在的场合
	    		if($('#cityId').length == 0) {
	    			return false;
	    		}
	    		// 清空城市信息
	    		$('#cityId').val("");
	    		$("#cityText").text(text);
	    		$('#cityTemp').empty();
	    		// 清空县级市信息
	    		$('#countyId').val("");
	    		$("#countyText").text(text);
	    		$('#countyTemp').empty();
	    		nextGrade = 2;
	    	} 
	    	// 选择城市的场合
	    	else if(grade == 2) {
	    		// 设置城市名称
	    		$("#cityText").text(name);
	    		// 城市hidden值修改
	    		$("#cityId").val(id);
	    		// 县级市不存在的场合
	    		if($('#countyId').length == 0) {
	    			return false;
	    		}
	    		// 清空县级市信息
	    		$('#countyId').val("");
	    		$("#countyText").text(text);
	    		$('#countyTemp').empty();
	    		nextGrade = 3;
	    	} 
	    	// 选择县级市的场合
	    	else if(grade == 3) {
	    		// 设置县级市名称
	    		$("#countyText").text(name);
	    		// 县级市hidden值修改
	    		$("#countyId").val(id);
	    		return false;
	    	}
	    	if(id == undefined || id == '') {
	    		return false;
	    	}
	    	var url = $('#querySubRegionUrl').val();
	    	var param = 'regionId=' + id;
	    	var callback = function(msg) {
	    		if(msg) {
	    			// 全部
	    			var defaultText = $('#defaultText').text();
	    			var json = eval('('+msg+')'); 
	    			var str = '<ul class="u2"><li onclick="binOLSTJCS02.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
	    		    for (var one in json){
	    		        str += '<li id="'+json[one]["regionId"]+'" onclick="binOLSTJCS02.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
	    		    }
	    		    str += '</ul>';
	    		    if(grade == 1) {
	    		    	$("#cityTemp").html(str);
	    		    } else if(grade == 2) {
	    		    	$("#countyTemp").html(str);
	    		    }
	    		}
	    	};
	    	cherryAjaxRequest({
	    		url: url,
	    		param: param,
	    		callback: callback
	    	});
	    },
	    
	    "popBelongToDepart":function(obj,param){
	    	var $this = $(obj);
	    	//测试区分
	    	var testType = $("#testType option:selected").val();
	    	//取得所有部门类型
	     	var departType = "";
	     	for(var i=0;i<$("#departTypePop option").length;i++){
	     		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
	     		//不绑定柜台部门
	     		if(departTypeValue != "4"){
	     			departType += "&departType="+departTypeValue;
	     		}
	     	}
	     	param = param + "&privilegeFlg=1&businessType=1" + departType+"&testType="+testType;
	    	var that = this;
	    	var callback = function(){
	    		var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
	    		var departId = "";
	    		var departName = "";
	    		if(checkedRadio){
	    			departId = checkedRadio.val();
		    		departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
	    		}
	    		$("#organizationID").val(departId);
	    		$("#showRelDepartName").html(departName.unEscapeHTML());
	    	};
	    	var option={
	    			checkType:"radio",
	    			brandInfoId:$("#brandInfoId option:selected").val(),
	    			param:param,
	    			click:callback
	    	};
	    	popAjaxDepDialog(option);
	    },
	    
	    //弹出绑定部门部门窗口
	    "popBindDepart":function(obj,param){
	    	var $this = $(obj);
	    	//测试区分
	    	var testType = $("#testType option:selected").val();
	    	//取得所有部门类型
	     	var departType = "";
	     	for(var i=0;i<$("#departTypePop option").length;i++){
	     		var departTypeValue = $("#departTypePop option:eq("+i+")").val();
	     		//不绑定柜台部门
	     		if(departTypeValue != "4"){
	     			departType += "&departType="+departTypeValue;
	     		}
	     	}
	     	param = param + "&privilegeFlg=1&businessType=1" + departType+"&testType="+testType;
	    	var that = this;
	    	var option={
	    			checkType:"checkbox",
	    			brandInfoId:$("#brandInfoId option:selected").val(),
	    			param:param,
	    			targetId: "bindDepart_tbody",
	    			bindFlag:true,
	    			getHtmlFun:that.getRowHtml
	    	};
	     	popAjaxDepDialog(option);
	    },
	    
	    //取得添加部门仓库关系行html
	    "getRowHtml":function(info){
	    	var departId = info.departId;
	    	var departCode = info.departCode;
	    	var departName = info.departName;
	    	var departType = info.departType;
	    	var html = [];
	    	html.push('<tr>');
	    	html.push('<input type="hidden" name="departId" value="'+departId+'" />');
	    	html.push('<td><span>'+departCode.unEscapeHTML()+'</span><input type="hidden" name="relOrganizationIDArr" value='+departId+'></input></td>');
	    	html.push('<td><span>'+departName.unEscapeHTML()+'</span></td>');
	    	html.push('<td><span>'+departType.unEscapeHTML()+'</span></td>');
	    	html.push('<td><span>'+$("#defaultFlag_div").html()+'</span></td>');
	    	html.push('<td><span><input class="text" name="commentArr" style="width:90%" maxLength="100"/></span></td>');
	    	html.push('<td><a href="#" id='+departId+' class="delete center" onclick="binOLSTJCS02.deleteDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">'+$("#deleteTitle").val()+'</span></a></td>');
	    	html.push('</tr>');
	    	return html.join("");
	    },
	    
	    //删除部门仓库关系
	    "deleteDepart":function(obj){
	    	var $this = $(obj);
	    	$this.parent().parent().remove();
	    },
	    
	    //保存添加
	    "save":function(){
	    	if(!$('#mainForm').valid()) {
				return false;
			}
	    	var url = $("#saveUrl").prop("href");
	    	var param = $('#mainForm').serialize();
	    	var callback = function(msg){
	    		if($('#actionResultBody').length > 0) {
					if(window.opener.oTableArr[0] != null)window.opener.BINOLSTJCS01.search();
				}
	    	};
	    	cherryAjaxRequest({
				url: url,
				param: param,
				callback:callback
			});
	    },
	    
	    //关闭窗口
	    "close" : function() {
			window.close();
		},
		
		//当测试类型改变时清空部门信息（包括归属部门和关联部门）
		"clearDepartInfo":function(){
			$("#organizationID").val("");
			$("#showRelDepartName").html("");
			$("#bindDepart_tbody").html("");
		}
};

var binOLSTJCS02 = new BINOLSTJCS02();

$(document).ready(function(){
	binOLSTJCS02.dialogBody = $("#departDialog").html();
	if (window.opener) {
	       window.opener.lockParentWindow();
	    }
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			depotCode: {required: true,maxlength: 10},
			depotNameCN:{required: true,maxlength: 60},
			testType: {required: true},
			brandInfoId: {required: true},
			depotNameCN: {maxlength: 60}
		}
	});
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		binOLSTJCS02.showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		binOLSTJCS02.showRegin(this,"cityTemp");
	});
	// 县级市选择
	$("#county").click(function(){
		// 显示县级市列表DIV
		binOLSTJCS02.showRegin(this,"countyTemp");
	});
});

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};