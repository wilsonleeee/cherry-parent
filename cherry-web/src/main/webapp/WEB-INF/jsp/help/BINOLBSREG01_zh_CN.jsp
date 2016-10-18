<%--区域结构模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		区域结构模块是一个对区域管理的操作画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>      
                    <div class="step">
                                                                 定位:点击定位按钮可以根据区域查询出符合条件的区域信息，并以树形结构显示。也可以展开前面的加号查询。
                                                                 双击选中的一个区域，在右边窗口即将出现区域的具体画面。点击停用按钮，在左边停用的区域将出现灰色，
                                                                 点击启用就可以恢复。                                                                
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>2</label>
                    <div class="step">                                          
                                                               创建区域：点击“创建区域”按钮，打开创建区域页面，填写相关的区域信息，带*号为必填项。
                                                               在“创建区域”页面中点击“管辖省份”按钮，弹出省份信息对话框，选择一个或多个省份，
                                                               点击“确定”按钮确认选择的内容。最后，点击页面上的“保存”的按钮，保存新建的区域信息。
                                                               新建好的区域信息，点击树形结构目录中相应的上级区域，就可以查看到新建的区域。            
                    </div>
                    <div class="line"></div>
                </div>
            	<div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                               编辑：在左边的树型模型中，选择一个区域，在右边显示其具体的信息，点击“编辑”按钮进入编辑区域，
                                                               可以对上级区域进行修改，完成后，点击“保存”按钮可以保存区域信息。                                             
                    </div>
                    <div class="line"></div>
                </div>    
                
                <div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                             停用/启用：选择需要启用或者停用的区域，在弹出的确认框，点击“确定”按钮，即可完成区域的启用或者停用。                                           
                    </div>
                    <div class="line"></div>
                </div>                
            </div>
        </div>
     