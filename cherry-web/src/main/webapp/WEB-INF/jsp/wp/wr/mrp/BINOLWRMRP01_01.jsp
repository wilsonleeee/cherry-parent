<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.mb.BINOLMBMBM02">


	

	
	  <%-- 会员基本信息 --%>
	  <div class="section">
      	<div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-info"></span>
            <s:text name="global.page.title"/>
          </strong>
	    </div>
        <div class="section-content">
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm02_memCode"></s:text></th>
			  <td><span><s:property value="memberInfoMap.memCode"/></span></td>
			  <th><s:text name="binolmbmbm02_levelName"></s:text></th>
			  <td><span><s:property value="memberInfoMap.levelName"/>
			  <s:if test="%{memberInfoMap.levelEndDate != null}">
			  	(<s:text name="binolmbmbm02_levelTimeLimit"></s:text>：<s:property value="memberInfoMap.levelStartDate"/>~<s:property value="memberInfoMap.levelEndDate"/>)
			  </s:if>
			  </span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_name"></s:text></th>
			  <td><span><s:property value="memberInfoMap.name"/></span></td>
			  <th><s:text name="binolmbmbm02_counterCode"></s:text></th>
			  <td><span>
			  <s:if test='%{memberInfoMap.counterName != null && !"".equals(memberInfoMap.counterName)}'>
	  		    (<s:property value="memberInfoMap.counterCode"/>)<s:property value="memberInfoMap.counterName"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memberInfoMap.counterCode != null && !"".equals(memberInfoMap.counterCode)}'>
	  		    	<s:property value="memberInfoMap.counterCode"/>
	  		  	</s:if>
	  		  </s:else>
			  </span></td>
            </tr>
            <tr>
			  <th><s:text name="binolmbmbm02_memberBirthDay"></s:text></th>
			  <td><span><s:property value="memberInfoMap.birth"/></span></td>
			  <th><s:text name="binolmbmbm02_employeeCode"></s:text></th>
			  <td><span>
			  <s:if test='%{memberInfoMap.employeeName != null && !"".equals(memberInfoMap.employeeName)}'>
	  		    (<s:property value="memberInfoMap.employeeCode"/>)<s:property value="memberInfoMap.employeeName"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memberInfoMap.employeeCode != null && !"".equals(memberInfoMap.employeeCode)}'>
	  		    	<s:property value="memberInfoMap.employeeCode"/>
	  		  	</s:if>
	  		  </s:else>
			  </span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_gender"></s:text></th>
			  <td><span><s:property value='#application.CodeTable.getVal("1006", memberInfoMap.gender)' /></span></td>
              <th><s:text name="binolmbmbm02_joinDate"></s:text></th>
			  <td><span><s:property value="memberInfoMap.joinDate"/></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_mobilePhone"></s:text></th>
			  <td><span><s:property value="memberInfoMap.mobilePhone"/></span></td>
			  <th><s:text name="binolmbmbm02_telephone"></s:text></th>
			  <td><span><s:property value="memberInfoMap.telephone"/></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_email"></s:text></th>
			  <td><span><s:property value="memberInfoMap.email"/></span></td>
			  <th><s:text name="binolmbmbm02_testType"></s:text></th>
			  <td><span>
			  <s:if test='%{memberInfoMap.testType != null && memberInfoMap.testType == 0}'>
			    <s:text name="binolmbmbm02_testType0" />
			  </s:if>
			  <s:else>
			    <s:text name="binolmbmbm02_testType1" />
			  </s:else>
			  </span></td>
            </tr>  
          </table>
          
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm02_totalPoint"></s:text></th>
			  <td><span><s:property value="memberInfoMap.totalPoint"/></span></td>
			  <th><s:text name="binolmbmbm02_firstSaleCounter"></s:text></th>
			  <td><span>
			  <s:if test='%{memberInfoMap.firstSaleCounterName != null && !"".equals(memberInfoMap.firstSaleCounterName)}'>
	  		    (<s:property value="memberInfoMap.firstSaleCounterCode"/>)<s:property value="memberInfoMap.firstSaleCounterName"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memberInfoMap.firstSaleCounterCode != null && !"".equals(memberInfoMap.firstSaleCounterCode)}'>
	  		    	<s:property value="memberInfoMap.firstSaleCounterCode"/>
	  		  	</s:if>
	  		  </s:else>
			  </span></td>			  
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_changablePoint"></s:text></th>
			  <td><span><s:property value="memberInfoMap.changablePoint"/></span></td>
              <th><s:text name="binolmbmbm02_firstSaleDate"></s:text></th>
			  <td><span><s:property value="memberInfoMap.firstSaleDate"/></span></td>
            </tr> 
            <tr>
              <th><s:text name="binolmbmbm02_initTotalAmount"></s:text></th>
			  <td><span><s:property value="memberInfoMap.initTotalAmount"/></span></td>
			  <th><s:text name="binolmbmbm02_lastSaleDate"></s:text></th>
			  <td><span><s:property value="memberInfoMap.lastSaleDate"/></span></td>
            </tr>
          </table>
          
          <table class="detail" cellpadding="0" cellspacing="0">
		    <tr>
		      <th><s:text name="binolmbmbm02_identityCard"></s:text></th>
			  <td><span><s:property value="memberInfoMap.identityCard"/></span></td>
			  <th><s:text name="binolmbmbm02_referrer"></s:text></th>
			  <td><span>
			  <s:if test='%{memberInfoMap.recommendName != null && !"".equals(memberInfoMap.recommendName)}'>
	  		    (<s:property value="memberInfoMap.referrer"/>)<s:property value="memberInfoMap.recommendName"/>
	  		  </s:if>
	  		  <s:else>
	  		  	<s:if test='%{memberInfoMap.referrer != null && !"".equals(memberInfoMap.referrer)}'>
	  		    	<s:property value="memberInfoMap.referrer"/>
	  		  	</s:if>
	  		  </s:else>
			  </span></td>
		    </tr>
		    <tr>
              <th><s:text name="binolmbmbm02_blogId"></s:text></th>
			  <td><span><s:property value="memberInfoMap.blogId"/></span></td>
			  <th><s:text name="binolmbmbm02_channelCode"></s:text></th>
			  <td><span><s:property value='#application.CodeTable.getVal("1301", memberInfoMap.channelCode)' /></span></td>
			  
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_messageId"></s:text></th>
			  <td><span><s:property value="memberInfoMap.messageId"/></span></td>
			  <th><s:text name="binolmbmbm02_isReceiveMsg"></s:text></th>
			  <td><span>
			    <s:if test="%{memberInfoMap.isReceiveMsg == 1}">
			  	  <s:text name="binolmbmbm02_isReceiveMsg1"></s:text>
			    </s:if>
			    <s:elseif test="%{memberInfoMap.isReceiveMsg == 0}">
			      <s:text name="binolmbmbm02_isReceiveMsg0"></s:text>
			    </s:elseif>
			  </span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_wechatBindTime"></s:text></th>
			  <td><span><s:property value="memberInfoMap.wechatBindTime"/></span></td>
			  <th><s:text name="binolmbmbm02_active"></s:text></th>
			  <td><span>
			    <s:if test="%{memberInfoMap.active == 1}">
			  	  <s:text name="binolmbmbm02_active1"></s:text>
			    </s:if>
			    <s:elseif test="%{memberInfoMap.active == 0}">
			      <s:text name="binolmbmbm02_active0"></s:text>
			    </s:elseif>
			  </span></td>
            </tr>
		    <tr>
			  <th><s:text name="binolmbmbm02_profession"></s:text></th>
			  <td><span><s:property value='#application.CodeTable.getVal("1236", memberInfoMap.profession)' /></span></td>
			  <th><s:text name="binolmbmbm02_activeDate"></s:text></th>
			  <td><span><s:property value="memberInfoMap.activeDate"/></span></td>
			</tr>
			<tr>
              <th><s:text name="binolmbmbm02_maritalStatus"></s:text></th>
			  <td><span><s:property value='#application.CodeTable.getVal("1043", memberInfoMap.maritalStatus)' /></span></td>
			  <th><s:text name="binolmbmbm02_activeChannel"></s:text></th>
			  <td><span><s:property value='#application.CodeTable.getVal("1298", memberInfoMap.activeChannel)' /></span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm02_connectTime"></s:text></th>
			  <td colspan="3"><span><s:property value="memberInfoMap.connectTimeValue" /></span></td>
            </tr> 
            <s:if test='%{#application.CodeTable.getCodes("1204") != null && !#application.CodeTable.getCodes("1204").isEmpty()}'> 
			<tr>
			  <th><s:text name="binolmbmbm02_memType"></s:text></th>
			  <td colspan="3"><span><s:property value='#application.CodeTable.getVal("1204", memberInfoMap.memType)' /></span></td>
			</tr> 
			</s:if>
			<tr>
      	      <th><s:text name="binolmbmbm02_memo1"></s:text></th>
			  <td colspan="3"><span><s:property value="memberInfoMap.memo1" /></span></td>
      	    </tr>
      	    <tr>
      	      <th><s:text name="binolmbmbm02_memo2"></s:text></th>
			  <td colspan="3"><span><s:property value="memberInfoMap.memo2" /></span></td>
      	    </tr>
          </table>
	    </div>
	  </div>
			
	  <%--=================== 会员持卡信息 ===================== --%>
	  <div class="section">
        <div class="section-header">
       	  <strong>
           	<span class="ui-icon icon-ttl-section-info"></span>
           	<s:text name="binolmbmbm02_memCardInfo" />
          </strong>
	    </div>
        <div class="section-content">
		  <table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
		    <thead>
			  <tr>
			    <th width="25%"><s:text name="binolmbmbm02_memCode" /></th>
			    <th width="25%"><s:text name="binolmbmbm02_memCodeCounterCode" /></th>
			    <th width="20%"><s:text name="binolmbmbm02_memCodeBaCode" /></th>
			    <th width="20%"><s:text name="binolmbmbm02_grantDate" /></th>
			    <th width="10%"><s:text name="binolmbmbm02_cardValidFlag" /></th>
			  </tr>
			</thead>
			<tbody>
			<s:if test="%{memberInfoMap.memCardInfoList != null}">
			  <s:iterator value="%{memberInfoMap.memCardInfoList}" id="memCardInfoMap">
			    <tr>
		  		  <td><span><s:property value="#memCardInfoMap.memCode"/></span></td>
		  		  <td><span>
		  		  <s:if test='%{#memCardInfoMap.memCodeCouName != null && !"".equals(#memCardInfoMap.memCodeCouName)}'>
		  		    (<s:property value="#memCardInfoMap.memCodeCouCode"/>)<s:property value="#memCardInfoMap.memCodeCouName"/>
		  		  </s:if>
		  		  <s:else>
		  		  	<s:if test='%{#memCardInfoMap.memCodeCouCode != null && !"".equals(#memCardInfoMap.memCodeCouCode)}'>
		  		    	<s:property value="memCardInfoMap.memCodeCouCode"/>
		  		  	</s:if>
		  		  </s:else>
		  		  </span></td>
		  		  <td><span>
		  		  <s:if test='%{#memCardInfoMap.memCodeEmpName != null && !"".equals(#memCardInfoMap.memCodeEmpName)}'>
		  		    (<s:property value="#memCardInfoMap.memCodeEmpCode"/>)<s:property value="#memCardInfoMap.memCodeEmpName"/>
		  		  </s:if>
		  		  <s:else>
		  		  	<s:if test='%{#memCardInfoMap.memCodeEmpCode != null && !"".equals(#memCardInfoMap.memCodeEmpCode)}'>
		  		    	<s:property value="memCardInfoMap.memCodeEmpCode"/>
		  		  	</s:if>
		  		  </s:else>
		  		  </span></td>
		  		  <td><span><s:property value="#memCardInfoMap.grantDate"/></span></td>
		  		  <td>
		  		  <s:if test="%{#memCardInfoMap.cardValidFlag == 1}"><span class='ui-icon icon-valid'></span></s:if>
				  <s:elseif test="%{#memCardInfoMap.cardValidFlag == 0}"><span class='ui-icon icon-invalid'></span></s:elseif>
				  <s:else><span></span></s:else>
		  		  </td>
	  			</tr>
			  </s:iterator>
	  		</s:if>
			</tbody>
		  </table>
	    </div>
	  </div>
	  
	  <s:if test="%{memberInfoMap.memberAddressInfo != null}">
	  <s:set value="%{memberInfoMap.memberAddressInfo}" var="memberAddressInfo"></s:set>
	  <div class="section">
        <div class="section-header">
      	  <strong>
      	    <span class="ui-icon icon-ttl-section-info"></span>
      	    <s:text name="binolmbmbm02_memAddressTitle"></s:text>
      	  </strong>
        </div>
        <div class="section-content">
	      	<table class="detail" cellpadding="0" cellspacing="0">
	          <tr>
			    <th><s:text name="binolmbmbm02_provinceId"></s:text></th>
			    <td><span><s:property value="%{#memberAddressInfo.provinceName}"/></span></td>
			    <th><s:text name="binolmbmbm02_address"></s:text></th>
				<td><span><s:property value="%{#memberAddressInfo.addressLine1}"/></span></td>
			  </tr>
			  <tr>
			    <th><s:text name="binolmbmbm02_cityId"></s:text></th>
				<td><span><s:property value="%{#memberAddressInfo.cityName}"/></span></td>
				<th><s:text name="binolmbmbm02_postcode"></s:text></th>
			    <td><span><s:property value="%{#memberAddressInfo.zipCode}"/></span></td>
			  </tr>
			  <tr>
			  	<th><s:text name="binolmbmbm02_countyId"></s:text></th>
				<td colspan="3"><span><s:property value="%{#memberAddressInfo.countyName}"/></span></td>
			  </tr>
	        </table>
	    </div>
	  </div>
	  </s:if>
	  
	  <%-- 会员问卷信息 --%>
	  <s:if test="%{memberInfoMap.memPagerList != null}">
		  <div class="section">
      		<div class="section-header">
	          <strong>
	            <span class="ui-icon icon-ttl-section-info"></span>
	            <s:text name="binolmbmbm02_extendProperty"></s:text>
	          </strong>
		    </div>
	        <div class="section-content">
	          <table class="detail" cellpadding="0" cellspacing="0">
	            <s:iterator value="memberInfoMap.memPagerList" id="questionMap" status="status">
	              <s:if test="%{#status.index%2 == 0}"><tr></s:if>
	                <th><s:property value="#questionMap.questionItem"/></th>
				    <td class="td_point"><span><s:property value="#questionMap.answer"/></span></td>
				  <s:if test="%{#status.index%2 != 0}"></tr></s:if>  
	            </s:iterator>
	          </table>
		    </div>
		  </div>
	  </s:if>

	

</s:i18n>