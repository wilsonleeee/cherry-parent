<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS21.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS18">
<s:text name="CHA04_select" id="CHA04_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
    <s:if test='"0".equals(isPosCloud)'>
    	<s:text name="solu_title1" />&nbsp;&gt;&nbsp;
	    <s:text name="JCS21_detail"></s:text>
    </s:if>
    <s:elseif test='"1".equals(isPosCloud)'>
    	<s:text name="solu_prtlisttitle1" />&nbsp;&gt;&nbsp;
	    <s:text name="JCS21_addPrtList"></s:text>
    </s:elseif>
    </span>
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="solu_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="add" csrftoken="false" onsubmit="save();return false;">
    <s:if test='"0".equals(isPosCloud)'>
    <table class="detail" id="detailId" cellpadding="0" cellspacing="0">
  	  <tr>
  	  	<th><s:text name="solu_brandName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
  	  	<td><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></span></td>
        <th><s:text name="solu_startDate"></s:text></th>
        <td><span><s:textfield name="startDate" cssClass="date startTime" ></s:textfield></span></td>  
     </tr>
	 <tr> 
        <th><s:text name="solu_solutionName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="solutionName" cssClass="text" maxlength="20"></s:textfield></span></td>
        <th><s:text name="solu_endDate"></s:text></th>
        <td><span><s:textfield name="endDate" cssClass="date endTime" ></s:textfield></span></td>  
      </tr>
      <tr>
        <th><s:text name="solu_solutionCode"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td>
		<span><s:property value="solutionCode"/></span>
		<s:hidden id="unitCode" name="solutionCode" value="%{solutionCode}"></s:hidden>
        </td>

      <th><s:text name="solu_comments"></s:text> </th>
	  <td><span><s:textfield id="comments" name="comments" cssClass="text" maxlength="200"/></span></td>
	  </tr>
    </table>   
    </s:if>
    <s:elseif test='"1".equals(isPosCloud)'>
	    <table class="detail" id="detailId" cellpadding="0" cellspacing="0">
	  	  <tr>
	  	  	
	        <th><s:text name="solu_prtlistName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	        <td>
	        	<span><s:textfield name="solutionName" cssClass="text" maxlength="20"></s:textfield></span>
	        	<span class="hide"><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;"></s:select></span>
        	</td>
	        <th><s:text name="solu_prtliststartDate"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	        <td><span><s:textfield name="startDate" cssClass="date startTime" ></s:textfield></span></td>  
	     </tr>
		 <tr> 
	        <th><s:text name="solu_prtlistcomments"></s:text> </th>
		    <td><span><s:textfield id="comments" name="comments" cssClass="text" maxlength="200"/></span></td>
	        <th><s:text name="solu_prtlistendDate"></s:text></th>
	        <td><span><s:textfield name="endDate" cssClass="date endTime" ></s:textfield></span></td>  
	      </tr>
	      <tr>
	        <th><s:text name="solu_prtlistCode"></s:text> </th>
	        <td>
			  <span><s:property value="solutionCode"/></span>
			  <s:hidden id="unitCode" name="solutionCode" value="%{solutionCode}"></s:hidden>
	        </td>
	        <th></th>
	        <td></td>
		  </tr>
	    </table>   
    </s:elseif>
 </cherry:form>  
  </div>
</div> 


<hr class="space" />
<div class="center">
  <s:a action="BINOLPTJCS21_save" id="JCS21_save" cssStyle="display: none;"></s:a>
  <button class="save" onclick="save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="global.page.save"></s:text></span></button>
  <button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
</div>
</div>
</div>
</div>
</div>
</s:i18n> 