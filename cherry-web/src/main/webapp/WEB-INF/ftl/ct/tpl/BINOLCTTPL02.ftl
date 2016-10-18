<#include "/WEB-INF/ftl/common/head.inc.ftl">
<#include "/WEB-INF/ftl/ct/common/BINOLCTCOM04_mac.ftl">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/tpl/BINOLCTTPL02.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM04.js"></script>
<@s.i18n name="i18n.ct.BINOLCTTPL02">
<@s.url id="saveAction" action="BINOLCTTPL02_save" />
<@s.url id="changeUrl" action="BINOLCTTPL02_change" />
<input class="hide" id="isNeedInit" value="false"/>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
			<div class="clearfix">
				<span class="breadcrumb left"> 
					<span class="ui-icon icon-breadcrumb"></span>
        			<a href="#"><@s.text name="cttpl.submodule" /></a> > 
        			<#if showType?exists && showType == 'edit'>
        				<a href="#"><@s.text name="cttpl.editTemplate" /></a>
        			<#elseif showType?exists && showType == 'copy'>
        				<a href="#"><@s.text name="cttpl.copyTemplate" /></a>
        			<#else>
        				<a href="#"><@s.text name="cttpl.addTemplate" /></a>
        			</#if>
        		</span>
			</div>
		</div>
		<div class="panel-content">
			<div class="box2-active">
				<form id="templateForm" method="post" class="inline" >
					<input name="showType" class="hide" id="showType" value="${(showType)!?html}"/>
					<input name="templateCode" class="hide" id="templateCode" value="${(templateCode)!?html}"/>
					<div class="box-content clearfix">
						<div class="column" style="width:49%;">
						<@s.text name='cttpl.allvalue' id="allvalue"/>
						<p>
							<span><label><@s.text name="cttpl.templateName"/></label>
							<input name="templateName" class="text" type="text" value="${(templateName)!?html}" /></span>
						</p>
						<p>
							<label><@s.text name="cttpl.templateUse"/></label>
		               		<@s.select name="templateUse" list='#application.CodeTable.getCodes("1197")' listKey="CodeKey" listValue="Value" value="'${(templateUse)!?html}'" onchange="BINOLCTTPL02.doChange();return false;" />
		               	</p>
		                </div>
						<div class="column last" style="width:49%;">
						<p>
							<label style="width:90px"><@s.text name="cttpl.customerType"/></label>
							<@s.select name="customerType" list='#application.CodeTable.getCodes("1198")' listKey="CodeKey" listValue="Value" value="${(customerType)!?html}" />
						</p>
		                </div>
					</div>
					<div class="box-content">
						</p><div class="clearfix">
				            <div class="sidemenu2 left" style="width:20%;">
								<div class="sidemenu2-header">
									<strong><@s.text name="cttpl.templateParam" /></strong>
								</div>
							    <ul class="u1" style="width:100%; height:380px; overflow:auto;" id="templateParam">
							    	<#-- 调用沟通模板参数列表页面宏定义 -->
							    	<@getTemplateParam paramList=paramList/>
								</ul>
							</div>
							<div class="right" style="width: 78%" id="messageContents">
				                <@getTemplateEditPage msgContents=msgContents/>
				            </div>
			            </div></p>
					</div>
				</form>
				</p>
				<div class="hide" id="doChangeUrl">${(changeUrl)!}</div>
				<div class="center clearfix" id="pageButton">
					<button onclick="window.close();return false;" class="back" type="button">
						<span class="ui-icon icon-close"></span>
						<span class="button-text"><@s.text name="cttpl.close" /></span>
					</button>
		        	<button class="save" type="button" onClick="BINOLCTTPL02.doSave('${(saveAction)!}');return false;">
		        		<span class="ui-icon icon-save"></span>
						<span class="button-text"><@s.text name="cttpl.save" /></span>
		        	</button>
		   		</div>
			</div>
		</div>
	</div>
</div>
<div class="hide" id="paramCheckRemindPage">
	<#-- 调用沟通信息衍生参数选择提示页面宏定义 -->
	<@getParamCheckRemindPage /> 
</div>
<div id="paramRemindDialogTitle" class="hide"><@s.text name="cttpl.paramCheckRemind"/></div>
<div id="paramRemindDialogClose" class="hide"><@s.text name="cttpl.close"/></div>
</@s.i18n>
