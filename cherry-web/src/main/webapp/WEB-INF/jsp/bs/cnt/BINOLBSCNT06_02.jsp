<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT06_1.js"></script>
<s:url id="savaEdit_url" value="/basis/BINOLBSCNT06_savaEdit"></s:url>
<a style="display:none;" id="savaEditUrl" href="${savaEdit_url}"></a>
<s:if test="%{counterInfo != null && !counterInfo.isEmpty()}">
<s:i18n name="i18n.bs.BINOLBSCNT01">
<s:text name="global.page.select" id="select_default"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="counter.counterManage"/> &gt; <s:text name="counter.editCounter"/>
	       </span>
	    </div>
	  </div>
	  <div id="actionResultDisplay"></div>
	  <div class="panel-content clearfix">
        <cherry:form id="mainForm" class="inline" csrftoken="false">
          <s:hidden name="counterInfoId" value="%{counterInfo.counterInfoId}"></s:hidden>
          <s:hidden name="brandInfoId" value="%{counterInfo.brandInfoId}"></s:hidden>
          <%-- ====================== 基本信息DIV开始 ===================== --%>
			<div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              	<span class="highlight">（<s:text name="counter.describe"></s:text>）</span>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                	<tr>
                		<th>
                			<%-- 品牌code --%>
                			<s:text name="counter.brandNameChinese"><span class="highlight"><s:text name="global.page.required"></s:text></span></s:text>
                		</th>
                		<td>
                			<span>
                				<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="%{counterInfo.brandInfoId}"></s:select>
                			</span>
                		</td>
                		<th>
                			<%-- 测试区分 --%>
                			<s:text name="counter.counterKind"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span>
                		</th>
					    <td>
					    	<span>
					    		<s:select id="counterKind" list='#application.CodeTable.getCodes("1031")' listKey="CodeKey" listValue="Value" name="counterKind" value="%{counterInfo.counterKind}"></s:select>
					  		</span>
					  	</td>
                	</tr>
	                <tr>
				      <th>
				      		<%-- 柜台code --%>
				      		<s:text name="counter.counterCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span>
				      </th>
				      <td>
				      		<span>
				      			<s:if test="!counterInfo.counterCodeError">
                					<s:textfield id="counterCode" maxlength="15" cssClass="text" name="counterCode" value="%{counterInfo.counterCode}"></s:textfield>
	                			</s:if>
	                			<s:else>
	                				<s:textfield id="counterCode" maxlength="15" cssClass="text" cssStyle="color:red;" name="counterCode" value="%{counterInfo.counterCode}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
	                			</s:else>
				      		</span>
				      </td>
				      <th>
				      		<%-- 柜台名称 --%>
				      		<s:text name="counter.counterNameIF"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span>
				      </th>
				      <td>
				      		<span>
				      			<s:if test="!counterInfo.counterNameError">
                					<s:textfield id="counterName" maxlength="15" cssClass="text" name="counterName" value="%{counterInfo.counterName}"></s:textfield>
	                			</s:if>
	                			<s:else>
	                				<s:textfield id="counterName" maxlength="15" cssClass="text" cssStyle="color:red;" name="counterName" value="%{counterInfo.counterName}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
	                			</s:else>
				      		</span>
				      </td>
				    </tr>
				    <tr>
				    	<%-- 省 --%>
				      <th><s:text name="counter.province"></s:text></th>
				      <td>
					      	<span>
						        <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
				                	<span id="provinceText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
					                	<s:if test='null != counterInfo.provinceName && !"".equals(counterInfo.provinceName)'>
					                		<s:if test="counterInfo.provinceNameError">
					                			<span id="highlight" class="highlight"><s:property value="counterInfo.provinceName"/></span>
					                		</s:if>
					                		<s:else>
					                			<s:property value="counterInfo.provinceName"/>
					                		</s:else>
					                	</s:if>
					                	<s:else>
					                		<s:text name="global.page.select"/>
					                	</s:else>
				                	</span>
				     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
				     		 	</a>
				     		 	<s:hidden name="regionId" id="regionId" value="%{counterInfo._regionId}"/>
				     		 	<s:hidden name="provinceId" id="provinceId" value="%{counterInfo.provinceId}"/>
				     		 	<s:hidden name="regionName" id="regionName" value="%{counterInfo.regionName}"/>
				     		 	<s:hidden name="regionCode" id="regionCode" value="%{counterInfo.regionCode}"/>
				     		 	<s:hidden name="provinceCode" id="provinceCode" value="%{counterInfo.provinceCode}"/>
				     		 	<s:hidden name="provinceName" id="provinceName" value="%{counterInfo.provinceName}"/>
				     		 	<s:hidden name="cityCode" id="cityCode" value="%{counterInfo.cityCode}"/>
				     		 	<s:hidden name="cityName" id="cityName" value="%{counterInfo.cityName}"/>
					      	</span>
				      </td>
				      <th>
				      	<%-- 英文名称 --%>
				      		<s:text name="counter.nameForeign"></s:text>
				      </th>
				      <td>
				      		<span>
				      			<s:if test="!counterInfo.foreignNameError">
                					<s:textfield id="foreignName" maxlength="50" cssClass="text" name="foreignName" value="%{counterInfo.foreignName}"></s:textfield>
	                			</s:if>
	                			<s:else>
	                				<s:textfield id="foreignName" maxlength="50" cssClass="text" cssStyle="color:red;" name="foreignName" value="%{counterInfo.foreignName}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
	                			</s:else>
				      		</span>
				      </td>
				    </tr>
					<tr>
					  <%-- 市 --%>
					  <th><s:text name="counter.city"></s:text></th>
					  <td>
						  	<span>
							    <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
				                	<span id="cityText" class="button-text" style="min-width:100px;text-align:left;line-height: 1.4;">
				                	<s:if test='%{null != counterInfo.cityName && !"".equals(counterInfo.cityName)}'>
				                		<s:if test="counterInfo.cityNameError">
				                			<span id="highlight" class="highlight"><s:property value="counterInfo.cityName"/></span>
				                		</s:if>
				                		<s:else>
				                			<s:property value="counterInfo.cityName"/>
					                	</s:else>
				                	</s:if>
				                	<s:else>
				                		<s:if test="counterInfo.cityNameError">
				                			<span id="highlight" class="highlight"><s:text name="global.page.select"/></span>
				                		</s:if>
				                		<s:else>
				                			<s:text name="global.page.select"/>
					                	</s:else>
				                		
				                	</s:else>
				                	</span>
				     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
				     		 	</a>
				     		 	<s:hidden name="cityId" id="cityId" value="%{counterInfo.cityId}"/>
						  	</span>
					  </td>
					  <th>
					   <%-- 柜台分类 --%>
					  	<s:text name="counter.category"></s:text>
					  </th>
				      <td>
				      		<span>
				      			<s:if test="!counterInfo.counterCategoryError">
                					<s:textfield id="counterCategory" maxlength="100" cssClass="text" name="counterCategory" value="%{counterInfo.counterCategory}"></s:textfield>
	                			</s:if>
	                			<s:else>
	                				<s:textfield id="counterCategory" maxlength="100" cssClass="text" cssStyle="color:red;" name="counterCategory" value="%{counterInfo.counterCategory}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
	                			</s:else>
				      		</span>
				      </td>
					</tr>
					<tr>
					  <th>
					  	<%-- 经销商code --%>
					  	<s:text name="counter.resellerCode"></s:text>
					  </th>
					  <td>
					  	<span>
						  	<s:if test="!counterInfo.resellerCodeError">
						  		<s:textfield id="resellerCode" maxlength="5" cssClass="text" name="resellerCode" value="%{counterInfo.resellerCode}"></s:textfield>
						  	</s:if>
						  	<s:else>
						  		<s:textfield id="resellerCode" maxlength="5" cssClass="text" cssStyle="color:red;" name="resellerCode" value="%{counterInfo.resellerCode}"></s:textfield>
						  	</s:else>
					  	</span>
					  </td>
					  <th>
					  	<%-- 商场名称 --%>
					  	<s:text name="counter.mallName1"></s:text>
					  </th>
					  <td>
					  		<span>
					  			<s:if test="!counterInfo.mallNameError">
					  				<s:textfield id="mallName" maxlength="30" cssClass="text" name="mallName" value="%{counterInfo.mallName}"></s:textfield>
					  			</s:if>
					  			<s:else>
					  				<s:textfield id="mallName" maxlength="30" cssClass="text" cssStyle="color:red;" name="mallName" value="%{counterInfo.mallName}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  			</s:else>
					  		</span>
					  </td>
					</tr>
					<tr>
						<th>
					  	<%-- 经销商名称 --%>
					  	<s:text name="counter.resellerName1"></s:text>
					  </th>
				      <td>
				      	<span>
				      		<s:if test='!counterInfo.resellerNameError'>
				      			<s:textfield id="resellerName" maxlength="30" cssClass="text" name="resellerName" value="%{counterInfo.resellerName}"></s:textfield>
				      		</s:if>
				      		<s:else>
				      			<s:textfield id="resellerName" maxlength="30" cssClass="text" cssStyle="color:red;" name="resellerName" value="%{counterInfo.resellerName}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
				      		</s:else>
				      	</span>
				      </td>
				      <th>
					  	<%-- 渠道名称 --%>
					  	<s:text name="counter.channelName1"></s:text>
					  </th>
					  <td>
				      	<span>
				      		<s:if test='!counterInfo.channelNameError'>
				      			<s:textfield id="channelName" maxlength="50" cssClass="text" name="channeName" value="%{counterInfo.channelName}"></s:textfield>
				      		</s:if>
				      		<s:else>
				      			<s:textfield id="channelName" maxlength="50" cssClass="text" cssStyle="color:red;" name="channeName" value="%{counterInfo.channelName}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
				      		</s:else>
				      	</span>
				      </td>
					  
					</tr>
					<tr>
					  <th>
					  	<%-- 柜台面积 --%>
					  	<s:text name="counter.counterSpace"></s:text>
					  </th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.counterSpaceError">
					  			<s:textfield id="counterSpace" cssClass="text" name="counterSpace" value="%{counterInfo.counterSpace}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="counterSpace" cssClass="text" cssStyle="color:red;" name="counterSpace" value="%{counterInfo.counterSpace}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  <%-- 地址 --%>
					  <th><s:text name="counter.counterAddress"></s:text></th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.addressError">
					  			<s:textfield id="address" maxlength="50" cssStyle="width:300px;" cssClass="text" name="address" value="%{counterInfo.address}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="address" maxlength="50" cssClass="text" cssStyle="color:red;width:400px;" name="address" value="%{counterInfo.address}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  </tr>
					<tr>
					  <%-- 经度 --%>
					  <th><s:text name="counter.longitude"></s:text></th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.longitudeError">
					  			<s:textfield id="longitude" maxlength="32"  cssClass="text" name="longitude" value="%{counterInfo.longitude}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="longitude" maxlength="32" cssClass="text" cssStyle="color:red;" name="longitude" value="%{counterInfo.longitude}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  <th>
					  	<%-- 柜台电话 --%>
					  	<s:text name="counter.telephone"></s:text>
					  </th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.counterTelephoneError">
					  			<s:textfield id="counterTelephone" cssClass="text" name="counterTelephone" value="%{counterInfo.counterTelephone}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="counterTelephone" cssClass="text" cssStyle="color:red;" name="counterTelephone" value="%{counterInfo.counterTelephone}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  </tr>
					  <tr>
					  <%-- 纬度 --%>
					  <th><s:text name="counter.latitude"></s:text></th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.latitudeError">
					  			<s:textfield id="latitude" maxlength="32"  cssClass="text" name="latitude" value="%{counterInfo.latitude}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="latitude" maxlength="32" cssClass="text" cssStyle="color:red;" name="latitude" value="%{counterInfo.latitude}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  <!-- 所属系统 -->
					  <th><s:text name="counter.belongFaction"></s:text></th>
					  <td>
					  	<span>
				    		<s:select id="belongFaction" list='#application.CodeTable.getCodes("1309")' listKey="CodeKey" listValue="Value" name="belongFaction" headerKey="" headerValue="%{#select_default}" value="%{counterInfo.belongFaction}"></s:select>
				  		</span>
					  </td>
					  </tr>
					  <tr>
					  <th><s:text name="counter.employeeNum"></s:text></th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.employeeNumError">
					  			<s:textfield id="employeeNum" cssClass="text" name="employeeNum" value="%{counterInfo.employeeNum}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="employeeNum" cssClass="text" cssStyle="color:red;" name="employeeNum" value="%{counterInfo.employeeNum}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  <th>
					   <%-- 柜台类型 --%>
					  	<s:text name="counter.managingType2"></s:text>
					  </th>
				      <td>
				      		<span>
				      			<s:if test="!counterInfo.managingType2Error">
                					<span>
							    		<s:select id="managingType2" list='#application.CodeTable.getCodes("1403")' listKey="CodeKey" listValue="Value" name="managingType2" value="%{counterInfo.managingType2}"></s:select>
							  		</span>
	                			</s:if>
	                			<s:else>
	                				<span>
							    		<s:select id="managingType2" list='#application.CodeTable.getCodes("1403")' listKey="CodeKey" listValue="Value" name="managingType2" value="%{counterInfo.managingType2}"></s:select>
							  		</span>
	                			</s:else>
				      		</span>
				      </td>
				      </tr>
				      <tr>
					  <!-- 是否有POS机 -->
					  <th><s:text name="counter.posFlag"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.posFlagError">
					  			<s:select list='#application.CodeTable.getCodes("1308")' listKey="CodeKey" listValue="Value" name="posFlag" headerKey="" headerValue="%{#select_default}"></s:select>
					  		</s:if>
					  		<s:else>
					  			<s:select list='#application.CodeTable.getCodes("1308")' cssStyle="color:red;" listKey="CodeKey" listValue="Value" name="posFlag" headerKey="" headerValue="%{#select_default}"></s:select>
					  		</s:else>
					  	</span>
					  </td>
					  <!-- 银联设备号 -->
					  <th><s:text name="counter.equipmentCode"></s:text></th>
					  <td>
					  	<span>
					  		<s:if test="!counterInfo.equipmentCodeError">
					  			<s:textfield id="equipmentCode" cssClass="text" name="equipmentCode" value="%{counterInfo.equipmentCode}"></s:textfield>
					  		</s:if>
					  		<s:else>
					  			<s:textfield id="equipmentCode" cssClass="text" cssStyle="color:red;" name="equipmentCode" value="%{counterInfo.equipmentCode}" onchange="binOLBSCNT06_1.clearError(this);"></s:textfield>
					  		</s:else>
					  	</span>
					  </td>
					  </tr>
                </table>
			  </div>
			</div>
			
			<%--=================== 柜台主管 ===================== --%>
			<div class="section">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom04_popDataTableOfEmployee(this,$('#brandInfoId').serialize());return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="counter.counterHeader" /></span>
	       		</a>
	       		
              </div>
              <div class="section-content">
              <s:if test='counterInfo.basNameError'>
					<span id="showBasError" class="highlight">柜台主管不正确,请重新选择!</span>
				</s:if>
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
				  		<s:iterator value="%{counterInfo.employeeList}" id="employeeMap">
					  		<s:if test='%{#employeeMap.manageType == "1"}'>
					  			<tr>
					  			<td><s:property value="#employeeMap.employeeName"/></td>
					  			<td><s:property value="#employeeMap.departName"/></td>
					  			<td><s:property value="#employeeMap.categoryName"/></td>
					  			<td class="center">
							    	<s:hidden name="counterHead" value="%{#employeeMap.employeeId}"></s:hidden>
									<a class="delete" onclick="bscom04_removeEmployee(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="counter.delete" /></span></a>
								</td>
							    </tr>
					  		</s:if>
					  	</s:iterator>
				  </tbody>
				</table>
				
			  </div>
			</div>
           
            <%-- 操作按钮 --%>
            <div id="editButton" class="center clearfix">
       			<s:url action="BINOLBSCNT03_update" id="updateCounterUrl"></s:url>
       			<button class="save" onclick="binOLBSCNT06_1.saveEdit('${savaEdit_url}');return false;">
            		<span class="ui-icon icon-save"></span>
            		<span class="button-text"><s:text name="global.page.save"/></span>
             	</button>
	            <button id="close" class="close" type="button"  onclick="binOLBSCNT06_1.close();return false;">
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
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default}', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide">
	<s:if test="%{cityList != null && !cityList.isEmpty()}">
	<ul class="u2">
		<li onclick="bscom03_getNextRegin(this, '${select_default}', 2);"><s:text name="global.page.all"></s:text></li>
		<s:iterator value="cityList">
			<li id="<s:property value="regionId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 2);"><s:property value="regionName"/></li>
		</s:iterator>
	</ul>
	</s:if>
</div>
<div id="countyTemp" class="ui-option hide">
	<s:if test="%{countyList != null && !countyList.isEmpty()}">
	<ul class="u2">
		<li onclick="bscom03_getNextRegin(this, '${select_default }', 3);"><s:text name="global.page.all"></s:text></li>
		<s:iterator value="countyList">
			<li id="<s:property value="regionId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 3);"><s:property value="regionName"/></li>
		</s:iterator>
	</ul>
	</s:if>
</div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
</s:i18n>
<div class="hidden">
	<s:hidden id="editCounterCode" name="counterCode"></s:hidden>
</div>

<%-- 根据品牌ID筛选下拉列表URL --%>
<s:url id="filter_url" value="BINOLBSCNT04_filter"/>
<s:hidden name="filter_url" value="%{filter_url}"/>

<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM02.jsp" flush="true" />

</s:if>
<s:else>
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true" />
</s:else>

<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>
