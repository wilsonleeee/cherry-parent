<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- 产品分类保存URL --%>
<s:url id="save_Url" action="BINOLPTJCS01_save"/>
<%-- 产品分类一览URL --%>
<s:url id="search_Url" action="BINOLPTJCS01_search"/>
<%-- ========================= 产品分类编辑返回页 ============================= --%>
<s:i18n name="i18n.pt.BINOLPTJCS01">
 <td>
 	<input type="hidden" name="propId" value="<s:property value='category.propId'/>"/><%-- 产品分类ID --%>
	<span><input name="propNameCN" type="text" class="text" value="<s:property value='category.propNameCN'/>"/><span 
	class="highlight"><s:text name="global.page.required"/></span></span><%-- 分类中文名称 --%>
</td>
<td><span><input name="propNameEN" type="text" class="text" value="<s:property value='category.propNameEN'/>"/></span></td><%-- 分类英文名称 --%>
<td><%-- 终端下发  --%>
	<s:if test="optFlag > 0">
		<span>
			<s:select name="teminalFlag" list="#application.CodeTable.getCodes('1119')"
				listKey="CodeKey" listValue="Value" value="category.teminalFlag"/>
		</span>
	</s:if>
</td>
<td id="<s:property value='category.propId'/>_<s:property value='category.viewSeq'/>"><span></span></td><%--显示顺序 --%>
<td class="center">
	<a href="#" onclick="BINOLPTJCS01.save(this,'${save_Url}');return false;"><s:text name="global.page.save"/></a>|<a 
		href="#" onclick="BINOLPTJCS01.search('${search_Url}');return false;"><s:text name="global.page.cancle"/></a>
</td>
</s:i18n>