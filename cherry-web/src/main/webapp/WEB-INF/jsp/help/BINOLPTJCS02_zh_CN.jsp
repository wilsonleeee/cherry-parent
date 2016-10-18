<%--产品扩展属性维护模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
                <div class="text">
                	产品扩展属性维护模块是针对已有产品属性的一种动态补充，以满足各种业务有的需求。其中扩展属性的属性类型:文本框、单选框、复选框、下拉列表框，可以根据实际需求进行选择。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                 		 添加扩展属性:点击添加按钮后在一览列表会出现待新增的新属性行，填写属性名称及选择属性控件类型等项目后就可以点击当前行操作列的保存键以完成属性保存。其中属性控件类型当选择为单选框、复选框、下拉列表框时在点击保存后需要点击属性名称（具有超链接）进入到对应的属性选项查看编辑画面以完成对属性选项的编辑。
                        <img class="clearfix" src="/Cherry/images/help/BINOLPTJCS02_1_zh_CN.gif" width="90%">
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                               编辑扩展属性:有效区分处于启用状态的属性可以点击属性行所在操作列的编辑按钮对属性名称进行编辑。控件类型在新增保存时已确定，此处无法再进行编辑的。
                        <img class="clearfix" src="/Cherry/images/help/BINOLPTJCS02_2_zh_CN.gif" width="90%">
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>3</label>
                    <div class="step">
                                                                停用/启用扩展属性:选择需要停用或启用的属性，点击停用/启用按钮。此处不论选中的属性有效区分是否一致，点击停用/启用按钮后都会变成一致的有效区分。
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>4</label>
                    <div class="step">
                                                                查看扩展属性选项:点击扩展属性一览画面的属性名称后弹出此画面。显示当前扩展属性项目及其选项值。 
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>5</label>
                    <div class="step">
                                                                编辑扩展属性选项:点击扩展属性选项画面的编辑按钮即可以对扩展属性的选项值进行增减以及修改操作。建议在保存扩展属性选项时选项值需要至少有2项，其中若当前扩展属性的控件类型为复选框时，则必须为2项否则会提示无法保存。
                        <img class="clearfix" src="/Cherry/images/help/BINOLPTJCS02_3_zh_CN.gif" width="90%">
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
                                                             建议添加有多个选项的扩展属性时，最初建立完扩展属性后，先将其停用待扩展属性选项添加完毕后，再行启用。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
