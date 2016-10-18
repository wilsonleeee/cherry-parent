<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>	 
<s:i18n name="i18n.mb.BINOLMBLEL01">
<div id="reCalcDialogMain" >
<%-- 执行规则重算 --%>
<s:url id="execReCalc_url" action="BINOLMBLEL01_execReCalc"/>
<div id="reCalcDialog" class="hide">
	<div id="reCalcActionResult"></div>
	<div class="box2-active">
        <div class="box2 box2-content ui-widget">
            <p class="gray"><s:text name="lel01.explain1" /></p>
            <p class="gray" style="margin:0px;"><s:text name="lel01.explain2" /></p>
        </div>
    </div>
	<cherry:form id="reCalcDialogForm" class="inline" csrftoken="false">
    <div class="box2 box2-content ui-widget">
        <p><span class="ui-icon icon-arrow-crm"></span><span class="bg_title"><s:text name="lel01.reCalcDateText" /></span>
        <span><s:textfield id="reCalcDate" name="reCalcDate" cssClass="date" cssStyle="width:90px;"/></span>
        </p>
    </div>
	  </cherry:form>
	  <div class="hide">
	<span id="reCalcTitle"><s:text name="lel01.ruleReCalcText" /></span>
	<span id="reCalcConfirm"><s:text name="lel01.sure" /></span>
	<span id="dreCalcCancel"><s:text name="lel01.cancel" /></span>
	<span id="reCalcReconf"><s:text name="lel01.reconf" /></span>
	<a id="execReCalcUrl" href="${execReCalc_url}"></a>
</div>
</div>
</div>
</s:i18n>
