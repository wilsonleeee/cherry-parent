<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/fac/BINOLBSFAC02.js"></script>
<%-- 语言环境 --%>
<s:set id="language" value="#session.language"/>
<s:i18n name="i18n.bs.BINOLBSFAC02">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="BSFAC02.manage"/> &gt; <s:text name="BSFAC02.title"/>
		       </span>
		    </div>
	  	</div>
	  	<cherry:form id="mainForm" method="post" class="inline" csrftoken="false" action="BINOLBSFAC03_init">
	        <s:hidden name="manufacturerInfoId" value="%{manufacturerInfoId}"/>
	        <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
        </cherry:form>
	  	<div class="panel-content">
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
			                <th><s:text name="BSFAC02.manufacturerCode"/></th><%-- 厂商编码 --%>
			                <td><span><s:property value="facInfo.manufacturerCode"/></span></td>
			                <th><s:text name="BSFAC02.factoryNameCN"/></th><%-- 厂商名称 --%>
			                <td><span><s:property value="facInfo.factoryNameCN"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="BSFAC02.factoryNameShortCN"/></th><%-- 厂商简称 --%>
			                <td><span><s:property value="facInfo.factoryNameShortCN"/></span></td>
			                <th><s:text name="BSFAC02.factoryNameEN"/></th><%-- 厂商外文名 --%>
			                <td><span><s:property value="facInfo.factoryNameEN"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="BSFAC02.factoryNameShortEN"/></th><%-- 厂商外文简称 --%>
			                <td><span><s:property value="facInfo.factoryNameShortEN"/></span></td>
			                <th><s:text name="BSFAC02.legalPerson"/></th><%-- 法人代表 --%>
			                <td><span><s:property value="facInfo.legalPerson"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="BSFAC02.telePhone"/></th><%-- 联系电话 --%>
			                <td><span><s:property value="facInfo.telePhone"/></span></td>
			                <th><s:text name="BSFAC02.mobile"/></th><%-- 手机号码 --%>
			                <td><span><s:property value="facInfo.mobile"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="BSFAC02.levelCode"/></th><%-- 厂商等级 --%>
			                <td><span><s:property value="facInfo.levelCode"/></span></td>
			                <th><s:text name="BSFAC02.status"/></th><%-- 厂商状态 --%>
			                <td><span><s:property value="facInfo.status"/></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="BSFAC02.brandName"/></th><%-- 所属品牌 --%>
			                <s:if test="#language.equals('en_US')"><td><span><s:property value="facInfo.brandNameEN"/></span></td></s:if>
			                <s:else><td><span><s:property value="facInfo.brandNameCN"/></span></td></s:else>
			                <th><s:text name="BSFAC02.defaultFlag"/></th><%-- 默认显示 --%>
			                <td><span><s:property value='#application.CodeTable.getVal("1053", facInfo.defaultFlag1)'/></span></td>
		             	</tr>
		             	<tr>
		             		<th><s:text name="BSFAC02.validFlag"/></th><%-- 有效状态 --%>
		             		<td><span><s:property value='#application.CodeTable.getVal("1052", facInfo.validFlag)'/></span></td>
		             	</tr>
	         		</table>
                </div>
            </div>
           	<div class="section">
	            <div class="section-header">
	            	<strong>
	            		<span class="ui-icon icon-ttl-section-info"></span>
	            		<s:text name="BSFAC02.addressInfo"/><%-- 厂商地址 --%>
	            	</strong>
	            </div>
	            <div class="section-content">
	            	<s:iterator value="addList">
	            		<table class="detail" cellpadding="0" cellspacing="0">
	            			<caption>
			                  	<s:if test="defaultFlag2 == 1">
				                  	<span class="highlight"><s:text name="BSFAC02.def_address"/></span><%-- 默认地址 --%>
			                  	</s:if>
			                  	<s:else>
			                  		<s:text name="BSFAC02.bkp_address"/><%-- 备用地址 --%>
			                  	</s:else>
		                  	</caption>
			              	<tr>
				                <th><s:text name="BSFAC02.provinceName"/></th><%-- 省份 --%>
				                <s:if test="#language.equals('en_US')"><td><span><s:property value="provinceEN"/></span></td></s:if>
				             	<s:else><td><span><s:property value="provinceCN"/></span></td></s:else>
				                <th><s:text name="BSFAC02.cityName"/></th><%-- 城市 --%>
				                <s:if test="#language.equals('en_US')"><td><span><s:property value="cityEN"/></span></td></s:if>
				             	<s:else><td><span><s:property value="cityCN"/></span></td></s:else>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC02.addressType"/></th><%-- 地址类型 --%>
				                <td><span><s:property value='#application.CodeTable.getVal("1027",addressTypeId)'/></span></td>
				                <th><s:text name="BSFAC02.zipCode"/></th><%-- 邮编 --%>
				                <td><span><s:property value="zipCode"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="BSFAC02.address"/></th><%-- 地址 --%>
				                <td colspan="3"><p><s:property value="address"/></p></td>
			             	</tr>
		             	</table>	
	            	</s:iterator>
             	</div>
           	</div>
           	<%-- 操作按钮 --%>
            <div id="editButton" class="center clearfix">
          		<cherry:show domId="BSFAC02EDIT">
          			<button class="edit" onclick="editFac();return false;">
	              		<span class="ui-icon icon-edit-big"></span>
	              		<span class="button-text"><s:text name="global.page.edit"/></span>
	              	</button>
          		</cherry:show>
	            <button id="close" class="close" type="button"  onclick="doClose();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
	  	</div>
	</div>
</div>
</s:i18n>