<%@ taglib prefix="s" uri="/struts-tags" %>
<style>
	li {
		list-style-type: none;
	}
</style>
<form id="useForm">


<h3>使用门槛</h3>
<div id="useContionDiv">

<input class="z-param hide" name="contentNo" value="<s:property value='contentNo'/>">
<div style="min-height: 500px;" id="sendCondition">
	<div class="box4-content  clearfix">
		<div style="width:12%;float:left;">
			<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div><label class="bg_title" >购买门店
		</label></div>
		<div style="width:88%;float:left;display:none;" id="SendCounterDiv">
			<div class="left" style="width: 50%;" id="sendCounterWhite">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" style="display:inline" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>购买柜台</strong>
						<div class="right">
							<input type="hidden" name="oldKbn" class="oldKbn" value='<s:property value="useCondInfo.counterKbn_w"/>' />
							<span>柜台选择</span>
							<select id="cnt_u_w_select"  name="counterKbn_w" style="width:100px;display:inline;" onchange="binolssprm7302.changeCounterSpan(2,1, this,0,false);" class="z-param">
								<%--<option  value='0'>全部</option>
								<option  value='1'>导入门店</option>
								<option  value='2'>渠道选择</option>--%>
								<option <s:if test='%{useCondInfo.counterKbn_w == "0"}'>selected</s:if> value='0'>全部</option>
								<option <s:if test='%{useCondInfo.counterKbn_w == "1"}'>selected</s:if> value='1'>导入门店</option>
								<option <s:if test='%{useCondInfo.counterKbn_w == "2"}'>selected</s:if> value='2'>按渠道指定柜台</option>
								<option <s:if test='%{useCondInfo.counterKbn_w == "3"}'>selected</s:if> value='3'>渠道选择</option>
							</select>
							<a class="add" <s:if test='%{useCondInfo.counterKbn_w != "1"}'>style="display: none;"</s:if><s:else>style="display:inline-block;"</s:else> id="cnt_u_w_inputButton" onClick="binolssprm7302.popCounterLoadDialog('2','1',<s:property value='contentNo'/>);return false;">
								<span class="ui-icon icon-add"></span><span class="button-text">导入柜台</span>
							</a>
							<input type="hidden" id="cntwhiteChannel_use" name="cntwhiteChannel_use"  value="<s:property value='useCondInfo.cntwhiteChannel_use'/>"/>
						</div>
					</div>
					<div class="box2-content clearfix" style="height:150px;"><%-- 全部对应区域 --%>
						<div class="hide" id="cnt_u_w_commonDiv" style="display: block;height:150px; overflow:auto">
							<s:if test='%{useCondInfo.counterKbn_w == "1"}'>
								<s:iterator value="useCondInfo.cntWhiteList" id="cntWhiteInfo">
									<li>
										<%--<input type="checkbox" value='<s:property value="#cntWhiteInfo.organizationID"/>'  onclick="binolssprm7302.cnt_s_removeLi(this,1,2);"  name="cntWhiteChoose" checked="checked" value='<s:property value="#cntWhiteInfo.organizationID"/>'/>--%><s:property value="#cntWhiteInfo.counterCode"/>  <s:property value="#cntWhiteInfo.counterName"/>
										<input name="organizationID"  class="hide" value='<s:property value="#cntWhiteInfo.organizationID"/>'/>
										<input name="counterCode"  class="hide" value='<s:property value="#cntWhiteInfo.counterCode"/>'/>
										<input name="counterName"  class="hide" value='<s:property value="#cntWhiteInfo.counterName"/>'/>
									</li>
								</s:iterator>
							</s:if>

							<s:elseif test='%{useCondInfo.counterKbn_w == "3"}'>
								<s:iterator value="useCondInfo.channel_list" id="channel_list">
									<li>
										<input type="checkbox"value='<s:property value="#cntChannelInfo.id"/>'  <s:if test="#channel_list.checkFlag == 1">checked="checked"</s:if> /> <s:property value="#channel_list.name"/>
										<input name="id"  class="hide" value='<s:property value="#channel_list.id"/>'/>
										<input name="name"  class="hide" value='<s:property value="#channel_list.name"/>'/>
									</li>
								</s:iterator>
							</s:elseif>
						</div>

						<div class="jquery_tree" id="cnt_u_w_treeDiv" style="height:150px;display:none">
							<div class="treebox" style="overflow:auto;height: 150px">
								<div  class="ztree" id = cnt_u_w_div_tree></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="left" style="width: 50%;">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>排除柜台</strong>
						<div class="right">
							<a class="add" onClick="binolssprm7302.popCounterLoadDialog('2','2',<s:property value='contentNo'/>);return false;">
								<span class="ui-icon icon-add"></span><span class="button-text">导入排除柜台</span>
							</a>
							<input name="counterKbn_b" value="1" class="z-param hide">
						</div>
					</div>
					<div class="box2-content clearfix" id="cnt_u_b_div" style="height:150px;overflow:auto;">
						<s:iterator value="useCondInfo.cntBlackList" id="cntBlackInfo">
							<li>
								<%--<input type="checkbox" value='<s:property value="#cntBlackInfo.organizationID"/>' onclick="binolssprm7302.cnt_s_removeLi(this,2,2);" name="cntBlackChoose" checked="checked" value='<s:property value="#cntBlackInfo.organizationID"/>'/>--%><s:property value="#cntBlackInfo.counterCode"/>  <s:property value="#cntBlackInfo.counterName"/>
								<input name="organizationID"  class="hide" value='<s:property value="#cntBlackInfo.organizationID"/>'/>
								<input name="counterCode"  class="hide" value='<s:property value="#cntBlackInfo.counterCode"/>'/>
								<input name="counterName" class="hide" value='<s:property value="#cntBlackInfo.counterName"/>'/>
							</li>
						</s:iterator>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="box4-content  clearfix">
		<div style="width:12%;float:left;">
			<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
			<label class="bg_title">购买金额</label>
		</div>
		<div style="width:88%;float:left;display:none;">
			金额条件:<select name="amountCondition" id="amountCondition" class="z-param" style="width:80px;">
			<option <s:if test='%{useCondInfo.amountCondition == "0"}'>selected</s:if> value="0">实付金额</option>
			<option <s:if test='%{useCondInfo.amountCondition == "1"}'>selected</s:if> value="1">正价金额</option>
		</select>
			满 <input type="text" name ="useMinAmount" id="useMinAmount" class="number z-param" style="width:60px;" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" maxlength="9" value="<s:property value='useCondInfo.useMinAmount'/>"/> 元
		</div>
	</div>
	<!--   ssssssssssssssssssssssssssssss  -->
	<div class="box4-content  clearfix">
		<div style="width:12%;float:left;">
			<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
			<label class="bg_title" >产品范围</label>
		</div>
		<div style="width:88%;float:left;display:none;">
			<div class="left" style="width:50%;" id="sendProductWhite">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" style="display:inline" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>产品范围</strong>
							<span class="right">
								<input type="hidden" name="oldKbn" class="oldKbn" value='<s:property value="useCondInfo.productKbn_w"/>' />
								<span>产品选择 </span>
								<select name="productKbn_w" id="prt_u_w_productKbn" style="width:100px;" onchange="binolssprm7302.changePrtSpan('2','1', 'prt_u_w_productKbn');" class="z-param">
									<option <s:if test='%{useCondInfo.productKbn_w == "0"}'>selected</s:if> value='0'>全部</option>
									<option <s:if test='%{useCondInfo.productKbn_w == "1"}'>selected</s:if> value='1'>分类选择</option>
									<option <s:if test='%{useCondInfo.productKbn_w == "2"}'>selected</s:if> value='2'>产品选择</option>
									<option <s:if test='%{useCondInfo.productKbn_w == "3"}'>selected</s:if> value='3'>产品导入</option>
								</select>
								<span id="prt_u_w_BtnSpan">
									<span id="prt_u_w_selTypeBtn" class="hide">
										<a class="add" onClick="binolssprm7302.openProTypeDialog2('2');return false;">
											<span class="ui-icon icon-add"></span><span class="button-text">选择分类</span>
										</a>
										<!-- <input type="button" value="选择分类" onclick="binolssprm7302.openProTypeDialog2('1');"/> -->
									</span>
									<span id="prt_u_w_selPrtBtn" class="hide">
										<!-- <input type="button" value="选择产品" onclick="binolssprm7302.openProDialog2('1');"/> -->
										<a class="add" onClick="binolssprm7302.openProDialog2('2');return false;">
											<span class="ui-icon icon-add"></span><span class="button-text">选择产品</span>
										</a>
									</span>
									<span id="prt_u_w_impPrtBtn" class="hide">
										<!-- <input type="button" value="导入产品" onclick="binolssprm7302.popProductLoadDialog('1','1');"/>  -->
										<a class="add" onClick="binolssprm7302.popProductLoadDialog('2','1',<s:property value='contentNo'/>);return false;">
											<span class="ui-icon icon-add"></span><span class="button-text">导入产品</span>
										</a>
									</span>
								</span>
							&nbsp;&nbsp;
								<span>产品关系</span>
								<select name="relation" style="width:50px;" class="z-param">
									<option <s:if test='%{useCondInfo.relation == "2"}'>selected</s:if> value='2'>或者</option>
									<option <s:if test='%{useCondInfo.relation == "1"}'>selected</s:if> value='1'>并且</option>
								</select>
							</span>

					</div>

					<!-- 白名单内容框 -->
					<div id="prt_u_w_div" class="box2-content clearfix" style="height:150px;overflow:auto">
						<!-- 全部页 空白页 -->
						<div id="prt_u_w_allDIV" class="hide">
						</div>
						<!-- 产品分类内容框 -->
						<div id="prt_u_w_typeDIV" class="hide">
							<s:if test='%{useCondInfo.productKbn_w == "1"}'>
								<s:iterator value="useCondInfo.prtList_w" id="prtCouponPrtDetail">
									<li>
										<input type="hidden" name="cateValId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="cateId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />

										<input type="hidden" name="prtObjId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="cateVal" value='<s:property value="#prtCouponPrtDetail.cateVal"/>' />
										<input type="hidden" name="cateValName" value='<s:property value="#prtCouponPrtDetail.cateValName"/>' />

										<input type="checkbox" name="prtTypeBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);" />
										<s:property value="#prtCouponPrtDetail.cateVal"/>&nbsp; <s:property value="#prtCouponPrtDetail.cateValName"/>
									</li>
								</s:iterator>
							</s:if>
						</div>
						<!-- 选择产品内容框 -->
						<div id="prt_u_w_selPrtDIV" class="hide">
							<s:if test='%{useCondInfo.productKbn_w == "2"}'>
								<s:iterator value="useCondInfo.prtList_w" id="prtCouponPrtDetail">
									<li>
										<input type="hidden" name="prtVendorId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="prtVendId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="checkbox" name="selPrtBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);" />

										<input type="hidden" name="prtObjId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="unitCode" value='<s:property value="#prtCouponPrtDetail.unitCode"/>' />
										<input type="hidden" name="barCode" value='<s:property value="#prtCouponPrtDetail.barCode"/>' />
										<input type="hidden" name="nameTotal" value='<s:property value="#prtCouponPrtDetail.nameTotal"/>' />

										<s:property value="#prtCouponPrtDetail.unitCode"/>
										&nbsp; <s:property value="#prtCouponPrtDetail.barCode"/>
										&nbsp; <s:property value="#prtCouponPrtDetail.nameTotal"/>
										&nbsp;&nbsp;<input type="text" style="width: 20px;" name="proNum" value='<s:property value="#prtCouponPrtDetail.proNum"/>' onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')"/>
									</li>
								</s:iterator>
							</s:if>
						</div>
						<!-- 导入产品内容框 -->
						<div id="prt_u_w_impPrtDIV" class="hide">
							<s:if test='%{useCondInfo.productKbn_w == "3"}'>
								<s:iterator value="useCondInfo.prtList_w" id="prtCouponPrtDetail">
									<li>
										<input type="hidden" name="prtVendorId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="prtVendId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<%--<input type="checkbox" name="selPrtBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);binolssprm7302.delPrtLi('2','1',this);" />--%>

										<input type="hidden" name="prtObjId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="unitCode" value='<s:property value="#prtCouponPrtDetail.unitCode"/>' />
										<input type="hidden" name="barCode" value='<s:property value="#prtCouponPrtDetail.barCode"/>' />
										<input type="hidden" name="nameTotal" value='<s:property value="#prtCouponPrtDetail.nameTotal"/>' />
										<input type="hidden" name="proNum" value='<s:property value="#prtCouponPrtDetail.proNum"/>' />

										<s:property value="#prtCouponPrtDetail.unitCode"/>
										&nbsp; <s:property value="#prtCouponPrtDetail.barCode"/>
										&nbsp; <s:property value="#prtCouponPrtDetail.nameTotal"/>
										&nbsp;&nbsp;<input type="text" style="width: 20px;" name="prtNumLab" value='<s:property value="#prtCouponPrtDetail.prtObjNum"/>' disabled />
									</li>
								</s:iterator>
							</s:if>
						</div>
					</div>
				</div>
			</div>
			<div class="left" style="width: 50%;" id="sendProductBlack">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>排除产品</strong>
						<div class="right">
							<!-- <input type="button" value="导入排除产品" onclick="binolssprm7302.popProductLoadDialog('1','2');"/> -->
							<a class="add" onClick="binolssprm7302.popProductLoadDialog('2','2',<s:property value='contentNo'/>);return false;">
								<span class="ui-icon icon-add"></span><span class="button-text">导入排除产品</span>
							</a>
							<!--
                            <a class="add" onClick="binolssprm7302.writeImpPrt('1','2','0');return false;">
                                <span class="ui-icon icon-add"></span><span class="button-text">回写</span>
                            </a>
                            -->
						</div>
					</div>
					<!-- 黑名单内容框 -->
					<div id="prt_u_b_div" class="box2-content clearfix" style="height:150px;overflow:auto" >
						<input type="hidden" name ="productKbn_b" value="3" id="prt_u_b_productKbn" class="z-param hide">
						<!-- 导入产品内容框 -->
						<div id="prt_u_b_impPrtDIV" class="show">
							<s:if test='%{useCondInfo.productKbn_b == "3"}'>
								<s:iterator value="useCondInfo.prtList_b" id="prtCouponPrtDetail">
									<li>
										<input type="hidden" name="prtVendorId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="prtVendId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />

										<input type="hidden" name="prtObjId" value='<s:property value="#prtCouponPrtDetail.prtObjId"/>' />
										<input type="hidden" name="unitCode" value='<s:property value="#prtCouponPrtDetail.unitCode"/>' />
										<input type="hidden" name="barCode" value='<s:property value="#prtCouponPrtDetail.barCode"/>' />
										<input type="hidden" name="nameTotal" value='<s:property value="#prtCouponPrtDetail.nameTotal"/>' />

										<%--<input type="checkbox" name="selPrtBlackChoose" checked="checked" onclick="binolssprm7302.removeLi(this);binolssprm7302.delPrtLi('2','2',this);" />--%>

										<s:property value="#prtCouponPrtDetail.unitCode"/>
										&nbsp; <s:property value="#prtCouponPrtDetail.barCode"/>
										&nbsp; <s:property value="#prtCouponPrtDetail.nameTotal"/>

									</li>
								</s:iterator>
							</s:if>

						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="box4-content  clearfix">
		<div style="width:12%;float:left;">
			<div class="ui-icon icon-arrow-crm"  onclick="binolssprm7302.tabDiv(this);return false;"></div><label class="bg_title">需参活动
		</label></div>
		<div style="width:88%;float:left;display:none" id="SendRuleDiv">
			<div class="left campDiv" style="width: 50%;" id="sendRuleWhite">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" style="display:inline" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>活动选择</strong>
						<div class="right">
							<a class="add" onClick="binolssprm7302.popCampDialog('2','1',this);return false;">
								<span class="ui-icon icon-add"></span><span class="button-text">选择活动</span>
							</a>
						</div>
					</div>
					<div class="box2-content clearfix" style="height:150px;"><%-- 全部对应区域 --%>
						<div class="hide campList" id="rul_u_w_commonDiv" style="display: block;height:150px; overflow:auto">
							<s:iterator value="useCondInfo.campList_w" id="campList_w">
								<li>
									<input type="checkbox"  onclick="binolssprm7302.removeLi(this);return false;"  name="campListWhiteChoose" checked="checked"/><s:property value="#campList_w.campaignCode"/>  <s:property value="#campList_w.campaignName"/>
									<s:if test='#campList_w.campaignMode == "0"'>
										会员活动
									</s:if>
									<s:elseif test='#campList_w.campaignMode == "1"'>
										促销活动
									</s:elseif>
									<s:elseif test='#campList_w.campaignMode == "2"'>
										电子券活动
									</s:elseif>
									<input name="campaignCode" value='<s:property value="#campList_w.campaignCode"/>' type="hidden">
									<input name="campaignMode" value='<s:property value="#campList_w.campaignMode"/>'type="hidden">
									<input name="campaignName" value='<s:property value="#campList_w.campaignName"/>' type="hidden">
								</li>
							</s:iterator>
						</div>
					</div>
				</div>
			</div>
			<div class="left campDiv" style="width: 45%;" id="sendRuleBlack">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>排除活动</strong>
						<div class="right">
							<a class="add" onClick="binolssprm7302.popCampDialog('2','2',this);return false;">
								<span class="ui-icon icon-add"></span><span class="button-text">选择活动</span>
							</a>
						</div>
					</div>
					<div class="box2-content clearfix campList" style="height:150px;overflow:auto" id="rul_u_b_div">
						<s:iterator value="useCondInfo.campList_b" id="campList_b">
							<li>
								<input type="checkbox"  onclick="binolssprm7302.removeLi(this);return false;"  name="campListBlackChoose" checked="checked"/><s:property value="#campList_b.campaignCode"/>  <s:property value="#campList_b.campaignName"/>
								<s:if test='#campList_b.campaignMode == "0"'>
									会员活动
								</s:if>
								<s:elseif test='#campList_b.campaignMode == "1"'>
									促销活动
								</s:elseif>
								<s:elseif test='#campList_b.campaignMode == "2"'>
									电子券活动
								</s:elseif>
								<input name="campaignCode" value='<s:property value="#campList_b.campaignCode"/>' type="hidden">
								<input name="campaignMode" value='<s:property value="#campList_b.campaignMode"/>'type="hidden">
								<input name="campaignName" value='<s:property value="#campList_b.campaignName"/>' type="hidden">
							</li>
						</s:iterator>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="box4-content  clearfix">
		<div style="width:12%;float:left;">
			<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
			<label class="bg_title" >使用对象</label>
		</div>
		<div style="width:88%;float:left;display:none" id="useMemberDiv" class="memberDiv hide">
			<div class="left memberWriteDiv" style="width:100%;" id="useMemberWhite">
				<div class="box2 box2-active" style="display:inline;height:150px;">
					<div class="box2-header clearfix" style="display:inline" >
						<strong class="left active"><span class="ui-icon icon-flag"></span>使用对象</strong>
						<span class="left">
						&nbsp;&nbsp;
						<span>对象类型</span>
						&nbsp;&nbsp;
						<input type="hidden" class="oldKbn" value='<s:property value="useCondInfo.memberKbn_w"/>'/>
						<select id="member_u_w_Kbn" name="memberKbn_w" style="width:100px;" onchange="binolssprm7302.changeMemberSpan(1,0,1,this);return false;" class="z-param">
							<option <s:if test='%{useCondInfo.memberKbn_w == "0"}'>selected</s:if> value='0'>全部</option>
							<option <s:if test='%{useCondInfo.memberKbn_w == "2"}'>selected</s:if> value='2'>会员等级</option>
						</select>
					</span>
					</div>

					<!-- 白名单内容框 -->
					<div class="box2-content clearfix" style="height:150px;">
						<div id="mem_u_w_commonDiv" class="memberLevel <s:if test='%{useCondInfo.memberKbn_w =="2"}'></s:if><s:else>hide</s:else>">
							<s:iterator  value="useCondInfo.memLevel_w" id="memLevel">
								<li>
									<input type="checkbox" name="memLevel" <s:if test='%{#memLevel.flag=="1"}'>checked="checked"</s:if>/>
									<input type="hidden" name="memLevel" class="useCondMember_w" value="<s:property value='#memLevel.levelId'/>"/>
									<span><s:property value="#memLevel.levelName"/></span></br>
								</li>
							</s:iterator>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="box4-content  clearfix">
		<div style="width:12%;float:left;">
			<div class="ui-icon icon-arrow-crm" onclick="binolssprm7302.tabDiv(this);return false;"></div>
			<label class="bg_title">使用时间</label>
		</div>
		<div style="width:52%;display:none">
			<span><select name="useTimeType" id="useTimeType" onchange="binolssprm7302.changeSpan('useTimeSpan', this);" class="z-param" style="width:120px;">
				<option <s:if test='%{useCondInfo.useTimeType == "0"}'>selected</s:if> value="0">指定日期</option>
				<option <s:if test='%{useCondInfo.useTimeType == "1"}'>selected</s:if> value="1">参考发券日期</option>
			</select>
			<div id="useTimeSpan0" <s:if test='%{useCondInfo.useTimeType == "1"}'> class="hide" </s:if> style="float:right">
				<span>
				  	<input class="date" id="useStartTime" name="useStartTime" value="<s:property value='useCondInfo.useStartTime'/>"/>
					-
					<input class="date" id="useEndTime" name="useEndTime" value="<s:property value='useCondInfo.useEndTime'/>"/>
				   </span>
			</div>
		<div id="useTimeSpan1" <s:if test='%{useCondInfo.useTimeType == null || useCondInfo.useTimeType == "0"}'>  class="hide"</s:if>  style="float:right">
			后<input type="text" id="afterDays" class="number" maxlength="2" name="afterDays" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" value="<s:property value='useCondInfo.afterDays'/>" />天
			&nbsp;有效期<input type="text" id="validity" class="number" maxlength="2" onkeyup="this.value=this.value.replace(/\D/g,'')" onblur="this.value=this.value.replace(/\D/g,'')" name="validity" value="<s:property value='useCondInfo.validity'/>" />
			<select name="validityUnit" style="width:40px;" >
				<option <s:if test='%{useCondInfo.validityUnit == "0"}'>selected</s:if> value='0'>天</option>
				<option <s:if test='%{useCondInfo.validityUnit == "1"}'>selected</s:if> value='1'>月</option>
			</select>
		</div></span>
		</div>
	</div>
</div>
</div>
</form>
<script type="text/javascript">
	binolssprm7302.searchChannel('/Cherry/ss/BINOLSSPRM73_channel.action',1);
	$('#useStartTime').cherryDate({
		beforeShow: function(input){
			var value = $('#useEndTime').val();
			return [value,'maxDate'];
		}
	});
	$('#useEndTime').cherryDate({
		beforeShow: function(input){
			var value = $('#useStartTime').val();
			return [value,'minDate'];
		}
	});

	if($("#cnt_u_w_select").val() != 2){
		$("#cnt_u_w_commonDiv").show();
	}else{
		$("#cnt_u_w_commonDiv").hide();
	}

	binolssprm7302.changePrtSpan('2','1','prt_u_w_productKbn',1111);
</script>