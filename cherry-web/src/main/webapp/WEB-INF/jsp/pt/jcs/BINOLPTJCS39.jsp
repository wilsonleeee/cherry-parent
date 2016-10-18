<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/ajaxfileupload.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS39.js"></script>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript">
$(function() {
	productBinding({elementId:"nameTotal",showNum:20,targetId:"productId",targetShow:"nameTotal"}); 
	var url = $("#search_url").val();
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));});
} );
</script>
<s:url id="downLoad_url" value="/download/产品功能开启时间明细导入模板.xls"/>
<%-- 入库单导入导入URL --%>
<s:url id="import_url" value="/pt/BINOLSTIOS39_import"/>
<s:i18n name="i18n.pt.BINOLPTJCS38">
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
		<div class="panel-header">
		<cherry:form id="toEditForm" action="BINOLPTJCS40_init" method="post" csrftoken="false">
        	<input type="hidden" name="productFunctionID" value='<s:property value="productFunctionID"/>'/>
        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
		</cherry:form>
		<s:hidden id="productFunctionID" value="%{productFunctionID}"></s:hidden>
		<s:hidden id="pfValidFlag" value="%{prtFunInfo.validFlag}"></s:hidden>
		<input type="hidden" id="brandInfoId" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
		<%-- 编辑或添加区分 --%>
		<input type="hidden" id="flag" value="0"></input>
		<div class="clearfix"> 
		    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="prtFun_title1" />&nbsp;&gt;&nbsp;<s:text name="JCS39_detail"></s:text></span> 
		</div>
		</div>
		<div class="panel-content clearfix">
			<div class="section">
			  <div class="section-header">
				<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="prtfun_prtfunDetail"></s:text></strong>
			  </div>
			  <div class="section-content" id="detail">
				 <table class="detail" cellpadding="0" cellspacing="0">
				  	  <tr>
				          <th><s:text name="prtFunType"></s:text></th>
				          <td><span><s:property value='#application.CodeTable.getVal("1327",prtFunInfo.prtFunType)'/></span></td>
				          <th><s:text name="prtfun_startDate"></s:text></th>
				          <td><span><s:property value="prtFunInfo.startDate"/></span></td>
				      </tr>
				  	  <tr>
				          <th><s:text name="prtFunDateName"></s:text></th>
				          <td><span><s:property value="prtFunInfo.prtFunDateName"/></span></td>
				          <th><s:text name="prtfun_endDate"></s:text></th>
				          <td><span><s:property value="prtFunInfo.endDate"/></span></td>
				      </tr>
				    </table>    
			  </div>
			</div> 
		
			<div class="section show detail_box2" id="productInDepotExcelInfo">
				<div class="section-header">
					<strong>
				  	<span class="ui-icon icon-ttl-section-search-result"></span>
				  	<%-- <s:text name="global.page.list"></s:text> --%>
				  	<s:text name="prtfun_prtfunDetailPrt" />
			  	</strong>
				</div>
				<%-- ================== 错误信息提示 START ======================= --%>
			    <%-- ================== 信息提示区 START ======================= --%>
				<div id="actionDisplay">
					<div id="errorDiv" class="actionError" style="display:none;">
						<ul>
						    <li>
						    	<span id="errorSpan"></span>
						    </li>
						</ul>			
				  	</div>
				  	<div id="successDiv" class="actionSuccess" style="display:none;">
						<ul class="actionMessage">
					  		<li>
					  			<span id="successSpan">	</span>
					  		</li>
					 	</ul>
					</div>
				</div>
				<div id="actionResultDisplay"></div>
				<div id="errorMessage"></div>    
				<div style="display: none" id="errorMessageTemp">
					<div class="actionError">
					   <ul><li><span><s:text name="prtfun_errorMessage"/></span></li></ul>         
					</div>
				</div>
				<%-- ================== 错误信息提示   END  ======================= --%>
		        <div class="box">
		          <form id="searchForm" class="inline" onsubmit="BINOLPTJCS39.search(); return false;">
		            <div class="box-header">
		            	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
		            </div>
		            <div class="box-content clearfix">
		               <div class="column" style="width:49%;">
		                 <p>
		                   <label style="width: 65px;"><s:text name="prtfun_nameTotal"/></label> 
		                   <s:textfield name="nameTotal" cssClass="text" maxlength="50" />
		                   <input type="hidden" id="productId" name="productID">
		                 </p>
		               </div>
		               <div class="column last" style="width:50%;"> 
		            <p class="clearfix">
		              	<%--<cherry:show domId="">--%>
		              		<%-- 产品查询按钮 --%>
		              		<button class="right search" type="button" onclick="BINOLPTJCS39.search();return false;">
		              			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
		              		</button>
		              	<%--</cherry:show>--%>
		            </p>
		          		</div>
		       		</div>
		         </form>
		        </div>
				<div class="section-content">
					<div class="toolbar clearfix">
				  		<s:url action="BINOLPTJCS39_deleteRow" id="deleteSoluDetail"></s:url>
						<%-- 添加产品 --%>
						<a  class="add left" onclick="BINOLPTJCS39.openChkPrtDialogInit();return false;"  href="#">
		       		 		<span class="button-text"><s:text name="prtfun_addPrt"/></span>
		       		 		<span class="ui-icon icon-add"></span>
						</a>
						<%-- 删除产品 --%>
		                <a href="" class="delete left" onclick="BINOLPTJCS39.editBatchValidFlag('disable','${deleteSoluDetail}');return false;">
		                   <span class="ui-icon icon-disable"></span>
		                   <span class="button-text"><s:text name="global.page.delete"/></span>
		                </a>
		                <%-- 导入产品 --%>
						<span  class="left" style="margin-top: -3px;margin-left:8px;"> 
							<span class="highlight"><s:text name="binolstios09_snow" /></span>
							<s:text name="binolstios09_notice"/>
                            <a id="downloadURL" href="${downLoad_url }"><s:text name="binolstios09_down"/></a>
						    <input class="input_text" type="text" id="pathExcel" name="pathExcel"/>
						    <input type="button" value="<s:text name="global.page.browse"/>"/>
						    <input class="input_file" type="file" name="upExcel" id="upExcel" size="33" onchange="BINOLPTJCS39.deleteActionMsg();pathExcel.value=this.value;return false;" /> 
						    <input type="button" value="<s:text name="binolstios09_importExcel_btn"/>" onclick="BINOLPTJCS39.ajaxFileUpload('${import_url}');return false;" id="upload"/>
	        				<img id="loading" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
						</span>
					  	<span class="right">
				  			<a class="setting">
				  				<span class="ui-icon icon-setting"></span>
				             		<span class="button-text"><s:text name="global.page.colSetting"/></span>
		            		</a>
		            	</span>
					</div>
					<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
						<thead>
				        <tr>
				        	<th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
							<th><s:text name="NO."></s:text></th>
							<th><s:text name="prtfun_unitCode"></s:text></th>
							<th><s:text name="prtfun_nameTotal"></s:text></th>
							<th><s:text name="global.page.option"></s:text></th>
				        </tr>
					</thead>
					<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="center">
				<cherry:show domId="BINOLPTJCS38UPD">
					<button class="edit" onclick="BINOLPTJCS39.edit();return false;">
						<span class="ui-icon icon-edit-big"></span> <%-- 编辑按钮 --%>
						<span class="button-text"><s:text name="global.page.edit"/></span>
					</button>
				</cherry:show>
				<button class="close" type="button" onclick="window.close();return false;"><span class="ui-icon icon-close"></span><span class="button-text"><s:text name="global.page.close"/></span></button>
			</div>
		</div>
		</div>
	</div>
