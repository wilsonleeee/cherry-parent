var binolmomup02_global = {};
binolmomup02_global.needUnlock = true;
var BINOLMOMUP02_doRefresh = false;

window.onbeforeunload = function() {
    if (window.opener) {
        window.opener.unlockParentWindow();
        if(BINOLMOMUP02_doRefresh){
            var url = $('#search_url', window.opener.document).val();
            window.opener.search(url);
        }
    }
};

$(document).ready(function() {
    if (window.opener) {
        window.opener.lockParentWindow();
    }
    $('#openUpdateTime').cherryDate();
    cherryValidate({
        formId: "update",
        rules: {
            version:{required: true,maxlength: 50},
            downloadUrl: {required: true,maxlength: 100},	//渠道名称
            md5Key: {required: true,maxlength: 125}
        }
    });
});

function doBack(){
    var tokenVal = parentTokenVal();
    $("#parentCsrftoken").val(tokenVal);
    binolmomup02_global.needUnlock = false;
    $("#toDetailForm").submit();
}

function update() {
    if(!$('#update').valid()) {
        return false;
    }
    var param = $('#update').find(':input').serialize();
    var callback = function(msg) {
        if(window.opener.oTableArr[0] != null)window.opener.oTableArr[0].fnDraw();
    };
    cherryAjaxRequest({
        url: $('#MUP02_update').attr('href'),
        param: param,
        callback:  function(msg) {BINOLMOMUP02_doRefresh = true;}
    });
}