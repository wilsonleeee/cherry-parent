<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/emp/BINOLBSEMP03.js"></script>
<s:set id="BRAND_INFO_ID_VALUE"><%=CherryConstants.BRAND_INFO_ID_VALUE %></s:set>
<script type="text/javascript">
$(document).ready(function(){
	var categoryCode = '${employeeInfo.categoryCode}';
	if(categoryCode=='01'){
		$("#emp_flag").val("1");
		$("#highlight").hide();
	}else{
		$("#emp_flag").val("0");
		$("#highlight").show();
	}
});
</script>
<%-- 员工保存URL --%>
<s:url id="save_url" value="/basis/BINOLBSEMP03_save"></s:url>
<%-- 员工查询URL --%>
<s:url id="search_url" value="/basis/BINOLBSEMP01_search"/>
<%-- 城市查询URL --%>
<s:url id="getCity_url" value="/common/BINOLCM08_querySubRegion" />
<%-- 部门查询URL --%>
<s:url id="getDepart_url" value="/common/BINOLCM00_queryOrg" />
<%-- 岗位查询URL --%>
<s:url id="getPost_url" value="/common/BINOLCM00_queryPosition" />
<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>
<%-- 根据岗位ID取得岗位信息URL --%>
<s:url id="getPositionCategoryInfoUrl" value="/basis/BINOLBSEMP04_getPositionCategoryInfo" />
<s:hidden value="%{#getPositionCategoryInfoUrl}" id="getPositionCategoryInfoUrl"></s:hidden>
<s:hidden id="maintainBa" value="<s:property value='maintainBa'></s:property>"></s:hidden>

<s:i18n name="i18n.bs.BINOLBSEMP03">
<%--请选择 --%>
<s:text id="global.select" name="global.page.select"/>
<%--部门 --%>
<s:text id="employee.depart" name="employee.depart"/>
<%--岗位 --%>
<s:text id="employee.post" name="employee.post"/>

