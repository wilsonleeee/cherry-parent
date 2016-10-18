<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:url action="BINOLCM26_searchCodeByType" id="searchCodeByTypeUrl"></s:url>
<s:url action="BINOLCM02_initUserDialog" namespace="/common" id="searchUserInitUrl"></s:url>
<s:url action="BINOLCM02_initAllDepartDialog" namespace="/common" id="searchDepartInitUrl"></s:url>
<s:url action="BINOLCM26_save" id="saveAuditorUrl"></s:url>
<script type="text/javascript">
$(function(){
// 表单验证配置
cherryValidate({                        
    formId: 'modifyAudiorForm',
    rules: {
        auditorType:{required: true},
        auditorID:{required: true}
    }
});
});
var popModifyAuditor_js_i18n={
		selectBtn:'<s:text name="global.page.Popselect"/>',
		selectPlease:'<s:text name="global.page.select"/>'
		};
</script>
<div class="hide">
    <a id="searchCodeByTypeUrl" href="${searchCodeByTypeUrl}"></a>
    <a id="saveAuditorUrl" href="${saveAuditorUrl}"></a>
    <a id="searchUserInitUrl" href="${searchUserInitUrl}"></a>
    <a id="searchDepartInitUrl" href="${searchDepartInitUrl}"></a>
</div>
<%--修改参与者--%>
<div id="modifyAuditor" class="">
    <form id="modifyAudiorForm" method="post">
        <input type="hidden" id="ruleType" name="ruleType" value="<s:property value='ruleType'/>"/>
        <table style="margin:auto; width:100%;" class="detail">
            <tr>
                <th><s:text name="global.page.operationbusiness"/></th><%--当前业务操作--%>
                <td><s:property value="currentOperateName"/></td>
            </tr>
            <tr>
                <th><s:text name="global.page.participanttype"/><span class="highlight">*</span></th><%--参与者身份类型--%>
                <td>
                    <span>
                    <select id="auditorType" name="auditorType" onchange="modifyAuditor.searchCodeByType();">
                        <option value=""><s:text name="global.page.select"/></option>
                        <option value="1"><s:text name="global.page.user"/></option>
                        <option value="2"><s:text name="global.page.employeePost"/></option>
                        <option value="3"><s:text name="global.page.Employeedepart"/></option>
                    </select>
                    </span>
                </td>
            </tr>
            <tr>
                <th><s:text name="global.page.Participant"/><span class="highlight">*</span></th>
                <td>
                    <span id="auditorIdSpan">
                    <select id="auditorID" name="auditorID" onchange="">
                        <option value=""><s:text name="global.page.select"/></option>
                    </select>
                    </span>
                </td>
            </tr>
            <tr id="trPrivilegeRelationship" class="hide">
                <th><s:text name="os.navigation.PrivilegeRelationship"/><span class="highlight">*</span></th>
                <td>
                    <select id="privilegeFlag" name="privilegeFlag" >
                        <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_FOLLOW"/>"><s:text name="os.navigation.PrivilegeFollow"/></option>
                        <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_LIKE"/>"><s:text name="os.navigation.PrivilegeLike"/></option>
                        <option value="<s:property value="@com.cherry.cm.core.CherryConstants@OS_PRIVILEGEFLAG_ALL"/>"><s:text name="os.navigation.PrivilegeALL"/></option>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>