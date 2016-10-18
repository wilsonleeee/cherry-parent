<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/departBar.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/rps/BINOLPTRPS33.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popDataTable.js"></script>
<script type="text/javascript">
    <%--节日 --%>
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
    $('#payStartDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#payEndDate').val();
            return [value,'maxDate'];
        }
    });
    $('#payEndDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#payStartDate').val();
            return [value,'minDate'];
        }
    });
    cherryValidate({            
        formId: "mainForm",     
        rules: {
            startDate: {dateValid:true},    // 开始日期
            endDate: {dateValid:true},   // 结束日期
            payStartDate: {dateValid:true},    // 单据付款开始日期
            payEndDate: {dateValid:true}   // 单据付款结束日期
            
        }       
    });
    $("#mainForm").find("input").each(function(){
        $(this).keydown(function(e){
            var curKey = e.which; 
            if(curKey == 13){
                binOLPTRSP33_search();
                return false;
            }
        });
    });
</script>
<style>
    #DEPARTLINE{
        margin-left: 90px;
        margin-right: 5px;
        margin-bottom: 5px;
    }
    #RANGELABLE{
        width: 65px; 
        float:left;
        margin-top: 4px;
        text-align: right;
        padding-left:15px;
    }
    #DEPARTLINE span{
        margin-right: 2px;
    }
</style>

<s:url id="search_url" value="/pt/BINOLPTRPS33_search"/>
<%-- 商品详细URL --%>
<s:url id="getSaleProPrmSum_url" value="/pt/BINOLPTRPS33_getSaleProPrmSum"/>
<%-- EXCEL导出URL --%>
<s:url id="xls_url" value="/pt/BINOLPTRPS33_export"/>
<s:i18n name="i18n.pt.BINOLPTRPS33">
<%--
<div id="saleTitle" class="hide">
    <s:text name='RPS33_saleDetail'></s:text> 
</div>
<div class="hide" id="saleDialog"></div>
--%>
<div class="hide">
    <div id="dialogInitMessage"><s:text name="dialog_init_message" /></div> <%-- 初始化中... --%>
</div>
<div id="RPS33_select" class="hide"><s:text name="global.page.all" /></div>
<div class="panel-header">
    <%-- ###电商拦截订单一览记录查询 --%>
    <div class="clearfix"> 
        <span class="breadcrumb left">      
            <jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
        </span>  
    </div>
</div>

<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示   END  ======================= --%>

