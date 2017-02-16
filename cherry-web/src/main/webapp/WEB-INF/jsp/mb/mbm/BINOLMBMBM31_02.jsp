<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
      type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
    $().ready(function() {
        $('#startTimeSave').cherryDate({
            beforeShow: function(input){
                var value = $('#endTimeSave').val();
                return [value,'maxDate'];
            }
        });
        $('#endTimeSave').cherryDate({
            beforeShow: function(input){
                var value = $('#startTimeSave').val();
                return [value,'minDate'];
            }
        });
        cherryValidate({
            formId: 'mainRuleForm',
            rules: {
                ruleNameSave: {required: true,maxlength: 20},
                startTimeSave: {required: true,dateValid: true},
                endTimeSave: {required: true,dateValid: true},
                totalPointSave: {required: true,digits:true}
            }
        });
    });

</script>
<s:i18n name="i18n.mb.BINOLMBMBM31">
    <div class="panel ui-corner-all">
    <div id="actionResultDisplay"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div>
            <form id="mainRuleForm" class="inline">
                <s:url action="BINOLMBMBM31_saveIsMemCompleteRule" id="s_saveRule"></s:url>
                <table class="detail">
                    <tbody>
                    <tr>
                        <!-- 规则名称 -->
                        <th><s:text name="MBM31_ruleName"/><span class="highlight">*</span></th>
                        <td colspan="3"><span><input class="text" type="text" id ="ruleNameSave" maxlength="20" style="width: 280px;" name="ruleNameSave"/></span></td>
                    </tr>
                    <tr>
                        <th class="date"><s:text name="MBM31_startDate"></s:text><span class="highlight">*</span></th>
                        <td><span><s:textfield id="startTimeSave" name="startTimeSave" cssClass="date"  cssStyle="width:80px"/></span></td>
                        <th class="date"><s:text name="MBM31_endDate"></s:text><span class="highlight">*</span></th>
                        <td><span><s:textfield id="endTimeSave" name="endTimeSave" cssClass="date" cssStyle="width:80px"/></span></td>
                    </tr>
                    <tr>
                        <th><s:text name="MBM31_totalPoint"/><span class="highlight">*</span></th>
                        <td><span><input id="totalPointSave" class="text" type="text" name ="totalPointSave" maxlength="6"/></span></td>
                        <th><s:text name="MBM31_memo"/></th>
                        <td><span><input id="memoSave" class="text" type="text" name ="memoSave" maxlength="10" /></span></td>
                    </tr>
                    </tbody>
                </table>
                <div>
                    <input type = "text" name = "ruleJsonSave" id="ruleJsonSave" value="" style="display:none" />
                    <input type="hidden" id="memberAttrList" value='<s:property value='memberAttrList' />'/>
                </div>

            </form>
        </div>
        <hr class="space" />
        <div class="box4">
            <div class="box4-header clearfix" >
            </div>
            <div class="box4-content clearfix" id = "addRule_body" >
                <div class="clearfix">
                    <div class="right" >
                        <a class="add right" id="addRule" onclick="BINOLMBMBM31.addRule()">
                            <span class="ui-icon icon-add"></span><span class="button-text"><s:text name="MBM31_add"/></span>
                        </a>
                    </div>
                </div>
                <br/>
                <div class="group-content1 clearfix" id = "rule_content">
                </div>
            </div>
        </div>
        <hr class="space" />
        <%--<div class="center clearfix">--%>
            <%--&lt;%&ndash; 保存 &ndash;%&gt;--%>
            <%--<button class="save" id ="savePaper" onclick="BINOLMBMBM31.savePaper();"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"/></span></button>--%>
            <%--<button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>--%>
        <%--</div>--%>
    </div>
    <div id="addRule_dialog_main">
        <div id="addRule_dialog"  class="hide">
                <%-- ================== 错误信息提示 START ======================= --%>
            <div id="errorDiv1" class="actionError" style="display:none">
                <ul>
                    <li><span id="errorSpan1"></span></li>
                </ul>
            </div>
            <div id="errorDiv2" class="actionError" style="display:none">
                <ul>
                    <li><span id="errorSpan2"></span></li>
                </ul>
            </div>
        <div id="optionDiv">
    </div>
    </div>
</s:i18n>
<span id="url_saveRule" style="display:none">${s_saveRule}</span>
<%--错误信息提示 --%>
<%--
<div id="errmessage" style="display:none">
    <div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
        <p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
        <p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
        <p class="center">
            <button id="btnMessageConfirm" class="close" type="button">
                <span class="ui-icon icon-confirm"></span>
                <span class="button-text">确定</span>
            </button>
        </p>
    </div>
</div>--%>
