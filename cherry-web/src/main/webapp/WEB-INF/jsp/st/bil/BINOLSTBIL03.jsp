<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL03.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>  
<s:i18n name="i18n.st.BINOLSTBIL03">
    <div class="hide">
        <s:url id="search_url" value="/st/BINOLSTBIL03_search"/>
        <s:url id="export_url" action="BINOLSTBIL03_export"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:text name="BIL03_select" id="defVal"/>
        <div id="BIL03_select">${defVal}</div>
    </div>
     <div class="panel-header">
        <%-- 出库单查询 --%>
        <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
        </div>
      </div>
      <div id="errorMessage"></div>
      <div class="panel-content">
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="BINOLSTBIL03.search(); return false;">
            <div class="box-header">
            <strong>
                <span class="ui-icon icon-ttl-search"></span>
                <%-- 查询条件 --%>
                <s:text name="BIL03_condition"/>
            </strong>
            </div>
            <div class="box-content clearfix">
              <div class="column" style="width:50%;height:60px;">
                <p>
                <%-- 出库单号 --%>
                  <label><s:text name="BIL03_billNo"/></label>
                  <s:textfield name="billNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
                </p>
                <p>
                <%-- 产品名称 --%>
                    <label><s:text name="BIL03_productName"/></label>
                      <s:textfield name="nameTotal" cssClass="text" maxlength="100"/>
                    <s:hidden value="" name="prtVendorId" id="prtVendorId"></s:hidden>
                </p>
              </div>
              <div class="column last" style="width:49%;height:55px;">
               <p id="dateCover"  class="date">
                <%-- 日期范围 --%>
                  <label><s:text name="BIL03_date"/></label>
                  <span><s:textfield id="startDate" name="startDate" cssClass="date"/></span> - <span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                </p>    
                <p>
                 <%-- 审核状态 --%>
                 <label><s:text name="BIL03_verifiedFlag"/></label>
                 <s:text name="BIL03_selectAll" id="BIL03_selectAll"/>
                 <s:select name="verifiedFlag" 
                    list='#application.CodeTable.getCodes("1007")' 
                    listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{BIL03_selectAll}"/>
                 </p>
              </div>
              <%-- ======================= 组织联动共通导入开始  ============================= --%>
              <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
                  <s:param name="businessType">1</s:param>
                  <s:param name="operationType">1</s:param>
              </s:action>
              <%-- ======================= 组织联动共通导入结束  ============================= --%>
            </div>
            <p class="clearfix">
             <%--<cherry:show domId="SSPRO0727QUERY">--%>
                    <button class="right search" type="submit" onclick="BINOLSTBIL03.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="BIL03_search"/></span>
                    </button>
              <%-- </cherry:show> --%>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 查询结果一览 --%>
            <s:text name="BIL03_results_list"/>
         </strong>
        </div>
          <div class="section-content">
            <div class="toolbar clearfix">
            	<cherry:show domId="BINOLSTBIL03EXP">
                    <a class="export left" onclick="BINOLSTBIL03.exportExcel(this);return false;"  href="${export_url}">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><s:text name="global.page.export"></s:text></span>
					</a>
				</cherry:show>
                <span id="headInfo" style=""></span>
                <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
                <span class="button-text">
                <%-- 设置列 --%>
                <s:text name="BIL03_colSetting"/>
                </span></a></span></div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th><s:text name="BIL03_num"/></th><%-- No. --%>
                  <th><s:text name="BIL03_billNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 出库单号 --%>
                  <th><s:text name="BIL03_departName"/></th><%-- 部门  --%>
                  <th><s:text name="BIL03_inventName"/></th><%-- 实体仓库 --%>
                  <th><s:text name="BIL03_logicinventName"/></th><%-- 逻辑仓库 --%>
                  <th><s:text name="BIL03_Quantity"/></th><%-- 总数量 --%>   
                   <th><s:text name="BIL03_price"/></th><%-- 总数量 --%>   
                  <th><s:text name="BIL03_outfreedate"/></th><%-- 出库时间 --%>
                  <th><s:text name="BIL03_verifiedFlag"/></th><%-- 审核状态 --%>
                  <th><s:text name="BIL03_employeeName"/></th><%-- 操作员 --%>
                    <th><s:text name="BIL03_binEmployeeIDAudit"/></th><%-- 审核者--%>
                <th><s:text name="BIL03_Comments"/></th><%-- 备注--%>
                </tr>
              </thead>
            </table>
          </div>
        </div>
      </div>
 </s:i18n>
 <%-- ================== dataTable共通导入 START ======================= --%>
    <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
    <%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ============ 弹出dataTable 产品共通导入 START ================= --%>
<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />
<%-- ============ 弹出dataTable 产品共通导入 END =================== --%>
    <script type="text/javascript">
        // 节日
        var holidays = '${holidays }';
        $('#startDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#endDate').val();
                return [value,'maxDate'];
            }
        });
        $('#endDate').cherryDate({
            holidayObj: holidays,
            beforeShow: function(input){
                var value = $('#startDate').val();
                return [value,'minDate'];
            }
        });
    </script>
    <input type="hidden"  id="defStartDate" value=''/>
	<input type="hidden"  id="defEndDate"	value=''/>