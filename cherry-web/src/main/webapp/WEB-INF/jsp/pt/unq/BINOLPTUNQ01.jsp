<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/bs/common/BINOLBSCOM03.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/pt/unq/BINOLPTUNQ01.js?v=20170111"></script>
<script type="text/javascript">
$(function() {
	productBinding({elementId:"nameTotal",showNum:20,targetShow:"nameTotal",targetId:"prtVendorId"});//产品名称

	$("#genForm").find(".jis").keyup(function () {
       var reg = $(this).val().match( /^[0-9]*[1-9][0-9]*$/);
       var txt = '';
       if (reg != null) {
           txt = reg[0];
       }
       $(this).val(txt);
       
       var specVal = $("#spec").val();
       var boxCountVal = $("#boxCount").val();
       
       if(specVal == '' || boxCountVal == ''){
    	   $("#generateCountID").text(0);
    	   $("#generateCountVal").val(0);
    	   $("#generateCountID").css("color","red");
    	   
       }else if(specVal != '' && boxCountVal != ''){
    	   var generateCount = specVal * boxCountVal;
    	   $("#generateCountID").text(generateCount);
    	   $("#generateCountVal").val(generateCount);
    	   if(generateCount > 100000){
	    	   $("#generateCountID").css("color","red");
    	   }else{
	    	   $("#generateCountID").css("color","green");
    	   }
    	  // var reg = /(\d{3})/g;
    	  // var str=$("#generateCountID").text();
    	  // str.toString().replace(reg,"$1,");
       }
       
   }).change(function () {
       $(this).keypress();
       var v = $(this).val();
       if (/\.$/.test(v))
       {
           $(this).val(v.substr(0, v.length - 1));
       }
       
       var specVal = $("#spec").val();
       var boxCountVal = $("#boxCount").val();
       
       if(specVal == '' || boxCountVal == ''){
    	   $("#generateCountID").text(0);
    	   $("#generateCountVal").val(0);
    	   $("#generateCountID").css("color","red");
       }else if(specVal != '' && boxCountVal != ''){
    	   var generateCount = specVal * boxCountVal;
    	   $("#generateCountID").text(generateCount);
    	   $("#generateCountVal").val(generateCount);
    	   if(generateCount > 100000){
	    	   $("#generateCountID").css("color","red");
    	   }else{
	    	   $("#generateCountID").css("color","green");
    	   }
       }
   });
	
	cherryValidate( {
		formId : "genForm",
		rules : {
			spec : {
				required : true,
				number : true
			}, // 规格
			boxCount : {
				required : true,
				number : true
			}, // 箱数
			generateType : {
				required : true
			}, // 生成方式
			needBoxCode : {
				required : true
			}, // 是否需要箱码
			defaultActivationStatus : {
				required : true
			}, // 默认激活方式
 			prtVendorId : {
				required : false
			},  // 产品厂商ID
			generateCountVal : {
				required : true,
				generateCountValid : 100000
			} // 生成数量
		}
	});

} );
</script>
<s:i18n name="i18n.pt.BINOLPTUNQ01">

<s:url id="search_url" value="/pt/BINOLPTUNQ01_search"/> <%-- 查询唯一码生成记录 --%>
<s:hidden name="searchUrlID" value="%{search_url}"/>

<s:url id="exportExcel_url" action="BINOLPTUNQ01_exportExcel"/> <%-- 导出唯一码Excel--%>
<s:hidden name="exportExcelUrlID" value="%{exportExcel_url}"/>

<s:url id="exportCsv_url" action="BINOLPTUNQ01_exportCsv"/> <%-- 导出唯一码csv--%>
<s:hidden name="exportCsvUrlID" value="%{exportCsv_url}"/>

<s:url id="updExpExc_url" action="BINOLPTUNQ01_updExpExcCountAndExpExcTime"/> <%-- 修改导出Excel次数和导出时间--%>
<s:hidden name="updExlExpUrlID" value="%{updExpExc_url}"/>

<s:url id="generateUnqCode_url" action="BINOLPTUNQ01_generateUnqCode"/> <%-- 生成唯一码 --%>
<s:hidden name="generateUnqCodeUrlID" value="%{generateUnqCode_url}"/>

<s:url id="getNewPrtUnqBatchNo_url" action="BINOLPTUNQ01_getNewPrtUnqBatchNo"/> <%-- 取得批次号 --%>
<s:hidden name="getNewPrtUnqBatchNoUrlID" value="%{getNewPrtUnqBatchNo_url}"/>

