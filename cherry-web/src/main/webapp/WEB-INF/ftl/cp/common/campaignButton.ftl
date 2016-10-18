<#macro campaignButton btnKbn>
	<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<#--if btnKbn == "BTN000001">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text">上一步</span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doDraftSave('${draftSaveUrl}');return false;"><span class="ui-icon icon-save"></span><span class="button-text">存草稿</span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'next');return false;"><span class="ui-icon icon-mover"></span><span class="button-text">下一步</span></button>
	<#elseif btnKbn == "BTN000002">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text">上一步</span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doDraftSave('${draftSaveUrl}');return false;"><span class="ui-icon icon-save"></span><span class="button-text">存草稿</span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'confirm');return false;"><span class="ui-icon icon-mover"></span><span class="button-text">下一步</span></button>
	<#elseif btnKbn == "BTN000003">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text">上一步</span></button>
		<button type="button" class="save" onclick="BINOLCPCOM02.doRuleTest();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text">规则测试</span></button>
          <button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'save');return false;"><span class="ui-icon icon-save"></span><span class="button-text">保存</span></button>
          <#--<button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'save');return false;"><span class="ui-icon icon-mover"></span><span class="button-text">保存并创建相关活动</span></button>-->
	<#--if-->
	<#if btnKbn == "BTN_BACK01">
		<button type="button" class="back" onclick="BINOLCPCOM02.doNext(${backAction}, 'back');return false;"><span class="ui-icon icon-movel"></span><span class="button-text"><@s.text name="cp.back" /></span></button>
	<#elseif btnKbn == "BTN_DRAFTSAVE01" >
		<button type="button" class="save" onclick="BINOLCPCOM02.doDraftSave('${draftSaveUrl}', '${saveActionId}');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><@s.text name="cp.drafSave" /></span></button>
	<#elseif btnKbn == "BTN_NEXT01" >
		<button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'next');return false;"><span class="ui-icon icon-mover"></span><span class="button-text"><@s.text name="cp.next" /></span></button>
	<#elseif btnKbn == "BTN_NEXT02" >
		<button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'confirm');return false;"><span class="ui-icon icon-mover"></span><span class="button-text"><@s.text name="cp.next" /></span></button>
	<#elseif btnKbn == "BTN_SAVE01" >
		<button type="button" class="save" onclick="BINOLCPCOM02.doNext(${nextAction}, 'save');return false;"><span class="ui-icon icon-save"></span><span class="button-text"><@s.text name="cp.save" /></span></button>
	<#-- elseif btnKbn == "BTN_RULETEST01" >
		<button type="button" class="save" onclick="BINOLCPCOM02.doRuleTest();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><@s.text name="cp.ruleTest" /></span></button-->
	<#elseif btnKbn == "BTN_CANCEL01" >
		<button class="close" onclick="window.close();return false;">
        	<span class="ui-icon icon-close"></span>
            <span class="button-text"><@s.text name="cp.cancel" /></span>
         </button>
</#if>
</@s.i18n>
</#macro>