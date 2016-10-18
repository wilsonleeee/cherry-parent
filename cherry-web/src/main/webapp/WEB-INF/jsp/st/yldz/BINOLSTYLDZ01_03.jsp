<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/st/yldz/BINOLSTYLDZ01.js"></script>
<%-- 银联对账URL --%>
<div class="hide">
	<!-- 修改 -->
	<s:url id="s_dgupdateBankBill" value="/st/BINOLSTYLDZ01_updateBankBill"></s:url>
	<a id="updateBankBillUrl" href="${s_dgupdateBankBill}"></a>
	<!-- 添加 -->
	<s:url id="s_dgAddBankBill" value="/st/BINOLSTYLDZ01_addBankBill"></s:url>
	<a id="addBankBillUrl" href="${s_dgAddBankBill}"></a>
</div>
<s:i18n name="i18n.st.BINOLSTYLDZ01">
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
	<div id="div_main">
	    <div class="panel-header">
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<span class="ui-icon icon-breadcrumb"></span><s:text name="styldz01.native1" />&nbsp;&gt;&nbsp;<s:text name="styldz01.native2"></s:text>
				</span>
	        </div>
	    </div>
	    <form id="mainForm">
	    <div class="panel-content">
	    	<div class="box2 box2-active">
				<div class="box2-header clearfix">
					<strong class="active left">
						<span class="ui-icon icon-ttl-section-info"></span>
						<s:if test="editSaleMap==null"><s:text name="styldz01.add"/></s:if>
						<s:else><s:text name="styldz01.edit"/></s:else>
					</strong>
				</div>
				<div class="box2-content clearfix">
					 <table class="detail" cellpadding="0" cellspacing="0">
						<tr>
							<th><s:text name="styldz01.tradeDate"/></th>
							<td>
								<span id="tradeDateSpan"><s:textfield name="tradeDate" id="tradeDate" maxlength="50" cssClass="date" value="%{editSaleMap.tradeDate}"/></span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="styldz01.tradeTime"/></th>
							<td>
								<span id="TradeTimeSpan"><s:textfield name="tradeTime" id="tradeTime" maxlength="50" cssClass="text"  value="%{editSaleMap.tradeTime}"/></span>
								<span class="highlight"><s:text name="styldz01_timeStyle"></s:text></span>
							</td>
						</tr>
						<tr>
							<th><s:text name="styldz01.sysBillCode"/></th>
							<td>
								<span><s:textfield name="sysBillCode" id="sysBillCode" cssClass="text" value="%{editSaleMap.sysBillCode}"/></span>
							</td>
							<th><s:text name="styldz01.cardCode"/></th>
							<td>
								<span id="cardCodeSpan"><s:textfield name="cardCode" id="cardCode" cssClass="text" value="%{editSaleMap.cardCode}"/></span>
							</td>
						</tr>
						 <tr>
						 	<th><s:text name="styldz01.companyCode"/></th>
							<td>
								<span id="companyCodeSpan"><s:textfield name="companyCode" id="companyCode" maxlength="50" cssClass="text" value="%{editSaleMap.companyCode}"/></span>
							</td>
							<th><s:text name="styldz01.companyName"/></th>
							<td>
								<span id="companyNameSpan"><s:textfield name="companyName" id="companyName" cssClass="text" value="%{editSaleMap.companyName}"/></span>
							</td>
						</tr>
						<tr>
						 	<th><s:text name="styldz01.posCode"/></th>
							<td>
								<span id="posCodeSpan"><s:textfield name="posCode" id="posCode" maxlength="50" cssClass="text" value="%{editSaleMap.posCode}"/></span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="styldz01.posBillCode"/></th>
							<td>
								<span id="posBillCodeSpan"><s:textfield name="posBillCode" id="posBillCode" cssClass="text" value="%{editSaleMap.posBillCode}"/></span>
								<span class="highlight">*</span>
							</td>
						</tr>
						<tr>
						 	<th><s:text name="styldz01.hedgingBillCode"/></th>
							<td>
								<span id="hedgingBillCodeSpan"><s:textfield name="hedgingBillCode" id="hedgingBillCode" maxlength="50" cssClass="text" value="%{editSaleMap.hedgingBillCode}"/></span>
							</td>
							<th><s:text name="styldz01.referenceCode"/></th>
							<td>
								<span id="referenceCodeSpan"><s:textfield name="referenceCode" id="referenceCode" cssClass="text" value="%{editSaleMap.referenceCode}"/></span>
							</td>
						</tr>
						<tr>
						 	<th><s:text name="styldz01.amount"/></th>
							<td>
								<span id="amountSpan"><s:textfield name="amount" id="amount" maxlength="50" cssClass="text" value="%{editSaleMap.amount}"/></span>
							</td>
							<th><s:text name="styldz01.tradeType"/></th>
							<td>
								<span id="tradeTypeSpan"><s:textfield name="tradeType" id="tradeType" cssClass="text" value="%{editSaleMap.tradeType}"/></span>
							</td>
						</tr>
						<tr>
						 	<th><s:text name="styldz01.tradeResult"/></th>
							<td>
								<span id="tradeResultSpan"><s:textfield name="tradeResult" id="tradeResult" maxlength="50" cssClass="text" value="%{editSaleMap.tradeResult}"/></span>
							</td>
							<th><s:text name="styldz01.tradeAnswer"/></th>
							<td>
								<span id="tradeAnswerSpan"><s:textfield name="tradeAnswer" id="tradeAnswer" cssClass="text" value="%{editSaleMap.tradeAnswer}"/></span>
							</td>
						</tr>
						<tr class="hide">
							<%-- <th><s:text name="styldz01.saleCard"/></th>
							<td>
								<span id="cardCodeSpan"><s:textfield name="cardCode" id="cardCode" cssClass="text" value="%{editSaleMap.cardCode}"/></span>
							</td> --%>
							<td rowspan="4" class="hide"><s:textfield  cssClass="text" name="billId" value="%{editSaleMap.billId}"/>
							</td>
						</tr>
					</table>    
				</div>
			</div>
		     <div class="center clearfix" id="closeButton">
		     	<s:if test="editSaleMap==null">
			     	<button onclick="BINOLSTYLDZ01.addBankBill();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-confirm"></span>
		           		<span class="button-text"><s:text name="global.page.ok"/></span>
		         	</button>
	         	</s:if>
	         	<s:else>
		         	<button onclick="BINOLSTYLDZ01.updateBankBill();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-confirm"></span>
		           		<span class="button-text"><s:text name="global.page.ok"/></span>
		         	</button>
	         	</s:else>
	            <button onclick="BINOLSTYLDZ01.close();return false;" type="button" class="close" id="close">
	           		<span class="ui-icon icon-cancle"></span>
	           		<span class="button-text"><s:text name="global.page.cancle"/></span>
	         	</button>
		    </div>
	    </div>
	    </form>
	   	<%-- 错误信息 --%>
	    <div id="errMsg">
	    	<%-- 请选择上传文件。 --%>
	    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/>
	    	<input type="hidden" id="errmsg2" value='<s:text name="styldz01.error1"/>'/>
	    	<input type="hidden" id="errmsg3" value='<s:text name="styldz01.error2"/>'/>
	    	<input type="hidden" id="errmsg4" value='<s:text name="styldz01.error3"/>'/>
	    </div>
	</div>
</div>
<div class="hide" id="messageDialogTitle"><s:text name="styldz01.message"></s:text></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="styldz01.confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>
