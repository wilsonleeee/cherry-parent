<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.bs.BINOLBSCNT01">
<div id="errorDiv">
	<div class="actionError">
	    <ul>
	        <li>
	        	<span>
	        		<s:property value="message"/>
	        	</span>
	        </li>
	    </ul>         
	</div>
</div>
<table>
<s:iterator value="errorCounterList" id="counterMap">
	<s:url id="editInit" action="BINOLBSCNT06_editInit">
        <%-- 柜台消息ID --%>
    </s:url>
	<tr id="<s:property value='counterCode'></s:property>">
		<%-- 所属品牌 --%>
        <td>
        	<span style="margin: 5px;">
        		<s:property value="brandCode"></s:property>
        	</span>
        </td>
        <%-- 柜台号 --%>
        <td><span style="margin: 5px;"><s:property value="counterCode"></s:property></span></td>
        <%-- 柜台名称 --%>
        <td>
        	<span style="margin: 5px;">
        		<s:if test='"1".equals(cntCodeRule)'>
		            <a href="${editInit}?jsonStr=<s:property value='jsonStr'></s:property>&&counterCode=<s:property value='counterCode'></s:property>" class="popup" onclick="javascript:openWin(this);return false;">
						<s:property value="counterName"/>
					</a>
				</s:if>
				<s:elseif test='"2".equals(cntCodeRule)'>
					<s:property value="counterName"/>
				</s:elseif>
					
        	</span>
        </td>
        <%-- 柜台类型--%>
        <td><span style="margin: 5px;"><s:property value="counterKindShow"></s:property></span></td>
        <%-- 柜台主管 --%>
        <td>
        	<span style="margin: 5px;">
        		<s:if test='!"".equals(basName)||!"".equals(basCode)'>
        			<s:if test='!"".equals(basName)'>
        				<s:if test='!"".equals(basCode)'>
        					<s:property value="'('+basCode+')'+basName"></s:property>
        				</s:if>
        				<s:else>
        					<s:property value="basName"></s:property>
        				</s:else>
        			</s:if>
        			<s:else>
        				<s:if test='!"".equals(basName)'>
        					<s:property value="'('+basCode+')'+basName"></s:property>
        				</s:if>
        				<s:else>
        					<s:property value="basCode"></s:property>
        				</s:else>
        			</s:else>
        		</s:if>
        	</span>
        </td>
        <%-- 所属大区 --%>
        <td>
        	<span style="margin: 5px;">
				<s:property value="regionName"></s:property>
        	</span>
        </td>
        <%-- 所属省份 --%>
         <td>
        	<span style="margin: 5px;">
				<s:property value="provinceName"></s:property>
        	</span>
        </td>
        <%-- 所属城市 --%>
        <td>
        	<span style="margin: 5px;">
        			<s:if test='!"".equals(cityName)'>
        					<s:property value="cityName"></s:property>
        			</s:if>
        	</span>
        </td>
        <%-- 柜台分类 --%>
        <td><span style="margin: 5px;"><s:property value="counterCategory"></s:property></span></td>
        <%-- 所属系统 --%>
        <td><span style="margin: 5px;"><s:property value="belongFactionName"></s:property></span></td>
        <%-- 渠道 --%>
        <td><span style="margin: 5px;"><s:property value="channelName"></s:property></span></td>
        <%-- 经销商--%>
        <td>
        	<span style="margin: 5px;">
        		<s:if test='!"".equals(resellerName)||!"".equals(resellerCode)'>
        			<s:if test='!"".equals(resellerName)'>
        				<s:if test='!"".equals(resellerCode)'>
        					<s:property value="'('+resellerCode+')'+resellerName"></s:property>
        				</s:if>
        				<s:else>
        					<s:property value="resellerName"></s:property>
        				</s:else>
        			</s:if>
        			<s:else>
        				<s:if test='!"".equals(resellerName)'>
        					<s:property value="'('+resellerCode+')'+resellerName"></s:property>
        				</s:if>
        				<s:else>
        					<s:property value="resellerCode"></s:property>
        				</s:else>
        			</s:else>
        		</s:if>
        	</span>
        </td>
        <%-- 商场名称 --%>
        <td><span style="margin: 5px;"><s:property value="mallName"></s:property></span></td>
        <%-- 柜台英文名称 --%>
        <td><span style="margin: 5px;"><s:property value="foreignName"></s:property></span></td>
        <%-- 柜台地址 --%>
        <td><span style="margin: 5px;"><s:property value="address"></s:property></span></td>
        <%-- 柜台员工数 --%>
        <td><span style="margin: 5px;"><s:property value="employeeNum"></s:property></span></td>
        <%-- 柜台面积 --%>
        <td><span style="margin: 5px;"><s:property value="counterSpace"></s:property></span></td>
        <%-- 柜台电话--%>
        <td><span style="margin: 5px;"><s:property value="counterTelephone"></s:property></span></td>
        <%--柜台状态 --%>
        <td><span style="margin: 5px;"><s:property value="statusExcel"></s:property></span></td>
		<%--柜台级别 --%>
		<td><span style="margin: 5px;"><s:property value="counterLevelExcel"></s:property></span></td>
		<%--是否有POS机 --%>
		<td><span style="margin: 5px;"><s:property value="posFlagExcel"></s:property></span></td>
        <%-- 经度--%>
        <td><span style="margin: 5px;"><s:property value="longitude"></s:property></span></td>
        <%-- 纬度--%>
        <td><span style="margin: 5px;"><s:property value="latitude"></s:property></span></td>
        <%-- 柜台类型--%>
        <td><span style="margin: 5px;"><s:property value="managingType2"></s:property></span></td>
        <%-- 银联设备号--%>
        <td><span style="margin: 5px;"><s:property value="equipmentCode"></s:property></span></td>
        <%--错误原因 --%>        
        <td><span style="margin: 5px;"><s:property value="errorInfoList"></s:property></span></td>
        <%-- 操作 --%>
        <td>
        	<span style="margin: 5px;">
        		<a href="#" class="delete" onclick="BINOLBSCNT06.delErrorCounter('<s:property value='counterCode'></s:property>');return false;";>
        			<span class="ui-icon icon-delete"></span>
                	<span class="button-text"><s:text name="counter.delete"></s:text></span>
        		</a>
        	</span>
        </td>
	</tr>
</s:iterator>
</table>
</s:i18n>