<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS01.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>  
<s:i18n name="i18n.pt.BINOLPTRPS01">
    <s:url id="getDepartUrl" value="/common/BINOLCM00_queryDepart"/>
    <s:url id="getInventUrl" value="/common/BINOLCM00_queryInventory"/>
    <div class="hide">
        <s:url id="search_url" value="/pt/BINOLPTRPS01_search"/>
        <a id="searchUrl" href="${search_url}"></a>
        <s:text name="RPS01_select" id="defVal"/>
        <div id="RPS01_select">${defVal}</div>
    </div>
      <div class="panel-header">
        <%-- 盘点单查询 --%>
        <div class="clearfix"> 
            <span class="breadcrumb left"> 
                <span class="ui-icon icon-breadcrumb"></span>
                <%-- 仓库管理 --%>
                <s:text name="RPS01_breadcrumb"/>
                &gt; 
                <%-- 盘点单查询 --%>
                <s:text name="RPS01_title"/>
            </span>  
        </div>
      </div>
      <div id="errorMessage"></div>
      <div class="panel-content">
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="BINOLPTRPS01.search(); return false;">
            <div class="box-header">
            <strong>
                <span class="ui-icon icon-ttl-search"></span>
                <%-- 查询条件 --%>
                <s:text name="RPS01_condition"/>
            </strong>
            </div>
            <div class="box-content clearfix">
              <div class="column" style="width:50%;height:85px;">
                <p>
                <%-- 盘点单号 --%>
                  <label><s:text name="RPS01_stockTakingNo"/></label>
                  <s:textfield name="stockTakingNo" cssClass="text" maxlength="30"/>
                </p>
                <p>
                <%-- 产品名称 --%>
                    <label><s:text name="RPS01_productName"/></label>
                    <s:hidden name="prtVendorId"/>
                    <s:textfield name="productName" cssClass="text" maxlength="30"/>
                </p>
              </div>
              <div class="column last" style="width:49%;height:55px;">
               <p class="date">
                <%-- 盘点日期 --%>
                  <label><s:text name="RPS01_date"/></label>
                  <span><s:textfield id="startDate" name="startDate" cssClass="date"/></span> - <span><s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                </p>    
                <p>
                <%-- 盈亏 --%>
                  <label><s:text name="RPS01_profitKbn"/></label>
                 <s:text name="RPS01_selectAll" id="RPS01_selectAll"/>
                 <s:select name="profitKbn" 
                    list='#application.CodeTable.getCodes("1018")' 
                    listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS01_selectAll}"/>
                 </p> 
                 <p>
                 <%-- 审核状态 --%>
                 <label><s:text name="RPS01_verifiedFlag"/></label>
                 <s:text name="RPS01_selectAll" id="RPS01_selectAll"/>
                 <s:select name="verifiedFlag" 
                    list='#application.CodeTable.getCodes("1007")' 
                    listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS01_selectAll}"/>
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
                    <button class="right search" type="submit" onclick="BINOLPTRPS01.search();return false;">
                        <span class="ui-icon icon-search-big"></span>
                        <%-- 查询 --%>
                        <span class="button-text"><s:text name="RPS01_search"/></span>
                    </button>
                <%--</cherry:show>--%>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section hide">
          <div class="section-header">
          <strong>
            <span class="ui-icon icon-ttl-section-search-result"></span>
            <%-- 查询结果一览 --%>
            <s:text name="RPS01_results_list"/>
         </strong>
        </div>
          <div class="section-content">
            <div class="toolbar clearfix">
            <span id="headInfo" style=""></span>
            <%--
            <span class="left">
              <a class="export"><span class="ui-icon icon-export"></span>
                <span class="button-text">
             
                <s:text name="RPS01_export"/>
                </span></a> <a class="import"><span class="ui-icon icon-export"></span>
                <span class="button-text">
    
                <s:text name="RPS01_detailExport"/>
                </span>
                </a>
                <a class="import"><span class="ui-icon icon-export"></span>
      
                <span class="button-text"><s:text name="RPS01_gainExport"/></span></a></span> --%>
                <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
                <span class="button-text">
                <%-- 设置列 --%>
                <s:text name="RPS01_colSetting"/>
                </span></a></span></div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th><s:text name="RPS01_num"/></th><%-- No. --%>
                  <th><s:text name="RPS01_stockTakingNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 盘点单号 --%>
                  <th><s:text name="RPS01_departName"/></th><%-- 部门名称  --%>
                  <th><s:text name="RPS01_inventName"/></th><%-- 仓库名称 --%>
                  <th><s:text name="RPS01_gainQuantity"/></th><%-- 盘差 --%>
                  <th><s:text name="RPS01_summAmount"/></th><%-- 盘差金额 --%>
                  <th><s:text name="RPS01_type"/></th><%-- 盘点类型 --%>
                  <th><s:text name="RPS01_date"/></th><%-- 盘点日期 --%>
                  <th><s:text name="RPS01_verifiedFlag"/></th><%-- 审核状态 --%>
                  <th><s:text name="RPS01_employeeName"/></th><%-- 盘点员 --%>
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