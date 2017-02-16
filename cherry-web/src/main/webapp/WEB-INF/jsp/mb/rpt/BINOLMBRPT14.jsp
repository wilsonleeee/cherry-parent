<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/rpt/BINOLMBRPT14.js"></script>
<s:i18n name="i18n.mb.BINOLMBRPT14">
    <s:url id="search_url" value="/mb/BINOLMBRPT14_search"/>
    <div class="hide">
        <a id="searchUrl" href="${search_url}"></a>
    </div>
    <div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
    </div>
    <div id="errorMessage"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div id="actionResultDisplay"></div>
    <div class="panel-content">
        <div class="box">
            <cherry:form id="mainForm" class="inline">
                <div class="box-header">
                    <strong>
                        <span class="ui-icon icon-ttl-search"></span>
                            <%-- 查询条件 --%>
                        <s:text name="MBRPT14_condition"/>
                    </strong>
                </div>
                <div class="box-content clearfix">
                    <div class="column" style="width:49%;">
                        <p>
                        <p><%-- 手机号 --%>
                            <label  style="width: 120px;"><s:text name="MBRPT14_mobilePhone" /></label>
                            <s:textfield name="mobilePhone" cssClass="text" maxlength="15"/>
                        </p>
                        </p>
                    </div>
                    <div class="column last" style="width:50%;">
                        <p>
                            <label><s:text name="MBRPT14_completeDegree" /></label>
                            <span  style="width:50px;">
                                <input type="text" id="completeStart" name ="completeStart"  style="width:50px;"/>%</span>
                            <s:text name="MBRPT14_to" />
                            <span  >
                                 <input type="text" id="completeEnd" name ="completeEnd"  style="width:50px;"/>%</span>
                        </p>
                    </div>
                </div>
                <p class="clearfix">
                        <%-- 查询 --%>
                    <button class="right search" onclick="BINOLMBRPT14.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <span class="button-text"><s:text name="MBRPT14_search"/></span>
                    </button>
                </p>
            </cherry:form>
        </div>
            <%-- ====================== 结果一览开始 ====================== --%>
        <div id="section" class="section hide">
            <div class="section-header">
                <strong>
                    <span class="ui-icon icon-ttl-section-search-result"></span>
                    <s:text name="global.page.list"/>
                </strong>
            </div>
            <div class="section-content">
                <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
                    <thead>
                    <tr>
                        <th><s:text name="MBRPT14_no"/></th>
                        <th><s:text name="MBRPT14_mobilePhone"/></th>
                        <th><s:text name="MBRPT14_memberName"/></th>
                        <th><s:text name="MBRPT14_completeDegree"/></th>
                    </tr>
                    </thead>
                </table>
            </div>
        </div>
    </div>
    <%-- ====================== 结果一览结束 ====================== --%>

</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
