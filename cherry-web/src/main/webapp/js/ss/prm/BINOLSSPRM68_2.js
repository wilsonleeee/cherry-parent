function BINOLSSPRM68_2(){};
BINOLSSPRM68_2.prototype={
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
		}
		
}
var PRM68_2 = new BINOLSSPRM68_2();