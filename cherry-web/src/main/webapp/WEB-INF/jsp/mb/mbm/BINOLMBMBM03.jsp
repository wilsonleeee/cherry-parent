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
		  <span><s:text name="binolmbmbm03_memHistory"/></span>
	  </div>
      <div class="panel-content">
            <div class="section">
              <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span>
              <%-- 历史记录--%>
              <s:text name="binolmbmbm03_updateTab"/></strong>
              <s:if test='%{!"3".equals(clubMod)}'>
              <s:if test='%{clubList != null && clubList.size() != 0}'>
				<s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="searchMB03Level();return false;" Cssstyle="width:150px;"/>
			 </s:if>
			 </s:if>
			 <input type = "hidden" name="memberInfoId" id="memberInfoId" value='<s:property value="memberInfoId" />'/>
              </div>
              <div class="section-content" style="width:100%;overflow-x:auto;">
             <table width="100%" cellspacing="0" cellpadding="0" border="0" class="jquery_table" id="dataTable">
              <thead>
                <tr>
                 <th width="15%"  class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_OperatTime"/></strong><%-- 业务时间 --%>
				 </th>
				 <s:iterator value="memberConfiglist" status="status">
				  <s:if test='"1040".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                 <th width="15%" class="center" colspan="2">
                 <strong><s:text name="binolmbmbm03_levelName"/></strong><%-- 会员等级 --%>
                 </th>	
                 </s:if>
                  <s:if test='"1042".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                 <th width="10%" class="center" colspan="2">
                 <strong><s:text name="binolmbmbm03_cosmeticTimes"/></strong><%-- 化妆次数--%>
                 </th>
                 </s:if>
                  <s:if test='"1044".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                 <th width="15%" class="center" colspan="2">
                 <strong><s:text name="binolmbmbm03_addamount"/></strong><%-- 累计金额--%>
                 </th>	
                 </s:if>
                <%-- 
                  <s:if test='"1046".equals(ConfigCode)&&"1".equals(ConfigValue)'>	
                 <th width="15%"  class="center" colspan="2">
                 <strong><s:text name="binolmbmbm03_joinDate"/></strong><%-- 入会时间  --%><%-- 
                 </th>	
                 </s:if>
                --%>  
                </s:iterator>
				 <th width="10%" class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_employee"/></strong><%-- 操作员 --%>
				 </th>		
				 <th width="15%"  class="center" rowspan="2">
				 <strong><s:text name="binolmbmbm03_businessTime"/></strong><%-- 操作时间 --%>
				 </th>
				  <th  width="20%" class="center" rowspan="2" >
				 <strong><s:text name="binolmbmbm03_common"/></strong><%-- 修改理由 --%>
				 </th>
                </tr>
                <tr>
                 <s:iterator value="memberConfiglist" status="status">
                 <s:if test='"1040".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                  <th class="center"><s:text name="binolmbmbm03_beforeUpdate"/></th><%-- 修改前 --%>
                  <th class="center"><span class="red"><s:text name="binolmbmbm03_afterUpdate"/></span></th><%-- 修改后 --%>
                  </s:if>
                   <s:if test='"1042".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                  <th class="center"><s:text name="binolmbmbm03_beforeUpdate"/></th><%-- 修改前 --%>
                  <th class="center"><span class="red"><s:text name="binolmbmbm03_afterUpdate"/></span></th><%-- 修改后 --%>
                  </s:if>
                   <s:if test='"1044".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                  <th class="center"><s:text name="binolmbmbm03_beforeUpdate"/></th><%-- 修改前 --%>
                  <th class="center"><span class="red"><s:text name="binolmbmbm03_afterUpdate"/></span></th><%-- 修改后 --%>
                  </s:if>
                   <%-- 
                   <s:if test='"1046".equals(ConfigCode)&&"1".equals(ConfigValue)'>
                  <th class="center"><s:text name="binolmbmbm03_beforeUpdate"/></th><%-- 修改前 --%><%-- 
                  <th class="center"><span class="red"><s:text name="binolmbmbm03_afterUpdate"/></span></th><%-- 修改后 --%><%-- 
                  </s:if>
                  --%>
                  </s:iterator>
                </tr>
              </thead>
              <tbody id="levelBody">
              
              </tbody>
            </table>
            </div>
           </div>
       </div>
       <div class="hide">
	<s:url action="BINOLMBMBM03_searchLevel" id="searchLevel_Url"></s:url>
	<a href="${searchLevel_Url}" id="searchLevelUrl"></a>
</div>
</s:i18n>