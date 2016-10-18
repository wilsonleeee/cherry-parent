<#macro campaignSteps stepKbn>
	<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<#-- 会员入会导航条  -->
	<#if stepKbn == "STEP000001">
          <li style="width:33.3%"><span><@s.text name="cp.levelAndDate" /></span></li>
          <li style="width:33.3%"><span><@s.text name="cp.setRule" /></span></li>
          <li style="width:33.3%"><span><@s.text name="cp.showAndSave" /></span></li>
	</#if>
	
</@s.i18n>
</#macro>