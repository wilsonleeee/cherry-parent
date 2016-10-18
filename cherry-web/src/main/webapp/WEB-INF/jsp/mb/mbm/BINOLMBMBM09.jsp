<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>

<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>
<s:set id="QUESTIONTYPE_ESSAY"><%=MonitorConstants.QUESTIONTYPE_ESSAY %></s:set>
<s:set id="QUESTIONTYPE_APFILL"><%=MonitorConstants.QUESTIONTYPE_APFILL %></s:set>

<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM09.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM09">
<s:text name="global.page.select" id="select_default"></s:text>
<div class="panel-header">
    <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="binolmbmbm09_memberManage"></s:text>&nbsp;&gt;&nbsp;<s:text name="binolmbmbm09_memberList"></s:text> </span> 
    
    <span class="right">
      <cherry:show domId="BINOLMBMBM01_01">
    	<s:url action="BINOLMBMBM11_init" id="addMemberUrl"></s:url>
    	<a class="add" href="${addMemberUrl }" onclick="javascript:openWin(this);return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="binolmbmbm09_addMember"></s:text></span></a>
      </cherry:show>
      <cherry:show domId="BINOLMBMBM01_03">
        <s:url action="BINOLMBMBM15_moveMemCounter" id="moveMemCounterUrl"></s:url>
        <a class="add" href="#" onclick="binolmbmbm09.moveMemCounterInit('${moveMemCounterUrl}');return false;"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="binolmbmbm09_memChangeCou"></s:text></span></a>
      </cherry:show>
    </span>
    </div>
</div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="actionResultDisplay"></div>
<div id="errorMessage"></div>    
<div style="display: none" id="errorMessageTemp">
<div class="actionError">
   <ul><li><span><s:text name="binolmbmbm09_errorMessage"/></span></li></ul>         
