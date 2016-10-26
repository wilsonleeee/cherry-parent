/*
 * @(#)BINOLPLRLM03_BL.java     1.0 2010/10/27
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

package com.cherry.pl.rlm.bl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.pl.rlm.service.BINOLPLRLM99_Service;

/**
 * 角色授权BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
public class BINOLPLRLM03_BL {
	
	/** 角色管理Service */
	@Resource(name="binOLPLRLM99_Service")
	private BINOLPLRLM99_Service binOLPLRLM99_Service;
	
	/**
	 * 取得所有的功能权限
	 * 
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getResourceList(Map<String, Object> map) {
		List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
		// 取得所有的功能权限
		List<Map<String, Object>> list = binOLPLRLM99_Service.getMenuList(map);
		List<Object> notRoleList = (List)map.get("notRoleList");
		// 禁止权限存在的场合，去除禁止权限处理
		if(notRoleList != null && !notRoleList.isEmpty()) {
			// 查询所有的禁止菜单资源
			List<Map<String, Object>> removeList = binOLPLRLM99_Service.getNotMenuList(map);
			if(removeList != null && !removeList.isEmpty()) {
				// 查询所有菜单的子菜单数
				List<Map<String, Object>> childMenuCountList = binOLPLRLM99_Service.getChildMenuCount();
				// 具有层级的菜单List
				List<Map<String, Object>> menuList = new ArrayList<Map<String, Object>>();
				// 把线性的数据转换成层级的数据
				convertList2DeepListMenu(removeList, null, menuList);
				// 需要禁止的菜单List
				List<String> forbidMenuList = new ArrayList<String>();
				// 生成需要禁止的菜单List
				createForbidList(menuList, childMenuCountList, forbidMenuList);
				// 从允许操作的菜单中移除禁止的菜单
				Iterator it = list.iterator();
				while (it.hasNext()) {
					Map<String, Object> canMap = (Map)it.next();
					String menuId = String.valueOf(canMap.get("menuId"));
					if(forbidMenuList.contains(menuId) && !"LG".equals(menuId)) {
						it.remove();
					}
				}
			}
		}
//		// 取得指定角色的所有授权List
//		List<Map<String, Object>> roleResourceList = binOLPLRLM99_Service.getRoleResourceList(map);
//		List<String[]> valueList = new ArrayList<String[]>();
//		if(roleResourceList != null && !roleResourceList.isEmpty()) {
//			List<String> subSysIdList = new ArrayList<String>();
//			List<String> moduleIdList = new ArrayList<String>();
//			List<String> functionIdList = new ArrayList<String>();
//			List<String> pageIdList = new ArrayList<String>();
//			List<String> controlIdList = new ArrayList<String>();
//			for(int i = 0; i < roleResourceList.size(); i++) {
//				Map<String, Object> roleResourceMap = roleResourceList.get(i);
//				String resourceType = (String)roleResourceMap.get(PrivilegeConstants.RESOURCE_TYPE);
//				// 资源类型为子系统的场合
//				if(PrivilegeConstants.RESOURCE_TYPE_0.equals(resourceType)) {
//					subSysIdList.add((String)roleResourceMap.get(PrivilegeConstants.SUB_SYS_ID));
//				}
//				// 资源类型为模块的场合
//				else if(PrivilegeConstants.RESOURCE_TYPE_1.equals(resourceType)) {
//					moduleIdList.add((String)roleResourceMap.get(PrivilegeConstants.MODULE_ID));
//				}
//				// 资源类型为功能的场合
//				else if(PrivilegeConstants.RESOURCE_TYPE_2.equals(resourceType)) {
//					functionIdList.add((String)roleResourceMap.get(PrivilegeConstants.FUNCTION_ID));
//				}
//				// 资源类型为画面的场合
//				else if(PrivilegeConstants.RESOURCE_TYPE_3.equals(resourceType)) {
//					pageIdList.add((String)roleResourceMap.get(PrivilegeConstants.PAGE_ID));
//				}
//				// 资源类型为画面的场合
//				else if(PrivilegeConstants.RESOURCE_TYPE_4.equals(resourceType)) {
//					controlIdList.add((String)roleResourceMap.get(PrivilegeConstants.CONTROL_ID));
//				}
//			}
//			valueList.add(subSysIdList.toArray(new String[]{}));
//			valueList.add(moduleIdList.toArray(new String[]{}));
//			valueList.add(functionIdList.toArray(new String[]{}));
//			valueList.add(pageIdList.toArray(new String[]{}));
//			valueList.add(controlIdList.toArray(new String[]{}));
//		}
		// 把功能权限List做成画面表示用的有层次的List（按子系统、模块、功能、画面、控件这个顺序分五层表示）
//		if(list != null && !list.isEmpty()) {
//			List<String[]> keyList = new ArrayList<String[]>();
//			String[] key1 = {PrivilegeConstants.SUB_SYS_ID,PrivilegeConstants.SUB_SYS_NAME,PrivilegeConstants.RESOURCE_ID};
//			String[] key2 = {PrivilegeConstants.MODULE_ID,PrivilegeConstants.MODULE_NAME,PrivilegeConstants.RESOURCE_ID};
//			String[] key3 = {PrivilegeConstants.FUNCTION_ID,PrivilegeConstants.FUNCTION_NAME,PrivilegeConstants.RESOURCE_ID};
//			String[] key4 = {PrivilegeConstants.PAGE_ID,PrivilegeConstants.PAGE_NAME,PrivilegeConstants.RESOURCE_ID};
//			String[] key5 = {PrivilegeConstants.CONTROL_ID,PrivilegeConstants.CONTROL_NAME,PrivilegeConstants.RESOURCE_ID};
//			keyList.add(key1);
//			keyList.add(key2);
//			keyList.add(key3);
//			keyList.add(key4);
//			keyList.add(key5);
//			String[] key = {PrivilegeConstants.RESOURCE_ID};
//			convertList2DeepListPri(list, resultList, keyList, 0, key);
//		}
		// 把线性的数据转换成层级的数据
		convertList2DeepListMenu(list,null,resultList);
		// 把允许菜单中除画面外没有子菜单的菜单移除
		filterMenuId(resultList);
		return resultList;
	}
	
//	/**
//	 * 取得画面对应的控件资源List
//	 * 
//	 * @return List
//	 */
//	public List<Map<String, Object>> getPageControlList(Map<String, Object> map) {
//		
//		// 取得画面对应的控件资源List
//		return binOLPLRLM99_Service.getPageControlList(map);
//	}
//	
//	/**
//	 * 取得角色对应的某个画面的控件资源List
//	 * 
//	 * @return List
//	 */
//	public List<Map<String, Object>> getRoleControlList(Map<String, Object> map) {
//		
//		// 取得角色对应的某个画面的控件资源List
//		return binOLPLRLM99_Service.getRoleControlList(map);
//	}
	
	/**
	 * 角色授权处理
	 * 
	 * @param Map 
	 */
	public void tran_RoleAuthorize(Map<String, Object> map) {
		
		// 删除角色已有的的功能资源
		binOLPLRLM99_Service.deleteRoleResource(map);
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 菜单资源ID
		String[] menuId = (String[])map.get("menuId");
		if(menuId != null && menuId.length > 0) {
			for(String s : menuId) {
				Map<String, Object> resource = new HashMap<String, Object>();
				resource.putAll(map);
				// 菜单资源ID
				resource.put("menuId", s);
				list.add(resource);
			}
			// 保存角色功能资源
			binOLPLRLM99_Service.addRoleResource(list);
		}
//		// 子系统ID
//		String[] subSysId = (String[])map.get(PrivilegeConstants.SUB_SYS_ID);
//		// 模块ID
//		String[] moduleId = (String[])map.get(PrivilegeConstants.MODULE_ID);
//		// 功能ID
//		String[] functionId = (String[])map.get(PrivilegeConstants.FUNCTION_ID);
//		// 画面ID
//		String[] pageId = (String[])map.get(PrivilegeConstants.PAGE_ID);
//		// 控件ID
//		String[] controlId = (String[])map.get(PrivilegeConstants.CONTROL_ID);
//		// 子系统相关资源参数设置
//		setParam(subSysId, list, PrivilegeConstants.RESOURCE_TYPE_0, map);
//		// 模块相关资源参数设置
//		setParam(moduleId, list, PrivilegeConstants.RESOURCE_TYPE_1, map);
//		// 功能相关资源参数设置
//		setParam(functionId, list, PrivilegeConstants.RESOURCE_TYPE_2, map);
//		// 画面相关资源参数设置
//		setParam(pageId, list, PrivilegeConstants.RESOURCE_TYPE_3, map);
//		// 控件相关资源参数设置
//		setParam(controlId, list, PrivilegeConstants.RESOURCE_TYPE_4, map);
	}
	
