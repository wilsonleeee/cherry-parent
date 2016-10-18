<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>
<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>
<s:set id="QUESTIONTYPE_ESSAY"><%=MonitorConstants.QUESTIONTYPE_ESSAY %></s:set>
<s:set id="QUESTIONTYPE_APFILL"><%=MonitorConstants.QUESTIONTYPE_APFILL %></s:set>
<script type="text/javascript" src="/Cherry/js/wp/mbm/BINOLWPMBM01_02.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM11">
<s:text name="global.page.select" id="select_default"/>
<div class="crm_top clearfix">
	<div><s:text name="binolmbmbm11_addMem"/></div>
	
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
    <div class="section">
      <div class="section-header">
      	<strong class="left active">
      	  <s:text name="global.page.title"/>
      	</strong>
      </div>
      <div class="section-content">
      	<table class="detail" cellpadding="0" cellspacing="0">
      	  <tr>
      	    <th><s:text name="binolmbmbm11_memCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    <td><span>
		    <s:if test='%{cardMobileSyn == "Y"}'><s:textfield name="memCode" cssClass="text" maxlength="20" onkeyup="binolwpmbm01.synMoblie(this);return false;" onchange="binolwpmbm01.synMoblie(this);return false;"></s:textfield></s:if>
		    <s:else><s:textfield name="memCode" cssClass="text" maxlength="20"></s:textfield></s:else>
		    </span></td>
		    <th><s:text name="binolmbmbm11_employeeCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    <td><span><s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_name"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			<td><span><s:textfield name="memName" cssClass="text" maxlength="30"></s:textfield></span></td>
			<th><s:text name="binolmbmbm11_mobilePhone"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			<td><span>
			<s:if test='%{cardMobileSyn == "Y"}'>
			<s:textfield name="mobilePhone" cssClass="text" maxlength="20" readonly="true"></s:textfield>
			</s:if>
			<s:else><s:textfield name="mobilePhone" cssClass="text" maxlength="20"></s:textfield></s:else>
			</span></td>
      	  </tr>
      	  <tr>
      	  	<s:if test='%{birthFlag == "Y"}'>
	      	    <th><s:text name="binolmbmbm11_memberBirthDay"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
      	  	</s:if>
      	  	<s:else>
      	  		 <th><s:text name="binolmbmbm11_memberBirthDay"></s:text></th>
      	  	</s:else>
			<td><span >
			<s:if test="%{birthFormat == 1}"><s:textfield name="birth" cssClass="date"></s:textfield></s:if>
			<s:if test="%{birthFormat == 2}">
							<span >
								<select id="birthDayMonthQ1" name="birthDayMonthQ" style="width:50px;height:20px;">
									<option value="">月</option>
								</select>
								<select id="birthDayDateQ1" name="birthDayDateQ" style="width:50px;height:20px;">
									<option value="">日</option>
								</select>
							</span>
			</s:if>
			
			</span></td>
			<th><s:text name="binolmbmbm11_telephone"></s:text></th>
		    <td><span><s:textfield name="telephone" cssClass="text" maxlength="20"></s:textfield></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_gender"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
			<td><span><s:radio list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" name="gender" ></s:radio></span></td>
			<th><s:text name="binolmbmbm11_email"></s:text></th>
		    <td><span><s:textfield name="email" cssClass="text" maxlength="60"></s:textfield></span></td>
      	  </tr>
      	</table>
      	<cherry:show domId="BINOLWMMNG01_01">
      	<table class="detail" cellpadding="0" cellspacing="0">
          <tr>
			<th><s:text name="binolmbmbm11_identityCard"></s:text></th>
		    <td><span><s:textfield name="identityCard" cssClass="text" maxlength="18"></s:textfield></span></td>
			<th><s:text name="binolmbmbm11_referrer"></s:text></th>
			<td><span><s:textfield name="referrer" cssClass="text" maxlength="20"></s:textfield></span></td>
      	  </tr>
      	  <tr>
      	    <th><s:text name="binolmbmbm11_profession"></s:text></th>
			<td><span><s:select list='#application.CodeTable.getCodes("1236")' listKey="CodeKey" listValue="Value" name="profession" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
      	    <th><s:text name="binolmbmbm11_blogId"></s:text></th>
		    <td><span><s:textfield name="blogId" cssClass="text" maxlength="30"></s:textfield></span></td>
      	  </tr>
		  <tr>
		    <th><s:text name="binolmbmbm11_maritalStatus"></s:text></th>
			<td><span><s:radio list='#application.CodeTable.getCodes("1043")' listKey="CodeKey" listValue="Value" name="maritalStatus" ></s:radio></span></td>	
			<th><s:text name="binolmbmbm11_messageId"></s:text></th>
			<td><span><s:textfield name="messageId" cssClass="text" maxlength="30"></s:textfield></span></td>	   
		  </tr>
		  <tr>
     	    <th><s:text name="binolmbmbm11_memo1"></s:text></th>
		    <td><span style="width:100%"><s:textarea name="memo1" cssStyle="width:90%"></s:textarea></span></td>
		    <th><s:text name="binolmbmbm11_memo2"></s:text></th>
		    <td><span style="width:100%"><s:textarea name="memo2" cssStyle="width:90%"></s:textarea></span></td>
     	  </tr>
        </table>
        </cherry:show>
	  </div>
	</div>
	
	<div class="section">
      <div class="section-header">
      	<strong class="left active">
      	  <s:text name="binolmbmbm11_memAddressTitle"></s:text>
      	</strong>
      </div>
      <div class="section-content">
      	<table class="detail" cellpadding="0" cellspacing="0">
          <tr>
		    <th><s:text name="binolmbmbm11_provinceId"></s:text></th>
		    <td><span>
		    <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
              	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
   		 	</a>
   		 	<s:hidden name="provinceId" id="provinceId"/>
		    </span></td>
		    <th><s:text name="binolmbmbm11_address"></s:text></th>
			<td><span><s:textfield name="address" cssClass="text" maxlength="100"></s:textfield></span></td>
		  </tr>
		  <tr>
		    <th><s:text name="binolmbmbm11_cityId"></s:text></th>
			<td><span>
			<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
              	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
   		 	</a>
   		 	<s:hidden name="cityId" id="cityId"/>
			</span></td>
			<th><s:text name="binolmbmbm11_postcode"></s:text></th>
		    <td><span><s:textfield name="postcode" cssClass="text" maxlength="10"></s:textfield></span></td>
		  </tr>
		  <tr>
		    <th><s:text name="binolmbmbm11_countyId"></s:text></th>
			<td colspan=3><span>
			<a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
              	<span id="countyText" class="button-text choice"><s:text name="global.page.select"/></span>
   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
   		 	</a>
   		 	<s:hidden name="countyId" id="countyId"/>
			</span></td>
		  </tr>
        </table>
	  </div>
	</div>
	
	<s:if test="%{extendPropertyList != null && !extendPropertyList.isEmpty()}">
    <div class="section">
      <div class="section-header">
      	<strong class="left active">
      	  <s:text name="binolmbmbm11_extendProperty"></s:text>
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
		         			<option value='<s:property value="%{#answerMap.answer}"/>'><s:property value="%{#answerMap.answer}"/></option>
		         		</s:iterator>
		         	</select>
		          </s:if>
		          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_MULCHOICE}'>
	        		<s:iterator value="%{#questionMap.answerList}" id="answerMap" status="answerStatus">
	        			<input type="checkbox" name="propertyInfoList[${status.index}].propertyValues" value="${answerStatus.index+1 }" /><s:property value="%{#answerMap.answer}"/>
	        		</s:iterator>
		          </s:if>
		          <s:if test='%{#questionMap.questionType == #QUESTIONTYPE_ESSAY || #questionMap.questionType == #QUESTIONTYPE_APFILL}'>
	        		<s:textfield name="%{'propertyInfoList['+#status.index+'].propertyValues'}" cssClass="text"></s:textfield>
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
				<span><s:text name="binolmbmbm11_mobileCheckSuccess"></s:text></span>
			</li>
		</ul>
	</div>
	</div>
    <div class="center clearfix">
	  <button class="save">
    	<span class="ui-icon icon-save"></span>
    	<span class="button-text"><s:text name="global.page.save"/></span>
      </button>
      <s:if test='%{isNeedCheck == "Y"}'>
      <button class="mobileCheck" style="-moz-border-radius: 4px; -webkit-border-radius: 4px; border-radius: 4px;  border: 1px solid #7d8791; background: #ffffff url(images/ui-bg_gloss-wave_60_ffffff_500x100.png) left bottom repeat-x; font-weight: normal; color: #333333;display: inline-block; position: relative; padding: 0; margin-left: .5em; text-decoration: none !important; cursor: pointer; text-align: center; zoom: 1; overflow: visible; vertical-align:middle;">
   		<span class="ui-icon icon-mover"></span>
   		<span class="button-text"><s:text name="global.page.moblieCheck"/></span>
      </button>
      </s:if>
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
<div id="cityTemp" class="ui-option hide"></div>
<div id="countyTemp" class="ui-option hide"></div>
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
<s:a action="BINOLWPMBM01_add" id="addUrl" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_mobileCheckInit" id="mobileCheckInit" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_couponCheck" id="couponCheck" cssStyle="display: none;"></s:a>
<script type="text/javascript">
$(function() {
	$('#birth').cherryDate({yearRange: 'c-100:c'});
	// 生日框初始化处理
	function birthDayInit() {
		for(var i = 1; i <= 12; i++) {
			$("#birthDayMonthQ1").append('<option value="'+i+'">'+i+'</option>');
		}
		$("#birthDayMonthQ1").change(function(){
			var $date = $("#birthDayDateQ1");
			var month = $(this).val();
			var options = '<option value="">'+$date.find('option').first().html()+'</option>';
			if(month == "") {
				$date.html(options);
				return;
			}
			var i = 1;
			var max = 0;
			if(month == '2') {
				max = 29;
			} else if(month == '4' || month == '6' || month == '9' || month == '11') {
				max = 30;
			} else {
				max = 31;
			}
			for(i = 1; i <= max; i++) {
				options += '<option value="'+i+'">'+i+'</option>';
			}
			$date.html(options);
		});
	}
	birthDayInit();
	
	var birthFlag=$("#birthFlag").val();
	if(birthFlag == "Y"){
		// 表单验证初期化
		cherryValidate({
			formId: 'saveForm',
			rules: {
				memCode: {required: true,maxlength: 20},
				employeeId: {required: true},
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
		cherryValidate({
			formId: 'saveForm',
			rules: {
				memCode: {required: true,maxlength: 20},
				employeeId: {required: true},
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
	
	$(".save").click(function(){
		if(!$('#saveForm').valid()) {
			return false;
		}
		binolwpmbm01_02.save();
		return false;
	});
	//手机验证
	$(".mobileCheck").click(function(){
		binolwpmbm01_02.mobileCheck();
		return false;
	});
	
	$(".back").click(function(){
		$("#memOtherDiv").hide();
		$("#memInitDiv").show();
		$("#memOtherDiv").empty();
		return false;
	});
});
</script>