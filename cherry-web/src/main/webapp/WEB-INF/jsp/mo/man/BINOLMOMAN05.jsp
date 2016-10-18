<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css"
	type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN05.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript">
	if (window.opener) {
	    window.opener.lockParentWindow();
	}
</script>
<s:i18n name="i18n.mo.BINOLMOMAN05">
<s:text name="MAN05_filePath" id="filePath"/>
<s:url id="downloadUrl" value="%{filePath}"/>
<s:url id="importUdisk_url" value="/mo/BINOLMOMAN05_importUdisk"/>
<s:url id="getInformation_url" value="/mo/BINOLMOMAN05_getInformation"/>
<s:url id="saveUdisk_url" value="/mo/BINOLMOMAN05_saveUdisk"/>
<div class="panel ui-corner-all">
<div class="panel-header">
        <div class="clearfix"> 
            <span class="breadcrumb left"> 
                <span class="ui-icon icon-breadcrumb"></span>
                <%-- 机器管理 --%>
                <s:text name="MAN05_manage"/>
                &gt; 
                <%-- 添加U盘 --%>
                <s:text name="MAN05_title"/>
            </span>  
        </div>
    </div>
    <div id="actionResultDisplay"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
     <div id="errorDiv" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div class="section-content">
                <form id="mainForm" class="inline">
			      <table id="udiskDetail" class="detail" cellpadding="0" cellspacing="0">
			        <tr>
			            <th>
			                <%-- 所属品牌 --%>
			                <span><s:text name="MAN05_brandName"/></span>
			            </th>
			            <td>
			                <span>
			                   <s:if test="%{brandInfoList.size()> 1}">
			                    	<s:text name="MAN05_select" id="MAN05_select"/>
			                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" headerKey="" headerValue="%{#MAN05_select}" cssStyle="width:100px;"></s:select>
			                	</s:if><s:else>
			                    	<s:text name="MAN05_select" id="MAN05_select"/>
			                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select>      	
			                	</s:else>
			                </span>
			            </td>
			            <th></th>
			            <td></td>
			        </tr>
			        <tr>
			            <th>
			                <%-- U盘SN --%>
			                <s:text name="MAN05_udiskSn"/>
			                <span class="highlight"><s:text name="global.page.required"/></span>
			            </th>
			            <td>
			                <span>
			                    <s:textfield name="udiskSn" cssClass="text" maxlength="100" id="udiskSn"/>
			                </span>
			            </td>
			            <th>
			                <%-- 员工姓名 --%>
			                <s:text name="MAN05_employeeCode"/>
			            </th>
			            <td>
			                <span><s:textfield name="employeeCode" maxlength="15" cssClass="text" id="employeeCode" onclick="binOLMOMAN05.popDataTableOfEmployee(this,$('#brandInfoId').serialize());return false;"/></span>
			            </td>
			        </tr>
			    </table>
			    <div class="center clearfix" style="border-bottom:1px #ccc dashed;padding-bottom:10px;">
			    <a id="add" class="add" onclick="binOLMOMAN05.getInformation();return false;">
			                    <span class="ui-icon icon-add"></span>
			                    <span class="button-text"><s:text name="MAN05_add"/></span>
			                </a>
			    </div>
			    <div class="toolbar clearfix">
        <strong>
            <span class="left">
                <span class="ui-icon icon-ttl-section-info"></span>
                <s:text name="MAN05_importExcel_title"/>
            </span>
        </strong>
    </div>
    <div>
        <span class="highlight"><s:text name="MAN05_snow"/></span>
        <s:text name="MAN05_notice"/>
        <a href="${downloadUrl}"><s:text name="MAN05_download"/></a>
    </div>
     <div>
        <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
                <td>
                	<input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
				    <input type="button" value="<s:text name="global.page.browse"/>"/>
				    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;return false;" /> 
				    <input type="hidden" value="" name="csrftoken" id="csrftoken">
				    <input type="button" value="<s:text name="MAN05_importExcel_btn"/>" onclick="binOLMOMAN05.ajaxFileUpload()" id="test" /> 
				    <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
                </td>
            </tr>
        </table>
    </div>
    <table id="udiskArr" class="editable detail"> 
        <tbody style="">
            <tr>
                <th class="center"><s:text name="MAN05_udiskSn"/></th>
                <th class="center" style="width:100px"><s:text name="MAN05_employeeCode"/></th>
                <th class="center"><s:text name="MAN05_employeeName"/></th>
                <th class="center" style="min-width:100px"><s:text name="MAN05_position"/></th>
                <th class="center"><s:text name="MAN05_operation"/></th>
            </tr>
    	</tbody>
    </table>
                </form>
            </div>
        <hr class="space" />
	        <div class="center clearfix">
	        	<%-- 保存 --%>
	        	<button class="save" onclick="binOLMOMAN05.saveUdisk();"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"/></span></button>
	        </div>
     	<hr class="space" />    
    </div>
</div>
<span id="importUdisk" style="display:none">${importUdisk_url}</span>
<span id="getInformation" style="display:none">${getInformation_url}</span>
<span id="saveUdisk" style="display:none">${saveUdisk_url}</span>

<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg3" value='<s:text name="EMO00010"/>'/>
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00045"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="EMO00047"/>'/>
    
</div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/mo/common/BINOLMOCM01.jsp" flush="true" />