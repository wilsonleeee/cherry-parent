<#macro campaignButton btnKbn>
	<@s.i18n name="i18n.cp.BINOLCPCOM01">
	
	<#if btnKbn == "BTN000001">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text"><@s.text name="cp.back" /></span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doDraftSave('${draftSaveUrl}');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><@s.text name="cp.drafSave" /></span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'next');return false;"><span class="ui-icon icon-mover"></span><span class="button-text"><@s.text name="cp.next" /></span></button>
	<#elseif btnKbn == "BTN000002">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text"><@s.text name="cp.back" /></span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doDraftSave('${draftSaveUrl}');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><@s.text name="cp.drafSave" /></span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'confirm');return false;"><span class="ui-icon icon-mover"></span><span class="button-text"><@s.text name="cp.next" /></span></button>
	<#elseif btnKbn == "BTN000003">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text"><@s.text name="cp.back" /></span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'save');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><@s.text name="cp.save" /></span></button>
	</#if>
	
</@s.i18n>
</#macro>