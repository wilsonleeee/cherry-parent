<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS34_00.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS33.js"></script>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- 保存 --%>
<s:url id="save_url" action="BINOLPTJCS33_save"/>
<%-- 产品分类AJAX查询URL --%>
<%-- <s:url id="queryCate_url" action="BINOLPTJCS33_queryCate"/> --%>
<%-- 自动生成unitCode --%>
<s:url id="getUuitCodeUrl" value="BINOLPTJCS33_getUnitCode"></s:url>
<s:hidden name="getUuitCodeUrlId" value="%{getUuitCodeUrl}"/>
<%-- 检查是否存在相同的barcode产品信息 --%>
<s:url id="chkExistBarCodeUrl" value="BINOLPTJCS33_chkExistBarCode"></s:url>
<s:hidden name="chkExistBarCodeUrlId" value="%{chkExistBarCodeUrl}"/>
<%-- 产品方案ID --%>
<s:hidden id="productPriceSolutionID" value="%{productPriceSolutionID}"/>

<s:i18n name="i18n.pt.BINOLPTJCS03">
<s:text name="JCS03_selectFirst" id="JCS03_selectFirst"/>
<div id="div_main" class="main container clearfix">
    <div class="panel ui-corner-all">
    <div class="panel-header">
	   <div class="clearfix">
	    <span class="breadcrumb left"> 
	     <span class="ui-icon icon-breadcrumb"></span>
	     <s:text name="JCS03_manage"/> &gt; <s:text name="JCS03_header3"/>
	    </span>
	   </div>
	  </div>
      <div class="panel-content">
      <div id="actionResultDisplay"></div>
	  <form id="toAddForm" action="BINOLPTJCS33_init" method="post">
       	<input type="hidden" name="productId" id="productId" value='<s:property value="productId" />'/><%-- 产品ID --%>
       	<input type="hidden" name="brandInfoId" value='<s:property value="brandInfoId"/>'/><%-- BrandInfoId --%>
       	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
      </form>
      <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
          <%-- ======================= 基本属性开始  ============================= --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS33_3.jsp" flush="true"/>
          <%-- ======================= 基本属性结束  ============================= --%>
          
		  <%-- ======================= 扩展属性开始  ============================= --%>
		  <div class="section hide">
			<div class="section-content" id="extInfo">
	      		<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS33_2.jsp" flush="true"/>
	      	</div>
	      </div>
          <%-- ======================= 扩展属性结束  ============================= --%>
          
          
          <%-- ======================= BOM信息开始  ============================= --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS33_5.jsp" flush="true"/>
          <%-- ======================= BOM信息结束  ============================= --%>
          <%-- ======================= 产品分类开始  ============================= --%>
          <div class="section">
		    <div class="section-header clearfix">
		    	<strong class="left">
		    		<span class="ui-icon icon-ttl-section-info-edit"></span>
		    		<s:text name="JCS03_cateInfo"/><%-- 产品分类 --%>
		    	</strong>
		    </div>
			<%-- ================== 错误信息提示 START ======================= --%>
			<div id="cateInfoMessDiv"> 
				<div id="errorMessage"></div>   
			    <div style="display: none" id="errorMessageTemp1">
				   <div class="actionError">
				      <ul><li><span><s:text name="JCS03.cateInfoEmptyError"/></span></li></ul>         
				   </div>
			    </div>
		    </div>
		    <%-- ================== 错误信息提示   END  ======================= --%>
			    <div class="section-content" id="cateInfo">
	          		<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS33_1.jsp" flush="true"/>
	          	</div>
          </div>		
          <%-- ======================= 产品分类结束  ============================= --%> 
          <%-- ======================= 产品价格开始  ============================= --%>
          <div class="section">
            <div class="section-header clearfix">
            	<strong class="left">
            		<span class="ui-icon icon-ttl-section-info-edit"></span>
            		<s:text name="JCS03_standardsellPrice"/><%-- 产品标准价格 --%>
            	</strong>
            </div>
            <div id="minMaxPriceInfo" class="clearfix" style="padding:3px 10px;border: 1px solid #E5E5E5; background-color: #EEEEEE;">
            	<span>
            		<s:text name="JCS03_minSalePrice"/><%-- 销售最低价 --%>
            		<s:textfield cssClass="price" name="minSalePrice" id="minSalePrice" value="%{proMap.minSalePrice}"/>
            		
            		<s:text name="JCS03_moneryUnit" />
            	</span>
            	<span style="margin-left:30px;">
            		<s:text name="JCS03_maxSalePrice"/><%-- 销售最高价 --%>
            		<s:textfield cssClass="price" name="maxSalePrice" value="%{proMap.maxSalePrice}"/><s:text name="JCS03_moneryUnit" />
            	</span>
            	<div class="hide">
	            	<a class="add right" onclick="BINOLPTJCS34_00.addPriceDivRow();return false;" style="margin-top:4px;">																
	            		<span class="ui-icon icon-add"></span>
						<span class="button-text"><s:text name="JCS03_addPrice"/></span><%-- 添加价格--%>
	            	</a>
            	</div>
            </div>
            <div class="section-content" id="priceInfo">
              <table class="detail" cellpadding="0" cellspacing="0">
              	<%-- <caption style="height:20px;"></caption> --%>
              	<tbody>
              		<tr>
	                  <th><label><s:text name="JCS03_salePrice"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 销售价格 --%>
	                  <td><span><s:textfield name="salePrice" cssClass="price" maxlength="17" onkeyup="BINOLPTJCS34_00.setMemPrice(this);return false;" value="%{proMap.salePrice}"/><s:text name="JCS03_moneryUnit"/></span></td>
	                  <th><%-- 会员价格 --%>
	                  	<span style="background-color:#FFDDBB;border:1px solid red; padding:2px;"><s:text name="JCS03_memPrice"/></span>
	                  	<span class="calculator" title="<s:text name='JCS03.calculatorPrice'/>" onclick="BINOLPTJCS34_00.initRateDiv(this);"></span>
	                  </th>
     		          <td><span><s:textfield name="memPrice" cssClass="price" maxlength="17" value="%{proMap.memPrice}"/><s:text name="JCS03_moneryUnit" /></span></td>
	                </tr>
              		<tr class="hide">
	                  <th><label><s:text name="JCS03_StartDate"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 生效日期 --%>
	                  <td><span><s:textfield id="" name="priceStartDate" value="%{chkExistBarCodeMap.businessDate}" cssClass="date" cssStyle="width:80px;"/></span></td>
	                  <th><label><s:text name="JCS03_endDate"/></label></th><%-- 失效日期 --%>
	                  <td><span><s:textfield id="" name="priceEndDate" cssClass="date" cssStyle="width:80px;"/></span></td>
	                </tr>
                </tbody>
              </table>
            </div>
          </div>
          <%-- ======================= 产品价格结束  ============================= --%>

      	</cherry:form>
		<hr/>
        <div class="center clearfix" id="pageButton">
          <button class="save" type="button" onclick="BINOLPTJCS34_00.doSave('${save_url}');return false;">
          	<span class="ui-icon icon-save"></span>
          	<span class="button-text"><s:text name="global.page.save"/></span><%-- 保存 --%>
          </button>
          <button class="close" onclick="window.close();">
          	<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="global.page.close"/></span><%-- 关闭 --%>
          </button>
        </div>
      </div>
    </div>
  </div>
 <div id="priceRow" class="hide" >
   <table class="detail" cellpadding="0" cellspacing="0">
    <caption style="height:20px;">
		<span class="delBtn">
		   <a class="delete right" href="javascript:void(0)" onclick="binolptjcs03.delDivRow(this);return false;">
		   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
		   </a>
	    </span>
	</caption>
	<tbody>
      	<tr>
          <th><label><s:text name="JCS03_salePrice"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 销售价格 --%>
          <td><span><s:textfield name="salePrice" cssClass="price" maxlength="17" onkeyup="BINOLPTJCS34_00.setMemPrice(this);return false;"/><s:text name="JCS03_moneryUnit"/></span></td>
          <th>
          	<span style="background-color:#FFDDBB;border:1px solid red; padding:2px;"><s:text name="JCS03_memPrice"/></span>
          	<span class="calculator" title="<s:text name='JCS03.calculatorPrice'/>" onclick="BINOLPTJCS34_00.initRateDiv(this);"></span>
          </th><%-- 会员价格 --%>
        <td><span><s:textfield name="memPrice" cssClass="price" maxlength="17" /><s:text name="JCS03_moneryUnit"/></span></td>
        </tr>
     		<tr>
          <th><label><s:text name="JCS03_StartDate"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 生效日期 --%>
          <td><span><s:textfield id="" name="priceStartDate" cssClass="date" cssStyle="width:80px;"/></span></td>
          <th><label><s:text name="JCS03_endDate"/></label></th><%-- 失效日期 --%>
          <td><span><s:textfield id="" name="priceEndDate" cssClass="date" cssStyle="width:80px;"/></span></td>
        </tr>
    </tbody>
   </table>
 </div>
 <div class="rateDialog hide">
	<s:text name="JCS03.discountRate"/><%-- 折扣率 --%>
	<input class="number" id="memRate" value="100" onblur="BINOLPTJCS34_00.closeDialog(this);return false;"  
		onkeyup="BINOLPTJCS34_00.setMemPrice();return false;"/><s:text name="global.page.percent"/>
</div>
<jsp:include page="/WEB-INF/jsp/common/popUploadFile.jsp" flush="true" />
<input type="hidden" id="dateHolidays" name="dateHolidays" value="${holidays}"/>

<%-- 选择barcode校验弹窗 --%>
<div class="hide" id="dialogChkExistBarCodeDIV"></div>
<div style="display: none;">
      <div id="tipTitle"><s:text name="相同条码产品列表"/></div>
      <div id="pleaseSelect"><s:text name="pleaseSelect"/></div>
      <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
      <div id="dialogConfirmgoOn"><s:text name="global.page.goOn" /></div>
      <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
      <div id="dialogClose"><s:text name="global.page.close" /></div> 
</div>
</s:i18n>
