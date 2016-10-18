<%--角色管理模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		角色管理模块是对角色信息的一个查询画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:点击“询按”钮会在角色一览下面显示出角色相关信息。其中角色信息可以也可以根据查询条件按照部门角色，岗位角色，
                                                                  用户角色查询。一般的默认是查询全部的角色信息。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                            修改：点击“修改”按钮进入更新角色画面，可以修改角色名称，角色分类以及角色描述信息，单击“确定”即可完成角色的更新。                                  
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                            授权：点击“授权”按钮进入角色授权画面，点击左边的子系统，在右边展现出来的画面中选择该子系统和子系统下面的模块。
                                                            单击“确定”即可完成授权。在修改了角色信息后单击保存后，在保存成功的画面里面也可以对角色进行授权。                                  
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                            添加角色：点击“添加角色”按钮进入添加角色画面，填写相关项，带*号为必填项，填写完成后然后点击“确认”按钮，返回到角色管理主页面。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
       