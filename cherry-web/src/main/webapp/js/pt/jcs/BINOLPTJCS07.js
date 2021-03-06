var BINOLPTJCS07 = function () {
};
BINOLPTJCS07.prototype = {
	// 删除DIV
	"delDivRow" : function(obj) {
		var $salePrices = $("#priceInfo").find("input[name='salePrice']");
		// 保证价格信息不为空
		if($salePrices.length > 1){
			var $table = $(obj).parents('table');
			// 产品销售价格
			var $prtPriceId = $table.find("input[name='prtPriceId']");
			if($prtPriceId.length > 0){
				$table.empty();
				$table.append('<input type="hidden" name="prtPriceId" value="' + $prtPriceId.val() + '"/>');
				$table.append('<input type="hidden" name="option" value="2" />');
			}else{
				$table.remove();
			}
		}
	},
	// 后退
	"doBack" : function (){
		var tokenVal = parentTokenVal();
		$("#parentCsrftoken").val(tokenVal);
		$("#toDetailForm").submit();
	}
};
var BINOLPTJCS07 = new BINOLPTJCS07();
$(function(){
	// 初始化会员价格折扣率
	// 会员折扣
	var $memRate = $("#memRate");
	// 价格信息table
	var $table = $("#priceInfo").find("table").first();
	// 销售价格
	var $salePrice = $table.find("input[name='salePrice']");
	// 会员价格
	var $memPrice = $table.find("input[name='memPrice']");
	var salePrice = parseFloat($salePrice.val());
	var memPrice = parseFloat($memPrice.val());
	if(isNaN(salePrice)){
		salePrice = 0;
	}
	if(isNaN(memRate)){
		memRate = 0;
	}
	if(salePrice != 0){
		var memRate = parseInt(memPrice/salePrice * 100);
		$memRate.val(memRate);
	}
});

$(function() {
	//产品名称(中文名)前台正则校验
	$("#nameTotal").bind("keyup blur",function() {
		//清空span标签错误信息
		$("#nameTotalTip").html("");
		var nameTotal = $(this).val();
		if($.trim(nameTotal).length != 0) {
			//不能含有半角：分号;    逗号,    单双引号'"      斜杠/\      问号?
			var re = /^[^;,'"/?\\]+$/;
			if (!re.test(nameTotal)) {
				//span标签显示错误信息
				$("#nameTotalTip").html("含有非法字符/\\',;\"?");
			}
		}
	});
});

