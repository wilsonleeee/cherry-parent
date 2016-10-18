<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF09.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 更新厂商URL --%>
<s:url id="save_url" value="/pl/BINOLPLSCF08_update"></s:url>

<s:i18n name="i18n.pl.BINOLPLSCF06">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="edit_code"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
	  	<div class="panel-content">
	  		<form id="toDetailForm" action="BINOLPLSCF07_init" method="post">
				<s:hidden name="codeManagerID" value="%{codeManagerID}" />
	        	<input type="hidden" id="parentCsrftoken" name="csrftoken"/>
	        </form>
	  		<form id="mainForm" method="post" class="inline">
	  			<div id="actionResultDisplay"></div>
		  		<div class="section">
		            <div class="section-header">
		            	<strong>
		            		<span class="ui-icon icon-ttl-section-info-edit"></span>
		            		<s:text name="global.page.title"/><%-- 基本信息 --%>
		            	</strong>
		            </div>
		            <div id="base_info" class="section-content">
		                <table class="detail" cellpadding="0" cellspacing="0">
			              	<tr>
				                <th><s:text name="orgInfo"/><span class="highlight"></span></th><%-- 组织--%>
                  				<s:url action="BINOLPLSCF06_searchBrand" id="searchBrandUrl"></s:url> 					                
				                
				                <td>
				                	<span>
				                		<s:select cssStyle="width:100px;" list="orgInfoList" listKey="orgCode" listValue="orgName" name="orgCode" value="%{codeMInfo.orgCode}" onchange="plscf09_searchBrand('%{#searchBrandUrl}',this,'%{#select_default}');"></s:select>
				                		<s:hidden name="codeManagerID" value="%{codeManagerID}" />
				                	</span>
				                </td>
				                <th><s:text name="brandInfo"/><span class="highlight"></span></th><%-- 品牌--%>
				                <td><span><s:select cssStyle="width:100px;" list="brandInfoList" listKey="brandCode" listValue="brandName" name="brandCode" id="brandInfoId" value="%{codeMInfo.brandCode}"></s:select></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="codeType"/></th><%--Code类别  --%>
				                <td><span><s:textfield readonly="true" name="codeType" cssClass="text" maxlength="4" value="%{codeMInfo.codeType}"/></span></td>
				                <th><s:text name="codeName"/></th><%-- Code类别名称 --%>
				                <td><span><s:textfield name="codeName" cssClass="text" maxlength="20" value="%{codeMInfo.codeName}"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="keyDescription"/></th><%-- Key说明--%>
				                <td><span><s:textfield name="keyDescription" cssClass="text" maxlength="20" value="%{codeMInfo.keyDescription}"/></span></td>
				                <th><s:text name="value1Description"/></th><%-- Value1说明 --%>
				                <td><span><s:textfield name="value1Description" cssClass="text" maxlength="20" value="%{codeMInfo.value1Description}"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value2Description"/></th><%-- Value2说明 --%>
				                <td><span><s:textfield name="value2Description" cssClass="text" maxlength="20" value="%{codeMInfo.value2Description}"/></span></td>
				                <th><s:text name="value3Description"/></th><%-- Value3说明 --%>
				                <td><span><s:textfield name="value3Description" cssClass="text" maxlength="20" value="%{codeMInfo.value3Description}"/></span></td>
			             	</tr>
		         		</table>
	                </div>
	            </div>

	           	<%-- 操作按钮 --%>
	            <div id="saveButton" class="center clearfix">
          			<button class="save" onclick="plscf09_update('${save_url}');return false;">
	              		<span class="ui-icon icon-save"></span>
	              		<span class="button-text"><s:text name="global.page.save"/></span>
	              	</button>
	          		<button id="back" class="back" type="button" onclick="plscf09_doBack()">
		            	<span class="ui-icon icon-back"></span>
		            	<span class="button-text"><s:text name="global.page.back"/></span>
		            </button>
		            <button id="close" class="close" type="button"  onclick="doClose();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
            </form>
	  	</div>
	</div>
</div>
</s:i18n>