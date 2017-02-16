/*
 * 全局变量定义
 */
var binOLMBMBM31_global = {};
var BINOLMBMBM31_code=1;
function BINOLMBMBM31(){};

BINOLMBMBM31.prototype = {
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
            aoColumns :    [{ "sName": "number", "sWidth": "3%", "bSortable": false},
                { "sName": "ruleName"},
                { "sName": "startTime"},
                { "sName": "endTime"},
                { "sName": "operate", "bSortable": false},
                { "sName": "memo"}
            ],

            // 不可设置显示或隐藏的列
            aiExclude :[0, 1, 2, 3, 4],
            // 横向滚动条出现的临界宽度
            sScrollX : "100%",
            callbackFun : function (){}
        };
        // 调用获取表格函数
        getTable(tableSetting);
    },

    /**
     * 停止规则确认框
     */
    "popDeleteDialog" : function(obj) {
        $("#errorDiv2 #errorSpan2").html("");
        $("#errorDiv2").hide();
        $("#errorMessage").empty();
        $("#actionResultDisplay").empty();
        var _$this = $(obj);
        var that = this;

        // 删除对象的ID
        var param = 'completeDegreeRuleID=' + _$this.parent().find("#completeDegreeRuleID").val();
        param += '&startTime='+_$this.parent().find("#startTime").val();
        param += '&endTime='+_$this.parent().find("#endTime").val()+ "&csrftoken=" + getTokenVal();

        var dialogSetting = {
            dialogInit: "#popRuleDeleteTable",
            text: $("#delConfirmMessage").html(),
            width: 	500,
            height: 300,
            title: 	$("#deleteRuleTitle").text(),
            confirm: $("#dialogConfirm").text(),
            cancel: $("#dialogCancel").text(),
            confirmEvent: function(){
                BINOLMBMBM31.deleteConfirm($("#deleteRuleUrl").attr("href"),param);
                removeDialog("#popRuleDeleteTable");
            },
            cancelEvent: function(){
                removeDialog("#popRuleDeleteTable");
            }
        };
        openDialog(dialogSetting);
    },

    /**
     * 删除指定竞争对手
     */
    "deleteConfirm" : function(url,param) {
        var callback = function(msg){
            if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
                return;
            }
            var param_map = eval("("+msg+")");

            var resultCode=param_map.resultCode;
            var resultMsg=param_map.resultMsg;
            if(resultCode=="-1"){
                BINOLMBMBM31.showMessageDialog({
                    message:resultMsg,
                    type:"MESSAGE"
                });
                return false;
            }
            if(resultCode=="0"){
                removeDialog("#popRuleAddTable");
                BINOLMBMBM31.showMessageDialog({
                    message:"删除成功",
                    type:"MESSAGE"
                });
            }
            BINOLMBMBM31.search();
        };
        cherryAjaxRequest({
            url: url,
            param: param,
            callback: callback
        });
    },
   "addRule" : function () {
        var numChoice = $("#rule_content input[id^=input_]").length;
        if (numChoice == 20) {
            return false;
        }
       var memberAttrList = $("#memberAttrList").val();
        var memberAttrListJson = eval('('+memberAttrList+')');
       var select =BINOLMBMBM31_code;
       var str = '<div id = "div_'
           +select+ '" class="clearfix ruleDetail">' ;
       if(memberAttrListJson && memberAttrListJson.length > 0) {
           str += '<select name="memberAttrListJson" id="memberAttrListJson">';
           for (var i = 0; i < memberAttrListJson.length; i++) {
               str += '<option value="' + memberAttrListJson[i].memInfoKey + '">' + memberAttrListJson[i].memInfoValue + '</option>';
           }
           str += '</select>';
       }
       str += '完成比例'
       str += '<span class="highlight">*</span>'
       str += '<input class="text" type="text" id="percent" name="percent" maxlength="3"/>%';
       str += '奖励积分'
       str += '<input class="text" type="text" id="point" name="point" maxlength="6"/>';
       str +='<a id="delete" class="delete right" onclick="$(this).parent().remove();return false;" ><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a></div>'
       $("#rule_content").append(str);
       BINOLMBMBM31_code++;
    },
    "isPositiveNum":function(s) {//是否为大于等于0的正整数
        var re = /^[1-9]\d*|0$/ ;
        return re.test(s)
    },
    "savePaper" : function (){
        if(!$('#mainRuleForm').valid()) {
            return false;
        }
        $('#errorDiv').hide();
        // 获取会员属性个数
        var ruleNum = $("#rule_content").children().length;
        if(ruleNum < 1){
            BINOLMBMBM31.showErrorMsg("请至少添加一条信息完善属性");
            return false;
        }
        var isPointCorrect = 0;
        var totalPoint=0;
        var totalPercent=0;
        var isPercentCorrect=0;
        var ruleList = {};
        var pointGroupList = new Array();
        var noPointGroupList = new Array();
        $(".ruleDetail").each(function() {
            //alert($(this).child().find("percent").val());
            //alert($(this).find("#percent").val());
            //alert($(this).find("#percent").val());
            //alert($(this).val());
            if ($(this).find("#percent").val().length==0){
                isPercentCorrect=1;
            }
            if ($(this).find("#percent").val()) {
                var q = {};
                //q.key = $(this).find("#memberAttrListJson  option:selected").val();
                var keyName= $(this).find("#memberAttrListJson  option:selected").val();
                //var total = [];
                var percentMap = {};
                percentMap.percent =$(this).find("#percent").val();
                //percentMap.value = $(this).find("#percent").val();
                //total.push(percentMap);
                //var pointMap = {};
                percentMap.point =$(this).find("#point").val();
                //pointMap.value = $(this).find("#point").val();
                //total.push(percentMap);
                q[keyName] = percentMap;
                if ($(this).find("#point").val()){
                    pointGroupList.push(q);
                }else{
                    noPointGroupList.push(q);
                }
                if ($(this).find("#point").val()&&!BINOLMBMBM31.isPositiveNum($(this).find("#point").val())){
                    isPointCorrect=1;
                }
                if ($(this).find("#percent").val().length==0){
                    isPercentCorrect=1;
                }
                if (!BINOLMBMBM31.isPositiveNum($(this).find("#percent").val())){
                    isPercentCorrect=1;
                }
                totalPoint+=Number($(this).find("#point").val());
                totalPercent+=Number($(this).find("#percent").val());
            }

            });
        ruleList.pointGroup=pointGroupList;
        ruleList.noPointGroup=noPointGroupList;
        if(isPercentCorrect==1){
            BINOLMBMBM31.showErrorMsg("完成比例必须为正整数");
            return false;
        }
        if(isPointCorrect==1){
            BINOLMBMBM31.showErrorMsg("奖励积分必须为大于等于0的正整数");
            return false;
        }
        if(totalPercent!=100){
            BINOLMBMBM31.showErrorMsg("完成比例合计必须等于100%");
            return false;
        }

        var queStr = JSON2.stringify(ruleList);
        $("#ruleJsonSave ").val(queStr);
        var param = "csrftoken=" + getTokenVal() + "&" + $("#mainRuleForm").serialize()+"&newRuleFlag=1";
        var url = $("#url_saveRule").html();
        var opt = {
            url : url,
            param : param,
            callback : function(msg) {
                //if($('#actionResultBody').length > 0) {
                //    if(window.opener.oTableArr[0] != null)window.opener.BINOLMBMBM31.search();
                //}
                //$("div").removeClass("container");
                if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
                    return;
                }
                var param_map = eval("("+msg+")");

                var resultCode=param_map.resultCode;
                var resultMsg=param_map.resultMsg;
                if(resultCode=="-1"){
                    BINOLMBMBM31.showMessageDialog({
                        message:resultMsg,
                        type:"MESSAGE"
                    });
                    return false;
                }
                if(resultCode=="0"){
                    removeDialog("#popRuleAddTable");
                    BINOLMBMBM31.showMessageDialog({
                        message:"保存成功",
                        type:"MESSAGE"
                    });
                }
                BINOLMBMBM31.search();
            }
        };
        cherryAjaxRequest(opt);
    },
    "showErrorMsg":function(msg){
        BINOLMBMBM31.showMessageDialog({
            message:msg,
            type:"MESSAGE"
        });
    },
    "showMessageDialog":function (dialogSetting){
        if(dialogSetting.type == "MESSAGE"){
            $("#messageContent").show();
            $("#successContent").hide();
            $("#messageContentSpan").text(dialogSetting.message);
        }else{
            $("#messageContent").hide();
            $("#successContent").show();
            $("#successContentSpan").text(dialogSetting.message);
        }
        var $dialog = $('#messageDialogDiv');
        $dialog.dialog({
            //默认不打开弹出框
            autoOpen: false,
            //弹出框宽度
            width: 400,
            //弹出框高度
            height: 220,
            //弹出框标题
            title:"消息提示框",
            //弹出框索引
            zIndex: 99,
            modal: true,
            resizable:false,
            //关闭按钮
            close: function() {
                closeCherryDialog("messageDialogDiv");
                if(typeof dialogSetting.focusEvent == "function") {
                    dialogSetting.focusEvent();
                }
            }
        });
        $dialog.dialog("open");
        // 给确认按钮绑定事件
        $("#btnMessageConfirm").bind("click", function(){
            closeCherryDialog("messageDialogDiv");
            //binolssprm7302.messageConfirm(dialogSetting.focusEvent);
        });
    },
    /**
     * 根据ID查询对应的规则信息
     */
    "getRuleDetailInfo" : function(obj) {
        var $this = $(obj);
        var that = this;
        var url = $this.attr("href");
        //var dialogSetting = {
        //    index :123,
        //    dialogInit : "#popRuleDetailTable",
        //    width : 800,
        //    height : 560,
        //    zIndex: 1,
        //    resizable : true,
        //    title : "查看活动明细",
        //    //confirm :$("#dialogOk").text(),
        //    cancel: "关闭",
        //    cancelEvent: function(){
        //        removeDialog("#popRuleDetailTable");
        //    }
        //};
        var dialogSetting = {
            dialogInit: "#popRuleDetailTable",
            text:  "会员信息完善规则查看",
            width: 	800,
            height: 560,
            title: "会员信息完善规则查看",
            cancel: "关闭",
            cancelEvent: function(){removeDialog("#popRuleDetailTable");}
        };


        openDialog(dialogSetting);
        var callback = function(msg) {
            //取得活动明细信息
            $("#popRuleDetailTable").html(msg);
        };
        var $url= url+'&csrftoken='+getSerializeToken();
        cherryAjaxRequest( {
            url : $url,
            callback : callback
        });
    },
    /**
     * 根据ID查询对应的规则信息
     */
    "addRuleInfo" : function(obj) {
        var $this = $(obj);
        var url = $this.attr("href");
        var dialogSetting = {
            dialogInit: "#popRuleAddTable",
            width: 	800,
            height: 560,
            title: "会员信息完善规则",
            confirm: "保存",
            cancel: "关闭",
            confirmEvent: function(){BINOLMBMBM31.savePaper();},
            cancelEvent: function(){removeDialog("#popRuleAddTable");}
        };
        openDialog(dialogSetting);
        var callback = function(msg) {
            $("#popRuleAddTable").html(msg);
        };
        cherryAjaxRequest( {
            url : url,
            param: null,
            callback : callback,
            formId: '#mainForm'
        });
    },
    "popEditDialog":function(obj,isValid){
        var $this = $(obj);
        var url = $this.attr("href");
        var dialogSetting = {
            dialogInit: "#popRuleEditTable",
            width: 	800,
            height: 560,
            title: "编辑会员完善度规则",
            confirm: "保存",
            cancel: "关闭",
            confirmEvent: function(){BINOLMBMBM31.editRule(0,isValid);},
            cancelEvent: function(){removeDialog("#popRuleEditTable");}
        };
        openDialog(dialogSetting);
        var callback = function(msg) {
            $("#popRuleEditTable").html(msg);
        };
        cherryAjaxRequest( {
            url : url,
            callback : callback
            //formId: '#mainForm'
        });
    },
    "editRule" : function (newRuleFlag,isValid){
        //isValid为1表示区间范围内(活动进行中)
        //isValid为2表示区间范围的右侧（活动为进行，未来会进行）

        if(isValid == 2){
            if(!$('#editRuleForm').valid()) {
                return false;
            }
            $('#errorDiv').hide();
            // 获取会员属性个数
            var ruleNum = $("#rule_content").children().length;
            if(ruleNum < 1){
                BINOLMBMBM31.showErrorMsg("请至少添加一条信息完善属性");
                return false;
            }
            var isPointCorrect = 0;
            var totalPoint=0;
            var totalPercent=0;
            var isPercentCorrect=0;
            var ruleList = {};
            var pointGroupList = new Array();
            var noPointGroupList = new Array();
            $(".ruleDetail").each(function() {
                if ($(this).find("#percent").val().length==0){
                    isPercentCorrect=1;
                }
                if ($(this).find("#percent").val()) {
                    var q = {};
                    var keyName= $(this).find("#memberAttrListJson  option:selected").val();
                    var percentMap = {};
                    percentMap.percent =$(this).find("#percent").val();
                    percentMap.point =$(this).find("#point").val();
                    q[keyName] = percentMap;
                    if ($(this).find("#point").val()){
                        pointGroupList.push(q);
                    }else{
                        noPointGroupList.push(q);
                    }
                    if ($(this).find("#point").val()&&!BINOLMBMBM31.isPositiveNum($(this).find("#point").val())){
                        isPointCorrect=1;
                    }
                    if (!BINOLMBMBM31.isPositiveNum($(this).find("#percent").val())){
                        isPercentCorrect=1;
                    }
                    totalPoint+=Number($(this).find("#point").val());
                    totalPercent+=Number($(this).find("#percent").val());
                }

            });
            ruleList.pointGroup=pointGroupList;
            ruleList.noPointGroup=noPointGroupList;
            if(isPercentCorrect==1){
                BINOLMBMBM31.showErrorMsg("完成比例必须为正整数");
                return false;
            }
            if(isPointCorrect==1){
                BINOLMBMBM31.showErrorMsg("奖励积分必须为大于等于0的正整数");
                return false;
            }
            if(totalPercent!=100){
                BINOLMBMBM31.showErrorMsg("完成比例合计必须等于100%");
                return false;
            }

            var queStr = JSON2.stringify(ruleList);
            $("#ruleJsonSave ").val(queStr);
        }
        var param = "csrftoken=" + getTokenVal() + "&" + $("#editRuleForm").serialize()+"&newRuleFlag="+newRuleFlag;
        var url = $("#url_editRule").html();
        var opt = {
            url : url,
            param : param,
            callback : function(msg) {
                if(msg == null || msg == "" || msg == undefined || msg == "ERROR"){
                    return;
                }
                var param_map = eval("("+msg+")");

                var resultCode=param_map.resultCode;
                var resultMsg=param_map.resultMsg;
                if(resultCode=="-1"){
                    BINOLMBMBM31.showMessageDialog({
                        message:resultMsg,
                        type:"MESSAGE"
                    });
                    return false;
                }
                if(resultCode=="0"){
                    removeDialog("#popRuleEditTable");
                    BINOLMBMBM31.showMessageDialog({
                        message:"保存成功",
                        type:"MESSAGE"
                    });
                }
                BINOLMBMBM31.search();
            }
        };
        cherryAjaxRequest(opt);
    },
    "editInitRule" : function () {
        var numChoice = $("#rule_content input[id^=input_]").length;
        if (numChoice == 20) {
            return false;
        }
        var memberAttrList = $("#memberAttrList").val();
        var ruleConditionList=$("#ruleConditionList").val();
        var memberAttrListJson = eval('('+memberAttrList+')');
        var ruleConditionListJson = eval('('+ruleConditionList+')');

        $(ruleConditionListJson).each(function(i){
            var select =BINOLMBMBM31_code;
            var str = '<div id = "div_'
                +select+ '" class="clearfix ruleDetail">' ;
            if(memberAttrListJson && memberAttrListJson.length > 0) {
                str += '<select name="memberAttrListJson" id="memberAttrListJson">';
                $(memberAttrListJson).each(function(j){
                    str += '<option value="' + memberAttrListJson[j].memInfoKey + '"';
                    if(ruleConditionListJson[i].keyName == memberAttrListJson[j].memInfoKey){
                        str +=   'selected="selected"';
                    }
                    str +=   '>' + memberAttrListJson[j].memInfoValue ;
                    str +=   '</option>';
                });
                str += '</select>';
            }
            var point=ruleConditionListJson[i].point;
            if(point == "NULL"){
                point="";
            }
            str += '完成比例'
            str += '<span class="highlight">*</span>'
            str += '<input class="text" type="text" id="percent" name="percent" maxlength="3" value="'+ruleConditionListJson[i].percent+'"/>%';
            str += '奖励积分'
            str += '<input class="text" type="text" id="point" name="point" maxlength="6" value="'+point+'"/>';
            str +='<a id="delete" class="delete right" onclick="$(this).parent().remove();return false;" ><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a></div>'
            $("#rule_content").append(str);
            BINOLMBMBM31_code++;
        });

    }
};

var BINOLMBMBM31 = new BINOLMBMBM31();

$(document).ready(function() {
    $('#startTime').cherryDate({
        beforeShow: function(input){
            var value = $('#endTime').val();
            return [value,'maxDate'];
        }
    });
    $('#endTime').cherryDate({
        beforeShow: function(input){
            var value = $('#startTime').val();
            return [value,'minDate'];
        }
    });
    //$("#addRule").click(function() {
    //    BINOLMBMBM31.addRule();
    //});
});
