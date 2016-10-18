<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (popMsgTemplateList?? && popMsgTemplateList?size>0)>
	<#list popMsgTemplateList as templateInfo >
		<ul>
			<li>${(templateInfo.RowNumber)!?html}</li>
			<li>${(templateInfo.templateCode)!?html}</li>
			<li>${(templateInfo.templateName)!?html}</li>
			<li>${application.CodeTable.getVal("1197","${(templateInfo.templateUse)!?html}")}</li>
			<li><a href="${detailUrl}" onclick="javascript:openWin(this);return false;" >${(templateInfo.contents)!?html}</a>
				<span id="tplParams_${templateInfo.RowNumber}" >
        			<input type="hidden" name="templateCode" value="${(templateInfo.templateCode)!?html}"/>
	        	</span>
			</li>
			<li>${application.CodeTable.getVal("1198","${(templateInfo.customerType)!?html}")}</li>
			<li>
				<a href="${editUrl}" class="edit" onclick="">
					<span class="ui-icon icon-copy"></span>
					<span class="button-text">
						<@s.text name="cttpl.copy" />
					</span>
				</a>
			</li>
		</ul>
	</#list>
</#if>
</div>