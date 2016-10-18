<%-- 柜台消息（Excel导入）主画面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/mo/cio/BINOLMOCIO16.js"></script>
<%-- 柜台消息（Excel导入）URL --%>
<s:url id="importInit_url" value="/mo/BINOLMOCIO16_importInit"/>
<s:i18n name="i18n.mo.BINOLMOCIO16">
<s:text name="global.page.all" id="select_default"/>
<cherry:form id="mainForm"  class="inline">
	<div class="panel-header">
		<div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<cherry:show domId="BINOLMOCIO16IMP">
				<span class="right"> 
					<a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
						<span class="button-text"><s:text name="CIO16_native2"></s:text></span>
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
			        		<label><s:text name="CIO16_brandName"/></label>
			        		<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="" />
			        	</p>
			        </s:if>
			      	<s:else>
			      		<input type="hidden" name="brandInfoId" value="${brandInfoId}"/>
			      	</s:else>
			      	<p>
			          	<label style="width:80px;"><s:text name="CIO16_importBatch" /></label>
			            <span>
			                  <s:textfield name="importBatchCode" cssClass="text" maxlength="25"></s:textfield>
						</span>
			        </p>
			        <p>
			        	<label style="width:80px;"><s:text name="CIO16_importType" /></label>
			            <span>
			            	<s:select list='#application.CodeTable.getCodes("1306")' listKey="CodeKey" listValue="Value" id="isPublish" name="isPublish"  
							headerKey="" cssStyle="width:120px;" headerValue="%{#select_default}"
							onchange="BINOLMOCIO16.search();return false;"></s:select>
			            	
			            </span>
			        </p>
		      	</div>
		      	<div class="column last" style="width:49%; height: auto;">
			        <p class="date">
			        	<label><s:text name="CIO16_importDate"/></label>
			            <span>
			            	<s:textfield id="importStartDate" name="importStartDate" cssClass="date"/>
			            </span>
			            - 
			            <span>
			            	<s:textfield id="importEndDate" name="importEndDate" cssClass="date"/>
			            </span>
			        </p>
		      	</div>
	    	</div>
	    	<p class="clearfix">
		      <button class="right search" onclick="BINOLMOCIO16.search();return false;">
			      <span class="ui-icon icon-search-big"></span>
				      <span class="button-text">
				      <s:text name="global.page.search"></s:text>
			      </span>
		      </button>
		    </p>
		</div>
		<div class="section hide" id="section">
			<div class="section-header">
			  	<strong>
				  	<span class="ui-icon icon-ttl-section-search-result"></span>
				  	<s:text name="global.page.list"></s:text>
			  	</strong>
		  	</div>
			<div class="section-content">
			    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
			      <thead>
			        <tr>
			          <th><s:text name="NO."></s:text></th>
			          <th><s:text name="CIO16_importBatch"></s:text></th>
			          <th><s:text name="CIO16_importDate"></s:text></th>
			          <th><s:text name="CIO16_employee"></s:text></th>
			          <th><s:text name="CIO16_importType"></s:text></th>
			          <th><s:text name="CIO16_comments"></s:text></th>
			        </tr>
			      </thead>
			      <tbody></tbody>
			    </table>
			</div>
		</div>
	</div>
	<!-- 查询URL -->
	<div class="hide">
		<s:url action="BINOLMOCIO16_search" id="searchUrl"></s:url>
		<a href="${searchUrl}" id="search_Url"></a>
	</div>
</cherry:form>
</s:i18n>
<script type="text/javascript">
        // 节日
        var holidays = '${holidays }';
        $('#importStartDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#importEndDate').val();
                return [value,'maxDate'];
            }
        });
        $('#importEndDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#importStartDate').val();
                return [value,'minDate'];
            }
        });
</script>