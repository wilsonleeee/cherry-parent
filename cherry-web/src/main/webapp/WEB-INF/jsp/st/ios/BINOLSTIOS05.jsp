<%-- 自由盘点 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/common/cherry.js"></script>
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS05.js"></script>
<script type="text/javascript">
       if(refreshSessionTimerID == null){
	       	refreshSessionTimerID = setInterval("refreshCherrySession($('#s_refreshAjax').html())", 30*60*1000);
       }
</script>
<s:i18n name="i18n.st.BINOLSTIOS05">
<s:url id = "url_refreshAjax" value="/common/BINOLCM00_refreshSessionRequest"/>
<span id = "s_refreshAjax" style="display:none">${url_refreshAjax}</span>
<s:url id="url_getdepotAjax" value="/st/BINOLSTIOS05_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTIOS05_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<s:url id="url_checkBatchNo" value="/st/BINOLSTIOS05_checkBatchNo" />
<span id ="s_checkBatchNo" style="display:none">${url_checkBatchNo}</span>
<s:url id="url_save" value="/st/BINOLSTIOS05_save" />
<span id ="urlSave" style="display:none">${url_save}</span>
<s:url id="url_submit" value="/st/BINOLSTIOS05_submit" />
<span id ="urlSubmit" style="display:none">${url_submit}</span>
<s:url id="urlgetLogDepotByAjax" value="/st/StIndex_getLogDepotByAjax" />
<span id ="url_getLogDepotByAjax" style="display:none">${urlgetLogDepotByAjax}</span>
<div class="hide">
    <s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
    <input type="hidden" id="allowNegativeFlag" name="allowNegativeFlag" value='<s:property value="allowNegativeFlag" />'/>
</div>
<div class="panel-header">
    <div class="clearfix">
        <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
    </div>
