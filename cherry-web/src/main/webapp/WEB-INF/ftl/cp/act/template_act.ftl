<@s.i18n name="i18n.cp.BINOLCPCOM01">
	<#-- **************************************  模板配置开始  **************************************-->
	<#assign boxNo = 0/>
	<#-- =================创建活动================= -->
	<#if camTemp.tempCode == "BASE000053">
	 	<div class="box4" id="BASE000053_${index}">
	 		<#-- 共通Hidden值 -->
	 		<div class="hide" id="comParams">
	 			<@hideMetadata campTemplate=camTemp/>
	 		</div>
		 	<div class="box4-header">
		 		<strong><@s.text name="cp.setStep"/><@s.text name="cp.step101" /></strong>
				<a class="add right" onClick="ACT.addItem('#subItem');">
					<span class="ui-icon icon-add"></span>
					<span class="button-text"><@s.text name="cp.addAct" /></span>
				</a>
			</div>
			
			<div id="campContext" class="box4-content">
	            <ul class="sortable">
	            <#if camTemp.campList?exists && camTemp.campList?size gt 0>
	            	<#-- 是否隐藏删除按钮 -->
	            	<#assign hideDel = false/>
	            	<#if camTemp.campList?size == 1><#assign hideDel = true/></#if>
					<#list camTemp.campList as camp>
						<@getCampItem camp=camp hideDel=hideDel tempMap=camTemp/>
					</#list>
				<#else>
					<@getCampItem camp=camTemp.defSubCamp hideDel=true tempMap=camTemp/>
				</#if>
	            </ul>
	        </div>
	        <div class="hide" id="subItem">
	        	<@getCampItem camp={} hideDel=false tempMap=camTemp/>
			</div>
		</div>
	<#-- =================设置活动阶段主模板=================  -->
	<#elseif camTemp.tempCode == "BUS000043">
		<div class="box4" id="BUS000043_${index}">
			<#-- 共通Hidden值 -->
	 		<div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
        	<div class="box4-header"><strong><@s.text name="cp.setStep"/><@s.text name="cp.step102" /></strong></div>
        	<div class="box4-content">
          		<div class="clearfix" style="padding:0px;">
            		<#list camTemp.combTemps as combTemp>
			          	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
			        </#list>
         		</div>
        	</div>
      	</div>
    <#-- =================活动列表基础模板=================  -->
	<#elseif camTemp.tempCode == "BASE000055">
		<div style="width:20%;" class="left" id="BASE000055_${index}">
		<div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
		  <div class="box2-active clearfix">
	        <div class="box2-content clearfix">
	          <div class="left clearfix" style="width:100%;">
	          	<strong class="left" style="width:100%; padding-bottom:5px; border-bottom: 1px solid #E5E5E5;">
	          		<span class="ui-icon icon-medal-detail"></span>
	          		<@s.text name="cp.actList" />
	          	</strong>
				<@getCampList campList=camTemp.campList/>
	          </div>
	        </div>
	      </div>
	    </div>
	<#-- =================设置活动阶段模板=================  -->
	<#elseif camTemp.tempCode == "BASE000056">
		<div style="width:80%;" class="right" id="BASE000056_${index}">
          <div class="ml1">
            <div class="group-header clearfix">
                <input type="checkbox" id="allChecked" name="allChecked" value="1" onclick="ACT.setAll(this);"><label for="allChecked"><@s.text name="cp.setSameConfig" /></label>
            	<a style="margin:2px 5px 0 5px;" class="add right" onclick="ACT.openCampTimeDialog(this);">
            		<span style="left: 0; position: absolute;" class="ui-icon icon-add"></span>
            		<span style="position: static;" class="button-text">
            		<@s.text name="global.page.add" /><@s.text name="cp.step102" />
            		</span> 
            	</a>
            </div>
            <@getPopTime timeDialog="" />
            <#-- 共通Hidden值 -->
            <div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
            <div id="campContext" class="group-content box2-active clearfix">
        		<#-- 活动阶段块 -->
        		<#if camTemp.campList?exists && camTemp.campList?size gt 0>
	        		<@getTimeList campList=camTemp.campList />
				</#if>
            </div>
          </div>
        </div>
    <#-- =================设置活动范围模板=================  -->
	<#elseif camTemp.tempCode == "BUS000045">
		<div class="box4" id="BUS000045_${index}">
			<#-- 共通Hidden值 -->
	 		<div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
        	<div class="box4-header"><strong><@s.text name="cp.setStep"/><@s.text name="cp.step103" /></strong></div>
        	<div class="box4-content">
          		<div class="clearfix" style="padding:0px;">
            		<#list camTemp.combTemps as combTemp>
			          	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
			        </#list>
         		</div>
        	</div>
      	</div>
    <#-- =================设置活动范围子模板=================  -->
	<#elseif camTemp.tempCode == "BASE000059">
		<script>
			$(function() {
				// 活动范围树初始化
				CHERRYTREE.initTrees('${(camTemp.placeJson)!}');
			});
		</script>
		<div style="width:80%;" class="right" id="BASE000059_${index}">
          <div class="ml1">
            <div class="group-header clearfix">
                  <input type="checkbox" id="allChecked" name="allChecked" value="1" onclick="ACT.setAll(this);"><label for="allChecked"><@s.text name="cp.setSameConfig" /></label>
            </div>
            <div class="hide"><span id="errMsg_1"><@s.text name="cp.errorMsg" /></span></div>
            <#-- 共通Hidden值 -->
            <div class="hide" id="comParams">
            	<@hideMetadata campTemplate=camTemp/>
            </div>
            <div id="campContext" class="group-content box2-active clearfix">
        		<#-- 活动范围块 -->
        		<#if camTemp.campList?exists && camTemp.campList?size gt 0>
        			<@getCampPlaceList campList=camTemp.campList />
        		</#if>
            </div>
          </div>
        </div>
        <#-- =================设置活动对象模板=================  -->
	<#elseif camTemp.tempCode == "BUS000046">
		<div class="box4" id="${camTemp.tempCode}_${index}">
			<#-- 共通Hidden值 -->
	 		<div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
        	<div class="box4-header"><strong><@s.text name="cp.setStep"/><@s.text name="cp.step104" /></strong></div>
        	<div class="box4-content">
          		<div class="clearfix" style="padding:0px;">
            		<#list camTemp.combTemps as combTemp>
			          	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
			        </#list>
         		</div>
        	</div>
      	</div>
      	<#-- =================设置活动对象基础模板=================  -->
	<#elseif camTemp.tempCode == "BASE000060">
		<div style="width:80%;" class="right" id="BASE000060_${index}">
          <div class="ml1">
            <div class="group-header clearfix"></div>
            <#-- 共通Hidden值 -->
            <div class="hide"><span id="errMsg_1"><@s.text name="cp.errorMsg" /></span></div>
            <div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
            <div id="campContext" class="group-content box2-active clearfix">
        		<#-- 活动对象模板块-->
        		<#if camTemp.campList?exists && camTemp.campList?size gt 0>
        			<@getCampMebList campList=camTemp.campList />
        		</#if>
            </div>
          </div>
        </div>
         <#-- =================设置活动奖励大模板=================  -->
	<#elseif camTemp.tempCode == "BUS000047">
		<div class="box4" id="BUS000047_${index}">
			<#-- 共通Hidden值 -->
	 		<div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
        	<div class="box4-header"><strong><@s.text name="cp.setStep"/><@s.text name="cp.step105" /></strong></div>
        	<div class="box4-content">
          		<div class="clearfix" style="padding:0px;">
            		<#list camTemp.combTemps as combTemp>
			          	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
			        </#list>
         		</div>
        	</div>
      	</div>
      	<#-- =================设置活动奖励基础模板=================  -->
	<#elseif camTemp.tempCode == "BASE000061">
		<div id="deliveryType_hide" class="hide"><@getDeliveryType deliveryList=application.CodeTable.getCodes("1328") deliveryFlag=camTemp.deliveryFlag!/></div>
		<div style="width:80%;" class="right" id="BASE000061_${index}">
          <div class="ml1">
            <div class="group-header clearfix"></div>
            <#-- 共通Hidden值 -->
            <div class="hide" id="comParams"><@hideMetadata campTemplate=camTemp/></div>
            <input type="hidden" id="groupFlag" value="${(camTemp.groupFlag)!}" />
            <div id="campContext" class="group-content box2-active clearfix">
            <@getCampGiftList tempMap=camTemp/>
            </div>
          </div>
        </div>
		<#macro getLogicBtn logicOpt='AND'>
		 <#--
		 <div class="LOGICBTN">
			<button onclick="ACT.changeLogicOpt(this);return false;" class="button_${logicOpt}" type="button">
				<span class="text">${logicOpt}</span>
			</button>
		 </div>
		-->
		<div class="LOGICBTN">
			<button class="button_${logicOpt}" type="button">
				<span class="text">${logicOpt}</span>
			</button>
		</div>
		</#macro>
		<div class="hide">
			<div id="BOX_AND"><@getGiftBox boxInfo={"logicOpt":"AND","prtList":[]} boxNo=boxNo virtPrmFlag=camTemp.virtPrmFlag!/></div>
			<div id="BOX_OR"><@getGiftBox boxInfo={"logicOpt":"OR","prtList":[]} boxNo=boxNo virtPrmFlag=camTemp.virtPrmFlag!/></div>
			<div id="LOGIC_BTN"><@getLogicBtn /></div>
		</div>
    <#-- *******************预览与确认模板******************* -->
	<#elseif camTemp.tempCode == "BUS000048">
		<div class="box4" id="BUS000048_${index}">
        	<div class="box4-header"><strong><@s.text name="cp.setStep"/><@s.text name="cp.step106" /></strong></div>
        	<div class="box4-content">
          		<div class="clearfix" style="padding:0px;">
            		<#list camTemp.combTemps as combTemp>
			          	<@template index=index + "_" + combTemp_index camTemp=combTemp/>
			        </#list>
         		</div>
        	</div>
      	</div>
    <#-- *******************预览与确认模板******************* -->
	<#elseif camTemp.tempCode == "BUS000049">
	<div style="width:80%;" class="right" id="campContext" >
		<#if camTemp.campList?exists >
			<#list camTemp.campList as camp>
			<div <#if camp_index!= camp.curIndex!0>class="hide"</#if>>
	          <div class="ml1">
	            <div class="group-header clearfix">${camp.campName!}</div>
	            <div class="group-content box2-active clearfix">
	            	<#list camTemp.combTemps as combTemp>
			          	<@template index=index + "_" + combTemp_index camTemp=combTemp campMap=camp campIndex=camp_index/>
			        </#list>
	            </div>
	          </div>
	        </div>
	        </#list>
        </#if>
    </div>
    <#-- *******************活动阶段确认模板******************* -->
	<#elseif camTemp.tempCode == "BASE000057">
 	<@getCampStage_CFM campMap=campMap />
  	<#-- *******************活动范围确认模板******************* -->
	<#elseif camTemp.tempCode == "BASE000066">   
    <@getCampRange_CFM  campMap=campMap />
	<#-- *******************活动对象确认模板******************* -->
	<#elseif camTemp.tempCode == "BASE000065">
	<@getCampObject_CFM  campMap=campMap campIndex=campIndex/>
  	<#-- *******************活动奖励确认模板******************* -->
	<#elseif camTemp.tempCode == "BASE000067">  
    <@getCampGiftList_CFM  campMap=campMap/>
    <#-- *******************活动基本内容确认模板******************* -->
	<#elseif camTemp.tempCode == "BASE000068">   
    <@getCampBaseInfo_CFM  campMap=campMap/>
 	</#if>
	<#-- **************************************模板配置结束 ************************************** -->
</@s.i18n>
