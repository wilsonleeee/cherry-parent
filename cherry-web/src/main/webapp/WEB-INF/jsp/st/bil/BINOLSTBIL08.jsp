<%--移库单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL08.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL08">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL08_doaction"/>
<s:url id="save_url" value="/st/BINOLSTBIL08_save"/>
<s:url id="submit_url" value="/st/BINOLSTBIL08_submit"/>
<s:url id="delete_url" value="/st/BINOLSTBIL08_delete"/>
<s:url id="url_getdepotAjax" value="/st/BINOLSTBIL08_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTBIL08_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <a id="deleteUrl" href="${delete_url}"></a>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>     
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL08_title"/>&nbsp;(<s:text name="BIL08_num"/>:<s:property value="shiftMainData.BillNo"/>)</span>
        </div>
    </div>
    <div class="tabs">
    	
       <ul>	
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li><%-- 基本信息 --%>
            <li><a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
            </li><%-- 单据流程 --%>
      </ul>
	<div id="tabs-1" class="panel-content">
		<form id="mainForm" method="post" class="inline">
            <%--防止有button的form在text框输入后按Enter键后自动submit --%>
            <button type="submit" onclick="return false;" class="hide"></button>
			<div class="section">
			<div id="actionResultDisplay"></div>
			<%-- ================== 错误信息提示 START ======================= --%>
			<div id="errorDiv2" class="actionError" style="display:none">
            	<ul>
                	<li><span id="errorSpan2"></span></li>
            	</ul>         
        	</div>
			<%-- ================== 错误信息提示   END  ======================= --%>
          	<div class="section-header">
          		<strong>
          			<span class="ui-icon icon-ttl-section-info"></span>
                	<%-- 基本信息 --%>
                	<s:text name="global.page.title"/>
                </strong>
                <div id="print_param_hide" class="hide">
	          		<input type="hidden" name="billType" value="MV"/>
	          		<input type="hidden" name="pageId" value="BINOLSTBIL08"/>
	          		<input type="hidden" name="billId" value="${productShiftID}"/>
          		 </div>
	          	<cherry:show domId="BINOLSTBIL08PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSTBIL08VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
                <cherry:show domId="BINOLSTBIL08EXP">
                <div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${productShiftID}"/></div>
	             <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL08',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
	             	<span class="ui-icon icon-file-export"></span>
	             	<span class="button-text" style="font-size:12px;"><s:text name="global.page.exportPrint"/></span>
	             </button>
	             </cherry:show>
          	</div>
          	<div class="section-content">
            <div>
                <div class="box-header"></div>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 移库单号 --%>
                            <th><s:text name="BIL08_billNo"/></th>
                            <td><s:property value="shiftMainData.BillNoIF"/></td>
                            <%-- 操作日期 --%>
                            <th><s:text name="BIL08_date"/></th>
                            <td><s:property value="shiftMainData.OperateDate"/></td>
                        </tr>
                        <tr>
                            <%-- 申请人 --%>
                            <th><s:text name="BIL08_employeeName"/></th>
                            <td><s:property value="shiftMainData.EmployeeName"/></td>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <%-- 移库理由 --%>
                            <th><s:text name="BIL08_reason"/></th>
                            <td colspan=3>
                            	<%--
	                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
	                            	<s:textfield name="comments" cssStyle="width:99%" maxlength="200" value="%{Comments}"></s:textfield>
	                            </s:if>
	                            <s:else>
								--%>
	                            	<s:property value="shiftMainData.Comments"/>
								<%--
	                            </s:else>
								--%>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail">
                    <tbody>
                     
                        <tr>
                            <%-- 部门 --%>
                            <th><s:text name="BIL08_DepartCodeName"/></th>
                           <td><s:property value="shiftMainData.DepartCodeName"/></td>
                           <%-- 仓库 --%>
                            <th><s:text name="BIL08_DepotCodeName"/></th>
                            <td><s:property value="shiftMainData.DepotCodeName"/></td>
                          </tr>
                      </tbody>
                      </table>
                   <table class="detail">
                    <tbody>
                         <tr>
                            <%-- 移出方逻辑仓库 --%>
                            <th><s:text name="BIL08_FromLogicInventoryName"/></th>
                            <td><s:property value="shiftMainData.FromLogicInventoryName"/></td>
                            <%-- 移入方逻辑仓库 --%>
                            <th><s:text name="BIL08_ToLogicInventoryName"/></th>
                            <td><s:property value="shiftMainData.ToLogicInventoryName"/></td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail" style="margin-bottom: 5px;">
                    <tbody>
                        <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="BIL08_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1324", shiftMainData.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="BIL08_employeeNameAudit"/></th>
                            <td><s:property value="shiftMainData.AuditName"/></td>
                        </tr>
                    </tbody>
                </table>
                <div class="clearfix"></div>
            </div>
          </div>
        </div>
        <div class="section">
			<div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-search-result"></span>
	            	<%-- 移库单明细一览 --%>
	            	<s:text name="BIL08_results_list"/>
            	</strong>
          	</div>
			<div class="section-content">
            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
                <div class="toolbar clearfix">
                    <span class="left">
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <a class="add" onclick="BINOLSTBIL08.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="BIL08_add"/></span>
                        </a>
                        <a class="delete" onclick="BINOLSTBIL08.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="BIL08_delete"/></span>
                        </a>
                    </span>
                </div>
            </s:if>
            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
            <input type="hidden" id="entryID" name="entryID" value='<s:property value="shiftMainData.WorkFlowID"/>'/>
            <input type="hidden" id="actionID" name="actionID"/>
            <input type="hidden" id="organizationID" name="organizationID" value='<s:property value="shiftMainData.BIN_OrganizationID"/>'/>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
	                <tr>
	                    <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
	                        <th class="center" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL08.selectAll();"/><s:text name="BIL08_select"/></th><%-- 编号 --%>
	                    </s:if>
	                    <th class="center"><s:text name="BIL08_no"/></th><%-- 编号 --%>
	                    <th class="center"><s:text name="BIL08_UnitCode"/></th><%-- 厂商编码 --%>
	                    <th class="center"><s:text name="BIL08_BarCode"/></th><%-- 产品条码 --%> 
	                    <th class="center"><s:text name="BIL08_ProductName"/></th><%-- 产品名称 --%>
	                    <th class="center"><s:text name="BIL08_Price"/></th><%-- 单价 --%>
	                    <th class="center"><s:text name="BIL08_Quantity"/></th><%-- 数量 --%>
	                    <th class="center"><s:text name="BIL08_Amount"/></th><%-- 金额 --%>
	                    <th class="center"><s:text name="BIL08_remark"/></th><%-- 备注 --%>
	                </tr>
              	</thead>
              	<tbody id="databody">
              	    <%--
	                <tr id="dataRow0" style="display:none">
						<td id="dataTd0"><input id="chkbox" type="checkbox" value="0" onclick="BINOLSTBIL08.changechkbox(this);"/></td>
	                  	<td id="dataTd1" style="white-space:nowrap"></td>
	                  	<td id="dataTd2"></td>
		  				<td id="dataTd3"></td>
		            	<td id="dataTd4"></td>
		                <td id="dataTd5" class="alignRight"></td>
		                <td id="dataTd7" class="alignRight"><s:textfield name="quantityArr" id="quantityArr" cssClass="text-number" size="10" maxlength="9" onchange="BINOLSTBIL08.changeCount(this);"/></td>
		                <td id="money" style="text-align:right;"></td>
		                <td id="dataTd9"><s:textfield name="commentsArr" cssStyle="width:99%" maxlength="200"/></td>
		                <td style="display:none" id="dataTd10">
		                 	<input type="hidden" id="priceUnitArr" name="priceUnitArr"/>
                      		<input type="hidden" id="productVendorIDArr" name="productVendorIDArr"/>
                      		<input type="hidden" id="productVendorPackageIDArr" name="productVendorPackageIDArr"/>
                      		<input type="hidden" id="fromDepotInfoIDArr" name="fromDepotInfoIDArr"/>
                      		<input type="hidden" id="fromLogicInventoryInfoIDArr" name="fromLogicInventoryInfoIDArr"/>
                      		<input type="hidden" id="fromStorageLocationInfoIDArr" name="fromStorageLocationInfoIDArr"/>
                      		<input type="hidden" id="toDepotInfoIDArr" name="toDepotInfoIDArr"/>
                      		<input type="hidden" id="toLogicInventoryInfoIDArr" name="toLogicInventoryInfoIDArr"/>
                      		<input type="hidden" id="toStorageLocationInfoIDArr" name="toStorageLocationInfoIDArr"/>
                  		</td>
	                </tr>
	                --%>
                	<s:iterator value="shiftDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                        <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
                            <td id="dataTd0"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL08.changechkbox(this);"/></td>
                        </s:if>
                        <td id="dataTd1"><s:property value="#status.index+1"/></td>
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="ProductName"/></span></td>                        
                        <td id="dataTd5" class="alignRight">
                            <s:if test='null!=Price'>
                                <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="newCount" class="alignRight" style="width:10%;">
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
                                <s:textfield name="quantityArr" cssClass="text-number" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTBIL08.changeCount(this);"></s:textfield>
                            </s:if>
                            <s:else>
                            <s:if test='null!=Quantity'>
                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                            </s:if>
                            </s:else>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="money" class="alignRight">
                            <s:if test='null!=Price && null!=Quantity'>
                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd9">
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
                                <s:textfield name="commentsArr" cssStyle="width:99%" maxlength="200" value="%{Comments}"></s:textfield>
                            </s:if>
                            <s:else>
                                <p><s:property value="Comments"/></p>
                            </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>                   
                            <input type="hidden" id="productVendorPackageIDArr<s:property value='#status.index+1'/>" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
                            <input type="hidden" id="fromDepotInfoIDArr<s:property value='#status.index+1'/>" name="fromDepotInfoIDArr" value="<s:property value='FromDepotInfoID'/>"/>
                            <input type="hidden" id="fromLogicInventoryInfoIDArr<s:property value='#status.index+1'/>" name="fromLogicInventoryInfoIDArr" value="<s:property value='FromLogicInventoryInfoID'/>"/>
                            <input type="hidden" id="fromStorageLocationInfoIDArr<s:property value='#status.index+1'/>" name="fromStorageLocationInfoIDArr" value="<s:property value='FromStorageLocationInfoID'/>"/>
                            <input type="hidden" id="toDepotInfoIDArr<s:property value='#status.index+1'/>" name="toDepotInfoIDArr" value="<s:property value='ToDepotInfoID'/>"/>
                            <input type="hidden" id="toLogicInventoryInfoIDArr<s:property value='#status.index+1'/>" name="toLogicInventoryInfoIDArr" value="<s:property value='ToLogicInventoryInfoID'/>"/>
                            <input type="hidden" id="toStorageLocationInfoIDArr<s:property value='#status.index+1'/>" name="toStorageLocationInfoIDArr" value="<s:property value='ToStorageLocationInfoID'/>"/>
                            <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                        </td>
                    </tr>
                	</s:iterator>
				</tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='shiftDetailData.size()'/>"/>
                <input type="hidden" value="<s:property value="shiftMainData.UpdateTime"/>" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="shiftMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="shiftMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <input type="hidden" value="<s:property value="shiftMainData.BIN_OrganizationID"/>" id="organizationID" name="organizationID">
                
                <input type="hidden" value="<s:property value="shiftMainData.BIN_DepotInfoID"/>" id="BIN_DepotInfoID" name="BIN_DepotInfoID">
                <input type="hidden" value="<s:property value="shiftMainData.FromLogicInventoryInfoID"/>" id="FromLogicInventoryInfoID" name="FromLogicInventoryInfoID">
                <input type="hidden" value="<s:property value="shiftMainData.FromStorageLocationInfoID"/>" id="FromStorageLocationInfoID" name="FromStorageLocationInfoID">
                <input type="hidden" value="<s:property value="shiftMainData.ToLogicInventoryInfoID"/>" id="ToLogicInventoryInfoID" name="ToLogicInventoryInfoID">
                <input type="hidden" value="<s:property value="shiftMainData.ToStorageLocationInfoID"/>" id="ToStorageLocationInfoID" name="ToStorageLocationInfoID">
                <input type="hidden" value="<s:property value="shiftMainData.BIN_ProductVendorPackageID"/>" id="BIN_ProductVendorPackageID" name="BIN_ProductVendorPackageID">
                     
                <s:hidden id="productShiftID" name="productShiftID" value="%{#request.productShiftID}"></s:hidden>
                
            </div>
            <hr class="space" />
            <s:if test='!"2".equals(operateType) && !@com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
				<tr>
                	<th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                	<td class="center"><s:text name="BIL08_totalQuantity"/></td><%-- 总数量 --%>
                	<td class="center"><s:text name="BIL08_totalAmount"/></td><%-- 总金额--%>
              	</tr>
              	<tr>
	                <td class="center">
	                    <%-- 总数量 --%>
	                    <s:if test='null!=shiftMainData.TotalQuantity'>
	                        <span><s:text name="format.number"><s:param value="shiftMainData.TotalQuantity"></s:param></s:text></span>
	                    </s:if>
	                    <s:else>&nbsp;</s:else>
	                </td>
	                <td class="center">
	                    <%-- 总金额--%>
	                    <s:if test='null!=shiftMainData.TotalAmount'>
	                        <span><s:text name="format.price"><s:param value="shiftMainData.TotalAmount"></s:param></s:text></span>
	                    </s:if>
	                    <s:else>&nbsp;</s:else>
	                </td>
              	</tr>
            </table>
            </s:if>
            <hr class="space" />
            <div class="center">
            <s:if test='"2".equals(operateType)'>
                <button class="save" onclick="BINOLSTBIL08.saveShift();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.saveTemp"/></span>
                </button>
                <button class="confirm" onclick="BINOLSTBIL08.submitShift();return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                </button>
                <button class="delete" onclick="BINOLSTBIL08.deleteShift();return false;"><span class="ui-icon icon-delete"></span>
                    <%-- 删除 --%>
                    <span class="button-text"><s:text name="global.page.delete"/></span>
                </button>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_MV_AUDIT.equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_AUDIT2.equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_MV_EDIT.equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />             
            </s:elseif>
			<button class="close" onclick="window.close();return false;">
				<span class="ui-icon icon-close"></span>
				<%-- 关闭 --%>
				<span class="button-text"><s:text name="global.page.close"/></span>
			</button>
            </div>
          </div>
        </div>
        </form>
	</div>
	<div id="tabs-2">
	   <strong><s:text name="global.page.worksProcessing"/></strong>
	</div>
  </div>
</div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<%-- ================== 弹出datatable -- 产品共通START ======================= --%>
<%--<jsp:include page="/WEB-INF/jsp/common/popProductTable.jsp" flush="true" />--%>
<%-- ================== 弹出datatable -- 产品共通START ======================= --%>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 报表打印共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/exportDialog.jsp" flush="true" />
<%-- ==================  报表打印共通导入 End ======================== --%>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
</div>
<%-- ================== 工作流提示弹出窗口 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popOSDialog.jsp" flush="true" />
<%-- ==================  工作流提示弹出窗口End ======================== --%>