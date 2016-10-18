<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>

<div id="sEcho"><@s.property value="sEcho"/></div>
<div id="iTotalRecords"><@s.property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><@s.property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<@s.iterator value="origBrandList" id="origBrandMap">
<ul>
<@s.if test='%{checkType != null && checkType == "radio"}'>
	<li><input name="origBrandCode" type="radio"  class="checkbox" value='<@s.property value="#origBrandMap.originalBrand"/>'/></li>
</@s.if>
<@s.else>
	<li><input name="origBrandCode" type="checkbox"  class="checkbox" value='<@s.property value="#origBrandMap.originalBrand"/>'/></li>
</@s.else>
<li><span><@s.property value="#origBrandMap.originalBrand"/></span></li>
<li><span><@s.property value="#origBrandMap.origBrandName"/></span></li>
</ul>
</@s.iterator>
</div>