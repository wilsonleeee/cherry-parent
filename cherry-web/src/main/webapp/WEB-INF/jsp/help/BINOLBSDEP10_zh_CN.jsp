<%--品牌管理模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		品牌管理模块是专对品牌信息的一个查询化界面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                                  查询:查询按钮用于查询出符合条件的品牌信息，并显示在查询结果一览的下面。查询条件有品牌代码，品牌名称。默认为查询所有启用品牌信息。
                                                                  也可以自己勾选“包含停用品牌”复选框，将启用与停用的符合条件的品牌信息全部查询出来。                                         
                    
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>             
                    
                    <div class="step">
                                                               创建品牌：点击“创建品牌”按钮，进入创建品牌画面。其中品牌代码必须为英文或数字，中文名称为必填项。
                                                               单击“保存”即可创建新的品牌。
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                               编辑:点击“编辑”将进入品牌编辑画面，其中中文名称为必填项。单击“保存”即可完成编辑操作。                      
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                               停用/启用:选择需要启用或者停用的组织，在弹出的确认框，点击“确定”按钮，即可完成组织的启用或者停用。                      
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
