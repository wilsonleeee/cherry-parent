<%--促销品维护帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		促销品维护管理模块专对促销产品的一个查询化画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>         
                    <div class="step">
                                                           查询:在查询条件区域填写所需的查询项，点击“查询”按钮，将会在结果一览下面显示出有关促销品相关的信息。
                                                           其中，点击任意一个“促销品名称”可以查看该促销品的明细信息。                    
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                              添加促销品:点击“添加促销品”进入促销品添加画面，其中带*的为必填项。管理库存分为需要和不需要两种，
                                                              销售日期必须在停止销售日期前，不然促销品的添加不会添加到终端。填写好相关信息后，点击“保存”即可完成促销品的添加。                                 
                    </div>
                    <div class="line"></div>
                </div>           	                     
                
            	<div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                             编辑:任意点击一个促销品名称，即可进入促销品详细画面。点击“编辑”按钮即可进入促销品编辑画面，根据需要修改促销品信息，
                                                             点击“保存”即可完成促销品的修改。点击“返回”按钮，即可回到促销品详细画面。
                    </div>
                    <div class="line"></div>
                </div>           	
            </div>
        </div>
       
