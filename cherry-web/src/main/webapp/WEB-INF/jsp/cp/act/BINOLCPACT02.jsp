<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT01.js"></script>
<s:i18n name="i18n.cp.BINOLCPACT01">
<s:url id="MainList_url" action="BINOLCPACT01_search"/>
<s:url id="Init_url" action="BINOLCPACT01_init"/>
<%--活动停用URL --%>
<s:url id="s_subCampaignUrl" value="/cp/BINOLCPACT01_stopSubCampaign" />
	<div class="hide"><a id="MainListUrl" href="${MainList_url}"></a></div>
<s:text id="selectAll" name="global.page.all"/>
<div class="panel-header">
        <div class="clearfix">
        	<span class="breadcrumb left">
        	<span class="ui-icon icon-breadcrumb"></span><s:text name="ACT01_memberActivities"/> &gt; <s:text name="ACT01_memberDetail"/></span>
     		<span class="right"> 
       			<%-- 活动添加按钮 --%>
       			<cherry:show domId="CPACT01ADD">
	       		 	<a href="/Cherry/cp/BINOLCPCOM02_topInit.action?typeFlag=1" class="add" onclick="javascript:openWin(this);return false;">
	       		 		<span class="button-text"><s:text name="ACT01_campAdd"/></span>
	       		 		<span class="ui-icon icon-add"></span>
	       		 	</a>
	       		</cherry:show>
       		</span>
        </div>
    </div>
    <div class="panel-content">
         <div class="section">
       <cherry:form id="mainForm" class="inline" >
           <div class="section-header ">
           <p class="right">
	        <span>
	        	<%--主题活动--%>
	        	<input type="radio" id="searchMode_1" name="searchMode" value="1" onclick="BINOLCPACT01.changeMode('${Init_url}',this);"  />
	        	<label for="searchMode_1"><s:text name="ACT01_mainActSearch" /></label>
	        </span>
	        <span>
	        	<%--活动--%>
	        	<input type="radio" id="searchMode_2" name="searchMode" value="2" onclick="BINOLCPACT01.changeMode('${Init_url}',this);" checked="checked"/>
	        	<label for="searchMode_2"><s:text name="ACT01_actSearch" /></label>
	        </span>
		</p> 	
        </div>
    	<div class="box">
         <div class="box-header">
         		<%--活动搜素条件--%>
            	<span class="ui-icon icon-ttl-search"></span>
            	<strong><s:text name="ACT01_campSearch"/></strong>
         </div>
         <div class="box-content clearfix">
         <div style="width:50%;height:100px;" class="column">
             <p class="clearfix">
             	<%--活动代码--%>
              <label><s:text name="ACT04_subcampCode"/></label>
           	  <span><input id="subcampCode"  class="text" name="subcampCode"></span>
             </p>
             <p class="clearfix">
              <%--活动状态--%>
              <label><s:text name="ACT01_campState"/></label>
           	  <span>
	          	  <s:select name="campState" Cssstyle="width:186px;" list='#application.CodeTable.getCodes("1113")' listKey="CodeKey" listValue="Value" 
	          	  headerKey="" headerValue="%{selectAll}" value="1"/>
           	  </span>
             </p>
            <p>
               <%-- 有效状态--%>
               <label><s:text name="ACT01_validFlag"/></label>
               <s:select name="validFlag" Cssstyle="width:186px;" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
               	headerKey="" headerValue="%{selectAll}" value="1"/>
             </p>
          </div>
          <div style="width:49%;height:55px;" class="column last">  
            <p class="clearfix">
           		<%--品牌--%>
               <label style="width:80px;"><s:text name="ACT01_brand"/></label>
         		<s:select name="brandInfoId" Cssstyle="width:186px;" list="brandInfoList" listKey="brandInfoId" listValue="brandName" value="%{brandInfoId}"/>
            </p>
             <p class="clearfix">
            	<%--主题活动类型--%>
    			<label style="width:80px;"><s:text name="ACT01_maincampType"/></label>
      		 	<s:select id="mainCampType" name="campType" Cssstyle="width:186px;" list='#application.CodeTable.getCodes("1174")' listKey="CodeKey" listValue="Value"
               	headerKey="" headerValue="%{selectAll}"  onchange="BINOLCPACT01.campTypeChange(this);" />
            </p>
            <p class="clearfix">
            	<%--活动类型--%>
    			<label style="width:80px;"><s:text name="ACT01_campType"/></label>
    			<select id="subcampType_" name="subcampType" style="width:186px;" disabled="disabled">
    				<option value=""><s:text name="global.page.all"/></option>
    			</select>
      		 	<s:select id="subcampType_DHHD" name="subcampType"  Cssstyle="width:186px;" cssClass="hide" disabled="true" list='#application.CodeTable.getCodes("1229")' listKey="CodeKey" listValue="Value"
               	headerKey=""  headerValue="%{selectAll}"/>
               	<s:select id="subcampType_CXHD" name="subcampType" Cssstyle="width:186px;" cssClass="hide" disabled="true" list='#application.CodeTable.getCodes("1228")' listKey="CodeKey" listValue="Value"
               	headerKey=""  headerValue="%{selectAll}"/>
               	<s:select id="subcampType_LYHD" name="subcampType" Cssstyle="width:186px;" cssClass="hide" disabled="true" list='#application.CodeTable.getCodes("1247")' listKey="CodeKey" listValue="Value"
               	headerKey="" headerValue="%{selectAll}"/>
            </p>
            </div>
            </div>
            <p class="clearfix">
            	<%--查询--%>
              	<button onclick="BINOLCPACT01.search();return false;" class="right search">
              			<span class="ui-icon icon-search-big"></span>
              			<span class="button-text"><s:text name="ACT01_search"/></span>
              	</button>
            </p>
        </div>
         </cherry:form>
        </div>
        <div id="section" class="section hide" >
       	<div class="section-header" id="section-header">
        	<%--查询结果一览 --%>
			<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<s:text name="ACT01_searchResult"/>
          	</strong>
        </div>
		<div class="ui-tabs-panel">
	        	<div class="toolbar clearfix">
				 	<span class="right"> <%-- 设置列 --%>
						<a class="setting"> 
							<span class="ui-icon icon-setting"></span> 
							<span class="button-text"><s:text name="global.page.colSetting" /></span> 
						</a>
					</span>
			 	</div>
        <div class="section" id="result_list">
	            <table id="dataTable_2" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
	              	<thead>
		                <tr>
		                	<th>NO.</th>
		            		<%--活动名称--%>
			                <th><s:text name="ACT01_cmpName"/><span class="ui-icon ui-icon-document"></span></th>
			            	  <%--活动代码--%>
			                <th><s:text name="ACT01_subcampCode"/></th> 
			            	 <%--主题活动名称--%>
			                <th><s:text name="ACT01_maincampName"/></th> 
			            	<%--主题活动类型--%>
			                <th><s:text name="ACT01_maincampType"/></th> 
			                <%--活动类型--%>
			                <th><s:text name="ACT01_campType"/></th> 
			                <%--活动状态--%>
			                <th><s:text name="ACT01_campState"/></th>
			                <%--开始时间--%>
			                <th><s:text name="ACT01_startTime"/></th>
			                <%--结束时间--%>
			                <th width="15%" ><s:text name="ACT01_endTime"/></th>
			                <%--有效区分--%>
			                <th><s:text name="ACT01_validFlag"/></th>
			                 <%--操作--%>
			                <th><s:text name="ACT01_operator"/></th>
		                </tr>
	              	</thead>
	            </table>
          </div>
       </div>
      </div>
     </div>
     <div class="dialog2 clearfix" style="display:none" id="stop_act_dialog">
		<p class="clearfix">
			<p class="message"><span><s:text name="ACT01_stopCampMessage"/></span></p>
		</p>
	</div>
	<div id="dialogTitle" class="hide"><s:text name="ACT01_campTitle"/></div>
<div id="dialogConfirm" class="hide"><s:text name="ACT01_oK"/></div>
<div id="dialogCancel" class="hide"><s:text name="ACT01_cancle"/></div>
</s:i18n>

<%--会员活动停用URL --%>
<span id ="stopSubCampaignUrl" style="display:none">${s_subCampaignUrl}</span>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
