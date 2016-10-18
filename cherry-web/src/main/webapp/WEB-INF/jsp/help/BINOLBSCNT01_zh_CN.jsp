<%--柜台一览模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		柜台一览模块是专对柜台信息的一个查询化界面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                                  查询:点击“查询”，默认为查询所有启用柜台信息。
                                                                  也可以自己勾选“包含停用柜台”复选框，将启用与停用的符合条件的柜台信息全部查询出来。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                               创建柜台：点击“创建”柜台按钮，进入创建柜台画面，其中柜台编号是系统自动生成的，也允许用户
                                                               手动输入。柜台类型分为正式柜台，测试柜台。其中当柜台为测试柜台时，柜台名称必须包含 “测试”
                                                               或者“測試”。点击柜台主管名称即可进入员工信息画面，可以看到所有主管信息，单选上一个主管即可设置柜台主管。                                                      
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                               停用/启用:可以将选中的柜台进行启用，停用选中的柜台，选中的柜台将会从页面中消失，所以查询结果一览下面默认的柜台都为启用柜台。
                                                               如果需要启用停用柜台，勾选住“包含停用柜台”复选框，点击“查询”按钮即可。                      
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
                                                             需要注意的：当品牌柜台是在新后台中维护的时候，需要将系统配置项页面中的“是否调用Webservice进行柜台数据同步”项设置为是。                                                            
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
        
