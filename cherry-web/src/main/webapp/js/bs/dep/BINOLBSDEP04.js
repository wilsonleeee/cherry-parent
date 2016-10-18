
window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(function(){
	
	if (window.opener) {
    	window.opener.lockParentWindow();
	}
	
	// 表单验证初期化
	cherryValidate({
		formId: 'addOrganizationInfo',
		rules: {
			departCode: {required: true,maxlength: 15},
			departName: {required: true,maxlength: 30},
			type: {required: true},
			testType: {required: true},
			departNameShort: {maxlength: 20},
			nameForeign: {maxlength: 30},
			nameShortForeign: {maxlength: 30},
			addressLine1: {required: true,maxlength: 100},
			addressLine2: {maxlength: 100},
			zipCode: {alphanumeric: true},
			expiringDateDate : {dateValid:true},
			expiringDateTime : {timeHHmmssValid:true}
		}
	});
	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
	// 县级市选择
	$("#county").click(function(){
		// 显示县级市列表DIV
		bscom03_showRegin(this,"countyTemp");
	});
	
	// 到期日_date绑定日期控件
	$('#expiringDateDate').cherryDate({
		minDate : new Date()
	});
	// 到期日_time绑定日期控件
	$("#expiringDateTime").timepicker({
        timeFormat: "HH:mm:ss", // 设置时分秒格式
		showSecond: true, // 显示秒时间轴
		timeOnlyTitle: $("#timeOnlyTitle").text(),
		currentText: $("#currentText").text(),
		closeText: $("#closeText").text()
	});
	
});

// 保存部门前对form进行验证
function saveWithValid() {
	if($('#addOrganizationInfo').valid()) {
		saveOrganization();
	}
}

