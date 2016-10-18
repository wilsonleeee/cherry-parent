<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- ==================== 编码条码修改履历 ====================== --%>
<style type="text/css">
th {
	background: #eee;
}
.commons {
	color:#3366FF;
}
.commons:hover {
	color: #ff6d06;
    text-decoration: underline;
}
</style>
<s:i18n name="i18n.pt.BINOLPTJCS06">
<div id="tabs-6">
   <div class="section">
      <div class="section-content">
        <s:iterator value="prtBCHistoryList">
        	<span> 
        		<s:text name="JCS06.barCode"/>:
       			<span class="<s:if test='validFlag.equals("0")'>gray </s:if>">
       				<strong><s:property value="barCode"/></strong>
     			</span>
       		</span>
			<table cellpadding="0" cellspacing="0" width=100% style="margin-bottom:15px;">
				<thead>
					<tr>
						<th style="width:5%"  class="center" rowspan="2"><span>NO.</span></th>
						<th style="width:30%" class="center" colspan="2"><s:text name="JCS06.unitCode"/></th>
						<th style="width:30%" class="center" colspan="2"><s:text name="JCS06.barCode"/></th>
			        	<th style="width:30%" class="center" rowspan="2"><s:text name="JCS06.updateTime"/></th>
			        </tr>
					<tr>
						<th class="center"><s:text name="JCS06.beforeUpdate"/></th>
						<th class="center"><span class="red"><s:text name="JCS06.afterUpdate"/></span></th>
						<th class="center"><s:text name="JCS06.beforeUpdate"/></th>
						<th class="center"><span class="red"><s:text name="JCS06.afterUpdate"/></span></th>
					</tr>
				</thead>
				<s:iterator value="list" status="status">
				<tbody>
			     <tr id="${status.index + 1}" class="<s:if test='#status.odd == true'>odd</s:if><s:else>even</s:else>" > 
			     	<td style="width:5%"><span>${status.index + 1}</span></td>
			          <td><span><s:property value="oldUnitCode"/></span></td>
			          <s:if test='!oldUnitCode.equals(unitCode)'>
			          	  <td ><span class="red"><s:property value="unitCode"/></span></td>
			          </s:if>
			          <s:else>
				          <td><span><s:property value="unitCode"/></span></td>
			          </s:else>
			          <td><span><s:property value="oldBarCode"/></span></td>
			          <s:if test='!oldBarCode.equals(barCode)'>
				          <td><span class="red"><s:property value="barCode"/></span></td>
			          </s:if>
			          <s:else>
				          <td><span><s:property value="barCode"/></span></td>
			          </s:else>
			          <td class="center"><span><s:property value="updateTime"/></span></td>
			        </tr>
			    </tbody>
				
				
				</s:iterator>
		   </table>
        </s:iterator>
      </div>
    </div>
</div>
</s:i18n>
