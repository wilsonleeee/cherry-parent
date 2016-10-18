<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<s:i18n name="i18n.mo.BINOLMOMAN03">
<div class="hide">
    <s:url id="search_url" value="/mo/BINOLMOMAN03_getCounterList"/>
    <a id="searchUrl" href="${search_url}"></a>
</div>
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<h2><s:text name="MAN03_title"/></h2>
</div>
<div id="actionResultDisplay"></div>
<form id="bindCounterForm" onsubmit="BINOLMOMAN03.moman03_bind();return false;" >
<!--<s:hidden name="machineInfoId" value="%{#request.machineInfoId}"></s:hidden>-->
<s:hidden name="machineCode" value="%{#request.machineCode}"></s:hidden>
<s:hidden name="machineCodeOld" value="%{#request.machineCodeOld}"></s:hidden>
<s:hidden name="brandInfoId" value="%{#request.brandInfoId}"></s:hidden>
<div class="panel-content clearfix">
  <div class="section-content">
    <div id ="counterDialog">
	  <input type="text" class="text" id="SearchParam" onKeyup ="datatableFilter(this,0);"/>
	  <a class="search" onclick="datatableFilter($('#SearchParam'),0)"><span class="ui-icon icon-search"></span><span class="button-text">查找</span></a>
	  <hr class="space" />
    </div>
    <div id="errorMessage"></div>
      <%-- ================== 错误信息提示 START ======================= --%>
      <div id="errorDiv2" style="display:none">
        <div class="actionError">
          <ul>
             <li><span id="errorSpan2"></span></li>
          </ul>
        </div>
      </div>
      <%-- ================== 错误信息提示   END  ======================= --%>
    <input type="radio" name="counterInfoId" value=""/><s:text name="MAN03_anyCounter"/>
	<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	  <thead>
	    <tr>
	    <th><s:text name="MAN03_select"/></th>
	    <th><s:text name="MAN03_counterno"/></th>
	    <th><s:text name="MAN03_countername"/></th>
	    </tr>
	  </thead>
	</table>
  </div>
  <div class="center">
    <s:a action="BINOLMOMAN03_bind" id="bind" cssStyle="display: none;"></s:a>
    <button class="save" onclick="BINOLMOMAN03.moman03_bind();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
    <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
  </div>
</div>
</form>
</div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="EMO00002"/>'/>
</div>