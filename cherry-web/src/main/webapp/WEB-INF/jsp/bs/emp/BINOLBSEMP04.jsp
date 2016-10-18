<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/sjcl.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/emp/BINOLBSEMP04.js"></script>
<script type="text/javascript">
$(function(){
	// 节日
	var holidays = '${holidays }';
	$('#birthDay').cherryDate({holidayObj: holidays, yearRange:'c-60:c'});
	$('#commtDate').cherryDate({holidayObj: holidays, yearRange:'c-1:c'});
});
</script>
<%-- 员工保存URL --%>
<s:url id="save_url" value="/basis/BINOLBSEMP04_save"></s:url>
<%-- 城市查询URL --%>
<s:url id="getCity_url" value="/common/BINOLCM08_querySubRegion" />
<%-- 部门查询URL --%>
<s:url id="getDepart_url" value="/basis/BINOLBSEMP04_filter"></s:url>
<%-- 岗位查询URL --%>
<s:url id="getPost_url" value="/common/BINOLCM00_queryPosition" />
<%-- 实时刷新数据权限URL --%>
<s:url id="refreshPrivilegeUrl" value="/common/BINOLCMPL04_init" />
<s:hidden value="%{#refreshPrivilegeUrl}" id="refreshPrivilegeUrl"></s:hidden>
<%-- 根据岗位ID取得岗位信息URL --%>
<s:url id="getPositionCategoryInfoUrl" value="/basis/BINOLBSEMP04_getPositionCategoryInfo" />
<s:hidden value="%{#getPositionCategoryInfoUrl}" id="getPositionCategoryInfoUrl"></s:hidden>
<s:hidden id="maintainBa" value="<s:property value='maintainBa'></s:property>"></s:hidden>

