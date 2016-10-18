<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>

<s:i18n name="i18n.mb.BINOLMBMBM13">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div class="panel-header">
		    <div class="clearfix">
		       <span class="breadcrumb left"> 
		          <span class="ui-icon icon-breadcrumb"></span>
					<s:text name="binolmbmbm13_memberManage"/> &gt; <s:text name="binolmbmbm13_memInfoRecordInfo"/>
		       </span>
		    </div>
	  	</div>
	  	<div class="panel-content">
	  		<div class="section">
	            <div class="section-header">
	            	<strong>
	            		<span class="ui-icon icon-ttl-section-info"></span>
	            		<s:text name="binolmbmbm13_memInfoRecordInfo"/>
	            	</strong>
	            </div>
	            <div class="section-content">
	                <table class="detail" cellpadding="0" cellspacing="0">
		              	<tr>
			                <th><s:text name="binolmbmbm13_memCode"/></th>
			                <td><span>
			                <s:if test='%{!"ALL".equals(memInfoRecordInfo.memCode)}'>
			                <s:property value="memInfoRecordInfo.memCode"/>
			                </s:if>
			                </span></td>
			                <th><s:text name="binolmbmbm13_modifyTime"/></th>
			                <td><span><s:property value="memInfoRecordInfo.modifyTime"/></span></td> 
		             	</tr>
		             	<tr>
			            	<th><s:text name="binolmbmbm13_modifyCounter"/></th>
			                <td><span><s:property value="memInfoRecordInfo.modifyCounter"/></span></td>
			            	<th><s:text name="binolmbmbm13_modifyType"/></th>
			                <td><span><s:property value='#application.CodeTable.getVal("1241", memInfoRecordInfo.modifyType)' /></span></td>
		             	</tr>
		             	<tr>
			                <th><s:text name="binolmbmbm13_modifyEmployee"/></th>
			                <td><span><s:property value="memInfoRecordInfo.modifyEmployee"/></span></td>	    
			                <th><s:text name="binolmbmbm13_batchNo"/></th>
			                <td><span><s:property value="memInfoRecordInfo.batchNo"/></span></td>		                
		             	</tr>
		             	<tr>
		             		<th><s:text name="binolmbmbm13_sourse"/></th>
			                <td><span><s:property value="memInfoRecordInfo.sourse"/></span></td> 
			                <th><s:text name="binolmbmbm13_remark"/></th>
			                <td><span><s:property value="memInfoRecordInfo.remark"/></span></td>        
		             	</tr>
	         		</table>
	        	</div>
	        </div>
	           
            <%-- 明细信息 --%>
            <div class="section">
	          <div class="section-header">
		          <strong><span class="ui-icon icon-ttl-section-search-result"></span>
		          <s:text name="binolmbmbm13_memInfoRecordDetail" />
		          </strong>
	          </div>
	          <div class="section-content">
	            <table cellpadding="0" cellspacing="0" border="0" class="jquery_table"  width="100%">
	              <tbody>
	                <tr>
	                  <th width="20%"><s:text name="binolmbmbm13_modifyField" /></th>
	                  <th width="40%"><s:text name="binolmbmbm13_oldValue" /></th>
	                  <th width="40%"><s:text name="binolmbmbm13_newValue" /></th>  
	                </tr>
	                <s:iterator value="memInfoRecordInfo.memInfoRecordDetail" id="memInfoRecordDetailMap" status="status">
	                <tr class="<s:if test='#status.odd'>odd</s:if><s:else>even</s:else>">
	                  <td><s:property value='#application.CodeTable.getVal("1242", #memInfoRecordDetailMap.modifyField)' /></td>
	                  <td><s:property value="#memInfoRecordDetailMap.oldValue" /></td>
	                  <td><s:property value="#memInfoRecordDetailMap.newValue" /></td>
	                </tr>
	                </s:iterator>
	              </tbody>
	            </table>
	          </div>
            </div>
            
            <div class="center clearfix">
	            <button class="close" type="button"  onclick="window.close();return false;">
	           		<span class="ui-icon icon-close"></span>
	           		<span class="button-text"><s:text name="global.page.close"/></span>
	         	</button>
            </div>
	  	</div>
	</div>
</div>
</s:i18n>