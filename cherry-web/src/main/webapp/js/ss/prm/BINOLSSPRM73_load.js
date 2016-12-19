function BINOLSSPRM73_load() {};

BINOLSSPRM73_load.prototype = {
	//柜台导入
	"counterExeclLoad": function () {
		var $parentDiv = $("#counterLoadDialog");
		var $counterUpExcel = $("#counterUpExcel");
		var upMode =$parentDiv.find('select[name="upMode"]').val();
		var counterUpExcelId = 'counterUpExcel';
		var $counterPathExcel = $('#counterPathExcel');

		var url = $("#counterExeclLoad").attr("href");
		// AJAX登陆图片
		var $ajaxLoading = $parentDiv.find("#counterLoading");
		// 错误信息提示区
		var $errorMessage = $('#counterLoadDialog #actionResultDisplay');
		// 清空错误信息
		$errorMessage.hide();
		$errorMessage.empty();
		if($counterUpExcel.val()==''){
			$counterPathExcel.val('');
			$("#errorDiv").show();
			$("#errorSpan").text("请选择文件");
			return false;
		}
		$("#counterFail").addClass("hide");
		var conditionType = $("#conditionType").val();
		var targetDiv = $("#targetDiv").val();
		var filterType = $("#filterType").val();
		var contentNo = $("#contentNo").val();
		$ajaxLoading.ajaxStart(function(){$(this).show();});
		$.ajaxFileUpload({
			url: url,
			secureuri:false,
			data:{'csrftoken':parentTokenVal(),
				'ruleCode':$('#ruleCode').val(),
				'couponRule.brandInfoId':$('#brandInfoId').val(),
				'upMode': upMode,
				'conditionType': conditionType,
				'filterType':filterType,
				'contentNo':contentNo
			},
			fileElementId:counterUpExcelId,
			dataType: 'html',
			success: function (msg){
				var map = JSON.parse(msg);
				$ajaxLoading.ajaxComplete(function(){$(this).hide();});
				var resultCode = map.resultCode;
				var flag = map.operateFlag;
				if (resultCode>=0){
					var tips = "导入成功的数量:<span class='green'>"+map.successCount+"</span>&nbsp;导入失败的数量:<span class='red'>"+map.failCount+"</span>";
					$errorMessage.append(tips);
				}else {
					$errorMessage.append(map.resultMsg);
				}
				$errorMessage.show();
				if(resultCode>0){
					if($("#counterFail").find(".dataTables_wrapper").length>0){
						$("#counterLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#counterLoadDialog #conditionForm").append(html);
						var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="counterFailDataTable"><thead>';
						htmlStr += ' <tr><th>品牌*</th><th>柜台号*</th><th>柜台名称</th><th>备注</th></tr>';
						htmlStr += '</thead><tbody></tbody></table>';
						$("#counterFail").find(".section-content").html(htmlStr);
						binolssprm73_load.counterFailSearch();
					}else {
						$("#counterLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#counterLoadDialog #conditionForm").append(html);
						binolssprm73_load.counterFailSearch();
					}
				}
			}
		});
	},
	//产品导入
	"productExeclLoad": function () {
		var $parentDiv = $("#productDivDialog");
		var $productUpExcel = $("#productUpExcel");
		var upMode =$parentDiv.find('select[name="upMode"]').val();
		var productUpExcelId = 'productUpExcel';
		var $productPathExcel = $('#productPathExcel');

		var url = $("#popProductUpload").attr("href");

		// AJAX登陆图片
		var $ajaxLoading = $parentDiv.find("#productLoading");
		// 错误信息提示区
		var $errorMessage = $('#productLoadDialog #actionResultDisplay');
		// 清空错误信息
		$errorMessage.hide();
		$errorMessage.empty();
		if($productUpExcel.val()==''){
			$productPathExcel.val('');
			$("#errorDiv").show();
			$("#errorSpan").text("请选择文件");
			return false;
		}
		$("#productFail").addClass("hide");
		$("#productFail2").addClass("hide");
		var conditionType = $("#productLoadDialog #conditionType").val();
		var filterType = $("#productLoadDialog #filterType").val();
		var contentNo = $("#productLoadDialog #contentNo").val();
		$ajaxLoading.ajaxStart(function(){$(this).show();});
		$.ajaxFileUpload({
			url:url,
			secureuri:false,
			data:{'csrftoken':parentTokenVal(),
				'ruleCode':$('#ruleCode').val(),
				'couponRule.brandInfoId':$('#brandInfoId').val(),
				'upMode': upMode,
				'conditionType': conditionType,
				'filterType':filterType,
				'contentNo':contentNo
			},
			fileElementId:productUpExcelId,
			dataType: 'html',
			success: function (msg){
				var map = JSON.parse(msg);
				$ajaxLoading.ajaxComplete(function(){$(this).hide();});
				var resultCode = map.resultCode;
				var flag = map.operateFlag;
				if (resultCode>=0){
					var tips = "导入成功的数量:<span class='green'>"+map.successCount+"</span>&nbsp;导入失败的数量:<span class='red'>"+map.failCount+"</span>";
					$errorMessage.append(tips);
				}else {
					$errorMessage.append(map.resultMsg);
				}
				$errorMessage.show();
				if(resultCode>0){
					if($("#productFail").find(".dataTables_wrapper").length>0){
						$("#productLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#productLoadDialog #conditionForm").append(html);
						var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productFailDataTable"><thead>';
						htmlStr += '<tr><th>品牌*</th><th>产品编码*</th><th>产品条码</th><th>产品名称</th>';

						if(filterType==1){
							htmlStr +='<th>产品数量*</th>';
						}
						htmlStr +='<th>备注</th></tr>';
						htmlStr += '</thead><tbody></tbody></table>';
						$("#productFail").find(".section-content").html(htmlStr);

						binolssprm73_load.productFailSearch();
					}else{
						$("#productLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#productLoadDialog #conditionForm").append(html);
						binolssprm73_load.productFailSearch();
					}
				}
			}
		});
	},
	//会员导入
	"memberExeclLoad": function () {
		var $parentDiv = $("#memberLoadDialog");
		var $memberUpExcel = $("#memberUpExcel");
		var upMode =$parentDiv.find('select[name="upMode"]').val();
		var memberUpExcelId = 'memberUpExcel';
		var $memberPathExcel = $('#memberPathExcel');

		var url = $("#memberExeclLoad").attr("href");
		// AJAX登陆图片
		var $ajaxLoading = $parentDiv.find("#memberLoading");
		// 错误信息提示区
		var $errorMessage = $('#memberLoadDialog #actionResultDisplay');
		// 清空错误信息
		$errorMessage.hide();
		$errorMessage.empty();

		$("#memberFail").addClass("hide");
		if($memberUpExcel.val()==''){
			$memberPathExcel.val('');
			$("#errorDiv").show();
			$("#errorSpan").text("请选择文件");
			return false;
		}
		var conditionType = $("#conditionType").val();
		var targetDiv = $("#targetDiv").val();
		var filterType = $("#filterType").val();
		var contentNo = $("#contentNo").val();
		$ajaxLoading.ajaxStart(function(){$(this).show();});
		$.ajaxFileUpload({
			url: url,
			secureuri:false,
			data:{'csrftoken':parentTokenVal(),
				'ruleCode':$('#ruleCode').val(),
				'couponRule.brandInfoId':$('#brandInfoId').val(),
				'upMode': upMode,
				'conditionType': conditionType,
				'filterType':filterType,
				'contentNo':contentNo
			},
			fileElementId:memberUpExcelId,
			dataType: 'html',
			success: function (msg){
				$ajaxLoading.ajaxComplete(function(){$(this).hide();});
				var map = JSON.parse(msg);
				var resultCode = map.resultCode;
				var flag = map.operateFlag;
				if (resultCode>=0){
					var tips = "导入成功的数量:<span class='green'>"+map.successCount+"</span>&nbsp;导入失败的数量:<span class='red'>"+map.failCount+"</span>";
					$errorMessage.append(tips);
				}else {
					$errorMessage.append(map.resultMsg);
				}
				$errorMessage.show();
				if(resultCode>0){
					if($("#memberFail").find(".dataTables_wrapper").length>0){
						$("#memberLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#memberLoadDialog #conditionForm").append(html);
						var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memberFailDataTable">';
						htmlStr += '<thead><tr><th>品牌*</th><th>会员卡号</th><th>会员手机号*</th><th>BP号</th><th>会员等级</th><th>会员姓名</th><th>备注</th></tr>';
					    htmlStr += '</thead><tbody></tbody></table>';
						$("#memberFail").find(".section-content").html(htmlStr);
						binolssprm73_load.memberFailSearch();
					}else{
						$("#memberLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#memberLoadDialog #conditionForm").append(html);
						binolssprm73_load.memberFailSearch();
					}
				}
			}
		});
	},
	//ajax dateTable加载失败数据(柜台)
	"counterFailSearch": function(){
		var url = $("#counterFailSearch").attr("href");
		var csrftoken = parentTokenVal();
		var params = $("#counterLoadDialog #conditionForm").serialize();
		var ruleCode = $('#ruleCode').val();
		url = url + '?csrftoken='+ csrftoken + '&'+ params + '&failUploadDataDTO.ruleCode=' +ruleCode;
		// 显示结果一览
		$("#counterFail").removeClass("hide");
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#counterFailDataTable',
			// 一页显示页数
			iDisplayLength:5,
			// 数据URL
			url : url,
			// 表格列属性设置
			aoColumns : [  { "sName": "brandCode", "sWidth": "25%", "bSortable": false },
				{ "sName": "counterCode", "sWidth": "25%", "bSortable": false  },
				{ "sName": "counterName", "sWidth": "15%" , "bSortable": false },
				{ "sName": "errorMsg", "sWidth": "25%", "bSortable": false }],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			index:98
		};
		oTableArr[98] = null;
		fixedColArr[98] = null;
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//ajax dateTable加载失败数据(会员)
	"memberFailSearch": function(){
		var url = $("#memberFailSearch").attr("href");
		var csrftoken = parentTokenVal();
		var params = $("#memberLoadDialog #conditionForm").serialize();
		var ruleCode = $('#ruleCode').val();
		url = url + '?csrftoken='+ csrftoken + '&'+ params + '&failUploadDataDTO.ruleCode=' +ruleCode;
		// 显示结果一览
		$("#memberFail").removeClass("hide");
		// 表格设置
		var tableSetting = {
			// 表格ID
			tableId : '#memberFailDataTable',
			// 数据URL
			url : url,
			// 一页显示页数
			iDisplayLength:5,
			// 表格列属性设置
			aoColumns : [  { "sName": "brandCode", "sWidth": "25%", "bSortable": false },
				{ "sName": "memberCode", "sWidth": "25%", "bSortable": false  },
				{ "sName": "mobile", "sWidth": "15%", "bSortable": false  },
				{ "sName": "bpCode", "sWidth": "15%" , "bSortable": false },
				{ "sName": "memberLevel", "sWidth": "15%" , "bSortable": false },
				{ "sName": "name", "sWidth": "15%" , "bSortable": false },
				{ "sName": "errorMsg", "sWidth": "25%", "bSortable": false }],
			// 横向滚动条出现的临界宽度
			sScrollX : "100%",
			index:97
		};
		oTableArr[97] = null;
		fixedColArr[97] = null;
		// 调用获取表格函数
		getTable(tableSetting);
	},
	//ajax dateTable加载失败数据(产品)
	"productFailSearch": function(){
		var url = $("#productFailSearch").attr("href");
		var csrftoken = parentTokenVal();
		var params = $("#productLoadDialog #conditionForm").serialize();
		var ruleCode = $('#ruleCode').val();
		url = url + '?csrftoken='+ csrftoken + '&'+ params + '&failUploadDataDTO.ruleCode=' +ruleCode;
		var filterType = $("#productLoadDialog #filterType").val();

		// 表格设置
		if(filterType==1){//白名单 有产品数量
			// 显示结果一览
			$("#productFail").removeClass("hide");
			var tableSetting = {
				// 表格ID
				tableId : '#productFailDataTable',
				// 数据URL
				url : url,
				// 一页显示页数
				iDisplayLength:5,
				// 表格列属性设置
				aoColumns : [  { "sName": "brandCode", "sWidth": "15%", "bSortable": false },
					{ "sName": "unitCode", "sWidth": "20%" },
					{ "sName": "barCode", "sWidth": "20%" },
					{ "sName": "productName", "sWidth": "20%" },
					{ "sName": "productNumber", "sWidth": "5%", "bSortable": false},
					{ "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index:96
			};
			oTableArr[96] = null;
			fixedColArr[96] = null;
		}else if(filterType==2){//黑名单 无产品数量
			// 显示结果一览
			$("#productFail").removeClass("hide");
			var tableSetting = {
				// 表格ID
				tableId : '#productFailDataTable',
				// 数据URL
				url : url,
				// 一页显示页数
				iDisplayLength:5,
				// 表格列属性设置
				aoColumns : [  { "sName": "brandCode", "sWidth": "15%", "bSortable": false },
					{ "sName": "unitCode", "sWidth": "20%" },
					{ "sName": "barCode", "sWidth": "20%" },
					{ "sName": "productName", "sWidth": "25%" },
					{ "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index:95
			};
			oTableArr[95] = null;
			fixedColArr[95] = null;
		}
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"export":function(){
		var url =$("#exportExecl").attr("href");
	//	var csrftoken = parentTokenVal();
		var params = $("#conditionForm").serialize();
		var ruleCode = $('#ruleCode').val();
	//	url = url + '?csrftoken='+ csrftoken + '&'+ params + '&failUploadDataDTO.ruleCode=' +ruleCode;
		url = url + "?" + params + '&failUploadDataDTO.ruleCode=' +ruleCode;
		window.open(url,"_self");
	}
};

var binolssprm73_load =  new BINOLSSPRM73_load();