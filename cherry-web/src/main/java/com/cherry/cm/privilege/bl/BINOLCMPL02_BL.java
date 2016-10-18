/*
 * @(#)BINBEPLDPL02_BL.java     1.0 2010/11/04
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.privilege.service.BINOLCMPL01_Service;
import com.cherry.cm.privilege.service.BINOLCMPL02_Service;

/**
 * 岗位数据过滤权限共通BL
 * 
 * @author WangCT
 * @version 1.0 2010.11.04
 */
@SuppressWarnings("unchecked")
public class BINOLCMPL02_BL {
	
	/** 部门数据过滤权限共通BL */
	@Resource
	private BINOLCMPL01_BL binOLCMPL01_BL;
	
	/** 部门数据过滤权限共通Service */
	@Resource
	private BINOLCMPL01_Service binOLCMPL01_Service;
	
	/** 岗位数据过滤权限共通Service */
	@Resource
	private BINOLCMPL02_Service binOLCMPL02_Service;
	
	/**
	 * 创建岗位数据过滤权限
	 * 
	 * @return BATCH处理标志
	 * 
	 */
	public void tran_createDataPrivilege(Map<String, Object> map) throws Exception {
		
		// 取得权限配置信息
		Map<String, Object> privilegeConfInfo = binOLCMPL01_BL.getPrivilegeConfInfo();
		if(privilegeConfInfo == null || privilegeConfInfo.isEmpty()) {
			return;
		}
		
		// 数据查询长度
		int dataSize = CherryConstants.DATE_SIZE;
		// 数据抽出次数
		int currentNum = 0;
		// 排序字段
		map.put(CherryConstants.SORT_ID, "employeeId");
		while (true) {
			// 查询开始位置
			int startNum = dataSize * currentNum + 1;
			map.put(CherryConstants.START, startNum);
			// 查询结束位置
			map.put(CherryConstants.END, startNum + dataSize - 1);
			
			// 数据抽出次数累加
			currentNum++;
			// 查询所有的员工信息
			List<Map<String, Object>> employeeList = binOLCMPL01_Service.getEmployeeList(map);
			// 员工信息存在的场合
			if(employeeList != null && employeeList.size() > 0) {
				// 权限生成处理
				this.privilegeHandle(employeeList, privilegeConfInfo, map);
				// 员工数据少于一次抽取的数量，即为最后一页，跳出循环
				if(employeeList.size() < dataSize) {
					break;
				}
			} else {
				break;
			}
		}
		
				
//		// 查询管理员帐号
//		List<Map<String, Object>> adminIdList = binOLCMPL01_Service.getAdminIdList();
//		if(adminIdList != null && !adminIdList.isEmpty()) {
//			for(int i = 0; i < adminIdList.size(); i++) {
//				Map<String, Object> adminIdMap = adminIdList.get(i);
//				// 删除岗位数据权限
//				binOLCMPL02_Service.deletePositionPrivilege(adminIdMap);
//				Map<String, Object> sMap = new HashMap<String, Object>();
//				sMap.put("organizationInfoId", adminIdMap.get("organizationInfoId"));
//				// 查询指定组织的所有岗位
//				List<Map<String, Object>> positionList = binOLCMPL02_Service.getPositionList(sMap);
//				if(positionList != null && !positionList.isEmpty()) {
//					List<Map<String, Object>> positionList1 = new ArrayList<Map<String,Object>>();
//					List<Map<String, Object>> positionList2 = new ArrayList<Map<String,Object>>();
//					for(int n = 0; n < positionList.size(); n++) {
//						Map<String, Object> positionMap1 = new HashMap<String, Object>();
//						Map<String, Object> positionMap2 = new HashMap<String, Object>();
//						positionMap1.putAll(map);
//						// 岗位ID
//						positionMap1.put("employeeId", positionList.get(n).get("employeeId"));
//						// 岗位类别
//						positionMap1.put("positionCategoryId", positionList.get(n).get("positionCategoryId"));
//						// 用户ID
//						positionMap1.put("userId", adminIdMap.get("userId").toString());
//						// 业务类型
//						positionMap1.put("businessType", "A");
//						positionMap2.putAll(positionMap1);
//						// 操作类型
//						positionMap1.put("operationType", "0");
//						positionMap2.put("operationType", "1");
//						positionList1.add(positionMap1);
//						positionList2.add(positionMap2);
//					}
//					// 添加岗位数据过滤权限
//					binOLCMPL02_Service.addPositionPrivilege(positionList1);
//					binOLCMPL02_Service.addPositionPrivilege(positionList2);
//				}
//			}
//		}
		
	}
	
