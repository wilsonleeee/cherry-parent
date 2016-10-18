<#-- 沟通模板查询条件页面宏定义 -->
<#macro getTemplateSearchCondition>
	<div class="box-header">
		<strong><span class="ui-icon icon-ttl-search"></span><@s.text name="global.page.condition" /></strong>
	</div>
	<div class="box-content clearfix">
		<div class="column" style="width:49%;">
		<@s.text name='cttpl.allvalue' id="allvalue"/>
		<p>
			<label><@s.text name="cttpl.templateName"/></label>
			<input name="templateName" class="text" type="text" />
		</p>
        </div>
        <#if (!templateUse?exists)>
		<div class="column last" style="width:50%;">
		<p>
			<label style="width:90px"><@s.text name="cttpl.templateUse"/></label>
       		<@s.select name="templateUse" list='#application.CodeTable.getCodes("1197")' listKey="CodeKey" listValue="Value" 
       		headerKey="" headerValue="%{#allvalue}" value="" />
       	</p>
        </div>
        <#else>
			<input name="templateUse" class="hide" id="templateUse" value="${(templateUse)!?html}"/>
        </#if>
	</div>
	<p class="clearfix">
		<button class="right search"  onclick="search();return false;">
          	<span class="ui-icon icon-search-big"></span>
          	<span class="button-text"><@s.text name="cttpl.search" /></span>
		</button>
	</p>
</#macro>

<#-- 沟通模板查询结果页面宏定义 -->
<#macro getTemplateSearchResult>
	<div id="templatedetail" class="section">
		<div class="section-header">
			<strong>
	        	<span class="ui-icon icon-ttl-section-search-result"></span>
	        	<@s.text name="cttpl.showFindResult" />
	        </strong>
			<span class="right">
				<a href="#" class="setting">
					<span class="button-text"><@s.text name="global.page.colSetting"/></span>
		 			<span class="ui-icon icon-setting"></span>
				</a>
			</span>
		</div>
		<div class="section-content">
			<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
			<thead>
				<tr>
					<th><@s.text name="cttpl.rowNumber" /></th>
					<th><@s.text name="cttpl.templateCode" /></th>
					<th><@s.text name="cttpl.templateName" /></th>
					<th><@s.text name="cttpl.templateUse" /></th>
					<th><@s.text name="cttpl.contents" /></th>
					<th><@s.text name="cttpl.customerType" /></th>
					<th><@s.text name="cttpl.templateStatus" /></th>
					<th><@s.text name="cttpl.act" /></th>
				</tr>
			</thead>
			<tbody id="dataTableBody">
			</tbody>
			</table>
		</div>
	</div>
</#macro>

<#-- 模板参数列表宏定义 -->
<#macro getTemplateParam paramList=[]>
	<#if (paramList?? && paramList?size>0)>
		<#list paramList as paramInfo >
			<li onclick="BINOLCTCOM04.selectParam(this);return false;">
				<input name="paramCode" id="paramCode" type="text" class="text hide" value="${(paramInfo.variableCode)!?html}"/>
				<span>${(paramInfo.variableName)!?html}</span>
				<input name="paramValue" id="paramValue" type="text" class="text hide" value="${(paramInfo.variableValue)!?html}"/>
				<input name="paramType" id="paramType" type="text" class="text hide" value="${(paramInfo.type)!?html}"/>
				<input name="comments" id="comments" type="text" class="text hide" value="${(paramInfo.comments)!?html}"/>
			</li>
		</#list>
	</#if>
</#macro>

<#-- 沟通模板编辑页面宏定义 -->
<#macro getTemplateEditPage msgContents=''>
	<div class="ui-tabs-panel">
		<@s.text name="cttpl.msgContents" />
        <div class="clearfix" style="height: 170px;">
            <span>
            	<textarea cols="" id="msgContents" style="width: 95%; height:90%;" onkeyup="BINOLCTCOM04.changeViewValue(this);return false;">${(msgContents)!?html}</textarea>
            	<input name="msgContents" id="msgContentsTemp" value="" class="hide" /> 
            </span>
        </div>
        <div class="clearfix" style="width:95%">
	        <span class="right">
        		<label><@s.text name="cttpl.signature" />：</label>
        		<input id="signature" style="width: 80px;" class="text" type="text" value="${(signature)!?html}" onkeyup="BINOLCTCOM04.changeViewValue('#msgContents');return false;">
	        	<a id="expandIllegalChar" onclick="BINOLCTCOM04.expandIllegalChar(this)"  class="ui-select"> 
					<span class="button-text"><@s.text name="cttpl.illegalCharList" /></span> 
					<span class="ui-icon ui-icon-triangle-1-n"></span>
				</a>
	        </span>
	    </div>
        <@getIllegalChar illegalCharList=illegalCharList />
        <@s.text name="cttpl.msgView" />
        <div class="clearfix" style="height: 150px;">
            <textarea id="contentsView" class="text disabled" disabled="disabled" type="text" style="width: 95%; height:80%;" ></textarea>
        </div>
        <label><strong><@s.text name="cttpl.countExp1" /></strong></label>
        <label id="countTextNum"><@s.text name="cttpl.countTextNum" /></label>
        <label><strong><@s.text name="cttpl.countExp2" /></strong></label>
        <label id="countMsgNum"><@s.text name="cttpl.countMsgNum" /></label>
        <label><strong><@s.text name="cttpl.countExp3" /></strong></label>
    </div>
