<#-- 沟通信息内容列表模板的宏定义 -->
<#macro getTrItem activityList=[] msgList=[]>
	<#if (msgList?? && msgList?size>0)>
		<#if (msgList?size>1)>
			<#list msgList as msgResult >
				<@getTrItemInfo activityList=activityList msgResult=msgResult msgEditFlag='N' msgDelFlag='Y' />
			</#list>
		<#else>
			<#list msgList as msgResult >
				<@getTrItemInfo activityList=activityList msgResult=msgResult msgEditFlag='N' msgDelFlag='N' />
			</#list>
		</#if>
	<#else>
		<@getTrItemInfo activityList=activityList msgResult={} msgEditFlag='N' msgDelFlag='N'/>
	</#if>
</#macro>

<#macro getTrItemInfo activityList=[] msgResult={} msgEditFlag='N' msgDelFlag='N' >
	<tr id="msgInfoBox">
		<td><@s.select id="messageType" name="messageType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="${(msgResult.messageType)!?html}" /></td>
		<#if msgResult.smsChannel?exists && msgResult.smsChannel!='' >
		<td><@s.select id="smsChannel" name="smsChannel" list='#application.CodeTable.getCodes("1334")' listKey="CodeKey" listValue="Value" value="${(msgResult.smsChannel)!?html}" /></td>
		<#else>
		<td><@s.select id="smsChannel" name="smsChannel" list='#application.CodeTable.getCodes("1334")' listKey="CodeKey" listValue="Value" value="2" /></td>
		</#if>
		<td>
			<@s.text name="ctpln.conditionTitle" id="conditionTitle"/>
    		<@s.text name="ctpln.nameEmptyMessage" id="nameEmptyMessage"/>
			<input id="conditionBoxTitle" name="conditionBoxTitle" value="${conditionTitle}" class="hide" type="text" />
			<input id="expNameEmpty" name="expNameEmpty" value="${nameEmptyMessage}" class="hide" type="text" />
			<#if msgResult.searchCode?exists && msgResult.searchCode!='' >
				<span>
					<a id="linkRecordName" class="description" title="${conditionTitle}|${(msgResult.comments)!?html}" href="${setCommObjInitUrl}" onclick="BINOLCTPLN02.setCommObjInit(this);return false;">
						${(msgResult.recordName)!?html}
					</a>
					<lable id="lblRecordName" class="description hide" title="${conditionTitle}|${(msgResult.comments)!?html}">
						${(msgResult.recordName)!?html}
					</lable>
					<input id="searchCode" name="searchCode" value="${(msgResult.searchCode)!?html}" class="hide" type="text" />
					<input id="recordName" name="recordName" value="${(msgResult.recordName)!?html}" class="hide" type="text" />
					<input id="customerType" name="customerType" value="${(msgResult.customerType)!?html}" class="hide" type="text" />
					<input id="recordType" name="recordType" value="${(msgResult.recordType)!?html}" class="hide" type="text" />
					<input id="recordCount" name="recordCount" value="${(msgResult.recordCount)!?html}" class="hide" type="text" />
					<input id="conditionInfo" name="conditionInfo" value="${(msgResult.conditionInfo)!?html}" class="hide" type="text" />
					<input id="comments" name="comments" value="${(msgResult.comments)!?html}" class="hide" type="text" />
					<input id="fromType" name="fromType" value="${(msgResult.fromType)!?html}" class="hide" type="text" />
				</span>
			<#else>
				<span>
					<a id="linkRecordName" title="${conditionTitle}|" href="${setCommObjInitUrl}" onclick="BINOLCTPLN02.setCommObjInit(this);return false;">
						<@s.text name="ctpln.allMembers" />
					</a>
					<lable id="lblRecordName" title="${conditionTitle}|" class="hide"><@s.text name="ctpln.allMembers" /></lable>
					<input id="searchCode" name="searchCode" value="" class="hide" type="text" />
					<input id="recordName" name="recordName" value="" class="hide" type="text" />
					<input id="customerType" name="customerType" value="" class="hide" type="text" />
					<input id="recordType" name="recordType" value="" class="hide" type="text" />
					<input id="recordCount" name="recordCount" value="" class="hide" type="text" />
					<input id="conditionInfo" name="conditionInfo" value="" class="hide" type="text" />
					<input id="comments" name="comments" value="" class="hide" type="text" />
					<input id="fromType" name="fromType" value="" class="hide" type="text" />
				</span>
			</#if>
		</td>
		<td>
			<@s.text name="ctpln.message" id="contents"/>
			<#if msgResult.contents?exists && msgResult.contents!='' >
				<span>
					<input id="contents" name="contents" value="${(msgResult.contents)!?html}" class="hide" type="text" />
					<input id="isTemplate" name="isTemplate" value="${(msgResult.isTemplate)!?html}" class="hide" type="text" />
					<input id="templateCode" name="templateCode" value="${(msgResult.templateCode)!?html}" class="hide" type="text" />
					<a id="linkContents" class="description" title="${contents}|${(msgResult.contents)!?html}" href="${setMessageInitUrl}" onclick="BINOLCTPLN02.setMessageInit(this);return false;">
						<#if (msgResult.contents?length > 30) >
							${(msgResult.contents)?substring(0,30)!?html} ...
						<#else>
							${(msgResult.contents)!?html}
						</#if>
					</a>
					<lable id="lblContents" class="description hide" title="${contents}|${(msgResult.contents)!?html}">
						<#if (msgResult.contents?length > 30) >
							${(msgResult.contents)?substring(0,30)!?html} ...
						<#else>
							${(msgResult.contents)!?html}
						</#if>
					</lable>
				</span>
			<#else>
				<span>
					<input id="contents" name="contents" value="${(msgResult.contents)!?html}" class="hide" type="text" />
					<input id="isTemplate" name="isTemplate" value="${(msgResult.isTemplate)!?html}" class="hide" type="text" />
					<input id="templateCode" name="templateCode" value="${(msgResult.templateCode)!?html}" class="hide" type="text" />
					<a id="linkContents" title="${contents}|" href="${setMessageInitUrl}" onclick="BINOLCTPLN02.setMessageInit(this);return false;">
						<@s.text name="ctpln.chooseProm" />
					</a>
					<lable id="lblContents" class="hide" title="${contents}|"><@s.text name="ctpln.chooseProm" /></lable>
				</span>
			</#if>
		</td>
		<td>
			<select id="activityCode" name="activityCode" style="width:200px">
				<#if (activityList?? && activityList?size>0)>
					<option value=""><@s.text name="ctpln.defaultValue"/></option>
					<#list activityList as activityInfo >
						<#if activityInfo.campaignCode?exists && msgResult.activityCode?exists && activityInfo.campaignCode=msgResult.activityCode >
							<option value="${(activityInfo.campaignCode)!?html}" selected="selected">${(activityInfo.campaignName)!?html}</option>
						<#else>
							<option value="${(activityInfo.campaignCode)!?html}">${(activityInfo.campaignName)!?html}</option>
						</#if>
					</#list>
				<#else>
					<option value=""><@s.text name="ctpln.defaultValue"/></option>
				</#if>
			</select>
		</td>
		<td>
			<a id="editButton" href="${setMessageInitUrl}" class="edit" <#if msgEditFlag='N' >style="display: none;"</#if> onclick="BINOLCTPLN02.setMessageInit(this);return false;">
				<span class="ui-icon icon-edit"></span>
				<span class="button-text"><@s.text name="ctpln.edit" /></span>
			</a>
			<a id="testButton" href="${(testMsgAction)!}" class="edit" onclick="BINOLCTPLN02.setReceivedCodeInit(this);return false;">
				<span class="ui-icon icon-ok"></span>
				<span class="button-text"><@s.text name="ctpln.test" /></span>
			</a>
			<a id="msgDeleteButton" class="delete" <#if msgDelFlag='N' >style="display: none;"</#if> onclick="BINOLCTPLN02.viewRemove(this);return false;">
				<span class="ui-icon icon-delete"></span>
				<span class="button-text"><@s.text name="ctpln.delete" /></span>
			</a>
		</td>
	</tr>
