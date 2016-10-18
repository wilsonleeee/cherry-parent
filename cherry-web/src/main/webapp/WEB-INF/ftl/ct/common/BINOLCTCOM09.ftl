<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM09.js"></script>
<@s.i18n name="i18n.ct.BINOLCTCOM09">
<@s.url id="sendAction" action="BINOLCTCOM09_send" namespace="/ct" />
<div id="div_getPwdView">
	<div id="actionResultDisplay"></div>
	<@s.text name="ctcom.getPwdMobilephone" /></p>
	<div class="clearfix" style="height:100px;">
		<form id="getPwdForm" method="post" class="inline">
			<p><span class="highlight">*</span><span><@s.text name="ctcom.userName" />：<input name="userName" id="userName" class="text" value=""/></span></p>
			<p><span class="highlight">*</span><span><@s.text name="ctcom.mobilePhone" />：<input name="mobilePhone" id="mobilePhone" class="text" value=""/></span></p>
	    </form>
	</div></p>
	<div class="center clearfix" id="pageButton">
		<button class="confirm" id="sendMsg" onClick="BINOLCTCOM09.send('${(sendAction)!}');return false;">
			<span class="ui-icon icon-confirm"></span>
			<span class="button-text"><@s.text name="ctcom.send" /></span>
		</button>
		<button onclick="closeCherryDialog('getPwdDialogInit',this);" class="back" type="button">
			<span class="ui-icon icon-close"></span>
			<span class="button-text"><@s.text name="ctcom.cancel" /></span>
		</button>
	</div>
</div>
<div id="getPwdSuccessResultPage" class="hide">
	<br/>
	<div class="success">
		<div class="center">
			<div class="ui-state-success clearfix">
			  	<ul class="actionMessage">
					<li><span><@s.text name="ctcom.getPwdSendSuccessMessage" /></span></li>
				</ul>
			</div>
			<br/>
			<div>
				<button onclick="closeCherryDialog('getPwdDialogInit',this);" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctcom.close" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
<div id="getPwdErrorResultPage" class="hide">
	<br/>
	<div class="error">
		<div class="center">
			<div class="ui-state-error clearfix">
			  	<ul class="actionMessage">
					<li><span><@s.text name="ctcom.getPwdSendErrorMessage" /></span></li>
				</ul>
			</div>
			<br/>
			<div>
				<button onclick="closeCherryDialog('getPwdDialogInit',this);" class="back" type="button">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><@s.text name="ctcom.close" /></span>
				</button>
			</div>
		</div>
	</div>
</div>
</@s.i18n>