var POPMEMSEARCH = function() {};

POPMEMSEARCH.prototype = {
		"openProDialog" :function(object,index, flag) {
			var proTable = "proTable"+index;
	    	var proShowDiv = "proShowDiv"+index;
	    	var proTypeTable = "proTypeTable"+index;
	    	var proTypeShowDiv = "proTypeShowDiv"+index;
	    	var prtVendorId = "buyPrtVendorId";
	    	if(flag == '1') {
	    		proTable = "notProTable"+index;
	        	proShowDiv = "notProShowDiv"+index;
	        	proTypeTable = "notProTypeTable"+index;
	        	proTypeShowDiv = "notProTypeShowDiv"+index;
	        	prtVendorId = "notPrtVendorId";
	    	}
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
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
	     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="popmemsearch.deleteHtml(this);return false;">'+$div.find("#deleteButton").text()+'</a></td>';
	     				html += '</tr>';
	     				return html;
				   	},
				   	click : function(){
				   		$div.find("#"+proTypeTable).hide();
				   		$div.find("#"+proTypeShowDiv).empty();
				   		if($div.find("#"+proShowDiv).find("tr").length > 0) {
				   			$div.find("#"+proTable).show();
				   		} else {
				   			$div.find("#"+proTable).hide();
				   		}
				   	}
     	    };
     		popAjaxPrtDialog(option);
		},
		"openProTypeDialog" :function(object,index, flag) {
			var proTable = "proTable"+index;
	    	var proShowDiv = "proShowDiv"+index;
	    	var proTypeTable = "proTypeTable"+index;
	    	var proTypeShowDiv = "proTypeShowDiv"+index;
	    	var cateValId = "buyCateValId";
	    	if(flag == '1') {
	    		proTable = "notProTable"+index;
	        	proShowDiv = "notProShowDiv"+index;
	        	proTypeTable = "notProTypeTable"+index;
	        	proTypeShowDiv = "notProTypeShowDiv"+index;
	        	cateValId = "notCateValId";
	    	}
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
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
	     				html += '<td class="center" style="width:15%;"><a href="javascript:void(0);" onClick="popmemsearch.deleteHtml(this);return false;">'+$div.find("#deleteButton").text()+'</a></td>';
	     				html += '</tr>';
	     				return html;
				   	},
				   	click : function(){
				   		$div.find("#"+proTable).hide();
				   		$div.find("#"+proShowDiv).empty();
				   		if($div.find("#"+proTypeShowDiv).find("tr").length > 0) {
				   			$div.find("#"+proTypeTable).show();
				   		} else {
				   			$div.find("#"+proTypeTable).hide();
				   		}
				   	}
     	    };
			popAjaxPrtCateDialog(option);
		},
		"deleteHtml": function(_this) {
			var $tbody = $(_this).parent().parent().parent();
			$(_this).parent().parent().remove();
			if($tbody.find('tr').length == 0) {
				$tbody.parent().hide();
			}
		},
		// 添加搜索条件初始画面
		"addSearchRequestInit": function(addInitUrl, object) {
			popmemsearch.setJsonParam(object);
			var reqContent = $(object).parents("div[id^=memSearchRequestDiv]").find(":input").serialize();
			if(!reqContent) {
				return;
			}
			if($("#addSearchRequestDialog").length == 0) {
				$("body").append('<div style="display:none" id="addSearchRequestDialog"></div>');
			} else {
				$("#addSearchRequestDialog").empty();
			}
			var callback = function(msg) {
				$("#addSearchRequestDialog").html(msg);
				var dialogSetting = {
					dialogInit: "#addSearchRequestDialog",
					text: msg,
					width: 	500,
					height: 300,
					title: 	$("#addSearchRequestDialog").find("#saveCondition").text(),
					confirm: $("#addSearchRequestDialog").find("#dialogConfirm").text(),
					cancel: $("#addSearchRequestDialog").find("#dialogCancel").text(),
					confirmEvent: function(){popmemsearch.addSearchRequest(reqContent);},
					cancelEvent: function(){removeDialog("#addSearchRequestDialog");}
				};
				openDialog(dialogSetting);
			};
			cherryAjaxRequest({
				url: addInitUrl,
				param: null,
				callback: callback
			});
		},
		// 添加搜索条件出来
		"addSearchRequest": function(reqContent) {
			if(!$('#searchRequestForm').valid()) {
				return;
			}
			var param = $("#searchRequestForm").serialize();
			param += "&" + reqContent;
			var callback = function(msg) {
				removeDialog("#addSearchRequestDialog");
			};
			cherryAjaxRequest({
				url: $("#addSearchRequestUrl").attr("href"),
				param: param,
				callback: callback
			});
		},
		// 查询搜索条件一览
		"searchInit": function(searchInitUrl, object) {
			if($("#searchRequestDialog").length == 0) {
				$("body").append('<div style="display:none" id="searchRequestDialog"></div>');
			} else {
				$("#searchRequestDialog").empty();
			}
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			var callback = function(msg) {
				$("#searchRequestDialog").html(msg);
				var dialogSetting = {
					dialogInit: "#searchRequestDialog",
					text: msg,
					width: 	600,
					height: 350,
					title: 	$("#searchRequestDialog").find("#selHisCondition").text(),
					confirm: $("#searchRequestDialog").find("#dialogConfirm").text(),
					cancel: $("#searchRequestDialog").find("#dialogCancel").text(),
					confirmEvent: function(){popmemsearch.searchSearchRequest($div);},
					cancelEvent: function(){removeDialog("#searchRequestDialog");}
				};
				openDialog(dialogSetting);
				
				if(oTableArr[100]) {
					oTableArr[100] = null;
				}
				var searchUrl = $("#searchUrl").attr("href")+"?"+getSerializeToken();
				var tableSetting = {
						 // 表格ID
						 tableId : '#memSearchDataTable',
						 // 一页显示页数
						 iDisplayLength:5,
						 // 数据URL
						 url : searchUrl,
						 // 表格默认排序
						 aaSorting : [[ 0, "desc" ]],
						 // 表格列属性设置
						 aoColumns : [  { "sName": "searchRequestId", "sWidth": "5%", "bSortable": false}, 	
										{ "sName": "requestName", "sWidth": "25%"},                      
										{ "sName": "description", "sWidth": "50%"},
										{ "sName": "operate", "sWidth": "15%", "bSortable": false}],               
						index:100,
						fnDrawCallback:function() {
							$('#memSearchDataTable').find("a.description").cluetip({
								splitTitle: '|',
							    width: 150,
							    height: 'auto',
							    cluetipClass: 'default', 
							    cursor: 'pointer',
							    showTitle: false
							});
						}
				 };
				// 调用获取表格函数
				getTable(tableSetting);
			};
			cherryAjaxRequest({
				url: searchInitUrl,
				param: null,
				callback: callback
			});
		},
		// 选择某个搜索条件
		"searchSearchRequest": function($div) {
			if($("#memSearchDataTable").find(":input[checked]").length > 0) {
				var param = $("#memSearchDataTable").find(":input[checked]").serialize();
//				param += "&disableCondition=" + $("#memSearchDataTable").find(":input[checked]").val();
				var callback = function(msg) {
					removeDialog("#searchRequestDialog");
					$div.html($("#"+$div.attr("id"),msg).html());

					$div.find("#joinDateRangeDiv").find('li').each(function(){
						var that = this;
						$(that).find(':input[name=joinDateStart]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=joinDateEnd]').val();
								return [value,'maxDate'];
							}
						});
						$(that).find(':input[name=joinDateEnd]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=joinDateStart]').val();
								return [value,'minDate'];
							}
						});
					});
					$div.find("#lastSaleTimeRangeDiv").find('li').each(function(){
						var that = this;
						$(that).find(':input[name=lastSaleDateStart]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=lastSaleDateEnd]').val();
								return [value,'maxDate'];
							}
						});
						$(that).find(':input[name=lastSaleDateEnd]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=lastSaleDateStart]').val();
								return [value,'minDate'];
							}
						});
					});
					$div.find("#firstSaleTimeRangeDiv").find('li').each(function(){
						var that = this;
						$(that).find(':input[name=firstStartDay]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=firstEndDay]').val();
								return [value,'maxDate'];
							}
						});
						$(that).find(':input[name=firstEndDay]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=firstStartDay]').val();
								return [value,'minDate'];
							}
						});
					});
					$div.find(':input[name=saleTimeStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=saleTimeEnd]').val();
							return [value,'maxDate'];
						}
					});
					$div.find(':input[name=saleTimeEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=saleTimeStart]').val();
							return [value,'minDate'];
						}
					});
					$div.find(':input[name=notSaleTimeStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=notSaleTimeEnd]').val();
							return [value,'maxDate'];
						}
					});
					$div.find(':input[name=notSaleTimeEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=notSaleTimeStart]').val();
							return [value,'minDate'];
						}
					});
					$div.find(':input[name=participateTimeStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=participateTimeEnd]').val();
							return [value,'maxDate'];
						}
					});
					$div.find(':input[name=participateTimeEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=participateTimeStart]').val();
							return [value,'minDate'];
						}
					});
					$div.find(':input[name=levelAdjustDayStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=levelAdjustDayEnd]').val();
							return [value,'maxDate'];
						}
					});
					$div.find(':input[name=levelAdjustDayEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=levelAdjustDayStart]').val();
							return [value,'minDate'];
						}
					});
					if ($div.find('#clubJoinTimeStart').length > 0) {
						$div.find('#clubJoinTimeStart').cherryDate({
							beforeShow: function(input){
								var value = $div.find('#clubJoinTimeEnd').val();
								return [value,'maxDate'];
							}
						});
						$div.find('#clubJoinTimeEnd').cherryDate({
							beforeShow: function(input){
								var value = $div.find('#clubJoinTimeStart').val();
								return [value,'minDate'];
							}
						});
					}
