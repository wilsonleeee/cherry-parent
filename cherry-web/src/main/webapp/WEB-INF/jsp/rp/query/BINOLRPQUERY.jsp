<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/common/reportTable.js"></script>
<script type="text/javascript" src="/Cherry/js/rp/query/BINOLRPQUERY.js"></script>

<s:if test="%{biReportInfo != null && !biReportInfo.isEmpty()}">
<s:i18n name="i18n.rp.BINOLRPQUERY">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="rpquery_manage"></s:text>&nbsp;&gt;&nbsp;${biReportInfo.biRptName } </span> 
  </div>
</div>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="reportTableForm" class="inline">  
    <s:hidden name="biRptCode"></s:hidden>
	<s:url action="BINOLRPQUERY_searchBIRptDetailInit" id="searchBIRptDetail"></s:url>
	<s:hidden name="drillUrl" value="%{#searchBIRptDetail}"></s:hidden>
	<div class="box-header"> <strong><span class="icon icon-ttl-search"></span><s:text name="rpquery_countCondition"></s:text></strong></div>
	<div class="box-content clearfix">
	  <div class="column last" style="width:100%">
      <table border="0" class="clean">
	    <s:if test="%{defModeList.size() > 1}">
	    <tr>
	      <th scope="row" class="parenthesis"><label style="width:auto"><strong class="loud"><s:text name="rpquery_mode"></s:text></strong></label></th>
	      <td class="parenthesis"></td>
	      <td><s:select list="defModeList" listKey="defMode" listValue="modeName" onchange="binolrpquery.changeDefMode(this);" name="defMode" id="defMode"></s:select></td>
	    </tr>
	  	</s:if>
	    <s:set id="index" value="0"></s:set>
	    <s:iterator value="biRptDefMapList" id="biRptDefMap">
	      <tr>
            <th scope="row" class="parenthesis"><label style="width:auto"><strong class="loud">
            <s:if test="%{#biRptDefMap.defType == 0}"><s:text name="rpquery_column"></s:text></s:if>
	    	<s:if test="%{#biRptDefMap.defType == 1}"><s:text name="rpquery_row"></s:text></s:if>
	    	<s:if test="%{#biRptDefMap.defType == 2}"><s:text name="rpquery_count"></s:text></s:if>
            </strong></label></th>
            <td class="parenthesis"></td>
            <td>
	    	<s:iterator value="#biRptDefMap.list" id="rptDefColMap">
	    	  <span>
	    	    <s:if test="%{#biRptDefMap.defType == 2}">
	    	    <s:if test="%{#rptDefColMap.statisticVis == 1}">
	    	      <s:if test="%{#rptDefColMap.colRowVisible == 1}">
	    			<span class="tzCheckBox checked" onclick="binolrpquery.checkClick(this);">
	    			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
	    			  <span class="tzCBPart"></span>
	    			</span>
	    		  </s:if>
	    		  <s:else>
	    			<span class="tzCheckBox" onclick="binolrpquery.checkClick(this);">
	    			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
	    			  <span class="tzCBPart"></span>
	    			</span>
	    		  </s:else>
	    	    </s:if>
	    	    </s:if>
	    	    <s:else>
	    	      <s:if test="%{#rptDefColMap.colRowVisible == 1}">
	    			<span class="tzCheckBox checked" onclick="binolrpquery.checkClick(this);">
	    			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
	    			  <span class="tzCBPart"></span>
	    			</span>
	    		  </s:if>
	    		  <s:else>
	    			<span class="tzCheckBox" onclick="binolrpquery.checkClick(this);">
	    			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
	    			  <span class="tzCBPart"></span>
	    			</span>
	    		  </s:else>
	    	    </s:else>
	    		<input type="hidden" name="biRptDefList[${index }].defType" value="${biRptDefMap.defType }" />
	    		<input type="hidden" name="biRptDefList[${index }].defValue" value="${rptDefColMap.defValue }" /> 
	    		<input type="hidden" name="biRptDefList[${index }].defColumnNo" value="${rptDefColMap.defColumnNo }" /> 
	    		<input type="hidden" name="biRptDefList[${index }].colRowVisible" value="${rptDefColMap.colRowVisible }" id="colRowVisible"/> 
	    		<input type="hidden" name="biRptDefList[${index }].colRowTotalVis" value="${rptDefColMap.colRowTotalVis }" /> 
	    		<input type="hidden" name="biRptDefList[${index }].statisticType" value="${rptDefColMap.statisticType }" /> 
	    		<input type="hidden" name="biRptDefList[${index }].statisticVis" value="${rptDefColMap.statisticVis }" /> 
	    		<input type="hidden" name="biRptDefList[${index }].defScript" value="${rptDefColMap.defScript }" />
	    		<input type="hidden" name="biRptDefList[${index }].isDrillDown" value="${rptDefColMap.isDrillDown }" /> 
	    		<input type="hidden" name="biRptDefList[${index }].isDrillThrough" value="${rptDefColMap.isDrillThrough }" />  
	    		<input type="hidden" name="biRptDefList[${index }].defValueTbl" value="${rptDefColMap.defValueTbl }" />
	    		<input type="hidden" name="biRptDefList[${index }].defFormat" value="${rptDefColMap.defFormat }" />    
	    	  </span>
	    	  <s:set id="index" value="%{#index+1 }"></s:set>
	    	</s:iterator>
	    	</td>
	      </tr>
	    </s:iterator>
	  </table>
	  </div>
	</div>
    <div class="box-header"><strong><span class="icon icon-ttl-search"></span><s:text name="global.page.condition"></s:text></strong></div>
    <div class="box-content clearfix">
      <div class="column last" style="width:100%">
        <s:set id="index" value="0"></s:set>
        <s:iterator value="biRptQryDefMapList" id="biRptQryDefMap">
          <s:if test="%{#biRptQryDefMap.isVisible == 0}">
          <p class="hide">	
          	<label>${biRptQryDefMap.queryGrpName }</label>
          	<s:iterator value="#biRptQryDefMap.list" id="biRptQryDefPropMap">
          	<span>  
          	<input type="hidden" name="biRptQryDefList[${index }].queryPropValue" value="${biRptQryDefPropMap.queryPropValue }" />
  			<input type="hidden" name="biRptQryDefList[${index }].queryPropTbl" value="${biRptQryDefPropMap.queryPropTbl }" />
  			<input type="hidden" name="biRptQryDefList[${index }].defColumnNo" value="${biRptQryDefPropMap.defColumnNo }" />
  			<input type="hidden" name="biRptQryDefList[${index }].queryCondition" value="${biRptQryDefPropMap.queryCondition }" />
  			<input type="hidden" name="biRptQryDefList[${index }].queryValue" value="${biRptQryDefPropMap.queryValue }" />
  			<input type="hidden" name="biRptQryDefList[${index }].isVisible" value="${biRptQryDefMap.isVisible }" />
	        <s:set id="index" value="%{#index+1 }"></s:set>
	        </span>
	        </s:iterator>
	      </p>  
          </s:if>
          <s:else>
          <p>
        	<label>${biRptQryDefMap.queryGrpName }<s:if test="%{#biRptQryDefMap.isRequired == 1}"><span class="highlight"><s:text name="global.page.required"></s:text></span></s:if></label>
       		<s:iterator value="#biRptQryDefMap.list" id="biRptQryDefPropMap">
		      <span>  
		        <s:if test="%{#biRptQryDefPropMap.queryPropType == 0}">
       				<input type="text" class="text" name="biRptQryDefList[${index }].queryValue" value="${biRptQryDefPropMap.queryValue }" maxlength="${biRptQryDefPropMap.queryPropLen }"/>
       			</s:if>
       			<s:if test="%{#biRptQryDefPropMap.queryPropType == 1}">
       				<s:select list="#biRptQryDefPropMap.list" name="biRptQryDefList[%{#index }].queryValue" listKey="code" listValue="value" headerKey="" headerValue="%{#select_default}" value="%{#biRptQryDefPropMap.queryValue }"></s:select>
       			</s:if>
       			<s:if test="%{#biRptQryDefPropMap.queryPropType == 2}">
       				<s:iterator value="#biRptQryDefPropMap.list" id="qryValueMap">
       					<input type="checkbox" class="text" name="biRptQryDefList[${index }].queryValue" value="${qryValueMap.code }" />${qryValueMap.value }
       				</s:iterator>
       			</s:if>
       			<s:if test="%{#biRptQryDefPropMap.queryPropType == 3}">
       				<s:select list="#biRptQryDefPropMap.yearList" name="biRptQryDefList[%{#index }].queryValue" value="%{#biRptQryDefPropMap.year }"></s:select>
       				<s:select list="{'01','02','03','04','05','06','07','08','09','10','11','12'}" name="biRptQryDefList[%{#index }].queryValue" value="%{#biRptQryDefPropMap.month }"></s:select>
       			</s:if>
		        <s:if test="%{#biRptQryDefPropMap.queryPropType == 4}">
       				<s:select list="#biRptQryDefPropMap.list" name="departId" listKey="departId" listValue="departName" headerKey="" headerValue="%{#select_default}" onchange="binolrpquery.changeDepart(this,'%{#select_default}');"></s:select>
       				<input type="hidden" name="biRptQryDefList[${index }].queryValue" id="queryValue"/>
       				<input type="hidden" name="type" id="propValObj" value="${biRptQryDefPropMap.propValObj }"/>
       			</s:if>
       			<input type="hidden" name="biRptQryDefList[${index }].queryPropValue" value="${biRptQryDefPropMap.queryPropValue }" />
       			<input type="hidden" name="biRptQryDefList[${index }].queryPropTbl" value="${biRptQryDefPropMap.queryPropTbl }" />
       			<input type="hidden" name="biRptQryDefList[${index }].defColumnNo" value="${biRptQryDefPropMap.defColumnNo }" />
       			<input type="hidden" name="biRptQryDefList[${index }].queryCondition" value="${biRptQryDefPropMap.queryCondition }" />
       			<input type="hidden" name="biRptQryDefList[${index }].queryPropType" value="${biRptQryDefPropMap.queryPropType }" />
		        <s:set id="index" value="%{#index+1 }"></s:set>
		      </span>  
       		</s:iterator>
          </p>
          </s:else>	
        </s:iterator>
      </div>
    </div>
    <p class="clearfix">
      <s:url action="BINOLRPQUERY_searchBITable" id="searchBITable"></s:url>
      <button class="right search" onclick="binolrpquery.searchReportTable('${searchBITable }');return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
    </p>
  </cherry:form>
