<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<link rel="stylesheet" href="/Cherry/css/cherry/combobox.css" type="text/css">
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/man/BINOLMOMAN04.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<style>
	.treebox_left{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:550px;
		border-color:#D6D6D6 #D6D6D6 
	}
	.treebox_right{
		background:none repeat scroll 0 0 #FFFFFF;
		border:1px solid #FAD163;
		height:520px;
		border-color:#D6D6D6 #D6D6D6 
	}
	.box2-header strong {
    background: none repeat scroll 0 0 #F8F8F8;
    border: 1px solid #D6D6D6;
    display: block;
    height: 19px;
    padding: 3px 0.5em 4px;
	}
</style>
<s:i18n name="i18n.mo.BINOLMOMAN04">
<div class="panel-header" style = "background:">
     	<%-- ###机器升级 --%>
       <div class="clearfix"> 
	    <span class="breadcrumb left">	    
			<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
		</span>
       </div>
</div>
<div id="actionResultDisplay"></div>
    <%-- ================== 错误信息提示 START ======================= --%>
    <div id="errorDiv2" class="actionError" style="display:none">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>         
    </div>
    <%-- ================== 错误信息提示   END  ======================= --%>
 <div class="panel-content">
	<div class="box-yew">
		<div class="box-yew-header clearfix">
			<strong class="left">
				<span class="ui-icon icon-help-star-yellow"></span>
				<s:text name="MAN04_upgradeExplanation" />
			</strong> 
			<a id="expandCondition" style="margin-left: 25px; font-size: 12px; width: 80px" class="ui-select right"> 
				<span class="button-text choice" style="min-width: 50px;">
					<s:text name="MAN04_moreExplanation" />
				</span> 
				<span class="ui-icon ui-icon-triangle-1-n"></span>
			</a>
		</div>
		<div class="box-yew-content">
			<div class="step-content">
				<label style="margin: 1px 0 0 0px;">1</label>
				<div class="step">
					<s:text name="MAN04_explanation1" />
				</div>
				<div class="line"></div>
			</div>
			<div id="moreCondition" style="display: none;">
				<div class="step-content">
					<label style="margin: 1px 0 0 0px;">2</label>
					<div class="step">
						<s:text name="MAN04_explanation2" />
					</div>
					<div class="line"></div>
				</div>
				<div class="step-content">
					<label style="margin: 1px 0 0 0px;">3</label>
					<div class="step">
						<s:text name="MAN04_explanation3" />
					</div>
					<div class="line"></div>
				</div>
			</div>
		</div>
	</div>
	<div class="box2 box2-active">
		<div class="box2-header clearfix">
			<strong class="active left">
				<span class="ui-icon icon-ttl-section-info"></span>
				<s:text name="MAN04_upgradeConditions" />
			</strong>
		</div>
		<div class="box2-content clearfix">
			<cherry:form id="mainForm" class="inline">
	      	  	<s:url action="BINOLMOMAN04_init" id="s_init"></s:url>
				<s:url action="BINOLMOMAN04_getTreeNodesByAjax" id="s_getTreeNodesByAjax"></s:url>
				<s:url action="BINOLMOMAN04_getTreeRootNodesByAjax" id="s_getTreeRootNodesByAjax"></s:url>
				<s:url action="BINOLMOMAN04_updateCounterUpdateStatus" id="s_updateCounterUpdateStatus"></s:url>
				<s:url action="BINOLMOMAN04_getSubRegionNoUpdateStatus" id="s_getSubRegionNoUpdateStatus"></s:url>
				<s:url action="BINOLMOMAN04_fromUpdateStatusToNone" id="s_fromUpdateStatusToNone"></s:url>
				<s:url action="BINOLMOMAN04_fromNoneToUpdateStatus" id="s_fromNoneToUpdateStatus"></s:url>
				 <table class="detail" cellpadding="0" cellspacing="0">
					<tr>
						<th><s:text name="MAN04_brand"></s:text></th>
						<td>
							<s:text name="MAN04_select" id="MAN04_select"/>
			                <s:select id="brandInfoId" name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" cssStyle="width:100px;" onchange="changeMachineType('%{#s_getTreeRootNodesByAjax}');"></s:select>
						</td>
						<th><s:text name="MAN04_machineType" /></th>
						<td>
							<s:text name="global.page.all" id="PRO39_selectAll"/>
			         		<s:select name="machineType" list="machineTypeList" listKey="CodeKey" listValue="Value" headerKey="" onchange="changeMachineType('%{#s_getTreeRootNodesByAjax}');"/>
						</td>
					</tr>
				</table> 
			</cherry:form>   
	 	</div>
	 </div>
     <div class="dialog2 clearfix">
  		<div class="left" style="width:47%;">
   		 	<div class="jquery_tree">
   				<div class="box2-header clearfix" style="cursor:default"> 
   					<strong class="left active" id="left_tree" style="background:#E8E8C8">
   						<span class="ui-icon icon-flag"></span>
   						<s:text name="MAN04_counterSelect"/>
   					</strong>
   				</div>
    			<div class="jquery_tree treebox_left tree" id = "treeDemo_left" style="overflow: auto;background:#FCFCFC;">
      			</div>
    		</div>
  		</div>
 	 	<div class="left" style="width:5%; text-align:center; padding-top:200px;">
 	 	<cherry:show domId="BINOLMOMAN0401">
 	 		<a class="mover" id="leftToRight">
    			<span class="ui-icon icon-mover"></span>
 	 		</a>
 	 	</cherry:show> <br /><br /><br />
 	 	<cherry:show domId="BINOLMOMAN0402">
    		<a class="movel" id="rightToLeft">
 	 			<span class="ui-icon icon-movel"></span>
    		</a>
    	</cherry:show>
    	</div>
  		<div class="right" style="width:47%;"><div class="jquery_tree">
    		<div class="box2-header clearfix" onclick = "showDiv();" style="cursor:pointer"> <strong class="left active"  id="right_above_tree"><span class="ui-icon icon-flag2"></span><s:text name="MAN04_updateToOfficialVersion"/></strong></div>
    		<div class="jquery_tree treebox_right tree" id="treeDemo_right_above" style="overflow: auto;background:#FCFCFC;">
      		</div>
      		
      		<div class="box2-header clearfix" onclick = "showDiv();" style="cursor:pointer"> <strong class="left active" id="right_underside_tree"><span class="ui-icon icon-flag2"></span><s:text name="MAN04_updateToTestVersion"/></strong></div>
    		<div class="jquery_tree treebox_right tree" id="treeDemo_right_underside" style="overflow: auto;background:#FCFCFC;">
      		</div>
    	</div>
  		</div>
	</div>
 </div>
 <div id="dataTable_processing" class="dataTables_processing hide"  style="text-algin:left;background-color:#E8E8C8;"><label><s:text name ="MAN04_notice"></s:text></label></div>
 <div class="hide">
 	<div id="pleaseSelect"><s:text name="MAN04_pleaseSelect"></s:text></div>
 	<div id="aerYouSure"><s:text name="MAN04_aerYouSure"></s:text></div>
 </div>
 <div class="hide" id="dialogInit"></div>
      <div style="display: none;">
      <div id="tipTitle"><s:text name="MAN04_tipMessage"/></div>
      <div id="sureTitle"><s:text name="MAN04_tipMessage"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      <div id="sureMessage"><p class="message"><span><s:text name="MAN04_aerYouSure"/></span></p></div>
      <div id="messageWarn"><p class="message"><span><s:text name="MAN04_pleaseSelect" /></span></p></div>
 </div>
</s:i18n>
<span id="getTreeNodesByAjax" style="display:none">${s_getTreeNodesByAjax}</span>
<span id="updateCounterUpdateStatus" style="display:none">${s_updateCounterUpdateStatus}</span>
<span id="getTreeRootNodesByAjax" style="display:none">${s_getTreeRootNodesByAjax}</span>
<span id="getSubRegionNoUpdateStatus" style="display:none">${s_getSubRegionNoUpdateStatus}</span>
<span id="fromUpdateStatusToNone" style="display:none">${s_fromUpdateStatusToNone}</span>
<span id="fromNoneToUpdateStatus" style="display:none">${s_fromNoneToUpdateStatus}</span>

