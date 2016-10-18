<%--部门仓库关系配置帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		部门仓库关系配置，可以对部门和仓库之间的关系进行相互配置。其中部门名称和仓库名称都具有查询功能。例如：输入部门编号或者部门
               		名称可以查询出部门名称。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                 查询:在查询条件区域填写所需的查询项，点击“查询”按钮，显示查询的部门关系一览表。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                             添加部门仓库关系：点击“添加部门仓库关系”按钮，弹出新添部门仓库配置窗口，选择好需要配置的相关仓库和部门，
                                                             点击“确定”按钮，配置好的部门仓库关系将显示在查询列表中。
                    </div>
                    <div class="line"></div>
                </div>  
               
            	<div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                             编辑:点击“编辑”按钮，弹出部门仓库关系配置的编辑窗口，修改好相关内容后，点击“确定”按钮，保存修改后的信息。                                                                                                
                    </div>
                    <div class="line"></div>
                </div>           	
            </div>
        </div>