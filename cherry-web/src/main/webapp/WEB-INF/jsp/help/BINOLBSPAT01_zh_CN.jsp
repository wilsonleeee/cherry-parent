<%--往来单位一览模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		往来单位一览模块是专对往来单位的一个查询化界面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:查询按钮用于查询符合条件的往来单位信息，往来单位的查询条件有单位编码和单位名称。默认为查询所有启用的往来单位， 也可以自己勾选“包含停用往来单位”复选框，
                                                                  将启用与停用的符合条件的往来单位信息全部查询出来。                         
                    
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                             创建单位：点击“创建单位”按钮进入创建单位画面，其中带*号为必填项，单位编码必须输入数字和英文。 
                                                             点击“保存”即可创建单位。新建单位信息将在“往来单位一览”页面的查询列表中显示。                                     
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>  
                         	
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                              停用/启用:选择需要启用或者停用的往来户，在弹出的确认框，点击“确定”按钮，即可完成往来户的启用或者停用。
                    </div>
                    <div class="line"></div>
                </div> 
            </div>
        </div>
      