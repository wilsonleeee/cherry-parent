<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTTPL02">
<#-- 引用宏 -->
<#include "/WEB-INF/ftl/ct/common/BINOLCTCOM04_mac.ftl">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM04.js"></script>
<@s.url id="searchtpl_url" action="BINOLCTCOM04_search"/>
<@s.url id="saveTemplate_url" action="BINOLCTCOM04_save"/>
<div class="hide">
	<a id="searchTplUrl" href="${searchtpl_url}"></a>
	<a id="saveTemplateUrl" href="${saveTemplate_url}"></a>
	<span id="msgContentsNull"><@s.text name="cttpl.msgContentsNull" /></span>
</div>
<#-- 提示信息 -->
<div id="actionResultDiv1" class="hide">
	<ul>
		<li><span id="ActionResultSpan1"></span></li>
	</ul>
</div>	

<div class="ui-tabs" id="ui-tabs">
	<ul class="ui-tabs-nav clearfix">
        <li class="ui-tabs-selected">
        	<a href="#tabs-1" onclick="BINOLCTCOM04.setDisplay('div.advanced','show')"><@s.text name="cttpl.chooseTemplate"/></a>
        </li>
        <li>
        	<a href="#tabs-2" onclick="BINOLCTCOM04.setDisplay('div.advanced','hide')"><@s.text name="cttpl.editMessage"/></a>
        </li>
    </ul>
	<div class="ui-tabs-panel" id="tabs-1">
		<div class="box">
			<form id="getMessageForm" method="post" class="inline"> 
				<input name="status" class="hide" id="status" value="1"/>
				<input name="messageType" class="hide" id="messageType" value="${(messageType)!?html}"/>
				<#-- 调用沟通模板查询条件页面宏定义 -->
				<@getTemplateSearchCondition />
			</form>
		</div>
		<#-- 调用沟通模板查询结果页面宏定义 -->
		<@getTemplateSearchResult />
	</div>
	<div class="ui-tabs-panel" id="tabs-2">
		<div class="clearfix" style="height:25px;">
	        <a class="add right" onclick="BINOLCTCOM04.getDialog();return false;">
				<span class="ui-icon icon-save-s" style="margin-left:2px;"></span>
				<span class="button-text"><@s.text name="cttpl.saveTemplate" /></span>
			</a>
		</div>
        <div class="clearfix">
            <div class="sidemenu2 left" style="width:20%">
				<div class="sidemenu2-header">
					<strong><@s.text name="cttpl.templateParam" /></strong>
				</div>
			    <ul class="u1" style="width:100%; height:405px; overflow:auto;" id="templateParam">
			    	<#-- 调用沟通模板参数列表页面宏定义 -->
			    	<@getTemplateParam paramList=paramList/>
				</ul>
			</div>
            <div class="right" style="width:78%" id="messageContents">
	            <@getTemplateEditPage msgContents=msgContents/>                
            </div>
		</div>
		</p>
		<div class="center clearfix">
        	<button class="confirm" id="editTemplate" onClick="BINOLCTCOM04.editMsgTemplate();return false;">
        		<span class="ui-icon icon-confirm"></span>
        		<span class="button-text"><@s.text name="global.page.ok" /></span>
        	</button>
   		</div>
	</div>
</div>
<div class="hide" id="templateSavePage">
	<#-- 调用保存沟通模板页面宏定义 -->
	<@getTemplateSavePage />          
</div>
<div class="hide" id="paramCheckRemindPage">
	<#-- 调用沟通信息衍生参数选择提示页面宏定义 -->
	<@getParamCheckRemindPage /> 
</div>
<div id="templateSaveDialogTitle" class="hide"><@s.text name="cttpl.saveTemplate" /></div>
<div id="paramRemindDialogTitle" class="hide"><@s.text name="cttpl.paramCheckRemind"/></div>
<div id="paramRemindDialogClose" class="hide"><@s.text name="cttpl.close"/></div>
<#include "/WEB-INF/ftl/common/dataTable_i18n.ftl">
</@s.i18n>