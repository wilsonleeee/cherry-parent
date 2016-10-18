

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
		formId: 'addPositionInfo',
		rules: {
			positionCategoryId: {required: true},
			positionName: {required: true,maxlength: 50},
			positionNameForeign: {maxlength: 50},
			foundationDate: {dateValid: true},
			positionDESC: {maxlength: 200}
		}
	});
	
	// 日历初期化
	$('#addPositionInfo :input[name=foundationDate]').cherryDate();
	
	// 改变岗位类型事件初期执行
	changePosType();
	
});

// 保存岗位
function savePosition() {
	// 保存前对表单进行验证
	if(!$('#addPositionInfo').valid()) {
		return false;
	}
	
	$('#addPositionInfo :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var param = $('#addPositionInfo :input').serialize();
	
	var higherPos = $('#addPositionInfo :input[name=path]').val();
	var oldHigherPos = $('#addPositionInfo :input[name=higherPositionPath]').val();
	var positionId = $('#addPositionInfo :input[name=positionId]').val();
	var callback = function(msg) {
		if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		if(window.opener.document.getElementById('positionTree')) {
			if(positionId) {
				window.opener.positionDetail(positionId);
				window.opener.refreshPosNode(higherPos);
				if(oldHigherPos != higherPos) {
					window.opener.refreshPosNode(oldHigherPos);
				}
			} else {
				window.opener.refreshPosNode(higherPos);
			}
		}
	};
	cherryAjaxRequest({
		url: $('#addPositionUrl').attr("href"),
		param: param,
		callback: callback,
		formId: '#positionCherryForm'
	});
}

// 改变品牌时过滤其他下拉框数据
function bspos04_changeBrand(object,select_default) {
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#organizationId').empty();
		if(jsons.orgList.length > 0) {
			for(var i in jsons.orgList) {
				$('#organizationId').append('<option value="'+jsons.orgList[i].departId+'">'+jsons.orgList[i].departName+'</option>');
			}
		}
		$('#path').empty();
		if(jsons.higherPositionList && jsons.higherPositionList.length > 0) {
			for(var i in jsons.higherPositionList) {
				$('#path').append('<option value="'+jsons.higherPositionList[i].path+'">'+jsons.higherPositionList[i].positionName+'</option>');
			}
		}
		$('#positionCategoryId').empty();
		$('#positionCategoryId').append('<option value="">'+select_default+'</option>');
		if(jsons.positionCategoryList.length > 0) {
			for(var i in jsons.positionCategoryList) {
				$('#positionCategoryId').append('<option value="'+jsons.positionCategoryList[i].positionCategoryId+'">'+jsons.positionCategoryList[i].ategoryName+'</option>');
			}
		}
		$('#resellerInfoId').empty();
		$('#resellerInfoId').append('<option value="">'+select_default+'</option>');
		if(jsons.resellerInfoList.length > 0) {
			for(var i in jsons.resellerInfoList) {
				$('#resellerInfoId').append('<option value="'+jsons.resellerInfoList[i].resellerInfoId+'">'+jsons.resellerInfoList[i].resellerName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#positionCherryForm'
	});
}

// 改变部门时过滤上级岗位数据
function bspos04_changeOrg(object, select_default) {
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#path').empty();
		if(jsons.higherPositionList.length > 0) {
			for(var i in jsons.higherPositionList) {
				$('#path').append('<option value="'+jsons.higherPositionList[i].path+'">'+jsons.higherPositionList[i].positionName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByOrgUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#positionCherryForm'
	});
}

// 改变岗位类型
function changePosType() {
	var value = $('#positionType').val();
	if(value == '1' || value == '2') {
		$('#resellerInfoIdTh').show();
		$('#resellerInfoIdTd').show();
	} else {
		$('#resellerInfoIdTh').hide();
		$('#resellerInfoIdTd').hide();
	}
}

function bspos04_addEmployee() {
	
	$('#employeeBody').append($('#employees ').html());
	
}

function bspos04_delEmployee(obj) {
	$(obj).parents("tr").remove();
}

