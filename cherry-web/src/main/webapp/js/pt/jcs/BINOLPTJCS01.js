var BINOLPTJCS01 = function(){
};
BINOLPTJCS01.prototype = {
	// 添加新行
	"addLine":function (target,source){
		var $target = $(target);
		if($target.find(":text").length !=0){
			return;
		}
		var $source = $(source);
		// 新添加行
		var $newLine = $source.find("tr");
		// 新行所有列
		var $cols2 = $newLine.find("td");
		// 目标行class
		var lastClass ="";
		// 目标最后一行序号
		var num=0;
		// 目标最后一行
		var $lastLine = $target.find("tr:last");
		// 无行
		if($lastLine.length == 0){
			lastClass = "even";
		}else{
			// 目标行class
			lastClass = $lastLine.attr("class");
			// 目标最后一行所有列
			var $cols1 = $lastLine.find("td");
			// 目标最后一行序号
			num = parseInt($cols1.eq(0).html());
		}
		// 新行添加序号
		$cols2.eq(0).html(num + 1);
		// 新行添加Class
		if(lastClass == "odd"){
			$newLine.attr("class","even");
		}else{
			$newLine.attr("class","odd");
		}
		// 目标追加新行
		$target.append($source.html());
	},
	// 删除行
	"delLine":function (_this){
		var $this = $(_this);
		// 目标行
		var $targetLine = $this.parent().parent();
		// 删除目标行
		$targetLine.remove();
	},
	// 移动行
	"move" : function (_this,url,sum,flag){
		var $td = $(_this).parent();
		// 当前行ID
		var LineId = parseInt($td.parent().attr("id"));
		var param = $("#brandInfoId").serialize() + "&moveSeq=" + $td.attr("id") + "&csrftoken=" +$('#csrftoken').val();
		if(flag == 0){			// 移到最前
			param += "&moveSeq=" + $("#1").find("td").eq(4).attr("id");
		}else if(flag == 1){	// 上移
			param += "&moveSeq=" + $("#"+(LineId - 1)).find("td").eq(4).attr("id");
		}else if(flag == 2){	// 下移
			param += "&moveSeq=" + $("#"+(LineId + 1)).find("td").eq(4).attr("id");
		}else if(flag == 3){	// 移到最后
			param += "&moveSeq=" + $("#"+sum).find("td").eq(4).attr("id");
		}
		ajaxRequest(url,param,function(msg){$('#div_main').html(msg);});
	},
	// 编辑分类,分类属性值
	"edit" : function (url,id,lineId,flag){
		var param = "";
		// 需要编辑行
		var $line = $("#" + lineId);
		if($line.parent().find(":text").length !=0){
			return;
		}
		// 编辑分类
		if(flag == null){
			param += "csrftoken=" + $("#csrftoken").val() + "&propId=" + id;
		}else{
			// 编辑分类属性值
			param += "csrftoken=" + parentTokenVal() + "&propValId=" + id;
		}
		// 终端显示是否可操作flag
		if($("#optFlag").length > 0){
			param += "&optFlag=1";
		}
		// 序号
		var num = "<td>" + lineId + "</td>";
		ajaxRequest(url,param,function(msg){$line.html(num + msg);});
	},

	//仅显示停用类别或者显示非停用类别
	"showDisabledOrNot":function(obj){
		var $obj = $(obj);
		var showDisabled = 1;
		if($obj.attr("checked")){
			//这里需要传递showDisabled的属性,
			showDisabled = 0;
		}
		$("#showDisabled").val(showDisabled);
		//这里应该将form提交
		$("#mainForm").submit();
	},
	//停用&启用
	"changeFlag":function(obj,url,validFlag,propValId){
		//根据checkbox状态来断定showDisabled和validFlag
		var $checkbox = $("#checkbox");
		var showDisabled = 1;
		if($checkbox.attr("checked")){
			showDisabled = 0;
		}
		var $line = $(obj).parent().parent();
		var json = this.toJSON($line);
		var param = "json=" + json;
		//要传递showDisabled和validFlag
		param +="&showDisabled=" +showDisabled+ "&validFlag=" +validFlag+"&propValId="+propValId;
		param += "&" + $("#propId").serialize();
		var divId = "#dataTable";
		var title = $('#disableTitle').text();
		var text = $('#disableMessage').html();
		var title_hint,text_hint ;
		if(validFlag == 0){
			//如果要停用的话
			title_hint = $('#disableTitle').text();
			text_hint = $('#disableHint').html();
		}else{
			//如果表示启用的话
			title_hint = $('#enableTitle').text();
			text_hint = $('#enableHint').html();
		}
		//在这里要进行弹框提示
		var dialogConfirmSetting = {
			dialogInit: "#dialogHint",
			text: text_hint,
			width: 	500,
			height: 300,
			title: 	title_hint,
			cancel: $("#dialogCancel").text(),
			cancelEvent: function(){removeDialog("#dialogHint");},
			confirm: $("#dialogConfirm").text(),
			confirmEvent: function(){
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: function(msg) {
						// 返回无错误
						if(msg.indexOf('id="actionResultDiv"') == -1
							&& msg.indexOf('id="fieldErrorDiv"') == -1){
							$(divId).html(msg);
						}else if(msg.indexOf('id="fieldErrorDiv"') > -1){
							//在这里弹框显示 该分类下存在未停用的产品
							var dialogSetting = {
								dialogInit: "#dialogInit",
								text: text,
								width: 	500,
								height: 300,
								title: 	title,
								confirm: $("#dialogConfirm").text(),
								confirmEvent: function(){removeDialog("#dialogInit");}
							};
							openDialog(dialogSetting);
						}
					}
				});
				removeDialog("#dialogHint");
			}
		};
		openDialog(dialogConfirmSetting);
	},
	// 配置分类
	"setting" : function (url){
		var param = $("#brandInfoId").serialize()+ "&csrftoken=" +$('#csrftoken').val();
		url += "&" + param;
		popup(url,{width:'710',height:'550'});
	},
	// 改变品牌
	"search" : function (url){
		var param = $("#brandInfoId").serialize()+ "&csrftoken=" +$('#csrftoken').val();
		ajaxRequest(url,param,function(msg){$('#div_main').html(msg);});
	},
	// 取消分类属性值编辑
	"cancel" : function (url){
		var param = $("#propId").serialize()+ "&csrftoken=" + parentTokenVal();
		ajaxRequest(url,param,function(msg){$("#dataTable").html(msg);});
	},
	// 保存分类,分类属性值
	"save" : function (_this,url,flag){
		// 清除错误信息
		$("#actionResultDisplay").empty();
		$line = $(_this).parent().parent();
		var json = this.toJSON($line);
		var param = "json=" + json;
		var divId = "#div_main";
		// 保存分类
		if(flag == null){
			param += "&" + $("#brandInfoId").serialize();
		}else{
			param += "&" + $("#propId").serialize();
			divId = "#dataTable";
		}
		
		cherryAjaxRequest({
			url: url,
			param: param,
			callback: function(msg) {
				// 返回无错误
				if(msg.indexOf('id="actionResultDiv"') == -1 
						&& msg.indexOf('id="fieldErrorDiv"') == -1){
					$(divId).html(msg);
				}
			}
		});
	},
	// 对象JSON化
	"toJSON" : function (obj){
		var JSON = [];
		$(obj).find(':input').each(function(){
			var $this = $(this);
			if($.trim($this.val()) != '') {
				JSON.push('"'+encodeURIComponent($this.attr("name"))+'":"'
						+encodeURIComponent($.trim($this.val()).replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
			}
		});
		return "{"+JSON.toString()+"}";
	}
};
var BINOLPTJCS01 = new BINOLPTJCS01();
