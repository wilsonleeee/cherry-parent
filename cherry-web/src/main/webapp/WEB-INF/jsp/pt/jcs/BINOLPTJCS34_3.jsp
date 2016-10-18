<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%-- 产品查询URL --%>
<s:url id="search_url" value="/pt/BINOLPTJCS34_search"/>
<input type="hidden" id="search_url" value="${search_url}"/>
<script type="text/javascript">
$(function() {
	//productBinding({elementId:"unitCode",showNum:20,targetShow:"unitCode"});//厂商编码
	productBinding({elementId:"barCode",showNum:20,targetShow:"barCode"});//产品条码
	productBinding({elementId:"nameTotal",showNum:20,targetShow:"nameTotal"});//产品名称
	BINOLPTJCS34.originalBrandBinding({elementId:"originalBrandStr",codeType:"1299"}); // 子品牌查询 
	// 	cntProductBinding({elementId:"nameTotal",showNum:20,targetShow:"nameTotal",counterCode:"NR111908"});//产品名称
	var url = $("#search_url").val();
	BINOLPTJCS34.search(url);
	// 输入框trim处理
	$(":text").bind('focusout',function(){ 
		var $this = $(this);$this.val($.trim($this.val()));});
} );
</script>
<!-- 停用或启用url -->
<s:url id="disOrEnable_Url" action="BINOLPTJCS34_disOrEnable"/>
<!-- 查看相同产品是否存在有效数据 -->
<s:url id="getPrtDetail_Url" action="BINOLPTJCS34_getPrtDetail"/>
<!-- 查看当前启用的条码是否存在且有效 -->
<s:url id="getPrtBarCodeVF_Url" action="BINOLPTJCS34_getPrtBarCodeVF"/>
<span class="hide" id="disOrEnable">${disOrEnable_Url}</span>
<span class="hide" id="prtDetailUrl">${getPrtDetail_Url}</span>
<span class="hide" id="prtBarCodeVFUrl">${getPrtBarCodeVF_Url}</span>
<%-- 产品方案ID --%>
<s:hidden id="productPriceSolutionID" value="%{productPriceSolutionID}"/>
<s:hidden id="isPosCloudId" value="%{isPosCloud}"/>
<s:i18n name="i18n.pt.BINOLPTJCS04">
<s:text id="selectAll" name="global.page.select"/>
<s:hidden id="isU2M" value="%{isU2M}"/>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="actionResultDisplay"></div>
<div id="errorMessage"></div>    
<div style="display: none" id="errorMessageTemp">
	<div class="actionError">
	   <ul><li><span><s:text name="JCS04.errorMessage"/></span></li></ul>         
	</div>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div class="panel-content">
    <div class="hide">
        <s:url id="export" action="BINOLPTJCS34_export" ></s:url>
        <a id="downUrl" href="${export}"></a>
    </div>
        <div class="box">
          <cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
            <div class="box-header">
            	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
            </div>
            <div class="box-content clearfix">
               <div class="column" style="width:49%;">
               	<div class="hide">
               		<label style="width: 65px;"><s:text name="pro.brandInfo"/></label><%-- 品牌 --%>
               		<s:select name="brandInfoId" list="brandInfoList" listKey="brandInfoId" listValue="brandName" id="brandInfoId"/>
               	</div>
	   			<p>
                  <label style="width: 65px;"><s:text name="pro.barCode"/></label><%-- 产品条码--%>
                  <s:textfield name="barCode" cssClass = "text"/>
                </p>
               <p>
                  <label style="width: 65px;"><s:text name="pro.validFlag"/></label><%-- 有效状态--%>
                  <s:select name="validFlag" list='#application.CodeTable.getCodes("1137")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}" value="1"/>
                </p>
              </div> 
              <div class="column last" style="width:50%;"> 
                <p>
                  <label style="width: 65px;"><s:text name="pro.nameTotal"/></label><%-- 产品全名--%>
                  <s:textfield name="nameTotal" cssClass="text" maxlength="50" />
                </p> 
               <p>
                  <label style="width: 65px;"><s:text name="pro.originalBrand"/></label><%-- 子品牌--%>
                  <s:textfield name="originalBrandStr" cssClass="text" maxlength="50" onblur="BINOLPTJCS34.clearVal()" onkeyup="BINOLPTJCS34.clear()"/>
                  <s:hidden name="originalBrand"></s:hidden>
                  <s:hidden name="originalBrandStrOld"></s:hidden>
                  <%--<s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}"  />--%>
                </p>
              </div>
            </div>
            <p class="clearfix">
              	<%--<cherry:show domId="">--%>
              		<%-- 产品查询按钮 --%>
              		<button class="right search" type="button" onclick="BINOLPTJCS34.search('${search_url}');return false;">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
              		</button>
              	<%--</cherry:show>--%>
            </p>
          </cherry:form>
        </div>
        <div id="section" class="section">
          <div class="section-header">
          	<strong>
          		<span class="ui-icon icon-ttl-section-search-result"></span>
          		<s:text name="global.page.list"/>
          	</strong>
          </div>
          <div class="section-content">
            <div class="toolbar clearfix">
                <span class="left">
                    <cherry:show domId="BINOLPTJCS34EXPSSSSSSSSSSSSSSSSS">
                    <a id="export" class="export">
                        <span class="ui-icon icon-export"></span>
                        <span class="button-text"><s:text name="global.page.export"/></span>
                    </a>
                    </cherry:show>
           		<s:url action="BINOLPTJCS34_delete" id="delProduct"></s:url>
                <cherry:show domId="BINOLPTJCS34BAT">
	                <a href="" class="delete" onclick="BINOLPTJCS34.editBatchValidFlag('enable','${delProduct}');return false;">
	                   <span class="ui-icon icon-enable"></span>
	                   <span class="button-text"><s:text name="global.page.enable"/></span>
	                </a>
	                <a href="" class="delete" onclick="BINOLPTJCS34.editBatchValidFlag('disable','${delProduct}');return false;">
	                   <span class="ui-icon icon-disable"></span>
	                   <span class="button-text"><s:text name="global.page.disable"/></span>
	                </a>
                </cherry:show>
                </span>
            	<span class="right">
              		<a class="setting">
       		 			<span class="ui-icon icon-setting"></span><%-- 列设置按钮  --%>
       		 			<span class="button-text"><s:text name="global.page.colSetting"/></span>
       		 		</a>
              	</span>
            </div>
            
            <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%">
              <thead>
                <tr>
                  <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable_Cloned');"/></th>
                  <th><s:text name="pro.number"/></th><%-- 编号 --%>
                  <th><s:text name="pro.nameTotalCN"/><span class="ui-icon ui-icon-document"></span></th><%-- 中文名称--%>
                  <th><s:text name="产品描述"/><span class="ui-icon ui-icon-document"></span></th><%-- 产品描述--%>
                  <%--<th><s:text name="pro.nameTotalEN"/></th> --%><%-- 英文名称--%>
                  <th><s:text name="pro.unitCode"/></th><%-- 产品厂商编码 --%>
                  <th><s:text name="pro.barCode"/></th><%-- 产品条码 --%>
                  <th><s:text name="pro.originalBrand"/></th><%-- 品牌 --%>
                  <%--<th><s:text name="pro.itemType"/></th>--%><%-- 品类  --%>
                  <th><s:text name="JCS04.primaryCategoryBig"/></th><%-- 大分类--%>
                  <th><s:text name="JCS04.primaryCategoryMedium"/></th><%-- 中分类 --%>
                  <th><s:text name="JCS04.primaryCategorySmall"/></th><%-- 小分类 --%>
                   <%--<th><s:text name="pro.mode"/></th>--%><%-- 产品类型 --%>
                  <%--<th><s:text name="pro.guige"/></th>--%><%-- 计量单位  --%>
                  <%--<th><s:text name="pro.standardCost"/></th>--%><%-- 成本价格 --%>
                  <%--<th><s:text name="pro.orderPrice"/></th>--%><%-- 采购价格 --%>
                  <th><s:text name="solu_minSalePrice"/></th><%-- 销售最低价格 --%>
                  <th><s:text name="solu_maxSalePrice"/></th><%-- 销售最高价格 --%>
                  <th><s:text name="pro.salePrice"/></th><%-- 销售价格 --%>
                  <th><s:text name="pro.memPrice"/></th><%-- 会员价格 --%>
                  <th><s:text name="pro.validFlag"/></th><%-- 有效区分 --%>
                  <th><s:text name="global.page.option"/></th><%-- 操作 --%>
                </tr>
               </thead>           
              </table>
          </div>
		</div>
	</div>
	<div class="hide" id="dialogInitDIV"></div>
    <div style="display: none;">
      <div id="dialogConfirm"><s:text name="global.page.goOn" /></div>
      <div id="dialogConfirmOK"><s:text name="global.page.ok" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="enableValTitle"><s:text name="pro.enableValidFlag" /></div>
      <div id="disableValTitle"><s:text name="pro.disableValidFlag"/></div>
      <div id="confirIsDisable"><s:text name="JCS04.confirIsDisable"/></div>
      <div id="confirIsEnable"><s:text name="JCS04.confirIsEnable"/></div>
      
	  <div id="disableTitle"><s:text name="JCS04.disableTitle" /></div>
	  <div id="enableTitle"><s:text name="JCS04.enableTitle" /></div>
	  <div id="disableMessage"><p class="message"><span><s:text name="JCS04.disableMessage" /></span></p></div>
	  <div id="enableMessage"><p class="message"><span><s:text name="JCS04.enableMessage" /></span></p></div>
	  <%-- <div id="dialogConfirm"><s:text name="global.page.ok" /></div> --%>
      <div id="dialogClose"><s:text name="global.page.close" /></div>
      
	<div id="dialogConfirmOk"><s:text name="global.page.ok" /></div>
	<div id="disableTitleDelPrt"><s:text name="solu_delPrt" /></div>
      
    </div>
    <div class="hide" id="dialogInit"></div>  
    
	<s:url action="BINOLPTJCS19_addRow" id="addRowUrl" />
	<a href="${addRowUrl}" id="addRow_Url"></a>
	<s:url action="BINOLPTJCS34_addSelPrt" id="addSelPrtUrl" />
	<a href="${addSelPrtUrl}" id="addSelPrtUrl"></a>
	<s:url action="BINOLPTJCS19_editRow" id="editRowUrl" />
	<a href="${editRowUrl}" id="editRow_Url"></a>
    
	<%-- 编辑或添加区分 --%>
	<input type="hidden" id="flag" value="0"></input>
	<%-- 批量添加产品框 --%>
	<div class="hide" id="dialogChkPrtInitDIV"></div>
	<%-- 根据条件导入产品 --%>
	<div class="hide" id="dialogSelPrtInitDIV"></div>
	<%-- 批量添加产品面板 --%>
	<div class="section hide" id="addPrtTable">
	  <form id="mainForm">
	  <div class="section">
	    <div class="section-header"><strong><span class="ui-icon icon-ttl-section-list"></span><s:text name="solu_preprtArea"/></strong></div>
	    <div class="section-content">
	    <div class="toolbar clearfix">
	        <span class="left">
	            <a id="spanBtnadd" class="add" onclick="BINOLPTJCS34.openProPopup();"><span class="ui-icon icon-add"></span><span class="button-text"><s:text name="solu_addRow"/></span></a> 
	            <a id="spanBtdelete" class="delete" onclick="BINOLPTJCS34.deleterow();"><span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="solu_delRow"/></span></a>
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
	                    <th class="tableheader" width="18%"><s:text name="pro.originalBrand"/></th>
	                    <th class="tableheader" width="18%"><s:text name="solu_nameTotal"/></th>
	                    <%--
	                    <s:if test="productPriceSolutionInfo.isPosCloud == 0">
		                    <th class="tableheader" width="8%"><s:text name="solu_salePrice"/></th>
		                    <th class="tableheader" width="10%"><s:text name="solu_memPrice"/></th>
	                    </s:if>
	                    <s:elseif test="productPriceSolutionInfo.isPosCloud == 1">
		                    <th class="tableheader" width="8%"><s:text name="salePrice"/></th>
		                    <th class="tableheader" width="10%"><s:text name="memPrice"/></th>
	                    </s:elseif>
	                    --%>
	                    <th class="tableheader" width="8%"><s:text name="solu_minSalePrice"/></th>
	                    <th class="tableheader" width="8%"><s:text name="solu_maxSalePrice"/></th>
	                    
	                    
	                    <th class="tableheader" width="8%"><s:text name="salePrice"/></th>
	                    <th class="tableheader" width="10%"><s:text name="memPrice"/></th>
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
	
	<%-- 根据条件导入产品面板 --%>
	<div class="section hide" id="selPrtTable">
		<%--商品 --%>
		<div class="detail_box2" style="height: 141px;" >
			<div class="section">
				<div class="section-header">
					<div class="left">
						<strong><span class="ui-icon icon-ttl-section-info"></span><s:text name="筛选条件" /></strong>
					</div>
				</div>
				<div class="section-content">
				<form id="selForm">
               	<p>
                  <label><s:text name="pro.originalBrand"/></label><%-- 子品牌--%>
                  <s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value"
                  	headerKey="" headerValue="%{selectAll}"  />
                </p>
		        <span id="soluMsg" >
					<span class="highlight">※</span>
					<span class="red">选择品牌导入可能会覆盖门店中对应的品牌产品属性（如价格等）。</span>
		        </span>
                </form>
				</div>
			</div>
		</div>
	</div>
    
    
    
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>
		 
