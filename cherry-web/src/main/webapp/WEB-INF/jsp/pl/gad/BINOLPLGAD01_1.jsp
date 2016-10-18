<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho"/></div>
<div id="iTotalRecords"><s:property value="iTotalRecords"/></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords"/></div>
<%-- ======================此段代码固定 结束======================= --%>
<s:i18n name="i18n.pl.BINOLPLGAD01">
<div id="aaData">
    <s:iterator value="gadgetInfoList" id="gad">
  	  <ul >
  	  	<%-- NO. --%>
  	  	<li><s:property value="RowNumber"/></li>
  	  	<%-- 小工具名称 --%>
  	  	<li><span><s:property value="gadgetName"/></span></li>
  	  	<%--小工具代码 --%>
  	  	<li><span><s:property value="gadgetCode"/></span></li>
  	  	<%-- 所属画面 --%>
  	  	<li><span><s:property value='#application.CodeTable.getVal("1130",pageId)'/></span></li>
  	  	<%-- 操作 --%>
  	  	<li>
  	  		<input type="hidden" id="gadgetInfoId" name="gadgetInfoId" value="<s:property value="gadgetInfoId"/>"/>
            <s:if test="gadgetConfPath != null && gadgetConfPath != ''">
            	<s:url value="%{gadgetConfPath}" id="gadgetConfPathUrl"></s:url>
             	<a href="${gadgetConfPathUrl}" class="add" name="edit" onclick="binOLPLGAD01.popupWin(this);return false;" style="margin-top:-3px;">
              <span class="ui-icon icon-edit"></span>
              <span class="button-text"><s:text name="GAD01_config"/></span>
             	</a>
            </s:if>
            <s:else>
            </s:else>
  	  	</li>
  	  </ul>
    </s:iterator>
</div>
</s:i18n>
