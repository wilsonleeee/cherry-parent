/*
 * @(#)BINOLCMPL03_BL.java     1.0 2011.11.02
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
package com.cherry.cm.privilege.bl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.privilege.service.BINOLCMPL03_Service;

/**
 * 柜台主管变更处理共通BL
 * 
 * @author WangCT
 * @version 1.0 2011.11.02
 */
public class BINOLCMPL03_BL {
	
	/** 柜台主管变更处理共通Service */
	@Resource
	private BINOLCMPL03_Service binOLCMPL03_Service;
	
//	/** 部门数据过滤权限共通BL */
//	@Resource
//	private BINOLCMPL01_BL binOLCMPL01_BL;
//	
//	/** 岗位数据过滤权限共通BL */
//	@Resource
//	private BINOLCMPL02_BL binOLCMPL02_BL;
	
	/** 发送实时刷新数据权限MQ消息BL */
	@Resource
	private BINOLCMPL04_BL binOLCMPL04_BL;
	
	/**
	 * 柜台主管变更处理
	 * 
	 * @param map 包括组织ID、品牌ID、部门ID、员工ID
	 */
	public void updateCounterHeader(Map<String, Object> map) throws Exception {
		
		// 原柜台主管
		Object counterHeader = map.get("oldEmployeeId");
		// 新柜台主管
		Object employeeId = map.get("employeeId");
		if(counterHeader != null && !"".equals(counterHeader)) {
			// 删除柜台主管
			binOLCMPL03_Service.deleteCounterHeader(map);
		}
		if(employeeId != null && !"".equals(employeeId.toString())) {
			// 作成者
			map.put(CherryConstants.CREATEDBY, "BINOLCMPL03");
			// 作成程序名
			map.put(CherryConstants.CREATEPGM, "BINOLCMPL03");
			// 更新者
			map.put(CherryConstants.UPDATEDBY, "BINOLCMPL03");
			// 更新程序名
			map.put(CherryConstants.UPDATEPGM, "BINOLCMPL03");
			// 部门类型
			map.put("departType", "4");
			// 管理类型
			map.put("manageType", "1");
			// 添加柜台主管
			binOLCMPL03_Service.insertCounterHeader(map);
		}
		
		
//		// 柜台主管是否变化
//		boolean isChange = false;
//		if(employeeId != null && !"".equals(employeeId.toString())) {
//			if(counterHeader != null && counterHeader.equals(employeeId.toString())) {
//				isChange = false;
//			} else {
//				isChange = true;
//			}
//		} else {
//			if(counterHeader != null && !"".equals(counterHeader)) {
//				isChange = true;
//			}
//		}
//		// 柜台主管有变化的场合，做更新柜台主管处理，同时刷新相关员工的数据权限
//		if(isChange) {
//			if(counterHeader != null && !"".equals(counterHeader)) {
//				// 删除柜台主管
//				binOLCMPL03_Service.deleteCounterHeader(map);
//			}
//			if(employeeId != null && !"".equals(employeeId.toString())) {
//				// 作成者
//				map.put(CherryConstants.CREATEDBY, "BINOLCMPL03");
//				// 作成程序名
//				map.put(CherryConstants.CREATEPGM, "BINOLCMPL03");
//				// 更新者
//				map.put(CherryConstants.UPDATEDBY, "BINOLCMPL03");
//				// 更新程序名
//				map.put(CherryConstants.UPDATEPGM, "BINOLCMPL03");
//				// 部门类型
//				map.put("departType", "4");
//				// 管理类型
//				map.put("manageType", "1");
//				// 添加柜台主管
//				binOLCMPL03_Service.insertCounterHeader(map);
//			}
//		}
	}
	
//	/**
//	 * 刷新数据权限处理
//	 * 
//	 * @param map
//	 */
//	@SuppressWarnings("unchecked")
//	public void updatePrivilege(Map<String, Object> map) throws Exception {
//		
//		// 作成者
//		map.put(CherryConstants.CREATEDBY, "BINOLCMPL03");
//		// 作成程序名
//		map.put(CherryConstants.CREATEPGM, "BINOLCMPL03");
//		// 更新者
//		map.put(CherryConstants.UPDATEDBY, "BINOLCMPL03");
//		// 更新程序名
//		map.put(CherryConstants.UPDATEPGM, "BINOLCMPL03");
//		Map<String, Object> privilegeConfInfo = null;
//		// 需要刷新人员数据权限的员工List
//		List empPrivilegeList = (List)map.get("empPrivilegeList");
//		if(empPrivilegeList != null && !empPrivilegeList.isEmpty()) {
//			// 取得权限配置信息
//			privilegeConfInfo = binOLCMPL01_BL.getPrivilegeConfInfo();
//			if(privilegeConfInfo == null || privilegeConfInfo.isEmpty()) {
//				return;
//			}
//			map.put("employeeList", empPrivilegeList);
//			// 查询需要更新人员数据权限的员工
//			List<Map<String, Object>> employeeMapList = binOLCMPL03_Service.getEmployeeList(map);
//			// 人员权限生成处理
//			binOLCMPL02_BL.privilegeHandle(employeeMapList, privilegeConfInfo, map);
//		}
//		// 需要刷新部门数据权限的人员List
//		List depPrivilegeList = (List)map.get("depPrivilegeList");
//		List<Map<String, Object>> employeeMapList = new ArrayList<Map<String,Object>>();
//		if(depPrivilegeList != null && !depPrivilegeList.isEmpty()) {
//			map.put("employeeList", depPrivilegeList);
//			// 查询需要更新部门数据权限的员工
//			employeeMapList = binOLCMPL03_Service.getEmployeeList(map);
//		}
//		// 柜台上级部门变更的场合，取得变更前后的上级部门List
//		List higherOrgList = (List)map.get("higherOrgList");
//		if(higherOrgList != null && !higherOrgList.isEmpty()) {
//			// 查询原柜台上级和新柜台上级的所有上级部门
//			List<Map<String, Object>> allHigherOrgList = binOLCMPL03_Service.getHigherOrgList(map);
//			if(allHigherOrgList != null && !allHigherOrgList.isEmpty()) {
//				// 创建非柜台部门从属关系权限
//				binOLCMPL01_BL.createDepartRelation(allHigherOrgList, map);
//			}
//			// 查询部门结构变化引起部门权限变化的人员
//			List<Map<String, Object>> employeeListByOrg = binOLCMPL03_Service.getEmployeeListByOrg(map);
//			if(employeeListByOrg != null && !employeeListByOrg.isEmpty()) {
//				if(employeeMapList != null && !employeeMapList.isEmpty()) {
//					for(int i = 0; i < employeeListByOrg.size(); i++) {
//						String employeeId = employeeListByOrg.get(i).get("employeeId").toString();
//						boolean flag = true;
//						for(int j = 0; j < employeeMapList.size(); j++) {
//							String employeeIdTemp = employeeMapList.get(j).get("employeeId").toString();
//							if(employeeId.equals(employeeIdTemp)) {
//								flag = false;
//								break;
//							}
//						}
//						if(flag) {
//							employeeMapList.add(employeeListByOrg.get(i));
//						}
//					}
//				} else {
//					employeeMapList = new ArrayList<Map<String,Object>>();
//					employeeMapList.addAll(employeeListByOrg);
//				}
//			}
//		}
//		if(employeeMapList != null && !employeeMapList.isEmpty()) {
//			if(privilegeConfInfo == null) {
//				// 取得权限配置信息
//				privilegeConfInfo = binOLCMPL01_BL.getPrivilegeConfInfo();
//				if(privilegeConfInfo == null || privilegeConfInfo.isEmpty()) {
//					return;
//				}
//			}
//			// 部门权限生成处理
//			binOLCMPL01_BL.privilegeHandle(employeeMapList, privilegeConfInfo, map);
//		}
//	}
	
