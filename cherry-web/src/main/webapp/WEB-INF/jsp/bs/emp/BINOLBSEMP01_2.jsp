<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>


<script type="text/javascript">

$(document).ready(function() {
	
	// 员工查询
	bsemp01_search();

	
	// 省份选择
	$("#province").click(function(){
		// 显示省份列表DIV
		bscom03_showRegin(this,"provinceTemp");
	});
	// 城市选择
	$("#city").click(function(){
		// 显示城市列表DIV
		bscom03_showRegin(this,"cityTemp");
	});
} );

</script>


<s:i18n name="i18n.bs.BINOLBSEMP01">
	<s:text name="global.page.select" id="select_default"/>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorMessage"></div>
    <div style="display: none" id="errorMessageTemp">
    <div class="actionError">
       <ul><li><span><s:text name="employee.errorMessage"/></span></li></ul>         
    </div>
    </div>
    <div style="display: none" id="errorMessageTemp1">
    <div class="actionError">
       <ul><li><span><s:text name="employee.errorMessage1"/></span></li></ul>         
    </div>
    </div>
    
    <%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" action="/Cherry/basis/search" method="post" class="inline" onsubmit="bsemp01_search();return false;">
        	<s:hidden name="provinceId" id="provinceId"/>
        	<s:hidden name="cityId" id="cityId"/>
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            	<input type="checkbox" name="validFlag" value="1"/><s:text name="invalid_employee"/>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
              	<%-- 用户代号 --%>
              	<label><s:text name="employee.employeeCode"/></label>
                	<s:textfield name="employeeCode" cssClass="text"/>
              </p>
              <p>
              	<%-- 员工姓名 --%>
              	<label><s:text name="employee.employeeName"/></label>
                	<s:textfield name="employeeName" cssClass="text"/>
              </p>
              <p>
                	<%-- 登录帐号 --%>	
                	<label><s:text name="employee.longinName"/></label>
                	<s:textfield name="longinName" cssClass="text"/>
              </p>
              <div class="clearfix" style="line-height:25px;">
                 <%--部门 --%>	
                 <label class="left"><s:text name="employee.section"/></label>
                 <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
              	 <table class="all_clean left"><tbody id="inOrganization_ID"></tbody></table>
                 <%--<input type="hidden" name="inOrganizationId" id="inOrganizationId" value=''>--%>
                 <%--<span id="departNameReceive" ></span>--%>
                 <a class="add" onclick="openDepartBox();return false;">
                     <span class="ui-icon icon-search"></span>
                     <span class="button-text"><s:text name="global.page.Popselect"/></span>
                 </a>
               </div>
            </div>
            <div class="column last" style="width:49%; height: auto;">
            <div class="clearfix" style="line-height:25px;margin-bottom:7px;">

                	<%-- 岗位 --%>	
                	<label><s:text name="employee.post"/></label>
                	<s:select name="positionCategoryId" list="positionCategoryList"
              		 		listKey="positionCategoryId" listValue="categoryName" headerKey="" headerValue="%{select_default}" cssStyle="width:100px;"/>
              </div>
              <div style="line-height:25px;margin-bottom:6px;" class="clearfix">

                <%-- 员工所属省份 --%>
                <label><s:text name="employee.province"/></label>
                <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
                	<span id="provinceText" class="button-text" style="min-width:100px;text-align:left;"><s:text name="global.page.select"/></span>
     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
     		 	</a>
              </div>
             <div class="clearfix" style="line-height:25px;">
                <%-- 员工所属城市 --%>
                <label><s:text name="employee.city"/></label>
                <a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
                	<span id="cityText" class="button-text" style="min-width:100px;text-align:left;"><s:text name="global.page.select"/></span>
     		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
     		 	</a>
              </div>
            </div>
          </div>
          <p class="clearfix">
           		<cherry:show domId="BINOLBSEMP0106">
           		<%-- 员工查询按钮 --%>
           		<button class="right search" onclick="bsemp01_search();return false;">
           			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
           		</button>
           		</cherry:show>
          </p>
        </cherry:form>
      </div>
      <div id="section" class="section hide">
        <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span>
        		<s:text name="global.page.list"/>
        	</strong>
        </div>
        <div class="section-content">
          <div class="toolbar clearfix">
           	<span class="left">
           		<s:url action="BINOLBSEMP05_delete" id="delEmployee"></s:url>
           		<%-- EXCEL导出URL --%>
				<s:url id="xls_url" action="BINOLBSEMP01_export" />
				<cherry:show domId="BINOLBSEMP01EX">
		     	<span style="margin-right: 10px;"> 
		     	<a id="export" class="export" onclick="exportExcel('${xls_url}');return false;">
					<span class="ui-icon icon-export"></span> 
					<span class="button-text"><s:text name="global.page.exportExcel" /></span>
				</a>
				</span>
				</cherry:show>
                <cherry:show domId="BINOLBSEMP0102">
                <a href="" class="add" onclick="bsemp01_editValidFlag('enable','${delEmployee}');return false;">
                   <span class="ui-icon icon-enable"></span>
                   <span class="button-text"><s:text name="global.page.enable"/></span>
                </a>
                </cherry:show>
                <cherry:show domId="BINOLBSEMP0103">
                <a href="" class="delete" onclick="bsemp01_editValidFlag('disable','${delEmployee}');return false;">
                   <span class="ui-icon icon-disable"></span>
                   <span class="button-text"><s:text name="global.page.disable"/></span>
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
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
            <thead>
              <tr>              
                <%-- NO. --%>
                <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
                <%-- 员工代号 --%>
                <th><s:text name="employee.employeeCode"/><span class="ui-icon ui-icon-document"></span></th>
                <%-- 登录帐号 --%>
                <th><s:text name="employee.longinName"/></th>
                <%-- 员工姓名 --%>
                <th><s:text name="employee.employeeName"/></th>
                <%-- 所属省份 --%>
                <th><s:text name="employee.province"/></th>
                <%-- 所属城市 --%>
                <th><s:text name="employee.city"/></th>
                <%-- 所属岗位 --%>
                <th><s:text name="employee.post"/></th>
                <%-- 所属品牌 --%>
                <th><s:text name="employee.brand"/></th>
                <%-- 所属部门 --%>
                <th><s:text name="employee.depart"/></th>
                <%-- 手机 --%>
                <th><s:text name="employee.mobilePhone"/></th>
                <%-- 邮箱 --%>
                <th><s:text name="employee.email"/></th>
                <%-- 有效区分 --%>
                <th><s:text name="employee.flag"/></th>
              </tr>
            </thead>           
          </table>
        </div>
      </div>
    </div>
	<%-- ================== 省市选择DIV START ======================= --%>
	<div id="provinceTemp" class="ui-option hide" style="width:325px;">
		<div class="clearfix">
			<span class="label"><s:text name="global.page.range"></s:text></span>
			<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
		</div>
		<s:iterator value="reginList">
	 		<s:set name="provinceList" value="provinceList" />
	    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
	    	<ul class="u2">
	     		<s:iterator value="#provinceList">
	         		<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
	         			<s:property value="provinceName"/>
	         		</li>
	      		</s:iterator>
	      	</ul>
	    	</div>
	   	</s:iterator>
	</div>
	<div id="cityTemp" class="ui-option hide"></div>
	<%-- 下级区域查询URL --%>
	<s:url id="querySubRegionUrl" value="/common/BINOLCM08_querySubRegion"></s:url>
	<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
	<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
	<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
	<%-- ================== 省市选择DIV  END  ======================= --%>
</s:i18n>
<%--员工查询URL --%>
<s:url id="search_url" value="/basis/BINOLBSEMP01_search"/>
<s:hidden name="search_url" value="%{search_url}"/>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>    