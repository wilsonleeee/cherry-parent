<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF11.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 保存code类别URL --%>
<s:url id="save_url" value="/pl/BINOLPLSCF11_saveCoder"></s:url>

<s:i18n name="i18n.pl.BINOLPLSCF07">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="code_query"/> &gt; <s:text name="code_add"/> 
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
	  	<div class="panel-content">
			<%--code值查询 form area --%>
	        <cherry:form id="toCoderQueryForm" class="inline" action="BINOLPLSCF10_init" csrftoken="false">
	        	<s:hidden name="codeManagerID" value="%{codeManagerID}"/>
	        	<s:hidden name="orgCode" value="%{orgCode}"/>
	        	<s:hidden name="brandCode" value="%{brandCode}"/>
	        	<s:hidden name="orgName" value="%{orgName}"/>
	        	<s:hidden name="brandName" value="%{brandName}"/>
	        	<s:hidden name="codeType" value="%{codeType}"/>
	        	<input type="hidden" id="parentCsrftoken1" name="csrftoken"/>	
			</cherry:form>
	  	
	  		<cherry:form id="mainForm" method="post" action="BINOLPLSCF11_saveCoder" class="inline" csrftoken="false">
	  			<div id="actionResultDisplay"></div>
		  		<div class="section">
		            <div class="section-header">
		            	<strong>
		            		<span class="ui-icon icon-ttl-section-info-edit"></span>
		            		<s:text name="global.page.title"/><%-- 基本信息 --%>
		            	</strong>
		            </div>
		            <div id="base_info" class="section-content">
		            	<s:hidden name="codeManagerID" value="%{codeManagerID}"/>
			        	<s:hidden name="orgCode" value="%{orgCode}"/>
			        	<s:hidden name="brandCode" value="%{brandCode}"/>
			        	<s:hidden name="codeType" value="%{codeType}"/>
			        	<input type="hidden" id="parentCsrftoken2" name="csrftoken"/>	
		                <table class="detail" cellpadding="0" cellspacing="0">
			             	<tr>
				                <th><s:text name="orgInfo"/></th><%-- 组织--%>
				                 <td><span><s:text name="orgName"/></span></td>
				                <th><s:text name="brandInfo"/></th><%-- 品牌--%>
				                 <td><span><s:text name="brandName"/></span></td>					                 
			             	</tr>
		                	
			              	<tr>
				                <th><s:text name="codeType"/><span class="highlight"></span></th><%--Code类别 --%>
				                <td><span><s:text name="codeType"/></span></td>
				                <th><s:text name="codeKey"/><span class="highlight"></span></th><%-- CodeKey--%>
				                <td><span><s:textfield name="codeKey" cssClass="text" maxlength="4"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value1"/></th><%-- Value1 --%>
				                <td><span><s:textfield name="value1" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="value2"/></th><%-- Value2--%>
				                <td><span><s:textfield name="value2" cssClass="text" maxlength="20"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value3"/></th><%-- value3--%>
				                <td><span><s:textfield name="value3" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="grade"/></th><%-- 级别--%>
				                <td><span><s:textfield name="grade" cssClass="text"/></span></td>				                
			             	</tr>
			             	<tr>
				                <th><s:text name="codeOrder"/></th><%-- 显示顺序--%>
				                <td><span><s:textfield name="codeOrder" cssClass="text"/></span></td>
			             	</tr>
			             	
		         		</table>
	                </div>
	            </div>
	           	<%-- 操作按钮 --%>
	            <div id="saveButton" class="center clearfix">
          			<button class="save" onclick="plscf11_saveCoder('${save_url}');return false;">
	              		<span class="ui-icon icon-save"></span>
	              		<span class="button-text"><s:text name="global.page.save"/></span>
	              	</button>
	          		<button id="back" class="back" type="button" onclick="plscf11_doBack()">
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