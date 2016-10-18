/*

 * @(#)BINOLBSDEP04_Action.java     1.0 2010/10/27
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

package com.cherry.bs.dep.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.dep.bl.BINOLBSDEP04_BL;
import com.cherry.bs.dep.form.BINOLBSDEP04_Form;
import com.cherry.bs.dep.service.BINOLBSDEP04_Service;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM15_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 添加部门画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP04_Action extends BaseAction implements ModelDriven<BINOLBSDEP04_Form> {
	
	private static final long serialVersionUID = -5892587447043194619L;
	
	/** 添加部门画面BL */
	@Resource(name="binOLBSDEP04_BL")
	private BINOLBSDEP04_BL binOLBSDEP04_BL;
	
	/** 添加部门画面Service */
	@Resource
	private BINOLBSDEP04_Service binOLBSDEP04_Service;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="binOLCM15_BL")
	private BINOLCM15_BL binOLCM15_BL;
	
	@Resource(name="binOLCM05_BL")
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource(name="CodeTable")
	private CodeTable code;
	
	/** 添加部门画面Form */
	private BINOLBSDEP04_Form form = new BINOLBSDEP04_Form();

	/**
	 * 
	 * 添加部门画面初期处理
	 * 
	 * @return 添加部门画面 
	 */
	public String init() {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		
		// 总部用户的场合
		if(userInfo.getBIN_BrandInfoID() == CherryConstants.BRAND_INFO_ID_VALUE) {
			// 取得品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 品牌ID
			brandMap.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandMap.put("brandName", userInfo.getBrandName());
			brandInfoList = new ArrayList<Map<String, Object>>();
			brandInfoList.add(brandMap);
		}
		
		// 品牌存在的场合
		if(brandInfoList != null && !brandInfoList.isEmpty()) {
			String brandInfoId = String.valueOf(brandInfoList.get(0).get(CherryConstants.BRANDINFOID));
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, brandInfoId);
			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
			// 取得仓库信息List
			depotInfoList = binOLBSDEP04_BL.getDepotInfoList(map);
			
			if(binOLCM14_BL.isConfigOpen("1007", String.valueOf(userInfo.getBIN_OrganizationInfoID()), brandInfoId)) {
				Map codeMap = code.getCode("1120","0");
				map.put("type", "0");
				map.put("length", codeMap.get("value2"));
				// 作成者
				map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
				// 更新者
				map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
				// 作成模块
				map.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
				// 更新模块
				map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
				// 自动生成部门代码
				form.setDepartCode((String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(map));
			}
		}
		
		// 到期日 : 默认 2100-01-01 00:00:00.000
//		form.setExpiringDate_date(CherryConstants.longLongAfter);
//		form.setExpiringDate_time(CherryConstants.maxTime);

		// 是否支持部门协同
		form.setMaintainOrgSynergy(binOLCM14_BL.isConfigOpen("1371",
							userInfo.getBIN_OrganizationInfoID(),
							userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	
	/**
	 * 根据品牌ID筛选下拉列表
	 * 
	 */
	public String filterByBrandInfo() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言
		String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
		if(language != null) {
			map.put(CherryConstants.SESSION_LANGUAGE, language);
		}
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 所属品牌
		map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		//  取得区域List
		resultMap.put("reginList", binolcm00BL.getReginList(map));
		// 取得仓库信息List
		resultMap.put("depotInfoList", binOLBSDEP04_BL.getDepotInfoList(map));
		if(binOLCM14_BL.isConfigOpen("1007", String.valueOf(userInfo.getBIN_OrganizationInfoID()), form.getBrandInfoId())) {
			Map codeMap = code.getCode("1120","0");
			map.put("type", "0");
			map.put("length", codeMap.get("value2"));
			// 作成者
			map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
			// 更新者
			map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());
			// 作成模块
			map.put(CherryConstants.CREATEPGM, "BINOLBSEMP04");
			// 更新模块
			map.put(CherryConstants.UPDATEPGM, "BINOLBSEMP04");
			// 自动生成部门代码
			resultMap.put("departCode", (String)codeMap.get("value1")+binOLCM15_BL.getSequenceId(map));
		}
		ConvertUtil.setResponseByAjax(response, resultMap);
		return null;
	}
	
	/**
	 * 
	 * 添加部门处理
	 * 
	 * @return 添加成功画面
	 */
	public String addOrganization() throws Exception {

		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSDEP04");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP04");
		
		// 到期日
		if(CherryChecker.isNullOrEmpty(form.getExpiringDateDate()) && CherryChecker.isNullOrEmpty(form.getExpiringDateTime())){
			form.setExpiringDateDate(CherryConstants.longLongAfter);
			form.setExpiringDateTime(CherryConstants.maxTime);
		}
		map.put("expiringDate", form.getExpiringDateDate().trim() + " " + form.getExpiringDateTime());
		
		// 添加部门处理
		binOLBSDEP04_BL.tran_addOrganization(map);
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 添加部门前字段验证处理
	 * 
	 */
	public void validateAddOrganization() throws Exception {
		
		// 部门代码必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getDepartCode())) {
			this.addFieldError("departCode", getText("ECM00009",new String[]{getText("PBS00001")}));
		} else {
			// 部门代码不能超过5位验证
			if(form.getDepartCode().length() > 15) {
				this.addFieldError("departCode", getText("ECM00020",new String[]{getText("PBS00001"),"15"}));
			}
		}
		// 部门名称必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getDepartName())) {
			this.addFieldError("departName", getText("ECM00009",new String[]{getText("PBS00002")}));
		} else {
			// 部门名称不能超过30位验证
			if(form.getDepartName() != null && form.getDepartName().length() > 30) {
				this.addFieldError("departName", getText("ECM00020",new String[]{getText("PBS00002"),"30"}));
			}
		}
		// 部门类型必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getType())) {
			this.addFieldError("type", getText("ECM00009",new String[]{getText("PBS00038")}));
		}
		// 品牌必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getBrandInfoId())) {
			this.addFieldError("brandInfoId", getText("ECM00009",new String[]{getText("PBS00048")}));
		}
		// 测试区分必须入力验证
		if(CherryChecker.isNullOrEmpty(form.getTestType())) {
			this.addFieldError("testType", getText("ECM00009",new String[]{getText("PBS00060")}));
		}
		// 部门简称不能超过20位验证
		if(form.getDepartNameShort() != null && form.getDepartNameShort().length() > 20) {
			this.addFieldError("departNameShort", getText("ECM00020",new String[]{getText("PBS00003"),"20"}));
		}
		// 部门英文名称不能超过30位验证
		if(form.getNameForeign() != null && form.getNameForeign().length() > 30) {
			this.addFieldError("nameForeign", getText("ECM00020",new String[]{getText("PBS00004"),"30"}));
		}
		// 部门英文简称不能超过20位验证
		if(form.getNameShortForeign() != null && form.getNameShortForeign().length() > 20) {
			this.addFieldError("nameShortForeign", getText("ECM00020",new String[]{getText("PBS00005"),"20"}));
		}
		// 到期日（年月日）
		boolean bolExpir_date = CherryChecker.isNullOrEmpty(form.getExpiringDateDate());
		boolean bolExpir_time = CherryChecker.isNullOrEmpty(form.getExpiringDateTime());
		
		if(bolExpir_date && !bolExpir_time){
			this.addFieldError("expiringDateTime", getText("ECM00097",new String[] { getText("PBS00078") }));
		}
		if(!bolExpir_date && bolExpir_time){
			this.addFieldError("expiringDateTime", getText("ECM00097",new String[] { getText("PBS00078") }));
		}
		
