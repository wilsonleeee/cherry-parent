// AJAX加载部门下拉框
function getDepart(_this){
	// 加载部门URL
	var url = "/Cherry/common/BINOLCM13_queryDepart";
	// 参数
	var params = getCommParams();
	// 要做AJAX查询的部门下拉框集合
	var $departs;
	var $this = $(_this);
	// 当前等级的下级部门list
	$departs = $this.parent().nextAll().find('select[id^="DEPART_"]');
	// 清除柜台信息
	cleanCounter("#departModeText_4");
	// 部门select初始化
	initDepart($departs);
	params += "&" + getRangeParams();
	// AJAX加载部门
	ajaxRequest(url,params,function(msg){setDepart(msg,$departs);},"json");
}
// 清除柜台信息
function cleanCounter(id){
	$counter = $(id);
	$next = $counter.next();
	$counter.val("");
	$next.val("");
}
function setDepartDisable(_this){
	var $this = $(_this);
	var type = $this.val();
	var $departline = $("#DEPARTLINE");
	var $departs = $departline.find(":input").not(":hidden[name='departId']");
	var $th = $departline.find("th");
	$th.removeClass("sorting");
	if( type == ""){
		$departs.each(function(){
			$(this).prop("disabled",false);
		});
	}else{
		var disFlag = false;
		// 循环所有部门，高亮选中的组织类型的部门表头
		// 当前选中类型部门其后的disabled
		$departs.each(function(j){
			var $that = $(this);
			var thisId = $that.prop("id");
			$that.prop("disabled",disFlag);

			if(thisId.indexOf("DEPART_") > -1){
				thisId = thisId.substring("DEPART_".length);
			}else{
				thisId = thisId.substring("departModeText_".length);
			}
			if(!disFlag){
				if(type == thisId){
					disFlag = true;
	    			$th.eq(j).addClass("sorting");
				}
			}else{
				//$that.find("option:first").prop("selected",true);
			}
		});
		if(type != "4"){
			// 清除柜台信息
			cleanCounter("#departModeText_4");
			$("#departModeText_4").next().val("");
		}
	}
}
// 部门下拉框初始化
function initDepart($departs){
	// 部门下拉框初始化
	$departs.each(function(){
		var $this = $(this);
		$this.find("option:not(:first)").remove();
		if($this.is(":enabled")){
			$this.prop("disabled",true);
			// 用于区分此下拉框开始是enabled
			$this.addClass("cMark");
		}
    });
}
// 设置部门下拉框
function setDepart(msgJson,$departs){
	// 取的组织类
	for (var one in msgJson){
	    var departId = msgJson[one].departId;
	    var departName = msgJson[one].departName;
	    var departCode = msgJson[one].departCode;
	    var departType = msgJson[one].departType;
	    // 目标部门下拉框控件
	    var $depart = null;
	    // 取得目标部门下拉框控件
	    $departs.each(function(){
	    	var $this = $(this);
	    	var thisIds = $this.prop("id").split("_");
	    	for(var i in thisIds){
	    		if(departType == thisIds[i]){
	    			$depart = $this;
	    			return;
	    		}
	    	}
	    });
	    // 目标部门下拉框控件添加元素
	    if($depart !=null && $depart != undefined){
	    	var str = '<option value="'+ departId + '">[' + escapeHTMLHandle(departCode) + ']' + escapeHTMLHandle(departName) + '</option>';
	        $depart.append(str);
	    }
    }
	// 下拉框解锁
	$departs.filter(".cMark").prop("disabled",false);
	$departs.filter(".cMark").removeClass("cMark");
}
//取得区域子集List
function getsubRegionList(_this){
	var url = "/Cherry/common/BINOLCM00_querySubRegion";
	var $this = $(_this);
	var $parent = $this.parent();
	var $next = $parent.next().find('select');
	var $nextAll = $this.parent().nextAll().find('select').not(":last");
	$nextAll.find("option:not(:first)").remove();
	if($this.val() != ""){
		var params = getSerializeToken();
		params += "&regionId=" + $this.val();
		doAjax2(url, "regionId", "regionName", $next, params);
	}
	// 清除柜台信息
	cleanCounter("#areaModeText");
 }
