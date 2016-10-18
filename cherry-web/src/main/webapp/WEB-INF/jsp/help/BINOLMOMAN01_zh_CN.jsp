<%--终端一览模块 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		终端一览模块是对终端机信息的一个查询画面，其中柜台名称是可以查询的。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:查询按钮用于查询符合条件的终端机，并将结果显示在查询结果一览下。 
                    </div>
                    <div class="line"></div>
                </div>
              
            	<div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                            启用/停用/下发：已经下发的机器处于启用与停用的两种状态，选择停用的机器再启用或者选择启用的机器再停用都可以改变
                                                            机器的状态。但是未下发的机器是不能够启用与停用的，所以要先下发，再启用或停用机器。                                               
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                          解除绑定/绑定柜台：下发的机器可以绑定柜台，所以下发的机器后面都会出现一个绑定柜台按钮，点击“绑定柜台按钮”，
                                                          进入绑定柜台画面，可以在任意柜台下面选择一个柜台，如果柜台很多也可以通过查找功能，在输入框中输入需要柜台的部分
                                                          关键字会进行模糊查询给出结果。 也可以选择多个机器，然后点击“解除绑定”完成解除绑定功能。
                    </div>
                    <div class="line"></div>
                </div>           	           	
            </div>
        </div>
       