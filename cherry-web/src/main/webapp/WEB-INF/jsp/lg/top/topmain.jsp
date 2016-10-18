<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="com.cherry.cm.cmbeans.CherryTaskInstance" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<script type=text/javascript>
function openProcessImage(tokenID){
	// 新页面地址
	var url = "/Cherry/SHOWPROCESSIMAGEPAGE?tokenId="+tokenID;
	var token = $('#csrftoken').val();
	url += "&csrftoken=" + token;	
	popup(url,{height:650, width:900});	
}

function todoProcess(entryID,billType,billID,proType){	
	var url = "/Cherry/TODOPROCESSINSTANCE?OS_EntryID="+entryID+"&OS_BillID="+billID+"&OS_BillType="+billType+"&OS_ProType="+proType;
	var token = $('#csrftoken').val();
	url += "&csrftoken=" + token;	
	popup(url,{height:650, width:990});
}
function refreshTaskList(){
	cherryAjaxRequest({
		url:"/Cherry/TopGetTaskList",
		callback:refreshTaskListSuccess
	});
}
function refreshTaskListSuccess(msg){
	$('#taskDiv').html(msg);
}
</script>
<cherry:form id="mainForm" onsubmit="javascript:return false;" csrftoken="true">
 <input type="hidden" id="currentUnitID" name="currentUnitID" value="TOP"/>
<s:i18n name="i18n.lg.TOPPAGE1">
<%
	UserInfo userinfo = (UserInfo)request.getSession().getAttribute(CherryConstants.SESSION_USERINFO);
    
%>
<div class="panel ui-corner-all">
      <div class="panel-content" style="padding:2px;">  
      <div class="welcome">
        	<div class="welcome-l">
            <div class="welcome-r">
        	<p class="welcome-t" style="font-size:20px;margin-bottom:5px;"><s:text name="lblWelcome1"/></p>
            <p class="welcome-t" style="font-size:18px;"></p>
            <div class="section" id="taskDiv">
			  <%
			  Object ob  = request.getAttribute("TaskList");
			  if(ob!=null){  	
			  	List<CherryTaskInstance> taskList = (List<CherryTaskInstance>)ob;
			  	int size = 0;
			  	if(taskList!=null){
			  		size = taskList.size();
			  	}
			  %>
          <div class="section-header"><strong><span class="ui-icon icon-agent"></span><s:text name="lblTaskTODO"/><span style="color:red"><%=size %></span>&nbsp;<s:text name="lblTaskUnit"/></strong></div>
          <div class="section-content">
            <table border="0">
			  <tbody>
			  <tr>
			    <th style="text-align:center;width:20px;">No.</th>
			    <th style="text-align:center;width:120px;"><s:text name="lblTaskName"/></th>
			    <th style="text-align:center;width:120px;"><s:text name="lblOrderNo"/></th>
			    <th style="text-align:center;width:120px;"><s:text name="lblDepart"/></th>
			    <th style="text-align:center;width:120px;"><s:text name="lblEmployee"/></th>
			    <!-- <th style="text-align:center;width:50px;"><s:text name="lblImage"/></th> -->
			    <th style="text-align:center;width:100px;"><s:text name="lblToDO"/></th>
			    
			  </tr>
<% 	
     	if(taskList!=null){
     		CherryTaskInstance tempTask;
     		for(int i =0;i<taskList.size();i++){
     			tempTask=taskList.get(i);

    %>
		     <tr>
		    		<td><%=i+1 %></td>		    		
		    		<td><%=tempTask.getTaskName() %></td>
		    		<td><%=tempTask.getBillCode() %></td>
		    		<td><%=tempTask.getDepartCodeNameFrom() %></td>
		    		<td><%=tempTask.getEmployeeCodeName() %></td>
		    		<td><a href="javascript:void(0)" onclick="todoProcess(<%=tempTask.getEntryID()%>,'<%=tempTask.getBillType() %>',<%=tempTask.getBillID() %>,'<%=tempTask.getProType() %>')"><s:text name="lblToDO"/></a></td>
		    		
		     </tr>    	
<%
 		}
 	}     	
}
    %>  
			</tbody>
			</table>            
          </div>
        </div>
       </div>
      </div>
    </div>
  </div>
    </div>
</s:i18n>
</cherry:form>