</div>
<div class="section hide">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="rpquery_result"></s:text></strong></div>
  <div class="section-content">
    <div id="reportTableDiv">
    	<table cellpadding="0" cellspacing="0" border="0" width="100%" id="reportTable"></table>
    	<div id="reportTableError"></div>
    </div>
  </div>
</div>
</div>

<s:if test="%{defModeList.size() > 1}">
<div id="defModeTemp" class="hide">
<s:iterator value="defModeList" id="defModeMap">
<table border="0" class="clean" id="defMode_${defModeMap.defMode }">
<tr>
  <th scope="row" class="parenthesis"><label style="width:auto"><strong class="loud"><s:text name="rpquery_mode"></s:text></strong></label></th>
  <td class="parenthesis"></td>
  <td><s:select list="defModeList" listKey="defMode" listValue="modeName" onchange="binolrpquery.changeDefMode(this);" name="defMode" id="defMode"></s:select></td>
</tr>
<s:set id="index" value="0"></s:set>
<s:iterator value="#defModeMap.list" id="biRptDefMap">
  <tr>
    <th scope="row" class="parenthesis"><label style="width:auto"><strong class="loud">
        <s:if test="%{#biRptDefMap.defType == 0}"><s:text name="rpquery_column"></s:text></s:if>
 		<s:if test="%{#biRptDefMap.defType == 1}"><s:text name="rpquery_row"></s:text></s:if>
 		<s:if test="%{#biRptDefMap.defType == 2}"><s:text name="rpquery_count"></s:text></s:if>
    </strong></label></th>
    <td class="parenthesis"></td>
    <td>
 	<s:iterator value="#biRptDefMap.list" id="rptDefColMap">
 	  <span>
 	    <s:if test="%{#biRptDefMap.defType == 2}">
 	    <s:if test="%{#rptDefColMap.statisticVis == 1}">
 	      <s:if test="%{#rptDefColMap.colRowVisible == 1}">
 			<span class="tzCheckBox checked" onclick="binolrpquery.checkClick(this);">
 			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
 			  <span class="tzCBPart"></span>
 			</span>
 		  </s:if>
 		  <s:else>
 			<span class="tzCheckBox" onclick="binolrpquery.checkClick(this);">
 			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
 			  <span class="tzCBPart"></span>
 			</span>
 		  </s:else>
 	    </s:if>
 	    </s:if>
 	    <s:else>
 	      <s:if test="%{#rptDefColMap.colRowVisible == 1}">
 			<span class="tzCheckBox checked" onclick="binolrpquery.checkClick(this);">
 			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
 			  <span class="tzCBPart"></span>
 			</span>
 		  </s:if>
 		  <s:else>
 			<span class="tzCheckBox" onclick="binolrpquery.checkClick(this);">
 			  <span class="tzCBContent">${rptDefColMap.defValue }</span>
 			  <span class="tzCBPart"></span>
 			</span>
 		  </s:else>
 	    </s:else>
 		<input type="hidden" name="biRptDefList[${index }].defType" value="${biRptDefMap.defType }" />
 		<input type="hidden" name="biRptDefList[${index }].defValue" value="${rptDefColMap.defValue }" /> 
 		<input type="hidden" name="biRptDefList[${index }].defColumnNo" value="${rptDefColMap.defColumnNo }" /> 
 		<input type="hidden" name="biRptDefList[${index }].colRowVisible" value="${rptDefColMap.colRowVisible }" id="colRowVisible"/> 
 		<input type="hidden" name="biRptDefList[${index }].colRowTotalVis" value="${rptDefColMap.colRowTotalVis }" /> 
 		<input type="hidden" name="biRptDefList[${index }].statisticType" value="${rptDefColMap.statisticType }" /> 
 		<input type="hidden" name="biRptDefList[${index }].statisticVis" value="${rptDefColMap.statisticVis }" /> 
 		<input type="hidden" name="biRptDefList[${index }].defScript" value="${rptDefColMap.defScript }" />
 		<input type="hidden" name="biRptDefList[${index }].isDrillDown" value="${rptDefColMap.isDrillDown }" /> 
 		<input type="hidden" name="biRptDefList[${index }].isDrillThrough" value="${rptDefColMap.isDrillThrough }" />  
 		<input type="hidden" name="biRptDefList[${index }].defValueTbl" value="${rptDefColMap.defValueTbl }" /> 
 		<input type="hidden" name="biRptDefList[${index }].defFormat" value="${rptDefColMap.defFormat }" /> 
 	  </span>
 	  <s:set id="index" value="%{#index+1 }"></s:set>
 	</s:iterator>
 	</td>
  </tr>
</s:iterator>
</table>
</s:iterator>
</div>
</s:if>
</s:i18n>
<s:url action="BINOLRPVALUE_queryDepart" id="queryDepartUrl">
<s:param name="businessType" value="%{biReportInfo.businessType}"></s:param>
</s:url>
<a href="${queryDepartUrl }" id="queryDepartUrl" Style="display:none;"></a>
</s:if>























 

    

 