</div>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content clearfix">
<div class="box">
  <cherry:form id="memberCherryForm" class="inline">
  	<s:hidden name="clubMod"></s:hidden>
    <s:hidden name="brandInfoId"></s:hidden>
    <s:hidden name="exportMode"></s:hidden>
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
      <a class="ui-select right" style="margin-left: 10px; font-size: 12px;width:75px" id="expandCondition">
        <span class="button-text choice"><s:text name="binolmbmbm09_moreCondition" /></span>
 		<span class="ui-icon ui-icon-triangle-1-n"></span>
 	  </a>
 	  <a class="delete right" onclick="binolmbmbm09.resetCondition('#memberCherryForm');return false;">
        <span class="ui-icon icon-disable"></span>
        <span class="button-text"><s:text name="global.page.resetCondition"></s:text></span>
 	  </a>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
      	<table class="detail" cellpadding="0" cellspacing="0">
		  <tbody>
			<tr>
			  <td colspan="4" style="line-height:25px;" class="td_point">
			    <span class="ui-widget breadcrumb" style="margin-right:30px;">
			    <s:checkbox name="addrNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_addrNotEmpty"/>
			    </span>
                <span class="ui-widget breadcrumb" style="margin-right:30px;">
                <s:checkbox name="telNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_telNotEmpty"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:30px;">
                <s:checkbox name="emailNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_emailNotEmpty"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:30px;">
                <s:checkbox name="couNotEmpty" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_couNotEmpty"/>
                </span>
                <%-- 
                <span class="ui-widget breadcrumb" style="margin-right:50px;">
                <s:checkbox name="telCheck" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_telCheck"/>
                </span>
                --%>
                <span class="ui-widget breadcrumb" style="margin-right:30px;">
                <s:checkbox name="validFlag" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_validFlag"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:30px;">
                <s:checkbox name="memInfoRegFlg" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_memInfoRegFlg"/>
                </span>
                <span class="ui-widget breadcrumb" style="margin-right:30px;">
                <s:checkbox name="testType" fieldValue="1"></s:checkbox><s:text name="binolmbmbm09_testType"/>
                </span>
              </td>
            </tr>
            
            <s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'>
            <tr>
              <th><s:text name="binolmbmbm09_memType" /></th>
              <td colspan="3">
				<span>
				<s:checkboxlist list='#application.CodeTable.getCodes("1204")' listKey="CodeKey" listValue="Value" name="memType" ></s:checkboxlist>
				</span>
	   		  </td>
            </tr>
            </s:if>
             <s:if test='%{"3".equals(clubMod)}'>
            <tr>
              <th><s:text name="binolmbmbm09_memberLevel" /></th>
              <td colspan="3" class="td_point">
				<span>
				<s:if test="null != memLevelList && memLevelList.size() > 0">
				<s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" ></s:checkboxlist>
				</s:if>
				</span>
	   		  </td>
            </tr>
            </s:if>
            <tr>
              <th><s:text name="binolmbmbm09_joinDate" /></th>
              <td colspan="3">
              	<span>
				<select name="joinDateMode" onchange="binolmbmbm09.selectDateMode(this);">
					<option value="-1" <s:if test="%{joinDateMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{joinDateMode == 1}">selected</s:if>><s:text name="global.page.curJoinDate"/></option>
					<option value="0" <s:if test="%{joinDateMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{joinDateMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				</span>
				<span class='<s:if test="%{joinDateMode != 0}">hide</s:if>'>
				<s:text name="global.page.joinDateDes" />
				<s:textfield name="joinDateRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
				<s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="joinDateUnit" cssStyle="width:auto;"></s:select>
				<select name="joinDateUnitFlag" style="width:auto;">
					<option value="1" <s:if test="%{joinDateUnitFlag == 1}">selected</s:if>><s:text name="global.page.joinDateUnitFlag1" /></option>
					<option value="2" <s:if test="%{joinDateUnitFlag == 2}">selected</s:if>><s:text name="global.page.joinDateUnitFlag2" /></option>
				</select>
				</span>
				<span class='<s:if test="%{joinDateMode != 9}">hide</s:if>'>
				<span><s:textfield name="joinDateStart" cssClass="date"/></span><span>-<s:textfield name="joinDateEnd" cssClass="date"/></span>
				</span>
				<span style="margin-left:20px;" class='<s:if test="%{joinDateMode == null || joinDateMode == -1}">hide</s:if>'>
				<s:text name="global.page.joinDateSaleDateRel" />
				<input id="joinDateSaleDateRel1" type="radio" name="joinDateSaleDateRel" value="1" <s:if test="%{joinDateSaleDateRel == 1}">checked</s:if>><label for="joinDateSaleDateRel1"><s:text name="global.page.and" /></label>
				<input id="joinDateSaleDateRel2" type="radio" name="joinDateSaleDateRel" value="2" <s:if test="%{joinDateSaleDateRel == 2}">checked</s:if>><label for="joinDateSaleDateRel2"><s:text name="global.page.or" /></label>
				</span>
              </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm09_birthDay" /></th>
              <td colspan="3">
                <span>
                <select name="birthDayMode" onchange="binolmbmbm09.selectBirthDayMode(this);">
					<option value="-1" <s:if test="%{birthDayMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{birthDayMode == 0}">selected</s:if>><s:text name="global.page.curBirthDayMonth"/></option>
					<option value="1" <s:if test="%{birthDayMode == 1}">selected</s:if>><s:text name="global.page.curBirthDayDate"/></option>
					<option value="2" <s:if test="%{birthDayMode == 2}">selected</s:if>><s:text name="global.page.dynamicBirthDay"/></option>
					<option value="9" <s:if test="%{birthDayMode == 9}">selected</s:if>><s:text name="global.page.customBirthDay"/></option>
					<option value="3" <s:if test="%{birthDayMode == 3}">selected</s:if>><s:text name="global.page.birthDayRange"/></option>
				</select>
                </span>
                <span class='<s:if test="%{birthDayMode != 0}">hide</s:if>'>
                <span><s:textfield name="birthDayDateStart" cssClass="text" cssStyle="width:30px;"></s:textfield><s:text name="global.page.date"/></span><span>-<s:textfield name="birthDayDateEnd" cssClass="text" cssStyle="width:30px;"></s:textfield><s:text name="global.page.date"/></span>
                <input type="checkbox" name="joinDateFlag" value="1" <s:if test="%{joinDateFlag==1}">checked</s:if> id="joinDateFlag1"/><label for="joinDateFlag1"><s:text name="global.page.joinDateFlag" /></label>
                </span>
                <span class='<s:if test="%{birthDayMode != 1}">hide</s:if>'>
                <input type="checkbox" name="curDayJoinDateFlag" value="1" <s:if test="%{curDayJoinDateFlag==1}">checked</s:if> id="curDayJoinDateFlag1"/><label for="curDayJoinDateFlag1"><s:text name="global.page.curDayJoinDateFlag" /></label>
                </span>
                <span class='<s:if test="%{birthDayMode != 2}">hide</s:if>'>
                <select name="birthDayPath" style="width:auto;">
					<option value="1" <s:if test="%{birthDayPath == 1}">selected</s:if>><s:text name="global.page.birthDayPath1" /></option>
					<option value="2" <s:if test="%{birthDayPath == 2}">selected</s:if>><s:text name="global.page.birthDayPath2" /></option>
				</select>
                <s:textfield name="birthDayRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <select name="birthDayUnit" style="width:auto;">
					<option value="1" <s:if test="%{birthDayUnit == 1}">selected</s:if>><s:text name="global.page.birthDayUnit1" /></option>
					<option value="2" <s:if test="%{birthDayUnit == 2}">selected</s:if>><s:text name="global.page.birthDayUnit2" /></option>
				</select>
				<s:text name="global.page.birthDayText" />
                </span>
                <span class='<s:if test="%{birthDayMode != 9}">hide</s:if>'>
                <s:select list="monthList" name="birthDayMonth" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" value="%{birthDayMonth}" onchange="binolmbmbm09.selectMonth(this,'0');"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateList != null && !dateList.isEmpty()}">
          		<s:select list="dateList" name="birthDayDate" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" value="%{birthDayDate}"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDate" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>
                </span>
                <span class='<s:if test="%{birthDayMode != 3}">hide</s:if>'>
                <s:select list="monthList" name="birthDayMonthRangeStart" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="binolmbmbm09.selectMonth(this,'1');" value="%{birthDayMonthRangeStart}"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateRangeStartList != null && !dateRangeStartList.isEmpty()}">
          		<s:select list="dateRangeStartList" name="birthDayDateRangeStart" cssStyle="width:auto;" value="%{birthDayDateRangeStart}"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDateRangeStart" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>-
          		<s:select list="monthList" name="birthDayMonthRangeEnd" headerKey="" headerValue="%{select_default}" cssStyle="width:auto;" onchange="binolmbmbm09.selectMonth(this,'1');" value="%{birthDayMonthRangeEnd}"></s:select><s:text name="global.page.month"/>
          		<s:if test="%{dateRangeEndList != null && !dateRangeEndList.isEmpty()}">
          		<s:select list="dateRangeEndList" name="birthDayDateRangeEnd" cssStyle="width:auto;" value="%{birthDayDateRangeEnd}"></s:select><s:text name="global.page.date"/>
          		</s:if>
          		<s:else>
          		<select name="birthDayDateRangeEnd" style="width:auto;"><option value=""><s:text name="global.page.select" /></option></select><s:text name="global.page.date"/>
          		</s:else>
                </span>
			  </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm09_memCode" /></th>
              <td <s:if test='%{!"3".equals(clubMod)}'> colspan="3" </s:if>>
                <span><s:textfield name="memCode" cssClass="text"></s:textfield></span>
              </td>
              <s:if test='%{"3".equals(clubMod)}'>
              <th><s:text name="binolmbmbm09_memberPoint" /></th>
              <td>
                <span><s:textfield name="memberPointStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="memberPointEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td> 
              </s:if>
            </tr>
			<tr>
              <th><s:text name="binolmbmbm09_name" /></th>
              <td <s:if test='%{!"3".equals(clubMod)}'> colspan="3" </s:if>>
                <span><s:textfield name="name" cssClass="text"></s:textfield></span>
			  </td>
			  <s:if test='%{"3".equals(clubMod)}'>
              <th><s:text name="binolmbmbm09_changablePoint" /></th>
              <td>
                <span><s:textfield name="changablePointStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="changablePointEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>
              </s:if>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm09_mobilePhone" /></th>
              <td <s:if test='%{!"3".equals(clubMod)}'> colspan="3" </s:if>>
                <span><s:textfield name="mobilePhone" cssClass="text"></s:textfield></span>
			  </td>
			   <s:if test='%{"3".equals(clubMod)}'>
              <th><s:text name="binolmbmbm09_curDealDate" /></th>
              <td>
                <span><s:textfield name="curDealDateStart" cssClass="date"></s:textfield></span><span>-<s:textfield name="curDealDateEnd" cssClass="date"></s:textfield></span>
              </td>
               </s:if>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm09_mebSex" /></th>
              <td>
                <span><s:checkboxlist list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="mebSex" ></s:checkboxlist></span>
              </td>
              <th><s:text name="binolmbmbm09_counterName" /></th>
              <td class="td_point">
                <s:hidden name="regionId"></s:hidden>
			    <s:hidden name="provinceId"></s:hidden>
                <s:hidden name="cityId"></s:hidden>
                <s:hidden name="memCounterId"></s:hidden>
                <s:hidden name="channelRegionId"></s:hidden>
                <s:hidden name="channelId"></s:hidden>
                <s:hidden name="exclusiveFlag"></s:hidden>
                <s:hidden name="modeFlag"></s:hidden>
                <s:hidden name="couValidFlag"></s:hidden>
                <s:hidden name="belongId"></s:hidden>
                <span id="regionNameDiv" style="line-height: 18px;"></span>
			    <s:url action="BINOLCM02_initRegionDialog" namespace="/common" id="initRegionDialogUrl"></s:url>
		        <a class="add" style="float: none;" onclick="binolmbmbm09.popRegionDialog('${initRegionDialogUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
              </td>
            </tr>   
            <tr>
              <th><s:text name="binolmbmbm09_age" /></th>
              <td>
                <span><s:textfield name="ageStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="ageEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
			  </td> 
			  <th><s:text name="binolmbmbm09_memBA" /></th>
              <td>
                <span>
                <s:hidden name="employeeId"></s:hidden>
		    	<s:hidden name="employeeCode"></s:hidden>
		    	<span id="employeeDiv" style="line-height: 18px;"></span>
		    	<s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		    	<a class="add" onclick="binolmbmbm09.popEmployeeList('${searchEmployeeInitUrl}');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
                </span>
              </td>
            </tr> 
            <tr>
              <th><s:text name="binolmbmbm09_channelCode" /></th>
              <td>
                <span><s:select list='#application.CodeTable.getCodes("1301")' listKey="CodeKey" listValue="Value" name="channelCode" headerKey="" headerValue="%{#select_default}"></s:select></span>
			  </td>
			  <th><s:text name="binolmbmbm09_bindWeChat" /></th>
              <td>
                <span>
                  <select name="bindWeChat">
                    <option value=""><s:text name="global.page.select" /></option>
                    <option value="1"><s:text name="binolmbmbm09_bindWeChat1" /></option>
                    <option value="2"><s:text name="binolmbmbm09_bindWeChat2" /></option>
                  </select>
                </span>
			  </td>
            </tr>  
            <tr>                 
			  <th><s:text name="binolmbmbm09_activeChannel" /></th>
              <td>
                <span><s:select list='#application.CodeTable.getCodes("1298")' listKey="CodeKey" listValue="Value" name="activeChannel" headerKey="" headerValue="%{#select_default}"></s:select></span>
			  </td> 
              <th><s:text name="binolmbmbm09_wechatBindTime" /></th>
              <td>
                <span><s:textfield name="wechatBindTimeStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="wechatBindTimeEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>  
            </tr>    
            <tr>     
             <s:if test='%{"3".equals(clubMod)}'>
              <th><s:text name="binolmbmbm09_referFlag" /></th>
              <td>
                  <span>
                  <select name="referFlag" onchange="binolmbmbm09.selectReferFlag(this);" style="width:135px;">
					<option value="-1" <s:if test="%{referFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{referFlag == 1}">selected</s:if>><s:text name="global.page.referFlag1"/></option>
					<option value="2" <s:if test="%{referFlag == 2}">selected</s:if>><s:text name="global.page.referFlag2"/></option>
				  </select>
				  </span>
				  <span class='<s:if test="%{referFlag != 1}">hide</s:if>'>
					<s:text name="global.page.referredMemCode" />：<s:textfield name="referredMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
                  <span class='<s:if test="%{referFlag != 2}">hide</s:if>'>
					<s:text name="global.page.referrerMemCode" />：<s:textfield name="referrerMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
              </td>
               </s:if>
              <th><s:text name="binolmbmbm09_lastSaleDate" /></th>
              <td <s:if test='%{!"3".equals(clubMod)}'> colspan="3" </s:if>>
              	<span><s:textfield name="lastSaleDateStart" cssClass="date" cssStyle="width:75px;"/></span><span>-<s:textfield name="lastSaleDateEnd" cssClass="date" cssStyle="width:75px;"/></span>
              </td>
            </tr> 
             <s:if test='%{"3".equals(clubMod)}'>
            <tr>
              <th><s:text name="global.page.levelAdjustDay" /></th>
              <td colspan="3">
             	<span>
				<select name="levelAdjustDayFlag" onchange="binolmbmbm09.selectLevelAdjustDayMode(this);">
					<option value="-1" <s:if test="%{levelAdjustDayFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{levelAdjustDayFlag == 1}">selected</s:if>><s:text name="global.page.curLevelAdjustDay"/></option>
					<option value="2" <s:if test="%{levelAdjustDayFlag == 2}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="3" <s:if test="%{levelAdjustDayFlag == 3}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				</span>
				<span class='<s:if test="%{levelAdjustDayFlag != 2}">hide</s:if>'>
				<s:text name="global.page.levelAdjustDayDes" />
				<s:textfield name="levelAdjustDayRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <select name="levelAdjustDayUnit" style="width:auto;">
					<option value="1" <s:if test="%{levelAdjustDayUnit == 1}">selected</s:if>><s:text name="global.page.levelAdjustDayUnit1" /></option>
					<option value="2" <s:if test="%{levelAdjustDayUnit == 2}">selected</s:if>><s:text name="global.page.levelAdjustDayUnit2" /></option>
				</select>
				</span>
				<span class='<s:if test="%{levelAdjustDayFlag != 3}">hide</s:if>'>
				<s:textfield name="levelAdjustDayStart" cssClass="date" id="levelAdjustDayStart" cssStyle="width:80px"/>-<s:textfield name="levelAdjustDayEnd" cssClass="date" id="levelAdjustDayEnd" cssStyle="width:80px"/>
				</span>
				<span style="margin-left:20px;" class='<s:if test="%{levelAdjustDayFlag == null || levelAdjustDayFlag == -1}">hide</s:if>'>
				<s:text name="global.page.levelChangeType" />
				<s:select list='#application.CodeTable.getCodes("1249")' listKey="CodeKey" listValue="Value" name="levelChangeType" headerKey="" headerValue="%{#select_default}" cssStyle="width:auto;"></s:select>
				</span>
              </td>
            </tr>       
            </s:if>
          </tbody>
        </table>
      </div>
    </div>
	<s:if test='%{!"3".equals(clubMod)}'>
	 <div class="box-header"> 
      <strong><span class="icon icon-ttl-search"></span><s:text name="binolmbmbm09_clubAttr" /></strong>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
        <table class="detail" cellpadding="0" cellspacing="0" id="clubAttrs">
          <tbody>
             <tr>
              <th><s:text name="binolmbmbm09_memClub" /></th>
              <td colspan="3">
				<span>
				<s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="binolmbmbm09.changeLevel(this);return false;" Cssstyle="width:150px;"/>
				</span>
	   		  </td>
            </tr>
            
            <tr>
              <th><s:text name="binolmbmbm09_memberLevel" /></th>
              <td colspan="3">
				<span id="levelSpan">
				<s:if test="null != memLevelList && memLevelList.size() > 0">
				<s:checkboxlist list="memLevelList" listKey="memberLevelId" listValue="levelName" name="memLevel" ></s:checkboxlist>
				</s:if>
				</span>
	   		  </td>
            </tr>
             <tr>
             <th><s:text name="binolmbmbm09_ClubCounterName" /></th>
              <td>
              <s:hidden name="clubRegionId"></s:hidden>
			    <s:hidden name="clubProvinceId"></s:hidden>
                <s:hidden name="clubCityId"></s:hidden>
                <s:hidden name="clubMemCounterId"></s:hidden>
                <s:hidden name="clubChannelRegionId"></s:hidden>
                <s:hidden name="clubChannelId"></s:hidden>
                <s:hidden name="clubExclusiveFlag"></s:hidden>
                <s:hidden name="clubModeFlag"></s:hidden>
                <s:hidden name="clubCouValidFlag"></s:hidden>
                <s:hidden name="clubBelongId"></s:hidden>
                <span id="clubRegionNameDiv" style="line-height: 18px;"></span>
			    <s:url action="BINOLCM02_initRegionDialog" namespace="/common" id="initRegionDialogUrl"></s:url>
		        <a class="add" style="float: none;" onclick="binolmbmbm09.popRegionDialog('${initRegionDialogUrl}', '1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
              </td>
              <th><s:text name="binolmbmbm09_memberPoint" /></th>
              <td>
                <span><s:textfield name="memberPointStart" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"></s:textfield></span><span>-<s:textfield name="memberPointEnd" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"></s:textfield></span>
              </td>
             </tr>
             <tr>
             <th><s:text name="binolmbmbm09_clubBA" /></th>
              <td>
                <span>
                <s:hidden name="clubEmployeeId"></s:hidden>
		    	<s:hidden name="clubEmployeeCode"></s:hidden>
		    	<span id="clubEmployeeDiv" style="line-height: 18px;"></span>
		    	<s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		    	<a class="add" onclick="binolmbmbm09.popEmployeeList('${searchEmployeeInitUrl}','1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
                </span>
              </td>
              <th><s:text name="binolmbmbm09_changablePoint" /></th>
              <td>
                <span><s:textfield name="changablePointStart" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"></s:textfield></span><span>-<s:textfield name="changablePointEnd" cssClass="text club-attr" cssStyle="width:75px;" disabled="true"></s:textfield></span>
              </td>
             </tr>
             <tr>
             <th><s:text name="binolmbmbm09_ClubJoinTime" /></th>
              <td>
              	<span class="no-club-date"><input type="text" class="date" disabled="disabled"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
				<span class="no-club-date">-<input type="text" class="date" disabled="disabled"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
				<span class="hide club-date"><s:textfield name="clubJoinTimeStart" cssClass="date" /></span>
				<span class="hide club-date">-<s:textfield name="clubJoinTimeEnd" cssClass="date"/></span>
              </td>
             
              <th><s:text name="binolmbmbm09_curDealDate" /></th>
              <td>
              	<span class="no-club-date"><input type="text" class="date" disabled="disabled"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
				<span class="no-club-date">-<input type="text" class="date" disabled="disabled"/><button class="ui-datepicker-trigger" disabled="disabled"></button></span>
                <span class="hide club-date"><s:textfield name="curDealDateStart" cssClass="date"></s:textfield></span>
                <span class="hide club-date">-<s:textfield name="curDealDateEnd" cssClass="date"></s:textfield></span>
              </td>
             </tr>
             
             <tr>
             <th><s:text name="binolmbmbm09_referFlag" /></th>
              <td>
              	<span>
                  <select name="referFlag" onchange="binolmbmbm09.selectReferFlag(this);" style="width:135px;" class="club-attr" disabled="disabled">
					<option value="-1" <s:if test="%{referFlag == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="1" <s:if test="%{referFlag == 1}">selected</s:if>><s:text name="global.page.referFlag1"/></option>
					<option value="2" <s:if test="%{referFlag == 2}">selected</s:if>><s:text name="global.page.referFlag2"/></option>
				  </select>
				  </span>
				  <span class='<s:if test="%{referFlag != 1}">hide</s:if>'>
					<s:text name="global.page.referredMemCode" />：<s:textfield name="referredMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
                  <span class='<s:if test="%{referFlag != 2}">hide</s:if>'>
					<s:text name="global.page.referrerMemCode" />：<s:textfield name="referrerMemCode"  cssClass="text" cssStyle="width:100px"/>
				  </span>
              </td>
             <th><s:text name="global.page.isReceiveMsg" /></th>
              <td>
                  <select name="isReceiveMsg" style="width:135px;" class="club-attr" disabled="disabled">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1"><s:text name="global.page.isReceiveMsg1" /></option>
					<option value="0"><s:text name="global.page.isReceiveMsg0" /></option>
				  </select>
              </td>
              
             </tr>
          </tbody>
        </table>
        
      </div>
    </div>
    </s:if>
    <div id="moreCondition" style="display: none;">
    <s:if test='%{"3".equals(clubMod)}'>
    <s:if test='%{"1".equals(tagFlag)}'>
    <div class="box-header"> 
      <strong><span class="icon icon-ttl-search"></span><s:text name="global.page.MemberTag" /></strong>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
        <table class="detail" cellpadding="0" cellspacing="0">
          <tbody>
            <tr>
              <th><s:text name="global.page.isNewMember" /></th>
              <td><span>
              	<select name="isNewMember" style="width:135px;">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1"><s:text name="global.page.yes" /></option>
					<option value="0"><s:text name="global.page.no" /></option>
				  </select>
              </span></td>
              <th><s:text name="global.page.flagBuyCount" /></th>
              <td>
               <span>
              	<select name="flagBuyCount" style="width:135px;">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="N1"><s:text name="global.page.flagBuyCountVal1" /></option>
					<option value="N2"><s:text name="global.page.flagBuyCountVal2" /></option>
					<option value="N3"><s:text name="global.page.flagBuyCountVal3" /></option>
				</select>
              </span>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.isActivityMember" /></th>
              <td>
              <span>
                <select name="isActivityMember" style="width:135px;">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1"><s:text name="global.page.yes" /></option>
					<option value="0"><s:text name="global.page.no" /></option>
				  </select>
				</span>
              </td>
               <th><s:text name="global.page.actiCount" /></th>
              <td>
             <span><s:textfield name="actiCountStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="actiCountEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.favActiType" /></th>
              <td>
              <span>
                <select name="favActiType" style="width:135px;">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="1"><s:text name="global.page.favActiType1" /></option>
					<option value="2"><s:text name="global.page.favActiType2" /></option>
				  </select>
				</span>
              </td>
               <th><s:text name="global.page.unBuyInterval" /></th>
              <td>
             <span>
             <select name="unBuyInterval" style="width:135px;">
					<option value=""><s:text name="global.page.select"/></option>
					<option value="3"><s:text name="global.page.unBuyInterval1" /></option>
					<option value="6"><s:text name="global.page.unBuyInterval2" /></option>
					<option value="9"><s:text name="global.page.unBuyInterval3" /></option>
					<option value="10"><s:text name="global.page.unBuyInterval4" /></option>
				  </select>
             </span>
              </td>
            </tr>
            <tr>
              <th><s:text name="global.page.mostCateBClass" /></th>
              <td colspan="3">
                <span class="right" >
								<a onclick="binolmbmbm09.openCateDialog('mostCateBClassId','memPrtCate_1',<s:property value='bigPropId'/>,this,'checkbox');return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrtCate_1">
						
					</tbody>
				</table>
              </td>
            </tr>
          </tbody>
          <tr>
              <th><s:text name="global.page.mostCateMClass" /></th>
              <td colspan="3">
                <span class="right" >
								<a onclick="binolmbmbm09.openCateDialog('mostCateMClassId','memPrtCateMClass_1',<s:property value='midPropId'/>,this,'checkbox');return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrtCateMClass_1">
						
					</tbody>
				</table>
              </td>
            </tr>
          <tr>
              <th><s:text name="global.page.mostPrt" /></th>
              <td colspan="3">
                <span class="right" >
								<a onclick="binolmbmbm09.openMemProDialog(1);return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrt_1">
					</tbody>
				</table>
              </td>
            </tr>
     <!--   <tr>
              <th><s:text name="global.page.jointCateBClass" /></th>
              <td colspan="3">
                <span class="right" >
								<a onclick="binolmbmbm09.openCateDialog(2,this,'checkbox');return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrtCate_2">
						
					</tbody>
				</table>
              </td>
            </tr>
          <tr>
              <th><s:text name="global.page.jointCateMClass" /></th>
              <td colspan="3">
                <span class="right" >
								<a onclick="binolmbmbm09.openCateMClassDialog(2,this,'checkbox');return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrtCateMClass_2">
						
					</tbody>
				</table>
              </td>
            </tr>
          </tbody>
          <tr>
              <th><s:text name="global.page.jointPrt" /></th>
              <td colspan="3">
                <span class="right" >
								<a onclick="binolmbmbm09.openMemProDialog(2);return false;" class="add right"> 
									<span class="ui-icon icon-search"></span>
									<span class="button-text"><s:text name="global.page.select" /></span>
								</a>
				</span>
				<table class="all_clean" style="margin-left: 15px;">
					<tbody id="memPrt_2">
					</tbody>
				</table>
              </td>
            </tr>   --> 
            <tr>
               <th><s:text name="global.page.pct" /></th>
              <td colspan="3">
             <span><s:textfield name="pctStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="pctEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    </s:if>
    </s:if>
    <div class="box-header"> 
      <strong><span class="icon icon-ttl-search"></span><s:text name="binolmbmbm09_memSale" /></strong>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
        <table class="detail" cellpadding="0" cellspacing="0">
          <tbody>
            <tr class="changeSaleFlag">
			  <td colspan="4" style="line-height:25px;" class="td_point">
			    <span class="ui-widget breadcrumb" style="margin-right:5px;">
			    <input id="isSaleFlag1" type="radio" name="isSaleFlag" value="1" checked="checked" onclick="binolmbmbm09.changeSaleFlag(this);"><label for="isSaleFlag1"><s:text name="global.page.isSaleFlag1" /></label>
			    </span>
                <span class="ui-widget breadcrumb" style="margin-right:5px;">
                <input id="isSaleFlag0" type="radio" name="isSaleFlag" value="0" onclick="binolmbmbm09.changeSaleFlag(this);"><label for="isSaleFlag0"><s:text name="global.page.isSaleFlag0" /></label>
                </span>
              </td>
            </tr>
            
            <tr class="hasSale">
              <th><s:text name="binolmbmbm09_saleTime" /></th>
              <td>
                <span>
                <select name="saleTimeMode" onchange="binolmbmbm09.selectDateMode(this);">
					<option value="-1" <s:if test="%{saleTimeMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{saleTimeMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{saleTimeMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
				</select>
				</span>
                <span class='<s:if test="%{saleTimeMode != 0}">hide</s:if>'>
                <s:text name="global.page.saleDateDes" />
                <s:textfield name="saleTimeRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="saleTimeUnit" cssStyle="width:auto;"></s:select>
                <s:text name="global.page.saleTimeUnitFlag" />
                </span>
                <span class='<s:if test="%{saleTimeMode != 9}">hide</s:if>'>
                <span><s:textfield name="saleTimeStart" cssClass="date" /></span><span>-<s:textfield name="saleTimeEnd" cssClass="date" /></span>
                </span>
              </td>
              <th><s:text name="binolmbmbm09_saleCount" /></th>
              <td>
              	<span><s:textfield name="saleCountStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="saleCountEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>
            </tr>
            <tr class="hasSale">
              <th><s:text name="binolmbmbm09_saleCounterCode" /></th>
              <td colspan=3>
                <s:hidden name="saleRegionId"></s:hidden>
			    <s:hidden name="saleProvinceId"></s:hidden>
                <s:hidden name="saleCityId"></s:hidden>
                <s:hidden name="saleMemCounterId"></s:hidden>
                <s:hidden name="saleChannelRegionId"></s:hidden>
                <s:hidden name="saleChannelId"></s:hidden>
                <s:hidden name="saleModeFlag"></s:hidden>
                <s:hidden name="saleCouValidFlag"></s:hidden>
                <s:hidden name="saleBelongId"></s:hidden>
                <span id="saleRegionNameDiv" style="line-height: 18px;"></span>
			    <s:url action="BINOLCM02_initRegionDialog" namespace="/common" id="initRegionDialogUrl"></s:url>
		        <a class="add" style="float: none;" onclick="binolmbmbm09.popSaleRegionDialog('${initRegionDialogUrl}');return false;">
		          <span class="ui-icon icon-search"></span>
		          <span class="button-text"><s:text name="global.page.Popselect"></s:text></span>
		        </a>
              </td>
            </tr>
            <cherry:show domId="BINOLMBMBM09_01">
	            <tr class="hasSale" >
	              <th>
	              	  <s:text name="binolmbmbm09_saleSubBrand"></s:text>
	              </th>
	              <td colspan="3">
	              	  <span><s:select list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value" name="saleSubBrand" headerKey="" headerValue="%{#select_default}"></s:select></span>
	              </td>
	            </tr>
            </cherry:show>
            <tr class="hasSale">
              <th><s:text name="global.page.payQuantity" /></th>
              <td>
                <span><s:textfield name="payQuantityStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="payQuantityEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>
              <th><s:text name="binolmbmbm09_payAmount" /></th>
              <td>
                <span><s:textfield name="payAmountStart" cssClass="text" cssStyle="width:75px;"></s:textfield></span><span>-<s:textfield name="payAmountEnd" cssClass="text" cssStyle="width:75px;"></s:textfield></span>
              </td>
            </tr>
            <tr class="hasSale">
              <th><s:text name="binolmbmbm09_saleProduct" /></th>
              <td colspan="3">
			   	<div class="right" style="margin:2px 0px;">
				<a class="add" onClick="binolmbmbm09.openProDialog('0');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
				</a>
				<a class="add" onClick="binolmbmbm09.openProTypeDialog('0');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selProCat" /></span>
				</a>
				</div>
				<div class="right">
			   		<s:text name="global.page.relation" />
			   		<input id="relation2" type="radio" value="2" name="relation" checked="checked">
					<label for="relation2"><s:text name="global.page.relation2" /></label>
			   		<input id="relation1" type="radio" value="1" name="relation">
					<label for="relation1"><s:text name="global.page.relation1" /></label>
			   	</div>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable" class="hide">
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:35%;"><s:text name="global.page.productname" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="proShowDiv"></tbody>
				</table>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTypeTable" class="hide">
				  <thead>
					<tr>
					  <th style="width:35%;"><s:text name="global.page.cateVal" /></th>
					  <th style="width:50%;"><s:text name="global.page.cateValName" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="proTypeShowDiv"></tbody>
				</table>
              </td>
            </tr>
            <tr class="hasSale">
              <th><s:text name="binolmbmbm09_notSaleProduct" /></th>
              <td colspan="3">
			   	<div class="right" style="margin:2px 0px;">
				<a class="add" onClick="binolmbmbm09.openProDialog('1');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
				</a>
				<a class="add" onClick="binolmbmbm09.openProTypeDialog('1');return false;">
					<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selProCat" /></span>
				</a>
				</div>
				<div class="right">
			   		<s:text name="global.page.relation" />
			   		<input id="notRelation2" type="radio" value="2" name="notRelation" checked="checked">
					<label for="notRelation2"><s:text name="global.page.relation2" /></label>
			   		<input id="notRelation1" type="radio" value="1" name="notRelation">
					<label for="notRelation1"><s:text name="global.page.relation1" /></label>
			   	</div>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="notProTable" class="hide">
				  <thead>
					<tr>
					  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
					  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
					  <th style="width:35%;"><s:text name="global.page.productname" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="notProShowDiv"></tbody>
				</table>
				<table cellspacing="0" cellpadding="0" style="width:100%;" id="notProTypeTable" class="hide">
				  <thead>
					<tr>
					  <th style="width:35%;"><s:text name="global.page.cateVal" /></th>
					  <th style="width:50%;"><s:text name="global.page.cateValName" /></th>
					  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
					</tr>
				  </thead>
				  <tbody id="notProTypeShowDiv"></tbody>
				</table>
              </td>
            </tr>
            
            <tr class="notSale hide">
              <th><s:text name="binolmbmbm09_notSaleTime" /></th>
              <td colspan="3" style="width:85%">
                <span>
                <select name="notSaleTimeMode" onchange="binolmbmbm09.selectDateMode(this, 1);">
					<option value="-1" <s:if test="%{notSaleTimeMode == -1}">selected</s:if>><s:text name="global.page.select"/></option>
					<option value="0" <s:if test="%{notSaleTimeMode == 0}">selected</s:if>><s:text name="global.page.dynamic"/></option>
					<option value="9" <s:if test="%{notSaleTimeMode == 9}">selected</s:if>><s:text name="global.page.custom"/></option>
					<option value="8" <s:if test="%{notSaleTimeMode == 8}">selected</s:if>><s:text name="global.page.lastSaleDay"/></option>
				</select>
				</span>
                <span class='<s:if test="%{notSaleTimeMode != 0}">hide</s:if>'>
                <s:text name="global.page.notSaleDateDes" />
                <s:textfield name="notSaleTimeRange" cssClass="text" cssStyle="width:30px;"></s:textfield>
                <s:select list='#application.CodeTable.getCodes("1239")' listKey="CodeKey" listValue="Value" name="notSaleTimeUnit" cssStyle="width:auto;"></s:select>
                <s:text name="global.page.notSaleTimeUnitFlag" />
                </span>
                <span class='<s:if test="%{notSaleTimeMode != 9}">hide</s:if>'>
                <span><s:textfield name="notSaleTimeStart" cssClass="date" /></span><span>-<s:textfield name="notSaleTimeEnd" cssClass="date" /></span>
                </span>
                <span class='<s:if test="%{notSaleTimeMode != 8}">hide</s:if>'>
                <s:text name="global.page.notSaleDateDes" />
                <s:textfield name="notSaleTimeRangeLast" cssClass="text" cssStyle="width:30px;"></s:textfield>
             	<s:text name="global.page.Day"/>
                <s:text name="global.page.notSaleTimeUnitFlag" />
                </span>
              </td>
            </tr>
          </tbody>
        </table>
        
      </div>
    </div>
    
    <div class="box-header"> 
      <strong><span class="icon icon-ttl-search"></span><s:text name="binolmbmbm09_campaignRecord" /></strong>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
        <table class="detail" cellpadding="0" cellspacing="0">
          <tbody>
            <tr>
              <th><s:text name="binolmbmbm09_participateTime" /></th>
              <td><span><s:textfield name="participateTimeStart" cssClass="date" /></span><span>-<s:textfield name="participateTimeEnd" cssClass="date" /></span></td>
              <th><s:text name="binolmbmbm09_campaignCounterId" /></th>
              <td>
                <s:hidden name="campaignCounterId"></s:hidden>
                <span id="campaignCounterDiv" style="line-height: 18px;"></span>
                <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
                <a class="add" onclick="binolmbmbm09.popCampaignCounterList('${searchCounterInitUrl}');return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
              </td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm09_campaignCode" /></th>
              <td colspan="3">
                <s:hidden name="campaignMode"></s:hidden>
                <s:hidden name="campaignCode"></s:hidden>
                <span id="campaignDiv" style="line-height: 18px;"></span>
                <s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:url>
                <a class="add" onclick="binolmbmbm09.popCampaignList('${searchCampaignInitUrl}');return false;">
				  <span class="ui-icon icon-search"></span>
				  <span class="button-text"><s:text name="global.page.Popselect" /></span>
			    </a>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
    
    <s:if test="%{extendPropertyList != null && !extendPropertyList.isEmpty()}">
    <div class="box-header"> 
      <strong><span class="icon icon-ttl-search"></span><s:text name="binolmbmbm09_extendProperty"></s:text></strong>
    </div>
    <div class="box-content clearfix">
      <div style="padding: 15px 0px 5px;">
      	<table class="detail" cellpadding="0" cellspacing="0">
          <tbody>
          	<s:iterator value="extendPropertyList" id="questionMap" status="status">
          	<tr>
              <th style="width: 15%"><s:property value="%{#questionMap.questionItem}"/></th>
              <td style="width: 85%" class="td_point">
                <span>
                  <s:hidden name="%{'propertyInfoList['+#status.index+'].extendPropertyId'}" value="%{#questionMap.paperQuestionId}"></s:hidden>
      			  <s:hidden name="%{'propertyInfoList['+#status.index+'].propertyType'}" value="%{#questionMap.questionType}"></s:hidden>
                  <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_SINCHOICE}'>
		         	<select name="propertyInfoList[${status.index}].propertyValues">
		         		<option value=""><s:text name="global.page.select"></s:text></option>
		         		<s:iterator value="%{#questionMap.answerList}" id="answerMap">
		         			<option value='<s:property value="%{#answerMap.answer}"/>'><s:property value="%{#answerMap.answer}"/></option>
		         		</s:iterator>
		         	</select>
		          </s:if>
		          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_MULCHOICE}'>
	        		<s:if test="%{searchMode == 1}">
	        		<s:iterator value="%{#questionMap.answerList}" id="answerMap" status="answerStatus">
	        			<input type="checkbox" name="propertyInfoList[${status.index}].propertyValues" value='<s:property value="%{#answerMap.answer}"/>' /><s:property value="%{#answerMap.answer}"/>
	        		</s:iterator>
	        		</s:if>
	        		<s:else>
	        		<s:iterator value="%{#questionMap.answerList}" id="answerMap" status="answerStatus">
	        			<input type="checkbox" name="propertyInfoList[${status.index}].propertyValues" value="${answerStatus.index+1 }" /><s:property value="%{#answerMap.answer}"/>
	        		</s:iterator>
	        		</s:else>
		          </s:if>
		          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_ESSAY || #questionMap.questionType == #QUESTIONTYPE_APFILL}'>
	        		<s:textfield name="%{'propertyInfoList['+#status.index+'].propertyValues'}"></s:textfield>
		          </s:if>
                </span>
              </td>
            </tr>
            </s:iterator>
          </tbody>
        </table>
      </div>
    </div>
    </s:if>
    
    </div>
    
    <p class="clearfix">
      <button class="right search" onclick="binolmbmbm09.searchMember();return false;">
        <span class="ui-icon icon-search-big"></span>
        <span class="button-text"><s:text name="binolmbmbm09_searchMemInfo"></s:text></span>
      </button>
      <cherry:show domId="BINOLMBMBM01_06">
      <button class="right search" onclick="binolmbmbm09.searchMemSale();return false;">
        <span class="ui-icon icon-search-big"></span>
        <span class="button-text"><s:text name="binolmbmbm09_searchMemSale"></s:text></span>
      </button>
      </cherry:show>
    </p>
  </cherry:form>
