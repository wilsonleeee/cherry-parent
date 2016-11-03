<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<div class="section">
	<div class="section-content">
		<s:if test="logList == null || logList.size() == 0">没有规则日志文件！</s:if>
			
		<s:else>
		<table border="0" cellpadding="0" cellspacing="0" width="100%" style="margin-bottom: 10px;">
	  		<thead>
	    		<tr>
	      			<th width="10%">序号</th>
	      			<th width="30%">日志名称</th>
	      			<th width="20%">创建日期</th>
	      			<th width="20%">日志类型</th>
	      			<th width="20%">文件大小</th>
	    		</tr>
	  		</thead>
	  		<tbody>
	  			<s:iterator value="logList" id="logMap" status="R">
	  				<tr class='<s:if test="%{#R.index%2 == 0}">odd</s:if><s:else>even</s:else>'>
	  					<td><s:property value="#R.index+1"/></td>
	  					<td><a href="<s:property value='fileUrl'/>/logs/" class="popup" onclick="javascript:openLogs(this,'<s:property value='logName'/>','<s:property value="useFlag"/>');return false;"><s:property value="logName"/></a></td>		
	  					<td>
	  						<s:if test='#logMap.createDate !=null && !"".equals(#logMap.createDate)'>
	  						<s:property value="createDate"/>
	  						</s:if>
	  						<s:else>&nbsp;</s:else>
	  					</td>
	  					<td><s:property value="logType"/></td>
	  					<td><s:property value="logSize"/></td>
	  				</tr>
	  			</s:iterator>
	  		</tbody>
		</table>
		</s:else>
 	</div>
</div>