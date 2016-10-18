<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %> 
<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<script type="text/javascript" src="/Cherry/plugins/zTree v3.1/js/jquery.ztree.all-3.1.js"></script>
<div class="jquery_tree">
<div class="box2-header clearfix">
<span class="right">
<input id="locationPosition" type="text" class="text locationPosition ac_input" style="width: 120px;" name="locationPosition" autocomplete="off">
<a class="search" id="locationRegionButtion" style="margin: 0px 0px 3px -4px;"><span class="ui-icon icon-position"></span><span class="button-text"><s:text name="global.page.locationPosition"/></span></a>
</span>
</div>
<div class="treebox" style="overflow:auto;">
<div class="ztree" id="channelTree"></div>
</div>
</div>

<div class="hide">
<div id="channelDialogTitle"><s:text name="global.page.selectChannel" /></div>
<div id="channelDialogConfirm"><s:text name="global.page.dialogConfirm" /></div>
<div id="channelDialogCancel"><s:text name="global.page.dialogCancel" /></div>
<div id="select_default"><s:text name="global.page.select" /></div>
<div id="regionDialogHandle"><s:text name="global.page.loading" /></div>
<div id="regionDialogResult"><s:text name="global.page.noResult" /></div>
<s:url action="BINOLSSPRM73_channelDialog" namespace="/ss" id="popChannelDialogUrl"></s:url>
<a href="${popChannelDialogUrl }" id="popChannelDialogUrl"></a>
<s:url action="BINOLCM02_locationRegion" namespace="/common" id="locationRegionUrl"></s:url>
<a href="${locationRegionUrl }" id="locationRegionUrl"></a>
</div>