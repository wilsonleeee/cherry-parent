<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS47.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<s:i18n name="i18n.pt.BINOLPTJCS45">
<s:text id="global.select" name="global.page.select"/>
<s:text name="CHA03_select" id="CHA03_select"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
<cherry:form id="toDetailForm" action="BINOLPTJCS49_init" method="post" csrftoken="false">
       	<input type="hidden" name="productPriceSolutionID" value='<s:property value="productPriceSolutionID"/>'/>
       	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
</cherry:form>
  <div class="clearfix"> 
   <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span>
   		<s:if test='"0".equals(isPosCloud)'>
   			<s:text name="solu_title1" />&nbsp;&gt;&nbsp;
   			<s:text name="JCS47_detail"></s:text>
	    </s:if>
	    <s:elseif test='"1".equals(isPosCloud)'>
	    	<s:text name="solu_prtlisttitle1" />&nbsp;&gt;&nbsp;
	    	<s:text name="JCS47_editlistdetail"></s:text>
	    </s:elseif>
   </span> 
    <%-- <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="CHA03_title1" />&nbsp;&gt;&nbsp;<s:text name="CHA03_edit"></s:text></span>--%> 
  </div>
</div>
<div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="section">
  <div class="section-header">
	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="solu_baseInfo"></s:text></strong>
  </div>
  <div class="section-content">
  <cherry:form id="update" csrftoken="false" onsubmit="save();return false;">
    <input type="hidden" name="productPriceSolutionID" value='<s:property value="productPriceSolutionID"/>'/>
    <s:hidden name="soluUpdateTime" value="%{productPriceSolutionInfo.soluUpdateTime}"></s:hidden>
	<s:hidden name="soluModifyCount" value="%{productPriceSolutionInfo.soluModifyCount}"></s:hidden>
    <s:if test='"0".equals(isPosCloud)'>
    <table class="detail" id="detailId" cellpadding="0" cellspacing="0">
  	  <tr>
        <th><s:text name="solu_solutionName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="solutionName" cssClass="text" value="%{productPriceSolutionInfo.solutionName}" maxlength="50"/></span></td>
        <th><s:text name="solu_solutionCode"></s:text></th>
        <td> 
		<span><s:property value="productPriceSolutionInfo.solutionCode"/></span>
		<s:hidden id="unitCode" name="solutionCode" value="%{productPriceSolutionInfo.solutionCode}"></s:hidden>
        </td>  
      </tr>
      <tr>
        <th><s:text name="solu_startDate"></s:text></th>
	    <td><span><s:textfield name="startDate" cssClass="date startTime" value="%{productPriceSolutionInfo.startDateYMD}"></s:textfield></span></td>
        <th><s:text name="solu_endDate"></s:text></th>
	    <td><span><s:textfield name="endDate" cssClass="date endTime" value="%{productPriceSolutionInfo.endDateYMD}"></s:textfield></span></td>
	  </tr>
	  <tr>
        <th><s:text name="solu_comments"></s:text></th>
	    <td><span><s:textfield name="comments" cssClass="text" value="%{productPriceSolutionInfo.comments}"></s:textfield></span></td>
        <th><s:text name="solu_isSynchProductPrice"></s:text></th>
        <td><span><s:select list='#application.CodeTable.getCodes("1416")' listKey="CodeKey" listValue="Value" name="isSynchProductPrice" value="%{productPriceSolutionInfo.isSynchProductPrice}"></s:select></span></td>
	       
	  </tr>
    </table>    
    </s:if>
    <s:elseif test='"1".equals(isPosCloud)'>
    <table class="detail" id="detailId" cellpadding="0" cellspacing="0">
  	  <tr>
        <th><s:text name="solu_prtlistName"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
        <td><span><s:textfield name="solutionName" cssClass="text" value="%{productPriceSolutionInfo.solutionName}" maxlength="50"/></span></td>
        <th><s:text name="solu_prtliststartDate"></s:text><span class="highlight"><s:text name="global.page.required"></s:text></span></th>
	    <td><span><s:textfield name="startDate" cssClass="date startTime" value="%{productPriceSolutionInfo.startDateYMD}"></s:textfield></span></td>
      </tr>
      <tr>
        <th><s:text name="solu_prtlistCode"></s:text></th>
        <td> 
		<span><s:property value="productPriceSolutionInfo.solutionCode"/></span>
		<s:hidden id="unitCode" name="solutionCode" value="%{productPriceSolutionInfo.solutionCode}"></s:hidden>
        </td>
        <th><s:text name="solu_prtlistendDate"></s:text></th>
	    <td><span><s:textfield name="endDate" cssClass="date endTime" value="%{productPriceSolutionInfo.endDateYMD}"></s:textfield></span></td>
	  </tr>
	  <tr>
 		<th><s:text name="solu_prtlistcomments"></s:text></th>
	    <td><span><s:textfield name="comments" cssClass="text" value="%{productPriceSolutionInfo.comments}"></s:textfield></span></td>
        <th> </th>
        <td><span> </span></td> 
	  </tr>
    </table> 
    </s:elseif>
 </cherry:form>  
  </div>
</div> 
 <div class="center clearfix" id="pageButton">
          <button class="save" onclick="save();return false;">
          <s:a action="BINOLPTJCS47_save" id="JCS47_save" cssStyle="display: none;"></s:a>
          <span class="ui-icon icon-save"></span>
          <span class="button-text"><s:text name="global.page.save"/></span>
          </button>
          <button id="back" class="back" type="button" onclick="doBack()">
           	<span class="ui-icon icon-back"></span>
           	<span class="button-text"><s:text name="global.page.back"/></span>
          </button>
          <button class="close" onclick="window.close();">
          	<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="global.page.close"/></span><%-- 关闭 --%>
          </button>
    </div>
</div>
</div>
</div>
</div>
</s:i18n> 