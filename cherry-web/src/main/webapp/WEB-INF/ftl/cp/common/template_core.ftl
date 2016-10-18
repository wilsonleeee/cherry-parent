	<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<#-- 活动条件确认  -->
	<#if camTemp.tempCode == "BUS000003">
	<div class="section">
          <div class="section-header"><span class="ui-icon icon-ttl-section-info"></span><strong><@s.text name="cp.baseInfo"/></strong></div>
          <div class="section-content">
            <table border="0" class="detail">
              <tr>
                <th scope="row"><@s.text name="cp.mainActName"/></th>
                <td>${(campInfo.campaignName)!?html}</td>
                <th scope="row"><@s.text name="cp.brand"/></th>
                <td>${(campInfo.brandName)!?html}</td>
              </tr>
              <tr>
                <th scope="row"><@s.text name="cp.creater"/></th>
                <td>${(campInfo.campaignSetByName)!?html}</td>
                <#if (memberClubId)! == "">
                <th scope="row"><@s.text name="cp.actDes"/></th>
                <td style="white-space: normal;"><span>${(campInfo.descriptionDtl)!?html} </span></td>
                <#else>
                 <th scope="row">会员俱乐部</th>
                <td style="white-space: normal;"><span>${(campInfo.clubName)!?html}</span></td>
                </#if>
              </tr>
              <tr>
                <th><@s.text name="cp.ruleDate"/></th>
                <td <#if (memberClubId)! == "">colspan="3" </#if>><label>${(campInfo.campaignFromDate)!?html}</label>
                <#if (campInfo.campaignToDate)! == "">
                	<label class="gray"><@s.text name="cp.notEndTimeTip" /></label>
                <#else>
                	 ～ ${(campInfo.campaignToDate)!?html}
                </#if>
                </td>
                <#if (memberClubId)! != "">
                	<th scope="row"><@s.text name="cp.actDes"/></th>
                	<td style="white-space: normal;"><span>${(campInfo.descriptionDtl)!?html} </span></td>
                </#if>
              </tr>
            </table>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><span class="ui-icon icon-ttl-section-info"></span><strong><strong><@s.text name="cp.details"/></strong></div>
          <div class="section-content">
              <#list camTemp.combTemps as combTemp>
		          <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		        </#list>
          </div>
        </div>
        
		<#elseif camTemp.tempCode == "BASE000026">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="purchase"/></th>
    		<td width="100%">
    		<div class="cond-context" id="div_main" style="height:125px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
			<div class="column" style="width:50%;height:100px;">
	        <input type="hidden" name="selPrtIndex" value="${(camTemp.tempCode)!?html}_${index}"/>
	        <p <#if ((campInfo.templateType)! == "PT01" || (campInfo.campaignType)! == "1" || (campInfo.campaignType)! == "2")>class="hide"</#if>>
	        		<span class="context-title">
			  		<@s.text name="cp.nowLevel"/>
			  		</span>
			  		<span>
			  		<select name="memberLevelId" id="memberLevelId_${camTemp.tempCode}_${index}">
	            		<#list memberList as level>
	            			<option  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
	            		</#list>
	            	</select>
	            	</span>
	        	</p>
        	<p>
		  		<span <#if (campInfo.templateType)! != "PT10">class="context-title"<#else>class="context-title hide"</#if>>
		  			<@s.text name="cp.purchaseDate"/>
		  		</span>
		  		<span <#if (campInfo.templateType)! == "PT10">class="context-title"<#else>class="context-title hide"</#if>>
		  			<@s.text name="cp.retTime"/>
		  		</span>
		  		<span>
		  		 <input type="text" class="date singleDate" name="ticketDate"/> 
		  		</span>
        	</p>
        	<p>
		  		<span <#if (campInfo.templateType)! != "PT10">class="context-title"<#else>class="context-title hide"</#if>>
		  			<@s.text name="cp.useMoney"/>
		  		</span>
		  		<span <#if (campInfo.templateType)! == "PT10">class="context-title"<#else>class="context-title hide"</#if>>
		  			<@s.text name="cp.retMoney"/>
		  		</span>
		  		<span>
		  		<input type="text" name="amount" class="number"/><@s.text name="cp.yuan"/>
		  		</span>
        	 </p> 
        	 </div>
        	 <div class="column last" style="width:49%;height:55px;">  
	        	<p>
	        		<span class="context-title">
	        			购买柜台
	        		</span>
	        		<span>
	        			<input type="text" class="text" id="counterName" name="counterName" onClick="TEMP000.showCounterTable(this);return false;" />
	        			<input type="hidden" id="counterCode" name="counterCode" />
	        		</span>
	        	</p>
        	<p>
		  		<span class="context-title" id="buyPro" onClick="TEMP000.showTable(this)" style="cursor:pointer;color:green;">
		  			<#if (campInfo.templateType)! != "PT10"><@s.text name="cp.buyPro"/><#else><@s.text name="retGoodes"/></#if></span>
		  		<span id="prtSel_${(camTemp.tempCode)!?html}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
	         </span>
        	</p>
	          </div>
        	 <#include "/WEB-INF/ftl/common/popProductTable.ftl">
			 <#include "/WEB-INF/ftl/common/popCounterTable.ftl">
        	</div>
      </td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000033">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="memberInfo"/></th>
    		<td width="100%">
    		<div class="cond-context" style="height:125px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
	        <input type="hidden" name="selPrtIndex" id="selPrtIndex" value="${(camTemp.tempCode)!?html}_${index}"/>
        	<div class="column" style="width:50%;height:100px;">
        	<p>
		  		<span class="context-title">
		  		<@s.text name="cp.memberLevel"/>
		  		</span>
		  		<span>
		  		 <select name="memberLevelId" id="memberLevelId_${camTemp.tempCode}_${index}">
            		<#list memberList as level>
            			<option  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
		  		</span>
        	</p>
        	 <p>
		  		<span class="context-title">
		  		<@s.text name="cp.useCount"/>
		  		</span>
		  		<span>
		  		<input type="text" name="amount" class="number"/><@s.text name="cp.second"/>
		  		</span>
        	 </p>
        	 <p>
		  		<span class="context-title">
		  		<@s.text name="cp.lastUpDate"/>
		  		</span>
		  		<span style="margin-left:5px">
		  		 <input type="text" class="date singleDate" name="ticketDate"/> 
		  		</span>
        	 </p>
        	 </div>
        	 <div class="column last" style="width:49%;height:55px;">  
        	<p>
		  		<span class="context-title">
		  		<@s.text name="cp.addMoney"/>
		  		</span>
		  		<span>
		  		<input type="text" name="amount" class="number"/><@s.text name="cp.yuan"/>
		  		</span>
        	 </p>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="cp.addTime"/>
		  		</span>
		  		<span>
		  		<input type="text" name="amount" class="number"/><@s.text name="cp.month"/>
		  		</span>
        	 </p>
        	 </div>
        	</div>
      </td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000035">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="cp.joinInfo"/></th>
    		<td width="100%">
    		<div class="cond-context" style="height:70px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
        	<div>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="cp.joinTime"/>
		  		</span>
		  		<span>
		  		 <input type="text" class="date singleDate" name="ticketDate"/> 
		  		</span>
        	</p>
        	 <p>
		  		<span class="context-title">
		  		<@s.text name="cp.joinLevel"/>
		  		</span>
		  		<span>
		  		<select name="memberLevelId" id="memberLevelId_${camTemp.tempCode}_${index}">
            		<#list memberList as level>
            			<option  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
            	</span>
        	 </p>
        	 </div>
        	</div>
      </td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000036">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="cp.memberInfo"/></th>
    		<td width="100%">
    		<div class="cond-context" style="height:40px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
        	<div>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="cp.memberBir"/>
		  		</span>
		  		<span>
		  		 <input type="text" class="date singleDate" name="ticketDate"/> 
		  		</span>
        	</p>
        	 </div>
        	</div>
      		</td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000037">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="memberInfo"/></th>
    		<td width="100%">
    		<div class="cond-context" style="height:70px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
        	<div>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="nowLevel"/>
		  		</span>
		  		<span>
		  		<select name="memberFromLevelId" id="memberLevelId_${camTemp.tempCode}_${index}">
            		<#list memberList as level>
            			<option  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
            	</span>
        	</p>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="upLevel"/>
		  		</span>
		  		<span>
		  		<select name="memberToLevelId" id="memberLevelId_${camTemp.tempCode}_${index}">
            		<#list memberList as level>
            			<option  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
            	</span>
        	</p>
        	 </div>
        	</div>
      		</td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000038">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="memberInfo"/></th>
    		<td width="100%">
    		<div class="cond-context" style="height:30px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
        	<div>
        	<p>
		  		<@s.text name="buyBill"/>
		  		<input type="text" class="input" name="bill"/>
		  		<@s.text name="bill" />
        	</p>
        	 </div>
        	</div>
      		</td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000039">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="memberInfo"/></th>
    		<td width="100%">
    		<div class="cond-context" style="height:70px;">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
        	<div>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="nowLevel"/>
		  		</span>
		  		<span>
		  		<select name="memberLevelId" id="memberLevelId_${camTemp.tempCode}_${index}">
            		<#list memberList as level>
            			<option  value="${level.memberLevelId.toString()}" > ${(level.levelName)!?html} </option>
            		</#list>
            	</select>
            	</span>
        	</p>
        	<p>
		  		<span class="context-title">
		  		<@s.text name="getPointDate"/>
		  		</span>
		  		<span>
		  		 <input type="text" class="date singleDate" name="ticketDate"/> 
		  		</span>
        	</p>
        	 </div>
        	</div>
      		</td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000040">
		<tr id="${camTemp.tempCode}_${index}">
			<th><@s.text name="purchase"/></th>
    		<td width="100%">
    		<div class="cond-context" id="div_main">
    		<input type="hidden" name="tempCode" value="${camTemp.tempCode}"/>
			<input type="hidden" name="groupCode" value="${camTemp.groupCode}"/>
			<input type="hidden" name="RULE-TEST-FLAG" value="1"/>
	        <input type="hidden" name="selPrtIndex" value="${(camTemp.tempCode)!?html}_${index}"/>
        	<p>
		  		<span class="context-title">
		  			<@s.text name="purchaseDate"/>
		  		</span>
		  		<span>
		  		 <input type="text" class="date singleDate" name="ticketDate"/> 
		  		</span>
        	</p>
        	<p>
        		<span class="context-title">
		  			<label id="buyPro" onClick="TEMP000.showTable(this, '0')" style="cursor:pointer;color:green;"><@s.text name="buyPro"/></label>
		  		</span>
		  		<span id="bugProTest">
	         </span>
        	</p>
   				<#include "/WEB-INF/ftl/common/popProductTable.ftl">
        	</div>
      </td>
   	 	</tr>
   	 	
   	 	<#elseif camTemp.tempCode == "BASE000041">
   	 	<tr id="${camTemp.tempCode}_${index}">
   	 		<th>销售记录</th>
   	 		<td width="100%">
   	 		<div id="div_main">
   	 		<input type="button" name="addJon" class="add right" value="添加" onClick="TEMP000.addBuyRecord(this);return false;"/>
   	 		<#include "/WEB-INF/ftl/common/popProductTable.ftl">
			<#include "/WEB-INF/ftl/common/popCounterTable.ftl"></div></td>
   	 	</tr>
		<tr>
			<th>第<label id="billLabel" style="color:#ffffff;padding-bottom: 5px;">1</label>单</th>
    		<td width="100%">
    		<div class="cond-context" style="height:105px;">
	       	<div class="buyRecord">
	       	 <div class="column" style="width:50%;height:80px;">  
	        	<input type="hidden" name="selPrtIndex" value="${(camTemp.tempCode)!?html}_${index}"/>
	        	<p>
	        		<span class="context-title"><@s.text name="purchaseDate"/></span>
	        		<span><input type="text" class="date singleDate" name="ticketDate"/> </span>
	        	</p>
	        	<p>
	        		<span class="context-title"><@s.text name="useMoney"/></span>
	        		<span><input type="text" name="amount" class="number"/><@s.text name="yuan"/></span>
	        	</p>
	        	</div>
        	 <div class="column last" style="width:49%;height:55px;">  
	        	<p>
	        		<span class="context-title">
	        			购买柜台
	        		</span>
	        		<span>
	        			<input type="text" class="text" id="counterName" name="counterName" onClick="TEMP000.showCounterTable(this);return false;" />
	        			<input type="hidden" id="counterCode" name="counterCode" />
	        		</span>
	        	</p>
	        	<p>
	        		<span class="context-title" id="buyPro" onClick="TEMP000.showTable(this)" style="cursor:pointer;color:green;">
	        		<@s.text name="buyPro"/></span>
	        		<span id="prtSel_${(camTemp.tempCode)!?html}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
	        	</p>
	        	</div>
	        </div>
        	</div>
      		</td>
   	 	</tr>
	</#if>
	</@s.i18n>