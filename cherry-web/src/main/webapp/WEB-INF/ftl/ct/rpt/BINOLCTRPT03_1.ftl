<#assign s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"]/> 
<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/> 
<@s.i18n name="i18n.ct.BINOLCTRPT03">
<div id="sEcho">${(sEcho)!?html}</div>
<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
<div id="aaData">
<#if (msgDetailList?? && msgDetailList?size>0)>
	<#list msgDetailList as msgDetailInfo >
		<@s.url id="sendUrl" action="BINOLCTRPT03_send" namespace="/ct">
			<@s.param name="messageCode">${(msgDetailInfo.messageCode)!?html}</@s.param>
		</@s.url>
		<ul>
			<li>${(msgDetailInfo.RowNumber)!?html}</li>
			<li>${(msgDetailInfo.planCode)!?html}</li>
			<li>
				<#if ((msgDetailInfo.runType)! == 1) >
					${(msgDetailInfo.planName)!?html}
				<#elseif ((msgDetailInfo.runType)! == 3) >
					<#if ((msgDetailInfo.planCode)! == '9') >
						<@s.text name="ctrpt.resendEvent"/>
					<#elseif ((msgDetailInfo.planCode)! == '10') >
						<@s.text name="ctrpt.sendEvent"/>
					<#elseif ((msgDetailInfo.planCode)! == '99') >
						<@s.text name="ctrpt.testEvent"/>
					<#elseif ((msgDetailInfo.planCode)! == '100') >
						<@s.text name="ctrpt.sysEvent"/>
					<#else>
						${application.CodeTable.getVal("1219","${(msgDetailInfo.planCode)!?html}")}
					</#if>
				</#if>
			</li>
			<li>${application.CodeTable.getVal("1262","${(msgDetailInfo.runType)!?html}")}</li>
			<li>${(msgDetailInfo.memCode)!?html}</li>
			<li>${(msgDetailInfo.memName)!?html}</li>
			<li>${(msgDetailInfo.joinDate)!?html}</li>
			<li>${(msgDetailInfo.birthDay)!?html}</li>
			<li>${(msgDetailInfo.counterCode)!?html}</li>
			<li>${(msgDetailInfo.counterName)!?html}</li>
			<li>${(msgDetailInfo.rsm)!?html}</li>
			<li>${(msgDetailInfo.amm)!?html}</li>
			<li>${(msgDetailInfo.mobilephone)!?html}</li>
			<li><@s.text name="ctrpt.message" id="message"/>
				<a title="${message}|${(msgDetailInfo.message)!?html}" class="description" style="color:black">${(msgDetailInfo.messageCut)!?html}</a>
			</li>
			<li>${(msgDetailInfo.sendTime)!?html}</li>
			<li>${(msgDetailInfo.couponCode)!?html}</li>
			<li>
				<@cherry.show domId="CTRPT03SEND">
				<a id="testButton" class="edit" onclick="BINOLCTRPT03.getSendDialog(this);return false;" href="${sendUrl}">
					<span class="ui-icon icon-ok"></span>
					<span class="button-text"><@s.text name="ctrpt.send" /></span>
				</a>
				</@cherry.show>
			</li>
		</ul>
	</#list>
</#if>
</div>
</@s.i18n>