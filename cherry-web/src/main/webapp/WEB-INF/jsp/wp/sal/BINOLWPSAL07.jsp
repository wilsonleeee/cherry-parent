<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL07.js?v=20160929"></script>
<s:i18n name="i18n.wp.BINOLWPSAL07">
<style>
#container { width:100%; padding:0; margin:2em auto; height:100%;display: table;}
#leftDiv { width:30%; text-align:left; line-height:200px; display:table-cell; vertical-align: top;}
#rightDiv { width:50%; text-align:right; line-height:200px; display:table-cell; vertical-align: top;}
#payTypeDiv {width: 60%;  text-align: right; display: inline-block;}
</style>
<div class="hide">
	<s:url id="s_dgSearchUrl" value="/wp/BINOLWPSAL07_search" />
	<a id="dgSearchUrl" href="${s_dgSearchUrl}"></a>
	<s:url id="s_dgReturnHistoryUrl" value="/wp/BINOLWPSAL07_returnHistory" />
	<a id="dgReturnHistoryUrl" href="${s_dgReturnHistoryUrl}"></a>
	<s:url id="s_dgReturnBillUrl" value="/wp/BINOLWPSAL07_returnBill" />
	<a id="dgReturnBillUrl" href="${s_dgReturnBillUrl}"></a>
	<s:url id="s_dgReturnsGoodsUrl" value="/wp/BINOLWPSAL07_returnsGoods" />
	<a id="dgReturnsGoodsUrl" href="${s_dgReturnsGoodsUrl}"></a>
	<s:url id="s_dgGetBillDetailUrl" value="/wp/BINOLWPSAL07_getBillDetail" />
	<a id="dgGetBillDetailUrl" href="${s_dgGetBillDetailUrl}"></a>
	<s:url id="s_dgGetSrBillDetailUrl" value="/wp/BINOLWPSAL07_getSrBillDetail" />
	<a id="dgGetSrBillDetailUrl" href="${s_dgGetSrBillDetailUrl}"></a>
	<s:url id="s_dgGetAllBillsUrl" value="/wp/BINOLWPSAL07_getAllBills" />
	<a id="dgGetAllBillsUrl" href="${s_dgGetAllBillsUrl}"></a>
	<!-- 储值卡服务信息 -->
	<s:url id="s_dgGetServiceBillDetailUrl" value="/wp/BINOLWPSAL07_getServiceBillDetail" />
	<a id="dgGetServiceBillDetailUrl" href="${s_dgGetServiceBillDetailUrl}"></a>
	<!-- 支付方式信息 -->
	<s:url id="s_dgGetPayTypeBillDetailUrl" value="/wp/BINOLWPSAL07_getPayTypeBillDetail" />
	<a id="dgGetPayTypeBillDetailUrl" href="${s_dgGetPayTypeBillDetailUrl}"></a>
