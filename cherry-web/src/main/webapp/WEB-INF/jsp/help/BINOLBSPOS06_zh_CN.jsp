<%--岗位管理模块页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		岗位管理模块是专对岗位信息的一个查询化界面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                                查询：查询按钮用于查询出符合条件的岗位信息，并显示在查询结果一览的下面。查询信息可以根据查询条件自由设置，默认为查询所有启用岗位信息。
                                                                也可以自己勾选“包含停用岗位”复选框，将启用与停用的符合条件的岗位信息全部查询出来。      
                                                                
                    </div>
                    <div class="line"></div>
                </div> 
                <div class="step-content">
                    <label>2</label>                    
                    <div class="step">
                                                               添加岗位：点击“添加岗位”按钮，进入添加岗位画面，岗位代码，岗位名称，岗位级别为必填项，其中岗位级别必须填写数字。
                                                               点击“保存”即可添加一个岗位。
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                                停用/启用（列表模式）:选择需要启用或者停用的岗位，在弹出的确认框，点击“确定”按钮，即可完成岗位的启用或者停用。                  
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>

