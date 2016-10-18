<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.ss.BINOLSSPRM15">
<div id="aaData">
	<s:iterator value="activeList" id="active">
		<s:url id="s_editPrmActiveUrl" value="/ss/BINOLSSPRM37_init" >
    		<s:param name="activeID">${active.activeID}</s:param>
    		<s:param name="showType">detail</s:param>
    		<s:param name="initFlag">1</s:param>
    	</s:url>
		<ul>
		   <%-- 活动代码 --%>
			<li>			
				<s:if test='#active.counterActId != null && !"".equals(#active.counterActId)'>
				      <s:property value="counterActId"/>
				</s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 促销柜台 --%>
			<li>
				<s:if test='#active.prmCounter !=null && !"".equals(#active.prmCounter)'><s:property value="prmCounter"/></s:if>
				<s:else><s:text name='allCounter'/></s:else>
			</li>
			<%-- 促销品 --%>
			<li>
				<s:if test='#active.prmPro !=null && !"".equals(#active.prmPro)'><s:property value="prmPro"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 活动名称 --%>
			<li>
				<s:if test='#active.activeName !=null && !"".equals(#active.activeName)'>
					<a href="${s_editPrmActiveUrl}" class="popup" onclick="javascript:openWin(this);return false;">
					<s:property value="activeName"/></a></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 下发日期 --%>
			<li>
				<s:if test='#active.sendDate !=null && !"".equals(#active.sendDate)'><s:property value="sendDate"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 开始日期 --%>
			<li>
				<s:if test='#active.startDate !=null && !"".equals(#active.startDate)'><s:property value="startDate"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 结束日期 --%>
			<li>
				<s:if test='#active.endDate !=null && !"".equals(#active.endDate)'><s:property value="endDate"/></s:if>
				<s:else>&nbsp;</s:else>
			</li>
			<%-- 有效区分 --%>
			<li>
				<s:if test='"1".equals(#active.validFlag)'><span class='ui-icon icon-valid'></span></s:if>
				<s:else><span class='ui-icon icon-invalid'></span></s:else>
			</li>
		</ul>
	</s:iterator>
</div>
</s:i18n>