<script type="text/javascript" >

<%-- 导出后页面刷新 --%>
window.onbeforeunload = function(){
	window.opener.location.reload();
}

</script>	
	<%-- ================== 错误信息提示 START ======================= --%>
	<div id="actionResultDisplay"></div>
	<div id="errorMessage"></div>
	<div style="display: none" id="errorMessageTemp">
		<div class="actionError">
		</div>
	</div>
	<%-- ================== 错误信息提示   END  ======================= --%>
    <div class="panel-header">
		<div class="clearfix">
			<span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
		
		</div>
    </div>

  <div class="panel-content"> 
    <div class="box-content clearfix">
			<cherry:form id="genForm" class="inline">
				<div class="box-header" style="padding-left: 10px;">
					<strong>
						<span class="ui-icon icon-ttl-section-info-edit"></span> 
						<s:text name="生成唯一码设置" />  <%-- 条件设置 --%>
					</strong>
				</div>

				<table id="genConditionId" class="detail" cellpadding="0" cellspacing="0" style="margin: 0">
					<tbody>
						<tr>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"> <s:text name="批次号" /></label> <%-- 批次号 --%> 
								</span>
								<span>
									<span id="prtUniqueCodeBatchNoSpan"><s:property value="newBatchNo"/></span>
									<s:hidden name="prtUniqueCodeBatchNo" value="%{newBatchNo}"></s:hidden>
								</span>
							</td>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"> <s:text name="批号描述" /></label> <%-- 批号描述 --%> 
								</span>
								<span>
									<s:textfield name="prtUniqueCodeBatchDescribe" cssClass="text" />
								</span>
							</td>
						</tr>
					
						<tr>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"> <s:text name="PTUNQ.generateMode" /></label> <%-- 生成方式 --%> 
									<span class="highlight"><s:text name="global.page.required"/></span>
								</span>
								<span>
									<s:select name="generateType" list='#application.CodeTable.getCodes("1397")' listKey="CodeKey" listValue="Value" />
								</span>
							</td>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"><span class="highlight"><s:text name="global.page.required"/></span><s:text name="PTUNQ.boxCode" /></label> <%-- 箱码 --%>
								</span>
								<span>
									<s:radio list="#{'1':'生成箱码','0':'不需要箱码'}" name="needBoxCode" title="请选择" value="'0'"/>
								</span>
							</td>
						</tr>
						<tr>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"><span class="highlight"><s:text name="global.page.required"/></span><s:text name="PTUNQ.defaultState" /></label> <%-- 默认状态--%> 
								</span>
								<span>
									 <s:radio  list='#application.CodeTable.getCodes("1395")' name="defaultActivationStatus"  listKey="CodeKey" listValue="Value" title="请选择" value="'1'"/>
									 <%--<input type="hidden" name="defaultActivationStatus" value="1" /> --%>
								</span>
							</td>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"><span class="highlight"><s:text name="global.page.required"/></span><s:text name="PTUNQ.specifications" /></label> <%-- 规格--%>
								</span>
								<span>
									<s:textfield name="spec" cssClass="text jis" maxlength="5"/>
								</span>
								<span class="gray"><s:text name="(每箱唯一码数量)"></s:text></span>
							</td>
						</tr>
						<tr>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"><s:text name="选择产品" /></label> <%-- 商品编码--%> 
								</span>
								<span id=nameTotals>
									<s:textfield name="nameTotal" cssClass="text" />
									<s:hidden name="prtVendorId"></s:hidden>
								</span>
							</td>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"><span class="highlight"><s:text name="global.page.required"/></span><s:text name="PTUNQ.boxCount" /></label> <%-- 箱数--%> 
								</span>
								<span>
									<s:textfield name="boxCount" cssClass="text jis" maxlength="5" />
								</span>
							</td>
						</tr>
						<tr>
							<td>
							</td>
							<td style="padding-left: 15px;">
								<span>
									<label style="width: 65px;"><s:text name="PTUNQ.productQuantity" /></label> <%-- 产品数量 --%> 
								</span>
								<strong><span id="generateCountID"></span></strong>
								<s:hidden name="generateCountVal"></s:hidden>
								<span class="gray"><s:text name="(根据箱数及规格生成，生成数最高10万条)"></s:text></span>
							</td>
							
						</tr>

						
					</tbody>
				</table>
				<p class="clearfix" style="margin-top: 10px;">
					<cherry:show domId="BINOLPTUNQ0104"> <%-- 生成按钮权限设置--%>
					<%-- 唯一码生成按钮 --%>
					<button class="right search" type="button" onclick="BINOLPTUNQ01.generateCodeInit();return false;">
						<span class="ui-icon icon-search-big"></span>
						<span class="button-text"><s:text name="PTUNQ.generate" /></span>
					</button>
					</cherry:show>
				</p>
	        </cherry:form>
	        
     </div>
     
  <div class="panel-content">  <%-- 查询条件--%>
     <cherry:form id="selectForm" class="inline">
       <div class="box-content clearfix">
                    <div class="box-header">
			          	<strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong>
                    </div>
					<div style="width: 100%; height: 20px;" class="column last">
						<p class="right">
							<label><s:text name="PTUNQ.generateDate"/></label>
		               		<span><s:textfield name="fromDate" cssClass="date" readOnly="readOnly"></s:textfield></span> - 
		              		<span><s:textfield name="toDate" cssClass="date" readOnly="readOnly"></s:textfield></span>
	               		
	               		<cherry:show domId="BINOLPTUNQ0101"> <%-- 权限设置--%> 
						<%-- 查询按钮 --%> 
						<button class="right search" type="button" onclick="BINOLPTUNQ01.search();return false;">
							<span class="ui-icon icon-search-big"></span>
							<span class="button-text"><s:text name="PTUNQ.select" /></span>
						</button>
						</cherry:show>
						</p>
					</div>
    
        </div>
      </cherry:form>
  </div>  
 	      
  <div id="section" class="section"> <%-- 生成记录具体内容--%>
       <div class="section-header">
        	<strong>
        		<span class="ui-icon icon-ttl-section-search-result"></span><%-- 查询结果一览 --%>
        		<s:text name="global.page.list"/>
        	</strong>
       </div>        
       <div class="section-content">
       			<div class="toolbar clearfix">
					<span class="right"> 
						<a class="setting">
							<span class="ui-icon icon-setting"></span> <%-- 列设置按钮  --%>
							<span class="button-text"><s:text name="global.page.colSetting" /></span>
						</a>
					</span>
				</div>
          <table id="dataTable" cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%"><%-- 表头  --%>
            <thead>
               <tr>          
                <th><input type="checkbox" id="checkAll" onclick="bscom03_checkRecord(this,'#dataTable');"/><s:text name="PTUNQ.selectAll"/></th>
                <th><s:text name="PTUNQ.number"/></th>
                <th><s:text name="PTUNQ.prodUnqBatchNo"/></th>
                <th><s:text name="PTUNQ.generateTime"/></th>
                <th><s:text name="PTUNQ.productName"/></th>
                <th><s:text name="PTUNQ.manufacturerCode"/></th>
                <th><s:text name="PTUNQ.productBarCode"/></th>
                <th><s:text name="PTUNQ.unqCount"/></th>
				<th><s:text name="PTUNQ.exportStatus"/></th>	
				<th><s:text name="PTUNQ.finalExportTime"/></th>	
				<th><s:text name="PTUNQ.describe"/></th>
				<th><s:text name="PTUNQ.operation"/></th>
              </tr>
            </thead>           
           </table>
         </div>
      </div>
    </div>
      
    <div class="hide" id="dialogInitUnq01"></div>	
    <div class="hide" id="dialogGenCodeInitDIV"></div>	
     <div style="display: none;">
       	<div id="dialogConfirmIss"><s:text name="global.page.goOn" /></div>
       	<div id="dialogCancelIss"><s:text name="global.page.cancle" /></div>
	    <p id="operateSuccessId" class="success"><span><s:text name="global.page.operateSuccess"/></span></p>
	    <p id="operateFaildId" class="message"><span><s:text name="global.page.operateFaild"/></span></p>
     
		<div id="deleteTitle14"><s:text name="PTUNQ.deleteTitle" /></div>			
		<div id="deleteMessage14"><p class="message"><span><s:text name="PTUNQ.deleteText" /></span></p></div>
		<div id="dialogConfirm14"><s:text name="global.page.goOn" /></div>
	    <div id="dialogCancel14"><s:text name="global.page.cancle" /></div>
	    <div id="dialogClose14"><s:text name="global.page.close" /></div>
    </div>
</s:i18n>

<div class="hide">
	<%-- ================== dataTable共通导入 START ======================= --%>
	<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
</div>
<%-- ================== 打印预览共通导入 START ======================= --%>
<jsp:include page="/applet/printer.jsp" flush="true" />
<%-- ================== 打印预览共通导入 END ========================= --%>
