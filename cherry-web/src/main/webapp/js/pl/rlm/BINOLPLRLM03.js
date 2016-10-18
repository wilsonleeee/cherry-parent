$(function() {
	
	// 展开合并按钮事件绑定处理
	$('#dialogInit span.expand').click(function(){
		if($(this).children('.ui-icon').is('.ui-icon-triangle-1-s')) {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
			//$(this).children('.ui-icon').switchClass('ui-icon-triangle-1-s','ui-icon-triangle-1-n');
		} else {
			$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
			//$(this).children('.ui-icon').switchClass('ui-icon-triangle-1-n','ui-icon-triangle-1-s');
		}
		if($(this).parent('.group-header').parent('.group').children('.group-content').is(':hidden')) {
			$(this).parent('.group-header').parent('.group').children('.group-content').show();
		} else {
			$(this).parent('.group-header').parent('.group').children('.group-content').hide();
		}
		if($(this).parent('td').parent('tr').next('.after').is(':hidden')) {
			//$(this).parent('td').parent('tr').addClass('active').next('.after').addClass('active').show();
			$(this).parent('td').parent('tr').next('.after').addClass('active').removeClass('hide');
		} else {
			//$(this).parent('td').parent('tr').removeClass('active').next('.after').removeClass('active').hide();
			$(this).parent('td').parent('tr').next('.after').removeClass('active').addClass('hide');
		}
		return false;
	}); 

	$("#dialogInit").find("#resource_LG").find("#subSysId").prop("checked",true).prop("disabled",true);
	
	// 权限画面所有checkbox事件绑定处理
	$("#dialogInit :checkbox").click(function(){
		var $this = $(this);
		// 子系统checkbox被点击的场合
		if($this.attr("id")=="subSysId") {
			var $subSys = $this.parent().parent();
			if(this.checked) {
				$subSys.find(":checkbox").prop("checked",true);
			} else {
				$subSys.find(":checkbox").prop("checked",false);
			}
		}
		// 模块checkbox被点击的场合
		if($this.attr("id")=="moduleId") {
			var $module = $this.parent().parent();
			if(this.checked) {
				$module.find(":checkbox").prop("checked",true);
			} else {
				$module.find(":checkbox").prop("checked",false);
			}
			checkboxInit(this,"moduleId");
		}
		// 画面checkbox被点击的场合
		if($this.attr("id")=="pageId") {
			var $page = $this.parent().parent().next(".after");
			if(this.checked) {
				$page.find(":checkbox").prop("checked",true);
			} else {
				$page.find(":checkbox").prop("checked",false);
			}
			checkboxInit(this,"pageId");
		}
		// 控件checkbox被点击的场合
		if($this.attr("id")=="controlId") {
			checkboxInit(this,"controlId");
		}
	});
});	

function subSysCheckboxInit($object) {
	var $subSysId = $object.parent().parent().parent().prev().find(":checkbox");
	if($subSysId.val() == 'LG') {
		return;
	}
	if($object.parent().parent().parent().find(":checkbox[checked]").length != 0) {
		$subSysId.prop("checked",true);
	} else {
		$subSysId.prop("checked",false);
	}
}

function moduleCheckboxInit($object) {
	var $moduleId = $object.parent().parent().parent().parent().parent().prev().find(":checkbox");
	if($object.parent().parent().parent().parent().parent().find(":checkbox[checked]").length != 0) {
		$moduleId.prop("checked",true);
	} else {
		$moduleId.prop("checked",false);
	}
	subSysCheckboxInit($moduleId);
}

function pageCheckboxInit($object) {
	var $pageId = $object.parents("tr.after").prev().find(":checkbox");
	if($object.parent().parent().find(":checkbox[checked]").length != 0) {
		$pageId.prop("checked",true);
		moduleCheckboxInit($pageId);
	} else {
		//$pageId.prop("checked",false);
	}
}

function checkboxInit(object,id) {
	if(id == "controlId") {
		pageCheckboxInit($(object));
	}
	if(id == "pageId") {
		moduleCheckboxInit($(object));
	}
	if(id == "moduleId") {
		subSysCheckboxInit($(object));
	}
}

// 权限画面左边菜单事件，控制右边子系统的显示和隐藏
function selectSubSys(object,id) {
	$(object).siblings().removeClass("on");
	$(object).addClass("on");
	if(id == null) {
		$("div.group[id^=resource_]").show();
	} else {
		$("#resource_"+id).siblings().hide();
		$("#resource_"+id).show();
	}
}