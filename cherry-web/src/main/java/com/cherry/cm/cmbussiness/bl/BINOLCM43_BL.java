package com.cherry.cm.cmbussiness.bl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbussiness.dto.AgentInfo;
import com.cherry.cm.cmbussiness.dto.BaseDTO;
import com.cherry.cm.cmbussiness.service.BINOLCM43_Service;
import com.cherry.cm.core.CherryConstants;

/**
 * 微商管理BL
 * 
 * @author WangCT
 * @version 2015-08-18 1.0.0
 */
public class BINOLCM43_BL {
	
	/** 微商管理Service */
	@Resource
	private BINOLCM43_Service binOLCM43_Service;
	
	/** 标准区域共通BL */
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	/** 各类编号取号共通BL */
	@Resource
	private BINOLCM15_BL binOLCM15_BL;
	
	/**
	 * 创建微商处理
	 * 
	 */
	public void tran_createAgent(AgentInfo agentInfo) {
		
		int orgId = agentInfo.getOrganizationInfoId();
		int brandId = agentInfo.getBrandInfoId();
		String agentMobile = agentInfo.getAgentMobile();
		String agentName = agentInfo.getAgentName();
		String agentProvince = agentInfo.getAgentProvince();
		String agentCity = agentInfo.getAgentCity();
		String provinceId = agentInfo.getProvinceId();
		String cityId = agentInfo.getCityId();
		String agentLevel = agentInfo.getAgentLevel();
		String agentOpenID = agentInfo.getAgentOpenID();
		String supOrgPath = agentInfo.getSupOrgPath();
		String supEmpPath = agentInfo.getSupEmpPath();
		String departName = null;
		if(null != agentInfo.getDepartName() && !"".equals(agentInfo.getDepartName())) {
			departName = agentInfo.getDepartName();
		} else {
			departName = agentInfo.getAgentName();
		}
		
		// 添加部门处理
		Map<String, Object> departMap = new HashMap<String, Object>();
		departMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		departMap.put(CherryConstants.BRANDINFOID, brandId);
		if((provinceId != null && !"".equals(provinceId)) 
				|| (cityId != null && !"".equals(cityId))) {
			departMap.put("provinceId", provinceId);
			departMap.put("cityId", cityId);
		} else {
			if(agentCity != null && !"".equals(agentCity)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityName(agentProvince, agentCity);
				if(regionInfo != null) {
					departMap.put("provinceId", regionInfo.get("provinceId"));
					departMap.put("cityId", regionInfo.get("cityId"));
				}
			}
		}
		Map<String, Object> orgNodeMap = new HashMap<String, Object>();
		orgNodeMap.put("orgPath", supOrgPath);
		String newOrgNodeId = binOLCM43_Service.getNewOrgNodeId(orgNodeMap);
		departMap.put("newNodeId", newOrgNodeId);
		departMap.put("departCode", agentMobile);
		departMap.put("departName", departName);//部门名称改为用微店名称，而不是微商姓名
		departMap.put("type", agentLevel);
		departMap.put("testType", "0");
		departMap.put("status", "0");
		this.setCommonParam(departMap, agentInfo);
		int departId = binOLCM43_Service.addOrganization(departMap);
		
		// 添加员工处理
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		employeeMap.put(CherryConstants.BRANDINFOID, brandId);
		employeeMap.put("departId", departId);
		Map<String, Object> categoryInfo = new HashMap<String, Object>();
		categoryInfo.put("categoryCode", "10");
		categoryInfo = binOLCM43_Service.getPositionCategoryInfo(categoryInfo);
		if(categoryInfo != null) {
			employeeMap.put("positionCategoryId", categoryInfo.get("positionCategoryId"));
		}
		Map<String, Object> empNodeMap = new HashMap<String, Object>();
		empNodeMap.put("empPath", supEmpPath);
		String newEmpNodeId = binOLCM43_Service.getNewEmpNodeId(empNodeMap);
		employeeMap.put("newNodeId", newEmpNodeId);
		employeeMap.put("employeeCode", agentMobile);
		employeeMap.put("employeeName", agentName);
		employeeMap.put("mobilePhone", agentMobile);
		employeeMap.put("openID", agentOpenID);
		this.setCommonParam(employeeMap, agentInfo);
		binOLCM43_Service.addEmployee(employeeMap);
		
		// 添加柜台处理
		Map<String, Object> counterMap = new HashMap<String, Object>();
		counterMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		counterMap.put(CherryConstants.BRANDINFOID, brandId);
		counterMap.put("organizationId", departId);
		counterMap.put("counterCode", agentMobile);
		counterMap.put("counterNameIF", departName);//柜台名称改为用微店名称，而不是微商姓名
		counterMap.put("counterKind", "0");
		counterMap.put("status", "0");
		this.setCommonParam(counterMap, agentInfo);
		binOLCM43_Service.addCounterInfo(counterMap);
		
		// 添加柜台默认仓库处理
		Map<String, Object> ivtMap = new HashMap<String, Object>();
		// 所属组织
		ivtMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		// 所属品牌
		ivtMap.put(CherryConstants.BRANDINFOID, brandId);
		// 所属部门
		ivtMap.put("organizationId", departId);
		// 缺省仓库区分
		ivtMap.put("defaultFlag", CherryConstants.IVT_DEFAULTFLAG);
		// 仓库名称
		ivtMap.put("inventoryNameCN", agentName+CherryConstants.IVT_NAME_CN_DEFAULT);
		// 设定仓库类型为柜台仓库
		ivtMap.put("depotType", "02");
		// 测试区分
		ivtMap.put("testType", "0");
		// 仓库编码
		Map<String, Object> codeMap = new HashMap<String, Object>();
		codeMap.put(CherryConstants.ORGANIZATIONINFOID, orgId);
		codeMap.put(CherryConstants.BRANDINFOID, brandId);
		codeMap.put("type", "3");
		codeMap.put("length", "4");
		ivtMap.put("inventoryCode", "IVT"+binOLCM15_BL.getSequenceId(codeMap));
		this.setCommonParam(ivtMap, agentInfo);
		// 添加仓库
		int depotInfoId = binOLCM43_Service.addDepotInfo(ivtMap);
		ivtMap.put("depotInfoId", depotInfoId);
		// 添加部门仓库关系
		binOLCM43_Service.addInventoryInfo(ivtMap);
	}
	
