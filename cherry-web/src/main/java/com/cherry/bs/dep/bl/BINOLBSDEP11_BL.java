/*
 * @(#)BINOLBSDEP11_BL.java     1.0 2011.2.10
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
import com.cherry.bs.dep.service.BINOLBSDEP92_Service;
import com.cherry.bs.reg.service.BINOLBSREG04_Service;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CustomerContextHolder;


/**
 * 品牌添加画面BL
 * 
 * @author WangCT
 * @version 1.0 2011.2.10
 */
public class BINOLBSDEP11_BL {
	
	/** 品牌信息管理共通Service */
	@Resource
	private BINOLBSDEP92_Service binOLBSDEP92_Service;
	
	/** 添加部门画面Service */
	@Resource
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 添加部门画面BL */
	@Resource
	private BINOLBSDEP04_BL binOLBSDEP04_BL;
	
	/** 区域添加画面Service */
	@Resource
	private BINOLBSREG04_Service binOLBSREG04_Service;
	
	/**
	 * 添加品牌
	 * 
	 * @param map 添加内容
	 */
	public void tran_addBrandInfo(Map<String, Object> map) {
		
		// 取得系统时间
		String sysDate = binOLBSDEP92_Service.getSYSDate();
		// 系统时间设定
		map.put(CherryConstants.CREATE_TIME, sysDate);
		// 添加品牌
		int brandInfoId = binOLBSDEP92_Service.addBrandInfo(map);
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 给某品牌添加默认的基本配置信息
//		binOLBSDEP92_Service.addSystemConfig(map);
		// 给品牌添加默认的岗位信息
		binOLBSDEP92_Service.addPosCategory(map);
		// 给品牌添加默认的密码配置信息
		binOLBSDEP92_Service.addPwdConfig(map);
		// 添加品牌业务日期
		binOLBSDEP92_Service.addBussinessDate(map);
		
		// 添加默认厂商设定map
		Map<String, Object> facMap = new HashMap<String, Object>();
		facMap.putAll(map);
		// 默认厂商代码
		facMap.put("manufacturerCode", CherryConstants.MANUFACTURERCODE);
		// 默认厂商名称（中文）
		facMap.put("factoryNameCN", CherryConstants.FACTORYNAMECN);
		// 默认厂商代码（中文简称）
		facMap.put("factoryNamecnCNShort", CherryConstants.FACTORYNAMECNCNSHORT);
		// 默认厂商代码（英文）
		facMap.put("factoryNameEN", CherryConstants.FACTORYNAMEEN);
		// 默认厂商代码（英文简称）
		facMap.put("factoryNameENShort", CherryConstants.FACTORYNAMEENSHORT);
		// 默认区分
		facMap.put("defaultFlag", CherryConstants.FACTORYDEFAULTFLAG);
		// 添加默认厂商信息
		binOLBSDEP92_Service.addManufacturerInfo(facMap);
		
		// 取得品牌区域节点
		String brandRegionNode = binOLBSDEP92_Service.getBrandRegionNode(map);
		// 品牌区域节点不存在的场合，添加品牌区域节点
		if(brandRegionNode == null) {
			// 添加组织品牌区域节点设定map
			Map<String, Object> regMap = new HashMap<String, Object>();
			regMap.putAll(map);
			// 区域类型
			regMap.put("regionType", CherryConstants.REGIONTYPEORGBRAND);
			// 加入日期
			regMap.put("joinDate", sysDate);
			// 取得组织区域节点
			String nodeId = binOLBSDEP92_Service.getOrgRegionNode(map);
			// 组织区域节点不存在的场合，先添加组织区域节点
			if(nodeId == null) {
				regMap.put("higherPath", CherryConstants.DUMMY_VALUE);
				// 取得新节点
				nodeId = binOLBSREG04_Service.getNewRegNodeId(regMap);
				regMap.put("nodeId", nodeId);
				// 所属品牌ID
				regMap.put(CherryConstants.BRANDINFOID, CherryConstants.BRAND_INFO_ID_VALUE);
				// 区域中文名称
				regMap.put("regionNameChinese", map.get("orgNameChinese"));
				// 区域代码
				regMap.put("regionCode", CherryConstants.REGIONCODEORG);
				// 添加组织区域节点
				binOLBSREG04_Service.addRegion(regMap);
			}
			regMap.put("higherPath", nodeId);
			// 取得新节点
			nodeId = binOLBSREG04_Service.getNewRegNodeId(regMap);
			regMap.put("nodeId", nodeId);
			// 所属品牌ID
			regMap.put(CherryConstants.BRANDINFOID, map.get(CherryConstants.BRANDINFOID));
			// 区域中文名称
			regMap.put("regionNameChinese", map.get("brandNameChinese"));
			// 区域代码
			regMap.put("regionCode", CherryConstants.REGIONCODEBRAND);
			// 添加品牌区域节点
			binOLBSREG04_Service.addRegion(regMap);
		}
		
		// 查询顶级部门信息
		Map<String, Object> higherDepartMap = binOLBSDEP04_Service.getHigherDepart(map);
//		// 添加岗位内容设定map
//		Map<String, Object> posMap = new HashMap<String, Object>();
//		posMap.put("path", higherDepartMap.get("seniorPostPath"));
//		// 取得岗位新节点
//		String newPosNodeId = binOLBSDEP04_Service.getNewPosNodeId(posMap);
//		posMap.putAll(map);
//		posMap.put("newNodeId", newPosNodeId);
//		posMap.put("organizationId", higherDepartMap.get("organizationId"));
//		// 添加岗位
//		int postionId = binOLBSDEP04_Service.addPosition(posMap);
		
		// 添加部门内容设定map
		Map<String, Object> orgMap = new HashMap<String, Object>();
		orgMap.put("path", higherDepartMap.get("path"));
		// 取得新部门节点
		String newNodeId = binOLBSDEP04_Service.getNewNodeId(orgMap);
		orgMap.putAll(map);
		orgMap.put("newNodeId", newNodeId);
		// 所属品牌
		orgMap.put(CherryConstants.BRANDINFOID, brandInfoId);
//		// 直属上级岗位
//		orgMap.put("seniorPost", postionId);
		// 部门代码
		orgMap.put("departCode", map.get("brandCode"));
		// 部门名称
		if(map.get("brandNameChinese") != null && !"".equals(map.get("brandNameChinese"))) {
			orgMap.put("departName", map.get("brandNameChinese"));
			orgMap.put("brandName", map.get("brandNameChinese"));
		}
		// 部门简称
		if(map.get("brandNameShort") != null && !"".equals(map.get("brandNameShort"))) {
			orgMap.put("departNameShort", map.get("brandNameShort"));
		}
		// 部门英文名称
		if(map.get("brandNameForeign") != null && !"".equals(map.get("brandNameForeign"))) {
			orgMap.put("nameForeign", map.get("brandNameForeign"));
		}
		// 部门英文简称
		if(map.get("brandNameForeignShort") != null && !"".equals(map.get("brandNameForeignShort"))) {
			orgMap.put("nameShortForeign", map.get("brandNameForeignShort"));
		}
		// 部门类型
		orgMap.put("type", "1");
		// 测试区分
		orgMap.put("testType", "0");
		int organizationId = binOLBSDEP04_Service.addOrganization(orgMap);
		
		// 添加仓库内容设定map
		Map<String, Object> ivtMap = new HashMap<String, Object>();
		ivtMap.putAll(map);
		// 所属组织
		ivtMap.put(CherryConstants.ORGANIZATIONINFOID, map.get(CherryConstants.ORGANIZATIONINFOID));
		// 所属品牌
		ivtMap.put(CherryConstants.BRANDINFOID, brandInfoId);
		// 所属部门
		ivtMap.put("organizationId", organizationId);
		// 缺省仓库区分
		ivtMap.put("defaultFlag", CherryConstants.IVT_DEFAULTFLAG);
		// 仓库名称
		ivtMap.put("inventoryNameCN", map.get("brandNameChinese")+CherryConstants.IVT_NAME_CN_DEFAULT);
		// 设定仓库类型为非柜台仓库
		ivtMap.put("depotType", "01");
		// 测试区分
		ivtMap.put("testType", "0");
		// 添加默认仓库处理
		binOLBSDEP04_BL.addDefaultDepot(ivtMap);
		
		// 新后台数据源
		orgMap.put("dataSourceName", CustomerContextHolder.getCustomerDataSourceType());
		
		//判断BIN_BrandDataSourceConfig表中是否存在该品牌的配置信息
		int OrgCode = binOLBSDEP92_Service.checkOrgCode(orgMap);
		if(OrgCode==0){
		binOLBSDEP92_Service.addDataSource(orgMap);
		}
	}
	
	/**
	 * 判断品牌代码是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 品牌ID
	 */
	public String checkBrandCode(Map<String, Object> map) {
		
		// 判断品牌代码是否已经存在
		return binOLBSDEP92_Service.checkBrandCode(map);
	}
	
	/**
	 * 判断品牌名称是否已经存在
	 * 
	 * @param map 查询条件
	 * @return 品牌ID
	 */
	public String checkBrandName(Map<String, Object> map) {
		
		// 判断品牌名称是否已经存在
		return binOLBSDEP92_Service.checkBrandName(map);
	}

}
