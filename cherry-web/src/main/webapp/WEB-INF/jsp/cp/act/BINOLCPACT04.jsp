<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp"%>
<link rel="stylesheet" href="/Cherry/css/cherry/template.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/crm_web.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/cp/act/BINOLCPACT04.js"></script>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div class="crm_top clearfix">
	<span><div class="icon_theme"></div><s:text name="ACT04_actCampName" />：<s:property value="campaignInfo.campaignName"/></span>
	<span><div class="icon_datego"></div><s:text name="ACT04_actMainCampDate" />：<s:property value="campaignInfo.campaignFromDate"/>&nbsp;<s:text name="ACT04_actDao" />&nbsp;<s:property value="campaignInfo.campaignToDate"/></span>
	<span><div class="icon_class"></div><s:text name="ACT04_actMainCampMebType" />：<s:property value='#application.CodeTable.getVal("1174", campaignInfo.campaignType)'/></span>
</div>
<div class="crm_main container clearfix">
    <div class="crm_left" id="actMenuDiv">
        <div class="crm_menu">
        	<form id="actDetailForm">
				<s:hidden name="campaignId"></s:hidden>
				<input type="hidden" id="parentCsrftoken" name="csrftoken"/>
			</form>
            <ul>
                <li><a class="menuOne on"><s:text name="ACT04_actSubDetail"/></a>
                	<ul>
                		<s:url action="BINOLCPACT02_actTopInit" id="actTopInitUrl"></s:url>
                    	<li><a dir="${actTopInitUrl }" id="topMenu"><s:text name="ACT04_actBaseInfo" /></a></li>
                    	 <s:iterator value="subMenuList" id="subMenu">
                    	 <s:url action="BINOLCPACT02_subInit" id="subInitUrl">
                    	 	<s:param name="subCampId"><s:property value="campRuleId"/></s:param>
                    	 </s:url>
                    	 <s:if test="subCampaignName.equals(subCampaignName_temp)">
                    	 <li class="gray" ><a <s:if test='validFlag == 0'>style="color:#AAA;"</s:if> dir="${subInitUrl }"><s:property value="subCampaignName"/></a></li>
                    	 </s:if>
                    	 <s:else>
                    	 <li class="gray TIP" title="<s:text name="ACT04_tip1"><s:param><s:property value="subCampaignName"/></s:param></s:text>"><a <s:if test='validFlag == 0'>style="color:#AAA;"</s:if> dir="${subInitUrl }"><s:property value="subCampaignName_temp"/></a></li>
                    	 </s:else>
                    	</s:iterator>
                    </ul>
                </li>
                 <li><a class="menuOne on"><s:text name="ACT04_actExecution"/></a>
	            	<s:if test='!"CXHD".equals(campaignInfo.campaignType)'>
	                	<ul>
	                		<s:url action="BINOLCPACT05_actBatchReservation" id="actBatchResUrl"></s:url>
	                    	<s:if test="menuMap.order == 1">
	                    	<li>
	                    	 <cherry:show domId="CPACT01BATCH">
				       		 	<a dir="${actBatchResUrl}" id="topMenu"><s:text name="ACT04_actReservation" /></a>
				       		 </cherry:show>
	                    	</li>
	                    	</s:if>
	                    	<s:url action="BINOLCPACT06_initRun" id="actBatchStockUrl">
	                    		<s:param name="state">RV</s:param>
	                    		<s:param name="newState">AR</s:param>
	                    	</s:url>
							<s:url action="BINOLCPACT06_initOrderDispatch" id="initOrderDispatchUrl">
							</s:url>
	                    	<s:if test="menuMap.stock == 1">
	                    	<li>
								<cherry:show domId="CPACT01SHIP">
									<a dir="${actBatchStockUrl}" id="topMenu"><s:text name="ACT04_actStock" /></a>
								</cherry:show>
	                    	</li>
							<li>
								<cherry:show domId="CPACT02SHIP">
									<a dir="${initOrderDispatchUrl}" id="topMenu"><s:text name="ACT04_actStock2" /></a>
								</cherry:show>
							</li>
	                    	</s:if>
	                    	<s:if test='campaignInfo.campaignStockFromDate == null || "".equals(campaignInfo.campaignStockFromDate)'>
		                    	<s:url action="BINOLCPACT06_initRun" id="actBatchCAUrl">
		                    		<s:param name="state">AR</s:param>
		                    		<s:param name="newState">CA</s:param>
		                    	</s:url>
	                    	</s:if>
	                    	<s:else>
	                    		<s:url action="BINOLCPACT06_initRun" id="actBatchCAUrl">
		                    		<s:param name="state">RV</s:param>
		                    		<s:param name="newState">CA</s:param>
		                    	</s:url>
	                    	</s:else>
	                    	<s:if test="menuMap.order == 1">
	                    	<li>
	                    	 <cherry:show domId="CPACT01BATCA">
				       		 	<a dir="${actBatchCAUrl}" id="topMenu"><s:text name="ACT04_actCancel" /></a>
				       		 </cherry:show>
	                    	</li>
	                    	</s:if>
	                    </ul>
	                 </s:if>
                </li>
                 <li><a class="menuOne on"><s:text name="ACT04_campResult"/></a>
               	 <s:if test='"DHHD".equals(campaignInfo.campaignType) && campaignInfo.campaignOrderFromDate==null'>
	                    <ul>
	                    <s:url action="BINOLCPACT06_init" id="initUrl"></s:url>
                    	<li>
                    	 <cherry:show domId="CPACT01RESULT">
			       		 	<a dir="${initUrl}" id="topMenu"><s:text name="ACT04_campdetail" /></a>
			       		 </cherry:show>
                    	</li>
	                		<s:url action="BINOLCPACT10_init" id="init10_Url">
	                		</s:url>
	                    	<li>
	                    	  <cherry:show domId="CPACT01EXCHANGE">
				       		 	<a dir="${init10_Url}" id="topMenu"><s:text name="ACT04_exChangeList" /></a>
				       		  </cherry:show>
	                    	</li>
	                    </ul>
                    </s:if>
                    <s:else>
                	<ul>
                		<s:url action="BINOLCPACT06_init" id="initUrl"></s:url>
                    	<li>
                    	 <cherry:show domId="CPACT01RESULT">
			       		 	<a dir="${initUrl}" id="topMenu"><s:text name="ACT04_campdetail" /></a>
			       		 </cherry:show>
                    	</li>
                    </ul>
                    <ul>
                		<s:url action="BINOLCPACT09_init" id="initUrl">
                		<s:param name="activityCode"><s:property value="campaignInfo.campaignCode"/></s:param>
                		</s:url>
                    	<li>
                   	     <cherry:show domId="CPACT01GOTRET">
			       		 	<a dir="${initUrl}" id="topMenu"><s:text name="ACT04_getResult" /></a>
			       		 </cherry:show>
                    	</li>
                    </ul>
                    <ul>
                		<s:url action="BINOLCPACT11_init" id="init11Url">
                		<s:param name="campCode"><s:property value="campaignInfo.campaignCode"/></s:param>
                		</s:url>
                    	<li>
                    	 <cherry:show domId="CPACT01HISTORY">
			       		 	<a dir="${init11Url}" id="topMenu"><s:text name="ACT04_actHistoryList" /></a>
			       		 </cherry:show>
                    	</li>
                    </ul>
                   </s:else>
                </li>
            </ul>
        </div>
    </div>
    <div class="crm_middle" id="crmArrowDiv"><a class="crm_l_arrow"></a></div>
    <div class="crm_right" id="crmContentDiv">
    	<div class="crm_content" id ="actDetailDiv"></div>
    </div>
</div>
<div class="hide" id="objDialogTitle"><s:text name="global.page.searchMemTitle" /></div>
<div class="hide" id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div class="hide" id="dialogCancel"><s:text name="global.page.cancle" /></div>
</s:i18n>
