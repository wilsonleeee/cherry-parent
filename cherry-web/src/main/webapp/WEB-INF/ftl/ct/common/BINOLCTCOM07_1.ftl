<#assign c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTCOM07">
<script type="text/javascript" src="/Cherry/js/ct/common/BINOLCTCOM07.js"></script>
<div id="sendResultPage">
	<div class="header container clearfix">
		<br>
		<h1 class="logo left">
			<a href="#"><@s.text name="ctcom.logotext" /></a>
		</h1>
	</div><br/>
	<@s.if test="hasActionErrors()">
	<div class="error">
		<div class="center">
			<div class="ui-state-error clearfix">
				<@s.actionerror />
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
	</@s.if>
	<br/>
	<@s.if test="hasActionMessages()">
	<div class="success">
		<div class="center">
			<div class="ui-state-success clearfix">
				<@s.actionmessage />
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
	</@s.if>	
	
	<div class="footer">
		<@s.text name="ctcom.pagefootertext" />
	</div>
</div>
</@s.i18n>
