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
            formId: 'editRuleForm',
            rules: {
                ruleNameSave: {required: true,maxlength: 20},
                startTimeSave: {required: true,dateValid: true},
                endTimeSave: {required: true,dateValid: true},
                totalPointSave: {required: true,digits:true}
            }
        });
    });
    BINOLMBMBM31.editInitRule();
</script>
<s:i18n name="i18n.mb.BINOLMBMBM31">
    <div class="panel ui-corner-all">
 <%--   <div id="actionResultDisplay"></div>
    &lt;%&ndash; ================== 错误信息提示 START ======================= &ndash;%&gt;
    <div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>--%>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div>
            <form id="editRuleForm" class="inline">
                <s:url action="BINOLMBMBM31_editIsMemCompleteRule" id="s_editRule"></s:url>
                <input class="hide" name="completeDegreeRuleID" value='<s:property value="rule_map.completeDegreeRuleID"/> '/>
                <table class="detail">
                    <tbody>
                    <tr>
                        <!-- 规则名称 -->
                        <th><s:text name="MBM31_ruleName"/><span class="highlight">*</span></th>
                        <td colspan="3"><span><input class="text" type="text" id ="ruleNameSave" maxlength="20" style="width: 280px;" name="ruleNameSave" value='<s:property value="rule_map.ruleName"/>'/></span></td>
                    </tr>
                    <tr>
                        <th class="date"><s:text name="MBM31_startDate"></s:text><span class="highlight">*</span></th>
                        <td><span><s:textfield id="startTimeSave" name="startTimeSave" cssClass="date"  cssStyle="width:80px"  value="%{rule_map.startTime}"/></span></td>
                        <th class="date"><s:text name="MBM31_endDate"></s:text><span class="highlight">*</span></th>
                        <td><span><s:textfield id="endTimeSave" name="endTimeSave" cssClass="date" cssStyle="width:80px" value="%{rule_map.endTime}" /></span></td>
                    </tr>
                    <tr>
                        <th><s:text name="MBM31_totalPoint"/><span class="highlight">*</span></th>
                        <td><span><input id="totalPointSave" class="text" type="text" name ="totalPointSave" value='<s:property value="rule_map.totalPoint"/>'/></span></td>
                        <th><s:text name="MBM31_memo"/></th>
                        <td><span><input id="memoSave" class="text" type="text" name ="memoSave" maxlength="10" value='<s:property value="rule_map.memo"/>'/></span></td>
                    </tr>
                    </tbody>
                </table>
                <div>
                    <input type = "text" name = "ruleJsonSave" id="ruleJsonSave" value="" style="display:none" />
                    <input type="hidden" id="memberAttrList" value='<s:property value='memberAttrList' />'/>
                    <input type="hidden" id="ruleConditionList" value='<s:property value='ruleConditionListJson' />'/>
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
<span id="url_editRule" style="display:none">${s_editRule}</span>