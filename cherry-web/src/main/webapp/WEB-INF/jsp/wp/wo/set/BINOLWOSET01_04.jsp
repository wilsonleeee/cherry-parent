<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript">

var mode = '${mode}';
$(function(){
	if(mode == '1') {
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				employeeName: {required: true, maxlength: 128},
				identityCard: {iCardValid: true},
				mobilePhone: {phoneValid: $("#mobileRule").val()}
			}
		});
	} else if(mode == '2') {
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
<s:hidden name="oldIdentityCard" value="%{baInfo.identityCard}"></s:hidden>
<s:hidden name="oldMobilePhone" value="%{baInfo.mobilePhone}"></s:hidden>
<s:hidden name="mode"></s:hidden>

<s:if test='%{"1".equals(mode)}'>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th><s:text name="WOSET01_employeeName" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="employeeName" cssClass="text" value="%{baInfo.employeeName }"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_identityCard" /></th>
  			<td><span><s:textfield name="identityCard" cssClass="text" value="%{baInfo.identityCard }"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_mobilePhone" /></th>
  			<td><span><s:textfield name="mobilePhone" cssClass="text" value="%{baInfo.mobilePhone }"></s:textfield></span></td>
  		</tr>
  	</tbody>
  </table>
</s:if>
<s:elseif test='%{"2".equals(mode)}'>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th><s:text name="WOSET01_identityCard" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="identityCard" cssClass="text" value="%{baInfo.identityCard }"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_employeeName" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="employeeName" cssClass="text" value="%{baInfo.employeeName }"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_mobilePhone" /></th>
  			<td><span><s:textfield name="mobilePhone" cssClass="text" value="%{baInfo.mobilePhone }"></s:textfield></span></td>
  		</tr>
  	</tbody>
  </table>
</s:elseif>
<s:elseif test='%{"3".equals(mode)}'>
  <table class="detail">
  	<tbody>
  		<tr>
  			<th><s:text name="WOSET01_mobilePhone" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="mobilePhone" cssClass="text" value="%{baInfo.mobilePhone }"></s:textfield></span></td>
  		</tr>	
  		<tr>
  			<th><s:text name="WOSET01_employeeName" /><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  			<td><span><s:textfield name="employeeName" cssClass="text" value="%{baInfo.employeeName }"></s:textfield></span></td>
  		</tr>
  		<tr>
  			<th><s:text name="WOSET01_identityCard" /></th>
  			<td><span><s:textfield name="identityCard" cssClass="text" value="%{baInfo.identityCard }"></s:textfield></span></td>
  		</tr>
  	</tbody>
  </table> 
</s:elseif>
  
</form>
</s:if>
<s:else>
<s:text name="WOSET01_error1"></s:text>
</s:else>

</s:i18n>
