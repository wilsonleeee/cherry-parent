<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/wr/crp/BINOLWRCRP01.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>

<script type="text/javascript">
	//节日
	var holidays = '${holidays }';
    $('#campaignOrderDateStart').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#campaignOrderDateEnd').val();
            return [value,'maxDate'];
        }
    });
    $('#campaignOrderDateEnd').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#campaignOrderDateStart').val();
            return [value,'minDate'];
        }
    });
    
    $('#bookDateStart').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#bookDateEnd').val();
            return [value,'maxDate'];
        }
    });
    $('#bookDateEnd').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#bookDateStart').val();
            return [value,'minDate'];
        }
    });
    
    $('#finishTimeStart').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#finishTimeEnd').val();
            return [value,'maxDate'];
        }
    });
    $('#finishTimeEnd').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#finishTimeStart').val();
            return [value,'minDate'];
        }
    });
</script>
<s:i18n name="i18n.wp.BINOLWRCRP01">
<s:text name="global.page.select" id="select_default"></s:text>
    <div class="panel-header">
	  <div class="clearfix"> 
		<span class="breadcrumb left">	    
		  <span class="ui-icon icon-breadcrumb"></span>
		  <s:text name="WRCRP01_wrcrp"></s:text>&nbsp;&gt;&nbsp;<s:text name="WRCRP01_campaignOrderInfo"></s:text>
		</span>
	  </div>
    </div>
    <div class="panel-content">
      <div class="box">
        <cherry:form id="mainForm" class="inline">
          <div class="box-header">
          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
          </div>
          <div class="box-content clearfix">
            <div class="column" style="width:50%; height: auto;">
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_campaignOrderDate"/></label>
               	<s:textfield name="campaignOrderDateStart" cssClass="date"/>-<s:textfield name="campaignOrderDateEnd" cssClass="date"/>
              </p>
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_customerName"/></label>
               	<s:textfield name="customerName" cssClass="text"/>
              </p>
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_subType"/></label>
               	<s:select name="subType" list="#application.CodeTable.getCodes('1178')" listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{select_default}"/>
              </p>
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_state"/></label>
               	<select id="state" name="state">
				    <option value=""><s:text name="global.page.select"></s:text></option>
				    <option value="AR"><s:text name="WRCRP01_order"></s:text></option>
				    <option value="OK"><s:text name="WRCRP01_ok"></s:text></option>
				</select>
              </p>
            </div>
            <div class="column last" style="width:49%; height: auto;">
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_bookDate"/></label>
               	<s:textfield name="bookDateStart" cssClass="date"/>-<s:textfield name="bookDateEnd" cssClass="date"/>
              </p>
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_finishTime"/></label>
               	<s:textfield name="finishTimeStart" cssClass="date"/>-<s:textfield name="finishTimeEnd" cssClass="date"/>
              </p>
              <p>
               	<label style="width:100px;"><s:text name="WRCRP01_mobilePhone"/></label>
               	<s:textfield name="mobilePhone" cssClass="text"/>
              </p>
              <p>
                <label style="width:100px;"><s:text name="WRCRP01_employee"/></label>
                <s:hidden name="employeeName"></s:hidden>
                <s:select list="employeeList" listKey="baInfoId" listValue="baName" name="employeeId" headerKey="" headerValue="%{#select_default}"></s:select>
              </p>
            </div>
          </div>
          <p class="clearfix">
         	<button class="right search" type="button" onclick="BINOLWRCRP01.search();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search" /></span>
            </button>
          </p>
        </cherry:form>
      </div>
      <div id="section" class="section hide">
       	<div class="section-header" id="section-header">
        	<%--查询结果一览 --%>
			<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<s:text name="global.page.list"/>
          	</strong>
        </div>
		<div class="ui-tabs-panel">
        	<div class="toolbar clearfix">
			 	<span class="right"> <%-- 设置列 --%>
					<a class="setting"> 
						<span class="ui-icon icon-setting"></span> 
						<span class="button-text"><s:text name="global.page.colSetting" /></span> 
					</a>
				</span>
		 	</div>
        <div class="section" id="result_list">
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              	<thead>
	                <tr>
	                	<th><s:text name="WRCRP01_number"/></th>
	            		<%-- 二维码 --%>
		                <th><s:text name="WRCRP01_couponCode"/></th>
		            	<%-- 活动类型 --%>
		                <th><s:text name="WRCRP01_subType"/></th> 
		            	<%-- 客户姓名 --%>
		                <th><s:text name="WRCRP01_customerName"/></th> 
		                <%-- 手机号码 --%>
		                <th><s:text name="WRCRP01_mobilePhone"/></th>
		                <%-- 客户生日 --%>
		                <th><s:text name="WRCRP01_birthDay"/></th>
		                <%--客户性别 --%>
		                <th><s:text name="WRCRP01_gender"/></th>
		                <%--预约操作日期--%>
		                <th><s:text name="WRCRP01_campaignOrderDate"/></th>
		                <%--预约服务日期--%>
		                <th><s:text name="WRCRP01_bookDate"/></th>
		                <%--实际领用时间--%>
		                <th><s:text name="WRCRP01_finishTime"/></th>
		                <%--操作员--%>
		                <th><s:text name="WRCRP01_optPerson"/></th>
		                <%--状态--%>
		                <th><s:text name="WRCRP01_state"/></th>
	                </tr>
              	</thead>
            </table>
          </div>
       </div>
      </div>
    </div>
</s:i18n>
<div class="hide">
<s:a action="BINOLWRCRP01_search" id="searchUrl"></s:a>
</div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>