/*
 * @(#)BINOLBSDEP07_BL.java     1.0 2011.2.10
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

package com.cherry.bs.dep.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.bs.dep.service.BINOLBSDEP91_Service;
import com.cherry.bs.emp.service.BINOLBSEMP04_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;
import com.cherry.cm.util.DateUtil;
import com.cherry.pl.common.PrivilegeConstants;

/**
 * 组织添加画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP07_BL {
	
	/** 组织信息管理共通Service */
	@Resource
	private BINOLBSDEP91_Service binOLBSDEP91_Service;
	
	/** 添加部门画面Service */
	@Resource
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	@Resource
	private BINOLBSEMP04_Service binolbsemp04Service;
	
	/**
	 * 添加组织
	 * 
	 * @param map 添加内容
	 */
	public void tran_addOrganization(Map<String, Object> map) {
		
		// 取得系统时间
		String sysDate = binOLBSDEP91_Service.getSYSDate();
		// 系统时间设定
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 添加组织
		int organizationInfoId = binOLBSDEP91_Service.addOrganization(map);
		// 组织ID
		map.put("organizationInfoId", organizationInfoId);
		
		// 添加部门内容设定map
		Map<String, Object> orgMap = new HashMap<String, Object>();
		orgMap.put("path", CherryConstants.DUMMY_VALUE);
		// 取得新部门节点
		String newNodeId = binOLBSDEP04_Service.getNewNodeId(orgMap);
		orgMap.putAll(map);
		orgMap.put("newNodeId", newNodeId);
		// 所属品牌
		orgMap.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
		// 部门代码
		if(map.get("orgCode") != null && !"".equals(map.get("orgCode"))) {
			orgMap.put("departCode", map.get("orgCode"));
		}
		// 部门名称
		if(map.get("orgNameChinese") != null && !"".equals(map.get("orgNameChinese"))) {
			orgMap.put("departName", map.get("orgNameChinese"));
		}
		// 部门简称
		if(map.get("orgNameShort") != null && !"".equals(map.get("orgNameShort"))) {
			orgMap.put("departNameShort", map.get("orgNameShort"));
		}
		// 部门英文名称
		if(map.get("orgNameForeign") != null && !"".equals(map.get("orgNameForeign"))) {
			orgMap.put("nameForeign", map.get("orgNameForeign"));
		}
		// 部门英文简称
		if(map.get("orgNameForeignShort") != null && !"".equals(map.get("orgNameForeignShort"))) {
			orgMap.put("nameShortForeign", map.get("orgNameForeignShort"));
		}
		// 部门类型
		orgMap.put("type", "0");
		// 测试区分
		orgMap.put("testType", "0");
		// 添加部门
		binOLBSDEP04_Service.addOrganization(orgMap);
		
//		// 添加岗位内容设定map
//		Map<String, Object> posMap = new HashMap<String, Object>();
//		posMap.put("path", CherryConstants.DUMMY_VALUE);
//		// 取得岗位新节点
//		String newPosNodeId = binOLBSDEP04_Service.getNewPosNodeId(posMap);
//		posMap.putAll(map);
//		posMap.put("newNodeId", newPosNodeId);
//		posMap.put("organizationId", organizationId);
//		// 添加岗位
//		int postionId = binOLBSDEP04_Service.addPosition(posMap);
//		
//		posMap.put("seniorPost", postionId);
//		// 更新部门的直属上级岗位
//		binOLBSDEP04_Service.updateOrganization(posMap);
		
		// 添加管理员
		int userId = binOLBSDEP91_Service.addUser(map);
		// 管理员ID
		map.put("userId", userId);
		// 系统角色
		map.put("roleId", "2");
		// 开始日期
		map.put("startDate", binOLBSDEP91_Service.getSYSDate().substring(0,10));
		// 有效期限
		map.put("expireDate", DateUtil.coverString2Date(PrivilegeConstants.EXPIRE_DATE_VALUE));
		// 权限区分
		map.put("privilegeFlag", "1");
		// 添加管理员角色
		binOLBSDEP91_Service.addUserRole(map);
		map.put("dataBaseName", CustomerContextHolder.getCustomerDataSourceType());
		// 登录帐号到配置数据库
		binolbsemp04Service.insertUserConf(map);
	}
	
	/**
	 * 判断组织代码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 组织ID
	 */
	public String getOrgIdByOrgCode(Map<String, Object> map) {
		
		// 判断组织代码是否已经存在
		return binOLBSDEP91_Service.getOrgIdByOrgCode(map);
	}
	
	/**
	 * 判断组织名称是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 组织ID
	 */
	public String getOrganizationInfoID(Map<String, Object> map) {
		
		// 判断组织名称是否已经存在
		return binOLBSDEP91_Service.getOrganizationInfoID(map);
	}

}
