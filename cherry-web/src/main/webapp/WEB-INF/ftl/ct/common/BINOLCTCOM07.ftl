<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTCOM07">
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM07.js"></script>
<@s.url id="saveAction" action="BINOLCTCOM07_send" />
<@s.url id="getTemplateUrl" action="BINOLCTCOM08_init" namespace="/ct" />
<div id="div_sendview">
	<div class="main container clearfix">
		<div class="panel ui-corner-all">
			<div class="panel-header">
				<div class="clearfix">
					<span class="breadcrumb left"> 
						<span class="ui-icon icon-breadcrumb"></span>
	        			<@s.text name="ctcom.submodule" />
	        		</span>
				</div>
			</div>
			<div class="panel-content">
				<div class="clearfix">
					<div class="left" style="width:78%">
						<span class="left"> 
		        			<strong>
		        				<@s.text name="ctcom.receiverCode" />
		        				${(mobilePhone)!?html}
		        			</strong>
		        		</span>
					</div>
					<div class="right" style="width:20%">
						<div class="clearfix">
				        	<a id="chooseTemplate" href="${getTemplateUrl}" class="edit right" style="width:100px" onclick="BINOLCTCOM07.getTemplateInit(this);return false;">
								<span class="ui-icon icon-edit"></span>
								<span class="button-text"><strong><@s.text name="ctcom.chooseTemplate" /></strong></span>
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="panel ui-corner-all">
		<div class="panel-content">
			<div class="clearfix">
	            <div class="sidemenu2 left" style="width:20%">
					<div class="sidemenu2-header">
						<strong><@s.text name="ctcom.templateParam" /></strong>
					</div>
				    <ul class="u1" style="width:100%; height:405px; overflow:auto;" id="templateParam">
				    	<#if (paramList?? && paramList?size>0)>
							<#list paramList as paramInfo >
								<li onclick="BINOLCTCOM07.selectParam(this);return false;">
									<input name="paramCode" id="paramCode" type="text" class="text hide" value="${(paramInfo.variableCode)!?html}"/>
									<span>${(paramInfo.variableName)!?html}</span>
									<input name="paramValue" id="paramValue" type="text" class="text hide" value="${(paramInfo.variableValue)!?html}"/>
									<input name="comments" id="comments" type="text" class="text hide" value="${(paramInfo.comments)!?html}"/>
								</li>
							</#list>
						</#if>
					</ul>
				</div>
	            <div class="right" style="width:78%" id="messageContents">
	            	<form id="sendMsgForm" method="post" class="inline" >
						<input name="mobilePhone" class="hide" id="mobilePhone" value="${(mobilePhone)!?html}"/>
						<input name="customerSysId" class="hide" id="customerSysId" value="${(customerSysId)!?html}"/>
						<input name="memberCode" class="hide" id="memberCode" value="${(memberCode)!?html}"/>
						<input name="sourse" class="hide" id="sourse" value="${(sourse)!?html}"/>
			            <div class="box-content" style="min-height:425px; margin:2px">
							<hr class="space" />
							<@s.text name="ctcom.msgContents" />
					        <div class="clearfix" style="height: 200px;">
					            <span id="contentsSpan">
					            	<textarea cols="" id="msgContents" style="width: 95%; height:85%;" onkeyup="BINOLCTCOM07.changeViewValue(this);return false;"></textarea>
					            	<input name="msgContents" id="msgContentsTemp" value="" class="hide" /> 
					            </span>
					            <span class="clearfix right" style="margin-right:2%">
					            	<@s.text name="ctcom.signature" />：
					            	<input id="signature" style="width: 80px;" class="text" type="text" value="${(signature)!?html}" onkeyup="BINOLCTCOM07.changeViewValue('#msgContents');return false;">
					            	<a id="expandIllegalChar" onclick="BINOLCTCOM07.expandIllegalChar(this)"  class="ui-select"> 
										<span class="button-text"><@s.text name="ctcom.illegalCharList" /></span> 
										<span class="ui-icon ui-icon-triangle-1-n"></span>
									</a>
					            </span>
					        </div>
					        <hr class="space" />
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
					        <@s.text name="ctcom.msgView" />
					        <div class="clearfix" style="height: 135px;">
					            <textarea id="contentsView" class="text disabled" disabled="disabled" type="text" style="width: 95%; height:85%;" ></textarea>
					        </div>
					        <label><strong><@s.text name="ctcom.countExp1" /></strong></label>
					        <label id="countTextNum"><@s.text name="ctcom.countTextNum" /></label>
					        <label><strong><@s.text name="ctcom.countExp2" /></strong></label>
					        <label id="countMsgNum"><@s.text name="ctcom.countMsgNum" /></label>
					        <label><strong><@s.text name="ctcom.countExp3" /></strong></label>
					        <hr class="space" />
					    </div>
				    </form>
	            </div>
			</div>
			</p>
			<div class="center clearfix" id="pageButton">
	        	<button class="confirm" id="sendMsg" onClick="BINOLCTCOM07.sendMsg('${(saveAction)!}');return false;">
	        		<span class="ui-icon icon-confirm"></span>
	        		<span class="button-text"><@s.text name="ctcom.send" /></span>
	        	</button>
	   		</div>
		</div>
	</div>
</div>
<div class="hide" id="chooseTemplateTitle"><@s.text name="ctcom.chooseTemplateTitle" /></div>
<div id="sendResultPage" class="hide">
	<div class="header container clearfix">
		<br>
		<h1 class="logo left">
			<a href="#"><@s.text name="ctcom.logotext" /></a>
		</h1>
	</div><br/>
	<div class="success">
		<div class="center">
			<div class="ui-state-success clearfix">
			  	<ul class="actionMessage">
					<li><span><@s.text name="ctcom.message" /></span></li>
				</ul>
			</div>
			<br/>
			<div>
				<button onclick="closeCherryDialog('sendDialogInit',this);" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctcom.close" /></span>
				</button>
			</div>
		</div>
	</div>
	<div class="footer">
		<@s.text name="ctcom.pagefootertext" />
	</div>
</div>
</@s.i18n>
