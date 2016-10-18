<%-- 菜单组菜单配置--%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="cherry" uri="/cherry-tags"%>
<%@ include file="/WEB-INF/jsp/common/head.inc.jsp" %>
<jsp:include page="/WEB-INF/jsp/common/popHead.ieCssRepair.jsp" flush="true"></jsp:include>
<link rel="stylesheet" href="/Cherry/plugins/zTree/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree/jquery.ztree-2.6.min.js"></script>
<script type="text/javascript" src="/Cherry/js/mo/pmc/BINOLMOPMC02.js"></script>
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-i18n.js"></script>
<style>
.msgBody{
    width:520px;
}
.section-header1 {
    border-bottom: 1px solid #E5E5E5;
    height: 24px;
    line-height: 24px;
    padding: 0 10px 5px 5px;
}
</style>

<s:i18n name="i18n.mo.BINOLMOPMC02">
<div class="hide">
    <s:url id="getMenuTree_url" action="BINOLMOPMC02_getMenuTree"/>
    <a id="getMenuTreeUrl" href="${getMenuTree_url}"></a>
    <s:url id="saveMenuTree_url" action="BINOLMOPMC02_saveMenuTree"/>
    <a id="saveMenuTreeUrl" href="${saveMenuTree_url}"></a>
</div>
<form id="treeForm" onsubmit="">
<s:hidden name="menuGrpID" value="%{#request.menuGrpID}"></s:hidden>
<s:hidden name="machineType" value="%{posMenuGrpInfo.machineType}"></s:hidden>
</form>
<div class="panel ui-corner-all">
<div id="div_main">
	<div class="panel-header">
	    <div class="clearfix">
	        <span class="breadcrumb left"> 
	            <span class="ui-icon icon-breadcrumb"></span>
	            <%-- 菜单配置管理 --%>
	            <s:text name="PMC02_breadcrumb"/>&gt;
	            <%-- 配置菜单 --%>
	            <s:text name="PMC02_title"/>
	        </span>
	    </div>
	</div>
    <div id="actionResultDisplay"></div>
	<div class="panel-content" >
            <div class="box">
                <div class="box-header">
		            <strong>
						<span class="ui-icon icon-ttl-section-info-edit"></span>
						<s:text name="global.page.title"/><%-- 基本信息 --%>
					</strong>
				</div>
                <div class="box-content clearfix">
                    <div class="left">
			        	<%-- 所属品牌 --%>
						<p class="clearfix">
			    			<span class="left">
								<label><s:text name="PMC02_brandName"/>：</label>
								<label><span><s:property value="posMenuGrpInfo.brandName"/></span></label>
							</span>
						</p>
						<%-- 菜单组名称 --%>
						<p class="clearfix">
						    <span class="left">
								<label><s:text name="PMC02_posMenuGrpName"/>：</label>
								<label><span><s:property value="posMenuGrpInfo.posMenuGrpName"/></span></label>
							</span>
						</p>
						<p class="clearfix">
                            <span class="left">
                                <label><s:text name="PMC02_validDate"/>：</label>
                                <!-- 加入生效起止日期后只有两种情况（1：起止日期都空，2：起止日期都不空） -->
                                <s:if test='null != posMenuGrpInfo.startDate && !"".equals(posMenuGrpInfo.startDate) && null != posMenuGrpInfo.endDate && !"".equals(posMenuGrpInfo.endDate)'>
	                                <span><s:property value="posMenuGrpInfo.startDate"/></span>
	                                ~
	                                <span><s:property value="posMenuGrpInfo.endDate"/></span>
                                </s:if>
                            </span>
                        </p>
                        <!-- 机器类型 -->
                        <p class="clearfix">
						    <span class="left">
								<label><s:text name="PMC02_machineType"/>：</label>
								<label><span><s:property value='#application.CodeTable.getVal("1284",posMenuGrpInfo.machineType)'/></span></label>
							</span>
						</p>
						<%-- 菜单组状态（是否有特殊配置） --%>
						<p class="clearfix">
							<span class="left">
								<label><s:text name="PMC02_isSpecial"/>：</label>
								<label>
									<s:if test='null != posMenuGrpInfo.isSpecial && "1".equals(posMenuGrpInfo.isSpecial)'>
									<span class="highlight"><s:text name="PMC02_special"/></span>
									</s:if><s:elseif test='null != posMenuGrpInfo.isSpecial && "0".equals(posMenuGrpInfo.isSpecial)'>
										<span class="highlight"><s:text name="PMC02_notSpecial"/></span>
									</s:elseif>
								</label>
							</span>
						</p>
					</div>
                </div>
            </div>
            <div class="clearfix section-header1"> 
                <strong>
					<span class="ui-icon icon-flag"></span>
					<s:text name="PMC02_choicePosMenu"/>
				</strong>
				<span class="right">
					<input id="position_left_0" class="text locationPosition ac_input" type="text" style="width: 200px;" name="locationPosition" autocomplete="off">
					<a class="search" onclick="BINOLMOPMC02.locationPosition(this);return false;">
						<span class="ui-icon icon-position"></span>
						<span class="button-text"><s:text name="PMC02_locationPosition"></s:text></span>
					</a>
				</span>
            </div>
            <hr class="space" />
            <div class="treebox" style="overflow:auto;">
			    <div class="jquery_tree treebox tree" id="treeDemo" style="overflow: auto;background:#FCFCFC;">
			    </div>
			</div>
            <hr class="space" />
            <div class="center">
            	<s:if test='null != posMenuGrpInfo.pastStatus && "0".equals(posMenuGrpInfo.pastStatus)'>
			    <button class="save" type="button" onclick="BINOLMOPMC02.save();">
					<span class="ui-icon icon-confirm"></span>
					<span class="button-text"><s:text name="global.page.save"></s:text></span>
				</button>
				</s:if>
				<button class="close" type="button" onclick="window.close();return false;">
					<span class="ui-icon icon-close"></span>
					<span class="button-text"><s:text name="global.page.close"/></span>
			    </button>
		    </div>
        </div>
<div id="dataTable_processing" class="dataTables_processing"  style="text-algin:left;">
</div>
</div>
</div>
</s:i18n>