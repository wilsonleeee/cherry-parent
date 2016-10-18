<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript">

var mode = '${mode}';
$(function(){
	if(mode == '2') {
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				identityCard: {required: true, iCardValid: true},
				employeeName: {required: true, maxlength: 128},
				mobilePhone: {phoneValid: $("#mobileRule").val()}
			}
		});
	} else if(mode == '3') {
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				mobilePhone: {required: true, phoneValid: $("#mobileRule").val()},
				employeeName: {required: true, maxlength: 128},
				identityCard: {iCardValid: true}
			}
		});
	}
});
</script>
<s:i18n name="i18n.wp.BINOLWOSET01">

<s:if test="%{baInfo != null}">
<div id="actionResultDisplay"></div>
<form id="saveForm">
<s:hidden name="employeeId" value="%{baInfo.employeeId}"></s:hidden>
<s:hidden name="mode"></s:hidden>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th><s:text name="WOSET01_employeeCode" /></th>
  			<td><span><s:property value="baInfo.employeeCode"/></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_employeeName" /></th>
  			<td><span><s:property value="baInfo.employeeName"/></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_identityCard" /></th>
  			<td><span><s:property value="baInfo.identityCard"/></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_mobilePhone" /></th>
  			<td><span><s:property value="baInfo.mobilePhone"/></span></td>
  		</tr>
  	</tbody>
  </table> 
</form>
</s:if>
<s:else>
<s:if test='%{"1".equals(mode)}'>
<s:text name="WOSET01_error1"></s:text>
</s:if>
<s:elseif test='%{"2".equals(mode)}'>
<div id="actionResultDisplay"></div>
<form id="saveForm">
<s:hidden name="mode"></s:hidden>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th><s:text name="WOSET01_identityCard" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="identityCard" cssClass="text"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_employeeName" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="employeeName" cssClass="text"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_mobilePhone" /></th>
  			<td><span><s:textfield name="mobilePhone" cssClass="text"></s:textfield></span></td>
  		</tr>
  	</tbody>
  </table>
</form>
</s:elseif>
<s:elseif test='%{"3".equals(mode)}'>
<div id="actionResultDisplay"></div>
<form id="saveForm">
<s:hidden name="mode"></s:hidden>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th><s:text name="WOSET01_mobilePhone" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="mobilePhone" cssClass="text"></s:textfield></span></td>
  		</tr>	
  		<tr>
  			<th><s:text name="WOSET01_employeeName" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="employeeName" cssClass="text"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_identityCard" /></th>
  			<td><span><s:textfield name="identityCard" cssClass="text"></s:textfield></span></td>
  		</tr>
  	</tbody>
  </table> 
</form>
</s:elseif>
</s:else>

</s:i18n>
