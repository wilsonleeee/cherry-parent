function BINOLSSPRM88_2(){};
BINOLSSPRM88_2.prototype={
	"calEventBind":function (){
		$('#activeTimeInfo .startDate').cherryDate({
			holidayObj: '',
			beforeShow: function(input){
				var value = $(input).parent().parent().next().find(".endDate").val();
				return [value,'maxDate'];
			}
		});
		$('#activeTimeInfo .endDate').cherryDate({
			holidayObj: '',
			beforeShow: function(input){
				var value = $(input).parent().parent().prev().find(".startDate").val();
				return [value,'minDate'];
			}
		});
	},
	"initTime":function(){
		$('#activeTimeInfo .time').timepicker({
			timeFormat: 'HH:mm:ss',
			showSecond: true,
			timeOnlyTitle: '设置时分秒',
			currentText: '当前时间',
			closeText: '关闭',
			hourMax: 23
		});
	},
	"addActiveTime":function(){
		var $target = $('#activeTimeInfo');
		var $timeItem = $('#timeItem');
		$target.append($timeItem.html());
		this.calEventBind();
		$target.find('.close').show();
	},
	"removeTime":function(_this){
		var $target = $('#activeTimeInfo');
		$(_this).parent().remove();
		var $items = $target.children();
		if($items.length == 1){
			$items.find('.close').hide();
		}
	},
	"nextBefore":function(){
		var $timeJson = $('#timeJson');
		var $startTime = $('#startTime');
		var $endTime = $('#endTime');
		var $timeInfo = $('#activeTimeInfo');
		var $times = $timeInfo.children();
		var timeJson = Obj2JSONArr($times);
		$timeJson.val(timeJson);
		$startTime.val($timeInfo.find('input[name="startDate"]').val() + " " + $timeInfo.find('input[name="startTime"]').val());
		$endTime.val($timeInfo.find('input[name="endDate"]').val() + " " + $timeInfo.find('input[name="endTime"]').val());
		var tree = CHERRYTREE.trees[0];
		if(!isEmpty(tree)){
			// 取得选中节点
			var nodes = CHERRYTREE.getTreeNodes(tree,true);
			// 取得选中节点【排除全选节点的子节点】
			var checkedNodes = CHERRYTREE.getCheckedNodes(nodes);
			// 取得叶子节点
			var leafNodes = CHERRYTREE.getLeafNodes(nodes);
			$("#placeJson_0").val(JSON.stringify(checkedNodes));
			$("#saveJson_0").val(JSON.stringify(leafNodes));
		}
		//黑名单
		var counterBlack = new Array();
		$("#tree_1").find('li').each(function() {
			if($(this).find("input").is(":checked")){
				var counter=new Object();
				counter.id=$(this).find("input[name='id']").val();
				counter.name=$(this).find("input[name='name']").val();
				counterBlack.push(counter);
			}
		});
		$("#placeJson_1").val(JSON.stringify(counterBlack));
		$("#saveJson_1").val(JSON.stringify(counterBlack));
		//var tree1 = CHERRYTREE.trees[1];
		//if(!isEmpty(tree1)){
			//// 取得选中节点
			//var nodes1 = CHERRYTREE.getTreeNodes(tree1,true);
			//// 取得选中节点【排除全选节点的子节点】
			//var checkedNodes1 = CHERRYTREE.getCheckedNodes(nodes1);
			//// 取得叶子节点
			//var leafNodes1 = CHERRYTREE.getLeafNodes(nodes1);
			//$("#placeJson_1").val(JSON.stringify(checkedNodes1));
			//$("#saveJson_1").val(JSON.stringify(leafNodes1));
		//}
	},
	// 改变地点类型
	"changeType" : function(_this,index){
		var $this = $(_this);
		// 清空树内容
		CHERRYTREE.trees[index] = null;
		$("#tree_" + index).empty();
		if($this.val() == '5'){// 导入柜台
			$("#importWhiteCounter").show();
			$("#locationId_"+index).hide();
		}else{
			$("#importWhiteCounter").hide();
			CHERRYTREE.searchNodes($this, index);
		}
	},
	//柜台导入页面弹出框
	"popCounterLoadDialog":function(filterType){
		var dialogId = 'counterDivDialog';
		var $dialog = $("#" + dialogId);
		if($dialog.length == 0) {
			$("body").append('<div style="display:none" id="'+dialogId+'"></div>');
		} else {
			$dialog.empty();
		}
		PRM88_2.nextBefore();
		var title;
		var counterList;
		if(filterType==1){
			title = '导入柜台';
			counterList =$("#saveJson_0").val();
		}else if(filterType==2){
			title = '导入排除柜台';
			counterList =$("#saveJson_1").val();
		}
		PRM88_2.nextBefore();
		var url = '/Cherry/ss/BINOLSSPRM88_popLoadCounterInit';
		var param="&filterType="+filterType+"&ExecLoadType=1&counterList="+counterList;
		cherryAjaxRequest({
			url: url,
			param:param,
			callback: function(msg){
				$dialog.html(msg);
				// 弹出验证框
				var dialogSetting = {
					dialogInit: "#" + dialogId,
					text: msg,
					width: 	800,
					height: 400,
					title: title,
					confirm:"确定",
					confirmEvent:function(){
						PRM88_2.counterConfirm(filterType,dialogId);
						removeDialog("#" + dialogId);
					},
					closeEvent: function(){
						removeDialog("#" + dialogId);
					}
				};
				openDialog(dialogSetting);
				$(".ui-dialog-titlebar-close.ui-corner-all").hide();
			}
		});
	},
	"counterConfirm":function(filterType){
		if(filterType == 1){//白名单
			var excelCounter_w_json=$("#excelCounter_w").val();
			if (excelCounter_w_json!=""){
				//var $tree_0 = $("#tree_0");
				//$("#tree_0").empty();
				CHERRYTREE.trees[0] = null;
				$("#tree_0").empty();
				var excelCounter_w_list=eval("("+excelCounter_w_json+")");
				var counterNodes = excelCounter_w_list;
				if(isEmpty(CHERRYTREE.trees[0])){
					// 柜台节点加载到树
					CHERRYTREE.loadTree(counterNodes,0,true);
				}else{
					CHERRYTREE.addNodes(CHERRYTREE.trees[0], counterNodes, true);
				}
				$("#selectAll_0").attr("checked",'true');
			}
		}else{//黑名单
			var excelCounter_b_json=$("#excelCounter_b").val();
			if (excelCounter_b_json!=""){
				var excelCounter_b_list=eval("("+excelCounter_b_json+")");
				//var counterNodes = excelCounter_b_list;
				//if(isEmpty(CHERRYTREE.trees[1])){
				//	// 柜台节点加载到树
				//	CHERRYTREE.loadTree(counterNodes,1,true);
				//}else{
				//	CHERRYTREE.addNodes(CHERRYTREE.trees[1], counterNodes, true);
				//}
				var html="";
				for(var i=0;i<excelCounter_b_list.length;i++){
					html +="<li>&nbsp;&nbsp;&nbsp;";
					html += "<input type='checkbox' checked='checked' onchange='PRM88_2.checkSelectAll(this);'>";
					html += "<button type='button' hidefocus='true'  treenode_ico='' class='ico_docu'></button>";
					html += ""+excelCounter_b_list[i].counterCode+"   （"+excelCounter_b_list[i].counterName+")";
					html += "<input name='name' class='hide' value='"+excelCounter_b_list[i].counterName+"'>";
					html += "<input name='id' class='hide' value='"+excelCounter_b_list[i].counterCode+"'>";
					html +="</li>";
				}
				$("#selectAll_1").attr("checked",'true');
				$("#tree_1").html(html);
			}
		}
	},
	"initBlackCnt":function(){
		var placeJsonb_json=$("#initJson_1").val();
		if (placeJsonb_json!=""){
			$("#selectAll_1").attr("checked",'true');
			var excelCounter_b=eval("("+placeJsonb_json+")");
			var html="";
			for(var i=0;i<excelCounter_b.length;i++){
				html +="<li>&nbsp;&nbsp;&nbsp;";
				html += "<input type='checkbox' checked='checked' onchange='PRM88_2.checkSelectAll(this);'>";
				html += "<button type='button' hidefocus='true'  treenode_ico='' class='ico_docu'></button>";
				html+=excelCounter_b[i].id+"   （"+excelCounter_b[i].name+")";
				html += "<input name='name' class='hide' value='"+excelCounter_b[i].name+"'>";
				html += "<input name='id' class='hide' value='"+excelCounter_b[i].id+"'>";
				html +="</li>";
			}
			$("#tree_1").append(html);
		}
	},
	"checkSelectAll":function(obj){
		//var tree = CHERRYTREE.trees[1];
		//if($(obj).is(":checked")){
		//	if(!isEmpty(tree)){
		//		tree.checkAllNodes(true);
		//	}
		//}else{
		//	if(!isEmpty(tree)){
		//		tree.checkAllNodes(false);
		//	}
		//}
		var $this = $(obj);
		//if ($this.prop("checked")) {
		//	$("#tree_1").find('li').each(function() {
		//		$(this).find("input").prop("checked", true);
		//	});
		//} else {
		//	$("#tree_1").find('li').each(function() {
		//		$(this).find("input").prop("checked", false);
		//	});
		//}
		if($this.attr('id') == "selectAll_1") {
			if ($this.prop("checked")) {
				$("#tree_1").find('li').each(function() {
					$(this).find("input").prop("checked", true);
				});
			} else {
				$("#tree_1").find('li').each(function () {
					$(this).find("input").prop("checked", false);
				});
			}
		} else {
			if($("#tree_1").find(':checkbox:not([checked])').length == 0) {
				$('#selectAll_1').prop("checked",true);
			} else {
				$('#selectAll_1').prop("checked",false);
				//var index =0;
				//$("#tree_1").find('li').each(function() {
				//	if($(this).find("input").is(":checked")){
				//		index++;
				//	}
				//});
				//var selectLength=$("#tree_1 li").length;
				//if (selectLength ==index){
				//	$('#selectAll_1').prop("checked",true);
				//}else{
				//	$('#selectAll_1').prop("checked",false);
				//}
			}

		}
	}

}
var PRM88_2 = new BINOLSSPRM88_2();
