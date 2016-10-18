<@s.i18n name="i18n.ct.BINOLCTPLN02">
<#-- 引用宏 -->
<#include "/WEB-INF/ftl/ct/pln/BINOLCTPLN02_mac.ftl">
<@s.url id="testMsgAction" action="BINOLCTCOM05_init" />
<@s.url id="setCommObjInitUrl" action="BINOLCM33_init" namespace="/common"/>
<@s.url id="setMessageInitUrl" action="BINOLCTCOM04_init" />
<@s.text name='ctpln.anyValue' id="anyValue"/>
<div class="box-header">
	<span><@s.text name="ctpln.timeRange"/>：</span>
	<@s.select name="frequencyCode" id="frequencyCode" list='#application.CodeTable.getCodes("1260")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{#anyValue}" value="${(frequencyCode)!?html}"  onchange="BINOLCTPLN02.frequencyChange();return false;" />
	<span id="sendTimeRange" <#if frequencyCode?exists && frequencyCode=='0' >class="hide"</#if>>
		<input name="sendBeginTime" id="sendBeginTime" type="text" style="width:50px" class="text" value="${(sendBeginTime)!?html}"/>
		<@s.text name="ctpln.toTime"/>
		<input name="sendEndTime" id="sendEndTime" type="text" style="width:50px" class="text" value="${(sendEndTime)!?html}"/>
		<@s.text name="ctpln.timeExpansion" />
	</span>
</div>
<table class="editable" id="messageListTable" style="width:100%; margin-bottom:9px;" border="0" cellpadding="0" cellspacing="0">
<thead>
	<tr>
		<th style="width:10%;"><label><@s.text name="ctpln.messageType"/></label></th>
		<th style="width:10%;"><label><@s.text name="ctpln.smsChannel"/></label></th>
		<th style="width:15%;"><label><@s.text name="ctpln.commCustomer"/></label></th>
		<th><label><@s.text name="ctpln.message"/><span class="highlight">*</span></label></th>
		<th style="width:15%;"><label><@s.text name="ctpln.activityName"/></label></th>
		<th style="width:17%;"><label><@s.text name="ctpln.act"/></label></th>
	</tr>
</thead>
<tbody>
    <#-- 引用沟通信息内容列表模板的宏定义 -->
	<#if msgList?exists >
		<@getTrItem activityList=activityList msgList=msgList />
	<#else>
		<@getTrItem activityList=[] msgList=[]  />
	</#if>
</tbody>
</table>
</@s.i18n>