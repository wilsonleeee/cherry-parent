<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<script type="text/javascript" src="/Cherry/js/common/cherryDate.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM77.js?V=20160812"></script>
<s:i18n name="i18n.ss.BINOLSSPRM77">
<div class="main container clearfix" style="width:100%; height:100%; overflow:auto;" >
	<div class="panel ui-corner-all">
		<div id="div_main">
			
			<s:text id="selectAll" name="global.page.all"/>
			<%-- ================== 错误信息提示 START ======================= --%>
				<div id="errorMessage"></div>
			<%-- ================== 错误信息提示   END  ======================= --%>
			
			<div class="panel-content">
				<div class="box">
					<%-- form start --%>
		        	<cherry:form id = "mainForm" class ="inline" >
						<%--查询条件 --%>
			            <div class="box-header"> <strong><span class="ui-icon icon-ttl-search"></span><s:text name="global.page.condition"/></strong><%-- <input id ="ruleValidFlag" type="checkbox" value="1" name="ruleValidFlag">包含停用规则--%></div>
			            <div class="box-content clearfix">
			            	<div class="column" style="width:19%; height:30px;border-right: 0px">
			            		<p>
			            			<!-- 会员BP号 -->
			            			<label style="width:60px;"><s:text name="BPCode"/></label>
			            			<span class="text">
			            				${bpCode }
			            			</span>
			            		</p>
			            	</div>
			            	<div class="column" style="width:19%; height:30px;border-right: 0px">
			            		<p>
			            			<!-- 会员卡号 -->
			            			<label style="width:60px;"><s:text name="MemCount"/></label>
			            			<span class="text">
			            				${memCount }
			            			</span>
			            		</p>
			            	</div>
			            	<div class="column" style="width:19%; height:30px;border-right: 0px">
			            		<p>
			            			<!-- 会员姓名 -->
			            			<label style="width:60px;"><s:text name="MemName"/></label>
			            			<span class="text">
			            				${memName }
			            			</span>
			            		</p>
			            	</div>
			            	<div class="column" style="width:20%; height:30px;border-right: 0px">
			            		<p>
			            			<!-- 会员手机号 -->
			            			<label style="width:70px;"><s:text name="MemPhone"/></label>
			            			<span class="text">
			            				${memPhone }
			            			</span>
			            		</p>
			            	</div>
			            	<div class="column" style="width:20%; height:30px;border-right: 0px">
			                	<p>
			                  		<%-- 券状态 --%>
									<label style="width:70px;"><s:text name="Status"/></label>
									<span class="text">
										<%-- <s:select name="status" list='#application.CodeTable.getCodes("1384")' listKey="CodeKey" listValue="Value"
										headerKey="" headerValue="%{selectAll}" /> --%>
										<select name="status">
											<option value="">全部</option>
											<option value="AR"><s:text name="NotUsed"/></option>
											<option value="OK"><s:text name="IsUsed"/></option>
											<option value="CA"><s:text name="IsCancled"/></option>
											<option value="CK"><s:text name="IsUsed2"/></option>
										</select>
									</span>
			                	</p>            		
			            	</div>
			            	<div class="column" style="display:none;">
			                	<p>
			                  		<s:textfield name="bpCode" cssClass="text" id="bpCode"/>
			                  		<s:textfield name="brandCode" cssClass="text" id="brandCode"/>
			                  		<s:textfield name="memCount" cssClass="text" id="memCount"/>
			                  		<s:textfield name="memPhone" cssClass="text" id="memPhone"/>
			                	</p>            		
			            	</div>
			            </div>
		        		<div class="clearfix">
				              <button class="right search" onclick="binolssprm77.search();return false;">
				              	<span class="ui-icon icon-search-big"></span>
				              	<span class="button-text"><s:text name="global.page.search"/></span>
				              </button>
			            </div>
		        	</cherry:form>
		        	<%-- form end --%>	
				</div>
				<div class="section hide" id="coupon">
					<div class="section-header"><strong><span class="icon icon-ttl-section"></span><s:text name="global.page.list"></s:text></strong></div>
						<div class="toolbar clearfix">
							<span class="right">
						   		<%-- 列设置按钮  --%>
						   		<a class="setting">
									<span class="ui-icon icon-setting"></span>
									<span class="button-text"><s:text name="global.page.colSetting"/></span>
								</a>
						   	</span>
						</div>
						<div class="section-content">
							<table cellpadding="0" cellspacing="0" border="0" class="jquery_table" id="couponDataTable">
								 <thead>
								 	 <tr>
				         				<th><s:text name="CouponType"/></th>
				          				<th><s:text name="RuleCode"/></th>
				          				<th><s:text name="RuleName"/></th>
				          				<th><s:text name="CouponNo"/></th>
				          				<th><s:text name="Status"/></th>
				         				<th><s:text name="CreateTime"/></th>
				          				<th><s:text name="EndTime"/></th>
				         			 	<th><s:text name="FinishTime"/></th>
				         				<th><s:text name="RelateNoA"/></th>
				         			 	<th><s:text name="RelateNoB"/></th>
				         			 	<th><s:text name="Operate"/></th>
				          			</tr>
				         		</thead>
				         		<tbody>
				         		</tbody> 
							</table>
						</div>
					</div>
				
				</div> 
			
		</div>
	</div>
</div>
</s:i18n>
<div class="dialog2 clearfix" style="display:none" id="send_msg_dialog">
	<p class="clearfix message">
		<span></span>
		<img height="15px" class="hide" src="/Cherry/css/cherry/img/loading.gif"/>
	</p>
</div>
<div class="dialog2 clearfix" style="display:none" id="send_result_dialog">
	<p class="clearfix message">
		<span></span>
	</p>
</div>
<s:url action="BINOLSSPRM77_search" id="coupon_Url"></s:url>
<a href="${coupon_Url}" id="couponUrl"></a>
<s:url action="BINOLSSPRM77_sendMsg" id="send_msg" namespace="/ss"></s:url>
<a href="${send_msg}" id="sendUrl"></a>
<jsp:include page="/WEB-INF/jsp/common/dataTable_i18n.jsp" flush="true" />