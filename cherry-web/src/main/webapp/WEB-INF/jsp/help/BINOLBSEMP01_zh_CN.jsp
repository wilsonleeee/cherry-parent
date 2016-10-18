<%--用户信息维护模块页面 --%>
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>  
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>功能描述</strong></div>
            <div class="box-yew-content bg_yew">                
               	<div class="text">
               		用户信息维护模块是对用户信息进行维护的一个画面，包含对用户的查看，修改，增加操作，其显示模式有树型模式和列表模式。
               		在“用户信息维护”页面，点击标签按钮“基本信息”、“考勤记录”和“数据权限”，可以进行查看人员的相关信息。
               	</div>
            </div>
        </div>	
        <div class="box-yew">
            <div class="box-yew-header clearfix"><strong class="left"><span class="ui-icon icon-help-star-yellow"></span>使用方法</strong></div>
            <div class="box-yew-content">
            	<div class="step-content">
                    <label>1</label>
                    <div class="step">                          
                                                                  定位:在输入框中输入部分关键字，点击“定位”按钮，查询出符合条件的用户信息，并以树形结构显示。也可以展开前面的加号查询。
                                                                 选中的一个部门，在右边窗口即将出现用户的具体画面。       
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>2</label>
                    <div class="step">
                    	添加人员：在“用户信息维护”页面，点击“添加人员”按钮，进入添加人员画面。其中带*为必填项，红色字体文字为特殊说明。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>3</label>
                    <div class="step">
                    	上司：在“添加人员”页面上，点击“上司”按钮，弹出员工信息窗口，为添加的人员设置上司，选择相关的人员，点击“确定”按钮，
                    	            确认设置后的信息。上司只能设置一个。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>4</label>
                    <div class="step">
                    	关注人员：在“添加人员”页面上，点击“关注人员”按钮，弹出员工信息窗口，选择关注的人员，点击“确定”按钮，确认选择的员工信息。
                                                                                            关注人员可多选。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>5</label>
                    <div class="step">
                    	管辖部门：在“添加人员”页面上，点击“管辖部门”按钮，弹出部门信息窗口，选择相关管辖部门，点击“确定”按钮，确认设置后的信息。
                                                                        其中管辖部门可多选。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>6</label>
                    <div class="step">
                    	关注部门：在“添加人员”页面上，点击“关注部门”按钮，弹出部门信息窗口，选择相关的关注部门，点击“确定”按钮，确认设置后的信息。
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>7</label>
                    <div class="step">
                    	显示更多信息：在“添加人员”页面上点击“显示更多信息”在下面的列表中会出现地址信息和入离职信息，根据需要填写好相关的信息，点击“保存”按钮，
                    	即可完成信息的保存并且完成部门的创建。
                    </div>
                    <div class="line"></div>
                </div>
                <div class="step-content">
                    <label>8</label>               
                    <div class="step">
                                                              编辑：在左边的树型结构中选择一个部门，然后点击它，在右边显示该部门的具体信息。点击该页面的“编辑”按钮，
                                                              进入“人员信息编辑”画面，根据需要对其信息进行编辑，其具体操作可以参照上面的“创建部门”部分。信息修改完毕后，
                                                              点击“保存”按钮即可完成信息的编辑工作。       
                    </div>
                    <div class="line"></div>
                </div>
            	
                <div class="step-content">
                    <label>9</label>               
                    <div class="step">                    
                                                               查询（列表模式）：在列表模式下，点击“查询”按钮，将结果显示在查询结果一览下面。
                                                               查询信息可以根据查询条件自由设置，默认为查询所有启用人员信息。 也可以自己勾选
                     “包含停用人员”复选框，将启用与停用的符合条件的人员信息全部查询出来。 点击人员代号还可以查看人员明细信息。                      
                    </div>
                    <div class="line"></div>
                </div>
                
                <div class="step-content">
                    <label>10</label>               
                    <div class="step">
                                                               启用/停用：选择需要启用或者停用的用户，在弹出的确认框，点击“确定”按钮，即可完成用户的启用或者停用。                   
                    </div>
                    <div class="line"></div>
                </div>
            </div>
        </div>
