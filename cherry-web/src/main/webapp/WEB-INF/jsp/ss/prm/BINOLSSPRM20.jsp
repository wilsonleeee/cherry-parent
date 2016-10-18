<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM20.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script language="javascript">
</script>
<s:i18n name="i18n.ss.BINOLSSPRM20">
<s:url id="url_getOutDepart" value="/ss/BINOLSSPRM20_GETOUTDEPART" />
<span id ="s_getOutDepart" style="display:none">${url_getOutDepart}</span>

<s:url id="url_getDepot" value="/ss/BINOLSSPRM20_GETDEPOT" />
<span id ="s_getDepot" style="display:none">${url_getDepot}</span>

<s:url id="url_popup" value="/ss/BINOLSSPRM20_POPUP" />
<span id ="s_popup" style="display:none">${url_popup}</span>

<s:url id="url_saveURL" value="/ss/BINOLSSPRM20_SAVE" />
<span id ="s_saveURL" style="display:none">${url_saveURL}</span>

      <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="lbltitle1"/> &gt; <s:text name="lbltitle2"/></span> </div>
      </div>
      <div id="actionResultDisplay"></div>
      <div id="errorDiv2" class="actionError" style="display:none">
		      <ul>
		         <li><span id="errorSpan2"></span></li>
		      </ul>			
      </div>
		<s:if test="hasActionErrors()">
			<div class="actionError" id="actionResultDiv">
			  	<s:actionerror/>
			  	<ct:form id="mainForm" action="" class="inline"></ct:form>
			</div>
			<div style="height:20px"></div>
		</s:if>
		<s:else>
      <div class="panel-content">  
      <%-- ========概要部分============= --%> 
      <ct:form id="mainForm" action="" class="inline">
      <div class="box">
            <div class="box-header"><span ></span></div>
            <div class="box-content clearfix">
              <div style="width: 100%; height: 85px;" class="column last">
              <p><label><s:text name="lbldate"/></label>&nbsp;<%= new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %></p>
                 
                <p><label><s:text name="lblOutDepart"/></label>
                  <%-- 
                  <select style="width:120px;" name="outOrganizationId" id="outOrganizationId" onchange="SSPRM20_chooseOutDepart(this)" >		                  		
	                  		<s:iterator value="outOrganizationList">
				         		<option value="<s:property value="OrganizationID" />">
				         			<s:property value="DepartName"/>
				         		</option>
				      		</s:iterator>
	               </select>
                   --%>
                   <input type="hidden" name="outOrganizationId" id="outOrganizationId">
                   <label id="outOrgName" style="width:150px;text-align:left;"></label>
                   <a class="add " onclick="SSPRM20_openOutDepartBox(this);">
                       <span class="ui-icon icon-search"></span>
                       <span class="button-text"><s:text name="lblselect"/></span>
                   </a>
	               &nbsp;
	               <label><s:text name="lblOutDepot"/></label>
	               <select style="width:120px;" name="outDepotId" id="outDepotId" >
                            <option value=""><s:text name="PRM20_Select"/></option>
	                  		<s:iterator value="outDepotList">
				         		<option value="<s:property value="BIN_InventoryInfoID" />">
				         			<s:property value="InventoryName"/>
				         		</option>
				      		</s:iterator>
	               </select>
	               &nbsp;	              
                </p>  
                <p><label><s:text name="lblRelevanceOrder"/></label>
                 <a href="javascript:SSPRM20_openPopup();void(0);"><s:text name="lblChooseFileLink"/></a>
                </p>                          
              </div>              
               <div style="width: 100%; height: 50px; padding: 0px 0px 10px; margin: 0px 0px 5px;display:none" class="column">
                <p><label><s:text name="lblReasonAll"/></label>&nbsp;<textarea name="reasonAll" id="reasonAll" maxlength="100" onkeyup="return isMaxLen(this)" rows="1" style="width:440px;height:45px;overflow:hidden"></textarea> 
           		</p>                
              </div>
            </div>         
        </div>		
      <div id="mydetail" class="section">          
        </div>
      </ct:form>
     </div>
     </s:else>
</s:i18n>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00008"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00025"/>'/>
	<input type="hidden" id="errmsgESS00042" value='<s:text name="ESS00042"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>