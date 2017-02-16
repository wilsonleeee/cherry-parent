/*
 * 全局变量定义
 */
function BINOLMBRPT14(){};

BINOLMBRPT14.prototype = {
    /**
     * 查询
     */
    "search" : function(msg) {
        $("#errorDiv2 #errorSpan2").html("");
        $("#errorDiv2").hide();
        $("#errorMessage").empty();
        if(typeof msg != "undefined"){
            $("#actionResultDisplay").html(msg);
            $("#actionResultDisplay").show();
        }else{
            $("#actionResultDisplay").empty();
        }
        var $form = $('#mainForm');
        if (!$form.valid()) {
            return false;
        };

        var url = $("#searchUrl").attr("href");
        // 查询参数序列化【包含了csrftoken值】
        var params= $form.serialize();
        url = url + "?" + params;
        // 显示结果一览
        $("#section").show();
        // 表格设置
        var tableSetting = {
            // 表格ID
            tableId : '#dataTable',
            // 表格默认排序
            aaSorting : [[ 1, "asc" ]],
            // 数据URL
            url : url,
            // 表格列属性设置
            aoColumns :    [
                { "sName": "no","bSortable": false},
                { "sName": "mobilePhone"},
                { "sName": "memberName"},
                { "sName": "totalPercent"}
            ],

            // 不可设置显示或隐藏的列
            aiExclude :[0, 1, 2, 3],
            // 横向滚动条出现的临界宽度
            sScrollX : "100%",
            callbackFun : function (){}
        };
        // 调用获取表格函数
        getTable(tableSetting);
    }
};

var BINOLMBRPT14 = new BINOLMBRPT14();

$(document).ready(function() {

});
