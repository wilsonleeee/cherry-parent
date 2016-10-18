<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH12.js"></script>
<%-- 发货单（Excel导入）URL --%>
<s:url id="importInit_url" value="/st/BINOLSTSFH12_importInit"/>
<s:i18n name="i18n.st.BINOLSTSFH12">
<cherry:form id="mainForm"  class="inline">
<s:text name="global.page.all" id="select_default"/>
    <div class="panel-header">
        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<cherry:show domId="BINOLSTSFH12IMP">
				<span class="right"> 
					<a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
						<span class="button-text"><s:text name="SFH12_native2"></s:text></span>
						<span class="ui-icon icon-add"></span>
					</a>
				</span>
			</cherry:show>
        </div>
    </div>
 <div id="errorMessage"></div>
 <div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="box">
  
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
      	<s:if test="brandInfoList != null && brandInfoList.size() > 0">
      		<p>
        		<label><s:text name="SFH12_brandName"/></label>
        		<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="" />
        	</p>
        </s:if>
      	<s:else>
      		<input type="hidden" name="brandInfoId" value="${brandInfoId}"/>
      	</s:else>
      	<p>
          <label style="width:80px;"><s:text name="SFH12_importBatch" /></label>
            <span>
                  <s:textfield name="importBatchCode" cssClass="text" maxlength="25"></s:textfield>
			</span>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
        <p class="date">
        	<label><s:text name="SFH12_importDate"/></label>
            <span>
            	<s:textfield id="importStartTime" name="importStartTime" cssClass="date"/>
            </span> - 
            <span>
            	<s:textfield id="importEndTime" name="importEndTime" cssClass="date"/>
            </span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLSTSFH12.search();return false;">
	      <span class="ui-icon icon-search-big"></span>
		      <span class="button-text">
		      <s:text name="global.page.search"></s:text>
	      </span>
      </button>
    </p>
</div>
<div class="section hide" id="prtDeliverExcelInfo">
  <div class="section-header">
  	<strong>
	  	<span class="ui-icon icon-ttl-section-search-result"></span>
	  	<s:text name="global.page.list"></s:text>
  	</strong>

  </div>

  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="prtDeliverExcelInfoDataTable">
      <thead>
        <tr>
          <th><s:text name="NO."></s:text></th>
          <th><s:text name="SFH12_importBatch"></s:text></th>
          <th><s:text name="SFH12_importDate"></s:text></th>
          <th><s:text name="SFH12_employee"></s:text></th>
          <th><s:text name="SFH12_comments"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
<div class="hide">
<s:url action="BINOLSTSFH12_search" id="searchUrl"></s:url>
<a href="${searchUrl}" id="search_Url"></a>
</div>
 </cherry:form>
</s:i18n>  
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
 <script type="text/javascript">
	 	$(document).ready(function() {
	 		BINOLSTSFH12.search();
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