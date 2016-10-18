<%--实体仓库维护模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">
               	<div class="text">
               		实体仓库维护模块是一个对出入仓库/区域的一个业务类型的一个查询画面。其中仓库/区域可以进行查询。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                           查询:在查询条件区域填写所需的查询项，点击“查询”按钮，将会在结果一览下面显示出实体仓库的业务信息。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                             添加业务配置：点击“添加业务配置”按钮，弹出仓库业务关系配置窗口，可以选择业务的类型，出库的仓库以及对应的一个或者
                                                             多个入库的仓库。其中入库仓库可以通过仓库编号或者仓库名称查询得到，点击“确定”按钮，配置好实体仓库的业务将显示在查询
                                                             列表中。
                    </div>
                    <div class="line"></div>
                </div> 
                
            	<div class="step-content">
                    <label>3</label>               
                    <div class="step">                   
                                                              编辑:点击“编辑”按钮，弹出仓库业务关系配置窗口，修改好相关内容后，点击“确定”按钮，保存修改后的信息。
                    </div>
                    <div class="line"></div>
                </div>           	
            </div>
        </div>