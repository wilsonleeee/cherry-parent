/*
 * @(#)BINOLBSDEP03_Action.java     1.0 2010/10/27
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.bs.dep.bl.BINOLBSDEP02_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP03_BL;
import com.cherry.bs.dep.bl.BINOLBSDEP04_BL;
import com.cherry.bs.dep.form.BINOLBSDEP03_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM00_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 更新部门画面Action
 * 
 * @author WangCT
 * @version 1.0 2010.10.27
 */
@SuppressWarnings("unchecked")
public class BINOLBSDEP03_Action extends BaseAction implements ModelDriven<BINOLBSDEP03_Form> {

	private static final long serialVersionUID = -8820499483166605894L;
	private static final Logger logger = LoggerFactory.getLogger(BINOLBSDEP03_Action.class);
	
	/** 更新部门画面BL */
	@Resource(name="binOLBSDEP03_BL")
	private BINOLBSDEP03_BL binOLBSDEP03_BL;
	
	/** 部门详细画面BL */
	@Resource(name="binOLBSDEP02_BL")
	private BINOLBSDEP02_BL binOLBSDEP02_BL;
	
	/** 添加部门画面BL */
	@Resource(name="binOLBSDEP04_BL")
	private BINOLBSDEP04_BL binOLBSDEP04_BL;
	
	/** 共通BL */
	@Resource(name="binOLCM00_BL")
	private BINOLCM00_BL binolcm00BL;
	
	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	
	/** 更新部门画面Form */
	private BINOLBSDEP03_Form form = new BINOLBSDEP03_Form();
	
	/**
	 * 
	 * 更新部门画面初期处理
	 * 
	 * @return 更新部门画面 
	 * @throws Exception 
	 */
	public String init() throws Exception {
		
		Map<String, Object> map = new HashMap<String, Object>();
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属品牌code
		map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		// 部门ID
		map.put(CherryConstants.ORGANIZATIONID, form.getOrganizationId());
		// 查询部门信息
		organizationInfo = binOLBSDEP02_BL.getOrganizationInfo(map);
		if(organizationInfo != null && !organizationInfo.isEmpty()) {
			// 查询部门地址List
			departAddressList = binOLBSDEP02_BL.getDepartAddressList(map);
			// 取得部门联系人List
			departContactList = binOLBSDEP02_BL.getDepartContactList(map);
			// 语言
			String language = (String)session.get(CherryConstants.SESSION_LANGUAGE);
			if(language != null) {
				map.put(CherryConstants.SESSION_LANGUAGE, language);
			}
			// 所属组织
			map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, organizationInfo.get(CherryConstants.BRANDINFOID));
			// 取得区域List
			reginList = binolcm00BL.getReginList(map);
			
			// 取得省ID
			Object provinceId = (Object)organizationInfo.get("provinceId");
			// 取得城市ID
			Object cityId = (Object)organizationInfo.get("cityId");
			// 省存在的场合，取得城市List
			if(provinceId != null && !"".equals(provinceId.toString())) {
				// 区域Id
				map.put("regionId", provinceId);
				cityList = binolcm00BL.getChildRegionList(map);
			}
			// 城市存在的场合，取得县级市List
			if(cityId != null && !"".equals(cityId.toString())) {
				// 区域Id
				map.put("regionId", cityId);
				countyList = binolcm00BL.getChildRegionList(map);
			}
		}
		// 是否支持部门协同
		form.setMaintainOrgSynergy(binOLCM14_BL.isConfigOpen("1371",
							userInfo.getBIN_OrganizationInfoID(),
							userInfo.getBIN_BrandInfoID()));
		return SUCCESS;
	}
	
	/**
	 * 
	 * 更新部门信息处理
	 * 
	 * @return 更新完了画面
	 */
	public String updateOrganizationInfo() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getBIN_UserID());
		// 作成程序名
		map.put(CherryConstants.CREATEPGM, "BINOLBSDEP03");
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getBIN_UserID());
		// 更新程序名
		map.put(CherryConstants.UPDATEPGM, "BINOLBSDEP03");
		
		// 到期日
		if(CherryChecker.isNullOrEmpty(form.getExpiringDateDate()) && CherryChecker.isNullOrEmpty(form.getExpiringDateTime())){
			form.setExpiringDateDate(CherryConstants.longLongAfter);
			form.setExpiringDateTime(CherryConstants.maxTime);
		}
		map.put("expiringDate", form.getExpiringDateDate().trim() + " " + form.getExpiringDateTime());
		
		try {
			// 更新部门信息
			binOLBSDEP03_BL.tran_updateOrganizationInfo(map);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			// 更新失败场合
			if(e instanceof CherryException){
                CherryException temp = (CherryException)e;            
                this.addActionError(temp.getErrMessage());       
                return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
            }else{
                throw e;
            }    
		}
		this.addActionMessage(getText("ICM00001"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}
	
	/**
	 * 
	 * 更新部门前字段验证处理
	 * 
	 */
	public void validateUpdateOrganizationInfo() throws Exception {
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
		
//		// 到期日（年月日）
//		if(form.getExpiringDateDate() != null && !"".equals(form.getExpiringDateDate().trim())){
//			if(!CherryChecker.checkDate(form.getExpiringDateDate().trim())){
//				// 日期格式验证
//				this.addFieldError("expiringDate_date", getText("ECM00022",new String[] { getText("PBS00076") }));
//			}
//		}
//		// 到期日（时分秒）
//		if(form.getExpiringDateTime() != null && !"".equals(form.getExpiringDateTime())){
//			if(!CherryChecker.checkTime(form.getExpiringDateTime())){
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
			if(orgId != null && !"".equals(orgId) && !form.getOrganizationId().equals(orgId)) {
				this.addFieldError("departCode", getText("EBS00005"));
			}
		}
	}
	
	/** 部门信息 */
	private Map organizationInfo;
	
	/** 部门地址List */
	private List<Map<String, Object>> departAddressList;
	
	/** 部门联系人List */
	private List<Map<String, Object>> departContactList;
	
	/** 区域List */
	private List<Map<String, Object>> reginList;
	
	/** 城市List */
	private List<Map<String, Object>> cityList;
	
	/** 县级市List */
	private List<Map<String, Object>> countyList;

	public Map getOrganizationInfo() {
		return organizationInfo;
	}

	public void setOrganizationInfo(Map organizationInfo) {
		this.organizationInfo = organizationInfo;
	}

	public List<Map<String, Object>> getDepartAddressList() {
		return departAddressList;
	}

	public void setDepartAddressList(List<Map<String, Object>> departAddressList) {
		this.departAddressList = departAddressList;
	}

	public List<Map<String, Object>> getDepartContactList() {
		return departContactList;
	}

	public void setDepartContactList(List<Map<String, Object>> departContactList) {
		this.departContactList = departContactList;
	}

	public List<Map<String, Object>> getReginList() {
		return reginList;
	}

	public void setReginList(List<Map<String, Object>> reginList) {
		this.reginList = reginList;
	}

	public List<Map<String, Object>> getCityList() {
		return cityList;
	}

	public void setCityList(List<Map<String, Object>> cityList) {
		this.cityList = cityList;
	}

	public List<Map<String, Object>> getCountyList() {
		return countyList;
	}

	public void setCountyList(List<Map<String, Object>> countyList) {
		this.countyList = countyList;
	}

	@Override
	public BINOLBSDEP03_Form getModel() {
		return form;
	}

}
