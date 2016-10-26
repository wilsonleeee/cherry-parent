<#-- ========================================== 模板宏定义开始  =========================================-->
<#-- *******取得子活动列表模块****** -->
<#-- campList 	:子活动列表			-->
<#-- allSelected:是否全选 			-->
<#-- ****************************** -->
<#macro getCampList campList>
    <div class="bg1 left clearfix" style="width:100%;">
      <ul id="campList">
      	<#list campList as camp>
      		<li class="<#if camp_index == camp.curIndex!0>on cMark</#if>" 
      			onclick="ACT.setCamp(this,'${camp_index}');return false;">
      			<span class="ui-icon icon-arrow-crm"></span>${camp.campName!?html}
      		</li>
      	</#list>
      </ul>
    </div>
</#macro>
<#-- ********取得子活动模块********* -->
<#-- camp    :子活动 				-->
<#-- index   :子活动索引 				-->
<#-- hideDel :是否隐藏删除按钮		-->
<#-- ****************************** -->
<#macro getCampItem camp hideDel tempMap>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
	<#assign enEditFlag = true/>
</#if>
<li class="border FORM_CONTEXT">
    <table class="detail" style="float:left;width:90%; margin-bottom: 0px;">
      <tbody>
	      <#if tempMap.topLimitFlag! == 1 >
		  	<@getCampItem_Limit camp=camp  hideDel=hideDel  enEditFlag=enEditFlag />
		  <#else>
		  	<#if tempMap.campaignType! == 'DHHD'>
		  		<@getCampItem_DHHD  camp=camp  hideDel=hideDel  enEditFlag=enEditFlag />
		  	<#else>
		  		<@getCampItem_COMM  camp=camp  hideDel=hideDel  enEditFlag=enEditFlag />
		  	</#if>
		  </#if>
      </tbody>
    </table>
    <div class="clearfix">
    	<a style="display:<#if hideDel>none<#else>block</#if>; margin-top:
    	<#if tempMap.topLimitFlag! == 1 >38px<#else><#if tempMap.campaignType! == 'DHHD'>28px<#else>5px</#if></#if>;" onClick="ACT.removeItem(this);return false;" class="add right deleteClass">
    		<span class="ui-icon icon-delete"></span>
			<span class="button-text"><@s.text name="cp.delete" /></span>
    	</a>
    </div>
</li>
</#macro>
<#-- ********取得子活动模块********* -->
<#-- camp    :子活动 				-->
<#-- index   :子活动索引 				-->
<#-- hideDel :是否隐藏删除按钮		-->
<#-- ****************************** -->
<#macro getCampItem_COMM camp hideDel enEditFlag=false>
<tr>
  <th>
  	<@s.text name="cp.actName" /><span class="highlight"><@s.text name="global.page.required"/></span>
  	<input type="hidden" name="SUBCAMP_RULETYPE" value="S00001"/>
  	<input type="hidden" name="SUBCAMP_DETAILNO" value="${(camp.SUBCAMP_DETAILNO)!'0'}"/>
  	<#if camp.campaignRuleId?exists>
  	<input type="hidden" name="campaignRuleId" value="${(camp.campaignRuleId)!}"/>
  	</#if>
  </th>
  <td><span><input type="text" value="${camp.campName!?html}" name="campName" class="text textLong"/></span></td>
  <th rowspan="3"><@s.text name="cp.actDes" /></th>
  <td rowspan="3"><span><input name="description" class="text" style="width: 300px;" value="${(camp.description)!?html}"/></span></td>
</tr>
</#macro>
<#-- ********取得子活动模块********* -->
<#-- camp    :子活动 				-->
<#-- index   :子活动索引 				-->
<#-- hideDel :是否隐藏删除按钮		-->
<#-- ****************************** -->
<#macro getCampItem_Limit camp hideDel enEditFlag=false>
<tr>
  <th>
  	<@s.text name="cp.actName" /><span class="highlight"><@s.text name="global.page.required"/></span>
  	<input type="hidden" name="SUBCAMP_RULETYPE" value="S00001"/>
  	<input type="hidden" name="SUBCAMP_DETAILNO" value="${(camp.SUBCAMP_DETAILNO)!'0'}"/>
  	<#if camp.campaignRuleId?exists>
  	<input type="hidden" name="campaignRuleId" value="${(camp.campaignRuleId)!}"/>
  	</#if>
  </th>
  <td ><span><input type="text" value="${camp.campName!?html}" name="campName" class="text textLong"/></span></td>
  <th rowspan="3"><@s.text name="cp.actDes" /></th>
  <td rowspan="3"><span><textarea  name="description" class="text" style="width:300px;height:55px;overflow-y:auto" rows="1" value="${(camp.description)!?html}">${(camp.description)!?html}</textarea></span></td>
</tr>
<tr>
  <th><@s.text name="cp.times" /></th>
  <td><span><input name="times" maxlength="8" class="text textLong"  value="${(camp.times)!?html}"/></span></td>
</tr>
<tr>
  <th><@s.text name="cp.topLimit" /></th>
  <td><span><input name="topLimit" maxlength="8" class="text textLong"  value="${(camp.topLimit)!?html}"/></span></td>
</tr>
</#macro>
<#-- ********取得子活动模块********* -->
<#-- camp    :子活动 				-->
<#-- index   :子活动索引 				-->
<#-- hideDel :是否隐藏删除按钮		-->
<#-- ****************************** -->
<#macro getCampItem_DHHD camp hideDel enEditFlag=false>
<tr>
  <th>
  	<@s.text name="cp.actName" /><span class="highlight"><@s.text name="global.page.required"/></span>
  	<input type="hidden" name="SUBCAMP_RULETYPE" value="S00001"/>
  	<input type="hidden" name="SUBCAMP_DETAILNO" value="${(camp.SUBCAMP_DETAILNO)!'0'}"/>
  	<#if camp.campaignRuleId?exists>
  	<input type="hidden" name="campaignRuleId" value="${(camp.campaignRuleId)!}"/>
  	</#if>
  </th>
  <td ><span><input type="text" value="${camp.campName!?html}" name="campName" class="text textLong"/></span></td>
  <th rowspan="2"><@s.text name="cp.actDes" /></th>
  <td rowspan="2"><span><textarea  name="description" class="text" style="width:300px;height:55px;overflow-y:auto" rows="1" value="${(camp.description)!?html}">${(camp.description)!?html}</textarea></span></td>
