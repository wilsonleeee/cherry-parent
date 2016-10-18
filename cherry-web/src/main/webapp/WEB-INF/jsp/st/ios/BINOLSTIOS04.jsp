<%-- 商品盘点 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/st/ios/BINOLSTIOS04.js"></script>
<style>
.column label {
    width: 95px;
}
</style>
<s:i18n name="i18n.st.BINOLSTIOS04">
<s:url id = "url_refreshAjax" value="/common/BINOLCM00_refreshSessionRequest"/>
<span id = "s_refreshAjax" style="display:none">${url_refreshAjax}</span>
<s:url id="url_getdepotAjax" value="/st/BINOLSTIOS04_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getStockCount" value="/st/BINOLSTIOS04_getStockCount" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>
<s:url id="url_checkBatchNo" value="/st/BINOLSTIOS04_checkBatchNo" />
<span id ="s_checkBatchNo" style="display:none">${url_checkBatchNo}</span>
<s:url id="url_save" value="/st/BINOLSTIOS04_save" />
<span id ="urlSave" style="display:none">${url_save}</span>
<s:url id="url_saveTemp" value="/st/BINOLSTIOS04_saveTemp" />
<span id ="urlSaveTemp" style="display:none">${url_saveTemp}</span>
<s:url id="url_startStocktaking" value="/st/BINOLSTIOS04_STOCKTAKING" />
<span id ="s_startStocktaking" style="display:none">${url_startStocktaking}</span>
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
    <cherry:form id="mainForm" class="inline">
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
    <%-- ========概要部分============= --%>
        <div class="box">
            <div class="box-header"><span></span></div>
            <div class="box-content clearfix">
                <div id="divColumnLeft" style="width: 50%;" class="column">
                	<div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                		<label><s:text name="IOS04_StocktakingMode"/></label>
                		<span class="red">
                       	<s:if test='"0".equals(blindFlag)'><s:text name="IOS04_noBlind"/></s:if>
                       	<s:if test='"1".equals(blindFlag)'><s:text name="IOS04_isBlind"/></s:if>
                       	</span>
                    </div>
                    <%--部门 --%>
                    <div class="clearfix" style="line-height:25px;margin-bottom:6px;white-space:nowrap;">
                        <label style="display:block;float:left;"><s:text name="IOS04_depart"/></label>
                        <input id="organizationId" type="hidden" name="organizationId" value='${organizationId}'>
                         <span  style="float:left;display:block;text-align:left;"><span id="orgName" ><s:property value='departInit'/></span></span>
                        <a id="openDepartBox" class="add left" onclick="BINOLSTIOS04.openDepartBox(this);">
                            <span class="ui-icon icon-search"></span>
                            <span class="button-text" ><s:text name="IOS04_select"/></span>
                        </a>
                        <s:hidden name="witposStaking" id="witposStaking" value="%{witposStaking}"></s:hidden>
                    </div>
                    <%--实体仓库 --%>
                    <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                        <label><s:text name="IOS04_depot"/></label>
                        <select disabled style="width:200px;" name="depotInfoId" id="depotInfoId">                               
                            <option value=""><s:text name="IOS04_selectAll"/></option>
                        </select>
                    </div>
                    <%--逻辑仓库 --%>
                    <div class="clearfix" style="line-height:25px;margin-bottom:6px;">
                    <s:if test='null!=logicDepotsInfoList && logicDepotsInfoList.size()>0'>
                        <label><s:text name="IOS04_logicDepot"/></label>
                        <select disabled style="width:200px;" name="logicDepotsInfoId" id="logicDepotsInfoId">
                            <s:iterator value="logicDepotsInfoList">
                                <option value="<s:property value="BIN_LogicInventoryInfoID" />">
                                    <s:property value="LogicInventoryCodeName"/>
                                </option>
                            </s:iterator>
                        </select>
                        </s:if>
                    </div>
                    <%--批次盘点 --%>
                    <div class="<s:if test='!batchFlag.equals("1")'>hide</s:if>">
                        <label><s:text name="IOS04_isBatchStockTaking"/></label>
                        <input type="radio" id="BatchStockTakingTrue" name="isBatchStockTaking" value="true"/><label for="BatchStockTakingTrue" style="width:10px"><s:text name="IOS04_yes"/></label>
                        <input type="radio" id="BatchStockTakingFalse" name="isBatchStockTaking" value="false" checked/><label for="BatchStockTakingFalse" style="width:10px"><s:text name="IOS04_no"/></label>
                    </div>
                </div>
                <div id="divColumnRight" style="width: 49%;" class="column last">
                    <s:text name="IOS04_all" id="IOS04_all"/>
                    <s:iterator value="prtCategoryList" id="prtCatPropValueList" status="status">
                        <p>
                            <label><s:text name="IOS04_category%{#status.index+1}"/></label>
                            <s:select list="prtCatPropValueList" Cssstyle="width:120px;" name="categoryArr" id="categoryArr%{#status.index+1}" listKey="BIN_PrtCatPropValueID" listValue="PropValueChinese" headerKey="" headerValue="%{#IOS04_all}">                               
                            </s:select>
                        </p>
                    </s:iterator>
                </div>
                <hr>
                    <div class="column last" style="width: 100%; padding: 0px; margin:5px 0px 10px 0px;">
                        <label><s:text name="IOS04_reasonAll"/></label>&nbsp;
                        <textarea name="comments" id="comments" maxlength="100" onkeyup="return isMaxLen(this)" rows="1" style="width:80%;height:50px;overflow-y:hidden;overflow-x:auto;"></textarea> 
                    </div>
               
            </div>
            <p class="clearfix">
                 <button id="spanBtnStart" onclick="BINOLSTIOS04.startStocktaking();" type="button" class="right search">
                     <span class="ui-icon icon-search-big"></span>
                     <span  class="button-text"><s:text name="IOS04_btnStocktaking"/></span>
                 </button>
                 <button id="spanBtnCancel" onclick="BINOLSTIOS04.cancelStocktaking();" type="button" class="right search" style="display:none">
                     <span class="ui-icon icon-search-big"></span>
                     <span class="button-text" ><s:text name="IOS04_btnGiveup"/></span>
                 </button>
             </p>
        </div>
        <input type="hidden" id=blindFlag name="blindFlag" value="<s:property value='blindFlag' />"/>
        <div id="mydetail" class="section">
        </div>
        <div class="hide" id="dialogInit"></div>
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
    <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <input type="hidden" id="errmsg_EST00006" value='<s:text name="EST00006"/>'/>
    <input type="hidden" id="errmsg_EST00007" value='<s:text name="EST00007"/>'/>
    <input type="hidden" id="errmsg_EST00008" value='<s:text name="EST00008"/>'/>
    <input type="hidden" id="errmsg_EST00009" value='<s:text name="EST00009"/>'/>
    <input type="hidden" id="errmsg_EST00013" value='<s:text name="EST00013"/>'/>
    <input type="hidden" id="errmsg_EST00015" value='<s:text name="EST00015"/>'/>
    <input type="hidden" id="errmsg_EST00017" value='<s:text name="EST00017"/>'/>
    <input type="hidden" id="errmsg_EST00024" value='<s:text name="EST00024"/>'/>
    <input type="hidden" id="errmsg_EST00030" value='<s:text name="EST00030"/>'/>
</div>
