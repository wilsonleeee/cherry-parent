/*		
 * @(#)BINOLSSPRM04_Action.java     1.0 2010/11/23		
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
package com.cherry.ss.prm.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM14_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.core.PropertiesUtil;
import com.cherry.ss.prm.bl.BINOLSSPRM04_BL;
import com.cherry.ss.prm.form.BINOLSSPRM04_Form;
import com.opensymphony.xwork2.ModelDriven;

@SuppressWarnings("unchecked")
public class BINOLSSPRM04_Action extends BaseAction implements
		ModelDriven<BINOLSSPRM04_Form> {

	private static final long serialVersionUID = 6146373617460632392L;

	@Resource
	private BINOLSSPRM04_BL binolssprm04_BL;
	
	/** 系统配置项 共通BL */
	@Resource
	private BINOLCM14_BL binOLCM14_BL;

	/** 参数FORM */
	private BINOLSSPRM04_Form form = new BINOLSSPRM04_Form();

	/** 促销品信息List */
	private Map prmInfo;

	/** 促销品销售价格List */
	private List prmSalePriceList;

	/** 取得部门机构促销产品价格List */
	private List prmPriceDepartList;

	/** 促销品厂商List */
	private List prmFacList;

	/** 促销品扩展信息 */
	private List prmExtList;

	/** 图片路径 */
	private String imagePath;
	
	/** 积分兑礼促销品是否需要下发价格标志*/
	private String sendFlag;

	public BINOLSSPRM04_Form getModel() {
		return form;
	}

	public Map getPrmInfo() {
		return prmInfo;
	}

	public void setPrmInfo(Map prmInfo) {
		this.prmInfo = prmInfo;
	}
	
	public List getPrmSalePriceList() {
		return prmSalePriceList;
	}

	public void setPrmSalePriceList(List prmSalePriceList) {
		this.prmSalePriceList = prmSalePriceList;
	}

	public List getPrmPriceDepartList() {
		return prmPriceDepartList;
	}

	public void setPrmPriceDepartList(List prmPriceDepartList) {
		this.prmPriceDepartList = prmPriceDepartList;
	}

	public List getPrmFacList() {
		return prmFacList;
	}

	public void setPrmFacList(List prmFacList) {
		this.prmFacList = prmFacList;
	}

	public List getPrmExtList() {
		return prmExtList;
	}

	public void setPrmExtList(List prmExtList) {
		this.prmExtList = prmExtList;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getSendFlag() {
		return sendFlag;
	}

	public void setSendFlag(String sendFlag) {
		this.sendFlag = sendFlag;
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
		// 取得参数MAP
		Map<String, Object> map = new HashMap<String, Object>();
		// 用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 用户ID
		map.put(CherryConstants.USERID, userInfo.getBIN_UserID());
		// 语言类型
		map.put(CherryConstants.SESSION_LANGUAGE, session
				.get(CherryConstants.SESSION_LANGUAGE));
		// 组织id
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		// 促销产品ID
		map.put("promotionProId", form.getPromotionProId());
		// 促销品信息
		prmInfo = binolssprm04_BL.searchPrmInfo(map);
		if (null != prmInfo && !prmInfo.isEmpty()) {
//			// 促销品销售价格
//			prmSalePriceList = binolssprm04_BL.searchPrmSalePriceList(map);
//			// 取得部门机构促销产品价格List
//			prmPriceDepartList = binolssprm04_BL.searchPrmPriceDepartList(map);
			// 只取有效的促销品厂商信息
			map.put("validFlagKbn", "1");
			// 促销品厂商List
			prmFacList = binolssprm04_BL.searchPrmFacList(map);
			// 促销品扩展信息
			prmExtList = binolssprm04_BL.searchPrmExtList(map);
			// 图片路径
			imagePath = PropertiesUtil.pps.getProperty("uploadFilePath.upImage");
			// 取得系统配置项积分兑礼是否下发价格
			sendFlag = binOLCM14_BL.getConfigValue("1033", String.valueOf(map.get("organizationInfoId")), String.valueOf(form.getBrandInfoId()));
		}
		return SUCCESS;

	}

}
