<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<div id="sEcho"><@s.property value="sEcho"/></div>
<div id="iTotalRecords"><@s.property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><@s.property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<#if (popRuleInfoList?? && popRuleInfoList?size>0)>
<@s.iterator value="popRuleInfoList" id="popRuleInfo">
<ul>
	<li><input name="selRule" type="checkbox"  class="checkbox" value='<@s.property value="#popRuleInfo.campInfo"/>'/></li>
<li><@s.property value="#popRuleInfo.campaignName"/></li>
</ul>
</@s.iterator>
</#if>
</div>