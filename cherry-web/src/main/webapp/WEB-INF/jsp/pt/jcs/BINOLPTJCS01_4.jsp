<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 产品分类选项值保存URL --%>
<s:url id="save_Url" action="BINOLPTJCS01_saveVal">
	<s:param name="brandInfoId">${brandInfoId}</s:param>
</s:url>
<%-- 产品分类属性一览URL --%>
<s:url id="search_Url" action="BINOLPTJCS01_searchVal"/>
<%-- ========================= 产品分类属性值编辑 ============================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS01">
<td>
	<input type="hidden" name="propValId" value="<s:property value='category.propValId'/>"/><%-- 产品分类选项值ID --%>
	<span><input name="propValueCherry" class="date" value="<s:property value='category.propValueCherry'/>"/></span><%-- 属性代码 --%>
</td>
 <td>
	<span><input name="propValueCN" class="text" value="<s:property value='category.propValueCN'/>"/><span 
	class="highlight"><s:text name="global.page.required"/></span></span><%-- 中文名称 --%>
</td>
<td><span><input name="propValueEN" class="text" value="<s:property value='category.propValueEN'/>"/></span></td><%-- 英文名称 --%>
<td class="center">
	<a href="#" onclick="BINOLPTJCS01.save(this,'${save_Url}',1);return false;"><s:text name="global.page.save"/></a>|<a 
		href="#" onclick="BINOLPTJCS01.cancel('${search_Url}');return false;"><s:text name="global.page.cancle"/></a>
</td>
</s:i18n>