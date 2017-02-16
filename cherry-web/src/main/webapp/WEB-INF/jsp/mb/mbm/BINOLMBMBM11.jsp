<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>
<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>
<s:set id="QUESTIONTYPE_ESSAY"><%=MonitorConstants.QUESTIONTYPE_ESSAY %></s:set>
<s:set id="QUESTIONTYPE_APFILL"><%=MonitorConstants.QUESTIONTYPE_APFILL %></s:set>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/common/BINOLMBCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM11.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#birth').cherryDate({yearRange: 'c-100:c'});
	$('#joinDate').cherryDate({yearRange: 'c-100:c',maxDate:'${sysDate}'});
});
</script>

<s:i18n name="i18n.mb.BINOLMBMBM11">
<s:text name="global.page.select" id="select_default"/>
<s:text name="binolmbmbm11_selEmpErrorMsg" id="selEmpErrorMsg"></s:text>
<div class="main container clearfix">
<div class="panel ui-corner-all">
  <div class="panel-header">
    <div class="clearfix">
      <span class="breadcrumb left"> 
        <span class="ui-icon icon-breadcrumb"></span>
		<s:text name="binolmbmbm11_title"/> &gt; <s:text name="binolmbmbm11_addMem"/>
      </span>
    </div>
  </div>
  
  <div id="actionResultDisplay"></div>
  
  <div class="panel-content clearfix">
  <form id="mainForm" class="inline">
	<s:hidden name="memLevelConfirm"></s:hidden>
	<div class="box2 box2-active" style="margin:0 0 10px 0;">
      <div class="box2-header clearfix">
      	<strong class="left active">
      	  <s:text name="global.page.title"/>
      	</strong>
      	<span class="gray" style="display: inline-block; margin: 3px 0 0 10px;"><s:text name="binolmbmbm11_remark"></s:text></span>
      </div>
      <div class="box2-content clearfix" style="padding:10px 10px 0;">
      	<table class="detail" cellpadding="0" cellspacing="0">
      	  <tr>
      	    <th><s:text name="binolmbmbm11_memCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    <td><span><s:textfield name="memCode" cssClass="text" maxlength="20"></s:textfield></span></td>
		    <th><s:text name="binolmbmbm11_memberLevel"></s:text></th>
		    <td><span><s:select list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memberLevel" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_nickname"></s:text></th>
		    <td><span><s:textfield name="nickname" cssClass="text" maxlength="30"></s:textfield></span></td>
		    <th><s:text name="binolmbmbm11_creditRating"></s:text></th>
		    <td><span><s:select list='#application.CodeTable.getCodes("1317")' listKey="CodeKey" listValue="Value" name="creditRating" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_name"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			<td><span><s:textfield name="memName" cssClass="text" maxlength="30"></s:textfield></span></td>
		    <th><s:text name="binolmbmbm11_counterCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    <td>
		    <span>
		    <s:hidden name="organizationId"></s:hidden>
		    <s:hidden name="organizationCode"></s:hidden>
		    <s:hidden name="counterKind"></s:hidden>
		    <span id="counterDiv" style="line-height: 18px;"></span>
		    <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
		    <a class="add" onclick="binolmbcom01.popCounterList('${searchCounterInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		    </span>
		    </td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_mobilePhone"></s:text></th>
			<td><span><s:textfield name="mobilePhone" cssClass="text" maxlength="20"></s:textfield></span></td>
		    <th><s:text name="binolmbmbm11_employeeCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    <td>
		    <span>
		    <s:hidden name="employeeId"></s:hidden>
		    <s:hidden name="employeeCode"></s:hidden>
		    <span id="employeeDiv" style="line-height: 18px;"></span>
		    <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		    <a class="add" onclick="binolmbcom01.popEmployeeList('${searchEmployeeInitUrl}','${selEmpErrorMsg}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		    </span>
		    </td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_telephone"></s:text></th>
		    <td><span><s:textfield name="telephone" cssClass="text" maxlength="20"></s:textfield></span></td>
      	    <th><s:text name="binolmbmbm11_joinDate"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    <td><span><s:textfield name="joinDate" cssClass="date"></s:textfield></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_email"></s:text></th>
		    <td><span><s:textfield name="email" cssClass="text" maxlength="60"></s:textfield></span></td>
      	    <th><s:text name="binolmbmbm11_memberBirthDay"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			<td><span><s:textfield name="birth" cssClass="date"></s:textfield></span></td>
      	  </tr>
      	  <tr>
      	  	<th><s:text name="binolmbmbm11_tencentQQ"></s:text></th>
		    <td><span><s:textfield name="tencentQQ" cssClass="text" maxlength="20"></s:textfield></span></td>
			<th><s:text name="binolmbmbm11_gender"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			<td><span><s:radio list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="gender" ></s:radio></span></td>
      	  </tr>
      	  <tr>
      	  	<th><s:text name="binolmbmbm11_initTotalAmount"></s:text></th>
		    <td><span><s:textfield name="initTotalAmount" cssClass="text"></s:textfield></span></td>
		    <th><s:text name="binolmbmbm11_returnVisit"></s:text></th>
		    <td><span><s:select list='#application.CodeTable.getCodes("1423")' listKey="CodeKey" listValue="Value" name="returnVisit" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
      	  </tr>
		  <tr>
			<th><s:text name="binolmbmbm11_income"></s:text></th>
			<td><span><s:select list='#application.CodeTable.getCodes("1425")' listKey="CodeKey" listValue="Value" name="income" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
			<th><s:text name="binolmbmbm11_skinType"></s:text></th>
			<td><span><s:select list='#application.CodeTable.getCodes("1424")' listKey="CodeKey" listValue="Value" name="skinType" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
		  </tr>
      	</table>
      	
      	<table class="detail" cellpadding="0" cellspacing="0">
          <tr>
			<th><s:text name="binolmbmbm11_identityCard"></s:text></th>
		    <td><span><s:textfield name="identityCard" cssClass="text" maxlength="18"></s:textfield></span></td>
			<th><s:text name="binolmbmbm11_referrer"></s:text></th>
			<td><span><s:textfield name="referrer" cssClass="text" maxlength="20"></s:textfield></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_blogId"></s:text></th>
		    <td><span><s:textfield name="blogId" cssClass="text" maxlength="30"></s:textfield></span></td>
      	    <th><s:text name="binolmbmbm11_channelCode"></s:text></th>
		    <td><span><s:select list='#application.CodeTable.getCodes("1301")' listKey="CodeKey" listValue="Value" name="channelCode" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_messageId"></s:text></th>
			<td><span><s:textfield name="messageId" cssClass="text" maxlength="30"></s:textfield></span></td>
      	    <th><s:text name="binolmbmbm11_isReceiveMsg"></s:text></th>
		    <td><span>
		      <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1"><label for="isReceiveMsg1"><s:text name="binolmbmbm11_isReceiveMsg1"></s:text></label>
			  <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0"><label for="isReceiveMsg0"><s:text name="binolmbmbm11_isReceiveMsg0"></s:text></label>
		    </span></td>
      	  </tr>
          <tr>
			<th><s:text name="binolmbmbm11_profession"></s:text></th>
			<td><span><s:select list='#application.CodeTable.getCodes("1236")' listKey="CodeKey" listValue="Value" name="profession" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
			<th><s:text name="binolmbmbm11_active"></s:text></th>
			<td><span>
			  <input type="radio" name="active" id="active1" value="1"><label for="active1"><s:text name="binolmbmbm11_active1"></s:text></label>
			  <input type="radio" name="active" id="active0" value="0"><label for="active0"><s:text name="binolmbmbm11_active0"></s:text></label>
			</span></td>
		  </tr>
		  <tr>
		    <th><s:text name="binolmbmbm11_maritalStatus"></s:text></th>
			<td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1043")' listKey="CodeKey" listValue="Value" name="maritalStatus" ></s:radio></span></td>		   
		  </tr>
		  <tr>
      	    <th><s:text name="binolmbmbm11_connectTime"></s:text></th>
			<td colspan="3"><span><s:checkboxlist list='#application.CodeTable.getCodes("1237")' listKey="CodeKey" listValue="Value" name="connectTime" ></s:checkboxlist></span></td>
		  </tr>
		  <s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'>
		  <tr>
			<th><s:text name="binolmbmbm11_memType"></s:text></th>
		    <td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" ></s:radio></span></td>
		  </tr>
		  </s:if>
		  <tr>
     	    <th><s:text name="binolmbmbm11_memo1"></s:text></th>
		    <td><span style="width:100%"><s:textarea name="memo1" cssStyle="width:90%"></s:textarea></span></td>
		    <th><s:text name="binolmbmbm11_memo2"></s:text></th>
		    <td><span style="width:100%"><s:textarea name="memo2" cssStyle="width:90%"></s:textarea></span></td>
     	  </tr>
        </table>
	  </div>
	</div>
	
	<div class="box2 box2-active" style="margin:0 0 10px 0;">
      <div class="box2-header clearfix">
      	<strong class="left active">
      	  <s:text name="binolmbmbm11_memAddressTitle"></s:text>
      	</strong>
      </div>
      <div class="box2-content clearfix" style="padding:10px 10px 0;">
      	<table class="detail" cellpadding="0" cellspacing="0">
          <tr>
		    <th><s:text name="binolmbmbm11_provinceId"></s:text></th>
		    <td><select name="provinceId" id="provinceDiv"></select></td>
		    <th><s:text name="binolmbmbm11_address"></s:text></th>
			<td><span><s:textfield name="address" cssClass="text" maxlength="200"></s:textfield></span></td>
		  </tr>
		  <tr>
		    <th><s:text name="binolmbmbm11_cityId"></s:text></th>
			<td><select name="cityId" id="cityDiv"></select></td>
			<th><s:text name="binolmbmbm11_postcode"></s:text></th>
		    <td><span><s:textfield name="postcode" cssClass="text" maxlength="10"></s:textfield></span></td>
		  </tr>
		  <tr>
		    <th><s:text name="binolmbmbm11_countyId"></s:text></th>
		    <td colspan="3"><select name="countyId" id="countyDiv"></select></td>
		  </tr>
        </table>
	  </div>
	</div>
	
	<s:if test="%{extendPropertyList != null && !extendPropertyList.isEmpty()}">
    <div class="box2 box2-active" style="margin:0 0 10px 0;">
      <div class="box2-header clearfix">
      	<strong class="left active">
      	  <s:text name="binolmbmbm11_extendProperty"></s:text>
      	</strong>
      </div>
      <div class="box2-content clearfix" style="padding:10px 10px 0;">
        <table class="detail" cellpadding="0" cellspacing="0">
          <tbody>
          	<s:iterator value="extendPropertyList" id="questionMap" status="status">
          	<tr>
              <th style="width: 15%"><s:property value="%{#questionMap.questionItem}"/></th>
              <td style="width: 85%">
                <span>
                  <s:hidden name="%{'propertyInfoList['+#status.index+'].paperId'}" value="%{#questionMap.paperId}"></s:hidden>
                  <s:hidden name="%{'propertyInfoList['+#status.index+'].extendPropertyId'}" value="%{#questionMap.paperQuestionId}"></s:hidden>
      			  <s:hidden name="%{'propertyInfoList['+#status.index+'].propertyType'}" value="%{#questionMap.questionType}"></s:hidden>
                  <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_SINCHOICE}'>
		         	<select name="propertyInfoList[${status.index}].propertyValues">
		         		<option value=""><s:text name="global.page.select"></s:text></option>
		         		<s:iterator value="%{#questionMap.answerList}" id="answerMap">
		         			<option value='<s:property value="%{#answerMap.answer}"/>'><s:property value="%{#answerMap.answer}"/></option>
		         		</s:iterator>
		         	</select>
		          </s:if>
		          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_MULCHOICE}'>
	        		<s:iterator value="%{#questionMap.answerList}" id="answerMap" status="answerStatus">
	        			<input type="checkbox" name="propertyInfoList[${status.index}].propertyValues" value="${answerStatus.index+1 }" /><s:property value="%{#answerMap.answer}"/>
	        		</s:iterator>
		          </s:if>
		          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_ESSAY || #questionMap.questionType == #QUESTIONTYPE_APFILL}'>
	        		<s:textfield name="%{'propertyInfoList['+#status.index+'].propertyValues'}" cssClass="text"></s:textfield>
		          </s:if>
                </span>
              </td>
            </tr>
            </s:iterator>
          </tbody>
        </table>
      </div>
    </div>
    </s:if>
   
    <div class="center clearfix">
	  <s:url action="BINOLMBMBM11_add" id="addMemberUrl"></s:url>
	  <button class="save" onclick="binolmbmbm11.addMember('${addMemberUrl }');return false;">
    	<span class="ui-icon icon-save"></span>
    	<span class="button-text"><s:text name="global.page.save"/></span>
      </button>
      <button id="close" class="close" type="button"  onclick="window.close();return false;">
    	<span class="ui-icon icon-close"></span>
    	<span class="button-text"><s:text name="global.page.close"/></span>
  	  </button>
    </div>
  </form>  
  </div>
</div>
</div>
</s:i18n>


<script type="text/javascript">
$(function(){
	var regionJson = '${regionJson}';
	if(regionJson) {
		regionJson = eval('('+regionJson+')');
	}
	regionLinkageInit(regionJson, {
		optionDefault: "${select_default }",
		provinceId: "provinceDiv",
		cityId: "cityDiv",
		countyId: "countyDiv"
	});
});
</script>