</tr>
<tr>
  <th><@s.text name="cp.times" /></th>
  <td><span><input name="times" maxlength="8" class="text textLong"  value="${(camp.times)!?html}"/></span></td>
</tr>
</#macro>
<#-- *******取得子活动阶段子模块****** -->
<#-- index  	:子活动索引 			  -->
<#-- time  		:活动阶段 			  -->
<#-- ******************************** -->
<#macro getTimeItem index time camp>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
	<#assign enEditFlag = true/>
</#if>
	<li id="${time.timeType!?html}" class="border <#if time.fromDate==""&& time.toDate=="">hide</#if>" style="margin:0px 0px 10px 0px;">
	  <table class="detail" cellpadding="0" cellspacing="0" style="float:left;width:85%; margin-bottom: 0px;">
          <thead>
            <tr>
              <th style="width:10%;"><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><@s.property value="#application.CodeTable.getVal('1195','${time.timeType!?html}')"/></span></th>
              <td>
              <#if enEditFlag || (time.fromDate=="" && time.toDate==""||time.status == "0")>
              <span>
              	<span>
              	<input class="date" id="campFromDate_${index!}_${time.timeType!?html}" name="fromDate" value="${time.fromDate!?html}"/>
              	</span>
	  			<span style="margin: 0 5px;"><@s.text name="cp.to" /></span>
	  			<span>
	  			<input class="date" id="campToDate_${index!}_${time.timeType!?html}" name="toDate" value="${time.toDate!?html}"/>
	  			</span>
	  			<input type="hidden" name="timeType" value="${time.timeType!?html}" />
	  			<input type="hidden" name="status" value="${time.status!?html}" />
              </span>
              <#else>
               <span>
	  			<span>${time.fromDate!}<input class="text" type="hidden" name="fromDate" value="${time.fromDate!?html}"/></span>
	  			<span style="margin: 0 5px;"> <@s.text name="cp.to" /></span>
	  			<span>${time.toDate!}<input class="text" type="hidden" name="toDate" value="${time.toDate!?html}"/></span>
                </span>
  	  			<input type="hidden" name="timeType" value="${time.timeType!?html}"/>
	  			<input type="hidden" name="status" value="${time.status!?html}" />
              </#if>
              </td>
            </tr>
          </thead>
        </table>
        <div class="clearfix">
        	<#if time.status == "0">
            <a style="display: block; margin-top:5px;" onClick="ACT.removeCampTime(this);return false;" class="add right deleteClass">
            	<span class="ui-icon icon-delete"></span>
            	<span class="button-text"><@s.text name="global.page.delete" /></span>                    
            </a>
            </#if>                     
        </div>
    </li>
</#macro>
<#-- *******取得子活动阶段模块****** -->
<#-- campList : 子活动列表	   	    -->
<#-- ****************************** -->
<#macro getTimeList campList>
	<#list campList as camp>
		<div class="<#if camp_index!=0>hide</#if>">
			<div class="FORM_CONTEXT">
				<#-- 取得会员活动共通hidden -->
				<@getCampHideDiv camp=camp/>
				<div class="EN_SAME"><input type="hidden" value="" id="timeList_${camp_index}" name="timeList"></div>
			</div>
		    <ul class="sortable">
		    	<#if camp.timeList?exists>
		    		<#list camp.timeList as time>
		    		<@getTimeItem index=camp_index time=time camp=camp />
		    		</#list>
		    	</#if>	
		    </ul>
		</div>
	</#list>
</#macro>
<#-- ****添加活动阶段弹出框模块****** -->
<#macro getPopTime  timeDialog >
<div id="PopDialogMain" >
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
</#macro>
<#-- *******取得子活动范围模块****** -->
<#-- campList 	:子活动列表		  	-->
<#-- ****************************** -->
<#macro getCampPlaceList campList>
	<#list campList as camp>
		<#--可编辑Flag-->
		<#assign enEditFlag = false/>
		<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
			<#assign enEditFlag = true/>
		</#if>
		<div id="campContext_${camp_index}" class="<#if camp_index!= camp.curIndex!0>hide</#if>">
		    <div class=" FORM_CONTEXT">
		    	<#-- 取得会员活动共通hidden -->
				<@getCampHideDiv camp=camp/>
		    	<div style="margin-top:0px;" class="relation clearfix EN_SAME">
					<span class="left" style="margin-left:5px;">
						<@s.text name="cp.actPlaceType" />
						<#if enEditFlag>
						<select id="locationType_${camp_index}" name="locationType" onchange="CHERRYTREE.changeType(this,${camp_index});" style="margin-left:10px;width:120px;">
							<option value=""><@s.text name="global.page.select"/></option>
							<#list application.CodeTable.getCodes("1156") as code>
								<option <#if code.CodeKey==camp.locationType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
						  	</#list>
						</select>
						<#else>
						<select id="locationType_${camp_index}" name="locationType" disabled="disabled" style="margin-left:10px;width:120px;">
							<#list application.CodeTable.getCodes("1156") as code>
								<option <#if code.CodeKey==camp.locationType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
						  	</#list>
						</select>
						<input type="hidden" name="locationType" value="${camp.locationType!}" />
						</#if>
					</span>
					<span id="importDiv_${camp_index}" class="left <#if (camp.locationType)!?html != "5">hide</#if>" style="margin-left:10px;">
						<a href="/Cherry/download/柜台信息模板.xls"><@s.text name="cp.downModel" /></a>
						<input class="input_text" type="text" id="pathExcel_${camp_index}" name="pathExcel"/>
					    <input type="button" value="<@s.text name="global.page.browse"/>"/>
					    <input class="input_file" style="height:auto;" type="file" name="upExcel" id="upExcel_${camp_index}" size="40" onchange="pathExcel_${camp_index}.value=this.value;return false;" /> 
		        		<input type="button" value="<@s.text name="cp.importExcel_btn"/>" onclick="CHERRYTREE.ajaxFileUpload('${camp_index}');return false;"/>
		        		<img id="loadingImg_${camp_index}" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
					</span>
					<input type="hidden" name="placeJson" id="placeJson_${camp_index}" value=""/>
	          		<input type="hidden" name="saveJson" id="saveJson_${camp_index}" value=""/>
			    </div>
		  		<div class="ztree jquery_tree" style="padding:0px;">
		            <div class="box2-header clearfix" >
		            	<strong class="left active"><span class="ui-icon icon-flag"></span><@s.text name="cp.step004" /></strong>
		            	<div  id="locationId_${camp_index}">
		            		<span style="margin-left:10px;">
		            			<input type="checkbox" id="selectAll_${camp_index}" onchange="CHERRYTREE.selectAll(this,${camp_index});"/>
		            			<label for="selectAll_${camp_index}"><@s.text name="global.page.selectAll" /></label>
		            		</span>
			            	<a class="search right" onclick="CHERRYTREE.getPosition(${camp_index});return false;">
								<span class="ui-icon icon-position"></span>
								<span class="button-text"><@s.text name="cp.position" /></span>
							</a>
							<input type="text" id="locationPositiontTxt_${camp_index}" class="text right locationPosition ac_input" >
						</div>
		          	</div>
		            <div style="overflow:auto;height:300px;" class="jquery_tree treebox tree" id="tree_${camp_index}"></div>
		        </div>
		  	</div>
		</div>
	</#list>