</div>
<%-- 方案添加产品对话框 --%>
<div class="hide" id="dialogChkPrtInitDIV"></div>
<div class="hide" id="dialogInit"></div>  
<div class="section hide" id="addPrtTable">
  <form id="mainForm">
  <div class="section">
    <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="prtfun_preprtArea"/></strong></div>
    <div class="section-content">
    <div class="toolbar clearfix">
        <span class="left">
            <a id="spanBtnadd" class="add" onclick="BINOLPTJCS39.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="prtfun_addRow"/></span></a> 
            <a id="spanBtdelete" class="delete" onclick="BINOLPTJCS39.deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="prtfun_delRow"/></span></a>
            <%-- <span class="bg_yew" ><span style="line-height:25px;" class="highlight">*<s:text name="拖拽明细行可改变位置 "/></span></span> --%>
        </span>
    </div>
    <div id="canceldetail">
      <div style="width:100%;overflow-x:scroll;">
        <table id="mainTable" cellpadding="0" cellspacing="0" border="0" width="100%" class="background_1">
            <thead>
               <tr>
                    <th class="tableheader" width="3%">
                        <input id="allSelect" type="checkbox"  class="checkbox" onclick="doSelectAll(this,'#newDatabody');"/>
                        <s:text name="global.page.selectAll"/>
                    </th>
                    <th class="tableheader" width="15%" onclick="BINOLSTSFH01.sortTable('_unitCodeArr');">
                    	<div class="DataTables_sort_wrapper">
                    		<s:text name="prtfun_unitCode"/><span id="_unitCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span>
                   		</div>
                    </th>
                    <th class="tableheader" width="18%"><s:text name="prtfun_nameTotal"/></th>
                    <th style="display:none">
                </tr>
            </thead>
            <tbody id="databody">
            </tbody>
        </table>
      </div>
    </div>
    <div id="mydetail" class="section">
    </div>
    <hr class="space" />
    <hr class="space" />
    <!--  
    <div class="center hide">
        <button class="save" type="button" onclick="BINOLSTSFH01.save();return false;"><span class="ui-icon icon-save"></span><span class="button-text"><s:text name="SFH01_Save"/></span></button>
        <cherry:show domId="BINOLSTSFH0101">
            <button class="confirm" type="button" onclick="BINOLSTSFH01.btnSendClick()"><span class="ui-icon icon-confirm"></span><span class="button-text"><s:text name="SFH01_OK"/></span></button>
        </cherry:show>
    </div>
    -->
  </div>
  </div>
  </form>
