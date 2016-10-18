<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.bs.BINOLBSSAM07">
<div id="aaData">
	<s:iterator value="bAAttendanceList" status="status">
	     <ul>
	     	<li><s:property value="#status.index+iDisplayStart+1" /></li>
	     	<li><s:property value="departName" /></li>
			<li><s:property value="employeeName" /></li>
			<li><s:property value="workDate"/></li>
			<li>
				<s:if test="attendanceType==1"><s:text name="BSSAM07_sCheck"/></s:if>
				<s:elseif test="attendanceType==0"><s:text name="BSSAM07_xCheck"/></s:elseif>
				<s:elseif test="attendanceType==2"><s:text name="BSSAM07_check"/></s:elseif>
			</li>
			<li><s:date name="attendanceDateTime" format="yyyy-MM-dd HH:mm:ss"/></li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
