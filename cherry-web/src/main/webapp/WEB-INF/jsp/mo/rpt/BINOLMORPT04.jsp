<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ page import="com.cherry.mo.common.MonitorConstants" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<s:set id="QUESTIONTYPE_SINCHOICE"><%=MonitorConstants.QUESTIONTYPE_SINCHOICE %></s:set>
<s:set id="QUESTIONTYPE_MULCHOICE"><%=MonitorConstants.QUESTIONTYPE_MULCHOICE %></s:set>

<script type="text/javascript" src="/Cherry/js/mo/rpt/BINOLMORPT04.js"></script>

<s:i18n name="i18n.mo.BINOLMORPT04">
<div class="panel ui-corner-all">
<div class="main clearfix">
	<div class="panel-header">
	  <div class="clearfix"> 
	    <span class="breadcrumb left"> 
	      <span class="ui-icon icon-breadcrumb"></span>
	      <s:text name="binolmorpt04.rptTitle"/>&gt; <s:text name="binolmorpt04.rptName"/> 
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
		      <th><s:text name="binolmorpt04.paperName"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.paperName"/></span></td>
		      <th><s:text name="binolmorpt04.employeeName"></s:text></th>
		      <td><span>
		      	<s:if test='%{checkAnswerMap.employeeCode != null && !"".equals(checkAnswerMap.employeeCode)}'>
			  	  (<s:property value="checkAnswerMap.employeeCode"/>)<s:property value="checkAnswerMap.employeeName"/>
			    </s:if>
		      </span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt04.paperType"></s:text></th>
		      <td><span><s:property value='#application.CodeTable.getVal("1107", checkAnswerMap.paperType)' /></span></td>
		      <th><s:text name="binolmorpt04.answerDate"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.answerDate"/></span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt04.departName"></s:text></th>
		      <td><span>
		        <s:if test='%{checkAnswerMap.departCode != null && !"".equals(checkAnswerMap.departCode)}'>
			  	  (<s:property value="checkAnswerMap.departCode"/>)<s:property value="checkAnswerMap.departName"/>
			    </s:if>
		      </span></td>
		      <th><s:text name="binolmorpt04.memName"></s:text></th>
		      <td><span>
		      <s:if test='%{checkAnswerMap.memCode != null && !"".equals(checkAnswerMap.memCode)}'>
		      (<s:property value="checkAnswerMap.memCode"/>)
		      </s:if>
		      <s:property value="checkAnswerMap.memName"/>
		      </span></td>
		    </tr>
		    <tr>
		      <th><s:text name="binolmorpt04.totalPoint"></s:text></th>
		      <td><span><s:property value="checkAnswerMap.realTotalPoint"/></span></td>
		      <th><s:text name="binolmorpt04.isPoint"></s:text></th>
		      <td><span><s:property value='#application.CodeTable.getVal("1040", checkAnswerMap.isPoint)'/></span></td>
		    </tr>
          </table>
	    </div>
	  </div>  
      <div class="section">
       	<div class="section-header">
            <strong><span class="ui-icon icon-ttl-section-info-edit"></span><s:text name="binolmorpt04.answerTitle"/></strong>
        </div>
        <div class="section-content clearfix" id="questionDetail">
			<div class="group-header clearfix">
			  <strong><s:property value="%{checkAnswerMap.paperName}"/></strong>
			</div>
			<div class="group-content clearfix">
			  <s:iterator value="%{checkAnswerMap.checkQuestionList}" id="checkQuestionMap" status="status">
				<div class="clearfix" style="line-height: 26px;">
				  <div class="clearfix">
				  	<span>${status.index+1}：</span>
				  	<span>
				  	  <s:property value="%{#checkQuestionMap.questionItem}"/>
				  	  <s:if test='%{#checkQuestionMap.answer != null && !"".equals(#checkQuestionMap.answer)}'>
				      	<span class="highlight"><s:text name="binolmorpt04.answer" />：<s:property value="%{#checkQuestionMap.answer}"/>
				      	 <s:if test='%{"1".equals(checkAnswerMap.isPoint)}'>
				      	 <s:if test="%{#checkQuestionMap.questionType == #QUESTIONTYPE_SINCHOICE||#checkQuestionMap.questionType == #QUESTIONTYPE_MULCHOICE}">
				      	(<s:text name="binolmorpt04.totalPoint"></s:text>:<s:property value="%{#checkQuestionMap.point}"/>)
				      	</s:if>
				      </s:if>
				      	</span>
				      </s:if>
				      
				  	</span>
				  </div>
				  <s:if test="%{#checkQuestionMap.questionType == #QUESTIONTYPE_SINCHOICE}">
				    <div class="clearfix" style="line-height: 20px;margin-left: 25px;">
				      <s:iterator value="%{#checkQuestionMap.answerList}" id="answerMap" status="status">
				        <span class="clearfix">
				          <input type="radio" id="radio_${status.index }" disabled="disabled" <s:if test="%{#answerMap.checkStatus == 1}">checked</s:if>/>
				          <s:property value="%{#answerMap.answer}"/>
				        </span>
				      </s:iterator>
				    </div>  
				  </s:if>
				  <s:if test="%{#checkQuestionMap.questionType == #QUESTIONTYPE_MULCHOICE}">
				    <div class="clearfix" style="line-height: 20px;margin-left: 25px;">
				      <s:iterator value="%{#checkQuestionMap.answerList}" id="answerMap">
				        <span class="clearfix">
				          <input type="checkbox" id="checkbox_${status.index }" disabled="disabled" <s:if test="%{#answerMap.checkStatus == 1}">checked</s:if>/>
				          <s:property value="%{#answerMap.answer}"/>
				        </span>
				      </s:iterator>
				    </div>  
				  </s:if>
				</div>
			  </s:iterator>
			</div>
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