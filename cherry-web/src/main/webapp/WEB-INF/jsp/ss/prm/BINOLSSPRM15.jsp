<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM14_15.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM15.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<%@ page import="com.cherry.ss.common.PromotionConstants" %>
<s:i18n name="i18n.ss.BINOLSSPRM15">
<script type="text/javascript">
$(document).ready(function() {
	// 假日
	holidays = '${holidays}';
	activeSearchCalEventBind();
});
</script>
<s:set id="PRM_ACT_SPACE_OF_TIME"><%=PromotionConstants.PRM_ACT_SPACE_OF_TIME%></s:set>
<div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        </div>
	</div>
 	<%-- ================== 错误信息提示 START ======================= --%>
		<div id="errorMessage"></div>
	<%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" action="/ss/BINOLSSPRM15_search" class="inline" onsubmit="search();return false;">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          	<input type="checkbox" name="validFlag" value="1"/><s:text name="invalid_type"/>
          </div>
           <div class="box-content clearfix">
	          <div class="column" style="width:49%; height:100px;">
	            <p><%-- 促销柜台 --%>
              	<label><s:text name="searchPrmCounter"/></label>
                <s:textfield name="prmCounter" cssClass="text" id="prmCounter"/>
                <s:hidden name="prmCounterId" id="prmCounterId"></s:hidden>
                </p>
	            <p>
               	<%-- 活动名称 --%>	
               	<label><s:text name="searchPrmActiveName"/></label>
               	<s:textfield name="prmActiveName" cssClass="text" id="prmActiveName"/>
               	<s:hidden name="activityCode" id="activityCode"></s:hidden>
                </p>
                <p>
               	<%-- 活动状态 --%>	
               	<label><s:text name="searchActState"/></label>
               	<select id="actState" name="actState" class="valid">
				    <option value=""><s:text name="global.page.all"/></option>
				    <option selected="selected" value="1"><s:text name="actState_1"/></option>
				    <option value="0"><s:text name="actState_0"/></option>
				</select>
              	</p>
	           </div>
	           <div class="column last" style="width:50%; height:100px;">
	            <p><%-- 促销品 --%>	
               	<label><s:text name="searchPrmProduct"/></label>
               	<s:textfield name="prmProduct" cssClass="text" id="prmProduct"/>
               	<s:hidden name="prmProductId" id="prmProductId"></s:hidden>
               	<s:hidden name="prtType" id="prtType"></s:hidden>
                </p>
                <p class="date"> 
                <%--开始日期 --%>
	            <label><s:text name="searchPrmDate"/></label>
                <span>
                <s:textfield id="startDate" name="startDate" cssClass="date" value ="%{startDate}" ></s:textfield>
                </span>-<span>
                <%--结束日期 --%>
                <s:textfield id="endDate" name="endDate" cssClass="date" value ="%{endDate}" ></s:textfield>
                </span>
              	</p>
	            </div>
	         </div>
          <p class="clearfix">
         	<%-- 查询按钮 --%>
         	<button class="right search" type="submit" onclick="search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
         	</button>	
          </p>
        </cherry:form>
      </div>
      <div id="section" class="section hide">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div class="toolbar clearfix">
           	<span class="right">
           		<%-- 列设置按钮  --%>
           		<a href="#" class="setting">
           			<span class="button-text"><s:text name="global.page.colSetting"/></span>
    		 		<span class="ui-icon icon-setting"></span>
    		 	</a>
           	</span>
          </div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <%-- 活动代码 --%>
                <th><s:text name="searchActiveCode"/><span class="ui-icon ui-icon-document"></span></th>
                <%-- 促销柜台 --%>
                <th><s:text name="searchPrmCounter"/></th>
                <%-- 促销品 --%>
                <th><s:text name="searchPrmProduct"/></th>
                <%-- 活动名称 --%>
                <th><s:text name="searchPrmActiveName"/></th>
                <%-- 下发日期 --%>
                <th><s:text name="sendDate"/></th>
                <%-- 开始日期 --%>
                <th><s:text name="startDate"/></th>
                <%-- 结束日期 --%>
                <th><s:text name="endDate"/></th>
                <%-- 有效区分 --%>
                <th><s:text name="validFlag"/></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
</s:i18n>

<%-- 活动查询URL --%>
<s:url id="search_url" value="/ss/BINOLSSPRM15_search"/>
<div class="hide">
	<a id="searchUrl" href="${search_url}"></a>
</div>
<%--促销产品查询URL --%>
<s:url id="s_indSearchPrmPrtUrl" value="/ss/BINOLSSPRM15_indSearchPrmPrt" />
<%--促销柜台查询URL --%>
<s:url id="s_indSearchPrmCounterUrl" value="/ss/BINOLSSPRM15_indSearchPrmCounter" />
<%--促销活动名查询URL --%>
<s:url id="s_indSearchPrmActNameUrl" value="/ss/BINOLSSPRM15_indSearchPrmActName" />
<%--促销品查询URL --%>
<span id ="indSearchPrmPrtUrl" style="display:none">${s_indSearchPrmPrtUrl}</span>
<%--促销柜台查询URL --%>
<span id ="indSearchPrmCounterUrl" style="display:none">${s_indSearchPrmCounterUrl}</span>
<%--促销活动名查询URL --%>
<span id ="indSearchPrmActNameUrl" style="display:none">${s_indSearchPrmActNameUrl}</span>
<div class="hide" id="dialogInit"></div>    
<span id="spaceDays" style="display:none">${PRM_ACT_SPACE_OF_TIME}</span>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>