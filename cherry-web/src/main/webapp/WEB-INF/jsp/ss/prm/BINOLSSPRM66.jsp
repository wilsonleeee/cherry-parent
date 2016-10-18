<%--入库单导入 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM66.js"></script>

<s:i18n name="i18n.ss.BINOLSSPRM66">
	<div class="hide">
		<%--导入初始画面URL --%>
		<s:url namespace="/ss" action="BINOLSSPRM66_importInit" id="importInit_url" />
		<s:url namespace="/ss" action="BINOLSSPRM66_search" id="searchUrl" />
		<a id="search_Url" href="${searchUrl }" ></a>
	</div>
	<cherry:form id="mainForm">
		<s:text name="global.page.all" id="select_default"/>
		<div class="panel-header">
			<div class="clearfix"> 
				<span class="breadcrumb left">	    
					<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
				</span>
				<span class="right"> 
					<cherry:show domId="SSPRM66IMPORT">
						<a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
							<span class="button-text"><s:text name="SSPRM66_import"></s:text></span>
							<span class="ui-icon icon-add"></span>
						</a>
					</cherry:show>
				</span>
			</div>
		</div>
		<div id="errorMessage"></div>
		<div id="actionResultDisplay"></div>
		<div class="panel-content clearfix">
			<div class="box">
				<div class="box-header">
					<strong>
						<span class="ui-icon icon-ttl-search"></span>
						<s:text name="global.page.condition"/>
					</strong>
				</div>
				<div class="box-content clearfix">
					<div class="column" style="width: 50%; height: auto;">
						<p>
							<label><s:text name="SSPRM66_importBatch" /></label>
							 <s:textfield name="importBatchCode" cssClass="text" maxlength="25" />
						</p>
					</div>
					<div class="column last" style="width: 49%; height: auto;">
						<p>
							<label><s:text name="SSPRM66_importDate" /></label>
							<span>
							<s:textfield id="importStartTime" name="importStartTime" cssClass="date"/>
							</span> - <span>
							<s:textfield id="importEndTime" name="importEndTime" cssClass="date"/>
							</span>
						</p>
					</div>
				</div>
				<p class="clearfix">
					<button class="right search" onclick="BINOLSSPRM66.search();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text">
							<s:text name="global.page.search"></s:text>
						</span>
					</button>
				</p>
			</div>
			<div class="section hide" id="prmInDepotExcelInfo">
				<div class="section-header">
					<strong>
						<span class="ui-icon icon-ttl-section-search-result"></span>
						<s:text name="global.page.list"></s:text>
				  	</strong>
				</div>
				<div class="section-content">
					<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="prmInDepotExcelInfoDataTable">
						<thead>
							<tr>
								<th><s:text name="NO."></s:text></th>
								<th><s:text name="SSPRM66_importBatch"></s:text></th>
								<th><s:text name="SSPRM66_importDate"></s:text></th>
								<th><s:text name="SSPRM66_importEmployee"></s:text></th>
								<th><s:text name="SSPRM66_importComments"></s:text></th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</cherry:form>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />

<script>
$(document).ready(function(){
	BINOLSSPRM66.search();
    var holidays = '${holidays }';
    $('#importStartTime').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#importEndTime').val();
            return [value,'maxDate'];
        }
    });
    $('#importEndTime').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#importStartTime').val();
            return [value,'minDate'];
        }
    });
});
</script>