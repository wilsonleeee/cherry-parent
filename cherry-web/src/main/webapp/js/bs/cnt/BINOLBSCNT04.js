
function BINOLBSCNT04() {};

BINOLBSCNT04.prototype = {	
		// 根据品牌ID筛选下拉列表
		"changeBrandInfo" : function(object,text) {
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
			// 清空渠道信息
			$('#channelId').empty();
			$('#channelId').append('<option value="">'+text+'</option>');
			// 清空商场信息
			$('#mallInfoId').empty();
			$('#mallInfoId').append('<option value="">'+text+'</option>');
			// 清空经销商信息
			$('#resellerInfoId').empty();
			$('#resellerInfoId').append('<option value="">'+text+'</option>');
			// 清空柜台主管
			$('#counterHeadDiv').find('tbody').empty();
			// 清空关注柜台的人
			$('#likeCounterEmpDiv').find('tbody').empty();
			// 清空上级部门
			$('#higheOrgDiv').find('tbody').empty();
			if($(object).val() != "") {
				var callback = function(msg){
					if(msg) {
						// 默认地区
						var defaultTitle = $('#defaultTitle').text();
						// 全部
						var defaultText = $('#defaultText').text();
						var jsons = eval('('+msg+')');
						// 区域信息存在
						if(jsons.reginList.length > 0) {
							var html = '<div class="clearfix"><span class="label">'+defaultTitle+'</span><ul class="u2"><li onclick="binolbscnt04.getNextRegin(this, \''+text+'\', 1);return false;">'+defaultText+'</li></ul></div>';
							for(var i in jsons.reginList) {
								html += '<div class="clearfix"><span class="label">'+escapeHTMLHandle(jsons.reginList[i].reginName)+'</span><ul class="u2">';
								for(var j in jsons.reginList[i].provinceList) {
									html += '<li id="'+jsons.reginList[i].provinceList[j].provinceId+'" onclick="binolbscnt04.getNextRegin(this, \''+text+'\', 1);">'+escapeHTMLHandle(jsons.reginList[i].provinceList[j].provinceName)+'</li>';
								}
								html += '</ul></div>';
							}
							$('#provinceTemp').html(html);
						}
						// 渠道信息存在
						if(jsons.channelList.length > 0) {
							for(var i in jsons.channelList) {
								$('#channelId').append('<option value="'+jsons.channelList[i].channelId+'">'+escapeHTMLHandle(jsons.channelList[i].channelName)+'</option>');
							}
						}
						// 商场信息存在
						if(jsons.mallInfoList.length > 0) {
							for(var i in jsons.mallInfoList) {
								$('#mallInfoId').append('<option value="'+jsons.mallInfoList[i].mallInfoId+'">'+escapeHTMLHandle(jsons.mallInfoList[i].mallName)+'</option>');
							}
						}
						// 经销商信息存在
						if(jsons.resellerInfoList.length > 0) {
							for(var i in jsons.resellerInfoList) {
								$('#resellerInfoId').append('<option value="'+jsons.resellerInfoList[i].resellerInfoId+'">'+escapeHTMLHandle(jsons.resellerInfoList[i].resellerName)+'</option>');
							}
						}
						// 柜台号存在
						if(jsons.counterCode) {
							$('#counterCode').val(jsons.counterCode);
						}
					}
				};
				cherryAjaxRequest({
					url: $('#filter_url').val(),
					param: $(object).serialize(),
					callback: callback
				});
			}
		},
		/**
		 * 取得柜台编号(自然堂)
		 */
		"getCntCode" : function(){
			var url = $("#getCntCodeUrlId").val();
			var params = $('#mainForm').serialize();
			var parentToken = getParentToken();
			params += "&" + parentToken;
			
			cherryAjaxRequest({
				url: url,
				param: params,
				reloadFlg : true,
				callback: function(data){
//					alert("getCntCode: " + data);
					$("#counterCodeId").text(data);
					$("#counterCode").val(data);
				}
			});
		},
		// 保存柜台处理
		"addCounter" : function(url) {
			if(!$('#mainForm').valid()) {
				return false;
			}
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
				}
			};
			cherryAjaxRequest({
				url: url,
				param: $('#mainForm').serialize(),
				callback: callback
			});
		},
		"close" : function() {
			window.close();
		},
		"selectResellerDepart" : function() {
			var param = "privilegeFlg=1&businessType=0&departType=3";
			var callback = function() {
	    		var checkedRadio = $("#departDialogInit").find("input[name='organizationId']:checked");
	    		var departId = "";
	    		var departName = "";
	    		if(checkedRadio){
	    			departId = checkedRadio.val();
		    		departName = checkedRadio.parent().parent().children("td").eq(2).find("span").text().escapeHTML();
	    		}
	    		$("#resellerDepartId").val(departId);
	    		$("#showResellerDepart").html(departName.unEscapeHTML());
	    	};
			var option = {
					"brandInfoId": $("#brandInfoId").val(),
					"checkType": "radio",
					"param": param,
					"click": callback
			};
			popAjaxDepDialog(option);
		},
		
		/**
		 * 在所属系统的text框上绑定下拉框选项
		 * @param Object:options
		 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
		 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
		 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中柜台名称；"code":表示要选中的是柜台CODE；"codeName":表示的是选中的是(code)name。默认是"name"
		 * 
		 * */
		"belongFactionBinding" : function(options){
			var csrftoken = '';
			if($("#csrftokenCode").length > 0) {
				csrftoken = $("#csrftokenCode").serialize();
				if(!csrftoken) {
					csrftoken = $('#csrftokenCode',window.opener.document).serialize();
				}
			} else {
				csrftoken = $('#csrftoken').serialize();
				if(!csrftoken) {
					csrftoken = $('#csrftoken',window.opener.document).serialize();
				}
			}
			var strPath = window.document.location.pathname;
			var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			var url = postPath+"/common/BINOLCM21_getCodes.action"+"?"+csrftoken;
			
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					originalBrandStr: function() { return $('#'+options.elementId).val();},
					codeType: options.codeType,
					//默认是最多显示50条
					number:options.showNum? options.showNum : 10,
					//默认是选中柜台名称
					selected:options.selected ? options.selected : "value1",
				},
			    
				loadingClass: "ac_loading",
				minChars:1,
			    matchContains:1,
			    matchContains: true,
			    scroll: false,
			    cacheLength: 0,
			    width:230,
			    max:options.showNum ? options.showNum : 50,
				formatItem: function(row, i, max) {
					return escapeHTMLHandle(row[1]);//+" "+"["+escapeHTMLHandle(row[1])+"]";
				}
			}).result(function(event, data, formatted){
				$('#belongFactionName').val(data[1]);
				$('#belongFaction').val(data[0]);
			}).bind("change",function(){
				$('#belongFaction').val("");
			});
		},
		
		/**
		 * 在开票单位的text框上绑定下拉框选项
		 * @param Object:options
		 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
		 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
		 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中柜台名称；"code":表示要选中的是柜台CODE；"codeName":表示的是选中的是(code)name。默认是"name"
		 * 
		 * */
		"invoiceCompanyBinding" : function(options){
			var csrftoken = '';
			if($("#csrftokenCode").length > 0) {
				csrftoken = $("#csrftokenCode").serialize();
				if(!csrftoken) {
					csrftoken = $('#csrftokenCode',window.opener.document).serialize();
				}
			} else {
				csrftoken = $('#csrftoken').serialize();
				if(!csrftoken) {
					csrftoken = $('#csrftoken',window.opener.document).serialize();
				}
			}
			var strPath = window.document.location.pathname;
			var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
			var url = postPath+"/common/BINOLCM21_getCodes.action"+"?"+csrftoken;
			
			$('#'+options.elementId).autocompleteCherry(url,{
				extraParams:{
					originalBrandStr: function() { return $('#'+options.elementId).val();},
					codeType: options.codeType,
					//默认是最多显示50条
					number:options.showNum? options.showNum : 10,
							//默认是选中柜台名称
							selected:options.selected ? options.selected : "value1",
				},
				
				loadingClass: "ac_loading",
				minChars:1,
				matchContains:1,
				matchContains: true,
				scroll: false,
				cacheLength: 0,
				width:230,
				max:options.showNum ? options.showNum : 50,
						formatItem: function(row, i, max) {
							return escapeHTMLHandle(row[1]);//+" "+"["+escapeHTMLHandle(row[1])+"]";
						}
			}).result(function(event, data, formatted){
				$('#invoiceCompanyName').val(data[1]);
				$('#invoiceCompany').val(data[0]);
			});			
		},
		
		// 省、市、县级市的联动查询
		"getNextRegin" :function (obj, text, grade) {
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
				
				// 清除错误信息
				$("#provinceId").parent().removeClass('error');
				$("#provinceId").parent().find('#errorText').remove();
				
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
				
				// 根据城市CityID等生成柜台编号(自然堂)
				// 取得柜台编号(自然堂)
				if($("#cntCodeRule").val() == 2){
					this.getCntCode();
				}
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
//			alert(param);
			var callback = function(msg) {
//				alert(msg);
				if(msg) {
					// 全部
					var defaultText = $('#defaultText').text();
					var json = eval('('+msg+')'); 
					var str = '<ul class="u2"><li onclick="binolbscnt04.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+defaultText+'</li>';
				    for (var one in json){
				        str += '<li id="'+json[one]["regionId"]+'" onclick="binolbscnt04.getNextRegin(this, \''+text+'\', '+nextGrade+');">'+escapeHTMLHandle(json[one]["regionName"])+'</li>';
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
};

var binolbscnt04 =  new BINOLBSCNT04();

$(document).ready(function() {
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	
	// 表单验证初期化
	cherryValidate({
		formId: 'mainForm',
		rules: {
			//counterCode: {required: true,maxlength: 15},
			counterNameIF: {required: true,maxlength: 50},
			counterKind: {required: true},
			brandInfoId: {required: true},
//			provinceId: {required: true},
//			cityId: {required: true},
			counterNameShort: {maxlength: 20},
			nameForeign: {maxlength: 50},
			counterTelephone: {maxlength: 20},
			counterAddress: {maxlength: 100},
			counterSpace: {floatValid: [4,2]},
			employeeNum:{validIntNum:true},
			passWord:{maxlength: 15},
			expiringDateDate : {dateValid:true},
			expiringDateTime : {timeHHmmssValid:true},
			longitude : {lonValid:true},
			latitude : {latValid:true},
			posFlag : {required: true},
			busniessPrincipal: {maxlength: 50}
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

window.onbeforeunload = function(){
	if(window.opener){
		window.opener.unlockParentWindow();
	}
};



