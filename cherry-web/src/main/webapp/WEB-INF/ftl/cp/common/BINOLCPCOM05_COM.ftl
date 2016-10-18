
<div class="main container clearfix">
    <div class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
        <a href="#"><@s.text name="cp.memberAct" /></a> > <a href="#"><@s.text name="cp.baseInfo" /></a> </span> </div>
      </div>
            <div class="panel-content">
            <div class="section">
			  <div class="section-content">
	            <@c.form id="toNextForm" action="BINOLCPCOM02_init" method="post" csrftoken="false">
	        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/> 
	        	<input type="hidden" name="actionKbn" id="actionKbn" value="fromTop"/>
	        	<input type="hidden" name="actionId" id="actionId"/>
	        	<input type="hidden" name="saveStep" id="saveStep" value="${(saveStep)!?c}"/>
	        	<#-- 会员活动保存信息 -->   
	        	<input type="hidden" name="campSaveInfo" id="campSaveInfo" value="${(campSaveInfo)!?html}"/>
	        	<input type="hidden" name="optKbn" id="optKbn" value="${(optKbn)!}"/> 
	        	<#-- 保存其他有用信息 -->
	        	<input type="hidden" name="extraInfo" id="extraInfo" value="${(extraInfo)!?html}"/>
        		<input type="hidden" name="copyFlag" id="copyFlag" value="${(copyFlag)!}"/>
        		<#-- 规则类型 -->
        		<input type="hidden" name="campInfo.pointRuleType" id="pointRuleType" value="${(campInfo.pointRuleType)!?html}"/>
        		<#-- 积分类型 -->
		        <input type="hidden" name="campInfo.templateType" id="templateType" value="${(campInfo.templateType)!?html}"/>
		        <#-- 类型区分 -->
		        <input type="hidden" name="campInfo.campaignTypeFlag" id="campaignTypeFlag" value="${(campInfo.campaignTypeFlag)!}"/>
		        <#-- 活动状态 -->
		        <input type="hidden" name="campInfo.state" value="${(campInfo.state)!}"/>
		        <input type="hidden" name="campInfo.campaignId" value="${(campInfo.campaignId)!}"/>
		        <input type="hidden" name="campInfo.groupFlag" value="${(campInfo.groupFlag)!}"/>
		        <input type="hidden" name="campInfo.campaignCode" value="${(campInfo.campaignCode)!}"/>
		<#--================================================================ 基础信息  =================================================================-->
		        <div class="box2 box2-active">
		        <div class="box2-header clearfix"><strong class="active left"><span class="ui-icon icon-ttl-section-info"></span><@s.text name="cp.campBaseInfo"/></strong></div>
				<div class="box2-content">
			    <table cellspacing="0" cellpadding="0" class="detail"  style="margin-bottom:0px">
			  	  <tbody>
				  	  <tr>
				  	  	  <th><@s.text name="cp.mainActName" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
				  	  	  <td><span><input class="text textLong" type="text" name="campInfo.campaignName" value="${(campInfo.campaignName)!?html}" /></span></td>
				  	  	  <th><@s.text name="cp.brand" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
				  	  	  <td> 
				  	  	  	<#if optKbn == 0>
		            			<#if (brandInfoList?? && brandInfoList?size > 0)>
		            				<select name="brandInfoId" style="width:120px;">
		            				<#list brandInfoList as brandInfo>
		            				<option <#if (campInfo.brandInfoId)! == brandInfo.brandInfoId.toString() >selected="selected"</#if>  value="${brandInfo.brandInfoId.toString()}" >
		            				${(brandInfo.brandName)!?html}
		            				</option>
		            				</#list>
		            				</select>
		             			</#if>
		             			<#elseif optKbn == 2>
		             				<#if (brandInfoList?? && brandInfoList?size > 0)>
			            				<select disabled="true" style="width:120px;">
			            				<#list brandInfoList as brandInfo>
						            	<option <#if (brandInfoId)! == brandInfo.brandInfoId.toString() >selected="selected"</#if>  value="${brandInfo.brandInfoId.toString()}" >
						            	${(brandInfo.brandName)!?html}
						            	</option>
						            	</#list>
						            	</select>
			            				<input type="hidden" name="brandInfoId" value="${(brandInfoId)!}"/>
			            			</#if>
	             			</#if>
	             		  </td>
             		  </tr>
             		  <tr>
             		  <#-- 主题活动类型 -->
             		  	<th><@s.text name="cp.mainActType" /><span class="highlight"><@s.text name="global.page.required"/></span></th>
             		  	<td>
             		  		<#if optKbn == 0>
             		  		<select id="campaignType" name="campInfo.campaignType" style="width:120px;" onchange="BINOLCPCOM05.changeSubType(this);">
             		  			<#list application.CodeTable.getCodes("1174") as typeInfo>
		             		  			<option <#if (campInfo.campaignType)! == typeInfo.CodeKey >selected="selected"</#if>  value="${typeInfo.CodeKey}" >
							            	${(typeInfo.Value)!}
							            </option>
             		  			</#list>
             		  		</select>
				            <#elseif optKbn == 2>
				            	<select disabled="true" style="width:120px;">
             		  			<#list application.CodeTable.getCodes("1174") as typeInfo>
             		  			<option <#if (campInfo.campaignType)! == typeInfo.CodeKey >selected="selected"</#if>  value="${typeInfo.CodeKey}" >
					            	${(typeInfo.Value)!}
					            	</option>
             		  			</#list>
             		  		</select>
				            	<input type="hidden" id="campaignType" name="campInfo.campaignType" value="${(campaignType)!?html}"/>
				            </#if>
				            <#if (clubList?? && clubList?size > 0) >
				            <span id="clubSpan" style="float:none" <#if (campInfo.campaignType)! != "" &&  (campInfo.campaignType)! != "DHHD"> class="hide" </#if>>
				              <label><@s.text name="cp.memclubst" /></label>
				              <select id="memberClubId" name="memberClubId">
				              		<#list clubList as clubInfo>
					            	<option <#if (memberClubId)! == clubInfo.memberClubId.toString() >selected="selected"</#if>  value="${(clubInfo.memberClubId)!}" >
					            	${(clubInfo.clubName)!}
					            	</option>
					            	</#list>
					            	</select>
				            </span>
				            </#if>
				        </td>
				         <#-- 活动类型 -->
             		  	<th><@s.text name="cp.actType" /><span class="highlight">*</span></th>
						  <td>
						  <span>
						  <select style="width:120px;" id="subCampType"
						  <#if optKbn == 2>disabled="disabled" <#else>name="campInfo.subCampType"</#if>>
						  </select>
						  <#if optKbn == 2>
						  	<input type="hidden" name="campInfo.subCampType" value="${campInfo.subCampType!}"/>
						  </#if>
						  </span>
						</td>
				     </tr>
				     <tr id="exPointInfo"></tr>
					<tr>
						<#-- 领取柜台 -->
						<th><@s.text name="cp.gotCounter"/></th>
						<td>
							<span>
						  	 <select name="campInfo.gotCounter" style="width:120px;">
								<#list application.CodeTable.getCodes("1254") as code>
									<option <#if code.CodeKey==campInfo.gotCounter!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
							  	</#list>
						  	</select>
							</span>
						 </td>
						
						<#-- 活动验证 -->
						<th><@s.text name="cp.actValidate"/></th>
						<td>
							<div>
						  	<select name="campInfo.subCampaignValid" style="width:120px;" id="subCampaignValid" onchange="BINOLCPCOM05.changeSubValid(this);">
								<#list application.CodeTable.getCodes("1230") as code>
									<option <#if code.CodeKey==campInfo.subCampaignValid!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
							  	</#list>
						  	</select>
						  	</div>
						 </td>
						 
					</tr>
					<tr>
				     	<#-- 终端可否更改礼品构成 -->
						<th><@s.text name="cp.goodsChangeable"/><span class="highlight">*</span></th>
						<td>
						<#if (campInfo.state)! != "1" >
							<@s.radio name="campInfo.goodsChangeable" list="#application.CodeTable.getCodes('1234')"
	                    		listKey="CodeKey" listValue="Value" value='${(campInfo.goodsChangeable)!"0"}'/>
						<#else>
					  			<@s.radio name="campInfo.goodsChangeable" disabled="true" list="#application.CodeTable.getCodes('1234')"
	                    		listKey="CodeKey" listValue="Value" value='${(campInfo.goodsChangeable)!"0"}'/>
					  			<input type="hidden" name="campInfo.goodsChangeable" value="${campInfo.goodsChangeable!}"/>
					  	</#if>
						</td>
						<#-- 微信访问 -->
						<th><@s.text name="cp.wechatFlag"/></th>
						<td>
							<@s.radio name="campInfo.wechatFlag" list="#application.CodeTable.getCodes('1233')"
	                    		listKey="CodeKey" listValue="Value" value='${(campInfo.wechatFlag)!"1"}'/>
						</td>
						
				     </tr>
					<tr>
						<#-- 是否需要购买 -->
						<th><@s.text name="cp.needBuyFlagTxt"/></th>
						<td>
							<@s.radio name="campInfo.needBuyFlag" list="#application.CodeTable.getCodes('1235')"
	                    		listKey="CodeKey" listValue="Value" value='${(campInfo.needBuyFlag)!"0"}' onclick="BINOLCPCOM05.setSendFlag();"/>
						</td>
						<th><@s.text name="cp.creater" /></th>
             		  	<td>
             		  		<span><input type="text" class="text disabled" name="campInfo.campaignSetBy"  value="${(campInfo.campaignSetByName)!?html}" disabled="disabled" /></span>
				            <input type="hidden" name="campInfo.campaignSetBy" value="${(campInfo.campaignSetBy)!?c}"/>
				            <input type="hidden" name="campInfo.campaignSetByName" value="${(campInfo.campaignSetByName)!?html}"/>
				        </td>
						
					</tr>

					<tr>
						<#-- 是否下发活动 -->
						<th><@s.text name="cp.needSendFlag"/></th>
						<td>
							<@s.radio name="campInfo.sendFlag" list="#application.CodeTable.getCodes('1233')"
	                    		listKey="CodeKey" listValue="Value" value='${(campInfo.sendFlag)!"1"}'/>
						</td>
						<th rowspan="2"><@s.text name="cp.actDes" /></th>
             		  	<td rowspan="2">
             		  		<span style="width:85%;">
             		  			<textarea cols="" rows="" name="campInfo.descriptionDtl" style="width: 93%;height:40px;">${(campInfo.descriptionDtl)!?html}</textarea>
             		  		</span>
             		  	</td>
					</tr>
					
					<tr>
						<#-- 是否采集活动获知方式 -->
						<th><@s.text name="cp.actWayKnow"/></th>
						<td>
							<@s.radio name="campInfo.isCollectInfo" list="#application.CodeTable.getCodes('1234')"
	                    		listKey="CodeKey" listValue="Value" value='${(campInfo.isCollectInfo)!"0"}'/>
						</td>
					</tr>
			    </tbody>
			 </table>
		</div>
	</div> 
	<#--================================================================ 活动时间   =================================================================-->
		<div class="box2 box2-active">
			<div class="box2-header clearfix"><strong class="active left"><span class="ui-icon icon-clock"></span><@s.text name="cp.actTime" /></strong></div>
			<div class="box2-content clearfix">
				<table cellspacing="0" cellpadding="0" style="float: left; width: 100%; margin-bottom: 0px;" class="detail">
					<thead>
						<th style="width:35%;"><span><@s.text name="cp.actDate" /></span><span class="highlight"><@s.text name="global.page.required"/></span></th>
						<th style="width:65%;">
						<span class="left"><@s.text name="cp.step102" /></span>
						<span class="right">
						<#if (campInfo.state)! != '1'>
						 <a style="margin:2px 5px 0 5px;" class="add right" onclick="BINOLCPCOM05.openCampTimeDialog(this)">
							 <span style="left: 0; position: absolute;" class="ui-icon icon-add"></span>
							 <span style="position: static;" class="button-text"><@s.text name="cp.addCampTime" /></span> 
						 </a>
						 </#if>
						 </span>
						 </th>
					</thead>
					<tbody>
						<tr>
							<td>
								<div style="margin:0 auto;width:350px;">
								<span>
				             		<span><input class="date" id="campFromDate" name="campInfo.campaignFromDate" value="${campInfo.campaignFromDate!?html}"/></span>
			             		 	<span style="margin: 0 5px;"><@s.text name="cp.to" /></span>
			             		 	<span><input class="date" id="campToDate" name="campInfo.campaignToDate" value="${campInfo.campaignToDate!?html}"/></span>
		             		 	</span>		
							    </div>
							</td>
							<td>
								<ul class="sortable">
									<#list application.CodeTable.getCodes("1195") as time>
						    		<@getTimeItem time=time/>
						    		</#list>
			    				</ul>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
      </@c.form>      
  </div>
