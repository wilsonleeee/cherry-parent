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
			brandId: {required: true},
			roleName: {required: true,maxlength: 50},
			roleKind: {required: true},
			decription: {maxlength: 100}
		}
	});
});
</script>

<div id="actionResultDisplay"></div>
<form id="roleForm">
<s:i18n name="i18n.pl.BINOLPLRLM01">
  <s:text name="select_default" id="select_default"></s:text>
  
  <table class="detail">
  	<tbody>
  		<tr>
  			<s:if test='%{brandInfoList != null && !brandInfoList.isEmpty()}'>
	  			<th>
	  				<s:text name="brandInfo"></s:text>
	  				<span class="highlight"><s:text name="global.page.required"></s:text></span>
	  			</th>
	  			<td>
	  				<s:select list="brandInfoList" listKey="brandInfoId" listValue="brandName" name="brandId" headerKey="" headerValue="%{#select_default}"></s:select>
	  			</td>
  			</s:if>
  		</tr>
  		<tr>
  			<th>
	  			<s:text name="role_name" />
	  			<span class="highlight"><s:text name="global.page.required"></s:text></span>
  			</th>
  			<td>
  				<span><s:textfield name="roleName" cssClass="text" maxlength="50"></s:textfield></span>
  				<span><s:fielderror fieldName="roleName" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror></span>
  			</td>
  		</tr>
  		<tr>
  			<th>
  				<s:text name="role_kind" />
  				<span class="highlight"><s:text name="global.page.required"></s:text></span>
  			</th>
  			<td>
  				<span><s:select list='#application.CodeTable.getCodes("1009")' listKey="CodeKey" listValue="Value" name="roleKind" headerKey="" headerValue="%{select_default}"></s:select></span>
  				<span><s:fielderror fieldName="roleKind" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror></span>
  			</td>
  		</tr>
  		<tr>
  			<th>
  				<s:text name="role_decription" />
  			</th>
  			<td>
  				<span><s:textarea name="decription" onpropertychange="return isMaxLen(this);" maxlength="100"></s:textarea></span>
    			<span><s:fielderror fieldName="decription" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror></span>
  			</td>
  		</tr>
  	</tbody>
  </table>
</s:i18n>  
</form>

