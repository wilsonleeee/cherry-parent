<!--组织管理一览模块页面  -->
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		组织管理一览模块是专对组织信息的一个查询化界面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:查询按钮用于查询符合条件的组织信息，查询条件为组织名称。默认为查询所有启用的组织， 也可以自己勾选“包含停用组织”复选框，
                                                                  将启用与停用的符合条件的组织信息全部查询出来。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                              停用/启用:选择需要启用或者停用的组织，在弹出的确认框，点击“确定”按钮，即可完成组织的启用或者停用。
                    </div>
                    <div class="line"></div>
                </div>           	
            	       	
            	<div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                             编辑:点击“编辑”按钮，进入组织编辑画面，其中组织中文名称为必填项，填写需要修改的信息，单击“保存”按钮可将修改的信息进行保存。 
                    </div>
                    <div class="line"></div>
                </div>           	
            </div>
        </div>
       