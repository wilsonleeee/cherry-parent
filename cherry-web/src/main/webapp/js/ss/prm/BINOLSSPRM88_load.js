function BINOLSSPRM88_load() {};

BINOLSSPRM88_load.prototype = {
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
				$errorMessage.append(map.resultMsg);
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
						BINOLSSPRM88_load.counterFailSearch();
					}else {
						$("#counterLoadDialog #conditionForm").find("#operateFlag").remove();
						var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						$("#counterLoadDialog #conditionForm").append(html);
						BINOLSSPRM88_load.counterFailSearch();
					}
				}
			}
		});
	},
	//产品导入
	"productExeclLoad": function () {
		var $parentDiv = $("#productLoadDialog");
		var $productUpExcel = $("#productUpExcel");
		var upMode =$parentDiv.find('select[name="upMode"]').val();
		var productUpExcelId = 'productUpExcel';
		var $productPathExcel = $('#productPathExcel');

		var url = $("#productExeclLoad").attr("href");

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
		var filterType = $("#productLoadDialog #filterType").val();
		var excelProduct_w = $("#productLoadDialog #excelProduct_w").val();
		var excelProduct_b = $("#productLoadDialog #excelProduct_b").val();
		$ajaxLoading.ajaxStart(function(){$(this).show();});
		$.ajaxFileUpload({
			url:url,
			secureuri:false,
			data:{'csrftoken':parentTokenVal(),
				'brandInfoId':$('#brandInfoId').val(),
				'upMode': upMode,
				'filterType':filterType,
				'searchCode':searchCode,
				'excelProduct_w':excelProduct_w,
				'excelProduct_b':excelProduct_b
			},
			fileElementId:productUpExcelId,
			dataType: 'html',
			success: function (msg){
				var map = JSON.parse(msg);
				//TODO
				//var productJson = map.productJson;

				$ajaxLoading.ajaxComplete(function(){$(this).hide();});
				var resultCode = map.resultCode;
				var searchCode = map.searchCode;
				if (resultCode == '0' || resultCode == '1') {
					if (filterType == '2') {
						$("#searchCode").val(searchCode);
					}else {
						$("#searchCode").val(searchCode) ;
					}
					if (filterType=='1'){
						$("#excelProduct_w").val(JSON2.stringify(map.productJson));
					}else{
						$("#excelProduct_b").val(JSON2.stringify(map));
					}
					// {"searchCode":"EC010031612050000037","resultCode":0,"successCount":3}
					var tips = '<span>导入成功的数量: <span class="green">' + map.successCount + '</span></span> ';
					tips += '<span>导入失败的数量: <span class="red">'  + map.failCount + '</span></span> ';
					$errorMessage.append(tips);
					$errorMessage.show();
				}else {
					$errorMessage.append(map.resultMsg);
					$errorMessage.show();
				}
				if(resultCode >0){
					if($("#productFail").find(".dataTables_wrapper").length>0){
						//$("#productLoadDialog #conditionForm").find("#searchCode").remove();
						//var html='<input type="hidden" id="searchCode" name="searchCode" value="'+searchCode+'"/>';
						//$("#productLoadDialog #conditionForm").append(html);
						var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="productFailDataTable"><thead>';
						if (filterType==1){
							htmlStr +='<tr><th>品牌*</th><th>是否产品范围</th><th>是否特定产品*</th><th>厂商编码*</th><th>产品条码</th><th>产品名称</th><th>数量金额*</th><th>比较条件*</th><th>比较值*</th>';
						}else {
							htmlStr +='<tr><th>品牌*</th><th>厂商编码*</th><th>产品条码</th><th>产品名称</th>';
						}
						htmlStr +='<th>备注</th></tr>';
						htmlStr += '</thead><tbody></tbody></table>';
						$("#productFail").find(".section-content").html(htmlStr);

						binolssprm88_load.productFailSearch();
					}else{
						//$("#productLoadDialog #conditionForm").find("#searchCode").remove();
						//var html='<input type="hidden" id="searchCode" name="searchCode" value="'+searchCode+'"/>';
						//$("#productLoadDialog #conditionForm").append(html);
						binolssprm88_load.productFailSearch();
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
		var searchCode =  $("#searchCode").val();
		$ajaxLoading.ajaxStart(function(){$(this).show();});
		$.ajaxFileUpload({
			url: url,
			secureuri:false,
			data:{'csrftoken':parentTokenVal(),
				'brandInfoId':$('#brandInfoId').val(),
				'upMode': upMode,
				'filterType':filterType,
				'searchCode':searchCode
			},
			fileElementId:memberUpExcelId,
			dataType: 'html',
			success: function (msg){
				$ajaxLoading.ajaxComplete(function(){$(this).hide();});
				var map = JSON.parse(msg);
				var resultCode = map.resultCode;
				var searchCode = map.searchCode;
				$errorMessage.append(map.resultMsg);
				$errorMessage.show();

				if (resultCode == '0' || resultCode == '1') {
					$("#searchCode").val(searchCode) ;
					var tips = '<span>导入成功的数量: <span class="green">' + map.successCount + '</span></span> ';
					tips += '<span>导入失败的数量: <span class="red">'  + map.failCount + '</span></span> ';
					$errorMessage.append(tips);
					$errorMessage.show();
				}

				if(resultCode>0){
					if($("#memberFail").find(".dataTables_wrapper").length>0){
						//$("#memberLoadDialog #conditionForm").find("#operateFlag").remove();
						//var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						//$("#memberLoadDialog #conditionForm").append(html);
						var htmlStr = '<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memberFailDataTable">';
						htmlStr += '<thead><tr><th>品牌*</th><th>会员卡号</th><th>会员手机号*</th><th>BP号</th><th>会员等级</th><th>会员姓名</th><th>备注</th></tr>';
					    htmlStr += '</thead><tbody></tbody></table>';
						$("#memberFail").find(".section-content").html(htmlStr);
						binolssprm88_load.memberFailSearch();
					}else{
						//$("#memberLoadDialog #conditionForm").find("#operateFlag").remove();
						//var html='<input type="hidden" id="operateFlag" name="failUploadDataDTO.operateFlag" value="'+flag+'"/>';
						//$("#memberLoadDialog #conditionForm").append(html);
						binolssprm88_load.memberFailSearch();
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
		url = url + '?csrftoken='+ csrftoken + '&'+ params ;
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
		//var params = $("#productLoadDialog #conditionForm").serialize();

		var filterType = $("#productLoadDialog #filterType").val();
		var searchCode = $("#productLoadDialog #searchCode").val();
		var operateType = $("#productLoadDialog #operateType").val();
		url = url + '?csrftoken='+ csrftoken + '&filterType='+ filterType+'&searchCode='+searchCode+'&operateType='+operateType;
		// 显示结果一览
		$("#productFail").removeClass("hide");
		// 表格设置
		if(filterType==1){//白名单 有产品数量

			var tableSetting = {
				// 表格ID
				tableId : '#productFailDataTable',
				// 数据URL
				url : url,
				// 一页显示页数
				iDisplayLength:5,
				// 表格列属性设置
				aoColumns : [  { "sName": "brandCode", "sWidth": "15%", "bSortable": false },
					{ "sName": "productRange", "sWidth": "20%" , "bSortable": false},
					{ "sName": "productAppointed", "sWidth": "20%" , "bSortable": false},
					{ "sName": "unitCode", "sWidth": "20%" , "bSortable": false},
					{ "sName": "barCode", "sWidth": "5%", "bSortable": false},
					{ "sName": "productName", "sWidth": "5%", "bSortable": false},
					{ "sName": "quantityOrAmount", "sWidth": "5%", "bSortable": false},
					{ "sName": "compareCondition", "sWidth": "5%", "bSortable": false},
					{ "sName": "compareValue", "sWidth": "5%", "bSortable": false},
					{ "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index:96
			};
		}else if(filterType==2){//黑名单 无产品数量
			var tableSetting = {
				// 表格ID
				tableId : '#productFailDataTable',
				// 数据URL
				url : url,
				// 一页显示页数
				iDisplayLength:5,
				// 表格列属性设置
				aoColumns : [  { "sName": "brandCode", "sWidth": "15%", "bSortable": false },
					{ "sName": "unitCode", "sWidth": "20%", "bSortable": false },
					{ "sName": "barCode", "sWidth": "20%", "bSortable": false },
					{ "sName": "productName", "sWidth": "25%" , "bSortable": false},
					{ "sName": "errorMsg", "sWidth": "20%", "bSortable": false }],
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index:96
			};
		}

		oTableArr[96] = null;
		fixedColArr[96] = null;
		// 调用获取表格函数
		getTable(tableSetting);
	},
	"export":function(){
		var url =$("#exportExecl").attr("href");
	//	var csrftoken = parentTokenVal();
		var params = $("#conditionForm").serialize();
		url = url + "?" + params;
		window.open(url,"_self");
	},
	"cleanSearchCode":function(obj){
		var $searchCode = $(obj).parent('.loadDialog').find('#searchCode');
		$searchCode.val('');
	}
};

var binolssprm88_load =  new BINOLSSPRM88_load();