<!-- BA模式 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/bs/emp/BINOLBSEMP07.js"></script>
<script type="text/javascript" src="/${CHERRY_CONTEXT_PATH}/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript">
		// 省份选择
		$("#province").click(function(){
			// 显示省份列表DIV
			bscom03_showRegin(this,"provinceTemp");
		});
		// 城市选择
		$("#city").click(function(){
			// 显示城市列表DIV
			bscom03_showRegin(this,"cityTemp");
		});
		//一级代理商下拉框绑定
		var optionParent = {
				elementId:"parentResellerName",
				selectedCode:"parentResellerCode",
				selected:"name",
				showNum:20,
				flag:"1",
				provinceKey:"provinceId",
				cityKey:"cityId",
				resellerTypeKey:"resellerType"
				
		};
		resellerInfoBinding(optionParent);
		
		// 二级代理商下拉框绑定
		var option = {
				elementId:"resellerName",
				selectedCode:"resellerCode",
				selected:"name",
				showNum:20,
				flag:"2",
				parentKey:"parentResellerCode",
				provinceKey:"provinceId",
				cityKey:"cityId",
				resellerTypeKey:"resellerType"
		};
		resellerInfoBinding(option);
		// 查询
		BINOLBSEMP07.baSearch();
	
</script>

<s:i18n name="i18n.bs.BINOLBSEMP07">
<div class="hide">
	<s:url id="search_url" value="/basis/BINOLBSEMP07_search"/>
    <a id="searchUrl" href="${search_url}"></a>
    <s:url id="createCouponInit_url" value="/basis/BINOLBSEMP07_createCouponInit"/>
    <a id="createCouponInitUrl" href="${createCouponInit_url}"></a>
    <s:url id="createCoupon_url" value="/basis/BINOLBSEMP07_createCoupon"/>
    <a id="createCouponUrl" href="${createCoupon_url}"></a>
</div>
<div id="errorMessage"></div>
<%-- ================== 错误信息提示 START ======================= --%>
<div id="errorDiv2" style="display:none">
    <div class="actionError">
        <ul>
            <li><span id="errorSpan2"></span></li>
        </ul>
    </div>
</div>
<%-- ================== 错误信息提示   END  ======================= --%>
<div id="actionResultDisplay"></div>
	<div class="panel-content">
        <div class="box">
		<cherry:form id="baModelForm" class="inline">
			<s:hidden name="provinceId" id="provinceId"/>
          	<s:hidden name="cityId" id="cityId"/>	
            <div class="box-header"></div>
            <div class="box-content clearfix">
              <div class="column" style="width:49%; height:auto;">
              	<p>
               		<%--代理商类别--%>
               		<label style="width:100px;"><s:text name="EMP07_resellerType"/></label>
               		<s:text name="global.page.all" id="EMP07_selectAll"/>
               		<s:select name="resellerType" list="#application.CodeTable.getCodes('1314')" onchange="BINOLBSEMP07.clearResellerVal();return false;"
              				listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{EMP07_selectAll}"/>
               	</p>
               	<p>
					<label style="width:100px;"><s:text name="EMP07_provinceName"/></label>
					<a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
						<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
						<span class="ui-icon ui-icon-triangle-1-s"></span>
					</a>						
				</p>
				<p>
					<label style="width:100px;"><s:text name="EMP07_cityName"/></label>
					<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
					    <span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
					    <span class="ui-icon ui-icon-triangle-1-s"></span>
					</a>						
				</p>
              </div>
              <div class="column last" style="width:50%; height:auto;">
              	<p>
                	<label style="width:100px;"><s:text name="EMP07_reseller1L" /></label>
                	<input type="hidden" name="parentResellerCode" id="parentResellerCode"></input>
                    <input type="text" class="text" id="parentResellerName"></input>
                </p>
                 <p>
                	<label style="width:100px;"><s:text name="EMP07_reseller2L" /></label>
                	<input type="hidden" name="resellerCode" id="resellerCode"></input>
                    <input type="text" class="text" id="resellerName"></input>
                </p>
             </div>
            </div>
            <p class="clearfix">
              <button class="right search" type="button" onclick="BINOLBSEMP07.baSearch();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="EMP07_search" /></span>
              </button>
            </p>
		</cherry:form>
        </div>
        <div id="section" class="section hide">
     		<div class="section-header">
     		<strong>
     			<span class="ui-icon icon-ttl-section-search-result"></span>
     			<s:text name="global.page.list"/>
    		</strong>
   			</div>
          <div class="section-content">
            <div class="toolbar clearfix">
            	<span class="left">
	            	<cherry:show domId="BINOLBSEMP07CRT">
					    <%-- 添加按钮 --%>
						<a class="add" id="createCouponButton" onclick="BINOLBSEMP07.popCreateCouponDialog();return false;">
				       	<span class="button-text"><s:text name="EMP07_createCoupon"/></span>
				       	<span class="ui-icon icon-add"></span>
				       	</a>
					</cherry:show>
            	</span>
            </div>
            <table id="dataTableBaModel" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
              <thead>
                <tr>
                  <th><input type="checkbox" id="checkAll" onclick="BINOLBSEMP07.checkBaRecord(this,'#dataTableBaModel_Cloned');"/><s:text name="global.page.selectAll"/></th>
                  <th class="center"><s:text name="EMP07_reseller1L" /></th><!-- 二级代理商 -->
                  <th class="center"><s:text name="EMP07_reseller2L" /></th><!-- 二级代理商 -->
                  <th class="center"><s:text name="EMP07_batchCount" /></th>
                  <th class="center"><s:text name="EMP07_couponCount" /></th>
                </tr>
              </thead>
          	</table>
         </div>
      </div>
	</div>
<div class="hide">
<!-- 隐藏国际化文本内容 -->
<div id="createCouponTitle"><s:text name="EMP07_createCoupon" /></div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
<div id="saveSuccessTitle"><s:text name="EMP07_save_success" /></div>
</div>
<s:text name="global.page.select" id="select_default"/>
<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="bscom03_getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="provinceId" />" onclick="bscom03_getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM00_querySubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>