var binOLSSPRM19_global = {};
// 当前点击对象
binOLSSPRM19_global.thisClickObj = {};
binOLSSPRM19_global.dialogBody = "";
/**
 * 页面初期处理
 */

$(document).ready(function() {

	binOLSSPRM19_global.dialogBody = $('#promotionDialog').html();
});

/**
 * 打开促销产品查询box
 * 
 * @param thisObj
 * @return
 */
function SSPRM19_openPromotionSearchBox(thisObj) {
	SSPRM19_clearActionMsg();
	if ($('#inOrganizationId').val() == null
			|| $('#inOrganizationId').val() == ""
			|| $("#outOrganizationId").val() == ""
			|| $("#outOrganizationId").val() == null) {
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if ($('#inDepotId').val() == null || $('#inDepotId').val() == "") {
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00042').val());
		$('#errorDiv2').show();
		return false;
	}
	changeOddEvenColor();
	var option = {
		targetId : "databody",
		checkType : "checkbox",
		prmCate : 'CXLP',
		mode : 2,
		brandInfoId : $("#brandInfoId").val(),
		isStock : "1",// 显示管理库存的促销品
		getHtmlFun : function(info) {
			var proId = info.proId;
			var unitCode = info.unitCode;
			var barCode = info.barCode;
			var nameTotal = info.nameTotal;
			var price = info.standardCost;
			var html = '<tr>';
			html += '<td><input type="checkbox" onclick="SSPRM19_changechkbox(this);" value="'
					+ proId + '"></td>';
			html += '<td style="white-space: nowrap;"><span id="unicodeSpan" class="left">'
					+ unitCode + '</span></td>';
			html += '<td>' + barCode + '</td>';
			html += '<td>' + nameTotal + '</td>';
			html += '<td style="text-align: right;">' + price + '</td>';
			html += '<td><input onchange="SSPRM19_changeCount(this);" class="text-number" style="width:95%;" maxlength="9" name="quantityuArr" /></td>';
			html += '<td style="text-align: right;">0.00</td>';
			html += '<td><input value="" maxlength="200" style="width:95%;" name="reasonArr"></td>';
			html += '<td style="display: none;">';
			html += '<input type="hidden" name="priceUnitArr" value="' + price
					+ '"/>';
			html += '<input type="hidden" name="prmVendorId" value="' + proId
					+ '"/>';
			html += '<input type="hidden" name="promotionProductVendorIDArr" value="'
					+ proId + '"/>';
			html += '</td></tr>';
			return html;
		},
		click : function() {
			changeOddEvenColor();
			// 设置全选状态
			var $checkboxs = $('#databody').find(':checkbox');
			var $unChecked = $('#databody').find(':checkbox:not(":checked")');
			if ($unChecked.length == 0 && $checkboxs.length > 0) {
				$('#allSelect').prop("checked", true);
			} else {
				$('#allSelect').prop("checked", false);
			}
		}
	};
	popAjaxPrmDialog(option);
}

/**
 * 更改了调入部门
 * 
 */
function SSPRM19_chooseInDepart() {
	SSPRM19_clearActionMsg();
	var param = 'inOrganizationId=' + $('#inOrganizationId').val();
	$("#outOrganizationId").val("");
	$("#outOrgName").html("&nbsp");
	// 取得仓库
	cherryAjaxRequest({
		url : $("#s_getDepot").html(),
		param : param,
		callback : SSPRM19_getDepotSuccess
	});
}

/**
 * 修改仓库下拉框
 * 
 * @param data
 * @return
 */
function SSPRM19_getDepotSuccess(data) {

	// data为json格式的文本字符串
	var member = eval("(" + data + ")"); // 包数据解析为json 格式
	$("#inDepotId").empty();
	$.each(member, function(i) {
		$("#inDepotId").append(
				"<option value='" + member[i].BIN_InventoryInfoID + "'>"
						+ escapeHTMLHandle(member[i].InventoryName)
						+ "</option>");
	});

	// //取得调出部门
	// var param
	// ='positionId='+$('#positionId').val()+'&inOrganizationId='+$('#inOrganizationId').val();
	// cherryAjaxRequest({
	// url:$("#s_getOutDepart").html(),
	// param:param,
	// callback:SSPRM19_getOutDepartSuccess
	// });
	SSPRM19_clearDetailData();
}
/**
 * 选择了岗位下拉框后改变调入部门下拉框
 * 
 * @param msg
 * @return
 */
function SSPRM19_getOutDepartSuccess(msg) {

	// data为json格式的文本字符串
	var member = eval("(" + msg + ")"); // 包数据解析为json 格式
	$("#outOrganizationIDArr").empty();
	$.each(member,
			function(i) {
				$("#outOrganizationIDArr").append(
						"<option value='" + member[i].BIN_OrganizationID + "'>"
								+ escapeHTMLHandle(member[i].DepartName)
								+ "</option>");
			});
	SSPRM19_clearDetailData();
}

/**
 * 更改促销品查询checkbox状态
 * 
 * @param thisObj
 * @return
 */
function SSPRM19_changeChecked(thisObj) {
	// 取得更改checkbox后的状态
	var checkState = $(thisObj).prop('checked');
	if (checkState) {
		// 先全部取消check状态
		$('#prm_dataTableBody .checkbox').prop('checked', false);
		// 复原单个选中
		$(thisObj).prop('checked', true);
	}
}

function SSPRM19_getStockSuccess(msg) {
	var $tr_obj = binOLSSPRM19_global.thisClickObj.parent().parent();
	var trID = $tr_obj.attr('id');
	var index = trID.substr(7);

	if (msg == "[]") {
		$('#' + trID).find('#dataTd5').html("0");
		$('#bookCountArr' + index).val("0");
		$('#hasprmflagArr' + index).val("0");
		return;
	}

	var member = eval("(" + msg + ")"); // 数据解析为json 格式
	$.each(member, function(i) {
		$('#hasprmflagArr' + index).val("1");
		$('#' + trID).find('#dataTd5').html(member[i].Quantity);
		$('#bookCountArr' + index).val(member[i].Quantity);
	});
}

/**
 * 删除选中行
 */
function SSPRM19_deleterow() {
	SSPRM19_clearActionMsg();
	var $checkboxs = $("#databody").find(":checkbox:checked");
	$checkboxs.parent().parent().remove();
	changeOddEvenColor();
	$('#allSelect').prop("checked", false);
}

/**
 * 全选，全不选
 */
function SSPRM19_selectAll() {
	if ($('#allSelect').prop("checked")) {
		$("#databody :checkbox").each(function() {
			if ($(this).val() != 0) {
				$(this).prop("checked", true);
			}

		});
	} else {
		$("#databody :checkbox").each(function() {
			$(this).removeAttr("checked");
		});

	}
}

/**
 * 点击了选择框后，来控制全选框
 * 
 * @return
 */
function SSPRM19_changechkbox(obj) {
	var chked = $(obj).prop("checked");
	if (!chked) {
		$('#allSelect').prop("checked", false);
		return false;
	}
	var $unChecked = $("#databody").find(":checkbox:not(':checked')");
	if ($unChecked.length == 0) {
		$('#allSelect').prop("checked", true);
	}
}
/**
 * 输入了调拨数量
 * 
 * @param thisObj
 */
function SSPRM19_changeCount(thisObj) {
	var $this = $(thisObj);
	var $td_obj = $this.parent();
	var $tr_obj = $td_obj.parent();
	var price = Number($tr_obj.find(":input[name='priceUnitArr']").val());
	var $amount = $td_obj.next();
	var count = $this.val();
	if(isNaN(parseInt(count))){
		count = 0;
		$this.val("");
	} else {
		count = Math.abs(parseInt(count));
		$(thisObj).val(count);
	}
	var amount = (count * price).toFixed(2);
	$amount.text(amount);
}

/**
 * 点击保存按钮
 */
function SSPRM19_submitSave() {
	SSPRM19_clearActionMsg();
	if (!SSPRM19_checkData()) {
		return;
	}
	cherryAjaxRequest({
		url : $("#s_saveURL").html(),
		param : $('#mainForm').formSerialize(),
		callback : SSPRM19_submitSuccess
	});
}

/**
 * 点击申请按钮
 */
function SSPRM19_submitSend() {
	SSPRM19_clearActionMsg();
	if (!SSPRM19_checkData()) {
		return;
	}
	cherryAjaxRequest({
		url : $("#s_sendURL").html(),
		param : $('#mainForm').formSerialize(),
		callback : SSPRM19_submitSuccess
	});
}

/**
 * 提交前对数据进行检查
 * 
 * @returns {Boolean}
 */
function SSPRM19_checkData() {

	changeOddEvenColor();

	if ($('#inOrganizationId').val() == null
			|| $('#inOrganizationId').val() == ""
			|| $("#outOrganizationId").val() == ""
			|| $("#outOrganizationId").val() == null) {
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	var flag = true;
	var count = 0;
	// 检查有无空行
	$.each($('#databody >tr'), function(i) {
		count += 1;
		var $this = $(this);
		var quantityuArr = $this.find(":input[name='quantityuArr']").val();
		if (quantityuArr == "" || quantityuArr == "0") {
			flag = false;
			$(this).attr("class", "errTRbgColor");
		} else {
			$(this).removeClass('errTRbgColor');
		}
	});

	if (!flag) {
		// 有空行存在
		$('#errorDiv2 #errorSpan2').html($('#errmsg1').val());
		$('#errorDiv2').show();
		return flag;
	}
	if (count == 0) {
		flag = false;
		$('#errorDiv2 #errorSpan2').html($('#errmsg2').val());
		$('#errorDiv2').show();
		return flag;
	}
	return flag;
}

/**
 * 提交成功的回调函数
 * 
 * @param msg
 */
function SSPRM19_submitSuccess(msg) {
	submitflag = true;
	if (msg.indexOf("actionMessage") > -1) {
		$("#databody > tr").remove();
		$("#outOrganizationId").val("");
		$("#outOrgName").html("");
	}
}

/**
 * 删除掉画面头部的提示信息框
 * 
 * @return
 */
function SSPRM19_clearActionMsg() {
	$('#actionResultDisplay').html("");
	$('#errorDiv2').attr("style", 'display:none');
	$.each($('#databody >tr'), function(i) {
		if (i > 0) {
			$(this).removeClass('errTRbgColor');
		}
	});
}
function changeOddEvenColor() {
	$("#databody tr:odd").attr("class", "even");
	$("#databody tr:even").attr("class", "odd");
}
function changeOddEvenColor1() {
	$("#databody tr:odd").attr("class", function() {
		if ($(this).attr("class") == "errTRbgColor") {

		} else {
			$(this).addClass("odd");
		}

	});
	$("#databody tr:even").attr("class", function() {
		if ($(this).attr("class") == "errTRbgColor") {

		} else {
			$(this).addClass("even");
		}

	});
}
function SSPRM19_clearDetailData() {
	$("#databody > tr").remove();
}

/**
 * 调入部门弹出框
 * 
 * @return
 */
function SSPRM19_openInDepartBox(thisObj) {
	SSPRM19_clearActionMsg();
	changeOddEvenColor();
	// 取得所有部门类型
	var departType = "";
	for ( var i = 0; i < $("#departTypePop option").length; i++) {
		var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
		// 排除4柜台
		if (departTypeValue != "4") {
			departType += "&departType=" + departTypeValue;
		}
	}
	var param = "checkType=radio&privilegeFlg=1&businessType=1" + departType;
	var callback = function() {
		var $selected = $('#departDataTable').find(':input[checked]').parents(
				"tr");
		if ($selected.length > 0) {
			var departId = $selected.find("input@[name='organizationId']")
					.val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			$("#inOrganizationId").val(departId);
			$("#inOrgName").text("(" + departCode + ")" + departName);
			SSPRM19_chooseInDepart();
		}
	};
	popDataTableOfDepart(thisObj, param, callback);
}

/**
 * 调出部门弹出框
 * 
 * @return
 */
function SSPRM19_openOutDepartBox(thisObj) {
	SSPRM19_clearActionMsg();
	if ($('#inOrganizationId').val() == null
			|| $('#inOrganizationId').val() == "") {
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if ($('#inDepotId').val() == null || $('#inDepotId').val() == "") {
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00042').val());
		$('#errorDiv2').show();
		return false;
	}
	changeOddEvenColor();
	// 取得所有部门类型
	var departType = "";
	for ( var i = 0; i < $("#departTypePop option").length; i++) {
		var departTypeValue = $("#departTypePop option:eq(" + i + ")").val();
		// 排除4柜台
		if (departTypeValue != "4") {
			departType += "&departType=" + departTypeValue;
		}
	}
	var param = "checkType=radio&businessType=1&testTypeFlg=1&levelFlg=0&organizationId="
			+ $("#inOrganizationId").val() + departType;
	var callback = function() {
		var $selected = $('#departDataTable').find(':input[checked]').parents(
				"tr");
		if ($selected.length > 0) {
			var departId = $selected.find("input@[name='organizationId']")
					.val();
			var departCode = $selected.find("td:eq(1)").text();
			var departName = $selected.find("td:eq(2)").text();
			$("#outOrganizationId").val(departId);
			$("#outOrgName").html("(" + departCode + ")" + departName);
		}
	};
	popDataTableOfDepart(thisObj, param, callback);
}