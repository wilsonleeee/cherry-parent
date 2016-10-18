/*  
 * @(#)BINOLBSWEM04_Action.java     1.0 2015/08/18      
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
package com.cherry.bs.wem.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.wem.form.BINOLBSWEM04_Form;
import com.cherry.bs.wem.interfaces.BINOLBSWEM01_IF;
import com.cherry.bs.wem.interfaces.BINOLBSWEM04_IF;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM43_BL;
import com.cherry.cm.cmbussiness.dto.AgentInfo;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.CherryUtil;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.dr.cmbussiness.util.CampRuleUtil;
import com.cherry.webservice.agent.bl.AgentInfoLogic;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 微商一览
 * 
 * @author Hujh
 * @version 1.0 2015/08/18
 */
public class BINOLBSWEM04_Action extends BaseAction implements ModelDriven<BINOLBSWEM04_Form> {
	
	private static final long serialVersionUID = 1158366407329340452L;
	
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSWEM04_Action.class);

	BINOLBSWEM04_Form form = new BINOLBSWEM04_Form();
	
	@Resource
	private BINOLCM08_BL binOLCM08_BL;
	
	@Resource(name="binOLBSWEM04_BL")
	private BINOLBSWEM04_IF binOLBSWEM04_BL;	
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name = "binOLBSWEM01_BL")
	private BINOLBSWEM01_IF binOLBSWEM01_BL;
	
	@Resource
	private BINOLCM43_BL binOLCM43_BL;
	
	@Resource(name="CodeTable")
	private CodeTable codeTable;
	
	@Resource(name="agentInfoLogic")
	private AgentInfoLogic agentInfoLogic;
	
	/** 区域List */
	@SuppressWarnings("rawtypes")
	private List reginList;
	
	private List<Map<String, Object>> wechatMerchantList;
	
	private List agentLevelList;
	
	private Map<String, Object> agentInfoMap;
	
	private Map<String, Object> superAgentInfoMap;
	
	private List<Map<String, Object>> reservedCodeList;
	
	public String init() {
		// 参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		//取得部门类型
		List codeList = codeTable.getAllByCodeType("1000");
		agentLevelList = binOLBSWEM04_BL.getAgentLevelList(codeList);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 不是总部用户的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 取得区域List
		reginList = binOLCM08_BL.getReginList(map);
		
		return SUCCESS;
	}
	
	/**
	 * 查询
	 */
	public String search() {
		try {
			Map<String, Object> map = getSearchMap();
			int count = binOLBSWEM04_BL.getWechatMerchantCount(map);
			if(count > 0) {
				wechatMerchantList = binOLBSWEM04_BL.getWechatMerchantList(map);
			} 
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			// 系统发生异常，请联系管理员
			this.addActionError(getText("ECM00036"));
            return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		return SUCCESS;
	}
	
	
	private Map<String, Object> getSearchMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put("agentMobile", form.getAgentMobile());
		map.put("agentName", form.getAgentName());
		map.put("agentLevel", form.getAgentLevel());//部门类型
		map.put("superMobile", form.getSuperMobile());
		map.put("provinceId", form.getProvinceId());
		map.put("cityId", form.getCityId());
		map.put("departName", form.getDepartName());
		map.put("categoryCode", "10");//指定岗位为10
		//取上级path
		if(!CherryChecker.isNullOrEmpty(form.getSuperMobile(), true)) {
			Map<String, Object> superAgentMap = binOLCM43_BL.getAgentInfo(form.getSuperMobile());
			if(!CherryChecker.isNullOrEmpty(superAgentMap, true)) {
				map.put(CherryConstants.CREATEDBY, userInfo.getEmployeeCode());
				map.put(CherryConstants.CREATEPGM, "BINOLBSWEM04_Action");
				String supEmpPath = ConvertUtil.getString(superAgentMap.get("empPath"));
				String supOrgPath = ConvertUtil.getString(superAgentMap.get("orgPath"));
				if(!CherryChecker.isNullOrEmpty(supEmpPath, true)) {
					map.put("supEmpPath", supEmpPath);
				} 
				if(!CherryChecker.isNullOrEmpty(supOrgPath, true)) {
					map.put("supOrgPath", supOrgPath);
				}
			}
		}
		ConvertUtil.setForm(form, map);
		map = CherryUtil.removeEmptyVal(map);
		return map;
	}

	/**
	 * 新增初始化
	 * @return
	 */
	public String add() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String organizationInfoId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		form.setPre_suffix(binOLCM14_BL.getConfigValue("1335", organizationInfoId, brandInfoId));//微店名称前后缀
		List codeList = codeTable.getAllByCodeType("1000");
		agentLevelList = binOLBSWEM04_BL.getAgentLevelList(codeList);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		reginList = binOLCM08_BL.getReginList(map);
		return SUCCESS;
	}
	
	/**
	 * 新增
	 * @throws Exception 
	 */
	public String insert() throws Exception {
		Map<String, Object> map = getSearchMap();//参数map
/*		if(CherryChecker.isNullOrEmpty(form.getDepartName())) {
			UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
			String organizationInfoId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
			String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("BIN_OrganizationInfoID", organizationInfoId);
			tempMap.put("BIN_BrandInfoID", brandInfoId);
			map.put("departName", getAgentShopName(tempMap));//微店名称
		} else {
			map.put("departName", form.getDepartName());//微店名称
		}
*/		AgentInfo agentInfo = CampRuleUtil.map2Bean(map, AgentInfo.class);
		try {
			binOLCM43_BL.tran_createAgent(agentInfo);
			//将预留号置为已使用
			//binOLBSWEM04_BL.tran_setReservedCodeInvalid(map);
			this.addActionMessage(getText("ICM00002"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.addActionError(getText("ECM00089"));
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	//新增验证
	public void validateInsert() {
		Map<String, Object> map = getSearchMap();//参数map
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String agentMobile = form.getAgentMobile();
		String agentName = form.getAgentName();
		String agentLevel = form.getAgentLevel();
		String superMobile = form.getSuperMobile();
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		//微商手机号验证
		if(CherryChecker.isNullOrEmpty(agentMobile, true)){
			this.addFieldError("agentMobile", getText("BSWEM0001"));
		} else if (!CherryChecker.isPhoneValid(agentMobile, mobileRule)) {
			this.addFieldError("agentMobile", getText("EBS00004"));
		} else {
			//判断手机号是否存在
			Map<String, Object> mobilePhoneExistsMap = binOLCM43_BL.getAgentExistByMobile(agentMobile);
			if(!CherryChecker.isNullOrEmpty(mobilePhoneExistsMap, true)) {
				//微商手机号已存在
				this.addFieldError("agentMobile", getText("ECM00032", new String[] { getText("WEM00001") }));
			}
			//判断该手机号是否存在申请表中，若存在着不能添加
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("BIN_OrganizationInfoID", organizationInfoId);
			tempMap.put("BIN_BrandInfoID", brandInfoId);
			tempMap.put("ApplyMobile", agentMobile);
			Map<String, Object> mobExistsInAgentApplyMap = binOLBSWEM04_BL.getMobExistsInAgentApply(tempMap);
			if(null != mobExistsInAgentApplyMap) {
				this.addFieldError("agentMobile", "此手机号已经提出申请代理，不需要添加");
			}
		}
		
		//名称不能为空
		if(CherryChecker.isNullOrEmpty(agentName, true)) {
			this.addFieldError("agentName", getText("BSWEM0002"));
		}
		
		//部门类型不能为空
		if(CherryChecker.isNullOrEmpty(agentLevel, true)) {
			this.addFieldError("agentLevel", getText("BSWEM0003"));
		}
		
		//上级手机号不能为空，且改手机号对应的上级级别必须大于选择的部门
		if(CherryChecker.isNullOrEmpty(superMobile, true)) {
			this.addFieldError("superMobile", getText("BSWEM0004"));
		} else {
			//查询上级的信息
			Map<String, Object> superAgentMap = binOLCM43_BL.getAgentInfo(superMobile);
			if(CherryChecker.isNullOrEmpty(superAgentMap, true)) {
				this.addFieldError("superMobile", getText("BSWEM0005"));
			} else {
				String superAgentLevel = ConvertUtil.getString(superAgentMap.get("agentLevel"));
				if(CherryChecker.isNullOrEmpty(superAgentLevel, true)) {
					this.addFieldError("superMobile", getText("BSWEM0006"));
				} else {
					String superLevel = codeTable.propGrade("1000", superAgentLevel);
					String level = codeTable.propGrade("1000", agentLevel);
					//部门类型等级大于上级等级
					if(superLevel.compareToIgnoreCase(level) >= 0) {
						this.addFieldError("superMobile", getText("BSWEM0007"));
					}
				}
			}
		}
	}
	
	/*
	 * 编辑页面
	 */
	public String edit() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String organizationInfoId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		form.setPre_suffix(binOLCM14_BL.getConfigValue("1335", organizationInfoId, brandInfoId));//微店名称前后缀
		List codeList = codeTable.getAllByCodeType("1000");
		agentLevelList = binOLBSWEM04_BL.getAgentLevelList(codeList);
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		}
		map.put(CherryConstants.SESSION_LANGUAGE, session.get(CherryConstants.SESSION_LANGUAGE));
		reginList = binOLCM08_BL.getReginList(map);
		String agentMobile = form.getAgentMobile();
		agentInfoMap = binOLCM43_BL.getAgentInfo(agentMobile);
		String superAgentMobile = ConvertUtil.getString(agentInfoMap.get("superMobile"));
		if(!"".equals(superAgentMobile) && null != superAgentMobile) {
			superAgentInfoMap = binOLCM43_BL.getAgentInfo(superAgentMobile);
			if(null != superAgentInfoMap) {
				String superAgentLevel = ConvertUtil.getString(superAgentInfoMap.get("agentLevel"));
				String superAgentLevelName = codeTable.getVal("1000", superAgentLevel);
				agentInfoMap.put("superAgentLevelName", superAgentLevelName);
			}
		}
		String employeeId = String.valueOf(agentInfoMap.get("employeeId"));
		if(null != employeeId && !"null".equalsIgnoreCase(employeeId) && !"".equals(employeeId)) {
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("BIN_OrganizationInfoID", organizationInfoId);
			tempMap.put("BIN_BrandInfoID", brandInfoId);
			tempMap.put("BIN_EmployeeID", employeeId);
			form.setAgentAccountInfoList(binOLBSWEM04_BL.getAgentAccountInfoList(tempMap));
		}
		return SUCCESS;
	}
	
	
	/**
	 * 更新
	 * @throws Exception 
	 */
	public String save() throws Exception {
		Map<String, Object> map = getUpdateAgentMap();
		AgentInfo agentInfo = CampRuleUtil.map2Bean(map, AgentInfo.class);
		try {
			//审核操作
			String auditFlag = ConvertUtil.getString(map.get("auditFlag"));
			if(!CherryChecker.isNullOrEmpty(auditFlag, true)) {
				//将微店名称查询出来
				String agentMobile = ConvertUtil.getString(map.get("agentMobile"));
				Map<String, Object> agentInfoMap = binOLCM43_BL.getAgentInfo(agentMobile);
				String departName = ConvertUtil.getString(agentInfoMap.get("departName"));
				if(CherryChecker.isNullOrEmpty(departName)) {
					agentInfo.setDepartName(agentInfo.getAgentName());
				} else {
					agentInfo.setDepartName(departName);
				}
				binOLBSWEM01_BL.tran_audit(map);
				//审核不通过时不更新微商信息
				if("2".equals(auditFlag)) {
					this.addActionMessage(getText("ICM00002"));
					return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
				}
			}
			//如果修改了微店名称，则调用PHP接口修改微店名称
			String oldDepartName = ConvertUtil.getString(request.getParameter("oldDepartName"));
			if(!CherryChecker.isNullOrEmpty(oldDepartName) && !oldDepartName.equalsIgnoreCase(form.getDepartName())) {
				map.put("oldDepartName", oldDepartName);
				binOLBSWEM04_BL.updatePHPDepartName(map);//调用PHP接口修改微店名称
				binOLBSWEM04_BL.tran_setReservedCodeInvalid(map);//修改预留号的可用状态
			}
			String agentOpenID = agentInfo.getAgentOpenID();
			if(!CherryChecker.isNullOrEmpty(agentOpenID, true)) {
				binOLBSWEM04_BL.takeAgentInterface(map);
			}
			binOLCM43_BL.tran_updateAgent(agentInfo);
			this.addActionMessage(getText("ICM00002"));
		} catch(Exception e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
			this.addActionError(e.getMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	private Map<String, Object> getUpdateAgentMap() {
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, Object> agentInfoMap = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo)session.get(CherryConstants.SESSION_USERINFO);
		//取指定上级信息
		if(!CherryChecker.isNullOrEmpty(form.getSuperMobile())) {
			Map<String, Object> superAgentMap = binOLCM43_BL.getAgentInfo(form.getSuperMobile());
			if(!CherryChecker.isNullOrEmpty(superAgentMap, true)) {
				String supEmpPath = ConvertUtil.getString(superAgentMap.get("empPath"));
				String supOrgPath = ConvertUtil.getString(superAgentMap.get("orgPath"));
				if(!CherryChecker.isNullOrEmpty(supEmpPath, true)) {
					map.put("supEmpPath", supEmpPath);
				} 
				if(!CherryChecker.isNullOrEmpty(supOrgPath, true)) {
					map.put("supOrgPath", supOrgPath);
				}
			}
		}
		if(!CherryChecker.isNullOrEmpty(form.getOldAgentMobile(), true)) {
			agentInfoMap = binOLCM43_BL.getAgentInfo(form.getOldAgentMobile());
		} else {
			agentInfoMap = binOLCM43_BL.getAgentInfo(form.getAgentMobile());
		}
		String agentOpenID = ConvertUtil.getString(agentInfoMap.get("agentOpenID"));
		String empPath = ConvertUtil.getString(agentInfoMap.get("empPath"));
		String orgPath = ConvertUtil.getString(agentInfoMap.get("orgPath"));
		String employeeId = ConvertUtil.getString(agentInfoMap.get("employeeId"));
		String departId = ConvertUtil.getString(agentInfoMap.get("departId"));
		String oldSuperMobile = ConvertUtil.getString(agentInfoMap.get("superMobile"));
		String employeeCode = ConvertUtil.getString(agentInfoMap.get("employeeCode"));
		
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.UPDATEDBY, userInfo.getEmployeeCode());
		map.put(CherryConstants.UPDATEPGM, "BINOLBSWEM04_Action");
		map.put("agentMobile", form.getAgentMobile());
		map.put("oldAgentMobile", form.getOldAgentMobile());
		map.put("agentName", form.getAgentName());
		map.put("agentLevel", form.getAgentLevel());//部门类型
		map.put("superMobile", form.getSuperMobile());
		map.put("oldSuperMobile", oldSuperMobile);
		map.put("provinceId", form.getProvinceId());
		map.put("cityId", form.getCityId());
		String agentProvince = binOLCM08_BL.getRegionNameById(form.getProvinceId());
		String agentCity = binOLCM08_BL.getRegionNameById(form.getCityId());
		map.put("agentProvince", agentProvince);
		map.put("agentCity", agentCity);
		map.put("agentOpenID", agentOpenID);
		map.put("empPath", empPath);
		map.put("orgPath", orgPath);
		map.put("employeeId", employeeId);
		map.put("departId", departId);
		map.put("departName", form.getDepartName());
		map.put("employeeCode", employeeCode);
		String auditFlag = request.getParameter("auditFlag");
		if(!CherryChecker.isNullOrEmpty(auditFlag, true)) {
			map.put("agentApplyId", request.getParameter("agentApplyId"));//审核时的单据号
			map.put("auditEmployeeCode", userInfo.getEmployeeCode());
			map.put("auditFlag", auditFlag);
			map.put("reason", request.getParameter("reason"));//理由
		}
		return map;
	}

	public void validateSave() {
		Map<String, Object> map = getUpdateAgentMap();//参数map
		String organizationInfoId = ConvertUtil.getString(map.get("organizationInfoId"));
		String brandInfoId = ConvertUtil.getString(map.get("brandInfoId"));
		String agentMobile = form.getAgentMobile();
		String oldAgentMobile = form.getOldAgentMobile();
		String agentName = form.getAgentName();
		String agentLevel = form.getAgentLevel();
		String superMobile = form.getSuperMobile();
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		//微商手机号验证
		if(CherryChecker.isNullOrEmpty(agentMobile, true)){
			this.addFieldError("agentMobile", getText("BSWEM0001"));
		} else if (!CherryChecker.isPhoneValid(agentMobile, mobileRule)) {
			this.addFieldError("agentMobile", getText("EBS00004"));
		} else if(!"".equals(oldAgentMobile) && null != oldAgentMobile && !agentMobile.equalsIgnoreCase(oldAgentMobile)) {
			//判断手机号是否存在
			Map<String, Object> mobilePhoneExistsMap = binOLCM43_BL.getAgentExistByMobile(agentMobile);
			if(!CherryChecker.isNullOrEmpty(mobilePhoneExistsMap, true)) {
				//微商手机号已存在
				this.addFieldError("agentMobile", getText("ECM00032", new String[] { getText("WEM00001") }));
			}
		}
		//审核不通过时上级可为空
		String auditFlag = ConvertUtil.getString(map.get("auditFlag"));
		if(!CherryChecker.isNullOrEmpty(auditFlag) && "2".equals(auditFlag)) {
			
		} else {
			//名称不能为空
			if(CherryChecker.isNullOrEmpty(agentName, true)) {
				this.addFieldError("agentName", getText("BSWEM0002"));
			}
			
			//部门类型不能为空
			if(CherryChecker.isNullOrEmpty(agentLevel, true)) {
				this.addFieldError("agentLevel", getText("BSWEM0003"));
			} else {
				//上级手机号不能为空，且改手机号对应的上级级别必须大于选择的部门
				if(CherryChecker.isNullOrEmpty(superMobile, true)) {
					this.addFieldError("superMobile", getText("BSWEM0004"));
				} else {
					//查询上级的信息
					Map<String, Object> superAgentMap = binOLCM43_BL.getAgentInfo(superMobile);
					if(CherryChecker.isNullOrEmpty(superAgentMap, true)) {
						this.addFieldError("superMobile", getText("BSWEM0005"));
					} else {
						String superAgentLevel = ConvertUtil.getString(superAgentMap.get("agentLevel"));
						if(CherryChecker.isNullOrEmpty(superAgentLevel, true)) {
							this.addFieldError("superMobile", getText("BSWEM0006"));
						} else {
							String superLevel = codeTable.propGrade("1000", superAgentLevel);
							String level = codeTable.propGrade("1000", agentLevel);
							//部门类型等级大于上级等级
							if(superLevel.compareToIgnoreCase(level) >= 0) {
								this.addFieldError("superMobile", getText("BSWEM0007"));
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * 判断下级人数
	 * @return
	 * @throws Exception
	 */
	public String getSubAmount() throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String organizationInfoId = String.valueOf(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = String.valueOf(userInfo.getBIN_BrandInfoID());
		String employeeId = ConvertUtil.getString(request.getParameter("employeeId"));
		if(null != employeeId && !"".equals(employeeId)) {
			map.put("organizationInfoId", organizationInfoId);
			map.put("brandInfoId", brandInfoId);
			map.put("employeeId", employeeId);
			try {
				int limit = binOLBSWEM01_BL.getLimitAmout(map);
				map.clear();
				if(binOLBSWEM01_BL.getSubAmount(employeeId) > limit) {
					map.put("resultCode", "-1");
				} else {
					map.put("resultCode", "0");
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				map.put("resultCode", "-1");
			}
		}
		ConvertUtil.setResponseByAjax(response, map);
		return null;
	}
	
	/**
	 * 选择预留号初始化
	 * @return
	 */
	public String selectReservedCodeInit() {
		return SUCCESS;
	}
	
	/**
	 * 查询有效的预留号
	 */
	public String selectReservedCodeSearch() {
		Map<String, Object> map = new HashMap<String, Object>();
		UserInfo userInfo = (UserInfo) session.get(CherryConstants.SESSION_USERINFO);
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		map.put(CherryConstants.ORGANIZATIONINFOID, organizationInfoId);
		map.put(CherryConstants.BRANDINFOID, brandInfoId);
		map.put("ReservedCode", form.getReservedCode());
		map.put("ValidFlag", "1");
		ConvertUtil.setForm(form, map);
		int count = binOLBSWEM04_BL.getReservedCodeCount(map);
		if(count > 0) {
			reservedCodeList = binOLBSWEM04_BL.getReservedCodeList(map);
		}
		form.setITotalDisplayRecords(count);
		form.setITotalRecords(count);
		return SUCCESS;
	}
	
	@Override
	public BINOLBSWEM04_Form getModel() {
		
		return form;
	}

	public List getReginList() {
		return reginList;
	}

	public void setReginList(List reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getWechatMerchantList() {
		return wechatMerchantList;
	}

	public void setWechatMerchantList(List<Map<String, Object>> wechatMerchantList) {
		this.wechatMerchantList = wechatMerchantList;
	}

	public Map<String, Object> getAgentInfoMap() {
		return agentInfoMap;
	}

	public void setAgentInfoMap(Map<String, Object> agentInfoMap) {
		this.agentInfoMap = agentInfoMap;
	}

	public List getAgentLevelList() {
		return agentLevelList;
	}

	public void setAgentLevelList(List agentLevelList) {
		this.agentLevelList = agentLevelList;
	}

	public Map<String, Object> getSuperAgentInfoMap() {
		return superAgentInfoMap;
	}

	public void setSuperAgentInfoMap(Map<String, Object> superAgentInfoMap) {
		this.superAgentInfoMap = superAgentInfoMap;
	}

	public List<Map<String, Object>> getReservedCodeList() {
		return reservedCodeList;
	}

	public void setReservedCodeList(List<Map<String, Object>> reservedCodeList) {
		this.reservedCodeList = reservedCodeList;
	}

}