/**
 * 取得最低级部门
 * @return
 */
function getMinDepart(){
	// 部门
	var $departs = $("#DEPARTLINE").find('select[id^="DEPART_"]');
	var $minDepart = null;
 	if($departs.length > 0){
 		$departs.each(function(i){
 			if($(this).val()!="") {$minDepart = $(this);}
 	 	});
 		if($minDepart == null){
 	 		$minDepart = $departs.first();
 	 	}
 	}
 	return $minDepart;
}
// 取得共通参数
function getCommParams(){
	var params = "";
	// 业务类型
 	var businessType = $("#BUSINESSTYPE").serialize();
 	// 操作类型
 	var operationType = $("#OPERATIONTYPE").serialize();
 	var csrftoken = getSerializeToken();
 	params = businessType + "&" + operationType + "&" + csrftoken;
 	return params;
}
 //选择范围参数取得
function getRangeParams(){
	var params = "params=";
	var expr = "#hide_comm_div,div.tabs > div:visible";
	var json = Obj2JSON(expr,true);
	$departType = $("div.tabs > div:visible").find("#DEPARTTYPE").find("input:checked");
	var $testType = $("#testType");
	if($departType.length > 0){
		var val = $departType.val();
		if(val != ""){
			val  = '["' + val.replace(/\_/g,'","') + '"]';
			json.push('"departType":'+val);
		}
	}
 	return params + "{"+ json + "}";
}
/**
 * 在部门的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是50
 * 				String:options.selected 参数可选，希望被选中的部分，"name":表示要选中部门名称；"code":表示要选中的是部门CODE，默认是"name"
 * 				String:options.includeCounter 参数可选，查询结果中是否包含柜台，只要提供该参数就表示包含柜台信息，默认是不包含
 * 				String:options.privilegeFlag 参数可选，是否带权限查询（0：不带权限，1：带权限）为空的场合默认不带权限
 *
 * */
