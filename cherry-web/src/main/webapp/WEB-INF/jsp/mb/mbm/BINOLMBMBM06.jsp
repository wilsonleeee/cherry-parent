<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>
<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>
<s:set id="QUESTIONTYPE_ESSAY"><%=MonitorConstants.QUESTIONTYPE_ESSAY %></s:set>
<s:set id="QUESTIONTYPE_APFILL"><%=MonitorConstants.QUESTIONTYPE_APFILL %></s:set>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM06.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	$('#birth').cherryDate({yearRange: 'c-100:c'});
	var editJoinDateFlag = '<s:property value="editJoinDateFlag"/>';
	if(editJoinDateFlag == '1') {
		$('#joinDate').cherryDate({yearRange: 'c-100:c',maxDate:'${sysDate}'});
	}
	if ($('#joinTimeClub').length > 0) {
		$('#joinTimeClub').cherryDate({yearRange: 'c-100:c',maxDate:'${sysDate}'});
	}
});
</script>

<s:i18n name="i18n.mb.BINOLMBMBM06">
	<s:text name="global.page.select" id="select_default"/>
	<s:text name="binolmbmbm06_selEmpErrorMsg" id="selEmpErrorMsg"></s:text>
	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm06_memberEdit" /></span>
	</div>
	<div id="actionResultDisplay"></div>
	<div class="panel-content clearfix">
	<form id="mainForm">
	  <s:hidden name="memberInfoId" value="%{memberInfoMap.memberInfoId}"></s:hidden>
      <s:hidden name="modifyTime" value="%{memberInfoMap.updateTime}"></s:hidden>
      <s:hidden name="modifyCount" value="%{memberInfoMap.modifyCount}"></s:hidden>
      <s:hidden name="memCodeOld" value="%{memberInfoMap.memCode}"></s:hidden>
      <s:hidden name="cardCount" value="%{memberInfoMap.cardCount}"></s:hidden>
      <s:hidden name="activeOld" value="%{memberInfoMap.active}"></s:hidden>
      <s:hidden name="version" value="%{memberInfoMap.version}"></s:hidden>
      <s:hidden name="birthOld" value="%{memberInfoMap.birth}"></s:hidden>
      <s:hidden name="referrerIdOld" value="%{memberInfoMap.referrerId}"></s:hidden>
      <s:hidden name="initTotalAmountOld" value="%{memberInfoMap.initTotalAmount}"></s:hidden>
      <s:hidden name="status" value="%{memberInfoMap.status}"></s:hidden>
      <s:hidden name="mobilePhoneOld" value="%{memberInfoMap.mobilePhone}"></s:hidden>
      <s:hidden id="memCardMobileSameFlag" value="%{memCardMobileSameFlag}"></s:hidden>
	  <%-- 会员基本信息 --%>
	  <div class="section">
      	<div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span>
            <s:text name="global.page.title"/>
          </strong>
          <span class="gray" style="margin: 3px 0 0 10px;"><s:text name="binolmbmbm06_remark"></s:text></span>
	    </div>
        <div class="section-content">
        <s:if test='%{"3".equals(clubMod)}'>
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm06_memCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td><span>
		      <span style="line-height: 18px; margin-right: 10px;"><s:property value="%{memberInfoMap.memCode}"/></span>		      
		      <s:textfield name="memCode" value="%{memberInfoMap.memCode}" cssClass="text" maxlength="20" cssStyle="display:none"></s:textfield>
		      <s:if test="%{memberInfoMap.status == 1}">
		      <a class="delete" onclick="binolmbmbm06.editMemCode(this);"><span class="ui-icon icon-edit"></span><span class="button-text" id="editMemCodeButton"><s:text name="global.page.changeMemCode"></s:text></span></a>
		      </s:if>
		      </span></td>
		      <th><s:text name="binolmbmbm06_levelName"></s:text></th>
			  <td><span><s:property value="memberInfoMap.levelName"/></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_name"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:textfield name="memName" value="%{memberInfoMap.name}" maxlength="30" cssClass="text"></s:textfield></span></td>
		      <th><s:text name="binolmbmbm06_counterCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td>
		      <span>
		      <s:hidden name="organizationId" value="%{memberInfoMap.organizationId}"></s:hidden>
		      <s:hidden name="organizationCode" value="%{memberInfoMap.counterCode}"></s:hidden>
		      <s:hidden name="counterKind" value="%{memberInfoMap.counterKind}"></s:hidden>
		      <span id="counterDiv" style="line-height: 18px;">
		      <s:if test='%{memberInfoMap.counterCode != null && !"".equals(memberInfoMap.counterCode)}'>
		      <s:if test='%{memberInfoMap.counterName != null && !"".equals(memberInfoMap.counterName)}'>
		      (<s:property value="memberInfoMap.counterCode"/>)<s:property value="memberInfoMap.counterName"/>
		      </s:if>
		      <s:else>
		      <s:property value="memberInfoMap.counterCode"/>
		      </s:else>
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,1);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popCounterList('${searchCounterInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      </td>
            </tr>
            <tr>
	      	    <th><s:text name="binolmbmbm06_nickname"></s:text></th>
			    <td><span><s:textfield name="nickname" value="%{memberInfoMap.nickname}" maxlength="30" cssClass="text"></s:textfield></span></td>
			    <th><s:text name="binolmbmbm06_creditRating"></s:text></th>
			    <td><span><s:select list='#application.CodeTable.getCodes("1317")' listKey="CodeKey" listValue="Value" name="creditRating" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.creditRating}"></s:select></span></td>
	      	</tr>
            <tr>
              <th><s:text name="binolmbmbm06_mobilePhone"></s:text></th>
			  <td><span><s:textfield name="mobilePhone" value="%{memberInfoMap.mobilePhone}" maxlength="20" cssClass="text"></s:textfield></span></td>
		      <th><s:text name="binolmbmbm06_employeeCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td>
		      <span>
		      <s:hidden name="employeeId" value="%{memberInfoMap.employeeId}"></s:hidden>
		      <s:hidden name="employeeCode" value="%{memberInfoMap.employeeCode}"></s:hidden>
		      <span id="employeeDiv" style="line-height: 18px;">
		      <s:if test='%{memberInfoMap.employeeCode != null && !"".equals(memberInfoMap.employeeCode)}'>
		      <s:if test='%{memberInfoMap.employeeName != null && !"".equals(memberInfoMap.employeeName)}'>
		      (<s:property value="memberInfoMap.employeeCode"/>)<s:property value="memberInfoMap.employeeName"/>
		      </s:if>
		      <s:else>
		      <s:property value="memberInfoMap.employeeCode"/>
		      </s:else>		      
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,2);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popEmployeeList('${searchEmployeeInitUrl}','${selEmpErrorMsg}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      </td>
            </tr>
            <tr>
			  <th><s:text name="binolmbmbm06_telephone"></s:text></th>
			  <td><span><s:textfield name="telephone" value="%{memberInfoMap.telephone}" maxlength="20" cssClass="text"></s:textfield></span></td>
			  <th><s:text name="binolmbmbm06_joinDate"></s:text></th>
			  <td><span>
			  <s:if test="%{editJoinDateFlag == 1}">
			  	<s:textfield name="joinDate" cssClass="date" value="%{memberInfoMap.joinDate}"></s:textfield>
			  </s:if>
			  <s:elseif test="%{editJoinDateFlag == 2}">
			  	<s:property value="%{memberInfoMap.joinDate}"/><s:hidden name="joinDate" value="%{memberInfoMap.joinDate}"></s:hidden>
			  </s:elseif>
			  <s:else>
			  	<s:hidden name="joinDate" value="%{memberInfoMap.joinDate}"></s:hidden>
			  </s:else>
			  </span></td>
            </tr>
            <tr> 
			  <th><s:text name="binolmbmbm06_email"></s:text></th>
			  <td><span><s:textfield name="email" value="%{memberInfoMap.email}" maxlength="60" cssClass="text"></s:textfield></span></td>
			  <th><s:text name="binolmbmbm06_memberBirthDay"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:textfield name="birth" value="%{memberInfoMap.birth}" cssClass="date"></s:textfield></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_tencentQQ"></s:text></th>
		      <td><span><s:textfield name="tencentQQ" cssClass="text" value="%{memberInfoMap.tencentQQ}" maxlength="20"></s:textfield></span></td>
			  <th><s:text name="binolmbmbm06_gender"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:radio list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="gender" value="%{memberInfoMap.gender}"></s:radio></span></td>
            </tr>
            <tr>
            	<th><s:text name="binolmbmbm06_initTotalAmount"></s:text></th>
		      	<td colspan="3"><span><s:textfield name="initTotalAmount" cssClass="text" value="%{memberInfoMap.initTotalAmount}"></s:textfield></span></td>
            </tr>
          </table>
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm06_identityCard"></s:text></th>
			  <td><span><s:textfield name="identityCard" value="%{memberInfoMap.identityCard}" maxlength="18" cssClass="text"></s:textfield></span></td>
              <th><s:text name="binolmbmbm06_referrer"></s:text></th>
			  <td><span><s:textfield name="referrer" value="%{memberInfoMap.referrer}" cssClass="text" maxlength="20"></s:textfield></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_blogId"></s:text></th>
		      <td><span><s:textfield name="blogId" value="%{memberInfoMap.blogId}" cssClass="text" maxlength="30"></s:textfield></span></td>
              <th><s:text name="binolmbmbm06_channelCode"></s:text></th>
			  <td><span><s:select list='#application.CodeTable.getCodes("1301")' listKey="CodeKey" listValue="Value" name="channelCode" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.channelCode}"></s:select></span></td>
            </tr>
		    <tr>
			  <th><s:text name="binolmbmbm06_messageId"></s:text></th>
			  <td><span><s:textfield name="messageId" value="%{memberInfoMap.messageId}" cssClass="text" maxlength="30"></s:textfield></span></td>
			  <th><s:text name="binolmbmbm06_isReceiveMsg"></s:text></th>
		      <td><span>
		        <s:if test="%{memberInfoMap.isReceiveMsg == 1}">
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" checked="checked"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:if>
		        <s:elseif test="%{memberInfoMap.isReceiveMsg == 0}">
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" checked="checked"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:elseif>
		        <s:else>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:else>
		      </span></td>
			</tr>
			<tr>
			  <th><s:text name="binolmbmbm06_profession"></s:text></th>
			  <td><span><s:select list='#application.CodeTable.getCodes("1236")' listKey="CodeKey" listValue="Value" name="profession" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.profession}"></s:select></span></td>
			  <th><s:text name="binolmbmbm06_active"></s:text></th>
			  <td><span>
			    <s:if test="%{memberInfoMap.active == 1}">
		        <input type="radio" name="active" id="active1" value="1" checked="checked"><label for="active1"><s:text name="binolmbmbm06_active1"></s:text></label>
		        <input type="radio" name="active" id="active0" value="0"><label for="active0"><s:text name="binolmbmbm06_active0"></s:text></label>
		        </s:if>
		        <s:elseif test="%{memberInfoMap.active == 0}">
		        <input type="radio" name="active" id="active1" value="1"><label for="active1"><s:text name="binolmbmbm06_active1"></s:text></label>
		        <input type="radio" name="active" id="active0" value="0" checked="checked"><label for="active0"><s:text name="binolmbmbm06_active0"></s:text></label>
		        </s:elseif>
		        <s:else>
		        <input type="radio" name="active" id="active1" value="1"><label for="active1"><s:text name="binolmbmbm06_active1"></s:text></label>
		        <input type="radio" name="active" id="active0" value="0"><label for="active0"><s:text name="binolmbmbm06_active0"></s:text></label>
		        </s:else>
			  </span></td>
			</tr>
			<tr>
			  <th><s:text name="binolmbmbm06_maritalStatus"></s:text></th>
			  <td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1043")' listKey="CodeKey" listValue="Value" name="maritalStatus" value="%{memberInfoMap.maritalStatus}"></s:radio></span></td>
			</tr>
			<tr>
      	      <th><s:text name="binolmbmbm06_connectTime"></s:text></th>
			  <td colspan="3"><span><s:checkboxlist list='#application.CodeTable.getCodes("1237")' listKey="CodeKey" listValue="Value" name="connectTime" value="%{memberInfoMap.connectTime}"></s:checkboxlist></span></td>
      	    </tr>
			<s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'>
			<tr>
			  <th><s:text name="binolmbmbm06_memType"></s:text></th>
		      <td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" value="%{memberInfoMap.memType}"></s:radio></span></td>
			</tr>
			</s:if>
			<tr>
      	      <th><s:text name="binolmbmbm06_memo1"></s:text></th>
			  <td><span style="width:100%"><s:textarea name="memo1" value="%{memberInfoMap.memo1}" cssStyle="width:90%"></s:textarea></span></td>
			  <th><s:text name="binolmbmbm06_memo2"></s:text></th>
			  <td><span style="width:100%"><s:textarea name="memo2" value="%{memberInfoMap.memo2}" cssStyle="width:90%"></s:textarea></span></td>
      	    </tr>
          </table>
          </s:if>
          <s:else>
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm06_memCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td><span>
		      <span style="line-height: 18px; margin-right: 10px;"><s:property value="%{memberInfoMap.memCode}"/></span>		      
		      <s:textfield name="memCode" value="%{memberInfoMap.memCode}" cssClass="text" maxlength="20" cssStyle="display:none"></s:textfield>
		      <s:if test="%{memberInfoMap.status == 1}">
		      <a class="delete" onclick="binolmbmbm06.editMemCode(this);"><span class="ui-icon icon-edit"></span><span class="button-text" id="editMemCodeButton"><s:text name="global.page.changeMemCode"></s:text></span></a>
		      </s:if>
		      </span></td>
		      <th><s:text name="binolmbmbm06_counterCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td>
		      <span>
		      <s:hidden name="organizationId" value="%{memberInfoMap.organizationId}"></s:hidden>
		      <s:hidden name="organizationCode" value="%{memberInfoMap.counterCode}"></s:hidden>
		      <s:hidden name="counterKind" value="%{memberInfoMap.counterKind}"></s:hidden>
		      <span id="counterDiv" style="line-height: 18px;">
		      <s:if test='%{memberInfoMap.counterCode != null && !"".equals(memberInfoMap.counterCode)}'>
		      <s:if test='%{memberInfoMap.counterName != null && !"".equals(memberInfoMap.counterName)}'>
		      (<s:property value="memberInfoMap.counterCode"/>)<s:property value="memberInfoMap.counterName"/>
		      </s:if>
		      <s:else>
		      <s:property value="memberInfoMap.counterCode"/>
		      </s:else>
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,1);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popCounterList('${searchCounterInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_name"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:textfield name="memName" value="%{memberInfoMap.name}" maxlength="30" cssClass="text"></s:textfield></span></td>
		      <th><s:text name="binolmbmbm06_employeeCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td>
		      <span>
		      <s:hidden name="employeeId" value="%{memberInfoMap.employeeId}"></s:hidden>
		      <s:hidden name="employeeCode" value="%{memberInfoMap.employeeCode}"></s:hidden>
		      <span id="employeeDiv" style="line-height: 18px;">
		      <s:if test='%{memberInfoMap.employeeCode != null && !"".equals(memberInfoMap.employeeCode)}'>
		      <s:if test='%{memberInfoMap.employeeName != null && !"".equals(memberInfoMap.employeeName)}'>
		      (<s:property value="memberInfoMap.employeeCode"/>)<s:property value="memberInfoMap.employeeName"/>
		      </s:if>
		      <s:else>
		      <s:property value="memberInfoMap.employeeCode"/>
		      </s:else>		      
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,2);"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popEmployeeList('${searchEmployeeInitUrl}','${selEmpErrorMsg}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      </td>
            </tr>
            <tr>
	      	    <th><s:text name="binolmbmbm06_nickname"></s:text></th>
			    <td><span><s:textfield name="nickname" value="%{memberInfoMap.nickname}" maxlength="30" cssClass="text"></s:textfield></span></td>
			    <th><s:text name="binolmbmbm06_creditRating"></s:text></th>
			    <td><span><s:select list='#application.CodeTable.getCodes("1317")' listKey="CodeKey" listValue="Value" name="creditRating" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.creditRating}"></s:select></span></td>
	      	</tr>
            <tr>
              <th><s:text name="binolmbmbm06_mobilePhone"></s:text></th>
			  <td><span><s:textfield name="mobilePhone" value="%{memberInfoMap.mobilePhone}" maxlength="20" cssClass="text"></s:textfield></span></td>
		      <th><s:text name="binolmbmbm06_joinDate"></s:text></th>
			  <td><span>
			  <s:if test="%{editJoinDateFlag == 1}">
			  	<s:textfield name="joinDate" cssClass="date" value="%{memberInfoMap.joinDate}"></s:textfield>
			  </s:if>
			  <s:elseif test="%{editJoinDateFlag == 2}">
			  	<s:property value="%{memberInfoMap.joinDate}"/><s:hidden name="joinDate" value="%{memberInfoMap.joinDate}"></s:hidden>
			  </s:elseif>
			  <s:else>
			  	<s:hidden name="joinDate" value="%{memberInfoMap.joinDate}"></s:hidden>
			  </s:else>
			  </span></td>
            </tr>
            <tr>
			  <th><s:text name="binolmbmbm06_telephone"></s:text></th>
			  <td><span><s:textfield name="telephone" value="%{memberInfoMap.telephone}" maxlength="20" cssClass="text"></s:textfield></span></td>
			  <th><s:text name="binolmbmbm06_memberBirthDay"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:textfield name="birth" value="%{memberInfoMap.birth}" cssClass="date"></s:textfield></span></td>
            </tr>
            <tr> 
			  <th><s:text name="binolmbmbm06_email"></s:text></th>
			  <td><span><s:textfield name="email" value="%{memberInfoMap.email}" maxlength="60" cssClass="text"></s:textfield></span></td>
			  <th><s:text name="binolmbmbm06_gender"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:radio list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="gender" value="%{memberInfoMap.gender}"></s:radio></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_tencentQQ"></s:text></th>
		      <td><span><s:textfield name="tencentQQ" cssClass="text" value="%{memberInfoMap.tencentQQ}" maxlength="20"></s:textfield></span></td>
              <th><s:text name="binolmbmbm06_initTotalAmount"></s:text></th>
		      <td colspan="3"><span><s:textfield name="initTotalAmount" cssClass="text" value="%{memberInfoMap.initTotalAmount}"></s:textfield></span></td>
            </tr>
          </table>
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm06_identityCard"></s:text></th>
			  <td><span><s:textfield name="identityCard" value="%{memberInfoMap.identityCard}" maxlength="18" cssClass="text"></s:textfield></span></td>
              <th><s:text name="binolmbmbm06_channelCode"></s:text></th>
			  <td><span><s:select list='#application.CodeTable.getCodes("1301")' listKey="CodeKey" listValue="Value" name="channelCode" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.channelCode}"></s:select></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_blogId"></s:text></th>
		      <td><span><s:textfield name="blogId" value="%{memberInfoMap.blogId}" cssClass="text" maxlength="30"></s:textfield></span></td>
              <th><s:text name="binolmbmbm06_active"></s:text></th>
			  <td><span>
			    <s:if test="%{memberInfoMap.active == 1}">
		        <input type="radio" name="active" id="active1" value="1" checked="checked"><label for="active1"><s:text name="binolmbmbm06_active1"></s:text></label>
		        <input type="radio" name="active" id="active0" value="0"><label for="active0"><s:text name="binolmbmbm06_active0"></s:text></label>
		        </s:if>
		        <s:elseif test="%{memberInfoMap.active == 0}">
		        <input type="radio" name="active" id="active1" value="1"><label for="active1"><s:text name="binolmbmbm06_active1"></s:text></label>
		        <input type="radio" name="active" id="active0" value="0" checked="checked"><label for="active0"><s:text name="binolmbmbm06_active0"></s:text></label>
		        </s:elseif>
		        <s:else>
		        <input type="radio" name="active" id="active1" value="1"><label for="active1"><s:text name="binolmbmbm06_active1"></s:text></label>
		        <input type="radio" name="active" id="active0" value="0"><label for="active0"><s:text name="binolmbmbm06_active0"></s:text></label>
		        </s:else>
			  </span></td>
            </tr>
		    <tr>
			  <th><s:text name="binolmbmbm06_messageId"></s:text></th>
			  <td colspan="3"><span><s:textfield name="messageId" value="%{memberInfoMap.messageId}" cssClass="text" maxlength="30"></s:textfield></span></td>
			</tr>
			<tr>
			  <th><s:text name="binolmbmbm06_profession"></s:text></th>
			  <td colspan="3"><span><s:select list='#application.CodeTable.getCodes("1236")' listKey="CodeKey" listValue="Value" name="profession" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.profession}"></s:select></span></td>
			</tr>
			<tr>
			  <th><s:text name="binolmbmbm06_maritalStatus"></s:text></th>
			  <td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1043")' listKey="CodeKey" listValue="Value" name="maritalStatus" value="%{memberInfoMap.maritalStatus}"></s:radio></span></td>
			</tr>
			<tr>
      	      <th><s:text name="binolmbmbm06_connectTime"></s:text></th>
			  <td colspan="3"><span><s:checkboxlist list='#application.CodeTable.getCodes("1237")' listKey="CodeKey" listValue="Value" name="connectTime" value="%{memberInfoMap.connectTime}"></s:checkboxlist></span></td>
      	    </tr>
			<s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'>
			<tr>
			  <th><s:text name="binolmbmbm06_memType"></s:text></th>
		      <td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" value="%{memberInfoMap.memType}"></s:radio></span></td>
			</tr>
			</s:if>
			<tr>
      	      <th><s:text name="binolmbmbm06_memo1"></s:text></th>
			  <td><span style="width:100%"><s:textarea name="memo1" value="%{memberInfoMap.memo1}" cssStyle="width:90%"></s:textarea></span></td>
			  <th><s:text name="binolmbmbm06_memo2"></s:text></th>
			  <td><span style="width:100%"><s:textarea name="memo2" value="%{memberInfoMap.memo2}" cssStyle="width:90%"></s:textarea></span></td>
      	    </tr>
          </table>
          </s:else> 
	    </div>
	  </div>
	  
	  <%--=================== 会员俱乐部信息 ===================== --%>
	<s:if test='%{!"3".equals(clubMod)}'>
	  <div class="section">
        <div class="section-header">
       	  <strong>
           	<span class="ui-icon icon-ttl-section-info"></span>
           <s:text name="binolmbmbm06_memClubMsg" />
          </strong>
          <div class="right">
				<div id="editMsg" style="display: block;">
					<button onclick="binolmbmbm06.editRecMsg()" type="button" class="save"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="binolmbmbm06_clubEdit" /></span></button>
				</div>
				<div class="hide" id="saveMsg" style="display: none;">
					<button onclick="binolmbmbm06.backRecMsg()" type="button" class="save"><span class="ui-icon icon-back"></span><span class="button-text"><s:text name="binolmbmbm06_clubBack" /></span></button>
					
					<button onclick="binolmbmbm06.saveRecMsg()" type="button" class="save"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="binolmbmbm06_clubSave" /></span></button>
				</div>
			</div>
	    </div>
	     <div class="section-content">
	    <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm06_memClub" /></th>
			  <td>
			  <s:if test='%{clubList != null && clubList.size() != 0}'>
			  <s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="binolmbmbm06.changeClub(this)" Cssstyle="width:150px;"/>
			   </s:if>
			  </td>
			  <th></th>
			  <td></td>
            </tr>
          </table>
          <table class="detail" cellpadding="0" cellspacing="0" id="clubTable">
            <tr>
              <th><s:text name="binolmbmbm06_levelName" /></th>
			  <td><span>
			  <s:property value="memClubMap.levelName"/>
			  <s:if test="%{memClubMap.levelEndDate != null}">
			  	(<s:property value="memClubMap.levelStartDate"/> ~
			  	<s:property value="memClubMap.levelEndDate"/>)
			  	</s:if>
			  </span></td>
			  <th><s:text name="binolmbmbm06_isReceiveMsg" /></th>
			  <td><span>
			     <s:if test="%{memClubMap.isReceiveMsg == 1}">
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" checked="checked" disabled="disabled"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" disabled="disabled"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:if>
		        <s:elseif test="%{memClubMap.isReceiveMsg == 0}">
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" disabled="disabled"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" checked="checked" disabled="disabled"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:elseif>
		        <s:else>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" disabled="disabled"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" disabled="disabled"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:else>
			  </span>
			  </td>			  
            </tr> 
            <tr>
              <th><s:text name="binolmbmbm06_referrer"></s:text></th>
			  <td><span>
			 <s:textfield name="referrerClub" value="%{memClubMap.referrerClub}" cssClass="text" maxlength="20" disabled="true" id="referrerClub"></s:textfield>
			 <s:hidden name="clubReferIdOld" value="%{memClubMap.referrerIdClub}" id="clubReferIdOld"></s:hidden>
			  </span></td>
			  <th><s:text name="binolmbmbm06_joinTimeCB"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
			  <span class="hide club-edit">
			 	<s:textfield name="joinTimeClub" cssClass="date" value="%{memClubMap.joinTimeClub}"></s:textfield>
			  </span>
			  <span class="club-dtl">
			 	<s:property value="memClubMap.joinTimeClub"/>
			  </span>
			  </td> 
            </tr>
             <tr>
              <th><s:text name="binolmbmbm06_counterCodeCB"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
			  <span class="hide club-edit">
		      <s:hidden name="organizationIdClub" value="%{memClubMap.organizationIdClub}"></s:hidden>
		      <s:hidden name="organizationCodeClub" value="%{memClubMap.counterCodeClub}"></s:hidden>
		      <s:hidden name="counterKindClub" value="%{memClubMap.counterKindClub}"></s:hidden>
		      <span id="counterDivClub" style="line-height: 18px;">
		      <s:if test='%{memClubMap.counterCodeClub != null && !"".equals(memClubMap.counterCodeClub)}'>
		      <s:if test='%{memClubMap.counterNameClub != null && !"".equals(memClubMap.counterNameClub)}'>
		      (<s:property value="memClubMap.counterCodeClub"/>)<s:property value="memClubMap.counterNameClub"/>
		      </s:if>
		      <s:else>
		      <s:property value="memClubMap.counterCodeClub"/>
		      </s:else>
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,1,'1');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popCounterList('${searchCounterInitUrl}','1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      <span class="club-dtl">
			 	 <s:if test='%{memClubMap.counterNameClub != null && !"".equals(memClubMap.counterNameClub)}'>
	  		    (<s:property value="memClubMap.counterCodeClub"/>)<s:property value="memClubMap.counterNameClub"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memClubMap.counterCodeClub != null && !"".equals(memClubMap.counterCodeClub)}'>
	  		    	<s:property value="memClubMap.counterCodeClub"/>
	  		  	</s:if>
	  		  </s:else>
			  </span>
			  </td>
			  <th><s:text name="binolmbmbm06_employeeCB"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
			  <span class="hide club-edit">
		      <s:hidden name="employeeIdClub" value="%{memClubMap.employeeIdClub}"></s:hidden>
		      <s:hidden name="employeeCodeClub" value="%{memClubMap.employeeCodeClub}"></s:hidden>
		      <span id="employeeDivClub" style="line-height: 18px;">
		      <s:if test='%{memClubMap.employeeCodeClub != null && !"".equals(memClubMap.employeeCodeClub)}'>
		      <s:if test='%{memClubMap.employeeNameClub != null && !"".equals(memClubMap.employeeNameClub)}'>
		      (<s:property value="memClubMap.employeeCodeClub"/>)<s:property value="memClubMap.employeeNameClub"/>
		      </s:if>
		      <s:else>
		      <s:property value="memClubMap.employeeCodeClub"/>
		      </s:else>		      
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,2,'1');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popEmployeeList('${searchEmployeeInitUrl}','${selEmpErrorMsg}','1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      <span class="club-dtl">
			 <s:if test='%{memClubMap.employeeNameClub != null && !"".equals(memClubMap.employeeNameClub)}'>
	  		    (<s:property value="memClubMap.employeeCodeClub"/>)<s:property value="memClubMap.employeeNameClub"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memClubMap.employeeCodeClub != null && !"".equals(memClubMap.employeeCodeClub)}'>
	  		    	<s:property value="memClubMap.employeeCodeClub"/>
	  		  	</s:if>
	  		  </s:else>
			  </span>
			  </td>
			  </tr>
          </table>
          </div>
	 </div>
	 </s:if>
	  
	  <s:if test="%{memberInfoMap.memberAddressInfo != null}">
	  <s:set value="%{memberInfoMap.memberAddressInfo}" var="memberAddressInfo"></s:set>
	  <s:hidden name="addressInfoId" value="%{#memberAddressInfo.addressInfoId}"></s:hidden>
	  </s:if>
	  <div class="section">
      	<div class="section-header">
      	  <strong>
      	    <span class="ui-icon icon-ttl-section-info"></span>
      	    <s:text name="binolmbmbm06_memAddressTitle"></s:text>
      	  </strong>
        </div>
        <div class="section-content">
	      	<table class="detail" cellpadding="0" cellspacing="0">
	          <tr>
			    <th><s:text name="binolmbmbm06_provinceId"></s:text></th>
			    <td><select name="provinceId" id="provinceDiv"></select></td>
			    <th><s:text name="binolmbmbm06_address"></s:text></th>
				<td><span><s:textfield name="address" value="%{#memberAddressInfo.addressLine1}" cssClass="text" maxlength="200"></s:textfield></span></td>
			  </tr>
			  <tr>
			    <th><s:text name="binolmbmbm06_cityId"></s:text></th>
				<td><select name="cityId" id="cityDiv"></select></td>
				<th><s:text name="binolmbmbm06_postcode"></s:text></th>
			    <td><span><s:textfield name="postcode" value="%{#memberAddressInfo.zipCode}" cssClass="text" maxlength="10"></s:textfield></span></td>
			  </tr>
			  <tr>
			  <th><s:text name="binolmbmbm06_countyId"></s:text></th>
			    <td colspan="3"><select name="countyId" id="countyDiv"></select></td>
			  </tr>
	        </table>
	    </div>
	  </div>
	  
	  <s:if test="%{extendPropertyList != null && !extendPropertyList.isEmpty()}">
	    <div class="section">
      	  <div class="section-header">
	      	<strong>
	      	  <span class="ui-icon icon-ttl-section-info"></span>
	      	  <s:text name="binolmbmbm06_extendProperty"></s:text>
	      	</strong>
	      </div>
	      <div class="section-content">
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
			         			<option value='<s:property value="%{#answerMap.answer}"/>' <s:if test="%{#answerMap.checked == true}">selected</s:if>><s:property value="%{#answerMap.answer}"/></option>
			         		</s:iterator>
			         	</select>
			          </s:if>
			          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_MULCHOICE}'>
		        		<s:iterator value="%{#questionMap.answerList}" id="answerMap" status="answerStatus">
		        			<input type="checkbox" name="propertyInfoList[${status.index}].propertyValues" value="${answerStatus.index+1 }" <s:if test="%{#answerMap.checked == true}">checked</s:if>/><s:property value="%{#answerMap.answer}"/>
		        		</s:iterator>
			          </s:if>
			          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_ESSAY || #questionMap.questionType == #QUESTIONTYPE_APFILL}'>
		        		<s:textfield name="%{'propertyInfoList['+#status.index+'].propertyValues'}" value="%{#questionMap.answer}" cssClass="text"></s:textfield>
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
 		<s:url action="BINOLMBMBM06_update" id="updateMemInfoUrl"></s:url>
 		<button id="save" class="save" type="button" onclick="binolmbmbm06.updMemInfo('${updateMemInfoUrl}');return false;" >
      	  <span class="ui-icon icon-save"></span>
      	  <span class="button-text"><s:text name="global.page.save"/></span>
        </button>
      </div>
	</form>
	</div>
</s:i18n>


<span id="editButtonText" class="hide"><s:text name="global.page.changeMemCode"/></span>
<span id="cancleButtonText" class="hide"><s:text name="global.page.cancle"/></span>
<div class="hide">
	<s:url action="BINOLMBMBM06_changeClub" id="changeClub_Url"></s:url>
	<a href="${changeClub_Url}" id="changeClubUrl"></a>
	<s:url action="BINOLMBMBM06_saveRecMsg" id="saveRec_Url"></s:url>
	<a href="${saveRec_Url}" id="saveRecUrl"></a>
</div>

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
		countyId: "countyDiv",
		provinceVal: "${memberAddressInfo.provinceId}",
		cityVal: "${memberAddressInfo.cityId}",
		countyVal: "${memberAddressInfo.countyId}"
	});
});
</script>
