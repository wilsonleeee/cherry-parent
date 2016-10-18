<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<%@ page import = "java.util.*" %>
<%@page import="com.cherry.cm.core.CherryConstants"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/popOSDialog.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH03.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH05.js"></script>
<script type="text/javascript" src="/Cherry/js/st/common/BINOLSTCM15.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/ws/mng/BINOLWSMNG02_05.js"></script>
<script type="text/javascript">
//来自订单，刷新
var currentUnitID = $('#currentUnitID',window.opener.document).val();
if(currentUnitID == "BINOLSTSFH02"){
    if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
}
var referrer = document.referrer;
//来自订单执行生成发货单Action后跳转，同时又是在首页，需要刷新。
if(referrer.indexOf("BINOLSTSFH03")>-1){
	refreshGadgetTask();
}
window.onbeforeunload = function(){
	if(OS_BILL_Jump_needUnlock){
	    if (binOLSTSFH05_global.needUnlock) {
	        if (window.opener) {
	            window.opener.unlockParentWindow();
	        }
	    }
	}
};
</script>
<style>
    .box-header {
        padding: 0.2em 0;
    }
    table.detail {
         margin-bottom: 3px;
    }
</style>

<%
	//当前操作员工ID
	String optionEmployeeId = (String)request.getAttribute("optionEmployeeId");
	//发货单主表数据
	Map mainData = (Map)request.getAttribute("productDeliverMainData");
	//审核区分	未提交审核
	final String verifiedFlag = CherryConstants.AUDIT_FLAG_UNSUBMIT;
%>


<s:i18n name="i18n.st.BINOLSTSFH05">
<div class="main container clearfix">
<s:url id="doaction_url" value="/st/BINOLSTSFH05_doaction"/>
<s:url id="submit_url" value="/st/BINOLSTSFH05_submit"/>
<s:url id="save_url" value="/st/BINOLSTSFH05_save"/>
<s:url id="delete_url" value="/st/BINOLSTSFH05_delete"/>
<s:url id="url_getdepotAjax" value="/st/BINOLSTSFH03_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTSFH03_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>

<div class="hide">
    <a id="osdoactionUrl" href="${doaction_url}"></a>
    <a id="submitUrl" href="${submit_url}"></a>
    <a id="saveUrl" href="${save_url}"></a>
    <a id="deleteUrl" href="${delete_url}"></a>
    <s:text name="SFH05_pleaseSelect" id="pleaseSelect"></s:text>
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <div id="dialogConfirm"><s:text name="SFH05_dialog_confirm" /></div>
    <div id="dialogCancel"><s:text name="SFH05_dialog_cancel" /></div>
    <input type="hidden"  id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
    <s:text name="global.page.select" id="globalSelect"/>
    <input type="hidden" id="beforeDoActionFun_101"  value="binOLWSMNG02_05.beforeDoActionFun();"/>
