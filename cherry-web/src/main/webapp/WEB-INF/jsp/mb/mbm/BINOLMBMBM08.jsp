<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM03.js"></script>
<s:i18n name="i18n.mb.BINOLMBMBM03">
<s:text name="global.page.select" id="global.page.select"></s:text>

   <div class="crm_content_header">
	  <span class="icon_crmnav"></span>
	  <span><s:text name="binolmbmbm03_mainTain_point"/></span>
   </div>
   <div id="actionResultDisplay"></div>
   <div class="hide">
   <span id="pointUnChanged"><s:text name="binolmbmbm03_pointUnChanged" /></span>
	
	</div>
      <div class="panel-content" id="div_main">
       <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
       <s:url id="updateCurPoint_url" action="BINOLMBMBM03_updateCurPoint"></s:url>
      <div class="hide">
           <s:textfield  name="memberInfoId" cssClass="text" value="%{memberInfoMap.memberInfoId}" />
         	<s:textfield  name="memCode" cssClass="text" value="%{memberInfoMap.MemCode}" />
           <s:textfield  name="pointMdCount" cssClass="text" value="%{memberInfoMap.pointMdCount}" />
            <s:textfield  name="pointUdTime" cssClass="text" value="%{memberInfoMap.pointUdTime}" />
            </div>
               <div id="actionResultDisplay"></div>
			   <div class="hide">
				<div class="actionError" id="actionResultDiv">
				  	<s:actionerror/>			  
				</div>
				</div>
           <div class="section-header">
           <s:if test='%{!"3".equals(clubMod)}'>
           <s:if test='%{clubList != null && clubList.size() != 0}'>
			<label><s:text name="binolmbmbm03_memClub" /></label>
			<s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName" onchange="searchMB03MemInfo();return false;" Cssstyle="width:150px;"/>
			</s:if>
			</s:if>
           <label><s:text name="binolmbmbm03_selectMode"/></label><%-- 选择维护模式 --%>
			<s:select id="pointType" name="pointType" Cssstyle="width:120px;height:20px;"  list='#application.CodeTable.getCodes("1221")' listKey="CodeKey" 
			         listValue="Value" headerKey=""  onchange="checkedRadio(this);return false;"/>
			
           </div>
		   <div class="box-header"></div>
		   <div id="box2content" class="box2-content">
	   			
                </div>
			<div class="box-header"></div>
			<div class="section-header">
				<strong class="left active"> <span class="ui-icon icon-ttl-section-info"></span><s:text name="binolmbmbm03_reasonOrpassword"/></strong>
				</div>
		    <div class="section-content"></div>
		    <div id="pointComment">
			<table cellspacing="0" cellpadding="0" class="detail">
                 <tr>
                    <th style="width:15%;" ><%--备注--%><s:text name="binolmbmbm03_common"/><span class="highlight">*</span></th>
                    <td colspan="3" style="width:85%;">
                    <span  style="width:95%;" >
                    <s:textfield  maxlength="110" name="comments" cssStyle="width:100%;" cssClass="text"/>
                    </span>
                    </td>
                  </tr>
			</table>
				 <div class="center clearfix">
               <%-- 保存按钮 --%>
       		  <button id="save" class="save" type="button" onclick="updateCurPoint('${updateCurPoint_url}');return false;" >
            	<span class="ui-icon icon-save"></span>
            	<span class="button-text"><s:text name="global.page.save"/></span>
              </button>
            </div>
				</div>
	        </cherry:form>
			</div>
			<div class="hide">
	<s:url action="BINOLMBMBM08_searchMemInfo" id="searchMemInfo_Url"></s:url>
	<a href="${searchMemInfo_Url}" id="searchMemInfoUrl"></a>
</div>
          
</s:i18n>

