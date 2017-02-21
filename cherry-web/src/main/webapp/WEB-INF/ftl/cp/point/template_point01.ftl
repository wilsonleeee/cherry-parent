<@s.i18n name="i18n.cp.BINOLCPCOM01">
<#-- 赠与积分  -->
	<#if camTemp.tempCode == "BASE000015">
	
		<tr id="${camTemp.tempCode}_${index}" class="giveClass">
		    <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
		    <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		    <th><@s.text name="cp.selectPointType"/></th>
		    <td>
		    	<@s.select id="calcuKbnSel_${index}" name="calcuKbn" onchange="TEMP002.changeLabel(this);TEMP002.showDefRule(this, '2')" list='#application.CodeTable.getCodes("1167")' 
					listKey="CodeKey" listValue="Value" value="${(camTemp.calcuKbn)!?html}" />
		    </td>
		    <th>
		    	<span id="mulTxt" <#if (camTemp.calcuKbn)! == "2">class="hide"</#if>>
					<@s.text name="cp.mulPointTxt" />
				</span>
				<span id="pointTxt" <#if (camTemp.calcuKbn)! != "2">class="hide"</#if>>
					<@s.text name="cp.selectMarkGive"/>
				</span>
			</th>
		    <td <#if (camTemp.segmePoints?? && camTemp.segmePoints?size>0) > class="hide" </#if>>
		    	<span>
					<input type="text" class="number" name="multipleMark" value="${(camTemp.multipleMark)!?html}">
				</span>
				<span id="mul" style="margin-left:5px;" <#if (camTemp.calcuKbn)! == "2">class="hide"</#if>>
					<@s.text name="cp.mul" />
				</span>
				<span id="point" style="margin-left:5px;" <#if (camTemp.calcuKbn)! == "1" || (camTemp.calcuKbn)! == "">class="hide"</#if>>
					<@s.text name="cp.mark" />
				</span>
				<#-- 按单笔消费额分段  -->
				<a class="right" style="cursor:pointer" onClick="TEMP002.segmePoint(this);"><span class="ui-icon icon-add"></span><@s.text name="cp.segmeTxt" /></a>
			</td>	
			 <td id="segmePoints" <#if (!camTemp.segmePoints?? || camTemp.segmePoints?size == 0) >class="hide" </#if>>
			 	<a class="add left" onClick="TEMP002.addSegme(this);">
				<span class="ui-icon icon-add"></span>
				<span class="button-text"><@s.text name="cp.add" /></span></a>
		    	<#if (camTemp.segmePoints?? && camTemp.segmePoints?size>0) >
		    		<#list camTemp.segmePoints as segmePointInfo>
		    			<div class="segmeContent" style="clear:both;border:thin dotted #C0C0C0;margin-top:10px;">
			    		<a class="add right" onclick="TEMP002.delSegme(this);">
					<span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="cp.delete" /></span>
	                     </a>
	                     <#-- 金额下限  -->
			    		<span style="float:none"><label class="gray"><@s.text name="cp.lowerTxt" /></label><@s.text name="cp.oneBuyTxt" />
			    	<@s.select name="lowerLimit" list='#application.CodeTable.getCodes("1211")' 
					listKey="CodeKey" listValue="Value" value="${(segmePointInfo.lowerLimit)!}"/>
			    			<input type="text" class="number" name="lowerAmount" value="${(segmePointInfo.lowerAmount)!}"/> <@s.text name="cp.yuan" /></span><br/>
			    			<#-- 金额上限  -->
			    			<span style="float:none"><label class="gray"><@s.text name="cp.highTxt" /></label><@s.text name="cp.oneBuyTxt" />
			    			<@s.select name="highLimit" list='#application.CodeTable.getCodes("1212")' 
					listKey="CodeKey" listValue="Value" value="${(segmePointInfo.highLimit)!}" />
			    			<input type="text" class="number" name="highAmount" value="${(segmePointInfo.highAmount)!}"/> <@s.text name="cp.yuan" /></span><br/>
			    			<#-- 积分  -->
			    			<span style="float:none"><label class="gray"><@s.text name="cp.segPointTxt" /></label><input type="text" class="number" name="rewardPoint" value="${(segmePointInfo.rewardPoint)!}"/> 
			    			<label id="mul" <#if (camTemp.calcuKbn)! == "2">class="hide"</#if>>
								<@s.text name="cp.mul" />
							</label>
							<label id="point" <#if (camTemp.calcuKbn)! != "2">class="hide"</#if>>
								<@s.text name="cp.mark" />
							</label>
			    		</span><br/>
			    	</div>
		    		</#list>
		    	</#if>
		    	<div class="hide" id="segmePanel">
		    		<div class="segmeContent" style="clear:both;border:thin dotted #C0C0C0;margin-top:10px;">
			    		<a class="add right" onclick="TEMP002.delSegme(this);">
					<span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="cp.delete" /></span>
	                     </a>
	                    <#-- 金额下限  --> 
			    		<span style="float:none"><label class="gray"><@s.text name="cp.lowerTxt" /></label><@s.text name="cp.oneBuyTxt" />
			    	<@s.select name="lowerLimit" list='#application.CodeTable.getCodes("1211")' 
					listKey="CodeKey" listValue="Value" />
			    			<input type="text" class="number" name="lowerAmount"/> <@s.text name="cp.yuan" /></span><br/>
			    			<#-- 金额上限  -->
			    			<span style="float:none"><label class="gray"><@s.text name="cp.highTxt" /></label><@s.text name="cp.oneBuyTxt" />
			    			<@s.select name="highLimit" list='#application.CodeTable.getCodes("1212")' 
					listKey="CodeKey" listValue="Value" />
			    			<input type="text" class="number" name="highAmount"/> <@s.text name="cp.yuan" /></span><br/>
			    			<#-- 积分  -->
			    			<span style="float:none"><label class="gray"><@s.text name="cp.segPointTxt" /></label><input type="text" class="number" name="rewardPoint"/> 
			    			<label id="mul" <#if (camTemp.calcuKbn)! == "2">class="hide"</#if>>
								<@s.text name="cp.mul" />
							</label>
							<label id="point" <#if (camTemp.calcuKbn)! != "2">class="hide"</#if>>
								<@s.text name="cp.mark" />
							</label>
			    		</span><br/>
			    	</div>
		    	</div>
			</td>	
		</tr>
		
		<#if "2" != (campInfo.pointRuleType)!>
		<tr id="DEFAULTRULESEL_${index}"  <#if (camTemp.defaultExecSel)! == ""> class="hide" </#if>>
			<th><@s.text name="cp.execDefaultTxt" /></th>
		    <td>
		    	<input type="radio" name="defaultExecSel_${index}" id="defYes_${index}" class="radio" value="0" <#if (camTemp.defaultExecSel)! == "" || (camTemp.defaultExecSel)! == "0">checked="checked"</#if>/>
				<label for="defYes_${index}"><@s.text name="cp.yes" /></label>
				<input type="radio" name="defaultExecSel_${index}" id="defNo_${index}" value="1" class="radio" <#if (camTemp.defaultExecSel)! == "1">checked="checked"</#if>/>
				<label for="defNo_${index}"><@s.text name="cp.no" /></label><br/>
				<span><label class="gray"><@s.text name="cp.execDefaultDesp" /></label></span> <br/>
				<span class="hide" id="allPrtDesp_${index}"><label class="gray"><@s.text name="cp.allPrtDespTxt" /></label></span>
				<span class="hide" id="specPrtDesp_${index}"><label class="gray"><@s.text name="cp.specPrtDespTxt" /></label></span>
		    </td>
		</tr>
		<script type="text/javascript">
		if ($("#rangeFlag").val() == "0") {
				$("#allPrtDesp_${index}").hide();
				$("#specPrtDesp_${index}").show();
			} else {
				$("#allPrtDesp_${index}").show();
				$("#specPrtDesp_${index}").hide();
			}
		</script>
		</#if>
<#-- 赠与积分(确认画面)  -->
	<#elseif camTemp.tempCode == "BASE000020">
	    <#if (camTemp.segmePoints?? && camTemp.segmePoints?size>0) >
	    	<#list camTemp.segmePoints as segmePointInfo>
	    	<p>
	    	<#if (segmePointInfo.lowerAmount?? && "" != segmePointInfo.lowerAmount) >
	    	<#-- 金额下限  --> 
	    	<label class="gray"><@s.text name="cp.lowerTxt" /></label><label><@s.text name="cp.oneBuyTxt" />
				${application.CodeTable.getVal("1211","${(segmePointInfo.lowerLimit)!?html}")}</label>
		    			<label style="color:red;"> ${(segmePointInfo.lowerAmount)} </label> <label><@s.text name="cp.yuan" /> </label><br/>
		    </#if>
		    <#if (segmePointInfo.highAmount?? && "" != segmePointInfo.highAmount) >
		    			<#-- 金额上限  -->
		    			<label class="gray"><@s.text name="cp.highTxt" /></label><label><@s.text name="cp.oneBuyTxt" />
				${application.CodeTable.getVal("1212","${(segmePointInfo.highLimit)!?html}")}</label>
		    			<label style="color:red;">${(segmePointInfo.highAmount)}</label> <label><@s.text name="cp.yuan" /></label><br/>
		   	</#if>
		    			<label class="gray">
		    			<#if (camTemp.calcuKbn)! == "2">
			    		<@s.text name="cp.markTxt" />
			    		<#else>
			    		<@s.text name="cp.mulTxt" />
			    		</#if>
			    		</label>
		    			<label style="color:red;">${(segmePointInfo.rewardPoint)}</label>
		    			<label id="mul" <#if (camTemp.calcuKbn)! == "2">class="hide"</#if>>
							<@s.text name="cp.mul" />
						</label>
						<label id="point" <#if (camTemp.calcuKbn)! != "2">class="hide"</#if>>
							<@s.text name="cp.mark" />
						</label>
		    </p>
		    </#list>
	    <#else>
	    <p>
	    	<span>
	    	<label>
	    		<#if (camTemp.calcuKbn)! == "2">
	    		<@s.text name="cp.markTxt" />
	    		<#else>
	    		<@s.text name="cp.mulTxt" />
	    		</#if>
	    	</label>
	    	<label style="color:red;">${(camTemp.multipleMark)!?html}</label>
	    	<label><#if (camTemp.calcuKbn)! == "2">
	    		<@s.text name="cp.mark" />
	    		<#else>
	    		<@s.text name="cp.mul" />
	    		</#if></label>
	    	</span>
	    </p>
	    </#if>
		<#if "" != (camTemp.defaultExecSel)!>
		<p>
		    <span>
		    	<label><@s.text name="cp.execDefaultTxt" /></label>
		    	<label class="gray"><#if "0" == (camTemp.defaultExecSel)!>
		    			<@s.text name="cp.yes" />
		    			<#else>
		    			<@s.text name="cp.no" />
		    			</#if>
		    	</label>
		    </span>
		   </p>
		</#if>
		
