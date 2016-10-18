<#include "/WEB-INF/ftl/common/head.inc.ftl">
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM03.js"></script>
<@s.url id="prevAction" action="BINOLCTCOM02_init" />
<@s.url id="saveAction" action="BINOLCTCOM03_save" />
<@s.url id="showCustomerUrl" action="BINOLCTCOM06_init" />
<div class="panel ui-corner-all">
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left"> 
				<span class="ui-icon icon-breadcrumb"></span>
				<#if showType?exists && showType == 'view'>
	        		<@s.text name="ctpln.showplan" />
        		<#else>
        			<a href="#"><@s.text name="ctpln.submodule" /></a> > 
	        		<a href="#"><@s.text name="ctpln.preview" /></a>
        		</#if>
        	</span>
		</div>
	</div>
	<div class="panel-content">
		<input name="userId" class="hide" id="userId" value="${(userId)!?html}"/>
		<input name="privilegeFlag" class="hide" id="privilegeFlag" value="${(privilegeFlag)!?html}"/>
		<input name="overSetNumber" class="hide" id="overSetNumber" value="${(overSetNumber)!?html}"/>
		<#if showType?exists && showType == 'view'>
			<div class="main container clearfix" id="div_main">
				<input name="planCode" class="hide" id="planCode" value="${(planCode)!?html}"/>
			</div>
		<#else>
			<ol id="ol_steps" class="steps clearfix">
				<li style="width:33.3%" class=""><span><@s.text name="ctpln.planInfo" /></span></li>
				<li style="width:33.3%" class="prev"><span><@s.text name="ctpln.communicationSet" /></span></li>
				<li style="width:33.3%" class="on last"><span><@s.text name="ctpln.preview" /></span></li>
			</ol>
			<form id="setDetailForm" method="post" class="inline">
				<input name="brandInfoId" class="hide" id="brandInfoId" value="${(brandInfoId)!?html}"/>
				<input name="showChannel" class="hide" id="showChannel" value="${(showChannel)!?html}"/>
				<input name="campaignCode" class="hide" id="campaignCode" value="${(campaignCode)!?html}"/>
				<input name="campaignName" class="hide" id="campaignName" value="${(campaignName)!?html}"/>
				<input name="campaignID" class="hide" id="campaignID" value="${(campaignID)!?html}"/>
				<input name="planCode" class="hide" id="planCode" value="${(planCode)!?html}"/>
				<input name="planName" class="hide" id="planName" value="${(planName)!?html}"/>
				<input name="channelId" class="hide" id="channelId" value="${(channelId)!?html}"/>
				<input name="channelName" class="hide" id="channelName" value="${(channelName)!?html}"/>
				<input name="counterCode" class="hide" id="counterCode" value="${(counterCode)!?html}"/>
				<input name="counterName" class="hide" id="counterName" value="${(counterName)!?html}"/>
				<input name="memo" class="hide" id="memo" value="${(memo)!?html}"/>
				<input name="conditionUseFlag" class="hide" id="conditionUseFlag" value="${(conditionUseFlag)!?html}"/>
				<input name="commResultInfo" class="hide" id="commResultInfo" value="${(commResultInfo)!?html}"/>
			</form>
		</#if>
		<#-- 弹出页面 -->
		<div class="hide" id="customerDialogInit"></div>
		<div class="hide" id="customerDialogTitle"><@s.text name="ctpln.showCustomerDialogTitle" /></div>
		<div class="clearfix" id="commSetInfoDiv">
			<div class="box4">
				<div class="box4-header">
					<strong><@s.text name="ctpln.planInfo" /></strong>
				</div>
 				<div class="box4-content">
					<table class="detail" border="0" cellpadding="0" cellspacing="0">
						<tr>
					    	<th><@s.text name="ctpln.planName" /></th>
							<td>
								<input name="planName" value="${(planName)!?html}" class="text disabled" disabled="disabled" type="text" />
								<input name="planName" value="${(planName)!?html}" class="hide" type="text" />
							</td>
					        <th><@s.text name="ctpln.campaignName" /></th>
					        <td>
					        	<input name="campaignName" value="${(campaignName)!?html}" class="text disabled" disabled="disabled" type="text" />
					        </td>
					    </tr>
					    <#if (showChannel?? && showChannel == "1") >
					    <tr>
					    	<th><@s.text name="ctpln.channelName" /></th>
							<td>
								<#if (channelId?? && channelId != "") >
									<input name="channelName" value="${(channelName)!?html}" class="text disabled" disabled="disabled" type="text" />
								<#else>
					        		<input name="channelName" value="" class="text disabled" disabled="disabled" type="text" />
					        	</#if>
							</td>
					        <th><@s.text name="ctpln.counterName" /></th>
					        <td>
					        	<#if (counterCode?? && counterCode != "") >
					        		<input name="counterName" value="${(counterName)!?html}" class="text disabled" disabled="disabled" type="text" />
					        	<#else>
					        		<input name="counterName" value="" class="text disabled" disabled="disabled" type="text" />
					        	</#if>
					        </td>
					    </tr>
					    </#if>
					    <tr>
					    	<th><@s.text name="ctpln.createTime" /></th>
					        <td><input name="createTime" value="${(createTime)!?html}" class="text disabled" disabled="disabled" type="text" /></td>
					        <th><@s.text name="ctpln.brandName" /></th>
					        <td><input name="brandName" value="${(brandName)!?html}" class="text disabled" disabled="disabled" type="text" /></td>
					    </tr>
					    <tr>
					    	<th><@s.text name="ctpln.memo1" /></th>
					        <td colspan = "3"><textarea cols="" rows="" disabled="disabled" name="memo" style="width: 80%;" >${(memo)!}</textarea></td>
					    </tr>
					</table>
				</div>
			</div>
			
			<#if (commResultList?? && commResultList?size>0)>
				<#list commResultList as commResult >
				<div class="box4">
					<div class="box4-header">
						<strong>${(commResult.communicationName)!?html}</strong>
						<#if commResult.runType?exists && '1' == commResult.runType.toString() >
							<span style="color:red">(<@s.text name="ctpln.runType"/>)</span>
						</#if>
					</div>
	 				<div class="box4-content">
						<p>
							<@s.text name="ctpln.communicationTime" />:
							<#if '1' == commResult.timeType.toString() >
								<input id="commRunTime" name="commRunTime" value="${(commResult.sendDate)!?html} ${(commResult.sendTime)!?html}" class="hide" type="text" />
								${(commResult.sendDate)!?html}
								${(commResult.sendTime)!?html}
							<#elseif '2' == commResult.timeType.toString() >
								${application.CodeTable.getVal("1216","${(commResult.referTypeCode)!?html}")}
								<#if '1' == commResult.referAttribute.toString() >
									<@s.text name="ctpln.bef"/>
								<#else>
									<@s.text name="ctpln.after"/>
								</#if>
								${(commResult.dateValue)!?html}
								<@s.text name="ctpln.date"/>
								<@s.text name="ctpln.at"/>
								${(commResult.timeValue)!?html}
								<@s.text name="ctpln.send"/> . 
								<@s.text name="ctpln.validDate"/>
								<#if !commResult.runBeginTime?exists && !commResult.runEndTime?exists >
									<@s.text name="ctpln.unlimited"/>
								<#else>
									<#if commResult.runBeginTime?exists && commResult.runEndTime?exists>
										${(commResult.runBeginTime)!?html}
										<@s.text name="ctpln.todate"/>
										${(commResult.runEndTime)!?html}
									<#elseif commResult.runBeginTime?exists>
										${(commResult.runBeginTime)!?html}
										<@s.text name="ctpln.begin"/>
									<#elseif commResult.runEndTime?exists>
										<@s.text name="ctpln.todate"/>
										${(commResult.runEndTime)!?html}
									</#if>
								</#if>
							<#elseif '3' == commResult.timeType.toString() >
								${application.CodeTable.getVal("1217","${(commResult.frequencyCode)!?html}")}
								<#if '1' != commResult.frequencyCode.toString() >
									<#if 'A' == commResult.forAttribute.toString() >
										${(commResult.dayValue)!?html}
										<@s.text name="ctpln.day"/>
									<#elseif 'E' == commResult.forAttribute.toString()>
										<@s.text name="ctpln.notexists"/>
										${(commResult.dayValue)!?html}
										<@s.text name="ctpln.day"/>
									<#elseif 'B' == commResult.forAttribute.toString()>
										<@s.text name="ctpln.before"/>
										${(commResult.dayValue)!?html}
										<@s.text name="ctpln.date"/>
									<#elseif 'L' == commResult.forAttribute.toString()>
										<@s.text name="ctpln.last"/>
										${(commResult.dayValue)!?html}
										<@s.text name="ctpln.date"/>
									<#elseif 'EB' == commResult.forAttribute.toString()>
										<@s.text name="ctpln.notbefore"/>
										${(commResult.dayValue)!?html}
										<@s.text name="ctpln.date"/>
									<#elseif 'EL' == commResult.forAttribute.toString()>
										<@s.text name="ctpln.notend"/>
										${(commResult.dayValue)!?html}
										<@s.text name="ctpln.date"/>
									</#if>									
								</#if>
								<@s.text name="ctpln.at"/>
								${(commResult.tValue)!?html}
								<@s.text name="ctpln.send"/> . 
								<@s.text name="ctpln.validDate"/>
								<#if !commResult.sendBeginTime?exists && !commResult.sendEndTime?exists >
									<@s.text name="ctpln.unlimited"/>
								<#else>
									<#if commResult.sendBeginTime?exists && commResult.sendEndTime?exists>
										${(commResult.sendBeginTime)!?html}
										<@s.text name="ctpln.todate"/>
										${(commResult.sendEndTime)!?html}
									<#elseif commResult.sendBeginTime?exists>
										${(commResult.sendBeginTime)!?html}
										<@s.text name="ctpln.begin"/>
									<#elseif commResult.sendEndTime?exists>
										<@s.text name="ctpln.todate"/>
										${(commResult.sendEndTime)!?html}
									</#if>
								</#if>
							<#elseif '4' == commResult.timeType.toString() >
								${application.CodeTable.getVal("1215","${(commResult.timeType)!?html}")} - 
								<#if (commResult.conditionCode?exists && commResult.conditionCode=='2') || (commResult.conditionCode?exists && commResult.conditionCode=='3')>
									<#if commResult.paramValue?exists && commResult.paramValue!=''>
										<@s.text name="ctpln.at"/>
										${(commResult.paramValue)!?html}
										<#if '1' == commResult.paramAttribute.toString() >
											<@s.text name="ctpln.date"/> 
										<#elseif '2' == commResult.paramAttribute.toString()>
											<@s.text name="ctpln.month"/> 
										<#elseif '3' == commResult.paramAttribute.toString()>
											<@s.text name="ctpln.year"/> 
										</#if>
										<@s.text name="ctpln.within"/> 
									</#if>
								</#if>
								<@s.text name="ctpln.when"/>
								${application.CodeTable.getVal("1218","${(commResult.conditionCode)!?html}")}
								${(commResult.conditionValue)!?html}
								<@s.text name="ctpln.ontime"/> 
								<@s.text name="ctpln.send"/> . 
								<@s.text name="ctpln.validDate"/>
								<#if !commResult.runBegin?exists && !commResult.runEnd?exists >
									<@s.text name="ctpln.unlimited"/>
								<#else>
									<#if commResult.runBegin?exists && commResult.runEnd?exists>
										${(commResult.runBegin)!?html}
										<@s.text name="ctpln.todate"/>
										${(commResult.runEnd)!?html}
									<#elseif commResult.runBegin?exists>
										${(commResult.runBegin)!?html}
										<@s.text name="ctpln.begin"/>
									<#elseif commResult.runEnd?exists>
										<@s.text name="ctpln.todate"/>
										${(commResult.runEnd)!?html}
									</#if>
								</#if>
							<#elseif '5' == commResult.timeType.toString() >
								${application.CodeTable.getVal("1215","${(commResult.timeType)!?html}")}-
								${application.CodeTable.getVal("1219","${(commResult.eventCode)!?html}")}
								<@s.text name="ctpln.ontime"/> 
								<@s.text name="ctpln.send"/> 
							</#if>
						</p>
						<table class="detail td_white" style="width:100%;" border="0" cellpadding="0" cellspacing="0">
						<tr>
							<th style="width:35%;"><strong><@s.text name="ctpln.person" /></strong></th>
							<th style="width:8%;"><strong><@s.text name="ctpln.type" /></strong></th>
							<th style="width:8%;"><strong><@s.text name="ctpln.smsChannel" /></strong></th>
							<th><strong><@s.text name="ctpln.message" /></strong></th>
						</tr>
						<#if (commResult.objList?? && commResult.objList?size>0)>
							<#list commResult.objList as objResult >
								<#if (objResult.msgList?? && objResult.msgList?size>0)>
									<#list objResult.msgList as msgResult >
									<tr id="objInfoBox">
										<td style="width:35%;">
											<input id="recordCode" name="recordCode" value="${(objResult.recordCode)!?html}" class="hide" type="text" />
											<input id="recordType" name="recordType" value="${(objResult.recordType)!?html}" class="hide" type="text" />
											<input id="customerType" name="customerType" value="${(objResult.customerType)!?html}" class="hide" type="text" />
											<input id="recordCount" name="recordCount" value="${(objResult.recordCount)!?html}" class="hide" type="text" />
											<input id="conditionInfo" name="conditionInfo" value="${(objResult.conditionInfo)!?html}" class="hide" type="text" />
											<span>
												<span class="left">
													<a id="recordName" name="recordName" href="${showCustomerUrl}" onclick="BINOLCTCOM03.showCustomerInit(this);return false;">
														${(objResult.recordName)!?html}
													</a>
												</span>
												<#if (objResult.recordCount?? && objResult.recordCount != "") >
												<span class="green">
													<@s.text name="ctpln.countExplanation1" />
													${(objResult.recordCount)!?html}
													<@s.text name="ctpln.countExplanation2" />
												</span>
												</#if>
											</span>
											<br/>
											<span><@s.text name="ctpln.conditionTitle" /> :	${(objResult.conditionDisplay)!?html}</span>
										</td>
										<td style="width:8%;">${application.CodeTable.getVal("1203","${(msgResult.messageType)!?html}")}</td>
										<td style="width:8%;">
										<#if (msgResult.smsChannel?? && msgResult.smsChannel != "") >
											${application.CodeTable.getVal("1334","${(msgResult.smsChannel)!?html}")}
										</#if>
										</td>
										<td><span>${(msgResult.contents)!?html}</span></td>
									</tr>
									</#list>
								</#if>
							</#list>
						</#if>
						</table>
					</div>
				</div>
				</#list>
			</#if>
		</div>
		<div class="center clearfix" id="pageButton">
			<#if showType?exists && showType == 'view'>
				<button onclick="window.close();return false;" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctpln.close" /></span>
				</button>
			<#else>
				<button onclick="BINOLCTCOM03.back('${(prevAction)!}');return false;" class="back" type="button">
					<span class="ui-icon icon-movel"></span>
					<span class="button-text"><@s.text name="ctpln.back" /></span>
				</button>
				<button onclick="BINOLCTCOM03.doSave('${(saveAction)!}');return false;" class="save" type="button">
					<span class="ui-icon icon-save"></span>
					<span class="button-text"><@s.text name="ctpln.save" /></span>
				</button>
			</#if>
		</div>
	</div>
</div>
<div class="dialog2 clearfix" style="display:none" id="overSetNumber_dialog">
	<p class="clearfix">
		<p class="message">
			<span><@s.text name="ctpln.overSetNumberMessage1"/><label id="lblCustomerCount"></label><@s.text name="ctpln.overSetNumberMessage2"/></span>
		</p>
	</p>
</div>
<div id="overSetDialogTitle" class="hide"><@s.text name="ctpln.overSetNumberTitle"/></div>
<div id="overSetDialogConfirm" class="hide"><@s.text name="ctpln.ok"/></div>
<div id="overSetDialogCancel" class="hide"><@s.text name="ctpln.cancel"/></div>
</@s.i18n>