<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/mo/mup/BINOLMOMUP02.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.mo.BINOLMOMUP02">
    <s:text id="global.select" name="global.page.select"/>
    <s:text name="MUP02_select" id="MUP02_select"/>
    <div class="main container clearfix">
        <div class="panel ui-corner-all">
            <div id="div_main">
                <div class="panel-header">
                    <cherry:form id="toDetailForm" action="BINOLMOMUP02_init" method="post" csrftoken="false">
                        <input type="hidden" name="softwareVersionInfoId" value='<s:property value="softwareVersionInfoId"/>'/>
                        <input type="hidden" name="csrftoken" id="parentCsrftoken"/>
                    </cherry:form>
                    <div class="clearfix">
                        <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="MUP02_title1" />&nbsp;&gt;&nbsp;<s:text name="MUP02_edit"></s:text></span>
                    </div>
                </div>
                <div id="actionResultDisplay"></div>
                <div class="panel-content clearfix">
                    <div class="section">
                        <div class="section-header">
                            <strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="MUP02_baseInfo"></s:text></strong>
                        </div>
                        <div class="section-content">
                            <cherry:form id="update" csrftoken="false" onsubmit="save();return false;">
                                <input type="hidden" name="softwareVersionInfoId" value='<s:property value="softwareVersionInfoId"/>'/>
                                <table class="detail" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <th><s:text name="MUP02_version"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
                                        <td><span><s:textfield name="version" cssClass="text" value="%{softVersionInfoDetail.Version}" maxlength="50"/></span></td>
                                        <th><s:text name="MUP02_downloadUrl"></s:text></th>
                                        <td><span><s:textfield name="downloadUrl" cssClass="text" value="%{softVersionInfoDetail.DownloadURL}" maxlength="100"></s:textfield></span></td>
                                    </tr>
                                    <tr>
                                        <th><s:text name="MUP02_md5Key"></s:text></th>
                                        <td><span><s:textfield name="md5Key" cssClass="text" value="%{softVersionInfoDetail.MD5Key}" maxlength="125"></s:textfield></span></td>
                                        <th><s:text name="MUP02_openUpdateTime"></s:text></th>
                                        <td><span><s:textfield name="openUpdateTime" cssClass="date" value="%{softVersionInfoDetail.OpenUpdateTime}"></s:textfield></span></td>
                                    </tr>
                                </table>
                            </cherry:form>
                        </div>
                    </div>
                    <div class="center clearfix" id="pageButton">
                        <button class="save" onclick="update();return false;">
                            <s:a action="BINOLMOMUP02_update" id="MUP02_update" cssStyle="display: none;"></s:a>
                            <span class="ui-icon icon-save"></span>
                            <span class="button-text"><s:text name="global.page.save"/></span>
                        </button>
                        <button class="close" onclick="window.close();">
                            <span class="ui-icon icon-close"></span>
                            <span class="button-text"><s:text name="global.page.close"/></span><%-- 关闭 --%>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</s:i18n>