<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF10.js"></script>
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%-- 保存URL --%>
<s:url id="save_url" value="/pl/BINOLPLSCF11_init"></s:url>

<s:i18n name="i18n.pl.BINOLPLSCF07">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
			        <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="scf_code_title"/> &gt; <s:text name="code_query"/>
		       </span>
		    </div>
	  	</div>
	  	<%-- ================== 错误信息提示 START ======================= --%>
      	<div id="errorMessage"></div>
      	<%-- ================== 错误信息提示   END  ======================= --%>
	  	<div class="panel-content">
			<%--返回code管理值详细 form area --%>	  	
	  		<cherry:form id="toDetailForm" action="BINOLPLSCF07_init" method="post" csrftoken="false">
				<s:hidden name="codeManagerID" value="%{codeManagerID}" />
	        	<input type="hidden" id="parentCsrftoken1" name="csrftoken"/>
	        </cherry:form>
			<%--code值查询 form area --%>
	        <cherry:form id="searchCoderForm" class="inline"  csrftoken="false">
	        	<s:hidden name="codeManagerID" value="%{codeManagerID}"/>
	        	<s:hidden name="orgCode" value="%{orgCode}"/>
	        	<s:hidden name="brandCode" value="%{brandCode}"/>
	        	<s:hidden name="codeType" value="%{codeType}"/>
	        	<input type="hidden" id="parentCsrftoken" name="csrftoken"/>	
			</cherry:form>
			<%--code值添加 form area --%>
	        <cherry:form id="addCoderForm" class="inline" action="BINOLPLSCF11_init" csrftoken="false">
	        	<s:hidden name="codeManagerID" value="%{codeManagerID}"/>
	        	<s:hidden name="orgCode" value="%{orgCode}"/>
	        	<s:hidden name="brandCode" value="%{brandCode}"/>
	        	<s:hidden name="orgName" value="%{orgName}"/>
	        	<s:hidden name="brandName" value="%{brandName}"/>
	        	<s:hidden name="codeType" value="%{codeType}"/>
	        	<input type="hidden" id="parentCsrftoken2" name="csrftoken"/>	
			</cherry:form>
		    <%--code值详细form area --%>
		    <cherry:form id="coderDetailForm" class="inline" action="BINOLPLSCF12_init" csrftoken="false">
		       	<input type="hidden" id="parentCsrftoken3" name="csrftoken"/>
		       	<input type="hidden" id="coderID" name="coderID"/>	
	        	<s:hidden name="codeManagerID" value="%{codeManagerID}"/>
	        	<s:hidden name="orgCode" value="%{orgCode}"/>
	        	<s:hidden name="brandCode" value="%{brandCode}"/>		       	
	        	<s:hidden name="orgName" value="%{orgName}"/>
	        	<s:hidden name="brandName" value="%{brandName}"/>
	        	<s:hidden name="codeType" value="%{codeType}"/>		       	
			</cherry:form>
			
  			<div id="actionResultDisplay"></div>
	  		<div class="section">
		        <%--dataTabel area --%>
		        <div class="section hide" id="coderSection">
		          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span><s:text name="global.page.list" /></strong></div>
		          <div class="section-content">
		            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="dateTable" width="100%">
		              <thead>
		                <tr>
		                  <%-- <th><s:text name="brandInfo" /></th> --%>
		                  <th><s:text name="Code类别" /></th>
		                  <th><s:text name="Key" /></th>
		                  <th><s:text name="值1" /></th>
		                  <th><s:text name="值2" /></th>
		                  <th><s:text name="值3" /></th>
		                  <th><s:text name="级别" /></th>
		                  <th><s:text name="显示顺序" /></th>                  
		                </tr>
		              </thead>
		              <tbody>
		              </tbody>
		            </table>
		          </div>
		        </div>
            </div>

	        <%-- 操作按钮 --%>
           	<div id="saveButton" class="center clearfix">
         		<button class="save" onclick="plscf10_addCoder('${save_url}');return false;">
              		<span class="ui-icon icon-save"></span>
              		<span class="button-text"><s:text name="add_coder"/></span>
              	</button>
          		<button id="back" class="back" type="button" onclick="plscf10_doBack()">
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
	<s:a action="BINOLPLSCF10_coderList" id="coderListUrl"></s:a>

</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
