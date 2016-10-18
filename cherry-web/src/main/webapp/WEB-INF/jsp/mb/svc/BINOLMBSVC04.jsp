 <%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<script type="text/javascript" src="/Cherry/js/mb/svc/BINOLMBSVC04.js"></script>
<s:i18n name="i18n.mb.BINOLMBSVC04">
<script type="text/javascript">
</script>

<s:url id="update_url" value="/mb/BINOLMBSVC04_update"/>
<s:url id="inti_url" value="/mb/BINOLMBSVC04_init"/>
<a id="intiUrl" href="${inti_url}"></a>
<div class="hide">
	<div id="dialogConfirm"><s:text name="SVC04_dialogConfirm"/></div>
	<div id="dialogCancel"><s:text name="SVC04_dialogCancel"/></div>
	<div id="operatetitle"><s:text name="SVC04_operatetitle" /></div>
	<div id="operateContent"><p class="message"><span><s:text name="SVC04_operateContent" /></span></p></div>
</div>
  <div id="actionResultDisplay"></div>	
	<div id="errorMessage"></div>           

  		  	<div class="panel-header">
	<div class="clearfix">
		<span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
			</div> 

</div>		 

			<div class="panel-content">
			<div class="box">
				<cherry:form id="mainForm" class="inline">				
	            <div class="box-content clearfix">
	              <div style="width:100%;">
	              
                	<p><%--储值卡可用范围--%>
						<label style="width:150px;"><s:text name="SVC04_cardUseRange" /></label>
						<s:text name="global.page.select" id="operatStatus"/>
						<s:if test="rangeMap != null && rangeMap.size() > 0">
							<s:select name="rangeCode" id="rangeCode" list="#application.CodeTable.getCodes('1401')" listKey="CodeKey" listValue="Value" onChange="operateInfo('${update_url}');return false;"  value="%{rangeMap.rangeCode}"/>						   
						</s:if>
						<s:else>
							 <s:select name="rangeCode" id="rangeCode" list="#application.CodeTable.getCodes('1401')" listKey="CodeKey" listValue="Value" onChange="operateInfo('${update_url}');return false;"  value="%{rangeMap.rangeCode}"  headerKey="" headerValue="%{operatStatus}"/>
						</s:else>
	               		
					</p>
					   </div>
                	
	            </div>       
	          </cherry:form>
	        </div>
	</div>
	 <div class="hide" id="dialogInit"></div>
    <%-- ====================== 结果一览结束 ====================== --%>   
    </s:i18n>
