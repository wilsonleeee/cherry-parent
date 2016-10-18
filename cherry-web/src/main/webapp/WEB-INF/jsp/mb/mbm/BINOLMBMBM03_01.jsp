<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM03_01.js"></script>
<s:url id="save_url" action="BINOLMBMBM03_save"></s:url>
<s:i18n name="i18n.mb.BINOLMBMBM03">
<s:text name="global.page.select" id="global.page.select"></s:text>

   <div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm03_memberMaintenance"/></span>
   </div>
   <div id="actionResultDisplay"></div>
   
      <div class="panel-content" id="div_main">
         <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
           <%--历史记录--%>
           <s:text name="global.page.title"/>
           </strong></div>
       <div class="section-content">
        <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
		        <div class="hide">
		        <s:textfield  name="memberInfoId" cssClass="text" value="%{memberInfoMap.memberInfoId}"/>
		         <s:textfield  name="memCode" cssClass="text" value="%{memberInfoMap.MemCode}" />
				<s:textfield  name="memInfoUdTime" cssClass="text" value="%{memberInfoMap.memInfoUdTime}" />
				<s:textfield  name="memInfoMdCount" cssClass="text" value="%{memberInfoMap.memInfoMdCount}" />
				<s:textfield  name="extInfoUdTime" cssClass="text" value="%{memberInfoMap.extInfoUdTime}" />
				<s:textfield  name="extInfoMdCount" cssClass="text" value="%{memberInfoMap.extInfoMdCount}" />
				</div>
                <input type="password" style="display: none;"/>
                <table class="detail" cellpadding="0" cellspacing="0">
                  <tr> <%--名称--%>
                  <th><s:text name="binolmbmbm03_name"/></th>
                    <td><span><s:property value="memberInfoMap.Name"/></span></td>
                	   <%--会员卡号--%>
                  <th><s:text name="binolmbmbm03_memCode"/></th>
                    <td><span><s:property value="memberInfoMap.MemCode"/></span></td>
                  </tr>
                  <tr> <%--性别--%>
                  <th><label><s:text name="binolmbmbm03_gender"/></label></th>
                    <td><span><s:property value='#application.CodeTable.getVal("1006", memberInfoMap.Gender)'/></span></td>
                  <th><%--手机--%>
                      <s:text name="binolmbmbm03_mobilePhone"/></th>
                    <td><span><s:property value="memberInfoMap.MobilePhone"/></span></td>
                  </tr>
                </table>
                <div class="section-header"><strong><span class="ui-icon icon-ttl-section-info"></span>
           		<%-- 会员当前属性 --%>
              <s:text name="binolmbmbm03_attribute"/></strong></div>
              <div class="section-content" id="memInfo">
                 <table class="detail" cellpadding="0" cellspacing="0">
                 <s:if test='%{!"3".equals(clubMod)}'>
                 <tr ><%--会员俱乐部--%>
                  <th style="width: 15%"><label><s:text name="binolmbmbm03_memClub" /></label></th>
				    <td style="width: 85%"><span>
				    	<s:if test='%{clubList != null && clubList.size() != 0}'>
				    	<s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="changeMB03Level();return false;" Cssstyle="width:150px;"/>
                       	</s:if>
                       	</span>
                       </td>
                   </tr>
                   </s:if>
                  <s:iterator value="memberConfiglist" status="status">
                  <s:if test='"1040".equals(ConfigCode)&&"1".equals(ConfigValue)&&"1".equals(configvalue2)'>
                  <tr ><%--会员等级--%>
                  <th style="width: 15%"><label><s:text name="binolmbmbm03_levelName"/><span class="highlight">*</span></label></th>
				    <td style="width: 85%"><span><s:select cssStyle="width:120px;" id="memberLevelId"  name="memberLevelId" list="memberLevellist" listKey="memberLevelId" listValue="levelName"
                       	tabindex="15" headerKey="" headerValue="%{global.page.select}" value="memberInfoMap.memberLevelId"/>
                       	<s:hidden name="oldmemberLevelId" id="oldmemberLevelId"/>
                       	</span>
                       </td>
                   </tr>
                   </s:if>   
          		  <s:elseif test='"1040".equals(ConfigCode)&&"1".equals(ConfigValue)&&"0".equals(configvalue2)'>
          		  <tr ><%--会员等级--%>
                  <th style="width: 15%"><label><s:text name="binolmbmbm03_levelName"/></label></th>
				    <td style="width: 85%"><span><s:label cssStyle="width:120px;"  name="memberLevelId" value="%{memberInfoMap.LevelName}"/>
                       	<s:hidden name="oldmemberLevelId" value='"memberInfoMap.memberLevelId"'/>
                       	</span>
                       </td>
                   </tr>
          		 </s:elseif>
          		 <s:else>
          		 </s:else>
          		 <%-- 
          		 <s:if test='"1046".equals(ConfigCode)&&"1".equals(ConfigValue)&&"1".equals(configvalue2)'>
          		 <tr>
                    <th><%--入会时间--%><%-- 
                      <s:text name="binolmbmbm03_joinDate"/><span class="highlight">*</span></th>
                      <td ><span><s:textfield  cssStyle="width:100px;" name="joinDateShow" cssClass="date" value="%{memberInfoMap.joinDate}" />
                       <s:hidden name="oldjoinDate"/>
                        <s:hidden name="joinDate"/>
                      </span>
                      </td> 
                 </tr>
                </s:if>
                <s:elseif test='"1046".equals(ConfigCode)&&"1".equals(ConfigValue)&&"0".equals(configvalue2)'>
          		  <tr>
                    <th><%--入会时间--%><%-- 
                      <s:text name="binolmbmbm03_joinDate"/><span class="highlight"></span></th>
                      <td ><span><s:label cssStyle="width:100px;" name="joinDateShow" value="%{memberInfoMap.joinDate}" />
                       <s:hidden name="oldjoinDate"/>
                       <s:hidden name="joinDate"/>
                      </span>
                      </td> 
                 </tr>
          		 </s:elseif>
          		 --%>
                 <s:else>
          		 </s:else>
  		           <s:if test='"1042".equals(ConfigCode)&&"1".equals(ConfigValue)&&"1".equals(configvalue2)'>
                  <tr>
                  <th style="width: 15%"><%--化妆次数--%><s:text name="binolmbmbm03_cosmeticTimes"/></th>
                    <td style="width: 85%"><span><s:textfield cssStyle="width:115px;" maxlength="20" onchange="MBM03_Number(this)" name="btimes" cssClass="text" value="%{memberInfoMap.btimes}" tabindex="20"/>
                    <s:hidden name="oldbtimes"/>
                    </span> 
                    </td>
                  </tr>
                  </s:if>
                  <s:elseif test='"1042".equals(ConfigCode)&&"1".equals(ConfigValue)&&"0".equals(configvalue2)'>
          		   <tr>
                  <th style="width: 15%"><%--化妆次数--%><s:text name="binolmbmbm03_cosmeticTimes"/></th>
                    <td style="width: 85%"><span><s:label cssStyle="width:115px;" name="btimes"  value="%{memberInfoMap.btimes}" />
                    <s:hidden name="oldbtimes"/>
                    </span> 
                    </td>
                  </tr>
          		 </s:elseif>
                   <s:else>
          		 </s:else>
          		  <s:if test='"1044".equals(ConfigCode)&&"1".equals(ConfigValue)&&"1".equals(configvalue2)'>
                 <tr><%--累计金额 --%>
                  <th style="width: 15%"><label><s:text name="binolmbmbm03_addamount"/></label></th>
			   		  <td style="width: 85%"><span><s:textfield cssStyle="width:115px;" name="totalAmount" maxlength="20" cssClass="text" value="%{memberInfoMap.totalAmount}" />
				   		 <s:hidden name="oldtotalAmount"/></span>
			   		  </td>
			   		  </tr>
			     </s:if>
                  <s:elseif test='"1044".equals(ConfigCode)&&"1".equals(ConfigValue)&&"0".equals(configvalue2)'>
          		  <tr ><%--累计金额 --%>
                  <th style="width: 15%"><label><s:text name="binolmbmbm03_addamount"/></label></th>
			   		  <td style="width: 85%"><span><s:label cssStyle="width:115px;" name="totalAmount"  value="%{memberInfoMap.totalAmount}" />
				   		 <s:hidden name="oldtotalAmount"/></span>
			   		  </td>
			   		  </tr>
          		 </s:elseif>
          		 <s:else>
          		 </s:else>
                 </s:iterator>
                   <tr >
                    <th ><%--备注--%><s:text name="binolmbmbm03_common"/><span class="highlight">*</span></th>
                    <td colspan="3">
                    <span  style="width:95%;" >
                    <s:textfield  maxlength="200" name="comments" cssStyle="width:100%;" cssClass="text"/>
                    </span>
                    </td>
                  </tr>
				</table>
                 </div>
             </cherry:form>
              </div>
                <div class="center clearfix">
            <%--保存--%>
       		  <button id="save" class="save" type="button" onclick="doSave('${save_url}');return false;" >
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="global.page.save"/></span>
              </button>
            </div>
              </div>
          <div class="hide">
	<s:url action="BINOLMBMBM03_changeLevel" id="changeLevel_Url"></s:url>
	<a href="${changeLevel_Url}" id="changeLevelUrl"></a>
</div>
</s:i18n>
<script type="text/javascript">
var holidays = '${holidays }';
$('#joinDateShow').cherryDate({
	
});
</script>
