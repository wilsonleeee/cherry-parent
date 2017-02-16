<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%--<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>--%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%--<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>--%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
      type="text/css">
<%--<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>--%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
//    $().ready(function() {
//        $('#startTimeSave').cherryDate({
//            beforeShow: function(input){
//                var value = $('#endTimeSave').val();
//                return [value,'maxDate'];
//            }
//        });
//        $('#endTimeSave').cherryDate({
//            beforeShow: function(input){
//                var value = $('#startTimeSave').val();
//                return [value,'minDate'];
//            }
//        });
//        if (window.opener) {
//            window.opener.lockParentWindow();
//        }
//        cherryValidate({
//            formId: 'mainRuleForm',
//            rules: {
//                ruleNameSave: {required: true,maxlength: 20},
//                startTimeSave: {required: true,dateValid: true},
//                endTimeSave: {required: true,dateValid: true},
//                totalPointSave: {required: true,digits:true}
//            }
//        });
//    });
    window.onbeforeunload = function(){
        if (window.opener) {
            window.opener.unlockParentWindow();
        }
    };
</script>
<s:i18n name="i18n.mb.BINOLMBMBM31">
    <%-- ================== js国际化  START ======================= --%>
    <%--<jsp:include page="/WEB-INF/jsp/mo/cio/BINOLMOCIO10_1.jsp" flush="true" />--%>
    <%-- ================== js国际化导入    END  ======================= --%>
    <%--<div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix"><span class="breadcrumb left">
			<span class="ui-icon icon-breadcrumb"></span>
			<s:text name="MBM31_meminfoCompleteRule" />&gt;
			<span><s:text name="MBM31_addRuleButton" /></span> </span>
        </div>
    </div>
    <div id="actionResultDisplay"></div>--%>
    <%-- ================== 错误信息提示 START ======================= --%>
    <%--<div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>
    </div>--%>
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
                        <td colspan="3"><span><s:property value="rule_map.ruleName"/></span></td>
                    </tr>
                    <tr>
                        <th class="date"><s:text name="MBM31_startDate"></s:text><span class="highlight">*</span></th>
                        <td><span><s:property value="rule_map.startTime"/></span></td>
                        <th class="date"><s:text name="MBM31_endDate"></s:text><span class="highlight">*</span></th>
                        <td><span><s:property value="rule_map.endTime"/></span></td>
                    </tr>
                    <tr>
                        <th><s:text name="MBM31_totalPoint"/><span class="highlight">*</span></th>
                        <td><span><s:property value="rule_map.totalPoint"/></span></td>
                        <th><s:text name="MBM31_memo"/></th>
                        <td><span><s:property value="rule_map.memo"/></span></td>
                    </tr>
                    </tbody>
                </table>

            </form>
        </div>
        <hr class="space" />
        <div class="box4">
            <div class="box4-header clearfix" >
            </div>
            <div class="box4-content clearfix" >
                <div class="group-content1 clearfix" id = "rule_content">
                    <table class="detail">
                    <s:iterator value="%{ruleConditionList}" id="ruleCondition" status="i">
                        <tr>
                            <th style="width: 20%"><s:text name="MBM31_attr"/></th>
                            <td style="width: 15%"><s:property value="valueName"/></td>
                            <th style="width: 20%"><s:text name="MBM31_completePercent"/><span class="highlight">*</span></th>
                            <td style="width: 15%"><s:property value="percent"/>%</td>
                            <th style="width: 20%"><s:text name="MBM31_completePoint"/></th>
                            <td style="width: 15%"><s:property value="point"/></td>
                        </tr>
                    </s:iterator>
                    </table>
                </div>
            </div>
        </div>
        <hr class="space" />
    </div>
</s:i18n>
<span id="url_saveRule" style="display:none">${s_saveRule}</span>
