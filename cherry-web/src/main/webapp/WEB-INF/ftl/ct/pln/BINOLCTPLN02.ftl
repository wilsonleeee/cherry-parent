<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTPLN02">
<#-- 引用宏 -->
<#include "/WEB-INF/ftl/ct/pln/BINOLCTPLN02_mac.ftl">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/pln/BINOLCTPLN02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<@s.url id="testMsgAction" action="BINOLCTCOM05_init" />
<@s.url id="setCommObjInitUrl" action="BINOLCM33_init" namespace="/common"/>
<@s.url id="setMessageInitUrl" action="BINOLCTCOM04_init" namespace="/ct" />
<@s.url id="changeUrl" action="BINOLCTPLN02_change" namespace="/ct" />
<@s.url id="stopEventUrl" action="BINOLCTPLN02_stop" namespace="/ct" />
<@s.url id="saveAction" action="BINOLCTPLN02_save" namespace="/ct" />
<@s.url id="conditionDisplayAction" action="BINOLCM33_conditionDisplay" namespace='/common'/>
<@s.url id="searchSearchReCordAction" action="BINOLCTPLN02_searchSearchReCord" namespace="/ct" />
<a id="conditionDisplayUrl" href="${conditionDisplayAction}"></a>
<a id="searchSearchReCordUrl" href="${searchSearchReCordAction}"></a>
<div class="panel-header">
    <div class="clearfix"> 
        <span class="breadcrumb left"> 
        	<span class="ui-icon icon-breadcrumb"></span>
        	<a href="#"><@s.text name="ctpln.module" /></a> > 
        	<a href="#"><@s.text name="ctpln.submodule" /></a> > 
        	<a href="#"><@s.text name="ctpln.title" /></a>
        </span>
    </div>
