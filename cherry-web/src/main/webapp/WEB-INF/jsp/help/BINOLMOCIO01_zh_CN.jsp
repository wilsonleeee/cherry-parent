<%--柜台消息模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		柜台消息模块是一个对柜台消息信息的一个查询化画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:查询按钮用于查询符合条件的柜台消息信息。其查询条件有“标题/内容”和“发布日期”，查询后的结果显示在查询结果一览下面。
                                                                  其中点击任意一个消息内容，在弹出的对话框中，可以查看消息详细。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                             新增柜台消息:点击“新增柜台消息”按钮弹出新增柜台消息对话框，其中消息题目为必填项，且不能有重复，最大长度是10个字。
                                                             输入相应的标题及内容后，单击“确定”即可完成柜台的新增。
                    </div>
                    <div class="line"></div>
                </div>                   	
            	       	
            	<div class="step-content">
                    <label>3</label>
                    <div class="step">
                                                            发布：选中要发布的消息,点击“发布”，进入柜台消息发布画面后。其中可收柜台是下发该消息到下表所勾选的柜台禁止下发到柜台即选中的柜台不下发。
                                                            如果柜台有很多，可以使用定位功能迅速找到该柜台，输入框中输入需要柜台的部分关键字会进行模糊查询给出结果。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                            查看发布：点击“查看发布”按钮进入发布范围画面，这里可以查看到问卷下发到了哪些柜台。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>5</label>               
                    <div class="step">
                                                            编辑：点击“编辑”按钮在弹出的“编辑柜台消息”对话框中，修改消息标题或者消息内容，单击“确定”即可完成柜台消息的编辑。
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
                                                             需要注意的：编辑柜台消息，只能编辑没有下发的消息，已下发的消息无法编辑，发布后的消息不能再编辑。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
        
        
       