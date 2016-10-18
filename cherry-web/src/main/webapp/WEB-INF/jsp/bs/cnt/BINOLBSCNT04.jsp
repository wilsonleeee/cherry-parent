<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT04.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<link rel="stylesheet" href="/Cherry/css/cherry/cherry_timepicker.css" type="text/css">
<script type="text/javascript">
$(function() {
	
	binolbscnt04.belongFactionBinding({elementId:"belongFactionName",codeType:"1309"}); 
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));
	});
	
	binolbscnt04.invoiceCompanyBinding({elementId:"invoiceCompanyName",codeType:"1319"}); 
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));
	}); 
} );
</script>

<s:i18n name="i18n.bs.BINOLBSCNT01">
<s:text name="global.page.select" id="select_default"/>
<div id="timeOnlyTitle" class="hide"><s:text name="global.timepicker.timeOnlyTitle"/></div>
<div id="currentText" class="hide"><s:text name="global.timepicker.currentText"/></div>
<div id="closeText" class="hide"><s:text name="global.timepicker.closeText"/></div>

<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="counter.counterManage"/> &gt; <s:text name="counter.addCounter"/>
	       </span>
	    </div>
	  </div>
	  <div id="actionResultDisplay"></div>
	  <div class="panel-content clearfix">
        <cherry:form id="mainForm" class="inline" csrftoken="false">
          <%-- ====================== 基本信息DIV开始 ===================== --%>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
	                <tr>
				      <th>
				      <s:text name="counter.counterCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span>
				      <s:hidden id="cntCodeRule" name="cntCodeRule" value="%{cntCodeRule}"></s:hidden>
				      </th>
				      <td>
				      	<span>
				      		<s:if test="cntCodeRule == 1">
					      		<s:textfield name="counterCode" cssClass="text" maxlength="15"></s:textfield>
				      		</s:if>
				      		<s:elseif test="cntCodeRule == 2">
				      			<%--<s:textfield name="counterCode" cssClass="text" maxlength="15"></s:textfield>--%>
				      			<span id="counterCodeId"></span>
				      			<s:hidden id="counterCode" name="counterCode" value="%{counterInfo.counterCode}"></s:hidden>
				      		</s:elseif>
				      	</span>
				      </td>
				      <th><s:text name="counter.counterKind"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					  <td><span><s:select list='#application.CodeTable.getCodes("1031")' listKey="CodeKey" listValue="Value" name="counterKind" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
				    </tr>
				    <tr>
				      <th><s:text name="counter.brandNameChinese"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				      <td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="binolbscnt04.changeBrandInfo(this,'%{#select_default}')"></s:select></span></td>
				      <th><s:text name="counter.counterNameIF"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				      <td><span><s:textfield name="counterNameIF" cssClass="text" maxlength="50"></s:textfield></span></td>
				    </tr>
				    <tr>
				      <th><s:text name="counter.province"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
				      <td><span>
				        <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
		                	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
		     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
		     		 	</a>
		     		 	<s:hidden name="regionId" id="regionId"/>
		     		 	<s:hidden name="provinceId" id="provinceId"/>
				      </span></td>
				      <th><s:text name="counter.counterNameShort"></s:text></th>
				      <td><span><s:textfield name="counterNameShort" cssClass="text" maxlength="20"></s:textfield></span></td>
				    </tr>
					<tr>
					  <th><s:text name="counter.city"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					  <td><span>
					    <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
		                	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
		     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
		     		 	</a>
		     		 	<s:hidden name="cityId" id="cityId" onchange="binolbscnt04.getCntCode()"/>
					  </span></td>
					  <th><s:text name="counter.nameForeign"></s:text></th>
				      <td><span><s:textfield name="nameForeign" cssClass="text" maxlength="50"></s:textfield></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.county"></s:text></th>
					  <td><span>
					    <a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
		                	<span id="countyText" class="button-text choice"><s:text name="global.page.select"/></span>
		     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
		     		 	</a>
		     		 	<s:hidden name="countyId" id="countyId"/>
					  </span></td>
					  <th><s:text name="counter.telephone"></s:text></th>
				      <td><span><s:textfield name="counterTelephone" cssClass="text" maxlength="20"></s:textfield></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.counterAddress"></s:text></th>
					  <td><span><s:textfield name="counterAddress" cssClass="text" maxlength="100"></s:textfield></span></td>
					  <th><s:text name="counter.status"></s:text></th>
				      <td><span><s:select list='#application.CodeTable.getCodes("1030")' listKey="CodeKey" listValue="Value" name="status"></s:select></span></td> 
					</tr>
					<tr>
						<!-- 运营模式 -->
						<th><s:text name="counter.operateMode"></s:text></th>
						<td><span><s:select list='#application.CodeTable.getCodes("1318")' listKey="CodeKey" listValue="Value" name="operateMode" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
						<!-- 所属系统 -->
						<th><s:text name="counter.belongFaction"></s:text></th>
						<td>
							<span>
								<s:textfield name="belongFactionName" cssClass="text" maxlength="30"/>
								<s:hidden name="belongFaction" id="belongFaction"/>
							</span>
						</td>
					</tr>
					<tr>
						<!-- 开票单位 -->
						<th><s:text name="counter.invoiceCompany"></s:text></th>
						<td>
							<span>
								<s:textfield name="invoiceCompanyName" cssClass="text" maxlength="30"/>
								<s:hidden name="invoiceCompany" id="invoiceCompany"/>
							</span>
						</td>
						<!-- 员工数 -->
						<th><s:text name="counter.employeeNum"></s:text></th>
						<td><span><s:textfield name="employeeNum" cssClass="text" maxlength="9"></s:textfield></span></td>
					</tr>
					<tr>
					  <th><s:text name="counter.channelName"></s:text></th>
					  <td><span>
					  	<s:select name="channelId" list="channelList" listKey="channelId" listValue="channelName" headerKey="" headerValue="%{#select_default}"></s:select>
					  </span></td>
					  <th><s:text name="counter.counterLevel"></s:text></th>
					  <td><span><s:select list='#application.CodeTable.getCodes("1032")' listKey="CodeKey" listValue="Value" name="counterLevel" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
					</tr>
					<tr>	
					  <th><s:text name="counter.mallName"></s:text></th>
					  <td><span>
					  	<s:select name="mallInfoId" list="mallInfoList" listKey="mallInfoId" listValue="mallName" headerKey="" headerValue="%{#select_default}"></s:select>
					  </span></td>
					  <th><s:text name="counter.counterCategory"></s:text></th>
					  <td><span><s:textfield name="counterCategory" cssClass="text" maxlength="100"></s:textfield></span></td>
					  <!-- 
					  <th><s:text name="counter.counterSpace"></s:text></th>
					  <td><span><s:textfield name="counterSpace" cssClass="text"></s:textfield><s:text name="square_meters"></s:text></span></td>
					   -->
					</tr>
					<tr>
					  <th><s:text name="counter.resellerName"></s:text></th>
					  <td><span>
					  	<s:select name="resellerInfoId" list="resellerInfoList" listKey="resellerInfoId" listValue="resellerName" headerKey="" headerValue="%{#select_default}"></s:select>
					  </span></td>
					  <th><s:text name="counter.resellerDepart"></s:text></th>
					  <td>
					    <span id="showResellerDepart"></span>
					    <s:hidden name="resellerDepartId"></s:hidden>
					    <a class="add right" onclick="binolbscnt04.selectResellerDepart();">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
					  </td>
					</tr>
					<tr>
						<%--经度 --%>
						<th><s:text name="counter.longitude"></s:text></th>
						 <td><span><s:textfield name="longitude" cssClass="text" maxlength="32"></s:textfield></span></td>
						<%--纬度 --%>
						<th><s:text name="counter.latitude"></s:text></th>
						 <td><span><s:textfield name="latitude" cssClass="text" maxlength="32"></s:textfield></span></td>
					</tr>
					<tr>
						<%-- 到期日 --%>
						<th><s:text name="counter.expiringDate"></s:text></th>
						<td>
							<span><s:textfield id="expiringDateDate" name="expiringDateDate" cssClass="date"/></span>
							<span style='margin-left:10px;'><s:textfield id="expiringDateTime" name="expiringDateTime" cssClass="date"/></span>
						</td>
						<%-- 是否有POS机 --%>
						<th><s:text name="counter.posFlag"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
						<td><span><s:select list='#application.CodeTable.getCodes("1308")' listKey="CodeKey" listValue="Value" name="posFlag" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
					</tr>
						<%-- 柜台协同区分 --%>
					<s:if test="maintainPassWord || maintainCoutSynergy">
						<tr>
							<s:if test="maintainPassWord">
								<th><s:text name="counter.passWord"></s:text></th>
								<td><span><s:textfield name="passWord" cssClass="text" value="" maxLength="15"></s:textfield></span></td>
							</s:if>
							<s:if test="maintainCoutSynergy">
								<th><s:text name="counter.counterSynergyFlag"></s:text></th>
								<td>
									<span>
										<s:select
											list='#application.CodeTable.getCodes("1331")'
											listKey="CodeKey" listValue="Value"
											name="counterSynergyFlag" >
										</s:select>
									</span>
								</td>
							</s:if>
						</tr>
					</s:if>
					
					<tr>
						<!-- 商圈 -->
						<th><s:text name="counter.busDistrict"></s:text></th>
						<td><span><s:textfield name="busDistrict" cssClass="text" maxlength="50"></s:textfield></span></td>
						<!-- 业务负责人 -->
						<th><s:text name="counter.busniessPrincipal"></s:text></th>
						<td><span><s:textfield name="busniessPrincipal" cssClass="text" maxlength="50"></s:textfield></span></td>
					</tr>
					<tr>
						<!-- 商圈 -->
						<th><s:text name="counter.equipmentCode"></s:text></th>
						<td><span><s:textfield name="equipmentCode" cssClass="text" maxlength="50"></s:textfield></span></td>
						<!-- 柜台类型 -->
						<th><s:text name="counter.managingType2"></s:text></th>
						<td><span><s:select list='#application.CodeTable.getCodes("1403")' listKey="CodeKey" listValue="Value" name="managingType2" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
					</tr>
                </table>
			  </div>
			</div>
			
			<%--=================== 上级部门 ===================== --%><%-- 
			<div class="section" id="higheOrgDiv">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom04_popDataTableOfHigheOrg(this,$('#brandInfoId').serialize());return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="counter.higheOrg" /></span>
	       		</a>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.departCode" /></th>
				      <th width="30%"><s:text name="counter.departName" /></th>
				      <th width="30%"><s:text name="counter.departType" /></th>
				      <th width="10%"><s:text name="counter.operation" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  </tbody>
				</table>
			  </div>
			</div>--%>
          
			<%--=================== 柜台主管 ===================== --%>
			<div class="section" id="counterHeadDiv">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom04_popDataTableOfEmployee(this,$('#brandInfoId').serialize());return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="counter.counterHeader" /></span>
	       		</a>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.name" /></th>
				      <th width="30%"><s:text name="counter.depart" /></th>
				      <th width="30%"><s:text name="counter.postion" /></th>
				      <th width="10%"><s:text name="counter.operation" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  </tbody>
				</table>
			  </div>
			</div>
            
			<%--=================== 关注该柜台的人 ===================== --%>
			<div class="section" id="likeCounterEmpDiv">
              <div class="section-header clearfix">
            	<a class="add left" href="#" onclick="bscom04_popDataTableOfLikeEmployee(this,$('#brandInfoId').serialize()); return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="counter.likeCounterEmp" /></span>
	       		</a>
              </div>
	          <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="counter.name" /></th>
				      <th width="30%"><s:text name="counter.depart" /></th>
				      <th width="30%"><s:text name="counter.postion" /></th>
				      <th width="10%"><s:text name="counter.operation" /></th>
				    </tr>
				  </thead>
				  <tbody>
				  </tbody>
				</table>
		  	  </div>
			</div>
	      
            <%-- 操作按钮 --%>
            <div id="editButton" class="center clearfix">
       			<s:url action="BINOLBSCNT04_save" id="addCounterUrl"></s:url>
       			<button class="save" onclick="binolbscnt04.addCounter('${addCounterUrl }');return false;">
            		<span class="ui-icon icon-save"></span>
            		<span class="button-text"><s:text name="global.page.save"/></span>
             	</button>
	            <button id="close" class="close" type="button"  onclick="binolbscnt04.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
          <%-- ====================== 基本信息DIV结束 ===================== --%>
        </cherry:form>
      </div>
</div>
</div>

<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="binolbscnt04.getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="binolbscnt04.getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide"></div>
<div id="countyTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>

<s:url id="getCntCodeUrl" value="BINOLBSCNT04_getCntCode"></s:url>
<s:hidden name="getCntCodeUrlId" value="%{getCntCodeUrl}"/>

<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
</s:i18n>

<%-- 根据品牌ID筛选下拉列表URL --%>
<s:url id="filter_url" value="BINOLBSCNT04_filter"/>
<s:hidden name="filter_url" value="%{filter_url}"/>

<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM02.jsp" flush="true" />

<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>