<div class="panel-content">
    <div class="box">
        <cherry:form id="mainForm" class="inline">
            <input name="brandInfoId" id="brandInfoId" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
            <div class="box-header">
                <strong>
                    <span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/>
                </strong>
            </div>
            <div class="box-content clearfix">
                <div style="padding: 15px 0px 5px;">
                    <table class="detail" cellpadding="0" cellspacing="0" id="rps33SearchId">
                        <tbody>
                            <tr>
                                <%--单据号 --%>
                                <th><s:text name="RPS33_billCode" /></th>
                                <td>
                                    <span><s:textfield name="billCode" cssClass="text" maxlength="35" onblur="ignoreCondition(this);return false;"/></span> 
                                </td>
                                <%--下单日期 --%>
                                <th><s:text name="RPS33_date" /></th>
                                <td id="dateCover">
                                    <span><s:textfield id="startDate" name="startDate" cssClass="date" readonly=""/>-<s:textfield id="endDate" name="endDate" cssClass="date"/></span>
                                </td>
                            </tr>
                            <tr>
                            	 <%-- 单据状态 --%>
                                <th><s:text name="RPS33_billState" /></th>
                                <td>
                                    <span>
                                    	<s:text name="global.page.all" id="RPS34_selectAll"/>
                                        <s:select name="billState" list="#application.CodeTable.getCodes('1310')"
                                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS34_selectAll}"/>
                                    </span>
                                </td>  
                                <%--单据付款时间--%>
                                <th><s:text name="RPS33_BillPayTime" /></th>
                                <td id="dateCover1">
                                    <span><s:textfield id="payStartDate" name="payStartDate" cssClass="date"/>-<s:textfield id="payEndDate" name="payEndDate" cssClass="date"/></span>
                                </td>                                                                   
                            </tr>
                            <tr>
                                <%-- 销售人员 --%>
                                <th><s:text name="RPS33_saleEmployeeName" /></th>
                                <td>
                                    <span><s:textfield name="employeeCode" cssClass="text" maxlength="30"/> </span>
                                </td>
                                <%-- 交易类型 --%>
                                <th><s:text name="RPS33_saleType" /></th>
                                <td>
                                    <span>
                                        <s:text name="global.page.all" id="RPS33_selectAll"/>
                                        <s:select name="saleType" list="#application.CodeTable.getCodes('1055')"
                                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS33_selectAll}"/>
                                    </span>
                                </td>                               
                            </tr>
                            <tr>
                                <th><s:text name="RPS33_campaignCode" /></th>
                                <td >
                                    <s:hidden name="campaignMode"></s:hidden>
                                    <s:hidden name="campaignCode"></s:hidden>
                                    <s:hidden name="campaignName"></s:hidden>
                                    <span id="campaignDiv" style="line-height: 18px;"></span>
                                    <s:url action="BINOLCM02_initCampaignDialog" namespace="/common" id="searchCampaignInitUrl"></s:url>
                                    <a class="add" onclick="binolptrps33.popCampaignList('${searchCampaignInitUrl}');return false;">
                                        <span class="ui-icon icon-search"></span>
                                        <span class="button-text"><s:text name="global.page.Popselect" /></span>
                                    </a>
                                </td>
                                <%-- 消费者类型 --%>
                                <th><s:text name="RPS33_consumerType" /></th>
                                <td>
                                    <span>
                                        <s:text name="global.page.all" id="RPS33_selectAll"/>
                                        <s:select name="consumerType" list="#application.CodeTable.getCodes('1105')"
                                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS33_selectAll}"/>
                                    </span>
                                </td>                                
                            </tr>
                            <tr>
                                <%-- 会员卡号 --%>
                                <th><s:text name="RPS33_memCode" /></th>
                                <td>
                                    <span><s:textfield name="memCode" cssClass="text"/></span>
                                </td>
                                <th><s:text name="RPS33_payType" /></th>
                                <td>
                                    <span>
                                        <s:text name="global.page.all" id="RPS13_selectAll"/>
                                        <s:select name="payTypeCode" list="#application.CodeTable.getCodes('1175')"
                                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS33_selectAll}"/>
                                    </span>
                                </td>                               
                            </tr>
                            <tr>
                                <%-- 流水号 --%>
                                <th><s:text name="RPS33_orderNo" /></th>
                                <td>
                                    <span><s:textfield name="saleRecordCode" cssClass="text" maxlength="30" onblur="ignoreCondition(this);return false;"/></span>
                                </td>  
                                <%-- 单据类型 --%>
                                <th><s:text name="RPS33_ticketType" /></th>
                                <td>
                                    <span>
                                        <s:select name="ticketType" list="#application.CodeTable.getCodes('1261')"
                                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS33_selectAll}"/>
                                    </span>
                                </td>                                                     
                            </tr>
                            <tr>
                            	<%-- 是否预售 --%>
                                <th><s:text name="RPS33_preSale" /></th>
                                <td>
                                	<span>
                                        <s:select name="preSale" list="#application.CodeTable.getCodes('1344')"
                                            listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{RPS33_selectAll}"/>
                                    </span>
                                </td>
                                <%-- 宝贝编码 --%>
                                <th><s:text name="RPS33_outCode" /></th>
                                <td>
                                	<input type="text" name="outCode" maxlength="40">
                                	<input hidden="" name="exceptionList" id="exceptionList" value="${exceptionList}"/>
                                </td> 
                             </tr>
                             <tr>
					    <td style="padding:3px;" colspan="4" class="detail box2-active">
							<%--商品 --%>
							<div class="detail_box2" >
							    <%-- 销售商品 --%>
								<div class="section">
									<div class="section-header">
										<div class="left"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="RPS33_salePrmProduct" /></strong></div>
											<div class="right">
											   	<input type="hidden"  name="saleProPrmConcatStr" value="OR"/>
												<a class="add" onClick="binolptrps33.openProDialog('0');return false;">
													<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPro" /></span>
												</a>
												<a class="add" onClick="binolptrps33.openPrmDialog('0');return false;">
													<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="global.page.selPrm" /></span>
												</a>
											</div>
									</div>
								    <div class="section-content">
										<table cellspacing="0" cellpadding="0" style="width:100%;" id="proTable" class="hide">
											<thead>
												<tr>
												  <th style="width:25%;"><s:text name="global.page.prtvendorcode" /></th>
												  <th style="width:25%;"><s:text name="global.page.barcode" /></th>
												  <th style="width:35%;"><s:text name="global.page.productname" /></th>
												  <th class="center" style="width:15%;"><s:text name="global.page.option" /></th>
												</tr>
											</thead>
											<tbody id="proShowDiv"></tbody>
										</table>
								    </div>
								</div>
							</div>	
					    </td>
					</tr>
                        </tbody>
                    </table>
                </div>
                <%-- ======================= 组织联动共通导入开始  ============================= --%>
                <s:action name="BINOLCM13_query" namespace="/common" executeResult="true">
                    <s:param name="showType">0</s:param>
                    <s:param name="businessType">3</s:param>
                    <s:param name="operationType">1</s:param>
                    <s:param name="mode">dpat,area,chan</s:param>
                </s:action>
                <%-- ======================= 组织联动共通导入结束  ============================= --%>
            </div> 
            <p class="clearfix">
                <%-- 查询 --%>
                <button class="right search" type="button" onclick="binOLPTRSP33_search('<s:property value="#search_url"/>')">
                    <span class="ui-icon icon-search-big"></span>
                    <span class="button-text"><s:text name="global.page.search"/></span>
                </button>
            </p> 
         </cherry:form>  
    </div>
    <%-- ====================== 结果一览开始 ====================== --%>
    <div id="section" class="section hide">
        <div class="section-header">
            <strong> 
            <span class="ui-icon icon-ttl-section-search-result"></span> 
            <s:text name="global.page.list" />
            </strong>
        </div>
        <div class="section-content">
            <div class="toolbar clearfix">
                    <span style="margin-right:10px;">
                        <cherry:show domId="BINOLPTRPS33EXP">
                       <a id="export" class="export" onclick="binOLPTRSP33_exportExcel('${xls_url}');return false;">
                          <span class="ui-icon icon-export"></span>
                          <span class="button-text"><s:text name="global.page.exportExcel"/></span>
                       </a>
                       </cherry:show>
                       <cherry:show domId="BINOLPTRPS33CSV">
                       <a id="export" class="export" onclick="binolptrps33.exportCsv();return false;">
                          <span class="ui-icon icon-export"></span>
                          <span class="button-text"><s:text name="global.page.exportCsv"/></span>
                       </a>
                       </cherry:show>
                    </span>
                <span id="headInfo" ></span>
                <span class="right"> <%-- 设置列 --%>
                    <a class="setting"> 
                        <span class="ui-icon icon-setting"></span> 
                        <span class="button-text"><s:text name="global.page.colSetting" /></span> 
                    </a>
                </span>
            </div>
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
                <thead>
                    <tr>
                        <%-- No. --%>
                        <th><s:text name="No." /></th>
                        <%-- 单据号 --%>
                        <th><s:text name="RPS33_billCode" /><span class="ui-icon ui-icon-document"></span></th>
                        <%-- 柜台 --%>
                        <th><s:text name="RPS33_counterName" /></th>
                        <%-- BA--%>
                        <th><s:text name="RPS33_saleBA" /></th>
                        <%-- 会员卡号   --%>
                        <th><s:text name="RPS33_memCode" /></th>
                        <%-- 业务类型   --%>
                        <th><s:text name="RPS33_saleType" /></th>
                        <%-- 消费者类型   --%>
                        <th><s:text name="RPS33_consumerType" /></th>
                        <%-- 单据类型   --%>
                        <th><s:text name="RPS33_ticketType" /></th>
                        <%-- 总数量 --%>
                        <th><s:text name="RPS33_quantity" /></th>
                        <%-- 总金额  --%>
                        <th><s:text name="RPS33_amount" /></th>
                        <%-- 运费 --%>
                        <th><s:text name="RPS33_ExpressCost" /></th>
                        <%-- 下单时间 --%>
                        <th><s:text name="RPS33_billCreateTime" /></th>
                        <%-- 单据状态   --%>
                        <th><s:text name="RPS33_billState" /></th>
                        <%-- 是否预售   --%>
                        <th><s:text name="RPS33_preSale" /></th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
    <div class="hide">
		<div id="deleteButton"><s:text name="global.page.delete" /></div><%-- 商品列表删除按钮 --%>
	</div>
    <%-- ====================== 结果一览结束 ====================== --%>
</div>
</s:i18n>
<input type="hidden"  id="defStartDate" value=''/>
<input type="hidden"  id="defEndDate"   value=''/>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<span id="search" style="display:none">${search_url}</span>
<span id="getSaleProPrmSum_url" style="display:none">${getSaleProPrmSum_url}</span>
<jsp:include page="/WEB-INF/jsp/common/popExportDialog.jsp" flush="true" />
<div class="hide">
<s:url id="exportCsvUrl" action="BINOLPTRPS33_exportCsv" ></s:url>
<a id="exportCsvUrl" href="${exportCsvUrl}"></a>
<s:url id="exporChecktUrl" action="BINOLPTRPS33_exportCheck" ></s:url>
<a id="exporChecktUrl" href="${exporChecktUrl}"></a>
</div>