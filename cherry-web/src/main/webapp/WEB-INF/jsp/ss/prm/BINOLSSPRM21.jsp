	<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM21.js?v=20161129"></script>
<script language="javascript">
</script>	
				<s:i18n name="i18n.ss.BINOLSSPRM21">
			<s:url id = "url_refreshAjax" value="/common/BINOLCM00_refreshSessionRequest"/>
	   		<span id = "s_refreshAjax" style="display:none">${url_refreshAjax}</span>
	   		
			<s:url id="url_getdepotAjax" value="/ss/BINOLSSPRM21_GETDEPOT" />
			<span id ="s_getdepotAjax" style="display:none">${url_getdepotAjax}</span>
			
            <s:url id="url_getLogicDepotAjax" value="/ss/BINOLSSPRM21_GETLOGICDEPOT" />
            <span id ="s_getLogicDepotAjax" style="display:none">${url_getLogicDepotAjax}</span>
			
			<s:url id="url_getMCategoryAjax" value="/ss/BINOLSSPRM21_GETSECONDCATEGORY" />
			<span id ="s_getMCategoryAjax" style="display:none">${url_getMCategoryAjax}</span>
			
			<s:url id="url_getSCategoryAjax" value="/ss/BINOLSSPRM21_GETSMALLCATEGORY" />
			<span id ="s_getSCategoryAjax" style="display:none">${url_getSCategoryAjax}</span>
			
			<s:url id="url_startStocktaking" value="/ss/BINOLSSPRM21_STOCKTAKING" />
			<span id ="s_startStocktaking" style="display:none">${url_startStocktaking}</span>
			
			<s:url id="url_saveURL" value="/ss/BINOLSSPRM21_SAVE" />
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
      <cherry:form id="mainForm" action="" class="inline">
      <div class="box">
            <div class="box-header clearfix"><span class="right" ><input type="checkbox" name="validFlag" id="validFlag" value="0"></input>
                    <s:text name="lblValidFlag"/></span></div>
            <div class="box-content clearfix">
              <div style="width: 50%;" class="column">
                <p><label><s:text name="lbldepartment"/></label>
                  <select style="width:200px;" name="organizationId" id="organizationId" onchange="chooseDepart(this)" >		                  		
	                  		<s:iterator value="organizationList">
				         		<option value="<s:property value="OrganizationID" />">
				         			<s:property value="DepartName"/>
				         		</option>
				      		</s:iterator>
	               </select>      
                </p>
                <p><label><s:text name="lblOutDepot"/></label>                 
                 <select style="width:200px;" name="depot" id="depot">		                  		
	                  		<s:iterator value="depotList">
				         		<option value="<s:property value="BIN_InventoryInfoID" />">
				         			<s:property value="InventoryName"/>
				         		</option>
				      		</s:iterator>
	               </select>
	               <input type="hidden" name ="hidDepot" id="hidDepot"/>
                 </p>
                <div id="pLogicDepot" class="<s:if test='null == logicDepotList || logicDepotList.size()==0'>hide</s:if>">
                    <label><s:text name="lblOutLogicDepot"/></label>
                    <select style="width:200px;" name="logicDepot" id="logicDepot">
                        <s:iterator value="logicDepotList">
                            <option value="<s:property value="BIN_LogicInventoryInfoID" />">
                                <s:property value="LogicInventoryCodeName"/>
                            </option>
                        </s:iterator>
                    </select>
                    <input type="hidden" name ="hidLogicDepot" id="hidLogicDepot"/>
                </div>
              </div>
              <div style="width: 49%;" class="column last">
                <p><label><s:text name="lblLCategory"/></label>
                	<select style="width:100px;" name="largeCategory" id="largeCategory" onchange="choosePrimaryCategory()">
		                  <option value="0"><s:text name="textAll"/></option>		                  
		                  <s:iterator value="largeCategoryList">
					         <option value="<s:property value="PrimaryCategoryCode" />">
					         	<s:property value="PrimaryCategoryName"/>
					         </option>
					      </s:iterator>
		            </select>
		        </p>
                <p><label><s:text name="lblMCategory"/></label>                
               		<select style="width:100px;" name="middleCategory" id="middleCategory" onchange="chooseSecondCategory()">
               			<option value="0"><s:text name="textAll"/></option>	                  	
		             </select>
		        </p>
               <div><label><s:text name="lblSCategory"/></label>
               		<select style="width:100px;" name="smallCategory" id="smallCategory" >
               		<option value="0"><s:text name="textAll"/></option>	                  
		            </select>
		       </div>
              </div>
              <hr>
              <div style="width: 100%; padding: 0px; margin:5px 0px;" class="column last">
                <p><label><s:text name="lblReasonAll"/></label>&nbsp;<textarea name="reasonAll" id="reasonAll" maxlength="100" onkeyup="return isMaxLen(this)" rows="1" style="width:85%;height:45px;overflow-y:hidden"></textarea> 
           
                </p>                
              </div>
            </div>
            <p class="clearfix">
              		<button id="spanBtnStart" onclick="startStocktaking()" type="button" class="right search">
              			<span class="ui-icon icon-search-big"></span>
              			<span  class="button-text"><s:text name="btnStocktaking"/></span>
              		</button>
              		<button id="spanBtnCancel" onclick="cancelStocktaking()" type="button" class="right search" style="display:none">
              			<span class="ui-icon icon-search-big"></span>
              			<span class="button-text" ><s:text name="btnGiveup"/></span>
              		</button>
            </p>
        </div>		
      <div id="mydetail" class="section">
      </div>
      </cherry:form>
     </div>
     <div id="errmessage" style="display:none">
	<input type="hidden" id="errmsgESS00042" value='<s:text name="ESS00042"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
	<input type="hidden" id="errmsgESS00007" value='<s:text name="ESS00007"/>'/>
	<input type="hidden" id="errmsgESS00061" value='<s:text name="ESS00061"/>'/>
	
</div>
</s:i18n>		

