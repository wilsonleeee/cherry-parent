<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript">

$(function(){

	var mode = '${mode}';
	if(mode == '1') {
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				searchKey: {required: true}
			}
		});
	} else if(mode == '2') {
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				searchKey: {required: true, iCardValid: true}
			}
		});
	} else if(mode == '3') {
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				searchKey: {required: true, phoneValid: $("#mobileRule").val()}
			}
		});
	}
});
</script>

<div id="actionResultDisplay"></div>
<form id="saveForm">
<s:hidden name="mode"></s:hidden>
<s:i18n name="i18n.wp.BINOLWOSET01">
  <table class="detail">
  	<tbody>
  		<tr>
  			<s:if test='%{"1".equals(mode)}'>
  			<th><s:text name="WOSET01_employeeCode" /></th>
  			</s:if>
  			<s:elseif test='%{"2".equals(mode)}'>
  			<th><s:text name="WOSET01_identityCard" /></th>
  			</s:elseif>
  			<s:elseif test='%{"3".equals(mode)}'>
  			<th><s:text name="WOSET01_mobilePhone" /></th>
  			</s:elseif>
  			<td><span><s:textfield name="searchKey" cssClass="text"></s:textfield></span></td>
  		</tr>
  	</tbody>
  </table>
</s:i18n>  
</form>

