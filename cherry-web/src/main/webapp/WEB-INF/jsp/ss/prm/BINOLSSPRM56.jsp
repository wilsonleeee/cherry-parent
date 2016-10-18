<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM56.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
function SSPRM56_agree(){
	cherryAjaxRequest({
		url:$("#s_agree").html(),
		param:$("#mainForm").formSerialize(),
		callback:SSPRM56_AuditSuccess
	});
}

function SSPRM56_disagree(){
	cherryAjaxRequest({
		url:$("#s_disagree").html(),
		param:$("#mainForm").formSerialize(),
		callback:SSPRM56_AuditSuccess
	});
}

function SSPRM56_AuditSuccess(msg){
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$("#btnAgree").hide();
		$("#btnDisagree").hide();
		//$("#buttonarea2").show();
	}
}

/**
 * 点击保存按钮
 */
function SSPRM56_submitFormSave(){	
	SSPRM56_clearActionMsg();
	if(!SSPRM56_checkData()){
		return false;
	}

	cherryAjaxRequest({
		url:$("#s_saveURL").html(),
		param:$('#mainForm').formSerialize(),
		callback:SSPRM56_SaveFinished
	});	
}

/**
 * 点击保存按钮
 */
function SSPRM56_submitFormSend(){	
	SSPRM56_clearActionMsg();
	if(!SSPRM56_checkData()){
		return false;
	}

	cherryAjaxRequest({
		url:$("#s_sendURL").html(),
		param:$('#mainForm').formSerialize(),
		callback:SSPRM56_SaveFinished
	});	
}
/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM56_SaveFinished(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$('#btnSave').hide();
		$('#btnSend').hide();
		$('#btnDelete').hide();
	}
	if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
}
/**
 * 点击删除按钮
 */
function SSPRM56_submitFormDelete(){	
	SSPRM56_clearActionMsg();
	cherryAjaxRequest({
		url:$("#s_deleteURL").html(),
		param:$('#mainForm').formSerialize(),
		callback:SSPRM56_submitSuccessSave
	});	
}
/**
 * 提交成功的回调函数
 * @param msg
 */
function SSPRM56_submitSuccessSave(msg){
	submitflag=true;
	if($('#actionResultDiv').attr('class')=='actionSuccess'){
		$('#btnSave').hide();
		$('#btnSend').hide();
		$('#btnDelete').hide();
	}
	if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
}

