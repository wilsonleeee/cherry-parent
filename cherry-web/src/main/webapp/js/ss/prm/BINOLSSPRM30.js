//工作流单据间跳转是否解锁
var OS_BILL_Jump_needUnlock = true;

window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	}
};

$(document).ready(function() {
	$('.tabs').tabs();
	initDetailTable();
	if (window.opener) {
       window.opener.lockParentWindow();
    }
	$("#exportButton").prop("disabled",false);
} );