<s:i18n name="i18n.bs.BINOLBSEMP04">
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
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
        	<input type="password" style="display: none;"/>
        	<%--=================== 基本信息 ===================== --%>
        	<div id="actionResultDisplay"></div>
            <div class="section" id="b_info">
              <div class="section-header">
              	<strong>
              		<span class="ui-icon icon-ttl-section-info-edit"></span>
              		<s:text name="global.page.title"/><%-- 基本信息 --%>
              	</strong>
              </div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
                    <th><s:text name="employee.code"/><span class="highlight">*</span></th><%-- 员工代号 --%>
                    <td><span><s:textfield name="employeeCode" cssClass="text" maxlength="15"/></span></td>
                    <th><s:text name="employee.name"/><span class="highlight">*</span></th><%-- 姓名 --%>
                    <td><span><s:textfield name="employeeName" cssClass="text" maxlength="30"/></span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="employee.brand"/><span class="highlight">*</span></th><%-- 品牌 --%>
                    <td><span>
                    	<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" 
	                    		onchange="BSEMP04_getDepart(this,'%{getDepart_url}','%{global.select}');" cssStyle="width:100px;"/>
              		</span></td>
              		<th><s:text name="employee.foreignName"/></th><%-- 外文名 --%>
                    <td><span><s:textfield name="employeeNameForeign" cssClass="text" maxlength="30"/></span></td>
                  </tr>
                  <tr>
              		<th><s:text name="employee.post"/><span class="highlight">*</span></th><%-- 岗位 --%>
                    <td>
                    	<span>
                    		<s:select name="positionCategoryId" list="positionCategoryList"
              		 				listKey="positionCategoryId" listValue="categoryName" headerKey="" headerValue="%{global.select}" cssStyle="width:100px;" onchange="bsemp04_selectPos(this);"/>
                    	</span>
                    	<span id="creatOrgFlagSpan" class="hide">
                    		<s:text name="employee.creatOrgFlag"></s:text>
                    		(
                    		<input type="checkbox" name="creatOrgFlag" value="0" id="creatOrgFlag0" onclick="bsemp04_selectCreatOrgFlag(this)"/>
                    		<label for="creatOrgFlag0"><s:text name="employee.testType0" /></label>
                    		<input type="checkbox" name="creatOrgFlag" value="1" id="creatOrgFlag1" onclick="bsemp04_selectCreatOrgFlag(this)"/>
                    		<label for="creatOrgFlag1"><s:text name="employee.testType1" /></label>
                    		)
                   		</span>
                    </td>
                    <th><s:text name="employee.phone"/></th><%-- 联系电话 --%>
                    <td><span><s:textfield name="phone" cssClass="text"/></span></td>
                  </tr>
                  <tr>
                  	<th><s:text name="employee.depart"/><span id="highlight" class="highlight">*</span></th><%-- 部门 --%>
                    <td>
				      	<input type="hidden" name="departId" id="departId"></input>
				      	<span id="showRelDepartName">
				      	</span>
				      	<a id="selectDepartButton" class="add right" style="margin-left:50px" onclick="bsemp04_popDepart(this);return false;">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="global.page.Popselect"/></span>
                        </a>
				    </td>
                    <th><s:text name="employee.mobilePhone"/></th><%-- 手机 --%>
                    <td><span><s:textfield name="mobilePhone" cssClass="text"/></span></td>
                  </tr>
                  <tr>
                    <th><s:text name="employee.email"/></th><%-- 电子邮箱 --%>
                    <td><span><s:textfield name="email" cssClass="text" maxlength="60"/></span></td>
                    <th><s:text name="employee.identityCard"/></th><%-- 身份证号 --%>
                    <td><span><s:textfield name="identityCard" cssClass="text"/></span></td>
                  </tr>
                </table>
              </div>
            </div>
            
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
            
            <%--=================== 选择上司 ===================== --%>
            <div class="section" id="b_higher">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom02_popDataTableOfEmployee(this,$('#brandInfoId').serialize()+'&'+$('#positionCategoryId').serialize());return false;">
	       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="employee.higher"/></span>
	       		</a>
              </div>
              <div class="section-content">
				<table class="detail" cellpadding="0" cellspacing="0">
				  <tr>
                   	<th style="width: 15%"><s:text name="employee.higher"/></th><%-- 上司 --%>
                    <td style="width: 85%" id="higherResult"></td>
                  </tr>
				</table>
			  </div>
            </div>
            
            <%--=================== 关注用户 ===================== --%>
            <div class="section" id="b_likeEmployee">
              <div class="section-header clearfix">
              	<a class="add left" href="#" onclick="bscom02_popDataTableOfLikeEmployee(this,$('#brandInfoId').serialize()); return false;">
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
				      <th width="30%"><s:text name="employee.departType"/></th>
				      <th width="10%"><s:text name="employee.operation"/></th>
				    </tr>
				  </thead>
				  <tbody>
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
				      <th width="30%"><s:text name="employee.departType"/></th>
				      <th width="10%"><s:text name="employee.operation"/></th>
				    </tr>
				  </thead>
				  <tbody>
				  </tbody>
				</table>
			  </div>
            </div>
            
            <div class="section">
              <div class="section-header clearfix">
            	<span id="hideMoreInfo" class="hide"><s:text name="employee.hideMoreInfo" /></span>
            	<span id="showMoreInfo" class="hide"><s:text name="employee.showMoreInfo" /></span>
            	<a class="add left" href="#" onclick="bsemp04_more(this); return false;">
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
	                    <td><span><s:textfield name="birthDay" cssClass="text"/></span></td>
	                    <th><s:text name="employee.gender"/></th><%-- 性别 --%>
	                    <td>
	                    	<s:radio name="gender" list="#application.CodeTable.getCodes('1006')" 
	                    		listKey="CodeKey" listValue="Value"></s:radio>
	              		</td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.academic"/></th><%-- 学历 --%>
	                    <td>
	                    	<s:select name="academic" list="#application.CodeTable.getCodes('1042')"
	              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}"/>
	                    </td>
	                  	<th><s:text name="employee.maritalStatus"/></th><%-- 婚姻状况 --%>
	                    <td>
	                    	<s:select name="maritalStatus" list="#application.CodeTable.getCodes('1043')"
	              		 		listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{global.select}"/>
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
	              	<a class="add right" href="#" onclick="addDiv('#b_address','#h_address'); return false;">
		       			<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.add"/><s:text name="employee.addressInfo"/></span>
		       		</a>
	              </div>
	              <div class="section-content">
	               	<table class="detail" cellpadding="0" cellspacing="0">
	                  <caption>
	                  	<span class="left">
		                  	<span class="highlight"><s:text name="employee.def_display"/></span><%-- 默认显示 --%>
	                  	</span>	
	                  </caption>
	                  <tr>
	                    <th><s:text name="employee.province"/></th><%-- 省份 --%>
	                    <td>
	                    	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
	                    		list="provinceList" listKey="provinceId" listValue="provinceName" 
	                    		onchange="BSEMP04_getCity(this,'%{getCity_url}','%{global.select}');"/>
	              		</td>
	                    <th><s:text name="employee.city"/></th><%-- 城市 --%>
	                    <td>
	                    	<select name="cityId">
	                    		<option value=""><s:text name="global.page.select"/></option>
	                    	</select>
	                    </td>
	                  </tr>
	                  <tr>
	                    <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
	                    <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
	                    <th><s:text name="employee.address"/></th><%-- 地址 --%>
	                    <td>
	                    	<span>
	                    		<s:textfield name="address" cssClass="text" cssStyle="width:270px" maxlength="100"/>
	                    		<input type="hidden" name="defaultFlag" value="1"/>
	                    	</span>
	                    </td>
	                  </tr>
	                </table>
	           	  </div>
	            </div>
	            
	            <%--=================== 入离职信息 ===================== --%>
	            <div class="section" id="b_quit">
	              <div class="section-header">
	              	<strong>
	              		<span class="ui-icon icon-ttl-section-info-edit"></span>
	              		<s:text name="employee.quit_info"/><%-- 入离职信息  --%>
	              	</strong>
	              </div>
	              <div class="section-content">
					<table class="detail" cellpadding="0" cellspacing="0">
	                  	<tr>
		                    <th><s:text name="employee.com_date"/></th><%-- 入职日期  --%>
		                    <td><span><s:textfield name="commtDate" cssClass="text"/></span></td>
		                    <th><s:text name="employee.dep_date"/></th><%-- 离职日期  --%>
		                    <td><span></span></td>
		                </tr>
		                <tr>
		                    <th><s:text name="employee.dep_reason"/></th><%-- 离职理由  --%>
		                    <td colspan="3"><span></span></td>
		                </tr>
		            </table>
	              </div>
	            </div>
              </div>
            </div>
            
          	<%--===================  操作按钮 ===================== --%>
            <div id="saveButton" class="center clearfix">
          		<cherry:show domId="BINOLBSEMP0103">
          			<button class="save" onclick="BSEMP04_saveEmp('${save_url}');return false;">
	              		<span class="ui-icon icon-save"></span>
	              		<span class="button-text"><s:text name="global.page.save"/></span>
	              	</button>
          		</cherry:show>
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
  	<%--======== 隐藏备用地址 ========= --%>
	<div id="h_address">
       <table class="detail" cellpadding="0" cellspacing="0">
         <caption>
       		<span class="left"><s:text name="employee.bkp_address"/></span><%-- 备用地址 --%>
       		<a class="delete right" href="#" onclick="delDiv(this); return false;">
       			<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span>
       		</a>
         </caption>
         <tr>
           <th><s:text name="employee.province"/></th><%-- 省份 --%>
              <td>
              	<s:select name="provinceId" headerKey="" headerValue="%{global.select}"
               		list="provinceList" listKey="provinceId" listValue="provinceName" 
               		onchange="BSEMP04_getCity(this,'%{getCity_url}','%{global.select}');"/>
        	  </td>
              <th><s:text name="employee.city"/></th><%-- 城市 --%>
              <td>
              	<select name="cityId">
              		<option value=""><s:text name="global.page.select"/></option>
              	</select>
              </td>
         </tr>
         <tr>
           <th><s:text name="employee.zipCode"/></th><%-- 邮编 --%>
           <td><span><s:textfield name="zipCode" cssClass="text"/></span></td>
           <th><s:text name="employee.address"/></th><%-- 地址 --%>
           <td>
           	<span>
            	<s:textfield name="address" cssClass="text" cssStyle="width:270px" maxlength="100"/>
            	<input type="hidden" name="defaultFlag" value="0"/>
           	</span>
          </td>
         </tr>
       </table>
    </div>
</div>
</s:i18n>
<div class="hide">
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
<s:hidden name="emp_flag" id="emp_flag"></s:hidden>
<s:select list="positionCategoryList" listKey="positionCategoryId" listValue="categoryCode" id="positionCategoryTemp"></s:select>
</div>
<jsp:include page="/WEB-INF/jsp/bs/common/BINOLBSCOM01.jsp" flush="true" />
