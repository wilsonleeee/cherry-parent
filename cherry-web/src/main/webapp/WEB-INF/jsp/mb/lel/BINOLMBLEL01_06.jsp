<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/mb/lel/BINOLMBLEL01_6.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script> 
<s:i18n name="i18n.mb.BINOLMBLEL01">
<s:url id="execReCalc_url" action="BINOLMBLEL01_execReCalc"/>
<s:text id="selectAll" name="global.page.all"/>
<div class="main container clearfix">
<div class="panel ui-corner-all">
<div id="div_main">
<div class="panel-header">
  <div class="clearfix"> 
    <span class="breadcrumb left"><span class="ui-icon icon-breadcrumb"></span><s:text name="lel01.title" />&nbsp;&gt;&nbsp;<s:text name="lel01.ruleReCalcText"></s:text></span> 
  </div>
</div>
<div class="panel-content">
<div id="actionResultDisplay"></div>
<div id="reCalcActionResult"></div>
	<div class="box2-active">
        <div class="box2 box2-content ui-widget">
            <div class="gray"> <span class="highlight">※ </span><s:text name="lel01.explain1" />,<s:text name="lel01.explain2" /></div>
        </div>
    </div>
<form  class="inline" method="post" id="reCalcDialogForm" >
  <div class="section-header"><span class="ui-icon icon-ttl-section-info"></span>
           <%--重算条件--%>
           <s:text name="lel01.reCalCondition"/>
           </div>
    <div class="section-content" ></div>
    <div class="box2 box2-content clearfix">
    <span class="left" style="width:40%;margin:3px 0;">
			<span class="ui-icon icon-arrow-crm"></span>
			<span class="bg_title"><s:text name="lel01.brand"/></span>
			<span class="gray"><span><s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName"
    	onchange="BINOLMBLEL01.search('%{search_url}');return false;" value="%{brandInfoId}"/></span></span>
		</span>
		<span class="left" style="width:40%;margin:3px 0;">
			<span class="ui-icon icon-arrow-crm"></span>
			<span class="bg_title"><s:text name="lel01.reCalcDateText" /></span>
			<span class="gray"><span><s:textfield  cssStyle="width:100px;" name="reCalcDate" id="reCalcDate" cssClass="date" value="" /></span></span>
		</span>
	</div>
	<div class="section-header">
      <s:text name="lel01.reCalMember"/>
           <select style="150px;height:20px;"  id="selectMode" name="selectMode" onchange="BINOLMBLEL01_6.checkedSelect(this);return false;">
			    <option value="1"> <s:text name="lel01.allMembers"/> </option>
			    <option value="2"> <s:text name="lel01.someMembers"/></option>
			</select>
   <a id="addopenMem" class="add right" onclick="BINOLMBLEL01_6.openMemPopup();">
	<span class="ui-icon icon-add"></span><span class="button-text"><s:text name="lel01.addMembers" /></span>
  </a>
    </div>
    <div class="section-content" ></div>
<div class="section-content" ></div>
<div id="activebox" class="box2-active">
    <div class="box2 box2-content ui-widget">
      <ul id="databody" class="clearfix"></ul>     
    </div>
</div>
</form>
<hr class="space" />
      <div  class="center clearfix">
         <%--保存--%>
	  <button id="savebutton" class="save" type="button" onclick="BINOLMBLEL01_6.doSave('${execReCalc_url}');return false;" >
    	<span class="ui-icon icon-save"></span>
    	<span class="button-text"><s:text name="global.page.save"/></span>
      </button>
        <%--关闭--%>
	  <button id="close" class="close" type="button"  onclick="window.close();return false;">
    	<span class="ui-icon icon-close"></span>
    	<span class="button-text"><s:text name="global.page.close"/></span>
       </button>
     </div>
</div>
 </div>
 
</div>
<div class="hide">
<a href="/Cherry/mb/BINOLMBLEL01_execReCalc.action" id="execReCalcUrl"></a>
<span id="reCalcReconf"><s:text name="lel01.reconf" /></span>
</div>
</div>
</s:i18n> 
<script type="text/javascript">
var holidays = '${holidays }';
$('#reCalcDate').cherryDate({
	
});
</script>