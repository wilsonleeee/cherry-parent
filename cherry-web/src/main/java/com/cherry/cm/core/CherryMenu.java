/*  
 * @(#)CherryMenu.java     1.0 2011/05/31      
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
package com.cherry.cm.core;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单
 * 菜单设计成层次结构的
 * @author <a href="mailto:dingyongchang@hotmail.com">XiaoMitang</a>
 * @version 1.0
 */
public class CherryMenu {
	//private static final long serialVersionUID = -6480730530764515023L;
	public CherryMenu(){
		childList = new ArrayList<CherryMenu>();
	}
	//菜单ID
	private String menuID;
	//父菜单ID
	private String parentMenuID;
	//父菜单名称
	private String parentMenuName;
	//菜单名称
	private String menuName;
	//菜单类型
	private String menuType;
	//URL
	private String menuURL;
	
	private String iconCSS;
	

	private String menuTarget;
	//父菜单
	private CherryMenu parentMenu;
	//子菜单列表
	private List<CherryMenu> childList;
	
	/**
	 * @param menu
	 */
	public void addChildMenu(CherryMenu menu){		
		childList.add(menu);		
	}
	
	public CherryMenu getChildMenuByID(String tmpMenuID){
		if(tmpMenuID.equals(menuID)){
			return this;
		}
		if(childList.size()>0){
			for(CherryMenu tmp2 :childList){
				if(tmpMenuID.equals(tmp2.getMenuID())){
					return tmp2;
				}
				if(tmp2.getChildList().size()>0){
					for(CherryMenu tmp3:tmp2.getChildList()){
						if(tmpMenuID.equals(tmp3.getMenuID())){
							return tmp3;
						}
						if(tmp3.getChildList().size()>0){
							for(CherryMenu tmp4:tmp3.getChildList()){
								if(tmpMenuID.equals(tmp4.getMenuID())){
									return tmp4;
								}
							}
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * @return the menuID
	 */
	public String getMenuID() {
		return menuID;
	}
	/**
	 * @param menuID the menuID to set
	 */
	public void setMenuID(String menuID) {
		this.menuID = menuID;
	}
	/**
	 * @return the parentMenuID
	 */
	public String getParentMenuID() {
		return parentMenuID;
	}
	/**
	 * @param parentMenuID the parentMenuID to set
	 */
	public void setParentMenuID(String parentMenuID) {
		this.parentMenuID = parentMenuID;
	}
	/**
	 * @return the menuName
	 */
	public String getMenuName() {
		return menuName;
	}
	/**
	 * @param menuName the menuName to set
	 */
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	/**
	 * @return the menuType
	 */
	public String getMenuType() {
		return menuType;
	}
	/**
	 * @param menuType the menuType to set
	 */
	public void setMenuType(String menuType) {
		this.menuType = menuType;
	}
	/**
	 * @return the childList
	 */
	public List<CherryMenu> getChildList() {
		return childList;
	}
	/**
	 * @param childList the childList to set
	 */
	public void setChildList(List<CherryMenu> childList) {
		this.childList = childList;
	}
	/**
	 * @return the menuURL
	 */
	public String getMenuURL() {
		return menuURL;
	}
	/**
	 * @param menuURL the menuURL to set
	 */
	public void setMenuURL(String menuURL) {
		this.menuURL = menuURL;
	}
	/**
	 * @return the parentMenuName
	 */
	public String getParentMenuName() {
		return parentMenuName;
	}
	/**
	 * @param parentMenuName the parentMenuName to set
	 */
	public void setParentMenuName(String parentMenuName) {
		this.parentMenuName = parentMenuName;
	}
	/**
	 * @return the parentMenu
	 */
	public CherryMenu getParentMenu() {
		return parentMenu;
	}
	/**
	 * @param parentMenu the parentMenu to set
	 */
	public void setParentMenu(CherryMenu parentMenu) {
		this.parentMenu = parentMenu;
	}

	/**
	 * @return the menuTarget
	 */
	public String getMenuTarget() {
		return menuTarget;
	}

	/**
	 * @param menuTarget the menuTarget to set
	 */
	public void setMenuTarget(String menuTarget) {
		this.menuTarget = menuTarget;
	}
	public String getIconCSS() {
		return iconCSS;
	}

	public void setIconCSS(String iconCSS) {
		this.iconCSS = iconCSS;
	}
}
