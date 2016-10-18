<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM21.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM21"> 	
 	<div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm21_title" /></span>
	</div>
	<div class="panel-content clearfix">
 	
 	<s:if test="%{referrerMap != null}">
 	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:property value="memName"/><s:text name="binolmbmbm21_referrer" />
          </strong>
	    </div>
        <div class="section-content">
          <table class="detail" style="margin-bottom: 3px;">
            <tr>
			  <th><s:text name="binolmbmbm21_memCode" /></th>
			  <td><span><s:property value="referrerMap.memCode"/></span></td>
			  <th><s:text name="binolmbmbm21_joinDate" /></th>
			  <td><span><s:property value="referrerMap.joinDate"/></span></td>
		    </tr>
		    <tr>
			  <th><s:text name="binolmbmbm21_name" /></th>
			  <td><span><s:property value="referrerMap.name"/></span></td>
			  <th><s:text name="binolmbmbm21_counterName" /></th>
			  <td><span><s:property value="referrerMap.counterName"/></span></td>
		    </tr>
		    <tr>
			  <th><s:text name="binolmbmbm21_mobilePhone" /></th>
			  <td><span><s:property value="referrerMap.mobilePhone"/></span></td>
			  <th><s:text name="binolmbmbm21_provinceName" /></th>
			  <td><span><s:property value="referrerMap.provinceName"/></span></td>
		    </tr>
		    <tr>
			  <th><s:text name="binolmbmbm21_birthDay" /></th>
			  <td><span>
			  	<s:if test='%{referrerMap.birthYear != null && !"".equals(referrerMap.birthYear) && referrerMap.birthMonth != null && !"".equals(referrerMap.birthMonth) && referrerMap.birthDay != null && !"".equals(referrerMap.birthDay)}'>
					<s:property value="referrerMap.birthYear"/>-<s:property value="referrerMap.birthMonth"/>-<s:property value="referrerMap.birthDay"/>
				</s:if>
			  </span></td>
			  <th><s:text name="binolmbmbm21_cityName" /></th>
			  <td><span><s:property value="referrerMap.cityName"/></span></td>
		    </tr>
		  </table>
		</div>    
	</div>
	</s:if>
	
	<div class="section">
        <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span><s:property value="memName"/><s:text name="binolmbmbm21_referList" />
          </strong>
          
          <a id="expandConditionRefer" style="margin-left: 0px; font-size: 12px;" class="ui-select right">
	        <span style="min-width:50px;" class="button-text choice"><s:text name="global.page.condition" /></span>
	 		<span class="ui-icon ui-icon-triangle-1-n"></span>
	 	  </a>
	    </div>
        <div class="section-content">
	  
		  <div class="box hide" id="referConditionDiv">
		  <form id="referCherryForm" class="inline" onsubmit="binolmbmbm21.searchReferList();return false;" >
		  <s:hidden name="memberInfoId"></s:hidden>
		  <div class="box-header">
		  <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		  </div>
		  <div class="box-content clearfix">
		  <div class="column" style="width:50%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm21_memCode"/></label>
	          <s:textfield name="memCode" cssClass="text"/>
	        </p>
	        
	      </div>
	      <div class="column last" style="width:49%; height: auto;">
	        <p>
	          <label style="width:60px;"><s:text name="binolmbmbm21_mobilePhone"/></label>
	          <s:textfield name="mobilePhone" cssClass="text"/>
	        </p>
	      </div>
		  </div>
		  <p class="clearfix">
	          <button class="right search" onclick="binolmbmbm21.searchReferList();return false;"><span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"></s:text></span></button>
	      </p>
		  </form>
		  </div>
		  
	  	  <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="referListDataTable">
		      <thead>
		        <tr>
		          <th><s:text name="binolmbmbm21_name"></s:text></th>
		          <th><s:text name="binolmbmbm21_memCode"></s:text></th>
		          <th><s:text name="binolmbmbm21_mobilePhone"></s:text></th>
		          <th><s:text name="binolmbmbm21_birthDay"></s:text></th>
		          <th><s:text name="binolmbmbm21_provinceName"></s:text></th>
		          <th><s:text name="binolmbmbm21_cityName"></s:text></th>
		          <th><s:text name="binolmbmbm21_counterName"></s:text></th>
		          <th><s:text name="binolmbmbm21_joinDate"></s:text></th>
		        </tr>
		      </thead>
		      <tbody>
		      </tbody>
		    </table>
		  
	      
		</div>
	</div>
	
	
    
    </div>
      
</s:i18n>   

<div class="hide">
	<s:url action="BINOLMBMBM21_search" id="searchReferListUrl"></s:url>
	<a href="${searchReferListUrl }" id="searchReferListUrl"></a>
</div>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>  