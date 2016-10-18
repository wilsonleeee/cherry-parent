<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM04.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM04"> 	
 	<s:text name="global.page.select" id="select_default"/>
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm04_memPointTab" /></span>
	</div>
	<div class="panel-content clearfix">
 	
 	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm04_pointInfo" />
          </strong>
	    </div>
        <div class="section-content" id="pointDiv">
        <s:if test='%{!"3".equals(clubMod)}'>
        <table class="detail" style="margin-bottom: 3px;">
           <tr>
			  <th><s:text name="binolmbmbm04_memClub" /></th>
			  <td><span>
			  <s:if test='%{clubList != null && clubList.size() != 0}'>
			  <s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="binolmbmbm04.changeClub();" Cssstyle="width:150px;"/>
			  </s:if>
			  </span></td>
		    <th></th>
			   <td></td>
		    </tr>
		  </table>
		  </s:if>
          <table class="detail point-info" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm04_freezePoint" /></th>
			  <td><span><s:property value="memPointInfo.totalPoint"/></span></td>
			  <th><s:text name="binolmbmbm04_changablePoint" /></th>
			  <td><span><s:property value="memPointInfo.changablePoint"/></span></td>
		    </tr>
		  </table>
          <table class="detail point-info" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm04_totalPoint" /></th>
			  <td><span><s:property value="memPointInfo.totalPoint"/></span></td>
			  <th><s:text name="binolmbmbm04_totalChanged" /></th>
			  <td><span><s:property value="memPointInfo.totalChanged"/></span></td>
		    </tr>
		  </table>
          <table class="detail point-info" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm04_curDisablePoint" /></th>
			  <td><span><s:property value="memPointInfo.curDisablePoint"/></span></td>
			  <th><s:text name="binolmbmbm04_curDealDate" /></th>
			  <td><span><s:property value="memPointInfo.curDealDate"/></span></td>
		    </tr>
		    <tr>
			  <th><s:text name="binolmbmbm04_totalDisablePoint" /></th>
			  <td><span><s:property value="memPointInfo.totalDisablePoint"/></span></td>
			  <th><s:text name="binolmbmbm04_preDisableDate" /></th>
			  <td><span><s:property value="memPointInfo.preDisableDate"/></span></td>
		    </tr>
		  </table>
		</div>    
	</div>
	
	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm04_pointDetail" />
          </strong>
          <a id="expandConditionPoint" style="margin-left: 0px; font-size: 12px;" class="ui-select right">
	        <span style="min-width:50px;" class="button-text choice"><s:text name="global.page.condition" /></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
	 	  </a>
	    </div>
        <div class="section-content">
        
		<div class="box hide" id="pointConditionDiv">
		<form id="pointDetailForm" class="inline" onsubmit="binolmbmbm04.searchPointDetail();return false;" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_billCode" /></label>
	          <s:textfield name="billCode" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_relevantSRCode" /></label>
	          <s:textfield name="relevantSRCode" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_departCode" /></label>
	          <s:textfield name="departCode" cssClass="text"></s:textfield>
	        </p>
	        <p>
	          <label style="width:80px;"><s:text name="binolmbmbm04_prtVendorId" /></label>
	          <s:textfield name="prtNamePoint" cssClass="text" maxlength="30"/>
			  <input type="hidden" id="prtVendorIdPoint" name="prtVendorId" value="" />
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm04_changeDate" /></label>
	          <span><s:textfield name="changeDateStart" cssClass="date" cssStyle="width: 80px;"></s:textfield></span><span>-<s:textfield name="changeDateEnd" cssClass="date" cssStyle="width: 80px;"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbptm04_memPoint" /></label>
	          <span><s:textfield name="memPointStart" cssClass="text" cssStyle="width:75px"></s:textfield></span><span>-<s:textfield name="memPointEnd" cssClass="text" cssStyle="width:75px"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm04_billType" /></label>
	          <s:select list='#application.CodeTable.getCodes("1213")' listKey="CodeKey" listValue="Value" name="tradeType" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm04_campaignName" /></label>
	          <s:select list="campaignNameList" name="subCampaignId" listKey="subCampaignId" listValue="campaignName" headerKey="" headerValue="%{#select_default}"></s:select>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm04.searchPointDetail();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		</form>
		</div>
		
		<div class="ui-tabs">

			<div class="clearfix">
			<ul class ="ui-tabs-nav left" id="tabSelect">
			  <li id="0" class="<s:if test='%{"0".equals(displayFlag)}'>ui-tabs-selected</s:if>" onclick="binolmbmbm04.changeTab(this);"><a><s:text name="binolmbmbm04_memSaleDisplayMode0"/></a></li>
			  <li id="1" class="<s:if test='%{!"0".equals(displayFlag)}'>ui-tabs-selected</s:if>" onclick="binolmbmbm04.changeTab(this);"><a><s:text name="binolmbmbm04_memSaleDisplayMode1"/></a></li>
			</ul>
			<span class="right" style="margin: 3px 12px 0px 0px;">
		     	<a id="export" class="export" onclick="binolmbmbm04.exportExcel();return false;">
		          <span class="ui-icon icon-export"></span>
		          <span class="button-text"><s:text name="global.page.export"/></span>
		        </a>
		    </span>
		    </div>
		    
		    <div class="ui-tabs-panel">
		      	<div id="pointDetailDataDiv"></div>
		      	<%-- ====================== 结果一览结束 ====================== --%>
				<div class="hide">
					<div id="binolmbmbm04_memberCode"><s:text name="binolmbmbm04_memberCode" /></div>
					<div id="binolmbmbm04_billCode"><s:text name="binolmbmbm04_billCode" /></div>
					<div id="binolmbmbm04_billType"><s:text name="binolmbmbm04_billType" /></div>
					<cherry:show domId="BINOLMBMBM10_29">
					<div id="binolmbmbm04_departCode"><s:text name="binolmbmbm04_departCode" /></div>
					</cherry:show>
					<div id="binolmbmbm04_changeDate"><s:text name="binolmbmbm04_changeDate" /></div>
					<div id="binolmbmbm04_amount"><s:text name="binolmbmbm04_amount" /></div>
					<div id="binolmbmbm04_quantity"><s:text name="binolmbmbm04_quantity" /></div>
					<div id="binolmbmbm04_point"><s:text name="binolmbmbm04_point" /></div>
					<div id="binolmbmbm04_proName"><s:text name="binolmbmbm04_proName" /></div>
					<div id="binolmbmbm04_unitCode"><s:text name="binolmbmbm04_unitCode" /></div>
					<div id="binolmbmbm04_barCode"><s:text name="binolmbmbm04_barCode" /></div>
					<div id="binolmbmbm04_saleType"><s:text name="binolmbmbm04_saleType" /></div>
					<div id="binolmbmbm04_proPrice"><s:text name="binolmbmbm04_proPrice" /></div>
					<div id="binolmbmbm04_proQuantity"><s:text name="binolmbmbm04_proQuantity" /></div>
					<div id="binolmbmbm04_proPoint"><s:text name="binolmbmbm04_proPoint" /></div>
					<div id="binolmbmbm04_pointType"><s:text name="binolmbmbm04_pointType" /></div>
					<div id="binolmbmbm04_combCampaignName"><s:text name="binolmbmbm04_combCampaignName" /></div>
					<div id="binolmbmbm04_mainCampaignName"><s:text name="binolmbmbm04_mainCampaignName" /></div>
					<div id="binolmbmbm04_subCampaignName"><s:text name="binolmbmbm04_subCampaignName" /></div>
					<div id="binolmbmbm04_reason"><s:text name="binolmbmbm04_reason" /></div>
					<div id="binolmbmbm04_relevantSRCode"><s:text name="binolmbmbm04_relevantSRCode" /></div>
					<div id="binolmbmbm04_relevantSRTime"><s:text name="binolmbmbm04_relevantSRTime" /></div>
					<div id="binolmbmbm04_hasRelevantSRCode"><s:text name="binolmbmbm04_hasRelevantSRCode" /></div>
				</div>
	      	</div>
	    
	    </div>
    
    	</div>
    </div>
    
    </div>
      
</s:i18n>   

<div class="hide">
	<s:url action="BINOLMBMBM04_searchMemPoint" id="searchMemPoint_Url"></s:url>
	<a href="${searchMemPoint_Url}" id="searchMemPointUrl"></a>
	<s:url action="BINOLMBMBM04_searchPointInfo" id="searchPointInfoUrl"></s:url>
	<a href="${searchPointInfoUrl }" id="searchPointInfoUrl"></a>
	<s:url id="exportUrl" action="BINOLMBMBM04_export" ></s:url>
	<a id="exportUrl" href="${exportUrl}"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  