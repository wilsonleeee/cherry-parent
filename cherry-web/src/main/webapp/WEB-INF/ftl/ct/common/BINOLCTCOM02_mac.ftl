<#macro getTrItemInfo msgResult={} msgDelFlag='N' >
	<tr id="msgInfoBox">
		<td><@s.select name="messageType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" value="${(msgResult.messageType)!?html}" /></td>
		<#if msgResult.smsChannel?exists && msgResult.smsChannel!='' >
		<td><@s.select id="smsChannel" name="smsChannel" list='#application.CodeTable.getCodes("1334")' listKey="CodeKey" listValue="Value" value="${(msgResult.smsChannel)!?html}" /></td>
		<#else>
		<td><@s.select id="smsChannel" name="smsChannel" list='#application.CodeTable.getCodes("1334")' listKey="CodeKey" listValue="Value" value="1" /></td>
		</#if>
		<td>
			<#if msgResult.contents?exists && msgResult.contents!='' >
				<@s.text name="ctpln.message" id="contents"/>
				<span>
					<input id="contents" name="contents" value="${(msgResult.contents)!?html}" class="hide" type="text" />
					<input id="isTemplate" name="isTemplate" value="${(msgResult.isTemplate)!?html}" class="hide" type="text" />
					<input id="templateCode" name="templateCode" value="${(msgResult.templateCode)!?html}" class="hide" type="text" />
					<a id="linkContents" class="description" title="${contents}|${(msgResult.contents)!?html}" href="${setMessageInitUrl}" onclick="BINOLCTCOM02.setMessageInit(this);return false;">
						<#if (msgResult.contents?length > 30) >
							${(msgResult.contents)?substring(0,30)!?html} ...
						<#else>
							${(msgResult.contents)!?html}
						</#if>
					</a>
				</span>
			<#else>
				<span>
					<input id="contents" name="contents" value="${(msgResult.contents)!?html}" class="hide" type="text" />
					<input id="isTemplate" name="isTemplate" value="${(msgResult.isTemplate)!?html}" class="hide" type="text" />
					<input id="templateCode" name="templateCode" value="${(msgResult.templateCode)!?html}" class="hide" type="text" />
					<a id="linkContents" href="${setMessageInitUrl}" onclick="BINOLCTCOM02.setMessageInit(this);return false;">
						<@s.text name="ctpln.chooseProm" />
					</a>
				</span>
			</#if>
		</td>
		<td>
			<a href="${setMessageInitUrl}" class="edit" onclick="BINOLCTCOM02.setMessageInit(this);return false;">
				<span class="ui-icon icon-edit"></span>
				<span class="button-text"><@s.text name="ctpln.edit" /></span>
			</a>
			<a href="${(testMsgAction)!}" class="edit" onclick="BINOLCTCOM02.setReceivedCodeInit(this);return false;">
				<span class="ui-icon icon-ok"></span>
				<span class="button-text"><@s.text name="ctpln.test" /></span>
			</a>
			<a id="msgDeleteButton" class="delete" <#if msgDelFlag='N' >style="display: none;"</#if> onclick="BINOLCTCOM02.viewRemove(this);return false;">
				<span class="ui-icon icon-delete"></span>
				<span class="button-text"><@s.text name="ctpln.delete" /></span>
			</a>
		</td>
	</tr>
</#macro>

<#-- 沟通信息内容列表模板的宏定义 -->
<#macro getTrItem msgList=[]>
	<#if (msgList?? && msgList?size>0)>
		<#if (msgList?size>1)>
			<#list msgList as msgResult >
				<@getTrItemInfo msgResult=msgResult msgDelFlag='Y' />
			</#list>
		<#else>
			<#list msgList as msgResult >
				<@getTrItemInfo msgResult=msgResult msgDelFlag='N' />
			</#list>
		</#if>
	<#else>
		<@getTrItemInfo msgResult={} msgDelFlag='N'/>
	</#if>
