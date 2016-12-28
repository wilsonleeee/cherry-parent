<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>
<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>
<s:set id="QUESTIONTYPE_ESSAY"><%=MonitorConstants.QUESTIONTYPE_ESSAY %></s:set>
<s:set id="QUESTIONTYPE_APFILL"><%=MonitorConstants.QUESTIONTYPE_APFILL %></s:set>
<script type="text/javascript" src="/Cherry/js/wp/mbm/BINOLWPMBM01_03.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM06">
<s:text name="global.page.select" id="select_default"/>
<div class="crm_top clearfix">
	<div><s:text name="binolmbmbm06_memberEdit" /></div>
	
	<span class="right" style="padding-top:6px;">
	<a class="add back">
		<span class="ui-icon icon-import"></span>
		<span class="button-text"><s:text name="global.page.back"/></span>
	</a>
	</span>
	
</div>
<div class="wp_main">
    <div class="wp_left">
        <div class="wp_content" style="margin-right: 0;padding-right: 0.5em">
        	<div class="wp_leftbox">
            	
    <div id="actionResultDisplay"></div>
    <form id="saveForm">
      <s:hidden name="memberInfoId" value="%{memberInfoMap.memberInfoId}"></s:hidden>
      <s:hidden name="modifyTime" value="%{memberInfoMap.updateTime}"></s:hidden>
      <s:hidden name="modifyCount" value="%{memberInfoMap.modifyCount}"></s:hidden>
      <s:hidden name="memCodeOld" value="%{memberInfoMap.memCode}"></s:hidden>
      <s:hidden name="cardCount" value="%{memberInfoMap.cardCount}"></s:hidden>
      <s:hidden name="activeOld" value="%{memberInfoMap.active}"></s:hidden>
      <s:hidden name="version" value="%{memberInfoMap.version}"></s:hidden>
      <s:hidden name="birthOld" value="%{memberInfoMap.birth}"></s:hidden>
      <s:hidden name="referrerIdOld" value="%{memberInfoMap.referrerId}"></s:hidden>
      <s:hidden name="initTotalAmountOld" value="%{memberInfoMap.initTotalAmount}"></s:hidden>
      <s:hidden name="status" value="%{memberInfoMap.status}"></s:hidden>
      <s:hidden name="mobilePhoneOld" value="%{memberInfoMap.mobilePhone}"></s:hidden>
      
      <s:hidden name="joinDate" value="%{memberInfoMap.joinDate}"></s:hidden>
      <s:hidden name="organizationId" value="%{memberInfoMap.organizationId}"></s:hidden>
      <s:hidden name="organizationCode" value="%{memberInfoMap.counterCode}"></s:hidden>
      <s:hidden name="counterKind" value="%{memberInfoMap.counterKind}"></s:hidden>
      <s:hidden name="employeeId" value="%{memberInfoMap.employeeId}"></s:hidden>
	  <s:hidden name="employeeCode" value="%{memberInfoMap.employeeCode}"></s:hidden>
      <s:hidden name="isReceiveMsg" value="%{memberInfoMap.isReceiveMsg}"></s:hidden>
      <s:hidden name="active" value="%{memberInfoMap.active}"></s:hidden>
      <s:iterator value="memberInfoMap.connectTime" id="connectTime">
      	<s:hidden name="connectTime" value="%{#connectTime}"></s:hidden>
      </s:iterator>
      <s:hidden name="memType" value="%{memberInfoMap.memType}"></s:hidden>
      <s:hidden name="initTotalAmount" value="%{memberInfoMap.initTotalAmount}"></s:hidden>
      <s:hidden name="channelCode" value="%{memberInfoMap.channelCode}"></s:hidden>
		<s:hidden name="isAllowUpdate" value="%{memberInfoMap.isAllowUpdate}"></s:hidden>
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
              <th><s:text name="binolmbmbm06_memCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		      <td><span>
		      <span style="line-height: 18px; margin-right: 10px;"><s:property value="%{memberInfoMap.memCode}"/></span>		      
		      <s:if test='%{cardMobileSyn == "Y"}'>
		      <s:textfield name="memCode" value="%{memberInfoMap.memCode}" cssClass="text" maxlength="20" cssStyle="display:none" onkeyup="binolwpmbm01.synMoblie(this);return false;" onchange="binolwpmbm01.synMoblie(this);return false;"></s:textfield>
		      </s:if>
		      <s:else>
		      <s:textfield name="memCode" value="%{memberInfoMap.memCode}" cssClass="text" maxlength="20" cssStyle="display:none"></s:textfield>
		      </s:else>
		      <s:if test="%{memberInfoMap.status == 1}">
		      <a class="delete" id="editMemCode"><span class="ui-icon icon-edit"></span><span class="button-text" id="editMemCodeButton"><s:text name="global.page.changeMemCode"></s:text></span></a>
		      </s:if>
		      </span></td>
		      <th><s:text name="binolmbmbm06_mobilePhone"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span>
			  <s:if test='%{cardMobileSyn == "Y"}'>
			  <s:textfield name="mobilePhone" value="%{memberInfoMap.mobilePhone}" maxlength="20" cssClass="text" readonly="true"></s:textfield>
			  </s:if>
			  <s:else>
			  <s:textfield name="mobilePhone" value="%{memberInfoMap.mobilePhone}" maxlength="20" cssClass="text"></s:textfield>
			  </s:else>
			  </span></td>
            </tr>
            <tr>
              <th><s:text name="binolmbmbm06_name"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:textfield name="memName" value="%{memberInfoMap.name}" maxlength="30" cssClass="text"></s:textfield></span></td>
		      <th><s:text name="binolmbmbm06_telephone"></s:text></th>
			  <td><span><s:textfield name="telephone" value="%{memberInfoMap.telephone}" maxlength="20" cssClass="text"></s:textfield></span></td>
            </tr>
            <tr> 
			  <th><s:text name="binolmbmbm06_memberBirthDay"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td>
				  <span>
					  <s:if test='memberInfoMap.isAllowUpdate=="1"'>
						  <s:textfield name="birth" value="%{memberInfoMap.birth}" cssClass="date"></s:textfield>
					  </s:if>
					  <s:else>
						  <s:property value="%{memberInfoMap.birth}"/>
					  </s:else>
				  </span>
			  </td>
			  <th><s:text name="binolmbmbm06_email"></s:text></th>
			  <td><span><s:textfield name="email" value="%{memberInfoMap.email}" maxlength="60" cssClass="text"></s:textfield></span></td> 
            </tr>
            <tr>
			  <th><s:text name="binolmbmbm06_gender"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			  <td><span><s:radio list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="gender" value="%{memberInfoMap.gender}"></s:radio></span></td>
              <th><s:text name="binolmbmbm06_referrer"></s:text></th>
			  <td><span><s:textfield name="referrer" value="%{memberInfoMap.referrer}" cssClass="text" maxlength="20"></s:textfield></span></td>
            </tr>
          </table>
          
          <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
              <th><s:text name="binolmbmbm06_identityCard"></s:text></th>
			  <td><span><s:textfield name="identityCard" value="%{memberInfoMap.identityCard}" maxlength="18" cssClass="text"></s:textfield></span></td>
              <th><s:text name="binolmbmbm06_blogId"></s:text></th>
		      <td><span><s:textfield name="blogId" value="%{memberInfoMap.blogId}" cssClass="text" maxlength="30"></s:textfield></span></td>
            </tr>
		    <tr>
			  <th><s:text name="binolmbmbm06_profession"></s:text></th>
			  <td><span><s:select list='#application.CodeTable.getCodes("1236")' listKey="CodeKey" listValue="Value" name="profession" headerKey="" headerValue="%{#select_default}" value="%{memberInfoMap.profession}"></s:select></span></td>
			  <th><s:text name="binolmbmbm06_messageId"></s:text></th>
			  <td><span><s:textfield name="messageId" value="%{memberInfoMap.messageId}" cssClass="text" maxlength="30"></s:textfield></span></td>
			</tr>
			<tr>
			  <th><s:text name="binolmbmbm06_maritalStatus"></s:text></th>
			  <td colspan="3"><span><s:radio list='#application.CodeTable.getCodes("1043")' listKey="CodeKey" listValue="Value" name="maritalStatus" value="%{memberInfoMap.maritalStatus}"></s:radio></span></td>
			</tr>
			<tr>
      	      <th><s:text name="binolmbmbm06_memo1"></s:text></th>
			  <td><span style="width:100%"><s:textarea name="memo1" value="%{memberInfoMap.memo1}" cssStyle="width:90%"></s:textarea></span></td>
			  <th><s:text name="binolmbmbm06_memo2"></s:text></th>
			  <td><span style="width:100%"><s:textarea name="memo2" value="%{memberInfoMap.memo2}" cssStyle="width:90%"></s:textarea></span></td>
      	    </tr>
          </table>
	    </div>
	  </div>
	  
	  <s:if test="%{memberInfoMap.memberAddressInfo != null}">
	  <s:set value="%{memberInfoMap.memberAddressInfo}" var="memberAddressInfo"></s:set>
	  <s:hidden name="addressInfoId" value="%{#memberAddressInfo.addressInfoId}"></s:hidden>
	  </s:if>
	  <div class="section">
      	<div class="section-header">
      	  <strong>
      	    <span class="ui-icon icon-ttl-section-info"></span>
      	    <s:text name="binolmbmbm06_memAddressTitle"></s:text>
      	  </strong>
        </div>
        <div class="section-content">
	      	<table class="detail" cellpadding="0" cellspacing="0">
	          <tr>
			    <th><s:text name="binolmbmbm06_provinceId"></s:text></th>
			    <td><span>
			    <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	              	<span id="provinceText" class="button-text choice">
	              	<s:if test='%{#memberAddressInfo.provinceId != null && !"".equals(#memberAddressInfo.provinceId)}'><s:property value="%{#memberAddressInfo.provinceName}"/></s:if>
	              	<s:else><s:text name="global.page.select"/></s:else>
	              	</span>
	   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	   		 	</a>
	   		 	<s:hidden name="provinceId" value="%{#memberAddressInfo.provinceId}" id="provinceId"/>
			    </span></td>
			    <th><s:text name="binolmbmbm06_address"></s:text></th>
				<td><span><s:textfield name="address" value="%{#memberAddressInfo.addressLine1}" cssClass="text" maxlength="100"></s:textfield></span></td>
			  </tr>
			  <tr>
			    <th><s:text name="binolmbmbm06_cityId"></s:text></th>
				<td><span>
				<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	              	<span id="cityText" class="button-text choice">
	              	<s:if test='%{#memberAddressInfo.cityId != null && !"".equals(#memberAddressInfo.cityId)}'><s:property value="%{#memberAddressInfo.cityName}"/></s:if>
	              	<s:else><s:text name="global.page.select"/></s:else>
	              	</span>
	   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	   		 	</a>
	   		 	<s:hidden name="cityId" value="%{#memberAddressInfo.cityId}" id="cityId"/>
				</span></td>
				<th><s:text name="binolmbmbm06_postcode"></s:text></th>
			    <td><span><s:textfield name="postcode" value="%{#memberAddressInfo.zipCode}" cssClass="text" maxlength="10"></s:textfield></span></td>
			  </tr>
			  <tr>
			    <th><s:text name="binolmbmbm06_countyId"></s:text></th>
				<td colspan=3><span>
				<a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
	              	<span id="countyText" class="button-text choice">
					<s:if test='%{#memberAddressInfo.countyId != null && !"".equals(#memberAddressInfo.countyId)}'><s:property value="%{#memberAddressInfo.countyName}"/></s:if>
	              	<s:else><s:text name="global.page.select"/></s:else>
					</span>
	   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
	   		 	</a>
	   		 	<s:hidden name="countyId" value="%{#memberAddressInfo.countyId}" id="countyId"/>
				</span></td>
			  </tr>
	        </table>
	    </div>
	  </div>
	  
	  <s:if test="%{extendPropertyList != null && !extendPropertyList.isEmpty()}">
	    <div class="section">
      	  <div class="section-header">
	      	<strong>
	      	  <span class="ui-icon icon-ttl-section-info"></span>
	      	  <s:text name="binolmbmbm06_extendProperty"></s:text>
	      	</strong>
	      </div>
	      <div class="section-content">
	        <table class="detail" cellpadding="0" cellspacing="0">
	          <tbody>
	          	<s:iterator value="extendPropertyList" id="questionMap" status="status">
	          	<tr>
	              <th style="width: 15%"><s:property value="%{#questionMap.questionItem}"/></th>
	              <td style="width: 85%">
	                <span>
	                  <s:hidden name="%{'propertyInfoList['+#status.index+'].paperId'}" value="%{#questionMap.paperId}"></s:hidden>
	                  <s:hidden name="%{'propertyInfoList['+#status.index+'].extendPropertyId'}" value="%{#questionMap.paperQuestionId}"></s:hidden>
	      			  <s:hidden name="%{'propertyInfoList['+#status.index+'].propertyType'}" value="%{#questionMap.questionType}"></s:hidden>
	                  <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_SINCHOICE}'>
			         	<select name="propertyInfoList[${status.index}].propertyValues">
			         		<option value=""><s:text name="global.page.select"></s:text></option>
			         		<s:iterator value="%{#questionMap.answerList}" id="answerMap">
			         			<option value='<s:property value="%{#answerMap.answer}"/>' <s:if test="%{#answerMap.checked == true}">selected</s:if>><s:property value="%{#answerMap.answer}"/></option>
			         		</s:iterator>
			         	</select>
			          </s:if>
			          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_MULCHOICE}'>
		        		<s:iterator value="%{#questionMap.answerList}" id="answerMap" status="answerStatus">
		        			<input type="checkbox" name="propertyInfoList[${status.index}].propertyValues" value="${answerStatus.index+1 }" <s:if test="%{#answerMap.checked == true}">checked</s:if>/><s:property value="%{#answerMap.answer}"/>
		        		</s:iterator>
			          </s:if>
			          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_ESSAY || #questionMap.questionType == #QUESTIONTYPE_APFILL}'>
		        		<s:textfield name="%{'propertyInfoList['+#status.index+'].propertyValues'}" value="%{#questionMap.answer}" cssClass="text"></s:textfield>
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
	
	
	
    <div  id="CheckMessageDiv" class="hide">
		<div class="actionSuccess">
			<ul>
				<li>
					<span><s:text name="binolmbmbm06_mobileCheckSuccess"></s:text></span>
				</li>
			</ul>
		</div>
	</div>
	<div  id="CheckMessageDiv2" class="hide">
	<div class="actionError">
		<ul>
			<li>
				<span><s:text name="binolmbmbm06_mobileCheck"></s:text></span>
			</li>
		</ul>
	</div>
	</div>
	<p></p>
    <div class="center clearfix">
      <s:if test='%{isNeedCheck == "Y"}'>
      <button class="mobileCheck" style="-moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px;  border: 1px solid #7d8791; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; vertical-align:middle;">
   		<span class="ui-icon icon-mover"></span>
   		<span class="button-text"><s:text name="global.page.moblieCheck"/></span>
      </button>
      </s:if>
      <s:else>
      <button class="save">
    	<span class="ui-icon icon-save"></span>
    	<span class="button-text"><s:text name="global.page.save"/></span>
      </button>
      </s:else>
      <button type="button" class="back">
   		<span class="ui-icon icon-back"></span>
   		<span class="button-text"><s:text name="global.page.back"/></span>
   	  </button>
    </div>
    </form>       	
            	
            	
            </div>
        </div>
    
    </div>
