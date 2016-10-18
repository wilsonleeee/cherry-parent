<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 活动组时间一览DIV --%>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<s:if test='map.prmActiveGrpType != null && !"".equals(map.prmActiveGrpType)'>
<span class="green">
	<span><s:text name="prmActiveGrpType"/></span><%-- 活动组类型 --%>
	<span>:</span>
	<span><s:property value='#application.CodeTable.getVal("1174",map.prmActiveGrpType)'/></span>
	<input type="hidden" name="prmGrpType" value="<s:property value='map.prmActiveGrpType'/>"/>
</span>
</s:if>	
<s:if test='map.reserveBeginDate != null && !"".equals(map.reserveBeginDate)'>
	<span class="green" style="margin-left:20px;"><%-- 预约开始时间 --%>
		<span><s:text name="prmActiveGrpReserveDate"/></span>
		<span>:</span>
		<span><s:property value="map.reserveBeginDate"/></span>
		<span>~</span>
		<span><s:property value="map.reserveEndDate"/></span>
		<input type="hidden" name="reserveBeDate" value="<s:property value='map.reserveBeginDate'/>"/>
		<input type="hidden" name="reserveEDate" value="<s:property value='map.reserveEndDate'/>"/>
	</span>		
</s:if>			
<s:if test='map.activityBeginDate != null && !"".equals(map.activityBeginDate)'>				
	<span class="green" style="margin-left:20px;"><%-- 领用开始时间 --%>
		<span><s:text name="prmActiveGrpActivityDate"/></span>
		<span>:</span>
		<span><s:property value="map.activityBeginDate"/></span>
		<span>~</span>
		<span><s:property value="map.activityEndDate"/></span>
		<input type="hidden" name="activityBeDate" value="<s:property value='map.activityBeginDate'/>"/>
		<input type="hidden" name="activityEDate" value="<s:property value='map.activityEndDate'/>"/>
	</span>
</s:if>						
</s:i18n>