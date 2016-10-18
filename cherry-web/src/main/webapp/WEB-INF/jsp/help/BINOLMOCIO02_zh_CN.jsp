<%--问卷管理模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		问卷管理模块是对问卷信息的一个查询化画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:查询按钮用于查询符合条件的问卷信息。在查询结果一览中还可以根据问卷的时间状态查看进行中的问卷，已过期的问卷，
                                                                  未开始的问卷或者是其他的。还可以点击任意一个问卷名称查看问卷详细信息，问卷详细画面不仅显示了基本信息，还显示了问题详细。
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
                                                             新增问卷:点击“新增问卷”按钮，进入问卷制作画面。其中“是否为计分问卷”如果选择“是”，则添加问题时会要求输入各答案的分值。
                                                             问卷名称要求问卷名称相同，且不能超过14个字。添加问题根据选择不同类型的题目，会弹出对应的窗口，输入好题目以及选项。
                                                             编辑完成后，点击“下一题”。  新增的问题将会在已经添加的问题下面显示出来，分别可以对问题进行编辑，删除，上移和下移。
                    </div>
                    <div class="line"></div>
                </div>    
                
                <div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                             预览/保存：在“问卷制作”页面，点击“预览”按钮进行问卷查看，最后点击“保存”按钮，结束新增问卷。                                                                                                                              
                    </div>
                    <div class="line"></div>
                </div>                   	
            	       	
            	<div class="step-content">
                    <label>5</label>               
                    <div class="step">
                                                            下发：点击“下发”按钮进入问卷下发画面，“其中下发到选中柜台”是下发到该问卷到下表所勾选的柜台，“禁止下发到选中柜台”
                                                            则下发到没有选中的柜台，根据需要选择一种下发方式即可。如果柜台有很多，可以使用定位功能迅速找到需要的柜台，输入
                                                            框中输入需要柜台的部分关键字会进行模糊查询给出结果 。                                              
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>6</label>               
                    <div class="step">
                                                            查看发布：点击查看“发布”按钮进入发布范围画面，这里显示了问卷发布的柜台范围。
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
                                                             如果是计分问卷则要输入分值，输入完分值后不能再次改变为非计分问卷。  一次只能下发一张问卷。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                            注意问卷的状态，制作中的状态，可以编辑删除等操作；可使用状态不能进行编辑，如果要编辑请先停用。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
       