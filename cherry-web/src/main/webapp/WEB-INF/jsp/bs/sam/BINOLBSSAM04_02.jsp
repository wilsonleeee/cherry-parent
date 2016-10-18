<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM04.js"></script>
<%-- 银联对账URL --%>
<div class="hide">
	<!-- 修改 -->
	<s:url id="s_dgupdateSalesBonusRate" value="/basis/BINOLBSSAM04_updateSalesBonusRate"></s:url>
	<a id="updateSalesBonusRateUrl" href="${s_dgupdateSalesBonusRate}"></a>
	<!-- 添加 -->
	<s:url id="s_dgAddSalesBonusRate" value="/basis/BINOLBSSAM04_addSalesBonusRate"></s:url>
	<a id="addSalesBonusRateUrl" href="${s_dgAddSalesBonusRate}"></a>
</div>
<s:i18n name="i18n.bs.BINOLBSSAM04">
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
	<div id="div_main">
	    <div class="panel-header">
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<span class="ui-icon icon-breadcrumb"></span><s:text name="BSSAM04_native1" />&nbsp;&gt;&nbsp;<s:text name="BSSAM04_native2"></s:text>
				</span>
	        </div>
	    </div>
	    <form id="mainForm">
	    <div class="panel-content">
	    	<div class="box2 box2-active">
				<div class="box2-header clearfix">
					<strong class="active left">
						<span class="ui-icon icon-ttl-section-info"></span>
						<s:if test="editSaleMap==null"><s:text name="BSSAM04_add"/></s:if>
						<s:else><s:text name="BSSAM04_edit"/></s:else>
					</strong>
				</div>
				<div class="box2-content clearfix">
					 <table class="detail" cellpadding="0" cellspacing="0">
						<tr>
							<th><s:text name="BSSAM04_bonusEmployeePosition"></s:text></th>
							<td>
								<span id="bonusEmployeePositionSpan">
								<s:select headerKey="" headerValue="%{global.select}" cssClass="text" cssStyle="width:185px;" id="bonusEmployeePosition" name="bonusEmployeePosition" list="positionCategoryList" listKey="BIN_PositionCategoryID" listValue="CategoryName" value="%{editSaleMap.bonusEmployeePosition}"></s:select>
								</span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="BSSAM04_saleEmployeePosition"></s:text></th>
							<td>
								<span id="saleEmployeePositionSpan">
								<s:select headerKey="" headerValue="%{global.select}" cssClass="text" cssStyle="width:185px;" id="saleEmployeePosition" name="saleEmployeePosition" list="positionCategoryList" listKey="BIN_PositionCategoryID" listValue="CategoryName" value="%{editSaleMap.saleEmployeePosition}"></s:select>
								</span>
								<span class="highlight">*</span>
							</td>
						</tr>
						<tr>
							<th><s:text name="BSSAM04_counterCode"></s:text></th>
							<td>
								<span>
								<input id="counterCode" name="counterCode" class="text" value="${editSaleMap.counterCode}">
								</span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="BSSAM04_beginAmount"></s:text></th>
							<td>
								<span id="beginAmountSpan"><s:textfield onkeyup="BINOLBSSAM04.checkNumber()" name="beginAmount" id="beginAmount" cssClass="text" value="%{editSaleMap.beginAmount}"/></span>
								<span class="highlight">*</span>
							</td>
						</tr>
						 <tr>
						 	<th><s:text name="BSSAM04_endAmount"></s:text></th>
							<td>
								<span id="endAmountSpan"><s:textfield onkeyup="BINOLBSSAM04.checkNumber()" name="endAmount" id="endAmount" cssClass="text" value="%{editSaleMap.endAmount}"/></span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="BSSAM04_bonusRate"></s:text></th>
							<td>
								<span id="bonusRateSpan"><s:textfield onkeyup="BINOLBSSAM04.checkNumber()" name="bonusRate" id="bonusRate" cssClass="text" value="%{editSaleMap.bonusRate}"/></span>
								<span class="highlight">*</span>
							</td>
						</tr>
						<tr>
							<th><s:text name="BSSAM04_memo"></s:text></th>
							<td colspan="3">
								<span id="memoSpan"><s:textarea cssStyle="width:500px;" name="memo" id="memo" value="%{editSaleMap.memo}"/></span>
							</td>
						</tr>
					</table>    
				</div>
			</div>
			<s:textfield  cssClass="hide" name="recordId" value="%{editSaleMap.recordId}"/>
		     <div class="center clearfix" id="closeButton">
		     	<s:if test="editSaleMap==null">
			     	<button onclick="BINOLBSSAM04.addSalesBonusRate();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-confirm"></span>
		           		<span class="button-text"><s:text name="global.page.ok"/></span>
		         	</button>
	         	</s:if>
	         	<s:else>
		         	<button onclick="BINOLBSSAM04.updateSalesBonusRate();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-confirm"></span>
		           		<span class="button-text"><s:text name="global.page.ok"/></span>
		         	</button>
	         	</s:else>
	            <button onclick="BINOLBSSAM04.close();return false;" type="button" class="close" id="close">
	           		<span class="ui-icon icon-cancle"></span>
	           		<span class="button-text"><s:text name="global.page.cancle"/></span>
	         	</button>
		    </div>
	    </div>
	    </form>
	</div>
</div>
<div class="hide" id="messageDialogTitle"><s:text name="BSSAM04_message"></s:text></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="BSSAM04_confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>
<script>
$(function(){
	// 锁定父界面
	window.opener.lockParentWindow();
});
</script>