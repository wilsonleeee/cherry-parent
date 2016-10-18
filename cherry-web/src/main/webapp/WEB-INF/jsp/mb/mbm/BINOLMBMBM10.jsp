<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<link rel="stylesheet" href="../css/common/blueprint/crm_web.css" type="text/css">
<link rel="stylesheet" href="/Cherry/css/cherry/gadgets.css" type="text/css">
<script type="text/javascript" src="/Cherry/gadgets/js/core:rpc:pubsub:shindig-container.js?c=1&debug=1"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryGadget.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/common/BINOLMBCOM01.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM10.js"></script>

<body>
<s:if test="hasActionErrors()">
<jsp:include page="/WEB-INF/jsp/common/actionResultBody.jsp" flush="true"/>
</s:if>
<s:else>
<s:if test='%{code != null && !"".equals(code)}'>
<s:hidden name="code" id="csrftoken"></s:hidden>
</s:if>
<s:i18n name="i18n.mb.BINOLMBMBM10">
<div class="crm_top clearfix">
	<span>
	<div class="icon_member"></div><s:text name="binolmbmbm10_memName" /><s:property value="memberInfoMap.name"/>
	<s:if test="%{memberInfoMap.gender == 1}"><s:text name="binolmbmbm10_gender1" /></s:if>
	<s:elseif test="%{memberInfoMap.gender == 2}"><s:text name="binolmbmbm10_gender2" /></s:elseif>
	</span>
	<span><div class="icon_memCode"></div><s:text name="binolmbmbm10_memCode" /><s:property value="memberInfoMap.memCode"/></span>
	<span><div class="icon_tel"></div><s:text name="binolmbmbm10_memMobilePhone" /><s:property value="memberInfoMap.mobilePhone"/></span>
	<span><div class="icon_class"></div><s:text name="binolmbmbm10_memLevelName" /><s:property value="memberInfoMap.levelName"/></span>
	<div style="float:right;margin:7px 15px 0 0;line-height:19px;">
		<s:url action="BINOLMBMBM27_init" id="addIssueUrl">
		  <s:param name="memberInfoId" value="memberInfoId"></s:param>
		</s:url>
		<s:text name="binolmbmbm10_showButton" id="showButtonText"></s:text>
		<s:text name="binolmbmbm10_hiddenButton" id="hiddenButtonText"></s:text>
		<div class="hide">
		  <span id="showButtonDiv">${showButtonText }</span>
		  <span id="hiddenButtonDiv">${hiddenButtonText }</span>
		</div>
		<a id="expandIssueButton" class="ui-select" style="margin-left: 10px; font-size: 12px;float:right">
			<span class="button-text choice" style="line-height: normal;line-height: 22px\9;margin: 0 10px 0 0;margin: 0 15px 0 0\9;min-width: 20px;padding: 1px 10px;padding: 1px 20px 1px 5px\9;" id="expandCharacter">${showButtonText }</span>
			<span class="ui-icon ui-icon-triangle-1-n" id="expandDirection" style="line-height:normal;margin-right: 5px;right:0px;right:15px\9;"></span>
		</a>
		<a class="add" onclick="binolmbmbm10.addIssue('${addIssueUrl}');return false;" id="addIssueButton" style="margin-left: 10px; font-size: 12px;float:right">
		  <span class="ui-icon icon-add" style="line-height:normal;left:5px;"></span>
		  <span class="button-text" style="line-height:normal;line-height: 22px\9;margin:0 0 0 5px;"><s:text name="binolmbmbm10_addIssue"></s:text></span>
		</a>
		<%-- 
		<a id="addIssueButton" style="margin-left: 10px; font-size: 12px;width:95px" class="ui-select right">
			<span class="ui-icon icon-add" style="line-height:normal;left:5px;"></span>
	        <span class="button-text choice" style="line-height:normal;margin:0 0 0 20px;"><s:text name="binolmbmbm10_addIssue"></s:text></span>
	 		<span class="ui-icon ui-icon-triangle-1-s"style="line-height:normal;margin-right: 0px;right:5px;" id="expandDirection"></span>
 	    </a>
 	    --%>
    </div>    
</div>

