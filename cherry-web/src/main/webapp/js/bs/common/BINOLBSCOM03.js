
// 省、市、县级市的联动查询
function bscom03_getNextRegin(obj, text, grade) {
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
			var str = '<ul class="u2"><li onclick="bscom03_getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
		    for (var one in json){
		        str += '<li id="'+json[one]["regionId"]+'" onclick="bscom03_getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
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
}

// 显示省、市或县级市信息
function bscom03_showRegin(object, reginDiv) {
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
}

// 删除确认
function bscom03_deleteConfirm(flag, url, param, callback) {
	var _param = "";
	if(flag == 'enable') {
		_param = 'validFlag=1';
		var title = $('#enableTitle').text();
		var text = $('#enableMessage').html();
	} else {
		_param = 'validFlag=0';
		var title = $('#disableTitle').text();
		var text = $('#disableMessage').html();
	}
	if(param != null && param != "") {
		_param = _param + "&" + param;
	}
	var dialogSetting = {
		dialogInit: "#dialogInit",
		text: text,
		width: 	500,
		height: 300,
		title: 	title,
		confirm: $("#dialogConfirm").text(),
		cancel: $("#dialogCancel").text(),
		confirmEvent: function(){bscom03_deleteHandle(url, _param, callback);},
		cancelEvent: function(){removeDialog("#dialogInit");}
	};
	openDialog(dialogSetting);
}

// 删除处理
function bscom03_deleteHandle(url, param, delCallback) {
	var callback = function(msg) {
		$("#dialogInit").html(msg);
		if($("#errorMessageDiv").length > 0) {
			$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
			$("#dialogInit").dialog( "option", {
				buttons: [{
					text: $("#dialogClose").text(),
				    click: function(){removeDialog("#dialogInit");}
				}]
			});
		} else {
			removeDialog("#dialogInit");
			if(typeof(delCallback) == "function") {
				delCallback();
			}
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// 选择记录
function bscom03_checkRecord(object, id) {
	$("#errorMessage").empty();
	var $id = $(id);
	if($(object).attr('id') == "checkAll") {
		if(object.checked) {
			$id.find(':checkbox').prop("checked",true);
		} else {
			$id.find(':checkbox').prop("checked",false);
		}
	} else {
		if($id.find(':checkbox:not([checked])').length == 0) {
			$id.prev().find('#checkAll').prop("checked",true);
		} else {
			$id.prev().find('#checkAll').prop("checked",false);
		}
	}
}