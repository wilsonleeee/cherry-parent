<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#-- 购买产品  -->
	<#if camTemp.tempCode == "BASE000001">
	<div class="box2" id="BASE000001_${index}">
		<div class="box2-header clearfix">
		    <strong class="left"><span class="ui-icon icon-buy"></span><@s.text name="cp.buyPro" /></strong>
	    </div>
	    <div class="box2-content selPro">
	        <p>
	          <input id="radio_any_${index}" type="radio" class="radio" onClick="TEMP001.boldLabel(this)" name="prtRadio_${index}" 
	          	value="0" <#if (camTemp.prtRadio)! == "0" || (camTemp.prtRadio)! == "">checked="checked"</#if>/>
	          <label for="radio_any_${index}"><@s.text name="cp.buyEvePro"/></label>
	        </p>
	        <p class="clearfix proClass">
	        <span class="left">
		        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
		        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		        <input type="hidden" name="selPrtIndex" value="${(camTemp.tempCode)!?html}_${index}"/>
				<input id="radio_appoint_${index}" type="radio" class="radio" name="prtRadio_${index}" value="1" onClick="TEMP001.boldLabel(this)" <#if (camTemp.prtRadio)! == "1">checked="checked"</#if>/>
				<label id="choicePro" class="errorMsg" for="radio_appoint_${index}"><@s.text name="cp.buyOnePro"/></label>
				<a class="add right" onClick="TEMP001.showTable(this);">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.choicePro" /></span></a>
	         </span>
	         </p>
	         <p class="clearfix" style="margin-left: 120px;">
	         <span class="left">
	          <span id="prtSel_${(camTemp.tempCode)!?html}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
	          <#if (camTemp.productList?? && camTemp.productList?size > 0) >
	          	<#list camTemp.productList as productList>
	            	<#if (productList.nameTotal)! != "">
	          		<span class="span_BASE000001">
          				<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
          				<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
	          			<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span> 
	          			<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
	          			<span class="ui-icon ui-icon-close"></span></span>
	          		</span>
	          		</#if>
   				</#list>
	          </#if>
	         </span>
	         </span>
	         </p>
	      </div>
  	</div>
  	
  	<#-- 购买产品(内容确认)  -->
  	<#elseif camTemp.tempCode == "BASE000004">
	      <tr>
            <th width="100px"><@s.text name="cp.buyPro"/></th>
            <td style="white-space: normal;">
            <#if (camTemp.prtRadio)! == "1" >
	            <#if (camTemp.productList?? && camTemp.productList?size > 0) >
	            <#list camTemp.productList as productList>
	            	<#if (productList.nameTotal)! != "">
		          		<span class="span_BASE000001">
		          			<span class="bg_title">${(productList.nameTotal)!?html}</span> 
		          		</span>
		          	</#if>
	   			 </#list>
		         </#if>
		     <#elseif (camTemp.prtRadio)! == "0">
		         	<@s.text name="cp.buyEvePro"/>
		     </#if>
	         </td>
	      
	      
  	<#-- 消费金额  -->
	<#elseif camTemp.tempCode == "BASE000002">
          <div class="box2" id="BASE000002_${index}">
			<div class="box2-header clearfix">
           			<strong class="left"><span class="ui-icon icon-money"></span><@s.text name="cp.totalUseMoney" /></strong>
           	</div>
            <div class="box2-content clearfix">
            	<input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
            	<input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
                <p><@s.text name="cp.passOrMore"/> <span>
                  <input type="text" class="number" name="minAmount" value="${(camTemp.minAmount)!?html}"/>
                  	<@s.text name="cp.yuan"/> </span><@s.text name="cp.less"/> <span>
                  <input type="text" class="number" name="maxAmount" value="${(camTemp.maxAmount)!?html}"/>
                  	<@s.text name="cp.yuan"/> </span></p>
              </div>
          </div>
    <#-- 消费金额(内容确认)  -->
	<#elseif camTemp.tempCode == "BASE000006">
            <th><@s.text name="cp.totalUseMoney" /></th>
            <td>
            	<#if "" != (camTemp.minAmount)!>
	            	<span><@s.text name="cp.passOrMore"/></span>
	            	<span class="red">${(camTemp.minAmount)!?html}</span>
	            	<span> <@s.text name="cp.yuan"/> </span>
	            </#if>
	             <#if "" != (camTemp.maxAmount)!>
	            	<span><@s.text name="cp.less"/></span>
	            	<span class="red">${(camTemp.maxAmount)!?html}</span> 
	            	<span><@s.text name="cp.yuan"/> </span>
	            </#if>
            </td>
          </tr>
          
	
    <#-- 累积金额  -->
    <#elseif camTemp.tempCode == "BASE000003">
    	<div class="box2" id="BASE000003_${index}">
            <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
            <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
            <div class="box2-header clearfix" >
            	<strong class="left"><span class="ui-icon icon-money"></span><@s.text name="cp.totalMoney" /></strong>
            </div>
            <div class="box2-content">
                <p>
                	<input type="radio" class="radio" onClick="TEMP001.setChecked(this,'${index}');" name="plusChoice_${index}" value="0" id="validDateFlag_${index}" <#if (camTemp.plusChoice)! == "" || (camTemp.plusChoice)! == "0">checked="checked"</#if> />
                	<label for="validDateFlag_${index}"><@s.text name="cp.validDate" /></label>
                	<#if (camTemp.levelValidInfo??) >
		            	(
		            	<span class="bg_title"><@s.text name="cp.startTimeTxt" /></span>
		            	<#if (camTemp.levelValidInfo.startTimeKbn)! == "" || (camTemp.levelValidInfo.startTimeKbn)! == "0">
		            	<@s.text name="cp.levelTimeTxt" />
		            	<#else>
		            	<@s.text name="cp.offiLevelTimeTxt" />
		            	</#if>
		            	<span class="bg_title"><@s.text name="cp.endTimeTxt" /></span>
		            	<#if (camTemp.levelValidInfo.normalYear)! == "" || (camTemp.levelValidInfo.normalYear)! == "0">
		            	${(camTemp.levelValidInfo.memberDate0)!}<@s.text name="cp.inNormalYear" />
		            	<#elseif (camTemp.levelValidInfo.normalYear)! == "1">
		            	${(camTemp.levelValidInfo.memberDate1)!}<@s.text name="cp.inYear" />
		            	<#elseif (camTemp.levelValidInfo.normalYear)! == "2">
		            	${(camTemp.levelValidInfo.memberDate2)!}<@s.text name="cp.inMonth" />
		            	<#else>
		            		<@s.text name="cp.forever" />
		            	</#if>
						)
		            </#if>
                </p>
              <p>
                	<input type="radio" class="radio" onClick="TEMP001.setChecked(this,'${index}');" onClick="TEMP001.setChecked(this,'${index}');"  name="plusChoice_${index}" value="2" id="firstSelf_${index}" <#if (camTemp.plusChoice)! == "2">checked="checked"</#if> />
                	<label for="firstSelf_${index}"><@s.text name="cp.firstSelfTxt" /></label>
                </p>
                <p>
                	<input type="radio" class="radio" onClick="TEMP001.setChecked(this,'${index}');" name="plusChoice_${index}" value="1" id="selfFlag_${index}" <#if (camTemp.plusChoice)! == "1">checked="checked"</#if> />
                	<label for="selfFlag_${index}"><@s.text name="cp.selfSetRule" /></label>
                </p>
                 
                <p style="margin-left:20px;">
                	<span>累计开始时间：
                	<select name="totalStime" style="width:120px;" onchange="TEMP001.showSelSpan(this,'totalStime_')">
            			<option <#if (camTemp.totalStime)! != "1"  >selected="selected"</#if>  value="0" >有效期开始日</option>
            			<option <#if (camTemp.totalStime)! == "1" >selected="selected"</#if>  value="1" >每年 </option>
            		</select>
            		<span id="totalStime_1" <#if (camTemp.totalStime)! != "1" >class="hide"</#if>> <input type="text" name="tsMonth" class="number" value="${(camTemp.tsMonth)!?html}"/> 月  <input type="text" name="tsDay" class="number" value="${(camTemp.tsDay)!?html}"/> 日 </span>
            		</span>
            		<br/>
                	<input type="radio" class="radio" name="plusTime_${index}" value="0" id="monthFlag_${index}" <#if (camTemp.plusTime)! == "0">checked="checked"</#if>/>
                  <label for="monthFlag_${index}"><@s.text name="cp.continue"/></label> <span>
                  <input type="text" name="monthNum" class="number" value="${(camTemp.monthNum)!?html}"/>
                  </span> <@s.text name="cp.inMonth"/>
                </p>
                <p style="margin-left:20px;">
                  <input type="radio" class="radio" name="plusTime_${index}" value="1" id="yearFlag_${index}" <#if (camTemp.plusTime)! == "1">checked="checked"</#if>/>
                  <label for="yearFlag_${index}"><@s.text name="cp.continue"/></label> <span>
                  <input type="text" name="yearNum" class="number" value="${(camTemp.yearNum)!?html}"/>
                  </span> <@s.text name="cp.inYear"/>
                  <input type="checkbox" name="yltKbn" value="1" <#if "1" == (camTemp.yltKbn)!  >checked="checked"</#if> onclick="TEMP001.showYearLimit(this, '${index}')"/>
					<@s.text name="cp.yearLit"/>
				  <span <#if (camTemp.yltKbn)! != "1" >class="hide"</#if> id="ylzSpan_${index}"> <@s.text name="cp.yearDi"/>  <input type="text" class="number" name="ylz" value="${(camTemp.ylz)!?html}"/> <@s.text name="text1"/>
				  	<select name="yll">
            			<option <#if (camTemp.yll)! == "0" >selected="selected"</#if>  value="0" ><@s.text name="cp.yearYinei"/></option>
            			<option <#if (camTemp.yll)! == "1" >selected="selected"</#if>  value="1" ><@s.text name="cp.yearYiwai"/> </option>
            		</select>
				   </span>
                </p>
                <p style="margin-left:20px;">
                  <input type="radio" class="radio" name="plusTime_${index}" value="2" id="normalYearFlag_${index}" <#if (camTemp.plusTime)! == "2">checked="checked"</#if>/>
                  <label for="normalYearFlag_${index}"><@s.text name="cp.continue"/></label> <span>
                  <input type="text" name="normalYearNum" class="number" value="${(camTemp.normalYearNum)!?html}"/>
                  </span> <@s.text name="cp.inNormalYear"/><@s.text name="cp.inNormalyearTip"/>
                </p>
                  <p>累计金额<@s.text name="cp.passOrMore"/> <span>
                  <input type="text" class="number" name="plusminAmount" value="${(camTemp.plusminAmount)!?html}"/>
                  	<@s.text name="cp.yuan"/> </span><@s.text name="cp.less"/> <span>
                  <input type="text" class="number" name="plusmaxAmount" value="${(camTemp.plusmaxAmount)!?html}"/>
                  	<@s.text name="cp.yuan"/> </span>
                  	<br/>
                  	购买次数<@s.text name="cp.passOrMore"/> <span>
                  <input type="text" class="number" name="bstime" value="${(camTemp.bstime)!?html}"/>
                  	次 </span><@s.text name="cp.less"/> <span>
                  <input type="text" class="number" name="betime" value="${(camTemp.betime)!?html}"/>
                  	次 </span>&nbsp;&nbsp;与累计金额关系
                  	<select name="timeCond">
            			<option <#if (camTemp.yll)! != "1" >selected="selected"</#if>  value="0" >且</option>
            			<option <#if (camTemp.yll)! == "1" >selected="selected"</#if>  value="1" >或 </option>
            		</select>
                  	</p>
                  	<p><@s.text name="cp.firstBillInclude" />
                  	<input type="radio" name="plusfirstBillSel_${index}" id="yes_${index}" class="radio" value="0" <#if (camTemp.plusfirstBillSel)! == "" || (camTemp.plusfirstBillSel)! == "0">checked="checked"</#if>/>
					<label for="yes_${index}"><@s.text name="cp.yes" /></label>
					<input type="radio" name="plusfirstBillSel_${index}" id="no_${index}" value="1" class="radio" <#if (camTemp.plusfirstBillSel)! == "1">checked="checked"</#if>/>
					<label for="no_${index}"><@s.text name="cp.no" /></label>
					</p>
					<p><input type="checkbox" name="lastKbn" value="1" <#if "1" == (camTemp.lastKbn)!>checked="checked"</#if> />
						<@s.text name="cp.lastTicketDate" /></p>
					<p><input type="checkbox" name="fromKbn" value="1" <#if "1" == (camTemp.fromKbn)!>checked="checked"</#if> />
						<@s.text name="cp.fromKbnTxt"/>
					</p>
              </div>
          </div>
          
          
     <#-- 累积金额 (内容确认)  -->
	<#elseif camTemp.tempCode == "BASE000005">
	<tr>
	    <th width="100px"><@s.text name="cp.totalMoney" /></th>
	    <td>
	    	<#if "" != (camTemp.totalStime)!>
	    	<label class="gray">
	    		累计开始时间：
	    		<#if "1" == (camTemp.totalStime)!>
        			每年  ${(camTemp.tsMonth)!?html} 月 ${(camTemp.tsDay)!?html} 日
        		<#else>
        			有效期开始日
        		</#if>
        	</label>
        	<br/>
        	</#if>
	    	<#if "0" == (camTemp.plusChoice)!>
        		<span><@s.text name="cp.validDate"/></span>
        	<#elseif "2" == (camTemp.plusChoice)!>
        		<span><@s.text name="cp.firstSelfTxt" /></label></span>
        	<#elseif "1" == (camTemp.plusChoice)!>
	        	<#if "0" == (camTemp.plusTime)!>
	        		<span><@s.text name="cp.continue"/></span>
	        		<span class="red">${(camTemp.monthNum)!?html}</span>
	        		<span><@s.text name="cp.inMonth"/></span>
	        	<#elseif "1" == (camTemp.plusTime)!>
	        		<span><@s.text name="cp.continue"/></span>
	        		<span class="red">${(camTemp.yearNum)!?html}</span>
	        		<span><@s.text name="cp.inYear"/></span>
	        		 <#if "1" == (camTemp.yltKbn)!>
	        			<label class="gray">&nbsp( <@s.text name="cp.yearDi"/>  ${(camTemp.ylz)!?html} <@s.text name="text1"/>
	        			 <#if "0" == (camTemp.yll)!>
	        			 <@s.text name="cp.yearYinei"/>
	        			 <#else>
	        			 <@s.text name="cp.yearYiwai"/>
	        			 </#if>
	        			)</label>
	        		 </#if>
	        	<#elseif "2" == (camTemp.plusTime)!>
	        		<span><@s.text name="cp.continue"/></span>
	        		<span class="red">${(camTemp.normalYearNum)!?html}</span>
	        		<span><@s.text name="cp.inNormalYear"/></span>
	        	</#if>
        	</#if>
        	<#if "0" == (camTemp.plusfirstBillSel)!>
        		<span><@s.text name="cp.includeFirstBill"/></span>
        	<#else>
        		<span><@s.text name="cp.noIncludeFirstBill"/></span>
        	</#if>
        	<#if "" != (camTemp.plusminAmount)!>
            	<span><@s.text name="cp.totalMoney" /><@s.text name="cp.passOrMore"/></span>
            	<span class="red">${(camTemp.plusminAmount)!?html}</span>
            	<span> <@s.text name="cp.yuan"/> </span>
            </#if>
             <#if "" != (camTemp.plusmaxAmount)!>
            	<span><@s.text name="cp.less"/></span>
            	<span class="red">${(camTemp.plusmaxAmount)!?html}</span> 
            	<span><@s.text name="cp.yuan"/> </span>
            </#if>
            <#if "1" == (camTemp.lastKbn)!>
            	<label class="gray">&nbsp( <@s.text name="cp.lastTicketDate" /> )</label>
            </#if>
            <#if "" != (camTemp.bstime)! || "" != (camTemp.betime)!>
            	<br/>
            	<label>
            		购买次数
            		<#if "" != (camTemp.bstime)!>
            			<@s.text name="cp.passOrMore"/>${(camTemp.bstime)!?html} 次
            		</#if>
            		<#if "" != (camTemp.betime)!>
            			<@s.text name="cp.less"/>${(camTemp.betime)!?html} 次
            		</#if>
            		&nbsp&nbsp与累计金额关系：
            		 <#if "1" == (camTemp.timeCond)!>
            		 	或
            		 	<#else>
            		 	且
            		 </#if>
            	</label>
            	</#if>
            <#if "1" == (camTemp.fromKbn)!>
            	<br/>
            	<label class="gray">
            	<@s.text name="cp.fromKbnTxt"/>
            	</label>
            </#if>
        </td>
     
	
     <#-- 单次购买  -->  
	<#elseif camTemp.tempCode == "BUS000001">
	<div class="group" id="${camTemp.tempCode}_${index}">
		<input type="hidden" id="tempCode_${camTemp.tempCode}_${index}" name="tempCode" value="${camTemp.tempCode}"/>
		<input type="hidden" id="groupCode_${camTemp.tempCode}_${index}" name="groupCode" value="${camTemp.groupCode}"/>
		<input type="hidden" name="RELAT_OPER_KEY" value="0"/>
      <div class="group-header clearfix"> <strong class="left"><@s.text name="cp.onceBuy" />
        <input type="checkbox" id="check_${camTemp.tempCode}_${index}" class="checkbox" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> name="isChecked"
        	onclick="CAMPAIGN_TEMPLATE.showDiv(this, '#condition_${camTemp.tempCode}_${index}');" value="1"/>
        <label><@s.text name="cp.setRule"/></label><label style="color:red; "><@s.text name="cp.fullCondition"/></label>
        </strong> </div>
      <div id="condition_${camTemp.tempCode}_${index}" class="group-content clearfix" <#if (camTemp.isChecked)! != "1">style="display:none;"</#if>>
        <#if camTemp.combTemps?? >
			<#list camTemp.combTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		<#else>
			<#list camTemp.addCombTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		</#if>
      </div>
    </div> 
    
    <#-- 单次购买(内容确认)  -->  
	<#elseif camTemp.tempCode == "BUS000004">
	<#if "0" != (camTemp.showFlg)!>
		<div class="box2">
				<div class="box2-header clearfix">
				    <strong class="left"><@s.text name="cp.onceBuy" /></strong>
			    </div>
		        <div class="box2-content">
	                  <table class="detail">
	                      <#list camTemp.combTemps as combTemp>
					          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
					        </#list>
	                  </table>
	            </div>
			</div>
     </#if>
     
    <#-- 累积购买  -->  
	<#elseif camTemp.tempCode == "BUS000002">
	<div class="group" id="${camTemp.tempCode}_${index}">
		<input type="hidden" id="tempCode_${camTemp.tempCode}_${index}" name="tempCode" value="${camTemp.tempCode}"/>
		<input type="hidden" id="groupCode_${camTemp.tempCode}_${index}" name="groupCode" value="${camTemp.groupCode}"/>
		<input type="hidden" name="RELAT_OPER_KEY" value="0"/>
      <div class="group-header clearfix"> <strong class="left"><@s.text name="cp.plusBuy" />
        <input type="checkbox" id="check_${camTemp.tempCode}_${index}" class="checkbox" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> name="isChecked"
        	onclick="CAMPAIGN_TEMPLATE.showDiv(this, '#condition_${camTemp.tempCode}_${index}');" value="1"/>
        <label><@s.text name="cp.setRule"/></label><label style="color:red; "><@s.text name="cp.fullCondition"/></label>
        </strong> </div>
      <div id="condition_${camTemp.tempCode}_${index}" class="group-content clearfix" <#if (camTemp.isChecked)! != "1">style="display:none;"</#if>>
        <#if camTemp.combTemps?? >
			<#list camTemp.combTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		<#else>
			<#list camTemp.addCombTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		</#if>
      </div>
    </div> 
    
    <#-- 累积购买(内容确认)  -->  
	<#elseif camTemp.tempCode == "BUS000005">
	<#if "0" != (camTemp.showFlg)!>
		<div class="box2">
				<div class="box2-header clearfix">
				    <strong class="left"><@s.text name="cp.plusBuy" /></strong>
			    </div>
		        <div class="box2-content">
	                  <table class="detail">
	                      <#list camTemp.combTemps as combTemp>
					          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
					        </#list>
	                  </table>
	            </div>
			</div>
     </#if>
     
   
		<#-- 等级和有效期  -->  
		<#elseif camTemp.tempCode == "BASE000010">
		<div id="${camTemp.tempCode}_${index}">
		<div class="section">
			<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
			<input type="hidden" name="levelName" value="${(camTemp.levelName)!?html}" id="levelName_${camTemp.tempCode}_${index}"/>
			<input type="hidden" name="grade" value="${(camTemp.grade)!?html}" id="grade_${camTemp.tempCode}_${index}"/>
          <div class="section-header"> <strong><span class="ui-icon icon-ttl-section-info"></span><@s.text name="cp.levelAndDate"/></strong> </div>
          <div class="section-content">
          <table class="detail" >
          <tr>
            <th><@s.text name="cp.memberLevel"/></th>
            <td><select name="memberLevelId" id="memberLevelId_${camTemp.tempCode}_${index}" onchange="TEMP001.changeDate(this);">
            		<#list camTemp.memberLevelList as level>
            			<option <#if (camTemp.memberLevelId)! == level.memberLevelId.toString() >selected="selected"</#if>  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
            </td>
            <th><@s.text name="cp.memberDate"/></th>
            <td>
            	<span><@s.property value="${(camTemp.memberDate)!}"/><@s.text name='${(camTemp.textName)!}'/></span>
            	<input type="hidden" name="memberDate" value="${(camTemp.memberDate)!?html}"/>
            	<input type="hidden" name="textName" value="${(camTemp.textName)!?html}"/>
            </td>
          </tr>
          </table>
		  </div>
        </div>
        <div class="section">
			<div class="section-header">
				<span class="ui-icon icon-ttl-section-info"></span>
				<input type="checkbox" value="1" <#if (camTemp.isCounter)! == "1">checked="checked"</#if> class="checkbox"  
            			id="check_isCounter" name="isCounter"  onclick="CAMPAIGN_TEMPLATE.showDiv(this, '#isCounterDiv');">
            			<label for="check_isCounter"><strong><@s.text name="cp.CounterCheck"/></strong></label>
				
			</div>
			<div class="section-content">
			<div class="box4 <#if camTemp.isCounter! != "1">hide</#if>" id="isCounterDiv" >
				<div class="box4-header"><strong><@s.text name="cp.clubDizhi" /></strong></div>
				<div class="box4-content">
				<div style="padding:0px;margin-top:15px;" class="ztree jquery_tree box2 box2-active">
	            <div class="box2-header clearfix">
	            	<p>
		 		  	<#-- 柜台类别  -->
		 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.countKbnTxt" /></span>
		 		  	<span style="margin-left:20px;">
		 		  		<@s.select name="counterKbn" list='#application.CodeTable.getCodes("1223")' 
							listKey="CodeKey" listValue="Value" value='${(camTemp.counterKbn)!}' />
		 		  	</span>
		 		  	</p>
		 		  	</br>
	            	<strong class="left active" style="font-size:14px;padding-top:2px;"><span class="ui-icon icon-flag" style="margin-top:5px;"></span><@s.text name="cp.clubSelCounter" /></strong>
	            	<span style="margin:3px 15px 0px 15px;" class="left">                      
	                   	<@s.text name="cp.clubLocationType" />
		            	<select id="locationType_0" name="locationType" onchange="CHERRYTREE.changeType(this,0);" style="width:120px;">
							<option value=""><@s.text name="global.page.select"/></option>
							<@getOptList list=application.CodeTable.getCodes("1156") val= camTemp.locationType! />
						</select>
	            	</span>
	            	<span id="importDiv_0" class="left <#if camTemp.locationType! != "5">hide</#if>" style="margin-left:10px;">
						<a href="/Cherry/download/柜台信息模板.xls"><@s.text name="cp.clubModDown"/></a>
						<input class="input_text" type="text" id="pathExcel_0" name="pathExcel" style="height:20px;"/>
					    <input type="button" value="<@s.text name="global.page.browse"/>"/>
					    <input class="input_file" style="height:auto;margin-left:-314px;" type="file" name="upExcel" id="upExcel_0" size="40" onchange="pathExcel_0.value=this.value;return false;" /> 
		        		<input type="button" value="导入" onclick="CHERRYTREE.ajaxFileUpload('0');return false;"/>
		        		<img id="loadingImg_0" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
					</span>
					
	            	<div id="locationId_0">
		            	<a onclick="CHERRYTREE.getPosition(0);return false;" class="search right" style="margin:3px 0 0 0;">
							<span class="ui-icon icon-position"></span>
							<span class="button-text"><@s.text name="cp.clubDingwei"/></span>
						</a>
						<input type="text" class="text right locationPosition ac_input" id="locationPositiontTxt_0" autocomplete="off">
					</div>
	          	</div>
	            <div id="tree_0" class="jquery_tree treebox tree" style="overflow:auto;height:300px;"></div>
	            <input type="hidden" name="placeJson" id="placeJson_0" value=""/>
	          	<input type="hidden" name="saveJson" id="saveJson_0" value=""/>
		   		</div>
		   		</div>
		   		</div>
			</div>
		</div>
        </div>
        <script>
			$(function() {
				// 活动范围树初始化
				CHERRYTREE.loadTree('${camTemp.placeJson!}',0);
			});
		</script>
        <#-- 等级和有效期(内容确认)  -->  
	<#elseif camTemp.tempCode == "BASE000007">
		<div class="box4" >
			<div class="box4-header" >
				<span><@s.text name="cp.levelAndDate" /></span>
			</div>
			<div class="box4-content" >
				 <table class="detail">
                      <tr>
                        <th width="100px"><@s.text name="cp.memberLevel"/></th>
                        <td>${(camTemp.levelName)!?html}</td>
                        <th><@s.text name="cp.memberDate"/></th>
                        <td>
                        <span><@s.property value="${(camTemp.memberDate)!}"/><@s.text name='${(camTemp.textName)!}'/></span></td>
                      </tr>
                  </table>
			</div>
		</div>
		<#if (camTemp.isCounter)! == '1' && camTemp.placeList?exists && camTemp.locationType !='0'>
        <div class="box4" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.CounterRange"/></strong></div>
	<div class="box4-content clearfix">
			<p>
             	<#-- 柜台类别  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.countKbnTxt" /></span>
                <label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1223", '${(camTemp.counterKbn)!"1"}')}</label>
              </p>
              </br>
			<div style="width:20%;float:left;">
			<span class="ui-icon icon-arrow-crm"></span>
			<@s.property value="#application.CodeTable.getVal('1156','${(camTemp.locationType)!?html}')"/>
			</div>
			<div style="width:80%;float:left;">
				<#list camTemp.placeList as placeMap>
					<#if placeMap.half=false>
						<span style="position: relative; margin-right:20px;">
						<#if placeMap.level=0>
						 <span style="color:#FF3030;white-space: nowrap;">${(placeMap.name)!?html}</span>
						</#if>
						<#if placeMap.level=1>
						 <span style="color:#FF7F24;white-space: nowrap;">${(placeMap.name)!?html}</span>
						</#if>
						<#if placeMap.level=2>
						 <span style="color:#4876FF;white-space: nowrap;">${(placeMap.name)!?html}</span>
						</#if>
						<#if placeMap.level=3>
						 <span style="color:#32CD32;white-space: nowrap;">${(placeMap.name)!?html}</span>
						</#if>
						</span>
					</#if>
				</#list>
			</div>
	</div>
