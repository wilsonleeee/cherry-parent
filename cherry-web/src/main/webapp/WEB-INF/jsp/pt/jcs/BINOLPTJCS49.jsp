<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS49.js"></script>
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
<s:i18n name="i18n.pt.BINOLPTJCS45">
<s:text name="CHA02_select" id="CHA02_select"/>
<div class="main container clearfix">
	<div class="panel ui-corner-all">
		<div id="div_main">
		<div class="panel-header">
		<cherry:form id="toEditForm" action="BINOLPTJCS47_init" method="post" csrftoken="false">
		        	<input type="hidden" name="productPriceSolutionID" value='<s:property value="productPriceSolutionID"/>'/>
		        	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
		        	<input type="hidden" name="isSynchProductPrice" id="isSynchProductPrice" value="%{productPriceSolutionInfo.isSynchProductPrice}"/>
		</cherry:form>
		<s:hidden id="productPriceSolutionID" value="%{productPriceSolutionID}"></s:hidden>
		<input type="hidden" id="brandInfoId" value='<s:property value="#session.userinfo.BIN_BrandInfoID"/>'>
		<%-- 编辑或添加区分 --%>
		<input type="hidden" id="flag" value="0"></input>
		<input type="hidden" id="isPosCloud" value="<s:property value="productPriceSolutionInfo.isPosCloud"/>"></input>
		<div class="clearfix"> 
			<s:if test="productPriceSolutionInfo.isPosCloud == 0">
			    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="solu_title1" />&nbsp;&gt;&nbsp;<s:text name="JCS49_detail"></s:text></span> 
			</s:if>
			<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
			    <span class="breadcrumb left"> <span class="ui-icon icon-breadcrumb"></span><s:text name="solu_prtlisttitle1" />&nbsp;&gt;&nbsp;<s:text name="JCS49_prtlistdetail"></s:text></span> 
			</s:elseif>
		</div>
		</div>
		<div class="panel-content clearfix">
			<s:if test="productPriceSolutionInfo.isPosCloud == 0">
				<div class="section">
				  <div class="section-header">
					<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="solu_soluDetail"></s:text></strong>
				  </div>
				  <div class="section-content" id="detail">
					 <table class="detail" cellpadding="0" cellspacing="0">
					  	  <tr>
					          <th><s:text name="solu_solutionName"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.solutionName"/></span></td>
					          <th><s:text name="solu_solutionCode"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.solutionCode"/></span></td>
					      </tr>
					  	  <tr>
					          <th><s:text name="solu_startDate"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.startDate"/></span></td>
					          <th><s:text name="solu_endDate"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.endDate"/></span></td>
					      </tr>
					      <tr>
					          <th><s:text name="solu_comments"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.comments" /></span></td> 
					          <th><s:text name="solu_isSynchProductPrice"></s:text></th>
         					  <td><span><s:property value='#application.CodeTable.getVal("1416", productPriceSolutionInfo.isSynchProductPrice)' /></span></td>	      
					      </tr>
					    </table>    
				  </div>
				</div> 
				
          <%-- ======================= 产品分类开始  ========================================================== --%>
          <div class="section detail_box2" >
		    <div class="section-header clearfix">
		    	<strong class="left">
		    		<span class="ui-icon icon-ttl-section-info-edit"></span>
		    		<s:text name="solu_soluDetail_Cate"/><%-- 产品分类 --%>
		    	</strong>
		    </div>
			<%-- ================== 错误信息提示 START ======================= --%>
			<div id="cateInfoMessDiv"> 
				<div id="errorMessageCate"></div>   
			    <div style="display: none" id="errorMessageTemp1">
				   <div class="actionError">
				      <ul><li><span><s:text name="JCS03.cateInfoEmptyError"/></span></li></ul>         
				   </div>
			    </div>
		    </div>
		    <%-- ================== 错误信息提示   END  ======================= --%>
		    <div class="section-content" id="cateInfo">
          		<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS49_02.jsp" flush="true"/>
          	</div>
          </div>		
          <%-- ======================= 产品分类结束  ========================================================== --%> 
			</s:if>
			<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
				<div class="section">
				  <div class="section-header">
					<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="JCS49_cntprtlistdetail"></s:text></strong>
				  </div>
				  <div class="section-content" id="detail">
					 <table class="detail" cellpadding="0" cellspacing="0">
					  	  <tr>
					          <th><s:text name="solu_prtlistName"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.solutionName"/></span></td>
					          <th><s:text name="solu_prtliststartDate"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.startDate"/></span></td>
					      </tr>
					  	  <tr>
					          <th><s:text name="solu_prtlistCode"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.solutionCode"/></span></td>
					          <th><s:text name="solu_prtlistendDate"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.endDate"/></span></td>
					      </tr>
					      <tr>
					          <th><s:text name="solu_prtlistcomments"></s:text></th>
					          <td><span><s:property value="productPriceSolutionInfo.comments" /></span></td> 
					          <th> </th>
					          <td><span> </span></td> 
					      </tr>
					    </table>    
				  </div>
				</div> 
			</s:elseif>
		
			<div class="section show detail_box2" id="productInDepotExcelInfo">
				<div class="section-header">
					<strong>
				  	<span class="ui-icon icon-ttl-section-search-result"></span>
				  	<%-- <s:text name="global.page.list"></s:text> --%>
				  	<s:if test="productPriceSolutionInfo.isPosCloud == 0">
				  		<s:text name="solu_soluDetailPrt" />
	            	</s:if>
	            	<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
	            		<s:text name="solu_cntsoluDetailPrt" />
	            	</s:elseif>
			  	</strong>
				</div>
				<%-- ================== 错误信息提示 START ======================= --%>
				<div id="actionResultDisplay"></div>
				<div id="errorMessage"></div>    
				<div style="display: none" id="errorMessageTemp">
					<div class="actionError">
					   <ul><li><span><s:text name="solu_errorMessage"/></span></li></ul>         
					</div>
				</div>
				<%-- ================== 错误信息提示   END  ======================= --%>
		        <div class="box">
		          <form id="searchForm" class="inline" onsubmit="BINOLPTJCS49.search(); return false;">
		            <div class="box-header">
		            	<strong>
			            	<span class="ui-icon icon-ttl-search"></span>
		            		<s:text name="global.page.condition"/>
		            	</strong>
		            </div>
		            <div class="box-content clearfix">
		               <div class="column" style="width:49%;">
		                 <p>
		                   <label style="width: 65px;"><s:text name="solu_nameTotal"/></label> 
		                   <s:textfield name="nameTotal" cssClass="text" maxlength="50" />
		                   <input type="hidden" id="productId" name="productID">
		                 </p>
		               </div>
		               <div class="column last" style="width:50%;"> 
		            <p class="clearfix">
		              	<%--<cherry:show domId="">--%>
		              		<%-- 产品查询按钮 --%>
		              		<button class="right search" type="button" onclick="BINOLPTJCS49.search();return false;">
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
					<%-- 
						<cherry:show domId="BINOLPTJCS45EXP">
							<a  class="export left" onclick="javascript:BINOLSTIOS08.exportExcel(this);return false;"  href="${export_url}">
								<span class="ui-icon icon-export"></span>
								<span class="button-text"><s:text name="binolstios08_exportExcel_btn"></s:text></span>
							</a>
				  		</cherry:show>
			  		--%>
			  		
			  		<s:url action="BINOLPTJCS49_deleteRow" id="deleteSoluDetail"></s:url>
			  		
			  		<s:if test='productPriceSolutionInfo.isPosCloud == 0'>
				  		<cherry:show domId="BINOLPTJCS4503">
							<a  class="add left" onclick="BINOLPTJCS49.openChkPrtDialogInit();return false;"  href="#">
		       		 			<span class="button-text"><s:text name="solu_addPrt"/></span>
			       		 		<span class="ui-icon icon-add"></span>
							</a>
			                <a href="" class="delete" 
			                	onclick="BINOLPTJCS49.editBatchValidFlag('disable','${deleteSoluDetail}');return false;"
			                	title="去除当前勾选的产品">
			                   <span class="button-text"><s:text name="global.page.delete"/></span> <%-- 删除 --%>
			                   <span class="ui-icon icon-disable"></span>
			                </a>
				  		</cherry:show>
			  		</s:if>
			  		<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
			  			<cherry:show domId="BINOLPTJCS2303">
							  <a class="add left" onclick="BINOLPTJCS49.openChkPrtDialogInit();return false;" href="#" title="从产品云中选择产品并添加到当前门店产品列表中。">
								<span class="button-text"><s:text name="从产品云添加产品"/></span><%-- 从产品库添加产品 --%>
			       		 		<span class="ui-icon icon-add"></span>
			       		 	  </a>	
				              <a href="" class="delete" 
				              	  onclick="BINOLPTJCS49.editBatchValidFlag('disable','${deleteSoluDetail}');return false;"
				                	title="去除当前勾选的产品">
				                   <span class="button-text"><s:text name="global.page.delete"/></span> <%-- 删除 --%>
				                   <span class="ui-icon icon-disable"></span>
				              </a>
			                  <a href="" class="add " onclick="BINOLPTJCS49.addPrt();return false;" title="创建产品云中没有的产品，并添加到当前门店产品列表中。">
			                     <span class="button-text"><s:text name="自建产品"/></span> <%-- 自建产品 --%>
			                     <span class="ui-icon icon-add"></span>
			                  </a>
					       	  <form id="toAddForm" action="BINOLPTJCS23_init" method="post">
					        	<input type="hidden" name="productPriceSolutionID" value='<s:property value="productPriceSolutionID"/>'/>
					        	<input type="hidden" name="csrftoken" id="parentCsrftokenToAddForm"/>
					          </form>
			  			</cherry:show>
			  		</s:elseif>
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
							<th><s:text name="solu_unitCode"></s:text></th>
							<th><s:text name="solu_barCode"></s:text></th>
							<th><s:text name="originalBrand"></s:text></th>
							<th><s:text name="solu_nameTotal"></s:text></th>
							<s:if test="productPriceSolutionInfo.isPosCloud == 0">
								<th><s:text name="solu_salePrice"></s:text></th>
								<th><s:text name="solu_memPrice"></s:text></th>
							</s:if>
							<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
								<th><s:text name="salePrice"></s:text></th>
								<th><s:text name="memPrice"></s:text></th>
							</s:elseif>
							<th><s:text name="global.page.option"></s:text></th>
				        </tr>
					</thead>
					<tbody></tbody>
					</table>
				</div>
			</div>
			<div class="center">
				<s:if test="productPriceSolutionInfo.isPosCloud == 0">
					<cherry:show domId="BINOLPTJCS45UPD">
						<button class="edit" onclick="BINOLPTJCS49.edit();return false;">
							<span class="ui-icon icon-edit-big"></span> <%-- 编辑按钮 --%>
							<span class="button-text"><s:text name="global.page.edit"/></span>
						</button>
					</cherry:show>
				</s:if>
				<s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
					<cherry:show domId="BINOLPTJCS23UPD">
						<button class="edit" onclick="BINOLPTJCS49.edit();return false;">
							<span class="ui-icon icon-edit-big"></span> <%-- 编辑按钮 --%>
							<span class="button-text"><s:text name="global.page.edit"/></span>
						</button>
					</cherry:show>
				</s:elseif>
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
    <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="solu_preprtArea"/></strong></div>
    <div class="section-content">
    <div class="toolbar clearfix">
        <span class="left">
            <a id="spanBtnadd" class="add" onclick="BINOLPTJCS49.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="solu_addRow"/></span></a> 
            <a id="spanBtdelete" class="delete" onclick="BINOLPTJCS49.deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="solu_delRow"/></span></a>
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
                   <%--  <th class="tableheader" width="15%" onclick="BINOLSTSFH01.sortTable('_unitCodeArr');"> --%>
                    <th class="tableheader" width="15%" >
                    	<div class="DataTables_sort_wrapper">
                    		<s:text name="solu_unitCode"/><span id="_unitCodeArr" class="css_right ui-icon ui-icon-carat-2-n-s"></span>
                   		</div>
                    </th>
                    <th class="tableheader" width="18%" id= "thBarCodeId"><s:text name="solu_barCode"/></th>
                    <th class="tableheader" width="18%"><s:text name="originalBrand"/></th>
                    <th class="tableheader" width="18%"><s:text name="solu_nameTotal"/></th>
                    <s:if test="productPriceSolutionInfo.isPosCloud == 0">
	                    <th class="tableheader" width="8%"><s:text name="solu_salePrice"/></th>
	                    <th class="tableheader" width="10%"><s:text name="solu_memPrice"/></th>
                    </s:if>
                    <s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
	                    <th class="tableheader" width="8%"><s:text name="salePrice"/></th>
	                    <th class="tableheader" width="10%"><s:text name="memPrice"/></th>
                    </s:elseif>
                    <th style="display:none"></th>
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


<div class="hide">
	<s:url action="BINOLPTJCS49_search" id="searchUrl" />
	<a href="${searchUrl}" id="search_Url"></a>
	<s:url action="BINOLPTJCS49_addRow" id="addRowUrl" />
	<a href="${addRowUrl}" id="addRow_Url"></a>
	<s:url action="BINOLPTJCS49_editRow" id="editRowUrl" />
	<a href="${editRowUrl}" id="editRow_Url"></a>
	
	<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
    <div id="dialogClose"><s:text name="global.page.close" /></div>
	<div id="disableTitle"><s:text name="solu_delPrt" /></div>
	<div id="enableTitle"><s:text name="counter.enableTitle" /></div>
	<div id="disableMessage"><p class="message"><span><s:text name="solu_disableMessage" /></span></p></div>
</div> 
</s:i18n>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<div id="detailData" class="hide"></div>
