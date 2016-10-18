<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%--业务流程配置 --%>
<script type="text/javascript" src="/Cherry/js/pl/scf/BINOLPLSCF15.js"></script>
<s:url id="edit_url" value="/pl/BINOLPLCOM02_editInit"></s:url>
<s:url id="init_url" value="/pl/BINOLPLCOM02_init"></s:url>
<s:url id="show_url" value="/pl/BINOLPLCOM03_init"></s:url>
<s:url id="changeBrand_url" value="/pl/BINOLPLSCF15_changeBrand"></s:url>
<div class="hide">
    <a id="changeBrand_url" href="${changeBrand_url}"></a>
</div>
<s:i18n name="i18n.pl.BINOLPLSCF15">
    <div class="panel-header">
        <div class="clearfix">
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span> 
        </div>
    </div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="actionResultDisplay" class="hide"></div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
        <div class="box3">
            <%-- 所属品牌 --%>
            <label><s:text name="SCF15_brand"></s:text></label>
            <s:if test='null != brandInfoList && brandInfoList.size()>0'>
                <s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" onchange="binOLPLSCF15.changeBrand();return false;" headerKey="" cssStyle="width:100px;"></s:select>
            </s:if>
        </div>
    
        <div id="section" class="section">
            <%--业务流程一览 --%>
            <div class="section-header clearfix">
                <strong class="left">
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <s:text name="SCF15_list"/>
                </strong>
            </div>
            <div class="section-content">
                <div class="toolbar clearfix">
                    <span class="left">
                    
                    </span>
                </div>
                <cherry:form id="mainForm">
                    <div style="overflow-x:auto;overflow-y:hidden;">
                        <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%;">
                            <thead>
                                <tr>
                                    <th style="width: 3%;"><s:text name="SCF15_num"/></th>
                                    <th style="width:70%;"><s:text name="SCF15_flowName"/></th>
                                    <th style="width:10%;"><s:text name="SCF15_status"/></th>
                                    <th style="width: 10%;" class="center"><s:text name="SCF15_option"/></th>
                                </tr>
                            </thead>
                            <tbody id="dataTable">
                                <s:iterator value="workflowInfoList" status="status">
                                    <tr class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" >
                                        <td>${status.index + 1}</td>
                                        <td><span><s:property value="Value"/></span></td>
                                        <td>
                                            <s:if test='"1".equals(configStatus)'>
                                                <s:text name="SCF15_custom"/>
                                            </s:if>
                                            <s:else>
                                                <s:text name="SCF15_default"/>
                                            </s:else>
                                        </td>
                                        <td>
                                            <input type="hidden" id="flowType" name="flowType" value="<s:property value="CodeKey"/>"/>
                                            <a href="${init_url}" class="add" name="edit" onclick="binOLPLSCF15.popupWin(this);return false;" style="margin-top:-3px;">
	                                            <span class="ui-icon icon-edit"></span>
	                                            <span class="button-text"><s:text name="SCF15_edit"/></span>
                                            </a>
                                            <a href="${show_url}" class="delete" name="edit" onclick="binOLPLSCF15.popupWin(this);return false;" style="margin-top:-3px;">
                                                <span class="ui-icon ui-icon-document"></span>
                                                <span class="button-text"><s:text name="SCF15_detail"/></span>
                                            </a>
                                        </td>
                                    </tr>
                                </s:iterator>
                            </tbody>
                        </table>
                    </div>
                </cherry:form>
            </div>
        </div>
    </div>
    <div class="hide">
        <input type="hidden" id="status_custom" value='<s:text name="SCF15_custom"/>'/>
        <input type="hidden" id="status_default" value='<s:text name="SCF15_default"/>'/>
    </div>
</s:i18n>
