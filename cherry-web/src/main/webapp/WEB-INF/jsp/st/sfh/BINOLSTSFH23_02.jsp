<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%-- 产品订货--%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js?201610061427"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js?201610061427"></script>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js?201610061427"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH22.js?201610061427"></script>
<s:i18n name="i18n.st.BINOLSTSFH22">

<cherry:form id="mainForm" class="inline" onsubmit="return false;">
    <div class="hide">
        <input type="hidden" id="currentMenuID" name="currentMenuID" value="BINOLSTSFH23_02"/>
        <input type="hidden" id="counterCode" value='<s:property value="counterCode"/>'/>
        <s:text name="SFH22_Select" id="select"/>
        <s:url id="url_getdepotAjax" value="/st/BINOLSTSFH22_getDepot" />
        <span id ="urlgetdepotAjax" >${url_getdepotAjax}</span>
        <s:url id="getLogicInfo_url" value="/st/BINOLSTSFH22_getLogicInfo" />
        <span id ="getLogicInfo" >${getLogicInfo_url}</span>
        <s:url id="s_submitURL" value="/st/BINOLSTSFH22_submit" />
        <span id ="submitURL" >${s_submitURL}</span>
        <s:url id="s_btnSendMsmURL" value="/st/BINOLSTSFH22_sendMsm" />
        <span id ="btnSendMsmURL" >${s_btnSendMsmURL}</span>
        <s:url id="s_btnSelectSuggestPrduct" value="/st/BINOLSTSFH22_selectSuggestProduct" />
        <span id ="btnSelectSuggestPrductURL" >${s_btnSelectSuggestPrduct}</span>
        <s:url id="s_saveURL" value="/st/BINOLSTSFH22_save" />
        <span id ="saveURL" >${s_saveURL}</span>
        <s:url id="add_url" value="/common/BINOLCM02_popPrtImageDialog" /> <%-- 浓妆淡抹订单复制页面专用--%>
        <span id ="add_url" >${add_url}</span>
        <s:url id="url_getStockCount" value="/st/BINOLSTSFH22_getPrtVenIdAndStock" />
        <span id ="s_getStockCount" >${url_getStockCount}</span>
        <s:url id="url_getOutDepart" value="/st/BINOLSTSFH22_getOutDepart" />
        <span id ="s_getOutDepart" >${url_getOutDepart}</span>
        <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
        <input type="hidden" id="brandInfoId" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
        <s:url id="getOrderInfo_url" value="/st/BINOLSTSFH23_getOrderInfo"/> <%-- 根据订单号查询详细订单信息--%>
        <s:hidden name="getOrderInfoUrlID" value="%{getOrderInfo_url}"/>
    </div>
    <div class="panel-header">
        <div class="clearfix"> 
            <span class="breadcrumb left">      
                <span class="ui-icon icon-breadcrumb"></span><s:text name="订单复制"/>
            </span>
        </div>
    </div>
    <div id="actionResultDisplay"></div>
     <div id="errorMessage1"></div> 
    <%-- ==================隐藏域参数======================= --%>
    <div id ="123" style="display:none">
           <input type="hidden" name="inOrganizationId" id="inOrganizationId" value='${initInfoMap.defaultDepartID}'>
           <input type="hidden" id="inTestType" value="<s:property value='initInfoMap.defaultTestType'/>"></input>
           <input type="hidden" name="outOrganizationId" id="outOrganizationId" value='${initInfoMap.defaultOutDepartID}'>
           <input type="hidden" id="date"  name="date" value='<s:property value="date"/>'>
           <input type="hidden" name="isAllowNegativeInventory"  id="isAllowNegativeInventory" value='<s:property value="isAllowNegativeInventory"/>'>
           <s:select name="inLogicDepotId" list="inLogicDepotList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="true" cssStyle="width:200px;" onchange="BINOLSTSFH22.clearDetailData('flag');return false;"/>
	       <select style="width:200px;" name="inDepotId" id="inDepotId" disabled="true" onchange="BINOLSTSFH22.clearDetailData();return false;">
	              <s:iterator value="inDepotList">
	                  <option value="<s:property value="BIN_DepotInfoID" />">
	                      <s:property value="DepotCodeName"/>
	                  </option>
	              </s:iterator>
	       </select>
	          
     </div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>
    </div>
    <div id="errorMessage"  class="actionError" style="display:none" >     
        <ul>
            <li><span id="errorMessage1">操作失败</span></li>
        </ul>
    </div>
        <div style="display: none" id="EBS00145"><!-- 请选择需要删除的订单行 -->
	    <div class="actionError">
	       <ul><li><span><s:text name="SFH22_EBS00145"/></span></li></ul>         
	    </div>
	 </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
    <s:if test="hasActionErrors()">
        <div class="actionError" id="actionResultDiv">
            <s:actionerror/>
        </div>
        <div style="height:20px"></div>
    </s:if>
    <s:else>
        <div class="panel-content">
            <div class="section">
                <div class="section-header">
                    <strong>
                        <span class="ui-icon icon-ttl-section-info"></span><s:text name="SFH22_general"/>
                    </strong>
                </div>
            <div class="section-content">
                <table class="detail">
                    <tr>
                        <th><s:text name="SFH22_orderNum"/></th>
                         <td> <span class="left"><s:property value='initInfoMap.defaultOrderNo'/></span>
                          <input type="hidden" name="orderNum" id="orderNum" value='${initInfoMap.defaultOrderNo}'>
                         </td>
                        <th><s:text name="SFH22_expectDeliverDate"/></th>
                        <td><span><s:textfield name="expectDeliverDate" id="expectDeliverDate" cssClass="date" readOnly="readOnly"></s:textfield></span> </td>
                    </tr>
                    <tr>
                        <th><s:text name="SFH22_inDepartCodeName"/></th>
                        <td> <span id="inDepartCodeName" class="left"><s:property value='initInfoMap.defaultDepartCodeName'/></span></td>
                        <th><s:text name="SFH22_employeeName"/></th>
                        <td><s:property value="#session.userinfo.employeeName"/></td>
                    </tr>
                    <tr>
                        <th> <s:text name="SFH22_deliverAddress"/></th>
                        <td colspan=1>
                            <span> <input class="text" type="text" name="deliverAddress" id="deliverAddress" maxlength="200" value="${initInfoMap.defaultAddress}" /></span>
                        </td>
                        <th><s:text name="SFH22_orderStatus"/></th>
                        <td><span  id="orderStatus"><s:property value="#application.CodeTable.getVal('1146',initInfoMap.defaultVerifiedFlag)"/></span>
                        </td>
                    </tr>
                    <tr>
                        <th><s:text name="SFH22_remark"/></th>
                        <td colspan=3>
                            <input class="text" type="text" name="reasonAll" id="reasonAll" maxlength="200" style="width:95%;"/>
                        </td>
                    </tr>
                </table>
                </div>
            </div>
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="SFH22_detail"/></strong></div>
            <div class="section-content">
            <div class="toolbar clearfix">
                <span class="left">
                    <a id="spanBtnadd" class="add" onclick="BINOLSTSFH22.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="列表模式选择产品"/></span></a> 
                    <a id="spanBtdelete" class="delete" onclick="BINOLSTSFH22.deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="SFH22_delete"/></span></a>
                    <a id="spanBtnaddP" class="add" onclick="BINOLSTSFH22.popAdd();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="商城模式选择产品"/></span></a>
                    <a id="suggestProduct" class="add" onclick="BINOLSTSFH22.openSuggestDay('${initInfoMap.defaultDepartID}');"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="建议订货单"/></span></a>
                </span>
            </div>
            <div id="canceldetail">
                <div style="width:100%;overflow-x:scroll;">
                <input type="hidden" id="rowNumber" value="0"/>
                <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
                    <thead>
                       <tr>
                            <th class="tableheader" width="3%">
                                <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
                                <s:text name="SFH22_allselect"/>
                            </th>
                            <th class="tableheader" width="15%" onclick="BINOLSTSFH22.sortTable('_unitCodeArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH22_code"/><span id="_unitCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="15%" onclick="BINOLSTSFH22.sortTable('_barCodeArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH22_barcode"/><span id="_barCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="18%"><s:text name="SFH22_proname"/></th>
                            <th class="tableheader" width="8%"><s:text name="SFH22_price"/></th>
                            <th class="tableheader" width="5%"><s:text name="SFH22_NowCount"/></th>
                            <th class="tableheader" width="10%" onclick="BINOLSTSFH22.sortTable('_quantArr');">
                            <div class="DataTables_sort_wrapper"><s:text name="SFH22_quantity"/><span id="_quantArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                            <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="SFH22_money"/><s:text name="SFH22_moneyu"/></div></th>
                            <th class="tableheader" width="20%"><s:text name="SFH22_reason"/></th>
                            <th style="display:none">
                        </tr>
                    </thead>
                    <tbody id="databody">
                  
                    <s:if test="orderInfoList!=null">
          	 			<s:iterator  value="orderInfoList"  status="status" var="q">
          	 			<tr id="question1">
          	 			<td class="center"><input type="checkbox" id="chkbox" onclick="BINOLSTSFH22.changechkbox(this);"/></td>
          	 			<td><s:property value="unitCode"/></td>
          	 			<td><s:property value="barCode"/></td>
          	 			<td><s:property value="nameTotal"/></td>
          	 			<td style="text-align:right;"><s:property value="distributionPrice"/></td>
          	 			<td style="text-align:right;"><s:property value="stockAmount"/></td>
          	 			<td style="text-align:right;"><input value='<s:property value="Quantity"/>' name="quantityArr"style="width:120px;text-align:right;" id="quantityArr" cssClass="text-number" size="10" maxlength="9" onchange="BINOLSTSFH22.calcuAmount(this);"/></td>
          	 			<td style="text-align:right;"><s:property value="money"/></td>
          	 			<td class="center"><input value="" name="reasonArr"  id="reasonArr" size="25"  maxlength="200" cssStyle="width:98%"/></td>
          	 			<td style="display:none">
				        <input type="hidden" id="prtVendorId"  name="prtVendorId" value='<s:property value="BIN_ProductVendorID"/>'/>
	                    <input type="hidden" id="productVendorIDArr" name="productVendorIDArr" value='<s:property value="BIN_ProductVendorID"/>'/>
	                    <input type="hidden" id="priceUnitArr" name="priceUnitArr"  value='<s:property value="Quantity"/>'/>
	                    <input type="hidden" id="stockAmount" name="stockAmount"  value='<s:property value="stockAmount"/>'/>
	                    <input type="hidden" id="referencePriceArr" name="referencePriceArr" value=""/></td>
	          	 		</tr>
          	 			</s:iterator>
          	 		</s:if>
                    </tbody>
                </table>
                </div>
            </div>
            <div id="mydetail" class="section">
            </div>
            <hr class="space" />
            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
                <tr>
                    <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                    <td class="center"><s:text name="SFH22_totalQuantity"/></td><%-- 总数量 --%>
                    <td class="center"><s:text name="SFH22_totalAmount"/></td><%-- 总金额--%>
                </tr>
                <tr>
                    <td class="center">
                    <%-- 总数量 --%>
                    <span id="totalQuantity">0</span>
                    </td>
                    <td class="center">
                        <%-- 总金额--%>
                        <span id="totalAmount">0.00</span>
                    </td>
                </tr>
            </table>
            <hr class="space" />
            <div class="center clearfix">
                <button id="save"  class="save" type="button" onclick="BINOLSTSFH22.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="SFH22_Save"/></span></button>
                <button id="sumbit" class="confirm" type="button" onclick="BINOLSTSFH22.btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="SFH22_OK"/></span></button>
                <button id="btnSendMsm" class="confirm" type="button" onclick="BINOLSTSFH22.btnSendMsm()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="确认款已付"/></span></button>
            </div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
<%-- ================== 弹出发货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出收货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/BINOLSTCM12.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
</cherry:form>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
    <input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
    <input type="hidden" id="notOnlyOneWarning" value='<s:text name="SFH22_notOnlyOne"/>'/>
    <input type="hidden" id="noOutDepart" value='<s:text name="SFH22_noOutDepart"/>'/>
    <input type="hidden" id="noOutDepot" value='<s:text name="SFH22_noOutDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH22_noInDepart"/>'/>
    <input type="hidden" id="noInDepot" value='<s:text name="SFH22_noInDepot"/>'/>
    <input type="hidden" id="noInDepart" value='<s:text name="SFH22_noInDepart"/>'/>
    <input type="hidden" id="configOutDepart" value='<s:text name="SFH22_configOutDepart"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
    <input type="hidden" id="errmsg_EST00048" value='<s:text name="EST00048"/>'/>
    <input type="hidden" id="errmsg_ECM00089" value='<s:text name="ECM00089"/>'/>
    <input type="hidden" id="errmsg9" value="建议订货天数只能是正整数"/>
</div>
    <%-- ====================== 建议订货生成天数弹出框 ====================== --%>
	 <div id="div_main">
	 <div id="sugggestDayPop" class="hide">
    	<div class="clearfix">
    	 <form id="sugggestDayForm">
    	 <div id="actionResultDisplay"></div>
    		<div>
    		<table style="width:100%;" class="detail">
	            <tr>
					<th><s:text name="建议订货天数"/><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
		    		<td><span><s:textfield id="orderDayNum" name="orderDayNum" cssClass="text" maxlength="20"/></span></td>
	            </tr>
    		</table>
    		</div>
    	</form>
    	</div>
    </div>
    </div>
    <%-- ====================== 建议订货生成天数弹出框  ====================== --%>
</s:i18n>
<%-- ================== dataTable共通导�?START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导�?   END  ======================= --%>