</div>
<div class="panel-content clearfix">
	<div id="actionResultDisplay"></div>
	<div class="section-header" style="height:30px">
		<@s.text name='ctpln.timeOnlyTitle' id="timeOnlyTitle"/>
		<@s.text name='ctpln.currentText' id="currentText"/>
		<@s.text name='ctpln.closeText' id="closeText"/>
		<input name="timeOnlyTitle" class="hide" id="timeOnlyTitle" value="${(timeOnlyTitle)!?html}" />
		<input name="currentText" class="hide" id="currentText" value="${(currentText)!?html}"/>
		<input name="closeText" class="hide" id="closeText" value="${(closeText)!?html}"/>
		<input name="timeText" class="hide" id="timeText" value=""/>
		<input name="hourText" class="hide" id="hourText" value=""/>
		<input name="minuteText" class="hide" id="minuteText" value=""/>
		<@c.form id="mainForm" class="inline" >
		<div class="clearfix" id="eventManage">
			<span class="left">
			    <strong>
			    	<span class="ui-icon icon-ttl-section-search-result"></span>
					<span><@s.text name="ctpln.eventType"/>：</span>
				</strong>
				<@s.select name="eventType" cssStyle="width:150px" list='#application.CodeTable.getCodes("1219")' listKey="CodeKey" listValue="Value" value="" onchange="BINOLCTPLN02.doChange();return false;"/>
			</span>
			<span class="right" id="stopEventSetButton">
				<a href="${(stopEventUrl)!}" class="add right" onclick="BINOLCTPLN02.stopEventSetDialog(this);return false;">
					<span class="ui-icon icon-delete"></span>
					<span class="button-text"><@s.text name="ctpln.stopEventSet" /></span>
				</a>
			</span>
		</div>
		</@c.form>
	</div>
	<div class="section-content" id="setInfoBox">
		<div class="toolbar clearfix section-header">
			<strong>
			<span class="left">
				<span class="ui-icon icon-ttl-section-info"></span>
				<@s.text name="ctpln.eventSetInfo"/>
			</span>
			<span style="color:red">
				<lable id="lblExplanation1" class="hide"><@s.text name="ctpln.Explanation1" /></lable>
				<lable id="lblExplanation2" class="hide"><@s.text name="ctpln.Explanation2" /></lable>
			</span>
			</strong>
			<div id="addMsgBox" class="hide">
			<span class="right">
				<a class="add right" onclick="BINOLCTPLN02.addNewMessage(this);return false;">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="ctpln.addMessage" /></span>
				</a>
			</span>
			</div>
		</div>
		<div class="boxes" id="delaySetBox">
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
	    </div>
	    <div class="hide" id="dialogInit"></div>
	    <div class="hide" id="objDialogInit"></div>
	    <div class="hide" id="resDialogInit"></div>
	    <div class="hide" id="customerDialogInit"></div>
	    <div class="hide" id="resDialogTitle"><@s.text name="ctpln.resDialogTitle" /></div>
	    <div class="hide" id="objDialogTitle"><@s.text name="ctpln.objDialogTitle" /></div>
	    <div class="hide" id="msgDialogTitle"><@s.text name="ctpln.msgDialogTitle" /></div>
	    <div class="hide" id="customerDialogTitle"><@s.text name="ctpln.showCustomerDialogTitle" /></div>
	    <div class="hide" id="searchResult"><@s.text name="ctpln.searchResult" /></div>
	    <div class="hide" id="doChangeUrl">${(changeUrl)!}</div>
	    <div class="dialog2 clearfix" style="display:none" id="save_eventSet_dialog">
			<p class="clearfix">
				<p class="message"><span><@s.text name="ctpln.saveEventSetMessage"/></span></p>
			</p>
		</div>
		<div class="dialog2 clearfix" style="display:none" id="stop_eventSet_dialog">
			<p class="clearfix">
				<p class="message"><span><@s.text name="ctpln.stopEventSetMessage"/></span></p>
			</p>
		</div>
		<div id="saveDialogTitle" class="hide"><@s.text name="ctpln.saveEventSet"/></div>
		<div id="stopDialogTitle" class="hide"><@s.text name="ctpln.stopEventSet"/></div>
		<div id="dialogConfirm" class="hide"><@s.text name="ctpln.ok"/></div>
		<div id="dialogCancel" class="hide"><@s.text name="ctpln.cancel"/></div>
	    <div class="center clearfix" id="pageButton">
	    	<div id="setInfoEdit">
		    	<button onclick="BINOLCTPLN02.infoEdit(this);return false;" class="edit" type="button">
					<span class="ui-icon icon-edit"></span>
					<span class="button-text"><@s.text name="ctpln.edit" /></span>
				</button>
			</div>
			<div id="setInfoSave" class="hide">
		    	<button onclick="BINOLCTPLN02.editCancel(this);return false;" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctpln.cancel" /></span>
				</button>	    	
				<button onclick="BINOLCTPLN02.saveEventSetDialog('${(saveAction)!}');return false;" class="close" type="button">
					<span class="ui-icon icon-confirm"></span>
					<span class="button-text"><@s.text name="ctpln.confirm" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
<div id="importDetailViewBtn" class="hide">
	<hr class="space">
	<a class="add right" href="BINOLCTCOM06_init.action" onclick="BINOLCTPLN02.showCustomerInit(this,2);return false;">
		<span class="ui-icon icon-search"></span>
		<span class="button-text">
			<@s.text name="ctpln.importDetailView" />
		</span>
	</a>
</div>
<div id="addObjName" class="hide">
	<div class="box2-active">
		<div class="box2 box2-content ui-widget">
			<label><strong><@s.text name="ctpln.objName"/><span class="highlight">*</span></strong></label>
			<span><input id="searchName" name="searchName" class="text" type="text" maxlength="45" style="width:40%;"/></span>
			
		</div>
	</div>
</div>
<#-- 沟通对象Excel导入-->
<div class="hide">
	<span id="excelImport"><@s.text name="ctpln.excelImport" /></span>
	<span id="importNameNull"><@s.text name="ctpln.importNameNull" /></span>
	<span id="importCodeNull"><@s.text name="ctpln.importCodeNull" /></span>
</div>
<#-- 引用沟通对象搜索结果页面内容 -->
<@getSearchRecord />
<#-- 引用隐藏的沟通信息内容的宏定义，在点击增加沟通信息按钮时增加的页面内容  -->
<@getHideMsgInfo />
</@s.i18n>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">