</#macro>

<#-- 模板内容非法字符列表宏定义 -->
<#macro getIllegalChar illegalCharList=[]>
	<div id="illegalCharDiv" class="clearfix ui-corner-all" style="width:95%; display:none; padding:5px; background:#FFF; border:1px #CCC solid;">
		<#if (illegalCharList?? && illegalCharList?size>0)>
			<ul>
				<#list illegalCharList as illegalCharInfo >
					<li style="list-style:none; float:left; background:#F9F9F9; border:1px #CCC solid; padding:1px 5px; margin:1px 2px; cursor:pointer;">
						${(illegalCharInfo.charValue)!?html}
					</li>
				</#list>
			</ul>
		<#else>
			<@s.text name="table_sZeroRecords" />
		</#if>
		<div id="illegalCharError" class="hide"><@s.text name="cttpl.illegalCharError" /></div>
		<div id="illegalCharCount" class="hide"><@s.text name="cttpl.illegalCharCount" /></div>
	</div>
</#macro>

<#-- 沟通信息衍生参数选择提示页面宏定义 -->
<#macro getParamCheckRemindPage>
	<div class="center">
		<div class="clearfix">
		  	<p class="message" style="margin:5% auto 0">
		  		<span><@s.text name="cttpl.paramCheckRemindText"/></span>
		  	</p>
		</div>
	</div>
</#macro>

<#-- 沟通模板保存页面宏定义 -->
<#macro getTemplateSavePage>
		<table class="detail">
			<tr>
				<th>
					<@s.text name="cttpl.templateName" />
					<span class="highlight">*</span>
				</th>
				<td>
					<span>
						<input type="text" name="templateName" class="text" id="templateName" maxlength="50" />
					</span>	
				</td>
			<tr>
			<tr>
				<th><@s.text name="cttpl.templateUse" /></th>
				<td>
					<#if (!templateUse?exists)>
			       		<@s.select name="templateUse" list='#application.CodeTable.getCodes("1197")' listKey="CodeKey" listValue="Value" />
			        <#else>
			        	${application.CodeTable.getVal("1197","${(templateUse)!?html}")}
						<input name="templateUse" class="hide" id="templateUse" value="${(templateUse)!?html}"/>
			        </#if>
				</td>
			<tr>
			<tr>
				<th><@s.text name="cttpl.templateType" /></th>
				<td>
					<#if (!messageType?exists)>
			       		<@s.select name="messageType" list='#application.CodeTable.getCodes("1203")' listKey="CodeKey" listValue="Value" />
			        <#else>
			        	${application.CodeTable.getVal("1203","${(messageType)!?html}")}
						<input name="messageType" class="hide" id="messageType" value="${(messageType)!?html}"/>
			        </#if>
				</td>
			<tr>
			<tr>
				<th><@s.text name="cttpl.customerType" /></th>
				<td>
					<@s.select name="customerType" list='#application.CodeTable.getCodes("1198")' listKey="CodeKey" listValue="Value"/>
				</td>
			<tr>
		</table>
	<div class="center">
		<a class="add" onclick="BINOLCTCOM04.confirmDialog();return false;">
			<span class="ui-icon icon-save-s" style="margin-left:2px;"></span>
			<span class="button-text"><@s.text name="cttpl.save" /></span>
		</a>
		<a class="add" onclick="BINOLCTCOM04.closeDialog();return false;">
			<span class="ui-icon icon-disable" style="margin-left:2px;"></span>
			<span class="button-text"><@s.text name="cttpl.close" /></span>
		</a>
	</div>
</#macro>