</#macro>
<#-- *******活动对象块************ -->
<#-- campList 	:子活动列表		  -->
<#-- **************************** -->
<#macro getCampMebList campList>
<#list campList as camp>
<div id="campContext_${camp_index}" class="<#if camp_index!= camp.curIndex!0>hide</#if>">
	<#assign conInfoDivShow = false/>
	<#assign campMebType = camp.campMebType!/>
	<#if campMebType == "1" || campMebType == '2'><#assign conInfoDivShow = true/></#if>
	<div class="FORM_CONTEXT">
		<#-- 取得会员活动共通hidden -->
		<@getCampHideDiv camp=camp/>
		<#-- ======对象类型======  -->
		<@getCampMebTypeBox camp=camp index=camp_index/>
	</div>
	<div id="conInfoDiv_${camp_index}" class="relation clearfix" style="margin-top:0px;<#if !conInfoDivShow >display:none;</#if>">
		<p id="searchCondition_${camp_index}" class="left green" style="width:75%;text-align:left;margin:0px 5px;">
			<#if conInfoDivShow>
				<@s.action name="BINOLCM33_conditionDisplay" namespace="/common" executeResult=true>
	       			<@s.param name="reqContent">${camp.campMebInfo!}</@s.param>
	       		</@s.action>
	       	</#if>	
		</p>
	</div>
	<div id="mebResult_div_${camp_index}" class="group-content box2-active hide">
        <div class="section hide" id="memberInfo_${camp_index}">
		  <div class="section-content">
		    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table"
		    width="100%" id="memberDataTable_${camp_index}">
		      <thead>
		        <tr>
		          <th><@s.text name="cp.memObjectType" /></th>
		          <th><@s.text name="cp.memCardCode" /></th>
		          <th><@s.text name="cp.memName" /></th>
		          <th><@s.text name="cp.memMobile" /></th>
		          <th><@s.text name="cp.brithDay" /></th>
		          <th><@s.text name="global.page.joinDate" /></th>
		          <th><@s.text name="global.page.changablePoint" /></th>
		          <th class="center"><@s.text name="cp.isReceiveMsg" /></th>
		        </tr>
		      </thead>
		      <tbody>
		      </tbody>
		    </table>
		  </div>
		</div>
	</div>
</div>
</#list>
</#macro>
<#-- *********活动对象类型******** -->
<#-- index 	:子活动列表索引		  -->
<#-- **************************** -->
<#macro getCampMebTypeBox camp index>
<#assign campMebType = '0'/>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
	<#assign enEditFlag = true/>
</#if>
<div class="relation clearfix EN_SAME" style="margin-top:0px;">
	<#-- ======对象类型======  -->
	<span style="margin:3px 5px;" class="left">
		<span style="margin:3px 10px;" class="left">
			<@s.text name="cp.actObjectType" />
		</span>
	<@getCampMebType camp=camp index=index enEditFlag=enEditFlag/>
	</span>
	<#-- 选择搜索条件-->
	<span id="linkMebSearch_${index}" class="<#if !(campMebType == '1' || campMebType == '2')>hide</#if>">
		<a id="importLink_${index}" onclick="ACT.searchConDialog(this,${index});return false;" class="search left" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.choice" /></span>
    	</a>
	</span>
	<#-- 导入弹出框-->
	<span id="linkMebImport_${index}" class="<#if campMebType != '3'>hide</#if>">
		<a id="memImportLink_${index}" onclick="ACT.popMemImport();return false;" class="search left" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.choice" /></span>
    	</a>
	</span>
	<#-- 活动对象数量显示-->
	<span id="memCountShow_${index}" class="left ShowCount  <#if 0 == camp.total!0>hide</#if>" style="margin:5px 20px;">
		<@s.text name="cp.memCount" />
		<strong id="memCount_${index}" class="green"><@s.text name="format.number"><@s.param value="${camp.total!0}"></@s.param></@s.text></strong>
		<span id="yugu_${index}" class="red <#if campMebType != '1'>hide</#if>">[<@s.text name="global.page.estimate" />]</span>
	</span>
	<#-- 查询活动对象-->
	<span id="searchMeb_${index}" class="right <#if campMebType == '0' || campMebType == '5' || campMebType =='6'>hide</#if>">
		<a onclick="ACT.memSearch(${index});return false;" class="search" style="margin: 3px 0 0 20px;">
      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.memSearch" /></span>
    	</a>
	</span>
	<#-- 活动对象JSON-->
	<input type="hidden" id="campMebInfo_${index}" name="campMebInfo" value="${(camp.campMebInfo)!?html}"/>
	<div class="SEARCHCODE"><input type="hidden" id="searchCode_${index}" name="searchCode" value="${(camp.searchCode)!?html}"/></div>
	<div class="hide" id="objDialogTitle"><@s.text name="objDialogTitle" /></div>
	<div class="hide" id="dialogConfirm"><@s.text name="dialogConfirm" /></div>
	<div class="hide" id="dialogCancel"><@s.text name="dialogCancel" /></div>
