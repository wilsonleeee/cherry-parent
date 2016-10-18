<%-- 新增 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTJCS06">
<div id="actionResultDisplay2"></div>
<hr class="space" />
<form id="addForm" class="inline" method="post">
  <table width="100%" class="detail" border="1" cellspacing="0" cellpadding="0">
  <%-- 逻辑仓库代码 --%>
  <tr id="logInvCode_tr">
    <th ><s:text name="JCS06.LogicInventoryCode"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:textfield id="logInvCode" name="logInvCode" cssClass="text" maxlength="10" cssStyle="width:250px;"/></span></td>
  </tr>
  <%-- 逻辑仓库名称 --%>
  <tr id="logInvNameCN_tr">
    <th ><s:text name="JCS06.InventoryNameCN"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:textfield id="logInvNameCN" name="logInvNameCN" cssClass="text" maxlength="30" cssStyle="width:250px;"/></span></td>
  </tr>
  <%-- 逻辑仓库名称 英文名--%>
  <tr id="logInvNameEN_tr">
    <th ><s:text name="JCS06.InventoryNameEN"/></th>
    <td><span><s:textfield id="logInvNameEN" name="logInvNameEN" cssClass="text" maxlength="30" cssStyle="width:250px;"/></span></td>
  </tr>
  <%-- 描述--%>
  <tr id="comments_tr">
    <th ><s:text name="JCS06.Comments"/></th>
    <td><span><s:textfield id="comments" name="comments" cssClass="text" maxlength="15" cssStyle="width:250px;"/></span></td>
  </tr>
  <%-- 逻辑仓库类型--%>
  <tr id="type_tr">
    <th><s:text name="JCS06.Type"/></th>
    <td><span><s:select cssStyle="width:150px" name="type" list="#application.CodeTable.getCodes('1143')" listKey="CodeKey" listValue="Value" /></span></td>
  </tr>
  <%-- 排序--%>
  <tr id="orderNo_tr">
    <th ><s:text name="JCS06.OrderNO"/></th>
    <td><span><s:textfield id="orderNo" name="orderNo" cssClass="text" maxlength="3" cssStyle="width:95px;"/></span></td>
  </tr>
  <%-- 默认仓库--%>
  <tr id="defaultFlag_tr">
    <th><s:text name="JCS06.DefaultFlag"/></th>
    <td><span><s:select cssStyle="width:100px" name="defaultFlag" list="#application.CodeTable.getCodes('1135')" listKey="CodeKey" listValue="Value" /></span></td>
  </tr>
</table>
</form>

<script type="text/javascript">
$(document).ready(function(){
	cherryValidate({
		formId: "addForm",
		rules: {
			logInvCode:{required: true},
			logInvNameCN:{required: true},
			orderNo: {digits:true}
		}		
	});
});
</script>
</s:i18n>