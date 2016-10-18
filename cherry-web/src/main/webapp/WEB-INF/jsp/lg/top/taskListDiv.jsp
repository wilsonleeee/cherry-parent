<%@ page language="java" pageEncoding="utf-8"%>
<%@ page import="java.util.*" %>
<%@ page import="com.cherry.cm.core.CherryConstants" %>
<%@ page import="com.cherry.cm.cmbeans.UserInfo" %>
<%@ page import="com.cherry.cm.cmbeans.RoleInfo" %>
<%@ page import="com.cherry.cm.cmbeans.CherryTaskInstance" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%> 
<s:i18n name="i18n.lg.TOPPAGE1">
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
</s:i18n>