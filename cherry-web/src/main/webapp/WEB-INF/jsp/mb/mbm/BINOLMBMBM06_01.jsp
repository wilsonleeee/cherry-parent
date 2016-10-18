<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM06">
<tr>
 <th><s:text name="binolmbmbm06_levelName" /></th>
			  <td>
			  <span>
			  	<s:property value="memClubMap.levelName"/>
			  	<s:if test="%{memClubMap.levelEndDate != null}">
			  	(<s:property value="memClubMap.levelStartDate"/> ~
			  	<s:property value="memClubMap.levelEndDate"/>)
			  	</s:if>
			  	</span>
			  </td>
			  <th><s:text name="binolmbmbm06_isReceiveMsg" /></th>
			  <td><span>
			     <s:if test="%{memClubMap.isReceiveMsg == 1}">
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" checked="checked" disabled="disabled"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" disabled="disabled"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:if>
		        <s:elseif test="%{memClubMap.isReceiveMsg == 0}">
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" disabled="disabled"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" checked="checked" disabled="disabled"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:elseif>
		        <s:else>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg1" value="1" disabled="disabled"><label for="isReceiveMsg1"><s:text name="binolmbmbm06_isReceiveMsg1"></s:text></label>
		        <input type="radio" name="isReceiveMsg" id="isReceiveMsg0" value="0" disabled="disabled"><label for="isReceiveMsg0"><s:text name="binolmbmbm06_isReceiveMsg0"></s:text></label>
		        </s:else>
			  </span>
			  </td>	
        </tr> 
        <tr>
              <th><s:text name="binolmbmbm06_referrer"></s:text></th>
			  <td><span>
			 <s:textfield name="referrerClub" value="%{memClubMap.referrerClub}" cssClass="text" maxlength="20" disabled="true" id="referrerClub"></s:textfield>
			 <s:hidden name="clubReferIdOld" value="%{memClubMap.referrerIdClub}" id="clubReferIdOld"></s:hidden>
			  </span></td>	  
            <th><s:text name="binolmbmbm06_joinTimeCB"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
			  <span class="hide club-edit">
			 	<s:textfield name="joinTimeClub" cssClass="date" value="%{memClubMap.joinTimeClub}"></s:textfield>
			  </span>
			  <span class="club-dtl">
			 	<s:property value="memClubMap.joinTimeClub"/>
			  </span>
			  </td> 
         </tr>
             <tr>
              <th><s:text name="binolmbmbm06_counterCodeCB"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
			  <span class="hide club-edit">
		      <s:hidden name="organizationIdClub" value="%{memClubMap.organizationIdClub}"></s:hidden>
		      <s:hidden name="organizationCodeClub" value="%{memClubMap.counterCodeClub}"></s:hidden>
		      <s:hidden name="counterKindClub" value="%{memClubMap.counterKindClub}"></s:hidden>
		      <span id="counterDivClub" style="line-height: 18px;">
		      <s:if test='%{memClubMap.counterCodeClub != null && !"".equals(memClubMap.counterCodeClub)}'>
		      <s:if test='%{memClubMap.counterNameClub != null && !"".equals(memClubMap.counterNameClub)}'>
		      (<s:property value="memClubMap.counterCodeClub"/>)<s:property value="memClubMap.counterNameClub"/>
		      </s:if>
		      <s:else>
		      <s:property value="memClubMap.counterCodeClub"/>
		      </s:else>
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,1,'1');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initCounterDialog" namespace="/common" id="searchCounterInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popCounterList('${searchCounterInitUrl}','1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      <span class="club-dtl">
			 	 <s:if test='%{memClubMap.counterNameClub != null && !"".equals(memClubMap.counterNameClub)}'>
	  		    (<s:property value="memClubMap.counterCodeClub"/>)<s:property value="memClubMap.counterNameClub"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memClubMap.counterCodeClub != null && !"".equals(memClubMap.counterCodeClub)}'>
	  		    	<s:property value="memClubMap.counterCodeClub"/>
	  		  	</s:if>
	  		  </s:else>
			  </span>
			  </td>
			  <th><s:text name="binolmbmbm06_employeeCB"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
			  <span class="hide club-edit">
		      <s:hidden name="employeeIdClub" value="%{memClubMap.employeeIdClub}"></s:hidden>
		      <s:hidden name="employeeCodeClub" value="%{memClubMap.employeeCodeClub}"></s:hidden>
		      <span id="employeeDivClub" style="line-height: 18px;">
		      <s:if test='%{memClubMap.employeeCodeClub != null && !"".equals(memClubMap.employeeCodeClub)}'>
		      <s:if test='%{memClubMap.employeeNameClub != null && !"".equals(memClubMap.employeeNameClub)}'>
		      (<s:property value="memClubMap.employeeCodeClub"/>)<s:property value="memClubMap.employeeNameClub"/>
		      </s:if>
		      <s:else>
		      <s:property value="memClubMap.employeeCodeClub"/>
		      </s:else>		      
		      <span class="close" style="margin: 0 10px 2px 5px;" onclick="binolmbcom01.delHtml(this,2,'1');"><span class="ui-icon ui-icon-close" style="background-position: -80px -129px;"></span></span>
		      </s:if>
		      </span>
		      <s:url action="BINOLCM02_initEmployeeDialog" namespace="/common" id="searchEmployeeInitUrl"></s:url>
		      <a class="add" onclick="binolmbcom01.popEmployeeList('${searchEmployeeInitUrl}','${selEmpErrorMsg}','1');return false;"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="global.page.Popselect"></s:text></span></a>
		      </span>
		      <span class="club-dtl">
			 <s:if test='%{memClubMap.employeeNameClub != null && !"".equals(memClubMap.employeeNameClub)}'>
	  		    (<s:property value="memClubMap.employeeCodeClub"/>)<s:property value="memClubMap.employeeNameClub"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memClubMap.employeeCodeClub != null && !"".equals(memClubMap.employeeCodeClub)}'>
	  		    	<s:property value="memClubMap.employeeCodeClub"/>
	  		  	</s:if>
	  		  </s:else>
			  </span>
			  </td>
			  </tr>
</s:i18n>