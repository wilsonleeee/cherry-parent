<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM34.js"></script>
<s:i18n name="i18n.ss.BINOLSSPRM34">
<s:url id="saveUrl" action="BINOLSSPRM34_save"/>
<div class="main container clearfix">
      <div class="panel-content">
      	<div id="errorDiv" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan"></span></li>
		      </ul>			
          </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
          		<%-- 基本信息 --%>
          		<s:text name="PRM34_header"/>
          	</strong>
          </div>
          <div class="section-content">
          <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
            <table class="detail" cellpadding="0" cellspacing="0">
              <tr>
                <th>
                	<%-- 发货单号 --%>
                	<s:text name="PRM34_deliverRecNo"/>
                </th>
                <td>
                	<span><s:property value="deliverInfo.deliverRecNo"/></span>
                	<%-- 促销产品收发货ID --%>
		             <input type="hidden" name="deliverId" value="${deliverInfo.deliverId}"/>
		             <%-- 收发货单号 --%>
		             <input type="hidden" name="deliverRecNo" value="<s:property value='deliverInfo.deliverRecNo'/>"/>
		             <%-- 更新日时 --%>
		             <input type="hidden" name="deliverUpdateTime" value="${deliverInfo.deliverUpdateTime}"/>
		             <%-- 更新次数 --%>
		             <input type="hidden" name="modifyCount" value="${deliverInfo.modifyCount}"/>
		             <%-- 发货部门 --%>
		             <input type="hidden" name="deliverDepId" value="${deliverInfo.deliverDepId}"/>
                </td>
                <th>
                	<%-- 发货部门 --%>
                	<s:text name="PRM34_departName"/>
                </th>
                <td>
                	<span><s:property value="deliverInfo.departName"/></span>
                </td>
              </tr>
              <tr>
                <th>
                	<%-- 发货日期 --%>
                	<s:text name="PRM34_deliverDate"/>
                </th>
                <td>
                	<span><s:property value="deliverInfo.deliverDate"/></span>
                </td>
                <th>
                	<%-- 发货仓库 --%>
                	<s:text name="PRM34_inventName"/>
                </th>
                <td>
                	<s:select name="inventoryId" list="inventoryList" listKey="BIN_InventoryInfoID" listValue="InventoryName" value="deliverInfo.inventoryId" 
                			onchange="needSave();"></s:select>
                </td>
              </tr>
              <tr>
                <th>
                	<%-- 入库状态 --%>
                	<s:text name="PRM34_stockInFlag"/>
                </th>
                <td>
                	<%-- 入库状态 --%>
					<span><s:property value='#application.CodeTable.getVal("1017", deliverInfo.stockInFlag)'/></span>
                </td>
                <th>
                	<%-- 收货部门--%>
                	<s:text name="PRM34_departNameReceive"/>
                </th>
                <td>
                	<s:select name="receiveDepId" list="receiveDepList" listKey="BIN_OrganizationID" listValue="DepartName" value="deliverInfo.receiveDepId"
                			onchange="needSave();"></s:select>
                </td>
                
              </tr>
              <tr>
                <th>
                	<%-- 审核状态 --%>
                	<s:text name="PRM34_verifiedFlag"/>
                </th>
                <td>
                	<%-- 审核状态 --%>
					<span><s:property value='#application.CodeTable.getVal("1007", deliverInfo.verifiedFlag)'/></span>
                </td>
                <th>
                	<%-- 制单员 --%>
                	<s:text name="PRM34_employeeName"/>
                </th>
                <td><span><s:property value="deliverInfo.employeeName"/></span></td>
              </tr>
              <tr>
                <th>
                	<%-- 发货理由 --%>
                	<s:text name="PRM34_reason"/>
                </th>
                <td>
                	<s:textfield name="reason" cssClass="text" value="%{deliverInfo.reason}" maxlength="200" onchange="needSave();"></s:textfield>
                </td>
              </tr>
              </table>
            </cherry:form>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 发货明细一览 --%>
            <s:text name="PRM34_results_list"/>
            </strong>
          </div>
          <div class="section-content">
          	 <div class="toolbar clearfix"><span class="left">
             <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this, '#detailTableBody');"/>
             <s:text name="PRM34_btnallselect"/>
             <a class="add" onclick="javascript:addTableRow('#detailTableBody', '#newRowBody');return false;">
	             <span class="ui-icon icon-add"></span>
	             <span class="button-text"><s:text name="PRM34_btnadd"/></span>
             </a> 
             <a class="delete" onclick="javascript:delTableRow('#detailTableBody');return false;"><span class="ui-icon icon-delete"></span>
             		<span class="button-text"><s:text name="PRM34_btndelete"/></span>
             </a>
              	</span> 
              </div>
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
            <table id="detailTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th class="tableheader" width="3%"><s:text name="PRM34_lblselect"/></th>
                  <th class="tableheader" width="1%"><s:text name="PRM34_num"/></th><%-- No. --%>
                  <th class="tableheader" width="22%"><s:text name="PRM34_unitCode"/></th><%-- 厂商编码 --%>
                  <th class="tableheader" width="15%"><s:text name="PRM34_barCode"/></th><%-- 促销品条码 --%>
                  <th class="tableheader" width="15%"><s:text name="PRM34_nameTotal"/></th><%-- 促销品名称 --%>
                  <th class="tableheader" width="8%"><s:text name="PRM34_price"/><s:text name="PRM34_lblmoneyu"/></th><%-- 单价 --%>
                  <th class="tableheader" width="8%"><s:text name="PRM34_quantity"/></th><%-- 发货数量 --%>
                  <th class="tableheader" width="8%"><s:text name="PRM34_detailAmount"/><s:text name="PRM34_lblmoneyu"/></th><%-- 总金额 --%>
                  <th class="tableheader" width="20%"><s:text name="PRM34_reason"/></th><%-- 发货理由 --%>
                </tr>
              </thead>
              <tbody id="detailTableBody">
              <s:iterator value="deliverDetailList" id="deliverDetail" status="R">
                <tr class="<s:if test='#R.odd'>odd</s:if><s:else>even</s:else>">
                	<td class="hide">
                		<%-- 促销产品收发货明细ID --%>
	                  <input type="hidden" name="deliverDetailId" value="${deliverDetail.deliverDetailId}"/>
	                  <%-- 促销产品厂商ID --%>
	                  <input type="hidden" name="prtVendorId" value="${deliverDetail.prtVendorId}"/>
	                  <%-- 价格 --%>
	                  <input type="hidden" name="price" value="${deliverDetail.price}"/>
	                  <%-- 更新日时 --%>
	                  <input type="hidden" name="detailUpdateTime" value="${deliverDetail.detailUpdateTime}"/>
	                  <%-- 更新次数  --%>
	                  <input type="hidden" name="modifyCount" value="${deliverDetail.modifyCount}"/>
	                  <%-- 有效区分 --%>
             		  <input type="hidden" name="validFlag"/>
                	</td>
                	<td>
                	<input type="checkbox" name="deliverDetailId" value="${deliverDetail.deliverDetailId}" class="checkbox" onclick="changeAllSelect(this, '#detailTableBody');"/>
                	</td>
                  <td class=rowNum><s:property value="#R.index+1"/></td>
                  <td>
	                  <span class="unitCode left">
	                  <s:if test='unitCode != null && !"".equals(unitCode)'>
	                  	<s:property value="unitCode"/>
	                  </s:if>
	                  <s:else>
	                  	&nbsp;
	                  </s:else>
	                  </span>
	                  <a onclick="javascript:openPromDialog(this);return false;" class="popup right" href="javascript:void(0);">
	                  	<span class="ui-icon icon-search"></span>
	                  </a>
                  </td>
                  <td class="barCode">
                  <s:if test='barCode != null && !"".equals(barCode)'>
                  	<s:property value="barCode"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="nameTotal">
                  <s:if test='nameTotal != null && !"".equals(nameTotal)'>
                  	<s:property value="nameTotal"/>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td class="price" style="text-align:right;">
                  <s:if test='price != null'>
                  	<s:text name="format.price"><s:param value="price"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  	<s:textfield name="quantity" cssClass="text-number" maxlength="10" size="10" value="%{quantity}" onchange="changeAmount(this);"></s:textfield>
                  </td>
                  <td class="detailAmount" style="text-align:right;">
                  <s:if test='detailAmount != null'>
                  	<s:text name="format.price"><s:param value="detailAmount"></s:param></s:text>
                  </s:if>
                  <s:else>
                  	&nbsp;
                  </s:else>
                  </td>
                  <td>
                  	<s:select name="reason" list='#application.CodeTable.getCodes("1051")' listKey="CodeKey" listValue="Value" value="%{reason}" onchange="needSave();"/>
                  </td>
                </tr>
               </s:iterator>
              </tbody>
            </table>
            </div>
            <hr class="space" />
            <div class="center">
            	<cherry:show domId="SSPRM0734BSAVE">
           		<button class="save" type="button" onclick="saveDetail('${saveUrl}');">
           			<span class="ui-icon icon-save"></span>
           			<span class="button-text"><s:text name="PRM34_btnSave"/></span>
           		</button>
           		</cherry:show>
           		<cherry:show domId="SSPRM0734BSEND">
            	<button class="confirm" type="button" onclick="saveDetail('${saveUrl}','1');">
            		<span class="ui-icon icon-confirm"></span>
            		<span class="button-text"><s:text name="PRM34_btnOK"/></span>
            	</button>
            	</cherry:show>
              <button class="close" onclick="window.close();"><span class="ui-icon icon-close"></span>
              <%-- 关闭 --%>
              <span class="button-text"><s:text name="PRM34_close"/></span>
             </button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <table id="newRow" class="hide">
    	<tbody id="newRowBody">
    	<tr>
    		<td class="hide">
    			<%-- 促销产品厂商ID --%>
	             <input type="hidden" name="prtVendorId"/>
	             <%-- 价格 --%>
		         <input type="hidden" name="price"/>
    		</td>
           <td><input id="deliverDetailId" type="checkbox" class="checkbox" onclick="changeAllSelect(this, '#detailTableBody');"/></td>
           <td class="rowNum"></td>
           <td>
           	<span class="unitCode left">
              	&nbsp;
              </span>
              <a onclick="javascript:openPromDialog(this);return false;" class="popup right" href="javascript:void(0);">
              	<span class="ui-icon icon-search"></span>
              </a>
           </td>
           <td class="barCode">
           	&nbsp;
           </td>
           <td class="nameTotal">
           	&nbsp;
           </td>
           <td class="price" style="text-align:right;">
           	&nbsp;
           </td>
           <td>
           	<s:textfield name="quantity" cssClass="text-number" maxlength="10" size="10" onchange="changeAmount(this);"></s:textfield>
           </td>
           <td class="detailAmount" style="text-align:right;">
           	&nbsp;
           </td>
           <td>
           	<s:select name="reason" list='#application.CodeTable.getCodes("1051")' listKey="CodeKey" listValue="Value"/>
           </td>
         </tr>
         </tbody>
    </table>
    <div id="errmsg1" class="hide"><s:text name="ESS00007"/></div>
    <div id="errmsg2" class="hide"><s:text name="ESS00008"/></div>
    <%-- ================== 弹出datatable -- 促销产品共通导入 START ======================= --%>
	<%@ include file="/WEB-INF/jsp/common/prmProductTable.jsp" %>
	<%-- ================== 弹出datatable -- 促销产品共通导入 START ======================= --%>
</s:i18n>
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
	<%-- ================== dataTable共通导入    END  ======================= --%>
