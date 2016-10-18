<#assign cherry=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"]/>
<@s.i18n name="i18n.ct.BINOLCTRPT05">
	<div id="sEcho">${(sEcho)!?html}</div>
	<div id="iTotalRecords">${(iTotalRecords)!?html}</div>
	<div id="iTotalDisplayRecords">${(iTotalDisplayRecords)!?html}</div>
	<div id="analysisTotalInfo" class="hide">
		<span>
			<span id="sendTotalInfo">
				<@s.text name="ctrpt.sendTotal" /><span class="green"><strong><@s.text name="format.number"><@s.param value="${(analysisTotalInfo.totalSendNumber)!?html}"></@s.param></@s.text></strong></span>
			</span>
			<span id="joinTotalInfo">
				<@s.text name="ctrpt.joinTotal" /><span class="green" id="joinTotal"><strong><@s.text name="format.number"><@s.param value="${(analysisTotalInfo.totalSaleNumber)!?html}"></@s.param></@s.text></strong></span>
			</span>
			<span id="rateTotalInfo">
				<@s.text name="ctrpt.rateTotal" />
				<span class="green"><strong>
					<#if analysisTotalInfo.totalSaleNumber?exists && analysisTotalInfo.totalSaleNumber != 0 >
					<@s.text name="format.price">
						<@s.param value="${analysisTotalInfo.totalSaleNumber*100/analysisTotalInfo.totalSendNumber}"></@s.param>
					</@s.text>%
					<#else>
					0
					</#if>
				</strong></span>
			</span>
			<span id="saleTotalInfo">
				<@s.text name="ctrpt.billQuantityTotal" /><span class="green"><strong><@s.text name="format.number"><@s.param value="${(analysisTotalInfo.totalBillQuantity)!?html}"></@s.param></@s.text></strong></span>
				<@s.text name="ctrpt.amountTotal" /><span class="green"><strong><@s.text name="format.price"><@s.param value="${(analysisTotalInfo.totalAmount)!?html}"></@s.param></@s.text></strong></span>
				<@s.text name="ctrpt.quantityTotal" /><span class="green"><strong><@s.text name="format.number"><@s.param value="${(analysisTotalInfo.totalQuantity)!?html}"></@s.param></@s.text></strong></span>
			</span>
		</span>						
	</div>
	<div id="aaData">
		<#if (analysisList?? && analysisList?size>0)>
			<#list analysisList as analysisInfo >
				<ul>
					<li>${(analysisInfo.RowNumber)!?html}</li>
					<li><span>${(analysisInfo.regionName)!?html}</span></li>
					<li><span>${(analysisInfo.departName)!?html}</span></li>
					<li>
						<span>
							<#if analysisInfo.counterCode?exists && analysisInfo.counterCode != ''>
								（${(analysisInfo.counterCode)!?html}）
							</#if>
							${(analysisInfo.counterName)!?html}
						</span>
					</li>
					<li>${(analysisInfo.sendNumber)!?html}</li>
					<li><span id="joinNumber">${(analysisInfo.saleNumber)!?html}</span></li>
					<li>
						<#if analysisInfo.rate?exists && analysisInfo.rate != 0>
							<span class="highlight">${(analysisInfo.rate)!?html}%</span>
						<#else>
							${(analysisInfo.rate)!?html}
						</#if>
					</li>
					<li>
						<span id="saleBillQuantity">
							<@s.text name="format.number"><@s.param value="${(analysisInfo.billQuantity)!?html}" /></@s.text>
						</span>
					</li>
					<li>
						<span id="saleAmount">
							<@s.text name="format.price"><@s.param value="${(analysisInfo.amount)!?html}" /></@s.text>
						</span>
					</li>
					<li>
						<span id="saleQuantity">
							<@s.text name="format.number"><@s.param value="${(analysisInfo.quantity)!?html}" /></@s.text>
						</span>
					</li>
					<li>
						<@s.url action="BINOLCTRPT05_initSendDetail" id="popSendDetailDialogUrl">
							<@s.param name="organizationId" value="${(analysisInfo.organizationId)!?html}" />
						</@s.url>
						<a href="${popSendDetailDialogUrl}" class="edit" onclick="BINOLCTRPT05.popSendDetailDialog(this);return false;">
							<span class="ui-icon icon-copy"></span>
							<span class="button-text">
								<@s.text name="ctrpt.sendDetail" />
							</span>
						</a>
						<@s.url action="BINOLCTRPT05_initJoinDetail" id="popJoinDetailDialogUrl">
							<#if analysisInfo.organizationId?exists>
								<@s.param name="organizationId" value="${(analysisInfo.organizationId)!?html}" />
							<#else>
								<@s.param name="organizationId" value="0" />
							</#if>
						</@s.url>
						<a href="${popJoinDetailDialogUrl}" class="edit" onclick="BINOLCTRPT05.popJoinDetailDialog(this);return false;">
							<span class="ui-icon icon-copy"></span>
							<span class="button-text">
								<@s.text name="ctrpt.joinDetail" />
							</span>
						</a>
						<@s.url action="BINOLCTRPT05_initSaleDetail" id="popSaleDetailDialogUrl">
							<#if analysisInfo.organizationId?exists>
								<@s.param name="organizationId" value="${(analysisInfo.organizationId)!?html}" />
							<#else>
								<@s.param name="organizationId" value="0" />
							</#if>
						</@s.url>
						<a href="${popSaleDetailDialogUrl}" class="edit" onclick="BINOLCTRPT05.popSaleDetailDialog(this);return false;">
							<span class="ui-icon icon-copy"></span>
							<span class="button-text">
								<@s.text name="ctrpt.saleDetail" />
							</span>
						</a>
					</li>
				</ul>
			</#list>
		</#if>
	</div>
</@s.i18n>