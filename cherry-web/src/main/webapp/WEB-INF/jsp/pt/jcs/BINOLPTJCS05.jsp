<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS05.js"></script>
<%-- 产品模板下载URL --%>
<s:url id="downLoad_url" value="/download/产品导入模板.xls"/>
<%-- 产品批量导入URL --%>
<s:url id="import_url" value="/pt/BINOLPTJCS05_import"/>
<s:i18n name="i18n.pt.BINOLPTJCS05">
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
					<span class="highlight"><s:text name="JCS05_snow"/></span><s:text name="JCS05_notice"/>
					<a href="${downLoad_url }"><s:text name="JCS05_download"/></a>
					<s:if test="brandInfoList.size() > 1">
					<s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"></s:select>
					</s:if>
					<s:else><input type="hidden" id="brandInfoId" name="brandInfoId" value='<s:property value="brandInfoId"/>'/></s:else>
				</div>
        		<span>
        			<input class="input_text" type="text" id="pathExcel" name="pathExcel" />
					<input type="button" value="<s:text name="global.page.browse"/>"/>
        			<input type="file" name="upExcel" id="upExcel" class="input_file" size="33" onchange="pathExcel.value=this.value;BINOLPTJCS05.deleteActionMsg();return false;"/>
        			<input type="button" value="<s:text name="JCS05_importExcel_btn"/>" onclick="BINOLPTJCS05.ajaxFileUpload('${import_url}');return false;" id="upload"/>
        			<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">  
        		</span> 
        	</cherry:form>
        </div>
    </div>
    <%-- 错误信息 --%>
    <div id="errMsg">
    	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/><%-- 请选择上传文件。 --%>
    </div>
</s:i18n>