</div>
</#macro>
<#-- 会员信息搜索弹出页面 -->
<div class="hide" id="searchCondialogInit"></div>

<#-- *****活动奖励***** -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getComGiftItem camp index virtPrmFlag='1' prmCate='' groupFlag=''>
<#if groupFlag=='1'><@getComGiftItem1 camp=camp index=index virtPrmFlag=virtPrmFlag prmCate=prmCate/>
<#else><@getComGiftItem0 camp=camp index=index virtPrmFlag=virtPrmFlag prmCate=prmCate/>
</#if>
</#macro>
<#-- *****活动奖励礼品集合***** -->
<#macro getGiftTable list enEditFlag tbodyId>
	<table cellspacing="0" cellpadding="0" style="width: 100%;">
        <thead>
			<tr>
			  <th><@s.text name="cp.giftunitCode" /></th>
			  <th><@s.text name="cp.giftName" /></th>
			  <th><@s.text name="cp.giftbarCode" /></th>
			  <th><@s.text name="cp.giftPrice" /></th>
			  <th><@s.text name="cp.giftNum" /></th>
			  <th><@s.text name="cp.deliveryType" /></th>
			  <#if enEditFlag><th class="center"><@s.text name="cp.actionCol" /></th></#if>
			</tr>
	  </thead>
	  <tbody id="${tbodyId}" class="prtBody"> 
		<#list list as prt>
			<tr <#if prt.prmCate?exists &&(prt.prmCate="DHCP" || prt.prmCate="DHMY" || prt.prmCate="TZZK")>class="gray"</#if> >
				<td class="hide">
					<input type="hidden" name="<#if prt.prtType! == 'P' >prmVendorId<#else>prtVendorId</#if>" value="${(prt.proId)!}"/>
					<input type="hidden" name="prmCate" value="${(prt.prmCate)!?html}"/>
					<input type="hidden" name="price" value="${(prt.price)!'0'}"/>
					<input type="hidden" name="unitCode" value="${(prt.unitCode)!?html}"/>
					<input type="hidden" name="barCode" value="${(prt.barCode)!?html}"/>
				</td>
				<td>${(prt.unitCode)!?html}</td>
				<td style="white-space:normal">${(prt.nameTotal)!?html}</td>
				<td>${(prt.barCode)!?html}</td>
				<td>
					<#if prt.prmCate?exists &&(prt.prmCate="DHCP" || prt.prmCate="DHMY" || prt.prmCate="TZZK")>
						<@s.text name="format.price"><@s.param value="${(prt.price!'0')?number *-1}"></@s.param></@s.text>
					<#else>
						<@s.text name="format.price"><@s.param value="${(prt.price!'0')?number}"></@s.param></@s.text >
					</#if>
				</td>
				<#if enEditFlag>
				<td><span><input class="number" name="quantity" value='${(prt.quantity)!}'  style="text-align:right;"/></span></td>
				<td><@getDeliveryType deliveryList=prt.deliveryList![] editFlag=enEditFlag /></td>
				<td class="center"><a onClick="$(this).parent().parent().remove();return false;" style="cursor:pointer;"><@s.text name="cp.delete" /></a></td>
				<#else>
				<td>${(prt.quantity)!}<input type="hidden" name="quantity" value='${(prt.quantity)!}' /></td>
				<td><@getDeliveryType deliveryList=prt.deliveryList![] editFlag=enEditFlag /></td>
				</#if>
			</tr>
		</#list>	
	  </tbody>
	</table>
</#macro>
<#--配送方式-->
<#macro getDeliveryType deliveryList=[] editFlag=true deliveryFlag='1'>
<#if deliveryFlag=='1'>
	<#assign v_deliveryType=""/>
	<#list deliveryList as delivery>
		<#if editFlag>
			<input type="checkbox" <#if (delivery.checked)!false>checked="true"<#assign v_deliveryType>${v_deliveryType}_${delivery.CodeKey}</#assign></#if>
			onclick="ACT.setDeliveryType(this);" value="${(delivery.CodeKey)!}" />
			<label>${delivery.Value}</label>
			<#if delivery_has_next><br/></#if>
		<#else>
			<#if (delivery.checked)!false><#assign v_deliveryType>${v_deliveryType}_${delivery.CodeKey}</#assign>
				<input type="checkbox" checked="true" disabled="true"/>
				<label>${delivery.Value}</label>
				<#if delivery_has_next><br/></#if>
			</#if>
		</#if>
		
	</#list>
	<input type="hidden" name="deliveryType" value="${v_deliveryType}" />
</#if>
</#macro>
<#--组合奖励【关闭】状态时的活动奖励模板-->
<#macro getComGiftItem0 camp index virtPrmFlag='1' prmCate=''>
	<#--可编辑Flag-->
	<#assign enEditFlag = true/>
	<#if camp.state! =='2'>
		<#assign enEditFlag = false/>
	</#if>
	<div class="clearfix">
		<#if enEditFlag>
			<#if virtPrmFlag !='3' || prmCate != 'DHMY'>
				<a class="add right" onclick="ACT.openDialog('prtBody_${index}', 'P');return false;" style="margin-top:3px;">
					<span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.choicePrm" /></span>
				</a>
				<a class="add right" onclick="ACT.openDialog('prtBody_${index}', 'N');return false;" style="margin-top:3px;">
					<span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.choicePrt" /></span>
				</a>
			</#if>
			<#if virtPrmFlag=='3'>
				<a class="add right" onclick="ACT.openDialog('prtBody_${index}', 'P','${prmCate}');return false;" style="margin-top:3px;">
					<span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.choiceBarCode" /></span>
				</a>
			</#if>
		</#if>
     </div>
     <@getGiftTable list=camp.prtList![] enEditFlag=enEditFlag tbodyId='prtBody_' + index/>
	 <#if virtPrmFlag!='3' && prmCate == 'DHCP'>
		<div class="toolbar_bottom clearfix">
			<div class="right">
				<span>
					<@s.text name="cp.pointNeed2" />
					<span class="highlight">*</span>
					<input id="rewardInfo_BB_${index}_1" name="exPoint" class="price" style="width:80px;text-align:right;" value="${(camp.exPoint)!}" />
					<@s.text name="cp.amout" />
					<span class="highlight">*</span>
					<input id="rewardInfo_CC_${index}" name="amout" class="price" style="width:80px;text-align:right;" value="${(camp.amout)!}" />
					<@s.text name="cp.yuan" />
				</span>
			</div>
		</div>
	</#if>
