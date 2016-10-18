<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/sam/BINOLBSSAM07.js"></script>
<s:i18n name="i18n.bs.BINOLBSSAM07">
	<s:url id="export_url" action="/basis/BINOLBSSAM07_export"/>
	<div class="hide">
		<s:url id="search_url" value="/basis/BINOLBSSAM07_search"/>
		<a id="searchUrl" href="${search_url}"></a>
	</div>
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">      
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		</div>
	</div>
	<div id="actionResultDisplay"></div>
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
	<div class="panel-content">
		<div class="box">
  		<cherry:form id="mainForm" class="inline">
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
        <p>
          <label style="width:80px;"><s:text name="BSSAM07_employeeName"></s:text></label>
            <span>
            	<s:textfield cssClass="text" name="employeeName"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="BSSAM07_departName"></s:text></label>
            <span>
            	<s:textfield cssClass="text" name="departName"></s:textfield>
			</span>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
      	<p>
          <label><s:text name="BASAM07_workDate"></s:text></label>
            <span>
            	<s:textfield cssClass="date" id="startDateTime" name="startDateTime"></s:textfield>-
            	<s:textfield cssClass="date" id="endDateTime" name="endDateTime"></s:textfield>
			</span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLBSSAM07.search();return false;">
	      <span class="ui-icon icon-search-big"></span>
		      <span class="button-text">
		      <s:text name="global.page.search"></s:text>
	      </span>
      </button>
    </p>
    </cherry:form>
</div>
		<div id="bAAttendanceSection" class="section hide">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
					<s:text name="BSSAM07_resultlist"/>
				</strong>
			</div>
			<div class="section-content" id="result_list">
				<div class="toolbar clearfix">
					<a class="export left" onclick="BINOLBSSAM07.exportExcel(this);return false;"  href="${export_url}">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><s:text name="global.page.export"></s:text></span>
					</a>
		   			<span id="headInfo"></span>
					<span class="right">
						<a class="setting">
                            <span class="ui-icon icon-setting"></span>
            	            <span class="button-text">
	                            <s:text name="BSSAM07_colSetting"/>
                            </span>
						</a>
					</span>
				</div>
				<table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
					<thead>
						<tr>
							<th><s:text name="BSSAM07_no"/></th>
							<th><s:text name="BSSAM07_departName"/></th>
							<th><s:text name="BSSAM07_employeeName"/></th>
							<th><s:text name="BASAM07_workDate"/></th>
							<th><s:text name="BSSAM07_attendanceType"/></th>
							<th><s:text name="BSSAM07_attendanceDateTime"/></th>
						</tr>
					</thead>
				</table>
			</div>
		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>



