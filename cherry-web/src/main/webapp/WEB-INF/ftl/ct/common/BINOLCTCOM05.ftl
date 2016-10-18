<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM05.js"></script>
<@s.i18n name="i18n.ct.BINOLCTPLN01">
<@s.url id="sendAction" action="BINOLCTCOM05_send" />
<div id="div_testview">
	<@s.text name="ctpln.resCodeList" /><span class="highlight">*</span></p>
	<div class="clearfix" style="height:100px;">
		<form id="resCodeForm" method="post" class="inline">
			<input name="smsChannel" class="hide" id="smsChannel" value="${(smsChannel)!?html}"/>
			<input name="messageType" class="hide" id="messageType" value="${(messageType)!?html}"/>
			<input name="contents" class="hide" id="contents" value="${(contents)!?html}"/>
	    	<textarea cols="" id="resCodeList" style="width:97%;" name="resCodeList"></textarea>
	    </form>
	</div></p>
	<div class="center clearfix" id="pageButton">
		<button class="confirm" id="sendMsg" onClick="BINOLCTCOM05.send('${(sendAction)!}');return false;">
			<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><@s.text name="ctpln.send" /></span>
		</button>
		<button onclick="closeCherryDialog('resDialogInit',this);" class="back" type="button">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><@s.text name="ctpln.cancel" /></span>
		</button>
	</div>
</div>
<div id="testResultPage" class="hide">
	<br/>
	<div class="success">
		<div class="center">
			<div class="ui-state-success clearfix">
			  	<ul class="actionMessage">
					<li><span><@s.text name="ctpln.testSendMessage" /></span></li>
				</ul>
			</div>
			<br/>
			<div>
				<button onclick="closeCherryDialog('resDialogInit',this);" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctpln.close" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
</@s.i18n>