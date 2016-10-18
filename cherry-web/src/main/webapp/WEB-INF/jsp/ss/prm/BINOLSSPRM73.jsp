<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM73.js?V=20160721"></script>
<s:i18n name="i18n.ss.BINOLSSPRM73">
<input type="hidden" id="checkMsg_0" value="<s:text name="checkMsg_0"/>"/>
<input type="hidden" id="checkMsg_201" value="<s:text name="checkMsg_201"/>"/>
<s:text id="selectAll" name="global.page.all"/>
	<div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
	</div>
	
	 <%-- ================== 错误信息提示 START ======================= --%>
		<div id="errorMessage"></div>
	  <%-- ================== 错误信息提示   END  ======================= --%>
	  
	 <div class="panel-content">
		<div class="box">
			<%-- form start --%>
        	<cherry:form id = "mainForm" class ="inline" >
	          	<%--查询条件 --%>
	            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong><input id ="ruleValidFlag" type="checkbox" value="1" name="ruleValidFlag">包含停用规则</div>
	            <div class="box-content clearfix">
	            	<div class="column" style="width:49%; height:100px;">
	                	<p>
	                  		<%-- 规则名称 --%>	
			               	<label style="width:80px;">规则名称</label>
			               	<s:textfield name="ruleName" cssClass="text" id="ruleName"/>
			               	<s:hidden name="ruleCode" id="ruleCode"></s:hidden>
	                	</p>
	                	<p>
	                  		<%-- 审核状态 --%>
							<label style="width:80px;">审核状态</label>
							<span class="text">
								<s:select name="status"  list='#application.CodeTable.getCodes("1380")' listKey="CodeKey" listValue="Value"
								headerKey="" headerValue="%{selectAll}" />
							</span>
	                	</p>
	                	
	              	</div>
	              	<s:if test='%{brandInfoId != null && !"".equals(brandInfoId)}'>
	              		<input type="hidden" name="brandInfoId" value='<s:property value="brandInfoId"/>'/>
	              	</s:if>
	              	<s:elseif test='%{brandList != null && !brandList.isEmpty()}'>
	              		<p>
	              			<s:select name="brandInfoId" list="brandList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>
	              		</p>
	              	</s:elseif>
	            	<div class="column last" style="width:50%; height:50px;">
	            		<p class="date">
	                  		<%--发券时间 --%>
	                  		<label style="width:80px;">发券时间</label>
	                  		<span>
	                  		<s:textfield name="sendStartTime" cssClass="date" ></s:textfield>
	                  		</span>
	                  		-
	                  		<span>
	                  		<%--结束日期 --%>
	                  		<s:textfield name="sendEndTime" cssClass="date" ></s:textfield>
	                  		</span>
	                	</p>
	              	</div>
	            </div>
	            <div class="clearfix">
		              	<button class="right search" onclick="binolssprm73.searchPointInfo();return false;">
		              		<span class="ui-icon icon-search-big"></span>
		              		<span class="button-text"><s:text name="global.page.search"/></span>
		              	</button>
	            </div>
         	</cherry:form>
         	<%-- form end --%>
		</div>
  <div class="section hide" id="couponRule">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="couponRuleDataTable">
      <thead>
        <tr>
          <th><s:text name="global.page.number"></s:text></th>
          <th>规则名称</th>
          <th>活动编码</th>
          <th>发券开始时间</th>
          <th>发券结束时间</th>
          <th>审核状态</th>
          <th>有效区分</th>
          <th><s:text name="os.navigation.operator"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
	</div>
	</div>
	<div class="dialog2 clearfix" style="display:none" id="check_coupon_dialog">
		<p class="clearfix message">
			<span></span>
			<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
		</p>
	</div>
</s:i18n>
<div class="hide" id="couponDialogInit"></div>
<div class="hide">
<s:url action="BINOLSSPRM73_search" id="couponRule_Url"></s:url>
<a href="${couponRule_Url }" id="couponRuleUrl"></a>
<s:url action="BINOLSSPRM73_couponBatch" id="couponBatch_Url"></s:url>
<a href="${couponBatch_Url }" id="couponBatchUrl"></a>
<s:url action="BINOLSSPRM73_batchInit" id="batchInit_Url"></s:url>
<a href="${batchInit_Url }" id="batchInitUrl"></a>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

	  