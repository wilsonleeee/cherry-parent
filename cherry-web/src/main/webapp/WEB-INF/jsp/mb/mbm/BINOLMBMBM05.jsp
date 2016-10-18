<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM05.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM05">
	<s:text name="global.page.select" id="select_default"/>
	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm05_memSaleTab" /></span>
	</div>
	<div class="panel-content clearfix">
	
	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm05_saleCountInfo" />
          </strong>
	    </div>
	    <s:if test='%{!"3".equals(clubMod)}'>
	    <div class="section-content">
	    <table class="detail" style="margin-bottom: 3px;">
           <tr>
			  <th><s:text name="binolmbmbm05_memClub" /></th>
			  <td><span>
			  <s:if test='%{clubList != null && clubList.size() != 0}'>
			  <s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="binolmbmbm05.searchSaleRecord();" Cssstyle="width:150px;"/>
			  </s:if>
			  </span></td>
		    <th></th>
			   <td></td>
		    </tr>
		  </table>
		</div>   
		</s:if>
        <div class="section-content" id="saleCountInfo">
		</div>    
	</div>
	
	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm05_saleInfo" />
          </strong>
          
          <a id="expandConditionSale" style="margin-left: 0px; font-size: 12px;" class="ui-select right">
	        <span style="min-width:50px;" class="button-text choice"><s:text name="global.page.condition" /></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
	 	  </a>
	    </div>
        <div class="section-content">
	  
		  <div class="box hide" id="saleConditionDiv">
		  <form id="saleCherryForm" class="inline" onsubmit="binolmbmbm05.searchSaleRecord();return false;">
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_billCode"/></label>
	          <s:textfield name="billCode" cssClass="text"/>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_prtVendorId"/></label>
	          <s:textfield name="nameTotal" cssClass="text" maxlength="30"/>
		      <input type="hidden" id="prtVendorId" name="prtVendorId" value="" />
	        </p>
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_saleTime"/></label>
	          <span><s:textfield name="saleTimeStart" cssClass="date" cssStyle="width: 80px;"></s:textfield></span><span>-<s:textfield name="saleTimeEnd" cssClass="date" cssStyle="width: 80px;"></s:textfield></span>
	        </p>
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm05_saleType"/></label>
	          <s:select name="saleType" list="#application.CodeTable.getCodes('1055')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm05.searchSaleRecord();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		  </form>
		  </div>
		  
	  	  <div class="ui-tabs">

			<div class="clearfix">
			<ul class ="ui-tabs-nav left" id="tabSelect">
			  <li id="0" class="<s:if test='%{"0".equals(displayFlag)}'>ui-tabs-selected</s:if>" onclick="binolmbmbm05.changeTab(this);"><a><s:text name="binolmbmbm05_memSaleDisplayMode0"/></a></li>
			  <li id="1" class="<s:if test='%{!"0".equals(displayFlag)}'>ui-tabs-selected</s:if>" onclick="binolmbmbm05.changeTab(this);"><a><s:text name="binolmbmbm05_memSaleDisplayMode1"/></a></li>
			</ul>
			<span class="right" style="margin: 3px 12px 0px 0px;">
		     	<a id="export" class="export" onclick="binolmbmbm05.exportExcel();return false;">
		          <span class="ui-icon icon-export"></span>
		          <span class="button-text"><s:text name="global.page.export"/></span>
		        </a>
		    </span>
		    </div>

		  	<div class="ui-tabs-panel">
		      	<div id="memSaleDataDiv"></div>
		      	<%-- ====================== 结果一览结束 ====================== --%>
				<div class="hide">
					<div id="binolmbmbm05_saleMemCode"><s:text name="binolmbmbm05_saleMemCode" /></div>
					<div id="binolmbmbm05_billCode"><s:text name="binolmbmbm05_billCode" /></div>
					<div id="binolmbmbm05_saleType"><s:text name="binolmbmbm05_saleType" /></div>
					<cherry:show domId="BINOLMBMBM10_29">
					<div id="binolmbmbm05_departCode"><s:text name="binolmbmbm05_departCode" /></div>
					</cherry:show>
					<div id="binolmbmbm05_saleTime"><s:text name="binolmbmbm05_saleTime" /></div>
					<div id="binolmbmbm05_quantity"><s:text name="binolmbmbm05_quantity" /></div>
					<div id="binolmbmbm05_amount"><s:text name="binolmbmbm05_amount" /></div>
					<div id="binolmbmbm05_proName"><s:text name="binolmbmbm05_proName" /></div>
					<div id="binolmbmbm05_unitCode"><s:text name="binolmbmbm05_unitCode" /></div>
					<div id="binolmbmbm05_barCode"><s:text name="binolmbmbm05_barCode" /></div>
					<div id="binolmbmbm05_detailSaleType"><s:text name="binolmbmbm05_detailSaleType" /></div>
					<div id="binolmbmbm05_pricePay"><s:text name="binolmbmbm05_pricePay" /></div>
					<div id="binolmbmbm05_saleEmployee"><s:text name="binolmbmbm05_saleEmployee" /></div>
					<div id="binolmbmbm05_campaignName"><s:text name="binolmbmbm05_campaignName" /></div>
					<div id="binolmbmbm05_saleExt"><s:text name="binolmbmbm05_saleExt" /></div>
					<div id="binolmbmbm05_UniqueCode"><s:text name="binolmbmbm05_UniqueCode" /></div>
					<div id="binolmbmbm05_billState"><s:text name="binolmbmbm05_billState" /></div>
				</div>
	      	</div>
	      	
		  </div>
		  
	      
		</div>
	</div>
	
	</div>
	
</s:i18n>     

<div class="hide">
	<s:url action="BINOLMBMBM05_searchSaleRecord" id="searchSaleRecordUrl"></s:url>
	<a href="${searchSaleRecordUrl }" id="searchSaleRecordUrl"></a>
	<s:url action="BINOLMBMBM05_countSaleInfo" id="countSaleInfoUrl"></s:url>
	<a href="${countSaleInfoUrl }" id="countSaleInfoUrl"></a>
	<s:url id="exportUrl" action="BINOLMBMBM05_export" ></s:url>
	<a id="exportUrl" href="${exportUrl}"></a>
	
	<input type="hidden" id="sysConfigShowUniqueCode" value='<s:property value="sysConfigShowUniqueCode"/>'/>
</div> 

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  