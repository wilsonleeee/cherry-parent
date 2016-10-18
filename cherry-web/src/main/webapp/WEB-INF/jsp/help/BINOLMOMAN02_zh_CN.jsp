<%--添加终端模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		添加终端模块就是添加终端信息的画面。有两种添加方式，一种是通过页面输入终端信息，点击确认添加即可
               		完成单个终端信息的添加。另一种是通过Excel导入方式添加，这种适用于添加多条终端信息。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                                 模板下载：点击模板下载将打开机器信息导入模板。可以在此模板中增添一条或者多条终端信息，然后选择
                                                                 添加了多条信息的模板文件，点击“批量导入”，即可增添多条终端信息。也可以在本地机器上自己选择一个
                                                                 模板，然后进行导入，注意模板的格式。
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
                                                             需要注意的： 机器编号是真实的物理机器的序列号，符合一定的编码规则。通讯号码是终端的通讯卡号。                                                           
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                                                             所有添加的终端情况都会在下表列出来，验证正确之后要点保存按钮。                                                           
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div> 
       