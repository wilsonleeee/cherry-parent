var TEMP001_dialogBody ="";
var TEMP001_GLOBAL = function () {

};
TEMP001_GLOBAL.prototype = {
		"extendOption" : {
			/*
			 * 购买产品信息
			 */
			"BASE000001_INFO" : function(id){
				var m = [];
				var n = [];
				var $id = $("#" + id);
				// 取得除产品信息以外的数据
				$id.find(':input').not($(":input", "#prtSel_" + id)).each(function(){
					if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
						if(this.value != '') {
							var name = this.name;
							if (0 == name.indexOf("prtRadio")) {
								name = "prtRadio";
							}
							m.push('"'+name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				// 把产品信息转化为list
				$("#prtSel_" + id).find(".span_BASE000001").each(function (){
					var p = [];
					$(this).find(':input').each(function (){
						p.push('"'+ this.name +'":"'+
								$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
					});
					n.push("{" + p.toString() + "}");
				});
				m.push('"productList":[' + n.toString() + "]");
				return m.toString();
			},
			
			/*
			 * 累积金额
			 */
			"BASE000003_INFO" : function(id){
				var m = [];
				var $id = $("#" + id);
				// 取得除产品信息以外的数据
				$id.find(':input').each(function(){
					if ((!$(this).is(":checkbox") && !$(this).is(":radio")) || $(this).is(":radio[checked]") || $(this).is(":checkbox[checked]")) {
						if(this.value != '') {
							var name = this.name;
							if (0 == name.indexOf("plusTime")) {
								name = "plusTime";
							}else if (0 == name.indexOf("plusChoice")) {
								name = "plusChoice";
							}else if (0 == name.indexOf("plusfirstBillSel")) {
								name = "plusfirstBillSel";
							}
							m.push('"'+name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				return m.toString();
			},
			/*
			 * 降级设置信息
			 */
			"BASE000011_INFO" : function(id){
				var m = [];
				var $id = $("#" + id);
				if($id.parents(".no_submit").length > 0) {
					return null;
				}
				$id.find(':input').not(".no_submit").each(function(){
					if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
						if(this.value != '') {
							var name = this.name;
							if (0 == name.indexOf("downLevel")) {
								name = "downLevel";
							}else if (0 == name.indexOf("plusfirstDownSel")) {
								name = "plusfirstDownSel";
							}
							m.push('"'+name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				return m.toString();
			},
			
			/*
			 * 失效设置信息
			 */
			"BASE000013_INFO" : function(id){
				var m = [];
				var $id = $("#" + id);
				$id.find(':input').each(function(){
					if (!$(this).is(":radio") || $(this).is(":radio[checked]")) {
						if(this.value != '') {
							var name = this.name;
							if (0 == name.indexOf("loseLevel")) {
								name = "loseLevel";
							}
							m.push('"'+name+'":"'+
									$.trim(this.value.replace(/\\/g,'\\\\').replace(/"/g,'\\"'))+'"');
						}
					}
				});
				return m.toString();
			},
			/*
			 * 等级和有效期信息
			 */
			"BASE000010_INFO" : function(id){
				// 会员等级ID
				$("#ruleMemberLevelId").val($("#memberLevelId_" + id).val());
				if ($("#check_isCounter").is(":checked")) {
					var trees = CHERRYTREE.trees;
					// 取得选中节点
					for(var i = 0; i < trees.length; i++){
						if(!isEmpty(trees[i])){
							// 取得选中节点
							var nodes = CHERRYTREE.getTreeNodes(trees[i],true);
							// 取得选中节点【排除全选节点的子节点】
							var checkedNodes = CHERRYTREE.getCheckedNodes(nodes);
							// 取得叶子节点
							var leafNodes = CHERRYTREE.getLeafNodes(nodes);
							$("#placeJson_" + i).val(JSON.stringify(checkedNodes));
							$("#saveJson_" + i).val(JSON.stringify(leafNodes));
						}
					}
				} else {
					$("#locationType_0").val("");
					CHERRYTREE.changeType("#locationType_0",0);
				}
				return CAMPAIGN_TEMPLATE.BASE_INFO(id);
			},

			/*
			 * 单次购买信息
			 */
			"BUS000001_INFO" : function(id) {
				var busInfo;
				var combTemps = [];
				var $id = $("#" + id);
				if ($("#check_" + id).is(":checked")) {
					busInfo = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				}
				return busInfo;
				
			},
			
			/*
			 * 累积购买信息
			 */
			"BUS000002_INFO" : function(id) {
				var busInfo;
				var combTemps = [];
				var $id = $("#" + id);
				if ($("#check_" + id).is(":checked")) {
					busInfo = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				}
				return busInfo;
				
			},
			
			/*
			 *  升级信息
			 */
			"BUS000006_INFO" : function(id) {
				var busInfo;
				var combTemps = [];
				var $id = $("#" + id);
				if ($("#check_" + id).is(":checked")) {
					busInfo = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				}
				return busInfo;
				
			},
			
			/*
			 * 降级信息
			 */
			"BUS000010_INFO" : function(id) {
				var busInfo;
				var combTemps = [];
				var $id = $("#" + id);
				if ($("#check_" + id).is(":checked")) {
					busInfo = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				}
				return busInfo;
				
			},
			
			/*
			 * 失效信息
			 */
			"BUS000012_INFO" : function(id) {
				var busInfo;
				var combTemps = [];
				var $id = $("#" + id);
				if ($("#check_" + id).is(":checked")) {
					busInfo = eval("CAMPAIGN_TEMPLATE.BUS_INFO('" + id + "')");
				}
				return busInfo;
				
			}
		},
		
		"extendRules" : {// 消费金额
			"BASE000002" : {minAmount: {floatValid: [10,2]},		// 最低消费
			maxAmount: {floatValid: [10,2]}		// 最高消费
			},
			// 累积时间 
			"BASE000003" : {monthNum: {digits: true, maxlength: 3},		// 月数
							tsMonth: {digits: true, range:[1,12]},	
							tsDay: {digits: true, range:[1,31]},
							bstime: {digits: true, range:[1,999]},
							betime: {digits: true, range:[1,999]}
						},
			"BASE000011" : {
				tsdMonth: {digits: true, range:[1,999]},
				tsdDay: {digits: true, range:[1,999]}
				
			},
						
			// 等级和有效期
			"BASE000010" : {fromDate: {dateValid: true},		// 开始日期
						toDate: {dateValid: true}           // 结束日期
						}
		},
		"selPrtIndex" : "",
		"showTable" : function (obj){
				// 加粗选中的label的字体
				TEMP001.boldLabel(obj);
				// 取索引值
				TEMP001.selPrtIndex = $("input[name='selPrtIndex']", $(obj).parent().parent()).val();
				// 弹出信息框
				CAMPAIGN_TEMPLATE.openProPopup(obj, 0, TEMP001.selPrtIndex);
				},
		"setChecked" : function (obj, index){
					// 累积金额中选择自定义规则时默认选中第一个时间段
					var radioCheck = $('input[name="plusTime_' + index + '"]:checked', $(obj).parents(".box2")).length;
					if($(obj).val() == 1){
						if (radioCheck == 0) {
							$("#monthFlag_" + index).prop("checked",true);
						}
					} else {
						if (radioCheck > 0) {
							$('input[name="plusTime_' + index + '"]:checked', $(obj).parents(".box2")).prop("checked",false);
						}
					}
				},
		"boldLabel" : function (obj){
				// 遍历所有的label
				if($(obj).find(".box2").length > 0){
					$label = $(obj).find(".box2").find("label");
				}else{
					$label = $(obj).parents(".box2").find("label");
				}
				// 遍历指定框中的label
				$label.not($(":input[name^='plus']").next()).each(function(){
					// 提取label所关联的radio
					var radioId = $(this).attr("for");
					var $radio = $("#" + radioId);
					// 关联的radio选中时，label字体加粗
					if ($radio.is(":checked")) {
						$(this).css("font-weight","bold"); 
					}else{
						$(this).css("font-weight","normal"); 
					}
					if($(this).val() == 1){
						$(this).attr("class", "bg_yew"); 
					}
				});
			},
		"showSelSpan" : function(obj, prefix) {
			var val = $(obj).val();
			var $hideSpan = $(obj).parent().find("span[id^='" + prefix + "']");
			$hideSpan.find(":input").val('');
			$hideSpan.hide();
			var $showSpan = $(obj).parent().find("#" + prefix + val);
			if ($showSpan.length > 0) {
				$showSpan.show();
			}
		},
		// 显示或隐藏规则详细信息
		"showMore" : function (obj,parent,children,flag){
				if(flag == 1){
					// 是否设置规则
					if ($(obj).is(":checked")) {
						$(obj).parents("." + parent).first().find("." + children).first().show();
						$(obj).parents("." + parent).first().find(".line").show();
					} else {
						$(obj).parents("." + parent).first().find("." + children).first().hide();
						$(obj).parents("." + parent).first().find(".line").hide();
					}
				}else{
					// 显示或隐藏规则
					if($(obj).children("span").hasClass("ui-icon ui-icon-triangle-1-n"))
					{
						// 查找该对象父节点下面对应的子节点
						$(obj).parents("." + parent).first().find("." + children).first().hide();
						$(obj).children("span").removeClass("ui-icon-triangle-1-n").addClass("ui-icon-triangle-1-s");
						// 当隐藏整个升级或降级框时，隐藏线
						if(parent == "box4"){
							$(obj).parents("." + parent).first().find(".line").hide();
						}
					}else{
						$(obj).parents("." + parent).first().find("." + children).first().show();
						// 设置按钮样式
						$(obj).children("span").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-n");
						if(parent == "box4"){
							$(obj).parents("." + parent).first().find(".line").show();
						}
					}
				}
			},
					
			
	/* **************************************** 等级和有效期   start **************************************** */
		/* 
		 * 根据所选等级查询等级有效期
		 * 
		 * Inputs:  Object:obj 			选中的会员等级
		 * 
		 */
		"changeDate" : function (obj) {
				var url = $("#queryLevelDateUrl").attr("href");
				var param = $(obj).serialize() + "&" + $("#brandInfoId").serialize();
				if ($("#memberClubId").length > 0) {
					param += '&' + $("#memberClubId").serialize();
				}
				cherryAjaxRequest({
					url: url,
					param: param,
					callback: function(msg) {
						if(msg && window.JSON && window.JSON.parse) {
							var msgJson = window.JSON.parse(msg);
							if (msgJson) {
								var id = $(obj).attr("id");
								if (id) {
									var suffix = id.replace("memberLevelId_", "");
									// 默认开始日期
									$fromDate = $("#campFromDate_" + suffix);
									// 默认结束日期
									$toDate = $("#campToDate_" + suffix);
									$fromDate.val(msgJson.defFromDate);
									$toDate.val(msgJson.toDate);
								}
							}
						}
					}
				});
			},
			"showYearLimit": function(obj, index) {
				var $sp = $("#ylzSpan_" + index);
				if ($(obj).is(":checked")) {
					$sp.show();
				} else {
					$sp.hide();
				}
			}
};
var TEMP001 = new TEMP001_GLOBAL();
CAMPAIGN_TEMPLATE.addOption(TEMP001.extendOption);
CAMPAIGN_TEMPLATE.addTempRules(TEMP001.extendRules);

$(document).ready(function() {
	TEMP001.boldLabel("#mainForm");
	// 产品popup初始化
	TEMP001_dialogBody = $('#productDialog').html();
	if($("[id^='deleteUp']").not($("[id^='deleteUp']", ".no_submit")).length == 1){
		$("[id^='deleteUp']").not($("[id^='deleteUp']", ".no_submit")).hide();
	}
	if($("[id^='deleteDown']").not($("[id^='deleteDown']", ".no_submit")).length == 1){
		$("[id^='deleteDown']").not($("[id^='deleteDown']", ".no_submit")).hide();
	}
	$(".selPro").each(function(){
		if($(this).find(".span_BASE000001").length == 1){
			$(this).find('.close' , '.span_BASE000001').hide();
		}
	});
});
