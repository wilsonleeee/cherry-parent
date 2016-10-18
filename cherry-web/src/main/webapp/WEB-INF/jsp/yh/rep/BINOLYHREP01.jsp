<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/yh/rep/BINOLYHREP01.js"></script>
<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
	$('#startDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#endDate').val();
			return [value,'maxDate'];
		}
	});
	$('#endDate').cherryDate({
		holidayObj: holidays,
		beforeShow: function(input){
			var value = $('#startDate').val();
			return [value,'minDate'];
		}
	});
</script>
<s:i18n name="i18n.yh.BINOLYHREP01">
<div class="hide">
	<s:url action="BINOLYHREP01_search" id="search_url"></s:url>
	<a href="${search_url }" id="searchUrl"></a>
</div>
<div class="panel-header">
  <div class="clearfix">
	<span class="breadcrumb left">	    
	  <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	</span>  
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content">
	<cherry:form id="mainForm" class="inline">
		<cherry:brand name="brandInfoId" />
		<s:hidden name="provinceId" id="provinceId"/>
        <s:hidden name="cityId" id="cityId"/>
		<div class="box">	
			<div class="box-header">
           		<strong>
           			<span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
           		</strong>
            </div>
            <div class="box-content clearfix">
               	<div class="column" style="width:50%;height:auto;">
	                <p>
	                	<label><s:text name="YHREP01_billCode" /></label>
	                    <s:textfield name="billCode" cssClass="text" maxlength="35"></s:textfield>
	                </p>
	                <p>
	                	<label><s:text name="YHREP01_reseller1L" /></label>
	                	<input type="hidden" name="parentResellerCode" id="parentResellerCode"></input>
	                    <input type="text" class="text" id="parentResellerName"></input>
	                </p>
	                <p>
	                	<label><s:text name="YHREP01_reseller2L" /></label>
	                	<input type="hidden" name="resellerCode" id="resellerCode"></input>
	                    <input type="text" class="text" id="resellerName"></input>
	                </p>
		        </div>    
        		<div class="column last" style="width:49%;height:auto;">
        			<p>
	                  	<label><s:text name="YHREP01_date"/></label>
	                  	<span><s:textfield name="startDate" cssClass="date"/></span>
	                  	 - 
	                  	<span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>
	               	<p>
						<label><s:text name="YHREP01_provinceName"/></label>
						<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
							<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
							<span class="ui-icon ui-icon-triangle-1-s"></span>
						</a>						
					</p>
					<p>
						<label><s:text name="YHREP01_cityName"/></label>
						<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
						    <span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
						    <span class="ui-icon ui-icon-triangle-1-s"></span>
						</a>						
					</p>
	            </div>
            </div> 
         	<p class="clearfix">
         		<button class="right search" type="button" onclick="BINOLYHREP01.search();return false;">
         			<span class="ui-icon icon-search-big"></span>
         			<span class="button-text"><s:text name="global.page.search"/></span>
         		</button>
        	</p>
       	</div> 
    </cherry:form>  
	<%-- ====================== 结果一览开始 ====================== --%>
	<div id="section" class="section hide">
		<div class="section-header">
			<strong> 
				<span class="ui-icon icon-ttl-section-search-result"></span> 
				<s:text name="global.page.list" />
		 	</strong>
		</div>
		<div class="section-content">
			<div class="toolbar clearfix">
				<span id="headInfo"></span>
				<span class="right"> 
					<%-- 设置列 --%>
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span>
					</a>
				</span>
			</div>
			<div id="saleRptDiv">
				<table class="jquery_table" style="width: 100%" id="dataTable">
					<thead>
						<tr>
							<th><s:text name="No." /></th>
							<th><s:text name="YHREP01_billCode" /></th>
							<th><s:text name="YHREP01_couponCode" /></th>
							<th><s:text name="YHREP01_member" /></th>
							<th><s:text name="YHREP01_productName" /></th>
							<th><s:text name="YHREP01_amount" /></th>
							<th><s:text name="YHREP01_reseller1L" /></th>
							<th><s:text name="YHREP01_reseller2L" /></th>
						</tr>
					</thead>
					<tbody></tbody>
				</table>
			</div>
		</div>
	</div>
	<%-- ====================== 结果一览结束 ====================== --%>
</div>
<s:text name="global.page.select" id="select_default"/>
<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== CSV导出共通 START ======================= --%>
<%-- <jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" /> --%>
<%-- ================== CSV导出共通 END ======================= --%>