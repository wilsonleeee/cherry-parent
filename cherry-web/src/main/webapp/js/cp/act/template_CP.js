/**
 * 打开礼品弹出框
 */
ACT.openDialog = function(targetId, prtType,prmCate,index){
	var option = {
 	   	targetId: targetId,
 	   	checkType : "checkbox",
  		prmCate: 'CXLP',
        mode : 2,
        //  bindFlag : true,
        brandInfoId : $("#brandInfoId").val(),
	       	getHtmlFun:function(info){
			return ACT.getHtmlFun(info,prtType,prmCate,true);
	   	}
    };
	if(!isEmpty(prmCate)){
		option.prmCate = prmCate;
	}
	if(prtType == 'N'){
		popAjaxPrtDialog(option);
	}else{
		popAjaxPrmDialog(option);
	}
}

ACT.openDialog2 = function(_this, prtType,prmCate){
	var $this = $(_this);
	var $target = $this.parent().next().find('tbody');
	if(!isEmpty(prmCate)){
		prmCate = 'TZZK';
	}
	ACT.openDialog($target.prop('id'),prtType,prmCate);
}

//改变优惠券方式
ACT.changeCouponType=function(_this,index){
	var $this = $(_this);
	var $couponNumShow = $("#couponCount_" + index);
	var $couponCount = $("#couponCount_AA_" + index);
	var $linkCouponImport = $("#linkCouponImport_"+index);
	$("#couponInfo_"+index).hide();
	var $searchCoupon =$("#searchCoupon_"+index);
//	$("#batchCode_" + index).val("");
	if($this.val() =='3' ){
		$couponNumShow.hide();
		$linkCouponImport.show();
		$searchCoupon.show();
		$couponCount.val("");
	}else{
		$couponNumShow.show();
		$linkCouponImport.hide();
		$searchCoupon.hide();
	}
}