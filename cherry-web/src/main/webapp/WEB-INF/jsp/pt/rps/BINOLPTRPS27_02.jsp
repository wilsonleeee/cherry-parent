<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<div class="jquery_tree">
	<div class="box2-header clearfix">
		<strong class="left active">
			<span class="ui-icon icon-flag2"></span>
			<s:text name="global.page.selectdepart" />
		</strong> 
		<span class="right">
			<input type="text" class="text locationPosition ac_input" style="width: 120px;" name="locationPosition" id="locationPosition" autocomplete="off" onkeyup="BINOLPTRPS27.locationPosition();this.focus();" >
			<a class="search" id="locationRegionButtion" style="margin: 0px 0px 3px -4px;" onclick="BINOLPTRPS27.locationPosition();return false;">
				<span class="ui-icon icon-position"></span>
				<span class="button-text"><s:text name="global.page.locationPosition" /></span>
			</a>
		</span>
	</div>
	<div class="treebox" style="overflow: auto;">
		<div class="ztree" id="departTree">
		</div>
	</div>
</div>
<div class="hide">
	<s:url action="BINOLPTRPS27_setParameter" id="setParameterUrl" />
	<s:url action="BINOLPTRPS27_getDepartTree"  id="getDepartTree" />
	<a id="setParameter_Url" href="${setParameterUrl }"></a>
	<a id="setParameter_url" href="${setParameterUrl }"></a>
	<a id="getDepartTree_Url" href="${getDepartTree }"></a>
	<input id="parameter_type" type="hidden" value="DP">
</div>



