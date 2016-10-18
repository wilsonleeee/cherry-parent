<@s.i18n name="i18n.cp.BINOLCPCOM04">
<#if (ruleTestDTO.resultFlag == 0)>
<div class="ruleSuccess">
  <ul>
   	 <li style="margin-bottom:10px;"><span class="ui-icon icon-success"></span><span class="result-info">${(ruleTestDTO.resultDpt)!?html}</span></li>
   	 <li><span style="color:#808080;"><@s.text name="matchingRule"/></span><span>${(ruleTestDTO.resultDetail)!?html}</span></li>
  </ul>
 </div>
 <#elseif (ruleTestDTO.resultFlag == -1)>
 <div class="ruleSuccess">
	<ul>
   	 <li style="margin-bottom:10px;"><span class="ui-icon icon-error"></span><span class="result-info"><@s.text name="unMatchRule"/></span></li>
  </ul>
  </div>
</#if>
</@s.i18n>