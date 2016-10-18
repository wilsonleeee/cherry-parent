<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/st/yldz/BINOLSTYLDZ01.js"></script>
<%-- 银联对账URL --%>
<div class="hide">
	<s:url id="importInit_url" value="/st/BINOLSTYLDZ01_importInit"/>
	<!-- 查询 -->
	<s:url id="s_dgSearch" value="/st/BINOLSTYLDZ01_Search"></s:url>
	<a id="searchUrl" href="${s_dgSearch}"></a>
	<!-- 删除 -->
	<s:url id="s_dgDelete" value="/st/BINOLSTYLDZ01_delete"></s:url>
	<a id="deleteUrl" href="${s_dgDelete}"></a>
	<!-- 添加初始化 -->
	<s:url id="s_dgAddInit" value="/st/BINOLSTYLDZ01_addBankBillInit"></s:url>
	<!-- 添加 -->
	<s:url id="s_dgAddBankBill" value="/st/BINOLSTYLDZ01_addBankBill"></s:url>
	<a id="addBankBillUrl" href="${s_dgAddBankBill}"></a>
</div>
<s:i18n name="i18n.st.BINOLSTYLDZ01">
<cherry:form id="mainForm"  class="inline">
<s:text name="global.page.all" id="select_default"/>
    <div class="panel-header">
        <div class="clearfix"> 
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right"> 
				<a onclick="javascript:openWin(this);return false;" class="add"  href="${s_dgAddInit}">
					<span class="button-text"><s:text name="styldz01.add"></s:text></span>
					<span class="ui-icon icon-add"></span>
				</a>
				<a onclick="javascript:openWin(this);return false;" class="add"  href="${importInit_url}">
					<span class="button-text"><s:text name="styldz01.import"></s:text></span>
					<span class="ui-icon icon-add"></span>
				</a>
			</span>
        </div>
    </div>
 <div id="errorMessage"></div>
 <div id="actionResultDisplay"></div>
<div class="panel-content clearfix">
<div class="box">
  
    <div class="box-header"> 
      <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
    </div>
    <div class="box-content clearfix">
      <div class="column" style="width:50%; height: auto;">
     	<p>
          <label style="width:80px;"><s:text name="styldz01.sysBillCode"></s:text></label>
            <span>
                  <s:textfield name="sysBillCode" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.hedgingBillCode"></s:text></label>
            <span>
                  <s:textfield name="hedgingBillCode" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.posBillCode"></s:text></label>
            <span>
                  <s:textfield name="posBillCode" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.referenceCode"></s:text></label>
            <span>
                  <s:textfield name="referenceCode" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.posCode"></s:text></label>
            <span>
                  <s:textfield name="posCode" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.companyCode"></s:text></label>
            <span>
                  <s:textfield name="companyCode" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.cardCode"></s:text></label>
            <span>
                  <s:textfield name="cardCode" cssClass="text"></s:textfield>
			</span>
        </p>
      </div>
      <div class="column last" style="width:49%; height: auto;">
      	<p>
          <label style="width:80px;"><s:text name="styldz01.companyName"></s:text></label>
            <span>
                  <s:textfield name="companyName" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.tradeResult"></s:text></label>
            <span>
                  <s:textfield name="tradeResult" cssClass="text"></s:textfield>
			</span>
        </p>
        <p>
          <label style="width:80px;"><s:text name="styldz01.tradeAnswer"></s:text></label>
            <span>
                  <s:textfield name="tradeAnswer" cssClass="text"></s:textfield>
			</span>
        </p>
      	<p>
          <label style="width:80px;"><s:text name="styldz01.batchId"></s:text></label>
            <span>
                  <s:textfield name="batchId" cssClass="text"></s:textfield>
			</span>
        </p>
      	<p>
          <label style="width:80px;"><s:text name="styldz01.saleType"></s:text></label>
            <span>
                  <s:textfield name="tradeType" cssClass="text"></s:textfield>
			</span>
        </p>
        <p class="date">
        	<label style="width:80px;"><s:text name="styldz01.tradeDate"></s:text></label>
            <span>
            	<s:textfield id="tradeDateStart" name="tradeDateStart" cssClass="date"/>
            </span> - 
            <span>
            	<s:textfield id="tradeDateEnd" name="tradeDateEnd" cssClass="date"/>
            </span>
        </p>
      </div>
    </div>
    <p class="clearfix">
      <button class="right search" onclick="BINOLSTYLDZ01.search();return false;">
	      <span class="ui-icon icon-search-big"></span>
		      <span class="button-text">
		      <s:text name="global.page.search"></s:text>
	      </span>
      </button>
    </p>
