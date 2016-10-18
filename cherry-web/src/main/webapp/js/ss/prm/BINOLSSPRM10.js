
window.onbeforeunload = function(){
		if (window.opener) {
			window.opener.unlockParentWindow();
	}
};
	
$(document).ready(function() {
	if (window.opener) {
		window.opener.lockParentWindow();
	}
	cherryValidate({			
		formId: "addCatrgoryForm",		
		rules: {		
		        itemClassNameCN: {maxlength: 50},	            // 类别中文名
		        itemClassNameEN: {maxlength: 50},	            // 类别英文名
		        itemClassCode: {required:true, maxlength: 20},  // 类别码
		        curClassCode: {maxlength: 4}                    // 类别特征码
		}		
	});
} );

//根据品牌改变类别
function cate10_changeBrand(object) {
	
	var callback = function(msg){
		var jsons = eval('('+msg+')');
		$('#path').empty();
		if(jsons.higherCategoryList.length > 0) {
			for(var i in jsons.higherCategoryList) {
				$('#path').append('<option value="'+jsons.higherCategoryList[i].path+'">'+jsons.higherCategoryList[i].itemClassName+'</option>');
			}
		}
	};
	cherryAjaxRequest({
		url: $('#filterByBrandInfoUrl').attr("href"),
		param: $(object).serialize(),
		callback: callback,
		formId: '#addCatrgoryForm'
	});
	
}

//保存
function doSave(url) {
	if (!$('#addCatrgoryForm').valid()) {
		return false;
	};
	
	$('#addCatrgoryForm :input').each(function(){
		$(this).val($.trim(this.value));
	});
	var param = $('#addCatrgoryForm :input').serialize();
	
	var higherCategory = $('#addCatrgoryForm :input[name=path]').val();
	var callback = function(msg) {
		if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
		if(window.opener.document.getElementById('categoryTree')) {
			window.opener.refreshCateNode(higherCategory);
		}
	};
	cherryAjaxRequest({
		url: url,
		param: param,
		callback: callback,
		formId: '#addCatrgoryForm'
	});
}

	function doClose(){
		window.close();
	}