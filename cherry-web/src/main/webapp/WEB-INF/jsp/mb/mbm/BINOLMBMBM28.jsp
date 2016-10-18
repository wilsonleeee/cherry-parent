<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript" src="/Cherry/js/mb/mbm/BINOLMBMBM28.js"></script>

<s:i18n name="i18n.mb.BINOLMBMBM28">
<s:text name="binolmbmbm28_backButton" id="backButtonText"></s:text>
<s:text name="binolmbmbm28_editButton" id="editButtonText"></s:text>
<s:text name="binolmbmbm28_deleteButton" id="deleteButtonText"></s:text>
<div class="panel-content clearfix">  
<s:if test="%{issueDetailInfo != null}">
<div class="detail_box">
<div id="actionResultDialogDisplay"></div>

<div class="crm_issue_title">
	<span>
	<s:if test='%{issueDetailInfo.issueSummary != null && !"".equals(issueDetailInfo.issueSummary)}'>
	<s:property value="issueDetailInfo.issueSummary"/>
	</s:if>
	<s:else>
	<s:set value="%{issueDetailInfo.description}" var="description"></s:set>
	<cherry:cut length="20" value="${description }"></cherry:cut>
	</s:else>
	(<s:property value="issueDetailInfo.issueNo"/>)</span>
	<s:if test="%{backIssueId != null}">
	<span class="right">
	  <s:url action="BINOLMBMBM28_init" id="backIssueDetailUrl">
		<s:param name="backIssueId" value="backIssueId"></s:param>
		<s:param name="backIssueIdQuene" value="backIssueIdQuene"></s:param>
	  </s:url>
	  <a href="#" onclick="binolmbmbm28.backIssueDetail('${backIssueDetailUrl}');return false;" title="${backButtonText }">
		<span class="ui-icon icon-back"></span>
	  </a>
	</span>
	</s:if>
</div>


<div class="crm_issue_box">

<div class="section">
  <div class="section-header clearfix">
	<div class="icon_arrow_b" onclick="binolmbmbm28.contract(this);">
      <s:text name="binolmbmbm28_issueDetailInfo"/>
	</div>
	<%-- <s:if test="%{issueDetailInfo.speaker == curAuthor}">--%>
		<span class="right">
		  <s:url action="BINOLMBMBM28_editIssueInit" id="editIssueInitUrl">
		    <s:param name="issueId" value="%{issueDetailInfo.issueId}"></s:param>
		  </s:url>
		  <a href="#" onclick="binolmbmbm28.editIssueInit('${editIssueInitUrl}');return false;" title="${editButtonText }">
		    <span class="ui-icon icon-edit"></span>
		  </a>
		  <s:url action="BINOLMBMBM28_delIssue" id="delIssueUrl">
		    <s:param name="issueId" value="%{issueDetailInfo.issueId}"></s:param>
		  </s:url>
		  <a href="#" onclick="binolmbmbm28.delIssue('${delIssueUrl}');return false;" title="${binolmbmbm28_deleteButton }">
		    <span class="ui-icon icon-disable"></span>
		  </a>
	    </span>
    <%--</s:if>	--%>	    
  </div>
  <div class="section-content">
	<div class="crm_issue_boxmargin clearfix">
	<div class="crm_issue_info crm_issue_info_line">
      <div>
	    <label><s:text name="binolmbmbm28_issueType"/>：</label>
	    <span>
	    <s:property value='#application.CodeTable.getVal("1272", issueDetailInfo.issueType)' />&nbsp;&nbsp;
	    <s:property value='#application.CodeTable.getVal("1329", issueDetailInfo.issueSubType)' />
	    </span>
	  </div>
      <div>
	    <label><s:text name="binolmbmbm28_issueStatus"/>：</label>
	    <span><s:property value='#application.CodeTable.getVal("1118", issueDetailInfo.issueStatus)' /></span>
	  </div>
	  <div>
	    <label><s:text name="binolmbmbm28_resolution"/>：</label>
	    <span><s:property value='#application.CodeTable.getVal("1117", issueDetailInfo.resolution)' /></span>
	  </div>
    </div>
    <div class="crm_issue_info crm_issue_info_line">
      <div>
	    <label><s:text name="binolmbmbm28_priority"/>：</label>
	    <span><s:property value='#application.CodeTable.getVal("1271", issueDetailInfo.priority)' /></span>
	  </div>
	  <div>
	    <label><s:text name="binolmbmbm28_assignee"/>：</label>
	    <span><s:property value="issueDetailInfo.assigneeName"/></span>
	  </div>
	  <div>
	    <label><s:text name="binolmbmbm28_speaker"/>：</label>
	    <span><s:property value="issueDetailInfo.speakerName"/></span>
	  </div>
    </div>
	<div class="crm_issue_info">
	  <div>
	    <label><s:text name="binolmbmbm28_createTime"/>：</label>
	    <span><s:property value="issueDetailInfo.createTime"/></span>
	  </div>
	  <div>
	    <label><s:text name="binolmbmbm28_updateTime"/>：</label>
	    <span><s:property value="issueDetailInfo.updateTime"/></span>
	  </div>
	  <div>
	    <s:if test="%{issueDetailInfo.resolution == 0}">		  
		    <label><s:text name="binolmbmbm28_dueDate"/>：</label>
		    <span><s:property value="issueDetailInfo.dueDate"/></span>
	    </s:if>
	    <s:else>
		    <label><s:text name="binolmbmbm28_resolutionDate"/>：</label>
		    <span><s:property value="issueDetailInfo.resolutionDate"/></span>
	    </s:else>
	  </div>
    </div>
	</div>
	<div class="crm_issue_boxmargin clearfix">
	  <div class="crm_issue_info crm_issue_info_other">
	  <div>
	    <label><s:text name="binolmbmbm28_billCode"/>：</label>
	    <span><s:property value="issueDetailInfo.billCode"/></span>
	  </div>
	  <div>
	    <label><s:text name="binolmbmbm28_campaignName"/>：</label>
	    <span><s:property value="issueDetailInfo.campaignName"/></span>
	  </div>
	  <div>
	    <label><s:text name="binolmbmbm28_reIssueNo"/>：</label>
	    <span><a href="#" class="issueLink" style="color: #3366FF;"><s:property value="issueDetailInfo.reIssueNo"/></a></span>
	  </div>
	  </div>
	</div>
  </div>  