</div>
<div id="getBillsPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 300px;">
    <div class="wpleft_header">
    <s:text name="wpsal07.selectAll" id="selectAll"/>
    <input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
    <input type="hidden" id="POSTuihuo" name="POSTuihuo" value="<s:property value='POSTuihuo'/>"/>
    <input type="hidden" id="originalSaleType" name="originalSaleType" value=""/>
    <form id="searchBillsForm" method="post" class="inline">
    	<input type="hidden" id="dgSrCounterCode" name="dgSrCounterCode" value="<s:property value='counterCode'/>"/>
   	    <div class="header_box">
			<s:text name="wpsal07.searchDate" />
			<span><s:textfield id="dgSaleDateStart" name="dgSaleDateStart" cssClass="date" cssStyle="width:80px;" value="%{nowDate}" onkeyup="BINOLWPSAL07.keyupStartDate(this);return false;"/></span>
			<s:text name="wpsal07.toDate" />
			<span><s:textfield id="dgSaleDateEnd" name="dgSaleDateEnd" cssClass="date" cssStyle="width:80px;" value="%{nowDate}" onkeyup="BINOLWPSAL07.keyupEndDate(this);return false;"/></span>
        </div>
   	    <div class="header_box">
			<s:text name="wpsal07.billsCode" />
			<input id="dgBillCode" name="dgBillCode" type="text" class="date" maxlength="30" style="width:180px;" value="" />
        </div>
   	   <div class="header_box">
			<s:text name="wpsal07.baName" />
			<s:select id="dgBaCode" name="dgBaCode" class="titleTools" list="baList" listKey="baCode" listValue="baName" cssStyle="width:80px;" headerKey="" headerValue="%{selectAll}"/>
        </div>
   	    <div class="header_box">
			<s:text name="wpsal07.member" />
			<input id="memberSearchStr" name="memberSearchStr" type="text" class="date" style="width:100px;" maxlength="20" value="" />
			<button id="btnMemberSearch" class="close" type="button" onclick="BINOLWPSAL07.search();return false;">
	    		<span class="ui-icon icon-search"></span>
				<span class="button-text"><s:text name="wpsal07.search"/></span>
			</button>
			<button id="btnHistorySrBill" class="close" type="button" onclick="BINOLWPSAL07.showHistorySrBill();return false;">
	    		<span class="ui-icon icon-search"></span>
				<span class="button-text"><s:text name="wpsal07.showHistorySrBill"/></span>
			</button>
        </div>
    </form>
    </div>
 <!-- 
 	<div>
    	<div>
	    	<span>
		    	<s:text name="wpsal07.getAllBillsTip1"/>
		    	<a id="btnGetAllBills" onclick="BINOLWPSAL07.getAllBills();return false;">
					<s:text name="wpsal07.getAllBills"/>
				</a>
				<s:text name="wpsal07.getAllBillsTip2"/>
			</span>
		</div>
    </div>
 --> 
    <div id="searchBillsDiv" class="wp_tablebox">
      <table id="searchBillsTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <thead>
          <tr>
            <th width="5%"><s:text name="wpsal07.rowNumber"/></th>
            <th width="20%"><s:text name="wpsal07.billsCode"/></th>
            <th width="15%"><s:text name="wpsal07.saleTime"/></th>
            <th width="15%"><s:text name="wpsal07.memberCode"/></th>
            <th width="10%"><s:text name="wpsal07.baName"/></th>
            <th width="8%"><s:text name="wpsal07.quantity"/></th>
            <th width="8%"><s:text name="wpsal07.payAmount"/></th>
            <th width="15%"><s:text name="wpsal07.act"/></th>
          </tr>
        </thead>
        <tbody id="searchBillsbody">
        </tbody>
      </table>
    </div>
    <div id="returnHistoryBillsDiv" class="wp_tablebox hide">
      <table id="returnHistoryBillsTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
        <thead>
          <tr>
            <th width="5%"><s:text name="wpsal07.rowNumber"/></th>
            <th width="20%"><s:text name="wpsal07.srBillsCode"/></th>
            <th width="20%"><s:text name="wpsal07.orgBillsCode"/></th>
            <th width="10%"><s:text name="wpsal07.saleTime"/></th>
            <th width="10%"><s:text name="wpsal07.memberCode"/></th>
            <th width="7%"><s:text name="wpsal07.baName"/></th>
            <th width="8%"><s:text name="wpsal07.srQuantity"/></th>
            <th width="10%"><s:text name="wpsal07.srPayAmount"/></th>
            <th width="10%"><s:text name="wpsal07.act"/></th>
          </tr>
        </thead>
        <tbody id="returnHistoryBillsbody">
        </tbody>
      </table>
    </div>
    <div class="bottom_butbox clearfix">
    	<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL07.cancel();return false;">
    		<span class="ui-icon icon-close"></span>
			<span class="button-text"><s:text name="wpsal07.cancel"/></span>
		</button>
    </div>
