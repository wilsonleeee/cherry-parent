function BINOLWPMBM01(){};

BINOLWPMBM01.prototype = {
	// 会员首页初始化
	"memInit": function(){
		
		var binolwpmbm01Message = {
				validateError1: "请先输入条件",
				validateError2: "请输入生日",
				validateError3: "请输入姓名",	
	    		loading: "加载中...",
	    		nosale: "无销售",
	    		amount: "金额",
	    		point: "积分",
	    		other: "其他",
	    		pieTitle1: "大类占比(金额)",
	    		pieTitle2: "大类占比(数量)"
	    };
	    var language = $('#CHERRY_LANGUAGE').text();
	    if(language == "zh_TW") {
	    	binolwpmbm01Message = {
	    			validateError1: "請先輸入條件",
	    			validateError2: "請輸入生日",
					validateError3: "請輸入姓名",
		    		loading: "加載中...",
		    		nosale: "無銷售",
		    		amount: "金額",
		    		point: "積分",
		    		other: "其他",
		    		pieTitle1: "大類佔比(金額)",
		    		pieTitle2: "大類佔比(數量)"
	        };
	    }
		
		$("body").css("overflow-y","scroll");
		oTableArr[100] = null;
		fixedColArr[100] = null;
		$('#search').click(function(){
			var result = validateParam();
			if(result) {
				$("#validateError").show();
				$("#validateError").html(result);
				return false;
			}
			$("#validateError").hide();
			var url = $("#searchMemUrl").attr("href") + "?" + getSerializeToken();
			var params = $("#queryParam").serialize();
			if(params != null && params != "") {
				url = url + "&" + params;
			}
			// 显示结果一览
			$("#result").show();	
			// 表格设置
			var tableSetting = {
					 // 表格ID
					 tableId: '#result',
					 // 数据URL
					 url: url,
					 // 表格默认排序
					 aaSorting: [[ 6, "desc" ]],
					 // 表格列属性设置
					 aoColumns: [{ "sName": "radio", "sWidth": "4%", "bSortable": false },
					             { "sName": "mobilePhone", "sWidth": "10%", "bSortable": false },
					             { "sName": "memCode", "sWidth": "8%", "bSortable": false},
					             { "sName": "memName", "sWidth": "8%", "bSortable": false },
					             { "sName": "birthDay", "sWidth": "8%", "bSortable": false },
					             { "sName": "levelName", "sWidth": "8%", "bSortable": false},
					             { "sName": "joinDate", "sWidth": "8%" },
					             { "sName": "changablePoint", "sWidth": "8%", "bSortable": false},
					             { "sName": "changeDate", "sWidth": "10%", "bSortable": false},
					             { "sName": "totalAmount", "sWidth": "10%", "bSortable": false},
					             { "sName": "amount", "sWidth": "10%", "bSortable": false},
					             { "sName": "couponCount", "sWidth": "8%", "bSortable": false}],
					// 横向滚动条出现的临界宽度
					sScrollX: "100%",
					iDisplayLength: 5,
					index: 100,
					fnDrawCallback: function() {
						buttonDisable();
						$("#result").find('tr').click(function() {
							$(this).find(':input[name=memberInfoId]').attr("checked","checked");
							buttonEnable();
							memReport(this);
						});
						if($("#result").find('tr').find(':input[name=memberInfoId]').length == 1) {
							$("#result").find('tr').find(':input[name=memberInfoId]').parents('tr').click();
						}
					}
			};
			// 调用获取表格函数
			getTable(tableSetting);
			return false;
		});
		
		// 验证是否存在查询条件
		function validateParam() {
			var result = "";
			var mobilePhoneQ = $("#queryParam").find(":input[name=mobilePhoneQ]").val();
			var memCodeQ = $("#queryParam").find(":input[name=memCodeQ]").val();
			var memNameQ = $("#queryParam").find(":input[name=memNameQ]").val();
			var birthDayMonthQ = $("#queryParam").find(":input[name=birthDayMonthQ]").val();
			var birthDayDateQ = $("#queryParam").find(":input[name=birthDayDateQ]").val();
			if(mobilePhoneQ || memCodeQ) {
				result = "";
			} else {
				if(memNameQ && birthDayMonthQ && birthDayDateQ) {
					result = "";
				} else {
					if(memNameQ) {
						result = binolwpmbm01Message.validateError2;
					} else if(birthDayMonthQ && birthDayDateQ) {
						result = binolwpmbm01Message.validateError3;
					} else {
						result = binolwpmbm01Message.validateError1;
					}
				}
			}
			return result;
		}
		
		// 生日框初始化处理
		function birthDayInit() {
			for(var i = 1; i <= 12; i++) {
				$("#birthDayMonthQ").append('<option value="'+i+'">'+i+'</option>');
			}
			$("#birthDayMonthQ").change(function(){
				var $date = $("#birthDayDateQ");
				var month = $(this).val();
				var options = '<option value="">'+$date.find('option').first().html()+'</option>';
				if(month == "") {
					$date.html(options);
					return;
				}
				var i = 1;
				var max = 0;
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
			});
		}
		birthDayInit();
		
		$("#doSale").click(function(){
			var wmFlag = $("#wmFlag").val();
			if(wmFlag == '1') {
				var url = $("#counterSaleBtn").attr('href') + '&' + getSerializeToken();
				var param = $("#result").find(':input[name=memberInfoId]').serialize();
				if(param) {
					url = url + "&" + param;	
					url += "&mobilePhoneQ="+$("#mobilePhoneQ").val();
				}
				popup(url, {width:window.screen.width-10,height:window.screen.height-35});
				return false;
			}
			var url = $("#doSaleUrl").attr("href");
			var param = $("#result").find(':input[name=memberInfoId]').serialize();
			if(param) {
				url = url + "?" + param;
			}
			$("#member_main").hide();
			$("#member_main").empty();
			$("body").css("overflow-y","auto");
			$("#webpos_main").show();
			cherryAjaxRequest({
				url: url,
				param: null,
				callback: function(data) {
					// 还原按钮样式
					$("#btnShowMember").attr("class","btn_top");
					
					if(data != undefined && data != null && data != ""){
						if(data != "MULTIPLE"){
							// 查询结果为单条记录的情况下显示记录详细内容
							var memberInfo = eval("("+data+")");
							$('#memberInfoId').val(memberInfo.memberInfoId);
							$('#memberCode').val(memberInfo.memberCode);
							$('#memberName').val(memberInfo.memberName);
							$('#memberLevel').val(memberInfo.memberLevel);
							$('#mobilePhone').val(memberInfo.mobilePhone);
							$('#changablePoint').val(memberInfo.changablePoint);
							$('#txtMemberName').text(memberInfo.memberName);
							$('#spanMemberCode').text(memberInfo.memberCode);
							$('#spanMemberName').text(memberInfo.memberName);
							$('#spanTotalPoint').text(memberInfo.totalPoint);
							$('#spanChangablePoint').text(memberInfo.changablePoint);
							$('#spanJoinDate').text(memberInfo.joinDate);
							$('#spanLastSaleDate').text(memberInfo.lastSaleDate);
							
							// 行数大于10行时查询会员显示在记录行下部，小于10时显示在页面底部
							if($('#databody >tr').length >= 10){
								$("#memberPageDiv").attr("class","wp_bottom2");
								$("#memberContentDiv").removeAttr("class");
								$("#memberBoxDiv").removeAttr("class");
							}else{
								$("#memberPageDiv").attr("class","wp_bottom");
								$("#memberContentDiv").attr("class","wp_content_b");
								$("#memberBoxDiv").attr("class","wp_leftbox");
							}
							// 显示会员信息
							$("#memberPageDiv").show();
							// 查询符合条件的促销活动
							var setParams = {};
							setParams.memberInfoId = memberInfo.memberInfoId;
							setParams.mobilePhone = memberInfo.mobilePhone;
							setParams.changablePoint = memberInfo.changablePoint;
							BINOLWPSAL02.getPromotion(setParams);
						}else{
							// 重新加载促销
							var setParams = {};
							BINOLWPSAL02.getPromotion(setParams);
							// 清空会员信息
							BINOLWPSAL02.emptyMemberInfo();
						}
					}else{
						// 重新加载促销
						var setParams = {};
						BINOLWPSAL02.getPromotion(setParams);
						// 清空会员信息
						BINOLWPSAL02.emptyMemberInfo();
					}
					// 计算会员价格和金额
					BINOLWPSAL02.calcuMemberSaleAmout();
					
					$(document).unbind("keydown");
					// 页面绑定F1到F8快捷键
					$(document).bind("keydown", "f1", function (e) { e.preventDefault();BINOLWPSAL02.collect(); })
						.bind("keydown", "f2", function (e) { e.preventDefault();BINOLWPSAL02.discount(); })
						.bind("keydown", "f3", function (e) { e.preventDefault();BINOLWPSAL02.hangBills(); })
						.bind("keydown", "f4", function (e) { e.preventDefault();BINOLWPSAL02.searchBills(); })
						.bind("keydown", "f5", function (e) { e.preventDefault();BINOLWPSAL02.showMember(); })
						.bind("keydown", "f6", function (e) { e.preventDefault();BINOLWPSAL02.emptyShoppingCart(); })
						.bind("keydown", "f7", function (e) { e.preventDefault();BINOLWPSAL02.returnsGoods(); })
						.bind("keydown", "f8", function (e) { e.preventDefault();BINOLWPSAL02.addHistoryBill(); });
					
					// 获取最后一行
					var $lastTr = $('#databody').find("tr:last");
					if(($lastTr.find("#productVendorIDArr").val() == undefined || $lastTr.find("#productVendorIDArr").val() == "") 
							&& ($lastTr.find("#activityTypeCode").val() != "ZDZK" && $lastTr.find("#activityTypeCode").val() != "ZDQL")){
						// 最后一行第一个可见的文本框获得焦点
						$('#databody').find("tr:last").find("input:text:visible:first").focus();
					}else{
						// 最后一个可见的文本框获得焦点
						$('#databody >tr').find("input:text:visible:last").select();
					}
				}
			});
			return false;
		});
		$("#addMem").click(function(){
			var url = $("#addInitUrl").attr("href");
			$("#memInitDiv").hide();
			$("#memOtherDiv").show();
			$("#memOtherDiv").empty();
			cherryAjaxRequest({
				url: url,
				param: null,
				callback: function(data) {
					$("#memOtherDiv").html(data);
				}
			});
			return false;
		});
		$("#updMem").click(function(){
			var param = $("#result").find(':input[name=memberInfoId]').serialize();
			if(param) {
				var url = $("#updateInitUrl").attr("href");
				url = url + "?" + param;
				$("#memInitDiv").hide();
				$("#memOtherDiv").show();
				$("#memOtherDiv").empty();
				cherryAjaxRequest({
					url: url,
					param: null,
					callback: function(data) {
						$("#memOtherDiv").html(data);
					}
				});
			}
			return false;
		});
		$("#saleList").click(function(){
			var param = $("#result").find(':input[name=memberInfoId]').serialize();
			if(param) {
				var url = $("#saleInitUrl").attr("href");
				url = url + "?" + param;
				$("#memInitDiv").hide();
				$("#memOtherDiv").show();
				$("#memOtherDiv").empty();
				cherryAjaxRequest({
					url: url,
					param: null,
					callback: function(data) {
						$("#memOtherDiv").html(data);
					}
				});
			}
			return false;
		});
		$("#pointList").click(function(){
			var param = $("#result").find(':input[name=memberInfoId]').serialize();
			if(param) {
				var url = $("#pointInitUrl").attr("href");
				url = url + "?" + param;
				$("#memInitDiv").hide();
				$("#memOtherDiv").show();
				$("#memOtherDiv").empty();
				cherryAjaxRequest({
					url: url,
					param: null,
					callback: function(data) {
						$("#memOtherDiv").html(data);
					}
				});
			}
			return false;
		});
		$("#detailMem").click(function(){
			var param = $("#result").find(':input[name=memberInfoId]').serialize();
			if(param) {
				var url = $("#detailUrl").attr("href");
				url = url + "?" + param;
				$("#memInitDiv").hide();
				$("#memOtherDiv").show();
				$("#memOtherDiv").empty();
				cherryAjaxRequest({
					url: url,
					param: null,
					callback: function(data) {
						$("#memOtherDiv").html(data);
					}
				});
			}
			return false;
		});
		$(document).unbind("keydown");
		$(document).keydown(function(event){
			if(event.keyCode == '112') {// F1
				$("#doSale").click();
				return false;
			} else if(event.keyCode == '113') {// F2
				$("#addMem").click();
				return false;
			} else if(event.keyCode == '114') {// F3
				$("#updMem").click();
				return false;
			} else if(event.keyCode == '115') {// F4
				$("#saleList").click();
				return false;
			} else if(event.keyCode == '116') {// F5
				$("#pointList").click();
				return false;
			} else if(event.keyCode == '117') {// F6
				$("#detailMem").click();
				return false;
			} else if(event.keyCode == '27') {// ESC
				$("#memOtherDiv").hide();
				$("#memInitDiv").show();
				$("#memOtherDiv").empty();
				return false;
			}
		});
		
		// 生成会员销售报表处理
		function memReport(object) {
			$(object).parent().find("tr[class=detail]").remove();
			if($(object).attr("class").indexOf("selectedColor") == -1) {
				$(object).addClass("selectedColor");
				$(object).siblings().removeClass("selectedColor");
				if($(object).next() && $(object).next().attr("class") == "detail") {
		    		$(object).next().show();
		    	} else {
		    		var url = $(object).find(":input[name=memReportUrl]").val();
		    		if(url) {
		    			$(object).after('<tr class="detail"><td colspan="'+$(object).find('td').length+'" class="detail box2-active"></td></tr>');
		        		var $td = $(object).next().find("td");
		        		$td.html(binolwpmbm01Message.loading);
		        		
		        		var tdWidth = $td.width()-40;
		        		if(tdWidth < 300) {
		        			tdWidth = 300;
		        		}
    	        		var width1 = parseInt(tdWidth*3/7, 10);
    	        		var width2 = parseInt((tdWidth-width1)/2, 10);
    	        		var height1 = parseInt(width1*1/3, 10);
    	        		if(height1 < 150) {
    	        			height1 = 150;
    	        		}
    	        		var height2 = parseInt(width2*4/9, 10)+25;
    	        		var tickLength = parseInt(width1/70, 10);
    	        		
    	        		var height = height1;
    	        		if(height < height2) {
    	        			height = height2;
    	        		}
    	        		var paddingTop1 = parseInt((height-height1)/2, 10);
    	        		var paddingTop2 = parseInt((height-height2)/2, 10);
    	        		
    	        		url = url + "&tickLength=" + tickLength;
		        		
		            	var callback = function(msg) {
		            		if(msg) {
		            			var data = eval('('+msg+')');
		            			var chart1List = data.chart1;
		            			var chart2List = data.chart2;
		            			var chart3List = data.chart3;
		            			var amountBars = [];
		            			var pointBars = [];
		            			var ticks = [];
		            			var chart2 = [];
		            			var chart3 = [];

		            			if(!chart1List && !chart2List && !chart3List) {
		            				$td.html(binolwpmbm01Message.nosale);
		            				return;
		            			}
		            			
		    	        		var html = '<div class="memReport">'+
		    	        			'<div style="float:left;padding-top:'+paddingTop1+'px;"><div id="chart1" style="width:'+width1+'px;height:'+height1+'px;"></div></div>'+
		    	        			'<div style="float:left;padding-top:'+paddingTop2+'px;"><div style="width:'+width2+'px;height:'+height2+'px;">'+
		    	        			'<div style="font-weight:bold;line-height:22px;margin:0 0 3px 10px;">'+binolwpmbm01Message.pieTitle1+'</div>'+
		    	        			'<div id="chart2" style="height:'+(height2-25)+'px;"></div>'+'</div></div>'+
		    	        			'<div style="float:left;padding-top:'+paddingTop2+'px;"><div style="width:'+width2+'px;height:'+height2+'px;">'+
		    	        			'<div style="font-weight:bold;line-height:22px;margin:0 0 3px 10px;">'+binolwpmbm01Message.pieTitle2+'</div>'+
		    	        			'<div id="chart3" style="height:'+(height2-25)+'px;"></div>'+'</div></div>'+
		    	        			'</div>';
		    	        		$td.html(html);
		            			
		            			if(chart1List) {
		            				for(var j = 0; j < chart1List.length; j++) {
		            					var date = chart1List[j].date;
		            					var amount = chart1List[j].amount;
		            					var point = chart1List[j].point;
		            					amountBars.push([j, amount]);
		            					pointBars.push([j, point]);
		            					var dates = date.split("-");
		            					date = dates[0].substring(2) + "/" + parseInt(dates[1], 10) + "/" + parseInt(dates[2], 10);
			            				ticks.push([j, date]);
		            				}
		            				var start = ticks.length;
		            				for(var i = start; i < tickLength; i++) {
		            					amountBars.push([i, null]);
		            					pointBars.push([i, null]);
		            				}
		            				var barsObj1 = {
	            						data: amountBars, 
	            						bars: {show: true, align: 'right', lineWidth: 0, barWidth: 0.15, fillColor: "#75A11B"}, 
	            						label: binolwpmbm01Message.amount
		            				};
			            			var barsObj2 = {
		            					data: pointBars, 
		            					bars: {show: true, align: 'left', lineWidth: 0, barWidth: 0.15, fillColor: "#F2AA08"}, 
		            					label: binolwpmbm01Message.point
			            			};
			            			$.plot($("#chart1"), [barsObj1, barsObj2], {
			            				colors:["#75A11B", "#F2AA08"],
			            	            xaxis: {
			            	            	ticks: ticks,
			            	            	tickColor: "#ffffff"
			            	            },
			            	            yaxis: {
			            	            	minTickSize: 1,
			            	            	tickFormatter: function(val, axis) {
			            	            		return val;
			            	            	},
			            	            	tickColor: null
			            	            },
			            	            grid: {
			            	            	hoverable: true,
			            	            	borderWidth: 0
			            	            },
			            	            legend: {
			            	            	show: true,
			            	            	position: "ne"
			            	            }
			            	        });
		            				var previousPoint = null, previousLabel = null;
		            		        $("#chart1").bind("plothover", function (event, pos, item) {
		            		        	if (item) {
		            						if ((previousLabel != item.series.label) || (previousPoint != item.dataIndex)) {
		            							previousPoint = item.dataIndex;
		            							previousLabel = item.series.label;
		            							$("#tooltip").remove();
		            							var y = item.datapoint[1];
		            							var offset = 5;
		            							if(item.series.label == binolwpmbm01Message.amount) {
		            								offset = -5
		            							}
		            							showTooltip(item.pageX+offset, item.pageY, item.series.label+"："+y);
		            						}
		            					} else {
		            						$("#tooltip").remove();
		            						previousPoint = null;
		            					}
		            				});
		            			}
		            			if(chart2List) {
		            				for(var j = 0; j < chart2List.length; j++) {
			            				var name = chart2List[j].name;
			            				var value = chart2List[j].value;
										var labelText = name == "-1" ? binolwpmbm01Message.other : name;
										//labelText += "("+value+")";
										chart2.push({"label":labelText,"data":value});
									}
		            				$.plot($("#chart2"), chart2,
	    	            			{
	        					        colors:["#75A11B", "#F2AA08", "#CB4B4B", "#9440ED", "#1E90FF", "#3CB371", "#CD853F", "#BB5500", "#6A5ACD", "#00CED1"],
	        					        series: {
	        					            pie: {
	        					                show: true,
	        					                radius: 1,
	        					                label: {
	        					                    show: true,
	        					                    radius: 3/4,
	        					                    formatter: function(label, series){
	        					                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;line-height:18px;">'+
	        					                        Math.round(series.percent)+'%</div>';
	        					                    }
	        					                }
	        					            }
	        					        },
	        					        legend: {
	        					            show: true,
	        					            position:"ne"
	        					        }
	    	        				});
		            			}
		            			if(chart3List) {
		            				for(var j = 0; j < chart3List.length; j++) {
			            				var name = chart3List[j].name;
			            				var value = chart3List[j].value;
										var labelText = name == "-1" ? binolwpmbm01Message.other : name;
										//labelText += "("+value+")";
										chart3.push({"label":labelText,"data":value});
									}
		            				$.plot($("#chart3"), chart3,
	    	            			{
	        					        colors:["#75A11B", "#F2AA08", "#CB4B4B", "#9440ED", "#1E90FF", "#3CB371", "#CD853F", "#BB5500", "#6A5ACD", "#00CED1"],
	        					        series: {
	        					            pie: {
	        					                show: true,
	        					                radius: 1,
	        					                label: {
	        					                    show: true,
	        					                    radius: 3/4,
	        					                    formatter: function(label, series){
	        					                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;line-height:18px;">'+
	        					                        Math.round(series.percent)+'%</div>';
	        					                    }
	        					                }
	        					            }
	        					        },
	        					        legend: {
	        					            show: true,
	        					            position:"ne"
	        					        }
	    	        				});
		            			}
		            		} else {
		            			$td.html(binolwpmbm01Message.nosale);
		            		}
		            	};
		            	cherryAjaxRequest({
		            		url: url,
		            		callback: callback
		            	});
		    		}
		    	}
			} else {
				$(object).removeClass("selectedColor");
			}
		}
		
		function showTooltip(x, y, contents) {
			var width = 120;
			if($("body").width() - x < width) {
				x = x - width - 5;
			}
			$('<div id="tooltip">' + contents + '</div>').css({
				position: 'absolute',
				display: 'none',
				width: width,
				'text-align': 'center',
				top: y - 30,
				left: x,
				border: '1px solid #CB4B4B',
				padding: '5px 2px 3px 2px',
				'background-color': '#CB4B4B',
				'font-size': '12px',
				opacity: 0.80
			}).appendTo("body").fadeIn(200);
		}
		
		// 按钮无效
		function buttonDisable() {
			$("#updMem").attr("disabled","disabled");
			$("#updMem").attr("class","btn_top_disable");
			$("#saleList").attr("disabled","disabled");
			$("#saleList").attr("class","btn_top_disable");
			$("#pointList").attr("disabled","disabled");
			$("#pointList").attr("class","btn_top_disable");
			$("#detailMem").attr("disabled","disabled");
			$("#detailMem").attr("class","btn_top_disable");
		}
		
		// 按钮有效
		function buttonEnable() {
			$("#updMem").removeAttr("disabled");
			$("#updMem").attr("class","btn_top");
			$("#saleList").removeAttr("disabled");
			$("#saleList").attr("class","btn_top");
			$("#pointList").removeAttr("disabled");
			$("#pointList").attr("class","btn_top");
			$("#detailMem").removeAttr("disabled");
			$("#detailMem").attr("class","btn_top");
		}
		
		// 存在检索条件进行初始查询
		if(!validateParam()) {
			$('#search').click();
		}
	},
	// 省、市、县级市的联动查询
	"getNextRegin": function(obj, text, grade) {
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
				var str = '<ul class="u2" style="width: 500px;"><li onclick="binolwpmbm01.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
			    for (var one in json){
			        str += '<li id="'+json[one]["regionId"]+'" onclick="binolwpmbm01.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
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
	// 显示省、市或县级市信息
	"showRegin": function(object, reginDiv) {
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
	// 查询明细信息
	"searchDetail" : function(object) {
		$(object).parent().find("tr[class=detail]").remove();
		if($(object).attr("class").indexOf("selectedColor") == -1) {
			$(object).addClass("selectedColor");
			$(object).siblings().removeClass("selectedColor");
			if($(object).next() && $(object).next().attr("class") == "detail") {
	    		$(object).next().show();
	    	} else {
	    		var url = $(object).find("input").val();
	    		if(url) {
	    			$(object).after('<tr class="detail"><td colspan="'+$(object).find('td').length+'" class="detail box2-active"></td></tr>');
	        		var $td = $(object).next().find("td");
	            	var callback = function(msg) {
	            		$td.html(msg);
	            	};
	            	cherryAjaxRequest({
	            		url: url,
	            		callback: callback
	            	});
	    		}
	    	}
		} else {
			$(object).removeClass("selectedColor");
		}
	},
	"synMoblie" : function(Object){
		var $this = $(Object);
		var memCode = $this.val();
		$("#saveForm #mobilePhone").val(memCode);
	}
};

var binolwpmbm01 = new BINOLWPMBM01();