// 把参数拼成json对象形式的字符串
function getJsonParam(object) {
	var m = [];
	$(object).find(':input').each(function(){
		if(this.value && this.value != '') {
			if($(this).attr("type") == 'checkbox') {
				if(this.checked) {
					m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent($.trim(this.value).replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				}
			} else {
				m.push('"'+encodeURIComponent(this.name)+'":"'+encodeURIComponent($.trim(this.value).replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			}
		}
	});
	return '{'+m.toString()+'}';
}

// 保存部门
function saveOrganization() {
	$('#departBasic :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var param = $('#departBasic :input').serialize();
	
	if($("#higheOrgDiv :input").length > 0) {
		param = param + "&" + $("#higheOrgDiv :input").serialize();
	}
	param = param + "&" + $('#lowerCounterDiv :input').serialize();
	var addParam = [];
	$('#departAddress').find('.divItem').each(function(){
		addParam.push(getJsonParam(this));
	});
	param = param + '&departAddress=' + '[' + addParam.toString() + ']';
	
	var contactParam = [];
	$('#departContact').find('table').each(function(){
		contactParam.push(getJsonParam(this));
	});
	param = param + '&departContact=' + '[' + contactParam.toString() + ']';
	
//	var higherOrg = $('#departBasic :input[name=path]').val();
//	var oldHigherOrg = $('#departBasic :input[name=higherDepartPath]').val();
	var organizationId = $('#departBasic :input[name=organizationId]').val();
//	var lowerCounterDivLen = $('#lowerCounterDiv :input').length;
	var refreshPrivilegeUrl = $("#refreshPrivilegeUrl").val();
	var callback = function(msg) {
		if($('#actionResultBody').length > 0) {
			if(refreshPrivilegeUrl) {
				window.opener.cherryAjaxRequest({
					url: refreshPrivilegeUrl,
					param: null,
					reloadFlg : true,
					callback: function(msg) {
					}
				});
			}
			if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
			if(window.opener.document.getElementById('organizationTree')) {
				window.opener.bsdep01_refreshOrgNode();
				window.opener.bsdep01_organizationDetail(organizationId);
//				if(organizationId) {
//					window.opener.bsdep01_organizationDetail(organizationId);
//					if(lowerCounterDivLen > 0) {
//						window.opener.bsdep01_refreshOrgNode(null);
//					} else {
//						window.opener.bsdep01_refreshOrgNode(higherOrg);
//						if(oldHigherOrg != higherOrg) {
//							window.opener.bsdep01_refreshOrgNode(oldHigherOrg);
//						}
//					}
//				} else {
//					if(lowerCounterDivLen > 0) {
//						window.opener.bsdep01_refreshOrgNode(null);
//					} else {
//						window.opener.bsdep01_refreshOrgNode(higherOrg);
//					}
//				}
			}
		}
	};
	cherryAjaxRequest({
		url: $('#addOrganizationUrl').attr("href"),
		param: param,
		callback: callback,
		formId: '#organizationCherryForm'
	});
}

//function changeHigherOrgUpd(object,select_default) {
//	
//	if(object.value == $('#higherDepartPath').val()) {
//		$('#higherPositionSelect').parent().hide();
//	} else {
//		var callback = function(msg){
//			$('#higherPositionSelect').empty();
//			$('#higherPositionSelect').append('<option value="">'+select_default+'</option>');
//			var jsons = eval('('+msg+')');
//			if(jsons.length > 0) {
//				for(var i in jsons) {
//					$('#higherPositionSelect').append('<option value="'+jsons[i].positionPath+'">'+jsons[i].positionName+'</option>');
//				}
//			}
//			$('#higherPositionSelect').parent().show();
//		};
//		cherryAjaxRequest({
//			url: $('#searchPosUrl').attr("href"),
//			param: $(object).serialize(),
//			callback: callback,
//			formId: '#organizationCherryForm'
//		});
//	}
//}

//// 选择上级部门
//function changeHigherOrg() {
//	var thisVal = $('#organizationTypeId').val();
//	$('#organizationTypeId').empty();
//	$type = $('#higherOrganizationId').next().find('span');
//	for(var i = 0; i < $type.length; i++) {
//		if($type[i].id == $('#higherOrganizationId').val()) {
//			var $grade = $('#typeGrade').find('span');
//			for(var j = 0; j < $grade.length; j++) {
//				if($($type[i]).text() == $grade[j].id) {
//					var text = $($grade[j]).text();
//					$('#selectTypeId').find('div').each(function(){
//						if(parseInt(this.id,10) > parseInt(text,10)) {
//							$('#organizationTypeId').append('<option value="'+$(this).find('span').attr("id")+'">'+$(this).find('span').text()+'</option>');
//						}
//					});
//					$('#organizationTypeId').val(thisVal);
//					break;
//				}
//			}
//			break;
//		}
//	}
//	if($('#organizationTypeId').length > 0) {
//		changeType($('#organizationTypeId')[0]);
//	}
//}

//// 选择部门类型
//function changeType(object) {
//	if(object.value == '4') {
//		$('#counterInfo').show();
//		$('#selectCounter').show();
//		if($('#counterInfo').find('div.divItem').length == 0) {
//			var callback = function(msg){
//				$('#counterInfo').html(msg);
//				$('#counterInfo').find('div.divItem').first().append($('#regin-city-subCity-div').html());
//			};
//			var param = null;
//			if($('#brandInfoId').length > 0) {
//				param = $('#brandInfoId').serialize();
//			}
//			cherryAjaxRequest({
//				url: $('#counterInitUrl').attr("href"),
//				param: param,
//				callback: callback,
//				formId: '#organizationCherryForm'
//			});
//		}
//	} else {
//		$('#counterInfo').hide();
//		$('#selectCounter').hide();
//	}
//}

// 添加地址
function addAddress(object) {
    var len = $('#departAddressTemp').find(':input[name=addressTypeId]').find('option').length;
	if($(object).parent().next().find('.divItem').length >= len) {
		return false;
	}
	$(object).parent().next().append($('#departAddressTemp').html());
	$(object).parent().next().find('.divItem').last().append($('#regin-city-subCity-div').html());
}

// 删除地址
function removeAddress(object) {
	$(object).parents('.divItem').remove();
}

// 设定默认地址
function changeDefaulAdd(object) {
	if(object.checked) {
		$(object).parents('.divItem').siblings().find(':checkbox').prop("checked",false);
	}
}

// 添加部门联系人
function addContact(object) {
    var len = 5;
	if($(object).parent().next().find('table').length >= len) {
		return false;
	}
	$(object).parent().next().append($('#departContactTemp').html());
}

// 删除部门联系人
function removeContact(object) {
	$(object).parents('table').remove();
}

// 设定默认联系人
function changeDefaulContact(object) {
	if(object.checked) {
		$(object).parents('table').siblings().find(':checkbox').prop("checked",false);
	}
}

// 区域选择
function changeRegin(object,reginDiv) {

	var opos = $(object).offset();
	var oleft = parseInt(opos.left, 10);
	var otop = parseInt(opos.top + $(object).outerHeight(), 10);
	var $table = $(object).parents('div.divItem');
	var $reginDiv = $table.find('div.'+reginDiv);
	if($reginDiv.is(':hidden') && $reginDiv.find('li').length > 0) {
		$reginDiv.css({'left': oleft + "px", 'top': otop });
		$reginDiv.show();
		
		if(reginDiv != 'provinceDiv') {
			$table.find('div.provinceDiv').hide();
		}
		if(reginDiv != 'cityDiv') {
			$table.find('div.cityDiv').hide();
		}
		if(reginDiv != 'countyDiv') {
			$table.find('div.countyDiv').hide();
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


// 省份城市县级市选择条件清空
function empty_province(object, text){
	
	var $table = $(object).parents('div.divItem');
	// 省设置“请选择 ”
	$table.find('span.provinceText').text(text);
	// 省hidden值清空
	$table.find(':input[name=province]').val('');
	// 城市县级市选择条件清空
	empty_city($table,text);
}

// 城市县级市选择条件清空
function empty_city($table,text) {
	
	// 市设置“请选择 ”
	$table.find('span.cityText').text(text);
	// 市hidden值清空
	$table.find(':input[name=city]').val('');
	// 市内容清空
	$table.find('div.cityDiv').empty();
	// 县级市选择条件清空
	empty_county($table,text);
}

// 县级市选择条件清空
function empty_county($table,text) {
	
	// 县设置“请选择 ”
	$table.find('span.countyText').text(text);
	// 县hidden值清空
	$table.find(':input[name=county]').val('');
	// 市内容清空
	$table.find('div.countyDiv').empty();
}

// AJAX取得城市
function getCity(object,city,county){
	var $table = $(object).parents('div.divItem');
	var id = $(object).attr('id');
	var text = $(object).text();
	// 设置省名称
	$table.find(':input[name=province]').val(id);
	// 省hidden值修改
	$table.find('span.provinceText').text(text);
	var i18nText = $('#selectInitText').text();
	// 城市县级市选择条件清空
	empty_city($table, i18nText);
	if(id == undefined || id == '') {
		return false;
	}
	var url = $('#querySubRegionUrl').val();
	var param = getParentToken() + '&regionId=' + $(object).attr('id');
	var callback = function(msg) {
		if(msg) {
			// 全部
			var defaultText = $('#defaultText').text();
			var json = eval('('+msg+')');
			var str = '<ul class="u2"><li onclick="clickCity(this);">'+defaultText+'</li>';
		    for (var one in json){
			    var regionId = json[one]["regionId"];
			    var regionName = escapeHTMLHandle(json[one]["regionName"]);
		        str = str + '<li id="'+regionId+'" onclick="clickCity(this);">'+regionName+'</li>';
		    }
		    $table.find('div.cityDiv').html(str);
		    if(city){
		    	var $city = $table.find('div.cityDiv').find('li[id='+city+']');
		    	clickCity($city[0],county);
		    }
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback
	});
}

// AJAX取得县级市
function clickCity(object,county) {
	var $table = $(object).parents('div.divItem');
	var id = $(object).attr('id');
	var text = $(object).text();
	// 设置市名称
	$table.find(':input[name=city]').val(id);
	// 市hidden值修改
	$table.find('span.cityText').text(text);
	if($table.parent('#counterInfo').length > 0) {
		var i18nText = $('#selectInitText').text();
		// 县级市选择条件清空
		empty_city($table, i18nText);
		if(id == undefined || id == '') {
			return false;
		}
		var url = $('#querySubRegionUrl').val();
		var param = getParentToken() + '&regionId=' + $(object).attr('id');
		var callback = function(msg) {
			if(msg) {
				// 全部
				var defaultText = $('#defaultText').text();
				var json = eval('('+msg+')');
				var str = '<ul class="u2"><li onclick="clickCounty(this);">'+defaultText+'</li>';
			    for (var one in json){
				    var regionId = json[one]["regionId"];
				    var regionName = escapeHTMLHandle(json[one]["regionName"]);
			        str = str + '<li id="'+regionId+'" onclick="clickCounty(this);">'+regionName+'</li>';
			    }
			    $table.find('div.countyDiv').html(str);
			    if(county){
			    	var $county = $table.find('div.countyDiv').find('li[id='+county+']');
			    	clickCounty($county);
			    }
			}
		};
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: callback
		});
	}
}

function clickCounty(object) {
	var $table = $(object).parents('div.divItem');
	// 设置市名称
	$table.find(':input[name=county]').val($(object).attr('id'));
	// 市hidden值修改
	$table.find('span.countyText').text($(object).text());
}

//function bsdep04_changeBrandInfoOld(object,select_default) {
//	$('#departBasic :input').each(function(){
//		if(this.name == 'brandInfoId' || this.name == 'path' || this.name == 'type') {
//		} else {
//			$(this).val('');
//		}
//	});
//	var i18nText = $('#selectInitText').text();
//	$('#counterInfo').find('span.provinceText').text(i18nText);
//	$('#counterInfo').find('span.cityText').text(i18nText);
//	$('#counterInfo').find('span.countyText').text(i18nText);
//	$('#departAddress').find('div.section-content').empty();
//	$('#departContact').find('div.section-content').empty();
//	var callback = function(msg){
//		// 默认地区
//		var defaultTitle = $('#defaultTitle').text();
//		// 全部
//		var defaultText = $('#defaultText').text();
//		var jsons = eval('('+msg+')');
//		$('#higherOrganizationId').empty();
//		$('#higherOrganizationId').next().empty();
//		if(jsons.higherOrganizationList.length > 0) {
//			for(var i in jsons.higherOrganizationList) {
//				$('#higherOrganizationId').append('<option value="'+jsons.higherOrganizationList[i].path+'">'+jsons.higherOrganizationList[i].departName+'</option>');
//				$('#higherOrganizationId').next().append('<span id="'+jsons.higherOrganizationList[i].path+'">'+jsons.higherOrganizationList[i].type+'</span>');
//			}
//		}
//		
//		$('#regin-city-subCity-div').find('div.provinceDiv').empty();
//		if(jsons.reginList.length > 0) {
//			var html = '<div class="clearfix"><span class="label">'+defaultTitle+'</span><ul class="u2"><li onclick="getCity(this);return false;">'+defaultText+'</li></ul></div>';
//			for(var i in jsons.reginList) {
//				html += '<div class="clearfix"><span class="label">'+jsons.reginList[i].reginName+'</span><ul class="u2">';
//				for(var j in jsons.reginList[i].provinceList) {
//					html += '<li id="'+jsons.reginList[i].provinceList[j].provinceId+'" onclick="getCity(this);">'+jsons.reginList[i].provinceList[j].provinceName+'</li>';
//				}
//				html += '</ul></div>';
//			}
//			$('#regin-city-subCity-div').find('div.provinceDiv').html(html);
//		}
//		
//		// 部门类型选择范围初期化
//		changeHigherOrg();
//	};
//	cherryAjaxRequest({
//		url: $('#filterByBrandInfoUrl').attr("href"),
//		param: $(object).serialize(),
//		callback: callback,
//		formId: '#organizationCherryForm'
//	});
//}

//function selectCounter() {
//	var param = $('#counterInfoId').serialize();
//	if($('#brandInfoId').length > 0) {
//		param += '&' + $('#brandInfoId').serialize();
//	}
//	popDataTableOfCounter(null,param);
//}
//
//function bsdep99_selectContact(object) {
//	var param = $(object).parents('table').find(':input[name=employeeId]').serialize();
//	if($('#brandInfoId').length > 0) {
//		param += '&' + $('#brandInfoId').serialize();
//	}
//	bsdep99_popDataTableOfEmployee(object,param);
//}

function bsdep04_changeBrandInfo(object,text) {
	
	$('#departAddress').find('div.section-content').empty();
	$('#departContact').find('div.section-content').empty();
	// 清空上级部门
	$('#higheOrgDiv').find('tbody').empty();
	// 清空下级柜台
	$('#lowerCounterDiv').find('tbody').empty();
	// 清空省信息
	$('#provinceId').val("");
	$("#provinceText").text(text);
	$('#provinceTemp').empty();
	// 清空城市信息
	$('#cityId').val("");
	$("#cityText").text(text);
	$('#cityTemp').empty();
	// 清空县级市信息
	$('#countyId').val("");
	$("#countyText").text(text);
	$('#countyTemp').empty();
	// 清空仓库信息
	$('#depotInfoId').empty();
	$('#depotInfoId').append('<option value="">'+text+'</option>');
	$('#regin-city-subCity-div').find('div.provinceDiv').empty();
	
	var callback = function(msg){
		// 默认地区
		var defaultTitle = $('#defaultTitle').text();
		// 全部
		var defaultText = $('#defaultText').text();
		var jsons = eval('('+msg+')');
		if(jsons.reginList.length > 0) {
			var html = '<div class="clearfix"><span class="label">'+defaultTitle+'</span><ul class="u2"><li onclick="getCity(this);return false;">'+defaultText+'</li></ul></div>';
			for(var i in jsons.reginList) {
				html += '<div class="clearfix"><span class="label">'+escapeHTMLHandle(jsons.reginList[i].reginName)+'</span><ul class="u2">';
				for(var j in jsons.reginList[i].provinceList) {
					html += '<li id="'+jsons.reginList[i].provinceList[j].provinceId+'" onclick="getCity(this);">'+escapeHTMLHandle(jsons.reginList[i].provinceList[j].provinceName)+'</li>';
				}
				html += '</ul></div>';
			}
			$('#regin-city-subCity-div').find('div.provinceDiv').html(html);
			
			var html = '<div class="clearfix"><span class="label">'+defaultTitle+'</span><ul class="u2"><li onclick="bscom03_getNextRegin(this, \''+text+'\', 1);return false;">'+defaultText+'</li></ul></div>';
			for(var i in jsons.reginList) {
				html += '<div class="clearfix"><span class="label">'+escapeHTMLHandle(jsons.reginList[i].reginName)+'</span><ul class="u2">';
				for(var j in jsons.reginList[i].provinceList) {
					html += '<li id="'+jsons.reginList[i].provinceList[j].provinceId+'" onclick="bscom03_getNextRegin(this, \''+text+'\', 1);">'+escapeHTMLHandle(jsons.reginList[i].provinceList[j].provinceName)+'</li>';
				}
				html += '</ul></div>';
			}
			$('#provinceTemp').html(html);
		}
		// 仓库信息存在
		if(jsons.depotInfoList.length > 0) {
			for(var i in jsons.depotInfoList) {
				$('#depotInfoId').append('<option value="'+jsons.depotInfoList[i].depotInfoId+'">'+escapeHTMLHandle(jsons.depotInfoList[i].depotNameCN)+'</option>');
			}
		}
		// 部门代码存在
		if(jsons.departCode) {
			$('#departCode').val(jsons.departCode);
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#organizationCherryForm'
	});
}

function bsdep04_changeDepotType(object) {
	if($(object).val() == 0) {
		$('#depotInfoId').hide();
	} else {
		$('#depotInfoId').show();
		$('#depotInfoId').val("");
	}
}