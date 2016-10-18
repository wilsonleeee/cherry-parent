<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/bs/emp/BINOLBSEMP08.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<div class="hide">
	<s:url id="search_url" value="/basis/BINOLBSEMP08_search"/>
    <a id="searchUrl" href="${search_url}"></a>
</div>
<s:i18n name="i18n.bs.BINOLBSEMP08">
	<form id="mainForm">
		<div class="panel ui-corner-all">
			<div id="div_main">
				<div class="panel-header">
					<div class="clearfix"> 
						<span class="breadcrumb left">	    
							<span class="ui-icon icon-breadcrumb"></span>
							<s:text name="EMP08_native1" />&nbsp;&gt;&nbsp;
							<s:text name="EMP08_native2" />
						</span>
					</div>
				</div>
				<div id="errorMessage"></div>
				<%-- ================== 错误信息提示 START ======================= --%>
				<div id="errorDiv2" style="display:none">
				    <div class="actionError">
				        <ul>
				            <li><span id="errorSpan2"></span></li>
				        </ul>
				    </div>
				</div>
				<%-- ================== 错误信息提示   END  ======================= --%>
 				<div id="actionResultDisplay"></div>
 				<div class="panel-content clearfix">
 					<div class="box">
 						<input type="hidden" value="<s:property value="batchCode"/>"  name="batchCode" />
 						<div class="box-header"> 
 							<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
 						</div>
 						<div class="box-content clearfix">
 							<div class="column" style="width:50%; height: auto;">
 								<p>
 									<!-- 一级代理商  -->
					               	<label style="width:80px;"><s:text name="EMP08_reseller1L" /></label>
					               	<input type="hidden" name="parentResellerCode" id="parentResellerCode"></input>
					                <input type="text" class="text" id="parentResellerName"></input>
				            	</p>
 								<p>
 									<!-- 二级代理商  -->
					               	<label style="width:80px;"><s:text name="EMP08_reseller2L" /></label>
					               	<input type="hidden" name="resellerCode" id="resellerCode"></input>
					                <input type="text" class="text" id="resellerName"></input>
				            	</p>
 							</div>
 							<div class="column last" style="width:49%; height: auto;">
				                <p>
				                  <label style="width:80px;"><s:text name="EMP08_synchFlag" /></label>
				                  <s:text name="global.page.all" id="selectAll"/>
				                  <s:select name="synchFlag" list="#application.CodeTable.getCodes('1312')"
				               				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}"/>
				                </p>
				                <p>
				                  <label style="width:80px;"><s:text name="EMP08_couponCode" /></label>
				                  <input name="couponCode" type="text" class="text" />
				                </p>
 							</div>
 						</div>
 						<p class="clearfix">
	 						<button class="right search" type="button" onclick="BINOLBSEMP08.search();return false;">
				         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="EMP08_search" /></span>
				            </button>
 						</p>
 					</div>
 					<div class="section hide" id="section">
 						<div class="section-header">
 							<strong>
				     			<span class="ui-icon icon-ttl-section-search-result"></span>
				     			<s:text name="global.page.list"/>
				    		</strong>
 						</div>
	 					<div class="section-content">
	 						<div class="toolbar clearfix"></div>
	 						<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
	 							<thead>
					                <tr>
					                  <th><s:text name="EMP08_number" /></th>
					                  <th class="center"><s:text name="EMP08_reseller1L" /></th>
					                  <th class="center"><s:text name="EMP08_reseller2L" /></th>
					                  <th class="center"><s:text name="EMP08_couponCode" /></th>
					                  <th class="center"><s:text name="EMP08_useFlag" /></th>
					                  <th class="center"><s:text name="EMP08_synchFlag" /></th>
					                  <th class="center"><s:text name="EMP08_operation" /></th>
					                </tr>
				                 </thead>
				                 <tbody></tbody>
	 						</table>
	 					</div>
 					</div>
 				</div>
			</div>
		</div>
		<div class="hide" id="dialogInit"></div>
		<div class="hide">
			<!-- 隐藏国际化文本内容 -->
			<div id="deleteBaCouponTitle"><s:text name="EMP08_deleteCoupon" /></div>
			<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
			<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
			<div id="dialogClose"><s:text name="global.page.close" /></div>
			<div id="dialogContent">
				<p class="message"><span><s:text name="EMP08_confirmDelete"></s:text></span>
			</div>
			<div id="saveSuccessTitle"><s:text name="EMP08_delete_success" /></div>
		</div>
	</form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<script type="text/javascript">
	$(document).ready(function() {
		BINOLBSEMP08.search();
		
		//一级代理商下拉框绑定
		var optionParent = {
				elementId:"parentResellerName",
				selectedCode:"parentResellerCode",
				selected:"name",
				showNum:20,
				flag:"1"
		};
		resellerInfoBinding(optionParent);
		
		// 二级代理商下拉框绑定
		var option = {
				elementId:"resellerName",
				selectedCode:"resellerCode",
				selected:"name",
				showNum:20,
				flag:"2",
				parentKey:"parentResellerCode"
		};
		resellerInfoBinding(option);
		
	});
</script>