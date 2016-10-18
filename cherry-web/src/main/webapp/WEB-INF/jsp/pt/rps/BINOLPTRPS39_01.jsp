<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib prefix="cherry" uri="/cherry-tags" %>
<%-- ======================此段代码固定 开始======================= --%>
<div id="sEcho"><s:property value="sEcho" /></div>
<div id="iTotalRecords"><s:property value="iTotalRecords" /></div>
<div id="iTotalDisplayRecords"><s:property value="iTotalDisplayRecords" /></div>
<%-- ======================此段代码固定 结束======================= --%>
<!-- 反向催单 -->
<s:i18n name="i18n.pt.BINOLPTRPS39">
	<div id="aaData">
		<s:iterator value="remindList" id="taking">
			<ul>
				<li>
					<%-- 催单单据号 --%> 
					<s:if test='BIN_ReminderNo != null && !"".equals(BIN_ReminderNo)'>
						<s:property value="BIN_ReminderNo"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 催单单据号 --%> 
					<s:if test='DeliverNo != null && !"".equals(DeliverNo)'>
						<s:property value="DeliverNo"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 收货柜台  --%> 
					<s:if test='ReceiveCntName != null && !"".equals(ReceiveCntName)'>
						<s:property value="ReceiveCntName"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 货物类型 --%> 
					<s:if test='BIN_CargoType != null && !"".equals(BIN_CargoType)'>
						<s:if test='"N".equals(BIN_CargoType)'>
							<s:text name="rps39_product"/>
						</s:if>
						<s:elseif test='"P".equals(BIN_CargoType)'>
							<s:text name="rps39_promotion"/>
						</s:elseif>
						<s:else>
							<s:property value="BIN_ReminderNo"/>
						</s:else>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 收货日期 --%> 
					<s:if test='Receive_Date != null && !"".equals(Receive_Date)'>
						<s:property value="Receive_Date"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 延迟天数 --%> 
					<s:if test='delayDate != null && !"".equals(delayDate)'>
						<s:property value="delayDate"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 数量 --%> 
					<s:if test='Receive_Quantity != null && !"".equals(Receive_Quantity)'>
						<s:property value="Receive_Quantity"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 制单人员 --%> 
					<s:if test='EmployeeName != null && !"".equals(EmployeeName)'>
						<s:property value="EmployeeName"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 制单人员邮箱 --%> 
					<s:if test='Email != null && !"".equals(Email)'>
						<s:property value="Email"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 制单人员电话 --%> 
					<s:if test='MobilePhone != null && !"".equals(MobilePhone)'>
						<s:property value="MobilePhone"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 货运单号 --%> 
					<s:if test='ExpressBillCode != null && !"".equals(ExpressBillCode)'>
						<s:property value="ExpressBillCode"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 第一次催单时间 --%> 
					<s:if test='Fst_ReminderTime != null && !"".equals(Fst_ReminderTime)'>
						<s:property value="Fst_ReminderTime"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 第二次催单时间 --%> 
					<s:if test='Snd_ReminderTime != null && !"".equals(Snd_ReminderTime)'>
						<s:property value="Snd_ReminderTime"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 制单日期 --%> 
					<s:if test='Trade_Date != null && !"".equals(Trade_Date)'>
						<s:property value="Trade_Date"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<%-- 备注 --%> 
					<s:if test='Comment != null && !"".equals(Comment)'>
						<s:property value="Comment"/>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<s:url id="remind_url" action="BINOLPTRPS39_reminder"/>
					<%-- 消单操作  --%> 
					<s:if test='Reminder_Count != null && !"".equals(Reminder_Count)'>
						<%-- 已消单，不用催单，记录催单次数 --%>
						<s:if test='Status == "1"'>
							<s:if test='Reminder_Count == "0"'>
								<span style="margin-left:auto;"><s:text name="rps39_none"/></span>
							</s:if>
							<s:elseif test='Reminder_Count == "1"'>
								<span style="margin-left:auto;"><s:text name="rps39_once"/></span>
							</s:elseif>
							<s:elseif test='Reminder_Count == "2"'>
								<span style="background-color:rgb(210, 208, 208);margin-left:auto;"><s:text name="rps39_twice"/></span>
							</s:elseif>
						</s:if>
						<%-- 未消单 --%>
						<s:else>
							<%-- 未催单 --%>
							<s:if test='"N".equals(BIN_CargoType)'>
								<cherry:show domId="BINOLPTRPS39Rem">
									<s:if test='Reminder_Count == "0"'>
										<a href="" class="delete" onclick="BINOLPTRPS39.remind('<s:property value="Reminder_Count"/>', '<s:property value="BIN_ReminderID"/>', '${remind_url}', '${taking.BIN_CargoType}');return false;">
						                   <span class="button-text" style="background: #ff6d06;padding:1px .8em 1px .8em;"><s:text name="rps39_remind"/></span>
						                </a>
									</s:if>
									<s:if test='Reminder_Count == "1"'>
										<a href="" class="delete" onclick="BINOLPTRPS39.remind('<s:property value="Reminder_Count"/>', '<s:property value="BIN_ReminderID"/>', '${remind_url}', '${taking.BIN_CargoType}');return false;">
						                   <span class="button-text" style="background: #ff0000;padding:1px .8em 1px .8em;"><s:text name="rps39_remind"/></span>
						                </a>
									</s:if>
								</cherry:show>
							</s:if>
							
							<s:elseif test='"P".equals(BIN_CargoType)'>
								<cherry:show domId="BINOLSSPRM70Rem">
									<s:if test='Reminder_Count == "0"'>
										<a href="" class="delete" onclick="BINOLPTRPS39.remind('<s:property value="Reminder_Count"/>', '<s:property value="BIN_ReminderID"/>', '${remind_url}', '${taking.BIN_CargoType}');return false;">
						                   <span class="button-text" style="background: #ff6d06;padding:1px .8em 1px .8em;"><s:text name="rps39_remind"/></span>
						                </a>
									</s:if>
									<s:if test='Reminder_Count == "1"'>
										<a href="" class="delete" onclick="BINOLPTRPS39.remind('<s:property value="Reminder_Count"/>', '<s:property value="BIN_ReminderID"/>', '${remind_url}', '${taking.BIN_CargoType}');return false;">
						                   <span class="button-text" style="background: #ff0000;padding:1px .8em 1px .8em;"><s:text name="rps39_remind"/></span>
						                </a>
									</s:if>
								</cherry:show>
							</s:elseif>
							
							<s:if test='Reminder_Count == "2"'>
								<span style="background-color:rgb(210, 208, 208);margin-left:auto;"><s:text name="rps39_twice"/></span>
							</s:if>
						</s:else>
					</s:if>
					<s:else>
                		-	
            		</s:else>
				</li>
				<li>
					<div id="handleDiv">
						<input type="hidden" name="csrftoken" id="csrftoken"/>
						<s:url id="rps39handle_url" value="/pt/BINOLPTRPS39_handle">
							<s:param name="reminderId">${taking.BIN_ReminderID}</s:param>
							<s:param name="cargoType">${taking.BIN_CargoType}</s:param>
							<s:param name="remInOrganizationId">${taking.BIN_OrganizationID}</s:param>
							<s:param name="parentCsrftoken">${csrftoken}</s:param>
						</s:url>
						<%-- 销单--%> 
						<s:if test='Status != null && !"".equals(Status)'>
							<%--未销单 --%>
							<s:if test='Status == "0"'>
								<s:if test='"N".equals(BIN_CargoType)'>
									<cherry:show domId="BINOLPTRPS39Han">
										<a href="${rps39handle_url }" class="delete" onclick="BINOLPTRPS39.handleInit(this, '${taking.BIN_ReminderID}', '${taking.BIN_CargoType}', '${taking.BIN_OrganizationID}');return false;">
											<span class="ui-icon icon-delete"></span>
						                   <span class="button-text" ><s:text name="rps39_handle"/></span>
						                </a>
					                </cherry:show>
								</s:if>
								<s:elseif test='"P".equals(BIN_CargoType)'>
									<cherry:show domId="BINOLSSPRM70Han">
										<a href="${rps39handle_url }" class="delete" onclick="BINOLPTRPS39.handleInit(this, '${taking.BIN_ReminderID}', '${taking.BIN_CargoType}', '${taking.BIN_OrganizationID}');return false;">
											<span class="ui-icon icon-delete"></span>
						                   <span class="button-text" ><s:text name="rps39_handle"/></span>
						                </a>
					                </cherry:show>
								</s:elseif>
							</s:if>
							<%--已消单 --%>
							<s:else>
								<span style="background-color:rgb(210, 208, 208);margin-left:auto;"><s:text name="rps39_handled"/></span>
							</s:else>
						</s:if>
						<s:else>
		               		-	
		           		</s:else>
	          		</div>
				</li>
			</ul>
		</s:iterator>
	</div>
</s:i18n>
