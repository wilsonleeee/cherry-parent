<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTTPL03">
<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	<thead>
		<tr>
			<th style="width:40px;">No.</th>
			<th class="center"><@s.text name="cttpl.variableCode" /></th>
			<th><@s.text name="cttpl.variableName" /></th>
			<th><@s.text name="cttpl.variableValue" /></th>
			<th><@s.text name="cttpl.basicVariable" /></th>
			<th class="center"><@s.text name="cttpl.operatorChar" /></th>
			<th><@s.text name="cttpl.computedValue" /></th>
			<th class="center"><@s.text name="cttpl.type" /></th>
			<th><@s.text name="cttpl.comments" /></th>
			<th><@s.text name="cttpl.validFlag" /></th>
			<th class="center"><@s.text name="cttpl.act" /></th>
		</tr>
	</thead>
	<tbody>
		<#if (paramList?? && paramList?size>0)>
			<#list paramList as paramInfo >
				<tr class="<#if paramInfo_index%2 == 0>odd<#else>even</#if>">
					<td><span>${(paramInfo_index+1)!?html}</span></td>
					<td class="center"><span>${(paramInfo.variableCode)!?html}</span></td>
					<td><span>${(paramInfo.variableName)!?html}</span></td>
					<td><span>${(paramInfo.variableValue)!?html}</span></td>
					<td>
						<span>
							<#if paramInfo.basicVariable?exists>
								（${(paramInfo.basicVariable)!?html}）
							</#if>
							${(paramInfo.basicVariableName)!?html}
						</span>
					</td>
					<td class="center">
						<span>
							<#if paramInfo.type?exists && paramInfo.type == 1 >
								<#if paramInfo.operatorChar?exists >
									${application.CodeTable.getVal("1275","${(paramInfo.operatorChar)!?html}")}
								</#if>
							<#elseif paramInfo.type?exists && paramInfo.type == 3>
								<#if paramInfo.operatorChar?exists >
									${application.CodeTable.getVal("1302","${(paramInfo.operatorChar)!?html}")}
								</#if>
							</#if>
						</span>
					</td>
					<td class="center"><span>${(paramInfo.computedValue)!?html}</span></td>
					<td class="center">
						<span>
							<#if paramInfo.type?exists && paramInfo.type != 2 >
								<span class="highlight"><@s.text name="cttpl.yes" /></span>
							<#else>
								<@s.text name="cttpl.no" />
							</#if>
						</span>
					</td>
					<td>
						<span>
							<a class="description" title='<@s.text name="cttpl.comments" /> | ${(paramInfo.comments)!?html}'>
								${(paramInfo.commentsCut)!?html}
							<a>
						</span>
					</td>
					<td>
						<span>
							<#if ((paramInfo.validFlag)! == "1") >
								<span class='ui-icon icon-valid'></span>
							<#else>
								<span class='ui-icon icon-invalid'></span>
							</#if>
							<#if paramInfo.validFlag?exists >
								${application.CodeTable.getVal("1196","${(paramInfo.validFlag)!?html}")}
							</#if>
						</span>
					</td>
					<td class="center">
						<span>
							<#if queryType?exists && queryType == '1'>
								<@cherry.show domId="BINOLCTTPL03EDI">
									<a class="edit" onclick="BINOLCTTPL03.editDialog('${(paramInfo.variableCode)!?html}','${(paramInfo.type)!?html}');">
										<span class="ui-icon icon-edit"></span>
					      				<span class="button-text">
					      					<@s.text name="global.page.edit" />
					      				</span>
					      			</a>
								</@cherry.show>
							</#if>
							<#if queryType?exists && queryType == '2'>
				      			<#if ((paramInfo.validFlag)! == "1") >
				      				<@cherry.show domId="BINOLCTTPL03DIS">
						      			<a class="delete" onclick="BINOLCTTPL03.disableDialog('${(paramInfo.associateId)!?html}','0');">
											<span class="ui-icon icon-disable"></span>
						      				<span class="button-text">
						      					<@s.text name="global.page.disable" />
						      				</span>
						      			</a>
					      			</@cherry.show>
				      			<#else>
				      				<@cherry.show domId="BINOLCTTPL03ENA">
						      			<a class="delete" onclick="BINOLCTTPL03.disableDialog('${(paramInfo.associateId)!?html}','1');">
											<span class="ui-icon icon-enable"></span>
						      				<span class="button-text">
						      					<@s.text name="global.page.enable" />
						      				</span>
						      			</a>
					      			</@cherry.show>
				      			</#if>
			      			</#if>
						</span>
					</td>
				</tr>
			</#list>
		<#else>
			<tr><td valign="top" colspan="11" class="dataTables_empty"><@s.text name="table_sZeroRecords" /></td></tr>
		</#if>			
	</tbody>
</table>
</@s.i18n>
