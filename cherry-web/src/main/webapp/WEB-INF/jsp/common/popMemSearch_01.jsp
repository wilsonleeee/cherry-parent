<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 

<script type="text/javascript">
$(function(){
	// 表单验证初期化
	cherryValidate({
		formId: 'searchRequestForm',
		rules: {
			requestName: {required: true, maxlength: 50},
			description: {maxlength: 300}
		}
	});
});
</script>

<div id="actionResultDisplay"></div>
<cherry:form id="searchRequestForm" csrftoken="false">
  <table class="detail">
  	<tbody>
  		<tr>
  			<th>
	  			<s:text name="global.page.saveConditionName"></s:text>
	  			<span class="highlight"><s:text name="global.page.required"></s:text></span>
  			</th>
  			<td>
  				<span><s:textfield name="requestName" cssClass="text" maxlength="50"></s:textfield></span>
  				<span><s:fielderror fieldName="requestName" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror></span>
  			</td>
  		</tr>
  		<tr>
  			<th>
  				<s:text name="global.page.saveConditionDetail"></s:text>
  			</th>
  			<td>
  				<span><s:textarea name="description" onpropertychange="return isMaxLen(this);" maxlength="300"></s:textarea></span>
    			<span><s:fielderror fieldName="description" cssClass="ui-icon icon-error tooltip-trigger-error"></s:fielderror></span>
  			</td>
  		</tr>
  	</tbody>
  </table>
</cherry:form>

<div class="hide">
<div id="saveCondition"><s:text name="global.page.saveCondition" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<s:url action="BINOLCM33_add" id="addSearchRequestUrl"></s:url>
<a href="${addSearchRequestUrl }" id="addSearchRequestUrl"></a>
</div>