</#macro>
<#--组合奖励【开启】状态时的活动奖励模板-->
<#macro getComGiftItem1 camp index virtPrmFlag='1' prmCate=''>
	<#--可编辑Flag-->
	<#assign enEditFlag = true/>
	<#if camp.state! =='2'><#assign enEditFlag = false/></#if>
	<div class="clearfix">
		<div class="clearfix">
	        <span class="sortbox_head"><@s.text name="cp.rewardConTent" /></span>
	        <#if prmCate=='DHCP'  && virtPrmFlag !='3'>
	        <span>
				<span><@s.text name="cp.pointNeed2" /></span>
				<span class="highlight">*</span>
				<input value="${camp.exPoint!}" style="width:60px;text-align:right;" class="price" name="exPoint" id="rewardInfo_BB_${index}_1">
				<span><@s.text name="cp.amout" /></span>
				<span class="highlight">*</span>
				<input value="${camp.amout!}" style="width:60px;text-align:right;" class="price" name="amout" id="rewardInfo_CC_${index}">
				<span><@s.text name="cp.yuan" /></span>
			</span>
			</#if>
	        <a style="margin-top:3px;" onclick="ACT.addBox('OR',${index});" class="add right">
	            <span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.addChoiceCom" /></span>
	        </a>
	        <a style="margin-top:3px;" onclick="ACT.addBox('AND',${index});" class="add right">
	            <span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.addFixedCom" /></span>
	        </a>
	    </div>
	    <div class="PARAMS">
	    	<input type="hidden" name="logicOpt" value="${(camp.rewardMap.logicOpt)!'AND'}"/>
	        <input type="hidden" name="logicOptArr" value=""/>
	    </div>
	    <div id="boxDiv_${index}" class="sortbox_content clearfix">
			<#list (camp.rewardMap.logicOptArr)![] as box>
				<#assign boxNo = boxNo + 1/>
				<@getGiftBox boxInfo=box boxNo=boxNo virtPrmFlag=virtPrmFlag/>
				<#if box_has_next><@getLogicBtn logicOpt=(camp.rewardMap.logicOpt)! /></#if>
			</#list>
		</div>
	</div>
</#macro>
<#-- ********礼品领用奖励********* -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getGiftBox boxInfo boxNo virtPrmFlag>
<#assign logicOpt = 'AND'/>
<#if boxInfo.logicOpt?exists><#assign logicOpt = boxInfo.logicOpt/></#if>
<div style="margin:0px;" class="GIFTBOX box2">
    <div class="PARAMS sortbox_subhead_${logicOpt} clearfix">
	    <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="cp.group_${logicOpt}" /></span>
	    <span class="prompt_text"><@s.text name="cp.group_tip_${logicOpt}" /></span>
        <a class="delete right" onclick="ACT.delBox(this);return false;" style="margin: 10px;">
        	<span class="ui-icon icon-delete-big">close</span>
        </a>
    	<a class="add right" onclick="ACT.openDialog2(this, 'P');return false;" style="margin-top:3px;">
			<span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.choicePrm" /></span>
		</a>
		<a class="add right" onclick="ACT.openDialog2(this, 'N');return false;" style="margin-top:3px;">
			<span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.choicePrt" /></span>
		</a>
		<#if virtPrmFlag=='3'>
			<a class="add right" onclick="ACT.openDialog2(this, 'P',true);return false;" style="margin-top:3px;">
				<span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="cp.choiceBarCode" /></span>
			</a>
		</#if>
		<input type="hidden" name="logicOpt" value="${logicOpt}"/>
		<input type="hidden" name="logicOptArr" value=""/>
    </div>
    <div class="box2-content_${logicOpt}">
    	<@getGiftTable list=boxInfo.logicOptArr![] enEditFlag=true tbodyId='boxBody_' + boxNo />
	</div>
</div>
</#macro>
<#macro getPriceControl priceControl saleBatchNo needBuyFlag='1'>
<div class="box4" class="UN_SAME" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.shopingCondition" /></strong></div>
	<div class="box4-content clearfix">
		<div style="" class="left">
	    	<span class="ui-icon icon-arrow-crm" style="margin-top:6px;"></span>
	    	<#if needBuyFlag == '1'>
	    		<@s.text name="cp.shopingCondition_1" />
	    	<#else>
	    		<@s.text name="cp.shopingCondition_2" />
	    		<input class="number" name="saleBatchNo" value="${saleBatchNo}" style="margin:0;"/>
	    		<@s.text name="cp.shopingCondition_3" />
	    	</#if>
			<input class="price" name="priceControl" value="${priceControl}" />
			<@s.text name="cp.yuan" />
		</div>
	</div>
</div>
</#macro>
<#macro getPriceControl_CFM priceControl saleBatchNo='' needBuyFlag='1'>
<#--<#if needBuyFlag == '1' && priceControl != ''>-->
<#if priceControl != ''>
<div class="box4" class="UN_SAME" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.shopingCondition" /></strong></div>
	<div class="box4-content clearfix">
		<div style="" class="left">
	    	<span class="ui-icon icon-arrow-crm"></span>
	    	<#if needBuyFlag == '1'>
	    		<@s.text name="cp.shopingCondition_1" />
	    	<#else>
	    		<@s.text name="cp.shopingCondition_2" />
	    		<strong style="color:#ff0000;font-size:15px;">
	    			<span class="red">
	    			<#if saleBatchNo == ''><@s.text name="cp.batchNo_ALL"/>
	    			<#else><@s.text name="format.number"><@s.param value="${saleBatchNo?number}"></@s.param></@s.text>
					</#if>
					</span>
				</strong>
	    		<@s.text name="cp.shopingCondition_3" />
	    	</#if>
	    	<strong style="color:#ff0000;font-size:15px;">
				<span class="red"><@s.text name="format.price"><@s.param value="${priceControl?number}"></@s.param></@s.text></span>
			</strong>
			<@s.text name="cp.yuan" />
		</div>
	</div>
