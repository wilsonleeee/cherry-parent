<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<div id="aaData">
    <s:iterator value="popCampObjList" id="popCampObjMap">
        <ul>
            <li>
                <input type="radio" name="campaignRuleID" value='<s:property value="#popCampObjMap.campaignRuleID"/>'/>
                <input type="hidden" name="subCampaignCode" value='<s:property value="#popCampObjMap.subCampaignCode"/>'/>
                <input type="hidden" name="subCampaignName" value='<s:property value="#popCampObjMap.subCampaignName"/>'/>
            </li>
            <li><s:property value="#popCampObjMap.subCampaignCode"/></li>
            <li><s:property value="#popCampObjMap.subCampaignName"/></li>
        </ul>
    </s:iterator>
</div>