</div>
<div class="section hide" id="memberInfo">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
    <div class="toolbar clearfix">
      <span class="left">
     	<cherry:show domId="BINOLMBMBM01_02">
     	<a id="export" class="export" onclick="binolmbmbm09.exportExcel('0');return false;">
          <span class="ui-icon icon-export"></span>
          <span class="button-text"><s:text name="global.page.exportExcel"/></span>
        </a>
        <a id="export" class="export" onclick="binolmbmbm09.exportCsv();return false;">
          <span class="ui-icon icon-export"></span>
          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
        </a>
        </cherry:show>
        <s:url action="BINOLMBMBM17_editValidFlag" id="editMemValidFlagUrl"></s:url>
        <cherry:show domId="BINOLMBMBM01_05">
        <a href="" class="add" onclick="binolmbmbm09.editValidFlag('enable','${editMemValidFlagUrl}');return false;">
           <span class="ui-icon icon-enable"></span>
           <span class="button-text"><s:text name="global.page.enable"/></span>
        </a>
        </cherry:show>
        <cherry:show domId="BINOLMBMBM01_04">
        <a href="" class="delete" onclick="binolmbmbm09.editValidFlag('disable','${editMemValidFlagUrl}');return false;">
           <span class="ui-icon icon-disable"></span>
           <span class="button-text"><s:text name="global.page.disable"/></span>
        </a>
        </cherry:show>
        <cherry:show domId="BINOLMBMBM01_08">
        <a href="" class="delete" onclick="binolmbmbm09.delAllMemInit();return false;">
           <span class="ui-icon icon-disable"></span>
           <span class="button-text"><s:text name="global.page.disableAll"/></span>
        </a>
        </cherry:show>
      </span>
      
      <span class="right">
    	<%-- 列设置按钮  --%>
    	<a href="#" class="setting">
    		<span class="button-text"><s:text name="global.page.colSetting"/></span>
			<span class="ui-icon icon-setting"></span>
		</a>
      </span>
    </div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
      <thead>
        <tr>
          <th><input type="checkbox" id="checkAll" onclick="binolmbmbm09.checkAllRecord(this,'#dataTable_Cloned');"/></th>
          <th><s:text name="binolmbmbm09_name"></s:text></th>
          <th><s:text name="binolmbmbm09_memCode"></s:text></th>
          <th><s:text name="binolmbmbm09_nickname"></s:text></th>
          <th><s:text name="binolmbmbm09_mobilePhone"></s:text></th>
          <th><s:text name="binolmbmbm09_telephone"></s:text></th>
          <th><s:text name="binolmbmbm09_email"></s:text></th>
          <th><s:text name="binolmbmbm09_tencentQQ"></s:text></th>
          <th><s:text name="binolmbmbm09_mebSex"></s:text></th>
          <th><s:text name="binolmbmbm09_birthDay"></s:text></th>
          <th><s:text name="binolmbmbm09_memberLevel"></s:text></th>
          <th><s:text name="binolmbmbm09_creditRating"></s:text></th>
          <th><s:text name="binolmbmbm09_memberPoint"></s:text></th>
          <th><s:text name="binolmbmbm09_changablePoint"></s:text></th>
          <s:if test='%{!"3".equals(clubMod)}'>
          <th><s:text name="binolmbmbm09_ClubCounterName"></s:text></th>
          <th><s:text name="binolmbmbm09_clubBA"></s:text></th>
          <th><s:text name="binolmbmbm09_ClubJoinTime"></s:text></th>
          </s:if>
          <th><s:text name="binolmbmbm09_counterName"></s:text></th>
          <th><s:text name="binolmbmbm09_memBA"></s:text></th>
          <th><s:text name="binolmbmbm09_joinDate"></s:text></th>
          <th><s:text name="binolmbmbm09_memo1"></s:text></th>
          <th><s:text name="binolmbmbm09_memo2"></s:text></th>
          <th><s:text name="binolmbmbm09_status"></s:text></th>
          <s:if test='%{"3".equals(clubMod)}'>
          <th><s:text name="binolmbmbm09_referFlag"></s:text></th>
          </s:if>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>

