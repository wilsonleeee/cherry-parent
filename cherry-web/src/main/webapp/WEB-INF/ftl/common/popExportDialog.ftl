<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/>
<div id="exportWarnText" class="hide">
	<p class="message" style="margin: 0;text-align: left;padding: 10px 0;background: none repeat scroll 0 0 #F3F9EB;">
		<label><@s.text name="export.exportCharset" /></label>
		<@s.select name="charset" list='#application.CodeTable.getCodes("1279")' listKey="CodeKey" listValue="Value"/>
	</p>
	<p class="message" style="margin: 0;text-align: left;padding: 5px 0 0;border-top: 1px dotted #CCCCCC;">
		<@s.text name="export.exportWarnMsg" />
	</p>
</div>