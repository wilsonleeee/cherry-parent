<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF12.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 保存code类别URL --%>
<s:url id="save_url" value="/pl/BINOLPLSCF06_saveCodeM"></s:url>

<s:i18n name="i18n.pl.BINOLPLSCF07">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="code_query"/> &gt; <s:text name="code_detail"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
	  	<div class="panel-content">
	  			<%-- code值编辑Form --%>
			  	<cherry:form id="coderEditForm" method="post" class="inline" csrftoken="false" action="BINOLPLSCF13_init">
					<s:hidden name="codeManagerID" value="%{codeManagerID}" />			        
			        <s:hidden name="coderID" value="%{coderID}"/>
			        <s:hidden name="orgName" value="%{orgName}"/>
			        <s:hidden name="brandName" value="%{brandName}"/>
			        <s:hidden name="orgCode" value="%{orgCode}"/>
			        <s:hidden name="brandCode" value="%{brandCode}"/>			        	        
			        <s:hidden name="codeType" value="%{codeType}"/>	        			        
			        <input type="hidden" id="parentCsrftoken" name="csrftoken"/>
		        </cherry:form>
				<%--返回code值一览 form area --%>	  	
		  		<cherry:form id="toQueryForm" action="BINOLPLSCF10_init" method="post" csrftoken="false">
					<s:hidden name="codeManagerID" value="%{codeManagerID}" />
			        <s:hidden name="orgCode" value="%{orgCode}"/>
			        <s:hidden name="brandCode" value="%{brandCode}"/>
			        <s:hidden name="orgName" value="%{orgName}"/>
			        <s:hidden name="brandName" value="%{brandName}"/>	        
			        <s:hidden name="codeType" value="%{codeType}"/>	        
		        	<input type="hidden" id="parentCsrftoken1" name="csrftoken"/>
		        </cherry:form>
			        
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
				                <th><s:text name="orgInfo"/></th><%-- 组织--%>
				                 <td><span><s:label name="orgName"/></span></td>
				                <th><s:text name="brandInfo"/></th><%-- 品牌--%>
				                 <td><span><s:label name="brandName"/></span></td>					                 
			             	</tr>
		                	
			              	<tr>
				                <th><s:text name="codeType"/><span class="highlight"></span></th><%--Code类别 --%>
				                <td><span><s:label name="codeType"/></span></td>
				                <th><s:text name="codeKey"/><span class="highlight"></span></th><%-- CodeKey--%>
				                <td><span><s:label name="coderDetail.codeKey" cssClass="text" maxlength="4"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value1"/></th><%-- Value1 --%>
				                <td><span><s:label name="coderDetail.value1" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="value2"/></th><%-- Value2--%>
				                <td><span><s:label name="coderDetail.value2" cssClass="text" maxlength="20"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value3"/></th><%-- value3--%>
				                <td><span><s:label name="coderDetail.value3" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="grade"/></th><%-- 级别--%>
				                <td><span><s:label name="coderDetail.grade" cssClass="text"/></span></td>				                
			             	</tr>
			             	<tr>
				                <th><s:text name="codeOrder"/></th><%-- 显示顺序--%>
				                <td><span><s:label name="coderDetail.codeOrder" cssClass="text"/></span></td>
			             	</tr>			             	
		         		</table>
	                </div>
	            </div>
	           	<%-- 操作按钮 --%>
	            <div id="editButton" class="center clearfix">
	       			<button class="edit" onclick="scf12Edit();return false;">
	            		<span class="ui-icon icon-edit-big"></span>
	            		<span class="button-text"><s:text name="global.page.edit"/></span>
	            	</button>
	          		<button id="back" class="back" type="button" onclick="plscf12_doBack()">
		            	<span class="ui-icon icon-back"></span>
		            	<span class="button-text"><s:text name="global.page.back"/></span>
		            </button>
	            	
		            <button id="close" class="close" type="button"  onclick="doClose();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
	  	</div>
	</div>
</div>

</div>
</s:i18n>