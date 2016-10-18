<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="com.cherry.cm.cmbeans.RoleInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.text.SimpleDateFormat" %>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM50.js"></script>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script language="javascript">
function ajaxFileUpload(){
	SSPRM50_deleteActionMsg();
	
	if($('#outOrganizationId').val()==null || $('#outOrganizationId').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00024').val());
		$('#errorDiv2').show();
		return false;
	}
	if($('#outDepot').val()==null || $('#outDepot').val()==""){
		$('#errorDiv2 #errorSpan2').html($('#errmsgESS00042').val());
		$('#errorDiv2').show();
		return false;
	}
	
	if($('#upExcel').val()==''){
		$('#errorDiv2 #errorSpan2').html($('#errmsg3').val());
		$('#errorDiv2').show();
		return false;
	}
	
	$("#loading").show();
    $.ajaxFileUpload(
        {
     url:'/Cherry/ss/BINOLSSPRM50_UPLOAD.action',            //需要链接到服务器地址
     secureuri:false,
     data:{'csrftoken':$('#csrftoken').val(),'outOrganizationId':$('#outOrganizationId').val()},
     fileElementId:'upExcel',                        //文件选择框的id属性
     dataType: 'html',                                     //服务器返回的格式，可以是json
     success: function (data, status)            //相当于java中try语句块的用法
     {     
        
    	// parsefile();
    	 $("#loading").hide();
    	 $('#mydetail').html(data);
     },
     error: function (data, status, e)            //相当于java中catch语句块的用法
     {        
    	 $("#loading").hide();
     }
 }
    );
}

function chooseFile(){
	upExcel.click();
}

</script>

<ct:form id="mainForm" action="/Cherry/ss/BINOLSSPRM50_SUBMIT" enctype="multipart/form-data">
<s:i18n name="i18n.ss.BINOLSSPRM50">
<%
UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd"); //格式化当前系统日�
String dateTime = dateFm.format(new java.util.Date());

%>
<div class="hide">
<s:select id="departTypePop" name="departTypePop" list='#application.CodeTable.getCodes("1000")' listKey="CodeKey" listValue="Value"/>
</div>
<s:hidden id="depotJson"></s:hidden>
<s:url id="url_getdepotAjax" value="/ss/BINOLSSPRM50_getDepot" />
<span id ="urlgetdepotAjax" style="display:none">${url_getdepotAjax}</span>
<s:url id="url_getconDepartAjax" value="/ss/BINOLSSPRM50_getDepart" />
<span id ="urlgetdepartAjax" style="display:none">${url_getconDepartAjax}</span>
<s:url id="url_popup" value="/ss/BINOLSSPRM50_OPENPOPUP" />
<span id ="s_popup" style="display:none">${url_popup}</span>
<s:url id="url_parsefile" value="/ss/BINOLSSPRM50_PARSEFILE" />
<span id ="s_parsefile" style="display:none">${url_parsefile}</span>
<s:url id="downloadUrl" value="/download/"/>
<span id ="s_downloadUrl" style="display:none">${downloadUrl}</span>
<script>
function downloadClick(){
    var downloadUrl=$('#s_downloadUrl').html()+'${brandCode}'+"/Excel发货模板.xls";
    window.location.href=downloadUrl;
}
</script>
<s:url id="s_submitURL" value="/ss/BINOLSSPRM50_SUBMIT" />
<span id ="submitURL" style="display:none">${s_submitURL}</span>

      <div class="panel-header">
        <div class="clearfix"> <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="lbltitle"/> &gt; <s:text name="lbltitle2"/></span> </div>
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
      <%-- ========概要部分============= --%> 
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
                        <input type="hidden" name="outOrganizationId" id="outOrganizationId">
                        <span id="outOrgName" class="left"></span>
                        <a class="add right" onclick="SSPRM50_openSendDepartBox(this);"><span class="ui-icon icon-search"></span><span class="button-text"><s:text name="lblselect"/></span></a>
                    <%--
                    <select style="width:100px;" name="outOrganizationId" id="outOrganizationId" onchange="chooseDepart(this)" >		                  		
	                  		<s:iterator value="outOrganizationList">
				         		<option value="<s:property value="OrganizationID" />">
				         			<s:property value="DepartName"/>
				         		</option>
				      		</s:iterator>
	                  	</select>
                        --%>
                    </td>
                    
                    <th><s:text name="lblOutDepot"/></th>
                    <td>                   		
		                  	<select style="width:100px;" name="outDepot" id="outDepot" onchange="SSPRM50_deleteActionMsg()" >
		                  	<option value=""><s:text name="PRM50_SelectAll"/></option>
                            <s:iterator value="outDepotList">
					         		<option value="<s:property value="BIN_InventoryInfoID" />">
					         			<s:property value="InventoryName"/>
					         		</option>
					      		</s:iterator>
		                  	</select>		                  	
					</td>
                  </tr>
                  <tr>
                  <td colspan="4">
                  <span class="highlight"><s:text name="btnSnow"/></span>
                  <s:text name="btnNotice"/>
                  <a href ="#" onclick="downloadClick();"><s:text name="btnDownload"/></a>
                  </td>
                  </tr>               
                    <tr>
                    <th><s:text name="lblChooseFile"/></th>
                    <td colspan="3">
                    <input type="file" id="upExcel" name="upExcel"  onchange="SSPRM50_deleteActionMsg();" size="45" style="heiht:25px"/> 
                    <input type="button" value="<s:text name="btnUpload"/>" onclick="ajaxFileUpload()" id="test"/><img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
                   </td>
                   </tr>
                </table> 
              </div>
            </div>
        <div id="mydetail" class="section">       
        </div>
      </div>
     </s:else>

</s:i18n>
</ct:form>
<div id="errmessage" style="display:none">
	<input type="hidden" id="errmsg1" value='<s:text name="ESS00013"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="ESS00014"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="ESS00031"/>'/>
	<input type="hidden" id="errmsgESS00024" value='<s:text name="ESS00024"/>'/>
	<input type="hidden" id="errmsgESS00042" value='<s:text name="ESS00042"/>'/>
</div>
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/popDepartTable.jsp" flush="true" />
<%-- ================== 弹出datatable -- 部门共通START ======================= --%>