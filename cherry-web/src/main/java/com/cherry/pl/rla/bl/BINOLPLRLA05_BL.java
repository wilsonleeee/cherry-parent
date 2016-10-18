package com.cherry.pl.rla.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.rla.interfaces.BINOLPLRLA05_IF;
import com.cherry.pl.rla.service.BINOLPLRLA05_Service;

/*  
 * @(#)BINOLPLRLA05_BL.java    1.0 2012-4-5     
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
public class BINOLPLRLA05_BL implements BINOLPLRLA05_IF {
	
	@Resource
	private BINOLPLRLA05_Service binOLPLRLA05_Service;

	/**
	 * 根据员工查询角色页面	取得员工总数
	 * 
	 * 
	 * */
	@Override
	public int getEmployeeCount(Map<String, Object> map) {
		return binOLPLRLA05_Service.getEmployeeCount(map);
	}

	/**
	 * 根据员工查询角色页面	取得员工LIST
	 * 
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getEmployeeList(Map<String, Object> map) {
		return binOLPLRLA05_Service.getEmployeeList(map);
	}

	/**
	 * 根据角色查询员工画面	取得角色总数
	 * 
	 * 
	 * */
	@Override
	public int getRoleCount(Map<String, Object> map) {
		return binOLPLRLA05_Service.getRoleCount(map);
	}

	/**
	 * 根据角色查询员工画面	取得角色LIST
	 * 
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getRoleList(Map<String, Object> map) {
		return binOLPLRLA05_Service.getRoleList(map);
	}

	/**
	 * 根据员工ID查询其所对应的角色（包括部门、岗位以及用户角色）
	 * 
	 * */
	@Override
	public List<Map<String, Object>> getRolesByEmployee(Map<String, Object> map) {
		return binOLPLRLA05_Service.getRolesByEmployee(map);
	}

	/**
	 * 根据某个角色ID查询出拥有这些角色的员工
	 * 
	 * */
	@Override
	public Map<String, Object> getEmployeesByRole(Map<String, Object> map) {
		
		String roleKind = (String)map.get("RoleKind");
		
		List<Map<String,Object>> employeeList = null;
		int count = 0;
		
		//如果是部门角色
		if("0".equals(roleKind)){
			employeeList = binOLPLRLA05_Service.getEmpByOrgRoleList(map);
			count = binOLPLRLA05_Service.getEmpByOrgRoleCount(map);
		}//如果是岗位类别角色
		else if("1".equals(roleKind)){
			employeeList = binOLPLRLA05_Service.getEmpByPostCatRoleList(map);
			count = binOLPLRLA05_Service.getEmpByPostCatRoleCount(map);
		}//如果是用户角色
		else if("3".equals(roleKind)){
			employeeList = binOLPLRLA05_Service.getEmpByUserRoleList(map);
			count = binOLPLRLA05_Service.getEmpByUserRoleCount(map);
		}else{
			employeeList = new ArrayList<Map<String,Object>>();
		}
		
		Map<String ,Object> resultMap = new HashMap<String ,Object>();
		resultMap.put("list", employeeList);
		resultMap.put("count", count);
		return resultMap;
	}

	/**
	 * 根据员工ID查询其所拥有的菜单资源
	 * 
	 * */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getMenusByEmployee(Map<String, Object> map) {
		
		// 根据员工ID查询其所对应的角色（包括部门、岗位以及用户角色）
		List<Map<String, Object>> rolesList = binOLPLRLA05_Service.getRolesByEmployee(map);
		if(rolesList != null && !rolesList.isEmpty()) {
			// 允许角色
			List<String> isRoleList = new ArrayList<String>();
			// 禁止角色
			List<String> notRoleList = new ArrayList<String>();
			for(Map<String, Object> roleInfo : rolesList) {
				String privilegeFlag = (String)roleInfo.get("PrivilegeFlag");
				if(privilegeFlag != null && "0".equals(privilegeFlag)) {
					notRoleList.add(roleInfo.get("BIN_RoleID").toString());
				} else {
					isRoleList.add(roleInfo.get("BIN_RoleID").toString());
				}
			}
			if(isRoleList != null && !isRoleList.isEmpty()) {
				map.put("roleList", isRoleList);
				// 查询所有允许菜单List
				List<Map<String, Object>> canList = binOLPLRLA05_Service.getMenuList(map);
				if(canList != null && !canList.isEmpty()) {
					if(notRoleList != null && !notRoleList.isEmpty()) {
						map.put("roleList", notRoleList);
						// 查询所有禁止菜单List
						List<Map<String, Object>> forbidList = binOLPLRLA05_Service.getMenuList(map);
						if(forbidList != null && !forbidList.isEmpty()) {
							// 查询所有菜单的子菜单数
							List<Map<String, Object>> childMenuCountList = binOLPLRLA05_Service.getChildMenuCount();
							// 具有层级的菜单List
							List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
							// 把线性的数据转换成层级的数据
							convertList2DeepListMenu(forbidList, null, menuList);
							// 需要禁止的菜单List
							List<String> forbidMenuList = new ArrayList<String>();
							// 生成需要禁止的菜单List
							createForbidList(menuList, childMenuCountList, forbidMenuList);
							// 从允许操作的菜单中移除禁止的菜单
							Iterator it = canList.iterator();
							while (it.hasNext()) {
								Map<String, Object> canMap = (Map)it.next();
								String menuId = String.valueOf(canMap.get("menuId"));
								if(forbidMenuList.contains(menuId) && !"LG".equals(menuId)) {
									it.remove();
								}
							}
						}
					}
					if(canList != null && !canList.isEmpty()) {
						// 具有层级的菜单List
						List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
						// 把线性的数据转换成层级的数据
						convertList2DeepListMenu(canList, null, menuList);
						// 把允许菜单中除画面外没有子菜单的菜单移除
						filterMenuId(menuList);
						return menuList;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * 把线性的数据转换成层级的数据
	 * 
	 * @param 
	 * 		list 线性数据List
	 * 		menuId 每一层的父节点ID
	 *      resultList 层级数据List
	 */
	public void convertList2DeepListMenu(List<Map<String, Object>> list, String menuId, List<Map<String, Object>> resultList) {
		if(list == null || list.isEmpty()) {
			return;
		}
		// 父节点ID为null时，取菜单类型为1（TOPMENU）的数据作为第一层数据
		if(menuId == null) {
			for (int i = 0; i < list.size(); i++) {
				if("1".equals(list.get(i).get("menuType"))) {
					resultList.add(list.get(i));
					list.remove(i);
					i--;
				}
			}
		} else {
			for (int i = 0; i < list.size(); i++) {
				// 把相同父节点ID的数据作为一组数据
				if(menuId.equals(list.get(i).get("parentMenuId"))) {
					resultList.add(list.get(i));
					list.remove(i);
					i--;
				}
			}
		}
		if(resultList != null && !resultList.isEmpty()) {
			// 菜单类型为4（画面控件）的数据作为最后一层数据
			if(!"4".equals(resultList.get(0).get("menuType"))) {
				for(int i = 0; i < resultList.size(); i++) {
					if(list == null || list.isEmpty()) {
						break;
					}
					String deepMenuId = (String)resultList.get(i).get("menuId");
					List<Map<String, Object>> deepResultList = new ArrayList<Map<String,Object>>();
					resultList.get(i).put("list", deepResultList);
					// 递归取得当前层的下层结构数据
					convertList2DeepListMenu(list,deepMenuId,deepResultList);
				}
			}
		}
	}
	
	/**
	 * 
	 * 生成需要禁止的菜单List
	 * 
	 * @param 
	 * 		list 所有的禁止菜单List（层级结构的）
	 * 		childMenuCountList 所有菜单的子菜单数
	 *      removeList 需要禁止的菜单List
	 */
	@SuppressWarnings("unchecked")
	public void createForbidList(List<Map<String, Object>> list, List<Map<String, Object>> childMenuCountList, List<String> removeList) {
		if(list == null || list.isEmpty()) {
			return;
		}
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String menuId = (String)map.get("menuId");
			List nextList = (List)map.get("list");
			// 不存在子菜单的场合，表示该菜单需要禁止
			if(nextList == null || nextList.isEmpty()) {
				removeList.add(menuId);
			} else {
				String menuType = (String)map.get("menuType");
				if("1".equals(menuType) || "2".equals(menuType)) {
					createForbidList(nextList, childMenuCountList, removeList);
				} else {
					// 根据菜单ID取得该菜单的子菜单数
					int size = getChildMenuCountByMenuId(childMenuCountList, menuId);
					// 子菜单数和禁止菜单的子菜单数一样的场合，表示该菜单需要禁止，否则对子菜单进行递归处理
					if(nextList.size() == size) {
						removeList.add(menuId);
					} else {
						createForbidList(nextList, childMenuCountList, removeList);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 根据菜单ID取得该菜单的子菜单数
	 * 
	 * @param 
	 * 		childMenuCountList 所有菜单的子菜单数
	 * 		menuId 菜单ID
	 * @return
	 * 		子菜单数
	 */
	public int getChildMenuCountByMenuId(List<Map<String, Object>> childMenuCountList, String menuId) {
		
		int count = 0;
		for(Map<String, Object> map : childMenuCountList) {
			String menuIdTemp = (String)map.get("menuId");
			if(menuId.equals(menuIdTemp)) {
				count = (Integer)map.get("count");
				break;
			}
		}
		return count;
	}
	
	/**
	 * 
	 * 把允许菜单中除画面外没有子菜单的菜单移除
	 * 
	 * @param 
	 * 		menuList 允许菜单List
	 */
	@SuppressWarnings("unchecked")
	public void filterMenuId(List<Map<String, Object>> menuList) {
		
		for(int i = 0; i < menuList.size(); i++) {
			Map<String, Object> menuMap = menuList.get(i);
			String menuType = (String)menuMap.get("menuType");
			if("1".equals(menuType) || "2".equals(menuType)) {
				List nextList = (List)menuMap.get("list");
				if(nextList != null && !nextList.isEmpty()) {
					filterMenuId(nextList);
					if(nextList == null || nextList.isEmpty()) {
						menuList.remove(i);
						i--;
					}
				} else {
					menuList.remove(i);
					i--;
				}
			} else {
				return;
			}
		}
	}

}
