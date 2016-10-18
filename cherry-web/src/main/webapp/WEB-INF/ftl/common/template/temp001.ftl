<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#-- 购买产品  -->
	<#if camTemp.tempCode == "BASE000001">
	<tr id="BASE000001_${index}">
	    <th scope="row"><span class="title"><span>${(camTemp.tempName)!?html}</span></span> </th>
	    <td width="100%">
	    <div class="box3 selPro">
	        <p>
	          <input id="radio_any_${index}" type="radio" class="radio" onClick="TEMP001.boldLabel(this)" name="prtRadio_${index}" value="0" <#if (camTemp.prtRadio)! == "0" || (camTemp.prtRadio)! == "">checked="checked"</#if>/>
	          <label for="radio_any_${index}"><@s.text name="buyEvePro" /></label>
	        </p>
	        <p class="clearfix proClass">
	        <span class="left">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
	        <input type="hidden" name="selPrtIndex" value="${index}"/>
	          <input id="radio_appoint_${index}" type="radio" class="radio" name="prtRadio_${index}" onClick="TEMP001.showTable(this)" value="1" <#if (camTemp.prtRadio)! == "1">checked="checked"</#if>/>
	          <label id="choicePro" class="errorMsg" for="radio_appoint_${index}"><@s.text name="buyPointPro" /></label>
	          </span>
	          <span class="left">
	          <br /> 
	          <span id="prtSel_BASE000001_${index}" class="showPro_${index}">
	          <#if (camTemp.productList?? && camTemp.productList?size > 0) >
	          	<#list camTemp.productList as productList>
	          		<span class="span_BASE000001">
	          			<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
	          			<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
	          			<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
	          			<span>${(productList.nameTotal)!?html}</span> 
	          			<span class="close" onclick="TEMP001_delete(this);return false;">
	          			<span class="ui-icon ui-icon-close"></span></span>
	          			<span onclick="TEMP001_openTable(this);return false;" class="add">
	          			<span class="ui-icon ui-icon-plus"></span></span>
	          		</span>
   				</#list>
	          </#if>
	         </span>
	         </span>
	         </p>
	      </div></td>
  	</tr>
  	
  	<#-- 购买产品(内容确认)  -->
  	<#elseif camTemp.tempCode == "BASE000004">
	      <tr>
            <th width="100px"><@s.text name="buyPro" /></th>
            <td>
            <#if (camTemp.prtRadio)! == "1" >
	            <#if (camTemp.productList?? && camTemp.productList?size > 0) >
	            <#list camTemp.productList as productList>
		          		<span class="span_BASE000001" style="margin-right:10px;background-color:#FFEC9D">
		          			<span>${(productList.nameTotal)!?html}</span> 
		          		</span>
	   				</#list>
		         </#if>
		     <#elseif (camTemp.prtRadio)! == "0">
		         	<@s.text name="buyEvePro" />
		     </#if>
	         </td>
          </tr>
	      
	      
  	<#-- 消费金额  -->
	<#elseif camTemp.tempCode == "BASE000002">
          <tr id="BASE000002_${index}">
            <th scope="row"><span class="title"><span>${(camTemp.tempName)!?html}</span></span></th>
            <td><div class="box3">
            	<input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
            	<input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
                <p><@s.text name="moreOrPass" /> <span>
                  <input type="text" class="number" name="minAmount" value="${(camTemp.minAmount)!?html}"/>
                  	<@s.text name="yuan" />  </span>
                  	<@s.text name="less" /> <span>
                  <input type="text" class="number" name="maxAmount" value="${(camTemp.maxAmount)!?html}"/>
                  	<@s.text name="yuan" /></span></p>
              </div></td>
          </tr>
    <#-- 消费金额(内容确认)  -->
	<#elseif camTemp.tempCode == "BASE000006">
		<tr>
            <th><@s.text name="useMoney" /></th>
            <td>
            	<#if "" != (camTemp.minAmount)!>
            	<@s.text name="moreOrPass" /> <span class="red">${(camTemp.minAmount)!?html}</span> <@s.text name="yuan" />
	            </#if>
	             <#if "" != (camTemp.maxAmount)!>
	            	<@s.text name="less" /><span class="red">${(camTemp.maxAmount)!?html}</span> <@s.text name="yuan" />
	            </#if>
            </td>
          </tr>
          
	
    <#-- 累积时间  -->
    <#elseif camTemp.tempCode == "BASE000003">
    	<tr id="BASE000003_${index}">
            <th scope="row"><span class="title"><span>${(camTemp.tempName)!?html}</span></span></th>
            <td><div class="box3">
                <p><@s.text name="continue" /><span>
                  <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
                  <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
                  <input type="text" name="monthNum" class="number" value="${(camTemp.monthNum)!?html}"/>
                  </span><@s.text name="inMonth" />  </p>
              </div></td>
          </tr>
          
          
     <#-- 累积时间 (内容确认)  -->
	<#elseif camTemp.tempCode == "BASE000005">
	<tr>
	    <th width="100px"><@s.text name="plusTime" /></th>
	    <td>
	    	<#if "" != (camTemp.monthNum)!>
        		<@s.text name="continue" /><span class="red">${(camTemp.monthNum)!?html}</span> <@s.text name="inMonth" /></td>
        	</#if>
        </td>
	  </tr>
     
	
     <#-- 单次购买或累积购买  -->  
	<#elseif camTemp.tempCode == "BUS000001" || camTemp.tempCode == "BUS000002">
	<div class="box2" id="${camTemp.tempCode}_${index}">
		<input type="hidden" id="tempCode_${camTemp.tempCode}_${index}" name="tempCode" value="${camTemp.tempCode}"/>
		<input type="hidden" id="groupCode_${camTemp.tempCode}_${index}" name="groupCode" value="${camTemp.groupCode}"/>
		<input type="hidden" name="relationChar" value="&&" class="no_submit"/>
      <div class="box2-header clearfix"> <strong class="left">${camTemp.tempName}
        <input type="checkbox" id="check_${camTemp.tempCode}_${index}" class="checkbox" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> name="isChecked"
        	onclick="CAMPAIGN_TEMPLATE.showDiv(this, '#condition_${camTemp.tempCode}_${index}');" value="1"/>
        <label><@s.text name="setInRule" /></label><label style="color:red; "><@s.text name="enAll" /></label>
        </strong> </div>
      <div id="condition_${camTemp.tempCode}_${index}" class="box2-content clearfix" <#if (camTemp.isChecked)! == "1">style="padding:.5em .5em 0 .5em;" <#else>style="padding:.5em .5em 0 .5em;display:none;"</#if>>
        <table border="0" class="clean">
        <#if camTemp.combTemps?? >
			<#list camTemp.combTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		<#else>
			<#list camTemp.addCombTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		</#if>
        </table>
      </div>
    </div> 
    
    <#-- 单次购买或累积购买(内容确认)  -->  
	<#elseif camTemp.tempCode == "BUS000004" || camTemp.tempCode == "BUS000005">
	<#if "0" != (camTemp.showFlg)!>
		<tr>
            <th scope="row" width="200px;">
            <span class="loud large">${camTemp.tempName}</span></th>
            <td width="100%"><div class="box4">
                <div class="box4-content clearfix">
                  <table border="0" width="100%" class="clean">
                      <#list camTemp.combTemps as combTemp>
				          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				        </#list>
                  </table>
                </div>
              </div>
             </td>
          </tr>
     </#if>
		<#-- 等级和有效期  -->  
		<#elseif camTemp.tempCode == "BASE000010">
		<div class="box2" id="${camTemp.tempCode}_${index}">
			<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
          <div class="box2-header clearfix"> <strong class="left"><@s.text name="lelAndDate" /></strong> </div>
          <div class="box2-content clearfix block2">
            <p>
            <label><@s.text name="memberLevel" /></label>
            	<select name="memberLevelId" id="memberLevelId_${camTemp.tempCode}_${index}" onchange="TEMP001.changeDate(this);">
            		<#list camTemp.memberLevelList as level>
            			<option <#if (camTemp.memberLevelId)! == level.memberLevelId.toString() >selected="selected"</#if>  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
            </p>
            <#if "2" == (camTemp.campaignType)!>
            <p class="clearfix" id="levelDateList">
              <label><@s.text name="memberLevelDate" /></label>
         		${(camTemp.levelDateList)!}
            </p>
            </#if>
            <p>
            <label><@s.text name="ruleType" /></label>
            <@s.select  name="ruleType" id="ruleType_${camTemp.tempCode}_${index}" list='#application.CodeTable.getCodes("1128")' listKey="CodeKey" listValue="Value" value="${(camTemp.ruleType)!?html}"/>
            </p>
            <p>
            <label><@s.text name="ruleDate" /></label>
            <span>
            <input type="text" class="date" id="campFromDate_${camTemp.tempCode}_${index}" name="fromDate" value="${(camTemp.fromDate)!?html}"/> 
            </span>~ 
            <span>
            <input type="text" class="date" id="campToDate_${camTemp.tempCode}_${index}" name="toDate" value="${(camTemp.toDate)!?html}"/>
            </span>
            </p>
			 </div>
        </div>
        
        <#-- 等级和有效期(内容确认)  -->  
	<#elseif camTemp.tempCode == "BASE000007">
		 <tr>
                <th scope="row"><span class="loud large">${(camTemp.tempName)!?html}</span></th>
                <td width="100%"><div class="box4">
                    <div class="box4-content clearfix">
                      <table border="0" width="100%" class="clean">
                          <tr>
                            <th width="100px"><@s.text name="memberLevel" /></th>
                            <td>${(camTemp.levelName)!?html}</td>
                          </tr>
                          <tr>
                            <th><@s.text name="ruleType" /></th>
                            <td>
                            <@s.property value='#application.CodeTable.getVal("1128", ${(camTemp.ruleType)!?html})'/></td>
                          </tr>
                          <tr>
                            <th><@s.text name="ruleDate" /></th>
                            <td>${(camTemp.fromDate)!?html} ～ ${(camTemp.toDate)!?html}</td>
                          </tr>
                      </table>
                    </div>
                  </div></td>
              </tr>
             <#-- 升级框  -->  
       <#elseif camTemp.tempCode == "BUS000006" >
       <#if "0" != (camTemp.showFlag)!>
       		<div class="box4" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
				<div class="box4-header">
					<a class="add right" onClick="TEMP001.addRule(this,'${camTemp.tempCode}_${index}');"><span class="ui-icon icon-add"></span><span class="button-text">添加规则</span></a>
					<span id="hideInfo" class="expand left" onClick="TEMP001.showMore(this,'box4','box4-content',0)">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
					<strong><@s.text name="upLevel" />
            		<input type="checkbox" value="1" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> class="checkbox" 
            			name="isChecked"  id="check_${camTemp.tempCode}_${index}"  onClick="TEMP001.showMore(this,'box4','box4-content',1);">
					<label><@s.text name="upLevelSet" /></label>
            		</strong>
				</div>
				<div  class="box4-content" <#if (camTemp.isChecked)! == "1" >style="display:block;"<#else>style="display:none;"</#if> >
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line" style="padding:0 1.5em 0 1.5em">
							<hr class="dotted2" />
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				<div id="add_${camTemp.tempCode}_${index}" class="hide no_submit">
				<div class="line" style="padding:0 1.5em 0 1.5em">
					<hr class="dotted2" />
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
       		<div  class="box4-content" id="${camTemp.tempCode}_${index}">
       		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
       		<div style="padding:0 0 0 1.4em">
				<a class="add right deleteClass" name="deleteUp" onclick="TEMP001.deleteRule(this,'box4-content')">
				<span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="deleteLevel" /></span></a>
				<p>
					<span class="expand left" onclick="TEMP001.showMore(this, 'box4-content', 'box2',0);">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
					<label><@s.text name="upToLevel" /></label>
					<span><select name="upMemberLevelId" id="memberLevelId_${index}">
            		<#list camTemp.upMemberLevelList as level>
            			<option <#if (camTemp.upMemberLevelId)! == level.memberLevelId.toString() >selected="selected"</#if>  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            		</select></span>
				</p>
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
			
			<#-- 降级框  -->  
       <#elseif camTemp.tempCode == "BUS000010" >
       	<#if "0" != (camTemp.showFlag)!>
       		<div class="box4" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
				<div class="box4-header">
					<a class="add right" onClick="TEMP001.addRule(this,'${camTemp.tempCode}_${index}');">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="addRule" /></span>
					</a>
					<span id="hideInfo" class="expand left" onClick="TEMP001.showMore(this, 'box4' , 'box4-content',0)">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
					<strong><@s.text name="downLevel" />
            		<input type="checkbox" value="1" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> class="checkbox"  
            			id="check_${camTemp.tempCode}_${index}" name="isChecked"  onClick="TEMP001.showMore(this, 'box4' , 'box4-content',1)">
            		<label><@s.text name="setDownLevel" /></label>
            		</strong>
				</div>
				<div  class="box4-content" <#if (camTemp.isChecked)! == "1" >style="display:block;"<#else>style="display:none;"</#if> >
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line" style="padding:0 1.5em 0 1.5em">
							<hr class="dotted2" />
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				<div id="add_${camTemp.tempCode}_${index}" class="hide no_submit">
				<div class="line" style="padding:0 1.5em 0 1.5em">
					<hr class="dotted2" />
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
       		<div  class="box4-content" id="${camTemp.tempCode}_${index}">
       		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
       			<div style="padding:0 0 0 1.4em">
       			<a class="add right deleteClass" name="deleteDown" onclick="TEMP001.deleteRule(this,'box4-content')">
        			<span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="deleteLevel" /></span>
        			</a>
        			<span class="expand left" onClick="TEMP001.showMore(this, 'box4-content','trClass',0);">
					<span class="ui-icon ui-icon-triangle-1-n"></span>
					</span>
            		<label><@s.text name="downToLevel" /></label>
					<span><select name="downMemberLevelId" id="memberLevelId_${index}">
            			<#list camTemp.downMemberLevelList as level>
            			<option <#if (camTemp.downMemberLevelId)! == level.memberLevelId.toString() >selected="selected"</#if>  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            			</#list>
            		</select></span>
					<table border="0" class="clean">
              	<tr class="trClass">
                	<td width="100%">
                    	<p><input type="radio" class="radio" name="downLevel_${index}" value="0" <#if (camTemp.downLevel)! == "0" || (camTemp.downLevel)! == "">checked="checked"</#if>/><@s.text name="full" />
					  		<span><input type="text" class="number" name="fullMonthDown" value="${(camTemp.fullMonthDown)!}"/><@s.text name="monthUp" /></span>
                    	</p>
                    	<p class="clearfix">
                     		<input type="radio" class="radio" name="downLevel_${index}" value="1" <#if (camTemp.downLevel)! == "1" || (camTemp.downLevel)! == "">checked="checked"</#if>/>
                      		<label><@s.text name="selfRule" /></label>
						</p>
						<div style="margin-left:4em" class="left">
					  		<p><@s.text name="plusTime" />  
								<label style="margin-left:2em;vertical-align:middle;"><@s.text name="continue" /></label>
								<span><input type="text" class="number" name="monthNumDown" value="${(camTemp.monthNumDown)!}"><@s.text name="monthOne" /></span>
					  		</p>
					  		<p> <@s.text name="plusMoney" /> <label style="margin-left:2em;vertical-align:middle;"><@s.text name="noPass" /></label>
								<span><input type="text" class="number" name="minMoneyDown" value="${(camTemp.minMoneyDown)!}"><@s.text name="yuan" /></span>
					  		</p>
					  		<p> <@s.text name="plusNum" /> <label style="margin-left:2em;vertical-align:middle;"><@s.text name="lessThen" /></label>
								<span><input type="text" class="number" name="minTimeDown" value="${(camTemp.minTimeDown)!}"><@s.text name="time" /></span>
					  		</p>
					  	</div>
                  </td>
              	</tr>
            	</table>
				</div>
			</div>
			<#-- 失效框  -->  
         <#elseif camTemp.tempCode == "BUS000012" >
       		<div class="box4" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<div class="box4-header">
					<strong><@s.text name="loseLevel" />
            		<input type="checkbox" value="1" <#if (camTemp.isChecked)! == "1">checked="checked"</#if> class="checkbox"  
            			id="check_${camTemp.tempCode}_${index}" name="isChecked"  onclick="CAMPAIGN_TEMPLATE.showDiv(this, '#info_${camTemp.tempCode}_${index}');">
            		<label><@s.text name="setLoseLevel" /></label>
            		</strong>
				</div>
				<div id="info_${camTemp.tempCode}_${index}" <#if (camTemp.isChecked)! == "1" >style="display:block;"<#else>style="display:none"</#if> >
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
			</div>
			<#-- 失效设置框  -->  
       <#elseif camTemp.tempCode == "BASE000013" >
       		<div class="box4-content" id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<div style="padding:0 0 0 1.4em">
					<table border="0" class="clean">
        			<tr>
            			<td><label><@s.text name="memberLoseLevel" /></label>
						</td>
            		</tr>
					<tr>
                		<td width="100%">
                    	<p><input type="radio" class="radio" name="loseLevel_${index}" value="0" <#if (camTemp.loseLevel)! == "0" || (camTemp.loseLevel)! == "">checked="checked"</#if>/><@s.text name="full" />
					  		<span><input type="text" class="number" name="fullMonthLose" value="${(camTemp.fullMonthLose)!}"/><@s.text name="monthUp" /></span>
                    	</p>
                    	<p class="clearfix">
                     		<input type="radio" class="radio" name="loseLevel_${index}" value="1" <#if (camTemp.loseLevel)! == "1" || (camTemp.loseLevel)! == "">checked="checked"</#if>/>
                      		<label><@s.text name="selfRule" /></label>
						</p>
						<div style="margin-left:4em" class="left">
					  		<p><@s.text name="plusTime" />
								<label style="margin-left:2em;vertical-align:middle;"><@s.text name="continue" /></label>
								<span><input type="text" class="number" name="monthNumLose" value="${(camTemp.monthNumLose)!}"><@s.text name="monthOne" /></span>
					  		</p>
					  		<p><@s.text name="plusMoney" /> <label style="margin-left:2em;vertical-align:middle;"><@s.text name="noPass" /></label>
								<span><input type="text" class="number" name="minMoneyLose" value="${(camTemp.minMoneyLose)!}"><@s.text name="yuan" /></span>
					  		</p>
					  		<p><@s.text name="plusNum" /> <label style="margin-left:2em;vertical-align:middle;"><@s.text name="lessThen" /></label>
								<span><input type="text" class="number" name="minTimeLose" value="${(camTemp.minTimeLose)!}"><@s.text name="time" /></span>
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
       		<tr id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
                <th scope="row"><span class="loud large"><@s.text name="upLevel" /> </span></th>
                <td>
                <div class="box4">
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line" style="padding:0 1.5em 0 1.5em">
							<hr class="dotted2" />
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
				</td>
              </tr>
           </#if>   
              <#-- 升级设置确认框  -->  
         <#elseif camTemp.tempCode == "BUS000009" >
       		<div id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<p style="margin-top:0.5em;margin-left:1em;"><@s.text name="upLevelTo" /><label style="color:red;">${(camTemp.levelName)!?html}</label></p>
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
            </div>
            
            <#-- 降级确认框  -->  
         <#elseif camTemp.tempCode == "BUS000011" >
         <#if "0" != (camTemp.showFlg)!>
       		<tr id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
                <th scope="row"><span class="loud large"><@s.text name="downLevel" /></span></th>
                <td>
                <div class="box4">
				<#list camTemp.combTemps as combTemp>
					<#if combTemp_index != 0>
						<div class="line" style="padding:0 1.5em 0 1.5em">
							<hr class="dotted2" />
						</div>
					</#if>
			        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
				</div>
				</td>
              </tr>
           </#if>  
              <#-- 降级设置确认框  -->  
         <#elseif camTemp.tempCode == "BASE000012" >
       		<div id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<p style="margin-top:0.5em;margin-left:1em;"><@s.text name="downLevelTo" /><label style="color:red;">${(camTemp.levelName)!?html}</label></p>
				<#if (camTemp.downLevel)! == "1" >
	            	<p><label style="margin-left:2em;vertical-align:middle;"><@s.text name="continue" /><span style="color:red">${(camTemp.monthNumDown)!}</span><@s.text name="monthOne" /></label></p>
					<p><label style="margin-left:2em;vertical-align:middle;"><@s.text name="noPass" /></label><span style="color:red">${(camTemp.minMoneyDown)!}</span><@s.text name="yuan" /></p>
					<p><label style="margin-left:2em;vertical-align:middle;"><@s.text name="lessThen" /></label><span style="color:red">${(camTemp.minTimeDown)!}</span><@s.text name="time" /></p>
		     	<#elseif (camTemp.downLevel)! == "0">
		         	<label style="margin-left:2em;vertical-align:middle;"><@s.text name="full" /></label><span style="color:red">${(camTemp.fullMonthDown)!}</span><@s.text name="monthUp" />
		     	</#if>
            </div>
            
             <#-- 失效确认框  -->  
         <#elseif camTemp.tempCode == "BUS000013" >
         <#if "0" != (camTemp.showFlg)!>
       		<tr id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
                <th scope="row"><span class="loud large"><@s.text name="loseLevel" /></span></th>
				<#list camTemp.combTemps as combTemp>
			          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
              </tr>
             </#if>
              <#-- 失效设置确认框  -->  
         <#elseif camTemp.tempCode == "BASE000014" >
       		<td id="${camTemp.tempCode}_${index}">
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<div class="box4">
				<#if (camTemp.loseLevel)! == "1" >
	            	<p><label style="margin-left:2em;vertical-align:middle;"><@s.text name="continue" /></label><span style="color:red">${(camTemp.monthNumLose)!}</span><@s.text name="monthOne" /></p>
					<p><label style="margin-left:2em;vertical-align:middle;"><@s.text name="noPass" /></label><span style="color:red">${(camTemp.minMoneyLose)!}</span><@s.text name="yuan" /></p>
					<p><label style="margin-left:2em;vertical-align:middle;"><@s.text name="lessThen" /></label><span style="color:red">${(camTemp.minTimeLose)!}</span><@s.text name="time" /></p>
		     	<#elseif (camTemp.loseLevel)! == "0">
		         	<label style="margin-left:2em;vertical-align:middle;"><@s.text name="full" /></label><span style="color:red">${(camTemp.fullMonthLose)!}</span><@s.text name="monthUp" />
		     	</#if>
				</div>
            </td>
            
             <#-- 单次购买和累积购买确认框  -->  
         <#elseif camTemp.tempCode == "BUS000014" || camTemp.tempCode == "BUS000015" >
       		<#if "0" != (camTemp.showFlg)!>
				<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
				<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
				<p style="margin-top:0.5em;margin-left:1em;">${(camTemp.tempName)!}</p>
                  <table border="0" width="100%" class="clean">
                    <#list camTemp.combTemps as combTemp>
				        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
				    </#list>
                  </table>
           </#if>
	</#if>
</@s.i18n>