<div class="section hide" id="memSaleInfo">
  <div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
  <div class="section-content">
    <div class="toolbar clearfix">
      <span class="left">
     	<cherry:show domId="BINOLMBMBM01_07">
     	<a id="export" class="export" onclick="binolmbmbm09.exportMemSaleExcel('0');return false;">
          <span class="ui-icon icon-export"></span>
          <span class="button-text"><s:text name="global.page.exportExcel"/></span>
        </a>
        <a id="export" class="export" onclick="binolmbmbm09.exportMemSaleCsv();return false;">
          <span class="ui-icon icon-export"></span>
          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
        </a>
        </cherry:show>
      </span>
      <span id="saleCountInfo" style="margin-left:10px;"></span>
    </div>
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="memSaleDataTable">
      <thead>
        <tr>
          <th><s:text name="binolmbmbm09_memCode"></s:text></th>
          <th><s:text name="binolmbmbm09_billCode"></s:text></th>
          <th><s:text name="binolmbmbm09_saleType"></s:text></th>
          <th><s:text name="binolmbmbm09_saleTime"></s:text></th>
          <cherry:show domId="BINOLMBMBM10_29">
          <th id="saleCouFlag"><s:text name="binolmbmbm09_counter"></s:text></th>
          </cherry:show>
          <th><s:text name="binolmbmbm09_amount"></s:text></th>
          <th><s:text name="binolmbmbm09_quantity"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>

