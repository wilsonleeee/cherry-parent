<#-- 扩展属性展示画面模板 -->
<#assign index=0 />
<#if (prtExtList?? && prtExtList?size > 0)>
	<#list prtExtList as prtExt>
		<#-- 奇校验 -->
		<#if (parity != 0) && prtExt_index==0>
			<#assign index=1 />
		<#elseif (prtExt_index+index) % 2 == 0><tr>
		</#if>
		<th>
			<#if (prtExt.groupName?if_exists != "")>${(prtExt.groupName)!?html}<#else>${(prtExt.propertyName)!?html}</#if><#-- 扩展属性名 -->
		</th>
    	<td><span>
    	<#if (prtExt.groupName?if_exists != "")>
	    	<#list prtExt.groupList as group>
	    		${(group.propertyName)!?html}<#if group_has_next>,</#if>
	    	</#list>
    	<#else>
    		<#if (prtExt.defValue?if_exists != "")>${(prtExt.defValue)!?html}
			<#else>${(prtExt.propertyValue)!?html}
			</#if>
    	</#if>
    	
    	</span></td><#-- 扩展属性值 -->
		<#if (((prtExt_index+index) % 2 == 1) || !prtExt_has_next) && index == 0></tr></#if>
	</#list>
</#if>