<@s.i18n name="i18n.ss.BINOLSSPRM69">
<div class="section-header clearfix">
  	<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><@s.text name="SSPRM69_relationGroupTitle"/></strong>
  	<@s.url id="initRelationAddUrl" value="/ss/BINOLSSPRM69_initPage">
  		<@s.param name="params.pageNo">2</@s.param>
  		<@s.param name="brandId">${brandId}</@s.param>
  	</@s.url>
  	<a  href="${initRelationAddUrl }" class="add right" onclick="openWin(this);return false;">
  		<span class="ui-icon icon-add"></span>
  		<span class="button-text"><@s.text name="SSPRM69_relationGroupAdd"/></span>
  	</a>
  	<span class="green"><@s.text name="SSPRM69_tips2"/></span>
</div>
<div class="section-content">
	<table id="ruleRelationGroupTable" border="0" cellpadding="0" cellspacing="0" width="100%">
  		<thead>
		 	<tr>
        		 <th scope="col" style="width: 5%;"><@s.text name="No"/></th><#-- No. -->
		         <th scope="col" style="width: 20%;"><@s.text name="SSPRM69_relationGroupName"/></th><#-- 分组名称 -->
		         <th scope="col"><@s.text name="SSPRM69_relationGroupComments"/></th><#-- 分组描述 -->
		         <th scope="col" class="center" style="width: 10%;"><@s.text name="SSPRM69_validFlag"/></th><#-- 有效区分 -->
		         <th scope="col" class="center" width="10%"><@s.text name="SSPRM69_act"/></th><#-- 操作 -->
		    </tr>
		</thead>
		<tbody id="ruleRelationGroupBody">
			<#if ruleRelationGroupList?exists && ruleRelationGroupList?size gt 0>
				<#list ruleRelationGroupList as ruleRelationGroup>
				<tr class="<#if ruleRelationGroup_index % 2==0>odd<#else>even</#if>" >
					<td>${ruleRelationGroup_index + 1! }</td>
					<td>${ruleRelationGroup.groupName! }</td>
					<td>${ruleRelationGroup.comments! }</td>
					<td class="center">
						<#if ruleRelationGroup.validFlag?exists && ruleRelationGroup.validFlag == '1'>
							<span class="ui-icon icon-valid"><span class="hide">${ruleRelationGroup.groupName! }</span></span>
						<#else>
							<span class="ui-icon icon-invalid"></span>
						</#if>
					</td>
					<td class="center">
						<@s.url id="initRelationAddUrl" value="/ss/BINOLSSPRM69_initPage">
					  		<@s.param name="params.pageNo">5</@s.param>
					  		<@s.param name="params.groupNo">${ruleRelationGroup.groupNo! }</@s.param>
					  		<@s.param name="brandId">${brandId! }</@s.param>
					  	</@s.url>
						<a  href="${initRelationAddUrl }" class="edit" onclick="openWin(this);return false;">
							<span class="ui-icon ui-icon-document"></span>
							<span class="button-text"><@s.text name="SSPRM69_view"/></span>
						</a>
						<@s.url id="disOrEnableUrl" value="/ss/BINOLSSPRM69_disOrEnableRelation">
					  		<@s.param name="params.groupNo">${ruleRelationGroup.groupNo! }</@s.param>
					  	</@s.url>
						<#if ruleRelationGroup.validFlag?exists && ruleRelationGroup.validFlag == '1'>
							<a href="${disOrEnableUrl }" class="edit" onclick="PRM69.disOrEnableDialog(this,'0');return false;">
								<span class="ui-icon icon-disable"></span>
								<span class="button-text"><@s.text name="global.page.disable"/></span>
							</a>
						<#else>
							<a href="${disOrEnableUrl }" class="edit" onclick="PRM69.disOrEnableDialog(this,'1');return false;">
								<span class="ui-icon icon-enable"></span>
								<span class="button-text"><@s.text name="global.page.enable"/></span>
							</a>
						</#if>
					</td>
				</tr>
				</#list>
			<#else>
				<tr>
					<td colspan="5"><@s.text name="table_sZeroRecords"/></td>
				</tr>
			</#if>
		</tbody>
    </table>
</div>   
</@s.i18n>