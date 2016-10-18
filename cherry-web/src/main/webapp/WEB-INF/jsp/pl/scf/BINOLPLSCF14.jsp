<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF14.js"></script>
<%-- 柜台批量导入URL --%>
<s:url id="import_url" value="/pl/BINOLPLSCF14_import"/>
<%-- 组织查询URL --%>
<s:url id="org_url" value="BINOLPLSCF14_searchOrg"/>
<s:hidden name="org_url" value="%{org_url}" id="orgUrl"/>
<%-- 品牌查询URL --%>
<s:url id="brand_url" value="BINOLPLSCF14_searchBrand"/>
<s:hidden name="brand_url" value="%{brand_url}" id="brandUrl"/>
<%-- 保存文件 --%>
<s:url id="save_url" value="BINOLPLSCF14_saveFile"/>
<s:hidden name="save_url" value="%{save_url}" id="saveUrl"/>
<s:i18n name="i18n.pl.BINOLPLSCF14">
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
        		<table class="detail" cellpadding="0" cellspacing="0">
		            <tr>
		                <td>
		        			<input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
						    <input type="button" value="<s:text name="global.page.browse"/>"/>
						    <input class="input_file" type="file" name="upfile" id="upfile" size="33" onchange="pathExcel.value=this.value;BINOLPLSCF14.deleteActionMsg();return false;" /> 
						    <input type="button" value="<s:text name="import"/>" onclick="BINOLPLSCF14.ajaxFileUpload('${import_url}');" id="upload" /> 
						    <img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display: none;">
        				</td>
        			</tr>
        		</table>
        	</cherry:form>
        </div>
        <div id="fileInfoTable" class="hide section-content"  style="overflow-x:auto;overflow-y:hidden">
        <div style="overflow-x:auto;overflow-y:hidden">
        <table id="dataTable" cellpadding="0" cellspacing="0" border="0" width="100%">
        <thead>
              <tr>              
                <th>
                <input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTableBody');"/></th>
                <%-- 文件名 --%>
                <th><s:text name="fileName"/><span class="ui-icon ui-icon-document"></span></th>
                <%-- 组织代码 --%>
                <th><s:text name="orgCode"/></th>
                <%-- 品牌代码 --%>
                <th><s:text name="brandCode"/></th>
                <%-- 文件类别 --%>
                <th><s:text name="fileCategory"/></th>
                <%-- 文件类型 --%>
                <th><s:text name="fileCode"/></th>
                <%-- 操作 --%>
                <th><s:text name="action"/></th>
              </tr>
              </thead>
        	<tbody id="dataTableBody">
              </tbody>
          </table></div>
          <button type="button" onclick="BINOLPLSCF14.save();return false;" class="save" id="save" style="float:right;margin-top:20px">
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="save"/></span>
              </button>
        </div>
    </div>
    <%-- 错误信息 --%>
    <div id="errMsg" class="hide">
    <div class="actionError">
  		<ul class="errorMessage"><%-- 请选择上次文件 --%>
        <li><span><s:text name="EMO00010"/></span></li>	</ul>
	</div>
    </div>
    <%-- 错误信息 --%>
    <div id="errfileMsg" class="hide">
    <div class="actionError">
  		<ul class="errorMessage"><%-- 格式不正确 --%>
        <li><span><s:text name="PPL00040"/></span></li>	</ul>
	</div>
    </div>
</s:i18n>