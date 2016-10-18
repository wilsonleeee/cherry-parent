<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<s:i18n name="i18n.cp.BINOLCPACT01">
<div class="crm_content_header">
      <span class="icon_crmnav"></span>
        <span><s:text name="ACT04_actDetail" /> &gt;<s:text name="ACT04_actBaseInfo" /> </span>
            </div>
        	<div class="panel-content">
		     <div class="box4" style="margin-top:0px;">
                        <div class="box4-header"><strong><s:text name="ACT04_actSubBaseInfo" /></strong></div><%--主题活动基本信息--%>
						<div class="box4-content">
			    <table  class="detail">
			  	  <tbody>	
			  	  <tr>
			  	  	  <th><s:text name="ACT01_campCode" /></th><%--主题活动代码--%>
			  	  	  <td><span><s:property value="campaignInfo.campaignCode"/></span></td>
			  	  	   <th><s:text name="ACT04_actCreatByName" /></th><%-- 创建者 --%>
            		  <td><span><s:property value="campaignInfo.campaignSetByName"/></span></td>
			  	  </tr>		  	  
			  	  <tr>
			  	  	  <th><s:text name="ACT04_actCampName" /></th><%--主题活动名称--%>
			  	  	  <td><span><s:property value="campaignInfo.campaignName"/></span></td>
			  	  	  <th><s:text name="ACT04_actBrandName" /></th><%--所属品牌--%>
			  	  	  <td><span><s:property value="campaignInfo.brandName"/></span></td>
			  	  </tr>
             	  <tr>
             		  <th><s:text name="ACT04_actMainCampMebType" /></th><%--主题活动类型--%>
	           		  <td><span>
	           		  <s:property value='#application.CodeTable.getVal("1174", campaignInfo.campaignType)'/>
	           		   <s:if test='"DHHD".equals(campaignInfo.campaignType)'>
	           		  <s:if test='null != campaignInfo.clubName && !"".equals(campaignInfo.clubName)'>
                             &nbsp;(<s:property value="campaignInfo.clubName"/>)
                        	</s:if>
                       </s:if>
	           		  </span>
	           		  </td>
             		  <th><s:text name="ACT01_campType" /></th><%--活动类型--%>
	           		  <td>  
	           		  	<s:if test='"LYHD".equals(campaignInfo.campaignType)'>
                             <s:property value="#application.CodeTable.getVal('1247',campExtInfoMap.subCampaignType)"/>
                        </s:if>
                        <s:elseif test='"DHHD".equals(campaignInfo.campaignType)'>
                            <s:property value="#application.CodeTable.getVal('1229',campExtInfoMap.subCampaignType)"/>
                            
                        </s:elseif>
                         <s:elseif test='"CXHD".equals(campaignInfo.campaignType)'>
                            <s:property value="#application.CodeTable.getVal('1228',campExtInfoMap.subCampaignType)"/>
                        </s:elseif>
					</td>
				  </tr>
				  <s:if test='campaignInfo.campaignType == "DHHD"'>
					  <tr>
	             		  <th><s:text name="ACT04_exPointDeadDate" /></th><%--积分截止日期--%>
		           		  <td><span><s:property value='campaignInfo.exPointDeadDate'/></span></td>
	             		  <th><s:text name="ACT04_exPointDeductFlag" /></th><%--积分扣除时间--%>
		           		  <td><s:property value="#application.CodeTable.getVal('1255',campaignInfo.exPointDeductFlag)"/></td>
					  </tr>
				  </s:if>
				  <tr>
					  <th><s:text name="ACT04_actVerRules" /></th><%-- 活动验证 --%>
					  <td>
					  <span><s:property value="#application.CodeTable.getVal('1230',campExtInfoMap.subCampaignValid)"/></span>
					  </td>
					  <th>
						<span class="left"><s:text name="ACT04_verifCodeRule" /></span><%-- 验证规则 --%>
					  </th>
					  <td>
					     <span><s:property value="campExtInfoMap.localValidRule"/></span>
					  </td>
				  </tr>
				  <tr>
				 	  <th><s:text name="ACT04_IsCollectInfo" /></th><%-- 是否采集活动获知方式--%>
					  <td><s:property value="#application.CodeTable.getVal('1234',campExtInfoMap.isCollectInfo)"/></td>
 					  <th><s:text name="ACT04_needBuyFlag" /></th><!-- 是否需要购买-->
					  <td><s:property value="#application.CodeTable.getVal('1235',campaignInfo.needBuyFlag)"/></td>
				  </tr>
		         <tr>
		         	  <th><s:text name="ACT04_goodsChangeable" /></th><%-- 终端可否更改礼品构成--%>
					  <td><s:property value="#application.CodeTable.getVal('1040',campaignInfo.goodsChangeable)"/></td>
           		  	  <th><s:text name="ACT04_SendFlag" /></th><%-- 是否下发活动--%>
           		  	  <td><s:property value="#application.CodeTable.getVal('1233',campaignInfo.sendFlag)"/></td>
           		 </tr>
           		 <tr>
           		 	  <th><s:text name="ACT04_gotCnt" /></th><%-- 领取柜台--%>
           		  	  <td><s:property value="#application.CodeTable.getVal('1254',campaignInfo.gotCounter)"/></td>
           		  	  <th><s:text name="ACT04_actDescription" /></th><%-- 活动描述--%>
           		  	  <td style="word-wrap:break-word;word-break:break-all"><p><s:property value="campaignInfo.descriptionDtl"/></p></td>
           		 </tr>
			    </tbody></table>
			  </div>
			</div>
			 <div class="box4" style="margin-top:0px;">
                        <div class="box4-header"><strong><s:text name="ACT04_actCampTime" /></strong></div><%--活动阶段--%>
						<div class="box4-content clearfix">
							<table cellspacing="0" cellpadding="0" class="detail" style="float: left; width: 100%; margin-bottom: 0px;">
					<thead>
						<tr><th style="width:40%"><span><s:text name="ACT04_actMainCampTime" /></span></th>
						<th style="width:60%">
						<span class="left"><s:text name="ACT04_actCampTime" /></span>
						 </th>
					</tr></thead>
					<tbody>
						<tr>
							<td>
								<div style="margin:0 auto;width:200px;">
		             		 	<span>
			             		 	<span><s:property value="campaignInfo.campaignFromDate"/></span>
			             		 	<span style="margin: 0 5px;"><s:text name="ACT04_actDao" /></span>
			             		 	<span><s:property value="campaignInfo.campaignToDate"/></span>	
		             		 	</span>
							    </div>
							</td>
							<td>
								<ul class="sortable">
								<li style="margin:5px 0px;" id="3" class="<s:if test='campaignInfo.campaignOrderFromDate==null'>hide</s:if>">
									<table  style="width:100%;" >
								        <tbody><tr>
								          <th style="width:20%;">
								          	<span><s:text name="ACT04_actResTimes" /></span><!-- 预约阶段 -->
								          </th>
								          <td style="width:80%;">
										          <span>
							             		 	<span><s:property value="campaignInfo.campaignOrderFromDate"/></span>
							             		 	<span style="margin: 0 5px;"><s:text name="ACT04_actDao" /></span>
							             		 	<span><s:property value="campaignInfo.campaignOrderToDate"/></span>	
						             		 	 </span>
								          </td>
								        </tr>
								    </tbody>
								    </table>
								    <div class="clearfix"></div>
								</li>
								<li style="margin:5px 0px;" id="3" class="<s:if test='campaignInfo.campaignStockFromDate==null'>hide</s:if>">
									<table style="width:100%;">
								        <tbody><tr>
								          <th style="width:20%;">
								          	<span><s:text name="ACT04_actStockingTime" /></span><!--备货阶段 -->
								          </th>
								          <td style="width:80%;">
										          <span>
							             		 	<span><s:property value="campaignInfo.campaignStockFromDate"/></span>
							             		 	<span style="margin: 0 5px;"><s:text name="ACT04_actDao" /></span>
							             		 	<span><s:property value="campaignInfo.campaignStockToDate"/></span>	
						             		 	 </span>
								          </td>
								        </tr>
								    </tbody>
								    </table>
								    <div class="clearfix"></div>
								</li>
								<li style="margin:5px 0px;" id="3" >
									<table  style="width:100%;">
								        <tbody><tr>
								          <th style="width:20%;">
								          	<span><s:text name="ACT04_actObtainTime" /></span><!--领用阶段 -->
								          </th>
								          <td style="width:80%;">
								          	<s:if test='campaignInfo.obtainFromDate != null'>
									          	<span>
							             		 	<span><s:property value="campaignInfo.obtainFromDate"/></span>
							             		 	<span style="margin: 0 5px;"><s:text name="ACT04_actDao" /></span>
							             		 	<span><s:property value="campaignInfo.obtainToDate"/></span>	
						             		 	 </span>
								          	</s:if>
										    <s:else>
										    	<s:if test= 'campaignInfo.referType == "1"'><s:text name="ACT04_referType1" /></s:if>
										    	<s:elseif test='campaignInfo.referType == "2"'><s:text name="ACT04_referType2" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "3"'><s:text name="ACT04_referType3" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "4"'><s:text name="ACT04_referType4" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "5"'><s:text name="ACT04_referType5" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "6"'><s:text name="ACT04_referType6" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "7"'><s:text name="ACT04_referType7" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "8"'><s:text name="ACT04_referType8" /></s:elseif>
										    	<s:elseif test='campaignInfo.referType == "9"'><s:text name="ACT04_referType9" /></s:elseif>
										    	<s:if test= 'campaignInfo.attrA == "1"'><s:text name="ACT04_attrA1" /></s:if>
										    	<s:elseif test='campaignInfo.attrA == "2"'><s:text name="ACT04_attrA2" /></s:elseif>
										    	<s:property value="campaignInfo.valA"/>
										    	<s:if test= 'campaignInfo.attrB == "1"'><s:text name="ACT04_attrB1" /></s:if>
										    	<s:elseif test='campaignInfo.attrB == "2"'><s:text name="ACT04_attrB2" /></s:elseif>
												<s:text name="ACT04_valB"/>
												<s:property value="campaignInfo.valB"/>
										    	<s:if test= 'campaignInfo.attrC == "1"'><s:text name="ACT04_attrB1" /></s:if>
										    	<s:elseif test='campaignInfo.attrC == "2"'><s:text name="ACT04_attrB2" /></s:elseif>
										    </s:else>
								          </td>
								        </tr>
								    </tbody>
								    </table>
								    <div class="clearfix"></div>
								</li>
			    				</ul>
							</td>
						</tr>
					</tbody>
				</table>
			  </div>
			</div>
           </div>
         </s:i18n>
