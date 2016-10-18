/**
 * 打开礼品弹出框
 */
ACT.openDialog = function(targetId, prtType, prmCate){
	var option = {
 	   	targetId: targetId,
   		checkType : "checkbox",
   		isExchanged: '1',
   		prmCate: 'CXLP',
        mode : 2,
        //  bindFlag : true,
        brandInfoId : $("#brandInfoId").val(),
	    getHtmlFun:function(info){
			return ACT.getHtmlFun(info,prtType,prmCate,false);
	   	}
    };
	if(!isEmpty(prmCate)){
		option.checkType = 'radio';
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
		prmCate = 'DHCP';
	}
	ACT.openDialog($target.prop('id'),prtType,prmCate);
}
