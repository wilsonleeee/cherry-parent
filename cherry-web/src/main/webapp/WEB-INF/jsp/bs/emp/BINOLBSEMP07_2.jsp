<!-- 批次模式 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<script type="text/javascript">
	//二级代理商下拉框绑定
	var option = {
			elementId:"resellerName",
			selectedCode:"resellerCode",
			selected:"name",
			showNum:20
	};
	resellerInfoBinding(option);
    // 节日
    var holidays = '${holidays }';
    $('#startCreateDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#endCreateDate').val();
            return [value,'maxDate'];
        }
    });
    $('#endCreateDate').cherryDate({
        holidayObj: holidays,
        beforeShow: function(input){
            var value = $('#startCreateDate').val();
            return [value,'minDate'];
        }
    });
</script>

<s:i18n name="i18n.bs.BINOLBSEMP07">
<div class="hide">
	<s:url id="batchSearch_url" value="/basis/BINOLBSEMP07_batchSearch"/>
    <a id="batchSearchUrl" href="${batchSearch_url}"></a>
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
		<cherry:form id="batchModelForm" class="inline">
            <div class="box-header"></div>
            <div class="box-content clearfix">
              <div class="column" style="width:49%; height:auto;">
              	<p>
                  <label style="width:100px;"><s:text name="EMP07_batch" /></label>
                  <input name="batchCode" type="text" class="text" />
                </p>
                <p>
                	<label style="width:100px;"><s:text name="EMP07_baName" /></label>
                	<input type="hidden" name="resellerCode" id="resellerCode"></input>
                    <input type="text" class="text" id="resellerName"></input>
                </p>
              </div>
              <div class="column last" style="width:50%; height:auto;">
                <p class="date">
                	<%-- 生成日期--%>
                    <label style="width:100px;"><s:text name="EMP07_createDate"></s:text></label>
                    <span><s:textfield id="startCreateDate" name="startCreateDate" cssClass="date"/></span>
                    - 
                    <span><s:textfield id="endCreateDate" name="endCreateDate" cssClass="date"/></span>
                </p>
                <p>
                  <label style="width:100px;"><s:text name="EMP07_couponCode" /></label>
                  <input name="couponCode" type="text" class="text" />
                </p>
             </div>
            </div>
            <p class="clearfix">
              <button class="right search" type="button" onclick="BINOLBSEMP07.batchSearch();return false;">
         		<span class="ui-icon icon-search-big"></span><span class="button-text"><s:text name="EMP07_search" /></span>
              </button>
            </p>
		</cherry:form>
        </div>
        <div id="sectionBatchModel" class="section hide">
     		<div class="section-header">
     		<strong>
     			<span class="ui-icon icon-ttl-section-search-result"></span>
     			<s:text name="global.page.list"/>
    		</strong>
   			</div>
			<div class="section-content">
	          <div id ="ui-tabs" class="ui-tabs">
	          	<ul class ="ui-tabs-nav clearfix">
		          <%--进行中--%>
		          <li id="tab1_li" class="ui-tabs-selected" onclick = "BINOLBSEMP07.baCouponBatchFilter('in_progress',this);"><a><s:text name="EMP07_in_progress"/></a></li>
		          <%--已过期--%>
		          <li id="tab2_li" onclick = "BINOLBSEMP07.baCouponBatchFilter('past_due',this);"><a><s:text name="EMP07_past_due"/></a></li>
		          <%--未开始--%>
		          <li id="tab3_li" onclick = "BINOLBSEMP07.baCouponBatchFilter('not_start',this);"><a><s:text name="EMP07_not_start"/></a></li>
		          <%--其他--%>
		          <li id="tab4_li"  onclick = "BINOLBSEMP07.baCouponBatchFilter('not_release',this);"><a><s:text name="EMP07_not_release"/></a></li>
		       	</ul>
		       	<div id="tabs-1" class="ui-tabs-panel" style="overflow-x:auto;overflow-y:hidden;">
		            <div class="toolbar clearfix">
			            <div class="toolbar clearfix">
					        <span class="right"> <%-- 设置列 --%>
								<a class="setting"> 
									<span class="ui-icon icon-setting"></span> 
									<span class="button-text"><s:text name="global.page.colSetting" /></span> 
								</a>
							</span>
						</div>
		            </div>
		            <table id="dataTableBatchModel" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" >
		              <thead>
		                <tr>
		                  <th><s:text name="EMP07_number" /></th>
		                  <th class="center"><s:text name="EMP07_batchCode" /></th>
		                  <th class="center"><s:text name="EMP07_batchName" /></th>
		                  <th class="center"><s:text name="EMP07_couponType" /></th>
		                  <th class="center"><s:text name="EMP07_parValue" /></th>
		                  <th class="center"><s:text name="EMP07_useTimes" /></th>
		                  <th class="center"><s:text name="EMP07_startDate" /></th>
		                  <th class="center"><s:text name="EMP07_endDate" /></th>
		                  <th class="center"><s:text name="EMP07_createDate" /></th>
		                  <th class="center"><s:text name="EMP07_amountCondition" /></th>
		                  <th class="center"><s:text name="EMP07_operation" /></th>
		                </tr>
		              </thead>
		          	</table>
		         </div>
		       </div>
         	</div>
      	</div>
	</div>
<div class="hide">
<!-- 隐藏国际化文本内容 -->
<div id="deleteBaCouponTitle"><s:text name="EMP07_deleteCoupon" /></div>
<div id="dialogConfirm"><s:text name="global.page.ok" /></div>
<div id="dialogCancel"><s:text name="global.page.cancle" /></div>
<div id="dialogClose"><s:text name="global.page.close" /></div>
<div id="dialogContent">
<p class="message"><span><s:text name="EMP07_confirmDelete"></s:text></span>
</div>
<div id="saveSuccessTitle"><s:text name="EMP07_delete_success" /></div>
</div>
</s:i18n>
<%-- ================== dataTable共通导入 START ======================= --%>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
<%-- ================== dataTable共通导入    END  ======================= --%>