<%--角色分配帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		角色分配页面是对角度进行分配的一个画面。其中有按部门分配，岗位分配和用户角色分配三种分配方式。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  部门角色分配：点击“部门角色分配”，在左边选择将要分配角色权限的部门，在右边勾选已配置好的角色权限，可以选择使用该角色权限的有效日期，
                                                                  如不设置有效日期系统将默认为永久权限。最后点击“保存”按钮，保存已分配好的角色权限。
                    </div>
                    <div class="line"></div>
                </div> 
                
                <div class="step-content">
                    <label>2</label>             
                    <div class="step">
                                                                  岗位角色分配：点击“岗位角色分配”按钮，在左边选择要分配角色权限的岗位，在右边勾选已设置好的角色权限，
                                                                  可以选择该角色权限的有效日期，如不设置有效日期系统将默认为永久权限。最后点击“保存”按钮，
                                                                  保存已分配好的角色权限。
                    </div>
                    <div class="line"></div>
                </div> 
                
                <div class="step-content">
                    <label>3</label>             
                    <div class="step">
                                                                  用户角色分配：点击“用户角色分配”按钮，出现所有用户一览列表，可以在查询条件区域根据“用户帐号”和“用户姓名”查询某一用户信息。
                                                                  每一条用户信息后面都有“分配”按钮，点击后弹出用户角色分配窗口，
                                                                  可以给用户分配角色权限，编辑好要分配的权限信息，点击“确认”保存信息，返回到“用户角色分配”主页面。
                    </div>
                    <div class="line"></div>
                </div> 
            </div>
        </div>
       