<#include "/WEB-INF/ftl/common/head.inc.ftl">
<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<#-- 引用国际化文件 -->
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<#-- 引用宏 -->
<#include "/WEB-INF/ftl/ct/common/BINOLCTCOM02_mac.ftl">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<@s.url id="prevAction" action="BINOLCTCOM01_init" />
<@s.url id="nextAction" action="BINOLCTCOM03_init" />
<@s.url id="testMsgAction" action="BINOLCTCOM05_init" />
<@s.url id="conditionDisplayAction" action="BINOLCM33_conditionDisplay" namespace='/common'/>
<@s.url id="searchSearchReCord_url" action="BINOLCTCOM02_searchSearchReCord" />
<@s.url id="getCustomerCount_url" action="BINOLCTCOM02_getCustomerCount" />
<@s.url id="getSendType_url" action="BINOLCTCOM02_getSendType" />
<a id="getSendTypeUrl" href="${getSendType_url}"></a>
<a id="searchSearchReCordUrl" href="${searchSearchReCord_url}"></a>
<a id="getCustomerCountUrl" href="${getCustomerCount_url}"></a>
<div class="panel ui-corner-all">
	<div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left"> 
				<span class="ui-icon icon-breadcrumb"></span>
        		<a href="#"><@s.text name="ctpln.submodule" /></a> > 
        		<a href="#"><@s.text name="ctpln.communicationSet" /></a>
        	</span>
		</div>
	</div>
	<div id="actionResultDisplay"></div>
	<div class="panel-content COMMBOX">
		<ol id="ol_steps" class="steps clearfix">
			<li style="width:33.3%" class="prev"><span><@s.text name="ctpln.planInfo" /></span></li>
			<li style="width:33.3%" class="on"><span><@s.text name="ctpln.communicationSet" /></span></li>
			<li style="width:33.3%" class="last"><span><@s.text name="ctpln.preview" /></span></li>
		</ol>
		<#if (campaignCode?? && campaignCode != "") >
		<div class="clearfix">
			<span class="left">
				<strong>
					<@s.text name="ctpln.campaignName" /> : 
					<span style="color:blue">
						${(campaignName)!?html}
					</span>
				</strong>
				<#if (beginDate?? && beginDate != "") && (endDate?? && endDate != "") >
					<span style="color:blue">( ${(beginDate)!?html} <@s.text name="ctpln.todate"/> ${(endDate)!?html} )</span>
				</#if>
			</span>
		</div>
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
		<div class="section-header" style="padding:0px">
			<span class="left" style="margin-right:50px;">
				<strong>
					<@s.text name="ctpln.planName" /> : 
					<span style="color:blue">
						${(planName)!?html}
					</span>
				</strong>
			</span>
			<#if (showChannel?? && showChannel == "1") >
				<#if (channelId?? && channelId != "") >
				<span class="left" style="margin-right:50px;">
					<strong>
						<@s.text name="ctpln.channelName" /> : 
						<span style="color:blue">
							${(channelName)!?html}
						</span>
					</strong>
				</span>
				</#if>
				<#if (counterCode?? && counterCode != "") >
				<span class="left" style="margin-right:50px;">
					<strong>
						<@s.text name="ctpln.counterName" /> : 
						<span style="color:blue">
							${(counterName)!?html}
						</span>
					</strong>
				</span>
				</#if>
			</#if>
			<#-- 增加沟通按钮 -->
			<span class="right">
				<a class="add" onclick="BINOLCTCOM02.addNewComm(this);return false;">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="ctpln.addcommunication" /></span>
				</a>
			</span>
			<#-- 沟通删除按钮 -->
			<span class="right hide" id="commDeleteButton">
				<a class="delete" onclick="BINOLCTCOM02.deleteComm(this);return false;">
					<span class="ui-icon icon-delete"></span>
					<span class="button-text"><@s.text name="ctpln.delcommunication" /></span>
				</a>
			</span>
		</div>
		
		<form id="infoSetForm" method="post" class="inline" >
		<#-- 保存上一页面action提交的值 -->
		<input name="brandInfoId" class="hide" id="brandInfoId" value="${(brandInfoId)!?html}"/>
		<input name="showChannel" class="hide" id="showChannel" value="${(showChannel)!?html}"/>
		<input name="campaignID" class="hide" id="campaignID" value="${(campaignID)!?html}"/>
		<input name="campaignCode" class="hide" id="campaignCode" value="${(campaignCode)!?html}"/>
		<input name="campaignName" class="hide" id="campaignName" value="${(campaignName)!?html}"/>
		<input name="userId" class="hide" id="userId" value="${(userId)!?html}"/>
		<input name="privilegeFlag" class="hide" id="privilegeFlag" value="${(privilegeFlag)!?html}"/>
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
		<@s.text name='ctpln.timeOnlyTitle' id="timeOnlyTitle"/>
		<@s.text name='ctpln.currentText' id="currentText"/>
		<@s.text name='ctpln.closeText' id="closeText"/>
		<input name="timeOnlyTitle" class="hide" id="timeOnlyTitle" value="${(timeOnlyTitle)!?html}" />
		<input name="currentText" class="hide" id="currentText" value="${(currentText)!?html}"/>
		<input name="closeText" class="hide" id="closeText" value="${(closeText)!?html}"/>
		<input name="timeText" class="hide" id="timeText" value=""/>
		<input name="hourText" class="hide" id="hourText" value=""/>
		<input name="minuteText" class="hide" id="minuteText" value=""/>
		
		<input name="nowDate" class="hide" id="nowDate" value="${(nowDate)!?html}"/>
		<div class="section-content">
			<div class="clearfix">
				<div class="sidemenu2 left" style="width:20%;">
					<div class="sidemenu2-header">
						<strong><@s.text name="ctpln.communicationList" /></strong>
					</div>
				    <ul class="u1" style="width:100%;" id="commNameList">
				    	<#-- 引用沟通名称列表的宏定义 -->
				    	<@getCommNameLi commNameList=commResultList/>
					</ul>
				</div>
				<div class="right" style="width:78%;" id="commList">
					<#-- 引用沟通页面整体的宏定义 -->
					<@getHideCommListBox commInfoList=commResultList/>
				</div>
			</div>
		</div>
		<#-- 弹出页面 -->
		<div class="hide" id="dialogInit"></div>
		<div class="hide" id="resDialogInit"></div>
		<div class="hide" id="resDialogTitle"><@s.text name="ctpln.resDialogTitle" /></div>
		<div class="hide" id="objDialogTitle"><@s.text name="ctpln.objDialogTitle" /></div>
		<div class="hide" id="msgDialogTitle"><@s.text name="ctpln.msgDialogTitle" /></div>
		<div class="hide" id="dialogConfirm"><@s.text name="ctpln.dialogConfirm" /></div>
		<div class="hide" id="dialogCancel"><@s.text name="ctpln.dialogCancel" /></div>
		<div class="hide" id="conditionDisplayUrl">${(conditionDisplayAction)!}</div>
		<#-- 翻页按钮 -->
		<div class="clearfix"></p>
			<div class="center">
				<button onclick="BINOLCTCOM02.toNext('${(prevAction)!}');return false;" class="back" type="button">
					<span class="ui-icon icon-movel"></span>
					<span class="button-text"><@s.text name="ctpln.back" /></span>
				</button>
				<button onclick="BINOLCTCOM02.toNext('${(nextAction)!}');return false;" class="save" type="button">
					<span class="ui-icon icon-mover"></span>
					<span class="button-text"><@s.text name="ctpln.next" /></span>
				</button>
			</div>
		</div>
		
	</div>
