<%--产品维护帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		产品维护是专对产品信息的查询，在“产品维护”页面，点击树形列表中的某些分类，可以查看该分类下的所有产品列表。
               		点击“树模式”或“列表模式”可改变查询列表的显示模式。系统默认显示的是树模式。点击“添加产品”按钮，可添加新的产品信息。
               		点击列表中的产品名字，可以打开该产品的详细信息页面，可以进行编辑。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">
                                                                  定位:在输入框中输入部分关键字，查询出符合条件的用户信息，点击“定位”按钮，并以树形结构显示。也可以展开前面的加号查询。
                                                                  选中的一个部门，在右边窗口即将出现用户的具体画面。  
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                    	添加产品：在产品维护页面，点击“添加产品”按钮，进入产品添加画面，填写新加产品的基本信息，带*号为必填，
                                                              其中厂商编码和产品条码的输入项必须为数字，字母，下划线，点号，横亘。在分类信息区域为添加的产品配置分类信息。
                                                              点击分类信息的“请选择”按钮，为添加的产品选择分类。点击“显示扩展信息”按钮， 展开更多的信息。                            
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>
                    <div class="step">
                    	添加价格： 在“产品添加”页面，点击“添加价格”按钮，将再添加一组价格表格。点击价格表格上方的“删除”按钮，将删除价格表格。
                                                                                             点击表格中“会员价格”旁边的小图标，显示“折扣率编辑框”。最后填写完需要添加的产品信息，点击“保存”按钮进行保存。
                    </div>
                    <div class="line"></div>
                </div>
            
                <div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                               编辑（列表模式）:点击任意一个中文名称，进入产品详细画面。产品详细画面分为四个模块：基本信息，分类信息，销售价格信息，图片信息。
                                                               点击“编辑”按钮，即可进入产品编辑画面。根据需要修改产品信息，点击“保存”即可完成产品的修改。                    
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
     