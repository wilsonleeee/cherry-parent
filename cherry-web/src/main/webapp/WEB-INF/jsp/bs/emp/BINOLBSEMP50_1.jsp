<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.bs.BINOLBSEMP50">
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
<s:iterator value="errorBaList" id="baMap">
	<s:url id="editInit" action="BINOLBSEMP50_editInit">
        <%-- 柜台消息ID --%>
    </s:url>
	<tr id="<s:property value='baCode'></s:property>">
		<%-- 所属品牌 --%>
        <td>
        	<span style="margin: 5px;">
        		<s:property value="brandCode"></s:property>
        	</span>
        </td>
        <%-- BA号 --%>
        <td><span style="margin: 5px;">
        	<s:property value="baCode"></s:property>
        </span></td>
        <%-- BA名称 --%>
        <td>
        	<span style="margin: 5px;">
        		<a href="${editInit}?jsonStr=<s:property value='jsonStr'></s:property>&&operateFlag=<s:property value='operateFlag'></s:property>&&baCode=<s:property value='baCode'></s:property>" class="popup" onclick="javascript:openWin(this);return false;">
				<s:property value="baName"/>
				</a>
        	</span>
        </td>
        <%-- 柜台 --%>
        <td>
        	<span style="margin: 5px;">
        		<s:if test='!"".equals(counterName)||!"".equals(counterCode)'>
        			<s:if test='!"".equals(counterName)'>
        				<s:if test='!"".equals(counterCode)'>
        					<s:property value="'('+counterCode+')'+counterName"></s:property>
        				</s:if>
        				<s:else>
        					<s:property value="counterName"></s:property>
        				</s:else>
        			</s:if>
        			<s:else>
        				<s:if test='!"".equals(counterName)'>
        					<s:property value="'('+counterCode+')'+counterName"></s:property>
        				</s:if>
        				<s:else>
        					<s:property value="counterCode"></s:property>
        				</s:else>
        			</s:else>
        		</s:if>
        	</span>
        </td>
        <%-- 手机号 --%>
        <td><span style="margin: 5px;">
        	<s:property value="mobilePhone"></s:property>
        </span></td>
        <%-- 入职日期 --%>
        <td><span style="margin: 5px;">
        	<s:property value="commtDate"></s:property>
        </span></td>
        <%-- 离职日期 --%>
        <td><span style="margin: 5px;">
        	<s:property value="depDate"></s:property>
        </span></td>
        <%--错误原因 --%>        
        <td><span style="margin: 5px;"><s:property value="errorInfoList"></s:property></span></td>
        <%-- 操作 --%>
        <td>
        	<span style="margin: 5px;">
        		<a href="#" class="delete" onclick="BINOLBSEMP50.delErrorBA('<s:property value='baCode'></s:property>');return false;">
        			<span class="ui-icon icon-delete"></span>
                	<span class="button-text"><s:text name="EMP06_delete"></s:text></span>
        		</a>
        	</span>
        </td>
	</tr>
</s:iterator>
</table>
</s:i18n>