</div>
<div id="salePriceEditForm" class="hide">
	<span id="salePrice"><s:textfield name="salePrice" cssClass="price" maxlength="17"  /></span>
</div>
<div id="memPriceEditForm" class="hide">
	<span id="memPrice"><s:textfield name="memPrice" cssClass="price" maxlength="17"  /></span>
</div>

<%-- 错误信息 --%>
<div id="errMsg">
	<%-- 请选择上传文件。 --%>
	<input type="hidden" id="errmsg1" value='<s:text name="EMO00010"/>'/>
	<input type="hidden" id="errmsg2" value='<s:text name="binolstios09_error1"/>'/>
	<input type="hidden" id="errmsg3" value='<s:text name="binolstios09_error2"/>'/>
	<input type="hidden" id="errmsg4" value='<s:text name="binolstios09_error3"/>'/>
</div>
<div class="hide">
	<s:url action="BINOLPTJCS39_search" id="searchUrl" />
	<a href="${searchUrl}" id="search_Url"></a>
	<s:url action="BINOLPTJCS39_addRow" id="addRowUrl" />
	<a href="${addRowUrl}" id="addRow_Url"></a>
	<s:url action="BINOLPTJCS39_editRow" id="editRowUrl" />
	<a href="${editRowUrl}" id="editRow_Url"></a>
	
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
	<div id="disableTitle"><s:text name="prtfun_delPrt" /></div>
	<div id="enableTitle"><s:text name="counter.enableTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="prtfun_disableMessage" /></span></p></div>
</div> 
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<div id="detailData" class="hide"></div>
