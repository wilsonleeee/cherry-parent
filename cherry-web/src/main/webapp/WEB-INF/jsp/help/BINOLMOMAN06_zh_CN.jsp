<%--U盘管理模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		U盘管理模块是查询是一个U盘信息的查询画面。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>               
                    <div class="step">
                                                                 添加U盘信息：点击“添加U盘信息”按钮进入U盘信息添加画面。该画面有两种添加方式，一种是添加单个U盘信息，通过手动输入
                      U盘编号,员工代码后,点选“添加”。另一种添加多条信息，通过EXCEL模板输入多条信息，然后导入输入信息的模板 ，单击“批量”
                                                                   导入即可在下表中列出添加的U盘信息，验证正确之后要点“保存”按钮。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">
                                                                绑定/解除绑定：绑定/解除绑用于已经下发的U盘信息，绑定即为U盘绑定一个用户。点击“绑定”按钮，弹出员工信息窗口，选择
                                                                一个用户或者在输入框中输入部分关键字，点击“查找”进行模糊查询用户信息，点击“保存”即可完成绑定。返回到U盘管理画面，
                                                                勾选一个或者多个U盘信息，可以对其解除绑定，也可以删除U盘信息。
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
       