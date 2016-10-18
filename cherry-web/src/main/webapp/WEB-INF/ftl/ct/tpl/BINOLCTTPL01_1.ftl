<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTTPL01">
<@s.url id="editUrl" action="BINOLCTCOM01_editInit" namespace="/ct"/>
<@s.url id="detailUrl" action="BINOLCTCOM01_detailInit" namespace="/ct"/>
<@s.url id="validUrl" action="BINOLCTCOM01_disable" namespace="/ct"/>
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (templateList?? && templateList?size>0)>
	<#list templateList as templateInfo >
		<@s.url id="editUrl" action="BINOLCTTPL02_editInit">
			<@s.param name="templateCode">${(templateInfo.templateCode)!?html}</@s.param>
			<@s.param name="showType">edit</@s.param>
		</@s.url>
		<@s.url id="copyUrl" action="BINOLCTTPL02_editInit">
			<@s.param name="templateCode">${(templateInfo.templateCode)!?html}</@s.param>
			<@s.param name="showType">copy</@s.param>
		</@s.url>
		<ul>
			<li>${(templateInfo.RowNumber)!?html}</li>
			<li>${(templateInfo.templateCode)!?html}</li>
			<li>${(templateInfo.templateName)!?html}</li>
			<li>${application.CodeTable.getVal("1197","${(templateInfo.templateUse)!?html}")}</li>
			<li>
				<@s.text name="cttpl.contents" id="contents"/>
				<a title="${contents}|${(templateInfo.contents)!?html}" class="description" style="color:black">${(templateInfo.contentsCut)!?html}</a>
				<span id="tplParams_${templateInfo.RowNumber}" >
        			<input type="hidden" name="templateCode" value="${(templateInfo.templateCode)!?html}"/>
	        	</span>
			</li>
			<li>${application.CodeTable.getVal("1198","${(templateInfo.customerType)!?html}")}</li>
			<li>
				<#if ((templateInfo.status)! == "1") >
					<span class='ui-icon icon-valid'></span>
				<#else>
					<span class='ui-icon icon-invalid'></span>
				</#if>
				${application.CodeTable.getVal("1137","${(templateInfo.status)!?html}")}
			</li>
			<li>
				<@cherry.show domId="BINOLCTTPL01COP">
				<a href="${(copyUrl)!}" class="edit" onclick="javascript:openWin(this);return false;">
					<span class="ui-icon icon-copy"></span>
					<span class="button-text">
						<@s.text name="cttpl.copy" />
					</span>
				</a>
				</@cherry.show>
				<#if ((templateInfo.status)! == "1") >
					<@cherry.show domId="BINOLCTTPL01EDI">
					<a href="${(editUrl)!}" class="edit" onclick="javascript:openWin(this);return false;">
		         		<span class="ui-icon icon-edit"></span>
		         		<span class="button-text">
							<@s.text name="cttpl.edit" />
		          		</span>
	             	</a>
	        		</@cherry.show>
					<@cherry.show domId="BINOLCTTPL01STO">
					<a class="delete" onclick="BINOLCTTPL01.disableTemplateDialog('${(templateInfo.templateCode)!?html}');">
						<span class="ui-icon icon-delete"></span>
	      				<span class="button-text">
	      					<@s.text name="cttpl.disable" />
	      				</span>
	      			</a>
	      			</@cherry.show>
      			</#if>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>