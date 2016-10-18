<%--订货参数帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		订货参数设定是一个对订货方面涉及到的参数进行设定的画面，其中可设置因素包括：产品参数设定，柜台参数设定，最低库存天数设定。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>      
                    <div class="step">
                                                               查询：点击“产品参数设定”按钮，在查询条件里面填写好相关的信息，点击“查询”按钮，在结果一览下面查询出信息结果。
                                                               点击“柜台参数设定”按钮，点击“查询”按钮，将柜台参数显示在结果一览下面，在此页面，可以对柜台订货参数进行编辑，
                                                               点击“编辑”按钮，在弹出的“设定柜台订货参数”对话框中，可以修改“订货间隔”和“在途天数”，点击“确定”完成参数的
                                                               修改。在“最低库存天数设定”页面中，点击“查询”按钮，查询出信息结果，点击“编辑”按钮，在弹出的“设定最低库存天数”
                                                               可以修改“最低库存天数”，点击“确定”，即可完成修改。                                                      
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>2</label>
                    <div class="step">                                          
                                                               设定产品订货参数：在“产品参数设定”页面，点击“设定产品订货参数”按钮，弹出窗口，在选择产品中选择一类或者
                                                               多类产品信息，也可以在输入框中输入部分关键字，点击“定位”按钮进入产品的选择，选择完毕后，输入该产品的“
                                                               查询年月”和“调整系数”即可完成产品参数的设定。
                                                               
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                               下发所有参数：点击“下发所有参数”按钮，弹出“确定要进行各类参数下发吗”的提示框，选择“确定”即可
                                                               将所有类型的需要下发的参数下发。                                 
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
     	
     	