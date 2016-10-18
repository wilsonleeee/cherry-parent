<#include "/WEB-INF/ftl/common/head.inc.ftl">
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<@s.url id="nextAction" action="BINOLCTCOM02_init" />
<@s.url id="changeActAction" action="BINOLCTCOM01_changeActivity" />
<#-- 引用国际化文件 -->
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<div class="main container clearfix" id="div_main">
	<div class="panel ui-corner-all">
		<div class="panel-header">
			<div class="clearfix">
				<span class="breadcrumb left"> 
					<span class="ui-icon icon-breadcrumb"></span>
        			<a href="#"><@s.text name="ctpln.submodule" /></a> > 
        			<a href="#"><@s.text name="ctpln.addplaninfo" /></a>
        		</span>
			</div>
		</div>
		<div id="actionResultDisplay"></div>
		<div class="panel-content">
			<ol id="ol_steps" class="steps clearfix">
				<li style="width:33.3%" class="on"><span><@s.text name="ctpln.planInfo" /></span></li>
				<li style="width:33.3%" class=""><span><@s.text name="ctpln.communicationSet" /></span></li>
				<li style="width:33.3%" class="last"><span><@s.text name="ctpln.preview" /></span></li>
			</ol>
			<div class="box2-active">
				<div class="box2 box2-content ui-widget">
				<form id="baseInfoForm" method="post" class="inline" >
				<input name="userId" class="hide" id="userId" value="${(userId)!?html}"/>
				<input name="privilegeFlag" class="hide" id="privilegeFlag" value="${(privilegeFlag)!?html}"/>
				<input name="campaignID" class="hide" id="campaignID" value="${(campaignID)!?html}"/>
				<input name="showChannel" class="hide" id="showChannel" value="${(showChannel)!?html}"/>
				<input name="commResultInfo" class="hide" id="commResultInfo" value="${(commResultInfo)!?html}"/>
				<p>
					<@s.text name='ctpln.selectvalue' id="selectvalue"/>
					<#-- 如果接收到的活动编号为空，则活动选择框为可用；如果活动编号不为空则禁用活动选择框 -->
					<#if (campaignCode?? && campaignCode != "") >
						<label>
							<strong>
								<@s.text name="ctpln.campaignName"/> : 
								<span style="color:blue">
									${(campaignName)!?html}
								</span>
							</strong>
							<#if (beginDate?? && beginDate != "") && (endDate?? && endDate != "") >
								<span style="color:blue">( ${(beginDate)!?html} <@s.text name="ctpln.todate"/> ${(endDate)!?html} )</span>
							</#if>
						</label>
						<input name="campaignCode" id="campaignCode" class="hide" value="${(campaignCode)!?html}"/>
						<input name="campaignName" id="campaignName" class="text hide" disabled="disabled" value="${(campaignName)!?html}"/></br>
						<span <#if (commResultInfo?? && commResultInfo != "")>class="hide"</#if> >
							<strong>
								<@s.text name="ctpln.conditionExplanation"/>
							</strong>
							<input id="conditionUseFlag" name="conditionUseFlag" class="checkbox" type="checkbox" onchange="BINOLCTCOM01.conditionUseChange();return false;" <#if (conditionUseFlag?? && conditionUseFlag == "1")>value="${(conditionUseFlag)!?html}" checked="checked"<#else>value="1"</#if> />
						</span>
					<#else>
						<label>
							<strong>
								<@s.text name="ctpln.campaignName"/> : 
								<span style="color:blue">
									<@s.text name="ctpln.noCampaign"/>
								</span>
							</strong>
						</label>
					</#if>
				</p>
				<p>
					<table class="detail" cellpadding="0" cellspacing="0">
					<tbody>
						<tr>
							<th>
								<label><@s.text name="ctpln.planName"/><span class="highlight">*</span></label>
							</th>
							<td>
								<input name="planCode" class="hide" id="planCode" value="${(planCode)!?html}"/>
								<span><input name="planName" class="text" type="text" value="${(planName)!}"/></span>
							</td>
							<th>
								<label><@s.text name="ctpln.brandName"/><span class="highlight">*</span></label>
							</th>
							<td>
								<#-- 当权限为管辖多个品牌的情况时显示品牌选择框 -->
								<#if (brandList?? && brandList?size > 0) >
				         			<@s.select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" value="" />
				         		<#else>
				         			<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
				         			<input name="brandName" class="text disabled" disabled="disabled" value="${(brandName)!}" type="text" />
				        		</#if>
							</td>
						</tr>
						<#if (showChannel?? && showChannel == "1") >
						<tr>
							<th>
								<label><@s.text name="ctpln.channel"/></label>
							</th>
							<td>
								<span><@s.select id="channelId" name="channelId" list="channelList" listKey="channelId" listValue="channelName" headerKey="" headerValue="" onchange="BINOLCTCOM01.changeChannel();"/></span>
							</td>
							<th>
								<label><@s.text name="ctpln.counter"/></label>
							</th>
							<td>
								<input id="counterCode" name="counterCode" type="hidden" value="${(counterCode)!}" />
								<span><input id="counterName" name="counterName" type="text" maxlength="20" <#if channelId?? && channelId != "">class="text"<#else>class="text disabled" disabled="disabled"</#if> value="${(counterName)!}" /></span>
							</td>
						</tr>
						</#if>
						<tr>
							<th>
								<label><@s.text name="ctpln.memo"/></label>
							</th>
							<td colspan="3">
								<span style="width: 80%;"><textarea cols="" rows="" name="memo" style="width: 100%;" >${(memo)!}</textarea></span>
							</td>
						</tr>
					</tbody>
					</table>
				</p>
				</form>
				</div>
			</div>
			<#-- 翻页按钮 -->
			<div class="center clearfix">
				<button onclick="window.close();return false;" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctpln.cancel" /></span>
				</button>
				<button onclick="BINOLCTCOM01.toNext('${(nextAction)!}');return false;" class="save" type="button">
					<span class="ui-icon icon-mover"></span>
					<span class="button-text"><@s.text name="ctpln.next" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
<div class="dialog2 clearfix" style="display:none" id="conditionCancel_dialog">
	<p class="message" style="margin:10% auto 0;"><span><@s.text name="ctpln.conditionCancelMsg"/></span></p>
</div>
<div id="dialogTitle" class="hide"><@s.text name="ctpln.cancelCondition"/></div>
<div id="dialogConfirm" class="hide"><@s.text name="ctpln.ok"/></div>
</@s.i18n>
