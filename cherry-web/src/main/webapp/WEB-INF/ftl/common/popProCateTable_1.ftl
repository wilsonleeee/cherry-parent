<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>

<div id="sEcho"><@s.property value="sEcho"/></div>
<div id="iTotalRecords"><@s.property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><@s.property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<@s.iterator value="popCateInfoList" id="cateInfoMap">
<ul>
<@s.if test='%{checkType != null && checkType == "radio"}'>
	<li><input name="cateInfo" type="radio"  class="checkbox" value='<@s.property value="#cateInfoMap.cateInfo"/>'/></li>
</@s.if>
<@s.else>
	<li><input name="cateInfo" type="checkbox"  class="checkbox" value='<@s.property value="#cateInfoMap.cateInfo"/>'/></li>
</@s.else>
<li><@s.property value="#cateInfoMap.cateCode"/></li>
<li><@s.property value="#cateInfoMap.cateType"/></li>
<li><@s.property value="#cateInfoMap.cateName"/></li>
</ul>
</@s.iterator>
</div>