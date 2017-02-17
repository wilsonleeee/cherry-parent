/*	
 * @(#)BINOLPLSCF16_Action.java     1.0 2013/01/15		
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
package com.cherry.pl.scf.action;

import com.cherry.cm.cmbeans.UserInfo;
import com.cherry.cm.cmbussiness.bl.BINOLCM27_BL;
import com.cherry.cm.core.BaseAction;
import com.cherry.cm.core.CherryConstants;
import com.cherry.cm.util.Bean2Map;
import com.cherry.pl.scf.form.BINOLPLSCF16_Form;
import com.opensymphony.xwork2.ModelDriven;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 会员索引管理画面Action
 * 
 * @author WangCT
 * @version 1.0 2013/01/15
 */
public class BINOLPLSCF16_Action extends BaseAction implements ModelDriven<BINOLPLSCF16_Form> {

	private static final long serialVersionUID = 17422651204023418L;
	
	/** 访问WebService共通BL **/
	@Resource
	private BINOLCM27_BL binOLCM27_BL;
	
	/**
	 * 会员索引管理画面初期化
	 */
	public String init() throws Exception {
		
		return SUCCESS;
	}
	
	/**
	 * 会员数据创建索引处理
	 */
	public void createMemIndex() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// 添加会员索引数据
		//binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMINFO, CherryConstants.WS_TYPE_POST, map);
	}
	
	/**
	 * 销售数据创建索引处理
	 */
	public void createSaleIndex() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// 添加销售索引数据
		//binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMSALEINFO, CherryConstants.WS_TYPE_POST, map);
	}
	
	/**
	 * 会员数据更新索引处理
	 */
	public void updMemIndex() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// 更新全部会员索引数据
		//binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMINFO, CherryConstants.WS_TYPE_PUT, map);
	}
	
	/**
	 * 销售数据更新索引处理
	 */
	public void updSaleIndex() throws Exception {
		
		Map<String, Object> map = (Map)Bean2Map.toHashMap(form);
		// 登陆用户信息
		UserInfo userInfo = (UserInfo) session
				.get(CherryConstants.SESSION_USERINFO);
		// 所属组织
		map.put(CherryConstants.ORGANIZATIONINFOID, userInfo.getBIN_OrganizationInfoID());
		map.put(CherryConstants.ORG_CODE, userInfo.getOrganizationInfoCode());
		// 不是总部的场合
		if(userInfo.getBIN_BrandInfoID() != CherryConstants.BRAND_INFO_ID_VALUE) {
			// 所属品牌
			map.put(CherryConstants.BRANDINFOID, userInfo.getBIN_BrandInfoID());
			map.put(CherryConstants.BRAND_CODE, userInfo.getBrandCode());
		}
		// 更新全部会员销售数据
		//binOLCM27_BL.accessCherryWebService(CherryConstants.WS_MEMSALEINFO, CherryConstants.WS_TYPE_PUT, map);
	}
	
	/**  */
	private BINOLPLSCF16_Form form = new BINOLPLSCF16_Form();

	@Override
	public BINOLPLSCF16_Form getModel() {
		return form;
	}

}
