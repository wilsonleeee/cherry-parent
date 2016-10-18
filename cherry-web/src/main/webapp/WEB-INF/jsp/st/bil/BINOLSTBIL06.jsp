<%--报损单明细 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/bil/BINOLSTBIL06.js"></script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>
<s:i18n name="i18n.st.BINOLSTBIL06">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTBIL06_doaction"/>
<s:url id="save_url" value="/st/BINOLSTBIL06_save"/>
<s:url id="submit_url" value="/st/BINOLSTBIL06_submit"/>
<s:url id="delete_url" value="/st/BINOLSTBIL06_delete"/>
<s:url id="url_getdepotAjax" value="/st/BINOLSTBIL06_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTBIL06_getStockCount" />
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
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="BIL06_title"/>&nbsp;(<s:text name="BIL06_num"/>:<s:property value="outboundFreeMainData.BillNo"/>)</span>
        </div>
    </div>
    <div class="tabs">
   <ul>
      <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li>
      <li>
          <a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
      </li>
    </ul>

   <!-- 将画面上的div 设置id="tabs-1" 此div一般是指class="panel-content"的div，如： -->
   <div id="tabs-1" class="panel-content">
	<div>
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
          		<input type="hidden" name="billType" value="LS"/>
          		<input type="hidden" name="pageId" value="BINOLSTBIL06"/>
          		<input type="hidden" name="billId" value="${outboundFreeID}"/>
          	</div>
	          	<cherry:show domId="BINOLSTBIL06PNT">
	          	<button onclick="openPrintApp('Print');return false;" class="confirm right">
					<span class="ui-icon icon-file-print"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.print"/></span>
				</button>
				</cherry:show>
				<cherry:show domId="BINOLSTBIL06VEW">
				<button onclick="openPrintApp('View');return false;" class="confirm right">
					<span class="ui-icon icon-file-view"></span>
					<span class="button-text" style="font-size:12px;"><s:text name="global.page.view"/></span>
				</button>
				</cherry:show>
            	<cherry:show domId="BINOLSTBIL06EXP">
            	<div id="export_param_hide" class="hide"><input type="hidden" name="billId" value="${outboundFreeID}"/></div>
	             <button id="exportButton" disabled="disabled" class="confirm right" onclick="BINOLCM99.getExportDialog('BINOLSTBIL06',1);BINOLCM99.setExportParams('#export_param_hide');return false;">
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
                            <%-- 报损单号 --%>
                            <th><s:text name="BIL06_billNo"/></th>
                            <td><s:property value="outboundFreeMainData.BillNoIF"/></td>
                            <%-- 操作日期 --%>
                            <th><s:text name="BIL06_date"/></th>
                            <td><s:property value="outboundFreeMainData.OperateDate"/></td>
                        </tr>
                        <tr>
                            <%-- 申请人 --%>
                            <th><s:text name="BIL06_employeeName"/></th>
                            <td><s:property value="outboundFreeMainData.EmployeeName"/></td>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <%-- 报损理由 --%>
                            <th><s:text name="BIL06_reason"/></th>
                            <td colspan=3>
                            	<%--
	                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
	                            	<s:textfield name="comments" cssStyle="width:99%" maxlength="200" value="%{Comments}"></s:textfield>
	                            </s:if>
	                            <s:else>
								--%>
	                            	<s:property value='outboundFreeMainData.Comments'/>
								<%--
	                            </s:else>
								--%>
                            </td>
                        </tr>
                    </tbody>
                </table>
                <table class="detail">
                    <tbody>
                    	<%--
                        <tr>
						--%>
                            <%-- 部门 --%>
						<%--
                            <th><s:text name="BIL06_DepartCodeName"/></th>
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                                <td>
                                    <span id="outOrgName" class="left"><s:property value="outboundFreeMainData.DepartCodeName"/></span>
                                    <a class="add right" onclick="BINOLSTBIL06.openDepartBox(this);">
                                        <span class="ui-icon icon-search"></span>
                                        <span class="button-text"><s:text name="BIL06_select"/></span>
                                    </a>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="outboundFreeMainData.DepartCodeName"/></td>
                            </s:else>
							<th></th>
							<td></td>
                        </tr>
						--%>
                        <tr>
                            <s:text name="BIL06_selectAll" id="BIL06_selectAll"/>
                            <%-- 实体仓库 --%>
                            <th><s:text name="BIL06_DepotCodeName"/></th>
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                                <td>
                                	<s:if test='null!=depotsInfoList'>
	                                	<s:select id="depotInfoID" name="depotInfoID" onchange="BINOLSTBIL06.refreshDetailID();" list="depotsInfoList" value="%{outboundFreeMainData.BIN_DepotInfoID}" listKey="BIN_DepotInfoID" listValue="DepotCodeName" headerKey="" cssStyle="width:200px;">
                                    	</s:select>
                                	</s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="outboundFreeMainData.DepotCodeName"/></td>
                            </s:else>
                            <%-- 逻辑仓库 --%>
                            <th><s:text name="BIL06_LogicInventoryName"/></th>
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                                <td>
                                	<s:if test='null!=logicDepotsInfoList'>
	                                    <s:select id="logicInventoryInfoID" name="logicInventoryInfoID" onchange="BINOLSTBIL06.refreshDetailID();" list="logicDepotsInfoList" value="%{outboundFreeMainData.BIN_LogicInventoryInfoID}"  listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
	                                    </s:select>
                                    </s:if>
                                </td>
                            </s:if>
                            <s:else>
                                <td><s:property value="outboundFreeMainData.LogicInventoryName"/></td>
                            </s:else>
                        </tr>
                    </tbody>
                </table>
                <table class="detail" style="margin-bottom: 5px;">
                    <tbody>
                        <tr>
                            <%-- 审核状态 --%>
                            <th><s:text name="BIL06_verifiedFlag"/></th>
                            <td><s:property value='#application.CodeTable.getVal("1323", outboundFreeMainData.VerifiedFlag)'/></td>
                            <%-- 审核者 --%>
                            <th><s:text name="BIL06_employeeNameAudit"/></th>
                            <td><s:property value="outboundFreeMainData.AuditName"/></td>
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
	            	<%-- 报损单明细一览 --%>
	            	<s:text name="BIL06_results_list"/>
            	</strong>
          	</div>
			<div class="section-content">
            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                <div class="toolbar clearfix">
                    <span class="left">
                        <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
                        <a class="add" onclick="BINOLSTBIL06.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="BIL06_add"/></span>
                        </a>
                        <a class="delete" onclick="BINOLSTBIL06.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="BIL06_delete"/></span>
                        </a>
                    </span>
                </div>
            </s:if>
            <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
            <input type="hidden" id="entryID" name="entryID" value='<s:property value="outboundFreeMainData.WorkFlowID"/>'/>
            <input type="hidden" id="actionID" name="actionID"/>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
				<thead>
	                <tr>
	                    <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
	                        <th class="center" width="1%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTBIL06.selectAll();"/><s:text name="BIL06_select"/></th><%-- 编号 --%>
	                    </s:if>
	                    <th class="center"><s:text name="BIL06_no"/></th><%-- 编号 --%>
	                    <th class="center"><s:text name="BIL06_UnitCode"/></th><%-- 厂商编码 --%>
	                    <th class="center"><s:text name="BIL06_BarCode"/></th><%-- 产品条码 --%> 
	                    <th class="center"><s:text name="BIL06_ProductName"/></th><%-- 产品名称 --%>
	                    <th class="center"><s:text name="BIL06_Price"/></th><%-- 单价 --%>
	                    <th class="center"><s:text name="BIL06_Quantity"/></th><%-- 数量 --%>
	                    <th class="center"><s:text name="BIL06_Amount"/></th><%-- 金额 --%>
	                    <th class="center"><s:text name="BIL06_remark"/></th><%-- 备注 --%>
	                </tr>
              	</thead>
              	<tbody id="databody">
              	    <%--
	                <tr id="dataRow0" style="display:none">
						<td id="dataTd0"><input id="chkbox" type="checkbox" value="0" onclick="BINOLSTBIL06.changechkbox(this);"/></td>
	                  	<td id="dataTd1" style="white-space:nowrap"></td>
	                  	<td id="dataTd2"></td>
		  				<td id="dataTd3"></td>
		            	<td id="dataTd4"></td>
		                <td id="dataTd5" class="alignRight"></td>
		                <td id="dataTd7" class="alignRight"><s:textfield name="quantityArr" id="quantityArr" cssClass="text-number" size="10" maxlength="9" onchange="BINOLSTBIL06.changeCount(this);"/></td>
		                <td id="money" style="text-align:right;"></td>
		                <td id="dataTd9"><s:textfield name="commentsArr" cssStyle="width:99%" maxlength="200"/></td>
		                <td style="display:none" id="dataTd10">
                      		<input type="hidden" id="priceUnitArr" name="priceUnitArr"/>
                      		<input type="hidden" id="productVendorIDArr" name="productVendorIDArr"/>
                      		<input type="hidden" id="productVendorPackageIDArr" name="productVendorPackageIDArr"/>
                      		<input type="hidden" id="inventoryInfoIDArr" name="inventoryInfoIDArr"/>
                      		<input type="hidden" id="logicInventoryInfoIDArr" name="logicInventoryInfoIDArr"/>
                      		<input type="hidden" id="storageLocationInfoIDArr" name="storageLocationInfoIDArr"/>
                      		<input type="hidden" id="inventoryInfoIDAcceptArr" name="inventoryInfoIDAcceptArr"/>
                  		</td>
	                </tr>
	                --%>
                	<s:iterator value="outboundFreeDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                        <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                            <td id="dataTd0"><input id="chkbox" type="checkbox" value="<s:property value='#status.index+1'/>" onclick="BINOLSTBIL06.changechkbox(this);"/></td>
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
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                                <s:textfield name="quantityArr" cssClass="text-number" size="10" maxlength="9" value="%{Quantity}" onchange="BINOLSTBIL06.changeCount(this);"></s:textfield>
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
                            <s:if test='"2".equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                                <s:textfield name="commentsArr" cssStyle="width:99%" maxlength="200" value="%{Comments}"></s:textfield>
                            </s:if>
                            <s:else>
                                <p><s:property value="Comments"/></p>
                            </s:else>
                        </td>
                        <td style="display:none" id="dataTd10">
                            <input type="hidden" id="priceUnitArr<s:property value='#status.index+1'/>" name="priceUnitArr" value="<s:property value='Price'/>"/>                   
                            <input type="hidden" id="productVendorPackageIDArr<s:property value='#status.index+1'/>" name="productVendorPackageIDArr" value="<s:property value='BIN_ProductVendorPackageID'/>"/>
                            <input type="hidden" id="inventoryInfoIDArr<s:property value='#status.index+1'/>" name="inventoryInfoIDArr" value="<s:property value='BIN_DepotInfoID'/>"/>
                            <input type="hidden" id="logicInventoryInfoIDArr<s:property value='#status.index+1'/>" name="logicInventoryInfoIDArr" value="<s:property value='BIN_LogicInventoryInfoID'/>"/>
                            <input type="hidden" id="storageLocationInfoIDArr<s:property value='#status.index+1'/>" name="storageLocationInfoIDArr" value="<s:property value='BIN_StorageLocationInfoID'/>"/>
                            <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
                            <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                        </td>
                    </tr>
                	</s:iterator>
				</tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='outboundFreeDetailData.size()'/>"/>
                <input type="hidden" value="<s:property value="outboundFreeMainData.UpdateTime"/>" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="outboundFreeMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="outboundFreeMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <input type="hidden" value="<s:property value="outboundFreeMainData.BIN_OrganizationID"/>" id="organizationID" name="organizationID">
                <s:hidden id="outboundFreeID" name="outboundFreeID" value="%{#request.outboundFreeID}"></s:hidden>
            </div>
            <hr class="space" />
            <s:if test='!"2".equals(operateType) && !@com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
				<tr>
                	<th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                	<td class="center"><s:text name="BIL06_totalQuantity"/></td><%-- 总数量 --%>
                	<td class="center"><s:text name="BIL06_totalAmount"/></td><%-- 总金额--%>
              	</tr>
              	<tr>
	                <td class="center">
	                    <%-- 总数量 --%>
	                    <s:if test='null!=outboundFreeMainData.TotalQuantity'>
	                        <span><s:text name="format.number"><s:param value="outboundFreeMainData.TotalQuantity"></s:param></s:text></span>
	                    </s:if>
	                    <s:else>&nbsp;</s:else>
	                </td>
	                <td class="center">
	                    <%-- 总金额--%>
	                    <s:if test='null!=outboundFreeMainData.TotalAmount'>
	                        <span><s:text name="format.price"><s:param value="outboundFreeMainData.TotalAmount"></s:param></s:text></span>
	                    </s:if>
	                    <s:else>&nbsp;</s:else>
	                </td>
              	</tr>
            </table>
            </s:if>
            <hr class="space" />
            <div class="center">
            <s:if test='"2".equals(operateType)'>
                <button class="save" onclick="BINOLSTBIL06.saveOrder();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                <button class="confirm" onclick="BINOLSTBIL06.submitOrder();return false;"><span class="ui-icon icon-confirm"></span>
                    <%-- 提交 --%>
                    <span class="button-text"><s:text name="global.page.submit"/></span>
                </button>
                <button class="delete" onclick="BINOLSTBIL06.deleteOrder();return false;"><span class="ui-icon icon-delete"></span>
                    <%-- 删除 --%>
                    <span class="button-text"><s:text name="global.page.delete"/></span>
                </button>
            </s:if>
            <s:elseif test='@com.cherry.cm.core.CherryConstants@OPERATE_LS_AUDIT.equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_AUDIT2.equals(operateType) || @com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
                <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
                <s:if test='@com.cherry.cm.core.CherryConstants@OPERATE_LS_EDIT.equals(operateType)'>
					<button class="save" onclick="BINOLSTBIL06.saveOrder();return false;"><span class="ui-icon icon-save"></span>
                    <%-- 保存 --%>
                    <span class="button-text"><s:text name="global.page.save"/></span>
                </button>
                </s:if>
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
	</div>
   <!-- 此处再加入tabs-2  div-->
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