</div>
<div class="section hide" id="section">
  <div class="section-header">
  	<strong>
	  	<span class="ui-icon icon-ttl-section-search-result"></span>
	  	<s:text name="global.page.list"></s:text>
  	</strong>
	<span class="right"> <%-- 设置列 --%>
			<a class="setting"> 
				<span class="ui-icon icon-setting"></span> 
				<span class="button-text"><s:text name="global.page.colSetting" /></span> 
			</a>
	</span>
  </div>

  <div class="section-content">
    <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="saleListTable">
      <thead>
        <tr>
          <th><s:text name="styldz01.Num"></s:text></th>
          <th><s:text name="styldz01.tradeDate"></s:text></th>
          <th><s:text name="styldz01.tradeTime"></s:text></th>
          <th><s:text name="styldz01.cardCode"></s:text></th>
          <th><s:text name="styldz01.companyCode"></s:text></th>
          <th><s:text name="styldz01.companyName"></s:text></th>
          <th><s:text name="styldz01.posCode"></s:text></th>
          <th><s:text name="styldz01.posBillCode"></s:text></th>
          <th><s:text name="styldz01.sysBillCode"></s:text></th>
          <th><s:text name="styldz01.hedgingBillCode"></s:text></th>
          <th><s:text name="styldz01.referenceCode"></s:text></th>
          <th><s:text name="styldz01.tradeType"></s:text></th>
          <th><s:text name="styldz01.amount"></s:text></th>
          <th><s:text name="styldz01.tradeResult"></s:text></th>
          <th><s:text name="styldz01.tradeAnswer"></s:text></th>
          <th><s:text name="styldz01.batchId"></s:text></th>
          <th><s:text name="styldz01.act"></s:text></th>
        </tr>
      </thead>
      <tbody>
      </tbody>
    </table>
  </div>
</div>
</div>
 </cherry:form>
<div class="hide" id="messageDialogTitle"><s:text name="styldz01.message"></s:text></div>
<div id="messageDialogDiv" class="hide ui-dialog-content ui-widget-content" style="display: none; width: auto; min-height: 200px;">
	<p id="messageContent" class="message hide" style="margin:40px auto 30px;"><span id="messageContentSpan"></span></p>
	<p id="successContent" class="success hide" style="margin:40px auto 30px;"><span id="successContentSpan"></span></p>
	<p class="center">
		<button id="btnMessageConfirm" class="close" type="button">
    		<span class="ui-icon icon-confirm"></span>
            <span class="button-text"><s:text name="styldz01.confirm"/></span>
		</button>
	</p>
</div>
</s:i18n>  
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
 <script type="text/javascript">
	 	$(document).ready(function() {
	 	    BINOLSTYLDZ01.search();
	        var holidays = '${holidays }';
	        $('#tradeDateStart').cherryDate({
	            holidayObj: holidays,
	            beforeShow: function(input){
	                var value = $('#tradeDateEnd').val();
	                return [value,'maxDate'];
	            }
	        });
	        $('#tradeDateEnd').cherryDate({
	            holidayObj: holidays,
	            beforeShow: function(input){
	                var value = $('#tradeDateStart').val();
	                return [value,'minDate'];
	            }
	        });
		});
    </script>