</div>
</s:i18n>

<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="binolwpmbm01.getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="binolwpmbm01.getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide">
<s:if test="%{cityList != null && !cityList.isEmpty()}">
<ul class="u2" style="width: 500px;">
	<li onclick="binolwpmbm01.getNextRegin(this, '${select_default }', 2);"><s:text name="global.page.all"></s:text></li>
	<s:iterator value="cityList">
		<li id="<s:property value="regionId" />" onclick="binolwpmbm01.getNextRegin(this, '${select_default }', 2);"><s:property value="regionName"/></li>
	</s:iterator>
</ul>
</s:if>
</div>
<div id="countyTemp" class="ui-option hide">
<s:if test="%{countyList != null && !countyList.isEmpty()}">
<ul class="u2" style="width: 500px;">
	<li onclick="binolwpmbm01.getNextRegin(this, '${select_default }', 3);"><s:text name="global.page.all"></s:text></li>
	<s:iterator value="countyList">
		<li id="<s:property value="regionId" />" onclick="binolwpmbm01.getNextRegin(this, '${select_default }', 3);"><s:property value="regionName"/></li>
	</s:iterator>
</ul>
</s:if>
</div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM08_queryAllSubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
<%-- ================== 短信验证隐藏DIV  START  ======================= --%>
<s:hidden name="isNeedCheck" value="%{isNeedCheck}"/>
<div id="mobileCheckDiv" class="hide"></div>
<%-- ================== 短信验证隐藏DIV  END  ======================= --%>
<span id="editButtonText" class="hide"><s:text name="global.page.changeMemCode"/></span>
<span id="cancleButtonText" class="hide"><s:text name="global.page.cancle"/></span>

