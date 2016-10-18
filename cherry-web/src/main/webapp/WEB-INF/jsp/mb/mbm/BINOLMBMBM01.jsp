<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>

<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>
<s:set id="QUESTIONTYPE_ESSAY"><%=MonitorConstants.QUESTIONTYPE_ESSAY %></s:set>
<s:set id="QUESTIONTYPE_APFILL"><%=MonitorConstants.QUESTIONTYPE_APFILL %></s:set>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM01.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#joinDateStart').cherryDate();
	$('#joinDateEnd').cherryDate({
		beforeShow: function(input){
			var value = $('#joinDateStart').val();
			return [value,'minDate'];
		}
	});
});
</script>

<s:i18n name="i18n.mb.BINOLMBMBM01">
<s:text name="global.page.select" id="global.page.select"></s:text>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbmbm01_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="binolmbmbm01_memberList"></s:text> </span> 
    
    <span class="right">
      <cherry:show domId="BINOLMBMBM01_01">	
    	<s:url action="BINOLMBMBM11_init" id="addMemberUrl"></s:url>
    	<a class="add" href="${addMemberUrl }" onclick="javascript:openWin(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="binolmbmbm01_addMember"></s:text></span></a>
      </cherry:show>
    </span>
    </div>
</div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="memberCherryForm" class="inline" onsubmit="binolmbmbm01.searchMember();return false;">
    <s:hidden name="brandInfoId"></s:hidden>
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
      <a id="expandCondition" style="margin-left: 0px; font-size: 12px;" class="ui-select right">
        <span style="min-width:50px;" class="button-text choice"><s:text name="binolmbmbm01_moreCondition" /></span>
 		<span class="ui-icon ui-icon-triangle-1-n"></span>
 	  </a>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
      	<table class="detail" cellpadding="0" cellspacing="0">
		  <tbody>
			<tr>
			  <td colspan="4" style="line-height:25px;">
			    <span class="ui-widget breadcrumb" style="margin-right:50px;">
			    <s:checkbox name="addrNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm01_addrNotEmpty"/>
			    </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:checkbox name="telNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm01_telNotEmpty"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:checkbox name="emailNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm01_emailNotEmpty"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:checkbox name="telCheck" fieldValue="1"></s:checkbox><s:text name="binolmbmbm01_telCheck"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:checkbox name="validFlag" fieldValue="1"></s:checkbox><s:text name="binolmbmbm01_validFlag"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:checkbox name="memInfoRegFlg" fieldValue="1"></s:checkbox><s:text name="binolmbmbm01_memInfoRegFlg"/>
                </span>
              </td>
            </tr>
            <s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'>
            <tr>
              <th><s:text name="binolmbmbm01_memType" /></th>
              <td colspan="3">
				<span>
				<s:checkboxlist list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" ></s:checkboxlist>
				</span>
	   		  </td>
            </tr>
            </s:if>
            <tr>
              <th><s:text name="binolmbmbm01_memberLevel" /></th>
              <td colspan="3">
				<span><s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" ></s:checkboxlist></span>
	   		  </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm01_memCode" /></th>
              <td>
                <span><s:textfield name="memCode" cssClass="text"></s:textfield></span>
              </td>
              <th><s:text name="binolmbmbm01_mobilePhone" /></th>
              <td>
                <span><s:textfield name="mobilePhone" cssClass="text"></s:textfield></span>
			  </td>
            </tr>
			<tr>
              <th><s:text name="binolmbmbm01_name" /></th>
              <td>
                <span><s:textfield name="name" cssClass="text"></s:textfield></span>
			  </td>
			  <th><s:text name="binolmbmbm01_mebSex" /></th>
              <td>
                <span><s:checkboxlist list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="mebSex" ></s:checkboxlist></span>
              </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm01_birthDay" /></th>
              <td>
                <span>
                <s:select list="monthList" name="birthDayMonth" headerKey="" headerValue="%{global.page.select}" cssStyle="width:auto;"></s:select><s:text name="binolmbmbm01_month" />
          		<select name="birthDayDate" id="birthDayDate" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="binolmbmbm01_date" />
                </span>
			  </td>
			  <th><s:text name="binolmbmbm01_age" /></th>
              <td>
                <span><s:textfield name="ageStart" cssClass="number"></s:textfield>-<s:textfield name="ageEnd" cssClass="number"></s:textfield></span>
			  </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm01_joinDate" /></th>
              <td>
              	<span><s:textfield name="joinDateStart" cssClass="date"></s:textfield>-<s:textfield name="joinDateEnd" cssClass="date"></s:textfield></span>
              </td>
			  <th><s:text name="binolmbmbm01_memberPoint" /></th>
              <td>
                <span><s:textfield name="memberPointStart" cssClass="number"></s:textfield>-<s:textfield name="memberPointEnd" cssClass="number"></s:textfield></span>
              </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm01_counterCode" /></th>
              <td>
                <span><s:textfield name="counterCode" cssClass="text"></s:textfield></span>
              </td>
			  <th><s:text name="binolmbmbm01_referrerMemCode" /></th>
              <td>
                <span><s:textfield name="referrerMemCode" cssClass="text"></s:textfield></span>
              </td>
            </tr>                  
          </tbody>
        </table>
      </div>
    </div>

    <div id="moreCondition" style="display: none;">
    
    <s:if test="%{extendPropertyList != null && !extendPropertyList.isEmpty()}">
    <div class="box-header"> 
      <strong><span class="icon icon-ttl-search"></span><s:text name="binolmbmbm01_extendProperty"></s:text></strong>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
      	<table class="detail" cellpadding="0" cellspacing="0">
          <tbody>
          	<s:iterator value="extendPropertyList" id="questionMap" status="status">
          	<tr>
              <th style="width: 15%"><s:property value="%{#questionMap.questionItem}"/></th>
              <td style="width: 85%">
                <span>
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
	        		<s:textfield name="%{'propertyInfoList['+#status.index+'].propertyValues'}"></s:textfield>
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
    
    </div>
    <p class="clearfix">
      <button class="right search" onclick="binolmbmbm01.searchMember();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="memberInfo">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dateTable">
      <thead>
        <tr>
          <th><s:text name="binolmbmbm01_number"></s:text></th>
          <th><s:text name="binolmbmbm01_name"></s:text></th>
          <th><s:text name="binolmbmbm01_memCode"></s:text></th>
          <th><s:text name="binolmbmbm01_mobilePhone"></s:text></th>
          <th><s:text name="binolmbmbm01_memberLevel"></s:text></th>
          <th><s:text name="binolmbmbm01_joinDate"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
</s:i18n>  

<div class="hide">
<s:url action="BINOLMBMBM01_search" id="memberListUrl"></s:url>
<a href="${memberListUrl }" id="memberListUrl"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />