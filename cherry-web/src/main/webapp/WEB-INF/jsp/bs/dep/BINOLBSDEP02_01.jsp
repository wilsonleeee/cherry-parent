<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>

<script type="text/javascript">
$(function(){
	
	oTableArr = new Array(null,null);
    fixedColArr = new Array(null,null);
	
	$('.tabs').tabs();
	$("a@[href='#tabs-2']").click(function() {
		searchCouEvent();
    });
});

function searchCouEvent() {
	
	var url = $("#searchCouEventUrl").attr("href");
    var params = getSerializeToken() + "&" + $("#counterInfoId").serialize();
    url = url + "?" + params;
    // 表格设置
    var tableSetting = {
            // 表格ID
            tableId : '#couEventDataTable',
            // 数据URL
            url : url,
            // 表格默认排序
            aaSorting : [[ 2, "desc" ]],
            // 表格列属性设置
            aoColumns : [  { "sName": "no","bSortable": false,"sWidth": "1%"},
                           { "sName": "eventNameId","sWidth": "49%"},
                           { "sName": "fromDate","sWidth": "50%"}
                       ],
                           
           // 不可设置显示或隐藏的列  
           //aiExclude :[0, 1],
           // 横向滚动条出现的临界宽度
           sScrollX : "100%",
           index: 2
           // 固定列数
           //fixedColumns : 2
    };
    // 调用获取表格函数
    getTable(tableSetting);
	
}
</script>

<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>

<s:i18n name="i18n.bs.BINOLBSDEP01">


  
<s:if test="%{organizationInfo == null}">
    
  <div class="tabs">
     <ul>
	     <li><a href="#tabs-1"><s:text name="counter.counterInfo"/></a></li><%-- 柜台信息 --%>
	     <li><a href="#tabs-2"><s:text name="counter.counterEventInfo"/></a></li><%-- 柜台事件信息 --%>
     </ul>
    
    
    <div id="tabs-1">
    
    <div class="section">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
    
    <div class="section-content" id="counterInfo">
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
			  <th><s:text name="counter.nameShortForeign"></s:text></th>
		      <td><span><s:property value="counterInfo.nameShortForeign"/></span></td>
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
			  <th><s:text name="counter.expiringDate"></s:text></th>
			  <td><span><s:property value="counterInfo.expiringDate"/></span></td>
			  <%-- 柜台协同区分 --%>
				<s:if test="maintainCoutSynergy">
					<th><s:text name="counter.counterSynergyFlag"></s:text></th>
					<td><span><s:property value='#application.CodeTable.getVal("1331", counterInfo.counterSynergyFlag)' /></span></td>
				</s:if>
				<s:else>					
					<th></th>
					<td></td>
				</s:else>
			</tr>
		</table>
    </div>
    </div>
  
  <%--=================== 上级部门 ===================== --%><%-- 
	<div class="section">
            <div class="section-header clearfix">
      		<strong class="left">
          		<span class="ui-icon icon-ttl-section-info-edit"></span>
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
          		<span class="ui-icon icon-ttl-section-info-edit"></span>
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
		  			<td>
		  			<s:if test="%{modeFlg != null}">
		  			<s:url action="BINOLBSEMP02_init" id="employeeDetailUrl">
						<s:param name="employeeId" value="%{#employeeMap.employeeId}"></s:param>
					</s:url>
		  			<a href="${employeeDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
		  			<s:property value="#employeeMap.employeeName"/>
		  			</a>
		  			</s:if>
		  			<s:else>
		  			<s:property value="#employeeMap.employeeName"/>
		  			</s:else>
		  			</td>
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
          		<span class="ui-icon icon-ttl-section-info-edit"></span>
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
		  			<td>
		  			<s:if test="%{modeFlg != null}">
		  			<s:url action="BINOLBSEMP02_init" id="employeeDetailUrl">
						<s:param name="employeeId" value="%{#employeeMap.employeeId}"></s:param>
					</s:url>
		  			<a href="${employeeDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
		  			<s:property value="#employeeMap.employeeName"/>
		  			</a>
		  			</s:if>
		  			<s:else>
		  			<s:property value="#employeeMap.employeeName"/>
		  			</s:else>
		  			</td>
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
          		<s:text name="employeeInDepart" />
          	</strong>
            </div>
            <div class="section-content">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		  <thead>
		    <tr>
		      <th width="30%"><s:text name="employee.employeeCode" /></th>
		      <th width="30%"><s:text name="employee.employeeName" /></th>
		      <th width="30%"><s:text name="employee.post" /></th>
		    </tr>
		  </thead>
		  <tbody>
		  	<s:if test="%{counterInfo.employeeInDepartList != null}">
		  	<s:iterator value="counterInfo.employeeInDepartList" id="employeeInDepartMap">
		    <tr>
	  			<td>
	  			  <s:if test="%{modeFlg != null}">
	  			  <s:url action="BINOLBSEMP02_init" id="employeeDetailUrl">
					<s:param name="employeeId" value="%{#employeeInDepartMap.employeeId}"></s:param>
				  </s:url>
	  			  <a href="${employeeDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
	  				<s:property value="#employeeInDepartMap.employeeCode"/>
	  			  </a>
	  			  </s:if>
	  			  <s:else>
	  			    <s:property value="#employeeInDepartMap.employeeCode"/>
	  			  </s:else>
	  			</td>
	  			<td><s:property value="#employeeInDepartMap.employeeName"/></td>
	  			<td><s:property value="#employeeInDepartMap.categoryName" /></td>
  			</tr>
  			</s:iterator>
  			</s:if>
		  </tbody>
		</table>
	  </div>
	</div>
	
	</div>
	
	
	<div id="tabs-2">
      <div class="center clearfix">
        <s:url action="BINOLBSCNT02_searchCouEvent" id="searchCouEventUrl"/>
        <a id="searchCouEventUrl" href="${searchCouEventUrl}"></a>
        <s:hidden name="counterInfoId" value="%{counterInfo.counterInfoId}"/>
        <table id="couEventDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
              <tr>
                  <th><s:text name="counter.number"/></th>
                  <th><s:text name="counter.eventNameId"/></th>
                  <th><s:text name="counter.fromDate"/></th>
              </tr>
              </thead>
        </table> 
      </div>
    </div>
	
	</div>
</s:if>		
<s:else>
	
	<div class="section">
    <div class="section-header">
    <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="base_title"></s:text></strong>
    </div>
	
	<div class="section-content">
      <table class="detail" cellpadding="0" cellspacing="0">
        <tr>
          <th><s:text name="departCode"></s:text></th>
          <td><span><s:property value="organizationInfo.departCode"/></span></td>
          <th><s:text name="testType"></s:text></th>
          <td><span>
            <s:if test="%{organizationInfo.testType != null && organizationInfo.testType == 0}"><s:text name="testType0"></s:text></s:if>
            <s:if test="%{organizationInfo.testType != null && organizationInfo.testType == 1}"><s:text name="testType1"></s:text></s:if>
          </span></td>
        </tr>
        <tr>
          <th><s:text name="type"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1000", organizationInfo.type)' /></span></td>
          <th><s:text name="departName"></s:text></th>
          <td><span><s:property value="organizationInfo.departName"/></span></td>
        </tr>
        <tr>
          <th><s:text name="brandInfo"></s:text></th>
          <td><span>
            <s:if test="%{organizationInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
		    <s:else><s:property value="organizationInfo.brandNameChinese"/></s:else>
          </span></td>
          <th><s:text name="departNameShort"></s:text></th>
          <td><span><s:property value="organizationInfo.departNameShort"/></span></td>
        </tr>
        <tr>
          <th><s:text name="regionName"></s:text></th>
          <td><span><s:property value="organizationInfo.regionName"/></span></td>
          <th><s:text name="nameForeign"></s:text></th>
          <td><span><s:property value="organizationInfo.nameForeign"/></span></td>
        </tr>
        <tr>
          <th><s:text name="provinceName"></s:text></th>
          <td><span><s:property value="organizationInfo.provinceName"/></span></td>
          <th><s:text name="nameShortForeign"></s:text></th>
          <td><span><s:property value="organizationInfo.nameShortForeign"/></span></td>
        </tr>
        <tr>
          <th><s:text name="cityName"></s:text></th>
          <td><span><s:property value="organizationInfo.cityName"/></span></td>
          <th><s:text name="status"></s:text></th>
          <td><span><s:property value='#application.CodeTable.getVal("1030", organizationInfo.status)' /></span></td> 
        </tr>
        <tr>
          <th><s:text name="countyName"></s:text></th>
          <td><span><s:property value="organizationInfo.countyName"/></span></td>
          <th><s:text name="counter.expiringDate"></s:text></th>
          <td><span><s:property value="organizationInfo.expiringDate"/></span></td>
        </tr>
        
        <%-- 柜台协同区分 --%>
        <s:if test="maintainOrgSynergy">
	        <tr>
	          	<th><s:text name="depart.orgSynergyFlag"></s:text></th>
				<td><span><s:property value='#application.CodeTable.getVal("1331", organizationInfo.orgSynergyFlag)' /></span></td>					
				<th></th>
				<td></td>
	        </tr>
        </s:if>
      </table>     
    </div>
    
    </div>

<%--=================== 上级部门 ===================== --%>
	<div class="section">
            <div class="section-header clearfix">
      		<strong class="left">
          		<span class="ui-icon icon-ttl-section-info-edit"></span>
          		<s:text name="higherOrganization" />
          	</strong>
            </div>
            <div class="section-content">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		  <thead>
		    <tr>
		      <th width="30%"><s:text name="departCode" /></th>
		      <th width="30%"><s:text name="departName" /></th>
		      <th width="30%"><s:text name="type" /></th>
		    </tr>
		  </thead>
		  <tbody>
		  	<s:if test="%{organizationInfo.higherDepartPath != null}">
		    <tr>
	  			<td><s:property value="organizationInfo.higherDepartCode"/></td>
	  			<td><s:property value="organizationInfo.higherDepartName"/></td>
	  			<td><s:property value='#application.CodeTable.getVal("1000", organizationInfo.higherType)' /></td>
  			</tr>
  			</s:if>
		  </tbody>
		</table>
	  </div>
	</div>
	<%--=================== 管辖该部门的人 ===================== --%>
	<div class="section">
            <div class="section-header clearfix">
          	<strong class="left">
          		<span class="ui-icon icon-ttl-section-info-edit"></span>
          		<s:text name="counter.followDepartEmpLabel" />
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
		  	<s:iterator value="employeeList" id="employeeMap">
		  		<s:if test='%{#employeeMap.manageType == "1"}'>
		  			<tr>
		  			<td>
		  			<s:if test="%{modeFlg != null}">
		  			<s:url action="BINOLBSEMP02_init" id="employeeDetailUrl">
						<s:param name="employeeId" value="%{#employeeMap.employeeId}"></s:param>
					</s:url>
		  			<a href="${employeeDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
		  			<s:property value="#employeeMap.employeeName"/>
		  			</a>
		  			</s:if>
		  			<s:else>
		  			<s:property value="#employeeMap.employeeName"/>
		  			</s:else>
		  			</td>
		  			<td><s:property value="#employeeMap.departName"/></td>
		  			<td><s:property value="#employeeMap.categoryName"/></td>
		  			</tr>
		  		</s:if>
		  	</s:iterator>
		  </tbody>
		</table>
  	  </div>
	</div>
	<%--=================== 关注该部门的人 ===================== --%>
	<div class="section">
            <div class="section-header clearfix">
          	<strong class="left">
          		<span class="ui-icon icon-ttl-section-info-edit"></span>
          		<s:text name="counter.likeDepartEmpLabel" />
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
		  	<s:iterator value="employeeList" id="employeeMap">
		  		<s:if test='%{#employeeMap.manageType == "0"}'>
		  			<tr>
		  			<td>
		  			<s:if test="%{modeFlg != null}">
		  			<s:url action="BINOLBSEMP02_init" id="employeeDetailUrl">
						<s:param name="employeeId" value="%{#employeeMap.employeeId}"></s:param>
					</s:url>
		  			<a href="${employeeDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
		  			<s:property value="#employeeMap.employeeName"/>
		  			</a>
		  			</s:if>
		  			<s:else>
		  			<s:property value="#employeeMap.employeeName"/>
		  			</s:else>
		  			</td>
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
          		<s:text name="employeeInDepart" />
          	</strong>
            </div>
            <div class="section-content">
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		  <thead>
		    <tr>
		      <th width="30%"><s:text name="employee.employeeCode" /></th>
		      <th width="30%"><s:text name="employee.employeeName" /></th>
		      <th width="30%"><s:text name="employee.post" /></th>
		    </tr>
		  </thead>
		  <tbody>
		  	<s:if test="%{employeeInDepartList != null}">
		  	<s:iterator value="employeeInDepartList" id="employeeInDepartMap">
		    <tr>
	  			<td>
	  			  <s:if test="%{modeFlg != null}">
	  			  <s:url action="BINOLBSEMP02_init" id="employeeDetailUrl">
					<s:param name="employeeId" value="%{#employeeInDepartMap.employeeId}"></s:param>
				  </s:url>
	  			  <a href="${employeeDetailUrl }" class="popup" onclick="javascript:openWin(this);return false;" style="color: #3366FF;">
	  				<s:property value="#employeeInDepartMap.employeeCode"/>
	  			  </a>
	  			  </s:if>
	  			  <s:else>
	  			    <s:property value="#employeeInDepartMap.employeeCode"/>
	  			  </s:else>
	  			</td>
	  			<td><s:property value="#employeeInDepartMap.employeeName"/></td>
	  			<td><s:property value="#employeeInDepartMap.categoryName" /></td>
  			</tr>
  			</s:iterator>
  			</s:if>
		  </tbody>
		</table>
	  </div>
	</div>
</s:else>	


  
<s:if test="%{departAddressList != null && !departAddressList.isEmpty()}">  
  <div class="section">
    <div class="section-header clearfix">
    <strong class="left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="departAddress"></s:text></strong>
    </div>
    
    <div class="section-content">
      <s:iterator value="departAddressList" id="departAddress">
      <table class="detail" cellpadding="0" cellspacing="0">
        <caption>
          <span class="left">
         	  <s:property value='#application.CodeTable.getVal("1027", #departAddress.addressTypeId)' />
         	  <s:if test="%{#departAddress.defaultFlag == 1 }">&nbsp;&nbsp;<span class="highlight"><s:text name="defaultFlag"></s:text></span></s:if>
          </span>
        </caption>
        <tr>
          <th><s:text name="addressLine1"></s:text></th>
          <td><span><s:property value="#departAddress.addressLine1"/></span></td>
          <th><s:text name="provinceName"></s:text></th>
          <td><span><s:property value="#departAddress.provinceName"/></span></td>
        </tr>
        <tr>
          <th><s:text name="addressLine2"></s:text></th>
          <td><span><s:property value="#departAddress.addressLine2"/></span></td>
          <th><s:text name="cityName"></s:text></th>
          <td><span><s:property value="#departAddress.cityName"/></span></td>
        </tr>
        <tr>
          <th><s:text name="zipCode"></s:text></th>
          <td><span><s:property value="#departAddress.zipCode"/></span></td>
          <%-- <th><s:text name="locationGPS"></s:text></th>
          <td><span>${departAddress.locationGPS }</span></td>--%>
        </tr>
      </table>
      </s:iterator>
    </div> 
  </div>
</s:if> 

