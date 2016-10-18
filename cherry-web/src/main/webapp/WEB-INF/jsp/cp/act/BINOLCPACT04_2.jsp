<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.cp.BINOLCPACT01">
	<div class="crm_content_header">
    	<span class="icon_crmnav"></span>
       	<span><s:text name="ACT04_actSubDetail" /> &gt; <s:property value="subCampInfo.SubCampaignName"/></span>
   	</div>
   	<div id="div_main" class="panel-content clearfix">
		<div class="section">
        	<%-- ==========================================子活动详细———>基本信息=========================================== --%>				
			<div class="box4" style="margin-top:0px;">
            	<div class="box4-header">
            		<strong><s:text name="ACT04_actSubBaseInfo" /></strong><%--基本信息 --%>
                	<input type="hidden"  name="brandInfoId" id="brandInfoId" value='<s:property value="subCampInfo.brandInfoId"/>'/>
                </div>
                <div class="box4-content">
                	<table class="detail <s:if test='campaignInfo.campaignOrderFromDate==null'>hide</s:if>"  style="margin-bottom:0px;" >
                     	<tbody>
                          	<tr>
	                            <th><s:text name="ACT04_actSubCampName" /></th><%--活动名称 --%>
	                            <td><span ><s:property value="subCampInfo.SubCampaignName"/></span></td>
	                            <th><s:text name="ACT01_subcampCode" /></th><%--活动代码 --%>
	                            <td><span ><s:property value="subCampInfo.SubCampaignCode"/></span></td>
                          	</tr>
                           	<tr>
                           		<th><s:text name="ACT04_times" /></th><%--预约次数上限 --%>
                           		<td >
		                            <span>
			                            <s:if test='subCampInfo.Times>0'><s:property value="subCampInfo.Times"/></s:if>
			                            <s:else></s:else>
		                            </span>
	                            </td>
	                            <th><s:text name="ACT01_campType" /></th><%--活动类型 --%>
	                            <td>
		                            <s:if test='"CXHD".equals(campaignInfo.campaignType)'>
		                            	<s:property value="#application.CodeTable.getVal('1228',subCampInfo.SubCampaignType)"/>
		                            </s:if>
		                            <s:if test='"LYHD".equals(campaignInfo.campaignType)'>
		                             	<s:property value="#application.CodeTable.getVal('1247',subCampInfo.SubCampaignType)"/>
		                            </s:if>
		                            <s:if test='"DHHD".equals(campaignInfo.campaignType)'>
		                            	<s:property value="#application.CodeTable.getVal('1229',subCampInfo.SubCampaignType)"/>
		                           </s:if>
	                            </td>
	                         </tr>
	                         <tr>
	                            <th><s:text name="ACT04_topLimit" /></th><%--预约总数量上限 --%>
	                            <td >
		                            <span>
			                            <s:if test='subCampInfo.TopLimit>0'><s:property value="subCampInfo.TopLimit"/></s:if>
			                            <s:else></s:else>
		                            </span>
	                            </td>
	                            <th></th>
	                            <td></td>
	                         </tr>
	                         	<tr>
	                            <th  ><s:text name="ACT04_actDescription" /></th><%--活动描述 --%>
	                            <td  colspan="3" style="word-wrap:break-word;word-break:break-all"><p><s:property value="subCampInfo.Description"/></p></td>
	                         </tr>
                   		</tbody>
               		</table>
               		<table class="detail <s:if test='campaignInfo.campaignOrderFromDate!=null'>hide</s:if>"  style="margin-bottom:0px;">
                     	<tbody>
                          	<tr>
	                            <th><s:text name="ACT04_actSubCampName" /></th><%--活动名称 --%>
	                            <td><span ><s:property value="subCampInfo.SubCampaignName"/></span></td>
	                            <th><s:text name="ACT01_subcampCode" /></th><%--活动代码 --%>
	                            <td><span ><s:property value="subCampInfo.SubCampaignCode"/></span></td>
                          	</tr>
                           	<tr>
	                            <th><s:text name="ACT01_campType" /></th><%--活动类型 --%>
	                            <td>
		                            <s:if test='"CXHD".equals(campaignInfo.campaignType)'>
		                            	<s:property value="#application.CodeTable.getVal('1228',subCampInfo.SubCampaignType)"/>
		                            </s:if>
		                            <s:if test='"LYHD".equals(campaignInfo.campaignType)'>
		                             	<s:property value="#application.CodeTable.getVal('1247',subCampInfo.SubCampaignType)"/>
		                            </s:if>
		                            <s:if test='"DHHD".equals(campaignInfo.campaignType)'>
		                            	<s:property value="#application.CodeTable.getVal('1229',subCampInfo.SubCampaignType)"/>
		                           </s:if>
	                            </td>
	                            <th><s:text name="ACT04_actDescription" /></th><%--活动描述 --%>
	                            <td style="word-wrap:break-word;word-break:break-all"><p><s:property value="subCampInfo.Description"/></p></td>
	                         </tr>
                   		</tbody>
               		</table>
            	</div>
           	</div>
			<%-- ==========================================子活动详细———>活动范围=========================================== --%>	
			<s:set var="place_cnt"  value= "0"/>
			<s:set var="place_size"  value= "20"/>
			<s:if test='subCampInfo.placeType==2||subCampInfo.placeType==4||subCampInfo.placeType==5'>
			<s:set var="place_cnt"  value= "1"/>
			<s:set var="place_size"  value= "12"/>
			</s:if>
            <div class="box4"  style="margin-top:0px;">
               	<div class="box4-header">
               		<strong><s:text name="ACT04_actCampAdress" /></strong>
          			<span class="ui-widget breadcrumb" style="position: relative; margin-left:30px;"></span>
          			<s:if test="subCampInfo.campPlaceList != null">
          				<s:if test='subCampInfo.campPlaceList.size() gt #place_size'>
						<a class="right search" onclick="BINOLCPACT04.showMore(this,'#morePlace');return false;">
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="ACT04_morePlace" /></span>
					 	</a>
					 	</s:if>
					</s:if>
          		</div>
                <div class="box4-content clearfix">
                   	<div style="width:15%;float:left;">
                   		<span style="width:15%;white-space: nowrap;">
                   			<span class="ui-icon icon-arrow-crm"></span>
                   	 		<s:property value="#application.CodeTable.getVal('1156',subCampInfo.placeType)"/>
                   		</span>
					</div>
	                <div style="width:85%;float:left;">
	                	<s:if test="subCampInfo.campPlaceList != null && subCampInfo.campPlaceList.size() > 0">
	                		<s:set var="place_end"  value= "#place_size"/>
	                		<s:if test="subCampInfo.campPlaceList.size() < #place_size">
	                			<s:set var="place_end"  value= "subCampInfo.campPlaceList.size()"/>
	                		</s:if>
		                 	<s:if test='#place_cnt == 1'>
		                 		<ul>
		                 			<s:iterator value="subCampInfo.campPlaceList" begin="0" end="#place_end - 1">
										<li class="left" style="width:33.33%;white-space: nowrap;">
											<span style="background-color: #FFFFFF;margin:5px 0px;width:210px;">
												(<s:property value="placeCode"/>)<s:property value="placeName"/>
											</span>
										</li>
									</s:iterator>
								</ul>
								<s:if test='subCampInfo.campPlaceList.size() gt #place_size'>
									<ul id="morePlace" class="hide">
			                 			<s:iterator value="subCampInfo.campPlaceList" begin="#place_size">
											<li class="left" style="width:33.33%;white-space: nowrap;">
												<span style="background-color: #FFFFFF;margin:5px 0px;width:210px;">
													(<s:property value="placeCode"/>)<s:property value="placeName"/>
												</span>
											</li>
										</s:iterator>
									</ul>
								</s:if>
							</s:if>
							<s:else>
								<ul>
									<s:iterator value="subCampInfo.campPlaceList" begin="0" end="#place_end-1">
										<li class="left" style="width:20%;">
											<span style="background-color: #FFFFFF;margin:5px 0px;width:150px;"><s:property value="placeName"/></span>
										</li>
									</s:iterator>
								</ul>
								<s:if test='subCampInfo.campPlaceList.size() gt #place_size'>
									<ul id="morePlace" class="hide">
										<s:iterator value="subCampInfo.campPlaceList" begin="#place_size">
											<li class="left" style="width:20%;">
												<span style="background-color: #FFFFFF;margin:5px 0px;width:150px;"><s:property value="placeName"/></span>
											</li>
										</s:iterator>
									</ul>
								</s:if>
							</s:else>
						</s:if>
	                 </div>
	          	</div>
	   		</div>
	        <%-- ==========================================子活动详细———>活动对象=========================================== --%>
   			<div class="box4" style="margin-top:0px;">
                <div class="box4-header">
                	<strong><s:text name="ACT04_actCampObj" /></strong>
                	<s:if  test='subCampInfo.campMebMap.campMebType==1||subCampInfo.campMebMap.campMebType==2
	                 	||subCampInfo.campMebMap.campMebType==3'>
	                	<span class="ui-widget" style="position: relative; margin-left:20px;">
		                	<s:text name="ACT04_memCount" />
		                	<span class="green">
		                	<s:text name="format.number"><s:param value="subCampInfo.campMebMap.total"></s:param></s:text>
		                	</span>
		                	<s:if  test='subCampInfo.campMebMap.campMebType==1'><span class="red">[<s:text name="global.page.estimate" />]</span></s:if>
		                </span>
	                 	<input type="hidden" name="searchCode" id="searchCode" value='<s:property value="subCampInfo.campMebMap.searchCode"/>'/>
	                  	<a class="right search" onclick="BINOLCPACT04.memSearch();return false;">
							<span class="ui-icon icon-search"></span>
							<span class="button-text"><s:text name="ACT04_actCampSearch" /></span>
					 	</a>
				 	</s:if>
                </div>
                <div class="box4-content clearfix">
                	<div class="clearfix">
	                    <div class="left" style="width:10%;">
	                    	<span class="ui-icon icon-arrow-crm"></span>
	                    	<s:property value="#application.CodeTable.getVal('1224',subCampInfo.campMebMap.campMebType)"/>
	                    </div> 
	                    <p class="left" style="margin:0; width:90%;">
		                   	<s:action name="BINOLCM33_conditionDisplay" namespace="/common" executeResult="true">
		                   		<s:param name="reqContent" value="subCampInfo.campMebMap.conInfo" />
		                   	</s:action>
	                    </p>
	                    
				 	</div>
                    <div>
						<div class="section hide" id="memberInfo">
						  	<div class="section-content">
						    	<table class="jquery_table" style="width:100%;" id="memberDataTable">
							      <thead>
							        <tr>
							          <th><s:text name="ACT04_actCamMebType" /></th><%--对象类型--%>
							          <th><s:text name="ACT04_actMebCode" /></th><%--卡号/编号--%>
							          <th><s:text name="ACT04_actMebName" /></th><%--姓名--%>
							          <th><s:text name="ACT04_actMebPhhone" /></th><%--手机号--%>
							          <th><s:text name="ACT04_actMebBirthDay" /></th><%--生日--%>
							          <th><s:text name="global.page.joinDate" /></th><%--入会日期--%>
							          <th><s:text name="global.page.changablePoint" /></th><%--可兑换积分--%>
							          <th class="center"><s:text name="ACT04_actMebMessage" /></th><%--接收短信--%>
							        </tr>
							      </thead>
							      <tbody></tbody>
							    </table>
							</div>
						</div>
					</div>
             	</div>           
             </div>
             <%-- ==========================================子活动详细———>购买条件=========================================== --%>
             <s:if test='subCampInfo.PriceControl != null && subCampInfo.PriceControl != 0'>
				<div class="box4" class="UN_SAME" style="margin-top:0px;">
					<div class="box4-header"><strong><s:text name="ACT04_shopingCondition" /></strong></div>
					<div class="box4-content clearfix">
						<div style="" class="left">
					    	<span class="ui-icon icon-arrow-crm"></span>
					    	<s:if test='campaignInfo.needBuyFlag == "1"'>
					    		<s:text name="ACT04_shopingCondition_1" />
					    	</s:if>
					    	<s:else>
					    		<s:text name="ACT04_shopingCondition_2" />
					    		<strong style="color:#ff0000;font-size:15px;">
					    			<span class="red">
					    			<s:if test='subCampInfo.SaleBatchNo == null || subCampInfo.SaleBatchNo == 0'><s:text name="ACT04_batchNo_ALL" /></s:if>
									<s:else><s:text name="format.number"><s:param value="subCampInfo.SaleBatchNo"></s:param></s:text></s:else>
									</span>
								</strong>
								<s:text name="ACT04_shopingCondition_3" />
					    	</s:else>
					    	<strong style="color:#ff0000;font-size:15px;">
								<span class="red"><s:text name="format.price"><s:param value="subCampInfo.PriceControl"></s:param></s:text></span>
							</strong>
							<s:text name="ACT04_yuan" />
						</div>
					</div>
				</div>
			</s:if>
			<%-- ==========================================子活动详细———>购买条件=========================================== --%>
             <s:if test='subCampInfo.DeliveryPoints != 0 || subCampInfo.DeliveryPrice != 0'>
				<div class="box4" class="UN_SAME" style="margin-top:0px;">
					<div class="box4-header"><strong><s:text name="cp.deliveryInfo" /></strong></div>
					<div class="box4-content clearfix">
						<div class="left" style="">
					    	<span class="ui-icon icon-arrow-crm"></span>
							<s:text name="cp.deliveryInfo_1"/>
							<span class="red">
							<s:text name="format.price"><s:param value="subCampInfo.DeliveryPrice"></s:param></s:text>
							</span>
							<s:text name="cp.deliveryInfo_2"/>
							<span class="red">
							<s:text name="format.number"><s:param value="subCampInfo.DeliveryPoints"></s:param></s:text>
							</span>
							<s:text name="cp.deliveryInfo_3"/>
						</div>
					</div>
				</div>
			</s:if>
             <%-- ==========================================子活动详细———>活动奖励=========================================== --%>		
             <div class="box4" style="margin-top:0px;">
                <div class="box4-header"><strong><s:text name="ACT04_actAward"/></strong><%--活动奖励--%>
                <span class="ui-widget" style="position: relative; margin-left:20px;">
                	<s:property value="#application.CodeTable.getVal('1226',subCampInfo.rewardType)"/>
                </span>
                </div>
                <div id="actProList" class="box4-content clearfix" >
                	<s:if test='subCampInfo.prtList != null && subCampInfo.prtList.size() > 0'>
                		<div>
                			<span class="ui-icon icon-arrow-crm"></span><s:property value="#application.CodeTable.getVal('1226',subCampInfo.rewardType)"/>
	                		<span style="margin-left:50px;">
		                		<span>
		                		<s:iterator value="subCampInfo.virtList">
		                			<s:iterator value="list">
			                			<s:text name="ACT04_needPoint"/><strong style="color:#ff0000;font-size:15px;"><s:property value="exPoint"/></strong>
										<s:text name="ACT04_deductions"/>:<strong style="color:#ff0000;font-size:15px;"><s:text name="format.price"><s:param value="price*-1"></s:param></s:text></strong><s:text name="ACT04_yuan"/>	
		                			</s:iterator>									
								</s:iterator>
								</span>
							</span>
						</div>
	                	<div class="sortbox_content clearfix">
	                	<s:iterator value="subCampInfo.prtList" status="st" id ="result">
	                		<s:if test='subCampInfo.prtList != null && subCampInfo.prtList.size() > 0'>
								<s:if test='"".equals(groupType)'> </s:if>
								<s:else>
									<jsp:include page="/WEB-INF/jsp/cp/act/BINOLCPACT04_4.jsp" flush="true" />
								</s:else>
							</s:if>
							<%--组合关系--%>
								<s:if test='#st.last'> </s:if>
								 <s:else>
									<button class="button_<s:property value="logicOpt"/>" type="button">
										<span class="text"><s:property value="logicOpt"/></span>
									</button>
								</s:else>
						</s:iterator>
						</div>
					</s:if>
					<s:else>
	                	<jsp:include page="/WEB-INF/jsp/cp/act/BINOLCPACT04_3.jsp" flush="true" />
                	</s:else>
                </div>
        	</div>
  		</div>
	</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>