</div>
</#if>
                <#-- 入会框  -->  
       <#elseif camTemp.tempCode == "BUS000016" >
       <#if "0" != (camTemp.showFlag)!>
       		<div class="group" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
				<input type="hidden" name="RELAT_OPER_KEY" value="1"/>
				<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
				<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
				<div class="group-header">
					<span id="hideInfo" class="expand left" onClick="TEMP001.showMore(this,'group','group-content',0)">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
					<strong>
					<#if "" == (camTemp.levelCalcKbn)!>
					<@s.text name="cp.joinRule"/>
            		<label style="color:red"><@s.text name="cp.fullOne" /></label>
            		<#else>
            		<@s.text name="cp.beThisLevel" />
            		</#if>
            		</strong>
				</div>
				<div  class="group-content">
				<#if "" == (camTemp.levelCalcKbn)!>
					<#list camTemp.combTemps as combTemp>
				        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
					</#list>
					<div class="group-header clearfix">
						<strong><@s.text name="cp.cardLimitTxt" /></strong>
					</div>
					<div class="group-content clearfix">
						<div class="box2">
							<div class="box2-header clearfix">
							    <strong class="left"><@s.text name="cp.cardTxt" /></strong>
						    </div>
						    <div class="box2-content selPro">
						    	 <p><@s.text name="cp.cardStartTxt" /><input type="text" value="${(camTemp.cardStart)!}" name="cardStart" class="text"/><br/>
						    	 	<label class="gray"><@s.text name="cp.multTextTxt" /></label>
						    	 </p>
						    </div>
						 </div>
					</div>
					<#else>
					<div class="group-header clearfix">
						<strong><@s.text name="cp.cardLimitTxt" /></strong>
					</div>
					<div class="group-content clearfix">
						<div class="box2">
							<div class="box2-header clearfix">
							    <strong class="left"><@s.text name="cp.cardTxt" /></strong>
						    </div>
						    <div class="box2-content selPro">
						    	 <p><@s.text name="cp.cardRule" /><input type="text" value="${(camTemp.cardReg)!}" name="cardReg" class="text"/><br/>
						    	 	<label class="gray"><@s.text name="cp.cardRuleDesp" /></label>
						    	 	<input type="hidden" value="${(camTemp.levelCalcKbn)!}" name="levelCalcKbn"/>
						    	 </p>
						    </div>
						 </div>
					</div>
				</#if>
				</div>
			</div>
			
		</#if>
		<#-- 入会确认框  -->  
         <#elseif camTemp.tempCode == "BUS000017" >
         <#if "0" != (camTemp.showFlg)!>
       		<div class="box4">
                <div class="box4-header" >
                	<span>
                	<#if "" == (camTemp.levelCalcKbn)!>
                	<@s.text name="cp.joinRule"/> 
                	<#else>
            		<@s.text name="cp.beThisLevel" />
            		</#if>
                	</span>
                </div>
                <div class="box4-content" >
                <#if "" == (camTemp.levelCalcKbn)!>
						<#list camTemp.combTemps as combTemp>
					        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
						</#list>
						<div class="box2">
						<div class="box2-header clearfix">
						    <strong class="left"><@s.text name="cp.cardTxt" /></strong>
					    </div>
				        <div class="box2-content">
			                  <table class="detail">
			
			      <tbody><tr>
		            <th width="100px"><@s.text name="cp.cardLimitTxt" /></th>
		            <td style="white-space: normal;">
		            <#if "" != (camTemp.cardStart)!>
		           <@s.text name="cp.cardStartTxt" /> ${(camTemp.cardStart)!}
		           <#else>
		        <@s.text name="cp.noAffilRule" />
		            </#if>
		         
				</td>
				<th></th>
		            <td>
		            </td>
		          </tr>
			        </tbody></table>
			            </div>
					</div>
				<#else>
				<div class="box2">
						<div class="box2-header clearfix">
						    <strong class="left"><@s.text name="cp.cardTxt" /></strong>
					    </div>
				        <div class="box2-content">
			                  <table class="detail">
			
			      <tbody><tr>
		            <th width="100px"><@s.text name="cp.cardLimitTxt" /></th>
		            <td style="white-space: normal;">
		          		 <@s.text name="cp.cardRule" /> ${(camTemp.cardReg)!}

				</td>
				<th></th>
		            <td>
		            </td>
		          </tr>
			        </tbody></table>
			            </div>
					</div>
			</#if>
                </div>
                
               </div>
             
           </#if>   
            
             <#-- 升级框  -->  
       <#elseif camTemp.tempCode == "BUS000006" >
       <#if "0" != (camTemp.showFlag)!>
       		<div class="group" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
				<div class="group-header">
					<span id="hideInfo" class="expand" onClick="TEMP001.showMore(this,'group','group-content',0)">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
					<strong><@s.text name="cp.upLevel"/>
            		<input type="checkbox" value="1" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> class="checkbox" 
            			name="isChecked"  id="check_${camTemp.tempCode}_${index}"  onClick="TEMP001.showMore(this,'group','group-content',1);">
					<label for="check_${camTemp.tempCode}_${index}"><@s.text name="cp.setUpRule"/></label>
            		</strong>
				</div>
				<div  class="group-content" <#if (camTemp.isChecked)! == "1" >style="display:block;"<#else>style="display:none;"</#if> >
				<div class="toolbar clearfix">
					<a class="add right" onClick="CAMPAIGN_TEMPLATE.addRule(this,'${camTemp.tempCode}_${index}', 'group');">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.addRule"/></span></a>
				</div>
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line_Yellow">
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				<div id="add_${camTemp.tempCode}_${index}" class="hide no_submit">
				<div class="line_Yellow">
				</div>
				<#list camTemp.addCombTemps as combTemp>
			          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
				</#list>
				</div>
				</div>
			</div>
		</#if>
			<#-- 升级设置框  -->  
       <#elseif camTemp.tempCode == "BUS000007" >
       		<div  class="group" id="${camTemp.tempCode}_${index}">
       		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
			<input type="hidden" name="RELAT_OPER_KEY" value="1"/>
			<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
       		<div class="group-header graybg">
				<label><@s.text name="cp.upToLevel"/></label>
				<span style="position:relative;"><select name="upMemberLevelId" id="memberLevelId_${index}">
				<#if (camTemp.upMemberLevelList?? && camTemp.upMemberLevelList?size > 0) >
        		<#list camTemp.upMemberLevelList as level>
        			<option <#if (camTemp.upMemberLevelId)! == level.memberLevelId.toString() >selected="selected"</#if>  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
        		</#list>
        		</#if>
        		</select></span>
				<span class="expand" onclick="TEMP001.showMore(this, 'group', 'group-content',0);">
				<span class="ui-icon ui-icon-triangle-1-n"></span></span>
			</div>
			<div class="group-content graybroder">
			<div class="toolbar clearfix">
				<a class="add right deleteClass" id="deleteUp_${index}" name="deleteUp" onclick="CAMPAIGN_TEMPLATE.deleteRule(this,'group')">
				<span class="ui-icon icon-delete"></span>
				<span class="button-text"><@s.text name="cp.delRule"/></span></a>
			</div>
			<#if camTemp.combTemps?? >
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
			<#else>
				<#list camTemp.addCombTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
			</#if>
			</div>
			</div>
			
			<#-- 降级框  -->  
       <#elseif camTemp.tempCode == "BUS000010" >
       	<#if "0" != (camTemp.showFlag)!>
       		<div class="group" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
				<div class="group-header">
					<span id="hideInfo" class="expand left" onClick="TEMP001.showMore(this, 'group' , 'group-content',0)">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
					<strong><@s.text name="cp.downLevel"/>
            		<input type="checkbox" value="1" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> class="checkbox"  
            			id="check_${camTemp.tempCode}_${index}" name="isChecked"  onClick="TEMP001.showMore(this, 'group' , 'group-content',1)">
            		<label for="check_${camTemp.tempCode}_${index}"><@s.text name="cp.setDownRule"/></label>
            		</strong>
				</div>
				<div  class="group-content" <#if (camTemp.isChecked)! == "1" >style="display:block;"<#else>style="display:none;"</#if> >
				<div class="toolbar clearfix">
					<a class="add right" onClick="CAMPAIGN_TEMPLATE.addRule(this,'${camTemp.tempCode}_${index}', 'group');">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.addRule"/></span>
					</a>
				</div>
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line_Yellow">
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				<div id="add_${camTemp.tempCode}_${index}" class="hide no_submit">
				<div class="line_Yellow">
				</div>
				<#list camTemp.addCombTemps as combTemp>
			          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
				</#list>
				</div>
				</div>
			</div>
		</#if>
			
		<#-- 降级设置框  -->  
       <#elseif camTemp.tempCode == "BASE000011" >
       		<div id="${camTemp.tempCode}_${index}" class="group">
       		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
			<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
       			<div class="group-header">
        			<span class="expand left" onClick="TEMP001.showMore(this, 'group','group-content',0);">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
            		<label><@s.text name="cp.downToLevel"/></label>
					<span style="position:relative;"><select name="downMemberLevelId" id="memberLevelId_${index}">
            			<#list camTemp.downMemberLevelList as level>
            			<option <#if (camTemp.downMemberLevelId)! == level.memberLevelId.toString() >selected="selected"</#if>  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            			</#list>
            		</select></span>
            	</div>
            	<div class="group-content" >
            		<div class="toolbar clearfix">
		       			<a class="add right deleteClass" id="deleteDown_${index}" name="deleteDown" onclick="CAMPAIGN_TEMPLATE.deleteRule(this,'group')">
		        			<span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="cp.delRule"/></span>
	        			</a>
            		</div>
					<table border="0" class="clean">
              		<tr class="trClass">
                	<td width="100%">
                		<p><label class="bg_title"><@s.text name="cp.endMemberDate" /></label></p>
                    	<p><input type="radio" class="radio" id="downMonth_${index}" name="downLevel_${index}" value="0" <#if (camTemp.downLevel)! == "0" || (camTemp.downLevel)! == "">checked="checked"</#if>/>
					  		<label for="downMonth_${index}"><@s.text name="cp.notUp" /></label>
                    	</p>
                    	<p class="clearfix">
                     		<input type="radio" class="radio" id="downSet_${index}" name="downLevel_${index}" value="1" <#if (camTemp.downLevel)! == "1" || (camTemp.downLevel)! == "">checked="checked"</#if>/>
                      		<label for="downSet_${index}"><@s.text name="cp.selfSetRule"/> <@s.text name="cp.inValidDayTxt" /></label>
						</p>
				  		<#--<p style="margin-left:4em"><@s.text name="cp.useMoney" />
							<span><input type="text" class="number" name="minMoneyDown" value="${(camTemp.minMoneyDown)!}"><@s.text name="cp.yuan" /></span>
				  		</p>-->
				  		<p style="margin-left:4em">
				  			<span>累计开始时间：
		                	<select name="downTStime" style="width:120px;" onchange="TEMP001.showSelSpan(this,'downTStime_')">
		            			<option <#if (camTemp.downTStime)! != "1"  >selected="selected"</#if>  value="0" >有效期开始日</option>
		            			<option <#if (camTemp.downTStime)! == "1" >selected="selected"</#if>  value="1" >每年 </option>
		            		</select>
		            		<span id="downTStime_1" <#if (camTemp.downTStime)! != "1" >class="hide"</#if>> <input type="text" name="tsdMonth" class="number" value="${(camTemp.tsdMonth)!?html}"/> 月  <input type="text" name="tsdDay" class="number" value="${(camTemp.tsdDay)!?html}"/> 日 </span>
		            		</span>
				  		</p>
				  		<p style="margin-left:4em"><@s.text name="cp.totalAmountDownTxt"/>
							<span><input type="text" class="number" name="minMoney" value="${(camTemp.minMoney)!}"><@s.text name="cp.yuan" /></span>
				  		</p>
				  		<p style="margin-left:4em"><@s.text name="cp.totalTimeDownTxt"/>
							<span><input type="text" class="number" name="minTimeDown" value="${(camTemp.minTimeDown)!}"><@s.text name="cp.count" /></span>
				  		</p>
					  	<p style="margin-left:4em"><@s.text name="cp.firstBillInclude" />
	                  	<input type="radio" name="plusfirstDownSel_${index}" id="downyes_${index}" class="radio" value="0" <#if (camTemp.plusfirstBillSel)! == "0">checked="checked"</#if>/>
						<label for="downyes_${index}"><@s.text name="cp.yes" /></label>
						<input type="radio" name="plusfirstDownSel_${index}" id="downno_${index}" value="1" class="radio" <#if (camTemp.plusfirstBillSel)! == "" || (camTemp.plusfirstBillSel)! == "1">checked="checked"</#if>/>
						<label for="downno_${index}"><@s.text name="cp.no" /></label>
						</p>
                  	</td>
              		</tr>
            		</table>
            	</div>
			</div>
			<#-- 失效框  -->  
         <#elseif camTemp.tempCode == "BUS000012" >
       	<#if "0" != (camTemp.showFlag)!>
       		<div class="group" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<div class="group-header">
					<strong><@s.text name="cp.lose" />
            		<input type="checkbox" value="1" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> class="checkbox"  
            			id="check_${camTemp.tempCode}_${index}" name="isChecked"  onclick="CAMPAIGN_TEMPLATE.showDiv(this, '#info_${camTemp.tempCode}_${index}');">
            		<label><@s.text name="cp.loseRule" /></label>
            		</strong>
				</div>
				<div id="info_${camTemp.tempCode}_${index}" class="group-content" <#if (camTemp.isChecked)! == "1" >style="display:block;"<#else>style="display:none"</#if> >
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
			</div>
       	</#if>
			<#-- 失效设置框  -->  
       <#elseif camTemp.tempCode == "BASE000013" >
       		<div class="group" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
				<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
				<div class="group-header">
					<strong><@s.text name="cp.memberLoseRule" /></strong>
				</div>
				<div class="group-content">
					<table border="0" class="clean">
					<tr>
                		<td width="100%">
                		<p><label class="bg_title"><@s.text name="cp.endMemberDate" /></label></p>
                    	<p><input type="radio" class="radio" name="loseLevel_${index}" id="loseMonth" value="0" <#if (camTemp.loseLevel)! == "0" || (camTemp.loseLevel)! == "">checked="checked"</#if>/>
					  		<label for="loseMonth"><@s.text name="cp.notUp" /></label>
                    	</p>
                    	<p class="clearfix">
                     		<input type="radio" class="radio" id="loseSet" name="loseLevel_${index}" value="1" <#if (camTemp.loseLevel)! == "1" || (camTemp.loseLevel)! == "">checked="checked"</#if>/>
                      		<label for="loseSet"><@s.text name="cp.selfSetRule"/></label>
						</p> 
						<div style="margin-left:4em" class="left">
					  		<p><@s.text name="cp.useMoney" /><label style="margin-left:2em;vertical-align:middle;"><@s.text name="cp.noPass" /></label>
								<span><input type="text" class="number" name="minMoneyLose" value="${(camTemp.minMoneyLose)!}"><@s.text name="cp.yuan" /></span>
					  		</p>
					  		<p><@s.text name="cp.useCount" /> <label style="margin-left:2em;vertical-align:middle;"><@s.text name="cp.less" /></label>
								<span><input type="text" class="number" name="minTimeLose" value="${(camTemp.minTimeLose)!}"><@s.text name="cp.count" /></span>
					  		</p>
					  	</div>
                  		</td>
               	 	</tr>
					</table>
				</div>
			</div>
			<#-- 升级确认框  -->  
         <#elseif camTemp.tempCode == "BUS000008" >
         <#if "0" != (camTemp.showFlg)!>
       		<div class="box4">
				<div class="box4-header">
					<@s.text name="cp.upLevel"/>
				</div>
				<div class="box4-content" >
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line_Yellow">
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
              </div>
           </#if>   
              <#-- 升级设置确认框  -->  
         <#elseif camTemp.tempCode == "BUS000009" >
       		<div class="box2">
				<div class="box2-header clearfix" >
					<strong class="left"><@s.text name="cp.upLevelTo" />${(camTemp.levelName)!?html}</strong>
				</div>
				<div class="box2-content" >
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
            </div>
            
            <#-- 降级确认框  -->  
         <#elseif camTemp.tempCode == "BUS000011" >
         <#if "0" != (camTemp.showFlg)!>
       		<div class="box4">
                <div class="box4-header" >
                	<span><@s.text name="cp.downLevel"/></span>
                </div>
                <div class="box4-content" >
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line_Yellow">
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
              </div>
           </#if>  
              <#-- 降级设置确认框  -->  
         <#elseif camTemp.tempCode == "BASE000012" >
       		<div class="box2">
				<div class="box2-header clearfix" >
					<strong class="left" ><@s.text name="cp.downLevelTo" />${(camTemp.levelName)!?html}</strong>
				</div>
				<div class="box2-content" >
				<#if (camTemp.downLevel)! == "1" >
					<#if "" != (camTemp.downTStime)!>
					<p>
						<label>
							累计开始时间：
							<#if (camTemp.downTStime)! == "1"  >
								每年 ${(camTemp.tsdMonth)!} 月 ${(camTemp.tsdDay)!} 日
							<#else>
								有效期开始日
							</#if>
						</label>
					</p>
					</#if>
					<p><label><@s.text name="cp.endMemberDate" /></label></p>
					<#if "" != (camTemp.minMoney)!>
					<p>
					<label><@s.text name="cp.totalAmountDownTxt" /></label><label>
						<span style="color:red;">${(camTemp.minMoney)!}</span><@s.text name="cp.yuan" /></label></p>
					</#if>
					<#if "" != (camTemp.minTimeDown)!>
					<p><label><@s.text name="cp.totalTimeDownTxt" /></label><label>
						<span style="color:red;">${(camTemp.minTimeDown)!}</span><@s.text name="cp.count" /></label></p>
					</#if>
					<#if "0" == (camTemp.plusfirstDownSel)!>
		        		<label><@s.text name="cp.includeFirstTxt" /></label>
		        	<#else>
		        		<label><@s.text name="cp.noIncludeFirstTxt" /></label>
		        	</#if>
		     	<#elseif (camTemp.downLevel)! == "0">
		         	<label>
		         	<span class="gray"><@s.text name="cp.endMemberDate" /><@s.text name="cp.notUp" /></span></label>
		     	</#if>
		     	</div>
            </div>
            
             <#-- 失效确认框  -->  
         <#elseif camTemp.tempCode == "BUS000013" >
         <#if "0" != (camTemp.showFlg)!>
       		<div class="box4" id="${camTemp.tempCode}_${index}">
                <div class="box4-header" >
                	<span><@s.text name="cp.lose" /> </span>
                </div>
                <div class="box4-content" >
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				<div>
              </div>
             </#if>
              <#-- 失效设置确认框  -->  
         <#elseif camTemp.tempCode == "BASE000014" >
       		<div class="box2">
				<div class="box2-header clearfix">
					<strong class="left" ><@s.text name="cp.loseRule" /></strong>
				</div>
				<div class="box2-content" >
				<#if (camTemp.loseLevel)! == "1" >
					<p><label><@s.text name="cp.useMoney" /></label><label><@s.text name="cp.noPass" />
						<span style="color:red;">${(camTemp.minMoneyLose)!}</span><@s.text name="cp.yuan" /></label></p>
					<#if "" != (cp.useCount)!>
					<p><label><@s.text name="cp.useCount" /></label><label><@s.text name="cp.less" />
						<span style="color:red;">${(camTemp.minTimeLose)!}</span><@s.text name="cp.count" /></label></p>
		     		</#if>
		     	<#elseif (camTemp.loseLevel)! == "0">
		         	<label>
		         	<span class="gray"><@s.text name="cp.endMemberDate" /><@s.text name="cp.notUp" /></label>
		     	</#if>
				</div>
            </div>
            
             <#-- 单次购买确认框  -->  
         <#elseif camTemp.tempCode == "BUS000014">
       		<#if "0" != (camTemp.showFlg)!>
       		<div class="section">
			<div class="section-header">
			    <strong class="left"><@s.text name="cp.onceBuy" /></strong>
			</div>
	        <div class="section-content">
                  <table class="detail">
                      <#list camTemp.combTemps as combTemp>
				          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				        </#list>
                  </table>
            </div>
			</div>
           </#if>
        
         <#-- 累积购买确认框  -->  
         <#elseif camTemp.tempCode == "BUS000015" >
       		<#if "0" != (camTemp.showFlg)!>
       		<div class="section">
			<div class="section-header">
			    <strong class="left"><@s.text name="cp.plusBuy" /></strong>
			</div>
	        <div class="section-content">
                  <table class="detail">
                      <#list camTemp.combTemps as combTemp>
				          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				        </#list>
                  </table>
            </div>
			</div>
           </#if>
           
	   <#-- 规则描述模块 -->
	   <#elseif camTemp.tempCode == "BASE000050">
		<#if (camTemp.depList?? && camTemp.depList?size>0)>
		<div class="box4">
			<div class="box4-header clearfix">
				<strong class="left" ><@s.text name="cp.ruleDel" /></strong>
			</div>
			<div class="box4-content" >
				<#list camTemp.depList as depInfo>
					<p>
						<label>${(depInfo.ruleContent)!?html}</label>
					</p>
				</#list>
			</div>
		</div>
		</#if>
	</#if>
	</@s.i18n>
