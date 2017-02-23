<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.ss.BINOLSSPRM13">
<div class="box4" id="baseInfo">
	<div class="box4-header clearfix">
		<strong class="left"><s:text name='baseInfo'/></strong><!-- 活动基础信息 -->
	</div>
	<div class="box4-content clearfix">
		<table class="detail">
           	<tbody>
               	<tr>
                   <th><s:text name="prmActiveName"/></th>
                   <td><s:property value="map.prmActiveName"/></td>
                   <th><s:text name="selectBrandName"/></th>
                   <td>
                   		<s:iterator value="brandInfoList">
		  				<s:if test="map.brandInfoId == brandInfoId"><s:property value="brandName"/></s:if>
		  				</s:iterator>
                   </td>
               	</tr>
				<tr>
				  <th><s:text name="prmActiveType"/></th>
				  <td>
				  	<s:property value="#application.CodeTable.getVal('1185',map.activityType)"/>
				  	<s:property value="#application.CodeTable.getVal('1210',map.activityType)"/>
				  </td>
				  <th><s:text name="terminalChange"/></th>
				  <td><s:property value="#application.CodeTable.getVal('1040',map.mainModify)"/></td>
			  	</tr>
				<%--<s:if test='"1".equals(map.couponFlag)'>--%>
					<%--<tr>--%>
						<%--<th><s:text name="systemCode"/></th>--%>
						<%--<td><s:property value="#application.CodeTable.getVal('1426',map.systemCode)"/></td>--%>
						<%--<th><s:text name="linkMainCode"/></th>--%>
						<%--<td><s:property value='map.linkMainCode'/></td>--%>
					<%--</tr>--%>
				<%--</s:if>--%>
			  	<tr>
				  	<th><s:text name="prmActiveGrp"/></th>
				  	<td colspan="3">
				  		<span style="margin-right:15px;">
				  			<s:iterator value="prmActiveGrpList">
				  				<s:if test="map.actGrpID == promotionActGrpID">
				  				<s:property value="groupName"/>
				  				</s:if>
				  			</s:iterator>
				  		</span>
				  		<span>[</span>
				  		<div id="groupInfo"><jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_3.jsp" flush="true"/></div>
				  		<span>]</span>
				  	</td>
			  	</tr>
			  	<tr>
				  	<th><s:text name="prmDescription"/></th>
				  	<td colspan="3"><s:property value="map.descriptionDtl"/></td>
			  	</tr>
   			</tbody>
   		</table>
	</div>
</div>
</s:i18n>