</div>
<!--============================== 单据明细页START =======================================-->
<div id="getBillDetailPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 300px;">
	<cherry:show domId="BINOLWPSAL07RBB">
		<div class="header_box">
		<s:text name="wpsal07_businessReturnDate"/>
	        <span><s:textfield id="businessReturnDate" name="businessReturnDate" cssClass="date titleTools" disabled="disabled" value="%{nowDate}" onkeyup="BINOLWPSAL07.keyupBusinessDate(this);return false;" cssStyle="width:150px"/></span>
	    </div>
	</cherry:show>
	<div id="getBillDetailDiv" class="wp_tablebox">
	  <form id="billInfoForm" method="post" class="inline">
		  <input type="hidden" id="dgCheckedBillCode" name="dgCheckedBillCode" value=""/>
		  <input type="hidden" id="dgCheckedBaCode" name="dgCheckedBaCode" value=""/>
		  <input type="hidden" id="dgCheckedBaName" name="dgCheckedBaName" value=""/>
		  <input type="hidden" id="dgCheckedMemberCode" name="dgCheckedMemberCode" value=""/>
		  <input type="hidden" id="dgCheckedMemberName" name="dgCheckedMemberName" value=""/>
		  <input type="hidden" id="dgCheckedMemberLevel" name="dgCheckedMemberLevel" value=""/>
		  <input type="hidden" id="dgCheckedCustomerType" name="dgCheckedCustomerType" value=""/>
		  <input type="hidden" id="dgCheckedSaleType" name="dgCheckedSaleType" value=""/>
		  <input type="hidden" id="dgCheckedBusinessDate" name="dgCheckedBusinessDate" value=""/>
		  <input type="hidden" id="dgCheckedBusinessTime" name="dgCheckedBusinessTime" value=""/>
		  <input type="hidden" id="dgCheckedTotalQuantity" name="dgCheckedTotalQuantity" value=""/>
		  <input type="hidden" id="dgCheckedTotalAmount" name="dgCheckedTotalAmount" value=""/>
		  <input type="hidden" id="dgCheckedTotalOriginalAmount" name="dgCheckedTotalOriginalAmount" value=""/>
		  <input type="hidden" id="dgCheckedRoundingAmount" name="dgCheckedRoundingAmount" value=""/>
		  <input type="hidden" id="dgCheckedCostPoint" name="dgCheckedCostPoint" value=""/>
		  <input type="hidden" id="dgCheckedCostPointAmount" name="dgCheckedCostPointAmount" value=""/>
		  <input type="hidden" id="billSrDetailStr" name="billSrDetailStr" value=""/>
	  </form>
	  <table id="getBillDetailTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_table">
	    <thead>
	      <tr>
	        <th width="12%"><s:text name="wpsal07.unitCode"/></th>
	        <th width="12%"><s:text name="wpsal07.barCode"/></th>
	        <th><s:text name="wpsal07.productName"/></th>
	        <th id="dgBillState" width="5%"><s:text name="wpsal07.billState"/></th>
	        <th id="dgOrgQuantity" width="5%"><s:text name="wpsal07.quantity"/></th>
	        <th width="6%"><s:text name="wpsal07.price"/></th>
	        <th width="6%"><s:text name="wpsal07.memberPrice"/></th>
	        <s:if test="isPlatinumPrice=='Y'.toString()">
		        <th width="6%"><s:text name="wpsal07.platinumPrice"/></th>
	        </s:if>
	        <th width="5%"><s:text name="wpsal07.discountRate"/></th>
	        <th width="6%"><s:text name="wpsal07.realPrice"/></th>
	        <th width="8%"><s:text name="wpsal07.amount"/></th>
	        <th id="dgAllowQuantity" width="6%"><s:text name="wpsal07.allowQuantity"/></th>
	        <th id="dgReturnsQuantity" width="6%"><s:text name="wpsal07.returnsQuantity"/></th>
	        <th id="dgReturnsAct" width="3%"><s:text name="wpsal07.act"/></th>
	      </tr>
	    </thead>
	    <tbody id="getBillDetailbody">
	    </tbody>
	  </table>
	</div>

	<div id="container">
    <div id="leftDiv">
	<div id="serviceDiv">
    	<table id="getServiceBillDetailTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail">
			<thead>
				 <tr align="center">
				    <th><s:text name="wpsal07_serviceName"/></th>
				    <th><s:text name="wpsal07_serviceQuantity"/></th>
				    <th id="status"><s:text name="wpsal07.billState"/></th>
				    <th id="allQuantity"><s:text name="wpsal07_returnQuantity"/></th>
				    <%-- <th class="hide" id="printQuantity" width="70px"><s:text name="wpsal07_quantity"/></th>
				    <th class="hide" id="dgServiceReturnsAct" width="35px"><s:text name="wpsal07.act"/></th> --%>
				 </tr>
				 </thead>
				 <tbody id="getServiceBillDetailbody"></tbody>
		</table>
    </div> 
    </div>
    <div id="rightDiv">
    <div id="payTypeDiv">
		<table id="getpayTypeDetailTable" width="100%" cellspacing="0" cellpadding="0" border="0" class="wp_detail">
	    	<thead>
	      	<tr>
	        	<th><s:text name="wpsal07_payType"/></th>
	        	<th><s:text name="wpsal07_payAmount"/></th>
	        	<th><s:text name="wpsal07_returnAmount"/></th>
	     	</tr>
	    	</thead>
	    	<tbody id="getPayTypeDetailbody"></tbody>
	  </table>
    </div>
    </div>
</div> 

	<div class="bottom_butbox clearfix">
		<button id="btnPrintBill" class="close" type="button" onclick="BINOLWPSAL07.printBill();return false;">
			<span class="ui-icon icon_print"></span>
			<span class="button-text"><s:text name="wpsal07.printBill" /></span>
		</button>
		<button id="btnReturnsBill" class="close hide" type="button" onclick="BINOLWPSAL07.returnsBillDialog();return false;">
			<span class="ui-icon icon_save"></span>
			<span class="button-text"><s:text name="wpsal07.returnsBill" /></span>
		</button>
		<button id="btnReturnGoods" class="close hide" type="button" onclick="BINOLWPSAL07.returnsGoods();return false;">
    		<span class="ui-icon icon-save"></span>
			<span class="button-text"><s:text name="wpsal07.returnsGoods"/></span>
		</button>
    	<button id="btnBack" class="close" type="button" onclick="BINOLWPSAL07.back();return false;">
    		<span class="ui-icon icon-back"></span>
			<span class="button-text"><s:text name="wpsal07.back"/></span>
		</button>
    </div>
</div>
	<div class="hide" id="TuiHuo_messageDialogTitle"><s:text name="wpsal07.messageDialogTitle"/></div>
	<div id="TuiHuo_messageDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
		<p id="TuiHuo_messageContent" class="message hide" style="margin:40px auto 30px;"><span id="TuiHuo_messageSpan"></span></p>
		<p class="center">
			<button id="TuiHuo_btnMessage" class="close" type="button">
	    		<span class="ui-icon icon-confirm"></span>
	            <span class="button-text"><s:text name="wpsal07_confirm"/></span>
			</button>
		</p>
	</div>
<!--============================== 单据明细页END =======================================-->
</s:i18n>
