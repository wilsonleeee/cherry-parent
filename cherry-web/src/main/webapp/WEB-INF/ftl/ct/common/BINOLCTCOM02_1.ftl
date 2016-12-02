<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<@s.url id="showCustomerUrl" action="BINOLCTCOM06_init" />
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (searchRecordList?? && searchRecordList?size>0)>
	<#list searchRecordList as searchRecordInfo >
		<ul class="TEMPLATEVALUE">
			<li>
				${(searchRecordInfo.RowNumber)!?html}
				<input id="tbSearchCode_${searchRecordInfo_index}" value="${(searchRecordInfo.searchCode)!?html}" class="hide"/>
				<input id="tbRecordName_${searchRecordInfo_index}" value="${(searchRecordInfo.recordName)!?html}" class="hide"/>
				<input id="tbCustomerType_${searchRecordInfo_index}" value="${(searchRecordInfo.customerType)!?html}" class="hide"/>
				<input id="tbRecordCount_${searchRecordInfo_index}" value="${(searchRecordInfo.recordCount)!?html}" class="hide"/>
				<input id="tbFromType_${searchRecordInfo_index}" value="${(searchRecordInfo.fromType)!?html}" class="hide"/>
				<input id="tbRecordType_${searchRecordInfo_index}" value="${(searchRecordInfo.recordType)!?html}" class="hide"/>
			</li>
			<li>${(searchRecordInfo.searchCode)!?html}</li>
			<li>${(searchRecordInfo.recordName)!?html}</li>
			<li>${application.CodeTable.getVal("1198","${(searchRecordInfo.customerType)!?html}")}</li>
			<li>${(searchRecordInfo.recordCount)!?html}</li>
			<li>
				<a class="add" id="recordName" name="recordName" href="${showCustomerUrl}" onclick="BINOLCTCOM02.showCustomerInit(this,1,'${searchRecordInfo_index}');return false;">
					<span class="ui-icon ui-icon-document"></span>
					<span class="button-text">
						<@s.text name="cttpl.view" />
					</span>
				</a>
				<a class="add" onclick="BINOLCTCOM02.chooseCommObj(1,'${searchRecordInfo_index}');return false;">
					<span class="ui-icon icon-search"></span>
					<span class="button-text">
						<@s.text name="cttpl.choose" />
					</span>
					<input id="templateCode" value="${(templateInfo.templateCode)!?html}" class="hide" type="text" />
					<input id="contents" value="${(templateInfo.contents)!?html}" class="hide" type="text" />
				</a>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>