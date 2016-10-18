<%--渠道一览模块页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		渠道一览模块是专对渠道信息的一个查询化画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:查询按钮用于查询符合条件的渠道信息。默认为查询所有启用的渠道， 也可以自己勾选“包含停用渠道”复选框，
                                                                  将启用与停用的符合条件的渠道信息全部查询出来。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                             添加渠道：点击添加渠道按钮进入添加渠道画面，带*号为必填。渠道类型分为：直营和间营两种。
                                                             填写完成后，点击“保存”按钮，保存新建的渠道。                               
                    </div>
                    <div class="line"></div>
                </div>  
                         	
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                              启用/停用：选择需要启用或者停用的用户，在弹出的确认框，点击“确定”按钮，即可完成用户的启用或者停用。
                    </div>
                    <div class="line"></div>
                </div>           	
            	       	
            	<div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                             编辑:点击一个渠道名称，进入该渠道的渠道详细画面，点击“编辑”按钮，进入渠道编辑画面，修改后点击“保存”即可。
                                                             点击“返回”可以回到渠道详细画面。
                    </div>
                    <div class="line"></div>
                </div>           	
            </div>
        </div>