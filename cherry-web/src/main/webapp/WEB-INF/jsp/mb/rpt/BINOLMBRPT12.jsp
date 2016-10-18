<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/rpt/BINOLMBRPT12.js"></script>
<s:url id="rpt_initPrtDialogUrl" value="/mb/BINOLMBRPT12_initPrtDialog" />
<s:url id="rpt_initCateDialogUrl" value="/mb/BINOLMBRPT12_initCateDialog" />
<s:url id="rpt_searchMemUrl" value="/mb/BINOLMBRPT12_search" />
<s:url id="rpt_exportMemUrl" value="/mb/BINOLMBRPT12_export" />
<s:i18n name="i18n.mb.BINOLMBRPT12">
<div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		 	 会员报表&nbsp;&gt;&nbsp;会员连带销售报表
		</span>
	  </div>
    </div>
    <div id="errorMessage"></div>    
   			 <div style="display: none" id="errorMessageTemp">
   			 <div class="actionError">
   			    <ul><li><span></span></li></ul>         
    		</div>
    	</div>
    <div id="actionResultDisplay"></div>
    <div class="panel-content  clearfix">
      <div class="box">
        <cherry:form id="mainForm" class="inline">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
			<div style="padding: 15px 0px 5px;">
      			<table class="detail" cellpadding="0" cellspacing="0">
				  <tbody>
					<tr>
		              <th><s:text name="rptSaleTime"></s:text></th>
		              <td>
						<span>
						<span><s:textfield name="startDate" cssClass="date"></s:textfield></span>  <span>&nbsp;-&nbsp;<s:textfield name="endDate" cssClass="date"></s:textfield></span>
						</span>
			   		  </td>
			   		  <th></th>
			   		  <td></td>
		            </tr>
		            <tr>
		              <th><s:text name="rptMainPrt"></s:text></th>
		              <td colspan="3">
						<span>
						<a onclick="binolmbrpt12.openPrtDialog('mainPrt','1','mainPrtId');return false;" class="add"> 
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="rptSelPrt"></s:text></span>
						</a>
						<a onclick="binolmbrpt12.openCateDialog('mainPrt','1','mainCateId');return false;" class="add right"> 
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="rptSelCate"></s:text></span>
						</a>
						</span>
						<table class="all_clean" style="margin-left: 20px;">
						<tbody id="mainPrt">
							
						</tbody>
						</table>
			   		  </td>
		            </tr>
		            <tr>
		              <th><s:text name="rptJointPrt"></s:text></th>
		              <td colspan="3">
						<span>
						<a onclick="binolmbrpt12.openJointPrtCateDialog();return false;" class="add"> 
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="rptSelJointPrt"></s:text></span>
						</a>
						</span>
						<table class="all_clean" style="margin-left: 20px;">
						<tbody id="jointPrt">
							
						</tbody>
						</table>
			   		  </td>
		            </tr>
		          </tbody>
		        </table>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" id="searchButton" onclick="binolmbrpt12.searchList();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>	
          </p>
        </cherry:form>
      </div>
      <div id="resultList" class="hide">
      <div class="section">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div class="toolbar clearfix">
			  	<a id="exportExcel" class="export" onclick="binolmbrpt12.exportFile('Excel');return false;">
	          	  <span class="ui-icon icon-export"></span>
	          	  <span class="button-text"><s:text name="global.page.exportExcel"/></span>
	          	</a>
	          	<a id="exportCSV" class="export" onclick="binolmbrpt12.exportFile('Csv');return false;">
	          	  <span class="ui-icon icon-export"></span>
	          	  <span class="button-text"><s:text name="global.page.exportCsv"/></span>
	          	</a>
		  	<span id="headInfo"></span>
		  </div>
          <table id="resultDataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <th><s:text name="rptMemberName"></s:text></th>
                <th><s:text name="rptMemberCode"></s:text></th>
                <th><s:text name="rptMobile"></s:text></th>
                <th><s:text name="rptBirthDay"></s:text></th>
                <th><s:text name="rptMemberLevel"></s:text></th>
                <th><s:text name="rptMemberPoint"></s:text></th>
                <th><s:text name="rptCounterCode"></s:text></th>
                <th><s:text name="rptCounterName"></s:text></th>
                <th><s:text name="rptJointDate"></s:text></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
      </div>
    </div>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<div class="hide">
   	<a id="rptInitPrtDialogUrl" href="${rpt_initPrtDialogUrl}"></a>
   	<a id="rptInitCateDialogUrl" href="${rpt_initCateDialogUrl}"></a>
   	<a id="rptSearchMemUrl" href="${rpt_searchMemUrl}"></a>
   	<a id="rptExportMemUrl" href="${rpt_exportMemUrl}"></a>
</div>
</div>