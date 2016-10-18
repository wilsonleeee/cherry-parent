<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<script type="text/javascript" src="/Cherry/js/wp/sal/BINOLWPSAL11.js?V=20160901"></script>
<s:i18n name="i18n.wp.BINOLWPSAL11">
<div class="hide">
	<s:url id="s_addMemberInfoUrl" value="/wp/BINOLWPSAL11_addMember" />
	<a id="addMemberInfoUrl" href="${s_addMemberInfoUrl}"></a>
</div>
<div id="actionResultDisplay"></div>
<s:text name="global.page.select" id="select_default"/>
<form id="memberCodeForm" method="post" class="inline">
<span class="hide"><input type="hidden" id="dgMemCode" name="dgMemCode" value="<s:property value='memberCode'/>" /></span>
</form>
<form id="addMemberForm" method="post" class="inline">
<input type="hidden" id="memberRule" name="memberRule" value="<s:property value='memberRule'/>" />
<input type="hidden" id="mobileRule" name="mobileRule" value="<s:property value='mobileRule'/>" />
<input type="hidden" id="birthFormat" name="birthFormat" value="<s:property value='birthFormat'/>" />
<input type="hidden" id="dgOpenedState" name="dgOpenedState" value="Y"/>
<input type="hidden" id="dgViewType" name="dgViewType" value="<s:property value='viewType'/>" />
<input type="hidden" id="dgBaInfoCode" name="dgBaInfoCode" value="<s:property value='baCode'/>" />
<div id="addMemberPageDiv" class="hide ui-dialog-content ui-widget-content" style="display: block; width: auto; min-height: 200px;">
    <div class="wpleft_header">
        <div class="header_box"><s:text name="wpsal11.saleBaName" /><span class="top_detail2"><s:property value="baName"/></span></div>
        <div class="actionError">
			<ul class="actionMessage">
				<li>
					<span><s:text name="wpsal11.joinMemberRemind" /></span>
				</li>
			</ul>
		</div>
    </div>
    <div class="wp_tablebox">
        <table width="100%" cellspacing="0" cellpadding="0" border="0" class="detail">
            <tbody>
                <tr>
                    <th><s:text name="wpsal11.memberCode" /><span class="highlight">*</span></th>
                    <td><span>
                    <s:if test='%{cardMobileSyn == "Y"}'>
                     	<input id="dgMemberCode" name="dgMemberCode" maxlength="20" value="<s:property value='memberCode'/>" onchange="BINOLWPSAL11.synMoblie(this);return false;" onkeyup="BINOLWPSAL11.synMoblie(this);return false;"/>
                    </s:if>
                    <s:else>
                   	 	<input id="dgMemberCode" name="dgMemberCode" maxlength="20" value="<s:property value='memberCode'/>" onchange="BINOLWPSAL11.changeMemberCode(this)"/>
                    </s:else>
                    </span></td>
                    <th><s:text name="wpsal11.baName" /><span class="highlight">*</span></th>
                    <td><span><s:select id="dgBaInfoId" name="dgBaInfoId" list="baList" listKey="baInfoId" listValue="baName" headerKey="" headerValue="%{#select_default}"></s:select></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal11.memberName" /><span class="highlight">*</span></th>
                    <td><span><input id="dgMemberName" name="dgMemberName" maxlength="10" value="" /></span></td>
                    <th><s:text name="wpsal11.mobilePhone" /><span class="highlight">*</span></th>
                    <td><span>
                    <s:if test='%{cardMobileSyn == "Y"}'>
                    <input id="dgMobilePhone" name="dgMobilePhone" maxlength="20" value="<s:property value='memberCode'/>" readOnly="true"/>
                    </s:if>
                    <s:else>
                    <input id="dgMobilePhone" name="dgMobilePhone" maxlength="20" value="<s:property value='memberCode'/>" />
                    </s:else>
                    </span></td>
                </tr>
                <tr>
                	<s:if test='birthFlag == "Y"'>
	                    <th><s:text name="wpsal11.birthday" /><span class="highlight">*</span></th>
                	</s:if>
                	<s:else>
                		<th><s:text name="wpsal11.birthday" /></th>
                	</s:else>
                    <td>
                    	<s:if test="birthFormat == 2">
                    		<span>
								<select name="birthMonthValue" style="width:50px;" id="birthMonthValue">
									<option value=""><s:text name="wpsal11.birthMonthText" /></option>
								</select>
							</span>
							<span>
								<select name="birthDayValue" style="width:50px;" id="birthDayValue">
									<option value=""><s:text name="wpsal11.birthDayText" /></option>
								</select>
							</span>
                    	</s:if>
                    	<s:else>
                    		<span><s:textfield id="dgBirthday" name="dgBirthday" cssClass="date" cssStyle="width:90px"></s:textfield></span>
                    	</s:else>
                    </td>
                    <th><s:text name="wpsal11.telephone" /></th>
                    <td><span><input id="dgTelephone" name="dgTelephone" maxlength="20" value="" /></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal11.gender" /><span class="highlight">*</span></th>
                    <td><span><s:radio id="dgGender" name="dgGender" list='#application.CodeTable.getCodes("1006")' listKey="CodeKey" listValue="Value" ></s:radio></span></td>
                    <th><s:text name="wpsal11.email" /></th>
                    <td><span><input id="dgEmail" name="dgEmail" maxlength="50" value="" /></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal11.province" /></th>
                    <td>
						<span>
					    <a id="province" class="ui-select" style="margin-left: 0px; font-size: 12px;">
			              	<span id="provinceText" class="button-text choice"><s:text name="global.page.select"/></span>
			   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
			   		 	</a>
			   		 	<s:hidden name="provinceId" id="provinceId"/>
					    </span>
					</td>
                    <th><s:text name="wpsal11.address" /></th>
                    <td><span><input id="dgAddress" name="dgAddress" maxlength="100" style="width:300px" value="" /></span></td>
                </tr>
                <tr>
                	<th><s:text name="wpsal11.city" /></th>
                    <td>
						<span>
						<a id="city" class="ui-select" style="margin-left: 0px; font-size: 12px;">
			              	<span id="cityText" class="button-text choice"><s:text name="global.page.select"/></span>
			   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
			   		 	</a>
			   		 	<s:hidden name="cityId" id="cityId"/>
						</span>
					</td>
                    <th><s:text name="wpsal11.postcode" /></th>
                    <td><span><input id="dgPostCode" name="dgPostCode" maxlength="10" value="" /></span></td>
                </tr>
                <tr>
                	<th><s:text name="wpsal11.county" /></th>
                    <td colspan=3>
						<span>
						<a id="county" class="ui-select" style="margin-left: 0px; font-size: 12px;">
			              	<span id="countyText" class="button-text choice"><s:text name="global.page.select"/></span>
			   		 		<span class="ui-icon ui-icon-triangle-1-s"></span>
			   		 	</a>
			   		 	<s:hidden name="countyId" id="countyId"/>
						</span>
					</td>
                </tr>
                <cherry:show domId="BINOLWMMNG01_01">
                <tr>
                    <th><s:text name="wpsal11.identityCard" /></th>
                    <td><span><input id="dgIdentityCard" name="dgIdentityCard" maxlength="20" value="" /></span></td>
                    <th><s:text name="wpsal11.recMember" /></th>
                    <td><span><input id="dgReferrer" name="dgReferrer" maxlength="20" value="" /></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal11.blogId" /></th>
                    <td><span><input id="dgBlogId" name="dgBlogId" maxlength="50" value="" /></span></td>
                    <th><s:text name="wpsal11.messageId" /></th>
                    <td><span><input id="dgMessageId" name="dgMessageId" maxlength="50" value="" /></span></td>
                </tr>
                <tr>
                    <th><s:text name="wpsal11.memo" /></th>
                    <td colspan="3"><span><input id="dgMemo" name="dgMemo" maxlength="200" style="width:650px" value="" /></span></td>
                </tr>
                </cherry:show>
            </tbody>
        </table>
    </div>
    <div  id="CheckMessageDiv" class="hide">
	<div class="actionSuccess">
		<ul>
			<li>
				<span><s:text name="wpsal11.mobileCheckSuccess"/></span>
			</li>
		</ul>
	</div>
	</div>
    <div class="bottom_butbox clearfix">
    	<button id="btnContinue" class="close" type="button" onclick="BINOLWPSAL11.saleContinue();return false;">
    		<span class="ui-icon icon-mover"></span>
            <span class="button-text"><s:text name="wpsal11.Continue"/></span>
		</button>
    	<button id="btnSave" class="close" type="button" onclick="BINOLWPSAL11.save();return false;">
    		<span class="ui-icon icon-save"></span>
            <span class="button-text"><s:text name="wpsal11.save"/></span>
		</button>
		<s:if test='%{isNeedCheck == "Y"}'>
		<button id="btnMobile" class="close" type="button" onclick="BINOLWPSAL11.sendMessage();return false;">
    		<span class="ui-icon icon-mover"></span>
            <span class="button-text"><s:text name="global.page.moblieCheck"/></span>
		</button>
		</s:if>
		<s:if test='"COL".equals(viewType) && "Y".equals(isMemberSaleFlag)'>
			<button id="btnNotJoin" class="close" type="button" onclick="BINOLWPSAL11.notJoinMember();return false;">
	    		<span class="ui-icon icon-stop"></span>
	            <span class="button-text"><s:text name="wpsal11.notJoinMember"/></span>
			</button>
		</s:if>
		<button id="btnCancel" class="close" type="button" onclick="BINOLWPSAL11.cancel();return false;">
			<span class="ui-icon icon-close"></span>
            <span class="button-text"><s:text name="wpsal11.cancel"/></span>
		</button>
    </div>
