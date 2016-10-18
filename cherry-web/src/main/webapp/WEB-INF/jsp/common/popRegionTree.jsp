<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 

<div style="margin-bottom: 10px;line-height: 28px;">
<span>
<select name="selMode" id="selMode">
<option value="1"><s:text name="global.page.selMode1"/></option>
<option value="2"><s:text name="global.page.selMode2"/></option>
<option value="3"><s:text name="global.page.selMode3"/></option>
<option value="4"><s:text name="global.page.selMode4"/></option>
<option value="5"><s:text name="global.page.selMode5"/></option>
</select>
</span>
<span>
<select name="popCouValidFlag" id="popCouValidFlag">
<option value="1"><s:text name="global.page.popCouValidFlag1"/></option>
<option value="0"><s:text name="global.page.popCouValidFlag0"/></option>
</select>
</span>
<s:if test="%{hasSelExclusiveFlag == 1}">
<span>
<input type="radio" name="selExclusiveFlag" value="1" id="exclusiveFlag1" checked="checked" /><label for="exclusiveFlag1"><s:text name="global.page.selExclusiveFlag1"/></label>
<input type="radio" name="selExclusiveFlag" value="2" id="exclusiveFlag2"/><label for="exclusiveFlag2"><s:text name="global.page.selExclusiveFlag2"/></label>
</span>
</s:if>
</div>
<div class="jquery_tree">
<div class="box2-header clearfix">
<strong class="left active"><span class="ui-icon icon-flag2"></span><s:text name="global.page.memCounter" /></strong>
<span class="right">
<input id="locationPosition" type="text" class="text locationPosition ac_input" style="width: 120px;" name="locationPosition" autocomplete="off">
<a class="search" id="locationRegionButtion" style="margin: 0px 0px 3px -4px;"><span class="ui-icon icon-position"></span><span class="button-text"><s:text name="global.page.locationPosition"/></span></a>
</span>
</div>
<div class="treebox" style="overflow:auto;">
<div id="channelRegionDiv" style="padding: 5px 5px 5px 8px;border-bottom: 1px dashed #D6D6D6;">
<strong><s:text name="global.page.channelRegion"/></strong>
<span></span>
</div>
<div class="ztree" id="regionTree"></div>
</div>
</div>

<div class="hide">
<div id="regionDialogTitle"><s:text name="global.page.selectProvince" /></div>
<div id="regionDialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="regionDialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="select_default"><s:text name="global.page.select" /></div>
<div id="regionDialogHandle"><s:text name="global.page.loading" /></div>
<div id="regionDialogResult"><s:text name="global.page.noResult" /></div>
<s:url action="BINOLCM02_popRegionDialog" namespace="/common" id="popRegionDialogUrl"></s:url>
<a href="${popRegionDialogUrl }" id="popRegionDialogUrl"></a>
<s:url action="BINOLCM02_popChannelDialog" namespace="/common" id="popChannelDialogUrl"></s:url>
<a href="${popChannelDialogUrl }" id="popChannelDialogUrl"></a>
<s:url action="BINOLCM02_locationRegion" namespace="/common" id="locationRegionUrl"></s:url>
<a href="${locationRegionUrl }" id="locationRegionUrl"></a>
</div>