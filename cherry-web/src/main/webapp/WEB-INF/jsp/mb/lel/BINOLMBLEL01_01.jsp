<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<s:i18n name="i18n.mb.BINOLMBLEL01">
<s:iterator value="levelList" id="it1" status="st1">
	 	<tr class="<s:if test="#st1.even">even</s:if><s:else>odd</s:else>">
	 		<td><span><s:property value="#st1.count"/></span></td>
	      	<td>
	      		<span><s:property value="levelName"/></span>
	      		<s:if test='1 == defaultFlag' >
	      			<span style="color:red;">(<s:text name="lel01.defaultLevel" />)</span>
	      		</s:if>
	      	</td><%-- 等级名称 --%>
	      	<td><span><s:property value="levelCode"/></span></td><%-- 等级代码 --%>
	      	<td>
	       		<s:iterator begin="1" end="#st1.count">
	       			<span class="ui-icon icon-star-big"></span>
	       		</s:iterator>
	      	</td><%-- 等级级别--%>
	      	<td><s:property value="memberDate"/><s:text name='%{textName}'/>
	      	</td>
	      	<td><p><s:property value="description"/></p></td><%-- 等级描述 --%>
	 	</tr>
</s:iterator>
</s:i18n>