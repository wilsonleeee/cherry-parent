<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>

<s:i18n name="i18n.st.BINOLSTSFH02">
	
	<s:url id="exportUrl" action="BINOLSTSFH02_export" namespace="/st"/>
	<s:url id="searchUrl" action="BINOLWSMNG01_search"></s:url>
	<s:url id="addInitUrl" action="BINOLWSMNG01_addInit"></s:url>
	<s:text name="SFH02_selectAll" id="SFH02_selectAll"/>
	<div class="hide">
		<a id="searchUrl" href="${searchUrl}"></a>
		<a id="addInitUrl" href="${addInitUrl}"></a>
		<a id="exportUrl" href="${exportUrl}"></a>
	</div>
	     <div class="panel-header">
	      	<%-- 订货单一览 --%>
	        <div class="clearfix"> 
	        <span class="breadcrumb left">	    
				<jsp:include page="/WEB-INF/jsp/common/navigation.inc.jsp" flush="true" />
			</span>
			<span class="right">
		      <a class="add" href="${addInitUrl}" onclick="javascript:openWin(this);return false;">
		    	<span class="ui-icon icon-add"></span>
		    	<span class="button-text"><s:text name="global.page.add"></s:text></span>
		      </a>
		    </span>
	        </div> 
	      </div>
	     <%-- ================== 错误信息提示 START ======================= --%>
   			 <div id="errorMessage"></div>    
   			 <div style="display: none" id="errorMessageTemp">
   			 <div class="actionError">
    		</div>
    	</div>
   		 <%-- ================== 错误信息提示   END  ======================= --%>
	      <div id="actionResultDisplay"></div>
	      <div class="panel-content">
	        <div class="box">
	          <cherry:form id="mainForm" class="inline">
	           <input type="hidden" id="currentUnitID" name="currentUnitID" value="BINOLSTSFH02"/>
	           <s:hidden name="params"></s:hidden>
	            <div class="box-header">
	            <strong>
	            	<span class="ui-icon icon-ttl-search"></span>
	            	<%-- 查询条件 --%>
	            	<s:text name="SFH02_condition"/>
	            </strong>
	               </div>
	            <div class="box-content clearfix">
	              <div class="column" style="width:50%;height:90px;">
	                <p id="dateCover" class="date">
	                <%-- 加入日期 --%>
	                  <label><s:text name="SFH02_date"/></label>
	                  <span><s:textfield name="startDate" cssClass="date"/></span> - <span><s:textfield name="endDate" cssClass="date"/></span>
	                </p>    
	                <p>
	               	<%-- 订单编号 --%>
	                  <label><s:text name="SFH02_orderNo"/></label>
	                  <s:textfield name="orderNo" cssClass="text" maxlength="40" onblur="ignoreCondition(this);return false;"/>
	                </p>
	              </div>
	              <div class="column last" style="width:49%;height:55px;">      
	                 <p>
	               	<%--处理状态--%>
	                 <label><s:text name="SFH02_tradeStatus"/></label>
	                 <s:select name="tradeStatus" list='#application.CodeTable.getCodes("1142")' 
	                 	listKey="CodeKey" listValue="Value" headerKey="" headerValue="%{SFH02_selectAll}"/>
	                 </p>
	              </div>
	              
	            </div>
	            <p class="clearfix">
	                <%-- 查询 --%>
	                <%--
                    <button class="right search"  onclick="test_creatOrder();return false">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text">TEST</span>
	              	</button>
                    --%>
	              	<button class="right search"  id="search">
	              			<span class="ui-icon icon-search-big"></span>
	              			<span class="button-text"><s:text name="SFH02_search"/></span>
	              	</button>
	            </p>
	          </cherry:form>
	        </div>
	        <div id="section" class="section hide">
	          <div class="section-header">
	          <strong>
	          	<span class="ui-icon icon-ttl-section-search-result"></span>
	          	<%-- 查询结果一览 --%>
	          	<s:text name="SFH02_results_list"/>
	         </strong>
	        </div>
	          <div class="section-content" id="result_list">
	            <div class="toolbar clearfix">
	            <cherry:show domId="BINOLWSMNG01EXP">
                    <a class="export left" onclick="javascript:exportExcel(this);return false;"  href="${exportUrl}">
						<span class="ui-icon icon-export"></span>
						<span class="button-text"><s:text name="global.page.export"></s:text></span>
					</a>
	            </cherry:show>
	            <span id="headInfo" style=""></span>
	            <span class="right"><a class="setting"><span class="ui-icon icon-setting"></span>
	            <span class="button-text">
	             <%-- 设置列 --%>
	             <s:text name="SFH02_colSetting"/>
	             </span></a></span></div>
	             
	             <table cellpadding="0" cellspacing="0" border="0" class="jquery_table" width="100%" id="dataTable">
	              <thead>
	                <tr>	             
	                 <th><input type="checkbox" id="allSelect" onclick="checkBillAll(this);"/><s:text name="global.page.selectAll"/></th><%-- 选择 --%>
	                  <th><s:text name="SFH02_orderNo"/></th><%-- 订单单号--%>
	                  <th><s:text name="SFH02_tradeDateTime"/></th><%-- 订货时间--%>
	                  <th><s:text name="SFH02_totalQuantity"/></th><%-- 总数量--%>
	                  <th><s:text name="SFH02_totalAmount"/></th><%-- 总金额--%>
	                  <th><s:text name="SFH02_tradeStatus"/></th><%-- 处理状态--%>
	                  <th><s:text name="SFH02_verifiedFlag"/></th><%-- 审核区分--%>
                      <th><s:text name="SFH02_createEmployee"/></th><%-- 制单员工--%>
	                </tr>
	              </thead>
	            </table>
	          </div>
	        </div>
	      </div>
	 </s:i18n>
	    <%-- ================== dataTable共通导入 START ======================= --%>
		<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />
		<%-- ================== dataTable共通导入    END  ======================= --%>
		<%-- ================== 打印预览共通导入 START ======================= --%>
		<jsp:include page="/applet/printer.jsp" flush="true" />
		<%-- ================== 打印预览共通导入 END ========================= --%>
		<script type="text/javascript">
		$(function() {
			// 节日
			var holidays = '${holidays }';
			$('#startDate').cherryDate({
				holidayObj: holidays,
				beforeShow: function(input){
					var value = $('#endDate').val();
					return [value,'maxDate'];
				}
			});
			$('#endDate').cherryDate({
				holidayObj: holidays,
				beforeShow: function(input){
					var value = $('#startDate').val();
					return [value,'minDate'];
				}
			});
			cherryValidate({			
				formId: "mainForm",		
				rules: {
					startDate: {dateValid:true},	// 开始日期
					endDate: {dateValid:true}	// 结束日期
				}		
			});
			productBinding({elementId:"nameTotal",showNum:20});
			
			$("#search").click(function(){
				var $form = $('#mainForm');
				$form.find(":input").each(function() {
					$(this).val($.trim(this.value));
				});
				if (!$form.valid()) {
					return false;
				};
				
				var aoColumnsArr = [	{ "sName": "checkbox","bSortable": false,"sWidth": "5%"},			//0
				                    	{ "sName": "orderNoIF","sWidth": "20%"},			                    //1
				                    	{ "sName": "tradeDateTime","sWidth": "20%"},								//12
										{ "sName": "totalQuantity","sWidth": "10%"},										//6
										{ "sName": "totalAmount","sWidth": "15%"},						//7
										{ "sName": "tradeStatus","sWidth": "10%"},											//8
										{ "sName": "verifiedFlag","sWidth": "10%"},											//9
										{ "sName": "binEmployeeID","sWidth": "10%"}];						//15
				var aiExcludeArr = [0,1];
				var url = $("#searchUrl").attr("href");
				// 查询参数序列化
				var params= $("#mainForm").serialize();
				if(params != null && params != "") {
					url = url + "?" + params;
				}
				 // 显示结果一览
				 $("#section").show();
				 // 表格设置
				 var tableSetting = {
						 // datatable 对象索引
						 index : 1,
						 // 表格ID
						 tableId : '#dataTable',
						 // 数据URL
						 url : url,
						 // 排序列
						 aaSorting : [[2, "desc"]],
						 // 表格列属性设置			 
						 aoColumns : aoColumnsArr,
						 // 不可设置显示或隐藏的列	
						 aiExclude :[0, 1],
						 // 横向滚动条出现的临界宽度
						 sScrollX : "100%",
						 // 固定列数
						 fixedColumns : 2,
						callbackFun : function (msg){
				 		var $msg = $("<div></div>").html(msg);
				 		var $headInfo = $("#headInfo",$msg);
				 		if($headInfo.length > 0){
				 			$("#headInfo").html($headInfo.html());
				 		}else{
				 			$("#headInfo").empty();
					 	}
				 	},
			 		fnDrawCallback:function(){}
				 };
				 // 调用获取表格函数
				 getTable(tableSetting);
				 return false;
			});
		});
		
		function ignoreCondition(_this){
			var $this = $(_this);
			if($.trim($this.val()) == ""){
				// 单据输入框为空时，日期显示
				$("#startDate").prop("disabled",false);
				$("#endDate").prop("disabled",false);
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				if(startDate == ""){
					$("#startDate").val($("#defStartDate").val());
				}
				if(endDate == ""){
					$("#endDate").val($("#defEndDate").val());
				}
				$("#COVERDIV_AJAX").remove(); //清除覆盖的DIV块
			}else{
				// 单据输入框不为空时，日期隐藏
				var datecover=$("#dateCover");  //需要覆盖的内容块的ID
				requestStart(datecover);		//内容块覆盖一块DIV来实现不允许输入日期
				$("#startDate").prop("disabled",true);
				$("#endDate").prop("disabled",true);
				var startDate = $("#startDate").val();
				var endDate = $("#endDate").val();
				if(startDate != ""){
					$("#defStartDate").val(startDate);
					$("#startDate").val("");
				}
				if(endDate != ""){
					$("#defEndDate").val(endDate);
					$("#endDate").val("");
				}
			}
		}
		
		/***
		 * 导出excel
		 * @param url
		 * @returns {Boolean}
		 */
		function exportExcel(url) {
			$("#mainForm").find(":input").each(function() {
				$(this).val($.trim(this.value));
			});
			//无数据不导出
			if($(".dataTables_empty:visible").length == 0){
			    if (!$('#mainForm').valid()) {
			        return false;
			    };
			    // 查询参数序列化
				var params = $("#mainForm").serialize();
			    //选中导出的单据，不选导出全部
			    $.each($("#dataTable_Cloned").find("[id=checkbill]:checked"), function(n){
			        params += "&checkedBillIdArr="+$(this).val();
			    });
			    url = url + "?" + params;
			    window.open(url,"_self");
			}
		}
		 
		 $("#search").click();
			
		</script>
		<input type="hidden"  id="defStartDate" value=''/>
		<input type="hidden"  id="defEndDate"	value=''/>   