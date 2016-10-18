<!-- CODE值管理模块帮助页面 -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		CODE值管理模块是查询CODE相关信息的一个画面。
               	</div>
            </div>
        </div>
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                        添加CODE管理值：点击添加CODE管理值进入添加CODE管理值画面，先填写
						基本信息，“Code类别”为必填项。点击“添加Code值”按钮，列表中会增加新的可编辑状态的行。
						然后设置新的Code值，完成后点击“保存”按钮。新添加的Code值信息会显示在上一级主页面的查询结果列表中。
                    </div>
                    <div class="line"></div>
                </div>
              
            	<div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                            刷新系统CODE值：点击“刷新系统CODE值”按钮，让新添加的CODE值信息写入系统中，在其它模块中也能马上使用新的
                    CODE值信息。                                 
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
                                                             需要注意的：新加的Code值信息如不使用“刷新系统Code值”按钮，只能在查询结果列表中显示，其它模块不能使用新设置的Code值。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        