	/**
	 * 
	 * 权限生成处理
	 * 
	 * @param employeeList 需要生成权限的员工List
	 * @param privilegeConfInfo 权限配置信息
	 * @param map 包括作成者、作成程序名、更新者、更新程序名这些参数
	 * @throws Exception 
	 * 
	 */
	public void privilegeHandle(List<Map<String, Object>> employeeList, Map<String, Object> privilegeConfInfo, Map<String, Object> map) throws Exception {
		
		for(int i = 0; i < employeeList.size(); i++) {
			Map<String, Object> employeeMap = employeeList.get(i);
			// 用户ID
			Object userId = employeeMap.get("userId");
			// 雇员ID
			Object employeeId = employeeMap.get("employeeId");
			// 取得员工的权限配置信息
			List<Map<String, Object>> empPlConfInfoList = binOLCMPL01_BL.getEmpPlConfInfo(privilegeConfInfo, employeeMap);
			if(empPlConfInfoList != null && !empPlConfInfoList.isEmpty()) {
				// 临时保存权限对象
				Map<Integer, Object> privilegeMap = new HashMap<Integer, Object>();
				// 删除岗位数据过滤权限
				binOLCMPL02_Service.deletePositionPrivilege(employeeMap);
				for(int j = 0; j < empPlConfInfoList.size(); j++) {
					Map<String, Object> empPlConfInfoMap = empPlConfInfoList.get(j);
					// 业务类型
					String businessType = (String)empPlConfInfoMap.get("businessType");
					// 操作类型
					String operationType = (String)empPlConfInfoMap.get("operationType");
					// 权限类型
					int privilegeType = (Integer)empPlConfInfoMap.get("privilegeType");	
					// 用户拥有的所有权限
					List<Map<String, Object>> privilegeList;
					// 权限已经存在临时对象中
					if(privilegeMap.containsKey(privilegeType)) {
						privilegeList = (List)privilegeMap.get(privilegeType);
					} else {
						Map<String, Object> paramMap = new HashMap<String, Object>();
						// 雇员ID
						paramMap.put("employeeId", employeeId);
						// 权限类型
						paramMap.put("privilegeType", privilegeType);
						// 查询该用户的所有岗位权限
						privilegeList = getPositionIDList(paramMap);
						// 把岗位权限储存在临时对象中
						privilegeMap.put(privilegeType, privilegeList);
					}
					if(privilegeList != null && !privilegeList.isEmpty()) {
						for(int n = 0; n < privilegeList.size(); n++) {
							Map<String, Object> dataPrivilegeMap = privilegeList.get(n);
							// 作成者
							dataPrivilegeMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
							// 作成程序名
							dataPrivilegeMap.put(CherryConstants.CREATEPGM, map.get(CherryConstants.CREATEPGM));
							// 更新者
							dataPrivilegeMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
							// 更新程序名
							dataPrivilegeMap.put(CherryConstants.UPDATEPGM, map.get(CherryConstants.UPDATEPGM));
							// 雇员ID
							dataPrivilegeMap.put("employeeId", employeeId);
							if(userId != null) {
								// 用户ID
								dataPrivilegeMap.put("userId", userId);
							}
							// 业务类型
							dataPrivilegeMap.put("businessType", businessType);
							// 操作类型
							dataPrivilegeMap.put("operationType", operationType);
						}
						// 添加岗位数据过滤权限
						binOLCMPL02_Service.addPositionPrivilege(privilegeList);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * 查询某一用户管辖的所有岗位ID
	 * 
	 * @param map 查询条件
	 * 
	 */
	public List<Map<String, Object>> getPositionIDList(Map<String, Object> map) {
		
		if(map.get("privilegeType") == null) 
			return null;
		// 权限类型
		int privilegeType = (Integer)map.get("privilegeType");
		if(privilegeType == 0) {
			// 查询某一用户管辖的所有岗位ID(权限类型为0时)
			return binOLCMPL02_Service.getPositionID0List(map);
		} else if(privilegeType == 1) {
			// 查询某一用户管辖的所有岗位ID(权限类型为1时)
			return binOLCMPL02_Service.getPositionID1List(map);
		} else if(privilegeType == 2) {
			// 查询某一用户管辖的所有岗位ID(权限类型为2时)
			return binOLCMPL02_Service.getPositionID2List(map);
		} else {
			return null;
		}
	}

}
