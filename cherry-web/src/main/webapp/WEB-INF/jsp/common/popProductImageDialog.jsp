<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ taglib prefix="page" uri="/WEB-INF/jpager.tld"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/sunny/jquery-ui-1.8.5.custom.css" type="text/css" media="screen, projection">
<link rel="stylesheet" href="/${CHERRY_CONTEXT_PATH}/css/common/blueprint/screen.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/lib/jquery-1.7.1.min.js"></script>
<script type="text/javascript" src="/Cherry/js/common/popProductImageDialog.js?v20161014"></script>

<style type="text/css">
body {
	padding:0;
	margin:0;
	font-family: Verdana, Geneva, sans-serif;
	font-size:14px;
	background:#fff;
}
.goodsdiv {
	width:100%;/*可调为100%*/
	height:auto;/*可调为auto*/
	margin:0 auto;
	/*border: #ccc 1px solid;*/
}
.goodsbox {
	padding: 0 0 2em;
}
.goodstop {
	height:36px;
	background:#eee;
	padding:0 1em;
	border-bottom: #ccc 1px solid;
}
.goodstop label {
	line-height:36px;
	margin:0 .5em 0 0;
}
.goodstop select {
	width: 120px;
    vertical-align:middle;
    font-size: 14px;
	margin:0 1em 0 0;
}
.goodstop input.text {
	width: 180px;
    vertical-align: 1px;
}
.goodstop input.checkbox {
	zoom:1.3;
}
.goodssearch {
	width:79px;
	height:25px;
	background:url(images/button_img.png) center no-repeat;
	border: none;
	vertical-align: -5px;
	cursor:pointer;
}
.goodscontent {
	padding:1em;
}
.goodscontentsub {
	width:100%;
}
table.goodstable {
	width:100%;
	border-collapse: separate;
    border-spacing: 0;
    border:none;
}
table.goodstable td {
	width:25%;
	border:none;
}
.goodsimgbox {
	width:150px;
	height:150px;
	margin:0 auto;
	position:relative;
}
.goodsimgbox img {
	width:100%;
	height:100%;
}
.goodsimgbox img.select {
	border:1px #ff6d06 solid;
}
.select_icon {
	position:absolute;
	right:-1px;
	bottom:-1px;
	width:40px;
	height:40px;
	background:url(/Cherry/images/icon_select.png) right bottom no-repeat;
	background-size:100% 100%;
	-webkit-background-size:100% 100%;
}
.goodstextbox {
	width:150px;
	margin:.5em auto 0;
}
.goddstitle {
	border-bottom:#ccc 1px dashed;
	color:#333;
	padding:0 0 .5em;
	margin:0 0 .5em;
	height:38px;
	white-space: normal;
}
.goodstextbox .price span {
	color:#FF0000;
}
.tb-text {
    font-size: 12px;
    margin: 0;
    height: 26px;
    border: 1px solid #a7a6ac;
    width: 36px;
    background-position: -406px -41px;
    color: #666;
	padding: 3px 2px 0 3px;
    line-height: 26px;
	text-align: center;
}
.mui-amount-btn {
    display: inline-block;
    vertical-align: middle;
	line-height: 31px;
    color: #878787;
}
.mui-amount-increase {
    width: 16px;
    height: 12px;
    overflow: hidden;
    border: 1px solid #a7a6ab;
    display: block;
    font-family: tm-detail-font;
    line-height: 12px;
    font-size: 16px;
    cursor: pointer;
}
.num .goods_subtract,.num .goods_add {
	width: 20px;
    height: 20px;
    line-height:15px;
    background: #eee;
    border-radius: 10px;
    display: inline-block;
    text-align: center;
    color: #ff6d06;
    font-size: 20px;
    cursor: pointer;
    vertical-align: middle;
    border:none;
}
.goodstextbox .price,.goodstextbox .stock,.goodstextbox .num {
	margin:0 0 .5em;
	color:#666;
}
.goodsbottom {
	width:100%;
	border-top:#ccc 1px solid;
	margin:.5em 0 0 0;
	height:64px;
	padding:.5em 0;
	text-align:center;
}

</style>



