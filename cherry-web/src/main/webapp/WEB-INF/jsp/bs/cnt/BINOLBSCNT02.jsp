<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT02.js"></script>

<s:if test="%{counterInfo != null && !counterInfo.isEmpty()}">
<s:i18n name="i18n.bs.BINOLBSCNT01">
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="counter.counterManage"/> &gt; <s:text name="counter.counterDetail"/>
	       </span>
	    </div>
	  </div>
	  <div class="panel-content clearfix">
      	<div class="tabs">
      	  <ul>
            <li><a href="#tabs-1"><s:text name="counter.counterInfo"/></a></li><%-- 柜台信息 --%>
            <li><a href="#tabs-2"><s:text name="counter.counterEventInfo"/></a></li><%-- 柜台事件信息 --%>
            <cherry:show domId="BINOLBSCNT01SOL">
               <li><a href="#tabs-3"><s:text name="counter.counterProductSolution"/></a></li><%-- 柜台产品方案 --%>
            </cherry:show>
          </ul>
      	
          <cherry:form id="mainForm" method="post" class="inline" csrftoken="false" action="BINOLBSCNT03_init">
          <s:hidden name="counterInfoId" value="%{counterInfo.counterInfoId}"/>
          <s:hidden name="counterCode" value="%{counterInfo.counterCode}"/>
          <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
          <%-- ====================== 基本信息DIV开始 ===================== --%>
          <div id="tabs-1">
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th><s:text name="counter.counterCode"></s:text></th>
				      <td><span><s:property value="counterInfo.counterCode"/></span></td>
				      <th><s:text name="counter.counterKind"></s:text></th>
					  <td><span><s:property value='#application.CodeTable.getVal("1031", counterInfo.counterKind)' /></span></td>
				    </tr>
				    <tr>
				      <th><s:text name="counter.brandNameChinese"></s:text></th>
				      <td><span><s:property value="counterInfo.brandNameChinese"/></span></td>
				      <th><s:text name="counter.counterNameIF"></s:text></th>
				      <td><span><s:property value="counterInfo.counterNameIF"/></span></td>
				    </tr>
				    <tr>
				      <th><s:text name="counter.region"></s:text></th>
				      <td><span><s:property value="counterInfo.region"/></span></td>
				      <th><s:text name="counter.counterNameShort"></s:text></th>
				      <td><span><s:property value="counterInfo.counterNameShort"/></span></td>
				    </tr>
					<tr>
					  <th><s:text name="counter.province"></s:text></th>
				      <td><span><s:property value="counterInfo.province"/></span></td>
				      <th><s:text name="counter.nameForeign"></s:text></th>
				      <td><span><s:property value="counterInfo.nameForeign"/></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.city"></s:text></th>
					  <td><span><s:property value="counterInfo.city"/></span></td>
					  <th><s:text name="counter.telephone"></s:text></th>
				      <td><span><s:property value="counterInfo.counterTelephone"/></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.county"></s:text></th>
					  <td><span><s:property value="counterInfo.county"/></span></td>
					  <th><s:text name="counter.status"></s:text></th>
				      <td><span><s:property value='#application.CodeTable.getVal("1030", counterInfo.status)' /></span></td> 
					</tr>
					<tr>
					  <th><s:text name="counter.counterAddress"></s:text></th>
					  <td><span><s:property value="counterInfo.counterAddress"/></span></td>
					  <th><s:text name="counter.counterLevel"></s:text></th>
					  <td><span><s:property value='#application.CodeTable.getVal("1032", counterInfo.counterLevel)' /></span></td>
					</tr>
					<tr>
						<!-- 运营模式 -->
						<th><s:text name="counter.operateMode"></s:text></th>
						<td><span><s:property value='#application.CodeTable.getVal("1318",counterInfo.operateMode)'/></span></td>
						<!-- 所属系统 -->
						<th><s:text name="counter.belongFaction"></s:text></th>
						<td><span><s:property value='#application.CodeTable.getVal("1309",counterInfo.belongFaction)'/></span></td>
					</tr>
					<tr>
						<!-- 开票单位 -->
						<th><s:text name="counter.invoiceCompany"></s:text></th>
						<td><span><s:property value='#application.CodeTable.getVal("1319",counterInfo.invoiceCompany)'/></span></td>
						<!-- 员工数 -->
						<th><s:text name="counter.employeeNum"></s:text></th>
						<td><span><s:property value="counterInfo.employeeNum"/></span></td>
					</tr>
					<tr>	
					  <th><s:text name="counter.channelName"></s:text></th>
					  <td><span><s:property value="counterInfo.channelName"/></span></td>
					  <th><s:text name="counter.updateStatus"></s:text></th>
				      <td><span><s:property value='#application.CodeTable.getVal("1023", counterInfo.updateStatus)' /></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.mallName"></s:text></th>
					  <td><span><s:property value="counterInfo.mallName"/></span></td>
					  <th><s:text name="counter.counterCategory"></s:text></th>
					  <td><span><s:property value="counterInfo.counterCategory"/></span></td>
					  <!-- 
					  <th><s:text name="counter.counterSpace"></s:text></th>
					  <td><span><s:if test='%{counterInfo.counterSpace != null && counterInfo.counterSpace != ""}'><s:property value="counterInfo.counterSpace"/><s:text name="square_meters"></s:text></s:if></span></td>
					   -->
					</tr>
					<tr>
					  <th><s:text name="counter.resellerName"></s:text></th>
					  <td><span><s:property value="counterInfo.resellerName"/></span></td>
					  <th><s:text name="counter.resellerDepart"></s:text></th>
					  <td><span><s:property value="counterInfo.resellerDepartName"/></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.longitude"></s:text></th>
					  <td><span><s:property value="counterInfo.longitude"/></span></td>
					  <th><s:text name="counter.latitude"></s:text></th>
					  <td><span><s:property value="counterInfo.latitude"/></span></td>
					</tr>
					<tr>
						<%-- 到期日 --%>
						<th><s:text name="counter.expiringDate"></s:text></th>
						<td><span><s:property value="counterInfo.expiringDate"/></span></td>
						<%-- 是否有POS机 --%>
						<th><s:text name="counter.posFlag"></s:text></th>
						<td><span><s:property value='#application.CodeTable.getVal("1308", counterInfo.posFlag)' /></span></td>
					</tr>
					<%-- 柜台协同区分 --%>
					<s:if test="maintainPassWord || maintainCoutSynergy">
						<tr>
							<s:if test="maintainPassWord">
								<th><s:text name="counter.passWord"></s:text></th>
								<td><span><s:property value="counterInfo.passWord"/></span></td>
							</s:if>
							<s:if test="maintainCoutSynergy">
								<th><s:text name="counter.counterSynergyFlag"></s:text></th>
								<td>
									<span>
										<s:property value='#application.CodeTable.getVal("1331", counterInfo.counterSynergyFlag)' />
									</span>
								</td>
							</s:if>							
							<s:if test="!(maintainPassWord && maintainCoutSynergy)">
								<th></th>
								<td></td>
							</s:if>					
						</tr> 
					</s:if>
					
					<tr>
						<!-- 商圈 -->
						<th><s:text name="counter.busDistrict"></s:text></th>
						<td><span><s:property value="counterInfo.busDistrict"/></span></td>
						<!-- 业务负责人 -->
						<th><s:text name="counter.busniessPrincipal"></s:text></th>
						<td><span><s:property value="counterInfo.busniessPrincipal"/></span></td>
					</tr>
					<tr>
						<!-- 银联设备号 -->
						<th><s:text name="counter.equipmentCode"></s:text></th>
						<td><span><s:property value="counterInfo.equipmentCode"/></span></td>
						<!-- 柜台类型 -->
						<th><s:text name="counter.managingType2"></s:text></th>
						<td><span><s:property value='#application.CodeTable.getVal("1403",counterInfo.managingType2)'/></span></td>
					</tr>
                </table>
			  </div>
			</div>
			
			<%--=================== 上级部门 ===================== --%><%-- 
			<div class="section">
              <div class="section-header clearfix">
	       		<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="counter.higheOrg" />
            	</strong>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.departCode" /></th>
				      <th width="30%"><s:text name="counter.departName" /></th>
				      <th width="30%"><s:text name="counter.departType" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  	<s:if test="%{counterInfo.higherDepart != null}">
				    <s:set id="higherDepartMap" value="%{counterInfo.higherDepart}"></s:set>
				    <tr>
			  			<td><s:property value="#higherDepartMap.departCode"/></td>
			  			<td><s:property value="#higherDepartMap.departName"/></td>
			  			<td><s:property value='#application.CodeTable.getVal("1000", #higherDepartMap.type)' /></td>
		  			</tr>
		  			</s:if>
				  </tbody>
				</table>
			  </div>
			</div>--%>
          
			<%--=================== 柜台主管 ===================== --%>
			<div class="section">
              <div class="section-header clearfix">
              	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="counter.counterHeader" />
            	</strong>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.name" /></th>
				      <th width="30%"><s:text name="counter.depart" /></th>
				      <th width="30%"><s:text name="counter.postion" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  	<s:iterator value="%{counterInfo.employeeList}" id="employeeMap">
				  		<s:if test='%{#employeeMap.manageType == "1"}'>
				  			<tr>
				  			<td><s:property value="#employeeMap.employeeName"/></td>
				  			<td><s:property value="#employeeMap.departName"/></td>
				  			<td><s:property value="#employeeMap.categoryName"/></td>
				  			</tr>
				  		</s:if>
				  	</s:iterator>
				  </tbody>
				</table>
			  </div>
			</div>
            
			<%--=================== 关注该柜台的人 ===================== --%>
			<div class="section">
              <div class="section-header clearfix">
            	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info"></span>
            		<s:text name="counter.likeCounterEmpLabel" />
            	</strong>
              </div>
	          <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.name" /></th>
				      <th width="30%"><s:text name="counter.depart" /></th>
				      <th width="30%"><s:text name="counter.postion" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  	<s:iterator value="%{counterInfo.employeeList}" id="employeeMap">
				  		<s:if test='%{#employeeMap.manageType == "0"}'>
				  			<tr>
				  			<td><s:property value="#employeeMap.employeeName"/></td>
				  			<td><s:property value="#employeeMap.departName"/></td>
				  			<td><s:property value="#employeeMap.categoryName"/></td>
				  			</tr>
				  		</s:if>
				  	</s:iterator>
				  </tbody>
				</table>
		  	  </div>
			</div>
			
			<%--=================== 所属部门的员工 ===================== --%>
			<div class="section">
	            <div class="section-header clearfix">
	      		<strong class="left">
	          		<span class="ui-icon icon-ttl-section-info-edit"></span>
	          		<s:text name="counter.employeeInDepart" />
	          	</strong>
	            </div>
	            <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.employeeCode" /></th>
				      <th width="30%"><s:text name="counter.name" /></th>
				      <th width="30%"><s:text name="counter.postion" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  	<s:if test="%{counterInfo.employeeInDepartList != null}">
				  	<s:iterator value="counterInfo.employeeInDepartList" id="employeeInDepartMap">
				    <tr>
			  			<td><s:property value="#employeeInDepartMap.employeeCode"/></td>
			  			<td><s:property value="#employeeInDepartMap.employeeName"/></td>
			  			<td><s:property value="#employeeInDepartMap.categoryName" /></td>
		  			</tr>
		  			</s:iterator>
		  			</s:if>
				  </tbody>
				</table>
			  </div>
			</div>
	      
            <%-- 操作按钮 --%>
            <div id="editButton" class="center clearfix">
       			<cherry:show domId="BINOLBSCNT0104">
       			<button class="edit" onclick="binolbscnt02.editCounter();return false;">
            		<span class="ui-icon icon-edit-big"></span>
            		<span class="button-text"><s:text name="global.page.edit"/></span>
             	</button>
             	</cherry:show>
	            <button id="close" class="close" type="button"  onclick="binolbscnt02.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
          </div>
          <%-- ====================== 基本信息DIV结束 ===================== --%>
          </cherry:form>
        
        
          <div id="tabs-2">
          	<div class="center clearfix">
              <s:url action="BINOLBSCNT02_searchCouEvent" id="searchCouEventUrl"/>
              <a id="searchCouEventUrl" href="${searchCouEventUrl}"></a>
              <table id="couEventDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="counter.number"/></th>
                        <th><s:text name="counter.eventNameId"/></th>
                        <th><s:text name="counter.fromDate"/></th>
                    </tr>
                    </thead>
              </table>
              
              <button id="close" class="close" type="button"  onclick="binolbscnt02.close();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          	  </button>
          	  
            </div>
          </div>
          
          
          <%--产品方案 --%>
          <div id="tabs-3">
          	<div class="center clearfix">
                 <cherry:show domId="BINOLBSCNT01SOL">
              <s:url action="BINOLBSCNT02_searchCouSolution" id="searchCouSolutionUrl"/>
              <a id="searchCouSolutionUrl" href="${searchCouSolutionUrl}"></a>
                 <table id="couSolutionDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                    <thead>
                    <tr>
                        <th><s:text name="counter.number"/></th>
                        <th><s:text name="counter.counterSolutionCode"/></th>
                        <th><s:text name="counter.counterSolutionName"/></th>
	                    <th><s:text name="counter.solu_startDate"/></th><%-- 方案价格生效日 --%>
		                <th><s:text name="counter.solu_endDate"/></th><%-- 方案价格失效日期 --%>
                        <th><s:text name="counter.validFlag"/></th>

                    </tr>
                    </thead>
                 </table>
	              <button id="close" class="close" type="button"  onclick="binolbscnt02.close();return false;">
	            	<span class="ui-icon icon-close"></span>
	            	<span class="button-text"><s:text name="global.page.close"/></span>
	          	  </button>
          	     </cherry:show>
            </div>
          </div>
          
        </div>
      </div>
</div>
</div>
</s:i18n>
</s:if>
<s:else>
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true" />
</s:else>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>   
