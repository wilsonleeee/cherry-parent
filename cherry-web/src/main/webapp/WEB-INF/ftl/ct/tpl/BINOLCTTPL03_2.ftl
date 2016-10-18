<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTTPL03">
	<form id="editForm">
		<input class="hide" name="associateId" id="associateId" value="${(paramInfo.associateId)!?html}" />
		<input class="hide" name="variableCode" id="variableCode" value="${(paramInfo.variableCode)!?html}" />
		<table class="detail">
			<tr>
				<th><@s.text name="cttpl.variableCode"/></th>
				<td>
					${(paramInfo.variableCode)!?html}
				</td>
				<th><@s.text name="cttpl.variableName"/></th>
				<td>
					${(paramInfo.variableName)!?html}
				</td>
			</tr>
			<tr>
				<th><@s.text name="cttpl.variableValue"/></th>
				<td>${(paramInfo.variableValue)!?html}</td>
				<th><@s.text name="cttpl.type"/></th>
				<td>
					<#if paramInfo.type?exists>
						${application.CodeTable.getVal("1276","${(paramInfo.type)!?html}")}
					</#if>
				</td>
			</tr>
			<#if paramInfo.type?exists && paramInfo.type == 1 >
				<tr>
					<th><@s.text name="cttpl.basicVariable"/></th>
					<td>
						<span>
							<@s.select name="basicVariable" id="basicVariable" cssStyle="width:100%" list='paramList' listKey="variableCode" listValue="variableName"  />
							<input class="hide" id="basicVariableTemp" value="${(paramInfo.basicVariable)!?html}" />
						</span>
					</td>
					<th><@s.text name="cttpl.operatorChar"/></th>
					<td>
						<span>
							<@s.select name="operatorChar" id="operatorChar" cssStyle="width:50px" list='#application.CodeTable.getCodes("1275")' listKey="CodeKey" listValue="Value" value="${(paramInfo.operatorChar)!?html}" />
							<@s.text name="cttpl.computedValue1"/>
							<input class="text" name="computedValue" id="computedValue" value="${(paramInfo.computedValue)!?html}" style="width:50px;" />
						</span>
					</td>
				</tr>
			<#elseif paramInfo.type?exists && paramInfo.type == 3 >
				<tr>
					<th><@s.text name="cttpl.basicVariable"/></th>
					<td>
						<span>
							<@s.select name="basicVariable" id="basicVariable" cssStyle="width:100%" list='paramList' listKey="variableCode" listValue="variableName"  />
							<input class="hide" id="basicVariableTemp" value="${(paramInfo.basicVariable)!?html}" />
						</span>
					</td>
					<th><@s.text name="cttpl.operatorChar"/></th>
					<td>
						<span>
							<#if paramInfo.variableCode?exists && ('XD' == paramInfo.variableCode.toString() || 'XL' == paramInfo.variableCode.toString() || 'XB' == paramInfo.variableCode.toString()) >
								<select name="operatorChar" id="operatorChar" Style="width:50px">
									<option id="after" value="2"><@s.text name="cttpl.after"/></option>
								</select>
							<#else>
								<@s.select name="operatorChar" id="operatorChar" cssStyle="width:50px" list='#application.CodeTable.getCodes("1302")' listKey="CodeKey" listValue="Value" value="${(paramInfo.operatorChar)!?html}" />
							</#if>
							<@s.text name="cttpl.computedValue1"/>
							<input class="text" name="computedValue" id="computedValue" value="${(paramInfo.computedValue)!?html}" style="width:50px;" />
							<#if paramInfo.variableCode?exists && ('XL' == paramInfo.variableCode.toString() || 'XB' == paramInfo.variableCode.toString()) >
								<@s.text name="cttpl.months"/>
							<#else>
								<@s.text name="cttpl.days"/>
							</#if>
						</span>
					</td>
				</tr>
			</#if>
			<tr>
				<th><@s.text name="cttpl.isBasicFlag"/></th>
				<td>
					<#if paramInfo.isBasicFlag?exists>
						${application.CodeTable.getVal("1303","${(paramInfo.isBasicFlag)!?html}")}
					</#if>
				</td>
				<th><@s.text name="cttpl.validFlag"/></th>
				<td>
					${application.CodeTable.getVal("1196","${(paramInfo.validFlag)!?html}")}
				</td>
			</tr>
			<tr>
				<th><@s.text name="cttpl.comments"/></th>
				<td colspan="3">
					<textarea id="comments" name="comments" style="height:50px;width:400px;"  maxlength="500">${(paramInfo.comments)!?html}</textarea>
				</td>
			</tr>
		</table>
	</form>
</@s.i18n>
