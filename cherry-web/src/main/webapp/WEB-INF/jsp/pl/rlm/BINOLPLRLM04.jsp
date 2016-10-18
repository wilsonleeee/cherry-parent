<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript">

$(function(){

	// 表单验证初期化
	cherryValidate({
		formId: 'roleForm',
		rules: {
			roleName: {required: true,maxlength: 50},
			roleKind: {required: true},
			decription: {maxlength: 100}
		}
	});
});
</script>
<div id="actionResultDisplay"></div>
<s:if test="roleInfo != null">
<form id="roleForm">
<s:i18n name="i18n.pl.BINOLPLRLM01">
  <s:hidden name="brandInfoId" value="%{roleInfo.brandInfoId}"></s:hidden>
  <s:hidden name="roleId"></s:hidden>
  <s:hidden name="modifyTime" value="%{roleInfo.updateTime}"></s:hidden>
  <s:hidden name="modifyCount" value="%{roleInfo.modifyCount}"></s:hidden>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th>
  				<s:text name="role_name" />
  				<span class="highlight"><s:text name="global.page.required"></s:text></span>
  			</th>
  			<td>
  				<span>
  					<s:textfield name="roleName" cssClass="text" value="%{roleInfo.roleName}" maxlength="50"></s:textfield>
  				</span>
  				<span>
  					<s:fielderror fieldName="roleName" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror>
 				</span>
 			</td>
  		</tr>
  		<tr>
  			<th>
  				<s:text name="role_kind" />
  				<span class="highlight"><s:text name="global.page.required"></s:text></span>
  			</th>
  			<td>
  				<span>
	  				<s:text name="select_default" id="select_default"/>
	  				<s:select list='#application.CodeTable.getCodes("1009")' listKey="CodeKey" listValue="Value" name="roleKind" headerKey="" headerValue="%{select_default}" value="%{roleInfo.roleKind}"></s:select>
			    </span>
			    <span>
			    	<s:fielderror fieldName="roleKind" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror>
  				</span>
  			</td>
  		</tr>
  		<tr>
  			<th>
  				<s:text name="role_decription" />
  			</th>
  			<td>
  				<span>
  					<s:textarea name="decription" value="%{roleInfo.decription}" onpropertychange="return isMaxLen(this);" maxlength="100"></s:textarea>
   				</span>
   				<span>
   					<s:fielderror fieldName="decription" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror>
  				</span>
  			</td>
  		</tr>
  	</tbody>
  </table>
</s:i18n>  
</form>
</s:if>
<s:else>
<s:i18n name="i18n.message.message">
<p class="message"><span><s:text name="EPL00001" /></span></p>
</s:i18n>
</s:else>