</div>
</form>
</s:i18n>

<%-- ================== 省市选择DIV START ======================= --%>
<div id="provinceTemp" class="ui-option hide" style="width:325px;">
	<div class="clearfix">
		<span class="label"><s:text name="global.page.range"></s:text></span>
		<ul class="u2"><li onclick="BINOLWPSAL11.getNextRegin(this, '${select_default }', 1);return false;"><s:text name="global.page.all"></s:text></li></ul>
	</div>
	<s:iterator value="reginList" id="reginMap">
    	<div class="clearfix"><span class="label"><s:property value="reginName"/></span>
    	<ul class="u2">
     		<s:iterator value="%{#reginMap.provinceList}">
         		<li id="<s:property value="%{#reginMap.reginId}" />_<s:property value="provinceId" />" onclick="BINOLWPSAL11.getNextRegin(this, '${select_default }', 1);">
         			<s:property value="provinceName"/>
         		</li>
      		</s:iterator>
      	</ul>
    	</div>
   	</s:iterator>
</div>
<div id="cityTemp" class="ui-option hide">
<s:if test="%{cityList != null && !cityList.isEmpty()}">
<ul class="u2" style="width: 500px;">
	<li onclick="BINOLWPSAL11.getNextRegin(this, '${select_default }', 2);"><s:text name="global.page.all"></s:text></li>
	<s:iterator value="cityList">
		<li id="<s:property value="regionId" />" onclick="BINOLWPSAL11.getNextRegin(this, '${select_default }', 2);"><s:property value="regionName"/></li>
	</s:iterator>
</ul>
</s:if>
</div>
<div id="countyTemp" class="ui-option hide"></div>
<%-- 下级区域查询URL --%>
<s:url id="querySubRegionUrl" value="/common/BINOLCM08_queryAllSubRegion"></s:url>
<s:hidden name="querySubRegionUrl" value="%{querySubRegionUrl}"/>
<s:a action="BINOLWPSAL11_mobileCheckInit" id="mobileCheckInit" cssStyle="display: none;"></s:a>
<s:a action="BINOLWPMBM01_couponCheck" id="couponCheck" cssStyle="display: none;"></s:a>
<span id="defaultTitle" class="hide"><s:text name="global.page.range"></s:text></span>
<span id="defaultText" class="hide"><s:text name="global.page.all"></s:text></span>
<%-- ================== 省市选择DIV  END  ======================= --%>