
function BINOLMBMBM09() {
	
};

BINOLMBMBM09.prototype = {
	// 会员搜索处理
	"searchMember" : function() {
		if(!$('#memberCherryForm').valid()) {
			return false;
		}
		binolmbmbm09.setJsonParam("#memberCherryForm");
		var url = $("#memberListUrl").attr("href");
		var params= $("#memberCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#memSaleInfo").hide();
		$("#memberInfo").show();	
		var columns;
		var scol;
		if ($('#clubMod').val() != '3') {
			scol = 19;
			columns = [{ "sName": "number", "sWidth": "5%", "bSortable": false },
		               { "sName": "memName", "sWidth": "10%", "bSortable": false },
		               { "sName": "memCode", "sWidth": "10%" },
		               { "sName": "nickname", "sWidth": "10%", "bSortable": false },
		               { "sName": "mobilePhone", "sWidth": "10%", "bSortable": false },
		               { "sName": "telephone", "sWidth": "10%", "bSortable": false,"bVisible": false },
		               { "sName": "email", "sWidth": "10%", "bSortable": false,"bVisible": false },
		               { "sName": "tencentQQ", "sWidth": "10%", "bSortable": false,"bVisible": false },
		               { "sName": "gender", "sWidth": "5%", "bSortable": false,"bVisible": false },
		               { "sName": "birthDay", "sWidth": "10%", "bSortable": false },
		               { "sName": "levelName", "sWidth": "10%" },
		               { "sName": "creditRating", "sWidth": "10%", "bVisible": false },
		               { "sName": "totalPoint", "sWidth": "10%" },
		               { "sName": "changablePoint", "sWidth": "10%","bSortable": false,"bVisible": false },
		               { "sName": "clubCounterCode", "sWidth": "20%" },
		               { "sName": "clubEmployeeCode", "sWidth": "20%","bVisible": false },
		               { "sName": "clubJoinTime", "sWidth": "10%" },
		               { "sName": "counterCode", "sWidth": "20%","bVisible": false },
		               { "sName": "employeeCode", "sWidth": "20%","bVisible": false },
		               { "sName": "joinDate", "sWidth": "10%"},
		               { "sName": "memo1", "sWidth": "10%", "bSortable": false,"bVisible": false },
		               { "sName": "memo2", "sWidth": "10%", "bSortable": false,"bVisible": false },
		               { "sName": "status", "sWidth": "5%" }]
		} else {
			scol = 16;
			columns = [{ "sName": "number", "sWidth": "5%", "bSortable": false },
               { "sName": "memName", "sWidth": "10%", "bSortable": false },
               { "sName": "memCode", "sWidth": "10%" },
               { "sName": "nickname", "sWidth": "10%", "bSortable": false },
               { "sName": "mobilePhone", "sWidth": "10%", "bSortable": false },
               { "sName": "telephone", "sWidth": "10%", "bSortable": false,"bVisible": false },
               { "sName": "email", "sWidth": "10%", "bSortable": false,"bVisible": false },
               { "sName": "tencentQQ", "sWidth": "10%", "bSortable": false,"bVisible": false },
               { "sName": "gender", "sWidth": "5%", "bSortable": false,"bVisible": false },
               { "sName": "birthDay", "sWidth": "10%", "bSortable": false },
               { "sName": "levelName", "sWidth": "10%" },
               { "sName": "creditRating", "sWidth": "10%", "bVisible": false },
               { "sName": "totalPoint", "sWidth": "10%" },
               { "sName": "changablePoint", "sWidth": "10%","bSortable": false,"bVisible": false },
               { "sName": "counterCode", "sWidth": "20%" },
               { "sName": "employeeCode", "sWidth": "20%","bVisible": false },
               { "sName": "joinDate", "sWidth": "10%" },
               { "sName": "memo1", "sWidth": "10%", "bSortable": false,"bVisible": false },
               { "sName": "memo2", "sWidth": "10%", "bSortable": false,"bVisible": false },
               { "sName": "status", "sWidth": "5%" },
               { "sName": "referrerCode", "sWidth": "10%", "bVisible": false}]
		}
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#dataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ scol, "desc" ]],
				 // 表格列属性设置
				 aoColumns : columns,
				// 不可设置显示或隐藏的列
				aiExclude :[0, 1, 2],	
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				// 固定列数
				fixedColumns : 3,
				fnDrawCallback : function() {
					$('#dataTable').find("a.description").cluetip({
						splitTitle: '|',
					    width: 300,
					    height: 'auto',
					    cluetipClass: 'default', 
					    cursor: 'pointer',
					    showTitle: false
					});
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    // 查询会员销售
	"searchMemSale" : function() {
		if(!$('#memberCherryForm').valid()) {
			return false;
		}
		binolmbmbm09.setJsonParam("#memberCherryForm");
		var url = $("#memSaleListUrl").attr("href");
		var params= $("#memberCherryForm").serialize();
		if(params != null && params != "") {
			url = url + "?" + params;
		}
		// 显示结果一览
		$("#memberInfo").hide();
		$("#memSaleInfo").show();
		var aoColumns = [];
		aoColumns.push({ "sName": "memberCode", "sWidth": "10%", "bSortable": false });
		aoColumns.push({ "sName": "billCode", "sWidth": "20%", "bSortable": false });
		aoColumns.push({ "sName": "saleType", "sWidth": "10%" });
		aoColumns.push({ "sName": "saleTime", "sWidth": "15%" });
		if($("#saleCouFlag").length > 0) {
			aoColumns.push({ "sName": "departCode", "sWidth": "15%" });
		}
		aoColumns.push({ "sName": "amount", "sWidth": "10%" });
		aoColumns.push({ "sName": "quantity", "sWidth": "10%" });
		// 表格设置
		var tableSetting = {
				 // 表格ID
				 tableId : '#memSaleDataTable',
				 // 数据URL
				 url : url,
				 // 表格默认排序
				 aaSorting : [[ 3, "desc" ]],
				 // 表格列属性设置
				 aoColumns : aoColumns,
				// 横向滚动条出现的临界宽度
				sScrollX : "100%",
				index: 1,
				colVisFlag: true,
				callbackFun: function(msg) {
			 		var $msg = $("<div></div>").html(msg);
			 		var $headInfo = $("#saleCountInfo",$msg);
			 		if($headInfo.length > 0) {
			 			$("#saleCountInfo").html($headInfo.html());
			 		} else {
			 			$("#saleCountInfo").empty();
			 		}
				}
		};
		// 调用获取表格函数
		getTable(tableSetting);
    },
    // 选择购买产品弹出框
    "openProDialog" :function(flag) {
    	var proTable = "proTable";
    	var proShowDiv = "proShowDiv";
    	var proTypeTable = "proTypeTable";
    	var proTypeShowDiv = "proTypeShowDiv";
    	var prtVendorId = "buyPrtVendorId";
    	if(flag == '1') {
    		proTable = "notProTable";
        	proShowDiv = "notProShowDiv";
        	proTypeTable = "notProTypeTable";
        	proTypeShowDiv = "notProTypeShowDiv";
        	prtVendorId = "notPrtVendorId";
    	}
		var option = {
         	   	targetId: proShowDiv,
 	           	checkType : "checkbox",
 	           	mode : 2,
 	            popValidFlag: 2,
 	           	brandInfoId : $("#brandInfoId").val(),
 		       	getHtmlFun:function(info){
     		       	var html = '<tr>'; 
     				html += '<td class="hide"><input type="hidden" name="prtVendorId" value="' + info.proId + '"/><input type="hidden" name="'+prtVendorId+'" value="' + info.proId + '"/></td>';
     				html += '<td style="width:25%;">' + info.unitCode + '</td>';
     				html += '<td style="width:25%;">' + info.barCode + '</td>';
     				html += '<td style="width:35%;">' + info.nameTotal + '</td>';
     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolmbmbm09.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
     				html += '</tr>';
     				return html;
			   	},
			   	click : function() {
			   		$("#"+proTypeTable).hide();
			   		$("#"+proTypeShowDiv).empty();
			   		if($("#"+proShowDiv).find("tr").length > 0) {
			   			$("#"+proTable).show();
			   		} else {
			   			$("#"+proTable).hide();
			   		}
			   	}
 	    };
 		popAjaxPrtDialog(option);
	},
	// 选择购买产品类别弹出框
	"openProTypeDialog" :function(flag) {
		var proTable = "proTable";
    	var proShowDiv = "proShowDiv";
    	var proTypeTable = "proTypeTable";
    	var proTypeShowDiv = "proTypeShowDiv";
    	var cateValId = "buyCateValId";
    	if(flag == '1') {
    		proTable = "notProTable";
        	proShowDiv = "notProShowDiv";
        	proTypeTable = "notProTypeTable";
        	proTypeShowDiv = "notProTypeShowDiv";
        	cateValId = "notCateValId";
    	}
		var option = {
         	   	targetId: proTypeShowDiv,
 	           	checkType : "checkbox",
 	           	mode : 2,
 	           	brandInfoId : $("#brandInfoId").val(),
 		       	getHtmlFun:function(info){
     		       	var html = '<tr>'; 
     				html += '<td class="hide"><input type="hidden" name="cateValId" value="' + info.cateValId + '"/><input type="hidden" name="'+cateValId+'" value="' + info.cateValId + '"/></td>';
     				html += '<td style="width:25%;">' + info.cateVal + '</td>';
     				html += '<td style="width:25%;">' + info.cateValName + '</td>';
     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="binolmbmbm09.deleteHtml(this);return false;">'+$("#deleteButton").text()+'</a></td>';
     				html += '</tr>';
     				return html;
			   	},
			   	click : function(){
			   		$("#"+proTable).hide();
			   		$("#"+proShowDiv).empty();
			   		if($("#"+proTypeShowDiv).find("tr").length > 0) {
			   			$("#"+proTypeTable).show();
			   		} else {
			   			$("#"+proTypeTable).hide();
			   		}
			   	}
 	    };
		popAjaxPrtCateDialog(option);
	},
	// 删除购买产品或者购买产品类别信息
	"deleteHtml": function(_this) {
		var $tbody = $(_this).parent().parent().parent();
		$(_this).parent().parent().remove();
		if($tbody.find('tr').length == 0) {
			$tbody.parent().hide();
		}
	},
	// 选择购买柜台弹出框
	"popSaleCounterList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#saleCounterId").val($checkedRadio.val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delSaleCounterHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#saleCounterDiv").html(html);
			}
		}
		var value = $("#saleCounterId").val();
		popDataTableOfCounterList(url, null, value, callback);
	},
	// 删除选择的购买柜台
	"delSaleCounterHtml": function(object) {
		$("#saleCounterId").val("");
		$("#saleCounterDiv").empty();
	},
	// 选择活动柜台弹出框
	"popCampaignCounterList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#campaignCounterId").val($checkedRadio.val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delCampaignCounterHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#campaignCounterDiv").html(html);
			}
		}
		var value = $("#campaignCounterId").val();
		popDataTableOfCounterList(url, null, value, callback);
	},
	// 删除选择的活动柜台
	"delCampaignCounterHtml": function(object) {
		$("#campaignCounterId").val("");
		$("#campaignCounterDiv").empty();
	},
	// 选择发卡柜台弹出框
	"popMemCounterList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#memCounterId").val($checkedRadio.val());
				var html = $checkedRadio.next().val();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delMemCounterHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#memCounterDiv").html(html);
			}
		}
		var value = $("#memCounterId").val();
		popDataTableOfCounterList(url, null, value, callback);
	},
	// 删除选择的发卡柜台
	"delMemCounterHtml": function(object) {
		$("#memCounterId").val("");
		$("#memCounterDiv").empty();
	},
	// 选择发卡柜台弹出框（区域模式）
	"popRegionDialog": function(url, isClub) {
		if ('1' == isClub && $("#memberClubId").val() == "") {
			binolmbmbm09.clubWarnDialog();
			return false;
		}
		var $exclusiveFlag;
		var $modeFlag;
		var $couValidFlag;
		var $regionId;
		var $provinceId;
		var $cityId;
		var $memCounterId;
		var $channelRegionId;
		var $channelId;
		var $regionNameDiv;
		var $belongId;
		if ('1' == isClub) {
			$exclusiveFlag = $("#clubExclusiveFlag");
			$modeFlag = $("#clubModeFlag");
			$couValidFlag = $("#clubCouValidFlag");
			$regionId = $("#clubRegionId");
			$provinceId = $("#clubProvinceId");
			$cityId = $("#clubCityId");
			$memCounterId = $("#clubMemCounterId");
			$channelRegionId = $("#clubChannelRegionId");
			$channelId = $("#clubChannelId");
			$regionNameDiv = $("#clubRegionNameDiv");
			$belongId = $("#clubBelongId");
		} else {
			$exclusiveFlag = $("#exclusiveFlag");
			$modeFlag = $("#modeFlag");
			$couValidFlag = $("#couValidFlag");
			$regionId = $("#regionId");
			$provinceId = $("#provinceId");
			$cityId = $("#cityId");
			$memCounterId = $("#memCounterId");
			$channelRegionId = $("#channelRegionId");
			$channelId = $("#channelId");
			$regionNameDiv = $("#regionNameDiv");
			$belongId = $("#belongId");
		}
		
		var callback = function(treeObj, selMode, selExclusiveFlagObj, popCouValidFlagObj, channelRegionObj) {
			if(treeObj == null) {
				return;
			}
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length > 0) {
				var regionId = "";
				var provinceId = "";
				var cityId = "";
				var memCounterId = "";
				var channelId = "";
				var belongId = "";

				// 节点ID值
				var nodeId = "";
				// 节点ID去掉第一个字母后的值
				var subNodeId = "";
				var count = 0;
				var selExclusiveFlag = selExclusiveFlagObj.selExclusiveFlag;
				var couValidFlag = popCouValidFlagObj.popCouValidFlag;
				var channelRegionId = channelRegionObj.channelRegionId;
				
				var regionNameDiv = selExclusiveFlagObj.selExclusiveFlagText+"("+popCouValidFlagObj.popCouValidFlagText+")：";
				if(selMode == "2") {
					if(channelRegionId) {
						regionNameDiv += channelRegionObj.channelRegionText + ",";
					}
				}
				for(var i = 0; i < nodes.length; i++) {
					var curNode = nodes[i];
					var parentNode = curNode.getParentNode();
					// 父节点不存在或者父节点为半选状态，而且当前节点没有子节点或者当前节点为全选状态，那么把该节点保存下来
					if((parentNode == null || parentNode.check_Child_State == 1) 
							&& (curNode.check_Child_State == 2 || curNode.check_Child_State == -1)) {
						nodeId = curNode.id;
						subNodeId = nodeId.substring(1, nodeId.length);
						// 区域节点
						if(nodeId.indexOf("R") > -1) {
							if(regionId == "") {
								regionId = subNodeId
							} else {
								regionId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("P") > -1) {// 省节点
							if(provinceId == "") {
								provinceId = subNodeId;
							} else {
								provinceId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("C") > -1) {// 城市节点
							if(cityId == "") {
								cityId = subNodeId;
							} else {
								cityId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("Q") > -1) {// 渠道节点
							if(channelId == "") {
								channelId = subNodeId;
							} else {
								channelId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("B") > -1) {
							if(belongId == "") {
								belongId = subNodeId;
							} else {
								belongId += "," + subNodeId;
							}
						} else {
							if(memCounterId == "") {// 柜台节点
								memCounterId = subNodeId;
							} else {
								memCounterId += "," + subNodeId;
							}
						}
						count++;
						if(count <= 3) {
							if(count == 1) {
								regionNameDiv += curNode.name;
							} else {
								regionNameDiv += "," + curNode.name;
							}
						}
					}
				}
				if(count > 3) {
					regionNameDiv += "...";
				}
				$exclusiveFlag.val(selExclusiveFlag);
				$modeFlag.val(selMode);
				$couValidFlag.val(couValidFlag);
				$regionId.val(regionId);
				$belongId.val(belongId);
				$provinceId.val(provinceId);
				$cityId.val(cityId);
				$memCounterId.val(memCounterId);
				$channelRegionId.val(channelRegionId);
				$channelId.val(channelId);
				regionNameDiv += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delProvinceHtml(' + isClub + ');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$regionNameDiv.html(regionNameDiv);
			} else {
				binolmbmbm09.delProvinceHtml(isClub);
			}
		}
		
		// 区域树初始默认选中值
		var value = {};
		var regionInfo = [];
		var regionId = $regionId.val();
		var provinceId = $provinceId.val();
		var cityId = $cityId.val();
		var memCounterId = $memCounterId.val();
		var channelRegionId = $channelRegionId.val();
		var channelId = $channelId.val();
		var exclusiveFlag = $exclusiveFlag.val();
		var modeFlag = $modeFlag.val();
		var couValidFlag = $couValidFlag.val();
		var belongId = $belongId.val();
		if(modeFlag == "1") {
			if(regionId) {
				var regionIds = regionId.split(",");
				for(var i = 0; i < regionIds.length; i++) {
					regionInfo.push("R"+regionIds[i]);
				}
			}
			if(provinceId) {
				var provinceIds = provinceId.split(",");
				for(var i = 0; i < provinceIds.length; i++) {
					regionInfo.push("P"+provinceIds[i]);
				}
			}
			if(cityId) {
				var cityIds = cityId.split(",");
				for(var i = 0; i < cityIds.length; i++) {
					regionInfo.push("C"+cityIds[i]);
				}
			}
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		} else if(modeFlag == "2") {
			value.channelRegionId = channelRegionId;
			if(channelId) {
				var channelIds = channelId.split(",");
				for(var i = 0; i < channelIds.length; i++) {
					regionInfo.push("Q"+channelIds[i]);
				}
			}
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		} else if(modeFlag == "3") {
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		} else if (modeFlag == "4") {
			if (belongId) {
				var belongIds = belongId.split(",");
				for(var i = 0; i < belongIds.length; i++) {
					regionInfo.push("B"+belongIds[i]);
				}
			}
		} else if (modeFlag == "5") {
			if (belongId) {
				var belongIds = belongId.split(",");
				for(var i = 0; i < belongIds.length; i++) {
					regionInfo.push("B"+belongIds[i]);
				}
			}
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		}
		value.regionInfo = regionInfo;
		value.selMode = modeFlag;
		value.exclusiveFlag = exclusiveFlag;
		value.couValidFlag = couValidFlag;
		var param = "privilegeFlg=1";
		if ($("#memberClubId").length > 0) {
			param += "&" + $("#memberClubId").serialize();
		}
		popRegionDialog(url, param, value, callback);
	},
	// 删除选择的发卡柜台（区域模式）
	"delProvinceHtml": function(isClub) {
		var $exclusiveFlag;
		var $modeFlag;
		var $couValidFlag;
		var $regionId;
		var $provinceId;
		var $cityId;
		var $memCounterId;
		var $channelRegionId;
		var $channelId;
		var $regionNameDiv;
		var $belongId;
		if ('1' == isClub) {
			$exclusiveFlag = $("#clubExclusiveFlag");
			$modeFlag = $("#clubModeFlag");
			$couValidFlag = $("#clubCouValidFlag");
			$regionId = $("#clubRegionId");
			$provinceId = $("#clubProvinceId");
			$cityId = $("#clubCityId");
			$memCounterId = $("#clubMemCounterId");
			$channelRegionId = $("#clubChannelRegionId");
			$channelId = $("#clubChannelId");
			$regionNameDiv = $("#clubRegionNameDiv");
			$belongId = $("#clubBelongId");
		} else {
			$exclusiveFlag = $("#exclusiveFlag");
			$modeFlag = $("#modeFlag");
			$couValidFlag = $("#couValidFlag");
			$regionId = $("#regionId");
			$provinceId = $("#provinceId");
			$cityId = $("#cityId");
			$memCounterId = $("#memCounterId");
			$channelRegionId = $("#channelRegionId");
			$channelId = $("#channelId");
			$regionNameDiv = $("#regionNameDiv");
			$belongId = $("#belongId");
		}
		$modeFlag.val("");
		$exclusiveFlag.val("");
		$couValidFlag.val("");
		$regionId.val("");
		$provinceId.val("");
		$cityId.val("");
		$memCounterId.val("");
		$channelRegionId.val("");
		$channelId.val("");
		$belongId.val("");
		$regionNameDiv.html("");
	},
	// 导出会员信息
	"exportExcel" : function(flag) {
        if($("#memberInfo").find(".dataTables_empty:visible").length==0) {
        	if($("#exportDialog").length == 0) {
    			$("body").append('<div style="display:none" id="exportDialog"></div>');
    		} else {
    			$("#exportDialog").empty();
    		}
    	    var dialogSetting = {
    	    		dialogInit: "#exportDialog",
    	    		text: $("#exportModeDiv").html(),
    	    		width: 400,
    	    		height: 200,
    	    		title: $("#pushTitleText").val(),
    	    		confirm: $("#dialogConfirm").text(),
    	    		cancel: $("#dialogCancel").text(),
    				confirmEvent: function(){
    					$("#memberCherryForm").find(":input[name=exportMode]").val($("#exportDialog").find(":input[name=exportMode]").val());
    					removeDialog("#exportDialog");
    					var callback = function(msg) {
    		        		var url = $("#exportUrl").attr("href");
    		        		$("#memberCherryForm").attr("action",url);
    		        		$("#memberCherryForm").submit();
    		        	};
    		        	exportExcelReport({
    		        		url: $("#exporChecktUrl").attr("href"),
    		        		param: $("#memberCherryForm").serialize(),
    		        		callback: callback
    		        	});
    				},
    				cancelEvent: function(){removeDialog("#exportDialog");}
    		};
    	    openDialog(dialogSetting);
        }
    },
    // 导出会员信息
	"exportCsv" : function() {
		if($("#memberInfo").find(".dataTables_empty:visible").length==0) {
			if($("#exportDialog").length == 0) {
    			$("body").append('<div style="display:none" id="exportDialog"></div>');
    		} else {
    			$("#exportDialog").empty();
    		}
    	    var dialogSetting = {
    	    		dialogInit: "#exportDialog",
    	    		text: $("#exportModeDiv").html(),
    	    		width: 400,
    	    		height: 200,
    	    		title: $("#pushTitleText").val(),
    	    		confirm: $("#dialogConfirm").text(),
    	    		cancel: $("#dialogCancel").text(),
    				confirmEvent: function(){
    					$("#memberCherryForm").find(":input[name=exportMode]").val($("#exportDialog").find(":input[name=exportMode]").val());
    					removeDialog("#exportDialog");
    					exportReport({
    						exportUrl:$("#exportCsvUrl").attr("href"),
    						exportParam:$("#memberCherryForm").serialize()
    					});
    				},
    				cancelEvent: function(){removeDialog("#exportDialog");}
    		};
    	    openDialog(dialogSetting);
		}
	},
	// 导出会员销售信息
	"exportMemSaleExcel" : function(flag) {
        if($("#memSaleInfo").find(".dataTables_empty:visible").length==0) {
        	var callback = function(msg) {
        		var url = $("#exportMemSaleUrl").attr("href");
        		$("#memberCherryForm").attr("action",url);
        		$("#memberCherryForm").submit();
        	};
        	exportExcelReport({
        		url: $("#exportMemSaleCheckUrl").attr("href"),
        		param: $("#memberCherryForm").serialize(),
        		callback: callback
        	});
        }
    },
	// 导出会员销售信息
	"exportMemSaleCsv" : function() {
		if($("#memSaleInfo").find(".dataTables_empty:visible").length==0) {
			exportReport({
				exportUrl:$("#exportMemSaleCsvUrl").attr("href"),
				exportParam:$("#memberCherryForm").serialize()
			});
		}
	},
    // 会员转柜初始画面表示
    "moveMemCounterInit": function(url) {
    	if($("#moveMemCounterDialog").length == 0) {
    		$("body").append('<div style="display:none" id="moveMemCounterDialog"></div>');
    	} else {
    		$("#moveMemCounterDialog").empty();
    	}
    	var dialogSetting = {
    		dialogInit: "#moveMemCounterDialog",
    		text:this.moveMemCounterDiv,
    		width: 	500,
    		height: 300,
    		title: 	$("#moveMemCouDialogTitle").text(),
    		confirm: $("#moveMemCouDialogConfirm").text(),
    		cancel: $("#moveMemCouDialogCancel").text(),
    		confirmEvent: function(){binolmbmbm09.moveMemCounter(url);},
    		cancelEvent: function(){removeDialog("#moveMemCounterDialog");}
    	};
    	openDialog(dialogSetting);
    },
    "changeMoveCouType": function(obj) {
    	var subType = $(obj).val();
    	$("#moveMemCounterDialog").find("#oldOrgId").val("");
		$("#moveMemCounterDialog").find("#oldCounterCode").val("");
		$("#moveMemCounterDialog").find("#oldOrgDiv").empty();
		$("#moveMemCounterDialog").find("#newOrgId").val("");
		$("#moveMemCounterDialog").find("#newCounterCode").val("");
		$("#moveMemCounterDialog").find("#newOrgDiv").empty();
		$("#moveMemCounterDialog").find("#batchCode").val("");
    	if(subType == "1") {
    		$("#moveMemCounterDialog").find("#moveCouType1").show();
    		$("#moveMemCounterDialog").find("#moveCouType2").hide();
    	} else {
    		$("#moveMemCounterDialog").find("#moveCouType2").show();
    		$("#moveMemCounterDialog").find("#moveCouType1").hide();
    	}
    },
    // 会员转柜处理
    "moveMemCounter": function(url) {
    	var param = $("#moveMemCounterDialog").find("#moveMemCounterForm").serialize();
    	$("#moveMemCounterDialog").html($("#moveMemCouDialogHandle").text());
    	var callback = function(msg) {
    		$("#moveMemCounterDialog").html(msg);
    		if($("#moveMemCounterDialog").find("#reMoveMemCounterDiv").length > 0) {
    			$("#moveMemCounterDialog").dialog( "option", {
    				buttons: [{
    					text: $("#moveMemCouDialogConfirm").text(),
    				    click: function(){binolmbmbm09.reMoveMemCounter();}
    				},
    				{
    					text: $("#moveMemCouDialogCancel").text(),
    				    click: function(){removeDialog("#moveMemCounterDialog");}
    				}]
    			});
    		} else {
    			$("#messageDiv").find("span").html($("#messageDiv").next().text());
    			$("#moveMemCounterDialog").dialog( "option", {
    				buttons: [{
    					text: $("#moveMemCouDialogClose").text(),
    				    click: function(){removeDialog("#moveMemCounterDialog");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
    				}],
    				closeEvent: function(){removeDialog("#moveMemCounterDialog");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
    			});
    		}
    	}
    	cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback,
    		isResultHandle: false
    	});
    },
    "reMoveMemCounter": function() {
    	var url = $("#reMoveMemCounterUrl").attr("href");
    	var param = $("#moveMemCounterDialog").find("#reMoveMemCounterForm").serialize();
    	$("#moveMemCounterDialog").html($("#moveMemCouDialogHandle").text());
    	var callback = function(msg) {
    		$("#moveMemCounterDialog").html(msg);
    		$("#messageDiv").find("span").html($("#messageDiv").next().text());
			$("#moveMemCounterDialog").dialog( "option", {
				buttons: [{
					text: $("#moveMemCouDialogClose").text(),
				    click: function(){removeDialog("#moveMemCounterDialog");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
				}],
				closeEvent: function(){removeDialog("#moveMemCounterDialog");if(oTableArr[0] != null)oTableArr[0].fnDraw();}
			});
    	}
    	cherryAjaxRequest({
    		url: url,
    		param: param,
    		callback: callback,
    		isResultHandle: false
    	});
    },
    // 会员转柜选择原柜台
    "popOldOrgDialog": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#moveMemCounterDialog").find("#oldOrgId").val($checkedRadio.val());
				$("#moveMemCounterDialog").find("#oldCounterCode").val($checkedRadio.next().val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delOrgHtml('+1+');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#moveMemCounterDialog").find("#oldOrgDiv").html(html);
			}
		}
		var value = $("#moveMemCounterDialog").find("#oldOrgId").val();
		var param = "validFlag=1&privilegeFlg=1";
		popDataTableOfCounterList(url, param, value, callback);
	},
	// 会员转柜选择新柜台
	"popNewOrgDialog": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#moveMemCounterDialog").find("#newOrgId").val($checkedRadio.val());
				$("#moveMemCounterDialog").find("#newCounterCode").val($checkedRadio.next().val());
				$("#moveMemCounterDialog").find("#newCounterKind").val($checkedRadio.next().next().val());
				var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delOrgHtml('+2+');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#moveMemCounterDialog").find("#newOrgDiv").html(html);
			}
		}
		var value = $("#moveMemCounterDialog").find("#newOrgId").val();
		var param = "privilegeFlg=1";
		popDataTableOfCounterList(url, param, value, callback);
	},
	// 会员转柜删除选择的柜台
	"delOrgHtml": function(flag) {
		if(flag == 1) {
			$("#moveMemCounterDialog").find("#oldOrgId").val("");
			$("#moveMemCounterDialog").find("#oldCounterCode").val("");
			$("#moveMemCounterDialog").find("#oldOrgDiv").empty();
		} else {
			$("#moveMemCounterDialog").find("#newOrgId").val("");
			$("#moveMemCounterDialog").find("#newCounterCode").val("");
			$("#moveMemCounterDialog").find("#newOrgDiv").empty();
		}
	},
	// 选择会员记录
	"checkAllRecord": function(object, id) {
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
	},
	// 会员启用停用处理
	"editValidFlag" : function(flag, url) {
		if($('#dataTable_Cloned :input[checked]').length == 0) {
			$("#errorMessage").html($("#errorMessageTemp").html());
			return false;
		}
		var param = "";
		var title = "";
		var text = "";
		param = $('#dataTable_Cloned :input[checked]').nextAll().serialize();
		if(flag == 'enable') {
			param += '&validFlag=1';
			title = $('#enableTitle').text();
			text = $('#enableMessage').html();
		} else {
			param += '&validFlag=0';
			title = $('#disableTitle').text();
			text = $('#disableMessage').html();
		}
		var callback = function() {
			if(oTableArr[0] != null)oTableArr[0].fnDraw();
		};
		
		if($("#editValidFlagDialog").length == 0) {
			$("body").append('<div style="display:none" id="editValidFlagDialog"></div>');
		} else {
			$("#editValidFlagDialog").empty();
		}
		var dialogSetting = {
			dialogInit: "#editValidFlagDialog",
			text: text,
			width: 	500,
			height: 300,
			title: 	title,
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){binolmbmbm09.editValidFlagHandle(url, param, callback);},
			cancelEvent: function(){removeDialog("#editValidFlagDialog");}
		};
		openDialog(dialogSetting);
	},
	"editValidFlagHandle": function(url, param, delCallback) {
		var callback = function(msg) {
			$("#editValidFlagDialog").html(msg);
			if($("#errorMessageDiv").length > 0) {
				$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
				$("#editValidFlagDialog").dialog( "option", {
					buttons: [{
						text: $("#dialogClose").text(),
					    click: function(){removeDialog("#editValidFlagDialog");}
					}]
				});
			} else {
				removeDialog("#editValidFlagDialog");
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
	},
	
	// 停用已选条件的全部会员处理
	"delAllMemInit" : function() {
		if($("#delAllMemDialog").length == 0) {
			$("body").append('<div style="display:none" id="delAllMemDialog"></div>');
		} else {
			$("#delAllMemDialog").empty();
		}
		var dialogSetting = {
			dialogInit: "#delAllMemDialog",
			text: $("#delAllMemDiv").html(),
			width: 	500,
			height: 300,
			title: 	$("#disableTitle").html(),
			confirm: $("#dialogConfirm").text(),
			cancel: $("#dialogCancel").text(),
			confirmEvent: function(){
				if($("#delAllMemDialog").find("#remark").val() != '') {
					binolmbmbm09.delAllMem();
				} else {
					var $parent = $("#delAllMemDialog").find("#remark").parent();
					$parent.addClass("error");
					$parent.append('<span class="ui-icon icon-error tooltip-trigger-error" id="errorText"></span>');
					$parent.find('#errorText').attr("title",'error|'+$("#delAllMemCheckMes").html());
					$parent.find('#errorText').cluetip({
				    	splitTitle: '|',
					    width: 150,
					    cluezIndex: 20000,
					    tracking: true,
					    cluetipClass: 'error', 
					    arrows: true, 
					    dropShadow: false,
					    hoverIntent: false,
					    sticky: false,
					    mouseOutClose: true,
					    closePosition: 'title',
					    closeText: '<span class="ui-icon ui-icon-close"></span>'
					});
				}
			},
			cancelEvent: function(){removeDialog("#delAllMemDialog");}
		};
		openDialog(dialogSetting);
	},
	"delAllMem": function() {
		var param = $("#memberCherryForm").serialize() + '&' + $("#delAllMemDialog").find("#remark").serialize();
		$("#delAllMemDialog").html($("#dialogHandle").text());
		$("#delAllMemDialog").dialog( "option", {
			buttons: [{
				text: $("#dialogClose").text(),
			    click: function(){removeDialog("#delAllMemDialog");}
			}]
		});
		var callback = function(msg) {
			$("#delAllMemDialog").html(msg);
			if($("#errorMessageDiv").length > 0) {
				$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
				$("#delAllMemDialog").dialog( "option", {
					buttons: [{
						text: $("#dialogClose").text(),
					    click: function(){removeDialog("#delAllMemDialog");}
					}]
				});
			} else {
				removeDialog("#delAllMemDialog");
				if(oTableArr[0] != null)oTableArr[0].fnDraw();
			}
		};
		cherryAjaxRequest({
			url: $("#delAllMemUrl").attr("href"),
			param: param,
			callback: callback
		});
	},
	"selectDateMode": function(object, kbn) {
		if (1 == kbn) {
			if($(object).val() == "-1") {
				$(object).parent().next().hide();
				$(object).parent().next().next().hide();
				$(object).parent().next().next().next().hide();
			} else {
				if($(object).val() == "9") {
					$(object).parent().next().next().show();
					$(object).parent().next().hide();
					$(object).parent().next().next().next().hide();
				} else if($(object).val() == "0") {
					$(object).parent().next().next().hide();
					$(object).parent().next().next().next().hide();
					$(object).parent().next().show();
				} else {
					$(object).parent().next().next().hide();
					$(object).parent().next().hide();
					$(object).parent().next().next().next().show();
				}
			}
		} else {
			if($(object).val() == "-1") {
				$(object).parent().next().hide();
				$(object).parent().next().next().hide();
				$(object).parent().next().next().next().hide();
			} else {
				if($(object).val() == "9") {
					$(object).parent().next().next().show();
					$(object).parent().next().hide();
				} else if($(object).val() == "0") {
					$(object).parent().next().next().hide();
					$(object).parent().next().show();
				} else {
					$(object).parent().next().next().hide();
					$(object).parent().next().hide();
				}
				$(object).parent().next().next().next().show();
			}
		}
	},
	"selectBirthDayMode": function(object) {
		if($(object).val() == "-1") {
			$(object).parent().next().hide();
			$(object).parent().next().next().hide();
			$(object).parent().next().next().next().hide();
			$(object).parent().next().next().next().next().hide();
			$(object).parent().next().next().next().next().next().hide();
		} else {
			if($(object).val() == "0") {
				$(object).parent().next().show();
				$(object).parent().next().next().hide();
				$(object).parent().next().next().next().hide();
				$(object).parent().next().next().next().next().hide();
				$(object).parent().next().next().next().next().next().hide();
			} else if($(object).val() == "1") {
				$(object).parent().next().hide();
				$(object).parent().next().next().show();
				$(object).parent().next().next().next().hide();
				$(object).parent().next().next().next().next().hide();
				$(object).parent().next().next().next().next().next().hide();
			} else if($(object).val() == "2") {
				$(object).parent().next().hide();
				$(object).parent().next().next().hide();
				$(object).parent().next().next().next().show();
				$(object).parent().next().next().next().next().hide();
				$(object).parent().next().next().next().next().next().hide();
			} else if($(object).val() == "9") {
				$(object).parent().next().hide();
				$(object).parent().next().next().hide();
				$(object).parent().next().next().next().hide();
				$(object).parent().next().next().next().next().show();
				$(object).parent().next().next().next().next().next().hide();
			} else if($(object).val() == "3") {
				$(object).parent().next().hide();
				$(object).parent().next().next().hide();
				$(object).parent().next().next().next().hide();
				$(object).parent().next().next().next().next().hide();
				$(object).parent().next().next().next().next().next().show();
			}
		}
	},
	"selectLevelAdjustDayMode": function(object) {
		if($(object).val() == "-1") {
			$(object).parent().next().hide();
			$(object).parent().next().next().hide();
			$(object).parent().next().next().next().hide();
		} else {
			if($(object).val() == "1") {
				$(object).parent().next().next().hide();
				$(object).parent().next().hide();
			} else if($(object).val() == "2") {
				$(object).parent().next().next().hide();
				$(object).parent().next().show();
			} else {
				$(object).parent().next().next().show();
				$(object).parent().next().hide();
			}
			$(object).parent().next().next().next().show();
		}
	},
	"selectMonth": function(object,flag) {
		var $date = $(object).next();
		var month = $(object).val();
		var i = 1;
		var max = 0;
		var options = '<option value="">'+$(object).find('option').first().html()+'</option>';
		if(month == "") {
			$date.html(options);
			return;
		}
		if(flag == '1') {
			options = '';
		}
		if(month == '2') {
			max = 29;
		} else if(month == '4' || month == '6' || month == '9' || month == '11') {
			max = 30;
		} else {
			max = 31;
		}
		for(i = 1; i <= max; i++) {
			options += '<option value="'+i+'">'+i+'</option>';
		}
		$date.html(options);
	},
	"selectReferFlag": function(object) {
		if($(object).val() == "1") {
			$(object).parent().next().next().hide();
			$(object).parent().next().show();
		} else if($(object).val() == "2") {
			$(object).parent().next().hide();
			$(object).parent().next().next().show();
		} else {
			$(object).parent().next().hide();
			$(object).parent().next().next().hide();
		}
	},
	// 选择活动弹出框
	"popCampaignList": function(url) {
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$("#campaignMode").val($checkedRadio.next().val());
				$("#campaignCode").val($checkedRadio.val());
				var html = $checkedRadio.parent().next().text();
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#campaignDiv").html(html);
			}
		}
		var value = $("#campaignCode").val();
		popDataTableOfCampaignList(url, null, value, callback);
	},
	// 删除选择的活动
	"delCampaignHtml": function(object) {
		$("#campaignMode").val("");
		$("#campaignCode").val("");
		$("#campaignDiv").empty();
	},
	// 重置查询条件
	"resetCondition": function(formId) {
//		$(formId)[0].reset();
		$(formId).find(':text').val('');
		$(formId).find(':checkbox, :radio').removeAttr('checked');
		$(formId).find('select').val('');
		binolmbmbm09.selectDateMode($(formId).find(':input[name="joinDateMode"]')[0]);
		binolmbmbm09.selectBirthDayMode($(formId).find(':input[name="birthDayMode"]')[0]);
		binolmbmbm09.selectLevelAdjustDayMode($(formId).find(':input[name="levelAdjustDayFlag"]')[0]);
		binolmbmbm09.selectMonth($(formId).find(':input[name="birthDayMonth"]')[0],'0');
		binolmbmbm09.selectMonth($(formId).find(':input[name="birthDayMonthRangeStart"]')[0],'1');
		binolmbmbm09.selectMonth($(formId).find(':input[name="birthDayMonthRangeEnd"]')[0],'1');
		binolmbmbm09.selectReferFlag($(formId).find(':input[name="referFlag"]')[0]);
		binolmbmbm09.delProvinceHtml();
		binolmbmbm09.selectDateMode($(formId).find(':input[name="saleTimeMode"]')[0]);
		binolmbmbm09.delSaleCounterHtml();
		binolmbmbm09.delSaleProvinceHtml();
		binolmbmbm09.selectDateMode($(formId).find(':input[name="notSaleTimeMode"]')[0]);
		$("#isSaleFlag1").attr("checked", "checked");
		binolmbmbm09.changeSaleFlag($("#isSaleFlag1")[0]);
		$("#relation2").attr("checked", "checked");
		$("#notRelation2").attr("checked", "checked");
		$("#proShowDiv").empty();
		$("#proTable").hide();
		$("#proTypeShowDiv").empty();
		$("#proTypeTable").hide();
		$("#notProShowDiv").empty();
		$("#notProTable").hide();
		$("#notProTypeShowDiv").empty();
		$("#notProTypeTable").hide();
		binolmbmbm09.delCampaignHtml();
		binolmbmbm09.delCampaignCounterHtml();
		if ($('#memberClubId').length > 0) {
			binolmbmbm09.cleanClubAttrs('');
		}
		if ($('[id^="memPrtCate_"]').length > 0) {
			$('[id^="memPrtCate_"]').empty();
			$('[id^="memPrt_"]').empty();
		}
		
	},
	// 改变购买记录查询条件
	"changeSaleFlag": function(objcet) {
		if($(objcet).val() == '1') {
			$(objcet).parents("tr.changeSaleFlag").parent().find("tr.hasSale").show();
			$(objcet).parents("tr.changeSaleFlag").parent().find("tr.notSale").hide();
		} else {
			$(objcet).parents("tr.changeSaleFlag").parent().find("tr.hasSale").hide();
			$(objcet).parents("tr.changeSaleFlag").parent().find("tr.notSale").show();
		}
	},
	// 选择发卡BA弹出框
	"popEmployeeList": function(url, isClub) {
		if ('1' == isClub && $("#memberClubId").val() == "") {
			binolmbmbm09.clubWarnDialog();
			return false;
		}
		var $employeeId;
		var $employeeCode;
		var $employeeDiv;
		if ('1' == isClub) {
			$employeeId = $("#clubEmployeeId");
			$employeeCode = $("#clubEmployeeCode");
			$employeeDiv = $("#clubEmployeeDiv");
		} else {
			$employeeId = $("#employeeId");
			$employeeCode = $("#employeeCode");
			$employeeDiv = $("#employeeDiv");
		}
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$employeeId.val($checkedRadio.val());
				$employeeCode.val($checkedRadio.next().val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delEmpHtml(this,' + isClub +');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$employeeDiv.html(html);
			}
		}
		var param = "privilegeFlg=1";
		var value = $employeeId.val();
		popDataTableOfEmployeeList(url, param, value, callback);
	},
	"delEmpHtml": function(object, isClub) {
		var $employeeId;
		var $employeeCode;
		var $employeeDiv;
		if ('1' == isClub) {
			$employeeId = $("#clubEmployeeId");
			$employeeCode = $("#clubEmployeeCode");
			$employeeDiv = $("#clubEmployeeDiv");
		} else {
			$employeeId = $("#employeeId");
			$employeeCode = $("#employeeCode");
			$employeeDiv = $("#employeeDiv");
		}
		$employeeId.val("");
		$employeeCode.val("");
		$employeeDiv.empty();
	},
	// 选择购买柜台弹出框（区域模式）
	"popSaleRegionDialog": function(url) {
		var callback = function(treeObj, selMode, selExclusiveFlagObj, popCouValidFlagObj, channelRegionObj) {
			if(treeObj == null) {
				return;
			}
			var nodes = treeObj.getCheckedNodes(true);
			if(nodes.length > 0) {
				var regionId = "";
				var provinceId = "";
				var cityId = "";
				var memCounterId = "";
				var channelId = "";
				var belongId = "";
				// 节点ID值
				var nodeId = "";
				// 节点ID去掉第一个字母后的值
				var subNodeId = "";
				var count = 0;
				var selExclusiveFlag = selExclusiveFlagObj.selExclusiveFlag;
				var couValidFlag = popCouValidFlagObj.popCouValidFlag;
				var channelRegionId = channelRegionObj.channelRegionId;
				
				var regionNameDiv = popCouValidFlagObj.popCouValidFlagText+"：";
				if(selMode == "2") {
					if(channelRegionId) {
						regionNameDiv += channelRegionObj.channelRegionText + ",";
					}
				}
				for(var i = 0; i < nodes.length; i++) {
					var curNode = nodes[i];
					var parentNode = curNode.getParentNode();
					// 父节点不存在或者父节点为半选状态，而且当前节点没有子节点或者当前节点为全选状态，那么把该节点保存下来
					if((parentNode == null || parentNode.check_Child_State == 1) 
							&& (curNode.check_Child_State == 2 || curNode.check_Child_State == -1)) {
						nodeId = curNode.id;
						subNodeId = nodeId.substring(1, nodeId.length);
						// 区域节点
						if(nodeId.indexOf("R") > -1) {
							if(regionId == "") {
								regionId = subNodeId
							} else {
								regionId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("P") > -1) {// 省节点
							if(provinceId == "") {
								provinceId = subNodeId;
							} else {
								provinceId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("C") > -1) {// 城市节点
							if(cityId == "") {
								cityId = subNodeId;
							} else {
								cityId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("Q") > -1) {// 渠道节点
							if(channelId == "") {
								channelId = subNodeId;
							} else {
								channelId += "," + subNodeId;
							}
						} else if(nodeId.indexOf("B") > -1) {
							if(belongId == "") {
								belongId = subNodeId;
							} else {
								belongId += "," + subNodeId;
							}
						} else {
							if(memCounterId == "") {// 柜台节点
								memCounterId = subNodeId;
							} else {
								memCounterId += "," + subNodeId;
							}
						}
						count++;
						if(count <= 3) {
							if(count == 1) {
								regionNameDiv += curNode.name;
							} else {
								regionNameDiv += "," + curNode.name;
							}
						}
					}
				}
				if(count > 3) {
					regionNameDiv += "...";
				}
				$("#saleExclusiveFlag").val(selExclusiveFlag);
				$("#saleModeFlag").val(selMode);
				$("#saleCouValidFlag").val(couValidFlag);
				$("#saleRegionId").val(regionId);
				$("#saleProvinceId").val(provinceId);
				$("#saleCityId").val(cityId);
				$("#saleMemCounterId").val(memCounterId);
				$("#saleChannelRegionId").val(channelRegionId);
				$("#saleChannelId").val(channelId);
				$("#saleBelongId").val(belongId);
				regionNameDiv += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbmbm09.delSaleProvinceHtml();"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$("#saleRegionNameDiv").html(regionNameDiv);
			} else {
				binolmbmbm09.delSaleProvinceHtml();
			}
		}
		
		// 区域树初始默认选中值
		var value = {};
		var regionInfo = [];
		var regionId = $("#saleRegionId").val();
		var provinceId = $("#saleProvinceId").val();
		var cityId = $("#saleCityId").val();
		var memCounterId = $("#saleMemCounterId").val();
		var channelRegionId = $("#saleChannelRegionId").val();
		var channelId = $("#saleChannelId").val();
		var exclusiveFlag = $("#saleExclusiveFlag").val();
		var modeFlag = $("#saleModeFlag").val();
		var couValidFlag = $("#saleCouValidFlag").val();
		var belongId = $("#saleBelongId").val();
		if(modeFlag == "1") {
			if(regionId) {
				var regionIds = regionId.split(",");
				for(var i = 0; i < regionIds.length; i++) {
					regionInfo.push("R"+regionIds[i]);
				}
			}
			if(provinceId) {
				var provinceIds = provinceId.split(",");
				for(var i = 0; i < provinceIds.length; i++) {
					regionInfo.push("P"+provinceIds[i]);
				}
			}
			if(cityId) {
				var cityIds = cityId.split(",");
				for(var i = 0; i < cityIds.length; i++) {
					regionInfo.push("C"+cityIds[i]);
				}
			}
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		} else if(modeFlag == "2") {
			value.channelRegionId = channelRegionId;
			if(channelId) {
				var channelIds = channelId.split(",");
				for(var i = 0; i < channelIds.length; i++) {
					regionInfo.push("Q"+channelIds[i]);
				}
			}
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		} else if(modeFlag == "3") {
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		} else if (modeFlag == "4") {
			if (belongId) {
				var belongIds = belongId.split(",");
				for(var i = 0; i < belongIds.length; i++) {
					regionInfo.push("B"+belongIds[i]);
				}
			}
		} else if (modeFlag == "5") {
			if (belongId) {
				var belongIds = belongId.split(",");
				for(var i = 0; i < belongIds.length; i++) {
					regionInfo.push("B"+belongIds[i]);
				}
			}
			if(memCounterId) {
				var memCounterIds = memCounterId.split(",");
				for(var i = 0; i < memCounterIds.length; i++) {
					regionInfo.push("D"+memCounterIds[i]);
				}
			}
		}
		value.regionInfo = regionInfo;
		value.selMode = modeFlag;
		value.exclusiveFlag = exclusiveFlag;
		value.couValidFlag = couValidFlag;
		var param = "privilegeFlg=1&hasSelExclusiveFlag=0";
		popRegionDialog(url, param, value, callback);
	},
	// 删除选择的购买柜台（区域模式）
	"delSaleProvinceHtml": function() {
		$("#saleModeFlag").val("");
		$("#saleExclusiveFlag").val("");
		$("#saleCouValidFlag").val("");
		$("#saleRegionId").val("");
		$("#saleProvinceId").val("");
		$("#saleCityId").val("");
		$("#saleMemCounterId").val("");
		$("#saleChannelRegionId").val("");
		$("#saleChannelId").val("");
		$("#saleBelongId").val("");
		$("#saleRegionNameDiv").html("");
	},
	"changeLevel" : function (obj) {
    	var clubId = $(obj).val();
    	binolmbmbm09.cleanClubAttrs(clubId);
    	if ("" != clubId) {
    		var url = $("#searchLevelUrl").attr("href");
    		var param = $("#memberClubId").serialize() + "&" + $("#memberInfoId").serialize() + "&" + "csrftoken=" + getTokenVal();
    		$.ajax({
    	        url: url, 
    	        type: 'post',
    	        data: param,
    	        success: function(msg){
    						if(window.JSON && window.JSON.parse) {
    							var msgJson = window.JSON.parse(msg);
    							if (msgJson) {
    								var htm = "";
	    					    	for (var one in msgJson){
	    					    		htm += '<input type="checkbox" id="memLevel-' + one + '" value="' + msgJson[one]["memberLevelId"] +  '" name="memLevel">' +
	    					    		' <label class="checkboxLabel" for="memLevel-' + one + '">' + msgJson[one]["levelName"] + '</label>'
	    							    
	    						    }
	    					    	$("#levelSpan").html(htm);
    							}
    						}
    					}
    	    });
    	} else {
    		$("#levelSpan").empty();
    	}
    },
    "cleanClubAttrs" : function(kbn) {
    	$('#clubAttrs').find(':text').val('');
    	$('#clubAttrs').find('select').not('#memberClubId').val('');
    	if ("" != kbn) {
    		if ($("#memberPointStart").is(':disabled')) {
    			$('.club-attr','#clubAttrs').prop("disabled", false);
    			$('.no-club-date','#clubAttrs').hide();
    			$('.club-date','#clubAttrs').show();
    		}
    	} else {
    		if (!$("#memberPointStart").is(':disabled')) {
	    		$('.club-attr','#clubAttrs').prop("disabled", true);
				$('.club-date','#clubAttrs').hide();
				$('.no-club-date','#clubAttrs').show();
				$('#levelSpan').empty();
    		}
    	}
    	binolmbmbm09.delProvinceHtml('1');
		binolmbmbm09.delEmpHtml(null,'1');
		binolmbmbm09.selectReferFlag($('#clubAttrs').find(':input[name="referFlag"]')[0]);
    },
    //弹出确认框
	"clubWarnDialog" : function() {
		var dialogSetting = {
				dialogInit: "#dialogClubWarn",
				text: "<p class='message'><span>" + $('#clubSelMsg').text() + "</span></p>",
				width: 	500,
				height: 300,
				title: 	$('#clubWarnMsg').text(),
				cancel: $('#dialogClose').text(),
				cancelEvent: function(){
					removeDialog($("#dialogClubWarn"));
				}
			};
		openDialog(dialogSetting);
	},
	//选择购买产品大类弹出
    "openCateDialog" : function(cateId, targetId, propId,_this,checkType){
		var checkType=typeof(checkType)=="undefined" || checkType=="" || checkType==null ? "radio":"checkbox";
		var $this = $(_this);
		var cateInfo = "[{}]";
		var option = {
         	   targetId: targetId,
         	   propId : propId,
         	  checkType :checkType,
 	           cateInfo :cateInfo,
 	           mode : 2,
 	           click:function(){},
 		       getHtmlFun:function(info){
 		    	   	  var html;
 		    	      if(checkType=="checkbox"){
 		    	    	html = '<tr style="float : left"><td><span class="list_normal">';
 		    	       }else{
 		    	    	html = '<tr><td><span class="list_normal">';   
 		    	       }
		       			html += '<span class="text" style="line-height:19px;">' + info.cateValName + '</span>';
		       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="binolmbmbm09.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
  		       			html += '<input type="hidden" name="cateValId" value="' + info.cateValId + '"/>';
  		       			html += '<input type="hidden" name="' + cateId + '" value="' + info.cateValId + '"/>';
  		       			html += '</span></td></tr>';
 				return html;
 	           }
 	        };
 		popAjaxPrtCateDialog(option);
	},
	// 选择购买产品弹出框
    "openMemProDialog" :function(propId) {
    	var prtId;
    	if (1 == propId) {
    		prtId = "mostPrtId";
    	} else {
    		prtId = "jointPrtId";
    	}
		var option = {
         	   	targetId: "memPrt_" + propId,
 	           	checkType : "checkbox",
 	           	mode : 2,
 	            popValidFlag: 2,
 	           	brandInfoId : $("#brandInfoId").val(),
 	           	click:function(){},
 		       	getHtmlFun:function(info){
 		       	 var html = '<tr style="float : left"><td><span class="list_normal">';
	       			html += '<span class="text" style="line-height:19px;">' + info.nameTotal + '(U: ' + info.unitCode + ' B: ' + info.barCode +  ')</span>';
	       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="binolmbmbm09.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
	       			html += '<input type="hidden" name="prtVendorId" value="' + info.proId + '"/>';
	       			html += '<input type="hidden" name="' + prtId + '" value="' + info.proId + '"/>';
	       			html += '</span></td></tr>';
     				return html;
			   	}
 	    };
 		popAjaxPrtDialog(option);
	},
	// 删除分类
	"removeCate":function(_this){
		// 分类块
		var $obj = $(_this).parent().parent().parent();
		$obj.remove();
	},
	"addJoinDateRange":function(index) {
		var length = $("#joinDateRangeDiv").find('ul').find('li').length;
		$("#joinDateRangeDiv").find('ul').append('<li>'
			+'<input name="joinDateStart" class="date" id="joinDateStart'+index+'_'+length+'" style="width:80px"/>-<input name="joinDateEnd" class="date" id="joinDateEnd'+index+'_'+length+'" style="width:80px"/>'
			+'<a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>'
			+'</li>');
		$('#joinDateStart'+index+'_'+length).cherryDate({
			beforeShow: function(input){
				var value = $('#joinDateEnd'+index+'_'+length).val();
				return [value,'maxDate'];
			}
		});
		$('#joinDateEnd'+index+'_'+length).cherryDate({
			beforeShow: function(input){
				var value = $('#joinDateStart'+index+'_'+length).val();
				return [value,'minDate'];
			}
		});
	},
	"addMemPointRange":function() {
		$("#memPointRangeDiv").find('ul').append('<li>'
			+'<input name="memberPointStart" class="text" style="width:75px"/>-<input name="memberPointEnd" class="text" style="width:75px"/>'
			+'<a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>'
			+'</li>');
	},
	"addChangablePointRange":function() {
		$("#changablePointRangeDiv").find('ul').append('<li>'
			+'<input name="changablePointStart" class="text" style="width:75px"/>-<input name="changablePointEnd" class="text" style="width:75px"/>'
			+'<a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>'
			+'</li>');
	},
	"addLastSaleTimeRange":function(index) {
		var length = $("#lastSaleTimeRangeDiv").find('ul').find('li').length;
		$("#lastSaleTimeRangeDiv").find('ul').append('<li>'
			+'<input name="lastSaleDateStart" class="date" id="lastSaleDateStart'+index+'_'+length+'" style="width:80px"/>-<input name="lastSaleDateEnd" class="date" id="lastSaleDateEnd'+index+'_'+length+'" style="width:80px"/>'
			+'<a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>'
			+'</li>');
		$('#lastSaleDateStart'+index+'_'+length).cherryDate({
			beforeShow: function(input){
				var value = $('#lastSaleDateEnd'+index+'_'+length).val();
				return [value,'maxDate'];
			}
		});
		$('#lastSaleDateEnd'+index+'_'+length).cherryDate({
			beforeShow: function(input){
				var value = $('#lastSaleDateStart'+index+'_'+length).val();
				return [value,'minDate'];
			}
		});
	},
	"addFirstSaleTimeRange":function(index) {
		var length = $("#firstSaleTimeRangeDiv").find('ul').find('li').length;
		$("#firstSaleTimeRangeDiv").find('ul').append('<li>'
			+'<input name="firstStartDay" class="date" id="firstStartDay'+index+'_'+length+'" style="width:80px"/>-<input name="firstEndDay" class="date" id="firstEndDay'+index+'_'+length+'" style="width:80px"/>'
			+'<a href="#" class="right" onclick="$(this).parent().remove(); return false;" role="button"><span class="ui-icon icon-delete-big">close</span></a>'
			+'</li>');
		$('#firstStartDay'+index+'_'+length).cherryDate({
			beforeShow: function(input){
				var value = $('#firstEndDay'+index+'_'+length).val();
				return [value,'maxDate'];
			}
		});
		$('#firstEndDay'+index+'_'+length).cherryDate({
			beforeShow: function(input){
				var value = $('#firstStartDay'+index+'_'+length).val();
				return [value,'minDate'];
			}
		});
	},
	"setJsonParam":function(obj){
		var $obj = $(obj);
		var joinDateRanges = [];
		$obj.find("#joinDateRangeDiv").find("li").each(function(){
			var start = $(this).find(":input[name=joinDateStart]").val();
			var end = $(this).find(":input[name=joinDateEnd]").val();
			if(start != '' || end != '') {
				var joinDateRange = {
					"joinDateStart":start,
					"joinDateEnd":end
				}
				joinDateRanges.push(joinDateRange);
			}
		});
		if(joinDateRanges.length > 0) {
			$obj.find("#joinDateRangeJson").val(JSON.stringify(joinDateRanges));
		} else {
			$obj.find("#joinDateRangeJson").val("");
		}

		var memPointRanges = [];
		$obj.find("#memPointRangeDiv").find("li").each(function(){
			var start = $(this).find(":input[name=memberPointStart]").val();
			var end = $(this).find(":input[name=memberPointEnd]").val();
			if(start != '' || end != '') {
				var memPointRange = {
					"memberPointStart":start,
					"memberPointEnd":end
				}
				memPointRanges.push(memPointRange);
			}
		});
		if(memPointRanges.length > 0) {
			$obj.find("#memPointRangeJson").val(JSON.stringify(memPointRanges));
		} else {
			$obj.find("#memPointRangeJson").val("");
		}

		var changablePointRanges = [];
		$obj.find("#changablePointRangeDiv").find("li").each(function(){
			var start = $(this).find(":input[name=changablePointStart]").val();
			var end = $(this).find(":input[name=changablePointEnd]").val();
			if(start != '' || end != '') {
				var changablePointRange = {
					"changablePointStart":start,
					"changablePointEnd":end
				}
				changablePointRanges.push(changablePointRange);
			}
		});
		if(changablePointRanges.length > 0) {
			$obj.find("#changablePointRangeJson").val(JSON.stringify(changablePointRanges));
		} else {
			$obj.find("#changablePointRangeJson").val("");
		}

		var lastSaleTimeRanges = [];
		$obj.find("#lastSaleTimeRangeDiv").find("li").each(function(){
			var start = $(this).find(":input[name=lastSaleDateStart]").val();
			var end = $(this).find(":input[name=lastSaleDateEnd]").val();
			if(start != '' || end != '') {
				var lastSaleTimeRange = {
					"lastSaleDateStart":start,
					"lastSaleDateEnd":end
				}
				lastSaleTimeRanges.push(lastSaleTimeRange);
			}
		});
		if(lastSaleTimeRanges.length > 0) {
			$obj.find("#lastSaleTimeRangeJson").val(JSON.stringify(lastSaleTimeRanges));
		} else {
			$obj.find("#lastSaleTimeRangeJson").val("");
		}

		var firstSaleTimeRanges = [];
		$obj.find("#firstSaleTimeRangeDiv").find("li").each(function(){
			var start = $(this).find(":input[name=firstStartDay]").val();
			var end = $(this).find(":input[name=firstEndDay]").val();
			if(start != '' || end != '') {
				var firstSaleTimeRange = {
					"firstStartDay":start,
					"firstEndDay":end
				}
				firstSaleTimeRanges.push(firstSaleTimeRange);
			}
		});
		if(firstSaleTimeRanges.length > 0) {
			$obj.find("#firstSaleTimeRangeJson").val(JSON.stringify(firstSaleTimeRanges));
		} else {
			$obj.find("#firstSaleTimeRangeJson").val("");
		}
	},
	"selectFirstDayMode":function(obj) {
		var mode = $(obj).val();
		$("#spanNoSaleDaysMode1,#spanNoSaleDaysMode2").hide();
		$("#spanNoSaleDaysMode1,#spanNoSaleDaysMode2").find(":input").val("");
		$("#spanNoSaleDaysMode" + mode).show();
	}
};


var binolmbmbm09 =  new BINOLMBMBM09();

$(function(){
	// 打开关闭会员高级搜索事件绑定
	$('#expandCondition').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-n')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			$('#moreCondition').show();
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			$('#moreCondition').hide();
		}
	});
	$('#joinDateStart0_0').cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateEnd0_0').val();
			return [value,'maxDate'];
		}
	});
	$('#joinDateEnd0_0').cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateStart0_0').val();
			return [value,'minDate'];
		}
	});
	$('#saleTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#saleTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#saleTimeStart').val();
			return [value,'minDate'];
		}
	});
	$('#notSaleTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#notSaleTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#notSaleTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#notSaleTimeStart').val();
			return [value,'minDate'];
		}
	});
	$('#participateTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#participateTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#participateTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#participateTimeStart').val();
			return [value,'minDate'];
		}
	});
	$('#curDealDateStart').cherryDate({
		beforeShow: function(input){
			var value = $('#curDealDateEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#curDealDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#curDealDateStart').val();
			return [value,'minDate'];
		}
	});
	$('#wechatBindTimeStart').cherryDate({
		beforeShow: function(input){
			var value = $('#wechatBindTimeEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#wechatBindTimeEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#wechatBindTimeStart').val();
			return [value,'minDate'];
		}
	});
	$('#levelAdjustDayStart').cherryDate({
		beforeShow: function(input){
			var value = $('#levelAdjustDayEnd').val();
			return [value,'maxDate'];
		}
	});
	$('#levelAdjustDayEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#levelAdjustDayStart').val();
			return [value,'minDate'];
		}
	});
	$('#lastSaleDateStart0_0').cherryDate({
		beforeShow: function(input){
			var value = $('#lastSaleDateEnd0_0').val();
			return [value,'maxDate'];
		}
	});
	$('#lastSaleDateEnd0_0').cherryDate({
		beforeShow: function(input){
			var value = $('#lastSaleDateStart0_0').val();
			return [value,'minDate'];
		}
	});
	$('#firstStartDay0_0').cherryDate({
		beforeShow: function(input){
			var value = $('#firstEndDay0_0').val();
			return [value,'maxDate'];
		}
	});
	$('#firstEndDay0_0').cherryDate({
		beforeShow: function(input){
			var value = $('#firstStartDay0_0').val();
			return [value,'minDate'];
		}
	});
	var rules = {
			joinDateStart: {dateValid: true},
			joinDateEnd: {dateValid: true},
			joinDateRange: {integerValid: true},
			birthDayDateStart: {integerValid: true,rangeValid: [1,31]},
			birthDayDateEnd: {integerValid: true,rangeValid: [1,31]},
			birthDayRange: {integerValid: true},
			memberPointStart: {numberValid: true},
			memberPointEnd: {numberValid: true},
			changablePointStart: {numberValid: true},
			changablePointEnd: {numberValid: true},
			curDealDateStart: {dateValid: true},
			curDealDateEnd: {dateValid: true},
			ageStart: {integerValid: true},
			ageEnd: {integerValid: true},
			saleTimeRange: {integerValid: true},
			saleTimeStart: {dateValid: true},
			saleTimeEnd: {dateValid: true},
			saleCountStart: {integerValid: true},
			saleCountEnd: {integerValid: true},
			payAmountStart: {numberValid: true},
			payAmountEnd: {numberValid: true},
			notSaleTimeRange: {integerValid: true},
			notSaleTimeRangeLast: {integerValid: true},
			notSaleTimeStart: {dateValid: true},
			notSaleTimeEnd: {dateValid: true},
			participateTimeStart: {dateValid: true},
			participateTimeEnd: {dateValid: true},
			wechatBindTimeStart: {dateValid: true},
			wechatBindTimeEnd: {dateValid: true},
			levelAdjustDayStart: {dateValid: true},
			levelAdjustDayEnd: {dateValid: true},
			lastSaleDateStart: {dateValid: true},
			lastSaleDateEnd: {dateValid: true},
			actiCountStart: {numberValid: true},
			actiCountEnd: {numberValid: true},
			pctStart: {floatValid: [6,4]},
			pctEnd: {floatValid: [6,4]}
		};
	if ($('#clubJoinTimeStart').length > 0) {
		$('#clubJoinTimeStart').cherryDate({
			beforeShow: function(input){
				var value = $('#clubJoinTimeEnd').val();
				return [value,'maxDate'];
			}
		});
		$('#clubJoinTimeEnd').cherryDate({
			beforeShow: function(input){
				var value = $('#clubJoinTimeStart').val();
				return [value,'minDate'];
			}
		});
		Object.copyVal(rules, 
			{
			clubJoinTimeStart: {dateValid: true},
			clubJoinTimeEnd: {dateValid: true}
			});
	}
	// 表单验证初期化
	cherryValidate({
		formId: 'memberCherryForm',
		rules: rules
	});
	binolmbmbm09.moveMemCounterDiv = $("#moveMemCounterDiv").html();
	$("#moveMemCounterDiv").remove();
});
