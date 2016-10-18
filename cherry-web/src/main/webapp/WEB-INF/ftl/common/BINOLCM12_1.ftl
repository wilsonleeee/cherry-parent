<#-- 扩展属性值添加,编辑画面模板 -->
<#assign index=0 />
<#-- 扩展属性数 -->
<#assign num=0 />
<#if (prtExtList?? && prtExtList?size > 0)>
	<#list prtExtList as prtExt>
		<#-- 奇校验 -->
		<#if (parity != 0) && prtExt_index==0>
			<#assign index=1 />
		<#elseif (prtExt_index+index) % 2 == 0><tr>
		</#if>
		<#if prtExt.groupName?if_exists != ""><#-- checkbox框 -->
			<@getColumn2 index=num column=prtExt></@getColumn2>
			<#assign num=num + (prtExt.groupList)?size />
		<#else><#-- 输出text框或select框或radio框 -->
			<@getColumn1 index=num column=prtExt></@getColumn1>
			<#assign num=num + 1 />
		</#if>
		<#if (((prtExt_index+index) % 2 == 1) || !prtExt_has_next) && index == 0></tr></#if>
	</#list>
</#if>
<#-- 宏定义(输出text框或select框或radio框) -->
<#macro getColumn1 index column>
	<input type="hidden" name="extendPropertyList[${index}].extendPropertyId" value="${(column.extendPropertyId)!?c}" />
	<th>${(column.propertyName)!?html}</th><#-- 扩展属性名 -->
    <td>
    	<span>
    		<#if (column.viewType)! == "select">
    			<select name="extendPropertyList[${index}].propertyValue"><#-- 下拉框 -->
    			<#if column.itemList??>
    			<#list column.itemList as item>
    				<option <#if (column.propertyValue)! == item.codeKey>selected="selected"</#if> value="${item.codeKey}">${(item.value)!?html}</option>
    			</#list>
    			</#if>
    			</select>
    		<#elseif (column.viewType)! == "radio"><#-- 单选框 -->
    		<#if column.itemList??>
    			<#list column.itemList as item>
    				<input type="radio"
	    			<#if (column.id)??><#-- 编辑画面 -->
							<#if (column.propertyValue)! == item.codeKey>checked="checked"</#if> 
					<#else><#-- 添加画面 -->
						<#if item_index == 0>checked="checked"</#if> 
					</#if>
						name="extendPropertyList[${index}].propertyValue" value="${item.codeKey}">${(item.value)!?html}</input>
				</#list>
			</#if>
    		<#else>
    			<input type="text" name="extendPropertyList[${index}].propertyValue" class="text textOth" value="${(column.propertyValue)!}"/><#-- 文本框 -->
    		</#if>
    	</span>
    </td>
</#macro>
<#-- 宏定义(输出checkbox框) -->
<#macro getColumn2 index column>
	<th>${(column.groupName)!?html}</th><#-- 扩展属性组名 -->
	<td><span>
		<#if column.groupList??>
			<#list column.groupList as group>
				<input type="hidden" name="extendPropertyList[${group_index + index}].extendPropertyId" value="${(group.extendPropertyId)!?c}" />
				<#if (group.viewType)! == "checkbox"><#-- 复选框 -->
					<input type="checkbox" <#if (group.propertyValue)! == "1">checked="checked"</#if> value="1"
					name="extendPropertyList[${group_index + index}].propertyValue" >${(group.propertyName)!?html}</input>
				</#if>
			</#list>
		</#if>
	</span></td>
</#macro>