function ClosePopupForm(){
	var ob = window.opener.document.getElementById("currentUnitID");
	if(ob){
		if(ob.value=='TOP'){
			window.opener.refreshTaskList();
		}else if(ob.value=='BINOLSSPRM55'){
			window.opener.research();
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
<s:i18n name="i18n.ss.BINOLSSPRM56">

<cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
    <%--防止有button的form在text框输入后按Enter键后自动submit --%>
    <button type="submit" onclick="return false;" class="hide"></button>
<input type="hidden" id="entryID" name="entryID" value='<s:property value="returnInfo.WorkFlowID"/>'/>
<div class="hide">
<s:url id="doaction_url" value="/ss/BINOLSSPRM56_doaction"/>
<a id="osdoactionUrl" href="${doaction_url}"></a>
</div>

<s:url id="url_agree" value="/ss/BINOLSSPRM56_AGREE" />
<span id ="s_agree" style="display:none">${url_agree}</span>

<s:url id="url_disagree" value="/ss/BINOLSSPRM56_DISAGREE" />
<span id ="s_disagree" style="display:none">${url_disagree}</span>

<s:url id="url_saveURL" value="/ss/BINOLSSPRM56_SAVE" />
<span id ="s_saveURL" style="display:none">${url_saveURL}</span>

<s:url id="url_sendURL" value="/ss/BINOLSSPRM56_SEND" />
<span id ="s_sendURL" style="display:none">${url_sendURL}</span>

<s:url id="url_deleteURL" value="/ss/BINOLSSPRM56_DELETE" />
<span id ="s_deleteURL" style="display:none">${url_deleteURL}</span>

<div class="main container clearfix">
<div class="panel ui-corner-all">
	<div class="panel-header">
	  <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
<!--	  <s:text name="lbltitle"/> &gt; -->
	   <s:if test='"1".equals(operateType)'>
	  		<s:text name="lbltitle0"/>(<s:text name="PRM56_sequenceNo"/>:<s:property value="returnInfo.AllocationNo"/>)
	  </s:if>
	   <s:if test='"2".equals(operateType) || "72".equals(operateType)'>
            <s:text name="lbltitle1"/>(<s:text name="PRM56_sequenceNo"/>:<s:property value="returnInfo.AllocationNo"/>)
        </s:if>
        <s:if test='"71".equals(operateType)'>
            <s:text name="lbltitle2"/>(<s:text name="PRM56_sequenceNo"/>:<s:property value="returnInfo.AllocationNo"/>)
        </s:if> 
        <s:if test='"80".equals(operateType)'>
            <s:text name="lbltitle4"/>(<s:text name="PRM56_sequenceNo"/>:<s:property value="returnInfo.AllocationNo"/>)
        </s:if> 
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

   <!-- 将画面上的div 设置id="tabs-1" 此div一般是指class="panel-content"的div，如： -->
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
            <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                    <%-- 调拨单号 --%>
                    <th><s:text name="PRM56_allocationNo"/></th>
                    <td><s:property value="returnInfo.AllocationNoIF"/></td>
                    <%-- 申请日期 --%>
                    <th><s:text name="PRM56_date"/></th>
                    <td><s:property value="returnInfo.AllocationDate"/></td>
                </tr>
                <tr>
                    <%-- 关联单号 --%>
                    <th><s:text name="PRM56_relevanceNo"/></th>
                    <td><s:property value="returnInfo.RelevanceNo"/></td>
                    <th></th>
                    <td></td>
                </tr>
                <tr>
                    <%-- 调拨理由 --%>
                    <th><s:text name="PRM56_reason"/></th>
                    <td colspan=3>
                        <s:if test='"2".equals(operateType) || "72".equals(operateType)'>
                            <textarea name="reasonAll" id="reasonAll" maxlength="100" onkeyup="return isMaxLen(this)" rows="1" style="width:95%;height:30px;overflow:hidden"><s:property value="returnInfo.Reason"/></textarea>
                        </s:if>
                        <s:else>
                            <s:property value="returnInfo.Reason"/>
                        </s:else>
                    </td>
                </tr>
            </table>
            <table class="detail" cellpadding="0" cellspacing="0">
                <tr>
                    <%-- 申请部门 --%>
                    <th><s:text name="PRM56_sendOrg"/></th>
                    <td><s:property value="returnInfo.DepartCodeName"/></td>
                    <%-- 调入仓库 --%>
                    <th><s:text name="lblInDepart"/></th>
                    <td>
                        <s:if test='"2".equals(operateType) || "72".equals(operateType)'>
                            <s:select id="inDepotId" name="inDepotId" list="inDepotList" value="%{returnList.get(0).BIN_InventoryInfoID}" 
                                listKey="BIN_InventoryInfoID" listValue="InventoryName" headerKey="" cssStyle="width:200px;">
                            </s:select>
                        </s:if>
                        <s:else>
                            <s:property value="returnList.get(0).inventoryName"/>
                            <input type="hidden" value="<s:property value="returnList.get(0).BIN_InventoryInfoID"/>" id="inDepotId" name="inDepotId">
                        </s:else> 
                    </td>
                </tr>
                <tr>
                    <s:if test='"2".equals(operateType) || "72".equals(operateType)'>
                        <s:if test='null != inLogicList && inLogicList.size()>0'>
                            <%--调入逻辑仓库 --%>
                            <th><s:text name="PRM56_inLogicDepot"/></th>
                            <td>
                                <s:select id="inLogicId" name="inLogicId" list="inLogicList" value="%{returnList.get(0).BIN_LogicInventoryInfoID}" 
                                    listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;">
                                </s:select>
                            </td>
                            <%-- 调出部门 --%>
                            <th><s:text name="PRM56_receiveOrg"/></th>
                            <td><s:property value="returnInfo.DepartReceiveCodeName"/></td>
                        </s:if>
                        <s:else>
                            <%-- 调出部门 --%>
                            <th><s:text name="PRM56_receiveOrg"/></th>
                            <td><s:property value="returnInfo.DepartReceiveCodeName"/></td>
                            <th></th>
                            <td></td>
                        </s:else>
                    </s:if>
                    <s:else>
                        <%--调入逻辑仓库 --%>
                        <th><s:text name="PRM56_inLogicDepot"/></th>
                        <td>
                            <s:property value="returnList.get(0).LogicInventoryCodeName"/>
                            <input type="hidden" value="<s:property value="returnList.get(0).BIN_LogicInventoryInfoID"/>" id="inLogicId" name="inLogicId">
                        </td>
                        <%-- 调出部门 --%>
                        <th><s:text name="PRM56_receiveOrg"/></th>
                        <td><s:property value="returnInfo.DepartReceiveCodeName"/></td>
                    </s:else>
                </tr>
                <s:if test='"80".equals(operateType)'>
                    <tr>
                        <%-- 调出仓库 --%>
                        <th><s:text name="PRM56_outDepot"/></th>
                        <td>
                            <select style="width:200px;" name="outDepotId" id="outDepotId" >
                                <s:iterator value="outDepotList">
                                    <option value="<s:property value="BIN_InventoryInfoID" />">
                                        <s:property value="InventoryName"/>
                                    </option>
                                </s:iterator>
                            </select>
                        </td>
                        <s:if test='null != outLogicList && outLogicList.size()>0'>
                            <%-- 调出逻辑仓库 --%>
                            <th><s:text name="PRM56_outLogicDepot"/></th>
                            <td>
                                <select style="width:200px;" name="outLogicId" id="outLogicId" >
                                    <s:iterator value="outLogicList">
                                        <option value="<s:property value="BIN_LogicInventoryInfoID" />">
                                            <s:property value="LogicInventoryCodeName"/>
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
                </s:if>
            </table>
            <s:if test='!"80".equals(operateType)'>
                <table class="detail" cellpadding="0" cellspacing="0">
                    <tr>
                        <%-- 审核状态 --%>
                        <th><s:text name="PRM56_verifiedFlag"/></th>
                        <td><s:property value='#application.CodeTable.getVal("1007", returnInfo.VerifiedFlag)'/></td>
                        <th></th>
                        <td></td>
                    </tr>
                </table>
            </s:if>
            <div class="clearfix"></div>
          </div>
        </div>
        <div class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-search-result"></span>
          	<%-- 调拨明细一览 --%>
            <s:text name="PRM56_results_list"/>
            </strong>
          </div>
          <s:if test='"2".equals(operateType) || "72".equals(operateType)|| "71".equals(operateType)'>
	  		<div class="toolbar clearfix"><span class="left">
             <input id="allSelect" type="checkbox"  class="checkbox" onclick="SSPRM56_selectAll()"/>
             <input id="BIN_BrandInfoID" type="hidden" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'></input>
             <s:text name="btnallselect"/><a class="add" onclick="SSPRM56_openPromotionSearchBox(this);"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnadd"/></span></a> <a class="delete" onclick="SSPRM56_deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="btndelete"/></span></a></span></div>
		   </s:if>          
          <div class="section-content">          	 
            <div id="table_scroll" style="overflow-x:auto;overflow-y:hidden">
             
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
	                <s:if test='"2".equals(operateType) || "72".equals(operateType)|| "71".equals(operateType)'>
				  		<th class="tableheader" width="10px"><s:text name="lblselect"/></th>
				   </s:if>
				   <s:else>
				    	<th class="tableheader" width="10px">No.</th>
				   </s:else>                  
                  <th class="tableheader" width="150px"><s:text name="lblCode"/></th>
                  <th class="tableheader" width="120px"><s:text name="lblBarcode"/></th>
                  <th class="tableheader" width="150px"><s:text name="lblProname"/></th>
                  <th class="tableheader" ><s:text name="lblPrice"/></th>
                  <th class="tableheader" ><s:text name="lblCount"/></th>               
                  <th class="tableheader" width="80px"><s:text name="lblMoney"/></th>
                  <th class="tableheader" ><s:text name="lblReason"/></th>
                  <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody">
              <%--
              <tr id="dataRow0" style="display:none">
                  <td class="center" id="dataTd0"><input id="chkbox" type="checkbox" value="0" onclick="SSPRM56_changechkbox(this)"/></td>
                  <td id="dataTd1" style="white-space:nowrap"></td>
                  <td id="dataTd2"></td>
                  <td id="dataTd3"></td>
                  <td id="dataTd4" style="text-align:right;"></td>
                  <td id="dataTd7"><s:textfield name="quantityuArr" cssClass="text-number" size="10" maxlength="9" onchange="SSPRM56_changeCount(this)"/></td>
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
                	<s:if test='"2".equals(operateType) || "72".equals(operateType)|| "71".equals(operateType)'>
				  		 <td class="center" id="dataTd0"><input id="chkbox" type="checkbox" value="<%= i %>" onclick="SSPRM56_changechkbox(this)"/></td>
					     <td id="dataTd1" style="white-space:nowrap"><s:property value="UnitCode"/></td>
	                  	 <td id="dataTd2"><s:property value="BarCode"/></td>
	                  	 <td id="dataTd3"><s:property value="nameTotal"/></td>
	                  	 <td id="dataTd4" style="text-align:right;"><s:text name="format.price"><s:param value="Price"></s:param></s:text></td>
		                 <td id="dataTd7"><input type="text" name="quantityuArr" class="text-number" size="10" maxlength="9" onchange="SSPRM56_changeCount(this)" value='<s:property value="Quantity"/>'></td>
		                 <td id="money<%= i %>" style="text-align:right;"><s:text name="format.price"><s:param value="Price * Quantity"></s:param></s:text></td>
		                 <td id="dataTd10"><input type="text" name="reasonArr" size="25" maxlength="200" value="<s:property value='Reason'/>"></td>
				    </s:if>
				   <s:else>
				    	<td class="center" id="dataTd0"><%= i %></td>
				    	 <td id="dataTd1" style="white-space:nowrap"><span class="left" id="unicodeSpan"><s:property value="UnitCode"/></span>	                  		
	                  	 </td>
	                  	 <td id="dataTd2"><s:property value="BarCode"/></td>
	                  	 <td id="dataTd3"><s:property value="nameTotal"/></td>
	                  	 <td id="dataTd4" style="text-align:right;"><s:text name="format.price"><s:param value="Price"></s:param></s:text></td>
		                 <td id="dataTd7" style="text-align:right;"><s:text name="format.number"><s:param value="Quantity"></s:param></s:text></td>
		                 <td id="money<%= i %>" style="text-align:right;"><s:text name="format.price"><s:param value="Price * Quantity"></s:param></s:text></td>
		                 <td id="dataTd10"><s:property value='Reason'/></td>
				   </s:else>                  	 
	                 <td style="display:none" id="dataTd11">
	                  <input type="hidden" id="priceUnitArr<%= i %>" name="priceUnitArr" value="<s:property value='Price'/>"/>	                 
	                  <input type="hidden" id="priceTotalArr<%= i %>" name="priceTotalArr"/>
	                  <input type="hidden" id="promotionProductVendorIDArr<%= i %>" name="promotionProductVendorIDArr" value="<s:property value='BIN_PromotionProductVendorID'/>"/>
                  	  <input type="hidden" name="prmVendorId" value="<s:property value='BIN_PromotionProductVendorID'/>"/>
                  	</td>
	                </tr>
	                <%  i++; %>
                </s:iterator>
              </tbody>
            </table>           
           	</div>
           	<s:if test='"1".equals(operateType) || "80".equals(operateType)'>
           		<hr class="space" />
	            <table cellpadding="0" cellspacing="0" width="30%" border="0" class="right editable">
	              <tr>
	                <th rowspan="2" scope="row" class="center"><s:text name="global.page.total"/></th><%-- 合计 --%>
	                <td class="center"><s:text name="PRM56_totalQuantity"/></td><%-- 总数量 --%>
	                <td class="center"><s:text name="PRM56_totalAmount"/></td><%-- 总金额--%>
	              </tr>
	              <tr>
	                <td class="center"><s:text name="format.number"><s:param value="returnInfo.TotalQuantity"></s:param></s:text></td>
	             	<td class="center"><s:text name="format.price"><s:param value="returnInfo.TotalAmount"></s:param></s:text></td>
	              </tr>
	            </table>
		    </s:if>
            
		   <div style="display:none">
		    <input type="hidden" id="rowNumber" value="<%= i %>"/>
		    <input type="hidden" value="" name="csrftoken" id="csrftoken">
		    <input type="hidden" value="" name="operationFlag" id="operationFlag">
		    <input type="hidden" value="<s:property value="returnInfo.AllocationNoIF"/>" name="proAllocationNOIF" id="proAllocationNO">
		    <input type="hidden" value="<s:property value="taskInstanceID"/>" name="taskInstanceID" id="taskInstanceID">
		    <input type="hidden" value="<s:property value="returnInfo.UpdateTime"/>" name="updateTime" id="updateTime">
		    <input type="hidden" value="<s:property value="returnInfo.ModifyCount"/>" name="modifyCount" id="modifyCount">
		    <input type="hidden" value="<s:property value="returnInfo.VerifiedFlag"/>" name="verifiedFlag" id="verifiedFlag">
		    <input type="hidden" value="<s:property value="returnInfo.BIN_OrganizationID"/>" name="inOrganizationId"  id="inOrganizationId">
		    <input type="hidden" value="<s:property value="returnInfo.BIN_OrganizationIDAccept"/>" name="outOrganizationId"  id="outOrganizationId">
		    <input type="hidden" value="<s:property value="returnInfo.BIN_PromotionAllocationID"/>" id="proAllocationId" name="proAllocationId">
		   </div>
          </div>
        </div>
      </div>
      </s:else>
      <hr class="space" />
       <div class="center">
        	<s:if test='"2".equals(operateType)'>
           	 <%-- 修改 --%>
             <button type="button" class="save" onclick="SSPRM56_submitFormSave()" id="btnSave"><span class="ui-icon icon-save"></span>              
              <span class="button-text"><s:text name="btnSave"/></span>
             </button>
             <%-- 申请 --%>
             <button class="confirm" type="button" onclick="SSPRM56_submitFormSend()" id="btnSend"><span class="ui-icon icon-confirm"></span>
             <span class="button-text"><s:text name="btnSend"/></span>
             </button>            
             <%-- 删除 --%>
              <button type="button"  class="close" onclick="SSPRM56_submitFormDelete();" id="btnDelete"><span class="ui-icon icon-close"></span>
              <span class="button-text"><s:text name="btnDelete"/></span>
              </button>
		    </s:if>	
		     <s:elseif test='"71".equals(operateType) || "72".equals(operateType)|| "80".equals(operateType)'>
				<jsp:include page="/WEB-INF/jsp/common/osActionButton2.inc.jsp" flush="true" /> 
            </s:elseif>
              <%-- 关闭 --%>
              <button type="button" class="close" onclick="ClosePopupForm();"><span class="ui-icon icon-close"></span>             
              <span class="button-text"><s:text name="global.page.close"/></span>
             </button> 
			<hr class="space" />
			</div>
			</div>
   <!-- 此处再加入tabs-2  div-->
   <div id="tabs-2">
      <strong><s:text name="global.page.worksProcessing"/></strong>
   </div>
</div>
       </div>
    </div>
    	<script type="text/javascript">
		$("#databody tr:odd").addClass("even");
		$("#databody tr:even").addClass("odd");	
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
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
