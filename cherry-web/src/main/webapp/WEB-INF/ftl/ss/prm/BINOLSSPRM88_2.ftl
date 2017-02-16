<link rel="stylesheet" href="/Cherry/plugins/zTree v3.1/css/zTreeStyle/zTreeStyle.css" type="text/css" media="screen, projection">
<link type="text/css" href="/Cherry/css/cherry/cherry_timepicker.css" rel="stylesheet">
<script type="text/javascript" src="/Cherry/js/lib/jquery-ui-timepicker-addon.js"></script>
<script>
    $(document).ready(function() {
        // 日历事件绑定
        PRM88_2.calEventBind();
        PRM88_2.initTime();
        PRM88_2.initBlackCnt();
        // 活动范围树初始化
        <#--console.log('${pageTemp.placeJson!}');-->
        <#--console.log('${pageTemp.placeJson_b!}');-->
        CHERRYTREE.loadTree('${pageTemp.placeJson!}',0);
        <#--CHERRYTREE.loadTree('${pageTemp.placeJson_b!}',1,true);-->
    });
</script>
<div class="box4">
    <div class="box4-header"><strong><@s.text name="TimePlace" /><@s.text name="setting" /></strong></div>
    <div class="box4-content">
        <div class="box2 box2-active">
            <div class="box2-header clearfix">
                <strong class="left active"><span class="ui-icon icon-clock"></span><@s.text name="ruleTime" /></strong>
			<#--
            <span style="margin: 5px 0 0 20px" class="green left">
                <span><@s.text name="groupActTime" /></span><span>:</span>
                <span>${pageA.activityBeDate!}</span>
                <span>~</span>
                <span>${pageA.activityEDate!}</span>
            </span>

            <a class="add right" onclick="PRM88_2.addActiveTime();" style="margin-top: 5px;">
                <span class="ui-icon icon-add"></span><span class="button-text">增加活动阶段</span>
             </a>
             -->
            </div>
            <div class="box2-content start clearfix" style="padding:0.5em 1.5em;">
                <div id="activeTimeInfo" class="end">
				<@getTimeItem startDate=pageTemp.startDate! endDate=pageTemp.endDate! startTime=pageTemp.startTime endTime=pageTemp.endTime disabled=pageTemp.disabled!false />
                </div>
            </div>
            <input type="hidden" name="pageB.timeJson" id="timeJson" value=""/>
            <input type="hidden" name="pageB.startTime" id="startTime"value=""/>
            <input type="hidden" name="pageB.endTime" id="endTime"value=""/>
        </div>
        <div style="padding:0px;margin-top:15px;margin-Bottom:15px;width: 50%;float:left;" class="ztree jquery_tree box2 box2-active">
            <div class="box2-header clearfix">
                <strong class="left active" style="font-size:14px;padding-top:2px;"><span class="ui-icon icon-flag" style="margin-top:5px;"></span><@s.text name="rulePlace"/></strong>
            	<span style="margin:3px 10px 0px 5px;" class="left">
                    <@s.text name="placeType"/>
                        <select id="locationType_0" name="pageB.locationType" onchange="PRM88_2.changeType(this,0);" style="width:90px;">
                            <option value=""><@s.text name="global.page.select"/></option>
						<@getOptionList list=application.CodeTable.getCodes("1156") val= pageB.locationType! />
						<#assign locationType = pageB.locationType!/>
                        </select>
            	</span>
            	<span id="importDiv_0" class="left hide" style="margin-left:10px;">
					<a href="/Cherry/download/柜台信息模板.xls"><@s.text name="templeteDown" /></a>
					<input class="input_text" type="text" id="pathExcel_0" name="pathExcel" style="height:20px;"/>
				    <input type="button" value="<@s.text name="global.page.browse"/>"/>
				    <input class="input_file" style="height:auto;margin-left:-314px;" type="file" name="upExcel" id="upExcel_0" size="40" onchange="pathExcel_0.value=this.value;return false;" />
	        		<input type="button" value="<@s.text name="import"/>" onclick="CHERRYTREE.ajaxFileUpload('0');return false;"/>
	        		<img id="loadingImg_0" src="/Cherry/css/cherry/img/loading.gif" height="15px" style="display:none;">
				</span>
                <div id="importWhiteCounter" <#if locationType != '5'>style="display:none;"</#if>>
                    <a  onclick="PRM88_2.popCounterLoadDialog('1');return false;" class="search left hide">
                        <span class="ui-icon icon-add"></span>
                        <span class="button-text"><@s.text name="counterImport"/></span>
                    </a>
                </div>
                <div id="locationId_0">
            		<span style="">
            			<input type="checkbox" id="selectAll_0" onchange="CHERRYTREE.selectAll(this,0);"/>
            			<label for="selectAll_0"><@s.text name="global.page.selectAll" /></label>
            		</span>
                    <a onclick="CHERRYTREE.getPosition(0);return false;" class="search right" style="margin:3px 0 0 0;">
                        <span class="ui-icon icon-position"></span>
                        <span class="button-text"><@s.text name="position"/></span>
                    </a>
                    <input type="text" class="text right locationPosition ac_input" id="locationPositiontTxt_0" autocomplete="off" style="width: 70px">
                </div>
            </div>
            <div id="tree_0" class="jquery_tree treebox tree" style="overflow:auto;height:300px;"></div>
            <input type="hidden" name="pageB.placeJson" id="placeJson_0" value=""/>
            <input type="hidden" name="pageB.saveJson" id="saveJson_0" value=""/>
        </div>
        <div style="padding:0px;margin-top:15px;margin-Bottom:15px;width: 50%;float:left;" class="ztree jquery_tree box2 box2-active">
            <div class="box2-header clearfix">
                <strong class="left active" style="font-size:14px;padding-top:2px;"><span class="ui-icon icon-flag" style="margin-top:5px;"></span><@s.text name="removeCounter"/></strong>
                <div id="locationId_1" style="margin-left:120px;">
            		<span style="">
            			<input type="checkbox" id="selectAll_1" class="chk checkbox_true_full" onchange="PRM88_2.checkSelectAll(this);"/>
            			<label for="selectAll_1"><@s.text name="global.page.selectAll" /></label>
            		</span>
                    <a onclick="PRM88_2.popCounterLoadDialog('2');return false;" class="search right" style="margin:3px 0 0 0;" >
                        <span class="ui-icon icon-add"></span>
                        <span class="button-text"><@s.text name="removeCounterImport"/></span>
                    </a>
                </div>
            </div>
            <div id="tree_1" class="jquery_tree treebox tree" style="overflow:auto;height:300px;"></div>
            <#--<input type="hidden" name="pageB.placeJsonBlack" id="placeJson_1" value='<@s.property value="pageB.placeJsonBlack"/>'/>-->
            <input type="hidden" name="pageB.placeJsonBlack" id="placeJson_1" value=""/>
            <input type="hidden" name="pageB.saveJsonBlack" id="saveJson_1" value=""/>
            <input type="hidden" name="pageB.initJsonBlack" id="initJson_1" value="<@s.property value="pageTemp.placeJson_b"/>"/>
        </div>
    </div>
</div>

<#macro getTimeItem startDate endDate startTime='00:00:00' endTime='23:59:59' disabled=false>
<div class="clearfix time_box">
    <p class="column">
		<span>
			<label><@s.text name="startDate"/></label><input class="date startDate" name="startDate" type="text" value="${startDate}" <#if disabled>disabled="true"</#if> />
			<#if disabled>
                <input name="startDate" type="hidden" value="${startDate}"/>
			</#if>
			<label><input class="date time" name="startTime" type="text" value="${startTime}"/></label>
		</span>
    </p>
    <p class="column">
		<span>
			<label><@s.text name="endDate"/></label><input class="date endDate" name="endDate" type="text" value="${endDate}">
			<label><input class="date time" name="endTime" type="text" value="${endTime}"/></label>
		</span>
    </p>
</div>
</#macro>