</div>
    <div id="actionResultDisplay"></div>
    <cherry:form id="mainForm" class="inline" onsubmit="return false;">
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
    <s:hidden name="brandInfoId" id="brandInfoId" value="%{brandInfoId}"></s:hidden>
    <input type="hidden" id=blindFlag name="blindFlag" value="<s:property value='blindFlag' />"/>
    	<div class="section">
              <div class="section-header">
				<strong>
					<span class="ui-icon icon-ttl-section-info"></span>
					<s:text name="IOS05_general"/>
				</strong>
			  </div>
              <div class="section-content">
              <table class="detail">
              	<tr>
              		<th><s:text name="IOS05_depart"/></th>
              		<td>
						<input id="organizationId" type="hidden" name="organizationId" value='${organizationId}'>
                        <span id="orgName" class="left"><s:property value='departInit'/></span>
                        <a class="add right" onclick="BINOLSTIOS05.openDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text"><s:text name="IOS05_select"/></span>
                        </a>
                          <s:hidden name="witposStaking" id="witposStaking" value="%{witposStaking}"></s:hidden>
					</td>
					<th><s:text name="IOS05_StocktakingMode"/></th>
              		<td>
              			<span class="red">
              			<s:if test='"0".equals(blindFlag)'><s:text name="IOS05_noBlind"/></s:if>
                    	<s:if test='"1".equals(blindFlag)'><s:text name="IOS05_isBlind"/></s:if>
                    	</span>
					</td>
              		
              	</tr>
              	<tr>
              		<th><s:text name="IOS05_depot"/></th>
              		<td>
              			<select style="width:200px;" name="depotInfoId" id="depotInfoId" disabled="disabled" onchange="BINOLSTIOS05.clearDetailData();return false;">
                           <option value=""><s:text name="IOS05_selectAll"/></option>
						</select>
					</td>
					<s:if test='null!=logicDepotsInfoList && logicDepotsInfoList.size()>0'> 
						<th><s:text name="IOS05_logicDepot"/></th>
						<td>
							<s:select disabled="true" name="logicDepotsInfoId" list="logicDepotsInfoList" listKey="BIN_LogicInventoryInfoID" listValue="LogicInventoryCodeName" headerKey="" cssStyle="width:200px;" onchange="BINOLSTIOS05.clearDetailData();"></s:select>
						</td>
					</s:if>
					<s:else>
	              		<th></th>
	              		<td></td>
              		</s:else>
              	</tr>

                <s:if test='!batchFlag.equals("1")'>
                    <tr>
                        <th class="hide"><s:text name="IOS05_isBatchStockTaking"/></th>
                        <td class="hide">
                            <input type="radio" id="BatchStockTakingTrue" name="isBatchStockTaking" value="true" /><label for="BatchStockTakingTrue" style="width:10px"><s:text name="IOS05_yes"/></label>
                            <input type="radio" id="BatchStockTakingFalse" name="isBatchStockTaking" value="false" checked/><label for="BatchStockTakingFalse" style="width:10px"><s:text name="IOS05_no"/></label>
                        </td>
                        <th><s:text name="IOS05_reason"></s:text></th>
                        <td colspan="3"><input class="text" type="text" name="reason" id="reason" maxlength="200" style="width:95%;"/></td>
                    </tr>
                </s:if>
				<s:else>
                    <tr>
                        <th><s:text name="IOS05_isBatchStockTaking"/></th>
                        <td>
                            <input type="radio" id="BatchStockTakingTrue" name="isBatchStockTaking" value="true" /><label for="BatchStockTakingTrue" style="width:10px"><s:text name="IOS05_yes"/></label>
                            <input type="radio" id="BatchStockTakingFalse" name="isBatchStockTaking" value="false" checked/><label for="BatchStockTakingFalse" style="width:10px"><s:text name="IOS05_no"/></label>
                        </td>
                        <th><s:text name="IOS05_reason"></s:text></th>
                        <td><input class="text" type="text" name="reason" id="reason" maxlength="200" style="width:95%;"/></td>
                    </tr>
				</s:else>

              </table>
              </div>
            </div>        
        <div class="section">
            <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="IOS05_detail"/></strong></div>
            <div class="section-content">
                <div class="toolbar clearfix">
                    <span class="left">
                        <a class="add" onclick="BINOLSTIOS05.openProPopup(this);">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS05_addProduct"/></span>
                        </a>
                        <a class="delete" onclick="BINOLSTIOS05.deleteRow();">
                            <span class="ui-icon icon-delete"></span>
                            <span class="button-text"><s:text name="IOS05_delete"/></span>
                        </a>
                        <a id="addNewLineBtn" class="add" onclick="BINOLSTIOS05.addNewLine();">
                            <span class="ui-icon icon-add"></span>
                            <span class="button-text"><s:text name="IOS05_add"/></span>
                        </a>
                    </span>
                </div>
                <div style="width:100%;overflow-x:scroll;">
                    <input type="hidden" id="rowNumber" value="0"/>
                    <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
                        <thead>
                            <tr>
                                <th class="tableheader" width="3%"><input id="allSelect" type="checkbox"  class="checkbox" onclick="BINOLSTIOS05.selectAll();"/><s:text name="IOS05_select"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS05_code"/></th>
                                <th class="tableheader" width="10%"><s:text name="IOS05_barcode"/></th>
                                <th class="tableheader" width="18%"><s:text name="IOS05_name"/></th>
                                <th class="tableheader" width="12%"><s:text name="IOS05_batchNo"/></th>
                                <s:if test='"0".equals(blindFlag)'>
                                    <th class="tableheader" width="12%"><s:text name="IOS05_curStock"/></th>
                                </s:if>
                                <th class="tableheader" width="5%"><s:text name="IOS05_stockTakingQuantity"/></th>
                                <s:if test='"0".equals(blindFlag)'>
                                    <th class="tableheader" width="10%"><s:text name="IOS05_GainQuantity"/></th>
                                </s:if>
                                <th class="tableheader" width="30%"><s:text name="IOS05_remark"/></th>
                                <th style="display:none">
                            </tr>
                        </thead>
                        <tbody id="databody">
                        </tbody>
                    </table>
                </div>
                <hr class="space" />
                <div class="center clearfix">
                	<cherry:show domId="BINOLSTIOS0502">
                	<button class="save" type="button" onclick="BINOLSTIOS05.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.saveTemp"/></span></button>
                     </cherry:show>
                    <cherry:show domId="BINOLSTIOS0501">
                    <button class="confirm" type="button" onclick="BINOLSTIOS05.confirm();"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="IOS05_save"/></span></button>
                    </cherry:show>
                </div>
            </div>
        </div>
    </div>
    </s:else>
    </cherry:form>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>

<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>

<div id="errmessage" style="display:none">
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00017" value='<s:text name="EST00017"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00035" value='<s:text name="EST00035"/>'/>
</div>