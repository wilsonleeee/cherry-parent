<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC02.js?v=20160926"></script>
<s:i18n name="i18n.mb.BINOLMBSVC02">
	<div class="hide">
		<s:url id="s_stopCard" value="/mb/BINOLMBSVC02_stopCard" />
		<a id="stopCardUrl" href="${s_stopCard}"></a>
		<s:url id="s_abandonCard" value="/mb/BINOLMBSVC02_abandonCard" />
		<a id="abandonCardUrl" href="${s_abandonCard}"></a>
		<s:url id="s_cardRechargeInit" value="/mb/BINOLMBSVC03_cardRechargeInit" />
		<a id="cardRechargeInitUrl" href="${s_cardRechargeInit}"></a>
		<s:url id="s_openCardInit" value="/mb/BINOLMBSVC02_openCardInit" />
		<a id="openCardInitUrl" href="${s_openCardInit}"></a>
	</div>
	<div id="dialogInit" class="hide">
	</div>
	<div class="panel-header">
		<div id="actionResultDisplay"></div>
		<div id="successDiv" class="actionSuccess" style="display:none;">
			<ul class="actionMessage">
		  		<li><span id="successSpan"></span></li>
		 	</ul>
		</div>
		<div id="errorMessage"></div>
		<div class="clearfix">
			<span class="breadcrumb left"> <span
				class="ui-icon icon-breadcrumb"></span> <s:text
					name="SVC02_svcTitle" /> &gt; <s:text name="SVC02_cardManager" /></span>
			<span class="right"> <%-- 储值卡充值按钮 --%> 
				<cherry:show domId="BINOLMBSVC0201">
					<a  class="add"
						onclick="BINOLMBSVC02.cardRechargeInit();return false;"> <span
						class="button-text"><s:text name="SVC02_cardRecharge" /></span> <span
						class="ui-icon icon-add"></span>
					</a>
				</cherry:show>
				<cherry:show domId="BINOLMBSVC0202">
					<a  class="add"
						onclick="BINOLMBSVC02.openCardInit();return false;"> <span
						class="button-text"><s:text name="SVC02_cardOpen" /></span> <span
						class="ui-icon icon-add"></span>
					</a>
				</cherry:show>
			</span>
		</div>
	</div>
	<div class="panel-content">
		<div class="box">
			<cherry:form id="cardForm" class="inline">
				<div class="box-header">
					<%--主题活动搜索条件--%>
					<span class="ui-icon icon-ttl-search"></span> <strong><s:text
							name="SVC02_cardSearch" /></strong>
					<input id="dateType" type="checkbox" value="1" name="dateType">
					<s:text name="SVC02_dateType"></s:text>
				</div>
				<div class="box-content clearfix">
					<div style="width: 50%; height: 100px;" class="column">
						<p class="clearfix">
							<%--储值卡卡号--%>
							<label style="width: 80px;"><s:text name="SVC02_cardCode" /></label>
							<span><input id="cardCode" class="text" name="cardCode"></span>
						</p>
						<p class="clearfix" style="">
							<%--储值卡状态--%>
							<label style="width: 80px;"><s:text
									name="SVC02_cardState" /></label> <span> <s:select
									name="cardState" Cssstyle="width:186px;"
									list='#application.CodeTable.getCodes("1339")'
									listKey="CodeKey" listValue="Value" headerKey=""
									headerValue="%{selectAll}" value="''" />
							</span>
						</p>
						<input id="cardType" class="hide" name="cardType" value="${cardType }">
					</div>
					<div style="width: 49%; height: 55px;" class="column last">
						<p class="clearfix">
							<%--预留手机号--%>
							<label style="width: 80px;"><s:text
									name="SVC02_obligateMobile" /></label> <span><input
								id="mobilePhone" class="text" name="mobilePhone"></span>
						</p>
						<s:if test='counterCode==""'>
							<p>
					          <label style="width: 80px;"><s:text name="SVC02_departName"></s:text></label>
					            <span>
					                  <input id="counterCode" name="counterCode" class="text">
								</span>
					        </p>
						</s:if><s:else><input id="counterCode" name="counterCode" value="<s:property value='counterCode'/>" class="hide"></s:else>
					</div>
				</div>
				<p class="clearfix">
					<%--查询--%>
					<button class="right search" id="searchButton">
						<span class="ui-icon icon-search-big"></span><span
							class="button-text"><s:text name="global.page.search" /></span>
					</button>
				</p>
			</cherry:form>
		</div>
		<div id="resultList" class="hide">
			<div class="section">
				<div class="section-header">
					<strong> <span
						class="ui-icon icon-ttl-section-search-result"></span> <s:text
							name="global.page.list" />
					</strong>
					<span class="right">
						<a href="#" class="setting">
						<span class="button-text"><s:text name="global.page.colSetting"/></span>
		 				<span class="ui-icon icon-setting"></span>
						</a>
					</span>
				</div>
				<div class="section-content">
					<div class="toolbar clearfix">
		  				<a id="cardExportExcel" class="export" href="javascript:void(0);">
          	  			<span class="ui-icon icon-export"></span>
          	  			<span class="button-text"><s:text name="SVC02_cardExportExcel"/></span>
          				</a>
	          			<a id="cardExportCsv" class="export" href="javascript:void(0);">
	          	  		<span class="ui-icon icon-export"></span>
	          	  		<span class="button-text"><s:text name="SVC02_cardExportCSV"/></span>
	          			</a>
	          			<%--
	          			<a id="saleExportExcel" class="export" href="javascript:void(0);">
          	  			<span class="ui-icon icon-export"></span>
          	  			<span class="button-text"><s:text name="SVC02_saleExportExcel"/></span>
          				</a>
	          			<a id="saleExportCsv" class="export" href="javascript:void(0);">
	          	  		<span class="ui-icon icon-export"></span>
	          	  		<span class="button-text"><s:text name="SVC02_saleExportCSV"/></span>
	          			</a>
	          			 --%>
		  			<span id="headInfo"></span>
		  			</div>
					<table id="resultCardDataTable" cellpadding="0" cellspacing="0"
						border="0" class="jquery_table" width="100%">
						<thead>
							<tr>
								<th><s:text name="SVC02_number" /></th>
								<th><s:text name="SVC02_cardNo" /></th>
								<th><s:text name="SVC02_obligateMobile" /></th>
								<th><s:text name="SVC02_amount" /></th>
								<th><s:text name="SVC02_depositAmount"></s:text></th>
								<th><s:text name="SVC02_totalAmount"></s:text></th>
								<th><s:text name="SVC02_lastDepositAmount"></s:text></th>
								<th><s:text name="SVC02_lastDepositTime"></s:text></th>
								<th><s:text name="SVC02_cardState" /></th>
								<th><s:text name="SVC02_departName" /></th>
								<th><s:text name="SVC02_cardStyle" /></th>
								<th><s:text name="SVC02_operator" /></th>
							</tr>
						</thead>
					</table>
				</div>
			</div>
		</div>
	</div>
	<!-- 停用卡DIV -->
	<div class="dialog2 clearfix" style="display: none"
		id="stop_card_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC02_stopCardMessage" /></span>
		</p>
	</div>
	<!-- 废弃储值卡卡DIV -->
	<div class="dialog2 clearfix" style="display: none"
		id="abandon_card_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC02_abandonMessage" /></span>
		</p>
	</div>
	<!-- 重置储值卡密码DIV -->
	<div class="dialog2 clearfix" style="display: none"
		id="reset_psd_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC02_resetPsdMessage" /></span>
		</p>
	</div>
	<!-- 修改储值卡密码DIV -->
	<div class="dialog2 clearfix" style="display: none"
		id="modify_card_dialog">
		<p class="clearfix">
		<p class="message">
			<span><s:text name="SVC02_modifyPsdMessage" /></span>
		</p>
	</div>
	<div id="dialogTitle" class="hide">
		<s:text name="SVC02_stopCardTitle" />
	</div>
	<div id="dialogResetPSDTitle" class="hide">
		<s:text name="SVC02_resetPSDTitle" />
	</div>
	<div id="dialogAbandonTitle" class="hide">
		<s:text name="SVC02_abandonTitle" />
	</div>
	<div id="dialogModifyTitle" class="hide">
		<s:text name="SVC02_modifyTitle" />
	</div>
	<div id="dialogConfirm" class="hide">
		<s:text name="SVC02_confirm" />
	</div>
	<div id="dialogCancel" class="hide">
		<s:text name="SVC02_cancel" />
	</div>
	<!-- 旧密码形式修改 -->
	<div id="byOldPSD" class="hide">
		<s:text name="SVC02_oldPassword" />
	</div>
	<!-- 验证号形式修改 -->
	<div id="byVerificationCode" class="hide">
		<s:text name="SVC02_verificationCode" />
	</div>
</s:i18n>

<div class="hide">
	<s:a action="BINOLMBSVC02_cardsearch" id="cardSearchUrl"></s:a>
	<s:a action="BINOLMBSVC02_cardExportExcel" id="cardExportExcelUrl"></s:a>
	<s:a action="BINOLMBSVC02_cardExportCsv" id="cardExportCsvUrl"></s:a>
	<s:a action="BINOLMBSVC02_cardExportCheck" id="cardExportCheckUrl"></s:a>
	
	<s:a action="BINOLMBSVC02_saleExportExcel" id="saleExportExcelUrl"></s:a>
	<s:a action="BINOLMBSVC02_saleExportCsv" id="saleExportCsvUrl"></s:a>
	<s:a action="BINOLMBSVC02_saleExportCheck" id="saleExportCheckUrl"></s:a>
	<s:a action="BINOLMBSVC02_sendMessageInit" id="sendMessageInitUrl"></s:a>
	<s:a action="BINOLMBSVC02_oldPasswordInit" id="oldPasswordInitUrl"></s:a>
</div>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp"
		flush="true" />
