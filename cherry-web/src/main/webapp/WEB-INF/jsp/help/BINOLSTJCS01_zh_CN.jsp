<%--实体仓库维护一览模块帮助页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		实体仓库维护一览模块是对实体仓库信息的一个查询画面。其中部门名称和实体仓库都具有查询功能。例如：输入部门编号或者部门
               		名称可以查询出部门名称。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>             
                    <div class="step">
                                                                  查询:在“实体仓库维护”页面的查询条件区域填写所需的查询项，点击“查询”按钮，显示查询的实体仓库列表。默认为查询所有有效的实体仓库信息。
                                                                  也可以自己勾选“包含停用仓库”复选框，将启用与停用的符合条件的实体仓库信息全部查询出来。点击仓库列表中的仓库编号，能查看该仓库的详细信息。
                    </div>
                    <div class="line"></div>
                </div>                          
                
                <div class="step-content">
                    <label>2</label>               
                    <div class="step">                                       
                                                             创建实体仓库：点击“创建实体仓库”按钮，打开创建实体仓库页面。在新建页面中，填写好相关的信息，
                                                             点击“添加关联部门”按钮，其中带*号为必填项，弹出部门信息对话框。选择好相关的部门信息，也可以通过输入部分关键字，使用
                                                             查找功能完成部门的模糊查找，点击“确定”按钮，添加的部门信息将显示在页面中，
                                                             新添加的部门信息可以编辑备注内容和删除该部门信息，最后点击“保存”按钮，可将新建的实体仓库信息显示
                                                             在“实体仓库维护”页面中的查询列表中。
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>  
                         	
                <div class="step-content">
                    <label>3</label>               
                    <div class="step">
                                                              停用/启用:停用的实体仓库，要勾选查询条件的“包含无效仓库”选项查询后，才会在查询结果显示。
				                     勾选已启用的仓库，点击“停用”按钮，可停用该仓库。勾选已停用的仓库，点击“启用”按钮，可启用该仓库。        
                    </div>
                    <div class="line"></div>
                </div>           	
            	       	
            	<div class="step-content">
                    <label>4</label>               
                    <div class="step">
                                                             编辑:点击一个仓库编号，进入实体仓库明细画面，可以查看仓库明细信息。点击编辑按钮，进入编辑实体仓库画面，修改后点击保存即可。
                                                             点击返回可以回到渠道详细画面。                          
                                                                                   
                    </div>
                    <div class="line"></div>
                </div>           	
            </div>
        </div>

