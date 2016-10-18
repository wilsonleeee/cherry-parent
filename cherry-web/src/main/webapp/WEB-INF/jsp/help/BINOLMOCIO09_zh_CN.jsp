<%--考核问卷管理模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		考核问卷管理模块是一个对考核问卷信息的一个查询化画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                              查询:查询按钮用于查询符合条件的考核问卷信息。 还可以点击任意一个问卷名称查看考核问卷详细信息。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                             编辑/停用/启用:问卷中的状态如果是制作中的状态，则可以编辑删除等操作。可使用状态不能进行编辑，如果要编辑请先停用 。
                    </div>
                    <div class="line"></div>
                </div> 
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                             新增问卷:点击“新增问卷”按钮，进入“问卷制作”画面。添加问题前先要创建一个分组，即问题要添加在分组中。
                                                             添加问题下面有二个按钮，分别是单选题和填空填， 可以根据需要进行题目的选择，分别会弹出对应的窗口，
                                                             输入好题目，选项以及分值。 编辑完成后，点击“下一题”， 添加的问题将会在“已经添加的问题”区域显示出来。
                                                            点击“预览”按钮，进行问卷查看，最后点击“确定”，弹出窗口，显示问卷部分，点击“保存”，结束新增问卷。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                             编辑/删除/上移/下移:在已经添加的问题的后面，分别可以对问题进行编辑，删除，上移和下移。
                    </div>
                    <div class="line"></div>
                </div>        	
            </div>
        </div>
        
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>注意事项</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                             需要注意的：一次只能停用和编辑一张问卷。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
       