	/**
	 * 刷新数据权限处理
	 * 
	 * @param map
	 */
	public void updatePrivilege(Map<String, Object> map) throws Exception {
		
		// 是否需要刷新数据权限
		boolean isReDataPl = false;
		// 需要刷新人员数据权限的员工List
		List empPrivilegeList = (List)map.get("empPrivilegeList");
		// 需要刷新人员数据权限的场合
		if(empPrivilegeList != null && !empPrivilegeList.isEmpty()) {
			map.put("isReEmpPl", "1");
			isReDataPl = true;
		}
		// 需要刷新部门数据权限的人员List
		List depPrivilegeList = (List)map.get("depPrivilegeList");
		// 柜台上级部门变更的场合，取得变更前后的上级部门List
		List higherOrgList = (List)map.get("higherOrgList");
		// 需要刷新部门数据权限的场合
		if((depPrivilegeList != null && !depPrivilegeList.isEmpty()) 
				|| (higherOrgList != null && !higherOrgList.isEmpty())) {
			map.put("isReOrgPl", "1");
			isReDataPl = true;
		}
		// 部门结构发生变化的场合需要刷新部门从属权限
		if(higherOrgList != null && !higherOrgList.isEmpty()) {
			map.put("isReOrgRelPl", "1");
			isReDataPl = true;
		}
		if(isReDataPl) {
			// 发送实时刷新数据权限MQ消息
			binOLCMPL04_BL.sendRefreshPlMsg(map);
		}
	}


}
