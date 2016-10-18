<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/wp/ws/mng/BINOLWSMNG07.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/common/popDataTable.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.wp.BINOLWSMNG07">
    <s:url id="search_url" value="/ws/BINOLWSMNG07_search"/>
    <s:url id="export_url" value="/st/BINOLSTBIL15_export"/>
    <s:text name="MNG07_selectAll" id="MNG07_selectAll"/>
    <div class="hide">
        <a id="searchUrl" href="${search_url}"></a>
    </div>
        <div class="panel-header">
            <%-- 盘点申请一览 --%>
            <div class="clearfix"> 
	            <span class="breadcrumb left">      
	                <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
	            </span>
	            <span class="right">
	            <cherry:show domId="BINOLWSMNG0703">
	                <a id="btnAddGivenCR" class="add" onclick="binOLWSMNG07.addBillInit(this);" style="color:black;">
	                    <span class="ui-icon icon-add"></span>
	                    <span class="button-text"><s:text name="MNG07_addGivenCntPrt"/></span>
	                </a>
	            </cherry:show>
	            <cherry:show domId="BINOLWSMNG0701">
	                <a id="btnAddSelCR" class="add" onclick="binOLWSMNG07.addBillInit(this);" style="color:black;">
	                    <span class="ui-icon icon-add"></span>
	                    <span class="button-text"><s:text name="MNG07_add"/></span>
	                </a>
	            </cherry:show>
                <cherry:show domId="BINOLWSMNG0702">
                    <a id="btnAddAllCR" class="add right" onclick="binOLWSMNG07.addBillInit(this);" style="color:black;">
                        <span class="ui-icon icon-add"></span>
                        <span class="button-text"><s:text name="MNG07_addAllCntPrt"/></span>
                    </a>
                </cherry:show>  
	            </span>
            </div> 
        </div>
        <%-- ================== 错误信息提示 START ======================= --%>
            <div id="errorMessage"></div>    
            <div style="display: none" id="errorMessageTemp">
                <div class="actionError"></div>
        </div>
        <%-- ================== 错误信息提示   END  ======================= --%>
        <div id="actionResultDisplay"></div>
        <div class="panel-content">
            <div class="box">
                <cherry:form id="mainForm" class="inline">
                        <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLWSMNG07"/>
                        <div class="box-header">
                            <strong>
                                <span class="ui-icon icon-ttl-search"></span>
	                            <%-- 查询条件 --%>
	                            <s:text name="MNG07_condition"/>
	                        </strong>
                        </div>
                        <div class="box-content clearfix">
                            <div class="column" style="width:50%;height:70px;">
                                <p id="dateCover" class="date">
	                                <%-- 日期 --%>
	                                <label><s:text name="MNG07_date"/></label>
	                                <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
                                </p>      
                                <p>
                                    <%-- 单号 --%>
                                    <label><s:text name="MNG07_billNo"/></label>
                                    <s:textfield name="stockTakingNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
                                </p>
                            </div>
                            <div class="column last" style="width:49%;height:55px;">
                                <p>  
	                                <%-- 审核状态 --%>
	                                <label><s:text name="MNG07_verifiedFlag"/></label>
	                                <s:select name="verifiedFlag" list='verifiedFlagsCAList'
	                                    listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{MNG07_selectAll}"/>
                                </p>
                                <p>
                                </p>
                            </div>
                        </div>
		                <p class="clearfix">
		                    <button class="right search"  onclick="binOLWSMNG07.search();return false;">
		                            <span class="ui-icon icon-search-big"></span>
		                            <span class="button-text"><s:text name="MNG07_search"/></span>
		                    </button>
		                </p>
                    </cherry:form>
                </div>
                <div id="section" class="section hide">
                    <div class="section-header">
                        <strong>
                            <span class="ui-icon icon-ttl-section-search-result"></span>
                            <%-- 查询结果一览 --%>
                            <s:text name="MNG07_results_list"/>
                        </strong>
                    </div>
                    <div class="section-content" id="result_list">
                        <div class="toolbar clearfix">
                        	<cherry:show domId="BINOLWSMNG07EXP">
                            <a class="export left" onclick="javascript:binOLWSMNG07.exportExcel(this);return false;"  href="${export_url}">
                                <span class="ui-icon icon-export"></span>
                                <span class="button-text"><s:text name="global.page.exportExcel"></s:text></span>
                            </a>
                            </cherry:show>
                            <span id="headInfo"></span>
                            <span class="right">
	                            <a class="setting">
	                                <span class="ui-icon icon-setting"></span>
	                                <span class="button-text">
	                                    <%-- 设置列 --%>
	                                    <s:text name="MNG07_colSetting"/>
	                                </span>
	                            </a>
                            </span>
                        </div>
                    </div>
		            <table id="dataTable"  cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
		                <thead>
		                    <tr>                 
				                <th><s:text name="MNG07_no"/></th><%-- No. --%>
		                        <th><s:text name="MNG07_billNo"/><span class="ui-icon ui-icon-document"></span></th><%-- 盘点申请单号 --%>
		                        <th><s:text name="MNG07_checkQuantity"/></th><%-- 实盘总数量 --%>
		                        <th><s:text name="MNG07_sumGainQuantity"/></th><%-- 盘差总数量 --%>
		                        <th><s:text name="MNG07_sumGainPrice"/></th><%-- 盘差总金额 --%>
		                        <th><s:text name="MNG07_type"/></th><%-- 盘点类型 --%>
		                        <th><s:text name="MNG07_date"/></th><%-- 盘点申请时间 --%>
		                        <th><s:text name="MNG07_verifiedFlag"/></th><%-- 审核状态 --%>
		                        <th><s:text name="MNG07_createEmployee"/></th><%-- 操作员 --%>
		                    </tr>
		                </thead>
		            </table>
                </div>
            </div>
			<div id="errmessage" style="display:none">
			    <input type="hidden" id="errmsg_EST00038" value='<s:text name="EST00038"/>'/>
			</div>
        </s:i18n>
        <div class="hide">
            <s:a action="BINOLWSMNG07_checkAddBillInit" namespace="/ws" id="checkAddBillInitUrl"></s:a>
            <s:a action="BINOLWSMNG07_addBillInit" namespace="/ws" id="addBillInitUrl"></s:a>
            <!-- 导出相关URL -->
            <s:a action="BINOLSTBIL15_export" namespace="/st" id="exportExcelUrl"></s:a>
            <s:a action="BINOLSTBIL15_exportSummary" namespace="/st" id="exportSummaryUrl"></s:a>
            <s:url id="exportCsvUrl"  namespace="/st" action="BINOLSTBIL15_exportCsv" ></s:url>
            <a id="exportCsvUrl" href="${exportCsvUrl}"></a>
            <s:url id="exporChecktUrl" namespace="/st" action="BINOLSTBIL15_exportCheck" ></s:url>
            <a id="exporChecktUrl" href="${exporChecktUrl}"></a>
            <!-- 查询单据时的部门共通条参数 -->
            <s:hidden name="params"></s:hidden>
        </div>
        <%-- ================== dataTable共通导入 START ======================= --%>
        <jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
        <%-- ================== dataTable共通导入    END  ======================= --%>
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
        <input type="hidden"  id="defEndDate"   value=''/>   