<s:i18n name="">
    <%--请选择 --%>
	<s:text id="global.select" name="global.page.select"/>
	<s:text id="selectAll" name="global.page.all"/>
	<div class="goodsdiv">
		<div class="goodsbox">
          <cherry:form id="mainForm" class="inline" onsubmit="search(); return false;">
          		<div class="goodstop">
					<label><s:text name="产品品牌"/></label>
					<s:select name="originalBrand" list='#application.CodeTable.getCodes("1299")' listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{selectAll}"/>
                	<s:hidden name="queStr"></s:hidden>
					<label><s:text name="产品分类"/></label>
					<select id="productCategoryId" name="productCategoryId">
                		<option value="">请选择</option>
                		<s:if test="productCategoryList.size()>0">
	                		<s:iterator value="productCategoryList" var="pro">
	                			<s:if test='productCategoryTemp == prtCatPropValueID'>
	                				<option value="${pro.prtCatPropValueID}" selected="selected">${pro.PropValueChinese}</option>
	                			</s:if>
	                			<s:else>
	                				<option value="${pro.prtCatPropValueID}">${pro.PropValueChinese}</option>
	                			</s:else>
	                		</s:iterator>
                		</s:if>
                	</select>
					<label><s:text name="产品名称"/></label>
					<s:textfield name="nameTotal"></s:textfield>
					<span>
						<input type="checkbox" id="isNewProductCheckbox" name="isNewProductCheckbox" onclick="newProductCheck(this)"/>
               		 	<input type="hidden" name="isNewProductFlag" id="isNewProductFlag" value="${isNewProductFlag}"/>
						<label>新品</label>
					</span>
					<button class="search" type="submit" onclick="search();return false">
              			<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="global.page.search"/></span>
              		</button>
				</div>
          </cherry:form>          
          <div class="goodscontent">
			<div class="goodscontentsub">
				<table id="dataTable" class="goodstable">
            		<page:pager dz="20">  <!--  dz代表默认的显示记录条数，如果不指定，则默认为20条-->
              			<s:iterator value="popProductInfoList" id="productMap" status="r">
               			<page:item nr="${r.count}"> <!--  nr表示要处理的是哪个页面的记录,${r.count}显示1,2,3....序号-->
               				<c:if test="${r.count%4==1}"><tr></c:if>
               				<td>
								<div class="goodsimgbox">
									<img  id="productImageUrl"  class=""  src="${productMap.productImagePath}"   style="cursor: pointer" onclick="changeBox(this)"> 
									<!-- <img id="productImageUrl"  class=""  alt="" src="/Cherry/images/default.jpg"  style="cursor: pointer" onclick="changeBox(this)"/>-->
									<span id="productImageSelect" class="" ></span>
									<input type="hidden"  id="prtVendorId" name="prtVendorId" value="${productMap.BIN_ProductVendorID}"/>
             				 				<input type="hidden"  id="unitCodeArr" name="unitCodeArr" value="${productMap.unitCode}"/>
             				 				<input type="hidden"  id="barCodeArr" name="barCodeArr" value="${productMap.barCode}"/>
             				 				<input type="hidden"  id="productName" name="productName" value="${productMap.nameTotal}"/>
             				 				<input type="hidden"  id="amount" name="amount" value="${productMap.amount}"/>
             				 				<input type="hidden"  id="distributionPrice" name="distributionPrice" value="${productMap.distributionPrice}"/>
             				 				<input type="hidden"  id="reasonArr" name="reasonArr" value=""/>
								</div>
								<div class="goodstextbox">
									<div class="goddstitle" title="${productMap.nameTotal}">
										<s:if test="nameTotal.length()>21">  
											<s:property value="nameTotal.substring(0,20)"/>...
										</s:if>  
										<s:else>  
											${productMap.nameTotal}
										</s:else>
									</div>
									<div class="price">价格：<span>${productMap.distributionPrice}</span></div>
									<div class="stock">库存：<span>${productMap.amount}</span></div>
									<div class="num">
										数量：
										<span class="goods_subtract" onclick="upOrdown(this,'down')">-</span>
										<input id="quantityArr" name="quantityArr" type="text" class="tb-text mui-amount-input" value="1" maxlength="9" title="请输入购买量" onkeyup="validAmount(this)" onchange="validAmount(this)">
										<span class="goods_add" onclick="upOrdown(this,'up')">+</span>
									</div>
								</div>
							</td>
                			<c:if test="${r.count%4==0 || r.isLast()}"></tr></c:if>
               			</page:item>
              			</s:iterator>
            		</page:pager> 
				</table>
            </div>
      </div>
      <div class="goodsbottom">
      	<page:bt></page:bt><!-- 这行显示最下面的导航栏  -->
		<div id="submitDiv" style="margin-top:20px;" class="center clearfix">
			<button class="confirm" type="button" onclick="productSave();return false;">
		      		<span class="ui-icon icon-confirm"></span>
		      		<span class="button-text"><s:text name="加入订单"/></span>
		      	</button>
		      	<button class="close" onclick="window.close();return false;">
		      		<span class="ui-icon icon-close"></span>
				<%-- 关闭 --%>
				<span class="button-text"><s:text name="global.page.close"/></span>
			</button>
		</div>
      </div>
    </div>
    </div>
</s:i18n>
<%-- 产品图片列表模式URL --%>
<div style="display:none;">
<s:url id="search_url" value="/common/BINOLCM02_popPrtImageDialog" />
<span id ="search_url" class="hide">${search_url}</span>
</div>
  
