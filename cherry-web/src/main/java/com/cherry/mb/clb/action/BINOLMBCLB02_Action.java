/*	
 * @(#)BINOLMBCLB02_Action.java     1.0 2014/04/29	
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
package com.cherry.mb.clb.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM05_BL;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryChecker;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.CherryException;
import com.cherry.cm.core.CodeTable;
import com.cherry.cm.util.Bean2Map;
import com.cherry.cm.util.ConvertUtil;
import com.cherry.cp.common.CampConstants;
import com.cherry.mb.clb.form.BINOLMBCLB02_Form;
import com.cherry.mb.clb.interfaces.BINOLMBCLB02_IF;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.xwork2.ModelDriven;

/**
 * 会员俱乐部添加Action
 * 
 * @author hub
 * @version 1.0 2014.04.29
 */
public class BINOLMBCLB02_Action extends BaseAction implements
ModelDriven< BINOLMBCLB02_Form>{

	private static final long serialVersionUID = -8557764115255513420L;
	
	private static Logger logger = LoggerFactory
			.getLogger(BINOLMBCLB02_Action.class.getName());
	
	@Resource
	private BINOLCM05_BL binOLCM05_BL;
	
	@Resource
	private BINOLMBCLB02_IF binolmbclb02IF;
	
	@Resource
	private BINOLCM14_BL binOLCM14_BL;
	
	@Resource(name="CodeTable")
	private CodeTable code;
	
	/** 参数FORM */
	private BINOLMBCLB02_Form form = new BINOLMBCLB02_Form();

	@Override
	public BINOLMBCLB02_Form getModel() {
		// TODO Auto-generated method stub
		return form;
	}
	
	/**
	 * <p>
	 * 会员俱乐部一览初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String init() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		form.setClubSetBy(String.valueOf(userInfo.getBIN_UserID()));
		// 品牌ID
		int brandInfoId = userInfo.getBIN_BrandInfoID();
		// 取得所管辖的品牌List
		List<Map<String, Object>> brandList = null;
		// 总部用户登录的时候
		if (CherryConstants.BRAND_INFO_ID_VALUE == brandInfoId) {
			map.put("noHeadKbn", "1");
			brandList = binOLCM05_BL.getBrandInfoList(map);
			if (null != brandList && !brandList.isEmpty()) {
				map.put("brandInfoId", ((Map<String, Object>) 
						brandList.get(0)).get("brandInfoId"));
			}
		} else {
			map.put("brandInfoId", brandInfoId);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 取得品牌名称
			brandMap.put("brandName", binOLCM05_BL.getBrandName(map));
			// 品牌ID
			brandMap.put("brandInfoId", ConvertUtil.getString(brandInfoId));
			// 取得所管辖的品牌List
			brandList = new ArrayList<Map<String, Object>>();
			brandList.add(brandMap);
		}
		form.setBrandInfoList(brandList);
		if (CherryChecker.isNullOrEmpty(form.getClubMod())) {
			// 俱乐部模式
			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
					.getBIN_OrganizationInfoID()), String.valueOf(brandInfoId));
			form.setClubMod(clubMod);
		}
		return "success";
	}
	
	/**
	 * <p>
	 * 编辑画面一览初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String edit() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		form.setClubSetBy(String.valueOf(userInfo.getBIN_UserID()));
		try {
			// 会员俱乐部ID
			map.put("memberClubId", form.getMemberClubId());
			// 取得会员俱乐部信息
			Map<String, Object> clubInfo = binolmbclb02IF.getClubInfo(map);
			// 品牌ID
			int brandInfoId = Integer.parseInt(clubInfo.get("brandInfoId").toString());
			map.put("brandInfoId", brandInfoId);
			Map<String, Object> brandMap = new HashMap<String, Object>();
			// 取得品牌名称
			brandMap.put("brandName", binOLCM05_BL.getBrandName(map));
			// 品牌ID
			brandMap.put("brandInfoId", ConvertUtil.getString(brandInfoId));
			// 取得所管辖的品牌List
			List<Map<String, Object>> brandList = new ArrayList<Map<String, Object>>();
			brandList.add(brandMap);
			form.setBrandInfoList(brandList);
			form.setClubInfo(clubInfo);
			// 俱乐部模式
			String clubMod = null;
			if (CherryChecker.isNullOrEmpty(form.getClubMod())) {
				// 俱乐部模式
				clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
						.getBIN_OrganizationInfoID()), String.valueOf(brandInfoId));
				form.setClubMod(clubMod);
			}
			// 产品模式
			if ("2".equals(clubMod)) {
				// 取得会员俱乐部对应关系列表
				List<Map<String, Object>> clubBrandList = binolmbclb02IF.getClubBrandList(map);
				if (null != clubBrandList) {
					for (Map<String, Object> clubBrandInfo : clubBrandList) {
						// 子品牌代码
						String key = (String) clubBrandInfo.get("originalBrand");
						// 子品牌名称
						String name = null;
						if ("ALL".equals(key)) {
							name = "全部子品牌";
						} else {
							// 子品牌名称
							name = code.getVal("1299", key);
						}
						clubBrandInfo.put("origBrandName", name);
					}
				}
				// 所选择的子品牌列表
				form.setOrigBrandList(clubBrandList);
			} else {
				// 取得活动地点JSON
				String placeJson = binolmbclb02IF.getPlaceJson(clubInfo, map);
				clubInfo.put("placeJson", placeJson);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return CherryConstants.GLOBAL_ERROR;
		}
		return "success";
	}
	
	/**
	 * <p>
	 * 详细画面一览初期显示
	 * </p>
	 * 
	 * 
	 * @param 无
	 * @return String 跳转页面
	 * 
	 */
	public String detail() throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		try {
			// 会员俱乐部ID
			map.put("memberClubId", form.getMemberClubId());
			// 取得会员俱乐部信息
			Map<String, Object> clubInfo = binolmbclb02IF.getClubInfo(map);
			// 品牌ID
			int brandInfoId = Integer.parseInt(clubInfo.get("brandInfoId").toString());
			map.put("brandInfoId", brandInfoId);
			// 取得品牌名称
			form.setBrandName(binOLCM05_BL.getBrandName(map));
			form.setClubInfo(clubInfo);
			// 俱乐部模式
			String clubMod = binOLCM14_BL.getConfigValue("1299", String.valueOf(userInfo
						.getBIN_OrganizationInfoID()), String.valueOf(brandInfoId));
			// 产品模式
			if ("2".equals(clubMod)) {
				// 取得会员俱乐部对应关系列表
				List<Map<String, Object>> clubBrandList = binolmbclb02IF.getClubBrandList(map);
				if (null != clubBrandList) {
					for (Map<String, Object> clubBrandInfo : clubBrandList) {
						// 子品牌代码
						String key = (String) clubBrandInfo.get("originalBrand");
						// 子品牌名称
						String name = null;
						if ("ALL".equals(key)) {
							name = "全部子品牌";
						} else {
							// 子品牌名称
							name = code.getVal("1299", key);
						}
						clubBrandInfo.put("origBrandName", name);
					}
				}
				// 所选择的子品牌列表
				form.setOrigBrandList(clubBrandList);
			} else {
				String locationType = (String) clubInfo.get("locationType");
				if (!CampConstants.LOTION_TYPE_0.equals(locationType)){
					String palceJson = (String) clubInfo.get(CampConstants.PLACE_JSON);
					List<Map<String, Object>> palceList = (List<Map<String, Object>>) JSONUtil
							.deserialize(palceJson);
					form.setPlaceList(palceList);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return CherryConstants.GLOBAL_ERROR;
		}
		return "success";
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
    public String save() throws Exception{
    	// 验证提交的参数
		if (!validateForm()) {
			return CherryConstants.GLOBAL_ACCTION_RESULT;
		}
    	Map<String, Object> map = (Map<String, Object>)Bean2Map.toHashMap(form);
    	// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo
				.getBIN_OrganizationInfoID());
		try {
			// 保存俱乐部
			binolmbclb02IF.tran_saveClub(map);
		} catch (CherryException e) {
			this.addActionError(e.getErrMessage());
			return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
		}
		// 处理成功
		this.addActionMessage(getText("ICM00002"));
		return CherryConstants.GLOBAL_ACCTION_RESULT_BODY;
    }
    
    /**
   	 * 验证提交的参数
   	 * 
   	 * @param 无
   	 * @return boolean 验证结果
   	 * @throws Exception
   	 * 
   	 */
   	private boolean validateForm() throws Exception {
   		// 验证结果
   		boolean isCorrect = true;
   		// 俱乐部名称
   		String clubName = form.getClubName();
   		Map<String, Object> ckMap = new HashMap<String, Object>();
   		int clubId = 0;
   		int memClubId = 0;
   		if (!CherryChecker.isNullOrEmpty(form.getMemberClubId())) {
   			clubId = Integer.parseInt(form.getMemberClubId());
   		}
   		// 俱乐部名称必须入力验证
   		if (CherryChecker.isNullOrEmpty(clubName, true)) {
   			this.addFieldError("clubName",
   					getText("ECM00009", new String[] { getText("PMB01001") }));
   			isCorrect = false;
   		} else if (clubName.length() > 50) {
   			// 俱乐部名称不能超过50位验证
   			this.addFieldError(
   					"clubName",
   					getText("ECM00020", new String[] { getText("PMB01001"),
   							"50" }));
   			isCorrect = false;
   		} else {
	   		ckMap.put("clubName", clubName);
	   		Map<String, Object> idMap = binolmbclb02IF.getClubId(ckMap);
	   		if (null != idMap && !idMap.isEmpty()) {
	   			memClubId = Integer.parseInt(String.valueOf(idMap.get("memberClubId")));
	   		}
	   		if (0 != memClubId && clubId != memClubId) {
	   			// 俱乐部名称重复
	   			this.addFieldError(
	   					"clubName",
	   					getText("ECM00032", new String[] { getText("PMB01001")}));
	   			isCorrect = false;
	   		}
   		}
   		// 俱乐部代码
   		String clubCode = form.getClubCode();
   		// 俱乐部代码必须入力验证
   		if (CherryChecker.isNullOrEmpty(clubCode, true)) {
   			this.addFieldError("clubCode",
   					getText("ECM00009", new String[] { getText("PMB01002")}));
   			isCorrect = false;
   		} else if (clubCode.length() > 10) {
   			// 俱乐部代码不能超过10位验证
   			this.addFieldError(
   					"clubCode",
   					getText("ECM00020", new String[] { getText("PMB01002"),
   							"10" }));
   			isCorrect = false;
   		} else {
	   		ckMap.remove("clubName");
	   		ckMap.put("clubCode", clubCode);
	   		memClubId = 0;
	   		Map<String, Object> idMap = binolmbclb02IF.getClubId(ckMap);
	   		if (null != idMap && !idMap.isEmpty()) {
	   			memClubId = Integer.parseInt(String.valueOf(idMap.get("memberClubId")));
	   		}
	   		if (0 != memClubId && clubId != memClubId) {
		   			// 俱乐部代码重复
		   			this.addFieldError(
		   					"clubCode",
		   					getText("ECM00032", new String[] { getText("PMB01002")}));
		   			isCorrect = false;
//		   		} else {
//		   			// 已下发
//		   			if (!"0".equals(idMap.get("sendFlag")) && !clubCode.equals(idMap.get("clubCode"))) {
//		   				// 俱乐部代码已下发不能修改
//			   			this.addFieldError(
//			   					"clubCode",
//			   					 "俱乐部已下发不能修改代码");
//			   			isCorrect = false;
//		   			}
//		   		}
	   		}
   		}
   		// 俱乐部描述
   		String descriptionDtl = form.getDescriptionDtl();
   		if (!CherryChecker.isNullOrEmpty(descriptionDtl, true) 
   				&& descriptionDtl.length() > 300) {
   			// 俱乐部描述不能超过300位验证
   			this.addFieldError(
   					"descriptionDtl",
   					getText("ECM00020", new String[] { getText("PMB01003"),
   							"300" }));
   			isCorrect = false;
   		}
   		if ("2".equals(form.getClubMod())) {
	   		// 子品牌列表
	   		String origBrands = form.getOrigBrands();
	   		boolean isBrand = true;
	   		if (CherryChecker.isNullOrEmpty(origBrands, true)) {
	   			isBrand = false;
	   			isCorrect = false;
	   		} else {
	   			List<Map<String, Object>> brandList = (List<Map<String, Object>>) JSONUtil.deserialize(origBrands);
	   			if (null == brandList || brandList.isEmpty()) {
	   				isBrand = false;
	   	   			isCorrect = false;
	   			}
	   		}
	   		if (!isBrand) {
	   			this.addActionError(getText("PMB01004"));
	   		}
   		} else {
   			if (CherryChecker.isNullOrEmpty(form.getPlaceJson(), true)) {
   				isCorrect = false;
   				this.addActionError(getText("PMB01005"));
   			}
   		}
   		return isCorrect;
   	}
}