<div class="main container clearfix">
<div class="panel ui-corner-all">
	  <div class="panel-header">
	    <div class="clearfix">
	       <span class="breadcrumb left"> 
	          <span class="ui-icon icon-breadcrumb"></span>
				<s:text name="emp_manage"/> &gt; <s:text name="emp_title"/>
	       </span>
	    </div>
	  </div>
	  <%-- ================== 错误信息提示 START ======================= --%>
      <div id="errorMessage"></div>
      <%-- ================== 错误信息提示   END  ======================= --%>
      <input type="hidden" id="mobileRule" name="mobileRule" value="<s:property value='mobileRule'/>"/>
      <div class="panel-content">
      	<cherry:form id="toDetailForm" action="BINOLBSEMP02_init" method="post" csrftoken="false">
        	<s:hidden name="employeeId" value="%{employeeInfo.employeeId}"></s:hidden>
        	<input type="hidden" id="parentCsrftoken" name="csrftoken"/>
        </cherry:form>
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
        	<input type="password" style="display: none;"/>
        	<%--=================== 基本信息 ===================== --%>
        	<div id="actionResultDisplay"></div>
            <div class="section">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div id="b_info" class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                    <th><s:text name="employee.code"/><span class="highlight">*</span></th><%-- 员工代号 --%>
                    <td>
                    	<span><s:property value="employeeInfo.employeeCode"></s:property></span>
                    	<s:hidden name="employeeCode" value="%{employeeInfo.employeeCode}"></s:hidden>
                    	<s:hidden name="oldPositionCategoryId" value="%{employeeInfo.positionCategoryId}"></s:hidden>
                    	<s:hidden name="employeeId" value="%{employeeInfo.employeeId}" />
                    	<s:hidden name="userId" value="%{employeeInfo.userId}" />
                    	<s:hidden name="brandInfoId" value="%{employeeInfo.brandInfoId}" />
                    	<s:hidden name="brandCode" value="%{employeeInfo.brandCode}"></s:hidden>
                    	<s:hidden name="empPath" value="%{employeeInfo.empPath}" />
                    	<s:if test="%{employeeInfo.higher != null}">
                    	<s:hidden name="oldHigher" value="%{employeeInfo.higher}" />
                    	</s:if>
                    	<s:hidden name="modifyTime" value="%{employeeInfo.modifyTime}" />
                    	<s:hidden name="modifyCount" value="%{employeeInfo.modifyCount}" />
                    	<s:iterator value="employeeDepartList" id="employeeDepartMap">
					      <s:if test="%{#employeeDepartMap.manageType == 1 && #employeeDepartMap.departType == 4}">
					      	<s:hidden name="oldfollowDepart" value="%{#employeeDepartMap.organizationId}"></s:hidden>
					      </s:if>
					      <s:if test="%{#employeeDepartMap.manageType == 0 || #employeeDepartMap.manageType == 1}">
					      	<s:if test="%{#employeeDepartMap.departType == 4}">
					      	  <s:hidden name="oldfollowLikeDepart" value="%{#employeeDepartMap.organizationId}"></s:hidden>
					      	</s:if>
					      </s:if>
					    </s:iterator>
                    </td>
                    <th><s:text name="employee.name"/><span class="highlight">*</span></th><%-- 姓名 --%>
                    <td><span><s:textfield name="employeeName" cssClass="text" value="%{employeeInfo.employeeName}" maxlength="30"/></span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="employee.brand"/></th><%-- 品牌 --%>
                    <td><span>
                    	<s:if test="%{employeeInfo.brandInfoId == #BRAND_INFO_ID_VALUE}"><s:text name="PPL00006"></s:text></s:if>
                    	<s:else><s:property value='employeeInfo.brandName'/></s:else>
                    </span></td>
              		<th><s:text name="employee.foreignName"/></th><%-- 外文名 --%>
                    <td><span><s:textfield name="employeeNameForeign" cssClass="text" value="%{employeeInfo.employeeNameForeign}" maxlength="30"/></span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="employee.post"/><span class="highlight">*</span></th><%-- 岗位 --%>
                    <td>
                    	<span>
                    		<s:select name="positionCategoryId" list="positionCategoryList"
              		 				listKey="positionCategoryId" listValue="categoryName" headerKey="" headerValue="%{global.select}" value="%{employeeInfo.positionCategoryId}" cssStyle="width:100px;" onchange="bsemp03_selectPos(this);"/>
                    	</span>     	
                    	<s:if test='%{employeeInfo.organizationId == null}'>
	                    	<span id="creatOrgFlagSpan" class="hide">
	                    		<s:text name="employee.creatOrgFlag"></s:text>
	                    		(
	                    		<input type="checkbox" name="creatOrgFlag" value="0" id="creatOrgFlag0" onclick="bsemp03_selectCreatOrgFlag(this)"/>
	                    		<label for="creatOrgFlag0"><s:text name="employee.testType0" /></label>
	                    		<input type="checkbox" name="creatOrgFlag" value="1" id="creatOrgFlag1" onclick="bsemp03_selectCreatOrgFlag(this)"/>
	                    		<label for="creatOrgFlag1"><s:text name="employee.testType1" /></label>
	                    		)
	                   		</span>
                    	</s:if>
                    </td>
                    <th><s:text name="employee.phone"/></th><%-- 联系电话 --%>
                    <td><span><s:textfield name="phone" cssClass="text" value="%{employeeInfo.phone}"/></span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.depart"/><span id="highlight" class="highlight">*</span></th><%-- 部门 --%>
                    <td>
				      	<s:hidden name="departId" id="departId" value="%{employeeInfo.organizationId}"></s:hidden>
				      	<span id="showRelDepartName"><s:property value="employeeInfo.departName"></s:property>
				      	</span>
				      	<a id="selectDepartButton" class="add right" style="margin-left:50px" onclick="bsemp03_popDepart(this);return false;">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
				    </td>
                    <th><s:text name="employee.mobilePhone"/></th><%-- 手机 --%>
                    <td><span><s:textfield name="mobilePhone" cssClass="text" value="%{employeeInfo.mobilePhone}"/></span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.email"/></th><%-- 电子邮箱 --%>
                    <td><span><s:textfield name="email" cssClass="text" value="%{employeeInfo.email}" maxlength="60"/></span></td>
                    <th><s:text name="employee.identityCard"/></th><%-- 身份证号 --%>
                    <td><span><s:textfield name="identityCard" cssClass="text" value="%{employeeInfo.identityCard}"/></span></td>
                  </tr>
                </table>
              </div>
            </div>
            
            <s:if test="%{employeeInfo.userId == null}">
            <div class="section" id="b_longin">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="employee.addLonginName"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                    <th><s:text name="employee.longinName"/></th><%-- 登录帐号 --%>
                    <td><span><s:textfield name="longinName" cssClass="text" maxlength="30"/></span></td>
                    <th><s:text name="employee.password"/></th><%-- 登录密码 --%>
                    <td><span>
                    <s:password name="passwordShow" cssClass="text" maxlength="30"></s:password>
                    <input type="hidden" name="password" id="password"/>
                    </span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.currentUserPsd"/></th>
                    <td class="bg_yew" colspan="3">
                    <span><s:password name="currentUserPsd" cssClass="text" maxlength="30"></s:password></span>
                    <span class="highlight" style="line-height:25px;"><s:text name="employee.currentUserPsdNote"/></span>
                    </td>
                  </tr>
                  <s:if test='%{createBIUser != null && "true".equals(createBIUser)}'>
                  <tr>
                    <th><s:text name="employee.biFlag"/></th>
                    <td colspan="3"><span>
                    <input id="biFlag1" type="radio" value="1" name="biFlag"><label for="biFlag1"><s:text name="employee.biFlag1"/></label>
					<input id="biFlag2" type="radio" value="2" name="biFlag" checked="checked"><label for="biFlag2"><s:text name="employee.biFlag2"/></label>
                    </span></td>
                  </tr>  
                  </s:if>
                </table>
              </div>
            </div>
            </s:if>
            <s:else>
            <div class="section" id="b_longin">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="employee.updLonginName"/>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                    <th><s:text name="employee.longinName"/></th><%-- 登录帐号 --%>
                    <td><span><s:property value="employeeInfo.longinName"/><s:hidden name="longinName" value="%{employeeInfo.longinName}"></s:hidden> </span></td>
                    <th><s:text name="employee.password"/></th><%-- 登录密码 --%>
                    <td><span>
                    <s:password name="passwordShow" cssClass="text" maxlength="30"></s:password>
                    <input type="hidden" name="password" id="password"/>
                    </span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.currentUserPsd"/></th>
                    <td class="bg_yew" colspan="3">
                    <span><s:password name="currentUserPsd" cssClass="text" maxlength="30"></s:password></span>
                    <span class="highlight" style="line-height:25px;"><s:text name="employee.currentUserPsdNoteU"/></span>
                    </td> 
                  </tr>
                  <s:if test='%{createBIUser != null && "true".equals(createBIUser)}'>
                  <tr>
                    <th><s:text name="employee.biFlag"/></th>
                    <td colspan="3"><span>
                    <input id="biFlag1" type="radio" value="1" name="biFlag" <s:if test="%{employeeInfo.biFlag == 1}">checked</s:if>><label for="biFlag1"><s:text name="employee.biFlag1"/></label>
					<input id="biFlag2" type="radio" value="2" name="biFlag" <s:if test="%{employeeInfo.biFlag == null || employeeInfo.biFlag == 2}">checked</s:if>><label for="biFlag2"><s:text name="employee.biFlag2"/></label>
                    </span></td>
                  </tr>  
                  </s:if>
                  <tr>
                    <th><s:text name="employee.isEnable"/></th>
                    <td colspan="3"><span>
                    <input id="validFlag1" type="radio" value="1" name="validFlag" <s:if test="%{employeeInfo.validFlag == 1}">checked</s:if>><label for="validFlag1"><s:text name="employee.enable"/></label>
					<input id="validFlag0" type="radio" value="0" name="validFlag" <s:if test="%{employeeInfo.validFlag == 0}">checked</s:if>><label for="validFlag0"><s:text name="employee.disnable"/></label>
                    </span></td>
                  </tr>
                </table>
              </div>
            </div>
            </s:else>
            
            <%--=================== 选择上司 ===================== --%>
            <div class="section" id="b_higher">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom02_popDataTableOfEmployee(this,$('#brandInfoId').serialize()+'&'+$('#empPath').serialize()+'&'+$('#positionCategoryId').serialize());return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="employee.higher"/></span>
	       		</a>
              </div>
              <div class="section-content">
				<table class="detail" cellpadding="0" cellspacing="0">
				  <tr>
                   	<th style="width: 15%"><s:text name="employee.higher"/></th><%-- 上司 --%>
                    <td style="width: 85%" id="higherResult">
                    <s:if test="%{employeeInfo.higher != null}">
                    	<s:hidden name="higher" value="%{employeeInfo.higher}"></s:hidden>
                    	<s:hidden name="higherEmployeeId" value="%{employeeInfo.higherEmployeeId}"></s:hidden>
                    	<span class="left" style="word-wrap:break-word;overflow:hidden; margin-right:10px;"><s:property value="employeeInfo.higherName"/></span>
                    	<span class="close left" onclick="bscom02_closeHigher(this);"><span class="ui-icon ui-icon-close"></span></span>
                    </s:if>	
                    </td>
                  </tr>
				</table>
			  </div>
            </div>
            
            <%--=================== 关注用户 ===================== --%>
            <div class="section" id="b_likeEmployee">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom02_popDataTableOfLikeEmployee(this,$('#brandInfoId').serialize()+'&'+$('#employeeId').serialize()); return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="employee.likeEmployee"/></span>
	       		</a>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="employee.name"/></th>
				      <th width="30%"><s:text name="employee.depart"/></th>
				      <th width="30%"><s:text name="employee.post"/></th>
				      <th width="10%"><s:text name="employee.operation"/></th>
				    </tr>
				  </thead>
				  <tbody>
				    <s:iterator value="likeEmployeeList" id="likeEmployeeMap">
				    <tr>
				    <td><s:property value="#likeEmployeeMap.employeeName"/></td>
				    <td><s:property value="#likeEmployeeMap.departName"/></td>
				    <td><s:property value="#likeEmployeeMap.categoryName"/></td>
				    <td class="center">
				    	<s:hidden name="likeEmployeeId" value="%{#likeEmployeeMap.likeEmployeeId}"></s:hidden>
						<a class="delete" onclick="bscom02_removeEmployee(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="employee.delete"/></span></a></td>
				    </tr>
				    </s:iterator>
				  </tbody>
				</table>
			  </div>
            </div>
            
            <%--=================== 管辖部门 ===================== --%>
            <div class="section" id="b_followDepart">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom02_popDataTableOfFollowDepart(this,$('#brandInfoId').serialize()); return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="employee.followDepart"/></span>
	       		</a>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="employee.departCode"/></th>
				      <th width="30%"><s:text name="employee.departName"/></th>
				      <th width="20%"><s:text name="employee.departType"/></th>
				      <th width="10%"><s:text name="employee.operation"/></th>
				      <th width="10%"><s:text name="global.page.validFlag"/></th>
				    </tr>
				  </thead>
				  <tbody>
				  <s:iterator value="employeeDepartList" id="employeeDepartMap">
				    <s:if test="%{#employeeDepartMap.manageType == 1}">
				    <tr>
				    <td><s:property value="#employeeDepartMap.departCode"/></td>
				    <td><s:property value="#employeeDepartMap.departName"/></td>
				    <td>
				    	<s:property value='#application.CodeTable.getVal("1000", #employeeDepartMap.departType)' />
				    	<s:hidden name="departType" value="%{#employeeDepartMap.departType}"></s:hidden>
				    </td>
				    <td class="center">
				    	<s:hidden name="organizationId" value="%{#employeeDepartMap.organizationId}"></s:hidden>
						<a class="delete" onclick="bscom02_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="employee.delete"/></span></a>
					</td>
				    <td class="center">
						<s:if test='"1".equals(validFlag)'>
							<span class='ui-icon icon-valid' title="启用"></span>
						</s:if><%-- 有效区分 --%>
						<s:else>
							<span class='ui-icon icon-invalid' title="停用"></span>
						</s:else>
				    </td>
				    </tr>
				    </s:if>
				    </s:iterator>
				  </tbody>
				</table>
			  </div>
            </div>
            
            <%--=================== 关注部门 ===================== --%>
            <div class="section" id="b_likeDepart">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom02_popDataTableOfLikeDepart(this,$('#brandInfoId').serialize()); return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="employee.likeDepart"/></span>
	       		</a>
              </div>
              <div class="section-content">
				<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
				  <thead>
				    <tr>
				      <th width="30%"><s:text name="employee.departCode"/></th>
				      <th width="30%"><s:text name="employee.departName"/></th>
				      <th width="20%"><s:text name="employee.departType"/></th>
				      <th width="10%"><s:text name="employee.operation"/></th>
				      <th width="10%"><s:text name="global.page.validFlag"/></th>
				    </tr>
				  </thead>
				  <tbody>
				  <s:iterator value="employeeDepartList" id="employeeDepartMap">
				    <s:if test="%{#employeeDepartMap.manageType == 0}">
				    <tr>
				    <td><s:property value="#employeeDepartMap.departCode"/></td>
				    <td><s:property value="#employeeDepartMap.departName"/></td>
				    <td>
				    	<s:property value='#application.CodeTable.getVal("1000", #employeeDepartMap.departType)' />
				    	<s:hidden name="departType" value="%{#employeeDepartMap.departType}"></s:hidden>
				    </td>
				    <td class="center">
				    	<s:hidden name="organizationId" value="%{#employeeDepartMap.organizationId}"></s:hidden>
						<a class="delete" onclick="bscom02_removeDepart(this);return false;"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="employee.delete"/></span></a>
					</td>
				    <td class="center">
						<s:if test='"1".equals(validFlag)'>
							<span class='ui-icon icon-valid' title="启用"></span>
						</s:if><%-- 有效区分 --%>
						<s:else>
							<span class='ui-icon icon-invalid' title="停用"></span>
						</s:else>
				    </td>
				    </tr>
				    </s:if>
				    </s:iterator>
				  </tbody>
				</table>
			  </div>
            </div>
            
            <div class="section">
              <div class="section-header clearfix">
            	<span id="hideMoreInfo" class="hide"><s:text name="employee.hideMoreInfo" /></span>
            	<span id="showMoreInfo" class="hide"><s:text name="employee.showMoreInfo" /></span>
            	<a class="add left" href="#" onclick="bsemp03_more(this); return false;">
      				<span class="ui-icon ui-icon-triangle-1-s"></span><span class="button-text"><s:text name="employee.showMoreInfo"/></span>
      			</a>
              </div>
              <div class="section-content hide" id="empMore">
              
                <%--=================== 用户其他信息 ===================== --%>
	            <div class="section" id="b_moreInfo">
	              <div class="section-content">
					<table class="detail" cellpadding="0" cellspacing="0">
	                  <tr>
	                    <th><s:text name="employee.birthDay"/></th><%-- 生日 --%>
                    	<td><span><s:textfield name="birthDay" cssClass="text" value="%{employeeInfo.birthDay}"/></span></td>
                    	<th><s:text name="employee.gender"/></th><%-- 性别 --%>
	                    <td>
	                    	<s:radio name="gender" list="#application.CodeTable.getCodes('1006')" 
	                    		listKey="CodeKey" listValue="Value" value="employeeInfo.gender"></s:radio>
	              		</td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.academic"/></th><%-- 学历 --%>
	                    <td>
	                    	<s:select name="academic" list="#application.CodeTable.getCodes('1042')"
	              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}" value="employeeInfo.academic"/>
	                    </td>
	                    <th><s:text name="employee.maritalStatus"/></th><%-- 婚姻状况 --%>
	                    <td>
	                    	<s:select name="maritalStatus" list="#application.CodeTable.getCodes('1043')"
	              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}" value="employeeInfo.maritalStatus"/>
	              		</td>
	                  </tr>
	                </table>
				  </div>
	            </div>
	            
	            <div class="section" id="b_address">
              <div class="section-header clearfix">
              	<strong class="left">
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="employee.addressInfo"/><%-- 地址信息 --%>
              	</strong>
              	<a class="add right" href="#" onclick="BSEMP03_addDiv('#b_address','#h_address'); return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.add"/><s:text name="employee.addressInfo"/></span>
	       		</a>
              </div>
              <div class="section-content">
              	<%-- ===== 地址信息LIST为空时 ====== --%>
                <s:if test="null == addressList || addressList.size()==0">
                	<table class="detail" cellpadding="0" cellspacing="0">
	                  <caption> 	
	                  	<span class="left">
		                  	<input type="radio" name="defaultFlag" checked="checked"/>
		                  	<span><s:text name="employee.def_display"/></span><%-- 默认显示 --%>
	                  	</span>
	                  	<span class="right hide button-del">
		                  	<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'addressInfoId'); return false;">
				       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
				       		</a>
	                  	</span>
	                  </caption>
	                  <tr>
	                    <th><s:text name="employee.province"/></th><%-- 省份 --%>
	                    <td>
	                    	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
	                    		list="provinceList" listKey="provinceId" listValue="provinceName" 
	                    		onchange="BSEMP03_getCity(this,'%{getCity_url}','%{global.select}');"/>
	              		</td>
	                    <th><s:text name="employee.city"/></th><%-- 城市 --%>
	                    <td><select name="cityId"><option value=""><s:text name="global.page.select"/></option></select></td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
	                    <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
	                    <th><s:text name="employee.address"/></th><%-- 地址 --%>
	                    <td><span><s:textfield name="address" cssClass="text" cssStyle="width:275px" maxlength="100"/></span></td>
	                  </tr>
	                </table>
                </s:if>
               <s:iterator value="addressList">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <caption>
                  	<span class="left">
	                  	<s:if test="defaultFlag == 1">
	                  		<input type="radio" name="defaultFlag" checked="checked"/>
	                  	</s:if>
	                  	<s:else>
                  			<input type="radio" name="defaultFlag"/>
                  		</s:else>
	                  	<span><s:text name="employee.def_display"/></span><%-- 默认显示 --%>
	                  	<s:hidden name="option" value="1"/>
	                  	<s:hidden name="addressInfoId" value="%{addressInfoId}"/>
                  	</span>
                  	<s:if test="addressList.size() == 1">
                  		<span class="right hide button-del">
                  			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'addressInfoId'); return false;">
				       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
				       		</a>
                  		</span>
                  	</s:if>
                  	<s:else>
                  		<span class="right button-del">
                  			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'addressInfoId'); return false;">
				       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
				       		</a>
                  		</span>
                  	</s:else>
                  </caption>
                  <tr>
                    <th><s:text name="employee.province"/></th><%-- 省份 --%>
                    <td>
                    	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
                    		list="provinceList" listKey="provinceId" listValue="provinceName" 
                    		onchange="BSEMP03_getCity(this,'%{getCity_url}','%{global.select}');"/>
              		</td>
                    <th><s:text name="employee.city"/></th><%-- 城市 --%>
                    <td>
                    	<select name="cityId">
                    		<option value="<s:property value='cityId'/>"><s:text name="global.page.select"/></option>
                    	</select>
                   	</td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
                    <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
                    <th><s:text name="employee.address"/></th><%-- 地址 --%>
                    <td><span><s:textfield name="address" cssClass="text" cssStyle="width:275px" maxlength="100"/></span></td>
                  </tr>
                </table>
               </s:iterator>
              </div>
            </div>
           
            <%--=================== 入离职信息 ===================== --%>
            <div class="section">
              <div class="section-header clearfix">
              	<strong class="left">
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="employee.quit_info"/><%-- 入离职信息  --%>
              	</strong>
              	<a class="add right" href="#" onclick="BSEMP03_addDiv('#quitInfo', '#h_quitInfo',true);return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.add"/><s:text name="employee.quit_info"/></span>
	       		</a>
              </div>
              <div id="quitInfo" class="section-content">
              	<s:if test="null==quitList || quitList.size()==0">
					<table class="detail" cellpadding="0" cellspacing="0">
						<caption>
			     			<span class="left"><s:text name="employee.quit_info"/></span>
			     			<span class="right hide button-del">
	                			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'employeeQuitId');return false;">
					       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
					       		</a>
                			</span>
				     	</caption>
                   		<tr>
		                    <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
		                    <td><span><s:textfield id="" name="commtDate" cssClass="text"/></span></td>
		                    <th><s:text name="employee.dep_date"/></th><%-- 离职日期  --%>
		                    <td><span><s:textfield id="" name="depDate" cssClass="text"/></span></td>
		                </tr>
		                <tr>
		                    <th><s:text name="employee.dep_reason"/></th><%-- 离职理由  --%>
		                    <td colspan="3"><span><s:textfield name="depReason" cssClass="text" cssStyle="width:750px" maxlength="50"/></span></td>
		                </tr>
		            </table>
				</s:if>
                <s:iterator value="quitList">
                	<table class="detail" cellpadding="0" cellspacing="0">
                		<caption>
                			<span class="left"><s:text name="employee.quit_info"/></span>
                			<s:if test="quitList.size()==1">
	                			<span class="right hide button-del">
		                			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'employeeQuitId');return false;">
						       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
						       		</a>
	                			</span>
	                  		</s:if>
	                  		<s:else>
	                  			<span class="right button-del">
		                			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'employeeQuitId');return false;">
						       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
						       		</a>
	                			</span>
	                  		</s:else>
		                  	<input type="hidden" name="option" value="1"/>
		                  	<s:hidden name="employeeQuitId" value='%{employeeQuitId}'/>
                		</caption>
	                  	<tr>
		                    <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
		                    <td><span><s:textfield id="" name="commtDate" cssClass="text" value="%{comDate}"/></span></td>
		                    <th><s:text name="employee.dep_date"/></th><%-- 离职日期  --%>
		                    <td><span><s:textfield id="" name="depDate" cssClass="text" value="%{depDate}"/></span></td>
		                </tr>
		                <tr>
		                    <th><s:text name="employee.dep_reason"/></th><%-- 离职理由  --%>
		                    <td colspan="3"><span><s:textfield name="depReason" cssStyle="width:750px" cssClass="text" value="%{depReason}" maxlength="50"/></span></td>
		                </tr>
		            </table>
               	</s:iterator>
              </div>
            </div>
              
              </div>
            </div>  
            
            
          	<%--===================  操作按钮 ===================== --%>
            <div id="saveButton" class="center clearfix">
          		<cherry:show domId="BINOLBSEMP0103">
          			<button class="save" onclick="BSEMP03_saveEmp('${save_url}');return false;">
	              		<span class="ui-icon icon-save"></span>
	              		<span class="button-text"><s:text name="global.page.save"/></span>
	              	</button>
          		</cherry:show>
          		<s:if test='%{fromPage != null && fromPage != ""}'>
          		<button id="back" class="back" type="button" onclick="BSEMP03_doBack()">
	            	<span class="ui-icon icon-back"></span>
	            	<span class="button-text"><s:text name="global.page.back"/></span>
	            </button>
	            </s:if>
	            <button id="close" class="close" type="button"  onclick="doClose();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
        </cherry:form>
      </div>
    </div>
  </div>
  <%--=================== 隐藏内容 ===================== --%>
  
  <div class="hide">
  	<%--======== 隐藏URL========= --%>
  	<div id="h_url">
  		<%-- 城市查询URL --%>
		<input type="hidden" id="getCity_url" value="<s:property value='#getCity_url'/>" />
		<%-- 部门查询URL --%>
		<input type="hidden" id="getDepart_url" value="<s:property value='#getDepart_url'/>" />
		<%-- 岗位查询URL --%>
		<input type="hidden" id="getPost_url" value="<s:property value='#getPost_url'/>" />
		<%--请选择 --%>
		<input type="hidden" id="global_select" value="<s:text name="global.page.select" />"/>
		<%--部门 --%>
		<input type="hidden" id="employee_depart" value="<s:text name="employee.depart" />"/>
		<%--岗位 --%>
		<input type="hidden" id="employee_post" value="<s:text name="employee.post" />"/>
  	</div>
  	<%--======== 隐藏备用地址 ========= --%>
	<div id="h_address">
       <table class="detail" cellpadding="0" cellspacing="0">
         <caption>
       		<span class="left"> 
	           	<input type="radio" name="defaultFlag"/>
	           	<span><s:text name="employee.def_display"/></span><%-- 默认显示 --%>
       		</span>
       		<span class="right button-del">
    			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'addressInfoId');return false;">
 					<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
 				</a>
   			</span>
         </caption>
         <tr>
           <th><s:text name="employee.province"/></th><%-- 省份 --%>
              <td>
              	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
               		list="provinceList" listKey="provinceId" listValue="provinceName" 
               		onchange="BSEMP03_getCity(this,'%{getCity_url}','%{global.select}');"/>
        	  </td>
              <th><s:text name="employee.city"/></th><%-- 城市 --%>
              <td>
              	<select name="cityId"><option value=""><s:text name="global.page.select"/></option></select>
              </td>
         </tr>
         <tr>
           <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
           <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
           <th><s:text name="employee.address"/></th><%-- 地址 --%>
           <td>
           	<span>
            	<s:textfield name="address" cssClass="text" cssStyle="width:275px" maxlength="100"/>
            	<input type="hidden" name="defaultFlag"/>
           	</span>
          </td>
         </tr>
       </table>
    </div>
    <div id="h_quitInfo">
    	<table class="detail" cellpadding="0" cellspacing="0">
     		<caption>
     			<span class="left"><s:text name="employee.quit_info"/></span>
     			<span class="right button-del">
	     			<a class="delete" href="#" onclick="BSEMP03_delDiv(this,'employeeQuitId');return false;">
		 				<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
		 			</a>
     			</span>
	     	</caption>
	        <tr>
	           <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
	           <td><span><s:textfield id="" name="commtDate" cssClass="text"/></span></td>
	           <th><s:text name="employee.dep_date"/></th><%-- 离职日期  --%>
	           <td><span><s:textfield id="" name="depDate" cssClass="text"/></span></td>
	       </tr>
	       <tr>
	           <th><s:text name="employee.dep_reason"/></th><%-- 离职理由  --%>
	           <td colspan="3"><span><s:textfield name="depReason" cssStyle="width:750px" cssClass="text" maxlength="50"/></span></td>
	       </tr>
	   </table>
    </div>
</div>
</s:i18n>
<s:hidden name="holidays" value="%{holidays}"/>
<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM01.jsp" flush="true" />
<div class="hide">
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
<s:hidden name="emp_flag" id="emp_flag"></s:hidden>
<s:select list="positionCategoryList" listKey="positionCategoryId" listValue="categoryCode" id="positionCategoryTemp"></s:select>
</div>