<#-- 活动范围  -->
	<#elseif camTemp.tempCode == "BASE000016">
		<div class="box4" id="${camTemp.tempCode}_${index}">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
	        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	        <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
			<div class="box4-header">
				<strong><@s.text name="cp.actLimit" /></strong>
			</div>
			<div class="box4-content">
			<p><input type="checkbox" name="timeSetting" <#if (camTemp.timeSetting)! == "1">checked="checked"</#if> value="1" onclick="TEMP002.showTimeSetting(this)"/>
				<#-- 规则执行时间需要精确到秒  -->
				<span><@s.text name="cp.timeSettingTxt" /></span>
				
			</p>
			<div id="timeTemplate" <#if (camTemp.timeSetting)! != "1">class="hide"</#if>>
			<p>
			<span style="margin-left:60px;">
						<#-- 开始日  -->
				        <span class="bg_title" style="margin-left:20px;"><@s.text name="cp.startDateTxt" /></span> ${(campInfo.campaignFromDate)!?html}
				        <#-- 时间  -->
				        <span class="bg_title" style="margin-left:20px;"><@s.text name="cp.timeTxt" /></span>	
				        <input type="text" class="input focusShow" name="startHH" value="${(camTemp.startHH)!"00"?html}" maxlength="2" />  ：<input type="text" class="input focusShow" name="startMM" value="${(camTemp.startMM)!"00"?html}" maxlength="2"/>  ：<input type="text" class="input focusShow" name="startSS" value="${(camTemp.startSS)!"00"?html}" maxlength="2"/>
			</span>
			</p>
			
			<p>
			<span style="margin-left:60px;">
			<#if (campInfo.campaignToDate)! == "">
				<span style="margin-left:20px;"><label class="gray"><@s.text name="cp.notEndTimeTip" /></label></span>
			<#else>		<#-- 结束日  -->
				        <span class="bg_title" style="margin-left:20px;"><@s.text name="cp.endDateTxt" /></span> ${(campInfo.campaignToDate)!?html}
				        <#-- 时间  -->
				        <span class="bg_title" style="margin-left:20px;"><@s.text name="cp.timeTxt" /></span>	
				        <input type="text" class="input focusShow" name="endHH" value="${(camTemp.endHH)!"23"?html}" maxlength="2"/>  ：<input type="text" class="input focusShow" name="endMM" value="${(camTemp.endMM)!"59"?html}" maxlength="2"/>  ：<input type="text" class="input focusShow" name="endSS" value="${(camTemp.endSS)!"59"?html}" maxlength="2"/>
				        <#-- 包含结束时间  -->
				        <span><label class="gray"><@s.text name="cp.endTimeDesp" /></label></span>
			</#if>
			</span>
			</p>
			<script type="text/javascript">
			       $("#timeTemplate").find(":input['.focusShow']").bind({
			       		"focus" : function(){ 
							var name = $(this).prop("name");
							var val = $(this).val();
							$(this).val("");
							if (val != "") {
								TEMP002.tempVal[name] = val;
							}
						},
						"focusout" : function(){ 
							var name = $(this).prop("name");
							var val = $(this).val();
							if (val == "") {
								$(this).val(TEMP002.tempVal[name]);
							} else {
								val = $.trim(val);
								if (val.length == 1) {
									var re = /^[0-9]$/; 
									if (re.test(val)) {
										val = "0" + val;
										TEMP002.tempVal[name] = val;
										$(this).val(val);
									}
								}
							}
						}
			       });
			 </script>
			</div>
			<p>
 		  	<#-- 规则属性  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointRuleKbnTxt" /></span>
 		  	<span style="margin-left:20px;">
 		  	<#if optKbn == 2>
	            	<select disabled="true">
		            <#list application.CodeTable.getCodes("1172") as pointRuleInfo>
		            	<option <#if (camTemp.pointRuleKbn)! == pointRuleInfo.CodeKey.toString() >selected="selected"</#if>  value="${pointRuleInfo.CodeKey.toString()}" >
		            	${(pointRuleInfo.Value)!?html}
		            	</option>
		            </#list>
		            </select>
		            <input type="hidden" name="pointRuleKbn" id="pointRuleKbnSel" value="${(camTemp.pointRuleKbn)!?html}" />
		        <#else>
 		  		<@s.select id="pointRuleKbnSel" name="pointRuleKbn" onchange="TEMP002.validSubRuleKbn('#pointRuleKbnSel','${camTemp.tempCode}_${index}')" list='#application.CodeTable.getCodes("1172")' 
					listKey="CodeKey" listValue="Value" value="${(camTemp.pointRuleKbn)!?html}" />
				</#if>	
 		  	</span>
 		  	</p>
 		  	<p>
			<#-- 规则模板  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointType" /></span>
 		  	<span style="margin-left:20px;">
 		  		 <#if optKbn == 2>
	            	<select disabled="true">
		            <#list application.CodeTable.getCodes("1165") as pointInfo>
		            	<option <#if (camTemp.templateType)! == pointInfo.CodeKey.toString() >selected="selected"</#if>  value="${pointInfo.CodeKey.toString()}" >
		            	${(pointInfo.Value)!?html}
		            	</option>
		            </#list>
		            </select>
		            <input type="hidden" name="templateType" id="templateType_${camTemp.tempCode}_${index}" value="${(camTemp.templateType)!?html}"/>
		        <#else>
		        	<#if (application.CodeTable.getCodes("1165")?? && application.CodeTable.getCodes("1165")?size > 0)>
		            <select name="templateType" id="templateType_${camTemp.tempCode}_${index}" onchange="TEMP002.selRuleKbn(this);">
		            <#list application.CodeTable.getCodes("1165") as pointInfo>
		            	<option <#if (campInfo.templateType)! == pointInfo.CodeKey.toString() >selected="selected"</#if>  value="${pointInfo.CodeKey.toString()}" >
		            	${(pointInfo.Value)!?html}
		            	</option>
		            </#list>
		            </select>
		            </#if>
	            </#if>
 		  	</span>
 		  	</p>
 		  	<p>
 		  	<#-- 附属方式  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.subRuleKbnTxt" /></span>
 		  	<span style="margin-left:20px;">
 		  	<@s.select id="subRuleKbnSel" name="subRuleKbn" list='#application.CodeTable.getCodes("1182")' 
					listKey="CodeKey" cssStyle="width:180px;" listValue="Value" value="${(camTemp.subRuleKbn)!?html}" />
				<span style="font-size: 12px; color: rgb(128, 128, 128); clear: both;"> <@s.text name="cp.subRuleKbnDesp" /> </span>
				<script type="text/javascript">
			       		TEMP002.validSubRuleKbn('#pointRuleKbnSel');
			      </script>
 		  	</span>
 		  	</p>
 		  	<p>
 		  	<#-- 积分类型  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointKbn" /></span>
 		  	<span style="margin-left:20px;">
 		  		<@s.select name="pointType" list='#application.CodeTable.getCodes("1183")' 
					listKey="CodeKey" listValue="Value" value="${(camTemp.pointType)!?html}" />
 		  	</span>
 		  	</p>
				<p>
					<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.actObj" /></span>
					<span style="margin-left:20px;"><@s.select style="margin-left:20px;" name="member" id="member" onchange="TEMP002.showMember(this)" list='#application.CodeTable.getCodes("1155")' 
						listKey="CodeKey" listValue="Value" value="${(camTemp.member)!?html}" />
					</span>
					<span style="margin-left:20px;">
				        	<input type="checkbox" name="referKbn" id="referKbn" <#if (camTemp.referKbn)! == "1">checked="checked"</#if> value="1"/>
				        	<label for="referKbn" style="cursor:pointer;color: rgb(128, 128, 128);"><@s.text name="cp.referTxt" /></label></span>
				      </span>
				      <span style="margin-left:20px;">
				        	<input type="checkbox" name="refPTKbn" id="refPTKbn" <#if (camTemp.refPTKbn)! == "1">checked="checked"</#if> value="1"/>
				        	<label for="refPTKbn" style="cursor:pointer;color: rgb(128, 128, 128);"><@s.text name="cp.refPTKbnTxt" /></label></span>
				      </span>
				</p>
				<p style="margin-left:60px;" <#if (camTemp.member)! != "1">class="hide"</#if> id="memberInfo">
					<#if (camTemp.memberLevelList?? && camTemp.memberLevelList?size > 0)>
				        <#list camTemp.memberLevelList as memberLevel>
				        <span class="memberList" style="margin-left:20px;">
				        	<input type="checkbox" name="memberShowFlag" <#if (memberLevel.memberShowFlag)! == "1">checked="checked"</#if> value="1"/>
				        	<span>${(memberLevel.levelName)!}</span>
				        	<input type="hidden" name="levelName" value="${(memberLevel.levelName)!}"/>
				        	<input type="hidden" name="memberLevelId" value="${(memberLevel.memberLevelId)!}"/>
				        </span>
				        </#list>
			        </#if>
				</p>
			<p>
			<#-- 会员积分  -->
 		  	<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.memberPoint" /></span>
				<span>
				<select style="margin-left:20px;" name="mPoint" id="mPointSel" onchange="TEMP002.showOrHidePanel(this,'#mPointCk','2')">
					<option <#if (camTemp.mPoint)! == "1" >selected="selected"</#if> value="1"><@s.text name="cp.noLimitTxt" /></option>
					<option <#if (camTemp.mPoint)! == "2" >selected="selected"</#if> value="2"><@s.text name="cp.mempointRange" /></option>
				</select> 
				</span>
			</p>
			<p style="margin-left:80px;" <#if (camTemp.mPoint)! != "2">class="hide"</#if> id="mPointCk">
			      <span><input type="text" name="minpt" class="number" value="${(camTemp.minpt)!}"></span> - 
                 <span><input type="text" name="maxpt" class="number" value="${(camTemp.maxpt)!}"></span>
				 <br/><label class="gray"><@s.text name="cp.pointRangeDesp" /></label>
			</p>
			<@s.text id="pageSel" name="global.page.select"/>
 		  	<#-- 入会日期  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.jnDateCondTxt" /></span>
 		  	<span style="margin-left:20px;">
 		  	<@s.select id="jnDateSel" name="jnDate" list='#application.CodeTable.getCodes("1264")' 
					listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{pageSel}" value="${(camTemp.jnDate)!?html}" />
 		  	</span>
 		  	</p>
 		  	<#-- 入会途径  -->
 		  	<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.channelTxt" /></span>
				<span>
				<select style="margin-left:20px;" name="channelSel" id="channelSel" onchange="TEMP002.showChannel(this)">
					<option <#if (camTemp.channelSel)! == "1" >selected="selected"</#if> value="1"><@s.text name="cp.allValue" /></option>
					<option <#if (camTemp.channelSel)! == "2" >selected="selected"</#if> value="2"><@s.text name="cp.selSomeTxt" /></option>
				</select> 
				</span>
			</p>
			<p style="margin-left:60px;" <#if (camTemp.channelSel)! != "2">class="hide"</#if> id="channelCk">
			     <#list application.CodeTable.getCodes("1301") as channel>
			        <span style="margin-left:20px;">
			        	<input type="checkbox" name="chlCd" id="chlCd_${channel_index}" value="${(channel.CodeKey)!}"
			        	<#if (camTemp.chlCdList?? && camTemp.chlCdList?size > 0)>
			        		<#list camTemp.chlCdList as chlCdInfo>
			        			<#if (chlCdInfo.chlCd)! == (channel.CodeKey)! >checked="checked"
			        				<#break>
			        			</#if>
			        		</#list>
			        	</#if>
			        	/>
			        	<label for="chlCd_${channel_index}" style="cursor:pointer;"><span>${(channel.Value)!}</span></label>
			        </span>
			     </#list>
			</p>
			<#-- 首单时间  -->
 		  	<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.firstBuyTime" /></span>
				<span>
				<select style="margin-left:20px;" name="datecp">
					<option <#if (camTemp.datecp)! == "1" >selected="selected"</#if> value="1"><@s.text name="cp.noLimitTxt" /></option>
					<option <#if (camTemp.datecp)! == "2" >selected="selected"</#if> value="2"><@s.text name="cp.datecpTxt" /></option>
				</select> 
				</span>
			</p>
 		  	<p>
 		  	<#-- 会员生肖  -->
 		  	<@s.text id="pageSel1" name="global.page.select"/>
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.zodiacTxt" /></span>
 		  	<span style="margin-left:20px;">
 		  	<@s.select id="zodiacSel" name="zodiac" list='#application.CodeTable.getCodes("1281")' 
					listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{pageSel1}" value="${(camTemp.zodiac)!?html}" />
 		  	</span>
 		  	</p>
				<p>
 		  	<#-- 柜台类别  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.countKbnTxt" /></span>
 		  	<span style="margin-left:20px;">
 		  		<@s.select name="counterKbn" list='#application.CodeTable.getCodes("1223")' 
					listKey="CodeKey" listValue="Value" value='${(camTemp.counterKbn)!}' />
 		  	</span>
 		  	<span style="margin-left:20px;">
	        	<input type="checkbox" name="ctKbn" id="ctKbn" <#if (camTemp.ctKbn)! == "1">checked="checked"</#if> value="1"/>
	        	<label for="ctKbn" style="cursor:pointer;color: rgb(128, 128, 128);">非开卡柜台消费时</span>
	      	</span>
 		  	</p>
				<p>
					<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.actArea" /></span>
					<span style="margin-left:20px;"><@s.select style="margin-left:20px;" list='#application.CodeTable.getCodes("1156")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.choicePlace)!?html}" id="choicePlace" name="choicePlace" onchange="TEMP002.showSearch(this)"/>
					<input type="hidden" name="locationType" id="locationType" value="${(camTemp.locationType)!0}" />
					</span>
					<a onclick="TEMP002.showTree('#choicePlace');return false;" class="search" id="searchTree" <#if (camTemp.choicePlace)! == "0" || (camTemp.choicePlace)! == "" || (camTemp.choicePlace)! == "5">style="display:none;"</#if>>
						<span class="ui-icon icon-search"></span>
						<span class="button-text"><@s.text name="cp.search" /></span>
					</a>
					<span <#if (camTemp.choicePlace)! != "5">class="hide"</#if> id="importDiv">
					<span class="highlight">※</span><@s.text name="cp.useModel" />
					<a href="/Cherry/download/柜台信息模板.xls"><@s.text name="cp.downModel" /></a>
				    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
				    <input type="button" value="<@s.text name="global.page.browse"/>"/>
				    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;return false;" /> 
				    <input type="button" value="<@s.text name="cp.importExcel_btn"/>" onclick="TEMP002.ajaxFileUpload();return false;" id="upload"/>
    				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
					</span>
				</p>
				<div id="treeArea" <#if (camTemp.choicePlace)! == "0" || (camTemp.choicePlace)! == "">class="hide"</#if>>
				<div class="box2-header clearfix" > 
				   <strong id="left_tree" class="left active">
		       			<span class="ui-icon icon-flag"></span>
		       			<@s.text name="cp.choiceCounter" />
		       	   </strong>
		       	</div>
				<div class="jquery_tree">
		    		<div class="jquery_tree treebox_left tree ztree" id = "treeDemo" style="overflow: auto;background:#FFF;height:350px;border:1px solid #CCC">
		     			 
		      		</div>
		    	</div>
		    	<#if (camTemp.nodesList?? && camTemp.nodesList?size > 0)>
			       <script type="text/javascript">
			       		var nodesList = '${(camTemp.nodes)!}';
						TEMP002.loadTree(nodesList);
			      </script>
			    </#if>
			    </div>
			</div>
			<div class="hide">
				<span id="errmsg1"><@s.text name="cp.errorMsg" /></span>
			</div>
		</div>
		
<#-- 活动范围确认画面  -->
<#elseif camTemp.tempCode == "BASE000021">
	<div class="box2">
		<div class="box2-header clearfix">
			<strong class="left"><@s.text name="cp.actLimit" /></strong>
		</div>
		<div class="box2-content clearfix">
			<#if (camTemp.timeSetting)! == "1">
				<p>
              	<#-- 规则执行开始时间  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.startTime" /></span>
                <label style="margin-left:20px;" class="gray">${(campInfo.campaignFromDate)!?html} ${(camTemp.startHH)!?html}:${(camTemp.startMM)!?html}:${(camTemp.startSS)!?html}</label>
              </p>
              <#if (campInfo.campaignToDate)! != "">
              	<p>
              	<#-- 规则执行结束时间  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.endTime" /></span>
                <label style="margin-left:20px;" class="gray">${(campInfo.campaignToDate)!?html} ${(camTemp.endHH)!?html}:${(camTemp.endMM)!?html}:${(camTemp.endSS)!?html}</label>
              </p>
              </#if>
			</#if>
			 <p>
             	<#-- 规则属性  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointRuleKbnTxt"/></span>
                <label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1172", "${(camTemp.pointRuleKbn)!?html}")}</label>
              </p>
             <#if (campInfo.templateType)! != "">
              <p>
              	<#-- 规则模板  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointType"/></span>
                <label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1165", "${(campInfo.templateType)!?html}")}</label>
              </p>
             </#if>
             <#if (camTemp.pointRuleKbn)! == "2">
              <p>
              	<#-- 附属方式  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.subRuleKbnTxt"/></span>
                <label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1182", "${(camTemp.subRuleKbn)!?html}")}</label>
              </p>
             </#if>
             <p>
             	<#-- 积分类型  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointKbn"/></span>
                <label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1183", "${(camTemp.pointType)!?html}")}</label>
              </p>
			<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.actObj" /></span>
				<#if (camTemp.memberLevelList?? && camTemp.memberLevelList?size > 0)>
			        <#list camTemp.memberLevelList as memberInfo>
			        	<#if "1" == (memberInfo.memberShowFlag)!>
			        		<label style="margin-left:20px;" class="gray">${(memberInfo.levelName)!?html}</label>
			        	</#if>
			        </#list>
		    	<#else>
		    		<label style="margin-left:20px;" class="gray"><@s.text name="cp.allPeo" /></label>
	        	</#if>
	        	<#if (camTemp.referKbn)! == "1">
	        	<label style="margin-left:20px;" class="gray">
	        	(<@s.text name="cp.referTxt" />)
	        	</label>
	        	</#if>
	        	<#if (camTemp.refPTKbn)! == "1">
	        	<label style="margin-left:20px;" class="gray">
	        	(<@s.text name="cp.refPTKbnTxt" />)
	        	</label>
	        	</#if>
	        </p>
	        <p>
 		  	<#-- 会员积分  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.memberPoint" /></span>
            <label style="margin-left:20px;" class="gray">
           	<#if (camTemp.mPoint)! == "2" >
            	<@s.text name="cp.mempointRange" />：${(camTemp.minpt)!} - ${(camTemp.maxpt)!} 
            <#else>
            	<@s.text name="cp.noLimitTxt" />
            </#if>
            </label>
 		  	</p>
	        <p>
 		  	<#-- 入会日期  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.jnDateCondTxt" /></span>
            <label style="margin-left:20px;" class="gray">
           	<#if (camTemp.jnDate)! != "" >
            ${application.CodeTable.getVal("1264", "${(camTemp.jnDate)!?html}")}
            <#else>
            	<@s.text name="cp.noLimitTxt" />
            </#if>
            </label>
 		  	</p>
 		  	<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.channelTxt" /></span>
				<#if (camTemp.channelSel)! == "2" >
			       <#if (camTemp.chlCdList?? && camTemp.chlCdList?size > 0)>
			       		<#list camTemp.chlCdList as chlCdInfo>
			       			<#list application.CodeTable.getCodes("1301") as channel>
				        	<#if (chlCdInfo.chlCd)! == (channel.CodeKey)! >
				        		<label style="margin-left:20px;" class="gray">${(channel.Value)!?html}</label>
				        	</#if>
				        	</#list>
			        	</#list>
			       </#if>
		    	<#else>
		    		<label style="margin-left:20px;" class="gray"><@s.text name="cp.allValue" /></label>
	        	</#if>
	        </p>
	     <#-- 首单时间  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.firstBuyTime" /></span>
            <label style="margin-left:20px;" class="gray">
           	<#if (camTemp.datecp)! == "2" >
           		 <@s.text name="cp.datecpTxt" />
            <#else>
            	<@s.text name="cp.noLimitTxt" />
            </#if>
            </label>
 		  	</p>
 		  	 <p>
 		  	<#-- 会员生肖  -->
 		  	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.zodiacTxt" /></span>
            <label style="margin-left:20px;" class="gray">
           	<#if (camTemp.zodiac)! != "" >
            ${application.CodeTable.getVal("1281", "${(camTemp.zodiac)!?html}")}
            <#else>
            	<@s.text name="cp.noLimitTxt" />
            </#if>
            </label>
 		  	</p>
	         <p>
             	<#-- 柜台类别  -->
                <span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.countKbnTxt" /></span>
                <label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1223", '${(camTemp.counterKbn)!"1"}')}</label>
                <#if (camTemp.ctKbn)! == "1">
	        	<label style="margin-left:20px;" class="gray">
	        	(非开卡柜台消费时)
	        	</label>
	        	</#if>
              </p>
	        <p>
	        	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.actPlace" /></span>
				<#if ((camTemp.choicePlace)! != "0" && camTemp.checkNodes?? && camTemp.checkNodes?size > 0)>
			        <#list camTemp.checkNodes as actcounter>
			        	<label style="margin-left:20px;" class="gray">${(actcounter.name)!?html}</label>
			        </#list>
			    <#else>
			    	<label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1156","${(camTemp.choicePlace)!?html}")}</label>
		        </#if>
		    </p>
		</div>
	</div>
		
