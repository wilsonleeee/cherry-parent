<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<div id ="checkPaperDialog" class="hide">
    <input type="text" class="text" onKeyup ="datatableFilter(this,9);" id="popPaperName"/>
    <a class="search" onclick="datatableFilter('#popPaperName',9);return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.searchfor"/></span></a>
  	<hr class="space" />
    <table id="checkPaper_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
       <thead>
            <tr>
               <th><s:text name="global.page.Popselect"/></th><%--选择 --%>
               <th><s:text name="global.page.paperName"/></th><%--问卷名称--%>
               <th><s:text name="global.page.paperStatus"/></th><%--问卷状态 --%>
            </tr>
        </thead>
        <tbody>
        </tbody>
   </table>
   <div class="center clearfix">
        <button class="confirm" id="checkPaperConfirm"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="global.page.ok"/></span></button>
   </div>
</div>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<s:url action="BINOLCM02_popCheckPaper" namespace="/common" id="popCheckPaperUrl" />
<span id ="popCheckPaperUrl" style="display:none">${popCheckPaperUrl}</span>
<span id ="PoppaperTitle" style="display:none"><s:text name="global.page.PoppaperTitle"/></span><%--考核问卷信息 --%>