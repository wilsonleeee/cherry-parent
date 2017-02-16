$(document).ready(function() {
	// 促销柜台选择绑定
	prmCounterBinding();
	// 促销品选择绑定
	prmPrtBinding();
	// 促销活动名绑定
	prmActNameBinding();
	cherryValidate(
	{
		formId:'mainForm',
		rules:
		{
			startDate:{dateValid:true},
			endDate:{dateValid:true}
		}
	});
});
/**
 * 促销柜台选择绑定
 * @return
 */
function prmCounterBinding (){
	var url = $('#indSearchPrmCounterUrl').html()+"?csrftoken="+$('#csrftoken').val();
	textBinding({elementId:"prmCounter",urlSrc:url,useId:"prmCounterId",index:0});
}
/**
 * 促销活动名选择绑定
 * @return
 */
function prmActNameBinding (){
	var url = $('#indSearchPrmActNameUrl').html()+"?csrftoken="+$('#csrftoken').val();
	textBinding({elementId:"prmActiveName",urlSrc:url,useId:"activityCode",index:1});
}
/**
 * 促销品选择绑定
 * @return
 */
function prmPrtBinding(){
	var url = $('#indSearchPrmPrtUrl').html()+"?csrftoken="+$('#csrftoken').val();
	textBinding({elementId:"prmProduct",urlSrc:url,useId:"prmProductId",index:2});
}
/**
 * 在产品的text框上绑定下拉框选项
 * @param Object:options
 * 				String:options.elementId 元素ID,要绑定下拉框的元素ID
 * 				String:options.showNum 参数可选，下拉最大的显示个数，默认是20
 * 				String:options.urlSrc url地址
 * 				String:options.useId 元素隐藏id，对应元素的实际id
 * 				int:options.index 调用方法的索引值
 * 
 * */
function textBinding(options){
	var recordPrtName = null;
	var keycode = [13,37,38,39,40,16,17,9,27,123,18,20,93,91,144];
	var flag = false;
	var url = options.urlSrc;
	$('#'+options.elementId).autocompleteCherry(url,{
		extraParams:{
			paramInfoStr: function() { return $('#'+options.elementId).val();},
			//默认是最多显示50条
			number:options.showNum ? options.showNum : 20
		},
		loadingClass: "ac_loading",
		minChars:1,
	    matchContains:1,
	    matchContains: true,
	    scroll: false,
	    cacheLength: 0,
	    width:300,
	    max:options.showNum ? options.showNum : 20,
		formatItem: function(row, i, max) {
			return escapeHTMLHandle(row[1])+" ["+escapeHTMLHandle(row[0])+"]";
		}
	}).result(function(event, data, formatted){
		$("#" + options.useId).val(data[2]);
		
		if(data.length>3){
			$("#prtType").val(data[3]);
		}
		
		$('#'+options.elementId).data("prtName" + options.index,$("#" +  + options.useId).val());
		$('#'+options.elementId).data("change" + options.index,true);
	}).bind("keydown",function(event){
		for( var i in keycode){
			if(event.keyCode == keycode[i]){
				flag = true;
			}
		}
		if(flag){
			if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change" + options.index,true);
				$('#'+options.elementId).data("flag" + options.index,false);
				
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName")){
				$('#'+options.elementId).data("change" + options.index,false);
				$('#'+options.elementId).data("flag" + options.index,false);
			}else{
				$('#'+options.elementId).data("change" + options.index,false);
				$('#'+options.elementId).data("flag" + options.index,true);
			}
			flag = false;
		}else{
			if((!$('#'+options.elementId).data("flag" + options.index))&&
					(!$('#'+options.elementId).data("change" + options.index))&&
					($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName" + options.index))){
				$("#" + options.useId).val("");
			}else if($('#'+options.elementId).val() == $('#'+options.elementId).data("prtName" + options.index)){
				$('#'+options.elementId).data("change" + options.index,true);
				$('#'+options.elementId).data("flag" + options.index,true);
			}else if($('#'+options.elementId).val() != $('#'+options.elementId).data("prtName" + options.index)){
				$('#'+options.elementId).data("change" + options.index,false);
				$('#'+options.elementId).data("flag" + options.index,true);
			}else{
				$('#'+options.elementId).data("change" + options.index,false);
				$('#'+options.elementId).data("flag" + options.index,true);
			}
		}
	}).bind("change",function(){
		if(!$('#'+options.elementId).data("change" + options.index)&&($('#'+options.elementId).data("flag" + options.index))){
			$("#" + options.useId).val("");
			$("#" + options.useId).next().val("");
		}else{
			$('#'+options.elementId).data("change" + options.index,false);
		}
	}).data("flag" + options.index,true);
}
