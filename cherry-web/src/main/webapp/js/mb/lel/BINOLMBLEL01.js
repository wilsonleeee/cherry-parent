function BINOLMBLEL01() {
}
BINOLMBLEL01.prototype = {
	// 弹出框内容	
    "dialogHtml" : "",
    
    // 重算弹出框
    "reCalcDialogHtml" : "",
    
	// 取得参数
	"getParams" : function() {
		// 参数(品牌信息ID)
		var params = $("#brandInfoId").serialize();
		params += "&" + $('#csrftoken').serialize();
		if($("#brandName").length != 0){
			params += "&" + $("#brandName").serialize();
		}
		if ($("#memberClubId").length > 0) {
			params += "&" + $("#memberClubId").serialize();
		}
		return params;
	},
	// 查询
	"search" : function(url) {
		cherryAjaxRequest({
			url: url,
			param: this.getParams(),
			callback: function(msg) {$("#lelBody").html(msg);}
		});
	},
	"selBrand" : function(url) {
		if ($("#memberClubId").length == 0) {
			BINOLMBLEL01.search(url);
		} else {
			var param = this.getParams();
			var clubUrl = $("#searchClubUrl").attr("href");
			$("#memberClubId").empty();
			doAjax2(clubUrl, "memberClubId", "clubName", $("#memberClubId"), param, null, function(msg){
				//if(window.JSON && window.JSON.parse) {
				//	var msgJson = window.JSON.parse(msg);
				//	if ($.isEmptyObject(msgJson)) {
				if ($("#memberClubId").find("option").length == 0) {
						var str = '<option value="">--未设置--</option>';
						$("#memberClubId").append(str);
				}
				//	}
				//}
				BINOLMBLEL01.search(url);
			});
		}
	},
	// 编辑
	"edit" : function(url){
		cherryAjaxRequest({
			url: url,
			param: this.getParams(),
			callback: function(msg) {$("#div_main").html(msg);}
		});
	},
	// 初期化日期
	"initDate" : function() {
		var $table = $("#levelTable");
		var dateHolidaysVal = $("#dateHolidays").val();
		$table.find(".date").unbind("cherryDate");
		$table.find("tr").each(
				function() {
					$(this).find("input[name='fromDate']").not("input:disabled").cherryDate(
							{
								holidayObj : dateHolidaysVal,
								beforeShow : function(input) {
									var value = $(input).parents("tr").find(
											"input[name='toDate']").val();
									return [ value, "maxDate" ];
								}
							});
					$(this).find("input[name='toDate']").cherryDate(
							{
								holidayObj : dateHolidaysVal,
								beforeShow : function(input) {
									var value = $(input).parents("tr").find(
											"input[name='fromDate']").val();
									return [ value, "minDate" ];
								}
							});
				});
	},
	// 添加新行
	"addLine" : function(target, source) {
		var $trs = $(target).find("tr");
		var $source = $(source);
		// 目标最后一行
		var $lastLine = $trs.last();
		// 新行序号列
		var $cols2 = $source.find("td:first");
		// 目标行class
		var lastClass = "";
		// 目标最后一行序号
		var num = 0;
		// 无行
		if ($lastLine.length == 0) {
			lastClass = "even";
		} else {
			// 目标行class
			lastClass = $lastLine.prop("class");
			// 目标序号列
			var $cols1 = $lastLine.find("td:first");
			// 目标最后一行序号
			num = parseInt($cols1.html());
		}
		// 新行添加序号
		$cols2.html(num + 1);
		// 新行添加Class
		if (lastClass == "odd") {
			$source.attr("class", "even");
		} else {
			$source.attr("class", "odd");
		}
		// 目标追加新行
		$(target).append($source.html());
		// 日期初始化
		this.initDate();
	},
	// 删除行
	"delLine" : function(_this) {
		var $this = $(_this);
		// 目标行
		var $targetLine = $this.parent().parent();
		var $levelId = $targetLine.find("input[name='levelId']");
		// nextAll行
		var $nextAll = $targetLine.nextAll();
		if ($levelId.length != 0) {
			$("#delList").append('<span><input type="hidden" name="levelId" value="' + $levelId.val() + '"/></span>');
		}
		// 删除目标行
		$targetLine.remove();
		// 序号-1
		$nextAll.each(function() {
			// 序号列
			var $col = $(this).find("td:first");
			$col.text(parseInt($col.text()) - 1);
		});
	},
	// 对象JSON化
	"toJSON" : function (obj){
		var JSON = [];
		$(obj).find(':input').each(function(){
			if(!$(this).is(":radio") || $(this).is(":radio[checked]")){
				$this = $(this);
				if($.trim($this.val())!= '') {
					var name = $this.prop("name");
					if(name.indexOf("defaultLevel") > -1){
						name = "defaultLevel";
					}
					JSON.push('"'+name+'":"'
						+encodeURIComponent($.trim($this.val())
							.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
				}
			}
		});
		return "{"+JSON.toString()+"}";
	},
	// 对象JSON数组化
	"toJSONArr" : function ($obj){
		var that = this;
		var JSONArr = [];
		$obj.each(function(){
			JSONArr.push(that.toJSON(this));
		});
		return "["+JSONArr.toString()+"]";
	},
	// 下一步
	"next" :function(url){
		var json = this.toJSONArr($("#levelTable").find("tr"));
		var delJson = this.toJSONArr($("#delList").find("span"));
		var param = this.getParams();
		param += "&json=" + json + "&delJson=" + delJson;
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') == -1 
					&& msg.indexOf('id="fieldErrorDiv"') == -1){
					$("#div_main").html(msg);
				}
			}
		});
	},
	// 一览画面（取消）
	"list" :function(url){
		cherryAjaxRequest({
			url: url,
			param: this.getParams(),
			callback: function(msg) {$("#div_main").html(msg);}
		});
	},
	// 上一步
	"back" :function(url){
		var json = $("#json").serialize();
		var delJson = $("#delJson").serialize();
		var param = this.getParams();
		param += "&" + json + "&" + delJson;
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {$("#div_main").html(msg);}
		});
	},
	// 保存
	"save" :function(url){
		var param = this.getParams();
		var delJson = $("#delJson").serialize();
		var json = $("#json").serialize();
		var detailJson = this.toJSONArr($("#tables").find("tr"));
		param += "&" + json + "&" + delJson + "&detailJson=" + detailJson;
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') == -1 
					&& msg.indexOf('id="fieldErrorDiv"') == -1){
					$("#div_main").html(msg);
				}
			}
		});
	},
	// 移动
	"move" : function (obj, flag){
		var tableId = "setGradeTable";
		var tHtml = '';
		var mHtml = '';
		var defaultId = $("#" + tableId).find("tr").find(":radio[checked]").first().attr("id");
		if($("#" + tableId).find("tr").length > 1){
			if(flag == 0){
				mHtml = $(obj).parent().parent().html();
				$(obj).parent().parent().remove();
				tHtml = $("#" + tableId).html();
				$("#" + tableId).empty();
				$("#" + tableId).html('<tr>' + mHtml + '</tr>');
				$("#" + tableId).append(tHtml);
			}else if(flag == 1){
				tHtml = $(obj).parent().parent().prev().html();
				mHtml = $(obj).parent().parent().html();
				$(obj).parent().parent().prev().html(mHtml);
				$(obj).parent().parent().html(tHtml);
			}else if(flag == 2){
				tHtml = $(obj).parent().parent().next().html();
				mHtml = $(obj).parent().parent().html();
				$(obj).parent().parent().next().html(mHtml);
				$(obj).parent().parent().html(tHtml);
			}else if(flag == 3){
				mHtml = $(obj).parent().parent().html();
				$(obj).parent().parent().remove();
				$("#" + tableId).append('<tr>' + mHtml + '</tr>');
			}
			BINOLMBLEL01.arrowsFlag();
			$("#" + defaultId).attr("checked","checked");
		}
	},
	
	"arrowsFlag" : function (){
		var tableId = "setGradeTable";
		var trLength = $("#" + tableId).find("tr").length;
		$("#" + tableId).find("tr").each(function (i){
			var msgHtml = '';
			if($(this).find("td").eq(3).find("a").length > 0 ){
				if(i != 0){
					msgHtml = msgHtml + '<a href="#" onclick="BINOLMBLEL01.move(this,0);return false;"' + 
            			'class="left"><span class="arrow-first"></span></a>' +
            			'<a href="#" onclick="BINOLMBLEL01.move(this,1);return false;" '+
            			'class="left"><span class="arrow-up"></span></a>';
				}else{
					msgHtml = msgHtml + '<span class="left" style="height:16px; width:16px; display:block;"></span>' +
            			'<span class="left" style="height:16px; width:16px; display:block;"></span>';
				}
				if(i != trLength - 1){
					msgHtml = msgHtml + '<a href="#" onclick="BINOLMBLEL01.move(this,2);return false;" ' +
            			'class="left"><span class="arrow-down"></span></a>' +
            			'<a href="#" onclick="BINOLMBLEL01.move(this,3);return false;"' + 
            			'class="left"><span class="arrow-last"></span></a>';
				}
				$(this).find("td").eq(3).html(msgHtml);
			}
			var gradeHtml = '';
			var gradeIndex = parseInt(i) + 1;
			for(var j = -1;j < i;j++){
				gradeHtml = gradeHtml + '<span class="ui-icon icon-star-big"></span>';
			}
			gradeHtml = gradeHtml + '<input type="hidden" name="grade" value="' + gradeIndex+ '"/>';
			$(this).find("td").eq(0).html(gradeHtml);
		});
	},
	
	/*
	 * 执行规则重算
	 */
	"execReCalc":function(){
		$("#reCalcActionResult").empty();
		if (!$('#reCalcDialogForm').valid()) {
			return false;
		};
		var isconf = confirm($("#reCalcReconf").html());
		if (!isconf) {
			return false;
		}
		var url = $("#execReCalcUrl").attr("href");
		// 参数(品牌信息ID)
		var param = $("#reCalcDate").serialize() + "&" + $("#brandInfoId").serialize();
		// 禁用确定按钮
		$("#confirmBtn").attr("disabled", "disabled");
		cherryAjaxRequest({
			url: url,
			param: param,
			isDialog: true,
			resultId: "#reCalcActionResult",
			bodyId: "#reCalcDialog",
			callback: function(msg) {
				// 激活确定按钮
				$("#confirmBtn").removeAttr("disabled");
			}
		});
	},
	
	/*
	 * 规则重算初始化pop窗口
	 */
	"reCalcDialogInit":function(){
		if ("" == BINOLMBLEL01.reCalcDialogHtml) {
			BINOLMBLEL01.reCalcDialogHtml = $("#reCalcDialogMain").html();
		} else {
			$("#reCalcDialogMain").html(BINOLMBLEL01.reCalcDialogHtml);
		}
		$('#reCalcDate').cherryDate();
		cherryValidate({			
			formId: "reCalcDialogForm",		
			rules: {		
				reCalcDate: {required: true, dateValid:true}	// 重算开始日
			}		
		});
		var dialogSetting = {
				bgiframe: true,
				top:100,
				width:500, 
				height:350,
				minWidth:500,
				minHeight:300,
				zIndex: 90,
				modal: true, 
				title: $("#reCalcTitle").text(),
				close: function(event, ui) {
				$('#reCalcDialog').dialog("destroy");
				$('#reCalcDialog').remove();
				},
				buttons: [{
					id: "confirmBtn",
					text: $("#reCalcConfirm").text(),
				    click: function(){BINOLMBLEL01.execReCalc();}
				},
				{
					text: $("#dreCalcCancel").text(),
					click: function() {
						$('#reCalcDialog').dialog("destroy");
						$('#reCalcDialog').remove();
						
					}
				}]
			};
		$('#reCalcDialog').dialog(dialogSetting);
	},
	/*
	 * 设定会员有效期初始化pop窗口
	 */
	"addMsgInit":function(obj,url,levelId){
		if("" == BINOLMBLEL01.dialogHtml){
			BINOLMBLEL01.dialogHtml = $("#validDateDigMain").html();
		}else{
			$("#validDateDigMain").html(BINOLMBLEL01.dialogHtml);
		}
		$(":radio[name='normalYear']").click(function(){BINOLMBLEL01.selNormalYear(this);});
		var $td = $(obj).parent().parent();
		// 有效期信息
		var $periodvalidity = $td.find("#periodvalidity");
		if ("" != $periodvalidity.val()) {
			if(window.JSON && window.JSON.parse) {
				var validJson = window.JSON.parse($periodvalidity.val());
				// 等级类别
				var levelKbn = validJson.levelKbn;
				if ("1" == levelKbn) {
					$("#levelKbn1").prop("checked",true);
					$("#levelKbn0").prop("checked",false);
				}
				// 开始时间
				var startTimeKbn = validJson.startTimeKbn;
				if ("1" == startTimeKbn) {
					$("#startTimeKbn1").prop("checked",true);
					$("#startTimeKbn0").prop("checked",false);
				}
				// 超出有效期
				var moreDate = validJson.moreDate;
				if ("1" == moreDate) {
					$("#moreDate").prop("checked",true);
				} else {
					$("#moreDate").prop("checked",false);
				}
				// 结束时间
				var normalYear = validJson.normalYear;
				if ("0" == normalYear) {
					$("#memberDate0").val(validJson.memberDate0);
					if ("1" == validJson.kpLevel) {
						$("#kpLevel1").prop("checked",true);
						$("#kpLevelEnd").val(validJson.kpLevelEnd);
					}
					BINOLMBLEL01.showHideSpan("#kpLevel1", "#kpLevelEndSpan");
				} else {
					$("#normalYear0").prop("checked",false);
					$("#normalYear" + normalYear).prop("checked",true);
					if ("3" != normalYear) {
						$("#memberDate" + normalYear).val(validJson["memberDate" + normalYear]);
						$("#endKbn" + normalYear).val(validJson["endKbn" + normalYear]);
					} else {
						$("#moreDate").attr("checked",false);
						$("#moreDate").attr("disabled",true);
					}
				}
				// 是否保级
				var keepLevelKbn = validJson.keepLevelKbn;
				if (keepLevelKbn && "0" != keepLevelKbn) {
					$("#keepLevelKbn0").prop("checked",false);
					$("#keepLevelKbn" + keepLevelKbn).prop("checked",true);
					if ("2" == keepLevelKbn) {
						$("#cardStart").val(validJson.cardStart);
					}
				}
				// 入会途径
				var cls = validJson.cls;
				if (cls) {
					var clsArr = cls.split(",");
					for (var i = 0; i < clsArr.length; i++) {
						$(':input[value="' + clsArr[i] + '"]').prop("checked", true);
					}
				}
			}
		}
		$('#validDateDig').dialog({
			modal: true,width:600, height:600, zIndex: 9999, resizable:false,title:$("#setMsgTitle").text(), 
			buttons: [{
				text: $("#dialogConfirm").text(),
			    click: function(){BINOLMBLEL01.saveMemberDate(obj,url,levelId);}
			},
			{
				text: $("#dialogCancel").text(),
				click: function() {
					$('#validDateDig').dialog( "destroy" );
					$("#validDateDig").remove();
				}
			}],
			close: function(event, ui) { 
			$('#validDateDig').dialog( "destroy" );
			$("#validDateDig").remove(); }
		});
	},
	
	"showHideSpan" : function(obj, sid) {
		if ($(obj).is(":checked")){
			$(sid).show();
		} else {
			$(sid).hide();
			$(sid).find(":input").val('');
		}
	},
	/*
	 * 选择结束时间
	 */
	"selNormalYear" : function (obj){
		if ($(obj).val() == "3") {
			$("#moreDate").attr("checked",false);
			$("#moreDate").attr("disabled",true);
		} else {
			$("#moreDate").attr("disabled",false);
		}
	},
	/*
	 * 设定会员有效期确定事件
	 */
	"saveMemberDate" : function (obj,url,levelId){
		var param = "levelMemberId=" + levelId;
		var p = [];
		var cl = [];
		$("#validDateDig").find(":input").each(function (){
			if (($(this).is(':radio') || $(this).is(':checkbox')) && !$(this).is(':checked')) {
				return true;
			}
			if (this.name == 'chlCd') {
				cl.push($(this).val());
			} else {
				p.push('"'+this.name+'":"'
						+$.trim($(this).val().replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			}
		});
		var minfo = p.toString();
		if (cl.length > 0) {
			minfo += ',"cls":"' + cl.toString() + '"';
		}
		param = param + '&memberInfo={' + minfo + '}'; 
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				if(msg.indexOf('id="actionResultDiv"') == -1 && msg.indexOf('id="fieldErrorDiv"') == -1){
					if(window.JSON && window.JSON.parse) {
						var msgJson = window.JSON.parse(msg);
					    var selVal = msgJson.memberDateFlag;
					    var selLab = msgJson.memberDate;
					    var str = selLab;
					    str = str + $("#text" + selVal).text();
						$(obj).parents("#memberDate").find("span").first().text(str);
						$(obj).parents("#memberDate").find("#periodvalidity").val('{' + minfo + '}');
					   }
					$("#validDateDig").dialog( "destroy" );
					$("#validDateDig").remove();
				}
			}
		});
	},
	//弹出确认框
	"dialogPanel" : function(option) {
		var dialogSetting = {
				dialogInit: option.dialogId,
				text: option.text,
				width: 	500,
				height: 300,
				title: 	option.title,
				cancel: option.cancelTxt,
				cancelEvent: function(){
					removeDialog($(option.dialogId));
				}
			};
		openDialog(dialogSetting);
	},
	"sendLevel" : function(obj) {
		var url = $(obj).attr("href");
		var param = $("#brandInfoId").serialize();
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				if(window.JSON && window.JSON.parse) {
					var msgJson = window.JSON.parse(msg);
					var option = {
							dialogId : "#dialogSendLevel",
							title : "下发等级", 
							cancelTxt : "关闭"
					};
					var ret = msgJson["RESULT"];
					if ("NG01" == ret) {
						option.text = "<p class='message'><span>下发等级发生异常</span></p>";
					} else {
						option.text = "<p class='message'><span>下发等级已正常结束</span></p>";
					}
					BINOLMBLEL01.dialogPanel(option);
				}
			}
		});
	}
};
var BINOLMBLEL01 = new BINOLMBLEL01();
