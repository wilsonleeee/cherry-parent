/*
 * @(#)BINOLPLRLA99_BL.java     1.0 2010/10/27
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

package com.cherry.pl.rla.bl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.DateUtil;
import com.cherry.pl.common.PrivilegeConstants;
import com.cherry.pl.rla.service.BINOLPLRLA99_Service;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 角色分配BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLPLRLA99_BL {
	
	/** 角色分配Service */
	@Resource
	private BINOLPLRLA99_Service binOLPLRLA99_Service;
	
	/**
	 * 取得角色信息List
	 * 
	 * @param map 画面传入参数
	 * @return 角色信息List
	 */
	public List<Map<String, Object>> getRoleInfoList(Map<String, Object> map) throws Exception {
		
		List<Map<String, Object>> roleInfoList;
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 选中角色信息
		String roleAssign = (String)map.get("roleAssign");
		if(roleAssign != null && !"".equals(roleAssign)) {
			list = (List<Map<String, Object>>)JSONUtil.deserialize(roleAssign);
		}
		// 组织ID存在的场合
		if(map.get(CherryConstants.ORGANIZATIONID) != null) {
			// 角色分类设定为组织角色
			map.put(PrivilegeConstants.ROLE_KIND, PrivilegeConstants.ROLE_KIND_0);
			if(roleAssign == null) {
				// 取得组织角色List
				list = binOLPLRLA99_Service.getOrganizationRoleList(map);
			}
		} 
		// 岗位类别ID存在的场合
		else if(map.get(CherryConstants.POSITIONCATEGORYID) != null) {
			// 角色分类设定为岗位类别角色
			map.put(PrivilegeConstants.ROLE_KIND, PrivilegeConstants.ROLE_KIND_1);
			if(roleAssign == null) {
				// 取得岗位类别角色List
				list = binOLPLRLA99_Service.getPositionCategoryRoleList(map);
			}
		} 
		// 岗位ID存在的场合
		else if(map.get(CherryConstants.POSITIONID) != null) {
			// 角色分类设定为岗位角色
			map.put(PrivilegeConstants.ROLE_KIND, PrivilegeConstants.ROLE_KIND_2);
			if(roleAssign == null) {
				// 取得岗位角色List
				list = binOLPLRLA99_Service.getPositionRoleList(map);
			}
		} 
		// 用户ID存在的场合
		else if(map.get(CherryConstants.USERID) != null) {
			// 角色分类设定为用户角色
			map.put(PrivilegeConstants.ROLE_KIND, PrivilegeConstants.ROLE_KIND_3);
			if(roleAssign == null) {
				// 取得用户角色List
				list = binOLPLRLA99_Service.getUserRoleList(map);
			}
		}
		// 取得角色信息List
		roleInfoList =  binOLPLRLA99_Service.getRoleInfoList(map);
		if(roleInfoList != null && !roleInfoList.isEmpty()) {
			if(list != null && !list.isEmpty()) {
				for(int i = 0; i < roleInfoList.size(); i++) {
					Map<String, Object> roleInfo = roleInfoList.get(i);
					int roleId = (Integer)roleInfo.get(PrivilegeConstants.ROLE_ID);
					for(int j = 0; j < list.size(); j++) {
						if(list.get(j).get(PrivilegeConstants.ROLE_ID) != null 
								&& roleId == Integer.parseInt(list.get(j).get(PrivilegeConstants.ROLE_ID).toString())) {
							String startDate = (String)list.get(j).get(PrivilegeConstants.START_DATE);
							if(startDate == null || "".equals(startDate)) {
								startDate = binOLPLRLA99_Service.getSYSDate().substring(0,10);;
							}
							roleInfo.put(PrivilegeConstants.START_DATE, startDate);
							// 有效期限
							String expireDate = (String)list.get(j).get(PrivilegeConstants.EXPIRE_DATE);
							if(expireDate != null && !PrivilegeConstants.EXPIRE_DATE_VALUE.equals(expireDate)) {
								roleInfo.put(PrivilegeConstants.EXPIRE_DATE, expireDate);
							}
							roleInfo.put(PrivilegeConstants.PRIVILEGE_FLAG, list.get(j).get(PrivilegeConstants.PRIVILEGE_FLAG));
							roleInfo.put(PrivilegeConstants.CHECKBOX, PrivilegeConstants.CHECKBOX_CHECKED);
							list.remove(j);
							break;
						}
					}
				}
			}
		}
		return roleInfoList;
	}
	
	/**
	 * 保存分配的角色
	 * 
	 * @param map 角色分配信息
	 */
	public void saveRoleAssigned(Map<String, Object> map) throws Exception {
		
		// 选中角色信息
		String roleAssign = (String)map.get("roleAssign");
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 选中角色信息不为空时
		if(roleAssign != null && !"".equals(roleAssign)) {
			list = (List<Map<String, Object>>)JSONUtil.deserialize(roleAssign);
		}
		// 组织ID存在的场合
		if(map.get(CherryConstants.ORGANIZATIONID) != null) {
			// 删除该组织已有的角色
			binOLPLRLA99_Service.deleteOrganizationRole(map);
			if(list != null && list.size() > 0) {
				for(int i = 0; i < list.size(); i++) {
					setDate(list.get(i));
					list.get(i).put(CherryConstants.ORGANIZATIONID, map.get(CherryConstants.ORGANIZATIONID));
				}
				// 添加组织角色
				binOLPLRLA99_Service.addOrganizationRole(list);
			}
		} 
		// 岗位类别ID存在的场合
		else if(map.get(CherryConstants.POSITIONCATEGORYID) != null) {
			// 删除该岗位类别已有的角色
			binOLPLRLA99_Service.deletePositionCategoryRole(map);
			if(list != null && list.size() > 0) {
				for(int i = 0; i < list.size(); i++) {
					setDate(list.get(i));
					list.get(i).put(CherryConstants.POSITIONCATEGORYID, map.get(CherryConstants.POSITIONCATEGORYID));
				}
				// 添加岗位类别角色
				binOLPLRLA99_Service.addPositionCategoryRole(list);
			}
		} 
		// 岗位ID存在的场合
		else if(map.get(CherryConstants.POSITIONID) != null) {
			// 删除该岗位已有的角色
			binOLPLRLA99_Service.deletePositionRole(map);
			if(list != null && list.size() > 0) {
				for(int i = 0; i < list.size(); i++) {
					setDate(list.get(i));
					list.get(i).put(CherryConstants.POSITIONID, map.get(CherryConstants.POSITIONID));
				}
				// 添加岗位角色
				binOLPLRLA99_Service.addPositionRole(list);
			}
		} 
		// 用户ID存在的场合
		else if(map.get(CherryConstants.USERID) != null) {
			// 删除该用户已有的角色
			binOLPLRLA99_Service.deleteUserRole(map);
			if(list != null && list.size() > 0) {
				for(int i = 0; i < list.size(); i++) {
					setDate(list.get(i));
					list.get(i).put(CherryConstants.USERID, map.get(CherryConstants.USERID));
				}
				// 添加用户角色
				binOLPLRLA99_Service.addUserRole(list);
			}
		}
	}
	
	/**
	 * 开始日期和有效期限登录数据库前的日期格式化
	 * 
	 * @param map 角色分配信息
	 */
	public void setDate(Map<String, Object> map) {
		// 开始日期
		String startDate = (String)map.get(PrivilegeConstants.START_DATE);
		// 开始日期为空时
		if(startDate == null || "".equals(startDate)) {
			// 默认设置当天
			startDate = binOLPLRLA99_Service.getSYSDate().substring(0,10);
		}
		map.put(PrivilegeConstants.START_DATE, DateUtil.coverString2Date(startDate));
		// 有效期限
		String expireDate = (String)map.get(PrivilegeConstants.EXPIRE_DATE);
		// 有效期限为空时
		if(expireDate == null || "".equals(expireDate)) {
			// 默认设置一个大日期
			expireDate = PrivilegeConstants.EXPIRE_DATE_VALUE;
		}
		map.put(PrivilegeConstants.EXPIRE_DATE, DateUtil.coverString2Date(expireDate));
	}
	
	/**
	 * 取得系统时间
	 * 
	 * @return 系统时间
	 */
	public String getSysDate() {
		return binOLPLRLA99_Service.getSYSDate();
	}

}