</div>  

<div class="section">
  <div class="section-header">
	<div class="icon_arrow_b" onclick="binolmbmbm28.contract(this);">
      <s:text name="binolmbmbm28_description"/>
	</div>
  </div>
  <div class="section-content">
  	<div class="crm_issue_boxmargin"><s:property value="issueDetailInfo.descriptionHtml" escape="false"/></div>
  </div>
</div>  

<div class="section">
  <div class="section-header">
	<div class="icon_arrow_b" onclick="binolmbmbm28.contract(this);">
      <s:text name="binolmbmbm28_issueAction"/>
	</div>
  </div>
  <div class="section-content">
	<s:iterator value="issueDetailInfo.issueActionList" id="issueActionMap">
	<div class="crm_issue_action">
	  <div class="clearfix">
		<span>
		  <s:property value="%{#issueActionMap.assigneeName}"/>
		  <span style="color:#999;margin-left:5px;"><s:text name="binolmbmbm28_addIssueAction" />-<s:property value="%{#issueActionMap.updateTime}"/></span>
		</span>
		<%-- <s:if test="%{#issueActionMap.author == curAuthor}">--%>
		  <span class="right hide" id="operateDiv">
			  <a href="#" onclick="binolmbmbm28.editIssueAction(this);return false;" title="${editButtonText }">
			    <span class="ui-icon icon-edit"></span>
			  </a>
			  <s:url action="BINOLMBMBM28_delIssueAction" id="delIssueActionUrl"></s:url>
			  <a href="#" onclick="binolmbmbm28.delIssueAction(this,'${delIssueActionUrl}')" title="${deleteButtonText }">
			    <span class="ui-icon icon-disable"></span>
			  </a>
		  </span>
		<%--</s:if>--%>
	  </div>
	  <div style="margin:5px 0 0 0" class="crm_issue_boxmargin"><s:property value="%{#issueActionMap.actionBodyHtml}" escape="false"/></div>
	  <div class="hide">
	    <form id="issueActionForm">
		    <s:hidden name="issueId" value="%{issueDetailInfo.issueId}"></s:hidden>
		    <s:iterator value="backIssueIdQuene" var="backIssueIdQueneS">
		    <s:hidden name="backIssueIdQuene" value="%{backIssueIdQueneS}"></s:hidden>
		    </s:iterator>
		    <s:hidden name="issueActionId"></s:hidden>
		    <s:hidden name="oldActionBody" value="%{#issueActionMap.actionBody}"></s:hidden>
		    <s:textarea name="actionBodyAdd" value="%{#issueActionMap.actionBody}" cssStyle="width:90%"></s:textarea>
	    </form>
	    <div style="margin: 5px 0 0 -5px">
		    <s:url action="BINOLMBMBM28_saveIssueAction" id="saveIssueActionUrl"></s:url>
		    <a class="add" href="#" onclick="binolmbmbm28.saveIssueAction(this,'${saveIssueActionUrl}');return false;">
		      <span class="ui-icon icon-enable"></span>
		      <span class="button-text"><s:text name="binolmbmbm28_saveButton"></s:text></span>
		    </a>
		    <a class="add" href="#" onclick="binolmbmbm28.removeIssueAction(this);return false;">
		      <span class="ui-icon icon-disable"></span>
		      <span class="button-text"><s:text name="binolmbmbm28_cancelButton"></s:text></span>
		    </a>
	    </div>
	  </div>
	</div>
	</s:iterator>
	
	<div style="padding: 10px;">
	  <div style="margin-left:-5px">
		<a class="add" href="#" onclick="binolmbmbm28.addIssueAction(this);return false;">
	      <span class="ui-icon icon-add"></span>
	      <span class="button-text"><s:text name="binolmbmbm28_issueAction"></s:text></span>
	    </a>
	  </div>
	  
	  <div class="hide">
	    <div style="margin: 0 0 3px 0;"><s:text name="binolmbmbm28_issueAction"/></div>
	    <form id="issueActionForm">
		    <s:hidden name="issueId" value="%{issueDetailInfo.issueId}"></s:hidden>
		    <s:iterator value="backIssueIdQuene" var="backIssueIdQueneS">
		    <s:hidden name="backIssueIdQuene" value="%{backIssueIdQueneS}"></s:hidden>
		    </s:iterator>
		    <s:if test="%{issueDetailInfo.resolution == 0}">
		    <s:select list='#application.CodeTable.getCodes("1117")' listKey="CodeKey" listValue="Value" name="resolutionAdd"></s:select>
		    </s:if>
		    <s:textarea name="actionBodyAdd" cssStyle="width:90%"></s:textarea>
	    </form>
	    <div style="margin: 5px 0 0 -5px">
		    <s:url action="BINOLMBMBM28_saveIssueAction" id="saveIssueActionUrl"></s:url>
		    <a class="add" href="#" onclick="binolmbmbm28.saveIssueAction(this,'${saveIssueActionUrl}');return false;">
		      <span class="ui-icon icon-enable"></span>
		      <span class="button-text"><s:text name="binolmbmbm28_saveButton"></s:text></span>
		    </a>
		    <a class="add" href="#" onclick="binolmbmbm28.removeIssueAction(this);return false;">
		      <span class="ui-icon icon-disable"></span>
		      <span class="button-text"><s:text name="binolmbmbm28_cancelButton"></s:text></span>
		    </a>
	    </div>
	  </div>
    </div>
  </div>
    
 
</div>  

</div>

</div>
</s:if>
<s:else>
<div class="detail_box">
<div class="actionError">
<ul class="errorMessage"><li><span><s:text name="binolmbmbm28_issueDetailError" /></span></li></ul>
</div>
</div>
</s:else>
</div>

<div class="hide">
  <s:url action="BINOLMBMBM28_init" id="searchIssueDetailUrl">
    <s:param name="backIssueIdQuene" value="backIssueIdQuene"></s:param>
  </s:url>
  <a href="${searchIssueDetailUrl }" id="searchIssueDetailUrl"></a>
  <div id="delIssueActionText"><s:text name="binolmbmbm28_delIssueActionText" /></div>
  <div id="delIssueText"><s:text name="binolmbmbm28_delIssueText" /></div>
</div> 
</s:i18n>