</div>
<div id="div_main" class="panel ui-corner-all">
    <div class="panel-header">
        <div class="clearfix">
            <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="SFH05_title2"/>&nbsp;</span>
        </div>
    </div>

   <!-- 将画面上的div 设置id="tabs-1" 此div一般是指class="panel-content"的div，如： -->
    <div class="panel-content">
      
      <form id="mainForm" method="post" class="inline">
        <input type="hidden" id="operateType" name="operateType" value='<s:property value="operateType"/>'/>
        <input type="hidden" id="entryID" name="entryID" value='<s:property value="productDeliverMainData.WorkFlowID"/>'/>
        <input type="hidden" id="actionID" name="actionID"/>
        <input type="hidden" id="deliverType" name="deliverType" value='<s:property value="productDeliverMainData.DeliverType"/>'/>
        <input type="hidden" id="receiveDepot" name="receiveDepot" value="<s:property value="receiveDepotList.get(0).get('BIN_DepotInfoID')"/>"></input>
        <input type="hidden" id="receiveLogiInven" name="receiveLogiInven" value="<s:property value="receiveLogiInvenList.get(0).get('BIN_LogicInventoryInfoID')"/>"></input>
        <input type="hidden" name="comments" id="comments"/>
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
		</div>
          
            <div class="section-content">
                <div>
                <div class="box-header"></div>
                <table class="detail">
                    <tbody>
                        <tr>
                            <%-- 发货单单号 --%>
                            <th><s:text name="SFH05_deliverNo"/></th>
                            <td><s:property value="productDeliverMainData.DeliverNoIF"/></td>
                            <%-- 发货单日期 --%>
                            <th><s:text name="SFH05_date"/></th>
                            <td><s:property value="productDeliverMainData.Date"/></td>
                        </tr>
                        <tr>
                            <th><s:text name="SFH05_BA"/></th>
                            <td>
                                <s:if test='null != counterBAList && counterBAList.size>0'>
                                    <s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
                                </s:if>
                                <s:else>
                                    <s:property value="#session.userinfo.employeeName"/>
                                    <s:hidden name="tradeEmployeeID" value="%{#session.userinfo.BIN_EmployeeID}"></s:hidden>
                                </s:else>
                            </td>
                            <th></th>
                            <td></td>
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
	            <%-- 入出库明细一览 --%>
	            <s:text name="SFH05_results_list2"/>
            </strong>
          </div>
          <div class="section-content">
            <div class="toolbar clearfix">
            </div>
 
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                    <th id="th_No" class="center"><s:text name="SFH05_no"/></th><%-- 编号 --%>
                    <th id="th_UnitCode" class="center"><s:text name="SFH05_UnitCode"/></th><%-- 厂商编码 --%>
                    <th id="th_BarCode" class="center"><s:text name="SFH05_BarCode"/></th><%-- 产品条码 --%>
                    <th id="th_ProductName" class="center"><s:text name="SFH05_ProductName"/></th><%-- 产品名称 --%>
                    <th id="th_ReferencePrice" class="center" width="10%"><s:text name="SFH05_ReferencePrice"/></th><%-- 参考价格 --%>
                    <th id="th_Price" class="center" width="10%"><s:text name="SFH05_Price"/></th>
                    <th id="th_Quantity" class="center"><s:text name="SFH05_Quantity"/></th><%-- 数量 --%>
                    <th id="th_Amount" class="center"><s:text name="SFH05_Amount"/></th><%-- 金额 --%>
                    <th id="th_Remark" class="center"><s:text name="SFH05_remark"/></th><%-- 备注 --%>
                </tr>
              </thead>
              <tbody id="databody">
                <s:iterator value="productDeliverDetailData" status="status">
                    <tr id="dataRow<s:property value='#status.index+1'/>" class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
                        <td id="dataTd1"><s:property value="#status.index+1"/></td>
                        <td id="dataTd2"><span><s:property value="UnitCode"/></span></td>
                        <td id="dataTd4"><span><s:property value="BarCode"/></span></td>
                        <td id="dataTd3"><span><s:property value="ProductName"/></span></td>
                        <td id="dataTdReferencePrice" class="alignRight">
                            <s:if test='null!=ReferencePrice'>
                                <s:text name="format.price"><s:param value="ReferencePrice"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd5" class="alignRight">
                            <s:textfield name="priceUnitArr" cssClass="price hide" size="10" maxlength="9" value="%{getText('{0,number,##0.00}',{Price})}" onchange="BINOLSTSFH05.setPrice(this);return false;"></s:textfield>
                            <span id="spanCalPrice" class="hide"><span class="calculator" onclick="BINOLSTSFH05.initRateDiv(this);" title="<s:text name="SFH05_CalTitle"/>"></span></span>
                            <div id="hidePriceArr">
                                <s:text name="format.price"><s:param value="Price"></s:param></s:text>
                            </div>
                        </td>      
                        <td id="newCount" class="alignRight" style="width:10%;">
                        
                            <input type="text"  id="quantityArr" name="quantityArr" style="width:120px;text-align:right;" class="hide"  size="10" maxlength="4" value='<s:property value="Quantity"/>' onchange="BINOLSTSFH05.changeCount(this);"/>
                            <div id="hideQuantiyArr">
                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                            </div>
                        <%-- 
                            <s:if test='null!=Quantity'>
                                <s:text name="format.number"><s:param value="Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                            --%>
                        </td>
                        <td id="money" class="alignRight">
                            <s:if test='null!=Price && null!=Quantity'>
                                <s:text name="format.price"><s:param value="Price*Quantity"></s:param></s:text>
                            </s:if>
                            <s:else>&nbsp;</s:else>
                        </td>
                        <td id="dataTd9">
                            <p><s:property value="Comments"/></p>
                        </td>
                        <td style="display:none" id="dataTd10">
	                       <input type="hidden" id="referencePriceArr<s:property value='#status.index+1'/>" name="referencePriceArr" value="<s:property value='ReferencePrice'/>"/>                   
	                       <input type="hidden" id="suggestedQuantityArr<s:property value='#status.index+1'/>" name="suggestedQuantityArr" value="<s:property value='SuggestedQuantity'/>"/>
	                       <input type="hidden" id="productVendorIDArr<s:property value='#status.index+1'/>" name="productVendorIDArr" value="<s:property value='BIN_ProductVendorID'/>"/>
	                       <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
	                    </td>
                    </tr>
                </s:iterator>
              </tbody>
            </table>
            </div>
            <div style="display:none">
                <input type="hidden" id="rowNumber" value="<s:property value='productDeliverDetailData.size()'/>"/>
                <input type="hidden" value="<s:property value="productDeliverMainData.UpdateTime"/>" name="updateTime" id="updateTime">
                <input type="hidden" value="<s:property value="productDeliverMainData.ModifyCount"/>" name="modifyCount" id="modifyCount">
                <input type="hidden" value="<s:property value="productDeliverMainData.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
                <%-- 发货部门 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_OrganizationID"/>" id="outOrganizationID" name="outOrganizationID">
                <%-- 发货实体仓库 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_DepotInfoID"/>" id="outDepotInfoID" name="outDepotInfoID">
                <%-- 发货逻辑仓库 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_LogicInventoryInfoID"/>" id="outLogicInventoryInfoID" name="outLogicInventoryInfoID">
                <%-- 收货部门 --%>
                <input type="hidden" value="<s:property value="productDeliverMainData.BIN_OrganizationIDReceive"/>" id="inOrganizationID" name="inOrganizationID">              
                
                <s:hidden id="productDeliverID" name="productDeliverId" value="%{#request.productDeliverId}"></s:hidden>
                <input type="hidden" id="inTestType" value="<s:property value='productDeliverMainData.TestType'/>"></input>
                <input type="hidden" name="prtVendorId" value="<s:property value='BIN_ProductVendorID'/>"/>
                <input type="hidden" id="checkStockFlag" name="checkStockFlag" value="<s:property value='checkStockFlag'/>"/>
                <s:if test='operateType.equals("50")'>
                    <input type="hidden" id="receiveEdit" name="receiveEdit" value="YES"/>
                </s:if>
            </div>
            
            <hr class="space" />
           	<table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
              <tr>
                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
                <td class="center"><s:text name="SFH05_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="SFH05_totalAmount"/></td><%-- 总金额--%>
              </tr>
              <tr>
                <td id="totalQuantity" class="center">
                    <%-- 总数量 --%>
                    <s:if test='null!=productDeliverMainData.TotalQuantity'>
                        <span><s:text name="format.number"><s:param value="productDeliverMainData.TotalQuantity"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
                <td id="totalAmount" class="center">
                    <%-- 总金额--%>
                    <s:if test='null!=productDeliverMainData.TotalAmount'>
                        <span><s:text name="format.price"><s:param value="productDeliverMainData.TotalAmount"></s:param></s:text></span>
                    </s:if>
                    <s:else>&nbsp;</s:else>
                </td>
              </tr>
            </table>
            <hr class="space" />
            <div class="center">
			  <jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" />
              <button class="close" onclick="window.close();return false;"><span class="ui-icon icon-close"></span>
	            <%-- 关闭 --%>
	            <span class="button-text"><s:text name="global.page.close"/></span>
              </button>
            </div>
          </div>
        </div>
        </form>
      
      </div>

    </div>
    </div>
    <div class="rateDialog hide">
        <span id="spanCalTitle" style="display:none;"><s:text name="SFH05_CalTitle"/></span>
        <s:text name="SFH05_discountRate"/><%-- 折扣率 --%>
        <input class="number" id="priceRate" value="100" onblur="BINOLSTSFH05.closeDialog(this);return false;"  
            onkeyup="BINOLSTSFH05.setDiscountPrice(this);return false;"/><s:text name="global.page.percent"/>
        <input type ="hidden" id="curRateIndex" value=""/>
    </div>
</s:i18n>
<form action="BINOLSTSFH05_init" id="productDeliverDetailUrl" method="post">
    <input name="csrftoken" type="hidden"/>
    <s:hidden name="productDeliverId" value="%{#request.productDeliverId}"></s:hidden>
</form>
<div class="hide" id="dialogInit"></div>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
    <input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
    <input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00025" value='<s:text name="EST00025"/>'/>
    <input type="hidden" id="errmsg_EST00034" value='<s:text name="EST00034"/>'/>
    <input type="hidden" id="errmsg_EST00039" value='<s:text name="EST00039"/>'/>
</div>
