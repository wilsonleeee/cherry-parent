$(function () {
	
	// 选择角色checkbox事件绑定
	$("#roleInfoList :checkbox").each(function(){
		$(this).click(function(){
			if(this.id=="roleIdAll") {
				if(this.checked) {
					$("input@[id=roleId]").prop("checked",true);
				} else {
					$("input@[id=roleId]").prop("checked",false);
				}
			} else {
				if(this.checked) {
					if($("input@[id=roleId]").length == $("input@[id=roleId][checked]").length) {
						$("input@[id=roleIdAll]").prop("checked",true);
					}
				} else {
					$("input@[id=roleIdAll]").prop("checked",false);
				}
			}
		});
	});
	
	// 选择角色checkbox初期化处理
	if($("input@[id=roleId]").length != 0 && $("input@[id=roleId]").length == $("input@[id=roleId][checked]").length) {
		$("input@[id=roleIdAll]").prop("checked",true);
	}

	// 有error时，error初期化处理
	$("#roleInfoList").find(".icon-error").each(function(){
		$(this).parent().addClass("error");
		$(this).attr("id","errorText");
		$(this).attr("title","error|"+$(this).text());
		$(this).cluetip({
		      splitTitle: '|',
			  width: 150,
			  tracking: true,
			  cluetipClass: 'error', 
			  arrows: true, 
			  dropShadow: false,
			  hoverIntent: false,
			  sticky: false,
			  mouseOutClose: true,
			  closePosition: 'title',
			  closeText: '<span class="ui-icon ui-icon-close"></span>'
		});
	});

	
});