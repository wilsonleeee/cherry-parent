<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF07.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 保存code类别URL --%>
<s:url id="save_url" value="/pl/BINOLPLSCF07_save"></s:url>

<s:i18n name="i18n.pl.BINOLPLSCF06">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="add_code"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<div id="errorDiv" class="actionError" style="display:none">
            <ul>
                <li><span id="errorSpan"></span></li>
            </ul>         
        </div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
	  	<div class="panel-content">
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
		                	<s:if test="%{orgInfoList != null}">
				             	<tr>
					                <th><s:text name="orgInfo"/></th><%-- 组织--%>
                  					<s:url action="BINOLPLSCF06_searchBrand" id="searchBrandUrl"></s:url> 					                
					                 <td><span><s:select cssStyle="width:100px;" list="orgInfoList" listKey="orgCode" listValue="orgName" name="orgCode" onchange="plscf07_searchBrand('%{#searchBrandUrl}',this,'%{#select_default}');"></s:select></span></td>
					                <th><s:text name="brandInfo"/></th><%-- 品牌--%>
					                 <td><span><s:select cssStyle="width:100px;" list="brandInfoList" listKey="brandCode" listValue="brandName" name="brandCode" id="brandInfoId"></s:select></span></td>					                 
				             	</tr>
			             	</s:if>
		                	
			              	<tr>
				                <th><s:text name="codeType"/><span class="highlight">*</span></th><%--Code类别 --%>
				                <td><span><s:textfield name="codeType" cssClass="text" maxlength="4" onchange="changeCodeType();"/></span></td>
				                <th><s:text name="codeName"/><span class="highlight"></span></th><%-- Code类别名称 --%>
				                <td><span><s:textfield name="codeName" cssClass="text" maxlength="20"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="keyDescription"/></th><%-- Key说明 --%>
				                <td><span><s:textfield name="keyDescription" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="value1Description"/></th><%-- Value1说明--%>
				                <td><span><s:textfield name="value1Description" cssClass="text" maxlength="20"/></span></td>
			             	</tr>
			             	<tr>
				                <th><s:text name="value2Description"/></th><%-- Value2说明--%>
				                <td><span><s:textfield name="value2Description" cssClass="text" maxlength="20"/></span></td>
				                <th><s:text name="value3Description"/></th><%-- Value3说明--%>
				                <td><span><s:textfield name="value3Description" cssClass="text" maxlength="20"/></span></td>				                
			             	</tr>
			             	
		         		</table>
	                </div>
	            </div>
	           
            </form>
            <div class="section">
		        <%--dataTabel area --%>
		        <div class="section" id="coderSection">
		          <div class="section-header">
			          <strong><span class="ui-icon icon-ttl-section-search-result"></span>
			          <s:text name="code_add" />
			          </strong>
		          </div>
		          <div class="section-content">
		          
		            <form id="addListInfo" method="post" class="inline">
		            <div class="toolbar clearfix">
			           <a id="addBtn" class="add" onclick="scf07AddNewCode();return false;" >
		                		<span class="ui-icon icon-add"></span>
		                		<span class="button-text"><s:text name="add_coder"/></span>
	               	 	   </a>
			          </div>
		            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table"  width="100%" id="editListTable">
		              <thead>
		              	<tr>
		                  <%-- <th><s:text name="brandInfo" /></th> --%>
		                  <th class="center" style="width:10%"><s:text name="codeType" /></th>
		                  <th class="center" style="width:9%"><s:text name="codeKey" /></th>
		                  <th class="center" style="width:18%"><s:text name="value1" /></th>
		                  <th class="center" style="width:18%"><s:text name="value2" /></th>
		                  <th class="center" style="width:18%"><s:text name="value3" /></th>
		                  <th class="center" style="width:9%"><s:text name="grade" /></th>
		                  <th class="center" style="width:9%"><s:text name="codeOrder" /></th>
		                  <th class="center" style="width:9%"><s:text name="SCF06_option" /></th>                  
		                </tr>
		              </thead>
		              <tbody></tbody>
		            </table>
		            </form>
		          </div>
		        </div>
            </div>
            <%-- 操作按钮 --%>
	            <div id="saveButton" class="center clearfix">
          			<button class="save" onclick="plscf07_saveCodeM('${save_url}');return false;">
	              		<span class="ui-icon icon-save"></span>
	              		<span class="button-text"><s:text name="global.page.save"/></span>
	              	</button>
		            <button id="close" class="close" type="button"  onclick="doClose();return false;">
		           		<span class="ui-icon icon-close"></span>
		           		<span class="button-text"><s:text name="global.page.close"/></span>
		         	</button>
	            </div>
	  	</div>
	  		
	</div>
</div>


<%--=============================================添加Code的部分Start=================================== --%>

<table id="addNewCode" class="hide">
	<tr>          		
		<td style="width:10%" class="center"><span name="codyType_span"></span></td>
		
		<td class="center"><span>
			<input type="text" style="width:65px" class="text" id="codeKeyArr" value="" maxlength="8" name="codeKeyArr">
			</span>
		</td>	
		
		<td class="center">
		<span>
			<input type="text" style="width:95%" class="text" id="value1Arr" value="" maxlength="20" name="value1Arr">
		</span>
		</td>
		
		<td class="center">
		<span>
			<input type="text" style="width:95%" class="text" id="value2Arr" value="" maxlength="20" name="value2Arr">
		</span>
		</td>
		
		<td>
		<span>
			<input type="text" style="width:95%" class="text" id="value3Arr" value="" maxlength="20" name="value3Arr">
			</span>
		</td>
		
		<td class="center">
		<span>
			<input  type="text" style="width:90%" class="text" id="gradeArr" value="" maxlength="10" name="gradeArr" onchange="scf07IsInteger(this);return false;"/>
			</span>
		</td>
		
		<td class="center">
		<span>
			<input type="text" style="width:90%" class="text" id="codeOrderArr" value="" maxlength="10" name="codeOrderArr" onchange="scf07IsInteger(this);return false;"/>
			</span>
		</td>
		<td class="center">
			<a class="center" style="cursor:pointer" onclick="scf07Delete(this);return false;">
			<s:text name="SCF06_delete"/>
			</a>
		</td>
	</tr>
</table>
<%--=============================================添加Code的部分End=================================== --%>
<div style="display: none;">
	<div id="error1"><s:text name="SCF06_Error1" /></div>
	<div id="error2"><s:text name="SCF06_Error2" /></div>
</div>
</s:i18n>