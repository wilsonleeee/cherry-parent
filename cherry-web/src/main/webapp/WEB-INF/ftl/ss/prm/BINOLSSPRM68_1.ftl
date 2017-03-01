<div class="box4">
	<div class="box4-header">
 		<strong><@s.text name="baseInfo" /><@s.text name="setting" /></strong>
		<a href="#" class="add right" onclick="PRM68_1.addPrmActGrop();"><span class="ui-icon icon-add"></span><span class="button-text"><@s.text name="addActGrp" /></span></a>
		<#--<a href="#" class="add right" onclick="PRM68_1.eidtPrmActGrop();"><span class="ui-icon icon-edit"></span><span class="button-text">编辑主活动</span></a>-->
	</div>
	<div class="box4-content">
		<table class="detail">
           	<tbody>
               	<tr>
                   <th><@s.text name="ruleName" /><span class="red">*</span></th>
                   <td>
                   		<span><input class="text" name="pageA.prmActiveName" maxlength="50" value="${pageA.prmActiveName!}"/></span>
                   		<input type="hidden" name="pageA.activeID" value="${activeID!}"/>
                   		<input type="hidden" name="pageA.ruleID" value="${pageA.ruleID!}"/>
                   		<input type="hidden" name="pageA.activityCode" value="${pageA.activityCode!}"/>
                   		<input type="hidden" name="pageA.level" value="${pageA.level!0}"/>
                   		<input type="hidden" name="pageA.enContinue" value="${pageA.enContinue!'1'}"/>
                   		<input type="hidden" name="pageA.OPT_KBN" value="${pageA.OPT_KBN!}"/>
						<input type="hidden" name="pageA.mainModify" value="0"/>
                        <input type="hidden" name="pageA.sendFlag" value="${pageA.sendFlag!}"/>
						<input type="hidden" name="pageA.updTime" value="${pageA.updTime!}"/>
						<input type="hidden" name="pageA.modCount" value="${pageA.modCount!}"/>
                       <input type="hidden" name="pageA.couponFlag" value="${pageTemp.couponFlag!}"/>
                   </td>
                   <th><@s.text name="brandName" /><span class="highlight">*</span></th>
                   <td>
	                   <select style="width:185px;" name="pageA.brandInfoId" >
	                   	 <#list pageTemp.brandList! as brand>
		      				 <option <#if pageA.brandInfoId?exists && pageA.brandInfoId + '' == brand.brandInfoId?c>selected="selected"</#if> value="${brand.brandInfoId!?c}">${brand.brandName!}</option>
		      			 </#list>
	                   </select>
                   </td>
               	</tr>
			  	<tr>
				  	<th><@s.text name="prmActGrp" /><span class="highlight">*</span></th>
				  	<td colspan="">
					  	<span>
					  		<select name="pageA.prmActGrp" id="prmActGrp" class="left" style="width:187px;">
							    <option value=""></option>
							    <#list pageTemp.prmActGrpList! as actGrp>
				      				 <option <#if pageA.prmActGrp! == actGrp.promotionActGrpID?c>selected="selected"</#if> value="${actGrp.promotionActGrpID!?c}">${actGrp.groupName!}</option>
				      			</#list>
							</select>
						</span>
				  		<div id="groupInfo"></div>
				  	</td>
				  	<th><div><@s.text name="subBrand" /></div></th>
					  <td>
					  	<select name="pageA.subBrandCode" style="width:187px;">
					  		<option value=""><@s.text name="global.page.all" /></option>
							<@getOptionList list=application.CodeTable.getCodes("1299") val=(pageA.subBrandCode)!''/>
						</select>
					  </td>
			  	</tr>
				<tr>
				  <th><@s.text name="shortCode" /></th>
				  <td><span><input class="text" name="pageA.shortCode" maxlength="10" value="${pageA.shortCode!}"/></span></td>
				  <th><div><@s.text name="maxExecCount" /><span class="highlight">*</span></div></th>
				  <td><span><input class="text" name="pageA.maxExecCount" maxlength="10" value="${pageA.maxExecCount!'1'}"></span></td>
			  	</tr>
			  	<tr>
			  	  <th><@s.text name="useCoupon" /></th>
				  <td><@getRadioList list=useCouponList name="pageA.useCoupon" val=(pageA.useCoupon)!'1'/></td>
				  <th><div><@s.text name="subCampValid" /></div></th>
				  <td>
				  	<select id="subCampValid" name="pageA.subCampValid" style="width:187px;"
							<#if (pageTemp.couponFlag?exists && pageTemp.couponFlag == '1')
									&& (pageA.systemCode?exists && pageA.systemCode != '')>disabled="disabled"</#if> >
						<@getOptionList list=application.CodeTable.getCodes("1230") val=(pageA.subCampValid)!'0'/>
					</select>
				  <#if (pageTemp.couponFlag?exists && pageTemp.couponFlag == '1')
				  		&& (pageA.systemCode?exists && pageA.systemCode != '')>
				   		<input type="hidden" name="pageA.subCampValid" value="2"/>
				   </#if>
				  </td>
			  	</tr>
			  	<tr>
				  <th><div><@s.text name="zgqFlag" /></div></th>
				  <td><@getRadioList list=zgqFlagList name="pageA.zgqFlag" val=(pageA.zgqFlag)!'0'/></td>
				  <#-- 终端促销模式-->
					<th><@s.text name="needBuyFlagTxt"/></th>
					<td><@getRadioList name="pageA.needBuyFlag" list=needBuyFlagList val=(pageA.needBuyFlag)!'1' /></td>
			  	</tr>
			  	<tr>
				  <th><div><@s.text name="isMust" /></div></th>
				  <td><@getRadioList list=isMustList name="pageA.isMust" val=(pageA.isMust)!'0'/></td>
				  <th rowspan="<#if pageTemp.couponFlag == '1'>3<#else>2</#if>"><@s.text name="descriptionDtl" /></th>
				  <td rowspan="<#if pageTemp.couponFlag == '1'>3<#else>2</#if>">
				  	<textarea name="pageA.descriptionDtl" maxlength="100" style="height: 50px;padding:0px;">${pageA.descriptionDtl!}</textarea>
				  </td>
			  	</tr>
			  	<tr>
				  <th><@s.text name="enMessage" /></th>
				  <td><@getRadioList list=enMessageList name="pageA.enMessage" val=(pageA.enMessage)!'0'/></td>
			  	</tr>
				<#if pageTemp.couponFlag == '1'>
                <tr>
                    <th><@s.text name="systemCode" /></th>
                    <td>
						<span>
							<select id="systemCode" name="pageA.systemCode" style="width:120px;" onchange="PRM68_1.setSubCampValid(this);">
								<option value=""></option>
								<@getOptionList list=application.CodeTable.getCodes("1426") val=(pageA.systemCode)!''/>
							</select>
						<@s.text name="linkMainCode" />：<input id="linkMainCode" class="text" name="pageA.linkMainCode" style="width: 140px;" value="${pageA.linkMainCode!}"/>
						</span>
						<input type="hidden" name="pageA.oldSystemCode" value="${pageA.oldSystemCode!}"/>
					</td>
                </tr>
				</#if>
   			</tbody>
   		</table>
	</div>
