<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/cnt/BINOLBSCNT06.js"></script>
<%-- 柜台模板下载URL --%>
<s:url id="downLoad_url" value="/download/柜台导入模板.xls"/>
<%-- 柜台批量导入URL --%>
<s:url id="import_url" value="/basis/BINOLBSCNT06_import"/>
<s:i18n name="i18n.bs.BINOLBSCNT01">
    <div class="panel-header">
        <div class="clearfix"> 
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
		<s:url id="export" action="BINOLBSCNT06_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
        </div>
    </div>
    <%-- ================== 信息提示区 START ======================= --%>
	<div id="errorMessage"></div>
	<%-- ================== 信息提示区   END  ======================= --%>
    <div class="panel-content">
        <div class="section-content">
        	<cherry:form id="mainForm" class="inline">
        		<div>
					<span class="highlight"><s:text name="counter.snow"/></span><s:text name="counter.notice"/>
					<a href="${downLoad_url }"><s:text name="counter.download"/></a>
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
		                	<input type="file" name="upExcel" id="upExcel" class="input_file"  size="33" onchange="pathExcel.value=this.value;BINOLBSCNT06.deleteActionMsg();return false;"/>
		        			<input type="button" value="<s:text name="counter.importExcel_btn"/>" onclick="BINOLBSCNT06.ajaxFileUpload('${import_url}');return false;" id="upload"/>
		        			<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
		                </td>
		            </tr>
		        </table>
        	</cherry:form>
        </div>
        <div id="errorCountersShow" style="display:none;">
        <div id="section" class="section">
	    	<div class="section-header">
	        	<strong>
	        		<span class="ui-icon icon-ttl-section-search-result"></span>
	        		<s:text name="counter.warm1"/><span class="highlight" id="showBasError"><s:text name="counter.warm2"/></span><s:text name="counter.warm3"/>
	        	</strong>
	        	<span class="highlight">※</span><span class="breadcrumb">（<s:text name="counter.clickTip"></s:text>）</span>
	        	<a id="export" class="export" onclick="BINOLBSCNT06.exportExcel();return false;">
                   <span class="ui-icon icon-export"></span>
                   <span class="button-text"><s:text name="global.page.export"/></span>
                </a>
	        </div>
	        <div class="section-content" style="overflow-x:auto;width:100%;">
	        	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" style="width:100%;">
			        <thead>
			          <tr>
			            <%-- 所属品牌 --%>
			            <th><s:text name="counter.brandNameChinese"/></th>
			            <%-- 柜台号 --%>
			            <th><s:text name="counter.counterCode"/><span class="ui-icon ui-icon-document"></span></th>
			            <%-- 柜台名称 --%>
			            <th><s:text name="counter.counterNameIF"/></th>
			            <%-- 柜台类型--%>
			            <th><s:text name="counter.testType"/></th>
			            <%-- 柜台主管 --%>
			            <th><s:text name="counter.counterHeader"/></th>
			            <%-- 所属大区 --%>
			            <th><s:text name="counter.region"/></th>
			            <%-- 所属省份 --%>
			            <th><s:text name="counter.province"/></th>
			            <%-- 所属城市 --%>
			            <th><s:text name="counter.city"/></th>
			            <%-- 柜台分类 --%>
			            <th><s:text name="counter.category"/></th>
			            <%-- 所属系统--%>
			            <th><s:text name="counter.belongFaction"/></th>
			            <%-- 渠道 --%>
			            <th><s:text name="counter.channelName"/></th>
			            <%-- 经销商--%>
			            <th><s:text name="counter.reseller"/></th>
			            <%-- 商场名称 --%>
			            <th><s:text name="counter.mall"/></th>
			            <%-- 柜台英文名称 --%>
			            <th><s:text name="counter.nameForeign"/></th>
			            <%-- 柜台地址 --%>
			            <th><s:text name="counter.counterAddress"/></th>
			            <%-- 柜台员工数--%>
			            <th><s:text name="counter.employeeNum"/></th>
			            <%-- 柜台面积 --%>
			            <th><s:text name="counter.space"/></th>
			            <%-- 柜台电话 --%>
			            <th><s:text name="counter.telephone"/></th>
			            <%--柜台状态 --%>
			            <th><s:text name="counter.status"/></th>
			            <%--柜台级别 --%>
			            <th><s:text name="counter.counterLevel"/></th>
			            <%--是否有POS机 --%>
			            <th><s:text name="counter.posFlag"/></th>
			            <%-- 经度 --%>
			            <th><s:text name="counter.longitude"/></th>
			            <%-- 纬度 --%>
			            <th><s:text name="counter.latitude"/></th>
			            <%-- 柜台类型 --%>
			            <th><s:text name="counter.managingType2"/></th>
			            <%-- 银联设备号 --%>
			            <th><s:text name="counter.equipmentCode"/></th>
			         	<%-- 错误原因 --%>
			            <th><s:text name="counter.error"/></th>
			            <%-- 操作 --%>
			            <th><s:text name="counter.option"/></th>
			          </tr>
			        </thead>
			        <tbody id="errorCounters">
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