</div>
</#if>
</#macro>
<#-- ********礼品领用奖励********* -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getLYHDCampGiftList camp index virtPrmFlag='1' groupFlag='' needBuyFlag='0' showCoupon=0>
<#--可编辑Flag-->
<#assign enEditFlag = false/>
<#if !camp.state?exists || camp.state=='0' || camp.state=='3'>
	<#assign enEditFlag = true/>
</#if>
<#--奖励类型-->
<#assign rewardType = '1'/>
<#if showCoupon == 1><#assign rewardType = '2'/></#if>
<#if camp.rewardType?exists><#assign rewardType = camp.rewardType/></#if>
<div class="<#if index!= camp.curIndex!0>hide</#if>">
	<div class="FORM_CONTEXT">
		<#-- 取得会员活动共通hidden -->
		<@getCampHideDiv camp=camp/>
		<#--coupon设置 -->
		<@getCouponSetting index=index camp=camp enEditFlag=enEditFlag showCoupon=showCoupon/>
		<#--购买金额 -->
		<@getPriceControl priceControl=(camp.priceControl)! saleBatchNo=(camp.saleBatchNo)! needBuyFlag=needBuyFlag/>
		<#-- 奖励方式 -->
		<div style="margin-top:0px;" class="relation clearfix UN_SAME">
			<span class="left" style="margin-left:5px">
				<@s.text name="cp.campReward" />
				<select id="rewardType_${index}" name="rewardType" onchange="ACT.changeRewardType(this,${index});" 
					<#if !enEditFlag>disabled="disabled"</#if> style="margin-left:10px;width:140px;">
					<#list application.CodeTable.getCodes("1226") as code>
						<option <#if code.CodeKey==rewardType >selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
				  	</#list>
				</select>
				<#if !enEditFlag><input type="hidden" id="rewardType_${index}" name="rewardType" value="${rewardType!}" /></#if>
			</span>
		</div>
		
		<div id="barCode_${index}" <#if rewardType !="2">class="hide"</#if>>
		<#-- 虚拟促销品生成方式 -->
		<@getBarCodeDiv camp=camp index=index virtPrmFlag=virtPrmFlag prmCate='TZZK'/>
		</div>
		<input type="hidden" name="virtPrmFlag" value="${virtPrmFlag}" />
		<input type="hidden" name="rewardInfo" id="rewardInfo_A_${index}" value="${(camp.rewardInfo)!?html}" />
		<@getDeliveryPrice index=index points=(camp.deliveryPoints)!'' price=(camp.deliveryPrice)!'' />
	</div>
	<div id="giftList_${index}" class="group-content box2-active clearfix">
		<#-- 奖励指定礼品块 -->
		<div <#if rewardType !="1">class="hide"</#if> id="giftList_${index}_1">
			<@getComGiftItem camp=camp index=index virtPrmFlag=virtPrmFlag prmCate='TZZK' groupFlag=groupFlag/>
		</div>
		<#-- 奖励电子抵用券 -->
		<div <#if rewardType!="2">class="hide"</#if> id="giftList_${index}_2">
			<div>
				<table style="width: 100%;">
					<tbody>
					  <tr>
					  	<th style="text-align:right; width:20%;padding-right:10px;"><@s.text name="cp.eleCoupon"/>
					  	<span class="highlight">*</span>
					  </th>
						<td style="width:70%;padding:5px 10px;">
							<span>
								<input id="rewardInfo_AA_${index}" class="price" value="<#if camp.rewardType! =='2'>${camp.rewardInfo!}</#if>" style="width:80px;text-align:right;"/>
								<@s.text name="cp.yuan" />
							</span>
						</td>
					  </tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
</#macro>
<#macro getCouponSetting index enEditFlag camp showCoupon>
<#if showCoupon == 1>
	<#--Coupon类型-->
	<#assign couponType = '0'/>
	<#if camp.couponType?exists><#assign couponType = camp.couponType/></#if>
	<div style="margin-top:0px;" class="box4">  
		<div class="box4-header">
			<strong><@s.text name="cp.couponInfo" /></strong>
			<#-- 查询导入COUPON-->
			<span id="searchCoupon_${index}" class="right <#if couponType != '3'>hide</#if>">
				<a onclick="ACT.couponSearch(${index});return false;" class="search" style="margin: 0px 0 0 20px;">
			  		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.couponSearch" /></span>
				</a>
			</span>
		</div>
		<div class="box4-content UN_SAME">
			<div class="clearfix">
				<input type="hidden" name="obtainToDate" value="${camp.obtainToDate}" />
				<input type="hidden" name="campaignCode" id="campaignCode_${index}" value="${camp.campaignCode}" />
				<div class="BATCHCODE"><input type="hidden" id="batchCode_${index}" name="batchCode" value="${(camp.batchCode)!?html}"/></div>
				<table style="width: 100%;">
					<tbody>
					  <tr>
					  	<th style="text-align:right; width:15%;padding-right:10px;">
							<@s.text name="cp.couponSelect"/>
						  	<span class="highlight">*</span>
						</th>
						<td style="width:85%;padding:5px 10px;">
							<span>
								<select id="couponType_AA_${index}"  name="couponType" onchange="ACT.changeCouponType(this,${index});" 
									<#if !enEditFlag>disabled="disabled"</#if> style="width:150px;">
									<#if camp.campMebType == '5'>
										<#list application.CodeTable.getCodes("1282") as code>
											<#if code.CodeKey=='1' || code.CodeKey=='3'>
												<option  <#if code.CodeKey==camp.couponType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
											</#if>
									  	</#list>
									<#else>  
										<#list application.CodeTable.getCodes("1282") as code>
											<option <#if code.CodeKey==camp.couponType!>selected="true"</#if> value="${code.CodeKey!?html}">${code.Value!?html}</option>
									  	</#list>
								  	</#if>
								</select>
							<#if !enEditFlag><input type="hidden" id="couponType_${index}" name="couponType" value="${(camp.couponType)!}" /></#if>
							</span>
							<#-- Coupon导入弹出框-->
							<span id="linkCouponImport_${index}" class="<#if couponType != '3'>hide</#if>">
								<a id="memCouponLink_${index}" onclick="ACT.popCouponImport();return false;" class="search" style="margin: 0px 0 0 20px;">
						      		<span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.choice" /></span>
						    	</a>
							</span>
						</td>
					  </tr>
					  <tr id="couponCount_${index}" <#if couponType == '3'>class="hide"</#if>>
					  	<th style="text-align:right; width:15%;padding-right:10px;">
							<@s.text name="cp.couponCount"/>
						  	<span class="highlight">*</span>
						</th>
						<td style="width:85%;padding:5px 10px;">
							<span>
								<input name="couponCount" id="couponCount_AA_${index}" <#if !enEditFlag>disabled="disabled"</#if> class="price" value="${camp.couponCount!}" />
								<#if !enEditFlag><input type="hidden" name="couponCount" value="${(camp.couponCount)!}" /></#if>
								<@s.text name="个" />
							</span>
						</td>
					  </tr>
					</tbody>
				</table>
		  		<div class="section hide" id="couponInfo_${index}">
		  			<div class="section-content">
				    	<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="couponDataTable_${index}">
				      		<thead>
						        <tr>
						          <th><@s.text name="cp.batchNo" /></th>
						          <th><@s.text name="cp.couponCode" /></th>
						          <th><@s.text name="cp.couponImportTime" /></th>
						        </tr>
				     		</thead>
				      		<tbody></tbody>
				    	</table>
		 			</div>
				</div>
			</div>
		</div>
	</div>