</div>
<div id="addObjName" class="hide">
	<div class="box2-active">
		<div class="box2 box2-content ui-widget">
			<label><strong><@s.text name="ctpln.objName"/><span class="highlight">*</span></strong></label>
			<span><input id="searchName" name="searchName" class="text" type="text" maxlength="45" style="width:40%;"/></span>
			
		</div>
	</div>
</div>

<#-- 在点击增加沟通按钮时增加的列表内容  -->
<div id="addNewLi" class="hide">
	<#-- 引用沟通名称列表的宏定义 -->
	<@getCommNameLi commNameList=commInitList/>
</div>

<#-- 在点击增加沟通按钮时增加的页面内容  -->
<div id="addNewComm" class="hide">
	<#-- 引用沟通页面整体的宏定义 -->
	<@getHideCommListBox commInfoList=commInitList/>
</div>

<#-- 在点击增加沟通对象按钮时增加的页面内容  -->
<div id="addNewCommObjInfo" class="hide">
	<#-- 引用沟通对象模板的宏定义 -->
	<@getCommObjInfoBox objResult={} objDelFlag='N'/>
</div>

<#-- 引用隐藏的沟通信息内容的宏定义，在点击增加沟通信息按钮时增加的页面内容  -->
<@getHideMsgInfo />

<#-- 引用沟通对象搜索结果页面内容 -->
<@getSearchRecord />

<#-- 沟通对象Excel导入-->
<div class="hide">
	<span id="excelImport"><@s.text name="cttpl.excelImport" /></span>
	<span id="importNameNull"><@s.text name="cttpl.importNameNull" /></span>
	<span id="importCodeNull"><@s.text name="cttpl.importCodeNull" /></span>
</div>

<#-- 沟通对象明细弹出窗 -->
<div class="hide" id="customerDialogInit"></div>
<div class="hide" id="customerDialogTitle"><@s.text name="ctpln.showCustomerDialogTitle" /></div>
<div class="hide" id="searchResult"><@s.text name="ctpln.searchResult" /></div>
<div id="importDetailViewBtn" class="hide">
	<hr class="space">
	<a class="add right" href="BINOLCTCOM06_init.action" onclick="BINOLCTCOM02.showCustomerInit(this,2);return false;">
		<span class="ui-icon icon-search"></span>
		<span class="button-text">
			<@s.text name="cttpl.importDetailView" />
		</span>
	</a>
</div>
</@s.i18n>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
