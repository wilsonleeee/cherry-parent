/*
 * @(#)BINOLBSDEP03_BL.java     1.0 2010/10/27
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
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.cnt.service.BINOLBSCNT04_Service;
import com.cherry.bs.dep.service.BINOLBSDEP03_Service;
import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cm.util.DateUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 
 * 更新部门画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP03_BL {
	
	/** 更新部门画面Service */
	@Resource
	private BINOLBSDEP03_Service binOLBSDEP03_Service;
	
	/** 添加部门画面Service */
	@Resource
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 创建柜台画面Service */
	@Resource
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	/**
	 * 
	 * 更新部门信息
	 * 
	 * @param map 更新条件
	 */
	public void tran_updateOrganizationInfo(Map<String, Object> map) throws Exception {
		
		// 更新部门内容设定map
		Map<String, Object> orgMap = new HashMap<String, Object>();
		// 部门ID
		orgMap.put("organizationId", map.get("organizationId"));
		// 更新日时
		orgMap.put("modifyTime", map.get("modifyTime"));
		// 更新次数
		orgMap.put("modifyCount", map.get("modifyCount"));
		// 取得系统时间
		String sysDate = binOLBSDEP03_Service.getSYSDateTime();
		// 更新时间设定
		orgMap.put(CherryConstants.UPDATE_TIME, sysDate);
		// 更新者
		orgMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		orgMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP03");
		// 部门代码
		orgMap.put("departCode", map.get("departCode"));
		// 部门名称
		if(map.get("departName") != null && !"".equals(map.get("departName"))) {
			orgMap.put("departName", map.get("departName"));
		}
		// 部门简称
		if(map.get("departNameShort") != null && !"".equals(map.get("departNameShort"))) {
			orgMap.put("departNameShort", map.get("departNameShort"));
		}
		// 部门英文名称
		if(map.get("nameForeign") != null && !"".equals(map.get("nameForeign"))) {
			orgMap.put("nameForeign", map.get("nameForeign"));
		}
		// 部门英文简称
		if(map.get("nameShortForeign") != null && !"".equals(map.get("nameShortForeign"))) {
			orgMap.put("nameShortForeign", map.get("nameShortForeign"));
		}
		// 部门类型
		if(map.get("type") != null && !"".equals(map.get("type"))) {
			orgMap.put("type", map.get("type"));
		}
		// 状态
		if(map.get("status") != null && !"".equals(map.get("status"))) {
			orgMap.put("status", map.get("status"));
		}
		// 测试区分
		if(map.get("testType") != null && !"".equals(map.get("testType"))) {
			orgMap.put("testType", map.get("testType"));
		}
		// 所属区域
		if(map.get("regionId") != null && !"".equals(map.get("regionId"))) {
			orgMap.put("regionId", map.get("regionId"));
		}
		// 所属省份
		if(map.get("provinceId") != null && !"".equals(map.get("provinceId"))) {
			orgMap.put("provinceId", map.get("provinceId"));
		}
		// 所属城市
		if(map.get("cityId") != null && !"".equals(map.get("cityId"))) {
			orgMap.put("cityId", map.get("cityId"));
		}
		// 所属县级市
		if(map.get("countyId") != null && !"".equals(map.get("countyId"))) {
			orgMap.put("countyId", map.get("countyId"));
		}
		// 到期日
		orgMap.put("expiringDate", map.get("expiringDate"));
		
		// 部门协同区分
		if(map.get("orgSynergyFlag") != null && !"".equals(map.get("orgSynergyFlag"))) {
			orgMap.put("orgSynergyFlag", map.get("orgSynergyFlag"));			
		}
		
		// 判断柜台是否已经过期
		String expiringDate = ConvertUtil.getString(map.get("expiringDate"));
		if(!"".equals(expiringDate)){
			// 判断柜台是否已经过期
			int compare = DateUtil.compareDate((String)map.get("expiringDate"), sysDate);
			if(compare <= 0){
				orgMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_DISABLE);
				orgMap.put("status", "4");
			} else {
				orgMap.put(CherryConstants.VALID_FLAG, CherryConstants.VALIDFLAG_ENABLE);
				orgMap.put("status", "0");
			}
		}
		
		// 更新部门信息
		int result = binOLBSDEP03_Service.updateOrganizationInfo(orgMap);
		// 更新0件的场合
		if(result == 0) {
			throw new CherryException("ECM00038");
		}
		
		// 取得下级柜台path
		List<String> counterPathList = (List)map.get("counterPath");
		if(counterPathList != null && !counterPathList.isEmpty()) {
			Map<String, Object> counterMap = new HashMap<String, Object>();
			counterMap.putAll(map);
			counterMap.put("path", map.get("departPath"));
			for(String counterPath : counterPathList) {
				String newCounterNodeId = binOLBSDEP04_Service.getNewNodeId(counterMap);
				counterMap.put("newNodeId", newCounterNodeId);
				counterMap.put("departPath", counterPath);
				// 组织结构节点移动
				binOLBSDEP03_Service.updateOrganizationNode(counterMap);
			}
		}
		// 取得部门ID
		String organizationId = (String)map.get("organizationId");
		// 取得地址信息
		String departAddress = (String)map.get("departAddress");
		// 取得原地址ID
		List<String> oldAddressInfoIdList = (List)map.get("oldAddressInfoId");
		// 地址存在的场合
		if(departAddress != null && !"".equals(departAddress)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(departAddress);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> addMap = list.get(i);
				String addressInfoId = (String)addMap.get("addressInfoId");
				// 作成者
				addMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
				// 作成程序名
				addMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP03");
				// 更新者
				addMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
				// 更新程序名
				addMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP03");
				// 部门ID
				addMap.put("organizationId", organizationId);
				// 默认显示标志
				if(addMap.get("defaultFlag") != null && !"".equals(addMap.get("defaultFlag"))) {
					addMap.put("defaultFlag", 1);
				} else {
					addMap.put("defaultFlag", 0);
				}
				if(addressInfoId != null && !"".equals(addressInfoId)) {
					// 更新时间设定
					addMap.put(CherryConstants.UPDATE_TIME, sysDate);
					// 更新部门地址
					int resultAdd = binOLBSDEP03_Service.updateAddress(addMap);
					// 更新0件的场合
					if(resultAdd == 0) {
						throw new CherryException("ECM00038");
					}
					// 更新下属机构地址
					int resultSubAdd = binOLBSDEP03_Service.updateSubordinateAddress(addMap);
					// 更新0件的场合
					if(resultSubAdd == 0) {
						throw new CherryException("ECM00038");
					}
					oldAddressInfoIdList.remove(addressInfoId);
				} else {
					// 创建时间设定
					addMap.put(CherryConstants.CREATE_TIME, sysDate);
					// 添加地址
					int newAddressInfoId = binOLBSDEP04_Service.addAddress(addMap);
					// 地址ID
					addMap.put("addressInfoId", newAddressInfoId);
					// 添加下属机构地址
					binOLBSDEP04_Service.addSubordinateAddress(addMap);
				}
			}
		}
		// 存在需要删除的原地址的场合，做伦理删除处理
		if(oldAddressInfoIdList != null && !oldAddressInfoIdList.isEmpty()) {
			for(String oldAddr: oldAddressInfoIdList) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("addressInfoId", oldAddr);
				// 部门ID
				paramMap.put("organizationId", organizationId);
				// 系统时间设定
				paramMap.put(CherryConstants.UPDATE_TIME, sysDate);
				// 更新者
				paramMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
				// 更新程序名
				paramMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP03");
				// 伦理删除部门地址
				binOLBSDEP03_Service.deleteAddress(paramMap);
				// 伦理删除下属机构地址
				binOLBSDEP03_Service.deleteSubordinateAddress(paramMap);
			}
		}
		// 取得部门联系人信息
		String departContact = (String)map.get("departContact");
		// 取得原部门联系人
		List<String> oldContactInfoIdList = (List)map.get("oldContactInfoId");
		// 部门联系人存在的场合
		if(departContact != null && !"".equals(departContact)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(departContact);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> contactMap = list.get(i);
				String employeeId = (String)contactMap.get("employeeId");
				// 作成者
				contactMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
				// 作成程序名
				contactMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP03");
				// 更新者
				contactMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
				// 更新程序名
				contactMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP03");
				// 部门ID
				contactMap.put("organizationId", organizationId);
				// 默认显示标志
				if(contactMap.get("defaultFlag") != null && !"".equals(contactMap.get("defaultFlag"))) {
					contactMap.put("defaultFlag", 1);
				} else {
					contactMap.put("defaultFlag", 0);
				}
				if(oldContactInfoIdList == null || !oldContactInfoIdList.contains(employeeId)) {
					// 创建时间设定
					contactMap.put(CherryConstants.CREATE_TIME, sysDate);
					// 添加部门联系人
					binOLBSDEP04_Service.addContactInfo(contactMap);
				} else {
					// 更新时间设定
					contactMap.put(CherryConstants.UPDATE_TIME, sysDate);
					// 更新部门联系人
					int resultContact = binOLBSDEP03_Service.updateContactInfo(contactMap);
					// 更新0件的场合
					if(resultContact == 0) {
						throw new CherryException("ECM00038");
					}
					oldContactInfoIdList.remove(employeeId);
				}
			}
		}
		// 存在需要删除的原部门联系人的场合，做伦理删除处理
		if(oldContactInfoIdList != null && !oldContactInfoIdList.isEmpty()) {
			for(String employeeId: oldContactInfoIdList) {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				// 联系人ID
				paramMap.put("employeeId", employeeId);
				// 部门ID
				paramMap.put("organizationId", organizationId);
				// 系统时间设定
				paramMap.put(CherryConstants.UPDATE_TIME, sysDate);
				// 更新者
				paramMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
				// 更新程序名
				paramMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP03");
				// 伦理删除部门联系人
				binOLBSDEP03_Service.deleteContactInfo(paramMap);
			}
		}
		
		// 新上级
		String path = (String)map.get("path");
		// 原上级
		String higherDepartPath = (String)map.get("higherDepartPath");
		// 上级是否变化
		boolean isMove = false;
		if(path != null) {
			if(higherDepartPath != null && higherDepartPath.equals(path)) {
				isMove = false;
			} else {
				isMove = true;
			}
		} else {
			if(higherDepartPath != null) {
				isMove = true;
			}
		}
		// 上级部门变更的场合
		if(isMove) {
			if(path != null && !"".equals(path)) {
				map.put("path", path);
			} else {
				// 取得品牌下的未知节点
				path = binOLBSCNT04_Service.getUnknownPath(map);
				// 未知节点不为空的场合，该节点作为柜台的上级节点
				if(path != null && !"".equals(path)) {
					map.put("path", path);
				} else {
					// 在品牌下添加一个未知节点来作为没有上级部门的柜台的上级节点
					Map<String, Object> unknownOrgMap = new HashMap<String, Object>();
					// 所属组织
					unknownOrgMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
					// 所属品牌
					unknownOrgMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
					// 作成者
					unknownOrgMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
					// 作成程序名
					unknownOrgMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
					// 更新者
					unknownOrgMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
					// 更新程序名
					unknownOrgMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
					// 未知节点添加在品牌节点下
					unknownOrgMap.put("path", binOLBSCNT04_Service.getFirstPath(map));
					// 取得未知节点path
					String unknownPath = binOLBSDEP04_Service.getNewNodeId(unknownOrgMap);
					unknownOrgMap.put("newNodeId", unknownPath);
					// 未知节点的部门代码
					unknownOrgMap.put("departCode", CherryConstants.UNKNOWN_DEPARTCODE);
					// 未知节点的部门名称
					unknownOrgMap.put("departName", CherryConstants.UNKNOWN_DEPARTNAME);
					// 未知节点的部门类型
					unknownOrgMap.put("type", CherryConstants.UNKNOWN_DEPARTTYPE);
					// 未知节点的到期日expiringDate
					unknownOrgMap.put("expiringDate", DateUtil.suffixDate(CherryConstants.longLongAfter, 1));
					// 添加未知节点
					binOLBSDEP04_Service.addOrganization(unknownOrgMap);
					map.put("path", unknownPath);
				}
			}
			// 取得新部门节点
			String newNodeId = binOLBSDEP04_Service.getNewNodeId(map);
			map.put("newNodeId", newNodeId);
			// 组织结构节点移动
			binOLBSDEP03_Service.updateOrganizationNode(map);
		}
	}

}
