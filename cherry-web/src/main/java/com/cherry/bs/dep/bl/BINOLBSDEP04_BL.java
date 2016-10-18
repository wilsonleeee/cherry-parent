/*
 * @(#)BINOLBSDEP04_BL.java     1.0 2010/10/27
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
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.DateUtil;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 添加部门画面BL
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP04_BL {
	
	/** 添加部门画面Service */
	@Resource
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 更新部门画面Service */
	@Resource
	private BINOLBSDEP03_Service binOLBSDEP03_Service;
	
	/** 创建柜台画面Service */
	@Resource
	private BINOLBSCNT04_Service binOLBSCNT04_Service;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="CodeTable")
	private CodeTable code;
	
	/**
	 * 添加部门处理
	 * 
	 * @param map 添加内容
	 */
	public void tran_addOrganization(Map<String, Object> map) throws Exception {
		
		// 添加部门内容设定map
		Map<String, Object> orgMap = new HashMap<String, Object>();
		// 取得上级部门节点
		String path = (String)map.get("path");
		// 上级部门存在的场合
		if(path != null && !"".equals(path)) {
			orgMap.put("path", path);
		} else {
			// 取得品牌下的未知节点
			path = binOLBSCNT04_Service.getUnknownPath(map);
			// 未知节点不为空的场合，该节点作为柜台的上级节点
			if(path != null && !"".equals(path)) {
				orgMap.put("path", path);
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
				orgMap.put("path", unknownPath);
			}
		}
		// 取得新部门节点
		String newNodeId = binOLBSDEP04_Service.getNewNodeId(orgMap);
		orgMap.put("newNodeId", newNodeId);
		// 取得系统时间
		String sysDate = binOLBSDEP04_Service.getSYSDate();
		// 所属组织
		orgMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
		orgMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 系统时间设定
		orgMap.put(CherryConstants.CREATE_TIME, sysDate);
		// 作成者
		orgMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 作成程序名
		orgMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
		// 更新者
		orgMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		orgMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
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
			orgMap.put("counterSynergyFlag", map.get("orgSynergyFlag"));			
		}		
		
		// 添加部门
		int organizationId = binOLBSDEP04_Service.addOrganization(orgMap);
		
		// 取得下级柜台path
		List<String> counterPathList = (List)map.get("counterPath");
		if(counterPathList != null && !counterPathList.isEmpty()) {
			Map<String, Object> counterMap = new HashMap<String, Object>();
			counterMap.putAll(map);
			counterMap.put("path", newNodeId);
			for(String counterPath : counterPathList) {
				String newCounterNodeId = binOLBSDEP04_Service.getNewNodeId(counterMap);
				counterMap.put("newNodeId", newCounterNodeId);
				counterMap.put("departPath", counterPath);
				// 组织结构节点移动
				binOLBSDEP03_Service.updateOrganizationNode(counterMap);
			}
		}
		// 取得地址信息
		String departAddress = (String)map.get("departAddress");
		if(departAddress != null && !"".equals(departAddress)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(departAddress);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> addMap = list.get(i);
				if(addMap.get("addressLine1") != null && !"".equals(addMap.get("addressLine1"))) {
					// 系统时间设定
					addMap.put(CherryConstants.CREATE_TIME, sysDate);
					// 作成者
					addMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
					// 作成程序名
					addMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
					// 更新者
					addMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
					// 更新程序名
					addMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
					// 添加地址
					int addressInfoId = binOLBSDEP04_Service.addAddress(addMap);
					// 部门ID
					addMap.put("organizationId", organizationId);
					// 地址ID
					addMap.put("addressInfoId", addressInfoId);
					// 默认显示标志
					if(addMap.get("defaultFlag") != null && !"".equals(addMap.get("defaultFlag"))) {
						addMap.put("defaultFlag", 1);
					} else {
						addMap.put("defaultFlag", 0);
					}
					// 添加下属机构地址
					binOLBSDEP04_Service.addSubordinateAddress(addMap);
					
				}
			}
		}
		// 取得部门联系人信息
		String departContact = (String)map.get("departContact");
		if(departContact != null && !"".equals(departContact)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(departContact);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> contactMap = list.get(i);
				if(contactMap.get("employeeId") != null && !"".equals(contactMap.get("employeeId"))) {
					// 系统时间设定
					contactMap.put(CherryConstants.CREATE_TIME, sysDate);
					// 作成者
					contactMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
					// 作成程序名
					contactMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
					// 更新者
					contactMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
					// 更新程序名
					contactMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
					// 部门ID
					contactMap.put("organizationId", organizationId);
					// 默认显示标志
					if(contactMap.get("defaultFlag") != null && !"".equals(contactMap.get("defaultFlag"))) {
						contactMap.put("defaultFlag", 1);
					} else {
						contactMap.put("defaultFlag", 0);
					}
					// 添加部门联系人
					binOLBSDEP04_Service.addContactInfo(contactMap);
				}
			}
		}
		
		// 取得仓库设定标志 0：创建默认仓库 1：选择已有仓库
		String isCreateFlg = (String)map.get("isCreateFlg");
		// 添加仓库内容设定map
		Map<String, Object> ivtMap = new HashMap<String, Object>();
		// 作成者
		ivtMap.put(CherryConstants.CREATEDBY, map.get(CherryConstants.CREATEDBY));
		// 作成程序名
		ivtMap.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
		// 更新者
		ivtMap.put(CherryConstants.UPDATEDBY, map.get(CherryConstants.UPDATEDBY));
		// 更新程序名
		ivtMap.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
		// 所属组织
		ivtMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
		ivtMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
		// 所属部门
		ivtMap.put("organizationId", organizationId);
		// 缺省仓库区分
		ivtMap.put("defaultFlag", CherryConstants.IVT_DEFAULTFLAG);
		// 测试区分
		ivtMap.put("testType", map.get("testType"));
		// 仓库设定标志为0（创建默认仓库）时
		if(isCreateFlg != null && "0".equals(isCreateFlg)) {
			// 仓库名称
			ivtMap.put("inventoryNameCN", map.get("departName")+CherryConstants.IVT_NAME_CN_DEFAULT);
			// 设定仓库类型为非柜台仓库
			ivtMap.put("depotType", "01");
			// 添加默认仓库处理
			this.addDefaultDepot(ivtMap);
		} else {
			// 取得仓库ID
			String depotInfoId = (String)map.get("depotInfoId");
			// 仓库ID存在时
			if(depotInfoId != null && !"".equals(depotInfoId)) {
				// 取得仓库信息
				Map<String, Object> depotInfoMap = binOLBSDEP04_Service.getDepotInfo(map);
				if(depotInfoMap != null) {
					ivtMap.putAll(depotInfoMap);
					// 添加部门仓库关系
					binOLBSDEP04_Service.addInventoryInfo(ivtMap);
				}
			}
		}
	}
	
	/**
	 * 验证同一组织中是否存在同样的部门ID
	 * 
	 * @param map 查询条件
	 * @return 件数
	 */
	public String getOrganizationIdCheck(Map<String, Object> map) {
		
		// 验证同一组织中是否存在同样的部门ID
		return binOLBSDEP04_Service.getOrganizationIdCheck(map);
	}
	
	/**
	 * 取得仓库信息List
	 * 
	 * @param map 查询条件
	 * @return 仓库信息List
	 */
	public List<Map<String, Object>> getDepotInfoList(Map<String, Object> map) {
		
		// 取得仓库信息List
		return binOLBSDEP04_Service.getDepotInfoList(map);
	}
	
	/**
	 * 添加默认仓库处理
	 * 
	 * @param map 添加内容
	 */
	public void addDefaultDepot(Map<String, Object> map) {
		
		// 添加仓库内容设定map
		Map<String, Object> ivtMap = new HashMap<String, Object>();
		ivtMap.putAll(map);
		
		// 仓库编码
		Map codeMap = code.getCode("1120","3");
		map.put("type", "3");
		map.put("length", codeMap.get("value2"));
		// 自动生成仓库编码
		ivtMap.put("inventoryCode", (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(map));
		// 判断仓库编码是否已经存在
		int count = binOLBSDEP04_Service.getDepotCountByCode(ivtMap);
		while(count > 0) {
			// 自动生成仓库编码
			ivtMap.put("inventoryCode", (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(map));
			// 判断仓库编码是否已经存在
			count = binOLBSDEP04_Service.getDepotCountByCode(ivtMap);
		}
		// 添加仓库
		int depotInfoId = binOLBSDEP04_Service.addDepotInfo(ivtMap);
		ivtMap.put("depotInfoId", depotInfoId);
		// 添加部门仓库关系
		binOLBSDEP04_Service.addInventoryInfo(ivtMap);
	}

}
