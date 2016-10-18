<%--促销活动一览帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		打开“促销活动一览”页面，是对促销活动进行查询的一个查询画面。根据查询条件查询主活动。在查询结果列表中，
               		点击标签项“进行中”、“未开始”、“已过期”、“指定日期”对查询结果进行排查。可对列表中的主活动进行“复制”、
               		“停用”、“编辑”的操作。点击“新建活动”，可创建新的主活动和子活动。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                                查询：点击此按钮可以查看促销活动的具体信息，并显示在查询结果一览下面。
                                                                还可以根据促销活动的时间查询正在进行中的活动，未开始的活动，已过期的和指定日期的活动。点击任意一个活动
                                                                名还可以查询活动的详细信息。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                               新建活动（活动设定）：点击“促销活动一览”页面的“新建活动”按钮，打开新建活动页面。先选择主活动项，
                                                               选择后的主活动信息将显示在页面中。若没有相应的主活动，点击“添加主活动”按钮，创建新的主活动。
                                                               在打开的“添加主活动”窗口内，填写相应的信息，带*为必填。主活动添加完成后，主活动信息将显示在页面上。                                                               
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>
                    <div class="step">
                                                               新建活动（规则设定）：在“新增活动”页面，填写“规则设定”方面的内容，点击“活动时间”，设置促销活动的时间，
                                                               注意促销活动时间不得超出主活动的领用时间。点击“活动地点”，设置该促销活动的地点。在打开的“活动地点”窗口，
                                                               选择相关的“活动地点类型”项，相关的活动地点显示在左侧“非促销地点”区域，勾选相应的地点，点击中间的“向右移动”按钮，
                                                               被勾选的地点将被移动到右侧“活动地点”区域。选择完成后，点“确定”按钮，活动地点设置完成。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>
                    <div class="step">
                                                               活动奖励（赠送礼品）：在“新增活动”页面，“活动奖励”区域，复选“赠送礼品”复选框，将显示列表头信息，点击“赠送促销品”
                                                               按钮，在弹出的“促销产品信息”窗口中，选择需要赠送的促销产品，点击“确定”，产品信息将显示到活动奖励列表中，可以编辑数量。
                                                               点击“赠送产品”按钮还可以添加需要赠送的产品信息，其方法同“赠送促销品”。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>5</label>
                    <div class="step">
                                                               活动奖励（套装折扣）：在“新增活动”页面，“活动奖励”区域，复选“套装折扣”复选框，显示折扣金额相关信息，点击
                     “选择折扣产品”，在弹出的产品信息窗口中选择需要折扣的产品信息，点击“确定”，产品信息将显示到套装折扣列表中，
                                                               可以编辑其产品数量。点击“对任意产品折扣”，在输入框中输入产品数量，即可完成对任意产品的折扣。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>注意事项</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                             需要注意的：在终端设置选项中，选择终端“是”/“否”可以修改下发的促销信息，系统默认为“否”。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
       
