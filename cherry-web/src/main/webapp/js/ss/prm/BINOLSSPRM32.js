window.onbeforeunload = function(){
	if (window.opener) {
		window.opener.unlockParentWindow();
	}
};

$(document).ready(function() {
	initDetailTable();
	getPrintTip();
	if (window.opener) {
       window.opener.lockParentWindow();
    }
} );

//库存明细
function getPrintTip() {
	var csrftoken = getParentToken();
	$('#dataTable').find("a.inventory").each(function(){
		var url = $(this).attr("href") + '&' + csrftoken;
		$(this).attr("rel",url);
	});
	$('#dataTable').find("a.inventory").cluetip({
	    width: '520',
		closeText:'<span class="ui-icon icon-invalid"></span>',
		closePosition:'title',
		sticky:true,
		mouseOutClose:true,
		
	    cluetipClass: 'default',
	    cursor:'pointer',
	    arrows: true, 
	    dropShadow: false});
}

function setRPM32Params(id){
	var $hideDiv = $(id);
	var $hiddens = $hideDiv.children(":input");
	var params = $hideDiv.find("div").find("input[name='params']").val();
	var JSON = [];
	$hiddens.each(function(){
		var $this = $(this);
		JSON.push('"'+$this.prop("name")+'":"'+ $this.val() +'"');
	});
	if(params != null && params != undefined && params != ""){
		if(JSON.length > 0){
			params = params.substring(0,params.length-1);
			params += "," + JSON.toString() + "}";
		}
	}else{
		params = "{" + JSON.toString() + "}";
	}
	$("#e_params").val(params);
}