</div>
<div class="hide">
	<div id="AddprmActDialog">
		<div class="box2-active clearfix">
			<table class="detail">
	           	<tbody>
	               	<tr>
	                   <th><@s.text name="groupName"/><span class="red">*</span></th>
	                   <td><span><input type ="text" class ="text" name ="groupName" maxlength="20"/></span></td>
	               	 </tr>
	               	 <tr>
	                   <th><@s.text name="prmGrpType"/><span class="highlight">*</span></th>
	                   <td>
		                   <input type="hidden" name="prmGrpType" value="CXHD"/>
							<select disabled="true">
								<@getOptionList list=application.CodeTable.getCodes("1174") val= 'CXHD' />
							</select>
	                    </td>
	               	 </tr>
	               	 <tr>
	                   <th><@s.text name="startDate"/><span class="red">*</span></th>
	                   <td><span><input type="text" id="startDate" class="date" name="activityBeginDate" style="font-family:simsun;"/></span></td>
	               	 </tr>
	               	 <tr>
	                   <th><@s.text name="endDate"/><span class="red">*</span></th>
	                   <td><span><input type="text" id="endDate" class="date" name="activityEndDate" style="font-family:simsun;"/></span></td>
	               	 </tr>
	            </tbody>
	        </table>
		</div>
	</div>
</div>