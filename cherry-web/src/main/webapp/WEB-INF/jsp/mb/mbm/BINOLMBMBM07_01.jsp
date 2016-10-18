<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  

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
		    <td><s:property value="campaignDetailInfo.subCampaignName"/></td>
		    <th><s:text name="binolmbmbm07_subCampaignCode"/></th>
		    <td><s:property value="campaignDetailInfo.subCampaignCode"/></td>
	      </tr>
	      <tr>
		    <th><s:text name="binolmbmbm07_campaignType"/></th>
		    <td>
				<s:if test='"CXHD".equals(campaignDetailInfo.campaignType)'>
	             	<s:property value="#application.CodeTable.getVal('1228',campaignDetailInfo.subCampaignType)"/>
	            </s:if>
	            <s:if test='"LYHD".equals(campaignDetailInfo.campaignType)'>
	              	<s:property value="#application.CodeTable.getVal('1247',campaignDetailInfo.subCampaignType)"/>
	            </s:if>
	            <s:if test='"DHHD".equals(campaignDetailInfo.campaignType)'>
	             	<s:property value="#application.CodeTable.getVal('1229',campaignDetailInfo.subCampaignType)"/>
	        	</s:if>
		    </td>
		    <th><s:text name="binolmbmbm07_description"/></th>
		    <td><span><s:property value="campaignDetailInfo.description"/></span></td>
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
    	<s:if test='campaignDetailInfo.actLocationType==2 || campaignDetailInfo.actLocationType==4 || campaignDetailInfo.actLocationType==5'>
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

<%-- ==========================================子活动详细———>活动对象=========================================== --%>
<s:if test="%{campaignDetailInfo.campMebMap != null}">
<div class="box4" style="margin-top:0px;">
  <div class="box4-header">
  	<strong><s:text name="binolmbmbm07_campaignObject" /></strong>
  </div>
  <div class="box4-content clearfix">
  	<div class="clearfix">
      <div class="left" style="width:10%;">
       	<span class="ui-icon icon-arrow-crm"></span>
       	<s:property value="#application.CodeTable.getVal('1224',campaignDetailInfo.campMebMap.campMebType)"/>
      </div> 
      <p class="left" style="margin:0; width:90%;">
       	<s:action name="BINOLCM33_conditionDisplay" namespace="/common" executeResult="true">
       		<s:param name="reqContent" value="campaignDetailInfo.campMebMap.conInfo" />
       	</s:action>
      </p>
	</div>      
  </div>           
</div>
</s:if>

<%-- ==========================================活动详细———>活动奖励=========================================== --%>		
<s:if test="%{campaignDetailInfo.subResultInfoList != null}">  
  <div class="box4" style="margin-top:0px;">
    <div class="box4-header">
	  <strong><s:text name="binolmbmbm07_campaignResult"/></strong>
    </div>
    <div class="box4-content">
	    <table style="width: 100%;margin-top:5px;">
		  <thead>
			<tr>
		  		<th><s:text name="binolmbmbm07_unitCode"/></th><%--礼品编码--%>
		  		<th><s:text name="binolmbmbm07_nameTotal"/></th><%--礼品名称--%>
		  		<th><s:text name="binolmbmbm07_barCode"/></th><%--礼品条码--%>
		  		<th><s:text name="binolmbmbm07_exPoint"/></th><%--积分值--%>
		  		<th><s:text name="binolmbmbm07_price"/></th><%--礼品单价--%>
		  		<th><s:text name="binolmbmbm07_quantity"/></th><%--礼品数量--%>
			</tr>
		  </thead>
	 	  <tbody> 
		 	<s:iterator value="campaignDetailInfo.subResultInfoList">
		 		<tr class='<s:if test='"DHMY".equals(prmCate)||"DHCP".equals(prmCate)||"TZZK".equals(prmCate)'>gray</s:if>'>
					<td><s:property value="unitCode"/></td>
					<td><s:property value="nameTotal"/></td>
					<td><s:property value="barCode"/></td>
					<td><s:property value="exPoint"/></td>
					<td>
					  <s:if test='%{price != null && !"".equals(price)}'>
					    <s:text name="format.price"><s:param value="price"></s:param></s:text>
					  </s:if>
					</td>
					<td><s:property value="quantity"/></td>
				</tr>
			</s:iterator>	
	  	  </tbody>
		</table>			
    </div>
  </div>   
</s:if>

</div> 
</div>
</s:i18n>
</s:if>