</#macro>

<#-- 隐藏沟通信息内容的宏定义 -->
<#macro getHideMsgInfo>
<table id="addNewMessageinfo" class="hide">
	<tbody>
		<#-- 引用沟通信息内容列表模板的宏定义 -->
		<@getTrItemInfo msgResult={} msgDelFlag='N'/>
	</tbody>
</table>
</#macro>

<#macro getCommObjInfoBox objResult={} objDelFlag='N' >
<div class="box4 COMMOBJECT">
	<div class="box4-header" style="height:18px;">
		<#if objDelFlag='Y' >
			<a id="objDeleteButton" class="right" onclick="BINOLCTCOM02.viewRemove(this);return false;"><span class="ui-icon icon-close"></span></a>
		<#else>
			<a id="objDeleteButton" class="right hide" onclick="BINOLCTCOM02.viewRemove(this);return false;"><span class="ui-icon icon-close"></span></a>
		</#if>
	</div>
	<div class="box4-content">
		<@s.text name="ctpln.conditionTitle" id="conditionTitle"/>
		<@s.text name="ctpln.searchResult" id="searchResultTitle"/>
		<div class="clearfix" style="height:35px;">
    		<div class="clearfix left" style="width:78%;">
        		<table id="commObjinfo" width="100%" border="0" cellpadding="0" cellspacing="0">
	        		<tbody>
						<tr>
							<th><label><@s.text name="ctpln.person"/><span class="highlight">*</span></label></th>
							<td style="width:60%;">
								<span style="width:80%;">
									<#-- 判断沟通对象搜索记录各项属性是否存在，若存在将值赋给输入框 -->
									<#if objResult.recordName?exists >
										<input id="recordName" name="recordName" style="width:90%;" title="${conditionTitle}|${(objResult.conditionDisplay)!?html}" value="${(objResult.recordName)!?html}" class="text description disabled" readonly="readonly" type="text" href="${setCommObjInitUrl}" onclick="BINOLCTCOM02.setCommObjInit(this);return false;" />
									<#else>
										<input id="recordName" name="recordName" style="width:90%;" value="" class="text disabled" readonly="readonly" type="text" href="${setCommObjInitUrl}" onclick="BINOLCTCOM02.setCommObjInit(this);return false;" />
									</#if>
									<#if objResult.conditionDisplay?exists >
										<input id="conditionDisplay" name="conditionDisplay" value="${(objResult.conditionDisplay)!?html}" class="hide" type="text" />
									<#else>
										<input id="conditionDisplay" name="conditionDisplay" value="" class="hide" type="text" />
									</#if>
									<#if objResult.recordCode?exists >
										<input id="recordCode" name="recordCode" value="${(objResult.recordCode)!?html}" class="hide" type="text" />
									<#else>
										<input id="recordCode" name="recordCode" value="" class="hide" type="text" />
									</#if>
									<#if objResult.customerType?exists >
										<input id="customerType" name="customerType" value="${(objResult.customerType)!?html}" class="hide" type="text" />
									<#else>
										<input id="customerType" name="customerType" value="" class="hide" type="text" />
									</#if>
									<#if objResult.recordType?exists >
										<input id="recordType" name="recordType" value="${(objResult.recordType)!?html}" class="hide" type="text" />
									<#else>
										<input id="recordType" name="recordType" value="" class="hide" type="text" />
									</#if>
									<#if objResult.recordCount?exists >
										<input id="recordCount" name="recordCount" value="${(objResult.recordCount)!?html}" class="hide" type="text" />
									<#else>
										<input id="recordCount" name="recordCount" value="" class="hide" type="text" />
									</#if>
									<#if objResult.conditionInfo?exists >
										<input id="conditionInfo" name="conditionInfo" value="${(objResult.conditionInfo)!?html}" class="hide" type="text" />
									<#else>
										<input id="conditionInfo" name="conditionInfo" value="" class="hide" type="text" />
									</#if>
									<#if objResult.disableCondition?exists >
										<input id="disableCondition" name="disableCondition" value="${(objResult.disableCondition)!?html}" class="hide" type="text" />
									<#else>
										<input id="disableCondition" name="disableCondition" value="" class="hide" type="text" />
									</#if>
									<#if objResult.fromType?exists >
										<input id="fromType" name="fromType" value="${(objResult.fromType)!?html}" class="hide" type="text" />
									<#else>
										<input id="fromType" name="fromType" value="" class="hide" type="text" />
									</#if>
								</span>
							</td>
							<td>
								<a href="${setCommObjInitUrl}" class="edit right" onclick="BINOLCTCOM02.setCommObjInit(this);return false;">
									<span class="ui-icon icon-edit"></span>
									<span class="button-text"><@s.text name="ctpln.editObj" /></span>
								</a>
							</td>
						</tr>
					</tbody>
				</table>
    		</div>
    		<div class="clearfix right" style="width:20%;">
    			<#-- 增加沟通信息的按钮 -->
    			<a class="add right" onclick="BINOLCTCOM02.addNewMessage(this);return false;">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="ctpln.addMessage" /></span>
				</a>
    		</div>
		</div>
		<#-- 沟通信息内容表格 -->
		<div class="clearfix">
			<table border="0" width="100%" class="editable" id="messageListTable">
	        	<thead>
		        	<tr>
		        		<th style="width:15%;"><label><@s.text name="ctpln.type"/></label></th>
		        		<th style="width:15%;"><label><@s.text name="ctpln.smsChannel"/></label></th>
		        		<th><label><@s.text name="ctpln.message"/><span class="highlight">*</span></label></th>
		        		<th style="width:15%;"><label><@s.text name="ctpln.act"/></label></th>
		        	</tr>
	        	</thead>
	        	<tbody id="planTbody">
	        		<#-- 引用沟通信息内容列表模板的宏定义 -->
	        		<#if objResult.msgList?exists >
						<@getTrItem msgList=objResult.msgList />
					<#else>
						<@getTrItem msgList=[]  />
					</#if>
	        		
	        	</tbody>
	        </table>
        </div>
	</div>
