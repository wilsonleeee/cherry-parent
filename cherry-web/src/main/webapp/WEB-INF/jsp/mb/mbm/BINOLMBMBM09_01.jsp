<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM09">
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<s:iterator value="memberInfoList" id="memberInfo" status="status">
<ul>
<li><span>
<s:checkbox name="memberInfoId" fieldValue="%{#memberInfo.memId}" onclick="binolmbmbm09.checkAllRecord(this,'#dataTable_Cloned');"></s:checkbox>
<s:hidden name="memberInfoId" value="%{#memberInfo.memId}"></s:hidden>
<s:hidden name="memCode" value="%{#memberInfo.memCode}"></s:hidden>
<s:hidden name="memCurValidFlag" value="%{#memberInfo.status}"></s:hidden>
<s:hidden name="versionDb" value="%{#memberInfo.versionDb}"></s:hidden>
</span></li>
<li><span>
<s:url action="BINOLMBMBM10_init" id="memberDetailUrl">
	<s:param name="memberInfoId" value="%{#memberInfo.memId}"></s:param>
</s:url>
<a href="${memberDetailUrl }" class="popup" onclick="javascript:openWin(this,{width:window.screen.width-10,height:window.screen.height-35});return false;">
	<s:if test='%{memName != null && !"".equals(memName)}'><s:property value="memName"/></s:if>
	<s:else><s:text name="binolmbmbm09_unknown"></s:text></s:else>
</a>
</span></li>
<li><span><s:property value="memCode"/></span></li>
<li><span><s:property value="nickname"/></span></li>
<li><span><s:property value="mobilePhone"/></span></li>
<li><span><s:property value="telephone"/></span></li>
<li><span><s:property value="email"/></span></li>
<li><span><s:property value="tencentQQ"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1006", #memberInfo.gender)' /></span></li>
<li><span>
<s:if test='%{birthYear != null && !"".equals(birthYear) && birthMonth != null && !"".equals(birthMonth) && birthDay != null && !"".equals(birthDay)}'>
<s:property value="birthYear"/>-<s:property value="birthMonth"/>-<s:property value="birthDay"/>
</s:if>
</span></li>
<li><span><s:property value="levelName"/></span></li>
<li><span><s:property value='#application.CodeTable.getVal("1317", #memberInfo.creditRating)' /></span></li>
<li><span><s:property value="totalPoint"/></span></li>
<li><span><s:property value="changablePoint"/></span></li>
<s:if test='%{!"3".equals(clubMod)}'>
	<li><span>
<s:if test='%{clubCounterName != null && !"".equals(clubCounterName)}'>
(<s:property value="clubCounterCode"/>)<s:property value="clubCounterName"/>
</s:if>
<s:elseif test='%{clubCounterCode != null && !"".equals(clubCounterCode)}'>
<s:property value="clubCounterCode"/>
</s:elseif>
</span></li>
<li><span>
<s:if test='%{clubEmployeeName != null && !"".equals(clubEmployeeName)}'>
(<s:property value="clubEmployeeCode"/>)<s:property value="clubEmployeeName"/>
</s:if>
<s:elseif test='%{clubEmployeeCode != null && !"".equals(clubEmployeeCode)}'>
<s:property value="clubEmployeeCode"/>
</s:elseif>
</span></li>
<li><span>
	<s:if test='%{clubJoinTime != null && !"".equals(clubJoinTime)}'>
	<s:property value="clubJoinTime"/>
	</s:if>
	</span></li>
</s:if>
<li><span>
<s:if test='%{counterName != null && !"".equals(counterName)}'>
(<s:property value="counterCode"/>)<s:property value="counterName"/>
</s:if>
<s:elseif test='%{counterCode != null && !"".equals(counterCode)}'>
<s:property value="counterCode"/>
</s:elseif>
</span></li>
<li><span>
<s:if test='%{employeeName != null && !"".equals(employeeName)}'>
(<s:property value="employeeCode"/>)<s:property value="employeeName"/>
</s:if>
<s:elseif test='%{employeeCode != null && !"".equals(employeeCode)}'>
<s:property value="employeeCode"/>
</s:elseif>
</span></li>
<li><span><s:property value="joinDate"/></span></li>
<li><span>
<s:set value="%{#memberInfo.memo1 }" var="memo1"></s:set>
<a title='<s:text name="binolmbmbm09_memo1" />|<s:property value="memo1"/>' class="description">
  <cherry:cut length="20" value="${memo1 }"></cherry:cut>
</a>
</span></li>
<li><span>
<s:set value="%{#memberInfo.memo2 }" var="memo2"></s:set>
<a title='<s:text name="binolmbmbm09_memo2" />|<s:property value="memo2"/>' class="description">
  <cherry:cut length="20" value="${memo2 }"></cherry:cut>
</a>
</span></li>
<li>
<s:if test='%{status == 1}'><span class='ui-icon icon-valid'></span></s:if>
<s:else><span class='ui-icon icon-invalid'></span></s:else>
</li>
<s:if test='%{"3".equals(clubMod)}'>
<li>
<span>
	<s:if test='%{referrerCode != null && !"".equals(referrerCode)}'>
	<s:property value="referrerCode"/>
	</s:if>
</span>
</li>
</s:if>
</ul>
</s:iterator>
</div>
</s:i18n>