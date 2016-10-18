function BINOLPTJCS14(){};

var BINOLPTJCS14_global = {};
//是否需要解锁
BINOLPTJCS14_global.needUnlock = true;

window.onbeforeunload = function(){
	if (BINOLPTJCS14_global.needUnlock) {
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

BINOLPTJCS14.prototype={
		
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
	// 维护产品价格方案
	"addPrtSolu" : function (url) {
		// 清空提示消息
		this.cleanMsgDIV();
		
		var param = $('#mainForm').find(':input[name=csrftoken]').serialize();
		if($("#categoryTree").find('a.jstree-clicked').length > 0) {
			param += "&path=" + $("#categoryTree").find('a.jstree-clicked').parent().attr('id');
		}
		url = url + '?' + param;
		popup(url);
	},
	/**
	 * 清空提示消息
	 */
	"cleanMsgDIV" : function(){
		// 清空消息
		$("#msgDIV").empty();
		// 清除方案错误提示信息
		$('#productPriceSolutionID').parent().removeClass('error');
		$('#productPriceSolutionID').parent().find('#errorText').remove();
	},
	/**
	 * 添加保存
	 */
	"addSave":function(){
//		alert(JSON.stringify(ptjcs14_currnetClick));
		
//		// 清空消息
		this.cleanMsgDIV();
		
		var soluStartDate = "";
		var soluEndDate = "";
		
		// 保存后，将产品节点勾选
		var treeObj = $.fn.zTree.getZTreeObj("tree_0");
		var $productPriceSolutionID = $("#productPriceSolutionID");
		if($productPriceSolutionID.val()){
			treeObj.checkNode(ptjcs14_currnetClick, true, true);
			
			// 将当前方案的生效区间存入变量
			var list = JSON.parse($("#prtPriceSolutionListId").val());
			$.each(list, function(i,item){
				if($productPriceSolutionID.val() == item.solutionID){
					// 生效开始日期
					if(item.startDate){
						soluStartDate = item.startDate;
					}
					// 生效结束日期
					if(item.endDate){
						soluEndDate = item.endDate;
					}
				}
			});
			
		} else {
			treeObj.checkNode(ptjcs14_currnetClick, false, true);
		}
		
		var param;
		var tree = ptjcs14Tree.trees[0];
		if(!isEmpty(tree)){
//				// 取得选中节点
			var nodes = ptjcs14Tree.getTreeNodes(tree,true);
			// 取得选中节点【排除全选节点的子节点】
			var checkedNodes = ptjcs14Tree.getCheckedNodes(nodes);
			// 取得叶子节点
			var leafNodes = ptjcs14Tree.getLeafNodes(nodes);
			$("#placeJson_0").val(JSON.stringify(checkedNodes));
			$("#saveJson_0").val(JSON.stringify(leafNodes));
//				
		}
		
		param = $("#mainForm").serialize();
		
		param += "&startTime="+soluStartDate +"&endTime="+soluEndDate;
		
		var parentToken = getParentToken();
		param += "&" + parentToken;
		
		cherryAjaxRequest({
			url: $("#addSaveUrlId").val(),
			param: param,
			callback: function(data){
				if(data.indexOf('fieldErrorDiv') < 0){
					var map = JSON.parse(data);
					if(map.result != '0'){
						// 显示结果信息
						var ht = $("#actionSuccessId").clone();
						$("#msgDIV").html('').append(ht);
//						alert("应用成功");
						// 加载新的saveJson
						$("#dbSaveJson").val(map.saveJson);
						ptjcs14Tree.setCntNode();
						
						// 成功后，刷新当前节点初始勾选状态
						if($productPriceSolutionID.val()){
							ptjcs14_currnetClickCheckedOld = true;
						} else {
							ptjcs14_currnetClickCheckedOld = false;
						}
						
					} else {
						// 显示结果信息
						var ht= $("#actionFaildId").clone();
						$("#msgDIV").html('').append(ht);
//						alert("应用失败");
						
						// 失败后，恢复至初始化勾选状态
						treeObj.checkNode(ptjcs14_currnetClick, ptjcs14_currnetClickCheckedOld, true);
					}
					
				}
			},
			coverId : "#pageButton"
		});
		
	},
	"getPrtPriceSoluList" : function(){
		var $productPriceSolutionID = $("#productPriceSolutionID");
		
		var key = $productPriceSolutionID.val();
		
		cherryAjaxRequest({
			url: $("#getPrtPriceSoluListId").val(),
			callback: function(data){
				var list = JSON.parse(data);
				if(list != '0'){
					$productPriceSolutionID.empty();
					var headOption = '<option value="">'+$("#select_defaultId").text()+'</option>';
					$productPriceSolutionID.append(headOption);
					$.each(list, function(i,item){
						// 将新添加的方案放到select上
						var htmlTmp = '<option value="'+item.solutionID +'">'+item.solutionNameDesc+'</option>';
						$productPriceSolutionID.append(htmlTmp);
						
						if(key == item.solutionID){
							// 生效开始日期
							if(item.startDate){
								$("#soluStartDateId").text(item.startDate);
							}
							// 生效结束日期
							if(item.endDate){
								$("#soluEndDateId").text(item.endDate);
								soluEndTimeVal = item.endDate;
							}
							
							// 过期方案 时间区间显示红色
							var businessDateVal = $('#businessDateId').val();
							var bol = JCS14.compareDate(soluEndTimeVal,businessDateVal);
							if(bol){
								$("#soluStartDateId").parent().removeClass('red');
								$("#soluStartDateId").parent().addClass('green');
								
								$("#soluEndDateId").parent().removeClass('red');
								$("#soluEndDateId").parent().addClass('green');
							}else{
								$("#soluStartDateId").parent().removeClass('green');
								$("#soluStartDateId").parent().addClass('red');
								
								$("#soluEndDateId").parent().removeClass('green');
								$("#soluEndDateId").parent().addClass('red');
								
							}
						}
						
					});
				} else {
					// 显示结果信息
				}
				
				$('#productPriceSolutionID').val(key);	
				
				// 存放最新的所有方案
				$("#prtPriceSolutionListId").val(data);
			}
		});
		
	},
	/**
	 * 比较日期
	 */
	"compareDate" : function (date1,date2){
		var d1=new Date(date1.replace("-", "/").replace("-", "/"));
		var d2=new Date(date2.replace("-", "/").replace("-", "/"));
		if(d1 >= d2){
			return true;
		}
		return false;
	},
	/**
	 * 显示方案生效区间
	 */
	"disSoluDate" : function(soluIdVal){
		var soluEndTimeVal = '';
		
		var $soluDateId = $("#soluDateId");
		
		if(soluIdVal == ''){
			// 隐藏
			$soluDateId.removeClass('show');
			if(!$soluDateId.hasClass('hide')){
				$soluDateId.addClass('hide');
			}
		}else {
			// 显示
			$soluDateId.removeClass('hide');
			if(!$soluDateId.hasClass('show')){
				$soluDateId.addClass('show');
			}
			
			var list = JSON.parse($("#prtPriceSolutionListId").val());
			$.each(list, function(i,item){
//				alert(JSON.stringify(item));
				if(soluIdVal == item.solutionID){
					// 生效开始日期
					if(item.startDate){
						$("#soluStartDateId").text(item.startDate);
					}
					// 生效结束日期
					if(item.endDate){
						$("#soluEndDateId").text(item.endDate);
						soluEndTimeVal = item.endDate;
					}
				}
			});
			
			// 过期方案 时间区间显示红色
			var businessDateVal = $('#businessDateId').val();
			var bol = this.compareDate(soluEndTimeVal,businessDateVal);
			if(bol){
				$("#soluStartDateId").parent().removeClass('red');
				$("#soluStartDateId").parent().addClass('green');
				
				$("#soluEndDateId").parent().removeClass('red');
				$("#soluEndDateId").parent().addClass('green');
			}else{
				$("#soluStartDateId").parent().removeClass('green');
				$("#soluStartDateId").parent().addClass('red');
				
				$("#soluEndDateId").parent().removeClass('green');
				$("#soluEndDateId").parent().addClass('red');
				
			}
		}
	},
	"changeSolu" : function(_this){
		// 清空提示消息
		this.cleanMsgDIV();
		
		var $this = $(_this);
		
		// 清除错误提示信息
		$this.parent().removeClass('error');
		$this.parent().find('#errorText').remove();
		
		JCS14.disSoluDate($this.val());
		
		// 当方案选项值为“请选择”时，显示提示信息
		if($this.val() != ''){
			$("#clearSaveId").hide();
		}else {
			$("#clearSaveId").show();
		}
		

	}
		
		
}
var JCS14 = new BINOLPTJCS14();

