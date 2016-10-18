/**
 * @author LuoHong
 */
/**以页面为单位的js函数采用原型的方法声明，调用的时候采用"画面名.方法名"*/
var BINOLBSPAT01=function(){};

BINOLBSPAT01.prototype = {
		"search":function(){
			var $form = $('#mainForm');
			if (!$form.valid()) {
				return false;
			};
			var url = $("#searchUrl").attr("href");
			 // 查询参数序列化
			var param= $form.serialize();
			 url = url + "?" + param;

			 // 显示结果一览
			 $("#section").show();
			// 表格设置
			 var tableSetting = {
					 // 表格ID
					 tableId : '#dataTable',
					 // 数据URL
					 url : url,
					 // 排序列
					 aaSorting : [[2, "desc"]],
					 // 表格列属性设置
					 aoColumns : [	{ "sName": "checkbox", "bSortable": false}, // 0
			                    	{ "sName": "no", "bSortable": false},			// 1
			                    	{ "sName": "code"},	// 2	
									{ "sName": "nameCn"},	                // 3	
									{ "sName": "nameEn","bVisible": false},	//4
									{ "sName": "region"},	// 5	
									{ "sName": "province"},	                // 6	
									{ "sName": "city"},	//7
									{ "sName": "phoneNumber"},//8
									{ "sName": "postalCode","bVisible": false, "sClass": "center"},// 9
									{ "sName": "contactPerson","bVisible": false},  //10
									{ "sName": "contactAddress","bVisible": false}, //11
									{ "sName": "address"}, //12
									{ "sName": "deliverAddress","bVisible": false}, //13
			                        { "sName": "validFlag", "sClass": "center"},//14
									{ "sName": "Operate", "bSortable": false,"sClass": "center" }], // 15
					// 横向滚动条出现的临界宽度
					sScrollX : "100%",
					// 不可设置显示或隐藏的列	
					aiExclude :[0, 1, 2],
					// 固定列数
					fixedColumns : 3
			 };
			 // 调用获取表格函数
			 getTable(tableSetting);
			},
			"checkSelectAll":function (checkbox){
			$("#errorMessage").empty();
			$('#dataTable_wrapper #dataTable_Cloned').find(":checkbox").prop("checked",$(checkbox).prop("checked"));
			if ($(checkbox).prop("checked") && $("input@[id=validFlag][checked]").length>0){
			}
		},
		// 根据品牌ID筛选下拉列表
		"changeBrandInfo" : function(object,text) {
			// 清空省信息
			$('#provinceId').val("");
			$("#provinceText").text(text);
			$('#provinceTemp').empty();
			// 清空城市信息
			$('#cityId').val("");
			$("#cityText").text(text);
			$('#cityTemp ul').empty();
			// 清空渠道信息
			$('#channelId').empty();
			$('#channelId').append('<option value="">'+text+'</option>');
			if($(object).val() != "") {
				var callback = function(msg){
					if(msg) {
						// 默认地区
						var defaultTitle = $('#defaultTitle').text();
						// 全部
						var defaultText = $('#defaultText').text();
						var jsons = eval('('+msg+')');
						if(jsons.reginList.length > 0) {
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
						if(jsons.channelList.length > 0) {
							for(var i in jsons.channelList) {
								$('#channelId').append('<option value="'+jsons.channelList[i].channelId+'">'+escapeHTMLHandle(jsons.channelList[i].channelName)+'</option>');
							}
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

		"checkSelect":function(checkbox){
			$("#errorMessage").empty();
			if($(checkbox).prop("checked")) {
		        if($("#dataTable_Cloned input@[id=validFlag]").length == $("#dataTable_Cloned input@[id=validFlag][checked]").length) {
		            $("input@[id=allSelect]").prop("checked",true);
		        }
		    } else {
		        $("input@[id=allSelect]").prop("checked",false);
		    }
		},
		
		/*
		 * 确认画面
		 */
		"confirmInit":function (object,confirmname){
			if(!this.validCheckBox()){
		        return false;
		    };
			var param = $(object).parent().find(':input').serialize();
			param += "&"+$("#dataTable_Cloned").find(":checkbox[checked]").nextAll("#partnerIdArr").serialize();
			var strText = '';
			var strTitle = '';
			if (confirmname == 'disable') {
				strText="#disableText";
				strTitle="#disableTitle";
			}else if(confirmname == 'enable'){
				strText="#enableText";
				strTitle="#enableTitle";
			}
			var dialogSetting = {
				dialogInit: "#dialogInit",
				text:  $(strText).html(),
				width: 	400,
				height: 300,
				title: 	$(strTitle).text(),
				confirm: $("#dialogConfirm").text(),
				cancel: $("#dialogCancel").text(),
				confirmEvent: function(){BINOLBSPAT01.confirmProcess($(object).attr("href"),param);},
				cancelEvent: function(){removeDialog("#dialogInit");}
			};
			openDialog(dialogSetting);
		},
		"confirmProcess":function (url,param) {
			var callback = function(msg) {
				$("#dialogInit").html(msg);
				if($("#errorMessageDiv").length > 0) {
					$("#errorMessageDiv").find("span").html($("#errorMessageDiv").next().text());
					$("#dialogInit").dialog( "option", {
						buttons: [{
							text: $("#dialogClose").text(),
						    click: function(){removeDialog("#dialogInit");if(oTableArr[0] != null)oTableArr[0].fnDraw(); }
						}]
					});
				} else {
					removeDialog("#dialogInit");
					if(oTableArr[0] != null)oTableArr[0].fnDraw();
				}
			};
			cherryAjaxRequest({
				url: url,
				param: param,
				callback: callback,
				formId: '#mainForm'
			});
		},
		/*
		 * 验证勾选，显示隐藏错误提示
		 */
		"validCheckBox":function (){
			var flag = true;
			var checksize = $("input@[id=validFlag][checked]").length;
			if (checksize == 0){
			    //没有勾选
				$("#errorMessage").html
		
		($("#errorMessageTemp").html());
		        $('#errorMessage').show();
		        flag = false;
		        }
			return flag;
		},
		
		/*
		 * 关闭Dialog
		 */
		"dialogClose":function (){
			this.doClose('#dialogInit');
		}
};

var BINOLBSPAT01 = new BINOLBSPAT01();
	
//画面初始状态加载
$(document).ready(function() {
	
	$("#result_table table").attr("id", "dataTable");
	$("#result_list").append($("#result_table").html());
	$("#result_table table").removeAttr("id");
	BINOLBSPAT01.search();
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
});

