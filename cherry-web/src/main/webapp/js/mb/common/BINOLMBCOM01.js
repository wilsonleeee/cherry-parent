function BINOLMBCOM01(){};

BINOLMBCOM01.prototype = {
	// 选择发卡柜台弹出框
	"popCounterList": function(url, clubKbn) {
		var $organizationId;
		var $organizationCode;
		var $counterKind;
		var $counterDiv;
		var clickHtm;
		var param = "privilegeFlg=1";
		// 俱乐部发卡柜台
		if ('1' == clubKbn) {
			$organizationId = $("#organizationIdClub");
			$organizationCode = $("#organizationCodeClub");
			$counterKind = $("#counterKindClub");
			$counterDiv = $("#counterDivClub");
			clickHtm = "binolmbcom01.delHtml(this,1,'1');";
			param += "&" + $("#memberClubId").serialize();
		} else {
			$organizationId = $("#organizationId");
			$organizationCode = $("#organizationCode");
			$counterKind = $("#counterKind");
			$counterDiv = $("#counterDiv");
			clickHtm = "binolmbcom01.delHtml(this,1);";
		}
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$organizationId.val($checkedRadio.val());
				$organizationCode.val($checkedRadio.next().val());
				$counterKind.val($checkedRadio.next().next().val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="' + clickHtm +'"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$counterDiv.html(html);
//				$("#employeeId").val("");
//				$("#employeeCode").val("");
//				$("#employeeDiv").empty();
			}
		}
		var value = $organizationId.val();
		popDataTableOfCounterList(url, param, value, callback);
	},
	// 选择发卡BA弹出框
	"popEmployeeList": function(url, errorMsg, clubKbn) {
//		if($("#organizationId").val() != "") {
//			var callback = function(tableId) {
//				var $checkedRadio = $("#"+tableId).find(":input[checked]");
//				if($checkedRadio.length > 0) {
//					$("#employeeId").val($checkedRadio.val());
//					$("#employeeCode").val($checkedRadio.next().val());
//					var code = $checkedRadio.parent().next().text();
//					var name = $checkedRadio.parent().next().next().text();
//					var html = name != '' ? '(' + code + ')' + name : code;
//					html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,2);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
//					$("#employeeDiv").html(html);
//				}
//			}
//			var param = "organizationId="+$("#organizationId").val()+"&categoryCode=01";
//			var value = $("#employeeId").val();
//			popDataTableOfEmployeeList(url, param, value, callback);
//		} else {
//			return confirm(errorMsg);
//		}
		var $employeeId;
		var $employeeCode;
		var $employeeDiv;
		var clickHtm;
		// 俱乐部BA
		if ('1' == clubKbn) {
			$employeeId = $("#employeeIdClub");
			$employeeCode = $("#employeeCodeClub");
			$employeeDiv = $("#employeeDivClub");
			clickHtm = "binolmbcom01.delHtml(this,2,'1');";
		} else {
			$employeeId = $("#employeeId");
			$employeeCode = $("#employeeCode");
			$employeeDiv = $("#employeeDiv");
			clickHtm = "binolmbcom01.delHtml(this,2);";
		}
		var callback = function(tableId) {
			var $checkedRadio = $("#"+tableId).find(":input[checked]");
			if($checkedRadio.length > 0) {
				$employeeId.val($checkedRadio.val());
				$employeeCode.val($checkedRadio.next().val());
				var code = $checkedRadio.parent().next().text();
				var name = $checkedRadio.parent().next().next().text();
				var html = name != '' ? '(' + code + ')' + name : code;
				html += '<span class="close" style="margin: 0 10px 2px 5px;" onclick="' + clickHtm + '"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>';
				$employeeDiv.html(html);
			}
		}
		var param = "categoryCode=01&privilegeFlg=1";
		var value = $employeeId.val();
		popDataTableOfEmployeeList(url, param, value, callback);
	},
	"delHtml": function(object, flag, cbkbn) {
		if ('1' == cbkbn) {
			if(flag == 1) {
				$("#organizationIdClub").val("");
				$("#organizationCodeClub").val("");
				$("#counterKindClub").val("");
				$("#counterDivClub").empty();
	//			$("#employeeId").val("");
	//			$("#employeeCode").val("");
	//			$("#employeeDiv").empty();
			} else {
				$("#employeeIdClub").val("");
				$("#employeeCodeClub").val("");
				$("#employeeDivClub").empty();
			}
		} else {
			if(flag == 1) {
				$("#organizationId").val("");
				$("#organizationCode").val("");
				$("#counterKind").val("");
				$("#counterDiv").empty();
	//			$("#employeeId").val("");
	//			$("#employeeCode").val("");
	//			$("#employeeDiv").empty();
			} else {
				$("#employeeId").val("");
				$("#employeeCode").val("");
				$("#employeeDiv").empty();
			}
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
				var str = '<ul class="u2" style="width: 500px;"><li onclick="binolmbcom01.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
			    for (var one in json){
			        str += '<li id="'+json[one]["regionId"]+'" onclick="binolmbcom01.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
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
	}
};

var binolmbcom01 =  new BINOLMBCOM01();
