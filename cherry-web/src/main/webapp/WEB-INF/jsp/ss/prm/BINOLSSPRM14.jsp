<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM14_15.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM14.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<%@ page import="com.cherry.ss.common.PromotionConstants" %>
<s:i18n name="i18n.ss.BINOLSSPRM14">
<s:text id="selectAll" name="global.page.all"/>
<%-- ================== js国际化  START ======================= --%>
<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM13_2.jsp" flush="true" />
<%-- ================== js国际化导入    END  ======================= --%>
<script type="text/javascript">
$(document).ready(function() {
	// 假日
	holidays = '${holidays}';
	// js 国际化变量设置
	BINOLSSPRM14_js_i18n.counter = '<s:text name="counter"/>';
	BINOLSSPRM14_js_i18n.channel = '<s:text name="channel"/>';
	BINOLSSPRM14_js_i18n.city = '<s:text name="city"/>';

	activeSearchCalEventBind();
});
</script>
	<input type="hidden" id="loadRuleMsg_1" value="<s:text name="loadRuleMsg_1"/>"/>
	<input type="hidden" id="loadRuleMsg_2" value="<s:text name="loadRuleMsg_2"/>"/>
	<input type="hidden" id="publishMsg_0" value="<s:text name="publishMsg_0"/>"/>
	<input type="hidden" id="publishMsg_1" value="<s:text name="publishMsg_1"/>"/>
	<input type="hidden" id="publishMsg_100" value="<s:text name="publishMsg_100"/>"/>
	<input type="hidden" id="publishMsg_101" value="<s:text name="publishMsg_101"/>"/>
	<input type="hidden" id="publishMsg_102" value="<s:text name="publishMsg_102"/>"/>
	<input type="hidden" id="checkMsg_0" value="<s:text name="checkMsg_0"/>"/>
	<input type="hidden" id="checkMsg_201" value="<s:text name="checkMsg_201"/>"/>
	<%-- URL set start --%>
	<%--新增促销活动URL --%>
    <s:url id="s_newPrmActiveUrl" value="/ss/BINOLSSPRM13" >
    	<s:param name="initFlag">1</s:param>
    </s:url>
    <s:url id="s_loadRuleUrl" value="/ss/BINOLSSPRM14_loadRule" />
    <%--新增促销活动URL --%>
    <s:url id="s_newPrmRuleUrl" value="/ss/BINOLSSPRM68_init" >
    	<s:param name="opt">1</s:param>
    </s:url>
    <%--促销活动查询URL --%>
	<s:url id="s_searchActiveUrl" value="/ss/BINOLSSPRM14_searchActive" />
    <%--促销活动下发URL --%>
    <s:url id="s_publicActiveUrl" value="/common/BINOLCM09_publicActive" />
    <%--促销活动停用URL --%>
    <s:url id="s_stopPrmActiveUrl" value="/ss/BINOLSSPRM37_stopPrmActive" />
    <%--促销主活动名查询URL --%>
    <s:url id="s_indSearchPrmGrpNameUrl" value="/ss/BINOLSSPRM14_indSearchPrmGrpName" />
    <%-- URL set end --%>
	<s:set id="PRM_ACT_SPACE_OF_TIME"><%=PromotionConstants.PRM_ACT_SPACE_OF_TIME%></s:set>
    <div class="panel-header">
        <div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
        	<cherry:show domId="SSPRM0214ADD">
	        	<a class="add right" id="newActive" href ="${s_newPrmActiveUrl}" onclick="openWin(this);return false;">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="newActive"/></span>
	        	</a>
        	</cherry:show>
        	<cherry:show domId="SSPRM0214RULE">
        		<a class="add right" id="newActive" href ="${s_newPrmRuleUrl}" onclick="openWin(this);return false;">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="newRule"/></span>
	        	</a>
	        </cherry:show>
	        <cherry:show domId="SSPRM0214LOAD">
	        	<a class="add right" href ="${s_loadRuleUrl}" onclick="loadRule('${s_loadRuleUrl}');return false;">
	        		<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="loadRule"/></span>
	        	</a>
	        </cherry:show>
        </div>
	</div>
    
	  <%-- ================== 错误信息提示 START ======================= --%>
		<div id="errorMessage"></div>
	  <%-- ================== 错误信息提示   END  ======================= --%>
      
	<div class="panel-content">
		<div class="box">
			<%-- form start --%>
        	<cherry:form id = "mainForm" class ="inline" >
	          	<%--查询条件 --%>
	            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="searchCondition"/></strong><input id ="actValidFlag" type="checkbox" value="1" name="actValidFlag"><s:text name="containStop"/></div>
	            <div class="box-content clearfix">
	            	<input type="hidden" id="actState" name="actState" value="in_progress"/>
	            	<div class="column" style="width:49%; height:100px;">
	                	<p>
	                  		<%-- 活动名称 --%>	
			               	<label style="width:80px;"><s:text name="searchPrmActiveName"/></label>
			               	<s:textfield name="searchPrmActiveName" cssClass="text" id="prmActiveName"/>
			               	<s:hidden name="activityCode" id="activityCode"></s:hidden>
	                	</p>
	                	<p>
	                  		<%--活动商品 --%>
	                  		<label style="width:80px;"><s:text name="searchPrmProduct"/></label>
                  			<s:textfield name="searchPrmProduct" cssClass="text" id="prmProduct"/>
               				<s:hidden name="prmProductId" id="prmProductId"></s:hidden>
               				<s:hidden name="prtType" id="prtType"></s:hidden>
	                	</p>
	                	<p class="date">
	                  		<%--开始日期 --%>
	                  		<label style="width:80px;"><s:text name="searchPrmDate"/></label>
	                  		<span>
	                  		<s:textfield id="searchPrmStartDate" name="searchPrmStartDate" cssClass="date" value ="%{searchPrmStartDate}" ></s:textfield>
	                  		</span>
	                  		-
	                  		<span>
	                  		<%--结束日期 --%>
	                  		<s:textfield id="searchPrmEndDate" name="searchPrmEndDate" cssClass="date" value ="%{searchPrmEndDate}" ></s:textfield>
	                  		</span>
	                	</p>
	              	</div>
	            	<div class="column last" style="width:50%; height:50px;">
	            		<p>
	                  		<%--促销主活动名 --%>
	                  		<label style="width:80px;"><s:text name="searchPrmGrpName"/></label>
	                  		<s:textfield name="searchPrmGrpName" cssClass="text" id="groupName"/>
	                  		<s:hidden name="groupCode" />
	                	</p>
	                	<p>
	                  		<%--活动柜台--%>
	                  		<label style="width:80px;"><s:text name="searchPrmLocation"/></label>
	                  		<s:textfield name="searchPrmLocation" cssClass="text" id="prmCounter"/>
                			<s:hidden name="prmCounterId" id="prmCounterId"></s:hidden>
	                	</p>
	                	<p class="clearfix">
							<%-- 主活动类型 --%>
							<label style="width:80px;"><s:text name="activeType"/></label>
							<span class="text">
								<s:select name="activeType"  list='#application.CodeTable.getCodes("1174")' listKey="CodeKey" listValue="Value"
								headerKey="" headerValue="%{selectAll}" value="1"/>
							</span>
						</p>
	              	</div>
	            </div>
	            <div class="clearfix">
	            	<%--<cherry:show domId="SSPRM0214QU">--%>
		              	<button class="right search" onclick="search();return false;">
		              		<span class="ui-icon icon-search-big"></span>
		              		<span class="button-text"><s:text name="searchPrmActive"/></span>
		              	</button>
	              	<%--</cherry:show>--%>
	            </div>
         	</cherry:form>
         	<%-- form end --%>
		</div>
        <div class="section hide" id="section">
			<div class="section-header" id="section-header">
				<strong>
	          		<span class="ui-icon icon-ttl-section-search-result"></span>
	          		<s:text name="searchResult"/>
	          	</strong>
	        </div>
	        <div id="errorMessage2"></div>
	        <%-- tab start --%>
			<div id ="ui-tabs" class="ui-tabs">
	          	<ul class ="ui-tabs-nav clearfix">
		            <%--进行中--%>
		            <li id="tab2_li" class="ui-tabs-selected" onclick = "prmActDateFilter('in_progress',this);"><a><s:text name="in_progress"/></a></li>
		            <%--未开始--%>
		            <li id="tab3_li" onclick = "prmActDateFilter('not_start',this);"><a><s:text name="not_start"/></a></li>
		            <%--已过期--%>
		            <li id="tab4_li" onclick = "prmActDateFilter('past_due',this);"><a><s:text name="past_due"/></a></li>
		            <%--模版--%>
		            <li id="tab5_li" onclick = "prmActDateFilter('template',this);"><a><s:text name="template"/></a></li>
	          	</ul>
				<div id="tabs-1" class="ui-tabs-panel" style="overflow-x:auto;overflow-y:hidden;">
	            	<table id ="act_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
						<thead>
				        	<tr>
				            	<%--活动名--%>
				                <th><s:text name="activeName"/><span class="ui-icon ui-icon-document"></span></th>
				            	<%--活动编码--%>
				                <th><s:text name="activityCode"/></th>
				            	<%--主活动名--%>
				                <th><s:text name="prmGrpName"/></th> 
				                <%--开始时间--%>
				                <th><s:text name="startTime"/></th>
				                <%--结束时间--%>
				                <th><s:text name="endTime"/></th>
				                <%--设置人--%>
				                <th><s:text name="creatUser"/></th>
				            	<%--停用区分--%>
				                <th><s:text name="stopFlag"/></th>
				                <%--操作--%>
				                <th><s:text name="operator"/></th>
							</tr>
				        </thead>
					</table>
	          	</div>
			</div>
			<%-- tab end --%>
		</div>
	</div>
	
	<div class="dialog2 clearfix" style="display:none" id="stop_act_dialog">
		<p class="clearfix message">
			<span></span>
			<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
		</p>
	</div>
	<div class="dialog2 clearfix" style="display:none" id="public_act_dialog">
		<p class="clearfix message">
			<span></span>
			<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
		</p>
	</div>
	<div class="dialog2 clearfix" style="display:none" id="check_act_dialog">
		<p class="clearfix message">
			<span></span>
			<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
		</p>
	</div>
</s:i18n>

<%-- for js URL start --%>

<%--促销活动查询URL --%>
<span id ="searchActiveUrl" style="display:none">${s_searchActiveUrl}</span>
<%--促销活动停用URL --%>
<span id ="stopPrmActiveUrl" style="display:none">${s_stopPrmActiveUrl}</span>
<%--促销主活动名查询URL --%>
<span id ="indSearchPrmGrpNameUrl" style="display:none">${s_indSearchPrmGrpNameUrl}</span>


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
<%-- for js URL end --%>

<span id="spaceDays" style="display:none">${PRM_ACT_SPACE_OF_TIME}</span>

<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