</div>
  	<hr class="space" />
    <div class="center clearfix">
	      <button class="close" onclick="window.close();return false;">
	    	<span class="ui-icon icon-close"></span>
	        <span class="button-text"><@s.text name="cp.cancel" /></span>
	     </button>
      	<button class="back" onclick="BINOLCPCOM05.doNext('${(nextAction)!}');return false;">
      	<span class="ui-icon icon-mover"></span><span class="button-text"><@s.text name="cp.next" /></span></button>
    </div>
   </div>
  </div>
</div>
<div class="hide">
  	<select id="subCampType_DHHD">
		<#list application.CodeTable.getCodes("1229") as code>
			<option <#if code.CodeKey==campInfo.subCampType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
	  	</#list>
  	</select>
  	<select id="subCampType_LYHD">
		<#list application.CodeTable.getCodes("1247") as code>
			<option <#if code.CodeKey==campInfo.subCampType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
	  	</#list>
  	</select>
  	<select id="subCampType_CXHD">
		<#list application.CodeTable.getCodes("1228") as code>
			<option <#if code.CodeKey==campInfo.subCampType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
	  	</#list>
  	</select>
  	
  	<select id="subCampaignValid_DHHD">
		<#list application.CodeTable.getCodes("1230") as code>
			<option <#if code.CodeKey==campInfo.subCampaignValid!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
	  	</#list>
  	</select>
  	<select id="subCampaignValid_LYHD">
		<#list application.CodeTable.getCodes("1230") as code>
			<option <#if code.CodeKey==campInfo.subCampaignValid!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
	  	</#list>
  	</select>
  	<select id="subCampaignValid_CXHD">
		<#list application.CodeTable.getCodes("1230") as code>
		 	<#if code.CodeKey=='0' || code.CodeKey=='2' || code.CodeKey=='5'>
				<option <#if code.CodeKey==campInfo.subCampaignValid!'2'>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
			</#if> 
	  	</#list>
  	</select>
  	<table>
  		<tr id="exPointInfo_DHHD">
  			<th><@s.text name="cp.exPointDeadDate" /></th>
 		 	<td>
 		 		<#if (campInfo.state)! == "1">
 		 			${campInfo.exPointDeadDate!}
 		 			<input type="hidden" name="campInfo.exPointDeadDate" value="${campInfo.exPointDeadDate!}"/>
 		 		<#else>
 		 			<span><input class="date" id="exPointDeadDate" name="campInfo.exPointDeadDate" value="${campInfo.exPointDeadDate!}" style="width:90px;"/></span>
 		 		</#if>
 		 	</td>
 		 	<#-- 
 		 	<th><@s.text name="cp.exPointDeductFlag" /></th>
 		 	<td>
 		 		<select id="exPointDeductFlag" <#if (campInfo.state)! == "1">disabled</#if> name="campInfo.exPointDeductFlag" style="width:120px;" onchange="BINOLCPCOM05.changeDeduct(this);">
 		 			<option value=""></option>
 		 			<#list application.CodeTable.getCodes("1255") as code>
						<option <#if code.CodeKey==campInfo.exPointDeductFlag!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
				  	</#list>
 		 		</select>
 		 		<#if (campInfo.state)! == "1"><input type="hidden" name="campInfo.exPointDeductFlag" value="${campInfo.exPointDeductFlag!}"/></#if>
 		 	</td>
 		 	-->
 		 	<th><@s.text name="cp.isShared" /></th>
 		 	<td>
 		 		<input type="hidden" name="campInfo.exPointDeductFlag" value="1"/>
 		 		<#assign isShared = campInfo.isShared!''/>
 		 		<#if isShared =='' ><#assign isShared = '0'/></#if>
						<@s.radio name="campInfo.isShared" list="#application.CodeTable.getCodes('1234')"
                    		listKey="CodeKey" listValue="Value" value='${isShared}'/>
 		 	</td>
  		</tr>
  	</table>
</div>
<div id="PopDialogMain">
	<div id="PopTimeDialog" class="hide">
	   	<table id="PopTime_dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table2" width="100%">
	   		<thead>
		        <tr>
		           <th><@s.text name="cp.choice" /></th>
		           <th><@s.text name="cp.step102" /></th>
		        </tr>
		    </thead>
	    	<tbody>
		    <#list application.CodeTable.getCodes("1195") as code>
		    	<tr class="center">
					<td><input type="checkbox" class="checkbox" value='${code.CodeKey!?html}'/></td>
					<td><span>${code.Value!?html}</span></td>
				</tr>
		    </#list>  
			</tbody>
	   	</table>
	   	<hr>
	  	<span id="global_page_ok" style="display:none"><@s.text name="global.page.ok" /></span>
		<span id="PopTimeTitle" style="display:none"><@s.text name="cp.step102" /></span>
	</div>
</div>