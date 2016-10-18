<%-- 编辑--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.st.BINOLSTJCS09">
<div id="actionResultDisplay2"></div>
<hr class="space" />
<form id="main_form" method="post">
	<input type="hidden" name="logicDepotId" value="<s:property value="getLogicDepot.logicDepotId"/>"/>
 <table width="80%" class="detail" border="1" cellspacing="0" cellpadding="0">
  
  <tr id="logicType_tr">
    <th><s:text name="JCS09_logicType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td>
    	<span>
    		<s:hidden name="logicType" id="logicType" value="%{getLogicDepot.logicType}"></s:hidden>
    		<s:select cssStyle="width:150px" name="logicType_select" list="#application.CodeTable.getCodes('1181')" listKey="CodeKey" listValue="Value" value="%{getLogicDepot.logicType}" disabled="true" onchange="BINOLSTJCS09.getLogiDepotByAjax('main_form',true);return false;"/>
    	</span>
	    <span>
				<span class="highlight">※</span>
				<span class="gray"><s:text name="JSC09_logicTypeWarm"></s:text></span>
	    </span>
    </td>
  </tr>
  <tr id="productType_tr">
    <th><s:text name="JCS09_productType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td><span><s:select cssStyle="width:150px" name="productType" list="#application.CodeTable.getCodes('1134')" listKey="CodeKey" listValue="Value" value="%{getLogicDepot.productType}" /></span></td>
  </tr>
  <input name="editedProductType" type="hidden" value="<s:property value='getLogicDepot.productType'/>"></input>
  <%-- 业务类型 --%>
  <tr id="businessType_tr">
    <th><s:text name="JCS09_businessType"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
    <td>
    	<span>
    		<s:hidden name="businessType" id="businessType" value="%{getLogicDepot.businessType}"></s:hidden>
		    <s:if test='getLogicDepot.logicType == "0"'>
    			<s:select cssStyle="width:150px" name="businessType_select" list="#application.CodeTable.getCodes('1133')" listKey="CodeKey" listValue="Value" value="%{getLogicDepot.businessType}" disabled="true" onchange="BINOLSTJCS09.getLogiDepotByAjax('addForm');return false;"/>
		    </s:if>
		    <s:else>
    			<s:select cssStyle="width:150px" name="businessType_select" list="#application.CodeTable.getCodes('1184')" listKey="CodeKey" listValue="Value" value="%{getLogicDepot.businessType}" disabled="true" onchange="BINOLSTJCS09.getLogiDepotByAjax('addForm');return false;"/>
		    </s:else>
    	</span>
    </td>
  </tr>
  <s:if test = "'OD'.equals(getLogicDepot.businessType)">
  	 <tr id="subType_tr">
	    <th><s:text name="JSC09_subType"/></th>
	    <s:text name="JSC09_pleaseSelect" id="JSC09_pleaseSelect"></s:text>
	    <td><span><s:select cssStyle="width:150px" name="subType" id="subType" list="#application.CodeTable.getCodes('1168')" listKey="CodeKey" listValue="Value" value="getLogicDepot.subType" /></span></td>
	  </tr>
	  <input name="editedSubType" type="hidden" value="<s:property value='getLogicDepot.subType'/>"></input>
  </s:if>
  <!--  -->
  <tr id="logicInvId_tr_0">
     <th> <s:text name="JCS09_logicInventoryInfoId"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	    <td>
	      <span>
	        <s:select name="logicInvId" id="logicInvId" cssStyle="width:150px" list="businessLogicDepotsList" listKey="BIN_LogicInventoryInfoID" listValue="InventoryCodeName" value="%{getLogicDepot.logicInvId}"></s:select>
	      </span>
	 </td>
  </tr>
  <%-- 
  <tr>
    <td><span style="float:right;"><s:text name="JCS09_inOutFlag"/><span class="highlight"><s:text name="global.page.required"></s:text></span></span></td>
    <td>
        <span>
          <s:select cssStyle="width:150px" name="inOutFlag" list="#application.CodeTable.getCodes('1025')" listKey="CodeKey" listValue="Value" value="%{getLogicDepot.inOutFlag}" />
        </span>
    </td>
  </tr>
  --%>
  <tr>
    <th><s:text name="JCS09_configOrder"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
       <td><span><s:textfield name="configOrder" cssClass="text" maxlength="3" cssStyle="width:95px;" value="%{getLogicDepot.configOrder}"/></span>
       <span>
		<span class="highlight">※</span>
		<span class="gray"><s:text name="JSC09_warm"></s:text></span>
		
	   </span>
	   </td>
  </tr>
  <tr>
    <th><s:text name="JCS09_comments"/></th>
    <td><s:textfield name="comments" cssClass="text" maxlength="30" cssStyle="width:300px;" value="%{getLogicDepot.comments}"/></td>
  </tr>
</table>
<input name="editFlag" type="hidden" value="1"></input>
<input type="hidden" name="modifyCount"  value="${getLogicDepot.modifyCount}"/>
<input type="hidden" name="updateTime"  value="${getLogicDepot.updateTime}"/>
</form>
<script type="text/javascript">
$(document).ready(function(){
	cherryValidate({			
		formId: "main_form",		
		rules: {
			logicType: {required: true,digits:true},
			productType:{required: true,digits:true},
			businessType:{required: true},
			logicInvId:{required: true,digits:true},
			configOrder: {required: true,digits:true}
		}		
	});
});
</script>
</s:i18n>