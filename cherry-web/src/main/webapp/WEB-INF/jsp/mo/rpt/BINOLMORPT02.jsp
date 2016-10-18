<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<s:set id="CHECKPAPER_QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.CHECKPAPER_QUESTIONTYPE_SINCHOICE %></s:set>

<script type="text/javascript" src="/Cherry/js/mo/rpt/BINOLMORPT02.js"></script>

<s:i18n name="i18n.mo.BINOLMORPT02">
<div class="panel ui-corner-all">
<div class="main clearfix">
	<div class="panel-header">
	  <div class="clearfix"> 
	    <span class="breadcrumb left"> 
	      <span class="ui-icon icon-breadcrumb"></span>
	      <s:text name="binolmorpt02.rptTitle"/>&gt; <s:text name="binolmorpt02.rptName"/> 
	    </span>
	  </div>
	</div>
    <div class="panel-content" >
      <div class="section">
        <div class="section-header">
          <strong><span class="ui-icon icon-ttl-section-info-edit"></span><s:text name="global.page.title"/></strong>
        </div>
	    <div class="section-content clearfix">
	      <table class="detail" cellpadding="0" cellspacing="0">
            <tr>
		      <th><s:text name="binolmorpt02.paperName"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.paperName"/></span></td>
		      <th><s:text name="binolmorpt02.totalPoint"></s:text></th>
		      <td><span class="highlight"><s:property value="checkAnswerMap.totalPoint"/></span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt02.departName"></s:text></th>
		      <td><span>
		        <s:if test='%{checkAnswerMap.departCode != null && !"".equals(checkAnswerMap.departCode)}'>
			  	  (<s:property value="checkAnswerMap.departCode"/>)<s:property value="checkAnswerMap.departName"/>
			    </s:if>
		      </span></td>
		      <th><s:text name="binolmorpt02.pointLevel"></s:text></th>
		      <td><span class="highlight"><s:property value="checkAnswerMap.pointLevel"/></span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt02.employeeName"></s:text></th>
		      <td><span>
		      	<s:if test='%{checkAnswerMap.employeeCode != null && !"".equals(checkAnswerMap.employeeCode)}'>
			  	  (<s:property value="checkAnswerMap.employeeCode"/>)<s:property value="checkAnswerMap.employeeName"/>
			    </s:if>
		      </span></td>
		      <th><s:text name="binolmorpt02.advice"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.advice"/></span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt02.ownerName"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.ownerName"/></span></td>
		      <th><s:text name="binolmorpt02.orderImprove"></s:text></th>
		      <td><span>
		      <s:if test="%{checkAnswerMap.orderImprove == 1}"><s:text name="binolmorpt02.orderImprove.yes" /></s:if>
		      <s:else><s:text name="binolmorpt02.orderImprove.no" /></s:else>
		      </span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt02.checkDate"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.checkDate"/></span></td>
		      <th><s:text name="binolmorpt02.orderImproveLastDate"></s:text></th>
		      <td><span>
		      <s:if test="%{checkAnswerMap.orderImprove == 1}"><s:property value="checkAnswerMap.orderImproveLastDate"/></s:if>
		      </span></td>
		    </tr>
          </table>
	    </div>
	  </div>  
      <div class="section">
       	<div class="section-header">
            <strong><span class="ui-icon icon-ttl-section-info-edit"></span><s:text name="binolmorpt02.answerTitle"/></strong>
        </div>
        <div class="section-content clearfix" id="questionDetail">
       	  <s:iterator value="%{checkAnswerMap.checkQuestionList}" id="checkQuestionGroupMap">
      		<div id="${checkQuestionGroupMap.checkQuestionGroupId}">
				<div class="group-header clearfix" style="cursor:pointer" onclick="showQuestion(this);">
				  <strong><s:property value="%{#checkQuestionGroupMap.groupName}"/></strong>
				</div>
				<div class="group-content clearfix">
				  <s:iterator value="%{#checkQuestionGroupMap.list}" id="checkQuestionMap" status="status">
					<div class="clearfix" style="line-height: 26px;">
					  <div class="clearfix">
					  	<span>${status.index+1}：</span>
					  	<span>
					  	  <s:property value="%{#checkQuestionMap.questionItem}"/>
					  	  (<s:property value="%{#checkQuestionMap.minPoint}"/>-<s:property value="%{#checkQuestionMap.maxPoint}"/>)
					  	  <s:if test='%{#checkQuestionMap.answer != null && !"".equals(#checkQuestionMap.answer)}'>
					      	<span class="highlight"><s:text name="binolmorpt02.totalPoint" />：<s:property value="%{#checkQuestionMap.point}"/></span>
					      </s:if>
					  	</span>
					  </div>
					  <s:if test="%{#checkQuestionMap.questionType == #CHECKPAPER_QUESTIONTYPE_SINCHOICE}">
					    <div class="clearfix" style="line-height: 20px;margin-left: 25px;">
					      <s:iterator value="%{#checkQuestionMap.answerList}" id="answerMap" status="status">
					        <span class="clearfix">
					          <input type="radio" id="radio_${status.index }" disabled="disabled" <s:if test="%{#answerMap.checkStatus == 1}">checked</s:if>/>
					          <s:property value="%{#answerMap.answer}"/>(<s:property value="%{#answerMap.point}"/>)
					        </span>
					      </s:iterator>
					    </div>  
					  </s:if>
					</div>
				  </s:iterator>
				</div>
			</div>
			<hr class="space"/>
       	  </s:iterator>
        </div>
      </div>
    
		<%-- 操作按钮 --%>
		<div id="button" class="center clearfix">
		  <button id="close" class="close" type="button"  onclick="window.close();return false;">
		    <span class="ui-icon icon-close"></span>
		    <span class="button-text"><s:text name="global.page.close"/></span>
		  </button>
		</div>
		<hr class="space" />
	</div>
  </div>
</div>
</s:i18n>