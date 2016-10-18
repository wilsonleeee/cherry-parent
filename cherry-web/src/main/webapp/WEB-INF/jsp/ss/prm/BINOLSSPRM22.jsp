<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/Cherry/js/common/cherry.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM22.js"></script>
<script language="javascript">
</script>
<script type="text/javascript">
       if(refreshSessionTimerID == null){
	       	refreshSessionTimerID = setInterval("refreshCherrySession($('#s_refreshAjax').html())", 30*60*1000);
       }
</script>
<s:i18n name="i18n.ss.BINOLSSPRM22">
<s:url id = "url_refreshAjax" value="/common/BINOLCM00_refreshSessionRequest"/>
<span id = "s_refreshAjax" style="display:none">${url_refreshAjax}</span>

<s:url id="url_getdepotAjax" value="/ss/BINOLSSPRM22_GETDEPOT" />
<span id ="s_getdepotAjax" style="display:none">${url_getdepotAjax}</span>

<s:url id="url_getLogicDepotAjax" value="/ss/BINOLSSPRM22_GETLOGICDEPOT" />
<span id ="s_getLogicDepotAjax" style="display:none">${url_getLogicDepotAjax}</span>

<s:url id="url_getStockCount" value="/ss/BINOLSSPRM22_GETSTOCKCOUNT" />
<span id ="s_getStockCount" style="display:none">${url_getStockCount}</span>

<s:url id="url_saveURL" value="/ss/BINOLSSPRM22_SAVE" />
<span id ="s_saveURL" style="display:none">${url_saveURL}</span>

    <div class="hide">
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
      <div id="errorDiv2" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan2"></span></li>
		      </ul>			
      </div>
      <div class="panel-content">  
      <%-- ========概要部分============= --%> 
      <ct:form id="mainForm" action="" class="inline">
      <s:hidden name="brandInfoId" value="%{brandInfoId}"></s:hidden>
      <div class="section">
	    <div class="section-header">
			<strong>
				<span class="ui-icon icon-ttl-section-info"></span>
				<s:text name="lblgeneral"/>
			</strong>
		</div>
		<div class="section-content">
			<table class="detail">
				<tr>
					<th><s:text name="lbldate"/></th>
					<td><%= new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %></td>
					<th><s:text name="lbldepartment"/></th>
					<td>
						<select style="width:200px;" name="organizationId" id="organizationId" onchange="SSPRM22_chooseDepart(this)" >		                  		
							<s:iterator value="organizationList">
								<option value="<s:property value="OrganizationID" />">
									<s:property value="DepartName"/>
								</option>
							</s:iterator>
						</select>
					</td>
				</tr>
				<tr>
					<th><s:text name="lblOutDepot"/></th>
					<td>
						<select style="width:200px;" name="depot" id="depot" onchange="SSPRM22_clearDetail();">		                  		
							<s:iterator value="depotList">
							<option value="<s:property value="BIN_InventoryInfoID" />">
								<s:property value="InventoryName"/>
							</option>
							</s:iterator>
						</select>
						<input type="hidden" name ="hidDepot" id="hidDepot"/>
					</td>
					<span id="pLogicDepot" class="<s:if test='null == logicDepotList || logicDepotList.size()==0'>hide</s:if>">
						<th>
							<s:text name="lblOutLogicDepot"/>
						</th>
						<td>
							<select style="width:200px;" name="logicDepot" id="logicDepot" onchange="SSPRM22_clearDetail();">                             
								<s:iterator value="logicDepotList">
									<option value="<s:property value="BIN_LogicInventoryInfoID" />">
										<s:property value="LogicInventoryCodeName"/>
									</option>
								</s:iterator>
							</select>
							<input type="hidden" name ="hidLogicDepot" id="hidLogicDepot"/>
						</td>
					</span>
				</tr>
				<tr>
					<th><s:text name="lblReasonAll"></s:text></th>
					<td colspan=3><input class="text" type="text" name="reasonAll" id="reasonAll" maxlength="200" style="width:95%;"/></td>
				</tr>
			</table>
		</div>
	  </div>        
      <div id="mydetail" class="section">
          <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="lbldetail"/></strong></div>
		  <div class="section-content">
          <div class="toolbar clearfix"><span class="left">
             <input id="allSelect" type="checkbox"  class="checkbox" onclick="SSPRM22_selectAll()"/>
             <s:text name="btnallselect"/><a class="add" onclick="SSPRM22_openPromotionSearchBox(this);"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="btnadd"/></span></a> <a class="delete" onclick="SSPRM22_deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="btndelete"/></span></a></span></div>
            <div style="width:100%;overflow-x:scroll;">
            <input type="hidden" id="rowNumber" value="1"/>
            <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
              <thead>
                <tr>
	               <th class="tableheader" width="3%"><s:text name="lblselect"/></th>
	               <th class="tableheader" width="10%"><s:text name="lblCode"/></th>
	               <th class="tableheader" width="10%"><s:text name="lblBarcode"/></th>
	               <th class="tableheader" width="18%"><s:text name="lblProname"/></th>
	               <th class="tableheader" width="5%"><s:text name="lblPrice"/></th>
	               <th class="tableheader" width="12%"><s:text name="lblBookCount"/></th>            
	               <th class="tableheader" width="5%"><s:text name="lblCheckCount"/></th>
	               <th class="tableheader" width="5%"><s:text name="lblGainQuantity"/></th>
	               <th class="tableheader" width="10%"><s:text name="lblMoney"/></th>
	               <th class="tableheader" width="20%"><s:text name="lblReason"/></th>
	               <th style="display:none">
                </tr>
              </thead>
              <tbody id="databody">            
              </tbody>
            </table>
            </div>
            <hr class="space" />
            <div class="center clearfix">            	
            	<button class="confirm" type="button" onclick="SSPRM22_submitFormSave()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnOK"/></span></button>
            </div>
          </div>
        </div>
      </ct:form>
     </div>
</s:i18n>
<%-- ================== 弹出datatable -- 促销产品共通导�?START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00025"/>'/>
	<input type="hidden" id="errmsg4" value='<s:text name="ESS00062"/>'/>
		<input type="hidden" id="errmsgESS00042" value='<s:text name="ESS00042"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
</div>
<%--<--%@ include file="/WEB-INF/jsp/common/prmDepotPopDiv.jsp" %-->
<%-- ================== 弹出datatable -- 促销产品共通导�?START ======================= --%>