</div>
</#macro>


<#-- 沟通对象模板的宏定义 -->
<#macro getCommObjBox objList=[]>
	<#if (objList?? && objList?size>0)>
		<#if (objList?size>1)>
			<#list objList as objResult >
				<@getCommObjInfoBox objResult=objResult objDelFlag='Y' />
			</#list>
		<#else>
			<#list objList as objResult >
				<@getCommObjInfoBox objResult=objResult objDelFlag='N' />
			</#list>
		</#if>
	<#else>
		<@getCommObjInfoBox objResult={} objDelFlag='N' />
	</#if>
</#macro>

<#-- 沟通基本信息模板的宏定义 -->
<#macro getHideCommBox commResult={} >
<div class="clearfix">
	<table id="commDetailinfo" class="detail" width="100%" border="0" cellpadding="0" cellspacing="0">
	<tbody>
		<tr>
			<th><label><@s.text name="ctpln.communicationName"/><span class="highlight">*</span></label></th>
			<td>
				<#-- 沟通名称更改调用Js函数 -->
				<input name="communicationName" id="communicationName" onchange="BINOLCTCOM02.textChange(this);return false;" class="text" type="text" value="${(commResult.communicationName)!?html}" />
				<input name="communicationCode" id="communicationCode" type="text" class="text hide" value="${(commResult.communicationCode)!?html}"/>
			</td>
			<td rowspan="3" style="width:15%;">
				<#-- 增加沟通对象的按钮 -->
				<div class="clearfix">
					<a class="add right" onclick="BINOLCTCOM02.addNewCommObject(this);return false;">
						<span class="ui-icon icon-add"></span>
						<span class="button-text"><@s.text name="ctpln.addperson" /></span>
					</a>
				</div>
			</td>
		</tr>
		<tr>
			<th><label><@s.text name="ctpln.communicationTime"/><span class="highlight">*</span></label></th>
			<td>
				<div class="clearfix">
				<p>
					<#-- 沟通时间类型值更改事件调用Js函数 -->
					<span><@s.select name="timeType" list='#application.CodeTable.getCodes("1215")' listKey="CodeKey" listValue="Value" value="${(commResult.timeType)!}" onchange="BINOLCTCOM02.selectChange(this);return false;" /></span>
					<#if (commResult.timeType?exists && commResult.timeType =='3')>
						<#if (commResult.birthFlag?exists && commResult.birthFlag == '1')>
							<span><span><input type="checkbox" checked="checked" id="birthFlag_${(commResult.communicationCode)!?html}" class="checkbox" name="birthFlag" value="1"/><@s.text name="ctpln.birthFlag"/></span><span class="highlight"><@s.text name="ctpln.birthFlagMsg"/></span></span>
						<#else>
							<span><span><input type="checkbox"  id="birthFlag_${(commResult.communicationCode)!?html}" class="checkbox" name="birthFlag" value="1"/><@s.text name="ctpln.birthFlag"/></span><span class="highlight"><@s.text name="ctpln.birthFlagMsg"/></span></span>
						</#if>
					<#else>
						<span class="hide"><span><input type="checkbox"  id="birthFlag_${(commResult.communicationCode)!?html}" class="checkbox" name="birthFlag" value="1"/><@s.text name="ctpln.birthFlag"/></span><span class="highlight"><@s.text name="ctpln.birthFlagMsg"/></span></span>
					</#if>
				</p>
				</div>
				<#-- 当沟通时间选择指定时间时需要显示的页面内容 -->
				<div <#if commResult.timeType?exists && commResult.timeType!='1' >class="hide"</#if> id="fixedTime">
					<div class="clearfix">
					<p class="date">
						<span><label><@s.text name="ctpln.sendDate"/></label>
						<@s.textfield name="sendDate" id="sendDate_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.sendDate)!}" ></@s.textfield></span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.sendTime"/></label>
						<input name="sendTime" id="sendTime_${(commResult.communicationCode)!?html}" type="text" class="text" value="${(commResult.sendTime)!}"/>
						</span>
					</p>
					</div>
				</div>
				
				<#-- 当沟通时间选择参考某个时间点时需要显示的页面内容 -->
				<div <#if (commResult.timeType?exists && commResult.timeType!='2') || !commResult.timeType?exists >class="hide"</#if> id="referTime">
					<div class="clearfix">
					<p>
						<#if (campaignCode?exists && campaignCode!='')>
							<@s.select name="referTypeCode" cssStyle="width:180px" list='#application.CodeTable.getCodes("1216")' listKey="CodeKey" listValue="Value" value="${(commResult.referTypeCode)!}" onchange="BINOLCTCOM02.referTypeChange(this);return false;" />
						<#else>
							<@s.select name="referTypeCode" cssStyle="width:180px" list='#application.CodeTable.getCodes("1216","6","20")' listKey="CodeKey" listValue="Value" value="${(commResult.referTypeCode)!}" onchange="BINOLCTCOM02.referTypeChange(this);return false;" />
						</#if>
					</p>
					</div>
					<div id="beginDateDiv" class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.indate"/></label>
						<@s.textfield name="runBeginTime" id="runBeginTime_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.runBeginTime)!}"></@s.textfield>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					<div id="endDateDiv" class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.todate"/></label>
						<@s.textfield name="runEndTime" id="runEndTime_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.runEndTime)!}"></@s.textfield>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span>
						<select id="referAttribute" name="referAttribute">
							<#if (commResult.referAttribute?exists && commResult.referAttribute=='2') >
								<option id="referafter" <#if commResult.referTypeCode?exists && commResult.referTypeCode=='11'>class="hide"</#if> value="2" selected="selected"><@s.text name="ctpln.after"/></option>
							<#else>
								<option id="referafter" <#if commResult.referTypeCode?exists && commResult.referTypeCode=='11'>class="hide"</#if> value="2"><@s.text name="ctpln.after"/></option>
							</#if>
							<#if (commResult.referAttribute?exists && commResult.referAttribute=='1') >
								<option id="referbef" <#if (commResult.referTypeCode?exists && (commResult.referTypeCode=='5' || commResult.referTypeCode=='6' || commResult.referTypeCode=='7' || commResult.referTypeCode=='9' || commResult.referTypeCode=='10'))>class="hide"</#if> value="1" selected="selected"><@s.text name="ctpln.bef"/></option>
							<#else>
								<option id="referbef" <#if (commResult.referTypeCode?exists && (commResult.referTypeCode=='5' || commResult.referTypeCode=='6' || commResult.referTypeCode=='7' || commResult.referTypeCode=='9' || commResult.referTypeCode=='10'))>class="hide"</#if> value="1"><@s.text name="ctpln.bef"/></option>
							</#if>
						</select>
						<input name="dateValue" id="dateValue" style="width:90px" type="text" class="text" value="${(commResult.dateValue)!}"/>
						<label><@s.text name="ctpln.date"/></label>
						<label id="dateBegin" <#if (commResult.referTypeCode?exists && commResult.referTypeCode!='11') || !commResult.referTypeCode?exists >class="hide"</#if>><@s.text name="ctpln.begin"/></label>
						</span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.at"/></label>
						<input name="timeValue" id="timeValue_${(commResult.communicationCode)!?html}" type="text" class="text" value="${(commResult.timeValue)!}"/>
						<label><@s.text name="ctpln.send"/></label></span>
					</p>
					</div>
				</div>
				<@s.text name='cttpl.at' id="attat"/>
				<#-- 当沟通时间选择循环执行时需要显示的页面内容 -->
				<div <#if (commResult.timeType?exists && commResult.timeType!='3') || !commResult.timeType?exists >class="hide"</#if> id="loopRun">
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.indate"/></label>
						<@s.textfield name="sendBeginTime" id="sendBeginTime_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.sendBeginTime)!}"></@s.textfield>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.todate"/></label>
						<@s.textfield name="sendEndTime" id="sendEndTime_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.sendEndTime)!}"></@s.textfield>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span>
						<@s.select name="frequencyCode" list='#application.CodeTable.getCodes("1217")' listKey="CodeKey" listValue="Value" value="${(commResult.frequencyCode)!}" onchange="BINOLCTCOM02.frequencyChange(this);return false;"/>
						<select id="forAttribute" name="forAttribute" <#if (commResult.frequencyCode?exists && commResult.frequencyCode=='1')>class="hide"</#if> onchange="BINOLCTCOM02.forAttributeChange(this);return false;">
							<#if (commResult.forAttribute?exists && commResult.forAttribute=='A') >
								<option value="A" selected="selected"><@s.text name="ctpln.at"/></option>
							<#else>
								<option value="A"><@s.text name="ctpln.at"/></option>
							</#if>
							<#if (commResult.forAttribute?exists && commResult.forAttribute=='E') >
								<option value="E" selected="selected"><@s.text name="ctpln.notexists"/></option>
							<#else>
								<option value="E"><@s.text name="ctpln.notexists"/></option>
							</#if>
							<#if (commResult.forAttribute?exists && commResult.forAttribute=='B') >
								<option value="B" selected="selected"><@s.text name="ctpln.before"/></option>
							<#else>
								<option value="B"><@s.text name="ctpln.before"/></option>
							</#if>
							<#if (commResult.forAttribute?exists && commResult.forAttribute=='L') >
								<option value="L" selected="selected"><@s.text name="ctpln.last"/></option>
							<#else>
								<option value="L"><@s.text name="ctpln.last"/></option>
							</#if>
							<#if (commResult.forAttribute?exists && commResult.forAttribute=='EB') >
								<option value="EB" selected="selected"><@s.text name="ctpln.notbefore"/></option>
							<#else>
								<option value="EB"><@s.text name="ctpln.notbefore"/></option>
							</#if>
							<#if (commResult.forAttribute?exists && commResult.forAttribute=='EL') >
								<option value="EL" selected="selected"><@s.text name="ctpln.notend"/></option>
							<#else>
								<option value="EL"><@s.text name="ctpln.notend"/></option>
							</#if>
						</select>
						</span>
					</p>
					</div>
					<div id="dayview" <#if (commResult.frequencyCode?exists && commResult.frequencyCode=='1')>class="hide"<#else>class="clearfix"</#if>>
					<p>
						<span><input name="dayValue" id="dayValue" type="text" class="text" value="${(commResult.dayValue)!}"/>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					
					<div class="clearfix">
					<p>
						<span><input name="tValue" id="tValue_${(commResult.communicationCode)!?html}" type="text" class="text" value="${(commResult.tValue)!}"/>
						<label><@s.text name="ctpln.send"/></label></span>
					</p>
					</div>
				</div>
				
				<#-- 当沟通时间选择条件触发时需要显示的页面内容 -->
				<div <#if (commResult.timeType?exists && commResult.timeType!='4') || !commResult.timeType?exists >class="hide"</#if> id="conditionTrigger">
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.indate"/></label>
						<@s.textfield name="runBegin" id="runBegin_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.runBegin)!}"></@s.textfield>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.todate"/></label>
						<@s.textfield name="runEnd" id="runEnd_${(commResult.communicationCode)!?html}" cssClass="text" value="${(commResult.runEnd)!}"></@s.textfield>
						<label><@s.text name="ctpln.day"/></label></span>
					</p>
					</div>
					<div id="paramview" <#if (commResult.conditionCode?exists && commResult.conditionCode!='1') && (commResult.conditionCode?exists && commResult.conditionCode!='4')>class="clearfix"<#else>class="hide"</#if>>
					<p>
						<span><label><@s.text name="ctpln.fill"/></label>
						<label><@s.text name="ctpln.at"/></label>
						<input name="paramValue" id="paramValue" style="width:90px" type="text" class="text" value="${(commResult.paramValue)!}"/>
						<select id="paramAttribute" name="paramAttribute">
							<#if (commResult.paramAttribute?exists && commResult.paramAttribute=='1') >
								<option value="1" selected="selected"><@s.text name="ctpln.date"/></option>
							<#else>
								<option value="1"><@s.text name="ctpln.date"/></option>
							</#if>
							<#if (commResult.paramAttribute?exists && commResult.paramAttribute=='2') >
								<option value="2" selected="selected"><@s.text name="ctpln.month"/></option>
							<#else>
								<option value="2"><@s.text name="ctpln.month"/></option>
							</#if>
							<#if (commResult.paramAttribute?exists && commResult.paramAttribute=='3') >
								<option value="3" selected="selected"><@s.text name="ctpln.year"/></option>
							<#else>
								<option value="3"><@s.text name="ctpln.year"/></option>
							</#if>
						</select>
						<label><@s.text name="ctpln.within"/></label>
						</span>
					</p>
					</div>
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.when"/></label>
						<#if (campaignCode?exists && campaignCode!='')>
							<@s.select name="conditionCode" cssStyle="width:180px" list='#application.CodeTable.getCodes("1218")' listKey="CodeKey" listValue="Value" value="${(commResult.conditionCode)!}"  onchange="BINOLCTCOM02.conditionChange(this);return false;"/>
						<#else>
							<@s.select name="conditionCode" cssStyle="width:180px" list='#application.CodeTable.getCodes("1218","0","2")' listKey="CodeKey" listValue="Value" value="${(commResult.conditionCode)!}"  onchange="BINOLCTCOM02.conditionChange(this);return false;"/>
						</#if>
						<input name="conditionValue" id="conditionValue" style="width:90px" type="text" class="text" value="${(commResult.conditionValue)!}"/>
						<label><@s.text name="ctpln.ontime"/></label>
						<label><@s.text name="ctpln.send"/></label></span>
					</p>
					</div>
				</div>
				
				<#-- 当沟通时间选择事件触发时需要显示的页面内容 -->
				<div <#if (commResult.timeType?exists && commResult.timeType!='5') || !commResult.timeType?exists >class="hide"</#if> id="eventTrigger">
					<div class="clearfix">
					<p>
						<span><label><@s.text name="ctpln.event"/></label>
						<@s.select name="eventCode" list='#application.CodeTable.getCodes("1219")' listKey="CodeKey" listValue="Value" value="${(commResult.eventCode)!}" />
						<label><@s.text name="ctpln.ontime"/></label>
						<label><@s.text name="ctpln.send"/></label></span>
					</p>
					</div>
				</div>
			</td>
		</tr>
		<tr>
			<th>
				<@s.text name="ctpln.runType"/>
			</th>
			<td>
				<input id="runType" name="runType" class="checkbox" type="checkbox" <#if (commResult.runType?? && commResult.runType == "1")>value="${(commResult.runType)!?html}" checked="true"<#else>value="1"</#if> />
				<@s.text name="ctpln.runTypeExplanation"/>
			</td>
		</tr>
	</tbody>
	</table>