<s:if test="%{departContactList != null && !departContactList.isEmpty()}">  
  <div class="section">
    <div class="section-header clearfix">
    <strong class="left"><span class="ui-icon icon-ttl-section-info"></span><s:text name="departContact"></s:text></strong>
    </div>
    
    <div class="section-content">
      <s:iterator value="departContactList" id="departContact">
      <table class="detail" cellpadding="0" cellspacing="0">
        <s:if test="%{#departContact.defaultFlag == 1 }">
        <caption>
        	<span class="left"><span class="highlight"><s:text name="defaultFlag"></s:text></span></span>
        </caption>
        </s:if>
        <tr>
          <th><s:text name="employeeNam"></s:text></th>
          <td><span><s:property value="#departContact.employeeName"/></span></td>
          <th><s:text name="phone"></s:text></th>
          <td><span><s:property value="#departContact.phone"/></span></td>
        </tr>
        <tr>
          <th><s:text name="mobilePhone"></s:text></th>
          <td><span><s:property value="#departContact.mobilePhone"/></span></td>
          <th><s:text name="email"></s:text></th>
          <td><span><s:property value="#departContact.email"/></span></td>
        </tr>
      </table>
      </s:iterator>
    </div> 
  </div>
</s:if>

<s:if test="%{modeFlg != null}">
	<s:if test="%{organizationInfo != null}">
	  <s:if test='%{organizationInfo.type != "0" && organizationInfo.type != "1" && organizationInfo.type != "Z"}'>
	  <hr class="space" />
	  <div class="center">
	  	<s:url action="BINOLBSDEP03_init" id="updateOrganizationUrl">
		<s:param name="organizationId" value="%{organizationInfo.organizationId}"></s:param>
		</s:url>
		<a href="${updateOrganizationUrl }" id="updateOrganizationUrl" style="display: none;"></a>
		<cherry:show domId="BINOLBSDEP0104">
		  <button class="save" onclick="openWin('#updateOrganizationUrl');return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="edit_button"></s:text></span></button> <%-- <button class="delete"><span class="ui-icon icon-delete-big2"></span><span class="button-text">停用</span></button>--%>
		</cherry:show>
		<s:url action="BINOLBSDEP05_delete" id="delDepart">
          	<s:param name="organizationId" value="%{organizationInfo.organizationId}"></s:param>
        </s:url>
        <s:hidden name="organizationId" value="%{organizationInfo.organizationId}"/>
        <s:hidden name="higherDepartPath" value="%{organizationInfo.higherDepartPath}" />
  		<s:if test="%{organizationInfo.validFlag == 1}">
   		<cherry:show domId="BINOLBSDEP0103">
   		<button class="edit" onclick="bsdep01_editValidFlag('disable','${delDepart }');return false;">
       		<span class="ui-icon icon-delete-big"></span>
       		<span class="button-text"><s:text name="global.page.disable"/></span>
       	</button>
       	</cherry:show>
   		</s:if>
   		<s:else>
   		<cherry:show domId="BINOLBSDEP0102">
   		<button class="edit" onclick="bsdep01_editValidFlag('enable','${delDepart }');return false;">
       		<span class="ui-icon icon-success"></span>
       		<span class="button-text"><s:text name="global.page.enable"/></span>
       	</button>
       	</cherry:show>
   		</s:else>
	  </div>
	  </s:if>
	</s:if>
</s:if>
<s:else>
  	<hr class="space" />
	<cherry:form action="BINOLBSDEP03_init" csrftoken="false" id="updateOrganization">
	  <input name="csrftoken" type="hidden"/>
	  <s:hidden name="organizationId" value="%{organizationInfo.organizationId}"></s:hidden>
	  <s:hidden name="fromPage" value="1"></s:hidden>
	</cherry:form> 
	<div class="center">
	  <s:if test="%{organizationInfo != null}">
	  <s:if test='%{organizationInfo.type != "0" && organizationInfo.type != "1" && organizationInfo.type != "Z"}'>
	  <cherry:show domId="BINOLBSDEP0104">
	  <button class="save" onclick="updateOrganization();return false;"><span class="ui-icon icon-edit-big"></span><span class="button-text"><s:text name="edit_button"></s:text></span></button> <%-- <button class="delete"><span class="ui-icon icon-delete-big2"></span><span class="button-text">停用</span></button>--%>
	  </cherry:show>
	  </s:if>
	  </s:if>
	  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
	</div>
</s:else>
 

</s:i18n>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>   

    
 