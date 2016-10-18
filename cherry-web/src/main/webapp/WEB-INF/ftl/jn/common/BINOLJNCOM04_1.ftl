<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<div id="sEcho"><@s.property value="sEcho"/></div>
<div id="iTotalRecords"><@s.property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><@s.property value="iTotalDisplayRecords"/></div>
<div id="aaData">

	<@s.iterator value="campaignList" id="campaignInfo" status="R">
	<#assign index><@s.property value="RowNumber"/></#assign>
		<ul>
			<#-- 编号 -->
			<li>
				<@s.property value="RowNumber"/>
				<span id="urlParams_${index}">
					<input type="hidden" id="campaignId_${index}" name="campaignId" value="<@s.property value='#campaignInfo.campaignId'/>"/>
				</span>
			</li>
			<#-- 活动名称 -->
			<li>			
				<@s.if test='#campaignInfo.campaignName != null && !"".equals(#campaignInfo.campaignName)'>
				      <@s.property value="campaignName"/>
				</@s.if>
				<@s.else>&nbsp;</@s.else>
			</li>
			<#-- 有效期开始日  -->
			<li><@s.if test='#campaignInfo.campaignFromDate != null'>
				      <@s.property value="campaignFromDate"/>
				</@s.if>
				<@s.else>&nbsp;</@s.else>
			</li>
			<#-- 有效期结束日  -->
			<li><@s.if test='#campaignInfo.campaignToDate != null'>
				      <@s.property value="campaignToDate"/>
				</@s.if>
				<@s.else>&nbsp;</@s.else>
			</li>
			<#-- 活动状态 -->
			<li>
				<@s.if test='#campaignInfo.campaignStatus != null && !"".equals(#campaignInfo.campaignStatus)'>
				      <@s.if test='"1".equals(#campaignInfo.campaignStatus)'>
				     		未开始
					  </@s.if>
					  <@s.elseif test='"2".equals(#campaignInfo.campaignStatus)'>
					  		进行中
					  </@s.elseif>
					  <@s.else>已结束</@s.else>
				</@s.if>
				<@s.else>&nbsp;</@s.else>
			</li>
			<#-- 活动设定者 -->
			<li>			
				<@s.if test='#campaignInfo.employeeName != null && !"".equals(#campaignInfo.employeeName)'>
				      <@s.property value="employeeName"/>
				</@s.if>
				<@s.else>&nbsp;</@s.else>
			</li>
			<li>
				&nbsp;
			</li>
		</ul>
	</@s.iterator>
</div>