<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import = "java.util.*" %>
<%@ page import="java.text.SimpleDateFormat" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM52.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	window.onbeforeunload = function(){
		if(OS_BILL_Jump_needUnlock){
			if (window.opener) {
				window.opener.unlockParentWindow();
			}
		}
	};		
	if (window.opener) {	
	  window.opener.lockParentWindow();	
	}	
} );

function ClosePopupForm(){
	var ob = window.opener.document.getElementById("currentUnitID");
	if(ob){
		if(ob.value=='TOP'){
			window.opener.refreshTaskList();
		}else if(ob.value=='BINOLSSPRM18'){
			window.opener.search();
		}
	}
	window.close();
}
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
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日期
String dateTime = dateFm.format(new java.util.Date());
%>
<div class="hide">
    <input type="hidden"  id="dateToday" value='<%= dateTime %>'>
</div>
<s:i18n name="i18n.ss.BINOLSSPRM52">
<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
    <%--防止有button的form在text框输入后按Enter键后自动submit --%>
    <button type="submit" onclick="return false;" class="hide"></button>
<div class="hide">
<s:url id="doaction_url" value="/ss/BINOLSSPRM52_doaction"/>
<a id="osdoactionUrl" href="${doaction_url}"></a>

<s:url id="url_saveURL" value="/ss/BINOLSSPRM52_SAVE" />
<span id ="s_saveURL">${url_saveURL}</span>

<s:url id="url_deleteURL" value="/ss/BINOLSSPRM52_DELETE" />
<span id ="s_deleteURL">${url_deleteURL}</span>

<s:url id="url_auditAgreeURL" value="/ss/BINOLSSPRM52_auditAgree" />
<span id ="s_auditAgreeURL">${url_auditAgreeURL}</span>

<s:url id="url_auditDisagreeURL" value="/ss/BINOLSSPRM52_auditDisagree" />
<span id ="s_auditDisagreeURL">${url_auditDisagreeURL}</span>

<s:url id="url_sendURL" value="/ss/BINOLSSPRM52_SEND" />
<span id ="s_sendURL">${url_sendURL}</span>

<s:url id="url_getStockCount" value="/ss/BINOLSSCM01_GETSTOCKCOUNT" />
<span id ="s_getStockCount" >${url_getStockCount}</span>

<div id="dialogConfirm"><s:text name="dialog_confirm" /></div>
<div id="dialogCancel"><s:text name="dialog_cancel" /></div> 
</div>
<div class="panel ui-corner-all">
	<div class="panel-header">
	  <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
