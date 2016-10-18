<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<script type="text/javascript">
$(function () {
	$('#actProList').find("tr.gray,tr.red").cluetip({width: '500',splitTitle:'|'});
});
</script>  

<s:if test="%{campaignDetailInfo != null && !campaignDetailInfo.isEmpty()}">
<s:i18n name="i18n.mb.BINOLMBMBM07">
<div class="detail_box" style="line-height: 20px;">
<div class="section">
<%-- ==========================================活动详细———>基本信息=========================================== --%>
  <div class="box4" style="margin-top:0px;">
	<div class="box4-header">
	  <strong><s:text name="binolmbmbm07_campaignDetail" /></strong>
	</div>
	<div class="box4-content">
	  	<table class="detail" style="margin-bottom:0px;">
	      <tr>
		    <th><s:text name="binolmbmbm07_activityName"/></th>
		    <td><s:property value="campaignDetailInfo.activityName"/></td>
		    <th><s:text name="binolmbmbm07_subCampaignCode"/></th>
		    <td><s:property value="campaignDetailInfo.activityCode"/></td>
	      </tr>
	      <tr>
		    <th><s:text name="binolmbmbm07_campaignType"/></th>
		    <td><s:property value='#application.CodeTable.getVal("1174", campaignDetailInfo.activityType)' /></td>
		    <th><s:text name="binolmbmbm07_description"/></th>
		    <td><span><s:property value="campaignDetailInfo.descriptionDtl"/></span></td>
	      </tr>
	    </table>
	</div>
  </div>

<%-- ==========================================活动详细———>活动范围=========================================== --%>
<s:if test="%{campaignDetailInfo.campPlaceList != null}">
<div class="box4" style="margin-top:0px;">
  <div class="box4-header">
	<strong><s:text name="binolmbmbm07_campaignPlace" /></strong>
  </div>
  <div class="box4-content clearfix">
    <div style="width:15%;float:left;">
   		<span style="width:15%;white-space: nowrap;">
   			<span class="ui-icon icon-arrow-crm"></span>
   	 		<s:property value="#application.CodeTable.getVal('1156',campaignDetailInfo.actLocationType)"/>
   		</span>
	</div>
    <div style="width:85%;float:left;">
    	<s:if test='campaignDetailInfo.actLocationType==2 || campaignDetailInfo.actLocationType==4 || campaignDetailInfo.actLocationType==6'>
        	<ul>
        	<s:iterator value="campaignDetailInfo.campPlaceList">
				<li class="left" style="width:30%;white-space: nowrap;">
					<span style="background-color: #FFFFFF;margin:5px 0px;width:210px;">
						(<s:property value="counterCode"/>)<s:property value="counterName"/>
					</span>
				</li>
			</s:iterator>
			</ul>
		</s:if>
		<s:elseif test='campaignDetailInfo.actLocationType==1 || campaignDetailInfo.actLocationType==3'>
			<ul>
				<s:iterator value="campaignDetailInfo.campPlaceList">
					<li class="left" style="width:10%;">
						<s:if test='!"".equals(placeName)' >
							<span style="background-color: #FFFFFF;margin:5px 0px;width:150px;"><s:property value="placeName"/></span>
						</s:if>
					</li>
				</s:iterator>
			</ul> 
		</s:elseif>
    </div>
  </div>
</div>
</s:if>

<%-- ==========================================活动详细———>活动奖励=========================================== --%>	
<s:if test="3 == map.virtualPrmFlag">
	<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM38_4.jsp" flush="true"/>
</s:if>
<s:else>
	<jsp:include page="/WEB-INF/jsp/ss/prm/BINOLSSPRM38_3.jsp" flush="true"/>
</s:else>

</div> 
</div>
</s:i18n>
</s:if>


