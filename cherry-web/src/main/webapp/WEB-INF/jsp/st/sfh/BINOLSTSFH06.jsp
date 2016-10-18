<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.*" %>
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Expires" content="Thu, 01 Dec 1994 16:00:00 GMT">
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/st/sfh/BINOLSTSFH06.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/common/BINOLSTCM15.js"></script>
<s:i18n name="i18n.st.BINOLSTSFH06">
<%-- 这个值用于点击生成空白单 建议发货单时设置  --%>
<input type="hidden" id="btnState" value=""/>
<input type="hidden" id="rowNumber" value="0"/>
<cherry:form id="mainForm" action="/Cherry/st/BINOLSTSFH06_SUBMIT">
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());

%>
<div class="hide">
<s:text name="SFH06_Select" id="select"/>
<s:hidden id="depotJson"></s:hidden>
<s:url id="url_getdepotAjax" value="/st/BINOLSTSFH06_getDepot" />
<span id ="urlgetdepotAjax" >${url_getdepotAjax}</span>
<s:url id="url_getconDepartAjax" value="/ss/BINOLSTSFH06_getDepart" />
<span id ="urlgetdepartAjax" >${url_getconDepartAjax}</span>
<s:url id="s_submitURL" value="/st/BINOLSTSFH06_submit" />
<span id ="submitURL" >${s_submitURL}</span>
<s:url id="s_saveURL" value="/st/BINOLSTSFH06_save" />
<span id ="saveURL" >${s_saveURL}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTSFH06_getPrtVenIdAndStock" />
<span id ="s_getStockCount" >${url_getStockCount}</span>
<!-- 取得指定产品在收货方的库存 -->
<s:url id="url_getReceiveStockCount" value="/st/BINOLSTSFH06_getReceivePrtStock" />
<span id ="s_getReceiveStockCount" >${url_getReceiveStockCount}</span>
<div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
<div id="dialogCancel"><s:text name="dialog_cancel" /></div>
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
<s:url id="addBlankBill_url" value="/st/BINOLSTSFH06_addBlankBill"/>
<a id="addBlankBillUrl" href="${addBlankBill_url}"></a>
<s:url id="getCostPriceURL" value="/st/BINOLSTSFH06_getCostPriceURL"/>
<span id ="getCostPriceURL" >${getCostPriceURL}</span>
<s:url id="searchSuggestBill_url" value="/st/BINOLSTSFH06_searchSuggestBill"/>
<a id="searchSuggestBillUrl" href="${searchSuggestBill_url}"></a>
<s:url id="url_getDeliverDetail" value="/st/BINOLSTCM12_getDeliverDetail" />
<span id ="s_getDeliverDetail" >${url_getDeliverDetail}</span>
<input type="hidden"  id="brandInfoId" value='<%=userinfo.getBIN_BrandInfoID() %>'>
<input type="hidden"  id="checkStockFlag" value='<s:property value='checkStockFlag'/>'>
<input type="hidden"  id="delQuantityLT" value='<s:property value='delQuantityLT'/>'>
<input type="hidden"  id="sysConfigUsePrice" value='<s:property value='sysConfigUsePrice'/>'>
<input type="hidden"  id="useCostPrice" value='<s:property value='useCostPrice'/>'>
<input type="hidden"  id="dateToday" value='<%= dateTime %>'>
<input type="hidden" id="configVal" name="configVal" value='<s:property value="configVal"/>'>
</div>
      <div class="panel-header">
        <div class="clearfix"> 
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
		</div>
      </div>
      <div id="actionResultDisplay"></div>
      	       <%-- ================== 错误信息提示 START ======================= --%>
          <div id="errorDiv2" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan2"></span></li>
		      </ul>			
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
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lblgeneral"/></strong></div>
              <div class="section-content">
              <table class="detail">
              	<tr>
              		<th><s:text name="lbldate"/></th>
              		<td><%= dateTime %></td>
              		<th><s:text name="lbloperator"/></th>
              		<td><%=userinfo.getEmployeeName() %></td>
              	</tr>
              	<tr>
              		<th><s:text name="lbldepartment"/></th>
              		<td>
                        <input type="hidden" name="outOrganizationId" id="outOrganizationId" value='${organizationId}'>
                        <span id="outOrgName" class="left"><s:property value='departInit'/></span>
                        <a class="add right" onclick="STSFH06_openSendDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="lblselect"/></span>
                        </a>
              		</td>
              		<th><s:text name="lblrecdepartment"/></th>
              		<td>
                        <input type="hidden" name="inOrganizationId" id="inOrganizationId">
                        <span id="inOrgName" class="left"></span>
                        <a class="add right" onclick="STSFH06_openReceiveDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="lblselect"/></span>
                        </a>
              		</td>
              	</tr>
              	<tr>
              		<th><s:text name="lblOutDepot"/></th>
              		<td>
              			<select style="width:200px;" name="outDepotId" id="outDepotId" disabled="true" onchange="STSFH06_clearDetailData();return false;">
                           <option value=""><s:text name="SFH06_Select"/></option>
	                  	<s:iterator value="outDepotList">
				         		<option value="<s:property value="BIN_InventoryInfoID" />">
				         			<s:property value="InventoryName"/>
				         		</option>
				      		</s:iterator>
	                  	</select>	
					</td>
					<%	List  logicInventoryList = (List)request.getAttribute("logicInventoryList");
	              	  	if(!logicInventoryList.isEmpty())
	              	  	{
	              	%>
	              		<th><s:text name="lblOutLoginDepot"/></th>
	              		<td>
	              			<s:select name="outLoginDepotId" list="logicInventoryList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" disabled="true" cssStyle="width:200px;" onchange="STSFH06_clearDetailData('flag');return false;"/>
	              		</td>
	              		
	              	<%	}else{%>
	              		<th></th>
	              		<td>
	              		</td>
	              	<%} %>
					
              	</tr>
              	<tr>
              		<th><s:text name="SFH06_deliverType"/></th>
              		<td><s:select name="deliverType" list='#application.CodeTable.getCodes("1168")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" cssStyle="width:150px;"/></td>
              		<th><s:text name="SFH06_PlanArriveDate"/></th>
              		<td>
                        <p class="date">
                            <span><s:textfield name="planArriveDate" cssClass="date" id="planArriveDate"/></span>
                        </p>
              		</td>
              	</tr>
              	<tr>
                    <th><s:text name="lblReasonAll"></s:text></th>
                    <td colspan=3><input class="text" type="text" name="reasonAll" id="reasonAll" maxlength="200" style="width:95%;"/></td>
               </tr>
              </table>
              </div>
            </div>        
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
		  <div class="section-content">
		  <div id="suggestBill_processing" class="dataTables_processing"  style="top: 50%;left: 50%;display: none;"></div>
          <div class="toolbar clearfix"><span class="left">
            <a class="add" onclick="BINOLSTSFH06.openProStockPopup(this);">
                <span class="ui-icon icon-search"></span>
                <span class="button-text"><s:text name="SFH06_ReceiveStock"/></span>
            </a>
             <a id="spanBtnadd" class="add" onclick="BINOLSTSFH06.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnSelectProduct"/></span></a> 
             <cherry:show domId="BINOLSTSFH0603">
             <a class="add" id="billCopy" onclick="BINOLSTSFH06.copyDeliver();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="billCopy"/></span></a>
             </cherry:show>
             <cherry:show domId="BINOLSTSFH0602">
              <a id="spanBtnStart" class="add"  onclick="BINOLSTSFH06.STSFH06_addblankbill();"> 
                <span class="ui-icon icon-add"></span>
               <span class="button-text" >
               <s:text name="blankbill"/></span>
               </a>
               <a id="spanBtnCancel" class="delete" style="display:none;" onclick="BINOLSTSFH06.canceProductResult(true);">
              <span class="ui-icon icon-delete"></span>
               <span class="button-text" >
               <s:text name="Cancelblankbill"/></span>
               </a>
               </cherry:show>
               <cherry:show domId="BINOLSTSFH0604">
                <a id="spanBtnSuggest" class="add"  onclick="BINOLSTSFH06.suggestBill();"> 
                    <span class="ui-icon icon-add"></span>
                    <span class="button-text" ><s:text name="SFH06_SuggestBill"/></span>
                </a> 
                <span id="spanCancelSuggest" style="display:none;border:1px solid #7D8791;padding:3px 1px 6px 1px;">
                <a id="spanBtnCancelSuggest" class="delete" style="display:none;" onclick="BINOLSTSFH06.canceProductResultSuggest(true);">
                    <span class="ui-icon icon-delete"></span>
                    <span class="button-text" >
                    <s:text name="SFH06_CancelSuggestbill"/></span>
               </a>
                <a id="spanBtnDelNoNeed" class="delete" style="display:none;" onclick="BINOLSTSFH06.delNoNeed();" title="<s:text name="SFH06_DelNoNeedTip"><s:param><s:property value='delQuantityLT'/></s:param></s:text>">
                    <span class="ui-icon icon-delete"></span>
                    <span class="button-text" >
                    <s:text name="SFH06_DelNoNeed"/></span>
               </a>
               </span>
               </cherry:show>
                <a id="spanBtnAddNewLine" class="add" onclick="BINOLSTSFH06.addNewLine();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnadd"/></span></a> 
                <a id="spanBtdelete" class="delete" onclick="STSFH06_deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="btndelete"/></span></a>
               <span class="bg_yew" ><span style="line-height:25px;" class="highlight">*<s:text name="lbldrag"/></span></span>
               </span>
                 <div id="inquiretab" class="hide right">   
                  	    <a class="add right" style="margin-top:3px;" onclick="inquireLblcodeBox(this);">
               			<span class="ui-icon icon-search"></span>
						<span class="button-text"> <s:text name="inquire"/></span>
						</a>
                  		<input class="text right" type="text" id="inquirelblcode"  />
               	 </div>
             </div>
   			 <div id="canceldetail">
            <div  style="width:100%;overflow-x:scroll;">
            <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
              <thead>
                <tr>
               	  <th class="tableheader" width="3%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#databody');"/>
             	  <s:text name="btnallselect"/></th>
                  <th class="tableheader" width="15%" onclick="STSFH06_sortTable('_unitCodeArr');">
                  <div class="DataTables_sort_wrapper"><s:text name="lblcode"/><span id="_unitCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                  <s:if test='"1".equals(configVal)'>
	                  <th class="tableheader" width="15%"><s:text name="erpCode"/></th>
                  </s:if>
                  <th class="tableheader" width="15%" onclick="STSFH06_sortTable('_barCodeArr');">
                  <div class="DataTables_sort_wrapper"><s:text name="lblbarcode"/><span id="_barCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                  <th class="tableheader" width="18%"><s:text name="lblproname"/></th>
                  <th class="tableheader" width="8%"><s:text name="SFH06_ReferencePrice"/></th>
                  <th class="tableheader" width="8%"><s:text name="SFH06_costPrice"/></th><%--成本价 --%>
                  <th class="tableheader" width="8%"><s:text name="SFH06_totalCostPrice"/></th><%--总成本价 --%>
                  <th class="tableheader" width="8%">
                    <s:text name="lblprice"/>
                    <span class="calculator" onclick="BINOLSTSFH06.initRateDiv(this,'batch');" title="<s:text name="SFH06_BatchCalTitle"/>"></span>
                     <input type="hidden" id="batchPriceRate" value="100.00">
                  </th>
                  <th class="tableheader" width="5%"><s:text name="lblNowCount"/></th>
                  <!-- 收货方库存 -->
                  <th class="tableheader" width="5%"><s:text name="SFH06_ReceiveStock"/></th>           
                  <th class="tableheader" width="10%" onclick="STSFH06_sortTable('_quantArr');">
                  <div class="DataTables_sort_wrapper"><s:text name="lbloutcount"/><span id="_quantArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span></div></th>
                  <th class="tableheader" width="10%"><div style="line-height:13px;"><s:text name="lblmoney"/><s:text name="lblmoneyu"/></div></th>
                  <th class="tableheader" width="20%"><s:text name="lblreason"/></th>
                  <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody">
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
                <td class="center"><s:text name="SFH06_totalQuantity"/></td><%-- 总数量 --%>
                <td class="center"><s:text name="SFH06_totalAmount"/></td><%-- 总金额--%>
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
            <div id="submitDiv" class="center clearfix">
            	<button class="save" type="button" onclick="STSFH06_save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="btnSave"/></span></button>
            	<cherry:show domId="BINOLSTSFH0601">
            	<button class="confirm" type="button" onclick="STSFH06_btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
                </cherry:show>
            </div>
            <div id="submit_processing" class="dataTables_processing"  style="top: 50%;left: 50%;display: none;"></div>
          </div>
        </div>
      </div>
      </s:else>
      <div class="hide" id="dialogInit"></div>