<s:a action="BINOLWPMBM01_update" id="updateUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_mobileCheckUpdateInit" id="mobileCheckUpdateInit" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_couponCheck" id="couponCheck" cssStyle="display: none;"></s:a>

<script type="text/javascript">
$(function() {

	//表示需要修改会员生日
	if($("#isAllowUpdate").val()==1){
		$('#birth').cherryDate({yearRange: 'c-100:c'});
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				memCode: {required: true,maxlength: 20},
				memName: {required: true,maxlength: 30},
				mobilePhone: {phoneTelValid: "#telephone",maxlength: 20},
				telephone: {telValid: true,maxlength: 20},
				birth: {required: true,dateValid: true},
				email: {emailValid: true,maxlength: 60},
				gender: {required: true},
				referrer: {maxlength: 20},
				identityCard: {maxlength: 18},
				blogId: {maxlength: 30},
				messageId: {maxlength: 30},
				memo1: {maxlength: 512},
				memo2: {maxlength: 512},
				address: {maxlength: 100},
				postcode: {zipValid:true,maxlength: 10}
			}
		});
	}else{
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				memCode: {required: true,maxlength: 20},
				memName: {required: true,maxlength: 30},
				mobilePhone: {phoneTelValid: "#telephone",maxlength: 20},
				telephone: {telValid: true,maxlength: 20},
				email: {emailValid: true,maxlength: 60},
				gender: {required: true},
				referrer: {maxlength: 20},
				identityCard: {maxlength: 18},
				blogId: {maxlength: 30},
				messageId: {maxlength: 30},
				memo1: {maxlength: 512},
				memo2: {maxlength: 512},
				address: {maxlength: 100},
				postcode: {zipValid:true,maxlength: 10}
			}
		});
	}


	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		binolwpmbm01.showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		binolwpmbm01.showRegin(this,"cityTemp");
	});
	// 区县选择
	$("#county").click(function(){
		// 显示城市列表DIV
		binolwpmbm01.showRegin(this,"countyTemp");
	});
	
	$("#editMemCode").click(function(){
		if($("#memCode").is(":hidden")) {
    		$("#memCode").show();
    		$("#memCode").prev().hide();
    		$(this).attr("style","margin-top: -3px;");
    		$("#editMemCodeButton").prev().removeClass("icon-edit").addClass("icon-delete");
    		$("#editMemCodeButton").html($("#cancleButtonText").html());
    	} else {
    		$("#memCode").hide();
    		$("#memCode").prev().show();
    		$("#memCode").val($("#memCodeOld").val());
    		$("#mobilePhone").val($("#memCodeOld").val());
    		$(this).attr("style","");
    		$("#editMemCodeButton").prev().removeClass("icon-delete").addClass("icon-edit");
    		$("#editMemCodeButton").html($("#editButtonText").html());
    	}
	});
	
	$(".save").click(function(){
		$("#CheckMessageDiv2").addClass("hide");
		$("#CheckMessageDiv").addClass("hide");
		//判断是否需要短信验证以及是否验证通过
		var isNeedCheck = $("#isNeedCheck").val();
		if(isNeedCheck == "Y"){
			var checkFlag = $("#checkFlag").val();
			if(checkFlag){
				if(checkFlag!='0'){
					$("#CheckMessageDiv2").removeClass("hide");
					return false;
				}
			}else{
				$("#CheckMessageDiv2").removeClass("hide");
				return false;
			}
		}
		if(!$('#saveForm').valid()) {
			return false;
		}
		binolwpmbm01_03.save();
		return false;
	});
	
	$(".back").click(function(){
		$("#memOtherDiv").hide();
		$("#memInitDiv").show();
		$("#memOtherDiv").empty();
		return false;
	});
	
	//手机验证
	$(".mobileCheck").click(function(){
		binolwpmbm01_03.mobileCheck();
		return false;
	});
});
</script>