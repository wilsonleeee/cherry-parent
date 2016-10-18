<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM05.js"></script>
<%-- 银联对账URL --%>
<div class="hide">
	<!-- 修改 -->
	<s:url id="s_dgupdateSalesBonusRate" value="/basis/BINOLBSSAM05_updateSalesBonusRate"></s:url>
	<a id="updateSalesBonusRateUrl" href="${s_dgupdateSalesBonusRate}"></a>
	<!-- 添加 -->
	<s:url id="s_dgAddSalesBonusRate" value="/basis/BINOLBSSAM05_addSalesBonusRate"></s:url>
	<a id="addSalesBonusRateUrl" href="${s_dgAddSalesBonusRate}"></a>
</div>
<s:i18n name="i18n.bs.BINOLBSSAM05">
<s:text name="global.page.select" id="select_default"/>
<div class="panel ui-corner-all">
	<div id="div_main">
	    <div class="panel-header">
	        <div class="clearfix"> 
				<span class="breadcrumb left">	    
					<span class="ui-icon icon-breadcrumb"></span><s:text name="BSSAM05_native1" />&nbsp;&gt;&nbsp;<s:text name="BSSAM05_native2"></s:text>
				</span>
	        </div>
	    </div>
	    <form id="mainForm">
	    <div class="panel-content">
	    	<div class="box2 box2-active">
				<div class="box2-header clearfix">
					<strong class="active left">
						<span class="ui-icon icon-ttl-section-info"></span>
						<s:if test="editSaleMap==null"><s:text name="BSSAM05_add"/></s:if>
						<s:else><s:text name="BSSAM05_edit"/></s:else>
					</strong>
				</div>
				<div class="box2-content clearfix">
					 <table class="detail" cellpadding="0" cellspacing="0">
						<tr>
							<th><s:text name="BSSAM05_employeeID"></s:text></th>
							<td>
								<s:if test="editSaleMap==null">
									<span id="employeeIDSpan">
										<%-- <s:textfield name="employeeID" id="employeeID" cssClass="text" value="%{editSaleMap.employeeID}"/> --%>
										<input id="employeeID" name="employeeID" class="text" value="${editSaleMap.employeeID}">
									</span>
									<span class="highlight">*</span>
								</s:if><s:else>
									<span id="employeeIDSpan">${editSaleMap.employeeID}</span>
								</s:else>
							</td>
							<th><s:text name="BSSAM05_assessmentEmployee"></s:text></th>
							<td>
								<%-- <span id="assessmentEmployeeSpan"><s:textfield name="assessmentEmployee" id="assessmentEmployee" cssClass="text" value="%{editSaleMap.assessmentEmployee}"/></span> --%>
								<s:if test="editSaleMap==null">
									<%-- <span id="assessmentEmployeeSpan"><s:textfield name="assessmentEmployee" id="assessmentEmployee" cssClass="text" value="%{editSaleMap.assessmentEmployee}"/></span> --%>
									<span id="assessmentEmployeeSpan"><input id="assessmentEmployee" name="assessmentEmployee" class="text"></span>
									<span class="highlight">*</span>
								</s:if><s:else>
									<span id="assessmentEmployeeSpan">${editSaleMap.assessmentEmployee}</span>
								</s:else>
							</td>
						</tr>
						<tr>
							<th><s:text name="BSSAM05_assessmentYear"></s:text></th>
							<td>
								<span id="assessmentYearSpan"><s:textfield onkeyup="BINOLBSSAM05.checkNumber(this)" maxlength="4" name="assessmentYear" id="assessmentYear" cssClass="text" value="%{editSaleMap.assessmentYear}"/></span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="BSSAM05_assessmentMonth"></s:text></th>
							<td>
								<span id="assessmentMonthSpan"><s:textfield onkeyup="BINOLBSSAM05.checkNumber(this)" maxlength="2" name="assessmentMonth" id="assessmentMonth" cssClass="text" value="%{editSaleMap.assessmentMonth}"/></span>
								<span class="highlight">*</span>
							</td>
						</tr>
						 <tr>
						 	<th><s:text name="BSSAM05_score"></s:text></th>
							<td>
								<span id="scoreSpan"><s:textfield onkeyup="BINOLBSSAM05.checkNumber(this)" name="score" id="score" cssClass="text" value="%{editSaleMap.score}"/></span>
								<span class="highlight">*</span>
							</td>
							<th><s:text name="BSSAM05_assessmentDate"></s:text></th>
							<td>
								<span id="assessmentDateSpan"><s:textfield name="assessmentDate" id="assessmentDate" cssClass="date" value="%{editSaleMap.assessmentDate}"/></span>
								<span class="highlight">*</span>
							</td>
						</tr>
						<tr>
							<th><s:text name="BSSAM05_memo"></s:text></th>
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
			     	<button onclick="BINOLBSSAM05.addSalesBonusRate();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-confirm"></span>
		           		<span class="button-text"><s:text name="global.page.ok"/></span>
		         	</button>
	         	</s:if>
	         	<s:else>
		         	<button onclick="BINOLBSSAM05.updateSalesBonusRate();return false;" type="button" class="close" id="close">
		           		<span class="ui-icon icon-confirm"></span>
		           		<span class="button-text"><s:text name="global.page.ok"/></span>
		         	</button>
	         	</s:else>
	            <button onclick="BINOLBSSAM05.close();return false;" type="button" class="close" id="close">
	           		<span class="ui-icon icon-cancle"></span>
	           		<span class="button-text"><s:text name="global.page.cancle"/></span>
	         	</button>
		    </div>
	    </div>
	    </form>
	</div>
</div>
<div class="hide" id="messageDialogTitle"><s:text name="BSSAM05_message"></s:text></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="BSSAM05_confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>
 <script type="text/javascript">
	 	$(document).ready(function() {
	 		window.opener.lockParentWindow();
	 		$('#assessmentDate').cherryDate({
	 			beforeShow: function(input){}
	 		});
	 		// 员工下拉框绑定
	 		var option = {
	 			elementId:"employeeID",
	 			showNum:20,
	 			selected:"code"
	 		};
	 		employeeBinding(option);
	 		var option = {
	 			elementId:"assessmentEmployee",
	 			showNum:20,
	 			selected:"code"
	 		};
	 		employeeBinding(option);
	 		// 表单验证配置
	 		cherryValidate({						
	 			formId: 'mainForm',
	 			rules: {
	 				saleDateStart: {dateValid: true}
	 			}
	 		});
		});
 </script>