<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<div id="sEcho"><@s.property value="sEcho"/></div>
<div id="iTotalRecords"><@s.property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><@s.property value="iTotalDisplayRecords"/></div>
<div id="aaData">
<#-- 活动组编辑URL -->
<@s.url id="editUrl" action="BINOLJNCOM03_edit" namespace="/jn"/>
	<@s.iterator value="campaignGrpList" id="campaignGrp" status="R">
	<#assign index><@s.property value="RowNumber"/></#assign>
		<ul>
			<#-- 编号 -->
			<li>
				<@s.property value="RowNumber"/>
				<span id="urlParams_${index}">
					<input type="hidden" id="campaignGrpId_${index}" name="campaignGrpId" value="<@s.property value='#campaignGrp.campaignGrpId'/>"/>
				</span>
			</li>
			<#-- 活动组名称 -->
			<li>			
				<@s.if test='#campaignGrp.groupName != null && !"".equals(#campaignGrp.groupName)'>
				      <@s.property value="groupName"/>
				</@s.if>
				<@s.else>&nbsp;</@s.else>
			</li>
			<#-- 活动类型  -->
			<li><span><@s.property value="#application.CodeTable.getVal('1112',#campaignGrp.campaignType)"/></span></li>
			<#-- 有效区分 -->
			<li>
				<@s.if test='"1".equals(#campaignGrp.validFlag)'><span class='ui-icon icon-valid'></span></@s.if>
				<@s.else><span class='ui-icon icon-invalid'></span></@s.else>
			</li>
			<li>
				<@s.if test='"1".equals(#campaignGrp.validFlag)'>
					<a class="delete" onclick = ""><span class="ui-icon icon-delete"></span><span class="button-text">停用</span></a>
					<a href ="${editUrl}" class="delete" onclick="BINOLJNCOM02.openPage(this, ${index});return false;"><span class="ui-icon icon-edit"></span><span class="button-text">编辑</span></a>
				</@s.if>
			</li>
		</ul>
	</@s.iterator>
</div>