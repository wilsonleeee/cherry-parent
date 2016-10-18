var TEMP001_dialogBody ="";
var TEMP001_GLOBAL = function () {

};
TEMP001_GLOBAL.prototype = {
		"selPrtIndex" : "",
		"showTable" : function (obj){
				// 加粗选中的label的字体
				TEMP001.boldLabel(obj);
				// 弹出信息框
				openProPopup(obj);
				// 取索引值
				TEMP001.selPrtIndex = $("input[name='selPrtIndex']", $(obj).parent().parent()).val();
				},
		"selectProduct"	 : function (){
				// 取得选择的产品
				var selectedProArr = $('#prt_dataTable input:checked');
				// 添加产品
				for (var i=0;i<selectedProArr.length;i++){
					var flg = true;
					var $selectedPro = $(selectedProArr[i]);
					var selectedProInfoArr = window.JSON2.parse($selectedPro.val());
					// 产品全称
					var selectedProName = selectedProInfoArr.nameTotal;
					var selectedUninCode = selectedProInfoArr.unitCode;
					var selectedBarCode = selectedProInfoArr.barCode;
					// 取得当前已有的产品值
					var hasProArrBarCode = $(':input[name="barCode"]','.showPro_' + TEMP001.selPrtIndex);
					var hasProArrUnitCode = $(':input[name="unitCode"]','.showPro_' + TEMP001.selPrtIndex);
					// 判断该产品是否已经存在
					for(var j=0;j<hasProArrBarCode.length;j++){
						var $hasProBarCode = $(hasProArrBarCode[j]);
						var $hasProUninCode = $(hasProArrUnitCode[j]);
						var hasProInfoBarCode = $hasProBarCode.val();
						var hasProInfoUninCode = $hasProUninCode.val();
						// 通过产品编码和促销品条码判断
						if(selectedBarCode == hasProInfoBarCode && selectedUninCode == hasProInfoUninCode){
							flg = false;
							break;
						}
					}
					// 该产品不存在时，添加产品
					if(flg){
						var span = '<span class="span_BASE000001"><input type="hidden" name="nameTotal" value="'+ selectedProName+'" /><input type="hidden" name="unitCode" value="'+ selectedUninCode+'" /><input type="hidden" name="barCode" value="'+ selectedBarCode+'" /><span>' + selectedProName + 
							'</span> <span class="close" onclick="TEMP001_delete(this);return false;"><span class="ui-icon ui-icon-close"></span></span>' +
							'<span onclick="TEMP001_openTable(this);return false;" class="add"><span class="ui-icon ui-icon-plus"></span></span></span></span>';
						$(".showPro_" + TEMP001.selPrtIndex).append(span);
					}
				}
				$(".selPro").each(function(){
					if($(this).find(".span_BASE000001").length == 1){
						$(this).find('.close' , '.span_BASE000001').hide();
					}else{
						$(this).find('.close' , '.span_BASE000001').show();
					}
				});
				// 关闭弹出框
				closeCherryDialog('productDialog',TEMP001_dialogBody);
				oTableArr[1]= null;	
			},
		"delete" : function (obj){
				// 删除产品
				$(obj).parent().remove();
				$(".selPro").each(function(){
					if($(this).find(".span_BASE000001").length == 1){
						$(this).find('.close' , '.span_BASE000001').hide();
					}
				});
			},
		"openTable" : function (obj) {
				var $obj = $(obj).parents(".proClass").first().find("#choicePro");
				TEMP001.showTable($obj);
			},
		"boldLabel" : function (obj){
				// 遍历所有的label
				if($(obj).find(".box2").length > 0){
					$label = $(obj).find(".box2").find("label");
				}else{
					$label = $(obj).parents(".box2").find("label");
				}
				// 遍历指定框中的label
				$label.each(function(){
					// 提取label所关联的radio
					var radioId = $(this).attr("for");
					var $radio = $("#" + radioId);
					// 关联的radio选中时，label字体加粗
					if ($radio.is(":checked")) {
						$(this).css("font-weight","bold"); 
					}else{
						$(this).css("font-weight","normal"); 
					}
				});
			},
		// 显示或隐藏规则详细信息
		"showMore" : function (obj,parent,children,flag){
				if(flag == 1){
					// 是否设置规则
					if ($(obj).is(":checked")) {
						$(obj).parents("." + parent).first().find("." + children).show();
						$(obj).parents("." + parent).first().find(".line").show();
					} else {
						$(obj).parents("." + parent).first().find("." + children).hide();
						$(obj).parents("." + parent).first().find(".line").hide();
					}
				}else{
					// 显示或隐藏规则
					if($(obj).children("span").hasClass("ui-icon ui-icon-triangle-1-n"))
					{
						// 查找该对象父节点下面对应的子节点
						$(obj).parents("." + parent).first().find("." + children).hide();
						$(obj).children("span").removeClass("ui-icon-triangle-1-n").addClass("ui-icon-triangle-1-s");
						// 当隐藏整个升级或降级框时，隐藏线
						if(parent == "box4"){
							$(obj).parents("." + parent).first().find(".line").hide();
						}
					}else{
						$(obj).parents("." + parent).first().find("." + children).show();
						// 设置按钮样式
						$(obj).children("span").removeClass("ui-icon-triangle-1-s").addClass("ui-icon-triangle-1-n");
						if(parent == "box4"){
							$(obj).parents("." + parent).first().find(".line").show();
						}
					}
				}
			},
		// 添加规则
		"addRule" : function (obj,divId){
			CAMPAIGN_TEMPLATE.changeIndexTempCopy("#add_" + divId);
			$("#" + divId).find(".box4-content").first().append($("#add_" + divId).html());
			CAMPAIGN_TEMPLATE.changeIndexTempCopy("#add_" + divId);
			$(obj).parents(".box4").find(".deleteClass").show();
			var size = $("#TEMPCOPY-SIZE-" + divId).val();
			if (size) {
				var sizeInt = parseInt(size);
				sizeInt++;
				$("#TEMPCOPY-SIZE-" + divId).val(sizeInt);
			}
		},
		
		// 删除规则
		"deleteRule" : function (obj,parent){
			$parent = $(obj).parents("." + parent).first();
			if ($parent.prev(".line").length == 0) {
				if($parent.nextAll(".line").length == 0){
					$parent.parent().nextAll(".line").first().remove();
				}else{
					$parent.nextAll(".line").first().remove();
				}
			} else {
				$parent.prev(".line").remove();
			}
			$box = $(obj).parents(".box4").first();
			if($box.find("." + parent).length == 4){
				$box.find("." + parent).find(".deleteClass").hide();
			}
			$parent.remove();
			var id = $box.attr("id");
			var size = $("#TEMPCOPY-SIZE-" + id).val();
			if (size) {
				var sizeInt = parseInt(size);
				sizeInt--;
				$("#TEMPCOPY-SIZE-" + id).val(sizeInt);
			}
			if($(obj).parents(".box").find(".box4-content").length == 1){
				$(obj).parents(".box4").find(".deleteClass").hide();
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
			}
};

var TEMP001 = new TEMP001_GLOBAL();

function openProPopup(_this){
	var proPopParam = {
			thisObj : _this,
			  index : 1,
		  checkType : "checkbox",
		      modal : false,
		  autoClose : [],
		  dialogBody : TEMP001_dialogBody,
		  csrftoken : getParentToken()
	};
	popDataTableOfPrtInfo(proPopParam);
}

function TEMP001_delete(obj){
	// 删除产品
	$(obj).parent().remove();
	$(".selPro").each(function(){
		if($(this).find(".span_BASE000001").length == 1){
			$(this).find('.close' , '.span_BASE000001').hide();
		}
	});
}

function TEMP001_openTable(obj) {
	var $obj = $(obj).parents(".proClass").first().find("#choicePro");
	TEMP001.showTable($obj);
}
$(document).ready(function() {
	TEMP001.boldLabel("#mainForm");
	// 产品popup初始化
	TEMP001_dialogBody = $('#productDialog').html();
	if($(".box4-content [name='deleteUp']").not($(".box4-content [name='deleteUp']", ".no_submit")).length == 1){
		$(".box4-content [name='deleteUp']").not($(".box4-content [name='deleteUp']", ".no_submit")).hide();
	}
	if($(".box4-content [name='deleteDown']").not($(".box4-content [name='deleteDown']", ".no_submit")).length == 1){
		$(".box4-content [name='deleteDown']").not($(".box4-content [name='deleteDown']", ".no_submit")).hide();
	}
	$(".selPro").each(function(){
		if($(this).find(".span_BASE000001").length == 1){
			$(this).find('.close' , '.span_BASE000001').hide();
		}
	});
});