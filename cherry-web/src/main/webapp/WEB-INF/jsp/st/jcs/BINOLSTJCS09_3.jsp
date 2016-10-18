<%-- 新增 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTJCS09">
<div id="actionResultDisplay2"></div>
<hr class="space" />
<form id="addForm" class="inline" method="post">
  <table width="100%" class="detail" border="1" cellspacing="0" cellpadding="0">
  <tr id="logicType_tr">
    <th ><s:text name="JCS09_logicType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:select cssStyle="width:150px" name="logicType" list="#application.CodeTable.getCodes('1181')" listKey="CodeKey" listValue="Value" onchange="BINOLSTJCS09.getLogiDepotByAjax('addForm');return false;"/></span>
	    <span>
				<span class="highlight">※</span>
				<span class="gray"><s:text name="JSC09_logicTypeWarm"></s:text></span>
	    </span>
    </td>
  </tr>
  <tr id="productType_tr">
    <th><s:text name="JCS09_productType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:select cssStyle="width:150px" name="productType" list="#application.CodeTable.getCodes('1134')" listKey="CodeKey" listValue="Value" /></span></td>
  </tr>
  <%--
  <tr id="businessType_tr">
    <td><span style="float:right;"><s:text name="JCS09_businessType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></span></td>
    <td><span><s:select cssStyle="width:150px" name="businessType" list="#application.CodeTable.getCodes('1133')" listKey="CodeKey" listValue="Value" onchange="BINOLSTJCS09.getLogiDepotByAjax('addForm');return false;"/></span></td>
  </tr>
   --%>
  <tr id="logicInvId_tr_0">
  	<th><s:text name="JCS09_logicInventoryInfoId"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td>
    	<span>
    		<select style="width:150px" name="logicInvId" id="logicInvId" ></select>
    	</span>
    </td>
  </tr>
  <%-- 
  <tr>
    <td><span style="float:right;"><s:text name="JCS09_inOutFlag"/><span class="highlight"><s:text name="global.page.required"></s:text></span></span></td>
    <td><span><s:select cssStyle="width:150px" name="inOutFlag" list="#application.CodeTable.getCodes('1025')" listKey="CodeKey" listValue="Value" /></span></td>
  </tr>
  --%>
  <tr>
    <th><s:text name="JCS09_configOrder"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
       <td><span><s:textfield id="configOrder" name="configOrder" cssClass="text" maxlength="3" cssStyle="width:95px;"/></span>
       <span>
			<span class="highlight">※</span>
			<span class="gray"><s:text name="JSC09_warm"></s:text></span>
	   </span>
    </td>
  </tr>
  <tr>
    <th><s:text name="JCS09_comments"/></th>
    <td><s:textfield id="comments" name="comments" cssClass="text" maxlength="15" cssStyle="width:300px;"/></td>
  </tr>
</table>
</form>

<script type="text/javascript">
$(document).ready(function(){
	cherryValidate({
		formId: "addForm",
		rules: {
			logicType:{required: true},
			productType:{required: true},
			businessType:{required: true},
			logicInvId:{required: true,digits:true},
			configOrder: {required: true,digits:true}
		}		
	});
});
</script>
</s:i18n>