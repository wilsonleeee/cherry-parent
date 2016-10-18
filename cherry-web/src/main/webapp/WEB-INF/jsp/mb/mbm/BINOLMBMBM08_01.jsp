<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM03.js"></script>
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
<s:i18n name="i18n.mb.BINOLMBMBM03">

	  <div class="crm_content_header">
		  <span class="icon_crmnav"></span>
		  <span><s:text name="binolmbmbm03_mainTenanceResume"/></span>
	  </div>
      <div class="panel-content">
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span>
              <%-- 历史记录--%>
              <s:text name="binolmbmbm03_updateTab"/></strong></div>
              <div class="section-content" style="width:100%;overflow-x:auto;">
            <table width="100%" cellspacing="0" cellpadding="0" border="0" class="jquery_table" id="dataTable1">
              <thead>
                <tr>
				 <th width="15%"  class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_OperatTime"/></strong><%-- 操作时间 --%>
				 </th>
				 <th width="15%"  class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_tradeNo"/></strong><%-- 单据号 --%>
				 </th>
                 <th width="15%"  class="center" colspan="2">
                 <strong><s:text name="binolmbmbm03_pointsRepair"/></strong><%-- 总积分值  --%>
                 </th>	
                 <th width="8%"  class="center" rowspan="2">
                 <strong><s:text name="binolmbmbm03_pointsMaintain"/></strong><%-- 积分差值  --%>
                 </th>	
				 <th width="12%" class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_employee"/></strong><%-- 操作员 --%>
				 </th>	
				   <th width="15%"  class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_businessTime"/></strong><%-- 操作时间 --%>
				 </th>
				 <th  width="30%" class="center" rowspan="2" >
				 <strong><s:text name="binolmbmbm03_common"/></strong><%-- 修改理由 --%>
				 </th>	
                </tr>
                <tr>
                  <th class="center"><s:text name="binolmbmbm03_beforeUpdate"/></th><%-- 修改前 --%>
                  <th class="center"><span class="red"><s:text name="binolmbmbm03_afterUpdate"/></span></th><%-- 修改后 --%>
                </tr>
              </thead>
              <tbody>
              <s:if test="null != memPointList">
              <ul>
              	<s:iterator value="memPointList" status="status">
                	<tr class="<s:if test='#status.index%2==0'>odd</s:if><s:else>even</s:else>">
					<td class="center"><span><s:property value="businessTime"/></span></td><%--操作时间 --%>
					<td class="center"><span><s:property value="tradeNo"/></span></td><%--操作时间 --%>
	                  <s:if test='"".equals(oldCurPoints)|| null == oldCurPoints'><td class="center"><span></span></td></s:if>
	                  <s:else>
	                 <td class="center"><s:property value="oldCurPoints"/></td> <%--修改前的积分总值--%>
	                  </s:else>
	                  <s:if test='"".equals(curPoints)|| null == curPoints'><td class="center"><span></span></td></s:if>
	                   <s:else>
	                  <td class="center red"><s:property value="curPoints"/></td><%--修改后的积分总值 --%>
	                  </s:else>
	                  <s:if test='usedCount>=0'>
	                  <td class="center"><s:property value="usedCount"/></td> <%--积分差值（正）--%>
	                  </s:if>
	                  <s:else>
	                  <td class="center red"><s:property value="usedCount"/></td> <%--积分差值（负）--%>
	                  </s:else>
					  <td class="center"><s:property value="employeeName"/></td><%-- 操作员 --%>
					  <td class="center"><span><s:property value="updateTime"/></span></td><%--操作时间 --%>
					  <s:if test='"Cherry".equals(Channel)'>
					  <td  >
					  <s:url id="queryReason" action="BINOLMBMBM08_getReason">
							<s:param name="memPointId">${memPointId}</s:param>
					  </s:url>
					 <a class="reason1" style="cursor:pointer;" href="${queryReason}" rel="${queryReason}" title='<s:text name="binolmbmbm03_commondetial"/>'>
						<span class="commons" ><s:property value="messageReason_temp"/></span>
					 </a>
					  </td><%-- 备注--%></s:if>
					  <s:elseif test='"Wechat".equals(Channel)'> 
					  <td > <span style="color:#00CD00"><s:text name="binolmbmbm03_weChat"/></span> </td><%-- 由微信修改--%>
					  </s:elseif>
					  <s:else><td > <span style="color:red"><s:text name="binolmbmbm03_Channel"/></span> </td><%-- 由终端修改--%></s:else>
	                </tr>
                </s:iterator>
                </ul>
                </s:if>
              </tbody>
            </table>
            </div>
           </div>
       </div>
</s:i18n>