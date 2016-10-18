/*	
 * @(#)CherryShow.java     1.0 2010/10/12		
 * 		
 * Copyright (c) 2010 SHANGHAI BINGKUN DIGITAL TECHNOLOGY CO.,LTD		
 * All rights reserved		
 * 		
 * This software is the confidential and proprietary information of 		
 * SHANGHAI BINGKUN.("Confidential Information").  You shall not		
 * disclose such Confidential Information and shall use it only in		
 * accordance with the terms of the license agreement you entered into		
 * with SHANGHAI BINGKUN.		
 */

package com.cherry.cm.cherrytags;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import org.apache.taglibs.standard.lang.support.ExpressionEvaluatorManager;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.w3c.dom.Document;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.interfaces.BINOLCM19_IF;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryMenu;

/**
 * 功能权限判断用的自定义标签
 * 
 * @author LiPC
 * @version 1.0 2010/10/12
 */
@SuppressWarnings("unchecked")
public class CherryShow extends TagSupport {

	private static final long serialVersionUID = 5353200000691385699L;

	/** 要显示的控件ID */
	private String domId;	
	
	/**
	 * 权限控制用
	 * 该属性应该取单据的创建者（或者其他参与者）
	 * 如果该属性的值和当前登录用户的员工ID相同，则能看见该标签
	 * 
	 */
	private String employeeId;




	@Override
	public int doStartTag() throws JspException {
		
		if(domId != null) {
			HttpSession session = pageContext.getSession();
			if(session != null) {
				UserInfo userinfo = (UserInfo)session.getAttribute(CherryConstants.SESSION_USERINFO);
				//如果配置了employeeId，则直接判断是否和session中相符
				if(employeeId!=null && employeeId.equals(String.valueOf(userinfo.getBIN_EmployeeID()))){
					return EVAL_BODY_INCLUDE;
				}
				
				//0：没有配置工作流规则
				//1：配置了规则，但是用户不符合规则
				//2：配置了规则，且用户符合规则
				//PageContext pageContext = (PageContext) this.getJspContext(); 
				ServletContext servletContext = pageContext.getServletContext(); 
				WebApplicationContext wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext); 

				/* 从上下文中获取指定的Bean */ 
				BINOLCM19_IF binOLCM19_BL = (BINOLCM19_IF)wac.getBean("binOLCM19_BL"); 

				int flag = binOLCM19_BL.macthCherryShowRule(userinfo, domId);
				// 取得所有权限
				Map<String, Object> xmldocumentmap = (Map<String, Object>)session.getAttribute(CherryConstants.SESSION_LEFTMENU_XMLDOCMAP);
				if(xmldocumentmap != null) {
					// 取得当前子系统菜单ID
					String topmenucurrent = (String)session.getAttribute(CherryConstants.SESSION_TOPMENU_CURRENT);
					if(topmenucurrent != null) {
						// 取得对应菜单下的权限
						CherryMenu doc = (CherryMenu)xmldocumentmap.get(topmenucurrent);
						// 判断控件是否存在权限中,存在的场合显示控件
						if(doc != null && doc.getChildMenuByID(domId) != null) {
							if(flag!=1){
								return EVAL_BODY_INCLUDE;
							}
						}else{
							if(flag==2){
								return EVAL_BODY_INCLUDE;
							}
						}
					}
					// 取得所有菜单信息
					List topMenuList = (List)session.getAttribute(CherryConstants.SESSION_TOPMENU_LIST);
					if(topMenuList != null && !topMenuList.isEmpty()) {
						for(int i = 0; i < topMenuList.size(); i++) {
							Map topMenuMap = (Map)topMenuList.get(i);
							// 取得菜单ID
							String topmenu = (String)topMenuMap.get("MENU_ID");
							// 菜单ID为当前菜单时，由于前面已做权限判断，这里直接跳过
							if(topmenu.equals(topmenucurrent)) {
								continue;
							}
							// 取得对应菜单下的权限
							CherryMenu doc = (CherryMenu)xmldocumentmap.get(topmenu);
							// 判断控件是否存在权限中,存在的场合显示控件
							if(doc != null && doc.getChildMenuByID(domId) != null) {
								if(flag!=1){
									return EVAL_BODY_INCLUDE;
								}
							}else{
								if(flag==2){
									return EVAL_BODY_INCLUDE;
								}
							}
						}
					}
				}
			}
		}
		return SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {

		return EVAL_PAGE;
	}

	public String getDomId() {
		return domId;
	}

	public void setDomId(String id) {
		this.domId = id;
	}
	

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		try{
		this.employeeId = String.valueOf(ExpressionEvaluatorManager.evaluate("employeeId", employeeId, java.lang.String.class, this, pageContext));   
		}catch(Exception ex){
			
		}
	}
}