<div class="hide">
<div id="deleteButton"><s:text name="global.page.delete" /></div>
<div id="selExclusiveFlag1"><s:text name="global.page.selExclusiveFlag1" /></div>
<div id="selExclusiveFlag2"><s:text name="global.page.selExclusiveFlag2" /></div>
<s:url action="BINOLMBMBM09_search" id="memberListUrl"></s:url>
<a href="${memberListUrl }" id="memberListUrl"></a>
<s:url action="BINOLMBMBM09_searchMemSale" id="memSaleListUrl"></s:url>
<a href="${memSaleListUrl }" id="memSaleListUrl"></a>
<s:url id="exporChecktUrl" action="BINOLMBMBM09_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
<s:url id="exportUrl" action="BINOLMBMBM09_export" ></s:url>
<a id="exportUrl" href="${exportUrl}"></a>
<s:url id="exportCsvUrl" action="BINOLMBMBM09_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exportMemSaleCheckUrl" action="BINOLMBMBM09_exportMemSaleCheck" ></s:url>
<a id="exportMemSaleCheckUrl" href="${exportMemSaleCheckUrl}"></a>
<s:url id="exportMemSaleUrl" action="BINOLMBMBM09_exportMemSale" ></s:url>
<a id="exportMemSaleUrl" href="${exportMemSaleUrl}"></a>
<s:url id="exportMemSaleCsvUrl" action="BINOLMBMBM09_exportMemSaleCsv" ></s:url>
<a id="exportMemSaleCsvUrl" href="${exportMemSaleCsvUrl}"></a>
<s:url action="BINOLMBMBM15_reMoveMemCounter" id="reMoveMemCounterUrl"></s:url>
<a id="reMoveMemCounterUrl" href="${reMoveMemCounterUrl}"></a>
<s:url action="BINOLMBMBM09_delAllMem" id="delAllMemUrl"></s:url>
<a id="delAllMemUrl" href="${delAllMemUrl}"></a>
<s:url action="BINOLMBMBM09_searchLevel" id="searchLevel_Url"></s:url>
<a href="${searchLevel_Url }" id="searchLevelUrl"></a>