</#if>
</#macro>

<#-- ********礼品领用奖励********* -->
<#-- camp 	:	子活动信息		  -->
<#-- index 	:	子活动索引		  -->
<#-- **************************** -->
<#macro getLYHDCampGiftList_CFM  campMap>
<#if campMap.rewardType == '1'>
	<div>
		<span class="ui-icon icon-arrow-crm"></span>
		<@s.property value="#application.CodeTable.getVal('1226','${(campMap.rewardType)!?html}')"/>
	</div>
<#-- 奖励组合模式显示-->
<#if campMap.rewardMap?exists>
<div class="sortbox_content clearfix">
	<#list (campMap.rewardMap.logicOptArr)![] as box>
		<div class="GIFTBOX box2" style="margin:0px;">
			<div class="PARAMS sortbox_subhead_${box.logicOpt} clearfix">
			    <span><span class="ui-icon icon-ttl-section-list"></span><@s.text name="cp.group_${box.logicOpt}" /></span>
			    <span class="prompt_text"><@s.text name="cp.group_tip_${box.logicOpt}" /></span>
		    </div>
		    <div class="box2-content_AND">
				<@getGiftTable list=box.logicOptArr![] enEditFlag=false tbodyId='boxBody_' + boxNo/>
			</div>
		</div>
		<#if box_has_next>
			<button type="button" class="button_${campMap.rewardMap.logicOpt}">
				<span class="text">${campMap.rewardMap.logicOpt}</span>
			</button>
		</#if>
	</#list>
</div>
<#else>
<#-- 奖励一般模式显示-->
<div>
	<@getGiftTable list=campMap.prtList![] enEditFlag=false tbodyId=''/>
</div>
</#if>
<#elseif campMap.rewardType == '2'>
	<div style="width:20%;float:left;">
		<span class="ui-icon icon-arrow-crm"></span>
		<#if campMap.campaignType == 'CXHD'>
		<@s.text name="cp.vouchersPrice" />
		<#else>
		<@s.property value="#application.CodeTable.getVal('1226','${(campMap.rewardType)!?html}')"/>
		 </#if>
	</div>
	<div style="width:80%;float:left;">
		<strong style="color:#ff0000;font-size:15px;">
			<span class="red">${campMap.rewardInfo!}</span>
			<@s.text name="cp.yuan" />
		</strong>
	</div>
</#if>
</#macro>

<#-- ================================= 活动确认 ======================================-->
<#-- ************* 活动阶段确认画面 *************-->
<#macro getCampStage_CFM  campMap>
 <div class="box4" style="margin-top:0px;">
<div class="box4-header"><strong><@s.text name="cp.step102" /></strong>
<span class="ui-widget" style="position: relative; margin-left:30px;"></span>
</div>
<div class="box4-content">
	<table class="detail" cellpadding="0" cellspacing="0">
	 <#if campMap.orderFromDate! !="">
		 <tr>
		  <th style="width:15%;"><@s.property value="#application.CodeTable.getVal('1195',1)"/></th>
		  <td style="width:85%;">${(campMap.orderFromDate)!?html} <@s.text name="cp.to" /> ${(campMap.orderToDate)!?html}</td>
		</tr>
	</#if>
	<#if campMap.stockFromDate! !="">
		 <tr>
		  <th style="width:15%;"><@s.property value="#application.CodeTable.getVal('1195',2)"/></th>
		  <td style="width:85%;">${(campMap.stockFromDate)!?html} <@s.text name="cp.to" /> ${(campMap.stockToDate)!?html}</td>
		</tr>
	</#if>
	<#if campMap.obtainFromDate! !="">
		 <tr>
		  <th style="width:15%;"><@s.property value="#application.CodeTable.getVal('1195',3)"/></th>
		  <td style="width:85%;">${(campMap.obtainFromDate)!?html} <@s.text name="cp.to" /> ${(campMap.obtainToDate)!?html}</td>
		</tr>
	</#if>
	</table>
</div>
</div>
</#macro> 
<#-- ************* 活动范围确认画面 *************-->
<#macro getCampRange_CFM  campMap>
<div class="box4" style="margin-top:0px;">
	<div class="box4-header"><strong><@s.text name="cp.step103" /></strong></div>
	<div class="box4-content clearfix">
		<div style="width:20%;float:left;">
			<span class="ui-icon icon-arrow-crm"></span>
			<@s.property value="#application.CodeTable.getVal('1156','${(campMap.locationType)!?html}')"/>
		</div>
		<#if campMap.placeJson?exists && campMap.locationType !='0'>
			<div style="width:80%;float:left;">
				<#list campMap.placeJson as placeMap>
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
		</#if>
	</div>
</div>
</#macro>

