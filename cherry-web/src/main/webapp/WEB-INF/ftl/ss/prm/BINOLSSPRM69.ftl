<#-- struts 标签库 -->
<#global s=JspTaglibs["/WEB-INF/cherrytld/struts-tags.tld"] />
<#-- 自定义标签库 -->
<#global c=JspTaglibs["/WEB-INF/cherrytld/cherrytags.tld"] />
<@s.i18n name="i18n.ss.BINOLSSPRM69">
<script type="text/javascript" src="/Cherry/js/ss/prm/BINOLSSPRM69.js"></script>
	<div class="panel-header">
        <div class="clearfix">
        	<span class="breadcrumb left">
        	<span class="ui-icon icon-breadcrumb"></span><@s.text name="SSPRM69_title1"/> &gt; <@s.text name="SSPRM69_title2"/></span>
        </div>
    </div>
    <@c.form id="mainForm"></@c.form>
    <div class="panel-content">
        <div class="section">
          <div class="section-content">
          	<div class="tabs">
				<ul>
					<li><a href="#tabs-1"><@s.text name="SSPRM69_ruleSetTitle"/></a></li>
					<li><a href="#tabs-2"><@s.text name="SSPRM69_relationSetTitle"/></a></li>
				</ul>
	          	<div id="tabs-1">
	          		<div id="actionResultDiv-1"></div>
					<@c.brand id="brandId-1" onchange="PRM69.initLevel();return false;" />
			        <div class="section-header clearfix">
			          	<strong class="left"><span class="ui-icon icon-ttl-section-list"></span><@s.text name="SSPRM69_ruleList"/></strong>
			          	<span class="green" style="margin-left:5px;"><@s.text name="SSPRM69_tips0" /></span>
			          	<span id="tips1" class="red hide"><@s.text name="SSPRM69_tips1" /></span>
			          	<span class="right">
				          	<a class="add" id="editLevelBtn" onclick="PRM69.editLevel();return false;">
								<span class="ui-icon icon-edit"></span>
								<span class="button-text"><@s.text name="global.page.edit"/></span>
							</a>
			          	</span>
			        </div>
			        <div class="section-content" id="ruleDiv">
			          	
			        </div>
		        	<div class="center hide" id="saveLevelBtn" >
		        		<br>
		        		<button class="save" onclick="PRM69.saveLevel();return false;" type="button">
							<span class="ui-icon icon-save"></span>
							<span class="button-text"><@s.text name="global.page.save"/></span>
						</button>
						<button class="close" onclick="PRM69.initLevel();return false;" type="button">
							<span class="ui-icon icon-close"></span>
							<span class="button-text"><@s.text name="global.page.cancle"/></span>
						</button>
					</div>
		        </div>
		        <div id="tabs-2" class="hide">
		        	<div id="actionResultDiv-2"></div>
					<@c.brand id="brandId-2" onchange="PRM69.initRelation();return false;" />
					<div id="relationInfoDiv">
			       		
			       	</div>
		        </div>
	        </div>
		  </div>
	   </div>
   </div>
   
   <div style="display:none">
   	   <#-- =================================== 移动按钮开 =================================== -->
	   <div id="up">
			<a href="#" onclick="PRM69.move(this,0);return false;" class="left" title="<@s.text name="os.navigation.arrow-first" />"><span class="arrow-first"></span></a>
			<a href="#" onclick="PRM69.move(this,1);return false;" class="left" title="<@s.text name="os.navigation.arrow-up" />"><span class="arrow-up"></span></a>
	   </div>
	   <div id="space">
			<span class="left" style="height:16px; width:16px; display:block;"></span>
			<span class="left" style="height:16px; width:16px; display:block;"></span>
	   </div>
	   <div id="down">
			<a href="#" onclick="PRM69.move(this,2);return false;" class="left" title="<@s.text name="os.navigation.arrow-down" />"><span class="arrow-down"></span></a>
			<a href="#" onclick="PRM69.move(this,3);return false;" class="left" title="<@s.text name="os.navigation.arrow-last" />"><span class="arrow-last"></span></a>
	   </div>
	   <#-- ========================= 停用启用弹出框 =============================-->
	   <div id="disableTitle"><@s.text name="SSPRM69_disableTitle" /></div>
	   <div id="disableMsg"><p class="message"><span><@s.text name="SSPRM69_disableMsg" /></span></p></div>
	   <div id="enableTitle"><@s.text name="SSPRM69_enableTitle" /></div>
	   <div id="enableMsg"><p class="message"><span><@s.text name="SSPRM69_enableMsg" /></span></p></div>
	   <div id="warnMsg"><@s.text name="SSPRM69_warnMsg" /></div>
	   <div id="confirm_btn"><@s.text name="global.page.dialogConfirm" /></div>
	   <div id="cancel_btn"><@s.text name="global.page.dialogCancel" /></div>
   </div>
</@s.i18n>