<div id="moveMemCouDialogTitle"><s:text name="moveMemCouDialogTitle" /></div>
<div id="moveMemCouDialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="moveMemCouDialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="moveMemCouDialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="moveMemCouDialogHandle"><s:text name="global.page.handle" /></div>
<div id="moveMemCounterDiv">
<div style="padding: 30px 0 0 20px">
<form id="moveMemCounterForm">
<div style="margin-bottom: 25px">
<s:text name="subType"></s:text>
<input type="radio" name="subType" value="1" checked="checked" id="subType1" onclick="binolmbmbm09.changeMoveCouType(this);"/><label for="subType1"><s:text name="subType1"></s:text></label>
<input type="radio" name="subType" value="2" id="subType2" onclick="binolmbmbm09.changeMoveCouType(this);"/><label for="subType2"><s:text name="subType2"></s:text></label>
</div>
<div id="moveCouType1">
<div>
<s:text name="oldCounter" />
<span id="oldOrgDiv"></span>
<s:hidden name="oldOrgId"></s:hidden>
<s:hidden name="oldCounterCode"></s:hidden>
<s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
<a class="add" onclick="binolmbmbm09.popOldOrgDialog('${searchCounterInitUrl}');return false;">
<span class="ui-icon icon-search"></span>
<span class="button-text"><s:text name="global.page.Popselect" /></span>
</a>
</div>
<div style="margin-top: 20px;">
<s:text name="newCounter" />
<span id="newOrgDiv"></span>
<s:hidden name="newOrgId"></s:hidden>
<s:hidden name="newCounterCode"></s:hidden>
<s:hidden name="newCounterKind"></s:hidden>
<s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
<a class="add" onclick="binolmbmbm09.popNewOrgDialog('${searchCounterInitUrl}');return false;">
<span class="ui-icon icon-search"></span>
<span class="button-text"><s:text name="global.page.Popselect" /></span>
</a>
</div>
</div>
<div id="moveCouType2" class="hide">
<s:text name="batchCode"></s:text>
<s:textfield name="batchCode" cssClass="text"></s:textfield>
</div>
</form>
</div>
</div>

