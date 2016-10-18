<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<script type="text/javascript" src="/Cherry/js/mb/common/BINOLMBCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/cct/BINOLMBCCT05.js"></script>
<@s.i18n name="i18n.mb.BINOLMBCCT05">
<@s.url id="saveAction" action="BINOLMBCCT05_save" />
<style>
.column label {
	width:auto;
}
</style>
<div class="clearfix">
	<form id="customerInfoForm" method="post" class="inline" >
		<input type="hidden" id="customerSysId" name="customerSysId" value="${(customerSysId)!}"/>
		<input type="hidden" id="customerNumber" name="customerNumber" value="${(customerNumber)!}"/>
		<input type="hidden" id="customerNumberType" name="customerNumberType" value="${(customerNumberType)!}"/>
		<input type="hidden" id="mobileRule" name="mobileRule" value="${(mobileRule)!}"/>
		<div class="box-content clearfix" style="margin: 0px;border: none;">
			<div style="width:100%;">
				<table class="detail" cellpadding="0" cellspacing="0">
					<tr>
						<th>
							<label><@s.text name="mbcct.customerName"/><span class="highlight">*</span></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(customerName)!}
							<#else>
								<input name="customerName" class="text" type="text" value="${(customerName)!}"/>
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.company"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(company)!}
							<#else>
								<input name="company" class="text" type="text" value="${(company)!}"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.gender"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								<@s.property value='#application.CodeTable.getVal("1006", ${(ccGender)!})' />
							<#else>
								<@s.radio name="ccGender" listKey="CodeKey" listValue="Value" list='#application.CodeTable.getCodes("1006")' value="${(ccGender)!}"></@s.radio>
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.post"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(post)!}
							<#else>
								<input name="post" class="text" type="text" value="${(post)!}"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.mobilePhone"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccMobilePhone)!}
							<#else>
								<input name="ccMobilePhone" class="text" type="text" value="${(ccMobilePhone)!}"/>
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.industry"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(industry)!}
							<#else>
								<input name="industry" class="text" type="text" value="${(industry)!}"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.telephone"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccTelephone)!}
							<#else>
								<input name="ccTelephone" class="text" type="text" value="${(ccTelephone)!}"/>
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.zip"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccZip)!}
							<#else>
								<input name="ccZip" class="text" type="text" value="${(ccZip)!}"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.birthDay"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccBirthDay)!}
							<#else>
								<span><@s.textfield  id="ccBirthDay" name="ccBirthDay" cssClass="date" style="width:90px" value="${(ccBirthDay)!}" /></span>
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.messageId"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccMessageId)!}
							<#else>
								<input name="ccMessageId" class="text" type="text" value="${(ccMessageId)!}"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.customerType"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								<@s.property value='#application.CodeTable.getVal("1286", ${(customerType)!})' />
							<#else>
								<@s.select name="customerType" list='#application.CodeTable.getCodes("1286")' listKey="CodeKey" listValue="Value" value="${(customerType)!}" />
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.isReceiveMsg"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								<@s.property value='#application.CodeTable.getVal("1231", ${(ccIsReceiveMsg)!})' />
							<#else>
								<@s.radio name="ccIsReceiveMsg" listKey="CodeKey" listValue="Value" list='#application.CodeTable.getCodes("1231")' value="${(ccIsReceiveMsg)!}"></@s.radio>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.email"/></label>
						</th>
						<td colspan="3">
							<#if (pageType?? && pageType=="S") >
								${(ccEmail)!}
							<#else>
								<input name="ccEmail" class="text" type="text" value="${(ccEmail)!}" style="width:75%"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.province"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccProvince)!}
							<#else>
								<input name="ccProvince" class="text" type="text" value="${(ccProvince)!}"/>
							</#if>
						</td>
						<th>
							<label><@s.text name="mbcct.city"/></label>
						</th>
						<td>
							<#if (pageType?? && pageType=="S") >
								${(ccCity)!}
							<#else>
								<input name="ccCity" class="text" type="text" value="${(ccCity)!}"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.address"/></label>
						</th>
						<td colspan="3">
							<#if (pageType?? && pageType=="S") >
								${(ccAddress)!}
							<#else>
								<input name="ccAddress" class="text" type="text" value="${(ccAddress)!}" style="width:75%;"/>
							</#if>
						</td>
					</tr>
					<tr>
						<th>
							<label><@s.text name="mbcct.memo"/></label>
						</th>
						<td colspan="3">
							<#if (pageType?? && pageType=="S") >
								<span>${(ccMemo)!}</span>
							<#else>
								<textarea cols="" rows="" name="ccMemo" style="width: 75%;height:32px;" >${(ccMemo)!}</textarea>
							</#if>
						</td>
					</tr>
				</table>
            </div>
		</div>
	</form>
	<div class="center clearfix" id="pageButton">
		<#if (pageType?? && pageType=="E") >
			<button class="save" type="button" onClick="BINOLMBCCT05.doSave('${(saveAction)!}','${(pageType)!}');return false;">
				<span class="ui-icon icon-save"></span>
				<span class="button-text"><@s.text name="mbcct.save" /></span>
			</button>
			<button class="back" type="button" onClick="closeCherryDialog('customerDialog',this);">
				<span class="ui-icon icon-cancel"></span>
				<span class="button-text"><@s.text name="mbcct.cancel" /></span>
			</button>
		<#else>
			<#if (pageType?? && pageType=="S") >
				<button class="back" type="button" onClick="closeCherryDialog('customerDialog',this);">
					<span class="ui-icon icon-cancel"></span>
					<span class="button-text"><@s.text name="mbcct.cancel" /></span>
				</button>
			<#else>
				<button class="save" type="button" onClick="BINOLMBCCT05.doSave('${(saveAction)!}','');return false;">
					<span class="ui-icon icon-save"></span>
					<span class="button-text"><@s.text name="mbcct.save" /></span>
				</button>
				<button class="save" type="button" onClick="BINOLMBCCT05.doCancel();return false;">
					<span class="ui-icon icon-cancel"></span>
					<span class="button-text"><@s.text name="mbcct.cancel" /></span>
				</button>
			</#if>
		</#if>
	</div>
</div>
</@s.i18n>


