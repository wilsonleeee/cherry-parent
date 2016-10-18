<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<s:i18n name="i18n.mb.BINOLMBMBM02">
 <tr>
          <th><s:text name="binolmbmbm02_levelName" /></th>
	 <td><span>
	 			<s:property value="memClubMap.levelName"/>
			  	<s:if test="%{memClubMap.levelEndDate != null}">
			  	(<s:property value="memClubMap.levelStartDate"/> ~
			  	<s:property value="memClubMap.levelEndDate"/>)
			  	</s:if>
			  	</span>
			 </td>
	 <th><s:text name="binolmbmbm02_isReceiveMsg" /></th>
	 <td><span>
	   <s:if test="%{memClubMap.isReceiveMsg == 1}">
	 	  <s:text name="binolmbmbm02_isReceiveMsg1"></s:text>
	   </s:if>
	   <s:elseif test="%{memClubMap.isReceiveMsg == 0}">
	     <s:text name="binolmbmbm02_isReceiveMsg0"></s:text>
	   </s:elseif>
	 </span></td>			  
	        </tr>
	        <tr>
              <th><s:text name="binolmbmbm02_referrer"></s:text></th>
			  <td><span>
			 <s:if test='%{memClubMap.recommendNameClub != null && !"".equals(memClubMap.recommendNameClub)}'>
	  		    (<s:property value="memClubMap.referrerClub"/>)<s:property value="memClubMap.recommendNameClub"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memClubMap.referrerClub != null && !"".equals(memClubMap.referrerClub}'>
	  		    	<s:property value="memClubMap.referrerClub"/>
	  		  	</s:if>
	  		  </s:else>
			  </span></td>
			  <th><s:text name="binolmbmbm02_joinTimeCB"></s:text></th>
			  <td>
			  <span>
			 <s:property value="memClubMap.joinTimeClub"/>
			  </span>
			  </td>	
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_counterCodeCB"></s:text></th>
			  <td>
			  <span>
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
			  <th><s:text name="binolmbmbm02_employeeCB"></s:text></th>
			  <td>
			  <span>
			 <s:if test='%{memClubMap.employeeNameClub != null && !"".equals(memClubMap.employeeNameClub)}'>
	  		    (<s:property value="memClubMap.employeeCodeClub"/>)<s:property value="memberInfoMap.employeeNameClub"/>
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