<#-- 计算设置（四舍五入，上限）  -->
	<#elseif camTemp.tempCode == "BASE000017">
	<div class="box4" id="${camTemp.tempCode}_${index}">
		<div class="box4-header">
			<strong><@s.text name="cp.comSet" /></strong>
		</div>
		<div class="box4-content">
		    <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
		    <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		    <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	    	
	    	<div class="box2">
	    		<div class="box2-header clearfix" >
	    			<strong class="left"><@s.text name="cp.limit" /></strong>
	    		</div>
	    		<div class="box2-content" >
			    	<p style="margin-left:20px;" >
						<span class="bg_title"><input type="checkbox" id="oneLimit" name="oneLimit" onclick="TEMP002.showLimitDiv(this);" value="1" <#if (camTemp.oneLimit)! == "1">checked="checked"</#if>/>
						<label for="oneLimit" style="cursor:pointer;"><@s.text name="cp.oneLimit" /></label></span>
					</p>
					<div id="oneLimitDiv" <#if (camTemp.oneLimit)! == "">class="hide"</#if>>
					<p style="margin-left:40px">
						<input type="radio"  class="radio"  value="2" <#if "" == (camTemp.onePoint)! || (camTemp.onePoint)! == "2">checked="checked"</#if> id="oneLimitMark" name="onePoint"/>
						<label for="oneLimitMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="maxPoint" value="${(camTemp.maxPoint)!?html}"/>
						</span>
						<span><label><@s.text name="cp.passPoint1" /></label>
						</span>
					</p>
					<p style="margin-left:40px">
						<input type="radio"  class="radio"  value="3" <#if (camTemp.onePoint)! == "3">checked="checked"</#if> id="oneLimitGiveMark" name="onePoint"/>
						<label for="oneLimitGiveMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="maxGivePoint" value="${(camTemp.maxGivePoint)!?html}"/>
							</span><span><label><@s.text name="cp.passMoneyGive" /></label>
						</span>
						<span>
							<input type="text" class="number" name="mulGive" value="${(camTemp.mulGive)!?html}" />
							</span><span><label><@s.text name="cp.mulPoint" /></label>
						</span>
					</p>
					</div>
					<div class="line_Yellow">
					</div>
					<p style="margin-left:20px;">
						<span class="bg_title"><input type="checkbox" id="allLimit" name="allLimit" onclick="TEMP002.showLimitDiv(this);" value="1" <#if (camTemp.allLimit)! == "1">checked="checked"</#if> />
						<label for="allLimit" style="cursor:pointer;"><@s.text name="cp.dayLimit" /></label></span>
					</p>
					<div id="allLimitDiv" <#if (camTemp.allLimit)! == "">class="hide"</#if>>
					<p style="margin-left:40px">
						<input type="radio" class="radio" value="2" id="allLimitMark" name="allPoint" <#if "" == (camTemp.allPoint)! || (camTemp.allPoint)! == "2">checked="checked"</#if>/>
						<label for="allLimitMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="allLimitPoint" value="${(camTemp.allLimitPoint)!?html}"/>
							</span><span><label><@s.text name="cp.passPoint1" /></label>
						</span>
					</p>
					<p style="margin-left:40px">
						<input type="radio"  class="radio"  value="3" <#if (camTemp.allPoint)! == "3">checked="checked"</#if> id="allLimitGiveMark" name="allPoint"/>
						<label for="allLimitGiveMark"><@s.text name="cp.giveMonPass" /></label>
						<span>
							<input type="text" class="number" name="maxAllGivePoint" value="${(camTemp.maxAllGivePoint)!?html}"/>
							</span><span><label><@s.text name="cp.passMoneyGive" /></label>
						</span>
						<span>
							<input type="text" class="number" name="mulAllGive" value="${(camTemp.mulAllGive)!?html}" />
							</span><span><label><@s.text name="cp.mulPoint" /></label>
						</span>
					</p>
					</div>
					<div class="line_Yellow">
					</div>
					<p style="margin-left:20px;">
						<span class="bg_title"><input type="checkbox" id="actLimit" name="actLimit" onclick="TEMP002.showLimitDiv(this);" value="1" <#if (camTemp.actLimit)! == "1">checked="checked"</#if> />
						<label for="actLimit" style="cursor:pointer;"><@s.text name="cp.actDateLimit" /></label></span>
					</p>
					<div id="actLimitDiv" <#if (camTemp.actLimit)! == "">class="hide"</#if>>
					<p style="margin-left:40px">
						<input type="radio" class="radio" value="2" id="actLimitMark1" name="actPoint" <#if "" == (camTemp.actPoint)! || (camTemp.actPoint)! == "2">checked="checked"</#if>/>
						<label for="actLimitMark1"><@s.text name="cp.actTimePlusPoint" /></label>
						<span>
							<input type="text" class="number" name="actLimitPoint" value="${(camTemp.actLimitPoint)!?html}"/>
							</span><span><@s.text name="cp.passPoint1" />
						</span>
					</p>
					<p style="margin-left:40px">
						<input type="radio" class="radio" value="3" id="actLimitMark" name="actPoint" <#if (camTemp.actPoint)! == "3">checked="checked"</#if>/>
						<label for="actLimitMark"><@s.text name="cp.actTimePlusPoint" /></label>
						<span>
							<input type="text" class="number" name="actLimitGivePoint" value="${(camTemp.actLimitGivePoint)!?html}"/>
							</span><span><label><@s.text name="cp.passMoneyGive" /></label>
						</span>
						<span>
							<input type="text" class="number" name="actGivePoint" value="${(camTemp.actGivePoint)!?html}"/>
							</span><span><label><@s.text name="cp.mulPoint" /></label>
						</span>
					</p>
					</div>
	    		</div>
	    	</div>
		</div>
	</div>
	
