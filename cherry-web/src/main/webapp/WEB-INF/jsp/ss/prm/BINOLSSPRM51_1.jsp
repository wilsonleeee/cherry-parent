<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="ct" uri="/cherry-tags"%> 
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap"%>
<link rel="stylesheet" href="/Cherry/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/Cherry/css/cherry/cherry.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery.form.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	window.onbeforeunload = function(){
		if (window.opener) {
			window.opener.unlockParentWindow();
		}
	};		
	if (window.opener) {	
	  window.opener.lockParentWindow();	
	}	
} );

function endSelect(){	

	//如果画面上有错误提示（只会出现一个提示：没有数据），直接关闭本画面
	if($('#errorMsgDivTOP2').html()!=null){		
		window.close(); 
		return ;
	}
	
	var okflag = false;
	$("#databody :checkbox").each(function(){
		if($(this).prop("checked"))
			{	
			  okflag = true;          
			}					
		});	
	if(okflag){
		//如果选中了数据，主画面去取得数据显示在画面上
		var argUrl = $('#s_getDeliverDetail').html();
		var params=$('#deliverForm').formSerialize();
		window.opener.getDeliverDetail(argUrl,params);		
	}
	window.close();
	return false;
}
</script>
<s:i18n name="i18n.ss.BINOLSSPRM51">
<div style="height:20px"></div>
<div class="panel ui-corner-all">
	<div class="panel-content">
	<span><STRONG><s:text name="lblwelcome"/></STRONG></span>
	<s:url id="url_getDeliverDetail" value="/ss/BINOLSSPRM51_getDeliverDetail" />
	<span id ="s_getDeliverDetail" style="display:none">${url_getDeliverDetail}</span>
	<div class="center clearfix">
		<div class="center clearfix" style="margin-top:15px">
		      <s:if test="hasActionErrors()">
				<div class="actionError" id="errorMsgDivTOP2">
				  	<s:actionerror/>
				</div>
			 </s:if>
		<div style="width:100%;text-align:left;overflow-x:scroll;">
		<s:form action ="/ss/BINOLSSPRM50_UPLOAD.action" method ="POST" id="deliverForm" name="deliverForm"> 
         <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%">
           <thead>
             <tr>
             <th class="tableheader" width="3%" ><s:text name="lblRadio"/></th>
               <th class="tableheader" width="5%">No.</th>
               <th class="tableheader" width="10%"><s:text name="lblDeliverReceiveNo"/></th>
               <th class="tableheader" width="18%"><s:text name="lblOrganizationID"/></th>
               <th class="tableheader" width="5%"><s:text name="lblEmployeeID"/></th>
               <th class="tableheader" width="12%"><s:text name="lblTotalQuantity"/></th>
               <th class="tableheader" ><s:text name="lblDeliverDate"/></th>
             </tr>
           </thead>
           <tbody id="databody">
           <% int i = 0; %>
            <s:iterator value="deliverDataList" status="status">
            
             <tr id="dataRow<%= i %>" > 
               <td id="dataTd0" style="text-align:center"><input id="deliverchkbox" name="deliverchkbox" type="checkbox" value="<s:property value='BIN_PromotionDeliverID'/>_<s:property value='TaskInstanceID'/>"/></td>               
               <td id="dataTd1" style="text-align:center"><%= i+1 %></td>
               <td id="dataTd2"><s:property value='DeliverReceiveNoIF' /></td>               
               <td id="dataTd4"><s:property value='DepartName' /></td>
               <td id="dataTd5"><s:property value='EmployeeName' /></td>    
               <td id="dataTd7" style="text-align:right"><s:property value='TotalQuantity' /></td>
               <td id="dataTd8"><s:property value='DeliverDate' /></td>
             </tr>
             <%  i += 1; %>
            </s:iterator>  
                    
           </tbody>
         </table>
         <input type="hidden" value="" name="csrftoken" id="csrftoken">
		 </s:form > 
         </div>
		</div>
		<div class="center clearfix" style="margin-top:15px">
		    <button class="confirm" type="button" onclick="endSelect()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="btnPopOk"/></span></button>
		</div>
	</div>
</div>
</div>
<script type="text/javascript">
$("#databody tr:odd").addClass("even");
$("#databody tr:even").addClass("odd");
$("#csrftoken").val(window.opener.document.getElementById("csrftoken").value);
</script>
</s:i18n>