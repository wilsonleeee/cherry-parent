<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<s:i18n name="i18n.bs.BINOLBSSAM06">
<div id="aaData">
	<s:iterator value="bAAttendanceList" status="status">
     <ul>
     	<li><s:property value="#status.index+iDisplayStart+1" /></li>
     	<li><s:property value="departName" /></li>
		<li><s:property value="employeeName" /></li>
		<li><s:property value="workDate" /></li>
		<li><s:if test="stime>0"><s:text name="BSSAM06_isCheck"/></s:if><s:else><s:text name="BSSAM06_noCheck"/></s:else></li>
		<li><s:if test="time1>0"><s:text name="BSSAM06_isCheck"/></s:if><s:else><s:text name="BSSAM06_noCheck"/></s:else></li>
		<li><s:if test="time2>0"><s:text name="BSSAM06_isCheck"/></s:if><s:else><s:text name="BSSAM06_noCheck"/></s:else></li>
		<li><s:if test="time3>0"><s:text name="BSSAM06_isCheck"/></s:if><s:else><s:text name="BSSAM06_noCheck"/></s:else></li>
		<li><s:if test="time4>0"><s:text name="BSSAM06_isCheck"/></s:if><s:else><s:text name="BSSAM06_noCheck"/></s:else></li>
		<li><s:if test="xtime>0"><s:text name="BSSAM06_isCheck"/></s:if><s:else><s:text name="BSSAM06_noCheck"/></s:else></li>
	</ul>
	</s:iterator>
</div>
</s:i18n>