</cherry:form>
<div class="rateDialog hide">
    <span id="spanCalTitle" style="display:none;"><s:text name="SFH06_CalTitle"/></span>
    <s:text name="SFH06_discountRate"/><%-- 折扣率 --%>
    <input class="number" id="priceRate" value="100" onblur="BINOLSTSFH06.closeDialog(this);return false;"  
        onkeyup="BINOLSTSFH06.setDiscountPrice(this);return false;"/><s:text name="global.page.percent"/>
    <input type ="hidden" id="curRateIndex" value=""/>
</div>
<%-- ================== 弹出发货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出收货部门datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/popDepartTableBusinessType.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/st/common/BINOLSTCM12.jsp" flush="true" />
<%-- ================== 弹出datatable -- 促销品发货单共通START ======================= --%>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00040"/>'/>
	<input type="hidden" id="errmsg4" value='<s:text name="ESS00042"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
	<input type="hidden" id="notOnlyOneWarning" value='<s:text name="SFH06_notOnlyOne"/>'/>
	<input type="hidden" id="noOutDepart" value='<s:text name="SFH06_noOutDepart"/>'/>
	<input type="hidden" id="noOutDepot" value='<s:text name="SFH06_noOutDepot"/>'/>
	<input type="hidden" id="noinDepart" value='<s:text name="SFH06_noinDepart"/>'/>
	<input type="hidden" id="errmsg_EST00034" value='<s:text name="EST00034"/>'/>
	<input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</div>
</s:i18n>
<%-- ================== dataTable共通导�?START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导�?   END  ======================= --%>