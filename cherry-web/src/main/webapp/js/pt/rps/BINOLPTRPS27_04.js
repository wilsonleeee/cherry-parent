//绑定展开隐藏事件
$('#popParameter span.expand').click(function(){
	if($(this).children('.ui-icon').is('.ui-icon-triangle-1-s')) {
		$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-s').addClass('ui-icon-triangle-1-n');
	} else {
		$(this).children('.ui-icon').removeClass('ui-icon-triangle-1-n').addClass('ui-icon-triangle-1-s');
	}
	if($(this).parent('.group-header').parent('.group').children('.group-content').is(':hidden')) {
		$(this).parent('.group-header').parent('.group').children('.group-content').show();
	} else {
		$(this).parent('.group-header').parent('.group').children('.group-content').hide();
	}
	if($(this).parent('td').parent('tr').next('.after').is(':hidden')) {
		$(this).parent('td').parent('tr').next('.after').addClass('active').removeClass('hide');
	} else {
		$(this).parent('td').parent('tr').next('.after').removeClass('active').addClass('hide');
	}
	return false;
}); 
// 所有checkbox事件绑定处理
$("#popParameter :checkbox").click(function(){
	var $this = $(this);
	//操作人员checkbox被点击的场合
	if($this.attr("id")=="dpCheck") {
		var $module = $this.parent().parent();
		if(this.checked) {
			$module.find(":checkbox").prop("checked",true);
		} else {
			$module.find(":checkbox").prop("checked",false);
		}
		checkboxInit(this,"dpCheck");
	}
	// 业务分组checkbox被点击的场合
	if($this.attr("id")=="gpCheck") {
		var $page = $this.parent().parent().next(".after");
		if(this.checked) {
			$page.find(":checkbox").prop("checked",true);
		} else {
			$page.find(":checkbox").prop("checked",false);
		}
		checkboxInit(this,"gpCheck");
	}
	// 业务明细checkbox被点击的场合
	if($this.attr("id")=="btCheck") {
		checkboxInit(this,"btCheck");
	}
});

function subSysCheckboxInit($object) {
	var $subSysId = $object.parent().parent().parent().parent().parent().prev().find(":checkbox");
	if($object.parent().parent().parent().find(":checkbox[checked]").length != 0) {
		$subSysId.prop("checked",true);
	} else {
		$subSysId.prop("checked",false);
	}
	moduleCheckboxInit($subSysId);
}

function moduleCheckboxInit($object) {
	var $moduleId = $object.parent().parent().parent().parent().parent().prev().find(":checkbox");
	if($object.parent().parent().parent().parent().parent().find(":checkbox[checked]").length != 0) {
		$moduleId.prop("checked",true);
	} else {
		$moduleId.prop("checked",false);
	}
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
	if(id == "dpCheck") {
		pageCheckboxInit($(object));
	}
	if(id == "gpCheck") {
		moduleCheckboxInit($(object));
	}
	if(id == "btCheck") {
		subSysCheckboxInit($(object));
	}
}