</div>
</#macro>

<#-- 沟通页面整体的宏定义 -->
<#macro getHideCommListBox commInfoList=[]>
	<@s.url id="setMessageInitUrl" action="BINOLCTCOM04_init" namespace="/ct"/>
	<@s.url id="setCommObjInitUrl" action="BINOLCM33_init" namespace="/common"/>
	<#if (commInfoList?? && commInfoList?size>0)>
		<#list commInfoList as commResult >
		<div class="box2-active COMMINFO" id="commBox_${(commResult.communicationCode)!?html}">
			<div class="box2 box2-content ui-widget" id="commObjectList">
				<#-- 引用沟通基本信息模板的宏定义 -->
		        <@getHideCommBox commResult />
		        <#-- 引用沟通对象模板的宏定义 -->
		        <@getCommObjBox objList=commResult.objList />
			</div>
		</div>
		</#list>
	</#if>
</#macro>

<#-- 沟通名称列表的宏定义 -->
<#macro getCommNameLi commNameList=[]>
	<#if (commNameList?? && commNameList?size>0)>
		<#list commNameList as commInfo >
			<li class="on MENUTEXT" onclick="BINOLCTCOM02.selectCommbox(this);return false;">
				<span>${(commInfo.communicationName)!?html}</span>
				<#-- 隐藏的沟通名称菜单标识值，用于点击菜单时判断需要显示的页面模块内容 -->
				<input name="commBoxId" id="commBoxId" type="text" class="text hide" value="${(commInfo.communicationCode)!?html}"/>
			</li>
		</#list>
	</#if>
</#macro>

<#-- 沟通对象搜索结果页面宏定义 -->
<#macro getSearchRecord>
	<div id="searchRecordTabs" class="hide">
		<div class="ui-tabs">
			<ul class="ui-tabs-nav clearfix">
				<li><a href="#tabs-1"><@s.text name="ctpln.searchCondition"/></a></li>
				<li><a href="#tabs-2"><@s.text name="ctpln.searchResult"/></a></li>
				<li><a href="#tabs-3"><@s.text name="cttpl.excelImport"/></a></li>
			</ul>
			<div calss="ui-tabs-panel" id="tabs-1">
			</div>
			<div calss="ui-tabs-panel" id="tabs-2">
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
							<button class="right search"  onclick="BINOLCTCOM02.searchSearchReCord();return false;">
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
			</div>
			<div calss="ui-tabs-panel" id="tabs-3">
			</div>
		</div>
	</div>
</#macro>


