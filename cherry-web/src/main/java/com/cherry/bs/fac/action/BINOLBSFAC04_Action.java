/*	
 * @(#)BINOLBSFAC04_Action.java     1.0 2011/02/17	
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
package com.cherry.bs.fac.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.bs.fac.bl.BINOLBSFAC04_BL;
import com.cherry.bs.fac.form.BINOLBSFAC04_Form;
import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM08_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 
 * 生产厂商添加Action
 * 
 * 
 * 
 * @author lipc
 * @version 1.0 2011.02.17
 */
@SuppressWarnings("unchecked")
public class BINOLBSFAC04_Action extends BaseAction implements
		ModelDriven<BINOLBSFAC04_Form> {

	private static final long serialVersionUID = 4016429654550262821L;

	/** 参数FORM */
	private BINOLBSFAC04_Form form = new BINOLBSFAC04_Form();

	@Resource
	private BINOLCM05_BL binOLCM05_BL;

	@Resource
	private BINOLCM08_BL binOLCM08_BL;

	@Resource(name="binOLCM14_BL")
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource
	private BINOLBSFAC04_BL binolbsfac04BL;

	/** 品牌List */
	private List brandInfoList;

	/** 省份List */
	private List provinceList;
	
	private String mobileRule;

	@Override
	public BINOLBSFAC04_Form getModel() {
		return form;
	}

	public List getBrandInfoList() {
		return brandInfoList;
	}

	public void setBrandInfoList(List brandInfoList) {
		this.brandInfoList = brandInfoList;
	}

	public List getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List provinceList) {
		this.provinceList = provinceList;
	}

	public String getMobileRule() {
		return mobileRule;
	}

	public void setMobileRule(String mobileRule) {
		this.mobileRule = mobileRule;
	}

	/**
	 * <p>
	 * 画面初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		// 用户信息
		UserInfo userInfo = (UserInfo) session     
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织ID
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 手机号校验规则
		mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
					
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 所属组织ID
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 品牌ID
		map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 业务类型
		map.put(CherryConstants.BUSINESS_TYPE, CherryConstants.BUSINESS_TYPE0);
		// 操作类型
		map
				.put(CherryConstants.OPERATION_TYPE,
						CherryConstants.OPERATION_TYPE0);
		// 总部用户登录时
		if (CherryConstants.BRAND_INFO_ID_VALUE == userInfo
				.getBIN_BrandInfoID()) {
			// 取得所管辖的品牌List
			brandInfoList = binOLCM05_BL.getBrandInfoList(map);
		} else {
			// 品牌信息
			Map<String, Object> brandInfo = new HashMap<String, Object>();
			// 品牌ID
			brandInfo.put("brandInfoId", userInfo.getBIN_BrandInfoID());
			// 品牌名称
			brandInfo.put("brandName", userInfo.getBrandName());
			// 品牌List
			brandInfoList = new ArrayList();
			brandInfoList.add(brandInfo);
		}
		// 取得区域List
		provinceList = binOLCM08_BL.getProvinceList(map);

		return SUCCESS;
	}

	/**
	 * <p>
	 * 保存
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * @throws Exception
	 * 
	 */
	public String save() throws Exception {
		// 参数MAP取得
		Map<String, Object> map = getParamsMap();
		// 验证失败
		if (!validateSave(map)) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
		// 添加厂商到数据库
		binolbsfac04BL.tran_insertFactory(map);
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
	}

	/**
	 * 参数验证
	 * 
	 * @throws Exception
	 * 
	 */
	private boolean validateSave(Map<String, Object> map) throws Exception{
		// 验证结果
		boolean validResult = true;
		// ==== 厂商编号必须验证 ===== //
		String manufacturerCode = (String)map.get("manufacturerCode");
		if (CherryChecker.isNullOrEmpty(manufacturerCode)) {
			this.addFieldError("manufacturerCode", getText("ECM00009",
					new String[] { getText("PBS00020") }));
			validResult = false;
		} else if (!CherryChecker.isAlphanumeric(manufacturerCode)) {
			// 厂商编号英数验证
			this.addFieldError("manufacturerCode", getText("ECM00031",
					new String[] { getText("PBS00044") }));
			validResult = false;
		} else if (manufacturerCode.length() > 20) {
			// 厂商编号长度验证
			this.addFieldError("manufacturerCode", getText("ECM00020",
					new String[] { getText("PBS00044"), "20" }));
			validResult = false;
		} else {
			// 厂商ID取得
			String factoryId = binolbsfac04BL.getFactoryId(map);
			if (!CherryChecker.isNullOrEmpty(factoryId)) {
				// 厂商编号重复验证
				this.addFieldError("manufacturerCode", getText("ECM00032",
						new String[] { getText("PBS00044") }));
				validResult = false;
			}
		}
		// ==== 厂商名称验证 ===== //
		String factoryNameCN = (String) map.get("factoryNameCN");
		if (CherryChecker.isNullOrEmpty(factoryNameCN)) {
			this.addFieldError("factoryNameCN", getText("ECM00009",
					new String[] { getText("PBS00045") }));
			validResult = false;
		} else if(factoryNameCN.length() > 30) {
			this.addFieldError("factoryNameCN", getText("ECM00020",
					new String[] { getText("PBS00045"), "30" }));
			validResult = false;
		}
		// ==== 厂商简称长度验证 ===== //
		String factoryNameCNShort = (String)map.get("factoryNameCNShort");
		if (!CherryChecker.isNullOrEmpty(factoryNameCNShort)
				&& factoryNameCNShort.length() > 20) {
			this.addFieldError("factoryNameCNShort", getText("ECM00020",
					new String[] { getText("PBS00040"), "20" }));
			validResult = false;
		}
		// ==== 厂商英文名称长度验证 ===== //
		String factoryNameEN = (String)map.get("factoryNameEN");
		if (!CherryChecker.isNullOrEmpty(factoryNameEN)
				&& factoryNameEN.length() > 30) {
			this.addFieldError("factoryNameEN", getText("ECM00020",
					new String[] { getText("PBS00041"), "30" }));
			validResult = false;
		}
		// ==== 厂商英文简称长度验证 ===== //
		String factoryNameENShort = (String)map.get("factoryNameENShort");
		if (!CherryChecker.isNullOrEmpty(factoryNameENShort)
				&& factoryNameENShort.length() > 20) {
			this.addFieldError("factoryNameENShort", getText("ECM00020",
					new String[] { getText("PBS00042"), "20" }));
			validResult = false;
		}
		// ==== 法人代表长度验证 ===== //
		String legalPerson = (String)map.get("legalPerson");
		if (!CherryChecker.isNullOrEmpty(legalPerson)
				&& legalPerson.length() > 30) {
			this.addFieldError("legalPerson", getText("ECM00020",
					new String[] { getText("PBS00043"), "30" }));
			validResult = false;
		}
		// 联系电话入力验证
		String telePhone = (String)map.get("telePhone");
		if (!CherryChecker.isNullOrEmpty(telePhone)
				&& !CherryChecker.isTelValid(telePhone)) {
			this.addFieldError("telePhone", getText("EBS00001"));
			validResult = false;
		}
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织ID
		String organizationInfoId = ConvertUtil.getString(userInfo.getBIN_OrganizationInfoID());
		// 品牌ID
		String brandInfoId = ConvertUtil.getString(userInfo.getBIN_BrandInfoID());
		// 获取手机号验证规则
		String mobileRule = binOLCM14_BL.getConfigValue("1090", organizationInfoId, brandInfoId);
		// 手机号码数字验证
		String mobile = (String)map.get("mobile");
		if (!CherryChecker.isNullOrEmpty(mobile)
				&& !CherryChecker.isPhoneValid(mobile, mobileRule)) {
			this.addFieldError("mobile", getText("EBS00004"));
			validResult = false;
		}
		// 地址信息入力验证
		String addressInfo = (String)map.get("addressInfo");
		if (!CherryChecker.isNullOrEmpty(addressInfo)) {
			// 员工地址List
			List<Map<String, String>> addrList = (List<Map<String, String>>) JSONUtil
					.deserialize(addressInfo);
			if (null != addrList) {
				for (int i = 0; i < addrList.size(); i++) {
					Map<String, String> addr = addrList.get(i);
					// 邮编格式验证
					if (!CherryChecker.isNullOrEmpty(addr.get("zipCode"))
							&& !CherryChecker.isZipValid(addr.get("zipCode"))) {
						this.addFieldError("zipCode_" + i, getText("ECM00023",
								new String[] { getText("PBS00011") }));
						validResult = false;
					}
					// 地址长度验证
					if (!CherryChecker.isNullOrEmpty(addr.get("address"))
							&& addr.get("address").length() > 100) {
						this.addFieldError("address_" + i, getText("ECM00020",
								new String[] { getText("PBS00024"), "100" }));
						validResult = false;
					}
					if ((null != addr.get("zipCode") || null != addr.get("addressTypeId") || 
							null != addr.get("provinceId") || null != addr.get("cityId"))
							&& null == addr.get("address")) {
						// 必须验证
						this.addFieldError("address_" + i, getText(
								"ECM00009", new String[] { getText("PBS00024") }));
						validResult = false;
					}
				}
			}
		}
		return validResult;
	}

	/**
	 * 参数MAP取得
	 * 
	 * @throws Exception
	 * 
	 */
	private Map<String, Object> getParamsMap() throws Exception {
		// form参数放入Map中
		Map<String, Object> map = (Map<String, Object>) Bean2Map
				.toHashMap(form);
		// 循环map剔除value值前后空格
		for(Map.Entry<String,Object> en: map.entrySet()){
			String value = ConvertUtil.getString(en.getValue()).trim();
			en.setValue(value);
		}
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		// 作成者
		map.put(CherryConstants.CREATEDBY, userInfo.getLoginName());
		// 更新者
		map.put(CherryConstants.UPDATEDBY, userInfo.getLoginName());

		return map;
	}

}