<!--	  <s:text name="lbltitleB"/> &gt; -->
	    <s:if test='"1".equals(operateType)'>
            <s:text name="lbltitle0"/>
        </s:if>
        <s:if test='"2".equals(operateType) || "42".equals(operateType)'>
            <s:text name="lbltitle1"/>
        </s:if>
        <s:if test='"41".equals(operateType)'>
            <s:text name="lbltitle2"/>
        </s:if>
        <s:if test='"50".equals(operateType)'>
            <s:text name="lbltitle3"/>
        </s:if>
        (<s:text name="PRM52_sequenceNo"/>:<s:property value="returnInfo.DeliverReceiveNo"/>)
	  </span> 
	  </div>
	</div>
    <div class="tabs">
		<ul>
            <li><a href="#tabs-1"><s:text name="global.page.title"/></a></li>
	        <li>
	           <a href="#tabs-2" onclick="clickTab_2WorkFlow();return false;"><s:text name="global.page.work"/></a>
            </li>
        </ul>
	<div id="tabs-1" class="panel-content">
 	<div id="actionResultDisplay"></div>
      <div id="errorDiv2" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan2"></span></li>
		      </ul>			
      </div>
		<s:if test="hasActionErrors()">
			<div class="actionError" id="actionResultDiv">
			  	<s:actionerror/>			  
			</div>
			<div style="height:20px"></div>
		</s:if>
		<s:else>
      <div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="global.page.title"/>
          	</strong>
          </div>
         
          <div class="section-content">
            <div class="box-header"></div>
            <table class="detail">
                <tr>
                    <%-- 发货单号 --%>
                    <th><s:text name="PRM52_allocationNo"/></th>
                    <td><s:property value="returnInfo.DeliverReceiveNoIF"/></td>
                    <%-- 发货日期 --%>
                    <th><s:text name="PRM52_date"/></th>
                    <td><s:property value="returnInfo.DeliverDate"/></td>
                </tr>
                <tr>
                    <%-- 关联单号 --%>
                    <th><s:text name="PRM52_relevanceNo"/></th>
                    <td><s:property value="returnInfo.RelevanceNo"/></td>
                     <%-- 制单员 --%>
                    <th><s:text name="PRM52_employeeName"/></th>
                   <td><span><s:property value="returnInfo.EmployeeName"/></span></td>
                </tr>
                <tr>
                    <%-- 预计到货日期 --%>
                    <th><s:text name="PRM52_planArriveDate"/></th>
                    <td>
                        <s:if test='"2".equals(operateType) || "41".equals(operateType)|| "42".equals(operateType)'>
                            <s:textfield id="planArriveDate" cssStyle="width:80px;" name="planArriveDate" value="%{returnInfo.PlanArriveDate}" cssClass="date"/>
                        </s:if>
                        <s:else>
                            <s:property value="returnInfo.PlanArriveDate"/>
                        </s:else>
                    </td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                <%-- 发货理由 --%>
                    <th><s:text name="PRM52_reason"/></th>
                    <td colspan=3>
                        <s:if test='"2".equals(operateType) || "41".equals(operateType)|| "42".equals(operateType)'>
                            <s:select name="reasonAll" list='#application.CodeTable.getCodes("1051")' listKey="CodeKey" listValue="Value"/>
                             <input type="hidden" value="<s:property value='returnInfo.Reason'/>" id="hidReasonAll">
                        </s:if>
                        <s:else>
                            <s:property value='#application.CodeTable.getVal("1051", returnInfo.Reason)'/>
                        </s:else>
                    </td>
                </tr>
            </table>
            <table class="detail">
                <tr>
                    <%-- 发货部门 --%>
                    <th><s:text name="PRM52_sendOrg"/></th>
                    <td><s:property value="returnInfo.DepartCodeName"/></td>
                    <%-- 发货仓库 --%>
                    <th><s:text name="lblInDepart"/></th>
                    <td><s:property value="returnList.get(0).inventoryName"/></td>
                </tr>
                <s:if test="null != returnList.get(0).BIN_LogicInventoryInfoID && returnList.get(0).BIN_LogicInventoryInfoID != 0">
                <tr>
                    <%-- 发货逻辑仓库 --%>
                    <th><s:text name="PRM52_logicInventoryName"/></th>
                    <td><s:property value="returnList.get(0).logicInventoryName"/><input type="hidden" name="outLoginDepotId" value="<s:property value="returnList.get(0).BIN_LogicInventoryInfoID"/>"/></td>
                    <th></th>
                    <td></td>
                </tr>
                </s:if>
                <tr>
                    <%-- 收货部门 --%>
                    <th><s:text name="PRM52_receiveOrg"/></th>
                    <td>
	                   <s:property value="returnInfo.DepartReceiveCodeName"/>
                    </td>
                    <%-- 收货仓库 --%>
                    <s:if test='"50".equals(operateType)'>
	                    <th><s:text name="PRM52_inDepot"/></th>
	                    <td>
	                       <select style="width:200px;" name="inDepotId" id="inDepotId" >
	                           <s:iterator value="inDepotList">
	                               <option value="<s:property value="BIN_InventoryInfoID" />">
	                                   <s:property value="InventoryName"/>
	                                   </option>
	                           </s:iterator>
	                       </select>
	                    </td>
                    </s:if>
                    <s:else>
                        <th></th>
                        <td></td>
                    </s:else>
                </tr>
                <s:if test='"50".equals(operateType)&& inLoginDepotList.size() != 0'>
                <tr>
                    <%-- 收货逻辑仓库 --%>
                    <th><s:text name="PRM52_inLogicDepot"/></th>
                    <td>
	                   <select style="width:200px;" name="inLogicDepotId" id="inLogicDepotId" >
                           <s:iterator value="inLoginDepotList">
                               <option value="<s:property value="BIN_LogicInventoryInfoID" />">
                                   <s:property value="LogicInventoryCodeName"/>
                                   </option>
                           </s:iterator>
                       </select>
                    </td>
                    <th></th>
                    <td></td>
                </tr>
                </s:if>
                <tr>
                    <%-- 审核状态 --%>
                    <th><s:text name="PRM52_verifiedFlag"/></th>
                    <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.VerifiedFlag)'/></td>
                     <%-- 入库状态 --%>
                    <th><s:text name="PRM52_stockInFlag"/></th>
                    <td><span><s:property value='#application.CodeTable.getVal("1017", returnInfo.StockInFlag)'/></span></td>
                </tr>
            </table>
            <div class="clearfix"></div>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 发货明细一览 --%>
            <s:text name="PRM52_results_list"/>
            </strong>
          </div>
          <s:if test='"2".equals(operateType) || "41".equals(operateType)|| "42".equals(operateType)'>
		  		<div class="toolbar clearfix"><span class="left">
	             <input id="allSelect" type="checkbox"  class="checkbox" onclick="SSPRM52_selectAll()"/>
	             <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
	             <s:text name="btnallselect"/><a class="add" onclick="SSPRM52_openPromotionSearchBox(this);"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnadd"/></span></a> <a class="delete" onclick="SSPRM52_deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="btndelete"/></span></a></span></div>
		  </s:if>
          <div class="section-content">          	 
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
             
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
                   <s:if test='"2".equals(operateType) || "41".equals(operateType)|| "42".equals(operateType)'>
				           <th class="tableheader" width="10px"><s:text name="lblselect"/></th>
				   </s:if>
				   <s:else>
	                        <th class="tableheader" width="10px">No.</th>
	               </s:else>		                                 
                  <th class="tableheader" width="150px"><s:text name="lblCode"/></th>
                  <th class="tableheader" width="120px"><s:text name="lblBarcode"/></th>
                  <th class="tableheader" width="150px"><s:text name="lblProname"/></th>
                  <th class="tableheader" ><s:text name="lblPrice"/></th>
                   <s:if test='"2".equals(operateType) || "41".equals(operateType)|| "42".equals(operateType)'>
				         <th class="tableheader" width="5%"><s:text name="lblNowCount"/></th> 
				   </s:if>
                  <th class="tableheader" ><s:text name="lblCount"/></th>               
                  <th class="tableheader" width="80px"><s:text name="lblMoney"/></th>
                  <th class="tableheader" ><s:text name="lblReason"/></th>
                  <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody">
              <%--
              <tr id="dataRow0" style="display:none">
                  <td class="center" id="dataTd0"><input id="chkbox" type="checkbox" value="0" onclick="SSPRM52_changechkbox(this)"/></td>
                  <td id="dataTd1" style="white-space:nowrap"></td>
                  <td id="dataTd2"></td>
                  <td id="dataTd3"></td>
                  <td id="dataTd4" style="text-align:right;"></td>
                  <td id="nowCount" style="text-align:right;"></td> 
                  <td id="dataTd7" style="text-align: right;"><s:textfield name="quantityuArr" id="quantityuArr" cssClass="text-number" size="10" maxlength="9" onchange="SSPRM52_changeCount(this)"/></td>
                  <td id="money" style="text-align:right;"></td>
                  <td id="dataTd10"><s:textfield name="reasonArr" size="25" maxlength="200"/></td>
                  <td style="display:none" id="dataTd11">
	                  <input type="hidden" id="priceUnitArr" name="priceUnitArr"/>	                 
	                  <input type="hidden" id="priceTotalArr" name="priceTotalArr"/>
	                  <input type="hidden" id="promotionProductVendorIDArr" name="promotionProductVendorIDArr"/>
                  </td>
                </tr> 
                --%>
                <% int i =1; %>
                <s:iterator value="returnList">
                	<tr id="dataRow<%= i %>">
                	<s:if test='"2".equals(operateType) || "41".equals(operateType)|| "42".equals(operateType)'>
				  		 <td class="center" id="dataTd0"><input id="chkbox" type="checkbox" value="<%= i %>" onclick="SSPRM52_changechkbox(this)"/></td>
					     <td id="dataTd1" style="white-space:nowrap"><s:property value="UnitCode"/></td>
	                  	 <td id="dataTd2"><s:property value="BarCode"/></td>
	                  	 <td id="dataTd3"><s:property value="NameTotal"/></td>	                  	 
	                  	 <td id="dataTd4" style="text-align:right;"><s:text name="format.price"><s:param value="Price"></s:param></s:text></td>
		                 <td id="nowCount" style="text-align:right;"><s:property value="NowCount"/></td>
		                 <td id="dataTd7" style="text-align:right;"><input type="text" name="quantityuArr" id="quantityuArr" class="text-number" size="10" maxlength="9" onchange="SSPRM52_changeCount(this)" value='<s:property value='Quantity'/>'></td>
		                 <td id="money<%= i %>" style="text-align:right;"><s:text name="format.price"><s:param value="Price * Quantity"></s:param></s:text></td>
		                 <td id="dataTd10"><input type="text" name="reasonArr" size="25" maxlength="200" value="<s:property value='Reason'/>"></td>
				    </s:if>
				    <s:else>
				    	<td class="center" id="dataTd0"><%= i %></td>
				    	 <td id="dataTd1" style="white-space:nowrap"><s:property value="UnitCode"/></td>
	                  	 <td id="dataTd2"><s:property value="BarCode"/></td>
	                  	 <td id="dataTd3"><s:property value="NameTotal"/></td>
	                  	 <td id="dataTd4" style="text-align:right;"><s:text name="format.price"><s:param value="Price"></s:param></s:text></td>
		                 <td id="dataTd7" style="text-align:right;"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></td>
		                 <td id="money<%= i %>" style="text-align:right;"><s:text name="format.price"><s:param value="Price * Quantity"></s:param></s:text></td>
		                 <td id="dataTd10"><s:property value='Reason'/></td>
				    </s:else>                  	 
	                 <td style="display:none" id="dataTd11">
	                  <input type="hidden" id="priceUnitArr<%= i %>" name="priceUnitArr" value="<s:property value='Price'/>"/>	                 
	                  <input type="hidden" id="priceTotalArr<%= i %>" name="priceTotalArr"/>
	                  <input type="hidden" id="nowCountArr" name="nowCountArr" value="<s:property value='NowCount'/>"/>
	                  <input type="hidden" id="promotionProductVendorIDArr<%= i %>" name="promotionProductVendorIDArr" value="<s:property value='BIN_PromotionProductVendorID'/>"/>
                  	  <input type="hidden" name="prmVendorId" value="<s:property value='BIN_PromotionProductVendorID'/>"/>
                  	</td>
	                </tr>
	                <%  i++; %>
                </s:iterator>
              </tbody>
            </table>           
           	</div>
           		<hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	              <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                <td class="center"><s:text name="PRM52_totalQuantity"/></td><%-- 总数量 --%>
	                <td class="center"><s:text name="PRM52_totalAmount"/></td><%-- 总金额--%>
	              </tr>
	              <tr>
	                <td id="totalQuantity" class="center"><s:text name="format.number"><s:param value="returnInfo.TotalQuantity"></s:param></s:text></td>
	             	<td id="totalAmount" class="center"><s:text name="format.price"><s:param value="returnInfo.TotalAmount"></s:param></s:text></td>
	              </tr>
	            </table>
		   <div style="display:none">
		    <input type="hidden" id="rowNumber" value="<%= i %>"/>
		    <input type="hidden" value="" name="csrftoken" id="csrftoken">
		    <input type="hidden" value="" name="operationFlag" id="operationFlag">
		    <input type="hidden" value="<s:property value="taskInstanceID"/>" name="taskInstanceID" id="taskInstanceID">
		     <input type="hidden" value="<s:property value="returnList.get(0).BIN_InventoryInfoID"/>" name="outDepotId" id="outDepotId">
		    <input type="hidden" value="<s:property value="returnInfo.UpdateTime"/>" name="updateTime" id="updateTime">
		    <input type="hidden" value="<s:property value="returnInfo.ModifyCount"/>" name="modifyCount" id="modifyCount">
		    <input type="hidden" value="<s:property value="returnInfo.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
		    <input type="hidden" value="<s:property value="returnInfo.StockInFlag"/>" name="stockInFlag" id="stockInFlag">
		    <input type="hidden" value="<s:property value="returnInfo.BIN_OrganizationID"/>" id="outOrganizationID" name="outOrganizationID">
		    <input type="hidden" value="<s:property value="returnInfo.BIN_OrganizationIDReceive"/>" id="inOrganizationID" name="inOrganizationID">
		     <input type="hidden" value="<s:property value="returnInfo.DeliverReceiveNoIF"/>" id="deliverReceiveNO" name="deliverReceiveNOIF">
		    <input type="hidden" value="<s:property value="returnInfo.BIN_PromotionDeliverID"/>" id="deliverId" name="deliverId">
		    <input type="hidden" value="<s:property value="returnInfo.EmployeeName"/>" id="employeeName" name="employeeName">
		    <s:if test="null != returnList.get(0).BIN_LogicInventoryInfoID && returnList.get(0).BIN_LogicInventoryInfoID != 0">
                <input type="hidden" id="outLogicInventoryInfoID" name="outLogicInventoryInfoID" value="<s:property value="returnList.get(0).BIN_LogicInventoryInfoID"/>"/>
		    </s:if>
            <s:else>
                <input type="hidden" id="outLogicInventoryInfoID" name="outLogicInventoryInfoID" value="0"/>
            </s:else>
            <input type="hidden" value="<s:property value="checkStockFlag"/>" id="checkStockFlag" name="checkStockFlag">
            <input type="hidden" value="<s:property value="lockSection"/>" id="lockSection" name="lockSection">
		   </div>
          </div>
        </div>
      </div>
      </s:else>
      <hr class="space" />
       <div class="center">
            <input type="hidden" id="entryID" name="entryID" value='<s:property value="workFlowID"/>'/>
		    <s:if test='"2".equals(operateType)'>
				<%-- 保存--%>
				<button type="button" class="save" onclick="SSPRM52_btnSaveClick()" id="btnSave" ><span class="ui-icon icon-save"></span>              
				<span class="button-text"><s:text name="btnSave"/></span>
				</button> 
				<%-- 发货 --%>
				<button class="confirm" type="button" onclick="SSPRM52_btnSendClick()" id="btnSend"><span class="ui-icon icon-confirm"></span>
				<span class="button-text"><s:text name="btnSend"/></span>
				</button>            
				<%-- 删除 --%>
				<button type="button"  class="close" onclick="SSPRM52_btnDeleteClick();" id="btnDelete"><span class="ui-icon icon-close"></span>
				<span class="button-text"><s:text name="btnDelete"/></span>
				</button>
		    </s:if>
		    <s:elseif test='"41".equals(operateType) || "42".equals(operateType)|| "50".equals(operateType)'>
				<jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" /> 
            </s:elseif>
              <%-- 关闭 --%>
             <button type="button" class="close" onclick="ClosePopupForm();"><span class="ui-icon icon-close"></span>             
              <span class="button-text"><s:text name="global.page.close"/></span>
             </button>
            <hr class="space">
       </div>
       </div>
        <div id="tabs-2">
        <strong><s:text name="global.page.worksProcessing"/></strong>
        </div>
    </div>
    </div>
    
    <div class="hide" id="dialogInit"></div>
    	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");
		var verifiedFlag = $('#verifiedFlag').val();
		$('#reasonAll').val($('#hidReasonAll').val());
		$("#csrftoken").val(window.opener.document.getElementById("csrftoken").value);
	</script> 
	</cherry:form>
<%-- ================== 弹出datatable -- 促销产品共通导�?START ======================= --%>
<%--<%@ include file="/WEB-INF/jsp/common/prmProductTable.jsp" %>--%>
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00025"/>'/>
	<input type="hidden" id="errmsg4" value='<p class="message"><span><s:text name="ESS00040"/></span></p>'/>
    <input type="hidden" id="errmsg_EST00034" value='<s:text name="EST00034"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>