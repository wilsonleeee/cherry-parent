var BINOLPTJCS00_1 = function() {
};
BINOLPTJCS00_1.prototype = {
	// 对象JSON化
	"toJSON" : function(obj) {
		var JSON = [];
		$(obj).find(':input').each(
				function() {
					$this = $(this);
					if (($this.attr("type") == "radio" && this.checked)
							|| $this.attr("type") != "radio") {
						if ($.trim($this.val()) != '') {
							var name = $this.attr("name");
							if (name.indexOf("_") != -1) {
								name = name.split("_")[0];
							}
							JSON.push('"'
									+ encodeURIComponent(name)
									+ '":"'
									+ encodeURIComponent($.trim($this.val())
											.replace(/\\/g, '\\\\').replace(
													/"/g, '\\"')) + '"');
						}
					}
				});
		return "{" + JSON.toString() + "}";
	},
	// 对象JSON数组化
	"toJSONArr" : function($obj) {
		var that = this;
		var JSONArr = [];
		$obj.each(function() {
			JSONArr.push(that.toJSON(this));
		});
		return "[" + JSONArr.toString() + "]";
	},
	// 取得参数
	"getParams" : function() {
		// 参数(品牌信息ID)
		var params = $("#brandSel").serialize();
		var parentToken = getParentToken();
		params += "&" + parentToken;
		return params;
	},
	// 添加DIV
	"addDivRow" : function(id, rowId) {
		$(id).append($(rowId).html());
	},

	// 显示/隐藏扩展属性DIV
	"showExtInfo" : function(_this, text1, text2) {
		var $extInfo = $("#extInfo");
		var $this = $(_this);
		var $button = $this.find("span.button-text");
		if ($extInfo.find(":visible").length > 0) {
			$extInfo.hide();
			$button.text(text1);
			$("#prtExtLab").hide();
		} else {
			$extInfo.show();
			$button.text(text2);
			$("#prtExtLab").show();
		}
	},
	// 大小分类绑定处理
	"bind" : function(_this, url, teminalFlag) {
		// 要替换的分类行
		var $line = null;
		var propValId = $(_this).val();
		var param = getParentToken() + "&propValId=" + propValId;
		if (teminalFlag == 1) {
			$line = $("#cate_2");
			param += "&teminalFlag=2";
		} else {
			$line = $("#cate_1");
			param += "&teminalFlag=1";
		}
		if ($line.length == 0) {
			return;
		}
		// 要替换的分类ID
		var propId = $line.find("input:first").attr("name").split("_")[1];
		param += "&propId=" + propId;
		// 取得分类list
		ajaxRequest(url, param, function(msg) {
			if (teminalFlag == 2) {
				var str = $.param($(":radio:checked", msg));
				var val = str.substring(str.lastIndexOf("=") + 1);
				if (val == "") {
					return;
				}
			}
			$line.html(msg);
		});
	},
	// 显示更多产品分类
	"cateVisible": function(text1, text2){
		var cateVisibleVal = $("#cateVisible").val();
		if(cateVisibleVal == 0){
			// 显示更多产品分类
			$("#cateVisible").val(1);
			$("#cateInfo .detail .teminalTransOth").show();
			
			$("#cateVisibleBtn").text(text2);
		} else {
			// 隐藏更多产品分类
			$("#cateVisible").val(0);
			$("#cateInfo .detail .teminalTransOth").hide();
			
			$("#cateVisibleBtn").text(text1);
		}
	},
	// 关闭弹出框
	"closeDialog":function(_this){
		var $rateDiv = $(_this).parent();
		$rateDiv.hide();
	},
	// 删除分类
	"removeCate":function(_this){
		// 分类块
		var $obj = $(_this).parent().parent().parent();
		$obj.remove();
	},
	"openCateDialog" : function(propId,_this,checkType){
		checkType=typeof(checkType)=="undefined" || checkType=="" || checkType==null ? "radio":"checkbox";
		var $this = $(_this);
		var cateInfo = "[{}]";
		var $trs = $this.parent().parent().parent().siblings();
		cateInfo = Obj2JSONArr($trs);
		var option = {
         	   targetId: "prtCate_" + propId,
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
		       			html += '<span class="close" style="margin: 4px 0 0 6px;" onclick="BINOLPTJCS00_1.removeCate(this);return false;"><span class="ui-icon ui-icon-close"></span></span>';
  		       			html += '<input type="hidden" name="cateValId" value="' + info.cateValId + '"/>';
  		       			html += '<input type="hidden" name="propValId" value="' + info.cateValId + '"/>';
  		       			html += '</span></td></tr>';
 				return html;
 	           }
 	        };
 		popAjaxPrtCateDialog(option);
	}
};
//选中已选产品
function setCheckbox(){
	var $bomBody = $("#bomBody");
	var $prtVendorId = $("#barCode").find("input[name='prtVendorId']");
	var prtVendorId = null;
	if($prtVendorId.length == 1){
		prtVendorId = $prtVendorId.val();
	}
	var $prtSelected = $bomBody.find(':input[name=subPrtVendorId]');
	$("#dataTableBody").find(":input").each(function(){
		var $checkbox = $(this);
		var selectedObj = window.JSON2.parse($checkbox.val());
		var selectedPrtVendorId = selectedObj.productVendorId;
		// 不能把自己作为自己的BOM组件
		if(prtVendorId == selectedPrtVendorId){
			$checkbox.prop("disabled",true);
		}
		$prtSelected.each(function(){
			var $this = $(this);
			if($this.val() == selectedPrtVendorId){
				$checkbox.prop("checked",true);
				return false;
			}
		});
	});
}
// 产品pop框checkbox事件绑定
function checkboxBind() {
	$("#dataTableBody").find(":input").click(function() {
		var $thisCheckbox = $(this);
		var $bomBody = $("#bomBody");
		var $delButton = $("#delButton");
		var selectedObj = window.JSON2.parse($thisCheckbox.val());
		if (this.checked) {
			var html = '<tr><td>'
					+ selectedObj.unitCode
					+ '<input type="hidden" name="subPrtVendorId" value="'
					+ selectedObj.productVendorId
					+ '"/>'
					+ '</td><td>'
					+ selectedObj.barCode
					+ '</td><td>'
					+ selectedObj.nameTotal
					+ '</td><td><span><input name="bomPrice" class="price" value="'
					+ parseFloat(selectedObj.salePrice).toFixed("2")
					+ '"/></span></td><td><span><input name="bomQuantity" class="number" value="1"/></span></td><td class="center">'
					+ $delButton.html() + '</td></tr>';
			$bomBody.append(html);
		} else {
			$bomBody.find('tr').each(
					function() {
						if (selectedObj.productVendorId == $(this).find(
								':input[name=subPrtVendorId]').val()) {
							$(this).remove();
							return false;
						}
					});
		}
	});
}
var BINOLPTJCS00_1 = new BINOLPTJCS00_1();