//	/**
//	 * 相关资源参数设置
//	 * 
//	 * @param 资源ID数组
//	 * @param 要保存的功能资源List
//	 * @param 角色ID
//	 * @param 资源类别
//	 */
//	public void setParam(String[] array, List<Map<String, Object>> list, String resourceType, Map<String, Object> map) {
//		if(array != null && array.length > 0) {
//			for(String s : array) {
//				Map<String, Object> resource = new HashMap<String, Object>();
//				resource.putAll(map);
//				// 资源ID
//				resource.put(PrivilegeConstants.RESOURCE_ID, s);
//				// 资源类别
//				resource.put(PrivilegeConstants.RESOURCE_TYPE, resourceType);
//				list.add(resource);
//			}
//		}
//	}
	
//	/**
//	 * <p>
//	 * 层级List转换(支持多层转换)
//	 * </p>
//	 * 
//	 * @param List
//	 *          	转换前List
//	 * @param rootList
//	 *          	转换后List
//	 * @param list
//	 *          	多层key
//	 * @param list
//	 *          	多层值
//	 * @param int 
//	 *				层级
//	 * @param String[] 
//	 * 				保留的key
//	 * 
//	 * @return 无
//	 * 
//	 * @throws 无
//	 */
//	@SuppressWarnings("unchecked")
//	public void convertList2DeepListPri(List<Map<String, Object>> list,
//			List<Map<String, Object>> rootList, List<String[]> keysList, int deep, String[] keepKeys) {
//		if (list != null && rootList != null && keysList != null
//				&& keysList.size() > deep) {
//			String[] keys = keysList.get(deep);
//			String id = null;
//			if (keys != null && keys.length > 0) {
//				id = keys[0];
//			}
//			for (Map<String, Object> map : list) {
//				// 判断是否已经存在了
//				boolean isNotEqual = true;
//				for (Map<String, Object> rootMap : rootList) {
//					// 外层List的id
//					Object value = rootMap.get(id);
//					/* id相等的时候说明已经存在 */
//					if (value != null && value.equals(map.get(id))) {
//						// 更新外层List已存在的map
//						updateRootListPri(2, map, rootMap, "list", id,
//								keepKeys, keys);
//						if(map.get("resourceType") != null && Integer.parseInt(map.get("resourceType").toString()) == deep) {
//							rootMap.put("isChecked", true);
//						}
//						isNotEqual = false;
//						break;
//					}
//				}
//				if (isNotEqual) {
//					Map<String, Object> rootMap = new HashMap<String, Object>();
//					rootList.add(rootMap);
//					// 往外层List添加map
//					updateRootListPri(1, map, rootMap, "list", id,
//							keepKeys, keys);
//					if(map.get("resourceType") != null && Integer.parseInt(map.get("resourceType").toString()) == deep) {
//						rootMap.put("isChecked", true);
//					}
//				}
//			}
//			if (deep < keysList.size() - 1) {
//				deep++;
//				for (Map<String, Object> rootMap : rootList) {
//					List<Map<String, Object>> deepList = (List<Map<String, Object>>) rootMap
//							.get("list");
//					List<Map<String, Object>> deepListNew = new ArrayList<Map<String, Object>>();
//					rootMap.put("list", deepListNew);
//					convertList2DeepListPri(deepList, deepListNew, keysList, deep, keepKeys);
//				}
//			} else {
//				for (Map<String, Object> rootMap : rootList) {
//					rootMap.remove("list");
//				}
//			}
//		}
//	}
//
//	/**
//	 * <p>
//	 * 更新外层List
//	 * </p>
//	 * 
//	 * @param int 
//	 * 				更新区分
//	 * @param Map
//	 *            	读取的map
//	 * @param String
//	 *            	外层List的map
//	 * @param String
//	 *            	里面一层List的名字
//	 * @param String
//	 *            	外层List的id属性
//	 * @param String
//	 *            	id的值
//	 * @param String[] 
//	 * 				保留的key         
//	 * @param String[] 
//	 * 				外层List其它的属性
//	 * @return 无
//	 * 
//	 * @throws 无
//	 */
//	@SuppressWarnings("unchecked")
//	private void updateRootListPri(int updateFlg,
//			Map<String, Object> map, Map<String, Object> rootMap,
//			String depName, String id, String[] keepKeys,
//			String[] keys) {
//		// 里层List
//		List<Map<String, Object>> depList = new ArrayList<Map<String, Object>>();
//		if (map != null && rootMap != null) {
//			Map<String, Object> tempMap = new HashMap<String, Object>();
//			tempMap.putAll(map);
//			if (updateFlg == 1) {
//				Object idVal = tempMap.get(id);
//				// 外层List的id属性
//				rootMap.put(id, idVal);
//				tempMap.remove(id);
//				// 往外层List中添加里层List属性
//				rootMap.put(depName, depList);
//			} else {
//				depList = (List<Map<String, Object>>) rootMap.get(depName);
//				tempMap.remove(id);
//			}
//			if (keys != null) {
//				/* 往外层List中添加其它属性 */
//				for (String key : keys) {
//					if (id != null && !id.equals(key)) {
//						if (updateFlg == 1) {
//							rootMap.put(key, tempMap.get(key));
//						}
//						if (!ConvertUtil.isContain(keepKeys, key)) {
//							tempMap.remove(key);
//						}
//					}
//				}
//			}
//			Map<String, Object> depMap = new HashMap<String, Object>();
//			depMap.putAll(tempMap);
//			if (depList != null) {
//				depList.add(depMap);
//			}
//		}
//	}
	
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
			// 菜单类型为3（画面控件）的数据作为最后一层数据
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
	
	/**
	 * 
	 * 去除禁止权限处理
	 * 
	 * @param 
	 * 		list 菜单资源List
	 * 		removeList 禁止权限List
	 */
	@SuppressWarnings("unchecked")
	public void removeDataFromList(List<Map<String, Object>> list, List<String> removeList) {
		if(list == null || list.isEmpty()) {
			return;
		}
		for(int i = 0; i < list.size(); i++) {
			Map<String, Object> map = list.get(i);
			String menuId = (String)map.get("menuId");
			if(removeList.contains(menuId)) {
				List nextList = (List)map.get("list");
				if(nextList != null && !nextList.isEmpty()) {
					removeDataFromList(nextList, removeList);
					if(nextList == null || nextList.isEmpty()) {
						list.remove(i);
						i--;
					}
				} else {
					list.remove(i);
					i--;
				}
			}
			
		}
	}

}
