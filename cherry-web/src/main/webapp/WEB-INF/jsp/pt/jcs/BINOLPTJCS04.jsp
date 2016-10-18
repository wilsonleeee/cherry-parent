<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS04.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<%-- 产品添加URL --%>
<s:url id="add_url" action="BINOLPTJCS03_init"/>
<s:i18n name="i18n.pt.BINOLPTJCS04">
      <div class="panel-header">
        <div class="clearfix">
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
       		<span class="right"> 
       		<%-- 产品下发按钮 --%>
       		<cherry:show domId="BINOLPTJCS0403">
       		 	<a href="#" class="add" onclick="BINOLPTJCS04.issuedInit();return false;">
       		 		<span class="button-text"><s:text name="产品下发"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		 </cherry:show>
       		<%-- 产品添加按钮 --%>
       		<cherry:show domId="BINOLPTJCS0401">
       		 	<a href="#" class="add" onclick="BINOLPTJCS04.addprt('${add_url}');return false;">
       		 		<span class="button-text"><s:text name="pro.add"/></span>
       		 		<span class="ui-icon icon-add"></span>
       		 	</a>
       		 </cherry:show>
       		 	<s:url action="BINOLPTJCS04_treeInit" id="treeModeInitUrl"/>
        		<s:url action="BINOLPTJCS04_listInit" id="listModeInitUrl"/>
        		<small><s:text name="pro.displayMode"></s:text>:</small>
        		<a id="treeMode" class="display display-tree display-tree-on" title='<s:text name="pro.treeMode"></s:text>' 
        			onclick="BINOLPTJCS04.changeTreeOrTable(this,'${treeModeInitUrl}');return false;"></a><a 
        		id="listMode" class="display display-table" title='<s:text name="pro.listMode"></s:text>' 
        			onclick="BINOLPTJCS04.changeTreeOrTable(this,'${listModeInitUrl}');return false;"></a>
       		</span>
       	</div>
       	
       	<%-- 产品实时下发弹窗 --%>
       	<div class="hide" id="dialogIssuedInitDIV"></div>
        <div style="display: none;">
        	<div id="dialogConfirmIss"><s:text name="global.page.goOn" /></div>
        	<div id="dialogCancelIss"><s:text name="global.page.cancle" /></div>
		    
		    <p id="operateSuccessId" class="success"><span><s:text name="global.page.operateSuccess"/></span>
		    <p id="operateFaildId" class="message"><span><s:text name="global.page.operateFaild"/></span>
		    <p id="sysConfigNonSupportId" class="message"><span><s:text name="global.page.sysConfigNonSupport"/></span>
		    </p>
		    <div id="actionSuccessId" style="" class="actionSuccess">
		    	<ul class="actionMessage">
					<li><span id="msgId"><s:text name="global.page.operateSuccess"/></span></li>
				</ul>
			 </div>
			 <div id="actionFaildId" class="actionError">
			 	<ul class="errorMessage">
			 	<li><span><s:text name="global.page.operateFaild"/></span></li>	</ul>
			 </div>
        </div>
        <!-- 查看当前启用的条码是否存在且有效 -->
		<s:url id="issuedPrtUrl" action="BINOLPTJCS04_issuePrt"/>
		<span class="hide" id="issuedPrtId">${issuedPrtUrl}</span>
		
      </div>
      <div id="treeOrtableId">
	  	<s:action name="BINOLPTJCS04_treeInit" executeResult="true"></s:action>
	  </div>
</s:i18n>	 