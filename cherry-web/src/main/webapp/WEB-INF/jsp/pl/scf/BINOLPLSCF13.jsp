<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF13.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 编辑code值URL --%>
<s:url id="save_url" value="/pl/BINOLPLSCF13_saveCoder"></s:url>

<s:i18n name="i18n.pl.BINOLPLSCF07">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="code_query"/> &gt; <s:text name="code_detail"/> &gt; <s:text name="code_edit"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
	  	<div class="panel-content">
	  		<%--返回code值详细 form area --%>	  	
	  		<cherry:form id="toDetailForm" action="BINOLPLSCF12_init" method="post" csrftoken="false">
				<s:hidden name="codeManagerID" value="%{codeManagerID}" />
				<s:hidden name="coderID" value="%{coderID}" />				
		        <s:hidden name="orgCode" value="%{orgCode}"/>
		        <s:hidden name="brandCode" value="%{brandCode}"/>
		        <s:hidden name="orgName" value="%{orgName}"/>
		        <s:hidden name="brandName" value="%{brandName}"/>	        
		        <s:hidden name="codeType" value="%{codeType}"/>	        
	        	<input type="hidden" id="parentCsrftoken1" name="csrftoken"/>
	        </cherry:form>
	  		<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
	  			<div id="actionResultDisplay"></div>
		  		<div class="section">
		            <div class="section-header">
		            	<strong>
		            		<span class="ui-icon icon-ttl-section-info-edit"></span>
		            		<s:text name="global.page.title"/><%-- 基本信息 --%>
		            	</strong>
		            </div>
		            <div id="base_info" class="section-content">
		            	<s:hidden name="coderID" value="%{coderID}" />				
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
				                <td><span><s:textfield name="codeKey" cssClass="text" maxlength="4" value="%{coderDetail.codeKey}"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value1"/></th><%-- Value1 --%>
				                <td><span><s:textfield name="value1" cssClass="text" maxlength="20" value="%{coderDetail.value1}"/></span></td>
				                <th><s:text name="value2"/></th><%-- Value2--%>
				                <td><span><s:textfield name="value2" cssClass="text" maxlength="20" value="%{coderDetail.value2}"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value3"/></th><%-- value3--%>
				                <td><span><s:textfield name="value3" cssClass="text" maxlength="20" value="%{coderDetail.value3}"/></span></td>
				                <th><s:text name="grade"/></th><%-- 级别--%>
				                <td><span><s:textfield name="grade" cssClass="text" value="%{coderDetail.grade}"/></span></td>				                
			             	</tr>
			             	<tr>
				                <th><s:text name="codeOrder"/></th><%-- 显示顺序--%>
				                <td><span><s:textfield name="codeOrder" cssClass="text" value="%{coderDetail.codeOrder}"/></span></td>
			             	</tr>			             	
		         		</table>
	                </div>
	            </div>
	           	<%-- 操作按钮 --%>
	            <div id="saveButton" class="center clearfix">
          			<button class="save" onclick="plscf13_update('${save_url}');return false;">
	              		<span class="ui-icon icon-save"></span>
	              		<span class="button-text"><s:text name="global.page.save"/></span>
	              	</button>
	          		<button id="back" class="back" type="button" onclick="plscf13_doBack()">
		            	<span class="ui-icon icon-back"></span>
		            	<span class="button-text"><s:text name="global.page.back"/></span>
		            </button>
		            <button id="close" class="close" type="button"  onclick="doClose();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
            </cherry:form>
	  	</div>
	</div>
</div>

</div>
</s:i18n>