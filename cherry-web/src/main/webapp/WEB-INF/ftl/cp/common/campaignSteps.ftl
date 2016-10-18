<#macro campaignSteps stepKbn>
	<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<#-- 会员入会导航条  -->
	<#if stepKbn == "STEP000001">
		<@s.iterator value="stepNames" status="status">  
          <li><span>1.等级和有效期</span></li>
        </@s.iterator> 
    <#elseif stepKbn == "STEP000002">
    	  <li style="width:33.3%"><span>1.活动范围</span></li>
          <li style="width:33.3%"><span>2.活动奖励</span></li>
          <li style="width:33.3%"><span>3.预览和确认</span></li>
	</#if>
	</@s.i18n>
</#macro>