//					var calback1 = function(msg) {
//						$("#conditionDisplayDiv").html(msg);
//					};
//					cherryAjaxRequest({
//						url: $div.find("#conditionDisplayUrl").attr("href"),
//						param: param,
//						callback: calback1
//					});
				};
				cherryAjaxRequest({
					url: $div.find("#conditionInitUrl").attr("href"),
					param: param,
					callback: callback
				});
			}
		},
		// 删除某个搜索条件
		"delSearchRequest": function(object) {
			var callback = function(msg) {
				if(oTableArr[100]) {
					oTableArr[100].fnDraw();
				}
			};
			cherryAjaxRequest({
				url: $(object).attr("href"),
				param: null,
				callback: callback
			});
		},
		// 查询会员一览
		"searchMemInit": function(searchMemInitUrl, object) {
			popmemsearch.setJsonParam(object);
			var reqContent = $(object).parents("div[id^=memSearchRequestDiv]").find(":input").serialize();
			if($("#searchMemDialog").length == 0) {
				$("body").append('<div style="display:none" id="searchMemDialog"></div>');
			} else {
				$("#searchMemDialog").empty();
			}
			var userId = $("#userId").val();
			var privilegeFlag = $("#privilegeFlag").val();
			if(privilegeFlag == null || privilegeFlag == ""){
				privilegeFlag = "0";
			}
			var callback = function(msg) {
				$("#searchMemDialog").html(msg);
				var dialogSetting = {
					dialogInit: "#searchMemDialog",
					text: msg,
					width: 	800,
					height: 430,
					title: 	$("#searchMemDialog").find("#searchActObject").text(),
					confirm: $("#searchMemDialog").find("#dialogClose").text(),
					confirmEvent: function(){removeDialog("#searchMemDialog");}
				};
				openDialog(dialogSetting);
				
				
				if(oTableArr[101]) {
					oTableArr[101] = null;
				}
				var searchMemUrl = $("#searchMemUrl").attr("href")+"?"+getSerializeToken() + "&userId=" + userId + "&privilegeFlg=" + privilegeFlag;
				searchMemUrl += "&" + reqContent;
				var tableSetting = {
						 // 表格ID
						 tableId : '#memSearchDataTable',
						 // 一页显示页数
						 iDisplayLength:10,
						 // 数据URL
						 url : searchMemUrl,
						 // 表格默认排序
						 aaSorting : [[ 7, "desc" ]],
						 // 表格列属性设置
						 aoColumns : [  { "sName": "number", "sWidth": "5%", "bSortable": false}, 	
										{ "sName": "memName", "sWidth": "15%", "bSortable": false},                      
										{ "sName": "memCode", "sWidth": "15%", "bSortable": false},
										{ "sName": "mobilePhone", "sWidth": "15%", "bSortable": false},
										{ "sName": "birthDay", "sWidth": "15%", "bSortable": false},
										{ "sName": "levelName", "sWidth": "10%"},
										{ "sName": "changablePoint", "sWidth": "10%"},
										{ "sName": "joinDate", "sWidth": "15%"}],               
						index:101
				 };
				// 调用获取表格函数
				getTable(tableSetting);
			};
			var param = "userId=" + userId + "&privilegeFlg=" + privilegeFlag;
			cherryAjaxRequest({
				url: searchMemInitUrl,
				param: param,
				callback: callback
			});
		},
		"moreCondition": function(_this) {
			var $this = $(_this);
			var $next = $this.next();
			var $div = $this.parents("div[id^=memSearchRequestDiv]");
			var $obj = $div.find("div.moreCondition");
			if($obj.is(":hidden")) {
				$obj.slideDown("slow");
				$this.text($div.find("#baseCondition").text());
				$next.prop("class",'arrow_up');
			} else {
				$obj.slideUp("slow");
				$this.text($div.find("#moreCondition").text());
				$next.prop("class",'arrow_down');
			}
		},
		"popRegionDialog": function(url, object, isClub) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			if ('1' == isClub && $div.find("#memberClubId").val() == "") {
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
			var $regionName;
			var $regionNameDiv;
			var $belongId;
			if ('1' == isClub) {
				$exclusiveFlag = "#clubExclusiveFlag";
				$modeFlag = "#clubModeFlag";
				$couValidFlag = "#clubCouValidFlag";
				$regionId = "#clubRegionId";
				$provinceId = "#clubProvinceId";
				$cityId = "#clubCityId";
				$memCounterId = "#clubMemCounterId";
				$channelRegionId = "#clubChannelRegionId";
				$channelId = "#clubChannelId";
				$regionName = "#clubRegionName";
				$regionNameDiv = "#clubRegionNameDiv";
				$belongId = "#clubBelongId";
			} else {
				$exclusiveFlag = "#exclusiveFlag";
				$modeFlag = "#modeFlag";
				$couValidFlag = "#couValidFlag";
				$regionId = "#regionId";
				$provinceId = "#provinceId";
				$cityId = "#cityId";
				$memCounterId = "#memCounterId";
				$channelRegionId = "#channelRegionId";
				$channelId = "#channelId";
				$regionName = "#regionName";
				$regionNameDiv = "#regionNameDiv";
				$belongId = "#belongId";
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
					var channelId = "";
					var memCounterId = "";
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
					$div.find($exclusiveFlag).val(selExclusiveFlag);
					$div.find($modeFlag).val(selMode);
					$div.find($couValidFlag).val(couValidFlag);
					$div.find($regionId).val(regionId);
					$div.find($belongId).val(belongId);
					$div.find($provinceId).val(provinceId);
					$div.find($cityId).val(cityId);
					$div.find($memCounterId).val(memCounterId);
					$div.find($channelRegionId).val(channelRegionId);
					$div.find($channelId).val(channelId);
					$div.find($regionName).val(regionNameDiv);
					regionNameDiv += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delProvinceHtml(this,' + isClub + ');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$div.find($regionNameDiv).html(regionNameDiv);
				} else {
					popmemsearch.delProvinceHtml(object, isClub);
				}
			}
			
			// 区域树初始默认选中值
			var value = {};
			var regionInfo = [];
			var regionId = $div.find($regionId).val();
			var provinceId = $div.find($provinceId).val();
			var cityId = $div.find($cityId).val();
			var memCounterId = $div.find($memCounterId).val();
			var channelRegionId = $div.find($channelRegionId).val();
			var channelId = $div.find($channelId).val();
			var exclusiveFlag = $div.find($exclusiveFlag).val();
			var modeFlag = $div.find($modeFlag).val();
			var couValidFlag = $div.find($couValidFlag).val();
			var belongId = $div.find($belongId).val();
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
			}  else if (modeFlag == "4") {
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
			
			var userId = $("#userId").val();
			var privilegeFlag = $("#privilegeFlag").val();
			if(privilegeFlag == null || privilegeFlag == ""){
				privilegeFlag = "0";
			}
			var param = "userId=" + userId + "&privilegeFlg=" + privilegeFlag;
			if ($div.find("#memberClubId").length > 0) {
				param += "&" + $div.find("#memberClubId").serialize();
			}
			popRegionDialog(url, param, value, callback);
		},
		"delProvinceHtml": function(object, isClub) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			var $exclusiveFlag;
			var $modeFlag;
			var $couValidFlag;
			var $regionId;
			var $provinceId;
			var $cityId;
			var $memCounterId;
			var $channelRegionId;
			var $channelId;
			var $regionName;
			var $regionNameDiv;
			var $belongId;
			if ('1' == isClub) {
				$exclusiveFlag = "#clubExclusiveFlag";
				$modeFlag = "#clubModeFlag";
				$couValidFlag = "#clubCouValidFlag";
				$regionId = "#clubRegionId";
				$provinceId = "#clubProvinceId";
				$cityId = "#clubCityId";
				$memCounterId = "#clubMemCounterId";
				$channelRegionId = "#clubChannelRegionId";
				$channelId = "#clubChannelId";
				$regionName = "#clubRegionName";
				$regionNameDiv = "#clubRegionNameDiv";
				$belongId = "#clubBelongId";
			} else {
				$exclusiveFlag = "#exclusiveFlag";
				$modeFlag = "#modeFlag";
				$couValidFlag = "#couValidFlag";
				$regionId = "#regionId";
				$provinceId = "#provinceId";
				$cityId = "#cityId";
				$memCounterId = "#memCounterId";
				$channelRegionId = "#channelRegionId";
				$channelId = "#channelId";
				$regionName = "#regionName";
				$regionNameDiv = "#regionNameDiv";
				$belongId = "#belongId";
			}
			$div.find($modeFlag).val("");
			$div.find($exclusiveFlag).val("");
			$div.find($couValidFlag).val("");
			$div.find($regionId).val("");
			$div.find($provinceId).val("");
			$div.find($cityId).val("");
			$div.find($memCounterId).val("");
			$div.find($channelRegionId).val("");
			$div.find($channelId).val("");
			$div.find($regionName).val("");
			$div.find($belongId).val("");
			$div.find($regionNameDiv).html("");
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
					$(object).parent().find('span').remove();
					$(object).parent().attr("class","");
					$(object).parent().next().show();
					$(object).parent().next().next().hide();
					$(object).parent().next().next().next().hide();
					$(object).parent().next().next().next().next().hide();
					$(object).parent().next().next().next().next().next().hide();
				} else if($(object).val() == "1") {
					$(object).parent().find('span').remove();
					$(object).parent().attr("class","");
					$(object).parent().next().hide();
					$(object).parent().next().next().show();
					$(object).parent().next().next().next().hide();
					$(object).parent().next().next().next().next().hide();
					$(object).parent().next().next().next().next().next().hide();
				} else if($(object).val() == "2") {
					$(object).parent().find('span').remove();
					$(object).parent().attr("class","");
					$(object).parent().next().hide();
					$(object).parent().next().next().hide();
					$(object).parent().next().next().next().show();
					$(object).parent().next().next().next().next().hide();
					$(object).parent().next().next().next().next().next().hide();
				} else if($(object).val() == "9") {
					$(object).parent().find('span').remove();
					$(object).parent().attr("class","");
					$(object).parent().next().hide();
					$(object).parent().next().next().hide();
					$(object).parent().next().next().next().hide();
					$(object).parent().next().next().next().next().show();
					$(object).parent().next().next().next().next().next().hide();
				} else if($(object).val() == "3") {
					$(object).parent().find('span').remove();
					$(object).parent().attr("class","");
					$(object).parent().next().hide();
					$(object).parent().next().next().hide();
					$(object).parent().next().next().next().hide();
					$(object).parent().next().next().next().next().hide();
					$(object).parent().next().next().next().next().next().show();
				}
			}
		},
		"selectBirthDayDateMode": function(object) {
			if($(object).val() == "0") {
				$(object).parent().next().show();
			} else {
				$(object).parent().next().hide();
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
		"popCounterList": function(url, object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			var callback = function(tableId) {
				var $checkedRadio = $("#"+tableId).find(":input[checked]");
				if($checkedRadio.length > 0) {
					$div.find("#saleCounterId").val($checkedRadio.val());
					$div.find("#saleCounterCode").val($checkedRadio.next().val());
					
					var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
					$div.find("#saleCounterName").val(html);
					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delCounterHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$div.find("#saleCounterDiv").html(html);
				}
			}
			var value = $div.find("#saleCounterId").val();
			popDataTableOfCounterList(url, null, value, callback);
		},
		"delCounterHtml": function(object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			$div.find("#saleCounterId").val("");
			$div.find("#saleCounterCode").val("");
			$div.find("#saleCounterName").val("");
			$div.find("#saleCounterDiv").empty();
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
		// 选择活动柜台弹出框
		"popCampaignCounterList": function(url, object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			var callback = function(tableId) {
				var $checkedRadio = $("#"+tableId).find(":input[checked]");
				if($checkedRadio.length > 0) {
					$div.find("#campaignCounterId").val($checkedRadio.val());
					var html = '(' + $checkedRadio.parent().next().text() + ')' + $checkedRadio.parent().next().next().text();
					$div.find("#campaignCounterName").val(html);
					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delCampaignCounterHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$div.find("#campaignCounterDiv").html(html);
				}
			}
			var value = $div.find("#campaignCounterId").val();
			
			var userId = $("#userId").val();
			var privilegeFlag = $("#privilegeFlag").val();
			if(privilegeFlag == null || privilegeFlag == ""){
				privilegeFlag = "0";
			}
			var param = "userId=" + userId + "&privilegeFlg="+privilegeFlag;
			popDataTableOfCounterList(url, param, value, callback);
		},
		// 删除选择的活动柜台
		"delCampaignCounterHtml": function(object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			$div.find("#campaignCounterId").val("");
			$div.find("#campaignCounterName").val("");
			$div.find("#campaignCounterDiv").empty();
		},
		// 选择活动弹出框
		"popCampaignList": function(url, object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			var callback = function(tableId) {
				var $checkedRadio = $("#"+tableId).find(":input[checked]");
				if($checkedRadio.length > 0) {
					$div.find("#campaignMode").val($checkedRadio.next().val());
					$div.find("#campaignCode").val($checkedRadio.val());
					var html = $checkedRadio.parent().next().text();
					$div.find("#campaignName").val(html);
					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delCampaignHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$div.find("#campaignDiv").html(html);
				}
			}
			var value = $div.find("#campaignCode").val();
			popDataTableOfCampaignList(url, null, value, callback);
		},
		// 删除选择的活动
		"delCampaignHtml": function(object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			$div.find("#campaignMode").val("");
			$div.find("#campaignCode").val("");
			$div.find("#campaignName").val("");
			$div.find("#campaignDiv").empty();
		},
		// 重置查询条件
		"resetCondition": function(object, index) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
//			$div.find(':text:enabled').val('');
//			$div.find(':checkbox:enabled, :radio:enabled').removeAttr('checked');
//			$div.find('select:enabled').val('');
//			if($div.find(':input[name="joinDateMode"]').is(':enabled')) {
//				popmemsearch.selectDateMode($div.find(':input[name="joinDateMode"]')[0]);
//			}
//			if($div.find(':input[name="birthDayMode"]').is(':enabled')) {
//				popmemsearch.selectBirthDayMode($div.find(':input[name="birthDayMode"]')[0]);
//				popmemsearch.selectMonth($div.find(':input[name="birthDayMonth"]')[0],'0');
//				popmemsearch.selectMonth($div.find(':input[name="birthDayMonthRangeStart"]')[0],'1');
//				popmemsearch.selectMonth($div.find(':input[name="birthDayMonthRangeEnd"]')[0],'1');
//			}
//			if($div.find(':input[name="birthDayDateMode"]').is(':enabled')) {
//				popmemsearch.selectBirthDayDateMode($div.find(':input[name="birthDayDateMode"]')[0]);
//			}
//			if($div.find(':input[name="referFlag"]').is(':enabled')) {
//				popmemsearch.selectReferFlag($div.find(':input[name="referFlag"]')[0]);
//			}
//			if($div.find('#regionNameDiv').parent().find('a').length > 0) {
//				popmemsearch.delProvinceHtml(object);
//			}
//			if($div.find(':input[name="saleTimeMode"]').is(':enabled')) {
//				popmemsearch.selectDateMode($div.find(':input[name="saleTimeMode"]')[0]);
//			}
//			if($div.find('#saleCounterDiv').parent().find('a').length > 0) {
//				popmemsearch.delCounterHtml(object);
//			}
//			if($("#proTable"+index).parent().find('a').length > 0) {
//				$("#proShowDiv"+index).empty();
//				$("#proTable"+index).hide();
//				$("#proTypeShowDiv"+index).empty();
//				$("#proTypeTable"+index).hide();
//			}
//			if($("#notProTable"+index).parent().find('a').length > 0) {
//				$("#notProShowDiv"+index).empty();
//				$("#notProTable"+index).hide();
//				$("#notProTypeShowDiv"+index).empty();
//				$("#notProTypeTable"+index).hide();
//			}
//			if($div.find('#campaignDiv').parent().find('a').length > 0) {
//				popmemsearch.delCampaignHtml(object);
//			}
//			if($div.find('#campaignCounterDiv').parent().find('a').length > 0) {
//				popmemsearch.delCampaignCounterHtml(object);
//			}
			
			var param = $div.find("#disableCondition").serialize();
			if(param) {
				param += "&reqContent=" + $div.find("#disableCondition").val();
			}
			var callback = function(msg) {
				removeDialog("#searchRequestDialog");
				$div.html($("#"+$div.attr("id"),msg).html());

				$div.find("#joinDateRangeDiv").find('li').each(function(){
					var that = this;
					if($(that).find(':input[name=joinDateStart]').is(':enabled')) {
						$(that).find(':input[name=joinDateStart]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=joinDateEnd]').val();
								return [value,'maxDate'];
							}
						});
					}
					if($(that).find(':input[name=joinDateEnd]').is(':enabled')) {
						$(that).find(':input[name=joinDateEnd]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=joinDateStart]').val();
								return [value,'minDate'];
							}
						});
					}
				});

				$div.find("#lastSaleTimeRangeDiv").find('li').each(function(){
					var that = this;
					if($(that).find(':input[name=lastSaleDateStart]').is(':enabled')) {
						$(that).find(':input[name=lastSaleDateStart]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=lastSaleDateEnd]').val();
								return [value,'maxDate'];
							}
						});
					}
					if($(that).find(':input[name=lastSaleDateEnd]').is(':enabled')) {
						$(that).find(':input[name=lastSaleDateEnd]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=lastSaleDateStart]').val();
								return [value,'minDate'];
							}
						});
					}
				});

				$div.find("#firstSaleTimeRangeDiv").find('li').each(function(){
					var that = this;
					if($(that).find(':input[name=firstStartDay]').is(':enabled')) {
						$(that).find(':input[name=firstStartDay]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=firstEndDay]').val();
								return [value,'maxDate'];
							}
						});
					}
					if($(that).find(':input[name=firstEndDay]').is(':enabled')) {
						$(that).find(':input[name=firstEndDay]').cherryDate({
							beforeShow: function(input){
								var value = $(that).find(':input[name=firstStartDay]').val();
								return [value,'minDate'];
							}
						});
					}
				});
				
				if($div.find(':input[name=saleTimeStart]').is(':enabled')) {
					$div.find(':input[name=saleTimeStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=saleTimeEnd]').val();
							return [value,'maxDate'];
						}
					});
				}
				if($div.find(':input[name=saleTimeEnd]').is(':enabled')) {
					$div.find(':input[name=saleTimeEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=saleTimeStart]').val();
							return [value,'minDate'];
						}
					});
				}
				if($div.find(':input[name=notSaleTimeStart]').is(':enabled')) {
					$div.find(':input[name=notSaleTimeStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=notSaleTimeEnd]').val();
							return [value,'maxDate'];
						}
					});
				}
				if($div.find(':input[name=notSaleTimeEnd]').is(':enabled')) {
					$div.find(':input[name=notSaleTimeEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=notSaleTimeStart]').val();
							return [value,'minDate'];
						}
					});
				}
				if($div.find(':input[name=participateTimeStart]').is(':enabled')) {
					$div.find(':input[name=participateTimeStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=participateTimeEnd]').val();
							return [value,'maxDate'];
						}
					});
				}
				if($div.find(':input[name=participateTimeEnd]').is(':enabled')) {
					$div.find(':input[name=participateTimeEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=participateTimeStart]').val();
							return [value,'minDate'];
						}
					});
				}
				if($div.find(':input[name=levelAdjustDayStart]').is(':enabled')) {
					$div.find(':input[name=levelAdjustDayStart]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=levelAdjustDayEnd]').val();
							return [value,'maxDate'];
						}
					});
				}
				if($div.find(':input[name=levelAdjustDayEnd]').is(':enabled')) {
					$div.find(':input[name=levelAdjustDayEnd]').cherryDate({
						beforeShow: function(input){
							var value = $div.find(':input[name=levelAdjustDayStart]').val();
							return [value,'minDate'];
						}
					});
				}
			};
			cherryAjaxRequest({
				url: $div.find("#conditionInitUrl").attr("href"),
				param: param,
				callback: callback
			});
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
		"popSaleRegionDialog": function(url, object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			var callback = function(treeObj, selMode, selExclusiveFlagObj, popCouValidFlagObj, channelRegionObj) {
				if(treeObj == null) {
					return;
				}
				var nodes = treeObj.getCheckedNodes(true);
				if(nodes.length > 0) {
					var regionId = "";
					var provinceId = "";
					var cityId = "";
					var channelId = "";
					var memCounterId = "";
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
					$div.find("#saleExclusiveFlag").val(selExclusiveFlag);
					$div.find("#saleModeFlag").val(selMode);
					$div.find("#saleCouValidFlag").val(couValidFlag);
					$div.find("#saleRegionId").val(regionId);
					$div.find("#saleProvinceId").val(provinceId);
					$div.find("#saleCityId").val(cityId);
					$div.find("#saleMemCounterId").val(memCounterId);
					$div.find("#saleChannelRegionId").val(channelRegionId);
					$div.find("#saleChannelId").val(channelId);
					$div.find("#saleRegionName").val(regionNameDiv);
					$div.find("#saleBelongId").val(belongId);
					regionNameDiv += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="popmemsearch.delSaleProvinceHtml(this);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
					$div.find("#saleRegionNameDiv").html(regionNameDiv);
				} else {
					popmemsearch.delProvinceHtml(object);
				}
			}
			
			// 区域树初始默认选中值
			var value = {};
			var regionInfo = [];
			var regionId = $div.find("#saleRegionId").val();
			var provinceId = $div.find("#saleProvinceId").val();
			var cityId = $div.find("#saleCityId").val();
			var memCounterId = $div.find("#saleMemCounterId").val();
			var channelRegionId = $div.find("#saleChannelRegionId").val();
			var channelId = $div.find("#saleChannelId").val();
			var exclusiveFlag = $div.find("#saleExclusiveFlag").val();
			var modeFlag = $div.find("#saleModeFlag").val();
			var couValidFlag = $div.find("#saleCouValidFlag").val();
			var belongId = $div.find("#saleBelongId").val();
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
			}  else if (modeFlag == "4") {
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
			
			var userId = $("#userId").val();
			var privilegeFlag = $("#privilegeFlag").val();
			if(privilegeFlag == null || privilegeFlag == ""){
				privilegeFlag = "0";
			}
			var param = "userId=" + userId + "&hasSelExclusiveFlag=0&privilegeFlg="+privilegeFlag;
			popRegionDialog(url, param, value, callback);
		},
		"delSaleProvinceHtml": function(object) {
			var $div = $(object).parents("div[id^=memSearchRequestDiv]");
			$div.find("#saleModeFlag").val("");
			$div.find("#saleExclusiveFlag").val("");
			$div.find("#saleCouValidFlag").val("");
			$div.find("#saleRegionId").val("");
			$div.find("#saleProvinceId").val("");
			$div.find("#saleCityId").val("");
			$div.find("#saleMemCounterId").val("");
			$div.find("#saleChannelRegionId").val("");
			$div.find("#saleChannelId").val("");
			$div.find("#saleRegionName").val("");
			$div.find("#saleBelongId").val("");
			$div.find("#saleRegionNameDiv").html("");
		},
		"changeLevel" : function (obj) {
	    	var clubId = $(obj).val();
	    	var $div = $(obj).parents("div[id^=memSearchRequestDiv]");
	    	popmemsearch.cleanClubAttrs(obj, clubId);
	    	var $levelSpan = $div.find("#levelSpan");
	    	if ("" != clubId) {
	    		var url = $("#searchLevelUrl").attr("href");
	    		var param = $div.find("#memberClubId").serialize() + "&" + "csrftoken=" + getTokenVal();
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
		    					    	$levelSpan.html(htm);
	    							}
	    						}
	    					}
	    	    });
	    	} else {
	    		$levelSpan.empty();
	    	}
	    },
	    "cleanClubAttrs" : function(obj, kbn) {
	    	var $div = $(obj).parents("div[id^=memSearchRequestDiv]");
	    	var $clubAttrs = $div.find('#clubAttrs');
	    	$clubAttrs.find(':text').val('');
	    	$clubAttrs.find('select').not('#memberClubId').val('');
	    	if ("" != kbn) {
	    		if ($clubAttrs.find("#memberPointStart").is(':disabled')) {
	    			$clubAttrs.find('.club-attr').prop("disabled", false);
	    			$clubAttrs.find('.no-club-date').hide();
	    			$clubAttrs.find('.club-date').show();
	    		}
	    	} else {
	    		if (!$clubAttrs.find("#memberPointStart").is(':disabled')) {
	    			$clubAttrs.find('.club-attr').prop("disabled", true);
	    			$clubAttrs.find('.club-date').hide();
	    			$clubAttrs.find('.no-club-date').show();
	    		}
	    	}
	    	popmemsearch.delProvinceHtml(obj, '1');
	    	popmemsearch.selectReferFlag($clubAttrs.find(':input[name="referFlag"]')[0]);
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
			       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="popmemsearch.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
		       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="popmemsearch.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
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
		"selectFirstDayMode":function(obj) {
			var mode = $(obj).val();
			$("#spanNoSaleDaysMode1,#spanNoSaleDaysMode2").hide();
			$("#spanNoSaleDaysMode1,#spanNoSaleDaysMode2").find(":input").val("");
			$("#spanNoSaleDaysMode" + mode).show();
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
			var $obj = $(obj).parents("div[id^=memSearchRequestDiv]");
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
		}
}
var popmemsearch = new POPMEMSEARCH();