function departInfoBinding(options){
	var url = "/Cherry/common/BINOLCM13_queryDepart2.action";
	url += "?" + getCommParams();
	var $input = $('#'+options.elementId);
	var $next = $input.next();
	$input.autocompleteCherry(url,{
		extraParams:{
			departInfoStr: function() { return $.trim($input.val());},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			departType: function() {
				var elementId = options.elementId;
				var index = elementId.indexOf("_");
				if(index == -1){
					return "";
				}else{
					return elementId.substring(index+1);
				}
			},
			testType:function() {
				var $testType = $("#testType");
				if ($("#testType").val()=="0"){
					return 0;
				}else if($("#testType").val()=="1"){
					return 1;
				}else if ($("#testType").val()=="ALL"){
					return "ALL";
				}

				//if($testType.prop("checked")){
				//	return 1;
				//}else{
				//	return 0;
				//}
			},
			orgValid:function() {
				var $orgValid = $("#orgValid");
				if ($("#orgValid").val()=="0"){
					return 0;
				}else if($("#orgValid").val()=="1"){
					return 1;
				}else if ($("#orgValid").val()=="ALL"){
					return "ALL";
				}
				//if($orgValid.prop("checked")){
				//	return 0;
				//}else{
				//	return 1;
				//}
			},
			orgValidAll:function() {
				var $orgValidAll = $("#orgValidAll");
				if($orgValidAll.prop("checked")){
					return 1;
				}else{
					return 0;
				}
			},
			params:function() {
				$next.val("");
				return "{"+ Obj2JSON($("div.tabs > div:visible").find("table")) + "}" ;
			}
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:200,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return "["+ escapeHTMLHandle(row[1])+"]" +escapeHTMLHandle(row[0]);
		}
	}).result(function(event, data, formatted){
		$next.val(data[2]);
	}).bind("change",function(){
		// 清除input框后hidden值
		$next.val("");
	}).bind("focusout",function(){
		if($next.val() == ""){
			$input.val("");
		}
	});
}
function depotInfoBinding(options){
	var url = "/Cherry/common/BINOLCM13_queryDepot.action";
	url += "?" + getCommParams();
	var $input = $('#'+options.elementId);
	var $next = $input.next();
	$input.autocompleteCherry(url,{
		extraParams:{
			depotInfoStr: function() { return $.trim($input.val());},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 50,
			testType:function() {
				var $testType = $("#testType");
				if ($("#testType").val()=="0"){
					return 0;
				}else if($("#testType").val()=="1"){
					return 1;
				}else if ($("#testType").val()=="ALL"){
					return "ALL";
				}
			},
			orgValid:function() {
				var $orgValid = $("#orgValid");
				if ($("#orgValid").val()=="0"){
					return 0;
				}else if($("#orgValid").val()=="1"){
					return 1;
				}else if ($("#orgValid").val()=="ALL"){
					return "ALL";
				}
			},
			orgValidAll:function() {
				var $orgValidAll = $("#orgValidAll");
				if($orgValidAll.prop("checked")){
					return 1;
				}else{
					return 0;
				}
			},
			params:function() { return "{"+ Obj2JSON("div.tabs > div:visible") + "}";}
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:230,
	    max:options.showNum ? options.showNum : 50,
		formatItem: function(row, i, max) {
			return "["+escapeHTMLHandle(row[1])+"]"+escapeHTMLHandle(row[0]);
		}
	}).result(function(event, data, formatted){
		$next.val(data[2]);
		getLogicDepot();
	}).bind("change",function(){
		// 清除input框后hidden值
		$next.val("");
		// 初始化逻辑仓库
		$("#LGCINVENTORY").html($("#LGCINVENTORY_OPTION").html());
	}).bind("focusout",function(){
		if($next.val() == ""){
			$input.val("");
		}
	});

}
// ajax 刷新共通条
function getDepartBar(mode,showType,orgValidType){
	var url = "/Cherry/common/BINOLCM13_query";
	var commParams = getCommParams();
	var $showLgcDepot = $("#LGCINVENTORY");
	commParams += "&mode=" + mode + "&showType=" + showType + "&orgValidType="+orgValidType;

	if($("#testType").val()=="0"){
		commParams += "&testType=0";
	}else if ($("#testType").val()=="1"){
		commParams += "&testType=1";
	}else if ($("#testType").val()=="ALL"){
		commParams += "&testType=ALL";
	}

	if($("#orgValid").val()=="0"){
		commParams += "&orgValid=0";
	}else if($("#orgValid").val()=="1"){
		commParams += "&orgValid=1";
	}else if($("#orgValid").val()=="ALL"){
		commParams += "&orgValid=ALL";
	}

	if($("#orgValidAll").prop("checked")) {
		commParams += "&orgValidAll=1";
	}else {
		commParams += "&orgValidAll=0";
	}

	if($showLgcDepot.length > 0){
		commParams += "&showLgcDepot=1";
	}
	// AJAX加载共通条
	ajaxRequest(url,commParams,function(msg){
		$barDiv = $("#DEPART_DIV");
		$parent = $barDiv.parent();
		$barDiv.remove();
		$parent.append(msg);
	});
}
// 根据实体仓库ID查询逻辑仓库
function getLogicDepot(){
	var $target = $("#LGCINVENTORY");
	var url = "/Cherry/common/BINOLCM13_queryLgcDepot";
	var params = getSerializeToken();
	// 实体仓库ID参数
	params += "&" + $("#depotModeText").next().serialize();
	// 默认逻辑仓库选项
	//var $secd= $target.find("option").eq(1);
	// 保留前2条选项【空白，默认逻辑仓库】
	$target.find("option:not(:first)").remove();
	//$target.append($secd);
	doAjax(url, "lgcInventoryId", "lgcInventoryName", $target, params);
}