<div class="crm_main container clearfix">
    <div class="crm_left" id="crmMenuDiv">
        <div class="crm_menu">
            <form id="memDetailForm">
				<s:hidden name="memberInfoId"></s:hidden>
			</form>
            <ul>
                <s:url action="BINOLMBMBM10_topInit" id="memTopUrl"></s:url>
                <li><a dir="${memTopUrl }" id="topMenu" class="menuOne"><s:text name="binolmbmbm10_memTop" /></a></li>
                <li><a class="menuOne"><s:text name="binolmbmbm10_memInfo" /></a>
                	<ul>
                    	<s:url action="BINOLMBMBM02_init" id="memberDetailUrl"></s:url>
                    	<li><a dir="${memberDetailUrl }"><s:text name="binolmbmbm10_memInfoShow" /></a></li>
                    	<s:url action="BINOLMBMBM21_init" id="referrerUrl">
                    	  <s:param name="referrerId" value="%{memberInfoMap.referrerId}"></s:param>
                    	  <s:param name="memName" value="%{memberInfoMap.name}"></s:param>
                    	</s:url>
                    	<li><a dir="${referrerUrl }"><s:text name="binolmbmbm10_referrerInfo" /></a></li>
                    	<cherry:show domId="BINOLMBMBM10_27">
                    	<s:url action="BINOLMBMBM22_init" id="memAnswerUrl"></s:url>
                    	<li><a dir="${memAnswerUrl }"><s:text name="binolmbmbm10_memAnswerInfo" /></a></li>
                    	</cherry:show>
                    	<s:if test="%{memberInfoMap.memInfoRegFlg != null && memberInfoMap.memInfoRegFlg == 0}">
                    	<cherry:show domId="BINOLMBMBM10_24">
                    	<s:url action="BINOLMBMBM06_init" id="memberEditUrl"></s:url>
                    	<li><a dir="${memberEditUrl }"><s:text name="binolmbmbm10_memInfoUpd" /></a></li>
                    	</cherry:show>
                    	</s:if>
                    	<cherry:show domId="BINOLMBMBM10_25">
                    	<s:url action="BINOLMBMBM14_init" id="memInfoRecordUrl"></s:url>
                    	<li><a dir="${memInfoRecordUrl }"><s:text name="binolmbmbm10_memInfoRecord" /></a></li>
                    	</cherry:show>
                    	<cherry:show domId="BINOLMBMBM16">
                    	<s:url action="BINOLMBMBM16_relegationInit" id="relegationUrl"></s:url>
                    	<li><a dir="${relegationUrl }"><s:text name="binolmbmbm10_memHistory" /></a></li>
                    	</cherry:show>
                    	<s:if test="%{memberInfoMap.memInfoRegFlg != null && memberInfoMap.memInfoRegFlg == 0 && memberInfoMap.memCode != null}">
                    	<cherry:show domId="BINOLMBMBM10_20">
                    	<s:url action="BINOLMBMBM03_editinit" id="memProUpdUrl"></s:url>
                    	<li><a dir="${memProUpdUrl }"><s:text name="binolmbmbm10_memProUpd" /></a></li>
                    	</cherry:show>
                    	</s:if>
                    	<cherry:show domId="BINOLMBMBM10_21">
                    	<s:url action="BINOLMBMBM03_init" id="memProUpdRecordUrl"></s:url>
                    	<li><a dir="${memProUpdRecordUrl }"><s:text name="binolmbmbm10_memProUpdRecord" /></a></li>
                    	</cherry:show>
                    </ul>
                </li>
                <li><a class="menuOne"><s:text name="binolmbmbm10_memSaleInfo" /></a>
                	<ul>
                    	<s:url action="BINOLMBMBM05_init" id="SaleDetailInitUrl"></s:url>
                    	<li><a dir="${SaleDetailInitUrl }"><s:text name="binolmbmbm10_memSaleInfoShow" /></a></li>
                    </ul>
                </li>
                <li><a class="menuOne"><s:text name="binolmbmbm10_memPointInfo" /></a>
                	<ul>
                    	<s:url action="BINOLMBMBM04_init" id="PointDetailInitUrl"></s:url>
                    	<li><a dir="${PointDetailInitUrl }"><s:text name="binolmbmbm10_memPointInfoShow" /></a></li>
                    	<s:if test="%{memberInfoMap.memInfoRegFlg != null && memberInfoMap.memInfoRegFlg == 0 && memberInfoMap.memCode != null}">
                    	<cherry:show domId="BINOLMBMBM10_22">
                    	<s:url action="BINOLMBMBM08_init" id="PointUpdateUrl"></s:url>
                    	<li><a dir="${PointUpdateUrl}"><s:text name="binolmbmbm10_memPointInfoUpd" /></a></li>
                    	</cherry:show>
                    	</s:if>
                    	<cherry:show domId="BINOLMBMBM10_23">
                    	<s:url action="BINOLMBMBM08_initPointHis" id="PointHisUrl"></s:url>
                    	<li><a dir="${PointHisUrl}"><s:text name="binolmbmbm10_memPointInfoUpdRecord" /></a></li>
                    	</cherry:show>
                    </ul>
                </li>
                <cherry:show domId="BINOLMBMBM10_28">
                <li><a class="menuOne"><s:text name="binolmbmbm10_memCampaignInfo" /></a>
                	<ul>
                    	<s:if test="%{memberInfoMap.memInfoRegFlg != null && memberInfoMap.memInfoRegFlg == 0 && memberInfoMap.memCode != null}">
                    	<s:url action="BINOLMBMBM07_init" id="currentCampaignInitUrl"></s:url>
                    	<li><a dir="${currentCampaignInitUrl }"><s:text name="binolmbmbm10_memCampaignCurrent" /></a></li>
                    	<s:url action="BINOLMBMBM07_searchCampaignOrder" id="searchCampaignOrderUrl"></s:url>
                    	<li><a dir="${searchCampaignOrderUrl }"><s:text name="binolmbmbm10_memCampaignOrder" /></a></li>
                    	</s:if>
                    	<s:url action="BINOLMBMBM07_campaignHistoryInit" id="campaignHistoryInitUrl"></s:url>
                    	<li><a dir="${campaignHistoryInitUrl }"><s:text name="binolmbmbm10_memCampaignHistory" /></a></li>
                    </ul>
                </li>
                </cherry:show>
                <cherry:show domId="BINOLMBMBM10_26">
                <li><a class="menuOne"><s:text name="binolmbmbm10_communicationInfo" /></a>
                	<ul>
                    	<s:url action="BINOLMBMBM23_init" id="searchSmsSendDetailInitUrl">
                    		<s:param name="mobilePhone" value="%{memberInfoMap.mobilePhone}"></s:param>
                    	</s:url>
                    	<li><a dir="${searchSmsSendDetailInitUrl }"><s:text name="binolmbmbm10_smsSendDetail" /></a></li>
                    </ul>
                </li>
                </cherry:show>
                <li><a class="menuOne"><s:text name="binolmbmbm10_memIssue" /></a>
                	<ul>
                    	<s:url action="BINOLMBMBM26_init" id="searchIssueInitUrl"></s:url>
                    	<li><a dir="${searchIssueInitUrl }"><s:text name="binolmbmbm10_memIssue" /></a></li>
                    </ul>
                </li>
                <s:if test="%{hasMemWarnInfo == true}">
                <li><a class="menuOne"><s:text name="binolmbmbm10_memWarnInfo" /></a>
                	<ul>
                    	<s:url action="BINOLMBMBM20_init" id="memWarnInfoInitUrl"></s:url>
                    	<li><a dir="${memWarnInfoInitUrl }"><s:text name="binolmbmbm10_memWarnInfo" /></a></li>
                    </ul>
                </li>
                </s:if>
            </ul>
        </div>
    </div>
    <div class="crm_middle" id="crmArrowDiv"><a class="crm_l_arrow"></a></div>
    <div class="crm_right" id="crmContentDiv">
    	<div class="crm_issue hide" id="addIssueDiv">
    	  <s:action name="BINOLMBMBM27_init" executeResult="true"></s:action>
  		</div>
    	<div class="crm_content" id ="memDetailDiv"></div>
    </div>
</div>
</s:i18n>
</s:else>
</body>
</html>
