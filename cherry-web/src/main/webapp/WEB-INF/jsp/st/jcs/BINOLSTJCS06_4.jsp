<%-- 编辑--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTJCS06">
<div id="actionResultDisplay2"></div>
<hr class="space" />
<form id="main_form" method="post">
<input type="hidden" name="logInvId" value="<s:property value="getLogInv.logInvId"/>"/>
<table width="80%" class="detail" border="1" cellspacing="0" cellpadding="0">
  <%-- 逻辑仓库代码 --%>
  <tr id="logInvCode_tr">
    <th ><s:text name="JCS06.LogicInventoryCode"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td>
    	<span>
    		<s:hidden name="logInvCode" id="logInvCode" value="%{getLogInv.logInvCode}"></s:hidden>
    		<s:textfield id="logInvCode" name="logInvCode" cssClass="text" maxlength="10" cssStyle="width:250px;" value="%{getLogInv.logInvCode}" disabled="true"/>
   		</span>
 	</td>
  </tr>
  <%-- 逻辑仓库名称 --%>
  <tr id="logInvNameCN_tr">
    <th ><s:text name="JCS06.InventoryNameCN"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:textfield id="logInvNameCN" name="logInvNameCN" cssClass="text" maxlength="30" cssStyle="width:250px;" value="%{getLogInv.logInvNameCN}" /></span></td>
  </tr>
  <%-- 逻辑仓库名称 英文名--%>
  <tr id="logInvNameEN_tr">
    <th ><s:text name="JCS06.InventoryNameEN"/></th>
    <td><span><s:textfield id="logInvNameEN" name="logInvNameEN" cssClass="text" maxlength="30" cssStyle="width:250px;" value="%{getLogInv.logInvNameEN}" /></span></td>
  </tr>
  <%-- 描述--%>
  <tr id="comments_tr">
    <th ><s:text name="JCS06.Comments"/></th>
    <td><span><s:textfield id="comments" name="comments" cssClass="text" maxlength="15" cssStyle="width:250px;" value="%{getLogInv.comments}" /></span></td>
  </tr>
  <%-- 逻辑仓库类型--%>
  <tr id="type_tr">
    <th><s:text name="JCS06.Type"/></th>
    <td>
    	<span>
    		<s:hidden name="type" id="type" value="%{getLogInv.type}"></s:hidden>
    		<s:select cssStyle="width:150px" name="type" list="#application.CodeTable.getCodes('1143')" listKey="CodeKey" listValue="Value" value="%{getLogInv.type}" disabled="true"/>
   		</span>
  	</td>
  </tr>
  <%-- 排序--%>
  <tr id="orderNo_tr">
    <th ><s:text name="JCS06.OrderNO"/></th>
    <td><span><s:textfield id="orderNo" name="orderNo" cssClass="text" maxlength="10" cssStyle="width:95px;" value="%{getLogInv.orderNo}" /></span></td>
  </tr>
  <%-- 默认仓库--%>
  <tr id="defaultFlag_tr">
    <th><s:text name="JCS06.DefaultFlag"/></th>
    <td><span><s:select cssStyle="width:100px" name="defaultFlag" list="#application.CodeTable.getCodes('1135')" listKey="CodeKey" listValue="Value" value="%{getLogInv.defaultFlag}" /></span></td>
  </tr>
  <%-- 有效区分 --%>
  <tr id="validFlag_tr">
    <th><s:text name="JCS06.ValidFlag"/></th>
    <td><span><s:select cssStyle="width:100px" id="validFlagSel" name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value" value="%{getLogInv.validFlag}" /></span></td>
  </tr>
</table>
<input name="editFlag" type="hidden" value="1"></input>
<input type="hidden" name="modifyCount"  value="${getLogInv.modifyCount}"/>
<input type="hidden" name="updateTime"  value="${getLogInv.updateTime}"/>
</form>
<script type="text/javascript">
$(document).ready(function(){
	cherryValidate({			
		formId: "main_form",		
		rules: {
			logInvCode:{required: true},
			logInvNameCN:{required: true},
			validFlag: {required: true},
			orderNo: {digits:true}
		}		
	});
});
</script>
</s:i18n>