<#-- 计算设置确认画面  -->
<#elseif camTemp.tempCode == "BASE000022">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.comSet" /></strong>
	</div>
	<div class="box2-content">
		<#if (camTemp.oneLimit)! == "1">
			<#if (camTemp.onePoint)! == "2">
				<p>
					<label><@s.text name="cp.oneUsePoint" /></label>
					<label style="color:red;">${(camTemp.maxPoint)!?html}</label>
					<label><@s.text name="cp.passPoint1" /></label>
				</p>
			<#elseif (camTemp.onePoint)! == "3">
				<p>
					<label><@s.text name="cp.oneUsePoint" /></label>
					<label style="color:red;">${(camTemp.maxGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoneyGive" /></label>
					<label style="color:red;">${(camTemp.mulGive)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
		<#if (camTemp.allLimit)! == "1">
			<#if (camTemp.allPoint)! == "2">
				<p>
					<label><@s.text name="cp.allUsePoint" /></label>
					<label style="color:red;">${(camTemp.allLimitPoint)!?html}</label>
					<label><@s.text name="cp.passPoint1" /></label>
				</p>
			<#elseif (camTemp.allPoint)! == "3">
				<p>
					<label><@s.text name="cp.allUsePoint" /></label>
					<label style="color:red;">${(camTemp.maxAllGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoneyGive" /></label>
					<label style="color:red;">${(camTemp.mulAllGive)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
		<#if (camTemp.actLimit)! == "1">
			<#if (camTemp.actPoint)! == "2">
				<p>
					<label><@s.text name="cp.actTimePlusPoint" /></label>
					<label style="color:red;">${(camTemp.actLimitPoint)!?html}</label>
					<label><@s.text name="cp.passPoint1" /></label>
				</p>
			<#elseif (camTemp.actPoint)! == "3">
				<p>
					<label><@s.text name="cp.actTimePlusPoint" /></label>
					<label style="color:red;">${(camTemp.actLimitGivePoint)!?html}</label>
					<label><@s.text name="cp.passMoneyGive" /></label>
					<label style="color:red;">${(camTemp.actGivePoint)!?html}</label>
					<label><@s.text name="cp.mulComPoint" /></label>
				</p>
			</#if>
		</#if>
	</div>
	</div>

<#-- 会员入会积分  -->
	<#elseif camTemp.tempCode == "BUS000018">
	<div class="box4" id="${camTemp.tempCode}_${index}">
		<div class="box4-header">
			<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.memberJoin"/></strong>
			<a class="add right" onClick="CAMPAIGN_TEMPLATE.addRule(this,'${camTemp.tempCode}_${index}', 'box4');">
				<span class="ui-icon icon-add"></span>
				<span class="button-text"><@s.text name="cp.addRule"/></span></a>
		</div>
		<div class="box4-content">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
			<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
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
	
<#-- 入会赠与积分  -->
	<#elseif camTemp.tempCode == "BUS000036">
		<div id="${camTemp.tempCode}_${index}" class="box4-content-content">
		    <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
		    <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		    <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
		    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
		    <div class="toolbar clearfix">
		    	<a class="add right deleteClass" name="deleteJon" onclick="CAMPAIGN_TEMPLATE.deleteRule(this,'box4-content-content', 'box4-content')">
					<span class="ui-icon icon-delete"></span>
					<span class="button-text"><@s.text name="cp.delete" /></span>
				</a>
		    </div>
		    <table class="detail" >
		    	<tr> 
		    		<th><@s.text name="cp.joinLevel" /></th>
		    		<td>
		    			<#if (camTemp.memberLevelList?? && camTemp.memberLevelList?size > 0)>
			       			<span>
			       				<select name="memberLevelId" id="memberLevelId_${index}">
			       					<option <#if (camTemp.memberLevelId)! == "0" >selected="selected"</#if>  value="0" >
					            		<@s.text name="cp.anyLevelTxt" />
					            	</option>
					            	<#list camTemp.memberLevelList as memberInfo>
					            		<option <#if (camTemp.memberLevelId)! == memberInfo.memberLevelId.toString() >selected="selected"</#if>  value="${memberInfo.memberLevelId.toString()}" >
					            		${(memberInfo.levelName)!?html}
					            		</option>
					            	</#list>
			            		</select>
			            	</span>
			       		 </#if>
	       		 	</td>
	       		 	<th><@s.text name="cp.getPointTime" /></th>
	       		 	<td>
	       		 		<@s.select list='#application.CodeTable.getCodes("1157")' listKey="CodeKey" listValue="Value" 
							onchange="TEMP002.showPanel(this,'jonDatePanel');" value="${(camTemp.jonDate)!?html}"  name="jonDate"/><br/>
						<span <#if "2" != (camTemp.jonDate)!>class="hide"</#if> id="jonDatePanel2">
						<#-- 入会后  -->
							<span><label><@s.text name="cp.aftJoinTxt" /></label>
							<input type="text" class="input"  style="width:30px;height:15px;" name="jonDateLimit" value="${(camTemp.jonDateLimit)!?html}"/>
							<#-- 天  -->
							<label><@s.text name="cp.day" /></label></span>
							<br/>
							<#-- 包含入会当天  -->
							<span><input type="checkbox" name="theDay" id="theDay_${index}" <#if "1" == (camTemp.theDay)!>checked="checked"</#if> value="1"/><label for="theDay_${index}"><@s.text name="cp.contJoinDayTxt" /></label></span>
						</span>
						<span <#if "3" != (camTemp.jonDate)!>class="hide"</#if> id="jonDatePanel3">
						<#-- 入会后  -->
							<span><label><@s.text name="cp.aftJoinTxt" /></label>
							<input type="text" class="input"  style="width:30px;height:15px;" name="jonMonthLimit" value="${(camTemp.jonMonthLimit)!?html}"/>
							<#-- 月  -->
							<label><@s.text name="cp.monthNum" /></label></span>
							<br/>
							<#-- 截止到月末 -->
							<span><input type="checkbox" name="monthEnd" id="monthEnd_${index}" <#if "1" == (camTemp.monthEnd)!>checked="checked"</#if> value="1"/><label for="monthEnd_${index}"><@s.text name="cp.monthEndTxt" /></label></span>
							<br/>
							<#-- 包含入会当天  -->
							<span><input type="checkbox" name="theMonth" id="theDayMonth_${index}" <#if "1" == (camTemp.theMonth)!>checked="checked"</#if> value="1"/><label for="theDayMonth_${index}"><@s.text name="cp.contJoinDayTxt" /></label></span>
						</span>
	       		 	</td>
		    	</tr>
		    	<tr>
	    		<th></th>
	    		<td>
	    		</td>
	    		<#-- 选择单次  -->
	    		<th><@s.text name="cp.selBillTxt" /></th>
	    		<td>
	    			<@s.select list='#application.CodeTable.getCodes("1222")' listKey="CodeKey" listValue="Value" 
							onchange="TEMP002.showPanel(this,'billPanel');" value="${(camTemp.firstBillSel)!?html}"  name="firstBillSel"/>
					<br/>
	    			<span <#if "3" != (camTemp.firstBillSel)!>class="hide"</#if> id="billPanel3">
	    					<#-- 第  -->
							<label><@s.text name="cp.theTxt" /></label>
							<input type="text" class="input" style="width:30px;height:15px;" name="billTime" value="${(camTemp.billTime)!}"/>
							<#-- 单  -->
							<label><@s.text name="cp.billNoTxt" /></label>
						</span>
	    		</td>
	    	</tr>
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
	
<#-- 会员入会积分确认画面  -->
	<#elseif camTemp.tempCode == "BUS000027">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
        <#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
	        <#if (combTemp_index lt camTemp.combTemps?size - 1) >
				<div class="line_Yellow">
				</div>
			</#if>
		</#list>
	</div>
	</div>
	
<#-- 入会赠与积分(确认画面)  -->
	<#elseif camTemp.tempCode == "BUS000037">
		<p>
			<label><@s.text name="cp.joinLevelTo" /></label>
			<label class="gray">${(camTemp.levelName)!?html}</label>
		</p>
		<p>
			<label><@s.text name="cp.getPointTime" /></label>
			
				<#if "2" == (camTemp.jonDate)!>
				<#-- 入会后  -->
				<label class="gray"><@s.text name="cp.aftJoinTxt" /></label>
				<label style="color:red;">${(camTemp.jonDateLimit)!?html}</label>
				 <#if (camTemp.jonDateLimit??)>
				<#-- 天  -->
				<label class="gray"><@s.text name="cp.day" /></label>
				</#if>
				<#-- 包含入会当天  -->
				<#if "1" == (camTemp.theDay)!><label class="gray"><@s.text name="cp.contJoinDayTxt" /></label></#if>
				<#elseif "3" == (camTemp.jonDate)!>
				<#-- 入会后  -->
				<label class="gray"><@s.text name="cp.aftJoinTxt" /></label>
					<label style="color:red;">${(camTemp.jonMonthLimit)!?html}</label>
					<#-- 月  -->
					<label class="gray"><@s.text name="cp.monthNum" />
					<#-- 截止到月末 -->
					<#if "1" == (camTemp.monthEnd)!><@s.text name="cp.monthEndTxt" /></#if> 
					<#-- 包含入会当天  -->
					<#if "1" == (camTemp.theMonth)!><@s.text name="cp.contJoinDayTxt" /></#if>
					</label>
				<#else>
					<label class="gray">${application.CodeTable.getVal("1157", "${(camTemp.jonDate)!?html}")}</label>
				</#if>
			
		</p>
		<p>
			<#-- 选择单次  -->
			<label><@s.text name="cp.selBillTxt" /></label>
			<label class="gray">${application.CodeTable.getVal("1222", "${(camTemp.firstBillSel)!}")}</label>
			<#if "3" == (camTemp.firstBillSel)!>
				<#-- 第  -->
				<label class="gray"><@s.text name="cp.theTxt" /></label>
				<label style="color:red;">${(camTemp.billTime)!}</label>
				<#-- 单  -->
				<label class="gray"><@s.text name="cp.billNoTxt" /></label>
			</#if>
			
		</p>
		<#list camTemp.combTemps as combTemp>
	          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
		</#list>
	
<#-- 首单购买  -->
	<#elseif camTemp.tempCode == "BUS000019">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.firstBuy" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
        <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
        <input type="hidden" name="selPrtIndex" value="${camTemp.tempCode}_${index}"/>
        <table class="detail" >
        	<tr>	
        		<th><@s.text name="cp.firstTime" /></th>
        		<td>
        			<p><@s.select list='#application.CodeTable.getCodes("1158")' listKey="CodeKey" listValue="Value" 
						onchange="TEMP002.showDiv(this);" value="${(camTemp.firstBillDate)!?html}"  name="firstBillDate"/></p>
					<p>
					<span <#if "4" != (camTemp.firstBillDate)!> class="hide" </#if> id="appointDate">
						<input type="text" class="date" id="campFromDate_${camTemp.tempCode}_${index}" name="billStartTime" value="${(camTemp.billStartTime)!?html}"/>
						<@s.text name="cp.to" />
						<input type="text" class="date" id="campToDate_${camTemp.tempCode}_${index}" name="billEndTime" value="${(camTemp.billEndTime)!?html}"/>
					</span>
					<span <#if "2" != (camTemp.firstBillDate)!> class="hide" </#if> id="appointMonth">
						<#if (camTemp.monthList?? && camTemp.monthList?size > 0)>
			       			<select name="billMonth" style="width:60px;">
			            	<#list camTemp.monthList as monthInfo>
			            		<option <#if (camTemp.billMonth)! == monthInfo.monthValue.toString() >selected="selected"</#if>  value="${monthInfo.monthValue.toString()}" >
			            		${(monthInfo.monthLabel)!?html}
			            		</option>
			            	</#list>
			            	</select>
			        	</#if>
			        	<@s.text name="cp.month" />
		        	</span>
					<span <#if "3" != (camTemp.firstBillDate)!> class="hide" </#if> id="appointDay">
						<span>
							<input type="text" class="input"  style="width:30px;height:15px;" name="billStartMonth" value="${(camTemp.billStartMonth)!?html}"/>
							<@s.text name="cp.month" />
						</span>
						<span>
							<input type="text"  class="input" style="width:30px;height:15px;" name="billStartDay" value="${(camTemp.billStartDay)!?html}"/>
							<@s.text name="cp.dayTime" />
						</span>
					</span>
					<span <#if "6" != (camTemp.firstBillDate)!> class="hide" </#if> id="bindDaySpan">
						<#-- 绑定后  -->
							<span><label><@s.text name="cp.bindAfter" /></label>
							<input type="text" class="input"  style="width:30px;height:15px;" name="bindDayLimit" value="${(camTemp.bindDayLimit)!?html}"/>
							<#-- 天  -->
							<label><@s.text name="cp.day" /></label></span>
							<br/>
							<#-- 包含绑定当天  -->
							<span><input type="checkbox" name="bindDay" id="bindDay_${index}" <#if "1" == (camTemp.bindDay)!>checked="checked"</#if> value="1"/><label for="bindDay_${index}"><@s.text name="cp.isbindDay" /></label></span>
					</span>
					<span <#if "7" != (camTemp.firstBillDate)!> class="hide" </#if> id="saleDaySpan">
						<input type="text" class="date" id="saleFromDate_${camTemp.tempCode}_${index}" name="saleStartTime" value="${(camTemp.saleStartTime)!?html}"/>
						<@s.text name="cp.to" />
						<input type="text" class="date" id="saleToDate_${camTemp.tempCode}_${index}" name="saleEndTime" value="${(camTemp.saleEndTime)!?html}"/>
					</span>
					</p>
        		</td>
        		<#-- 选择单次  -->
	        	<th><@s.text name="cp.selBillTxt" /></th>
	    		<td>
	    			
					<#if "5" == (camTemp.firstBillDate)!>		
					<select name="firstBillSel" disabled="true">
		            <#list application.CodeTable.getCodes("1222") as fbInfo>
		            	<option <#if (camTemp.firstBillSel)! == fbInfo.CodeKey.toString() >selected="selected"</#if>  value="${fbInfo.CodeKey.toString()}" >
		            	${(fbInfo.Value)!?html}
		            	</option>
		            </#list>
		            </select>
					 <input type="hidden" name="firstBillSel" value="0" /> 
					 <#else>
					 <@s.select list='#application.CodeTable.getCodes("1222")' listKey="CodeKey" listValue="Value" 
							onchange="TEMP002.showPanel(this,'billPanel');" value="${(camTemp.firstBillSel)!?html}"  name="firstBillSel" />
					 </#if>
					<br/>
	    			<span <#if "3" != (camTemp.firstBillSel)!>class="hide"</#if> id="billPanel3">
	    					<#-- 第  -->
							<label><@s.text name="cp.theTxt" /></label>
							<input type="text" class="input" style="width:30px;height:15px;" name="billTime" value="${(camTemp.billTime)!}"/>
							<#-- 单  -->
							<label><@s.text name="cp.billNoTxt" /></label>
						</span>
	    		</td>
        	</tr>
			<tr id="saleProductSpecify"  <#if "7" != (camTemp.firstBillDate)!> class="hide" </#if> >
				<th><@s.text name="cp.yanqiPrtSelTxt" /></th>
				<td>
					<p>
						<span>
									<a class="add left " onClick="TEMP002.showTableForSale(this);" id="prtselbtn">
										<span class="ui-icon icon-add"></span>
										<span class="button-text"><@s.text name="cp.prtSel" /></span>
									</a>
									&nbsp;&nbsp;<@s.text name="cp.prtCondTxt" />
									<@s.select list='#application.CodeTable.getCodes("1287")' listKey="CodeKey" listValue="Value"
									value="${(camTemp.proCond)!?html}"  name="proCond"/>
									</span>

					</p>

				<p>
				<span id="prtSel_${camTemp.tempCode}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
						<#if (camTemp.productList?? && camTemp.productList?size > 0) >
							<#list camTemp.productList as productList>
								<span class="span_BASE000001">
									<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
									<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
									<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
									<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
									<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span>
									<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
											<span class="ui-icon ui-icon-close"></span></span>
										</span>
							</#list>
						</#if>
					</span>
						</p>
				</td>
				<th></th>
				<td></td>
			</tr>
        	<#list camTemp.combTemps as combTemp>
		        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
        </table>
	</div>
</div>

<#-- 首单购买确认画面  -->
	<#elseif camTemp.tempCode == "BUS000028">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<p><label><@s.text name="cp.firstTime" /></label>
		<label class="gray">
			<#if "0" == (camTemp.firstBillDate)! || "1" == (camTemp.firstBillDate)! || "5" == (camTemp.firstBillDate)!>
				${application.CodeTable.getVal("1158", "${(camTemp.firstBillDate)!?html}")}
				<#-- 月  -->
			<#elseif "2" == (camTemp.firstBillDate)!>${(camTemp.billMonth)!?html}<@s.text name="cp.month" />
			<#elseif "4" == (camTemp.firstBillDate)!>${(camTemp.billStartTime)!?html}<@s.text name="cp.to" />${(camTemp.billEndTime)!?html}
			<#-- 日  -->
			<#elseif "3" == (camTemp.firstBillDate)!>${(camTemp.billStartMonth)!?html}<@s.text name="cp.month" />${(camTemp.billStartDay)!?html}<@s.text name="cp.dayTime" />
			<#elseif "6" == (camTemp.firstBillDate)!><@s.text name="cp.bindAfter" /> ${(camTemp.bindDayLimit)!?html} <@s.text name="cp.day" />
			<#if "1" == (camTemp.bindDay)!> (<@s.text name="cp.isbindDay" />) </#if>
			<#elseif "7" == (camTemp.firstBillDate)!>${(camTemp.saleStartTime)!?html}<@s.text name="cp.to" />${(camTemp.saleEndTime)!?html}
			</#if>
		</label>
		</p>
		<p>
			<#-- 选择单次  -->
			<label><@s.text name="cp.selBillTxt" /></label>
			<label class="gray">${application.CodeTable.getVal("1222", "${(camTemp.firstBillSel)!}")}</label>
			<#if "3" == (camTemp.firstBillSel)!>
				<#-- 第  -->
				<label class="gray"><@s.text name="cp.theTxt" /></label>
				<label style="color:red;">${(camTemp.billTime)!}</label>
				<#-- 单  -->
				<label class="gray"><@s.text name="cp.billNoTxt" /></label>
			</#if>
			
		</p>
		<#if "7" == (camTemp.firstBillDate)!>
            <p>
			<#-- 特定产品  -->
                <label><@s.text name="cp.SpecPrt" /></label>
				<#if (camTemp.productList?? && camTemp.productList?size > 0) >
					<#list camTemp.productList as productList>
                        <span class="span_BASE000001">
		          			<span class="bg_title">
		          			${(productList.nameTotal)!?html}
		          			</span>
		          		</span>
					</#list>
				</#if>
            </p>
            <p>
                <label><@s.text name="cp.productCondTxt" /></label>
                <label class="gray">${application.CodeTable.getVal("1287", '${(camTemp.proCond)!"0"}')}</label>
            </p>
		</#if>

        <#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		</#list>
	</div>
	</div>
	
<#-- 默认规则  -->
	<#elseif camTemp.tempCode == "BUS000020">
	<div class="box4" id="${camTemp.tempCode}_${index}">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.normalPointTxt" /></strong>
	</div>
	<div class="box4-content">
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
	
<#-- 单次购买赠与积分  -->
	<#elseif camTemp.tempCode == "BUS000038">
		<div id="${camTemp.tempCode}_${index}" class="box4-content-content">
	    <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	    <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
	    <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	    <table class="detail" >
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
		
<#-- 单次购买确认画面  -->
	<#elseif camTemp.tempCode == "BUS000029">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/> 
	        <#if (combTemp_index lt camTemp.combTemps?size - 1) >
				<div class="line_Yellow">
				</div>
			</#if>
		</#list>
	</div>
	</div>
	
<#-- 默认规则赠与积分  -->
	<#elseif camTemp.tempCode == "BASE000063">
	<div class="box4" id="${camTemp.tempCode}_${index}">
		<div class="hide" id="defcom_param">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
	        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
		    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
		    <input type="hidden" name="multFlag" value='${(camTemp.multFlag)!}' id="multFlag"/>
	   	</div>
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.defaultRule" /></strong>
		<span <#if (camTemp.multFlag)! == "1" >class='hide'</#if> id="defbtn_0"><a class='add right' name="addJon" onClick="TEMP002.changeDefRule('1')"><span class="ui-icon icon-down"></span><span class="button-text">分等级设置</span></a></span>
		<span <#if (camTemp.multFlag)! != "1" >class='hide'</#if> id="defbtn_1"><a class='add right' name="addJon" onClick="TEMP002.changeDefRule('0')"><span class="ui-icon icon-export"></span><span class="button-text">默认方式</span></a></span>
	</div>
	<div class='box4-content <#if (camTemp.multFlag)! == "1" >hide</#if>' id="deft_cont_0">
		<table class="detail" >
			<tr class="giveClass">
		    <th><@s.text name="cp.defMultipleTxt" />
			</th>
		    <td>
		    	<span>
					<input type="text" style="width: 30px; height: 15px; border: 1px solid rgb(204, 204, 204);" name="pointMultiple" value="${(camTemp.pointMultiple)!?html}"> 倍
				</span>
			</td>		
		</tr>
		 </table>
	</div>
	<div class='box4-content <#if (camTemp.multFlag)! != "1" >hide</#if>' id="deft_cont_1">
	<#if (camTemp.memLevelList?? && camTemp.memLevelList?size > 0) >
	<#list camTemp.memLevelList as memberLevelInfo>
	<#if memberLevelInfo_index != 0>
		<div class="line_Yellow">
		</div>
	</#if>
	 <table class="detail" >
		<tr class="giveClass">
			<th>会员等级
			</th>
		    <td>
		    	<span>
					${(memberLevelInfo.levelName)!}
					<input type="hidden" name="memberLevelId" value="${(memberLevelInfo.memberLevelId)!?html}"/>
					<input type="hidden" name="levelName" value="${(memberLevelInfo.levelName)!?html}"/>
				</span>
			</td>	
		    <th><@s.text name="cp.defMultipleTxt" />
			</th>
		    <td>
		    	<span>
					<input type="text" style="width: 30px; height: 15px; border: 1px solid rgb(204, 204, 204);" name="ptmlt" value="${(memberLevelInfo.ptmlt)!?html}"> 倍
				</span>
			</td>		
		</tr>
		 </table>
		 </#list>
		</#if>
		</div>
	</div>
	
	<#-- 默认规则确认画面  -->
	<#elseif camTemp.tempCode == "BASE000064">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
	<#if (camTemp.multFlag)! != "1" >
		<p>
		    <span>
		    	<label><@s.text name="cp.defMultipleTxt" /></label>
		    	<label style="color:red;">${(camTemp.pointMultiple)!?html}</label>
		    	<label><@s.text name="cp.mul" /></label>
		    </span>
		   </p>
	<#else>
		<#if (camTemp.memLevelList?? && camTemp.memLevelList?size > 0) >
		<#list camTemp.memLevelList as memberLevelInfo>
			<p>
		    <span>
		    	<label>${(memberLevelInfo.levelName)!}：</label>
		    	<label><@s.text name="cp.defMultipleTxt" /></label>
		    	<label style="color:red;">${(memberLevelInfo.ptmlt)!?html}</label>
		    	<label><@s.text name="cp.mul" /></label>
		    </span>
		   </p>
		</#list>
		</#if>
	</#if>
	</div>
	</div>
		
<#-- 单次购买赠与积分(确认画面)  -->
	<#elseif camTemp.tempCode == "BUS000039">
		<#if (camTemp.minMoney)! != "">
			<p>
				<label><@s.text name="cp.defaultRule" /></label>
				<#if (camTemp.minMoney)! != "">
					<label><@s.text name="cp.pass" /></label>
					<label style="color:red;">${(camTemp.minMoney)!?html}</label>
					<label><@s.text name="cp.yuan" /></label>
				</#if>
				<#if (camTemp.maxMoney)! != "">
					<label><@s.text name="cp.less" /></label>
					<label style="color:red;">${(camTemp.maxMoney)!?html}</label>
					<label><@s.text name="cp.yuan" /></label>
				</#if>
			</p>
		</#if>
		<#list camTemp.combTemps as combTemp>
	          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
		</#list>
	
<#-- 购买产品  -->
	<#elseif camTemp.tempCode == "BUS000021">
	<div class="box4" id="${camTemp.tempCode}_${index}">
		<div class="box4-header">
			<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.buyPro" /></strong>
		</div>
		<div class="box4-content">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        	<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	        <input type="hidden" name="selPrtIndex" value="${camTemp.tempCode}_${index}"/>
	        <table class="detail" >
	        	<tr>
	        		<th><@s.text name="cp.SpecPrt" /></th>
	        		<td>
	        			<p><span><@s.select id="product" list='#application.CodeTable.getCodes("1164")' listKey="CodeKey" listValue="Value" 
							onchange="TEMP002.showDate(this,0,0);"  value="${(camTemp.product)!?html}"  name="product"/>
						</span>
						<span>
						<a class="add left " onClick="TEMP002.showTable(this);" id="prtselbtn">
							<span class="ui-icon icon-add"></span>
							<span class="button-text"><@s.text name="cp.prtSel" /></span>
						</a>
						&nbsp;&nbsp;<@s.text name="cp.prtCondTxt" />
						<@s.select list='#application.CodeTable.getCodes("1287")' listKey="CodeKey" listValue="Value" 
							value="${(camTemp.proCond)!?html}"  name="proCond"/>
						</span>
					<span class='highlight <#if (camTemp.product)! != "5">hide</#if>'>
					<a href="/Cherry/download/特定商品模板.xls"><@s.text name="cp.downModel" /></a><br/>
				    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
				    <input type="button" value="<@s.text name="global.page.browse"/>"/>
				    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;return false;" /> 
				    <input type="button" value="<@s.text name="cp.importExcel_btn"/>" onclick="TEMP002.prtFileUpload();return false;" id="upload"/>
    				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
					</span>
						</p>
						
						<p>
						<span id="prtSel_${camTemp.tempCode}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
						<#if (camTemp.product)! != "5">
			          	<#if (camTemp.productList?? && camTemp.productList?size > 0) >
			          	<#list camTemp.productList as productList>
			          		<span class="span_BASE000001">
			          			<#if (camTemp.product)! == "1">
			          				<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
			          				<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
			          				<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
			          				<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
			          				<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span>
		          				<#elseif (camTemp.product)! == "2" || (camTemp.product)! == "3">
			          				<input type="hidden" name="cateId" value="${(productList.cateId)!?html}" />
			          				<input type="hidden" name="cateName" value="${(productList.cateName)!?html}" />
			          				<input type="hidden" name="cateFlag" value="${(productList.cateFlag)!?html}" />
			          				<span style="margin:0px 5px;">${(productList.cateName)!?html}</span> 
			          			<#elseif (camTemp.product)! == "4">
			          				<input type="hidden" name="prmId" value="${(productList.prmId)!?html}" />
			          				<input type="hidden" name="prmNameTotal" value="${(productList.prmNameTotal)!?html}" />
			          				<span style="margin:0px 5px;">${(productList.prmNameTotal)!?html}</span>
		          				</#if>
		          				<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
		          				<span class="ui-icon ui-icon-close"></span></span>
			          		</span>
		   				</#list>
			          	</#if>
			          	</#if>
			         	</span>
			         	</p>
			         	<p id="prtImptPanel" <#if (camTemp.product)! != "5"> class="hide" </#if>>
			         	<label class="gray"><@s.text name="cp.imptPrtLimit" /></label>
			         	<br/>
			         	<a href="javascript:void(0)" onclick="javascript:TEMP002.showImptRst();return false;"><@s.text name="cp.imptPrtlist" /></a>
			         	<div id="prtImptDialog"></div>
			         	<div id="dialogInitMessage" class="hide" style="overflow-y:auto">
			         	<p><@s.text name="cp.imptPrtNum" />&nbsp;&nbsp;<label style="color:red;" id="imptPrtNum">
			         	<strong>
			         	${(camTemp.productList?size)!0}
			         	</strong></label></p>
			         		<table id="prtImptTable" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th><@s.text name="cp.proName" /></th>
						<th><@s.text name="cp.probarCode" /></th>
						<th><@s.text name="cp.prounitCode" /></th>
						<th><@s.text name="cp.proSaleType" /></th>
						<th><@s.text name="cp.actionCol" /></th>
					</tr>
				</thead>
				<tbody id="prtImptBody">
					<#if (camTemp.product)! == "5">
						<#if (camTemp.productList?? && camTemp.productList?size > 0) >
			          	<#list camTemp.productList as productList>
			          		<tr><td>${(productList.nameTotal)!}
			          		<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!}" />
			          		<input type="hidden" name="barCode" value="${(productList.barCode)!}" />
			          		<input type="hidden" name="unitCode" value="${(productList.unitCode)!}" />
			          		<input type="hidden" name="saleType" value="${(productList.saleType)!}" />
			          		<input type="hidden" name="proId" value="${(productList.proId)!}" />
			          		</td><td>${(productList.barCode)!}</td><td>${(productList.unitCode)!}</td><td>
			          		<#if (productList.saleType)! == "P">
			          		<@s.text name="cp.saleTypeP" />
			          		<#else>
			          		<@s.text name="cp.saleTypeN" />
			          		</#if>
			          		</td><td>
			          		<a href="javascript:void(0)" class="delete" onclick="javascript:TEMP002.delSepcPrt(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text">删除</span></a></td></tr>
			          	</#list>
			          	</#if>
					</#if>
				</tbody>
			</table>
			</div>
			         	</p>
	        		</td>
	        		<th><@s.text name="cp.giveLimit" /></th>
	        		<td>
	        			<span>
	        			<@s.select id="rangeFlag" list='#application.CodeTable.getCodes("1170")' listKey="CodeKey" listValue="Value" 
							value="${(camTemp.rangeFlag)!?html}"  name="rangeFlag" onchange="TEMP002.showDefRule(this, '0')"/>
						</span>
						<span id="rangeNumSpan" <#if (camTemp.rangeFlag)! == "1" || (camTemp.rangeFlag)! == "">class="hide"</#if> ><@s.text name="cp.singlePrtLimt" /><input type="text" name="rangeNum" class="number" id="rangeNum" value="${(camTemp.rangeNum)!?html}"/> 个</span>
	        		</td>
	        	</tr>
				<#list camTemp.combTemps as combTemp>
		        	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
	        </table>
		</div>
	</div>
	
<#-- 购买产品确认画面  -->
	<#elseif camTemp.tempCode == "BUS000030">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<p>
			<label><@s.text name="cp.SpecPrt" /></label>
			<#if (camTemp.product)! == "0">
				<label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1164","${(camTemp.product)!?html}")}</label>
			<#elseif (camTemp.product)! != "5">
				<#if (camTemp.productList?? && camTemp.productList?size > 0) >
	            <#list camTemp.productList as productList>
		          		<span class="span_BASE000001">
		          			<span class="bg_title">
		          			<#if (camTemp.product)! == "1">
		          			${(productList.nameTotal)!?html}
		          			<#elseif (camTemp.product)! == "2" || (camTemp.product)! == "3">
		          			${(productList.cateName)!?html}
		          			<#elseif (camTemp.product)! == "4">
		          			${(productList.prmNameTotal)!?html}
		          			</#if>
		          			</span> 
		          		</span>
	   				</#list>
		         </#if>
		      <#else>
			      <a href="javascript:void(0)" onclick="javascript:TEMP002.showImptRst();return false;"><@s.text name="cp.imptPrtlist" /></a>
			    <div id="prtImptDialog"></div>
			   <div id="dialogInitMessage" class="hide" style="overflow-y:auto">
			   <p><@s.text name="cp.imptPrtNum" />&nbsp;&nbsp;<label style="color:red;">
	         	<strong>
	         	${(camTemp.productList?size)!0}
	         	</strong></label></p>
			   <table id="prtImptTable" cellspacing="0" width="100%">
				<thead>
					<tr>
						<th><@s.text name="cp.proName" /></th>
						<th><@s.text name="cp.probarCode" /></th>
						<th><@s.text name="cp.prounitCode" /></th>
						<th><@s.text name="cp.proSaleType" /></th>
					</tr>
				</thead>
				<tbody id="prtImptBody">
					<#if (camTemp.productList?? && camTemp.productList?size > 0) >
		          	<#list camTemp.productList as productList>
		          		<tr><td>${(productList.nameTotal)!}
		          		</td><td>${(productList.barCode)!}</td><td>${(productList.unitCode)!}</td><td>
		          		<#if (productList.saleType)! == "P">
		          		<@s.text name="cp.saleTypeP" />
		          		<#else>
		          		<@s.text name="cp.saleTypeN" />
		          		</#if>
		          		</td></tr>
		          	</#list>
		          	</#if>
				</tbody>
			</table>
			</div>
		     </#if>
		  </p>
		   <p>
          	<label><@s.text name="cp.productCondTxt" /></label>
          	<label class="gray">${application.CodeTable.getVal("1287", '${(camTemp.proCond)!"0"}')}</label>
          </p>
          <p>
          	<label><@s.text name="cp.giveLimit" /></label>
          	<label class="gray">${application.CodeTable.getVal("1170", "${(camTemp.rangeFlag)!?html}")}
          	&nbsp
          	<#if (camTemp.rangeFlag)! == "0" && (camTemp.rangeNum)! != "">
          	(<@s.text name="cp.singlePrtLimt" /> ${(camTemp.rangeNum)!} 个)
          	</#if>
          	</label>
          </p>
	        <#list camTemp.combTemps as combTemp>
		        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
	</div>
	</div>
	
<#-- 会员生日  -->
	<#elseif camTemp.tempCode == "BUS000022">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.memberBir" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	    <table class="detail">
	    	<tr>
	    		<th><@s.text name="cp.actWeek" /></th>
	    		<td>
	    			<@s.select name="birthday" list='#application.CodeTable.getCodes("1159")' listKey="CodeKey" listValue="Value" 
						onchange="TEMP002.showPanel(this,'birthPanel');" value="${(camTemp.birthday)!?html}"/><br/>
					<span <#if "2" != (camTemp.birthday)!>class="hide"</#if> id="birthPanel2">
							<#-- 生日前  -->
							<span><label><@s.text name="cp.befBirthTxt" /></label>
							<input type="text" class="input" style="width:30px;height:15px;" name="befDay" value="${(camTemp.befDay)!}"/>
							<#-- 天  -->
							<label><@s.text name="cp.day" /></label></span>
							<#-- 后  -->
							<span><label><@s.text name="cp.aftTxt" /></label>
							<input type="text" class="input" style="width:30px;height:15px;" name="aftDay" value="${(camTemp.aftDay)!}"/>
							<#-- 天  -->
							<label><@s.text name="cp.day" /></label></label></span>
							<br/>
							<#-- 包含生日当天  -->
							<span><input type="checkbox" name="theBirth" id="theBirth" <#if "" == (camTemp.birthday)! && "" == (camTemp.theBirth)! || "1" == (camTemp.theBirth)!>checked="checked"</#if> value="1"/><label for="theBirth"><@s.text name="cp.contBirthDayTxt" /></label></span>
					</span>
				<span id = "birthPanel3" <#if "3" != (camTemp.birthday)!> class="hide"</#if>>
                <select style="width:auto;" id="specBirthMonth" name="spMonth" onchange="TEMP002.showMonthDay(this);" value="${(camTemp.spMonth)!}">
                </select>
          		<@s.text name="cp.month" />
          		 <select style="width:auto;" id="specBirthDay" name="spDay" value="${(camTemp.spDay)!}">
                </select>
               	<@s.text name="cp.dayTime" />
                </span>
					<#-- 特殊生日  -->
					<p><span><input type="checkbox" name="specBirth" id="specBirth" <#if "" == (camTemp.birthday)! && "" == (camTemp.specBirth)! || "1" == (camTemp.specBirth)!>checked="checked"</#if> value="1"/><label for="specBirth"><@s.text name="cp.specBirthTxt" /></label></span></p>
				<script type="text/javascript">
	       		var sm;
	       		var sd;
	       		var bd = '${(camTemp.birthday)!}';
	       		if (bd == "3") {
	       			sm = '${(camTemp.spMonth)!}';
	       			sd = '${(camTemp.spDay)!}';
	       		}
				TEMP002.showMonthDay('', sm, sd);
	      </script>
	    		</td>
	    		<#-- 选择单次  -->
	    		<th><@s.text name="cp.selBillTxt" /></th>
	    		<td>
	    			<@s.select list='#application.CodeTable.getCodes("1222")' listKey="CodeKey" listValue="Value" 
							onchange="TEMP002.showPanel(this,'billPanel');" value="${(camTemp.firstBillSel)!?html}"  name="firstBillSel"/>	
					<br/>
	    			<span <#if "3" != (camTemp.firstBillSel)!>class="hide"</#if> id="billPanel3">
	    					<#-- 第  -->
							<label><@s.text name="cp.theTxt" /></label>
							<input type="text" class="input" style="width:30px;height:15px;" name="billTime" value="${(camTemp.billTime)!}"/>
							<#-- 单  -->
							<label><@s.text name="cp.billNoTxt" /></label>
						</span>
	    		</td>
	    	</tr>
			<#list camTemp.combTemps as combTemp>
		        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
	    </table>
	     
	</div>
</div>

<#-- 会员生日确认画面  -->
	<#elseif camTemp.tempCode == "BUS000031">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<p><label><@s.text name="cp.actWeek" /></label>
			<#if "2" == (camTemp.birthday)!>
				<#-- 生日前  -->
				<label class="gray"><@s.text name="cp.befBirthTxt" /></label>
				<label style="color:red;">${(camTemp.befDay)!?html}</label>
				<label class="gray"><@s.text name="cp.day" /></label>
				<#-- 后  -->
				<label class="gray"><@s.text name="cp.aftTxt" /></label>
				<label style="color:red;">${(camTemp.aftDay)!?html}</label>
				<label class="gray"><@s.text name="cp.day" /></label>
				<#-- 包含生日当天  -->
				<#if "1" == (camTemp.theBirth)!><label class="gray"><@s.text name="cp.contBirthDayTxt" /></label></#if>
			<#elseif "3" == (camTemp.birthday)!>
				<label class="gray"><@s.text name="cp.theBirthTxt" />：</label>
				<label style="color:red;">${(camTemp.spMonth)!?html}</label>
				<label class="gray"><@s.text name="cp.month" /></label>
				<label style="color:red;">${(camTemp.spDay)!?html}</label>
				<label class="gray"><@s.text name="cp.dayTime" /></label>
			<#else>
				<label class="gray">${application.CodeTable.getVal("1159", "${(camTemp.birthday)!?html}")}</label>
			</#if>
			<#-- 特殊生日  -->
			<#if "1" == (camTemp.specBirth)!>
				<label style="color:red;"><@s.text name="cp.specBirthTxt" /></label>
			</#if>
		</p>
		<p>
			<#-- 选择单次  -->
			<label><@s.text name="cp.selBillTxt" /></label>
			<label class="gray">${application.CodeTable.getVal("1222", "${(camTemp.firstBillSel)!}")}</label>
			<#if "3" == (camTemp.firstBillSel)!>
				<#-- 第  -->
				<label class="gray"><@s.text name="cp.theTxt" /></label>
				<label style="color:red;">${(camTemp.billTime)!}</label>
				<#-- 单  -->
				<label class="gray"><@s.text name="cp.billNoTxt" /></label>
			</#if>
			
		</p>
        <#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		</#list>
	</div>
	</div>
	
<#-- 会员日  -->
	<#elseif camTemp.tempCode == "BUS000023">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.memberDay" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	    <table class="detail" >
	    	<tr>
	    		<th><@s.text name="cp.actWeek" /></th>
	    		<td colspan="3">
		    		<span style="width:100%;">
		    			<span>
							<input type="radio" class="radio" name="dayFlag" id="dayFlag" value="0" <#if "0" == (camTemp.dayFlag)! || "" == (camTemp.dayFlag)!>checked="checked"</#if>/>
							<label for="dayFlag"><@s.text name="cp.everyA" /></label>
						</span>
						<span>
							<input type="text" class="input"  style="width:30px;height:15px;" name="monthText"  value="${(camTemp.monthText)!?html}"/>
							<label><@s.text name="cp.monthInDay" /></label>
						</span>
						<span>
							<input type="text" class="input"  style="width:30px;height:15px;" name="dayText" value="${(camTemp.dayText)!?html}"/>
							<label><@s.text name="cp.day" /></label>
						</span>
					</span>
					<span>
						<span>
							<input type="radio" class="radio" name="dayFlag" id="dayEveFlag" value="1" <#if "1" == (camTemp.dayFlag)!>checked="checked"</#if>/>
							<label for="dayEveFlag"><@s.text name="cp.everyE" /></label>
						</span>
						<span>
							<input type="text" class="input" style="width:30px;height:15px;" name="monthEveText"  value="${(camTemp.monthEveText)!?html}" />
							<label><@s.text name="cp.monthInDE" /></label>
						</span>
						<@s.select name="dayNum" list='#application.CodeTable.getCodes("1162")' listKey="CodeKey" listValue="Value" value="${(camTemp.dayNum)!?html}"/>
						<@s.select name="dayWeek" list='#application.CodeTable.getCodes("1163")' listKey="CodeKey" listValue="Value" value="${(camTemp.dayWeek)!?html}"/>
					</span>
	    		</td>
	    	</tr>
			<#list camTemp.combTemps as combTemp>
		        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
	    </table>
	</div>
	</div>

<#-- 会员日确认画面  -->
	<#elseif camTemp.tempCode == "BUS000032">
	<div class="box2" >
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<p><label><@s.text name="cp.actWeek" /></label>
			<label class="gray">
			<#if "0" == (camTemp.dayFlag)!>
				<@s.text name="cp.every" />${(camTemp.monthText)!?html}<@s.text name="cp.monthInDay" />${(camTemp.dayText)!?html}<@s.text name="cp.day" />
			<#else><@s.text name="cp.every" />${(camTemp.monthEveText)!?html}<@s.text name="cp.monthInDE" />
				${application.CodeTable.getVal("1162", "${(camTemp.dayNum)!?html}")}${application.CodeTable.getVal("1163", "${(camTemp.dayWeek)!?html}")}
			</#if>
		</label></p>
        <#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		</#list>
	</div>
	</div>
	
<#--介绍人  -->
	<#elseif camTemp.tempCode == "BUS000025">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.introducer" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
		<table class="detail" >
			<tr>
				<th><@s.text name="cp.giveBill" /></th>
				<td colspan="3">
					<input type="radio"  class="radio" name="giveFlag" value="0" id="beforeBill" <#if (camTemp.giveFlag)! == "" || (camTemp.giveFlag)! == "0">checked="checked"</#if>/>
					<label for="beforeBill"><@s.text name="cp.beforeBill" /></label>
					<input type="text" class="input" style="width:30px;height:15px;" name="beforeBill" value="${(camTemp.beforeBill)!?html}"/>
					<@s.text name="cp.bill"/>
					<input type="radio"  class="radio" style="margin-left:100px" id="allBill" value="1" name="giveFlag" <#if (camTemp.giveFlag)! == "1">checked="checked"</#if>/>
					<label for="allBill"><@s.text name="cp.allBill" /></label>
				</td>
			</tr>
			<#list camTemp.combTemps as combTemp>
		        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
		</table>
	</div>
	</div>
	
<#-- 介绍人确认画面  -->
	<#elseif camTemp.tempCode == "BUS000034">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#if "0" == (camTemp.giveFlag)!>
		<p><label><@s.text name="cp.beforeBill" /></label><label style="color:red;">${(camTemp.beforeBill)!?html}</label>
			<label><@s.text name="cp.bill" /></label>
		<#else><p><label><@s.text name="cp.allBill" /></label></#if></p>
        <#list camTemp.combTemps as combTemp>
	        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
		</#list>
	</div>
	</div>
	
<#-- 赠品不计积分 -->
	<#elseif camTemp.tempCode == "BASE000042">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.proDotPoint" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
    	<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	    <input type="hidden" name="selPrtIndex" value="${camTemp.tempCode}_${index}"/>
	    <table class="detail" >
			<tr>
				<div  class="prtClass">
				<th>
					<label for="proFlag"><@s.text name="cp.suPro" /></label>
				</th>
				<td>
					<a class="add right" id="choicePointPro" onClick="TEMP002.showTable(this, 0);">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.choicePro" /></span></a>
					<span id="prtSel_${(camTemp.tempCode)!?html}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
			          <#if (camTemp.productList?? && camTemp.productList?size > 0) >
			          	<#list camTemp.productList as productList>
			          		<span class="span_BASE000001">
		          				<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
		          				<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
		          				<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
			          			<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
			          			<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span>
		          				<span <#if (camTemp.productList?size == 1)>style="display:none;" class="close hide"<#else>class="close"</#if> onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
		          				<span class="ui-icon ui-icon-close"></span></span>
			          		</span>
		   				</#list>
			          </#if>
			         </span>
				</td>
				</div>
				<div class="prmClass">
				<th>
					<label for="prmProFlag"><@s.text name="cp.suPrmPro" /></label>
				</th>
				<td>
					<a class="add right" id="choicePointPro" onClick="TEMP002.showTable(this, -1, '1')">
						<span class="ui-icon icon-add"></span>
						<span class="button-text"><@s.text name="cp.choicePrmPro" /></span></a>
					<span id="prtSel_${(camTemp.tempCode)!?html}_${index}_1" class="showPro_${(camTemp.tempCode)!?html}_${index}_1">
			          <#if (camTemp.prmProductList?? && camTemp.prmProductList?size > 0) >
			          	<#list camTemp.prmProductList as productList>
			          		<span class="span_BASE000001">
		          				<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
		          				<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
		          				<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
			          			<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
			          			<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span> 
		          				<span <#if (camTemp.prmProductList?size == 1)>style="display:none;" class="close hide"<#else>class="close"</#if> onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
		          				<span class="ui-icon ui-icon-close"></span></span>
			          		</span>
		   				</#list>
			          </#if>
			         </span>
				</td>
				</div>
		      </tr>
		      <tr>
		      	<th><@s.text name="cp.dotPoint" /></th>
		      	<td>
		      		<input type="radio" class="radio" name="discount" id="oneDisCount" value="0" <#if (camTemp.discount)! == "0" || "" == (camTemp.discount)!>checked="checked"</#if>/>
					<label for="oneDisCount"><@s.text name="cp.oneDotPoint" /></label>
					<input type="radio" class="radio" name="discount" id="allDisCount" value="1" <#if (camTemp.discount)! == "1">checked="checked"</#if>/>
					<label for="allDisCount"><@s.text name="cp.allDotPoint" /></label>
		      	</td>
		      </tr>
	    </table>
	</div>
	</div>
	
<#-- 赠品不计积分 确认画面  -->
	<#elseif camTemp.tempCode == "BASE000044">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#if "1" == (camTemp.proFlag)!>
			<p><label><@s.text name="cp.buy" /></label>
			<#if (camTemp.productList?? && camTemp.productList?size > 0) >
            	<#list camTemp.productList as productList>
	          		<span class="span_BASE000001">
	          			<span class="bg_title">${(productList.nameTotal)!?html}</span> 
	          		</span>
   				</#list>
	         </#if>
		</#if>
		<#if "1" == (camTemp.prmProFlag)!>
			<#if (camTemp.prmProductList?? && camTemp.prmProductList?size > 0) >
            	<#list camTemp.prmProductList as productList>
	          		<span class="span_BASE000001">
	          			<span class="bg_title">${(productList.nameTotal)!?html}</span> 
	          		</span>
   				</#list>
	         </#if>
		</#if>
		<#if "0" == (camTemp.discount)!>
			<label><@s.text name="cp.oneDotPoint" /></label>
		<#else>
			<label><@s.text name="cp.allDotPoint" /></label>
		</#if>
	</div>
	</div>
	
<#-- 折扣不计积分  -->
	<#elseif camTemp.tempCode == "BASE000043">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.zkDotPoint" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
    	<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
		<p>
			<input type="radio" name="discount" class="radio" id="oneDiscount" <#if (camTemp.discount)! == "0" || (camTemp.discount)! == "">checked="checked"</#if> value="0"/>
			<label for="oneDiscount"><@s.text name="cp.zkOnePointDot" /></label>
		</p>
		<p>
			<input type="radio" name="discount" class="radio" id="oneDisCount" <#if (camTemp.discount)! == "1">checked="checked"</#if> value="1"/>
			<label for="oneDisCount"><@s.text name="cp.zkDotPointAll" /></label>
		</p>
		<p>
			<input type="radio" name="discount" class="radio" id="allDiscount" <#if (camTemp.discount)! == "2">checked="checked"</#if> value="2"/>
			<label for="allDiscount"><@s.text name="cp.zkDotALLPoint" /></label>
		</p>
	</div>
	</div>
	
<#-- 折扣不计积分确认画面  -->
	<#elseif camTemp.tempCode == "BASE000045">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#if (camTemp.discount)! == "0"><label><@s.text name="cp.zkOnePointDot" /></label>
		<#elseif (camTemp.discount)! == "1"><label><@s.text name="cp.zkDotPointAll" /></label>
		<#elseif (camTemp.discount)! == "2"><label><@s.text name="cp.zkDotALLPoint" /></label>
		</#if>
	</div>
	</div>
<#--会员升级  -->
	<#elseif camTemp.tempCode == "BUS000026">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.memberUp" /></strong>
		<a class="add right" name="addJon" onClick="CAMPAIGN_TEMPLATE.addRule(this,'${camTemp.tempCode}_${index}','box4');">
			<span class="ui-icon icon-add"></span>
			<span class="button-text"><@s.text name="cp.addRule"/></span></a>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		<input type="hidden" name="TEMPCOPYSIZE" id="TEMPCOPY-SIZE-${camTemp.tempCode}_${index}" value='${(camTemp.TEMPCOPYSIZE)!"1"}'/>
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

<#-- 会员升级赠与积分  -->
	<#elseif camTemp.tempCode == "BUS000040">
		<div id="${camTemp.tempCode}_${index}" class="box4-content-content">
		    <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
		    <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        	<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	    	<div class="toolbar clearfix" >
		        <a class="add right deleteClass" name="deleteUp" onclick="CAMPAIGN_TEMPLATE.deleteRule(this,'box4-content-content')">
					<span class="ui-icon icon-delete"></span><span class="button-text"><@s.text name="cp.delete" /></span>
				</a>
	    	</div>
	    	<table class="detail">
	    		<tr id="member_${camTemp.tempCode}_${index}">
					<#if (camTemp.memberInfoList?? && camTemp.memberInfoList?size > 0) >
					<#list camTemp.memberInfoList as memberInfo>
						<div <#if (memberInfo.usedDate)! != (camTemp.levelDate)!>class="hide"</#if> id="member_${(memberInfo_index)!}" >
						<th><@s.text name="cp.nowLevel" /></th>
						<#if (memberInfo.memberLevelList?? && memberInfo.memberLevelList?size > 0)>
						<td>
							<select name="memberformLevelId">
				            <#list memberInfo.memberLevelList as memberLevelInfo>
				            	<option <#if (camTemp.memberformLevelId)! == memberLevelInfo.memberLevelId.toString() >selected="selected"</#if>  value="${memberLevelInfo.memberLevelId.toString()}_${memberLevelInfo.grade}" >
				            	${(memberLevelInfo.levelName)!?html}
				            	</option>
				            </#list>
				            </select>
				         </td>
				         </#if>
						 <th><@s.text name="cp.upToLevel" /></th>
						 <#if (memberInfo.memberLevelList?? && memberInfo.memberLevelList?size > 0)>
					     <td><span><select name="membertoLevelId">
					            <#list memberInfo.memberLevelList as memberLevelInfo>
					            	<option <#if (camTemp.membertoLevelId)! == memberLevelInfo.memberLevelId.toString() >selected="selected"</#if>  value="${memberLevelInfo.memberLevelId.toString()}_${memberLevelInfo.grade}" >
					            	${(memberLevelInfo.levelName)!?html}
					            	</option>
					            </#list>
					            </select></span>
					      </td>
					      </#if>
						</div>
					</#list>
					</#if>
				</tr>
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
		
<#-- 会员升级确认画面  -->
	<#elseif camTemp.tempCode == "BUS000035">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#list camTemp.combTemps as combTemp>
	          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
	           <#if (combTemp_index lt camTemp.combTemps?size - 1) >
					<div class="line_Yellow">
					</div>
				</#if>
		</#list>
	</div>
	</div>	
	
		
<#-- 会员升级赠与积分(确认画面)  -->
	<#elseif camTemp.tempCode == "BUS000041">
		<span>
	    <p><label><@s.text name="cp.memberLevelDate" />${(camTemp.levelDate)!?html}</label></p>
		<p><label><@s.text name="cp.from" /></label><label class="gray">${(camTemp.formLevelName)!?html}</label>
		<label><@s.text name="cp.upLevelTo" /></label><label class="gray">${(camTemp.toLevelName)!?html}</label></p>
		</span>
		<#list camTemp.combTemps as combTemp>
	          <@template index=index + "_" + combTemp_index + "_1_TEMPCOPY" camTemp=combTemp/>
		</#list>
		
<#--退货  -->
	<#elseif camTemp.tempCode == "BASE000018">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.retGoods" /></strong><span class="gray"><@s.text name="cp.noReceipt" /></span>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <table class="detail" >
        	<tr>
        		<th><@s.text name="cp.jianPoint" /></th>
        		<td colspan="2">
        			<span style="width:100%;margin-bottom:0.5em;">
						<span>
							<input type="radio" id="getMark" name="retGoodsFlag" value="0" <#if "0" == (camTemp.retGoodsFlag)! || "" == (camTemp.retGoodsFlag)!>checked="checked"</#if>  class="radio"/>
							<label for="getMark"><@s.text name="cp.cut" /></label>
						</span>
						<span>
							<input type="text" class="input" style="width:30px;height:15px;" id="marks" name="marks" value="${(camTemp.marks)!?html}"/>
							<label><@s.text name="cp.point" /></label>
						</span>
					</span>
					<span style="margin-bottom:0.5em;">
						<span>
							<input type="radio" id="calMark"  class="radio" name="retGoodsFlag" <#if "1" == (camTemp.retGoodsFlag)!>checked="checked"</#if> value="1"/>
							<label for="calMark"><@s.text name="cp.getMoney" /></label>
						</span>
						<span>
							<input type="text" class="input" style="width:30px;height:15px;" id="multipleMark" name="multipleMark" value="${(camTemp.multipleMark)!?html}"/>
							<label><@s.text name="cp.mulCut" /><label>
						</span>
					</span>
        		</td>
        	</tr>
        	<tr>
        		<th><@s.text name="cp.round" /></th>
        		<td clospan="2">
        			<p>
						<input type="radio" id="oneRound" name="oneRound" value="0" <#if (camTemp.oneRound)! == "0" || "" == (camTemp.oneRound)!>checked="checked"</#if> />
						<label for="oneRound"><@s.text name="cp.oneRound" /></label>
					</p>
					<p>
						<input type="radio" id="allRound" name="oneRound" value="1" <#if (camTemp.oneRound)! == "1">checked="checked"</#if>/>
						<label for="allRound"><@s.text name="cp.allRound" /></label>
					</p>
        		</td>
        	</tr>
        </table>
	</div>
	</div>
	
<#-- 退货确认画面  -->
	<#elseif camTemp.tempCode == "BASE000023">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#if "0" == (camTemp.retGoodsFlag)!>
			<p><label><@s.text name="cp.cut" /></label>
				<label style="color:red;">${(camTemp.marks)!?html}</label>
				<label><@s.text name="cp.point" /></label></p>
		<#elseif "1" == (camTemp.retGoodsFlag)!>
			<p><label><@s.text name="getMoney" /></label>
				<label style="color:red;">${(camTemp.multipleMark)!?html}</label>
				<label><@s.text name="cp.mulCut" /></label></p>
		</#if>
		<#if "0" == (camTemp.oneRound)!>
			<label><@s.text name="cp.oneRound" /></label>
		<#elseif "1" == (camTemp.oneRound)! >
			<label><@s.text name="cp.allRound" /></label>
		</#if>
	</div>
	</div>	
	
<#-- 积分清零 -->
	<#elseif camTemp.tempCode == "BASE000019">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.pointClear" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
		<p><@s.text name="cp.memberLevel" />
		<#if (camTemp.memberLevelList?? && camTemp.memberLevelList?size > 0)>
       		<select style="margin-left:45px;" name="memberLevelId">
            <#list camTemp.memberLevelList as memberInfo>
            	<option <#if (camTemp.memberLevelId)! == memberInfo.memberLevelId.toString() >selected="selected"</#if>  value="${memberInfo.memberLevelId.toString()}" >
            	${(memberInfo.levelName)!?html}
            	</option>
            </#list>
            </select>
        </#if></p>
		<p><input type="radio"  class="radio" id="consumeClear" name="pointClearFlag" <#if "0" == (camTemp.pointClearFlag)! || "" == (camTemp.pointClearFlag)!>checked="checked"</#if> value="0"/><label for="memberClear"><@s.text name="cp.memberPass" /></label>
		<span><input type="text" class="input" style="width:30px;height:15px;" name="maxMonthUse" value="${(camTemp.maxMonthUse)!?html}"/></span><@s.text name="cp.monthInClear" /></p>
		<p><input type="radio" name="pointClearFlag" value="1" class="radio" <#if "1" == (camTemp.pointClearFlag)!>checked="checked"</#if> id="markClear"/><label for="markClear"><@s.text name="cp.passDay" /></label>
		<span><input type="text" class="input" style="width:30px;height:15px;" name="maxMonthGet" value="${(camTemp.maxMonthGet)!?html}"/></span><@s.text name="cp.monthClear" /></p>
		<p style="margin-left:20px;"><@s.text name="cp.pointClearDay" />
		<span><input type="text"  class="input" style="width:30px;height:15px;" name="billStartMonth" value="${(camTemp.billStartMonth)!?html}"/><@s.text name="cp.month" /><span>
		<span><input type="text"  class="input" style="width:30px;height:15px;" name="billStartDay" value="${(camTemp.billStartDay)!?html}"/><@s.text name="cp.dayTime" /><span></p>
	</div>
</div>

<#-- 积分清零确认画面  -->
	<#elseif camTemp.tempCode == "BASE000024">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<p><label><@s.text name="cp.memberLevel" /></label>
			<label style="margin-left:20px;" class="gray">${(camTemp.levelName)!?html}</label></p>
		<#if "0" == (camTemp.pointClearFlag)!>
			<p><label><@s.text name="cp.memberPass" /></label>
				<label style="color:red;">${(camTemp.maxMonthUse)!?html}</label>
				<label><@s.text name="cp.monthInClear" /></label></p>
		<#else>
			<p><label><@s.text name="cp.passDay" /></label>
				<span><label style="color:red;">${(camTemp.maxMonthGet)!?html}</label></span>
				<label><@s.text name="cp.monthClear" /></label></p>
			<p><label><@s.text name="cp.pointClearDay" /></label>
				<span><label style="color:red;margin-left:20px;">${(camTemp.billStartMonth)!?html}<@s.text name="cp.month" />${(camTemp.billStartDay)!?html}<@s.text name="dayTime" /></label></span></p>
		</#if>
	</div>
	</div>
	
<#-- 积分兑换 -->
	<#elseif camTemp.tempCode == "BASE000027">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.pointChange" /><label style="color:red;"><@s.text name="cp.pointChangeGive" /></label></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
		<p><input type="radio" class="radio" name="exchange" id="exchangeGive" value="0" <#if (camTemp.exchange)! == "0" || (camTemp.exchange)! == "">checked="checked"</#if> /><label for="exchangeGive"><@s.text name="cp.changeGivePoint" /></label><p>
		<p><input type="radio" class="radio" name="exchange" id="exchangeNoGive" value="1" <#if (camTemp.exchange)! == "1">checked="checked"</#if>/><label for="exchangeNoGive"><@s.text name="cp.changeNoGivePoint" /></label><p>
	</div>
</div>

<#-- 积分兑换确认画面  -->
	<#elseif camTemp.tempCode == "BASE000028">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<#if "0" == (camTemp.exchange)!>
			<p><label class="gray"><@s.text name="cp.changeGivePoint" /></label></p>
		<#else>
			<p><span><label class="gray"><@s.text name="cp.changeNoGivePoint" /></label></span></p>
		</#if>
	</div>
	</div>
		
<#-- 积点产品 -->
	<#elseif camTemp.tempCode == "BASE000048">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.pointPro" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>  
	    <input type="hidden" name="selPrtIndex" value="${camTemp.tempCode}_${index}"/>
	    <table class="detail" >
	    	<tr>
	    		<th><@s.text name="cp.culPoint" /></th>
	    		<td>
	    			<span>
	    			<@s.select name="culModel" list='#application.CodeTable.getCodes("1171")' 
	    				listKey="CodeKey" listValue="Value" value="${(camTemp.culModel)!?html}"/>
	    			</span>
	    			<span>
	    				<label style="color:red;" ><@s.text name="cp.ruleCul" /></label>
	    			</span>
	    		</td>
	    		<th><@s.text name="cp.choicePointPro" /></th>
	    		<td>
	    			<a class="add" onClick="TEMP002.showTable(this, 0);">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.choicePro" /></span></a>
					<p class="clearfix" style="margin-left: 120px;">
			         <span class="left">
			          <span id="prtSel_${(camTemp.tempCode)!?html}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
			          <#if (camTemp.productList?? && camTemp.productList?size > 0) >
			          	<#list camTemp.productList as productList>
			          		<span class="span_BASE000001">
		          				<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
		          				<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
		          				<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
		          				<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
			          			<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span> 
			          			<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
			          			<span class="ui-icon ui-icon-close"></span></span>
			          		</span>
		   				</#list>
			          </#if>
			         </span>
			         </span>
			         </p>
	    		</td>
	    	</tr>
	    	<tr id="prtSel_includePro">
	    		<th><@s.text name="cp.morePointPro" /></th>
	    		<td colspan="3"  style="white-space: normal;">
	    			<#if (camTemp.includeProList?? && camTemp.includeProList?size>0)>
					<#list camTemp.includeProList as includePro>
		    			<span class="span_pro" style="width:90%;float:left;">
		    				<label><@s.text name="cp.getPro" /></label>
		    				<input type="text" class="input" name="includeProNum" value="${(includePro.includeProNum)!?html}" />
		    				<label><@s.text name="cp.difPro" /></label>
		    				<input type="text" class="input" name="pointNum" value="${(includePro.pointNum)!?html}" />
		    				<label><@s.text name="cp.point" /></label>
		    				<a class="add" onClick="TEMP002.addPointPro(this);">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.add" /></span></a>
		    				<a class="add deleteClass" name="deleteUp" <#if (camTemp.includeProList?size == 1)>style="display:none;"</#if>
		    					 onclick="TEMP002.deletePointPro(this);">
								<span class="ui-icon icon-delete"></span>
								<span class="button-text"><@s.text name="cp.delete" /></span></a>
						</span>
					</#list>
					<#else>
						<span class="span_pro" style="width:90%;float:left;">
		    				<label><@s.text name="cp.getPro" /></label>
		    				<input type="text" class="input" name="includeProNum" value="" />
		    				<label><@s.text name="cp.difPro" /></label>
		    				<input type="text" class="input" name="pointNum" value="" />
		    				<label><@s.text name="cp.point" /></label>
		    				<a class="add" onClick="TEMP002.addPointPro(this);">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.add" /></span></a>
							<a class="add deleteClass" name="deleteUp" style="display:none;" onclick="TEMP002.deletePointPro(this);">
								<span class="ui-icon icon-delete"></span>
								<span class="button-text"><@s.text name="cp.delete" /></span></a>
						</span>
					</#if>
	    		</td>
	    	 </tr>
	    	 <tr id="prtSel_onePro">
	    	 	<th><@s.text name="cp.buyPointPro" /></th>
	    	 	<td colspan="3"  style="white-space: normal;">
		    	 	<#if (camTemp.oneProList?? && camTemp.oneProList?size>0)>
					<#list camTemp.oneProList as onePro>
		    	 		<span class="span_pro"  style="width:90%;float:left;">
		    				<label><@s.text name="cp.buyOne" /></label>
		    				<input type="text" class="input" name="oneProNum" value="${(onePro.oneProNum)!?html}" />
		    				<label><@s.text name="cp.buyPoint" /></label>
		    				<input type="text" class="input" name="mulPoint" value="${(onePro.mulPoint)!?html}" />
		    				<label><@s.text name="cp.mulPoint" /></label>
		    				<a class="add" onClick="TEMP002.addPointPro(this);">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.add" /></span></a>
		    				<a class="add deleteClass" name="deleteUp" <#if (camTemp.oneProList?size == 1)>style="display:none;"</#if>
								 onclick="TEMP002.deletePointPro(this);">
								<span class="ui-icon icon-delete"></span>
								<span class="button-text"><@s.text name="cp.delete" /></span></a>
						</span>
					</#list>
					<#else>
						<span class="span_pro" style="width:90%;float:left;">
		    				<label><@s.text name="cp.buyOne" /></label>
		    				<input type="text" class="input" name="oneProNum" value="" />
		    				<label><@s.text name="cp.buyPoint" /></label>
		    				<input type="text" class="input" name="mulPoint" value="" />
		    				<label><@s.text name="cp.mulPoint" /></label>
		    				<a class="add" onClick="TEMP002.addPointPro(this);">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.add" /></span></a>
							<a class="add deleteClass" name="deleteUp" style="display:none;" onclick="TEMP002.deletePointPro(this);">
								<span class="ui-icon icon-delete"></span>
								<span class="button-text"><@s.text name="cp.delete" /></span></a>
						</span>
					</#if>
	    		</td>
	    	</tr>
	    	<tr>
	    		<th><@s.text name="cp.buyPointPro" /></th>
	    		<td colspan="3">
	    				<label><@s.text name="cp.buyOnePass" /></label>
	    				<input type="text" class="input" name="oneMoreProNum" value="${(camTemp.oneMoreProNum)!?html}" />
	    				<label><@s.text name="cp.buyPoint" /></label>
	    				<input type="text" class="input" name="mulMorePoint" value="${(camTemp.mulMorePoint)!?html}" />
	    				<label><@s.text name="cp.mulPoint" /></label>
	    		</td>
	    	</tr>
	    </table>
	</div>
</div>

<#-- 积点产品确认画面 -->
	<#elseif camTemp.tempCode == "BASE000049">
	<div class="box2" >
		<div class="box2-header clearfix" >
			<strong class="left" ><@s.text name="cp.pointPro" /></strong>
		</div>
		<div class="box2-content" >
			<p>
				<label><@s.text name="cp.proMoreRule" /></label>
				<label class="gray">${application.CodeTable.getVal("1171","${(camTemp.culModel)!?html}")}</label>
				<label><@s.text name="cp.mulPointWay" /></label>
			</p>
			<p>
				<label><@s.text name="cp.pointPro" /></label>
				<span>
				<#if (camTemp.productList?? && camTemp.productList?size > 0) >
	            <#list camTemp.productList as productList>
		          		<span class="span_BASE000001">
		          			<span class="bg_title">${(productList.nameTotal)!?html}</span> 
		          		</span>
	   				</#list>
		         </#if>
		        </span>
			</p>
			<#if (camTemp.includeProList?? && camTemp.includeProList?size>0)>
				<#list camTemp.includeProList as includePro>
					<#if (includePro.includeProNum)! != "" >
						<p>
							<label><@s.text name="cp.getPro" /></label>
							<label style="color:red;" >${(includePro.includeProNum)!?html}</label>
							<label><@s.text name="cp.difPro" /></label>
							<label style="color:red;" >${(includePro.pointNum)!?html}</label>
							<label><@s.text name="cp.point" /></label>
						</p>
					</#if>
				</#list>
			</#if>
			<#if (camTemp.oneProList?? && camTemp.oneProList?size>0)>
				<#list camTemp.oneProList as onePro>
					<#if (onePro.oneProNum)! != "" >
						<p>
							<label><@s.text name="cp.buyOne" /></label>
							<label style="color:red;" >${(onePro.oneProNum)!?html}</label>
							<label><@s.text name="cp.buyPoint" /></label>
							<label style="color:red;" >${(onePro.mulPoint)!?html}</label>
							<label><@s.text name="cp.mulPoint" /></label>
						</p>
					</#if>
				</#list>
			</#if>
			<#if (camTemp.oneMoreProNum)! != "">
			<p>
				<label><@s.text name="cp.buyOnePass" /></label>
				<label style="color:red;" >${(camTemp.oneMoreProNum)!?html}</label>
				<label><@s.text name="cp.buyPoint" /></label>
				<label style="color:red;" >${(camTemp.mulMorePoint)!?html}</label>
				<label><@s.text name="cp.mulPoint" /></label>
			</p>
			</#if>
		</div>
	</div>

<#-- 购买产品积分上限模块 -->
	<#elseif camTemp.tempCode == "BASE000046">
	<div class="box4" id="${camTemp.tempCode}_${index}">
	<div class="box4-header">
		<strong><@s.text name="cp.pointLimit" /></strong>
	</div>
	<div class="box4-content">
        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
		<p>
			<label><@s.text name="cp.round" /></label>
		</p>
		<p style="margin-left:40px;">
			<input type="radio" class="radio" id="oneRound" name="oneRound" value="0" <#if (camTemp.oneRound)! == "0" || "" == (camTemp.oneRound)!>checked="checked"</#if> />
			<label for="oneRound"><@s.text name="cp.oneRound" /></label>
		</p>
		<p style="margin-left:40px;">
			<input type="radio" class="radio" id="allRound" name="oneRound" value="1" <#if (camTemp.oneRound)! == "1">checked="checked"</#if>/>
			<label for="allRound"><@s.text name="cp.allRound" /></label>
		</p>
		<p>
			<input type="checkbox" name="buyProLimit" id="buyProLimit" value="1" <#if "1" == (camTemp.buyProLimit)!>checked="checked"</#if> />
			<label for="buyProLimit"><@s.text name="cp.oneBillPass" /></label>
			<span><input type="text" name="pointPro" class="input" value="${(camTemp.pointPro)!?html}"/><@s.text name="cp.point" /></span>
		</p>
	</div>
</div>

<#-- 购买产品积分上限确定模块  -->
	<#elseif camTemp.tempCode == "BASE000047">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.pointLimit" /></strong>
	</div>
	<div class="box2-content">
		<#if "0" == (camTemp.oneRound)!>
			<p><label class="gray"><@s.text name="cp.oneRound" /></label></p>
		<#else>
			<p><span><label class="gray"><@s.text name="cp.allRound" /></label></span></p>
		</#if>
		<#if "1" == (camTemp.buyProLimit)!>
			<p>
				<label class="gray" > <@s.text name="cp.oneBillPass" />${(camTemp.pointPro)?html}<@s.text name="cp.point" /></label>
			</p>
		</#if>
	</div>
	</div>
	
<#-- 规则描述模块 -->
   <#elseif camTemp.tempCode == "BASE000051">
	<#if (camTemp.depList?? && camTemp.depList?size>0)>
	<div class="box2">
		<div class="box2-header clearfix">
			<strong class="left" ><@s.text name="cp.ruleDel" /></strong>
		</div>
		<div class="box2-content" >
			<#list camTemp.depList as depInfo>
				<p>
					<label>${(depInfo.ruleContent)!?html}</label>
				</p>
			</#list>
		</div>
	</div>
	</#if>

<#-- 积分清零条件  -->
<#elseif camTemp.tempCode == "BASE008000">
		<div class="box4" id="${camTemp.tempCode}_${index}">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
	        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	        <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
			<div class="box4-header">
				<strong><@s.text name="cp.pointClearCond" /></strong>
			</div>
			<div class="box4-content">
				<p>
					<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointClearMemb" /></span>
					<span><@s.select style="margin-left:20px;" name="member" id="member" onchange="TEMP002.showMember(this)" list='#application.CodeTable.getCodes("1155")' 
						listKey="CodeKey" listValue="Value" value="${(camTemp.member)!?html}" />
					</span>
				</p>
				<p style="margin-left:60px;" <#if (camTemp.member)! != "1">class="hide"</#if> id="memberInfo">
					<#if (camTemp.memberLevelList?? && camTemp.memberLevelList?size > 0)>
				        <#list camTemp.memberLevelList as memberLevel>
				        <span class="memberList" style="margin-left:20px;">
				        	<input type="checkbox" name="memberShowFlag" <#if (memberLevel.memberShowFlag)! == "1">checked="checked"</#if> value="1"/>
				        	<span>${(memberLevel.levelName)!}</span>
				        	<input type="hidden" name="levelName" value="${(memberLevel.levelName)!}"/>
				        	<input type="hidden" name="memberLevelId" value="${(memberLevel.memberLevelId)!}"/>
				        </span>
				        </#list>
			        </#if>
				</p>
				<p>
					<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.belongCounter" /></span>
					<span><@s.select style="margin-left:20px;" list='#application.CodeTable.getCodes("1156")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.choicePlace)!?html}" id="choicePlace" name="choicePlace" onchange="TEMP002.showSearch(this)"/>
					<input type="hidden" name="locationType" id="locationType" value="${(camTemp.locationType)!0}" />
					</span>
					<a onclick="TEMP002.showTree('#choicePlace');return false;" class="search" id="searchTree" <#if (camTemp.choicePlace)! == "0" || (camTemp.choicePlace)! == "" || (camTemp.choicePlace)! == "5">style="display:none;"</#if>>
						<span class="ui-icon icon-search"></span>
						<span class="button-text"><@s.text name="cp.search" /></span>
					</a>
					<span <#if (camTemp.choicePlace)! != "5">class="hide"</#if> id="importDiv">
					<span class="highlight">※</span><@s.text name="cp.useModel" />
					<a href="/Cherry/download/柜台信息模板.xls"><@s.text name="cp.downModel" /></a>
				    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
				    <input type="button" value="<@s.text name="global.page.browse"/>"/>
				    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="pathExcel.value=this.value;return false;" /> 
				    <input type="button" value="<@s.text name="cp.importExcel_btn"/>" onclick="TEMP002.ajaxFileUpload();return false;" id="upload"/>
    				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
					</span>
				</p>
				<div id="treeArea" <#if (camTemp.choicePlace)! == "0" || (camTemp.choicePlace)! == "">class="hide"</#if>>
				<div class="box2-header clearfix" > 
				   <strong id="left_tree" class="left active">
		       			<span class="ui-icon icon-flag"></span>
		       			<@s.text name="cp.choiceCounter" />
		       	   </strong>
		       	</div>
				<div class="jquery_tree">
		    		<div class="jquery_tree treebox_left tree ztree" id = "treeDemo" style="overflow: auto;background:#FFF;height:350px;border:1px solid #CCC">
		     			 
		      		</div>
		    	</div>
		    	<#if (camTemp.nodesList?? && camTemp.nodesList?size > 0)>
			       <script type="text/javascript">
			       		var nodesList = '${(camTemp.nodes)!}';
						TEMP002.loadTree(nodesList);
			      </script>
			    </#if>
			    </div>
			</div>
			<div class="hide">
				<span id="errmsg1"><@s.text name="cp.errorMsg" /></span>
			</div>
		</div>
		
		<#-- 积分清零方式  -->
		<#elseif camTemp.tempCode == "BASE008001">
		<div class="box4" id="${camTemp.tempCode}_${index}">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
	        <input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	        <input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	        <input type="hidden" name="selPrtIndex" value="${camTemp.tempCode}_${index}"/>
			<div class="box4-header">
				<strong><@s.text name="cp.pointClearMed" /></strong>
			</div>
			<div class="box4-content">
				<p>
					<span class="ui-icon icon-arrow-crm"></span>
					<span class="bg_title"><@s.text name="cp.pointDate" /></span>
					<span style="margin-left:10px;">
						<@s.text name="cp.ptClearFromDate" />
						<@s.select style="width:145px;" list='#application.CodeTable.getCodes("1267")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.pcFromDate)!?html}" name="pcFromDate"/>
						&nbsp;
						<@s.text name="cp.ptClearLength" />
						<@s.select list='#application.CodeTable.getCodes("1270")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.lengthKbn)!?html}" name="lengthKbn" onchange="TEMP002.changePCLength(this)"/>
						<span <#if (camTemp.lengthKbn)! == "1"> class="hide" </#if> id="pcLength_0">
						<input type="text" class="number" name="clearNum" value="${(camTemp.clearNum)!}"/>
						<@s.select style="width:60px;" list='#application.CodeTable.getCodes("1268")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.numType)!?html}" name="numType"/>
						<input type="checkbox" id="curKbnCheck" name="curKbn" value="1" <#if (camTemp.curKbn)! == "1">checked="checked"</#if>/>
						<label for="curKbnCheck" style="cursor:pointer;"><@s.text name="cp.curdateCond" /></label>
						</span>
						<span <#if (camTemp.lengthKbn)! != "1"> class="hide" </#if> id="pcLength_1">
						<@s.text name="cp.ptClearYearNum" />
						<input type="text" class="number" name="yearNo" value="${(camTemp.yearNo)!}"/>
						<@s.text name="cp.ptClearYear" />
						<input type="text" class="number" name="disMonth" value="${(camTemp.disMonth)!}"/> <@s.text name="cp.month" />
						<input type="text" class="number" name="disDay" value="${(camTemp.disDay)!}"/> <@s.text name="cp.dayTime" />
						</span>
					</span>
				</p>
				
				<p>
					<span class="ui-icon icon-arrow-crm"></span>
					<span class="bg_title"><@s.text name="cp.ptClearExecDay" /></span>
					<span style="margin-left:10px;">
						<@s.text name="cp.ptClearDay" />
						<@s.select style="width:145px;" list='#application.CodeTable.getCodes("1269")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.pcExecDate)!?html}" name="pcExecDate" onchange="TEMP002.showExecDate(this)"/>
						<span id="execDate" <#if (camTemp.pcExecDate)! != "2"> class="hide"</#if>>
						&nbsp;
						<@s.text name="cp.ptClearTime" />
						<input type="text" class="number" name="execMonth" value="${(camTemp.execMonth)!}"/> <@s.text name="cp.month" />
						<input type="text" class="number" name="execDay" value="${(camTemp.execDay)!}"/> <@s.text name="cp.dayTime" />
						</span>
						<span id="execDate3" <#if (camTemp.pcExecDate)! != "3"> class="hide"</#if>>
						&nbsp;
						<@s.text name="cp.ptClearTime" />
						<@s.text name="cp.everyMD" />
						<input type="text" class="number" name="pcexDay" value="${(camTemp.pcexDay)!}"/> <@s.text name="cp.dayTime" />
						</span>
					</span>
				</p>
				<p>
					<span class="ui-icon icon-arrow-crm"></span>
					<span class="bg_title"><@s.text name="cp.specialVal" /></span>
					<span style="margin-left:10px;">
						<input type="checkbox" id="keysCheck" name="keysCheck" onclick="TEMP002.showSpeciKeys(this);" value="1" <#if (camTemp.keysCheck)! == "1">checked="checked"</#if>/>
						<label for="keysCheck" style="cursor:pointer;"><@s.text name="cp.specialValDesp" /></label><br/>
						<span id="keysText" <#if (camTemp.keysCheck)! != "1">class="hide"</#if>><textarea  cols="" rows="" name="speciKeys" style="width:500px;margin-left:20px;" >${(camTemp.speciKeys)!?html}</textarea> </span>
					</span>
				</p>
				<p>
					<span class="ui-icon icon-arrow-crm"></span>
					<span class="bg_title"><@s.text name="cp.yanqiTxt" /></span>
					<span style="margin-left:10px;" id>
						<input type="checkbox" id="yanqiCheck" name="yanqiCheck" value="1" onclick="TEMP002.showYanqi(this);" <#if (camTemp.yanqiCheck)! == "1">checked="checked"</#if> />
						<label for="yanqiCheck" style="cursor:pointer;"><@s.text name="cp.yanqiCondTxt" /></label>
						
					</span>
					<span style="margin-left:10px;" id="yanqiSpan" <#if (camTemp.yanqiCheck)! != "1"> class="hide" </#if>>
					<@s.text name="cp.yanqiNumTxt" />
					<input type="text" class="number" name="yanqiNum" value="${(camTemp.yanqiNum)!}"/>
						<@s.select style="width:60px;" list='#application.CodeTable.getCodes("1268")' listKey="CodeKey" 
						listValue="Value" value="${(camTemp.yanqiType)!?html}" name="yanqiType"/>
					</span>
					
				</p>
				<p id="yanqiPanel" <#if (camTemp.yanqiCheck)! != "1"> class="hide" </#if>>
				<span  style="margin-left:120px;">
							<@s.text name="cp.yanqiPrtTxt" />
							<select id="yanqiprt" name="yanqiprt" onchange="TEMP002.changeYanqiPrtType(this);">
							<option value="" <#if (camTemp.yanqiprt)! == ""> selected </#if> ><@s.text name="cp.yanqiPrtNoSelTxt" /></option>
							<option value="1" <#if (camTemp.yanqiprt)! == "1"> selected </#if>><@s.text name="cp.yanqiPrtSelTxt" /></option>
							<option value="4" <#if (camTemp.yanqiprt)! == "4"> selected </#if>><@s.text name="cp.yanqiPomSelTxt" /></option>
							</select>
							<span class="hide" id="prtselbtnSpan">
							<a class="add" onClick="TEMP002.showYanqiPrtTable(this);" id="prtselbtn">
								<span class="ui-icon icon-add"></span>
								<span class="button-text"><@s.text name="cp.yanqiPrtBtnTxt" /></span>
							</a>
							</span>
						<span id="prtSel_${camTemp.tempCode}_${index}" class="showPro_${(camTemp.tempCode)!?html}_${index}">
							<#if (camTemp.productList?? && camTemp.productList?size > 0) >
			          	<#list camTemp.productList as productList>
			          	<span class="span_BASE000001">
			          		<#if (camTemp.yanqiprt)! == "1">
			          				<input type="hidden" name="unitCode" value="${(productList.unitCode)!?html}" />
			          				<input type="hidden" name="barCode" value="${(productList.barCode)!?html}" />
			          				<input type="hidden" name="nameTotal" value="${(productList.nameTotal)!?html}" />
			          				<input type="hidden" name="proId" value="${(productList.proId)!?html}" />
			          				<span style="margin:0px 5px;">${(productList.nameTotal)!?html}</span>
			          			<#elseif (camTemp.yanqiprt)! == "4">
			          				<input type="hidden" name="prmId" value="${(productList.prmId)!?html}" />
			          				<input type="hidden" name="prmNameTotal" value="${(productList.prmNameTotal)!?html}" />
			          				<span style="margin:0px 5px;">${(productList.prmNameTotal)!?html}</span>
		          				</#if>
		          				<span class="close" onclick="CAMPAIGN_TEMPLATE_delete(this);return false;">
		          				<span class="ui-icon ui-icon-close"></span></span>
		          			</span>
		          		</#list>
			          	</#if>
			         	</span>
				</span>
				<br/>
				<br/>
				<span style="margin-left:120px;">
							<@s.text name="cp.yanqiPTTxt" /><input type="text" class="text" name="ptreson" value="${(camTemp.ptreson)!}" style="width:250px;"/>
				</span>
				</p>
			</div>
		</div>
		
	<#elseif camTemp.tempCode == "BUS008000">
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
                 <th scope="row"><@s.text name="cp.memclub" /></th>
                <td style="white-space: normal;"><span>${(campInfo.clubName)!?html}</span></td>
                </#if>
              </tr>
              <tr>
                <th><@s.text name="cp.ruleDate"/></th>
                <td <#if (memberClubId)! == "">colspan="3"</#if> ><label>${(campInfo.campaignFromDate)!?html}</label>
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
   <#-- 清零条件确认模板  -->
<#elseif camTemp.tempCode == "BASE008002">
	<div class="box2">
		<div class="box2-header clearfix">
			<strong class="left"><@s.text name="cp.pointClearCond" /></strong>
		</div>
		<div class="box2-content clearfix">
			<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointClearMemb" /></span>
				<#if (camTemp.memberLevelList?? && camTemp.memberLevelList?size > 0)>
			        <#list camTemp.memberLevelList as memberInfo>
			        	<#if "1" == (memberInfo.memberShowFlag)!>
			        		<label style="margin-left:20px;" class="gray">${(memberInfo.levelName)!?html}</label>
			        	</#if>
			        </#list>
		    	<#else>
		    		<label style="margin-left:20px;" class="gray"><@s.text name="cp.allPeo" /></label>
	        	</#if>
	        </p>
	        <p>
	        	<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.belongCounter" /></span>
				<#if ((camTemp.choicePlace)! != "0" && camTemp.checkNodes?? && camTemp.checkNodes?size > 0)>
			        <#list camTemp.checkNodes as actcounter>
			        	<label style="margin-left:20px;" class="gray">${(actcounter.name)!?html}</label>
			        </#list>
			    <#else>
			    	<label style="margin-left:20px;" class="gray">${application.CodeTable.getVal("1156","${(camTemp.choicePlace)!?html}")}</label>
		        </#if>
		    </p>
		</div>
	</div>
	
<#-- 清零方式确认模板  -->
<#elseif camTemp.tempCode == "BASE008003">
	<div class="box2">
		<div class="box2-header clearfix">
			<strong class="left"><@s.text name="cp.pointClearMed" /></strong>
		</div>
		<div class="box2-content clearfix">
			<p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.pointDate" /></span>
			    <label style="margin-left:20px;" class="gray"><@s.text name="cp.ptClearFromDate" /></label>
				<label style="color:red;">${application.CodeTable.getVal("1267","${(camTemp.pcFromDate)!?html}")}</label>
				<#if (camTemp.lengthKbn)! != "1">
		    	<label style="margin-left:20px;" class="gray"><@s.text name="cp.ptClearFixLength" /></label>
		    	<label style="color:red;">${(camTemp.clearNum)!}</label>
		    	<label class="gray">${application.CodeTable.getVal("1268","${(camTemp.numType)!?html}")}</label>
		    	&nbsp;
		    	<label class="gray">
		    	(
		    	<#if (camTemp.curKbn)! != "1">
			    	
			    	<@s.text name="cp.nocurdateCond" />
			    	<#else>
			    	<@s.text name="cp.curdateCond" />
			    	</#if>
			    	)
		    	</label>
		    	<#else>
		    	<label style="margin-left:20px;" class="gray"><@s.text name="cp.pcYearNumDesp" /></label>
		    	<label style="color:red;">${(camTemp.yearNo)!}</label><label class="gray"><@s.text name="cp.ptClearYear" /></label>
		    	<label style="color:red;">${(camTemp.disMonth)!}</label><label class="gray"><@s.text name="cp.month" /></label>
		    	<label style="color:red;">${(camTemp.disDay)!}</label><label class="gray"><@s.text name="cp.dayTime" /></label>
		    	</#if>
	  
	        </p>
	        <p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.ptClearExecDay" /></span>
			    <label style="margin-left:20px;" class="gray"><@s.text name="cp.ptClearDay" /></label>
				<label style="color:red;">${application.CodeTable.getVal("1269","${(camTemp.pcExecDate)!?html}")}</label>
				<span <#if (camTemp.pcExecDate)! != "2"> class="hide"</#if>>
		    	<label  style="margin-left:20px;" class="gray"><@s.text name="cp.ptClearTime" /></label>
		    	<label style="color:red;">${(camTemp.execMonth)!}</label>
		    	<label class="gray"><@s.text name="cp.month" /></label>
	  			<label style="color:red;">${(camTemp.execDay)!}</label>
		    	<label class="gray"><@s.text name="cp.dayTime" /></label>
		    	</span>
		    	<span <#if (camTemp.pcExecDate)! != "3"> class="hide"</#if>>
	  			<label style="color:red;">${(camTemp.pcexDay)!} 
	  			<@s.text name="cp.dayTime" />
	  			</label>
		    	
		    	</span>
	        </p>
	        <p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.specialVal" /></span>
			    <label style="margin-left:20px;" class="gray">
			    	<#if (camTemp.keysCheck)! != "1">
			    		<@s.text name="cp.noAffilRule" />
			    	<#else>
			    	${(camTemp.speciKeys)!?html}
			    	</#if>
			    </label>
	        </p>
	        <p>
				<span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.text name="cp.yanqiTxt" /></span>
			    <label style="margin-left:20px;" class="gray">
			    	<#if (camTemp.yanqiCheck)! != "1">
			    		<@s.text name="cp.noAffilRule" />
			    	<#else>
			    	<label class="gray"><@s.text name="cp.yanqiNumTxt" /></label>
			    	<label style="color:red;">${(camTemp.yanqiNum)!}</label>
			    	<label  style="margin-left:5px;" class="gray"><#if (camTemp.yanqiType)! == "0"><@s.text name="cp.monthNum" /> <#else> <@s.text name="text1" /> </#if> </label>
			    	</#if>
			    </label>
	        </p>
	        <#if (camTemp.yanqiCheck)! == "1">
	         <p>
	         	<span  style="margin-left:126px;">
							<label class="gray"><@s.text name="cp.yanqiPrtTxt" /></label>
							<#if (camTemp.productList?? && camTemp.productList?size > 0) >
			            <#list camTemp.productList as productList>
				          		<span class="span_BASE000001">
				          			<span class="bg_title">
				          			<#if (camTemp.yanqiprt)! == "1">
				          			${(productList.nameTotal)!?html}
				          			<#elseif (camTemp.yanqiprt)! == "4">
				          			${(productList.prmNameTotal)!?html}
				          			</#if>
				          			</span> 
				          		</span>
			   				</#list>
				         </#if>
				</span>
				<br/>
				<span style="margin-left:126px;">
							<label class="gray"><@s.text name="cp.yanqiPTTxt" /></label>
							<label style="color:red;">${(camTemp.ptreson)!} </label>
				</span>
	         </p>
	       </#if>
		</div>
	</div>
<#-- 促销活动  -->
	<#elseif camTemp.tempCode == "BUS000051">
	<div class="box4" id="${camTemp.tempCode}_${index}">
		<div class="box4-header">
			<strong><@s.text name="cp.pointType" />&nbsp:&nbsp<@s.text name="cp.actRule" /></strong>
		</div>
		<div class="box4-content">
	        <input type="hidden" name="tempCode" value="${(camTemp.tempCode)!?html}"/>
	        <input type="hidden" name="groupCode" value="${(camTemp.groupCode)!?html}"/>
        	<input type="hidden" name="RULE_TEMP_FLAG" value="1"/>
	    	<input type="hidden" name="ruleId" value="${(camTemp.ruleId)!?html}"/>
	        <input type="hidden" name="selPrtIndex" value="${camTemp.tempCode}_${index}"/>
	        <table class="detail" >
	        	<tr>
	        		<th><@s.text name="cp.actRuleJoin" /></th>
	        		<td>
	 
						<a class="add left" onClick="TEMP002.popActivityList('${searchCampaignInitUrl}',this);return false;">
							<span class="ui-icon icon-add"></span>
							<span class="button-text"><@s.text name="cp.actSel" /></span>
						</a>
						<span id="campaignDiv" style="line-height: 18px;">
						<#if (camTemp.actList?? && camTemp.actList?size > 0) >
			          	<#list camTemp.actList as actList>
			          		<span class="span_act">
			          			<input type="hidden" name="actCode" value="${(actList.actCode)!}"/>
			          			<input type="hidden" name="actName" value="${(actList.actName)!}"/>
			          			${(actList.actName)!}
			          			<span class="close" style="margin: 0 10px 2px 5px;" onclick="TEMP002.delActHtml(this);">
			          			<span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
			          		</span>
		   				</#list>
			          	</#if>
					 	</span>
	        		</td>
	        		<th></th>
	        		<td>
	        		</td>
	        	</tr>
				<#list camTemp.combTemps as combTemp>
		        	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
				</#list>
	        </table>
		</div>
	</div>
<#-- 促销活动确认画面  -->
	<#elseif camTemp.tempCode == "BUS000052">
	<div class="box2">
	<div class="box2-header clearfix">
		<strong class="left"><@s.text name="cp.step005" /></strong>
	</div>
	<div class="box2-content">
		<p>
			<label><@s.text name="cp.actRuleJoin" /></label>
			<#if (camTemp.actList?? && camTemp.actList?size > 0) >
            <#list camTemp.actList as actList>
	          		<span>
	          			<span class="bg_title">
	          			${(actList.actName)!}
	          			</span> 
	          		</span>
   				</#list>
	         </#if>
		  </p>
	        <#list camTemp.combTemps as combTemp>
		        <@template index=index + "_" + combTemp_index camTemp=combTemp/>
			</#list>
	</div>
	</div>
</#if>
</@s.i18n>