<#-- ***************活动对象确认画面  *************** -->
<#macro getCampObject_CFM campMap campIndex>
<div style="margin-top:0px;" class="box4">  
	<div class="box4-header"><strong><@s.text name="cp.step104" /></strong>
	<#if campMap.campMebType == '1' || campMap.campMebType == '2'|| campMap.campMebType == '3'|| campMap.campMebType == '4'>
	<span class="ui-widget" style="position: relative; margin-left:20px;">
		<@s.text name="cp.memCount" /><span class="green"><@s.text name="format.number"><@s.param value="${campMap.total!}"></@s.param></@s.text></span>
		<#if campMap.campMebType == '1'><span class="red">[<@s.text name="global.page.estimate" />]</span></#if>
	</span>
	<input type="hidden" id="searchCode_${campIndex}" name="searchCode" value="${campMap.searchCode!}"/>
	<a onclick="ACT.memSearch(${campIndex});return false;" class="right search">
        <span class="ui-icon icon-search"></span><span class="button-text"><@s.text name="cp.memSearch" /></span>
    </a>
    </#if>
	</div>
    <div class="box4-content">
    	<div class="clearfix">
		    <div class="left" style="width:15%;">
		    	<span class="ui-icon icon-arrow-crm"></span>
		    	<@s.property value="#application.CodeTable.getVal('1224','${(campMap.campMebType)!?html}')"/>   
	     	</div>
	     	<p class="left" style="width:85%;margin:0;">
	     	<#if campMap.campMebInfo! != "" && campMap.campMebInfo != "ALL_MEB" && campMap.campMebInfo != "ALL" && campMap.campMebInfo != "NO_MEB">
	           	<@s.action name="BINOLCM33_conditionDisplay" namespace="/common" executeResult=true>
	           		<@s.param name="reqContent">${(campMap.campMebInfo)!}</@s.param>
	           	</@s.action>
	        </#if>
	        </p>
	     </div>
     <#--查询活动对象结果-->
	 <div>
        <div class="section hide" id="memberInfo_${campIndex}">
		  <div class="section-content">
		    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table"
		    width="100%" id="memberDataTable_${campIndex}">
		      <thead>
		        <tr>
		          <th><@s.text name="cp.memObjectType" /></th>
		          <th><@s.text name="cp.memCardCode" /></th>
		          <th><@s.text name="cp.memName" /></th>
		          <th><@s.text name="cp.memMobile" /></th>
		          <th><@s.text name="cp.brithDay" /></th>
		          <th><@s.text name="global.page.joinDate" /></th>
		          <th><@s.text name="global.page.changablePoint" /></th>
		          <th class="center"><@s.text name="cp.isReceiveMsg" /></th>
		        </tr>
		      </thead>
		      <tbody>
		      </tbody>
		    </table>
		  </div>
		</div>
	</div>
	</div>
</div>
</#macro>
<#--=======设置虚拟促销品条码=======-->
<#macro getBarCodeDiv camp index virtPrmFlag prmCate>
<#--方式为用户录入-->
<#if virtPrmFlag == '2'>
	<div style="margin-top:0px;" class="relation clearfix UN_SAME">
		<span id="barCode_${prmCate}_${index}" class="left" style="margin-left:5px">
			<@s.text name="cp.barCode_v"/>
			<#if !camp.prmVendorId?exists || camp.prmVendorId == 0>
				<span class="highlight">*</span>
				<span><input class="text" style="width:105px;" name="barCode" onchange="ACT.vaildBarCode(this,${index});" value="${(camp.barCode)!}"/></span>
				<a onclick="getBarCode('${prmCate}',${index});return false;" class="search" style="margin:0px 0px 2px -5px;">
					<span class="ui-icon icon-search"></span>
					<span class="button-text"><@s.text name="cp.autoGet"/></span>
				</a>
			<#else>
				<input class="text" disabled="true" style="width:105px;" value="${(camp.barCode)!}"/>
				<input type="hidden" name="barCode" value="${(camp.barCode)!}"/>
				<input type="hidden" name="prmVendorId" value="${camp.prmVendorId!}"/>
			</#if>
		</span>
	</div>
</#if>
</#macro>
<#-- ==== 取得会员活动共通hidden ====-->
<#macro getCampHideDiv camp>
	<div class="hide UN_SAME">
  		<input type="hidden" value="${camp.SUBCAMP_DETAILNO!}" name="SUBCAMP_DETAILNO"/>
  		<input type="hidden" value="${camp.SUBCAMP_RULETYPE!}" name="SUBCAMP_RULETYPE"/>
  		<#if camp.campaignRuleId?exists>
  			<input type="hidden" value="${camp.campaignRuleId}" name="campaignRuleId"/>
  		</#if>
  	</div>
</#macro>
<#macro getDeliveryPrice index points="" price="">
<div id="DeliveryPrice_${index}" style="margin-top:10px;" class="box4 <#if price == '' && points=''>hide</#if>">
	<div class="box4-header"><strong><@s.text name="cp.deliveryInfo"/></strong></div>
	<div class="box4-content clearfix">
		<div class="left" style="">
	    	<span style="margin-top:6px;" class="ui-icon icon-arrow-crm"></span>
			<@s.text name="cp.deliveryInfo_1"/>
			<input style="margin:0;" value="${price}" name="deliveryPrice" class="price">
			<@s.text name="cp.deliveryInfo_2"/>
			<input value="${points}" name="deliveryPoints" class="number">
			<@s.text name="cp.deliveryInfo_3"/>
		</div>
	</div>
</div>
</#macro>
<#macro getDeliveryPrice_CFM points="" price="">
<div style="margin-top:10px;" class="box4 <#if price == '' && points=''>hide</#if>">
	<div class="box4-header"><strong><@s.text name="cp.deliveryInfo"/></strong></div>
	<div class="box4-content clearfix">
		<div class="left" style="">
	    	<span class="ui-icon icon-arrow-crm"></span>
			<@s.text name="cp.deliveryInfo_1"/>
			<span class="red">
				<#if price == ''>
				<@s.text name="format.price"><@s.param value="0"></@s.param></@s.text>
				<#else><@s.text name="format.price"><@s.param value="${price}"></@s.param></@s.text>
				</#if>
			</span>
			<@s.text name="cp.deliveryInfo_2"/>
			<span class="red">
				<#if points == ''>
				<@s.text name="format.number"><@s.param value="0"></@s.param></@s.text>
				<#else><@s.text name="format.number"><@s.param value="${points}"></@s.param></@s.text>
				</#if>
			</span>
			<@s.text name="cp.deliveryInfo_3"/>
		</div>
	</div>
</div>
</#macro>
<#-- ========================================== 模板宏定义结束  =========================================-->
