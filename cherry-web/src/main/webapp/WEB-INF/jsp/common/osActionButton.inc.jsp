<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.opensymphony.user.User,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow,
                 com.opensymphony.workflow.spi.Step,
                 java.util.*,
                 com.opensymphony.workflow.loader.WorkflowDescriptor,
                 com.opensymphony.workflow.loader.ActionDescriptor"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
<!--
function doaction(entryid,actionid){
	var curl = $("#osdoactionUrl").attr("href");
	curl=curl +"?entryid="+entryid+"&actionid="+actionid;
	var params=$('#mainForm').formSerialize();
	cherryAjaxRequest({
		url:curl,
		param:params,
		callback:function(msg){		
		  if(window.opener.oTableArr[1] != null)window.opener.oTableArr[1].fnDraw();
		  if($("#errorDiv2")) $("#errorDiv2").hide();
        }
	});
}
//-->
</script>
 <% 
     Object ob = request.getAttribute("ActionDescriptor");            
 if(ob!=null){
     ActionDescriptor[] adArr = (ActionDescriptor[])ob;
     for(int i=0;i<adArr.length;i++){
     	String actionName = adArr[i].getName();
     	String[] arr = actionName.split("_");
     	String buttonClass = "icon-close";
     	if(arr.length>1){
     		if("save".equals(arr[1])){
     			buttonClass="icon-save";
     		}else if("confirm".equals(arr[1])){
     			buttonClass="icon-confirm";
     		}else if("inhibit".equals(arr[1])){
     			buttonClass="icon-inhibit";
     		}else if("close".equals(arr[1])){
     			buttonClass="icon-close";
     		}
     	}
     	
 %>
       <button class="close" onclick='doaction(<s:property value="workFlowID"/>,<%= adArr[i].getId() %>);return false;'>
     	
     	<span class='ui-icon <%= buttonClass%>'></span>   
     	  <%if("os.agree".equals(arr[0])){ %>               
         <span class="button-text"><s:text name="os.agree"/></span>
         <%}else if("os.inhibit".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.inhibit"/></span>
          <%}else if("os.save".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.save"/></span>
          <%}else if("os.delete".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.delete"/></span>
         <%}else if("os.submit".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.submit"/></span>
         <%}else if("os.send".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.send"/></span>
         <%}else if("os.receive".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.receive"/></span>
         <%}else if("os.allocationout".equals(arr[0])){ %> 
         <span class="button-text"><s:text name="os.allocationout"/></span>
         <%} %>
        </button>    
 <% 
     }
 }
 %>