<div id="exportModeDiv">
<p class="message" style="margin: 0;text-align: left;padding: 10px 0;background: none repeat scroll 0 0 #F3F9EB;">
<label><s:text name="binolmbmbm09_exportMode"></s:text></label>
<select name="exportMode">
<option value="1"><s:text name="binolmbmbm09_exportMode1"></s:text></option>
<option value="2"><s:text name="binolmbmbm09_exportMode2"></s:text></option>
</select>
</p>
</div>

<div id="delAllMemDiv">
<p style="margin: 0;text-align: left;padding: 10px 0;background: none repeat scroll 0 0 #F3F9EB;font-size: 16px;">
<label><s:text name="binolmbmbm09_remark"></s:text></label>
<s:textarea name="remark" cssClass="text"></s:textarea>
</p>
<p style="margin: 0;text-align: left;padding: 10px 0 0;border-top: 1px dotted #CCCCCC;font-size: 16px;">
<s:text name="binolmbmbm09_delAllMemMessage" />
</p>
</div>
<div class="hide" id="dialogClubWarn"></div>
<div id="disableTitle"><s:text name="binolmbmbm09_disableTitle" /></div>
<div id="enableTitle"><s:text name="binolmbmbm09_enableTitle" /></div>
<div id="disableMessage"><p class="message"><span><s:text name="binolmbmbm09_disableMessage" /></span></p></div>
<div id="enableMessage"><p class="message"><span><s:text name="binolmbmbm09_enableMessage" /></span></p></div>
<div id="delAllMemMessage"><p class="message"><span><s:text name="binolmbmbm09_delAllMemMessage" /></span></p></div>
<div id="delAllMemCheckMes"><s:text name="binolmbmbm09_delAllMemCheckMes" /></div>
<div id="dialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="dialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="dialogClose"><s:text name="global.page.dialogClose" /></div>
<div id="exportTitle"><s:text name="binolmbmbm09_exportTitle" /></div>
<div id="exportText"><s:text name="binolmbmbm09_exportText" /></div>
<div id="dialogHandle"><s:text name="global.page.handle" /></div>
<div id="clubWarnMsg"><s:text name="binolmbmbm09_clubWarnMsg" /></div>
<div id="clubSelMsg"><s:text name="binolmbmbm09_clubSelMsg" /></div>
</div>
</s:i18n>  
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
