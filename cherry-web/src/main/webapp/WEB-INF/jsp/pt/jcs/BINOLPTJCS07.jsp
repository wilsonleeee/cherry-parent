<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS00.js?version=2017022701"></script>
<script type="text/javascript" src="/Cherry/js/pt/jcs/BINOLPTJCS07.js?version=2017022701"></script>
<!-- 语言 -->
<s:set id="language" value="#session.language" />
<%-- 保存 --%>
<s:url id="update_url" action="BINOLPTJCS07_update"/>
<%-- 详细 --%>
<s:url id="back_url" action="BINOLPTJCS06_init"/>
<s:i18n name="i18n.pt.BINOLPTJCS03">
<s:text name="JCS03_selectFirst" id="JCS03_selectFirst"/>
<div id="div_main" class="main container clearfix">
    <div class="panel ui-corner-all">
    <div class="panel-header">
	   <div class="clearfix">
	    <span class="breadcrumb left"> 
	     <span class="ui-icon icon-breadcrumb"></span>
	     <s:text name="JCS03_manage"/> &gt; <s:text name="JCS03_header2"/>
	    </span>
	   </div>
	  </div>
	  <form id="toDetailForm" action="BINOLPTJCS06_init" method="post">
       	<input type="hidden" name="productId" value='<s:property value="productId"/>'/><%-- 产品ID --%>
       	<input type="hidden" name="brandInfoId" value='<s:property value="brandInfoId"/>'/><%-- BrandInfoId --%>
       	<input type="hidden" name="validFlag" value='<s:property value="validFlag"/>'/><%-- 产品ID --%>
       	<input type="hidden" name="csrftoken" id="parentCsrftoken"/>
      </form>
      <div class="panel-content">
      <div id="actionResultDisplay"></div>
      <cherry:form id="mainForm" method="post" class="inline" csrftoken="false">
          <%-- ======================= 基本属性开始  ============================= --%>
          <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS07_2.jsp" flush="true"/>
          <%-- ======================= 基本属性结束  ============================= --%>
          <%-- ======================= BOM信息开始  ============================= --%>
	      <jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS03_5.jsp" flush="true"/>
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
          		<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS03_1.jsp" flush="true"/>
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
            		<input class="price" name="minSalePrice" value="<s:if test="proMap.minSalePrice != 0"><s:property value="proMap.minSalePrice"/></s:if>"/><s:text name="JCS03_moneryUnit"/>
            	</span>
            	<span style="margin-left:30px;">
            		<s:text name="JCS03_maxSalePrice"/><%-- 销售最高价 --%>
            		<input class="price" name="maxSalePrice" value="<s:if test="proMap.maxSalePrice != 0"><s:property value="proMap.maxSalePrice"/></s:if>"/><s:text name="JCS03_moneryUnit"/>
            	</span>
            	<a class="add right" onclick="BINOLPTJCS00.addPriceDivRow();return false;" style="margin-top:4px;">																
            		<span class="ui-icon icon-add"></span>
					<span class="button-text"><s:text name="JCS03_addPrice"/></span><%-- 添加价格--%>
            	</a>
            </div>
            <div class="section-content" id="priceInfo">
              	<s:iterator value="sellPriceList" status="st">
              		<table class="detail" cellpadding="0" cellspacing="0">
		              	<caption style="height:20px;">
		              		<s:if test="sellPriceList.size > 1">
			              	<span class="delBtn <s:if test="%{compareDateFlag}"> hide </s:if>">
							   <a class="delete right" href="javascript:void(0)" onclick="BINOLPTJCS07.delDivRow(this);return false;">
							   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
							   </a>
						    </span>
						    </s:if>
					    </caption>
		              	<tbody>
			              	<tr>
			                  <th><label><s:text name="JCS03_salePrice"/><span class="highlight"><s:text name="global.page.required"/></span></label><s:hidden name="prtPriceId" value="%{prtPriceId}"/><s:hidden name="option" value="1"/></th><%-- 销售价格 --%>
			                  <td>
			                  	<span>
			                  		<s:if test="%{compareDateFlag}"> 
				                  		<s:textfield name="salePrice" cssClass="price" maxlength="17" value="%{salePrice}" onkeyup="BINOLPTJCS00.setMemPrice(this);return false;" disabled="true"/> <s:text name="JCS03_moneryUnit"/>
		                  		    	<%--  <s:hidden name="salePrice" value="%{salePrice}"></s:hidden>  --%>
			                  		</s:if>
			                  		<s:else>
				                  		<s:textfield name="salePrice" cssClass="price" maxlength="17" value="%{salePrice}" onkeyup="BINOLPTJCS00.setMemPrice(this);return false;" /> <s:text name="JCS03_moneryUnit"/>
			                  		</s:else>
	                  		    </span>
	                  		 </td>
			                  <th>
			                  	<span style="background-color:#FFDDBB;border:1px solid red; padding:2px;"><s:text name="JCS03_memPrice"/></span>
			                  	<span class="calculator" title="<s:text name='JCS03.calculatorPrice'/>" onclick="BINOLPTJCS00.initRateDiv(this);"></span>
			                  </th><%-- 会员价格 --%>
		     		          <td>
		     		          	<span>
		     		          		<s:if test="%{compareDateFlag}"> 
			     		          		<s:textfield name="memPrice" cssClass="price" maxlength="17" value="%{memPrice}" disabled="true"/><s:text name="JCS03_moneryUnit"/>
		                  		    	<%-- <s:hidden name="memPrice" value="%{memPrice}"></s:hidden>  --%>
		     		          		</s:if>
		     		          		<s:else>
			     		          		<s:textfield name="memPrice" cssClass="price" maxlength="17" value="%{memPrice}"/><s:text name="JCS03_moneryUnit"/>
		     		          		</s:else>
	     		          		</span>
     		          		  </td>
			                </tr>
		              		<tr>
			                  <th><label><s:text name="JCS03_StartDate"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 生效日期 --%>
			                  <td>
			                  	<span>
			                  		<s:if test="%{compareDateFlag}"> 
				                  		<s:textfield id="" name="priceStartDate" value="%{startDate}" cssStyle="width:80px;" disabled="true"/>
		                  		    	<%-- <s:hidden name="priceStartDate" value="%{startDate}"></s:hidden> --%>
			                  		</s:if>
			                  		<s:else>
				                  		<s:textfield id="" name="priceStartDate" cssClass="date" value="%{startDate}" cssStyle="width:80px;"/>
			                  		</s:else>
		                  		</span>
	                  		  </td>
			                  <th><label><s:text name="JCS03_endDate"/></label></th><%-- 失效日期 --%>
			                  <td>
			                  	<span>
			                  		<s:if test="%{compareDateFlag}"> 
				                  		<s:textfield id="" name="priceEndDate" value="%{endDate}" cssStyle="width:80px;" disabled="true"/>
				                  		<%-- <s:hidden name="priceEndDate" value="%{endDate}"></s:hidden> --%>
			                  		</s:if>
			                  		<s:else>
				                  		<s:textfield id="" name="priceEndDate" cssClass="date" value="%{endDate}" cssStyle="width:80px;"/>
			                  		</s:else>
		                  		</span>
	                  		 </td>
			                </tr>		                
		                </tbody>
		            </table>
              	</s:iterator>
            </div>
          </div>
          <%-- ======================= 产品价格结束  ============================= --%>
		  <%-- ======================= 扩展属性开始  ============================= --%>
		  <div class="section">
		  	<div class="section-header clearfix">
			  	<span id="prtExtLab" class="hide">
			  		<strong class="left">
			  			<span class="ui-icon icon-ttl-section-info-edit"></span>
			  			<s:text name="JCS03_prtExt"/><%-- 产品扩展属性 --%>
			  		</strong>
		  		</span>
		  		<a class="add right" onclick="BINOLPTJCS00.showExtInfo(this,'<s:text name="JCS03_showExt"/>','<s:text name="JCS03_hideExt"/>');return false;">
		  			<span class="ui-icon icon-add"></span>
		     		<span class="button-text"><s:text name="JCS03_showExt"/></span><%-- 显示扩展属性--%>
		    	</a>
		  	</div>
		    <div class="section-content hide" id="extInfo">
	      		<jsp:include page="/WEB-INF/jsp/pt/jcs/BINOLPTJCS07_1.jsp" flush="true"/>
	      	</div>
	      </div>		
          <%-- ======================= 扩展属性结束  ============================= --%>
      	</cherry:form>
		<hr/>
        <div class="center clearfix" id="pageButton">
          <button class="save" type="button" onclick="BINOLPTJCS00.doSave('${update_url}');return false;">
          	<span class="ui-icon icon-save"></span>
          	<span class="button-text"><s:text name="global.page.save"/></span><%-- 保存 --%>
          </button>
          <button id="back" class="back" type="button" onclick="BINOLPTJCS07.doBack()">
           	<span class="ui-icon icon-back"></span>
           	<span class="button-text"><s:text name="global.page.back"/></span>
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
		   <a class="delete right" href="javascript:void(0)" onclick="BINOLPTJCS07.delDivRow(this);return false;">
		   	<span class="ui-icon icon-delete"></span><span class="button-text"><s:text name="global.page.delete"/></span><%-- 删除按钮 --%>
		   </a>
	    </span>
	</caption>
	<tbody>
      	<tr>
          <th><label><s:text name="JCS03_salePrice"/><span class="highlight"><s:text name="global.page.required"/></span></label></th><%-- 销售价格 --%>
          <td><span><s:textfield name="salePrice" cssClass="price" maxlength="17" onkeyup="BINOLPTJCS00.setMemPrice(this);return false;"/><s:text name="JCS03_moneryUnit"/></span></td>
          <th>
          	<span style="background-color:#FFDDBB;border:1px solid red; padding:2px;"><s:text name="JCS03_memPrice"/></span>
          	<span class="calculator" title="<s:text name='JCS03.calculatorPrice'/>" onclick="BINOLPTJCS00.initRateDiv(this);"></span>
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
<jsp:include page="/WEB-INF/jsp/common/popUploadFile.jsp" flush="true" />
<input type="hidden" id="dateHolidays" name="dateHolidays" value="${holidays}"/>
<div class="hide">
    <div id="disableTitle"><s:text name="JCS03_disableTitle" /></div>
    <div id="disableMessage"><p class="message"><span><s:text name="JCS03_disableMessage" /></span></p></div>
    <div id="dialogConfirm"><s:text name="global.page.ok" /></div>
    <div id="dialogCancel"><s:text name="global.page.cancle" /></div>
</div>
<div class="hide" id="dialogInit"></div>   
<div class="rateDialog hide">
	<s:text name="JCS03.discountRate"/><%-- 折扣率 --%>
	<input class="number" id="memRate" value="100" onblur="BINOLPTJCS00.closeDialog(this);return false;"  
		onkeyup="BINOLPTJCS00.setMemPrice();return false;"/><s:text name="global.page.percent"/>
</div>
</s:i18n>