</#macro>

<#-- 隐藏沟通信息内容的宏定义 -->
<#macro getHideMsgInfo>
<table id="addNewMessageinfo" class="hide">
	<tbody>
		<#-- 引用沟通信息内容列表模板的宏定义 -->
		<@getTrItemInfo activityList=activityList msgResult={} msgEditFlag='Y' msgDelFlag='N'/>
	</tbody>
</table>
</#macro>

<#-- 沟通对象搜索结果页面宏定义 -->
<#macro getSearchRecord>
	<div id="searchRecordTabs" class="hide">
		<div class="ui-tabs">
			<ul class="ui-tabs-nav clearfix">
				<li><a href="#tabs-1"><@s.text name="ctpln.searchCondition"/></a></li>
				<li><a href="#tabs-2"><@s.text name="ctpln.searchResult"/></a></li>
				<li><a href="#tabs-3"><@s.text name="ctpln.excelImport"/></a></li>
			</ul>
			<div calss="ui-tabs-panel" id="tabs-1">
			</div>
			<div calss="ui-tabs-panel" id="tabs-2">
				<@searchRecordPage />
			</div>
			<div calss="ui-tabs-panel" id="tabs-3">
			</div>
		</div>
	</div>
</#macro>

<#macro searchRecordPage>
	<form id="searchRecordFormTem">
		<div class="box">
			<div class="box-header">
				<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
			</div>
			<div class="box-content clearfix">
				<div class="column" style="width:49%;">
				<@s.text name="ctpln.allvalue" id="allvalue"/>
				<p>
					<label><@s.text name="ctpln.searchCode"/></label>
					<input name="searchCode" class="text" type="text" />
				</p>
				<p>
					<label><@s.text name="ctpln.searchName"/></label>
					<input name="recordName" class="text" type="text" />
				</p>
		        </div>
				<div class="column last" style="width:50%;">
				<p>
					<label style="width:90px"><@s.text name="ctpln.customerType"/></label>
		       		<@s.select name="customerType" list='#application.CodeTable.getCodes("1198")' listKey="CodeKey" listValue="Value" 
		       		headerKey="" headerValue="%{#allvalue}" value="" />
		       	</p>
		        </div>
			</div>
			<p class="clearfix">
				<button class="right search"  onclick="BINOLCTPLN02.searchSearchReCord();return false;">
		          	<span class="ui-icon icon-search-big"></span>
		          	<span class="button-text"><@s.text name="global.page.search" /></span>
				</button>
			</p>
		</div>
	</form>
	<div id="searchRecordDetail" class="section">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="global.page.list" />
	        </strong>
		</div>
		<div class="section-content">
			<table id="searchReCordDataTableTem" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
					<tr>
						<th><@s.text name="No." /></th>
						<th><@s.text name="ctpln.searchCode" /></th>
						<th><@s.text name="ctpln.searchName" /></th>
						<th><@s.text name="ctpln.customerType" /></th>
						<th><@s.text name="ctpln.recordCount" /></th>
						<th><@s.text name="ctpln.act" /></th>
					</tr>
				</thead>
				<tbody></tbody>
			</table>
		</div>
	</div>
</#macro>