//		if(form.getExpiringDate_date() != null && !"".equals(form.getExpiringDate_date().trim())){
//			if(!CherryChecker.checkDate(form.getExpiringDate_date().trim())){
//				// 日期格式验证
//				this.addFieldError("expiringDate_date", getText("ECM00022",new String[] { getText("PBS00076") }));
//			}
//		}
//		// 到期日（时分秒）
//		if(form.getExpiringDate_time() != null && !"".equals(form.getExpiringDate_time())){
//			if(!CherryChecker.checkTime(form.getExpiringDate_time())){
//				// 时间格式验证
//				this.addFieldError("expiringDate_time", getText("ECM00026",new String[] { getText("PBS00077") }));
//			}
//		}
		// 部门地址信息
		String departAddress = form.getDepartAddress();
		if(departAddress != null && !"".equals(departAddress)) {
			List<Map<String, Object>> list = (List<Map<String, Object>>)JSONUtil.deserialize(departAddress);
			for(int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				// 地址1必须入力验证
				if(CherryChecker.isNullOrEmpty(map.get("addressLine1"))) {
					this.addFieldError("addressLine1_"+i, getText("ECM00009",new String[]{getText("PBS00009")}));
				} else {
					// 地址1不能超过100位验证
					if(map.get("addressLine1").toString().length() > 100) {
						this.addFieldError("addressLine1_"+i, getText("ECM00020",new String[]{getText("PBS00009"),"100"}));
					}
				}
				// 地址2不能超过100位验证
				if(map.get("addressLine2") != null && map.get("addressLine2").toString().length() > 100) {
					this.addFieldError("addressLine2_"+i, getText("ECM00020",new String[]{getText("PBS00010"),"100"}));
				}
				// 邮编验证
				if(map.get("zipCode") != null && !"".equals(map.get("zipCode"))) {
					if(!CherryChecker.isAlphanumeric(map.get("zipCode").toString())) {
						this.addFieldError("zipCode_"+i, getText("ECM00031",new String[]{getText("PBS00011")}));
					}
				}
			}
		}
		if(form.getIsCreateFlg() != null && "1".equals(form.getIsCreateFlg())) {
			// 仓库ID存在时
			if(form.getDepotInfoId() != null && !"".equals(form.getDepotInfoId())) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("depotInfoId", form.getDepotInfoId());
				// 取得仓库信息
				Map<String, Object> depotInfoMap = binOLBSDEP04_Service.getDepotInfo(map);
				Object testType = depotInfoMap.get("testType");
				if(testType == null || !form.getTestType().equals(testType.toString())) {
					if("0".equals(form.getTestType())) {
						this.addFieldError("depotInfoId", getText("EBS00049"));
					} else {
						this.addFieldError("depotInfoId", getText("EBS00048"));
					}
				}
			}
		}
		if(!this.hasFieldErrors()) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 登陆用户信息
			UserInfo userInfo = (UserInfo) session
					.get(CherryConstants.SESSION_USERINFO);
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, form.getBrandInfoId());
			// 部门代码
			map.put("departCode", form.getDepartCode());
			String orgId = binOLBSDEP04_BL.getOrganizationIdCheck(map);
			if(orgId != null && !"".equals(orgId)) {
				this.addFieldError("departCode", getText("EBS00005"));
			}
		}
	}
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 品牌List */
	private List<Map<String, Object>> brandInfoList;
	
	/** 仓库信息List */
	private List<Map<String, Object>> depotInfoList;

	public List<Map<String, Object>> getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List<Map<String, Object>> brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getDepotInfoList() {
		return depotInfoList;
	}

	public void setDepotInfoList(List<Map<String, Object>> depotInfoList) {
		this.depotInfoList = depotInfoList;
	}

	@Override
	public BINOLBSDEP04_Form getModel() {
		return form;
	}

}
