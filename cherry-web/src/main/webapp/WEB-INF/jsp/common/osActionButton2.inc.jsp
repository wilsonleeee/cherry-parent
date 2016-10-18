<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="com.opensymphony.user.User,
                 com.opensymphony.workflow.Workflow,
                 com.opensymphony.workflow.basic.BasicWorkflow,
                 com.opensymphony.workflow.spi.Step,
                 java.util.*,
                 com.opensymphony.workflow.loader.WorkflowDescriptor,
                 com.opensymphony.workflow.loader.ActionDescriptor"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
 <% 
     Object ob = request.getAttribute("ActionDescriptor");            
 if(ob!=null){
     ActionDescriptor[] adArr = (ActionDescriptor[])ob;
     Map metaMap = null;
     for(int i=0;i<adArr.length;i++){
    	 metaMap = adArr[i].getMetaAttributes();
    	String buttonName = "";
     	String buttonClass = "icon-close";
     	String buttonFun = "";
     	String canEditFlag = "";
     	String action = "";
     	Map<String,Object> ruleMap = new HashMap<String,Object>();
     	if(metaMap != null && null!= metaMap.get("OS_ButtonNameCode")){
     		buttonName = metaMap.get("OS_ButtonNameCode").toString();
     	}else{
     		continue;
     	}
     	if(metaMap != null && null!= metaMap.get("OS_ButtonClass")){
     		buttonClass = metaMap.get("OS_ButtonClass").toString();
     	}
     	if(metaMap != null && null != metaMap.get("OS_ButtonScript")){
     	   buttonFun = metaMap.get("OS_ButtonScript").toString();
     	}
     	if(metaMap != null && null != metaMap.get("OS_NextStrutsAction")){
     	   action = metaMap.get("OS_NextStrutsAction").toString();
         }
        if(metaMap != null && null != metaMap.get("OS_ButtonEdit")){
            //如果没有在OS_Rule配置CanEditFlag=true，不显示修改按钮
            if(null != metaMap.get("OS_Rule") && !"".equals(metaMap.get("OS_Rule").toString())){
                ruleMap = (Map<String,Object>) com.googlecode.jsonplugin.JSONUtil.deserialize(metaMap.get("OS_Rule").toString());
                if(null!=ruleMap && null != ruleMap.get("CanEditFlag") && !"".equals(ruleMap.get("CanEditFlag"))){
                   canEditFlag = ruleMap.get("CanEditFlag").toString();
                   if("true".equals(canEditFlag)){
                       buttonFun = metaMap.get("OS_ButtonScript").toString();
                       //onclick时执行doaction带有双引号会有问题，所以要把双引号替换为单引号
                       //另外由于IE不支持单引号的实体名称"&apos;"，这里把双引号替换为单引号的实体编号才能兼容各大浏览器。
                       buttonFun = buttonFun.replaceAll("\"", "&#39;");
                   }else{
                       continue;
                   }
                }
            }else{
                continue;
            }
        }
 %>
       <button type="button" id='btn-<%= buttonClass%>' class="close" onclick='doaction(<s:property value="workFlowID"/>,<%= adArr[i].getId() %>,"<%=buttonFun%>","<%= action%>");return false;'>
     	
     	<span class='ui-icon <%= buttonClass%>'></span>   
     	  <%if("os.agree".equals(buttonName)){ %>               
         <span class="button-text"><s:text name="os.agree"/></span>
         <%}else if("os.inhibit".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.inhibit"/></span>
          <%}else if("os.save".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.save"/></span>
          <%}else if("os.delete".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.delete"/></span>
         <%}else if("os.submit".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.submit"/></span>
         <%}else if("os.send".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.send"/></span>
         <%}else if("os.receive".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.receive"/></span>
         <%}else if("os.allocationout".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.allocationout"/></span>
         <%}else if("os.edit".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.edit"/></span>
         <%}else if("os.createSD".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.createSD"/></span>
         <%}else if("os.trashOD".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.trashOD"/></span>
         <%}else if("os.trash".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.trash"/></span>
         <%}else if("os.confirmRR".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.confirmRR"/></span>
         <%}else if("os.allocationin".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.allocationin"/></span>
         <%}else if("os.confirmGR".equals(buttonName)){ %> 
         <span class="button-text"><s:text name="os.confirmGR"/></span>
         <%}else{ %> 
         <span class="button-text"><s:text name="buttonName"/></span>
         <%} %>
        </button>    
 <% 
     }
 }
 %>