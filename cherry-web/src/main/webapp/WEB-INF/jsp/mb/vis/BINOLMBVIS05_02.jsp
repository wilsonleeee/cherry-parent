<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/mb/vis/BINOLMBVIS05_02.js?V=20160920"></script>
<s:i18n name="i18n.mb.BINOLMBVIS05">
<div class="main container clearfix">
 <div class="panel ui-corner-all">
  <div class="hide">
  	<s:url id="confirm_url" value="/mb/BINOLMBVIS05_save"/>
        <a id="confirm_url" href="${confirm_url}"></a>
  </div>
  <div class="panel-header">
    <div class="clearfix">
       <span class="breadcrumb left"> 
          <span class="ui-icon icon-breadcrumb"></span>
          <s:text name="mbvis05_memVisTitle"/> &gt; <s:text name="mbvis05_visTitle"/>
       </span>
    </div>
  </div>
      <div class="panel-content">
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
          <div id="tabs-1">
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
              <%-- 基本信息 --%>
              <s:text name="global.page.title"/>
              </strong></div>
              <div class="box-content clearfix" id="mainForm">
		            <div class="column" style="width:50%; height: auto;">
		            	<p>
			               	<label><s:text name="mbvis05_employeeCode"/></label>
			          		<s:select name="tradeEmployeeID" list='counterBAList' listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{globalSelect}" />
		              	</p>
		            </div>
		            <div class="column last" style="width:49%; height: auto;">
		            	<p>
				          <label style="width:60px;"><s:text name="mbvis05_visState"/> </label>
				          <s:select list='#application.CodeTable.getCodes("1209")' listKey="CodeKey" listValue="Value" name="visitResult" headerKey="" headerValue="%{#select_default}"></s:select>
				        </p>
		            </div>
		            <div class="hide">
		            	<input name="memCode" value='<s:property value="memCode"/>'/>
		            	<input name="visitTaskId" value='<s:property value="visitTaskId"/>'/>
		            	<input name="startTime" value='<s:property value="startTime"/>'/>
		            	<input name="endTime" value='<s:property value="endTime"/>'/>
		            	<input name="visitType" value='<s:property value="visitType"/>'/>
		            </div>
          	</div>
          	<div class="box-content clearfix">
		            <div class="column" style="width:50%; height: auto;">
		            	<p>
			               	<label><s:text name="mbvis05_memNameTitle"></s:text></label>
			          		<s:property value="memberInfo.name"/>
		              	</p>
		              	<p>
			               	<label><s:text name="mbvis05_memCodeTitle"></s:text></label>
			          		<s:property value="memberInfo.memCode"/>
		              	</p>
		              	<p>
			               	<label><s:text name="mbvis05_memPhoneTitle"></s:text></label>
			          		<s:property value="memberInfo.mobilePhone"/>
		              	</p>
		            </div>
		            <div class="column last" style="width:49%; height: auto;">
		            	<p>
				          <label><s:text name="mbvis05_memVisTypeTitle"></s:text></label>
				          	<s:property value="memberInfo.visitTypeName"/>
				        </p>
				        <p>
				          <label><s:text name="mbvis05_memSendDateTitle"></s:text></label>
				          	<s:property value="memberInfo.joinDate"/>
				        </p>
				        <p>
				          <label><s:text name="mbvis05_memSkinTypeTitle"></s:text></label>
				          	<s:property value="memberInfo.skinType"/>
				        </p>
		            </div>
          	</div>
              <div class="section-content">
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr>
	                  <th><s:text name="mbvis05_buyDate"/></th>
	                  <th><s:text name="mbvis05_proName"/></th>
	                  <th><s:text name="mbvis05_proBarCode"/></th>
	                  <th><s:text name="mbvis05_proUnitCode"/></th>
	                  <th><s:text name="mbvis05_proQuantity"/> </th>
                  </tr>
                  <s:if test="detail_list == null">
                  	<tr>
                  		<td></td>
                  		<td></td>
                  		<td></td>
                  		<td></td>
                  		<td></td>
                  	</tr>
                  </s:if>
                  <s:else>
	                  <s:iterator value="detail_list" id="detail">
	                  	<tr>
	                  		<td><s:property value="CreateTime"/></td>
	                  		<td><s:property value="NameTotal"/></td>
	                  		<td><s:property value="BarCode"/></td>
	                  		<td><s:property value="UnitCode"/></td>
	                  		<td><s:property value="Quantity"/></td>
	                  	</tr>
	                  </s:iterator>
                  </s:else>
                </table>
              </div>
            </div>
            <div class="center clearfix">
            <%-- 促销品编辑按钮 --%>
       		  <button class="edit" onclick="BINOLMBVIS05_02.confirm();return false;" type="button">
       		 			<span class="ui-icon icon-edit-big"></span>
              			<span class="button-text"><s:text name="mbvis05_visConfirm"/> </span>
       		  </button>
       		<button id="close" class="close" type="button"  onclick="window.close();return false;">
            	<span class="ui-icon icon-close"></span>
            	<span class="button-text"><s:text name="global.page.close"/></span>
          	</button>
            </div>
          </div>
        </cherry:form>
      </div>
     </div>
   </div>
</s:i18n>