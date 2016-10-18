<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/bs/emp/BINOLBSEMP06.js"></script>
<%-- BA模板下载URL --%>
<s:url id="downLoad_url" value="/download/BA导入模板.xls"/>
<%-- BA批量导入URL --%>
<s:url id="import_url" value="/basis/BINOLBSEMP06_import"/>
<s:i18n name="i18n.bs.BINOLBSEMP06">
    <div class="panel-header">
        <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
    </div>
    <%-- ================== 信息提示区 START ======================= --%>
	<div id="errorMessage"></div>
	<%-- ================== 信息提示区   END  ======================= --%>
    <div class="panel-content">
        <div class="section-content">
        	<cherry:form id="mainForm" class="inline">
        		<div>
					<span class="highlight"><s:text name="EMP06_snow"/></span><s:text name="EMP06_notice"/>
					<a href="${downLoad_url }"><s:text name="EMP06_download"/></a>
					<s:if test="brandInfoList.size() > 1">
					<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></s:select>
					</s:if>
					<s:else><s:hidden name="brandInfoId" id="brandInfoId"></s:hidden></s:else>
				</div>
				<table class="detail" cellpadding="0" cellspacing="0">
		            <tr>
		                <td>
		                	<input class="input_text" type="text" id="pathExcel" name="pathExcel" />
							<input type="button" value="<s:text name="global.page.browse"/>"/>
		                	<input type="file" name="upExcel" id="upExcel" class="input_file"  size="33" onchange="pathExcel.value=this.value;BINOLBSEMP06.deleteActionMsg();return false;"/>
		        			<input type="button" value="<s:text name="EMP06_importExcel_btn"/>" onclick="BINOLBSEMP06.ajaxFileUpload('${import_url}');return false;" id="upload"/>
		        			<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
		                </td>
		            </tr>
		        </table>
        	</cherry:form>
        </div>
        <div id="errorBAShow" style="display:none;">
        <div id="section" class="section">
	    	<div class="section-header">
	        	<strong>
	        		<span class="ui-icon icon-ttl-section-search-result"></span>
	        		<s:text name="EMP06_warm1"/><span class="highlight" id="showBasError"><s:text name="EMP06_warm2"/></span><s:text name="EMP06_warm3"/>
	        	</strong>
	        	<span class="highlight">※</span><span class="breadcrumb">（<s:text name="EMP06_clickTip"></s:text>）</span>
	        </div>
	        <div class="section-content" style="overflow-x:auto;width:100%;">
	        	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
			        <thead>
			          <tr>
			            <%-- 所属品牌 --%>
			            <th><s:text name="EMP06_brandName"/></th>
			            <%-- BA号 --%>
			            <th><s:text name="EMP06_BACode"/></th>
			            <%-- BA名称 --%>
			            <th><s:text name="EMP06_BAName"/><span class="ui-icon ui-icon-document"></span></th>
			            <%--所属柜台 --%>
			            <th><s:text name="EMP06_counter"/></th>
			            <%--手机号--%>
			            <th><s:text name="EMP06_mobilePhone"/></th>
			            <%--入职日期 --%>
			            <th><s:text name="EMP06_commtDate"/></th>
			            <%--离职日期 --%>
			            <th><s:text name="EMP06_depDate"/></th>
			            <%--错误原因 --%>
			            <th><s:text name="EMP06_errorReason"/></th>
			            <%-- 操作 --%>
			            <th><s:text name="EMP06_option"/></th>
			          </tr>
			        </thead>
			        <tbody id="errorBA">
			        </tbody>           
			      </table>
	        </div>
	    </div>
	    </div>
    </div>
    
    <%-- 错误信息 --%>
    <div id="errMsg">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
    </div>
    <div id="hiddenTable" class="">
    </div>
</s:i18n>
<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>