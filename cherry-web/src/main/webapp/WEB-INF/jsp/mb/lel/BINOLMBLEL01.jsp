<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/mb/lel/BINOLMBLEL01.js"></script>
<%-- 会员等级重算URL --%>
<s:url id="lelReCalcInit_url" value="BINOLMBLEL01_lelReCalcInit"/>
<%-- 会员等级查询URL --%>
<s:url id="search_url" value="BINOLMBLEL01_search"/>
<%-- 会员等级编辑URL --%>
<s:url id="edit_url" value="BINOLMBLEL01_edit"/>
<%-- 会员俱乐部查询URL --%>
<s:url id="searchClub_url" action="BINOLCM05_queryClub" namespace="/common"/>
<s:url id="sendLevel_url" value="BINOLMBLEL01_sendLevel"/>
<s:i18n name="i18n.mb.BINOLMBLEL01">
	<div class="panel-header">
        <div class="clearfix">
        	<span class="breadcrumb left">
        	<span class="ui-icon icon-breadcrumb"></span><s:text name="lel01.title"/> &gt; <s:text name="lel01.subTitle"/></span>
        </div>
    </div>
    <div class="panel-content">
       <div class="section">
            <div class="section-header">
            	<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="lel01.gaoyao" /></strong>
            	<a href="${sendLevel_url}" onclick="javascript:BINOLMBLEL01.sendLevel(this);return false;" class="add right"><span class="ui-icon icon-add"></span>
				 <span class="button-text"><s:text name="lel01.sendLevel" /></span></a>
            	<cherry:show domId="BINOLMBLEL0102">
            	<a class="add right" href="${lelReCalcInit_url }" onclick="openWin(this);return false;"><span class="ui-icon icon-refresh-s"></span>
				 <span class="button-text"><s:text name="lel01.ruleReCalcText" /></span></a>
				</cherry:show>
            </div>
            <div class="section-content">
	    	<cherry:form id="mainForm">
		        <table class="detail">
              	<tbody><tr>
		          <th><label><s:text name="lel01.brand"/></label></th>
		          <td><s:select name="brandInfoId" id="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"
		          	onchange="BINOLMBLEL01.selBrand('%{search_url}');return false;" value="%{brandInfoId}"/>
		          	</td>
		          	<s:if test="clubList != null && !clubList.isEmpty()">
		          	<th><label><s:text name="lel01.memClub" /></label></th>
		          <td><s:select id="memberClubId" name="memberClubId" list="clubList" listKey="memberClubId" listValue="clubName"
		          	onchange="BINOLMBLEL01.search('%{search_url}');return false;" value="%{memberClubId}"/></td>
		          	</s:if>
		          	</tr>
		          	</tbody>
		          	</table>
	        </cherry:form>
            </div>
        </div>
        <div class="section">
          <div class="section-header clearfix">
          	<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><s:text name="lel01.detail"/></strong>
          	<cherry:show domId="BINOLMBLEL0101">
	          	<a class="edit right" onclick="BINOLMBLEL01.edit('${edit_url}');return false;">
	          		<span class="ui-icon icon-edit"></span><span class="button-text"><s:text name="lel01.edit"/></span>
	          	</a>
          	</cherry:show>
          </div>
          <div class="section-content">
          	<table border="0" cellpadding="0" cellspacing="0" width="100%">
          		<thead>
				 	<tr>
                		 <th scope="col" style="width: 4%;"><s:text name="lel01.No"/></th><%-- No. --%>
				         <th scope="col"><s:text name="lel01.name"/></th><%-- 等级名称 --%>
				         <th scope="col"><s:text name="lel01.code"/></th><%-- 等级编码 --%>
				         <th scope="col"><s:text name="lel01.grade"/></th><%-- 等级级别 --%>
				         <th scope="col"><s:text name="lel01.levelDate"/></th><%-- 会员有效期 --%>
				         <th scope="col"><s:text name="lel01.despt"/></th><%-- 等级描述 --%>
				    </tr>
				</thead>
				<tbody id="lelBody">
					<%-- ======================= 会员等级一览开始  ============================= --%>
		          	<jsp:include page="/WEB-INF/jsp/mb/lel/BINOLMBLEL01_01.jsp" flush="true"/>
		          	<%-- ======================= 会员等级一览结束  ============================= --%>
				</tbody>
	        </table>
		  </div>
	   </div>
   </div>
   <div class="hide" id="dialogSendLevel"></div>
   <%-- ======================= 规则重算设定POP开始  ============================= --%>
<jsp:include page="/WEB-INF/jsp/mb/lel/BINOLMBLEL01_05.jsp" flush="true"/>
<%-- ======================= 规则重算设定POP结束  ============================= --%>
   <input type="hidden" id="search_url" value="${search_url}"/>
   <div class="hide">
     	<a id="searchClubUrl" href="${searchClub_url}"></a>
     </div>
</s:i18n>