	/**
	 * 查询微商信息
	 * 
	 */
	public Map<String, Object> getAgentInfo(String agentMobile) {
		Map<String, Object> agentMap = new HashMap<String, Object>();
		agentMap.put("agentMobile", agentMobile);
		return binOLCM43_Service.getAgentInfo(agentMap);
	}
	
	/**
	 * 更新微商信息
	 * 
	 */
	public void tran_updateAgent(AgentInfo agentInfo) {
		
		// 更新员工信息
		Map<String, Object> employeeMap = new HashMap<String, Object>();
		employeeMap.put("employeeId", agentInfo.getEmployeeId());
		employeeMap.put("mobilePhone", agentInfo.getAgentMobile());
		employeeMap.put("employeeName", agentInfo.getAgentName());
		this.setCommonParam(employeeMap, agentInfo);
		binOLCM43_Service.updEmployee(employeeMap);
		
		String departName = null;
		if(null != agentInfo.getDepartName() && !"".equals(agentInfo.getDepartName())) {
			departName = agentInfo.getDepartName();
		} else {
			departName = agentInfo.getAgentName();
		}
		// 更新部门信息
		Map<String, Object> departMap = new HashMap<String, Object>();
		departMap.put("departId", agentInfo.getDepartId());
		departMap.put("departName", departName);
		String agentProvince = agentInfo.getAgentProvince();
		String agentCity = agentInfo.getAgentCity();
		String provinceId = agentInfo.getProvinceId();
		String cityId = agentInfo.getCityId();
		if((provinceId != null && !"".equals(provinceId)) 
				|| (cityId != null && !"".equals(cityId))) {
			departMap.put("provinceId", provinceId);
			departMap.put("cityId", cityId);
		} else {
			if(agentCity != null && !"".equals(agentCity)) {
				Map<String, Object> regionInfo = binOLCM08_BL.getRegionInfoByCityName(agentProvince, agentCity);
				if(regionInfo != null) {
					departMap.put("provinceId", regionInfo.get("provinceId"));
					departMap.put("cityId", regionInfo.get("cityId"));
				}
			}
		}
		departMap.put("type", agentInfo.getAgentLevel());
		this.setCommonParam(departMap, agentInfo);
		binOLCM43_Service.updDepart(departMap);
		
		//修改微店名称时修改柜台名称
		Map<String, Object> counterMap = new HashMap<String, Object>();
		counterMap.put("organizationId", agentInfo.getDepartId());
		counterMap.put("counterNameIF", departName);
		this.setCommonParam(counterMap, agentInfo);
		binOLCM43_Service.updCounter(counterMap);
		
		// 上级发生变化的场合
		if(null != agentInfo.getOldSuperMobile() && !agentInfo.getOldSuperMobile().equals(agentInfo.getSuperMobile())) {
			// 员工节点移动
			employeeMap = new HashMap<String, Object>();
			employeeMap.put("empPath", agentInfo.getEmpPath());
			Map<String, Object> empNodeMap = new HashMap<String, Object>();
			empNodeMap.put("empPath", agentInfo.getSupEmpPath());
			String newEmpNodeId = binOLCM43_Service.getNewEmpNodeId(empNodeMap);
			employeeMap.put("newNodeId", newEmpNodeId);
			this.setCommonParam(employeeMap, agentInfo);
			binOLCM43_Service.updEmployeeNode(employeeMap);
			
			// 部门节点移动
			departMap = new HashMap<String, Object>();
			departMap.put("orgPath", agentInfo.getOrgPath());
			Map<String, Object> orgNodeMap = new HashMap<String, Object>();
			orgNodeMap.put("orgPath", agentInfo.getSupOrgPath());
			String newOrgNodeId = binOLCM43_Service.getNewOrgNodeId(orgNodeMap);
			departMap.put("newNodeId", newOrgNodeId);
			this.setCommonParam(departMap, agentInfo);
			binOLCM43_Service.updDepartNode(departMap);
		}
	}
	
	/**
	 * 通过手机号查询微商是否存在
	 * 
	 */
	public Map<String, Object> getAgentExistByMobile(String agentMobile) {
		Map<String, Object> agentMap = new HashMap<String, Object>();
		agentMap.put("agentMobile", agentMobile);
		return binOLCM43_Service.getAgentExistByMobile(agentMap);
	}
	
	/**
	 * 通过微信OpenID查询微商是否存在
	 * 
	 */
	public Map<String, Object> getAgentExistByOpenID(String agentOpenID) {
		Map<String, Object> agentMap = new HashMap<String, Object>();
		agentMap.put("agentOpenID", agentOpenID);
		return binOLCM43_Service.getAgentExistByOpenID(agentMap);
	}
	
	/**
	 * 共通参数设置
	 * 
	 */
	public void setCommonParam(Map<String, Object> map, BaseDTO baseDTO) {
		// 作成者
		map.put(CherryConstants.CREATEDBY, baseDTO.getCreatedBy());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, baseDTO.getCreatePGM());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, baseDTO.getUpdatedBy());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, baseDTO.getUpdatePGM());
	}

}
